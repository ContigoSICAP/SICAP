/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.commands.Command;
import com.sicap.clientes.dao.AjusteCreditoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class CommandAjusteCreditos  implements Command {
    
    private String siguiente;

    public CommandAjusteCreditos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification mensaje[] = new Notification[1];
        int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
        int numCiclo = Integer.parseInt(request.getParameter("numCiclo"));
        String strFechaAjuste = request.getParameter("Fecha");
        int estatus = 0;
        int tipoEstatus = Integer.parseInt(request.getParameter("tipoEstatus"));
        int numMultas = Integer.parseInt(request.getParameter("numMultas"));
        if(request.getParameter("estatus") != null)
            estatus = 1;
        BitacoraUtil bitacora = new BitacoraUtil(idGrupo, request.getRemoteUser(), "CommandAjusteCreditos");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaAjuste = sdf.parse(strFechaAjuste);
            AjusteCreditoDAO ajusteDAO = new AjusteCreditoDAO();
            SaldoIBSVO saldo = new SaldoIBSVO();
            ArrayList<SaldoIBSVO> arrSaldos = ajusteDAO.getSaldos(tipoEstatus, idGrupo, numCiclo);
            for (int i = 0; i < arrSaldos.size(); i++) {
                saldo = aplicaMovimientoCartera(arrSaldos.get(i), fechaAjuste, ajusteDAO, numMultas);
                arrSaldos.set(i, new SaldoIBSVO(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP(), saldo.getSaldoConInteresAlFinal(), saldo.getSaldoMulta(), saldo.getSaldoIVAMulta(),
                        saldo.getMultaPagada(), saldo.getIvaMultaPagado(), saldo.getTotalVencido(), saldo.getEstatus(), saldo.getSaldoBucket()));
            }
            if(arrSaldos.size() > 0){
                mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "Ajuste Realizado");
                request.setAttribute("saldos", arrSaldos);
                for (SaldoIBSVO saldoVO : arrSaldos) {
                    bitacora.registraEventoString("grupo="+saldoVO.getIdClienteSICAP()+", ciclo="+saldoVO.getIdSolicitudSICAP());
                }
            }else
                mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "No se realizo algun ajuste");
        } catch (Exception e) {
            Logger.debug("ERROR: Inicio en CommandAjusteCreditos\n" + e);
            mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Error en Ajuste");
        }
        request.setAttribute("NOTIFICACIONES", mensaje);
        return siguiente;
    }
    
    private SaldoIBSVO aplicaMovimientoCartera(SaldoIBSVO saldo, Date fechaAjuste, AjusteCreditoDAO ajusteDAO, int numMultas) throws ClientesException, Exception{
        
        ArrayList<TablaAmortVO> arrTabla = ajusteDAO.saldosTabla(saldo);
        ArrayList<PagoVO> arrPagos = ajusteDAO.getPagosCredito(saldo);
        FormatUtil formato = new FormatUtil();
        Convertidor frontera = new Convertidor();
        double ivaSucursal = 0.16;
        if(frontera.esFronterizo(saldo.getIdSucursal()))
            ivaSucursal = 0.11;
        double saldoCongelado = formato.roundDouble(saldo.getMontoDesembolsado()*0.10, 2);
        double garantia = 0,saldoFavor = 0;
        int i = 0, numPago = 0, numPagoTab = 0, numPagoActual = 0, estatus = 0, banco = 0, aplGaran = 0;
        boolean pasa = true, aplicaSaldo = true;
        double montoMultas = 0, montoIntereses = 0, montoCapital = 0, montoPagadar = 0, montoPago = 0, montoPagado = 0;
        double multa = 0, ivaMulta = 0, interes = 0, ivaInteres = 0, capital = 0, fraccion = 0;
        String pagado = "";
        do {
            garantia = formato.roundDouble(garantia+arrPagos.get(i).getMonto(), 2);
            //System.out.println("[*]  garantia_"+garantia);
            arrPagos.set(i, new PagoVO(arrPagos.get(i).getMonto(), arrPagos.get(i).getFechaPago(), 1, arrPagos.get(i).getBancoReferencia()));
            //System.out.println("[*]  garantia_"+garantia+" saldoCongelado_"+saldoCongelado);
            ajusteDAO.actualizaPagoCredito(arrPagos.get(i), saldo);
            numPago=i;
            i++;
        } while (garantia<=saldoCongelado);
        if(garantia>saldoCongelado)
            saldoFavor = garantia-saldoCongelado;
        saldoFavor = formato.roundDouble(saldoFavor, 2);
        //System.out.println("[*] saldoFavor_"+saldoFavor+" garantia_"+garantia+" saldoCongelado_"+saldoCongelado);
        do{
           //System.out.println("[*] numPagoTab_"+arrTabla.get(numPagoTab).getNumPago()+" MontoPagar "+arrTabla.get(numPagoTab).getMontoPagar());
            if(saldoFavor < arrTabla.get(numPagoTab).getMontoPagar()){
                numPago++;
                //System.out.println("PAGO "+arrPagos.get(numPago).getFechaPago()+" "+arrPagos.get(numPago).getMonto()+" "+arrPagos.get(numPago).getStatus());
                saldoFavor = formato.roundDouble(saldoFavor+arrPagos.get(numPago).getMonto(), 2);
                banco = arrPagos.get(numPago).getBancoReferencia();
                arrPagos.set(numPago, new PagoVO(arrPagos.get(numPago).getMonto(), arrPagos.get(numPago).getFechaPago(), 1, arrPagos.get(numPago).getBancoReferencia()));
                //System.out.println(arrPagos.get(numPago).getFechaPago()+" "+arrPagos.get(numPago).getMonto()+" "+arrPagos.get(numPago).getStatus());
                ajusteDAO.actualizaPagoCredito(arrPagos.get(numPago), saldo);
            }
            //System.out.println("PAGO: "+arrPagos.get(numPago).getMonto());
            if(saldo.getPlazo() == 12)
                aplGaran = 8;
            else if(saldo.getPlazo() == 16)
                aplGaran = 12;
            if(arrTabla.get(aplGaran).getMontoPagar()==0 && aplicaSaldo){
                saldoFavor += saldoCongelado;
                saldoCongelado = 0;
                aplicaSaldo = false;
            }
            //System.out.println("**********SALDO APLICAR "+saldoFavor);
            numMultas = aplicaMultas(arrTabla, numPagoTab, arrPagos, numPago, saldoFavor, null, numMultas);
            //System.out.println("[*] "+arrTabla.get(numPagoTab).getNumPago()+", "+arrTabla.get(numPagoTab).getFechaPago()+", "+arrTabla.get(numPagoTab).getAbonoCapital()+", "+arrTabla.get(numPagoTab).getCapitalPagado()+", "+arrTabla.get(numPagoTab).getInteres()+", "+arrTabla.get(numPagoTab).getIvaInteres()+", "+arrTabla.get(numPagoTab).getInteresPagado()+", "+arrTabla.get(numPagoTab).getIvaInteresPagado()+", "+arrTabla.get(numPagoTab).getMulta()+", "+arrTabla.get(numPagoTab).getIvaMulta()+", "+arrTabla.get(numPagoTab).getMultaPagado()+", "+arrTabla.get(numPagoTab).getIvaMultaPagado()+", "+arrTabla.get(numPagoTab).getMontoPagar()+", "+arrTabla.get(numPagoTab).getTotalPagado()+", "+arrTabla.get(numPagoTab).getStatus()+", "+arrTabla.get(numPagoTab).getPagado());
            numPagoActual = numPagoTab;
            if(arrTabla.get(numPagoTab).getPagado().equals("N")){
                estatus = 1;
                montoPagadar = formato.roundDouble(arrTabla.get(numPagoTab).getMontoPagar(), 2);
                montoPago = saldoFavor;
                montoPagado = formato.roundDouble(arrTabla.get(numPagoTab).getTotalPagado(), 2);
                pagado = arrTabla.get(numPagoTab).getPagado();
                multa = formato.roundDouble(arrTabla.get(numPagoTab).getMultaPagado(), 2);
                ivaMulta = formato.roundDouble(arrTabla.get(numPagoTab).getIvaMultaPagado(), 2);
                interes = formato.roundDouble(arrTabla.get(numPagoTab).getInteresPagado(), 2);
                ivaInteres = formato.roundDouble(arrTabla.get(numPagoTab).getIvaInteresPagado(), 2);
                capital = formato.roundDouble(arrTabla.get(numPagoTab).getCapitalPagado(), 2);
                if(arrTabla.get(numPagoTab).getMulta() != arrTabla.get(numPagoTab).getMultaPagado()){
                    montoMultas = formato.roundDouble((arrTabla.get(numPagoTab).getMulta()-arrTabla.get(numPagoTab).getMultaPagado()) + (arrTabla.get(numPagoTab).getIvaMulta()-arrTabla.get(numPagoTab).getIvaMultaPagado()), 2);
                    if(saldoFavor >= montoMultas){
                        multa = arrTabla.get(numPagoTab).getMulta();
                        ivaMulta = arrTabla.get(numPagoTab).getIvaMulta();
                        saldoFavor -= montoMultas;
                    } else {
                        multa = formato.roundDouble(arrTabla.get(numPagoTab).getMulta()-arrTabla.get(numPagoTab).getMultaPagado(), 2);
                        ivaMulta = formato.roundDouble(arrTabla.get(numPagoTab).getIvaMulta()-arrTabla.get(numPagoTab).getIvaMultaPagado(), 2);
                        fraccion = formato.roundDouble(multa/(multa+ivaMulta), 2);
                        multa = formato.roundDouble(arrTabla.get(numPagoTab).getMultaPagado()+formato.roundDouble(saldoFavor*fraccion, 2), 2);
                        ivaMulta = formato.roundDouble(arrTabla.get(numPagoTab).getIvaMultaPagado()+formato.roundDouble(saldoFavor-formato.roundDouble(saldoFavor*fraccion, 2), 2), 2);
                        saldoFavor -= multa+ivaMulta;
                        /*multa = saldoFavor-(saldoFavor*ivaSucursal);
                        ivaMulta = saldoFavor*ivaSucursal;
                        multa = formato.roundDouble(multa, 2);
                        ivaMulta = formato.roundDouble(ivaMulta, 2);*/
                    }
                }
                if(arrTabla.get(numPagoTab).getInteres() != arrTabla.get(numPagoTab).getInteresPagado()){
                    montoIntereses = (arrTabla.get(numPagoTab).getInteres()-arrTabla.get(numPagoTab).getInteresPagado()) + (arrTabla.get(numPagoTab).getIvaInteres()-arrTabla.get(numPagoTab).getIvaInteresPagado());
                    if(saldoFavor >= montoIntereses){
                        interes = arrTabla.get(numPagoTab).getInteres();
                        ivaInteres = arrTabla.get(numPagoTab).getIvaInteres();
                        saldoFavor -= montoIntereses;
                    } else {
                        interes = formato.roundDouble(arrTabla.get(numPagoTab).getInteres()-arrTabla.get(numPagoTab).getInteresPagado(), 2);
                        ivaInteres = formato.roundDouble(arrTabla.get(numPagoTab).getIvaInteres()-arrTabla.get(numPagoTab).getIvaInteresPagado(), 2);
                        fraccion = formato.roundDouble(interes/(interes+ivaInteres), 2);
                        //System.out.println("fraccion "+fraccion);
                        //System.out.println("interes "+formato.roundDouble(saldoFavor*fraccion, 2));
                        //System.out.println("ivaInteres "+formato.roundDouble(saldoFavor-formato.roundDouble(saldoFavor*fraccion, 2), 2));
                        interes = formato.roundDouble(arrTabla.get(numPagoTab).getInteresPagado()+formato.roundDouble(saldoFavor*fraccion, 2), 2);
                        ivaInteres = formato.roundDouble(arrTabla.get(numPagoTab).getIvaInteresPagado()+formato.roundDouble(saldoFavor-formato.roundDouble(saldoFavor*fraccion, 2), 2), 2);
                        saldoFavor -= interes+ivaInteres;
                        /*interes = saldoFavor-(saldoFavor*ivaSucursal);
                        ivaInteres = saldoFavor*ivaSucursal;
                        interes = formato.roundDouble(interes+arrTabla.get(numPagoTab).getInteresPagado(), 2);
                        ivaInteres = formato.roundDouble(ivaInteres+arrTabla.get(numPagoTab).getIvaInteresPagado(), 2);*/
                    }
                }
                if(arrTabla.get(numPagoTab).getAbonoCapital() != arrTabla.get(numPagoTab).getCapitalPagado()){
                    montoCapital = arrTabla.get(numPagoTab).getAbonoCapital()-arrTabla.get(numPagoTab).getCapitalPagado();
                    if(saldoFavor >= montoCapital){
                        capital = arrTabla.get(numPagoTab).getAbonoCapital();
                        saldoFavor -= montoCapital;
                    } else {
                        if(saldoFavor > 0)
                            capital = saldoFavor+arrTabla.get(numPagoTab).getCapitalPagado();
                        else
                            capital = arrTabla.get(numPagoTab).getCapitalPagado();
                        saldoFavor = 0;
                    }
                    capital = formato.roundDouble(capital, 2);
                }
                //System.out.println("montoPago_"+montoPago+" montoPagadar_"+montoPagadar);
                if(montoPago >= montoPagadar){
                    //if(arrTabla.get(numPagoTab).getNumPago() == 16){
                    if(arrTabla.get(numPagoTab).getNumPago() == saldo.getPlazo()){
                        //System.out.println("montoPago_"+montoPago+" montoPagadar_"+montoPagadar+" banco_"+banco);
                        if(montoPago == montoPagadar && banco == 7)
                            saldoFavor = 0;
                    }
                    montoPagado += montoPagadar;
                    montoPagadar = 0;
                    if(estatus==0)
                        estatus = 1;
                    pagado = "S";
                    numPagoTab++;
                } else {
                    montoPagado += montoPago;
                    montoPagadar -= montoPago;
                    saldoFavor = 0;
                }
                //System.out.println(arrTabla.get(numPagoActual).getNumPago()+" "+arrTabla.get(numPagoTab).getStatus()+" estatus "+estatus);
                if(arrTabla.get(numPagoActual).getStatus() != 0)
                    estatus = arrTabla.get(numPagoActual).getStatus();
                montoPagado = formato.roundDouble(montoPagado, 2);
                montoPagadar = formato.roundDouble(montoPagadar, 2);
                saldoFavor = formato.roundDouble(saldoFavor, 2);
                arrTabla.set(numPagoActual, new TablaAmortVO(arrTabla.get(numPagoActual).getNumPago(), arrTabla.get(numPagoActual).getFechaPago(), arrTabla.get(numPagoActual).getAbonoCapital(), capital,
                        arrTabla.get(numPagoActual).getInteres(), arrTabla.get(numPagoActual).getIvaInteres(), interes, ivaInteres, arrTabla.get(numPagoActual).getMulta(), arrTabla.get(numPagoActual).getIvaMulta(),
                        multa, ivaMulta, montoPagadar, montoPagado, estatus, pagado, arrTabla.get(numPagoActual).getCondonacion()));
                //System.out.println("SALDO FAVOR: "+saldoFavor);
            } else{
                numPagoTab++;
            }
            //System.out.println("[**] "+arrTabla.get(numPagoActual).getNumPago()+", "+arrTabla.get(numPagoActual).getFechaPago()+", "+arrTabla.get(numPagoActual).getAbonoCapital()+", "+arrTabla.get(numPagoActual).getCapitalPagado()+", "+arrTabla.get(numPagoActual).getInteres()+", "+arrTabla.get(numPagoActual).getIvaInteres()+", "+arrTabla.get(numPagoActual).getInteresPagado()+", "+arrTabla.get(numPagoActual).getIvaInteresPagado()+", "+arrTabla.get(numPagoActual).getMulta()+", "+arrTabla.get(numPagoActual).getIvaMulta()+", "+arrTabla.get(numPagoActual).getMultaPagado()+", "+arrTabla.get(numPagoActual).getIvaMultaPagado()+", "+arrTabla.get(numPagoActual).getMontoPagar()+", "+arrTabla.get(numPagoActual).getTotalPagado()+", "+arrTabla.get(numPagoActual).getStatus()+", "+arrTabla.get(numPagoActual).getPagado());
            //System.out.println("saldoFavor : "+saldoFavor);
            //System.out.println("saldo.getPlazo()_"+saldo.getPlazo()+" == numPagoTab_"+numPagoTab+" || arrPagos.size()_"+arrPagos.size()+" == (numPago+1)_"+(numPago+1));
            if(saldo.getPlazo() == numPagoTab || arrPagos.size() == (numPago+1))
                pasa = false;
        } while(pasa);
        for (PagoVO pagoVO : arrPagos) {
            if(pagoVO.getEnviado() == 0)
                saldoFavor += pagoVO.getMonto();
        }
        for(i=numPagoActual; i<arrTabla.size();i++){
            estatus = 0;
            if(saldo.getPlazo() == 12){
                if((arrTabla.get(10).getFechaPago().before(fechaAjuste) || arrTabla.get(8).getMontoPagar()==0) && aplicaSaldo){
                    saldoFavor += saldoCongelado;
                    saldoCongelado = 0;
                    aplicaSaldo = false;
                }
            }
            if(saldo.getPlazo() == 16){
                if((arrTabla.get(14).getFechaPago().before(fechaAjuste) || arrTabla.get(12).getMontoPagar()==0) && aplicaSaldo){
                    saldoFavor += saldoCongelado;
                    saldoCongelado = 0;
                    aplicaSaldo = false;
                }
            }
            if(arrTabla.get(i).getPagado().equals("N")){
                //System.out.println("FECHA PAGO "+arrTabla.get(i).getNumPago()+" SALDO A FAVOR "+saldoFavor);
                numMultas = aplicaMultas(arrTabla, i, arrPagos, 0, saldoFavor, fechaAjuste, numMultas);
                //System.out.println("[***] "+arrTabla.get(i).getNumPago()+", "+arrTabla.get(i).getFechaPago()+", "+arrTabla.get(i).getAbonoCapital()+", "+arrTabla.get(i).getCapitalPagado()+", "+arrTabla.get(i).getInteres()+", "+arrTabla.get(i).getIvaInteres()+", "+arrTabla.get(i).getInteresPagado()+", "+arrTabla.get(i).getIvaInteresPagado()+", "+arrTabla.get(i).getMulta()+", "+arrTabla.get(i).getIvaMulta()+", "+arrTabla.get(i).getMultaPagado()+", "+arrTabla.get(i).getIvaMultaPagado()+", "+arrTabla.get(i).getMontoPagar()+", "+arrTabla.get(i).getTotalPagado()+", "+arrTabla.get(i).getStatus()+", "+arrTabla.get(i).getPagado()+"\n");
                if(saldoFavor>0){
                    montoPagadar = formato.roundDouble(arrTabla.get(i).getMontoPagar(), 2);
                    montoPago = saldoFavor;
                    montoPagado = formato.roundDouble(arrTabla.get(i).getTotalPagado(), 2);
                    pagado = arrTabla.get(i).getPagado();
                    multa = formato.roundDouble(arrTabla.get(i).getMultaPagado(), 2);
                    ivaMulta = formato.roundDouble(arrTabla.get(i).getIvaMultaPagado(), 2);
                    interes = formato.roundDouble(arrTabla.get(i).getInteresPagado(), 2);
                    ivaInteres = formato.roundDouble(arrTabla.get(i).getIvaInteresPagado(), 2);
                    capital = formato.roundDouble(arrTabla.get(i).getCapitalPagado(), 2);
                    if(arrTabla.get(i).getMulta() != arrTabla.get(i).getMultaPagado()){
                        montoMultas = (arrTabla.get(i).getMulta()-arrTabla.get(i).getMultaPagado()) + (arrTabla.get(i).getIvaMulta()-arrTabla.get(i).getIvaMultaPagado());
                        if(saldoFavor >= montoMultas){
                            multa = arrTabla.get(i).getMulta();
                            ivaMulta = arrTabla.get(i).getIvaMulta();
                            saldoFavor -= montoMultas;
                        } else {
                            multa = formato.roundDouble(arrTabla.get(numPagoTab).getMulta()-arrTabla.get(numPagoTab).getMultaPagado(), 2);
                            ivaMulta = formato.roundDouble(arrTabla.get(numPagoTab).getIvaMulta()-arrTabla.get(numPagoTab).getIvaMultaPagado(), 2);
                            fraccion = formato.roundDouble(multa/(multa+ivaMulta), 2);
                            multa = formato.roundDouble(arrTabla.get(numPagoTab).getMultaPagado()+formato.roundDouble(saldoFavor*fraccion, 2), 2);
                            ivaMulta = formato.roundDouble(arrTabla.get(numPagoTab).getIvaMultaPagado()+formato.roundDouble(saldoFavor-formato.roundDouble(saldoFavor*fraccion, 2), 2), 2);
                            saldoFavor -= multa+ivaMulta;
                            /*multa = saldoFavor-(saldoFavor*ivaSucursal);
                            ivaMulta = saldoFavor*ivaSucursal;
                            multa = formato.roundDouble(multa, 2);
                            ivaMulta = formato.roundDouble(ivaMulta, 2);*/
                        }
                    }
                    if(arrTabla.get(i).getInteres() != arrTabla.get(i).getInteresPagado()){
                        montoIntereses = (arrTabla.get(i).getInteres()-arrTabla.get(i).getInteresPagado()) + (arrTabla.get(i).getIvaInteres()-arrTabla.get(i).getIvaInteresPagado());
                        if(saldoFavor >= montoIntereses){
                            interes = arrTabla.get(numPagoTab).getInteres();
                            ivaInteres = arrTabla.get(numPagoTab).getIvaInteres();
                            saldoFavor -= montoIntereses;
                        } else {
                            interes = formato.roundDouble(arrTabla.get(numPagoTab).getInteres()-arrTabla.get(numPagoTab).getInteresPagado(), 2);
                            ivaInteres = formato.roundDouble(arrTabla.get(numPagoTab).getIvaInteres()-arrTabla.get(numPagoTab).getIvaInteresPagado(), 2);
                            fraccion = formato.roundDouble(interes/(interes+ivaInteres), 2);
                            interes = formato.roundDouble(arrTabla.get(numPagoTab).getInteresPagado()+formato.roundDouble(saldoFavor*fraccion, 2), 2);
                            ivaInteres = formato.roundDouble(arrTabla.get(numPagoTab).getIvaInteresPagado()+formato.roundDouble(saldoFavor-formato.roundDouble(saldoFavor*fraccion, 2), 2), 2);
                            saldoFavor -= interes+ivaInteres;
                            /*interes = saldoFavor-(saldoFavor*ivaSucursal);
                            ivaInteres = saldoFavor*ivaSucursal;
                            interes = formato.roundDouble(interes+arrTabla.get(numPagoTab).getInteresPagado(), 2);
                            ivaInteres = formato.roundDouble(ivaInteres+arrTabla.get(numPagoTab).getIvaInteresPagado(), 2);*/
                        }
                    }
                    if(arrTabla.get(i).getAbonoCapital() != arrTabla.get(i).getCapitalPagado()){
                        montoCapital = arrTabla.get(i).getAbonoCapital()-arrTabla.get(i).getCapitalPagado();
                        if(saldoFavor >= montoCapital){
                            capital = arrTabla.get(i).getAbonoCapital();
                            saldoFavor -= montoCapital;
                        } else {
                            if(saldoFavor > 0)
                                capital = saldoFavor+arrTabla.get(i).getCapitalPagado();
                            else
                                capital = 0;
                            saldoFavor = 0;
                        }
                        capital = formato.roundDouble(capital, 2);
                    }
                    //System.out.println("montoPago_"+montoPago+" montoPagadar_"+montoPagadar);
                    estatus = arrTabla.get(i).getStatus();
                    if(montoPago >= montoPagadar){
                        montoPagado += montoPagadar;
                        montoPagadar = 0;
                        if(estatus==0)
                            estatus = 1;
                        pagado = "S";
                        numPagoTab++;
                    } else {
                        montoPagado += montoPago;
                        montoPagadar -= montoPago;
                        if(estatus==0)
                            estatus = 1;
                        saldoFavor = 0;
                    }
                    montoPagado = formato.roundDouble(montoPagado, 2);
                    montoPagadar = formato.roundDouble(montoPagadar, 2);
                    saldoFavor = formato.roundDouble(saldoFavor, 2);
                    arrTabla.set(i, new TablaAmortVO(arrTabla.get(i).getNumPago(), arrTabla.get(i).getFechaPago(), arrTabla.get(i).getAbonoCapital(), capital,
                            arrTabla.get(i).getInteres(), arrTabla.get(i).getIvaInteres(), interes, ivaInteres, arrTabla.get(i).getMulta(), arrTabla.get(i).getIvaMulta(),
                            multa, ivaMulta, montoPagadar, montoPagado, estatus, pagado, arrTabla.get(i).getCondonacion()));
                    //System.out.println("[****] "+arrTabla.get(i).getNumPago()+", "+arrTabla.get(i).getFechaPago()+", "+arrTabla.get(i).getAbonoCapital()+", "+arrTabla.get(i).getCapitalPagado()+", "+arrTabla.get(i).getInteres()+", "+arrTabla.get(i).getIvaInteres()+", "+arrTabla.get(i).getInteresPagado()+", "+arrTabla.get(i).getIvaInteresPagado()+", "+arrTabla.get(i).getMulta()+", "+arrTabla.get(i).getIvaMulta()+", "+arrTabla.get(i).getMultaPagado()+", "+arrTabla.get(i).getIvaMultaPagado()+", "+arrTabla.get(i).getMontoPagar()+", "+arrTabla.get(i).getTotalPagado()+", "+arrTabla.get(i).getStatus()+", "+arrTabla.get(i).getPagado()+"\n");
                } else {
                    if(arrTabla.get(i).getStatus() != 0)
                        estatus = arrTabla.get(i).getStatus();
                    if(arrTabla.get(i).getFechaPago().equals(fechaAjuste) || arrTabla.get(i).getFechaPago().after(fechaAjuste)){
                        if(FechasUtil.inBetweenDays(fechaAjuste, arrTabla.get(i).getFechaPago()) <= 7)
                            estatus = 1;
                    }
                    arrTabla.set(i, new TablaAmortVO(arrTabla.get(i).getNumPago(), arrTabla.get(i).getFechaPago(), arrTabla.get(i).getAbonoCapital(), arrTabla.get(i).getCapitalPagado(),
                            arrTabla.get(i).getInteres(), arrTabla.get(i).getIvaInteres(), arrTabla.get(i).getInteresPagado(), arrTabla.get(i).getIvaInteresPagado(), arrTabla.get(i).getMulta(),
                            arrTabla.get(i).getIvaMulta(), arrTabla.get(i).getMultaPagado(), arrTabla.get(i).getIvaMultaPagado(), arrTabla.get(i).getMontoPagar(), arrTabla.get(i).getTotalPagado(),
                            estatus, arrTabla.get(i).getPagado(), arrTabla.get(i).getCondonacion()));
                }
            }
        }
        /*System.out.println("********************************************************************************************************");
        for (TablaAmortVO tabla : arrTabla) {
            System.out.println("[***] "+tabla.getNumPago()+", "+tabla.getFechaPago()+", "+tabla.getAbonoCapital()+", "+tabla.getCapitalPagado()+", "+tabla.getInteres()+", "+tabla.getIvaInteres()+", "+tabla.getInteresPagado()+", "+tabla.getIvaInteresPagado()+", "+tabla.getMulta()+", "+tabla.getIvaMulta()+", "+tabla.getMultaPagado()+", "+tabla.getIvaMultaPagado()+", "+tabla.getMontoPagar()+", "+tabla.getTotalPagado()+", "+tabla.getStatus()+", "+tabla.getPagado()+", "+tabla.getCondonacion());
        }*/
         return actualizaBase(arrTabla, saldoFavor, saldo, arrPagos.get(numPago).getFechaPago(), saldoCongelado, fechaAjuste, numPago, ajusteDAO);
    }
    
    private int aplicaMultas(ArrayList<TablaAmortVO> arrTabla, int numPagoTab, ArrayList<PagoVO> arrPagos, int numPago, double saldoFavor, Date fechaAjuste, int numMultas){
        
        Date fechaAPagar = arrTabla.get(numPagoTab).getFechaPago();
        Date fechaPago = arrPagos.get(numPago).getFechaPago();
        if(fechaAjuste!=null)
            fechaPago = fechaAjuste;
        int difDias = FechasUtil.inBetweenDays(fechaAPagar, fechaPago);
        double montoPagado = arrTabla.get(numPagoTab).getTotalPagado();
        double montoPagar = arrTabla.get(numPagoTab).getMontoPagar();
        double multa = arrTabla.get(numPagoTab).getMulta();
        double ivaMulta = arrTabla.get(numPagoTab).getIvaMulta();
        double multaPagada = arrTabla.get(numPagoTab).getMultaPagado();
        double ivaMultaPagada = arrTabla.get(numPagoTab).getIvaMultaPagado();
        double interes = arrTabla.get(numPagoTab).getInteresPagado();
        double ivaInteres = arrTabla.get(numPagoTab).getIvaInteresPagado();
        double capital = arrTabla.get(numPagoTab).getCapitalPagado();
        boolean cobraMulta = true;
        int condonacion = arrTabla.get(numPagoTab).getCondonacion();
        int estatus = arrTabla.get(numPagoTab).getStatus();
        //System.out.println("[**] fechaAPagar_"+fechaAPagar+" fechaPago "+fechaPago+" difDias "+difDias+" saldoFavor "+saldoFavor+" arrTabla.get(numPagoTab).getMontoPagar() "+arrTabla.get(numPagoTab).getMontoPagar());
        //System.out.println("[*] "+arrTabla.get(numPagoTab).getAbonoCapital()+", "+arrTabla.get(numPagoTab).getCapitalPagado()+", "+arrTabla.get(numPagoTab).getInteres()+", "+arrTabla.get(numPagoTab).getIvaInteres()+", "+arrTabla.get(numPagoTab).getInteresPagado()+", "+arrTabla.get(numPagoTab).getIvaInteresPagado()+", "+arrTabla.get(numPagoTab).getMulta()+", "+arrTabla.get(numPagoTab).getIvaMulta()+", "+arrTabla.get(numPagoTab).getMultaPagado()+", "+arrTabla.get(numPagoTab).getIvaMultaPagado()+", "+arrTabla.get(numPagoTab).getMontoPagar()+", "+arrTabla.get(numPagoTab).getTotalPagado()+", "+arrTabla.get(numPagoTab).getStatus()+", "+arrTabla.get(numPagoTab).getPagado()+", "+arrTabla.get(numPagoTab).getCondonacion());
        if(fechaPago.after(fechaAPagar) && difDias >= 3){
            //System.out.println("1)numMultas_"+numMultas);
            if(condonacion == 1){
                cobraMulta = false;
            } else{
                if(numMultas > 0){
                    numMultas--;
                    condonacion = 1;
                    cobraMulta = false;
                }
            }
            if(arrTabla.get(numPagoTab).getMulta()==0 && cobraMulta){
                if(arrTabla.get(numPagoTab).getTotalPagado() != 0){
                    if(arrTabla.get(numPagoTab).getTotalPagado()>=100){
                        montoPagar += 100;
                        multa = 86.21;
                        ivaMulta = 13.79;
                        estatus = 2;
                    }
                } else {
                    montoPagar += 100;
                    multa = 86.21;
                    ivaMulta = 13.79;
                    estatus = 2;
                }
                /*arrTabla.set(numPagoTab, new TablaAmortVO(arrTabla.get(numPagoTab).getNumPago(), arrTabla.get(numPagoTab).getFechaPago(), arrTabla.get(numPagoTab).getAbonoCapital(),
                        capital, arrTabla.get(numPagoTab).getInteres(), arrTabla.get(numPagoTab).getIvaInteres(),interes, ivaInteres, 86.21, 13.79, multa, ivaMulta, 
                        montoPagar, montoPagado, 2,arrTabla.get(numPagoTab).getPagado(), condonacion));*/
            }
            arrTabla.set(numPagoTab, new TablaAmortVO(arrTabla.get(numPagoTab).getNumPago(), arrTabla.get(numPagoTab).getFechaPago(), arrTabla.get(numPagoTab).getAbonoCapital(),
                        capital, arrTabla.get(numPagoTab).getInteres(), arrTabla.get(numPagoTab).getIvaInteres(),interes, ivaInteres, multa, ivaMulta, multaPagada, ivaMultaPagada, 
                        montoPagar, montoPagado, estatus,arrTabla.get(numPagoTab).getPagado(), condonacion));
        } else if (fechaPago.after(fechaAPagar)){
            arrTabla.set(numPagoTab, new TablaAmortVO(arrTabla.get(numPagoTab).getNumPago(), arrTabla.get(numPagoTab).getFechaPago(), arrTabla.get(numPagoTab).getAbonoCapital(),
                        capital, arrTabla.get(numPagoTab).getInteres(), arrTabla.get(numPagoTab).getIvaInteres(),interes, ivaInteres, multa, ivaMulta, multaPagada, ivaMultaPagada, 
                        montoPagar, montoPagado, 2,arrTabla.get(numPagoTab).getPagado(), condonacion));
        }
        //System.out.println("2)numMultas_"+numMultas);
        //System.out.println("[**] "+arrTabla.get(numPagoTab).getAbonoCapital()+", "+arrTabla.get(numPagoTab).getCapitalPagado()+", "+arrTabla.get(numPagoTab).getInteres()+", "+arrTabla.get(numPagoTab).getIvaInteres()+", "+arrTabla.get(numPagoTab).getInteresPagado()+", "+arrTabla.get(numPagoTab).getIvaInteresPagado()+", "+arrTabla.get(numPagoTab).getMulta()+", "+arrTabla.get(numPagoTab).getIvaMulta()+", "+arrTabla.get(numPagoTab).getMultaPagado()+", "+arrTabla.get(numPagoTab).getIvaMultaPagado()+", "+arrTabla.get(numPagoTab).getMontoPagar()+", "+arrTabla.get(numPagoTab).getTotalPagado()+", "+arrTabla.get(numPagoTab).getStatus()+", "+arrTabla.get(numPagoTab).getPagado()+", "+arrTabla.get(numPagoTab).getCondonacion());
        return numMultas;
    }
    
    private SaldoIBSVO actualizaBase (ArrayList<TablaAmortVO> arrTabla, double saldoFavor, SaldoIBSVO saldo, Date fechaUltPago, double saldoCongelado, Date fechaAjuste, int numPagos, AjusteCreditoDAO ajusteDAO){
        
        java.sql.Date sqlFechaAjuste = new java.sql.Date(fechaAjuste.getTime());
        FormatUtil formato = new FormatUtil();
        double capitalPagado = 0, interesPagado = 0, ivaInteresPagado = 0, multaPagado = 0, ivaMultaPagado = 0, totalPagado = 0;
        double capitalPagar = 0, interesPagar = 0, ivaInteresPagar = 0, multaPagar = 0, ivaMultaPagar = 0, totalPagar = 0;
        double capitalVen = 0, totalVen = 0;
        double capitalAct = 0, interesAct = 0, ivaInteresAct = 0;
        Date fechaAct = null, fechaVencida = null;
        int estatus = 0, diasTrans = 0, diasMora = 0, cuotasVen = 0, cuotasTrans = 0;
        for (TablaAmortVO amortizacion : arrTabla) {
            /*capitalAct = 0;
            interesAct = 0;
            ivaInteresAct = 0;
            fechaAct = amortizacion.getFechaPago();*/
            if(amortizacion.getPagado().equals("S")){
                capitalAct = 0;
                interesAct = 0;
                ivaInteresAct = 0;
                fechaAct = amortizacion.getFechaPago();
                capitalPagado += amortizacion.getCapitalPagado();
                interesPagado += amortizacion.getInteresPagado();
                ivaInteresPagado += amortizacion.getIvaInteresPagado();
                multaPagado += amortizacion.getMultaPagado();
                ivaMultaPagado += amortizacion.getIvaMultaPagado();
                totalPagado += amortizacion.getTotalPagado();
                estatus = 1;
                cuotasTrans = amortizacion.getNumPago();
                cuotasVen = 0;
            } else if (amortizacion.getPagado().equals("N")){
                capitalPagar += amortizacion.getAbonoCapital()-amortizacion.getCapitalPagado();
                capitalPagado += amortizacion.getCapitalPagado();
                //interesPagar += amortizacion.getInteres()-amortizacion.getInteresPagado();
                //ivaInteresPagar += amortizacion.getIvaInteres()-amortizacion.getIvaInteresPagado();
                interesPagado += amortizacion.getInteresPagado();
                ivaInteresPagado += amortizacion.getIvaInteresPagado();
                multaPagar += amortizacion.getMulta()-amortizacion.getMultaPagado();
                ivaMultaPagar += amortizacion.getIvaMulta()-amortizacion.getIvaMultaPagado();
                multaPagado += amortizacion.getMultaPagado();
                ivaMultaPagado += amortizacion.getIvaMultaPagado();
                totalPagar += amortizacion.getMontoPagar();
                totalPagado += amortizacion.getTotalPagado();
                if(amortizacion.getStatus() == 2){
                    fechaAct = amortizacion.getFechaPago();
                    capitalVen = capitalPagar;
                    interesPagar += amortizacion.getInteres()-amortizacion.getInteresPagado();
                    ivaInteresPagar += amortizacion.getIvaInteres()-amortizacion.getIvaInteresPagado();
                    totalVen = totalPagar;
                    cuotasTrans = amortizacion.getNumPago();
                    if(fechaVencida == null)
                        fechaVencida = amortizacion.getFechaPago();
                    cuotasVen++;
                    estatus = 2;
                }
                if(amortizacion.getStatus() == 1){
                    capitalAct = amortizacion.getAbonoCapital()-amortizacion.getCapitalPagado();
                    interesAct = amortizacion.getInteres()-amortizacion.getInteresPagado();
                    ivaInteresAct = amortizacion.getIvaInteres()-amortizacion.getIvaInteresPagado();
                    fechaAct = amortizacion.getFechaPago();
                    if(capitalVen == 0)
                        estatus = 1;
                }
            }
            /*System.out.println("UPDATE cf_cartera_db.D_TABLA_AMORTIZACION SET "+
                    "TA_CAPITAL_PAGADO="+amortizacion.getCapitalPagado()+","+
                    "TA_INTERES_PAGADO="+amortizacion.getInteresPagado()+","+
                    "TA_IVA_INTERES_PAGADO="+amortizacion.getIvaInteresPagado()+","+
                    "TA_MULTA="+amortizacion.getMulta()+","+
                    "TA_IVA_MULTA="+amortizacion.getIvaMulta()+","+
                    "TA_MULTA_PAGADO="+amortizacion.getMultaPagado()+","+
                    "TA_IVA_MULTA_PAGADO="+amortizacion.getIvaMultaPagado()+","+
                    "TA_MONTO_PAGAR="+amortizacion.getMontoPagar()+","+
                    "TA_MONTO_PAGADO="+amortizacion.getTotalPagado()+","+
                    "TA_STATUS="+amortizacion.getStatus()+","+
                    "TA_PAGADO='"+amortizacion.getPagado()+"' "+
                    "WHERE TA_NUMCLIENTE="+saldo.getIdClienteSICAP()+" AND TA_NUMCREDITO="+saldo.getCredito()+" AND TA_NUMPAGO="+amortizacion.getNumPago()+";");*/
            ajusteDAO.actualizaTablaAmort(amortizacion, saldo);
        }
        if(saldo.getPlazo() == 12){
            if(arrTabla.get(11).getPagado().equals("S"))
                estatus = 3;
        }
        if(saldo.getPlazo() == 16){
            if(arrTabla.get(15).getPagado().equals("S"))
                estatus = 3;
        }
        diasTrans = FechasUtil.inBetweenDays(saldo.getFechaDesembolso(), fechaAjuste);
        if(cuotasVen != 0)
            diasMora = FechasUtil.inBetweenDays(fechaVencida, fechaAjuste);
        if(saldoCongelado!=0)
            saldoFavor += saldoCongelado;
        //if(cuotasTrans == 16 && diasMora > 120)
        if(cuotasTrans == saldo.getPlazo() && diasMora > 120)
            estatus = 6;
        capitalPagar = formato.roundDouble(capitalPagar, 2);
        capitalPagado = formato.roundDouble(capitalPagado, 2);
        interesPagar = formato.roundDouble(interesPagar, 2);
        ivaInteresPagar = formato.roundDouble(ivaInteresPagar, 2);
        interesPagado = formato.roundDouble(interesPagado, 2);
        ivaInteresPagado = formato.roundDouble(ivaInteresPagado, 2);
        multaPagar = formato.roundDouble(multaPagar, 2);
        ivaMultaPagar = formato.roundDouble(ivaMultaPagar, 2);
        multaPagado = formato.roundDouble(multaPagado, 2);
        ivaMultaPagado = formato.roundDouble(ivaMultaPagado, 2);
        totalPagar = formato.roundDouble(totalPagar, 2);
        totalPagado = formato.roundDouble(totalPagado, 2);
        capitalVen = formato.roundDouble(capitalVen, 2);
        totalVen = formato.roundDouble(totalVen, 2);
        /*System.out.println("UPDATE cf_cartera_db.d_credito SET "+
                            "CR_MONTO_CUENTA="+saldoFavor+","+
                            "CR_NUM_DIAS="+diasTrans+","+
                            "CR_DIAS_MORA="+diasMora+","+
                            "CR_STATUS="+estatus+","+
                            "CR_MONTO_CUENTA_CONGELADA="+saldoCongelado+" "+
                            "WHERE CR_NUM_CLIENTE="+saldo.getIdClienteSICAP()+" AND CR_NUM_CREDITO="+saldo.getCredito()+";");
        System.out.println("UPDATE clientes.d_saldos SET "+
                            "IB_FECHA_GENERACION='"+sqlFechaAjuste+"',"+
                            "ib_num_cuotas_trancurridas="+cuotasTrans+","+
                            "ib_saldo_total_dia="+totalPagar+","+
                            "ib_saldo_capital="+capitalPagar+","+
                            "ib_saldo_interes="+(interesPagar+interesAct)+","+
                            "ib_saldo_interes_vigente="+interesAct+","+
                            "ib_saldo_interes_vencido="+interesPagar+","+
                            "ib_saldo_iva_interes="+(ivaInteresPagar+ivaInteresAct)+","+
                            "ib_saldo_multa="+multaPagar+","+
                            "ib_saldo_iva_multa="+ivaMultaPagar+","+
                            "ib_capital_pagado="+capitalPagado+","+
                            "ib_interes_normal_pagado="+interesPagado+","+
                            "ib_iva_interes_normal_pagado="+ivaInteresPagado+","+
                            "ib_multa_pagada="+multaPagado+","+
                            "ib_iva_multa_pagada="+ivaMultaPagado+","+
                            "ib_fecha_sig_amortizacion='"+fechaAct+"',"+
                            "ib_capital_sig_amortizacion="+capitalAct+","+
                            "ib_interes_sig_amortizacion="+interesAct+","+
                            "ib_iva_interes_sig_amortizacion="+ivaInteresAct+","+
                            "ib_saldo_con_intereses_al_final="+totalPagar+","+
                            "ib_capital_vencido="+capitalVen+","+
                            "ib_total_vencido="+totalVen+","+
                            "ib_estatus="+estatus+","+
                            "ib_num_dias_mora="+diasMora+","+
                            "ib_dias_transcurridos="+diasTrans+","+
                            "ib_cuotas_vencidas="+cuotasVen+","+
                            "ib_num_pagos_realizados="+(numPagos+1)+","+
                            "ib_moto_total_pagado="+totalPagado+","+
                            "ib_fecha_ultimo_pago='"+fechaUltPago+"',"+
                            "ib_saldo_bucket="+saldoFavor+" "+
                            "WHERE IB_NUMCLIENTESICAP="+saldo.getIdClienteSICAP()+" AND IB_CREDITO="+saldo.getCredito()+
                            ";");*/
        saldo.setFechaGeneracion(sqlFechaAjuste);
        saldo.setNumeroCuotasTranscurridas(cuotasTrans);
        //saldo.setSaldoTotalAlDia(totalPagar);
        saldo.setSaldoTotalAlDia(capitalPagar+multaPagar+ivaMultaPagar+interesPagar+ivaInteresPagar+interesAct+ivaInteresAct);
        saldo.setSaldoCapital(capitalPagar);
        saldo.setSaldoInteres(interesPagar+interesAct);
        saldo.setSaldoInteresVigente(interesAct);
        saldo.setSaldoInteresVencido(interesPagar);
        saldo.setSaldoIvaInteres(ivaInteresPagar+ivaInteresAct);
        saldo.setSaldoMulta(multaPagar);
        saldo.setSaldoIVAMulta(ivaMultaPagar);
        saldo.setCapitalPagado(capitalPagado);
        saldo.setInteresNormalPagado(interesPagado);
        saldo.setIvaInteresNormalPagado(ivaInteresPagado);
        saldo.setMultaPagada(multaPagado);
        saldo.setIvaMultaPagado(ivaMultaPagado);
        saldo.setFechaSigAmortizacion(new java.sql.Date(fechaAct.getTime()));
        saldo.setCapitalSigAmortizacion(capitalAct);
        saldo.setInteresSigAmortizacion(interesAct);
        saldo.setIvaSigAmortizacion(ivaInteresAct);
        saldo.setSaldoConInteresAlFinal(totalPagar);
        saldo.setCapitalVencido(capitalVen);
        saldo.setInteresVencido(interesPagar);
        saldo.setIvaInteresVencido(ivaInteresPagar);
        saldo.setTotalVencido(totalVen);
        saldo.setEstatus(estatus);
        saldo.setDiasMora(diasMora);
        saldo.setDiasTranscurridos(diasTrans);
        saldo.setCuotasVencidas(cuotasVen);
        saldo.setNumeroPagosRealizados(numPagos+1);
        saldo.setMontoTotalPagado(totalPagado);
        saldo.setFechaUltimoPago(new java.sql.Date(fechaUltPago.getTime()));
        saldo.setSaldoBucket(saldoFavor);
        
        ajusteDAO.actualizaCredito(saldoFavor, diasTrans, diasMora, estatus, saldoCongelado, saldo);
        ajusteDAO.actualizaSaldo(saldo);
        
        return saldo;
    }
}

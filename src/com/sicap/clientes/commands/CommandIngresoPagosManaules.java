package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author avillanueva
 */
public class CommandIngresoPagosManaules implements Command{
    
    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandIngresoPagosManaules.class);

    public CommandIngresoPagosManaules(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        String comando = "";
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<PagoVO> arrPago = new ArrayList<PagoVO>();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        try {
            comando = request.getParameter("command");
            if(comando.equals("buscaPagosManual")){
                PagoVO[] lstPagos = new PagoDAO().getNoEnviados();
                if(lstPagos.length > 0){
                    for (int i = 0; i < lstPagos.length; i++) {
                        //System.out.println("lstPagos "+lstPagos[i].getReferencia()+" "+lstPagos[i].getMonto()+" "+lstPagos[i].getFechaPago()+" "+lstPagos[i].getBancoReferencia()+" "+lstPagos[i].getFechaHora());
                        arrPago.add(lstPagos[i]);
                    }
                    notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Lista de pagos sin aplicar"));
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "No se encontraron pagos aplicar"));
                }
            } else if(comando.equals("aplicaPagosManual")){
                if(!catalogoDAO.ejecutandoCierre()){
                    ArrayList<PagoVO> arrAplicaPago = new PagosReferenciadosHelper().getPagosManuales(request);
                    ArrayList<String> arrPagos = new ArrayList<String>();
                    PagoDAO pagoDAO = new PagoDAO();
                    CreditoCartVO creditoVO = new CreditoCartVO();
                    CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
                    TransaccionesHelper transHelper = new TransaccionesHelper();
                    SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                    String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
                    Date fechaUltimoCierre  = sdf.parse(ultimaFecha);
                    SaldoIBSVO saldoVO = new SaldoIBSVO();
                    SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                    ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
                    ReferenciaGeneralVO referenciaVO = new ReferenciaGeneralVO();
                    double remanente = 0, saldoCredito = 0;
                    TablaAmortDAO tablaDAO = new TablaAmortDAO();
                    TablaAmortHelper tablaHelper = new TablaAmortHelper();
                    //APLICACION DE PAGOS A MONTO CREDITO
                    for (PagoVO pago : arrAplicaPago) {
                        myLogger.debug("**PAGO "+pago.getReferencia()+" "+pago.getMonto()+" "+pago.getFechaPago()+" "+pago.getBancoReferencia());
                        if (CreditoHelperCartera.actualizaSaldoCuenta(pago)) {
                            pago.setEnviado(1);
                            pagoDAO.updatePagosCartera(pago, pago.getReferencia());
                            creditoVO = creditoCartDAO.getCreditoReferencia(pago.getReferencia());
                            transHelper.registraPago(creditoVO, pago);
                            if(!arrPagos.contains(pago.getReferencia()))
                                arrPagos.add(pago.getReferencia());
                        }
                    }
                    //SE APLICAN PAGOS A CREDITOS SIN DUPLICAR
                    myLogger.debug("--Creditos a Procesar-- "+arrPagos.size());
                    for (String pagoReferencia : arrPagos) {
                        myLogger.debug("**REFERENCIA "+pagoReferencia);
                        referenciaVO = referenciaDAO.getReferenciaGeneral(pagoReferencia);
                        saldoVO = saldoDAO.getSaldo(referenciaVO.numcliente, referenciaVO.numSolicitud, pagoReferencia);
                        creditoVO = creditoCartDAO.getCreditoClienteSol(referenciaVO.numcliente, referenciaVO.numSolicitud);
                        new CommandCierreDia("").obtenSaldoFavor(creditoVO, saldoVO, saldoVO.getFechaGeneracion());
                        /*saldoFavor = obtenSaldoFavor(creditoVO, saldoVO);*/
                        saldoCredito = saldoVO.getSaldoConInteresAlFinal();
                        //VERIFICACION DE LIQUIDACION
                        if ((creditoVO.getMontoCuenta() - creditoVO.getMontoCuentaCongelada()) >= saldoCredito) {
                            myLogger.debug("**Liquida el credito "+creditoVO.getNumCliente()+", "+creditoVO.getNumSolicitud()+", "+creditoVO.getNumCredito());
                            TablaAmortVO tablaAmortLiq;
                            tablaAmortLiq = tablaDAO.getDivVigente(creditoVO.getNumCliente(), creditoVO.getNumCredito());
                            remanente = tablaHelper.aplicaPagosTabla(creditoVO, creditoVO.getMontoCuenta() - creditoVO.getMontoCuentaCongelada(), Convertidor.toSqlDate(tablaAmortLiq.fechaPago), false, 0, Convertidor.toSqlDate(fechaUltimoCierre));
                            CreditoCartVO creditoliq = null;
                            creditoliq = creditoCartDAO.getCredito(creditoVO.getNumCredito());
                            // Solo en caso de existir un remanente despues de aplicar los pagos se llama al metod de liquidar
                            if (remanente > 0 && creditoliq.getStatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO) {
                                remanente = tablaHelper.liquidaTablaProyectado(creditoVO, remanente, Convertidor.toSqlDate(fechaUltimoCierre));
                            }
                        } else {
                            myLogger.debug("**Aplica pagos parciales "+creditoVO.getNumCliente()+", "+creditoVO.getNumSolicitud()+", "+creditoVO.getNumCredito());
                            if(saldoVO.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE)
                                TablaAmortHelper.aplicaPagosTabla(creditoVO, creditoVO.getMontoCuenta() - creditoVO.getMontoCuentaCongelada(), Convertidor.toSqlDate(fechaUltimoCierre), false, 0, Convertidor.toSqlDate((fechaUltimoCierre)));
                            else
                                notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Referencia "+pagoReferencia+" se ingreso a Saldo a Favor"));
                        }
                    }
                    notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Pago(s) aplicado(s) a Cr&eacute;dito"));
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion"));
                }
            }
            request.setAttribute("PAGOS", arrPago);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (Exception e) {
            myLogger.error("Problema en Ingreso Pagos Manuales",e);
        }
        return siguiente;
    }
    
    /*private double obtenSaldoFavor(CreditoCartVO cartera, SaldoIBSVO saldo) {

        myLogger.debug("Saldo a Favor*************");
        double saldoFavor = 0;
        //System.out.println("Credito "+saldo.getReferencia());
        //System.out.println("Cuotas "+saldo.getNumeroCuotasTranscurridas()+", Estatus "+saldo.getEstatus());
        if (cartera.getStatus() == 1 && cartera.getMontoCuenta() >= saldo.getSaldoConInteresAlFinal()) {
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 16 && saldo.getNumeroCuotasTranscurridas() >= 13 && saldo.getEstatus() == 1) {
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 12 && saldo.getNumeroCuotasTranscurridas() >= 9 && saldo.getEstatus() == 1) {
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 16 && saldo.getNumeroCuotasTranscurridas() == 15) {
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 12 && saldo.getNumeroCuotasTranscurridas() == 11) {
            cartera.setMontoCuentaCongelada(0);
        } else if ((saldo.getSaldoConInteresAlFinal() - cartera.getMontoCuenta()) < cartera.getValorCuota() && saldo.getEstatus() == 1) {
            cartera.setMontoCuentaCongelada(0);
        }
        //System.out.println("SaldoBucket "+cartera.getMontoCuenta()+", Congelado "+cartera.getMontoCuentaCongelada());
        //if(cartera.getMontoCuentaCongelada() < cartera.getMontoCuenta())
        if (cartera.getMontoCuentaCongelada() < cartera.getMontoCuenta()) //saldoFavor = saldo.getSaldoBucket() - cartera.getMontoCuentaCongelada();
        {
            saldoFavor = cartera.getMontoCuenta() - cartera.getMontoCuentaCongelada();
        }
        saldoFavor = FormatUtil.roundDouble(saldoFavor, 2);
        myLogger.debug("Saldo Favor " + saldoFavor);
        return saldoFavor;
    }*/
}

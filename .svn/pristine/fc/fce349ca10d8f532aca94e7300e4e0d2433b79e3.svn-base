/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandEliminaPagos implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandEliminaPagos.class);

    public CommandEliminaPagos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        CatalogoDAO catalogoDAO = new CatalogoDAO();
        try {
            Notification[] mensaje = new Notification[1];
            String comando = request.getParameter("command");
            ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
            String referencia = "";
            int idGrupo = 0, idCiclo = 0;            
            Date fechaCierre = FechasUtil.getRestarDias(Convertidor.stringToDate(CatalogoHelper.getParametro("FECHA_CIERRE"),ClientesConstants.FORMATO_FECHA_EU),1);            
            if (comando.equals("buscaPagosEliminar")) {
                idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
                idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                referencia = new ReferenciaGeneralDAO().getReferencia(idGrupo, idCiclo, 'G');
                arrPagos = new PagoDAO().getArrPagosByReferencia(referencia);
                if (!arrPagos.isEmpty()) {
                    mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "Pagos con Referencia " + referencia);
                } else {
                    mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "No se encontraron pagos");
                }
            } else if (comando.equals("eliminaPagos")) {
                if(!catalogoDAO.ejecutandoCierre()){
                    arrPagos = new PagosReferenciadosHelper().getEliminaPagos(request);
                    ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
                    ReferenciaGeneralVO referenciaVO = new ReferenciaGeneralVO();
                    SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                    SaldoIBSVO saldoVO = new SaldoIBSVO();
                    CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
                    CreditoCartVO creditoVO = new CreditoCartVO();
                    TablaAmortHelper tablaHelp = new TablaAmortHelper();
                    PagoDAO pagoDAO = new PagoDAO();
                    PagoGrupalDAO pagoGroDAO = new PagoGrupalDAO();
                    PagoIndividualGruposDAO pagoIndivDAO = new PagoIndividualGruposDAO();
                    PagoGrupalVO pagoGroVO = new PagoGrupalVO();
                    for (PagoVO pago : arrPagos) {
                        referenciaVO = referenciaDAO.getReferenciaGeneral(pago.getReferencia());
                        myLogger.info("referenciaVO " + referenciaVO + " " + pago.getFechaPago() + " " + pago.getMonto() + " " + pago.getBancoReferencia() + " " + pago.getEnviado());
                        saldoVO = saldoDAO.getSaldo(referenciaVO.numcliente, referenciaVO.numSolicitud, pago.getReferencia());
                        saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaCierre));
                        creditoVO = creditoCartDAO.getCreditoClienteSol(referenciaVO.numcliente, referenciaVO.numSolicitud);
                        if(pago.getEnviado() == 1){
                            /*ELIMINACION DE PAGO APLICADO A CARTERA*/
                            myLogger.info("Eliminacion de pago de cartera");
                            tablaHelp.quitaPagoTabla(creditoVO, saldoVO, pago);
                        } else {
                            /*ELIMINACION DE PAGO*/
                            myLogger.info("Eliminacion de pago");
                            pagoGroVO = pagoGroDAO.getPagoGrupalMonto(creditoVO.getNumCliente(), creditoVO.getNumSolicitud(), pago);
                            pagoDAO.deletePago(pago);
                            pagoDAO.deletePagoODS(pago);
                            pagoGroDAO.deletePagoGrupal(pagoGroVO);
                            pagoIndivDAO.deletePagoIndividualGrupal(pagoGroVO);
                        }
                    }
                    mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Eliminaci&oacute;n Aplicada");
                } else {
                    mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
                }
            }
            request.setAttribute("REFERENCIA", referencia);
            request.setAttribute("PAGOS", arrPagos);
            request.setAttribute("NOTIFICACIONES", mensaje);
            request.setAttribute("GRUPO", idGrupo);
            request.setAttribute("CICLO", idCiclo);
        } catch (Exception e) {
            myLogger.error("Exception", e);
        }
        return siguiente;
    }

}

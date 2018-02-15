package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.PaynetDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.PagosPaynetHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PaynetVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandIncidenciasPaynet implements Command{
    
    private static Logger myLogger = Logger.getLogger(CommandIncidenciasPaynet.class);
    
    private String siguiente;

    public CommandIncidenciasPaynet(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        String comando = "";
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<PaynetVO> arrPaynet = new ArrayList<PaynetVO>();
        ArrayList<PaynetVO> arrTrans = new ArrayList<PaynetVO>();
        ReferenciaGeneralVO refVO = new ReferenciaGeneralVO();
        PaynetVO pagPaynetVO = new PaynetVO();
        PaynetDAO paynetDAO = new PaynetDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        try {
            comando = request.getParameter("command");
            if(comando.equals("incidenciasPaynet")){
                ReferenciaGeneralDAO refDAO = new ReferenciaGeneralDAO();
                GrupoDAO gpoDAO = new GrupoDAO();
                String fechaIni = Convertidor.dateToString(new java.util.Date(), "yyyy-MM-dd"), fechaFin = Convertidor.dateToString(new java.util.Date(), "yyyy-MM-dd");
                String nombre = "";
                if(!request.getParameter("fechaInicial").equals("")){
                    fechaIni = request.getParameter("fechaInicial");
                    fechaIni = fechaIni.substring(6, 10)+"-"+fechaIni.substring(3, 5)+"-"+fechaIni.substring(0, 2);
                }
                if(!request.getParameter("fechaFinal").equals("")){
                    fechaFin = request.getParameter("fechaFinal");
                    fechaFin = fechaFin.substring(6, 10)+"-"+fechaFin.substring(3, 5)+"-"+fechaFin.substring(0, 2);
                }
                for (int i = 1; i < 8; i++) {
                    arrPaynet.clear();
                    if(i != ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO && i != ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_CANCEL)
                        arrPaynet = paynetDAO.getTransPaynet(fechaIni, fechaFin, i);
                    if(arrPaynet.size() > 0){
                        for (PaynetVO paynetVO : arrPaynet) {
                            refVO = refDAO.getReferenciaGeneral(paynetVO.getReferenciaPay().substring(6, 19));
                            if(refVO != null){
                                nombre = gpoDAO.getNombreGrupo(refVO.numcliente);
                                refVO.nombre = nombre;
                            }
                            arrTrans.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), refVO));
                        }
                    }
                }
                if(arrTrans.size() == 0)
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "No hay transacciones para la fechas establecidas"));
            } else if(comando.equals("envioPagoPaynet")){
                if(!catalogoDAO.ejecutandoCierre()){
                    arrPaynet = new PagosPaynetHelper().getTransaccionesPaynet(request);
                    //new PagosReferenciadosHelper().procesaPagos(arrPaynet, 0);
                    new PagosReferenciadosHelper().procesaPagos(arrPaynet, new CuentasBancariasDAO().getNumCuentaBancaria(ClientesConstants.ID_BANCO_PAYNET, "00220", "C"));
                    for (PaynetVO paynetVO : arrPaynet) {
                        pagPaynetVO = paynetDAO.getTransPaynet(paynetVO);
                        arrTrans.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), pagPaynetVO.getEstatus(), paynetVO.getRefGeneralVO()));
                    }
                    notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Transacciones Procesadas"));
                } else{
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion"));
                }
            } else if(comando.equals("confirmaPagoPaynet")){
                arrPaynet = new PagosPaynetHelper().getTransaccionesPaynet(request);
                for (PaynetVO paynetVO : arrPaynet) {
                    paynetVO.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO);
                    paynetDAO.setEstatus(paynetVO);
                    pagPaynetVO = paynetDAO.getTransPaynet(paynetVO);
                    arrTrans.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), pagPaynetVO.getEstatus(), paynetVO.getRefGeneralVO()));
                }
                notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Transacciones Procesadas"));
            } else if(comando.equals("confirmaCancelaPaynet")){
                arrPaynet = new PagosPaynetHelper().getTransaccionesPaynet(request);
                for (PaynetVO paynetVO : arrPaynet) {
                    paynetVO.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_CANCEL);
                    paynetDAO.setEstatus(paynetVO);
                    pagPaynetVO = paynetDAO.getTransPaynet(paynetVO);
                    arrTrans.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), pagPaynetVO.getEstatus(), paynetVO.getRefGeneralVO()));
                }
                notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Transacciones Procesadas"));
            }
            request.setAttribute("TRANSACCIONES", arrTrans);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (Exception e) {
            myLogger.error("Problema en Incidencias Paynet ", e);
            e.printStackTrace();
        }
        return siguiente;
    }
}

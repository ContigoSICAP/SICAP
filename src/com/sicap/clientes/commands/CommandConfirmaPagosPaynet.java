package com.sicap.clientes.commands;

import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PaynetDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.PagosPaynetHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.PaynetVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

public class CommandConfirmaPagosPaynet {
    
    private static Logger myLogger = Logger.getLogger(CommandConfirmaPagosPaynet.class);
    
    public synchronized ArrayList<PaynetVO> procesaCargaConfiracion(FileItem file, HttpServletRequest request) throws Exception {
        
        ArrayList<PaynetVO> arrPagoPaynet = new ArrayList<PaynetVO>();
        ArrayList<PaynetVO> arrPagoPen = new ArrayList<PaynetVO>();
        /*ArrayList<PaynetVO> arrPagoCartera = new ArrayList<>();
        ArrayList<PaynetVO> arrIncidenciaPay = new ArrayList<>();
        ArrayList<PaynetVO> arrIncidenciaPag = new ArrayList<>();
        ArrayList<PaynetVO> arrConfiracion = new ArrayList<>();*/
        PaynetVO pagoPaynet = new PaynetVO();
        PagoVO pagoVO = new PagoVO();
        PaynetDAO paynetDAO = new PaynetDAO();
        PagoDAO pagoDAO = new PagoDAO();
        PagoReferenciadoDAO incidenciaDAO = new PagoReferenciadoDAO();
        String[] cadena;
        String fecha = "";
        Connection con = null;
        boolean error = false;
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            arrPagoPaynet = new PagosPaynetHelper().getPagosManuales(file);
            cadena = file.getName().split("_");
            fecha = cadena[2].substring(0, 4)+"-"+cadena[2].substring(4, 6)+"-"+cadena[2].substring(6, 8);
            int count = 0;
            for (PaynetVO paynetVO : arrPagoPaynet) {
                pagoPaynet = paynetDAO.getTransPaynet(paynetVO);
                pagoVO.setReferencia(paynetVO.getReferencia());
                pagoVO.setMonto(paynetVO.getMonto());
                pagoVO.setFechaPago(paynetVO.getFechaAutPagPay());
                pagoVO.setBancoReferencia(ClientesConstants.ID_BANCO_PAYNET);
                pagoVO.setNumRegistro(pagoPaynet.getNumPago());
                switch(pagoPaynet.getEstatus()){
                    case 0:
                        arrPagoPaynet.get(count).setTipoTran("No hay registro de pago");
                        //arrIncidenciaPay.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "No hay registro de pago"));
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_RECIBIDO:
                        arrPagoPaynet.get(count).setTipoTran("Transaccion sin registro de pago");
                        /*if(pagoDAO.getPagoPayVO(pagoVO) == 0){
                            arrPagoPaynet.get(count).setTipoTran("Transaccion sin registro de pago ingreso pago al credio");
                            //arrPagoCartera.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Transaccion sin registro de pago, ingreso pago al credio"));
                        } else {
                            arrPagoPaynet.get(count).setTipoTran("Transaccion sin registro de pago ingreso incidencia");
                            //arrIncidenciaPag.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Transaccion sin registro de pago, ingreso incidencia"));
                        }*/
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_CANCELADO:
                        arrPagoPaynet.get(count).setTipoTran("La transaccion se encuentra como cancelada");
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_ENVIO_PAGOS:
                        if(pagoDAO.getPagoVO(pagoVO, con) == 1){
                            arrPagoPaynet.get(count).setTipoTran("Pago confirmado");
                            //arrConfiracion.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Pago confirmado"));
                            paynetVO.setNumPago(pagoPaynet.getNumPago());
                            paynetVO.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO);
                            if(!paynetDAO.updatePagoPaynet(paynetVO, con)){
                                error = true;
                                break;
                            }
                        }
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO:
                        arrPagoPaynet.get(count).setTipoTran("Transaccion con previa confirmacion de pago");
                        //arrIncidenciaPay.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Transaccion con previa confirmacion de pago"));
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_CANCEL:
                        arrPagoPaynet.get(count).setTipoTran("Transaccion con previa confirmacion de cancelacion");
                        //arrIncidenciaPay.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Transaccion con previa confirmacion de cancelacion"));
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_PENDIENTE:
                        if(pagoDAO.getPagoVO(pagoVO, con) == 1){
                            arrPagoPaynet.get(count).setTipoTran("Pago confirmado");
                            //arrConfiracion.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Pago confirmado"));
                            paynetVO.setNumPago(pagoPaynet.getNumPago());
                            paynetVO.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO);
                            if(!paynetDAO.updatePagoPaynet(paynetVO, con)){
                                error = true;
                                break;
                            }
                        }
                        break;
                    case ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA:
                        if(incidenciaDAO.getIncidencias(pagoVO) == 1){
                            arrPagoPaynet.get(count).setTipoTran("Pago confirmado dentro de incidencias");
                            //arrIncidenciaPag.add(new PaynetVO(paynetVO.getIdPago(), pagoPaynet.getReferenciaPay(), pagoPaynet.getMonto(), paynetVO.getFechaAutPagPay(), null, pagoPaynet.getEstatus(), "Pago confirmado dentro de incidencias"));
                            paynetVO.setNumPago(pagoPaynet.getNumPago());
                            paynetVO.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_CONFIRM_PAGO);
                            if(!paynetDAO.updatePagoPaynet(paynetVO, con)){
                                error = true;
                                break;
                            }
                        }
                        break;
                }
                count++;
            }
            if (!error) {
                con.commit();
            } else {
                con.rollback();
                arrPagoPaynet.clear();
                /*arrIncidenciaPag.clear();
                arrIncidenciaPay.clear();
                arrPagoCartera.clear();
                arrConfiracion.clear();*/
            }
            arrPagoPen = paynetDAO.getTransPaynet(fecha, fecha, ClientesConstants.ID_ESTATUS_PAYNET_RECIBIDO);
            if(arrPagoPen.size()>0){
                for (PaynetVO paynetVO : arrPagoPen) {
                    arrPagoPaynet.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), "Transaccion sin ingreso de pago"));
                }
            }
            arrPagoPen = paynetDAO.getTransPaynet(fecha, fecha, ClientesConstants.ID_ESTATUS_PAYNET_CANCELADO);
            if(arrPagoPen.size()>0){
                for (PaynetVO paynetVO : arrPagoPen) {
                    arrPagoPaynet.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), "Transaccion cancelada"));
                }
            }
            arrPagoPen = paynetDAO.getTransPaynet(fecha, fecha, ClientesConstants.ID_ESTATUS_PAYNET_ENVIO_PAGOS);
            if(arrPagoPen.size()>0){
                for (PaynetVO paynetVO : arrPagoPen) {
                    arrPagoPaynet.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), "Transaccion como pago sin confirmar"));
                }
            }
            arrPagoPen = paynetDAO.getTransPaynet(fecha, fecha, ClientesConstants.ID_ESTATUS_PAYNET_PENDIENTE);
            if(arrPagoPen.size()>0){
                for (PaynetVO paynetVO : arrPagoPen) {
                    arrPagoPaynet.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), "Transaccion pendiente de confirmacion"));
                }
            }
            arrPagoPen = paynetDAO.getTransPaynet(fecha, fecha, ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
            if(arrPagoPen.size()>0){
                for (PaynetVO paynetVO : arrPagoPen) {
                    arrPagoPaynet.add(new PaynetVO(paynetVO.getIdPago(), paynetVO.getReferenciaPay(), paynetVO.getMonto(), paynetVO.getFechaAutPagPay(), paynetVO.getFechaAutMovPay(), paynetVO.getEstatus(), "Transaccion dentro de incidencias"));
                }
            }
        } catch (Exception e) {
            myLogger.error("Error en procesaCargaConfiracion ", e);
            throw new ClientesException(e.getMessage());
        } finally{
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrPagoPaynet;
    }
}

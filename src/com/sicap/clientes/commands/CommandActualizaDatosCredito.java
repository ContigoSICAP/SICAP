package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CreditoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import org.apache.log4j.Logger;

public class CommandActualizaDatosCredito implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaDatosCredito.class);
    private static final String COMMENT_AUTORIZACION_POR_EXCEPCION_MALO = "Autorizado por Excepción de Malo a Bueno";
    private static final String COMMENT_AUTORIZACION_POR_EXCEPCION_REGULAR = "Autorizado por Excepción de Regular a Bueno";
    private static final String COMMENT_AUTORIZACION_POR_OTRA_FINANCIERA = "Cliente Autorizado otra financiera";

    public CommandActualizaDatosCredito(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        Notification notificaciones[] = new Notification[1];
        ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
        CreditoVO buroActualizado = new CreditoVO();
        CreditoVO circuloActualizado = new CreditoVO();
        Connection conn = null;
        CreditoDAO dao = new CreditoDAO();
        BitacoraEstatusVO bitacoraEstatusVO = new BitacoraEstatusVO();
        SolicitudDAO solicitudDao = new SolicitudDAO();

        try {
            int resultado = 0;
            int resultado2 = 0;
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            int idObligado = HTMLHelper.getParameterInt(request, "idObligado");
            boolean permiteAutorizarPorExcepcion = (HTMLHelper.getParameterString(request, "permiteAutorizarPorExcepcion").equals("true")) ? true : false;
            boolean permiteAutorizarRegularP1 = (HTMLHelper.getParameterString(request, "permiteAutorizarRegularP1").equals("true")) ? true : false;
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            buroActualizado = CreditoHelper.getBuroVO(buroActualizado, request);
            circuloActualizado = CreditoHelper.getCirculoVO(circuloActualizado, request);
            int cantPases = 0;
            boolean continua = true;
            bitacoraEstatusVO = CreditoHelper.getBitacoraEstatus(bitacoraEstatusVO, request);
            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandActualizaDatosCredito");

            if (bitacoraEstatusVO.estatus == ClientesConstants.SOLICITUD_RECHAZADA) {
                cliente.solicitudes[indiceSolicitud].desembolsado = bitacoraEstatusVO.estatus;
            }
            if (bitacoraEstatusVO.estatus != ClientesConstants.SOLICITUD_PENDIENTE) {
                if (idObligado == 0) {
                    if (cliente.solicitudes[indiceSolicitud].buroCredito != null) {
                        buroActualizado.respCrediticia = cliente.solicitudes[indiceSolicitud].buroCredito.respCrediticia;
                        resultado = dao.updateBuroCliente(conn, buroActualizado);
                    } else {
                        resultado = dao.addBuroCredito(conn, buroActualizado);
                    }

                    if (cliente.solicitudes[indiceSolicitud].circuloCredito != null) {

                        //Autorizacion por otra Finaciera
                        if (circuloActualizado.otraFin) {                      
                            circuloActualizado.aceptaRegular = ClientesConstants.AUTORIZACION_OTRA_FINANCIERA;
                            cliente.solicitudes[indiceSolicitud].estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.comentario = COMMENT_AUTORIZACION_POR_OTRA_FINANCIERA;
                            //Actualizamos desembolsso a 1 listo para desembolsar
                            cliente.solicitudes[indiceSolicitud].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
                        } //Si se realizo Autorización por Excepcion
                        else if (circuloActualizado.calificacionMesaControl == ClientesConstants.CALIFICACION_CIRCULO_BUENA && permiteAutorizarPorExcepcion) {
                            myLogger.info("Autoriza credito rechazado por excepcion: " + cliente.idCliente + "-" + cliente.solicitudes[indiceSolicitud].idSolicitud);
                            circuloActualizado.aceptaRegular = ClientesConstants.AUTORIZADO_POR_EXCEPCION;
                            //Actualizamos estatus solicitud a aprobada
                            cliente.solicitudes[indiceSolicitud].estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.comentario = COMMENT_AUTORIZACION_POR_EXCEPCION_MALO;
                            //Actualizamos desembolsso a 1 listo para desembolsar
                            cliente.solicitudes[indiceSolicitud].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;

                        } else if (circuloActualizado.calificacionMesaControl == ClientesConstants.CALIFICACION_CIRCULO_BUENA && permiteAutorizarRegularP1) {
                            myLogger.info("Autoriza credito por excepcion de regular a bueno: " + cliente.idCliente + "-" + cliente.solicitudes[indiceSolicitud].idSolicitud);
                            circuloActualizado.aceptaRegular = ClientesConstants.AUTORIZADO_POR_EXCEPCION_REGULAR;
                            //Actualizamos estatus solicitud a aprobada
                            cliente.solicitudes[indiceSolicitud].estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
                            bitacoraEstatusVO.comentario = COMMENT_AUTORIZACION_POR_EXCEPCION_REGULAR;
                            //Actualizamos desembolsso a 1 listo para desembolsar
                            cliente.solicitudes[indiceSolicitud].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
                        }
                        
                            resultado2 = dao.updateBuroCliente(conn, circuloActualizado);
                        

                    } else {
                        resultado2 = dao.addBuroCredito(conn, circuloActualizado);
                    }
                   
                        cliente.solicitudes[indiceSolicitud].buroCredito = buroActualizado;
                        cliente.solicitudes[indiceSolicitud].circuloCredito = circuloActualizado;
                    
                } else {
                    if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito != null) {
                        buroActualizado.respCrediticia = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito.respCrediticia;
                        resultado = dao.updateBuroCliente(conn, buroActualizado);
                    } else {
                        resultado = dao.addBuroCredito(conn, buroActualizado);
                    }

                    if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].circuloCredito != null) {
                        resultado2 = dao.updateBuroCliente(conn, circuloActualizado);
                    } else {
                        resultado = dao.addBuroCredito(conn, circuloActualizado);
                    }

                    cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito = buroActualizado;
                    cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].circuloCredito = circuloActualizado;
                }
                
                    if (resultado != 0 && resultado2 != 0) {
                        if (bitacoraEstatusVO.estatus != 0) {
                            cliente.solicitudes[indiceSolicitud].estatus = bitacoraEstatusVO.estatus;
                            solicitudDao.updateSolicitud(conn, cliente.idCliente, cliente.solicitudes[indiceSolicitud]);
                            bitutil.registraEvento(cliente.solicitudes[indiceSolicitud]);
                            bitacoraEstatusVO.idCliente = cliente.idCliente;
                            bitacoraEstatusVO.idSolicitud = idSolicitud;
                            bitutil.registraCambioEstatus(bitacoraEstatusVO, request);
                        }
                        conn.commit();
                        bitutil.registraEvento(buroActualizado.toString());
                        bitutil.registraEvento(circuloActualizado.toString());
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Los datos no pudieron ser actualizados");
                    }
                
            } else {
                cliente.solicitudes[indiceSolicitud].estatus = bitacoraEstatusVO.estatus;
                solicitudDao.updateSolicitud(conn, cliente.idCliente, cliente.solicitudes[indiceSolicitud]);
                bitutil.registraEvento(cliente.solicitudes[indiceSolicitud]);
                bitacoraEstatusVO.idCliente = cliente.idCliente;
                bitacoraEstatusVO.idSolicitud = idSolicitud;
                bitutil.registraCambioEstatus(bitacoraEstatusVO, request);
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La solicitud se envio a estatus de pendiente");
                conn.commit();
            }
            session.setAttribute("CLIENTE", cliente);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("SQLException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }

}

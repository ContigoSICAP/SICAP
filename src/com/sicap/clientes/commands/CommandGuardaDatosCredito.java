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
import com.sicap.clientes.dao.BitacoraEstatusDAO;

public class CommandGuardaDatosCredito implements Command {

    private String siguiente;

    public CommandGuardaDatosCredito(String siguiente) {
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
        String respBuro = (String) session.getAttribute("BURO");
        BitacoraEstatusVO bitacoraEstatusVO = new BitacoraEstatusVO();
        SolicitudDAO solicitudDao = new SolicitudDAO();
        boolean continua = true;
        int cantPases = 0;

        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            int resultado = 0;
            int resultado2 = 0;
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            int idObligado = HTMLHelper.getParameterInt(request, "idObligado");
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            buroActualizado = CreditoHelper.getBuroVO(buroActualizado, request);
            bitacoraEstatusVO = CreditoHelper.getBitacoraEstatus(bitacoraEstatusVO, request);
            circuloActualizado = CreditoHelper.getCirculoVO(circuloActualizado, request);
            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaDatosCredito");

            if (respBuro != null) {
                buroActualizado.respCrediticia = respBuro;
            } else {
                buroActualizado.respCrediticia = "N/A";
            }
            circuloActualizado.respCrediticia = "N/A";

            if (bitacoraEstatusVO.estatus == ClientesConstants.SOLICITUD_RECHAZADA) {
                cliente.solicitudes[indiceSolicitud].desembolsado = bitacoraEstatusVO.estatus;
            }

            if (bitacoraEstatusVO.estatus != ClientesConstants.SOLICITUD_PENDIENTE) {
                if (idObligado == 0) {//Si se trata de guardar los datos del cliente
                    if (buroActualizado.otraFin) {
                        buroActualizado.aceptaRegular = ClientesConstants.AUTORIZACION_OTRA_FINANCIERA;
                    }
                    if (cliente.solicitudes[indiceSolicitud].buroCredito != null) {
                        buroActualizado.respCrediticia = cliente.solicitudes[indiceSolicitud].buroCredito.respCrediticia;
                        resultado = dao.updateBuroCliente(conn, buroActualizado);
                    } else {
                        resultado = dao.addBuroCredito(conn, buroActualizado);
                    }
                    if (cliente.solicitudes[indiceSolicitud].circuloCredito != null) {
                        resultado2 = dao.updateBuroCliente(conn, circuloActualizado);
                    } else {
                        resultado2 = dao.addBuroCredito(conn, circuloActualizado);
                    }

                    cliente.solicitudes[indiceSolicitud].buroCredito = buroActualizado;
                    cliente.solicitudes[indiceSolicitud].circuloCredito = circuloActualizado;

                } else {//Si se trata de guardar los datos del obligado
                    if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito != null) {
                        buroActualizado.respCrediticia = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito.respCrediticia;
                        resultado = dao.updateBuroCliente(conn, buroActualizado);
                    } else {
                        resultado = dao.addBuroCredito(conn, buroActualizado);
                    }

                    if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].circuloCredito != null) {
                        resultado2 = dao.updateBuroCliente(conn, circuloActualizado);
                    } else {
                        resultado2 = dao.addBuroCredito(conn, circuloActualizado);
                    }

                    cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].buroCredito = buroActualizado;
                    cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].circuloCredito = circuloActualizado;
                }

                /*
                 if(cliente.solicitudes[indiceSolicitud].buroCredito==null){
                 Logger.debug("cliente.solicitudes[indiceSolicitud].buroCredito==null");
                 resultado = dao.addBuroCredito(conn, buroActualizado);
                 }
                 else{
                 Logger.debug("cliente.solicitudes[indiceSolicitud].buroCredito==null else");
                 buroActualizado.respCrediticia = cliente.solicitudes[indiceSolicitud].buroCredito.respCrediticia;
                 resultado = dao.updateBuroCliente(conn, buroActualizado);
                 }
                 */
//			int resultado2 =  dao.addBuroCredito(conn, circuloActualizado); 
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
                    bitutil.registraEvento("Inserta datos de buro");
                    bitutil.registraEvento(circuloActualizado);
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                } else {
                    conn.rollback();
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Los datos no pudieron ser guardados fueron actualizados correctamente");
                }
            } else {
                cliente.solicitudes[indiceSolicitud].estatus = bitacoraEstatusVO.estatus;
                solicitudDao.updateSolicitud(conn, cliente.idCliente, cliente.solicitudes[indiceSolicitud]);
                bitutil.registraEvento(cliente.solicitudes[indiceSolicitud]);
                bitacoraEstatusVO.idCliente = cliente.idCliente;
                bitacoraEstatusVO.idSolicitud = idSolicitud;
                bitutil.registraCambioEstatus(bitacoraEstatusVO, request);
                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La solicitud se envio a estatus de pendiente");
            }

            session.setAttribute("CLIENTE", cliente);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }

}

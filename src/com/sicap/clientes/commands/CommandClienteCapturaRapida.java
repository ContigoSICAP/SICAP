package com.sicap.clientes.commands;

import com.sicap.clientes.controller.ApplicationController;
import com.sicap.clientes.dao.CatalogoDAO;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.sql.Connection;
import org.apache.log4j.Logger;

public class CommandClienteCapturaRapida implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(ApplicationController.class);

    public CommandClienteCapturaRapida(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        int idCliente = 0;
        ClienteVO existeRFC = null;
        Connection conn = null;
        HttpSession session = request.getSession();
        ClienteVO clienteVO = null;
        ClienteVO clienteSesionVO = null;
        CatalogoVO CatLocalidadVO = null;
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SolicitudVO solicitud = new SolicitudVO();
        ClienteDAO clienteDAO = new ClienteDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        DireccionDAO direccionDAO = new DireccionDAO();
        Notification notificaciones[] = new Notification[1];
        String nombre = request.getParameter("nombre").trim().toUpperCase();
        String aPaterno = request.getParameter("aPaterno").trim().toUpperCase();
        String aMaterno = request.getParameter("aMaterno").trim().toUpperCase();
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String nombreSucursal = "";
        SucursalDAO sucursalDAO = null;
        String respuestaWS = "";

        DireccionVO direccion = new DireccionVO();
        String RFC = "";

        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            clienteSesionVO = (ClienteVO) session.getAttribute("CLIENTE");
            if (nombre != null && aPaterno != null && aMaterno != null && fechaNacimiento != null) {
                if (!CatalogoHelper.esSucursaldeIxaya(HTMLHelper.getParameterInt(request, "sucursal"))) {
                    synchronized (this) {
                        RFC = RFCUtil.obtenRFC(nombre.trim().toUpperCase(), aPaterno.trim().toUpperCase(), aMaterno.trim().toUpperCase(), Convertidor.formatDateCirculo(fechaNacimiento));
                        existeRFC = clienteDAO.getClienteRFC(RFC.substring(0, 10));
                        if (request.getParameter("command").equals("actualizaClienteCR")) {
                            respuestaWS = ClientesUtil.comparaDuplicadadJUCAVI(new ClienteVO(0, nombre, aPaterno, aMaterno, RFC, Convertidor.stringToSqlDate(fechaNacimiento)));
                            if (respuestaWS.equals("")) {
                                if (existeRFC == null || ClientesUtil.comparaDuplicadadRFC(new ClienteVO(0, nombre, aPaterno, aMaterno, RFC, Convertidor.stringToSqlDate(fechaNacimiento))) || (existeRFC != null && clienteSesionVO.idCliente == existeRFC.idCliente)) {
                                    direccion = DireccionHelper.getVO(clienteSesionVO.direcciones[0], request);
                                    clienteSesionVO.nombre = nombre;
                                    if (aPaterno.equals("X")) {
                                        aPaterno = "";
                                    }
                                    if (aMaterno.equals("X")) {
                                        aMaterno = "";
                                    }
                                    BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandClienteCapturaRapida");
                                    clienteSesionVO.aPaterno = aPaterno;
                                    clienteSesionVO.aMaterno = aMaterno;
                                    clienteSesionVO.nombreCompleto = nombre + " " + aPaterno + " " + aMaterno;
                                    clienteSesionVO.fechaNacimiento = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaNacimiento"));
                                    clienteSesionVO.rfc = RFC;
                                    clienteSesionVO.entidadNacimiento = HTMLHelper.getParameterInt(request, "entidadNacimiento");
                                    clienteDAO.updateCliente(conn, clienteSesionVO);
                                    direccionDAO.updateDireccion(conn, clienteSesionVO.idCliente, direccion);
                                    bitutil.registraEvento(clienteSesionVO);
                                    conn.commit();
                                    clienteSesionVO.direcciones = direccionDAO.getDirecciones(clienteSesionVO.idCliente);
                                    if (clienteSesionVO.direcciones != null) {
                                        CatLocalidadVO = catalogoDAO.getLocalidad(clienteSesionVO.direcciones[0].idColonia, clienteSesionVO.direcciones[0].idLocalidad);
                                        if (CatLocalidadVO != null) {
                                            clienteSesionVO.direcciones[0].localidad = CatLocalidadVO.descripcion;
                                        }
                                    }
                                    session.setAttribute("CLIENTE", clienteSesionVO);
                                } else {
                                    sucursalDAO = new SucursalDAO();
                                    nombreSucursal = sucursalDAO.getSucursalNombre(existeRFC.idSucursal);
                                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El RFC ya se encuentra registrado en la sucursal " + nombreSucursal);
                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                    request.setAttribute("RFC_ENCONTRADO", existeRFC);
                                }
                            } else {
                                clienteVO = new ClienteVO();
                                siguiente = "/nuevoClienteCapturaRapida.jsp";
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, respuestaWS);
                                request.setAttribute("NOTIFICACIONES", notificaciones);
                            }
                        } else {
                            //ClientesUtil.comparaDuplicadadRFC(existeRFC, new ClienteVO(0, nombre, aPaterno, aMaterno, RFC, Convertidor.stringToSqlDate(fechaNacimiento)));
                            //if (existeRFC == null) {
                            respuestaWS = ClientesUtil.comparaDuplicadadJUCAVI(new ClienteVO(0, nombre, aPaterno, aMaterno, RFC, Convertidor.stringToSqlDate(fechaNacimiento)));
                            if (respuestaWS.equals("")) {
                                if (existeRFC == null || ClientesUtil.comparaDuplicadadRFC(new ClienteVO(0, nombre, aPaterno, aMaterno, RFC, Convertidor.stringToSqlDate(fechaNacimiento)))) {
                                    direccion = DireccionHelper.getVO(new DireccionVO(), request);
                                    clienteVO = new ClienteVO();
                                    clienteVO.rfc = RFC;
                                    clienteVO.nombre = nombre;
                                    if (aPaterno.equals("X")) {
                                        aPaterno = "";
                                    }
                                    if (aMaterno.equals("X")) {
                                        aMaterno = "";
                                    }
                                    clienteVO.aPaterno = aPaterno;
                                    clienteVO.aMaterno = aMaterno;
                                    clienteVO.nombreCompleto = clienteVO.nombre + " " + clienteVO.aPaterno + " " + clienteVO.aMaterno;
                                    clienteVO.fechaNacimiento = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaNacimiento"));
                                    clienteVO.entidadNacimiento = HTMLHelper.getParameterInt(request, "entidadNacimiento");
                                    clienteVO.idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
                                    //solicitud.tipoOperacion = HTMLHelper.getParameterInt(request, "operacion");
                                    clienteVO.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                                    clienteVO.fechaCaptura = new Timestamp(System.currentTimeMillis());
                                    idCliente = clienteDAO.addCliente(conn, clienteVO);
                                    BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandClienteCapturaRapida");
                                    bitutil.registraEvento(clienteVO);
                                    clienteVO.idCliente = idCliente;
                                    solicitud.idCliente = idCliente;
                                    solicitud.tipoOperacion = HTMLHelper.getParameterInt(request, "operacion");
                                    solicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
                                    solicitud.estatus = ClientesConstants.SOLICITUD_NUEVA;
                                    int idSolicitud = solicitudDAO.addSolicitud(conn, idCliente, solicitud);
                                    bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandClienteCapturaRapida");
                                    bitutil.registraEvento(solicitud);
                                    bitutil.registraCambioEstatus(solicitud, request);
                                    clienteVO.solicitudes = solicitudDAO.getSolicitudes(idCliente);
                                    int idDireccion = direccionDAO.addDireccion(conn, clienteVO.idCliente, direccion);
                                    conn.commit();
                                    session.setAttribute("CLIENTE", clienteVO);
                                    request.setAttribute("ID_SOLICITUD", new Integer(idSolicitud));
                                    request.setAttribute("idSolicitud", new Integer(idSolicitud));
                                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El RFC se registr&oacute; correctamente");
                                    siguiente = "/archivosAsociados.jsp";
                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                    clienteVO.direcciones = direccionDAO.getDirecciones(idCliente);
                                    if (clienteVO.direcciones != null) {
                                        CatLocalidadVO = catalogoDAO.getLocalidad(clienteVO.direcciones[0].idColonia, clienteVO.direcciones[0].idLocalidad);
                                        if (CatLocalidadVO != null) {
                                            clienteVO.direcciones[0].localidad = CatLocalidadVO.descripcion;
                                        }
                                    }
                                    clienteVO.solicitudes = solicitudDAO.getSolicitudes(idCliente);
                                    session.setAttribute("CLIENTE", clienteVO);
                                } else {
                                    clienteVO = new ClienteVO();
                                    clienteVO = clienteDAO.getClienteRFCCompleto(RFC);
                                    sucursalDAO = new SucursalDAO();
                                    nombreSucursal = sucursalDAO.getSucursalNombre(clienteVO.idSucursal);
                                    siguiente = "/nuevoClienteCapturaRapida.jsp";
                                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El RFC ya se encuentra registrado en la sucursal " + nombreSucursal);
                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                    request.setAttribute("RFC_ENCONTRADO", clienteVO);
                                }
                            } else {
                                siguiente = "/nuevoClienteCapturaRapida.jsp";
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, respuestaWS);
                                request.setAttribute("NOTIFICACIONES", notificaciones);
                            }
                        }
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El movimiento se debe de realizar en el sistema JUCAVI");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    siguiente = "/nuevoClienteCapturaRapida.jsp";
                }
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("Error en CommandClienteCapturaRapida", dbe);
            try {
                conn.rollback();
            } catch (Exception edbe) {
                myLogger.error("Error en CommandClienteCapturaRapida", edbe);
                throw new CommandException(edbe.getMessage());
            }
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Error en CommandClienteCapturaRapida", e);
            try {
                conn.rollback();
            } catch (Exception ee) {
                myLogger.error("Error en CommandClienteCapturaRapida", ee);
                throw new CommandException(ee.getMessage());
            }
            throw new CommandException(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception econn) {
                myLogger.error("Error en CommandClienteCapturaRapida", econn);
                throw new CommandException(econn.getMessage());
            }
        }
        return siguiente;
    }

}

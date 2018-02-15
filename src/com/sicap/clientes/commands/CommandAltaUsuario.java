package com.sicap.clientes.commands;

import com.sicap.clientes.exceptions.ClientesException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.UserSucursalDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandAltaUsuario implements Command {

    private String siguiente;

    public CommandAltaUsuario(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        UsuarioDAO usuDAO = new UsuarioDAO();
        UserSucursalDAO userSucursalDAO = new UserSucursalDAO();
        Connection conn = null;
        UsuarioVO usuario = null;
        String[] roles = null;

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String identificador = request.getParameter("identificador");
        int idSucursal = 0;
        try {
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
        } catch (Exception ex) {
            System.out.println("No encontre la sucursal");
            Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            usuario = usuDAO.getDatosUsuario(username);
        } catch (ClientesException ex) {
            System.out.println("No existe el usuario");
            Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (usuario != null) {
            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El usuario ya existe.");
            request.setAttribute("NOTIFICACIONES", notificaciones);
            return siguiente;
        }
        usuario = new UsuarioVO();
        if (!SecurityUtil.validaPassword(password)) {
            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El formato del password es incorrecto.");
        } else {
            try {
                conn = ConnectionManager.getMySQLConnection();
                conn.setAutoCommit(false);
                usuario = new UsuarioVO();
                usuario.nombre = username;
                usuario.password = password;
                usuario.nombreCompleto = nombreCompleto;
                usuario.identificador = identificador;
                usuario.idSucursal = idSucursal;
                roles = usuDAO.getRoles();
                usuDAO.addUsuario(usuario, null);
                //usuDAO.addRolUsuario(usuario.nombre, "AUXILIAR_CREDITO", conn);
                if (roles != null) {
                    for (int i = 0; i < roles.length; i++) {
                        if (request.getParameter(roles[i]) != null) {
                            usuDAO.addRolUsuario(usuario.nombre, roles[i], conn);
                            request.setAttribute(roles[i], roles[i]);
                        }
                    }
                }
                userSucursalDAO.addUserSucursal(username, idSucursal, conn);
                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El usuario fue guardado correctamente");
                usuario.roles = usuDAO.getRolesUsuario(usuario.nombre);
                request.setAttribute("MODIFICACION_USUARIO", usuario);
            } catch (ClientesException ex) {
                Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        request.setAttribute("NOTIFICACIONES", notificaciones);






        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CommandAltaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



        return siguiente;
    }
}

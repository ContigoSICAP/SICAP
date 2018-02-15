package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandModificaUsuario implements Command {

    private String siguiente;

    public CommandModificaUsuario(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        UsuarioDAO usuDAO = new UsuarioDAO();
        Connection conn = null;
        String[] roles = null;//catalogo de roles
        UsuarioVO usuario = new UsuarioVO();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String nombreCompleto = request.getParameter("nombreCompleto");
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            conn = ConnectionManager.getMySQLConnection();
            usuario.nombre = username;
            usuario.password = password;
            usuario.nombreCompleto = nombreCompleto;
            conn.setAutoCommit(false);
            usuario.idSucursal = idSucursal;
            
                    usuDAO.updateUsuarioSucursal(usuario, username);
            if (password != null && password.trim().length() != 0) {
                if (!SecurityUtil.validaPassword(password)) {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El formato del password es incorrecto.");

                    usuario = usuDAO.getDatosUsuario(username);
                    usuario.roles = usuDAO.getRolesUsuario(username);
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    request.setAttribute("MODIFICACION_USUARIO", usuario);
                    return siguiente;
                } else {

                    usuDAO.updateDatosUsuario(username, password, conn);
                    usuario = usuDAO.getDatosUsuario(username);
                    }
            }
            roles = usuDAO.getRoles();
            usuario = usuDAO.getDatosUsuario(username);
            String []rolesAux = usuario.roles;
            
            usuDAO.deleteRolesUsuario(usuario.nombre, conn);
            //usuDAO.addRolUsuario(usuario.nombre, "AUXILIAR_CREDITO", conn);
            if (roles != null) {
                for (int i = 0; i < roles.length; i++) {
                    if (request.getParameter(roles[i]) != null) {
                        
                        usuDAO.addRolUsuario(usuario.nombre, roles[i], conn);
                    }
                }
            }  
            conn.commit();
            usuario.roles = usuDAO.getRolesUsuario(usuario.nombre);
            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El usuario fue modificado correctamente");


            request.setAttribute("MODIFICACION_USUARIO", usuario);
            request.setAttribute("NOTIFICACIONES", notificaciones);

        } catch (ClientesDBException dbe) {
            dbe.printStackTrace();
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

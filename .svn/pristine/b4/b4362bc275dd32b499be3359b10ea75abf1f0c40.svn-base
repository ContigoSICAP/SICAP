package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandDesbloqueaUsuario implements Command{
	
	private String siguiente;
	 
	public CommandDesbloqueaUsuario(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException {
		Notification notificaciones[] = new Notification[1];
		UsuarioDAO usuDAO = new UsuarioDAO();
		Connection conn  = null;
		UsuarioVO usuario = new UsuarioVO();
		try{
			String username = request.getParameter("nombreUsuario");
			conn = ConnectionManager.getMySQLConnection();
			usuario = new UsuarioDAO().getDatosUsuario(username);
			
			if( usuario.intentosFallidos >= 3 ){
				usuario.status="A";
				usuario.intentosFallidos = 0;
			}else{
				usuario.fechaAcceso= new Date(System.currentTimeMillis());
				usuario.diasSinAccesar=0;
                                usuario.intentosFallidos=0;
			}
			usuDAO.updateUsuario(usuario, username);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"El usuario fue modificado correctamente");
			request.setAttribute("MODIFICACION_USUARIO",usuario);
			request.setAttribute("NOTIFICACIONES", notificaciones);
		}catch(ClientesDBException dbe){
			dbe.printStackTrace();
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		finally {
			try {
				if ( conn!=null ) conn.close();
			}
			catch(SQLException sqle) {
				throw new CommandException(sqle.getMessage());
			}
		}
		return siguiente;
	}
}
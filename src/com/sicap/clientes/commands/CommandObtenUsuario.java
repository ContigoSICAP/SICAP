package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandObtenUsuario implements Command{
	
	private String siguiente;
	 
	public CommandObtenUsuario(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException {
		try{
			String username = request.getParameter("nombreUsuario");
			UsuarioVO usuario = new UsuarioDAO().getDatosUsuario(username);
			usuario.roles = new UsuarioDAO().getRolesUsuario(username);
			request.setAttribute("MODIFICACION_USUARIO", usuario);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}
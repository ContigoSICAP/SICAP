package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandBuscaUsuarios implements Command {

	private String siguiente;
	
	
	public CommandBuscaUsuarios(String siguiente) {
		this.siguiente = siguiente;
	}
	
	
	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		UsuarioDAO usuariodao = new UsuarioDAO();
		UsuarioVO[] usuarios = null;
		
		try{
			usuarios = usuariodao.getUsuarios(request.getParameter("usuario"));
			if(usuarios.length==0)			
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron usuarios");
			else
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Usuarios encontrados");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("USUARIOS", usuarios);
			
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;	
		
	} 
	
}



package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;


public class CommandArrendatarioLocal implements Command{


	private String siguiente;


	public CommandArrendatarioLocal(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		try{
			request.setAttribute("TIPO_ARRENDATARIO", new Integer(ClientesConstants.ARRENDATARIO_LOCAL));
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}


}

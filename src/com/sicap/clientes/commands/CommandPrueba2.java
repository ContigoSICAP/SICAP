package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.GrupoUtil;




public class CommandPrueba2 implements Command{


	private String siguiente;


	public CommandPrueba2(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		GrupoUtil util = new GrupoUtil();
		util.procesoMonitorPagosGrupos();
		
		return siguiente;

	}


}

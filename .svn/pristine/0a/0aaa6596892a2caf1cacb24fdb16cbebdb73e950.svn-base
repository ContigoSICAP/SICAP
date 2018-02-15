package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;


public class CommandNull implements Command{


	private String siguiente;


	public CommandNull(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest req) throws CommandException {
		return siguiente;
	}


}

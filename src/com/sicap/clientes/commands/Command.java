package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;

public interface Command {

	public String execute(HttpServletRequest req) throws CommandException;

}

package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CodigoPostalVO;


public class CommandBuscaCP implements Command{


	private String siguiente;


	public CommandBuscaCP(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		String cp = null;
		CodigoPostalVO[] codigosPostales = null;
		CatalogoDAO dao = new CatalogoDAO();
		Notification notificaciones[] = new Notification[1];

		try{
			if ( request.getParameter("cp")!=null ){
				synchronized(this){
					cp = request.getParameter("cp");
					codigosPostales = dao.getColonias(cp);
				}
				if ( codigosPostales!=null && codigosPostales.length>0 ){
					request.setAttribute("CODIGOS_POSTALES",codigosPostales);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro el c&oacute;digo postal");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}
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

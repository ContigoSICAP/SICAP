package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.RepresentantesVO;


public class CommandSeleccionaRepresentante implements Command{

	private String siguiente;

	public CommandSeleccionaRepresentante(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		try{
			Notification notificaciones[] = new Notification[1];
			int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			int idRepresentante = HTMLHelper.getParameterInt(request, "idRepresentante");
			
			if(idSucursal>0) {
				RepresentantesVO representante = new RepresentantesVO();
				RepresentantesDAO representantedao = new RepresentantesDAO();
				representante = representantedao.getRepresentante(idRepresentante);
				request.setAttribute("REPRESENTANTE",representante);
			}
				
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
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

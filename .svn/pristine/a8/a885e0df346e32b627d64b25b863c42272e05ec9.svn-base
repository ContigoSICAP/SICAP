package com.sicap.clientes.commands;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SupervisorCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SupervisorCreditoVO;


public class CommandListaSupervisor implements Command{

	private String siguiente;

	public CommandListaSupervisor(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		//TreeMap catSupervisor = new TreeMap();
		ArrayList<SupervisorCreditoVO> listaSupervisor = null;
		SupervisorCreditoDAO supervisordao = new SupervisorCreditoDAO();
		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			if ( idSucursal>0 ){
				listaSupervisor = supervisordao.getSupervisorSucursal(idSucursal);
				if(listaSupervisor!=null && listaSupervisor.size()>0) {
					request.setAttribute("SUPERVISORES",listaSupervisor);
					Logger.debug("Supervisores encontrados");
															
				}	//fin if
				else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron supervisores para la sucursal seleccionada");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}	//fin else
			}	//fin if
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}  //fin else
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

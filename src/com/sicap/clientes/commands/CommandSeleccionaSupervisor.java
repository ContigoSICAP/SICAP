package com.sicap.clientes.commands;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SupervisorCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SupervisorCreditoVO;


public class CommandSeleccionaSupervisor implements Command{

	private String siguiente;

	public CommandSeleccionaSupervisor(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		try{
			Notification notificaciones[] = new Notification[1];
			int idSucursal = 0;
			
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			
			if(idSucursal>0) {
				//if(idEjecutivo!=0) {
					Enumeration parametros = request.getParameterNames();  //obtiene todos los parámetros
					
					while(parametros.hasMoreElements()) {
						String nombreParametro = (String)parametros.nextElement();
						String[] valores = request.getParameterValues(nombreParametro);
						/*
						if(valores.length == 1) {
							String valorParametro = valores[0]; 					
						}
						*/
						//el contenido de esos valores
						for(int i = 0; i < valores.length;i++) {
							if(nombreParametro.equals("opcion")) {	//busca por las checkboxes que se llaman así
								int idSupervisorModificar = Integer.parseInt(valores[i]);
								SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
								SupervisorCreditoDAO supervisordao = new SupervisorCreditoDAO();
								supervisor = supervisordao.getSupervisor(idSupervisorModificar);
								request.setAttribute("SUPERVISORES",supervisor);
							}
						}
					}
				//}
				//else {
				//	notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado a ning&uacute;n ejecutivo");
				//	request.setAttribute("NOTIFICACIONES",notificaciones);
				//} //fin else
			}	//fin if
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}	//fin else
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

package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SupervisorCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SupervisorCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.RepresentantesVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;


public class CommandModificarSupervisor implements Command{

	private String siguiente;

	public CommandModificarSupervisor(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		int idSupervisor = 0;
		String nombre = null;
		String aPaterno = null;
		String aMaterno = null;		
		String estatus = null;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			idSupervisor = HTMLHelper.getParameterInt(request, "idSupervisor");
			nombre = HTMLHelper.getParameterString(request,"nombre");
			aPaterno = HTMLHelper.getParameterString(request,"aPaterno");
			aMaterno = HTMLHelper.getParameterString(request,"aMaterno");
			estatus = HTMLHelper.getParameterString(request, "estatus");
			if(idSucursal>0) {
				if(idSupervisor>0) {
					SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
					SupervisorCreditoDAO supervisordao = new SupervisorCreditoDAO();
					supervisor = supervisordao.getSupervisor(idSupervisor);
					supervisor.nombre =nombre;
					supervisor.aPaterno =aPaterno;
					supervisor.aMaterno=aMaterno;
					supervisor.estatus=estatus;
					
					///checar el helper posible creacion.
					
					if( HTMLHelper.getIdAltaStatusSupervisor(estatus).equals("A")) 
						supervisor.estatus = "A";
					else
						supervisor.estatus = "B";
					
					int actualizar = supervisordao.updateSupervisor(supervisor);
					if(actualizar>0) {
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se actualizó el status para el supervisor");
						request.setAttribute("NOTIFICACIONES",notificaciones);
						request.setAttribute("SUPERVISOR",supervisor);
						request.setAttribute("ACTUALIZACION_SUPERVISOR", actualizar);
					}
					else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se pudo actualizar ese supervisor");
						request.setAttribute("NOTIFICACIONES",notificaciones);
					}
				}  //fin if
				else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado a ning&uacute;n supervisor");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}	//fin else
			}  //fin if
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

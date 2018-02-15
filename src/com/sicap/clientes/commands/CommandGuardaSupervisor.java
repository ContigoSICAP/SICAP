package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SupervisorCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SupervisorCreditoVO;


public class CommandGuardaSupervisor implements Command{


	private String siguiente;


	public CommandGuardaSupervisor(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		Notification notificaciones[] = new Notification[1];
		
		try{
			//int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int sucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			String nombre = HTMLHelper.getParameterString(request, "nombre");
			String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
			String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
			
			SupervisorCreditoDAO supervisordao = new SupervisorCreditoDAO();
			SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
			
			
			System.out.println("La Sucursal es: "+sucursal);
			System.out.println("El nombre es: "+nombre);
			System.out.println("La apaterno es: "+aPaterno);
			System.out.println("La amaterno es: "+aMaterno);

			
			supervisor.idSucursal = sucursal;
			supervisor.nombre = nombre;
			supervisor.aPaterno = aPaterno;
			supervisor.aMaterno = aMaterno;
			supervisor.estatus = "A";	//se asume que al registrarse se da de alta
			supervisor.usuario = request.getRemoteUser();
			
			supervisordao.addSupervisor(supervisor);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("EJECUTIVOS", supervisor);
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

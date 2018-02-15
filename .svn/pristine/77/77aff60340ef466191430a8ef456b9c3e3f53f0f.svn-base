package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SucursalVO;

public class CommandBuscaSucursal implements Command {

	private String siguiente;
	
	
	public CommandBuscaSucursal(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		SucursalDAO sucursaldao = new SucursalDAO();
		SucursalVO[] sucursal = null;
		
		try{
			//System.out.println("estoy en el comando buscaSucursal");
			//System.out.println("la sucursal q busco es : " + request.getParameter("sucursal"));
			sucursal = sucursaldao.getSucursal(request.getParameter("sucursal"));
			if(sucursal.length==0)			
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron Sucursales");
			else
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Sucursales encontradas");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("SUCURSAL", sucursal);
			
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



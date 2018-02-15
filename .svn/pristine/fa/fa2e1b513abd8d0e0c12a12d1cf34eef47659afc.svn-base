package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SucursalVO;

public class CommandObtenSucursal implements Command{
	
	private String siguiente;
	 
	public CommandObtenSucursal(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException {
		try{
			String nombreSucursal = request.getParameter("nombreSucursal");
			
			Notification notificaciones[] = new Notification[1];
			SucursalDAO sucursaldao = new SucursalDAO();
			SucursalVO[] sucursal = null;
			sucursal = sucursaldao.getSucursal(nombreSucursal);
			int numSucursal = sucursal[0].idSucursal;
			String detalleColonia = request.getParameter("colonia");
			int detalleCp = HTMLHelper.getParameterInt(request, "cp");
			String detalleMunicipio = request.getParameter("municipio");
			String detalleEstado = request.getParameter("estado");
		
			//SucursalVO sucVO = null;
			//sucVO = sucursaldao.getSucursalCom(nombreSucursal);
			
			request.setAttribute("SUCURSAL", sucursal);
	
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}
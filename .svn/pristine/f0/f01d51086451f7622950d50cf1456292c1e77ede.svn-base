package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.ClienteVO;


public class CommandGeneraRFC implements Command{


	private String siguiente;


	public CommandGeneraRFC(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		String nombre = "";
		String aPaterno = "";
		String aMaterno = "";
		String fechaNacimiento = "";
		int entidadNacimiento = 0;
		String RFC = "";
		ClienteVO cliente = new ClienteVO();

		Notification notificaciones[] = new Notification[1];

		nombre = request.getParameter("nombre");
		aPaterno = request.getParameter("aPaterno");
		aMaterno = request.getParameter("aMaterno");
		fechaNacimiento = request.getParameter("fechaNacimiento");
		entidadNacimiento = Integer.parseInt(request.getParameter("entidadNacimiento"));
		
		try{
			if ( nombre!=null && aPaterno!=null && aMaterno!=null && fechaNacimiento!=null){
				synchronized(this){
					RFC = RFCUtil.obtenRFC(nombre.trim().toUpperCase(), aPaterno.trim().toUpperCase(), aMaterno.trim().toUpperCase(), Convertidor.formatDateCirculo(fechaNacimiento));
					cliente.nombre = nombre.trim().toUpperCase();
					cliente.aPaterno = aPaterno.trim().toUpperCase();
					cliente.aMaterno = aMaterno.trim().toUpperCase();
					cliente.fechaNacimiento = Convertidor.stringToSqlDate(fechaNacimiento, ClientesConstants.FORMATO_FECHA);
					cliente.entidadNacimiento = entidadNacimiento;
					cliente.rfc = RFC;
				}
				if ( RFC!=null && RFC!="" ){
					request.setAttribute("ayudarfc",cliente);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"Error al generar el RFC");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}
		}

		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;

	}


}

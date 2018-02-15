package com.sicap.clientes.commands;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.RepresentantesVO;


public class CommandconsultaRepresentantes implements Command{

	private String siguiente;

	public CommandconsultaRepresentantes(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		//TreeMap catEjecutivos = new TreeMap();
		ArrayList<RepresentantesVO> listaRepresentantes = null;
		RepresentantesDAO representantedao = new RepresentantesDAO();
		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			if ( idSucursal>0 ){
				listaRepresentantes = representantedao.getRepresentanteSucursal(idSucursal);
				if(listaRepresentantes!=null && listaRepresentantes.size()>0) {
					request.setAttribute("REPRESENTANTES",listaRepresentantes);
					Logger.debug("Representantes encontrados");
															
				}	//fin if
				else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron representantes para la sucursal seleccionada");
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

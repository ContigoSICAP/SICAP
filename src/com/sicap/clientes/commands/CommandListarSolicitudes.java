package com.sicap.clientes.commands;


import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandListarSolicitudes implements Command{

	private String siguiente;

	public CommandListarSolicitudes(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		//TreeMap catEjecutivos = new TreeMap();
		SolicitudDAO solicituddao = new SolicitudDAO();
		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		int idEjecutivoOrigen = 0;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			idEjecutivoOrigen = HTMLHelper.getParameterInt(request, "idEjecutivoOrigen");

			TreeMap catEjecutivosOrigen = new TreeMap();
			TreeMap catEjecutivosDestino = new TreeMap();
	
			if ( idSucursal>0 ){
				//vuelve a hacer la consulta para no perder los datos de la sesion
				catEjecutivosOrigen = CatalogoHelper.getCatalogoEjecutivos(idSucursal);
				catEjecutivosDestino = CatalogoHelper.getCatalogoEjecutivos(idSucursal,"A");  //lista aquellos que están status=ALTA
				request.setAttribute("EJECUTIVOS_ORIGEN",catEjecutivosOrigen);
				request.setAttribute("EJECUTIVOS_DESTINO",catEjecutivosDestino);
				if( idEjecutivoOrigen>0 ) {
					//obtiene las solicitudes manejadas por ese ejecutivo excepto las canceladas
					SolicitudVO[] solicitudes =solicituddao.getSolicitudesByEjecutivo(idEjecutivoOrigen);
					if(solicitudes!=null && solicitudes.length>0) {
						request.setAttribute("CARTERA_CLIENTES",solicitudes);
						Logger.debug("Cartera clientes encontrados para ese ejecutivo " + solicitudes.length);
					}
					else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron clientes para ese ejecutivo");
						request.setAttribute("NOTIFICACIONES",notificaciones);						
					}
				}
				else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado el ejecutivo de origen");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
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

package com.sicap.clientes.commands;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;

public class CommandBuscaEjecutivosActivos implements Command {
    
    private String siguiente;

	public CommandBuscaEjecutivosActivos(String siguiente) {
		this.siguiente = siguiente;
	}
    public String execute(HttpServletRequest request) throws CommandException {

		try{

			Notification notificaciones[] = new Notification[1];
			int idSucursal = 0;
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
                        String nombre = HTMLHelper.getParameterString(request, "nombre");
			String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
			String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
                        int tipo = HTMLHelper.getParameterInt(request, "tipoEjecutivo");
                        EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                        ejecutivo.idSucursal = idSucursal;
			ejecutivo.nombre = nombre;
			ejecutivo.aPaterno = aPaterno;
			ejecutivo.aMaterno = aMaterno;
                        ejecutivo.tipoEjecutivo = tipo;                       

			TreeMap catEjecutivosActivos = new TreeMap();
			if ( idSucursal>0 ){
				catEjecutivosActivos = CatalogoHelper.getCatalogoEjecutivosActivos(idSucursal);
				if(catEjecutivosActivos!=null && catEjecutivosActivos.size()>0) {
					request.setAttribute("EJECUTIVOS_ACTIVOS",catEjecutivosActivos);
				}
				else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron ejecutivos origen para esa sucursal");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
								
			}
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
                        request.setAttribute("EJECUTIVOS", ejecutivo);
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
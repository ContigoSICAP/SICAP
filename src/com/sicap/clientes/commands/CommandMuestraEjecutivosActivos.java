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

/**
 *
 * @author vjimenez
 */
public class CommandMuestraEjecutivosActivos implements Command {
    
    private String siguiente;

	public CommandMuestraEjecutivosActivos(String siguiente) {
		this.siguiente = siguiente;
	}
    
public String execute(HttpServletRequest request) throws CommandException {

		try{

			Notification notificaciones[] = new Notification[1];
			int idSucursal = 0;
                        String estatus = HTMLHelper.getParameterString(request, "estatus");
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
                        EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                        ejecutivo.idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
                        ejecutivo.nombre = HTMLHelper.getParameterString(request, "nombre");
                        ejecutivo.aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
                        ejecutivo.aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
                        ejecutivo.tipoEjecutivo = HTMLHelper.getParameterInt(request, "tipoEjecutivo");
                        ejecutivo.upline = HTMLHelper.getParameterInt(request, "upline");
                        if (estatus.equals("Alta")){
                            ejecutivo.estatus="A";
                        }
                        else if (estatus.equals("Baja")){
                            ejecutivo.estatus="B";
                        }
                        
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
                        request.setAttribute("EJECUTIVO",ejecutivo);
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
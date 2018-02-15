package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;


public class CommandBuscaGrupos implements Command{


	private String siguiente;


	public CommandBuscaGrupos(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		GrupoVO grupos[] = null;
		GrupoDAO dao = new GrupoDAO();
		String sucursalesUsuario = CatalogoHelper.getCatalogoSucursalesTexto(request);
		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		int idGrupo = 0;
		String nombreGrupo = null;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			nombreGrupo = HTMLHelper.getParameterString(request, "nombreGrupo");
			if ( idSucursal!=0 ){
				if( HTMLHelper.getCheckBox(request, "consultaDetallada") ){
					grupos = dao.getGruposSucursalConDetalle(idSucursal);
					request.setAttribute("DETALLADA", "OK");
				}else{
					grupos = dao.getGruposSucursal(idSucursal);
				}
				if ( grupos!=null && grupos.length>0 ){
					request.setAttribute("GRUPOS", grupos);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron grupos para la sucursal");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else if( nombreGrupo!=null && !nombreGrupo.equals("") ){
				grupos = dao.getGrupoNombre(nombreGrupo, sucursalesUsuario);
				if ( grupos!= null ){
					request.setAttribute("GRUPOS", grupos);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontro ningun grupo con ese nombre");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else if( idGrupo!=0 ){
				grupos = new GrupoVO[1];
				grupos[0] = dao.getGrupo(idGrupo, sucursalesUsuario);
				if ( grupos[0] != null ){
					request.setAttribute("GRUPOS", grupos);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontro ningun grupo con ese numero");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else{
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ingrese un parametro de busqueda");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
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

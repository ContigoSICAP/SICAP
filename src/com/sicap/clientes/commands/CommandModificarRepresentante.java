package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.RepresentantesVO;


public class CommandModificarRepresentante implements Command{

	private String siguiente;

	public CommandModificarRepresentante(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];

		int idSucursal = 0;
		
		int idRepresentante = 0;
		String estatus = null;
		String nombre = null;
		float	factor = 0;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			idRepresentante = HTMLHelper.getParameterInt(request, "idRepresentante");
			estatus = HTMLHelper.getParameterString(request,"estatus");
			nombre = HTMLHelper.getParameterString(request,"txtNombre");
			factor = HTMLHelper.getParameterFloat(request,"txtFactor");
			// Nuevos Campos agregados por la extencion de Alta Convenio
			// Miguel Angel Mendoza 26 Nov 2010
			int tasaMora = HTMLHelper.getParameterInt(request, "tasaMora");
			int gastoCobranza = HTMLHelper.getParameterInt(request, "gastoCobranza");
			int plazoMaximo = HTMLHelper.getParameterInt(request, "plazoMaximo");
			int plazoMinimo = HTMLHelper.getParameterInt(request, "plazoMinimo");
			int diaGracia = HTMLHelper.getParameterInt(request, "diaGracia");
			int diaPago = HTMLHelper.getParameterInt(request, "diaPago");
			int segmento = HTMLHelper.getParameterInt(request, "idSegmento");
			int tipoTabla = HTMLHelper.getParameterInt(request, "idTipoTabla");
			int baseInteres = HTMLHelper.getParameterInt(request, "idBaseInteres");
			int feriado = HTMLHelper.getParameterInt(request, "idEvitaFeriados");
			int manejoFeriado = HTMLHelper.getParameterInt(request, "idManejoFeriados");
			int unidadTiempo = HTMLHelper.getParameterInt(request, "idUnidadTiempo");
			
			if(idSucursal>0) {
				if(idRepresentante>0) {
					RepresentantesVO representante = new RepresentantesVO();
					RepresentantesDAO representantedao = new RepresentantesDAO();
					//representante = representantedao.getRepresentante(idRepresentante);
					representante.idRepresentante=idRepresentante;
					representante.idSucursal=idSucursal;
					representante.nombre=nombre;
					representante.factor=factor;
					representante.estatus=estatus;
					// Nuevos Campos agregados por la extencion de Alta Convenio
					// Miguel Angel Mendoza 26 Nov 2010
					representante.tasaMora = tasaMora;
					representante.gastoCobranza = gastoCobranza;
					representante.plazoMaximo = plazoMaximo;
					representante.plazoMinimo = plazoMinimo;
					representante.diaGracia = diaGracia;
					representante.diaPago = diaPago;
					representante.segmento = segmento;
					representante.tipoTabla = tipoTabla;
					representante.baseInteres = baseInteres;
					representante.feriado = feriado;
					representante.manejoFeriado = manejoFeriado;
					representante.unidadTiempo = unidadTiempo;
					
					
					if( HTMLHelper.getIdAltaStatusEjecutivo(estatus).equals("A")) 
						representante.estatus = "A";
					else
						representante.estatus = "B";
					int actualizar = representantedao.updateRepresentante(representante);
					if(actualizar>0) {
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se actualizó el status para el representante");
						request.setAttribute("NOTIFICACIONES", notificaciones);
						request.setAttribute("REPRESENTANTE", representante);
						request.setAttribute("ACTUALIZACION_REPRESENTANTE", actualizar);
					}
					else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se pudo actualizar ese representante");
						request.setAttribute("NOTIFICACIONES",notificaciones);
					}
				}  //fin if
				else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado a ning&uacute;n representante");
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

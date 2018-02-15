package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.RepresentantesVO;


public class CommandGuardaRepresentantes implements Command{


	private String siguiente;


	public CommandGuardaRepresentantes(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		Notification notificaciones[] = new Notification[1];
		
		try{
			int sucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			String nombre = HTMLHelper.getParameterString(request, "nombre");
			float factor = HTMLHelper.getParameterFloat(request, "factor");
			
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
			
			RepresentantesDAO representantedao = new RepresentantesDAO();
			RepresentantesVO representante = new RepresentantesVO();
			
			representante.idSucursal = sucursal;
			representante.nombre = nombre;
			representante.factor = factor;
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
		
			representante.estatus = "A";	//se asume que al registrarse se da de alta
			
			representantedao.addRepresentante(representante);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("REPRESENTANTES", representante);
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

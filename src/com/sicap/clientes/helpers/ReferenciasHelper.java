package com.sicap.clientes.helpers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaComercialVO;
import com.sicap.clientes.vo.ReferenciaCrediticiaVO;
import com.sicap.clientes.vo.ReferenciaPersonalVO;

public class ReferenciasHelper {


		public  static ReferenciaPersonalVO getReferenciaPersonalVO( ReferenciaPersonalVO referenciaPersonal, HttpServletRequest request) throws Exception{
			referenciaPersonal.idReferencia = HTMLHelper.getParameterInt(request, "idReferencia");
			referenciaPersonal.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			referenciaPersonal.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			referenciaPersonal.nombre = HTMLHelper.getParameterString(request, "nombre");
			referenciaPersonal.telefono  = HTMLHelper.getParameterString(request, "telefono");
			referenciaPersonal.horarioLlamada = HTMLHelper.getParameterString(request, "horarioLlamada");
			referenciaPersonal.relacion  = HTMLHelper.getParameterString(request, "relacion");
			referenciaPersonal.tiempoConocimiento  = HTMLHelper.getParameterString(request, "tiempoConocimiento");
			referenciaPersonal.conocimientoOcupacion  = HTMLHelper.getParameterString(request, "conocimientoOcupacion");
			referenciaPersonal.visitaNegocio  = HTMLHelper.getParameterString(request, "visitaNegocio");
			referenciaPersonal.dondeVende  = HTMLHelper.getParameterString(request, "dondeVende");
			referenciaPersonal.tiempoOperacion  = HTMLHelper.getParameterString(request, "tiempoOperacion");
			referenciaPersonal.statusVentas  = HTMLHelper.getParameterString(request, "statusVentas");
			referenciaPersonal.conQuienVive  = HTMLHelper.getParameterString(request, "conQuienVive");
			referenciaPersonal.conocimientoVivienda  = HTMLHelper.getParameterString(request, "conocimientoVivienda");
			referenciaPersonal.impedimentoPago  = HTMLHelper.getParameterString(request, "impedimentoPago");
			referenciaPersonal.prestamoDinero  = HTMLHelper.getParameterString(request, "prestamoDinero");
			referenciaPersonal.recomendacionCredito  = HTMLHelper.getParameterString(request, "recomendacionCredito");
			referenciaPersonal.prestriaDinero  = HTMLHelper.getParameterString(request, "prestriaDinero");
			referenciaPersonal.disponibilidadRespaldo  = HTMLHelper.getParameterString(request, "disponibilidadRespaldo");
			referenciaPersonal.descripcionCliente  = HTMLHelper.getParameterString(request, "descripcionCliente");
			referenciaPersonal.calificacionCliente  = HTMLHelper.getParameterInt(request, "calificacionCliente");
			//referenciaPersonal.fechaRealizacionConsulta  =  new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaRealizacionConsulta").getTime());
			referenciaPersonal.fechaRealizacionConsulta  =  Convertidor.stringToSqlDate(request.getParameter("fechaRealizacionConsulta"));
			referenciaPersonal.direccion = HTMLHelper.getParameterString(request, "direccion");
			
			return referenciaPersonal;
		}


	public  static ReferenciaComercialVO getReferenciaComercialVO( ReferenciaComercialVO referenciaComercial, HttpServletRequest request) throws Exception{
		
		referenciaComercial.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		referenciaComercial.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		referenciaComercial.nombre = HTMLHelper.getParameterString(request, "nombre");
		referenciaComercial.telefono  = HTMLHelper.getParameterString(request, "telefono");
		referenciaComercial.horarioLlamada = HTMLHelper.getParameterString(request, "horarioLlamada");
		referenciaComercial.tiempoConocimiento  = HTMLHelper.getParameterString(request, "tiempoConocimiento");
		referenciaComercial.conocimientoOcupacion  = HTMLHelper.getParameterString(request, "conocimientoOcupacion");
		referenciaComercial.relacion  = HTMLHelper.getParameterString(request, "relacion");
		referenciaComercial.tipoProducto  = HTMLHelper.getParameterString(request, "tipoProducto");
		referenciaComercial.frecuenciaSurtido  = HTMLHelper.getParameterString(request, "frecuenciaSurtido");
		referenciaComercial.cantidadSurtido  = HTMLHelper.getParameterString(request, "cantidadSurtido");
		referenciaComercial.statusVentas  = HTMLHelper.getParameterString(request, "statusVentas");
		referenciaComercial.tipoPago  = HTMLHelper.getParameterString(request, "tipoPago");
		referenciaComercial.diasPago  = HTMLHelper.getParameterString(request, "diasPago");
		referenciaComercial.calidadPago  = HTMLHelper.getParameterString(request, "calidadPago");
		referenciaComercial.razonAtraso  = HTMLHelper.getParameterString(request, "razonAtraso");
		referenciaComercial.cantidadPersonal  = HTMLHelper.getParameterString(request, "cantidadPersonal");
		referenciaComercial.recomendacionCredito  = HTMLHelper.getParameterString(request, "recomendacionCredito");
		referenciaComercial.descripcionCliente  = HTMLHelper.getParameterString(request, "descripcionCliente");
		referenciaComercial.disponibilidadRespaldo  = HTMLHelper.getParameterString(request, "disponibilidadRespaldo");
		referenciaComercial.calificacionCliente  = HTMLHelper.getParameterInt(request, "calificacionCliente");
		referenciaComercial.fechaRealizacionConsulta  =  new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaRealizacionConsulta").getTime());
		return referenciaComercial;
	}


	public static ReferenciaCrediticiaVO[] getReferenciasCrediticias (ReferenciaCrediticiaVO[] referencias, HttpServletRequest request){

		ArrayList<ReferenciaCrediticiaVO> array = new ArrayList<ReferenciaCrediticiaVO>();
		ReferenciaCrediticiaVO referencia = null;

		String instituciones[] = request.getParameterValues("institucion");
		Logger.debug(instituciones.length + "");
		String numerosCreditos[] = request.getParameterValues("numCredito");
		String plazos[] = request.getParameterValues("plazo");
		String saldos[] = request.getParameterValues("saldo");
		String frecuenciasPago[] = request.getParameterValues("frecuenciaPago");
		
		for ( int i=0 ; i<3 ; i++ ){
			if ( instituciones[i]!=null && !instituciones[i].equals("") ){
				referencia = new ReferenciaCrediticiaVO();
				referencia.institucion = instituciones[i];
				referencia.numCredito = numerosCreditos[i];
				referencia.plazo = plazos[i];
				referencia.saldo = Double.parseDouble(saldos[i]);
				Logger.debug(referencia.saldo + "");
				referencia.frecuenciaPago = frecuenciasPago[i];
				array.add(referencia);
			}
		}
		if ( array.size()>0 ){
			referencias = new ReferenciaCrediticiaVO[array.size()];
		    for(int i=0;i<referencias.length; i++) referencias[i] = (ReferenciaCrediticiaVO)array.get(i);
		}

		return referencias;
	}


}

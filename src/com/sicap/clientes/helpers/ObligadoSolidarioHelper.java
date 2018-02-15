package com.sicap.clientes.helpers;


import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.EconomiaObligadoVO;
import com.sicap.clientes.vo.ObligadoSolidarioVO;


public class ObligadoSolidarioHelper{



	//Esde metodo ser� actualizado para funcionar como lo hace el getVo de ClienteHelper
	public static ObligadoSolidarioVO getVO (ObligadoSolidarioVO obligado, HttpServletRequest request) throws Exception{
		
		obligado.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		obligado.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		//obligado.rfc = request.getParameter("rfc").toUpperCase();
		obligado.rfc = request.getParameter("rfc");
		if ( obligado.rfc!=null )
			obligado.rfc = obligado.rfc.toUpperCase();
		obligado.fechaNacimiento = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaNacimiento").getTime());
		obligado.nombre = request.getParameter("nombre");
		obligado.aPaterno = request.getParameter("aPaterno");
		obligado.aMaterno = request.getParameter("aMaterno");
		obligado.sexo = HTMLHelper.getParameterInt(request, "sexo");
		obligado.nacionalidad = HTMLHelper.getParameterInt(request, "nacionalidad");
		obligado.tipoIdentificacion = HTMLHelper.getParameterInt(request, "tipoIdentificacion");
		obligado.numeroIdentificacion = request.getParameter("numeroIdentificacion");
		obligado.estadoCivil = HTMLHelper.getParameterInt(request, "estadoCivil");
		obligado.telefono = request.getParameter("telefono");
		//obligado.fechaFirmaSolicitud = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaFirmaSolicitud").getTime());
		obligado.fechaFirmaSolicitud = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaFirmaSolicitud"));

		obligado.empresa = request.getParameter("empresa");
		obligado.puesto = request.getParameter("puesto");
		obligado.telefonoExt = request.getParameter("telefonoExt");
		obligado.sueldoMensual = HTMLHelper.getParameterDouble(request, "sueldoMensual");
		obligado.direccionTrabajo = request.getParameter("direccionTrabajo");

		return obligado;

	}
	
	public static EconomiaObligadoVO getEconomiaObligadoVO (EconomiaObligadoVO economia, HttpServletRequest request) throws Exception{
		
		economia.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		economia.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		economia.idObligado = HTMLHelper.getParameterInt(request, "idObligado");
		economia.ocupacion = HTMLHelper.getParameterInt(request, "ocupacion");
		economia.frecuenciaIngresos = HTMLHelper.getParameterInt(request, "frecuenciaIngresos");
		economia.empresa = request.getParameter("empresa");
		economia.tipoContrato = HTMLHelper.getParameterInt(request, "tipoContrato");
		economia.salario = HTMLHelper.getParameterDouble(request, "salario");
		economia.pasivosFamiliares = HTMLHelper.getParameterDouble(request, "pasivosFamiliares");
		economia.activosFamiliares = HTMLHelper.getParameterDouble(request, "activosFamiliares");
		economia.ingresosFamiliares = HTMLHelper.getParameterDouble(request, "ingresosFamiliares");
		economia.gastosFamiliares = HTMLHelper.getParameterDouble(request, "gastosFamiliares");
		
		return economia;
		
	}
	
}
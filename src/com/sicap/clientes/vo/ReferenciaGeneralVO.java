package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class ReferenciaGeneralVO implements Serializable{

	public String contrato;
	public int numcliente;
	public String nombre;
	public String referencia;
	public Date fechaInicio;
	public int esT24;
	public int producto;
	public int numSolicitud;
	public int numSucursal;

	public ReferenciaGeneralVO(){
		contrato = null;
		numcliente = 0;
		nombre = null;
		referencia = null;
		fechaInicio = null;
		esT24 = 0;
		producto = 0;
		numSolicitud = 0;
		numSucursal = 0;
	}

	public String toString(){
		String  respuesta = null;
		respuesta = "contrato=[" + contrato + "],";
		respuesta += "numcliente=[" + numcliente+ "],";
		respuesta += "nombre=[" + nombre +"],";
		respuesta += "referencia=[" + referencia + "],";
		respuesta += "fechaInicio=[" + fechaInicio + "],";
		respuesta += "esT24=[" + esT24 + "],";
		respuesta += "producto=[" + producto +"],";
		respuesta += "numSolicitud=[" + numSolicitud + "],";
		respuesta += "numSucursal=[" + numSucursal + "]";

		return respuesta;
	}

}
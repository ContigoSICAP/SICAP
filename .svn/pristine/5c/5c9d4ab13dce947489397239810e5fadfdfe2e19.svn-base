package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class DisposicionVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int idDisposicion;
	public double monto;
	public TablaAmortizacionVO[] tablaAmostizaciones;
	public Timestamp fechaCaptura;


	public DisposicionVO (){
		idCliente = 0;
		idSolicitud = 0;
		idDisposicion = 0;
		monto = 0;
		tablaAmostizaciones = null;
		fechaCaptura = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "idDisposicion=["+idDisposicion+"],";
		respuesta += "monto=["+monto+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";		
		return respuesta;
	}


}

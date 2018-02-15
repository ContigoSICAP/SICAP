package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReferenciaCrediticiaVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int idReferencia;
	public String institucion;
	public String numCredito;
	public String plazo;
	public double saldo;
	public String frecuenciaPago;
	public Timestamp fechaCaptura;


	public ReferenciaCrediticiaVO() {
		this.idCliente = 0;
		this.idSolicitud = 0;
		this.idReferencia = 0;
		this.institucion = null;
		this.numCredito = null;
		this.plazo = null;
		this.saldo = 0;
		this.frecuenciaPago = null;
		this.fechaCaptura = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "idReferencia=["+idReferencia+"],";
		respuesta += "institucion=["+institucion+"],";
		respuesta += "numCredito=["+numCredito+"],";
		respuesta += "plazo=["+plazo+"],";
		respuesta += "saldo=["+saldo+"],";
		respuesta += "frecuenciaPago=["+frecuenciaPago+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";
		return respuesta;
	}


}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class LimiteCreditoVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int tasa;
	public int plazo;
	public int comision;
	public double monto;
	public boolean garantia;
	public String comentarios;
	public Timestamp fechaCaptura;
	public String usuario;
	
	public LimiteCreditoVO (){
		idCliente = 0;
		idSolicitud = 0;
		tasa = 0;
		plazo = 0;
		comision = 0;
		monto = 0;
		garantia = false;
		comentarios = null;
		fechaCaptura = null;
		usuario = null;
	}
	
	public String toString(){
		String respuesta;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "tasa=["+tasa+"],";
		respuesta += "plazo=["+plazo+"],";
		respuesta += "comision=["+comision+"],";
		respuesta += "monto=["+monto+"],";
		respuesta += "garantia=["+garantia+"],";
		respuesta += "comentarios=["+comentarios+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"],";
		respuesta += "usuario=["+usuario+"]";
		return respuesta;
	}
}

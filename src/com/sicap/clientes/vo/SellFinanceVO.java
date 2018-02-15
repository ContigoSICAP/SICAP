package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;


public class SellFinanceVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public String numeroFactura; 
	public int idMarca;
	public int idProducto;
	public int idPlan;
	public Timestamp fechaCaptura;


	public SellFinanceVO() {
		idCliente = 0;
		idSolicitud = 0;
		numeroFactura = null;
		idMarca = 0;
		idProducto = 0;
		idPlan = 0;
		fechaCaptura = null;
		
	}

	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "numeroFactura=["+numeroFactura+"],";
		respuesta += "idMarca=["+idMarca+"],";
		respuesta += "idProducto=["+idProducto+"],";
		respuesta += "idPlan=["+idPlan+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";
		
		return respuesta;
	}


}
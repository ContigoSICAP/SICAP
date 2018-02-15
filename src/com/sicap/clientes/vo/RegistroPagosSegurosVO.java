package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class RegistroPagosSegurosVO implements Serializable{

	public int idcliente;
	public int numsolicitud;
    public String referencia;
	public double montoCubierto;
	public double montoporCubrir;
	public Date fechaPago;
	public Timestamp fechaHora;
	public int bancoReferencia;
	public int idContabilidad;
	
	public RegistroPagosSegurosVO(){
		idcliente = 0;
		numsolicitud = 0;
		referencia = null;
		montoCubierto = 0;
		montoporCubrir = 0;
		fechaPago = null;		
		fechaHora = null;
		bancoReferencia=0;
		idContabilidad=0;
		
	
	}
	
	public String toString(){
		String respuesta = null;
		
		respuesta = "idcliente=[" + idcliente + "],";
		respuesta += "numsolicitud=[" + numsolicitud + "],";
		respuesta += "referencia=[" + referencia + "],";
		respuesta += "montoCubierto=[" + montoCubierto + "],";
		respuesta += "montoporCubrir=[" + montoporCubrir + "],";
		respuesta += "fechaPago=[" + fechaPago + "],";
		respuesta += "fechaHora=[" + fechaHora + "],";
		respuesta += "bancoReferencia=[" + bancoReferencia + "],";	
		respuesta += "idContabilidad=[" + idContabilidad + "],";
		return respuesta;
	}
}
	


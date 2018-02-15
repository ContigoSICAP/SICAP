package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;


public class IncidenciasMonitorPagosVO implements Serializable{

	public int numAlerta;
	public int visitaSupervisor;
	public int visitaGerente;
	public int visitaGestor;
	public Date fechaAlerta;
	public int numAtraso;
	public int numPago;
	public int estatus;
	public Date fechaRegistrada;
	
	

	public IncidenciasMonitorPagosVO(){

		numAlerta = 0;
		visitaSupervisor = 0;
		visitaGerente = 0;
		visitaGestor = 0;
		fechaAlerta = null;
		numAtraso = 0;
		numPago = 0;
		estatus = 0;
		fechaRegistrada = null;
	}

	public String toString(){
		String respuesta = null;

		respuesta = "numAlerta=["+numAlerta+"],";
		respuesta += "visitaSupervisor=["+visitaSupervisor+"],";
		respuesta += "visitaGerente=["+visitaGerente+"],";
		respuesta += "visitaGestor=["+visitaGestor+"],";
		respuesta += "fechaAlerta=["+fechaAlerta+"],";
		respuesta += "numAtraso=["+numAtraso+"],";
		respuesta += "numPago=["+numPago+"],";
		respuesta += "estatus=["+estatus+"],";
		respuesta += "fechaRegistrada=["+fechaRegistrada+"]";

		return respuesta;
	}

}

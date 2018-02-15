package com.sicap.clientes.vo;

import java.io.Serializable;


public class ComisionVO implements Serializable{


	public int id;
	public String descripcion;
	public double valor;
	public double porcentaje;
	public String status;


	public ComisionVO() {

		id = 0;
		descripcion = null;
		valor = 0;
		porcentaje = 0;
		status = null;

	}

	public String toString(){
		String respuesta = null;
		respuesta = "id=["+id+"],";
		respuesta += "descripcion=["+descripcion+"],";
		respuesta += "valor=["+valor+"],";
		respuesta += "porcentaje=["+porcentaje+"],";
		respuesta += "status=["+status+"]";
		return respuesta;
	}

}
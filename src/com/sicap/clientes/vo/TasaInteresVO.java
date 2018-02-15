package com.sicap.clientes.vo;

import java.io.Serializable;

public class TasaInteresVO implements Serializable{

	public int id;
	public String descripcion;
	public double valor;
	public double porcentaje;
	public String status;


	public TasaInteresVO() {

		id = 0;
		descripcion = null;
		valor = 0;
		status= null;
	}

}

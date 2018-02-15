package com.sicap.clientes.vo.inffinix;

import java.io.Serializable;

public class CodigoResultadoVO implements Serializable{
	
	public String codigoResultado;
	public String descripcion;
	
	public CodigoResultadoVO(){
		codigoResultado = null;
		descripcion = null;
	}
	
	public String toString(){
		String resultado;
		
		resultado = "codigoResultado=[" + codigoResultado + "],";
		resultado += "descripcion=[" + descripcion + "]";
		return resultado;
	}
}

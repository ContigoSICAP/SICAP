package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class RubrosVO  implements Serializable{
	
	public String tipoRubro;
	public double monto;
	public String status;
	public int origen;
	
	public String toString(){
		String respuesta = null;
		respuesta += "tipoRubro=[" + tipoRubro + "],";
		respuesta += "monto=[" + monto + "],";
		respuesta += "status=[" + status + "],";
		respuesta += "origen=[" + origen + "],";
		return respuesta;
	}
	

}

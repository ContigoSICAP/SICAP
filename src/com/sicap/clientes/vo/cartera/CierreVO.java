package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class CierreVO  implements Serializable{
	
	public int numCierre;
	public Date fechaInicio;
	public Date fechaFin;
	public String status;
	
	public String toString(){
		String respuesta = null;
		respuesta += "numCierre=[" + numCierre + "],";
		respuesta += "status=[" + status + "],";
		return respuesta;
	}
	

}

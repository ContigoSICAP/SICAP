package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class CatalogoCuentasVO  implements Serializable{
	
	public String numCuenta;
	public String nombreCuenta;
	public String naturalezaCuenta;
	public String tipoCuenta;
	
	public String toString(){
		String respuesta = null;
		respuesta += "numCuenta=[" + numCuenta + "],";
		respuesta += "nombreCuenta=[" + nombreCuenta + "],";
		respuesta += "naturalezaCuenta=[" + naturalezaCuenta + "],";
		respuesta += "tipoCuenta=[" + tipoCuenta + "],";
		return respuesta;
	}
	

}

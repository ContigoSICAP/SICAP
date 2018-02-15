package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;


public class BuroInternoVO implements Serializable{
	
	public int    numCliente;
	public int    numSucursal;
	public String apaterno;
	public String amaterno;
	public String nombre;
	public int    estatus;
	public int    motivoIngreso;
	public String descripcion;
	public Timestamp   fechaUltimaModificacion;
	public String usuarioUltimaModificacion;

	public BuroInternoVO() {
		numCliente         			= 0;
		numSucursal    				= 0;
		apaterno    				= null;
		amaterno 					= null;
		nombre           			= null;
		estatus           			= 0;
		motivoIngreso   			= 0;
		descripcion   				= null;
		fechaUltimaModificacion 	= null;
		usuarioUltimaModificacion	= null;
	}
	
	public String toString(){
		String respuesta = null;
		
		respuesta = "numCliente=["+numCliente+"],";
		respuesta += "numSucursal=["+numSucursal+"],";
		respuesta += "apaterno=["+apaterno+"],";
		respuesta += "amaterno=["+amaterno+"],";
		respuesta += "nombre=["+nombre+"],";
		respuesta += "estatus=["+estatus+"],";
		respuesta += "motivoIngreso=["+motivoIngreso+"],";
		respuesta += "descripcion=["+descripcion+"],";
		respuesta += "fechaUltimaModificacion=["+fechaUltimaModificacion+"],";
		respuesta += "usuarioUltimaModificacion=["+usuarioUltimaModificacion+"]";
		return respuesta;
	}

}

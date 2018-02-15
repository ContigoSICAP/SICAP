package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.Hashtable;

public class UsuarioVO implements Serializable{
	
	public String nombre;
	public String password;
	public String identificador;
        public String nombreCompleto;
	public Date   fechaCreacion;
	public Date   fechaVencimiento;
	public Date   fechaAcceso;
	public String status;
	public int    diasExpiracion;
	public int    intentosFallidos;
	public int    diasSinAccesar;
	public int	  idSucursal;
	public String[] roles;
	public SucursalVO[] sucursales;
	public Hashtable productos;
	

	public UsuarioVO() {
		nombre           = null;
		password         = null;
		identificador    = null;
                nombreCompleto   = null;
		fechaCreacion    = null;
		fechaVencimiento = null;
		fechaAcceso      = null;
		status           = null;
		diasExpiracion   = 0;
		diasSinAccesar   = 0;
		intentosFallidos = 0;
		idSucursal		 = 0;
		roles 			 = null;
		sucursales		 = null;
		productos		 = null;
		
	}
	
	public String toString(){
		String respuesta = null;
		respuesta = "nombre=["+nombre+"],";
		respuesta += "password=["+password+"],";
		respuesta += "identificador=["+identificador+"],";
                respuesta += "nombreCompleto=["+nombreCompleto+"],";
		respuesta += "idSucursal=["+idSucursal+"],";
		respuesta += "fechaCreacion=["+fechaCreacion+"],";
		respuesta += "fechaVencimiento=["+fechaVencimiento+"],";
		respuesta += "fechaAcceso=["+fechaAcceso+"],";
		respuesta += "status=["+status+"],";
		respuesta += "diasExpiracion=["+diasExpiracion+"],";
		respuesta += "intentosFallidos=["+intentosFallidos+"],";
		respuesta += "diasSinAccesar=["+diasSinAccesar+"]";
		return respuesta;
	}

}

package com.sicap.clientes.vo;

import java.io.Serializable;

public class BusquedaClientesVO implements Serializable{
	
	  public String RFC;
	  public int idCliente;
	  public String apellidoPaterno;
	  public String apellidoMaterno;
	  public String nombreS;
	  public int sucursal;
	  public SucursalVO[] sucursales;
	  
	  public BusquedaClientesVO (){
		     RFC = null;
		     idCliente = 0;
		     apellidoPaterno = null;
		     apellidoMaterno = null;
		     nombreS = null;
		     sucursal = 0;
		     sucursales = null;
			}
	
	  
	public String toString(){
		String respuesta = null;
		respuesta = "RFC=["+RFC+"],";
		respuesta = "idCliente=["+idCliente+"],";
		respuesta = "apellidoPaterno=["+apellidoPaterno+"],";
		respuesta = "apellidoMaterno=["+apellidoMaterno+"],";
		respuesta = "nombreS=["+nombreS+"],";
		respuesta = "sucursal=["+sucursal+"];";
    	return respuesta;
	}

}

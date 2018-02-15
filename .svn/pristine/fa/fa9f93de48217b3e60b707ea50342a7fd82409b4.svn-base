package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ArchivoAsociadoVO  implements Serializable{


	public int idCliente;
	public int idSolicitud;
	public int tipo;
	public int consecutivo;
	public String tipoCliente;
	public String nombre;
	public Timestamp fechaCaptura;
        public String estatusMigracion;


	public ArchivoAsociadoVO (){
		idCliente = 0;
		idSolicitud = 0;
		tipo = 0;
		consecutivo = 0;
		nombre = null;
		fechaCaptura = null;
		tipoCliente = null;
                estatusMigracion ="N";
	}
	//PROPIEDAD DE LA SO�ICITUD


	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta = "idSolicitud=["+idSolicitud+"],";
		respuesta += "tipo=["+tipo+"],";
		respuesta += "consecutivo=["+consecutivo+"],";
		respuesta += "tipoCliente=["+tipoCliente+"],";
		respuesta += "nombre=["+nombre+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";
                respuesta += "estatusMigracion = ["+estatusMigracion+"]";
		return respuesta;
	}

}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReporteCobranzaGrupalVO  implements Serializable{

	public int idAlerta;
	public int numCliente;
	//Temp de tranzaccion
	public String direccion;
	public String telefonos;
	
	public String nombreCliente;
	public String usuario;
	public Timestamp fechaCaptura;
	public boolean realizaPagos;
	public int receptorPagos;
	public int motivoNoContacto;
	public String receptorPagosOtro;
	public int numerofaltas;
	public boolean asesorVisitaSemanal;
	public boolean asesorPuntual;
	public boolean asesorProductivo;
	public boolean asesorRespeta;
	public boolean asesorRecibePagos;
	public String comentarios;

	
	public ReporteCobranzaGrupalVO (){
		idAlerta = 0;
		numCliente = 0;
		direccion = null;
		telefonos = null;
		nombreCliente = null;
		usuario = null;
		fechaCaptura = null;
		realizaPagos = false;
		motivoNoContacto = 0;
		receptorPagos = 0;
		receptorPagosOtro = null;
		numerofaltas = 0;
		asesorVisitaSemanal = false;
		asesorPuntual = false;
		asesorProductivo = false;
		asesorRespeta = false;
		asesorRecibePagos = false;
		comentarios = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "idAlerta=["+idAlerta+"],";
		respuesta += "numCliente=["+numCliente+"],";
		respuesta += "nombreCliente=["+nombreCliente+"],";
		respuesta += "usuario=["+usuario+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"],";
		respuesta += "realizaPagos=["+realizaPagos+"],";
		respuesta += "receptorPagos=["+receptorPagos+"],";
		respuesta += "motivoNoContanto=["+motivoNoContacto+"],";
		respuesta += "receptorPagosOtro=["+receptorPagosOtro+"],";
		respuesta += "numerofaltas=["+numerofaltas+"],";
		respuesta += "asesorVisitaSemanal=["+asesorVisitaSemanal+"],";
		respuesta += "asesorPuntual=["+asesorPuntual+"],";
		respuesta += "asesorProductivo=["+asesorProductivo+"],";
		respuesta += "asesorRespeta=["+asesorRespeta+"],";
		respuesta += "asesorRecibePagos=["+asesorRecibePagos+"],";
		respuesta += "comentarios=["+comentarios+"]";
		return respuesta;
	}

}

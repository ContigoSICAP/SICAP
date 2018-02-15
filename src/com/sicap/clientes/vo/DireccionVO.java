package com.sicap.clientes.vo;

import java.io.Serializable;


public class DireccionVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public String tabla;
	public int indiceTabla;
	public int idDireccion;
	public String estado;
	public int numestado;
	public int numMunicipio;
	public String municipio;
	public String ciudad;
	public int idColonia;
        public String idColoniaSepomex;
	public String colonia;
	public String calle;
	public String numeroExterior;
	public String numeroInterior;
	public String cp;
	public String asentamiento_cp;
	public int situacionVivienda;
	public String antDomicilio;
	public TelefonoVO telefonos[];
	public int idLocalidad;
	public String localidad;
	public int dirFommur;
        public int dirFommurDos;
        public int tipoVialidad;
        public int tipoAsentamiento;
        public String ambito;

	public DireccionVO(){

		idCliente = 0;
		idSolicitud = 0;
		tabla = null;
		indiceTabla = 0;
		idDireccion = 0;
		estado = null;
		numestado = 0;
		numMunicipio = 0;
		municipio = null;
		ciudad = null;
		colonia = null;
		calle = null;
		numeroExterior = null;
		numeroInterior = null;
		cp = null;
		asentamiento_cp = null;
		situacionVivienda = 0;
		antDomicilio = null;
		telefonos = null;
		idLocalidad = 0;
		localidad = null;
                tipoVialidad = 0;
                tipoAsentamiento = 0;
                idColoniaSepomex = null;
	}
	

	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "tabla=["+tabla+"],";
		respuesta += "indiceTabla=["+indiceTabla+"],";
		respuesta += "idDireccion=["+idDireccion+"],";
		respuesta += "estado=["+estado+"],";
		respuesta += "numestado=["+numestado+"],";
		respuesta += "numMunicipio=["+numMunicipio+"],";
		respuesta += "municipio=["+municipio+"],";
		respuesta += "ciudad=["+ciudad+"],";
		respuesta += "colonia=["+colonia+"],";
		respuesta += "calle=["+calle+"],";
		respuesta += "numeroExterior=["+numeroExterior+"],";
		respuesta += "numeroInterior=["+numeroInterior+"],";
		respuesta += "cp=["+cp+"],";
		respuesta += "asentamiento_cp=["+asentamiento_cp+"],";
		respuesta += "situacionVivienda=["+situacionVivienda+"],";
		respuesta += "antDomicilio=["+antDomicilio+"],";
		respuesta += "idLocalidad=["+idLocalidad+"],";
		respuesta += "Localidad=["+localidad+"],";
		respuesta += "dirFommur=["+dirFommur+"],";
                respuesta += "dirFommurDos=["+dirFommurDos+"]";
		respuesta += "dirFommur=["+dirFommur+"],";
                respuesta += "tipoVialidad=["+tipoVialidad+"],";
                respuesta += "tipoAsentamiento=["+tipoAsentamiento+"]";
		return respuesta;
	}


}

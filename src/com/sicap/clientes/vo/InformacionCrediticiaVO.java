package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class InformacionCrediticiaVO  implements Serializable{


	public int idCliente;
	public int idSolicitud;
	public int idObligado;
	public int idSociedad;
	public int idTipoRespuesta;
	public String respuesta;
	public Date fechaConsulta;
	public int idProvider;
        public String usuarioConsulta;

	public InformacionCrediticiaVO (){
		idCliente = 0;
		idSolicitud = 0;
		idObligado = 0;
		idSociedad = 0;
		idTipoRespuesta = 0;
		respuesta = "";
		fechaConsulta = null;
		idProvider = 0;
                usuarioConsulta = null;
	}

	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta = "idSolicitud=["+idSolicitud+"],";
		respuesta += "idObligado=["+idObligado+"],";		
		respuesta = "idSociedad=["+idSociedad+"],";
		respuesta = "idTipoRespuesta=["+idTipoRespuesta+"],";
		respuesta += "respuesta=["+respuesta+"],";
		respuesta += "fechaConsulta=["+fechaConsulta+"],";
		respuesta += "idProvider=["+idProvider+"],";
                respuesta += "usuarioConsulta=["+usuarioConsulta+"]";

		return respuesta;
	}

}

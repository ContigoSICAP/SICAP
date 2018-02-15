package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class ChequeVO implements Serializable{

	public int numCheque;
	public int numLote;
	public int numCliente;
	public int numSolicitud;
	public int numGrupo;
	public int numCiclo;
	public int estatus;
	public Date fechaAsignacion;
	public Date fechaCancelacion;
	public int tipoCancelacion;
	public String comentarios;

	public ChequeVO(){

		numCheque = 0;
		numLote = 0;
		numCliente = 0;
		numSolicitud = 0;
		numGrupo = 0;
		numCiclo = 0;
		estatus = 0;
		fechaAsignacion = null;
		fechaCancelacion = null;
		tipoCancelacion = 0;
		comentarios = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "numCheque=["+numCheque+"],";
		respuesta += "numLote=["+numLote+"],";
		respuesta += "numCliente=["+numCliente+"],";
		respuesta += "numSolicitud=["+numSolicitud+"],";
		respuesta += "numGrupo=["+numGrupo+"],";
		respuesta += "numCiclo=["+numCiclo+"],";
		respuesta += "estatus=["+estatus+"],";
		respuesta += "fechaAsignacion=["+fechaAsignacion+"],";
		respuesta += "fechaCancelacion=["+fechaCancelacion+"],";
		respuesta += "tipoCancelacion=["+tipoCancelacion+"],";
		respuesta += "comentarios=["+comentarios+"]";
		return respuesta;
	}

}

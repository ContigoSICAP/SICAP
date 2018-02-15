package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

/**
 * M�dulo: Gesti�n Seguros: Clase entidad de IncidenciasSegurosVO
 * @author jahtechnologies
 *
 */
public class IncidenciaSegurosVO implements Serializable {

	public Date fechaMovimiento;
	public String referencia;
	public int idCliente;	//numero de cliente SICAP
	public int tipoIncidencia;
	public String observaciones;
	public int bancoReferencia;
	public double montoseguroquenocubrio;
	
	public IncidenciaSegurosVO(){

		fechaMovimiento = null;
		referencia = null;
		idCliente = 0;
		tipoIncidencia = 0;
		observaciones = null;
		bancoReferencia=0;
		montoseguroquenocubrio=0.0;
	}

	public String toString(){

		String respuesta = null;
		respuesta = "fechaMovimiento=["+fechaMovimiento+"],";
		respuesta += "referencia=["+referencia+"],";
		respuesta += "idCliente=["+idCliente+"],";
		respuesta += "tipoIncidencia=["+tipoIncidencia+"],";
		respuesta += "observaciones=["+observaciones+"]";
		respuesta += "bancoReferencia=["+bancoReferencia+"]";
		respuesta += "montoseguroquenocubrio=["+montoseguroquenocubrio+"]";
		
		return respuesta;
	}
	
}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class EconomiaObligadoVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int idObligado;
	public int ocupacion;
	public int frecuenciaIngresos;
	public String empresa;
	public int tipoContrato;
	public double salario;
	public double pasivosFamiliares;
	public double activosFamiliares;
	public double ingresosFamiliares;
	public double gastosFamiliares;
	public Timestamp fechaCaptura;



	public EconomiaObligadoVO(){
		idCliente = 0;
		idSolicitud = 0;
		idObligado = 0;
		ocupacion = 0;
		frecuenciaIngresos = 0;
		empresa = null;
		tipoContrato = 0;
		salario = 0;
		pasivosFamiliares = 0;
		activosFamiliares = 0;
		ingresosFamiliares = 0;
		gastosFamiliares = 0;
		fechaCaptura = null;
	}



	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "idObligado=["+idObligado+"],";
		respuesta += "ocupacion=["+ocupacion+"],";
		respuesta += "frecuenciaIngresos=["+frecuenciaIngresos+"],";
		respuesta += "empresa=["+empresa+"],";
		respuesta += "tipoContrato=["+tipoContrato+"],";
		respuesta += "salario=["+salario+"],";
		respuesta += "pasivosFamiliares=["+pasivosFamiliares+"],";
		respuesta += "activosFamiliares=["+activosFamiliares+"],";
		respuesta += "ingresosFamiliares=["+ingresosFamiliares+"],";
		respuesta += "gastosFamiliares=["+gastosFamiliares+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";

		return respuesta;
	}


}

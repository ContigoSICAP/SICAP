package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class BitacoraBuroCirculoVO implements Serializable{

	public int numCuenta;
	public String referencia;
	public String mop;
	public int numDiasMora;
	public Double saldoActual;
	public Double saldoVencido;
	public int estatus;
	public Date fechaEnvio;
	public int cuentaEnviada;

	public BitacoraBuroCirculoVO(){

		numCuenta = 0;
		referencia = null;
		mop = null;
		numDiasMora = 0;
		saldoActual = 0.00;
		saldoVencido = 0.00;
		estatus = 0;
		fechaEnvio = null;
		cuentaEnviada = 0;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "numCuenta=["+numCuenta+"],";
		respuesta = "referencia=["+referencia+"],";
		respuesta = "mop=["+mop+"],";
		respuesta += "numDiasMora=["+numDiasMora+"],";
		respuesta += "saldoActual=["+saldoActual+"],";
		respuesta += "saldoVencido=["+saldoVencido+"],";
		respuesta += "estatus=["+estatus+"]";
		respuesta += "fechaEnvio=["+fechaEnvio+"]";
		respuesta += "cuentaEnviada=["+cuentaEnviada+"]";
		return respuesta;
	}

}
package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class PagoReferenciadoVO implements Serializable{

	public Date fecha_movimiento;
	public String referencia;
	public String descripcion;
	public double deposito;
	public double saldo;
	public String contrato;
	public boolean esT24;
	public int producto;
	public String nombre;
	public int banco;
	
	public PagoReferenciadoVO (){
		fecha_movimiento = null;
		referencia = null;
		descripcion = null;
		deposito = 0.0;
		saldo = 0.0;
		nombre = "";
		contrato = null;
		esT24 = false;
		producto = 0;
		banco  = 0;
	}

	public String toString(){
		String respuesta = null;
		respuesta =  "fecha_movimiento=["+fecha_movimiento+"],";
		respuesta += "referencia=["+referencia+"],";
		respuesta += "descripcion=["+descripcion+"],";
		respuesta += "deposito=["+deposito+"],";
		respuesta += "nombre=["+nombre+"],";
		respuesta += "contrato=["+nombre+"],";
		respuesta += "esT24=["+esT24+"],";
		respuesta += "producto=["+producto+"],";
		respuesta += "saldo=[" + saldo + "],";
		respuesta += "banco=[" + banco + "]";
		return respuesta;
	}
}

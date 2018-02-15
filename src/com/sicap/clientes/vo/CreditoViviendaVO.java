package com.sicap.clientes.vo;

import java.io.Serializable;


public class CreditoViviendaVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int tipoCredito;
	public int cofinanciado;
	public int tipoTasa;
	public double valorSolucion;
	public double derechos;
	public double gastosOperacion;
	public double valorAvaluo;
	public double impuestos;
	public double ahorro;
	public double subcuentaTitular;
	public double subcuentaConyuge;
	public String numCredito;
	public DireccionVO direccion;
	public String CLABEBancaria;

	public CreditoViviendaVO (){
		idCliente = 0;
		idSolicitud = 0;
		tipoCredito = 0;
		cofinanciado = 0;
		tipoTasa = 0;
		valorSolucion = 0;
		derechos = 0;
		gastosOperacion = 0;
		valorAvaluo = 0;
		impuestos = 0;
		ahorro = 0;
		subcuentaTitular = 0;
		subcuentaConyuge = 0;
		numCredito = null;
		direccion = null;
		CLABEBancaria = null;
	}
	
	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "tipoCredito=["+tipoCredito+"],";
		respuesta += "cofinanciado=["+cofinanciado+"],";
		respuesta += "tipoTasa=["+tipoTasa+"],";
		respuesta += "valorSolucion=["+valorSolucion+"],";
		respuesta += "derechos=["+derechos+"],";
		respuesta += "gastosOperacion=["+gastosOperacion+"],";
		respuesta += "valorAvaluo=["+valorAvaluo+"],";
		respuesta += "impuestos=["+impuestos+"],";
		respuesta += "ahorro=["+ahorro+"],";
		respuesta += "subcuentaTitular=["+subcuentaTitular+"],";
		respuesta += "subcuentaConyuge=["+subcuentaConyuge+"],";
		respuesta += "numCredito=["+numCredito+"],";
		respuesta += "CLABEBancaria=["+CLABEBancaria+"],";
		return respuesta;
	}
}

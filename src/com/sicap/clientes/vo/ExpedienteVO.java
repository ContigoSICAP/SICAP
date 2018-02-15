package com.sicap.clientes.vo;

import java.io.Serializable;


public class ExpedienteVO implements Serializable {
	
	public int idCliente;
	public int idSolicitud;
	public String solicitudcredito;
	public String idtitular;
	public String idsolidario;
	public String idaval;
	public String compdomicilio;
	public String autorizasic;
	public String consultatitular;
	public String consultasolidario;
	public String consultaavales;
	public String consultacirctitular;
	public String consultacircsolidario;
	public String consultacircavales;
	public String consultaintertitular;
	public String consultaintersolidario;
	public String consultainteravales;
	public String formatoevaluacion;
	public String perfiloperaciones;
	public String formatoref;
	public String formatocredito;
	public String bitacoracobranza;
	public String tablaamort;
	public String pagare;
	public String contratocredito;
	public String factgarantia;
	public String formatoseguro;
	public String facturabiengarantia;
	public String reglamentointerno;
	public String actaformaciongrupo;
	public String anexobgrupal;
	
	
	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "solicitudcredito=["+solicitudcredito+"],";
		respuesta += "idtitular=["+idtitular+"],";
		respuesta += "idsolidario=["+idsolidario+"],";
		respuesta += "idaval=["+idaval+"],";
		respuesta += "compdomicilio=["+compdomicilio+"],";
		respuesta += "autorizasic=["+autorizasic+"],";
		respuesta += "consultatitular=["+consultatitular+"],";
		respuesta += "consultasolidario=["+consultasolidario+"],";
		respuesta += "consultaavales=["+consultaavales+"],";
		respuesta += "consultacirctitular=["+consultacirctitular+"],";
		respuesta += "consultacircsolidario=["+consultacircsolidario+"],";
		respuesta += "consultacircavales=["+consultacircavales+"],";
		respuesta += "consultaintertitular=["+consultaintertitular+"],";
		respuesta += "consultaintersolidario=["+consultaintersolidario+"],";
		respuesta += "consultainteravales=["+consultainteravales+"],";
		respuesta += "formatoevaluacion=["+formatoevaluacion+"],";
		respuesta += "perfiloperaciones=["+perfiloperaciones+"],";
		respuesta += "formatoref=["+formatoref+"],";
		respuesta += "formatocredito=["+formatocredito+"],";
		respuesta += "bitacoracobranza=["+bitacoracobranza+"],";
		respuesta += "tablaamort=["+tablaamort+"],";
		respuesta += "pagare=["+pagare+"],";
		respuesta += "contratocredito=["+contratocredito+"],";
		respuesta += "factgarantia=["+factgarantia+"],";
		respuesta += "formatoseguro=["+formatoseguro+"],";
		respuesta += "facturabiengarantia=["+facturabiengarantia+"],";
		respuesta += "reglamentointerno=["+reglamentointerno+"],";
		respuesta += "actaformaciongrupo=["+actaformaciongrupo+"],";
		respuesta += "anexobgrupal=["+anexobgrupal+"],";
		return respuesta;
	}

}
package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;


public class EventosDePagoVO implements Serializable{

	public int numGrupo;
	public int numCiclo;
	public int numPago;
	public int identificador;
	public int numAtrasos;
	public int estatusVisitaSupervisor;
	public int estatusVisitaGerente;
	public int estatusVisitaGestor;
	public int estatusReporteCobranza;
	public String nombreGrupo;
	public String calificacion;
	public int situacionActualCredito; 
	public String enMora;
	public Date fechaAlertaSupervisor;
	public Date fechaAlertaGerente;
	public Date fechaAlertaGestor;
	public Date fechaReporteCobranza;
	public double montoPagar;
	public double montoPagado;
	public ReporteCobranzaGrupalVO[] reporteCobranza;
	public ReporteVisitaGrupalVO reporteVisita;
	public int diasVencidos;
	public double saldoTotalVencido;
	
	public int motivoElminacion;
	public String comentarioEliminacion;
	public int estatusRevisionMonitor;
	
	public boolean esIBS;


	public EventosDePagoVO(){

		numGrupo = 0;
		numCiclo = 0;
		numPago = 0;
		numAtrasos = 0;
		estatusVisitaSupervisor = 0;
		estatusVisitaGerente = 0;
		estatusVisitaGestor = 0;
		estatusReporteCobranza = 0;
		nombreGrupo = "";
		calificacion = "";
		situacionActualCredito = 0;
		enMora = "";
		identificador = -1;
		montoPagado = 0;
		montoPagar = 0;
		fechaAlertaSupervisor = null;
		fechaAlertaGerente = null;
		fechaAlertaGestor = null;
		fechaReporteCobranza = null;
		reporteCobranza = null;
		reporteVisita = null;
		motivoElminacion = 0;
		comentarioEliminacion = null;
		diasVencidos = 0;
		saldoTotalVencido = 0;
		estatusRevisionMonitor = 0;
		esIBS = false ; 
		
	}

	public String toString(){
		String respuesta = null;
		respuesta = "identificador=["+identificador+"],";
		respuesta = "numGrupo=["+numGrupo+"],";
		respuesta += "numCiclo=["+numCiclo+"],";
		respuesta += "nombreGrupo=["+nombreGrupo+"],";
		respuesta += "calificacion=["+calificacion+"],";
		respuesta += "situacionActualCredito=["+situacionActualCredito+"],";
		respuesta += "numPago=["+numPago+"],";
		respuesta += "numAtrasos=["+numAtrasos+"],";
		respuesta += "fechaReporteCobranza=["+fechaReporteCobranza+"],";
		respuesta += "estatusVisitaSupervisor=["+estatusVisitaSupervisor+"],";
		respuesta += "fechaAlertaSupervisor=["+fechaAlertaSupervisor+"],";
		respuesta += "estatusVisitaGerente=["+estatusVisitaGerente+"],";
		respuesta += "fechaAlertaGerente=["+fechaAlertaGerente+"],";
		respuesta += "fechaReporteCobranza=["+fechaReporteCobranza+"],";
		respuesta += "enMora=["+enMora+"],";
		respuesta += "estatusRevisionMonitor=["+estatusRevisionMonitor+"]";
		respuesta += ": motivoElminacion=["+motivoElminacion+"],";
		respuesta += "comentarioEliminacion=["+comentarioEliminacion+"]";
		return respuesta;
	}

}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.util.Date;

public class CuentaVO implements Serializable{

	private int    idCuenta;
	private String frecuencia;
	private String tipoCredito;
	private int	   tipoNegocio;	
	private String descTipoCredito;
	private String tipoCuenta;
	private String otorgante;
	private int    numeroPagos;
	private double limiteCredito;
	private double creditoMaximo;
	private double saldoActual;
	private double saldoVencido;
	private int    aPagar;
	private Date   fechaActualizacion;
	private Date   fechaApertura;
	private Date   fechaCierre;
	private Date   fechaUltimoPago;
	private String historial;
	private String cvePrevencion;
	private int    peorAtraso;
	private double saldoVencidoPeorAtraso;
	private Date   fechaPeorAtraso;
	
	public CuentaVO() {
		
		this.idCuenta               = 0;
		this.frecuencia             = null;
		this.tipoCredito            = null;
		this.descTipoCredito        = null;
		this.tipoNegocio			= 0;
		this.otorgante              = null;
		this.numeroPagos            = 0;
		this.limiteCredito          = 0;
		this.creditoMaximo          = 0;
		this.saldoActual            = 0;
		this.saldoVencido           = 0;
		this.aPagar                 = 0;
		this.fechaActualizacion     = null;
		this.fechaApertura          = null;
		this.fechaCierre            = null;
		this.fechaUltimoPago        = null;
		this.historial              = null;
		this.peorAtraso             = 0;
		this.saldoVencidoPeorAtraso = 0;
		this.fechaPeorAtraso        = null;
		
	}

	

	public int getAPagar() {
		return aPagar;
	}

	public void setAPagar(int pagar) {
		aPagar = pagar;
	}

	public double getCreditoMaximo() {
		return creditoMaximo;
	}

	public void setCreditoMaximo(double creditoMaximo) {
		this.creditoMaximo = creditoMaximo;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaPeorAtraso() {
		return fechaPeorAtraso;
	}

	public void setFechaPeorAtraso(Date fechaPeorAtraso) {
		this.fechaPeorAtraso = fechaPeorAtraso;
	}

	public Date getFechaUltimoPago() {
		return fechaUltimoPago;
	}

	public void setFechaUltimoPago(Date fechaUltimoPago) {
		this.fechaUltimoPago = fechaUltimoPago;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getHistorial() {
		return historial;
	}

	public void setHistorial(String historial) {
		this.historial = historial;
	}

	public String getCvePrevencion() {
		return cvePrevencion;
	}

	public void setCvePrevencion(String cvePrevencion) {
		this.cvePrevencion = cvePrevencion;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public int getNumeroPagos() {
		return numeroPagos;
	}

	public void setNumeroPagos(int numeroPagos) {
		this.numeroPagos = numeroPagos;
	}

	public String getOtorgante() {
		return otorgante;
	}

	public void setOtorgante(String otorgante) {
		this.otorgante = otorgante;
	}

	public int getPeorAtraso() {
		return peorAtraso;
	}

	public void setPeorAtraso(int peorAtraso) {
		this.peorAtraso = peorAtraso;
	}

	public double getSaldoActual() {
		return saldoActual;
	}

	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}

	public double getSaldoVencido() {
		return saldoVencido;
	}

	public void setSaldoVencido(double saldoVencido) {
		this.saldoVencido = saldoVencido;
	}

	public double getSaldoVencidoPeorAtraso() {
		return saldoVencidoPeorAtraso;
	}

	public void setSaldoVencidoPeorAtraso(double saldoVencidoPeorAtraso) {
		this.saldoVencidoPeorAtraso = saldoVencidoPeorAtraso;
	}

	public String getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(String tipoCredito) {
		this.tipoCredito = tipoCredito;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public int getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getDescTipoCredito() {
		return descTipoCredito;
	}

	public void setDescTipoCredito(String descTipoCredito) {
		this.descTipoCredito = descTipoCredito;
	}

	public int getTipoNegocio() {
		return tipoNegocio;
	}

	public void setTipoNegocio(int tipoNegocio) {
		this.tipoNegocio = tipoNegocio;
	}

}

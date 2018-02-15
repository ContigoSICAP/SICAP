package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class DescongelaPagoGarantiaIndVO  implements Serializable{
	
	private int numCliente;
	private int numSolicitud;
	private int numGrupo;
	private int numCiclo;
	private int numCredito;
	private String referencia;
	private String numDocumento;
	private String nombre_completo;
	private String nombre_grupo;
	private Date fechaDevolucion;
	private int numProducto;
	private int numSucursal;
	private String usuario;
	private double monto_credito;
	private double monto_desembolsado;
	private double monto_cuenta;
	// Se Agrega campo para la insercion del monto de cuenta Congelada
	private double monto_cuenta_congelada;
	private int status;
	private int num_pago;
	private double monto_pago;
	private int fondeador;
	
	public DescongelaPagoGarantiaIndVO(){
		numCliente  = 0;
		numSolicitud = 0;
		numGrupo  = 0;
		numCiclo  = 0;
		numCredito = 0;
		referencia = null;
		numDocumento = null;
		nombre_completo = null;
		nombre_grupo	= null;
		numProducto = 0;
		numSucursal = 0;
		usuario = "";
		monto_credito = 0;
		monto_desembolsado = 0;
		monto_cuenta = 0;
		monto_cuenta_congelada = 0;
		num_pago = 0;
		monto_pago = 0;
		status = 0;
		fondeador = 0;
		fechaDevolucion = null;
	}
	
	public String toString(){
		String respuesta = null;
		respuesta = "numCliente=["+numCliente+"],";
		respuesta += "numSolicitud=["+numSolicitud+"],";
		respuesta = "numGrupo=["+numGrupo+"],";
		respuesta += "numCiclo=["+numCiclo+"],";
		respuesta += "numCredito=["+numCredito+"],";
		respuesta += "referencia=["+referencia+"],";
		respuesta += "numDocumento=["+numDocumento+"],";
		respuesta += "numProducto=["+numProducto+"],";
		respuesta += "numSucursal=["+numSucursal+"],";
		respuesta += "usuario=["+usuario+"],";
		respuesta += "nombre_completo=["+nombre_completo+"],";
		return respuesta;
	}
	
	public int getNumCliente() {
		return numCliente;
	}

	public void setNumCliente(int numCliente) {
		this.numCliente = numCliente;
	}
	public int getNumGrupo() {
		return numGrupo;
	}

	public void setNumGrupo(int numGrupo) {
		this.numGrupo = numGrupo;
	}
	
	public int getNumCiclo() {
		return numCiclo;
	}

	public void setNumCiclo(int numCiclo) {
		this.numCiclo = numCiclo;
	}
	
	public int getNumSolicitud() {
		return numSolicitud;
	}

	public void setNumSolicitud(int numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	public int getNumCredito() {
		return numCredito;
	}

	public void setNumCredito(int numCredito) {
		this.numCredito = numCredito;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getNombreCompleto() {
		return nombre_completo;
	}

	public void setNombreCompleto(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}
	
	public String getNombreGrupo() {
		return nombre_completo;
	}

	public void setNombreGrupo(String nombre_grupo) {
		this.nombre_grupo = nombre_grupo;
	}
	
	public int getNumPago() {
		return num_pago;
	}

	public void setNumPago(int num_pago) {
		this.num_pago = num_pago;
	}

	public int getNumProducto() {
		return numProducto;
	}

	public void setNumProducto(int numProducto) {
		this.numProducto = numProducto;
	}
	
	public int getNumSucursal() {
		return numSucursal;
	}

	public void setNumSucursal(int numSucursal) {
		this.numSucursal = numSucursal;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	public double getMontoCredito() {
		return monto_credito;
	}

	public void setMontoCredito(double monto_credito) {
		this.monto_credito = monto_credito;
	}
	
	public double getMontoDesembolsado() {
		return monto_desembolsado;
	}

	public void setMontoDesembolsado(double monto_desembolsado) {
		this.monto_desembolsado = monto_desembolsado;
	}
	
	public double getMontoCuenta() {
		return monto_cuenta;
	}

	public void setMontoCuenta(double monto_cuenta) {
		this.monto_cuenta = monto_cuenta;
	}	

	public double getMontoPago() {
		return monto_pago;
	}

	public void setMontoPago(double monto_pago) {
		this.monto_pago = monto_pago;
	}
	
		
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFondeador() {
		return fondeador;
	}

	public void setFondeador(int fondeador) {
		this.fondeador = fondeador;
	}

	public void setMontoCuentaCongelada(double monto_cuenta_congelada) {
		this.monto_cuenta_congelada = monto_cuenta_congelada;
	}

	public double getMontoCuentaCongelada() {
		return monto_cuenta_congelada;
	}
	
	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion= fechaDevolucion;
	}
	
		
}

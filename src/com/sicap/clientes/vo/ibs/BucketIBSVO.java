package com.sicap.clientes.vo.ibs;

public class BucketIBSVO {
	/*
	 * Created on Jan 21, 2009
	 *
	 * To change the template for this generated file go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */

	/**
	 * @author jasvazher
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */

		private String numeroCredito;
		private String numeroCuenta;
		private String fechaTransaccion;
		private String conceptoTransaccion;
		private String montoTransaccion;
		private String tipoTransaccion;
		private String saldoCuenta;
		private String finRegistros;
		private String programa;
		private String fechaHoraSistema;
		//NUEVOS CAMPOS PARA ESTADO DE CUENTA SICAP
		private String fechaDeposito;
		private String montoAplicado;
		private String codigoBanco;
		


		/**
		 * @return
		 */
		public String getNumeroCredito() {
			return numeroCredito;
		}

		/**
		 * @return
		 */
		public String getNumeroCuenta() {
			return numeroCuenta;
		}

		/**
		 * @return
		 */
		public String getFechaTransaccion() {
			return fechaTransaccion;
		}

		/**
		 * @return
		 */
		public String getConceptoTransaccion() {
			return conceptoTransaccion;
		}

		/**
		 * @return
		 */
		public String getMontoTransaccion() {
			return montoTransaccion;
		}

		/**
		 * @return
		 */
		public String getTipoTransaccion() {
			return tipoTransaccion;
		}

		/**
		 * @return
		 */
		public String getSaldoCuenta() {
			return saldoCuenta;
		}

		/**
		 * @return
		 */
		public String getFinRegistro() {
			return finRegistros;
		}

		/**
		 * @return
		 */
		public String getPrograma() {
			return programa;
		}

		/**
		 * @return
		 */
		public String getFechaSistema() {
			return fechaHoraSistema;
		}

		
		/**
		 * @param string
		 */
		public void setNumeroCredito(String string) {
			numeroCredito = string;
		}

		/**
		 * @param string
		 */
		public void setNumeroCuenta(String string) {
			numeroCuenta = string;
		}

		/**
		 * @param string
		 */
		public void setFechaTransaccion(String string) {
			fechaTransaccion = string;
		}

		/**
		 * @param string
		 */
		public void setConceptoTransaccion(String string) {
			conceptoTransaccion = string;
		}

		/**
		 * @param string
		 */
		public void setMontoTransaccion(String string) {
			montoTransaccion = string;
		}

		/**
		 * @param string
		 */
		public void setTipoTransaccion(String string) {
			tipoTransaccion = string;
		}

		/**
		 * @param string
		 */
		public void setSaldoCuenta(String string) {
			saldoCuenta = string;
		}

		/**
		 * @param string
		 */
		public void setFinRegistro(String string) {
			finRegistros = string;
		}

		/**
		 * @param string
		 */
		public void setPrograma(String string) {
			programa = string;
		}

		/**
		 * @param string
		 */
		public void setFechaSistema(String string) {
			fechaHoraSistema = string;
		}

		/**
		 * @return the fechaDeposito
		 */
		public String getFechaDeposito() {
			return fechaDeposito;
		}

		/**
		 * @param fechaDeposito the fechaDeposito to set
		 */
		public void setFechaDeposito(String fechaDeposito) {
			this.fechaDeposito = fechaDeposito;
		}

		/**
		 * @return the montoAplicado
		 */
		public String getMontoAplicado() {
			return montoAplicado;
		}

		/**
		 * @param montoAplicado the montoAplicado to set
		 */
		public void setMontoAplicado(String montoAplicado) {
			this.montoAplicado = montoAplicado;
		}

		/**
		 * @return the codigoBanco
		 */
		public String getCodigoBanco() {
			return codigoBanco;
		}

		/**
		 * @param codigoBanco the codigoBanco to set
		 */
		public void setCodigoBanco(String codigoBanco) {
			this.codigoBanco = codigoBanco;
		}


		public String toString(){

			String salida = new String();
			salida = "numeroCredito=["+numeroCredito+"],";
			salida += "numeroCuenta=["+numeroCuenta+"],";
			salida += "fechaTransaccion=["+fechaTransaccion+"],";
			salida += "conceptoTransaccion=["+conceptoTransaccion+"],";
			salida += "tipoTransaccion=["+tipoTransaccion+"],";
			salida += "montoTransaccion=["+montoTransaccion+"],";
			salida += "saldoCuenta=["+saldoCuenta+"],";
			salida += "fechaDeposito=["+fechaDeposito+"]";
			return salida;

		}


}
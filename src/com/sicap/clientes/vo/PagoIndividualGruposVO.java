package com.sicap.clientes.vo;

import java.io.Serializable;

/**
 * Clase entidad de pago individual grupo
 * @author JahTechnologies
 *
 */
public class PagoIndividualGruposVO implements Serializable{

	/**
	 * Numero de grupo
	 */
	public int numGrupo;
	
	/**
	 * Numero de cliente
	 */
	public int numCliente;
	
	/**
	 * Numero de ciclo
	 */
	public int numCiclo;
	
	/**
	 * Numero de pago
	 */
	public int numPago;
	
	/**
	 * Monto
	 */
	public double monto;
	
	/**
	 * Usuario
	 */
	public String usuario;
	
	/**
	 * Corroborar - 0 - representa un pago sin procesar 1- un pago ya procesado
	 */
	public int corroborar;
	
	public String toString() {
		StringBuffer respuesta = new StringBuffer();
		respuesta.append("numGrupo=["+numGrupo+"],");
		respuesta.append("numCiclo=["+numCiclo+"],");
		respuesta.append("numCliente=["+numCliente+"],");
		respuesta.append("numPago=["+numPago+"],");
		respuesta.append("usuario=["+usuario+"],");
		respuesta.append("corroborar=["+corroborar+"],");
		respuesta.append("monto=["+monto+"]");
		return respuesta.toString();
	}
	
}

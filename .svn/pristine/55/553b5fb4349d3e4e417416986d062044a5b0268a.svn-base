package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

/**
 * Clase entidad de incidencias
 * @author JahTechnologies
 *
 */
public class IncidenciaPagoGrupalVO implements Serializable{

	/**
	 * Fecha de la incidencia
	 */
	public Date fecha;
	
	/**
	 * Numero de sucursal
	 */
	public int numSucursal;
	
	/**
	 * Nombre del grupo
	 */
	public String nombreGrupo;
	
	/**
	 * Monto esperado
	 */
	public double montoEsperado;
	
	/**
	 * Monto depositado
	 */
	public double montoDepositado;
	
	/**
	 * Diferencia
	 */
	public double diferencia;
	
	public String toString() {
		StringBuffer respuesta = new StringBuffer();
		respuesta.append("fecha=["+fecha.toString()+"],");
		respuesta.append("numSucursal=["+numSucursal+"],");
		respuesta.append("nombreGrupo=["+nombreGrupo+"],");
		respuesta.append("montoEsperado=["+montoEsperado+"],");
		respuesta.append("montoDepositado=["+montoDepositado+"],");
		respuesta.append("diferencia=["+diferencia+"],");
		return respuesta.toString();
	}
	
}

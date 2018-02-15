package com.sicap.clientes.vo;

import java.io.Serializable;
import java.util.Date;

public class ReporteCirculoEmpleoVO implements Serializable{

	private int idEmpleo;
	private String empresa;
	private String puesto;
	private double sueldo;
	private String calle;
	private String colonia;
	private String municipio;
	private String ciudad;
	private String estado;
	private String cp;
	private String telefono;
	private Date   fechaRegistro;
	
	public ReporteCirculoEmpleoVO() {
		this.idEmpleo  = 0;
		this.empresa  = null;
		this.puesto    = null;
		this.sueldo    = 0;
		this.calle     = null;
		this.colonia   = null;
		this.municipio = null;
		this.ciudad    = null;
		this.estado    = null;
		this.cp        = null;
		this.telefono  = null;
		this.fechaRegistro = null;
	}
	
	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public int getIdEmpleo() {
		return idEmpleo;
	}

	public void setIdEmpleo(int idEmpleo) {
		this.idEmpleo = idEmpleo;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public double getSueldo() {
		return sueldo;
	}

	public void setSueldo(double sueldo) {
		this.sueldo = sueldo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}

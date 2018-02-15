package com.sicap.clientes.vo;

import java.io.Serializable;
import java.util.Date;

public class PersonaVO implements Serializable{

	public PersonaVO() {
	}
	
	private String nombre = null;
	private String paterno = null;
	private String materno = null;
	private Date   nacimiento = null;
	private String rfc = null;
	private String curp = null;
	
	private Date   fechaConsulta = null;
	private double numConsulta   = 0;
	
	public String getCurp() {
		return curp;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	
	public Date getFechaConsulta() {
		return fechaConsulta;
	}
	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	public String getMaterno() {
		return materno;
	}
	public void setMaterno(String materno) {
		this.materno = materno;
	}
	public Date getNacimiento() {
		return nacimiento;
	}
	public void setNacimiento(Date nacimiento) {
		this.nacimiento = nacimiento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getNumConsulta() {
		return numConsulta;
	}
	public void setNumConsulta(double numConsulta) {
		this.numConsulta = numConsulta;
	}
	public String getPaterno() {
		return paterno;
	}
	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

}

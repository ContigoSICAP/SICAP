package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ConyugeVO implements Serializable{


	public int idCliente;
	public Date fechaEvaluacion;
	public String nombre;
	public String aPaterno;
	public String aMaterno;
	public String direccionDomicilio;
	public String telefonoDomicilio;
	public String direccionTrabajo;
	public String telefonoTrabajo;
	public String telefonoCelular;
	public Timestamp fechaCaptura;
	public String curp;
	public String rfc;
	public Date fechaNacimiento;
	public int sexo;
	public double sueldoMensual;
	public int formaIngreso;
	public int tipoSector;
	public int dependencia;
	

	public ConyugeVO(){
		idCliente = 0;
		fechaEvaluacion = null;
		nombre = null;
		aPaterno = null;
		aMaterno = null;
		direccionDomicilio = null;
		telefonoDomicilio = null;
		direccionTrabajo = null;
		telefonoTrabajo = null;
		telefonoCelular = null;
		fechaCaptura = null;
		curp = null;
		rfc = null;
		fechaNacimiento = null;
		sexo = 0;
		sueldoMensual = 0.00;
		formaIngreso = 0;
		tipoSector = 0;
		dependencia = 0;
	}


	
	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "fechaEvaluacion=["+fechaEvaluacion+"],";
		respuesta += "nombre=["+nombre+"],";
		respuesta += "aPaterno=["+aPaterno+"],";
		respuesta += "aMaterno=["+aMaterno+"],";
		respuesta += "direccionDomicilio=["+direccionDomicilio+"],";
		respuesta += "telefonoDomicilio=["+telefonoDomicilio+"],";
		respuesta += "direccionTrabajo=["+direccionTrabajo+"],";
		respuesta += "telefonoTrabajo=["+telefonoTrabajo+"],";
		respuesta += "telefonoCelular=["+telefonoCelular+"]";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";
		respuesta += "curp=["+curp+"]";
		respuesta += "rfc=["+rfc+"]";
		respuesta += "fechaNacimiento=["+fechaNacimiento+"]";
		respuesta += "sexo=["+sexo+"]";
		respuesta += "sueldoMensual=["+sueldoMensual+"]";
		respuesta += "formaIngreso=["+formaIngreso+"]";
		respuesta += "tipoSector=["+tipoSector+"]";
		respuesta += "dependencia=["+dependencia+"]";
		return respuesta;
	}

}

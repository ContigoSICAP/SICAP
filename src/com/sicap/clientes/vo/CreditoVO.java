package com.sicap.clientes.vo;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class CreditoVO implements Serializable{

	public int idCliente;
	public int idObligado;
	public int idSolicitud;
	public Date fechaConsulta;
	public Timestamp fechaCaptura;
	public int comportamiento;
	public int calificacionMesaControl;
	public String descripcion;
	public int tipoCredito;
	public int tipoCuenta;
	public int antCuenta;
	public int numBusquedaCuenta;
	public String respCrediticia;
	public Date fechaConsCrediticia;
        public int aceptaRegular;
        public boolean otraFin;



	public CreditoVO(){
		idCliente = 0;
		idObligado = 0;
		idSolicitud = 0;
		fechaConsulta = null;
		fechaCaptura = null;
		comportamiento = 0;
		calificacionMesaControl = 0;
		descripcion = null;
		tipoCredito = 0;
		tipoCuenta = 0;
		antCuenta = 0;
		numBusquedaCuenta = 0;
		respCrediticia = "";
		fechaConsCrediticia = null;
                aceptaRegular = 0;
                otraFin = false;
	}


	public String toString(){

		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idObligado=["+idObligado+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "fechaConsulta=["+fechaConsulta+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"],";
		respuesta += "comportamiento=["+comportamiento+"],";
		respuesta += "calificacionMesaControl=["+calificacionMesaControl+"],";
		respuesta += "descripcion=["+descripcion+"],";
		respuesta += "tipo=["+tipoCredito+"],";
		respuesta += "tipoCuenta=["+tipoCuenta+"],";
		respuesta += "antCuenta=["+antCuenta+"],";
		respuesta += "numBusquedaCuenta=["+numBusquedaCuenta+"],";
		//respuesta += "respuestaBuro=["+respCrediticia+"]";
		respuesta += "fechaConsCrediticia=["+fechaConsCrediticia+"],";
                respuesta += "aceptaRegular = ["+aceptaRegular+"],";
                respuesta += "otraFin = ["+otraFin+"]";
		
		return respuesta;
	
	}

    public CreditoVO(int idCliente, int idSolicitud) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
    }
        
}

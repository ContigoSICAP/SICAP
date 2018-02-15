package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class TablaAmortizacionVO implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int idDisposicion;
	public int numPago;
	public Date fechaPago;
	public double saldoInicial;
	public double abonoCapital;
	public double saldoCapital;
	public double comisionInicial;
	public double ivaComision;
	public double interes;
	public double ivaInteres;
	public double montoPagar;
	public double montoPagado;
	public int pagado;
	public int tipoAmortizacion;
        public double capitalAnticipado;
         public double incrementoCapital;

	public TablaAmortizacionVO(){
		idCliente = 0;
		idSolicitud = 0;
		idDisposicion = 0;
		numPago = 0;
		fechaPago = null;
		saldoInicial = 0;
		abonoCapital = 0;
		saldoCapital = 0;
		comisionInicial = 0;
		ivaComision = 0;
		interes = 0;
		ivaInteres = 0;
		montoPagar = 0;
		montoPagado = 0;
		pagado = 0;
		tipoAmortizacion = 0;
                capitalAnticipado =0;
                incrementoCapital=0;
                
	}

	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta += "idSolicitud=["+idSolicitud+"],";
		respuesta += "idDisposicion=["+idDisposicion+"],";
		respuesta += "numPago=["+numPago+"],";
		respuesta += "fechaPago=["+fechaPago+"],";
		respuesta += "saldoInicial=["+saldoInicial+"],";
		respuesta += "abonoCapital=["+abonoCapital+"],";
		respuesta += "saldoCapital=["+saldoCapital+"],";
		respuesta += "comisionInicial=["+comisionInicial+"],";
		respuesta += "ivaComision=["+ivaComision+"],";
		respuesta += "interes=["+interes+"],";
		respuesta += "ivaInteres=["+ivaInteres+"],";
		respuesta += "montoPagar=["+montoPagar+"],";
		respuesta += "montoPagado=["+montoPagado+"],";
		respuesta += "pagado=["+pagado+"]";
		respuesta += "tipoAmortizacion=["+tipoAmortizacion+"]";
                respuesta += "capitalAnticipado=["+capitalAnticipado+"]";
                respuesta += "incrementoCapital=["+incrementoCapital+"]";
		
		return respuesta;
	}
	
}

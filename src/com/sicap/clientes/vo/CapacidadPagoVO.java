package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class CapacidadPagoVO  implements Serializable{


	public int idCliente;
	public int idSolicitud;
	public int estatus;
	public double ingresosNomina;
	public double otrosNoComprobables;
	public int fuenteOtrosIngresos;
	public double otrosIngresos;
	public String marcaModeloAuto;
	public int estatusAuto;
	public double valorAuto;
	public double rentaVivienda;
	public double pagoDeuda;
	public double otrosGastos;
	public double disponibleMensual;
	public int cuotaSobreDisponible;
	public int cuotaSobreIngresoBruto;
	public Timestamp fechaCaptura;


	public CapacidadPagoVO (){
		idCliente = 0;
		idSolicitud = 0;
		estatus = 0;
		ingresosNomina = 0;
		otrosNoComprobables = 0;
		fuenteOtrosIngresos = 0;
		otrosIngresos = 0;
		marcaModeloAuto = null;
		estatusAuto = 0;
		valorAuto = 0;
		rentaVivienda = 0;
		pagoDeuda = 0;
		otrosGastos = 0;
		disponibleMensual = 0;
		cuotaSobreDisponible = 0;
		cuotaSobreIngresoBruto = 0;
		fechaCaptura = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=["+idCliente+"],";
		respuesta = "idSolicitud=["+idSolicitud+"],";
		respuesta += "estatus=["+estatus+"],";
		respuesta += "ingresosNomina=["+ingresosNomina+"],";
		respuesta += "otrosNoComprobables=["+otrosNoComprobables+"],";
		respuesta += "fuenteOtrosIngresos=["+fuenteOtrosIngresos+"],";
		respuesta += "otrosIngresos=["+otrosIngresos+"],";
		respuesta += "marcaModeloAuto=["+marcaModeloAuto+"],";
		respuesta += "estatusAuto=["+estatusAuto+"],";
		respuesta += "valorAuto=["+valorAuto+"],";
		respuesta += "rentaVivienda=["+rentaVivienda+"],";
		respuesta += "pagoDeuda=["+pagoDeuda+"],";
		respuesta += "otrosGastos=["+otrosGastos+"],";
		respuesta += "disponibleMensual=["+disponibleMensual+"],";
		respuesta += "cuotaSobreDisponible=["+cuotaSobreDisponible+"],";
		respuesta += "cuotaSobreIngresoBruto=["+cuotaSobreIngresoBruto+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"]";
		return respuesta;
	}

}

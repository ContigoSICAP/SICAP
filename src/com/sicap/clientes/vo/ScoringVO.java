package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ScoringVO  implements Serializable{

	public int idCliente;
	public int idSolicitud;
	public int estatus;

	public int calificacionSIC;
	public int tipoCuenta;
	public int antCuenta;
	public int numBuquedas;

	public int genero;
	public int edad;
	public int estadoCivil;
	public int antLaboral;
	public int tipoContrato;
	public int situacionVivienda;
	public double alquilerHipoteca;
	public int tiempoResidencia;
	public int dependientesEconomicos;

	public double ingresosNomina;
	public double otrosNoComprobables;
	public double otrosIngresos;
	public double rentaVivienda;
	public double pagoDeuda;
	public double otrosGastos;
	public double disponibleMensual;
	public double cuotaSobreDisponible;
	public double cuotaSobreIngresoBruto;

	public int nivelVivienda;
	public int calificacionZona;
	public int pisosVivienda;
	public int habitacionesVivienda;
	public int caracteristicasFachada;
	public int caracteristicasTecho;

	public int arraigoEmpresa;
	public int numeroEmpleados;
	public int jornada;
	public int plazoContrato;

	public int referencia1;
	public int referencia2;
	public int referenciaLaboral;
	public int referenciaArrendador;

	public int destinoCredito;

	public double monto;
	public int plazo;
	public int	periodicidad;
	public double montoConComision;
	public double cuota;
	public int tasa;
	public int comision;
	public double totalCostofinanciero;

	public int dictamenFinal;
	
	public double puntuacion;
	
	public int coberturaPago;
	
	public int tramo;
	
	public Timestamp fechaCaptura;

	public ScoringVO (){
		idCliente = 0;
		idSolicitud = 0;
		estatus = 0;

		calificacionSIC = 0;
		tipoCuenta = 0;
		antCuenta = 0;
		numBuquedas = 0;

		genero = 0;
		edad = 0;
		estadoCivil = 0;
		antLaboral = 0;
		tipoContrato = 0;
		situacionVivienda = 0;
		alquilerHipoteca = 0;
		tiempoResidencia = 0;
		dependientesEconomicos = 0;

		ingresosNomina = 0;
		otrosNoComprobables = 0;
		otrosIngresos = 0;
		rentaVivienda = 0;
		pagoDeuda = 0;
		otrosGastos = 0;
		disponibleMensual = 0;
		cuotaSobreDisponible = 0;
		cuotaSobreIngresoBruto = 0;

		nivelVivienda = 0;
		calificacionZona = 0;
		pisosVivienda = 0;
		habitacionesVivienda = 0;
		caracteristicasFachada = 0;
		caracteristicasTecho = 0;

		arraigoEmpresa = 0;
		numeroEmpleados = 0;
		jornada = 0;
		plazoContrato = 0;

		referencia1 = 0;
		referencia2 = 0;
		referenciaLaboral = 0;
		referenciaArrendador = 0;

		destinoCredito = 0;

		monto = 0;
		plazo = 0;
		periodicidad = 0;
		montoConComision = 0;
		cuota = 0;
		tasa = 0;
		comision = 0;
		totalCostofinanciero = 0;
		
		dictamenFinal = 0;
		
		puntuacion = 0;
		tramo = 0;
		
		coberturaPago = 0;
		
		fechaCaptura = null;
		
	}


	public String toString(){
		String respuesta = null;
		respuesta ="idCliente=["+idCliente+"],";
		respuesta +="idSolicitud=["+idSolicitud+"],";
		respuesta +="estatus=["+estatus+"],";
		respuesta +="calificacionSIC=["+calificacionSIC+"],";
		respuesta +="tipoCuenta=["+tipoCuenta+"],";
		respuesta +="antCuenta=["+antCuenta+"],";
		respuesta +="numBuquedas=["+numBuquedas+"],";
		respuesta +="genero=["+genero+"],";
		respuesta +="edad=["+edad+"],";
		respuesta +="estadoCivil=["+estadoCivil+"],";
		respuesta +="antLaboral=["+antLaboral+"],";
		respuesta +="tipoContrato=["+tipoContrato+"],";
		respuesta +="situacionVivienda=["+situacionVivienda+"],";
		respuesta +="alquilerHipoteca=["+alquilerHipoteca+"],";
		respuesta +="tiempoResidencia=["+tiempoResidencia+"],";
		respuesta +="dependientesEconomicos=["+dependientesEconomicos+"],";
		respuesta +="ingresosNomina=["+ingresosNomina+"],";
		respuesta +="otrosNoComprobables=["+otrosNoComprobables+"],";
		respuesta +="otrosIngresos=["+otrosIngresos+"],";
		respuesta +="rentaVivienda=["+rentaVivienda+"],";
		respuesta +="pagoDeuda=["+pagoDeuda+"],";
		respuesta +="otrosgastos=["+otrosGastos+"],";
		respuesta +="disponibleMensual=["+disponibleMensual+"],";
		respuesta +="cuotaSobreDisponible=["+cuotaSobreDisponible+"],";
		respuesta +="cuotaSobreIngrsoBruto=["+cuotaSobreIngresoBruto+"],";
		respuesta +="nivelVivienda=["+nivelVivienda+"],";
		respuesta +="calificacionZona=["+calificacionZona+"],";
		respuesta +="pisosVivienda=["+pisosVivienda+"],";
		respuesta +="habitacionesVivienda=["+habitacionesVivienda+"],";
		respuesta +="caracteristicasFachada=["+caracteristicasFachada+"],";
		respuesta +="caracteristicasTecho=["+caracteristicasTecho+"],";
		respuesta +="arraigoEmpresa=["+arraigoEmpresa+"],";
		respuesta +="numeroEmpleados=["+numeroEmpleados+"],";
		respuesta +="jornada=["+jornada+"],";
		respuesta +="plazoContrato=["+plazoContrato+"],";
		respuesta +="referencia1=["+referencia1+"],";
		respuesta +="referencia2=["+referencia2+"],";
		respuesta +="referenciaLaboral=["+referenciaLaboral+"],";
		respuesta +="referenciaArrendador=["+referenciaArrendador+"],";
		respuesta +="destinoCredito=["+destinoCredito+"],";
		
		respuesta +="monto=["+monto+"],";
		respuesta +="plazo=["+plazo+"],";
		respuesta +="periodicidad=["+periodicidad+"],";
		respuesta +="montoConComision=["+montoConComision+"],";
		respuesta +="cuota=["+cuota+"],";
		respuesta +="tasa=["+tasa+"],";
		respuesta +="comision=["+comision+"],";
		respuesta +="totalCostofinanciero=["+totalCostofinanciero+"],";
		
		respuesta +="dictamenFinal=["+dictamenFinal+"],";
		respuesta +="puntuacion=["+puntuacion+"],";
		respuesta +="tramo=["+tramo+"],";
		respuesta +="dictamenCapacidadPago=["+coberturaPago+"],";
		respuesta +="fechaCaptura=["+fechaCaptura+"]";
		return respuesta;
	}

}

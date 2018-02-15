package com.sicap.clientes.helpers;

import java.util.HashMap;
import java.util.Map;


public class CatalogoCDC {

	Map<String, String> frecuencia  = new HashMap<String, String>();
	Map<String, String> moneda      = new HashMap<String, String>();
	Map<String, String> tipoCredito = new HashMap<String, String>();
	Map<String, String> tipoCuenta  = new HashMap<String, String>();
	
	public CatalogoCDC() {
		initFrecuenciaPagos();
		initTipoMoneda();
		initTipoCredito();
		initTipoCuenta();
	}
	
	public String getFrecuencia(String key){
		String keyCDC = "";
		try{
		keyCDC = frecuencia.get(key);
		}catch(NullPointerException e){
			return keyCDC;
		}
		return keyCDC;
	}
	
	public String getMoneda(String key){
		String keyCDC = "";
		try{
		keyCDC = moneda.get(key);
		}catch(NullPointerException e){
			return keyCDC;
		}
		return keyCDC;
	}
	
	public String getTipoCredito(String key){
		String keyCDC = "";
		try{
		keyCDC = tipoCredito.get(key);
		}catch(NullPointerException e){
			return keyCDC;
		}
		return keyCDC;
	}
	
	public String getTipoCuenta(String key){
		String keyCDC = "";
		try{
		keyCDC = tipoCuenta.get(key);
		}catch(NullPointerException e){
			return keyCDC;
		}
		return keyCDC;
	}
	
	private void initFrecuenciaPagos(){
		frecuencia.put("B", "BIMESTRAL");
		frecuencia.put("M", "MENSUAL");
		frecuencia.put("Q", "QUINCENAL");
		frecuencia.put("A", "ANUAL");
		frecuencia.put("V", "VARIABLE");
		frecuencia.put("C", "CATORCENAL");
		frecuencia.put("T", "TRIMESTRAL");
		frecuencia.put("S", "SEMANAL");
		frecuencia.put("D", "DEDUCCIÓN DEL SALARIO");
		frecuencia.put("R", "PAGO MÁNIMO PARA CUENTAS REVOLVENTES");
		frecuencia.put("U", "UNA SOLA EXHIBICION");
	}
	
	private void initTipoMoneda(){
		moneda.put("MX", "PESOS MEXICANOS");
		moneda.put("US", "DÓLARES AMERICANOS");
		moneda.put("UD", "UNIDADES DE INVERSIÓN");
	}
	
	private void initTipoCredito(){
		tipoCredito.put("AA", "ARRENDAMIENTO AUTOMOTRIZ");
		tipoCredito.put("AB", "AUTOMOTRIZ BANCARIO");
		tipoCredito.put("AE", "FISICA ACTIVIDAD EMPRESARIAL");
		tipoCredito.put("AM", "APARATOS/MUEBLES");
		tipoCredito.put("AR", "ARRENDATARIO");
		tipoCredito.put("AV", "AVIACION");
		tipoCredito.put("BC", "BANCA COMUNAL");
		tipoCredito.put("BL", "BOTE/LANCHA");
		tipoCredito.put("BR", "BIENES RAICES");
		tipoCredito.put("CA", "COMPRA DE AUTOMÓVIL");
		tipoCredito.put("CC", "CREDITO AL CONSUMO");
		tipoCredito.put("CF", "CREDITO FISCAL");
		tipoCredito.put("CO", "CONSOLIDACION");
		tipoCredito.put("CP", "CREDITO PERSONAL AL CONSUMO");
		tipoCredito.put("ED", "EDITORIAL");
		tipoCredito.put("EQ", "EQUIPO");
		tipoCredito.put("FF", "FONDEO FIRA");
		tipoCredito.put("FI", "FIANZA");
		tipoCredito.put("FT", "FACTORAJE");
		tipoCredito.put("GS", "GRUPO SOLIDARIO");
		tipoCredito.put("HB", "HIPOTECARIO BANCARIO");
		tipoCredito.put("HE", "PRESTAMO TIPO HOME EQUITY");
		tipoCredito.put("HV", "HIPOTECARIO O VIVIENDA");
		tipoCredito.put("LC", "LINEA DE CREDITO");
		tipoCredito.put("MC", "MEJORA A LA CASA");
		tipoCredito.put("NC", "DESCONOCIDO");
		tipoCredito.put("ND", "NO DISPONIBLE");
		tipoCredito.put("NG", "PRESTAMO NO GARANTIZADO");
		tipoCredito.put("OT", "OTROS (MULTIPLES CREDITOS)");
		tipoCredito.put("PB", "PRESTAMO PERSONAL BANCARIO");
		tipoCredito.put("PC", "PROCAMPO");
		tipoCredito.put("PE", "PRESTAMO PARA ESTUDIANTE");
		tipoCredito.put("PG", "PRESTAMO GARANTIZADO");
		tipoCredito.put("PM", "PRESTAMO EMPRESARIAL");
		tipoCredito.put("PP", "PRESTAMO PERSONAL");
		tipoCredito.put("SH", "SEGUNDA HIPOTECA");
		tipoCredito.put("TC", "TARJETA DE CREDITO");
		tipoCredito.put("TD", "TARJETA DEPARTAMENTAL");
		tipoCredito.put("TG", "TARJETA GARANTIZADA");
		tipoCredito.put("TS", "TARJETA DE SERVICIOS");
		tipoCredito.put("UI", "USO INTERNO");
		tipoCredito.put("VR", "VEHICULO RECREATIVO");
	}
	
	private void initTipoCuenta(){
		tipoCuenta.put("F", "PAGOS FIJOS");
		tipoCuenta.put("H", "HIPOTECA");
		tipoCuenta.put("L", "SIN LÍMITE PREESTABLECIDO");
		tipoCuenta.put("R", "REVOLVENTE");
		tipoCuenta.put("M", "MONITOREO");
		tipoCuenta.put("Q", "QUIROGRAFARIO");
		tipoCuenta.put("A", "CRÉDITO DE HABILITACIÓN DE AVIO");
		tipoCuenta.put("E", "CRÉDITO REFACCIONARIO");
		tipoCuenta.put("P", "CRÉDITO PRENDARIO");
	}

}

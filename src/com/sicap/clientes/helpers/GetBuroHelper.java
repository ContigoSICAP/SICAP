package com.sicap.clientes.helpers;

import java.util.Hashtable;

public class GetBuroHelper{

	//Valida en la cadena si es fin de segmento
	public static boolean finSegmento(String cveSegmento){
		
		String[] segmentos = new String[11];
		segmentos[0] = "PN";
		segmentos[1] = "PA";
		segmentos[2] = "PE";
		segmentos[3] = "TL";
		segmentos[4] = "IQ";
		segmentos[5] = "RS";
		segmentos[6] = "HI";
		segmentos[7] = "HR";
		segmentos[8] = "CR";
		segmentos[9] = "SC";
		segmentos[10] = "ES";
		
		for (int i=0; i<=10;i++){
			if (cveSegmento.equals(segmentos[i]))
				return true;
		}

		return false;
	}
	
	public static Integer stringToInteger(String valor){
		
		Integer resp = 0;
		if (valor != null)
			resp = Integer.valueOf(valor);

		return resp;
	}	
	
	//Formatea una fecha sin slash
	public static String formatDate(String valor){
		
		if (valor.length() == 8){
			valor = valor.substring(0, 2) + "/" + valor.substring(2, 4) + "/" + valor.substring(4, 8);
		}
			return valor;
	}

	//Obtiene descripci�n del tipo de contrato en base a su clave
	public static String getTipoContrato(String cveContrato){
		
		Hashtable<String, String> contrato = new Hashtable<String, String>();
		contrato.put("AF", "Aparatos/Muebles");
		contrato.put("AG", "Agropecuario(PFAE))");
		contrato.put("AL", "Arrendamiento automotriz");
		contrato.put("AP", "Aviación");
		contrato.put("AU", "Compra de automóvil");
		contrato.put("BD", "Fianza");
		contrato.put("BT", "Bote/Lancha");
		contrato.put("CC", "Tarjeta de crédito");
		contrato.put("CE", "Cartas de crédito(PFAE)");
		contrato.put("CF", "Crédito fiscal");
		contrato.put("CL", "Línea de crédito");
		contrato.put("CO", "Consolidación");
		contrato.put("CS", "Crédito simple(PFAE)");
		contrato.put("CT", "Con colateral(PFAE)");
		contrato.put("DE", "Descuentos(PFAE)");
		contrato.put("EQ", "Equipo");
		contrato.put("FI", "Fideicomiso(PFAE)");
		contrato.put("FT", "Factoraje");
		contrato.put("HA", "Habilitación o avío(PFAE)");
		contrato.put("HE", "Préstamo tipo Home Equity");
		contrato.put("HI", "Mejoras a la casa");
		contrato.put("LS", "Arrendamiento");
		contrato.put("MI", "Otros");
		contrato.put("OA", "Otros adeudos vencidos(PFAE)");
		contrato.put("PA", "Préstamo para personas físicas con actividad empresarial(PFAE)");
		contrato.put("PB", "Editorial");
		contrato.put("PG", "PGUE-Préstamo como garantía de unidades industriales(PFAE)");
		contrato.put("PL", "Préstamo personal");
		contrato.put("PR", "Prendario(PFAE)");
		contrato.put("PQ", "Quirografario(PFAE)");
		contrato.put("RC", "Reestructurado(PFAE)");
		contrato.put("RD", "Redescuento(PFAE)");
		contrato.put("RE", "Bienes raíces");
		contrato.put("RF", "Refaccionario(PFAE)");
		contrato.put("RN", "Renovado(PFAE)");
		contrato.put("RV", "Vehículo recreativo");
		contrato.put("SC", "Tarjeta garantizada");
		contrato.put("SE", "Préstamo garantizado");
		contrato.put("SG", "Seguros");
		contrato.put("SM", "Segunda hipoteca");
		contrato.put("ST", "Préstamo para estudiante");
		contrato.put("TE", "Tarjeta de crédito empresarial");
		contrato.put("UK", "Desconocido");
		contrato.put("US", "Préstamo no garantizado");
		
		String resp = (String)contrato.get(cveContrato);

		return resp;
	}
	
	//Obtiene el tipo de responsabilidad del contrato en base a su clave
	public static String getResponsabilidad(String cveResp){
		
		Hashtable<String, String> responsabilidad = new Hashtable<String, String>();
		responsabilidad.put("I", "Individual");
		responsabilidad.put("J", "Mancomunado");
		responsabilidad.put("C", "Obligado solidario");
		responsabilidad.put("A", "Usuario autorizado");
		
		String resp = (String)responsabilidad.get(cveResp);

		return resp;
	}
	
	//Obtiene el tipo de cuenta en base a su clave
	public static String getTipoCuenta(String cveCuenta){
		
		Hashtable<String, String> cuenta = new Hashtable<String, String>();
		cuenta.put("I", "Pagos fijos");
		cuenta.put("M", "Hipoteca");
		cuenta.put("O", "Sin límite preestablecido");
		cuenta.put("R", "Revolvente");
		
		String resp = (String)cuenta.get(cveCuenta);

		return resp;
	}

	//Obtiene la frecuencia con la que el usuario realiza el pago
	public static String getFrecuencia(String cveFrecuencia){
		
		Hashtable<String, String> frecuencia = new Hashtable<String, String>();
		frecuencia.put("B", "Bimestral");
		frecuencia.put("D", "Diario");
		frecuencia.put("H", "Por hora");
		frecuencia.put("K", "Catorcenal");
		frecuencia.put("M", "Mensual");
		frecuencia.put("P", "Deducciún del salario");
		frecuencia.put("Q", "Trimetral");
		frecuencia.put("S", "Quincenal");
		frecuencia.put("W", "Semanal");
		frecuencia.put("Y", "Anual");
		frecuencia.put("Z", "Pago mínimo");
		
		String resp = (String)frecuencia.get(cveFrecuencia);
		if (resp == null)
			resp = "N/A";

		return resp;
	}
	
	//Obtiene el tipo de MOP
	public static String getMOP(String cveMOP){
		
		Hashtable<String, String> MOP = new Hashtable<String, String>();
		MOP.put("UR", "Cuenta sin información");
		MOP.put("00", "Muy reciente para ser informada");
		MOP.put("01", "Cuenta al corriente");
		MOP.put("02", "Atraso de 01 a 29 días");
		MOP.put("03", "Atraso de 30 a 59 días");
		MOP.put("04", "Atraso de 60 a 89 días");
		MOP.put("05", "Atraso de 90 a 119 días");
		MOP.put("06", "Atraso de 120 a 149 días");
		MOP.put("07", "Atraso de 150 días hasta 12 meses");
		MOP.put("96", "Atraso de 12 meses");
		MOP.put("97", "Cuenta con deuda parcial o total sin recuperar");
		MOP.put("99", "Fraude cometido por el cliente");
		
		String resp = (String)MOP.get(cveMOP);

		return resp;
	}

	//Obtiene el orden de los MOPS para el resumen de los cr�ditos 
	public static String getOrderMOP(int cveMOP){
		
		Hashtable<Integer,String> MOP = new Hashtable<Integer,String>();
		MOP.put(0, "UR");
		MOP.put(1, "00");
		MOP.put(2, "01");
		MOP.put(3, "02");
		MOP.put(4, "03");
		MOP.put(5, "04");
		MOP.put(6, "05");
		MOP.put(7, "06");
		MOP.put(8, "07");
		MOP.put(9, "96");
		MOP.put(10, "97");
		MOP.put(11, "99");
		
		String resp = (String)MOP.get(cveMOP);

		return resp;
	}

	//Obtiene el orden de los MOPS para el resumen de los cr�ditos 
	public static String monthConvert(String fecha){
		
		Hashtable<String, String> month = new Hashtable<String, String>();
		String resp = "";
		month.put("01", "Ene");
		month.put("02", "Feb");
		month.put("03", "Mar");
		month.put("04", "Abr");
		month.put("05", "May");
		month.put("06", "Jun");
		month.put("07", "Jul");
		month.put("08", "Ago");
		month.put("09", "Sep");
		month.put("10", "Oct");
		month.put("11", "Nov");
		month.put("12", "Dic");
		
		if (fecha.length() == 8){
			String mes = fecha.substring(2, 4);
			resp = (String)month.get(mes) + "/" + fecha.substring(6, 8);
		}else{
			resp = " ";
		}
		
		return resp;
	}

	
}
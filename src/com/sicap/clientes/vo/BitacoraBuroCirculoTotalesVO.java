package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class BitacoraBuroCirculoTotalesVO implements Serializable{
	
	public int totalCuentas;
	public int totalCuentasMora;
	public int totalCuentasCerradas;
	public double totalSaldosActuales;
	public double totalSaldosVencidos;
	public Date fechaEnvio;
	
	public BitacoraBuroCirculoTotalesVO(){
		totalCuentas = 0;
		totalCuentasMora = 0;
		totalCuentasCerradas = 0;
		totalSaldosActuales = 0.0;
		totalSaldosVencidos = 0.0;
		fechaEnvio = null;
	}
	
	public String toString(){
		String respuesta = null;
		
		respuesta = "totalCuentas=[" + totalCuentas + "],";
		respuesta += "totalCuentasMora=[" + totalCuentasMora + "],";
		respuesta += "totalCuentasCerradas=[" + totalCuentasCerradas + "],";
		respuesta += "totalSaldosActuales=[" + totalSaldosActuales + "],";
		respuesta += "totalSaldosVencidos=[" + totalSaldosVencidos + "],";
		respuesta += "fechaEnvio=[" + fechaEnvio + "]";
		
		return respuesta;
	}
}
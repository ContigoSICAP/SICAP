package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author avillanueva
 */
public class SaldoFondeadorVO implements Serializable{
    
    public int numFondeador;
    public String nombreFondeador;
    public String tipoMov;
    public double saldoInicial;
    public double saldoFinal;
    public double monto;
    public double montoDispersion;
    public double montoCancelacion;
    public double montoCobranza;
    public int numTransaccion;
    public int numCliente;
    public int numCredito;
    public int origen;
    public Date fechaIngreso;

    public String toString() {
        String respuesta = null;
        respuesta = "numFondeador=[" + numFondeador + "],";
        respuesta += "nombreFondeador=[" + nombreFondeador + "],";
        respuesta += "tipoMov=[" + tipoMov + "],";
        respuesta += "saldoInicial=[" + saldoInicial + "],";
        respuesta += "saldoFinal=[" + saldoFinal + "],";
        respuesta += "monto=[" + monto + "],";
        respuesta += "montoDispersion=[" + montoDispersion + "],";
        respuesta += "montoCancelacion=[" + montoCancelacion + "],";
        respuesta += "montoCobranza=[" + montoCobranza + "],";
        respuesta += "numTransaccion=[" + numTransaccion + "],";
        respuesta += "numCliente=[" + numCliente + "],";
        respuesta += "numCredito=[" + numCredito + "],";
        respuesta += "origen=[" + origen + "],";
        respuesta += "fechaIngreso=[" + fechaIngreso + "]";
        return respuesta;
    }
    
    public SaldoFondeadorVO() {
    }

    public SaldoFondeadorVO(int numFondeador, String nombreFondeador, double saldoInicial, double saldoFinal, double montoDispersion, double montoCancelacion, double montoCobranza) {
        this.numFondeador = numFondeador;
        this.nombreFondeador = nombreFondeador;
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
        this.montoDispersion = montoDispersion;
        this.montoCancelacion = montoCancelacion;
        this.montoCobranza = montoCobranza;
    }

    public SaldoFondeadorVO(int numFondeador, double monto) {
        this.numFondeador = numFondeador;
        this.monto = monto;
    }

    public SaldoFondeadorVO(int numFondeador, String tipoMov, double saldoFinal, double monto, int numTransaccion, int numCliente, int numCredito, int origen) {
        this.numFondeador = numFondeador;
        this.tipoMov = tipoMov;
        this.saldoFinal = saldoFinal;
        this.monto = monto;
        this.numTransaccion = numTransaccion;
        this.numCliente = numCliente;
        this.numCredito = numCredito;
        this.origen = origen;
    }

    public SaldoFondeadorVO(double montoCobranza, Date fechaIngreso) {
        this.montoCobranza = montoCobranza;
        this.fechaIngreso = fechaIngreso;
    }

    public int getNumFondeador() {
        return numFondeador;
    }

    public void setNumFondeador(int numFondeador) {
        this.numFondeador = numFondeador;
    }

    public String getNombreFondeador() {
        return nombreFondeador;
    }

    public void setNombreFondeador(String nombreFondeador) {
        this.nombreFondeador = nombreFondeador;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMontoDispersion() {
        return montoDispersion;
    }

    public void setMontoDispersion(double montoDispersion) {
        this.montoDispersion = montoDispersion;
    }

    public double getMontoCancelacion() {
        return montoCancelacion;
    }

    public void setMontoCancelacion(double montoCancelacion) {
        this.montoCancelacion = montoCancelacion;
    }

    public double getMontoCobranza() {
        return montoCobranza;
    }

    public void setMontoCobranza(double montoCobranza) {
        this.montoCobranza = montoCobranza;
    }

    public int getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public int getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(int numCliente) {
        this.numCliente = numCliente;
    }

    public int getNumCredito() {
        return numCredito;
    }

    public void setNumCredito(int numCredito) {
        this.numCredito = numCredito;
    }

    public String getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(String tipoMov) {
        this.tipoMov = tipoMov;
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}

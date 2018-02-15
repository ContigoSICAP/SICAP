package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

/**
 * Clase entidad de pago grupal
 *
 * @author JahTechnologies
 *
 */
public class PagoGrupalVO implements Serializable {

    /**
     * Numero de grupo
     */
    public int numGrupo;

    /**
     * Numero de ciclo
     */
    public int numCiclo;

    /**
     * Numero de transaccion
     */
    public int numTransaccion;

    public int numAmortizacion;

    /**
     * Numero de pago
     */
    public int numPago;

    /**
     * Fecha de pago
     */
    public Date fechaPago;

    /**
     * Monto
     */
    public double monto;

    public double solidario;

    public double ahorro;

    public double multa;

    public PagoGrupalVO() {
        numGrupo = 0;
        numCiclo = 0;
        numTransaccion = 0;
        numAmortizacion = 0;
        numPago = 0;
        fechaPago = null;
        monto = 0;
        solidario = 0;
        ahorro = 0;
        multa = 0;
    }

    public String toString() {
        StringBuffer respuesta = new StringBuffer();
        respuesta.append("numGrupo=[" + numGrupo + "],");
        respuesta.append("numCiclo=[" + numCiclo + "],");
        respuesta.append("numTransaccion=[" + numTransaccion + "],");
        respuesta.append("numAmortizacion=[" + numAmortizacion + "],");
        respuesta.append("numPago=[" + numPago + "],");
        respuesta.append("fechaPago=[" + fechaPago + "],");
        respuesta.append("monto=[" + monto + "],");
        respuesta.append("solidario=[" + solidario + "],");
        respuesta.append("ahorro=[" + ahorro + "],");
        respuesta.append("multa=[" + multa + "],");
        return respuesta.toString();
    }

    public PagoGrupalVO(int numGrupo, int numCiclo, int numTransaccion, int numAmortizacion, int numPago, Date fechaPago, double monto, double solidario, double ahorro, double multa) {
        this.numGrupo = numGrupo;
        this.numCiclo = numCiclo;
        this.numTransaccion = numTransaccion;
        this.numAmortizacion = numAmortizacion;
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.solidario = solidario;
        this.ahorro = ahorro;
        this.multa = multa;
    }

    public int getNumGrupo() {
        return numGrupo;
    }

    public void setNumGrupo(int numGrupo) {
        this.numGrupo = numGrupo;
    }

    public int getNumCiclo() {
        return numCiclo;
    }

    public void setNumCiclo(int numCiclo) {
        this.numCiclo = numCiclo;
    }

    public int getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public int getNumAmortizacion() {
        return numAmortizacion;
    }

    public void setNumAmortizacion(int numAmortizacion) {
        this.numAmortizacion = numAmortizacion;
    }

    public int getNumPago() {
        return numPago;
    }

    public void setNumPago(int numPago) {
        this.numPago = numPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getSolidario() {
        return solidario;
    }

    public void setSolidario(double solidario) {
        this.solidario = solidario;
    }

    public double getAhorro() {
        return ahorro;
    }

    public void setAhorro(double ahorro) {
        this.ahorro = ahorro;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

}

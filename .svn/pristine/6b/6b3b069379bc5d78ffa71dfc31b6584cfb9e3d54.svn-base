package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class DecisionComiteVO implements Serializable {

    public int idCliente;
    public int idSolicitud;
    public Date fechaRealizacion;
    public Timestamp fechaCaptura;
    public int decisionComite;
    public int causaRechazo;
    public int motivoRechazoCliente;
    public String detalleMotivoRechazoCliente;
    public double montoSinComision;
    public double montoAutorizado;
    public double montoRefinanciado;
    /**
     * Monto con seguro financiado.
     */
    public double montoConSeguro;
    public int plazoAutorizado;
    public int comision;
    public int tasa;
    public int frecuenciaPago;
    public int motivoCondicionamiento;
    public String comentariosComite;
    public double multa;
    public Date fechaValor;

    public DecisionComiteVO() {

        idCliente = 0;
        idSolicitud = 0;
        fechaRealizacion = null;
        fechaCaptura = null;
        decisionComite = 0;
        causaRechazo = 0;
        motivoRechazoCliente = 0;
        detalleMotivoRechazoCliente = null;
        montoSinComision = 0;
        montoAutorizado = 0;
        plazoAutorizado = 0;
        comision = 0;
        montoRefinanciado = 0;
        montoConSeguro = 0;
        tasa = 0;
        frecuenciaPago = 0;
        motivoCondicionamiento = 0;
        comentariosComite = null;
        multa = 0;
        fechaValor = null;

    }

    public String toString() {

        String respuesta = "idCliente=[" + idCliente + "],";
        respuesta += "idSolicitud=[" + idSolicitud + "],";
        respuesta += "fechaRealizacion=[" + fechaRealizacion + "],";
        respuesta += "fechaCaptura=[" + fechaCaptura + "],";
        respuesta += "decisionComite=[" + decisionComite + "],";
        respuesta += "causaRechazo=[" + causaRechazo + "],";
        respuesta += "motivoRechazoCliente=[" + motivoRechazoCliente + "],";
        respuesta += "detalleMotivoRechazoCliente=[" + detalleMotivoRechazoCliente + "],";
        respuesta += "montoSinComision=[" + montoSinComision + "],";
        respuesta += "montoAutorizado=[" + montoAutorizado + "],";
        respuesta += "montoRefinanciado=[" + montoRefinanciado + "],";
        respuesta += "montoConSeguro=[" + montoConSeguro + "],";
        respuesta += "plazoAutorizado=[" + plazoAutorizado + "],";
        respuesta += "comision=[" + comision + "],";
        respuesta += "tasa=[" + tasa + "],";
        respuesta += "frecuenciaPago=[" + frecuenciaPago + "],";
        respuesta += "motivoCondicionamiento=[" + motivoCondicionamiento + "],";
        respuesta += "comentariosComite=[" + comentariosComite + "],";
        respuesta += "multa=[" + multa + "],";
        respuesta += "fechaValor=[" + fechaValor + "]";

        return respuesta;
    }

    public DecisionComiteVO(int plazoAutorizado, int tasa, int frecuenciaPago) {
        this.plazoAutorizado = plazoAutorizado;
        this.tasa = tasa;
        this.frecuenciaPago = frecuenciaPago;
    }

    public int getCausaRechazo() {
        return causaRechazo;
    }

    public void setCausaRechazo(int causaRechazo) {
        this.causaRechazo = causaRechazo;
    }

    public String getComentariosComite() {
        return comentariosComite;
    }

    public void setComentariosComite(String comentariosComite) {
        this.comentariosComite = comentariosComite;
    }

    public int getComision() {
        return comision;
    }

    public void setComision(int comision) {
        this.comision = comision;
    }

    public int getDecisionComite() {
        return decisionComite;
    }

    public void setDecisionComite(int decisionComite) {
        this.decisionComite = decisionComite;
    }

    public String getDetalleMotivoRechazoCliente() {
        return detalleMotivoRechazoCliente;
    }

    public void setDetalleMotivoRechazoCliente(String detalleMotivoRechazoCliente) {
        this.detalleMotivoRechazoCliente = detalleMotivoRechazoCliente;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public int getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(int frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public double getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(double montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public double getMontoRefinanciado() {
        return montoRefinanciado;
    }

    public void setMontoRefinanciado(double montoRefinanciado) {
        this.montoRefinanciado = montoRefinanciado;
    }

    public double getMontoSinComision() {
        return montoSinComision;
    }

    public void setMontoSinComision(double montoSinComision) {
        this.montoSinComision = montoSinComision;
    }

    public int getMotivoCondicionamiento() {
        return motivoCondicionamiento;
    }

    public void setMotivoCondicionamiento(int motivoCondicionamiento) {
        this.motivoCondicionamiento = motivoCondicionamiento;
    }

    public int getMotivoRechazoCliente() {
        return motivoRechazoCliente;
    }

    public void setMotivoRechazoCliente(int motivoRechazoCliente) {
        this.motivoRechazoCliente = motivoRechazoCliente;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public int getPlazoAutorizado() {
        return plazoAutorizado;
    }

    public void setPlazoAutorizado(int plazoAutorizado) {
        this.plazoAutorizado = plazoAutorizado;
    }

    public int getTasa() {
        return tasa;
    }

    public void setTasa(int tasa) {
        this.tasa = tasa;
    }

    public double getMontoConSeguro() {
        return montoConSeguro;
    }

    public void setMontoConSeguro(double montoConSeguro) {
        this.montoConSeguro = montoConSeguro;
    }
}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class PagoVO implements Serializable {

    public int idIncidencia;
    public int numCliente;
    public String referencia;
    public String nuevareferencia;
    public String nombre;
    public double monto;
    public Date fechaPago;
    public String tipo;
    public Timestamp fechaHora;
    public int enviado;
    public double montoAbono;
    public int status;
    public int bancoReferencia;
    public int reprocesar;
    public String sucursal;
    public String observaciones;
    public String comentarios;
    public String usuarioReproceso;
    public int idContable;
    public int idContablePolizaSalidaIncidencia;
    public int destino;
    public boolean isCreditoIBS;
    public int numRepetido;
    public int numRegistro;
    public int numCuenta;
    public int numTransaccion;

    public PagoVO() {
        idIncidencia = 0;
        numCliente = 0;
        referencia = null;
        nuevareferencia = null;
        monto = 0.00;
        fechaPago = null;
        tipo = null;
        fechaHora = null;
        enviado = 0;
        status = 0;
        bancoReferencia = 0;
        reprocesar = 0;
        destino = 0;
        sucursal = null;
        observaciones = null;
        nombre = null;
        comentarios = null;
        usuarioReproceso = null;
        idContable = 0;
        idContablePolizaSalidaIncidencia = 0;
        isCreditoIBS = false;
        numRepetido = 0;
        numTransaccion = 0;
    }
    
    public PagoVO(String referencia, double monto, Date fechaPago, int numRegistro) {
        this.referencia = referencia;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.numRegistro = numRegistro;
    }

    public PagoVO(double monto, Date fechaPago, int enviado, int bancoReferencia) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.enviado = enviado;
        this.bancoReferencia = bancoReferencia;
    }
    
    public PagoVO(double monto, Date fechaPago, int enviado, int bancoReferencia, int numCuenta) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.enviado = enviado;
        this.bancoReferencia = bancoReferencia;
        this.numCuenta = numCuenta;
    }

    public PagoVO(double monto, Date fechaPago, int status) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.status = status;
    }

    public PagoVO(String referencia, double monto, Date fechaPago, Timestamp fechaHora, int enviado, int status, int bancoReferencia, String sucursal, int numCuenta) {
        this.referencia = referencia;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.fechaHora = fechaHora;
        this.enviado = enviado;
        this.status = status;
        this.bancoReferencia = bancoReferencia;
        this.sucursal = sucursal;
        this.numCuenta = numCuenta;
    }

    public PagoVO(double monto, int bancoReferencia) {
        this.monto = monto;
        this.bancoReferencia = bancoReferencia;
    }

    public PagoVO(String referencia, double monto, int bancoReferencia, Date fechaPago, int numRegistro, int numCuenta) {
        this.referencia = referencia;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.bancoReferencia = bancoReferencia;
        this.numRegistro = numRegistro;
        this.numCuenta = numCuenta;
    }

    public PagoVO(String referencia, double monto, Date fechaPago, int enviado, int bancoReferencia, int numCuenta) {
        this.referencia = referencia;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.enviado = enviado;
        this.bancoReferencia = bancoReferencia;
        this.numCuenta = numCuenta;
    }

    public PagoVO(String referencia, double monto, Date fechaPago, int enviado, int bancoReferencia, String tipo, int idContable, int numCuenta) {
        this.referencia = referencia;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.enviado = enviado;
        this.bancoReferencia = bancoReferencia;
        this.tipo = tipo;
        this.idContable = idContable;
        this.numCuenta = numCuenta;
    }

    public PagoVO(String referencia) {
        this.referencia = referencia;
    }

    public String toString() {
        String respuesta = null;

        respuesta = "idIncidencia=[" + idIncidencia + "],";
        respuesta += "referencia=[" + referencia + "],";
        respuesta += "nuevareferencia=[" + nuevareferencia + "],";
        respuesta += "monto=[" + monto + "],";
        respuesta += "fechaPago=[" + fechaPago + "],";
        respuesta += "tipo=[" + tipo + "],";
        respuesta += "fechaHora=[" + fechaHora + "],";
        respuesta += "enviado=[" + enviado + "]";
        respuesta += "status=[" + status + "]";
        respuesta += "bancoReferencia=[" + bancoReferencia + "]";
        respuesta += "sucursal=[" + sucursal + "]";
        respuesta += "reprocesar=[" + reprocesar + "]";
        respuesta += "producto=[" + destino + "]";
        respuesta += "usuarioReproceso=[" + usuarioReproceso + "]";
        respuesta += "idContable=[" + idContable + "]";
        respuesta += "idContablePolizaSalidaIncidencia=[" + idContablePolizaSalidaIncidencia + "]";
        respuesta += "numRepetido=[" + numRepetido + "]";

        return respuesta;
    }

    public int getBancoReferencia() {
        return bancoReferencia;
    }

    public void setBancoReferencia(int bancoReferencia) {
        this.bancoReferencia = bancoReferencia;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdContable() {
        return idContable;
    }

    public void setIdContable(int idContable) {
        this.idContable = idContable;
    }

    public int getIdContablePolizaSalidaIncidencia() {
        return idContablePolizaSalidaIncidencia;
    }

    public void setIdContablePolizaSalidaIncidencia(int idContablePolizaSalidaIncidencia) {
        this.idContablePolizaSalidaIncidencia = idContablePolizaSalidaIncidencia;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public boolean isIsCreditoIBS() {
        return isCreditoIBS;
    }

    public void setIsCreditoIBS(boolean isCreditoIBS) {
        this.isCreditoIBS = isCreditoIBS;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMontoAbono() {
        return montoAbono;
    }

    public void setMontoAbono(double montoAbono) {
        this.montoAbono = montoAbono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNuevareferencia() {
        return nuevareferencia;
    }

    public void setNuevareferencia(String nuevareferencia) {
        this.nuevareferencia = nuevareferencia;
    }

    public int getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(int numCliente) {
        this.numCliente = numCliente;
    }

    public int getNumRepetido() {
        return numRepetido;
    }

    public void setNumRepetido(int numRepetido) {
        this.numRepetido = numRepetido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getReprocesar() {
        return reprocesar;
    }

    public void setReprocesar(int reprocesar) {
        this.reprocesar = reprocesar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioReproceso() {
        return usuarioReproceso;
    }

    public void setUsuarioReproceso(String usuarioReproceso) {
        this.usuarioReproceso = usuarioReproceso;
    }

    public int getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(int numRegistro) {
        this.numRegistro = numRegistro;
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }
    public int getNumTransaccion() {
        return numTransaccion;
    }
    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }
    
    
}

package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class CreditoCartVO implements Serializable {

    private int numCliente;
    private int numSolicitud;
    private int numCredito;
    private String referencia;
    private String numDocumento;
    private int numCuenta;
    private int numProducto;
    private int numSucursal;
    private int numEmpresa;
    private Date fechaDesembolso;
    private Date fechaVencimiento;
    private Date fechaLiquidacion;
    private int numCuotas;
    private int periodicidad;
    private double monto_credito;
    private double monto_desembolsado;
    private double monto_comision;
    private double monto_iva_comision;
    private double monto_seguro;
    private double monto_iva_seguro;
    private double monto_otros_cargos;
    private double monto_iva_otros_cargos;
    private double monto_cuenta;
    // Se Agrega campo para la insercion del monto de cuenta Congelada
    private double monto_cuenta_congelada;
    private double tasa_int;
    private double tasa_int_siva;
    private double tasa_comision;
    private double tasa_mora;
    private double tasa_iva;
    private int dias;
    private int dias_mora;
    private int fondeador;
    private int numEjecutivo;
    private int status;
    private Date fechaUltimaActualizacion;
    private Date fechaUltimoPago;
    private double valor_cuota;
    private int banco_desembolso;
    private boolean aplicaGarantia;
    private double aplicaMontoGarantia;

    public CreditoCartVO() {
        numCliente = 0;
        numSolicitud = 0;
        numCredito = 0;
        referencia = null;
        numDocumento = null;
        numCuenta = 0;
        numProducto = 0;
        numSucursal = 0;
        numEmpresa = 0;
        fechaDesembolso = null;
        fechaVencimiento = null;
        fechaLiquidacion = null;
        numCuotas = 0;
        periodicidad = 0;
        monto_credito = 0;
        monto_desembolsado = 0;
        monto_cuenta = 0;
// Se inicializa variable nueva para la insercion del monto de cuenta Congelada
        monto_cuenta_congelada = 0;
        monto_comision = 0;
        monto_iva_comision = 0;
        monto_seguro = 0;
        monto_iva_seguro = 0;
        monto_otros_cargos = 0;
        monto_iva_otros_cargos = 0;
        tasa_int = 0;
        tasa_int_siva = 0;
        tasa_comision = 0;
        tasa_mora = 0;
        tasa_iva = 0;
        dias = 0;
        dias_mora = 0;
        status = 0;
        fondeador = 0;
        numEjecutivo = 0;
        fechaUltimaActualizacion = null;
        fechaUltimoPago = null;
        valor_cuota = 0;
        banco_desembolso = 0;
        aplicaGarantia = false;
        aplicaMontoGarantia = 0;
    }

    public int getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(int numCliente) {
        this.numCliente = numCliente;
    }

    public int getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(int numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public int getNumCredito() {
        return numCredito;
    }

    public void setNumCredito(int numCredito) {
        this.numCredito = numCredito;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }

    public int getNumProducto() {
        return numProducto;
    }

    public void setNumProducto(int numProducto) {
        this.numProducto = numProducto;
    }

    public int getNumSucursal() {
        return numSucursal;
    }

    public void setNumSucursal(int numSucursal) {
        this.numSucursal = numSucursal;
    }

    public int getNumEmpresa() {
        return numEmpresa;
    }

    public void setNumEmpresa(int numEmpresa) {
        this.numEmpresa = numEmpresa;
    }

    public Date getFechaDesembolso() {
        return fechaDesembolso;
    }

    public void setFechaDesembolso(Date fechaDesembolso) {
        this.fechaDesembolso = fechaDesembolso;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(int numCuotas) {
        this.numCuotas = numCuotas;
    }

    public double getMontoCredito() {
        return monto_credito;
    }

    public void setMontoCredito(double monto_credito) {
        this.monto_credito = monto_credito;
    }

    public double getMontoDesembolsado() {
        return monto_desembolsado;
    }

    public void setMontoDesembolsado(double monto_desembolsado) {
        this.monto_desembolsado = monto_desembolsado;
    }

    public double getMontoCuenta() {
        return monto_cuenta;
    }

    public void setMontoCuenta(double monto_cuenta) {
        this.monto_cuenta = monto_cuenta;
    }

    public double getMontoComision() {
        return monto_comision;
    }

    public void setMontoComision(double monto_comision) {
        this.monto_comision = monto_comision;
    }

    public double getMontoIvaComision() {
        return monto_iva_comision;
    }

    public void setMontoIvaComision(double monto_iva_comision) {
        this.monto_iva_comision = monto_iva_comision;
    }

    public double getMontoSeguro() {
        return monto_seguro;
    }

    public void setMontoSeguro(double monto_seguro) {
        this.monto_seguro = monto_seguro;
    }

    public double getMontoIvaSeguro() {
        return monto_iva_seguro;
    }

    public void setMontoIvaSeguro(double monto_iva_seguro) {
        this.monto_iva_seguro = monto_iva_seguro;
    }

    public double getMontoOtrosCargos() {
        return monto_otros_cargos;
    }

    public void setMontoOtrosCargos(double monto_otros_cargos) {
        this.monto_otros_cargos = monto_otros_cargos;
    }

    public double getMontoIvaOtrosCargos() {
        return monto_iva_otros_cargos;
    }

    public void setMontoIvaOtrosCargos(double monto_iva_otros_cargos) {
        this.monto_iva_otros_cargos = monto_iva_otros_cargos;
    }

    public double getTasaInteres() {
        return tasa_int;
    }

    public void setTasaInteres(double tasa_int) {
        this.tasa_int = tasa_int;
    }

    public double getTasaInteresSIVA() {
        return tasa_int_siva;
    }

    public void setTasaInteresSIVA(double tasa_int_siva) {
        this.tasa_int_siva = tasa_int_siva;
    }

    public double getTasaComision() {
        return tasa_comision;
    }

    public void setTasaComision(double tasa_comision) {
        this.tasa_comision = tasa_comision;
    }

    public double getTasaMora() {
        return tasa_mora;
    }

    public void setTasaMora(double tasa_mora) {
        this.tasa_mora = tasa_mora;
    }

    public double getTasaIVA() {
        return tasa_iva;
    }

    public void setTasaIVA(double tasa_iva) {
        this.tasa_iva = tasa_iva;
    }

    public int getNumDias() {
        return dias;
    }

    public void setNumDias(int dias) {
        this.dias = dias;
    }

    public int getNumDiasMora() {
        return dias_mora;
    }

    public void setNumDiasMora(int dias_mora) {
        this.dias_mora = dias_mora;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFondeador() {
        return fondeador;
    }

    public void setFondeador(int fondeador) {
        this.fondeador = fondeador;
    }

    public int getNumEjecutivo() {
        return numEjecutivo;
    }

    public void setNumEjecutivo(int numEjecutivo) {
        this.numEjecutivo = numEjecutivo;
    }

    public Date getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(Date fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public Date getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public double getValorCuota() {
        return valor_cuota;
    }

    public void setValorCuota(double valor_cuota) {
        this.valor_cuota = valor_cuota;
    }

    public int getBancoDesembolso() {
        return banco_desembolso;
    }

    public void setBancoDesembolso(int banco_desembolso) {
        this.banco_desembolso = banco_desembolso;
    }

    public void setMontoCuentaCongelada(double monto_cuenta_congelada) {
        this.monto_cuenta_congelada = monto_cuenta_congelada;
    }

    public double getMontoCuentaCongelada() {
        return monto_cuenta_congelada;
    }

    public boolean getAplicaGarantia() {
        return aplicaGarantia;
    }

    public void setAplicaGarantia(boolean aplicaGarantia) {
        this.aplicaGarantia = aplicaGarantia;
    }

    public double getAplicaMontoGarantia() {
        return aplicaMontoGarantia;
    }

    public void setAplicaMontoGarantia(double aplicaMontoGarantia) {
        this.aplicaMontoGarantia = aplicaMontoGarantia;
    }

    /**
     * 01/10/2017
     * Método que realiza la representracion del objeto CreditoCartVO en un string
     * @return 
     */
    @Override
    public String toString() {
        return "CreditoCartVO{" + "numCliente=" + numCliente + ", numSolicitud=" + numSolicitud + ", numCredito=" + numCredito + ", referencia=" + referencia + ", numDocumento=" + numDocumento + ", numCuenta=" + numCuenta + ", numProducto=" + numProducto + ", numSucursal=" + numSucursal + ", numEmpresa=" + numEmpresa + ", fechaDesembolso=" + fechaDesembolso + ", fechaVencimiento=" + fechaVencimiento + ", fechaLiquidacion=" + fechaLiquidacion + ", numCuotas=" + numCuotas + ", periodicidad=" + periodicidad + ", monto_credito=" + monto_credito + ", monto_desembolsado=" + monto_desembolsado + ", monto_comision=" + monto_comision + ", monto_iva_comision=" + monto_iva_comision + ", monto_seguro=" + monto_seguro + ", monto_iva_seguro=" + monto_iva_seguro + ", monto_otros_cargos=" + monto_otros_cargos + ", monto_iva_otros_cargos=" + monto_iva_otros_cargos + ", monto_cuenta=" + monto_cuenta + ", monto_cuenta_congelada=" + monto_cuenta_congelada + ", tasa_int=" + tasa_int + ", tasa_int_siva=" + tasa_int_siva + ", tasa_comision=" + tasa_comision + ", tasa_mora=" + tasa_mora + ", tasa_iva=" + tasa_iva + ", dias=" + dias + ", dias_mora=" + dias_mora + ", fondeador=" + fondeador + ", numEjecutivo=" + numEjecutivo + ", status=" + status + ", fechaUltimaActualizacion=" + fechaUltimaActualizacion + ", fechaUltimoPago=" + fechaUltimoPago + ", valor_cuota=" + valor_cuota + ", banco_desembolso=" + banco_desembolso + ", aplicaGarantia=" + aplicaGarantia + ", aplicaMontoGarantia=" + aplicaMontoGarantia + '}';
    }

}

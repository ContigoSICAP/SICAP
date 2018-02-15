
package com.sicap.clientes.vo;

import java.util.Date;



public class LineaCreditoVO 
{
    private int idLineaCredito; 
    private int idFondeador; 
    private String nombreLineaCredito; 
    private int montoLineaCredito; 
    private Date fechaVigenciaInicio; 
    private Date fechaVigenciaFin; 
    private String tasa; 
    public int estatus;
    public int preSeleccionCartera;
    
    
    public LineaCreditoVO(int idFondeador, String nombre, int monto, Date fechaIni, Date fechaFin, String tasa, int preSeleccionCartera)
    {
        this.idFondeador=idFondeador;
        this.nombreLineaCredito=nombre;
        this.montoLineaCredito=monto;
        this.fechaVigenciaInicio=fechaIni;
        this.fechaVigenciaFin=fechaFin;
        this.tasa=tasa;
        this.preSeleccionCartera=preSeleccionCartera;
    }
    
    public LineaCreditoVO(int idFondeador, String nombre, int monto, Date fechaIni, Date fechaFin, String tasa, int preSeleccionCartera, int estatus)
    {
        this.idFondeador=idFondeador;
        this.nombreLineaCredito=nombre;
        this.montoLineaCredito=monto;
        this.fechaVigenciaInicio=fechaIni;
        this.fechaVigenciaFin=fechaFin;
        this.tasa=tasa;
        this.estatus=estatus;
        this.preSeleccionCartera=preSeleccionCartera;
    }
    
    public LineaCreditoVO()
    {
        
    }
    public LineaCreditoVO(int idLineaCredito, String nombre)
    {
        this.idLineaCredito=idLineaCredito;
        this.nombreLineaCredito=nombre;
    }
    public LineaCreditoVO(int idLineaCredito, Date fechaVigenciaFin)
    {
        this.idLineaCredito=idLineaCredito;
        this.fechaVigenciaFin=fechaVigenciaFin;
    }

    public LineaCreditoVO(int idLineaCredito) {
        this.idLineaCredito = idLineaCredito;
    }
    
    public LineaCreditoVO(int idLineaCredito, Date fechaVigenciaFin, int idFondeador)
    {
        this.idLineaCredito=idLineaCredito;
        this.fechaVigenciaFin=fechaVigenciaFin;
        this.idFondeador=idFondeador;
    }
    
    public LineaCreditoVO(Date fechaVigenciaFin, int monto)
    {
        this.fechaVigenciaFin=fechaVigenciaFin;
        this.montoLineaCredito=monto;
    }
    

    public int getIdLineaCredito() {
        return idLineaCredito;
    }

    public void setIdLineaCredito(int idLineaCredito) {
        this.idLineaCredito = idLineaCredito;
    }

    public int getIdFondeador() {
        return idFondeador;
    }

    public void setIdFondeador(int idFondeador) {
        this.idFondeador = idFondeador;
    }

    public String getNombreLineaCredito() {
        return nombreLineaCredito;
    }

    public void setNombreLineaCredito(String nombreLineaCredito) {
        this.nombreLineaCredito = nombreLineaCredito;
    }

    public int getMontoLineaCredito() {
        return montoLineaCredito;
    }

    public void setMontoLineaCredito(int montoLineaCredito) {
        this.montoLineaCredito = montoLineaCredito;
    }

    public Date getFechaVigenciaInicio() {
        return fechaVigenciaInicio;
    }

    public void setFechaVigenciaInicio(Date fechaVigenciaInicio) {
        this.fechaVigenciaInicio = fechaVigenciaInicio;
    }

    public Date getFechaVigenciaFin() {
        return fechaVigenciaFin;
    }

    public void setFechaVigenciaFin(Date fechaVigenciaFin) {
        this.fechaVigenciaFin = fechaVigenciaFin;
    }

    public String getTasa() {
        return tasa;
    }

    public void setTasa(String tasa) {
        this.tasa = tasa;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getPreSeleccionCartera() {
        return preSeleccionCartera;
    }

    public void setPreSeleccionCartera(int preSeleccionCartera) {
        this.preSeleccionCartera = preSeleccionCartera;
    }
}
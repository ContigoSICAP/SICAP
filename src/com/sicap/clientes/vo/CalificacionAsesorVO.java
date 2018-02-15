package com.sicap.clientes.vo;

import java.io.Serializable;

public class CalificacionAsesorVO implements Serializable {
    
    private int idAsesor;
    private int idCredito;
    private int idSucursal;
    private int idGupo;
    private int idCiclo;
    private double mora;

    public CalificacionAsesorVO(int idCredito, int idGupo) {
        this.idCredito = idCredito;
        this.idGupo = idGupo;
    }

    public CalificacionAsesorVO(int idAsesor, int idCredito, int idSucursal, double mora) {
        this.idAsesor = idAsesor;
        this.idCredito = idCredito;
        this.idSucursal = idSucursal;
        this.mora = mora;
    }

    public CalificacionAsesorVO(int idAsesor, int idCredito, int idSucursal, int idGupo, int idCiclo) {
        this.idAsesor = idAsesor;
        this.idCredito = idCredito;
        this.idSucursal = idSucursal;
        this.idGupo = idGupo;
        this.idCiclo = idCiclo;
    }

    public int getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(int idAsesor) {
        this.idAsesor = idAsesor;
    }

    public int getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(int idCredito) {
        this.idCredito = idCredito;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdGupo() {
        return idGupo;
    }

    public void setIdGupo(int idGupo) {
        this.idGupo = idGupo;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }
    
}

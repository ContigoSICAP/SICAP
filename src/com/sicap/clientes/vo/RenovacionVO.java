package com.sicap.clientes.vo;

import java.io.Serializable;
import java.util.Date;

public class RenovacionVO implements Serializable{
    
    private int idEquipo;
    private int idCiclo;
    private int idAsesor;
    private int numIntegrantes;
    private Date fechaVencimiento;
    private int idMotivo;

    public RenovacionVO() {
        idEquipo = 0;
        idCiclo = 0;
        idAsesor = 0;
        numIntegrantes = 0;
        fechaVencimiento = null;
        idMotivo = 0 ;
    }

    public RenovacionVO(int idEquipo, int idCiclo, int idAsesor, int numIntegrantes, Date fechaVencimiento, int idMotivo) {
        this.idEquipo = idEquipo;
        this.idCiclo = idCiclo;
        this.idAsesor = idAsesor;
        this.numIntegrantes = numIntegrantes;
        this.fechaVencimiento = fechaVencimiento;
        this.idMotivo = idMotivo;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(int idAsesor) {
        this.idAsesor = idAsesor;
    }

    public int getNumIntegrantes() {
        return numIntegrantes;
    }

    public void setNumIntegrantes(int numIntegrantes) {
        this.numIntegrantes = numIntegrantes;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }
    
    
    public String toString() {
        String respuesta = null;
        respuesta = "RenovacionVO{" + "idEquipo=[" + idEquipo + "], idCiclo=[" + idCiclo + "], idAsesor=[" +idAsesor + "], numIntegrantes=[" + numIntegrantes +
                "], fechaVencimiento=[" + fechaVencimiento + "], idMotivo=[" + idMotivo + "]"+ '}';
        return respuesta;
    }
}

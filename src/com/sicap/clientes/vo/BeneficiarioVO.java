package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BeneficiarioVO implements Serializable {

    public int idCliente;
    public int idSolicitud;
    public int numSeguro;
    public int numBeneficiario;
    public String nombre;
    public String aPaterno;
    public String aMaterno;
    public int relacion;
    public String otraRelacion;
    public int porcentaje;
    public String fechaNacimiento;

    public BeneficiarioVO() {
    }

    public BeneficiarioVO(String nombre, String aPaterno, String aMaterno, int relacion, String otraRelacion, int porcentaje, String fechaNacimiento) {
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.relacion = relacion;
        this.otraRelacion = otraRelacion;
        this.porcentaje = porcentaje;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumBeneficiario() {
        return numBeneficiario;
    }

    public void setNumBeneficiario(int numBeneficiario) {
        this.numBeneficiario = numBeneficiario;
    }

    public int getNumSeguro() {
        return numSeguro;
    }

    public void setNumSeguro(int numSeguro) {
        this.numSeguro = numSeguro;
    }

    public String getOtraRelacion() {
        return otraRelacion;
    }

    public void setOtraRelacion(String otraRelacion) {
        this.otraRelacion = otraRelacion;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getRelacion() {
        return relacion;
    }

    public void setRelacion(int relacion) {
        this.relacion = relacion;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public class AseguradosVO implements Serializable{
    
    public String nombre;
    public String apPaterno;
    public String apMaterno;
    public String fecNacimiento;
    public int parentesco;
    public int numAsegurado;

    public AseguradosVO() {
    }

    public AseguradosVO(String nombre, String apPaterno, String apMaterno, String fecNacimiento, int parentesco) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.fecNacimiento = fecNacimiento;
        this.parentesco = parentesco;
    }
    
    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumAsegurado() {
        return numAsegurado;
    }

    public void setNumAsegurado(int numAsegurado) {
        this.numAsegurado = numAsegurado;
    }

    public int getParentesco() {
        return parentesco;
    }

    public void setParentesco(int parentesco) {
        this.parentesco = parentesco;
    }
    
}

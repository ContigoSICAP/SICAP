package com.sicap.clientes.vo;

import java.io.Serializable;
import java.util.List;

public class ReporteCirculoVO implements Serializable {

    PersonaVO personas = null;
    EncabezadoCirculoVO encabezado = null;
    List domicilios = null;
    List empleos = null;
    List cuentas = null;
    List consultas = null;
    List scoreFico = null;

    public List getCuentas() {
        return cuentas;
    }

    public void setCuentas(List cuentas) {
        this.cuentas = cuentas;
    }

    public List getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(List domicilios) {
        this.domicilios = domicilios;
    }

    public List getEmpleos() {
        return empleos;
    }

    public void setEmpleos(List empleos) {
        this.empleos = empleos;
    }

    public PersonaVO getPersonas() {
        return personas;
    }

    public void setPersonas(PersonaVO personas) {
        this.personas = personas;
    }

    public List getConsultas() {
        return consultas;
    }

    public void setConsultas(List consultas) {
        this.consultas = consultas;
    }

    public EncabezadoCirculoVO getEncabezadoCirculo() {
        return encabezado;
    }

    public void setEncabezadoCirculo(EncabezadoCirculoVO encabezado) {
        this.encabezado = encabezado;
    }

    public List getScoreFico() {
        return scoreFico;
    }

    public void setScoreFico(List scoreFico) {
        this.scoreFico = scoreFico;
    }
}

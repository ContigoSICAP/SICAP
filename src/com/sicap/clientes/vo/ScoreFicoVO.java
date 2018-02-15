package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class ScoreFicoVO implements Serializable {
    
    private int idScore;
    private String nombre;
    private int codigo;
    private int valor;
    private String razon1;
    private String razon2;
    private String razon3;
    private String razon4;
    private int idCliente;
    private int idSolicitud;
    private Date fechaCaptura;

    public ScoreFicoVO() {
    }

    public ScoreFicoVO(String nombre, int codigo, int valor, String razon1, String razon2, String razon3, String razon4, int idCliente, int idSolicitud, Date fechaCaptura) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.valor = valor;
        this.razon1 = razon1;
        this.razon2 = razon2;
        this.razon3 = razon3;
        this.razon4 = razon4;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.fechaCaptura = fechaCaptura;
    }

    public int getIdScore() {
        return idScore;
    }

    public void setIdScore(int idScore) {
        this.idScore = idScore;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getRazon1() {
        return razon1;
    }

    public void setRazon1(String razon1) {
        this.razon1 = razon1;
    }

    public String getRazon2() {
        return razon2;
    }

    public void setRazon2(String razon2) {
        this.razon2 = razon2;
    }

    public String getRazon3() {
        return razon3;
    }

    public void setRazon3(String razon3) {
        this.razon3 = razon3;
    }

    public String getRazon4() {
        return razon4;
    }

    public void setRazon4(String razon4) {
        this.razon4 = razon4;
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

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

}

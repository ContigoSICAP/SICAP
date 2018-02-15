package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;
public class EjecutivoCreditoVO implements Serializable {

    public int idEjecutivo;
    public int idSucursal;
    public String nombre;
    public String aPaterno;
    public String aMaterno;
    public String estatus;
    public String usuario;
    public Timestamp fechaHoramodificacion;
    public int tipoEjecutivo;
    public int upline;

    public EjecutivoCreditoVO() {
        idEjecutivo = 0;
        idSucursal = 0;
        nombre = null;
        aPaterno = null;
        aMaterno = null;
        estatus = null;
        usuario = null;
        fechaHoramodificacion = null;
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idEjecutivo=[" + idEjecutivo + "],";
        respuesta += "idSucursal=[" + idSucursal + "],";
        respuesta += "nombre=[" + nombre + "],";
        respuesta += "aPaterno=[" + aPaterno + "],";
        respuesta += "aMaterno=[" + aMaterno + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "usuario=[" + usuario + "],";
        respuesta += "fechaHoramodificacion=[" + fechaHoramodificacion + "],";

        return respuesta;
    }

    public EjecutivoCreditoVO(int idEjecutivo, int tipoEjecutivo) {
        this.idEjecutivo = idEjecutivo;
        this.tipoEjecutivo = tipoEjecutivo;
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Timestamp getFechaHoramodificacion() {
        return fechaHoramodificacion;
    }

    public void setFechaHoramodificacion(Timestamp fechaHoramodificacion) {
        this.fechaHoramodificacion = fechaHoramodificacion;
    }

    public int getIdEjecutivo() {
        return idEjecutivo;
    }

    public void setIdEjecutivo(int idEjecutivo) {
        this.idEjecutivo = idEjecutivo;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipoEjecutivo() {
        return tipoEjecutivo;
    }

    public void setTipoEjecutivo(int tipoEjecutivo) {
        this.tipoEjecutivo = tipoEjecutivo;
    }

    public int getUpline() {
        return upline;
    }

    public void setUpline(int upline) {
        this.upline = upline;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

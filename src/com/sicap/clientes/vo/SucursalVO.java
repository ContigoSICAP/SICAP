package com.sicap.clientes.vo;

import java.io.Serializable;

public class SucursalVO implements Serializable {

    public int idSucursal;
    public String nombre;
    public int idRegion;
    public int idPlaza;
    public boolean fronterizo;
    public String identificador;
    public int idBanco;
    public int tipoSucursal;
    public String direccion_calle;
    public String representante;
    public String numero;
    public int cp;
    public int idMunicipio;
    public String municipio;
    public int idEstado;
    public String estado;
    public int idColonia;
    public String colonia;
    public String telefono;
    public int estatus;
    public int codigo;
    public String nombreRegion;
    public int idSubdireccion;
    public String nombreSubdireccion;
    public String codigoSAP;
    public int calificacion;
    public double SeguroFunerario;

    public SucursalVO() {

        idSucursal = 0;
        nombre = null;
        idRegion = 0;
        idPlaza = 0;
        fronterizo = false;
        identificador = null;
        idBanco = 0;
        tipoSucursal = 0;
        direccion_calle = null;
        representante = null;
        numero = null;
        cp = 0;
        idMunicipio = 0;
        municipio = null;
        idEstado = 0;
        estado = null;
        idColonia = 0;
        colonia = null;
        telefono = null;
        estatus = 0;
        codigo = 0;
        calificacion =0;
        SeguroFunerario =0;

    }

    public String toString() {
        String respuesta = null;
        respuesta = "idSucursal=[" + idSucursal + "],";
        respuesta += "nombre=[" + nombre + "],";
        respuesta += "idRegion=[" + idRegion + "],";
        respuesta += "idPlaza=[" + idPlaza + "],";
        respuesta += "fronterizo=[" + fronterizo + "],";
        respuesta += "identificador=[" + identificador + "],";
        respuesta += "idBanco=[" + idBanco + "],";
        respuesta += "tipoSucursal=[" + tipoSucursal + "],";
        respuesta += "direccion_calle=[" + direccion_calle + "],";
        respuesta += "representante=[" + representante + "],";
        respuesta += "numero=[" + numero + "],";
        respuesta += "cp=[" + cp + "],";
        respuesta += "idMunicipio=[" + idMunicipio + "],";
        respuesta += "municipio=[" + municipio + "],";
        respuesta += "idEstado=[" + idEstado + "],";
        respuesta += "estado=[" + estado + "],";
        respuesta += "idColonia=[" + idColonia + "],";
        respuesta += "colonia=[" + colonia + "],";
        respuesta += "telefono=[" + telefono + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "codigo=[" + codigo + "],";
        respuesta += "codigoSAP=[" + codigoSAP + "],";
        respuesta += " calificacion=[" +  calificacion + "]";
        return respuesta;
    }

    public SucursalVO(int idSucursal, String nombre, int idRegion, String nombreRegion, int idSubdireccion, String nombreSubdireccion) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.idRegion = idRegion;
        this.nombreRegion = nombreRegion;
        this.idSubdireccion = idSubdireccion;
        this.nombreSubdireccion = nombreSubdireccion;
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

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public int getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(int idPlaza) {
        this.idPlaza = idPlaza;
    }

    public boolean isFronterizo() {
        return fronterizo;
    }

    public void setFronterizo(boolean fronterizo) {
        this.fronterizo = fronterizo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public int getTipoSucursal() {
        return tipoSucursal;
    }

    public void setTipoSucursal(int tipoSucursal) {
        this.tipoSucursal = tipoSucursal;
    }

    public String getDireccion_calle() {
        return direccion_calle;
    }

    public void setDireccion_calle(String direccion_calle) {
        this.direccion_calle = direccion_calle;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public int getIdSubdireccion() {
        return idSubdireccion;
    }

    public void setIdSubdireccion(int idSubdireccion) {
        this.idSubdireccion = idSubdireccion;
    }

    public String getNombreSubdireccion() {
        return nombreSubdireccion;
    }

    public void setNombreSubdireccion(String nombreSubdireccion) {
        this.nombreSubdireccion = nombreSubdireccion;
    }

    public String getCodigoSAP() {
        return codigoSAP;
    }

    public void setCodigoSAP(String codigoSAP) {
        this.codigoSAP = codigoSAP;
    }   

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

}

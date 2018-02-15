/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class DespachosVO implements Serializable{
    
    private int idDespacho;
    private String nomDespacho;
    private String calle;
    private int idDireccion;
    private String contacto;
    private String contactoAlt;

    public DespachosVO(int idDespacho, String nomDespacho) {
        this.idDespacho = idDespacho;
        this.nomDespacho = nomDespacho;
    }
    
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getContactoAlt() {
        return contactoAlt;
    }

    public void setContactoAlt(String contactoAlt) {
        this.contactoAlt = contactoAlt;
    }

    public int getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(int idDespacho) {
        this.idDespacho = idDespacho;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getNomDespacho() {
        return nomDespacho;
    }

    public void setNomDespacho(String nomDespacho) {
        this.nomDespacho = nomDespacho;
    }
    
}

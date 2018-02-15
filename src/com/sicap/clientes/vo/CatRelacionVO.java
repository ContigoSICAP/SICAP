/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo;

/**
 *
 * @author Alex
 */
public class CatRelacionVO {
    
    int numRelacion;
    String nomRelacion;

    public CatRelacionVO() {
    }

    public CatRelacionVO(int numRelacion, String nomRelacion) {
        this.numRelacion = numRelacion;
        this.nomRelacion = nomRelacion;
    }

    public String getNomRelacion() {
        return nomRelacion;
    }

    public void setNomRelacion(String nomRelacion) {
        this.nomRelacion = nomRelacion;
    }

    public int getNumRelacion() {
        return numRelacion;
    }

    public void setNumRelacion(int numRelacion) {
        this.numRelacion = numRelacion;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo;

/**
 *
 * @author Alex
 */
public class TipoSeguroVO {
    
    int idTipoSeguro;
    String nombreSeguro;

    public TipoSeguroVO() {
    }

    public TipoSeguroVO(int idTipoSeguro, String nombreSeguro) {
        this.idTipoSeguro = idTipoSeguro;
        this.nombreSeguro = nombreSeguro;
    }

    public int getIdTipoSeguro() {
        return idTipoSeguro;
    }

    public void setIdTipoSeguro(int idTipoSeguro) {
        this.idTipoSeguro = idTipoSeguro;
    }

    public String getNombreSeguro() {
        return nombreSeguro;
    }

    public void setNombreSeguro(String nombreSeguro) {
        this.nombreSeguro = nombreSeguro;
    }
    
}

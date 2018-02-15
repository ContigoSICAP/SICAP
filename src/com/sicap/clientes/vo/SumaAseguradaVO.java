/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo;

/**
 *
 * @author Alex
 */
public class SumaAseguradaVO {
    int idSuma;
    String suma;

    public SumaAseguradaVO() {
    }

    public SumaAseguradaVO(int idSuma, String suma) {
        this.idSuma = idSuma;
        this.suma = suma;
    }
    
    public int getIdSuma() {
        return idSuma;
    }

    public void setIdSuma(int idSuma) {
        this.idSuma = idSuma;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }
    
}

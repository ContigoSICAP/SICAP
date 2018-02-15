/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class NuevaAmortizacionVO implements Serializable{
    
    public double capitalPagado;
    public double interesPagado;
    public double ivaInteresPagado;
    public double multaPagado;
    public double ivaMultaPagado;

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public double getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(double interesPagado) {
        this.interesPagado = interesPagado;
    }

    public double getIvaInteresPagado() {
        return ivaInteresPagado;
    }

    public void setIvaInteresPagado(double ivaInteresPagado) {
        this.ivaInteresPagado = ivaInteresPagado;
    }

    public double getIvaMultaPagado() {
        return ivaMultaPagado;
    }

    public void setIvaMultaPagado(double ivaMultaPagado) {
        this.ivaMultaPagado = ivaMultaPagado;
    }

    public double getMultaPagado() {
        return multaPagado;
    }

    public void setMultaPagado(double multaPagado) {
        this.multaPagado = multaPagado;
    }
    
}

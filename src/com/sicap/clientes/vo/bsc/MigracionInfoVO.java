/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.vo.bsc;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author Alex
 */
public class MigracionInfoVO implements Serializable{
    
    private int periodo;
    private Date fecInicial;
    private Date fecFinal;
    private int numSucursal;
    private double capital;
    private double capitalPagado;
    private double interes;
    private double ivaInteres;
    private double interesPagado;
    private double ivaInteresPagado;
    private double multa;
    private double ivaMulta;
    private double multaPagado;
    private double ivaMultaPagado;
    private double montoPagar;
    private double montoPagado;    
    private int dashsucursal;
    private int dashSemana;
    private String dashNomCampo;
    private double dashValCampo;
    private String dashDescrip;

    public MigracionInfoVO() {
    }

    public MigracionInfoVO(int periodo, Date fecInicial, Date fecFinal) {
        this.periodo = periodo;
        this.fecInicial = fecInicial;
        this.fecFinal = fecFinal;
    }

    public MigracionInfoVO(int numSucursal, double capital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double montoPagado) {
        this.numSucursal = numSucursal;
        this.capital = capital;
        this.capitalPagado = capitalPagado;
        this.interes = interes;
        this.ivaInteres = ivaInteres;
        this.interesPagado = interesPagado;
        this.ivaInteresPagado = ivaInteresPagado;
        this.multa = multa;
        this.ivaMulta = ivaMulta;
        this.multaPagado = multaPagado;
        this.ivaMultaPagado = ivaMultaPagado;
        this.montoPagar = montoPagar;
        this.montoPagado = montoPagado;
    }

    public MigracionInfoVO(int dashsucursal, int dashSemana, String dashNomCampo, double dashValCampo, String dashDescrip) {
        this.dashsucursal = dashsucursal;
        this.dashSemana = dashSemana;
        this.dashNomCampo = dashNomCampo;
        this.dashValCampo = dashValCampo;
        this.dashDescrip = dashDescrip;
    }

    public Date getFecFinal() {
        return fecFinal;
    }

    public void setFecFinal(Date fecFinal) {
        this.fecFinal = fecFinal;
    }

    public Date getFecInicial() {
        return fecInicial;
    }

    public void setFecInicial(Date fecInicial) {
        this.fecInicial = fecInicial;
    }

    public int getNumSucursal() {
        return numSucursal;
    }

    public void setNumSucursal(int numSucursal) {
        this.numSucursal = numSucursal;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(double interesPagado) {
        this.interesPagado = interesPagado;
    }

    public double getIvaInteres() {
        return ivaInteres;
    }

    public void setIvaInteres(double ivaInteres) {
        this.ivaInteres = ivaInteres;
    }

    public double getIvaInteresPagado() {
        return ivaInteresPagado;
    }

    public void setIvaInteresPagado(double ivaInteresPagado) {
        this.ivaInteresPagado = ivaInteresPagado;
    }

    public double getIvaMulta() {
        return ivaMulta;
    }

    public void setIvaMulta(double ivaMulta) {
        this.ivaMulta = ivaMulta;
    }

    public double getIvaMultaPagado() {
        return ivaMultaPagado;
    }

    public void setIvaMultaPagado(double ivaMultaPagado) {
        this.ivaMultaPagado = ivaMultaPagado;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public double getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(double montoPagar) {
        this.montoPagar = montoPagar;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public double getMultaPagado() {
        return multaPagado;
    }

    public void setMultaPagado(double multaPagado) {
        this.multaPagado = multaPagado;
    }

    public String getDashDescrip() {
        return dashDescrip;
    }

    public void setDashDescrip(String dashDescrip) {
        this.dashDescrip = dashDescrip;
    }

    public String getDashNomCampo() {
        return dashNomCampo;
    }

    public void setDashNomCampo(String dashNomCampo) {
        this.dashNomCampo = dashNomCampo;
    }

    public int getDashSemana() {
        return dashSemana;
    }

    public void setDashSemana(int dashSemana) {
        this.dashSemana = dashSemana;
    }

    public double getDashValCampo() {
        return dashValCampo;
    }

    public void setDashValCampo(double dashValCampo) {
        this.dashValCampo = dashValCampo;
    }

    public int getDashsucursal() {
        return dashsucursal;
    }

    public void setDashsucursal(int dashsucursal) {
        this.dashsucursal = dashsucursal;
    }
    
}

package com.sicap.clientes.vo.cartera;

import java.io.Serializable;
import java.sql.Date;

public class TablaAmortVO implements Serializable {

    public int numCliente;
    public int numCredito;
    public int numDisposicion;
    public int tipoAmortizacion;
    public int numPago;
    public Date fechaPago;
    public double saldoInicial;
    public double abonoCapital;
    public double saldoCapital;
    public double capitalPagado;
    public double comision;
    public double ivaComision;
    public double interes;
    public double ivaInteres;
    public double interesAcum;
    public double ivaInteresAcum;
    public double interesPagado;
    public double ivaInteresPagado;
    public double intMoratorio;
    public double ivaIntMoratorio;
    public double intMoratorioPagado;
    public double ivaIntMoratorioPagado;
    public double multa;
    public double ivaMulta;
    public double multaPagado;
    public double ivaMultaPagado;
    public double montoPagar;
    public double totalPagado;
    public int status;
    public String pagado;
    public double montoCapital;
    public double montoInteres;
    public double montoIvaInteres;
    public double montoInteresMora;
    public double montoIvaInteresMora;
    public double montoMulta;
    public double montoIvaMulta;
    public int condonacion;
    public int cuotasVencidas;
    public double capitalVencido;
    public double interesVencido;
    public double ivaInteresVencido;
    public double montoTotVencido;
    public double moraVencido;
    public double ivaMoraVencido;
    public double multaVencido;
    public double ivaMultaVencido;
    public double capitalAnticipado;
    public int completaATiempo;
    public double incrementoCapital;
    public double quitaIntres;
    public double quitaIvaIntres;

    public TablaAmortVO() {
        numCliente = 0;
        numCredito = 0;
        numDisposicion = 0;
        tipoAmortizacion = 0;
        numPago = 0;
        fechaPago = null;
        saldoInicial = 0.00;
        abonoCapital = 0.00;
        saldoCapital = 0.00;
        capitalPagado = 0.00;
        comision = 0.00;
        ivaComision = 0.00;
        interes = 0.00;
        ivaInteres = 0.00;
        interesAcum = 0.00;
        ivaInteresAcum = 0.00;
        interesPagado = 0.00;
        ivaInteresPagado = 0.00;
        intMoratorio = 0.00;
        ivaIntMoratorio = 0.00;
        intMoratorioPagado = 0.00;
        ivaIntMoratorioPagado = 0.00;
        multa = 0.00;
        ivaMulta = 0.00;
        multaPagado = 0.00;
        ivaMultaPagado = 0.00;
        montoPagar = 0.00;
        totalPagado = 0.00;
        status = 0;
        pagado = "";
        montoCapital = 0;
        montoInteres = 0;
        montoIvaInteres = 0;
        capitalAnticipado = 0;
        incrementoCapital = 0.00;
        quitaIntres = 0.00;
        quitaIvaIntres = 0.00;
    }

    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double interes, double ivaInteres, double intMoratorio, double ivaIntMoratorio, double multa, double ivaMulta, double montoPagar, double totalPagado, int status, String pagado, double montoCapital, double montoInteres, double montoIvaInteres, double montoInteresMora, double montoIvaInteresMora, double montoMulta, double montoIvaMulta) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
        this.interes = interes;
        this.ivaInteres = ivaInteres;
        this.intMoratorio = intMoratorio;
        this.ivaIntMoratorio = ivaIntMoratorio;
        this.multa = multa;
        this.ivaMulta = ivaMulta;
        this.montoPagar = montoPagar;
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
        this.montoCapital = montoCapital;
        this.montoInteres = montoInteres;
        this.montoIvaInteres = montoIvaInteres;
        this.montoInteresMora = montoInteresMora;
        this.montoIvaInteresMora = montoIvaInteresMora;
        this.montoMulta = montoMulta;
        this.montoIvaMulta = montoIvaMulta;
    }

    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double intMoratorio, double ivaIntMoratorio, double intMoratorioPagado, double ivaIntMoratorioPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, int status, String pagado, double montoCapital, double montoInteres, double montoIvaInteres) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
        this.capitalPagado = capitalPagado;
        this.interes = interes;
        this.ivaInteres = ivaInteres;
        this.interesPagado = interesPagado;
        this.ivaInteresPagado = ivaInteresPagado;
        this.intMoratorio = intMoratorio;
        this.ivaIntMoratorio = ivaIntMoratorio;
        this.intMoratorioPagado = intMoratorioPagado;
        this.ivaIntMoratorioPagado = ivaIntMoratorioPagado;
        this.multa = multa;
        this.ivaMulta = ivaMulta;
        this.multaPagado = multaPagado;
        this.ivaMultaPagado = ivaMultaPagado;
        this.montoPagar = montoPagar;
        this.status = status;
        this.pagado = pagado;
        this.montoCapital = montoCapital;
        this.montoInteres = montoInteres;
        this.montoIvaInteres = montoIvaInteres;
    }
    
    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double totalPagado, int status, String pagado, int condonacion) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
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
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
        this.condonacion = condonacion;
    }
    
    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double totalPagado, int status, String pagado, double quitaIntres, double quitaIvaIntres) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
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
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
        this.quitaIntres = quitaIntres;
        this.quitaIvaIntres = quitaIvaIntres;
    }

    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double saldoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double intMoratorio, double ivaIntMoratorio, double intMoratorioPagado, double ivaIntMoratorioPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double totalPagado, int status, String pagado, double montoCapital, double montoInteres, double montoIvaInteres, double montoInteresMora, double montoIvaInteresMora, double montoMulta, double montoIvaMulta) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
        this.saldoCapital = saldoCapital;
        this.capitalPagado = capitalPagado;
        this.interes = interes;
        this.ivaInteres = ivaInteres;
        this.interesPagado = interesPagado;
        this.ivaInteresPagado = ivaInteresPagado;
        this.intMoratorio = intMoratorio;
        this.ivaIntMoratorio = ivaIntMoratorio;
        this.intMoratorioPagado = intMoratorioPagado;
        this.ivaIntMoratorioPagado = ivaIntMoratorioPagado;
        this.multa = multa;
        this.ivaMulta = ivaMulta;
        this.multaPagado = multaPagado;
        this.ivaMultaPagado = ivaMultaPagado;
        this.montoPagar = montoPagar;
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
        this.montoCapital = montoCapital;
        this.montoInteres = montoInteres;
        this.montoIvaInteres = montoIvaInteres;
        this.montoInteresMora = montoInteresMora;
        this.montoIvaInteresMora = montoIvaInteresMora;
        this.montoMulta = montoMulta;
        this.montoIvaMulta = montoIvaMulta;
    }

    public TablaAmortVO(Date fechaPago, double saldoCapital) {
        this.fechaPago = fechaPago;
        this.saldoCapital = saldoCapital;
    }

    public TablaAmortVO(int numPago, Date fechaPago, double abonoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double intMoratorio, double ivaIntMoratorio, double intMoratorioPagado, double ivaIntMoratorioPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double totalPagado, int status, String pagado, int completaATiempo) {
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
        this.capitalPagado = capitalPagado;
        this.interes = interes;
        this.ivaInteres = ivaInteres;
        this.interesPagado = interesPagado;
        this.ivaInteresPagado = ivaInteresPagado;
        this.intMoratorio = intMoratorio;
        this.ivaIntMoratorio = ivaIntMoratorio;
        this.intMoratorioPagado = intMoratorioPagado;
        this.ivaIntMoratorioPagado = ivaIntMoratorioPagado;
        this.multa = multa;
        this.ivaMulta = ivaMulta;
        this.multaPagado = multaPagado;
        this.ivaMultaPagado = ivaMultaPagado;
        this.montoPagar = montoPagar;
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
        this.completaATiempo = completaATiempo;
    }
    
    public TablaAmortVO(int numCliente, int numCredito, int numPago, Date fechaPago, double abonoCapital, double capitalPagado, double interes, double ivaInteres, double interesPagado, double ivaInteresPagado, double multa, double ivaMulta, double multaPagado, double ivaMultaPagado, double montoPagar, double totalPagado, int status, String pagado) {
        this.numCliente = numCliente;
        this.numCredito = numCredito;
        this.numPago = numPago;
        this.fechaPago = fechaPago;
        this.abonoCapital = abonoCapital;
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
        this.totalPagado = totalPagado;
        this.status = status;
        this.pagado = pagado;
    }

    public String toString() {
        String respuesta = null;
        respuesta += "numCliete=[" + numCliente + "],";
        respuesta += "numCredito=[" + numCredito + "],";
        respuesta += "numDisposicion=[" + numDisposicion + "],";
        respuesta += "tipoAmortizacion=[" + tipoAmortizacion + "],";
        respuesta += "noPago=[" + numPago + "],";
        respuesta += "saldoInicial=[" + saldoInicial + "],";
        respuesta += "abonoCapital=[" + abonoCapital + "],";
        respuesta += "saldoCapital=[" + saldoCapital + "],";
        respuesta += "capitalPagado=[" + capitalPagado + "],";
        respuesta += "comision=[" + comision + "],";
        respuesta += "ivaComision=[" + ivaComision + "],";
        respuesta += "interes=[" + interes + "],";
        respuesta += "ivaInteres=[" + ivaInteres + "],";
        respuesta += "interesAcum=[" + interesAcum + "],";
        respuesta += "ivaInteresAcum=[" + ivaInteresAcum + "],";
        respuesta += "interesPagado=[" + interesPagado + "],";
        respuesta += "ivaInteresPagado=[" + ivaInteresPagado + "],";
        respuesta += "intMoratorio=[" + intMoratorio + "],";
        respuesta += "ivaIntMoratorio=[" + ivaIntMoratorio + "],";
        respuesta += "intMoratorioPagado=[" + intMoratorioPagado + "],";
        respuesta += "IvaIntMoratorioPagado=[" + ivaIntMoratorioPagado + "],";
        respuesta += "multa=[" + multa + "],";
        respuesta += "ivaMulta=[" + ivaMulta + "],";
        respuesta += "multaPagado=[" + multaPagado + "],";
        respuesta += "ivaMultaPagado=[" + ivaMultaPagado + "],";
        respuesta += "motoPagar=[" + montoPagar + "],";
        respuesta += "totalPagado=[" + totalPagado + "],";
        respuesta += "status=[" + status + "],";
        respuesta += "pagado=[" + pagado + "],";
        return respuesta;
    }

    public double getAbonoCapital() {
        return abonoCapital;
    }

    public void setAbonoCapital(double abonoCapital) {
        this.abonoCapital = abonoCapital;
    }

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getIntMoratorio() {
        return intMoratorio;
    }

    public void setIntMoratorio(double intMoratorio) {
        this.intMoratorio = intMoratorio;
    }

    public double getIntMoratorioPagado() {
        return intMoratorioPagado;
    }

    public void setIntMoratorioPagado(double intMoratorioPagado) {
        this.intMoratorioPagado = intMoratorioPagado;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getInteresAcum() {
        return interesAcum;
    }

    public void setInteresAcum(double interesAcum) {
        this.interesAcum = interesAcum;
    }

    public double getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(double interesPagado) {
        this.interesPagado = interesPagado;
    }

    public double getIvaComision() {
        return ivaComision;
    }

    public void setIvaComision(double ivaComision) {
        this.ivaComision = ivaComision;
    }

    public double getIvaIntMoratorio() {
        return ivaIntMoratorio;
    }

    public void setIvaIntMoratorio(double ivaIntMoratorio) {
        this.ivaIntMoratorio = ivaIntMoratorio;
    }

    public double getIvaIntMoratorioPagado() {
        return ivaIntMoratorioPagado;
    }

    public void setIvaIntMoratorioPagado(double ivaIntMoratorioPagado) {
        this.ivaIntMoratorioPagado = ivaIntMoratorioPagado;
    }

    public double getIvaInteres() {
        return ivaInteres;
    }

    public void setIvaInteres(double ivaInteres) {
        this.ivaInteres = ivaInteres;
    }

    public double getIvaInteresAcum() {
        return ivaInteresAcum;
    }

    public void setIvaInteresAcum(double ivaInteresAcum) {
        this.ivaInteresAcum = ivaInteresAcum;
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

    public int getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(int numCliente) {
        this.numCliente = numCliente;
    }

    public int getNumCredito() {
        return numCredito;
    }

    public void setNumCredito(int numCredito) {
        this.numCredito = numCredito;
    }

    public int getNumDisposicion() {
        return numDisposicion;
    }

    public void setNumDisposicion(int numDisposicion) {
        this.numDisposicion = numDisposicion;
    }

    public int getNumPago() {
        return numPago;
    }

    public void setNumPago(int numPago) {
        this.numPago = numPago;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public double getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(double saldoCapital) {
        this.saldoCapital = saldoCapital;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTipoAmortizacion() {
        return tipoAmortizacion;
    }

    public void setTipoAmortizacion(int tipoAmortizacion) {
        this.tipoAmortizacion = tipoAmortizacion;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public int getCondonacion() {
        return condonacion;
    }

    public void setCondonacion(int condonacion) {
        this.condonacion = condonacion;
    }

    public double getMontoCapital() {
        return montoCapital;
    }

    public void setMontoCapital(double montoCapital) {
        this.montoCapital = montoCapital;
    }

    public double getMontoInteres() {
        return montoInteres;
    }

    public void setMontoInteres(double montoInteres) {
        this.montoInteres = montoInteres;
    }

    public double getMontoInteresMora() {
        return montoInteresMora;
    }

    public void setMontoInteresMora(double montoInteresMora) {
        this.montoInteresMora = montoInteresMora;
    }

    public double getMontoIvaInteres() {
        return montoIvaInteres;
    }

    public void setMontoIvaInteres(double montoIvaInteres) {
        this.montoIvaInteres = montoIvaInteres;
    }

    public double getMontoIvaInteresMora() {
        return montoIvaInteresMora;
    }

    public void setMontoIvaInteresMora(double montoIvaInteresMora) {
        this.montoIvaInteresMora = montoIvaInteresMora;
    }

    public double getMontoIvaMulta() {
        return montoIvaMulta;
    }

    public void setMontoIvaMulta(double montoIvaMulta) {
        this.montoIvaMulta = montoIvaMulta;
    }

    public double getMontoMulta() {
        return montoMulta;
    }

    public void setMontoMulta(double montoMulta) {
        this.montoMulta = montoMulta;
    }

    public double getCapitalVencido() {
        return capitalVencido;
    }

    public void setCapitalVencido(double capitalVencido) {
        this.capitalVencido = capitalVencido;
    }

    public int getCuotasVencidas() {
        return cuotasVencidas;
    }

    public void setCuotasVencidas(int cuotasVencidas) {
        this.cuotasVencidas = cuotasVencidas;
    }

    public double getInteresVencido() {
        return interesVencido;
    }

    public void setInteresVencido(double interesVencido) {
        this.interesVencido = interesVencido;
    }

    public double getIvaInteresVencido() {
        return ivaInteresVencido;
    }

    public void setIvaInteresVencido(double ivaInteresVencido) {
        this.ivaInteresVencido = ivaInteresVencido;
    }

    public double getMontoTotVencido() {
        return montoTotVencido;
    }

    public void setMontoTotVencido(double montoTotVencido) {
        this.montoTotVencido = montoTotVencido;
    }

    public double getIvaMoraVencido() {
        return ivaMoraVencido;
    }

    public void setIvaMoraVencido(double ivaMoraVencido) {
        this.ivaMoraVencido = ivaMoraVencido;
    }

    public double getMoraVencido() {
        return moraVencido;
    }

    public void setMoraVencido(double moraVencido) {
        this.moraVencido = moraVencido;
    }

    public double getIvaMultaVencido() {
        return ivaMultaVencido;
    }

    public void setIvaMultaVencido(double ivaMultaVencido) {
        this.ivaMultaVencido = ivaMultaVencido;
    }

    public double getMultaVencido() {
        return multaVencido;
    }

    public void setMultaVencido(double multaVencido) {
        this.multaVencido = multaVencido;

    }
    public double getCapitalAnticipado() {
        return capitalAnticipado;
    }

    public void setCapitalAnticipado(double multaVencido) {
        this.capitalAnticipado = capitalAnticipado;
    }

    public int getCompletaATiempo() {
        return completaATiempo;
    }

    public void setCompletaATiempo(int completaATiempo) {
        this.completaATiempo = completaATiempo;
    }

    public double getIncrementoCapital() {
        return incrementoCapital;
    }

    public void setIncrementoCapital(double incrementoCapital) {
        this.incrementoCapital = incrementoCapital;
    }

    public double getQuitaIntres() {
        return quitaIntres;
    }

    public void setQuitaIntres(double quitaIntres) {
        this.quitaIntres = quitaIntres;
    }

    public double getQuitaIvaIntres() {
        return quitaIvaIntres;
    }

    public void setQuitaIvaIntres(double quitaIvaIntres) {
        this.quitaIvaIntres = quitaIvaIntres;
    }
}

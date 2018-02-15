package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.sicap.clientes.vo.cartera.EventosVO;
import com.sicap.clientes.vo.cartera.PagoManualVO;
import com.sicap.clientes.vo.cartera.PagosConcentradoraVO;
import com.sicap.clientes.vo.ibs.BucketIBSVO;
import java.util.ArrayList;

public class CicloGrupalVO implements Serializable {

    public int idGrupo;
    public int idCiclo;
    public int idCreditoIBS;
    public int idCuentaIBS;
    public int idTipoCiclo;
    public int estatus;
    public int estatusT24;
    public int idDireccionReunion;
    public DireccionGenericaVO direccionReunion;
    public int idDireccionAlterna;
    public DireccionGenericaVO direccionAlterna;
    public int diaReunion;
    public int horaReunion;
    public int asesor;
    public int coordinador;
    public int tasa;
    public int comision;
    public int plazo;
    public double multaRetraso;
    public double multaFalta;
    public double monto;
    public double montoConComision;
    public double montoRefinanciado;
    public double montoConSeguro;
    public IntegranteCicloVO[] integrantes;
    public ArrayList<IntegranteCicloVO> integrantesArray;
    public TablaAmortizacionVO[] tablaAmortizacion;
    public TablaAmortizacionVO[] tablaAmortInterciclo;
    public double tasaCalculada;
    public String referencia;
    public Timestamp fechaCaptura;
    public SaldoIBSVO saldo;
    public SaldoT24VO saldoT24;
    public PagosConcentradoraVO[] pagosConcentradora;
    public PagoManualVO[] pagosManuales;
    public PagoVO[] pagos;
    public PagoGrupalVO[] pagosGrupales;
    public BucketIBSVO[] pagosIBS;
    public EventosDePagoVO[] eventosDePago;
    public ArchivoAsociadoVO[] archivosAsociados;
    public int desembolsado;
    public Date fechaUltimoPago;
    public double montoUltimoPago;
    public Date fechaValor;
    public int estatusAlertasPago;
    public int numIntegrantes;
    public EventosVO[] eventos;
    public int seguro;
    public Date fechaDispersion;
    public int bancoDispersion;
    public String nombreEquipo;
    public String nombreEjecutivo;
    public String nombreSucursal;
    public Date fechaVencimiento;
    public int aperturador;
    public int garantia;
    public int atrasos;
    public int fondeador;
    public int estatusIC;
    public int estatusIC2;
    public int semDisp;
    public int aceptaAdicional;
    /**
     * JECB 01/10/2017 atributo que indica si un grupo en un ciclo determinado
     * tiene asignado un credito adicional en por lo menos uno de sus
     * integrantes
     */
    public boolean tieneAsignadoCreditoAdicional;

    public int seguroCompleto;

    public CicloGrupalVO() {
        idGrupo = 0;
        idCiclo = 0;
        idTipoCiclo = 0;
        estatus = 0;
        estatusT24 = 0;
        idDireccionReunion = 0;
        direccionReunion = null;
        idDireccionAlterna = 0;
        direccionAlterna = null;
        diaReunion = 0;
        horaReunion = 0;
        asesor = 0;
        coordinador = 0;
        tasa = 0;
        comision = 0;
        plazo = 0;
        multaRetraso = 0;
        multaFalta = 0;
        monto = 0;
        montoConComision = 0;
        montoConSeguro = 0;
        montoRefinanciado = 0;
        integrantes = null;
        integrantesArray = null;
        tablaAmortizacion = null;
        tasaCalculada = 0;
        referencia = null;
        fechaCaptura = null;
        eventosDePago = null;
        archivosAsociados = null;
        desembolsado = 0;
        fechaUltimoPago = null;
        montoUltimoPago = 0.0;
        saldoT24 = null;
        fechaValor = null;
        estatusAlertasPago = 0;
        numIntegrantes = 0;
        eventos = null;
        fechaDispersion = null;
        bancoDispersion = 0;
        atrasos = 0;
        estatusIC = 0;
        estatusIC2 = 0;
        seguroCompleto = 0;
        aceptaAdicional = 0;
        /**
         * JECB 01/10/2017 Inicializacion del atributo que indica si el grupo
         * cuenta con un credito adicional en por lo menos uno de sus
         * integrantes
         */
        tieneAsignadoCreditoAdicional = false;
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idGrupo=[" + idGrupo + "],";
        respuesta += "idCiclo=[" + idCiclo + "],";
        respuesta += "idTipoCiclo=[" + idTipoCiclo + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "desembolsado=[" + desembolsado + "],";
        respuesta += "idDireccionReunion=[" + idDireccionReunion + "],";
        respuesta += "idDireccionAlterna=[" + idDireccionAlterna + "],";
        respuesta += "diaReunion=[" + diaReunion + "],";
        respuesta += "horaReunion=[" + horaReunion + "],";
        respuesta += "asesor=[" + asesor + "],";
        respuesta += "coordinador=[" + coordinador + "],";
        respuesta += "tasa=[" + tasa + "],";
        respuesta += "tasaCalculada=[" + tasaCalculada + "],";
        respuesta += "comision=[" + comision + "],";
        respuesta += "plazo=[" + plazo + "],";
        respuesta += "multaRetraso=[" + multaRetraso + "],";
        respuesta += "multaFalta=[" + multaFalta + "],";
        respuesta += "monto=[" + monto + "],";
        respuesta += "montoConComision=[" + montoConComision + "],";
        respuesta += "montoConSeguro=[" + montoConSeguro + "],";
        respuesta += "montoRefinanciado=[" + montoRefinanciado + "],";
        respuesta += "referencia=[" + referencia + "],";
        //respuesta += "integrantes=["+integrantes+"],";
        respuesta += "fechaValor=[" + fechaValor + "],";
        respuesta += "fechaCaptura=[" + fechaCaptura + "],";
        respuesta += "fechaDispersion=[" + fechaDispersion + "],";
        respuesta += "bancoDispersion=[" + bancoDispersion + "],";
        respuesta += "garantia=[" + garantia + "],";
        respuesta += "numIntegrantes=[" + numIntegrantes + "],";
        respuesta += "aperturador=[" + aperturador + "],";
        respuesta += "fondeador=[" + fondeador + "],";
        respuesta += "estatusIC[" + estatusIC + "],";
        respuesta += "estatusIC_disp2[" + estatusIC2 + "],";
        respuesta += "aceptaAdicional[" + aceptaAdicional + "],";
        respuesta += "tieneAsignadoCreditoAdicional[" + tieneAsignadoCreditoAdicional + "]";
        return respuesta;
    }

    public CicloGrupalVO(int idGrupo, int idCiclo, int asesor, Date fechaDispersion, String nombreEquipo, String nombreEjecutivo, String nombreSucursal, Date fechaVencimiento) {
        this.idGrupo = idGrupo;
        this.idCiclo = idCiclo;
        this.asesor = asesor;
        this.fechaDispersion = fechaDispersion;
        this.nombreEquipo = nombreEquipo;
        this.nombreEjecutivo = nombreEjecutivo;
        this.nombreSucursal = nombreSucursal;
        this.fechaVencimiento = fechaVencimiento;
    }

    public CicloGrupalVO(int idGrupo, int idCiclo) {
        this.idGrupo = idGrupo;
        this.idCiclo = idCiclo;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdCreditoIBS() {
        return idCreditoIBS;
    }

    public void setIdCreditoIBS(int idCreditoIBS) {
        this.idCreditoIBS = idCreditoIBS;
    }

    public int getIdCuentaIBS() {
        return idCuentaIBS;
    }

    public void setIdCuentaIBS(int idCuentaIBS) {
        this.idCuentaIBS = idCuentaIBS;
    }

    public int getIdTipoCiclo() {
        return idTipoCiclo;
    }

    public void setIdTipoCiclo(int idTipoCiclo) {
        this.idTipoCiclo = idTipoCiclo;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getEstatusT24() {
        return estatusT24;
    }

    public void setEstatusT24(int estatusT24) {
        this.estatusT24 = estatusT24;
    }

    public int getIdDireccionReunion() {
        return idDireccionReunion;
    }

    public void setIdDireccionReunion(int idDireccionReunion) {
        this.idDireccionReunion = idDireccionReunion;
    }

    public DireccionGenericaVO getDireccionReunion() {
        return direccionReunion;
    }

    public void setDireccionReunion(DireccionGenericaVO direccionReunion) {
        this.direccionReunion = direccionReunion;
    }

    public int getIdDireccionAlterna() {
        return idDireccionAlterna;
    }

    public void setIdDireccionAlterna(int idDireccionAlterna) {
        this.idDireccionAlterna = idDireccionAlterna;
    }

    public DireccionGenericaVO getDireccionAlterna() {
        return direccionAlterna;
    }

    public void setDireccionAlterna(DireccionGenericaVO direccionAlterna) {
        this.direccionAlterna = direccionAlterna;
    }

    public int getDiaReunion() {
        return diaReunion;
    }

    public void setDiaReunion(int diaReunion) {
        this.diaReunion = diaReunion;
    }

    public int getHoraReunion() {
        return horaReunion;
    }

    public void setHoraReunion(int horaReunion) {
        this.horaReunion = horaReunion;
    }

    public int getAsesor() {
        return asesor;
    }

    public void setAsesor(int asesor) {
        this.asesor = asesor;
    }

    public int getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(int coordinador) {
        this.coordinador = coordinador;
    }

    public int getTasa() {
        return tasa;
    }

    public void setTasa(int tasa) {
        this.tasa = tasa;
    }

    public int getComision() {
        return comision;
    }

    public void setComision(int comision) {
        this.comision = comision;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public double getMultaRetraso() {
        return multaRetraso;
    }

    public void setMultaRetraso(double multaRetraso) {
        this.multaRetraso = multaRetraso;
    }

    public double getMultaFalta() {
        return multaFalta;
    }

    public void setMultaFalta(double multaFalta) {
        this.multaFalta = multaFalta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMontoConComision() {
        return montoConComision;
    }

    public void setMontoConComision(double montoConComision) {
        this.montoConComision = montoConComision;
    }

    public double getMontoConSeguro() {
        return montoConSeguro;
    }

    public void setMontoConSeguro(double montoConSeguro) {
        this.montoConSeguro = montoConSeguro;
    }

    public double getMontoRefinanciado() {
        return montoRefinanciado;
    }

    public void setMontoRefinanciado(double montoRefinanciado) {
        this.montoRefinanciado = montoRefinanciado;
    }

    public IntegranteCicloVO[] getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(IntegranteCicloVO[] integrantes) {
        this.integrantes = integrantes;
    }

    public ArrayList<IntegranteCicloVO> getIntegrantesArray() {
        return integrantesArray;
    }

    public void setIntegrantesArray(ArrayList<IntegranteCicloVO> integrantesArray) {
        this.integrantesArray = integrantesArray;
    }

    public TablaAmortizacionVO[] getTablaAmortizacion() {
        return tablaAmortizacion;
    }

    public void setTablaAmortizacion(TablaAmortizacionVO[] tablaAmortizacion) {
        this.tablaAmortizacion = tablaAmortizacion;
    }

    public double getTasaCalculada() {
        return tasaCalculada;
    }

    public void setTasaCalculada(double tasaCalculada) {
        this.tasaCalculada = tasaCalculada;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public SaldoIBSVO getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoIBSVO saldo) {
        this.saldo = saldo;
    }

    public SaldoT24VO getSaldoT24() {
        return saldoT24;
    }

    public void setSaldoT24(SaldoT24VO saldoT24) {
        this.saldoT24 = saldoT24;
    }

    public PagosConcentradoraVO[] getPagosConcentradora() {
        return pagosConcentradora;
    }

    public void setPagosConcentradora(PagosConcentradoraVO[] pagosConcentradora) {
        this.pagosConcentradora = pagosConcentradora;
    }

    public PagoManualVO[] getPagosManuales() {
        return pagosManuales;
    }

    public void setPagosManuales(PagoManualVO[] pagosManuales) {
        this.pagosManuales = pagosManuales;
    }

    public PagoVO[] getPagos() {
        return pagos;
    }

    public void setPagos(PagoVO[] pagos) {
        this.pagos = pagos;
    }

    public PagoGrupalVO[] getPagosGrupales() {
        return pagosGrupales;
    }

    public void setPagosGrupales(PagoGrupalVO[] pagosGrupales) {
        this.pagosGrupales = pagosGrupales;
    }

    public BucketIBSVO[] getPagosIBS() {
        return pagosIBS;
    }

    public void setPagosIBS(BucketIBSVO[] pagosIBS) {
        this.pagosIBS = pagosIBS;
    }

    public EventosDePagoVO[] getEventosDePago() {
        return eventosDePago;
    }

    public void setEventosDePago(EventosDePagoVO[] eventosDePago) {
        this.eventosDePago = eventosDePago;
    }

    public ArchivoAsociadoVO[] getArchivosAsociados() {
        return archivosAsociados;
    }

    public void setArchivosAsociados(ArchivoAsociadoVO[] archivosAsociados) {
        this.archivosAsociados = archivosAsociados;
    }

    public int getDesembolsado() {
        return desembolsado;
    }

    public void setDesembolsado(int desembolsado) {
        this.desembolsado = desembolsado;
    }

    public Date getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public double getMontoUltimoPago() {
        return montoUltimoPago;
    }

    public void setMontoUltimoPago(double montoUltimoPago) {
        this.montoUltimoPago = montoUltimoPago;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public int getEstatusAlertasPago() {
        return estatusAlertasPago;
    }

    public void setEstatusAlertasPago(int estatusAlertasPago) {
        this.estatusAlertasPago = estatusAlertasPago;
    }

    public int getNumIntegrantes() {
        return numIntegrantes;
    }

    public void setNumIntegrantes(int numIntegrantes) {
        this.numIntegrantes = numIntegrantes;
    }

    public EventosVO[] getEventos() {
        return eventos;
    }

    public void setEventos(EventosVO[] eventos) {
        this.eventos = eventos;
    }

    public int getSeguro() {
        return seguro;
    }

    public void setSeguro(int seguro) {
        this.seguro = seguro;
    }

    public Date getFechaDispersion() {
        return fechaDispersion;
    }

    public void setFechaDispersion(Date fechaDispersion) {
        this.fechaDispersion = fechaDispersion;
    }

    public int getBancoDispersion() {
        return bancoDispersion;
    }

    public void setBancoDispersion(int bancoDispersion) {
        this.bancoDispersion = bancoDispersion;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombreEjecutivo() {
        return nombreEjecutivo;
    }

    public void setNombreEjecutivo(String nombreEjecutivo) {
        this.nombreEjecutivo = nombreEjecutivo;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getAperturador() {
        return aperturador;
    }

    public void setAperturador(int aperturador) {
        this.aperturador = aperturador;
    }

    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    public int getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(int atraso) {
        this.atrasos = atraso;
    }

    public int getFondeador() {
        return fondeador;
    }

    public void setFondeador(int fondeador) {
        this.fondeador = fondeador;
    }

    public int getEstatusIC() {
        return estatusIC;
    }

    public void setEstatusIC(int estatusIC) {
        this.estatusIC = estatusIC;
    }

    public TablaAmortizacionVO[] getTablaAmortInterciclo() {
        return tablaAmortInterciclo;
    }

    public void setTablaAmortInterciclo(TablaAmortizacionVO[] tablaAmortInterciclo) {
        this.tablaAmortInterciclo = tablaAmortInterciclo;
    }

    public int getEstatusIC2() {
        return estatusIC2;
    }

    public void setEstatusIC2(int estatusIC2) {
        this.estatusIC2 = estatusIC2;
    }

    public int getSemDisp() {
        return semDisp;
    }

    public void setSemDisp(int semDisp) {
        this.semDisp = semDisp;
    }

    public int getSeguroCompleto() {
        return seguroCompleto;
    }

    public void setSeguroCompleto(int seguroCompleto) {
        this.seguroCompleto = seguroCompleto;
    }

    public int getAceptaAdicional() {
        return aceptaAdicional;
    }

    public void setAceptaAdicional(int aceptaAdicional) {
        this.aceptaAdicional = aceptaAdicional;

    }
    public boolean isTieneAsignadoCreditoAdicional() {
        return tieneAsignadoCreditoAdicional;
    }

    public void setTieneAsignadoCreditoAdicional(boolean tieneAsignadoCreditoAdicional) {
        this.tieneAsignadoCreditoAdicional = tieneAsignadoCreditoAdicional;
    }
}

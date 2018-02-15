package com.sicap.clientes.vo;

import java.sql.Date;

public class SaldoHistoricoVO {
	
	private int    origen;
	private int    credito;
	private String referencia;
	private int    idClienteSICAP;
	private int    idSolicitudSICAP;
	private int    idClienteIBS;	
	private String nombreCliente;
	private String rfc;
	private int    idSucursal;
	private String nombreSucursal;
	private Date   fechaEnvio;
	private Date   fechaGeneracion;
	private int    horaGeneracion;
	private int    numeroCuotas;
	private int    numeroCuotasTranscurridas;
	private int    plazo;
	private int    periodicidad;
	private Date   fechaDesembolso;
	private Date   fechaVencimiento;
	private double montoCredito;
	private double saldoTotalAlDia;
	private double saldoCapital;
	private double saldoInteres;
	private double saldoInteresVigente;
	private double saldoInteresVencido;
	private double saldoInteresVencido90dias;
	private double saldoInteresCtasOrden;
	private double saldoIvaInteres;
	private double saldoBonificacionDeIVA;
	private double saldoMora;
	private double saldoIVAMora;
	private double saldoMulta;
	private double saldoIVAMulta;
	private double capitalPagado;
	private double interesNormalPagado;
	private double ivaInteresNormalPagado;
	private double bonificacionPagada;
	private double moratorioPagado;
	private double ivaMoraPagado;
	private double multaPagada;
	private double ivaMultaPagado;
	private double comision;
	private double ivaComision;
	private double montoSeguro;
	private double montoDesembolsado;
	private Date   fechaSigAmortizacion;
	private double capitalSigAmortizacion;
	private double interesSigAmortizacion;
	private double ivaSigAmortizacion;
	private int    Fondeador;
	private String nombreFondeador;
	private double tasaInteresSinIVA;
	private double tasaMoraSinIVA;
	private double tasaIVA;
	private double saldoConInteresAlFinal;
	private double capitalVencido;
	private double interesVencido;
	private double ivaInteresVencido;
	private double totalVencido;
	private int    estatus;
	
	private long   ctaContable;
	
	private int    idProducto;
	private Date   fechaIncumplimiento;
	private Date   fechaAcarteraVencida;
	private int    diasMora;
	private int    diasTranscurridos;
	private int    cuotasVencidas;
	private int    numeroPagosRealizados;
	private double montoTotalPagado;
	private Date   fechaUltimoPago;
	private String banderaReestructura;
	private double creditoReestructurado;
	private int    diasMoraReestructura;
	private String tasaPreferencialIVA;
	private int    cuentaBucket;
	private double saldoBucket;
	private Date   fechaHistorico;
        private double interesPorDevengar;
        private double ivaInteresPorDevengar;
        private int numPagoSostenido;
	
	public SaldoHistoricoVO(){
		origen  = 0;
		credito = 0;
		referencia = null;
		idClienteSICAP = 0;
		idSolicitudSICAP = 0;
		idClienteIBS = 0;
		nombreCliente = null;
		rfc = null;
		idSucursal = 0;
		nombreSucursal = null;
		fechaEnvio = null;
		fechaGeneracion = null;
		horaGeneracion = 0;
		numeroCuotas = 0;
		numeroCuotasTranscurridas = 0;
		plazo = 0;
		periodicidad = 0;
		fechaDesembolso = null;
		fechaVencimiento = null;
		montoCredito = 0;
		saldoTotalAlDia = 0;
		saldoCapital = 0;
		saldoInteres = 0;
		saldoInteresVigente = 0;
		saldoInteresVencido = 0;
		saldoInteresVencido90dias = 0;
		saldoInteresCtasOrden = 0;
		saldoIvaInteres = 0;
		saldoBonificacionDeIVA = 0;
		saldoMora = 0;
		saldoIVAMora = 0;
		saldoMulta = 0;
		saldoIVAMulta = 0;
		capitalPagado = 0;
		interesNormalPagado = 0;
		ivaInteresNormalPagado = 0;
		bonificacionPagada = 0;
		moratorioPagado = 0;
		ivaMoraPagado = 0;
		multaPagada = 0;
		ivaMultaPagado = 0;
		comision = 0;
		ivaComision = 0;
		montoSeguro = 0;
		montoDesembolsado = 0;
		fechaSigAmortizacion = null;
		capitalSigAmortizacion = 0;
		interesSigAmortizacion = 0;
		ivaSigAmortizacion = 0;
		Fondeador = 0;
		nombreFondeador = null;
		tasaInteresSinIVA = 0;
		tasaMoraSinIVA = 0;
		tasaIVA = 0;
		saldoConInteresAlFinal = 0;
		capitalVencido = 0;
		interesVencido = 0;
		ivaInteresVencido = 0;
		totalVencido = 0;
		estatus = 0;
		idProducto = 0;
		fechaIncumplimiento = null;
		fechaAcarteraVencida = null;
		diasMora = 0;
		diasTranscurridos = 0;
		cuotasVencidas = 0;
		numeroPagosRealizados = 0;
		montoTotalPagado = 0;
		fechaUltimoPago = null;
		banderaReestructura = null;
		creditoReestructurado = 0;
		diasMoraReestructura = 0;
		tasaPreferencialIVA = null;
		cuentaBucket = 0;
		saldoBucket  = 0;
		fechaHistorico = null;
                interesPorDevengar = 0;
                ivaInteresPorDevengar = 0;
                numPagoSostenido = 0;
	}

	public String getBanderaReestructura() {
		return banderaReestructura;
	}

	public void setBanderaReestructura(String banderaReestructura) {
		this.banderaReestructura = banderaReestructura;
	}

	public double getCapitalPagado() {
		return capitalPagado;
	}

	public void setCapitalPagado(double capitalPagado) {
		this.capitalPagado = capitalPagado;
	}

	public double getCapitalSigAmortizacion() {
		return capitalSigAmortizacion;
	}

	public void setCapitalSigAmortizacion(double capitalSigAmortizacion) {
		this.capitalSigAmortizacion = capitalSigAmortizacion;
	}

	public double getCapitalVencido() {
		return capitalVencido;
	}

	public void setCapitalVencido(double capitalVencido) {
		this.capitalVencido = capitalVencido;
	}

	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

	public int getCredito() {
		return credito;
	}

	public void setCredito(int credito) {
		this.credito = credito;
	}

	public double getCreditoReestructurado() {
		return creditoReestructurado;
	}

	public void setCreditoReestructurado(double creditoReestructurado) {
		this.creditoReestructurado = creditoReestructurado;
	}

	public int getCuotasVencidas() {
		return cuotasVencidas;
	}

	public void setCuotasVencidas(int cuotasVencidas) {
		this.cuotasVencidas = cuotasVencidas;
	}

	public int getDiasMora() {
		return diasMora;
	}

	public void setDiasMora(int diasMora) {
		this.diasMora = diasMora;
	}

	public int getDiasTranscurridos() {
		return diasTranscurridos;
	}

	public void setDiasTranscurridos(int diasTranscurridos) {
		this.diasTranscurridos = diasTranscurridos;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}


	public Date getFechaAcarteraVencida() {
		return fechaAcarteraVencida;
	}

	public void setFechaAcarteraVencida(Date fechaAcarteraVencida) {
		this.fechaAcarteraVencida = fechaAcarteraVencida;
	}

	public Date getFechaDesembolso() {
		return fechaDesembolso;
	}

	public void setFechaDesembolso(Date fechaDesembolso) {
		this.fechaDesembolso = fechaDesembolso;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public Date getFechaIncumplimiento() {
		return fechaIncumplimiento;
	}

	public void setFechaIncumplimiento(Date fechaIncumplimiento) {
		this.fechaIncumplimiento = fechaIncumplimiento;
	}

	public Date getFechaSigAmortizacion() {
		return fechaSigAmortizacion;
	}

	public void setFechaSigAmortizacion(Date fechaSigAmortizacion) {
		this.fechaSigAmortizacion = fechaSigAmortizacion;
	}

	public Date getFechaUltimoPago() {
		return fechaUltimoPago;
	}

	public void setFechaUltimoPago(Date fechaUltimoPago) {
		this.fechaUltimoPago = fechaUltimoPago;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public int getHoraGeneracion() {
		return horaGeneracion;
	}

	public void setHoraGeneracion(int horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public int getIdClienteIBS() {
		return idClienteIBS;
	}

	public void setIdClienteIBS(int idClienteIBS) {
		this.idClienteIBS = idClienteIBS;
	}

	public int getIdClienteSICAP() {
		return idClienteSICAP;
	}

	public void setIdClienteSICAP(int idClienteSICAP) {
		this.idClienteSICAP = idClienteSICAP;
	}

	public int getFondeador() {
		return Fondeador;
	}

	public void setFondeador(int fondeador) {
		Fondeador = fondeador;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public int getIdSolicitudSICAP() {
		return idSolicitudSICAP;
	}

	public void setIdSolicitudSICAP(int idSolicitudSICAP) {
		this.idSolicitudSICAP = idSolicitudSICAP;
	}

	public int getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}

	public double getInteresNormalPagado() {
		return interesNormalPagado;
	}

	public void setInteresNormalPagado(double interesNormalPagado) {
		this.interesNormalPagado = interesNormalPagado;
	}

	public double getInteresSigAmortizacion() {
		return interesSigAmortizacion;
	}

	public void setInteresSigAmortizacion(double interesSigAmortizacion) {
		this.interesSigAmortizacion = interesSigAmortizacion;
	}

	public double getInteresVencido() {
		return interesVencido;
	}

	public void setInteresVencido(double interesVencido) {
		this.interesVencido = interesVencido;
	}

	public double getIvaComision() {
		return ivaComision;
	}

	public void setIvaComision(double ivaComision) {
		this.ivaComision = ivaComision;
	}

	public double getIvaInteresNormalPagado() {
		return ivaInteresNormalPagado;
	}

	public void setIvaInteresNormalPagado(double ivaInteresNormalPagado) {
		this.ivaInteresNormalPagado = ivaInteresNormalPagado;
	}

	public double getIvaInteresVencido() {
		return ivaInteresVencido;
	}

	public void setIvaInteresVencido(double ivaInteresVencido) {
		this.ivaInteresVencido = ivaInteresVencido;
	}

	public double getIvaMoraPagado() {
		return ivaMoraPagado;
	}

	public void setIvaMoraPagado(double ivaMoraPagado) {
		this.ivaMoraPagado = ivaMoraPagado;
	}

	public double getIvaMultaPagado() {
		return ivaMultaPagado;
	}

	public void setIvaMultaPagado(double ivaMultaPagado) {
		this.ivaMultaPagado = ivaMultaPagado;
	}

	public double getIvaSigAmortizacion() {
		return ivaSigAmortizacion;
	}

	public void setIvaSigAmortizacion(double ivaSigAmortizacion) {
		this.ivaSigAmortizacion = ivaSigAmortizacion;
	}

	public double getMontoCredito() {
		return montoCredito;
	}

	public void setMontoCredito(double montoCredito) {
		this.montoCredito = montoCredito;
	}

	public double getMontoDesembolsado() {
		return montoDesembolsado;
	}

	public void setMontoDesembolsado(double montoDesembolsado) {
		this.montoDesembolsado = montoDesembolsado;
	}

	public double getMontoTotalPagado() {
		return montoTotalPagado;
	}

	public void setMontoTotalPagado(double montoTotalPagado) {
		this.montoTotalPagado = montoTotalPagado;
	}

	public double getMoratorioPagado() {
		return moratorioPagado;
	}

	public void setMoratorioPagado(double moratorioPagado) {
		this.moratorioPagado = moratorioPagado;
	}

	public double getMultaPagada() {
		return multaPagada;
	}

	public void setMultaPagada(double multaPagada) {
		this.multaPagada = multaPagada;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNombreFondeador() {
		return nombreFondeador;
	}

	public void setNombreFondeador(String nombreFondeador) {
		this.nombreFondeador = nombreFondeador;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public int getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(int numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public int getNumeroCuotasTranscurridas() {
		return numeroCuotasTranscurridas;
	}

	public void setNumeroCuotasTranscurridas(int numeroCuotasTranscurridas) {
		this.numeroCuotasTranscurridas = numeroCuotasTranscurridas;
	}

	public int getNumeroPagosRealizados() {
		return numeroPagosRealizados;
	}

	public void setNumeroPagosRealizados(int numeroPagosRealizados) {
		this.numeroPagosRealizados = numeroPagosRealizados;
	}

	public int getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(int periodicidad) {
		this.periodicidad = periodicidad;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public double getSaldoCapital() {
		return saldoCapital;
	}

	public void setSaldoCapital(double saldoCapital) {
		this.saldoCapital = saldoCapital;
	}

	public double getSaldoConInteresAlFinal() {
		return saldoConInteresAlFinal;
	}

	public void setSaldoConInteresAlFinal(double saldoConInteresAlFinal) {
		this.saldoConInteresAlFinal = saldoConInteresAlFinal;
	}

	public double getSaldoInteres() {
		return saldoInteres;
	}

	public void setSaldoInteres(double saldoInteres) {
		this.saldoInteres = saldoInteres;
	}

	public double getSaldoInteresCtasOrden() {
		return saldoInteresCtasOrden;
	}

	public void setSaldoInteresCtasOrden(double saldoInteresCtasOrden) {
		this.saldoInteresCtasOrden = saldoInteresCtasOrden;
	}

	public double getSaldoInteresVencido() {
		return saldoInteresVencido;
	}

	public void setSaldoInteresVencido(double saldoInteresVencido) {
		this.saldoInteresVencido = saldoInteresVencido;
	}

	public double getSaldoInteresVencido90dias() {
		return saldoInteresVencido90dias;
	}

	public void setSaldoInteresVencido90dias(double saldoInteresVencido90dias) {
		this.saldoInteresVencido90dias = saldoInteresVencido90dias;
	}

	public double getSaldoInteresVigente() {
		return saldoInteresVigente;
	}

	public void setSaldoInteresVigente(double saldoInteresVigente) {
		this.saldoInteresVigente = saldoInteresVigente;
	}

	public double getSaldoIvaInteres() {
		return saldoIvaInteres;
	}

	public void setSaldoIvaInteres(double saldoIvaInteres) {
		this.saldoIvaInteres = saldoIvaInteres;
	}

	public double getSaldoIVAMora() {
		return saldoIVAMora;
	}

	public void setSaldoIVAMora(double saldoIVAMora) {
		this.saldoIVAMora = saldoIVAMora;
	}

	public double getSaldoIVAMulta() {
		return saldoIVAMulta;
	}

	public void setSaldoIVAMulta(double saldoIVAMulta) {
		this.saldoIVAMulta = saldoIVAMulta;
	}

	public double getSaldoMora() {
		return saldoMora;
	}

	public void setSaldoMora(double saldoMora) {
		this.saldoMora = saldoMora;
	}

	public double getSaldoMulta() {
		return saldoMulta;
	}

	public void setSaldoMulta(double saldoMulta) {
		this.saldoMulta = saldoMulta;
	}

	public double getSaldoTotalAlDia() {
		return saldoTotalAlDia;
	}

	public void setSaldoTotalAlDia(double saldoTotalAlDia) {
		this.saldoTotalAlDia = saldoTotalAlDia;
	}

	public double getTasaInteresSinIVA() {
		return tasaInteresSinIVA;
	}

	public void setTasaInteresSinIVA(double tasaInteresSinIVA) {
		this.tasaInteresSinIVA = tasaInteresSinIVA;
	}

	public double getTasaIVA() {
		return tasaIVA;
	}

	public void setTasaIVA(double tasaIVA) {
		this.tasaIVA = tasaIVA;
	}

	public double getTasaMoraSinIVA() {
		return tasaMoraSinIVA;
	}

	public void setTasaMoraSinIVA(double tasaMoraSinIVA) {
		this.tasaMoraSinIVA = tasaMoraSinIVA;
	}

	public String getTasaPreferencialIVA() {
		return tasaPreferencialIVA;
	}

	public void setTasaPreferencialIVA(String tasaPreferencialIVA) {
		this.tasaPreferencialIVA = tasaPreferencialIVA;
	}

	public double getTotalVencido() {
		return totalVencido;
	}

	public void setTotalVencido(double totalVencido) {
		this.totalVencido = totalVencido;
	}

	public int getCuentaBucket() {
		return cuentaBucket;
	}

	public void setCuentaBucket(int cuentaBucket) {
		this.cuentaBucket = cuentaBucket;
	}

	public double getMontoSeguro() {
		return montoSeguro;
	}

	public void setMontoSeguro(double montoSeguro) {
		this.montoSeguro = montoSeguro;
	}

	public double getSaldoBucket() {
		return saldoBucket;
	}

	public void setSaldoBucket(double saldoBucket) {
		this.saldoBucket = saldoBucket;
	}

	public double getBonificacionPagada() {
		return bonificacionPagada;
	}

	public void setBonificacionPagada(double bonificacionPagada) {
		this.bonificacionPagada = bonificacionPagada;
	}

	public double getSaldoBonificacionDeIVA() {
		return saldoBonificacionDeIVA;
	}

	public void setSaldoBonificacionDeIVA(double saldoBonificacionDeIVA) {
		this.saldoBonificacionDeIVA = saldoBonificacionDeIVA;
	}

	public int getOrigen() {
		return origen;
	}

	public void setOrigen(int origen) {
		this.origen = origen;
	}

	public int getDiasMoraReestructura() {
		return diasMoraReestructura;
	}

	public void setDiasMoraReestructura(int diasMoraReestructura) {
		this.diasMoraReestructura = diasMoraReestructura;
	}

	public long getCtaContable() {
		return ctaContable;
	}

	public void setCtaContable(long ctaContable) {
		this.ctaContable = ctaContable;
	}

	public void setFechaHistorico(Date fechaHistorico) {
		this.fechaHistorico = fechaHistorico;
	}

	public Date getFechaHistorico() {
		return fechaHistorico;
	}
        
        public double getInteresPorDevengar() {
            return interesPorDevengar;
        }
        
        public void setInteresPorDevengar (double interesPorDevengar) {
            this.interesPorDevengar = interesPorDevengar;
        }
        
        public double getIvaInteresPorDevengar() {
            return ivaInteresPorDevengar;
        }
        
        public void setIvaInteresPorDevengar (double ivaInteresPorDevengar) {
            this.ivaInteresPorDevengar = ivaInteresPorDevengar;
        }

    public int getNumPagoSostenido() {
        return numPagoSostenido;
    }

    public void setNumPagoSostenido(int numPagoSostenido) {
        this.numPagoSostenido = numPagoSostenido;
    }
}


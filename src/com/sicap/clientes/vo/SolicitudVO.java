package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.sicap.clientes.vo.cartera.PagoManualVO;
import com.sicap.clientes.vo.cartera.PagosConcentradoraVO;
import com.sicap.clientes.vo.cartera.EventosVO;
import com.sicap.clientes.vo.ibs.BucketIBSVO;

public class SolicitudVO implements Serializable {

    public int idCliente;
    public int idSolicitud;
    public int idCreditoIBS;
    public int idCuentaIBS;
    public int estatus;
    public String cveSolicitud;
    public int idSucursal;
    public int tipoOperacion;
    public Date fechaFirma;
    public Timestamp fechaCaptura;
    public int medio;
    public int idEjecutivo;
    public int fuente;
    public double montoSolicitado;
    public int plazoSolicitado;
    public int frecuenciaPagoSolicitada;
    public int destinoCredito;
    public ObligadoSolidarioVO obligadosSolidarios[];
    public double montoPropuesto;
    public int plazoPropuesto;
    public int frecuenciaPagoPropuesta;
    public double cuota;
    public int desembolsado;
    public Date fechaDesembolso;
    public CreditoVO buroCredito;
    public CreditoVO circuloCredito;
    public DecisionComiteVO decisionComite;
    public OrdenDePagoVO ordenPago;
    public ArrendatarioVO arrendatarioDomicilio;
    public ArrendatarioVO arrendatarioLocal;
    public NegocioVO negocio;
    public ReferenciaComercialVO[] referenciasComerciales;
    public ReferenciaPersonalVO[] referenciasPersonales;
    public InformacionFinancieraVO informacion;
    public ArchivoAsociadoVO[] archivosAsociados;
    public EmpleoVO empleo;
    public ViviendaVO vivienda;
    public CapacidadPagoVO capacidadPago;
    public ReferenciaLaboralVO referenciaLaboral;
    public SegurosVO seguro;
    public String contrato;
    public TablaAmortizacionVO[] amortizacion;
    public ReferenciaCrediticiaVO[] referenciasCrediticias;
    public ScoringVO scoring;
    public double tasaCalculada;
    public String numCheque;
    public CreditoViviendaVO creditoVivienda;
    public String CAT;
    public ExpedienteVO expediente;
    public InformacionCrediticiaVO infoCreditoBuro;
    public InformacionCrediticiaVO infoCreditoCirculo;
    public int numrepresentante;
    public PagoVO[] pagos;
    public SaldoIBSVO saldo;
    public String comentarios;
    public double saldoVigente;
    public double saldoVencido;
    public LimiteCreditoVO limites;
    public SellFinanceVO sellFinance;
    public DisposicionVO[] disposiciones;
    public String referencia;
    public PagosConcentradoraVO[] pagosConcentradora;
    public PagoManualVO[] pagosManuales;
    public BucketIBSVO[] pagosIBS;
    public EventosVO[] eventos;
    public int reestructura;
    public int solicitudReestructura;
    public int enGarantia;
    public int idGrupo;
    public String nombreGrupo;
    public int idCiclo;
    public int estatusCiclo;
    public String strFrecuencia;
    public ScoreFicoVO scoreCC;
    public int consultaCC;
    public int origenMigracion;
    public int rolHogar;
    public int subproducto;
    public int otroCredito;
    public int mejorIngreso;
    public String idProgProspera;
    public int documentacionCompleta;
    //Estas solo aplican para el webservice
    public int numCuotasTrans;
    public int numCuotas;
    public Date fechaFirmaConUltimoCambioDoctos;
    
    public SolicitudVO() {
        idCliente = 0;
        idSolicitud = 0;
        idCreditoIBS = 0;
        idCuentaIBS = 0;
        estatus = 0;
        cveSolicitud = null;
        idSucursal = 0;
        tipoOperacion = 0;
        fechaFirma = null;
        fechaCaptura = null;
        medio = 0;
        idEjecutivo = 0;
        fuente = 0;
        montoSolicitado = 0;
        plazoSolicitado = 0;
        frecuenciaPagoSolicitada = 0;
        destinoCredito = 0;
        obligadosSolidarios = null;
        montoPropuesto = 0;
        plazoPropuesto = 0;
        frecuenciaPagoPropuesta = 0;
        cuota = 0;
        desembolsado = 0;
        fechaDesembolso = null;
        buroCredito = null;
        circuloCredito = null;
        decisionComite = null;
        ordenPago = null;
        referenciasComerciales = null;
        referenciasPersonales = null;
        informacion = null;
        archivosAsociados = null;
        empleo = null;
        vivienda = null;
        capacidadPago = null;
        referenciaLaboral = null;
        seguro = null;
        contrato = null;
        amortizacion = null;
        referenciasCrediticias = null;
        scoring = null;
        tasaCalculada = 0;
        numCheque = null;
        creditoVivienda = null;
        CAT = null;
        expediente = null;
        infoCreditoBuro = null;
        infoCreditoCirculo = null;
        numrepresentante = 0;
        saldo = null;
        comentarios = null;
        saldoVigente = 0;
        saldoVencido = 0;
        pagos = null;
        limites = null;
        sellFinance = null;
        disposiciones = null;
        referencia = "";
        pagosConcentradora = null;
        pagosManuales = null;
        pagosIBS = null;
        reestructura = 0;
        solicitudReestructura = 0;
        idGrupo=0;
        origenMigracion=0;
        rolHogar = 0;
        subproducto = 0;
        otroCredito = 0;
        mejorIngreso = 0;
        idProgProspera = "";
        documentacionCompleta =0;
        fechaFirmaConUltimoCambioDoctos = null;
        
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idCliente=[" + idCliente + "],";
        respuesta += "idSolicitud=[" + idSolicitud + "],";
        respuesta += "idCreditoIBS=[" + idCreditoIBS + "],";
        respuesta += "idCuentaIBS=[" + idCuentaIBS + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "cveSolicitud=[" + cveSolicitud + "],";
        respuesta += "idSucursal=[" + idSucursal + "],";
        respuesta += "tipoOperacion=[" + tipoOperacion + "],";
        respuesta += "fechaFirma=[" + fechaFirma + "],";
        respuesta += "fechaCaptura=[" + fechaCaptura + "],";
        respuesta += "medio=[" + medio + "],";
        respuesta += "idEjecutivo=[" + idEjecutivo + "],";
        respuesta += "fuente=[" + fuente + "],";
        respuesta += "montoSolicitado=[" + montoSolicitado + "],";
        respuesta += "plazoSolicitado=[" + plazoSolicitado + "],";
        respuesta += "frecuenciaPagoSolicitada=[" + frecuenciaPagoSolicitada + "],";
        respuesta += "destinoCredito=[" + destinoCredito + "],";
        respuesta += "montoPropuesto=[" + montoPropuesto + "],";
        respuesta += "plazoPropuesto=[" + plazoPropuesto + "],";
        respuesta += "frecuenciaPagoPropuesta=[" + frecuenciaPagoPropuesta + "],";
        respuesta += "cuota=[" + cuota + "],";
        respuesta += "desembolsado=[" + desembolsado + "],";
        respuesta += "fechaDesembolso=[" + fechaDesembolso + "],";
        respuesta += "contrato=[" + contrato + "],";
        respuesta += "numCheque=[" + numCheque + "],";
        respuesta += "tasaCalculada=[" + tasaCalculada + "],";
        respuesta += "CAT=[" + CAT + "],";
        respuesta += "saldoVigente=[" + saldoVigente + "],";
        respuesta += "saldoVencido=[" + saldoVencido + "],";
        respuesta += "numrepresentante=[" + numrepresentante + "],";
        respuesta += "referencia=[" + referencia + "],";
        respuesta += "reestructura=[" + reestructura + "],";
        respuesta += "solicitudReestructura=[" + solicitudReestructura + "],";
        respuesta += "idGrupo=[" + idGrupo + "],";
        respuesta += "consultaCC=[" + consultaCC + "],";
        respuesta += "origenMigracion=[" + origenMigracion + "],";
        respuesta += "rolHogar=[" + rolHogar + "],";
        respuesta += "subproducto=["+subproducto+"],";
        respuesta += "otroCredito=["+otroCredito+"]";
        respuesta += "mejorIngreso=["+mejorIngreso+"]";
        respuesta += "idProgProspera=["+idProgProspera+"]";
        respuesta += "documentacionCompleta=["+documentacionCompleta+"]";
        respuesta += "documentacionCompleta=["+documentacionCompleta+"]";   
        return respuesta;
    }

    public SolicitudVO(int idCliente, int idSolicitud, Date fechaFirma, double montoSolicitado, int plazoSolicitado, String strFrecuencia) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.fechaFirma = fechaFirma;
        this.montoSolicitado = montoSolicitado;
        this.plazoSolicitado = plazoSolicitado;
        this.strFrecuencia = strFrecuencia;
    }
    
    public SolicitudVO(int idCliente, int idSolicitud, SaldoIBSVO saldo) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.saldo = saldo;
    }

    public String getCAT() {
        return CAT;
    }

    public void setCAT(String CAT) {
        this.CAT = CAT;
    }

    public TablaAmortizacionVO[] getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(TablaAmortizacionVO[] amortizacion) {
        this.amortizacion = amortizacion;
    }

    public ArchivoAsociadoVO[] getArchivosAsociados() {
        return archivosAsociados;
    }

    public void setArchivosAsociados(ArchivoAsociadoVO[] archivosAsociados) {
        this.archivosAsociados = archivosAsociados;
    }

    public ArrendatarioVO getArrendatarioDomicilio() {
        return arrendatarioDomicilio;
    }

    public void setArrendatarioDomicilio(ArrendatarioVO arrendatarioDomicilio) {
        this.arrendatarioDomicilio = arrendatarioDomicilio;
    }

    public ArrendatarioVO getArrendatarioLocal() {
        return arrendatarioLocal;
    }

    public void setArrendatarioLocal(ArrendatarioVO arrendatarioLocal) {
        this.arrendatarioLocal = arrendatarioLocal;
    }

    public CreditoVO getBuroCredito() {
        return buroCredito;
    }

    public void setBuroCredito(CreditoVO buroCredito) {
        this.buroCredito = buroCredito;
    }

    public CapacidadPagoVO getCapacidadPago() {
        return capacidadPago;
    }

    public void setCapacidadPago(CapacidadPagoVO capacidadPago) {
        this.capacidadPago = capacidadPago;
    }

    public CreditoVO getCirculoCredito() {
        return circuloCredito;
    }

    public void setCirculoCredito(CreditoVO circuloCredito) {
        this.circuloCredito = circuloCredito;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public CreditoViviendaVO getCreditoVivienda() {
        return creditoVivienda;
    }

    public void setCreditoVivienda(CreditoViviendaVO creditoVivienda) {
        this.creditoVivienda = creditoVivienda;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public String getCveSolicitud() {
        return cveSolicitud;
    }

    public void setCveSolicitud(String cveSolicitud) {
        this.cveSolicitud = cveSolicitud;
    }

    public DecisionComiteVO getDecisionComite() {
        return decisionComite;
    }

    public void setDecisionComite(DecisionComiteVO decisionComite) {
        this.decisionComite = decisionComite;
    }

    public int getDesembolsado() {
        return desembolsado;
    }

    public void setDesembolsado(int desembolsado) {
        this.desembolsado = desembolsado;
    }

    public int getDestinoCredito() {
        return destinoCredito;
    }

    public void setDestinoCredito(int destinoCredito) {
        this.destinoCredito = destinoCredito;
    }

    public DisposicionVO[] getDisposiciones() {
        return disposiciones;
    }

    public void setDisposiciones(DisposicionVO[] disposiciones) {
        this.disposiciones = disposiciones;
    }

    public EmpleoVO getEmpleo() {
        return empleo;
    }

    public void setEmpleo(EmpleoVO empleo) {
        this.empleo = empleo;
    }

    public int getEnGarantia() {
        return enGarantia;
    }

    public void setEnGarantia(int enGarantia) {
        this.enGarantia = enGarantia;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public EventosVO[] getEventos() {
        return eventos;
    }

    public void setEventos(EventosVO[] eventos) {
        this.eventos = eventos;
    }

    public ExpedienteVO getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedienteVO expediente) {
        this.expediente = expediente;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaDesembolso() {
        return fechaDesembolso;
    }

    public void setFechaDesembolso(Date fechaDesembolso) {
        this.fechaDesembolso = fechaDesembolso;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public int getFrecuenciaPagoPropuesta() {
        return frecuenciaPagoPropuesta;
    }

    public void setFrecuenciaPagoPropuesta(int frecuenciaPagoPropuesta) {
        this.frecuenciaPagoPropuesta = frecuenciaPagoPropuesta;
    }

    public int getFrecuenciaPagoSolicitada() {
        return frecuenciaPagoSolicitada;
    }

    public void setFrecuenciaPagoSolicitada(int frecuenciaPagoSolicitada) {
        this.frecuenciaPagoSolicitada = frecuenciaPagoSolicitada;
    }

    public int getFuente() {
        return fuente;
    }

    public void setFuente(int fuente) {
        this.fuente = fuente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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

    public int getIdEjecutivo() {
        return idEjecutivo;
    }

    public void setIdEjecutivo(int idEjecutivo) {
        this.idEjecutivo = idEjecutivo;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public InformacionCrediticiaVO getInfoCreditoBuro() {
        return infoCreditoBuro;
    }

    public void setInfoCreditoBuro(InformacionCrediticiaVO infoCreditoBuro) {
        this.infoCreditoBuro = infoCreditoBuro;
    }

    public InformacionCrediticiaVO getInfoCreditoCirculo() {
        return infoCreditoCirculo;
    }

    public void setInfoCreditoCirculo(InformacionCrediticiaVO infoCreditoCirculo) {
        this.infoCreditoCirculo = infoCreditoCirculo;
    }

    public InformacionFinancieraVO getInformacion() {
        return informacion;
    }

    public void setInformacion(InformacionFinancieraVO informacion) {
        this.informacion = informacion;
    }

    public LimiteCreditoVO getLimites() {
        return limites;
    }

    public void setLimites(LimiteCreditoVO limites) {
        this.limites = limites;
    }

    public int getMedio() {
        return medio;
    }

    public void setMedio(int medio) {
        this.medio = medio;
    }

    public double getMontoPropuesto() {
        return montoPropuesto;
    }

    public void setMontoPropuesto(double montoPropuesto) {
        this.montoPropuesto = montoPropuesto;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public NegocioVO getNegocio() {
        return negocio;
    }

    public void setNegocio(NegocioVO negocio) {
        this.negocio = negocio;
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public int getNumrepresentante() {
        return numrepresentante;
    }

    public void setNumrepresentante(int numrepresentante) {
        this.numrepresentante = numrepresentante;
    }

    public ObligadoSolidarioVO[] getObligadosSolidarios() {
        return obligadosSolidarios;
    }

    public void setObligadosSolidarios(ObligadoSolidarioVO[] obligadosSolidarios) {
        this.obligadosSolidarios = obligadosSolidarios;
    }

    public OrdenDePagoVO getOrdenPago() {
        return ordenPago;
    }

    public void setOrdenPago(OrdenDePagoVO ordenPago) {
        this.ordenPago = ordenPago;
    }

    public PagoVO[] getPagos() {
        return pagos;
    }

    public void setPagos(PagoVO[] pagos) {
        this.pagos = pagos;
    }

    public PagosConcentradoraVO[] getPagosConcentradora() {
        return pagosConcentradora;
    }

    public void setPagosConcentradora(PagosConcentradoraVO[] pagosConcentradora) {
        this.pagosConcentradora = pagosConcentradora;
    }

    public BucketIBSVO[] getPagosIBS() {
        return pagosIBS;
    }

    public void setPagosIBS(BucketIBSVO[] pagosIBS) {
        this.pagosIBS = pagosIBS;
    }

    public PagoManualVO[] getPagosManuales() {
        return pagosManuales;
    }

    public void setPagosManuales(PagoManualVO[] pagosManuales) {
        this.pagosManuales = pagosManuales;
    }

    public int getPlazoPropuesto() {
        return plazoPropuesto;
    }

    public void setPlazoPropuesto(int plazoPropuesto) {
        this.plazoPropuesto = plazoPropuesto;
    }

    public int getPlazoSolicitado() {
        return plazoSolicitado;
    }

    public void setPlazoSolicitado(int plazoSolicitado) {
        this.plazoSolicitado = plazoSolicitado;
    }

    public int getReestructura() {
        return reestructura;
    }

    public void setReestructura(int reestructura) {
        this.reestructura = reestructura;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public ReferenciaLaboralVO getReferenciaLaboral() {
        return referenciaLaboral;
    }

    public void setReferenciaLaboral(ReferenciaLaboralVO referenciaLaboral) {
        this.referenciaLaboral = referenciaLaboral;
    }

    public ReferenciaComercialVO[] getReferenciasComerciales() {
        return referenciasComerciales;
    }

    public void setReferenciasComerciales(ReferenciaComercialVO[] referenciasComerciales) {
        this.referenciasComerciales = referenciasComerciales;
    }

    public ReferenciaCrediticiaVO[] getReferenciasCrediticias() {
        return referenciasCrediticias;
    }

    public void setReferenciasCrediticias(ReferenciaCrediticiaVO[] referenciasCrediticias) {
        this.referenciasCrediticias = referenciasCrediticias;
    }

    public ReferenciaPersonalVO[] getReferenciasPersonales() {
        return referenciasPersonales;
    }

    public void setReferenciasPersonales(ReferenciaPersonalVO[] referenciasPersonales) {
        this.referenciasPersonales = referenciasPersonales;
    }

    public SaldoIBSVO getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoIBSVO saldo) {
        this.saldo = saldo;
    }

    public double getSaldoVencido() {
        return saldoVencido;
    }

    public void setSaldoVencido(double saldoVencido) {
        this.saldoVencido = saldoVencido;
    }

    public double getSaldoVigente() {
        return saldoVigente;
    }

    public void setSaldoVigente(double saldoVigente) {
        this.saldoVigente = saldoVigente;
    }

    public ScoringVO getScoring() {
        return scoring;
    }

    public void setScoring(ScoringVO scoring) {
        this.scoring = scoring;
    }

    public SegurosVO getSeguro() {
        return seguro;
    }

    public void setSeguro(SegurosVO seguro) {
        this.seguro = seguro;
    }

    public SellFinanceVO getSellFinance() {
        return sellFinance;
    }

    public void setSellFinance(SellFinanceVO sellFinance) {
        this.sellFinance = sellFinance;
    }

    public int getSolicitudReestructura() {
        return solicitudReestructura;
    }

    public void setSolicitudReestructura(int solicitudReestructura) {
        this.solicitudReestructura = solicitudReestructura;
    }

    public String getStrFrecuencia() {
        return strFrecuencia;
    }

    public void setStrFrecuencia(String strFrecuencia) {
        this.strFrecuencia = strFrecuencia;
    }

    public double getTasaCalculada() {
        return tasaCalculada;
    }

    public void setTasaCalculada(double tasaCalculada) {
        this.tasaCalculada = tasaCalculada;
    }

    public int getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(int tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public ViviendaVO getVivienda() {
        return vivienda;
    }

    public void setVivienda(ViviendaVO vivienda) {
        this.vivienda = vivienda;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public ScoreFicoVO getScoreCC() {
        return scoreCC;
    }

    public void setScoreCC(ScoreFicoVO scoreCC) {
        this.scoreCC = scoreCC;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getEstatusCiclo() {
        return estatusCiclo;
    }

    public void setEstatusCiclo(int estatusCiclo) {
        this.estatusCiclo = estatusCiclo;
    }

    public int getConsultaCC() {
        return consultaCC;
    }

    public void setConsultaCC(int consultaCC) {
        this.consultaCC = consultaCC;
    }

    public int getOrigenMigracion() {
        return origenMigracion;
    }

    public void setOrigenMigracion(int origenMigracion) {
        this.origenMigracion = origenMigracion;
    }

    public int getRolHogar() {
        return rolHogar;
    }

    public void setRolHogar(int rolHogar) {
        this.rolHogar = rolHogar;
    }

    public int getSubproducto() {
        return subproducto;
    }

    public void setSubproducto(int subproducto) {
        this.subproducto = subproducto;
    }

    public int getOtroCredito() {
        return otroCredito;
    }

    public void setOtroCredito(int otroCredito) {
        this.otroCredito = otroCredito;
    }

    public int getMejorIngreso() {
        return mejorIngreso;
    }

    public void setMejorIngreso(int mejorIngreso) {
        this.mejorIngreso = mejorIngreso;
    }

    public String getIdProgProspera() {
        return idProgProspera;
    }

    public void setIdProgProspera(String idProgProspera) {
        this.idProgProspera = idProgProspera;
    }

    public int getDocumentacionCompleta() {
        return documentacionCompleta;
    }

    public void setDocumentacionCompleta(int documentacionCompleta) {
        this.documentacionCompleta = documentacionCompleta;
    }
    
    
}
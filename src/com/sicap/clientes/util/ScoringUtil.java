package com.sicap.clientes.util;

import com.sicap.clientes.exceptions.ClientesDBException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.CirculoDeCreditoHelper;
import com.sicap.clientes.vo.CuentaVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.TasaInteresVO;
import org.apache.log4j.Logger;

public class ScoringUtil {

    TreeMap paramsCalifCred = null;
    TreeMap paramsTipoCuenta = null;
    TreeMap paramsAntCuenta = null;
    TreeMap paramsBusquedas = null;
    TreeMap paramsGenero = null;
    TreeMap paramsEdad = null;
    TreeMap paramsAntLaboral = null;
    TreeMap paramsTipoContrato = null;
    TreeMap paramsDepEconomEdoCivil = null;
    TreeMap paramsSitVivienda = null;
    TreeMap paramsTiempoResid = null;
    TreeMap paramsNivelVivienda = null;
    TreeMap paramsCalifZona = null;
    TreeMap paramsPisosVivienda = null;
    TreeMap paramsHabitVivienda = null;
    TreeMap paramsFachada = null;
    TreeMap paramsTecho = null;
    TreeMap paramsArraigoEmp = null;
    TreeMap paramsEmpleados = null;
    TreeMap paramsJornada = null;
    TreeMap paramsPlazoContrato = null;
    TreeMap paramsDestCredito = null;
    static TreeMap comisiones = null;
    static TreeMap tasas = null;

    private double puntajeSIC = 0;

    private static Hashtable<Integer, Double> tramos = null;
    private static Logger myLogger = Logger.getLogger(ScoringUtil.class);

    public ScoringUtil() {

    }

    public ScoringUtil(int idProducto) throws ClientesException {

        puntajeSIC = 0;
//		porcentajeComision = 0;
        paramsCalifCred = CatalogoHelper.getParametrosScoring(1);
        paramsTipoCuenta = CatalogoHelper.getParametrosScoring(2);
        paramsAntCuenta = CatalogoHelper.getParametrosScoring(3);
        paramsBusquedas = CatalogoHelper.getParametrosScoring(4);
        paramsGenero = CatalogoHelper.getParametrosScoring(5);
        paramsEdad = CatalogoHelper.getParametrosScoring(6);
        paramsAntLaboral = CatalogoHelper.getParametrosScoring(7);
        paramsTipoContrato = CatalogoHelper.getParametrosScoring(8);
        paramsDepEconomEdoCivil = CatalogoHelper.getParametrosScoring(9);
        paramsSitVivienda = CatalogoHelper.getParametrosScoring(11);

        paramsTiempoResid = CatalogoHelper.getParametrosScoring(23);

        paramsNivelVivienda = CatalogoHelper.getParametrosScoring(12);
        paramsCalifZona = CatalogoHelper.getParametrosScoring(13);
        paramsPisosVivienda = CatalogoHelper.getParametrosScoring(14);
        paramsHabitVivienda = CatalogoHelper.getParametrosScoring(15);
        paramsFachada = CatalogoHelper.getParametrosScoring(16);
        paramsTecho = CatalogoHelper.getParametrosScoring(17);
        paramsArraigoEmp = CatalogoHelper.getParametrosScoring(18);
        paramsEmpleados = CatalogoHelper.getParametrosScoring(19);
        paramsJornada = CatalogoHelper.getParametrosScoring(20);
        paramsPlazoContrato = CatalogoHelper.getParametrosScoring(21);
        paramsDestCredito = CatalogoHelper.getParametrosScoring(22);
        myLogger.debug("producto " + idProducto);
        if (comisiones == null) {
            comisiones = CatalogoHelper.getCatalogoComisiones(idProducto);
        }
        if (tasas == null) {
            tasas = CatalogoHelper.getCatalogoTasasMensuales(idProducto);
        }

    }

    public void getDictamen(ScoringVO scoring) {

        double score = 0;
        try {
            puntajeSIC = getPuntajeInformacionCrediticia(scoring);
            puntajeSIC += getPuntajeTipoCta(scoring);
            puntajeSIC += getPuntajeAntCta(scoring);
            puntajeSIC += getPuntajeBusquedas(scoring);
            score = puntajeSIC;

            scoring.disponibleMensual = obtenDisponibleMensual(scoring);

            score += getPuntajeZonaVivienda(scoring);
            score += getPuntajePisosVivienda(scoring);
            score += getPuntajeHabitacionesVivienda(scoring);
            score += getPuntajeFachadaVivienda(scoring);
            score += getPuntajeTechoVivienda(scoring);

            score += getPuntajeNumeroEmpleados(scoring);
            score += getPuntajeJornada(scoring);

            score += getPuntajeDestinoCredito(scoring);

            scoring.montoConComision = ClientesUtil.calculaMontoConComision(scoring.monto, scoring.comision, comisiones);

            scoring.cuota = obtenCuota(scoring);
            scoring.cuotaSobreDisponible = obtenCuotaSobreDisponible(scoring);

            if (scoring.cuotaSobreDisponible > 0 && scoring.cuotaSobreDisponible <= 30) {
                scoring.coberturaPago = 1;
            } else {
                scoring.coberturaPago = 2;
            }

            scoring.cuotaSobreIngresoBruto = obtenCuotaSobreIngresoBruto(scoring);

            scoring.totalCostofinanciero = (scoring.cuota * scoring.plazo) - scoring.monto;

        } catch (Exception e) {
            myLogger.error("Exception", e);
        }

    }

    private double getPuntajeInformacionCrediticia(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.calificacionSIC != 0) {
            res = (Double) paramsCalifCred.get(scoring.calificacionSIC);
        }
        return res.doubleValue();

    }

    private double getPuntajeTipoCta(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.tipoCuenta != 0) {
            res = (Double) paramsTipoCuenta.get(new Integer(scoring.tipoCuenta));
        }
        return res.doubleValue();

    }

    private double getPuntajeAntCta(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.antCuenta != 0) {
            res = (Double) paramsAntCuenta.get(new Integer(scoring.antCuenta));
        }
        return res.doubleValue();

    }

    private double getPuntajeBusquedas(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.numBuquedas != 0) {
            res = (Double) paramsBusquedas.get(new Integer(scoring.numBuquedas));
        }
        return res.doubleValue();

    }

    private double getPuntajeGenero(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.genero != 0) {
            res = (Double) paramsGenero.get(new Integer(scoring.genero));
        }
        return res.doubleValue();

    }

    private double getPuntajeEdad(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring.edad < 24) {
            res = (Double) paramsEdad.get(1);
        }
        if (scoring.edad >= 24 && scoring.edad <= 34) {
            res = (Double) paramsEdad.get(2);
        }
        if (scoring.edad >= 35 && scoring.edad <= 48) {
            res = (Double) paramsEdad.get(3);
        }
        if (scoring.edad >= 49) {
            res = (Double) paramsEdad.get(4);
        }
        return res.doubleValue();

    }

    private double getPuntajeAntLaboral(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null) {
            if (scoring.antLaboral < 1) {
                res = (Double) paramsAntLaboral.get(1);
            } else if (scoring.antLaboral >= 1 && scoring.antLaboral <= 3) {
                res = (Double) paramsAntLaboral.get(2);
            } else if (scoring.antLaboral >= 4 && scoring.antLaboral <= 5) {
                res = (Double) paramsAntLaboral.get(3);
            } else {
                res = (Double) paramsAntLaboral.get(4);
            }
        }
        return res.doubleValue();

    }

//	private double getPuntajeTipoContrato(ScoringVO scoring){
//
//		Double res = new Double(0);
//		if ( scoring!=null && scoring.tipoContrato!=0 ){
//			res = (Double)paramsTipoContrato.get( scoring.tipoContrato );
//		}
//		return res.doubleValue();
//
//	}
    private double getPuntajeDepEconomicos(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null) {
            if (scoring.estadoCivil == 1 || scoring.estadoCivil == 5) {
                res = (Double) paramsDepEconomEdoCivil.get(1);
            } else if (scoring.estadoCivil == 4 && (scoring.dependientesEconomicos == 1 || scoring.dependientesEconomicos == 2 || scoring.dependientesEconomicos == 3)) {
                res = (Double) paramsDepEconomEdoCivil.get(2);
            } else if (scoring.estadoCivil == 4 && scoring.dependientesEconomicos == 4) {
                res = (Double) paramsDepEconomEdoCivil.get(3);
            } else if (scoring.estadoCivil == 2 && (scoring.dependientesEconomicos == 1 || scoring.dependientesEconomicos == 2 || scoring.dependientesEconomicos == 3)) {
                res = (Double) paramsDepEconomEdoCivil.get(4);
            } else if (scoring.estadoCivil == 2 && scoring.dependientesEconomicos == 4) {
                res = (Double) paramsDepEconomEdoCivil.get(3);
            } else if (scoring.estadoCivil == 3 && (scoring.dependientesEconomicos == 1 || scoring.dependientesEconomicos == 2)) {
                res = (Double) paramsDepEconomEdoCivil.get(5);
            } else if (scoring.estadoCivil == 3 && (scoring.dependientesEconomicos == 3 || scoring.dependientesEconomicos == 4)) {
                res = (Double) paramsDepEconomEdoCivil.get(6);
            }
        }
        return res.doubleValue();

    }

    private double getPuntajeSituacionVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.situacionVivienda != 0) {
            if (scoring.situacionVivienda == 1 || scoring.situacionVivienda == 4 || scoring.situacionVivienda == 5) {
                res = (Double) paramsSitVivienda.get(1);
            } else {
                res = (Double) paramsSitVivienda.get(2);
            }
        }
        return res.doubleValue();

    }

    private double getPuntajeTiempoResidencia(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring.tiempoResidencia <= 4) {
            res = (Double) paramsTiempoResid.get(1);
        } else {
            res = (Double) paramsTiempoResid.get(2);
        }
        return res.doubleValue();

    }

    private double getPuntajeNivelVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.nivelVivienda != 0) {
            res = (Double) paramsNivelVivienda.get(new Integer(scoring.nivelVivienda));
        }
        return res.doubleValue();

    }

    private double getPuntajeZonaVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.calificacionZona != 0) {
            res = (Double) paramsCalifZona.get(new Integer(scoring.calificacionZona));
        }
        return res.doubleValue();

    }

    private double getPuntajePisosVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.pisosVivienda != 0) {
            res = (Double) paramsPisosVivienda.get(new Integer(scoring.pisosVivienda));
        }
        return res.doubleValue();

    }

    private double getPuntajeHabitacionesVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.habitacionesVivienda != 0) {
            res = (Double) paramsHabitVivienda.get(new Integer(scoring.habitacionesVivienda));
        }
        return res.doubleValue();

    }

    private double getPuntajeFachadaVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.caracteristicasFachada != 0) {
            res = (Double) paramsFachada.get(new Integer(scoring.caracteristicasFachada));
        }
        return res.doubleValue();

    }

    private double getPuntajeTechoVivienda(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.caracteristicasTecho != 0) {
            res = (Double) paramsTecho.get(new Integer(scoring.caracteristicasTecho));
        }
        return res.doubleValue();

    }

    private double getPuntajeArraigoEmpresa(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null) {
            if (scoring.arraigoEmpresa < 1) {
                res = (Double) paramsArraigoEmp.get(1);
            } else if (scoring.arraigoEmpresa >= 1 && scoring.arraigoEmpresa <= 5) {
                res = (Double) paramsArraigoEmp.get(2);
            } else {
                res = (Double) paramsArraigoEmp.get(3);
            }
        }
        return res.doubleValue();

    }

    private double getPuntajeNumeroEmpleados(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.numeroEmpleados != 0) {
            res = (Double) paramsEmpleados.get(new Integer(scoring.numeroEmpleados));
        }
        return res.doubleValue();

    }

    private double getPuntajeJornada(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.jornada != 0) {
            res = (Double) paramsJornada.get(new Integer(scoring.jornada));
        }
        return res.doubleValue();

    }

    private double getPuntajePlazoContrato(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.tipoContrato != 0) {
            if (scoring.tipoContrato != 0) {
                res = (Double) paramsTipoContrato.get(scoring.tipoContrato);
            }
        }
        return res.doubleValue();

    }

    private double getPuntajeDestinoCredito(ScoringVO scoring) {

        Double res = new Double(0);
        if (scoring != null && scoring.destinoCredito != 0) {
            res = (Double) paramsDestCredito.get(new Integer(scoring.destinoCredito));
        }
        return res.doubleValue();

    }

//	private void asignaTasaComision(ScoringVO scoring){
//
//		int rangoSIC = 0;
//		int rangoPerfil = 0;
//
//		if ( puntajeSIC>=1000 ){
//			rangoSIC = 1;
//		}else if ( puntajeSIC<999 && puntajeSIC>=550 ){
//			rangoSIC = 2;
//		}else{
//			rangoSIC = 3;
//		}
//		if ( puntajePerfil>=1730 ){
//			rangoPerfil = 1;
//		}else if ( puntajePerfil<1729 && puntajePerfil>=680 ){
//			rangoPerfil = 2;
//		}else{
//			rangoPerfil = 3;
//		}
//
//		if ( (rangoSIC==1 && rangoPerfil==1) || (rangoSIC==1 && rangoPerfil==2) ){
//			scoring.tasa = 1;
//			scoring.comision = 1;
//			
//		}else if( rangoSIC==3 && rangoPerfil==3 ){
//			scoring.tasa = 3;
//			scoring.comision = 3;
//		}else{
//			scoring.tasa = 2;
//			scoring.comision = 2;
//		}
//		ComisionVO comision = (ComisionVO)comisiones.get(new Integer(scoring.comision));
//		porcentajeComision = comision.porcentaje;
//
//	}
    private double obtenCuota(ScoringVO scoring) {
        double resultado = 0;
        double valorTasa = 0;

        TasaInteresVO tasa = (TasaInteresVO) tasas.get(scoring.tasa);
        valorTasa = tasa.valor;
        if (scoring.plazo == 0) {
            resultado = valorTasa * scoring.montoConComision;
        } else {
            resultado = (scoring.montoConComision / scoring.plazo) + (valorTasa * scoring.montoConComision);
        }
        return resultado;
    }

    private double obtenCuotaDescuentoNomina(ScoringVO scoring) {
        double resultado = 0;
        double valorTasa = 0;
        float numdias = 0;
        float plazo_meses = 0;
        float plazo_anos = 0;
        if (scoring.periodicidad == ClientesConstants.PAGO_QUINCENAL) {
            numdias = (float) scoring.plazo / 2 / 12 * 365;
        } else if (scoring.periodicidad == ClientesConstants.PAGO_CATORCENAL) {
            numdias = scoring.plazo * 14;
        } else if (scoring.periodicidad == ClientesConstants.PAGO_MENSUAL) {
            numdias = (float) scoring.plazo / 12 * 365;
        } else {
            numdias = scoring.plazo * 7;
        }
        TasaInteresVO tasa = (TasaInteresVO) tasas.get(scoring.tasa);
        valorTasa = tasa.valor;

        if (scoring.plazo == 0) {
            resultado = valorTasa * scoring.montoConComision;
        } else {
            resultado = java.lang.Math.ceil((((scoring.montoConComision / (scoring.plazo)) + (valorTasa * 12 / 360 * scoring.montoConComision * numdias / (scoring.plazo)))));
        }
        myLogger.info("resultado " + resultado);
        return resultado;
    }

    private double obtenDisponibleMensual(ScoringVO scoring) {
        double resultado = 0;

        resultado = (scoring.ingresosNomina + (scoring.otrosNoComprobables * 0.8) + (scoring.otrosIngresos * 0.3)) - (scoring.rentaVivienda + scoring.pagoDeuda + scoring.otrosGastos);

        return resultado;
    }

    private double obtenDisponibleQuincenal(ScoringVO scoring, double factor) {
        double resultado = 0;

        resultado = (scoring.ingresosNomina * factor);

        return resultado;
    }

    private double obtenCuotaSobreDisponible(ScoringVO scoring) {
        double resultado = 0;

        if (scoring.disponibleMensual == 0) {
            resultado = 0;
        } else {
            resultado = (int) scoring.cuota / scoring.disponibleMensual;
        }
        return resultado * 100;
    }

    private double obtenCuotaSobreIngresoBruto(ScoringVO scoring) {

        double resultado = 0;
        double ingresoBruto = scoring.ingresosNomina + scoring.otrosIngresos + scoring.otrosNoComprobables;
        if (ingresoBruto == 0) {
            resultado = 0;
        } else {
            resultado = (int) scoring.cuota / ingresoBruto;
        }
        return resultado * 100;
    }

    public void getDictamenDescuento(ScoringVO scoring, double factor) {

        double score = 0;
        int factorPeriodo = 0;;
        try {
            if (scoring.periodicidad == ClientesConstants.PAGO_QUINCENAL) {
                factorPeriodo = 2;
            } else if (scoring.periodicidad == ClientesConstants.PAGO_CATORCENAL) {
                factorPeriodo = 4;
            } else if (scoring.periodicidad == ClientesConstants.PAGO_MENSUAL) {
                factorPeriodo = 1;
            } else {
                factorPeriodo = 1;
            }

            scoring.disponibleMensual = obtenDisponibleQuincenal(scoring, factor);
            myLogger.info("comision " + scoring.comision);
            scoring.montoConComision = ClientesUtil.calculaMontoConComision(scoring.monto, scoring.comision, comisiones);

            scoring.cuota = FormatUtil.roundDouble(obtenCuotaDescuentoNomina(scoring), 0);
            scoring.cuotaSobreDisponible = obtenCuotaSobreDisponible(scoring);

            if (scoring.cuotaSobreDisponible > 0 && scoring.cuotaSobreDisponible <= 100) {
                scoring.coberturaPago = 1;
            } else {
                scoring.coberturaPago = 2;
            }

            scoring.cuotaSobreIngresoBruto = obtenCuotaSobreIngresoBruto(scoring);

            scoring.totalCostofinanciero = (scoring.cuota * scoring.plazo) - scoring.monto;

        } catch (Exception e) {
            myLogger.error("Exception", e);
        }
    }

//	private int obtenDictamenFinal(ScoringVO scoring){
//		int dictamenFinal = 0;
//		int dicatamenPerfil = 0;
//		int dicatamenReferencias = ClientesConstants.APROBAR;
//		int dicatamenCapPago = 0;
//		
//		//
//		if ( scoring.cuotaSobreDisponible>0 && scoring.cuotaSobreDisponible<=35 )
//			dicatamenCapPago = ClientesConstants.APROBAR;
//		else
//			dicatamenCapPago = ClientesConstants.DENEGAR;
//
//		//
//		if ( scoring.calificacionSIC==2 ){
//			dicatamenPerfil = ClientesConstants.DENEGAR;
//		}else{
//			if ( puntajeTotal<=1999 )
//				dicatamenPerfil = ClientesConstants.DENEGAR;
//			else if ( puntajeTotal>=2000 && puntajeTotal<=2499 ){
//				dicatamenPerfil = ClientesConstants.DUDAR;
//			}
//			else if ( puntajeTotal>=2500 ){
//				dicatamenPerfil = ClientesConstants.APROBAR;
//			}
//		}
//		//
//		if ( scoring.referencia1==2 || scoring.referencia2==2 || scoring.referenciaLaboral==2 || scoring.referenciaArrendador==2 ){
//			dicatamenReferencias = ClientesConstants.DENEGAR;
//		}
//		//
//		if ( dicatamenPerfil==ClientesConstants.DENEGAR || dicatamenCapPago==ClientesConstants.DENEGAR || dicatamenReferencias==ClientesConstants.DENEGAR ){
//			dictamenFinal = ClientesConstants.DENEGAR;
//		}
//		else if ( dicatamenPerfil==ClientesConstants.DUDAR ){
//			dictamenFinal = ClientesConstants.DUDAR;
//		}else
//			dictamenFinal = ClientesConstants.APROBAR;
//		
//		return dictamenFinal;
//	}
    public void getScore(ScoringVO scoring) {

        if (tramos == null) {
            tramos = new Hashtable<Integer, Double>();
            tramos.put(1, 0.11446);
            tramos.put(2, 0.13267);
            tramos.put(3, 0.13906);
            tramos.put(4, 0.16007);
            tramos.put(5, 0.17897);
            tramos.put(6, 0.19205);
            tramos.put(7, 0.20454);
            tramos.put(8, 0.21242);
            tramos.put(9, 0.22357);
            tramos.put(10, 0.23305);
            tramos.put(11, 0.24270);
            tramos.put(12, 0.25388);
            tramos.put(13, 0.26405);
            tramos.put(14, 0.27069);
            tramos.put(15, 0.28359);
            tramos.put(16, 0.29320);
            tramos.put(17, 0.30699);
            tramos.put(18, 0.32138);
            tramos.put(19, 0.34300);
            tramos.put(20, 0.38658);
            tramos.put(21, 10000.00);
        }

        double resultadoSexo = getPuntajeGenero(scoring);
        double resultadoEdad = getPuntajeEdad(scoring);
        double resultadoEdoCivilDepEcon = getPuntajeDepEconomicos(scoring);
        double resultadoTipoVivienda = getPuntajeNivelVivienda(scoring);
        double resultadoAntLaboral = getPuntajeAntLaboral(scoring);
        double resultadoArraigoEmp = getPuntajeArraigoEmpresa(scoring);
        double resultadoTiempoRes = getPuntajeTiempoResidencia(scoring);
        double resultadoPlazoContrato = getPuntajePlazoContrato(scoring);
        double resultadoSituacionViv = getPuntajeSituacionVivienda(scoring);
        scoring.puntuacion = 1 / (1 + Math.pow(Math.E, ((1.11248252433131) - resultadoSexo - resultadoEdad - resultadoEdoCivilDepEcon - resultadoTipoVivienda - resultadoAntLaboral - resultadoArraigoEmp - resultadoTiempoRes - resultadoPlazoContrato - resultadoSituacionViv)));
        scoring.tramo = getTramo(scoring.puntuacion);

        if (scoring.tramo <= 7) {
            scoring.dictamenFinal = ClientesConstants.APROBAR;
        } else if (scoring.tramo >= 8 && scoring.tramo <= 11) {
            scoring.dictamenFinal = ClientesConstants.INVESTIGAR;
        } else if (scoring.tramo >= 12 && scoring.tramo <= 20) {
            scoring.dictamenFinal = ClientesConstants.ALTO_RIESGO;
        } else {
            scoring.dictamenFinal = ClientesConstants.DENEGAR;
        }

        //Logger.debug("Resultado score : "+scoring.puntuacion);
    }

    private int getTramo(double puntuacion) {
        int numero = 0;

        for (int i = 1; i <= tramos.size(); i++) {
            if (puntuacion < ((Double) tramos.get(i)).doubleValue()) {
                numero = i;
                break;
            }
        }

        return numero;
    }

    public static boolean esRegular(InformacionCrediticiaVO infoCredito) {
        boolean regular = true;
        CirculoDeCreditoHelper circuloHelp = new CirculoDeCreditoHelper();
        ReporteCirculoVO reporte;
        CuentaVO cuenta;
        List cuentas;
        int cuentasEnRango = 0;
        try {
            if (infoCredito != null) {
                reporte = (ReporteCirculoVO) circuloHelp.buildXMLToObject(infoCredito.respuesta);
                cuentas = reporte.getCuentas();
                Iterator iter = cuentas.listIterator();
                if (cuentas != null && cuentas.size() > 0) {
                    int c = 0;
                    iter = cuentas.listIterator();
                    while (iter.hasNext()) {
                        c++;
                        cuenta = (CuentaVO) iter.next();
                        if (cuenta.getCvePrevencion().equals("FD") || cuenta.getCvePrevencion().equals("UP")) {
                            regular = false;
                        }

                    }
                }
            }
        } catch (Exception e) {
            myLogger.error("esRegular", e);
        }
        return regular;

    }

    public static int getCalificacionCirculo(InformacionCrediticiaVO infoCredito) {

        CirculoDeCreditoHelper circuloHelp = new CirculoDeCreditoHelper();
        ReporteCirculoVO reporte;
        Calendar cal = Calendar.getInstance();
        CuentaVO cuenta;
        List cuentas;
        int calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
        int creditosComunales = 0;
        int creditosComunalesConSaldo = 0;
        int creditosNoComunicacion = 0;
        int creditosComunicacion = 0;
        int cuentasEnRango = 0;
        int score = 0;
        int cuentasEvaluadas = 0;
        double saldoActualComunal = 0;
        double saldoVencidoComunal = 0;
        double saldoApagarcomunal = 0;
        double saldoVencidoComunicaciones = 0;
        double saldoVencidoOtras = 0;
        int atrasos = 0; //en semanas
        myLogger.debug("Iniciando el calculo de la calificacion");
        try {
            if (infoCredito != null) {
                reporte = (ReporteCirculoVO) circuloHelp.buildXMLToObject(infoCredito.respuesta);
                cuentas = reporte.getCuentas();
                Iterator iter = cuentas.listIterator();
                if (cuentas != null && cuentas.size() > 0) {
                    int c = 0;
                    while (iter.hasNext()) {
                        cuenta = (CuentaVO) iter.next();
                        if (circuloHelp.enRango(cuenta)) {
                            cuentasEnRango++;
                        }
                    }
                    myLogger.debug("Cuentas en rango:" + cuentasEnRango);
                    iter = cuentas.listIterator();
                    while (iter.hasNext()) {
                        c++;
                        cuenta = (CuentaVO) iter.next();
                        myLogger.debug("Verificando cuenta:" + c);
                        if (circuloHelp.evaluar(cuenta, cuentasEnRango)) {
                            cuentasEvaluadas++;
                            if (circuloHelp.esComunal(cuenta)) {
                                myLogger.debug("Es comunal");
                                creditosComunales++;
                                myLogger.debug("Comunales:" + creditosComunales);
                                saldoActualComunal += cuenta.getSaldoActual();
                                myLogger.debug("Saldo Actual Comunal:" + saldoActualComunal);
                                saldoApagarcomunal += cuenta.getAPagar();
                                myLogger.debug("Saldo Pagar Comunal:" + saldoApagarcomunal);
                                if (cuenta.getSaldoActual() > 0) {
                                    creditosComunalesConSaldo++;
                                }
                                saldoVencidoComunal += cuenta.getSaldoVencido();
                                myLogger.debug("Saldo Vencido Comunal:" + saldoVencidoComunal);
                            } else if (!circuloHelp.esComunicacionesServicios(cuenta)) {
                                myLogger.debug("Es otra");
                                creditosNoComunicacion++;
                                myLogger.debug("Otros:" + creditosNoComunicacion);
                                saldoVencidoOtras += cuenta.getSaldoVencido();
                                myLogger.debug("Saldo Vencido otras:" + saldoVencidoOtras);
                                atrasos = atrasos + circuloHelp.obtenAtrasos(cuenta);
                                myLogger.debug("Atrasos:" + atrasos);
                            } else {
                                myLogger.debug("Es comunicaciones");
                                creditosComunicacion++;
                                myLogger.debug("Comunicacion: " + creditosComunicacion);
                                saldoVencidoComunicaciones += cuenta.getSaldoVencido();
                                myLogger.debug("Saldo Vencido comunicaciones:" + saldoVencidoComunicaciones);
                            }
                        }
                    }
                    score = circuloHelp.getScoreFico(reporte.getScoreFico());
                    if (cuentasEvaluadas > 0) {
                        if (creditosComunales > 0 && saldoVencidoComunal > 0) {
                            if (saldoVencidoComunal < 500 && saldoVencidoComunal == saldoActualComunal && saldoActualComunal == saldoApagarcomunal) {
                                calificacion = ClientesConstants.CALIFICACION_CIRCULO_REGULAR;
                            } else if (saldoVencidoComunal >= 500 || score < 550) {
                                calificacion = ClientesConstants.CALIFICACION_CIRCULO_MALA;
                            }
                        } else if (creditosComunales == 0 && saldoVencidoOtras > 0) {
                            calificacion = ClientesConstants.CALIFICACION_CIRCULO_MALA;
                        } else if (score >= 600 && saldoVencidoComunal == 0 && saldoVencidoOtras == 0 && saldoVencidoComunicaciones < 1500 && creditosComunalesConSaldo <= 3) {
                            calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                        } else {
                            calificacion = ClientesConstants.CALIFICACION_CIRCULO_REGULAR;
                        }
                    }
                }
            }
            myLogger.debug("La calificacion resultante es: " + calificacion);
        } catch (Exception e) {
            myLogger.error("getCalificacionCirculoMovil", e);
        }

        return calificacion;

    }

}

package com.sicap.clientes.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.SHFHelper;
import org.apache.log4j.Logger;

public class TimerUtil extends TimerTask implements ServletContextListener {

    private static Logger myLogger = Logger.getLogger(TimerUtil.class);
    private Timer timer;

    public void contextInitialized(ServletContextEvent evt) {

        // Iniciamos el timer
        timer = new Timer();
        //El timer se ejecuta cada : 30 minutos
        timer.schedule(this, 0, 10 * 60 * 1000);

    }

    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }

    public void run() {

        //Logger.debug("Evaluando generaci�n de archivos Buro C�rculo : "+new Date().toString());
        String startTime = CatalogoHelper.getParametro("HORA_GENERA_ARCHIVOS_BUROCIRCULO");
        int dayRunCirculo = Integer.parseInt(CatalogoHelper.getParametro("DIA_GENERA_ARCHIVO_CIRCULO"));
        Calendar cal = Calendar.getInstance();
        myLogger.info("Ejecutando TimerUtil : " + new Date().toString());
        try {
            /*  Se retira hasta decidir si el cierre se hara autom�ticamente JBL JUN/10 
             *   		if ( cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY ){
             String horaProcesoCierre = CatalogoHelper.getParametro("HORA_EJECUCION_FIN_DIA");
             CierreUtil util = new CierreUtil();

             Logger.debug("Verificando fecha de ultimo cierre...");
             if( util.validaUltimaFechaEjecucion() ){
             Logger.debug("Verificando hora para proceso de Cierre...");
             if ( horaProcesoCierre!=null && Integer.parseInt(horaProcesoCierre)<=cal.get(Calendar.HOUR_OF_DAY) ) {
             Logger.debug("Inicia proceso Cierre...");
             util.Cierre();
             Logger.debug("Proceso cierre terminando...");
             }else{
             Logger.debug("Hora no adecuada para inicio de proceso Cierre");
             }
             }
             else{
             Logger.debug("El proceso Cierre ya se ejcuto el dia de hoy");
             }
             }else{
             Logger.debug("No se realiza ninguna accion por ser domingo");
             }
             */
            /*    		if ( cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY ){
             String horaProcesoInffinix = CatalogoHelper.getParametro("HORA_INICIO_INFFINIX");
             InffinixUtil util = new InffinixUtil();

             Logger.debug("Verificando fecha de ultima carga Inffinix...");
             if( util.comparaFechasInffinix() ){
             Logger.debug("Verificando hora para proceso Inffinix...");
             if ( horaProcesoInffinix!=null && Integer.parseInt(horaProcesoInffinix)<=cal.get(Calendar.HOUR_OF_DAY) ) {
             Logger.debug("Inicia proceso Inffinix...");
             util.procesoEnvioArchivosInffinix();
             Logger.debug("Proceso Inffinix terminado...");
             }else{
             Logger.debug("Hora no adecuada para inicio de proceso inffinix");
             }
             }
             else{
             Logger.debug("El proceso Inffinix ya se ejcuto el dia de hoy");
             }
             }else{
             Logger.debug("No se realiza ninguna accion por ser domingo");
             }
             */
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                GrupoUtil util = new GrupoUtil();
                String horaProcesoMonitorPagos = CatalogoHelper.getParametro("HORA_INICIO_MONITOR_PAGOS_GRUPAL");
                if (util.comparaFechasMonitor()) {
                    if (horaProcesoMonitorPagos != null && Integer.parseInt(horaProcesoMonitorPagos) <= cal.get(Calendar.HOUR_OF_DAY)) {
                        myLogger.info("Inicia proceso monitor pagos grupal...");
                        util.procesoMonitorPagosGrupos();
                        util.procesoRecalificacionGrupos();
                        SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                        Date fechaHoy = new Date();
                        CatalogoHelper.updateParametro("FECHA_EJECUCION_MONITOR_PAGOS_GRUPAL", format.format(fechaHoy));
                    } else {
                        myLogger.info("Hora no adecuada para inicio de proceso monitor pagos grupal");
                    }
                } else {
                    myLogger.info("El proceso monitor de pagos grupal ya se jecuto el dia de hoy");
                }
            } else {
                myLogger.info("No se inicia monitor de pagos es Domingo!!!");
            }

            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                myLogger.info("Verificando carga saldos IBS...");
                String horaProcesoCargaSaldosIBS = CatalogoHelper.getParametro("HORA_INICIO_CARGA_SALDOS");
                if (horaProcesoCargaSaldosIBS != null && Integer.parseInt(horaProcesoCargaSaldosIBS) == cal.get(Calendar.HOUR_OF_DAY)) {
                    IBSUtil ibsUtil = new IBSUtil();
                    if (ibsUtil.validaUltimaFechaEjecucion()) {
                        myLogger.info("Inicia proceso de Carga Saldos IBS...");
                        ibsUtil.procesaArchivoSaldosIBS();
                    }
                } else {
                    myLogger.info("Hora no adecuada cargar el Archivo de Saldos IBS");
                }
            } else {
                myLogger.info("No se inicia la carga de Saldos es Domingo!!!");
            }
            /**
             * Procesando Hist�ricos SHF
             */

            if (SHFHelper.esDiaDeCarga()) {
                myLogger.info(":::: Es dia de Carga adecuado :::: ");
                String horaCargaHistoricoSHF = CatalogoHelper.getParametro("HORA_INICIO_CARGA_HISTORICO_SHF");
                if (horaCargaHistoricoSHF != null && Integer.parseInt(horaCargaHistoricoSHF) == cal.get(Calendar.HOUR_OF_DAY)) {
                    SHFHelper shfHelper = new SHFHelper();
                    if (shfHelper.validaUltimaFechaEjecucion()) {
                        myLogger.info(":::: Inicia proceso de Carga Hist�ricos SHF :::: ");
                        shfHelper.cargaSaldosHistoricosSHF();
                    } else {
                        myLogger.info(":::: El proceso fue ya fue ejecutado :::: ");
                    }
                } else {
                    myLogger.info(":::: Hora no adecuada para procesar los Hist�ricos SHF :::: ");
                }
            } else {
                myLogger.info(":::: No es dia Habil para la carga de Hist�ricos SHF :::: ");
            }

            /*
             //Eval�a si ejecuta proceso de generaci�n de archivo de c�rculo de cr�dito
             if ( startTime!=null && Integer.parseInt(startTime) == cal.get(Calendar.HOUR_OF_DAY) ){

             if ( cal.get(Calendar.DAY_OF_MONTH) == dayRunCirculo || esFinDeMes() ){
             Logger.debug("Ejecutando Generaci�n de archivo para C�rculo : "+new Date().toString());
             GeneraArchivoCirculoHelper.generaArchivo();
             }

             //Eval�a si ejecuta proceso de generaci�n de archivo de bur� de cr�dito
             //	    	if ( esFinDeMes() ){
             //	        	Logger.debug("Ejecutando Generaci�n de archivo para Buro : "+new Date().toString());
             //	    		GeneraArchivoBuroHelper.generaArchivo();
             //	    	}
             }

             */
        } catch (Exception e) {
            myLogger.error("Exception ",e);
        } finally {
            myLogger.info("Termina ejecucion de TimerUtil a las : " + new Date().toString());
        }
    }

    private boolean esFinDeMes() {
        Calendar cal = GregorianCalendar.getInstance();
        int dia = cal.get(GregorianCalendar.DATE);
        int diaFinalMes = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        if ((diaFinalMes == dia)) {
            return true;
        } else {
            return false;
        }
    }

}

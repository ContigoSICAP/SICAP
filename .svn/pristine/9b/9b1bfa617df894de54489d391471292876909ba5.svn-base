package com.sicap.clientes.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GeneraArchivoBuroHelper;
import com.sicap.clientes.helpers.GeneraArchivoCirculoHelper;
import org.apache.log4j.Logger;

public class BuroCirculoTimerUtil extends TimerTask implements ServletContextListener {

    private Timer timer;
    private static Logger myLogger = Logger.getLogger(BuroCirculoTimerUtil.class);

    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        long period = 1000 * 60 * 60 * 24;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.MAY, 4, 14, 30, 0);
        Date startTime = calendar.getTime();

        //El timer se ejecuta cada dia
        //timer.schedule(this, startTime, period);
        timer.schedule(this, 0, 60 * 60 * 1000);

    }

    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }

    public void run() {
        Calendar cal = Calendar.getInstance();
        
        try {

            //Logger.debug("Evaluando generaci�n de archivos Buro C�rculo : "+new Date().toString());
            String startTime = CatalogoHelper.getParametro("HORA_GENERA_ARCHIVOS_BUROCIRCULO");
            int dayRunCirculo = Integer.parseInt(CatalogoHelper.getParametro("DIA_GENERA_ARCHIVO_CIRCULO"));

            //Eval�a si ejecuta proceso de generaci�n de archivo de c�rculo de cr�dito
            if (startTime != null && Integer.parseInt(startTime) == cal.get(Calendar.HOUR_OF_DAY)) {
                
                if (cal.get(Calendar.DAY_OF_MONTH) == dayRunCirculo || esFinDeMes()) {
                    myLogger.info("Ejecutando Generaci�n de archivo para C�rculo : " + new Date().toString());
                    GeneraArchivoCirculoHelper.generaArchivo();
                }

                //Eval�a si ejecuta proceso de generaci�n de archivo de bur� de cr�dito
                if (esFinDeMes()) {
                    myLogger.info("Ejecutando Generaci�n de archivo para Buro : " + new Date().toString());
                    GeneraArchivoBuroHelper.generaArchivo();
                }
            }

        } catch (Exception e) {
            myLogger.error("Exception en generacion de archivo para Buro y circulo: ",e);
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

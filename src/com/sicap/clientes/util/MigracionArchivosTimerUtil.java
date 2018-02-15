package com.sicap.clientes.util;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.MigracionArchivosHelper;

/**
 *
 * @author LDAVILA
 */
public class MigracionArchivosTimerUtil extends TimerTask implements ServletContextListener {

    private Timer timer;
    private static Logger myLogger = Logger.getLogger(MigracionArchivosTimerUtil.class);

    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        //El timer se ejecuta cada :  1/2hora
        timer.schedule(this, 0, 30 * 60 * 1000);
        //El timer se ejecuta cada :  minuto
        //timer.schedule(this, 0,60 * 1000);//solo pruebas

    }

    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }

    public void run() {

        Calendar cal = Calendar.getInstance();
        try {
            String startTime = CatalogoHelper.getParametro("HORA_VALIDA_MIGRACION_ARCHIVOS");
            if (startTime != null && Integer.parseInt(startTime) == cal.get(Calendar.HOUR_OF_DAY)) {
                myLogger.info("Inicia Migracion de Archivos", null);
                MigracionArchivosHelper.migraArchivos();
            }
        } catch (Exception e) {
            myLogger.error("Exception ", e);
        }

    }

}

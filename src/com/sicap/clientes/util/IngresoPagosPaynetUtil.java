package com.sicap.clientes.util;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.IngresosPagosPaynetHelper;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class IngresoPagosPaynetUtil extends TimerTask implements ServletContextListener{
    
    private Timer timer;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(BuroCirculoTimerUtil.class);

    public void contextInitialized(ServletContextEvent evt) {
// Iniciamos el timer
        timer = new Timer();
        //Timer se ejecuta cada segundos
        //timer.schedule(this, 0, 1000);
        //Timer se ejecuta cada minuto
        //timer.schedule(this, 0, 60 * 1000);
        //Timer se ejecuta cada hora
        timer.schedule(this, 0, 60 * 60 * 1000);
    }
    
    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }
    
    public void run() {
        
        Calendar cal = Calendar.getInstance();
        try {
            String startTime = CatalogoHelper.getParametro("HORA_GENERA_PAGOS_PAYNET");
            if (startTime != null && Integer.parseInt(startTime) == cal.get(Calendar.HOUR_OF_DAY)){
                myLogger.debug("IngresoPagosPaynetUtil run");
                IngresosPagosPaynetHelper.traspasoTransPaynetPagos();
            }else
                myLogger.debug("Hora Erronea para pagos paynet");
        } catch (Exception e) {
            myLogger.error("Exception en generacion de pagos Paynet: ",e);
        }
    }
    
}

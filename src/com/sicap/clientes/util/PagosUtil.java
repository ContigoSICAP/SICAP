package com.sicap.clientes.util;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author avillanueva
 */
public class PagosUtil extends TimerTask implements ServletContextListener{
    
    private Timer timer;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(PagosUtil.class);
    
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
            String startTime = CatalogoHelper.getParametro("HORA_VALIDA_PAGOS");
            String strFechaPagos = CatalogoHelper.getParametro("FECHA_VALIDA_PAGOS");
            String strFechaCierre = CatalogoHelper.getParametro("FECHA_CIERRE");
            if (startTime != null && Integer.parseInt(startTime) == cal.get(Calendar.HOUR_OF_DAY) && !strFechaCierre.equals(strFechaPagos)){
                myLogger.debug("PagosUtil (Fichas Completas) run");
                PagosReferenciadosHelper.validaFichasCompletasATiempo(strFechaPagos);
                myLogger.debug("PagosUtil (Fichas Completas) close");
            }else
                myLogger.debug("Hora Erronea para Fichas Completas)");
        } catch (Exception e) {
            myLogger.error("Exception en generacion de Fichas Completas: ",e);
        }
    }
    
}

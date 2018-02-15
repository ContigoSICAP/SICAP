/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.sicap.clientes.dao.AmortPagareDAO;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

import com.sicap.clientes.vo.AmortizacionPagareVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ERojano
 */
public class VencimientoPagaresTimerUtil extends TimerTask implements ServletContextListener {

    private Timer timer;
    private static Logger myLogger = Logger.getLogger(VencimientoPagaresTimerUtil.class);

    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        //El timer se ejecuta diariamente
        timer.schedule(this, 0, 24 * 60 * 60 * 1000);
//        Para pruebas
//        timer.schedule(this, 0, 300 * 1000);

    }

    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }

    public void run() {
        try {
            myLogger.info("Inicia Verificacion de pagares por vencer y vencidos....");
            AmortPagareDAO amortPagareDao = new AmortPagareDAO();
            
            Calendar fecHoy = Calendar.getInstance();
            Calendar fec5diasAnticipacion = Calendar.getInstance();
            fec5diasAnticipacion.add(Calendar.DAY_OF_MONTH, 5);
            
            Calendar fec2diasAnticipacion = Calendar.getInstance();
            fec2diasAnticipacion.add(Calendar.DAY_OF_MONTH, 2);
            
            Calendar fec2diasVencidos = Calendar.getInstance();
            fec2diasVencidos.add(Calendar.DAY_OF_MONTH, -2);
            
            List<Date> fechasQuery = new ArrayList<Date>();
            fechasQuery.add(fecHoy.getTime());
            fechasQuery.add(fec5diasAnticipacion.getTime());
            fechasQuery.add(fec2diasAnticipacion.getTime());
            fechasQuery.add(fec2diasVencidos.getTime());
            
            List<AmortizacionPagareVO> pagaresVencidos = amortPagareDao.getAmortActivas(ClientesConstants.ACTIVO, fechasQuery);

            myLogger.info("Se obtuvieron: "+pagaresVencidos.size()+" pagares vencidos o por vencer");
            
            if(!pagaresVencidos.isEmpty()){
                FondeadorUtil.enviaCorreoPagareVencido(pagaresVencidos);
            }
            
        } catch (Exception e) {
            myLogger.error("Exception ", e);
        }

    }

}

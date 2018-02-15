/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.sicap.clientes.dao.bsc.MigracionInfoDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Alex
 */
public class ReportesBSCUtil extends TimerTask implements ServletContextListener{
    
    private Timer timer;
    
    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        //El timer se ejecuta cada dia
        //timer.schedule(this, startTime, period);
        //timer.schedule(this, 0, 60*60*1000);
        timer.schedule(this, 0, 5 * 60 * 1000);
    
    }
    
    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }
    
    public void run() {
        Date fecha = new Date();
        System.out.println("Ejeccion de Migfracion de Datos BSC "+fecha);
        MigracionInfoDAO exeMigracion = new MigracionInfoDAO();
        try {
            //exeMigracion.MigracionDatosBSC();
        } catch (Exception e) {
            System.out.println("Error al inicio de programa migracion");
            System.out.println(e);
        }
    }
}

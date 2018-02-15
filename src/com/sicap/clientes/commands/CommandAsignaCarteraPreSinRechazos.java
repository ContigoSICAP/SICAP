/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.Notification;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author rsolis
 */
public class CommandAsignaCarteraPreSinRechazos implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandAsignaCarteraPreSinRechazos.class);
    
    public CommandAsignaCarteraPreSinRechazos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        myLogger.info("EXECUTE DE CommandAsignaCarteraPreSinRechazos");
        
        int idFondeadorPre=0;
        
        //Validar que no este corriendo
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        boolean procesandoCierreFond = false;
        boolean procesandoCierreDia = false;
        Vector<Notification> notificaciones = new Vector<Notification>();
        Connection con = null;
        
        try {
            idFondeadorPre=HTMLHelper.getParameterInt(request, "idFondeadorCombo");
            
            //Ejecucion de procesos de cierre al momento
            procesandoCierreFond = catalogoDAO.ejecutandoCierreFondeadores();
            procesandoCierreDia = catalogoDAO.ejecutandoCierre();
            myLogger.info("PROCESANDO: " + procesandoCierreFond);
           
            
            //Que el fondeador tenga al menos un dato preseleccionada aun sin asigna
            boolean hasPreseleccionados;
            hasPreseleccionados=saldoDAO.existeCartera((-1)*idFondeadorPre);
            
            
            if(procesandoCierreFond){
                myLogger.debug("El cierre de fondeadores se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de fondeadores se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;
            }else if(procesandoCierreDia){
                myLogger.debug("El cierre de dia se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de dia se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;            
            }else if(!hasPreseleccionados){
                myLogger.debug("El fondeador seleccionado NO tiene creditos preselecciondos");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El fondeador seleccionado NO tiene creditos preselecciondos"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;   
            }else{
               
               //Mandar conexion desde aqui
               con=ConnectionManager.getMySQLConnection();
               con.setAutoCommit(false);
               
               //Llamar metodo para asignar preseleccionados
               saldoDAO.updateSaldosPreseleccionadosAAsigandos(idFondeadorPre, con);//TODO Botonazo si se declara que no rechazados.
               
               con.commit();
               notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Asiganción de cartera preseleccionada ejecutada con éxito"));
            }
            
            request.setAttribute("NOTIFICACIONES", notificaciones);
            
        }catch (SQLException sqlEx) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                myLogger.error(ex.getCause());
            }
        }catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommandPreseleccionaCartera.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //Cerrar conexion
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandPreseleccionaCartera.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } 
        
        return siguiente;
    }
    
}

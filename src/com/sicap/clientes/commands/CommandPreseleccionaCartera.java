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
 * @author Ramsses
 */
public class CommandPreseleccionaCartera implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandPreseleccionaCartera.class);

    public CommandPreseleccionaCartera(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        myLogger.info("EXECUTE DE CommandPreseleccionaCartera");
        
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
            
            
            
            //Traer fecha ultima ejejcucion completa
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            String sFechaUltimaEjecucion = CatalogoHelper.getParametroFondeador("FECHA_CIERRE_FONDEADOR",idFondeadorPre);
            Date fechaHoy = new Date();    
            String sFechaHoy = sdf.format(fechaHoy);
            
            myLogger.info("Ultima Ejecucion: "+ sFechaUltimaEjecucion);
            myLogger.info("Hoy: "+ sFechaHoy);
            boolean datesEquals = sFechaHoy.equals(sFechaUltimaEjecucion);
            myLogger.info("Equals: "+ datesEquals);
            
            //Que el fondeador tenga al menos un dato preseleccionada aun sin asigna
            boolean hasPreseleccionados;
            hasPreseleccionados=saldoDAO.existeCartera((-1)*idFondeadorPre);
            
            
            if(datesEquals ){
                myLogger.info("Ya se ha ejecutado el proceso para el FONDEADOR "+idFondeadorPre+" para la fecha de hoy");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Ya se ha ejecutado el proceso para el FONDEADOR "+idFondeadorPre+" para la fecha de hoy"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;
            }else if(procesandoCierreFond){
                myLogger.debug("El cierre de fondeadores se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de fondeadores se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;
            }else if(procesandoCierreDia){
                myLogger.debug("El cierre de dia se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de dia se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;            
            }else if(hasPreseleccionados){
                myLogger.debug("El fondeador seleccionado aún tiene creditos preselecciondos sin respuesta");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El fondeador seleccionado aun tiene creditos preselecciondos sin respuesta"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;   
            }else{
               
               //Mandar conexion desde aqui
               con=ConnectionManager.getMySQLConnection();
               con.setAutoCommit(false);
               
               //Llamar metodo pa preseleccionar
               
               switch(idFondeadorPre){
                   /* 
                   case ClientesConstants.ID_FONDEADOR_BURSA:
                        myLogger.info("----------------------------------------------------FONDEADOR "+idFondeadorPre+" ----------------------------------------------------");
                        FondeadorUtil.preSelCarteraBursa(con);
                        con.commit();
                        break;

                    case ClientesConstants.ID_FONDEADOR_ABC:
                        myLogger.info("----------------------------------------------------FONDEADOR "+idFondeadorPre+" ----------------------------------------------------");
                        FondeadorUtil.preSelCarteraABC(con);
                        con.commit();
                        break;
                    */
                    //Bajio se excluye mientras hay respues de Edgar
                    case ClientesConstants.ID_FONDEADOR_BAJIO:
                    //case ClientesConstants.ID_FONDEADOR_BXMAS:
                    //case ClientesConstants.ID_FONDEADOR_ACTINVER:
                        myLogger.info("----------------------------------------------------FONDEADOR "+idFondeadorPre+" ----------------------------------------------------");
                        FondeadorUtil.cierreFondeadorGenerico(idFondeadorPre,true, con);
                        con.commit();
                        break;

               } 
            
               notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Generacion terminada"));
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

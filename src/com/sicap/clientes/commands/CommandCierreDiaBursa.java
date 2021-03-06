/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.afirme.commons.model.dao.helpers.CatalogDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CatalogoVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramsses
 */
public class CommandCierreDiaBursa implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCierreDiaBursa.class);

    public CommandCierreDiaBursa(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest req) throws CommandException {
        myLogger.info("EXECUTE DE CommandCierreDiaBursa");
        if(siguiente != null || true){
            return null;
        }
        
        //TODO Hacer metodo en FondeadorUtil
        /**
         * Para esto se utilizara de la tabla parametros de manera similar a :
         * SALDO FONDEADOR 1, SALDO FONDEADOR 2 Y SALDO FONDEADOR 3
         * pero constante
         * SALDO BURSA que seria el saldo de credito que se tiene que tener en garantía.
         * A partir de la revision del saldo asignar cartera.
         */
        
        //Validar que no este corriendo
        CatalogoDAO catalogoDAO = new CatalogoDAO(); 
        boolean procesandoCierreFond = false;
        
        Connection con = null;
        Connection conCart = null;
        try {
            Vector<Notification> notificaciones = new Vector<Notification>();
            procesandoCierreFond = catalogoDAO.ejecutandoCierreFondeadores();
            myLogger.info("PROCESANDO: " + procesandoCierreFond);
            
            
            //Cierre se valida en fron con la habilitacion o no del Boton
            if(procesandoCierreFond){
                myLogger.debug("El cierre de fondeadores se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "El cierre de fondeadores se encuentra en proceso"));
                
            }else{
            
                //Setear bandera de cierre fondeadores en ejecucion a 1=Ejecutandose
                catalogoDAO.updateBanderaCierreFondeadores(1);
                
                //Mandar conexion desde aqui
                con=ConnectionManager.getMySQLConnection();
                conCart = ConnectionManager.getCWConnection();//Para bursa 
                //myLogger.info(con.getCatalog());

                con.setAutoCommit(false);
                conCart.setAutoCommit(false);
                //traer lista de fondeadores a correr procesos
                CatalogoDAO catDao = new CatalogoDAO();
                List<CatalogoVO> listaFondeadores = catDao.getCatalogoFondeadoresByPrioridadGreaterThan(0, false, con);
                myLogger.info("Size fondeadores: "+listaFondeadores.size());
                for(CatalogoVO fondeador: listaFondeadores){
                    myLogger.info("id: "+fondeador.getId()+" nombre: "+fondeador.getDescripcion());
                    int idFondeador=fondeador.getId();
                    switch(idFondeador){
                        case ClientesConstants.ID_FONDEADOR_BURSA:
                            myLogger.info("----------------------------------------------------FONDEADOR "+idFondeador+" ----------------------------------------------------");
                            FondeadorUtil.cierreBursa(con,conCart);
                            con.commit();
                            conCart.commit();
                            break;
                        
                        case ClientesConstants.ID_FONDEADOR_ABC:
                            myLogger.info("----------------------------------------------------FONDEADOR "+idFondeador+" ----------------------------------------------------");
                            FondeadorUtil.cierreABC(con);
                            con.commit();
                            conCart.commit();
                            break;
                        //Bajio se excluye mientras hay respues de Edgar
                        //case ClientesConstants.ID_FONDEADOR_BAJIO:
                        case ClientesConstants.ID_FONDEADOR_BXMAS:
                        case ClientesConstants.ID_FONDEADOR_ACTINVER:
                            myLogger.info("----------------------------------------------------FONDEADOR "+idFondeador+" ----------------------------------------------------");
                            FondeadorUtil.cierreFondeadorGenerico(idFondeador,false, con);
                            con.commit();
                            conCart.commit();
                            break;
                        

                    } 
                }
                
                //Setear bandera de cierre fondeadores en ejecucion a 0= NO Ejecutandose
                catalogoDAO.updateBanderaCierreFondeadores(0);
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de fondeadores fue ejecutado exitosamente"));
            
            }
            myLogger.info("Termina Proceso cierre de fondeadores...");
            req.setAttribute("NOTIFICACIONES", notificaciones);
            
        } catch (SQLException sqlEx) {
            try {
                con.rollback();
                conCart.rollback();
            } catch (SQLException ex) {
                myLogger.error(ex.getCause());
            }
        } catch (NamingException nEx) {
            myLogger.error(nEx.getCause());            
        } catch (ClientesException ex) {
            java.util.logging.Logger.getLogger(CommandCierreDiaBursa.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //Cerrar conexion
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandCierreDiaBursa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conCart != null) {
                try {
                    conCart.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandCierreDiaBursa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        return siguiente;
    }
    
    
}

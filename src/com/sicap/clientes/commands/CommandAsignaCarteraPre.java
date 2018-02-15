/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Request;
import com.sicap.clientes.dao.BitacoraSalidaCarteraDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ArchivosAsociadosHelper;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SaldosHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramsses
 */
public class CommandAsignaCarteraPre{
    
       private static Logger myLogger = Logger.getLogger(CommandAsignaCarteraPre.class);


    public void procesaArchivo(File myFile, HttpServletRequest request,Request requestUpload) {
        
        myLogger.info("EXECUTE DE CommandAsignaCarteraPre");
        
        int idFondeadorPre=0;
        
        //Validar que no este corriendo
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        boolean procesandoCierreFond = false;
        boolean procesandoCierreDia = false;
        Vector<Notification> notificaciones = new Vector<Notification>();
        Connection con = null;
        
        try {
            idFondeadorPre=HTMLHelper.getParameterInt(requestUpload, "idFondeadorCombo");
            myLogger.info("IDFONDEADOR: "+idFondeadorPre);
            
            //Ejecucion de procesos de cierre al momento
            procesandoCierreFond = catalogoDAO.ejecutandoCierreFondeadores();
            procesandoCierreDia = catalogoDAO.ejecutandoCierre();
            
            //Que el fondeador tenga al menos un dato preseleccionada aun sin asigna
            boolean hasPreseleccionados;
            hasPreseleccionados=saldoDAO.existeCartera((-1)*idFondeadorPre);
            
            
            
            if(procesandoCierreFond){
                myLogger.debug("El cierre de fondeadores se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "El cierre de fondeadores se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return;
            }else if(procesandoCierreDia){
                myLogger.debug("El cierre de dia se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "El cierre de dia se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return ;            
            }else if(!hasPreseleccionados){
                myLogger.debug("El fondeador seleccionado NO tiene creditos preselecciondos");
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "El fondeador seleccionado NO tiene creditos preselecciondos"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return ;   
            }
            
            
            if (!myFile.isMissing()) {
                myLogger.info("Existe el archivo");
                myLogger.info("myFile.getSize() " + myFile.getSize());
                if (myFile.getSize() > 0) {
                    myLogger.info("Contiene Datos");
                    myLogger.info(myFile.getFileName());
                    myLogger.info(myFile.getFieldName());
                    
                    //Se valida que el nombre contenga RECHAZADOS_,
                    //que coinicidinaID del combor como nombre del fondeador en archivo
                    //Que la extension del archivo seavalida para este proceso
                    if(validaArchivoRechazados(myFile,idFondeadorPre,notificaciones)){
                        myLogger.info("SE VA A PROCESAR ARCHIVO ");
                        
                        //Procesar archivo para parsear y traer id de saldos  rechazados
                        ArrayList<SaldoIBSVO> saldosRechazados= null;
                        if (idFondeadorPre == ClientesConstants.ID_FONDEADOR_BAJIO) {
                            saldosRechazados = procesaArchivoBajio(myFile);
                            
                            FondeadorUtil.displayListaSaldos(saldosRechazados);
                            
                        }
                        
                        
                        
                        //Validar que esten asociados al fondeador dado
                        boolean saldosAsociadosOK=true;
                        for(SaldoIBSVO saldoRechz:saldosRechazados){
                            
                            if(!saldoDAO.tienePreseleccion(saldoRechz, idFondeadorPre)){
                                myLogger.info("Sin preseleccion, break for y setear bandera de error en informacion detexto");
                                saldosAsociadosOK=false;
                                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "Los saldos asociados en el archivo no coinciden en su totalidad con los saldos preseleccionados"));
                                break;
                            }else{
                                myLogger.info("Con preseleccion :"+saldoRechz.getIdClienteSICAP()+"-"+saldoRechz.getIdSolicitudSICAP());
                            }
                            
                        }
                        
                        if(saldosAsociadosOK){
                            //Actulizar base de datos
                            //Mandar conexion desde aqui
                            con=ConnectionManager.getMySQLConnection();
                            con.setAutoCommit(false);
                            
                            
                            //Si estan TODOS ASOCIADOS a la preseleccion actualizarlos como Rechazados uno por uno
                            saldoDAO.updateSaldosRechazoFondeador(saldosRechazados, idFondeadorPre, con);
                            
                            //Guardar en bitácora todos los rechazados
                            saldoDAO.updateSaldosPreseleccionadosAAsigandos(idFondeadorPre, con);

                            //Depues actulizar todos los restantes como asignados cambiar el signo
                            BitacoraSalidaCarteraDAO bitacoraSalidaCarteraDAO = new BitacoraSalidaCarteraDAO();
                            bitacoraSalidaCarteraDAO
                                    .addBitacoraSalidaCartera(saldosRechazados, -1*idFondeadorPre, 0, ClientesConstants.ESTATUS_FONDEADOR_CARTERA_RECHAZADA, request.getRemoteUser(), con);
                            
                            
                            con.commit();
                            notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Archivo procesado con existosamente"));
                                
                        
                        }
                        
                    
                    }
                    
                    
                    

                }
            
            }
            
            
            request.setAttribute("NOTIFICACIONES", notificaciones);
            
        }catch (SQLException sqlEx) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                myLogger.error(ex.getCause());
            }
        }catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommandAsignaCarteraPre.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //Cerrar conexion
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandAsignaCarteraPre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
       
    }
    
    public boolean validaArchivoRechazados(File archivo, int idFondeadorPre,  Vector<Notification> notificaciones){
        boolean esValido= false;
        if(archivo != null){
            
            //Formato denombre RECHAZADOS_NOMBREFONDEADOR_29042017.txt
            String fileName = archivo.getFileName();

            if(fileName.substring(0, 11).equals("RECHAZADOS_")){
                myLogger.info("RECHAZADOS_ true");
                
                String fileNameFondeadorSplit [] = fileName.split("_");
                String nameFondeador = fileNameFondeadorSplit[1];
                myLogger.info("tipo fondeador: "+nameFondeador);
                
                //Validar que el id asociado coincida con el nombre del archivo
                boolean idNameValido=FondeadorUtil.validaNombrebyId(nameFondeador, idFondeadorPre);
                if(idNameValido){
                    
                    myLogger.info("Id vs Name valido? "+idNameValido);
                    //Se valida el la extension de archivo.
                    if (ArchivosAsociadosHelper.esFormatoValido(archivo)) {
                        myLogger.info("Tiene Formato Válido de tipo de archivo (ext) ");

                        esValido=true;

                    }else{
                        myLogger.info("La extensión no es válida. ");
                        notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "ERROR: La extensión no es válida. "));
                    }
                }else{
                    myLogger.info("El nombre del fondeador en el nombre de archivo no coincide con el seleccionado");
                    notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "ERROR: No coincide nombre fondeador"));
                }

            }else{
                myLogger.info("El nombre de archivo no es valido, no comienza con RECHAZADOS_");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "ERROR: No comienza con RECHAZADOS_ el nombre de Archivo"));
            }
        
        }
        
        
        
        return esValido;
        
    }
    
    
    public ArrayList<SaldoIBSVO> procesaArchivoBajio(File archivo){
    
        ArrayList<SaldoIBSVO> lstaSaldos= null;
        
        String lines[] = archivo.getContentString().split("\n");
        //int idx=0;
        myLogger.info("Content: ");
        try {
            if(lines.length>0){
                lstaSaldos = new ArrayList<SaldoIBSVO>();
            }
            for (String line : lines) {
                String cols [] = line.split(";");
                SaldoIBSVO saldoCurrent= new SaldoIBSVO();

                saldoCurrent.setIdClienteSICAP(Convertidor.stringToInt(cols[0]));
                saldoCurrent.setIdSolicitudSICAP(Convertidor.stringToInt(cols[1]));
                lstaSaldos.add(saldoCurrent);
                //myLogger.info(line);
            }
        } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CommandAsignaCarteraPre.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return lstaSaldos;
        
    
    }
}

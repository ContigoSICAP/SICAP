/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.jcraft.jsch.ChannelSftp;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.MailUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SFTPUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author ERojano
 */
public class CommandInterfacesBursa implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandInterfacesBursa.class);
    private static final String TRANSACCIONES = "transacciones";
    private static final String SINCRONIZACION = "sincronizacion";
    private static final String DEUDORES = "deudores";
    private static final String OPERACIONES = "operaciones";
    private static final String PAGOS = "pagos";
    private static final String FLUJOS = "flujos";
    private static final String FORMATO_TXT = ".txt";
    
    public CommandInterfacesBursa(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest req) throws CommandException {
        myLogger.info("Ejecutando Command Generar Interfaces Bursa");
        Vector<Notification> notificaciones = new Vector<Notification>();
        
        try {
            
            Date fechaRequest = HTMLHelper.getParameterDate(req, "fechaInicio");
            
            generarInterfacesBursa(fechaRequest);
            
            notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Se ha ejecutado la generación de interfaces de Bursa correctamente"));
            
            myLogger.info("Se mando a ejecutar Generacion de interfaces bursa...");
            req.setAttribute("NOTIFICACIONES", notificaciones);
            
        } catch (Exception nEx) {
            myLogger.error(nEx.getCause());            
        }
        
        return siguiente;
    }
    
    public void generarInterfacesBursa(Date fecha){
        try {
            String s = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatter.format(fecha);
            
            String rutaJar = CatalogoHelper.getParametro("RUTA_JAR_INTERFACES_BURSA");
            
            Process p = Runtime.getRuntime().exec("java -jar \""+rutaJar+"\" "+fechaString);
            
            BufferedReader stdInput = new BufferedReader(new
                     InputStreamReader(p.getInputStream()));
            
            BufferedReader stdError = new BufferedReader(new
                     InputStreamReader(p.getErrorStream()));
            
            // read the output from the command
            myLogger.info("Salida estandar de la ejecución de la generación de interfaces bursa:");
            while ((s = stdInput.readLine()) != null) {
                myLogger.info("output: "+s);
            }
            // read any errors from the attempted command
            myLogger.info("Salida de error de la ejecución de la generación de intefaces bursa:");
            while ((s = stdError.readLine()) != null) {
                myLogger.info("error: "+s);
            }
            
            copiarArchivosBursaSFTP(fechaString);
        } catch (IOException ex) {
            myLogger.error(ex.getCause()); 
        }
    }
    
    
    private void copiarArchivosBursaSFTP(String fechaString){
        
        String fechaArchivos = fechaString.replaceAll("-", "");
        
        String rutaLocal = CatalogoHelper.getParametro("RUTA_ARCHIVOS_INTERFACES_BURSA");
        List<File> archivosExistentes = new ArrayList<File>();
        
        String fileTransacciones = new String(rutaLocal+TRANSACCIONES+"_"+fechaArchivos+FORMATO_TXT);
        String fileSincronizacion = new String(rutaLocal+SINCRONIZACION+"_"+fechaArchivos+FORMATO_TXT);
        String filedeudores = new String(rutaLocal+DEUDORES+"_"+fechaArchivos+FORMATO_TXT);
        String fileOperaciones = new String(rutaLocal+OPERACIONES+"_"+fechaArchivos+FORMATO_TXT);
        String filePagos = new String(rutaLocal+PAGOS+"_"+fechaArchivos+FORMATO_TXT);
        String fileFlujos = new String(rutaLocal+FLUJOS+"_"+fechaArchivos+FORMATO_TXT);
        
        addExistFile(fileTransacciones, archivosExistentes);
        addExistFile(fileSincronizacion, archivosExistentes);
        addExistFile(filedeudores, archivosExistentes);
        addExistFile(fileOperaciones, archivosExistentes);
        addExistFile(filePagos, archivosExistentes);
        addExistFile(fileFlujos, archivosExistentes);
        
        myLogger.info("Archivos Existentes: "+archivosExistentes.size());
        
        if(!archivosExistentes.isEmpty()){
            SFTPUtil sftpUtil = new SFTPUtil();

            int puerto = Integer.parseInt(CatalogoHelper.getParametro("SFTP_PORT_ACFIN"));
            String servidor = CatalogoHelper.getParametro("SFTP_SERVER_ACFIN");
            String usuario = CatalogoHelper.getParametro("SFTP_USER_ACFIN");
            String password = CatalogoHelper.getParametro("SFTP_PASSWORD_ACFIN");
            String rutaDestino = CatalogoHelper.getParametro("SFTP_RUTA_DESTINO_INTERFACES");

            ChannelSftp channelSftp = sftpUtil.conectaSFTP(servidor, puerto, usuario, password);
            myLogger.info("Se conecto al SFTP ACFIN");
            
            String rutaDestinoAbsoluta = sftpUtil.copiarArchivoLocalToSFTP(archivosExistentes, rutaDestino, channelSftp);
            myLogger.info("Archivos copiados a ACFIN...........");
            sftpUtil.cerrarConexiones(channelSftp);
            
            boolean enviado = enviarCorreoNotificacionInterfacesBursa(archivosExistentes, fechaString, rutaDestinoAbsoluta);
            if(enviado){
                myLogger.info("No se ha enviado el correo de interfaces bursa");
            }else{
                myLogger.info("Se ha enviado el correo de interfaces Bursa correctamente.");
            }
        }
        
    }
    
    private void addExistFile(String pathArchivo, List<File> archivosExistentes){
        myLogger.info("Path Archivo: "+pathArchivo);
        File f = new File(pathArchivo);
        if(f.exists() && !f.isDirectory()) { 
            archivosExistentes.add(f);
        }
    }
    
    public boolean enviarCorreoNotificacionInterfacesBursa(List<File> archivos, String fechaCierre, String rutaAbsolutaArchivos){

        String [] fecArray = fechaCierre.split("-");
        String fechaString = fecArray[2]+"/"+fecArray[1]+"/"+fecArray[0];
        
        MailUtil mail = new MailUtil();
        String asunto="Interfaces para ACFIN al "+fechaString;
        StringBuilder msg = new StringBuilder();
        String destinatarios = CatalogoHelper.getParametro("MAIL_DESTINATARIOS_INTERFACES_BURSA");
        
        msg.append("Se han generado las siguientes interfaces para ACFIN:\n");
        
        for(File f : archivos){
            msg.append("\t\u2022 "+f.getName()+"\n");
        }

        msg.append("\nY se han copiado en la ruta: "+rutaAbsolutaArchivos);
                
        return mail.enviaCorreo(asunto, msg.toString(), destinatarios, 1);
    }
}

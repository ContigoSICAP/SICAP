/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author ERojano
 */
public class SFTPUtil {
    
    private static Logger myLogger = Logger.getLogger(SFTPUtil.class);
    
    public ChannelSftp conectaSFTP(String servidor, int puerto, String usuario, String password){
        Session session     = null;
        Channel     channel     = null;
        ChannelSftp channelSftp = null;

        try{
            JSch jsch = new JSch();
            session = jsch.getSession(usuario,servidor,puerto);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp)channel;
        }catch(Exception ex){
            myLogger.error("Error al crear conexion SFTP: "+ex);
        }
        return channelSftp;
    }
    
    public String copiarArchivoLocalToSFTP(List<File> archivos, String rutaDestino, ChannelSftp channelSftp)  {
        String rutaDestinoAbsoluta = null;
        try {
            channelSftp.cd(rutaDestino);
            for(File f : archivos){
                FileInputStream input = new FileInputStream(f);
                channelSftp.put(input, f.getName());
                input.close();
            }
            rutaDestinoAbsoluta = channelSftp.pwd();
        } catch (FileNotFoundException ex) {
            myLogger.error("Error al copiar archivo sftp en una ruta", ex);
        } catch (SftpException ex) {
            myLogger.error("Error al copiar archivo sftp en una ruta", ex);
        } catch (IOException ex) {
            myLogger.error("Error al cerrar inputStream", ex);
        }
        return rutaDestinoAbsoluta;
    }
    
    public void cerrarConexiones(ChannelSftp channelSftp){
        try {
            channelSftp.disconnect();
            channelSftp.getSession().disconnect();
        } catch (JSchException ex) {
            myLogger.error("Error al desconectar SFTP", ex);
        }
    }
}

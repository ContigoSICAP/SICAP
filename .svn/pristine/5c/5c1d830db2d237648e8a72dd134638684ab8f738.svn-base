package com.sicap.clientes.util;

import java.io.FileInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class FtpUtil {

    private static Logger myLogger = Logger.getLogger(FtpUtil.class);

    public FTPClient conectaFTP(String servidor, int puerto, String usuario, String password, FTPClient clienteFtp) {
        clienteFtp = new FTPClient();
        int ReplyCode = 0;
        try {
            clienteFtp.connect(servidor, puerto);
            ReplyCode = clienteFtp.getReplyCode();
            if (FTPReply.isPositiveCompletion(ReplyCode)) {
                if (clienteFtp.login(usuario, password)) {
                    clienteFtp.enterLocalPassiveMode();
                    clienteFtp.setFileType(clienteFtp.BINARY_FILE_TYPE);
                }
                else
                    myLogger.error( "Error en la autentificacion");
            } else {
                clienteFtp.disconnect();
                myLogger.error( "Error en la conexion:" + ReplyCode);
            }
        } catch (Exception e) {
            myLogger.error("conectaFTP", e);
            if (clienteFtp.isConnected()) {
                try {
                    clienteFtp.disconnect();
                } catch (IOException f) {
                    myLogger.error("conectaFTP", f);
                }
            }
        }
        return clienteFtp;
    }

    public String pasaArchivoFTP(FTPClient clienteFtp, String rutaLocal, String nombreArchivo) {
        String mensaje = "";
        InputStream inStrm = null;
        boolean estado = false;
        try {
            inStrm = new FileInputStream(rutaLocal);
            estado = clienteFtp.storeFile(nombreArchivo, inStrm);
            inStrm.close();
            if(!estado)
                mensaje = "No se paso";            
        } catch (Exception e) {
            myLogger.error("pasaArchivoFTP", e);
            mensaje = e + "";
        }
        return mensaje;
    }

}

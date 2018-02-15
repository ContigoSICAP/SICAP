package com.sicap.clientes.helpers;

import org.apache.commons.net.ftp.FTPClient;
import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FtpUtil;
import com.sicap.clientes.util.MailUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.util.ConnectionManager;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

public class PrimeraMigracionHelper {
    
     private static Logger myLogger = Logger.getLogger(PrimeraMigracionHelper.class);
     
     public static void primeraCarga(){
         FTPClient clienteFtp = new FTPClient();
        FtpUtil ftpUtil = new FtpUtil();
        ArrayList<ArchivoAsociadoVO> arrArchivosBO = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoDAO ArchivoAsociadoDAO = new ArchivoAsociadoDAO();
        MailUtil Mail = new MailUtil();
        String respuestaFpt = "";
        String rutaLocalComp = "";
        String rutaDestinoComp = "";
        File fLocal = null;
        File fDestino = null;        
        int CantRevisados = 0;
        FTPFile[] files = null;
        boolean cambioDirFTp = false;
        Connection cn = null;
        try {
            cn = ConnectionManager.getMySQLConnection();
            String Destinatarios = CatalogoHelper.getParametro("MAIL_CIERRE_RECIPIENT");
            int puerto = Integer.parseInt(CatalogoHelper.getParametro("FTP_PORT"));
            String servidor = "201.149.28.187";
            String usuario = "Sicap";
            String password = "Sicap.2015";
            String rutaLocal = "D:\\CLIENTES\\ARCHIVOS_ASOCIADOS";
            String rutaLocalGrupal = CatalogoHelper.getParametro("MIGRACION_RUTA_LOCAL_GRUPAL");
            String rutaDestino = CatalogoHelper.getParametro("MIGRACION_RUTA_DESTINO");
            String rutaDestinoGrupal = CatalogoHelper.getParametro("MIGRACION_RUTA_DESTINO_GRUPAL");
            String rangoFechas = CatalogoHelper.getParametro("RANGO_FECHAS_MIGRACION");
            Mail.enviaCorreo("Inicia Migracion", "Inicia Proceso de migracion de archivos ", Destinatarios);
            ClientesConstants ClientesConstants = new ClientesConstants();
            String respuesta = "";
            String pasoArchivo = "";
            arrArchivosBO = ArchivoAsociadoDAO.getArchivosMigracionFirstLoad(rangoFechas, cn);
            int CantMigrados = 0;

            if (arrArchivosBO.size() > 0) {
                myLogger.info("Archivos a Migrar" + arrArchivosBO.size());
                clienteFtp = ftpUtil.conectaFTP(servidor, puerto, usuario, password, clienteFtp);

                for (ArchivoAsociadoVO arrArchivosBO1 : arrArchivosBO) {
                    //Se crean Rutas local y destino
                    CantRevisados++;
                    int tries = 0;
                    if (!clienteFtp.sendNoOp()){                        
                        while (tries < 3 && !clienteFtp.sendNoOp()) {
                            clienteFtp = ftpUtil.conectaFTP(servidor, puerto, usuario, password, clienteFtp);
                            tries++;
                            myLogger.info("intento para recuperar la conexion intento: " + tries);

                        }
                        if (tries == 3) {
                            respuesta += "No se pudo recuperar la conexion Ftp\n";
                            myLogger.error("No se pudo recuperar la conexion Ftp");
                            break;
                        }
                    }
                    rutaLocalComp = rutaLocal;
                    rutaDestinoComp = rutaDestino;
                    cambioDirFTp = false;
                    if (!arrArchivosBO1.tipoCliente.equals("I")) {
                        rutaLocalComp += rutaLocalGrupal;
                        rutaDestinoComp += rutaDestinoGrupal;
                    }
                    rutaLocalComp += "\\" + arrArchivosBO1.idCliente + "\\" + arrArchivosBO1.idSolicitud + "\\" + arrArchivosBO1.nombre;
                    fLocal = new File(rutaLocalComp);
                    if (fLocal.exists()) {
                        if (clienteFtp.changeWorkingDirectory(rutaDestinoComp)) {
                            rutaDestinoComp += "\\" + arrArchivosBO1.idCliente;
                            cambioDirFTp = clienteFtp.changeWorkingDirectory(rutaDestinoComp);
                            if (!cambioDirFTp) {
                                cambioDirFTp = clienteFtp.makeDirectory(arrArchivosBO1.idCliente + "");
                                if (cambioDirFTp) {
                                    cambioDirFTp = clienteFtp.changeWorkingDirectory(rutaDestinoComp);
                                } else {
                                    respuesta += "problemas al crear el directorio" + rutaDestinoComp + "\n";
                                }
                            }
                            rutaDestinoComp += "\\" + arrArchivosBO1.idSolicitud;
                            cambioDirFTp = clienteFtp.changeWorkingDirectory(rutaDestinoComp);
                            if (!cambioDirFTp) {
                                cambioDirFTp = clienteFtp.makeDirectory(arrArchivosBO1.idSolicitud + "");
                                if (cambioDirFTp) {
                                    cambioDirFTp = clienteFtp.changeWorkingDirectory(rutaDestinoComp);
                                } else {
                                    respuesta += "problemas al crear el directorio" + rutaDestinoComp + "\n";
                                    myLogger.error("problemas al crear el directorio" + rutaDestinoComp + "\n");
                                }
                            }
                            if (cambioDirFTp) {
                                files = clienteFtp.listFiles(arrArchivosBO1.nombre);
                                if (files.length > 0) {
                                    respuesta += "El Archivo" + arrArchivosBO1.nombre + ": Ya existe \n";
                                    myLogger.error("El Archivo" + arrArchivosBO1.nombre + ": Ya existe \n");
                                    pasoArchivo="";
                                }
                                else{
                                    pasoArchivo = ftpUtil.pasaArchivoFTP(clienteFtp, rutaLocalComp, arrArchivosBO1.nombre);
                                }
                                if (pasoArchivo.equals("")) {
                                    arrArchivosBO1.estatusMigracion = ClientesConstants.ESTATUS_MIGRACION_SI;
                                    CantMigrados++;
                                } else {
                                    respuesta += "Error al pasar el archivo" + arrArchivosBO1.nombre + ": " + pasoArchivo + "\n";
                                    myLogger.error("Error al pasar el archivo" + arrArchivosBO1.nombre + ": " + pasoArchivo);
                                    arrArchivosBO1.estatusMigracion = ClientesConstants.ESTATUS_MIGRACION_ERROR;
                                }

                            }
                        } else {
                            arrArchivosBO1.estatusMigracion = ClientesConstants.ESTATUS_MIGRACION_ERROR;
                            respuesta += "Error en el directotio base " + rutaDestinoComp + "\n ";
                            myLogger.error("Error en el directotio base " + rutaDestinoComp);
                        }
                        ArchivoAsociadoDAO.updateEstatusArchivo(arrArchivosBO1);
                    } else {
                        respuesta += "EL archivo " + rutaLocalComp + " no existe \n";
                        myLogger.error("EL archivo " + rutaLocalComp + " no existe ");
                    }

                }
                clienteFtp.disconnect();
                Mail.enviaCorreo("Termina Migracion", "Se procesaron " + arrArchivosBO.size() + " De los cuales fueron exitosos: " + CantMigrados + "\n Mensajes : \n" + respuesta, Destinatarios);
                myLogger.info("Se procesaron " + arrArchivosBO.size() + " De los cuales fueron exitosos: " + CantMigrados);

            } else {
                myLogger.info("No hay archivos para migrar");
                Mail.enviaCorreo("MigracionArchivosHelper", "No hay archivos para migrar", Destinatarios);
            }
        } catch (Exception e) {
            myLogger.error("MigracionArchivosHelper", e);
            try {
                String Destinatarios = CatalogoHelper.getParametro("MAIL_CIERRE_RECIPIENT");
                Mail.enviaCorreo("Error Migracion", "Error en migracion : " + e+"/n se migraron "+CantRevisados, Destinatarios);

            } catch (Exception maile) {
                myLogger.error("MigracionArchivosHelper", maile);
            }

        } finally {
            try{
                clienteFtp.disconnect();
                if (cn != null) {
                    cn.close();
                }
            }
            catch (Exception ef){
                myLogger.error("Error al cerrar las conexiones");
            
            }
        }
     }
}

package com.sicap.clientes.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

public class ComunicacionUtil {
	
	private Connection conn;
	
	public boolean openSFTPConnection(String ip, String usr, String pwd){
		Logger.debug("Dentro de openFTPSConnection");
		boolean isAuthenticated = false;
		try{
			Logger.debug("Ip a conectar::" + ip);
			Logger.debug("User:" + usr);
			Logger.debug("Password:" + pwd);
			conn = new Connection(ip);
			conn.connect();
			isAuthenticated = conn.authenticateWithPassword(usr, pwd);
			if(!isAuthenticated || conn.isAuthenticationComplete()==false){
				throw new IOException("Authentication failed.");
			}
			Logger.debug("La conexion se ha establecido con el servidor:" + ip + "!!!");
			return true;
		}
		catch(IOException exc){
			Logger.debug("IOException en ComunicacionUtil.openFTPSConnection::" + exc);
			//exc.printStackTrace();
			return false;
		}
	}
	
	public void sendSFTP(String rutaArchivo, String rutaServidor, String modo){
		
		Logger.debug("Dentro de sendFTPS");
		Logger.debug("Ruta del archivo::"+rutaArchivo);
		Logger.debug("Ruta del servidor::"+rutaServidor);
		if(conn!=null){
			Logger.debug("No es null la conexion...");
			try{
				SCPClient scpClient = new SCPClient(conn);
				File arch = new File(rutaArchivo);
				Logger.debug("Ruta completa::"+ rutaArchivo + rutaServidor);
				BufferedReader buffer = new BufferedReader(new FileReader(arch));
				if(arch.exists()){
					Logger.debug("El archivo fue encontrado, en proceso de transferencia...");
					scpClient.put(rutaArchivo, rutaServidor, modo);
				}
				buffer.close();
				Logger.debug("Envio exitoso!!!");
			}
			catch(FileNotFoundException exc){
				exc.printStackTrace();
				Logger.debug("FileNotFoundException en ComunicacionUtil.sendFTPS::" + exc);
			}
			catch(IOException exc){
				exc.printStackTrace();
				Logger.debug("IOException en ComunicacionUtil.sendFTPS::" + exc);
			}	
		}
	}
	
	public void closeSFTPConnection(){
		try{
			Logger.debug("Por cerrar conexion...");
			conn.close();
			Logger.debug("Conexion cerrada...");
		}
		catch(Exception exc){
			Logger.debug("IOException en ComunicacionUtil.openFTPSConnection::" + exc);
		}
	}
}
package com.sicap.clientes.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.vo.ParametroVO;


/**

 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * @modelguid {E1E2B540-916C-468B-9AC1-69C0E5276268}
 */
public class SocketBuro {
	
	
	/** @modelguid {3FE122AE-95F7-4CAB-B740-508C8DA3969A} */
	public static String getStringBuro (String INTL) {
		
		String hostPuerto = ""; // IP Address y puerto
		String intl_respuesta = null;
		Socket socket = null;
		// Default Values!!!
		try{
			ParametroVO param = new ParametroVO();
			CatalogoDAO catDao = new CatalogoDAO();
			param = catDao.getParametro("HOST_PUERTO_BURO");
			hostPuerto = param.valor;

			DataOutputStream os = null;
			InputStream is = null;
			
	
			int c = 0;
			byte[] byte_array = new byte[33792];
			int count = 0;
			byte fin = 0x13;
	
			try {
				
				String host = hostPuerto.substring(0, hostPuerto.indexOf(':'));
				int port = Integer.parseInt(hostPuerto.substring(hostPuerto.indexOf(':')+1));
				
				Logger.debug( "Contacting " + host + " on port " + port);
				Logger.debug( "\nINTL Request:  " + INTL );
								
				socket = new Socket(host, port);
				
			} catch (UnknownHostException e) {
				System.out.println("No se pudo encontrar el host " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO - Error de comunicacion " + e.getMessage());
			}
	
			/** establece los canales de comunicacion  **/
			try {
				os = new DataOutputStream(socket.getOutputStream());
				is = socket.getInputStream();
			} catch (IOException e) {
				System.out.println("IO - Error de comunicacion al establecer los canales de comunicacion i/o " + e.getMessage());
			}
	
			/** envio de datos **/
			try {
				os.writeBytes(INTL);
				os.write(fin);
			} catch (IOException e) {
				System.out.println("IO - Error de comunicacion al enviar el INTL de consulta " + e.getMessage());
			}
	
			/** leyendo respuesta de TUC **/
			try {
				while ((c = is.read()) != 19) {
					byte_array[count++] = (byte) c;
				}
				intl_respuesta = new String(byte_array, 0, count);
			} catch (IOException e) {
				System.out.println("IO - Error de comunicacion al leer el INTL de respuesta " + e.getMessage());
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			Logger.debug("Excepci�n en getStringBuro SocketBuro");
		}finally{
			try{
				socket.close();
			}catch (Exception e) {
				
			}
		}
		Logger.debug("\nINTL Response: " + intl_respuesta);
		return intl_respuesta;
		
	}
}

package com.sicap.clientes.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.vo.ParametroVO;

public class ClienteCDC {
	
	private static final String TimeOut               = "<Error><Errores><DescripcionError>Se Rebaso el Tiempo de Espera con Circulo de Credito</DescripcionError></Errores></Error>";
	private static final String CDC_Down              = "<Error><Errores><DescripcionError>El servidor CDC se encuentra fuera de servicio [ Error: 400 ]</DescripcionError></Errores></Error>";
	private static final String CDC_Connection_Failed = "<Error><Errores><DescripcionError>Se rebaso el Tiempo de espera al intentar establecer la Conexión Circulo de Crédito</DescripcionError></Errores></Error>";
	
	@SuppressWarnings("deprecation")
	public static String getReporteCDC(String XMLSolicitud){

		ParametroVO param = new ParametroVO();
		CatalogoDAO catDao = new CatalogoDAO();
		
		String XMLReporte = "";
		int    HttpCode = -1;
		
		
		try {
			param = catDao.getParametro( "URL_CIRCULO" );
			String url = param.valor;
			
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(url);
			
			/**
			 * Confugurando Parametros de Time Out
			 */
			client.setTimeout(40*1000);
			client.setConnectionTimeout(20*1000);
			
			/**
			 * Agregando la solicitud al Post
			 */
			post.addParameter("solicitud", XMLSolicitud);
			
			/**
			 * Enviando Solicitud
			 */
			Logger.debug("Enviando Solicitud a Circulo de Crédito");
			HttpCode = client.executeMethod( post );
			
			if(HttpCode == 400){
				return CDC_Down;
			}
			
			else if(HttpCode == 200){
				XMLReporte = post.getResponseBodyAsString().trim();
			}
			
			else{
				return("El servidor CDC se encuentra fuera de servicio [ Error: "+HttpCode+" ]");
			}
			
		}catch(ConnectException conEx){
			return CDC_Down;
		}catch(SocketTimeoutException timeout){
			return TimeOut;
		}catch(ConnectTimeoutException cto){
			return CDC_Connection_Failed;
		}catch (IOException e) {
			return("Error al procesar la peticion  [ Error: "+HttpCode+" ]");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return XMLReporte;

	}

}

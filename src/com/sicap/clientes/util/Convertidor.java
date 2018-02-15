package com.sicap.clientes.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SucursalVO;


public class Convertidor {


	public static java.sql.Date toSqlDate(Date fecha){
		if ( fecha!=null ){
			return new java.sql.Date(fecha.getTime());
		}
		return null;
	}
	
	public static java.sql.Timestamp toSqlTimeStamp(Date fecha){
		if ( fecha!=null ){
			return new java.sql.Timestamp(fecha.getTime());
		}
		return null;
	}

	public static java.sql.Date stringToSqlDate(String fecha) throws Exception {

		java.util.Date retValue = stringToDate(fecha); 
		return toSqlDate(retValue); 

	}
	
	public static java.sql.Date stringToSqlDate(String fecha, String formato) throws Exception {

		java.util.Date retValue = stringToDate(fecha, formato); 
		return toSqlDate(retValue); 

	}

	public static java.util.Date stringToDate(String fecha) throws Exception {
		return stringToDate(fecha, null);
   }
	
	public static java.util.Date stringToDate(String fecha, String formato) throws Exception {
		java.util.Date retValue = null; 
		SimpleDateFormat date = null;

		if ( formato==null )
			date = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);
		else
			date = new SimpleDateFormat(formato);
		try { 
			date.setLenient(false);
			if ( fecha!=null && !fecha.equals("") ){
				if( fecha.equals("0001-01-01") || fecha.equals("00000000") || fecha.equals("0"))
					retValue = null;
				else
					retValue = date.parse(fecha); 
			}
		} catch(ParseException e) { 
			throw new Exception ( e.toString() );
		}
		return retValue; 
   }

	public static int stringToInt(String cadena) throws Exception{
		int valor = 0;
		try{
			if( cadena != null && !cadena.trim().equals("")){
				valor = Integer.parseInt(cadena.trim());
			}
		}catch(NumberFormatException e){
			throw new Exception(e.toString());
		}
		return valor;
	}


	public static long stringToLong(String cadena) throws Exception{
		long valor = 0;
		try{
			if( cadena != null && !cadena.trim().equals("")){
				valor = Long.parseLong(cadena.trim());
			}
		}catch(NumberFormatException e){
			throw new Exception(e.toString());
		}
		return valor;
	}


	public static float stringToFloat(String cadena) throws Exception{
		float valor = 0;
		try{
			valor = Float.parseFloat(cadena);
		}catch(NumberFormatException e){
			throw new Exception(e.toString());
		}
		return valor;
	}

	public static double stringToDouble(String cadena) throws Exception{
		double valor = 0;
		try{
			if( cadena != null && !cadena.equals("") ){
				if(cadena.indexOf("-") != -1){
					cadena = cadena.replace('-', ' ');
					valor = Double.parseDouble(cadena)*-1;
				}else{
					valor = Double.parseDouble(cadena);
				}
			}
		}catch(NumberFormatException e){
			throw new Exception(e.toString());
		}
		return valor;
	}
	public static double stringToDouble(String cadena, int decimales) throws Exception{
		double valor = 0;
		int divisor  = 0; 
		String aux = "1";
		try{
			if( cadena != null && !cadena.equals("") ){
				for( int i=0; i<decimales; i++ ){
					aux += "0";
				}
				divisor = Integer.parseInt( aux );
				if(cadena.indexOf("-") != -1){
					cadena = cadena.replace('-', ' ');
					valor = (Double.parseDouble(cadena) / divisor)*-1;
				}else{
					valor = Double.parseDouble(cadena) / divisor;
				}
			}
		}catch(NumberFormatException e){
			throw new Exception(e.toString());
		}
		return valor;
	}

	public static String dateToString(Date fecha) throws Exception {
		   String date = null;
		   try {
			   date = (new SimpleDateFormat(ClientesConstants.FORMATO_FECHA)).format(fecha); 
		   } catch ( Exception e ) {
			   throw new Exception(e.toString());
		   }
		   return date;
	   }

	
	public static String dateToString(Date fecha, String formato) throws Exception {
	   String date = null;
	   try {
		   date = (new SimpleDateFormat(formato)).format(fecha); 
	   } catch ( Exception e ) {
		   throw new Exception(e.toString());
	   }
	   return date;
   }

	public static String toSyncronetCharacterSet(String cadena){

		if ( cadena!=null && !cadena.trim().equals("") ){
			cadena = cadena.toUpperCase();
			cadena = cadena.replace('Ñ', 'N');
			cadena = cadena.replace('Á', 'A');
			cadena = cadena.replace('É', 'E');
			cadena = cadena.replace('Í', 'I');
			cadena = cadena.replace('Ó', 'O');
			cadena = cadena.replace('Ú', 'U');
			cadena = cadena.replace('Ü', 'U');
			cadena = cadena.replace('/', ' ');
		}
		return cadena;
	}

	public static Double getMontoIva(Double monto, int idSucursal, int opcion) throws Exception{
            //0 = Devuelve monto con iva
            //1 = Devuelve monto sin iva
            //2 = Devuelve iva del monto
            Double montoCalculado = 0.00;
            
            try{
                ParametroVO paramVO = new ParametroVO();
                CatalogoDAO catDAO = new CatalogoDAO();
                SucursalDAO sucDAO = new SucursalDAO();
                SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
                
                if(sucVO.fronterizo==true)
                    paramVO = catDAO.getParametro("IVA_FRONTERIZO");
                else
                    paramVO = catDAO.getParametro("IVA_NOFRONTERIZO");
                
                Double tasaIva = Convertidor.stringToDouble("0."+paramVO.valor);
                if(opcion==0)
                    montoCalculado = monto*(1+tasaIva);
                if(opcion==1)
                    montoCalculado = monto/(1+tasaIva);
                if(opcion==2)
                    montoCalculado = monto*tasaIva;
                
            }catch(Exception e){
                throw new Exception(e.toString());
            }
            return montoCalculado;
        }

	public static Double getMontoIva(Double monto, SucursalVO Sucursal, int opcion) throws Exception{
		//0 = Devuelve monto con iva
		//1 = Devuelve monto sin iva
		//2 = Devuelve iva del monto
		Double montoCalculado = 0.00;
		
		try{
			ParametroVO paramVO = new ParametroVO();
			CatalogoDAO catDAO = new CatalogoDAO();
			
			if(Sucursal.fronterizo==true)
				paramVO = catDAO.getParametro("IVA_FRONTERIZO");
			else
				paramVO = catDAO.getParametro("IVA_NOFRONTERIZO");
			
			Double tasaIva = Convertidor.stringToDouble("0."+paramVO.valor);
	
			if(opcion==0)
				montoCalculado = monto*(1+tasaIva);
			if(opcion==1)
				montoCalculado = monto/(1+tasaIva);
			if(opcion==2)
				montoCalculado = monto*tasaIva;
			
		}catch(Exception e){
			throw new Exception(e.toString());
		}
		return montoCalculado;
	}
	
	public static boolean esFronterizo(int idSucursal) throws Exception{
		//0 = Devuelve false si no es fronterizo
		boolean resp = false;
		
		try{
			SucursalDAO sucDAO = new SucursalDAO();
			SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
			if(sucVO.fronterizo==true)
				resp = true;
			
		}catch(Exception e){
			throw new Exception(e.toString());
		}
		return resp;
	}

	public static boolean esFronterizo(SucursalVO sucursal) {
		//0 = Devuelve false si no es fronterizo
		boolean resp = false;

		if(sucursal.fronterizo==true)
			resp = true;
			
		return resp;
	}
	
	public static String formatDateCirculo(String fecha) throws Exception {
		java.util.Date retValue = null; 
		SimpleDateFormat date = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA); 
		try { 
			date.setLenient(false);
			retValue = date.parse(fecha); 
		} catch(ParseException e) { 
			throw new Exception ( e.toString() );
		}
		return dateToStringCirculo(retValue); 
   }
	
	
	public static String dateToStringCirculo(Date fecha) throws Exception {
		   String date = null;
		   try {
			   date = (new SimpleDateFormat(ClientesConstants.FORMATO_CIRCULO)).format(fecha); 
			   date = FormatUtil.deleteChar(date, '/');
		   } catch ( Exception e ) {
			   throw new Exception(e.toString());
		   }
		   return date;
	   }

	public static String dateToStringBuro(Date fecha) throws Exception {
		   String date = null;
		   try {
			   date = (new SimpleDateFormat(ClientesConstants.FORMATO_FECHA)).format(fecha); 
		   } catch ( Exception e ) {
			   throw new Exception(e.toString());
		   }
		   return date;
	   }
	
	public static String dateToStringCDC(Date fecha) throws Exception {
		String date = "";
		try {
			if(fecha != null)
				date = (new SimpleDateFormat(ClientesConstants.FORMATO_REPORTE_CIRCULO)).format(fecha).toUpperCase(); 
		} catch ( Exception e ) {
			throw new Exception(e.toString());
		}
		return date;
	}
	
	public static String timestampToString(Timestamp fecha) throws Exception {
		String date = "";
		try {
			if(fecha != null)
				date = (new SimpleDateFormat(ClientesConstants.FORMATO_REPORTE_CIRCULO)).format(fecha).toUpperCase(); 
		} catch ( Exception e ) {
			throw new Exception(e.toString());
		}
		return date;
	}

		public static String characterSet(String cadena){

			if ( cadena!=null && !cadena.trim().equals("") ){
				cadena = cadena.replace('Ñ', 'N');
				cadena = cadena.replace('Á', 'A');
				cadena = cadena.replace('É', 'E');
				cadena = cadena.replace('Í', 'I');
				cadena = cadena.replace('Ó', 'O');
				cadena = cadena.replace('Ú', 'U');
				cadena = cadena.replace('Ü', 'U');
				cadena = cadena.replace('ñ', 'n');
				cadena = cadena.replace('á', 'a');
				cadena = cadena.replace('é', 'e');
				cadena = cadena.replace('í', 'i');
				cadena = cadena.replace('ó', 'o');
				cadena = cadena.replace('ú', 'u');
				cadena = cadena.replace('ü', 'u');
			}
			return cadena;
		}
	
}

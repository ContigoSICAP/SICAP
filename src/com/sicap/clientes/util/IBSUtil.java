package com.sicap.clientes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.IBSHelper;

public class IBSUtil {
	
	public void procesaArchivoSaldosIBS(){
		
		SaldoIBSDAO     saldosIBSdao = new SaldoIBSDAO();
		SimpleDateFormat sdf          = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
		
		try{
			List archivoSaldosIBS = null;
			/***** Carga Archivo IBS *****/
			Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Leyendo Archivo de SaldosIBS ");
			archivoSaldosIBS = IBSHelper.readFileSaldosIBS();
			
			if( archivoSaldosIBS!=null && archivoSaldosIBS.size()>0 ){
				Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Limpiando Tabla d_saldos ");
				//SE CAMBIO EL METODO YA QUE ERA UN TRUNCATE ALEX 2012-11-15
				//saldosIBSdao.deleteSaldosIBS();
				Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Persistiendo "+archivoSaldosIBS.size()+" registros en Base de Datos  ");
				saldosIBSdao.addSaldosIBS( archivoSaldosIBS );
				archivoSaldosIBS = null;
			}else
				Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Sin registros a persistir " );
			
			Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Proceso de carga de saldos IBS Finalizado " );
			
			/***** Carga Archivo CWIN *****/
			Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Leyendo Archivo de Saldos CWIN ");
			archivoSaldosIBS = IBSHelper.readFileSaldosCarteraWin();
			if( archivoSaldosIBS != null ){
				Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Persistiendo "+archivoSaldosIBS.size()+" registros en Base de Datos  ");
				saldosIBSdao.addSaldosIBS( archivoSaldosIBS );
			}else
				Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Sin registros a persistir " );
			
			Logger.debug("IBSUtil :: procesaArchivoSaldosIBS :: Proceso de carga de saldos CarteraWin Finalizado " );
			CatalogoHelper.updateParametro("FECHA_EJECUCION_CARGA_SALDOS", sdf.format(new Date()));
						
		}catch(Exception e){
			System.out.println("Problema al cargar saldos de IBS");
			e.printStackTrace();
		}
		
	}
	
	public boolean validaUltimaFechaEjecucion(){
    	boolean esFechaAnterior = false;
    	try{
			String ultimaFecha = CatalogoHelper.getParametro("FECHA_EJECUCION_CARGA_SALDOS");
			SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
			Date fechaUltimoProceso = format.parse(ultimaFecha);
			Calendar hoy = Calendar.getInstance();
			Date fechaHoy = format.parse( hoy.get(Calendar.YEAR)+"-"+(hoy.get(Calendar.MONTH)+1)+"-"+hoy.get(Calendar.DAY_OF_MONTH) );
			if(fechaHoy.compareTo(fechaUltimoProceso)>0)
				esFechaAnterior = true;
    	}
    	catch(ParseException exc){
    		Logger.debug("ParseException en IBSUtil.validaUltimaFechaEjecucion::" + exc);
    		exc.printStackTrace();
    	}
    	finally{
    		
    	}
    	return esFechaAnterior;
    }

}
package com.sicap.clientes.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.dao.ReferenciaSHFDAO;
import com.sicap.clientes.dao.SaldoHistoricoSFHDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaSHFVO;
import com.sicap.clientes.vo.SaldoT24VO;

public class SHFHelper {
	
	
	public static boolean esDiaDeCarga() {
		Calendar c2 = new GregorianCalendar();
		int dia = c2.get(Calendar.DATE) ;
		int mes = c2.get(Calendar.MONTH)+ 1;
		int anio = c2.get(Calendar.YEAR);
		if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))){
			int AnioBisiesto []= {31,29,31,30,31,30,31,31,30,31,30,31};
			if (AnioBisiesto[mes-1]==dia||dia==1)
				return true;
			else
				return false;
		}
		else{
			int AnioNormal []= {31,28,31,30,31,30,31,31,30,31,30,31};
			if (AnioNormal[mes-1]==dia||dia==1)
				return true;
			else
				return false;
		}
	}
	
	public void cargaSaldosHistoricosSHF(){
		ReferenciaSHFVO referenciaSHF = null;
		SaldoT24VO saldoT24VO = null;
		SaldoT24DAO saldoT24DAO = new SaldoT24DAO();
		SaldoHistoricoSFHDAO saldoSHFDAO = new SaldoHistoricoSFHDAO();
		SimpleDateFormat sdf          = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
		
		List referenciasSHF = new ReferenciaSHFDAO().getReferenciasSHF();
		Iterator i = referenciasSHF.iterator();
		System.out.println("Las referencias a cargar son: " +  referenciasSHF.size());
		
		while( i.hasNext() ){
			referenciaSHF = (ReferenciaSHFVO)i.next();
			System.out.println("Procesando Referencia ==> " + referenciaSHF.getReferencia());
			saldoT24VO = saldoT24DAO.getSaldosT24ByReferencia( referenciaSHF.getReferencia() );
			saldoSHFDAO.insertSaldoHistoricoSHF( saldoT24VO );
		}
		CatalogoHelper.updateParametro("FECHA_EJECUCION_CARGA_HISTORICOS_SHF", sdf.format(new Date()));
	}
	
	public boolean validaUltimaFechaEjecucion(){
    	boolean esFechaAnterior = false;
    	try{
			String ultimaFecha = CatalogoHelper.getParametro("FECHA_EJECUCION_CARGA_HISTORICOS_SHF");
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
    	return esFechaAnterior;
    }

}

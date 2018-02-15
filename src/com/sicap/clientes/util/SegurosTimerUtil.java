package com.sicap.clientes.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.SegurosAfirmeDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.SegurosVO;
 
public class SegurosTimerUtil extends TimerTask implements ServletContextListener {
    private Timer timer;
 
    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        //El timer se ejecuta cada : n minutos
        timer.schedule(this, 0, 30*60*1000);
      
    }
 
    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }
    
    public static void actualizaSegurosAfirme() {
    	 	
    	try{
    		SegurosAfirmeDAO segurosdao = new SegurosAfirmeDAO();
    		segurosdao.insertaSegurosAfirme();
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}    	
    }
   
    public static void setProximoVencimiento(SegurosVO seg) {
    	try {
    		DecisionComiteDAO decisiondao = new DecisionComiteDAO();
    		DecisionComiteVO decision = decisiondao.getDecisionComite(seg.idCliente, seg.idSolicitud);
    		int frecPago = decision.frecuenciaPago;
    		
    		switch(frecPago) {
    			case ClientesConstants.PAGO_SEMANAL:	//7 d�as a la semana
    				// si la fecha del proximo vencimiento es mayor a la fecha de vencimiento del cr�dito
    				// poner esta �ltima fecha
    				seg.fechaProximoVencimiento = Convertidor.toSqlDate(FechasUtil.getDate(seg.fechaProximoVencimiento,7,1));
    				Logger.debug("Nueva Fecha: " + seg.fechaProximoVencimiento + " " + ClientesConstants.PAGO_SEMANAL + " Pago semanal");
    				break;
    			case ClientesConstants.PAGO_QUINCENAL:	//15 d�as
    				seg.fechaProximoVencimiento = Convertidor.toSqlDate(FechasUtil.getDate(seg.fechaProximoVencimiento,15,1));
    				Logger.debug("Nueva Fecha: " + seg.fechaProximoVencimiento + " " + ClientesConstants.PAGO_QUINCENAL + " Pago quincenal");
    				break;
    			case ClientesConstants.PAGO_MENSUAL:  //30 d�as
    				seg.fechaProximoVencimiento = Convertidor.toSqlDate(FechasUtil.getDate(seg.fechaProximoVencimiento,1,0));
    				Logger.debug("Nueva Fecha: " + seg.fechaProximoVencimiento + " " + ClientesConstants.PAGO_MENSUAL + " Pago mensual");
    				break;
    		}   		
    	}
    	catch (ClientesException e) {
    		Logger.debug(e.getMessage());
    		e.printStackTrace();
    	}	
    }
    
    public static void cambiaEstatusPagoSeguro(SegurosVO seg, Date fechaVencimiento) {
		
		if(seg.saldoActual > 0 && seg.fechaVencimiento.equals(fechaVencimiento)) {
			seg.estatus = ClientesConstants.SEGURO_VENCIDO;
		}
		else { 
			seg.estatus = ClientesConstants.SEGURO_VIGENTE;
		}
		
	}
    
    public static void actualizarCuotas(SegurosVO seguro) {
    	
    	seguro.numCuotasTranscurridas++;
    	if(seguro.numCuotasRestantes!=0)
    	   seguro.numCuotasRestantes--;
    	
    	//verificar si pag�  saldo actual=0 significa que pag�
    	  if(seguro.saldoActual==0){
    		  seguro.numCuotasVigentes++;
    		  seguro.estatus = ClientesConstants.SEGURO_VIGENTE;
    	 }else{ // no pag� el seguro
    		  seguro.numCuotasVencidas++;
    	      seguro.estatus = ClientesConstants.SEGURO_VENCIDO;	  
    	 }
    	  	
    	    seguro.saldoActual = seguro.prima;
    }
    
    public static void getFechasVencimiento(Date vencimiento)  {
    	ArrayList<SegurosVO>listadoSeguros = new ArrayList<SegurosVO>();
    	SegurosDAO segurosdao = new SegurosDAO();
    	SegurosVO seguro = null;
    	
    	try {
    		listadoSeguros = segurosdao.getSeguros(Convertidor.toSqlDate(vencimiento));
    		if(listadoSeguros!=null) {
    			if(listadoSeguros.size()>0) {
    				Logger.debug("SegurosTimerUtil:Se han encontrado fechas vencidas " + listadoSeguros.size());
    				for(int i = 0; i < listadoSeguros.size(); i++) {
    					seguro = listadoSeguros.get(i);
    					
    					Logger.debug("Fecha de vencimiento del cr�dito : " + seguro.fechaVencimiento);
    					Logger.debug("Fecha del pr�ximo vencimiento del seguro : " + seguro.fechaProximoVencimiento);
    					//seguro.fechaVencimiento = seguro.fechaProximoVencimiento;    				
    					
    					if(seguro.fechaProximoVencimiento.before(seguro.fechaVencimiento)){
    					  Logger.debug("Se obtiene la frecuencia de pago y se calcula la nueva fecha");
    					  setProximoVencimiento(seguro);
    					  Logger.debug("Se actualizan cuotas");
    					  actualizarCuotas(seguro);
    					  segurosdao.updateSeguro(seguro);
    					}else if(seguro.fechaProximoVencimiento.equals(seguro.fechaVencimiento)){
    						Logger.debug("La fecha de vencimiento del cr�dito es igual a la fecha del " +
    								"pr�ximo vencimiento del seguro: Ya no se calcula nueva fecha, s�lo se actualizan cuotas");
    					   actualizarCuotas(seguro);
      					   segurosdao.updateSeguro(seguro);					
    					}else{
    						 Logger.debug("Fecha de vencimiento del cr�dito expir�");    						
    					} 
    					  
                          seguro = null;
                          
    				}
    				
    			}
    			else {
    				Logger.debug("No se han encontrado fechas vencidas para procesar");
    			}
    		}
    	}
    	catch (ClientesException e) {
    		Logger.debug(e.getMessage());
    		e.printStackTrace();
    	}finally{
    		Logger.debug("Termina ejecucion de SegurosTimerUtil a las : "+new Date().toString());
    	}
    }
    
    public static void procesamientoFinDia() {    	
    	Date fechaActual = new Date();
    	Logger.debug("SegurosTimerUtil:Fecha actual de procesamiento fin de dia : " + fechaActual.toString());
    	getFechasVencimiento(fechaActual);  	
    }
    
    public static void procesamientoFinMes() {
    	Calendar cal = GregorianCalendar.getInstance();
    	int dia = cal.get(GregorianCalendar.DATE);
    	Logger.debug("El ultimo dia del mes es " + cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
    	int diaFinalMes = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    	
    	if((diaFinalMes == dia)) {
    		Logger.debug("Nos encontramos en el ultimo dia del mes");	
    		actualizaSegurosAfirme();
    	}
    	else {
    		Logger.debug("Es otro dia cualquiera, no se ejecuta procesamientoFinMes");
    	}
    }
       
    public void run() {
    	Calendar cal = Calendar.getInstance();
    	
    	Logger.debug("Ejecutando SegurosTimerUtil : "+new Date().toString());
    	
    	try{
    		String  horaMensualEjecucion = CatalogoHelper.getParametro("HORA_EJECUCION_FIN_MES");
    		if (horaMensualEjecucion!=null && Integer.parseInt(horaMensualEjecucion)==cal.get(Calendar.HOUR_OF_DAY) ) {	
    			Logger.debug("Ejecutando Seguros: Procesamiento Fin de Mes");
    			procesamientoFinMes();
    		}
    		else {
    			Logger.debug("Fecha y hora no adecuada procesamiento fin de mes");
    		}
    		
	    	if (( cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY)) {
	    		String horaEliminacion = CatalogoHelper.getParametro("HORA_EJECUCION_FIN_DIA");	    		
	    		if ( horaEliminacion!=null && Integer.parseInt(horaEliminacion)==cal.get(Calendar.HOUR_OF_DAY) ) {
			    	Logger.debug("Ejecutando Seguros: Procesamiento Fin de Dia");
			    	procesamientoFinDia();
	    		}
	    		else{
	    			Logger.debug("Hora no adecuada para procesamiento fin de dia");
	    		}
	    	}else{
	    		Logger.debug("No se ejecuta procesamiento fin de dia por ser fin de semana");
	    	}
		}finally{
			Logger.debug("Termina ejecucion de SegurosTimerUtil a las : "+new Date().toString());
		}
    }
}

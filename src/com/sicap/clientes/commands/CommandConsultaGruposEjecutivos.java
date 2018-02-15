package com.sicap.clientes.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.MetasEjecutivosDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.MetasEjecutivosVO;


public class CommandConsultaGruposEjecutivos implements Command{


	private String siguiente;


	public CommandConsultaGruposEjecutivos(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		int idEjecutivo = 0;
		int idSucursal = 0;
		Notification notificaciones[] = new Notification[1];		
		
		try{
			String tipoConsulta = HTMLHelper.getParameterString(request, "tipoconsulta");
			Calendar cal = Calendar.getInstance();
			idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			int idMonth = HTMLHelper.getParameterInt(request, "idMonth");
			int idYear = HTMLHelper.getParameterInt(request, "idYear");
                        String monthYear = "";
                        if(idYear==2){
                            monthYear = String.valueOf(idMonth)+(cal.get(Calendar.YEAR));
                        } else if(idYear==1){
                            monthYear = String.valueOf(idMonth)+(cal.get(Calendar.YEAR)-1);
                        }                        
                        int monthYearInt = Integer.parseInt(monthYear);
                        System.out.println("MES: "+idMonth);
                        System.out.println("AÑO: "+idYear);
                        System.out.println("MES AÑO STRING: "+monthYear);
                        System.out.println("MES AÑO INT: "+monthYearInt);
			TreeMap catEjecutivos = (TreeMap)request.getAttribute("EJECUTIVOS_DESTINO");
			System.out.println("tipo consulta "+tipoConsulta);
			if ( tipoConsulta.equalsIgnoreCase("planeacion") ){
			Date dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
		    	String dateIni = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 0), ClientesConstants.FORMATO_FECHA_EU);
		    	String dateEnd = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 1), ClientesConstants.FORMATO_FECHA_EU);
		    	GrupoVO grupos[] = new GrupoDAO().getGruposPorVencer(idEjecutivo, dateIni, dateEnd);
				EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(idEjecutivo);
				
				if ( grupos != null && grupos.length > 0 ){
					request.setAttribute("GRUPOS", grupos);
					request.setAttribute("EJECUTIVO", ejecutivo);
				}
				else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron grupos por vencer para el ejecutivo seleccionado");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else if ( tipoConsulta.equalsIgnoreCase("gestion") ){				
		    	Date dateNow = cal.getTime();
		    	Date dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
                        
		    	int porcentaje = 0;
		    	int numIntegrantesNuevos = 0;
// JBL DIC/10 se cambio temporalmente para mostrar los resultados		    	
//		    	String dateIni = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNow, 2), ClientesConstants.FORMATO_FECHA_EU);
//		    	String dateEnd = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNow, 3), ClientesConstants.FORMATO_FECHA_EU);
		    	String dateIni = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 0), ClientesConstants.FORMATO_FECHA_EU);
		    	String dateEnd = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 1), ClientesConstants.FORMATO_FECHA_EU);
		    	System.out.println("ejecutivo "+idEjecutivo + "fechaini "+dateIni +"fechafin "+dateEnd);
		    	GrupoVO gruposNo[] = new GrupoDAO().getGruposMetasEjecutivos(idEjecutivo, dateIni, dateEnd);
		    	GrupoVO gruposSi[] = new GrupoDAO().getGruposMetasDesembolsados(idEjecutivo, dateIni, dateEnd);
		    	// JBL DIC/10 se cambio temporalmente para mostrar los resultados		    	
//		    	String fechaMeta = FechasUtil.obtenParteFecha(dateNow, 2) + FechasUtil.obtenParteFecha(dateNow, 3);
		    	String fechaMeta = FechasUtil.obtenParteFecha(dateNextMonth, 2) + FechasUtil.obtenParteFecha(dateNextMonth, 3);
		    	MetasEjecutivosVO metas = new MetasEjecutivosDAO().getMeta(idEjecutivo, Integer.valueOf(fechaMeta));
				EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(idEjecutivo);
				
				for ( int i=0 ; gruposSi!=null && i < gruposSi.length ; i++ ){
					numIntegrantesNuevos+=gruposSi[i].ciclos[0].numIntegrantes;
				}
				
				if ( metas != null )
					porcentaje = ((metas.integrantes-numIntegrantesNuevos)*100)/metas.meta;
				
				request.setAttribute("GRUPOSSI", gruposSi);
				request.setAttribute("GRUPOSNO", gruposNo);
				request.setAttribute("METAS", metas);
				request.setAttribute("PORCENTAJE", porcentaje);
				request.setAttribute("EJECUTIVO", ejecutivo);
				
				if ( gruposNo == null && gruposSi == null ){
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron grupos por vencer para esta semana del ejecutivo");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else if ( tipoConsulta.equalsIgnoreCase("monitoreo") ){
		    	GregorianCalendar dateOut = new GregorianCalendar();
		    	Date dateNow = cal.getTime();
				dateOut.setTime(dateNow);
		    	Date dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
		    	dateOut.roll(Calendar.MONTH, -1);
		    	GrupoVO[] gruposNo = null;
		    	GrupoVO[] gruposSi = null;
		    	String out = "";
		    	catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(HTMLHelper.getParameterInt(request, "idSucursal"), "A");
		    	
		    	Set set = catEjecutivos.keySet();
		    	Iterator llaves = set.iterator();
		    	Integer key = null;
		    	while (llaves.hasNext()){
		    		key = (Integer)llaves.next();
			    	String dateIni = Convertidor.dateToString(dateOut.getTime(), ClientesConstants.FORMATO_FECHA_EU);
//			    	String dateEnd = Convertidor.dateToString(dateNow, ClientesConstants.FORMATO_FECHA_EU);
			    	String dateEnd = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 1), ClientesConstants.FORMATO_FECHA_EU);
			    	System.out.println("ejecutivo "+key + "fechaini "+dateIni +"fechafin "+dateEnd);
			    	gruposNo = new GrupoDAO().getGruposMetasEjecutivos(key, dateIni, dateEnd);
			    	if ( gruposNo != null ){
			    		System.out.println("ENTRO A GRUPOS NO");
			    		out = "<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr bgcolor='#009865' class='whitetext'>" +
			    		"<td width='90%' align='center'>" + HTMLHelper.getDescripcion(catEjecutivos, key) + "</td></tr></table></td></tr>" +
			    		"<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr bgcolor='#009865' class='whitetext'><td width='2%'" +
		    			"align='center'>No.Grupo</td><td width='10%' align='center'>Grupo</td><td width='3%' align='center'>Calificación</td><td width='3%'" +
		    			"align='center'>Ciclo</td><td width='5%' align='center'>No.Integrantes</td><td width='7%' align='center'>Fecha de vencimiento</td>" +
		    			"</tr></table></td></tr>";
			    		
			    		for ( int i=0 ; i < gruposNo.length ; i++ ){
			    		out+="<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr>"+
			    			"<td width='2%'	align='center'>" + gruposNo[i].idGrupo + "</td><td width='10%' align='center'>" + gruposNo[i].nombre +
			    			"</td><td width='3%' align='center'>" + gruposNo[i].calificacion + "</td><input type='hidden' name='" + gruposNo[i].ciclos[0]+i + "value='" + 
			    			gruposNo[i].ciclos[0].idCiclo + "'><td width='3%' align='center'>" + gruposNo[i].ciclos[0].idCiclo + "</td><td width='5%' align='center'>" +
			    			gruposNo[i].ciclos[0].numIntegrantes + "<td width='7%' align='center'>" +
			    			HTMLHelper.displayField(gruposNo[i].ciclos[0].fechaUltimoPago) + "</td></tr></table></td></tr>";
			    		}
			    	}	
			    	
			    	dateIni = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNow, 0), ClientesConstants.FORMATO_FECHA_EU);
					dateEnd = Convertidor.dateToString(dateNow, ClientesConstants.FORMATO_FECHA_EU);			    	
					gruposSi = new GrupoDAO().getGruposMetasDesembolsados(key, dateIni, dateEnd);					

			    	if ( gruposSi != null ){
						if ( gruposNo == null ){
					    		out += "<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr bgcolor='#009865' class='whitetext'>" +
					    		"<td width='90%'	align='center'>" + HTMLHelper.getDescripcion(catEjecutivos, key) + "</td></tr></table></td></tr>" +
					    		"<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr bgcolor='#009865' class='whitetext'><td width='2%'" +
				    			"align='center'>No.Grupo</td><td width='10%' align='center'>Grupo</td><td width='3%' align='center'>Calificación</td><td width='3%'" +
				    			"align='center'>Ciclo</td><td width='5%' align='center'>No.Integrantes</td><td width='7%' align='center'>Fecha de vencimiento</td>" +
				    			"</tr></table></td></tr>";
						}
						
			    		for ( int i=0 ; i < gruposSi.length ; i++ ){
				    		out+="<tr><td colspan='2' align='center'><table border='1' width='90%' align='center'><tr>"+
				    			"<td width='2%'	align='center'>" + gruposSi[i].idGrupo + "</td><td width='10%' align='center'>" + gruposSi[i].nombre +
				    			"</td><td width='3%' align='center'>" + gruposSi[i].calificacion + "</td><input type='hidden' name='" + gruposSi[i].ciclos[0]+i + "value='" + 
				    			gruposSi[i].ciclos[0].idCiclo + "'><td width='3%' align='center'>" + gruposSi[i].ciclos[0].idCiclo + "</td><td width='5%' align='center'>" +
				    			gruposSi[i].ciclos[0].numIntegrantes + "<td width='7%' align='center'>" +
				    			HTMLHelper.displayField(gruposSi[i].ciclos[0].fechaUltimoPago) + "</td></tr></table></td></tr>";
				    		}
					}
			    }

				
		    	if ( !out.equals("") )
					request.setAttribute("SALIDA", out);
				else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron datos de grupos para la sucursal seleccionada");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}

			}
			request.setAttribute("idSucursal", idSucursal);
			request.setAttribute("idEjecutivo", idEjecutivo);
			request.setAttribute("EJECUTIVOS_DESTINO", catEjecutivos);

		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}


}

package com.sicap.clientes.helpers;


import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.CreditoViviendaVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
/**
 * 
 * @author Administrador
 *
 */

public class CreditoHelper{
    
    private static Logger myLogger = Logger.getLogger(CreditoHelper.class);
/**
 * 
 * @param notificaciones
 * @return
 */
	public static String displayNotifications(Notification[] notificaciones){

		String notificacion = "";
		Notification not = null;

		for ( int c=0 ; notificaciones!=null && c<notificaciones.length ; c++ ){
			not = (Notification)notificaciones[c];
			if ( not.type==ClientesConstants.ERROR_TYPE ){
				notificacion += "<b><font color='"+ClientesConstants.ERROR_COLOR+"'>"+not.text+"</font><b><br><br>";
			}
			else{
				notificacion += "<b><font color='"+ClientesConstants.INFO_COLOR+"'>"+not.text+"</font></b><br><br>";
			}
		}
		return notificacion;

	}
/**
 * 
 * @param buroActualizado
 * @param request
 * @return
 * @throws Exception
 */

	public static CreditoVO getBuroVO (CreditoVO buroActualizado ,HttpServletRequest request) throws Exception{
	
		buroActualizado.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		buroActualizado.idObligado =  HTMLHelper.getParameterInt(request, "idObligado");
		buroActualizado.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		buroActualizado.comportamiento = HTMLHelper.getParameterInt(request,"comportamiento");
		buroActualizado.calificacionMesaControl = HTMLHelper.getParameterInt(request,"calificacionMesaControlCirculo");
		buroActualizado.fechaCaptura= new Timestamp(System.currentTimeMillis());
		//buroActualizado.fechaConsulta= new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaConsulta").getTime());
		buroActualizado.fechaConsulta= Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaConsulta"));
		buroActualizado.descripcion = request.getParameter("descripcion");
		buroActualizado.tipoCredito = ClientesConstants.BURO_CREDIT0;
		buroActualizado.tipoCuenta = HTMLHelper.getParameterInt(request, "tipoCuenta");
		buroActualizado.antCuenta = HTMLHelper.getParameterInt(request, "antCuenta");
		buroActualizado.numBusquedaCuenta = HTMLHelper.getParameterInt(request, "numBusquedaCuenta");
                
		
		return buroActualizado;
		
	}
        public static BitacoraEstatusVO getBitacoraEstatus (BitacoraEstatusVO bitVO ,HttpServletRequest request) throws Exception{
	
		bitVO.estatus= HTMLHelper.getParameterInt(request, "estatusSolicitud");
		bitVO.comentario =  HTMLHelper.getParameterString(request, "comentario");
				
		return bitVO;
		
	}
	
	public static CreditoViviendaVO getCreditoViviendaVO (CreditoViviendaVO creditoActualizado ,HttpServletRequest request) throws Exception{
		
		creditoActualizado.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		creditoActualizado.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		creditoActualizado.tipoCredito = 1;
		creditoActualizado.cofinanciado = HTMLHelper.getParameterInt(request, "esquema");
		creditoActualizado.numCredito = HTMLHelper.getParameterString(request, "numcredito");
		creditoActualizado.tipoTasa = 1;
		creditoActualizado.valorSolucion = HTMLHelper.getParameterDouble(request, "valorhabitacional");
		creditoActualizado.valorAvaluo = HTMLHelper.getParameterDouble(request, "valoravaluo");
		creditoActualizado.derechos = HTMLHelper.getParameterDouble(request, "derechos");
		creditoActualizado.impuestos = HTMLHelper.getParameterDouble(request, "impuestos");
		creditoActualizado.gastosOperacion = HTMLHelper.getParameterDouble(request, "gastos");
		creditoActualizado.ahorro = HTMLHelper.getParameterDouble(request, "ahorro");
		creditoActualizado.subcuentaTitular = HTMLHelper.getParameterDouble(request, "subtitular");
		creditoActualizado.CLABEBancaria = HTMLHelper.getParameterString(request, "clavebancaria");
		return creditoActualizado;

	}

	public static CreditoVO getCirculoVO (CreditoVO buroActualizado ,HttpServletRequest request) throws Exception{
		
//Ajustar nombre paramentros 
		
		buroActualizado.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		buroActualizado.idObligado =  HTMLHelper.getParameterInt(request, "idObligado");
		buroActualizado.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		buroActualizado.comportamiento = HTMLHelper.getParameterInt(request,"comportamientoCirculo");
		buroActualizado.calificacionMesaControl = HTMLHelper.getParameterInt(request,"calificacionMesaControlCirculo");
		buroActualizado.fechaCaptura= new Timestamp(System.currentTimeMillis());
                if(HTMLHelper.getParameterDate(request, "fechaConsultaCirculo")!=null){
                    buroActualizado.fechaConsulta= new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaConsultaCirculo").getTime());
                }
		buroActualizado.descripcion = request.getParameter("descripcionCirculo");
		buroActualizado.tipoCredito = ClientesConstants.CIRCULO_CREDIT0;
                buroActualizado.aceptaRegular = HTMLHelper.getParameterInt(request, "aceptaRegular");
                buroActualizado.otraFin = HTMLHelper.getCheckBox(request, "otraFinancieraChck");

		return buroActualizado;

	}


	public static boolean puedeConsultarInfoCrediticia(HttpServletRequest request, int indice, ClienteVO cliente, int tipoSociedad) throws Exception{
		
		int idObligado = HTMLHelper.getParameterInt(request, "idObligado");
		boolean esUsuarioAutorizado = false;
		boolean existeArchivo = false;
		
		
		/*if ( tipoSociedad == ClientesConstants.SOCIEDAD_BURO ){
			if ( request.isUserInRole("CONSULTA_CREDITICIA") || request.isUserInRole("CONSULTA_CREDITICIA_NUEVA") )
			esUsuarioAutorizado = true;
		}
		else if ( tipoSociedad == ClientesConstants.SOCIEDAD_CIRCULO ){
			if ( request.isUserInRole("CONSULTA_CREDITICIA") || request.isUserInRole("CONSULTA_CREDITICIA_NUEVA") )
			esUsuarioAutorizado = true;
		}*/

		if ( cliente.solicitudes[indice].tipoOperacion==ClientesConstants.CREDIHOGAR || request.isUserInRole("ANALISIS_CREDITO") )
			return true;
		if ( idObligado==0 ){
			if ( request.isUserInRole("CONSULTA_CREDITICIA") || request.isUserInRole("CONSULTA_CREDITICIA_NUEVA") )
				esUsuarioAutorizado = true;
			
			existeArchivo = ArchivosAsociadosHelper.existe(cliente.solicitudes[indice].archivosAsociados, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
		}else{
			if ( request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO") || request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO_NUEVA") )
				esUsuarioAutorizado = true;
			
			/*if ( tipoSociedad == ClientesConstants.SOCIEDAD_BURO ){
				if ( request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO") || request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO_NUEVA") )
				esUsuarioAutorizado = true;
			}
			else if ( tipoSociedad == ClientesConstants.SOCIEDAD_CIRCULO ){
				if ( request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO") || request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO_NUEVA") )
				esUsuarioAutorizado = true;
			}*/

			if ( idObligado==ClientesConstants.OBLIGADO_UNO )
				existeArchivo = ArchivosAsociadosHelper.existe(cliente.solicitudes[indice].archivosAsociados, ClientesConstants.ARCHIVO_TIPO_PRIMER_OBLIGADO);
			else if (idObligado == ClientesConstants.OBLIGADO_DOS)
				existeArchivo = ArchivosAsociadosHelper.existe(cliente.solicitudes[indice].archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SEGUNDO_OBLIGADO);
		}
		
		if ( (existeArchivo && esUsuarioAutorizado) || (esEjecutivoAutorizado(cliente.solicitudes[indice].idEjecutivo) && esUsuarioAutorizado) )
			return true;
		else
			return false;
	
	}




	public static boolean esEjecutivoAutorizado(int idEjecutivo){
		boolean resultado = false;
		String valor = CatalogoHelper.getParametro("EJECUTIVOS_CONSULTA_BURO");
		String ejecutivosAutorizados[] = valor.split(",");
 		//int ejecutivosAutorizados[] = new int[elementos.length]; 
 
 		//{7,9,11,23,25,43,46,49,50,51,52,54,60,64,65,75,76,77,78,79,80,83,84,87};
		for ( int i=0 ; i<ejecutivosAutorizados.length ; i++ ){
			if ( Integer.parseInt(ejecutivosAutorizados[i].trim())==idEjecutivo ){
				resultado = true;
				break;
			}
		}
		return resultado;
	}

        public static int obtenerCalificacionCredito(CreditoVO credito){
            int calificacion = 0;
            
            if(credito.calificacionMesaControl != 0){
                calificacion = credito.calificacionMesaControl;
            }else if(credito.comportamiento != 0){
                calificacion = credito.comportamiento;
            }
            
            return calificacion;
        }
        
    public static Date obtenerFechaAutorizadosPorExcepcion(){
        Date fechaAutorizadosPorExcepcion=null;
        try {
            String fechaString = CatalogoHelper.getParametro("FECHA_AUTORIZADOS_POR_EXCEPCION");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            fechaAutorizadosPorExcepcion = sdf.parse(fechaString);
            
        } catch (ParseException ex) {
            myLogger.error("Error al parsear fecha ultimo cierre", ex);
        }
        return fechaAutorizadosPorExcepcion;
    }
}
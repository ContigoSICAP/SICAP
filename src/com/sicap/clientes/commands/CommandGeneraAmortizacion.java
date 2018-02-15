package com.sicap.clientes.commands;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;


public class CommandGeneraAmortizacion implements Command{


	private String siguiente;


	public CommandGeneraAmortizacion(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Calendar cal = Calendar.getInstance();
		TablaAmortizacionVO[] tabla = null;
		ClienteVO cliente = new ClienteVO();
		SolicitudVO solicitud = new SolicitudVO();
		GrupoVO grupo = new GrupoVO();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
		cliente.idCliente = 1;
		solicitud.idSolicitud = 1;
		solicitud.idSucursal = 1;
		grupo.idGrupo = 1;
		ciclo.idCiclo = 1;
		
		try{
			double montoSinComision	= HTMLHelper.getParameterDouble(request, "montoAutorizado");
			double pagoUnitario 	= HTMLHelper.getParameterDouble(request, "pagoUnitario");
			int plazoAutorizado 	= HTMLHelper.getParameterInt(request, "plazoAutorizado");
			int frecuenciaPago 		= HTMLHelper.getParameterInt(request, "frecuenciaPago");
			int operacion 			= HTMLHelper.getParameterInt(request, "operacion");
			String fecha 			= HTMLHelper.getParameterString(request, "fecha");
			double montoConComision = montoSinComision;
			Date fechaInicio = null;
			
			if ( fecha == null )
				fechaInicio = cal.getTime();
			else
				fechaInicio = Convertidor.stringToDate(fecha);
			
			//System.out.println("Parámetros Tasa logaritmo: " + montoSinComision + " " + pagoUnitario + " " + plazoAutorizado + " " + frecuenciaPago);
			Double tasaLogaritmo = 	TablaAmortizacionHelper.getTasaLogaritmico (montoSinComision, pagoUnitario, plazoAutorizado, frecuenciaPago, 0.00);
			//System.out.println("Tasa logaritmo: " + tasaLogaritmo);
			Double tasaDiaria = TablaAmortizacionHelper.calcTasa(operacion, montoSinComision, pagoUnitario, plazoAutorizado, frecuenciaPago, Convertidor.dateToString(fechaInicio), 1, tasaLogaritmo);
			//System.out.println("Tasa: " + tasaDiaria);

			if( operacion == ClientesConstants.CONSUMO ){
                            System.out.println("_______________________________"+request.getRemoteUser()+" CommandGeneraAmortizacion/CONSUMO"+" solicitud.idCliente "+solicitud.idCliente+" solicitud.idSolicitud "+solicitud.idSolicitud);
                            tabladao.delTablaAmortizacion(cliente.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
				tabla = TablaAmortizacionHelper.insertTablaInsolutoConsumo(cliente, solicitud, montoConComision, montoSinComision, pagoUnitario, plazoAutorizado, frecuenciaPago, Convertidor.dateToString(fechaInicio), tasaDiaria);
			}
			else if( operacion == ClientesConstants.MICROCREDITO ){
                            System.out.println("_______________________________"+request.getRemoteUser()+" CommandGeneraAmortizacion/MICROCREDITO"+" solicitud.idCliente "+solicitud.idCliente+" solicitud.idSolicitud "+solicitud.idSolicitud);
                            tabladao.delTablaAmortizacion(cliente.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                            tabla = TablaAmortizacionHelper.insertTablaInsolutoMicro(cliente, solicitud, montoConComision, montoSinComision, pagoUnitario, plazoAutorizado, frecuenciaPago, Convertidor.dateToString(fechaInicio), tasaDiaria);
			}
			else if( operacion == ClientesConstants.GRUPAL ){
                            System.out.println("_______________________________"+request.getRemoteUser()+" CommandGeneraAmortizacion/GRUPAL"+" solicitud.idCliente "+solicitud.idCliente+" solicitud.idSolicitud "+solicitud.idSolicitud);
                            tabladao.delTablaAmortizacion(grupo.idGrupo, ciclo.idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                            tabla = TablaAmortizacionHelper.insertTablaInsolutoComunal(grupo, ciclo, pagoUnitario, frecuenciaPago, Convertidor.dateToString(fechaInicio), tasaDiaria);
			}
			request.setAttribute("tablaAmortizacion", tabla);
			request.setAttribute("tasa", FormatUtil.roundDecimal(tasaDiaria, 6));
			if ( operacion == ClientesConstants.CONSUMO )
				request.setAttribute("elementos",TablaAmortizacionHelper.getDaysBetweenPayments(plazoAutorizado,frecuenciaPago, Convertidor.dateToString(fechaInicio)));
			else if( operacion == ClientesConstants.MICROCREDITO )
				request.setAttribute("elementos",TablaAmortizacionHelper.getDaysBetweenPaymentsMicro(plazoAutorizado,frecuenciaPago, Convertidor.dateToString(fechaInicio)));			
			
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

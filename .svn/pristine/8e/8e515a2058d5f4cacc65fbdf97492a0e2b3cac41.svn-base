package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.SellFinanceDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SaldosHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;


public class CommandGuardaCreditoSolicitado implements Command{


	private String siguiente;


	public CommandGuardaCreditoSolicitado(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		boolean esDistintoEstatus = false;
		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		SolicitudDAO solicituddao = new SolicitudDAO();
		SellFinanceDAO sellFinanceDAO = new SellFinanceDAO();
		solicitud.scoring = new ScoringVO();
		Calendar calendario = Calendar.getInstance();
		CreditoCartVO  credito		= new CreditoCartVO();
		CreditoCartDAO creditoDAO	= new CreditoCartDAO();
		TransaccionesHelper transacHelper = new TransaccionesHelper();
		Connection conn  = null;

		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE"); 
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			int estatusDesembolsoAnterior = cliente.solicitudes[indiceSolicitud].desembolsado;
			String numCheque = cliente.solicitudes[indiceSolicitud].numCheque;
			
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaCreditoSolicitado");
			//Actualizar solicitud
			int estatus = cliente.solicitudes[indiceSolicitud].estatus;
			boolean isNull = cliente.solicitudes[indiceSolicitud].sellFinance == null;
			System.out.println(isNull);
			solicitud = SolicitudHelper.getCreditoSolicitado(cliente.solicitudes[indiceSolicitud], request);
			if( estatus!=cliente.solicitudes[indiceSolicitud].estatus )
				esDistintoEstatus = true;
			if(solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE){
				if ( isNull )
					sellFinanceDAO.addSellFinance(solicitud.sellFinance);
				else
					sellFinanceDAO.updateSellFinance(solicitud.sellFinance);
			}
			if ( solicitud.estatus==ClientesConstants.SOLICITUD_EN_ANALISIS ){
				if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE){
					if ( ClientesUtil.clienteCompletoAnalisisDescuento(cliente, indiceSolicitud, notificaciones) ){
						solicituddao.updateSolicitud(cliente.idCliente, solicitud);
						cliente.solicitudes[indiceSolicitud] = solicitud;
						bitutil.registraEvento(solicitud);
						if(esDistintoEstatus)
							bitutil.registraCambioEstatus(solicitud, request);
						notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente"));
					}
				} else {
					if ( ClientesUtil.clienteCompletoAnalisisConsumo(cliente, indiceSolicitud, notificaciones) ){
						solicituddao.updateSolicitud(cliente.idCliente, solicitud);
						cliente.solicitudes[indiceSolicitud] = solicitud;
						bitutil.registraEvento(solicitud);
						if(esDistintoEstatus)
						bitutil.registraCambioEstatus(solicitud, request);
						notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente"));
					}
				}
			}else{
				if ( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){
//					if ( solicitud.seguro==null ){
//						notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Seguro de vida antes de realizar el desembolso") );
//						cliente.solicitudes[indiceSolicitud].desembolsado = estatusDesembolsoAnterior;
//					}
//					else{
						synchronized(this){
							if( !SolicitudHelper.asignaFechaDesembolso(conn, solicitud, cliente, request) ){
								conn.rollback();
								notificaciones.add ( new Notification(ClientesConstants.INFO_TYPE, "El cliente cuenta con una Orden de Pago Activa.") );
								request.setAttribute("NOTIFICACIONES", notificaciones);
								return siguiente;
							}
						}
						if( HTMLHelper.getCheckBox(request, "regeneraTabla") ){
							TablaAmortizacionHelper.actualizaTablaAmortizacion(cliente, idSolicitud);
							solicitud.tasaCalculada = cliente.solicitudes[indiceSolicitud].tasaCalculada;
						}
						if ( SolicitudHelper.requiereNumeroCheque(solicitud.tipoOperacion) && solicitud.numCheque.equalsIgnoreCase("0") ){
							notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron cheques para asignar") );
							cliente.solicitudes[indiceSolicitud].desembolsado = estatusDesembolsoAnterior;
							cliente.solicitudes[indiceSolicitud].numCheque = numCheque;
						}else{
/*
 * Cambio para que no registre en IBS							
 */
//							if ( SolicitudHelper.registerIBS( solicitud, cliente, request, notificaciones ) ){
							if ( CreditoHelperCartera.registraCreditoCartera( solicitud, cliente, request, notificaciones ) ){
//								TablaAmortHelper.insertTablaInsolutoMicro(cliente, solicitud,solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.montoSinComision, solicitud.cuota, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), solicitud.tasaCalculada);
								if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE){
									TablaAmortHelper.insertTablaDescuentoNomina(cliente, solicitud, solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.montoSinComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, solicitud.tasaCalculada, calendario.getTime());						
									
								} else {
									TablaAmortHelper.insertTablaInsolutoIndivSemanal(cliente, solicitud,solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.montoSinComision, solicitud.cuota, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), solicitud.tasaCalculada);									
								}
								EventoHelper.registraDesembolso( solicitud, cliente, request, notificaciones );
								SaldosHelper.insertSaldo(solicitud, cliente, request, notificaciones);
								solicituddao.updateSolicitud(cliente.idCliente, solicitud);
								cliente.solicitudes[indiceSolicitud] = solicitud;
// Se registra la transaccion de desembolso
								credito = creditoDAO.getCreditoClienteSol(cliente.idCliente, solicitud.idSolicitud);
								TransaccionesHelper.registraDesembolsoIndividual(credito, request, notificaciones);
								
								bitutil.registraEvento(solicitud);
								conn.commit();
								if( esDistintoEstatus )
									bitutil.registraCambioEstatus(solicitud, request);
								notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente"));
							}else
								cliente.solicitudes[indiceSolicitud].desembolsado = estatusDesembolsoAnterior;
						}
//					}
					if ( solicitud.sellFinance!=null ){
						if(solicitud.sellFinance.numeroFactura==null){
						notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el numero de empleado en el area de credito solicitado") );
						cliente.solicitudes[indiceSolicitud].desembolsado = estatusDesembolsoAnterior;
						}
					}
				}else{
					if( HTMLHelper.getCheckBox(request, "regeneraTabla") ){
						TablaAmortizacionHelper.actualizaTablaAmortizacion(cliente, idSolicitud);
						solicitud.tasaCalculada = cliente.solicitudes[indiceSolicitud].tasaCalculada;
					}
					if ( solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS && solicitud.decisionComite==null ){
						solicitud.estatus = ClientesConstants.SOLICITUD_EN_ANALISIS;
						esDistintoEstatus = true;
					}
					solicituddao.updateSolicitud(cliente.idCliente, solicitud);
					cliente.solicitudes[indiceSolicitud] = solicitud;
					bitutil.registraEvento(solicitud);
					if(esDistintoEstatus)
						bitutil.registraCambioEstatus(solicitud, request);
					notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente"));
				}
			}

			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza la solicitud del objeto cliente
			
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}finally{
			try {
				if ( conn!=null ) conn.close();
			}catch(SQLException e) {
				throw new CommandException(e.getMessage());
			}
		}
		return siguiente;
	}

}

package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SellFinanceDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DecisionComiteHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TasaInteresVO;


public class CommandAutorizaSellFinance implements Command{


	private String siguiente;


	public CommandAutorizaSellFinance(String siguiente) {
		this.siguiente = siguiente;
	}

	
	
	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		DecisionComiteVO  decisionComite = new DecisionComiteVO();
		ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
		DecisionComiteDAO decisionComitedao = new DecisionComiteDAO();
		SolicitudDAO solicituddao = new SolicitudDAO();
		SellFinanceDAO sellFinanceDAO = new SellFinanceDAO();
		ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
		Connection conn  = null;
		Calendar cal = Calendar.getInstance();
		int plazoAutorizado = 0;
		int frecuenciaPago = 0;
		
//		UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
		boolean actualizaSolicitud = true;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO clienteActual = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(clienteActual.idCliente, request.getRemoteUser(), "CommandGuardaAutorizaSellFinance");
			solicitud = clienteActual.solicitudes[indiceSolicitud];
			
			decisionComite = solicitud.decisionComite;
			TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
			TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);

			if ( decisionComite==null ){
				decisionComite = DecisionComiteHelper.getDecisionVO(new DecisionComiteVO(), solicitud, request);
// Se cambia la llamada a descuento nomina en lugar de sell finance				
//				if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE && ClientesUtil.clienteCompletoConsumo(clienteActual, indiceSolicitud, notificaciones) && ClientesUtil.tasasAplicaPlan(request, notificaciones)) {
				if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE && ClientesUtil.clienteCompletoDescuento(clienteActual, indiceSolicitud, notificaciones) && ClientesUtil.tasasAplicaPlanDescuentoNomina(request, notificaciones)) {
					decisionComitedao.addDecisionComite(conn, decisionComite);
					notificaciones.add( new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente") );
					solicitud.decisionComite = decisionComite;
					//solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
					solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
					synchronized(this){
						if( !SolicitudHelper.asignaFechaDesembolso(conn, solicitud, clienteActual, request) ){
							conn.rollback();
							notificaciones.add ( new Notification(ClientesConstants.INFO_TYPE, "El cliente cuenta con una Orden de Pago Activa.") );
							request.setAttribute("NOTIFICACIONES", notificaciones);
							return siguiente;
						}
					}
					//Si la solicitud es de consumo se preparan los elementos para calcular tabla de amortizacion

					if((solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE)  && (solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite!=ClientesConstants.CREDITO_RECHAZADO )){
						double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
						TasaInteresVO tasa = (TasaInteresVO)catTasas.get(decisionComite.tasa);
						
						decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
						decisionComitedao.updateDecisionComite(conn, decisionComite);
						solicitud.decisionComite = decisionComite;
						solicitud.tasaCalculada  = tasa.valor;
						plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
						frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
						//Genera la tabla de amortización
						
//						TablaAmortizacionHelper.insertTablaConsumo(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal , decisionComite.montoAutorizado, montoSinComision, plazoAutorizado, frecuenciaPago, tasa.valor, cal.getTime());
						TablaAmortizacionHelper.insertTablaDescuentoNomina(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal , decisionComite.montoAutorizado, montoSinComision, plazoAutorizado, frecuenciaPago, tasa.valor, cal.getTime());
						solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
					}
					if ( sellFinanceDAO.updateSellFinance(solicitud.sellFinance)!=1)
						sellFinanceDAO.addSellFinance(solicitud.sellFinance);
					
					pagoReferenciaVO.referencia   = ClientesUtil.makeReferencia(clienteActual, idSolicitud);
					pagoReferenciaVO.numcliente   = clienteActual.idCliente;
					pagoReferenciaVO.numSolicitud = idSolicitud;
					pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
					solicitud.referencia = pagoReferenciaVO.referencia;
					solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
					conn.commit();
					bitutil.registraEvento(solicitud);
					bitutil.registraEvento(decisionComite);
					bitutil.registraEvento(solicitud.sellFinance);
				// JBL DIC/10 se añade para que se pueda rechazar en forma directa sin pasar por la aprobacion	
				} else if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE && ClientesUtil.clienteCompletoDescuento(clienteActual, indiceSolicitud, notificaciones) &&  decisionComite.decisionComite==ClientesConstants.CREDITO_RECHAZADO ){
					decisionComitedao.addDecisionComite(conn, decisionComite);					
				}
				
			}else{
				decisionComite = DecisionComiteHelper.getDecisionVO(decisionComite, solicitud, request);
				if ( HTMLHelper.getParameterInt(request, "desembolsado")==ClientesConstants.DESEMBOLSADO /*&& solicitud.seguro==null*/ ){
					notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"Debe capturar la sección Seguro de vida antes de realizar el desembolso") );
				}else{
					//Asignación de cheque para los productos: MicroCrédito
					solicitud.decisionComite = decisionComite;
					//solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
					solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
					synchronized(this){
						SolicitudHelper.asignaFechaDesembolso(conn, solicitud, clienteActual, request);
					}
					//solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
					//Si la solicitud es de consumo se preparan los elementos para calcular tabla de amortización
					TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();

// Se cambia de sell finance a descuento nomina JBL OCT/10
//					if((solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE)  && (solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite!=ClientesConstants.CREDITO_RECHAZADO && ClientesUtil.tasasAplicaPlan(request, notificaciones))){
					if((solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE)  && (solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite!=ClientesConstants.CREDITO_RECHAZADO && ClientesUtil.tasasAplicaPlanDescuentoNomina(request, notificaciones))){
						double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
						TasaInteresVO tasa = (TasaInteresVO)catTasas.get(decisionComite.tasa);
						decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
						decisionComitedao.updateDecisionComite(conn, decisionComite);
						solicitud.decisionComite = decisionComite;
						plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
						frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
						if ( sellFinanceDAO.updateSellFinance(solicitud.sellFinance)!=1)
							sellFinanceDAO.addSellFinance(solicitud.sellFinance);
						//Borra la información de la tabla que se generá previamente
                                                System.out.println("_______________________________"+request.getRemoteUser()+" CommandAutorizaSellFinance"+" solicitud.idCliente "+solicitud.idCliente+" solicitud.idSolicitud "+solicitud.idSolicitud);
						tablaDAO.delTablaAmortizacion(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
						//Genera la nueva tabla de amortización
						
//						TablaAmortizacionHelper.insertTablaConsumo(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal , decisionComite.montoAutorizado, montoSinComision, plazoAutorizado, frecuenciaPago, tasa.valor, cal.getTime());						
						TablaAmortizacionHelper.insertTablaDescuentoNomina(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal , decisionComite.montoAutorizado, montoSinComision, plazoAutorizado, frecuenciaPago, tasa.valor, cal.getTime());						
						solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
					}
					
					if ( decisionComite.decisionComite==ClientesConstants.CREDITO_RECHAZADO ){
						decisionComitedao.updateDecisionComite(conn, decisionComite);
					}
					
// Se cambia sell finance por descuento nómina
//					if ( actualizaSolicitud && ClientesUtil.tasasAplicaPlan(request, notificaciones)){
					if ( actualizaSolicitud && ClientesUtil.tasasAplicaPlanDescuentoNomina(request, notificaciones)){
						solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
						conn.commit();
						bitutil.registraEvento(solicitud);
						bitutil.registraEvento(decisionComite);
						bitutil.registraEvento(solicitud.sellFinance);
						notificaciones.add( new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente") );
					}else{
						conn.rollback();
					}
				}
			}
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza la solicitud del objeto cliente
			clienteActual.solicitudes[indiceSolicitud] = solicitud;
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", clienteActual);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}finally {
			try {
				if ( conn!=null ) conn.close();
			}
			catch(SQLException sqle) {
				throw new CommandException(sqle.getMessage());
			}
		}
		return siguiente;
	}

}
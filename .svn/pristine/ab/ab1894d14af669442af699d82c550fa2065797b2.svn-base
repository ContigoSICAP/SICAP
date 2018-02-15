package com.sicap.clientes.commands;

import java.sql.Connection;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DescongelaPagoGarantiaIndVO;
import com.sicap.clientes.vo.cartera.RubrosVO;


public class CommandDescongelaPagoGarantiaInd implements Command{

	private String siguiente;

	public CommandDescongelaPagoGarantiaInd(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		//ArrayList<DescongelaAhorroVO> listaMontoCongelado = null;
		CreditoCartVO creditoVO = new CreditoCartVO();
		CreditoCartDAO creditoDAO = new CreditoCartDAO();
		GrupoVO 	grupoCiclo = new GrupoVO();
		GrupoDAO 	grupoDAO 	= new GrupoDAO();
		OrdenDePagoVO oPago		= null;
		SaldoIBSVO	  saldo		= new SaldoIBSVO();
		SaldoIBSDAO   saldoDAO  = new SaldoIBSDAO();
		IntegranteCicloVO integrante = new IntegranteCicloVO();
		IntegranteCicloDAO integrantesDAO = new IntegranteCicloDAO();
		PagoIndividualGruposVO   	pagosVO	  = new PagoIndividualGruposVO();
		PagoIndividualGruposDAO  	pagosDAO  = new PagoIndividualGruposDAO();
		DescongelaPagoGarantiaIndVO pagoInd	  = new DescongelaPagoGarantiaIndVO();
		SucursalDAO					sucursalDAO = new SucursalDAO();
		ClienteVO		cliente					  = new ClienteVO();
		ClienteDAO		clienteDAO				  = new ClienteDAO();
		EventoHelper	eventoHelp				  = new EventoHelper();
		TransaccionesHelper	transacHelp			  = new TransaccionesHelper();
		int idGrupo = 0;
		int idCiclo = 0;
		int idCliente = 0;
		int contador = 0;
		int numPago     = 0;
		String nombre_completo;
		double monto_pago_ind = 0.00;
		String numcheque = "";
		Connection conn  = null;
		
		
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			
			//grupoCiclo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			Calendar c1 = Calendar.getInstance();
			String fecha   = "";
			fecha = Convertidor.dateToString(c1.getTime());
			idGrupo      = HTMLHelper.getParameterInt(request, "idGrupo");
			idCiclo      = HTMLHelper.getParameterInt(request, "idCiclo");
			idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			contador = HTMLHelper.getParameterInt(request, "numGrupo");
			
			if (contador == 0){
				grupoCiclo = grupoDAO.getGrupo(idGrupo);
				creditoVO = creditoDAO.getCreditoClienteSolLiq(idGrupo, idCiclo);
				if (creditoVO != null){
// Se busca al integrante en el ciclo 
					integrante = integrantesDAO.getIntegranteCicloPorClienteCiclo(idGrupo, idCliente, idCiclo);
					if (integrante != null){
// Se busca que el integrante cuente con un pago de deposito en garantia
						pagosVO = pagosDAO.getPagoIndividualGrupos(idGrupo, idCiclo, idCliente, numPago);
						if (pagosVO != null){
							cliente  = clienteDAO.getCliente(idCliente);
							nombre_completo = ClienteHelper.getNombreCompleto(cliente);
							pagoInd = GrupoHelper.getPagoIndividual(creditoVO, pagosVO, request.getRemoteUser().toString(),grupoCiclo.nombre, nombre_completo,fecha);
							request.setAttribute("LISTAPAGOS",pagoInd);
							request.setAttribute("DATOSGRUPO",grupoCiclo);
							Logger.debug("Pago Encontrado");
							notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Cliente con deposito en garantía encontrado");
							request.setAttribute("NOTIFICACIONES",notificaciones);							
							
							// Se genera orden de pago o cheque de acuerdo a si tiene banco asignado
						} else {
							Logger.debug("Pago no Encontrado");
							notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro deposito en garantía para este cliente");
							request.setAttribute("NOTIFICACIONES",notificaciones);							
							
						}
					} else {
						Logger.debug("Cliente no Encontrado");
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro el cliente dentro del grupo y ciclo indicados");
						request.setAttribute("NOTIFICACIONES",notificaciones);							
						
					}
				} else {
					Logger.debug("Grupo y Ciclo no Encontrado o no cuentan con un credito liquidado");
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro un credito liquidado con el grupo y ciclo indicados");
					request.setAttribute("NOTIFICACIONES",notificaciones);							
					
				}
			}
			else {
				
				// Se obtienen los parametros
				idGrupo = HTMLHelper.getParameterInt(request, "numGrupo");
				idCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
				idCliente = HTMLHelper.getParameterInt(request, "numCliente");
				monto_pago_ind = HTMLHelper.getParameterDouble(request, "monto_ind");
				// Se obtienen el cliente y el credito
				grupoCiclo = grupoDAO.getGrupo(idGrupo);
				cliente  = clienteDAO.getCliente(idCliente);
				creditoVO = creditoDAO.getCreditoClienteSol(idGrupo, idCiclo);
				// se obtiene el monto del pago individual y se resta de los monto cuenta y monto cuenta congelado
				pagosVO = pagosDAO.getPagoIndividualGrupos(idGrupo, idCiclo, idCliente, numPago);
				integrante = integrantesDAO.getIntegranteCicloPorClienteCiclo(idGrupo, idCliente, idCiclo);
				nombre_completo = ClienteHelper.getNombreCompleto(cliente);
				pagoInd = GrupoHelper.getPagoIndividual(creditoVO, pagosVO, request.getRemoteUser().toString(),grupoCiclo.nombre, nombre_completo,fecha);
				RubrosVO             rubros		= new RubrosVO();
				int idBanco = sucursalDAO.getSucursal( cliente.idSucursal ).idBanco;
				// si existe banco asignado se crea una orden de pago
				if (idBanco > 0){
					String referencia = ClientesUtil.makeReferenciaDev(cliente , integrante.idSolicitud);
					Logger.debug("referencia "+referencia+" longitud "+referencia.length());					
					OrdenDePagoVO ordenesDePagoActuales = new OrdenDePagoDAO().getOrdenDePago(referencia);
					if (ordenesDePagoActuales != null){
						if (ordenesDePagoActuales.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA ){
							ordenesDePagoActuales = null;
						}
					}
					// En caso de que no exista una orden de pago de devolucion se continua
					if (ordenesDePagoActuales == null){
						oPago = new OrdenDePagoVO();
						oPago.setIdCliente  ( cliente.idCliente );
						oPago.setIdSolicitud( integrante.idSolicitud );
						oPago.setIdSucursal ( cliente.idSucursal );
						oPago.setUsuario    ( request.getRemoteUser() );
						oPago.setNombre     ( nombre_completo );
						oPago.setMonto      ( monto_pago_ind );
						oPago.setIdBanco    ( idBanco );
						oPago.setReferencia ( referencia );
						oPago.setEstatus    ( ClientesConstants.OP_DESEMBOLSADO );
						numcheque = ChequerasHelper.asignaOrdenDePago(conn, oPago);
						conn.commit();

						// Forma los rubros de la transaccion
						rubros.tipoRubro = ClientesConstants.EFECTIVO;
						rubros.monto	 = monto_pago_ind;
						rubros.status	 = ClientesConstants.RUBRO_VIGENTE;							
						transacHelp.registraDevolucion(creditoVO, rubros, Convertidor.stringToDate(fecha));
                                                 if (creditoVO.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                                                     transacHelp.registraDevolucionBursa(creditoVO,Convertidor.toSqlDate(Convertidor.stringToDate(fecha)),monto_pago_ind,0);
                                                 }
						// Registra el evento
						eventoHelp.registraDevolucion(pagoInd);
						// Actualiza la tabla de credito
						creditoVO.setMontoCuentaCongelada(creditoVO.getMontoCuentaCongelada() - monto_pago_ind);
						creditoVO.setMontoCuenta(creditoVO.getMontoCuenta() - monto_pago_ind);
						creditoDAO.updatePagoCredito(creditoVO);
						// Actualiza la tabla de saldos
						saldo = saldoDAO.getSaldo(creditoVO.getNumCliente(), creditoVO.getNumCredito());
						saldo.setSaldoBucket(saldo.getSaldoBucket() - monto_pago_ind);
						saldoDAO.updateSaldo(saldo);
						// Se carga la notificacion
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Se actualiza el monto de cuenta congelado y se genera la orden de pago");
						request.setAttribute("NOTIFICACIONES",notificaciones);														
					} else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"Existe una orden de pago de devolución activa para este cliente");
						request.setAttribute("NOTIFICACIONES",notificaciones);																
					}
				} else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No existe banco asignado para esta sucursal");
					request.setAttribute("NOTIFICACIONES",notificaciones);																								
				}
												
			}
			request.setAttribute("NOTIFICACIONES",notificaciones);
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

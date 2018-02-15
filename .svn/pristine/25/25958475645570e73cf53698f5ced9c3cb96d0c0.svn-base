package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;

public class CommandCancelaCheques implements Command{


	private String siguiente;


	public CommandCancelaCheques(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		int res = 1;
		String chequeNuevo = "";
		ChequesDAO chequedao = new ChequesDAO();
		ChequeVO cheque = new ChequeVO();
		SolicitudVO solicitud = new SolicitudVO();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		IntegranteCicloDAO integrantedao = new IntegranteCicloDAO();
		Notification notificaciones[] = new Notification[1];
		Connection conn  = null;
		Calendar cal = Calendar.getInstance();
		
		try{
			int numCheque = HTMLHelper.getParameterInt(request,"cheque");
			int lote = HTMLHelper.getParameterInt(request,"lote");
			int idCliente = HTMLHelper.getParameterInt(request,"cliente");
			int idSolicitud = HTMLHelper.getParameterInt(request,"solicitud");
			int idSucursal = HTMLHelper.getParameterInt(request,"idSucursal");
			int motivoCancel = HTMLHelper.getParameterInt(request,"motivo");
			String fechaAsignacion = HTMLHelper.getParameterString(request,"fecha");
			String comentarios = request.getParameter("comentarios");
			String tipoCancelacion = request.getParameter("cancel");
			int idGrupo = HTMLHelper.getParameterInt(request,"grupo");
			int idCiclo = HTMLHelper.getParameterInt(request,"ciclo");
			int tipoOperacion = HTMLHelper.getParameterInt(request,"tipooperacion");
			TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(tipoOperacion);
			
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			
			BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandCancelaCheques");
			//Agrega datos a objeto para la bitácora
			cheque.numCheque = numCheque;
			cheque.numLote = lote;
			cheque.numCliente = idCliente;
			cheque.numSolicitud = idSolicitud;
			cheque.numGrupo = idGrupo;
			cheque.numCiclo = idCiclo;
			cheque.estatus = ClientesConstants.CHEQUE_CANCELADO;
			cheque.fechaAsignacion = Convertidor.stringToSqlDate(fechaAsignacion);
			cheque.fechaCancelacion = Convertidor.toSqlDate(cal.getTime());
			cheque.tipoCancelacion = motivoCancel;
			cheque.comentarios = comentarios;
			
			//Se cancela cheque por desistimiento del crédito
			if ( tipoCancelacion!=null && tipoCancelacion.equals("desiste") ){
				if( !SolicitudUtil.creditoEnviado(idCliente, idSolicitud, tipoOperacion) || request.isUserInRole("SOPORTE_OPERATIVO") ){
					if ( tipoOperacion == ClientesConstants.GRUPAL ){
						solicitud = new SolicitudDAO().getSolicitud(idCliente, idSolicitud);
						solicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
						solicitud.fechaDesembolso = null;
						solicitud.numCheque = null;
						res = chequedao.updateCheque(conn, cheque);
						res = new SolicitudDAO().updateSolicitud(conn, idCliente, solicitud);
						//res = clienteDAO.updateGrupo(idCliente, 0);
						ciclo = new CicloGrupalDAO().getCiclo(idGrupo, idCiclo);
						if( ciclo.idTipoCiclo != ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO ){
							res = integrantedao.deleteIntegrante(conn, idGrupo, idCiclo, idCliente);
							ciclo.integrantes = integrantedao.getIntegrantes(conn, idGrupo, idCiclo);
							ciclo.montoConComision = 0;
							ciclo.monto = 0;
							for ( int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){
								ciclo.montoConComision+=ciclo.integrantes[i].monto;
								ciclo.monto += ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal );
//								Sumando Prima del Seguro
								ciclo.monto += ciclo.integrantes[i].primaSeguro;
								ciclo.montoConComision += ClientesUtil.calculaMontoConComision( ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal );
							}
						}else{
							ciclo.integrantes = integrantedao.getIntegrantes(idGrupo, idCiclo);
							GrupoUtil.procesaCancelaDesembolsoRefinanciamiento(conn, ciclo, idCliente);
						}
						GrupoVO grupo = new GrupoDAO().getGrupo(idGrupo);
						//DecisionComiteVO decision = new DecisionComiteDAO().getDecisionComite(idCliente, idSolicitud);
						TablaAmortizacionVO[] tabla = new TablaAmortizacionDAO().getElementos(idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
						//int plazo = tabla.length-1;
						Date fechaInicio = new Date();
						if ( tabla!=null && tabla.length>0)
							fechaInicio = tabla[0].fechaPago;
                                                System.out.println("_______________________________"+request.getRemoteUser()+" CommandCancelaCheques"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
						GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, fechaInicio);
						if (res==1){
							new CicloGrupalDAO().updateCiclo(conn, ciclo);
							conn.commit();
							notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"La operación se realizó con éxito");
							bitutil.registraEvento(solicitud);
							bitutil.registraEvento(cheque);
						}
						else
							conn.rollback();
					}else{
						res = chequedao.updateCheque(conn, cheque);
						solicitud = new SolicitudDAO().getSolicitud(idCliente, idSolicitud);
						solicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
						solicitud.fechaDesembolso = null;
						solicitud.numCheque = null;
						res = new SolicitudDAO().updateSolicitud(conn, idCliente, solicitud);
						if ( res==1 ){
							conn.commit();
							bitutil.registraEvento(solicitud);
							bitutil.registraEvento(cheque);
							notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"La operación se realizó con éxito, se canceló el cheque: " + numCheque);					
						}
						else
							conn.rollback();
					}
				}else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se puede cancelar el cheque debido a que el cliente ha sido enviado al sistema de cartera");
				}
			//Se cancela cheque por sustitución	
			}else{
				conn = ConnectionManager.getMySQLConnection();
				conn.setAutoCommit(false);
				res = chequedao.updateCheque(conn, cheque);
				synchronized(this){
					chequeNuevo = ChequerasHelper.asignaCheque(conn, idSucursal, idCliente, idSolicitud, idGrupo, idCiclo);
				}
				res = new SolicitudDAO().updateChequeSolicitud(conn, idCliente, idSolicitud, chequeNuevo);
				if ( !chequeNuevo.equals("0") && res == 1){
					request.setAttribute("CHEQUENUEVO", chequeNuevo);
					conn.commit();
					bitutil.registraEvento(solicitud);
					bitutil.registraEvento(cheque);
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"La operación se realizó con éxito, se asignó el siguiente número de cheque: " + chequeNuevo);
				}
				else{
					if ( chequeNuevo.equals("0") )
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontró cheque disponible para asignar");
					else
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE ,"Ocurrió un error en BD favor de reportarlo a HelpDesk");
				}
			}
			request.setAttribute("NOTIFICACIONES",notificaciones);
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

package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;

public class CommandCancelaChequesConfirmados implements Command{


	private String siguiente;


	public CommandCancelaChequesConfirmados(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		int res = 0;
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
			int motivoCancel = HTMLHelper.getParameterInt(request,"motivo");
			String fechaAsignacion = HTMLHelper.getParameterString(request,"fecha");
			String comentarios = request.getParameter("comentarios");
			String tipoCancelacion = request.getParameter("cancel");
			int idGrupo = HTMLHelper.getParameterInt(request,"grupo");
			int idCiclo = HTMLHelper.getParameterInt(request,"ciclo");
			int tipoOperacion = HTMLHelper.getParameterInt(request,"tipooperacion");
			boolean procesoOK = false;

			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);

			BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandCancelaChequesConfirmados");
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
						ciclo = new CicloGrupalDAO().getCiclo(idGrupo, idCiclo);
						if( ciclo.idTipoCiclo!=ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO ){
							int contadorIntegrantes = 0;
							ciclo.integrantes = integrantedao.getIntegrantes(conn, idGrupo, idCiclo);
							for( int i=0; i<ciclo.integrantes.length; i++ ){
								if(ciclo.integrantes[i].estatus != ClientesConstants.INTEGRANTE_CANCELADO)
									contadorIntegrantes ++;
							}
							if( contadorIntegrantes>10 ){
								solicitud = new SolicitudDAO().getSolicitud(idCliente, idSolicitud);
								solicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
								solicitud.fechaDesembolso = null;
								solicitud.numCheque = null;
								res = chequedao.updateCheque(conn, cheque);
								if( res==1 ){
									res = new SolicitudDAO().updateSolicitud(conn, idCliente, solicitud);
									if(res==1){
										IntegranteCicloVO integranteCiclo = integrantedao.getIntegranteCiclo( idCliente, solicitud.idSolicitud);
										integranteCiclo.estatus = ClientesConstants.INTEGRANTE_CANCELADO; 	
										res = integrantedao.updateIntegrante(conn, idGrupo, idCiclo, integranteCiclo);
										if(res==1){
											procesoOK = true;
										}
									}
								}
							}else{
								notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se puede cancelar el cheque, el ciclo debe contar con un mínimo de 10 integrantes");
							}
						}else{
							notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No es pocible cancelar el cheque");
						}

						if (procesoOK){
							conn.commit();
							notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"La operación se realizó con éxito");
							bitutil.registraEvento(solicitud);
							bitutil.registraEvento(cheque);
						}else{
							conn.rollback();
						}
					}
				}else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se puede cancelar el cheque debido a que el cliente ha sido enviado al sistema de cartera");
				}
				//Se cancela cheque por sustitución	
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

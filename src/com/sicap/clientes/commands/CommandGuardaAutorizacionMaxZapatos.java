package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DecisionComiteHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaAutorizacionMaxZapatos implements Command{


	private String siguiente;


	public CommandGuardaAutorizacionMaxZapatos(String siguiente) {
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
		ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
		Connection conn  = null;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO clienteActual = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(clienteActual.idCliente, request.getRemoteUser(), "CommandGuardaAutorizacionMaxZapatos");
			solicitud = clienteActual.solicitudes[indiceSolicitud];
			int estatusPrevio = solicitud.estatus;
			decisionComite = solicitud.decisionComite;
			TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);

			if ( decisionComite==null ){
				decisionComite = DecisionComiteHelper.getDecisionVO(new DecisionComiteVO(), solicitud, request);
				Logger.debug("montoAutorizado : "+decisionComite.montoAutorizado);
				Logger.debug("montoSinComision : "+decisionComite.montoSinComision);
				if (  ClientesUtil.clienteCompletoMaxZapatos(clienteActual, indiceSolicitud, notificaciones) ){
					decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(decisionComite.montoSinComision, decisionComite.comision, catComisiones, solicitud.tipoOperacion);
					Logger.debug("montoAutorizado : "+decisionComite.montoAutorizado);
					decisionComitedao.addDecisionComite(conn, decisionComite);
					notificaciones.add( new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente") );
					solicitud.decisionComite = decisionComite;
					//solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
					solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
					if(solicitud.tipoOperacion!=ClientesConstants.GRUPAL){
						pagoReferenciaVO.referencia   = ClientesUtil.makeReferencia(clienteActual, idSolicitud);
						pagoReferenciaVO.numcliente   = clienteActual.idCliente;
						pagoReferenciaVO.numSolicitud = idSolicitud;
						pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
						solicitud.referencia = pagoReferenciaVO.referencia;
					}
					solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
					conn.commit();
					bitutil.registraEvento(solicitud);
					bitutil.registraEvento(decisionComite);
					bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
				}
			}else{
				decisionComite = DecisionComiteHelper.getDecisionVO(decisionComite, solicitud, request);
				Logger.debug("montoAutorizado : "+decisionComite.montoAutorizado);
				Logger.debug("montoSinComision : "+decisionComite.montoSinComision);
				decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(decisionComite.montoSinComision, decisionComite.comision, catComisiones, solicitud.tipoOperacion);
				Logger.debug("montoAutorizado : "+decisionComite.montoAutorizado);
				solicitud.decisionComite = decisionComite;
				//solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
				solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
				decisionComitedao.updateDecisionComite(conn, decisionComite);
				solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
				conn.commit();
				bitutil.registraEvento(solicitud);
				bitutil.registraEvento(decisionComite);
				bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
				notificaciones.add( new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente") );
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
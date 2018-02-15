package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.ObligadoSolidarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ObligadoSolidarioHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.ObligadoSolidarioVO;


public class CommandGuardaDatosObligadoSolidario implements Command{


	private String siguiente;


	public CommandGuardaDatosObligadoSolidario(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		String paginaSiguiente = siguiente;
		int idSolicitud = 0;
		int idObligado = 0;
		ClienteVO cliente = new ClienteVO();
		ObligadoSolidarioVO obligado = new ObligadoSolidarioVO();
		ObligadoSolidarioDAO obligadodao = new ObligadoSolidarioDAO();
		DireccionVO direccion = null;
		DireccionDAO direcciondao = new DireccionDAO();
		Connection conn  = null;
		try{
			cliente = (ClienteVO)session.getAttribute("CLIENTE");
			idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			idObligado = HTMLHelper.getParameterInt(request, "idObligado");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaDatosObligadoSolidario");
			if ( idObligado==0 ){
				obligado = ObligadoSolidarioHelper.getVO(new ObligadoSolidarioVO(), request);
				obligado.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				direccion = DireccionHelper.getVO(new DireccionVO(), request);
				obligado.direccion = direccion;

				if ( obligado.rfc!=null && !obligadodao.obligadoDisponible(cliente.idCliente, idSolicitud, obligado.idObligado, obligado.rfc) ){
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El RFC existe para obligado solidario");
					paginaSiguiente = "/capturaObligadoSolidario.jsp";
					request.setAttribute("OBLIGADO_TEMPORAL", obligado);
				}else{
					conn = ConnectionManager.getMySQLConnection();
					conn.setAutoCommit(false);
					idObligado = obligadodao.addObligadoSolidario(conn, cliente.idCliente, idSolicitud, obligado);
					if ( direccion.idColonia!=0 ){
						direcciondao.addDireccion(conn, cliente.idCliente, idSolicitud, "d_obligados_solidarios",idObligado, direccion);
					}
					conn.commit();
					cliente.solicitudes[indiceSolicitud].obligadosSolidarios = obligadodao.getObligadosSolidarios(cliente.idCliente, idSolicitud);
					for ( int i=0 ; i<cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length ; i++ ){
						int idObligadoSol = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].idObligado;
						cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].direccion = direcciondao.getDireccion(cliente.idCliente, idSolicitud, "d_obligados_solidarios", idObligadoSol);
					}
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
					bitutil.registraEvento(obligado);
				}
			}else{
				obligado = ObligadoSolidarioHelper.getVO(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1], request);
				if ( obligado.rfc!=null && !obligadodao.obligadoDisponible(cliente.idCliente, idSolicitud, obligado.idObligado, obligado.rfc) ){
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El RFC existe para obligado solidario");
					paginaSiguiente = "/capturaObligadoSolidario.jsp";
					request.setAttribute("OBLIGADO_TEMPORAL", obligado);
				}else{
					conn = ConnectionManager.getMySQLConnection();
					conn.setAutoCommit(false);
					direccion = DireccionHelper.getVO(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion, request);
					obligadodao.updateObligadoSolidario(conn, cliente.idCliente, idSolicitud, idObligado, obligado);
					direcciondao.updateDireccion(conn, cliente.idCliente, idSolicitud, "d_obligados_solidarios", idObligado, direccion);
					conn.commit();
					//cliente.solicitudes[indiceSolicitud].obligadosSolidarios = obligadodao.getObligadosSolidarios(cliente.idCliente, idSolicitud);
					cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1] = obligado;
					for ( int i=0 ; i<cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length ; i++ ){
						int idObligadoSol = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].idObligado;
						cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].direccion = direcciondao.getDireccion(cliente.idCliente, idSolicitud, "d_obligados_solidarios", idObligadoSol);
					}
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
					bitutil.registraEvento(obligado);
				}
			}
			
			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		finally {
			try {
				if ( conn!=null ) conn.close();
			}
			catch(SQLException sqle) {
				throw new CommandException(sqle.getMessage());
			}
		}
		return paginaSiguiente;
	}


}

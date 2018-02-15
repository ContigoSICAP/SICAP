package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.NegocioDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ArchivosAsociadosHelper;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.NegocioVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaDatosAbreviada implements Command{

	private String siguiente;


	public CommandGuardaDatosAbreviada(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ClienteVO clienteActualizado = new ClienteVO();
		SolicitudVO solicitud = new SolicitudVO();
		DireccionVO direccion = new DireccionVO();
		ClienteDAO clientedao = new ClienteDAO();
		NegocioVO negocio = new NegocioVO();
		SolicitudDAO solicituddao = new SolicitudDAO();
		DireccionDAO direcciondao = new DireccionDAO();
		NegocioDAO negociodao = new NegocioDAO();
		Connection conn  = null;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO clienteActual = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
//			Adiciona la informacion capturada al cliente
			clienteActualizado = ClienteHelper.getVO(clienteActual, request);
//			Persiste los cambios en base de datos
			clientedao.updateCliente(conn, clienteActualizado);
			BitacoraUtil bitutil = new BitacoraUtil(clienteActualizado.idCliente, request.getRemoteUser(), "CommandGuardaDatosAbreviada");
			
			if ( idSolicitud==0 ){
				//Nueva solicitud
				solicitud = SolicitudHelper.getVO(new SolicitudVO(), request);
				direccion = DireccionHelper.getVO(new DireccionVO(), request);
				solicitud.estatus = ClientesConstants.ESTATUS_CAPTURADO;

				if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
					negocio.situacionLocal = HTMLHelper.getParameterInt(request, "situacionLocal");
					negocio.razonSocial = "N/D";
					negociodao.addNegocio(conn, clienteActualizado.idCliente, idSolicitud, negocio);
					solicitud.negocio = negocio;
					bitutil.registraEvento( solicitud.negocio.toString() );
					solicitud.estatus = ClientesConstants.SOLICITUD_EN_ANALISIS;
				}
				idSolicitud = solicituddao.addSolicitud(conn, clienteActualizado.idCliente, solicitud);
				direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion);
				conn.commit();
				bitutil.registraCambioEstatus(solicitud, request);
				bitutil.registraEvento( clienteActualizado.toString() );
				bitutil.registraEvento( solicitud.toString() );
				bitutil.registraEvento( direccion.toString() );
				clienteActualizado.solicitudes = solicituddao.getSolicitudes(clienteActualizado.idCliente);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				//Actualizar solicitud
				solicitud = SolicitudHelper.getVO(clienteActual.solicitudes[indiceSolicitud], request);
				int estatusPrevio = solicitud.estatus;
				Logger.debug("solicitud :"+solicitud.toString());
				if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO && !ArchivosAsociadosHelper.existe(solicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION) ){
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El cliente debe contar con la autorizacion de consulta de buró para realizar la captura abreviada");
				}else{
					solicituddao.updateSolicitud(conn, clienteActualizado.idCliente, solicitud);
					if ( clienteActual.direcciones!=null ){
						direccion = DireccionHelper.getVO(clienteActual.direcciones[0], request);
						direcciondao.updateDireccion(conn, clienteActualizado.idCliente, direccion);
					}
					else{
						direccion = DireccionHelper.getVO(new DireccionVO(), request);
						direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion);
					}
					if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
						negocio.situacionLocal = HTMLHelper.getParameterInt(request, "situacionLocal");
						if ( solicitud.negocio!=null ){
							solicitud.negocio.situacionLocal = negocio.situacionLocal;
							negociodao.updateNegocio(conn, clienteActualizado.idCliente, idSolicitud, solicitud.negocio);
							solicitud.estatus = ClientesConstants.SOLICITUD_EN_ANALISIS;
						}
						else{
							negocio.razonSocial = "N/D";
							negociodao.addNegocio(conn, clienteActualizado.idCliente, idSolicitud, negocio);
							solicitud.negocio = negocio;
							solicitud.estatus = ClientesConstants.SOLICITUD_EN_ANALISIS;
						}
						bitutil.registraEvento( solicitud.negocio.toString() );
					}
					solicituddao.updateSolicitud(conn, clienteActualizado.idCliente, solicitud);
					conn.commit();
					clienteActualizado.solicitudes[indiceSolicitud] = solicitud;
					bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
				}
				bitutil.registraEvento( clienteActualizado.toString() );
				bitutil.registraEvento( solicitud.toString() );
				bitutil.registraEvento( direccion.toString() );
			}

			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza la solicitud del objeto cliente			
			clienteActualizado.direcciones = direcciondao.getDirecciones(clienteActualizado.idCliente);
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", clienteActualizado);
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
		return siguiente;
	}


}

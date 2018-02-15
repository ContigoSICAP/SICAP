package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
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
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaOtrosDomicilios implements Command{

	private String siguiente;


	public CommandGuardaOtrosDomicilios(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ClienteVO clienteActualizado = new ClienteVO();
		SolicitudVO solicitud = new SolicitudVO();
		DireccionVO direccion2Anterior = new DireccionVO();
		DireccionVO direccion3Anterior  = new DireccionVO();
		DireccionVO direccion2 = new DireccionVO();
		DireccionVO direccion3 = new DireccionVO();
		DireccionDAO direcciondao = new DireccionDAO();
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

			BitacoraUtil bitutil = new BitacoraUtil(clienteActualizado.idCliente, request.getRemoteUser(), "CommandGuardaDatosAbreviada");

			if ( idSolicitud==0 ){
				int idDireccionNext = direcciondao.getNext(clienteActualizado.idCliente, 0, "d_clientes", 1);
				//Nueva solicitud
				if ( idDireccionNext>1 ){
					solicitud = SolicitudHelper.getVO(new SolicitudVO(), request);
					direccion2 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 2);
					direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion2);
					if(request.getParameter("cpTerceraDir")!=null && !request.getParameter("cpTerceraDir").equals("") && request.getParameter("cpTerceraDir")!=null){
						direccion3 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 3);
						direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion3);
					}
					solicitud.estatus = ClientesConstants.ESTATUS_CAPTURADO;

					conn.commit();
					bitutil.registraCambioEstatus(solicitud, request);
//					bitutil.registraEvento( clienteActualizado.toString() );
//					bitutil.registraEvento( solicitud.toString() );
					bitutil.registraEvento( direccion2.toString() );
					if(request.getParameter("cpTerceraDir")!=null && !request.getParameter("cpTerceraDir").equals("") && request.getParameter("cpTerceraDir")!=null)
						bitutil.registraEvento( direccion3.toString() );

					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
				}else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se ha capturado seccion datos generales.");
				}
			}else{
				//Actualizar solicitud
				solicitud = SolicitudHelper.getVO(clienteActual.solicitudes[indiceSolicitud], request);
				int estatusPrevio = solicitud.estatus;
				Logger.debug("solicitud :"+solicitud.toString());
				int numdirecciones = direcciondao.countDirecciones(clienteActualizado.idCliente);
				int idDireccionNext = direcciondao.getNext(clienteActualizado.idCliente, 0, "d_clientes", 1);
				if ( idDireccionNext>1 ){
					int j = 1;
					if(clienteActual.direcciones!=null){
						for ( int i=0 ; i<clienteActual.direcciones.length ; i++ ) {
							if(clienteActual.direcciones[i]!=null){	
								if(clienteActual.direcciones[i].idDireccion != 1 && j==1){
									direccion2Anterior = clienteActual.direcciones[i];
									j++;
								}else if(j==2){
									direccion3Anterior = clienteActual.direcciones[i];
								}
							}	
						}
					}
					if(idDireccionNext==2){
						direccion2 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 2);
						direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion2);
						if(request.getParameter("cpTerceraDir")!=null && !request.getParameter("cpTerceraDir").equals("") && request.getParameter("cpTerceraDir")!=null){
							direccion3 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 3);
							direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion3);
						}
					}else if(numdirecciones>1){

						direccion2 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 2);
						direcciondao.updateDireccionesAdicionales(conn, clienteActualizado.idCliente, direccion2, direccion2Anterior.idDireccion);
						if(numdirecciones==2 && request.getParameter("cpTerceraDir")!=null && !request.getParameter("cpTerceraDir").equals("") && request.getParameter("cpTerceraDir")!=null){
							direccion3 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 3);
							direcciondao.addDireccion(conn, clienteActualizado.idCliente, direccion3);
						}else if(numdirecciones==3){
							direccion3 = DireccionHelper.getOtrasDirecciones(new DireccionVO(), request, 3);
							direcciondao.updateDireccionesAdicionales(conn, clienteActualizado.idCliente, direccion3, direccion3Anterior.idDireccion);
						}
					}
					conn.commit();
					clienteActualizado.solicitudes[indiceSolicitud] = solicitud;
					bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
//					bitutil.registraEvento( clienteActualizado.toString() );
//					bitutil.registraEvento( solicitud.toString() );
					bitutil.registraEvento( direccion2.toString() );
					if(request.getParameter("idColoniaTerceraDir")!=null)
						bitutil.registraEvento( direccion3.toString() );
				}else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se ha capturado seccion datos generales.");
				}
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

package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.NegocioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.NegocioVO;


public class CommandGuardaNegocio implements Command{


	private String siguiente;


	public CommandGuardaNegocio(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		NegocioVO negocio = new NegocioVO();
		DireccionVO direccion = new DireccionVO();
		NegocioDAO negociodao = new NegocioDAO();
		Connection conn  = null;
		boolean existe = false;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaNegocio");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			negocio = cliente.solicitudes[indiceSolicitud].negocio;
			
			if ( negocio==null ){
				negocio = new NegocioVO();
				negocio = SolicitudHelper.getNegocioVO(negocio, request);
				direccion = DireccionHelper.getVO(new DireccionVO(), request);
				negocio.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				negocio.fechaCaptura = new Timestamp(System.currentTimeMillis());
				negocio.direccion = direccion;
				negociodao.addNegocio(conn, cliente.idCliente, idSolicitud, negocio);
				if ( direccion.idColonia!=0 )
					(new DireccionDAO()).addDireccion(conn, cliente.idCliente, idSolicitud, "d_negocios", 1, direccion);
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				negocio = SolicitudHelper.getNegocioVO(negocio, request);
				if ( negocio.direccion!=null )
					existe = true;
				direccion = DireccionHelper.getVO(negocio.direccion, request);
				negocio.direccion = direccion;
				negociodao.updateNegocio(conn, cliente.idCliente, idSolicitud, negocio);
				if ( !existe )
					(new DireccionDAO()).addDireccion(conn, cliente.idCliente, idSolicitud, "d_negocios", 1, direccion);
				else
					(new DireccionDAO()).updateDireccion(conn, cliente.idCliente, idSolicitud, "d_negocios", 1, direccion);
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(negocio);
			bitutil.registraEvento(direccion);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].negocio = negocio;
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
		return siguiente;
	}


}

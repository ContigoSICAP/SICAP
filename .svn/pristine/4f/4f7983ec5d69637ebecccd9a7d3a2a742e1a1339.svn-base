package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CreditoViviendaDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CreditoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoViviendaVO;
import com.sicap.clientes.vo.DireccionVO;


public class CommandGuardaDatosCreditoVivienda implements Command{

	private String siguiente;


	public CommandGuardaDatosCreditoVivienda(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		DireccionVO direccion = new DireccionVO();
		CreditoViviendaVO creditoVivienda = new CreditoViviendaVO();
		DireccionDAO direcciondao = new DireccionDAO();
		CreditoViviendaDAO creditodao = new CreditoViviendaDAO();
		Connection conn  = null;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO clienteActual = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
			creditoVivienda = clienteActual.solicitudes[indiceSolicitud].creditoVivienda;
			
			//direccion = (DireccionVO)request.getAttribute("direccionCredito");
			
			if ( creditoVivienda == null ){
				//Nuevo crédito
				direccion = DireccionHelper.getVO(new DireccionVO(), request);
				creditoVivienda = CreditoHelper.getCreditoViviendaVO(new CreditoViviendaVO(), request);
				creditodao.addCreditoVivienda(clienteActual.idCliente, idSolicitud, creditoVivienda);
				direcciondao.addDireccion(conn, clienteActual.idCliente, idSolicitud, "d_credito_vivienda", 1, direccion);
				conn.commit();
				clienteActual.solicitudes[indiceSolicitud].creditoVivienda = creditoVivienda;
				clienteActual.solicitudes[indiceSolicitud].creditoVivienda.direccion = direccion;
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				//Actualizar datos de crédito
				direccion = creditoVivienda.direccion;
				direccion = DireccionHelper.getVO(direccion, request);
				creditoVivienda = CreditoHelper.getCreditoViviendaVO(creditoVivienda, request);
				creditodao.updateCreditoVivienda(clienteActual.idCliente, idSolicitud, creditoVivienda);
				direcciondao.updateDireccion(conn, clienteActual.idCliente, idSolicitud, "d_credito_vivienda", 1, direccion);
				conn.commit();
				clienteActual.solicitudes[indiceSolicitud].creditoVivienda = creditoVivienda;
				clienteActual.solicitudes[indiceSolicitud].creditoVivienda.direccion = direccion;
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			//bitutil.registraEvento( direccion.toString() );

			request.setAttribute("NOTIFICACIONES",notificaciones);

			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", clienteActual);
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

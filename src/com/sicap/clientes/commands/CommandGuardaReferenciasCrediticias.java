package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaCrediticiaDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ReferenciasHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ReferenciaCrediticiaVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaReferenciasCrediticias implements Command{


	private String siguiente;


	public CommandGuardaReferenciasCrediticias(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();

		Connection conn  = null;
		try{
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int idCliente = cliente.idCliente;
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
			BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandGuardaReferenciasCrediticias");
			ReferenciaCrediticiaVO referencias[] = null;
			referencias = ReferenciasHelper.getReferenciasCrediticias(referencias, request);
			ReferenciaCrediticiaDAO referenciadao = new ReferenciaCrediticiaDAO();
			
			//conn = ConnectionManager.getMySQLConnection();
			//conn.setAutoCommit(false);
			
			for(int i=0; i<referencias.length;i++){
				referenciadao.addReferenciaCrediticia(idCliente, idSolicitud, referencias[i]);
				bitutil.registraEvento(referencias[i]);
			}

			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");			
			//conn.commit();
			solicitud.referenciasCrediticias = referencias;
			cliente.solicitudes[indiceSolicitud] = solicitud;
			request.setAttribute("NOTIFICACIONES",notificaciones);
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

package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ExpedienteDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ExpedienteVO;
import com.sicap.clientes.vo.SolicitudVO;

public class CommandGuardaExpedienteOperativoLegal implements Command{
	
	private String siguiente;


	public CommandGuardaExpedienteOperativoLegal(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		ExpedienteVO expediente = new ExpedienteVO();
		ExpedienteDAO expedientedao = new ExpedienteDAO();
				
		try{
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaExpedienteOperativoLegal");
			solicitud = cliente.solicitudes[indiceSolicitud];			
			expedientedao.deleteExpediente(cliente.idCliente, idSolicitud);
			expediente = SolicitudHelper.getExpediente(new ExpedienteVO(), request);  
			expedientedao.addExpediente(cliente.idCliente, idSolicitud,expediente);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			bitutil.registraEvento(expediente);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			solicitud.expediente = expediente;
			cliente.solicitudes[indiceSolicitud] = solicitud;
			session.setAttribute("CLIENTE", cliente);
			
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
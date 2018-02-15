package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.LimiteCreditoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.LimiteCreditoVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaLimitesCredito implements Command{


	private String siguiente;


	public CommandGuardaLimitesCredito(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		LimiteCreditoVO limite = new LimiteCreditoVO();
		LimiteCreditoDAO limitedao = new LimiteCreditoDAO();
		SolicitudDAO solicituddao = new SolicitudDAO();
		
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaLimitesCredito");
			if ( solicitud.limites==null ){
				limite = new LimiteCreditoVO();
				limite = ClienteHelper.getLimiteVO(limite, request);
				limite.idCliente = solicitud.idCliente;
				limite.idSolicitud = solicitud.idSolicitud;
				limitedao.addLimite(limite);
				bitutil.registraEvento(limite);
				solicitud.estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
				solicituddao.updateSolicitud(cliente.idCliente, solicitud);
				bitutil.registraEvento(solicitud);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				limite = ClienteHelper.getLimiteVO(solicitud.limites, request);
				solicitud.estatus = ClientesConstants.SOLICITUD_PREAPROBADA;
				solicituddao.updateSolicitud(cliente.idCliente, solicitud);
				bitutil.registraEvento(solicitud);
				limitedao.updateLimite(limite);
				bitutil.registraEvento(limite);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].limites = limite;
			//Actualiza el cliente en sesion
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

package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaPersonalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ReferenciasHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ReferenciaPersonalVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaReferenciaPersonal implements Command {

	private String siguiente;

	public CommandGuardaReferenciaPersonal(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		ReferenciaPersonalVO referenciaPersonal = new ReferenciaPersonalVO();
		ReferenciaPersonalDAO referenciaPersonaldao = new ReferenciaPersonalDAO();
		try {
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int idReferenciaPersonal = HTMLHelper.getParameterInt(request,"idReferencia");
			ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaReferenciaPersonal");
			solicitud = cliente.solicitudes[indiceSolicitud];
			Logger.debug("capturaReferenciaPersonal" +idReferenciaPersonal);
			if (idReferenciaPersonal==0) {
				referenciaPersonal = ReferenciasHelper.getReferenciaPersonalVO(referenciaPersonal, request);
				referenciaPersonal.idSolicitud = idSolicitud;
				referenciaPersonal.fechaCaptura = new Timestamp(System.currentTimeMillis());
				referenciaPersonaldao.addReferenciaPersonal(cliente.idCliente,idSolicitud, referenciaPersonal);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			} else {
				referenciaPersonal = ReferenciasHelper.getReferenciaPersonalVO(referenciaPersonal, request);
				referenciaPersonal.idSolicitud = idSolicitud;
				referenciaPersonaldao.updateReferenciaPersonal(cliente.idCliente, idSolicitud, idReferenciaPersonal,referenciaPersonal);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(referenciaPersonal);
			cliente.solicitudes[indiceSolicitud].referenciasPersonales  = referenciaPersonaldao.getReferenciaPersonales(cliente.idCliente, idSolicitud);
			cliente.solicitudes[indiceSolicitud] = solicitud;
			request.setAttribute("NOTIFICACIONES", notificaciones);
			session.setAttribute("CLIENTE", cliente);
		} catch (ClientesDBException dbe) {
			throw new CommandException(dbe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}

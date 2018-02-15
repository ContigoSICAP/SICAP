package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaComercialDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ReferenciasHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ReferenciaComercialVO;
import com.sicap.clientes.vo.SolicitudVO;

public class CommandGuardaReferenciaComercial implements Command {

	private String siguiente;

	public CommandGuardaReferenciaComercial(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		int idSolicitud = 0;
		int idReferenciaComercial = 0;
		ReferenciaComercialVO referenciaComercial = new ReferenciaComercialVO();
		ReferenciaComercialDAO referenciaComercialdao = new ReferenciaComercialDAO();
		try {
			idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			idReferenciaComercial = HTMLHelper.getParameterInt(request, "idReferenciaComercial");
			ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaReferenciaComercial");
			solicitud = cliente.solicitudes[indiceSolicitud];
			if ( idReferenciaComercial==0 ) {
				referenciaComercial = ReferenciasHelper.getReferenciaComercialVO(referenciaComercial, request);
				referenciaComercial.fechaCaptura = new Timestamp(System.currentTimeMillis());
				referenciaComercialdao.addReferenciaComercial(cliente.idCliente, idSolicitud, referenciaComercial);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
			}else{
				referenciaComercial = solicitud.referenciasComerciales[idReferenciaComercial-1];
				referenciaComercial = ReferenciasHelper.getReferenciaComercialVO(referenciaComercial, request);
				referenciaComercialdao.updateReferenciaComercial(cliente.idCliente, idSolicitud, idReferenciaComercial, referenciaComercial);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(referenciaComercial);
			solicitud.referenciasComerciales = referenciaComercialdao.getReferenciasComerciales(cliente.idCliente, idSolicitud);
			cliente.solicitudes[indiceSolicitud] = solicitud;
//			Actualiza el cliente en sesion
			request.setAttribute("NOTIFICACIONES",notificaciones);
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

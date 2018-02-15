package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaLaboralDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ReferenciaLaboralVO;


public class CommandGuardaReferenciaLaboral implements Command{


	private String siguiente;


	public CommandGuardaReferenciaLaboral(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ReferenciaLaboralVO referenciaLaboral = new ReferenciaLaboralVO();
		ReferenciaLaboralDAO referenciaPersonaldao = new ReferenciaLaboralDAO();
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaReferenciaLaboral");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			referenciaLaboral = cliente.solicitudes[indiceSolicitud].referenciaLaboral;
			
			if ( referenciaLaboral==null ){
				referenciaLaboral = new ReferenciaLaboralVO();
				referenciaLaboral = SolicitudHelper.getReferenciaLaboralVO(referenciaLaboral, request);
				referenciaLaboral.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				referenciaLaboral.fechaCaptura = new Timestamp(System.currentTimeMillis());
				referenciaPersonaldao.addReferencia(cliente.idCliente, idSolicitud, referenciaLaboral);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
			}else{
				referenciaLaboral = SolicitudHelper.getReferenciaLaboralVO(referenciaLaboral, request);
				referenciaPersonaldao.updateReferencia(cliente.idCliente, idSolicitud, referenciaLaboral);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(referenciaLaboral);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].referenciaLaboral = referenciaLaboral;
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

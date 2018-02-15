package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ViviendaDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ViviendaVO;


public class CommandGuardaVivienda implements Command{


	private String siguiente;


	public CommandGuardaVivienda(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ViviendaVO vivienda = new ViviendaVO();
		ViviendaDAO empleodao = new ViviendaDAO();
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaVivienda");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			vivienda = cliente.solicitudes[indiceSolicitud].vivienda;
			
			if ( vivienda==null ){
				vivienda = new ViviendaVO();
				vivienda = SolicitudHelper.getViviendaVO(vivienda, request);
				vivienda.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				vivienda.fechaCaptura = new Timestamp(System.currentTimeMillis());
				empleodao.addVivienda(cliente.idCliente, idSolicitud, vivienda);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
			}else{
				vivienda = SolicitudHelper.getViviendaVO(vivienda, request);
				empleodao. updateVivienda(cliente.idCliente, idSolicitud, vivienda);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(vivienda);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].vivienda = vivienda;
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

package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CapacidadPagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CapacidadPagoVO;
import com.sicap.clientes.vo.ClienteVO;


public class CommandGuardaCapacidadPago implements Command{


	private String siguiente;


	public CommandGuardaCapacidadPago(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		CapacidadPagoVO capacidadPago = new CapacidadPagoVO();
		CapacidadPagoDAO capacidaddao = new CapacidadPagoDAO();
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaCapacidadPago");
			capacidadPago = cliente.solicitudes[indiceSolicitud].capacidadPago;
			if ( capacidadPago==null ){
				capacidadPago = new CapacidadPagoVO();
				capacidadPago = SolicitudHelper.getCapacidadPagoVO(capacidadPago, request);
				capacidadPago.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				capacidadPago.fechaCaptura = new Timestamp(System.currentTimeMillis());
				capacidaddao.addCapacidadPago(cliente.idCliente, idSolicitud, capacidadPago);
				
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
			}else{
				capacidadPago = SolicitudHelper.getCapacidadPagoVO(capacidadPago, request);
				capacidaddao. updateCapacidadPago(cliente.idCliente, idSolicitud, capacidadPago);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(capacidadPago);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].capacidadPago = capacidadPago;
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

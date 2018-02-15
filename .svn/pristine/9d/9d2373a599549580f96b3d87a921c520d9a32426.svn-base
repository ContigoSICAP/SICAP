package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ArrendatarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ArrendatarioVO;
import com.sicap.clientes.vo.ClienteVO;


public class CommandGuardaArrendatario implements Command{


	private String siguiente;


	public CommandGuardaArrendatario(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ArrendatarioVO arrendatario = new ArrendatarioVO();
		ArrendatarioDAO arrendatariodao = new ArrendatarioDAO();
		
		try{
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int tipoArrendatario = HTMLHelper.getParameterInt(request, "tipoArrendatario");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaArrendatario");
			if ( tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO )
				arrendatario = cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio;
			else
				arrendatario = cliente.solicitudes[indiceSolicitud].arrendatarioLocal;
			if ( arrendatario==null ){
				arrendatario = SolicitudHelper.getArrendatario(new ArrendatarioVO(), request);
				arrendatario.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				arrendatario.fechaCaptura = new Timestamp(System.currentTimeMillis());
				arrendatariodao.adddArrendatario(cliente.idCliente, idSolicitud, arrendatario);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				arrendatario = SolicitudHelper.getArrendatario(arrendatario, request);
				arrendatariodao.updateArrendatario(cliente.idCliente, idSolicitud, arrendatario);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(arrendatario);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza la solicitud del objeto cliente
			if ( tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO )
				cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio = arrendatario;
			else
				cliente.solicitudes[indiceSolicitud].arrendatarioLocal = arrendatario;
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
			request.setAttribute("TIPO_ARRENDATARIO", new Integer(arrendatario.idTipo));
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

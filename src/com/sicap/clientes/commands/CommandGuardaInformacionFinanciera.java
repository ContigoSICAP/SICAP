package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.InformacionFinancieraDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.InformacionFinancieraVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaInformacionFinanciera implements Command{


	private String siguiente;


	public CommandGuardaInformacionFinanciera(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		InformacionFinancieraVO informacion = new InformacionFinancieraVO();
		InformacionFinancieraDAO informaciondao = new InformacionFinancieraDAO();
		
		try{
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			solicitud = cliente.solicitudes[indiceSolicitud];
			informacion = solicitud.informacion;
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaInformacionFinanciera");
			if ( informacion==null ){
				informacion = SolicitudHelper.getInformacionFinanciera(new InformacionFinancieraVO(), request);
				informacion.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				informacion.fechaCaptura = new Timestamp(System.currentTimeMillis());
				informaciondao.addInformacionFinanciera(cliente.idCliente, idSolicitud, informacion);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				informacion = SolicitudHelper.getInformacionFinanciera(informacion, request);
				informaciondao.updateInformacionFinanciera(cliente.idCliente, idSolicitud, informacion);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(informacion);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			solicitud.informacion = informacion;
			//Actualiza la solicitud del objeto cliente
			cliente.solicitudes[indiceSolicitud] = solicitud;
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
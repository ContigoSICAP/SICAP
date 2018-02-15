package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ConyugeDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ConyugeVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandGuardaConyuge implements Command{


	private String siguiente;


	public CommandGuardaConyuge(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ConyugeVO conyuge = new ConyugeVO();
		ConyugeDAO conyugedao = new ConyugeDAO();
		SolicitudVO solicitud = null;
		String siguienteVivienda = "/capturaConyugeVivienda.jsp";
		int idSolicitud = 0;
		
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			solicitud = cliente.solicitudes[indiceSolicitud];
			conyuge = cliente.conyuge;
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaConyuge");
			if ( conyuge==null ){
				conyuge = new ConyugeVO();
				conyuge = ClienteHelper.getConyugeVO(conyuge, request);
				conyuge.fechaCaptura = new Timestamp(System.currentTimeMillis());
				conyugedao.addConyuge(cliente.idCliente, conyuge);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}else{
				conyuge = ClienteHelper.getConyugeVO(conyuge, request);
				conyugedao.updateConyuge(cliente.idCliente, conyuge);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}
			bitutil.registraEvento(conyuge);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			//Actualiza el objeto cliente
			cliente.conyuge = conyuge;
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		if ( solicitud.tipoOperacion==ClientesConstants.VIVIENDA )
			return siguienteVivienda;
		else 
			return siguiente;
	}


}

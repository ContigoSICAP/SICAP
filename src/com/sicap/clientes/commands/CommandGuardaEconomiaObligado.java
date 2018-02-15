package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.EconomiaObligadoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ObligadoSolidarioHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.EconomiaObligadoVO;


public class CommandGuardaEconomiaObligado implements Command{


	private String siguiente;
 

	public CommandGuardaEconomiaObligado(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		HttpSession session = request.getSession();
		Notification notificaciones[] = new Notification[1];
		ClienteVO cliente =(ClienteVO) session.getAttribute("CLIENTE");
		EconomiaObligadoVO economia = new EconomiaObligadoVO();
		EconomiaObligadoDAO dao = new EconomiaObligadoDAO();
		int idCliente = 0;
		int idSolicitud = 0;
		int idObligado = 0;
		try{
			idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			idObligado = HTMLHelper.getParameterInt(request, "idObligado");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaEconomiaObligado");
			if ( cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].economia!=null ){
				economia = ObligadoSolidarioHelper.getEconomiaObligadoVO(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].economia, request);
				dao.updateEconomiaObligado(idCliente, idSolicitud, idObligado, economia);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
			}else{
				economia = ObligadoSolidarioHelper.getEconomiaObligadoVO(economia, request);
				economia.fechaCaptura = new Timestamp(System.currentTimeMillis());
				dao.addEconomiaObligado(idCliente, idSolicitud, idObligado, economia);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			}
			bitutil.registraEvento(economia);
			cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].economia = economia;
			session.setAttribute("CLIENTE", cliente);
			request.setAttribute("NOTIFICACIONES",notificaciones);
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

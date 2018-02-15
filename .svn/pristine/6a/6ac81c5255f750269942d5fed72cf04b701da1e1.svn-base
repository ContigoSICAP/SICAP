package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;


public class CommandModificaBancoOrden implements Command{

	private String siguiente;

	public CommandModificaBancoOrden(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		OrdenDePagoVO  ordenpago    = new OrdenDePagoVO();
		OrdenDePagoDAO ordenpagoDAO = new OrdenDePagoDAO();
		try{
			int idBanco  	= HTMLHelper.getParameterInt(request, "banco");
			String idordenpago = request.getParameter("idOrdenPago");
			
			ordenpago = ordenpagoDAO.getOrdenDePago(idordenpago);
			if (ordenpago != null){
				ordenpago.setIdBanco(idBanco);
				ordenpagoDAO.updateOrdenDePago(ordenpago);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"La orden de pago se actualizo correctamente");
				BitacoraUtil bitutil = new BitacoraUtil(ordenpago.getIdOrdenPago(), request.getRemoteUser(), "CommandModificaBancoOrden");
			} else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro la orden de pago");				
			}

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

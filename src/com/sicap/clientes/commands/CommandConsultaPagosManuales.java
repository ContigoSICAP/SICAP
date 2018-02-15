package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.cartera.EventosVO;

public class CommandConsultaPagosManuales implements Command{
	
	private String siguiente;
	
	public CommandConsultaPagosManuales(String siguiente){
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException{
		try{
			HttpSession session = request.getSession();
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int idCliente = Integer.parseInt(request.getParameter("idCliente"));
			int idSolicitud = Integer.parseInt(request.getParameter("idSolicitud"));
			String referencia = request.getParameter("referencia").trim();
			Integer indiceSolicitud = new Integer(SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud));
			if (cliente.solicitudes[indiceSolicitud].idCliente > 0){
				cliente.solicitudes[indiceSolicitud].eventos = new EventoDAO().getElementos(idCliente,cliente.solicitudes[indiceSolicitud].idCreditoIBS);
			} else if(cliente.solicitudes[indiceSolicitud].pagosManuales == null){
				cliente.solicitudes[indiceSolicitud].pagosManuales = new PagoManualDAO().getPagoManual(idCliente,idSolicitud, referencia);
			}
			request.setAttribute("CLIENTE", cliente);
			request.setAttribute("indiceSolicitud", indiceSolicitud);
		}catch(ClientesException exc){
			exc.printStackTrace();
			throw new CommandException(exc.getMessage());
		}
		return siguiente;
	}
}

package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.LoteChequesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.LoteChequesVO;


public class CommandConsultaChequeras implements Command{


	private String siguiente;


	public CommandConsultaChequeras(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		int idSucursal = 0;
		LoteChequesVO lotes[] = null;
		LoteChequesDAO loteDAO = new LoteChequesDAO();
		Notification notificaciones[] = new Notification[1];		
		
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
			lotes = loteDAO.getLotesCheques(idSucursal);
			if ( lotes!=null && lotes.length > 0)
				request.setAttribute("LOTES", lotes);
			else{
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron chequeras activas en la sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
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

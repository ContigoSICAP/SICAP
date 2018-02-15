package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ObligadoSolidarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandObtenObligadosSolidarios implements Command{


	private String siguiente;


	public CommandObtenObligadosSolidarios(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		HttpSession session = request.getSession();
		int idSolicitud = 0;
		ClienteVO cliente = null;
		SolicitudVO solicitud = null;
		ObligadoSolidarioDAO obligadodao = new ObligadoSolidarioDAO();
		try{
			idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			solicitud = cliente.solicitudes[indiceSolicitud];
			//Actualiza la solicitud del objeto cliente
			solicitud.obligadosSolidarios = obligadodao.getObligadosSolidarios(cliente.idCliente, solicitud.idSolicitud);
			//Persiste los cambios en base de datos
			if ( solicitud.obligadosSolidarios==null ){
				//No hay obligados para la solicitud
			}else{
				//DEBERA LLAMAR AL DAO DE DIRECCION
			}
			//Actualiza el cliente en sesion sincronizando el objeto en sesion con la informacion
			//almacenada en la base de datos
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

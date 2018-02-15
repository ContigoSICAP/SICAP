package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.ConyugeDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ClienteVO;

public class CommandMantenimientoCliente implements Command{
	
	private String siguiente;
	
	public CommandMantenimientoCliente(String siguiente){
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException{
		HttpSession session = request.getSession();
		ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
		CatalogoVO localidad = new CatalogoVO();
		CatalogoDAO dao = new CatalogoDAO();
		try{
			int idCliente = cliente.idCliente;
			cliente.conyuge = new ConyugeDAO().getConyuge(idCliente);
			Logger.debug("cliente.conyuge::::" + cliente.conyuge);
			cliente.direcciones = new DireccionDAO().getDirecciones(idCliente);
			Logger.debug("Direcciones:::" + cliente.direcciones);
			if(cliente.direcciones!=null){
				localidad = dao.getLocalidad(cliente.direcciones[0].idColonia, cliente.direcciones[0].idLocalidad);
				if (localidad != null)
					cliente.direcciones[0].localidad = localidad.descripcion;
				cliente.direcciones[0] = DireccionHelper.getVO(cliente.direcciones[0], request);
				cliente.direcciones[0].telefonos = new TelefonoDAO().getTelefonos(cliente.idCliente, cliente.direcciones[0].idDireccion);
			}else
			session.setAttribute("CLIENTE", cliente);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new CommandException(exc.getMessage());
		}
		return siguiente;
	}
}

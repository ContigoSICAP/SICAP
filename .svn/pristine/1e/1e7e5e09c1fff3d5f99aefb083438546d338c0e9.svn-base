package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.BuroInternoDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BuroInternoVO;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandBuscaIncidenciaBuroInterno implements Command{
	
	private String siguiente;

	public CommandBuscaIncidenciaBuroInterno(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		
	HttpSession session = request.getSession();
	BuroInternoVO buro[] = null;
	UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
	BuroInternoDAO dao = new BuroInternoDAO();
	BuroInternoVO busVO = new BuroInternoVO();
	Notification notificaciones[] = new Notification[1];
	int numCliente = 0;
	int idSolicitud = 0;
	String cliente = null;
	String solicitud = null;
	
	//busVO.sucursales = usuario.sucursales;
	//busVO.sucursal = usuario.idSucursal;
	try{
		idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		
		if ( request.getParameter("idCte")!=null || request.getParameter("apellidoPaterno")!=null || request.getParameter("apellidoMaterno")!=null || request.getParameter("nombre")!=null){
			busVO.numCliente = HTMLHelper.getParameterInt(request, "idCte");
			busVO.apaterno = request.getParameter("apellidoPaterno").trim();
			busVO.amaterno = request.getParameter("apellidoMaterno").trim();
			busVO.nombre = request.getParameter("nombre");
			buro = dao.buscaIncidenciaBuro(busVO);
			if ( buro!=null && buro.length>0 ){
				request.setAttribute("CLIENTES_BURO_INTERNO",buro);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Clientes encontrados");
			}else{
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron clientes");
			}
			request.setAttribute("NOTIFICACIONES",notificaciones);
		}
		
		if (request.getParameter("idCliente")!=null && idSolicitud == 1){
			cliente = request.getParameter("idCliente");
			solicitud = request.getParameter("idSolicitud");
			System.out.println("Ando por aqui y el hare la busqueda con el numCliente: "+cliente);
			System.out.println("Ando por aqui y el hare la busqueda con el idSolicitud: "+solicitud);
			
			numCliente = HTMLHelper.getParameterInt(request, "idCliente");
			//solicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			System.out.println("Ando por aqui y el hare la busqueda con el numCliente: "+numCliente);
			System.out.println("Ando por aqui y el hare la busqueda con el idSolicitud: "+solicitud);
			BuroInternoVO clienteBuro = new BuroInternoVO();
			clienteBuro = dao.buscaCliente(numCliente);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Actualizar los datos del cliente en Buro Interno");
			request.setAttribute("CLIENTE_BURO_INTERNO",clienteBuro);
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


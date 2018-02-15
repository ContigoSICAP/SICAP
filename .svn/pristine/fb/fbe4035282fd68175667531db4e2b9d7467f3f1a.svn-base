package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BusquedaClientesVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.UsuarioVO;


public class CommandBuscaClientePorRFC implements Command{

	private String siguiente;

	public CommandBuscaClientePorRFC(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		HttpSession session = request.getSession();
		ClienteVO clientes[] = null;
		UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
		ClienteDAO dao = new ClienteDAO();
		BusquedaClientesVO busVO = new BusquedaClientesVO();
		Notification notificaciones[] = new Notification[1];

		busVO.sucursales = usuario.sucursales;
		busVO.sucursal = usuario.idSucursal;
		try{
			if ( request.getParameter("rfc")!=null || request.getParameter("idCte")!=null || request.getParameter("apellidoPaterno")!=null || request.getParameter("apellidoMaterno")!=null || request.getParameter("nombreS")!=null){
				busVO.RFC = request.getParameter("rfc").trim();
				busVO.idCliente = HTMLHelper.getParameterInt(request, "idCte");
				busVO.apellidoPaterno = request.getParameter("apellidoPaterno").trim();
				busVO.apellidoMaterno = request.getParameter("apellidoMaterno").trim();
				busVO.nombreS = request.getParameter("nombreS").trim();
				if(busVO.sucursal!=0 && busVO.sucursales.length>0)
					clientes = dao.buscaCliente(busVO);
				if ( clientes!=null && clientes.length>0 ){
					request.setAttribute("CLIENTES_POR_RFC",clientes);
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Clientes encontrados");
				}/*else if ( obligadodao.findByRFC(rfc)!=null ){
					request.setAttribute("CLIENTES_POR_RFC",new ClienteVO[0]);
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"RFC registrado para obligado solidario");
				}*/else{
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontraron clientes");
				}
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

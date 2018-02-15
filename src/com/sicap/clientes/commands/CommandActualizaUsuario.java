package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandActualizaUsuario implements Command{
	
	private String siguiente;


	public CommandActualizaUsuario(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException {
		
		HttpSession session = request.getSession();
		Notification notificaciones[] = new Notification[1];
		UsuarioVO    user             = (UsuarioVO)session.getAttribute("USUARIO");
		String       password         = request.getParameter("user_pass");
		String       confPassword     = request.getParameter("user_passConf");
		boolean      flag             = true;
		String       regresar         = "/cambioPasswordUsuario.jsp";
		
		if ( user==null )
			user = (UsuarioVO)session.getAttribute("USUARIO_CAMBIO_PWD");
		if( !password.equals(confPassword) ){
			notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Tus contraseñas es diferente de la confirmación, ");
			flag = false;
		}
		else if( SecurityUtil.isMismoPassword(password, user.password) ){
			notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Tu contraseña debe ser distinta a la actual");
			flag = false;
		}
		else if( !SecurityUtil.validaPassword(password)){
			notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"Tu contraseña no cumple con las políticas establecidas");
			flag = false;
		}
		/**
		 * Como todas las validaciones fueron pasadas proseguimos a realizar el Update
		 */
		try {
			if( flag ){

				UsuarioDAO userDAO = new UsuarioDAO();
				if (userDAO.updateDatosUsuario(user.nombre, password, null)){
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Tu contraseña fue actualizada de manera correcta");
				}	
			}
		} catch (ClientesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		request.setAttribute("NOTIFICACIONES", notificaciones);
		if ( !flag )
			return regresar;
		return siguiente;
	}
	

}

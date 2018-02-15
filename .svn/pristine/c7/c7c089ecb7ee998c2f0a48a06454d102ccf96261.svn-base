package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.BuroInternoDAO;
import com.sicap.clientes.dao.UserSucursalDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BuroInternoVO;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandModificaIncidenciaBuroInterno implements Command{
	
	private String siguiente;

	public CommandModificaIncidenciaBuroInterno(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		BuroInternoDAO buroDAO = new BuroInternoDAO();
		UserSucursalDAO userSucursalDAO = new UserSucursalDAO();
		Connection conn  = null;
		BuroInternoVO buroInterno = new BuroInternoVO();
		try{
			
			String aPaterno = request.getParameter("aPaterno");
			String aMaterno = request.getParameter("aMaterno");
			String nombre = request.getParameter("nombre");
			String descripcion = request.getParameter("descripcion");
			String usuarioModificacion = request.getParameter("usuarioModificacion");
			String fechaUltimaModificacion = request.getParameter("fechaUltimaModificacion");
			int numCliente = HTMLHelper.getParameterInt(request, "idCliente");
			int idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
			int motivo = HTMLHelper.getParameterInt(request, "motivo");
			int status = HTMLHelper.getParameterInt(request, "status");
			
			buroInterno.descripcion = descripcion;
			buroInterno.usuarioUltimaModificacion = usuarioModificacion;
			buroInterno.numCliente = numCliente;
			buroInterno.numSucursal = 1;
			buroInterno.motivoIngreso = motivo;
			buroInterno.estatus = status;
			
			buroDAO.updateIncidenciaBuroInterno(buroInterno);
			
			/*
			int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			usuario = usuDAO.getDatosUsuario(username);
			if(usuario!=null){
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El usuario ya existe.");
				request.setAttribute("NOTIFICACIONES", notificaciones);
				return siguiente;
			}
			usuario = new UsuarioVO();
			if(!SecurityUtil.validaPassword(password)){
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El formato del password es incorrecto.");
			}else{
				conn = ConnectionManager.getMySQLConnection();
				conn.setAutoCommit(false);
				usuario = new UsuarioVO();
				usuario.nombre = username;
				usuario.password = password;
				usuario.identificador = identificador;
				usuario.idSucursal = idSucursal;
				roles = usuDAO.getRoles();
				usuDAO.addUsuario(usuario, conn);
				if(roles != null){
					for(int i = 0; i < roles.length; i++){
						if(request.getParameter(roles[i])!=null){
							usuDAO.addRolUsuario(usuario.nombre, roles[i], conn);
							request.setAttribute(roles[i], roles[i]);
						}
					}
				}
				userSucursalDAO.addUserSucursal(username, idSucursal, conn);
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"El usuario fue guardado correctamente");
				usuario.roles = usuDAO.getRolesUsuario(usuario.nombre);
				request.setAttribute("MODIFICACION_USUARIO", usuario);
				
			}*/
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"El datos del usuario fueron modificados en Buro Interno");
			request.setAttribute("NOTIFICACIONES", notificaciones);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		finally {
			try {
				if ( conn!=null ) conn.close();
			}
			catch(SQLException sqle) {
				throw new CommandException(sqle.getMessage());
			}
		}
		return siguiente;
	}
}


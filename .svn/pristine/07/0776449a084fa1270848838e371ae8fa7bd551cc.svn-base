package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.BuroInternoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.UserSucursalDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BuroInternoVO;
import com.sicap.clientes.vo.ClienteVO;

	
public class CommandBuscaClienteParaIncidencia implements Command{
		
	private String siguiente;

	public CommandBuscaClienteParaIncidencia(String siguiente) {
			this.siguiente = siguiente;
	}	
		
	public String execute(HttpServletRequest request) throws CommandException {
	
		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ClienteDAO clienteDAO = new ClienteDAO();
		UserSucursalDAO userSucursalDAO = new UserSucursalDAO();
		Connection conn  = null;
		ClienteVO cliente = new ClienteVO();
		try{
			
			int idCliente = Integer.parseInt(request.getParameter("idCte"));
			cliente = clienteDAO.getCliente(idCliente);
			if ( cliente!=null && cliente.idCliente >0 ){
				request.setAttribute("DatosCliente", cliente);
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Agregar la informacion restante");
			}else{
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"El Cliente solicitado aun no existe en el sistema por favor verifiquelo");
				siguiente = "/buscaClienteBuro.jsp";
			}
			request.setAttribute("NOTIFICACIONES",notificaciones);
		}catch(Exception e){
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

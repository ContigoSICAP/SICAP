package com.sicap.clientes.commands;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.ConyugeDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ConyugeVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.TelefonoVO;

public class CommandGuardaCambiosCliente implements Command{
	
	private String siguiente;
	
	public CommandGuardaCambiosCliente(String siguiente){
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request)throws CommandException{
		Notification notificaciones[] = new Notification[1];
		ConyugeVO conyuge = null;
		TelefonoVO telefonoPrincipal = null;
		TelefonoVO telefonoRecados = null;
		TelefonoVO telefonoCelular = null;
		DireccionVO direccion = null;
		Connection con = null;
		
		try{
			HttpSession session = request.getSession();
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			con = ConnectionManager.getMySQLConnection();
			con.setAutoCommit(false);
			cliente  = ClienteHelper.getVO(cliente, request);
			new ClienteDAO().updateCliente(cliente);
			conyuge = ClienteHelper.getConyugeVOMantenimiento(conyuge, request);
			if( conyuge!=null && conyuge.nombre!=null && !conyuge.nombre.equals("") && cliente.conyuge==null ){
				cliente.conyuge = conyuge;
				new ConyugeDAO().addConyuge(cliente.idCliente, cliente.conyuge);
			}else if( cliente.conyuge!=null ){
				cliente.conyuge = conyuge;
				new ConyugeDAO().updateConyuge(cliente.idCliente, cliente.conyuge);
			}
			if ( cliente.direcciones!=null && cliente.direcciones[0]!=null )
				direccion = cliente.direcciones[0];
			direccion = DireccionHelper.getVO(direccion, request);
			if(cliente.direcciones==null){
				new DireccionDAO().addDireccion(con, cliente.idCliente, direccion);
				cliente.direcciones = new DireccionVO[1];
				cliente.direcciones[0] = direccion;
			}else{
				new DireccionDAO().updateDireccion(con, cliente.idCliente, direccion);
			}
			telefonoPrincipal = ClienteHelper.getTelefonoVO( ClienteHelper.getTelefono(cliente.direcciones[0].telefonos, ClientesConstants.TELEFONO_PRINCIPAL), ClientesConstants.TELEFONO_PRINCIPAL, request);
			telefonoRecados = ClienteHelper.getTelefonoVO( ClienteHelper.getTelefono(cliente.direcciones[0].telefonos, ClientesConstants.TELEFONO_RECADOS), ClientesConstants.TELEFONO_RECADOS, request);
			telefonoCelular = ClienteHelper.getTelefonoVO( ClienteHelper.getTelefono(cliente.direcciones[0].telefonos, ClientesConstants.TELEFONO_CELULAR), ClientesConstants.TELEFONO_CELULAR, request);
			if(cliente.direcciones[0].telefonos!= null){
				if(cliente.direcciones[0].telefonos[0] != null){
					new TelefonoDAO().updateTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoPrincipal);
				}else{
					new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoPrincipal);
				}
				if(cliente.direcciones[0].telefonos[1] != null){
					new TelefonoDAO().updateTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoRecados);
				}else{
					new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoRecados);
				}
				if(cliente.direcciones[0].telefonos[2] != null){
					new TelefonoDAO().updateTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoCelular);
				}else{
					new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoCelular);
				} 
			}else{
				new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoPrincipal);
				new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoRecados);
				new TelefonoDAO().addTelefono(con, cliente.idCliente, cliente.direcciones[0].idDireccion, telefonoCelular);
			}
			con.commit();
			//cliente.direcciones[0].telefonos = new TelefonoDAO().getTelefonos(cliente.idCliente, cliente.direcciones[0].idDireccion);
			//cliente.direcciones[0] = DireccionHelper.getVO(cliente.direcciones[0], request);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La información del cliente ha sido acutalizada");
			request.setAttribute("CLIENTE",cliente);
			request.setAttribute("NOTIFICACIONES",notificaciones);
		}catch(Exception exc){
			exc.printStackTrace();
			throw new CommandException(exc.getMessage());
		}
		return siguiente;
	}
}

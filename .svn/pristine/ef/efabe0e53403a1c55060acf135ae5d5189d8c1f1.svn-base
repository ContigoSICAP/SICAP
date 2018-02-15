package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ColoniaVO;


public class CommandBuscaLocalidad implements Command{


	private String siguiente;


	public CommandBuscaLocalidad(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		String localidad = request.getParameter("nombre");
		int numero = 0;
		
		int idColonia = 0;
		CatalogoVO[] localidades = null;
		ColoniaVO colonia = new ColoniaVO();
		CatalogoDAO dao = new CatalogoDAO();
		Notification notificaciones[] = new Notification[1];

		try{
			if ( request.getParameter("idColonia") != null ){
				synchronized(this){
					idColonia = Integer.parseInt(request.getParameter("idColonia"));
					localidad = request.getParameter("nombre");
					if ( request.getParameter("numero") != null && request.getParameter("numero") != "")
						numero = Integer.parseInt(request.getParameter("numero"));
					
					colonia = dao.getDetalleColonia(idColonia);
					dao = new CatalogoDAO();
					if ( colonia != null)
						localidades = dao.getLocalidades(colonia.idEstado, colonia.idMunicipio, localidad, numero);
				}
				if ( localidades!=null && localidades.length>0 ){
					request.setAttribute("LOCALIDADES",localidades);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se encontro la localidad");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
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

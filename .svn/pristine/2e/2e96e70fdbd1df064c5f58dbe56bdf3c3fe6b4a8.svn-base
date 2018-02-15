package com.sicap.clientes.commands;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.util.ArrayList;
import java.sql.Date;

public class CommandConsultaOrdenesSeguros implements Command{

	private String siguiente;

	public CommandConsultaOrdenesSeguros(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();

		try{
                    Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
                    Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
                    ArrayList<OrdenDePagoVO> ordenesPagoSeguros = new ArrayList<OrdenDePagoVO>();
                    OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
                    ordenesPagoSeguros =  opagodao.getOrdenesSeguros(fechaInicio, fechaFin);
                    if( ordenesPagoSeguros.isEmpty()){
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"No se encontraron Registros con los Datos Proporcionados"));
                        ordenesPagoSeguros=null;
                    }
                    request.setAttribute("NOTIFICACIONES",notificaciones);
                    request.setAttribute("ORDENESDEPAGO", ordenesPagoSeguros);
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
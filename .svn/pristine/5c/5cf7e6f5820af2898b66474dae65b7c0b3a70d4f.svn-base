package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoVO;

public class CommandFechaReprocesarPagos extends DAOMaster implements Command{

	private String siguiente;

	public CommandFechaReprocesarPagos(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request)  throws CommandException{
		   
		   Notification notificaciones[] = new Notification[1];
								
		try{	
			PagoVO [] pagos = null;  			  
            String  fecha = request.getParameter("Fecha");
            String  fechaFin = request.getParameter("FechaFin");
            Logger.debug("Fecha: "+fecha);
            Logger.debug("FechaFin: "+fechaFin);
            PagoReferenciadoDAO pr = new PagoReferenciadoDAO();
            int opcionesaReprocesar = 1;           
            //pagos = pr.getPagosAReprocesar(opcionesaReprocesar,fecha);
            pagos = pr.getPagosAReprocesarCont(opcionesaReprocesar, fecha, fechaFin);
	           
            if(pagos!= null && pagos.length>0){           	           	
            	notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "Incidencias del día: "+fecha);
            	request.setAttribute("INCIDENCIAS", pagos);
            	request.setAttribute("NOTIFICACIONES", notificaciones);
            }else{
            	notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "No hay incidencias del día: "+fecha);
            	request.setAttribute("NOTIFICACIONES", notificaciones);
            }
		
	      }catch (Exception e) {
		    Logger.debug("Excepcion en CommandFechaReprocesarPagos: "+e.getMessage());
		    e.printStackTrace();
		    throw new CommandException(e.getMessage());
	      }
	      
		return siguiente;
	}
	
	
  }
	


package com.sicap.clientes.commands;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.SegurosAfirmeDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SegurosVO;

public class CommandReporteSeguros extends DAOMaster implements Command{


	private String siguiente;


	public CommandReporteSeguros(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request)  throws CommandException{
		
		 SegurosAfirmeDAO reportemensual = new SegurosAfirmeDAO();
		 String mes = request.getParameter("Mes");
		 String año = request.getParameter("Año");		 
		    		 		 
		 try{
		
			 ArrayList<SegurosVO> seguros =  reportemensual.getDatosReporteSeguros(mes,año);
			 request.setAttribute("SEGUROS", seguros);			 
			 
		 }catch (Exception e) {
			    Logger.debug("Excepcion en CommandReporteSeguros: "+e.getMessage());
			    e.printStackTrace();
			    throw new CommandException(e.getMessage());
		      }
		      		 
		return siguiente;	  
	}
	

}

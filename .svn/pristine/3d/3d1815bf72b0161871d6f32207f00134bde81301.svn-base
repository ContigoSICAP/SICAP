package com.sicap.clientes.commands;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.dao.GrupoDAO;


public class CommandListaDivMora implements Command{

	private String siguiente;

	public CommandListaDivMora(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		//TreeMap catEjecutivos = new TreeMap();
		TablaAmortVO[] listaDividendos = null;
		TablaAmortVO   tablaAmort 		= new TablaAmortVO();
		TablaAmortDAO listaDividendosDAO 		= new TablaAmortDAO();
		Notification notificaciones[] = new Notification[1];
		int idGrupo   = 0;
		int idCiclo = 0;
		CicloGrupalVO  ciclo 	= new CicloGrupalVO();
		CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
		GrupoVO		   grupo	= new GrupoVO();
		GrupoDAO	grupoDAO	= new GrupoDAO();	
		try{
			idGrupo     = HTMLHelper.getParameterInt(request, "idGrupo");
			idCiclo   = HTMLHelper.getParameterInt(request, "idCiclo");
			if (idCiclo > 0 && idGrupo > 0){
				ciclo = cicloDAO.getCiclo(idGrupo, idCiclo);
				grupo = grupoDAO.getGrupo(idGrupo);
				if (ciclo != null){
					listaDividendos = listaDividendosDAO.getElementosMorVen(ciclo.idGrupo, ciclo.idCreditoIBS, 0, 0);
				}
                                
				if(listaDividendos!=null && listaDividendos.length > 0) {
					request.setAttribute("DIVIDENDOS",listaDividendos);
					request.setAttribute("CICLO",ciclo);
					request.setAttribute("GRUPO",grupo);
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Dividendos encontrados");
					request.setAttribute("NOTIFICACIONES",notificaciones);
															
				}	//fin if
				else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron dividendos en mora para el grupo y ciclo seleccionados");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}	//fin else
			}	//fin if
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado un grupo con creditos activo");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}  //fin else
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

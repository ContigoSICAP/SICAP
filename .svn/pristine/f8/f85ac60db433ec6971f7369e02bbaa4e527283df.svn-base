package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;

public class CommandRefinanciamientoGrupal implements Command{


	private String siguiente;


	public CommandRefinanciamientoGrupal(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		HttpSession session = request.getSession();
		CicloGrupalVO nuevoCiclo = new CicloGrupalVO();
		GrupoVO grupo = new GrupoVO();

		try{
			grupo = (GrupoVO)session.getAttribute("GRUPO"); 
			int idCiclo = HTMLHelper.getParameterInt(request, "idCicloRefinancear");
			//Obtiene integrantes del ciclo activo
			nuevoCiclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			//Asigna tipo de ciclo Refinanceo y numero sigueinte
			nuevoCiclo.idTipoCiclo = ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO;
			nuevoCiclo.idCiclo++;
			nuevoCiclo.tasa = 7;
			nuevoCiclo.plazo = 16;
			//Procesa integrantes
			GrupoUtil.obtieneIntegrantesRefinanciamiento(nuevoCiclo);
			//Cambia calificacion a A1
			grupo.calificacion = "A1";
			//Se manda por request el nuevo ciclo
			request.setAttribute("CICLO", nuevoCiclo);
			request.setAttribute("ID_CICLO", 1);
			session.setAttribute("GRUPO", grupo);

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

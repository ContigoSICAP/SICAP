package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;

public class CommandConsultaPagosManualesGrupal implements Command{
	
	private String siguiente;
	
	public CommandConsultaPagosManualesGrupal(String siguiente){
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException{
		try{
			HttpSession session = request.getSession();
			GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
			System.out.println(grupo.toString());
			int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
			int idCiclo = Integer.parseInt(request.getParameter("idCiclo"));
			String referencia = request.getParameter("referencia").trim();
			CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			if (ciclo.idCreditoIBS > 0){
				ciclo.eventos = new EventoDAO().getElementos(idGrupo,ciclo.idCreditoIBS);
			} else if(ciclo.pagosManuales == null){
				ciclo.pagosManuales = new PagoManualDAO().getPagoManual(idGrupo,idCiclo, referencia);
			}
			GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
			session.setAttribute("GRUPO", grupo);
			request.setAttribute("idCiclo", idCiclo);
		}catch(ClientesException exc){
			exc.printStackTrace();
			throw new CommandException(exc.getMessage());
		}
		return siguiente;
	}

}

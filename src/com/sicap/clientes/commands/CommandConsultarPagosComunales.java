package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;

/**
 * Comando que muestra la matriz de pagos comunales para un grupo y ciclo especifico
 * @author JahTechnologies
 *
 */
public class CommandConsultarPagosComunales implements Command{

	/**
	 * Pagina a la que se redireccionara despues de la ejecucion del comando
	 */
	private String siguiente;

	/**
	 * 
	 * @param siguiente Pagina a la que se redireccionara despues de la ejecucion del comando
	 */
	public CommandConsultarPagosComunales(String siguiente) {
		this.siguiente = siguiente;
	}

	/**
	 * Ejecucion del comando
	 */
	public String execute(HttpServletRequest request) throws CommandException {

		try{
			HttpSession session = request.getSession();
			int numGrupo=Integer.parseInt(request.getParameter("idGrupo"));
			int numCiclo=Integer.parseInt(request.getParameter("idCiclo"));
			Notification notificaciones[] = new Notification[1];
			GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
			System.out.println("GRUPO : "+grupo.nombre);

			if ( grupo==null){
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El grupo no existe");
				request.setAttribute("NOTIFICACIONES",notificaciones);
				return "/consultaPagosComunales.jsp";
			}
			if (  grupo.ciclos==null){
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El grupo no tiene ciclos");
				request.setAttribute("NOTIFICACIONES",notificaciones);
				return "/consultaPagosComunales.jsp";
			}
			CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, numCiclo);
			ciclo.pagosGrupales = new PagoGrupalDAO().getPagosGrupales_(numGrupo, numCiclo);
			System.out.println("CICLO : "+ciclo.tablaAmortizacion.length);
			if( ciclo.pagosGrupales==null ){
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El ciclo no tiene pagos");
				request.setAttribute("NOTIFICACIONES",notificaciones);
				return "/consultaPagosComunales.jsp";
			}
			GrupoUtil.setCiclo(grupo, ciclo, numCiclo);
			request.setAttribute("idGrupo", Integer.toString(numGrupo));
			session.setAttribute("GRUPO", grupo);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}

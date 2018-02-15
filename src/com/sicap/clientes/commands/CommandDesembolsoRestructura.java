package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandDesembolsoRestructura implements Command{


	private String siguiente;


	public CommandDesembolsoRestructura(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		GrupoVO grupo = new GrupoVO();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		SolicitudDAO solDAO = new SolicitudDAO();
		CicloGrupalDAO ciclodao = new CicloGrupalDAO();
		
		Connection conn  = null;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);

			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");

			grupo = (GrupoVO)session.getAttribute("GRUPO");
			if ( idCiclo!=0 )
				ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);

			ciclo.integrantes = GrupoHelper.getIntegrantesCiclo(request);
			
			ciclo.monto = 0;
			ciclo.montoConComision = 0;
			for ( int i=0; i<ciclo.integrantes.length; i++){
					SolicitudVO solicitud = new SolicitudVO();
					solicitud = solDAO.getSolicitud(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
					solicitud.desembolsado = ClientesConstants.DESEMBOLSADO;
					solicitud.fechaDesembolso = Convertidor.toSqlDate(new Date());
					solDAO.updateSolicitud(conn, ciclo.integrantes[i].idCliente, solicitud);
					ciclo.integrantes[i].numCheque = solicitud.numCheque;
					ciclo.integrantes[i].desembolsado = solicitud.desembolsado;
					ciclo.monto += ciclo.integrantes[i].monto;
			}
			ciclo.montoConComision = ciclo.monto;
                        System.out.println("_______________________________"+request.getRemoteUser()+" CommandDesembolsoRestructura"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
			GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, new Date());
			ciclo.desembolsado = ClientesConstants.CICLO_DESEMBOLSO_CONFIRMADO;
			
			if ( GrupoHelper.registerIBS(grupo, ciclo, request, notificaciones) ){
				//new CicloGrupalDAO().updateCicloCredito(ciclo);
				ciclodao.updateCiclo(conn, ciclo);
				conn.commit();
				BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandDesembolsoRestructura");
				notificaciones.add( new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente") );
				bitutil.registraEvento(ciclo);
				//Se actualiza en sesión la información generada
				GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
				//grupo.ciclos[idCiclo-1] = ciclo;
				session.setAttribute("GRUPO", grupo);
			}else{
				conn.rollback();
			}
			request.setAttribute("NOTIFICACIONES", notificaciones);

			


		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}finally {
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

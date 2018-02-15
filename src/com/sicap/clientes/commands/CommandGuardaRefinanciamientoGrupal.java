package com.sicap.clientes.commands;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoIBSVO;


public class CommandGuardaRefinanciamientoGrupal implements Command{

	
	private String siguiente;

	
	public CommandGuardaRefinanciamientoGrupal(String siguiente) {
		this.siguiente = siguiente;
	}

	
	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		CicloGrupalVO cicloGrupal = new CicloGrupalVO();
		CicloGrupalVO cicloAnterior = new CicloGrupalVO();
		GrupoVO grupo = new GrupoVO();
		Connection conn  = null;
		CicloGrupalDAO cicloGrupalDAO = new CicloGrupalDAO();
		ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
		String validacionDesembolso = "NO";
		
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			grupo = (GrupoVO)session.getAttribute("GRUPO"); 
			BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaRefinciamientoGrupal");
			int idCiclo = HTMLHelper.getParameterInt(request, "idCicloRefinancear");
			int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			boolean esNuevo = (HTMLHelper.getParameterInt(request, "esNuevo")==1  ? true : false );
			//Obtiene ciclo del request
			cicloGrupal = GrupoHelper.getCicloGrupalVO(cicloGrupal, request);
			cicloGrupal.direccionReunion = DireccionHelper.getDireccionGenericaVO(cicloGrupal.direccionReunion, request);
			
			//Obtiene y procesa integrantes
			GrupoUtil.procesaIntegrantesRefinanciamiento(conn, cicloGrupal, request, esNuevo);
			
			if( esNuevo ){
				//Obtengo y Cierro ciclo anterior por refinaciamiento
				cicloAnterior = cicloGrupalDAO.getCiclo(idGrupo, idCiclo-1);
				cicloAnterior.estatus = 2;
				cicloGrupalDAO.updateCiclo(conn, cicloAnterior );
				
				//Guardo el nuevo ciclo y sus integrantes
				cicloGrupal.idDireccionReunion = new DireccionGenericaDAO().addDireccion(conn, cicloGrupal.direccionReunion);
				cicloGrupal.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				cicloGrupalDAO.addCicloGrupal(conn, cicloGrupal);
				for( int i=0; i<cicloGrupal.integrantes.length; i++ ){
					new IntegranteCicloDAO().addIntegrante(conn, cicloGrupal.idGrupo, cicloGrupal.idCiclo, cicloGrupal.integrantes[i]);
				}
				//Referencia de pago
				pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, cicloGrupal.idCiclo);
				pagoReferenciaVO.numcliente = grupo.idGrupo;
				pagoReferenciaVO.numSolicitud = cicloGrupal.idCiclo;
				new ReferenciaGeneralDAO().addReferencia(conn, pagoReferenciaVO);
				
			}else{
				new DireccionGenericaDAO().updateDireccion(conn, cicloGrupal.direccionReunion);
				cicloGrupalDAO.updateCiclo(conn, cicloGrupal);
			}
			//CAmbio calificacion del grupo
			new GrupoDAO().updateCalificacionGrupo(conn, "A1", idGrupo);
			cicloGrupal.plazo = 16;
			//Calcula tabla de amortizacion
                        System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaRefinanciamientoGrupal"+" ciclo.idGrupo "+cicloGrupal.idGrupo+" ciclo.idCiclo "+cicloGrupal.idCiclo);
			GrupoHelper.obtenTablaAmortizacion(grupo, cicloGrupal, new Date());
			validacionDesembolso = "OK";
			conn.commit();
			bitutil.registraEvento(cicloGrupal);
			notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente") );
			//Se adieren propiedades ST24 al ciclo
			cicloGrupal.saldo = new SaldoIBSVO();
			//Se agrega cilo en cesion
			grupo.ciclos = GrupoUtil.addCiclo(grupo, cicloGrupal);
			//Se manda por request el nuevo ciclo
			request.setAttribute("NOTIFICACIONES", notificaciones);
			request.setAttribute("VALIDACION", validacionDesembolso);
			request.setAttribute("ID_CICLO", cicloGrupal.idCiclo);
			session.setAttribute("GRUPO", grupo);

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

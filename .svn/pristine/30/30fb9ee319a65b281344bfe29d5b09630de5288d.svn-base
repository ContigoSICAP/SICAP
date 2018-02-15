package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
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
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;


public class CommandGuardaCicloRestructura implements Command{


	private String siguiente;


	public CommandGuardaCicloRestructura(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		GrupoVO grupo = new GrupoVO();
		CicloGrupalVO cicloAnterior = new CicloGrupalVO();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
		CicloGrupalDAO ciclodao = new CicloGrupalDAO();
		DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
		IntegranteCicloDAO integrantesdao = new IntegranteCicloDAO();
		ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
		String validacionDesembolso = "NO";
		Connection conn  = null;
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			grupo = (GrupoVO)session.getAttribute("GRUPO");
			BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaCicloGrupal");
			int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			
			cicloAnterior = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			if ( idCiclo==0 ){
				ciclo = new CicloGrupalVO();				
				ciclo = GrupoHelper.getCicloGrupalVO(ciclo, request);
				ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
				GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
				// validación si existe ciclo activo grupal
				if( (ciclodao.getCiclo(grupo.idGrupo)) ==null){
					ciclo.idDireccionReunion = direcciondao.addDireccion(conn, ciclo.direccionReunion);
					ciclo.plazo =  HTMLHelper.getParameterInt(request, "plazo");
					ciclo.comision = 1;
					ciclo.tasa = 2;
					ciclo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
					ciclodao.addCicloGrupal(conn, ciclo);
					ciclo.fechaCaptura = new Timestamp(System.currentTimeMillis());
					//Genera solicitudes y autorizaciones
					ciclo.monto = 0;
					ciclo.montoConComision = 0;
					SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, 2, (grupo.idOperacion==ClientesConstants.REESTRUCTURA_GRUPAL ? true:false), ciclo.plazo);
					for ( int i=0 ; i<ciclo.integrantes.length ; i++ ){
						ciclo.monto += ciclo.integrantes[i].monto;
						ClienteVO cliente = new ClienteDAO().getCliente(ciclo.integrantes[i].idCliente);
						cliente.idGrupo = idGrupo;
						new ClienteDAO().updateCliente(conn, cliente);
						integrantesdao.addIntegrante(conn, idGrupo, ciclo.idCiclo, ciclo.integrantes[i]);
					}
					ciclo.montoConComision = ciclo.monto;
					ciclodao.updateCiclo(conn, ciclo);
                                        System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaCicloRestructura"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
					GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, ciclo.fechaValor);
					validacionDesembolso = "OK";
					pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, ciclo.idCiclo);
					pagoReferenciaVO.numcliente = grupo.idGrupo;
					pagoReferenciaVO.numSolicitud = ciclo.idCiclo;
					pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
					ciclo.referencia = pagoReferenciaVO.referencia; 
					conn.commit();
					notificaciones.add( new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
					bitutil.registraEvento(ciclo);
					grupo.ciclos = GrupoUtil.addCicloRestructura(grupo, ciclo);
					//GrupoUtil.addCiclo(grupo, ciclo)(grupo, ciclo, idCiclo);
					request.setAttribute("idCiclo", ciclo.idCiclo);
				}else{
					notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE, "Ya existe un ciclo activo para el grupo"));
				}
			}else{
				ciclo = GrupoHelper.getCicloGrupalVO(cicloAnterior, request);
				ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
				ciclo.integrantes = GrupoHelper.getIntegrantesCiclo(request);
				CicloGrupalVO cicloGuardado = new CicloGrupalVO();
				cicloGuardado.integrantes = integrantesdao.getIntegrantes(idGrupo, idCiclo);
				for(int i=0 ; i<ciclo.integrantes.length ; i++){
					String nombre = "desembolso"+i;
					if( !HTMLHelper.getCheckBox(request, nombre) && GrupoHelper.existeIntegrante(cicloGuardado, ciclo.integrantes[i]) )
                		integrantesdao.deleteIntegrante(conn, idGrupo, idCiclo, ciclo.integrantes[i].idCliente);
				}
				GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
				ciclo.idDireccionReunion= direcciondao.updateDireccion(conn, ciclo.direccionReunion); 
			    ciclo.plazo =  HTMLHelper.getParameterInt(request, "plazo");
				ciclo.comision = 1;
				ciclo.tasa = 2;
				ciclo.monto = 0;
				ciclo.montoConComision = 0;
				SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, 2, (grupo.idOperacion==ClientesConstants.REESTRUCTURA_GRUPAL ? true:false), ciclo.plazo);
				for ( int i=0 ; i<ciclo.integrantes.length ; i++ ){
					ciclo.monto += ciclo.integrantes[i].monto;
				}
				ciclo.montoConComision = ciclo.monto;
                if ( ciclo.estatus==ClientesConstants.CICLO_DISPERSADO )
                                System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaCicloRestructura"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
                                GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, ciclo.fechaValor);
				validacionDesembolso = "OK";
				
				ciclodao.updateCiclo(ciclo);
//				else
//					ciclo.tablaAmortizacion = cicloAnterior.tablaAmortizacion;
				GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
				conn.commit();
				notificaciones.add( new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
				bitutil.registraEvento(ciclo);
				request.setAttribute("idCiclo", ciclo.idCiclo);
		    }
			// Aquí cae validación si existe ciclo activo grupal
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza el objeto cliente
			request.setAttribute("CICLO", ciclo);
			request.setAttribute("VALIDACION", validacionDesembolso);
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

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.EmpleoVO;
import java.sql.Date;
import org.apache.log4j.Logger;


public class CommandGuardaEmpleo implements Command{

        private static Logger myLogger = Logger.getLogger(CommandGuardaEmpleo.class);
	private String siguiente;


	public CommandGuardaEmpleo(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		EmpleoVO empleo = new EmpleoVO();
		EmpleoDAO empleodao = new EmpleoDAO();
		DireccionVO direccion = null;
		DireccionDAO direcciondao = new DireccionDAO();
		Connection conn  = null;
                CicloGrupalVO cicloGrupalVO = new CicloGrupalVO();
		try{
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaEmpleo");
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			empleo = cliente.solicitudes[indiceSolicitud].empleo;
			if ( empleo==null ){
				empleo = new EmpleoVO();
				empleo = SolicitudHelper.getEmpleoVO(empleo, request);
				empleo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				empleo.fechaCaptura = new Timestamp(System.currentTimeMillis());
				direccion = DireccionHelper.getVO(new DireccionVO(), request);
				empleo.direccion = direccion;
				conn = ConnectionManager.getMySQLConnection();
				conn.setAutoCommit(false);
				empleodao.addEmpleo(conn, cliente.idCliente, idSolicitud, empleo);
				if ( direccion.idColonia!=0 )
					direcciondao.addDireccion(conn, cliente.idCliente, idSolicitud, "d_empleos", 1, direccion);
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
			}else{
				empleo = SolicitudHelper.getEmpleoVO(empleo, request);
				conn = ConnectionManager.getMySQLConnection();
				conn.setAutoCommit(false);
				direccion = DireccionHelper.getVO(empleo.direccion, request);
				empleodao. updateEmpleo(conn, cliente.idCliente, idSolicitud, empleo);
				if ( empleo.direccion!=null && direccion.idColonia!=0 )
					direcciondao.updateDireccion(conn, cliente.idCliente, idSolicitud, "d_empleos", 1, direccion);
				else if ( empleo.direccion==null && direccion.idColonia!=0 )
					direcciondao.addDireccion(conn, cliente.idCliente, idSolicitud, "d_empleos", 1, direccion);
				empleo.direccion = direccion;
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
			}
			bitutil.registraEvento(empleo);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			
                           
                       
                           //Se heredan datos
                        if(cliente.solicitudes.length > 1)
                        {
                            if(cliente.solicitudes[indiceSolicitud].documentacionCompleta == 0)
                            {
                                String razonSocialAnterior = HTMLHelper.getParameterString(request, "razonSocialAnterior");
                                int tipoSectorAnterior = HTMLHelper.getParameterInt(request, "tipoSectorAnterior");
                                int ubicacionNegocioAnterior = HTMLHelper.getParameterInt(request, "ubicacionNegocioAnterior");


                                //direccion = DireccionHelper.getVO(new DireccionVO(), request);
                                DireccionVO direccionAnterior = DireccionHelper.getVODireccionAnterior(new DireccionVO(), request);

                                if(SolicitudUtil.HayDiferenciaEntreDireccionesBasico(direccion, direccionAnterior) || SolicitudUtil.HayDiferenciaEntreDatosActEco(empleo, razonSocialAnterior, tipoSectorAnterior, ubicacionNegocioAnterior))
                                {
                                    
                                    
                                    
                                    if(!request.isUserInRole("ANALISIS_CREDITO")){
                                        CicloGrupalDAO cicloGrupalDAO = new CicloGrupalDAO();
                                        CicloGrupalVO cgEst = cicloGrupalDAO.getCiclo(cliente.solicitudes[indiceSolicitud].idGrupo, cliente.solicitudes[indiceSolicitud].idCiclo);
                                        myLogger.info("Estatuc ciclo grupalEmp: "+((cgEst != null)?cgEst.estatus:"null"));
                                        if(cgEst == null || (cgEst != null && cgEst.estatus == ClientesConstants.CICLO_APERTURA)){
                                            cliente.solicitudes[indiceSolicitud].documentacionCompleta = 1;
                                    
                                            myLogger.info("Fecha Firma: "+cliente.solicitudes[indiceSolicitud].fechaFirma);
                                            if(cliente.solicitudes[indiceSolicitud].fechaFirma==null){
                                                cliente.solicitudes[indiceSolicitud].fechaFirma = new Date(new java.util.Date().getTime());
                                            }
                                            new SolicitudDAO().updateDocumentacionCompletaSolicitud(null, cliente.solicitudes[indiceSolicitud].idCliente , indiceSolicitud, cliente.solicitudes[indiceSolicitud].documentacionCompleta, cliente.solicitudes[indiceSolicitud].fechaFirma);

                                            cliente.solicitudes[indiceSolicitud].fechaFirmaConUltimoCambioDoctos = SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(cliente);

                                            //VALIDAR AQUI SI SE DEBE CONSIDERAR ESTATUS DE GRUPO PARA NO ELIMINAR EL REGISTRO
                                            ArchivoAsociadoDAO archivoDAO = new ArchivoAsociadoDAO();
                                            archivoDAO.deleteArchivoAsociado(cliente.solicitudes[indiceSolicitud].idCliente, idSolicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
                                            cliente.solicitudes[indiceSolicitud].archivosAsociados = null;
                                        }
                                    }
                                }
                            }
                        }
                        
                       
                        //Actualiza el objeto cliente
			cliente.solicitudes[indiceSolicitud].empleo = empleo;
                        
                        //Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		finally {
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

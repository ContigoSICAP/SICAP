package com.sicap.clientes.commands;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ArrendatarioDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EconomiaObligadoDAO;
import com.sicap.clientes.dao.InformacionFinancieraDAO;
import com.sicap.clientes.dao.NegocioDAO;
import com.sicap.clientes.dao.ObligadoSolidarioDAO;
import com.sicap.clientes.dao.ReferenciaComercialDAO;
import com.sicap.clientes.dao.ReferenciaCrediticiaDAO;
import com.sicap.clientes.dao.ReferenciaLaboralDAO;
import com.sicap.clientes.dao.ReferenciaPersonalDAO;
import com.sicap.clientes.dao.ScoringDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ArrendatarioVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.InformacionFinancieraVO;
import com.sicap.clientes.vo.NegocioVO;
import com.sicap.clientes.vo.ObligadoSolidarioVO;
import com.sicap.clientes.vo.ReferenciaComercialVO;
import com.sicap.clientes.vo.ReferenciaCrediticiaVO;
import com.sicap.clientes.vo.ReferenciaLaboralVO;
import com.sicap.clientes.vo.ReferenciaPersonalVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.UsuarioVO;


public class CommandRegistraReestructura implements Command{


	private String siguiente;


	public CommandRegistraReestructura(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		HttpSession session = request.getSession();
		ClienteVO cliente = new ClienteVO();
		String siguienteConsumo = "/capturaDatosClienteConsumo.jsp";
		String sellFinanceExterno = "/capturaAbreviadaDatosCliente.jsp";
		int producto = 0;
		UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
		DireccionVO direccion = null;
		DireccionDAO direcciondao = new DireccionDAO();
		Connection conn  = null;
		
		try{
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			//producto = HTMLHelper.getParameterInt(request, "producto");
			cliente =(ClienteVO) session.getAttribute("CLIENTE");
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandRegistraReestructura");
			
			//if ( producto!=0 ){
			
			// generar metodo en solicitud util, recibe nueva solicitud y 
			// el arreglo de solicitudes para ver cual debe tomar
			// este metodo le asigna el producto y el monto a la solicitud
			// tambien en la base debe quedar otra columna para ver
			// a que credito se le está haciendo la reestructura
			
				SolicitudVO nuevaSolicitud = new SolicitudVO();	
				nuevaSolicitud.idCliente = cliente.idCliente;
				nuevaSolicitud = SolicitudUtil.calculaReestructuraIndividual(nuevaSolicitud,cliente.solicitudes);				
				nuevaSolicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
				nuevaSolicitud.estatus = ClientesConstants.ESTATUS_CAPTURADO;
				nuevaSolicitud.idCliente = cliente.idCliente;
				producto = nuevaSolicitud.tipoOperacion;
				
				int indiceSolReestruc = SolicitudUtil.getIndice(cliente.solicitudes, nuevaSolicitud.solicitudReestructura );
				//Pasa seccion propuesta comite de credito
				nuevaSolicitud.montoPropuesto = nuevaSolicitud.montoSolicitado;
				//nuevaSolicitud.plazoPropuesto = cliente.solicitudes[indiceSolReestruc].plazoPropuesto;
				//nuevaSolicitud.frecuenciaPagoPropuesta = cliente.solicitudes[indiceSolReestruc].frecuenciaPagoPropuesta;
				nuevaSolicitud.destinoCredito = cliente.solicitudes[indiceSolReestruc].destinoCredito;
				
				int idSolicitud = new SolicitudDAO().addSolicitud(conn, cliente.idCliente, nuevaSolicitud);
				
				//*************************
				//Pasa obligados solidarios
				if( cliente.solicitudes[indiceSolReestruc].obligadosSolidarios!=null ){
					ObligadoSolidarioVO[] temporalObligadosSolidarios = new ObligadoSolidarioVO[cliente.solicitudes[indiceSolReestruc].obligadosSolidarios.length];
					for(int i=0; i<cliente.solicitudes[indiceSolReestruc].obligadosSolidarios.length; i++){
						ObligadoSolidarioVO obligado = cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i];
						obligado.idSolicitud = idSolicitud;
						int idObligado = new ObligadoSolidarioDAO().addObligadoSolidario(conn, cliente.idCliente, idSolicitud, obligado);
						
						//Guarada Datos de oblidaso solidarios
						/*if( cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].circuloCredito!=null ){
							obligado.circuloCredito = cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].circuloCredito;
							obligado.circuloCredito.idSolicitud = idSolicitud;
							new CreditoDAO().addBuroCredito(conn, obligado.circuloCredito);
						}
						if( cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].buroCredito!=null ){
							obligado.buroCredito = cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].buroCredito;
							obligado.buroCredito.idSolicitud = idSolicitud;
							new CreditoDAO().addBuroCredito(conn, obligado.buroCredito);
						}*/
						if( cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].economia!=null ){
							obligado.economia = cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].economia;
							obligado.economia.idSolicitud = idSolicitud;
							new EconomiaObligadoDAO().addEconomiaObligado(conn, cliente.idCliente, idSolicitud, idObligado, obligado.economia);
						}
						if( cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].direccion!=null ){
							direccion = cliente.solicitudes[indiceSolReestruc].obligadosSolidarios[i].direccion;
							direccion.idSolicitud = idSolicitud;
							obligado.direccion = direccion;
							direcciondao.addDireccion(conn, cliente.idCliente, idSolicitud, "d_obligados_solidarios",idObligado , direccion);
						}
						//Termina guardar datos obligado solidario
						
						temporalObligadosSolidarios[i] = obligado;
					}
					nuevaSolicitud.obligadosSolidarios = temporalObligadosSolidarios;
				}
				
				//*************************
				//Pasa la informacion de calificacion crediticia
				/*if( cliente.solicitudes[indiceSolReestruc].circuloCredito!=null ){
					CreditoVO circuloCredito = cliente.solicitudes[indiceSolReestruc].circuloCredito;
					circuloCredito.idSolicitud = idSolicitud;
					new CreditoDAO().addBuroCredito(conn, circuloCredito);
					nuevaSolicitud.circuloCredito = circuloCredito;
				}
				if( cliente.solicitudes[indiceSolReestruc].buroCredito!=null ){
					CreditoVO buroCredito = cliente.solicitudes[indiceSolReestruc].buroCredito;
					buroCredito.idSolicitud = idSolicitud;
					new CreditoDAO().addBuroCredito(conn, buroCredito);
					nuevaSolicitud.buroCredito = buroCredito;
				}*/
				
				//*************************
				//Pasa informacion negocio del cliente
				if( cliente.solicitudes[indiceSolReestruc].negocio!= null ){
					NegocioVO negocio = cliente.solicitudes[indiceSolReestruc].negocio;
					negocio.idSolicitud = idSolicitud;
					new NegocioDAO().addNegocio(conn, cliente.idCliente, idSolicitud, negocio);
					
					if( cliente.solicitudes[indiceSolReestruc].negocio.direccion!=null ){
						direccion = cliente.solicitudes[indiceSolReestruc].negocio.direccion;
						direccion.idSolicitud = idSolicitud;
						negocio.direccion = direccion;
						direcciondao.addDireccion(conn, cliente.idCliente, idSolicitud, "d_negocios", 1, direccion);
					}
					nuevaSolicitud.negocio = negocio;
				}
				
				//*************************
				//Pasa seccion de informacio financiera
				if( cliente.solicitudes[indiceSolReestruc].informacion!= null ){
					InformacionFinancieraVO informacion = cliente.solicitudes[indiceSolReestruc].informacion;
					informacion.idSolicitud = idSolicitud;
					new InformacionFinancieraDAO().addInformacionFinanciera(conn, cliente.idCliente, idSolicitud, informacion);
					nuevaSolicitud.informacion = informacion;
				}
				
				//*************************
				//Pasa referencias y arrendatarios
				if( cliente.solicitudes[indiceSolReestruc].arrendatarioLocal!= null ){
					ArrendatarioVO arrendatario = cliente.solicitudes[indiceSolReestruc].arrendatarioLocal;
					arrendatario.idSolicitud = idSolicitud;
					new ArrendatarioDAO().adddArrendatario(conn, cliente.idCliente, idSolicitud, arrendatario);
					nuevaSolicitud.arrendatarioLocal = arrendatario;
				}
				if( cliente.solicitudes[indiceSolReestruc].arrendatarioDomicilio!= null ){
					ArrendatarioVO arrendatario = cliente.solicitudes[indiceSolReestruc].arrendatarioDomicilio;
					arrendatario.idSolicitud = idSolicitud;
					new ArrendatarioDAO().adddArrendatario(conn, cliente.idCliente, idSolicitud, arrendatario);
					nuevaSolicitud.arrendatarioDomicilio = arrendatario;
				}
				if( cliente.solicitudes[indiceSolReestruc].referenciasPersonales!= null ){
					ReferenciaPersonalVO[] temporalReferenciasPersonales =new ReferenciaPersonalVO[cliente.solicitudes[indiceSolReestruc].referenciasPersonales.length];
					for(int i=0; i<cliente.solicitudes[indiceSolReestruc].referenciasPersonales.length; i++){
						ReferenciaPersonalVO referenciaPersonal = cliente.solicitudes[indiceSolReestruc].referenciasPersonales[i];
						referenciaPersonal.idSolicitud = idSolicitud;
						new ReferenciaPersonalDAO().addReferenciaPersonal(conn, cliente.idCliente, idSolicitud, referenciaPersonal);
						temporalReferenciasPersonales[i] = referenciaPersonal;
					}
					nuevaSolicitud.referenciasPersonales = temporalReferenciasPersonales;
				}
				if( cliente.solicitudes[indiceSolReestruc].referenciasCrediticias!=null ){
					ReferenciaCrediticiaVO[] temporalReferenciasCrediticias = new ReferenciaCrediticiaVO[cliente.solicitudes[indiceSolReestruc].referenciasCrediticias.length];
					for(int i=0; i<cliente.solicitudes[indiceSolReestruc].referenciasCrediticias.length; i++){
						ReferenciaCrediticiaVO referenciasCrediticias = cliente.solicitudes[indiceSolReestruc].referenciasCrediticias[i];
						referenciasCrediticias.idSolicitud = idSolicitud;
						new ReferenciaCrediticiaDAO().addReferenciaCrediticia(conn, cliente.idCliente, idSolicitud, referenciasCrediticias);
						temporalReferenciasCrediticias[i] = referenciasCrediticias;
					}
					nuevaSolicitud.referenciasCrediticias = temporalReferenciasCrediticias;
				}
				if( cliente.solicitudes[indiceSolReestruc].referenciasComerciales!=null ){
					ReferenciaComercialVO[] temporalReferenciasComerciales = new ReferenciaComercialVO[cliente.solicitudes[indiceSolReestruc].referenciasComerciales.length];
					for(int i=0; i<cliente.solicitudes[indiceSolReestruc].referenciasComerciales.length; i++){
						ReferenciaComercialVO referenciasComerciales = cliente.solicitudes[indiceSolReestruc].referenciasComerciales[i];
						referenciasComerciales.idSolicitud = idSolicitud;
						new ReferenciaComercialDAO().addReferenciaComercial(conn, cliente.idCliente, idSolicitud, referenciasComerciales);
						temporalReferenciasComerciales[i] = referenciasComerciales;
					}
					nuevaSolicitud.referenciasComerciales = temporalReferenciasComerciales;
				}
				if( cliente.solicitudes[indiceSolReestruc].referenciaLaboral!=null ){
					ReferenciaLaboralVO referenciaLaboral = new ReferenciaLaboralVO();
					referenciaLaboral.idSolicitud = idSolicitud;
					new ReferenciaLaboralDAO().addReferencia(conn, cliente.idCliente, idSolicitud, referenciaLaboral);
					nuevaSolicitud.referenciaLaboral = referenciaLaboral;
				}
				
				//*************************
				//Pasa Scoring para consumo o sell finance
				if( cliente.solicitudes[indiceSolReestruc].scoring!=null ){
					ScoringVO scoringTemporal = cliente.solicitudes[indiceSolReestruc].scoring;
					scoringTemporal.idSolicitud = idSolicitud;
					new ScoringDAO().addScoring(conn, scoringTemporal);
					nuevaSolicitud.scoring = scoringTemporal;
				}
				
				
				conn.commit();
				bitutil.registraCambioEstatus(nuevaSolicitud, request);
				bitutil.registraEvento(nuevaSolicitud);
				
				int n = cliente.solicitudes.length;
				SolicitudVO[] solicitudes = new SolicitudVO[n+1];

				for( int i=0 ; i<cliente.solicitudes.length ; i++ )
					solicitudes[i] = cliente.solicitudes[i];
				
				solicitudes[n] = nuevaSolicitud;
				cliente.solicitudes = solicitudes;
				
				session.setAttribute("CLIENTE",cliente);
				request.setAttribute("ID_SOLICITUD",new Integer(idSolicitud));
			//}
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
		if ( producto==ClientesConstants.CONSUMO || producto==ClientesConstants.VIVIENDA || producto==ClientesConstants.CREDIHOGAR || (producto==ClientesConstants.SELL_FINANCE && usuario.identificador.equals("I")))
			return siguienteConsumo;
		else if( producto==ClientesConstants.SELL_FINANCE && usuario.identificador.equals("E"))
			return sellFinanceExterno;
		return siguiente;

	}


}




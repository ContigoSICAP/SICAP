package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;

/**
 * Registra pago individuales grupales capturados en la matriz de pagos comunales
 * @author JBallesteros
 *
 */
public class CommandRegistrarPagoIndividual implements Command{

	/**
	 * Pagina a la que se redireccionara despues de la ejecucion del comando
	 */
	private String siguiente;

	/**
	 * 
	 * @param siguiente Pagina a la que se redireccionara despues de la ejecucion del comando
	 */
	public CommandRegistrarPagoIndividual(String siguiente) {
		this.siguiente = siguiente;
	}

	/**
	 * Ejecuta el comando
	 */
	public String execute(HttpServletRequest req) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = req.getSession();
                Connection conn = null;
		// Obtenemos num. de grupo
		int numGrupo=Integer.parseInt(req.getParameter("idGrupo"));
		// Obtenemos num. de ciclo
		int numCiclo = Integer.parseInt(req.getParameter("idCiclo"));
		GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
		CicloGrupalVO ciclo =  GrupoUtil.getCiclo(grupo.ciclos, numCiclo);
		
		try {
			PagoGrupalDAO pagoGrupaldao=new PagoGrupalDAO();
			PagoGrupalVO pagoGrupal=new PagoGrupalVO();
			PagoIndividualGruposDAO pagoIndividualdao=new PagoIndividualGruposDAO();
//			PagoGrupalVO[] pagos=null;
//			pagos=pagoGrupaldao.getPagosGrupales_(numGrupo, numCiclo);
			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);

			for(int i = 0; i<ciclo.tablaAmortizacion.length;i++) {
				for( int a = 0; a<ciclo.tablaAmortizacion.length; a++){
					String numPago= req.getParameter("numPago"+a);
					String numAmortizacion= req.getParameter("numAmortizacion"+a);
					
					if( i==Integer.parseInt(numAmortizacion) ){
						String valorSolidario= req.getParameter("solidario"+a);
						String valorAhorro = req.getParameter("ahorro"+a);
						String valorMulta = req.getParameter("multa"+a);
						
						for(int h = 0; h<ciclo.pagosGrupales.length; h++){
							if( ciclo.pagosGrupales[h].numPago==Integer.parseInt(numPago) ){
								pagoGrupal= ciclo.pagosGrupales[h];				
								pagoGrupal.solidario = Double.parseDouble(valorSolidario);
								pagoGrupal.ahorro = Double.parseDouble(valorAhorro);
								pagoGrupal.multa = Double.parseDouble(valorMulta);
								pagoGrupal.numGrupo = numGrupo;
								pagoGrupal.numCiclo = numCiclo;
								if( pagoGrupal.numAmortizacion==0  )
									pagoGrupal.numAmortizacion = Integer.parseInt(numAmortizacion);
								//pagoGrupal.numPago = i;
								pagoGrupaldao.updatePagoGrupal(pagoGrupal);
							}
						}
						
					}
						
				}
							
			}
// JBL, el ciclo se inicia desde 0 para tomar en cuenta los depósitos de garantía ENE/11
			for(int i = 0; i<ciclo.tablaAmortizacion.length;i++) {  //Busca los ciclo.pagosGrupales a realizar
				
				//Revisamos que pago se capturaron
				String valoresMatriz[] = req.getParameterValues("pago"+i);
				boolean siEsPago = false;
				for( int j=0 ; j<valoresMatriz.length ; j++ ) {
					if( Double.parseDouble(valoresMatriz[j]) > 0 )
						siEsPago = true;
				}
				
//				Valido si es pago 16 y tiene saldo vencido
				if( ciclo.tablaAmortizacion[i].numPago == 16 && ciclo.saldo.getEstatus()!=ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO && siEsPago ){
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No es pocible registrar el pago 16 aun cuenta con saldo vencido");
					break;
				}
				
				if(valoresMatriz==null) {
					continue;
				}
				else {
					//Se obtiene el pago grupal correspondiente
					
					pagoGrupal=pagoGrupaldao.getPagoGrupal(numGrupo, numCiclo, i);
					if( pagoGrupal==null ){
						pagoGrupal = new PagoGrupalVO();
						pagoGrupal.numPago = i;
					}
						
					for( int j=0 ; j<valoresMatriz.length ; j++ ) {
						
						IntegranteCicloVO[] integrantes = ciclo.integrantes;
						if(pagoIndividualdao.existePagoIndividual(numGrupo, numCiclo, i, true)) {
							
							for (int z=0;z<integrantes.length;z++){
								if(integrantes==null) { }
								else if( siEsPago ){
									PagoIndividualGruposVO pagoIndividual=new PagoIndividualGruposVO();
									pagoIndividual.monto=Double.parseDouble(valoresMatriz[z]);  
									pagoIndividual.numCiclo=numCiclo;
									pagoIndividual.numCliente=integrantes[z].idCliente;
									pagoIndividual.numGrupo=numGrupo;
									pagoIndividual.numPago=pagoGrupal.numPago;
									pagoIndividual.usuario=req.getRemoteUser();
									pagoIndividual.corroborar = 1;
									//pagoIndividualdao.addPagoIndividualGrupos(conn, pagoIndividual);
									pagoIndividualdao.updatePagoIndividualGrupos(conn, numGrupo, numCiclo, pagoGrupal.numPago, integrantes[z].idCliente, pagoIndividual);
									notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Se han registrado los pagos individuales");
								}
								
							}
                                                        conn.commit();
							req.setAttribute("idGrupo", Integer.toString(numGrupo));
							req.setAttribute("idCiclo", Integer.toString(numCiclo));
						}else if( pagoIndividualdao.existePagoIndividual(numGrupo, numCiclo, i) ){
							//no hace nada
							break;
						}else{
							// Se registran ciclo.pagosGrupales individuales capturados
							for (int z=0;z<integrantes.length;z++){
								if(integrantes==null) { }
								else if( siEsPago ){
									PagoIndividualGruposVO pagoIndividual=new PagoIndividualGruposVO();
									pagoIndividual.monto=Double.parseDouble(valoresMatriz[z]);  
									pagoIndividual.numCiclo=numCiclo;
									pagoIndividual.numCliente=integrantes[z].idCliente;
									pagoIndividual.numGrupo=numGrupo;
									pagoIndividual.numPago=pagoGrupal.numPago;
									pagoIndividual.usuario=req.getRemoteUser();
									pagoIndividual.corroborar = 1;
									pagoIndividualdao.addPagoIndividualGrupos(conn, pagoIndividual);
									//pagoIndividualdao.updatePagoIndividualGrupos(conn, numGrupo, numCiclo, pagoGrupal.numPago, integrantes[z].idCliente, pagoIndividual);
									notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Se han registrado los pagos individuales");
								}
								
							}
                                                        conn.commit();
							// METER LOS PAGOS AL GRUPO DE MULTA, SOLIDARIO, ETC.. ENVIAR LA CONEXION
							req.setAttribute("idGrupo", Integer.toString(numGrupo));
							req.setAttribute("idCiclo", Integer.toString(numCiclo));
						}
					}
				}
			}
                        conn.close();
		}
		catch (ClientesException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		catch (NamingException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException sqle) {
                        throw new CommandException(sqle.getMessage());
                    }
                }

		req.setAttribute("NOTIFICACIONES",notificaciones);
		return siguiente;
	}	//fin command
	
}

package com.sicap.clientes.commands;


import com.sicap.clientes.dao.IntegranteCicloDAO;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import org.apache.log4j.Logger;


public class CommandConsultaOrdenDePago implements Command{
        private static Logger myLogger = Logger.getLogger(CommandConsultaOrdenDePago.class);

	private String siguiente;


	public CommandConsultaOrdenDePago(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();
                /**
                 * JECB 01/10/2017
                 * Se agregan referencias al dao de saldos 
                 * y de integrante ciclo
                 */
		SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                IntegranteCicloDAO integranteCicloDao = new IntegranteCicloDAO();
                
		try{
			int idSucursal    = HTMLHelper.getParameterInt(request, "sucursal");
			int idCliente     = HTMLHelper.getParameterInt(request, "idCliente");
			String referencia = HTMLHelper.getParameterString(request, "referencia");
			
			OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
			
			OrdenDePagoVO ordenDePago =  opagodao.getOrdenDePago(idCliente, idSucursal, referencia);
			
			if( ordenDePago == null ){
				notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"No se encontraron Registros con los Datos Proporcionados"));
			}
                        
                        /**
                         * JECB 01/10/2017
                         * Se sube a sesion atributo
                         * para determinar si la orden de pago se trata de un 
                         * credito adicional
                         */
                        boolean isOrderPagoAdicional = AdicionalUtil.isOrdenPagoAdicional(ordenDePago);
                        request.setAttribute("ISORDENPAGOADICIONAL", new Boolean(isOrderPagoAdicional));
                        
                        /**
                         * JECB 01/10/2017
                         * Regla de negocio donde que permite continuar 
                         * con el procedimiento de cancelacion de ordenes de pago 
                         * de adicional siempre y cuando la semana en que se disperso el adicional 
                         * no sea mayor a las semanas transcurridas en el credito
                         */
                        boolean procedeCancelacionOrdenPagoAdic = false;
                        if(isOrderPagoAdicional){
                            IntegranteCicloVO icVO =  integranteCicloDao.getIntegrantesCicloFromOrdenPago(ordenDePago);
              
                            SaldoIBSVO saldo = saldoDAO.getSaldos(icVO.getIdGrupo(),icVO.getIdCiclo());
                            
                            int tipoAdicional = integranteCicloDao.obtenerTipoAdicionalIntegranteCiclo(saldo.getIdClienteSICAP(), 
                                    saldo.getIdSolicitudSICAP(), ordenDePago.getReferencia());
                            myLogger.debug("tipoAdicional:" + tipoAdicional);
                            int plazoAdicional = AdicionalUtil.plazoAdicional(tipoAdicional);
                            myLogger.debug("plazoAdicional:" + plazoAdicional);
                            myLogger.debug("semanas transcurridas:" + saldo.getNumeroCuotasTranscurridas());
                            if(saldo.getNumeroCuotasTranscurridas() > plazoAdicional){
                                procedeCancelacionOrdenPagoAdic = false;
                            }else{
                                procedeCancelacionOrdenPagoAdic = true;
                            }
                            request.setAttribute("PROCEDECANCELACIONADICIONAL", new Boolean(procedeCancelacionOrdenPagoAdic));
                        }
                        
                        /**
                         * JECB 01/10/2017
                         * Regla de negocio que verifica el intento de cancelacion de una orden 
                         * de pago de un cliente, permitiendola realizar siempre y cuando
                         * el cliente no tenga asignado ordenes de pago 
                         * relacionadas con creditos adicionales 
                         */
                        boolean clienteConOrdPagoAdic = false;
                        if(ordenDePago != null && !isOrderPagoAdicional){
                            clienteConOrdPagoAdic = opagodao.clienteCuentaConOrdenesDePagoDeCreditoAdicional(ordenDePago);
                        }
                        if(clienteConOrdPagoAdic){
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"La orden de pago no se puede cancelar porque el cliente tiene ordenes de pago de crédito adicional"));
                        }
                        request.setAttribute("CTETIENEORDPAGADICIONALES", new Boolean(clienteConOrdPagoAdic));
                        
			myLogger.debug("Devolviendo información al jsp");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("ORDENDEPAGO", ordenDePago);
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

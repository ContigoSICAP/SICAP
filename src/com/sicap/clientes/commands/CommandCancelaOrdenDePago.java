package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
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
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import org.apache.log4j.Logger;

public class CommandCancelaOrdenDePago implements Command {
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCancelaOrdenDePago.class);
    
    public CommandCancelaOrdenDePago(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
        IntegranteCicloDAO integrantedao = new IntegranteCicloDAO();
        SolicitudVO solicitud = null;
        OrdenDePagoVO oPago = null;
        IntegranteCicloVO integranteCiclo = null;
        CicloGrupalVO ciclo = new CicloGrupalVO();
        Connection conn = null;
        int idCiclo = 0;
        int idGrupo = 0;
        int res = 1;
        boolean procesoOK = false;
        int estatus = 0;
        
        try {
            int idOrden = HTMLHelper.getParameterInt(request, "idOrden");
            int operacion = HTMLHelper.getParameterInt(request, "idOperacion");
            myLogger.info("Cancelando orde : " + idOrden + ", operacion : " + operacion);
            
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            
            oPago = opagodao.getOrdenDePago(conn, idOrden);
            
            BitacoraUtil bitutil = new BitacoraUtil(oPago.getIdCliente(), request.getRemoteUser(), "CommandCancelaOrdenDePago");
            
            if (operacion == 1) {
                /**
                 * ******************************************************
                 * Actualizando Orden De Pago *
                 * ******************************************************
                 */
                
                oPago.setNombre(new ClienteDAO().getCliente(oPago.getIdCliente()).nombreCompleto);
                if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                    oPago.setEstatus(ClientesConstants.OP_ACTUALIZA_NOMBRE);
                }
                oPago.setUsuario(request.getRemoteUser());
                
                if (opagodao.updateOrdenDePago(conn, oPago) == 1) {
                    conn.commit();
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se actualizo correctamente la Orden de Pago"));
                    bitutil.registraEvento(oPago);
                } else {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Ocurrio un error al Actualizar la Orden de Pago"));
                }
                
            } else if (operacion == 2) {
                if (!SolicitudUtil.creditoEnviado(oPago.getIdCliente(), oPago.getIdSolicitud(), oPago.getIdOperacion()) || request.isUserInRole("SOPORTE_OPERATIVO")) {
                    
                    if (oPago.getEstatus() == ClientesConstants.OP_DESEMBOLSADO || oPago.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                        estatus = ClientesConstants.OP_CANCELACION_CONFIRMADA;
                    } else {
                        estatus = ClientesConstants.OP_SOLICITA_CANCELACION;
                    }
                    
                    oPago.setUsuario(request.getRemoteUser());
                    oPago.setEstatus(estatus);
                    oPago.setFechaCancelacion(new Timestamp(System.currentTimeMillis()));
                    
                    TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(oPago.getIdOperacion());
                    
                    solicitud = new SolicitudDAO().getSolicitud(oPago.getIdCliente(), oPago.getIdSolicitud());
                    solicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
                    solicitud.fechaDesembolso = null;
                    solicitud.numCheque = null;
                    
                    if (oPago.getIdOperacion() == ClientesConstants.GRUPAL) {
                        /**
                         * ******************************************************
                         * Cancelando Orden De Pago Grupal *
                         * ******************************************************
                         */
                        integranteCiclo = integrantedao.getIntegranteCiclo(oPago.getIdCliente(), oPago.getIdSolicitud());
                        idCiclo = integranteCiclo.idCiclo;
                        idGrupo = integranteCiclo.idGrupo;
                        
                        if (opagodao.updateOrdenDePago(conn, oPago) == 1) {
                            res = new SolicitudDAO().updateSolicitud(conn, oPago.getIdCliente(), solicitud);
                            ciclo = new CicloGrupalDAO().getCiclo(idGrupo, idCiclo);
                            if (res == 1) {
                                if (ciclo.idTipoCiclo != ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO) {
                                    res = integrantedao.deleteIntegrante(conn, idGrupo, idCiclo, oPago.getIdCliente());
                                    if (res == 1) {
//										double montoTotal = integrantedao.getMontoTotalCiclo(integranteCiclo.idGrupo, integranteCiclo.idCiclo);
                                        ciclo.integrantes = integrantedao.getIntegrantes(conn, idGrupo, idCiclo);
                                        ciclo.montoConComision = 0;
                                        ciclo.monto = 0;
                                        
                                        for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                                            ciclo.montoConComision += ciclo.integrantes[i].monto;
                                            ciclo.monto += ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                            //Sumando Prima del Seguro
                                            ciclo.monto += ciclo.integrantes[i].primaSeguro;
                                            ciclo.montoConComision += ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal);
                                        }
                                        new CicloGrupalDAO().updateCiclo(conn, ciclo);
                                    }
                                } else {
                                    ciclo.integrantes = integrantedao.getIntegrantes(idGrupo, idCiclo);
                                    GrupoUtil.procesaCancelaDesembolsoRefinanciamiento(conn, ciclo, oPago.getIdCliente());
                                }
                                GrupoVO grupo = new GrupoDAO().getGrupo(idGrupo);
                                //DecisionComiteVO decision = new DecisionComiteDAO().getDecisionComite(oPago.getIdCliente(), oPago.getIdSolicitud());
                                TablaAmortizacionVO[] tabla = new TablaAmortizacionDAO().getElementos(idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                                //int plazo = tabla.length-1;
                                Date fechaInicio = new Date();
                                if (tabla != null && tabla.length > 0) {
                                    fechaInicio = tabla[0].fechaPago;
                                }
//								tabla = GrupoHelper.obtenTablaAmortizacion(grupo, idCiclo, montoTotal, decision.comision, fechaInicio, plazo);
                                myLogger.debug("User: " + request.getRemoteUser() + ", grupo: " + ciclo.idGrupo + ", ciclo: " + ciclo.idCiclo);
                                GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, fechaInicio);
                                procesoOK = true;
                            }
                        }
                    } else {
                        /**
                         * ******************************************************
                         * Cancelando Orden De Pago Individual *
                         * ******************************************************
                         */
                        if (opagodao.updateOrdenDePago(conn, oPago) == 1) {
                            res = new SolicitudDAO().updateSolicitud(conn, oPago.getIdCliente(), solicitud);
                            if (res == 1) {
                                procesoOK = true;
                            }
                        }
                    }
                    
                    if (procesoOK) {
                        conn.commit();
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se cancelo correctamente la Orden de Pago No. " + oPago.getReferencia()));
                        bitutil.registraEvento(solicitud);
                        bitutil.registraEvento(oPago);
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se puede cancelar la Orden de Pago debido a que el cliente ha sido enviado al sistema de cartera"));
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("SQLException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        
        return siguiente;
    }
}

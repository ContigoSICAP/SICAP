package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import org.apache.log4j.Logger;

public class CommandCancelaOrdenDePagoConfirmada implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCancelaOrdenDePagoConfirmada.class);

    public CommandCancelaOrdenDePagoConfirmada(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
        IntegranteCicloDAO integrantedao = new IntegranteCicloDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        SolicitudVO solicitud = null;
        OrdenDePagoVO oPago = null;
        IntegranteCicloVO integranteCiclo = null;
        CicloGrupalVO ciclo = new CicloGrupalVO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        Connection conn = null;
        int idCiclo = 0;
        int idGrupo = 0;
        int res = 1;
        boolean procesoOK = false;
        int estatus = 0;

        try {
            int idOrden = HTMLHelper.getParameterInt(request, "idOrden");
            int tipoOper = HTMLHelper.getParameterInt(request, "idOperacion");

            /**
             * JECB 01/10/2017
             * Se obtiene de sesion el tipo de orden de pago que es;
             * true en caso de que se trate de una orden de pago de credito
             * adicional
             */
            boolean isOrdenPagoAdicional = request.getParameter("ordenPagoAdicional") != null ?
                    new Boolean(request.getParameter("ordenPagoAdicional")).booleanValue() : false;
            
            myLogger.debug("CommandCancelaOrdenDePagoConfirmada isOrdenPagoAdicional:" + isOrdenPagoAdicional);
            
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);

            oPago = opagodao.getOrdenDePago(conn, idOrden);

            BitacoraUtil bitutil = new BitacoraUtil(oPago.getIdCliente(), request.getRemoteUser(), "CommandCancelaOrdenDePagoConfirmada");

            if (!SolicitudUtil.creditoEnviado(oPago.getIdCliente(), oPago.getIdSolicitud(), oPago.getIdOperacion()) || request.isUserInRole("SOPORTE_OPERATIVO")) {
                myLogger.info("No enviado");
                if (oPago.getEstatus() == ClientesConstants.OP_DESEMBOLSADO) {
                    estatus = ClientesConstants.OP_CANCELACION_CONFIRMADA;
                } else {
                    estatus = ClientesConstants.OP_SOLICITA_CANCELACION;
                }

                oPago.setUsuario(request.getRemoteUser());
                oPago.setEstatus(estatus);
                oPago.setFechaCancelacion(new Timestamp(System.currentTimeMillis()));

                solicitud = new SolicitudDAO().getSolicitud(oPago.getIdCliente(), oPago.getIdSolicitud());
                myLogger.info("tipoOper " + tipoOper);
                if (tipoOper == 2) {
                    solicitud.desembolsado = ClientesConstants.CANCELADO;
                    solicitud.fechaDesembolso = null;
                }
                solicitud.numCheque = null;

                if (oPago.getIdOperacion() == ClientesConstants.GRUPAL) {
                    /**
                     * ******************************************************
                     * Cancelando Orden De Pago Grupal *
                     * ******************************************************
                     */
                    myLogger.info("Es grupal");
                    integranteCiclo = integrantedao.getIntegranteCiclo(oPago.getIdCliente(), oPago.getIdSolicitud());
                    idCiclo = integranteCiclo.idCiclo;
                    idGrupo = integranteCiclo.idGrupo;

                    int contadorIntegrantes = 0;
                    IntegranteCicloVO[] integrantesTemporal = new IntegranteCicloDAO().getIntegrantes(idGrupo, idCiclo);
                    for (int i = 0; i < integrantesTemporal.length; i++) {
                        if (integrantesTemporal[i].estatus != ClientesConstants.INTEGRANTE_CANCELADO) {
                            contadorIntegrantes++;
                        }
                    }
                    if (contadorIntegrantes >= 6) {
                        if (opagodao.updateOrdenDePago(conn, oPago) == 1) {
                            myLogger.info("Actualizo la orden de pago");
                            /**
                             * JECB 01/10/2017
                             * Si la orden de pago es referente a un adicional 
                             * no se actualiza la solicitud de credito ya que en los
                             * ordenes de pago de producto de credito comunal 
                             * si se cancela las solicitudes
                             */
                            if (!isOrdenPagoAdicional) {
                                res = new SolicitudDAO().updateSolicitud(conn, oPago.getIdCliente(), solicitud);
                            }
                            myLogger.info("Actualizo la solicitud");
                            ciclo = new CicloGrupalDAO().getCiclo(idGrupo, idCiclo);
                            if (res == 1) {
                                if (ciclo.idTipoCiclo != ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO) {
                                    //IntegranteCicloVO integranteTemp= integrantedao.getIntegranteCiclo(oPago.getIdCliente(), idCiclo);
                                    /**
                                     * JECB 01/10/2017
                                     * Se modica flujo de la cancelacion de una orden de pago
                                     * en caso de tratarse de una orden de pago de adicional
                                     * se prescinde de la funcionalidad de 
                                     * cancelar el integrante del ciclo
                                     */
                                    if (tipoOper == 2 && !isOrdenPagoAdicional) {
                                        integranteCiclo.estatus = ClientesConstants.INTEGRANTE_CANCELADO;
                                        res = integrantedao.updateIntegrante(conn, idGrupo, idCiclo, integranteCiclo);
                                        myLogger.info("Actualizo integrante");
                                        saldoVO = saldoDAO.getSaldos(idGrupo, idCiclo);
                                        saldoVO.setNumIntegrantesCancelacion(saldoVO.getNumIntegrantesCancelacion() + 1);
                                        saldoDAO.updateIntegrantesCancelados(conn, saldoVO);
                                        myLogger.info("Actualizo Integrantes Cancelados");
                                    }
                                } else {
                                    procesoOK = false;
                                }
                                procesoOK = true;
                            }
                        }
                    } else {
                        procesoOK = false;
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se puede cancelar la orden de pago, el ciclo debe contar con un mínimo de 5 integrantes"));
                    }
                }
                if (procesoOK) {
                    conn.commit();
                    myLogger.info("Se dio commit a la operacion");
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se canceló correctamente la orden de pago : " + oPago.getReferencia()));
                    bitutil.registraEvento(solicitud);
                    bitutil.registraEvento(oPago);
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Problemas al cancelar la orde de pago"));
                    conn.rollback();
                    myLogger.info("Rollback de la operacion");
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se puede cancelar la orden de pago, el crédito ha sido enviado al sistema de cartera"));
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception ", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }

        return siguiente;
    }
}

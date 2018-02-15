package com.sicap.clientes.commands;

import java.util.Enumeration;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.SaldoHistoricoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.EjecutivosHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.Logger;

public class CommandReasignarCartera implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandReasignarCartera.class);

    public CommandReasignarCartera(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Connection con = null;
        try {
            Notification notificaciones[] = new Notification[1];
            BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "CommandReasignarCartera");
            int idSucursal = 0;
            int idEjecutivoOrigen = 0;
            int idEjecutivoDestino = 0;
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            idEjecutivoOrigen = HTMLHelper.getParameterInt(request, "idEjecutivoOrigen");
            idEjecutivoDestino = HTMLHelper.getParameterInt(request, "idEjecutivoDestino");
            boolean cambioHist = false;
            if(request.getParameter("cambioHist") != null)
                cambioHist = true;
            int actualiza = 0;
            SolicitudDAO solicituddao = new SolicitudDAO();
            TreeMap catEjecutivosOrigen = new TreeMap();
            TreeMap catEjecutivosDestino = new TreeMap();
            int numEquipos = 0;
            ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();
            ArrayList<CicloGrupalVO> arrEquiposAsig = new ArrayList<CicloGrupalVO>();
            Calendar fechaHistorica = Calendar.getInstance();
            
            if (idSucursal > 0) {
                if (idEjecutivoOrigen > 0) {
                    catEjecutivosOrigen = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");
                    request.setAttribute("EJECUTIVOS_ORIGEN", catEjecutivosOrigen);
                    SolicitudVO[] solicitudes = solicituddao.getSolicitudesByEjecutivo(idEjecutivoOrigen);
                    if (solicitudes != null && solicitudes.length > 0) {
                        request.setAttribute("CARTERA_CLIENTES", solicitudes);
                    }
                    if (idEjecutivoDestino > 0) {
                        //catEjecutivosDestino = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");
                        catEjecutivosDestino = catEjecutivosOrigen;
                        request.setAttribute("EJECUTIVOS_DESTINO", catEjecutivosDestino);
                        //AQUI SE VALIDA QUE EL ORIGEN DESTINO Y EL ORIGEN SEAN DIFERENTES
                        numEquipos = HTMLHelper.getParameterInt(request, "numEquipos");
                        if(numEquipos == 0){
                            Enumeration parametros = request.getParameterNames();  //obtiene todos los parámetros
                            while (parametros.hasMoreElements()) {
                                String nombreParametro = (String) parametros.nextElement();
                                String[] valores = request.getParameterValues(nombreParametro);
                                /*
                                 if(valores.length == 1) {
                                 String valorParametro = valores[0]; 					
                                 }
                                 */
                                //el contenido de esos valores
                                for (int i = 0; i < valores.length; i++) {
                                    if (nombreParametro.equals("cambioEjecutivo")) {	//busca por las checkboxes que se llaman así
                                        myLogger.debug("Estos clientes se tienen que cambiar de ejecutivo: " + valores[i]);

                                        // cambios para enviar la solicitud
                                        String[] idClienteidSolicitud = valores[i].split(",");

                                        //int idCliente = Integer.parseInt(valores[i]);
                                        int idCliente = Integer.parseInt(idClienteidSolicitud[0]);
                                        int idSolicitud = Integer.parseInt(idClienteidSolicitud[1]);

                                        myLogger.debug("Cliente a cambiar: " + idCliente);
                                        myLogger.debug("Solicitud a cambiar: " + idSolicitud);

                                        SolicitudVO solicitud = solicituddao.getSolicitud(idCliente, idSolicitud);
                                        //asigna el nuevo ejecutivo el marcado como destino
                                        solicitud.idEjecutivo = idEjecutivoDestino;
                                        //tiene que hacer ciertas validaciones...asumimos el happy path
                                        actualiza = solicituddao.updateSolicitud(idCliente, solicitud);
                                        // Verifica si el tipo de operacion es comunal
                                        if (solicitud.tipoOperacion == ClientesConstants.GRUPAL) {
                                            IntegranteCicloVO integrante = new IntegranteCicloVO();
                                            IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
                                            CicloGrupalVO ciclo = new CicloGrupalVO();
                                            CicloGrupalDAO cicloGrupal = new CicloGrupalDAO();
                                            SaldoIBSVO saldo = new SaldoIBSVO();
                                            SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                                            integrante = integranteDAO.getIntegranteCiclo(idCliente, idSolicitud);
                                            ciclo = cicloGrupal.getCiclo(integrante.idGrupo, integrante.idCiclo);
                                            ciclo.asesor = solicitud.idEjecutivo;
                                            cicloGrupal.updateCiclo(ciclo);
                                            saldo = saldoDAO.getSaldo(ciclo.idGrupo, ciclo.idCiclo, ClientesConstants.GRUPAL);
                                            saldo.setCtaContable(solicitud.idEjecutivo);
                                            saldoDAO.updateSaldo(saldo);
                                        }
                                    }
                                }	//fin-for
                            }  //fin-while
                        } else {
                            try {
                                arrEquipos = new EjecutivosHelper().getAsignacionCarteraHelper(request);
                                con = ConnectionManager.getMySQLConnection();
                                con.setAutoCommit(false);
                                boolean error = false;
                                fechaHistorica.add(Calendar.MONTH, -1);
                                fechaHistorica.set(Calendar.DATE, fechaHistorica.getActualMaximum(Calendar.DATE));
                                for (CicloGrupalVO equipos : arrEquipos) {
                                    if(!new SaldoIBSDAO().setEjecutivo(equipos, idEjecutivoDestino, con))
                                        error = true;
                                    if(!new CicloGrupalDAO().setEjecutivo(equipos, idEjecutivoDestino, con))
                                        error = true;
                                    if(cambioHist){
                                        if(!new SaldoHistoricoDAO().setEjecutivoHistorico(new Date(fechaHistorica.getTime().getTime()),equipos, idEjecutivoDestino, con))
                                        error = true;
                                    }
                                    if(!error){
                                        bitacora.registraEventoString("grupo="+equipos.getIdGrupo()+", ciclo="+equipos.getIdCiclo()+" ejecutivo="+idEjecutivoDestino);
                                        actualiza++;
                                    }
                                }
                                con.commit();
                                request.setAttribute("CARTERA_CLIENTES", null);
                            } catch (Exception e) {
                                myLogger.error("Exception", e);
                            }                            
                        }
                        if (actualiza > 0) {
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se ha realizado el cambio de cartera-cliente");
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                        } else {
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "No se ha realizado el cambio de cartera-cliente");
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                        }
                    } //fin-if
                    else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado el ejecutivo destino o no está dado de alta");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado el ejecutivo de origen");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }  //fin else
            request.setAttribute("diaHabil", true);
        } catch (ClientesDBException dbe) {
            myLogger.error("Problema dentro CommandReasignarCartera", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro CommandReasignarCartera", e);
            throw new CommandException(e.getMessage());
        }finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("Problema dentro CommandReasignarCartera", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;

    }
}

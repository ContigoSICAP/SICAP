package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteIntercicloDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteIntercicloVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class CommandGuardaInterCiclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaInterCiclo.class);

    public CommandGuardaInterCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        ClientesConstants ClientesConstants = new ClientesConstants();
        GrupoVO grupo = new GrupoVO();
        SolicitudVO solicitudes[] = null;
        SaldoIBSVO saldo = new SaldoIBSVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        CicloGrupalVO cicloAnterior = new CicloGrupalVO();
        TablaAmortVO tablaVO = null;
        DecisionComiteVO decisionComite = null;
        ClienteIntercicloVO clienteICVO = new ClienteIntercicloVO();
        SolicitudVO solicitudVO = new SolicitudVO();
        BitacoraCicloVO bitacoraVO = new BitacoraCicloVO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO[] integrantesIntercicloVO = null;
        IntegranteCicloVO elementos[] = null;
        Connection conn = null;
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ClienteIntercicloDAO clienteICDAO = new ClienteIntercicloDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        InterCicloHelper interCicloHelper = new InterCicloHelper();
        String usuarioAutorizacion = "";

        boolean autorizado = true;

        int i, j = 0;

        int res = 0;
        try {

            conn = ConnectionManager.getMySQLConnection();
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            conn.setAutoCommit(false);
            boolean elimina = true;
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaInterCiclo");
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            int semDisp = HTMLHelper.getParameterInt(request, "semDisp");
            java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
            if (grupo.ciclos != null) {
                saldo = saldoDAO.getSaldos(idGrupo, idCiclo);
                if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                    if (saldo.getNumeroCuotasTranscurridas() <= ClientesConstants.DISPERSION_SEMANA_4) {
                        tablaVO = new TablaAmortDAO().getDivPago(idGrupo, saldo.getCredito(), semDisp);
                        if (interCicloHelper.validaCambioEstatus(semDisp, estatus, idGrupo, saldo.getCredito(), fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))) {
                            ciclo = GrupoHelper.getCicloGrupalApertura(ciclo, request);
                            ciclo = GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);//Clientes en el jsp selecionados
                            
                            myLogger.info("num integrantes ciclo seleccionados: "+ciclo.integrantes.length);
                            
                            //Validamos si hay interciclos autorizados por excepción
                            int numeroInterciclosAutorizadosPorExepcion = obtenerAutorizadosExcepcionInterciclo(ciclo);
                            
                            //Si hay interciclo autorizado por excepcion, se rechaza el interciclo
                            if(numeroInterciclosAutorizadosPorExepcion == 0){
                            
                                if (estatus == ClientesConstants.CICLO_ANALISIS || estatus == ClientesConstants.CICLO_REVALORACION) {
                                    for (i = 0; i < ciclo.integrantes.length; i++) {
                                        decisionComite = decisionComiteDAO.getDecisionComite(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                                        if (decisionComite == null) {
                                            autorizado = false;
                                            myLogger.debug("El integrante" + ciclo.integrantes[i].idCliente + " no tiene Autorizacion");
                                        }
                                    }
                                }
                                if (autorizado) {
                                    integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);// Integratnes ciclo Original
                                    integrantesIntercicloVO = new IntegranteCicloDAO().getIntegrantesNuevoInterCiclo(grupo.getIdGrupo(), idCiclo);//integratnes Interciclo
                                    if (estatus == ClientesConstants.CICLO_AUTORIZADO) {
                                        autorizado = interCicloHelper.validaDevolicionODP(integrantesCicloVO);
                                        if (!autorizado) {
                                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible Autorizar el equipo ya que existen Ordenes de Pago canceladas sin devolver"));
                                        }
                                    }
                                    if (autorizado) {
                                        if (CatalogoHelper.esSucursalAceptaRegular(grupo.sucursal)) {
                                            ciclo.integrantesArray = GrupoHelper.integraInterciclo(integrantesCicloVO, ciclo.integrantes);
                                            autorizado = GrupoHelper.validaAceptaRegulares(grupo, ciclo);
                                            if (!autorizado) {
                                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El equipo no cumple con la cantidad de clientes Regulares"));
                                            }
                                        }
                                    }
                                    if (autorizado) {
                                        if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                            ciclo.estatusIC = estatus;
                                        }
                                        if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                            ciclo.estatusIC2 = estatus;
                                        }
                                        for (i = 0; i < integrantesIntercicloVO.length; i++) {
                                            elimina = true;
                                            for (j = 0; j < ciclo.integrantes.length; j++) {
                                                if (ciclo.integrantes[j].idCliente == integrantesIntercicloVO[i].idCliente && ciclo.integrantes[j].idSolicitud == integrantesIntercicloVO[i].idSolicitud) {
                                                    elimina = false;
                                                }
                                            }
                                            if (elimina) {
                                                clienteICVO = new ClienteIntercicloVO();
                                                clienteICVO.setNumGrupo(grupo.getIdGrupo());
                                                clienteICVO.setNumCiclo(idCiclo);
                                                clienteICVO.setNumCliente(integrantesIntercicloVO[i].idCliente);
                                                clienteICVO.setNumSolicitud(integrantesIntercicloVO[i].idSolicitud);
                                                clienteICVO.setSemDispercion(semDisp);
                                                clienteICVO.setEstatus(2);
                                                clienteICDAO.updateIntegranteInterCiclo(conn, clienteICVO);
                                                solicitudVO = new SolicitudVO();
                                                solicitudVO = solicitudDAO.getSolicitud(integrantesIntercicloVO[i].idCliente, integrantesIntercicloVO[i].idSolicitud);
                                                solicitudVO.setSubproducto(0);
                                                solicitudDAO.updateSolicitud(conn, integrantesIntercicloVO[i].idCliente, solicitudVO);
                                            } else {
                                                solicitudes = solicituddao.getSolicitudes(integrantesIntercicloVO[i].idCliente);
                                                for (int y = 0; y < solicitudes.length; y++) {
                                                    if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                                        integrantesIntercicloVO[i].RenovacionDoc = 0;
                                                        if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new java.util.Date()) == 1) {
                                                            integrantesIntercicloVO[i].RenovacionDoc = 1;
                                                        }
                                                    }
                                                }
                                                if (integrantesIntercicloVO[i].RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                                                    integrantesIntercicloVO[i].RenovacionDoc = 1;
                                                }
                                                if (integrantesIntercicloVO[i].RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                                                    integrantesIntercicloVO[i].DocCompletos = 1;
                                                } else if (integrantesIntercicloVO[i].RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta == 0 && (ciclo.estatusIC == ClientesConstants.CICLO_ANALISIS || ciclo.estatusIC == ClientesConstants.CICLO_REVALORACION)) {
                                                    autorizado = false;
                                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible enviar el equipo por que al cliente: " + integrantesIntercicloVO[i].idCliente + " le falta indicar que tiene la documentaci&oacute;n completa"));

                                                }
                                            }
                                        }
                                        if (autorizado) {
                                            cicloDAO.updateEstatusCicloIC(conn, grupo.getIdGrupo(), idCiclo, estatus, semDisp);
                                            bitacoraVO.setIdEquipo(idGrupo);
                                            bitacoraVO.setIdCiclo(idCiclo);
                                            bitacoraVO.setEstatus(estatus);
                                            bitacoraVO.setIdComentario(bitacoraCicloDAO.getNumComentario(idGrupo, idCiclo) + 1);
                                            bitacoraVO.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                                            bitacoraVO.setUsuarioComentario(request.getRemoteUser());
                                            bitacoraVO.setSemDisp(semDisp);
                                            if (estatus == ClientesConstants.CICLO_APERTURA) {
                                                myLogger.debug("Estatus Apertura Inter-Ciclo");
                                                bitacoraVO.setUsuarioAsignado(request.getRemoteUser());
                                            } else if (estatus == ClientesConstants.CICLO_PENDIENTE || estatus == ClientesConstants.CICLO_RECHAZADO) {
                                                myLogger.debug("Estatus " + ciclo.estatus);
                                                bitacoraVO.setUsuarioAsignado(bitacoraCicloDAO.getUsuarioEstatusIC(idGrupo, idCiclo, ClientesConstants.CICLO_ANALISIS, semDisp));
                                            } else if (estatus == ClientesConstants.CICLO_REVALORACION) {
                                                myLogger.debug("Estatus Revaloración");
                                                usuarioAutorizacion = bitacoraCicloDAO.getUsuarioEstatusIC(idGrupo, idCiclo, ClientesConstants.CICLO_AUTORIZADO, semDisp);
                                                if (!usuarioAutorizacion.equals("")) {
                                                    bitacoraVO.setUsuarioAsignado(usuarioAutorizacion);
                                                } else {
                                                    bitacoraVO.setUsuarioAsignado(bitacoraCicloDAO.getUsuarioEstatusIC(idGrupo, idCiclo, ClientesConstants.CICLO_RECHAZADO, semDisp));
                                                }
                                                if (bitacoraVO.getUsuarioAsignado().equals("")) {
                                                    bitacoraVO.setUsuarioAsignado(bitacoraCicloDAO.getUsuarioEstatusIC(idGrupo, idCiclo, ClientesConstants.CICLO_PENDIENTE, semDisp));
                                                }
                                            } else if (estatus == ClientesConstants.CICLO_AUTORIZADO) {
                                                myLogger.debug("Estatus Autorizado");
                                                bitacoraVO.setUsuarioAsignado("sistema");
                                            } else if (estatus == ClientesConstants.CICLO_ANALISIS) {
                                                myLogger.debug("Estatus Análisis");
                                                bitacoraVO.setUsuarioAsignado("arodriguez");
                                                bitacoraVO.setUsuarioAsignado(usuarioDAO.getUsuarioAnalisis());

                                            }
                                            myLogger.info("Insertando registro de bitácora ciclo");
                                            if (estatus != ClientesConstants.CICLO_APERTURA) {
                                                bitacoraCicloDAO.insertaBitacoraCiclo(conn, bitacoraVO);
                                            }
                                            conn.commit();
                                            integrantesIntercicloVO = new IntegranteCicloDAO().getIntegrantesNuevoInterCiclo(grupo.getIdGrupo(), idCiclo);
                                            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                                grupo.ciclos[idCiclo - 1].setEstatusIC(estatus);
                                            }
                                            if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                                grupo.ciclos[idCiclo - 1].setEstatusIC2(estatus);
                                            }
                                            if (integrantesIntercicloVO != null) {
                                                elementos = new IntegranteCicloVO[integrantesCicloVO.length + integrantesIntercicloVO.length];
                                                for (i = 0; i < integrantesCicloVO.length; i++) {
                                                    elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                                                }
                                                for (j = 0; j < integrantesIntercicloVO.length; j++) {
                                                    InformacionCrediticiaVO conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(integrantesIntercicloVO[j].idCliente);
                                                    CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantesIntercicloVO[j].idCliente, integrantesIntercicloVO[j].idSolicitud, 2);
                                                    if (informacionCrediticia != null) {
                                                        if (informacionCrediticia.calificacionMesaControl != 0) {
                                                            integrantesIntercicloVO[j].calificacion = informacionCrediticia.calificacionMesaControl;
                                                        } else if (conusltaCrediticia != null) {
                                                            integrantesIntercicloVO[j].calificacion = informacionCrediticia.comportamiento;
                                                            integrantesIntercicloVO[j].calificacionAutomatica = informacionCrediticia.comportamiento;
                                                        } else {
                                                            integrantesIntercicloVO[j].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                                                        }
                                                    } else {
                                                        integrantesIntercicloVO[j].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                                                    }
                                                    if (integrantesIntercicloVO[j].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA && integrantesIntercicloVO[j].idSolicitud > 1) {
                                                        integrantesIntercicloVO[j].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                                                    }
                                                    solicitudes = solicituddao.getSolicitudes(integrantesIntercicloVO[j].idCliente);
                                                    for (int y = 0; y < solicitudes.length; y++) {
                                                        if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                                            integrantesIntercicloVO[j].RenovacionDoc = 0;
                                                            if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new java.util.Date()) == 1) {
                                                                integrantesIntercicloVO[j].RenovacionDoc = 1;
                                                            }
                                                        }
                                                    }
                                                    if (integrantesIntercicloVO[j].RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                                                        integrantesIntercicloVO[j].RenovacionDoc = 1;
                                                    }
                                                    if (integrantesIntercicloVO[j].RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                                                        integrantesIntercicloVO[j].DocCompletos = 1;
                                                    }
                                                    elementos[i + j] = (IntegranteCicloVO) integrantesIntercicloVO[j];
                                                }
                                                grupo.ciclos[idCiclo - 1].integrantes = elementos;
                                                for (int a = 0; grupo.ciclos[idCiclo - 1].integrantes != null && a < grupo.ciclos[idCiclo - 1].integrantes.length; a++) {
                                                    grupo.ciclos[idCiclo - 1].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision(grupo.ciclos[idCiclo - 1].integrantes[a].monto, grupo.ciclos[idCiclo - 1].integrantes[a].comision, catComisionesGrupal);
                                                }
                                            } else {
                                                grupo.ciclos[idCiclo - 1].integrantes = integrantesCicloVO;
                                            }
                                        }
                                    }
                                } else {
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible guardar el equipo ya que alguno de los integrantes no cuenta con la Autorización de Crédito"));
                                }
                                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos se guardaron correctamente"));
                            }else{
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se permite agregar clientes autorizados por excepción en InterCiclo"));
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Paso del tiempo de 2 dias habiles para enviar el equipo a estatus de Analisis"));
                        }
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El grupo no se encuentra en las semanas autorizadas a Inter-Ciclo"));
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El Grupo no esta vigente"));
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            session.setAttribute("idCiclo", idCiclo);
            session.setAttribute("GRUPO", grupo);
            request.setAttribute("semanaDisp", semDisp);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                myLogger.error("Exception", ex);
            }
            myLogger.error("Exception", e);
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

    private void ClienteIntercicloDAO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int obtenerAutorizadosExcepcionInterciclo(CicloGrupalVO ciclo) throws ClientesException{
        int numeroInterciclosAutorizadosPorExepcion = 0;
        CreditoDAO creditoDAO = new CreditoDAO();
        
        for(IntegranteCicloVO i : ciclo.integrantes){
            CreditoVO cre = creditoDAO.getCredito(i.idCliente, i.idSolicitud, 2);
            if(cre != null && (cre.aceptaRegular == ClientesConstants.AUTORIZADO_POR_EXCEPCION || cre.aceptaRegular == ClientesConstants.AUTORIZADO_POR_EXCEPCION_REGULAR)){
                numeroInterciclosAutorizadosPorExepcion++;
            }
        }
        return numeroInterciclosAutorizadosPorExepcion;
    }

}

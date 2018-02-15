/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class CommandActualizaclientesNuevos implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaclientesNuevos.class);

    public CommandActualizaclientesNuevos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        GrupoVO grupo = new GrupoVO();
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesAsignados = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesNuevos = new ArrayList<IntegranteCicloVO>();
        InformacionCrediticiaVO conusltaCrediticia = null;
        CreditoVO informacionCrediticia = null;
        SolicitudVO solicitudes[] = null;
        ScoreCirculoCreditoDAO scoreDAO = new ScoreCirculoCreditoDAO();
        SolicitudDAO solicitudDao = new SolicitudDAO();
        DecisionComiteVO decisionComite = null;
        DecisionComiteDAO decisionComitedao = new DecisionComiteDAO();
        String mensaje = "";
        myLogger.debug("Variables inicializadas");
        try {
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            myLogger.debug("Obteniendo estatus del ciclo");
            int estatusCiclo = new CicloGrupalDAO().getEstatusCiclo(idGrupo, idCiclo);
            myLogger.debug("Obteniendo Integrantes");
            integrantesCiclo = new IntegranteCicloDAO().getIntegrantesApertura(idGrupo, idCiclo);
            for (IntegranteCicloVO integrantes : integrantesCiclo) {
                integrantes.monto = ClientesUtil.calculaMontoSinComision(integrantes.monto, integrantes.comision, catComisiones);
                integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                integrantes.comision = GrupoUtil.obtieneComisionIntegrante(integrantes, grupo);
                integrantes.monto = ClientesUtil.calculaMontoConComision(integrantes.monto, integrantes.comision, catComisiones);
                //CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantes.idCliente, integrantes.idSolicitud, 2);
                informacionCrediticia = new CreditoDAO().getCredito(integrantes.idCliente, integrantes.idSolicitud, 2);
                if (informacionCrediticia != null) {
                    integrantes.calificacion = informacionCrediticia.comportamiento;
                    integrantes.calificacionAutomatica = informacionCrediticia.comportamiento;
                    if (informacionCrediticia.calificacionMesaControl != 0) {
                        integrantes.calificacion = informacionCrediticia.calificacionMesaControl;

                    }
                    integrantes.aceptaRegular = informacionCrediticia.aceptaRegular;
//                    else
//                        integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                } else if (integrantes.getIdSolicitud() == 1) {
                    integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                } else {
                    integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                }
                myLogger.info("Verifica si tiene solicitudes vencidas");
                solicitudes = solicitudDao.getSolicitudes(integrantes.idCliente);
                for (int y = 0; y < solicitudes.length; y++) {
                    if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                        integrantes.RenovacionDoc = 0;
                        if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                            myLogger.info("encontro solicitud con atiguedad de un año");
                            integrantes.RenovacionDoc = 1;
                        }
                    }
                }
                if (integrantes.RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                    integrantes.RenovacionDoc = 1;
                }
                if (integrantes.RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                    myLogger.info("La solictud esta marcada con los documentos completos");
                    integrantes.DocCompletos = 1;
                }
                myLogger.debug("Obteniendo Score");
                integrantes.scoreFico = scoreDAO.getValorScore(integrantes.idCliente);
                /*if(informacionCrediticia != null && informacionCrediticia.calificacionMesaControl > ClientesConstants.CALIFICACION_CIRCULO_BUENA){
                 integrantes.calificacion = informacionCrediticia.calificacionMesaControl;
                 } else {
                 conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(integrantes.idCliente);
                 //integrantes.calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                 }*/
            }
            if (estatusCiclo == ClientesConstants.CICLO_APERTURA || estatusCiclo == ClientesConstants.CICLO_RECHAZADO) {
                myLogger.debug("Obteniendo nuevos integrantes");
                //integrantesCiclo = new IntegranteCicloDAO().getIntegrantesApertura(idGrupo, idCiclo);
                integrantesAsignados = new IntegranteCicloDAO().getNuevosIntegrantes(idGrupo);
                for (IntegranteCicloVO integrante : integrantesAsignados) {
                    if (!GrupoHelper.existeNuevoIntegrante(integrantesCiclo, integrante)) {
                        decisionComite = null;
                        decisionComite = decisionComitedao.getDecisionComite(integrante.idCliente, integrante.idSolicitud);
                        if (decisionComite != null) {
                            integrante.monto = ClientesUtil.calculaMontoSinComision(integrante.monto, integrante.comision, catComisiones);
                            integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                            integrante.comision = GrupoUtil.obtieneComisionIntegrante(integrante, grupo);
                            integrante.monto = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisiones);
                            informacionCrediticia = new CreditoDAO().getCredito(integrante.idCliente, integrante.idSolicitud, 2);
                            if (informacionCrediticia != null) {
                                integrante.calificacion = informacionCrediticia.comportamiento;
                                integrante.calificacionAutomatica = informacionCrediticia.comportamiento;
                                if (informacionCrediticia.calificacionMesaControl != 0) {
                                    integrante.calificacion = informacionCrediticia.calificacionMesaControl;

                                }
                                integrante.aceptaRegular = informacionCrediticia.aceptaRegular;
//                            else
//                                integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                            } else if (integrante.getIdSolicitud() == 1) {
                                integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                            } else {
                                integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                            }
                            integrante.scoreFico = scoreDAO.getValorScore(integrante.idCliente);
                            integrante.esNuevo = 1;
                            solicitudes = solicitudDao.getSolicitudes(integrante.idCliente);
                            for (int y = 0; y < solicitudes.length; y++) {
                                if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                    integrante.RenovacionDoc = 0;
                                    if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                        myLogger.info("encontro solicitud con atiguedad de un año");
                                        integrante.RenovacionDoc = 1;
                                    }
                                }
                            }
                            if (integrante.RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                                integrante.RenovacionDoc = 1;
                            }
                            if (integrante.RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                                myLogger.info("La solictud esta marcada con los documentos completos");
                                integrante.DocCompletos = 1;
                            }
                            integrantesNuevos.add(integrante);
                        } else {
                            mensaje += "No se puede agregar al integrante " + integrante.idCliente + " " + integrante.nombre + " por que no tiene Autorizacion de credito <br>";
                        }
                    }
                }
                if (!mensaje.equals("")) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, mensaje));
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("INTEGRANTES_NUEVOS", integrantesNuevos);
            request.setAttribute("INTEGRANTES_CICLO", integrantesCiclo);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}

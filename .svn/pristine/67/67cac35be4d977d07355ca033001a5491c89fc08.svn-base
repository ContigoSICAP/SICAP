package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

public class CommandActualizaCicloApertura implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaCicloApertura.class);

    public CommandActualizaCicloApertura(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        DecisionComiteVO decisionComite = null;
        SolicitudVO solicitudes[] = null;
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        BitacoraCicloVO bitacora = new BitacoraCicloVO();
        ArrayList<IntegranteCicloVO> integrantesAsignados = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesNuevos = new ArrayList<IntegranteCicloVO>();
        DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        ScoreCirculoCreditoDAO scoreDAO = new ScoreCirculoCreditoDAO();
        IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
        DecisionComiteDAO decisionComitedao = new DecisionComiteDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        UsuarioDAO usuarioDao = new UsuarioDAO();
        SolicitudDAO solicitudDao = new SolicitudDAO();
        String validacionDesembolso = "NO";
        String usuarioAutorizacion = "";
        boolean autorizado = true;
        myLogger.debug("Variables inicializadas");
        try {
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            myLogger.debug("Obteniendo Grupo");
            grupo = new GrupoDAO().getGrupo(idGrupo);
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandActualizaCicloApertura");
            myLogger.debug("Obteniendo Ciclo");
            ciclo = GrupoHelper.getCicloGrupalApertura(ciclo, request);
            if (!grupo.calificacion.equals("B")) {
                myLogger.info("El equipo no tiene calificación B");
                ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
                myLogger.debug("Actualizando dirección del ciclo");
                direcciondao.updateDireccionApertura(ciclo.direccionReunion);
                myLogger.debug("Obteniendo Integrantes actuales");
                ciclo.integrantesArray = GrupoHelper.getIntegrantesCicloApertura(request);
                myLogger.debug("Eliminando Integrantes actuales");
                integranteDao.deleteIntegranteApertura(idGrupo, idCiclo);
                integranteDao.deleteIntegranteAperturaODS(idGrupo, idCiclo);
                for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                    myLogger.debug("Insertando Integrantes nuevos " + integrante.idCliente + ": " + integrante.nombre);
                    integranteDao.addIntegranteApertura(idGrupo, idCiclo, integrante);
                    bitutil.registraEvento(integrante);
                    myLogger.debug("Obteniendo Score");
                    integrante.scoreFico = scoreDAO.getValorScore(integrante.idCliente);
                    if (ciclo.estatus == ClientesConstants.CICLO_ANALISIS || ciclo.estatus == ClientesConstants.CICLO_REVALORACION) {
                        decisionComite = decisionComitedao.getDecisionComite(integrante.idCliente, integrante.idSolicitud);
                        if (decisionComite == null) {
                            autorizado = false;
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible guardar el equipo ya que el integrante " + integrante.idCliente + "  no cuenta con la Autorización de Crédito"));
                        }
                        
                    }
                    myLogger.info("Verifica si tiene solicitudes vencidas");
                    solicitudes = solicituddao.getSolicitudes(integrante.idCliente);
                    for (int y = 0; y < solicitudes.length; y++) {
                        if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                            integrante.RenovacionDoc = 0;
                            if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                myLogger.info("encontro solicitud con atiguedad de un año");
                                integrante.RenovacionDoc = 1;
                            }
                        }
                    }
                    if (integrante.RenovacionDoc==0 && (solicitudes[solicitudes.length - 1].documentacionCompleta>0|| solicitudes.length == 1)){
                        integrante.RenovacionDoc = 1;
                    }
                    if (integrante.RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                        myLogger.info("La solictud esta marcada con los documentos completos");
                        integrante.DocCompletos = 1;
                    } else if (integrante.RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta == 0 && (ciclo.estatus == ClientesConstants.CICLO_ANALISIS || ciclo.estatus == ClientesConstants.CICLO_REVALORACION)) {
                        autorizado = false;
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible enviar el equipo por que al cliente: "+integrante.idCliente+" le falta indicar que tiene la documentaci&oacute;n completa"));

                    }
                }
                
                if (ciclo.estatus == ClientesConstants.CICLO_ANALISIS || ciclo.estatus == ClientesConstants.CICLO_REVALORACION) {                    
                    int pilotoGrupo = GrupoHelper.obtenerPilotoGrupo(grupo, ciclo);                    
                    //Si tiene unicamente piloto 2 (clientes autorizados por excepción) realiza las validaciones correspondientes
                    if(pilotoGrupo == 2){                        
                        //Verificamos si la sucursal permite realizar autorizacion por excepcion
                        if(CatalogoHelper.sucursalPermiteAutorizacionPorExcepcion(grupo.sucursal)){
                            //Validamos el porcentaje de autorizados por excepcion en el equipo
                            String validacionPorcentajeAutExcep = GrupoUtil.validaPorcentajeAutExcepcionGrupo(idGrupo, idCiclo, 0 , 0);
                            if(validacionPorcentajeAutExcep != null && !validacionPorcentajeAutExcep.isEmpty()){
                               notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, validacionPorcentajeAutExcep)); 
                               autorizado = false;
                            }

                            //Validamos los pases de autorizacion por excepción
                            String paseDisponibleAutExcep = GrupoUtil.validaGruposAutorizadosExcepcion(grupo.idGrupo, idCiclo,0);
                            if(paseDisponibleAutExcep != null && !paseDisponibleAutExcep.isEmpty()){
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, paseDisponibleAutExcep));
                                autorizado = false;
                            }
                        }else{
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La sucursal no permite agregar clientes Autorizados por Excepción")); 
                            autorizado = false;
                        }
                        
                        //Si tiene ambos pilotos, no deberia permitir mandar a analisis
                    }else if (pilotoGrupo == 4){
                        if (integranteDao.getNumPasesOtraFinanciera()>=ClientesConstants.MAX_PASES_PILOTO_3){
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Se supero el maximo de clientes de otra Fianciera este para este mes")); 
                            autorizado = false;
                        }
                    }else if(pilotoGrupo == 3){
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se permite tener en un equipo clientes del piloto 1 y el piloto 2")); 
                        autorizado = false;
                    }
                }
                
                if (autorizado) {
                    if (CatalogoHelper.esSucursalAceptaRegular(grupo.sucursal)) {
                        autorizado = GrupoHelper.validaAceptaRegulares(grupo, ciclo);
                        if (!autorizado) {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El equipo no cumple con la cantidad de clientes Regulares"));
                        }
                    }
                }
                if (autorizado) {
                    if (ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO) {
                        myLogger.info("Estatus del ciclo: autorizado");
                        myLogger.debug("Actualizando número de equipo en solicitudes");
                        for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                            solicitudDao.updateGrupoSolicitud(idGrupo, integrante.idCliente, integrante.idSolicitud);
                        }
                    }
                    ciclo.monto = 0.0;
                    ciclo.montoConComision = 0.0;
                    for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                        ciclo.monto += integrante.monto;
                        integrante.monto = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
                        ciclo.montoConComision += integrante.monto;
                    }
                    /*ASIGNA EL FONDEADOR*/
                    if (ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO) {
                        ciclo.fondeador = FondeadorUtil.asignaFondeador(ciclo, grupo.sucursal);
                    }
                    myLogger.info("_______________________________" + request.getRemoteUser() + " CommandActualizaCicloApertura" + " ciclo.idGrupo " + ciclo.idGrupo + " ciclo.idCiclo " + ciclo.idCiclo);
                    GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, new Date()); //SE DEBE MODIFICAR PARA LA COMISION
                    myLogger.debug("Actualizando ciclo");
                    ciclodao.updateCicloApertura(ciclo);
                    bitutil.registraEvento(ciclo);
                    bitacora.setIdEquipo(idGrupo);
                    bitacora.setIdCiclo(idCiclo);
                    bitacora.setEstatus(ciclo.estatus);
                    bitacora.setIdComentario(bitacoraCicloDao.getNumComentario(idGrupo, idCiclo) + 1);
                    bitacora.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                    bitacora.setUsuarioComentario(request.getRemoteUser());
                    if (ciclo.estatus == ClientesConstants.CICLO_APERTURA) {
                        myLogger.debug("Estatus Apertura");
                        bitacora.setUsuarioAsignado(request.getRemoteUser());
                    } else if (ciclo.estatus == ClientesConstants.CICLO_PENDIENTE || ciclo.estatus == ClientesConstants.CICLO_RECHAZADO) {
                        myLogger.debug("Estatus " + ciclo.estatus);
                        bitacora.setUsuarioAsignado(bitacoraCicloDao.getUsuarioEstatus(idGrupo, idCiclo, ClientesConstants.CICLO_ANALISIS));
                    } else if (ciclo.estatus == ClientesConstants.CICLO_REVALORACION) {
                        myLogger.debug("Estatus Revaloración");
                        usuarioAutorizacion = bitacoraCicloDao.getUsuarioEstatus(idGrupo, idCiclo, ClientesConstants.CICLO_AUTORIZADO);
                        if (!usuarioAutorizacion.equals("")) {
                            bitacora.setUsuarioAsignado(usuarioAutorizacion);
                        } else {
                            bitacora.setUsuarioAsignado(bitacoraCicloDao.getUsuarioEstatus(idGrupo, idCiclo, ClientesConstants.CICLO_RECHAZADO));
                        }
                        if (bitacora.getUsuarioAsignado().equals("")) {
                            bitacora.setUsuarioAsignado(bitacoraCicloDao.getUsuarioEstatus(idGrupo, idCiclo, ClientesConstants.CICLO_PENDIENTE));
                        }
                    } else if (ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO) {
                        myLogger.debug("Estatus Autorizado");
                        bitacora.setUsuarioAsignado("sistema");
                    } else if (ciclo.estatus == ClientesConstants.CICLO_ANALISIS) {
                        myLogger.debug("Estatus Análisis");
                        //bitacora.setUsuarioAsignado("arodriguez");
                        bitacora.setUsuarioAsignado(usuarioDao.getUsuarioAnalisis());

                    }
                    myLogger.info("Insertando registro de bitácora ciclo");
                    bitacoraCicloDao.insertaBitacoraCiclo(null, bitacora);
                    validacionDesembolso = "OK";
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
                }
            } else {
                myLogger.debug("Ciclo con calificación B");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion"));
            }
            if (ciclo.estatus == ClientesConstants.CICLO_APERTURA || ciclo.estatus == ClientesConstants.CICLO_RECHAZADO) {
                myLogger.info("Estatus " + ciclo.estatus);
                CreditoVO informacionCrediticia = null;
                myLogger.debug("Obtieniendo nuevos integrantes");
                integrantesAsignados = new IntegranteCicloDAO().getNuevosIntegrantes(idGrupo);
                for (IntegranteCicloVO integrante : integrantesAsignados) {
                    if (!GrupoHelper.existeNuevoIntegrante(ciclo.integrantesArray, integrante)) {
                        myLogger.debug("Integrante Nuevo: " + integrante.idCliente + ": " + integrante.nombre);
                        integrante.monto = ClientesUtil.calculaMontoSinComision(integrante.monto, integrante.comision, catComisionesGrupal);
                        integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                        integrante.comision = GrupoUtil.obtieneComisionIntegrante(integrante, grupo);
                        integrante.monto = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
                        informacionCrediticia = new CreditoDAO().getCredito(integrante.idCliente, integrante.idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            integrante.calificacion = informacionCrediticia.comportamiento;
                            integrante.calificacionAutomatica = informacionCrediticia.comportamiento;
                            if (informacionCrediticia.calificacionMesaControl != 0) {
                                integrante.calificacion = informacionCrediticia.calificacionMesaControl;
                            }
//                                        else
//                                            integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                        } else if (integrante.getIdSolicitud() == 1) {
                            integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                        } else {
                            integrante.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                        }
                        myLogger.debug("Obteniendo Score");
                        integrante.scoreFico = scoreDAO.getValorScore(integrante.idCliente);
                        integrantesNuevos.add(integrante);
                        bitutil.registraEvento(integrante);
                    }
                }
                request.setAttribute("INTEGRANTES_NUEVOS", integrantesNuevos);
            }
            //bitutil.registraEvento(ciclo);
            /*for (int i = 0; i < ciclo.integrantesArray.size(); i++) {
                bitutil.registraEvento(ciclo.integrantesArray.get(i));
            }*/
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("VALIDACION", validacionDesembolso);
            request.setAttribute("INTEGRANTES_CICLO", ciclo.integrantesArray);

        } catch (ClientesDBException dbe) {
            myLogger.error("execute", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

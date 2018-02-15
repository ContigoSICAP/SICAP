package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandRechazaInterCiclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaInterCiclo.class);

    public CommandRechazaInterCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        ClientesConstants ClientesConstants = new ClientesConstants();
        SolicitudDAO solicituddao = new SolicitudDAO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO[] integrantesIntercicloVO = null;
        IntegranteCicloVO elementos[] = null;
        SolicitudVO solicitudes[] = null;
        int i = 0;

        try {
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandRechazaInterCiclo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            if (grupo.ciclos[idCiclo - 1].estatusIC == ClientesConstants.CICLO_APERTURA) {
                for (i = 0; i < grupo.ciclos[idCiclo - 1].integrantes.length; i++) {
                    SolicitudVO solicitud = new SolicitudVO();
                    solicitud = solicitudDAO.getSolicitud(grupo.ciclos[idCiclo - 1].integrantes[i].idCliente, grupo.ciclos[idCiclo - 1].integrantes[i].idSolicitud);
                    solicitud.setSubproducto(0);
                    solicitudDAO.updateSolicitud(grupo.ciclos[idCiclo - 1].integrantes[i].idCliente, solicitud);
                }
                ciclodao.updateEstatusCicloIC(null, grupo.idGrupo, idCiclo, ClientesConstants.CICLO_RECHAZADO, 0);
                bitutil.registraEvento(grupo.ciclos[idCiclo - 1]);
                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se rechazo Inter-Ciclo"));
                integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
                integrantesIntercicloVO = new IntegranteCicloDAO().getIntegrantesNuevoInterCiclo(grupo.getIdGrupo(), idCiclo);
                grupo.ciclos[idCiclo - 1].setEstatusIC(ClientesConstants.CICLO_RECHAZADO);
                if (integrantesIntercicloVO != null) {
                    elementos = new IntegranteCicloVO[integrantesCicloVO.length + integrantesIntercicloVO.length];
                    for (i = 0; i < integrantesCicloVO.length; i++) {
                        elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                    }
                    for (int j = 0; j < integrantesIntercicloVO.length; j++) {
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
                                if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                    integrantesIntercicloVO[j].RenovacionDoc = 1;
                                }
                            }
                        }
                        if (integrantesIntercicloVO[j].RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0|| solicitudes.length == 1)) {
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
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Alguien ya guardo este equipo"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            session.setAttribute("idCiclo", idCiclo);
            session.setAttribute("GRUPO", grupo);

        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}

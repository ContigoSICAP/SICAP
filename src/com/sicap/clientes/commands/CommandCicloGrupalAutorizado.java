package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandCicloGrupalAutorizado implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCicloGrupalAutorizado.class);

    public CommandCicloGrupalAutorizado(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        SegurosDAO seguroDAO = new SegurosDAO();
        EmpleoDAO empleoDAO = new EmpleoDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        String usuarioModificacion = request.getRemoteUser();
        BitacoraUtil bitacora = null;
        Connection con = null;

        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            List<Integer> lstDelIntegrantes = new ArrayList<Integer>();
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int fondeador = HTMLHelper.getParameterInt(request, "fondeador");
            int clieSeguro = 0;
            int numIntegrantesChecked = 0;
            boolean blnFicha = false;
            boolean infoIncompleta = false;
            boolean activo = false;
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
            ciclo.fondeador = fondeador;
            bitacora = new BitacoraUtil(grupo.idGrupo, usuarioModificacion, "CommandCicloGrupalAutorizado");
            if (ciclo.archivosAsociados != null) {
                for (ArchivoAsociadoVO archivo : ciclo.archivosAsociados) {
                    if (archivo.tipo == 16) {
                        blnFicha = true;
                    }
                }
            }
            /*for (int i = 0; i < ciclo.integrantes.length; i++) {
                lstDelIntegrantes.add(ciclo.integrantes[i].idCliente);
            }*/
            //GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
            /*for (int i = 0; i < ciclo.integrantes.length; i++) {
                for (int j = 0; j < lstDelIntegrantes.size(); j++) {
                    if(ciclo.integrantes[i].idCliente == lstDelIntegrantes.get(j)){
                        lstDelIntegrantes.remove(j);
                        integranteDAO.updateRolIntegrante(con, ciclo.integrantes[i]);
                        break;
                    }
                }
            }
            if(!lstDelIntegrantes.isEmpty()){
                for (Integer delIntegrante : lstDelIntegrantes) {
                    integranteDAO.deleteIntegrante(con, idGrupo, idCiclo, delIntegrante);
                }
            }*/
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                clieSeguro = seguroDAO.compruebaSeguro(ciclo.integrantes[i]);
                if (clieSeguro < 1 || empleoDAO.compruebaEmpleo(ciclo.integrantes[i]) == 0) {
                    infoIncompleta = true;
                }
                if (!integranteDAO.getIntegranteActivo(ciclo.integrantes[i].idCliente).equals("")) {
                    activo = true;
                }
            }
            if (!activo) {
                if (!infoIncompleta) {
                    //if (GrupoUtil.validaDiaSolicitudDesembolso(ciclo)) {//insertar validacion de dia
                    if (GrupoUtil.validafechaDesembolso(ciclo)) {
                        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
                        ciclo.estatus = ClientesConstants.CICLO_PARADESEMBOLSAR;
                        ciclo.fechaDispersion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaDispersion"));
                        ciclo.diaReunion = HTMLHelper.getParameterInt(request, "diaReunion");
                        ciclo.horaReunion = HTMLHelper.getParameterInt(request, "horaReunion");
                        ciclo.bancoDispersion = HTMLHelper.getParameterInt(request, "bancoDispersion");
                        bitacoraCiclo.setIdEquipo(idGrupo);
                        bitacoraCiclo.setIdCiclo(ciclo.idCiclo);
                        bitacoraCiclo.setEstatus(ciclo.estatus);
                        bitacoraCiclo.setIdComentario(bitacoraCicloDAO.getNumComentario(idGrupo, ciclo.idCiclo) + 1);
                        bitacoraCiclo.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                        bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                        bitacoraCiclo.setUsuarioAsignado("sistema");
                        myLogger.info("Insertando registro de bitácora ciclo");
                        bitacoraCicloDAO.insertaBitacoraCiclo(null, bitacoraCiclo);
                        bitacora.registraEvento(ciclo);
                        new CicloGrupalDAO().updateCiclo(ciclo);
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Peticion de Desembolso Enviada."));
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La fecha de desembolso no puede ser anterior al dia de hoy"));
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante no contiene información completa dentro de la solicitud."));
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante tiene una solicitud sin liquidar."));
            }
            con.close();
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("ClientesDBException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}

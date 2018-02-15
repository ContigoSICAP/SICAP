package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandIntercicloAutrizado implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCicloGrupalAutorizado.class);
    
    public CommandIntercicloAutrizado(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        TablaAmortVO tablaVO = null;
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        SegurosDAO seguroDAO = new SegurosDAO();
        EmpleoDAO empleoDAO = new EmpleoDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        Connection con = null;
        Date fechaActual = Calendar.getInstance().getTime();
        
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int semDisp = HTMLHelper.getParameterInt(request, "semDisp");
            java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
            int clieSeguro = 0;
            boolean infoIncompleta = false;
            boolean activo = false;
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                if(ciclo.integrantes[i].esInterciclo == 1&&ciclo.integrantes[i].semDisp == semDisp ){
                    clieSeguro = seguroDAO.compruebaSeguro(ciclo.integrantes[i]);
                    if (clieSeguro < 1 || empleoDAO.compruebaEmpleo(ciclo.integrantes[i]) == 0) {// quitar validacion empleo
                        infoIncompleta = true;
                    }
                    if (!integranteDAO.getIntegranteActivo(ciclo.integrantes[i].idCliente).equals("")) {
                        activo = true;
                    }
                }
            }
            if (!activo) {
                if (!infoIncompleta) {
                    tablaVO = new TablaAmortDAO().getDivPago(idGrupo, ciclo.idCreditoIBS, semDisp);
                    if(InterCicloHelper.validaCambioEstatus(semDisp, ClientesConstants.CICLO_PARADESEMBOLSAR, idGrupo, ciclo.idCreditoIBS, fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))){
                        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
                        if(semDisp == ClientesConstants.DISPERSION_SEMANA_2)
                            ciclo.estatusIC = ClientesConstants.CICLO_PARADESEMBOLSAR;
                        else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4)
                            ciclo.estatusIC2 = ClientesConstants.CICLO_PARADESEMBOLSAR;
                        ciclo.fechaDispersion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaDispersion"));
                        ciclo.diaReunion = HTMLHelper.getParameterInt(request, "diaReunion");
                        ciclo.horaReunion = HTMLHelper.getParameterInt(request, "horaReunion");
                        ciclo.bancoDispersion = HTMLHelper.getParameterInt(request, "bancoDispersion");
                        bitacoraCiclo.setIdEquipo(idGrupo);
                        bitacoraCiclo.setIdCiclo(ciclo.idCiclo);
                        bitacoraCiclo.setEstatus(ClientesConstants.CICLO_PARADESEMBOLSAR);
                        bitacoraCiclo.setIdComentario(bitacoraCicloDAO.getNumComentario(idGrupo, ciclo.idCiclo)+1);
                        bitacoraCiclo.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                        bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                        bitacoraCiclo.setUsuarioAsignado("sistema");
                        bitacoraCiclo.setSemDisp(semDisp);
                        myLogger.info("Insertando registro de bitácora ciclo");
                        bitacoraCicloDAO.insertaBitacoraCiclo(con, bitacoraCiclo);
                        new CicloGrupalDAO().updateEstatusCicloIC(con, idGrupo, idCiclo, ClientesConstants.CICLO_PARADESEMBOLSAR, semDisp);
                        con.commit();
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Peticion de Desembolso Enviada."));
                        GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Paso del tiempo de 1 dia habil para realizar la Peticion de Desembolso"));
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante no contiene información completa dentro de la solicitud."));
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante tiene una solicitud sin liquidar."));
            }
            con.close();
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("GRUPO", grupo);
        } catch (ClientesDBException dbe) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                myLogger.error("ClientesDBException", ex);
            }
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException sqle) {
                myLogger.error("ClientesDBException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}

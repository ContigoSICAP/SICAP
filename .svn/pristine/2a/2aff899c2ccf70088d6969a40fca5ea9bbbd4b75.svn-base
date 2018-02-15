package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.UsuarioVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandCambiaEstatusCiclo implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCambiaEstatusCiclo.class);

    public CommandCambiaEstatusCiclo(String siguiente) {
        this.siguiente = siguiente;
    }    
    
    public String execute(HttpServletRequest request) throws CommandException{
        HttpSession session = request.getSession();
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<BitacoraCicloVO> registrosCiclo = new ArrayList<BitacoraCicloVO>();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        BitacoraCicloVO registroBitacora = new BitacoraCicloVO();
        String comentarioCiclo = "";
        String analistaAsignar = "";
        int nuevoEstatus=0;
        int dispersion = 0;
        boolean regBitacora = true;
        GrupoVO grupo = new GrupoVO();
        GrupoDAO grupoDao = new GrupoDAO();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        SaldoIBSVO saldoVO = null;
        TablaAmortVO tablaVO = null;
        myLogger.debug("Variables inicializadas");
        try {
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
            int idEquipoAsignar = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCicloAsignar = HTMLHelper.getParameterInt(request, "idCiclo");
            int estatusAsignar = HTMLHelper.getParameterInt(request, "estatusAsignar");
            int accion = HTMLHelper.getParameterInt(request, "accion");
            int numEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
            Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
            Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
            java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
            int estatusCiclo = HTMLHelper.getParameterInt(request, "estatus");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int semDisp = HTMLHelper.getParameterInt(request, "semDisp");
            int idCredito = HTMLHelper.getParameterInt(request, "idCredito");
            int subProducto = HTMLHelper.getParameterInt(request, "subproducto");
            String analista = HTMLHelper.getParameterString(request, "analista");
            String usuarioModificacion = request.getRemoteUser();
            BitacoraUtil bitacora = new BitacoraUtil(idEquipoAsignar, usuarioModificacion, "CommandCambiaEstatusCiclo");
            myLogger.debug("Obteniendo número de comentario");
            int numComentario = bitacoraCicloDao.getNumComentario(idEquipoAsignar, idCicloAsignar)+1;
            tablaVO = new TablaAmortDAO().getDivPago(idEquipoAsignar, idCredito, semDisp);
            if(accion == 1){
                myLogger.debug("Accion Asignar");
                if(estatusAsignar==ClientesConstants.CICLO_ANALISIS){
                    myLogger.debug("Estatus Análisis");
                    analistaAsignar = HTMLHelper.getParameterString(request, "analistaAsignar");
                    nuevoEstatus=ClientesConstants.CICLO_ASIGNADO;
                    comentarioCiclo = "Se asigna equipo";
                } else if(estatusAsignar==ClientesConstants.CICLO_ASIGNADO){
                    myLogger.debug("Estatus Asignado");
                    analistaAsignar = HTMLHelper.getParameterString(request, "analistaAsignar");
                    nuevoEstatus=estatusAsignar;
                    comentarioCiclo = "Se reasigna equipo";
                } else if(estatusAsignar==ClientesConstants.CICLO_REVALORACION){
                    myLogger.debug("Estatus Revaloración");
                    analistaAsignar = HTMLHelper.getParameterString(request, "analistaAsignar");                
                    nuevoEstatus = estatusAsignar;
                    comentarioCiclo = "Se reasigna equipo";
                }
                if(subProducto == 0){
                    myLogger.info("Actualizando estatus del ciclo");
                    cicloDao.updateEstatusCiclo(idEquipoAsignar, idCicloAsignar, nuevoEstatus);
                } else if(subProducto == 1){
                    myLogger.info("Validando situacion del interciclo");
                    if(InterCicloHelper.validaCambioEstatus(semDisp, estatusCiclo, idEquipoAsignar, idCredito, fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))){
                        saldoVO = new SaldoIBSDAO().getSaldos(idEquipoAsignar, idCicloAsignar);
                        if(saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE){
                            comentarioCiclo += " Inter-Ciclo";
                            myLogger.info("Actualizando estatus del ciclo");
                            cicloDao.updateEstatusCicloIC(null, idEquipoAsignar, idCicloAsignar, nuevoEstatus, semDisp);
                        } else{
                            regBitacora = false;
                            notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "El equipo "+idEquipoAsignar+" no esta vigente"));
                        }
                    } else {
                        regBitacora = false;
                        notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "El equipo "+idEquipoAsignar+" no se encuentra en condiciones para Inter-Ciclo"));
                    }
                }
                siguiente="/consultaCicloEstatus.jsp";
            } else if(accion == 2){
                myLogger.debug("Acción Analizar");
                if(estatusAsignar==ClientesConstants.CICLO_ASIGNADO){
                    myLogger.debug("Estatus Asignado");
                    nuevoEstatus=ClientesConstants.CICLO_PROCESO;
                    analistaAsignar = request.getRemoteUser();
                    comentarioCiclo = "Comienza análisis";
                } else if(estatusAsignar==ClientesConstants.CICLO_REVALORACION){
                    myLogger.debug("Estatus Revaloración");
                    nuevoEstatus=ClientesConstants.CICLO_PROCESO;
                    analistaAsignar = request.getRemoteUser();
                    comentarioCiclo = "Comienza revaloración";
                }
                if(subProducto == 0){
                    myLogger.debug("Actualizando estatus del ciclo");
                    cicloDao.updateEstatusCiclo(idEquipoAsignar, idCicloAsignar, nuevoEstatus);
                } else if(subProducto == 1){
                    myLogger.info("Validando situacion del interciclo");
                    if(InterCicloHelper.validaCambioEstatus(semDisp, estatusCiclo, idEquipoAsignar, idCredito, fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))){
                        saldoVO = new SaldoIBSDAO().getSaldos(idEquipoAsignar, idCicloAsignar);
                        if(saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE){
                            comentarioCiclo += " Inter-Ciclo";
                            myLogger.info("Actualizando estatus del ciclo");
                            cicloDao.updateEstatusCicloIC(null, idEquipoAsignar, idCicloAsignar, nuevoEstatus, semDisp);
                        } else{
                            regBitacora = false;
                            notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "El equipo "+idEquipoAsignar+" no esta vigente"));
                        }
                    } else {
                        regBitacora = false;
                        notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "El equipo "+idEquipoAsignar+" no se encuentra en condiciones para Inter-Ciclo"));
                    }
                }
                if (idEquipoAsignar != 0) {
                    myLogger.debug("Obteniendo Grupo");
                    grupo = grupoDao.getGrupo(idEquipoAsignar);
                    myLogger.debug("Obteniendo Ciclo");
                    grupo.ciclos = cicloDao.getCiclos(grupo.idGrupo);
                    for (int i = 0; grupo.ciclos != null && i < grupo.ciclos.length; i++) {
                        grupo.ciclos[i] = new CicloGrupalDAO().getCiclo(idEquipoAsignar, grupo.ciclos[i].idCiclo);
                        if (grupo.ciclos[i].idCreditoIBS != 0) {
                            grupo.ciclos[i].saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, grupo.ciclos[i].idCiclo);
                            grupo.ciclos[i].setSemDisp(InterCicloHelper.semanaDispersion(grupo.ciclos[i].saldo, grupo.ciclos[i]));
                        } else {
                            grupo.ciclos[i].saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(idEquipoAsignar, grupo.ciclos[i].idCiclo, ClientesConstants.GRUPAL));
                        }
                        if (grupo.ciclos[i].saldo != null) {
                            grupo.ciclos[i].estatusT24 = grupo.ciclos[i].saldo.getEstatus();
                        }
                    }
                }                
                session.setAttribute("GRUPO", grupo);
                siguiente="/capturaGrupo.jsp";
            } else if(accion == ClientesConstants.CICLO_APERTURA){
                myLogger.debug("Acción revalorización");
                if(estatusAsignar==ClientesConstants.CICLO_PENDIENTE||estatusAsignar==ClientesConstants.CICLO_RECHAZADO){
                    nuevoEstatus=ClientesConstants.CICLO_REVALORACION;
                    myLogger.debug("Obteniendo usuario estatus 4 o 6");
                    analistaAsignar= bitacoraCicloDao.getUsuarioEstatus(idEquipoAsignar, idCicloAsignar, estatusAsignar);
                    comentarioCiclo="Se envía equipo para revaloración";
                }
                myLogger.info("Actualizando estatus");
                cicloDao.updateEstatusCiclo(idEquipoAsignar, idCicloAsignar, nuevoEstatus);
                siguiente="/consultaCicloEstatus.jsp";
            }
            if(regBitacora){
                registroBitacora.setComentario(comentarioCiclo);
                registroBitacora.setEstatus(nuevoEstatus);
                registroBitacora.setIdCiclo(idCicloAsignar);
                registroBitacora.setIdComentario(numComentario);
                registroBitacora.setIdEquipo(idEquipoAsignar);
                registroBitacora.setUsuarioAsignado(analistaAsignar);
                registroBitacora.setUsuarioComentario(usuarioModificacion);
                registroBitacora.setSemDisp(semDisp);
                myLogger.info("Insertando registro de bitácora ciclo");
                bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
            }
            //********************************* BUSQUEDA DE REGISTROS *******************************************//
            if((request.isUserInRole("SUCURSAL"))&&(!request.isUserInRole("manager"))){
                sucursal=usuario.idSucursal;
            }
            if(request.isUserInRole("ANALISIS_CREDITO")&&!request.isUserInRole("ASIGNACION_EQUIPOS")){
                analista=usuario.nombre;
            }
            if(request.isUserInRole("CONFIRMACION_DESEMBOLSO_GRUPAL")){
                dispersion=1;
            }
            myLogger.debug("Obteniendo última modificación");
            registrosCiclo = bitacoraCicloDao.getUltimaModificacion(numEquipo, fechaInicio, fechaFin, estatusCiclo, sucursal, analista, subProducto);
            bitacora.registraEvento(registroBitacora);
            request.setAttribute("REGISTROS", registrosCiclo);
            request.setAttribute("FECHAINICIO", fechaInicio);
            request.setAttribute("FECHAFIN", fechaFin);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandCambiaEstatusCiclo", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            myLogger.error("CommandCambiaEstatusCiclo", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
}

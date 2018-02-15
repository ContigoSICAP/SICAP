package com.sicap.clientes.commands;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.MetasEjecutivosDAO;
import com.sicap.clientes.dao.PlaneacionRenovacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.MetasEjecutivosVO;
import com.sicap.clientes.vo.PlaneacionRenovacionVO;
import java.util.ArrayList;
import java.util.Vector;

public class CommandGuardaPlaneacion implements Command {

    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandGuardaPlaneacion.class);

    public CommandGuardaPlaneacion(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        Calendar cal = Calendar.getInstance();
        Date fechaTemp = cal.getTime();
        Date dateNextMonth;
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            fechaTemp = FechasUtil.getDate(cal.getTime(), 0, 0);
        } else {
            fechaTemp = FechasUtil.getDate(cal.getTime(), 1, 0);
        }
        MetasEjecutivosVO metas = new MetasEjecutivosVO();
        PlaneacionRenovacionDAO planeacionDao = new PlaneacionRenovacionDAO();
        MetasEjecutivosDAO metasDao = new MetasEjecutivosDAO();
        GrupoDAO grupoDao = new GrupoDAO();
        myLogger.debug("Variables inicializadas");
        try {
            PlaneacionRenovacionVO[] grupos = GrupoHelper.getGruposPlaneacion(request);
            int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
            int meta = HTMLHelper.getParameterInt(request, "meta");
            int integrantes = HTMLHelper.getParameterInt(request, "integrantes");
            int integrantesTotal = HTMLHelper.getParameterInt(request, "integrantesTotal");
            int clientesNuevos = HTMLHelper.getParameterInt(request, "clientesNuevos");
            String accion = HTMLHelper.getParameterString(request, "accion");
            myLogger.debug("Variables de request obtenidas");
            BitacoraUtil bitutil = new BitacoraUtil(idEjecutivo, request.getRemoteUser(), "CommandGuardaPlaneacion");
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                dateNextMonth = FechasUtil.getDate(cal.getTime(), 0, 0);
            } else {
                dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
            }
            String fechaInicial = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 0), ClientesConstants.FORMATO_FECHA_EU);
            String fechaFinal = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 1), ClientesConstants.FORMATO_FECHA_EU);
            ArrayList<GrupoVO> gruposRenovacion = grupoDao.getGruposRenovacion(idEjecutivo, fechaInicial, fechaFinal);
            myLogger.debug("Obteniendo grupos de renovación por idEjecutivo, fechaInicial, fechaFinal");
            EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(idEjecutivo);
            myLogger.debug("Obteniendo asesor por idEjecutivo");
            String fechaMeta = FechasUtil.obtenParteFecha(fechaTemp, 2) + FechasUtil.obtenParteFecha(fechaTemp, 3);
            metas.idEjecutivo = idEjecutivo;
            metas.mesAño = Integer.valueOf(fechaMeta);
            metas.meta = meta;
            metas.integrantes = integrantes;
            metas.integrantesTotal = integrantesTotal;
            metas.clientesNuevos = clientesNuevos;
            myLogger.debug("Se asignan valores al objeto metas");
            if (accion.equals("actualizar")) {
                myLogger.debug("Ya existen metas");
                planeacionDao.eliminaPlaneacion(idEjecutivo);
                myLogger.info("Se elimina planeación existente");
                metasDao.eliminaMeta(idEjecutivo);
                myLogger.info("Se elimina meta existente");
            }
            metasDao.addMeta(metas);
            myLogger.info("Se inserta meta");
            if (grupos != null && idEjecutivo != 0 && meta != 0) {
                for (int i = 0; i < grupos.length; i++) {
                    planeacionDao.addPlaneacion(grupos[i]);
                    myLogger.info("Se inserta planeación");
                }
                if (accion.equals("actualizar")) {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
                } else {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Los datos no fueron actualizados correctamente"));
            }
            bitutil.registraEvento(metas);
            myLogger.debug("Se registra bitácora");
            request.setAttribute("METAEJECUTIVO", metas);
            request.setAttribute("GRUPOS", gruposRenovacion);
            request.setAttribute("EJECUTIVO", ejecutivo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
            myLogger.debug("Se establecen atributos en request");
        } catch (ClientesDBException dbe) {
            myLogger.error("CommandGuardaPlaneacion", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("CommandGuardaPlaneacion", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}

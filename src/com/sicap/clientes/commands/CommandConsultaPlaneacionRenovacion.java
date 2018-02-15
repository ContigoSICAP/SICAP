package com.sicap.clientes.commands;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.MetasEjecutivosDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.MetasEjecutivosVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandConsultaPlaneacionRenovacion implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaPlaneacionRenovacion.class);

    public CommandConsultaPlaneacionRenovacion(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        GrupoDAO grupoDao = new GrupoDAO();
        Calendar cal = Calendar.getInstance();
        Date fechaTemp = cal.getTime();
        Date dateNextMonth;
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            fechaTemp = FechasUtil.getDate(cal.getTime(), 0, 0);
        } else {
            fechaTemp = FechasUtil.getDate(cal.getTime(), 1, 0);
        }
        MetasEjecutivosVO meta = new MetasEjecutivosVO();
        MetasEjecutivosDAO metasDao = new MetasEjecutivosDAO();
        myLogger.debug("Variables inicializadas");
        try {
            int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
            //Date dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
            if ( cal.get(Calendar.DAY_OF_MONTH)<10 )
                dateNextMonth = FechasUtil.getDate(cal.getTime(), 0, 0);
            else
                dateNextMonth = FechasUtil.getDate(cal.getTime(), 1, 0);
            String fechaInicial = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 0), ClientesConstants.FORMATO_FECHA_EU);
            String fechaFinal = Convertidor.dateToString(FechasUtil.getFirstLastDay(dateNextMonth, 1), ClientesConstants.FORMATO_FECHA_EU);
            ArrayList <GrupoVO> gruposRenovacion = grupoDao.getGruposRenovacion(idEjecutivo, fechaInicial, fechaFinal);
            myLogger.debug("Obteniendo grupos de renovación por idEjecutivo, fechaInicial, fechaFinal");            
            String fechaMeta = FechasUtil.obtenParteFecha(fechaTemp, 2) + FechasUtil.obtenParteFecha(fechaTemp, 3);
            meta = metasDao.getMeta(idEjecutivo, Integer.valueOf(fechaMeta));
            myLogger.debug("Obteniendo meta por idEjecutivo, fechaMeta");
            if ( gruposRenovacion != null && gruposRenovacion.size() > 0 ){                
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(idEjecutivo);
                myLogger.debug("Obteniendo asesor por idEjecutivo");
                request.setAttribute("GRUPOS", gruposRenovacion);
                request.setAttribute("EJECUTIVO", ejecutivo);
                myLogger.debug("Se establecen atributos en request");
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron equipos de renovación para el asesor seleccionado"));
            }
            if (meta !=null){
                request.setAttribute("METAEJECUTIVO", meta);
                myLogger.debug("El asesor cuenta con metas y se establece en request");
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandConsultaPlaneacionRenovacion", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandConsultaPlaneacionRenovacion", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

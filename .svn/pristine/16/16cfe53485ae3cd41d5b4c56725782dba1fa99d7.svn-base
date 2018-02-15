package com.sicap.clientes.commands;

import com.sicap.clientes.dao.RenovacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.RenovacionVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CommandGuardaEquiposNoRenovados implements Command{
    
    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandGuardaEquiposNoRenovados.class);

    public CommandGuardaEquiposNoRenovados(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        int year = 0;
        Calendar cal = Calendar.getInstance();
        ArrayList<GrupoVO> equiposSinRenovar = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposNoRenovados = new ArrayList<GrupoVO>();
        RenovacionDAO renovacionDao = new RenovacionDAO();
        Vector<Notification> notificaciones = new Vector<Notification>();
        boolean actualizado = false;
        try {
            int numEquipos = HTMLHelper.getParameterInt(request, "equipos");
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            int idMonth = HTMLHelper.getParameterInt(request, "idMonth");
            int idYear = HTMLHelper.getParameterInt(request, "idYear");
            myLogger.debug("Variables obtenidas de request");
            for (int i = 0; i < numEquipos; i++) {                
                int activado = HTMLHelper.getParameterInt(request, "motivo"+i);
                int idEquipo = HTMLHelper.getParameterInt(request, "idGrupo"+i);
                int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo"+i);
                int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo"+i);
                int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes"+i);
                Date fechaVencimiento = HTMLHelper.getParameterDate(request, "vencimiento"+i);
                BitacoraUtil bitacora = new BitacoraUtil(idEquipo, request.getRemoteUser(), "CommandGuardaEquiposNoRenovados");
                if(activado>0){
                    actualizado=true;
                    RenovacionVO renovacion = new RenovacionVO(idEquipo, idCiclo, idEjecutivo, numIntegrantes, fechaVencimiento, activado);
                    renovacionDao.insertaPlaneacion(renovacion);
                    bitacora.registraEvento(renovacion);
                }
            }
            if(idYear==2){
                year = cal.get(Calendar.YEAR);
            } else if(idYear==1){
                year = cal.get(Calendar.YEAR)-1;
            }
            equiposSinRenovar = renovacionDao.getEquiposNoRenovados(idSucursal, year, idMonth);
            if(!equiposSinRenovar.isEmpty()){                
                for(GrupoVO equipo : equiposSinRenovar){
                    if(!new RenovacionDAO().esNoRenovado(equipo.idGrupo, equipo.idCicloOriginal)){
                        equiposNoRenovados.add(equipo);
                    }
                }
            }
            if(actualizado){                
                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se actualizaron los registros correctamente"));
            }
            if(equiposNoRenovados.isEmpty()){
                myLogger.debug("No hay registros");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("EQUIPOSNORENOVADOS", equiposNoRenovados);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandGuardaNoRenovados", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandGuardaNoRenovados", e);
            throw new CommandException(e.getMessage());
        }
	return siguiente;
    }
    
}

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.RenovacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CommandConsultaEquiposNoRenovados implements Command{
    
    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandConsultaEquiposNoRenovados.class);

    public CommandConsultaEquiposNoRenovados(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        int year = 0;
        Calendar cal = Calendar.getInstance();
        ArrayList<GrupoVO> equiposSinRenovar = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposNoRenovados = new ArrayList<GrupoVO>();
        RenovacionDAO renovacionDao = new RenovacionDAO();
        Vector<Notification> notificaciones = new Vector<Notification>();
        try{
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            int idMonth = HTMLHelper.getParameterInt(request, "idMonth");
            int idYear = HTMLHelper.getParameterInt(request, "idYear");
            myLogger.debug("Variables obtenidas de request");            
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
            if(equiposNoRenovados.isEmpty()){
                myLogger.debug("No hay registros");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("EQUIPOSNORENOVADOS", equiposNoRenovados);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandConsultaEquiposNoRenovados", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandConsultaEquiposNoRenovados", e);
            throw new CommandException(e.getMessage());
        }
	return siguiente;
    }
    
}

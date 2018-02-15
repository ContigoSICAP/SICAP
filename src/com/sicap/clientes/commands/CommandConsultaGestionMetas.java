package com.sicap.clientes.commands;

import com.sicap.clientes.dao.MetasEjecutivosDAO;
import com.sicap.clientes.dao.PlaneacionRenovacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.MetasEjecutivosVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandConsultaGestionMetas implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaGestionMetas.class);

    public CommandConsultaGestionMetas(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        MetasEjecutivosVO meta = new MetasEjecutivosVO();
        MetasEjecutivosDAO metasDao = new MetasEjecutivosDAO();
        PlaneacionRenovacionDAO planeacionDao = new PlaneacionRenovacionDAO();
        Calendar cal = Calendar.getInstance();
        ArrayList<GrupoVO> equiposPlaneados = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposTotales = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposPlaneadosRenovados = new ArrayList<GrupoVO>();
        int year = 0;
        int sumaIntegrantes = 0;
        int sumaNuevos = 0;
        int porcentaje = 0;
        myLogger.debug("Variables inicializadas");
        try {
            int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
            int idMonth = HTMLHelper.getParameterInt(request, "idMonth");
            int idYear = HTMLHelper.getParameterInt(request, "idYear");
            myLogger.debug("Variables obtenidas de request");
            String monthYear = "";
            if(idYear==2){
                year = cal.get(Calendar.YEAR);
            } else if(idYear==1){
                year = cal.get(Calendar.YEAR)-1;
            }
            monthYear = String.valueOf(idMonth)+String.valueOf(year);
            meta = metasDao.getMeta(idEjecutivo, Integer.valueOf(monthYear));
            myLogger.debug("Obteniendo meta del asesor");
            if(meta!=null){
                myLogger.debug("El asesor tiene meta");
                equiposPlaneados = planeacionDao.getEquiposPlaneados(idEjecutivo, year, idMonth);
                myLogger.debug("Obteniendo equipos planeados");
                equiposTotales = planeacionDao.getEquiposTotales(idEjecutivo, year, idMonth);
                myLogger.debug("Obteniendo equipos totales");
                for (int i = 0; i < equiposPlaneados.size(); i++) {
                    for (int j = 0; j < equiposTotales.size(); j++) {
                        if(equiposTotales.get(j).idGrupo==equiposPlaneados.get(i).idGrupo){
                            equiposPlaneadosRenovados.add(equiposTotales.get(j));
                            myLogger.info("Se agrega equipo renovado "+equiposTotales.get(j).toString());
                            sumaIntegrantes+=equiposTotales.get(j).ciclos[0].numIntegrantes;
                        }
                    }
                }                
                for (int i = 0; i < equiposPlaneadosRenovados.size(); i++) {
                    for (int j = 0; j < equiposPlaneados.size(); j++) {
                        if(equiposPlaneados.get(j).idGrupo==equiposPlaneadosRenovados.get(i).idGrupo){
                            myLogger.info("Se elimina equipo planeado "+equiposPlaneados.get(j).toString());
                            equiposPlaneados.remove(equiposPlaneados.get(j));
                        }
                    }
                    for (int j = 0; j < equiposTotales.size(); j++) {
                        if(equiposTotales.get(j).idGrupo==equiposPlaneadosRenovados.get(i).idGrupo){
                            myLogger.info("Se elimina equipo total "+equiposTotales.get(j).toString());
                            equiposTotales.remove(equiposTotales.get(j));
                        }
                    }
                }
                for(GrupoVO equipoNuevo : equiposTotales){
                    sumaNuevos+=equipoNuevo.ciclos[0].numIntegrantes;                    
                }
                myLogger.info("Se obtiene la suma de equipos nuevos: "+sumaNuevos);
                porcentaje = ((sumaIntegrantes+sumaNuevos)*100)/(meta.meta);
                myLogger.info("Se obtiene el porcentaje de la meta: "+porcentaje);
            } else {
                myLogger.info("El asesor no tiene meta");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron equipos para el asesor seleccionado"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("EQUIPOSPLANEADOS", equiposPlaneados);
            request.setAttribute("EQUIPOSRENOVADOS", equiposPlaneadosRenovados);
            request.setAttribute("EQUIPOSNUEVOS", equiposTotales);
            request.setAttribute("META", meta);
            request.setAttribute("SUMANUEVOS", sumaNuevos);
            request.setAttribute("SUMARENOVADOS", sumaIntegrantes);
            request.setAttribute("PORCENTAJE", porcentaje);
            request.setAttribute("idEjecutivo", idEjecutivo);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandConsultaGestionMetas", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandConsultaGestionMetas", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
}

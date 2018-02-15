package com.sicap.clientes.commands;

import com.sicap.clientes.dao.MetasEjecutivosDAO;
import com.sicap.clientes.dao.PlaneacionRenovacionDAO;
import com.sicap.clientes.dao.RenovacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.MetasEjecutivosVO;
import com.sicap.clientes.vo.PlaneacionRenovacionVO;
import com.sicap.clientes.vo.RenovacionVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CommandGuardaNoRenovado implements Command{
    
    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandGuardaNoRenovado.class);

    public CommandGuardaNoRenovado(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        RenovacionVO renovacion = new RenovacionVO();
        RenovacionDAO renovacionDao = new RenovacionDAO();
        PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
        PlaneacionRenovacionDAO planeacionDao = new PlaneacionRenovacionDAO();
        ArrayList<GrupoVO> equiposPlaneados = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposTotales = new ArrayList<GrupoVO>();
        ArrayList<GrupoVO> equiposPlaneadosRenovados = new ArrayList<GrupoVO>();
        MetasEjecutivosVO meta = new MetasEjecutivosVO();
        MetasEjecutivosDAO metasDao = new MetasEjecutivosDAO();
        int year = 0;
        Calendar cal = Calendar.getInstance();
        myLogger.debug("Variables inicializadas");
        try {
            int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
            int porcentaje = HTMLHelper.getParameterInt(request, "porciento");
            int sumaNuevos = HTMLHelper.getParameterInt(request, "nuevosAlcanzados");
            int sumaRenovados= HTMLHelper.getParameterInt(request, "RenovadosAlcanzados");
            int idEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
            int idCiclo= HTMLHelper.getParameterInt(request, "idCiclo");
            int idMotivo= HTMLHelper.getParameterInt(request, "idMotivo");
            int idMonth = HTMLHelper.getParameterInt(request, "idMonth");
            int idYear = HTMLHelper.getParameterInt(request, "idYear");
            myLogger.debug("Variables obtenidas de request");
            BitacoraUtil bitacora = new BitacoraUtil(idEquipo, request.getRemoteUser(), "CommandGuardaNoRenovado");
            planeacion = planeacionDao.getPlaneacionEquipo(idEquipo, idCiclo);
            myLogger.debug("Obteniendo objeto planeación");
            renovacion.setIdEquipo(idEquipo);
            renovacion.setIdCiclo(idCiclo);
            renovacion.setIdMotivo(idMotivo);
            renovacion.setIdAsesor(planeacion.numEjecutivo);
            renovacion.setNumIntegrantes(planeacion.integrantes);
            renovacion.setFechaVencimiento(planeacion.fechaVencimiento);
            myLogger.debug("Asignando valores a renovación");
            renovacionDao.insertaPlaneacion(renovacion);
            myLogger.info("Se inserta regsitro de renovación");
            bitacora.registraEvento(renovacion);
            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "La información fue guardada correctamente"));
            String monthYear = "";
            if(idYear==2){
                year = cal.get(Calendar.YEAR);
            } else if(idYear==1){
                year = cal.get(Calendar.YEAR)-1;
            }
            monthYear = String.valueOf(idMonth)+String.valueOf(year);
            meta = metasDao.getMeta(idEjecutivo, Integer.valueOf(monthYear));
            myLogger.debug("Se obtienen meta del asesor");
            equiposPlaneados = planeacionDao.getEquiposPlaneados(idEjecutivo, year, idMonth);
            myLogger.debug("Se obtienen registros de equipos planeados");
            equiposTotales = planeacionDao.getEquiposTotales(idEjecutivo, year, idMonth);
            myLogger.debug("Se obtienen registros de equipos totales");
                for (int i = 0; i < equiposPlaneados.size(); i++) {
                    for (int j = 0; j < equiposTotales.size(); j++) {
                        if(equiposTotales.get(j).idGrupo==equiposPlaneados.get(i).idGrupo){
                            equiposPlaneadosRenovados.add(equiposTotales.get(j));
                            myLogger.info("Se agrega equipo renovado "+equiposTotales.get(j).toString());
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
            request.setAttribute("EQUIPOSPLANEADOS", equiposPlaneados);
            request.setAttribute("EQUIPOSRENOVADOS", equiposPlaneadosRenovados);
            request.setAttribute("EQUIPOSNUEVOS", equiposTotales);
            request.setAttribute("META", meta);
            request.setAttribute("SUMANUEVOS", sumaNuevos);
            request.setAttribute("SUMARENOVADOS", sumaRenovados);
            request.setAttribute("PORCENTAJE", porcentaje);            
        } catch(ClientesDBException dbe){
                        myLogger.error("CommandGuardaNoRenovado", dbe);
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			myLogger.error("CommandGuardaNoRenovado", e);
			throw new CommandException(e.getMessage());
		}
	return siguiente;
    }
    
}

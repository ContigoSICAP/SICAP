package com.sicap.clientes.commands;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import java.util.Calendar;
import org.apache.log4j.Logger;

public class CommandBuscaEjecutivos implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandBuscaEjecutivos.class);

    public CommandBuscaEjecutivos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        try {

            Notification notificaciones[] = new Notification[1];
            int idSucursal = 0;
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            TreeMap catEjecutivosOrigen = new TreeMap();
            TreeMap catEjecutivosDestino = new TreeMap();
            Calendar fechaHoy = Calendar.getInstance();
            Calendar fechaPermiso = Calendar.getInstance();
            boolean diaHabil = false;
            int diaMesReasignacion = Integer.parseInt(CatalogoHelper.getParametro("DIA_MES_REASIGNACION"));

            if (HTMLHelper.getParameterString(request, "command").equals("reasignacionCartera")) {
                if (!request.isUserInRole("ADM_EJECUTIVOS_ALTAS")) {
                    fechaPermiso.set(Calendar.DATE, diaMesReasignacion);
                }
                //System.out.println("fechaPermiso "+fechaPermiso.get(Calendar.DAY_OF_MONTH)+"/"+fechaPermiso.get(Calendar.MONTH)+"/"+fechaPermiso.get(Calendar.YEAR)+" "+fechaPermiso.get(Calendar.DAY_OF_WEEK));
                if (fechaPermiso.get(Calendar.DAY_OF_WEEK) == 1) {
                    fechaPermiso.add(Calendar.DAY_OF_YEAR, 1);
                } else if (fechaPermiso.get(Calendar.DAY_OF_WEEK) == 7) {
                    fechaPermiso.add(Calendar.DAY_OF_YEAR, 2);
                }
                //System.out.println("fechaPermiso "+fechaPermiso.get(Calendar.DAY_OF_MONTH)+"/"+fechaPermiso.get(Calendar.MONTH)+"/"+fechaPermiso.get(Calendar.YEAR)+" "+fechaPermiso.get(Calendar.DAY_OF_WEEK));
                if (fechaHoy.equals(fechaPermiso)) {
                    notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "Dia habil para Reasignacion de Cartera");
                    diaHabil = true;
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Dia inhabil para Reasignacion de Cartera");
                }
                request.setAttribute("diaHabil", diaHabil);
                request.setAttribute("NOTIFICACIONES", notificaciones);
            } else {
                if (idSucursal > 0) {
                    catEjecutivosOrigen = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");
                    //catEjecutivosDestino = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");  //lista aquellos que están status=ALTA
                    catEjecutivosDestino = catEjecutivosOrigen;
                    if (catEjecutivosOrigen != null && catEjecutivosOrigen.size() > 0) {
                        request.setAttribute("EJECUTIVOS_ORIGEN", catEjecutivosOrigen);
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron ejecutivos origen para esa sucursal");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                    if (catEjecutivosDestino != null && catEjecutivosDestino.size() > 0) {
                        request.setAttribute("EJECUTIVOS_DESTINO", catEjecutivosDestino);
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron ejecutivos destino para esa sucursal");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
                request.setAttribute("diaHabil", true);
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("execute", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

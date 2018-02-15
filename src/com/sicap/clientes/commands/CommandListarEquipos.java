package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import java.util.ArrayList;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class CommandListarEquipos implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandListarEquipos.class);

    public CommandListarEquipos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        int idSucursal = 0;
        int idEjecutivoOrigen = 0;
        try {
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            idEjecutivoOrigen = HTMLHelper.getParameterInt(request, "idEjecutivoOrigen");

            TreeMap catEjecutivosOrigen = new TreeMap();
            TreeMap catEjecutivosDestino = new TreeMap();

            if (idSucursal > 0) {
                //vuelve a hacer la consulta para no perder los datos de la sesion
                catEjecutivosOrigen = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");
                //catEjecutivosDestino = CatalogoHelper.getCatalogoEjecutivos(idSucursal, "A");  //lista aquellos que están status=ALTA
                catEjecutivosDestino = catEjecutivosOrigen;
                request.setAttribute("EJECUTIVOS_ORIGEN", catEjecutivosOrigen);
                request.setAttribute("EJECUTIVOS_DESTINO", catEjecutivosDestino);
                if (idEjecutivoOrigen > 0) {
                    //obtiene las solicitudes manejadas por ese ejecutivo excepto las canceladas
                    ArrayList<CicloGrupalVO> arrEquipos = new CicloGrupalDAO().getEquiposEjecutivos(idEjecutivoOrigen);
                    if(arrEquipos != null)
                        request.setAttribute("CARTERA_EQUIPOS", arrEquipos);
                    else{
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontro cartera activa para el ejecutivo");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado el ejecutivo de origen");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            }else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }  //fin else
            request.setAttribute("diaHabil", true);
        } catch (ClientesDBException dbe) {
            myLogger.error("Proeblema dentro CommandListarEquipos", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Proeblema dentro CommandListarEquipos", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

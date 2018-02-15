package com.sicap.clientes.commands;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;

public class CommandListaEjecutivos implements Command {

    private String siguiente;

    public CommandListaEjecutivos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //TreeMap catEjecutivos = new TreeMap();
        ArrayList<EjecutivoCreditoVO> listaEjecutivos = null;
        EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
        Notification notificaciones[] = new Notification[1];
        int idSucursal = 0;
        String filtEstatus = "N"; //Variable para filtar por estatus 
        try {
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            filtEstatus = HTMLHelper.getParameterString(request, "selFilestatus");
            if (idSucursal > 0) {
                
                if(filtEstatus.equals("N")||filtEstatus==null){
                    listaEjecutivos = ejecutivodao.getEjecutivosSucursal(idSucursal);
                }
                else{
                    listaEjecutivos = ejecutivodao.getEjecutivosSucursal(idSucursal,filtEstatus);
                }
                if (listaEjecutivos != null && listaEjecutivos.size() > 0) {
                    request.setAttribute("EJECUTIVOS", listaEjecutivos);
                    Logger.debug("Ejecutivos encontrados");

                } //fin if
                else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron ejecutivos para la sucursal seleccionada");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }	//fin else
            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }  //fin else
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

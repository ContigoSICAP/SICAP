package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.util.ArrayList;
import java.util.Vector;

public class CommandOrdenesDePago implements Command {

    private String siguiente;

    public CommandOrdenesDePago(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();

        try {
            int banco = HTMLHelper.getParameterInt(request, "banco");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            Date fechaInicio = Convertidor.toSqlDate(new java.util.Date());
            Date fechaFin = Convertidor.toSqlDate(new java.util.Date());
            //Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
            //Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
            ArrayList<CicloGrupalVO> ciclosDis = new ArrayList<CicloGrupalVO>();
            ArrayList<OrdenDePagoVO> arrOrdenesTemp = new ArrayList<OrdenDePagoVO>();
            ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
            if(!request.getParameter("fechaInicial").equals(""))
                fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
            if(!request.getParameter("fechaFinal").equals(""))
                fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
            //BUSQUEDA DE SOLO PRODUCTO COMUNAL
            //ciclosDis = new CicloGrupalDAO().getCiclosFechaDispercion(fechaInicio, fechaFin, sucursal);
            //for (CicloGrupalVO ciclos : ciclosDis) {
                arrOrdenesTemp = new OrdenDePagoDAO().getArrOrdenesDePago(banco, estatus, fechaInicio, fechaFin, sucursal);
                for (OrdenDePagoVO ordenes : arrOrdenesTemp) {
                    arrOrdenes.add(new OrdenDePagoVO(ordenes.getIdOrdenPago(), ordenes.getIdCliente(), ordenes.getIdSolicitud(), ordenes.getIdOperacion(), ordenes.getIdSucursal(), ordenes.getNombre(), ordenes.getMonto(),
                            ordenes.getReferencia(), ordenes.getNombreArchivo(), ordenes.getUsuario(), ordenes.getFechaCaptura(), ordenes.getFechaEnvio(), 
                            ordenes.getIdBanco(), ordenes.getEstatus(), ordenes.getNomSucursal(), ordenes.getProducto(), ordenes.getDescEstatus(), ordenes.getGrupo() != null ? ordenes.getGrupo() : "--", ordenes.getNumEnvio()));
                }
            //}
            //arrOrdenes = new OrdenDePagoDAO().getArrOrdenesDePago(banco, sucursal, estatus, fechaInicio, fechaFin);
            if (arrOrdenes.isEmpty())
                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se encontraron Registros"));
            else
                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, ""));

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("ORDENESPAGO", arrOrdenes);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }

        return siguiente;
    }
}

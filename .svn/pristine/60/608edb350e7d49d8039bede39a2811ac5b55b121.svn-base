package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CommandCambiaSucursalCliente implements Command{
    private String siguiente;
    
    public CommandCambiaSucursalCliente(String siguiente){
        this.siguiente=siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        try {
            TreeMap catalogoEstatusSaldo = CatalogoHelper.getCatalogo("c_estatus_saldos");
            TreeMap catalogoSucursales = CatalogoHelper.getCatalogo("c_sucursales");
            int numCliente = HTMLHelper.getParameterInt(request, "numCliente");
            int nuevaSucursal = HTMLHelper.getParameterInt(request, "nuevaSucursal");
            BitacoraUtil bitacora = new BitacoraUtil(numCliente, request.getRemoteUser(), "CommandCambiaSucursalCliente");
            SolicitudVO solicitud = new SolicitudVO();
            ClienteVO cliente = new ClienteVO();
            ClienteDAO clienteDAO = new ClienteDAO();
            SolicitudDAO solicitudDAO = new SolicitudDAO();
            solicitud=solicitudDAO.getSolicitudSaldo(numCliente);
            if(solicitud!=null){
                String estatusSaldo = "";
                if(solicitud.getSaldo().getEstatus()!=0){
                    estatusSaldo = HTMLHelper.getDescripcion(catalogoEstatusSaldo, solicitud.getSaldo().getEstatus());
                }
                else {
                    estatusSaldo = "No dispersado";
                }
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible realizar el cambio de sucursal, tiene un saldo con estatus "+
                        estatusSaldo +" en el equipo "+solicitud.getSaldo().getIdClienteSICAP()+" "
                        +solicitud.getSaldo().getNombreCliente()+" ciclo "+solicitud.getSaldo().getIdSolicitudSICAP()));
            }
            else {
                if(clienteDAO.updateSucursal(numCliente, nuevaSucursal)){
                    cliente = clienteDAO.getCliente(numCliente);
                    bitacora.registraEvento(cliente);
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se cambia correctamente de sucursal a "+HTMLHelper.getDescripcion(catalogoSucursales, nuevaSucursal)));
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        }
        catch(ClientesDBException dbe){
            throw new CommandException(dbe.getMessage());
        }
        catch(Exception e){
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
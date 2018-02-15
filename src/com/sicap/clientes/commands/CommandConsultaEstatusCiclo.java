package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandConsultaEstatusCiclo implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaEstatusCiclo.class);
    
    public CommandConsultaEstatusCiclo(String siguiente) {
        this.siguiente=siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException{
        HttpSession session = request.getSession();
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<BitacoraCicloVO> registrosCiclo = new ArrayList<BitacoraCicloVO>();
        BitacoraCicloDAO bitacoraDao = new BitacoraCicloDAO();
        myLogger.debug("Variables inicializadas");
        try{
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
            Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
            Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
            int estatusCiclo = HTMLHelper.getParameterInt(request, "estatus");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int numEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
            int subProducto = HTMLHelper.getParameterInt(request, "subproducto");
            String analista = HTMLHelper.getParameterString(request, "analista");
            if((request.isUserInRole("SUCURSAL"))&&(!request.isUserInRole("manager"))){
                sucursal=usuario.idSucursal;
                myLogger.debug("SUCURSAL USUARIO: "+sucursal);
            }
            if(request.isUserInRole("ANALISIS_CREDITO")&&!request.isUserInRole("ASIGNACION_EQUIPOS")){
                analista=usuario.nombre;
                myLogger.debug("ANALISTA USUARIO: "+ analista);
            }
            if (request.isUserInRole("ANALISIS_CREDITO")&&estatusCiclo ==11){
                analista="";
                myLogger.debug("ANALISTA USUARIO PARA ESTATUS LISTO PARA DESEMBOLSAR: "+ analista);
            }
            myLogger.debug("Obteniendo registros");
            registrosCiclo = bitacoraDao.getUltimaModificacion(numEquipo, fechaInicio, fechaFin, estatusCiclo, sucursal, analista, subProducto);
            if(registrosCiclo.isEmpty()){
                myLogger.debug("No se encontraron ");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("REGISTROS", registrosCiclo);
            request.setAttribute("FECHAINICIO", fechaInicio);
            request.setAttribute("FECHAFIN", fechaFin);
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("subproducto", subProducto);
        }catch(ClientesDBException dbe){
            myLogger.error("CommandConsultaEstatusCiclo", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandConsultaEstatusCiclo", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

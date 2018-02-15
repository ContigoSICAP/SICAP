package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
/**
 *
 * @author LDAVILA
 */
public class CommandConsultaInterCiclo implements Command {
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaEstatusCiclo.class);
    
    public CommandConsultaInterCiclo(String siguiente){
        this.siguiente=siguiente;    
    }
    public String execute(HttpServletRequest request) throws CommandException{
        
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<CicloGrupalVO> registrosInterCiclo = new ArrayList<CicloGrupalVO>();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        try{
            Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
            Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
            int estatusCiclo = HTMLHelper.getParameterInt(request, "estatus");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int numEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
            
            registrosInterCiclo = cicloDao.getCicloInterCiclo(numEquipo, fechaInicio, fechaFin, estatusCiclo, sucursal);
            
            if(registrosInterCiclo.isEmpty()){
                 notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("REGISTROS", registrosInterCiclo );
            request.setAttribute("FECHAINICIO", fechaInicio);
            request.setAttribute("FECHAFIN", fechaFin);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        }catch(ClientesDBException dbe){
            myLogger.error("CommandConsultaEstatusCiclo", dbe);
            throw new CommandException(dbe.getMessage());
        }catch(Exception e){
            myLogger.error("CommandConsultaEstatusCiclo", e);
            throw new CommandException(e.getMessage());
        }      
        return siguiente;    
    }
    
}

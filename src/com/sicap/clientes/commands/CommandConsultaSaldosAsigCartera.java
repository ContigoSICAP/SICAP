package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandConsultaSaldosAsigCartera implements Command {
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaSaldosAsigCartera.class);

    public CommandConsultaSaldosAsigCartera(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        myLogger.info("Desde Consulta Asingnacion Cartera");
        
        try {
            
            Vector<Notification> notificaciones = new Vector<Notification>();
            CicloGrupalVO ciclo = new CicloGrupalVO();
            SaldoIBSVO saldo = new SaldoIBSVO();
            //CicloGrupalDAO cicloDao = new CicloGrupalDAO();
            SaldoIBSDAO saldoDao = new SaldoIBSDAO();
            
            int idEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            
            System.out.println("EQUIPO: "+idEquipo);
            System.out.println("CICLO: "+idCiclo);
            
            //Obtener Saldo si ib_fondeo_garantia distinto de 0->Credito Real
            //Se permitira credito real para seleccionar saldo
            saldo = saldoDao.getSaldoFondeoGarantia(idEquipo, idCiclo, 0,ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
            
              
            if(saldo==null){
                myLogger.info("SALDO nulo: "+ saldo);
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "No se encontró saldo en garantía con ese grupo y ciclo"));
            }

            
            System.out.println("LISTO con 0");
            //request.setAttribute("CICLO", ciclo);
            request.setAttribute("SALDO", saldo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
            
        } catch(ClientesDBException dbe){
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
    
}

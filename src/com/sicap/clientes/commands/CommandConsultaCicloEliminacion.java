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

public class CommandConsultaCicloEliminacion implements Command {
    private String siguiente;

    public CommandConsultaCicloEliminacion(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            Vector<Notification> notificaciones = new Vector<Notification>();
            CicloGrupalVO ciclo = new CicloGrupalVO();
            SaldoIBSVO saldo = new SaldoIBSVO();
            CicloGrupalDAO cicloDao = new CicloGrupalDAO();
            SaldoIBSDAO saldoDao = new SaldoIBSDAO();
            int idEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            System.out.println("EQUIPO: "+idEquipo);
            System.out.println("CICLO: "+idCiclo);
            ciclo = cicloDao.getCicloEliminacion(idEquipo, idCiclo);
            if(ciclo!=null){
                if(ciclo.getDesembolsado()==ClientesConstants.CICLO_DESEMBOLSO_CONFIRMADO){
                    saldo = saldoDao.getSaldo(idEquipo, ciclo.getIdCreditoIBS());
                    System.out.println("SALDO: "+saldo.getReferencia());
                } else {
                    saldo=null;
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Ciclo no encontrado"));
            }
            System.out.println("LISTO");
            request.setAttribute("CICLO", ciclo);
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

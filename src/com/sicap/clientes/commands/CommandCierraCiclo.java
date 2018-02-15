package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandCierraCiclo implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCierraCiclo.class);

    public CommandCierraCiclo(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException{
        HttpSession session = request.getSession();
        Notification[] notificaciones = new Notification[1];
        SaldoIBSVO saldo = new SaldoIBSVO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        BitacoraCicloVO registroBitacora = new BitacoraCicloVO();
        SaldoIBSDAO saldoDao = new SaldoIBSDAO();
        TablaAmortDAO tablaAmortizacionDao = new TablaAmortDAO();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        PagoDAO pagoDao = new PagoDAO();
        double montoPagar = 0;
        double montoPagado = 0;
        myLogger.debug("Variables inicializadas");
        try {
            GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
            int idEquipo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            myLogger.debug("Obteniendo saldo");
            saldo=saldoDao.getSaldos(idEquipo, idCiclo);
            BitacoraUtil bitacora = new BitacoraUtil(idEquipo, request.getRemoteUser(), "CommandCierraCiclo");
            if(tablaAmortizacionDao.getTotalPorpagar(saldo.getIdClienteSICAP(), saldo.getCredito())>0){
                myLogger.debug("Monto por pagar mayor a cero");
                notificaciones[0]=( new Notification(ClientesConstants.ERROR_TYPE, "No es posible cerrar el ciclo, aún tiene monto a pagar"));
            } else {
                myLogger.debug("Obteniendo Total por Pagar");
                montoPagar = tablaAmortizacionDao.getTotalPagado(saldo.getIdClienteSICAP(), saldo.getCredito());
                myLogger.debug("Obteniendo Total Pagado");
                montoPagado = pagoDao.getTotalPagado(saldo.getReferencia());
                if(montoPagado>=montoPagar){
                    myLogger.debug("Monto pagado es mayor o igual a Monto pagar, Pagado: " +montoPagado+ " Pagar: " +montoPagar);
                    notificaciones[0]=( new Notification(ClientesConstants.INFO_TYPE, "Se cierra el ciclo correctamente") );
                    myLogger.info("Actualizando estatus del ciclo");
                    cicloDao.updateEstatusCiclo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP(), ClientesConstants.CICLO_CERRADO);
                    myLogger.debug("Actualizando estatus del ciclo en varibale del grupo en session");
                    grupo.ciclos[idCiclo-1].estatus=ClientesConstants.CICLO_CERRADO;
                    registroBitacora.setComentario("Ciclo Cerrado");
                    registroBitacora.setEstatus(ClientesConstants.CICLO_CERRADO);
                    registroBitacora.setIdCiclo(saldo.getIdSolicitudSICAP());
                    registroBitacora.setIdComentario(bitacoraCicloDao.getNumComentario(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP())+1);
                    registroBitacora.setIdEquipo(saldo.getIdClienteSICAP());
                    registroBitacora.setUsuarioAsignado("sistema");
                    registroBitacora.setUsuarioComentario(request.getRemoteUser());
                    myLogger.info("Insertando registro en bitácora ciclo.");
                    bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
                    myLogger.info("Insertando registro en bitácora");
                    bitacora.registraEvento(registroBitacora);
                    session.setAttribute("GRUPO", grupo);
                } else {
                    myLogger.debug("El monto a pagar es mayor al monto pagado");
                    notificaciones[0]=( new Notification(ClientesConstants.ERROR_TYPE, "No es posible cerrar el ciclo, tiene una diferencia de "+FormatUtil.roundDecimal(montoPagar-montoPagado,2)));
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
                        
        } catch(ClientesDBException dbe){
            myLogger.error("CommandCierraCiclo", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("CommandCierraCiclo", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
}

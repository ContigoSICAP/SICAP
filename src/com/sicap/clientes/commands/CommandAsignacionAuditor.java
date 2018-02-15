package com.sicap.clientes.commands;

import com.sicap.clientes.dao.AuditoresDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.AuditorHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.AuditoresVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandAsignacionAuditor implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandAsignacionAuditor.class);

    public CommandAsignacionAuditor(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        AuditoresVO auditorVO = new AuditoresVO();
        try {
            int idAuditor = HTMLHelper.getParameterInt(request, "idAuditor");
            AuditoresDAO auditorDAO = new AuditoresDAO();
            if(request.getParameter("command").equals("buscaAuditorAsignar")){
                auditorVO.setNumAuditor(idAuditor);
                auditorVO.setArrSucursal(auditorDAO.findSucursalesAuditor(idAuditor, 1));
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, auditorVO.getArrSucursal().size()+" Sucursales Asignadas");
            } else if (request.getParameter("command").equals("asignaSucursalAuditor")){
                ArrayList<AuditoresVO> arrSucursalCargo = new ArrayList<AuditoresVO>(), arrSucursalCambio = new ArrayList<AuditoresVO>(), arrVerificar = new ArrayList<AuditoresVO>();
                AuditorHelper auditorHelper = new AuditorHelper();
                auditorVO = auditorHelper.getSucursalesAsignar(request);
                arrSucursalCargo = auditorDAO.findSucursalesAuditor(auditorVO.getNumAuditor(), 1);
                arrSucursalCambio =  auditorVO.getArrSucursal();
                arrVerificar = auditorHelper.getSucursalesVerificar(arrSucursalCargo, arrSucursalCambio);
                boolean sigue = auditorHelper.sucursalesBajaAuditor(auditorVO.getNumAuditor(), arrVerificar);
                SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                BitacoraUtil bitacora = null;
                String sucursales = "";
                if(sigue){
                    bitacora = new BitacoraUtil(idAuditor, request.getRemoteUser(), "CommandAsignacionAuditor");
                    if(arrSucursalCambio.size() == 0){
                        /*BAJA*/
                        for (AuditoresVO auditor : arrSucursalCargo) {
                            auditor.setNumAuditor(auditorVO.getNumAuditor());
                            auditorDAO.updateBajaSucursalAsig(auditor);
                        }
                        auditorDAO.updateBajaAuditor(auditorVO);
                        auditorVO.setArrSucursal(arrSucursalCambio);
                        bitacora.registraEventoString("idAuditor="+HTMLHelper.getParameterInt(request, "idAuditor")+", idSucursal=");
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Asignaci&oacute;n correcta. Auditor dato de baja");
                    } else if (arrSucursalCargo.size() == 0 && arrSucursalCambio.size() > 0){
                        /*ALTA*/
                        for (AuditoresVO sucursal : arrSucursalCambio) {
                            auditorDAO.insertSucursalAuditor(auditorVO.getNumAuditor(), sucursal.getIdSucursal());
                        }
                        auditorVO.setArrSucursal(arrSucursalCambio);
                        arrSucursalCargo = auditorVO.getArrSucursal();
                        for (AuditoresVO sucSaldos : arrSucursalCargo) {
                            saldoDAO.updateAuditorSaldos(auditorVO.getNumAuditor(), sucSaldos.getIdSucursal());
                            sucursales += sucSaldos.getIdSucursal()+",";
                        }
                        bitacora.registraEventoString("idAuditor="+HTMLHelper.getParameterInt(request, "idAuditor")+", idSucursal="+sucursales);
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Asignaci&oacute;n correcta");
                    } else {
                        /*CAMBIO*/
                        for (AuditoresVO sucursal : arrVerificar) {
                            if(auditorDAO.buscaSucursalAuditor(sucursal.getIdSucursal(), auditorVO.getNumAuditor())){
                                auditorVO.setIdSucursal(sucursal.getIdSucursal());
                                auditorDAO.updateBajaSucursalAsig(auditorVO);
                            }else{
                                auditorDAO.insertSucursalAuditor(auditorVO.getNumAuditor(), sucursal.getIdSucursal());
                            }
                        }
                        for (AuditoresVO sucursal : arrSucursalCambio) {
                            if(!auditorDAO.buscaSucursalAuditor(sucursal.getIdSucursal(), auditorVO.getNumAuditor())){
                                auditorVO.setIdSucursal(sucursal.getIdSucursal());
                                auditorDAO.insertSucursalAuditor(auditorVO.getNumAuditor(), sucursal.getIdSucursal());
                            }
                        }
                        auditorVO.setArrSucursal(arrSucursalCambio);
                        arrSucursalCargo = auditorVO.getArrSucursal();
                        for (AuditoresVO sucSaldos : arrSucursalCargo) {
                            saldoDAO.updateAuditorSaldos(auditorVO.getNumAuditor(), sucSaldos.getIdSucursal());
                            sucursales += sucSaldos.getIdSucursal()+",";
                        }
                        bitacora.registraEventoString("idAuditor="+HTMLHelper.getParameterInt(request, "idAuditor")+", idSucursal="+sucursales);
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Asignaci&oacute;n correcta");
                    }
                }else{
                    auditorVO.setArrSucursal(arrSucursalCargo);
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Las sucursales que estan a cargo del Auditor aun no han sido reasignadas");
                }
            }
            request.setAttribute("AUDITOR", auditorVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;        
    }
}

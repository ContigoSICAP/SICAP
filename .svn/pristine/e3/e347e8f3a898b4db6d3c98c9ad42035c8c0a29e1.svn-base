package com.sicap.clientes.commands;

import com.sicap.clientes.dao.AuditoresDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.AuditoresVO;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandModificaAuditor implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandModificaAuditor.class);

    public CommandModificaAuditor(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        AuditoresVO auditorVO = null;
        AuditoresVO modAuditorVO = null;
        ArrayList<AuditoresVO> arrAuditores = new ArrayList<AuditoresVO>(), arrSucursales = new ArrayList<AuditoresVO>();
        AuditoresDAO auditorDAO = new AuditoresDAO();
        try {
            int idAuditor = HTMLHelper.getParameterInt(request, "idAuditor");
            String nombre = HTMLHelper.getParameterString(request, "nombre");
            String apPaterno = HTMLHelper.getParameterString(request, "apPaterno");
            String apMaterno = HTMLHelper.getParameterString(request, "apMaterno");
            String rfc = HTMLHelper.getParameterString(request, "rfc").toUpperCase();
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            if(request.getParameter("command").equals("buscaAuditorModificar")){
                auditorVO = new AuditoresVO(idAuditor, nombre, apPaterno, apMaterno, rfc);
                arrAuditores = auditorDAO.findAuditor(auditorVO);
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Encontrados");
                if(arrAuditores.size() == 0)
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Sin Coincidencias");
            } else if(request.getParameter("command").equals("consultaDatosAuditor")){
                auditorVO = new AuditoresVO();
                auditorVO = auditorDAO.findAuditorID(idAuditor);
                notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "");
            } else if(request.getParameter("command").equals("modificaDatosAuditor")){
                auditorVO = auditorDAO.findAuditorID(idAuditor);
                modAuditorVO = new AuditoresVO(idAuditor, nombre, apPaterno, apMaterno, rfc, estatus);
                String verificaRFC = RFCUtil.obtenRFC(modAuditorVO.getNombre(), modAuditorVO.getApPaterno(), modAuditorVO.getApMaterno(), "1999-01-01");
                boolean esBaja = false, actualiza = true, sucActivo = false;
                if(modAuditorVO.getRfc().substring(0, 4).equals(verificaRFC.substring(0, 4))){
                    if(auditorVO.getApPaterno().toUpperCase().equals("X"))
                        auditorVO.setApPaterno("");
                    if(auditorVO.getApMaterno().toUpperCase().equals("X"))
                        auditorVO.setApMaterno("");
                    if(modAuditorVO.getEstatus() == 0 && auditorVO.getEstatus() != modAuditorVO.getEstatus()){
                        modAuditorVO.setFechaBaja(Convertidor.toSqlTimeStamp(new Date()));
                        esBaja = true;
                    } else if (auditorVO.getEstatus() != modAuditorVO.getEstatus()){
                        modAuditorVO.setFechaAlta(Convertidor.toSqlTimeStamp(new Date()));
                    }
                    if(!auditorVO.getRfc().substring(0, 10).equals(modAuditorVO.getRfc().substring(0, 10)) && auditorDAO.findAuditorRFC(modAuditorVO.getRfc().substring(0, 10))){
                        actualiza = false;
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Registro Duplicado. Verifique su informaci&oacute;n");
                    }
                    if(actualiza){
                        arrSucursales = auditorDAO.findSucursalesAuditor(auditorVO.getNumAuditor(), 1);
                        if(arrSucursales.size() > 0){
                            sucActivo = true;
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El Auditor aun tiene sucursales a cargo");
                        }else {
                            if(auditorDAO.updateAuditor(modAuditorVO, esBaja) == 1){
                                auditorVO = modAuditorVO;
                                BitacoraUtil bitacora = new BitacoraUtil(idAuditor, request.getRemoteUser(), "CommandModificaAuditor");
                                bitacora.registraEventoString("nombre="+auditorVO.getNombre()+", paterno="+auditorVO.getApPaterno()+", materno="+auditorVO.getApMaterno()+" rfc="+auditorVO.getRfc()+" estatus="+auditorVO.getEstatus());
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Actualizaci&oacute;n Confirmada");
                            }else
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con la Actualizaci&oacute;n");
                        }
                    }
                } else
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Sin correlaci&oacute;n en datos proporcionados");
            }
            request.setAttribute("AUDITORES", arrAuditores);
            request.setAttribute("AUDITOR", auditorVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

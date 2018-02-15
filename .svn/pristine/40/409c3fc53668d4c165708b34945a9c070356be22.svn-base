package com.sicap.clientes.commands;

import com.sicap.clientes.dao.AuditoresDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.AuditoresVO;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandAltaAuditor implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandAltaAuditor.class);

    public CommandAltaAuditor(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        AuditoresVO auditorVO = null;
        try {
            AuditoresDAO auditorDAO = new AuditoresDAO();
            int idAuditor = 0;
            String nombre = HTMLHelper.getParameterString(request, "nombre");
            String apPaterno = HTMLHelper.getParameterString(request, "apPaterno");
            String apMaterno = HTMLHelper.getParameterString(request, "apMaterno");
            String rfc = HTMLHelper.getParameterString(request, "rfc").toUpperCase();
            auditorVO = new AuditoresVO(0, nombre, apPaterno, apMaterno, rfc, 1);
            String verificaRFC = RFCUtil.obtenRFC(nombre, apPaterno, apMaterno, "1999-01-01");
            if(rfc.substring(0, 4).equals(verificaRFC.substring(0, 4))){
                if(!auditorDAO.findAuditorRFC(rfc.substring(0, 10))){
                    if(auditorVO.getApPaterno().toUpperCase().equals("X"))
                        auditorVO.setApPaterno("");
                    if(auditorVO.getApMaterno().toUpperCase().equals("X"))
                        auditorVO.setApMaterno("");
                    idAuditor = auditorDAO.insertAuditor(auditorVO);
                    if(idAuditor !=0){
                        auditorVO.setNumAuditor(idAuditor);
                        BitacoraUtil bitacora = new BitacoraUtil(idAuditor, request.getRemoteUser(), "CommandAltaAuditor");
                        bitacora.registraEventoString("nombre="+auditorVO.getNombre()+", paterno="+auditorVO.getApPaterno()+", materno="+auditorVO.getApMaterno()+" rfc="+auditorVO.getRfc());
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Ingreso Registro Correctamente");
                    } else
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con el Ingreso");
                } else
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Registro Duplicado. Verifique su informaci&oacute;n");
            } else
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Sin correlaci&oacute;n en datos proporcionados");
            request.setAttribute("AUDITOR", auditorVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
}

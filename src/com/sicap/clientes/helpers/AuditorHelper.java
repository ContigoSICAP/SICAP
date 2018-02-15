package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.AuditoresDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.vo.AuditoresVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class AuditorHelper {
    
    private static Logger myLogger = Logger.getLogger(AuditorHelper.class);
    
    public AuditoresVO getSucursalesAsignar(HttpServletRequest request) throws Exception{

        AuditoresVO auditor = new AuditoresVO();
        ArrayList<AuditoresVO> arrSucursal = new ArrayList<AuditoresVO>();
        int numSucursales = 0, idAuditor = 0;
        try {
            numSucursales = HTMLHelper.getParameterInt(request, "listaSuc");
            idAuditor = HTMLHelper.getParameterInt(request, "idAuditor");
            for (int i = 0; i <= numSucursales; i++) {
                if(request.getParameter("asignasuc"+i) != null){
                    arrSucursal.add(new AuditoresVO(HTMLHelper.getParameterInt(request, "idSucursal"+i)));
                }
            }
            auditor.setNumAuditor(idAuditor);
            auditor.setArrSucursal(arrSucursal);
        } catch (Exception e) {
            myLogger.error("getSucursalesAsignar", e);
            throw new CommandException(e.getMessage());
        }
        return auditor;
    }
    
    public boolean sucursalesBajaAuditor(int idAuditor, ArrayList<AuditoresVO> arrVerifica) throws CommandException{
        
        boolean sigue = true;
        int numSucursales = 0;
        try {
            AuditoresDAO auditorDAO = new AuditoresDAO();
            for (AuditoresVO sucursal : arrVerifica) {
                if(!auditorDAO.buscaSucursalAsignada(sucursal.getIdSucursal(), idAuditor))
                    sigue = false;
            }
        } catch (Exception e) {
            myLogger.error("sucursalesBajaAuditor", e);
            throw new CommandException(e.getMessage());
        }
        return sigue;
    }
    
    public ArrayList<AuditoresVO> getSucursalesVerificar(ArrayList<AuditoresVO> arrSucursalCargo, ArrayList<AuditoresVO> arrSucursalCambio) throws CommandException{
        
        ArrayList<AuditoresVO> arrVerifica = new ArrayList<AuditoresVO>();
        int numSucursales = 0;
        try {
            arrVerifica = (ArrayList<AuditoresVO>)arrSucursalCargo.clone();
            if(arrSucursalCambio.size() != 0){
                numSucursales = arrSucursalCargo.size();
                for (int i = 0; i < numSucursales; i++) {
                    for (AuditoresVO sucursal : arrSucursalCambio) {
                        if(arrSucursalCargo.get(i).getIdSucursal() == sucursal.getIdSucursal()){
                            arrVerifica.set(i, new AuditoresVO(0));
                            break;
                        }
                    }
                }
                for (int i = arrVerifica.size()-1; i >= 0; i--) {
                    if(arrVerifica.get(i).getIdSucursal() == 0)
                        arrVerifica.remove(i);
                }
            }
        } catch (Exception e) {
            myLogger.error("getSucursalesVerificar", e);
            throw new CommandException(e.getMessage());
        }
        return arrVerifica;
    }
}

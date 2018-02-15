/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rsolis
 */
public class TraspasoSaldoIxayaUtil extends DAOMaster{
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(TraspasoSaldoIxayaUtil.class);
    
    /**
     * Regresa 1 si se registró la transaccion, 0 si no 
     * @param idGrupo
     * @param idCiclo
     * @return 
     */
    public static int registraSaldoAFavorIxaya(int idGrupo, int idCiclo){
        int resultado = 0;
        try {
            //Obtenemos sucursal a partir del grupo y ciclo
            SaldoIBSDAO saldoDao = new SaldoIBSDAO();
            SaldoIBSVO saldoVo= saldoDao.getSaldos(idGrupo, idCiclo);
            int sucursal = saldoVo.getIdSucursal();
            myLogger.info("IdSucursal: "+sucursal);
            //Validar si la sucursal se encuentra entre las agregadas en el parametro LIBERACIONES_IXAYA
            boolean isSucursalEnIxaya = CatalogoHelper.esSucursaldeIxaya(sucursal);
            
            if(isSucursalEnIxaya){
                //Obtener credito a partir de grupo y ciclo
                CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
                CreditoCartVO creditoCartVo = creditoCartDAO.getCreditoClienteSol(idGrupo, idCiclo);
                
                if(creditoCartVo.getMontoCuenta()==0){
                    resultado=-1;
                
                }else{
                    //Inserta transacciones
                    TransaccionesHelper.registraSaldoAFavorIxaya(creditoCartVo);

                    //Actulizar en cartera_cec.d_credito el campo R_MONTO_CUENTA = 0;
                    creditoCartVo.setMontoCuenta(0);
                    creditoCartDAO.updatePagoCreditoCierre(creditoCartVo);

                    resultado=1;
                }
                
            }
            
            
            
        } catch (ClientesException ex) {
            Logger.getLogger(TraspasoSaldoIxayaUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TraspasoSaldoIxayaUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }
    
    
}

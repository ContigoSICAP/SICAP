/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.helpers;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FechasUtil;
import java.sql.Date;

/**
 *
 * @author Alex
 */
public class IntegrantesHelper {
    
    public int getTipoCliente(int idCliente, Date fechaIni, Date fechaFin){
        
        int tipo = 0;
        int direrencia = 0;
        /*System.out.println("idCliente "+idCliente);
        System.out.println("fechaIni "+fechaIni);
        System.out.println("fechaFin "+fechaFin);
        System.out.println("fechaIni.getMonth() "+fechaIni.getMonth());
        if(fechaFin!=null)
            System.out.println("fechaFin.getMonth() "+fechaFin.getMonth());*/
        if(fechaFin == null)
            tipo = ClientesConstants.TIPO_CLIENTE_NUEVO;
        else if(fechaIni.getMonth() == fechaFin.getMonth() && fechaIni.getYear() == fechaFin.getYear())
            tipo = ClientesConstants.TIPO_CLIENTE_RENOVADO;
        else if(fechaIni.getMonth() < fechaFin.getMonth() && fechaIni.getYear() == fechaFin.getYear())
            tipo = ClientesConstants.TIPO_CLIENTE_RENOVADO_ADELANTADO;
        else if(fechaIni.getMonth() == 11 && fechaIni.getYear() < fechaFin.getYear())//VALIDACION DE FIN DE AÑO
            tipo = ClientesConstants.TIPO_CLIENTE_RENOVADO_ADELANTADO;
        else{
            direrencia = FechasUtil.inBetweenDays(fechaFin, fechaIni);
            //System.out.println("direrencia "+direrencia);
            if(direrencia < 8)
                tipo = ClientesConstants.TIPO_CLIENTE_RECUPERADO7;
            else
                tipo = ClientesConstants.TIPO_CLIENTE_RECUPERADO;
        }
        //System.out.println("tipo "+tipo);
        return tipo;
    }
    
}

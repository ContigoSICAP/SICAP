/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.helpers;

import com.sicap.clientes.vo.ControlPagosVO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class ControlPagosHelper {
    
    public static ArrayList<ControlPagosVO> getControlPagos(HttpServletRequest request){
        
        ArrayList<ControlPagosVO> arrContPagos = new ArrayList<ControlPagosVO>();
        
        String numPago[] = request.getParameterValues("numPago");
        String fecReunion[] = request.getParameterValues("fecReunion");
        String fecReal[] = request.getParameterValues("fecReal");
        String fecPago[] = request.getParameterValues("fecPago");
        String difDiasFechas[] = request.getParameterValues("difDiasFechas");
        String montoPagoTab[] = request.getParameterValues("montoPagoTab");
        String montoPagoCajas[] = request.getParameterValues("montoPagoCajas");
        String difMontos[] = request.getParameterValues("difMontos");
        String ahorros[] = request.getParameterValues("ahorros");
        String saldoAhorro[] = request.getParameterValues("saldoAhorro");
        
        for (int i = 0; i < numPago.length; i++) {
            System.out.println("fecReunion[}: "+fecReunion[i]);
            System.out.println("fecReunion: "+Date.valueOf(fecReunion[i]));
            System.out.println("fecReunion2: "+Date.parse(fecReunion[i]));
            try {
                arrContPagos.add(new ControlPagosVO(Integer.parseInt(numPago[i]), Date.valueOf(fecReunion[i])));
                /*arrContPagos.add(new ControlPagosVO(, Date.valueOf(fecReunion[i]), Date.valueOf(fecReal[i]), Date.valueOf(fecPago[i]),
                    Integer.parseInt(difDiasFechas[i]), Double.parseDouble(montoPagoTab[i]), Double.parseDouble(montoPagoCajas[i]), 
                    Integer.parseInt(difMontos[i]), Integer.parseInt(ahorros[i]), Integer.parseInt(saldoAhorro[i])));*/
            System.out.println("arreglo: "+arrContPagos.get(i).getNumPago());
            } catch (Exception e) {
                System.out.println("error: "+e);
            }
            
        }
        
        return arrContPagos;
    }
}

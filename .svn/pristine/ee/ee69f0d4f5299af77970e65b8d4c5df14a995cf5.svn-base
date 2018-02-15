package com.sicap.clientes.helpers;

import com.sicap.clientes.vo.CicloGrupalVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class EjecutivosHelper {
    
    public ArrayList<CicloGrupalVO> getAsignacionCarteraHelper(HttpServletRequest request) {

        ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();
        String[] idClientes = null;
        try {
            idClientes = request.getParameterValues("asignaCartera");
            if (idClientes != null) {
                for (int i = 0; i < idClientes.length; i++) {
                    arrEquipos.add(new CicloGrupalVO(HTMLHelper.getParameterInt(request, "idGrupo"+idClientes[i]), HTMLHelper.getParameterInt(request, "idCiclo"+idClientes[i])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrEquipos;
    }
}

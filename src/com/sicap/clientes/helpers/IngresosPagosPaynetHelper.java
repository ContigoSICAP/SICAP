package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.PaynetDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CifraControlVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.PaynetVO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

public class IngresosPagosPaynetHelper {
    
    private static Logger myLogger = Logger.getLogger(IngresosPagosPaynetHelper.class);
    
    public static void traspasoTransPaynetPagos(){
        
        ArrayList<PaynetVO> arrTrans = new ArrayList<PaynetVO>();
        PaynetDAO paynetDAO = new PaynetDAO();
        Date fecha = new Date();
        fecha.setDate(fecha.getDate()-1);
        ParametroVO param = new ParametroVO();
        try {
            arrTrans = paynetDAO.getTransAutorizadasPaynet(Convertidor.toSqlDate(fecha));
            if(arrTrans.size()>0){
                //new PagosReferenciadosHelper().procesaPagos(arrTrans, 0);
                new PagosReferenciadosHelper().procesaPagos(arrTrans, new CuentasBancariasDAO().getNumCuentaBancaria(ClientesConstants.ID_BANCO_PAYNET, "00220", "C"));
            } else
                myLogger.debug("Transacciones Autorizadas Vacias =(");
        } catch (Exception e) {
            myLogger.error("Error en traspasoTransPaynetPagos ", e);
        }
        
    }
    
}

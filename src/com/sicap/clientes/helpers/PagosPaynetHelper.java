package com.sicap.clientes.helpers;

import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.PaynetVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

public class PagosPaynetHelper {
    
    private static Logger myLogger = Logger.getLogger(PagosPaynetHelper.class);
    
    public ArrayList<PaynetVO> getPagosManuales(FileItem archivo){

        ArrayList<PaynetVO> arrPagosPay = new ArrayList();
        PaynetVO pagoPay = new PaynetVO();
        //String strFecha = "";
        try {
            InputStream data = archivo.getInputStream();
            DataInputStream in = new DataInputStream(data); 
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            String linea;
            String[] cadena;
            while ((linea = buffer.readLine()) != null){
                if(linea.substring(0, 3).equals("REG")){
                    cadena = linea.split("\\|");
                    for (int i = 0; i < cadena.length; i++) {
                        switch(i){
                            case 2:
                                //strFecha = cadena[i].substring(6, 10)+"-"+ cadena[i].substring(3, 5)+"-"+ cadena[i].substring(0, 2);
                                pagoPay.setFechaAutPagPay(Convertidor.stringToSqlDate(cadena[i]));
                                break;
                            case 3:
                                pagoPay.setHoraPago(cadena[i]);
                                break;
                            case 4:
                                pagoPay.setIdPago(Integer.valueOf(cadena[i]));
                                break;
                            case 5:
                                pagoPay.setMonto(Double.valueOf(cadena[i]));
                                break;
                            case 6:
                                pagoPay.setReferenciaPay(cadena[i]);
                                break;
                            case 7:
                                pagoPay.setReferencia(cadena[i]);
                                break;
                        }
                    }
                    arrPagosPay.add(new PaynetVO(pagoPay.getIdPago(), pagoPay.getReferenciaPay(), pagoPay.getMonto(), pagoPay.getFechaAutPagPay(), pagoPay.getEstatus(), pagoPay.getReferencia(), pagoPay.getHoraPago()));
                }
            }
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }

        return arrPagosPay;
    }
    
    public ArrayList<PaynetVO> getTransaccionesPaynet(HttpServletRequest request){

        ArrayList<PaynetVO> arrPagosPay = new ArrayList();
        ReferenciaGeneralVO refVO = null;
        String[] idTran = null;
        BitacoraUtil bitacora = null;
        String fecha1, fecha2;
        try {
            idTran = request.getParameterValues("idCheckBox");
            if (idTran != null) {
                for (int i = 0; i < idTran.length; i++) {
                    fecha1 = request.getParameter("fechaAutPagPay"+idTran[i]);
                    fecha1 = fecha1.substring(8, 10)+"/"+fecha1.substring(5, 7)+"/"+fecha1.substring(0, 4);
                    fecha2 = request.getParameter("fechaAutMovPay"+idTran[i]);
                    fecha2 = fecha2.substring(8, 10)+"/"+fecha2.substring(5, 7)+"/"+fecha2.substring(0, 4);
                    bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "numEquipo"+idTran[i]), request.getRemoteUser(), "CommandIncidenciasPaynet");
                    refVO = new ReferenciaGeneralVO();
                    refVO.numcliente = HTMLHelper.getParameterInt(request, "numEquipo"+idTran[i]);
                    refVO.nombre = HTMLHelper.getParameterString(request, "nombre"+idTran[i]);
                    refVO.numSolicitud = HTMLHelper.getParameterInt(request, "numSolicitud"+idTran[i]);
                    refVO.referencia = HTMLHelper.getParameterString(request, "referencia"+idTran[i]);
                    arrPagosPay.add(new PaynetVO(HTMLHelper.getParameterInt(request, "idPago"+idTran[i]), HTMLHelper.getParameterString(request, "referenciaPay"+idTran[i]), HTMLHelper.getParameterDouble(request, "monto"+idTran[i]),
                            Convertidor.stringToSqlDate(fecha1), Convertidor.stringToSqlDate(fecha2), HTMLHelper.getParameterInt(request, "estatus"+idTran[i]), refVO));
                    bitacora.registraEventoString("accion="+request.getParameter("command")+", idTransaccionPaynet="+HTMLHelper.getParameterString(request, "idPago"+idTran[i])+", referenciaPaynet="+HTMLHelper.getParameterString(request, "referenciaPay"+idTran[i])+" estatus="+HTMLHelper.getParameterString(request, "estatus"+idTran[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrPagosPay;
    }
}

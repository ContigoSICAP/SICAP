package com.sicap.clientes.helpers.cartera;

import java.util.Calendar;
import java.util.Vector;
import java.util.Date;

import com.sicap.clientes.dao.cartera.TransaccionesContabDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.cartera.TransaccionesVO;
import com.sicap.clientes.vo.cartera.TransaccionesContabVO;

public class TransaccionesContabHelper {

    public static boolean registraTransaccion(TransaccionesContabVO[] transaccion) throws Exception {

        boolean result = true;
        TransaccionesContabVO temp = new TransaccionesContabVO();
        TransaccionesContabDAO transaccionDAO = new TransaccionesContabDAO();
        Calendar calendario = Calendar.getInstance();

        try {
            for (int i = 0; i < transaccion.length; i++) {
                temp.numTransaccion = transaccion[i].numTransaccion;
                temp.secuencial = i;
                temp.numCredito = transaccion[i].numCredito;
                temp.numCliente = transaccion[i].numCliente;
                temp.numProducto = transaccion[i].numProducto;
                temp.centroCostos = transaccion[i].centroCostos;
                temp.centroCostosDest = transaccion[i].centroCostosDest;
                temp.tipoTransaccion = transaccion[i].tipoTransaccion;
                temp.fechaProceso = transaccion[i].fechaProceso;
                temp.fechaValor = transaccion[i].fechaValor;
                temp.rubro = transaccion[i].rubro;
                temp.monto = transaccion[i].monto;
                temp.statusRubro = transaccion[i].statusRubro;
                temp.status = transaccion[i].status;
                temp.numCuenta = transaccion[i].numCuenta;
                temp.tipoMovimiento = transaccion[i].tipoMovimiento;
                temp.fondeador = transaccion[i].fondeador;
                temp.numCuentaSap = transaccion[i].numCuentaSap;
                transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static TransaccionesContabVO asignaTransaccion(TransaccionesVO transaccion, String numCuenta, String tipoMovimiento, int fondeador) throws Exception {

        TransaccionesContabVO temp = new TransaccionesContabVO();

        try {
            temp.numTransaccion = transaccion.numTransaccion;
            temp.secuencial = transaccion.secuencial;
            temp.numCredito = transaccion.numCredito;
            temp.numCliente = transaccion.numCliente;
            temp.numProducto = transaccion.numProducto;
            temp.centroCostos = transaccion.centroCostos;
            temp.tipoTransaccion = transaccion.tipoTransaccion;
            temp.fechaProceso = transaccion.fechaProceso;
            temp.fechaValor = transaccion.fechaValor;
            temp.rubro = transaccion.rubro;
            temp.monto = transaccion.monto;
            temp.statusRubro = transaccion.statusRubro;
            temp.status = transaccion.status;
            temp.numCuenta = numCuenta;
            temp.tipoMovimiento = tipoMovimiento;
            temp.fondeador = fondeador;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return temp;
    }
    public static String tipoDevolucion (boolean esAdicional, boolean esInterciclo){
        String tipo ="";
        if (esAdicional){
            tipo = ClientesConstants.DEVOLUCION_ADICIONAL;
        }else if (esInterciclo){            
            tipo = ClientesConstants.DEVOLUCION_INTERCICLO;
        }else{            
            tipo = ClientesConstants.DEVOLUCION;
        }
        return tipo;
    }

}

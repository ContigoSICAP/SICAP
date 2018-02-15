package com.sicap.clientes.helpers.cartera;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Date;

import com.sicap.clientes.dao.cartera.PerfilesContablesDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.cartera.PerfilesContablesVO;
import com.sicap.clientes.vo.cartera.TransaccionesContabVO;
import com.sicap.clientes.vo.cartera.TransaccionesVO;

public class PerfilesContablesHelper {

    public static TreeMap getCatalogoPerfiles(PerfilesContablesVO[] perfiles) {
        TreeMap<String, String> catalogo = new TreeMap<String, String>();
        for (int i = 0; i < perfiles.length; i++) {
            catalogo.put(new String(perfiles[i].codigoRubro + perfiles[i].statusRubro), perfiles[i].numCuenta + perfiles[i].tipoMovimiento);
        }
        return catalogo;

    }

    public static String getDescripcion(TreeMap catalogo, String indice) {

        String descripcion = new String();
        if (indice == null) {
            return "N/D";
        }
        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                if (key.toString().equals(String.valueOf(indice))) {
                    descripcion = catalogo.get(key).toString();
                }
            }
        }
        return descripcion;

    }

    public static TransaccionesContabVO[] getCuentaTipoMov(PerfilesContablesVO[] perfiles, TransaccionesVO transaccion, int fondeador) {
        TransaccionesContabVO transaccionContab = null;
        ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
        TransaccionesContabVO elementos[] = null;
        System.out.println("longitud perfil " + perfiles.length);
        for (int i = 0; i < perfiles.length; i++) {
            // Si el codigo de rubro y el status coinciden asigna la cuenta contable y el codigo de rubro a 
            if (perfiles[i].codigoRubro.equals(String.valueOf(transaccion.rubro)) && perfiles[i].statusRubro.equals(String.valueOf(transaccion.statusRubro)) && perfiles[i].origen == transaccion.origen) {
                transaccionContab = new TransaccionesContabVO();
                transaccionContab.numTransaccion = transaccion.numTransaccion;
                transaccionContab.secuencial = transaccion.secuencial;
                transaccionContab.numCredito = transaccion.numCredito;
                transaccionContab.numCliente = transaccion.numCliente;
                transaccionContab.numProducto = transaccion.numProducto;
                transaccionContab.centroCostos = transaccion.centroCostos;
                transaccionContab.tipoTransaccion = transaccion.tipoTransaccion;
                transaccionContab.fechaProceso = transaccion.fechaProceso;
                transaccionContab.fechaValor = transaccion.fechaValor;
                transaccionContab.rubro = transaccion.rubro;
                transaccionContab.monto = transaccion.monto;
                transaccionContab.statusRubro = transaccion.statusRubro;
                transaccionContab.status = transaccion.status;
                transaccionContab.numCuenta = perfiles[i].numCuenta;
                transaccionContab.tipoMovimiento = perfiles[i].tipoMovimiento;
                transaccionContab.fondeador = fondeador;
                transaccionContab.centroCostosDest = perfiles[i].centroCostosDest;
                transaccionContab.numCuentaSap = perfiles[i].numCuentaSap;
                array.add(transaccionContab);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesContabVO[array.size()];
                for (int h = 0; h < elementos.length; h++) {
                    elementos[h] = (TransaccionesContabVO) array.get(h);
                }
            }
        }
        return elementos;

    }

}

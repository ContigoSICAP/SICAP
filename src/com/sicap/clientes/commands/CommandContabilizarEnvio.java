package com.sicap.clientes.commands;

import java.util.ArrayList;
import com.sicap.clientes.dao.cartera.TransaccionesContabDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.cartera.GeneraArchivoContpaqHelper;
import com.sicap.clientes.vo.cartera.TransaccionesContabVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandContabilizarEnvio implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandContabilizarEnvio.class);
    TransaccionesContabDAO transaccionDAO = new TransaccionesContabDAO();
    private int contadorGral;

    public CommandContabilizarEnvio(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        ArrayList<TransaccionesContabVO> arrFechasTrans = new ArrayList<TransaccionesContabVO>();
        String cuentasBNTE = "'1102004002','1102005003','1102053003','1102004003'";
        String cuentasBNMX = "'1102051002','1102054003','1102051003'";
        String cuentasBBVA = "'1102102002','1102109003'";
        String cuentasSCOT = "'1102201002','1102301003'";
        String cuentasBAJI = "'1102151002','1102151003'";
        String cuentasCORR = cuentasBNTE+","+cuentasBNMX+","+cuentasBBVA+","+cuentasSCOT+","+cuentasBAJI;
        //String cuentasSNTDR = "";
        String lstTransacciones = "";
        myLogger.debug("Iniciando mayorizacion");
        try {
            arrFechasTrans = transaccionDAO.getFechasTransaccion();
            TransaccionesContabVO[] transaccion_ing = null;
            String ejercicio = "";
            for (TransaccionesContabVO fechaTrans : arrFechasTrans) {
                ejercicio = Convertidor.dateToString(fechaTrans.getFechaProceso(), "yyyy-MM-dd").substring(0, 7);
                transaccion_ing = transaccionDAO.getElementosIng(ejercicio);
                generaArchivosCONTPAQ(transaccion_ing);
            }
            myLogger.debug("Termina mayorizacion CONTPAQ");
            contadorGral = 0;
            for (TransaccionesContabVO fechaTrans : arrFechasTrans) {
                ejercicio = Convertidor.dateToString(fechaTrans.getFechaProceso(), "yyyy-MM-dd").substring(0, 7);
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasBNTE, true);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), ClientesConstants.ID_BANCO_BANORTE);
                }
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasBNMX, true);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), ClientesConstants.ID_BANCO_BANAMEX);
                }
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasBBVA, true);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), ClientesConstants.ID_BANCO_BANCOMER);
                }
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasSCOT, true);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), ClientesConstants.ID_BANCO_SCOTIABANK);
                }
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasBAJI, true);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), ClientesConstants.ID_BANCO_BANBAJIO);
                }
                lstTransacciones = transaccionDAO.getNumTransacciones(ejercicio, cuentasCORR, false);
                if(!lstTransacciones.equals("")){
                    transaccion_ing = transaccionDAO.getElementosIngSAP(ejercicio, lstTransacciones);
                    generaArchivosSAP(transaccion_ing, ejercicio, fechaTrans.getFechaProceso(), 0);
                }
            }
            myLogger.debug("Termina mayorizacion SAP");
            siguiente = "/mayoriza.jsp";
        } catch (Exception e) {
            myLogger.error("Error mayotizacion", e);
        }
        return siguiente;
    }
    
    private void generaArchivosCONTPAQ(TransaccionesContabVO[] transaccion_ing) throws ClientesDBException{
        
        Connection con = null;
        try {
            con = ConnectionManager.getCWConnection();
            con.setAutoCommit(false);
            boolean error = true;
            String tipo_transaccion = "";
            int tipo_operacion = 0, fondeador = 0, centro_costos = 0, contador = 0;
            ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
            GeneraArchivoContpaqHelper archivoHelper = new GeneraArchivoContpaqHelper();
            myLogger.debug("Transacciones a procesar: " + transaccion_ing.length);
            for (int j = 0; j < transaccion_ing.length; j++) {
                myLogger.debug("Procesando elemento: " + j);
                myLogger.debug("Transaccion: " + transaccion_ing[j].toString());
                if (j == 0) {
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    fondeador = transaccion_ing[j].fondeador;
                }
                if (tipo_transaccion.equals(String.valueOf(transaccion_ing[j].tipoTransaccion))
                        && tipo_operacion == transaccion_ing[j].numProducto &&
                        fondeador == transaccion_ing[j].fondeador) {
                    array.add(transaccion_ing[j]);
                    transaccion_ing[j].status = ClientesConstants.TRANSACCION_ENVIADA;
                    transaccionDAO.updateStatusTransaccion(con, transaccion_ing[j]);
                } else {
                    error = archivoHelper.generaArchivo(array, contador++);
                    if(!error)
                        con.commit();
                    else
                        con.rollback();
                    array = new ArrayList<TransaccionesContabVO>();
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    fondeador = transaccion_ing[j].fondeador;
                    array = new ArrayList<TransaccionesContabVO>();
                    array.add(transaccion_ing[j]);
                    transaccion_ing[j].status = ClientesConstants.TRANSACCION_ENVIADA;
                    transaccionDAO.updateStatusTransaccion(con, transaccion_ing[j]);
                }
            }
            archivoHelper = new GeneraArchivoContpaqHelper();
            error = archivoHelper.generaArchivo(array, contador++);
            if(!error)
                con.commit();
            else
                con.rollback();
        } catch (Exception e) {
            myLogger.error("Error generaArchivosCONTPAQ", e);
        } finally{
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    private void generaArchivosSAP(TransaccionesContabVO[] transaccion_ing, String ejercicio, Date fechaCorte, int banco) throws ClientesDBException{
        
        Connection con = null;
        try {
            con = ConnectionManager.getCWConnection();
            con.setAutoCommit(false);
            boolean error = true;
            String tipo_transaccion = "";
            int tipo_operacion = 0, fondeador = 0, centro_costos = 0, contador = 0;
            ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
            GeneraArchivoContpaqHelper archivoHelper = new GeneraArchivoContpaqHelper();
            myLogger.debug("Transacciones a procesar: " + transaccion_ing.length);
            for (int j = 0; j < transaccion_ing.length; j++) {
                myLogger.debug("Procesando elemento: " + j);
                myLogger.debug("Transaccion: " + transaccion_ing[j].toString());
                if (j == 0) {
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    fondeador = transaccion_ing[j].fondeador;
                }
                if (tipo_transaccion.equals(String.valueOf(transaccion_ing[j].tipoTransaccion))
                        && tipo_operacion == transaccion_ing[j].numProducto &&
                        fondeador == transaccion_ing[j].fondeador) {
                    array.add(transaccion_ing[j]);
                    transaccion_ing[j].statusSAP = ClientesConstants.TRANSACCION_ENVIADA;
                    transaccionDAO.updateStatusTransaccionSAP(con, transaccion_ing[j]);
                } else {
                    error = archivoHelper.generaArchivoSAP(array, contador++, ejercicio, fechaCorte, banco, contadorGral++);
                    if(!error)
                        con.commit();
                    else
                        con.rollback();
                    array = new ArrayList<TransaccionesContabVO>();
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    fondeador = transaccion_ing[j].fondeador;
                    array = new ArrayList<TransaccionesContabVO>();
                    array.add(transaccion_ing[j]);
                    transaccion_ing[j].statusSAP = ClientesConstants.TRANSACCION_ENVIADA;
                    transaccionDAO.updateStatusTransaccionSAP(con, transaccion_ing[j]);
                }
            }
            archivoHelper = new GeneraArchivoContpaqHelper();
            error = archivoHelper.generaArchivoSAP(array, contador++, ejercicio, fechaCorte, banco, contadorGral++);
            if(!error)
                con.commit();
            else
                con.rollback();
        } catch (Exception e) {
            myLogger.error("Error generaArchivosSAP", e);
        } finally{
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
}

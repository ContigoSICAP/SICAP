package com.sicap.clientes.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.PerfilesContablesDAO;
import com.sicap.clientes.dao.cartera.TransaccionesDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.TransaccionesContabHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.helpers.cartera.PerfilesContablesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.PerfilesContablesVO;
import com.sicap.clientes.vo.cartera.TransaccionesVO;
import com.sicap.clientes.vo.cartera.TransaccionesContabVO;
import javax.servlet.http.HttpServletRequest;

public class CommandContabilizar implements Command {

    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandDesembolsoGrupal.class);

    public CommandContabilizar(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        PerfilesContablesDAO perfilDAO = new PerfilesContablesDAO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        PerfilesContablesHelper perfilHelper = new PerfilesContablesHelper();
        myLogger.debug("Iniciando");
        try {
            /* OBTENCION DE TRANSACCIONES PENDIENTES DE CONTABILIZAR  JBL-OCT10 
             * Extrae las transacciones con estatus de Ingresadas
             */
            TransaccionesVO[] transaccion_ing = null;
            String tipo_transaccion = "";
            int tipo_operacion = 0;
            int fondeador = 0;
            int centro_costos = 0;
            int num_transaccion = 0;
            int origen = 0;
            double debe = 0;
            double haber = 0;
            boolean cuadrado = false;
            PerfilesContablesVO[] perfilVO = null;
            //CreditoCartVO credito = new CreditoCartVO();
            //CreditoCartDAO creditoDAO = new CreditoCartDAO();
            TransaccionesContabVO[] transacciones_cont = null;
            TransaccionesContabVO[] transaccion_cont = null;
            String fecha = request.getParameter("Fecha");
            String fechaFin = request.getParameter("FechaFin");

            ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
            boolean resultado;
            double diferencia = 0;

            transaccion_ing = transaccionDAO.getElementosIng(fecha, fechaFin);
            int numTranscciones = transaccion_ing.length;
            myLogger.debug("Transacciones a procesar: "+numTranscciones);
            // Se procesan todas las transacciones pendientes de contabilizar
            for (int j = 0; j < numTranscciones; j++) {
                myLogger.debug("Procesando transaccion indice: " + j);
                myLogger.debug("Transaccion : " + transaccion_ing[j].toString());
                
                if (j == 0) {        
                    // Se asignan las variables a verificar para en el caso de cambio se genere una poliza	
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    num_transaccion = transaccion_ing[j].numTransaccion;
                    myLogger.debug("Procesando transaccion: " + num_transaccion);
                    origen = transaccion_ing[j].origen;
                    myLogger.debug("Origen: " + origen);
                    // Se traen los perfiles contables y el credito para obtener el fondeador
                    //credito = creditoDAO.getCredito(transaccion_ing[j].numCredito);
                    //myLogger.debug("Credito: " + credito);
                    fondeador = transaccion_ing[j].fondeador;
                    myLogger.debug("fondeador: " + fondeador);
                    perfilVO = perfilDAO.getElementosTrans(tipo_transaccion, fondeador, tipo_operacion, centro_costos);
                    //perfilVO = perfilDAO.getElementosTrans(tipo_transaccion, 1, tipo_operacion, centro_costos);
                    myLogger.debug("Perfil contable: " + perfilVO.toString());
                }
                /*
                 * Se comparan los valores de las tres variables para saber si damos por buena la transaccion y sumamos a los totales				
                 */
                if (tipo_transaccion.equals(String.valueOf(transaccion_ing[j].tipoTransaccion)) && tipo_operacion == transaccion_ing[j].numProducto && centro_costos == transaccion_ing[j].centroCostos && fondeador == transaccion_ing[j].fondeador) {
//				if (tipo_transaccion.equals(String.valueOf(transaccion_ing[j].tipoTransaccion)) && tipo_operacion == transaccion_ing[j].numProducto && centro_costos == 1 ){
					/*
                     * Se verifica si continuamos en la misma transaccion				
                     */
                    if (num_transaccion == transaccion_ing[j].numTransaccion) {
                        /*
                         * Se obtiene la cuenta y el tipo de movimiento que corresponde al rubro y codigo de rubro en los perfiles de la transaccion	
                         */
                        myLogger.debug("Entra a la misma transaccion");
                        transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], transaccion_ing[j].fondeador);
                        //transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], 1);
                        if (transacciones_cont == null) {
                            myLogger.debug("No se encontro el perfil para el siguiente no. transaccion " + transaccion_ing[j].numTransaccion + " rubro " + transaccion_ing[j].rubro + " status " + transaccion_ing[j].statusRubro);
                            break;
                        }
                        /*
                         * Se lleva el calculo del debe y el haber
                         */
                        for (int h = 0; h < transacciones_cont.length; h++) {
                            myLogger.debug("Tipo de movimiento " + transacciones_cont[h].tipoMovimiento);
                            if (transacciones_cont[h].tipoMovimiento.equals(String.valueOf("D"))) {
                                debe = debe + transacciones_cont[h].monto;
                            } else {
                                haber = haber + transacciones_cont[h].monto;
                            }
                            array.add(transacciones_cont[h]);
                        }
                    } else {
                        /* Se verifica si el debe y el haber son iguales para las transacciones de pago , registro de pago y desembolso, en caso afirmativo se inserta la transaccion cuadrada
                         */
                        myLogger.debug("Entra a cambio de transaccion");
                        diferencia = debe - haber;
                        if (diferencia < 0.01 && diferencia > -0.01) {
                            cuadrado = true;
                        } else {
                            cuadrado = false;
                        }
                        if (cuadrado) {
                            myLogger.debug("La transaccion se encuentra cuadrada");
                            if (array.size() > 0) {
                                transaccion_cont = new TransaccionesContabVO[array.size()];
                                for (int i = 0; i < transaccion_cont.length; i++) {
                                    transaccion_cont[i] = (TransaccionesContabVO) array.get(i);
                                }
                            }
                            resultado = TransaccionesContabHelper.registraTransaccion(transaccion_cont);
                            if (resultado) {
                                /* Se actualiza el status de la transaccion a cuadrada
                                 */
                                transaccion_ing[j - 1].status = ClientesConstants.TRANSACCION_CUADRADA;
                                transaccionDAO.updateStatusTransaccion(transaccion_ing[j - 1]);
                                myLogger.debug("Actualizo transaccion: " + (j - 1));
                            } else {
                                myLogger.debug("Error al actualizar la transaccion " + transaccion_ing[j - 1].numTransaccion);
                            }
                        } else {
                            myLogger.debug("La transaccion " + transaccion_ing[j - 1].numTransaccion + "no esta cuadrada por " + diferencia);
                        }
                        /* Se comienza con los datos de la nueva transaccion
                         */
                        debe = 0;
                        haber = 0;
                        transacciones_cont = null;
                        array = new ArrayList<TransaccionesContabVO>();
                        /* Se comienza con los datos de la nueva transaccion
                         */
                        //credito = creditoDAO.getCredito(transaccion_ing[j].numCredito);
                        transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], transaccion_ing[j].fondeador);
                        //transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], 1);
                        if (transacciones_cont == null) {
                            myLogger.debug("No se encontro el perfil para el siguiente no. transaccion " + transaccion_ing[j].numTransaccion + " rubro " + transaccion_ing[j].rubro + " status " + transaccion_ing[j].statusRubro);
                            break;
                        }
                        for (int h = 0; h < transacciones_cont.length; h++) {
                            myLogger.debug("Tipo movimiento " + transacciones_cont[h].tipoMovimiento);
                            if (transacciones_cont[h].tipoMovimiento.equals(String.valueOf("D"))) {
                                debe = debe + transacciones_cont[h].monto;
                            } else {
                                haber = haber + transacciones_cont[h].monto;
                            }
                            array.add(transacciones_cont[h]);
                        }
                        /*
                         * Se asinga la transacion	
                         */
// Se asignan las variables a la nueva transaccion						
                        tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                        tipo_operacion = transaccion_ing[j].numProducto;
                        centro_costos = transaccion_ing[j].centroCostos;
//						centro_costos 	 = 1;
                        num_transaccion = transaccion_ing[j].numTransaccion;
                    }
                } else {
                    /* Se verifica si el debe y el haber son iguales para las transacciones de pago , registro de pago y desembolso, en caso afirmativo se inserta la transaccion cuadrada
                     */
                    diferencia = debe - haber;
                    if (diferencia < 0.01 && diferencia > -0.01) {
                        cuadrado = true;
                    } else {
                        cuadrado = false;
                    }
                    if (cuadrado) {
                        if (array.size() > 0) {
                            transaccion_cont = new TransaccionesContabVO[array.size()];
                            for (int i = 0; i < transaccion_cont.length; i++) {
                                transaccion_cont[i] = (TransaccionesContabVO) array.get(i);
                            }
                        }
                        resultado = TransaccionesContabHelper.registraTransaccion(transaccion_cont);
                        if (resultado) {
                            /* Se actualiza el status de la transaccion a cuadrada
                             */
                            transaccion_ing[j - 1].status = ClientesConstants.TRANSACCION_CUADRADA;
                            transaccionDAO.updateStatusTransaccion(transaccion_ing[j - 1]);
                            myLogger.debug("Actualizo transaccion: " + (j - 1) );
                        } else {
                            myLogger.debug("Error al actualizar la transaccion " + transaccion_ing[j - 1].numTransaccion);
                        }
                    } else {
                        myLogger.debug("La transaccion " + transaccion_ing[j - 1].numTransaccion + "no esta cuadrada por " + diferencia);
                    }
                    /* Se comienza con los datos de la nueva transaccion
                     */
                    debe = 0;
                    haber = 0;
                    transacciones_cont = null;
                    array = new ArrayList<TransaccionesContabVO>();
                    /* Se comienza con los datos de la nueva transaccion
                     */
                    //Se asignan las variables a la nueva transaccion						
                    tipo_transaccion = transaccion_ing[j].tipoTransaccion;
                    tipo_operacion = transaccion_ing[j].numProducto;
                    centro_costos = transaccion_ing[j].centroCostos;
                    num_transaccion = transaccion_ing[j].numTransaccion;
                    origen = transaccion_ing[j].origen;
                    fondeador = transaccion_ing[j].fondeador;

                    /* Se busca el nuevo perfil contable
                     */
                    // Se traen los perfiles contables y el credito para determinar el fondeador
                    //credito = creditoDAO.getCredito(transaccion_ing[j].numCredito);
                    perfilVO = perfilDAO.getElementosTrans(tipo_transaccion, fondeador, tipo_operacion, centro_costos);
                    //perfilVO = perfilDAO.getElementosTrans(tipo_transaccion, 1, tipo_operacion, centro_costos);
                    //Se consulta el nuevo perfil
                    transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], transaccion_ing[j].fondeador);
                    //transacciones_cont = PerfilesContablesHelper.getCuentaTipoMov(perfilVO, transaccion_ing[j], 1);
                    // Se verifica que se hayan encontrado coincidencias en el perfil
                    if (transacciones_cont == null) {
                        myLogger.debug("No se encontro el perfil para el siguiente no. transaccion " + transaccion_ing[j].numTransaccion + " rubro " + transaccion_ing[j].rubro + " status " + transaccion_ing[j].statusRubro);
                        break;
                    }
                    /*
                     * Se lleva el calculo del debe y el haber
                     */
                    for (int h = 0; h < transacciones_cont.length; h++) {
                        myLogger.debug("Tipo movimiento: " + transacciones_cont[h].tipoMovimiento);
                        if (transacciones_cont[h].tipoMovimiento.equals("D")) {
                            debe = debe + transacciones_cont[h].monto;
                        } else {
                            haber = haber + transacciones_cont[h].monto;
                        }
                        array.add(transacciones_cont[h]);
                    }
                }
            }
            myLogger.debug("Entra a la ultima transaccion");
            diferencia = debe - haber;
            if (diferencia < 0.01 && diferencia > -0.01) {
                cuadrado = true;
            } else {
                // En caso de ser la diferencia menor a un centavo lo dejamos como cuadrado
                cuadrado = false;
            }
            if (cuadrado) {
                myLogger.debug("La transaccion "+transaccion_ing[transaccion_ing.length - 1].numTransaccion+" se encuentra cuadrada");
                if (array.size() > 0) {
                    transaccion_cont = new TransaccionesContabVO[array.size()];
                    for (int i = 0; i < transaccion_cont.length; i++) {
                        transaccion_cont[i] = (TransaccionesContabVO) array.get(i);
                    }
                }
                resultado = TransaccionesContabHelper.registraTransaccion(transaccion_cont);
                if (resultado) {
                    /* Se actualiza el status de la transaccion a cuadrada
                     */
                    transaccion_ing[transaccion_ing.length - 1].status = ClientesConstants.TRANSACCION_CUADRADA;
                    transaccionDAO.updateStatusTransaccion(transaccion_ing[transaccion_ing.length - 1]);
                    myLogger.debug("Actualizo transaccion: " + (transaccion_ing.length - 1));
                } else {
                    myLogger.debug("Error al actualizar la transaccion " + transaccion_ing[transaccion_ing.length - 1].numTransaccion);
                }
            } else {
                myLogger.debug("La transaccion " + transaccion_ing[transaccion_ing.length - 1].numTransaccion + "no esta cuadrada por " + diferencia);
            }

            Calendar c1 = Calendar.getInstance();
            myLogger.debug("Actualiza parametro de fecha " + c1.getTime().toString());
            ParametroVO parametroVO = new ParametroVO();
            CatalogoDAO catalogoDAO = new CatalogoDAO();
            parametroVO = catalogoDAO.updateParametro("FECHA_CONTABLE", Convertidor.dateToString(c1.getTime(), ClientesConstants.FORMATO_FECHA_EU));
            parametroVO = catalogoDAO.getParametro("FECHA_CONTABLE");
            siguiente = "/contabiliza.jsp";

        } catch (Exception e) {
            myLogger.error("execute", e);
        }
        return siguiente;
    }
}

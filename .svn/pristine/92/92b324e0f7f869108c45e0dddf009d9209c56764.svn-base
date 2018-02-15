package com.sicap.clientes.helpers.cartera;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.SaldoFondeadorDAO;
import java.util.Calendar;
import java.util.Vector;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TransaccionesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TransaccionesVO;
import com.sicap.clientes.vo.CuentaBancariaVO;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.cartera.DevengamientoVO;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class TransaccionesHelper {

    private static Logger myLogger = Logger.getLogger(TransaccionesHelper.class);
    
public static java.sql.Date obtenerFechaUltimoCierre(){
        Date fechaUltimoCierre=null;
        try {
            String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            fechaUltimoCierre = sdf.parse(ultimaFecha);
            
        } catch (ParseException ex) {
            myLogger.error("Error al parsear fecha ultimo cierre", ex);
        }
        return Convertidor.toSqlDate(fechaUltimoCierre);
    }    public static boolean registraDesembolso(CreditoCartVO credito, CicloGrupalVO ciclo, HttpServletRequest request, Vector notificaciones, double saldoFondeador) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        DecimalFormat num = new DecimalFormat("#######.00");
        Calendar calendario = Calendar.getInstance();
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        TransaccionesVO temp = new TransaccionesVO();
        FondeadorUtil fondeadorUtil = new FondeadorUtil();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        SaldoFondeadorDAO saldoFondDAO = new SaldoFondeadorDAO();
        CicloGrupalDAO cicloGruplaDAO = new CicloGrupalDAO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        SaldoIBSDAO saldoIbsDAO = new SaldoIBSDAO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        int numero_transaccion = 0;
        int i = 0;
        int respuesta = 0;
        double comision = 0;
        double ivacomision = 0;

        /*
         * Se calcula el el total a pagar de interes e iva por todo el proyecto
         */
        try {
            TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
            double pagos[] = null;
            double pagoInteresIva = 0;
            pagos = new double[tablaVO.length];
            for (int j = 0; j < tablaVO.length; j++) {
                pagos[j] = tablaVO[j].interes + tablaVO[j].ivaInteres;
                pagoInteresIva = pagoInteresIva + pagos[j];
            }

            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            // MONTO DEL CREDITO
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.numCredito = (credito.getNumCredito());
            temp.numCliente = (credito.getNumCliente());
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
            temp.fechaProceso = (credito.getFechaDesembolso());
            temp.fechaValor = (credito.getFechaDesembolso());
            temp.rubro = ClientesConstants.CAPITAL;
            temp.monto = credito.getMontoCredito();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);
            // MONTO DE DESEMBOLSADO
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.numCredito = (credito.getNumCredito());
            temp.numCliente = (credito.getNumCliente());
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
            temp.fechaProceso = (credito.getFechaDesembolso());
            temp.fechaValor = (credito.getFechaDesembolso());
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = credito.getMontoDesembolsado();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            //temp.origen = ciclo.bancoDispersion;//CAMBIO DE ORIGEN
            switch (ciclo.bancoDispersion) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
            }
            int cuentaOrigen = temp.origen;
            //

            respuesta = fondeadorUtil.actualizaSaldoFondeador(new SaldoFondeadorVO(temp.fondeador, temp.tipoTransaccion, 0, temp.monto, temp.numTransaccion, temp.numCliente, temp.numCredito, temp.origen));
            if (respuesta == 2) {
                temp.fondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
                respuesta = fondeadorUtil.actualizaSaldoFondeador(new SaldoFondeadorVO(temp.fondeador, temp.tipoTransaccion, 0, temp.monto, temp.numTransaccion, temp.numCliente, temp.numCredito, temp.origen));
                ciclo.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                ciclo.saldo.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                credito.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                saldoIbsDAO.updateOtrosDatos(null, ciclo.saldo.getIdClienteSICAP(), ciclo.saldo.getIdSolicitudSICAP(), ClientesConstants.ID_FONDEADOR_CREDITO_REAL, 0);
                creditoDAO.updateCreditoFondeador(credito);
                cicloGruplaDAO.updateCiclo(null, ciclo);
            }
            transaccionDAO.addTransaccion(temp);
            //CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + temp.fondeador, num.format(saldoFondeador));
            // MONTO DE COMISION
            if (credito.getMontoComision() > 0) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.COMISION;
                temp.monto = credito.getMontoComision();
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
                // IVA DE COMISION
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.IVA_COMISION;
                temp.monto = credito.getMontoIvaComision();
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
            }
            // INTERES AL FINAL
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.numCredito = (credito.getNumCredito());
            temp.numCliente = (credito.getNumCliente());
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
            temp.fechaProceso = (credito.getFechaDesembolso());
            temp.fechaValor = (credito.getFechaDesembolso());
            temp.rubro = ClientesConstants.INTERES;
            temp.monto = pagoInteresIva;
            temp.statusRubro = ClientesConstants.RUBRO_NO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
            
            // calculamos el monto solamente de aquellos integrantes que tengan seguro financiado Agosto 2017
            // warn: el ciclo.integrantes viene de session
            int integrantesLenght = ciclo.integrantes.length; // evitas un calculo en el for
            double montoTotalConSeguro = 0;
            double montoTotalSinSeguro = 0;
            for (int k = 0; k < integrantesLenght; k++) {
                // Totales sin seguro y con seguro, warn: el ciclo viene en request
                IntegranteCicloVO integrante = ciclo.integrantes[k];
                if (integrante.montoConSeguro > 0) {
                    montoTotalSinSeguro += integrante.monto;
                    montoTotalConSeguro += integrante.montoConSeguro;
                }
            }   
            // Recalculamos para obtener nada mas el costo del seguro
            montoTotalConSeguro = Math.abs(montoTotalConSeguro - montoTotalSinSeguro);
            // Se realizan las transacciones del costo con seguro
            if (montoTotalConSeguro > 0) {
                // SEGURO FINANCIADO SIN IVA
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.SEGURO_FINANCIADO;
                // Costo del seguro sin iva
                double montoSeguroSinIva = montoTotalConSeguro - SegurosUtil.obtenerIva(montoTotalConSeguro);
                temp.monto = montoSeguroSinIva; // Solamente de los integrantes que tengan seguro
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.origen = 0;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);

                // IVA SEGURO FINANCIADO
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.IVA_SEGURO_FINANCIADO;
                temp.monto = SegurosUtil.obtenerIva(montoTotalConSeguro); // SEGURO SIN EL IVA 110 ejmplo 15.17
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.origen = 0;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar la transaccion-- " + e.getMessage()));
            myLogger.error("Excepcion en registraDesembolso ", e);
        }
        return result;
    }

    public static boolean registraDesembolsoIndividual(CreditoCartVO credito, HttpServletRequest request, Vector notificaciones) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        int i = 0;
        Calendar calendario = Calendar.getInstance();
        /*
         * Se calcula el el total a pagar de interes e iva por todo el proyecto
         */
        try {

            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            // MONTO DEL CREDITO
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.numCredito = (credito.getNumCredito());
            temp.numCliente = (credito.getNumCliente());
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
            //temp.fechaProceso = (credito.getFechaDesembolso());
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = (credito.getFechaDesembolso());
            temp.rubro = ClientesConstants.CAPITAL;
            temp.monto = credito.getMontoCredito();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);
            // MONTO DE DESEMBOLSADO
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.numCredito = (credito.getNumCredito());
            temp.numCliente = (credito.getNumCliente());
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
            //temp.fechaProceso = (credito.getFechaDesembolso());
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = (credito.getFechaDesembolso());
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = credito.getMontoDesembolsado();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
            // MONTO DE COMISION
            if (credito.getMontoComision() > 0) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.COMISION;
                temp.monto = credito.getMontoComision();
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
                // IVA DE COMISION
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.IVA_COMISION;
                temp.monto = credito.getMontoIvaComision();
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar la transaccion-- " + e.getMessage()));
            myLogger.error("Excepcion en registraDesembolsoIndividual ", e);
        }

        return result;
    }

    public void registraPagoDev(CreditoCartVO credito, PagoVO pago) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        int i = 1;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = pago.fechaPago;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = "_IN";
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = pago.fechaPago;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = "_IN";
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraPago ", e);
        }

    }

// Se utiliza para el registro de pago
    public void registraPago(CreditoCartVO credito, PagoVO pago) throws Exception {
        //^o^
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        Calendar calendario = Calendar.getInstance();
        double ivacomision = 0;

        int numero_transaccion = 0;
        int i = 1;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);

        } catch (Exception e) {
            myLogger.error("Excepcion en registraPago ", e);
        }

    }  

    public static void registraPagoGarantia(CreditoCartVO credito, PagoVO pago, boolean regComision) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        CuentaBancariaVO cuentaVO = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDAO = new CuentasBancariasDAO();
        Calendar calendario = Calendar.getInstance();
        double ivacomision = 0;

        int numero_transaccion = 0;
        int i = 1;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO_GARANTIA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO_GARANTIA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraPago ", e);
        }

    }
        public static void registraPagoGarantiaIdentificada(CreditoCartVO credito, PagoVO pago, boolean regComision) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        CuentaBancariaVO cuentaVO = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDAO = new CuentasBancariasDAO();
        Calendar calendario = Calendar.getInstance();
        double ivacomision = 0;

        int numero_transaccion = 0;
        int i = 1;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.IDENTIFICACION_DE_GARANTIA ;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(calendario.getTime());
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO_GARANTIA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(calendario.getTime());
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraPago ", e);
        }

    }

    public void registraPagoCancelacionDes(CreditoCartVO credito, PagoVO pago) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        int i = 0;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CANCELACION_DESEMBOLSO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = pago.fechaPago;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CAPITAL;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = 0;
            temp.status = "_IN";
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CANCELACION_DESEMBOLSO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = pago.fechaPago;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = "_IN";
            temp.origen = pago.numCuenta;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean registraPago(CreditoCartVO credito, PagoVO pago, Connection con) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        int numero_transaccion = 0;
        double ivacomision = 0;
        int i = 1;
        boolean listo = true;
        try {

            numero_transaccion = transaccionDAO.getMaxTransaccion(con) + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            listo = transaccionDAO.addTransaccion(temp, con);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            if (listo) {
                listo = transaccionDAO.addTransaccion(temp, con);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraPago ", e);
            throw new Exception(e.getMessage());
        }
        return listo;
    }
    // Se utiliza para la aplicacion de pago

    public void aplicaPago(CreditoCartVO credito, RubrosVO[] rubros, Date fechaValor) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        Calendar calendario = Calendar.getInstance();
        try {
            
            java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.PAGO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = Convertidor.toSqlDate(fechaValor);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en aplicaPago ", e);
        }
    }

    public void aplicaPagoGarantia(CreditoCartVO credito, Date fechaValor, double monto) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        try {
            
            java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = 1;
            temp.tipoTransaccion = ClientesConstants.PAGO_GARANTIA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = Convertidor.toSqlDate(fechaValor);
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = 2;
            temp.tipoTransaccion = ClientesConstants.PAGO_GARANTIA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = Convertidor.toSqlDate(fechaValor);
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en aplicaPagoGarantia ", e);
        }
    }

    public void registraProvision(CreditoCartVO credito, RubrosVO[] rubros, Date fechaIni) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        try {
            java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.PROVISION;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = Convertidor.toSqlDate(fechaIni);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraProvision ", e);
        }
    }

    public void registraMora(CreditoCartVO credito, RubrosVO[] rubros, Date fechaFin) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        try {
            java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.MORATORIO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = Convertidor.toSqlDate(fechaFin);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraMora ", e);
        }
    }

    public void registraMulta(CreditoCartVO credito, RubrosVO[] rubros, Date fechaFin) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.MULTA;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaFin);
                temp.fechaValor = Convertidor.toSqlDate(fechaFin);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraMulta: ", e);

        }
    }

    public void registraCambioEstadoVen(CreditoCartVO credito, RubrosVO[] rubros, Date fechaIni) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.C_ESTADO_VENCIDO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaIni);
                temp.fechaValor = Convertidor.toSqlDate(fechaIni);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCambioEstadoVen: ", e);
        }
    }

    public void registraCondonacion(CreditoCartVO credito, RubrosVO[] rubros, Date fechaCond) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        String fechaCondonacion = Convertidor.dateToString(fechaCond);
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.CONDONACION;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaCond);
                temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCondonacion ", e);
        }
    }

    public void registraDevolucion(CreditoCartVO credito, RubrosVO rubros, Date fechaDev) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        String fechaCondonacion = Convertidor.dateToString(fechaDev);
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.tipoTransaccion = ClientesConstants.DEVOLUCION;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
            temp.rubro = rubros.tipoRubro;
            temp.monto = rubros.monto;
            temp.statusRubro = rubros.status;
            //temp.origen = credito.getBancoDesembolso();//CAMBIO DE ORIGEN
            switch (credito.getBancoDesembolso()) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
            }
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            if(credito.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                temp.fondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
            }else{
                temp.fondeador = credito.getFondeador();
            }
            numero_transaccion = transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDevolucion: ", e);
        }
    }

    public void registraDevolucionOdp(CreditoCartVO credito, RubrosVO rubros, Date fechaDev, IntegranteCicloVO integrante,boolean Adicional, boolean Interciclo) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        int i = 0;
        double saldoFondeador = 0.00;
        int respuesta = 0;
        String Devolucion = "";
        String fechaCondonacion = Convertidor.dateToString(fechaDev);
        DecimalFormat num = new DecimalFormat("#######.00");
        SaldoFondeadorVO saldoFondeadorVo = new SaldoFondeadorVO();
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;;
            Devolucion = TransaccionesContabHelper.tipoDevolucion(Adicional,Interciclo) ;
            temp.secuencial = i++;
            temp.numTransaccion = numero_transaccion;
            temp.tipoTransaccion = Devolucion;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(fechaDev);
            temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = rubros.monto;
            temp.statusRubro = rubros.status;
            FondeadorUtil fondeadorUtil = new FondeadorUtil();
            //temp.origen = credito.getBancoDesembolso();//CAMBIO DE ORIGEN
            switch (credito.getBancoDesembolso()) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
                    
            }
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            saldoFondeadorVo.numFondeador = temp.fondeador;
            saldoFondeadorVo.origen = temp.origen;
            saldoFondeadorVo.tipoMov = temp.tipoTransaccion;
            saldoFondeadorVo.monto = temp.monto;
            saldoFondeadorVo.numCliente = temp.numCliente;
            saldoFondeadorVo.numTransaccion = numero_transaccion;
            saldoFondeadorVo.numCredito = temp.numCredito;
            respuesta = fondeadorUtil.actualizaSaldoFondeador(saldoFondeadorVo);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = Devolucion;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(fechaDev);
            temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
            temp.rubro = ClientesConstants.CUENTA;
            if (null != integrante && integrante.montoConSeguro > 0){
                temp.monto = rubros.monto+integrante.costoSeguro;
            }else{
                temp.monto = rubros.monto;
            }
            temp.statusRubro = rubros.status;
            temp.origen = 0;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
            
            // Se realizan las transacciones del costo con seguro
            if (null != integrante && integrante.montoConSeguro > 0) {
                // SEGURO FINANCIADO SIN IVA
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.tipoTransaccion = Devolucion;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaDev);
                temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
                temp.rubro = ClientesConstants.SEGURO_FINANCIADO;
                // Costo del seguro sin iva
                double montoSinIva = integrante.costoSeguro - SegurosUtil.obtenerIva(integrante.costoSeguro);
                temp.monto = montoSinIva;
                temp.statusRubro = rubros.status;
                temp.origen = 0;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);

                // IVA SEGURO FINANCIADO
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i++;
                temp.tipoTransaccion = Devolucion;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaDev);
                temp.fechaValor = Convertidor.stringToSqlDate(fechaCondonacion);
                temp.rubro = ClientesConstants.IVA_SEGURO_FINANCIADO;
                temp.monto = SegurosUtil.obtenerIva(integrante.costoSeguro);
                temp.statusRubro = rubros.status;
                temp.origen = 0;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(temp);
            }

        } catch (Exception e) {
            myLogger.error("Excepcion en registraDevolucionOdp: ", e);
        }
    }

    public void registraDevolucionSaldoFavor(CreditoCartVO credito, OrdenDePagoVO oPago, Date fechaDev) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        int i = 1;
        String fechaDevolucion = Convertidor.dateToString(fechaDev);
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.DEVOLUCION_SALDO_FAVOR;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = Convertidor.stringToSqlDate(fechaDevolucion);
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = oPago.getMonto();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            //temp.origen = oPago.getIdBanco();//CAMBIO DE ORIGEN
            switch (oPago.getIdBanco()) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(oPago.getIdBanco(), CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(oPago.getIdBanco(), CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(oPago.getIdBanco(), CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(oPago.getIdBanco(), CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    temp.origen = new CuentasBancariasDAO().getNumCuentaBancaria(credito.getBancoDesembolso(), CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
            }
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.DEVOLUCION_SALDO_FAVOR;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = Convertidor.stringToSqlDate(fechaDevolucion);
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = oPago.getMonto();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);

        } catch (Exception e) {
            myLogger.error("Excepcion en registraDevolucionSaldoFavor: ", e);
        }
    }

    public void registraCastigo(CreditoCartVO credito, RubrosVO[] rubros, Date fechaCierreCastigo) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.CASTIGO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.fechaValor = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCastigo: ", e);
        }
    }

    //^O^
    public static void registraCancelacionPago(CreditoCartVO credito, PagoVO pago, double saldoFavor) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        int numero_transaccion = 0;
        int i = 1;
        double ivacomision = 0;
        Date fecha = new Date();
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {

            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_CAN_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            //temp.monto = pago.monto-saldoFavor;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_CAN_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            //temp.monto = pago.monto-saldoFavor;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCancelacionPago: ", e);
        }

    }

    public static void regCancelacionPagoNoIdentificado(CreditoCartVO credito, PagoVO pago) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        int i = 1;
        try {
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_CAN_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(calendario.getTime());
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.origen = pago.numCuenta;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = 1;
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.REGISTRO_CAN_PAGO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(calendario.getTime());
            temp.fechaValor = pago.fechaPago;
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = pago.monto;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 99;
            temp.fondeador = 1;
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCancelacionPago: ", e);
        }

    }

    public void cancelaPago(CreditoCartVO credito, RubrosVO[] rubros) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.PAGO_CANCELADO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = credito.getFechaUltimoPago();
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                numero_transaccion = transaccionDAO.addTransaccion(temp);
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en cancelaPago: ", e);
        }
    }

    public void revierteTransaccionesCiclo(int idEquipo, int idCredito) throws ClientesDBException {
        TransaccionesDAO transaccionesDao = new TransaccionesDAO();
        ArrayList<TransaccionesVO> transaccionesCua = new ArrayList<TransaccionesVO>();
        ArrayList<TransaccionesVO> transacciones = new ArrayList<TransaccionesVO>();
        TransaccionesVO transaccionContraria = new TransaccionesVO();
        Calendar calendario = Calendar.getInstance();
        FondeadorUtil fondeadorUtil = new FondeadorUtil();
        int numeroTransaccion = 0;
        int respuesta = 0;
        Double saldoFondeador;
        myLogger.debug("Variables inicializadas");
        DecimalFormat num = new DecimalFormat("#######.00");
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            transacciones = transaccionesDao.getTransacciones(idEquipo, idCredito);
//            transaccionesDao.eliminaTransaccionesCiclo(idEquipo, idCredito);
            myLogger.info("Transacciones ING actualizadas a DEL");
            myLogger.debug("Obteniendo transacciones CUA-ING");
            transaccionesCua = transaccionesDao.getTransaccionesCuaIng(idEquipo, idCredito);

            if (transaccionesCua.size() > 0) {
                numeroTransaccion = transaccionesDao.getMaxTransaccion() + 1;
                for (int i = 0; i < transaccionesCua.size(); i++) {
                    transaccionContraria.numTransaccion = numeroTransaccion;
                    transaccionContraria.secuencial = transaccionesCua.get(i).secuencial;
                    transaccionContraria.tipoTransaccion = ("CAN_" + transaccionesCua.get(i).tipoTransaccion);
                    transaccionContraria.numCliente = transaccionesCua.get(i).numCliente;
                    transaccionContraria.numCredito = transaccionesCua.get(i).numCredito;
                    transaccionContraria.numProducto = transaccionesCua.get(i).numProducto;
                    transaccionContraria.centroCostos = transaccionesCua.get(i).centroCostos;
                    transaccionContraria.rubro = transaccionesCua.get(i).rubro;
                    transaccionContraria.statusRubro = transaccionesCua.get(i).statusRubro;
                    transaccionContraria.monto = transaccionesCua.get(i).monto;
                    transaccionContraria.status = ClientesConstants.TRANSACCION_INGRESADA;
                    transaccionContraria.origen = transaccionesCua.get(i).origen;//CAMBIO DE ORIGEN
                    transaccionContraria.fechaProceso = fechaCierre;
                    transaccionContraria.fechaValor = transaccionesCua.get(i).fechaValor;
                    transaccionContraria.fondeador = transaccionesCua.get(i).fondeador;
                    if (transaccionContraria.tipoTransaccion.equals("CAN_" + ClientesConstants.DESEMBOLSO) && transaccionContraria.rubro.equals(ClientesConstants.EFECTIVO)) {
                        respuesta = fondeadorUtil.actualizaSaldoFondeador(new SaldoFondeadorVO(transaccionContraria.fondeador, "CAN_" + transaccionContraria.tipoTransaccion, 0, transaccionContraria.monto, transaccionContraria.numTransaccion, transaccionContraria.numCliente, transaccionContraria.numCredito, transaccionContraria.origen));
                        myLogger.debug("respuesta Saldo fondeador:" + respuesta);
                    }
                    myLogger.debug("Transacción contraria a insertar: " + transaccionContraria);
                    transaccionesDao.insertaTransaccionContraria(transaccionContraria);
                    myLogger.info("Transacción contraria insertada");
                    if (i + 1 < transaccionesCua.size() && (transaccionesCua.get(i).numTransaccion != transaccionesCua.get(i + 1).numTransaccion)) {
                        numeroTransaccion++;
                    }
                }
            } else {
                for (int i = 0; i < transacciones.size(); i++) {
                    if (transacciones.get(i).tipoTransaccion.equals(ClientesConstants.DESEMBOLSO) && transacciones.get(i).rubro.equals(ClientesConstants.EFECTIVO)) {
                        respuesta = fondeadorUtil.actualizaSaldoFondeador(new SaldoFondeadorVO(transacciones.get(i).fondeador, "CAN_" + transacciones.get(i).tipoTransaccion, 0, transacciones.get(i).monto, transacciones.get(i).numTransaccion, transacciones.get(i).numCliente, transacciones.get(i).numCredito, transacciones.get(i).origen));
                        myLogger.debug("respuesta Saldo fondeador:" + respuesta);
                    }
                }
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en revierteTransaccionesCiclo: ", e);
        }
    }

    public void registraDesembolsoInterciclo(Connection con, CreditoCartVO credito, RubrosVO[] rubros, Date fechaCierreCastigo, double saldoFondeador) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        try {
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO_ADICIONAL;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.fechaValor = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                if (rubros[i].tipoRubro.equals(ClientesConstants.EFECTIVO) || rubros[i].tipoRubro.equals(ClientesConstants.CAPITAL) || rubros[i].tipoRubro.equals(ClientesConstants.INTERES)) {
                    temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                } else {
                    temp.status = "_IN";
                }
                temp.fondeador = credito.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA?ClientesConstants.ID_FONDEADOR_CREDITO_REAL:credito.getFondeador();
                temp.origen = rubros[i].origen;
                numero_transaccion = transaccionDAO.addTransaccion(con, temp);
                if (i == 1) {
                    new SaldoFondeadorDAO().insertMovimiento(new SaldoFondeadorVO(temp.fondeador, temp.tipoTransaccion, saldoFondeador, temp.monto, numero_transaccion, temp.numCliente, temp.numCredito, temp.origen));
                }
            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }
        public void registraDesembolsoBursa(CreditoCartVO credito, Date fechaCierreCastigo, double Capital, double interes) throws Exception {
TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        int i = 1;
        try {
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CARTERA_INGRESO_BURSA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso =  Convertidor.toSqlDate(fechaCierreCastigo);
            temp.fechaValor =  Convertidor.toSqlDate(fechaCierreCastigo);
            temp.rubro = ClientesConstants.CAPITAL;
            temp.monto = Capital;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.origen = 0;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = 9;
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CARTERA_INGRESO_BURSA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(fechaCierreCastigo);
            temp.fechaValor = Convertidor.toSqlDate(fechaCierreCastigo);
            temp.rubro = ClientesConstants.INTERES;
            temp.monto = interes;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = 9;
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }
        
    public void registraDevolucionBursa(CreditoCartVO credito, Date fechaCierreCastigo, double Capital, double interes) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        int i = 1;
        try {
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CARTERA_SALIDA_BURSA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso =  Convertidor.toSqlDate(fechaCierreCastigo);
            temp.fechaValor =  Convertidor.toSqlDate(fechaCierreCastigo);
            temp.rubro = ClientesConstants.CAPITAL;
            temp.monto = Capital;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.origen = 0;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = 9;
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.CARTERA_SALIDA_BURSA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = 3;
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = Convertidor.toSqlDate(fechaCierreCastigo);
            temp.fechaValor = Convertidor.toSqlDate(fechaCierreCastigo);
            temp.rubro = ClientesConstants.INTERES;
            temp.monto = interes;
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = 9;
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }
    /**
     * Registra las mismas tx definidas para registraDesembolsoInterciclo y se agregan para interciclo
     * las transacciones de seguros.
     * 
     * registraDesembolsoInterciclo sin el parametro integrantes se usa para grupo adicional, debido 
     * a eso se creo una copia del metodo agregando las tx de monto con seguro financiado.
     * 
     * @since Agosto 2017
     * @version 1.0v
     * 
     * @param con conexion
     * @param credito credito
     * @param rubros rubros
     * @param fechaCierreCastigo fecha cierre castigo
     * @param saldoFondeador saldo fondeador
     * @param cicloVO ciclo con los integrantes
     * @throws Exception 
     */
    public void registraDesembolsoInterciclo(Connection con, CreditoCartVO credito, RubrosVO[] rubros, Date fechaCierreCastigo, double saldoFondeador, CicloGrupalVO cicloVO, int SemDisp) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        try {
            int iSecuencia = 0;
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                iSecuencia = iSecuencia + 1; // i + 1
                temp.secuencial = iSecuencia;
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO_INTERCICLO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.fechaValor = Convertidor.toSqlDate(fechaCierreCastigo);
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                if (rubros[i].tipoRubro.equals(ClientesConstants.EFECTIVO) || rubros[i].tipoRubro.equals(ClientesConstants.CAPITAL) || rubros[i].tipoRubro.equals(ClientesConstants.INTERES)) {
                    temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                } else {
                    temp.status = "_IN";
                }
                temp.fondeador = credito.getFondeador();
                temp.fondeador = credito.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA?ClientesConstants.ID_FONDEADOR_CREDITO_REAL:credito.getFondeador();
                temp.origen = rubros[i].origen;
                
                numero_transaccion = transaccionDAO.addTransaccion(con, temp);
                if (i == 1) {
                    new SaldoFondeadorDAO().insertMovimiento(new SaldoFondeadorVO(temp.fondeador, temp.tipoTransaccion, saldoFondeador, temp.monto, numero_transaccion, temp.numCliente, temp.numCredito, temp.origen));
                }
            }
            // calculamos el monto solamente de aquellos integrantes que tengan seguro financiado Agosto 2017
            // warn: el ciclo.integrantes viene de session
            int integrantesLenght = cicloVO.integrantes.length; // evitas un calculo en el for
            double montoTotalConSeguro = 0;
            double montoTotalSinSeguro = 0;
            for (int k = 0; k < integrantesLenght; k++) {
                // Totales sin seguro y con seguro, warn: el ciclo viene en request
                IntegranteCicloVO integrante = cicloVO.integrantes[k];
                if ((integrante.tipo== ClientesConstants.TIPO_CLIENTE_INTERCICLO && SemDisp == 2)||(integrante.tipo== ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 && SemDisp == 4) )
                if (integrante.montoConSeguro > 0) {
                    montoTotalSinSeguro += integrante.monto;
                    montoTotalConSeguro += integrante.montoConSeguro;
                }
            }   
            // Recalculamos para obtener nada mas el costo del seguro
            montoTotalConSeguro = montoTotalConSeguro - montoTotalSinSeguro;
            // Se realizan las transacciones del costo con seguro
            if (montoTotalConSeguro > 0) {
                // SEGURO FINANCIADO SIN IVA
                iSecuencia = iSecuencia + 1;
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = iSecuencia;
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO_INTERCICLO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.SEGURO_FINANCIADO;
                // Costo del seguro sin iva
                double montoSeguroSinIva = montoTotalConSeguro - SegurosUtil.obtenerIva(montoTotalConSeguro);
                temp.monto = montoSeguroSinIva; // Solamente de los integrantes que tengan seguro
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.origen = 0;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(con,temp);

                // IVA SEGURO FINANCIADO
                temp.numTransaccion = numero_transaccion;
                iSecuencia = iSecuencia + 1;
                temp.secuencial = iSecuencia; // checar secuencia
                temp.numCredito = (credito.getNumCredito());
                temp.numCliente = (credito.getNumCliente());
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.tipoTransaccion = ClientesConstants.DESEMBOLSO_INTERCICLO;
                //temp.fechaProceso = (credito.getFechaDesembolso());
                temp.fechaProceso = obtenerFechaUltimoCierre();
                temp.fechaValor = (credito.getFechaDesembolso());
                temp.rubro = ClientesConstants.IVA_SEGURO_FINANCIADO;
                temp.monto = SegurosUtil.obtenerIva(montoTotalConSeguro); // SEGURO SIN EL IVA 110 ejmplo 15.17
                temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.origen = 0;
                temp.fondeador = credito.getFondeador();
                transaccionDAO.addTransaccion(con,temp);
            }            
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }


    public void registraCancelacionProvision(CreditoCartVO credito, DevengamientoVO deven) throws Exception {
        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();
        int numero_transaccion = 0;
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = 1;
            temp.tipoTransaccion = ClientesConstants.PROVISION_CANCELADO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = deven.getFechaDevengamiento();
            temp.rubro = ClientesConstants.INTERES;
            temp.monto = deven.getInteres();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = 2;
            temp.tipoTransaccion = ClientesConstants.PROVISION_CANCELADO;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = fechaCierre;
            temp.fechaValor = deven.getFechaDevengamiento();
            temp.rubro = ClientesConstants.IVA_INTERES;
            temp.monto = deven.getIvaInteres();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraCancelacionProvision ", e);
        }
    }

    public void registraSalidaFondeador(Connection con, CreditoCartVO credito, RubrosVO[] rubros, double capitalFondeador) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        FondeadorUtil fondeadorUtil = new FondeadorUtil();
        int numero_transaccion = 0;
        int respuesta = 0;
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.CAMBIO_FONDEADOR_SALIDA;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = Convertidor.toSqlDate(new Date());
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                temp.origen = rubros[i].origen;
                numero_transaccion = transaccionDAO.addTransaccion(con, temp);
            }
            respuesta = fondeadorUtil.actualizaSaldoFondeador(new SaldoFondeadorVO(temp.fondeador, temp.tipoTransaccion, 0, capitalFondeador, numero_transaccion, temp.numCliente, temp.numCredito, temp.origen));
            myLogger.debug("respuesta Saldo fondeador:" + respuesta);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }

    public void registraEntradaFondeador(Connection con, CreditoCartVO credito, RubrosVO[] rubros) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        java.sql.Date fechaCierre = obtenerFechaUltimoCierre();
        try {
            for (int i = 0; i < rubros.length; i++) {
                temp.numTransaccion = numero_transaccion;
                temp.secuencial = i + 1;
                temp.tipoTransaccion = ClientesConstants.CAMBIO_FONDEADOR_INGRESO;
                temp.numCliente = credito.getNumCliente();
                temp.numCredito = credito.getNumCredito();
                temp.numProducto = credito.getNumProducto();
                temp.centroCostos = credito.getNumSucursal();
                temp.fechaProceso = fechaCierre;
                temp.fechaValor = Convertidor.toSqlDate(new Date());
                temp.rubro = rubros[i].tipoRubro;
                temp.monto = rubros[i].monto;
                temp.statusRubro = rubros[i].status;
                temp.status = ClientesConstants.TRANSACCION_INGRESADA;
                temp.fondeador = credito.getFondeador();
                temp.origen = rubros[i].origen;
                numero_transaccion = transaccionDAO.addTransaccion(con, temp);

            }
        } catch (Exception e) {
            myLogger.error("Excepcion en registraDesembolsoInterciclo: ", e);
        }
    }

    /**
     * Metodo para registrar transaciones de salida/entrada de cartera De inicio
     * para Bursa
     *
     * @param con
     * @param credito
     * @param rubros
     * @param tipoTrans
     * @param cerrarCon
     * @throws Exception
     */
    public void registraPerfilCarteraFondeador(Connection con, CreditoCartVO credito, RubrosVO[] rubros, String tipoTrans, boolean cerrarCon, int idFondeadorTrans, java.sql.Date fechaUltimoCierre) throws Exception {

        TransaccionesVO tempTrans = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        int numero_transaccion = 0;
        try {
            for (int i = 0; i < rubros.length; i++) {
                tempTrans.numTransaccion = numero_transaccion;
                tempTrans.secuencial = i + 1;
                tempTrans.tipoTransaccion = tipoTrans;
                tempTrans.numCliente = credito.getNumCliente();
                tempTrans.numCredito = credito.getNumCredito();
                tempTrans.numProducto = credito.getNumProducto();
                tempTrans.centroCostos = credito.getNumSucursal();
                tempTrans.fechaProceso = fechaUltimoCierre;
                tempTrans.fechaValor = Convertidor.toSqlDate(new Date());
                tempTrans.rubro = rubros[i].tipoRubro;
                tempTrans.monto = rubros[i].monto;
                tempTrans.statusRubro = rubros[i].status;
                tempTrans.status = ClientesConstants.TRANSACCION_INGRESADA;
                tempTrans.fondeador = idFondeadorTrans;//credito.getFondeador();
                tempTrans.origen = rubros[i].origen;
                numero_transaccion = transaccionDAO.addTransaccion(con, tempTrans, cerrarCon);//TODO modificar o crear nuevo metodo para que no cieere ahi la conexion

}
        } catch (Exception e) {
            myLogger.error("Excepcion en registraPerfilCarteraFondeador: ", e);
        }
    }

    public static void registraSaldoAFavorIxaya(CreditoCartVO credito) throws Exception {

        TransaccionesVO temp = new TransaccionesVO();
        TransaccionesDAO transaccionDAO = new TransaccionesDAO();
        Calendar calendario = Calendar.getInstance();

        int numero_transaccion = 0;
        int i = 1;
        try {
            //numero_transaccion = transaccionDAO.getMaxTransaccion() + 1;
            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.SALDO_A_FAVOR_IXAYA;//Etiqueta nueva
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = Convertidor.toSqlDate(calendario.getTime());
            temp.rubro = ClientesConstants.EFECTIVO;
            temp.monto = credito.getMontoCuenta();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            //temp.origen = pago.bancoReferencia;//CAMBIO DE ORIGEN
            temp.origen = 0;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.fondeador = credito.getFondeador();
            numero_transaccion = transaccionDAO.addTransaccion(temp);

            temp.numTransaccion = numero_transaccion;
            temp.secuencial = i++;
            temp.tipoTransaccion = ClientesConstants.SALDO_A_FAVOR_IXAYA;
            temp.numCliente = credito.getNumCliente();
            temp.numCredito = credito.getNumCredito();
            temp.numProducto = credito.getNumProducto();
            temp.centroCostos = credito.getNumSucursal();
            temp.fechaProceso = obtenerFechaUltimoCierre();
            temp.fechaValor = Convertidor.toSqlDate(calendario.getTime());
            temp.rubro = ClientesConstants.CUENTA;
            temp.monto = credito.getMontoCuenta();
            temp.statusRubro = ClientesConstants.RUBRO_VIGENTE;
            temp.status = ClientesConstants.TRANSACCION_INGRESADA;
            temp.origen = 0;
            temp.fondeador = credito.getFondeador();
            transaccionDAO.addTransaccion(temp);
        } catch (Exception e) {
            myLogger.error("Excepcion en registraSaldoAFavorIxaya ", e);
        }

    }

}

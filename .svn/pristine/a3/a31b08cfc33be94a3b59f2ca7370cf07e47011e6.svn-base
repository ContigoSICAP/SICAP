package com.sicap.clientes.helpers.cartera;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.DevengamientoDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.dao.cartera.TransaccionesDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.MailUtil;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CuentaBancariaVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DevengamientoVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TransaccionesVO;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class TablaAmortHelper {

    private static Logger myLogger = Logger.getLogger(TablaAmortHelper.class);

    public static TablaAmortVO[] insertTablaInsolutoConsumo(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;

        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementosTabla[] = null;

        try {

            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();
            Date fechaTemp = calcFechaInicio(Convertidor.stringToDate(fechaIni));
            Date fechaQuincena1 = fechaTemp;
            Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            GregorianCalendar fechaOut = new GregorianCalendar();

            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;
            /*double ajusteFinal = 0.00;
             double pagoAjustado = 0.00;
             double ajusteUltima = 0.00;*/

            try {
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(Convertidor.stringToDate(fechaIni));
                temp.saldoInicial = montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                comision = montoConComision - montoSinComision;
                comisionSinIva = Convertidor.getMontoIva(comision, cliente.idSucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comision = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                array.add(temp);
                tabladao.addTablaAmort(temp);
            } catch (Exception e) {
                myLogger.error("Error en insert a Tabla de amortizacion Saldos Insolutos Consumo", e);
            }

            Integer elementos[] = null;
            elementos = getDaysBetweenPayments(plazo, frecuenciaPago, fechaIni);

            if (frecuenciaPago == 1) {
                plazo = plazo * 2;
            }

            int difDias = 0;

            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaTemp = calcFechaInicio(Convertidor.stringToDate(fechaIni));
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaTemp = calcFechaInicioMensual(Convertidor.stringToDate(fechaIni));
            }

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            tasaInteres = FormatUtil.roundDecimal(tasaAnual / 100 / 360, 10);

            if (Convertidor.esFronterizo(solicitud.idSucursal)) {
                tasaInteres = tasaInteres * (1.16 / 1.11);
            }

            for (int i = 0; i < plazo; i++) {

                if (i == plazo - 1) {
                    abonoCapital = sdoInsoluto;
                    difDias = elementos[i];
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    sdoInsoluto = 0;
                    interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                    ivaInteres = abonoInteres - interes;
                    /*ajusteUltima = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     ajusteUltima = ajusteUltima -(abonoCapital+abonoInteres);*/
                }

                if (i != plazo - 1) {
                    difDias = elementos[i];
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    if (abonoCapital < 0) {
                        abonoCapital = 0.00;
                    }
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                    ivaInteres = abonoInteres - interes;

                    /*if ( i==0 ){
                     double montoPagar = abonoCapital+abonoInteres;
                     pagoAjustado = java.lang.Math.ceil(montoPagar);
                     ajusteFinal = (pagoAjustado-montoPagar)*(plazo-1);
                     }*/
                }

                if (i != plazo) {
                    temp = new TablaAmortVO();
                    temp.numCliente = cliente.idCliente;
                    temp.numCredito = solicitud.idCreditoIBS;
                    temp.numPago = i + 1;

                    if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                        if (i == 0) {
                            fechaTemp = fechaQuincena1;
                        }
                        if (i == 1) {
                            fechaTemp = fechaQuincena2;
                        }
                        if (i > 1) {
                            if ((i % 2) == 0) {
                                fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                                fechaQuincena1 = fechaTemp;
                            } else {
                                fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                                fechaQuincena2 = fechaTemp;
                            }
                        }
                    }

                    if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL && i != 0) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }

                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comision = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);
                    /*if(i!=plazo-1){
                     temp.abonoCapital = abonoCapital;
                     temp.montoPagar = pagoAjustado;
                     }
                     else{
                     temp.abonoCapital = abonoCapital+ajusteUltima;
                     temp.montoPagar = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     }*/
                    tabladao.addTablaAmort(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("Error en insert a Tabla de amortizacion Saldos Insolutos Consumo", e);
        }

        return elementosTabla;

    }

    public static TablaAmortVO[] insertTablaInsolutoMicro(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {

        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;

        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementosTabla[] = null;

        try {

            Date fechaTemp = new Date();
            Date fechaInicio = Convertidor.stringToDate(fechaIni);

            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 15, 1);
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 1, 0);
            } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 7, 1);
            }

            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();

            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
                temp.saldoInicial = montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                comision = montoConComision - montoSinComision;
                comisionSinIva = Convertidor.getMontoIva(comision, cliente.idSucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comision = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.status = 1;
                temp.pagado = "S";
                array.add(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaInsolutoMicro", e);

            }

            Integer elementos[] = null;
            elementos = getDaysBetweenPaymentsMicro(plazo, frecuenciaPago, fechaIni);

            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                plazo = plazo * 2;
            } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                plazo = plazo * 4;
            }

            int difDias = 0;
            tasaInteres = FormatUtil.roundDouble(tasaAnual / 100 / 360, 10);

            for (int i = 0; i < plazo; i++) {
                if (i == 0) {
                    difDias = elementos[i];
                    abonoInteres = montoConComision * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO) {
                        interes = abonoInteres;
                        ivaInteres = 0;
                    } else {
                        interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                        ivaInteres = abonoInteres - interes;
                    }
                }

                if (i == plazo - 1) {
                    abonoCapital = sdoInsoluto;
                    difDias = elementos[i];
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    sdoInsoluto = 0;
                    if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO) {
                        interes = abonoInteres;
                        ivaInteres = 0;
                    } else {
                        interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                        ivaInteres = abonoInteres - interes;
                    }
                }

                if (i != 0 && i != plazo - 1) {
                    difDias = elementos[i];
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO) {
                        interes = abonoInteres;
                        ivaInteres = 0;
                    } else {
                        interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                        ivaInteres = abonoInteres - interes;
                    }
                }

                if (i != plazo) {
                    temp = new TablaAmortVO();
                    temp.numCliente = cliente.idCliente;
                    temp.numCredito = solicitud.idCreditoIBS;
                    temp.numPago = i + 1;

                    if (temp.numPago == 1) {
                        temp.status = 1;
                    } else {
                        temp.status = 0;
                    }

                    if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL && i != 0) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 15, 1);
                    }

                    if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL && i != 0) {
                        fechaTemp = FechasUtil.getDate(fechaInicio, i + 1, 0);
                    }
                    if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL && i != 0) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 7, 1);
                    }

                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comision = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);
                    temp.pagado = "N";

                    tabladao.addTablaAmort(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("insertTablaInsolutoMicro", e);
        }

        return elementosTabla;

    }

    public static TablaAmortVO[] insertTablaInsolutoComunal(GrupoVO grupo, CicloGrupalVO ciclo, double pagoUnitario, int frecuenciaPago, String fechaIni, double tasaAnual) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = ciclo.montoConComision;
        /*double ajusteFinal = 0.00;
         double pagoAjustado = 0.00;
         double ajusteUltima = 0.00;*/
        int plazo = 0;
        int difDias = 0;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            if (ciclo.plazo == 0) {
                plazo = 16;
            } else {
                plazo = ciclo.plazo;
            }
            difDias = 7;
        } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
            plazo = 8;
            difDias = 14;
        }

        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementosTabla[] = null;

        try {

            Date fechaTemp = Convertidor.stringToDate(fechaIni);
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();

            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                myLogger.info("tasa interes " + tasaAnual);
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(Convertidor.stringToDate(fechaIni));
                temp.saldoInicial = ciclo.montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                comision = ciclo.montoConComision - ciclo.monto - ciclo.montoRefinanciado;
                comisionSinIva = Convertidor.getMontoIva(comision, grupo.sucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comision = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.numCliente = grupo.idGrupo;
                temp.numCredito = ciclo.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.pagado = "N";

//				array.add(temp);
//				tabladao.addTablaAmort(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaInsolutoComunal", e);
            }

            tasaInteres = FormatUtil.roundDouble(tasaAnual / 100 / 360, 10);

            /*if ( Convertidor.esFronterizo(grupo.sucursal) )
             tasaInteres = tasaInteres*(1.15/1.10);*/
            for (int i = 0; i < plazo; i++) {

                if (i == plazo - 1) {
                    abonoCapital = sdoInsoluto;
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    sdoInsoluto = 0;
                    interes = Convertidor.getMontoIva(abonoInteres, grupo.sucursal, 1);
                    ivaInteres = abonoInteres - interes;
                    /*ajusteUltima = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     ajusteUltima = ajusteUltima -(abonoCapital+abonoInteres);*/
                }

                if (i != plazo - 1) {
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    interes = Convertidor.getMontoIva(abonoInteres, grupo.sucursal, 1);
                    ivaInteres = abonoInteres - interes;
                    /*if (i==0){
                     double montoPagar = abonoCapital+abonoInteres;
                     pagoAjustado = java.lang.Math.ceil(montoPagar);
                     ajusteFinal = (pagoAjustado-montoPagar)*(plazo-1);
                     }*/
                }

                if (i != plazo) {
                    temp = new TablaAmortVO();
                    if (i == 0) {
                        temp.status = 1;
                    } else {
                        temp.status = 0;
                    }
                    temp.numCliente = grupo.idGrupo;
                    temp.numCredito = ciclo.idCreditoIBS;
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                    temp.numPago = i + 1;
                    fechaTemp = FechasUtil.getDate(fechaTemp, difDias, 1);
                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comision = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);
                    temp.pagado = "N";


                    /*if(i!=plazo-1){
                     temp.abonoCapital = abonoCapital;
                     temp.montoPagar = pagoAjustado;
                     }
                     else{
                     temp.abonoCapital = abonoCapital+ajusteUltima;
                     temp.montoPagar = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     }*/
                    tabladao.addTablaAmort(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("insertTablaInsolutoComunal", e);
        }

        return elementosTabla;

    }

    public static TablaAmortVO[] insertTablaInsolutoIndivSemanal(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;
        /*double ajusteFinal = 0.00;
         double pagoAjustado = 0.00;
         double ajusteUltima = 0.00;*/
        int difDias = 0;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            if (plazo == 0) {
                plazo = 16;
            } else {
                plazo = plazo;
            }
            difDias = 7;
        } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
            plazo = 8;
            difDias = 14;
        }

        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementosTabla[] = null;

        try {

            Date fechaTemp = Convertidor.stringToDate(fechaIni);
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();

            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(Convertidor.stringToDate(fechaIni));
                temp.saldoInicial = montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                comision = montoConComision - montoSinComision;
                comisionSinIva = Convertidor.getMontoIva(comision, cliente.idSucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comision = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.numCliente = cliente.idCliente;
                temp.numCliente = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.pagado = "N";
                array.add(temp);
                tabladao.addTablaAmort(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaInsolutoIndivSemanal", e);
            }

            tasaInteres = FormatUtil.roundDouble(tasaAnual / 100 / 360, 10);

            /*if ( Convertidor.esFronterizo(grupo.sucursal) )
             tasaInteres = tasaInteres*(1.15/1.10);*/
            for (int i = 0; i < plazo; i++) {

                if (i == plazo - 1) {
                    abonoCapital = sdoInsoluto;
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    sdoInsoluto = 0;
                    interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                    ivaInteres = abonoInteres - interes;
                    /*ajusteUltima = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     ajusteUltima = ajusteUltima -(abonoCapital+abonoInteres);*/
                }

                if (i != plazo - 1) {
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    interes = Convertidor.getMontoIva(abonoInteres, cliente.idSucursal, 1);
                    ivaInteres = abonoInteres - interes;
                    /*if (i==0){
                     double montoPagar = abonoCapital+abonoInteres;
                     pagoAjustado = java.lang.Math.ceil(montoPagar);
                     ajusteFinal = (pagoAjustado-montoPagar)*(plazo-1);
                     }*/
                }

                if (i != plazo) {
                    temp = new TablaAmortVO();
                    if (i == 0) {
                        temp.status = 1;
                    } else {
                        temp.status = 0;
                    }
                    temp.numCliente = cliente.idCliente;
                    temp.numCredito = solicitud.idCreditoIBS;
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                    temp.numPago = i + 1;
                    fechaTemp = FechasUtil.getDate(fechaTemp, difDias, 1);
                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comision = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);
                    temp.pagado = "N";

                    /*if(i!=plazo-1){
                     temp.abonoCapital = abonoCapital;
                     temp.montoPagar = pagoAjustado;
                     }
                     else{
                     temp.abonoCapital = abonoCapital+ajusteUltima;
                     temp.montoPagar = java.lang.Math.ceil(abonoCapital+abonoInteres);
                     }*/
                    tabladao.addTablaAmort(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("insertTablaInsolutoIndivSemanal", e);
        }

        return elementosTabla;

    }

    //Algoritmo para generar tabla de amortizaci�n con c�lculo de inter�s Global para productos(Consumo y CrediHogar)
    public static void insertTablaConsumo(ClienteVO cliente, SolicitudVO solicitud, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;
        myLogger.info("tasa " + tasa);
        myLogger.info("monto " + monto);

        Date fechaTemp = calcFechaInicio(fechaInicio);
        Date fechaQuincena1 = fechaTemp;
        Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
        GregorianCalendar fechaOut = new GregorianCalendar();
        SucursalDAO sucDAO = new SucursalDAO();
        SucursalVO sucVO = sucDAO.getSucursal(cliente.idSucursal);

        try {
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;
            /*	No se inserta el primer registro, no hace falta		
             try{
             temp.numPago = 0;
             temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
             temp.saldoInicial = monto;
             temp.abonoCapital = 0;
             temp.saldoCapital = 0;
             temp.comision = comision;
             temp.ivaComision = ivaComision;
             temp.interes = 0;
             temp.ivaInteres = 0;
             temp.montoPagar = comision;
             temp.numCliente = cliente.idCliente;
             temp.numCredito = solicitud.idCreditoIBS;
             tabladao.addTablaAmort(temp);
             } catch(Exception e){
             System.err.print("Calculo Tabla Consumo Global" + e);
             }
             */
            Integer elementos[] = null;
            elementos = getDaysBetweenPayments(plazo, frecuenciaPago, Convertidor.dateToString(fechaInicio));
            int dias = 0;
            for (int i = 0; i < elementos.length; i++) {
                dias += elementos[i];
            }

            if (frecuenciaPago == 1) {
                plazo = plazo * 2;
            }

            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO));
            }
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            if (frecuenciaPago == 1) {
                fechaTemp = calcFechaInicio(fechaInicio);
            } else if (frecuenciaPago == 2) {
                fechaTemp = calcFechaInicioMensual(fechaInicio);
            }

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            for (int i = 0; i < plazo; i++) {
                temp = new TablaAmortVO();
                if (i == 0) {
                    temp.status = 1;
                } else {
                    temp.status = 0;
                }
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i + 1;
                if (frecuenciaPago == 1) {
                    if (i == 0) {
                        fechaTemp = fechaQuincena1;
                    }
                    if (i == 1) {
                        fechaTemp = fechaQuincena2;
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                        }
                    }
                } else if (frecuenciaPago == 2) {
                    if (i >= 1) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }
                }

                temp.fechaPago = Convertidor.toSqlDate(FechasUtil.getBusinessDate(fechaTemp));
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo - 1) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comision = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = montoApagar;
                tabladao.addTablaAmort(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaConsumo", e);
            throw new ClientesException(e.getMessage());
        }

    }

    public static void insertTablaDescuentoNomina(ClienteVO cliente, SolicitudVO solicitud, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        myLogger.info("monto " + monto + "monto sin com " + montoSinComision + "plazo " + plazo);
        if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL || frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            insertTablaDescuentoNominaQuin(cliente, solicitud, monto, montoSinComision, plazo, frecuenciaPago, tasa, fechaInicio);
        } else {
            insertTablaDescuentoNominaSem(cliente, solicitud, monto, montoSinComision, plazo, frecuenciaPago, tasa, fechaInicio);
        }
    }

    //Algoritmo para generar tabla de amortizaci�n con c�lculo de inter�s Global para productos Descuento Nomina
    public static void insertTablaDescuentoNominaQuin(ClienteVO cliente, SolicitudVO solicitud, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;

        Date fechaTemp = calcFechaArranqueDNQuincenal(fechaInicio);
        Date fechaInicial = fechaTemp;
        Date fechaQuincena1 = FechasUtil.getDate(fechaTemp, 15, 1);
        Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
        GregorianCalendar fechaOut = new GregorianCalendar();
        SucursalDAO sucDAO = new SucursalDAO();
        SucursalVO sucVO = sucDAO.getSucursal(cliente.idSucursal);

        try {
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;

            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicial);
                temp.saldoInicial = monto;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                temp.comision = comision;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision + ivaComision;
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
//				tabladao.addTablaAmort(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaDescuentoNominaQuin", e);
            }

            Integer elementos[] = null;
            elementos = getDaysBetweenPaymentsDN(plazo, frecuenciaPago, Convertidor.dateToString(fechaInicial));
            int dias = 0;
            for (int i = 0; i < elementos.length; i++) {
                dias += elementos[i];
            }

            if (frecuenciaPago == 1) {
                plazo = plazo;
            }

            myLogger.info("dias " + dias);
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            }
            abonoCapital = monto / plazo;
            myLogger.info("tasa " + tasa);
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            if (frecuenciaPago == 1) {
                fechaTemp = calcFechaInicioDNQuincenal(fechaInicio);
            } else if (frecuenciaPago == 2) {
                fechaTemp = calcFechaInicioMensual(fechaInicio);
            }

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            for (int i = 0; i < plazo; i++) {
                temp = new TablaAmortVO();
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i + 1;
                if (frecuenciaPago == 1) {
                    if (i == 0) {
                        fechaTemp = fechaQuincena1;
                    }
                    if (i == 1) {
                        fechaTemp = fechaQuincena2;
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                        }
                    }
                } else if (frecuenciaPago == 2) {
                    if (i >= 1) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }
                }

                temp.fechaPago = Convertidor.toSqlDate(FechasUtil.getBusinessDate(fechaTemp));
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo - 1) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comision = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = montoApagar;
                tabladao.addTablaAmort(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaDescuentoNominaQuin", e);
            throw new ClientesException(e.getMessage());
        }

    }

    public static void insertTablaDescuentoNominaSem(ClienteVO cliente, SolicitudVO solicitud, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;
        int dias = 0;
        Date fechaArranque = null;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            dias = plazo * 7;
        } else {
            dias = plazo * 14;
        }

        Date fechaTemp = null;

        try {
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucVO = sucDAO.getSucursal(cliente.idSucursal);
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;
// Tiene una dia se la semana fija de pago	    		    	
            fechaArranque = FechasUtil.getNextWeekDate(fechaInicio, 3);
            fechaTemp = fechaArranque;
            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaArranque);
                temp.saldoInicial = montoSinComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                temp.comision = comision;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision + ivaComision;
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
//				tabladao.addTablaAmort(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaDescuentoNominaSem", e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            } else {
                tasa = tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO);
            }
            monto = montoSinComision;
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            interes = interes - ivaInteres;
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            for (int i = 1; i <= plazo; i++) {
                temp = new TablaAmortVO();
                temp.numCliente = cliente.idCliente;
                temp.numCredito = solicitud.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i;
                if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                    fechaTemp = FechasUtil.getDate(fechaTemp, 7, 1);
                } else {
                    fechaTemp = FechasUtil.getDate(fechaTemp, 14, 1);
                }
                temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comision = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = abonoCapital + interes + ivaInteres;
                tabladao.addTablaAmort(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaDescuentoNominaSem", e);
        }

    }

    public static void actualizaTablaAmortizacion(ClienteVO cliente, int idSolicitud) throws ClientesException {
        actualizaTablaAmortizacion(cliente, idSolicitud, new Date());
    }

    public static void actualizaTablaAmortizacion(ClienteVO cliente, int idSolicitud, Date fecha) throws ClientesException {
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        switch (cliente.solicitudes[indiceSolicitud].tipoOperacion) {
            case 1:
                if (fecha == null) {
                    actualizaTablaConsumo(cliente, idSolicitud);
                } else {
                    actualizaTablaConsumo(cliente, idSolicitud, fecha);
                }
                break;
            case 4:
                if (fecha == null) {
                    actualizaTablaVivienda(cliente, idSolicitud);
                } else {
                    actualizaTablaVivienda(cliente, idSolicitud, fecha);
                }
                break;
            case 7:
                if (fecha == null) {
                    actualizaTablaConsumo(cliente, idSolicitud);
                } else {
                    actualizaTablaConsumo(cliente, idSolicitud, fecha);
                }
                break;
            case 21:
                if (fecha == null) {
                    actualizaTablaConsumo(cliente, idSolicitud);
                } else {
                    actualizaTablaConsumo(cliente, idSolicitud, fecha);
                }
                break;
        }
    }

    private static void actualizaTablaVivienda(ClienteVO cliente, int idSolicitud) throws ClientesException {
        actualizaTablaVivienda(cliente, idSolicitud, new Date());
    }

    private static void actualizaTablaConsumo(ClienteVO cliente, int idSolicitud) throws ClientesException {
        actualizaTablaConsumo(cliente, idSolicitud, new Date());
    }

    private static void actualizaTablaVivienda(ClienteVO cliente, int idSolicitud, Date time) throws ClientesException {

        //ejecuta para Tabla de Vivienda
        double prima = 0;
        double primaConComision = 0;
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        //Borra la anterior//
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        tablaDAO.delTablaAmortizacion(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
        //Incio Crear nueva//
        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(cliente.solicitudes[indiceSolicitud].tipoOperacion);
        TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(cliente.solicitudes[indiceSolicitud].tipoOperacion);
        double montoSinComision = ClientesUtil.calculaMontoSinComision(cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.comision, catComisiones);
        if (cliente.solicitudes[indiceSolicitud].seguro != null && cliente.solicitudes[indiceSolicitud].seguro.primaTotal != 0) {
            prima = cliente.solicitudes[indiceSolicitud].seguro.primaTotal;
            primaConComision = ClientesUtil.calculaMontoConComision(prima, cliente.solicitudes[indiceSolicitud].decisionComite.comision, catComisiones);
        }
        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(cliente.solicitudes[indiceSolicitud].decisionComite.tasa);
        insertTablaVivienda(cliente.idCliente, idSolicitud, cliente.idSucursal, cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, tasa.valor, time);
        cliente.solicitudes[indiceSolicitud].amortizacion = new TablaAmortizacionDAO().getElementos(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);

    }

    private static void actualizaTablaConsumo(ClienteVO cliente, int idSolicitud, Date time) throws ClientesException {
        //ejecuta para Tabla de Consumo y Credihogar
        double prima = 0;
        double primaConComision = 0;
        Calendar calendario = Calendar.getInstance();
        try {
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            //Borra la anterior//
            TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
            tablaDAO.delTablaAmortizacion(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
            //Incio Crear nueva//
            TreeMap catTasas = CatalogoHelper.getCatalogoTasas(cliente.solicitudes[indiceSolicitud].tipoOperacion);
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(cliente.solicitudes[indiceSolicitud].tipoOperacion);
            double montoSinComision = ClientesUtil.calculaMontoSinComision(cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.comision, catComisiones);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(cliente.solicitudes[indiceSolicitud].decisionComite.tasa);
            if (cliente.solicitudes[indiceSolicitud].seguro != null && cliente.solicitudes[indiceSolicitud].seguro.primaTotal != 0) {
                prima = cliente.solicitudes[indiceSolicitud].seguro.primaTotal;
                primaConComision = ClientesUtil.calculaMontoConComision(prima, cliente.solicitudes[indiceSolicitud].decisionComite.comision, catComisiones);
            }

            Double pagoUnitario = TablaAmortHelper.calcPagoUnitario(tasa.valor, cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), cliente.idSucursal, cliente.solicitudes[indiceSolicitud].tipoOperacion);
            Double tasaLogaritmo = TablaAmortHelper.getTasaLogaritmico(cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, tasa.valor);
            Double tasaCalculada = TablaAmortHelper.calcTasa(cliente.solicitudes[indiceSolicitud].tipoOperacion, cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), cliente.idSucursal, tasaLogaritmo);

            insertTablaInsolutoConsumo(cliente, cliente.solicitudes[indiceSolicitud], cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
            cliente.solicitudes[indiceSolicitud].amortizacion = new TablaAmortizacionDAO().getElementos(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
            cliente.solicitudes[indiceSolicitud].tasaCalculada = tasaCalculada;
        } catch (Exception e) {
            myLogger.error("actualizaTablaConsumo", e);
        }
    }

    public static double calcTasa(int operacion, double monto, double pagoUnitario, int pagos, int frecuenciaPago, String fechaIni, int idSucursal, double tasaAnual) throws Exception {
        boolean found = false;
        double abonoInteres = 0.00;
        double abonoCapital = 0.00;
        double sdoInsoluto = monto;
        Integer elementos[] = null;
        int backForward = 0;
        double tasaOriginal = tasaAnual;

        if (operacion == ClientesConstants.CONSUMO) {
            elementos = getDaysBetweenPayments(pagos, frecuenciaPago, fechaIni);
        } else if (operacion == ClientesConstants.MICROCREDITO) {
            elementos = getDaysBetweenPaymentsMicro(pagos, frecuenciaPago, fechaIni);
        }

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            pagos = pagos * 1;
        } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
            pagos = pagos * 2;
        } else if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            pagos = pagos * 2;
        }

        int difDias = 0;

        //tasaAnual = FormatUtil.roundDecimal(tasaAnual, 10);
        tasaAnual = FormatUtil.roundDecimal(tasaAnual, 6);
        try {
            while (!found) {
                if (backForward == 0) {
                    tasaAnual = tasaAnual + 0.000500;
                } else {
                    tasaAnual = tasaAnual - 0.000500;
                }

                sdoInsoluto = monto;
                for (int i = 0; i < pagos - 1; i++) {
                    if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                        difDias = 7;
                    } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
                        difDias = 14;
                    } else {
                        difDias = elementos[i];
                    }

                    abonoInteres = sdoInsoluto * difDias * ((tasaAnual / 100) / 360);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                }

                if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                    difDias = 7;
                } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
                    difDias = 14;
                } else {
                    difDias = elementos[pagos - 1];
                }

                abonoCapital = sdoInsoluto;
                abonoInteres = sdoInsoluto * difDias * ((tasaAnual / 100) / 360);

                double ultimaCuota = abonoCapital + abonoInteres;
                double dif = pagoUnitario - ultimaCuota;
                double cuotaAjustada = dif / pagos;
                double difFinal = pagoUnitario - cuotaAjustada;
                double resta = pagoUnitario - difFinal;

                if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL || frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
                    if (resta > 0.000000 && resta < pagoUnitario * .00001) {
                        found = true;
                    }
                } else {
                    if (resta > 0.000000 && resta < pagoUnitario * .00001) {
                        found = true;
                    }
                }
                if (tasaAnual > 1000.00) {
                    if (backForward == 0) {
                        backForward = 1;
                    }
                }

                if (tasaAnual < 0) {
                    if (backForward == 1) {
                        backForward = 0;
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("calcTasa", e);
        }

        if (tasaOriginal > 0) {
            return tasaAnual;
        } else {
            return tasaOriginal;
        }

    }

    public static int getDaysBetweenPaymentsTabla(TablaAmortVO tablaAmort) {
        TablaAmortVO tabla = null;
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        int dias = 0;

        try {
            // se obtiene el dividendo anterior
            tabla = tablaDAO.getDivPago(tablaAmort.numCliente, tablaAmort.numCredito, tablaAmort.numPago - 1);
            dias = FechasUtil.inBetweenDays(tabla.fechaPago, tablaAmort.fechaPago);

        } catch (Exception e) {
            myLogger.error("getDaysBetweenPaymentsTabla", e);
        }

        return dias;
    }

    public static Integer[] getDaysBetweenPayments(int pagos, int frecuenciaPago, String fechaIni) {
        if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            pagos = pagos * 2;
        }

        ArrayList<Integer> array = new ArrayList<Integer>();
        Integer elementos[] = null;

        try {
            Date fechaTemp = null;
            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaTemp = calcFechaInicio(Convertidor.stringToDate(fechaIni));
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaTemp = calcFechaInicioMensual(Convertidor.stringToDate(fechaIni));
            }

            Date fechaQuincena1 = fechaTemp;
            Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            GregorianCalendar fechaOut = new GregorianCalendar();

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            for (int i = 0; i < pagos; i++) {

                if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(Convertidor.stringToDate(fechaIni), fechaTemp));
                    }
                    if (i == 1) {
                        array.add(FechasUtil.inBetweenDays(fechaQuincena1, fechaQuincena2));
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaQuincena2, fechaQuincena1));
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaQuincena1, fechaQuincena2));
                        }
                    }
                }

                if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(Convertidor.stringToDate(fechaIni), fechaTemp));
                    } else {
                        array.add(FechasUtil.inBetweenDays(fechaTemp, FechasUtil.getDate(fechaTemp, 1, 0)));
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }
                }
            }

            if (array.size() > 0) {
                elementos = new Integer[array.size()];
                int totalDias = 0;
                for (int i = 0; i < array.size(); i++) {
                    elementos[i] = (Integer) array.get(i);
                    totalDias += elementos[i];
                    //System.out.println("NumDias amortizacion # " + i + " =" + array.get(i));
                }
                //System.out.println("Total de d�as = " + totalDias);
            }

        } catch (Exception e) {
            myLogger.error("getDaysBetweenPayments", e);
        }

        return elementos;
    }

    public static Integer[] getDaysBetweenPaymentsDN(int pagos, int frecuenciaPago, String fechaIni) {
        if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            pagos = pagos;
        }

        ArrayList<Integer> array = new ArrayList<Integer>();
        Integer elementos[] = null;

        try {
            Date fechaTemp = null;
            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaTemp = calcFechaInicio(Convertidor.stringToDate(fechaIni));
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaTemp = calcFechaInicioMensual(Convertidor.stringToDate(fechaIni));
            }

            Date fechaQuincena1 = fechaTemp;
            Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            GregorianCalendar fechaOut = new GregorianCalendar();

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            for (int i = 0; i < pagos; i++) {

                if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(Convertidor.stringToDate(fechaIni), fechaTemp));
                    }
                    if (i == 1) {
                        array.add(FechasUtil.inBetweenDays(fechaQuincena1, fechaQuincena2));
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaQuincena2, fechaQuincena1));
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaQuincena1, fechaQuincena2));
                        }
                    }
                }

                if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(Convertidor.stringToDate(fechaIni), fechaTemp));
                    } else {
                        array.add(FechasUtil.inBetweenDays(fechaTemp, FechasUtil.getDate(fechaTemp, 1, 0)));
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }
                }
            }

            if (array.size() > 0) {
                elementos = new Integer[array.size()];
                int totalDias = 0;
                for (int i = 0; i < array.size(); i++) {
                    elementos[i] = (Integer) array.get(i);
                    totalDias += elementos[i];
                    //System.out.println("NumDias amortizacion # " + i + " =" + array.get(i));
                }
                //System.out.println("Total de d�as = " + totalDias);
            }

        } catch (Exception e) {
            myLogger.error("getDaysBetweenPaymentsDN", e);
        }

        return elementos;
    }

    public static Integer[] getDaysBetweenPaymentsMicro(int pagos, int frecuenciaPago, String fechaIni) {
        if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            pagos = pagos * 2;
        }
        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            pagos = pagos * 4;
        }

        ArrayList<Integer> array = new ArrayList<Integer>();
        Integer elementos[] = null;

        try {
            Date fechaTemp = new Date();
            Date fechaAnt = new Date();
            Date fechaInicio = Convertidor.stringToDate(fechaIni);

            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 15, 1);
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 1, 0);
            } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                fechaTemp = FechasUtil.getDate(fechaInicio, 7, 1);
            }

            Date fechaQuincena1 = fechaTemp;
            Date fechaQuincena2 = null;

            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                fechaQuincena2 = FechasUtil.getDate(fechaInicio, 1, 0);
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 1, 0);
            } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 1, 0);
            }

            fechaAnt = fechaQuincena2;
            //Logger.debug("FECHAS: ---- " + fechaQuincena1 + " " + fechaQuincena2);
            for (int i = 0; i < pagos; i++) {

                if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(fechaInicio, fechaTemp));
                    }
                    if (i == 1) {
                        array.add(FechasUtil.inBetweenDays(fechaQuincena1, fechaQuincena2));
                    }
                    if (i > 1) {
                        //Logger.debug("IIIII: " + i);
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, i / 2, 0);
                            //Logger.debug("Quincena 1: ---- " + fechaTemp);
                            //fechaQuincena1 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaAnt, fechaTemp));
                            fechaAnt = fechaTemp;
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaInicio, i / 2 + 1, 0);
                            //Logger.debug("Quincena 2: ---- " + fechaTemp);
                            //fechaQuincena2 = fechaTemp;
                            array.add(FechasUtil.inBetweenDays(fechaAnt, fechaTemp));
                            fechaAnt = fechaTemp;
                        }
                    }
                }

                if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(fechaInicio, fechaTemp));
                    } else {
                        array.add(FechasUtil.inBetweenDays(fechaTemp, FechasUtil.getDate(fechaInicio, i + 1, 0)));
                        fechaTemp = FechasUtil.getDate(fechaInicio, i + 1, 0);
                    }
                }
                //VERIFICAR EL PROCEDIMIENTO PARA MICRO
                if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                    if (i == 0) {
                        array.add(FechasUtil.inBetweenDays(fechaInicio, fechaTemp));
                        fechaTemp = FechasUtil.getDate(fechaInicio, i * 7, 1);
                    } else {
                        array.add(FechasUtil.inBetweenDays(fechaTemp, FechasUtil.getDate(fechaInicio, i * 7, 1)));
                        fechaTemp = FechasUtil.getDate(fechaInicio, i * 7, 1);
                    }
                }
            }

            if (array.size() > 0) {
                elementos = new Integer[array.size()];
                int totalDias = 0;
                for (int i = 0; i < array.size(); i++) {
                    elementos[i] = (Integer) array.get(i);
                    totalDias += elementos[i];
                    //System.out.println("NumDias amortizacion # " + i + " =" + array.get(i));
                }
                //System.out.println("Total de d�as = " + totalDias);
            }

        } catch (Exception e) {
            myLogger.error("getDaysBetweenPaymentsMicro", e);
        }

        return elementos;
    }

    public static String makeTableIBS(TablaAmortizacionVO[] tabla, Integer elementos[], int frecuencia) {
        String tablaFormat = "";
        int dias = 0;
        boolean arrayDias = true;
        if (elementos == null) {
            arrayDias = false;
        }
        try {

            tablaFormat = "<table border='1' cellspacing='0'>";
            tablaFormat += "<tr bgcolor='#c0c0c0'><td><center><b>Pago</b></center></td>";
            tablaFormat += "<td><center><b>Dias</b></center></td>";
            tablaFormat += "<td><center><b>Fecha</b></center></td>";
            tablaFormat += "<td><center><b>Saldo inicial</b></center></td>";
            tablaFormat += "<td><center><b>Abono capital</b></center></td>";
            tablaFormat += "<td><center><b>Saldo capital</b></center></td>";
            tablaFormat += "<td><center><b>Comision inicial</b></center></td>";
            tablaFormat += "<td><center><b>IVA comisión</b></center></td>";
            tablaFormat += "<td><center><b>Interés</b></center></td>";
            tablaFormat += "<td><center><b>IVA Interés</b></center></td>";
            tablaFormat += "<td><center><b>Monto a pagar</b></center></td></tr>";

            for (int i = 0; i < tabla.length; i++) {
                tablaFormat += "<tr><td><center><font size='2'>" + HTMLHelper.displayField(tabla[i].numPago, true) + "</font></center></td>";
                if (i == 0) {
                    tablaFormat += "<td><b><font size='1.5'>" + HTMLHelper.displayField(new Integer(0)) + "</font></b></td>";
                } else {
                    if (frecuencia == ClientesConstants.PAGO_SEMANAL) {
                        dias = 7;
                    } else if (frecuencia == ClientesConstants.PAGO_CATORCENAL) {
                        dias = 14;
                    } else if (arrayDias) {
                        dias = elementos[i - 1];
                    }

                    tablaFormat += "<td><b><font size='1.5'>" + HTMLHelper.displayField(dias) + "</font></b></td>";
                }
                tablaFormat += "<td><b><font size='1.5'>" + HTMLHelper.displayField(tabla[i].fechaPago) + "</font></b></td>";
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].saldoInicial) + "</center></td>";
                //Abono a capital
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].abonoCapital) + "</center></td>";
                //Saldo capital
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].saldoCapital) + "</center></td>";
                //Comision inicial
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].comisionInicial) + "</center></td>";
                //IVA comisi�n
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].ivaComision) + "</center></td>";
                //Interes
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].interes) + "</center></td>";
                //IVA inter�s
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].ivaInteres) + "</center></td>";
                //Monto a pagar
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].montoPagar) + "</center></td></tr>";
            }
            tablaFormat += "</table>";

        } catch (Exception e) {
            myLogger.error("makeTableIBS", e);
        }

        return tablaFormat;
    }

    public static String makeTable(TablaAmortizacionVO[] tabla) {
        String tablaFormat = "";

        try {

            tablaFormat = "<table border='1' cellspacing='0'>";
            tablaFormat += "<tr bgcolor='#c0c0c0'><td><center><b>Pago</b></center></td>";
            tablaFormat += "<td><center><b>Fecha</b></center></td>";
            tablaFormat += "<td><center><b>Saldo inicial</b></center></td>";
            tablaFormat += "<td><center><b>Abono capital</b></center></td>";
            tablaFormat += "<td><center><b>Saldo capital</b></center></td>";
            tablaFormat += "<td><center><b>Comision inicial</b></center></td>";
            tablaFormat += "<td><center><b>IVA comisión</b></center></td>";
            tablaFormat += "<td><center><b>Interés</b></center></td>";
            tablaFormat += "<td><center><b>IVA Interés</b></center></td>";
            tablaFormat += "<td><center><b>Monto a pagar</b></center></td></tr>";

            for (int i = 0; i < tabla.length; i++) {
                tablaFormat += "<tr><td><center><font size='2'>" + HTMLHelper.displayField(tabla[i].numPago, true) + "</font></center></td>";
                tablaFormat += "<td><b><font size='1.5'>" + HTMLHelper.displayField(tabla[i].fechaPago) + "</font></b></td>";
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].saldoInicial) + "</center></td>";
                //Abono a capital
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].abonoCapital) + "</center></td>";
                //Saldo capital
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].saldoCapital) + "</center></td>";
                //Comision inicial
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].comisionInicial) + "</center></td>";
                //IVA comisi�n
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].ivaComision) + "</center></td>";
                //Interes
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].interes) + "</center></td>";
                //IVA inter�s
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].ivaInteres) + "</center></td>";
                //Monto a pagar
                tablaFormat += "<td><center>" + HTMLHelper.formatoMonto(tabla[i].montoPagar) + "</center></td></tr>";
            }
            tablaFormat += "</table>";

        } catch (Exception e) {
            myLogger.error("makeTable", e);
        }

        return tablaFormat;
    }

    public static String makePagare(TablaAmortizacionVO[] tabla) {
        String tablaPagare = "";
        tablaPagare = "<table border='1' cellspacing='0'>";
        tablaPagare += "<tr bgcolor='#c0c0c0'><td><b>Exhibición </b></td>";
        tablaPagare += "<td><b>Abono</b></td>";
        tablaPagare += "<td><b>Fecha de pago</b></td></tr>";

        for (int i = 1; i < tabla.length; i++) {
            tablaPagare += "<td><center><font size='2'>" + HTMLHelper.displayField(tabla[i].numPago, true) + "</center></font></td>";
            tablaPagare += "<td><center><font size='2'>" + HTMLHelper.formatoMonto(tabla[i].abonoCapital) + "</font></center></td>";
            tablaPagare += "<td><center><font size='2'>" + HTMLHelper.displayField(tabla[i].fechaPago) + "</font></td></tr>";
        }

        tablaPagare += "</table>";
        return tablaPagare;
    }

    public static double calcPagoUnitario(double tasaAnual, double monto, int pagos, int frecuenciaPago, String fechaIni, int idSucursal, int tipoOperacion) {
        int difDias = 0;
        Double intTotal = 0.00;
        Double ivaIntTotal = 0.00;
        Double total = 0.00;
        Double pagoUnitario = 0.00;
        Integer elementos[] = null;
        int sumadias = 0;

        try {
            boolean fronterizo = Convertidor.esFronterizo(idSucursal);

//			if ( tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL ){
            if (tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL || tipoOperacion == ClientesConstants.CONSUMO) {
                sumadias = pagos * 7;
            } else if (tipoOperacion == ClientesConstants.MICROCREDITO) {
                elementos = getDaysBetweenPaymentsMicro(pagos, frecuenciaPago, fechaIni);
            } else {
                elementos = getDaysBetweenPayments(pagos, frecuenciaPago, fechaIni);
            }

//			if ( tipoOperacion != ClientesConstants.GRUPAL && tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL ){
            if (tipoOperacion != ClientesConstants.GRUPAL && tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL && tipoOperacion != ClientesConstants.CONSUMO) {
                if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                    pagos = pagos * 2;
                }

                for (int i = 0; i < pagos; i++) {
                    sumadias += elementos[i];
                }
            }

            difDias = sumadias;

            if (fronterizo) {
                tasaAnual = tasaAnual * (1.15 / 1.10);
                intTotal = monto * ((tasaAnual / 360) * difDias);
                intTotal = intTotal / 100;
                ivaIntTotal = intTotal - Convertidor.getMontoIva(intTotal, idSucursal, 1);
            } else {
                intTotal = monto * ((tasaAnual / 360) * difDias);
                intTotal = intTotal / 100;
                ivaIntTotal = Convertidor.getMontoIva(intTotal, idSucursal, 2);
            }
            total = monto + intTotal + ivaIntTotal;
            pagoUnitario = total / pagos;

        } catch (Exception e) {
            myLogger.error("calcPagoUnitario", e);
        }

        pagoUnitario = java.lang.Math.ceil(pagoUnitario);

        //Logger.debug("PAGO UNITARIO--------> " + pagoUnitario);
        return pagoUnitario;
    }

    public static double getTasaLogaritmico(double monto, double pagoUnitario, int meses, int frecuenciaPago, double tasaOriginal) {
        //meses = duraci�n del cr�dito en meses
        //pagos = n�mero de amortizaciones del cr�dito
        //Periodos = n�mero de amortizaciones por a�o en base a la frecuencia de pago

        double tasaInteres = 0.00;
        double pagos = 0.00;
        int periodos = 0;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            pagos = meses;
            periodos = 48;
        } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
            pagos = meses * 2;
            periodos = 24;
        } else if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            pagos = meses * 2;
            periodos = 24;
        } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
            pagos = meses;
            periodos = 12;
        }

        try {
            double temp = 1 + (1 / pagos);
            //System.out.println("TEMP: " + temp);
            double primer = FormatUtil.roundDouble(java.lang.Math.log10(temp), 10);
            //System.out.println("PRIMER: " + primer);
            double segundo = FormatUtil.roundDouble(java.lang.Math.log10(2), 10);
            //System.out.println("SEGUNDO: " + segundo);
            double q = primer / segundo;
            //System.out.println("Q: " + q);
            double y = java.lang.Math.pow((1 + (pagoUnitario / monto)), (1 / q)) - 1;
            //System.out.println("Y: " + y);
            double i = java.lang.Math.pow(y, q) - 1;
            //System.out.println("I: " + i);
            tasaInteres = i * periodos * 100;
            //System.out.println("INTERES: " + interes);

        } catch (Exception e) {
            myLogger.error("getTasaLogaritmico", e);
        }

        if (tasaOriginal > 0) {
            return tasaInteres;
        } else {
            return tasaOriginal;
        }
    }

    public static Date calcFechaInicio(Date fechaIni) {
        GregorianCalendar fechaOut = new GregorianCalendar();
        fechaOut.setTime(fechaIni);
        int day = fechaOut.get(Calendar.DAY_OF_MONTH);

        try {
            if (day <= 5) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 16);
            }
            if (day > 5 && day < 20) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
            }
            if (day >= 20) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 16);
                fechaOut.add(Calendar.MONTH, 1);
            }

        } catch (Exception e) {
            myLogger.error("calcFechaInicio", e);
        }

        return fechaOut.getTime();
    }

    public static Date calcFechaInicioDNQuincenal(Date fechaIni) {
        GregorianCalendar fechaOut = new GregorianCalendar();
        fechaOut.setTime(fechaIni);
        int day = fechaOut.get(Calendar.DAY_OF_MONTH);

        try {
            if (day <= 15) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
            } else {
                fechaOut.set(Calendar.DAY_OF_MONTH, 16);
                fechaOut.add(Calendar.MONTH, 1);
            }

        } catch (Exception e) {
            myLogger.error("calcFechaInicioDNQuincenal", e);
        }

        return fechaOut.getTime();
    }

    public static Date calcFechaArranqueDNQuincenal(Date fechaIni) {
        GregorianCalendar fechaOut = new GregorianCalendar();
        fechaOut.setTime(fechaIni);
        int day = fechaOut.get(Calendar.DAY_OF_MONTH);

        try {
            if (day <= 15) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 15);
            } else {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
            }

        } catch (Exception e) {
            myLogger.error("calcFechaArranqueDNQuincenal", e);
        }

        return fechaOut.getTime();
    }

    public static Date calcFechaInicioMensual(Date fechaIni, int idProducto) {
        Date fecha = null;
        switch (idProducto) {
            case ClientesConstants.MAX_ZAPATOS:
                fecha = calcFechaInicioMensualMaxZapatos(fechaIni);
                break;
            default:
                fecha = calcFechaInicioMensual(fechaIni);
                break;
        }
        return fecha;
    }

    public static Date calcFechaInicioMensual(Date fechaIni) {
        GregorianCalendar fechaOut = new GregorianCalendar();
        fechaOut.setTime(fechaIni);
        int day = fechaOut.get(Calendar.DAY_OF_MONTH);

        try {
            if (day <= 10) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
            }
            if (day >= 26) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 2);
            }
            if (day > 10 && day < 26) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 16);
                fechaOut.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            myLogger.error("calcFechaInicioMensual", e);
        }

        return fechaOut.getTime();
    }

    public static Date calcFechaInicioMensualMaxZapatos(Date fechaIni) {

        GregorianCalendar fechaOut = new GregorianCalendar();
        fechaOut.setTime(fechaIni);
        int day = fechaOut.get(Calendar.DAY_OF_MONTH);
        try {
            if (day <= 11) {
                fechaOut.add(Calendar.MONTH, 1);
                fechaOut.set(Calendar.DAY_OF_MONTH, 02);
            } else if (day >= 12 && day <= 27) {
                fechaOut.add(Calendar.MONTH, 1);
                fechaOut.set(Calendar.DAY_OF_MONTH, 17);
            } else if (day >= 28) {
                fechaOut.add(Calendar.MONTH, 2);
                fechaOut.set(Calendar.DAY_OF_MONTH, 02);
            }
        } catch (Exception e) {
            myLogger.error("calcFechaInicioMensualMaxZapatos", e);
        }
        return fechaOut.getTime();
    }

    public static void insertTablaGlobal(int idGrupal, int idCiclo, int idSucursal, double monto, double montoConComision, double tasa, double tasaComision, Date fechaInicio, int plazo) {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;

        int dias = plazo * 7;
        Date fechaTemp = fechaInicio;

        try {
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
            comision = montoConComision - monto;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;

            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
                temp.saldoInicial = montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                temp.comisionInicial = comision;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision + ivaComision;
                temp.idCliente = idGrupal;
                temp.idSolicitud = idCiclo;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaGlobal", e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * (1.16 / 1.10);
            } else {
                tasa = tasa * 1.16;
            }
            monto = montoConComision;
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            interes = interes - ivaInteres;
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            for (int i = 1; i <= plazo; i++) {
                temp = new TablaAmortizacionVO();
                temp.idCliente = idGrupal;
                temp.idSolicitud = idCiclo;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                temp.numPago = i;
                fechaTemp = FechasUtil.getDate(fechaTemp, 7, 1);
                temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comisionInicial = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = abonoCapital + interes + ivaInteres;
                tabladao.addTablaAmortizacion(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaGlobal", e);
        }

    }

    public static void insertTablaMicro(int idCliente, int idSolicitud, int idSucursal, double monto, double montoSinComision, int plazo, double tasa, Date fechaInicio) {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;
        Date fechaFinal = FechasUtil.getDate(fechaInicio, plazo, 0);
        fechaFinal = FechasUtil.getBusinessDate(FechasUtil.getDateCierreMes(fechaFinal));
        int dias = FechasUtil.inBetweenDays(fechaInicio, fechaFinal);
        Date fechaTemp = fechaInicio;
        fechaTemp = FechasUtil.getDateCierreMes(fechaTemp);

        try {
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;

            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
                temp.saldoInicial = monto;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                temp.comisionInicial = comision;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision + ivaComision;
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaMicro", e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * (1.15 / 1.10);
            }
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            for (int i = 1; i <= plazo; i++) {
                temp = new TablaAmortizacionVO();
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i;
                fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                temp.fechaPago = Convertidor.toSqlDate(FechasUtil.getBusinessDate(fechaTemp));
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comisionInicial = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                //double pago = abonoCapital+interes+ivaInteres;
                temp.montoPagar = montoApagar;
                tabladao.addTablaAmortizacion(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaMicro", e);
        }

    }

    public static void insertTablaVivienda(int idCliente, int idSolicitud, int idSucursal, double monto, double montoSinComision, int pagos, int frecuenciaPago, double tasa, Date fechaInicio) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double interesTotal = 0.00;
        double abonoCapital = 0.00;
//		double abonoInteres = 0.00;		
//		double sdoInsoluto = monto;
        boolean fronterizo = false;
        double montoApagar = 0.00;

        try {
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
            Date fechaTemp = calcFechaInicio(fechaInicio);
            Date fechaQuincena1 = fechaTemp;
            Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            GregorianCalendar fechaOut = new GregorianCalendar();

            Date fechaFinal = FechasUtil.getDate(fechaInicio, pagos, 0);
            int dias = FechasUtil.inBetweenDays(fechaInicio, fechaFinal);

            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                comision = monto - montoSinComision;
                ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
                comision = comision - ivaComision;

                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
                temp.saldoInicial = monto;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                temp.comisionInicial = comision;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision + ivaComision;
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                myLogger.error("insertTablaVivienda", e);
            }

//			Integer elementos[] = null;
//			elementos = getDaysBetweenPays(pagos, frecuenciaPago, Convertidor.dateToString(fechaInicio));
            if (frecuenciaPago == 1) {
                pagos = pagos * 2;
            }

            fechaTemp = calcFechaInicio(fechaInicio);
            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            fronterizo = Convertidor.esFronterizo(sucVO);
            abonoCapital = monto / pagos;
            tasa = tasa * 1.15;

            interesTotal = ((monto * FormatUtil.roundDouble(tasa, 2)) / 100) / 360;
            interesTotal = (interesTotal * dias);

            interes = interesTotal / pagos;
            if (fronterizo) {
                ivaInteres = interes - (interes / 1.10);
                interes = interes - ivaInteres;
            } else {
                ivaInteres = interes - (interes / 1.15);
                interes = interes - ivaInteres;
            }

            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * pagos;

            for (int i = 0; i < pagos; i++) {
                temp = new TablaAmortizacionVO();
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i + 1;

                if (frecuenciaPago == 1) {
                    if (i == 0) {
                        fechaTemp = fechaQuincena1;
                    }
                    if (i == 1) {
                        fechaTemp = fechaQuincena2;
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                        }
                    }
                }

                if (frecuenciaPago == 2 && i != 0) {
                    fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                }

                temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                temp.saldoInicial = 0.00;
                if (i != pagos) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == pagos - 1) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comisionInicial = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = montoApagar;
                tabladao.addTablaAmortizacion(temp);
            }

        } catch (Exception e) {
            myLogger.error("insertTablaVivienda", e);
        }

    }

    public static void insertTablaMaxZapatos(int idCliente, int idSolicitud, int idDisposicion, int idSucursal, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        double interes = 0.00;
        double interesTotal = 0.00;
        double ivaInteres = 0.00;
        double comision = 0.00;
        double ivaComision = 0.00;
        double abonoCapital = 0.00;
        double montoApagar = 0.00;

        Date fechaTemp = calcFechaInicio(fechaInicio);
        Date fechaQuincena1 = fechaTemp;
        Date fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
        GregorianCalendar fechaOut = new GregorianCalendar();
        SucursalDAO sucDAO = new SucursalDAO();
        SucursalVO sucVO = sucDAO.getSucursal(idSucursal);

        try {
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;

            temp.numPago = 0;
            temp.fechaPago = Convertidor.toSqlDate(fechaInicio);
            temp.saldoInicial = monto;
            temp.abonoCapital = 0;
            temp.saldoCapital = 0;
            temp.comisionInicial = comision;
            temp.ivaComision = ivaComision;
            temp.interes = 0;
            temp.ivaInteres = 0;
            temp.montoPagar = comision + ivaComision;
            temp.idCliente = idCliente;
            temp.idSolicitud = idSolicitud;
            temp.idDisposicion = idDisposicion;
            temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
            tabladao.addTablaAmortizacion(temp);

            Integer elementos[] = null;
            elementos = obtenDiasPeriodos(plazo, frecuenciaPago, fechaInicio);
            int dias = 0;
            for (int i = 0; i < elementos.length; i++) {
                dias += elementos[i];
            }

            if (frecuenciaPago == 1) {
                plazo = plazo * 2;
            }

            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * (1.15 / 1.10);
            }
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            double tempMonto = abonoCapital + interes + ivaInteres;
            montoApagar = java.lang.Math.ceil(tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            if (frecuenciaPago == 1) {
                fechaTemp = calcFechaInicio(fechaInicio);
            } else if (frecuenciaPago == 2) {
                fechaTemp = calcFechaInicioMensualMaxZapatos(fechaInicio);
            }

            fechaQuincena1 = fechaTemp;
            fechaOut.setTime(fechaQuincena1);
            int day = fechaOut.get(Calendar.DAY_OF_MONTH);

            if (day == 16) {
                fechaOut.set(Calendar.DAY_OF_MONTH, 01);
                fechaOut.add(Calendar.MONTH, 1);
                fechaQuincena2 = fechaOut.getTime();
            } else {
                fechaQuincena2 = FechasUtil.getDate(fechaQuincena1, 15, 1);
            }

            for (int i = 0; i < plazo; i++) {
                temp = new TablaAmortizacionVO();
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.idDisposicion = idDisposicion;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.numPago = i + 1;
                if (frecuenciaPago == 1) {
                    if (i == 0) {
                        fechaTemp = fechaQuincena1;
                    }
                    if (i == 1) {
                        fechaTemp = fechaQuincena2;
                    }
                    if (i > 1) {
                        if ((i % 2) == 0) {
                            fechaTemp = FechasUtil.getDate(fechaQuincena1, 1, 0);
                            fechaQuincena1 = fechaTemp;
                        } else {
                            fechaTemp = FechasUtil.getDate(fechaQuincena2, 1, 0);
                            fechaQuincena2 = fechaTemp;
                        }
                    }
                } else if (frecuenciaPago == 2) {
                    if (i >= 1) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                    }
                }

                temp.fechaPago = Convertidor.toSqlDate(FechasUtil.getBusinessDate(fechaTemp));
                temp.saldoInicial = 0.00;
                if (i != plazo) {
                    abonoCapital = montoApagar - interesMasIva;
                }
                if (i == plazo - 1) {
                    abonoCapital = monto;
                    montoApagar = montoApagar - ajuste;
                }
                monto = monto - abonoCapital;
                temp.abonoCapital = abonoCapital;
                temp.saldoCapital = monto;
                temp.comisionInicial = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = montoApagar;
                tabladao.addTablaAmortizacion(temp);
            }
        } catch (Exception e) {
            myLogger.error("insertTablaMaxZapatos", e);
            throw new ClientesException(e.getMessage());
        }

    }

    public static Integer[] obtenDiasPeriodos(int pagos, int frecuenciaPago, Date fechaIni) {

        if (frecuenciaPago == 1) {
            pagos = pagos * 2;
        }
        ArrayList<Integer> array = new ArrayList<Integer>();
        Integer elementos[] = null;
        try {
            Date fechaTemp = null;
            fechaTemp = calcFechaInicioMensualMaxZapatos(fechaIni);
            for (int i = 0; i < pagos; i++) {
                if (i == 0) {
                    array.add(FechasUtil.inBetweenDays(fechaIni, fechaTemp));
                } else {
                    array.add(FechasUtil.inBetweenDays(fechaTemp, FechasUtil.getDate(fechaTemp, 1, 0)));
                    fechaTemp = FechasUtil.getDate(fechaTemp, 1, 0);
                }
            }
            if (array.size() > 0) {
                elementos = new Integer[array.size()];
                for (int c = 0; c < elementos.length; c++) {
                    elementos[c] = (Integer) array.get(c);
                }
            }
        } catch (Exception e) {
            myLogger.error("obtenDiasPeriodos", e);
        }
        return elementos;

    }

    public static void generaTablaAmortizacion(ClienteVO cliente, SolicitudVO solicitud) throws ClientesException {

        double montoInicialConComision = 0;
        double montoSinComision = 0;
        Calendar cal = Calendar.getInstance();

        if (solicitud.decisionComite != null && solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA) {
            TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(solicitud.decisionComite.tasa);

            try {
                switch (solicitud.tipoOperacion) {
                    case ClientesConstants.CONSUMO:
                        montoSinComision = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
                        montoSinComision += solicitud.seguro.primaTotal;
                        montoInicialConComision = ClientesUtil.calculaMontoConComision(montoSinComision, solicitud.decisionComite.comision, catComisiones);
                        if (solicitud.amortizacion != null) {
                            new TablaAmortizacionDAO().delTablaAmortizacion(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        }
                        //Genera la tabla de amortizaci�n
                        Double pagoUnitario = TablaAmortHelper.calcPagoUnitario(tasa.valor, montoInicialConComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, solicitud.tipoOperacion);
                        Double tasaLogaritmo = TablaAmortHelper.getTasaLogaritmico(montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor);
                        Double tasaCalculada = TablaAmortHelper.calcTasa(solicitud.tipoOperacion, montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo);
                        TablaAmortHelper.insertTablaInsolutoConsumo(cliente, solicitud, montoInicialConComision, montoSinComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada);

                        //TablaAmortizacionHelper.insertTablaConsumo(solicitud.idCliente, solicitud.idSolicitud, idSucursal , montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor, new Date());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        break;
                    case ClientesConstants.MICROCREDITO:
                        montoSinComision = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
                        montoSinComision += solicitud.seguro.primaTotal;
                        montoInicialConComision = ClientesUtil.calculaMontoConComision(montoSinComision, solicitud.decisionComite.comision, catComisiones);
                        if (solicitud.amortizacion != null) {
                            new TablaAmortizacionDAO().delTablaAmortizacion(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        }
                        //Genera la tabla de amortizaci�n
                        pagoUnitario = TablaAmortHelper.calcPagoUnitario(tasa.valor, montoInicialConComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, solicitud.tipoOperacion);
                        tasaLogaritmo = TablaAmortHelper.getTasaLogaritmico(montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor);
                        tasaCalculada = TablaAmortHelper.calcTasa(solicitud.tipoOperacion, montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo);
                        TablaAmortHelper.insertTablaInsolutoMicro(cliente, solicitud, montoInicialConComision, montoSinComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada);

                        //TablaAmortizacionHelper.insertTablaMicro(solicitud.idCliente, solicitud.idSolicitud, cliente.idSucursal, montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, tasa.valor, new Date());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        break;
                    case ClientesConstants.VIVIENDA:
                        montoSinComision = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
                        montoSinComision += solicitud.seguro.primaTotal;
                        montoInicialConComision = ClientesUtil.calculaMontoConComision(montoSinComision, solicitud.decisionComite.comision, catComisiones);
                        if (solicitud.amortizacion != null) {
                            new TablaAmortizacionDAO().delTablaAmortizacion(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        }
                        TablaAmortHelper.insertTablaVivienda(solicitud.idCliente, solicitud.idSolicitud, cliente.idSucursal, montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor, new Date());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        break;
                }
            } catch (Exception e) {
                myLogger.error("generaTablaAmortizacion", e);
                throw new ClientesException(e.getMessage());
            }
        }

    }

    //public static double aplicaPagosTabla(CreditoCartVO credito, double pago, Date fechaFin) throws ClientesException {
    public static double aplicaPagosTabla(CreditoCartVO credito, double pago, Date fechaFin, boolean acumulado, int dias_dividendo, Date fechaCierre) throws ClientesException {

        //ejecuta 
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO tablaDivAnterior = null;
        DevengamientoVO devengamiento = new DevengamientoVO();
        RubrosVO rubro = new RubrosVO();
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        DevengamientoDAO devengDAO = new DevengamientoDAO();
        EventoHelper eventohelper = new EventoHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        PagoDAO pagoDAO = new PagoDAO();
        saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
        double remanente = pago;
        double saldo_capital = 0.00;
        double saldo_capital_vigente = 0.00;
        double saldo_capital_vencido = 0.00;
        double saldo_interes = 0.00;
        double saldo_iva_interes = 0.00;
        double saldo_interes_vig = 0.00;
        double saldo_iva_interes_vig = 0.00;
        double saldo_interes_vencido = 0.00;
        double saldo_iva_interes_vencido = 0.00;
        double saldo_mora = 0.00;
        double saldo_iva_mora = 0.00;
        double saldo_multa = 0.00;
        double saldo_iva_multa = 0.00;
        double saldo_total_vencido = 0.00;
        double saldo_total_aldia = 0.00;
        double saldo_insoluto = 0.00;
        double saldo_con_interes_final = 0.00;
        double pago_capital = 0.00;
        double pago_capital_vencido = 0.00;
        double pago_interes = 0.00;
        double pago_iva_interes = 0.00;
        double pago_interes_vigente = 0.00;
        double pago_iva_interes_vigente = 0.00;
        double pago_interes_vencido = 0.00;
        double pago_iva_interes_vencido = 0.00;
        double pago_interes_vencido_90 = 0.00;
        double pago_iva_interes_vencido_90 = 0.00;
        double pago_interes_cta_orden = 0.00;
        double pago_mora = 0.00;
        double pago_iva_mora = 0.00;
        double pago_multa = 0.00;
        double pago_iva_multa = 0.00;
        double pago_total = 0.00;
        double monto_a_pagar = 0.00;
        double monto_aplicado = 0.00;
        double interes_acumulado = 0.00;
        double iva_interes_acumulado = 0.00;
        double diferencia_interes = 0.00;
        double diferencia_iva_interes = 0.00;
        double fraccion = 0.00;
        Calendar c1 = Calendar.getInstance();
        int status = 1;
        int dias_mora = 0;
        int cuotas_vencidas = 0;
        Date fecha_mora = null;
// Se obtienen datos de saldos con los cuales se trabajaran		
        saldo_total_aldia = saldoVO.getSaldoTotalAlDia();
        saldo_con_interes_final = saldoVO.getSaldoConInteresAlFinal();
        status = saldoVO.getEstatus();
        MailUtil Mail = new MailUtil();
        String Destinatarios = CatalogoHelper.getParametro("RECIPIENT");

        try {
            tablaAmort = tablaDAO.getDivPago(credito.getNumCliente(), credito.getNumCredito(), Convertidor.toSqlDate(fechaFin));
            for (int i = 0; i < tablaAmort.length; i++) {
                saldo_capital = tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                saldo_interes = tablaAmort[i].interes - tablaAmort[i].interesPagado;
                saldo_iva_interes = tablaAmort[i].ivaInteres - tablaAmort[i].ivaInteresPagado;
                saldo_mora = tablaAmort[i].intMoratorio - tablaAmort[i].intMoratorioPagado;
                saldo_iva_mora = tablaAmort[i].ivaIntMoratorio - tablaAmort[i].ivaIntMoratorioPagado;
                saldo_multa = tablaAmort[i].multa - tablaAmort[i].multaPagado;
                saldo_iva_multa = tablaAmort[i].ivaMulta - tablaAmort[i].ivaMultaPagado;
                monto_aplicado = 0;
                myLogger.info("pago capital vencido antes de bucle" + pago_capital_vencido);
                myLogger.info("Remanente: " + remanente);
                // Se obtiene el monto a pagar total del dividendo
                if (remanente >= tablaAmort[i].montoPagar) {
                    // En caso de que se lleve aculumado se calculara el interés al dia del dividendo vigente
                    if (acumulado && tablaAmort[i].status == ClientesConstants.DIVIDENDO_VIGENTE) {
                        myLogger.info("recalcula el interes vigente");
                        tablaDivAnterior = tablaDAO.getDivPago(credito.getNumCliente(), credito.getNumCredito(), tablaAmort[i].numPago - 1);
                        interes_acumulado = tablaDivAnterior.saldoCapital * (credito.getTasaInteresSIVA() / 100 / 360 * dias_dividendo);
                        iva_interes_acumulado = Convertidor.getMontoIva(interes_acumulado, credito.getNumSucursal(), 2);
                        diferencia_interes = tablaAmort[i].interes - interes_acumulado;
                        diferencia_iva_interes = tablaAmort[i].ivaInteres - iva_interes_acumulado;
                        myLogger.info("diferencia_interes " + diferencia_interes);
                        tablaAmort[i].interes = interes_acumulado;
                        tablaAmort[i].ivaInteres = iva_interes_acumulado;
                        tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - diferencia_interes - diferencia_iva_interes;
                        saldo_interes = tablaAmort[i].interes - tablaAmort[i].interesPagado;
                        saldo_iva_interes = tablaAmort[i].ivaInteres - tablaAmort[i].ivaInteresPagado;
                    }
                    if (tablaAmort[i].totalPagado == 0) {

                        // Se igualan los montos a pagados con el saldo inicial
                        tablaAmort[i].multaPagado = tablaAmort[i].multa;
                        tablaAmort[i].ivaMultaPagado = tablaAmort[i].ivaMulta;
                        tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorio;
                        tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorio;
                        tablaAmort[i].interesPagado = tablaAmort[i].interes;
                        tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteres;
                        tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                        remanente = remanente - tablaAmort[i].montoPagar;
                        tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + tablaAmort[i].montoPagar;
                        tablaAmort[i].montoPagar = 0;
                        tablaAmort[i].pagado = "S";
                        // se lleva un control sobre el total pagado
                        pago_capital = pago_capital + tablaAmort[i].capitalPagado;
                        pago_interes = pago_interes + tablaAmort[i].interesPagado;
                        pago_iva_interes = pago_iva_interes + tablaAmort[i].ivaInteresPagado;
                        pago_mora = pago_mora + tablaAmort[i].intMoratorioPagado;
                        pago_iva_mora = pago_iva_mora + tablaAmort[i].ivaIntMoratorioPagado;
                        pago_multa = pago_multa + tablaAmort[i].multaPagado;
                        pago_iva_multa = pago_iva_multa + tablaAmort[i].ivaMultaPagado;
                        monto_aplicado = tablaAmort[i].totalPagado;
                        if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                            pago_interes_vencido = pago_interes_vencido + saldo_interes;
                            pago_iva_interes_vencido = pago_iva_interes_vencido + saldo_iva_interes;
                            pago_capital_vencido = pago_capital_vencido + tablaAmort[i].capitalPagado;
                            // pago credito vencido 90 dias
                        } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                            fecha_mora = tablaAmort[i].fechaPago;
                            dias_mora = FechasUtil.inBetweenDays(fecha_mora, fechaFin);
                            pago_interes_vencido_90 = pago_interes_vencido_90 + saldo_interes;
                            pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + saldo_iva_interes;
                            pago_capital_vencido = pago_capital_vencido + tablaAmort[i].capitalPagado;
                        } else {
                            pago_interes_vigente = pago_interes_vigente + saldo_interes;
                        }

                    } else {
                        tablaAmort[i].multaPagado = tablaAmort[i].multaPagado + saldo_multa;
                        tablaAmort[i].ivaMultaPagado = tablaAmort[i].ivaMultaPagado + saldo_iva_multa;
                        tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorioPagado + saldo_mora;
                        tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorioPagado + saldo_iva_mora;
                        tablaAmort[i].interesPagado = tablaAmort[i].interesPagado + saldo_interes;
                        tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteresPagado + saldo_iva_interes;
                        tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + saldo_capital;
                        remanente = remanente - tablaAmort[i].montoPagar;
                        monto_aplicado = tablaAmort[i].montoPagar;
                        tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + tablaAmort[i].montoPagar;
                        tablaAmort[i].montoPagar = 0;
                        tablaAmort[i].pagado = "S";
                        // se lleva un control sobre el total pagado
                        pago_capital = pago_capital + saldo_capital;
                        pago_interes = pago_interes + saldo_interes;
                        pago_iva_interes = pago_iva_interes + saldo_iva_interes;
                        pago_mora = pago_mora + saldo_mora;
                        pago_iva_mora = pago_iva_mora + saldo_iva_mora;
                        pago_multa = pago_multa + saldo_multa;
                        pago_iva_multa = pago_iva_multa + saldo_iva_multa;
                        if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                            pago_interes_vencido = pago_interes_vencido + saldo_interes;
                            pago_iva_interes_vencido = pago_iva_interes_vencido + saldo_iva_interes;
                            pago_capital_vencido = pago_capital_vencido + saldo_capital;
                            // pago credito vencido 90 dias
                        } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                            pago_interes_vencido_90 = pago_interes_vencido_90 + saldo_interes;
                            pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + saldo_iva_interes;
                            pago_capital_vencido = pago_capital_vencido + saldo_capital;
                        } else {
                            pago_interes_vigente = pago_interes_vigente + saldo_interes;
                        }
                    }
// Se realizara unicamente un pago parcial						
                } else if (remanente > 0) {
                    myLogger.info("Entra a pago fraccionario" + tablaAmort[i].numCliente + "pago " + tablaAmort[i].numPago);
                    myLogger.info("pago capital vencido antes de fraccion " + pago_capital_vencido);
                    monto_aplicado = remanente;
                    // Comenzamos con el orden de prelacion MULTA
                    if (saldo_multa > 0) {
                        // Calcula si puede pagar completamente el rubro en caso contrario se paga en forma proporcional
                        if (remanente >= (saldo_multa + saldo_iva_multa)) {
                            tablaAmort[i].multaPagado = tablaAmort[i].multa;
                            tablaAmort[i].ivaMultaPagado = tablaAmort[i].ivaMulta;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_multa + saldo_iva_multa;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_multa - saldo_iva_multa;
                            pago_multa = pago_multa + saldo_multa;
                            pago_iva_multa = pago_iva_multa + saldo_iva_multa;
                            remanente = remanente - saldo_multa - saldo_iva_multa;

                        } else {
                            // Se calcula la fraccion
                            fraccion = FormatUtil.roundDecimal((saldo_multa / (saldo_multa + saldo_iva_multa)), 2);
                            tablaAmort[i].multaPagado = tablaAmort[i].multaPagado + FormatUtil.roundDecimal(remanente * fraccion, 2);
                            tablaAmort[i].ivaMultaPagado = tablaAmort[i].ivaMultaPagado + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                            pago_multa = pago_multa + FormatUtil.roundDecimal(remanente * fraccion, 2);
                            pago_iva_multa = pago_iva_multa + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                            remanente = 0;
                        }
                        // INTERES MORATORIO
                        if (saldo_mora > 0 && remanente > 0) {
                            if (remanente >= (saldo_mora + saldo_iva_mora)) {
                                tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorio;
                                tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorio;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_mora + saldo_iva_mora;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_mora - saldo_iva_mora;
                                pago_mora = pago_mora + saldo_mora;
                                pago_iva_mora = pago_iva_mora + saldo_iva_mora;
                                remanente = remanente - saldo_mora - saldo_iva_mora;
                            } else {
                                // Se calcula la fraccion
                                fraccion = FormatUtil.roundDecimal((saldo_mora / (saldo_mora + saldo_iva_mora)), 2);
                                tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorioPagado + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorioPagado + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                                pago_mora = pago_mora + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                pago_iva_mora = pago_iva_mora + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                                remanente = 0;
                            }
                        }
                        // INTERES
                        if (saldo_interes > 0 && remanente > 0) {
                            if (remanente >= (saldo_interes + saldo_iva_interes)) {
                                tablaAmort[i].interesPagado = tablaAmort[i].interes;
                                tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteres;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_interes + saldo_iva_interes;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_interes - saldo_iva_interes;
                                pago_interes = pago_interes + saldo_interes;
                                pago_iva_interes = pago_iva_interes + saldo_iva_interes;
                                remanente = remanente - saldo_interes - saldo_iva_interes;
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                                    pago_interes_vencido = pago_interes_vencido + saldo_interes;
                                    pago_iva_interes_vencido = pago_iva_interes_vencido + saldo_iva_interes;
                                } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_interes_vencido_90 = pago_interes_vencido_90 + saldo_interes;
                                    pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + saldo_iva_interes;
//									pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                                } else {
                                    pago_interes_vigente = pago_interes_vigente + saldo_interes;
                                }
                            } else {
                                // Se calcula la fraccion
//								fraccion = FormatUtil.roundDecimal((saldo_interes / (saldo_interes + saldo_iva_interes)),2);
                                fraccion = (saldo_interes / (saldo_interes + saldo_iva_interes));
                                myLogger.info("fraccion " + fraccion);
                                double pago_interes_fraccion = FormatUtil.roundDecimal(remanente * fraccion, 2);
                                tablaAmort[i].interesPagado = tablaAmort[i].interesPagado + pago_interes_fraccion;
                                tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteresPagado + remanente - pago_interes_fraccion;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
//								pago_interes = 		pago_interes + 		FormatUtil.roundDecimal(remanente*fraccion,2);
                                pago_interes = pago_interes + pago_interes_fraccion;
//								pago_iva_interes =	pago_iva_interes +	FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente*fraccion,2),2);
                                pago_iva_interes = pago_iva_interes + (remanente - pago_interes_fraccion);
                                myLogger.info("Interes pagado " + tablaAmort[i].interesPagado);
                                myLogger.info("pago interes fraccion " + pago_interes_fraccion);
                                myLogger.info("iva interes pagado " + tablaAmort[i].ivaInteresPagado);
                                myLogger.info(" monto a pagar " + tablaAmort[i].montoPagar);
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                                    pago_interes_vencido = pago_interes_vencido + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                    pago_iva_interes_vencido = pago_iva_interes_vencido + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                                } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_interes_vencido_90 = pago_interes_vencido_90 + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                    pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                                    //								pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                                } else {
                                    pago_interes_vigente = pago_interes_vigente + saldo_interes;
                                }
                                remanente = 0;
                            }
                        }
                        // CAPITAL					
                        if (saldo_capital > 0 && remanente > 0) {
                            if (remanente >= (saldo_capital)) {
                                tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_capital;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_capital;
                                remanente = remanente - saldo_capital;
                                pago_capital = pago_capital + saldo_capital;
                                tablaAmort[i].pagado = "S";
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + saldo_capital;
                                }
                            } else {
                                tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + remanente;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                                pago_capital = pago_capital + remanente;
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + remanente;
                                }
                                remanente = 0;
//									break;
                            }

                        }

                        // INTERES MORATORIO
                    } else if (saldo_mora > 0 && remanente > 0) {
                        myLogger.info("Entra a pago de mora remantente " + remanente);
                        if (remanente >= (saldo_mora + saldo_iva_mora)) {
                            tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorio;
                            tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorio;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_mora + saldo_iva_mora;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - (saldo_mora + saldo_iva_mora);
                            remanente = remanente - (saldo_mora + saldo_iva_mora);
                            pago_mora = pago_mora + saldo_mora;
                            pago_iva_mora = pago_iva_mora + saldo_iva_mora;
                            myLogger.info("pago_interes " + pago_mora);
                            myLogger.info("interes_pagado " + tablaAmort[i].intMoratorioPagado);
                            myLogger.info("pago_iva_mora " + pago_iva_mora);
                            myLogger.info("iva_mora_pagado " + tablaAmort[i].ivaIntMoratorioPagado);
                            myLogger.info("montoPagar " + tablaAmort[i].montoPagar);
                            myLogger.info("remanente despues de pago mora total " + remanente + " total a pagar " + tablaAmort[i].montoPagar);
                            myLogger.info("pago capital vencido posterior a pago mora " + pago_capital_vencido);
                        } else {
                            // Se calcula la fraccion
//							fraccion = FormatUtil.roundDecimal((saldo_mora / (saldo_mora + saldo_iva_mora)),2);
                            fraccion = saldo_mora / (saldo_mora + saldo_iva_mora);
                            double pago_interes_fraccion = FormatUtil.roundDecimal(remanente * fraccion, 2);
                            tablaAmort[i].intMoratorioPagado = tablaAmort[i].intMoratorioPagado + pago_interes_fraccion;
                            tablaAmort[i].ivaIntMoratorioPagado = tablaAmort[i].ivaIntMoratorioPagado + remanente - pago_interes_fraccion;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                            pago_mora = pago_mora + FormatUtil.roundDecimal(remanente * fraccion, 2);
                            pago_iva_mora = pago_iva_mora + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                            remanente = 0;
                        }
                        // INTERES						
                        if (saldo_interes > 0 && remanente > 0) {
                            if (remanente >= (saldo_interes + saldo_iva_interes)) {
                                tablaAmort[i].interesPagado = tablaAmort[i].interes;
                                tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteres;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_interes + saldo_iva_interes;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_interes - saldo_iva_interes;
                                pago_interes = pago_interes + saldo_interes;
                                pago_iva_interes = pago_iva_interes + saldo_iva_interes;
                                remanente = remanente - saldo_interes - saldo_iva_interes;
                                myLogger.info("pago capital vencido posterior a pago interes " + pago_capital_vencido);
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                                    pago_interes_vencido = pago_interes_vencido + saldo_interes;
                                    pago_iva_interes_vencido = pago_iva_interes_vencido + saldo_iva_interes;
                                } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_interes_vencido_90 = pago_interes_vencido_90 + saldo_interes;
                                    pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + saldo_iva_interes;
//									pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                                } else {
                                    pago_interes_vigente = pago_interes_vigente + saldo_interes;
                                }
                            } else {
                                // Se calcula la fraccion
//								fraccion = FormatUtil.roundDecimal((saldo_interes / (saldo_interes + saldo_iva_interes)),2);
                                fraccion = saldo_interes / (saldo_interes + saldo_iva_interes);
                                double pago_interes_fraccion = FormatUtil.roundDecimal(remanente * fraccion, 2);
                                tablaAmort[i].interesPagado = tablaAmort[i].interesPagado + pago_interes_fraccion;
//								tablaAmort[i].interesPagado    =   tablaAmort[i].interesPagado    +  FormatUtil.roundDecimal(remanente*fraccion,2);
//								tablaAmort[i].ivaInteresPagado =   tablaAmort[i].ivaInteresPagado +  FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente*fraccion,2),2);
                                tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteresPagado + remanente - pago_interes_fraccion;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                                pago_interes = pago_interes + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                pago_iva_interes = pago_iva_interes + remanente - pago_interes_fraccion;
                                myLogger.info("Interes pagado " + tablaAmort[i].interesPagado);
                                myLogger.info("pago interes fraccion " + pago_interes_fraccion);
                                myLogger.info("iva interes pagado " + tablaAmort[i].ivaInteresPagado);
                                myLogger.info(" monto a pagar " + tablaAmort[i].montoPagar);
                                myLogger.info("pago capital vencido posterior a pago interes " + pago_capital_vencido);
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
//									pago_interes_vencido = pago_interes_vencido + FormatUtil.roundDecimal(remanente*fraccion,2);
                                    pago_interes_vencido = pago_interes_vencido + pago_interes_fraccion;
//									pago_iva_interes_vencido = pago_iva_interes_vencido + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente*fraccion,2),2);
                                    pago_iva_interes_vencido = pago_iva_interes_vencido + remanente - pago_interes_fraccion;
                                } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_interes_vencido_90 = pago_interes_vencido_90 + pago_interes_fraccion;
                                    pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + remanente - pago_interes_fraccion;
//									pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                                } else {
                                    pago_interes_vigente = pago_interes_vigente + saldo_interes;
                                }
                                remanente = 0;
                            }
                        }
                        // CAPITAL					
                        if (saldo_capital > 0 && remanente > 0) {
                            myLogger.info("Entra a pago de capital en seccion mora remantente " + remanente);
                            if (remanente >= (saldo_capital)) {
                                tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_capital;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_capital;
                                remanente = remanente - saldo_capital;
                                pago_capital = pago_capital + saldo_capital;
                                tablaAmort[i].pagado = "S";
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + saldo_capital;
                                }
                            } else {
                                tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + remanente;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                                pago_capital = pago_capital + remanente;
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + remanente;
                                }
                                myLogger.info("pago capital vencido posterior a pago capital " + pago_capital_vencido);
                                remanente = 0;
//									break;
                            }
                        }

                        // INTERES						
                    } else if (saldo_interes > 0 && remanente > 0) {
                        if (remanente >= (saldo_interes + saldo_iva_interes)) {
                            tablaAmort[i].interesPagado = tablaAmort[i].interes;
                            tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteres;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_interes + saldo_iva_interes;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_interes - saldo_iva_interes;
                            pago_interes = pago_interes + saldo_interes;
                            pago_iva_interes = pago_iva_interes + saldo_iva_interes;
                            remanente = remanente - saldo_interes - saldo_iva_interes;
                            if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                                pago_interes_vencido = pago_interes_vencido + saldo_interes;
                                pago_iva_interes_vencido = pago_iva_interes_vencido + saldo_iva_interes;
                            } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                pago_interes_vencido_90 = pago_interes_vencido_90 + saldo_interes;
                                pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + saldo_iva_interes;
//								pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                            } else {
                                pago_interes_vigente = pago_interes_vigente + saldo_interes;
                            }
                        } else {
                            // Se calcula la fraccion
                            fraccion = (saldo_interes / (saldo_interes + saldo_iva_interes));
                            double pago_interes_fraccion = FormatUtil.roundDecimal(remanente * fraccion, 2);
                            myLogger.info("fraccion " + fraccion);
                            tablaAmort[i].interesPagado = tablaAmort[i].interesPagado + pago_interes_fraccion;
                            tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteresPagado + remanente - pago_interes_fraccion;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                            pago_interes = pago_interes + FormatUtil.roundDecimal(remanente * fraccion, 2);
//							pago_iva_interes =	pago_iva_interes +	FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente*fraccion,2),2);
                            pago_iva_interes = pago_iva_interes + remanente - pago_interes_fraccion;
                            myLogger.info("pago_interes " + pago_interes);
                            myLogger.info("interes_pagado " + tablaAmort[i].interesPagado);
                            myLogger.info("pago_iva_interes " + pago_iva_interes);
                            myLogger.info("ivainteres_pagado " + tablaAmort[i].ivaInteresPagado);
                            myLogger.info("montoPagar " + tablaAmort[i].montoPagar);
                            if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO) {
                                pago_interes_vencido = pago_interes_vencido + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                pago_iva_interes_vencido = pago_iva_interes_vencido + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
                            } else if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                pago_interes_vencido_90 = pago_interes_vencido_90 + FormatUtil.roundDecimal(remanente * fraccion, 2);
                                pago_iva_interes_vencido_90 = pago_iva_interes_vencido_90 + FormatUtil.roundDecimal(remanente - FormatUtil.roundDecimal(remanente * fraccion, 2), 2);
//								pago_capital_vencido	 = pago_capital_vencido + pago_capital;							
                            } else {
                                pago_interes_vigente = pago_interes_vigente + saldo_interes;
                            }
                            remanente = 0;
                        }
                        // CAPITAL					
                        if (saldo_capital > 0 && remanente > 0) {
                            if (remanente >= (saldo_capital)) {
                                tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_capital;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_capital;
                                remanente = remanente - saldo_capital;
                                pago_capital = pago_capital + saldo_capital;
                                tablaAmort[i].pagado = "S";
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + saldo_capital;
                                }
                            } else {
                                tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + remanente;
                                tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                                tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                                pago_capital = pago_capital + remanente;
                                if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                    pago_capital_vencido = pago_capital_vencido + remanente;
                                }
                                remanente = 0;
//								break;
                            }

                        }
                        // CAPITAL					
                    } else if (saldo_capital > 0 && remanente > 0) {
                        if (remanente >= (saldo_capital)) {
                            tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + saldo_capital;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - saldo_capital;
                            remanente = remanente - saldo_capital;
                            pago_capital = pago_capital + saldo_capital;
                            tablaAmort[i].pagado = "S";
                            if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                pago_capital_vencido = pago_capital_vencido + saldo_capital;
                            }
                        } else {
                            tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + remanente;
                            tablaAmort[i].totalPagado = tablaAmort[i].totalPagado + remanente;
                            tablaAmort[i].montoPagar = tablaAmort[i].montoPagar - remanente;
                            pago_capital = pago_capital + remanente;
                            if (tablaAmort[i].status == ClientesConstants.DIVIDENDO_MOROSO || tablaAmort[i].status == ClientesConstants.DIVIDENDO_VENCIDO) {
                                pago_capital_vencido = pago_capital_vencido + remanente;
                            }
                            remanente = 0;
                            //							break;
                        }
                    }
                } else {
                    break;
                }
// Registra el evento de aplicacion de pago con el monto realmente aplicado
                //eventohelper.registraPago(credito, tablaAmort[i], monto_aplicado, fechaFin, (saldo_total_aldia - (pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa)));
                eventohelper.registraPago(credito, tablaAmort[i], monto_aplicado, fechaFin, (saldo_con_interes_final - (pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa)));

            }
            /* Pasamos a actualizar la tabla de amortizacion con los nuevos valores
             * Se inicializan de nuevo las variables que totalizan para actualizar la tabla de saldos
             */
            saldo_capital = 0;
            saldo_interes = 0;
            saldo_iva_interes = 0;
            saldo_mora = 0;
            saldo_iva_mora = 0;
            saldo_multa = 0;
            saldo_iva_multa = 0;
            pago_total = pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa;
            monto_a_pagar = 0;
            saldo_total_vencido = 0;
            cuotas_vencidas = 0;
            dias_mora = 0;
            fecha_mora = null;
            saldo_con_interes_final = 0;
            System.out.println("PAGO TOTAL: " + pago_total);
            System.out.println("PAGO CAPITAL: " + pago_capital);

            for (int i = 0; i < tablaAmort.length; i++) {
                TablaAmortDAO tablaDAOAct = new TablaAmortDAO();
                /* Se actualiza la tabla de amortizacion 
                 * 				
                 */
                int rs = tablaDAOAct.updateSaldosTablaAmort(tablaAmort[i]);
                /* Se lleva el control de los pagos
                 * 				
                 */
                saldo_capital = saldo_capital + tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                saldo_interes = saldo_interes + tablaAmort[i].interes - tablaAmort[i].interesPagado;
                saldo_iva_interes = saldo_iva_interes + tablaAmort[i].ivaInteres - tablaAmort[i].ivaInteresPagado;

                if (tablaAmort[i].status > 1 && tablaAmort[i].montoPagar > 0) {
                    // Se calcula la fecha donde inicia la mora
                    if (fecha_mora == null) {
                        fecha_mora = tablaAmort[i].fechaPago;
                        dias_mora = FechasUtil.inBetweenDays(fecha_mora, fechaFin);
                    }

                    saldo_mora = saldo_mora + tablaAmort[i].intMoratorio - tablaAmort[i].intMoratorioPagado;
                    saldo_iva_mora = saldo_iva_mora + tablaAmort[i].ivaIntMoratorio - tablaAmort[i].ivaIntMoratorioPagado;
                    saldo_multa = saldo_multa + tablaAmort[i].multa - tablaAmort[i].multaPagado;
                    saldo_iva_multa = saldo_iva_multa + tablaAmort[i].ivaMulta - tablaAmort[i].ivaMultaPagado;
                    saldo_total_vencido = saldo_capital + saldo_interes + saldo_iva_interes + saldo_mora + saldo_iva_mora + saldo_multa + saldo_iva_multa;
                    cuotas_vencidas = cuotas_vencidas + 1;
                } else if (tablaAmort[i].status == 1) {
                    saldo_capital_vigente = saldo_capital_vigente + tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                    saldo_interes_vig = saldo_interes_vig + tablaAmort[i].interes - tablaAmort[i].interesPagado;
                    saldo_iva_interes = saldo_iva_interes + tablaAmort[i].ivaInteres - tablaAmort[i].ivaInteresPagado;
                }
                saldo_insoluto = saldo_interes + saldo_iva_interes;
                saldo_con_interes_final = saldo_interes + saldo_iva_interes;

            }
            System.out.println("ESTATUS CREDITO TABLAAMORT: " + credito.getStatus());
            /*
             * Se actualiza la tabla de saldos			
             */
            myLogger.info("pago capital " + pago_capital + "pago_interes " + pago_interes + "pago_iva_interes " + pago_iva_interes + ", pago_mora " + pago_mora + " , pago iva_mora " + pago_iva_mora + " , pago_capital_vencido " + pago_capital_vencido + " ,pago iva interes vencido " + pago_iva_interes_vencido + ", pago iva int vencido 90 " + pago_iva_interes_vencido_90 + "pago interes vencido 90 " + pago_interes_vencido_90);
            myLogger.info("saldo iva interes vencido " + saldoVO.getIvaInteresVencido());
            myLogger.info("interes vencido " + saldoVO.getInteresVencido());
            myLogger.info("Saldo interes vencido " + saldoVO.getSaldoInteresVencido());
            myLogger.info("pago interes vencido" + pago_interes_vencido);
            saldoVO.setCapitalPagado(saldoVO.getCapitalPagado() + pago_capital);
            saldoVO.setInteresNormalPagado(saldoVO.getInteresNormalPagado() + pago_interes);
            saldoVO.setIvaInteresNormalPagado(saldoVO.getIvaInteresNormalPagado() + pago_iva_interes);
            saldoVO.setMoratorioPagado(saldoVO.getMoratorioPagado() + pago_mora);
            saldoVO.setIvaMoraPagado(saldoVO.getIvaMoraPagado() + pago_iva_mora);
            saldoVO.setMultaPagada(saldoVO.getMultaPagada() + pago_multa);
            saldoVO.setIvaMultaPagado(saldoVO.getIvaMultaPagado() + pago_iva_multa);
            saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - pago_capital);
            saldoVO.setSaldoInteres(saldoVO.getSaldoInteres() - pago_interes - diferencia_interes);
            saldoVO.setSaldoInteresVigente(saldoVO.getSaldoInteresVigente() - pago_interes_vigente - diferencia_interes);
            saldoVO.setSaldoIvaInteres(saldoVO.getSaldoIvaInteres() - pago_iva_interes - diferencia_iva_interes);
            saldoVO.setSaldoMora(saldoVO.getSaldoMora() - pago_mora);
            saldoVO.setSaldoIVAMora(saldoVO.getSaldoIVAMora() - pago_iva_mora);
            saldoVO.setSaldoMulta(saldoVO.getSaldoMulta() - pago_multa);
            saldoVO.setSaldoIVAMulta(saldoVO.getSaldoIVAMulta() - pago_iva_multa);
            saldoVO.setCapitalVencido(saldoVO.getCapitalVencido() - pago_capital_vencido);
            if (credito.getStatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                saldoVO.setSaldoInteresCtasOrden(saldoVO.getSaldoInteresCtasOrden() - pago_interes_vigente - pago_interes_vencido);
                saldoVO.setSaldoInteresVencido(saldoVO.getSaldoInteresVencido());
                saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido() - pago_iva_interes_vencido);
                saldoVO.setSaldoInteresVencido90dias(saldoVO.getSaldoInteresVencido90dias() - pago_interes_vencido_90);
                if (saldoVO.getDiasMora() > 0) { // Unicamente cuando esta vencido y tiene saldo vencido (puede estar al corriente pero no en estado vigente por no llegar a pago sostenido JBL SEP-14
                    saldoVO.setInteresVencido(saldoVO.getInteresVencido() - pago_interes_vencido_90 - pago_interes_vencido - pago_interes_vigente);
                    saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido() - pago_iva_interes_vencido_90 - pago_iva_interes_vencido - pago_iva_interes_vigente);
                } else {
                    saldoVO.setInteresVencido(saldoVO.getInteresVencido() - pago_interes_vencido_90 - pago_interes_vencido);
                    saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido() - pago_iva_interes_vencido_90 - pago_iva_interes_vencido);
                }
            } else {
                saldoVO.setSaldoInteresVencido(saldoVO.getSaldoInteresVencido() - pago_interes_vencido);
                saldoVO.setSaldoInteresVencido90dias(saldoVO.getSaldoInteresVencido90dias() - pago_interes_vencido_90);
                saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido() - pago_iva_interes_vencido - pago_iva_interes_vencido_90);
                //saldoVO.setSaldoInteresVencido90dias(saldoVO.getSaldoInteresVencido90dias());
            }//			saldoVO.setInteresVencido(saldoVO.getInteresVencido()- pago_interes_vencido_90);
            saldoVO.setInteresVencido(saldoVO.getInteresVencido() - pago_interes_vencido);
            saldoVO.setCtaContable(saldoVO.getCtaContable());
            myLogger.info("interes vencido actualizado" + saldoVO.getInteresVencido());
            myLogger.info("Saldo interes vencido actualizado " + saldoVO.getSaldoInteresVencido());
            // la fecha de pago se toma como la fecha de aplicacion
            saldoVO.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            if (saldoVO.getTotalVencido() - (pago_capital_vencido + pago_interes_vencido + pago_iva_interes_vencido + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa) < 0) {
                saldoVO.setTotalVencido(0);
            } else {
                if (credito.getStatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                    if (saldoVO.getDiasMora() > 0) // Unicamente cuando esta vencido y tiene saldo vencido (puede estar al corriente pero no en estado vigente por no llegar a pago sostenido JBL SEP-14
                    {
                        saldoVO.setTotalVencido(saldoVO.getTotalVencido() - (pago_capital_vencido + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa + pago_interes_vencido_90 + pago_iva_interes_vencido_90 + pago_interes_vencido + pago_iva_interes_vencido + pago_interes_vigente + pago_iva_interes_vigente));
                    } else // No se toma en cuenta el pago interes vigente para el saldo vencido JBL SEP-14
                    {
                        saldoVO.setTotalVencido(saldoVO.getTotalVencido() - (pago_capital_vencido + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa + pago_interes_vencido_90 + pago_iva_interes_vencido_90 + pago_interes_vencido + pago_iva_interes_vencido));
                    }

                } else {
                    saldoVO.setTotalVencido(saldoVO.getTotalVencido() - (pago_capital_vencido + pago_interes_vencido + pago_iva_interes_vencido + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa));
                }
            }
//			saldoVO.setFechaGeneracion(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(c1.getTime()));
            //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaCierre));
            saldoVO.setSaldoConInteresAlFinal(saldoVO.getSaldoConInteresAlFinal() - (pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa));
            saldoVO.setSaldoTotalAlDia(saldo_total_aldia - (pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa));
            saldoVO.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldoVO.getReferencia()));
            saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_total);
            saldoVO.setSaldoBucket(remanente + credito.getMontoCuentaCongelada());
            if (saldo_total_aldia - (pago_capital + pago_interes + pago_iva_interes + pago_mora + pago_iva_mora + pago_multa + pago_iva_multa) <= .2) { // Se aplica una tolerancia de 20 centavos
                saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
                saldoVO.setDiasMora(0);
                saldoVO.setCuotasVencidas(0);
            } else if (remanente > 0 || saldo_total_vencido <= 0 && saldoVO.getEstatus() < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                saldoVO.setDiasMora(0);
                saldoVO.setCuotasVencidas(0);
            } else {
                saldoVO.setEstatus(status);
                saldoVO.setDiasMora(dias_mora);
                saldoVO.setCuotasVencidas(cuotas_vencidas);
            }
            if(saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO){
                devengamiento = devengDAO.getDevengamientoLiquidar(credito, null);
                ArrayList<RubrosVO> array_dev = new ArrayList<RubrosVO>();
                rubro = new RubrosVO();
                RubrosVO elementos_dev[] = null;
                rubro.tipoRubro = ClientesConstants.INTERES;
                rubro.monto = devengamiento.getInteres();
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro.monto = devengamiento.getIvaInteres();
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);
                if (array_dev.size() > 0) {
                    elementos_dev = new RubrosVO[array_dev.size()];
                    for (int i = 0; i < elementos_dev.length; i++) {
                        elementos_dev[i] = (RubrosVO) array_dev.get(i);
                    }
                }
                if (elementos_dev.length > 0) {
                    transacHelper.registraProvision(credito, elementos_dev, Convertidor.toSqlDate(fechaCierre));
                }
                devengDAO.setDevengamientoAnticipado(credito);
                saldoVO.setInteresPorDevengar(0);
                saldoVO.setIvaInteresPorDevengar(0);
                saldoVO.setInteresDevengados(saldoVO.getInteresDevengados()+devengamiento.getInteres());
                saldoVO.setIvaInteresDevengados(saldoVO.getIvaInteresDevengados()+devengamiento.getIvaInteres());
            }
            myLogger.info("va a actualizar saldo de referencia " + saldoVO.getReferencia());
            // Se actualiza la tabla de saldos
            saldoIBS.updateSaldo(saldoVO);
            /*
             * Se actualiza la tabla de credito			
             */
            myLogger.info("llega a actualizacion de credito");
            credito.setStatus(saldoVO.getEstatus());
            credito.setNumDiasMora(dias_mora);
            credito.setMontoCuenta(remanente + credito.getMontoCuentaCongelada());
            credito.setFechaUltimaActualizacion(Convertidor.toSqlDate(c1.getTime()));
            credito.setFechaUltimoPago(Convertidor.toSqlDate(c1.getTime()));

            // SE ACTUALIZA EL MONTO DE LA CUENTA EN TABLA DE CREDITOS
            CreditoHelperCartera.actualizaSaldoCuenta(credito);
            if(credito.getAplicaGarantia() && credito.getMontoCuentaCongelada() == 0){
                transacHelper.aplicaPagoGarantia(credito, fechaCierre, credito.getAplicaMontoGarantia());
                credito.setAplicaGarantia(false);
            }

            /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
             */
            // se llenan los montos de los rubros
            ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
            rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            if (pago_total > 0) {
                rubro.tipoRubro = ClientesConstants.EFECTIVO;
                rubro.monto = pago_total;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.info("Array pago 1" + array_pago.toString());
            if (pago_capital > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.CAPITAL;
                rubro.monto = pago_capital;
                if (saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                    rubro.status = ClientesConstants.RUBRO_VENCIDO;
                } else {
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                }
                array_pago.add(rubro);
            }
            myLogger.info("Array pago 2" + array_pago.toString());
            if (pago_interes > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.INTERES;
                if (saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                    if (pago_interes_vencido_90 > 0) {
                        rubro.status = ClientesConstants.RUBRO_VENCIDO;
                        rubro.monto = pago_interes_vencido_90;
                        array_pago.add(rubro);
                    }
                    if (pago_interes_vigente + pago_interes_vencido > 0) {
                        rubro = new RubrosVO();
                        rubro.tipoRubro = ClientesConstants.INTERES;
                        rubro.status = ClientesConstants.RUBRO_RESOLUCION;
                        rubro.monto = pago_interes_vencido + pago_interes_vigente;
                        array_pago.add(rubro);
                    }
                } else {
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    rubro.monto = pago_interes;
                    array_pago.add(rubro);
                }
            }
            myLogger.info("Array pago  3 " + array_pago.toString());
            if (pago_mora > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.INTERES_MORATORIO;
                rubro.monto = pago_mora;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            if (pago_multa > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.MULTA;
                rubro.monto = pago_multa;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            if (pago_iva_interes > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro.monto = pago_iva_interes;
                if (saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                    rubro.status = ClientesConstants.RUBRO_VENCIDO;
                } else {
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                }
                array_pago.add(rubro);
            }
            myLogger.info("Array pago 4" + array_pago.toString());
            if (pago_iva_mora > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES_MORATORIO;
                rubro.monto = pago_iva_mora;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            if (pago_iva_multa > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_MULTA;
                rubro.monto = pago_iva_multa;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            // En caso de estar el credito vencido se incluyen los rubros respectivos
            myLogger.info("rubros :(" + array_pago.toString());
            if (array_pago.size() > 0) {
                myLogger.info("Tiene pagos");
                elementos = new RubrosVO[array_pago.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_pago.get(i);
                }
            }
            myLogger.info(" va a checar si hay elementos");
            if (elementos != null) {
                if (elementos.length > 0) {
                    myLogger.info("Hay elementos");
                    transacHelper.aplicaPago(credito, elementos, fechaCierre);
                }
            }
            /*SE DESACTIVA EL DEVENGAMIENTO SEMANAL PARA REALIZARLO DIARIO
            // En caso de que haya sido pago para interes acumulado se debe de devengar el interes pagado
            if (acumulado) {
                ArrayList<RubrosVO> array_dev = new ArrayList<RubrosVO>();
                rubro = new RubrosVO();
                RubrosVO elementos_dev[] = null;
                rubro.tipoRubro = ClientesConstants.INTERES;
                rubro.monto = pago_interes_vigente;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro.monto = pago_iva_interes;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);

                if (array_dev.size() > 0) {
                    elementos_dev = new RubrosVO[array_dev.size()];
                    for (int i = 0; i < elementos_dev.length; i++) {
                        elementos_dev[i] = (RubrosVO) array_dev.get(i);
                    }
                }

                if (elementos_dev.length > 0) {
                    transacHelper.registraProvision(credito, elementos_dev, c1.getTime());
                }
            }*/

        } catch (Exception e) {
            try {
                myLogger.debug("envio de mail por error en el cierre");
                Mail.enviaCorreo("Error en Cierre", e + "", Destinatarios);
            } catch (Exception maile) {
                myLogger.error("Envio mail error", maile);
            }
            myLogger.error("aplicaPagosTabla", e);
            throw new ClientesException(e.getMessage());
        }
        return remanente;
    }

    public static double liquidaTabla(CreditoCartVO credito, double pago, Date fechaFin) throws ClientesException {

        //ejecuta 
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO tablaDivAnterior = null;
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        EventoHelper eventohelper = new EventoHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        PagoDAO pagoDAO = new PagoDAO();
        saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
        double remanente = pago;
        double saldo_capital = 0.00;
        double saldo_capital_vigente = 0.00;
        double saldo_capital_vencido = 0.00;
        double saldo_interes = 0.00;
        double saldo_iva_interes = 0.00;
        double saldo_interes_vig = 0.00;
        double saldo_iva_interes_vig = 0.00;
        double saldo_interes_vencido = 0.00;
        double saldo_iva_interes_vencido = 0.00;
        double saldo_mora = 0.00;
        double saldo_iva_mora = 0.00;
        double saldo_multa = 0.00;
        double saldo_iva_multa = 0.00;
        double saldo_total_vencido = 0.00;
        double saldo_total_aldia = 0.00;
        double saldo_insoluto = 0.00;
        double saldo_con_interes_final = 0.00;
        double pago_capital = 0.00;
        double pago_capital_vencido = 0.00;
        double pago_interes = 0.00;
        double pago_iva_interes = 0.00;
        double pago_interes_vigente = 0.00;
        double pago_interes_vencido = 0.00;
        double pago_iva_interes_vencido = 0.00;
        double pago_interes_vencido_90 = 0.00;
        double pago_iva_interes_vencido_90 = 0.00;
        double pago_interes_cta_orden = 0.00;
        double pago_mora = 0.00;
        double pago_iva_mora = 0.00;
        double pago_multa = 0.00;
        double pago_iva_multa = 0.00;
        double pago_total = 0.00;
        double monto_a_pagar = 0.00;
        double monto_aplicado = 0.00;
        double fraccion = 0.00;
        Calendar c1 = Calendar.getInstance();
        String clsDate = CatalogoHelper.getParametro("FECHA_CIERRE");
        int status = 1;
        int dias_mora = 0;
        int dias_dividendo = 0;
        int cuotas_vencidas = 0;
        Date fecha_mora = null;
// Se obtienen datos de saldos con los cuales se trabajaran		
        saldo_total_aldia = saldoVO.getSaldoTotalAlDia();
        status = saldoVO.getEstatus();

        try {
            tablaAmort = tablaDAO.getDivPagoNoVigente(credito.getNumCliente(), credito.getNumCredito());
            for (int i = 0; i < tablaAmort.length; i++) {
                saldo_capital = tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                monto_aplicado = 0;
                // Se obtiene el monto a pagar total del dividendo
                if (remanente >= tablaAmort[i].abonoCapital) {
                    if (tablaAmort[i].totalPagado == 0) {
                        // Se igualan los montos a pagados con el saldo inicial
                        tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                        tablaAmort[i].totalPagado = tablaAmort[i].capitalPagado;
                        remanente = remanente - tablaAmort[i].capitalPagado;
                        tablaAmort[i].montoPagar = 0;
                        tablaAmort[i].pagado = "S";
                        /*
                         * Se mete termporalemte, esto debe de retirarse mas adelante JBL NOV-13
                         */
                        tablaAmort[i].status = 1;
                        // se lleva un control sobre el total pagado
                        pago_capital = pago_capital + tablaAmort[i].capitalPagado;
                        monto_aplicado = tablaAmort[i].totalPagado;

                    } // Se realizara unicamente un pago parcial						
                } else {
                    tablaAmort[i].capitalPagado = tablaAmort[i].capitalPagado + saldo_capital;
                    tablaAmort[i].totalPagado = tablaAmort[i].capitalPagado;
                    remanente = remanente - saldo_capital;
                    tablaAmort[i].montoPagar = 0;
                    tablaAmort[i].pagado = "S";
                    /*
                     * Se mete termporalemte por cuestion de reportes, esto debe de retirarse mas adelante. JBL NOV-13
                     */
                    tablaAmort[i].status = 1;
                    // se lleva un control sobre el total pagado
                    pago_capital = pago_capital + saldo_capital;

                }
// Registra el evento de aplicacion de pago con el monto realmente aplicado
                eventohelper.registraPago(credito, tablaAmort[i], monto_aplicado, fechaFin, 0);

            }
            /* Pasamos a actualizar la tabla de amortizacion con los nuevos valores
             * Se inicializan de nuevo las variables que totalizan para actualizar la tabla de saldos
             */
            saldo_capital = 0;
            saldo_interes = 0;
            saldo_iva_interes = 0;
            saldo_mora = 0;
            saldo_iva_mora = 0;
            saldo_multa = 0;
            saldo_iva_multa = 0;
            pago_total = pago_capital;
            monto_a_pagar = 0;
            saldo_total_vencido = 0;
            cuotas_vencidas = 0;
            dias_mora = 0;
            fecha_mora = null;

            for (int i = 0; i < tablaAmort.length; i++) {
                TablaAmortDAO tablaDAOAct = new TablaAmortDAO();
                /* Se actualiza la tabla de amortizacion 
                 * 				
                 */
                int rs = tablaDAOAct.updateSaldosTablaAmort(tablaAmort[i]);
                /* Se lleva el control de los pagos
                 * 				
                 */
                saldo_capital = saldo_capital + tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                saldo_con_interes_final = saldo_interes + saldo_iva_interes;

            }
            /*
             * Se actualiza la tabla de saldos			
             */
            myLogger.info("pago capital " + pago_capital + "pago_interes " + pago_interes + "pago_iva_interes " + pago_iva_interes + "pago_mora " + pago_mora + "pago iva_mora " + pago_iva_mora);

            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldoVO.setCapitalPagado(saldoVO.getCapitalPagado() + pago_capital);
            saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - pago_capital);
//			saldoVO.setCapitalVencido(saldo_capital_vencido);
//			saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido()- pago_iva_interes_vencido);
            // la fecha de pago se toma como la fecha de aplicacion
            saldoVO.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            saldoVO.setTotalVencido(0);
            saldoVO.setSaldoConInteresAlFinal(0);
            saldoVO.setSaldoTotalAlDia(0);
            saldoVO.setSaldoInteres(0);
            saldoVO.setSaldoInteresVigente(0);
            saldoVO.setSaldoIvaInteres(0);
            saldoVO.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldoVO.getReferencia()));
            saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_total);
            saldoVO.setSaldoBucket(remanente + credito.getMontoCuentaCongelada());
            saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            saldoVO.setDiasMora(0);
            saldoVO.setCuotasVencidas(0);

            myLogger.info("va a actualizar saldo de referencia " + saldoVO.getReferencia());
            // Se actualiza la tabla de saldos
            saldoIBS.updateSaldo(saldoVO);
            /*
             * Se actualiza la tabla de credito			
             */
            myLogger.info("llega a actualizacion de credito");
            credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            credito.setNumDiasMora(dias_mora);
            credito.setMontoCuenta(remanente + credito.getMontoCuentaCongelada());
            credito.setFechaUltimaActualizacion(Convertidor.toSqlDate(c1.getTime()));
            credito.setFechaUltimoPago(Convertidor.toSqlDate(c1.getTime()));
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada());

            // SE ACTUALIZA EL MONTO DE LA CUENTA EN TABLA DE CREDITOS
            CreditoHelperCartera.actualizaSaldoCuenta(credito);

            /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
             */
            // se llenan los montos de los rubros
            ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            if (pago_total > 0) {
                rubro.tipoRubro = ClientesConstants.EFECTIVO;
                rubro.monto = pago_total;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.info("Array pago " + array_pago.toString());
            if (pago_capital > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.CAPITAL;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.info("rubros " + array_pago.toString());
            if (array_pago.size() > 0) {
                elementos = new RubrosVO[array_pago.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_pago.get(i);
                }
            }
            transacHelper.aplicaPago(credito, elementos, fechaFin);

        } catch (Exception e) {
            myLogger.error("liquidaTabla", e);
            throw new ClientesException(e.getMessage());
        }
        return remanente;
    }

    public static double liquidaTablaProyectado(CreditoCartVO credito, double pago, Date fechaFin) throws ClientesException {

        //ejecuta 
        DevengamientoVO devengamiento = new DevengamientoVO();
        RubrosVO rubro = new RubrosVO();
        TablaAmortVO[] tablaAmort = null;
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        DevengamientoDAO devengDAO = new DevengamientoDAO();
        EventoHelper eventohelper = new EventoHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        double remanente = pago;
        double saldo_capital = 0;
        double saldo_interes = 0;
        double saldo_iva_interes = 0;
        double saldo_interes_vig = 0;
        double saldo_iva_interes_vig = 0;
        double saldo_interes_vencido = 0;
        double saldo_iva_interes_vencido = 0;
        double saldo_mora = 0;
        double saldo_iva_mora = 0;
        double saldo_multa = 0;
        double saldo_iva_multa = 0;
        double saldo_total_vencido = 0;
        double saldo_total_aldia = 0;
        double saldo_insoluto = 0;
        double saldo_con_interes_final = 0;
        double pago_capital = 0;
        double pago_capital_vencido = 0;
        double pago_interes = 0;
        double pago_iva_interes = 0;
        double pago_mora = 0;
        double pago_iva_mora = 0;
        double pago_multa = 0;
        double pago_iva_multa = 0;
        double pago_total = 0;
        double monto_a_pagar = 0;
        double monto_aplicado = 0;
        double fraccion = 0;
        Calendar c1 = Calendar.getInstance();
        int status = 1;
        int dias_mora = 0;
        int cuotas_vencidas = 0;
        Date fecha_mora = null;

        try {
            tablaAmort = tablaDAO.getDivPagoNoVigente(credito.getNumCliente(), credito.getNumCredito());

            if (tablaAmort != null) {
                for (int i = 0; i < tablaAmort.length; i++) {
                    saldo_capital = tablaAmort[i].abonoCapital - tablaAmort[i].capitalPagado;
                    monto_aplicado = 0;
                    // Se obtiene el monto a pagar total del dividendo
                    myLogger.info("Remante dentro del for: " + remanente);
                    if (remanente >= tablaAmort[i].montoPagar) {
                        if (tablaAmort[i].totalPagado == 0) {
                            // Se igualan los montos a pagados con el saldo inicial
                            tablaAmort[i].capitalPagado = tablaAmort[i].abonoCapital;
                            tablaAmort[i].interesPagado = tablaAmort[i].interes;
                            tablaAmort[i].ivaInteresPagado = tablaAmort[i].ivaInteres;
                            //tablaAmort[i].totalPagado = tablaAmort[i].capitalPagado + tablaAmort[i].interesPagado + tablaAmort[i].ivaInteresPagado;
                            tablaAmort[i].totalPagado = tablaAmort[i].montoPagar;
                            myLogger.info("Calcula remanente = remanente " + remanente + "- tablaAmort[i] " + tablaAmort[i].totalPagado);
                            remanente = remanente - tablaAmort[i].totalPagado;
                            tablaAmort[i].montoPagar = 0;
                            tablaAmort[i].status = 1;
                            tablaAmort[i].pagado = "S";
                            // se lleva un control sobre el total pagado
                            pago_capital = pago_capital + tablaAmort[i].capitalPagado;
                            pago_interes = pago_interes + tablaAmort[i].interesPagado;
                            pago_iva_interes = pago_iva_interes + tablaAmort[i].ivaInteres;
                            monto_aplicado = tablaAmort[i].totalPagado;

                        } // Se realizara unicamente un pago parcial						
                    } else {
                        myLogger.info("error el calculo de liquidacion remanente " + remanente);
                        break;
                    }
                    // 				Registra el evento de aplicacion de pago con el monto realmente aplicado
                    myLogger.info("liquida tabla proyectado");
                    myLogger.info("monto_aplicado **** " + monto_aplicado);
                    myLogger.info("fechaFin **** " + fechaFin);

                    eventohelper.registraPago(credito, tablaAmort[i], monto_aplicado, fechaFin, 0);

                }
            }
            /* Pasamos a actualizar la tabla de amortizacion con los nuevos valores
             * Se inicializan de nuevo las variables que totalizan para actualizar la tabla de saldos
             */

            saldo_capital = 0;
            saldo_interes = 0;
            saldo_iva_interes = 0;
            saldo_mora = 0;
            saldo_iva_mora = 0;
            saldo_multa = 0;
            saldo_iva_multa = 0;
            pago_total = pago_capital + pago_interes + pago_iva_interes;
            monto_a_pagar = 0;
            saldo_total_vencido = 0;
            cuotas_vencidas = 0;
            dias_mora = 0;
            fecha_mora = null;

            if (tablaAmort != null) {
                myLogger.info("Se actualiza la tabla con los nuevos valores");
                for (int i = 0; i < tablaAmort.length; i++) {
                    TablaAmortDAO tablaDAOAct = new TablaAmortDAO();
                    /* Se actualiza la tabla de amortizacion 
                     * 				
                     */
                    int rs = tablaDAOAct.updateSaldosTablaAmort(tablaAmort[i]);
                }
            }
            /*
             * Se actualiza la tabla de saldos			
             */
            SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
            SaldoIBSVO saldoVO = new SaldoIBSVO();
            PagoDAO pagoDAO = new PagoDAO();
            myLogger.info("pago capital: " + pago_capital + ", pago_interes: " + pago_interes + ", pago_iva_interes: " + pago_iva_interes + ", pago_mora: " + pago_mora + ", pago iva_mora: " + pago_iva_mora);

            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldoVO.setCapitalPagado(saldoVO.getCapitalPagado() + pago_capital);
            saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - pago_capital);
//			saldoVO.setCapitalVencido(saldo_capital_vencido);
//			saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido()- pago_iva_interes_vencido);

            // Modificacion para que la liquidacion Anticipada aplique tambien interes de
            // la ultima semanalidad
            saldoVO.setInteresNormalPagado(saldoVO.getInteresNormalPagado() + pago_interes);
            saldoVO.setIvaInteresNormalPagado(saldoVO.getIvaInteresNormalPagado() + pago_iva_interes);
            //saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_capital + pago_interes + pago_iva_interes);
            // la fecha de pago se toma como la fecha de aplicacion
            saldoVO.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            saldoVO.setCapitalSigAmortizacion(0);
            saldoVO.setInteresSigAmortizacion(0);
            saldoVO.setIvaSigAmortizacion(0);
            saldoVO.setTotalVencido(0);
            saldoVO.setSaldoInteres(0);
            saldoVO.setSaldoIvaInteres(0);
            saldoVO.setSaldoInteresVencido(0);
            saldoVO.setInteresVencido(0);
            saldoVO.setIvaInteresVencido(0);
            saldoVO.setSaldoConInteresAlFinal(0);
//            saldoVO.setSaldoConInteresAlFinal(tablaDAO.getTotalPorpagar(credito.getNumCliente(), credito.getNumCredito()));
            saldoVO.setSaldoTotalAlDia(0);
            saldoVO.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldoVO.getReferencia()));
            saldoVO.setNumeroCuotasTranscurridas(saldoVO.getNumeroCuotasTranscurridas() + 1);
            //saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_total);
            saldoVO.setMontoTotalPagado(tablaDAO.getTotalPagado(credito.getNumCliente(), credito.getNumCredito()));
            saldoVO.setSaldoBucket(remanente + credito.getMontoCuentaCongelada());
            saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            saldoVO.setDiasMora(0);
            saldoVO.setCuotasVencidas(0);

            myLogger.info("va a actualizar saldo de referencia " + saldoVO.getReferencia());
            // Se actualiza la tabla de saldos
            /*
             * Se añade la transacción de la provision de interes que se pago de forma anticipada
             */
            devengamiento = devengDAO.getDevengamientoLiquidar(credito, null);
            if(devengamiento.getInteres() > 0){
                ArrayList<RubrosVO> array_dev = new ArrayList<RubrosVO>();
                rubro = new RubrosVO();
                RubrosVO elementos_dev[] = null;
                rubro.tipoRubro = ClientesConstants.INTERES;
                rubro.monto = devengamiento.getInteres();
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro.monto = devengamiento.getIvaInteres();
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_dev.add(rubro);
                if (array_dev.size() > 0) {
                    elementos_dev = new RubrosVO[array_dev.size()];
                    for (int i = 0; i < elementos_dev.length; i++) {
                        elementos_dev[i] = (RubrosVO) array_dev.get(i);
                    }
                }
                if (elementos_dev.length > 0) {
                    transacHelper.registraProvision(credito, elementos_dev, fechaFin);
                }
                devengDAO.setDevengamientoAnticipado(credito);
                saldoVO.setInteresPorDevengar(0);
                saldoVO.setIvaInteresPorDevengar(0);
                saldoVO.setInteresDevengados(saldoVO.getInteresDevengados()+devengamiento.getInteres());
                saldoVO.setIvaInteresDevengados(saldoVO.getIvaInteresDevengados()+devengamiento.getIvaInteres());
            }
            saldoIBS.updateSaldo(saldoVO);
            /*
             * Se actualiza la tabla de credito			
             */
            myLogger.info("llega a actualizacion de credito");
            credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            credito.setNumDiasMora(dias_mora);
            credito.setMontoCuenta(remanente + credito.getMontoCuentaCongelada());
            credito.setFechaUltimaActualizacion(Convertidor.toSqlDate(c1.getTime()));
            credito.setFechaUltimoPago(Convertidor.toSqlDate(c1.getTime()));
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada());

            // SE ACTUALIZA EL MONTO DE LA CUENTA EN TABLA DE CREDITOS
            CreditoHelperCartera.actualizaSaldoCuenta(credito);
            if(credito.getAplicaGarantia() && credito.getMontoCuentaCongelada() == 0){
                transacHelper.aplicaPagoGarantia(credito, fechaFin, credito.getAplicaMontoGarantia());
                credito.setAplicaGarantia(false);
            }

            /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
             */
            // se llenan los montos de los rubros
            ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
            rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            if (pago_total > 0) {
                rubro.tipoRubro = ClientesConstants.EFECTIVO;
                rubro.monto = pago_total;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.info("Array pago " + array_pago.toString());
            if (pago_capital > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.CAPITAL;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }
            if (pago_interes > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.INTERES;
                rubro.monto = pago_interes;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }
            if (pago_iva_interes > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro.monto = pago_iva_interes;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }

            myLogger.info("rubros " + array_pago.toString());
            if (array_pago.size() > 0) {
                elementos = new RubrosVO[array_pago.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_pago.get(i);
                }
            }
            transacHelper.aplicaPago(credito, elementos, fechaFin);

        } catch (Exception e) {
            myLogger.error("liquidaTablaProyectado", e);
            throw new ClientesException(e.getMessage());
        }
        return remanente;
    }

    public static double aplicaPagoAnticipadoReduccionGrupo(CreditoCartVO credito, CicloGrupalVO ciclo, GrupoVO grupo, double pago, double pagoMontoDesembolsado) throws ClientesException, ParseException {
        //ejecuta 
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO[] tablaNueva = null;
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        EventoHelper eventohelper = new EventoHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        TablaAmortizacionHelper tablaHelper = new TablaAmortizacionHelper();
        String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        Date fechaUltimoCierre  = sdf.parse(ultimaFecha);

        double remanente = 0;
        double saldo_capital = 0;
        double saldo_capital_calc = 0;
        double saldo_interes = 0;
        double saldo_iva_interes = 0;
        double interes_inicial = 0;
        double iva_interes_inicial = 0;
        double diferencia_interes = 0;
        double diferencia_iva_interes = 0;
        double saldo_total_aldia = 0;
        double saldo_insoluto = 0;
        double saldo_con_interes_final = 0;
        double pago_capital = 0;
        double pago_unitario = 0.00;
        double saldo_capital_1era_amort = 0.00;
        int dias_mora = 0;
        int num_pagos = 0;
        SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        PagoDAO pagoDAO = new PagoDAO();
        TablaAmortVO div_inicio = new TablaAmortVO();
        boolean incluyeIva = true;

        try {
            /*
             * Se obtiene el nuevo saldo de capital.
             */
            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldo_capital = saldoVO.getSaldoCapital() - pago;
            /*
             * Se obtiene los dividendos no vigentes, se obtiene el ultimo dividendo vigente, se calcula el pago unitario
             * se obtiene la tabla nueva
             */
            if (saldo_capital > 0) {
                pago_capital = pago;
                tablaAmort = tablaDAO.getDivPagoNoVigente(credito.getNumCliente(), credito.getNumCredito());
                div_inicio = tablaDAO.getDivPago(credito.getNumCliente(), credito.getNumCredito(), tablaAmort[0].numPago);
                interes_inicial = div_inicio.interes;
                iva_interes_inicial = div_inicio.ivaInteres;
                num_pagos = tablaAmort.length;
                myLogger.debug("numero de pagos " + num_pagos);
                myLogger.debug("tasa de interes " + credito.getTasaInteres());
                pago_unitario = TablaAmortizacionHelper.calcPagoUnitarioInsolutoDirectoCorto(credito.getTasaInteres(), saldo_capital, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getNumSucursal(), credito.getNumProducto());
                myLogger.debug("pago unitario inciial" + pago_unitario);
                pago_unitario = TablaAmortizacionHelper.calcPagoUnitarioIter(credito.getNumProducto(), saldo_capital, pago_unitario, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getNumSucursal(), credito.getTasaInteres());
                myLogger.debug("pago unitario iter" + pago_unitario);
                tablaNueva = calculaTablaInsolutoComunal(credito, grupo, ciclo, saldo_capital, saldo_capital, pago_unitario, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getTasaInteres());

                for (int i = 0; i < tablaAmort.length; i++) {
                    myLogger.debug("contador " + i);
                    tablaAmort[i].abonoCapital = tablaNueva[i].abonoCapital;
                    tablaAmort[i].saldoCapital = tablaNueva[i].saldoCapital;
                    tablaAmort[i].interes = tablaNueva[i].interes;
                    tablaAmort[i].ivaInteres = tablaNueva[i].ivaInteres;
                    tablaAmort[i].montoPagar = tablaNueva[i].montoPagar;
                    /* Se lleva el control de los saldos
                     */
                    saldo_capital_calc = saldo_capital_calc + tablaAmort[i].abonoCapital;
                    saldo_con_interes_final = saldo_con_interes_final + tablaAmort[i].abonoCapital + tablaAmort[i].interes + tablaAmort[i].ivaInteres;
                    if (i == 0) {
                        saldo_total_aldia = saldo_capital + tablaAmort[i].interes + tablaAmort[i].ivaInteres;
                        saldo_interes = tablaAmort[i].interes;
                        saldo_iva_interes = tablaAmort[i].ivaInteres;
                        saldo_capital_1era_amort = tablaAmort[i].abonoCapital;
                        diferencia_interes = interes_inicial - tablaAmort[i].interes;
                        diferencia_iva_interes = iva_interes_inicial - tablaAmort[i].ivaInteres;
                    }

                }
                /*
                 * Se actualizan los valores del ultimo dividendo pagado incluyendo alli el monto pagado en anticipo
                 */
                div_inicio.montoPagar = saldo_capital_1era_amort + saldo_interes + saldo_iva_interes;
                div_inicio.capitalAnticipado = pago;
                div_inicio.abonoCapital = saldo_capital_1era_amort;
                div_inicio.interes = saldo_interes;
                div_inicio.ivaInteres = saldo_iva_interes;
                div_inicio.saldoCapital = tablaAmort[0].saldoCapital;
                div_inicio.quitaIntres = diferencia_interes;
                div_inicio.quitaIvaIntres = diferencia_iva_interes;

                /* Pasamos a actualizar la tabla de amortizacion con los nuevos valores
                 * Se inicializan de nuevo las variables que totalizan para actualizar la tabla de saldos
                 */
                for (int i = 0; i < tablaAmort.length; i++) {
                    TablaAmortDAO tablaDAOAct = new TablaAmortDAO();
                    /* Se actualiza la tabla de amortizacion 
                     * 				
                     */
                    int rs = tablaDAOAct.updateSaldosTablaAmortExtend(tablaAmort[i]);

                }
                /*
                 * Se actualiza el ultimo dividendo pagado			
                 */
                TablaAmortDAO tablaDAODiv = new TablaAmortDAO();
                tablaDAODiv.updateSaldosTablaAmortExtend(div_inicio);

            } else {
                /*
                 * En caso de que el saldo de capital sea negativo se debe de liquidar la tabla, esto no debe de suceder aqui ya que al detectar un monto mayor en 
                 * cierre de dia se liquida la tabla automáticamente JBL- AGO-13				
                 */
            }
            /*
             * Se actualiza la tabla de saldos			
             */
            myLogger.info("saldo capital " + saldo_capital + "saldo_capital_calc " + saldo_capital_calc);

            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldoVO.setCapitalPagado(saldoVO.getCapitalPagado() + pago_capital);
            saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - pago_capital);
            saldoVO.setSaldoInteres(saldo_interes);
            saldoVO.setSaldoInteresVigente(saldo_interes);
            saldoVO.setSaldoIvaInteres(saldo_iva_interes);
            saldoVO.setCapitalSigAmortizacion(saldo_capital_1era_amort);
            saldoVO.setInteresSigAmortizacion(saldo_interes);
            saldoVO.setIvaSigAmortizacion(saldo_iva_interes);
            // la fecha de pago se toma como la fecha de aplicacion
            saldoVO.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            saldoVO.setTotalVencido(0);
            saldoVO.setSaldoConInteresAlFinal(saldo_con_interes_final);
            saldoVO.setSaldoTotalAlDia(saldo_total_aldia);
            saldoVO.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldoVO.getReferencia()));
            saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_capital);
            saldoVO.setSaldoBucket(remanente + saldoVO.getSaldoBucket());
            saldoVO.setDiasMora(0);
            saldoVO.setCuotasVencidas(0);
            //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
            saldoVO.setInteresPorDevengar(saldoVO.getInteresPorDevengar() - diferencia_interes);
            saldoVO.setIvaInteresPorDevengar(saldoVO.getIvaInteresPorDevengar() - diferencia_iva_interes);

            myLogger.info("va a actualizar saldo de referencia " + saldoVO.getReferencia());
            // Se actualiza la tabla de saldos
            saldoIBS.updateSaldo(saldoVO);
            /*
             * Se actualiza la tabla de credito			
             */
            myLogger.info("llega a actualizacion de credito");
//			credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            credito.setNumDiasMora(dias_mora);
            credito.setMontoCuenta(remanente + credito.getMontoCuenta());
            credito.setFechaUltimaActualizacion(Convertidor.toSqlDate(fechaUltimoCierre));
            credito.setFechaUltimoPago(Convertidor.toSqlDate(fechaUltimoCierre));
            String strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
            double porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia / 100;
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada() - (pagoMontoDesembolsado * porGarantia));

            // SE ACTUALIZA EL MONTO DE LA CUENTA EN TABLA DE CREDITOS
            CreditoHelperCartera.actualizaSaldoCuenta(credito);

            /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
             */
            // se llenan los montos de los rubros
            ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            if (pago_capital > 0) {
                rubro.tipoRubro = ClientesConstants.EFECTIVO;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.debug("Array pago " + array_pago.toString());
            if (pago_capital > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.CAPITAL;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.debug("rubros " + array_pago.toString());
            if (array_pago.size() > 0) {
                elementos = new RubrosVO[array_pago.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_pago.get(i);
                }
            }
            transacHelper.aplicaPago(credito, elementos, Convertidor.toSqlDate(fechaUltimoCierre));

            // la reduccion de interes de aplica como condonacion
            ArrayList<RubrosVO> array_condonacion = new ArrayList<RubrosVO>();
            RubrosVO rubro_con = new RubrosVO();
            RubrosVO elementos_con[] = null;
            if (diferencia_interes > 0) {
                rubro_con.tipoRubro = ClientesConstants.INTERES;
                rubro_con.monto = diferencia_interes;
                rubro_con.status = ClientesConstants.RUBRO_VIGENTE;
                array_condonacion.add(rubro_con);
            }
            myLogger.debug("Array condonacion " + array_condonacion.toString());
            if (diferencia_iva_interes > 0) {
                rubro_con = new RubrosVO();
                rubro_con.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro_con.monto = diferencia_iva_interes;
                rubro_con.status = ClientesConstants.RUBRO_VIGENTE;
                array_condonacion.add(rubro_con);
            }
            myLogger.debug("rubros " + array_condonacion.toString());
            if (array_condonacion.size() > 0) {
                elementos_con = new RubrosVO[array_condonacion.size()];
                for (int i = 0; i < elementos_con.length; i++) {
                    elementos_con[i] = (RubrosVO) array_condonacion.get(i);
                }
            }
            transacHelper.registraCondonacion(credito, elementos_con, Convertidor.toSqlDate(fechaUltimoCierre));

        } catch (Exception e) {
            myLogger.error("aplicaPagoAnticipadoReduccionGrupo", e);
            throw new ClientesException(e.getMessage());
        }
        return remanente;
    }

    public static TablaAmortVO[] calculaTablaInsolutoComunal(CreditoCartVO credito, GrupoVO grupo, CicloGrupalVO ciclo, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO montoConComision:" +montoConComision);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO montoSinComision:" +montoSinComision);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO pagoUnitario:" +pagoUnitario);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO plazo:" +plazo);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO frecuenciaPago:" +frecuenciaPago);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO fechaIni:" +fechaIni);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() INICIO METODO tasaAnual:" +tasaAnual);
        
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;
        /*double ajusteFinal = 0.00;
         double pagoAjustado = 0.00;
         double ajusteUltima = 0.00;*/
        int difDias = 0;

        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementosTabla[] = null;
        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            if (ciclo.plazo == 0) {
                plazo = 16;
            } else {
                if (plazo == 0) {
                    plazo = ciclo.plazo;
                } else {
                    plazo = plazo;
                }
            }
            difDias = 7;
        } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
            plazo = 8;
            difDias = 14;
        }

        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() plazo:" +plazo);
        myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() difDias:" +difDias);
        try {

            Date fechaTemp = new Date();
            Date fechaInicio = Convertidor.stringToDate(fechaIni);
            TablaAmortVO temp = new TablaAmortVO();
            TablaAmortDAO tabladao = new TablaAmortDAO();
            GregorianCalendar fechaOut = new GregorianCalendar();
            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                myLogger.info("tasa interes " + tasaAnual);
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(Convertidor.stringToDate(fechaIni));
                temp.saldoInicial = ciclo.montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                comision = ciclo.montoConComision - ciclo.monto - ciclo.montoRefinanciado;
                comisionSinIva = Convertidor.getMontoIva(comision, grupo.sucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comision = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.numCliente = grupo.idGrupo;
                temp.numCredito = ciclo.idCreditoIBS;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                temp.pagado = "N";
                
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() temp:" +temp);
            } catch (Exception e) {
                myLogger.error("calculaTablaInsolutoComunal", e);
            }
            tasaInteres = FormatUtil.roundDouble(tasaAnual / 100 / 360, 10);
            myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() tasaInteres:" +tasaInteres);
            myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() before for plazo:" +plazo);
            for (int i = 0; i < plazo; i++) {
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() i:" +i);
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() (i == plazo - 1):" +(i == plazo - 1));
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() (i != plazo - 1):" +(i != plazo - 1));
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() (i != plazo):" +(i != plazo));
                
                if (i == plazo - 1) {
                    abonoCapital = sdoInsoluto;
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    sdoInsoluto = 0;
                    interes = Convertidor.getMontoIva(abonoInteres, grupo.sucursal, 1);
                    ivaInteres = abonoInteres - interes;
                }

                if (i != plazo - 1) {
                    abonoInteres = sdoInsoluto * difDias * (tasaInteres);
                    abonoCapital = pagoUnitario - abonoInteres;
                    sdoInsoluto = sdoInsoluto - abonoCapital;
                    interes = Convertidor.getMontoIva(abonoInteres, grupo.sucursal, 1);
                    ivaInteres = abonoInteres - interes;
                }
                
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() abonoInteres:" +abonoInteres);
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() abonoCapital:" +abonoCapital);
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() sdoInsoluto:" +sdoInsoluto);
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() interes:" +interes);
                myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() ivaInteres:" +ivaInteres);

                if (i != plazo) {
                    temp = new TablaAmortVO();
                    if (i == 0) {
                        temp.status = 1;
                    } else {
                        temp.status = 0;
                    }
                    temp.numCliente = ciclo.idGrupo;
                    temp.numCredito = ciclo.idCreditoIBS;
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                    temp.numPago = i + 1;
                    fechaTemp = FechasUtil.getDate(fechaTemp, difDias, 1);
                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comision = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);
                    temp.pagado = "N";
                    array.add(temp);
                    
                    myLogger.info("TablaAmortHelper.calculaTablaInsolutoComunal() temp:" +temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            myLogger.error("calculaTablaInsolutoComunal", e);
        }

        return elementosTabla;

    }

    public static void quitaPagoTabla(CreditoCartVO credito, SaldoIBSVO saldo, PagoVO pago) throws ParseException {

        double saldoFavor = 0;
        double saldoTotQuitar = 0, saldoQuitar = 0;
        double quitaCapital = 0, quitaIneres = 0, quitaIvaInteres = 0, quitaMora = 0, quitaIvaMora = 0, quitaMulta = 0, quitaIvaMulta = 0, quitaTotal = 0;
        double monto = 0, montoIVA = 0, fraccion = 0;
        PagoGrupalDAO pagoGroDAO = new PagoGrupalDAO();
        PagoIndividualGruposDAO pagoIndivDAO = new PagoIndividualGruposDAO();
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        PagoDAO pagoDAO = new PagoDAO();
        PagoGrupalVO pagoGroVO = new PagoGrupalVO();
        ArrayList<PagoGrupalVO> arrPagoGroVO = new ArrayList<PagoGrupalVO>();
        TablaAmortVO tablaVO = new TablaAmortVO();
        int numPago = 0, diasMora = 0, cuotasVencidas = 0;
        TransaccionesHelper transHelper = new TransaccionesHelper();
        EventoHelper eventoHelper = new EventoHelper();
        FormatUtil formato = new FormatUtil();
        String fechaCierre = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        Date fechaUltCierre = sdf.parse(fechaCierre);
        //int diasDif = 0;
        myLogger.info("Procedimiento de quita pago");
        saldoFavor = credito.getMontoCuenta() - credito.getMontoCuentaCongelada();
        myLogger.info("saldoFavor " + saldoFavor + " monto " + pago.getMonto());
        try {
            if (saldoFavor > 0 && saldoFavor >= pago.getMonto()) {
                myLogger.info("SE CUBRE MONTO CON SALDO A FAVOR");
                credito.setMontoCuenta(credito.getMontoCuenta() - pago.getMonto());
                saldo.setSaldoBucket(credito.getMontoCuenta());
                saldo.setNumeroPagosRealizados(saldo.getNumeroPagosRealizados() - 1);
                //saldo.setFechaGeneracion(Convertidor.toSqlDate(fechaUltCierre));
            } else {
                myLogger.info("SE TIENE QUE HACER PAGOS PARCIALES");
                if (saldoFavor > 0) {
                    saldoTotQuitar = pago.getMonto() - saldoFavor;
                } else {
                    saldoTotQuitar = pago.getMonto();
                }
                saldoQuitar = saldoTotQuitar;
                myLogger.info("saldoQuitar " + saldoQuitar);
                //saldo.setTotalVencido(saldo.getTotalVencido() + saldoQuitar);
                saldo.setMontoTotalPagado(saldo.getMontoTotalPagado() - saldoQuitar);
                saldo.setSaldoConInteresAlFinal(saldo.getSaldoConInteresAlFinal() + saldoQuitar);
                //saldo.setFechaGeneracion(Convertidor.toSqlDate(fechaUltCierre));
                do {
                    diasMora = 0;
                    tablaVO = tablaDAO.getUltmioDivPagado(credito.getNumCliente(), credito.getNumCredito());
                    credito.setMontoCuenta(credito.getMontoCuentaCongelada());
                    saldo.setSaldoBucket(credito.getMontoCuenta());
                    if (saldoTotQuitar >= tablaVO.getTotalPagado()) {
                        quitaTotal += tablaVO.getTotalPagado();
                        saldoTotQuitar -= tablaVO.getTotalPagado();
                        tablaVO.setTotalPagado(0);
                    } else {
                        quitaTotal += saldoQuitar;
                        tablaVO.setTotalPagado(tablaVO.getTotalPagado() - saldoQuitar);
                        saldoTotQuitar = 0;
                    }
                    tablaVO.setPagado("N");
                    if (tablaVO.getFechaPago().after(fechaUltCierre) || tablaVO.getFechaPago().equals(fechaUltCierre)) {
                        tablaVO.setStatus(1);
                    } else {
                        tablaVO.setStatus(2);
                    }
                    /*saldo.setTotalVencido(saldo.getTotalVencido() + saldoQuitar);
                     saldo.setMontoTotalPagado(saldo.getMontoTotalPagado() - saldoQuitar);
                     saldo.setSaldoConInteresAlFinal(saldo.getSaldoConInteresAlFinal() + saldoQuitar);*/
                    if (tablaVO.getCapitalPagado() > 0 && saldoQuitar > 0) {
                        if (tablaVO.getCapitalPagado() > 0) {
                            myLogger.info("Quita capital");
                            if (saldoQuitar >= tablaVO.getCapitalPagado()) {
                                /*if ((tablaVO.getAbonoCapital() - tablaVO.getCapitalPagado()) > 0) {
                                 monto = (tablaVO.getAbonoCapital() - tablaVO.getCapitalPagado());
                                 } else {
                                 monto = tablaVO.getAbonoCapital();
                                 }*/
                                monto = tablaVO.getCapitalPagado();
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + monto);
                                saldo.setSaldoCapital(saldo.getSaldoCapital() + monto);
                                saldo.setCapitalPagado(saldo.getCapitalPagado() - monto);
                                if (tablaVO.getStatus() == 2) {
                                    saldo.setCapitalVencido(saldo.getCapitalVencido() + monto);
                                }
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + monto);
                                quitaCapital += monto;
                                saldoQuitar -= monto;
                                tablaVO.setCapitalPagado(0);
                                tablaVO.setCompletaATiempo(0);
                            } else {
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + saldoQuitar);
                                saldo.setSaldoCapital(saldo.getSaldoCapital() + saldoQuitar);
                                saldo.setCapitalPagado(saldo.getCapitalPagado() - saldoQuitar);
                                if (tablaVO.getStatus() == 2) {
                                    saldo.setCapitalVencido(saldo.getCapitalVencido() + saldoQuitar);
                                }
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + saldoQuitar);
                                quitaCapital += saldoQuitar;
                                tablaVO.setCapitalPagado(tablaVO.getCapitalPagado() - saldoQuitar);
                                tablaVO.setCompletaATiempo(0);
                                saldoQuitar = 0;
                            }
                        }
                    }
                    if ((tablaVO.getInteresPagado() + tablaVO.getIvaInteresPagado()) > 0 && saldoQuitar > 0) {
                        if ((tablaVO.getInteresPagado() + tablaVO.getIvaInteresPagado()) > 0) {
                            myLogger.info("Quita intereses ");
                            if (saldoQuitar >= (tablaVO.getInteresPagado() + tablaVO.getIvaInteresPagado())) {
                                /*if ((tablaVO.getInteres() - tablaVO.getInteresPagado()) + (tablaVO.getIvaInteres() - tablaVO.getIvaInteresPagado()) > 0) {
                                 monto = (tablaVO.getInteres() - tablaVO.getInteresPagado());
                                 montoIVA = (tablaVO.getIvaInteres() - tablaVO.getIvaInteresPagado());
                                 } else {
                                 monto = tablaVO.getInteres();
                                 montoIVA = tablaVO.getIvaInteres();
                                 }*/
                                monto = tablaVO.getInteresPagado();
                                montoIVA = tablaVO.getIvaInteresPagado();
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + monto + montoIVA);
                                saldo.setSaldoInteres(saldo.getSaldoInteres() + monto);
                                saldo.setSaldoInteresVigente(saldo.getSaldoInteresVigente() + monto);
                                if (tablaVO.getStatus() == 2) {
                                    saldo.setSaldoInteresVencido(saldo.getSaldoInteresVencido() + monto);
                                    saldo.setIvaInteresVencido(saldo.getIvaInteresVencido() + montoIVA);
                                }
                                saldo.setSaldoIvaInteres(saldo.getSaldoIvaInteres() + montoIVA);
                                saldo.setInteresNormalPagado(saldo.getInteresNormalPagado() - monto);
                                saldo.setIvaInteresNormalPagado(saldo.getIvaInteresNormalPagado() - montoIVA);
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + monto + montoIVA);
                                quitaIneres += monto;
                                quitaIvaInteres += montoIVA;
                                saldoQuitar -= (monto + montoIVA);
                                tablaVO.setInteresPagado(0);
                                tablaVO.setIvaInteresPagado(0);
                            } else {
                                monto = 0;
                                montoIVA = 0;
                                fraccion = formato.roundDouble(tablaVO.getInteres() / (tablaVO.getInteres() + tablaVO.getIvaInteres()), 2);
                                monto = formato.roundDouble(formato.roundDouble(saldoQuitar * fraccion, 2), 2);
                                montoIVA = formato.roundDouble(formato.roundDouble(saldoQuitar - formato.roundDouble(saldoQuitar * fraccion, 2), 2), 2);
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + monto + montoIVA);
                                saldo.setSaldoInteres(saldo.getSaldoInteres() + monto);
                                if (tablaVO.getStatus() == 2) {
                                    saldo.setSaldoInteresVencido(saldo.getSaldoInteresVencido() + monto);
                                    saldo.setIvaInteresVencido(saldo.getIvaInteresVencido() + montoIVA);
                                }
                                saldo.setSaldoIvaInteres(saldo.getSaldoIvaInteres() + montoIVA);
                                saldo.setInteresNormalPagado(saldo.getInteresNormalPagado() - monto);
                                saldo.setIvaInteresNormalPagado(saldo.getIvaInteresNormalPagado() - montoIVA);
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + monto + montoIVA);
                                quitaIneres += monto;
                                quitaIvaInteres += montoIVA;
                                tablaVO.setInteresPagado(tablaVO.getInteresPagado() - monto);
                                tablaVO.setIvaInteresPagado(tablaVO.getIvaInteresPagado() - montoIVA);
                                saldoQuitar = 0;
                            }
                        }
                    }
                    if (tablaVO.getMontoInteresMora() > 0 && saldoQuitar > 0) {
                        myLogger.info("Quita mora ");
                    }
                    if ((tablaVO.getMultaPagado() + tablaVO.getIvaMultaPagado()) > 0 && saldoQuitar > 0) {
                        if ((tablaVO.getMultaPagado() + tablaVO.getIvaMultaPagado()) > 0) {
                            myLogger.info("Quita multa ");
                            if (saldoQuitar >= (tablaVO.getMultaPagado() + tablaVO.getIvaMultaPagado())) {
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + (tablaVO.getMulta() - tablaVO.getMultaPagado()) + (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado()));
                                saldo.setSaldoMulta(saldo.getSaldoMulta() + (tablaVO.getMulta() - tablaVO.getMultaPagado()));
                                saldo.setSaldoIVAMulta(saldo.getSaldoIVAMulta() + (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado()));
                                saldo.setMultaPagada(saldo.getMultaPagada() - (tablaVO.getMulta() - tablaVO.getMultaPagado()));
                                saldo.setIvaMultaPagado(saldo.getIvaMultaPagado() - (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado()));
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + (tablaVO.getMulta() - tablaVO.getMultaPagado()) + (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado()));
                                quitaMulta += (tablaVO.getMulta() - tablaVO.getMultaPagado());
                                quitaIvaMulta += (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado());
                                saldoQuitar -= ((tablaVO.getMulta() - tablaVO.getMultaPagado()) + (tablaVO.getIvaMulta() - tablaVO.getIvaMultaPagado()));
                                tablaVO.setMultaPagado(0);
                                tablaVO.setIvaMultaPagado(0);
                            } else {
                                monto = 0;
                                montoIVA = 0;
                                fraccion = formato.roundDouble(tablaVO.getMulta() / (tablaVO.getMulta() + tablaVO.getIvaMulta()), 2);
                                monto = formato.roundDouble(formato.roundDouble(saldoQuitar * fraccion, 2), 2);
                                montoIVA = formato.roundDouble(formato.roundDouble(saldoQuitar - formato.roundDouble(saldoQuitar * fraccion, 2), 2), 2);
                                saldo.setSaldoTotalAlDia(saldo.getSaldoTotalAlDia() + monto + montoIVA);
                                saldo.setSaldoMulta(saldo.getSaldoMulta() + monto);
                                saldo.setSaldoIVAMulta(saldo.getSaldoIVAMulta() + montoIVA);
                                saldo.setMultaPagada(saldo.getMultaPagada() - monto);
                                saldo.setIvaMultaPagado(saldo.getIvaMultaPagado() - montoIVA);
                                tablaVO.setMontoPagar(tablaVO.getMontoPagar() + monto + montoIVA);
                                quitaMulta += monto;
                                quitaIvaMulta += montoIVA;
                                tablaVO.setMultaPagado(tablaVO.getMultaPagado() - monto);
                                tablaVO.setIvaMultaPagado(tablaVO.getIvaMultaPagado() - montoIVA);
                                saldoQuitar = 0;
                            }
                        }
                    }
                    if (tablaVO.getStatus() == 2) {
                        saldo.setTotalVencido(saldo.getCapitalVencido() + saldo.getSaldoInteresVencido() + saldo.getIvaInteresVencido() + saldo.getSaldoMulta() + saldo.getSaldoIVAMulta());
                        cuotasVencidas++;
                    }
                    tablaDAO.actSaldoTablaCierre(saldo, tablaVO);
                } while (saldoTotQuitar != 0);
                if (saldo.getTotalVencido() > 0) {
                    saldo.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                    credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                    if (fechaUltCierre.equals(tablaVO.getFechaPago())) {
                        diasMora = 1;
                    } else {
                        diasMora = FechasUtil.inBetweenDays(tablaVO.getFechaPago(), fechaUltCierre) + 1;
                    }
                    saldo.setDiasMora(diasMora);
                    credito.setNumDiasMora(diasMora);
                    saldo.setCuotasVencidas(cuotasVencidas);
                } else {
                    saldo.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                    credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                    saldo.setNumeroCuotasTranscurridas(tablaVO.getNumPago() - 1);
                    saldo.setFechaSigAmortizacion(tablaVO.getFechaPago());
                    saldo.setCapitalSigAmortizacion(tablaVO.getAbonoCapital());
                    saldo.setInteresSigAmortizacion(tablaVO.getInteres());
                    saldo.setIvaSigAmortizacion(tablaVO.getIvaInteres());
                }
                ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
                RubrosVO rubro = new RubrosVO();
                RubrosVO elementos[] = null;
                myLogger.info("quitaTotal " + quitaTotal);
                if (quitaTotal > 0) {
                    rubro.tipoRubro = ClientesConstants.EFECTIVO;
                    rubro.monto = quitaTotal;
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    array_pago.add(rubro);
                }
                myLogger.info("quitaCapital " + quitaCapital);
                if (quitaCapital > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.CAPITAL;
                    rubro.monto = quitaCapital;
                    if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                        rubro.status = ClientesConstants.RUBRO_VENCIDO;
                    } else {
                        rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    }
                    array_pago.add(rubro);
                }
                myLogger.info("quitaIneres " + quitaIneres);
                if (quitaIneres > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.INTERES;
                    rubro.monto = quitaIneres;
                    if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                        rubro.status = ClientesConstants.RUBRO_VENCIDO;
                    } else {
                        rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    }
                    array_pago.add(rubro);
                }
                myLogger.info("quitaIvaInteres " + quitaIvaInteres);
                if (quitaIvaInteres > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                    rubro.monto = quitaIvaInteres;
                    if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                        rubro.status = ClientesConstants.RUBRO_VENCIDO;
                    } else {
                        rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    }
                    array_pago.add(rubro);
                }
                myLogger.info("quitaMora " + quitaMora);
                if (quitaMora > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.INTERES_MORATORIO;
                    rubro.monto = quitaMora;
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    array_pago.add(rubro);
                }
                myLogger.info("quitaIvaMora " + quitaIvaMora);
                if (quitaIvaMora > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.IVA_INTERES_MORATORIO;
                    rubro.monto = quitaIvaMora;
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    array_pago.add(rubro);
                }
                myLogger.info("quitaMulta " + quitaMulta);
                if (quitaMulta > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.MULTA;
                    rubro.monto = quitaMulta;
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    array_pago.add(rubro);
                }
                myLogger.info("quitaIvaMulta " + quitaIvaMulta);
                if (quitaIvaMulta > 0) {
                    rubro = new RubrosVO();
                    rubro.tipoRubro = ClientesConstants.IVA_MULTA;
                    rubro.monto = quitaIvaMulta;
                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                    array_pago.add(rubro);
                }
                myLogger.info("rubros " + array_pago.toString());
                if (array_pago.size() > 0) {
                    elementos = new RubrosVO[array_pago.size()];
                    for (int i = 0; i < elementos.length; i++) {
                        elementos[i] = (RubrosVO) array_pago.get(i);
                    }
                }
                if (elementos.length > 0) {
                    transHelper.cancelaPago(credito, elementos);
                }
                //eventoHelper.registraCancelaPago(credito, tablaVO, quitaTotal, saldo.getFechaGeneracion(), saldo.getSaldoTotalAlDia());
                eventoHelper.registraCancelaPago(credito, tablaVO, quitaTotal, saldo.getFechaGeneracion(), saldo.getSaldoConInteresAlFinal());
            }
            pagoGroVO = pagoGroDAO.getPagoGrupalMonto(credito.getNumCliente(), credito.getNumSolicitud(), pago);
            arrPagoGroVO = pagoGroDAO.getArrPagoGrupal(credito.getNumCliente(), credito.getNumSolicitud(), pagoGroVO.getNumPago());
            pagoDAO.deletePago(pago);
            pagoDAO.deletePagoODS(pago);
            pagoGroDAO.deletePagoGrupal(pagoGroVO);
            pagoIndivDAO.deletePagoIndividualGrupal(pagoGroVO);
            if (!arrPagoGroVO.isEmpty()) {
                numPago = pagoGroVO.getNumPago();
                for (PagoGrupalVO pagoGrupal : arrPagoGroVO) {
                    pagoGrupal.setSolidario(0);
                    pagoGrupal.setAhorro(0);
                    pagoGrupal.setMulta(0);
                    pagoGroDAO.actualizaPagoGrupal(pagoGrupal, numPago);
                    pagoIndivDAO.deletePagoIndividualGrupal(pagoGrupal);
                    numPago++;
                }
            }
            saldo.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldo.getReferencia()));
            saldo.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldo.getReferencia())));
            new SaldoIBSDAO().updateSaldo(saldo);
            CreditoHelperCartera.actualizaSaldoCuenta(credito);
            eventoHelper.registraCancelacionPago(credito, pago.monto, saldoFavor);
            transHelper.registraCancelacionPago(credito, pago, saldoFavor);
        } catch (Exception e) {
            myLogger.error("quitaPagoTabla", e);
        }
    }

    public static void aplicaCapitalGrupoInterciclo(Connection con, Connection conCar, CreditoCartVO credito, CicloGrupalVO cicloVO, GrupoVO grupo, double montoIncremento,double montoSinSeg, double saldoFondeador, HttpServletRequest request, int numIntegrantes, int semDisp) throws ClientesException {

        TablaAmortizacionVO[] tablaAmortInterciclo = null;
        TablaAmortVO[] tablaAmortCart = null;
        TablaAmortizacionDAO tablaClieDAO = new TablaAmortizacionDAO();
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        double diferenciaInteres = 0, diferenciaIvaInteres = 0;
        int cuentaOrigen = 0;
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        double comision = 0;
        double ivacomision = 0;
        String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);

        try {
            Date fechaUltimoCierre  = sdf.parse(ultimaFecha);
            tablaAmortInterciclo = tablaClieDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
            tablaAmortCart = tablaCartDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), 0);
            for (int i = semDisp; i < tablaAmortInterciclo.length; i++) {
                if (i == semDisp) {
                    tablaAmortCart[i - 1].saldoCapital = tablaAmortInterciclo[i].saldoCapital;
                    tablaAmortCart[i - 1].incrementoCapital = montoIncremento;
                } else {
                    diferenciaInteres += tablaAmortInterciclo[i].interes - tablaAmortCart[i - 1].interes;
                    diferenciaIvaInteres += tablaAmortInterciclo[i].ivaInteres - tablaAmortCart[i - 1].ivaInteres;
                    tablaAmortCart[i - 1].abonoCapital = tablaAmortInterciclo[i].abonoCapital;
                    tablaAmortCart[i - 1].saldoCapital = tablaAmortInterciclo[i].saldoCapital;
                    tablaAmortCart[i - 1].interes = tablaAmortInterciclo[i].interes;
                    tablaAmortCart[i - 1].ivaInteres = tablaAmortInterciclo[i].ivaInteres;
                    tablaAmortCart[i - 1].montoPagar = tablaAmortInterciclo[i].montoPagar;
                }
                tablaCartDAO.updateSaldosTablaAmortExtendInterciclo(conCar, tablaAmortCart[i - 1]);
            }
            cicloVO.getSaldo().setSaldoCapital(cicloVO.getSaldo().getSaldoCapital() + montoIncremento);
            cicloVO.getSaldo().setSaldoConInteresAlFinal(cicloVO.getSaldo().getSaldoConInteresAlFinal() + montoIncremento + diferenciaInteres + diferenciaIvaInteres);
            cicloVO.getSaldo().setSaldoTotalAlDia(cicloVO.getSaldo().getSaldoTotalAlDia() + montoIncremento);
            cicloVO.getSaldo().setMontoCredito(cicloVO.getSaldo().getMontoCredito() + montoIncremento);
            cicloVO.getSaldo().setMontoDesembolsado(cicloVO.getSaldo().getMontoDesembolsado() + montoSinSeg);
            
            if(cicloVO.getSaldo().getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                cicloVO.getSaldo().setFechaAsigGarantia(Convertidor.toSqlDate(fechaUltimoCierre));                
            }            
            myLogger.info("Grupo : "+cicloVO.idGrupo+" Ciclo: "+cicloVO.idCiclo);
            myLogger.info("**Adicionales saldo1: "+cicloVO.getSaldo().getNumIntegrantesAdicionales());
            SaldoIBSVO saldoNumAdicionales = new SaldoIBSDAO().getSaldos(cicloVO.idGrupo, cicloVO.idCiclo);
            myLogger.info("**Adicionales saldoBDReciente: "+saldoNumAdicionales.getNumIntegrantesAdicionales());
            myLogger.info("**Num interciclos: "+numIntegrantes);
            int totalIntegrantesAdicionales = saldoNumAdicionales.getNumIntegrantesAdicionales()+numIntegrantes;
            
            cicloVO.getSaldo().setNumIntegrantesAdicionales(totalIntegrantesAdicionales);
            //cicloVO.getSaldo().setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
            new SaldoIBSDAO().updateSaldosInterciclo(con, cicloVO.getSaldo());
            String strGarantia = new CatalogoDAO().getGarantia(cicloVO.getGarantia());
            double porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia / 100;
            credito.setMontoCredito(credito.getMontoCredito() + montoIncremento);
            credito.setMontoDesembolsado(credito.getMontoDesembolsado() + montoIncremento);
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada() + (montoSinSeg * porGarantia));
            credito.setValorCuota(tablaAmortInterciclo[6].montoPagar);
            new CreditoCartDAO().updateCreditoInterciclo(conCar, credito);
            new TablaDevengamientoHelper().ajustaDevengamientoInterciclo(credito, cicloVO.getSaldo(), con, conCar);
            ArrayList<RubrosVO> array_desembolso = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            rubro.tipoRubro = ClientesConstants.CAPITAL;
            rubro.monto = montoIncremento;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            array_desembolso.add(rubro);
            myLogger.debug("Array pago " + array_desembolso.toString());
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.EFECTIVO;
            rubro.monto = montoSinSeg;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            switch (cicloVO.getBancoDispersion()) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
            }
            cuentaOrigen = rubro.origen;
            array_desembolso.add(rubro);
            myLogger.debug("Array pago " + array_desembolso.toString());
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.INTERES;
            rubro.monto = diferenciaInteres;
            rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
            rubro.origen = 0;
            array_desembolso.add(rubro);
            myLogger.debug("rubros " + array_desembolso.toString());
            if (array_desembolso.size() > 0) {
                elementos = new RubrosVO[array_desembolso.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_desembolso.get(i);
                }
            }
            transacHelper.registraDesembolsoInterciclo(conCar, credito, elementos, Convertidor.toSqlDate(fechaUltimoCierre), saldoFondeador, cicloVO,semDisp);
            if (credito.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                transacHelper.registraDesembolsoBursa(credito,Convertidor.toSqlDate(fechaUltimoCierre),montoIncremento,diferenciaInteres);
            }
            EventoHelper.registraDesembolsoInterciclo(cicloVO, grupo, request, montoIncremento, (diferenciaInteres + diferenciaIvaInteres));
            EventoHelper.registraDesembolsoInteresInterciclo(cicloVO, grupo, request, (diferenciaInteres + diferenciaIvaInteres));
        } catch (Exception e) {
            myLogger.error("aplicaCapitalGrupoInterciclo", e);
            throw new ClientesException(e.getMessage());
        }
    }
    
    public static void aplicaCapitalGrupoAdicional(Connection con, Connection conCar, CreditoCartVO credito, CicloGrupalVO cicloVO, GrupoVO grupo, double montoIncremento, double saldoFondeador, HttpServletRequest request, int semDisp) throws ClientesException {

        TablaAmortizacionVO[] tablaAmortInterciclo = null;
        TablaAmortVO[] tablaAmortCart = null;
        TablaAmortizacionDAO tablaClieDAO = new TablaAmortizacionDAO();
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        double diferenciaInteres = 0, diferenciaIvaInteres = 0;
        int cuentaOrigen = 0;
        CuentaBancariaVO cuenta = new CuentaBancariaVO();
        CuentasBancariasDAO cuentaDao = new CuentasBancariasDAO();
        double comision = 0;
        double ivacomision = 0;
        String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);

        try {
            Date fechaUltimoCierre  = sdf.parse(ultimaFecha);
            tablaAmortInterciclo = tablaClieDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_GRUPAL);
            tablaAmortCart = tablaCartDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), 0);
            for (int i = semDisp; i < tablaAmortInterciclo.length; i++) {
                if (i == semDisp) {
                    tablaAmortCart[i - 1].saldoCapital = tablaAmortInterciclo[i].saldoCapital;
                    tablaAmortCart[i - 1].incrementoCapital = montoIncremento;
                } else {
                    diferenciaInteres += tablaAmortInterciclo[i].interes - tablaAmortCart[i - 1].interes;
                    diferenciaIvaInteres += tablaAmortInterciclo[i].ivaInteres - tablaAmortCart[i - 1].ivaInteres;
                    tablaAmortCart[i - 1].abonoCapital = tablaAmortInterciclo[i].abonoCapital;
                    tablaAmortCart[i - 1].saldoCapital = tablaAmortInterciclo[i].saldoCapital;
                    tablaAmortCart[i - 1].interes = tablaAmortInterciclo[i].interes;
                    tablaAmortCart[i - 1].ivaInteres = tablaAmortInterciclo[i].ivaInteres;
                    tablaAmortCart[i - 1].montoPagar = tablaAmortInterciclo[i].montoPagar;
                }
                tablaCartDAO.updateSaldosTablaAmortExtendInterciclo(conCar, tablaAmortCart[i - 1]);
            }
            cicloVO.getSaldo().setSaldoCapital(cicloVO.getSaldo().getSaldoCapital() + montoIncremento);
            cicloVO.getSaldo().setSaldoConInteresAlFinal(cicloVO.getSaldo().getSaldoConInteresAlFinal() + montoIncremento + diferenciaInteres + diferenciaIvaInteres);
            cicloVO.getSaldo().setSaldoTotalAlDia(cicloVO.getSaldo().getSaldoTotalAlDia() + montoIncremento);
            cicloVO.getSaldo().setMontoCredito(cicloVO.getSaldo().getMontoCredito() + montoIncremento);
            cicloVO.getSaldo().setMontoDesembolsado(cicloVO.getSaldo().getMontoDesembolsado() + montoIncremento);
            //cicloVO.getSaldo().setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
            if(cicloVO.getSaldo().getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                cicloVO.getSaldo().setFechaAsigGarantia(Convertidor.toSqlDate(fechaUltimoCierre));                
            }            
            myLogger.info("Grupo : "+cicloVO.idGrupo+" Ciclo: "+cicloVO.idCiclo);
            myLogger.info("**Adicionales saldo1: "+cicloVO.getSaldo().getNumIntegrantesAdicionales());
            SaldoIBSVO saldoNumAdicionales = new SaldoIBSDAO().getSaldos(cicloVO.idGrupo, cicloVO.idCiclo);
            myLogger.info("**Adicionales saldoBDReciente: "+saldoNumAdicionales.getNumIntegrantesAdicionales());
            int totalIntegrantesAdicionales = saldoNumAdicionales.getNumIntegrantesAdicionales();            
            cicloVO.getSaldo().setNumIntegrantesAdicionales(totalIntegrantesAdicionales);            
            new SaldoIBSDAO().updateSaldosInterciclo(con, cicloVO.getSaldo());
            String strGarantia = new CatalogoDAO().getGarantia(cicloVO.getGarantia());
            double porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia / 100;
            credito.setMontoCredito(credito.getMontoCredito() + montoIncremento);
            credito.setMontoDesembolsado(credito.getMontoDesembolsado() + montoIncremento);
            
            /**JECB 01/10/2017
            Se omite calculo que incremanetaba la garantia en un 10% del monto del credito adicional
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada() + (montoIncremento * porGarantia));
            */
            credito.setValorCuota(tablaAmortInterciclo[semDisp+1].montoPagar);
            new CreditoCartDAO().updateCreditoInterciclo(conCar, credito);
            new TablaDevengamientoHelper().ajustaDevengamientoInterciclo(credito, cicloVO.getSaldo(), con, conCar);
            ArrayList<RubrosVO> array_desembolso = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            rubro.tipoRubro = ClientesConstants.CAPITAL;
            rubro.monto = montoIncremento;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            array_desembolso.add(rubro);
            myLogger.debug("Array pago " + array_desembolso.toString());
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.EFECTIVO;
            rubro.monto = montoIncremento;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            switch (cicloVO.getBancoDispersion()) {
                case ClientesConstants.ID_BANCO_BANORTE:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANCOMER:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                    break;
                case ClientesConstants.ID_BANCO_BANAMEX:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SCOTIABANK:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                    break;
                case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                    rubro.origen = new CuentasBancariasDAO().getNumCuentaBancaria(cicloVO.getBancoDispersion(), CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                    break;
            }
            cuentaOrigen = rubro.origen;
            array_desembolso.add(rubro);
            myLogger.debug("Array pago " + array_desembolso.toString());
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.INTERES;
            rubro.monto = diferenciaInteres;
            rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
            rubro.origen = 0;
            array_desembolso.add(rubro);
            myLogger.debug("rubros " + array_desembolso.toString());
            if (array_desembolso.size() > 0) {
                elementos = new RubrosVO[array_desembolso.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_desembolso.get(i);
                }
            }            
            transacHelper.registraDesembolsoInterciclo(conCar, credito, elementos, Convertidor.toSqlDate(fechaUltimoCierre), saldoFondeador);
            if (credito.getFondeador()==ClientesConstants.ID_FONDEADOR_BURSA){
                transacHelper.registraDesembolsoBursa(credito,Convertidor.toSqlDate(fechaUltimoCierre),montoIncremento,diferenciaInteres);
            }
            EventoHelper.registraDesembolsoInterciclo(cicloVO, grupo, request, montoIncremento, (diferenciaInteres + diferenciaIvaInteres));
            EventoHelper.registraDesembolsoInteresInterciclo(cicloVO, grupo, request, (diferenciaInteres + diferenciaIvaInteres));
        } catch (Exception e) {
            myLogger.error("aplicaCapitalGrupoAdicional", e);
            throw new ClientesException(e.getMessage());
        }
    }
    /**
     * JECB 01/10/2017
     * Se crea método aplicaPagoAnticipadoReducciongrupoAdicional empleado
     * en la devolucion de una orden de pago de credito adicional
     * para cuando se 
     * @param credito bean del credito
     * @param ciclo bean del ciclo
     * @param grupo bean del grupo
     * @param pago
     * @return
     * @throws ClientesException
     * @throws ParseException 
     */   
    public static double aplicaPagoAnticipadoReduccionGrupoAdicional(CreditoCartVO credito, CicloGrupalVO ciclo, GrupoVO grupo, double pago) throws ClientesException, ParseException {
        myLogger.info("método aplicaPagoAnticipadoReduccionGrupoAdicional");
        //ejecuta 
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO[] tablaNueva = null;
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        EventoHelper eventohelper = new EventoHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        TablaAmortizacionHelper tablaHelper = new TablaAmortizacionHelper();
        String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        Date fechaUltimoCierre  = sdf.parse(ultimaFecha);

        double remanente = 0;
        double saldo_capital = 0;
        double saldo_capital_calc = 0;
        double saldo_interes = 0;
        double saldo_iva_interes = 0;
        double interes_inicial = 0;
        double iva_interes_inicial = 0;
        double diferencia_interes = 0;
        double diferencia_iva_interes = 0;
        double saldo_total_aldia = 0;
        double saldo_insoluto = 0;
        double saldo_con_interes_final = 0;
        double pago_capital = 0;
        double pago_unitario = 0.00;
        double saldo_capital_1era_amort = 0.00;
        int dias_mora = 0;
        int num_pagos = 0;
        SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        PagoDAO pagoDAO = new PagoDAO();
        TablaAmortVO div_inicio = new TablaAmortVO();
        boolean incluyeIva = true;

        try {
            /*
             * Se obtiene el nuevo saldo de capital.
             */
            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldo_capital = saldoVO.getSaldoCapital() - pago;
            /*
             * Se obtiene los dividendos no vigentes, se obtiene el ultimo dividendo vigente, se calcula el pago unitario
             * se obtiene la tabla nueva
             */
            if (saldo_capital > 0) {
                pago_capital = pago;
                tablaAmort = tablaDAO.getDivPagoNoVigente(credito.getNumCliente(), credito.getNumCredito());
                div_inicio = tablaDAO.getDivPago(credito.getNumCliente(), credito.getNumCredito(), tablaAmort[0].numPago);
                interes_inicial = div_inicio.interes;
                iva_interes_inicial = div_inicio.ivaInteres;
                num_pagos = tablaAmort.length;
                myLogger.debug("numero de pagos " + num_pagos);
                myLogger.debug("tasa de interes " + credito.getTasaInteres());
                pago_unitario = TablaAmortizacionHelper.calcPagoUnitarioInsolutoDirectoCorto(credito.getTasaInteres(), saldo_capital, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getNumSucursal(), credito.getNumProducto());
                myLogger.debug("pago unitario inciial" + pago_unitario);
                pago_unitario = TablaAmortizacionHelper.calcPagoUnitarioIter(credito.getNumProducto(), saldo_capital, pago_unitario, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getNumSucursal(), credito.getTasaInteres());
                myLogger.debug("pago unitario iter" + pago_unitario);
                tablaNueva = calculaTablaInsolutoComunal(credito, grupo, ciclo, saldo_capital, saldo_capital, pago_unitario, num_pagos, credito.getPeriodicidad(), Convertidor.dateToString(div_inicio.fechaPago), credito.getTasaInteres());

                for (int i = 0; i < tablaAmort.length; i++) {
                    myLogger.debug("contador " + i);
                    tablaAmort[i].abonoCapital = tablaNueva[i].abonoCapital;
                    tablaAmort[i].saldoCapital = tablaNueva[i].saldoCapital;
                    tablaAmort[i].interes = tablaNueva[i].interes;
                    tablaAmort[i].ivaInteres = tablaNueva[i].ivaInteres;
                    tablaAmort[i].montoPagar = tablaNueva[i].montoPagar;
                    /* Se lleva el control de los saldos
                     */
                    saldo_capital_calc = saldo_capital_calc + tablaAmort[i].abonoCapital;
                    saldo_con_interes_final = saldo_con_interes_final + tablaAmort[i].abonoCapital + tablaAmort[i].interes + tablaAmort[i].ivaInteres;
                    if (i == 0) {
                        saldo_total_aldia = saldo_capital + tablaAmort[i].interes + tablaAmort[i].ivaInteres;
                        saldo_interes = tablaAmort[i].interes;
                        saldo_iva_interes = tablaAmort[i].ivaInteres;
                        saldo_capital_1era_amort = tablaAmort[i].abonoCapital;
                        diferencia_interes = interes_inicial - tablaAmort[i].interes;
                        diferencia_iva_interes = iva_interes_inicial - tablaAmort[i].ivaInteres;
                    }

                }
                /*
                 * Se actualizan los valores del ultimo dividendo pagado incluyendo alli el monto pagado en anticipo
                 */
                div_inicio.montoPagar = saldo_capital_1era_amort + saldo_interes + saldo_iva_interes;
                div_inicio.capitalAnticipado = pago;
                div_inicio.abonoCapital = saldo_capital_1era_amort;
                div_inicio.interes = saldo_interes;
                div_inicio.ivaInteres = saldo_iva_interes;
                div_inicio.saldoCapital = tablaAmort[0].saldoCapital;
                div_inicio.quitaIntres = diferencia_interes;
                div_inicio.quitaIvaIntres = diferencia_iva_interes;

                /* Pasamos a actualizar la tabla de amortizacion con los nuevos valores
                 * Se inicializan de nuevo las variables que totalizan para actualizar la tabla de saldos
                 */
                for (int i = 0; i < tablaAmort.length; i++) {
                    TablaAmortDAO tablaDAOAct = new TablaAmortDAO();
                    /* Se actualiza la tabla de amortizacion 
                     * 				
                     */
                    int rs = tablaDAOAct.updateSaldosTablaAmortExtend(tablaAmort[i]);

                }
                /*
                 * Se actualiza el ultimo dividendo pagado			
                 */
                TablaAmortDAO tablaDAODiv = new TablaAmortDAO();
                tablaDAODiv.updateSaldosTablaAmortExtend(div_inicio);

            } else {
                /*
                 * En caso de que el saldo de capital sea negativo se debe de liquidar la tabla, esto no debe de suceder aqui ya que al detectar un monto mayor en 
                 * cierre de dia se liquida la tabla automáticamente JBL- AGO-13				
                 */
            }
            /*
             * Se actualiza la tabla de saldos			
             */
            myLogger.info("saldo capital " + saldo_capital + "saldo_capital_calc " + saldo_capital_calc);

            saldoVO = saldoIBS.getSaldo(credito.getNumCliente(), credito.getNumCredito());
            saldoVO.setCapitalPagado(saldoVO.getCapitalPagado() + pago_capital);
            saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - pago_capital);
            saldoVO.setSaldoInteres(saldo_interes);
            saldoVO.setSaldoInteresVigente(saldo_interes);
            saldoVO.setSaldoIvaInteres(saldo_iva_interes);
            saldoVO.setCapitalSigAmortizacion(saldo_capital_1era_amort);
            saldoVO.setInteresSigAmortizacion(saldo_interes);
            saldoVO.setIvaSigAmortizacion(saldo_iva_interes);
            // la fecha de pago se toma como la fecha de aplicacion
            saldoVO.setFechaUltimoPago(Convertidor.toSqlDate(pagoDAO.getUltimoPago(saldoVO.getReferencia())));
            saldoVO.setTotalVencido(0);
            saldoVO.setSaldoConInteresAlFinal(saldo_con_interes_final);
            saldoVO.setSaldoTotalAlDia(saldo_total_aldia);
            saldoVO.setNumeroPagosRealizados(pagoDAO.getNumeroPagos(saldoVO.getReferencia()));
            saldoVO.setMontoTotalPagado(saldoVO.getMontoTotalPagado() + pago_capital);
            saldoVO.setSaldoBucket(remanente + saldoVO.getSaldoBucket());
            saldoVO.setDiasMora(0);
            saldoVO.setCuotasVencidas(0);
            //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
            saldoVO.setInteresPorDevengar(saldoVO.getInteresPorDevengar() - diferencia_interes);
            saldoVO.setIvaInteresPorDevengar(saldoVO.getIvaInteresPorDevengar() - diferencia_iva_interes);

            myLogger.info("va a actualizar saldo de referencia " + saldoVO.getReferencia());
            // Se actualiza la tabla de saldos
            saldoIBS.updateSaldo(saldoVO);
            /*
             * Se actualiza la tabla de credito			
             */
            myLogger.info("llega a actualizacion de credito");
            
            myLogger.info("llega a actualizacion de credito");
//			credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
            credito.setNumDiasMora(dias_mora);
            credito.setMontoCuenta(remanente + credito.getMontoCuenta());
            credito.setFechaUltimaActualizacion(Convertidor.toSqlDate(fechaUltimoCierre));
            credito.setFechaUltimoPago(Convertidor.toSqlDate(fechaUltimoCierre));
            String strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
            double porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia / 100;
            
            /**JECB 01/10/2017
            Se omite calculo que decremeta la garantia en un 10% del monto del credito adicional a cancelar
            credito.setMontoCuentaCongelada(credito.getMontoCuentaCongelada() - (pago * porGarantia));
            */
            
            // SE ACTUALIZA EL MONTO DE LA CUENTA EN TABLA DE CREDITOS
            CreditoHelperCartera.actualizaSaldoCuenta(credito);

            /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
             */
            // se llenan los montos de los rubros
            ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            if (pago_capital > 0) {
                rubro.tipoRubro = ClientesConstants.EFECTIVO;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.debug("Array pago " + array_pago.toString());
            if (pago_capital > 0) {
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.CAPITAL;
                rubro.monto = pago_capital;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                array_pago.add(rubro);
            }
            myLogger.debug("rubros " + array_pago.toString());
            if (array_pago.size() > 0) {
                elementos = new RubrosVO[array_pago.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) array_pago.get(i);
                }
            }
            transacHelper.aplicaPago(credito, elementos, Convertidor.toSqlDate(fechaUltimoCierre));

            // la reduccion de interes de aplica como condonacion
            ArrayList<RubrosVO> array_condonacion = new ArrayList<RubrosVO>();
            RubrosVO rubro_con = new RubrosVO();
            RubrosVO elementos_con[] = null;
            if (diferencia_interes > 0) {
                rubro_con.tipoRubro = ClientesConstants.INTERES;
                rubro_con.monto = diferencia_interes;
                rubro_con.status = ClientesConstants.RUBRO_VIGENTE;
                array_condonacion.add(rubro_con);
            }
            myLogger.debug("Array condonacion " + array_condonacion.toString());
            if (diferencia_iva_interes > 0) {
                rubro_con = new RubrosVO();
                rubro_con.tipoRubro = ClientesConstants.IVA_INTERES;
                rubro_con.monto = diferencia_iva_interes;
                rubro_con.status = ClientesConstants.RUBRO_VIGENTE;
                array_condonacion.add(rubro_con);
            }
            myLogger.debug("rubros " + array_condonacion.toString());
            if (array_condonacion.size() > 0) {
                elementos_con = new RubrosVO[array_condonacion.size()];
                for (int i = 0; i < elementos_con.length; i++) {
                    elementos_con[i] = (RubrosVO) array_condonacion.get(i);
                }
            }
            transacHelper.registraCondonacion(credito, elementos_con, Convertidor.toSqlDate(fechaUltimoCierre));

        } catch (Exception e) {
            myLogger.error("aplicaPagoAnticipadoReduccionGrupo", e);
            throw new ClientesException(e.getMessage());
        }
        return remanente;
    }
}

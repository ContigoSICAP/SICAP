package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;

public class TablaAmortizacionHelper {

    public static TablaAmortizacionVO[] insertTablaInsolutoConsumo(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        System.out.println("ENTRO");
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;

        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementosTabla[] = null;

        try {

            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
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
                temp.comisionInicial = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.idCliente = cliente.idCliente;
                temp.idSolicitud = solicitud.idSolicitud;
                array.add(temp);
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Error en insert a Tabla de amortizacion Saldos Insolutos Consumo" + e);
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
                tasaInteres = tasaInteres * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
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
                    temp = new TablaAmortizacionVO();
                    temp.idCliente = cliente.idCliente;
                    temp.idSolicitud = solicitud.idSolicitud;
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

                    if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL && i != 0) {
                        fechaTemp = FechasUtil.getDate(fechaTemp, 7, 0);
                    }

                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comisionInicial = 0.00;
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
                    tabladao.addTablaAmortizacion(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortizacionVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortizacionVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementosTabla;

    }

    public static TablaAmortizacionVO[] insertTablaInsolutoMicro(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;

        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementosTabla[] = null;

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

            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();

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
                temp.comisionInicial = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.idCliente = cliente.idCliente;
                temp.idSolicitud = solicitud.idSolicitud;
                array.add(temp);
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Error en insert a Tabla de amortizacion saldos insolutos Microcrédito" + e);
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
                    temp = new TablaAmortizacionVO();
                    temp.idCliente = cliente.idCliente;
                    temp.idSolicitud = solicitud.idSolicitud;
                    temp.numPago = i + 1;

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
                    temp.comisionInicial = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);

                    tabladao.addTablaAmortizacion(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortizacionVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortizacionVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementosTabla;

    }

    public static TablaAmortizacionVO[] insertTablaInsolutoIndivSemanal(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
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

        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementosTabla[] = null;

        try {

            Date fechaTemp = Convertidor.stringToDate(fechaIni);
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();

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
                temp.comisionInicial = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.idCliente = cliente.idCliente;
                temp.idSolicitud = solicitud.idSolicitud;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                array.add(temp);
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Error en insert a Tabla de amortizacion saldos insolutos Comunal" + e);
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
                    temp = new TablaAmortizacionVO();
                    temp.idCliente = cliente.idCliente;
                    temp.idSolicitud = solicitud.idSolicitud;
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                    temp.numPago = i + 1;
                    fechaTemp = FechasUtil.getDate(fechaTemp, difDias, 1);
                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comisionInicial = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    System.out.println("interes " + interes);
                    System.out.println("ivaInteres " + ivaInteres);
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
                    tabladao.addTablaAmortizacion(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortizacionVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortizacionVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementosTabla;

    }

    public static TablaAmortizacionVO[] insertTablaInsolutoComunal(GrupoVO grupo, CicloGrupalVO ciclo, double pagoUnitario, int frecuenciaPago, String fechaIni, double tasaAnual) {
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

        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementosTabla[] = null;

        try {

            Date fechaTemp = Convertidor.stringToDate(fechaIni);
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();

            Double tasaInteres = 0.0;
            Double comision = 0.0;
            Double ivaComision = 0.0;

            try {
                double comisionSinIva = 0.00;
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(Convertidor.stringToDate(fechaIni));
                temp.saldoInicial = ciclo.montoConComision;
                temp.abonoCapital = 0;
                temp.saldoCapital = 0;
                // Se le quita el costo del seguro al monto comision para el calculo del monto apagar Agosto 2017
                double costoSeguro = Math.abs((ciclo.montoConComision - ciclo.monto));
                comision = (ciclo.montoConComision - costoSeguro) - ciclo.monto - ciclo.montoRefinanciado;
                comisionSinIva = Convertidor.getMontoIva(comision, grupo.sucursal, 1);
                ivaComision = comision - comisionSinIva;
                temp.comisionInicial = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.idCliente = grupo.idGrupo;
                temp.idSolicitud = ciclo.idCiclo;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                array.add(temp);
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Error en insert a Tabla de amortizacion saldos insolutos Comunal" + e);
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
                    temp = new TablaAmortizacionVO();
                    temp.idCliente = grupo.idGrupo;
                    temp.idSolicitud = ciclo.idCiclo;
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                    temp.numPago = i + 1;
                    fechaTemp = FechasUtil.getDate(fechaTemp, difDias, 1);
                    temp.fechaPago = Convertidor.toSqlDate(fechaTemp);
                    temp.saldoInicial = 0.00;
                    temp.saldoCapital = sdoInsoluto;
                    temp.comisionInicial = 0.00;
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
                    tabladao.addTablaAmortizacion(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortizacionVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortizacionVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementosTabla;

    }

    public static void insertTablaConsumo(int idCliente, int idSolicitud, int idSucursal, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        insertTablaConsumo(idCliente, idSolicitud, 0, idSucursal, monto, montoSinComision, plazo, frecuenciaPago, tasa, fechaInicio);
    }

    //Algoritmo para generar tabla de amortizaci�n con c�lculo de inter�s Global para productos(Consumo y CrediHogar)
    public static void insertTablaConsumo(int idCliente, int idSolicitud, int idDisposicion, int idSucursal, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
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
                temp.idDisposicion = idDisposicion;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Calculo Tabla Consumo Global" + e);
            }

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
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            }
            abonoCapital = monto / plazo;
            System.out.println("tasa " + tasa);
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
            e.printStackTrace();
            System.err.println("Exception Calculo Tabla Consumo Global:\n" + e);
            throw new ClientesException(e.getMessage());
        }

    }

    public static void insertTablaDescuentoNomina(int idCliente, int idSolicitud, int idSucursal, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
        if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL || frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            insertTablaDescuentoNominaQuin(idCliente, idSolicitud, idSucursal, monto, montoSinComision, plazo, frecuenciaPago, tasa, fechaInicio);
        } else {
            insertTablaDescuentoNominaSem(idCliente, idSolicitud, idSucursal, monto, montoSinComision, plazo, frecuenciaPago, tasa, fechaInicio);
        }
    }

    //Algoritmo para generar tabla de amortizaci�n con c�lculo de inter�s Global para productos(Consumo y CrediHogar)
    public static void insertTablaDescuentoNominaQuin(int idCliente, int idSolicitud, int idSucursal, double monto, double montoSinComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) throws ClientesException {
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
        SucursalVO sucVO = sucDAO.getSucursal(idSucursal);

        try {
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            comision = monto - montoSinComision;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;

            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaInicial);
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
                temp.idDisposicion = 0;
                temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INDIVIDUAL;
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Calculo Tabla Consumo Global" + e);
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

            System.out.println("dias " + dias);
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            }
            abonoCapital = monto / plazo;
            System.out.println("tasa " + tasa);
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            double tempMonto = abonoCapital + interes + ivaInteres;
// Se deja de rendondear al siguiente peso debido a que se cobra por transferencia			
//			montoApagar = java.lang.Math.ceil(tempMonto);
            montoApagar = (tempMonto);
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
                temp = new TablaAmortizacionVO();
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
                temp.idDisposicion = 0;
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
            e.printStackTrace();
            System.err.println("Exception Calculo Tabla Consumo Global:\n" + e);
            throw new ClientesException(e.getMessage());
        }

    }

    public static void insertTablaDescuentoNominaSem(int idCliente, int idSolicitud, int idSucursal, double monto, double montoConComision, int plazo, int frecuenciaPago, double tasa, Date fechaInicio) {
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
            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucVO = sucDAO.getSucursal(idSucursal);
            comision = montoConComision - monto;
            ivaComision = (comision - Convertidor.getMontoIva(comision, sucVO, 1));
            comision = comision - ivaComision;
            fechaArranque = FechasUtil.getNextWeekDate(fechaInicio, 3);
            fechaTemp = fechaArranque;
            try {
                temp.numPago = 0;
                temp.fechaPago = Convertidor.toSqlDate(fechaArranque);
                temp.saldoInicial = montoConComision;
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
                System.err.print("Error en primer insert a Tabla de amortizacion descuento nomina semanal" + e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            } else {
                tasa = tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO);
            }
            monto = montoConComision;
            abonoCapital = monto / plazo;
            interesTotal = ((monto * tasa) / 100) / 360;
            interesTotal = (interesTotal * dias);
            interes = interesTotal / plazo;
            ivaInteres = Convertidor.getMontoIva(interes, sucVO, 2);
            interes = interes - ivaInteres;
            double tempMonto = abonoCapital + interes + ivaInteres;
// Se deja de redondear hacia el entero superior debido a que se cobra via transferencia			
//			montoApagar = java.lang.Math.ceil(tempMonto);			
            montoApagar = (tempMonto);
            double interesMasIva = interes + ivaInteres;
            double ajuste = montoApagar - tempMonto;
            ajuste = ajuste * plazo;

            for (int i = 1; i <= plazo; i++) {
                temp = new TablaAmortizacionVO();
                temp.idCliente = idCliente;
                temp.idSolicitud = idSolicitud;
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
                temp.comisionInicial = 0.00;
                temp.ivaComision = 0.00;
                temp.interes = interes;
                temp.ivaInteres = ivaInteres;
                temp.montoPagar = abonoCapital + interes + ivaInteres;
                tabladao.addTablaAmortizacion(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception InsertTableGrupal:\n" + e);
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

            Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), cliente.idSucursal, cliente.solicitudes[indiceSolicitud].tipoOperacion);
            Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, tasa.valor);
            Double tasaCalculada = TablaAmortizacionHelper.calcTasa(cliente.solicitudes[indiceSolicitud].tipoOperacion, cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), cliente.idSucursal, tasaLogaritmo);

            insertTablaInsolutoConsumo(cliente, cliente.solicitudes[indiceSolicitud], cliente.solicitudes[indiceSolicitud].decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado, cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
            cliente.solicitudes[indiceSolicitud].amortizacion = new TablaAmortizacionDAO().getElementos(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
            cliente.solicitudes[indiceSolicitud].tasaCalculada = tasaCalculada;
        } catch (Exception e) {
            e.printStackTrace();
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

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL && operacion != ClientesConstants.MICROCREDITO) {
            pagos = pagos * 1;
        } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL && operacion == ClientesConstants.MICROCREDITO) {
            pagos = pagos * 4;
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
            System.err.println("Exception calcTasa:\n" + e);
        }

        if (tasaOriginal > 0) {
            return tasaAnual;
        } else {
            return tasaOriginal;
        }

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
            System.err.println("Exception getDaysBetweenPays:\n" + e);
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
            System.err.println("Exception getDaysBetweenPays:\n" + e);
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
            System.err.println("Exception getDaysBetweenPaysMicro:\n" + e);
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
            System.err.println("Exception MakeTable:\n");
            e.printStackTrace();
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
            System.err.println("Exception MakeTable:\n" + e);
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
        double interes = 0;
        double anios = 0.00;
        int pagoAnual = 0;
        double numPagos = pagos;

        try {
            boolean fronterizo = Convertidor.esFronterizo(idSucursal);
            if (tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                sumadias = pagos * 7;
            } else if (tipoOperacion == ClientesConstants.MICROCREDITO) {
                elementos = getDaysBetweenPaymentsMicro(pagos, frecuenciaPago, fechaIni);
            } else {
                elementos = getDaysBetweenPayments(pagos, frecuenciaPago, fechaIni);
            }
            if (tipoOperacion != ClientesConstants.GRUPAL && tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL) {
                if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) /*
                 * En caso de SELL FINANCE, el plazo no es en meses si no en numero de periodos					
                 */ {
                    if (tipoOperacion == ClientesConstants.SELL_FINANCE) {
                        pagos = pagos;
                    } else {
                        pagos = pagos * 2;
                    }
                }

                for (int i = 0; i < pagos; i++) {
                    sumadias += elementos[i];
                }
            }

            difDias = sumadias;
            if (tipoOperacion == ClientesConstants.MICROCREDITO) {
                if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) //pagoAnual = 12;
                {
                    numPagos = numPagos;
                } else if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                    //pagoAnual = 24;
                    numPagos *= 2;
                } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                    //pagoAnual = 48;
                    numPagos *= 4;
                }
                /*interes = (tasaAnual/100)/pagoAnual;
                 total = monto*interes;
                 anios = numPagos/pagoAnual;
                 intTotal = 1-(Math.pow((1+interes), (-anios*pagoAnual)));
                 pagoUnitario = total/intTotal;*/
                intTotal = ((monto * (tasaAnual / 12)) * numPagos) / 100;
                total = monto + intTotal + ivaIntTotal;
                pagoUnitario = total / numPagos;

            } else {
                if (fronterizo) {
                    tasaAnual = tasaAnual * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
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
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            //System.out.println("INTERES: " + tasaInteres);

        } catch (Exception e) {
            System.err.println("Exception getTasaLogaritmico:\n" + e);
        }

        if (tasaOriginal > 0) {
            return tasaInteres;
        } else {
            return tasaOriginal;
        }
    }

    public static double getTasaLogaritmicoMicro(double monto, double pagoUnitario, int meses, int frecuenciaPago, double tasaOriginal, int tipoOperacion) {
        //meses = duraci�n del cr�dito en meses
        //pagos = n�mero de amortizaciones del cr�dito
        //Periodos = n�mero de amortizaciones por a�o en base a la frecuencia de pago

        double tasaInteres = 0.00;
        double pagos = 0.00;
        int periodos = 0;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            pagos = meses * 4;
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
            //System.out.println("INTERES: " + tasaInteres);

        } catch (Exception e) {
            System.err.println("Exception getTasaLogaritmico:\n" + e);
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
            System.err.println("Exception calcFechaInicio:\n" + e);
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
            System.err.println("Exception calcFechaInicio:\n" + e);
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
            System.err.println("Exception calcFechaInicio:\n" + e);
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
            System.err.println("Exception calcFechaInicio:\n" + e);
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
            e.printStackTrace();
            System.err.println("Exception calcFechaInicio:\n" + e);
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
                System.err.print("Error en primer insert a Tabla de amortizacion grupal" + e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            } else {
                tasa = tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO);
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
            e.printStackTrace();
            System.err.println("Exception InsertTableGrupal:\n" + e);
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
                System.err.print("Error en primer insert a Tabla de amortizacion micro" + e);
            }
            if (Convertidor.esFronterizo(sucVO)) {
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
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
            e.printStackTrace();
            System.err.println("Exception InsertTableGrupal:\n" + e);
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
                System.err.print("Error en insert a Tabla de amortizacion" + e);
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
            tasa = tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO);

            interesTotal = ((monto * FormatUtil.roundDouble(tasa, 2)) / 100) / 360;
            interesTotal = (interesTotal * dias);

            interes = interesTotal / pagos;
            if (fronterizo) {
                ivaInteres = interes - (interes / (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO));
                interes = interes - ivaInteres;
            } else {
                ivaInteres = interes - (interes / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
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
            System.err.println("Exception InsertTableVivienda:\n" + e);
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
                tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
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
            System.err.println("insertTablaMaxZapatos : " + e);
            e.printStackTrace();
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
            e.printStackTrace();
            System.err.println("Exception getDaysBetweenPays:\n" + e);
        }
        return elementos;

    }

    public static void generaTablaAmortizacion(ClienteVO cliente, SolicitudVO solicitud) throws ClientesException {

        double montoInicialConComision = 0;
        double montoSinComision = 0;
        Double pagoUnitario = null;
        Double tasaLogaritmo = null;
        Double tasaCalculada = null;
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
                        pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, montoInicialConComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, solicitud.tipoOperacion);
                        tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor);
                        tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo);
                        TablaAmortizacionHelper.insertTablaInsolutoConsumo(cliente, solicitud, montoInicialConComision, montoSinComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada);

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
                        pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, montoInicialConComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, solicitud.tipoOperacion);
                        tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor);
                        tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo);
                        TablaAmortizacionHelper.insertTablaInsolutoMicro(cliente, solicitud, montoInicialConComision, montoSinComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada);

                        //TablaAmortizacionHelper.insertTablaMicro(solicitud.idCliente, solicitud.idSolicitud, cliente.idSucursal, montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, tasa.valor, new Date());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        break;
                    case ClientesConstants.GRUPAL:
                        Logger.debug("Seguro para el cliente " + solicitud.idCliente + " con solicitud " + solicitud.idSolicitud);
                        /*IntegranteCicloDAO integraCiclo = new IntegranteCicloDAO();
                         CicloGrupalDAO cicloDao = new CicloGrupalDAO();
                         GrupoDAO grupoDao = new GrupoDAO();
                         CreditoVO credito = integraCiclo.numGrupoCicloXClienteSolisitud(solicitud);
                         CicloGrupalVO ciclo = cicloDao.getCiclo(credito.idCliente, credito.idSolicitud);
                         GrupoVO grupo = grupoDao.getGruposID(credito.idCliente);
                         System.out.println("ciclo.plazo: "+ciclo.plazo);
                         System.out.println("ciclo.montoConComision: "+ciclo.montoConComision);
                         System.out.println("ciclo.monto: "+ciclo.monto);
                         System.out.println("ciclo.montoRefinanciado: "+ciclo.montoRefinanciado);
                         System.out.println("ciclo.idCiclo: "+ciclo.idCiclo);
                         System.out.println("grupo.idGrupo: "+grupo.idGrupo);
                         System.out.println("grupo.sucursal: "+grupo.sucursal);
                         montoSinComision = ciclo.montoConComision;
                        
                         //montoSinComision = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
                         montoSinComision = ClientesUtil.calculaMontoSinComision(ciclo.monto, ciclo., catComisiones);
                         montoSinComision += solicitud.seguro.primaTotal;
                         montoInicialConComision = ClientesUtil.calculaMontoConComision(montoSinComision, solicitud.decisionComite.comision, catComisiones);
                         if (solicitud.amortizacion != null) {
                         System.out.println("Elimina tabla**********");
                         //*new TablaAmortizacionDAO().delTablaAmortizacion(credito.idCliente, credito.idSolicitud, ClientesConstants.AMORTIZACION_GRUPAL);
                         }
                         //Genera la tabla de amortizaci�n
                         pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, montoInicialConComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, solicitud.tipoOperacion);
                         tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor);
                         tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, montoInicialConComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo);
                         //TablaAmortizacionHelper.insertTablaInsolutoConsumo(cliente, solicitud, montoInicialConComision, montoSinComision, pagoUnitario, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada);
                         //*TablaAmortizacionHelper.insertTablaInsolutoComunal();

                         //TablaAmortizacionHelper.insertTablaConsumo(solicitud.idCliente, solicitud.idSolicitud, idSucursal , montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor, new Date());
                         solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);*/
                        break;
                    case ClientesConstants.VIVIENDA:
                        montoSinComision = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
                        montoSinComision += solicitud.seguro.primaTotal;
                        montoInicialConComision = ClientesUtil.calculaMontoConComision(montoSinComision, solicitud.decisionComite.comision, catComisiones);
                        if (solicitud.amortizacion != null) {
                            new TablaAmortizacionDAO().delTablaAmortizacion(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        }
                        TablaAmortizacionHelper.insertTablaVivienda(solicitud.idCliente, solicitud.idSolicitud, cliente.idSucursal, montoInicialConComision, montoSinComision, solicitud.decisionComite.plazoAutorizado, solicitud.decisionComite.frecuenciaPago, tasa.valor, new Date());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        break;
                }
            } catch (Exception e) {
                throw new ClientesException(e.getMessage());
            }
        }
    }

    public void actualizaTablaInsolutoMicro(ClienteVO cliente, SolicitudVO solicitud, double montoConComision, double montoSinComision, double pagoUnitario, int plazo, int frecuenciaPago, String fechaIni, double tasaAnual) {
        double interes = 0.00;
        double ivaInteres = 0.00;
        double abonoCapital = 0.00;
        double abonoInteres = 0.00;
        double sdoInsoluto = montoConComision;

        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementosTabla[] = null;

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

            TablaAmortizacionVO temp = new TablaAmortizacionVO();
            TablaAmortizacionDAO tabladao = new TablaAmortizacionDAO();

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
                temp.comisionInicial = comisionSinIva;
                temp.ivaComision = ivaComision;
                temp.interes = 0;
                temp.ivaInteres = 0;
                temp.montoPagar = comision;
                temp.idCliente = cliente.idCliente;
                temp.idSolicitud = solicitud.idSolicitud;
                array.add(temp);
                tabladao.addTablaAmortizacion(temp);
            } catch (Exception e) {
                System.err.print("Error en insert a Tabla de amortizacion saldos insolutos Microcrédito" + e);
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
                    temp = new TablaAmortizacionVO();
                    temp.idCliente = cliente.idCliente;
                    temp.idSolicitud = solicitud.idSolicitud;
                    temp.numPago = i + 1;

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
                    temp.comisionInicial = 0.00;
                    temp.ivaComision = 0.00;
                    temp.interes = interes;
                    temp.ivaInteres = ivaInteres;
                    temp.abonoCapital = abonoCapital;
                    temp.montoPagar = java.lang.Math.ceil(abonoCapital + abonoInteres);

                    tabladao.addTablaAmortizacion(temp);
                    array.add(temp);
                }

                if (array.size() > 0) {
                    elementosTabla = new TablaAmortizacionVO[array.size()];
                    for (int y = 0; y < elementosTabla.length; y++) {
                        elementosTabla[y] = (TablaAmortizacionVO) array.get(y);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double calcPagoUnitarioInsolutoDirectoCorto(double tasaAnual, double monto, int pagos, int frecuenciaPago, String fechaIni, int idSucursal, int tipoOperacion) {
        
        Double pagoUnitario = 0.00;
        double factor = 1.00;
        double resultado = 0;
        double valorTasaPeriodo = 0;
        int periodos_ano = 0;

        try {
            boolean fronterizo = Convertidor.esFronterizo(idSucursal);
            //Se incluye consumo en la categoria de semanales			
            if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
                factor = 2;
                periodos_ano = 24;
            } else if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                factor = 4.2857;
                periodos_ano = 52;
            } else if (frecuenciaPago == ClientesConstants.PAGO_CATORCENAL) {
                periodos_ano = 26;
                factor = 2.1429;
            } else if (frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
                factor = 1;
                periodos_ano = 12;
            } else {
                periodos_ano = 12;
                factor = 1;
            }
            if (fronterizo) {
                tasaAnual = ((tasaAnual * (1 + ClientesConstants.TASA_IVA_FRONTERIZO)) / 12) / 100;
            } else {
                tasaAnual = tasaAnual / 12 / 100;
            }

            valorTasaPeriodo = tasaAnual / periodos_ano * 12;
            double numerador = monto * valorTasaPeriodo * java.lang.Math.pow(1 + valorTasaPeriodo, pagos);
            double denominador = (java.lang.Math.pow(1 + valorTasaPeriodo, pagos)) - 1;
            resultado = FormatUtil.roundDouble((numerador / denominador), 2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        pagoUnitario = resultado;
        
        return pagoUnitario;
	}

        	// JBL 27-FEB 2013- Se calcula el pago unitario con iteraciones a partirl de un pago unitario inicial
	public static double calcPagoUnitarioIter (int operacion, double monto, double pagoUnitario, int pagos, int frecuenciaPago, String fechaIni, int idSucursal, double tasaAnual) throws Exception{
		boolean found = false;
		double abonoInteres = 0.00;
		double abonoCapital = 0.00;
		double sdoInsoluto = monto;
		int	salto_grande = 5;
		Integer elementos[] = null;
		int backForward = 0;
		Date fechaTemp = null;
		double tasaOriginal = tasaAnual;
		double pagoUnitarioCalc = pagoUnitario;
//		tasaAnual = tasaAnual*(1+ ClientesConstants.TASA_IVA_NO_FRONTERIZO);
		boolean migracion = false;
		Calendar cal = Calendar.getInstance();
	    GregorianCalendar fechaOut = new GregorianCalendar();
	    fechaOut.setTime(cal.getTime());
	    fechaOut.add(Calendar.DAY_OF_MONTH, 10);

		
		if ( operacion==ClientesConstants.CONSUMO )
			elementos = getDaysBetweenPayments(pagos, frecuenciaPago, fechaIni);
		else if( operacion==ClientesConstants.MICROCREDITO)
			elementos = getDaysBetweenPayments(pagos, frecuenciaPago, fechaIni);
		
		if( frecuenciaPago == ClientesConstants.PAGO_SEMANAL )
			pagos=pagos*1;
		else if ( frecuenciaPago == ClientesConstants.PAGO_CATORCENAL ){
			if (operacion==ClientesConstants.SELL_FINANCE )
				pagos=pagos;
			else 
				pagos=pagos * 2;			
		}
		else if ( frecuenciaPago == ClientesConstants.PAGO_QUINCENAL ){
			if (operacion==ClientesConstants.SELL_FINANCE )
				pagos=pagos;
			else 
				pagos=pagos * 2;
		}
		else if ( frecuenciaPago == ClientesConstants.PAGO_BIMESTRAL )
			pagos=pagos/2;
		else if ( frecuenciaPago == ClientesConstants.PAGO_SEMESTRAL )
			pagos=pagos/6;

		int difDias = 0;
		int bucle = 0;
		int cambio_sentido = 0;
		double resta = 0;
		double resta_anterior = 0;
		double resta_anterior_minima = 0;
		boolean salto_min = true;
		boolean salto_min_forzoso = false;
		
		//tasaAnual = FormatUtil.roundDecimal(tasaAnual, 10);
		pagoUnitarioCalc = FormatUtil.roundDecimal(pagoUnitarioCalc, 6);
		try{
			while (!found ){
				Logger.debug("backforward = " +backForward);
				if(backForward==0){
					if (java.lang.Math.abs(resta) < pagoUnitarioCalc*.1){
						pagoUnitarioCalc = pagoUnitarioCalc+1;
						salto_min = true;
					}
					else if (salto_min_forzoso){
						pagoUnitarioCalc = pagoUnitarioCalc+1;
						salto_min = true;
					}
					else {
						pagoUnitarioCalc = pagoUnitarioCalc+ salto_grande;
						salto_min = false;
					}
				}
				else {
					if (java.lang.Math.abs(resta) < pagoUnitarioCalc*.1){
						pagoUnitarioCalc = pagoUnitarioCalc-1;
						salto_min = true;
						}
					else if (salto_min_forzoso){
						pagoUnitarioCalc = pagoUnitarioCalc-1;
						salto_min = true;
					}
					else { 
						pagoUnitarioCalc = pagoUnitarioCalc-salto_grande;
						salto_min = false;
					}
				}
						
				
				sdoInsoluto = monto;
				for (int i=0; i<pagos-1; i++){
					if ( frecuenciaPago == ClientesConstants.PAGO_SEMANAL )
						difDias = 7;
					else if ( frecuenciaPago == ClientesConstants.PAGO_CATORCENAL )
						difDias = 14;
					else
						difDias = elementos[i];

					abonoInteres = sdoInsoluto*difDias*((tasaAnual/100)/360);
					abonoCapital = pagoUnitarioCalc-abonoInteres;
					sdoInsoluto = sdoInsoluto-abonoCapital;
				}

				if ( frecuenciaPago == ClientesConstants.PAGO_SEMANAL )
					difDias = 7;
				else if ( frecuenciaPago == ClientesConstants.PAGO_CATORCENAL )
					difDias = 14;
				else
					difDias = elementos[pagos-1];

				abonoCapital = sdoInsoluto;
				abonoInteres = sdoInsoluto*difDias*((tasaAnual/100)/360);

				double ultimaCuota = abonoCapital+abonoInteres;
				Logger.debug("abonoInteres:   "+ abonoInteres);
				Logger.debug("abonocapital:   "+ abonoCapital);
				Logger.debug("pagoUnitario Inicial:   "+ pagoUnitario);
				Logger.debug("pagoUnitario Calculado: "+ pagoUnitarioCalc);
				Logger.debug("ultima cuota:           "+ ultimaCuota);
				resta = pagoUnitarioCalc-ultimaCuota;

				
//				if(resta > 0.00 && java.lang.Math.abs(resta)<pagoUnitarioCalc*.05 && salto_min)
				if(java.lang.Math.abs(resta)<pagoUnitarioCalc*.01 && salto_min)
					found=true;
				
				pagoUnitarioCalc = java.lang.Math.ceil(pagoUnitarioCalc);
				
				resta = java.lang.Math.abs(resta);

/* Si la diferencia es mayor que la anterior se cambia de sentido */				
				if(resta > resta_anterior && bucle > 0){
					if(backForward==0){
						backForward=1;
						cambio_sentido = cambio_sentido + 1;
						Logger.debug("debe comenzar a restar");
					}
					else if(backForward == 1){
						backForward=0;
						cambio_sentido = cambio_sentido + 1;
						Logger.debug("debe comenzar a sumar");
					}
				}
				resta_anterior = resta;
				bucle = bucle + 1;
				
/* En caso de que vuelva al valor minimo despues de varios intentos se considera como buena */				
				if ((Double.compare(resta_anterior_minima, resta_anterior) == 0) && cambio_sentido > 2 && salto_min)
					found = true;
					
				if (bucle > 1){
					if (resta_anterior < resta_anterior_minima)
						resta_anterior_minima = resta_anterior;
				} else if (bucle == 1)
					resta_anterior_minima = resta_anterior;
					

/* Si cambia de sentido dos veces se irá por el salto minimo  */				
				if (cambio_sentido == 2)
					salto_min_forzoso = true;
/* Se incluyen limites para salir del bucle */				
				if (bucle == 200 || cambio_sentido > 5){
					Logger.debug("sale por cambio de sentido o maximo de iteraciones,cambio sentido = " +cambio_sentido);
					found = true;				
				}
			}
			
		}catch(Exception e){
			System.err.println ("Exception calcPagoUnitarioIter:\n" + e);
		}
		
		if ( pagoUnitarioCalc > 0 ){
			Logger.debug("pagoUnitarioCalc : " + pagoUnitarioCalc);
			return pagoUnitarioCalc;
		} else {
			Logger.debug("pago unitario original : " + pagoUnitario);
			return pagoUnitario;
		}

	}
        
        public static TablaAmortizacionVO[] insertTablaInsolutoComunalInterciclo(GrupoVO grupoVO, CicloGrupalVO cicloVO, double montoIncremento, double pagoUnitario, double saldoCapital, int Semdisp) {
            
            TablaAmortizacionVO[] tablaAmortInterciclo = null;
            TablaAmortizacionVO tablaAmortClie = new TablaAmortizacionVO();
            TablaAmortVO tablaAmortCart = new TablaAmortVO();
            ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
            TablaAmortVO[] tablaNueva = null;
            CreditoCartVO creditoVO = new CreditoCartVO();
            TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
            TablaAmortizacionDAO tablaClieDAO = new TablaAmortizacionDAO();
            try {
                tablaAmortInterciclo = tablaClieDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_GRUPAL);
                for (int i = 0; i < (Semdisp+1); i++) {
                    TablaAmortizacionVO amortizacion = tablaAmortInterciclo[i];
                    if(i == 0 ){
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    } else if (i >0 && i < Semdisp){//modificar aqui para que tome la semana
                        tablaAmortCart = tablaCartDAO.getDivPago(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), amortizacion.numPago);
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                        amortizacion.abonoCapital = tablaAmortCart.getAbonoCapital();
                        amortizacion.saldoCapital = tablaAmortCart.getSaldoCapital();
                        amortizacion.interes = tablaAmortCart.getInteres();
                        amortizacion.ivaInteres = tablaAmortCart.getIvaInteres();
                        amortizacion.montoPagar = tablaAmortCart.getMontoPagar()+tablaAmortCart.getTotalPagado()-tablaAmortCart.getMultaPagado()-tablaAmortCart.getIvaMultaPagado();
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    } else if (i == Semdisp){//modificar aque para que tome la semana
                        tablaAmortCart = tablaCartDAO.getDivPago(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), amortizacion.numPago);
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                        amortizacion.abonoCapital = tablaAmortCart.getAbonoCapital();
                        amortizacion.saldoCapital = tablaAmortCart.getSaldoCapital() + montoIncremento;
                        amortizacion.interes = tablaAmortCart.getInteres();
                        amortizacion.ivaInteres = tablaAmortCart.getIvaInteres();
                        amortizacion.montoPagar = tablaAmortCart.getMontoPagar()+tablaAmortCart.getTotalPagado()-tablaAmortCart.getMultaPagado()-tablaAmortCart.getIvaMultaPagado();
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    }
                    //System.out.println(amortizacion.numPago+" "+amortizacion.fechaPago+" "+amortizacion.saldoInicial+" "+amortizacion.abonoCapital+" "+amortizacion.saldoCapital+" "+amortizacion.interes+" "+amortizacion.ivaInteres+" "+amortizacion.montoPagar);
                }
                creditoVO = new CreditoCartDAO().getCreditoClienteSol(cicloVO.getIdGrupo(), cicloVO.getIdCiclo());
                arrTabla = tablaCartDAO.getTablaAmortizacion(grupoVO, null);
                for (int i = 0; i < Semdisp; i++) {
                    arrTabla.remove(0);
                }
                tablaNueva = TablaAmortHelper.calculaTablaInsolutoComunal(null, grupoVO, cicloVO, saldoCapital, saldoCapital, pagoUnitario, arrTabla.size(), creditoVO.getPeriodicidad(), Convertidor.dateToString(tablaAmortInterciclo[5].fechaPago), creditoVO.getTasaInteres());
                for (int i = 0; i < arrTabla.size(); i++) {
                    tablaAmortClie.idCliente = cicloVO.getIdGrupo();
                    tablaAmortClie.idSolicitud = cicloVO.getIdCiclo();
                    tablaAmortClie.idDisposicion = 0;
                    tablaAmortClie.tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                    tablaAmortClie.numPago = arrTabla.get(i).getNumPago();
                    tablaAmortClie.fechaPago = arrTabla.get(i).getFechaPago();
                    tablaAmortClie.abonoCapital = tablaNueva[i].getAbonoCapital();
                    tablaAmortClie.saldoCapital = tablaNueva[i].getSaldoCapital();
                    tablaAmortClie.interes = tablaNueva[i].getInteres();
                    tablaAmortClie.ivaInteres = tablaNueva[i].getIvaInteres();
                    tablaAmortClie.montoPagar = tablaNueva[i].getMontoPagar();
                    //System.out.println(tablaAmortClie.numPago+" "+tablaAmortClie.fechaPago+" "+tablaAmortClie.saldoInicial+" "+tablaAmortClie.abonoCapital+" "+tablaAmortClie.saldoCapital+" "+tablaAmortClie.interes+" "+tablaAmortClie.ivaInteres+" "+tablaAmortClie.montoPagar);
                    tablaClieDAO.addTablaAmortizacion(tablaAmortClie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
        
        public static TablaAmortizacionVO[] insertTablaInsolutoComunalAdicional(GrupoVO grupoVO, CicloGrupalVO cicloVO, double montoIncremento, double pagoUnitario, double saldoCapital, int Semdisp) {
            
            TablaAmortizacionVO[] tablaAmortInterciclo = null;
            TablaAmortizacionVO tablaAmortClie = new TablaAmortizacionVO();
            TablaAmortVO tablaAmortCart = new TablaAmortVO();
            ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
            TablaAmortVO[] tablaNueva = null;
            CreditoCartVO creditoVO = new CreditoCartVO();
            TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
            TablaAmortizacionDAO tablaClieDAO = new TablaAmortizacionDAO();
            int plazoAdicional = 0;
            try {
                tablaAmortInterciclo = tablaClieDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
                for (int i = 0; i < (Semdisp+1); i++) {
                    TablaAmortizacionVO amortizacion = tablaAmortInterciclo[i];
                    if(i == 0 ){
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    } else if (i >0 && i < Semdisp){//modificar aqui para que tome la semana
                        tablaAmortCart = tablaCartDAO.getDivPago(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), amortizacion.numPago);
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                        amortizacion.abonoCapital = tablaAmortCart.getAbonoCapital();
                        amortizacion.saldoCapital = tablaAmortCart.getSaldoCapital();
                        amortizacion.interes = tablaAmortCart.getInteres();
                        amortizacion.ivaInteres = tablaAmortCart.getIvaInteres();
                        amortizacion.montoPagar = tablaAmortCart.getMontoPagar()+tablaAmortCart.getTotalPagado()-tablaAmortCart.getMultaPagado()-tablaAmortCart.getIvaMultaPagado();
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    } else if (i == Semdisp){//modificar aque para que tome la semana
                        tablaAmortCart = tablaCartDAO.getDivPago(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), amortizacion.numPago);
                        amortizacion.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                        amortizacion.abonoCapital = tablaAmortCart.getAbonoCapital();
                        amortizacion.saldoCapital = tablaAmortCart.getSaldoCapital() + montoIncremento;
                        amortizacion.interes = tablaAmortCart.getInteres();
                        amortizacion.ivaInteres = tablaAmortCart.getIvaInteres();
                        amortizacion.montoPagar = tablaAmortCart.getMontoPagar()+tablaAmortCart.getTotalPagado()-tablaAmortCart.getMultaPagado()-tablaAmortCart.getIvaMultaPagado();
                        tablaClieDAO.addTablaAmortizacion(amortizacion);
                    }
                    //System.out.println(amortizacion.numPago+" "+amortizacion.fechaPago+" "+amortizacion.saldoInicial+" "+amortizacion.abonoCapital+" "+amortizacion.saldoCapital+" "+amortizacion.interes+" "+amortizacion.ivaInteres+" "+amortizacion.montoPagar);
                }
                creditoVO = new CreditoCartDAO().getCreditoClienteSol(cicloVO.getIdGrupo(), cicloVO.getIdCiclo());
                arrTabla = tablaCartDAO.getTablaAmortizacion(grupoVO, null);
                for (int i = 0; i < Semdisp; i++) {
                    arrTabla.remove(0);
                }
                tablaNueva = TablaAmortHelper.calculaTablaInsolutoComunal(null, grupoVO, cicloVO, saldoCapital, saldoCapital, pagoUnitario, arrTabla.size(), creditoVO.getPeriodicidad(), Convertidor.dateToString(arrTabla.get(0).fechaPago), creditoVO.getTasaInteres());
                for (int i = 0; i < arrTabla.size(); i++) {
                    tablaAmortClie.idCliente = cicloVO.getIdGrupo();
                    tablaAmortClie.idSolicitud = cicloVO.getIdCiclo();
                    tablaAmortClie.idDisposicion = 0;
                    tablaAmortClie.tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                    tablaAmortClie.numPago = arrTabla.get(i).getNumPago();
                    tablaAmortClie.fechaPago = arrTabla.get(i).getFechaPago();
                    tablaAmortClie.abonoCapital = tablaNueva[i].getAbonoCapital();
                    tablaAmortClie.saldoCapital = tablaNueva[i].getSaldoCapital();
                    tablaAmortClie.interes = tablaNueva[i].getInteres();
                    tablaAmortClie.ivaInteres = tablaNueva[i].getIvaInteres();
                    tablaAmortClie.montoPagar = tablaNueva[i].getMontoPagar();
                    //System.out.println(tablaAmortClie.numPago+" "+tablaAmortClie.fechaPago+" "+tablaAmortClie.saldoInicial+" "+tablaAmortClie.abonoCapital+" "+tablaAmortClie.saldoCapital+" "+tablaAmortClie.interes+" "+tablaAmortClie.ivaInteres+" "+tablaAmortClie.montoPagar);
                    tablaClieDAO.addTablaAmortizacion(tablaAmortClie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
        
        public static void insertTablaGlobalInterciclo(int idGrupal, int idCiclo, int idSucursal, double monto, double montoConComision, double tasa, double tasaComision, Date fechaInicio, int plazo) {
            
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
                /*try {
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
                    System.err.print("Error en primer insert a Tabla de amortizacion grupal" + e);
                }*/
                if (Convertidor.esFronterizo(sucVO)) {
                    tasa = tasa * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
                } else {
                    tasa = tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO);
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
                    temp.tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
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
                e.printStackTrace();
                System.err.println("Exception InsertTableGrupal:\n" + e);
            }

        }

    }


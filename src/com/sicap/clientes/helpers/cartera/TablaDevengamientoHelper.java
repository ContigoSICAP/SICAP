package com.sicap.clientes.helpers.cartera;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.DevengamientoDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DevengamientoVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class TablaDevengamientoHelper {

    private static Logger myLogger = Logger.getLogger(TablaDevengamientoHelper.class);

    public static void regitroDevengamientoDiario(TablaAmortVO[] tablaAmort, int frecuenciaPago) {
        System.out.println("com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper.regitroDevengamientoDiario()");
        DevengamientoVO devegamientoVO = null;
        DevengamientoDAO devegamientoDAO = new DevengamientoDAO();
        int periodoDias = 0, difDias = 0;
        try {
            if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
                periodoDias = 7;
            }
            for (int i = 0; i < tablaAmort.length; i++) {
                TablaAmortVO tablaAmortVO = tablaAmort[i];
                devegamientoVO = new DevengamientoVO(tablaAmortVO.getNumPago(), tablaAmortVO.getFechaPago(), tablaAmortVO.getNumCliente(), tablaAmortVO.getNumCredito(), tablaAmortVO.getInteres() / periodoDias, tablaAmortVO.getIvaInteres() / periodoDias, 0);
                difDias = periodoDias;
                System.out.println("Fecha amortizacion: "+tablaAmortVO.getFechaPago());
                for (int j = 0; j < periodoDias; j++) {
                    difDias -= 1;
                    devegamientoVO.setFechaDevengamiento(Convertidor.toSqlDate(FechasUtil.getRestarDias(tablaAmortVO.getFechaPago(), difDias)));
                    devegamientoDAO.insertDevengamiento(devegamientoVO, null);
                    System.out.println("TablaDevengamientoHelper para insert: "+devegamientoVO.getFechaDevengamiento());
                    System.out.println("");
                }
            }
        } catch (Exception e) {
            myLogger.error("regitroDevengamientoDiario", e);
        }
    }

    public static void devengamientoAtrasado(CreditoCartVO creditoVO, Date fechaFin) {
        System.out.println("com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper.devengamientoAtrasado()");
        DevengamientoVO devengamientoVO = new DevengamientoVO();
        RubrosVO rubro = new RubrosVO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        ArrayList<RubrosVO> array_dev = new ArrayList<RubrosVO>();
        DevengamientoDAO devengamientoDAO = new DevengamientoDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        try {
            devengamientoVO = devengamientoDAO.getDevengamientoAtrasado(creditoVO, Convertidor.toSqlDate(fechaFin));
            rubro = new RubrosVO();
            RubrosVO elementos_dev[] = null;
            rubro.tipoRubro = ClientesConstants.INTERES;
            rubro.monto = devengamientoVO.getInteres();
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            array_dev.add(rubro);
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.IVA_INTERES;
            rubro.monto = devengamientoVO.getIvaInteres();
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            array_dev.add(rubro);
            if (array_dev.size() > 0) {
                elementos_dev = new RubrosVO[array_dev.size()];
                for (int i = 0; i < elementos_dev.length; i++) {
                    elementos_dev[i] = (RubrosVO) array_dev.get(i);
                }
            }
            if (elementos_dev.length > 0) {
                transacHelper.registraProvision(creditoVO, elementos_dev, fechaFin);
            }
            saldoVO = saldoDAO.getSaldo(creditoVO.getNumCliente(), creditoVO.getNumCredito());
            saldoVO.setInteresPorDevengar(saldoVO.getInteresPorDevengar() - devengamientoVO.getInteres());
            saldoVO.setIvaInteresPorDevengar(saldoVO.getIvaInteresPorDevengar() - devengamientoVO.getIvaInteres());
            saldoVO.setInteresDevengados(saldoVO.getInteresDevengados() + devengamientoVO.getInteres());
            saldoVO.setIvaInteresDevengados(saldoVO.getIvaInteresDevengados() + devengamientoVO.getIvaInteres());
            saldoDAO.updateSaldo(saldoVO);
            devengamientoDAO.setDevengamientoAtrasado(creditoVO, Convertidor.toSqlDate(fechaFin));
        } catch (Exception e) {
            myLogger.error("regitroDevengamientoDiario", e);
        }
    }

    public void ajustaDevengamiento(CreditoCartVO creditoVO, SaldoIBSVO saldoVO) {
        System.out.println("com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper.ajustaDevengamiento()");
        DevengamientoVO devengamientoVO = null;
        DevengamientoVO devengarVO = null;
        GrupoVO grupoVO = new GrupoVO();
        ArrayList<TablaAmortVO> arrTablaVO = new ArrayList<TablaAmortVO>();
        DevengamientoDAO devenDAO = new DevengamientoDAO();
        int periodoDias = 0, diasDeven = 0;
        double conInteres = 0, conIvaInteres = 0;
        boolean fechaDevengamientoMayorAmortizacion = false;
        try {
            if (creditoVO.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
                periodoDias = 7;
            }
            grupoVO.setIdGrupo(creditoVO.getNumCliente());
            grupoVO.setIdGrupoIBS(creditoVO.getNumCredito());
            devengamientoVO = devenDAO.getSigDevengamiento(creditoVO, null);
            arrTablaVO = new TablaAmortDAO().getTablaAmortizacion(grupoVO, null);
            diasDeven = devenDAO.getDiasDevengadosPago(devengamientoVO, null);
            conInteres = (arrTablaVO.get(devengamientoVO.getNumPago() - 1).getQuitaIntres() / periodoDias) * diasDeven;
            conIvaInteres = (arrTablaVO.get(devengamientoVO.getNumPago() - 1).getQuitaIvaIntres() / periodoDias) * diasDeven;
            devenDAO.deleteDevengamiento(devengamientoVO, null);
            for (TablaAmortVO tablaAmortVO : arrTablaVO) {
                if (tablaAmortVO.getNumPago() == devengamientoVO.getNumPago()) {
                    do {
                        devengarVO = new DevengamientoVO(tablaAmortVO.getNumPago(), devengamientoVO.getFechaDevengamiento(), creditoVO.getNumCliente(), creditoVO.getNumCredito(), tablaAmortVO.getInteres() / periodoDias, tablaAmortVO.getIvaInteres() / periodoDias, 0);
                        devengarVO.setInteres(devengarVO.getInteres() - conInteres);
                        devengarVO.setIvaInteres(devengarVO.getIvaInteres() - conIvaInteres);
                        System.out.println("Se inserta devengam fecha: "+devengarVO.getFechaDevengamiento());
                        devenDAO.insertDevengamiento(devengarVO, null);
                        conInteres = 0;
                        conIvaInteres = 0;
                        devengamientoVO.setFechaDevengamiento(Convertidor.toSqlDate(FechasUtil.getRestarDias(devengarVO.getFechaDevengamiento(), -1)));
                        //Eliminamos horas, minutos, segundos a las fechas
                        Date truncFechaDevengamiento = FechasUtil.truncDate(devengamientoVO.getFechaDevengamiento());
                        Date truncFechaPagoAmort = FechasUtil.truncDate(tablaAmortVO.getFechaPago());
                        
                        //Validamos si las fecha de devengamiento es mayor que la fecha de la tabla amortizacion
                        fechaDevengamientoMayorAmortizacion = truncFechaDevengamiento.after(truncFechaPagoAmort);
                        System.out.println("Fecha trunc devengamiento: "+truncFechaDevengamiento);
                        System.out.println("Fecha tabla pago amortizacion : "+truncFechaPagoAmort);
                        System.out.println("Validacion: "+!fechaDevengamientoMayorAmortizacion);
                    } while (!fechaDevengamientoMayorAmortizacion);
                    devengamientoVO = new DevengamientoVO(devengarVO.getNumPago() + 1, Convertidor.toSqlDate(FechasUtil.getRestarDias(devengarVO.getFechaDevengamiento(), -1)), devengarVO.getNumCliente(), devengarVO.getNumCredito(), 0, 0, 0);
                    System.out.println("Fecha aumentada nuevamente: "+devengamientoVO.getFechaDevengamiento());
                }
            }
            devengarVO = devenDAO.getDevengamientoLiquidar(creditoVO, null);
            saldoVO.setInteresPorDevengar(devengarVO.getInteres());
            saldoVO.setIvaInteresPorDevengar(devengarVO.getIvaInteres());
            new SaldoIBSDAO().updateSaldo(saldoVO);
        } catch (Exception e) {
            myLogger.error("ajustaDevengamiento", e);
        }
    }

    public void ajustaDevengamientoInterciclo(CreditoCartVO creditoVO, SaldoIBSVO saldoVO, Connection con, Connection conCar) {

        DevengamientoVO devengamientoVO = null;
        DevengamientoVO devengarVO = null;
        GrupoVO grupoVO = new GrupoVO();
        ArrayList<TablaAmortVO> arrTablaVO = new ArrayList<TablaAmortVO>();
        DevengamientoDAO devenDAO = new DevengamientoDAO();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaPago, fechaDev = null;
        int periodoDias = 0, diasDeven = 0;
        double interesDevengado = 0, ivaInteresDevengado = 0;
        try {
            if (creditoVO.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
                periodoDias = 7;
            }
            grupoVO.setIdGrupo(creditoVO.getNumCliente());
            grupoVO.setIdGrupoIBS(creditoVO.getNumCredito());
            devengamientoVO = devenDAO.getSigDevengamiento(creditoVO, conCar);
            arrTablaVO = new TablaAmortDAO().getTablaAmortizacion(grupoVO, conCar);
            diasDeven = devenDAO.getDiasDevengadosPago(devengamientoVO, conCar);
            interesDevengado = devengamientoVO.getInteres() * diasDeven;
            ivaInteresDevengado = devengamientoVO.getIvaInteres() * diasDeven;
            interesDevengado = (((arrTablaVO.get(devengamientoVO.getNumPago() - 1).getInteres() / periodoDias) * diasDeven) - interesDevengado);
            ivaInteresDevengado = (((arrTablaVO.get(devengamientoVO.getNumPago() - 1).getIvaInteres() / periodoDias) * diasDeven) - ivaInteresDevengado);
            devenDAO.deleteDevengamiento(devengamientoVO, conCar);
            for (TablaAmortVO tablaAmortVO : arrTablaVO) {
                if (tablaAmortVO.getNumPago() == devengamientoVO.getNumPago()) {
                    do {
                        devengarVO = new DevengamientoVO(tablaAmortVO.getNumPago(), devengamientoVO.getFechaDevengamiento(), creditoVO.getNumCliente(), creditoVO.getNumCredito(), tablaAmortVO.getInteres() / periodoDias, tablaAmortVO.getIvaInteres() / periodoDias, 0);
                        devengarVO.setInteres(devengarVO.getInteres() + interesDevengado);
                        devengarVO.setIvaInteres(devengarVO.getIvaInteres() + ivaInteresDevengado);
                        System.out.println("Se inserta devengam fecha interciclo: "+devengarVO.getFechaDevengamiento());
                        devenDAO.insertDevengamiento(devengarVO, conCar);
                        interesDevengado = 0;
                        ivaInteresDevengado = 0;
                        devengamientoVO.setFechaDevengamiento(Convertidor.toSqlDate(FechasUtil.getRestarDias(devengarVO.getFechaDevengamiento(), -1)));
                        fechaDev = FechasUtil.truncDate(devengamientoVO.getFechaDevengamiento());
                        fechaPago = FechasUtil.truncDate(tablaAmortVO.getFechaPago());
                        System.out.println("Validacion: "+!fechaDev.after(fechaPago));
                    } while (!fechaDev.after(fechaPago));
                    devengamientoVO = new DevengamientoVO(devengarVO.getNumPago() + 1, Convertidor.toSqlDate(FechasUtil.getRestarDias(devengarVO.getFechaDevengamiento(), -1)), devengarVO.getNumCliente(), devengarVO.getNumCredito(), 0, 0, 0);
                }
            }
            devengarVO = devenDAO.getDevengamientoLiquidar(creditoVO, conCar);
            saldoVO.setInteresPorDevengar(devengarVO.getInteres());
            saldoVO.setIvaInteresPorDevengar(devengarVO.getIvaInteres());
            new SaldoIBSDAO().updateSaldosInterciclo(con, saldoVO);
        } catch (Exception e) {
            myLogger.error("ajustaDevengamientoInterciclo", e);
        }
    }

    public static double devengadoParcial(int semDisp, CreditoCartVO creditoVO, Connection con) throws ClientesException {        
        DevengamientoDAO devenDAO = new DevengamientoDAO();
        double montoDeven = 0;
        boolean inserta = false;
        try {
            DevengamientoVO devenVO[] = devenDAO.getDevengamientoSemana(creditoVO.getNumCliente(), creditoVO.getNumCredito(), semDisp);
            for (int i = 0; i < devenVO.length; i++) {
                if (devenVO[i].getDevengado() == 0) {
                    inserta = true;
                }
                else 
                    montoDeven += devenVO[i].getInteres();
            }
            if (!inserta) {
                devenVO = devenDAO.getDevengamientoSemana(creditoVO.getNumCliente(), creditoVO.getNumCredito(), semDisp + 1);
                for (int i = 0; i < devenVO.length; i++) {
                    if (devenVO[i].getDevengado() == 1) {
                        inserta = true;
                         montoDeven += devenVO[i].getInteres();
                    }
                }
            }          
        } catch (Exception e) {
            myLogger.error("regitroDevengamientoDiario", e);
        }
        return montoDeven;
    }//insertar aqui el el calculo de la transacion de devengameinto

}

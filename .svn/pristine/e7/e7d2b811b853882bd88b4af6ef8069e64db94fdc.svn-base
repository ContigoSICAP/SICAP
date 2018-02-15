package com.sicap.clientes.helpers.cartera;

import java.util.Calendar;
import java.util.Vector;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.DescongelaPagoGarantiaIndVO;
import com.sicap.clientes.vo.cartera.EventosVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.CondonacionesVO;
import java.sql.Connection;
import java.sql.Timestamp;

public class EventoHelper {

    public static boolean registraDesembolso(SolicitudVO solicitud, ClienteVO cliente, HttpServletRequest request, Vector notificaciones) throws Exception {

        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, solicitud.idSolicitud);
        boolean result = false;
        HttpSession session = request.getSession();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
//		loadSources();

        try {
            objlCredit = getEventoDes(cliente, solicitud, request.getRemoteUser());
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el EVENTO-- " + e.getMessage()));
            e.printStackTrace();
        }

        return result;
    }

    public static boolean registraDesembolso(CicloGrupalVO ciclo, GrupoVO grupo, HttpServletRequest request, Vector notificaciones) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
//		loadSources();

        try {
            objlCredit = getEventoDes(grupo, ciclo, request.getRemoteUser());
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el EVENTO-- " + e.getMessage()));
            e.printStackTrace();
        }

        return result;
    }
    
    public static boolean registraDesembolsoInteres(CicloGrupalVO ciclo, GrupoVO grupo, HttpServletRequest request, Vector notificaciones) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
//		loadSources();

        try {
            objlCredit = getEventoDesInt(grupo, ciclo, request.getRemoteUser());
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el EVENTO-- " + e.getMessage()));
            e.printStackTrace();
        }

        return result;
    }

    public void registraPago(CreditoCartVO credito, TablaAmortVO tablaAmort, double pago, Date fechaPago, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoPago(credito, tablaAmort, pago, fechaPago, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

// Se utiliza para el registro de pago
    public void registraPago(CreditoCartVO credito, PagoVO pago) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoPago(credito, pago);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void registraPago(CreditoCartVO credito, PagoVO pago, Connection con) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoPago(credito, pago);
            eventoDAO.addEvento(objlCredit,con);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void registraDevolucionOrden(CreditoCartVO credito, OrdenDePagoVO orden, PagoVO pago, SaldoIBSVO saldo) throws Exception {
        
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO  = new EventoDAO();
        try{
            objlCredit = getEventoDevolucionOrden(credito, orden, pago, saldo);
            eventoDAO.addEvento(objlCredit);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void registraDevolucionSaldoFavor(CreditoCartVO credito, OrdenDePagoVO orden, Date fecha) throws Exception {
        
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO  = new EventoDAO();
        try{
            objlCredit = getEventoDevolucionSaldoFavor(credito, orden, fecha);
            eventoDAO.addEvento(objlCredit);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static EventosVO getEventoDes(ClienteVO cliente, SolicitudVO solicitud, String user) {
        Calendar cal = Calendar.getInstance();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(cliente.idSucursal);
            objlCredit.numCredito = (solicitud.idCreditoIBS);
            objlCredit.numCliente = ((cliente.idCliente));
            objlCredit.tipoEvento = ClientesConstants.DESEMBOLSO;
            objlCredit.numDividendo = 0;
            /*objlCredit.fechaIni = (solicitud.fechaDesembolso);
            objlCredit.fechaFin = (solicitud.fechaDesembolso);*/
            fechaTime.setDate(solicitud.fechaDesembolso.getDate());
            fechaTime.setMonth(solicitud.fechaDesembolso.getMonth());
            fechaTime.setYear(solicitud.fechaDesembolso.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
//			objlCredit.monto = ( (solicitud.decisionComite.montoAutorizado+ solicitud.seguro.primaTotal) );
// Se quita el seguro de vida hasta que se active.
            objlCredit.monto = ((solicitud.decisionComite.montoAutorizado + 0));
            objlCredit.status = 1;
            objlCredit.usuario = user;
            // Se quita el seguro de vida hasta que se active.
//			objlCredit.saldo = ( (solicitud.decisionComite.montoAutorizado+ solicitud.seguro.primaTotal) );
            objlCredit.saldo = ((solicitud.decisionComite.montoAutorizado + 0));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    private static EventosVO getEventoDes(GrupoVO grupo, CicloGrupalVO ciclo, String user) {
        Calendar cal = Calendar.getInstance();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            objlCredit.numCredito = (ciclo.idCreditoIBS);
            objlCredit.numCliente = ((ciclo.idGrupo));
            objlCredit.tipoEvento = ClientesConstants.DESEMBOLSO;
            objlCredit.numDividendo = 0;
            /*objlCredit.fechaIni = (ciclo.tablaAmortizacion[0].fechaPago);
            objlCredit.fechaFin = (ciclo.tablaAmortizacion[0].fechaPago);*/
            fechaTime.setDate(ciclo.tablaAmortizacion[0].fechaPago.getDate());
            fechaTime.setMonth(ciclo.tablaAmortizacion[0].fechaPago.getMonth());
            fechaTime.setYear(ciclo.tablaAmortizacion[0].fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = (ciclo.montoConComision);
            objlCredit.status = 1;
            objlCredit.usuario = user;
            objlCredit.saldo = (ciclo.montoConComision);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    private static EventosVO getEventoDesInt(GrupoVO grupo, CicloGrupalVO ciclo, String user) {
        Calendar cal = Calendar.getInstance();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        double pagoInteresIva = 0;
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        try {
            for (int i = 0; i < ciclo.tablaAmortizacion.length; i++) {
                pagoInteresIva += ciclo.tablaAmortizacion[i].interes + ciclo.tablaAmortizacion[i].ivaInteres;
            }
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            objlCredit.numCredito = (ciclo.idCreditoIBS);
            objlCredit.numCliente = ((ciclo.idGrupo));
            objlCredit.tipoEvento = ClientesConstants.INTERESES;
            objlCredit.numDividendo = 0;
            /*objlCredit.fechaIni = (ciclo.tablaAmortizacion[0].fechaPago);
            objlCredit.fechaFin = (ciclo.tablaAmortizacion[0].fechaPago);*/
            fechaTime.setDate(ciclo.tablaAmortizacion[0].fechaPago.getDate());
            fechaTime.setMonth(ciclo.tablaAmortizacion[0].fechaPago.getMonth());
            fechaTime.setYear(ciclo.tablaAmortizacion[0].fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = (pagoInteresIva);
            objlCredit.status = 1;
            objlCredit.usuario = user;
            objlCredit.saldo = (ciclo.montoConComision+pagoInteresIva);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    // Se utiliza para la aplicacion de pago JBL- JUN/10		
    private static EventosVO getEventoPago(CreditoCartVO credito, TablaAmortVO tablaAmort, double pago, Date fechaPago, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.PAGO;
            objlCredit.numDividendo = tablaAmort.numPago;
            /*objlCredit.fechaIni = (tablaAmort.fechaPago);
            objlCredit.fechaFin = (Convertidor.toSqlDate(fechaPago));*/
            fechaTime.setDate(tablaAmort.fechaPago.getDate());
            fechaTime.setMonth(tablaAmort.fechaPago.getMonth());
            fechaTime.setYear(tablaAmort.fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            fechaTime.setDate(fechaPago.getDate());
            fechaTime.setMonth(fechaPago.getMonth());
            fechaTime.setYear(fechaPago.getYear());
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = pago;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

// Se utiliza para el registro de pago JBL- JUN/10	
    private static EventosVO getEventoPago(CreditoCartVO credito, PagoVO pago) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.REGISTRO_PAGO;
            objlCredit.numDividendo = 0;
            /*objlCredit.fechaIni = (pago.fechaPago);
            objlCredit.fechaFin = (pago.fechaPago);*/
            fechaTime.setDate(pago.fechaPago.getDate());
            fechaTime.setMonth(pago.fechaPago.getMonth());
            fechaTime.setYear(pago.fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = pago.monto;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = 0.00;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    private static EventosVO getEventoDevolucionOrden(CreditoCartVO credito, OrdenDePagoVO orden, PagoVO pago, SaldoIBSVO saldo) {
        
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        try{
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()) );
            objlCredit.tipoEvento = ClientesConstants.DEVOLUCION_ORDEN_PAGO;
            objlCredit.numDividendo = orden.getIdCliente();
            /*objlCredit.fechaIni = ( pago.fechaPago) ;
            objlCredit.fechaFin = ( pago.fechaPago);*/
            fechaTime.setDate(pago.fechaPago.getDate());
            fechaTime.setMonth(pago.fechaPago.getMonth());
            fechaTime.setYear(pago.fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = orden.getMonto();
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo   = saldo.getSaldoConInteresAlFinal();
        }catch(Exception e){
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    private static EventosVO getEventoDevolucionSaldoFavor(CreditoCartVO credito, OrdenDePagoVO orden, Date fecha) {
        
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        try{
            //SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()) );
            objlCredit.tipoEvento = ClientesConstants.DEVOLUCION_SALDO_FAVOR;
            objlCredit.numDividendo = orden.getIdCliente();
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fecha);
            objlCredit.fechaFin = Convertidor.toSqlDate(fecha);*/
            fechaTime.setDate(fecha.getDate());
            fechaTime.setMonth(fecha.getMonth());
            fechaTime.setYear(fecha.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = orden.getMonto();
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo   = 0.00;
        }catch(Exception e){
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraProvision(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoProv(credito, tablaAmort, num_dias, fechaIni, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoProv(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
// Calcula periodicidad, se toman en cuenta meses de 30 dias ya que el algorimo es 365/360 dias (aqui se calcula el denominador)
        int dias_periodo = 0;
        if (credito.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
            dias_periodo = 7;
        } else if (credito.getPeriodicidad() == ClientesConstants.PAGO_QUINCENAL) {
            dias_periodo = 15;
        } else {
            dias_periodo = 30;
        }

// Calcula el monto del interes
        double monto_provision = (tablaAmort.interes + tablaAmort.ivaInteres) / dias_periodo * num_dias;

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.PROVISION;
            objlCredit.numDividendo = tablaAmort.numPago;
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fechaIni);;
            objlCredit.fechaFin = (tablaAmort.fechaPago);*/
            fechaTime.setDate(fechaIni.getDate());
            fechaTime.setMonth(fechaIni.getMonth());
            fechaTime.setYear(fechaIni.getYear());
            objlCredit.fechaIni = fechaTime;
            fechaTime.setDate(tablaAmort.fechaPago.getDate());
            fechaTime.setMonth(tablaAmort.fechaPago.getMonth());
            fechaTime.setYear(tablaAmort.fechaPago.getYear());
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = num_dias;
            objlCredit.monto = monto_provision;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraMora(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_mora, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoMora(credito, tablaAmort, num_dias, fechaIni, fechaFin, monto_mora, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoMora(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_mora, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
// Calcula periodicidad, se toman en cuenta meses de 30 dias ya que el algorimo es 365/360 dias (aqui se calcula el denominador)
        int dias_periodo = 0;
        if (credito.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
            dias_periodo = 7;
        } else if (credito.getPeriodicidad() == ClientesConstants.PAGO_QUINCENAL) {
            dias_periodo = 15;
        } else {
            dias_periodo = 30;
        }

// Calcula el monto del interes
        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.MORATORIO;
            objlCredit.numDividendo = tablaAmort.numPago;
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fechaFin);
            objlCredit.fechaFin = Convertidor.toSqlDate(fechaFin);*/
            fechaTime.setDate(fechaIni.getDate());
            fechaTime.setMonth(fechaIni.getMonth());
            fechaTime.setYear(fechaIni.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = num_dias;
            objlCredit.monto = monto_mora;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraCambioEstadoVen(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_mora, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoCambioEstadoVen(credito, tablaAmort, num_dias, fechaIni, fechaFin, monto_mora, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoCambioEstadoVen(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_mora, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
// Calcula periodicidad, se toman en cuenta meses de 30 dias ya que el algorimo es 365/360 dias (aqui se calcula el denominador)
        int dias_periodo = 0;
        if (credito.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
            dias_periodo = 7;
        } else if (credito.getPeriodicidad() == ClientesConstants.PAGO_QUINCENAL) {
            dias_periodo = 15;
        } else {
            dias_periodo = 30;
        }

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.C_ESTADO_VENCIDO;
            objlCredit.numDividendo = tablaAmort.numPago;
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fechaFin);;
            objlCredit.fechaFin = Convertidor.toSqlDate(fechaFin);*/
            fechaTime.setDate(fechaFin.getDate());
            fechaTime.setMonth(fechaFin.getMonth());
            fechaTime.setYear(fechaFin.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = num_dias;
            objlCredit.monto = monto_mora;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraMulta(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_mora, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoMulta(credito, tablaAmort, num_dias, fechaIni, fechaFin, monto_mora, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoMulta(CreditoCartVO credito, TablaAmortVO tablaAmort, int num_dias, Date fechaIni, Date fechaFin, double monto_multa, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
// Calcula periodicidad, se toman en cuenta meses de 30 dias ya que el algorimo es 365/360 dias (aqui se calcula el denominador)
        int dias_periodo = 0;
        if (credito.getPeriodicidad() == ClientesConstants.PAGO_SEMANAL) {
            dias_periodo = 7;
        } else if (credito.getPeriodicidad() == ClientesConstants.PAGO_QUINCENAL) {
            dias_periodo = 15;
        } else {
            dias_periodo = 30;
        }

// Calcula el monto del interes
        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.MULTA;
            objlCredit.numDividendo = tablaAmort.numPago;
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fechaFin);;
            objlCredit.fechaFin = Convertidor.toSqlDate(fechaFin);*/
            fechaTime.setDate(fechaFin.getDate());
            fechaTime.setMonth(fechaFin.getMonth());
            fechaTime.setYear(fechaFin.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = num_dias;
            objlCredit.monto = monto_multa;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraCondonacion(CondonacionesVO condonacion, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoCondonacion(condonacion, saldo);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoCondonacion(CondonacionesVO condonacion, double saldo) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            objlCredit.numCredito = (condonacion.numCredito);
            objlCredit.numCliente = (condonacion.numCliente);
            objlCredit.tipoEvento = ClientesConstants.CONDONACION;
            objlCredit.numDividendo = condonacion.numDividendo;
            /*objlCredit.fechaIni = condonacion.fecha;
            objlCredit.fechaFin = condonacion.fecha;*/
            fechaTime.setDate(condonacion.fecha.getDate());
            fechaTime.setMonth(condonacion.fecha.getMonth());
            fechaTime.setYear(condonacion.fecha.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = condonacion.monto;
            objlCredit.status = 1;
            objlCredit.usuario = condonacion.usuario;
            objlCredit.saldo = saldo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    public void registraDevolucion(DescongelaPagoGarantiaIndVO pagoGarantia) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoDevolucion(pagoGarantia);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static EventosVO getEventoDevolucion(DescongelaPagoGarantiaIndVO pagoGarantia) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            objlCredit.numCredito = (pagoGarantia.getNumCredito());
            objlCredit.numCliente = (pagoGarantia.getNumGrupo());
            objlCredit.tipoEvento = ClientesConstants.DEVOLUCION;
            objlCredit.numDividendo = (pagoGarantia.getNumCliente());
            /*objlCredit.fechaIni = pagoGarantia.getFechaDevolucion();
            objlCredit.fechaFin = pagoGarantia.getFechaDevolucion();*/
            fechaTime.setDate(pagoGarantia.getFechaDevolucion().getDate());
            fechaTime.setMonth(pagoGarantia.getFechaDevolucion().getMonth());
            fechaTime.setYear(pagoGarantia.getFechaDevolucion().getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = pagoGarantia.getMontoPago();
            objlCredit.status = 1;
            objlCredit.usuario = pagoGarantia.getUsuario();
            objlCredit.saldo = 0.00;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    public void registraCastigo(SaldoIBSVO saldo, HttpServletRequest request) throws Exception {
        
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO  = new EventoDAO();
        try{
            objlCredit = getEventoCastigo(saldo, request);
            eventoDAO.addEvento(objlCredit);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static EventosVO getEventoCastigo(SaldoIBSVO saldo, HttpServletRequest request) {
        
        EventosVO objlCredit = new EventosVO();
        Calendar fechaActual = Calendar.getInstance();
        fechaActual.set(Calendar.HOUR_OF_DAY, 0);
        fechaActual.set(Calendar.MINUTE, 0);
        fechaActual.set(Calendar.SECOND, 0);
        fechaActual.set(Calendar.MILLISECOND, 0);
        try{
            objlCredit.numCredito = (saldo.getCredito());
            objlCredit.numCliente = (saldo.getIdClienteSICAP());
            objlCredit.tipoEvento = ClientesConstants.CASTIGO;
            objlCredit.numDividendo = (saldo.getIdClienteSICAP());
            /*objlCredit.fechaIni = Convertidor.toSqlDate(fechaActual.getTime());
            objlCredit.fechaFin = Convertidor.toSqlDate(fechaActual.getTime());*/
            objlCredit.fechaIni = new Timestamp(fechaActual.getTimeInMillis());
            objlCredit.fechaFin = new Timestamp(fechaActual.getTimeInMillis());
            objlCredit.numDias = saldo.getDiasMora();
            objlCredit.monto = (saldo.getSaldoCapital());
            objlCredit.status = 1;
            objlCredit.usuario = request.getRemoteUser();
            //objlCredit.saldo =  saldo.getSaldoTotalAlDia() ;
            objlCredit.saldo =  saldo.getSaldoConInteresAlFinal();
        }catch(Exception e){
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    public static void registraCancelacionPago(CreditoCartVO credito, double monto, double saldoFavor){
        
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        Date fecha = new Date();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(credito.getNumSucursal());
            objlCredit.numCredito = (credito.getNumCredito());
            objlCredit.numCliente = ((credito.getNumCliente()));
            objlCredit.tipoEvento = ClientesConstants.REGISTRO_CAN_PAGO;
            objlCredit.numDividendo = 0;
            /*objlCredit.fechaIni = (Convertidor.toSqlDate(fecha));
            objlCredit.fechaFin = (Convertidor.toSqlDate(fecha));*/
            fechaTime.setDate(fecha.getDate());
            fechaTime.setMonth(fecha.getMonth());
            fechaTime.setYear(fecha.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = 0;
            objlCredit.monto = monto;
            //objlCredit.monto = monto-saldoFavor;
            objlCredit.status = 1;
            objlCredit.usuario = "SISTEMA";
            objlCredit.saldo = 0.00;
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            Logger.debug("Problema dentro registraCancelacionPago");
            e.printStackTrace();
        }
    }
    
    public void registraCancelaPago(CreditoCartVO credito, TablaAmortVO tablaAmort, double pago, Date fechaPago, double saldo) throws Exception {

        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();

        try {
            objlCredit = getEventoPago(credito, tablaAmort, pago, fechaPago, saldo);
            objlCredit.tipoEvento = ClientesConstants.PAGO_CANCELADO;
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static boolean registraDesembolsoInterciclo(CicloGrupalVO ciclo, GrupoVO grupo, HttpServletRequest request, double montoIncremento, double montoIntereses) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        try {
            objlCredit = getEventoDesInterciclo(grupo, ciclo, request.getRemoteUser(), montoIncremento, montoIntereses);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    private static EventosVO getEventoDesInterciclo(GrupoVO grupo, CicloGrupalVO ciclo, String user, double montoIncremento, double montoIntereses) {
        Calendar cal = Calendar.getInstance();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            objlCredit.numCredito = (ciclo.idCreditoIBS);
            objlCredit.numCliente = ((ciclo.idGrupo));
            objlCredit.tipoEvento = ClientesConstants.DESEMBOLSO;
            objlCredit.numDividendo = 0;
            fechaTime.setDate(ciclo.tablaAmortizacion[4].fechaPago.getDate());
            fechaTime.setMonth(ciclo.tablaAmortizacion[4].fechaPago.getMonth());
            fechaTime.setYear(ciclo.tablaAmortizacion[4].fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = ciclo.saldo.getDiasTranscurridos();
            objlCredit.monto = (montoIncremento);
            objlCredit.status = 1;
            objlCredit.usuario = user;
            objlCredit.saldo = (ciclo.saldo.getSaldoConInteresAlFinal() - montoIntereses);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    public static boolean registraDesembolsoInteresInterciclo(CicloGrupalVO ciclo, GrupoVO grupo, HttpServletRequest request, double montoIntereses) throws Exception {

        boolean result = false;
        HttpSession session = request.getSession();
        EventosVO objlCredit = new EventosVO();
        EventoDAO eventoDAO = new EventoDAO();
        try {
            objlCredit = getEventoDesIntInterciclo(grupo, ciclo, request.getRemoteUser(), montoIntereses);
            eventoDAO.addEvento(objlCredit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    private static EventosVO getEventoDesIntInterciclo(GrupoVO grupo, CicloGrupalVO ciclo, String user, double montoIntereses) {
        EventosVO objlCredit = new EventosVO();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            objlCredit.numCredito = (ciclo.idCreditoIBS);
            objlCredit.numCliente = ((ciclo.idGrupo));
            objlCredit.tipoEvento = ClientesConstants.INTERESES;
            objlCredit.numDividendo = 0;
            fechaTime.setDate(ciclo.tablaAmortizacion[4].fechaPago.getDate());
            fechaTime.setMonth(ciclo.tablaAmortizacion[4].fechaPago.getMonth());
            fechaTime.setYear(ciclo.tablaAmortizacion[4].fechaPago.getYear());
            objlCredit.fechaIni = fechaTime;
            objlCredit.fechaFin = fechaTime;
            objlCredit.numDias = ciclo.saldo.getDiasTranscurridos();
            objlCredit.monto = (montoIntereses);
            objlCredit.status = 1;
            objlCredit.usuario = user;
            objlCredit.saldo = (ciclo.saldo.getSaldoConInteresAlFinal());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

}

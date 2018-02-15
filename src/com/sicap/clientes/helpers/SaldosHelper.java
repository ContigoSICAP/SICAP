package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.AuditoresDAO;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import java.util.ArrayList;

public class SaldosHelper {

    public static boolean insertSaldo(SolicitudVO solicitud, ClienteVO cliente, HttpServletRequest request, Vector notificaciones) throws Exception {

        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, solicitud.idSolicitud);
        boolean result = false;
        HttpSession session = request.getSession();
        SaldoIBSVO saldo = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
//		loadSources();


        try {
            saldo = setSaldoInsert(cliente, solicitud, request.getRemoteUser());
            saldoDAO.addSaldoIBS(saldo);

        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
            e.printStackTrace();
        }

        return result;
    }

    public static SaldoIBSVO insertSaldo(CicloGrupalVO ciclo, GrupoVO grupo, HttpServletRequest request, Vector notificaciones) throws Exception {

        HttpSession session = request.getSession();
        SaldoIBSVO saldo = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
//		loadSources();


        try {
            saldo = setSaldoInsert(grupo, ciclo, request.getRemoteUser());
            saldoDAO.addSaldoIBS(saldo);

        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
            e.printStackTrace();
        }

        return saldo;
    }

    private static SaldoIBSVO setSaldoInsert(ClienteVO cliente, SolicitudVO solicitud, String user) {
        Calendar cal = Calendar.getInstance();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoT24DAO = new SaldoIBSDAO();
        Double tasaIva = 0.000;
        TasaInteresVO tasaInteres = null;
        ComisionVO comision = null;
        boolean esReestructura = solicitud.reestructura == 1 ? true : false;

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(cliente.idSucursal);
            if(solicitud.tipoOperacion != ClientesConstants.MICROCREDITO){
                if (Convertidor.esFronterizo(cliente.idSucursal)) {
                    tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
                } else {
                    tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
                }
            }

            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
//			double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
            double primaConComision = 0;
            TablaAmortizacionVO[] tablaVO = solicitud.amortizacion;
            double pagos[] = null;
            double pagoInteresIva = 0;
            pagos = new double[tablaVO.length];
            for (int i = 0; i < tablaVO.length; i++) {
                pagos[i] = tablaVO[i].interes + tablaVO[i].ivaInteres;
                pagoInteresIva = pagoInteresIva + pagos[i];
            }
            saldoVO.setIdClienteSICAP((cliente.idCliente));
            saldoVO.setCredito((solicitud.idCreditoIBS));
            saldoVO.setIdSolicitudSICAP((solicitud.idSolicitud));
            saldoVO.setReferencia(solicitud.referencia);
            saldoVO.setIdSucursal((cliente.idSucursal));
            saldoVO.setNombreSucursal(sucursal.nombre);
            saldoVO.setFechaDesembolso(solicitud.fechaDesembolso);
            saldoVO.setFechaVencimiento(solicitud.amortizacion[solicitud.amortizacion.length - 1].fechaPago);
            saldoVO.setNumeroCuotas((solicitud.amortizacion.length - 1));
            saldoVO.setPeriodicidad((solicitud.decisionComite.frecuenciaPago));
            saldoVO.setMontoCredito((solicitud.decisionComite.montoAutorizado + primaConComision));
            saldoVO.setMontoDesembolsado((ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones)));
            saldoVO.setMontoSeguro((0));
            saldoVO.setComision((solicitud.amortizacion[0].comisionInicial));
            saldoVO.setIvaComision((solicitud.amortizacion[0].ivaComision));
            saldoVO.setTasaInteresSinIVA(solicitud.tasaCalculada);
            saldoVO.setTasaMoraSinIVA(solicitud.tasaCalculada * ClientesConstants.FACTOR_MORA_INDIV);
            saldoVO.setTasaIVA(tasaIva * 100);
            saldoVO.setIdProducto((solicitud.tipoOperacion));
            //********************Modificar para parametrizar con cat�logo de fondeadores
            saldoVO.setFondeador(1);
            saldoVO.setEstatus(1);
            saldoVO.setNombreCliente(cliente.nombreCompleto);
            saldoVO.setRfc(cliente.rfc);
            saldoVO.setNombreSucursal(sucursal.nombre);
            saldoVO.setFechaEnvio(solicitud.fechaDesembolso);
            saldoVO.setFechaGeneracion(solicitud.fechaDesembolso);
            saldoVO.setHoraGeneracion(1200);
            saldoVO.setNumeroCuotasTranscurridas(0);
            saldoVO.setPlazo(solicitud.decisionComite.plazoAutorizado);
            saldoVO.setSaldoTotalAlDia(solicitud.decisionComite.montoAutorizado + solicitud.amortizacion[1].interes + solicitud.amortizacion[1].ivaInteres);
            saldoVO.setSaldoCapital(solicitud.decisionComite.montoAutorizado);
            saldoVO.setSaldoInteres(solicitud.amortizacion[1].interes);
            saldoVO.setSaldoInteresVigente(solicitud.amortizacion[1].interes);
            saldoVO.setSaldoInteresVencido(0);
            saldoVO.setSaldoInteresVencido90dias(0);
            saldoVO.setSaldoInteresCtasOrden(0);
            saldoVO.setSaldoIvaInteres(solicitud.amortizacion[1].ivaInteres);
            saldoVO.setSaldoMora(0);
            saldoVO.setSaldoIVAMora(0);
            saldoVO.setSaldoMulta(0);
            saldoVO.setSaldoIVAMulta(0);
            saldoVO.setCapitalPagado(0);
            saldoVO.setInteresNormalPagado(0);
            saldoVO.setIvaInteresNormalPagado(0);
            saldoVO.setMoratorioPagado(0);
            saldoVO.setIvaMoraPagado(0);
            saldoVO.setMultaPagada(0);
            saldoVO.setIvaMultaPagado(0);
            saldoVO.setComision(solicitud.amortizacion[0].comisionInicial);
            saldoVO.setIvaComision(solicitud.amortizacion[0].ivaComision);
            saldoVO.setFechaSigAmortizacion(solicitud.amortizacion[1].fechaPago);
            saldoVO.setCapitalSigAmortizacion(solicitud.amortizacion[1].abonoCapital);
            saldoVO.setInteresSigAmortizacion(solicitud.amortizacion[1].interes);
            saldoVO.setIvaSigAmortizacion(solicitud.amortizacion[1].ivaInteres);
            saldoVO.setNombreFondeador("");
            saldoVO.setSaldoConInteresAlFinal(solicitud.decisionComite.montoAutorizado + pagoInteresIva);
            saldoVO.setCapitalVencido(0);
            saldoVO.setInteresVencido(0);
            saldoVO.setIvaInteresVencido(0);
            saldoVO.setTotalVencido(0);
            saldoVO.setFechaIncumplimiento(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setFechaAcarteraVencida(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setDiasMora(0);
            saldoVO.setDiasTranscurridos(1);
            saldoVO.setCuotasVencidas(0);
            saldoVO.setNumeroPagosRealizados(0);
            saldoVO.setMontoTotalPagado(0);
            saldoVO.setFechaUltimoPago(null);
            saldoVO.setBanderaReestructura("N");
            saldoVO.setCreditoReestructurado(0);
            saldoVO.setDiasMoraReestructura(0);
            saldoVO.setTasaPreferencialIVA("");
            saldoVO.setMontoSeguro(0);
            // En la cuenta bucket guardamos el numero de representante JBL JUL/10		
            saldoVO.setCuentaBucket(solicitud.numrepresentante);
            saldoVO.setSaldoBucket(0);
            saldoVO.setSaldoBonificacionDeIVA(0);
            saldoVO.setBonificacionPagada(0);
            saldoVO.setOrigen(1);
            saldoVO.setCtaContable(solicitud.idEjecutivo);
            saldoVO.setMontoConIntereses(ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones) + pagoInteresIva);
            saldoVO.setTasaElegida(Double.parseDouble(catTasas.get(solicitud.decisionComite.tasa).descripcion.replace("%", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldoVO;
    }

    private static SaldoIBSVO setSaldoInsert(GrupoVO grupo, CicloGrupalVO ciclo, String user) {
        Calendar cal = Calendar.getInstance();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoT24DAO = new SaldoIBSDAO();
        TablaAmortDAO tablaAmortDAO = new TablaAmortDAO();
        AuditoresDAO auditorDAO = new AuditoresDAO();
        Double tasaIva = 0.000;
        TasaInteresVO tasaInteres = null;
        ComisionVO comision = null;
        boolean esReestructura = false;

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            if (Convertidor.esFronterizo(grupo.sucursal)) {
                tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
            } else {
                tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
            }

            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
//			double primaConComision = SeguroHelper.obtenPrimaConComision(ciclo, catComisiones);
/*
             * Se calcula el el total a pagar de interes e iva por todo el proyecto
             */
            TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
            double pagos[] = null;
            double pagoInteresIva = 0;
            pagos = new double[tablaVO.length];
            for (int i = 0; i < tablaVO.length; i++) {
                pagos[i] = tablaVO[i].interes + tablaVO[i].ivaInteres;
                pagoInteresIva = pagoInteresIva + pagos[i];
            }
            /*
             * Se inicia la insercion en la tabla de saldos
             */
            saldoVO.setIdClienteSICAP((grupo.idGrupo));
            saldoVO.setCredito((ciclo.idCreditoIBS));
            saldoVO.setIdSolicitudSICAP((ciclo.idCiclo));
            saldoVO.setReferencia(ciclo.referencia);
            saldoVO.setIdSucursal((grupo.sucursal));
            saldoVO.setNombreSucursal(sucursal.nombre);
            saldoVO.setFechaDesembolso(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setFechaVencimiento(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago);
            saldoVO.setNumeroCuotas((ciclo.tablaAmortizacion.length - 1));
            saldoVO.setPeriodicidad((ClientesConstants.PAGO_SEMANAL));
            saldoVO.setMontoCredito(ciclo.montoConComision);
            saldoVO.setMontoDesembolsado((ClientesUtil.calculaMontoSinComision(ciclo.montoConComision, ciclo.comision, catComisiones)));
            //Modificar en cuanto se reactiven los seguros
            saldoVO.setMontoSeguro((0));
            saldoVO.setComision((ciclo.tablaAmortizacion[0].comisionInicial));
            saldoVO.setIvaComision((ciclo.tablaAmortizacion[0].ivaComision));
            saldoVO.setTasaInteresSinIVA((ciclo.tasaCalculada / (1 + tasaIva)));
            saldoVO.setTasaMoraSinIVA((ciclo.tasaCalculada * ClientesConstants.FACTOR_MORA_GRUPO / (1 + tasaIva)));
            saldoVO.setTasaIVA(tasaIva * 100);
            saldoVO.setIdProducto((ClientesConstants.GRUPAL));
            //********************Modificar para parametrizar con cat�logo de fondeadores
            saldoVO.setFondeador(ciclo.fondeador);
            saldoVO.setEstatus(1);
            saldoVO.setNombreCliente(grupo.nombre);
            saldoVO.setRfc(grupo.rfc);
            saldoVO.setFechaEnvio(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setFechaGeneracion(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setHoraGeneracion(1200);
            saldoVO.setNumeroCuotasTranscurridas(0);
            saldoVO.setPlazo(ciclo.plazo);
            saldoVO.setSaldoTotalAlDia(ciclo.montoConComision + ciclo.tablaAmortizacion[1].interes + ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setSaldoCapital(ciclo.montoConComision);
            saldoVO.setSaldoInteres(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setSaldoInteresVigente(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setSaldoInteresVencido(0);
            saldoVO.setSaldoInteresVencido90dias(0);
            saldoVO.setSaldoInteresCtasOrden(0);
            saldoVO.setSaldoIvaInteres(ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setSaldoMora(0);
            saldoVO.setSaldoIVAMora(0);
            saldoVO.setSaldoMulta(0);
            saldoVO.setSaldoIVAMulta(0);
            saldoVO.setCapitalPagado(0);
            saldoVO.setInteresNormalPagado(0);
            saldoVO.setIvaInteresNormalPagado(0);
            saldoVO.setMoratorioPagado(0);
            saldoVO.setIvaMoraPagado(0);
            saldoVO.setMultaPagada(0);
            saldoVO.setIvaMultaPagado(0);
            saldoVO.setComision(ciclo.tablaAmortizacion[0].comisionInicial);
            saldoVO.setIvaComision(ciclo.tablaAmortizacion[0].ivaComision);
            saldoVO.setMontoDesembolsado(ciclo.monto);
            saldoVO.setFechaSigAmortizacion(ciclo.tablaAmortizacion[1].fechaPago);
            saldoVO.setCapitalSigAmortizacion(ciclo.tablaAmortizacion[1].abonoCapital);
            saldoVO.setInteresSigAmortizacion(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setIvaSigAmortizacion(ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setNombreFondeador("");
            saldoVO.setSaldoConInteresAlFinal(ciclo.montoConComision + pagoInteresIva);
            saldoVO.setCapitalVencido(0);
            saldoVO.setInteresVencido(0);
            saldoVO.setIvaInteresVencido(0);
            saldoVO.setTotalVencido(0);
            saldoVO.setFechaIncumplimiento(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setFechaAcarteraVencida(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setDiasMora(0);
            saldoVO.setDiasTranscurridos(1);
            saldoVO.setCuotasVencidas(0);
            saldoVO.setNumeroPagosRealizados(0);
            saldoVO.setMontoTotalPagado(0);
            saldoVO.setFechaUltimoPago(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setBanderaReestructura("N");
            saldoVO.setCreditoReestructurado(0);
            saldoVO.setDiasMoraReestructura(0);
            saldoVO.setTasaPreferencialIVA("");
            saldoVO.setMontoSeguro(0);
            // En la cuenta bucket guardamos el numero de representante JBL JUL/10		
            saldoVO.setCuentaBucket(0);
            saldoVO.setSaldoBucket(0);
            saldoVO.setSaldoBonificacionDeIVA(0);
            saldoVO.setBonificacionPagada(0);
            saldoVO.setOrigen(1);
            saldoVO.setCtaContable(ciclo.asesor);
            saldoVO.setMontoConIntereses(java.lang.Math.ceil(ciclo.montoConComision + pagoInteresIva));
            saldoVO.setTasaElegida(Double.parseDouble(catTasas.get(ciclo.tasa).descripcion.replace("%", "")));
            saldoVO.setInteresPorDevengar(tablaAmortDAO.getInteresPorDevengar(saldoVO.getIdClienteSICAP(), saldoVO.getCredito()));
            saldoVO.setIvaInteresPorDevengar(saldoVO.getInteresPorDevengar()*ClientesConstants.TASA_IVA_NO_FRONTERIZO);
            saldoVO.setNumAuditor(auditorDAO.buscaSucursal(saldoVO.getIdSucursal()));
            saldoVO.setNumIntegrantesDesembolso(ciclo.integrantes.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldoVO;
    }

    public ArrayList<SaldoIBSVO> getAplicaCarteraCedidaHelper(HttpServletRequest request) {

        ArrayList<SaldoIBSVO> saldos = new ArrayList<SaldoIBSVO>();
        String[] idClientes = null;
        BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "CommandAjusteCreditos");
        try {
            idClientes = request.getParameterValues("aplicaCartera");
            if (idClientes != null) {
                for (int i = 0; i < idClientes.length; i++) {
                    //System.out.println(i+"_"+idClientes[i]+" "+HTMLHelper.getParameterInt(request, "idCliente"+idClientes[i])+" "+HTMLHelper.getParameterInt(request, "idSolicitud"+idClientes[i])+" "+HTMLHelper.getParameterString(request, "nomcliente"+idClientes[i])+" "+HTMLHelper.getParameterInt(request, "idCredito"+idClientes[i]));
                    saldos.add(new SaldoIBSVO(HTMLHelper.getParameterInt(request, "idCredito" + idClientes[i]), HTMLHelper.getParameterInt(request, "idCliente" + idClientes[i]),
                            HTMLHelper.getParameterInt(request, "idSolicitud" + idClientes[i]), HTMLHelper.getParameterString(request, "nomcliente" + idClientes[i]),
                            HTMLHelper.getParameterDouble(request, "montoCredito" + idClientes[i]), HTMLHelper.getParameterDouble(request, "saldoCartera"+ idClientes[i]),
                            HTMLHelper.getParameterInt(request, "diasMora" + idClientes[i]), HTMLHelper.getParameterString(request, "referencia" + idClientes[i]),
                            HTMLHelper.getParameterSqlDate(request, "fechaVencimiento" + idClientes[i])));
                    bitacora.registraEventoString("grupo="+HTMLHelper.getParameterInt(request, "idCliente" + idClientes[i])+", ciclo="+HTMLHelper.getParameterInt(request, "idSolicitud" + idClientes[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saldos;
    }
}
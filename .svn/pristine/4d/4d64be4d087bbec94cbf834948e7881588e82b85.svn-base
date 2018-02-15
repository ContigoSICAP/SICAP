package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Savepoint;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class SaldoIBSDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(SaldoIBSDAO.class);

    public SaldoIBSVO getSaldos(int numCliente, int numSolicitud) throws ClientesException {

        SaldoIBSVO saldosIBS = new SaldoIBSVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_numClienteSICAP = ? AND ib_numSolicitudSICAP = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + numCliente + "," + numSolicitud + " ]");
            rs = ps.executeQuery();

            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();

                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setMontoConIntereses(rs.getDouble("ib_montoconintereses"));
                saldosIBS.setTasaElegida(rs.getDouble("ib_tasaelegida"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setNumIntegrantesDesembolso(rs.getInt("ib_integranteini"));
                saldosIBS.setNumIntegrantesCancelacion(rs.getInt("ib_integrantecan"));
                saldosIBS.setNumIntegrantesAdicionales(rs.getInt("ib_addintegrante"));
                saldosIBS.setFondeoGarantia(rs.getInt("ib_fondeo_garantia"));

            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public SaldoIBSVO getSaldo(int numCliente, int numCredito) throws ClientesException {
        SaldoIBSVO saldosIBS = new SaldoIBSVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_numClienteSICAP = ? AND ib_credito = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();

                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldosIBS.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setInteresDevengados(rs.getDouble("ib_interes_devengado"));
                saldosIBS.setIvaInteresDevengados(rs.getDouble("ib_iva_interes_devengado"));

            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public SaldoIBSVO getSaldo(int numCliente, int numSolicitud, String referencia) throws ClientesException {

        SaldoIBSVO saldosIBS = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM D_SALDOS WHERE IB_NUMCLIENTESICAP = ? AND IB_NUMSOLICITUDSICAP = ? AND IB_REFERENCIA = ? AND IB_ESTATUS != 5";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setString(3, referencia);
            myLogger.debug("Ejecutando *= " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numSolicitud + "," + referencia + "]");
            rs = ps.executeQuery();
            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();
                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldosIBS.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setInteresDevengados(rs.getDouble("ib_interes_devengado"));
                saldosIBS.setIvaInteresDevengados(rs.getDouble("ib_iva_interes_devengado"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return saldosIBS;
    }
    
     public SaldoIBSVO getSaldo(int numCliente, int numSolicitud, String referencia, Connection conn) throws ClientesException {

        SaldoIBSVO saldosIBS = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM D_SALDOS WHERE IB_NUMCLIENTESICAP = ? AND IB_NUMSOLICITUDSICAP = ? AND IB_REFERENCIA = ? AND IB_ESTATUS != 5";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setString(3, referencia);
            myLogger.debug("Ejecutando *= " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numSolicitud + "," + referencia + "]");
            rs = ps.executeQuery();
            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();
                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldosIBS.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setInteresDevengados(rs.getDouble("ib_interes_devengado"));
                saldosIBS.setIvaInteresDevengados(rs.getDouble("ib_iva_interes_devengado"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldo", e);
            throw new ClientesException(e.getMessage());
        } 
        return saldosIBS;
    }
    /**
     * Se agrega credito real as
     * @param numCliente En saldo es el id del grup
     * @param numSolicitud Es el ciclo grupal
     * @param idFondeadorGarant Es el ID de credito real 1, los negativos son para preseleccion
     * @param idCreditoReal
     * @return
     * @throws ClientesException 
     */
    public SaldoIBSVO getSaldoFondeoGarantia(int numCliente, int numSolicitud,int idFondeadorGarant,int idCreditoReal) throws ClientesException {
        SaldoIBSVO saldosIBS = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM D_SALDOS WHERE IB_NUMCLIENTESICAP = ? AND IB_NUMSOLICITUDSICAP = ?  AND (IB_FONDEO_GARANTIA > ? OR IB_FONDEADOR = ?) ";
            
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setInt(3, idFondeadorGarant);
            ps.setInt(4, idCreditoReal);
            
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();

                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldosIBS.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setInteresDevengados(rs.getDouble("ib_interes_devengado"));
                saldosIBS.setIvaInteresDevengados(rs.getDouble("ib_iva_interes_devengado"));
                saldosIBS.setFondeoGarantia(rs.getInt("ib_fondeo_garantia"));

            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }
    
    public boolean isCreditoIBS(String referencia) throws ClientesException {

        boolean isCreditoIBS = false;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_referencia   = ? AND ib_origen = 1";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, referencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                isCreditoIBS = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("isCreditoIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("isCreditoIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return isCreditoIBS;
    }

    public boolean tieneMultas(int idEquipo, int idCredito) throws ClientesException {

        boolean tieneMultas = false;
        float multas = 0;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String query = "SELECT (ib_saldo_multa+ib_saldo_iva_multa+ib_multa_pagada+ib_iva_multa_pagada) AS Multas FROM d_saldos WHERE ib_numclientesicap = ? AND ib_credito = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCredito);
            rs = ps.executeQuery();
            if (rs.next()) {
                multas = rs.getFloat("Multas");
            }
            if (multas > 0) {
                tieneMultas = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("tieneMultas", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tieneMultas", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return tieneMultas;
    }
    
    public SaldoIBSVO validaInterCiclo(int idEquipo) throws ClientesException {

        SaldoIBSVO saldo  = new SaldoIBSVO();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String query = "Select ib_numclientesicap, ib_numSolicitudSicap, ib_fondeador, ib_num_cuotas_trancurridas " +
                            " from d_saldos, d_ciclos_grupales" +
                            " where ci_numgrupo = ib_numclienteSicap" +
                            " and ci_numciclo = ib_numSolicitudSICAP" +
                            " and ib_numclienteSicap = ?" +
                            " and ib_numSolicitudSICAP >=2 " +
                            " and ib_estatus = 1 " +
                            " and ib_plazo = 16";

            ps = conn.prepareStatement(query);
            ps.setInt(1, idEquipo);            
            rs = ps.executeQuery();
            if (rs.next()) {
                saldo.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldo.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSicap")); 
                saldo.setFondeador(rs.getInt("ib_fondeador"));
                saldo.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));            
            }
        } catch (SQLException sqle) {
            myLogger.error("aceptaInterCiclo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("aceptaInterCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                myLogger.error("aceptaInterCiclo", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return saldo;
    }

    public SaldoIBSVO[] getSaldosIBS(Date fechaHist) throws ClientesException {

        SaldoIBSVO saldo = null;
        Connection conn = null;
        ResultSet rs = null;
        List<SaldoIBSVO> lista = new ArrayList<SaldoIBSVO>();
        SaldoIBSVO saldosIBS[] = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_fecha_generacion=? AND ib_estatus IN (1,2,3,4,6,7); ";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, fechaHist);
            rs = ps.executeQuery();

            while (rs.next()) {
                saldo = new SaldoIBSVO();

                saldo.setOrigen(rs.getInt("ib_origen"));
                saldo.setCredito(rs.getInt("ib_credito"));
                saldo.setReferencia(rs.getString("ib_referencia"));
                saldo.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldo.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldo.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldo.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldo.setRfc(rs.getString("ib_rfc"));
                saldo.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldo.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldo.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldo.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldo.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldo.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldo.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldo.setPlazo(rs.getInt("ib_plazo"));
                saldo.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldo.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldo.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldo.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldo.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldo.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldo.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldo.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldo.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldo.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldo.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldo.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldo.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldo.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldo.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldo.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldo.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldo.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldo.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldo.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldo.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldo.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldo.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldo.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldo.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldo.setComision(rs.getDouble("ib_comision"));
                saldo.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldo.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldo.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldo.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldo.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldo.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldo.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldo.setFondeador(rs.getInt("ib_fondeador"));
                saldo.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldo.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldo.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldo.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldo.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldo.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldo.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldo.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldo.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldo.setEstatus(rs.getInt("ib_estatus"));
                saldo.setIdProducto(rs.getInt("ib_producto"));
                saldo.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldo.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldo.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldo.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldo.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldo.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldo.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldo.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldo.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldo.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldo.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldo.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldo.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldo.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldo.setCtaContable(rs.getLong("ib_cta_contable"));
                saldo.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldo.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldo.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));

                lista.add(saldo);

            }

            saldosIBS = new SaldoIBSVO[lista.size()];
            for (int i = 0; i < saldosIBS.length; i++) {
                saldosIBS[i] = (SaldoIBSVO) lista.get(i);
            }

        } catch (SQLException sqle) {
            myLogger.error("getSaldosIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public SaldoIBSVO[] getSaldosBySucursal(int numSucursal) throws ClientesException {

        SaldoIBSVO saldo = null;
        Connection conn = null;
        ResultSet rs = null;
        List<SaldoIBSVO> lista = new ArrayList<SaldoIBSVO>();
        SaldoIBSVO saldosIBS[] = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_numSucursal = ? and ib_estatus in (1,2)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numSucursal);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                saldo = new SaldoIBSVO();
                saldo.setOrigen(rs.getInt("ib_origen"));
                saldo.setCredito(rs.getInt("ib_credito"));
                saldo.setReferencia(rs.getString("ib_referencia"));
                saldo.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldo.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldo.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldo.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldo.setRfc(rs.getString("ib_rfc"));
                saldo.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldo.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldo.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldo.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldo.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldo.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldo.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldo.setPlazo(rs.getInt("ib_plazo"));
                saldo.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldo.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldo.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldo.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldo.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldo.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldo.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldo.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldo.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldo.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldo.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldo.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldo.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldo.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldo.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldo.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldo.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldo.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldo.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldo.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldo.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldo.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldo.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldo.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldo.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldo.setComision(rs.getDouble("ib_comision"));
                saldo.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldo.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldo.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldo.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldo.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldo.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldo.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldo.setFondeador(rs.getInt("ib_fondeador"));
                saldo.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldo.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldo.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldo.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldo.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldo.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldo.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldo.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldo.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldo.setEstatus(rs.getInt("ib_estatus"));
                saldo.setIdProducto(rs.getInt("ib_producto"));
                saldo.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldo.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldo.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldo.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldo.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldo.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldo.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldo.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldo.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldo.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldo.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldo.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldo.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldo.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldo.setCtaContable(rs.getLong("ib_cta_contable"));
                saldo.setNumDespacho(rs.getInt("ib_numdespacho"));
                saldo.setMaxCuotasVencidas(rs.getInt("ib_numdespacho"));
                saldo.setMaxDiasMora(rs.getInt("ib_numdespacho"));
                saldo.setMontoConIntereses(rs.getDouble("ib_saldo_bucket"));
                saldo.setTasaElegida(rs.getDouble("ib_tasaelegida"));
                saldo.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));

                lista.add(saldo);

            }

            saldosIBS = new SaldoIBSVO[lista.size()];
            for (int i = 0; i < saldosIBS.length; i++) {
                saldosIBS[i] = (SaldoIBSVO) lista.get(i);
            }

        } catch (SQLException sqle) {
            myLogger.error("getSaldosBySucursal", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosBySucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public int addSaldosIBS(List lista) throws ClientesException {

        SaldoIBSVO saldosIBS = null;

        Connection conn = null;
        int res = 0;

        String query
                = "INSERT INTO d_saldos "
                + "( ib_credito, ib_referencia, ib_numClienteSICAP, ib_numSolicitudSICAP, ib_numClienteIBS, ib_nombreCliente, ib_rfc, ib_numSucursal, ib_nombreSucursal, ib_fecha_envio, "
                + "ib_fecha_generacion, ib_hora_generacion, ib_num_cuotas, ib_num_cuotas_trancurridas, ib_plazo, ib_periodicidad, ib_fecha_desembolso, ib_fecha_vencimiento, ib_monto_credito, ib_saldo_total_dia, "
                + "ib_saldo_capital, ib_saldo_interes, ib_saldo_interes_vigente, ib_saldo_interes_vencido, ib_saldo_interes_vencido_90dias, ib_saldo_interes_cuentas_orden, ib_saldo_iva_interes, ib_saldo_interes_mora, ib_saldo_iva_mora, ib_saldo_multa, "
                + "ib_saldo_iva_multa, ib_capital_pagado, ib_interes_normal_pagado, ib_iva_interes_normal_pagado, ib_moratorio_pagado, ib_iva_moratorio_pagado, ib_multa_pagada, ib_iva_multa_pagada, ib_comision, ib_iva_comision, "
                + "ib_monto_desembolsado, ib_fecha_sig_amortizacion, ib_capital_sig_amortizacion, ib_interes_sig_amortizacion, ib_iva_interes_sig_amortizacion, ib_fondeador, ib_nombre_fondeador, ib_tasa_interes_sin_iva, ib_tasa_mora_sin_iva, ib_tasa_iva, "
                + "ib_saldo_con_intereses_al_final, ib_capital_vencido, ib_interes_vencido, ib_iva_interes_vencido, ib_total_vencido, ib_estatus, ib_producto, ib_fecha_incumplimiento, ib_fecha_a_cartera_vencida, ib_num_dias_mora, "
                + "ib_dias_transcurridos, ib_cuotas_vencidas, ib_num_pagos_realizados, ib_moto_total_pagado, ib_fecha_ultimo_pago, ib_bandera_reestructura, ib_credito_reestructurado, ib_dias_mora_reestructura,ib_tasa_preferencial_iva, ib_monto_seguro, ib_cuenta_bucket, ib_saldo_bucket, "
                + "ib_saldo_bonificacion_iva, ib_bonificacion_pagada, ib_origen, ib_cta_contable) "
                + "VALUES "
                + "( ?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,? )";

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);
            Iterator i = lista.iterator();

            while (i.hasNext()) {
                saldosIBS = (SaldoIBSVO) i.next();

                ps.setInt(1, saldosIBS.getCredito());
                ps.setString(2, saldosIBS.getReferencia());
                ps.setInt(3, saldosIBS.getIdClienteSICAP());
                ps.setInt(4, saldosIBS.getIdSolicitudSICAP());
                ps.setInt(5, saldosIBS.getIdClienteIBS());
                ps.setString(6, saldosIBS.getNombreCliente());
                ps.setString(7, saldosIBS.getRfc());
                ps.setInt(8, saldosIBS.getIdSucursal());
                ps.setString(9, saldosIBS.getNombreSucursal());
                ps.setDate(10, saldosIBS.getFechaEnvio());
                ps.setDate(11, saldosIBS.getFechaGeneracion());
                ps.setDouble(12, saldosIBS.getHoraGeneracion());
                ps.setInt(13, saldosIBS.getNumeroCuotas());
                ps.setInt(14, saldosIBS.getNumeroCuotasTranscurridas());
                ps.setInt(15, saldosIBS.getPlazo());
                ps.setInt(16, saldosIBS.getPeriodicidad());
                ps.setDate(17, saldosIBS.getFechaDesembolso());
                ps.setDate(18, saldosIBS.getFechaVencimiento());
                ps.setDouble(19, saldosIBS.getMontoCredito());
                ps.setDouble(20, saldosIBS.getSaldoTotalAlDia());
                ps.setDouble(21, saldosIBS.getSaldoCapital());
                ps.setDouble(22, saldosIBS.getSaldoInteres());
                ps.setDouble(23, saldosIBS.getSaldoInteresVigente());
                ps.setDouble(24, saldosIBS.getSaldoInteresVencido());
                ps.setDouble(25, saldosIBS.getSaldoInteresVencido90dias());
                ps.setDouble(26, saldosIBS.getSaldoInteresCtasOrden());
                ps.setDouble(27, saldosIBS.getSaldoIvaInteres());
                ps.setDouble(28, saldosIBS.getSaldoMora());
                ps.setDouble(29, saldosIBS.getSaldoIVAMora());
                ps.setDouble(30, saldosIBS.getSaldoMulta());
                ps.setDouble(31, saldosIBS.getSaldoIVAMulta());
                ps.setDouble(32, saldosIBS.getCapitalPagado());
                ps.setDouble(33, saldosIBS.getInteresNormalPagado());
                ps.setDouble(34, saldosIBS.getIvaInteresNormalPagado());
                ps.setDouble(35, saldosIBS.getMoratorioPagado());
                ps.setDouble(36, saldosIBS.getIvaMoraPagado());
                ps.setDouble(37, saldosIBS.getMultaPagada());
                ps.setDouble(38, saldosIBS.getIvaMultaPagado());
                ps.setDouble(39, saldosIBS.getComision());
                ps.setDouble(40, saldosIBS.getIvaComision());
                ps.setDouble(41, saldosIBS.getMontoDesembolsado());
                ps.setDate(42, saldosIBS.getFechaSigAmortizacion());
                ps.setDouble(43, saldosIBS.getCapitalSigAmortizacion());
                ps.setDouble(44, saldosIBS.getInteresSigAmortizacion());
                ps.setDouble(45, saldosIBS.getIvaSigAmortizacion());
                ps.setInt(46, saldosIBS.getFondeador());
                ps.setString(47, saldosIBS.getNombreFondeador());
                ps.setDouble(48, saldosIBS.getTasaInteresSinIVA());
                ps.setDouble(49, saldosIBS.getTasaMoraSinIVA());
                ps.setDouble(50, saldosIBS.getTasaIVA());
                ps.setDouble(51, saldosIBS.getSaldoConInteresAlFinal());
                ps.setDouble(52, saldosIBS.getCapitalVencido());
                ps.setDouble(53, saldosIBS.getInteresVencido());
                ps.setDouble(54, saldosIBS.getIvaInteresVencido());
                ps.setDouble(55, saldosIBS.getTotalVencido());
                ps.setInt(56, saldosIBS.getEstatus());
                ps.setInt(57, saldosIBS.getIdProducto());
                ps.setDate(58, saldosIBS.getFechaIncumplimiento());
                ps.setDate(59, saldosIBS.getFechaAcarteraVencida());
                ps.setInt(60, saldosIBS.getDiasMora());
                ps.setInt(61, saldosIBS.getDiasTranscurridos());
                ps.setInt(62, saldosIBS.getCuotasVencidas());
                ps.setInt(63, saldosIBS.getNumeroPagosRealizados());
                ps.setDouble(64, saldosIBS.getMontoTotalPagado());
                ps.setDate(65, saldosIBS.getFechaUltimoPago());
                ps.setString(66, saldosIBS.getBanderaReestructura());
                ps.setDouble(67, saldosIBS.getCreditoReestructurado());
                ps.setInt(68, saldosIBS.getDiasMoraReestructura());
                ps.setString(69, saldosIBS.getTasaPreferencialIVA());
                ps.setDouble(70, saldosIBS.getMontoSeguro());
                ps.setInt(71, saldosIBS.getCuentaBucket());
                ps.setDouble(72, saldosIBS.getSaldoBucket());
                ps.setDouble(73, saldosIBS.getSaldoBonificacionDeIVA());
                ps.setDouble(74, saldosIBS.getBonificacionPagada());
                ps.setInt(75, saldosIBS.getOrigen());
                ps.setLong(76, saldosIBS.getCtaContable());

                res = ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("addSaldosIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addSaldosIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }

    public int addSaldoIBS(SaldoIBSVO saldosIBS) throws ClientesException {

        Connection conn = null;
        int res = 0;

        String query
                = "INSERT INTO d_saldos "
                + "( ib_credito, ib_referencia, ib_numClienteSICAP, ib_numSolicitudSICAP, ib_numClienteIBS, ib_nombreCliente, ib_rfc, ib_numSucursal, ib_nombreSucursal, ib_fecha_envio, "
                + "ib_fecha_generacion, ib_hora_generacion, ib_num_cuotas, ib_num_cuotas_trancurridas, ib_plazo, ib_periodicidad, ib_fecha_desembolso, ib_fecha_vencimiento, ib_monto_credito, ib_saldo_total_dia, "
                + "ib_saldo_capital, ib_saldo_interes, ib_saldo_interes_vigente, ib_saldo_interes_vencido, ib_saldo_interes_vencido_90dias, ib_saldo_interes_cuentas_orden, ib_saldo_iva_interes, ib_saldo_interes_mora, ib_saldo_iva_mora, ib_saldo_multa, "
                + "ib_saldo_iva_multa, ib_capital_pagado, ib_interes_normal_pagado, ib_iva_interes_normal_pagado, ib_moratorio_pagado, ib_iva_moratorio_pagado, ib_multa_pagada, ib_iva_multa_pagada, ib_comision, ib_iva_comision, "
                + "ib_monto_desembolsado, ib_fecha_sig_amortizacion, ib_capital_sig_amortizacion, ib_interes_sig_amortizacion, ib_iva_interes_sig_amortizacion, ib_fondeador, ib_nombre_fondeador, ib_tasa_interes_sin_iva, ib_tasa_mora_sin_iva, ib_tasa_iva, "
                + "ib_saldo_con_intereses_al_final, ib_capital_vencido, ib_interes_vencido, ib_iva_interes_vencido, ib_total_vencido, ib_estatus, ib_producto, ib_fecha_incumplimiento, ib_fecha_a_cartera_vencida, ib_num_dias_mora, "
                + "ib_dias_transcurridos, ib_cuotas_vencidas, ib_num_pagos_realizados, ib_moto_total_pagado, ib_fecha_ultimo_pago, ib_bandera_reestructura, ib_credito_reestructurado, ib_dias_mora_reestructura,ib_tasa_preferencial_iva, ib_monto_seguro, ib_cuenta_bucket, ib_saldo_bucket, "
                + "ib_saldo_bonificacion_iva, ib_bonificacion_pagada, ib_origen, ib_cta_contable, ib_montoconintereses, ib_tasaelegida, ib_interes_xdevengar, ib_iva_interes_xdevengar, ib_numauditor, ib_integranteini, ib_integrantecan, ib_addintegrante) "
                + "VALUES "
                + "( ?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,0,0)";

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, saldosIBS.getCredito());
            ps.setString(2, saldosIBS.getReferencia());
            ps.setInt(3, saldosIBS.getIdClienteSICAP());
            ps.setInt(4, saldosIBS.getIdSolicitudSICAP());
            ps.setInt(5, saldosIBS.getIdClienteIBS());
            ps.setString(6, saldosIBS.getNombreCliente());
            ps.setString(7, saldosIBS.getRfc());
            ps.setInt(8, saldosIBS.getIdSucursal());
            ps.setString(9, saldosIBS.getNombreSucursal());
            ps.setDate(10, saldosIBS.getFechaEnvio());
            ps.setDate(11, saldosIBS.getFechaGeneracion());
            ps.setDouble(12, saldosIBS.getHoraGeneracion());
            ps.setInt(13, saldosIBS.getNumeroCuotas());
            ps.setInt(14, saldosIBS.getNumeroCuotasTranscurridas());
            ps.setInt(15, saldosIBS.getPlazo());
            ps.setInt(16, saldosIBS.getPeriodicidad());
            ps.setDate(17, saldosIBS.getFechaDesembolso());
            ps.setDate(18, saldosIBS.getFechaVencimiento());
            ps.setDouble(19, saldosIBS.getMontoCredito());
            ps.setDouble(20, saldosIBS.getSaldoTotalAlDia());
            ps.setDouble(21, saldosIBS.getSaldoCapital());
            ps.setDouble(22, saldosIBS.getSaldoInteres());
            ps.setDouble(23, saldosIBS.getSaldoInteresVigente());
            ps.setDouble(24, saldosIBS.getSaldoInteresVencido());
            ps.setDouble(25, saldosIBS.getSaldoInteresVencido90dias());
            ps.setDouble(26, saldosIBS.getSaldoInteresCtasOrden());
            ps.setDouble(27, saldosIBS.getSaldoIvaInteres());
            ps.setDouble(28, saldosIBS.getSaldoMora());
            ps.setDouble(29, saldosIBS.getSaldoIVAMora());
            ps.setDouble(30, saldosIBS.getSaldoMulta());
            ps.setDouble(31, saldosIBS.getSaldoIVAMulta());
            ps.setDouble(32, saldosIBS.getCapitalPagado());
            ps.setDouble(33, saldosIBS.getInteresNormalPagado());
            ps.setDouble(34, saldosIBS.getIvaInteresNormalPagado());
            ps.setDouble(35, saldosIBS.getMoratorioPagado());
            ps.setDouble(36, saldosIBS.getIvaMoraPagado());
            ps.setDouble(37, saldosIBS.getMultaPagada());
            ps.setDouble(38, saldosIBS.getIvaMultaPagado());
            ps.setDouble(39, saldosIBS.getComision());
            ps.setDouble(40, saldosIBS.getIvaComision());
            ps.setDouble(41, saldosIBS.getMontoDesembolsado());
            ps.setDate(42, saldosIBS.getFechaSigAmortizacion());
            ps.setDouble(43, saldosIBS.getCapitalSigAmortizacion());
            ps.setDouble(44, saldosIBS.getInteresSigAmortizacion());
            ps.setDouble(45, saldosIBS.getIvaSigAmortizacion());
            ps.setInt(46, saldosIBS.getFondeador());
            ps.setString(47, saldosIBS.getNombreFondeador());
            ps.setDouble(48, saldosIBS.getTasaInteresSinIVA());
            ps.setDouble(49, saldosIBS.getTasaMoraSinIVA());
            ps.setDouble(50, saldosIBS.getTasaIVA());
            ps.setDouble(51, saldosIBS.getSaldoConInteresAlFinal());
            ps.setDouble(52, saldosIBS.getCapitalVencido());
            ps.setDouble(53, saldosIBS.getInteresVencido());
            ps.setDouble(54, saldosIBS.getIvaInteresVencido());
            ps.setDouble(55, saldosIBS.getTotalVencido());
            ps.setInt(56, saldosIBS.getEstatus());
            ps.setInt(57, saldosIBS.getIdProducto());
            ps.setDate(58, saldosIBS.getFechaIncumplimiento());
            ps.setDate(59, saldosIBS.getFechaAcarteraVencida());
            ps.setInt(60, saldosIBS.getDiasMora());
            ps.setInt(61, saldosIBS.getDiasTranscurridos());
            ps.setInt(62, saldosIBS.getCuotasVencidas());
            ps.setInt(63, saldosIBS.getNumeroPagosRealizados());
            ps.setDouble(64, saldosIBS.getMontoTotalPagado());
            ps.setDate(65, saldosIBS.getFechaUltimoPago());
            ps.setString(66, saldosIBS.getBanderaReestructura());
            ps.setDouble(67, saldosIBS.getCreditoReestructurado());
            ps.setInt(68, saldosIBS.getDiasMoraReestructura());
            ps.setString(69, saldosIBS.getTasaPreferencialIVA());
            ps.setDouble(70, saldosIBS.getMontoSeguro());
            ps.setInt(71, saldosIBS.getCuentaBucket());
            ps.setDouble(72, saldosIBS.getSaldoBucket());
            ps.setDouble(73, saldosIBS.getSaldoBonificacionDeIVA());
            ps.setDouble(74, saldosIBS.getBonificacionPagada());
            ps.setInt(75, saldosIBS.getOrigen());
            ps.setLong(76, saldosIBS.getCtaContable());
            ps.setDouble(77, saldosIBS.getMontoConIntereses());
            ps.setDouble(78, saldosIBS.getTasaElegida());
            ps.setDouble(79, saldosIBS.getInteresPorDevengar());
            ps.setDouble(80, saldosIBS.getIvaInteresPorDevengar());
            ps.setDouble(81, saldosIBS.getNumAuditor());
            ps.setDouble(82, saldosIBS.getNumIntegrantesDesembolso());
            myLogger.debug("Ejecutando: "+ps);
            res = ps.executeUpdate();
            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("addSaldoIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addSaldoIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }

    public void respaldaSaldosIBS(int credito) throws ClientesException {
        Connection conn = null;
        try {
            conn = getConnection();
            
            String query = "insert into d_saldos_del (ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,ib_fecha_envio,ib_fecha_generacion,ib_hora_generacion,ib_num_cuotas,ib_num_cuotas_trancurridas,ib_plazo,ib_periodicidad,ib_fecha_desembolso,ib_fecha_vencimiento,ib_monto_credito,ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_interes_vencido_90dias,ib_saldo_interes_cuentas_orden,ib_saldo_iva_interes,ib_saldo_bonificacion_iva,ib_saldo_interes_mora,ib_saldo_iva_mora,ib_saldo_multa,ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_bonificacion_pagada,ib_moratorio_pagado,ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_comision,ib_iva_comision,ib_monto_seguro,ib_monto_desembolsado,ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_fondeador,ib_nombre_fondeador,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,ib_capital_vencido,ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,ib_producto,ib_fecha_incumplimiento,ib_fecha_a_cartera_vencida,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,ib_num_pagos_realizados,ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_bandera_reestructura,ib_credito_reestructurado,ib_dias_mora_reestructura,ib_tasa_preferencial_iva,ib_cuenta_bucket,ib_saldo_bucket,ib_cta_contable,ib_numdespacho,ib_max_cuotas_vencidas,ib_max_dias_mora,ib_montoconintereses,ib_tasaelegida,ib_interes_xdevengar,ib_iva_interes_xdevengar,ib_num_pago_sostenido,ib_numauditor,ib_integranteini,ib_integrantecan,ib_addintegrante,ib_interes_devengado,ib_iva_interes_devengado,ib_fecha_asig_garantia,ib_fondeo_garantia)"
                    + " (select ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,ib_fecha_envio,ib_fecha_generacion,ib_hora_generacion,ib_num_cuotas,ib_num_cuotas_trancurridas,ib_plazo,ib_periodicidad,ib_fecha_desembolso,ib_fecha_vencimiento,ib_monto_credito,ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_interes_vencido_90dias,ib_saldo_interes_cuentas_orden,ib_saldo_iva_interes,ib_saldo_bonificacion_iva,ib_saldo_interes_mora,ib_saldo_iva_mora,ib_saldo_multa,ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_bonificacion_pagada,ib_moratorio_pagado,ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_comision,ib_iva_comision,ib_monto_seguro,ib_monto_desembolsado,ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_fondeador,ib_nombre_fondeador,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,ib_capital_vencido,ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,ib_producto,ib_fecha_incumplimiento,ib_fecha_a_cartera_vencida,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,ib_num_pagos_realizados,ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_bandera_reestructura,ib_credito_reestructurado,ib_dias_mora_reestructura,ib_tasa_preferencial_iva,ib_cuenta_bucket,ib_saldo_bucket,ib_cta_contable,ib_numdespacho,ib_max_cuotas_vencidas,ib_max_dias_mora,ib_montoconintereses,ib_tasaelegida,ib_interes_xdevengar,ib_iva_interes_xdevengar,ib_num_pago_sostenido,ib_numauditor,ib_integranteini,ib_integrantecan,ib_addintegrante,ib_interes_devengado,ib_iva_interes_devengado,ib_fecha_asig_garantia,ib_fondeo_garantia"
                    + " FROM d_saldos WHERE ib_credito=?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, credito);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteSaldosIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteSaldosIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    
    public void deleteSaldosIBS(int credito, boolean conexionODS) throws ClientesException {
        Connection conn = null;
        try {
            if(conexionODS){
                conn = getODSConnection();
            }else{
                conn = getConnection();
            }
            String query = "DELETE FROM d_saldos WHERE ib_credito=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, credito);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteSaldosIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteSaldosIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void bajaLogicaSaldosIBS(int credito) {
        Connection conn = null;
        try {
            conn = getODSConnection();

            String query = "update d_saldos set ib_estatus = ? WHERE ib_credito = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, 5);
            ps.setInt(2, credito);
            myLogger.debug("Ejecutando: " + ps.toString());
            int result = ps.executeUpdate();
            myLogger.debug("Registros afectados: " + result);
        } catch (SQLException sqle) {
            myLogger.error("Error de conexión ODS...");
            myLogger.error("deleteSaldosIBS", sqle);
        } catch (Exception e) {
            myLogger.error("Error al realizar baja logica en saldos ODS");
            myLogger.error("deleteSaldosIBS", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                myLogger.error("Error de conexión ODS...");
                myLogger.error("deleteSaldosIBS", e);
            }
        }
    }

    public SaldoIBSVO getSaldo(int numCliente, int numSolicitud, int numProducto) throws ClientesException {

        SaldoIBSVO saldosIBS = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM D_SALDOS WHERE IB_NUMCLIENTESICAP = ? AND IB_NUMSOLICITUDSICAP = ? AND IB_PRODUCTO = ? AND IB_ESTATUS != 5";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setInt(3, numProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();
                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));
                saldosIBS.setInteresPorDevengar(rs.getDouble("ib_interes_xdevengar"));
                saldosIBS.setIvaInteresPorDevengar(rs.getDouble("ib_iva_interes_xdevengar"));
                saldosIBS.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));
                saldosIBS.setInteresDevengados(rs.getDouble("ib_interes_devengado"));
                saldosIBS.setIvaInteresDevengados(rs.getDouble("ib_iva_interes_devengado"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return saldosIBS;
    }

    public void updateSaldo(SaldoIBSVO saldo) throws ClientesException {

        SaldoIBSVO saldosIBS = null;
        Connection conn = null;
        int rs = 0;
        try {
            conn = getConnection();
            String query = "UPDATE  D_SALDOS SET IB_NUM_CUOTAS_TRANCURRIDAS = ?, IB_SALDO_TOTAL_DIA = ?, IB_SALDO_CAPITAL = ?, IB_SALDO_INTERES = ?, IB_SALDO_INTERES_VIGENTE = ?, IB_SALDO_INTERES_VENCIDO = ?, IB_SALDO_IVA_INTERES = ?, IB_SALDO_INTERES_MORA = ?, IB_SALDO_IVA_MORA = ?, IB_SALDO_INTERES_VENCIDO_90DIAS = ? , IB_SALDO_INTERES_CUENTAS_ORDEN = ?, "
                    + "IB_SALDO_MULTA = ?, IB_SALDO_IVA_MULTA = ?, IB_CAPITAL_PAGADO = ?, IB_INTERES_NORMAL_PAGADO = ?, IB_IVA_INTERES_NORMAL_PAGADO = ?, IB_MORATORIO_PAGADO = ?, IB_IVA_MORATORIO_PAGADO = ?, IB_MULTA_PAGADA = ?, IB_IVA_MULTA_PAGADA = ?,  IB_BONIFICACION_PAGADA = ?, IB_FECHA_SIG_AMORTIZACION = ?, "
                    + "IB_CAPITAL_SIG_AMORTIZACION = ?, IB_INTERES_SIG_AMORTIZACION = ?, IB_IVA_INTERES_SIG_AMORTIZACION = ?, IB_SALDO_CON_INTERESES_AL_FINAL = ?, IB_CAPITAL_VENCIDO = ?, IB_INTERES_VENCIDO = ?,IB_IVA_INTERES_VENCIDO = ?, IB_TOTAL_VENCIDO = ?, IB_ESTATUS = ?, IB_FECHA_INCUMPLIMIENTO = ?, IB_FECHA_A_CARTERA_VENCIDA = ?, "
                    + "IB_NUM_DIAS_MORA = ?, IB_DIAS_TRANSCURRIDOS = ?, IB_CUOTAS_VENCIDAS = ?, IB_NUM_PAGOS_REALIZADOS = ?, IB_MOTO_TOTAL_PAGADO = ?, IB_FECHA_ULTIMO_PAGO = ?, IB_BANDERA_REESTRUCTURA = ?, IB_CREDITO_REESTRUCTURADO = ?, IB_DIAS_MORA_REESTRUCTURA = ?, IB_SALDO_BUCKET = ?, IB_FECHA_GENERACION = ?, IB_CTA_CONTABLE = ?, IB_INTERES_XDEVENGAR = ?, "
                    + "IB_IVA_INTERES_XDEVENGAR = ? , IB_NUM_PAGO_SOSTENIDO = ?, ib_interes_devengado = ?, ib_iva_interes_devengado = ? "
                    + " WHERE IB_REFERENCIA = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            int param = 1;
            ps.setDouble(param++, saldo.getNumeroCuotasTranscurridas());
            ps.setDouble(param++, saldo.getSaldoTotalAlDia());
            ps.setDouble(param++, saldo.getSaldoCapital());
            ps.setDouble(param++, saldo.getSaldoInteres());
            ps.setDouble(param++, saldo.getSaldoInteresVigente());
            ps.setDouble(param++, saldo.getSaldoInteresVencido());
            ps.setDouble(param++, saldo.getSaldoIvaInteres());
            ps.setDouble(param++, saldo.getSaldoMora());
            ps.setDouble(param++, saldo.getSaldoIVAMora());
            ps.setDouble(param++, saldo.getSaldoInteresVencido90dias());
            ps.setDouble(param++, saldo.getSaldoInteresCtasOrden()); // param 10
            ps.setDouble(param++, saldo.getSaldoMulta());
            ps.setDouble(param++, saldo.getSaldoIVAMulta());
            ps.setDouble(param++, saldo.getCapitalPagado());
            ps.setDouble(param++, saldo.getInteresNormalPagado());
            ps.setDouble(param++, saldo.getIvaInteresNormalPagado());
            ps.setDouble(param++, saldo.getMoratorioPagado());
            ps.setDouble(param++, saldo.getIvaMoraPagado());
            ps.setDouble(param++, saldo.getMultaPagada());
            ps.setDouble(param++, saldo.getIvaMultaPagado());
            ps.setDouble(param++, saldo.getBonificacionPagada());
            ps.setDate(param++, saldo.getFechaSigAmortizacion());  //param 20
            ps.setDouble(param++, saldo.getCapitalSigAmortizacion());
            ps.setDouble(param++, saldo.getInteresSigAmortizacion());
            ps.setDouble(param++, saldo.getIvaSigAmortizacion());
            ps.setDouble(param++, saldo.getSaldoConInteresAlFinal());
            ps.setDouble(param++, saldo.getCapitalVencido());
            ps.setDouble(param++, saldo.getInteresVencido());
            ps.setDouble(param++, saldo.getIvaInteresVencido());
            ps.setDouble(param++, saldo.getTotalVencido());
            ps.setDouble(param++, saldo.getEstatus());
            ps.setDate(param++, saldo.getFechaIncumplimiento()); //param 30
            ps.setDate(param++, saldo.getFechaAcarteraVencida());
            ps.setInt(param++, saldo.getDiasMora());
            ps.setInt(param++, saldo.getDiasTranscurridos());
            ps.setInt(param++, saldo.getCuotasVencidas());
            ps.setInt(param++, saldo.getNumeroPagosRealizados());
            ps.setDouble(param++, saldo.getMontoTotalPagado());
            ps.setDate(param++, saldo.getFechaUltimoPago());
            ps.setString(param++, saldo.getBanderaReestructura());
            ps.setDouble(param++, saldo.getCreditoReestructurado());
            ps.setInt(param++, saldo.getDiasMoraReestructura()); //param 40
            ps.setDouble(param++, saldo.getSaldoBucket());
            ps.setDate(param++, saldo.getFechaGeneracion());
            ps.setLong(param++, saldo.getCtaContable());
            ps.setDouble(param++, saldo.getInteresPorDevengar());
            ps.setDouble(param++, saldo.getIvaInteresPorDevengar());
            ps.setInt(param++, saldo.getNumPagoSostenido());
            ps.setDouble(param++, saldo.getInteresDevengados());
            ps.setDouble(param++, saldo.getIvaInteresDevengados());
            ps.setString(param++, saldo.getReferencia());
            myLogger.debug("ps* " + ps);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + saldo.getReferencia() + "]");

            rs = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    public SaldoIBSVO[] getVSaldosIBS(int tipoOper) {
        Connection cn = null;
        ArrayList<SaldoIBSVO> arrayListSaldos = new ArrayList<SaldoIBSVO>();
        SaldoIBSVO[] arraySaldo = null;
        String query = "";

        if (tipoOper == 1) {
            query = "SELECT * FROM D_SALDOS WHERE IB_PRODUCTO NOT IN (3,5) AND IB_FECHA_DESEMBOLSO>='2013-07-01' "
                    + "AND IB_REFERENCIA NOT IN (SELECT BB_REFERENCIA FROM D_BITACORA_BUROCIRCULO WHERE BB_ESTATUS=2);";
        } else if (tipoOper == 2) {
            //Grupales
            /*query = "SELECT * FROM D_SALDOS WHERE IB_PRODUCTO =3  AND IB_FECHA_DESEMBOLSO>='2013-07-01' "
             + "AND IB_REFERENCIA NOT IN (SELECT BB_REFERENCIA FROM D_BITACORA_BUROCIRCULO WHERE BB_ESTATUS=2) ";*/
            query = "SELECT * FROM d_saldos INNER JOIN d_ciclos_grupales ON (ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo) "
                    + "WHERE ib_producto=3 AND ib_fecha_desembolso>='2013-07-01' AND ci_id_migracion=0 AND ib_referencia NOT IN (SELECT bb_referencia FROM d_bitacora_burocirculo WHERE bb_estatus=2);";
        }

        myLogger.debug("Procesando query: + " + query);

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SaldoIBSVO saldo = new SaldoIBSVO();
                saldo.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldo.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldo.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldo.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldo.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldo.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldo.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldo.setInteresSigAmortizacion(Convertidor.getMontoIva(rs.getDouble("ib_interes_sig_amortizacion"), (saldo.getIdSucursal()), 0));
                saldo.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldo.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldo.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldo.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldo.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldo.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldo.setRfc(rs.getString("ib_rfc"));
                saldo.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldo.setEstatus(rs.getInt("ib_estatus"));
                saldo.setIdProducto(rs.getInt("ib_producto"));
                saldo.setReferencia(rs.getString("ib_referencia"));
                saldo.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldo.setCredito(rs.getInt("ib_credito"));
                saldo.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                arrayListSaldos.add(saldo);
            }
        } catch (SQLException exc) {
            myLogger.error("SQLException en saldoT24DAO.getSaldosT24()::", exc);
        } catch (NamingException exc) {
            myLogger.error("NamingException en saldoT24DAO.getSaldosT24()::", exc);
        } catch (Exception exc) {
            myLogger.error("Esception caugth::", exc);
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc1) {
                    myLogger.error("SQLException en saldoIBSDAO.getSaldosT24()::", exc1);
                }
            }
        }
        arraySaldo = new SaldoIBSVO[arrayListSaldos.size()];
        for (int i = 0; i < arraySaldo.length; i++) {
            arraySaldo[i] = arrayListSaldos.get(i);
        }
        return arraySaldo;
    }

    public SaldoIBSVO getSaldoCongelado(int numCliente, int numCredito) throws ClientesException {

        SaldoIBSVO saldosIBS = new SaldoIBSVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT ib_num_cuotas_trancurridas,ib_estatus FROM d_saldos WHERE ib_numclientesicap = ? AND ib_credito = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + numCliente + "," + numCredito + " ]");
            rs = ps.executeQuery();

            if (rs.next()) {
                saldosIBS = new SaldoIBSVO(rs.getInt("ib_num_cuotas_trancurridas"), rs.getInt("ib_estatus"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoCongelado", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoCongelado", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public void updateEstatusSaldo(SaldoIBSVO saldo) throws ClientesException {

        String query = "UPDATE d_saldos SET ib_estatus = 4 WHERE ib_numclientesicap = ? AND ib_credito = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + saldo.getIdClienteSICAP() + "," + saldo.getCredito() + " ]");
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updateEstatusSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    public ArrayList<SaldoIBSVO> getAplicarCarteraCedida(int idSucursal) throws ClientesException {

        ArrayList<SaldoIBSVO> saldos = new ArrayList<SaldoIBSVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ib_credito,ib_numclientesicap,ib_numsolicitudsicap,ib_nombrecliente,ib_monto_credito,ib_saldo_con_intereses_al_final,ib_num_dias_mora,ib_referencia,ib_fecha_vencimiento "
                + "FROM d_saldos "
                + "WHERE ib_numsucursal= ? AND ib_num_dias_mora>=120 AND ib_estatus!=7";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros[" + idSucursal + "]");

            res = ps.executeQuery();
            while (res.next()) {
                saldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getInt("ib_numclientesicap"), res.getInt("ib_numsolicitudsicap"), res.getString("ib_nombrecliente"), res.getDouble("ib_monto_credito"),
                        res.getDouble("ib_saldo_con_intereses_al_final"), res.getInt("ib_num_dias_mora"), res.getString("ib_referencia"), res.getDate("ib_fecha_vencimiento")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getAplicarCarteraCedida", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getAplicarCarteraCedida", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return saldos;
    }

     public void updateOtrosDatos(Connection conn, int idGrupo, int idCiclo, int fondeador, int despacho) throws ClientesException {
         updateOtrosDatos(conn,idGrupo, idCiclo, fondeador, despacho,true);
     }
    public void updateOtrosDatos(Connection conn, int idGrupo, int idCiclo, int fondeador, int despacho,boolean cierraConexion) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_saldos SET ib_fondeador= ?,ib_numdespacho= ? WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ?";

        try {
            if (conn == null) {
                con = getConnection();
                ps = con.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, fondeador);
            ps.setInt(2, despacho);
            ps.setInt(3, idGrupo);
            ps.setInt(4, idCiclo);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros[" + fondeador + ", " + despacho + ", " + idGrupo + ", " + idCiclo + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateOtrosDatos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateOtrosDatos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            if(cierraConexion){
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    throw new ClientesDBException(e.getMessage());
                }
            }
            
        }
    }

    public double getSaldoCarteraCedida(int credito) throws ClientesException {

        double saldo = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT pc_monto FROM d_saldos INNER JOIN d_pagos_cartera ON (ib_referencia=pc_referencia AND ib_fecha_generacion=pc_fecha_pago) "
                + "WHERE ib_estatus=7 AND ib_credito=?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, credito);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros[" + credito + "]");
            res = ps.executeQuery();
            while (res.next()) {
                saldo = res.getDouble("pc_monto");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoCarteraCedida", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoCarteraCedida", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldo;
    }

    public int updatePagoSaldoCierre(CreditoCartVO credito) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        int res = 0;
        String query = "UPDATE d_saldos SET ib_saldo_bucket= ? WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ? AND ib_credito= ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, credito.getMontoCuenta());
            ps.setInt(2, credito.getNumCliente());
            ps.setInt(3, credito.getNumSolicitud());
            ps.setInt(4, credito.getNumCredito());
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros[" + credito.getNumCliente() + "," + credito.getNumSolicitud() + "," + credito.getNumCredito() + "]");

            if (ps.executeUpdate() == 1) {
                res = 1;
            }

        } catch (SQLException sqle) {
            myLogger.error("updatePagoSaldoCierre", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updatePagoSaldoCierre", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }

    public ArrayList<SaldoIBSVO> getSaldosActivosCierre(int idProducto, String fecha, int estatus) throws ClientesException {

        ArrayList<SaldoIBSVO> arrSaldos = new ArrayList<SaldoIBSVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numsucursal,ib_fecha_generacion,"
                + "ib_num_cuotas,ib_num_cuotas_trancurridas,ib_plazo,ib_periodicidad,ib_fecha_desembolso,"
                + "ib_fecha_vencimiento,ib_monto_credito,ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,"
                + "ib_saldo_interes_vencido,ib_saldo_iva_interes,ib_saldo_interes_mora,ib_saldo_iva_mora,ib_saldo_multa,"
                + "ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_moratorio_pagado,"
                + "ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,"
                + "ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,"
                + "ib_capital_vencido,ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,"
                + "ib_producto,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,ib_num_pagos_realizados,"
                + "ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_saldo_bucket,ib_max_cuotas_vencidas,ib_max_dias_mora "
                + "FROM d_saldos WHERE ib_producto= ? AND ib_estatus= ? and ib_fecha_desembolso<=?;";
        //+ "FROM d_saldos WHERE ib_producto= ? AND ib_estatus= ? and ib_fecha_desembolso<=? and ib_fecha_generacion!='2013-10-06';";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            ps.setInt(2, estatus);
            ps.setString(3, fecha);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idProducto + ", " + estatus + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosActivosCierre", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosActivosCierre", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrSaldos;
    }

    public void actSaldosCierre(SaldoIBSVO saldo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_saldos SET ib_saldo_total_dia= ?,ib_saldo_capital= ?,ib_saldo_interes= ?,ib_saldo_interes_vigente= ?,ib_saldo_interes_vencido= ?,ib_saldo_iva_interes= ?,"
                + "ib_saldo_interes_mora= ?,ib_saldo_iva_mora= ?,ib_saldo_multa= ?,ib_saldo_iva_multa= ?,ib_capital_pagado= ?,ib_interes_normal_pagado= ?,"
                + "ib_iva_interes_normal_pagado= ?,ib_fecha_sig_amortizacion= ?,ib_capital_sig_amortizacion= ?,ib_interes_sig_amortizacion= ?,ib_iva_interes_sig_amortizacion= ?,ib_saldo_con_intereses_al_final= ?,"
                + "ib_capital_vencido= ?,ib_interes_vencido= ?,ib_iva_interes_vencido= ?,ib_total_vencido= ?,ib_estatus= ?,ib_moto_total_pagado= ?,ib_fecha_ultimo_pago= ?,"
                + "ib_saldo_bucket= ?,ib_fecha_generacion= ?,ib_num_cuotas_trancurridas= ?,ib_num_dias_mora= ?,ib_cuotas_vencidas= ?,ib_dias_transcurridos= ?,"
                + "ib_num_pagos_realizados= ?,ib_max_cuotas_vencidas= ?,ib_max_dias_mora= ?,ib_moratorio_pagado= ?,ib_iva_moratorio_pagado= ?,ib_multa_pagada= ?,ib_iva_multa_pagada= ? "
                + "WHERE ib_numclientesicap= ? AND ib_credito= ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, saldo.getSaldoTotalAlDia());
            ps.setDouble(2, saldo.getSaldoCapital());
            ps.setDouble(3, saldo.getSaldoInteres());
            ps.setDouble(4, saldo.getSaldoInteresVigente());
            ps.setDouble(5, saldo.getSaldoInteresVencido());
            ps.setDouble(6, saldo.getSaldoIvaInteres());
            ps.setDouble(7, saldo.getSaldoMora());
            ps.setDouble(8, saldo.getSaldoIVAMora());
            ps.setDouble(9, saldo.getSaldoMulta());
            ps.setDouble(10, saldo.getSaldoIVAMulta());
            ps.setDouble(11, saldo.getCapitalPagado());
            ps.setDouble(12, saldo.getInteresNormalPagado());
            ps.setDouble(13, saldo.getIvaInteresNormalPagado());
            ps.setDate(14, saldo.getFechaSigAmortizacion());
            ps.setDouble(15, saldo.getCapitalSigAmortizacion());
            ps.setDouble(16, saldo.getInteresSigAmortizacion());
            ps.setDouble(17, saldo.getIvaSigAmortizacion());
            ps.setDouble(18, saldo.getSaldoConInteresAlFinal());
            ps.setDouble(19, saldo.getCapitalVencido());
            ps.setDouble(20, saldo.getInteresVencido());
            ps.setDouble(21, saldo.getIvaInteresVencido());
            ps.setDouble(22, saldo.getTotalVencido());
            ps.setInt(23, saldo.getEstatus());
            ps.setDouble(24, saldo.getMontoTotalPagado());
            ps.setDate(25, saldo.getFechaUltimoPago());
            ps.setDouble(26, saldo.getSaldoBucket());
            ps.setDate(27, saldo.getFechaGeneracion());
            ps.setInt(28, saldo.getNumeroCuotasTranscurridas());
            ps.setInt(29, saldo.getDiasMora());
            ps.setInt(30, saldo.getCuotasVencidas());
            ps.setInt(31, saldo.getDiasTranscurridos());
            ps.setInt(32, saldo.getNumeroPagosRealizados());
            ps.setInt(33, saldo.getMaxCuotasVencidas());
            ps.setInt(34, saldo.getMaxDiasMora());
            ps.setDouble(35, saldo.getMoratorioPagado());
            ps.setDouble(36, saldo.getIvaMoraPagado());
            ps.setDouble(37, saldo.getMultaPagada());
            ps.setDouble(38, saldo.getIvaMultaPagado());
            ps.setInt(39, saldo.getIdClienteSICAP());
            ps.setInt(40, saldo.getCredito());

            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("actSaldosCierre", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actSaldosCierre", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    public int cicloAntLiquidado(int idGrupo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT COUNT(ib_estatus) AS activos FROM d_saldos WHERE ib_numclientesicap=? AND ib_estatus NOT IN (3,5)";
        int estatus = 0;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);

            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idGrupo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                estatus = res.getInt("activos");
            }

        } catch (SQLException sqle) {
            myLogger.error("cicloAntLiquidado", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("cicloAntLiquidado", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return estatus;
    }

    public boolean setEjecutivo(CicloGrupalVO grupo, int idEjecutivo, Connection con) throws SQLException {

        boolean listo = true;
        String query = "";
        PreparedStatement pstm = null;
        try {
            query = "UPDATE d_saldos SET ib_cta_contable= ? WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idEjecutivo);
            pstm.setInt(2, grupo.idGrupo);
            pstm.setInt(3, grupo.idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idEjecutivo + ", " + grupo.idGrupo + ", " + grupo.idCiclo + "]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Problema dentro setEjecutivo", e);
            con.rollback();
            return listo = false;
        }
        return listo;
    }

    public void setFechaAlDia(Date fechaCierre) throws SQLException, ClientesException, NamingException {

        boolean listo = true;
        String query = "";
        PreparedStatement pstm = null;
        Connection con = null;
        try {
            query = "UPDATE d_saldos SET ib_fecha_generacion=? WHERE ib_fecha_generacion<? AND ib_estatus=1;";
            //query = "UPDATE d_saldos SET ib_fecha_generacion=? WHERE ib_estatus IN (1,2);";
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechaCierre);
            pstm.setDate(2, fechaCierre);
            myLogger.debug("Ejecutando = " + pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Problema dentro setFechaAlDia", e);
            con.close();
        }
        con.close();
    }

    public SaldoIBSVO getSaldoStatus(int numCliente, int numCredito, int status) throws ClientesException {
        SaldoIBSVO saldosIBS = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos WHERE ib_numClienteSICAP = ? AND ib_credito = ? AND ib_estatus in (?)";
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + status + "]");

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, status);

            rs = ps.executeQuery();

            if (rs.next()) {
                saldosIBS = new SaldoIBSVO();

                saldosIBS.setOrigen(rs.getInt("ib_origen"));
                saldosIBS.setCredito(rs.getInt("ib_credito"));
                saldosIBS.setReferencia(rs.getString("ib_referencia"));
                saldosIBS.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldosIBS.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldosIBS.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldosIBS.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldosIBS.setRfc(rs.getString("ib_rfc"));
                saldosIBS.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldosIBS.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldosIBS.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldosIBS.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosIBS.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldosIBS.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldosIBS.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosIBS.setPlazo(rs.getInt("ib_plazo"));
                saldosIBS.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldosIBS.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldosIBS.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldosIBS.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldosIBS.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldosIBS.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldosIBS.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldosIBS.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldosIBS.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldosIBS.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldosIBS.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldosIBS.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldosIBS.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldosIBS.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldosIBS.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldosIBS.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldosIBS.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldosIBS.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldosIBS.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldosIBS.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldosIBS.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldosIBS.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldosIBS.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldosIBS.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldosIBS.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldosIBS.setComision(rs.getDouble("ib_comision"));
                saldosIBS.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldosIBS.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldosIBS.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldosIBS.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldosIBS.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldosIBS.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldosIBS.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldosIBS.setFondeador(rs.getInt("ib_fondeador"));
                saldosIBS.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldosIBS.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldosIBS.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldosIBS.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldosIBS.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldosIBS.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldosIBS.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldosIBS.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldosIBS.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldosIBS.setEstatus(rs.getInt("ib_estatus"));
                saldosIBS.setIdProducto(rs.getInt("ib_producto"));
                saldosIBS.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldosIBS.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldosIBS.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldosIBS.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldosIBS.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldosIBS.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldosIBS.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldosIBS.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldosIBS.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldosIBS.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldosIBS.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldosIBS.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldosIBS.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldosIBS.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldosIBS.setCtaContable(rs.getLong("ib_cta_contable"));

            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoStatus", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoStatus", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosIBS;
    }

    public SaldoIBSVO getSaldoFavor(int numEquipo, int numCiclo) throws ClientesException {
        SaldoIBSVO saldoFavor = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT ib_numclientesicap, ib_numsolicitudsicap, ib_nombrecliente, ib_saldo_bucket FROM d_saldos "
                    + "WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=? AND ib_estatus=3 AND ib_saldo_bucket!=0";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + ps.toString());
            if (rs.next()) {
                saldoFavor = new SaldoIBSVO();
                saldoFavor.setIdClienteSICAP(rs.getInt("ib_numclientesicap"));
                saldoFavor.setIdSolicitudSICAP(rs.getInt("ib_numsolicitudsicap"));
                saldoFavor.setNombreCliente(rs.getString("ib_nombrecliente"));
                saldoFavor.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoFavor", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoFavor", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldoFavor;
    }

    public void eliminaSaldoBucket(int numEquipo, int numCiclo) throws SQLException, ClientesException, NamingException {
        Connection con = null;
        String query = "UPDATE d_saldos SET ib_saldo_bucket=0 WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?";

        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug(ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Problema dentro eliminaSaldoBucket", e);
            con.close();
        }
        con.close();
    }
    
    public int updateAuditorSaldos(int idAuditor, int idSucursal) throws ClientesDBException, NamingException{
        
        int res = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        String query = "UPDATE d_saldos SET ib_numauditor=? WHERE ib_estatus IN (1,2,4,6) AND ib_numsucursal=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idAuditor);
            pstm.setInt(2, idSucursal);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateAuditorSaldos", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }
    
    public String getSaldosfechacierre(String fechacierre) throws ClientesException {

        SaldoIBSVO saldosIBS = new SaldoIBSVO();
        String resultado = "Cantidad\tEstatus\tFechaGeneracion\n";
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "select count(ib_numclientesicap) as cantidad,ib_estatus, ib_fecha_generacion from d_saldos "
                    +"where ib_fecha_desembolso<? and ib_estatus in (1,2,4,6) group by ib_fecha_generacion,ib_estatus;";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, fechacierre);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " +fechacierre+ " ]");
            rs = ps.executeQuery();
             while (rs.next()) {
                 resultado +=rs.getString("cantidad");
                 resultado+="\t";
                 resultado +=rs.getString("ib_estatus");
                 resultado+="\t";
                 resultado +=rs.getString("ib_fecha_geneRACION");
                 resultado+="\n";        
             }
        } catch (SQLException sqle) {
            myLogger.error("getSaldos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return resultado;
    }
    public SaldoIBSVO getSaldoIntegranteActivo(int idCliente) throws ClientesException {

        SaldoIBSVO saldo = new SaldoIBSVO();
        String query = "SELECT d_saldos.* FROM d_integrantes_ciclo LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap AND ic_numciclo=ib_numsolicitudsicap) "
                + "WHERE ic_numcliente=? AND ic_estatus=0 AND ib_estatus!=3";        
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                saldo.setOrigen(rs.getInt("ib_origen"));
                saldo.setCredito(rs.getInt("ib_credito"));
                saldo.setReferencia(rs.getString("ib_referencia"));
                saldo.setIdClienteSICAP(rs.getInt("ib_numClienteSICAP"));
                saldo.setIdSolicitudSICAP(rs.getInt("ib_numSolicitudSICAP"));
                saldo.setIdClienteIBS(rs.getInt("ib_numClienteIBS"));
                saldo.setNombreCliente(rs.getString("ib_nombreCliente"));
                saldo.setRfc(rs.getString("ib_rfc"));
                saldo.setIdSucursal(rs.getInt("ib_numSucursal"));
                saldo.setNombreSucursal(rs.getString("ib_nombreSucursal"));
                saldo.setFechaEnvio(rs.getDate("ib_fecha_envio"));
                saldo.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldo.setHoraGeneracion(rs.getInt("ib_hora_generacion"));
                saldo.setNumeroCuotas(rs.getInt("ib_num_cuotas"));
                saldo.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldo.setPlazo(rs.getInt("ib_plazo"));
                saldo.setPeriodicidad(rs.getInt("ib_periodicidad"));
                saldo.setFechaDesembolso(rs.getDate("ib_fecha_desembolso"));
                saldo.setFechaVencimiento(rs.getDate("ib_fecha_vencimiento"));
                saldo.setMontoCredito(rs.getDouble("ib_monto_credito"));
                saldo.setSaldoTotalAlDia(rs.getDouble("ib_saldo_total_dia"));
                saldo.setSaldoCapital(rs.getDouble("ib_saldo_capital"));
                saldo.setSaldoInteres(rs.getDouble("ib_saldo_interes"));
                saldo.setSaldoInteresVigente(rs.getDouble("ib_saldo_interes_vigente"));
                saldo.setSaldoInteresVencido(rs.getDouble("ib_saldo_interes_vencido"));
                saldo.setSaldoInteresVencido90dias(rs.getDouble("ib_saldo_interes_vencido_90dias"));
                saldo.setSaldoInteresCtasOrden(rs.getDouble("ib_saldo_interes_cuentas_orden"));
                saldo.setSaldoIvaInteres(rs.getDouble("ib_saldo_iva_interes"));
                saldo.setSaldoBonificacionDeIVA(rs.getDouble("ib_saldo_bonificacion_iva"));
                saldo.setSaldoMora(rs.getDouble("ib_saldo_interes_mora"));
                saldo.setSaldoIVAMora(rs.getDouble("ib_saldo_iva_mora"));
                saldo.setSaldoMulta(rs.getDouble("ib_saldo_multa"));
                saldo.setSaldoIVAMulta(rs.getDouble("ib_saldo_iva_multa"));
                saldo.setCapitalPagado(rs.getDouble("ib_capital_pagado"));
                saldo.setInteresNormalPagado(rs.getDouble("ib_interes_normal_pagado"));
                saldo.setIvaInteresNormalPagado(rs.getDouble("ib_iva_interes_normal_pagado"));
                saldo.setBonificacionPagada(rs.getDouble("ib_bonificacion_pagada"));
                saldo.setMoratorioPagado(rs.getDouble("ib_moratorio_pagado"));
                saldo.setIvaMoraPagado(rs.getDouble("ib_iva_moratorio_pagado"));
                saldo.setMultaPagada(rs.getDouble("ib_multa_pagada"));
                saldo.setIvaMultaPagado(rs.getDouble("ib_iva_multa_pagada"));
                saldo.setComision(rs.getDouble("ib_comision"));
                saldo.setIvaComision(rs.getDouble("ib_iva_comision"));
                saldo.setMontoSeguro(rs.getDouble("ib_monto_seguro"));
                saldo.setMontoDesembolsado(rs.getDouble("ib_monto_desembolsado"));
                saldo.setFechaSigAmortizacion(rs.getDate("ib_fecha_sig_amortizacion"));
                saldo.setCapitalSigAmortizacion(rs.getDouble("ib_capital_sig_amortizacion"));
                saldo.setInteresSigAmortizacion(rs.getDouble("ib_interes_sig_amortizacion"));
                saldo.setIvaSigAmortizacion(rs.getDouble("ib_iva_interes_sig_amortizacion"));
                saldo.setFondeador(rs.getInt("ib_fondeador"));
                saldo.setNombreFondeador(rs.getString("ib_nombre_fondeador"));
                saldo.setTasaInteresSinIVA(rs.getDouble("ib_tasa_interes_sin_iva"));
                saldo.setTasaMoraSinIVA(rs.getDouble("ib_tasa_mora_sin_iva"));
                saldo.setTasaIVA(rs.getDouble("ib_tasa_iva"));
                saldo.setSaldoConInteresAlFinal(rs.getDouble("ib_saldo_con_intereses_al_final"));
                saldo.setCapitalVencido(rs.getDouble("ib_capital_vencido"));
                saldo.setInteresVencido(rs.getDouble("ib_interes_vencido"));
                saldo.setIvaInteresVencido(rs.getDouble("ib_iva_interes_vencido"));
                saldo.setTotalVencido(rs.getDouble("ib_total_vencido"));
                saldo.setEstatus(rs.getInt("ib_estatus"));
                saldo.setIdProducto(rs.getInt("ib_producto"));
                saldo.setFechaIncumplimiento(rs.getDate("ib_fecha_incumplimiento"));
                saldo.setFechaAcarteraVencida(rs.getDate("ib_fecha_a_cartera_vencida"));
                saldo.setDiasMora(rs.getInt("ib_num_dias_mora"));
                saldo.setDiasTranscurridos(rs.getInt("ib_dias_transcurridos"));
                saldo.setCuotasVencidas(rs.getInt("ib_cuotas_vencidas"));
                saldo.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
                saldo.setMontoTotalPagado(rs.getDouble("ib_moto_total_pagado"));
                saldo.setFechaUltimoPago(rs.getDate("ib_fecha_ultimo_pago"));
                saldo.setBanderaReestructura(rs.getString("ib_bandera_reestructura"));
                saldo.setCreditoReestructurado(rs.getDouble("ib_credito_reestructurado"));
                saldo.setDiasMoraReestructura(rs.getInt("ib_dias_mora_reestructura"));
                saldo.setTasaPreferencialIVA(rs.getString("ib_tasa_preferencial_iva"));
                saldo.setCuentaBucket(rs.getInt("ib_cuenta_bucket"));
                saldo.setSaldoBucket(rs.getDouble("ib_saldo_bucket"));
                saldo.setCtaContable(rs.getLong("ib_cta_contable"));
                saldo.setNumDespacho(rs.getInt("ib_numdespacho"));
                saldo.setMaxCuotasVencidas(rs.getInt("ib_numdespacho"));
                saldo.setMaxDiasMora(rs.getInt("ib_numdespacho"));
                saldo.setMontoConIntereses(rs.getDouble("ib_saldo_bucket"));
                saldo.setTasaElegida(rs.getDouble("ib_tasaelegida"));
                saldo.setNumPagoSostenido(rs.getInt("ib_num_pago_sostenido"));                
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteActivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteActivo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return saldo;

    }
    
    public SaldoIBSVO getNumPagosATiempo(Connection con, TablaAmortVO tabla) throws ClientesException {

        SaldoIBSVO saldosVO = new SaldoIBSVO();
        String query = "SELECT ib_fecha_generacion,ib_num_cuotas_trancurridas,ib_estatus,ib_num_pagos_realizados FROM d_saldos WHERE ib_numclientesicap=? AND ib_credito=?;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tabla.getNumCliente());
            ps.setInt(2, tabla.getNumCredito());
            myLogger.debug("Ejecutando = " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                saldosVO.setFechaGeneracion(rs.getDate("ib_fecha_generacion"));
                saldosVO.setNumeroCuotasTranscurridas(rs.getInt("ib_num_cuotas_trancurridas"));
                saldosVO.setEstatus(rs.getInt("ib_estatus"));
                saldosVO.setNumeroPagosRealizados(rs.getInt("ib_num_pagos_realizados"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getNumPagosATiempo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumPagosATiempo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return saldosVO;
    }
    
    public void updateSaldosInterciclo(Connection con,SaldoIBSVO saldoVO) throws ClientesDBException, NamingException{
        
        PreparedStatement pstm = null;
        String query = "UPDATE d_saldos SET ib_fecha_generacion=?,ib_monto_credito=?,ib_saldo_total_dia=?,ib_saldo_capital=?,ib_monto_desembolsado=?,ib_saldo_con_intereses_al_final=?,ib_addintegrante=?,"
                + "ib_interes_xdevengar=?,ib_iva_interes_xdevengar=? ";
        if (saldoVO.getFechaAsigGarantia()!=null){
                query += ", ib_fecha_asig_garantia = ? ";
                        }
                query += "WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?;";
        try {
            pstm = con.prepareStatement(query);
            pstm.setDate(1, saldoVO.getFechaGeneracion());
            pstm.setDouble(2, saldoVO.getMontoCredito());
            pstm.setDouble(3, saldoVO.getSaldoTotalAlDia());
            pstm.setDouble(4, saldoVO.getSaldoCapital());
            pstm.setDouble(5, saldoVO.getMontoDesembolsado());
            pstm.setDouble(6, saldoVO.getSaldoConInteresAlFinal());
            pstm.setInt(7, saldoVO.getNumIntegrantesAdicionales());
            pstm.setDouble(8, saldoVO.getInteresPorDevengar());
            pstm.setDouble(9, saldoVO.getIvaInteresPorDevengar());
            if (saldoVO.getFechaAsigGarantia()!=null){
                pstm.setDate(10, saldoVO.getFechaAsigGarantia());
                pstm.setInt(11, saldoVO.getIdClienteSICAP());
                pstm.setInt(12, saldoVO.getIdSolicitudSICAP());
            }else{
                pstm.setInt(10, saldoVO.getIdClienteSICAP());
                pstm.setInt(11, saldoVO.getIdSolicitudSICAP());
            }
            myLogger.debug("pstm "+pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateSaldosInterciclo", e);
        } finally{
            try {
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void updateIntegrantesCancelados(Connection con, SaldoIBSVO saldoVO) throws ClientesDBException, NamingException{
        
        PreparedStatement pstm = null;
        String query = "UPDATE d_saldos SET ib_integrantecan=? WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?;";
        try {
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldoVO.getNumIntegrantesCancelacion());
            pstm.setInt(2, saldoVO.getIdClienteSICAP());
            pstm.setInt(3, saldoVO.getIdSolicitudSICAP());
            myLogger.debug("pstm "+pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateIntegrantesCancelados", e);
        } finally{
            try {
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    
    //*******************//*******************//*******************//*******************//*******************//*******************
    //
    //Se fue  getSumaSaldoCapitalByFondeadoGarantia sin prametro de conexion
    //
    public double getSumaSaldoCapitalByFondeadoGarantiaAndNotEstatus(int idFondeoGarantia, int notEstatus, Connection con) throws ClientesException{
        
        double saldoGarantizado=0;
        
        PreparedStatement ps = null;
        ResultSet res = null;
        
        String query = "SELECT sum(ib_saldo_capital) as Saldo_Garantizado "
                + " FROM d_saldos "
                + " WHERE ib_fondeo_garantia = ? "
                + " AND ib_estatus <> ?; ";
        

        try {
            if(con == null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeoGarantia);
            ps.setInt(2, notEstatus);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeoGarantia +","+notEstatus+"]");
            res = ps.executeQuery();
            while (res.next()) {
                saldoGarantizado=res.getDouble("Saldo_Garantizado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoByFondeador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoByFondeador", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return saldoGarantizado;
    }
    
    
    public ArrayList<SaldoIBSVO> getSaldosEstadoByFondeoGarantiaAndNotEstatus(int idFondeoGarantia,int notEstatus,Connection con) throws ClientesException{
        ArrayList<SaldoIBSVO> listaSaldos = new ArrayList<SaldoIBSVO>();
        
        PreparedStatement ps = null;
        ResultSet res = null;
        
       
        String query = "SELECT sucursales.su_estado as estado, saldos.* "
                + " FROM d_saldos  as saldos "
                + " INNER JOIN c_sucursales as sucursales"
                + " ON saldos.ib_numSucursal=sucursales.su_numsucursal"
                + " WHERE saldos.ib_fondeo_garantia = ? "
                + " AND saldos.ib_estatus <> ?;";
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeoGarantia);
            ps.setInt(2, notEstatus);
            
            myLogger.debug("Ejecutando: " + ps);
            myLogger.debug("Parametros [" + idFondeoGarantia +","+notEstatus+"]");
            res = ps.executeQuery();
            while (res.next()) {
                listaSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora"), res.getInt("estado")    ));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoByFondeador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoByFondeador", e);
            throw new ClientesException(e.getMessage());
        }/* finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return listaSaldos;
    }
    
    /***
     * Obtiene la suma de todos los saldos elegibles
     * @param idFondeador De donde se obtiene
     * @param idEstatusSaldo Estatus(Vigente=1)
     * @param numDePagosMinimos Dependiendo de lasreglas del fondeador(Para bursa 2 0 3)
     * @param con Objeto de onexion
     * @return
     * @throws ClientesException 
     */
    public double getSaldoAcumuladoElegibleTotal (int idFondeador,Connection con) throws ClientesException{
        double saldoElegibleTotal = 0;
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT sum(saldos.ib_saldo_capital) as saldoElegibleTotal"
                + " FROM d_saldos  as saldos "
                + " INNER JOIN  d_ciclos_grupales AS cg "
                + " ON saldos.ib_numClienteSICAP = cg.ci_numgrupo "
                + "     AND saldos.ib_numSolicitudSICAP = cg.ci_numciclo"
                + " WHERE saldos.ib_fondeador = ?"
                + " AND ib_fondeo_garantia = 0 ";
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            myLogger.debug("Ejecutando: " + ps);
            myLogger.debug("Parametros [" + idFondeador +"]");
            res = ps.executeQuery();
            while (res.next()) {
                saldoElegibleTotal = res.getDouble("saldoElegibleTotal");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoAcumuladoElegibleTotal", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoAcumuladoElegibleTotal", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return saldoElegibleTotal;
    }

    
    
    /*public double getSaldoAcumuladoElegibleTotal (int idFondeador, int idEstatusSaldo, int numDePagosMinimos,int idFondeadorAGarantia,Connection con) throws ClientesException{
        double saldoElegibleTotal = 0;
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT sum(saldos.ib_saldo_capital) as saldoElegibleTotal"
                + " FROM d_saldos  as saldos "
                + " INNER JOIN  d_ciclos_grupales AS cg "
                + " ON saldos.ib_numClienteSICAP = cg.ci_numgrupo "
                + "     AND saldos.ib_numSolicitudSICAP = cg.ci_numciclo"

                + " WHERE saldos.ib_fondeador = ?"
                + " AND saldos.ib_estatus = ? "
                + " AND ib_num_cuotas_trancurridas>=?"
                + " AND ib_fondeo_garantia = 0 "
                + " AND cg.ci_consegurocompleto = 1"
                + " AND ib_tasa_interes_sin_iva  >= (SELECT pf_valor "
                + "                                 FROM c_parametros_fondeadores "
                + "                                 WHERE pf_cve = 'TASA_ELEGIBILIDAD_FONDEADOR' "
                + "                                 AND pf_fondeador=?) ";
        
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, idEstatusSaldo);
            ps.setInt(3, numDePagosMinimos);
            ps.setInt(4, idFondeadorAGarantia);
            myLogger.debug("Ejecutando: " + ps);
            myLogger.debug("Parametros [" + idFondeador +","+idEstatusSaldo+","+numDePagosMinimos+","+idFondeadorAGarantia+"]");
            res = ps.executeQuery();
            while (res.next()) {
                saldoElegibleTotal = res.getDouble("saldoElegibleTotal");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoAcumuladoElegibleTotal", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoAcumuladoElegibleTotal", e);
            throw new ClientesException(e.getMessage());
        //} finally {
            //try {
                //if (con != null) {
                    //con.close();
              //}
            //} catch (SQLException e) {
                //throw new ClientesDBException(e.getMessage());
            //}
        //}
    
        return saldoElegibleTotal;
    }*/
      
    /***
     * Acumulado por estado de los saldos elegibles para ser asignados con las reglas de BURSA
     * @param idFondeador De donde se obtiene
     * @param idEstatusSaldo Estatus(Vigente=1)
     * @param numDePagosMinimos Dependiendo de lasreglas del fondeador(Para bursa 2 0 3)
     * @param con Objeto de onexion
     * @return
     * @throws ClientesException 
     */
    public ArrayList<Integer> getEstadosBySaldosAcumuladosElegibles (int idFondeador, int idEstatusSaldo, int numDePagosMinimos,int idFondeadorAGarantia,Connection con) throws ClientesException{
        ArrayList<Integer> listaEstados = new ArrayList<Integer>();
        
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT sucursales.su_estado as estado, sum(saldos.ib_saldo_capital) as saldoCapitalEstado "
                + " FROM d_saldos  as saldos "
                + " INNER JOIN c_sucursales as sucursales"
                + " ON saldos.ib_numSucursal=sucursales.su_numsucursal"
                + " INNER JOIN  d_ciclos_grupales AS cg "
                + " ON saldos.ib_numClienteSICAP = cg.ci_numgrupo "
                + "     AND saldos.ib_numSolicitudSICAP = cg.ci_numciclo"

                + " WHERE saldos.ib_fondeador = ?"
                + " AND saldos.ib_estatus = ? "
                + " AND ib_num_cuotas_trancurridas>=?"
                + " AND ib_fondeo_garantia = 0 "
                + " AND cg.ci_consegurocompleto = 1"
                + " AND ib_tasa_interes_sin_iva  >= (SELECT pf_valor "
                + "                                 FROM c_parametros_fondeadores "
                + "                                 WHERE pf_cve = 'TASA_ELEGIBILIDAD_FONDEADOR' "
                + "                                 AND pf_fondeador=?) " 
                + " GROUP BY estado "
                + " ORDER BY saldoCapitalEstado DESC;";
        
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, idEstatusSaldo);
            ps.setInt(3, numDePagosMinimos);
            ps.setInt(4, idFondeadorAGarantia);
            myLogger.debug("Ejecutando: " + ps);
            myLogger.debug("Parametros [" + idFondeador +","+idEstatusSaldo+","+numDePagosMinimos+","+idFondeadorAGarantia+"]");
            res = ps.executeQuery();
            
            double saldoTotal = 0;
            while (res.next()) {
                listaEstados.add(res.getInt("estado"));
                myLogger.info("Saldo estado: "+res.getDouble("saldoCapitalEstado"));
                saldoTotal+=res.getDouble("saldoCapitalEstado");
            }
            myLogger.info("Saldo Total estados: "+saldoTotal);
            
        } catch (SQLException sqle) {
            myLogger.error("getEstadosBySaldosAcumuladosElegibles", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEstadosBySaldosAcumuladosElegibles", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return listaEstados;
    }
    
    /***
     * Lista de saldos por estado con las reglas de elegibilidad para BURSA
     * @param idFondeador
     * @param idEstatusSaldo
     * @param numDePagosMinimos
     * @param idEstado
     * @param con
     * @return
     * @throws ClientesException 
     */
    public ArrayList<SaldoIBSVO> getSaldosElegiblesByEstado(int idFondeador, int idEstatusSaldo,int numDePagosMinimos,int idEstado,int idFondeadorAGarantia,Connection con) throws ClientesException{
        ArrayList<SaldoIBSVO> listaSaldos = new ArrayList<SaldoIBSVO>();
        
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT sucursales.su_estado as estado, saldos.* "
                + " FROM d_saldos  as saldos "
                + " INNER JOIN c_sucursales as sucursales"
                + " ON saldos.ib_numSucursal=sucursales.su_numsucursal "
                + " INNER JOIN  d_ciclos_grupales AS cg\n" 
                + " ON saldos.ib_numClienteSICAP = cg.ci_numgrupo "
                + "     AND saldos.ib_numSolicitudSICAP = cg.ci_numciclo"
                + " WHERE saldos.ib_fondeador = ?"
                + " AND saldos.ib_estatus = ? "
                + " AND ib_num_cuotas_trancurridas>=?"
                + " AND sucursales.su_estado = ?"
                + " AND ib_fondeo_garantia = 0 "
                + " AND cg.ci_consegurocompleto = 1 "
                + " AND ib_tasa_interes_sin_iva  >= (SELECT pf_valor "
                + "                                 FROM c_parametros_fondeadores "
                + "                                 WHERE pf_cve = 'TASA_ELEGIBILIDAD_FONDEADOR' "
                + "                                 AND pf_fondeador=?) " 
                + " ORDER BY saldos.ib_fecha_desembolso DESC ;";
        
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, idEstatusSaldo);
            ps.setInt(3, numDePagosMinimos);
            ps.setInt(4, idEstado);
            ps.setInt(5, idFondeadorAGarantia);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeador +","+idEstatusSaldo+","+numDePagosMinimos+","+idEstado+","+ idFondeadorAGarantia+"]");
            
            res = ps.executeQuery();
            while (res.next()) {
                listaSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora"),res.getInt("estado")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosElegiblesByEstado", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosElegiblesByEstado", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return listaSaldos;
    }
    
    public ArrayList<SaldoIBSVO> getSaldosElegiblesByDiasVigencia(int idFondeador, int idEstatusSaldo,int numDiasVigencia,int numPagosMinimos,Connection con) throws ClientesException{
        ArrayList<SaldoIBSVO> listaSaldos = new ArrayList<SaldoIBSVO>();
        
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT * FROM d_saldos "
                + "WHERE ib_fondeador = ?  "
                + "AND ib_estatus = ?  "
                + "AND ib_num_cuotas_trancurridas>=? "
                + "AND ib_fondeo_garantia = 0  "
                + "AND datediff(ib_fecha_vencimiento,ib_fecha_generacion) > ? "
                + "ORDER BY ib_fecha_desembolso DESC;";
      
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, idEstatusSaldo);
            ps.setInt(3, numPagosMinimos);
            ps.setInt(4, numDiasVigencia);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeador +","+idEstatusSaldo+","+numDiasVigencia+","+numPagosMinimos+"]");
            
            res = ps.executeQuery();
            while (res.next()) {
                listaSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosElegiblesByDiasVigencia", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosElegiblesByDiasVigencia", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return listaSaldos;
    }
    
    public ArrayList<SaldoIBSVO> getSaldosElegiblesByEstatusSaldoAndFondeador(int idFondeador, int idEstatusSaldo,int numPagosMinimo,Connection con) throws ClientesException{
        ArrayList<SaldoIBSVO> listaSaldos = new ArrayList<SaldoIBSVO>();
        
        PreparedStatement ps = null;
        ResultSet res = null;
       
        String query = "SELECT * FROM d_saldos "
                + "WHERE ib_fondeador = ?  "
                + "AND ib_estatus = ? "
                + "AND ib_num_cuotas_trancurridas>=? "
                + "AND ib_fondeo_garantia = 0 "
                + "ORDER BY ib_fecha_desembolso DESC;";
      
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, idEstatusSaldo);
            ps.setInt(3, numPagosMinimo);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeador +","+idEstatusSaldo+"]");
            
            res = ps.executeQuery();
            while (res.next()) {
                listaSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosElegiblesByEstatusSaldoAndFondeador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosElegiblesByEstatusSaldoAndFondeador", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return listaSaldos;
    }
    
    //*******************//*******************//*******************//*******************//*******************//*******************
    
    public ArrayList<SaldoIBSVO> getSaldosByFondeador(int idFondeador) throws ClientesException{
        ArrayList<SaldoIBSVO> listaSaldos = new ArrayList<SaldoIBSVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        
        String query = "SELECT * FROM d_saldos WHERE ib_fondeador = ?;";
        
        
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeador +"]");
            res = ps.executeQuery();
            while (res.next()) {
                listaSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getInt("ib_numsucursal"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_num_cuotas"), res.getInt("ib_num_cuotas_trancurridas"), res.getInt("ib_plazo"), res.getInt("ib_periodicidad"), res.getDate("ib_fecha_desembolso"),
                        res.getDate("ib_fecha_vencimiento"), res.getDouble("ib_monto_credito"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_saldo_interes_mora"), res.getDouble("ib_saldo_iva_mora"), res.getDouble("ib_saldo_multa"),
                        res.getDouble("ib_saldo_iva_multa"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"), res.getDouble("ib_moratorio_pagado"),
                        res.getDouble("ib_iva_moratorio_pagado"), res.getDouble("ib_multa_pagada"), res.getDouble("ib_iva_multa_pagada"), res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"),
                        res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"), res.getDouble("ib_tasa_interes_sin_iva"), res.getDouble("ib_tasa_mora_sin_iva"), res.getDouble("ib_tasa_iva"), res.getDouble("ib_saldo_con_intereses_al_final"),
                        res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"), res.getInt("ib_estatus"),
                        res.getInt("ib_producto"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"),
                        res.getDouble("ib_moto_total_pagado"), res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getInt("ib_max_cuotas_vencidas"), res.getInt("ib_max_dias_mora")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosByFondeador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosByFondeador", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    
        return listaSaldos;
    }
        
    
    public void updateFondeadorSaldo(int idFondeador,int numClienteSicap, int numSolicitudSicap) 
            throws ClientesException{
        
        Connection con = null;
        PreparedStatement ps = null;
        int res = 0;
        
        String query = "UPDATE d_saldos "
                + "SET ib_fondeador=? "
                + "WHERE ib_numClienteSICAP=? "
                + "AND ib_numSolicitudSICAP=?;";
        
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            ps.setInt(2, numClienteSicap);
            ps.setInt(3, numSolicitudSicap);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idFondeador +","+numClienteSicap+","+numSolicitudSicap+"]");
            res = ps.executeUpdate();
            
        } catch (SQLException sqle) {
            myLogger.error("updateFondeadorSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateFondeadorSaldo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    
    }
    
    
     public void updateFondeoGarantia(SaldoIBSVO saldo) 
            throws ClientesException, SQLException{
         Connection con = null; 
        try {
            con = getConnection();
            //con.setAutoCommit(false);
            updateFondeoGarantia(saldo, con);
            //con.commit();
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }/*finally{
            try{
            
            if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
         
     }
     
    public void updateFondeoGarantia(SaldoIBSVO saldo,Connection con) 
            throws ClientesException, SQLException{
        
        
        PreparedStatement ps = null;
                
        String query = "UPDATE d_saldos "
                + "SET ib_fondeo_garantia=? ,"
                + "ib_fecha_asig_garantia = now()"
                + "WHERE ib_numClienteSICAP=? "
                + "AND ib_numSolicitudSICAP=?;";
        
        try {
            
            if(con==null){
                con = getConnection();
            }
            
            
            //con.setAutoCommit(false);
            ps = con.prepareStatement(query);
                
            ps.setInt(1, saldo.getFondeoGarantia());
            ps.setInt(2, saldo.getIdClienteSICAP());
            ps.setInt(3, saldo.getIdSolicitudSICAP());

            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + saldo.getFondeoGarantia() +","+saldo.getIdClienteSICAP()+","+saldo.getIdSolicitudSICAP()+"]");
            ps.executeUpdate();

            
            //con.commit();
            
        } catch (SQLException sqle) {
                myLogger.error("updateFondeadorSaldo", sqle);
            //con.rollback();
            throw  sqle;
           
            
        } catch (Exception e) {
            myLogger.error("updateFondeadorSaldo", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
    }
    
    public void updateFondeoGarantia(List <SaldoIBSVO> saldos,Connection con,String FechaCierre) 
            throws ClientesException, SQLException{
        
        
        PreparedStatement ps = null;
                
        String query = "UPDATE d_saldos "
                + "SET ib_fondeo_garantia=? ,"
                + "ib_fecha_asig_garantia = ?"
                + "WHERE ib_numClienteSICAP=? "
                + "AND ib_numSolicitudSICAP=?;";
        
        try {
            
            if(con==null){
                con = getConnection();
            }
            
            
            //con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            
            for(SaldoIBSVO saldo:saldos){
                
                ps.setInt(1, saldo.getFondeoGarantia());
                ps.setString(2, FechaCierre);
                ps.setInt(3, saldo.getIdClienteSICAP());
                ps.setInt(4, saldo.getIdSolicitudSICAP());

                myLogger.debug("Ejecutando: " + query);
                myLogger.debug("Parametros [" + saldo.getFondeoGarantia() +","+saldo.getIdClienteSICAP()+","+saldo.getIdSolicitudSICAP()+"]");
                ps.executeUpdate();
                
            }
            //con.commit();
            
            
            
        } catch (SQLException sqle) {
                myLogger.error("updateFondeadorSaldo", sqle);
            //con.rollback();
            throw  sqle;
           
            
        } catch (Exception e) {
            myLogger.error("updateFondeadorSaldo", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
    }
    
    
    public Integer getIdEstadoSaldo (SaldoIBSVO saldo) throws ClientesException{
        int idEstado = 0;
        try {
            Connection con = getConnection();
            idEstado=getIdEstadoSaldo(saldo, con);
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idEstado;
    }
    
    public Integer getIdEstadoSaldo (SaldoIBSVO saldo,Connection con) throws ClientesException{
        int idEstado = 0;
        
        PreparedStatement ps = null;
        ResultSet res = null;
   
        String query = "SELECT su_estado as estado "
                + " FROM d_saldos  as saldos "
                + " INNER JOIN c_sucursales as sucursales"
                + " ON saldos.ib_numSucursal=sucursales.su_numsucursal"
                + " WHERE saldos.ib_numClienteSICAP = ?"
                + " AND saldos.ib_numSolicitudSICAP = ? ;";
        
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getIdSolicitudSICAP());
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() +","+saldo.getIdSolicitudSICAP()+"]");
            res = ps.executeQuery();
            while (res.next()) {
                idEstado=res.getInt("estado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getIdEstadoSaldo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIdEstadoSaldo", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
        return Integer.valueOf(idEstado);
    }
    
    public Double getSumSaldosByEstadoAndFondeador (int idEstado, int idFondeador ) throws ClientesException{
        
        double sumSaldoEdoFond = 0;
        
        try {
            Connection con = getConnection();
            sumSaldoEdoFond=getSumSaldosByEstadoAndFondeador(idEstado, idFondeador, con);
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return sumSaldoEdoFond;
    }
    
    public Double getSumSaldosByEstadoAndFondeador (int idEstado, int idFondeador ,Connection con) throws ClientesException{
        
        PreparedStatement ps = null;
        ResultSet res = null;
        double sumSaldoEdoFond = 0;
   
        String query = "SELECT sum(saldos.ib_saldo_capital) as saldoCapitalEstado " +
                        " FROM d_saldos  as saldos " +
                        " INNER JOIN c_sucursales as sucursales\n" +
                        " ON saldos.ib_numSucursal=sucursales.su_numsucursal" +
                        " INNER JOIN  d_ciclos_grupales AS cg " +
                        " ON saldos.ib_numClienteSICAP = cg.ci_numgrupo " +
                        " AND saldos.ib_numSolicitudSICAP = cg.ci_numciclo\n" +
                        " WHERE sucursales.su_estado = ? "+ 
                        " AND saldos.ib_fondeo_garantia = ?" +
                        " GROUP BY sucursales.su_estado ;";
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idEstado);
            ps.setInt(2, idFondeador);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idEstado +","+idFondeador+"]");
            res = ps.executeQuery();
            while (res.next()) {
                sumSaldoEdoFond=res.getDouble("saldoCapitalEstado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSumSaldosByEstadoAndFondeador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSumSaldosByEstadoAndFondeador", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
        
    
        return Double.valueOf(sumSaldoEdoFond);
    }
    
    public boolean existeCartera(int idFondeador) throws ClientesException{
    
        boolean hayRegistros = false;
        
        try {
            Connection con = getConnection();
            hayRegistros=existeCartera(idFondeador, con);
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hayRegistros;
        
    }
    
    public boolean existeCartera(int idFondeador ,Connection con) throws ClientesException{
        
        PreparedStatement ps = null;
        ResultSet res = null;
        boolean hayRegistros = false;
        
        //El LIMIT 1 es para mejor el rendimiento de la consulta
        String query = "SELECT 1"
                + " FROM d_saldos "
                + " WHERE ib_fondeo_garantia = ? "
                + " LIMIT 1;";
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idFondeador);
            
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros ["+idFondeador+"]");
            res = ps.executeQuery();
            if (res.next()) {
               hayRegistros=true; 
            }
        } catch (SQLException sqle) {
            myLogger.error("existeCartera", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeCartera", e);
            throw new ClientesException(e.getMessage());
        }
        
    
        return hayRegistros;
    }
    
    public boolean tienePreseleccion(SaldoIBSVO saldo,int idFondeador) throws ClientesException{
    
        boolean hayRegistros = false;
        
        try {
            Connection con = getConnection();
            hayRegistros=tienePreseleccion(saldo,idFondeador, con);
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hayRegistros;
        
    }
    
    public boolean tienePreseleccion(SaldoIBSVO saldo, int idFondeador ,Connection con) throws ClientesException{
        
        PreparedStatement ps = null;
        ResultSet res = null;
        boolean hayRegistros = false;
   
        //El LIMIT 1 es para mejor el rendimiento de la consulta
        String query = "SELECT 1"
                + " FROM d_saldos "
                + " WHERE ib_numClienteSICAP = ? "
                + " AND ib_numSolicitudSICAP = ?"
                + " AND ib_fondeo_garantia = ? "
                + " LIMIT 1;";
        
        try {
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getIdSolicitudSICAP());
            ps.setInt(3, -1*idFondeador);
            
            myLogger.debug("Ejecutando: " + ps.toString());
            //myLogger.debug("Parametros ["+idFondeador+"]");
            res = ps.executeQuery();
            if (res.next()) {
               hayRegistros=true; 
            }
        } catch (SQLException sqle) {
            myLogger.error("existeCartera", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeCartera", e);
            throw new ClientesException(e.getMessage());
        }
        
    
        return hayRegistros;
    }
    
    /**
    * Metodo paraactualizar los saldos preseleccionados previamente como rechazado
    * Se actualiza ib_fondeo_garantia a 0 y el campo ib_fondeo_garantia_rechazo igual al fondeador que rechazo
    * @param saldos
    * @param con
    * @throws ClientesException
    * @throws SQLException 
    */
    public void updateSaldosRechazoFondeador(List <SaldoIBSVO> saldos, int idFondeadorRechazo,Connection con) throws ClientesException, SQLException{
        
        PreparedStatement ps = null;
                
        String query = "UPDATE d_saldos "
                + " SET ib_fondeo_garantia = 0 , "
                + " ib_fondeo_garantia_rechazo = ? ,"
                + " ib_fecha_asig_garantia = now() "
                + " WHERE ib_numClienteSICAP = ? "
                + " AND ib_numSolicitudSICAP = ?;";
        
        try {
            
            if(con==null){
                con = getConnection();
            }
            
            
            //con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            
            for(SaldoIBSVO saldo:saldos){
                
                ps.setInt(1, idFondeadorRechazo);
                ps.setInt(2, saldo.getIdClienteSICAP());
                ps.setInt(3, saldo.getIdSolicitudSICAP());

                myLogger.debug("Ejecutando: " + ps.toString());
                
                ps.executeUpdate();
                
            }
            //con.commit();
            
            
            
        } catch (SQLException sqle) {
                myLogger.error("updateFondeadorSaldo", sqle);
            //con.rollback();
            throw  sqle;
           
            
        } catch (Exception e) {
            myLogger.error("updateFondeadorSaldo", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
    
    }
   
    /**
    * Metodo paraactualizar los saldos preseleccionados previamente como rechazado
    * Se actualiza ib_fondeo_garantia a 0 y el campo ib_fondeo_garantia_rechazo igual al fondeador que rechazo
    * @param saldos
    * @param con
    * @throws ClientesException
    * @throws SQLException 
    */
    public void updateSaldosPreseleccionadosAAsigandos(int idFondeador, Connection con) throws ClientesException, SQLException{
        
        PreparedStatement ps = null;
        
        //Pasar de negativo a positivo el valor del fondeador        
        String query = "UPDATE d_saldos "
                + " SET ib_fondeo_garantia = ? , "
                + " ib_fecha_asig_garantia = now() "
                + " WHERE ib_fondeo_garantia = ? ;";
        
        try {
            
            if(con==null){
                con = getConnection();
            }
            
            ps = con.prepareStatement(query);
                        
            ps.setInt(1, idFondeador);
            ps.setInt(2, -1*idFondeador );

            myLogger.debug("Ejecutando: " + ps.toString());

            ps.executeUpdate();
            
            
        } catch (SQLException sqle) {
                myLogger.error("updateFondeadorSaldo", sqle);
            //con.rollback();
            throw  sqle;
           
            
        } catch (Exception e) {
            myLogger.error("updateFondeadorSaldo", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
    
    
    }
   

}

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
//import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoHistoricoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Date;
import java.sql.Savepoint;
import org.apache.log4j.Logger;

public class SaldoHistoricoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(SaldoIBSDAO.class);
    
    public int addSaldoHistorico(SaldoHistoricoVO saldoHistorico) throws ClientesException {


        Connection conn = null;
        int res = 0;

        String query =
                "INSERT INTO d_saldos_hist "
                + "( sh_credito, sh_referencia, sh_numClienteSICAP, sh_numSolicitudSICAP, sh_numClienteIBS, sh_nombreCliente, sh_rfc, sh_numSucursal, sh_nombreSucursal, sh_fecha_envio, "
                + "sh_fecha_generacion, sh_hora_generacion, sh_num_cuotas, sh_num_cuotas_trancurridas, sh_plazo, sh_periodicidad, sh_fecha_desembolso, sh_fecha_vencimiento, sh_monto_credito, sh_saldo_total_dia, "
                + "sh_saldo_capital, sh_saldo_interes, sh_saldo_interes_vigente, sh_saldo_interes_vencido, sh_saldo_interes_vencido_90dias, sh_saldo_interes_cuentas_orden, sh_saldo_iva_interes, sh_saldo_interes_mora, sh_saldo_iva_mora, sh_saldo_multa, "
                + "sh_saldo_iva_multa, sh_capital_pagado, sh_interes_normal_pagado, sh_iva_interes_normal_pagado, sh_moratorio_pagado, sh_iva_moratorio_pagado, sh_multa_pagada, sh_iva_multa_pagada, sh_comision, sh_iva_comision, "
                + "sh_monto_desembolsado, sh_fecha_sig_amortizacion, sh_capital_sig_amortizacion, sh_interes_sig_amortizacion, sh_iva_interes_sig_amortizacion, sh_fondeador, sh_nombre_fondeador, sh_tasa_interes_sin_iva, sh_tasa_mora_sin_iva, sh_tasa_iva, "
                + "sh_saldo_con_intereses_al_final, sh_capital_vencido, sh_interes_vencido, sh_iva_interes_vencido, sh_total_vencido, sh_estatus, sh_producto, sh_fecha_incumplimiento, sh_fecha_a_cartera_vencida, sh_num_dias_mora, "
                + "sh_dias_transcurridos, sh_cuotas_vencidas, sh_num_pagos_realizados, sh_moto_total_pagado, sh_fecha_ultimo_pago, sh_bandera_reestructura, sh_credito_reestructurado, sh_dias_mora_reestructura, sh_tasa_preferencial_iva, sh_monto_seguro, sh_cuenta_bucket, sh_saldo_bucket, "
                + "sh_saldo_bonificacion_iva, sh_bonificacion_pagada, sh_origen, sh_cta_contable, sh_fecha_historico, sh_interes_xdevengar, sh_iva_interes_xdevengar, sh_num_pago_sostenido) "
                + "VALUES "
                + "( ?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?)";

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, saldoHistorico.getCredito());
            ps.setString(2, saldoHistorico.getReferencia());
            ps.setInt(3, saldoHistorico.getIdClienteSICAP());
            ps.setInt(4, saldoHistorico.getIdSolicitudSICAP());
            ps.setInt(5, saldoHistorico.getIdClienteIBS());
            ps.setString(6, saldoHistorico.getNombreCliente());
            ps.setString(7, saldoHistorico.getRfc());
            ps.setInt(8, saldoHistorico.getIdSucursal());
            ps.setString(9, saldoHistorico.getNombreSucursal());
            ps.setDate(10, saldoHistorico.getFechaEnvio());
            ps.setDate(11, saldoHistorico.getFechaGeneracion());
            ps.setDouble(12, saldoHistorico.getHoraGeneracion());
            ps.setInt(13, saldoHistorico.getNumeroCuotas());
            ps.setInt(14, saldoHistorico.getNumeroCuotasTranscurridas());
            ps.setInt(15, saldoHistorico.getPlazo());
            ps.setInt(16, saldoHistorico.getPeriodicidad());
            ps.setDate(17, saldoHistorico.getFechaDesembolso());
            ps.setDate(18, saldoHistorico.getFechaVencimiento());
            ps.setDouble(19, saldoHistorico.getMontoCredito());
            ps.setDouble(20, saldoHistorico.getSaldoTotalAlDia());
            ps.setDouble(21, saldoHistorico.getSaldoCapital());
            ps.setDouble(22, saldoHistorico.getSaldoInteres());
            ps.setDouble(23, saldoHistorico.getSaldoInteresVigente());
            ps.setDouble(24, saldoHistorico.getSaldoInteresVencido());
            ps.setDouble(25, saldoHistorico.getSaldoInteresVencido90dias());
            ps.setDouble(26, saldoHistorico.getSaldoInteresCtasOrden());
            ps.setDouble(27, saldoHistorico.getSaldoIvaInteres());
            ps.setDouble(28, saldoHistorico.getSaldoMora());
            ps.setDouble(29, saldoHistorico.getSaldoIVAMora());
            ps.setDouble(30, saldoHistorico.getSaldoMulta());
            ps.setDouble(31, saldoHistorico.getSaldoIVAMulta());
            ps.setDouble(32, saldoHistorico.getCapitalPagado());
            ps.setDouble(33, saldoHistorico.getInteresNormalPagado());
            ps.setDouble(34, saldoHistorico.getIvaInteresNormalPagado());
            ps.setDouble(35, saldoHistorico.getMoratorioPagado());
            ps.setDouble(36, saldoHistorico.getIvaMoraPagado());
            ps.setDouble(37, saldoHistorico.getMultaPagada());
            ps.setDouble(38, saldoHistorico.getIvaMultaPagado());
            ps.setDouble(39, saldoHistorico.getComision());
            ps.setDouble(40, saldoHistorico.getIvaComision());
            ps.setDouble(41, saldoHistorico.getMontoDesembolsado());
            ps.setDate(42, saldoHistorico.getFechaSigAmortizacion());
            ps.setDouble(43, saldoHistorico.getCapitalSigAmortizacion());
            ps.setDouble(44, saldoHistorico.getInteresSigAmortizacion());
            ps.setDouble(45, saldoHistorico.getIvaSigAmortizacion());
            ps.setInt(46, saldoHistorico.getFondeador());
            ps.setString(47, saldoHistorico.getNombreFondeador());
            ps.setDouble(48, saldoHistorico.getTasaInteresSinIVA());
            ps.setDouble(49, saldoHistorico.getTasaMoraSinIVA());
            ps.setDouble(50, saldoHistorico.getTasaIVA());
            ps.setDouble(51, saldoHistorico.getSaldoConInteresAlFinal());
            ps.setDouble(52, saldoHistorico.getCapitalVencido());
            ps.setDouble(53, saldoHistorico.getInteresVencido());
            ps.setDouble(54, saldoHistorico.getIvaInteresVencido());
            ps.setDouble(55, saldoHistorico.getTotalVencido());
            ps.setInt(56, saldoHistorico.getEstatus());
            ps.setInt(57, saldoHistorico.getIdProducto());
            ps.setDate(58, saldoHistorico.getFechaIncumplimiento());
            ps.setDate(59, saldoHistorico.getFechaAcarteraVencida());
            ps.setInt(60, saldoHistorico.getDiasMora());
            ps.setInt(61, saldoHistorico.getDiasTranscurridos());
            ps.setInt(62, saldoHistorico.getCuotasVencidas());
            ps.setInt(63, saldoHistorico.getNumeroPagosRealizados());
            ps.setDouble(64, saldoHistorico.getMontoTotalPagado());
            ps.setDate(65, saldoHistorico.getFechaUltimoPago());
            ps.setString(66, saldoHistorico.getBanderaReestructura());
            ps.setDouble(67, saldoHistorico.getCreditoReestructurado());
            ps.setInt(68, saldoHistorico.getDiasMoraReestructura());
            ps.setString(69, saldoHistorico.getTasaPreferencialIVA());
            ps.setDouble(70, saldoHistorico.getMontoSeguro());
            ps.setInt(71, saldoHistorico.getCuentaBucket());
            ps.setDouble(72, saldoHistorico.getSaldoBucket());
            ps.setDouble(73, saldoHistorico.getSaldoBonificacionDeIVA());
            ps.setDouble(74, saldoHistorico.getBonificacionPagada());
            ps.setInt(75, saldoHistorico.getOrigen());
            ps.setLong(76, saldoHistorico.getCtaContable());
            ps.setDate(77, saldoHistorico.getFechaHistorico());
            ps.setDouble(78, saldoHistorico.getInteresPorDevengar());
            ps.setDouble(79, saldoHistorico.getIvaInteresPorDevengar());
            ps.setInt(80, saldoHistorico.getNumPagoSostenido());

            res = ps.executeUpdate();
            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("Problema dentro addSaldoHistorico",sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro addSaldoHistorico",e);
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
    
    public int addSaldoHistorico(Date fechaHist) throws ClientesException{
        
        int numInsert = 0;
//        Connection con = null;
//        PreparedStatement pstm = null;
//        String query = "";
//        try {
//            query = "INSERT INTO d_saldos_hist SELECT "
//                    +"ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,ib_fecha_envio,ib_fecha_generacion,ib_hora_generacion,ib_num_cuotas,ib_num_cuotas_trancurridas,"
//                    +"ib_plazo,ib_periodicidad,ib_fecha_desembolso,ib_fecha_vencimiento,ib_monto_credito,ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_interes_vencido_90dias,ib_saldo_interes_cuentas_orden,ib_saldo_iva_interes,ib_saldo_bonificacion_iva,ib_saldo_interes_mora,"
//                    +"ib_saldo_iva_mora,ib_saldo_multa,ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_bonificacion_pagada,ib_moratorio_pagado,ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_comision,ib_iva_comision,ib_monto_seguro,ib_monto_desembolsado,"
//                    +"ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_fondeador,ib_nombre_fondeador,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,ib_capital_vencido,ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,"
//                    +"ib_producto,ib_fecha_incumplimiento,ib_fecha_a_cartera_vencida,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,ib_num_pagos_realizados,ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_bandera_reestructura,ib_credito_reestructurado,ib_dias_mora_reestructura,ib_tasa_preferencial_iva,ib_cuenta_bucket,cr_monto_cuenta,"
//                    +"ib_cta_contable,ib_fecha_generacion,ib_interes_xdevengar,ib_iva_interes_xdevengar,ib_num_pago_sostenido,ib_numauditor,ib_integranteini,ib_integrantecan,ib_addintegrante,cr_monto_cuenta_congelada,ib_interes_devengado,ib_iva_interes_devengado,ib_fondeo_garantia, ib_fecha_asig_garantia "
//                    +"FROM d_saldos INNER JOIN cartera_cec.d_credito ON (ib_numClienteSICAP=cr_num_cliente AND ib_numSolicitudSICAP=cr_num_solicitud) WHERE ib_fecha_generacion=? AND ib_estatus IN (1,2,3,4,6,7);";
//            con = getConnection();
//            pstm = con.prepareStatement(query);
//            pstm.setDate(1, fechaHist);
//            myLogger.debug("Ejecutando = "+pstm);
//            pstm.executeUpdate();
//            numInsert = pstm.getUpdateCount();
//        } catch (SQLException sqle) {
//            myLogger.error("Problema dentro addSaldoHistorico",sqle);
//            throw new ClientesException(sqle.getMessage());
//        } catch (Exception e) {
//            myLogger.error("Problema dentro addSaldoHistorico",e);
//            throw new ClientesException(e.getMessage());
//        } finally {
//            try {
//                if (con != null)
//                    con.close();
//                if (pstm != null)
//                    pstm.close();
//            } catch (SQLException e) {
//                throw new ClientesDBException(e.getMessage());
//            }
//        }
        return numInsert;
    }
        public int addTablaAmortHistorico(Date fechaHist) throws ClientesException{
        
        int numInsert = 0;
//        Connection con = null;
//        PreparedStatement pstm = null;
//        String query = "";
//        try {
//            query = "Insert into d_tabla_amortizacion_hist" +
//                    " Select TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,TA_CAPITAL_PAGADO,TA_COMISION_INICIAL," +
//                    "        TA_IVA_COMISION,TA_INTERES,TA_IVA_INTERES,TA_INTERES_ACUM,TA_IVA_INTERES_ACUM,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,TA_IVA_MORA_PAGADO,TA_MULTA," +
//                    "        TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO,TA_CAPITAL_ANTICIPADO,ta_entiempo,ta_incremento_capital,ta_quita_interes,ta_quita_iva_interes," +
//                    "        ? " +
//                    " from d_tabla_amortizacion, d_credito" +
//                    " where ta_numcliente = cr_num_cliente" +
//                    " and TA_NUMCREDITO = cr_num_credito" +
//                    " and cr_status in (1,2,4)";
//            con = getCWConnection();
//            pstm = con.prepareStatement(query);
//            pstm.setDate(1, fechaHist);
//            myLogger.debug("Ejecutando = "+pstm);
//            pstm.executeUpdate();
//            numInsert = pstm.getUpdateCount();
//        } catch (SQLException sqle) {
//            myLogger.error("Problema dentro addTablaAmortHistorico",sqle);
//            throw new ClientesException(sqle.getMessage());
//        } catch (Exception e) {
//            myLogger.error("Problema dentro addTablaAMortHistorico",e);
//            throw new ClientesException(e.getMessage());
//        } finally {
//            try {
//                if (con != null)
//                    con.close();
//                if (pstm != null)
//                    pstm.close();
//            } catch (SQLException e) {
//                throw new ClientesDBException(e.getMessage());
//            }
//        }
        return numInsert;
    }

    public SaldoHistoricoVO getSaldoHistorico(int numCliente, int numSolicitud) throws ClientesException {

        SaldoHistoricoVO saldoHistorico = new SaldoHistoricoVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos_hist WHERE ib_numClienteSICAP = ? AND ib_numSolicitudSICAP = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            myLogger.debug("Ejecutando = "+ps);
            rs = ps.executeQuery();

            if (rs.next()) {
                saldoHistorico = new SaldoHistoricoVO();

                saldoHistorico.setOrigen(rs.getInt("sh_origen"));
                saldoHistorico.setCredito(rs.getInt("sh_credito"));
                saldoHistorico.setReferencia(rs.getString("sh_referencia"));
                saldoHistorico.setIdClienteSICAP(rs.getInt("sh_numClienteSICAP"));
                saldoHistorico.setIdSolicitudSICAP(rs.getInt("sh_numSolicitudSICAP"));
                saldoHistorico.setIdClienteIBS(rs.getInt("sh_numClienteIBS"));
                saldoHistorico.setNombreCliente(rs.getString("sh_nombreCliente"));
                saldoHistorico.setRfc(rs.getString("sh_rfc"));
                saldoHistorico.setIdSucursal(rs.getInt("sh_numSucursal"));
                saldoHistorico.setNombreSucursal(rs.getString("sh_nombreSucursal"));
                saldoHistorico.setFechaEnvio(rs.getDate("sh_fecha_envio"));
                saldoHistorico.setFechaGeneracion(rs.getDate("sh_fecha_generacion"));
                saldoHistorico.setHoraGeneracion(rs.getInt("sh_hora_generacion"));
                saldoHistorico.setNumeroCuotas(rs.getInt("sh_num_cuotas"));
                saldoHistorico.setNumeroCuotasTranscurridas(rs.getInt("sh_num_cuotas_trancurridas"));
                saldoHistorico.setPlazo(rs.getInt("sh_plazo"));
                saldoHistorico.setPeriodicidad(rs.getInt("sh_periodicidad"));
                saldoHistorico.setFechaDesembolso(rs.getDate("sh_fecha_desembolso"));
                saldoHistorico.setFechaVencimiento(rs.getDate("sh_fecha_vencimiento"));
                saldoHistorico.setMontoCredito(rs.getDouble("sh_monto_credito"));
                saldoHistorico.setSaldoTotalAlDia(rs.getDouble("sh_saldo_total_dia"));
                saldoHistorico.setSaldoCapital(rs.getDouble("sh_saldo_capital"));
                saldoHistorico.setSaldoInteres(rs.getDouble("sh_saldo_interes"));
                saldoHistorico.setSaldoInteresVigente(rs.getDouble("sh_saldo_interes_vigente"));
                saldoHistorico.setSaldoInteresVencido(rs.getDouble("sh_saldo_interes_vencido"));
                saldoHistorico.setSaldoInteresVencido90dias(rs.getDouble("sh_saldo_interes_vencido_90dias"));
                saldoHistorico.setSaldoInteresCtasOrden(rs.getDouble("sh_saldo_interes_cuentas_orden"));
                saldoHistorico.setSaldoIvaInteres(rs.getDouble("sh_saldo_iva_interes"));
                saldoHistorico.setSaldoBonificacionDeIVA(rs.getDouble("sh_saldo_bonificacion_iva"));
                saldoHistorico.setSaldoMora(rs.getDouble("sh_saldo_interes_mora"));
                saldoHistorico.setSaldoIVAMora(rs.getDouble("sh_saldo_iva_mora"));
                saldoHistorico.setSaldoMulta(rs.getDouble("sh_saldo_multa"));
                saldoHistorico.setSaldoIVAMulta(rs.getDouble("sh_saldo_iva_multa"));
                saldoHistorico.setCapitalPagado(rs.getDouble("sh_capital_pagado"));
                saldoHistorico.setInteresNormalPagado(rs.getDouble("sh_interes_normal_pagado"));
                saldoHistorico.setIvaInteresNormalPagado(rs.getDouble("sh_iva_interes_normal_pagado"));
                saldoHistorico.setBonificacionPagada(rs.getDouble("sh_bonificacion_pagada"));
                saldoHistorico.setMoratorioPagado(rs.getDouble("sh_moratorio_pagado"));
                saldoHistorico.setIvaMoraPagado(rs.getDouble("sh_iva_moratorio_pagado"));
                saldoHistorico.setMultaPagada(rs.getDouble("sh_multa_pagada"));
                saldoHistorico.setIvaMultaPagado(rs.getDouble("sh_iva_multa_pagada"));
                saldoHistorico.setComision(rs.getDouble("sh_comision"));
                saldoHistorico.setIvaComision(rs.getDouble("sh_iva_comision"));
                saldoHistorico.setMontoSeguro(rs.getDouble("sh_monto_seguro"));
                saldoHistorico.setMontoDesembolsado(rs.getDouble("sh_monto_desembolsado"));
                saldoHistorico.setFechaSigAmortizacion(rs.getDate("sh_fecha_sig_amortizacion"));
                saldoHistorico.setCapitalSigAmortizacion(rs.getDouble("sh_capital_sig_amortizacion"));
                saldoHistorico.setInteresSigAmortizacion(rs.getDouble("sh_interes_sig_amortizacion"));
                saldoHistorico.setIvaSigAmortizacion(rs.getDouble("sh_iva_interes_sig_amortizacion"));
                saldoHistorico.setFondeador(rs.getInt("sh_fondeador"));
                saldoHistorico.setNombreFondeador(rs.getString("sh_nombre_fondeador"));
                saldoHistorico.setTasaInteresSinIVA(rs.getDouble("sh_tasa_interes_sin_iva"));
                saldoHistorico.setTasaMoraSinIVA(rs.getDouble("sh_tasa_mora_sin_iva"));
                saldoHistorico.setTasaIVA(rs.getDouble("sh_tasa_iva"));
                saldoHistorico.setSaldoConInteresAlFinal(rs.getDouble("sh_saldo_con_intereses_al_final"));
                saldoHistorico.setCapitalVencido(rs.getDouble("sh_capital_vencido"));
                saldoHistorico.setInteresVencido(rs.getDouble("sh_interes_vencido"));
                saldoHistorico.setIvaInteresVencido(rs.getDouble("sh_iva_interes_vencido"));
                saldoHistorico.setTotalVencido(rs.getDouble("sh_total_vencido"));
                saldoHistorico.setEstatus(rs.getInt("sh_estatus"));
                saldoHistorico.setIdProducto(rs.getInt("sh_producto"));
                saldoHistorico.setFechaIncumplimiento(rs.getDate("sh_fecha_incumplimiento"));
                saldoHistorico.setFechaAcarteraVencida(rs.getDate("sh_fecha_a_cartera_vencida"));
                saldoHistorico.setDiasMora(rs.getInt("sh_num_dias_mora"));
                saldoHistorico.setDiasTranscurridos(rs.getInt("sh_dias_transcurridos"));
                saldoHistorico.setCuotasVencidas(rs.getInt("sh_cuotas_vencidas"));
                saldoHistorico.setNumeroPagosRealizados(rs.getInt("sh_num_pagos_realizados"));
                saldoHistorico.setMontoTotalPagado(rs.getDouble("sh_moto_total_pagado"));
                saldoHistorico.setFechaUltimoPago(rs.getDate("sh_fecha_ultimo_pago"));
                saldoHistorico.setBanderaReestructura(rs.getString("sh_bandera_reestructura"));
                saldoHistorico.setCreditoReestructurado(rs.getDouble("sh_credito_reestructurado"));
                saldoHistorico.setDiasMoraReestructura(rs.getInt("sh_dias_mora_reestructura"));
                saldoHistorico.setTasaPreferencialIVA(rs.getString("sh_tasa_preferencial_iva"));
                saldoHistorico.setCuentaBucket(rs.getInt("sh_cuenta_bucket"));
                saldoHistorico.setSaldoBucket(rs.getDouble("sh_saldo_bucket"));
                saldoHistorico.setFechaHistorico(rs.getDate("sh_fecha_historico"));
                saldoHistorico.setNumPagoSostenido(rs.getInt("sh_num_pago_sostenido"));

            }
        } catch (SQLException sqle) {
            myLogger.error("Problema dentro getSaldoHistorico",sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro getSaldoHistorico",e);
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

        return saldoHistorico;
    }


    public SaldoHistoricoVO[] getSaldoHistoricoCalificacion(Date fechaHist) throws ClientesException {
        Connection conn = null;
        ResultSet rs = null;
        List<SaldoHistoricoVO> lista = new ArrayList<SaldoHistoricoVO>();
        SaldoHistoricoVO saldosHistoricos[] = null;

        SaldoHistoricoVO saldoHistorico = null;
        try {
            conn = getConnection();
            String query = "SELECT * FROM d_saldos_hist WHERE sh_fecha_historico = ? AND sh_estatus in (1,2,4)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, fechaHist);
            myLogger.debug("Ejecutando = "+ps);
            rs = ps.executeQuery();

            if (rs.next()) {
                saldoHistorico = new SaldoHistoricoVO();

                saldoHistorico.setOrigen(rs.getInt("sh_origen"));
                saldoHistorico.setCredito(rs.getInt("sh_credito"));
                saldoHistorico.setReferencia(rs.getString("sh_referencia"));
                saldoHistorico.setIdClienteSICAP(rs.getInt("sh_numClienteSICAP"));
                saldoHistorico.setIdSolicitudSICAP(rs.getInt("sh_numSolicitudSICAP"));
                saldoHistorico.setIdClienteIBS(rs.getInt("sh_numClienteIBS"));
                saldoHistorico.setNombreCliente(rs.getString("sh_nombreCliente"));
                saldoHistorico.setRfc(rs.getString("sh_rfc"));
                saldoHistorico.setIdSucursal(rs.getInt("sh_numSucursal"));
                saldoHistorico.setNombreSucursal(rs.getString("sh_nombreSucursal"));
                saldoHistorico.setFechaEnvio(rs.getDate("sh_fecha_envio"));
                saldoHistorico.setFechaGeneracion(rs.getDate("sh_fecha_generacion"));
                saldoHistorico.setHoraGeneracion(rs.getInt("sh_hora_generacion"));
                saldoHistorico.setNumeroCuotas(rs.getInt("sh_num_cuotas"));
                saldoHistorico.setNumeroCuotasTranscurridas(rs.getInt("sh_num_cuotas_trancurridas"));
                saldoHistorico.setPlazo(rs.getInt("sh_plazo"));
                saldoHistorico.setPeriodicidad(rs.getInt("sh_periodicidad"));
                saldoHistorico.setFechaDesembolso(rs.getDate("sh_fecha_desembolso"));
                saldoHistorico.setFechaVencimiento(rs.getDate("sh_fecha_vencimiento"));
                saldoHistorico.setMontoCredito(rs.getDouble("sh_monto_credito"));
                saldoHistorico.setSaldoTotalAlDia(rs.getDouble("sh_saldo_total_dia"));
                saldoHistorico.setSaldoCapital(rs.getDouble("sh_saldo_capital"));
                saldoHistorico.setSaldoInteres(rs.getDouble("sh_saldo_interes"));
                saldoHistorico.setSaldoInteresVigente(rs.getDouble("sh_saldo_interes_vigente"));
                saldoHistorico.setSaldoInteresVencido(rs.getDouble("sh_saldo_interes_vencido"));
                saldoHistorico.setSaldoInteresVencido90dias(rs.getDouble("sh_saldo_interes_vencido_90dias"));
                saldoHistorico.setSaldoInteresCtasOrden(rs.getDouble("sh_saldo_interes_cuentas_orden"));
                saldoHistorico.setSaldoIvaInteres(rs.getDouble("sh_saldo_iva_interes"));
                saldoHistorico.setSaldoBonificacionDeIVA(rs.getDouble("sh_saldo_bonificacion_iva"));
                saldoHistorico.setSaldoMora(rs.getDouble("sh_saldo_interes_mora"));
                saldoHistorico.setSaldoIVAMora(rs.getDouble("sh_saldo_iva_mora"));
                saldoHistorico.setSaldoMulta(rs.getDouble("sh_saldo_multa"));
                saldoHistorico.setSaldoIVAMulta(rs.getDouble("sh_saldo_iva_multa"));
                saldoHistorico.setCapitalPagado(rs.getDouble("sh_capital_pagado"));
                saldoHistorico.setInteresNormalPagado(rs.getDouble("sh_interes_normal_pagado"));
                saldoHistorico.setIvaInteresNormalPagado(rs.getDouble("sh_iva_interes_normal_pagado"));
                saldoHistorico.setBonificacionPagada(rs.getDouble("sh_bonificacion_pagada"));
                saldoHistorico.setMoratorioPagado(rs.getDouble("sh_moratorio_pagado"));
                saldoHistorico.setIvaMoraPagado(rs.getDouble("sh_iva_moratorio_pagado"));
                saldoHistorico.setMultaPagada(rs.getDouble("sh_multa_pagada"));
                saldoHistorico.setIvaMultaPagado(rs.getDouble("sh_iva_multa_pagada"));
                saldoHistorico.setComision(rs.getDouble("sh_comision"));
                saldoHistorico.setIvaComision(rs.getDouble("sh_iva_comision"));
                saldoHistorico.setMontoSeguro(rs.getDouble("sh_monto_seguro"));
                saldoHistorico.setMontoDesembolsado(rs.getDouble("sh_monto_desembolsado"));
                saldoHistorico.setFechaSigAmortizacion(rs.getDate("sh_fecha_sig_amortizacion"));
                saldoHistorico.setCapitalSigAmortizacion(rs.getDouble("sh_capital_sig_amortizacion"));
                saldoHistorico.setInteresSigAmortizacion(rs.getDouble("sh_interes_sig_amortizacion"));
                saldoHistorico.setIvaSigAmortizacion(rs.getDouble("sh_iva_interes_sig_amortizacion"));
                saldoHistorico.setFondeador(rs.getInt("sh_fondeador"));
                saldoHistorico.setNombreFondeador(rs.getString("sh_nombre_fondeador"));
                saldoHistorico.setTasaInteresSinIVA(rs.getDouble("sh_tasa_interes_sin_iva"));
                saldoHistorico.setTasaMoraSinIVA(rs.getDouble("sh_tasa_mora_sin_iva"));
                saldoHistorico.setTasaIVA(rs.getDouble("sh_tasa_iva"));
                saldoHistorico.setSaldoConInteresAlFinal(rs.getDouble("sh_saldo_con_intereses_al_final"));
                saldoHistorico.setCapitalVencido(rs.getDouble("sh_capital_vencido"));
                saldoHistorico.setInteresVencido(rs.getDouble("sh_interes_vencido"));
                saldoHistorico.setIvaInteresVencido(rs.getDouble("sh_iva_interes_vencido"));
                saldoHistorico.setTotalVencido(rs.getDouble("sh_total_vencido"));
                saldoHistorico.setEstatus(rs.getInt("sh_estatus"));
                saldoHistorico.setIdProducto(rs.getInt("sh_producto"));
                saldoHistorico.setFechaIncumplimiento(rs.getDate("sh_fecha_incumplimiento"));
                saldoHistorico.setFechaAcarteraVencida(rs.getDate("sh_fecha_a_cartera_vencida"));
                saldoHistorico.setDiasMora(rs.getInt("sh_num_dias_mora"));
                saldoHistorico.setDiasTranscurridos(rs.getInt("sh_dias_transcurridos"));
                saldoHistorico.setCuotasVencidas(rs.getInt("sh_cuotas_vencidas"));
                saldoHistorico.setNumeroPagosRealizados(rs.getInt("sh_num_pagos_realizados"));
                saldoHistorico.setMontoTotalPagado(rs.getDouble("sh_moto_total_pagado"));
                saldoHistorico.setFechaUltimoPago(rs.getDate("sh_fecha_ultimo_pago"));
                saldoHistorico.setBanderaReestructura(rs.getString("sh_bandera_reestructura"));
                saldoHistorico.setCreditoReestructurado(rs.getDouble("sh_credito_reestructurado"));
                saldoHistorico.setDiasMoraReestructura(rs.getInt("sh_dias_mora_reestructura"));
                saldoHistorico.setTasaPreferencialIVA(rs.getString("sh_tasa_preferencial_iva"));
                saldoHistorico.setCuentaBucket(rs.getInt("sh_cuenta_bucket"));
                saldoHistorico.setSaldoBucket(rs.getDouble("sh_saldo_bucket"));
                saldoHistorico.setFechaHistorico(rs.getDate("sh_fecha_historico"));
                saldoHistorico.setNumPagoSostenido(rs.getInt("sh_num_pago_sostenido"));

                lista.add(saldoHistorico);
                
            }
            saldosHistoricos = new SaldoHistoricoVO[lista.size()];
                for (int i = 0; i < saldosHistoricos.length; i++) {
                saldosHistoricos[i] = (SaldoHistoricoVO) lista.get(i);
            }

        } catch (SQLException sqle) {
            myLogger.error("Problema dentro getSaldoHistoricoCalificacion",sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro getSaldoHistoricoCalificacion",e);
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

        return saldosHistoricos;
    }
    
    public boolean getHistorico(Date fechaHist) throws ClientesException {

        boolean historico = false;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String query = "SELECT sh_fecha_historico FROM d_saldos_hist WHERE sh_fecha_historico=? LIMIT 1";
            ps = conn.prepareStatement(query);
            ps.setDate(1, fechaHist);
            myLogger.debug("Ejecutando = "+ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                historico = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("Problema dentro getHistorico",sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro getHistorico",e);
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
        return historico;
    }
    
    public void eliminaHistoricosCiclo (int idEquipo, int idCredito) throws ClientesDBException, ClientesException {
        String query = "DELETE FROM d_saldos_hist WHERE sh_numclientesicap=? AND sh_credito=?";
        Connection con = null;
        PreparedStatement ps = null;
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setInt(1, idEquipo);
                ps.setInt(2, idCredito);
                myLogger.debug("Ejecutando: "+ps.toString());
                ps.executeUpdate();
            } catch(SQLException sqle){
                myLogger.error("SQLException", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e){
                myLogger.error("Exception", e);
                throw new ClientesException(e.getMessage());
            } finally {
                try {
                    if(con!=null){
                        con.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                } catch(SQLException sqle){
                    myLogger.error("SQLException", sqle);
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
        }
    
    public boolean setEjecutivoHistorico(Date fechaHist,CicloGrupalVO grupo, int idEjecutivo, Connection con) throws ClientesException, SQLException {

        boolean listo = true;
        PreparedStatement ps = null;
        try {
            String query = "UPDATE d_saldos_hist SET sh_cta_contable=? WHERE sh_numclientesicap=? AND sh_numsolicitudsicap=? AND sh_fecha_historico>=?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, idEjecutivo);
            ps.setInt(2, grupo.idGrupo);
            ps.setInt(3, grupo.idCiclo);
            ps.setDate(4, fechaHist);
            myLogger.debug("Ejecutando = "+ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Problema dentro setEjecutivoHistorico",e);
            con.rollback();
            return listo = false;
        }
        return listo;
    }
    
    public SaldoHistoricoVO getNumPagosHistorico(Connection con, TablaAmortVO tabla) throws ClientesException {

        SaldoHistoricoVO saldoHistVO = new SaldoHistoricoVO();
        String query = "SELECT sh_num_pagos_realizados,sh_fecha_historico FROM d_saldos_hist WHERE sh_numclientesicap=? AND sh_credito=? AND sh_fecha_historico=ADDDATE(?, INTERVAL -1 DAY);";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tabla.getNumCliente());
            ps.setInt(2, tabla.getNumCredito());
            ps.setDate(3, tabla.getFechaPago());
            myLogger.debug("Ejecutando = "+ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                saldoHistVO.setNumeroPagosRealizados(rs.getInt("sh_num_pagos_realizados"));
                saldoHistVO.setFechaHistorico(rs.getDate("sh_fecha_historico"));
            }
        } catch (SQLException sqle) {
            myLogger.error("Problema dentro getNumPagosHistorico",sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Problema dentro getNumPagosHistorico",e);
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

        return saldoHistVO;
    }
    
}
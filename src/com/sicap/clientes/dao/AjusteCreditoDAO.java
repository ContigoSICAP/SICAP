/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex
 */
public class AjusteCreditoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(AjusteCreditoDAO.class);
    private Connection con = null;
    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet res = null;
    private String query = "";

    public ArrayList<SaldoIBSVO> getSaldos(int estatus, int numGrupo, int numSolicitud) throws ClientesException {

        ArrayList<SaldoIBSVO> arrSaldos = new ArrayList<SaldoIBSVO>();
        try {
            con = getConnection();
            query = "SELECT ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_fecha_generacion,ib_plazo,ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes, "
                    + "ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_iva_interes,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_fecha_sig_amortizacion, "
                    + "ib_capital_sig_amortizacion,ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_saldo_con_intereses_al_final,ib_capital_vencido,ib_interes_vencido, "
                    + "ib_iva_interes_vencido,ib_total_vencido,ib_estatus,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,ib_num_pagos_realizados,ib_moto_total_pagado, "
                    + "IFNULL(ib_fecha_ultimo_pago,ib_fecha_desembolso) AS ib_fecha_ultimo_pago,ib_saldo_bucket,ib_fecha_desembolso,ib_num_cuotas_trancurridas,ib_monto_desembolsado,ib_numsucursal FROM d_saldos ";
            if (estatus == 0) {
                query += "WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ?";
            } else {
                query += "WHERE ib_estatus= ?";
            }
            ps = con.prepareStatement(query);
            if (estatus == 0) {
                ps.setInt(1, numGrupo);
                ps.setInt(2, numSolicitud);
            } else {
                ps.setInt(1, estatus);
            }
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + estatus + ", " + numGrupo + ", " + numSolicitud + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrSaldos.add(new SaldoIBSVO(res.getInt("ib_credito"), res.getString("ib_referencia"), res.getInt("ib_numClienteSICAP"), res.getInt("ib_numSolicitudSICAP"), res.getDate("ib_fecha_generacion"),
                        res.getInt("ib_plazo"), res.getDouble("ib_saldo_total_dia"), res.getDouble("ib_saldo_capital"), res.getDouble("ib_saldo_interes"), res.getDouble("ib_saldo_interes_vigente"),
                        res.getDouble("ib_saldo_interes_vencido"), res.getDouble("ib_saldo_iva_interes"), res.getDouble("ib_capital_pagado"), res.getDouble("ib_interes_normal_pagado"), res.getDouble("ib_iva_interes_normal_pagado"),
                        res.getDate("ib_fecha_sig_amortizacion"), res.getDouble("ib_capital_sig_amortizacion"), res.getDouble("ib_interes_sig_amortizacion"), res.getDouble("ib_iva_interes_sig_amortizacion"),
                        res.getDouble("ib_saldo_con_intereses_al_final"), res.getDouble("ib_capital_vencido"), res.getDouble("ib_interes_vencido"), res.getDouble("ib_iva_interes_vencido"), res.getDouble("ib_total_vencido"),
                        res.getInt("ib_estatus"), res.getInt("ib_num_dias_mora"), res.getInt("ib_dias_transcurridos"), res.getInt("ib_cuotas_vencidas"), res.getInt("ib_num_pagos_realizados"), res.getDouble("ib_moto_total_pagado"),
                        res.getDate("ib_fecha_ultimo_pago"), res.getDouble("ib_saldo_bucket"), res.getDate("ib_fecha_desembolso"), res.getInt("ib_num_cuotas_trancurridas"), res.getDouble("ib_monto_desembolsado"), res.getInt("ib_numsucursal")));
            }
        } catch (SQLException exc) {
            myLogger.error("getSaldos", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("getSaldos", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrSaldos;
    }

    public Date getUltimoPago(String referencia) throws ClientesException {
        Date fecha = new Date();
        int param = 1;
        query = "SELECT MAX(PC_FECHA_PAGO) AS FECHA FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [ " + referencia + "]");
            res = ps.executeQuery();
            while (res.next()) {
                fecha = res.getDate("FECHA");
            }
        } catch (SQLException exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return fecha;
    }

    public ArrayList<TablaAmortVO> saldosTabla(SaldoIBSVO saldo) throws ClientesException {

        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        query = "SELECT ta_no_pago,ta_fecha_pago,ta_abono_capital,ta_interes,ta_iva_interes,ta_monto_pagar FROM d_tabla_amortizacion WHERE ta_id_cliente= ? AND ta_id_solicitud= ? AND ta_no_pago!=0";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getIdSolicitudSICAP());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getIdSolicitudSICAP() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrTabla.add(new TablaAmortVO(res.getInt("ta_no_pago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"), 0, res.getDouble("ta_interes"), res.getDouble("ta_iva_interes"),
                        0, 0, 0, 0, 0, 0, res.getDouble("ta_monto_pagar"), 0, 0, "N", 0));
            }
        } catch (SQLException sqle) {
            myLogger.error("saldosTabla", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("saldosTabla", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arrTabla;
    }

    public ArrayList<PagoVO> getPagosCredito(SaldoIBSVO saldo) {

        ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
        query = "SELECT pc_monto,pc_fecha_pago,pc_banco_referencia FROM d_pagos_cartera WHERE pc_referencia = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, saldo.getReferencia());
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + saldo.getReferencia() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrPagos.add(new PagoVO(res.getDouble("pc_monto"), res.getDate("pc_fecha_pago"), 0, res.getInt("pc_banco_referencia")));
            }
        } catch (SQLException exc) {
            myLogger.error("getPagosCredito", exc);
        } catch (Exception exc) {
            myLogger.error("getPagosCredito", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagosCredito", exc);
            }
        }
        return arrPagos;
    }

    public void actualizaPagoCredito(PagoVO pago, SaldoIBSVO saldo) {

        query = "UPDATE d_pagos_cartera SET pc_enviado=1 WHERE pc_referencia = ? AND pc_monto = ? AND pc_fecha_pago = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, saldo.getReferencia());
            ps.setDouble(2, pago.getMonto());
            ps.setDate(3, pago.getFechaPago());
            //myLogger.debug("Ejecutando : " + query);
            //myLogger.debug("Parametros [" + saldo.getReferencia()+", "+pago.getMonto()+", "+pago.getFechaPago()+"]");
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("actualizaPagoCredito", exc);
        } catch (Exception exc) {
            myLogger.error("actualizaPagoCredito", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("actualizaPagoCredito", exc);
            }
        }
    }

    public void actualizaTablaAmort(TablaAmortVO tabla, SaldoIBSVO saldo) {

        query = "UPDATE d_tabla_amortizacion SET ta_capital_pagado = ?,ta_interes_pagado = ?,ta_iva_interes_pagado = ?,ta_multa = ?,ta_iva_multa = ?,ta_multa_pagado =?,"
                + "ta_iva_multa_pagado = ?,ta_monto_pagar = ?,ta_monto_pagado = ?,ta_status = ?,ta_pagado = ? "
                + "WHERE ta_numcliente = ? AND ta_numcredito = ? AND TA_NUMPAGO = ?";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, tabla.getCapitalPagado());
            ps.setDouble(2, tabla.getInteresPagado());
            ps.setDouble(3, tabla.getIvaInteresPagado());
            ps.setDouble(4, tabla.getMulta());
            ps.setDouble(5, tabla.getIvaMulta());
            ps.setDouble(6, tabla.getMultaPagado());
            ps.setDouble(7, tabla.getIvaMultaPagado());
            ps.setDouble(8, tabla.getMontoPagar());
            ps.setDouble(9, tabla.getTotalPagado());
            ps.setDouble(10, tabla.getStatus());
            ps.setString(11, tabla.getPagado());
            ps.setInt(12, saldo.getIdClienteSICAP());
            ps.setInt(13, saldo.getCredito());
            ps.setInt(14, tabla.getNumPago());
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + ", " + tabla.getNumPago() + "]");
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("actualizaTablaAmort", exc);
        } catch (Exception exc) {
            myLogger.error("actualizaTablaAmort", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("actualizaTablaAmort", exc);
            }
        }
    }

    public void actualizaCredito(double saldoFavor, int diasTrans, int diasMora, int estatus, double saldoCongelado, SaldoIBSVO saldo) {

        query = "UPDATE d_credito SET cr_monto_cuenta = ?,cr_num_dias = ?,cr_dias_mora = ?,cr_status = ?,cr_monto_cuenta_congelada = ?,cr_fecha_ult_actualiz = ?,cr_fecha_ult_pago = ? "
                + "WHERE cr_num_cliente = ? AND cr_num_credito =?";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, saldoFavor);
            ps.setDouble(2, diasTrans);
            ps.setDouble(3, diasMora);
            ps.setDouble(4, estatus);
            ps.setDouble(5, saldoCongelado);
            ps.setDate(6, saldo.getFechaGeneracion());
            ps.setDate(7, saldo.getFechaUltimoPago());
            ps.setInt(8, saldo.getIdClienteSICAP());
            ps.setInt(9, saldo.getCredito());
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("actualizaCredito", exc);
        } catch (Exception exc) {
            myLogger.error("actualizaCredito", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("actualizaCredito", exc);
            }
        }
    }

    public void actualizaSaldo(SaldoIBSVO saldo) {

        query = "UPDATE d_saldos SET ib_fecha_generacion = ?,ib_num_cuotas_trancurridas = ?,ib_saldo_total_dia = ?,ib_saldo_capital = ?,ib_saldo_interes = ?,"
                + "ib_saldo_interes_vigente = ?,ib_saldo_interes_vencido = ?,ib_saldo_iva_interes = ?,ib_saldo_multa = ?,ib_saldo_iva_multa = ?,ib_capital_pagado = ?,"
                + "ib_interes_normal_pagado = ?,ib_iva_interes_normal_pagado = ?,ib_multa_pagada = ?,ib_iva_multa_pagada = ?,ib_fecha_sig_amortizacion = ?,ib_capital_sig_amortizacion = ?,"
                + "ib_interes_sig_amortizacion = ?,ib_iva_interes_sig_amortizacion = ?,ib_saldo_con_intereses_al_final = ?,ib_capital_vencido = ?,ib_interes_vencido = ?,ib_iva_interes_vencido = ?,ib_total_vencido = ?,"
                + "ib_estatus = ?,ib_num_dias_mora = ?,ib_dias_transcurridos = ?,ib_cuotas_vencidas = ?,ib_num_pagos_realizados = ?,ib_moto_total_pagado = ?,"
                + "ib_fecha_ultimo_pago = ?,ib_saldo_bucket = ? WHERE ib_numclientesicap = ? AND ib_credito = ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, saldo.getFechaGeneracion());
            ps.setInt(2, saldo.getNumeroCuotasTranscurridas());
            ps.setDouble(3, saldo.getSaldoTotalAlDia());
            ps.setDouble(4, saldo.getSaldoCapital());
            ps.setDouble(5, saldo.getSaldoInteres());
            ps.setDouble(6, saldo.getSaldoInteresVigente());
            ps.setDouble(7, saldo.getSaldoInteresVencido());
            ps.setDouble(8, saldo.getSaldoIvaInteres());
            ps.setDouble(9, saldo.getSaldoMulta());
            ps.setDouble(10, saldo.getSaldoIVAMulta());
            ps.setDouble(11, saldo.getCapitalPagado());
            ps.setDouble(12, saldo.getInteresNormalPagado());
            ps.setDouble(13, saldo.getIvaInteresNormalPagado());
            ps.setDouble(14, saldo.getMultaPagada());
            ps.setDouble(15, saldo.getIvaMultaPagado());
            ps.setDate(16, saldo.getFechaSigAmortizacion());
            ps.setDouble(17, saldo.getCapitalSigAmortizacion());
            ps.setDouble(18, saldo.getInteresSigAmortizacion());
            ps.setDouble(19, saldo.getIvaSigAmortizacion());
            ps.setDouble(20, saldo.getSaldoConInteresAlFinal());
            ps.setDouble(21, saldo.getCapitalVencido());
            ps.setDouble(22, saldo.getInteresVencido());
            ps.setDouble(23, saldo.getIvaInteresVencido());
            ps.setDouble(24, saldo.getTotalVencido());
            ps.setDouble(25, saldo.getEstatus());
            ps.setDouble(26, saldo.getDiasMora());
            ps.setDouble(27, saldo.getDiasTranscurridos());
            ps.setDouble(28, saldo.getCuotasVencidas());
            ps.setDouble(29, saldo.getNumeroPagosRealizados());
            ps.setDouble(30, saldo.getMontoTotalPagado());
            ps.setDate(31, saldo.getFechaUltimoPago());
            ps.setDouble(32, saldo.getSaldoBucket());
            ps.setInt(33, saldo.getIdClienteSICAP());
            ps.setInt(34, saldo.getCredito());
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("actualizaSaldo", exc);
        } catch (Exception exc) {
            myLogger.error("actualizaSaldo", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("actualizaSaldo", exc);
            }
        }
    }
}

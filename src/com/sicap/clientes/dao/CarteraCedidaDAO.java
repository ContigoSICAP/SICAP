/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sun.org.apache.bcel.internal.generic.FALOAD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author Alex
 */
public class CarteraCedidaDAO extends DAOMaster{
    
    private String query = "";
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    
    public boolean updateDespachoSaldo(SaldoIBSVO saldo, int numDespacho, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            query = "UPDATE d_saldos SET ib_numdespacho=? WHERE ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numDespacho);
            pstm.setInt(2, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+numDespacho+", "+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro updateDespachoSaldo");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean updateSaldoCedido(SaldoIBSVO saldoCed, SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            query = "UPDATE d_saldos set ib_saldo_total_dia=0,ib_saldo_capital=0,ib_saldo_interes=0,ib_saldo_interes_vigente=0,ib_saldo_interes_vencido=0,ib_saldo_iva_interes=0,ib_saldo_multa=0,"+
                    "ib_saldo_iva_multa=0,ib_capital_pagado=?,ib_interes_normal_pagado=?,ib_iva_interes_normal_pagado=?,ib_multa_pagada=?,ib_iva_multa_pagada=?,"+
                    "ib_capital_sig_amortizacion=0,ib_interes_sig_amortizacion=0,ib_iva_interes_sig_amortizacion=0,ib_saldo_con_intereses_al_final=0,ib_capital_vencido=0,ib_total_vencido=0,ib_estatus=7,"+
                    "ib_moto_total_pagado=?,ib_fecha_ultimo_pago=CURDATE(),ib_saldo_bucket=0,ib_fecha_generacion=CURDATE(),ib_num_dias_mora=0,ib_cuotas_vencidas=0,ib_dias_transcurridos=DATEDIFF(CURDATE(),?),ib_num_pagos_realizados=ib_num_pagos_realizados+1 "+
                    "WHERE ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setDouble(1, saldo.getCapitalPagado());
            pstm.setDouble(2, saldo.getInteresNormalPagado());
            pstm.setDouble(3, saldo.getIvaInteresNormalPagado());
            pstm.setDouble(4, saldo.getMultaPagada());
            pstm.setDouble(5, saldo.getIvaMultaPagado());
            pstm.setDouble(6, saldo.getMontoTotalPagado());
            pstm.setDate(7, saldoCed.getFechaVencimiento());
            pstm.setInt(8, saldoCed.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro updateSaldoCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean updateCreditoCedido(SaldoIBSVO saldoCed, SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "UPDATE cartera_cec_local.d_credito SET cr_monto_cuenta=0,cr_num_dias=DATEDIFF(CURDATE(),?),cr_dias_mora=0,cr_status=7,cr_fecha_ult_actualiz=CURDATE(),cr_fecha_ult_pago=CURDATE(),"+
            query = "UPDATE cartera_cec.d_credito SET cr_monto_cuenta=0,cr_num_dias=DATEDIFF(CURDATE(),?),cr_dias_mora=0,cr_status=7,cr_fecha_ult_actualiz=CURDATE(),cr_fecha_ult_pago=CURDATE(),"+
                    "cr_monto_cuenta_congelada=0 WHERE cr_num_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setDate(1, saldo.getFechaVencimiento());
            pstm.setInt(2, saldoCed.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro updateCreditoCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertSaldoCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_saldos (ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,"+
            query = "INSERT INTO clientes_tci.d_saldos (ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,"+
                    "ib_fecha_envio,ib_fecha_generacion,ib_hora_generacion,ib_num_cuotas,ib_num_cuotas_trancurridas,ib_plazo,ib_periodicidad,ib_fecha_desembolso,ib_fecha_vencimiento,ib_monto_credito,"+
                    "ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_interes_vencido_90dias,ib_saldo_interes_cuentas_orden,ib_saldo_iva_interes,"+
                    "ib_saldo_bonificacion_iva,ib_saldo_interes_mora,ib_saldo_iva_mora,ib_saldo_multa,ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_bonificacion_pagada,"+
                    "ib_moratorio_pagado,ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_comision,ib_iva_comision,ib_monto_seguro,ib_monto_desembolsado,ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,"+
                    "ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_fondeador,ib_nombre_fondeador,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,ib_capital_vencido,"+
                    "ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,ib_producto,ib_fecha_incumplimiento,ib_fecha_a_cartera_vencida,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,"+
                    "ib_num_pagos_realizados,ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_bandera_reestructura,ib_credito_reestructurado,ib_dias_mora_reestructura,ib_tasa_preferencial_iva,ib_cuenta_bucket,ib_saldo_bucket,"+
                    "ib_cta_contable,ib_numdespacho,ib_fecha_migracion,ib_dias_transcurridos_migrados,ib_dias_mora_migrados,ib_cuotas_vencidas_migrados,ib_capital_migrado,ib_interes_migrado,ib_iva_interes_migrado,ib_multa_migrado,"+
                    "ib_saldo_total_migrado,ib_montoconintereses,ib_tasaelegida) "+
                    " SELECT ib_origen,ib_credito,ib_referencia,ib_numClienteSICAP,ib_numSolicitudSICAP,ib_numClienteIBS,ib_nombreCliente,ib_rfc,ib_numSucursal,ib_nombreSucursal,"+
                    "ib_fecha_envio,ib_fecha_generacion,ib_hora_generacion,ib_num_cuotas,ib_num_cuotas_trancurridas,ib_plazo,ib_periodicidad,ib_fecha_desembolso,ib_fecha_vencimiento,ib_monto_credito,"+
                    "ib_saldo_total_dia,ib_saldo_capital,ib_saldo_interes,ib_saldo_interes_vigente,ib_saldo_interes_vencido,ib_saldo_interes_vencido_90dias,ib_saldo_interes_cuentas_orden,ib_saldo_iva_interes,"+
                    "ib_saldo_bonificacion_iva,ib_saldo_interes_mora,ib_saldo_iva_mora,ib_saldo_multa,ib_saldo_iva_multa,ib_capital_pagado,ib_interes_normal_pagado,ib_iva_interes_normal_pagado,ib_bonificacion_pagada,"+
                    "ib_moratorio_pagado,ib_iva_moratorio_pagado,ib_multa_pagada,ib_iva_multa_pagada,ib_comision,ib_iva_comision,ib_monto_seguro,ib_monto_desembolsado,ib_fecha_sig_amortizacion,ib_capital_sig_amortizacion,"+
                    "ib_interes_sig_amortizacion,ib_iva_interes_sig_amortizacion,ib_fondeador,ib_nombre_fondeador,ib_tasa_interes_sin_iva,ib_tasa_mora_sin_iva,ib_tasa_iva,ib_saldo_con_intereses_al_final,ib_capital_vencido,"+
                    "ib_interes_vencido,ib_iva_interes_vencido,ib_total_vencido,ib_estatus,ib_producto,ib_fecha_incumplimiento,ib_fecha_a_cartera_vencida,ib_num_dias_mora,ib_dias_transcurridos,ib_cuotas_vencidas,"+
                    "ib_num_pagos_realizados,ib_moto_total_pagado,ib_fecha_ultimo_pago,ib_bandera_reestructura,ib_credito_reestructurado,ib_dias_mora_reestructura,ib_tasa_preferencial_iva,ib_cuenta_bucket,ib_saldo_bucket,"+
                    "ib_cta_contable,ib_numdespacho,CURDATE(),ib_dias_transcurridos,ib_num_dias_mora,ib_cuotas_vencidas,ib_capital_vencido,ib_interes_vencido,ib_iva_interes_vencido,"+
                    "(ib_saldo_multa+ib_saldo_iva_multa),ib_saldo_con_intereses_al_final,ib_montoconintereses,ib_tasaelegida FROM d_saldos WHERE ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertSaldoCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertCreditoCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO cartera_tci_local.d_credito (CR_NUM_CLIENTE,CR_NUM_SOLICITUD,CR_NUM_CREDITO,CR_REFERENCIA,CR_NUM_DOCUMENTO,CR_NUM_CUENTA,CR_NUM_PRODUCTO,CR_NUM_SUCURSAL,CR_NUM_EMPRESA,"+
            query = "INSERT INTO cartera_tci.d_credito (CR_NUM_CLIENTE,CR_NUM_SOLICITUD,CR_NUM_CREDITO,CR_REFERENCIA,CR_NUM_DOCUMENTO,CR_NUM_CUENTA,CR_NUM_PRODUCTO,CR_NUM_SUCURSAL,CR_NUM_EMPRESA,"+
                    "CR_FECHA_DESEMBOLSO,CR_FECHA_VENCIMIENTO,CR_PERIODICIDAD,CR_NUMERO_CUOTAS,CR_MONTO_CREDITO,CR_MONTO_DESEMBOLSADO,CR_MONTO_CUENTA,CR_MONTO_COMISION,CR_IVA_COMISION,CR_MONTO_SEGURO,"+
                    "CR_IVA_SEGURO,CR_OTROS_CARGOS,CR_IVA_OTROS_CARGOS,CR_TASA_INT,CR_TASA_INT_SINIVA,CR_TASA_COMISION_SINIVA,CR_TASA_MORA_SINIVA,CR_TASA_IVA,CR_NUM_DIAS,CR_DIAS_MORA,CR_STATUS,CR_FONDEADOR,"+
                    "CR_NUM_EJECUTIVO,CR_FECHA_LIQUIDACION,CR_FECHA_ULT_ACTUALIZ,CR_FECHA_ULT_PAGO,CR_VALOR_CUOTA,CR_BANCO_DESEMBOLSO,CR_MONTO_CUENTA_CONGELADA,CR_APLICA_SALDO) "+
                    " SELECT CR_NUM_CLIENTE,CR_NUM_SOLICITUD,CR_NUM_CREDITO,CR_REFERENCIA,CR_NUM_DOCUMENTO,CR_NUM_CUENTA,CR_NUM_PRODUCTO,CR_NUM_SUCURSAL,CR_NUM_EMPRESA,"+
                    "CR_FECHA_DESEMBOLSO,CR_FECHA_VENCIMIENTO,CR_PERIODICIDAD,CR_NUMERO_CUOTAS,CR_MONTO_CREDITO,CR_MONTO_DESEMBOLSADO,CR_MONTO_CUENTA,CR_MONTO_COMISION,CR_IVA_COMISION,CR_MONTO_SEGURO,"+
                    "CR_IVA_SEGURO,CR_OTROS_CARGOS,CR_IVA_OTROS_CARGOS,CR_TASA_INT,CR_TASA_INT_SINIVA,CR_TASA_COMISION_SINIVA,CR_TASA_MORA_SINIVA,CR_TASA_IVA,CR_NUM_DIAS,CR_DIAS_MORA,CR_STATUS,CR_FONDEADOR,"+
                    //"CR_NUM_EJECUTIVO,CR_FECHA_LIQUIDACION,CR_FECHA_ULT_ACTUALIZ,CR_FECHA_ULT_PAGO,CR_VALOR_CUOTA,CR_BANCO_DESEMBOLSO,CR_MONTO_CUENTA_CONGELADA,CR_APLICA_SALDO FROM cartera_cec_local.d_credito WHERE cr_num_credito=?";
                    "CR_NUM_EJECUTIVO,CR_FECHA_LIQUIDACION,CR_FECHA_ULT_ACTUALIZ,CR_FECHA_ULT_PAGO,CR_VALOR_CUOTA,CR_BANCO_DESEMBOLSO,CR_MONTO_CUENTA_CONGELADA,CR_APLICA_SALDO FROM cartera_cec.d_credito WHERE cr_num_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertCreditoCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertAmortClieCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_tabla_amortizacion (ta_id_cliente,ta_id_solicitud,ta_numdisposicion,ta_tpo_amortizacion,ta_no_pago,ta_fecha_pago,ta_saldo_inicial,ta_abono_capital,ta_saldo_capital,"+
            query = "INSERT INTO clientes_tci.d_tabla_amortizacion (ta_id_cliente,ta_id_solicitud,ta_numdisposicion,ta_tpo_amortizacion,ta_no_pago,ta_fecha_pago,ta_saldo_inicial,ta_abono_capital,ta_saldo_capital,"+
                    "ta_comision_inicial,ta_iva_comision,ta_interes,ta_iva_interes,ta_monto_pagar,ta_pagado) "+
                    " SELECT ta_id_cliente,ta_id_solicitud,ta_numdisposicion,ta_tpo_amortizacion,ta_no_pago,ta_fecha_pago,ta_saldo_inicial,ta_abono_capital,ta_saldo_capital,"+
                    "ta_comision_inicial,ta_iva_comision,ta_interes,ta_iva_interes,ta_monto_pagar,ta_pagado FROM d_tabla_amortizacion WHERE ta_id_cliente=? AND ta_id_solicitud=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            pstm.setInt(2, saldo.getIdSolicitudSICAP());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getIdSolicitudSICAP()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertAmortClieCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertAmortCartCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO cartera_tci_local.d_tabla_amortizacion (TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,"+
            query = "INSERT INTO cartera_tci.d_tabla_amortizacion (TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,"+
                    "TA_CAPITAL_PAGADO,TA_COMISION_INICIAL,TA_IVA_COMISION,TA_INTERES,TA_IVA_INTERES,TA_INTERES_ACUM,TA_IVA_INTERES_ACUM,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,"+
                    "TA_IVA_MORA_PAGADO,TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO)"+
                    " SELECT TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,"+
                    "TA_CAPITAL_PAGADO,TA_COMISION_INICIAL,TA_IVA_COMISION,TA_INTERES,TA_IVA_INTERES,TA_INTERES_ACUM,TA_IVA_INTERES_ACUM,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,"+
                    //"TA_IVA_MORA_PAGADO,TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO FROM cartera_cec_local.d_tabla_amortizacion WHERE ta_numcredito=?";
                    "TA_IVA_MORA_PAGADO,TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO FROM cartera_cec.d_tabla_amortizacion WHERE ta_numcredito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertAmortCartCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertCiclosGrupalesCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_ciclos_grupales (ci_numgrupo,ci_numciclo,ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_dia_reunion,ci_desembolsado,ci_hora_reunion,ci_ejecutivo,"+
            query = "INSERT INTO clientes_tci.d_ciclos_grupales (ci_numgrupo,ci_numciclo,ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_dia_reunion,ci_desembolsado,ci_hora_reunion,ci_ejecutivo,"+
                    "ci_coordinador,ci_multa_retraso,ci_multa_falta,ci_tasa,ci_fecha_captura,ci_numdireccion_reunion,ci_numdireccion_alterna,ci_contrato,ci_monto,ci_monto_con_comision,ci_monto_refinanciado,ci_tasa_calculada,"+
                    "ci_estatus_revision_monitor) "+
                    " SELECT ci_numgrupo,ci_numciclo,ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_dia_reunion,ci_desembolsado,ci_hora_reunion,ci_ejecutivo,"+
                    "ci_coordinador,ci_multa_retraso,ci_multa_falta,ci_tasa,ci_fecha_captura,ci_numdireccion_reunion,ci_numdireccion_alterna,ci_contrato,ci_monto,ci_monto_con_comision,ci_monto_refinanciado,ci_tasa_calculada,"+
                    "ci_estatus_revision_monitor FROM d_ciclos_grupales WHERE ci_numcredito_ibs=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertCiclosGrupalesCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertClientesCedido(SaldoIBSVO saldo, Connection con, Savepoint save, String lstClientes) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_clientes (en_numcliente,en_numcliente_ibs,en_numsucursal,en_estatus,en_nombre,en_primer_apellido,en_segundo_apellido,en_nombre_completo,en_rfc,en_fecha_nac,"+
            query = "INSERT INTO clientes_tci.d_clientes (en_numcliente,en_numcliente_ibs,en_numsucursal,en_estatus,en_nombre,en_primer_apellido,en_segundo_apellido,en_nombre_completo,en_rfc,en_fecha_nac,"+
                    "en_entidad_nac,en_sexo,en_nacionalidad,en_tipo_id,en_numero_identificacion,en_edo_civil,en_email,en_fecha_captura,en_fecharegistro_ibs,en_dependientes_economicos,en_numgrupo,en_nivel_estudios,en_curp) "+
                    "SELECT en_numcliente,en_numcliente_ibs,en_numsucursal,en_estatus,en_nombre,en_primer_apellido,en_segundo_apellido,en_nombre_completo,en_rfc,en_fecha_nac,"+
                    "en_entidad_nac,en_sexo,en_nacionalidad,en_tipo_id,en_numero_identificacion,en_edo_civil,en_email,en_fecha_captura,en_fecharegistro_ibs,en_dependientes_economicos,en_numgrupo,en_nivel_estudios,en_curp FROM d_saldos,d_integrantes_ciclo,d_clientes "+
                    "WHERE ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo AND ic_numcliente=en_numcliente AND ib_credito=? AND ic_numcliente NOT IN (?)";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            pstm.setString(2, lstClientes);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+",  ("+lstClientes+")]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertClientesCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertCliTelefonoCedido(SaldoIBSVO saldo, String lstClientes, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_telefonos (te_numcliente, te_numdireccion, te_numtelefono, te_tipotelefono, te_telefono) " +
            query = "INSERT INTO clientes_tci.d_telefonos (te_numcliente, te_numdireccion, te_numtelefono, te_tipotelefono, te_telefono) " +
                    "SELECT te_numcliente, te_numdireccion, te_numtelefono, te_tipotelefono, te_telefono FROM d_integrantes_ciclo,d_telefonos WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_numcliente=te_numcliente AND ic_numcliente NOT IN (?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            pstm.setInt(2, saldo.getIdSolicitudSICAP());
            pstm.setString(3, lstClientes);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getIdSolicitudSICAP()+", ("+lstClientes+")]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertCliTelefonoCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertCliDireccionCedido(SaldoIBSVO saldo, String lstClientes, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_direcciones (di_numcliente,di_numsolicitud,di_tabla,di_indice_tabla,di_numdireccion,di_numcolonia,di_calle,di_numero_ext,di_numero_int,di_situacion_vivienda,di_ant_domicilio,di_numlocalidad) " +
            query = "INSERT INTO clientes_tci.d_direcciones (di_numcliente,di_numsolicitud,di_tabla,di_indice_tabla,di_numdireccion,di_numcolonia,di_calle,di_numero_ext,di_numero_int,di_situacion_vivienda,di_ant_domicilio,di_numlocalidad) " +
                    "SELECT di_numcliente,di_numsolicitud,di_tabla,di_indice_tabla,di_numdireccion,di_numcolonia,di_calle,di_numero_ext,di_numero_int,di_situacion_vivienda,di_ant_domicilio,di_numlocalidad FROM d_integrantes_ciclo,d_direcciones WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_numcliente=di_numcliente AND di_numcliente NOT IN (?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            pstm.setInt(2, saldo.getIdSolicitudSICAP());
            pstm.setString(3, lstClientes);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getIdSolicitudSICAP()+", ("+lstClientes+")]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertCliDireccionCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertSolicitudesCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_solicitudes (so_numcliente,so_numsolicitud,so_numcredito_ibs,so_numcuenta_ibs,so_estatus,so_cvesolicitud,so_numsucursal,so_numoperacion,so_reestructura,"+
            query = "INSERT INTO clientes_tci.d_solicitudes (so_numcliente,so_numsolicitud,so_numcredito_ibs,so_numcuenta_ibs,so_estatus,so_cvesolicitud,so_numsucursal,so_numoperacion,so_reestructura,"+
                    "so_solicitud_reestructura,so_fecha_firma,so_fecha_captura,so_nummedio,so_numejecutivo,so_fuente,so_monto_solicitado,so_plazo_solicitado,so_frecpago_solicitada,so_destino_credito,"+
                    "so_monto_propuesto,so_plazo_propuesto,so_frecpago_propuesta,so_cuota,so_desembolsado,so_fecha_desembolso,so_no_contrato,so_tasa_calculada,so_numcheque,so_cat,so_comentarios,so_numrepresentante) "+
                    "SELECT so_numcliente,so_numsolicitud,so_numcredito_ibs,so_numcuenta_ibs,so_estatus,so_cvesolicitud,so_numsucursal,so_numoperacion,so_reestructura,"+
                    "so_solicitud_reestructura,so_fecha_firma,so_fecha_captura,so_nummedio,so_numejecutivo,so_fuente,so_monto_solicitado,so_plazo_solicitado,so_frecpago_solicitada,so_destino_credito,"+
                    "so_monto_propuesto,so_plazo_propuesto,so_frecpago_propuesta,so_cuota,so_desembolsado,so_fecha_desembolso,so_no_contrato,so_tasa_calculada,so_numcheque,so_cat,so_comentarios,so_numrepresentante FROM d_saldos,d_integrantes_ciclo,d_solicitudes "+
                    "WHERE ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo AND ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud AND ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertSolicitudesCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertIntegrantesCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_integrantes_ciclo (ic_numgrupo,ic_numciclo,ic_numcliente,ic_numsolicitud,ic_monto,ic_monto_refinanciar,ic_comision,ic_estatus,ic_calificacion,ic_rol,ic_fecha_captura)"+
            query = "INSERT INTO clientes_tci.d_integrantes_ciclo (ic_numgrupo,ic_numciclo,ic_numcliente,ic_numsolicitud,ic_monto,ic_monto_refinanciar,ic_comision,ic_estatus,ic_calificacion,ic_rol,ic_fecha_captura)"+
                    " SELECT ic_numgrupo,ic_numciclo,ic_numcliente,ic_numsolicitud,ic_monto,ic_monto_refinanciar,ic_comision,ic_estatus,ic_calificacion,ic_rol,ic_fecha_captura FROM d_saldos,d_integrantes_ciclo "+
                "WHERE ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo AND ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertIntegrantesCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertDeciComiteCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_decision_comite (de_numcliente,de_numsolicitud,de_fecharealizacion,de_fechacaptura,de_decision_comite,de_causarechazo,de_motivorechazocliente,"+
            query = "INSERT INTO clientes_tci.d_decision_comite (de_numcliente,de_numsolicitud,de_fecharealizacion,de_fechacaptura,de_decision_comite,de_causarechazo,de_motivorechazocliente,"+
                    "de_detallemotivorechazocliente,de_montoautorizado,de_montorefinanciado,de_plazoautorizado,de_comision,de_tasa,de_frecuenciapago,de_motivocondicionamiento,de_comentarioscomite,de_multa) "+
                    "SELECT de_numcliente,de_numsolicitud,de_fecharealizacion,de_fechacaptura,de_decision_comite,de_causarechazo,de_motivorechazocliente,"+
                    "de_detallemotivorechazocliente,de_montoautorizado,de_montorefinanciado,de_plazoautorizado,de_comision,de_tasa,de_frecuenciapago,de_motivocondicionamiento,de_comentarioscomite,de_multa "+
                    "FROM d_saldos,d_integrantes_ciclo,d_decision_comite "+
                    "WHERE ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo AND ic_numcliente=de_numcliente AND ic_numsolicitud=de_numsolicitud AND ib_credito=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertDeciComiteCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertPagosCedido(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_pagos_cartera (pc_referencia,pc_monto,pc_fecha_pago,pc_fecha_hora,pc_banco_referencia,pc_sucursal,pc_status,pc_destino,pc_id_contabilidad,pc_enviado,pc_numregistro)"+
            query = "INSERT INTO clientes_tci.d_pagos_cartera (pc_referencia,pc_monto,pc_fecha_pago,pc_fecha_hora,pc_banco_referencia,pc_sucursal,pc_status,pc_destino,pc_id_contabilidad,pc_enviado,pc_numregistro) "+
                    "SELECT pc_referencia,pc_monto,pc_fecha_pago,pc_fecha_hora,pc_banco_referencia,pc_sucursal,pc_status,pc_destino,pc_id_contabilidad,pc_enviado,0 FROM d_pagos_cartera WHERE pc_referencia=?";
            pstm = con.prepareStatement(query);
            pstm.setString(1, saldo.getReferencia());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getReferencia()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertPagosCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertPagosLiquida(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_pagos_cartera (pc_referencia,pc_monto,pc_fecha_pago,pc_fecha_hora,pc_banco_referencia,pc_sucursal,pc_status,pc_destino,pc_id_contabilidad,pc_enviado,pc_numregistro) VALUES (?,?,CURDATE(),CONCAT(CURDATE(),' ',CURTIME()),7,LEFT(?,3),6,1,0,1,0)";
            pstm = con.prepareStatement(query);
            pstm.setString(1, saldo.getReferencia());
            pstm.setDouble(2, saldo.getSaldoConInteresAlFinal());
            pstm.setString(3, saldo.getReferencia());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getReferencia()+", "+saldo.getSaldoConInteresAlFinal()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertPagosCedido");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean updateSaldoTabla(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "UPDATE cartera_cec_local.d_tabla_amortizacion SET ta_capital_pagado=ta_abono_capital,ta_interes_pagado=ta_interes,ta_iva_interes_pagado=ta_iva_interes, "+
            query = "UPDATE cartera_cec.d_tabla_amortizacion SET ta_capital_pagado=ta_abono_capital,ta_interes_pagado=ta_interes,ta_iva_interes_pagado=ta_iva_interes, "+
                    "ta_multa_pagado=ta_multa,ta_iva_multa_pagado=ta_iva_multa,ta_monto_pagado=(ta_monto_pagar+ta_monto_pagado),ta_pagado='S',ta_monto_pagar=0 WHERE ta_numcredito=? AND TA_PAGADO='N'";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]"); 
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro updateSaldoTabla");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public SaldoIBSVO getSaldo(SaldoIBSVO saldo) throws SQLException, ClientesException{
        
        SaldoIBSVO saldoVO = null;
        Connection conn = null;
        try {
            query = "SELECT SUM(ta_capital_pagado),SUM(ta_interes_pagado),SUM(ta_iva_interes_pagado),SUM(ta_multa_pagado),SUM(ta_iva_multa_pagado),SUM(ta_monto_pagado) "+
                    "FROM d_tabla_amortizacion WHERE ta_numcredito=?";
            conn = getCWConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, saldo.getCredito());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getCredito()+"]");
            res = pstm.executeQuery();
            if (res.next()) {
                saldoVO = new SaldoIBSVO(res.getDouble(1), res.getDouble(2), res.getDouble(3), res.getDouble(4), res.getDouble(5), res.getDouble(6));
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getGruposAsesor : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getGruposAsesor: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return saldoVO;
    }
    
    public boolean getBuscaGrupo(SaldoIBSVO saldo) throws SQLException, ClientesException{
        
        boolean existe = false;
        Connection conn = null;
        try {
            query = "SELECT gr_numgrupo FROM d_grupos WHERE gr_numgrupo=?;";
            conn = getTCIConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getNombreCliente()+"]");
            res = pstm.executeQuery();
            if (res.next())
                existe = true;
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getBuscaGrupo : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getBuscaGrupo: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return existe;
    }
    
    public boolean insertGrupo(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException, ClientesException, NamingException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_grupos SELECT * FROM d_grupos WHERE gr_numgrupo=?;";
            query = "INSERT INTO clientes_tci.d_grupos SELECT * FROM d_grupos WHERE gr_numgrupo=?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getNombreCliente()+"]");
            System.out.println("pstm "+pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertGrupo");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public int getDireccionReunion(int idCredito, int tipo) throws SQLException, ClientesException{
        
        int numDir = 0;
        Connection conn = null;
        try {
            query = "SELECT ci_numdireccion_reunion FROM d_ciclos_grupales WHERE ci_numcredito_ibs=?;";
            if(tipo == 1)
                conn = getConnection();
            else if(tipo == 2)
                conn = getTCIConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idCredito);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCredito+"]");
            res = pstm.executeQuery();
            if (res.next())
                numDir = res.getInt("ci_numdireccion_reunion");
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getDireccionReunion : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getDireccionReunion: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numDir;
    }
    
    public boolean insertDireccion(int numDir, Connection con, Savepoint save) throws SQLException, ClientesException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_direcciones_genericas SELECT * FROM d_direcciones_genericas where dg_numdireccion=?;";
            query = "INSERT INTO clientes_tci.d_direcciones_genericas SELECT * FROM d_direcciones_genericas where dg_numdireccion=?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numDir);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+numDir+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertDireccion");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertReferencia(SaldoIBSVO saldo, Connection con, Savepoint save) throws SQLException, ClientesException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO clientes_tci_local.d_referencias_generales SELECT * FROM d_referencias_generales WHERE rg_numcliente=? AND rg_numsolicitud=?;";
            query = "INSERT INTO clientes_tci.d_referencias_generales SELECT * FROM d_referencias_generales WHERE rg_numcliente=? AND rg_numsolicitud=?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            pstm.setInt(2, saldo.getIdSolicitudSICAP());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getIdSolicitudSICAP()+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertReferencia");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public List getListaClientes(SaldoIBSVO saldo, Connection con) throws SQLException, ClientesException{
        
        List list = new ArrayList();
        try {
            query = "SELECT ic_numcliente FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numciclo=?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, saldo.getIdClienteSICAP());
            pstm.setInt(2, saldo.getIdSolicitudSICAP());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+saldo.getIdClienteSICAP()+", "+saldo.getIdSolicitudSICAP()+"]");
            res = pstm.executeQuery();
            while(res.next()){
                list.add(res.getInt("ic_numcliente"));
            }
        } catch (SQLException e) {
            Logger.debug("Problema dentro getListaClientes");
            e.printStackTrace();
        }
        return list;
    }
    
    public int getCliente(int idCliente) throws SQLException, ClientesException, NamingException{
        
        int cliente = 0;
        Connection conn = null;
        try {
            query = "SELECT en_numcliente FROM d_clientes WHERE en_numcliente=?;";
            conn = getTCIConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idCliente);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCliente+"]");
            res = pstm.executeQuery();
            while(res.next()){
                cliente = res.getInt("en_numcliente");
            }
        } catch (SQLException e) {
            conn.close();
            Logger.debug("Problema dentro getCliente");
            e.printStackTrace();
        }
        conn.close();
        return cliente;
    }
    
}

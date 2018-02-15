package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import org.apache.log4j.Logger;

public class CreditoCartDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(CreditoCartDAO.class);

    public CreditoCartVO getCreditoClienteSol(int numCliente, int numSolicitud) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE CR_NUM_CLIENTE = ? AND CR_NUM_SOLICITUD = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + numSolicitud + "]");
            rs = ps.executeQuery();

            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
            //myLogger.debug(query);
            //myLogger.debug("parametros ["+ numCliente + ","+numSolicitud+"]");

        } catch (SQLException sqle) {
            myLogger.error("getCreditoClienteSol", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoClienteSol", e);
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

        return creditos;
    }

    public CreditoCartVO getCreditoClienteSolLiq(int numCliente, int numSolicitud) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE CR_NUM_CLIENTE = ? AND CR_NUM_SOLICITUD = ? AND CR_STATUS = 3";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);

            rs = ps.executeQuery();

            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
            myLogger.debug(query);
            myLogger.debug("parametros [" + numCliente + "," + numSolicitud + "]");

        } catch (SQLException sqle) {
            myLogger.error("getCreditoClienteSol", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoClienteSol", e);
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

        return creditos;
    }

    public CreditoCartVO getCreditoReferencia(String referencia) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE cr_referencia = ? ";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, referencia);

            rs = ps.executeQuery();

            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoReferencia", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoReferencia", e);
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

        return creditos;
    }

    public CreditoCartVO getCreditoReferencia(String referencia, Connection conn) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM d_credito WHERE cr_referencia = ? ";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, referencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoReferencia", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoReferencia", e);
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
        return creditos;
    }
    
    public CreditoCartVO getCreditoReferenciaPagos(String referencia, Connection con) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String query = "SELECT * FROM d_credito WHERE cr_referencia = ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoReferencia", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoReferencia", e);
            throw new ClientesException(e.getMessage());
        }
        return creditos;
    }

    public CreditoCartVO getCredito(int numCredito) throws ClientesException {

        CreditoCartVO creditos = new CreditoCartVO();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE cr_num_credito = ? ";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, numCredito);

            rs = ps.executeQuery();

            if (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCredito", e);
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

        return creditos;
    }

    public CreditoCartVO[] getCreditoVig() throws ClientesException {

        CreditoCartVO creditos = null;
        ArrayList<CreditoCartVO> array = new ArrayList<CreditoCartVO>();
        CreditoCartVO elementos[] = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            //String query = "SELECT * FROM d_credito WHERE cr_status in (1,2,5) AND CR_BANCO_DESEMBOLSO = 0";
            String query = "SELECT * FROM d_credito WHERE cr_status in (1,2,4,6) AND CR_BANCO_DESEMBOLSO = 0 "
                    + "ORDER BY CR_NUM_CLIENTE";

            PreparedStatement ps = conn.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
                array.add(creditos);
            }
            if (array.size() > 0) {
                myLogger.debug("numero cliente " + creditos.getNumCliente());
                elementos = new CreditoCartVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CreditoCartVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoVig", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoVig", e);
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
        return elementos;
    }

    /*
     * Metodo que se utiliza para obtener los creditos migrados, utilizando el cambo banco de desembolso como bandera
     */

    public CreditoCartVO[] getCreditoVigMigracion() throws ClientesException {

        CreditoCartVO creditos = null;
        ArrayList<CreditoCartVO> array = new ArrayList<CreditoCartVO>();
        CreditoCartVO elementos[] = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE CR_BANCO_DESEMBOLSO = 1 and cr_status in (1,2,4)";

            PreparedStatement ps = conn.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
                array.add(creditos);
            }
            if (array.size() > 0) {
                myLogger.debug("numero cliente " + creditos.getNumCliente());
                elementos = new CreditoCartVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CreditoCartVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoVigMigracion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoVigMigracion", e);
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
        return elementos;
    }

    public CreditoCartVO[] getCreditoMorVen() throws ClientesException {

        CreditoCartVO creditos = null;
        ArrayList<CreditoCartVO> array = new ArrayList<CreditoCartVO>();
        CreditoCartVO elementos[] = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            //String query = "SELECT * FROM d_credito WHERE cr_status in (2,4) and cr_banco_desembolso = 0";
            String query = "SELECT * FROM d_credito WHERE cr_status in (2,4,6) and cr_banco_desembolso = 0";

            PreparedStatement ps = conn.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
                array.add(creditos);
            }
            if (array.size() > 0) {
                elementos = new CreditoCartVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CreditoCartVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoMorVen", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoMorVen", e);
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

        return elementos;
    }

    public CreditoCartVO[] getCreditoMorVenMigracion() throws ClientesException {

        CreditoCartVO creditos = null;
        ArrayList<CreditoCartVO> array = new ArrayList<CreditoCartVO>();
        CreditoCartVO elementos[] = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE cr_status in (2,4) and cr_banco_desembolso = 1";

            PreparedStatement ps = conn.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                creditos = new CreditoCartVO();

                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
                array.add(creditos);
            }
            if (array.size() > 0) {
                elementos = new CreditoCartVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CreditoCartVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditoMorVenMigracion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditoMorVenMigracion", e);
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

        return elementos;
    }

    public int addCredito(List lista) throws ClientesException {

        CreditoCartVO credito = null;

        Connection conn = null;
        int res = 0;

        String query
                = "INSERT INTO d_credito "
                + "( cr_num_cliente, cr_num_solicitud, cr_num_credito, cr_referencia, cr_num_documento, cr_num_cuenta, cr_num_producto, cr_num_sucursal, cr_num_empresa, cr_fecha_desembolso,"
                + "cr_fecha_vencimiento,  cr_periodicidad, cr_numero_cuotas,cr_monto_credito, cr_monto_desembolsado, cr_monto_cuenta, cr_monto_comision, cr_iva_comision, cr_monto_seguro, cr_iva_seguro,"
                + "cr_otros_cargos, cr_iva_otros_cargos, cr_tasa_int, cr_tasa_int_siniva, cr_tasa_comision_siniva, cr_tasa_mora_siniva, cr_tasa_iva, cr_num_dias, cr_dias_mora, cr_status, cr_fondeador,"
                + "cr_num_ejecutivo, cr_fecha_liquidacion, cr_fecha_ult_actualiz, cr_fecha_ult_pago, cr_valor_cuota, cr_banco_desembolso,cr_monto_cuenta_congelada) "
                + "VALUES "
                + "( ?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?)";

        try {
            conn = getCWConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);
            Iterator i = lista.iterator();

            while (i.hasNext()) {
                credito = (CreditoCartVO) i.next();

                ps.setInt(1, credito.getNumCliente());
                ps.setInt(2, credito.getNumSolicitud());
                ps.setInt(3, credito.getNumCredito());
                ps.setString(4, credito.getReferencia());
                ps.setString(5, credito.getNumDocumento());
                ps.setInt(6, credito.getNumCuenta());
                ps.setInt(7, credito.getNumProducto());
                ps.setInt(8, credito.getNumSucursal());
                ps.setInt(9, credito.getNumEmpresa());
                ps.setDate(10, credito.getFechaDesembolso());
                ps.setDate(11, credito.getFechaVencimiento());
                ps.setInt(12, credito.getPeriodicidad());
                ps.setInt(13, credito.getNumCuotas());
                ps.setDouble(14, credito.getMontoCredito());
                ps.setDouble(15, credito.getMontoDesembolsado());
                ps.setDouble(16, credito.getMontoCuenta());
                ps.setDouble(17, credito.getMontoComision());
                ps.setDouble(18, credito.getMontoIvaComision());
                ps.setDouble(19, credito.getMontoSeguro());
                ps.setDouble(20, credito.getMontoIvaSeguro());
                ps.setDouble(21, credito.getMontoOtrosCargos());
                ps.setDouble(22, credito.getMontoIvaOtrosCargos());
                ps.setDouble(23, credito.getTasaInteres());
                ps.setDouble(24, credito.getTasaInteresSIVA());
                ps.setDouble(25, credito.getTasaComision());
                ps.setDouble(26, credito.getTasaMora());
                ps.setDouble(27, credito.getTasaIVA());
                ps.setDouble(28, credito.getNumDias());
                ps.setDouble(29, credito.getNumDiasMora());
                ps.setInt(30, credito.getStatus());
                ps.setInt(31, credito.getFondeador());
                ps.setInt(32, credito.getFondeador());
                ps.setDate(33, credito.getFechaLiquidacion());
                ps.setDate(34, credito.getFechaUltimaActualizacion());
                ps.setDate(35, credito.getFechaUltimoPago());
                ps.setDouble(36, credito.getValorCuota());
                ps.setInt(37, credito.getBancoDesembolso());
                ps.setDouble(38, credito.getMontoCuentaCongelada());

                res = ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("addCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addCredito", e);
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

    public int addCredito(CreditoCartVO credito) throws ClientesException {

        Connection conn = null;
        int res = 0;

        String query
                = "INSERT INTO d_credito "
                + "(cr_num_cliente, cr_num_solicitud, cr_num_credito, cr_referencia, cr_num_documento, cr_num_cuenta, cr_num_producto, cr_num_sucursal, cr_num_empresa, cr_fecha_desembolso,"
                + "cr_fecha_vencimiento,  cr_periodicidad, cr_numero_cuotas, cr_monto_credito, cr_monto_desembolsado, cr_monto_cuenta, cr_monto_comision, cr_iva_comision, cr_monto_seguro, cr_iva_seguro,"
                + "cr_otros_cargos, cr_iva_otros_cargos, cr_tasa_int, cr_tasa_int_siniva, cr_tasa_comision_siniva, cr_tasa_mora_siniva, cr_tasa_iva, cr_num_dias, cr_dias_mora, cr_status, "
                + "cr_fondeador, cr_num_ejecutivo, cr_fecha_liquidacion, cr_fecha_ult_actualiz, cr_fecha_ult_pago, cr_valor_cuota, cr_banco_desembolso,cr_monto_cuenta_congelada) "
                + "VALUES "
                + "(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?)";

        try {
            conn = getCWConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, credito.getNumCliente());
            ps.setInt(2, credito.getNumSolicitud());
            ps.setInt(3, credito.getNumCredito());
            ps.setString(4, credito.getReferencia());
            ps.setString(5, credito.getNumDocumento());
            ps.setInt(6, credito.getNumCuenta());
            ps.setInt(7, credito.getNumProducto());
            ps.setInt(8, credito.getNumSucursal());
            ps.setInt(9, credito.getNumEmpresa());
            ps.setDate(10, credito.getFechaDesembolso());
            ps.setDate(11, credito.getFechaVencimiento());
            ps.setInt(12, credito.getPeriodicidad());
            ps.setInt(13, credito.getNumCuotas());
            ps.setDouble(14, credito.getMontoCredito());
            ps.setDouble(15, credito.getMontoDesembolsado());
            ps.setDouble(16, credito.getMontoCuenta());
            ps.setDouble(17, credito.getMontoComision());
            ps.setDouble(18, credito.getMontoIvaComision());
            ps.setDouble(19, credito.getMontoSeguro());
            ps.setDouble(20, credito.getMontoIvaSeguro());
            ps.setDouble(21, credito.getMontoOtrosCargos());
            ps.setDouble(22, credito.getMontoIvaOtrosCargos());
            ps.setDouble(23, credito.getTasaInteres());
            ps.setDouble(24, credito.getTasaInteresSIVA());
            ps.setDouble(25, credito.getTasaComision());
            ps.setDouble(26, credito.getTasaMora());
            ps.setDouble(27, credito.getTasaIVA());
            ps.setDouble(28, credito.getNumDias());
            ps.setDouble(29, credito.getNumDiasMora());
            ps.setInt(30, credito.getStatus());
            ps.setInt(31, credito.getFondeador());
            ps.setInt(32, credito.getNumEjecutivo());
            ps.setDate(33, credito.getFechaLiquidacion());
            ps.setDate(34, credito.getFechaUltimaActualizacion());
            ps.setDate(35, credito.getFechaUltimoPago());
            ps.setDouble(36, credito.getValorCuota());
            ps.setInt(37, credito.getBancoDesembolso());
            ps.setDouble(38, credito.getMontoCuentaCongelada());
            myLogger.debug("Ejecutando " + ps.toString());
            res = ps.executeUpdate();
            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("addCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addCredito", e);
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

    public int updatePagoCredito(CreditoCartVO credito) throws ClientesException {

        String query = "UPDATE D_CREDITO SET CR_MONTO_CUENTA = ? , CR_MONTO_CUENTA_CONGELADA = ?, CR_DIAS_MORA = ?, CR_STATUS = ? , CR_FECHA_ULT_ACTUALIZ = ?, CR_FECHA_ULT_PAGO = ? "
                + "WHERE CR_NUM_CLIENTE = ? AND CR_NUM_CREDITO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(param++, credito.getMontoCuenta());
            ps.setDouble(param++, credito.getMontoCuentaCongelada());
            ps.setInt(param++, credito.getNumDiasMora());
            ps.setInt(param++, credito.getStatus());
            ps.setDate(param++, credito.getFechaUltimaActualizacion());
            ps.setDate(param++, credito.getFechaUltimoPago());
            ps.setInt(param++, credito.getNumCliente());
            ps.setInt(param++, credito.getNumCredito());
            myLogger.debug("Ejecutando " + ps.toString());
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updatePagoCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updatePagoCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int updatePagoCreditoCierre(CreditoCartVO credito) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        int res = 0;
        String query = "UPDATE D_CREDITO SET cr_monto_cuenta= ? WHERE cr_num_cliente= ? AND cr_num_credito= ?";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, credito.getMontoCuenta());
            ps.setInt(2, credito.getNumCliente());
            ps.setInt(3, credito.getNumCredito());
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updatePagoCreditoCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updatePagoCreditoCierre", e);
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
        return res;
    }
        public boolean updatePagoCreditoCarga(CreditoCartVO credito, Connection con) throws ClientesException {

        PreparedStatement ps = null;
        boolean listo = true;
        String query = "UPDATE D_CREDITO SET cr_monto_cuenta= ? WHERE cr_num_cliente= ? AND cr_num_credito= ?";
        try {
            ps = con.prepareStatement(query);
            ps.setDouble(1, credito.getMontoCuenta());
            ps.setInt(2, credito.getNumCliente());
            ps.setInt(3, credito.getNumCredito());
            myLogger.debug("Ejecutando " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("Error en updatePagoCreditoCarga", sqle);
            return listo = false;
        }
        return listo;
    }


    public boolean updatePagoCreditoCierre(CreditoCartVO credito, Connection con) throws ClientesException {

        PreparedStatement ps = null;
        boolean listo = true;
        String query = "UPDATE D_CREDITO SET cr_monto_cuenta= ? WHERE cr_num_cliente= ? AND cr_num_credito= ?";
        try {
            ps = con.prepareStatement(query);
            ps.setDouble(1, credito.getMontoCuenta());
            ps.setInt(2, credito.getNumCliente());
            ps.setInt(3, credito.getNumCredito());
            myLogger.debug("Ejecutando " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("Error en updatePagoCreditoCierre", sqle);
            return listo = false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return listo;
    }

    public int getMaxCredito() throws ClientesException {

        String query = "SELECT MAX(CR_NUM_CREDITO) FROM D_CREDITO ";

        Connection cn = null;
        int numCredito = 0;

        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numCredito = rs.getInt(1);
            }

        } catch (SQLException sqle) {
            myLogger.error("getMaxCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMaxCredito", e);
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
        return numCredito;

    }

    public void deleteCreditoCiclo(CreditoCartVO credito) throws ClientesDBException, ClientesException {
        String query = "DELETE FROM d_credito WHERE cr_num_cliente= ? and cr_num_credito= ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(1, credito.getNumCliente());
            ps.setDouble(2, credito.getNumCredito());
            myLogger.debug("Ejecutando " + query);
            myLogger.debug("Parametros [" + credito.getNumCliente() + "," + credito.getNumCredito() + "]");
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("deleteCreditoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteCreditoCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public void respaldaCredito(int numEquipo, int numCredito) throws ClientesDBException, ClientesException {
        String query = "insert into d_credito_del (CR_NUM_CLIENTE,CR_NUM_SOLICITUD,CR_NUM_CREDITO,CR_REFERENCIA,CR_NUM_DOCUMENTO,CR_NUM_CUENTA,CR_NUM_PRODUCTO,CR_NUM_SUCURSAL,CR_NUM_EMPRESA,CR_FECHA_DESEMBOLSO,CR_FECHA_VENCIMIENTO,CR_PERIODICIDAD,CR_NUMERO_CUOTAS,CR_MONTO_CREDITO,CR_MONTO_DESEMBOLSADO,CR_MONTO_CUENTA,CR_MONTO_COMISION,CR_IVA_COMISION,CR_MONTO_SEGURO,CR_IVA_SEGURO,CR_OTROS_CARGOS,CR_IVA_OTROS_CARGOS,CR_TASA_INT,CR_TASA_INT_SINIVA,CR_TASA_COMISION_SINIVA,CR_TASA_MORA_SINIVA,CR_TASA_IVA,CR_NUM_DIAS,CR_DIAS_MORA,CR_STATUS,CR_FONDEADOR,CR_NUM_EJECUTIVO,CR_FECHA_LIQUIDACION,CR_FECHA_ULT_ACTUALIZ,CR_FECHA_ULT_PAGO,CR_VALOR_CUOTA,CR_BANCO_DESEMBOLSO,CR_MONTO_CUENTA_CONGELADA,CR_APLICA_SALDO,cr_fechamov)"
                + " (select CR_NUM_CLIENTE,CR_NUM_SOLICITUD,CR_NUM_CREDITO,CR_REFERENCIA,CR_NUM_DOCUMENTO,CR_NUM_CUENTA,CR_NUM_PRODUCTO,CR_NUM_SUCURSAL,CR_NUM_EMPRESA,CR_FECHA_DESEMBOLSO,CR_FECHA_VENCIMIENTO,CR_PERIODICIDAD,CR_NUMERO_CUOTAS,CR_MONTO_CREDITO,CR_MONTO_DESEMBOLSADO,CR_MONTO_CUENTA,CR_MONTO_COMISION,CR_IVA_COMISION,CR_MONTO_SEGURO,CR_IVA_SEGURO,CR_OTROS_CARGOS,CR_IVA_OTROS_CARGOS,CR_TASA_INT,CR_TASA_INT_SINIVA,CR_TASA_COMISION_SINIVA,CR_TASA_MORA_SINIVA,CR_TASA_IVA,CR_NUM_DIAS,CR_DIAS_MORA,CR_STATUS,CR_FONDEADOR,CR_NUM_EJECUTIVO,CR_FECHA_LIQUIDACION,CR_FECHA_ULT_ACTUALIZ,CR_FECHA_ULT_PAGO,CR_VALOR_CUOTA,CR_BANCO_DESEMBOLSO,CR_MONTO_CUENTA_CONGELADA,CR_APLICA_SALDO,cr_fechamov"
                + " FROM d_credito WHERE cr_num_cliente= ? and cr_num_credito= ?)";
        Connection cn = null;
        PreparedStatement ps = null;
        try { 
            cn = getCWConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCredito);
            myLogger.debug(ps.toString());
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("eliminaCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public void eliminaCredito(int numEquipo, int numCredito, boolean conexionODS) throws ClientesDBException, ClientesException {
        String query = "DELETE FROM d_credito WHERE cr_num_cliente= ? and cr_num_credito= ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try { 
            if(conexionODS){
                cn = getCODSConnection();
            }else{
                cn = getCWConnection();
            }
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCredito);
            myLogger.debug(ps.toString());
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("eliminaCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public void bajaLogicaCredito(int numEquipo, int numCredito) {
        String query = "update d_credito set cr_status = ? WHERE cr_num_cliente = ? and cr_num_credito = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try { 
            cn = getCODSConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, 5);
            ps.setInt(2, numEquipo);
            ps.setInt(3, numCredito);
            myLogger.debug(ps.toString());
            int result = ps.executeUpdate();
            myLogger.debug("Registros afectados: " + result);
        } catch (SQLException sqle) {
            myLogger.error("Error de conexión ODS...");
            myLogger.error("eliminaCredito", sqle);
        } catch (Exception e) {
            myLogger.error("Error al realizar baja logica credito ODS...");
            myLogger.error("eliminaCredito", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("Error de conexión ODS...");
                myLogger.error("eliminaCredito", sqle);
            }
        }
    }    
    
    public void updateEstatusCredito(CreditoCartVO credito) throws ClientesDBException, ClientesException {

        String query = "UPDATE d_credito SET cr_status=4 WHERE cr_num_cliente= ? and cr_num_credito= ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(1, credito.getNumCliente());
            ps.setDouble(2, credito.getNumCredito());
            myLogger.debug("Ejecutando " + query);
            myLogger.debug("Parametros [" + credito.getNumCliente() + "," + credito.getNumCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateEstatusCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public void actCreditoCierre(SaldoIBSVO saldo, CreditoCartVO credito) throws ClientesException {

        Connection cn = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_credito SET cr_num_dias= ?,cr_dias_mora= ?,cr_status= ?,cr_fecha_ult_actualiz= ?,cr_fecha_ult_pago= ?,cr_monto_cuenta= ?,cr_monto_cuenta_congelada= ? "
                + "WHERE cr_num_cliente= ? AND cr_num_credito= ?";

        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, saldo.getDiasTranscurridos());
            ps.setInt(2, saldo.getDiasMora());
            ps.setInt(3, saldo.getEstatus());
            ps.setDate(4, saldo.getFechaGeneracion());
            ps.setDate(5, saldo.getFechaUltimoPago());
            ps.setDouble(6, saldo.getSaldoBucket());
            ps.setDouble(7, credito.getMontoCuentaCongelada());
            ps.setInt(8, saldo.getIdClienteSICAP());
            ps.setInt(9, saldo.getCredito());

            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("actCreditoCierre", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actCreditoCierre", e);
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
    }

    public double getMontoCuenta(int numEquipo, int numCiclo) throws ClientesException {
        double monto = 0;
        Connection conn = null;
        String query = "SELECT cr_monto_cuenta FROM d_credito WHERE cr_num_cliente=? AND cr_num_solicitud=?";

        try {
            conn = getCWConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            ResultSet rs = ps.executeQuery();
            myLogger.debug(ps.toString());
            if (rs.next()) {
                monto = rs.getDouble("cr_monto_cuenta");
            }
        } catch (SQLException sqle) {
            myLogger.error("getMontoCuenta", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMontoCuenta", e);
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
        return monto;
    }

    public void eliminaMontoCuenta(int numEquipo, int numCiclo) throws ClientesException {
        Connection conn = null;
        String query = "UPDATE d_credito SET cr_monto_cuenta=0 WHERE cr_num_cliente=? AND cr_num_solicitud=?";

        try {
            conn = getCWConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            ps.executeUpdate();
            myLogger.debug("Ejecutando: " + ps.toString());
        } catch (SQLException sqle) {
            myLogger.error("eliminaMontoCuenta", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaMontoCuenta", e);
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

    public void updateCreditoFondeador(Connection con, CreditoCartVO creditoVO) throws ClientesException {
        updateCreditoFondeador(con, creditoVO, true);
    }

    public void updateCreditoFondeador(Connection con, CreditoCartVO creditoVO, boolean cierraConexion) throws ClientesException {
        PreparedStatement ps = null;
        String query = "update d_credito"
                + " set cr_fondeador = ?"
                + " where cr_num_cliente= ?"
                + " and cr_num_credito = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setDouble(1, creditoVO.getFondeador());
            ps.setInt(2, creditoVO.getNumCliente());
            ps.setInt(3, creditoVO.getNumCredito());
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateCreditoInterciclo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCreditoInterciclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            if (cierraConexion) {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    throw new ClientesDBException(e.getMessage());
                }

            }

        }
    }

    public void updateCreditoFondeador(CreditoCartVO creditoVO) throws ClientesException {
        PreparedStatement ps = null;
        Connection con = null;
        String query = "update d_credito"
                + " set cr_fondeador = ?"
                + " where cr_num_cliente= ?"
                + " and cr_num_credito = ?";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, creditoVO.getFondeador());
            ps.setInt(2, creditoVO.getNumCliente());
            ps.setInt(3, creditoVO.getNumCredito());
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateCreditoInterciclo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCreditoInterciclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    public void updateCreditoInterciclo(Connection con, CreditoCartVO creditoVO) throws ClientesException {

        PreparedStatement ps = null;
        String query = "UPDATE d_credito SET cr_monto_credito=?,cr_monto_desembolsado=?,cr_valor_cuota=?,cr_monto_cuenta_congelada=? WHERE cr_num_cliente=? and cr_num_credito=?;";
        try {
            ps = con.prepareStatement(query);
            ps.setDouble(1, creditoVO.getMontoCredito());
            ps.setDouble(2, creditoVO.getMontoDesembolsado());
            ps.setDouble(3, creditoVO.getValorCuota());
            ps.setDouble(4, creditoVO.getMontoCuentaCongelada());
            ps.setInt(5, creditoVO.getNumCliente());
            ps.setInt(6, creditoVO.getNumCredito());
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateCreditoInterciclo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCreditoInterciclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }

    public ArrayList<CreditoCartVO> getArrayCreditoCastigo() throws ClientesException {

        CreditoCartVO creditos = null;
        ArrayList<CreditoCartVO> array = new ArrayList<CreditoCartVO>();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            String query = "SELECT * FROM d_credito WHERE cr_dias_mora>=? AND cr_status=?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, ClientesConstants.DIAS_MORA_CASTIGO);
            ps.setInt(2, ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO);
            myLogger.debug("ejecutando " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                creditos = new CreditoCartVO();
                creditos.setNumCliente(rs.getInt("cr_num_cliente"));
                creditos.setNumSolicitud(rs.getInt("cr_num_solicitud"));
                creditos.setNumCredito(rs.getInt("cr_num_credito"));
                creditos.setReferencia(rs.getString("cr_referencia"));
                creditos.setNumDocumento(rs.getString("cr_num_documento"));
                creditos.setNumCuenta(rs.getInt("cr_num_cuenta"));
                creditos.setNumProducto(rs.getInt("cr_num_producto"));
                creditos.setNumSucursal(rs.getInt("cr_num_sucursal"));
                creditos.setNumEmpresa(rs.getInt("cr_num_empresa"));
                creditos.setFechaDesembolso(rs.getDate("cr_fecha_desembolso"));
                creditos.setFechaVencimiento(rs.getDate("cr_fecha_vencimiento"));
                creditos.setFechaLiquidacion(rs.getDate("cr_fecha_liquidacion"));
                creditos.setPeriodicidad(rs.getInt("cr_periodicidad"));
                creditos.setNumCuotas(rs.getInt("cr_numero_cuotas"));
                creditos.setMontoCredito(rs.getDouble("cr_monto_credito"));
                creditos.setMontoDesembolsado(rs.getDouble("cr_monto_desembolsado"));
                creditos.setMontoCuenta(rs.getDouble("cr_monto_cuenta"));
                creditos.setMontoComision(rs.getDouble("cr_monto_comision"));
                creditos.setMontoIvaComision(rs.getDouble("cr_iva_comision"));
                creditos.setMontoSeguro(rs.getDouble("cr_monto_seguro"));
                creditos.setMontoIvaSeguro(rs.getDouble("cr_iva_seguro"));
                creditos.setMontoOtrosCargos(rs.getDouble("cr_otros_cargos"));
                creditos.setMontoIvaOtrosCargos(rs.getDouble("cr_iva_otros_cargos"));
                creditos.setTasaInteres(rs.getDouble("cr_tasa_int"));
                creditos.setTasaInteresSIVA(rs.getDouble("cr_tasa_int_siniva"));
                creditos.setTasaComision(rs.getDouble("cr_tasa_comision_siniva"));
                creditos.setTasaMora(rs.getDouble("cr_tasa_mora_siniva"));
                creditos.setTasaIVA(rs.getDouble("cr_tasa_iva"));
                creditos.setNumDias(rs.getInt("cr_num_dias"));
                creditos.setNumDiasMora(rs.getInt("cr_dias_mora"));
                creditos.setFondeador(rs.getInt("cr_fondeador"));
                creditos.setStatus(rs.getInt("cr_status"));
                creditos.setNumEjecutivo(rs.getInt("cr_num_ejecutivo"));
                creditos.setFechaUltimaActualizacion(rs.getDate("cr_fecha_ult_actualiz"));
                creditos.setFechaUltimoPago(rs.getDate("cr_fecha_ult_pago"));
                creditos.setValorCuota(rs.getDouble("cr_valor_cuota"));
                creditos.setBancoDesembolso(rs.getInt("cr_banco_desembolso"));
                creditos.setMontoCuentaCongelada(rs.getDouble("cr_monto_cuenta_congelada"));
                array.add(creditos);
            }
        } catch (SQLException sqle) {
            myLogger.error("getArrayCreditoCastigo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getArrayCreditoCastigo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
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

        return array;
    }
}

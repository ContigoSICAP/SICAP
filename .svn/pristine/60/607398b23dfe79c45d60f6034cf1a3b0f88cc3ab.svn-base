package com.sicap.clientes.dao.cartera;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import org.apache.log4j.Logger;

public class TablaAmortDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(TablaAmortDAO.class);

    public TablaAmortVO[] getElementos(int idCliente, int idSolicitud, int idAmortizacion) throws ClientesException {
        return getElementos(idCliente, idSolicitud, 0, idAmortizacion);
    }

    public TablaAmortVO[] getElementos(int numCliente, int numCredito, int idDisposicion, int idAmortizacion) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS, TA_CAPITAL_ANTICIPADO, ta_incremento_capital FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + idDisposicion + "," + idAmortizacion + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = idAmortizacion;
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                tabla.capitalAnticipado = rs.getDouble("TA_CAPITAL_ANTICIPADO");
                tabla.incrementoCapital =rs.getDouble("ta_incremento_capital");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getTablaAmort", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTablaAmort", e);
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

        return elementos;
    }

    public TablaAmortVO[] getElementosVigMorVen(int numCliente, int numCredito, int idDisposicion, int idAmortizacion) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,TA_CAPITAL_PAGADO,TA_COMISION_INICIAL,TA_IVA_COMISION, "
                + "TA_INTERES,TA_IVA_INTERES,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,TA_IVA_MORA_PAGADO, "
                + "TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO "
                + "FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ? AND TA_STATUS IN (1,2) "
                + "AND TA_PAGADO = 'N'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + idDisposicion + "," + idAmortizacion + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = idAmortizacion;
                tabla.numDisposicion = idDisposicion;
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosVigMorVen", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosVigMorVen", e);
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

        return elementos;
    }

    public TablaAmortVO[] getElementosMorVen(int numCliente, int numCredito, int idDisposicion, int idAmortizacion) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ? AND TA_STATUS IN (2,3) AND TA_PAGADO = 'N'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + idDisposicion + "," + idAmortizacion + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = idAmortizacion;
                tabla.numDisposicion = idDisposicion;
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosMorVen", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosMorVen", e);
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

        return elementos;
    }

    public TablaAmortVO[] getElementosMulta(int numCliente, int numCredito, int idDisposicion, int idAmortizacion) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ? AND TA_STATUS IN (2,3) AND TA_PAGADO = 'N' AND TA_MULTA > 0";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + idDisposicion + "," + idAmortizacion + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = idAmortizacion;
                tabla.numDisposicion = idDisposicion;
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
// Unicamente se añade cuando deben multa
                if (tabla.multa > tabla.multaPagado) {
                    array.add(tabla);
                }
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosMulta", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosMulta", e);
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

        return elementos;
    }

    public TablaAmortVO[] getDivPago(int numCliente, int numCredito, Date fechaFin) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO , TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + " AND TA_STATUS IN (1,2,3) AND TA_PAGADO = 'N' AND TA_FECHA_PAGO <= ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setDate(3, fechaFin);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivPago", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivPago", e);
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

        return elementos;
    }

    public TablaAmortVO getDivPago(int numCliente, int numCredito, int numPago) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO , TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + " AND TA_NUMPAGO = ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numPago);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + numPago + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivPago", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivPago", e);
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

        return tabla;
    }
    
    public TablaAmortVO getDivPagoFecha(int numCliente, int numCredito) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, "+
                "TA_COMISION_INICIAL, TA_IVA_COMISION, TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, "+
                "TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO , TA_STATUS FROM D_TABLA_AMORTIZACION " +
                "LEFT JOIN local_clientes_cec.d_saldos ON(ta_numcliente=ib_numclientesicap AND ta_numcredito=ib_credito) " +
                "WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND ta_numpago=ib_num_cuotas_trancurridas+1 ";
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                System.out.println("TABLA DAO FECHAVIG: "+tabla.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivPago", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivPago", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return tabla;
    }

    public TablaAmortVO[] getDivPagoNoVigente(int numCliente, int numCredito) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO , TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + //" AND TA_STATUS IN (0,1) AND TA_PAGADO = 'N' ORDER BY TA_NUMPAGO " ;
                " AND TA_STATUS IN (0,1,3) AND TA_PAGADO = 'N' ORDER BY TA_NUMPAGO ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivPagoNoVigente", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivPagoNoVigente", e);
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

        return elementos;
    }

     /**
     * JECB 01/10/2017
     * Se obtiene todos los registros de la tabla de amortizacion que no esten vigentes
     * @param numCliente numero del cliente 
     * @param numCredito numero de credito
     * @param plazoAdicional plazo del adicional
     * @return
     * @throws ClientesException 
     */
    public TablaAmortVO[] getDivPagoNoVigenteAdicional(int numCliente, int numCredito, int plazoAdicional) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO , TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + //" AND TA_STATUS IN (0,1) AND TA_PAGADO = 'N' ORDER BY TA_NUMPAGO " ;
                " AND TA_STATUS IN (0,1,3) AND TA_PAGADO = 'N' AND TA_NUMPAGO >= ? ORDER BY TA_NUMPAGO ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, plazoAdicional);
            myLogger.debug("getDivPagoNoVigenteAdicional Ejecutando = " + query);
            myLogger.debug("getDivPagoNoVigenteAdicional Parametros = [" + numCliente + "," + numCredito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortVO[array.size()];
                array.toArray(elementos);
                
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivPagoNoVigenteAdicional", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivPagoNoVigenteAdicional", e);
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

        return elementos;
    }
    
    public TablaAmortVO getDivVigente(int numCliente, int numCredito) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + " AND TA_STATUS in (1,2,3) AND TA_PAGADO = 'N'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivVigente", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivVigente", e);
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

        return tabla;
    }
        public TablaAmortizacionVO getElementos(int idCliente, int numCiclo )throws ClientesException {
        TablaAmortizacionVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementos[] = null;

        String query = "SELECT TA_NO_PAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_MONTO_PAGAR, TA_PAGADO FROM D_TABLA_AMORTIZACION WHERE TA_ID_CLIENTE = ? AND TA_ID_SOLICITUD = ? "
                + "AND TA_NO_PAGO= 0";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "," + numCiclo+ "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortizacionVO();
                tabla.idCliente = idCliente;
                tabla.numPago = rs.getInt("TA_NO_PAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.comisionInicial = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.pagado = rs.getInt("TA_PAGADO");
            }            
        } catch (SQLException sqle) {
            myLogger.error("getElementos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementos", e);
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

        return tabla;
    }

    public TablaAmortVO getDivVigenteProv(int numCliente, int numCredito, Date fechaPago) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + " AND TA_STATUS = 1 AND TA_FECHA_PAGO = ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setDate(3, fechaPago);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + fechaPago.toString() + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getDivVigenteProv", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDivVigenteProv", e);
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

        return tabla;
    }

    public TablaAmortVO getSiguienteDiv(int numCliente, int numCredito, int numPago) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + " AND TA_NUMPAGO = ? AND TA_PAGADO = 'N'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numPago);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + numPago + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSiguienteDiv", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSiguienteDiv", e);
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

        return tabla;
    }

    public boolean esVencimiento(int numCliente, int numCredito, Date fechaFin) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;
        boolean esVencimiento = false;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_FECHA_PAGO = ? AND TA_PAGADO = 'N'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setDate(3, fechaFin);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + fechaFin + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                esVencimiento = true;
                myLogger.debug("es vencimiento es verdadero para cliente " + tabla.numCliente);
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("esVencimiento", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("esVencimiento", e);
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

        return esVencimiento;
    }

    public boolean esVencimientoProv(int numCliente, int numCredito, Date fechaFin) throws ClientesException {
        TablaAmortVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortVO> array = new ArrayList<TablaAmortVO>();
        TablaAmortVO elementos[] = null;
        boolean esVencimiento = false;

        String query = "SELECT TA_TPO_AMORTIZACION, TA_NUMDISPOSICION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, "
                + "TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_PAGADO, TA_STATUS FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_FECHA_PAGO = ? ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setDate(3, fechaFin);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + fechaFin + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortVO();
                tabla.numCliente = numCliente;
                tabla.numCredito = numCredito;
                tabla.tipoAmortizacion = rs.getInt("TA_TPO_AMORTIZACION");
                tabla.numDisposicion = rs.getInt("TA_NUMDISPOSICION");
                tabla.numPago = rs.getInt("TA_NUMPAGO");
                tabla.fechaPago = rs.getDate("TA_FECHA_PAGO");
                tabla.saldoInicial = rs.getDouble("TA_SALDO_INICIAL");
                tabla.abonoCapital = rs.getDouble("TA_ABONO_CAPITAL");
                tabla.saldoCapital = rs.getDouble("TA_SALDO_CAPITAL");
                tabla.capitalPagado = rs.getDouble("TA_CAPITAL_PAGADO");
                tabla.comision = rs.getDouble("TA_COMISION_INICIAL");
                tabla.ivaComision = rs.getDouble("TA_IVA_COMISION");
                tabla.interes = rs.getDouble("TA_INTERES");
                tabla.ivaInteres = rs.getDouble("TA_IVA_INTERES");
                tabla.interesPagado = rs.getDouble("TA_INTERES_PAGADO");
                tabla.ivaInteresPagado = rs.getDouble("TA_IVA_INTERES_PAGADO");
                tabla.intMoratorio = rs.getDouble("TA_MORA");
                tabla.ivaIntMoratorio = rs.getDouble("TA_IVA_MORA");
                tabla.intMoratorioPagado = rs.getDouble("TA_MORA_PAGADO");
                tabla.ivaIntMoratorioPagado = rs.getDouble("TA_IVA_MORA_PAGADO");
                tabla.multa = rs.getDouble("TA_MULTA");
                tabla.ivaMulta = rs.getDouble("TA_IVA_MULTA");
                tabla.multaPagado = rs.getDouble("TA_MULTA_PAGADO");
                tabla.ivaMultaPagado = rs.getDouble("TA_IVA_MULTA_PAGADO");
                tabla.montoPagar = rs.getDouble("TA_MONTO_PAGAR");
                tabla.totalPagado = rs.getDouble("TA_MONTO_PAGADO");
                tabla.status = rs.getInt("TA_STATUS");
                tabla.pagado = rs.getString("TA_PAGADO");
                array.add(tabla);
            }
            if (array.size() > 0) {
                esVencimiento = true;
                myLogger.debug("es vencimiento es verdadero para cliente " + tabla.numCliente);
                elementos = new TablaAmortVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("esVencimientoProv", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("esVencimientoProv", e);
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

        return esVencimiento;
    }

    public int addTablaAmort(TablaAmortVO tabla) throws ClientesException {

        String query = "INSERT INTO D_TABLA_AMORTIZACION (TA_NUMCLIENTE, TA_NUMCREDITO, TA_NUMDISPOSICION, TA_TPO_AMORTIZACION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO,"
                + "TA_COMISION_INICIAL, TA_IVA_COMISION, TA_INTERES, TA_IVA_INTERES, TA_INTERES_ACUM, TA_IVA_INTERES_ACUM, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, "
                + "TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO,TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_STATUS, "
                + "TA_PAGADO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, tabla.numCliente);
            ps.setInt(param++, tabla.numCredito);
            ps.setInt(param++, tabla.numDisposicion);
            ps.setInt(param++, tabla.tipoAmortizacion);
            ps.setInt(param++, tabla.numPago);
            ps.setDate(param++, tabla.fechaPago);
            ps.setDouble(param++, tabla.saldoInicial);
            ps.setDouble(param++, tabla.abonoCapital);
            ps.setDouble(param++, tabla.saldoCapital);
            ps.setDouble(param++, tabla.capitalPagado); //10
            ps.setDouble(param++, tabla.comision);
            ps.setDouble(param++, tabla.ivaComision);
            ps.setDouble(param++, tabla.interes);
            ps.setDouble(param++, tabla.ivaInteres);
            ps.setDouble(param++, tabla.interesAcum);
            ps.setDouble(param++, tabla.ivaInteresAcum);
            ps.setDouble(param++, tabla.interesPagado);
            ps.setDouble(param++, tabla.ivaInteresPagado);
            ps.setDouble(param++, tabla.intMoratorio);
            ps.setDouble(param++, tabla.ivaIntMoratorio); //20
            ps.setDouble(param++, tabla.intMoratorioPagado);
            ps.setDouble(param++, tabla.ivaIntMoratorioPagado);
            ps.setDouble(param++, tabla.multa);
            ps.setDouble(param++, tabla.ivaMulta);
            ps.setDouble(param++, tabla.multaPagado);
            ps.setDouble(param++, tabla.ivaMultaPagado);
            ps.setDouble(param++, tabla.montoPagar);
            ps.setDouble(param++, tabla.totalPagado);
            ps.setInt(param++, tabla.status);
            ps.setString(param++, tabla.pagado); //30
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Para = " + tabla);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addTablaAmort", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addTablaAmort", e);
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

    public void delTablaAmortizacion(int numCliente, int numCredito, int idAmortizacion) throws ClientesException {

        delTablaAmortizacion(numCliente, numCredito, 0, idAmortizacion);

    }

    public void delTablaAmortizacion(int numCliente, int numCredito, int idDisposicion, int idAmortizacion) throws ClientesException {

        String query = "DELETE FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ?";

        Connection cn = null;

        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);

            //myLogger.debug("Ejecutando = "+query);
            //myLogger.debug("Parametros = ["+idCliente+","+idSolicitud+","+idDisposicion+","+idAmortizacion+"]");
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("delTablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delTablaAmortizacion", e);
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

    public int updateSaldosTablaAmort(TablaAmortVO tabla) throws ClientesException {

        String query = "UPDATE D_TABLA_AMORTIZACION SET TA_CAPITAL_PAGADO = ?, TA_INTERES_PAGADO = ? , TA_IVA_INTERES_PAGADO = ?, TA_MORA = ?, TA_IVA_MORA = ?, TA_MORA_PAGADO = ?, "
                + " TA_IVA_MORA_PAGADO = ? , TA_MULTA = ?, TA_IVA_MULTA = ?, TA_MULTA_PAGADO = ?, TA_IVA_MULTA_PAGADO = ?,  TA_MONTO_PAGAR = ?, TA_MONTO_PAGADO = ?, "
                + " TA_STATUS = ?, TA_PAGADO = ? "
                + " WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_NUMPAGO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(param++, tabla.capitalPagado);
            ps.setDouble(param++, tabla.interesPagado);
            ps.setDouble(param++, tabla.ivaInteresPagado);
            ps.setDouble(param++, tabla.intMoratorio);
            ps.setDouble(param++, tabla.ivaIntMoratorio);
            ps.setDouble(param++, tabla.intMoratorioPagado);
            ps.setDouble(param++, tabla.ivaIntMoratorioPagado);
            ps.setDouble(param++, tabla.multa);
            ps.setDouble(param++, tabla.ivaMulta);
            ps.setDouble(param++, tabla.multaPagado);
            ps.setDouble(param++, tabla.ivaMultaPagado);
            ps.setDouble(param++, tabla.montoPagar);
            ps.setDouble(param++, tabla.totalPagado);
            ps.setInt(param++, tabla.status);
            ps.setString(param++, tabla.pagado);
            ps.setInt(param++, tabla.numCliente);
            ps.setInt(param++, tabla.numCredito);
            ps.setInt(param++, tabla.numPago);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [Cliente:" + tabla.numCliente + ", Credito: " + tabla.numCredito + ", NoPago:" + tabla.numPago 
                         + " Capital pagado: "+tabla.capitalPagado+", Interes Pagado: "+tabla.interesPagado+", InteresPagado: "+tabla.ivaInteresPagado+", "
                         + "IvaInteresPAgado: "+tabla.ivaInteresPagado+", IvaIntMoratorio:"+ tabla.ivaIntMoratorio +", IntMoratorioPagado: "+tabla.intMoratorioPagado
                         + "IvaIntMoratorioPAgado: "+tabla.ivaIntMoratorioPagado+", Multa: "+ tabla.multa+", IvaMulta: "+ tabla.ivaMulta+", "
                         + "MultaPAgada: "+tabla.multaPagado+", IvaMultaPAgada: "+tabla.ivaMultaPagado+", MontoPAgar: "+tabla.montoPagar+","
                         + "TotalPagado: "+tabla.totalPagado+ ", Estatus: "+tabla.status+", Pagado: "+tabla.pagado+"]");
            res = ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updateSaldosTablaAmort", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSaldosTablaAmort", e);
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

    public int updateStatusVenTablaAmort(TablaAmortVO tabla) throws ClientesException {

        String query = "UPDATE D_TABLA_AMORTIZACION SET TA_STATUS = ?"
                + " WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_PAGADO = 'N' AND TA_STATUS = 2";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, tabla.status);
            //ps.setString(param++, tabla.pagado);
            ps.setInt(param++, tabla.numCliente);
            ps.setInt(param++, tabla.numCredito);
            res = ps.executeUpdate();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + tabla.numCliente + "," + tabla.numCredito + "]");

        } catch (SQLException sqle) {
            myLogger.error("updateStatusVenTablaAmort", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateStatusVenTablaAmort", e);
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

    public void respaldaTablaAmortizacion(int numCliente, int numCredito) throws ClientesException {

        String query = "insert into D_TABLA_AMORTIZACION_DEL (TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,TA_CAPITAL_PAGADO,TA_COMISION_INICIAL,TA_IVA_COMISION,TA_INTERES,TA_IVA_INTERES,TA_INTERES_ACUM,TA_IVA_INTERES_ACUM,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,TA_IVA_MORA_PAGADO,TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO,TA_CAPITAL_ANTICIPADO,ta_entiempo,ta_incremento_capital,ta_quita_interes,ta_quita_iva_interes,ta_fechamov)"
                + " (SELECT TA_NUMCLIENTE,TA_NUMCREDITO,TA_NUMDISPOSICION,TA_TPO_AMORTIZACION,TA_NUMPAGO,TA_FECHA_PAGO,TA_SALDO_INICIAL,TA_ABONO_CAPITAL,TA_SALDO_CAPITAL,TA_CAPITAL_PAGADO,TA_COMISION_INICIAL,TA_IVA_COMISION,TA_INTERES,TA_IVA_INTERES,TA_INTERES_ACUM,TA_IVA_INTERES_ACUM,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,TA_MORA,TA_IVA_MORA,TA_MORA_PAGADO,TA_IVA_MORA_PAGADO,TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO,TA_CAPITAL_ANTICIPADO,ta_entiempo,ta_incremento_capital,ta_quita_interes,ta_quita_iva_interes,ta_fechamov"
                + " FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getCWConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("delTablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delTablaAmortizacion", e);
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

    public void delTablaAmortizacion(int numCliente, int numCredito, boolean conexionODS) throws ClientesException {

        String query = "DELETE FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                cn = getCODSConnection();
            }else{
                cn = getCWConnection();
            }
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("delTablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delTablaAmortizacion", e);
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

    public ArrayList<TablaAmortVO> getTablaAmortizacion(GrupoVO grupo, Connection conn) throws ClientesException {

        String query = "SELECT TA_NUMPAGO,TA_FECHA_PAGO,TA_ABONO_CAPITAL,TA_CAPITAL_PAGADO,TA_CAPITAL_ANTICIPADO,TA_INTERES,TA_IVA_INTERES,TA_INTERES_PAGADO,TA_IVA_INTERES_PAGADO,"
                + "TA_MULTA,TA_IVA_MULTA,TA_MULTA_PAGADO,TA_IVA_MULTA_PAGADO,TA_MONTO_PAGAR,TA_MONTO_PAGADO,TA_STATUS,TA_PAGADO,ta_quita_interes,ta_quita_iva_interes,ta_incremento_capital "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ?";
        Connection cn = null;
        PreparedStatement ps = null;
        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        ResultSet res = null;

        try {
            if(conn == null){
                cn = getCWConnection();
                ps = cn.prepareStatement(query);
           } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, grupo.idGrupo);
            ps.setInt(2, grupo.idGrupoIBS);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + grupo.idGrupo + "," + grupo.idGrupoIBS + "]");
            res = ps.executeQuery();

            while (res.next()) {
                TablaAmortVO taVO = new TablaAmortVO(res.getInt("TA_NUMPAGO"), res.getDate("TA_FECHA_PAGO"), res.getDouble("TA_ABONO_CAPITAL"), res.getDouble("TA_CAPITAL_PAGADO"),
                        res.getDouble("TA_INTERES"), res.getDouble("TA_IVA_INTERES"), res.getDouble("TA_INTERES_PAGADO"), res.getDouble("TA_IVA_INTERES_PAGADO"),
                        res.getDouble("TA_MULTA"), res.getDouble("TA_IVA_MULTA"), res.getDouble("TA_MULTA_PAGADO"), res.getDouble("TA_IVA_MULTA_PAGADO"),
                        res.getDouble("TA_MONTO_PAGAR"), res.getDouble("TA_MONTO_PAGADO"), res.getInt("TA_STATUS"), res.getString("TA_PAGADO"), res.getDouble("ta_quita_interes"), res.getDouble("ta_quita_iva_interes"));
                taVO.capitalAnticipado = res.getDouble("TA_CAPITAL_ANTICIPADO");
                taVO.incrementoCapital = res.getDouble("ta_incremento_capital");
                arrTabla.add(taVO);
            }

        } catch (SQLException sqle) {
            myLogger.error("getTablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTablaAmortizacion", e);
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
        return arrTabla;
    }

    public double getTotalPagado(Integer numcliente, Integer numcredito) throws ClientesException {
        int param = 1;
        double total_pagado = 0;
        String query = "SELECT SUM(TA_MONTO_PAGADO) AS TOTAL_PAGADO FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ?";
        Connection cn = null;
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numcliente);
            ps.setInt(param++, numcredito);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total_pagado = rs.getDouble("TOTAL_PAGADO");
            }
        } catch (SQLException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getTotalPagado", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return total_pagado;
    }

    public double getTotalPagado(int numcliente, int numcredito) throws ClientesException {
        int param = 1;
        double total_pagado = 0;
        String query = "SELECT SUM(TA_MONTO_PAGADO) AS TOTAL_PAGADO FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? ";
        Connection cn = null;
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numcliente);
            ps.setInt(param++, numcredito);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total_pagado = rs.getDouble("TOTAL_PAGADO");
            }
        } catch (SQLException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getTotalPagado", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return total_pagado;
    }

    public double getTotalPorpagar(int numcliente, int numcredito) throws ClientesException {
        int param = 1;
        double total_pagar = 0;
        String query = "SELECT SUM(TA_MONTO_PAGAR) AS TOTAL_PAGAR FROM D_TABLA_AMORTIZACION WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? "
                + "AND TA_PAGADO = 'N'";
        Connection cn = null;
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numcliente);
            ps.setInt(param++, numcredito);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total_pagar = rs.getDouble("TOTAL_PAGAR");
            }
        } catch (SQLException exc) {
            myLogger.error("getTotalPorpagar", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getTotalPorpagar", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getTotalPorpagar", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return total_pagar;
    }

    public ArrayList<TablaAmortVO> saldosTablaCierre(SaldoIBSVO saldo) throws ClientesException {

        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numPago,ta_fecha_pago,ta_abono_capital,ta_saldo_capital,ta_capital_pagado,ta_interes,ta_iva_interes,ta_interes_pagado,ta_iva_interes_pagado,ta_mora,ta_iva_mora,ta_mora_pagado,ta_iva_mora_pagado,"
                + "ta_multa,ta_iva_multa,ta_multa_pagado,ta_iva_multa_pagado,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado,"
                + "(ta_abono_capital-ta_capital_pagado) AS capitalPagar,(ta_interes-ta_interes_pagado) AS interesPagar,(ta_iva_interes-ta_iva_interes_pagado) ivaInteresPagar,"
                + "(ta_mora-ta_mora_pagado) AS moraPagar,(ta_iva_mora-ta_iva_mora_pagado) AS ivaMoraPagar,(ta_multa-ta_multa_pagado) AS multaPagar,(ta_iva_multa-ta_iva_multa_pagado) AS ivaMultaPagar "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_status in (1,2) AND ta_pagado='N' order by ta_numPago;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrTabla.add(new TablaAmortVO(res.getInt("ta_numPago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"), res.getDouble("ta_saldo_capital"), res.getDouble("ta_capital_pagado"), res.getDouble("ta_interes"), res.getDouble("ta_iva_interes"), res.getDouble("ta_interes_pagado"), res.getDouble("ta_iva_interes_pagado"), res.getDouble("ta_mora"),
                        res.getDouble("ta_iva_mora"), res.getDouble("ta_mora_pagado"), res.getDouble("ta_iva_mora_pagado"), res.getDouble("ta_multa"), res.getDouble("ta_iva_multa"), res.getDouble("ta_multa_pagado"), res.getDouble("ta_iva_multa_pagado"), res.getDouble("ta_monto_pagar"), res.getDouble("ta_monto_pagado"), res.getInt("ta_status"), res.getString("ta_pagado"),
                        res.getDouble("capitalPagar"), res.getDouble("interesPagar"), res.getDouble("ivaInteresPagar"), res.getDouble("moraPagar"), res.getDouble("ivaMoraPagar"), res.getDouble("multaPagar"), res.getDouble("ivaMultaPagar")));
            }
        } catch (SQLException sqle) {
            myLogger.error("saldosTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("saldosTablaCierre", e);
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

    public void liquidaTablaCierre(SaldoIBSVO saldo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_capital_pagado=ta_abono_capital,ta_interes_pagado=ta_interes,ta_iva_interes_pagado=ta_iva_interes,ta_mora_pagado=ta_mora,ta_iva_mora_pagado=ta_iva_mora,"
                + "ta_multa_pagado=ta_multa,ta_iva_multa_pagado=ta_iva_multa,ta_monto_pagado=ta_monto_pagado+ta_monto_pagar,ta_monto_pagar=0,ta_pagado='S' WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_pagado='N'";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("liquidaTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("liquidaTablaCierre", e);
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
    }

    public void liquidaStatusTablaCierre(SaldoIBSVO saldo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_status=1 WHERE ta_numcliente= ? AND ta_numcredito= ? and ta_status=0";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("liquidaStatusTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("liquidaStatusTablaCierre", e);
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
    }

    public TablaAmortVO getSaldosTablaCierre(SaldoIBSVO saldo) throws ClientesException {

        TablaAmortVO tabla = new TablaAmortVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT SUM(ta_abono_capital) AS abonocapital,SUM(ta_capital_pagado) AS capitalPag,SUM(ta_interes) AS interes,SUM(ta_interes_pagado) AS interesPag,SUM(ta_iva_interes) AS ivainteres,"
                + "SUM(ta_iva_interes_pagado) AS ivaInteresPag,SUM(ta_mora) AS mora,SUM(ta_mora_pagado) AS moraPag,SUM(ta_iva_mora) AS ivamora,SUM(ta_iva_mora_pagado) AS ivaMoraPag,SUM(ta_multa) AS multa,"
                + "SUM(ta_iva_multa) AS ivamulta,SUM(ta_multa_pagado) AS multaPag,SUM(ta_iva_multa_pagado) AS ivaMultaPag,SUM(ta_monto_pagado) AS montoPag "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ?";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            if (res.next()) {
                tabla.setAbonoCapital(res.getDouble("abonocapital"));
                tabla.setCapitalPagado(res.getDouble("capitalPag"));
                tabla.setInteres(res.getDouble("interes"));
                tabla.setInteresPagado(res.getDouble("interesPag"));
                tabla.setIvaInteres(res.getDouble("ivainteres"));
                tabla.setIvaInteresPagado(res.getDouble("ivaInteresPag"));
                tabla.setIntMoratorio(res.getDouble("mora"));
                tabla.setIntMoratorioPagado(res.getDouble("moraPag"));
                tabla.setIvaIntMoratorio(res.getDouble("ivamora"));
                tabla.setIvaIntMoratorioPagado(res.getDouble("ivaMoraPag"));
                tabla.setMulta(res.getDouble("multa"));
                tabla.setMultaPagado(res.getDouble("multaPag"));
                tabla.setIvaMulta(res.getDouble("ivamulta"));
                tabla.setIvaMultaPagado(res.getDouble("ivaMultaPag"));
                tabla.setTotalPagado(res.getDouble("montoPag"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldosTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosTablaCierre", e);
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
        return tabla;
    }

    public void actSaldoTablaCierre(SaldoIBSVO saldo, TablaAmortVO saldoTab) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_capital_pagado= ?,ta_interes_pagado= ?,ta_iva_interes_pagado= ?,ta_mora_pagado= ?,ta_iva_mora_pagado= ?, "
                + "ta_multa_pagado= ?,ta_iva_multa_pagado= ?,ta_monto_pagado= ?,ta_monto_pagar= ?,ta_status= ?,ta_pagado= ?,ta_entiempo= ? "
                + "WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_numpago= ?";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, saldoTab.getCapitalPagado());
            ps.setDouble(2, saldoTab.getInteresPagado());
            ps.setDouble(3, saldoTab.getIvaInteresPagado());
            ps.setDouble(4, saldoTab.getIntMoratorioPagado());
            ps.setDouble(5, saldoTab.getIvaIntMoratorioPagado());
            ps.setDouble(6, saldoTab.getMultaPagado());
            ps.setDouble(7, saldoTab.getIvaMultaPagado());
            ps.setDouble(8, saldoTab.getTotalPagado());
            ps.setDouble(9, saldoTab.getMontoPagar());
            ps.setInt(10, saldoTab.getStatus());
            ps.setString(11, saldoTab.getPagado());
            ps.setInt(12, saldoTab.getCompletaATiempo());
            ps.setInt(13, saldo.getIdClienteSICAP());
            ps.setInt(14, saldo.getCredito());
            ps.setInt(15, saldoTab.getNumPago());

            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + ", " + saldoTab.getNumPago() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("actSaldoTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actSaldoTablaCierre", e);
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
    }

    public void actStatusTablaCierre(SaldoIBSVO saldo, int numPago, int estatus) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_status= ? WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_numpago= ?";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, estatus);
            ps.setInt(2, saldo.getIdClienteSICAP());
            ps.setInt(3, saldo.getCredito());
            ps.setInt(4, numPago);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + estatus + ", " + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + ", " + numPago + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("actStatusTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actStatusTablaCierre", e);
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
    }

    public TablaAmortVO getSaldoSiguienteCierre(SaldoIBSVO saldo) throws ClientesException {

        TablaAmortVO tabla = new TablaAmortVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numpago,(ta_abono_capital-ta_capital_pagado) AS capital,(ta_interes-ta_interes_pagado) AS interes,(ta_iva_interes-ta_iva_interes_pagado) AS ivainteres,"
                + "(ta_monto_pagar-ta_monto_pagado) AS montopago,ta_fecha_pago "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_status=1 AND ta_pagado='N'";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            if (res.next()) {
                tabla.setNumPago(res.getInt("ta_numpago"));
                tabla.setAbonoCapital(res.getDouble("capital"));
                tabla.setInteres(res.getDouble("interes"));
                tabla.setIvaInteres(res.getDouble("ivainteres"));
                tabla.setMontoPagar(res.getDouble("montopago"));
                tabla.setFechaPago(res.getDate("ta_fecha_pago"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoSiguienteCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoSiguienteCierre", e);
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
        return tabla;
    }

    public TablaAmortVO getSaldoVencidoCierre(SaldoIBSVO saldo) throws ClientesException {

        TablaAmortVO tabla = new TablaAmortVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT COUNT(ta_numpago) AS cuotasVen,MAX(ta_numpago) AS ta_numpago,MIN(ta_fecha_pago) AS ta_fecha_pago,SUM(ta_abono_capital-ta_capital_pagado) AS capitalVen,SUM(ta_interes-ta_interes_pagado) AS interesVen,"
                + "SUM(ta_iva_interes-ta_iva_interes_pagado) AS ivaInteresVen,SUM(ta_mora-ta_mora_pagado) AS moraVen,SUM(ta_iva_mora-ta_iva_mora_pagado) AS ivaMoraVen,SUM(ta_multa-ta_multa_pagado) AS multaVen,"
                + "SUM(ta_iva_multa-ta_iva_multa_pagado) AS ivaMultaVen,SUM(ta_monto_pagar) AS totalVen "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_status=2 AND ta_pagado='N'";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            if (res.next()) {
                tabla.setCuotasVencidas(res.getInt("cuotasVen"));
                tabla.setNumPago(res.getInt("ta_numpago"));
                tabla.setFechaPago(res.getDate("ta_fecha_pago"));
                tabla.setCapitalVencido(res.getDouble("capitalVen"));
                tabla.setInteresVencido(res.getDouble("interesVen"));
                tabla.setIvaInteresVencido(res.getDouble("ivaInteresVen"));
                tabla.setMoraVencido(res.getDouble("moraVen"));
                tabla.setIvaMoraVencido(res.getDouble("ivaMoraVen"));
                tabla.setMultaVencido(res.getDouble("multaVen"));
                tabla.setIvaMultaVencido(res.getDouble("ivaMultaVen"));
                tabla.setMontoTotVencido(res.getDouble("totalVen"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoVencidoCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoVencidoCierre", e);
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
        return tabla;
    }

    public TablaAmortVO getSaldoPagarCierre(SaldoIBSVO saldo) throws ClientesException {

        TablaAmortVO tabla = new TablaAmortVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT SUM(ta_abono_capital-ta_capital_pagado) AS capitalPagar,SUM(ta_interes-ta_interes_pagado) AS interesPagar,SUM(ta_iva_interes-ta_iva_interes_pagado) AS ivaInteresPagar, "
                + "SUM(ta_mora-ta_mora_pagado) AS moraPagar,SUM(ta_iva_mora-ta_iva_mora_pagado) AS ivaMoraPagar,SUM(ta_multa-ta_multa_pagado) AS multaPagar,SUM(ta_iva_multa-ta_iva_multa_pagado) AS ivaMultaPagar,"
                + "SUM(ta_monto_pagar) AS montoPagar FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_pagado='N'";
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            if (res.next()) {
                tabla.setMontoCapital(res.getDouble("capitalPagar"));
                tabla.setMontoInteres(res.getDouble("interesPagar"));
                tabla.setMontoIvaInteres(res.getDouble("ivaInteresPagar"));
                tabla.setMontoInteresMora(res.getDouble("moraPagar"));
                tabla.setMontoIvaInteresMora(res.getDouble("ivaMoraPagar"));
                tabla.setMontoMulta(res.getDouble("multaPagar"));
                tabla.setMontoIvaMulta(res.getDouble("ivaMultaPagar"));
                tabla.setMontoPagar(res.getDouble("montoPagar"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getSaldoPagarCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldoPagarCierre", e);
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
        return tabla;
    }

    public TablaAmortVO getPago(int numCliente, int numCredito, int numPago) throws ClientesException {

        TablaAmortVO tabla = new TablaAmortVO();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT ta_numcliente,ta_numcredito,ta_numpago,ta_fecha_pago,ta_abono_capital,ta_capital_pagado,ta_interes,ta_iva_interes,ta_interes_pagado,ta_iva_interes_pagado,ta_mora,ta_iva_mora,ta_mora_pagado,"
                + "ta_iva_mora_pagado,ta_multa,ta_iva_multa,ta_multa_pagado,ta_iva_multa_pagado,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente = ? AND ta_numcredito = ? AND ta_numpago = ?";
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numPago);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + numPago + "]");
            rs = ps.executeQuery();
            while (rs.next()) {
                tabla.setNumCliente(rs.getInt("ta_numcliente"));
                tabla.setNumCredito(rs.getInt("ta_numcredito"));
                tabla.setNumPago(rs.getInt("ta_numpago"));
                tabla.setFechaPago(rs.getDate("ta_fecha_pago"));
                tabla.setAbonoCapital(rs.getDouble("ta_abono_capital"));
                tabla.setCapitalPagado(rs.getDouble("ta_capital_pagado"));
                tabla.setInteres(rs.getDouble("ta_interes"));
                tabla.setIvaInteres(rs.getDouble("ta_iva_interes"));
                tabla.setInteresPagado(rs.getDouble("ta_interes_pagado"));
                tabla.setIvaInteresPagado(rs.getDouble("ta_iva_interes_pagado"));
                tabla.setIntMoratorio(rs.getDouble("ta_mora"));
                tabla.setIvaIntMoratorio(rs.getDouble("ta_iva_mora"));
                tabla.setIntMoratorioPagado(rs.getDouble("ta_mora_pagado"));
                tabla.setIvaIntMoratorioPagado(rs.getDouble("ta_iva_mora_pagado"));
                tabla.setMulta(rs.getDouble("ta_multa"));
                tabla.setIvaMulta(rs.getDouble("ta_iva_multa"));
                tabla.setMultaPagado(rs.getDouble("ta_multa_pagado"));
                tabla.setIvaMultaPagado(rs.getDouble("ta_iva_multa_pagado"));
                tabla.setMontoPagar(rs.getDouble("ta_monto_pagar"));
                tabla.setTotalPagado(rs.getDouble("ta_monto_pagado"));
                tabla.setStatus(rs.getInt("ta_status"));
                tabla.setPagado(rs.getString("ta_pagado"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getPago", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPago", e);
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

        return tabla;
    }

    public ArrayList<TablaAmortVO> saldosPendientesTablaCierre(SaldoIBSVO saldo) throws ClientesException {

        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numPago,ta_fecha_pago,ta_abono_capital,ta_interes,ta_iva_interes,ta_mora,ta_iva_mora,ta_multa,ta_iva_multa,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado,"
                + "(ta_abono_capital-ta_capital_pagado) AS capitalPagar,(ta_interes-ta_interes_pagado) AS interesPagar,(ta_iva_interes-ta_iva_interes_pagado) ivaInteresPagar,"
                + "(ta_mora-ta_mora_pagado) AS moraPagar,(ta_iva_mora-ta_iva_mora_pagado) AS ivaMoraPagar,(ta_multa-ta_multa_pagado) AS multaPagar,(ta_iva_multa-ta_iva_multa_pagado) AS ivaMultaPagar "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_pagado='N' order by ta_numPago;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrTabla.add(new TablaAmortVO(res.getInt("ta_numPago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"), res.getDouble("ta_interes"), res.getDouble("ta_iva_interes"), res.getDouble("ta_mora"),
                        res.getDouble("ta_iva_mora"), res.getDouble("ta_multa"), res.getDouble("ta_iva_multa"), res.getDouble("ta_monto_pagar"), res.getDouble("ta_monto_pagado"), res.getInt("ta_status"), res.getString("ta_pagado"),
                        res.getDouble("capitalPagar"), res.getDouble("interesPagar"), res.getDouble("ivaInteresPagar"), res.getDouble("moraPagar"), res.getDouble("ivaMoraPagar"), res.getDouble("multaPagar"), res.getDouble("ivaMultaPagar")));
            }
        } catch (SQLException sqle) {
            myLogger.error("saldosPendientesTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("saldosPendientesTablaCierre", e);
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

    public void condonaInteresTablaCierre(SaldoIBSVO saldo, int numPago) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_interes = 0,ta_iva_interes = 0 WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_numpago= ?";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());
            ps.setInt(3, numPago);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + ", " + numPago + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("condonaInteresTablaCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("condonaInteresTablaCierre", e);
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
    }

    public void liquidaTablaMicroCierre(SaldoIBSVO saldo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_capital_pagado=ta_abono_capital,ta_interes=0,ta_interes_pagado=ta_interes,ta_iva_interes=0,ta_iva_interes_pagado=ta_iva_interes,ta_mora_pagado=ta_mora,ta_iva_mora_pagado=ta_iva_mora,"
                + "ta_multa_pagado=ta_multa,ta_iva_multa_pagado=ta_iva_multa,ta_monto_pagado=ta_abono_capital,ta_monto_pagar=0,ta_pagado='S' WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_pagado='N'";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("liquidaTablaMicroCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("liquidaTablaMicroCierre", e);
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
    }

    public ArrayList<TablaAmortVO> saldosTablaAdelantoCierre(SaldoIBSVO saldo) throws ClientesException {

        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numPago,ta_fecha_pago,ta_abono_capital,ta_saldo_capital,ta_capital_pagado,ta_interes,ta_iva_interes,ta_interes_pagado,ta_iva_interes_pagado,ta_mora,ta_iva_mora,ta_mora_pagado,ta_iva_mora_pagado,"
                + "ta_multa,ta_iva_multa,ta_multa_pagado,ta_iva_multa_pagado,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado,"
                + "(ta_abono_capital-ta_capital_pagado) AS capitalPagar,(ta_interes-ta_interes_pagado) AS interesPagar,(ta_iva_interes-ta_iva_interes_pagado) ivaInteresPagar,"
                + "(ta_mora-ta_mora_pagado) AS moraPagar,(ta_iva_mora-ta_iva_mora_pagado) AS ivaMoraPagar,(ta_multa-ta_multa_pagado) AS multaPagar,(ta_iva_multa-ta_iva_multa_pagado) AS ivaMultaPagar "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente= ? AND ta_numcredito= ? AND ta_pagado='N' order by ta_numPago;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, saldo.getIdClienteSICAP());
            ps.setInt(2, saldo.getCredito());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getCredito() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrTabla.add(new TablaAmortVO(res.getInt("ta_numPago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"), res.getDouble("ta_saldo_capital"), res.getDouble("ta_capital_pagado"), res.getDouble("ta_interes"), res.getDouble("ta_iva_interes"), res.getDouble("ta_interes_pagado"), res.getDouble("ta_iva_interes_pagado"), res.getDouble("ta_mora"),
                        res.getDouble("ta_iva_mora"), res.getDouble("ta_mora_pagado"), res.getDouble("ta_iva_mora_pagado"), res.getDouble("ta_multa"), res.getDouble("ta_iva_multa"), res.getDouble("ta_multa_pagado"), res.getDouble("ta_iva_multa_pagado"), res.getDouble("ta_monto_pagar"), res.getDouble("ta_monto_pagado"), res.getInt("ta_status"), res.getString("ta_pagado"),
                        res.getDouble("capitalPagar"), res.getDouble("interesPagar"), res.getDouble("ivaInteresPagar"), res.getDouble("moraPagar"), res.getDouble("ivaMoraPagar"), res.getDouble("multaPagar"), res.getDouble("ivaMultaPagar")));
            }
        } catch (SQLException sqle) {
            myLogger.error("saldosTablaAdelantoCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("saldosTablaAdelantoCierre", e);
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

    public TablaAmortVO ajusteTablaMicroCierre(int numCredito, int numPago) throws ClientesException {

        TablaAmortVO tabla = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_fecha_pago,ta_saldo_capital FROM d_tabla_amortizacion WHERE ta_numcredito= ? AND ta_numpago= ?;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numCredito);
            ps.setInt(2, numPago);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCredito + ", " + numPago + "]");
            res = ps.executeQuery();
            while (res.next()) {
                tabla = new TablaAmortVO(res.getDate("ta_fecha_pago"), res.getDouble("ta_saldo_capital"));
            }
        } catch (SQLException sqle) {
            myLogger.error("ajusteTablaMicroCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("ajusteTablaMicroCierre", e);
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
        return tabla;
    }

    public void actualizaTablaMicroCierre(TablaAmortVO amortizacion, int numCredito) throws ClientesException {

        TablaAmortVO tabla = null;
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_tabla_amortizacion SET ta_abono_capital= ?,ta_saldo_capital= ?,ta_interes= ?,ta_monto_pagar= ? WHERE ta_numcredito= ? AND ta_numpago= ?;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, amortizacion.getAbonoCapital());
            ps.setDouble(2, amortizacion.getSaldoCapital());
            ps.setDouble(3, amortizacion.getInteres());
            ps.setDouble(4, amortizacion.getMontoPagar());
            ps.setInt(5, numCredito);
            ps.setInt(6, amortizacion.getNumPago());

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCredito + ", " + amortizacion.getNumPago() + ", " + amortizacion.getAbonoCapital() + ", " + amortizacion.getSaldoCapital() + ", " + amortizacion.getInteres() + ", " + amortizacion.getMontoPagar() + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("actualizaTablaMicroCierre", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actualizaTablaMicroCierre", e);
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
    }

    public int updateSaldosTablaAmortExtend(TablaAmortVO tabla) throws ClientesException {

        String query = "UPDATE D_TABLA_AMORTIZACION SET TA_SALDO_CAPITAL = ? , TA_ABONO_CAPITAL = ?, TA_CAPITAL_PAGADO = ?, "
                + "TA_INTERES = ?, TA_IVA_INTERES= ? , TA_INTERES_ACUM = ?, TA_IVA_INTERES_ACUM = ?, TA_INTERES_PAGADO = ? , "
                + "TA_IVA_INTERES_PAGADO = ?, TA_MORA = ?, TA_IVA_MORA = ?, TA_MORA_PAGADO = ?, TA_IVA_MORA_PAGADO = ? , TA_MULTA = ?, "
                + "TA_IVA_MULTA = ?, TA_MULTA_PAGADO = ?, TA_IVA_MULTA_PAGADO = ?,  TA_MONTO_PAGAR = ?, TA_MONTO_PAGADO = ?, "
                + " TA_STATUS = ?, TA_PAGADO = ?, TA_CAPITAL_ANTICIPADO = TA_CAPITAL_ANTICIPADO + ?, ta_quita_interes = ta_quita_interes + ?, ta_quita_iva_interes = ta_quita_iva_interes + ? "
                + " WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_NUMPAGO = ? AND TA_NUMDISPOSICION = 0";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(param++, tabla.saldoCapital);
            ps.setDouble(param++, tabla.abonoCapital);
            ps.setDouble(param++, tabla.capitalPagado);
            ps.setDouble(param++, tabla.interes);
            ps.setDouble(param++, tabla.ivaInteres);
            ps.setDouble(param++, tabla.interesAcum);
            ps.setDouble(param++, tabla.ivaInteresAcum);
            ps.setDouble(param++, tabla.interesPagado);
            ps.setDouble(param++, tabla.ivaInteresPagado);
            ps.setDouble(param++, tabla.intMoratorio);
            ps.setDouble(param++, tabla.ivaIntMoratorio);
            ps.setDouble(param++, tabla.intMoratorioPagado);
            ps.setDouble(param++, tabla.ivaIntMoratorioPagado);
            ps.setDouble(param++, tabla.multa);
            ps.setDouble(param++, tabla.ivaMulta);
            ps.setDouble(param++, tabla.multaPagado);
            ps.setDouble(param++, tabla.ivaMultaPagado);
            ps.setDouble(param++, tabla.montoPagar);
            ps.setDouble(param++, tabla.totalPagado);
            ps.setInt(param++, tabla.status);
            ps.setString(param++, tabla.pagado);
            ps.setDouble(param++, tabla.capitalAnticipado);
            ps.setDouble(param++, tabla.quitaIntres);
            ps.setDouble(param++, tabla.quitaIvaIntres);
            ps.setInt(param++, tabla.numCliente);
            ps.setInt(param++, tabla.numCredito);
            ps.setInt(param++, tabla.numPago);
            res = ps.executeUpdate();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + tabla.numCliente + "," + tabla.numCredito + "," + tabla.numPago + "]");

        } catch (SQLException sqle) {
            myLogger.error("updateSaldosTablaAmortExtend", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSaldosTablaAmortExtend", e);
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

    public double getInteresPorDevengar(int numCliente, int numCredito) throws ClientesException {
        double interes = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT SUM(ta_interes) interes FROM d_tabla_amortizacion WHERE ta_numcliente=? AND ta_numcredito=? AND ta_status IN(0,1) AND ta_pagado='N'";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + numCredito + "]");
            res = ps.executeQuery();
            while (res.next()) {
                interes = res.getDouble("interes");
            }
        } catch (SQLException sqle) {
            myLogger.error("getInteresPorDevengar", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getInteresPorDevengar", e);
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
        return interes;
    }

    public TablaAmortVO getUltmioDivPagado(int idCliente, int idCredito) throws ClientesException, SQLException {

        TablaAmortVO tabla = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numpago,ta_fecha_pago,ta_abono_capital,ta_capital_pagado,ta_interes,ta_iva_interes,ta_interes_pagado,ta_iva_interes_pagado,ta_mora,ta_iva_mora,ta_mora_pagado,ta_iva_mora_pagado,"
                + "ta_multa,ta_iva_multa,ta_multa_pagado,ta_iva_multa_pagado,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado,ta_entiempo "
                + "FROM d_tabla_amortizacion WHERE ta_numcliente=? AND ta_numcredito=? AND ta_monto_pagado!=0 ORDER BY ta_numpago DESC LIMIT 1;";

        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idCredito);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                tabla = new TablaAmortVO(res.getInt("ta_numpago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"), res.getDouble("ta_capital_pagado"), res.getDouble("ta_interes"),
                        res.getDouble("ta_iva_interes"), res.getDouble("ta_interes_pagado"), res.getDouble("ta_iva_interes_pagado"), res.getDouble("ta_mora"), res.getDouble("ta_iva_mora"),
                        res.getDouble("ta_mora_pagado"), res.getDouble("ta_iva_mora_pagado"), res.getDouble("ta_multa"), res.getDouble("ta_iva_multa"), res.getDouble("ta_multa_pagado"), res.getDouble("ta_iva_multa_pagado"),
                        res.getDouble("ta_monto_pagar"), res.getDouble("ta_monto_pagado"), res.getInt("ta_status"), res.getString("ta_pagado"), res.getInt("ta_entiempo"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getUltmioDivPagado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getUltmioDivPagado", e);
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
        return tabla;
    }

    public int getAtrasos(int grupo, int credito) throws ClientesDBException, NamingException, ClientesException {
        int atrasos = 0;
        String query = " select count(ta_status) as ATRASOS"
                + " from d_tabla_amortizacion"
                + " where ta_numcliente= ? AND ta_numcredito= ?"
                + " and ta_status  =2";
        Connection cn = null;
        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, grupo);
            ps.setInt(2, credito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + grupo + "," + credito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                atrasos = rs.getInt("ATRASOS");
            }
        } catch (SQLException sqle) {
            myLogger.error("getAtrasos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getAtrasos", e);
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

        return atrasos;
    }
    
    public ArrayList<TablaAmortVO> getPagosCorteTabla(Connection con, Date fecha) throws ClientesException {

        ArrayList<TablaAmortVO> arrTabla = new ArrayList<TablaAmortVO>();
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT ta_numcliente,ta_numcredito,ta_numpago,ta_fecha_pago,ta_abono_capital,ta_capital_pagado,ta_interes,ta_iva_interes,ta_interes_pagado,ta_iva_interes_pagado,"
                +"ta_multa,ta_iva_multa,ta_multa_pagado,ta_iva_multa_pagado,ta_monto_pagar,ta_monto_pagado,ta_status,ta_pagado "
                +"FROM d_tabla_amortizacion WHERE ta_fecha_pago=?;";
        try {
            ps = con.prepareStatement(query);
            ps.setDate(1, fecha);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                arrTabla.add(new TablaAmortVO(res.getInt("ta_numcliente"), res.getInt("ta_numcredito"), res.getInt("ta_numpago"), res.getDate("ta_fecha_pago"), res.getDouble("ta_abono_capital"),
                        res.getDouble("ta_capital_pagado"), res.getDouble("ta_interes"), res.getDouble("ta_iva_interes"), res.getDouble("ta_interes_pagado"), res.getDouble("ta_iva_interes_pagado"),
                        res.getDouble("ta_multa"), res.getDouble("ta_iva_multa"), res.getDouble("ta_multa_pagado"), res.getDouble("ta_iva_multa_pagado"), res.getDouble("ta_monto_pagar"), res.getDouble("ta_monto_pagado"), res.getInt("ta_status"), res.getString("ta_pagado")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosCorteTabla", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagosCorteTabla", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arrTabla;
    }
    
    public boolean updateFichaCompletaATiempo(Connection con, TablaAmortVO tabla) throws ClientesException {

        String query = "UPDATE d_tabla_amortizacion SET ta_entiempo=? WHERE ta_numcliente=? AND ta_numcredito=? AND ta_numpago=? AND ta_fecha_pago=?;";
        PreparedStatement ps = null;
        boolean listo = true;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tabla.getCompletaATiempo());
            ps.setInt(2, tabla.getNumCliente());
            ps.setInt(3, tabla.getNumCredito());
            ps.setInt(4, tabla.getNumPago());
            ps.setDate(5, tabla.getFechaPago());
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateFichaCompletaATiempo", sqle);
        } catch (Exception e) {
            myLogger.error("updateFichaCompletaATiempo", e);
            return listo = false;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return listo;
    }
    
    public int updateSaldosTablaAmortExtendInterciclo(Connection con,TablaAmortVO tabla) throws ClientesException {

        String query = "UPDATE D_TABLA_AMORTIZACION SET TA_SALDO_CAPITAL = ? , TA_ABONO_CAPITAL = ?, TA_CAPITAL_PAGADO = ?, "
                + "TA_INTERES = ?, TA_IVA_INTERES= ? , TA_INTERES_ACUM = ?, TA_IVA_INTERES_ACUM = ?, TA_INTERES_PAGADO = ? , "
                + "TA_IVA_INTERES_PAGADO = ?, TA_MORA = ?, TA_IVA_MORA = ?, TA_MORA_PAGADO = ?, TA_IVA_MORA_PAGADO = ? , TA_MULTA = ?, "
                + "TA_IVA_MULTA = ?, TA_MULTA_PAGADO = ?, TA_IVA_MULTA_PAGADO = ?,  TA_MONTO_PAGAR = ?, TA_MONTO_PAGADO = ?, "
                + " TA_STATUS = ?, TA_PAGADO = ?, TA_INCREMENTO_CAPITAL = TA_INCREMENTO_CAPITAL + ? "
                + " WHERE TA_NUMCLIENTE = ? AND TA_NUMCREDITO = ? AND TA_NUMPAGO = ? AND TA_NUMDISPOSICION = 0";

        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setDouble(param++, tabla.saldoCapital);
            ps.setDouble(param++, tabla.abonoCapital);
            ps.setDouble(param++, tabla.capitalPagado);
            ps.setDouble(param++, tabla.interes);
            ps.setDouble(param++, tabla.ivaInteres);
            ps.setDouble(param++, tabla.interesAcum);
            ps.setDouble(param++, tabla.ivaInteresAcum);
            ps.setDouble(param++, tabla.interesPagado);
            ps.setDouble(param++, tabla.ivaInteresPagado);
            ps.setDouble(param++, tabla.intMoratorio);
            ps.setDouble(param++, tabla.ivaIntMoratorio);
            ps.setDouble(param++, tabla.intMoratorioPagado);
            ps.setDouble(param++, tabla.ivaIntMoratorioPagado);
            ps.setDouble(param++, tabla.multa);
            ps.setDouble(param++, tabla.ivaMulta);
            ps.setDouble(param++, tabla.multaPagado);
            ps.setDouble(param++, tabla.ivaMultaPagado);
            ps.setDouble(param++, tabla.montoPagar);
            ps.setDouble(param++, tabla.totalPagado);
            ps.setInt(param++, tabla.status);
            ps.setString(param++, tabla.pagado);
            ps.setDouble(param++, tabla.incrementoCapital);
            ps.setInt(param++, tabla.numCliente);
            ps.setInt(param++, tabla.numCredito);
            ps.setInt(param++, tabla.numPago);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            //myLogger.debug("Parametros = [" + tabla.numCliente + "," + tabla.numCredito + "," + tabla.numPago + "]");

        } catch (SQLException sqle) {
            myLogger.error("updateSaldosTablaAmortExtend", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSaldosTablaAmortExtend", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    
    public Date getFechaPrimerIncumplimiento(int idEquipo, int idCiclo) throws ClientesException {

        String query = "SELECT MIN(ta_fecha_pago) AS fechaIncumplida FROM d_tabla_amortizacion WHERE ta_numcliente=? AND ta_numcredito=? AND ta_status in (2,3);";
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet res = null;
        Date fecha = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            if(res.next()){
                fecha = res.getDate("fechaIncumplida");
            }
        } catch (SQLException sqle) {
            myLogger.error("getFechaPrimerIncumplimiento", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getFechaPrimerIncumplimiento", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return fecha;
    }
}

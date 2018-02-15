package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import org.apache.log4j.Logger;

public class TablaAmortizacionDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(TablaAmortizacionDAO.class);

    public TablaAmortizacionVO[] getElementos(int idCliente, int idSolicitud, int idAmortizacion) throws ClientesException {
        return getElementos(idCliente, idSolicitud, 0, idAmortizacion);
    }

    public TablaAmortizacionVO[] getElementos(int idCliente, int idSolicitud, int idDisposicion, int idAmortizacion) throws ClientesException {
        TablaAmortizacionVO tabla = null;
        Connection cn = null;
        ArrayList<TablaAmortizacionVO> array = new ArrayList<TablaAmortizacionVO>();
        TablaAmortizacionVO elementos[] = null;

        String query = "SELECT TA_NO_PAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_COMISION_INICIAL, TA_IVA_COMISION,"
                + "TA_INTERES, TA_IVA_INTERES, TA_MONTO_PAGAR, TA_PAGADO FROM D_TABLA_AMORTIZACION WHERE TA_ID_CLIENTE = ? AND TA_ID_SOLICITUD = ? "
                + "AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "," + idSolicitud + "," + idDisposicion + "," + idAmortizacion + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabla = new TablaAmortizacionVO();
                tabla.idCliente = idCliente;
                tabla.idSolicitud = idSolicitud;
                tabla.tipoAmortizacion = idAmortizacion;
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
                array.add(tabla);
            }
            if (array.size() > 0) {
                elementos = new TablaAmortizacionVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TablaAmortizacionVO) array.get(i);
                }
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

        return elementos;
    }

    public int getMaxElementos(int idCliente, int idSolicitud) throws ClientesException {
        
        int numAmort = 0;
        Connection cn = null;
        String query = "SELECT MAX(ta_tpo_amortizacion) FROM d_tabla_amortizacion WHERE ta_id_cliente=? and ta_id_solicitud=?;";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numAmort = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("getMaxElementos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMaxElementos", e);
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

        return numAmort;
    }

    public int addTablaAmortizacion(TablaAmortizacionVO tabla) throws ClientesException {

        String query = "INSERT INTO D_TABLA_AMORTIZACION (TA_ID_CLIENTE, TA_ID_SOLICITUD, TA_NUMDISPOSICION, TA_TPO_AMORTIZACION, TA_NO_PAGO, TA_FECHA_PAGO,"
                + "TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_COMISION_INICIAL, TA_IVA_COMISION, "
                + "TA_INTERES, TA_IVA_INTERES, TA_MONTO_PAGAR, TA_PAGADO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, tabla.idCliente);
            ps.setInt(param++, tabla.idSolicitud);
            ps.setInt(param++, tabla.idDisposicion);
            ps.setInt(param++, tabla.tipoAmortizacion);
            ps.setInt(param++, tabla.numPago);
            ps.setDate(param++, tabla.fechaPago);
            ps.setDouble(param++, tabla.saldoInicial);
            ps.setDouble(param++, tabla.abonoCapital);
            ps.setDouble(param++, tabla.saldoCapital);
            ps.setDouble(param++, tabla.comisionInicial);
            ps.setDouble(param++, tabla.ivaComision);
            ps.setDouble(param++, tabla.interes);
            ps.setDouble(param++, tabla.ivaInteres);
            ps.setDouble(param++, tabla.montoPagar);
            ps.setInt(param++, tabla.pagado);
            myLogger.debug("Ejecutando = " + ps);
            //myLogger.debug("Para = "+tabla);
            res = ps.executeUpdate();
            //myLogger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("addTablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addTablaAmortizacion", e);
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

    public void delTablaAmortizacion(int idCliente, int idSolicitud, int idAmortizacion) throws ClientesException {

        delTablaAmortizacion(idCliente, idSolicitud, 0, idAmortizacion);

    }

    public void delTablaAmortizacion(int idCliente, int idSolicitud, int idDisposicion, int idAmortizacion) throws ClientesException {

        String query = "DELETE FROM D_TABLA_AMORTIZACION WHERE TA_ID_CLIENTE = ? AND TA_ID_SOLICITUD = ? AND TA_NUMDISPOSICION = ? AND TA_TPO_AMORTIZACION = ?";

        Connection cn = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, idDisposicion);
            ps.setInt(4, idAmortizacion);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "," + idSolicitud + "," + idDisposicion + "," + idAmortizacion + "]");
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

    public void delTablaAmortizacion(int idCliente, int idSolicitud) throws ClientesException {

        String query = "DELETE FROM d_tabla_amortizacion WHERE ta_id_cliente = ? AND ta_id_solicitud = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + ps.toString());
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
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
}

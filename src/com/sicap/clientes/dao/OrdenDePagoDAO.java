package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.BitacoraVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class OrdenDePagoDAO extends DAOMaster {

    public String mensaje = null;
    private static Logger myLogger = Logger.getLogger(OrdenDePagoDAO.class);

    public OrdenDePagoVO getOrdenDePago(int numCliente, int numSolicitud) throws ClientesException {
        OrdenDePagoVO oPago = new OrdenDePagoVO();
        Connection conn = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE pg_numcliente   = ?"
                + "  AND pg_numsolicitud = ?"
                + " AND pg_estatus BETWEEN 1 AND 8"
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                oPago = new OrdenDePagoVO();

                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }

        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenDePagoSeguro(int numCliente, int numSolicitud) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        String query
                = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente=? AND pg_numsolicitud=? ORDER BY pg_estatus DESC, pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + numSolicitud + "]");

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
            }
        } catch (Exception e) {
            myLogger.error("getOrdenDePagoSeguro", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePagoSeguro", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenPago(int numCliente, int numSolicitud) throws ClientesException {

        String query = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente =? AND pg_numsolicitud =? ORDER BY pg_identificador DESC";
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ResultSet rs = ps.executeQuery();
            myLogger.debug(ps.toString());

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
            }
        } catch (Exception e) {
            myLogger.error("getOrdenPago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenPago", exc);
            }
        }
        return oPago;
    }
    
    public OrdenDePagoVO getOrdenPago(int numCliente, int numSolicitud, String referencia) throws ClientesException {

        String query = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente =? AND pg_numsolicitud =? AND pg_referencia =?";
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setString(3, referencia);
            ResultSet rs = ps.executeQuery();
            myLogger.debug(ps.toString());

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
            }
        } catch (Exception e) {
            myLogger.error("getOrdenPago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenPago", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getODPSeguroGenerada(int numCliente, int numSolicitud) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        String query
                = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente=? AND pg_numsolicitud=? AND pg_estatus BETWEEN 9 AND 11 ORDER BY pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + numSolicitud + "]");

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
            }
        } catch (Exception e) {
            myLogger.error("getODPSeguroGenerada", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getODPSeguroGenerada", exc);
            }
        }
        return oPago;
    }

    public int getSizeODPSeguro(int numCliente, int numSolicitud) throws ClientesException {
        Connection conn = null;
        int size = 0;
        String query = "SELECT COUNT(pg_estatus) AS count FROM d_ordenes_de_pago WHERE pg_numcliente=? AND pg_numsolicitud=? AND pg_estatus BETWEEN 9 AND 11 GROUP BY pg_estatus ORDER BY pg_estatus ASC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parámetros [" + numCliente + ", " + numSolicitud + "]");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = (rs.getInt("count"));
            }
        } catch (Exception e) {
            myLogger.error("getSizeODPSeguro", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getSizeODPSeguro", exc);
            }
        }
        return size;
    }

    public ArrayList<OrdenDePagoVO> getOrdenPagoSeguro(int numCliente, int numSolicitud, int size) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;
        ArrayList<OrdenDePagoVO> ordenesDeSeguro = new ArrayList<OrdenDePagoVO>();
        String query
                = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente=? AND pg_numsolicitud=? AND pg_estatus BETWEEN 9 AND 11 ORDER BY pg_identificador DESC LIMIT ?";
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ps.setInt(3, size);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + numSolicitud + ", " + size + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                ordenesDeSeguro.add(oPago);
            }
        } catch (Exception e) {
            myLogger.error("getOrdenPagoSeguro", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenPagoSeguro", exc);
            }
        }
        return ordenesDeSeguro;
    }

    public ArrayList<OrdenDePagoVO> getOrdenesSeguros(Date fechaInicio, Date fechaFin) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;
        ArrayList<OrdenDePagoVO> ordenesDeSeguro = new ArrayList<OrdenDePagoVO>();
        String query
                = "SELECT d_ordenes_de_pago.*, su_nombre FROM d_ordenes_de_pago INNER JOIN c_sucursales\n"
                + "ON (pg_numsucursal=su_numsucursal)"
                + "WHERE pg_estatus>=9 AND pg_estatus<=11 AND DATE(pg_fecha_captura) BETWEEN ? AND ? ORDER BY DATE(pg_fecha_captura) ASC, su_nombre ASC, pg_numcliente ASC;";
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + fechaInicio + ", " + fechaFin + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                ordenesDeSeguro.add(oPago);
            }
        } catch (Exception e) {
            myLogger.error("getOrdenesSeguros", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenesSeguros", exc);
            }
        }
        return ordenesDeSeguro;
    }

    public OrdenDePagoVO getOrdenDePago(int numCliente, int numSucursal, String referencia) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE pg_numcliente   = ?"
                + "  AND pg_referencia   = ?"
                + "  AND pg_numsucursal  = ?"
                + "  AND pg_estatus IN (1, 2, 8) "
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setString(2, referencia);
            ps.setInt(3, numSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + ", " + referencia + ", " + numSucursal + "]");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                oPago = new OrdenDePagoVO();

                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));;
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }

        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenDePago(Connection conn, int idOrden) throws ClientesException {
        OrdenDePagoVO oPago = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM  d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE  pg_identificador = ? "
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }
        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenDePago(int idOrden) throws ClientesException {
        OrdenDePagoVO oPago = null;
        Connection conn = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM  d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE  pg_identificador = ? "
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }
        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenDePago(String referencia) throws ClientesException {
        Connection conn = null;
        OrdenDePagoVO oPago = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM  d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE  pg_referencia = ? "
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, referencia);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }

        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }
    
    public OrdenDePagoVO getOrdenDePago(Connection con, String referencia) throws ClientesException {
        
        OrdenDePagoVO oPago = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM  d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE  pg_referencia = ? "
                + " ORDER BY pg_identificador DESC LIMIT 1";

        try {
            ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }
        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO getOrdenDePagoReferencia(String referencia) throws ClientesException {
        Connection conn = null;
        OrdenDePagoVO oPago = null;

        String query = "SELECT * FROM d_ordenes_de_pago WHERE pg_referencia = ?";
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, referencia);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando: " + ps.toString());
            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setNumEnvio(rs.getInt("pg_envio"));
            }

        } catch (Exception e) {
            myLogger.error("getOrdenDePagoReferencia", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePagoReferencia", exc);
            }
        }
        return oPago;
    }

    public OrdenDePagoVO[] getOrdenesDePago(int numCliente, int numSolicitud) throws ClientesException {
        OrdenDePagoVO oPago = null;
        OrdenDePagoVO elementos[] = null;
        List<OrdenDePagoVO> lista = new ArrayList<OrdenDePagoVO>();
        Connection conn = null;

        String query
                = "SELECT d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "WHERE pg_numcliente   = ?"
                + "  AND pg_numsolicitud = ?";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                oPago = new OrdenDePagoVO();

                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");

                lista.add(oPago);
            }

        } catch (Exception e) {
            myLogger.error("getOrdenesDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenesDePago", exc);
            }
        }
        elementos = new OrdenDePagoVO[lista.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (OrdenDePagoVO) lista.get(i);
        }
        return elementos;
    }

    public OrdenDePagoVO[] getOrdenesDePago(int banco, int sucursal, int estatus, Date fechaInicio, Date fechaFin) throws ClientesException {
        OrdenDePagoVO oPago = null;
        OrdenDePagoVO elementos[] = null;
        List<OrdenDePagoVO> lista = new ArrayList<OrdenDePagoVO>();
        Connection conn = null;
        int param = 0;

        String query
                = "SELECT  d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM   d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + " WHERE pg_banco     = ? ";
        query += (sucursal > 0 ? "AND pg_numsucursal = ? " : " ");
        query += (estatus > 0 ? "AND pg_estatus   IN( ?, 4, 6) " : " ");

        if (fechaInicio != null) {
            if (fechaFin != null) {
                query += "AND DATE(pg_fecha_captura) BETWEEN ? AND ? ";
            } else {
                query += "AND DATE(pg_fecha_captura) = ? ";
            }
        }
        query += "ORDER BY pg_estatus, grupo, pg_numcliente, pg_numsolicitud ";

        myLogger.debug(" Query: " + query);

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            param++;
            ps.setInt(param, banco);

            if (sucursal > 0) {
                param++;
                ps.setInt(param, sucursal);
            }

            if (estatus > 0) {
                param++;
                ps.setInt(param, estatus);
            }

            if (fechaInicio != null) {
                if (fechaFin != null) {
                    param++;
                    ps.setDate(param, fechaInicio);
                    param++;
                    ps.setDate(param, fechaFin);
                } else {
                    param++;
                    ps.setDate(param, fechaInicio);
                }
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                oPago = new OrdenDePagoVO();

                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");

                lista.add(oPago);
            }

        } catch (Exception e) {
            myLogger.error("getOrdenesDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenesDePago", exc);
            }
        }
        elementos = new OrdenDePagoVO[lista.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (OrdenDePagoVO) lista.get(i);
        }
        return elementos;
    }

    public ArrayList<OrdenDePagoVO> getArrOrdenesDePago(int banco, int estatus, Date fechaInicio, Date fechaFin, int sucursal) throws ClientesException {
        
        ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
        Connection conn = null;
        int param = 0;
        
        String query = "SELECT pg_identificador,pg_referencia,pg_numcliente,pg_numsolicitud,pg_numsucursal,pg_nombre_cliente,pg_monto,pg_fecha_captura,pg_fecha_envio,pg_banco,pg_estatus," +
                "pg_nombre_archivo,pg_usuario,pg_envio,so_numoperacion 'operacion',op_nombre AS 'producto',su_nombre AS 'sucursal',co_descripcion AS 'estatus',gr_nombre AS 'grupo' " +
                "FROM d_ordenes_de_pago LEFT JOIN d_integrantes_ciclo ON (pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud and ic_tipo!=0) " +
                "LEFT JOIN d_solicitudes ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) LEFT JOIN c_operaciones ON (so_numoperacion=op_numoperacion) " +
                "LEFT JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) LEFT JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) " +
                "LEFT JOIN d_grupos ON (gr_numgrupo=ic_numgrupo AND gr_numsucursal=pg_numsucursal) WHERE pg_banco=? AND pg_estatus>0 ";
        query += (estatus > 0 ? "AND pg_estatus=? " : " ");
        if (fechaFin != null) {
            query += "AND date(pg_fecha_envio) BETWEEN ? AND ? ";
        } else {
            query += "AND date(pg_fecha_envio)=? ";
        }
        query += (sucursal > 0 ? "AND gr_numsucursal=? " : " ");
        query += "ORDER BY pg_estatus, grupo, pg_numcliente, pg_numsolicitud;";
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(++param, banco);
            if(estatus > 0)
                ps.setInt(++param, estatus);
            ps.setDate(++param, fechaInicio);
            if (fechaFin != null)
                ps.setDate(++param, fechaFin);
            if (sucursal > 0)
                ps.setInt(++param, sucursal);
            myLogger.debug("Ejecutando: "+ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arrOrdenes.add(new OrdenDePagoVO(rs.getInt("pg_identificador"), rs.getInt("pg_numcliente"), rs.getInt("pg_numsolicitud"), rs.getInt("operacion"), rs.getInt("pg_numsucursal"), 
                        rs.getString("pg_nombre_cliente"), rs.getDouble("pg_monto"), rs.getString("pg_referencia"), rs.getString("pg_nombre_archivo"), rs.getString("pg_usuario"), rs.getTimestamp("pg_fecha_captura"), 
                        rs.getTimestamp("pg_fecha_envio"), rs.getInt("pg_banco"), rs.getInt("pg_estatus"), rs.getString("sucursal"), rs.getString("producto"), rs.getString("estatus"), rs.getString("grupo") != null ? rs.getString("grupo") : "--", rs.getInt("pg_envio")));
            }
        } catch (Exception e) {
            myLogger.error("getArrOrdenesDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getArrOrdenesDePago", exc);
            }
        }
        return arrOrdenes;
    }

    public OrdenDePagoVO[] getOrdenesDePagoGrupos(int banco, int sucursal, int estatus, Date fechaInicio, Date fechaFin) throws ClientesException {
        OrdenDePagoVO oPago = null;
        OrdenDePagoVO elementos[] = null;
        List<OrdenDePagoVO> lista = new ArrayList<OrdenDePagoVO>();
        Connection conn = null;
        int param = 0;

        String query
                = "SELECT  d_ordenes_de_pago.*, "
                + "so_numoperacion 'operacion', "
                + "op_nombre AS 'producto', "
                + "su_nombre AS 'sucursal', "
                + "co_descripcion AS 'estatus', "
                + "gr_nombre AS 'grupo' "
                + "FROM   d_ordenes_de_pago "
                + "JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) "
                + "JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                + "JOIN (d_solicitudes "
                + "JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "LEFT JOIN(d_integrantes_ciclo "
                + "JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) "
                + "ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + " WHERE pg_banco     = ? AND SUBSTR(PG_REFERENCIA, 4, 1) = 9 ";
        query += (sucursal > 0 ? "AND pg_numsucursal = ? " : " ");
        query += (estatus > 0 ? "AND pg_estatus   IN( ?, 4, 6) " : " ");

        if (fechaInicio != null) {
            if (fechaFin != null) {
                query += "AND DATE(pg_fecha_captura) BETWEEN ? AND ? ";
            } else {
                query += "AND DATE(pg_fecha_captura) = ? ";
            }
        }
        query += "ORDER BY pg_estatus, grupo, pg_numcliente, pg_numsolicitud ";

        myLogger.debug(" Query: " + query);

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            param++;
            ps.setInt(param, banco);

            if (sucursal > 0) {
                param++;
                ps.setInt(param, sucursal);
            }

            if (estatus > 0) {
                param++;
                ps.setInt(param, estatus);
            }

            if (fechaInicio != null) {
                if (fechaFin != null) {
                    param++;
                    ps.setDate(param, fechaInicio);
                    param++;
                    ps.setDate(param, fechaFin);
                } else {
                    param++;
                    ps.setDate(param, fechaInicio);
                }
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                oPago = new OrdenDePagoVO();

                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");

                lista.add(oPago);
            }

        } catch (Exception e) {
            myLogger.error("getOrdenesDePagoGrupos", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenesDePagoGrupos", exc);
            }
        }
        elementos = new OrdenDePagoVO[lista.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (OrdenDePagoVO) lista.get(i);
        }
        return elementos;
    }

    public int addOrdenPago(Connection conn, OrdenDePagoVO oPago) throws ClientesException {
        myLogger.debug("Entrando addOrdenPago");
        String query = "INSERT INTO D_ORDENES_DE_PAGO (pg_numcliente, pg_numsolicitud, pg_numsucursal, pg_usuario, pg_nombre_cliente, pg_monto, "
                + "pg_referencia, pg_banco, pg_estatus, pg_fecha_envio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int res = 0;

        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setInt(1, oPago.getIdCliente());
            ps.setInt(2, oPago.getIdSolicitud());
            ps.setInt(3, oPago.getIdSucursal());

            ps.setString(4, oPago.getUsuario());
            ps.setString(5, oPago.getNombre());
            ps.setDouble(6, oPago.getMonto());
            ps.setString(7, oPago.getReferencia());
            ps.setInt(8, oPago.getIdBanco());
            ps.setInt(9, oPago.getEstatus());
            ps.setTimestamp(10, null);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
            myLogger.debug("Parametros [" + oPago.getIdCliente() + "," + oPago.getIdSolicitud() + "," + oPago.getIdSucursal() + "," + oPago.getReferencia() + "," + oPago.getMonto() + "]");

        } catch (Exception e) {
            myLogger.error("addOrdenPago", e);
            throw new ClientesException(e.getMessage());
        }

        return res;
    }

    public void addOrdenPago(OrdenDePagoVO oPago) throws ClientesException {
        String query = "INSERT INTO D_ORDENES_DE_PAGO (pg_numcliente, pg_numsolicitud, pg_numsucursal, pg_usuario, pg_nombre_cliente, pg_monto, "
                + "pg_referencia, pg_banco, pg_estatus, pg_fecha_envio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, oPago.getIdCliente());
            ps.setInt(2, oPago.getIdSolicitud());
            ps.setInt(3, oPago.getIdSucursal());
            ps.setString(4, oPago.getUsuario());
            ps.setString(5, oPago.getNombre());
            ps.setDouble(6, oPago.getMonto());
            ps.setString(7, oPago.getReferencia());
            ps.setInt(8, oPago.getIdBanco());
            ps.setInt(9, oPago.getEstatus());
            ps.setTimestamp(10, null);
            //ps.setTimestamp(11, oPago.getFechaEnvio());
            ps.executeUpdate();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + oPago + "]");
        } catch (SQLException sqle) {
            myLogger.error("addOrdenPago", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addOrdenPago", e);
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

    public boolean updateStatusFromList(Connection conn, List list, int envio, String command) throws ClientesException {

        Iterator iterator = list.iterator();
        OrdenDePagoVO oPago = null;
        PreparedStatement ps = null;
        boolean finished = false;
        int res = 0;
        BitacoraVO bitacora = null;
        List<BitacoraVO> bitacoraList = new ArrayList<BitacoraVO>();

        String query
                = "UPDATE d_ordenes_de_pago   "
                + "SET pg_usuario     = ?, "
                + "pg_fecha_envio = ?, "
                + "pg_estatus     = ?, "
                + "pg_envio       = ?  "
                + "WHERE  pg_identificador = ?";

        try {
            ps = conn.prepareStatement(query);

            while (iterator.hasNext()) {
                oPago = (OrdenDePagoVO) iterator.next();

                ps.setString(1, oPago.getUsuario());
                ps.setTimestamp(2, oPago.getFechaEnvio());
                ps.setInt(3, oPago.getEstatus());
                ps.setInt(4, envio);
                ps.setInt(5, oPago.getIdOrdenPago());

                myLogger.debug("Ejecutando = " + ps);
                res = ps.executeUpdate();
                myLogger.debug("Resultado = " + res);

                /**
                 * Alamacenando Información en la Bitácora
                 */
                bitacora = new BitacoraVO();
                bitacora.idCliente = oPago.getIdCliente();
                bitacora.usuario = oPago.getUsuario();
                bitacora.comando = command;
                bitacora.objeto = oPago;
                bitacoraList.add(bitacora);
            }
            guardaBitacora(bitacoraList);
            finished = true;

        } catch (Exception e) {
            myLogger.error("updateStatusFromList", e);
            throw new ClientesException(e.getMessage());
        }

        return finished;
    }

    public int updateOrdenDePago(Connection conn, OrdenDePagoVO oPago) throws ClientesException {
        PreparedStatement ps = null;
        int res = 0;

        String query
                = "UPDATE d_ordenes_de_pago "
                + "SET pg_identificador = ?, "
                + "pg_referencia    = ?, "
                + "pg_numcliente    = ?, "
                + "pg_numsolicitud  = ?, "
                + "pg_numsucursal   = ?, "
                + "pg_nombre_cliente= ?, "
                + "pg_monto         = ?, "
                + "pg_fecha_captura = ?, "
                + "pg_fecha_envio   = ?, "
                + "pg_fecha_cancelacion = ?, "
                + "pg_banco         = ?, "
                + "pg_estatus       = ?, "
                + "pg_nombre_archivo= ?, "
                + "pg_usuario       = ? "
                + "WHERE pg_identificador = ? ";

        try {
            ps = conn.prepareStatement(query);

            ps.setInt(1, oPago.getIdOrdenPago());
            ps.setString(2, oPago.getReferencia());
            ps.setInt(3, oPago.getIdCliente());
            ps.setInt(4, oPago.getIdSolicitud());
            ps.setInt(5, oPago.getIdSucursal());
            ps.setString(6, oPago.getNombre());
            ps.setDouble(7, oPago.getMonto());
            ps.setTimestamp(8, oPago.getFechaCaptura());
            ps.setTimestamp(9, oPago.getFechaEnvio());
            ps.setTimestamp(10, oPago.getFechaCancelacion());
            ps.setInt(11, oPago.getIdBanco());
            ps.setInt(12, oPago.getEstatus());
            ps.setString(13, oPago.getNombreArchivo());
            ps.setString(14, oPago.getUsuario());

            ps.setInt(15, oPago.getIdOrdenPago());
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeUpdate();
            myLogger.debug("RES: "+res);

        } catch (Exception e) {
            myLogger.error("updateOrdenDePago", e);
            throw new ClientesException(e.getMessage());
        }

        return res;
    }
    
    public int updateOrdenDePagoConfirmaDesembolso(Connection conn, OrdenDePagoVO oPago) throws ClientesException {
        PreparedStatement ps = null;
        int res = 0;

        String query
                = "UPDATE d_ordenes_de_pago "
                + "SET pg_referencia    = ?, "
                + "pg_numcliente    = ?, "
                + "pg_numsolicitud  = ?, "
                + "pg_numsucursal   = ?, "
                + "pg_nombre_cliente= ?, "
                + "pg_monto         = ?, "
                + "pg_fecha_captura = ?, "
                + "pg_fecha_envio   = ?, "
                + "pg_fecha_cancelacion = ?, "
                + "pg_banco         = ?, "
                + "pg_estatus       = ?, "
                + "pg_nombre_archivo= ?, "
                + "pg_usuario       = ? "
                + "WHERE pg_numcliente = ? AND pg_numsolicitud = ? AND pg_estatus=8 ";

        try {
            ps = conn.prepareStatement(query);

            ps.setString(1, oPago.getReferencia());
            ps.setInt(2, oPago.getIdCliente());
            ps.setInt(3, oPago.getIdSolicitud());
            ps.setInt(4, oPago.getIdSucursal());
            ps.setString(5, oPago.getNombre());
            ps.setDouble(6, oPago.getMonto());
            ps.setTimestamp(7, oPago.getFechaCaptura());
            ps.setTimestamp(8, oPago.getFechaEnvio());
            ps.setTimestamp(9, oPago.getFechaCancelacion());
            ps.setInt(10, oPago.getIdBanco());
            ps.setInt(11, oPago.getEstatus());
            ps.setString(12, oPago.getNombreArchivo());
            ps.setString(13, oPago.getUsuario());

            ps.setInt(14, oPago.getIdCliente());
            ps.setInt(15, oPago.getIdSolicitud());

            res = ps.executeUpdate();
            myLogger.debug("Ejecutando: "+ps.toString());
            myLogger.debug("RES: "+res);

        } catch (Exception e) {
            myLogger.error("updateOrdenDePago", e);
            throw new ClientesException(e.getMessage());
        }

        return res;
    }

    public int updateOrdenDePago(OrdenDePagoVO oPago) throws ClientesException {
        PreparedStatement ps = null;
        int res = 0;
        Connection conn = null;

        String query
                = "UPDATE d_ordenes_de_pago "
                + "SET pg_identificador = ?, "
                + "pg_referencia    = ?, "
                + "pg_numcliente    = ?, "
                + "pg_numsolicitud  = ?, "
                + "pg_numsucursal   = ?, "
                + "pg_nombre_cliente= ?, "
                + "pg_monto         = ?, "
                + "pg_fecha_captura = ?, "
                + "pg_fecha_envio   = ?, "
                + "pg_fecha_cancelacion = ?, "
                + "pg_banco         = ?, "
                + "pg_estatus       = ?, "
                + "pg_nombre_archivo= ?, "
                + "pg_usuario       = ? "
                + "WHERE pg_identificador = ? ";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, oPago.getIdOrdenPago());
            ps.setString(2, oPago.getReferencia());
            ps.setInt(3, oPago.getIdCliente());
            ps.setInt(4, oPago.getIdSolicitud());
            ps.setInt(5, oPago.getIdSucursal());
            ps.setString(6, oPago.getNombre());
            ps.setDouble(7, oPago.getMonto());
            ps.setTimestamp(8, oPago.getFechaCaptura());
            ps.setTimestamp(9, oPago.getFechaEnvio());
            ps.setTimestamp(10, oPago.getFechaCancelacion());
            ps.setInt(11, oPago.getIdBanco());
            ps.setInt(12, oPago.getEstatus());
            ps.setString(13, oPago.getNombreArchivo());
            ps.setString(14, oPago.getUsuario());

            ps.setInt(15, oPago.getIdOrdenPago());

            res = ps.executeUpdate();

        } catch (Exception e) {
            myLogger.error("updateOrdenDePago", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("updateOrdenDePago", exc);
            }
        }
        return res;
    }

    public int getNumeroEnvio() throws ClientesException {
        Connection conn = null;
        int envio = 0;

        String query
                = "SELECT MAX(pg_envio) AS envio "
                + "FROM d_ordenes_de_pago "
                + "WHERE DATE(pg_fecha_envio) = CURRENT_DATE "
                + "AND pg_banco = 2";

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);

            if (rs.next()) {
                envio = rs.getInt("envio");
            }

        } catch (Exception e) {
            myLogger.error("getNumeroEnvio", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNumeroEnvio", exc);
            }
        }
        return envio;
    }

    public int getNumeroEnvio(int banco) throws ClientesException {
        Connection conn = null;
        int envio = 0;

        String query
                = "SELECT MAX(pg_envio) AS envio "
                + "FROM d_ordenes_de_pago "
                + "WHERE DATE(pg_fecha_envio) = CURRENT_DATE "
                + "AND pg_banco = ?";

        try {
            conn = getConnection();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + banco + "]");
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, banco);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Resultado = " + rs);

            if (rs.next()) {
                envio = rs.getInt("envio");
            }

        } catch (Exception e) {
            myLogger.error("getNumeroEnvio", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNumeroEnvio", exc);
            }
        }
        return envio;
    }

    private void guardaBitacora(List list) {
        BitacoraVO bitacora = null;
        BitacoraDAO bitacoraDao = new BitacoraDAO();
        try {
            Iterator i = list.iterator();
            while (i.hasNext()) {
                bitacora = (BitacoraVO) i.next();
                bitacoraDao.add(bitacora);
            }
        } catch (Exception e) {
            myLogger.error("guardaBitacora", e);

        }
    }

    public int deleteOrdenPago(int numCliente, int numSolicitud) throws ClientesException {

        String query = "DELETE FROM D_ORDENES_DE_PAGO WHERE PG_NUMCLIENTE = ? AND PG_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteOrdenPago", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteOrdenPago", e);
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

    public void respaldaOrdenesCiclo(int idEquipo, int idCiclo) throws ClientesException {

        String query = "insert into d_ordenes_de_pago_del (pg_identificador,pg_referencia,pg_numcliente,pg_numsolicitud,pg_numsucursal,pg_nombre_cliente,pg_monto,pg_fecha_captura,pg_fecha_envio,pg_fecha_cancelacion,pg_banco,pg_estatus,pg_envio,pg_nombre_archivo,pg_usuario)"
                + " (select d_ordenes_de_pago.pg_identificador,d_ordenes_de_pago.pg_referencia,d_ordenes_de_pago.pg_numcliente,d_ordenes_de_pago.pg_numsolicitud,d_ordenes_de_pago.pg_numsucursal,d_ordenes_de_pago.pg_nombre_cliente,d_ordenes_de_pago.pg_monto,d_ordenes_de_pago.pg_fecha_captura,d_ordenes_de_pago.pg_fecha_envio,d_ordenes_de_pago.pg_fecha_cancelacion,d_ordenes_de_pago.pg_banco,d_ordenes_de_pago.pg_estatus,d_ordenes_de_pago.pg_envio,d_ordenes_de_pago.pg_nombre_archivo,d_ordenes_de_pago.pg_usuario "
                + " FROM d_ordenes_de_pago,d_integrantes_ciclo " +
                "WHERE pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud " +
                "AND ic_numgrupo=? AND ic_numciclo=?)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
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
                myLogger.error("SQLException", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public void eliminaOrdenesCiclo(int idEquipo, int idCiclo, boolean conexionODS) throws ClientesException {

        String query = "DELETE d_ordenes_de_pago.* FROM d_ordenes_de_pago,d_integrantes_ciclo " +
                "WHERE pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud " +
                "AND ic_numgrupo=? AND ic_numciclo=?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                cn = getODSConnection();
            }else{
                cn = getConnection();
            }
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
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
                myLogger.error("SQLException", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public ArrayList<OrdenDePagoVO> getOrdenesPago(int idBanco, int idSucursal, Date fechaIni, Date fechaFin) throws ClientesException, SQLException {

        ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
        String query = "SELECT ci_numgrupo,ci_numciclo,ic_numcliente,ic_numsolicitud,ic_enviado,en_nombre_completo,de_montoautorizado,gr_nombre,su_numsucursal,su_nombre,en_nombre,en_primer_apellido,en_segundo_apellido,su_ofictelecom,su_cdtelecom,en_numero_identificacion "
                + "FROM d_ciclos_grupales "
                + "LEFT JOIN d_integrantes_ciclo ON (ci_numgrupo=ic_numgrupo AND ci_numciclo=ic_numciclo) "
                + "LEFT JOIN d_clientes ON (ic_numcliente=en_numcliente) "
                + "LEFT JOIN d_decision_comite ON (ic_numcliente=de_numcliente AND ic_numsolicitud=de_numsolicitud) "
                + "LEFT JOIN d_grupos ON (ci_numgrupo=gr_numgrupo) "
                + "LEFT JOIN c_sucursales ON (gr_numsucursal=su_numsucursal) "
                + "WHERE /*ci_numcredito_ibs=0 AND*/ ci_estatus=1 /*AND ci_desembolsado=0*/ AND LEFT(ci_fecha_captura,10) BETWEEN ? AND ? ";
        if (idBanco != 0) {
            query += "AND ci_banco_dispersion=? ";
        }
        if (idSucursal != 0) {
            query += "AND gr_numsucursal=? ";
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int param = 0;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(++param, fechaIni);
            ps.setDate(++param, fechaFin);
            if (idBanco != 0) {
                ps.setInt(++param, idBanco);
            }
            if (idSucursal != 0) {
                ps.setInt(++param, idSucursal);
            }
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                arrOrdenes.add(new OrdenDePagoVO(0, res.getInt("ic_numcliente"), res.getInt("ic_numsolicitud"), res.getString("en_nombre_completo"), res.getDouble("de_montoautorizado"), res.getInt("ic_enviado"), res.getString("su_nombre"),
                        res.getString("gr_nombre"), res.getInt("su_numsucursal"), res.getString("en_nombre"), res.getString("en_primer_apellido"), res.getString("en_segundo_apellido"), res.getInt("su_ofictelecom"), res.getString("su_cdtelecom"), res.getString("en_numero_identificacion"), ""));
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getOrdenesPago", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getOrdenesPago", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return arrOrdenes;
    }

    public ArrayList<OrdenDePagoVO> getOrdenesPagoEnviadas(int numGrupo, int numCiclo) throws ClientesException, SQLException {
        ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
        String query = "SELECT d_ordenes_de_pago.* FROM d_integrantes_ciclo LEFT JOIN d_ordenes_de_pago "
                + "ON (ic_numcliente=pg_numcliente AND ic_numsolicitud=pg_numsolicitud) "
                + "WHERE ic_numgrupo=? AND ic_numciclo=? AND pg_estatus IN(2,6)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Parametros [" + numGrupo + "," + numCiclo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrOrdenes.add(new OrdenDePagoVO(res.getString("pg_referencia"), res.getInt("pg_numcliente"), res.getInt("pg_numsolicitud"), res.getInt("pg_numsucursal"),
                        res.getString("pg_nombre_cliente"), res.getDouble("pg_monto"), res.getInt("pg_banco"), res.getInt("pg_estatus")));
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getOrdenesPagoEnviadas", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getOrdenesPagoEnviadas", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return arrOrdenes;
    }

    public ArrayList<OrdenDePagoVO> getOrdenesPagoCliente(int numCliente, int numSolicitud) throws ClientesException, SQLException {
        ArrayList<OrdenDePagoVO> ordenesPago = new ArrayList<OrdenDePagoVO>();
        OrdenDePagoVO oPago = new OrdenDePagoVO();
        String query = "SELECT * FROM d_ordenes_de_pago WHERE pg_numcliente=? AND pg_numsolicitud=? ORDER BY pg_fecha_captura ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numSolicitud);
            myLogger.debug("Ejecutando = " + ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                ordenesPago.add(oPago);
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getOrdenesPagoCliente", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getOrdenesPagoCliente", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return ordenesPago;
    }

    public boolean cancelaOrdenesEnviadas(int numGrupo, int numCiclo) throws ClientesException, SQLException {
        boolean actualiza = false;
        String query = "UPDATE d_ordenes_de_pago LEFT JOIN d_integrantes_ciclo "
                + "ON (pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud) "
                + "LEFT JOIN d_solicitudes "
                + "ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                + "SET pg_estatus=4, so_desembolsado=3, ic_estatus=2 "
                + "WHERE ic_numgrupo=? AND ic_numciclo=? AND pg_estatus IN(2,6)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numGrupo + "," + numCiclo + "]");
            if (!ps.execute()) {
                actualiza = true;
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("cancelaOrdenesEnviadas", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("cancelaOrdenesEnviadas", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return actualiza;
    }

    public boolean diferenteDeDesembolsado(int numEquipo, int numCiclo) throws ClientesException, SQLException {
        boolean diferente = false;
        String query = "SELECT d_ordenes_de_pago.* FROM d_ordenes_de_pago,d_integrantes_ciclo " +
                "WHERE pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud " +
                "AND ic_numgrupo=? AND ic_numciclo=? AND pg_estatus!=8";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug(ps.toString());
            rs=ps.executeQuery();
            if(rs.next()){
                diferente=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException",sqle);
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally{
            if(con!=null){
                con.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return diferente;
    }
    
    public boolean existeOdpCiclo(int numEquipo, int numCiclo) throws ClientesException, SQLException {
        boolean respuesta = false;
        String query = "SELECT d_ordenes_de_pago.* FROM d_ordenes_de_pago,d_integrantes_ciclo " +
                "WHERE pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud " +
                "AND ic_numgrupo=? AND ic_numciclo=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejectuando: "+ps.toString());
            rs=ps.executeQuery();
            if(rs.next()){
                respuesta=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(con!=null){
                con.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return respuesta;
    }
    
    public boolean odpNoCancelada(int numEquipo, int numCiclo) throws ClientesException, SQLException {
        boolean respuesta = false;
        String query = "SELECT * FROM d_ordenes_de_pago LEFT JOIN d_integrantes_ciclo " +
                "ON (pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud) " +
                "WHERE ic_numgrupo=? AND ic_numciclo=? "+
                "AND ((pg_banco NOT IN(2,3) AND pg_estatus NOT IN(5,7))OR(pg_banco IN(2,3) AND pg_estatus!=7))";
                //"AND pg_estatus!=7;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs=ps.executeQuery();
            if(rs.next()){
                respuesta=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(con!=null){
                con.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return respuesta;
    }
    
    public boolean odpEnviadas(int numEquipo, int numCiclo) throws ClientesException, SQLException {
        boolean respuesta = false;
        String query = "SELECT * FROM d_ordenes_de_pago LEFT JOIN d_integrantes_ciclo ON (pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud) " +
                "WHERE ic_numgrupo=? AND ic_numciclo=? AND pg_estatus!=1";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs=ps.executeQuery();
            if(rs.next()){
                respuesta=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(con!=null){
                con.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return respuesta;
    }
    
    public boolean odpNoEnviadas(int numEquipo, int numCiclo) throws ClientesException, SQLException {
        boolean respuesta = false;
        String query = "SELECT * FROM d_ordenes_de_pago LEFT JOIN d_integrantes_ciclo ON (pg_numcliente=ic_numcliente AND pg_numsolicitud=ic_numsolicitud) " +
                "WHERE ic_numgrupo=? AND ic_numciclo=? AND pg_estatus!=2";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs=ps.executeQuery();
            if(rs.next()){
                respuesta=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(con!=null){
                con.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return respuesta;
    }
    
    public int getEnvioDiaTelecom(Date fecha) throws ClientesException, SQLException {

        int envio = 0;
        String query = "SELECT MAX(te_enviodia) AS enviodia FROM d_ordenes_telecom WHERE te_fechaenvio=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, fecha);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                envio = res.getInt("enviodia");
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getEnvioDiaTelecom", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEnvioDiaTelecom", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return envio;
    }

    public int getEnvioClienteTelecom() throws ClientesException, SQLException {

        int envio = 0;
        String query = "SELECT MAX(te_enviocliente) AS enviocliente FROM d_ordenes_telecom";
        Connection con = null;
        Statement st = null;
        ResultSet res = null;
        try {
            con = getConnection();
            st = con.createStatement();
            myLogger.debug("Ejecutando = " + query);
            res = st.executeQuery(query);
            while (res.next()) {
                envio = res.getInt("enviocliente");
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getEnvioClienteTelecom", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEnvioClienteTelecom", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return envio;
    }

    public int setOrdenTelecom(OrdenDePagoVO orden) throws ClientesException, SQLException {

        String query = "INSERT INTO d_ordenes_telecom VALUES (0,?,?,?,?,?,?,?,?);";
        Connection con = null;
        PreparedStatement ps = null;
        int res = 0;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, orden.getReferencia());
            ps.setDate(2, orden.getFecha());
            ps.setInt(3, orden.getIdOperacion());
            ps.setInt(4, orden.getIdOrdenPago());
            ps.setInt(5, orden.getIdCliente());
            ps.setInt(6, orden.getIdSolicitud());
            ps.setString(7, orden.getNombre());
            ps.setDouble(8, orden.getMonto());
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("setOrdenTelecom", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("setOrdenTelecom", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return res;
    }

    public String getReferenciaTelecom(int idCliente, int idSolicitud) throws ClientesException, SQLException {

        String query = "SELECT te_referencia FROM d_ordenes_telecom WHERE te_numcliente=? AND te_numsolicitud=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String refe = "";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                refe = res.getString("te_referencia");
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getReferenciaTelecom", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getReferenciaTelecom", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return refe;
    }

    public void updateEstatusODP(int idCliente, int idSolicitud, String referencia, int estatus) throws ClientesException, SQLException {

        String query = "UPDATE d_ordenes_de_pago SET pg_estatus=? WHERE pg_numcliente=? AND pg_numsolicitud=? AND pg_referencia=?;";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, estatus);
            ps.setInt(2, idCliente);
            ps.setInt(3, idSolicitud);
            ps.setString(4, referencia);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("updateEstatusODP", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusODP", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
    }
    
    public OrdenDePagoVO getOrdenDePago(String referencia, int idCliente, double monto) throws ClientesException {
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrdenDePagoVO oPago = null;
        String query = "SELECT d_ordenes_de_pago.*,so_numoperacion 'operacion',op_nombre AS 'producto',su_nombre AS 'sucursal',co_descripcion AS 'estatus',gr_nombre AS 'grupo' " 
                +"FROM d_ordenes_de_pago JOIN c_sucursales ON (pg_numsucursal=su_numsucursal) JOIN c_estatus_ordenes_pago ON (pg_estatus=co_num_estatus) "
                +"JOIN (d_solicitudes JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) ON (pg_numcliente=so_numcliente AND pg_numsolicitud=so_numsolicitud) "
                +"LEFT JOIN (d_integrantes_ciclo JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                +"WHERE pg_referencia LIKE ? AND pg_numcliente=? AND pg_monto=? ORDER BY pg_identificador DESC LIMIT 1;";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, referencia+"%");
            ps.setInt(2, idCliente);
            ps.setDouble(3, monto);
            rs = ps.executeQuery();
            if (rs.next()) {
                oPago = new OrdenDePagoVO();
                oPago.setIdOrdenPago(rs.getInt("pg_identificador"));
                oPago.setReferencia(rs.getString("pg_referencia"));
                oPago.setIdCliente(rs.getInt("pg_numcliente"));
                oPago.setIdSolicitud(rs.getInt("pg_numsolicitud"));
                oPago.setIdSucursal(rs.getInt("pg_numsucursal"));
                oPago.setNombre(rs.getString("pg_nombre_cliente"));
                oPago.setMonto(rs.getDouble("pg_monto"));
                oPago.setFechaCaptura(rs.getTimestamp("pg_fecha_captura"));
                oPago.setFechaEnvio(rs.getTimestamp("pg_fecha_envio"));
                oPago.setIdBanco(rs.getInt("pg_banco"));
                oPago.setEstatus(rs.getInt("pg_estatus"));
                oPago.setNombreArchivo(rs.getString("pg_nombre_archivo"));
                oPago.setUsuario(rs.getString("pg_usuario"));
                oPago.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                oPago.setDescEstatus(rs.getString("estatus"));
                oPago.setGrupo(rs.getString("grupo") != null ? rs.getString("grupo") : "--");
            }
        } catch (Exception e) {
            myLogger.error("getOrdenDePago", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return oPago;
    }
    
    /**
     * JECB 01/10/2017
     * Método que indica si un cliente cuenta ordenes de pago relacionadas con un credito
     * adicional
     * @param oPago bean del tipo OrdenDePagoVO
     * @return True en caso de que el cliente tenga ordenes de pago de credito adicional,
     * en cualquier otro caso false
     * @throws ClientesDBException 
     */
    public boolean clienteCuentaConOrdenesDePagoDeCreditoAdicional(OrdenDePagoVO oPago) throws ClientesDBException{
        boolean res = false;
        int count = 0;
        Connection conn = null;

        String query
                = "select count(*) from d_ordenes_de_pago " +
                    "WHERE pg_numcliente   = ? " +
                    "AND pg_numsucursal  = ? and SUBSTRING(pg_referencia, -1) = '8'";
               

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, oPago.getIdCliente());
            ps.setInt(2, oPago.getIdSucursal());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + oPago.getIdCliente() + ", " + oPago.getReferencia() + ", " + oPago.getIdSucursal() + "]");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            if(count > 0){
                res = true;
            }
        } catch (Exception e) {
            myLogger.error("clienteCuentaConOrdenesDePagoDeCreditoAdicional error:", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("clienteCuentaConOrdenesDePagoDeCreditoAdicional error:", exc);
            }
        }
        return res;
    }
    
    /**
     * Metodo que obtiene las odenes de pago por su grupo, ciclo y en su defecto por el id del cliente
     * @param numGrupo
     * @param numCiclo
     * @param idClientes
     * @return
     * @throws ClientesException
     * @throws SQLException 
     */
    public ArrayList<OrdenDePagoVO> getOrdenesPagoXGrupoCicloIdCliente(int numGrupo, int numCiclo, String idClientes) throws ClientesException, SQLException {
        ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
        String query = "SELECT d_ordenes_de_pago.* FROM d_integrantes_ciclo LEFT JOIN d_ordenes_de_pago "
                + "ON (ic_numcliente=pg_numcliente AND ic_numsolicitud=pg_numsolicitud) "
                + "WHERE ic_numgrupo=? AND ic_numciclo=? ";
        
        if(null != idClientes  && !idClientes.isEmpty()){
           query = query + "AND pg_numcliente IN("+idClientes+")"; 
        }
        
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Parametros [" + numGrupo + "," + numCiclo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrOrdenes.add(new OrdenDePagoVO(res.getString("pg_referencia"), res.getInt("pg_numcliente"), res.getInt("pg_numsolicitud"), res.getInt("pg_numsucursal"),
                        res.getString("pg_nombre_cliente"), res.getDouble("pg_monto"), res.getInt("pg_banco"), res.getInt("pg_estatus"), res.getTimestamp("pg_fecha_envio")));
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getOrdenesPagoEnviadas", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getOrdenesPagoEnviadas", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return arrOrdenes;
    }
}

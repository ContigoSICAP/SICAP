package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SegurosVO;
import java.sql.Savepoint;
import org.apache.log4j.Logger;

public class SegurosDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(SegurosDAO.class);

    public ArrayList<SegurosVO> getSeguros(Date vencimiento) throws ClientesException {

        String query = "SELECT SE_NUMCLIENTE, SE_NUMSOLICITUD, SE_NUMSEGURO, SE_SUMA_ASEGURADA, SE_MODULOS, SE_CONTRATACION, SE_FECHA_FIRMA, SE_COMENTARIOS, SE_FECHA_CAPTURA, "
                + " SE_FECHA_CONTRATACION, SE_FECHA_VENCIMIENTO, SE_FECHA_PROXIMO_VENCIMIENTO, SE_VALOR_MODULO, SE_PRIMA, SE_PRIMA_TOTAL, SE_NUMERO_CUOTAS_TRANSCURRIDAS, "
                + " SE_NUMERO_CUOTAS_VIGENTES, SE_NUMERO_CUOTAS_VENCIDAS, SE_NUMERO_CUOTAS_RESTANTES, SE_SALDO_INSOLUTO, SE_SALDO_ACTUAL,"
                + " SE_FECHA_ULTIMO_PAGO, SE_ESTATUS "
                + " FROM D_SEGUROS WHERE SE_FECHA_PROXIMO_VENCIMIENTO = ?";

        ArrayList<SegurosVO> seguros = new ArrayList<SegurosVO>();
        SegurosVO seg = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(1, (java.sql.Date) vencimiento);

            rs = ps.executeQuery();
            while (rs.next()) {
                seg = new SegurosVO();
                seg.idCliente = rs.getInt("SE_NUMCLIENTE");
                seg.idSolicitud = rs.getInt("SE_NUMSOLICITUD");
                seg.numSeguro = rs.getInt("SE_NUMSEGURO");
                seg.sumaAsegurada = rs.getInt("SE_SUMA_ASEGURADA");
                seg.modulos = rs.getInt("SE_MODULOS");
                seg.contratacion = rs.getInt("SE_CONTRATACION");
                seg.fechaFirma = rs.getDate("SE_FECHA_FIRMA");
                seg.comentarios = rs.getString("SE_COMENTARIOS");
                seg.fechaCaptura = rs.getTimestamp("SE_FECHA_CAPTURA");
                seg.fechaContratacion = rs.getDate("SE_FECHA_CONTRATACION");
                seg.fechaVencimiento = rs.getDate("SE_FECHA_VENCIMIENTO");
                seg.fechaProximoVencimiento = rs.getDate("SE_FECHA_PROXIMO_VENCIMIENTO");
                seg.valorModulo = rs.getInt("SE_VALOR_MODULO");
                seg.prima = rs.getDouble("SE_PRIMA");
                seg.primaTotal = rs.getDouble("SE_PRIMA_TOTAL");
                seg.numCuotasTranscurridas = rs.getInt("SE_NUMERO_CUOTAS_TRANSCURRIDAS");
                seg.numCuotasVigentes = rs.getInt("SE_NUMERO_CUOTAS_VIGENTES");
                seg.numCuotasVencidas = rs.getInt("SE_NUMERO_CUOTAS_VENCIDAS");
                seg.numCuotasRestantes = rs.getInt("SE_NUMERO_CUOTAS_RESTANTES");
                seg.saldoInsoluto = rs.getDouble("SE_SALDO_INSOLUTO");
                seg.saldoActual = rs.getDouble("SE_SALDO_ACTUAL");
                seg.fechaUltimoPago = rs.getDate("SE_FECHA_ULTIMO_PAGO");
                seg.estatus = rs.getInt("SE_ESTATUS");
                seguros.add(seg);
                myLogger.debug("Ejecutando = " + query);
                myLogger.debug("Parametro: " + vencimiento);
                myLogger.debug("Seguros: " + seg);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSeguros", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSeguros", e);
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
        return seguros;

    }

    //Retorna una lista de seguros vivos: vencidos y vigentes
    public ArrayList<SegurosVO> getSegurosVigentes() throws ClientesException {

        String query = "SELECT SE_NUMCLIENTE, SE_NUMSOLICITUD, SE_NUMSEGURO, SE_SUMA_ASEGURADA, SE_MODULOS, SE_CONTRATACION, SE_FECHA_FIRMA, SE_COMENTARIOS, SE_FECHA_CAPTURA, "
                + " SE_FECHA_VENCIMIENTO, SE_FECHA_PROXIMO_VENCIMIENTO, SE_VALOR_MODULO, SE_PRIMA, SE_PRIMA_TOTAL, SE_NUMERO_CUOTAS_TRANSCURRIDAS, "
                + " SE_NUMERO_CUOTAS_VIGENTES, SE_NUMERO_CUOTAS_VENCIDAS, SE_NUMERO_CUOTAS_RESTANTES, SE_SALDO_INSOLUTO, SE_SALDO_ACTUAL,"
                + " SE_FECHA_ULTIMO_PAGO, SE_ESTATUS "
                + " FROM D_SEGUROS WHERE SE_ESTATUS IN(?,?)";
		//" FROM D_SEGUROS WHERE SE_ESTATUS=?";

        ArrayList<SegurosVO> seguros = new ArrayList<SegurosVO>();
        SegurosVO seg = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, ClientesConstants.SEGURO_VIGENTE);
            ps.setInt(2, ClientesConstants.SEGURO_VENCIDO);
			//ps.setInt(3, 0);

            rs = ps.executeQuery();
            while (rs.next()) {
                seg = new SegurosVO();
                seg.idCliente = rs.getInt("SE_NUMCLIENTE");
                seg.idSolicitud = rs.getInt("SE_NUMSOLICITUD");
                seg.numSeguro = rs.getInt("SE_NUMSEGURO");
                seg.sumaAsegurada = rs.getInt("SE_SUMA_ASEGURADA");
                seg.modulos = rs.getInt("SE_MODULOS");
                seg.contratacion = rs.getInt("SE_CONTRATACION");
                seg.fechaFirma = rs.getDate("SE_FECHA_FIRMA");
                seg.comentarios = rs.getString("SE_COMENTARIOS");
                seg.fechaCaptura = rs.getTimestamp("SE_FECHA_CAPTURA");
                seg.fechaVencimiento = rs.getDate("SE_FECHA_VENCIMIENTO");
                seg.fechaProximoVencimiento = rs.getDate("SE_FECHA_PROXIMO_VENCIMIENTO");
                seg.valorModulo = rs.getInt("SE_VALOR_MODULO");
                seg.prima = rs.getDouble("SE_PRIMA");
                seg.primaTotal = rs.getDouble("SE_PRIMA_TOTAL");
                seg.numCuotasTranscurridas = rs.getInt("SE_NUMERO_CUOTAS_TRANSCURRIDAS");
                seg.numCuotasVigentes = rs.getInt("SE_NUMERO_CUOTAS_VIGENTES");
                seg.numCuotasVencidas = rs.getInt("SE_NUMERO_CUOTAS_VENCIDAS");
                seg.numCuotasRestantes = rs.getInt("SE_NUMERO_CUOTAS_RESTANTES");
                seg.saldoInsoluto = rs.getDouble("SE_SALDO_INSOLUTO");
                seg.saldoActual = rs.getDouble("SE_SALDO_ACTUAL");
                seg.fechaUltimoPago = rs.getDate("SE_FECHA_ULTIMO_PAGO");
                seg.estatus = rs.getInt("SE_ESTATUS");
                seguros.add(seg);
                myLogger.debug("Ejecutando = " + query);
                myLogger.debug("Seguros: " + seg);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSegurosVigentes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSegurosVigentes", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("getSegurosVigentes", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return seguros;

    }

    public SegurosVO getSeguros(int idCliente, int idSolicitud) throws ClientesException {
        SegurosVO seguro = null;
        Connection cn = null;

        String query = "SELECT SE_NUMSEGURO, SE_SUMA_ASEGURADA, SE_MODULOS, SE_CONTRATACION, SE_FECHA_FIRMA, SE_COMENTARIOS, SE_FECHA_CAPTURA, "
                + " SE_FECHA_VENCIMIENTO, SE_FECHA_PROXIMO_VENCIMIENTO, SE_VALOR_MODULO, SE_PRIMA, SE_PRIMA_TOTAL, SE_NUMERO_CUOTAS_TRANSCURRIDAS, "
                + " SE_NUMERO_CUOTAS_VIGENTES, SE_NUMERO_CUOTAS_VENCIDAS, SE_NUMERO_CUOTAS_RESTANTES, SE_SALDO_INSOLUTO, SE_SALDO_ACTUAL,"
                + " SE_FECHA_ULTIMO_PAGO, SE_ESTATUS, SE_BANCO, SE_CUENTA "
                + " FROM D_SEGUROS WHERE SE_NUMCLIENTE = ? AND SE_NUMSOLICITUD = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idCliente + ", " + idSolicitud + "]");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                seguro = new SegurosVO();
                seguro.idCliente = idCliente;
                seguro.idSolicitud = idSolicitud;
                seguro.numSeguro = rs.getInt("SE_NUMSEGURO");
                seguro.sumaAsegurada = rs.getInt("SE_SUMA_ASEGURADA");
                seguro.modulos = rs.getInt("SE_MODULOS");
                seguro.contratacion = rs.getInt("SE_CONTRATACION");
                seguro.fechaFirma = rs.getDate("SE_FECHA_FIRMA");
                seguro.comentarios = rs.getString("SE_COMENTARIOS");
                seguro.fechaCaptura = rs.getTimestamp("SE_FECHA_CAPTURA");
                seguro.fechaVencimiento = rs.getDate("SE_FECHA_VENCIMIENTO");
                seguro.fechaProximoVencimiento = rs.getDate("SE_FECHA_PROXIMO_VENCIMIENTO");
                seguro.valorModulo = rs.getInt("SE_VALOR_MODULO");
                seguro.prima = rs.getDouble("SE_PRIMA");
                seguro.primaTotal = rs.getDouble("SE_PRIMA_TOTAL");
                seguro.numCuotasTranscurridas = rs.getInt("SE_NUMERO_CUOTAS_TRANSCURRIDAS");
                seguro.numCuotasVigentes = rs.getInt("SE_NUMERO_CUOTAS_VIGENTES");
                seguro.numCuotasVencidas = rs.getInt("SE_NUMERO_CUOTAS_VENCIDAS");
                seguro.numCuotasRestantes = rs.getInt("SE_NUMERO_CUOTAS_RESTANTES");
                seguro.saldoInsoluto = rs.getDouble("SE_SALDO_INSOLUTO");
                seguro.saldoActual = rs.getDouble("SE_SALDO_ACTUAL");
                seguro.fechaUltimoPago = rs.getDate("SE_FECHA_ULTIMO_PAGO");
                seguro.estatus = rs.getInt("SE_ESTATUS");
                seguro.banco = rs.getInt("SE_BANCO");
                seguro.cuenta = rs.getInt("SE_CUENTA");
            }
        } catch (SQLException sqle) {
            myLogger.error("getSeguros", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSeguros", e);
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
        /*elementos = new SegurosVO[array.size()];
         for(int i=0;i<elementos.length; i++) elementos[i] = (SegurosVO)array.get(i);*/

        return seguro;
    }

    public int addSeguro(Connection cn, Savepoint save, SegurosVO seguro) throws ClientesException {

        String query = "INSERT INTO D_SEGUROS (SE_NUMCLIENTE, SE_NUMSOLICITUD, SE_NUMSEGURO, SE_SUMA_ASEGURADA,"
                + "SE_MODULOS, SE_CONTRATACION, SE_FECHA_FIRMA, SE_COMENTARIOS, SE_FECHA_CAPTURA, SE_FECHA_VENCIMIENTO, "
                + "SE_FECHA_PROXIMO_VENCIMIENTO, SE_VALOR_MODULO, SE_PRIMA, SE_PRIMA_TOTAL, SE_NUMERO_CUOTAS_TRANSCURRIDAS, SE_NUMERO_CUOTAS_VIGENTES, "
                + "SE_NUMERO_CUOTAS_VENCIDAS, SE_NUMERO_CUOTAS_RESTANTES, SE_SALDO_INSOLUTO, SE_SALDO_ACTUAL, SE_FECHA_ULTIMO_PAGO,"
                + "SE_ESTATUS, SE_PRIMA_TEMPO, SE_BANCO, SE_CUENTA) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Connection cn = null;
        int res = 0;
        try {
            PreparedStatement ps = null;
            //cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, seguro.idCliente);
            ps.setInt(2, seguro.idSolicitud);
            ps.setInt(3, seguro.numSeguro);
            ps.setInt(4, seguro.sumaAsegurada);
            ps.setInt(5, seguro.modulos);
            ps.setInt(6, seguro.contratacion);
            ps.setDate(7, seguro.fechaFirma);
            ps.setString(8, seguro.comentarios);
            //param++;   //Es el CURRENT_TIMESTAMP
            ps.setDate(9, seguro.fechaVencimiento);
            ps.setDate(10, seguro.fechaProximoVencimiento);
            ps.setInt(11, seguro.valorModulo);
            ps.setDouble(12, seguro.prima);
            //ps.setDouble(param++, seguro.primaTotal);
            ps.setDouble(13, 0);
            ps.setInt(14, seguro.numCuotasTranscurridas);
            ps.setInt(15, seguro.numCuotasVigentes);
            ps.setInt(16, seguro.numCuotasVencidas);
            ps.setInt(17, seguro.numCuotasRestantes);
            ps.setDouble(18, seguro.saldoInsoluto);
            ps.setDouble(19, seguro.saldoActual);
            ps.setDate(20, seguro.fechaUltimoPago);
            ps.setInt(21, seguro.estatus);
            ps.setDouble(22, seguro.primaTotal);
            ps.setInt(23, seguro.banco);
            ps.setInt(24, seguro.cuenta);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Conyuge= " + seguro.toString());
            res = ps.executeUpdate();
            //Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("addSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addSeguro", e);
            throw new ClientesException(e.getMessage());
        }
        /*finally {
         try {
         if ( cn!=null ) cn.close();
         }
         catch(SQLException sqle) {
         throw new ClientesDBException(sqle.getMessage());
         }
         }*/
        return res;
    }

    public int updateSeguro(SegurosVO seguro) throws ClientesException {
        return updateSeguro(null, seguro);
    }

    public int updateSeguro(Connection conn, Savepoint save, SegurosVO seguro) throws ClientesException {

        String query = "UPDATE D_SEGUROS SET SE_SUMA_ASEGURADA = ?, SE_MODULOS = ?, SE_CONTRATACION = ?, "
                + "SE_FECHA_FIRMA = ?, SE_COMENTARIOS = ?,SE_FECHA_CONTRATACION = ? ,SE_FECHA_VENCIMIENTO = ?, SE_FECHA_PROXIMO_VENCIMIENTO = ?,"
                + "SE_VALOR_MODULO = ?, SE_PRIMA = ?, se_prima_tempo = ?, SE_PRIMA_TOTAL = ?, SE_NUMERO_CUOTAS_TRANSCURRIDAS = ?, SE_NUMERO_CUOTAS_VIGENTES = ?, "
                + "SE_NUMERO_CUOTAS_VENCIDAS = ?, SE_NUMERO_CUOTAS_RESTANTES = ?, SE_SALDO_INSOLUTO = ?, SE_SALDO_ACTUAL = ?,"
                + "SE_FECHA_ULTIMO_PAGO = ?, SE_ESTATUS = ?, SE_BANCO = ?, SE_CUENTA = ?"
                + " WHERE SE_NUMCLIENTE = ? AND SE_NUMSOLICITUD = ? AND SE_NUMSEGURO = ?";

        //Connection cn = null;
        PreparedStatement ps = null;
        int res = 0;
        int param = 1;
        try {
            /*if( conn==null ){
             cn = getConnection();
             ps = cn.prepareStatement(query); 
             }
             else{*/
            ps = conn.prepareStatement(query);
            //}
            ps.setInt(param++, seguro.sumaAsegurada);
            ps.setInt(param++, seguro.modulos);
            ps.setInt(param++, seguro.contratacion);
            ps.setDate(param++, seguro.fechaFirma);
            ps.setString(param++, seguro.comentarios);
            ps.setDate(param++, seguro.fechaContratacion);
            ps.setDate(param++, seguro.fechaVencimiento);
            ps.setDate(param++, seguro.fechaProximoVencimiento);
            ps.setInt(param++, seguro.valorModulo);
            ps.setDouble(param++, seguro.prima);
            ps.setDouble(param++, seguro.primaTotal);
            ps.setDouble(param++, 0);
            ps.setInt(param++, seguro.numCuotasTranscurridas);
            ps.setInt(param++, seguro.numCuotasVigentes);
            ps.setInt(param++, seguro.numCuotasVencidas);
            ps.setInt(param++, seguro.numCuotasRestantes);
            ps.setDouble(param++, seguro.saldoInsoluto);
            ps.setDouble(param++, seguro.saldoActual);
            ps.setDate(param++, seguro.fechaUltimoPago);
            ps.setInt(param++, seguro.estatus);
            ps.setInt(param++, seguro.banco);
            ps.setInt(param++, seguro.cuenta);
            ps.setInt(param++, seguro.idCliente);
            ps.setInt(param++, seguro.idSolicitud);
            ps.setInt(param++, seguro.numSeguro);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Seguro = " + seguro.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSeguro", e);
            throw new ClientesException(e.getMessage());
        }
        /*finally {
         try {
         if ( conn!=null ) conn.close();
         }
         catch(SQLException sqle) {
         throw new ClientesDBException(sqle.getMessage());
         }
         }*/
        return res;
    }

    public int updateSeguro(Connection conn, SegurosVO seguro) throws ClientesException {

        String query = "UPDATE D_SEGUROS SET SE_SUMA_ASEGURADA = ?, SE_MODULOS = ?, SE_CONTRATACION = ?, "
                + "SE_FECHA_FIRMA = ?, SE_COMENTARIOS = ?,SE_FECHA_CONTRATACION = ? ,SE_FECHA_VENCIMIENTO = ?, SE_FECHA_PROXIMO_VENCIMIENTO = ?,"
                + "SE_VALOR_MODULO = ?, SE_PRIMA = ?, SE_PRIMA_TOTAL = ?, SE_NUMERO_CUOTAS_TRANSCURRIDAS = ?, SE_NUMERO_CUOTAS_VIGENTES = ?, "
                + "SE_NUMERO_CUOTAS_VENCIDAS = ?, SE_NUMERO_CUOTAS_RESTANTES = ?, SE_SALDO_INSOLUTO = ?, SE_SALDO_ACTUAL = ?,"
                + "SE_FECHA_ULTIMO_PAGO = ?, SE_ESTATUS = ?, SE_BANCO = ?, SE_CUENTA = ?"
                + " WHERE SE_NUMCLIENTE = ? AND SE_NUMSOLICITUD = ? AND SE_NUMSEGURO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int res = 0;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = cn.prepareStatement(query);
            }
            ps.setInt(param++, seguro.sumaAsegurada);
            ps.setInt(param++, seguro.modulos);
            ps.setInt(param++, seguro.contratacion);
            ps.setDate(param++, seguro.fechaFirma);
            ps.setString(param++, seguro.comentarios);
            ps.setDate(param++, seguro.fechaContratacion);
            ps.setDate(param++, seguro.fechaVencimiento);
            ps.setDate(param++, seguro.fechaProximoVencimiento);
            ps.setInt(param++, seguro.valorModulo);
            ps.setDouble(param++, seguro.prima);
            //ps.setDouble(param++, seguro.primaTotal);
            ps.setDouble(param++, 0);
            ps.setInt(param++, seguro.numCuotasTranscurridas);
            ps.setInt(param++, seguro.numCuotasVigentes);
            ps.setInt(param++, seguro.numCuotasVencidas);
            ps.setInt(param++, seguro.numCuotasRestantes);
            ps.setDouble(param++, seguro.saldoInsoluto);
            ps.setDouble(param++, seguro.saldoActual);
            ps.setDate(param++, seguro.fechaUltimoPago);
            ps.setInt(param++, seguro.estatus);
            ps.setInt(param++, seguro.banco);
            ps.setInt(param++, seguro.cuenta);

            ps.setInt(param++, seguro.idCliente);
            ps.setInt(param++, seguro.idSolicitud);
            ps.setInt(param++, seguro.numSeguro);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Seguro = " + seguro.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSeguro", e);
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

    public void deleteSeguro(SegurosVO seguro) throws ClientesException {
        String query = "DELETE FROM d_seguros WHERE se_numcliente=? AND se_numsolicitud=? AND se_numseguro=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, seguro.idCliente);
            ps.setInt(2, seguro.idSolicitud);
            ps.setInt(3, seguro.numSeguro);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + seguro.idCliente + ", " + seguro.idSolicitud + ", " + seguro.numSeguro + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteSeguro", e);
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
     public int deleteSeguro(Connection conn,int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM d_seguros" +
                       " where se_numcliente = ?" +
                       " and se_numsolicitud = ?";
        int param = 1;
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteSeguro", e);
            throw new ClientesException(e.getMessage());
        } 
        return res;
    }
    
    public void respaldaSegurosCiclo(int numEquipo, int numCiclo) throws ClientesException {
        String query = "insert into d_seguros_del (se_numcliente,se_numsolicitud,se_numseguro,se_suma_asegurada,se_modulos,se_contratacion,se_fecha_firma,se_comentarios,se_fecha_captura,se_fecha_contratacion,se_fecha_vencimiento,se_fecha_proximo_vencimiento,se_valor_modulo,se_prima,se_prima_total,se_numero_cuotas_transcurridas,se_numero_cuotas_vigentes,se_numero_cuotas_vencidas,se_numero_cuotas_restantes,se_saldo_insoluto,se_saldo_actual,se_fecha_ultimo_pago,se_estatus,se_prima_tempo,se_banco,se_cuenta) "
                + "(select se_numcliente,se_numsolicitud,se_numseguro,se_suma_asegurada,se_modulos,se_contratacion,se_fecha_firma,se_comentarios,se_fecha_captura,se_fecha_contratacion,se_fecha_vencimiento,se_fecha_proximo_vencimiento,se_valor_modulo,se_prima,se_prima_total,se_numero_cuotas_transcurridas,se_numero_cuotas_vigentes,se_numero_cuotas_vencidas,se_numero_cuotas_restantes,se_saldo_insoluto,se_saldo_actual,se_fecha_ultimo_pago,se_estatus,se_prima_tempo,se_banco,se_cuenta"
                + " FROM d_integrantes_ciclo LEFT JOIN d_solicitudes ON(ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) " +
                "LEFT JOIN d_seguros ON (so_numcliente=se_numcliente AND so_numsolicitud=se_numsolicitud) " +
                "WHERE se_numcliente is not null and se_numsolicitud is not null and ic_numgrupo=? AND ic_numciclo=?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + ps.toString());
            int rowAfected = ps.executeUpdate();
            myLogger.debug("Rows afectados: " + rowAfected);
        } catch (SQLException sqle) {
            myLogger.error("eliminaSegurosCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaSegurosCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

public void eliminaSegurosCiclo(int numEquipo, int numCiclo, boolean conexionODS) throws ClientesException {
        String query = "DELETE d_seguros.* FROM d_integrantes_ciclo LEFT JOIN d_solicitudes ON(ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) " +
                "LEFT JOIN d_seguros ON (so_numcliente=se_numcliente AND so_numsolicitud=se_numsolicitud) " +
                "WHERE ic_numgrupo=? AND ic_numciclo=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                con = getODSConnection();
            }else{
                con = getConnection();
            }
            ps = con.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaSegurosCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaSegurosCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    
    public int numSeguro(int idCliente) throws ClientesException {
        int numSeguro = 0;
        String query = "SELECT IFNULL(max(se_numseguro),0) as NUMSEGURO FROM d_seguros WHERE se_numcliente=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idCliente + "]");
            res = ps.executeQuery();
            if (res.next()) {
                numSeguro = res.getInt("NUMSEGURO");
            }
        } catch (SQLException sqle) {
            myLogger.error("numSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("numSeguro", e);
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
        return numSeguro;
    }

    public int compruebaSeguro(IntegranteCicloVO integrante) throws ClientesException {
        int numSeguro = 0;
        String query = "SELECT se_contratacion FROM d_seguros WHERE se_numcliente=? AND se_numsolicitud=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, integrante.idCliente);
            ps.setInt(2, integrante.idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + integrante.idCliente + ", " + integrante.idSolicitud + "]");
            res = ps.executeQuery();
            while (res.next()) {
                numSeguro = res.getInt("se_contratacion");
            }
        } catch (SQLException sqle) {
            myLogger.error("compruebaSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("compruebaSeguro", e);
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
        return numSeguro;
    }

    public int contratoSeguro(int idCliente, int idSolicitud) throws ClientesException {
        int idContrato = 0;
        String query = "SELECT se_contratacion FROM d_seguros WHERE se_numcliente=? AND se_numsolicitud=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            res = ps.executeQuery();
            if (res.next()) {
                idContrato = res.getInt("se_contratacion");
            }
        } catch (SQLException sqle) {
            myLogger.error("contratoSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("contratoSeguro", e);
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
        return idContrato;
    }
}

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import org.apache.log4j.Logger;

/**
 * Manejo de persistencia de la tabla d_pago_grupal
 *
 * @author JahTechnologies
 *
 */
public class PagoGrupalDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(PagoGrupalDAO.class);

    /**
     * Consulta de un gago grupal segun el grupo, ciclo y numero de pago
     *
     * @param numGrupo Numero de grupo
     * @param numCiclo Numero de ciclo
     * @param numPago Numero de pago
     * @return Clase entidad de pago grupal
     * @throws ClientesException
     */
    public PagoGrupalVO getPagoGrupal(int numGrupo, int numCiclo, int numPago) throws ClientesException {
        PagoGrupalVO pagoGrupal = null;
        Connection cn = null;
        String query = "SELECT *"
                + " FROM d_pago_grupal WHERE pg_numgrupo=? AND pg_numciclo=? AND pg_numpago=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
//			Logger.debug("Ejecutando = "+query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numPago);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagoGrupal = new PagoGrupalVO();
                pagoGrupal.numGrupo = rs.getInt("pg_numgrupo");
                pagoGrupal.numCiclo = rs.getInt("pg_numciclo");;
                pagoGrupal.numPago = rs.getInt("pg_numpago");;
                pagoGrupal.numTransaccion = rs.getInt("pg_numtransaccion");
                pagoGrupal.numAmortizacion = rs.getInt("pg_num_amortizacion");
                pagoGrupal.fechaPago = rs.getDate("pg_fechapago");
                pagoGrupal.monto = rs.getDouble("pg_monto");
//				Logger.debug("Pago esperado Individual encontrado : "+pagoGrupal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getPagoGrupal: ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getPagoGrupal: ", e);
            e.printStackTrace();
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
        return pagoGrupal;
    }

    /**
     * Consulta de todos los pagos grupales de un grupo y ciclo especifico
     *
     * @param numGrupo Numero de grupo
     * @param numCiclo Numero de ciclo
     * @return ArrayList de pagos grupales
     * @throws ClientesException
     */
    public ArrayList<PagoGrupalVO> getPagosGrupales(int numGrupo, int numCiclo) throws ClientesException {
        PagoGrupalVO pagoGrupal = null;
        ArrayList<PagoGrupalVO> pagos = new ArrayList<PagoGrupalVO>();
        Connection cn = null;
        String query = "SELECT * FROM D_PAGO_GRUPAL WHERE PG_NUMGRUPO=? AND PG_NUMCICLO=? ORDER BY PG_NUMPAGO";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagoGrupal = new PagoGrupalVO();
                pagoGrupal.numGrupo = rs.getInt("pg_numgrupo");
                pagoGrupal.numCiclo = rs.getInt("pg_numciclo");;
                pagoGrupal.numPago = rs.getInt("pg_numpago");;
                pagoGrupal.numTransaccion = rs.getInt("pg_numtransaccion");
                pagoGrupal.numAmortizacion = rs.getInt("pg_num_amortizacion");
                pagoGrupal.fechaPago = rs.getDate("pg_fechapago");
                pagoGrupal.monto = rs.getDouble("pg_monto");
                pagoGrupal.solidario = rs.getDouble("pg_solidario");
                pagoGrupal.ahorro = rs.getDouble("pg_ahorro");
                pagoGrupal.multa = rs.getDouble("pg_multa");

                pagos.add(pagoGrupal);
                //Logger.debug("Pago esperado Individual encontrado : "+pagoGrupal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getPagoGrupal: ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getPagoGrupal: ", e);
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
        return pagos;
    }

    public PagoGrupalVO[] getPagosGrupales_(int numGrupo, int numCiclo) throws ClientesException {
        PagoGrupalVO pagoGrupal = null;
        PagoGrupalVO[] elementos = null;
        ArrayList<PagoGrupalVO> pagos = new ArrayList<PagoGrupalVO>();
        Connection cn = null;
        String query = "SELECT * FROM D_PAGO_GRUPAL WHERE PG_NUMGRUPO=? AND PG_NUMCICLO=? ORDER BY PG_NUMPAGO";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagoGrupal = new PagoGrupalVO();
                pagoGrupal.numGrupo = rs.getInt("pg_numgrupo");
                pagoGrupal.numCiclo = rs.getInt("pg_numciclo");;
                pagoGrupal.numPago = rs.getInt("pg_numpago");;
                pagoGrupal.numTransaccion = rs.getInt("pg_numtransaccion");
                pagoGrupal.numAmortizacion = rs.getInt("pg_num_amortizacion");
                pagoGrupal.fechaPago = rs.getDate("pg_fechapago");
                pagoGrupal.monto = rs.getDouble("pg_monto");
                pagoGrupal.solidario = rs.getDouble("pg_solidario");
                pagoGrupal.ahorro = rs.getDouble("pg_ahorro");
                pagoGrupal.multa = rs.getDouble("pg_multa");
                pagos.add(pagoGrupal);
                if (pagos.size() > 0) {
                    elementos = new PagoGrupalVO[pagos.size()];
                    for (int i = 0; i < elementos.length; i++) {
                        elementos[i] = (PagoGrupalVO) pagos.get(i);
                    }
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getPagoGrupal: ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getPagoGrupal: ", e);
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
     * Obtiene el numero de pago actual para un grupo y ciclo especifico
     *
     * @param numGrupo Numero de grupo
     * @param numCiclo Numero de ciclo
     * @return Numero de pago actual
     * @throws ClientesException
     */
    public int getNumPagoGrupalActual(int numGrupo, int numCiclo) throws ClientesException {
        int resultado = 0;
        Connection cn = null;
        String query = "SELECT max(pg_numpago) as numpago FROM d_pago_grupal d where pg_numgrupo=? AND pg_numciclo=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getInt("numpago");
				//if(resultado==0) {	//si el resultado es cero indica que es el primer pago que se va a realizar
                //	resultado = 1;
                //}
                myLogger.debug("Pago  : " + resultado);
            }
            /*else {
             resultado = 1;	//si no hay pagos entonces quiere decir que es un primer pago
             Logger.debug("Pago  : "+ resultado);
             }*/
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getPagoGrupal: ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getPagoGrupal: ", e);
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
        return resultado;
    }

    /**
     * Inseta un pago grupal
     *
     * @param pagoGrupal Clase entidad de pago grupal
     * @return Numero 1 si la insercion fue correcta
     * @throws ClientesException
     */
    public int addPagoGrupal(PagoGrupalVO pagoGrupal) throws ClientesException {

        String query = "INSERT INTO d_pago_grupal (pg_numgrupo, pg_numciclo, pg_numtransaccion, pg_num_amortizacion, pg_numpago, pg_fechapago, pg_monto)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, pagoGrupal.numGrupo);
            ps.setInt(param++, pagoGrupal.numCiclo);
            ps.setInt(param++, pagoGrupal.numTransaccion);
            ps.setInt(param++, pagoGrupal.numAmortizacion);
            ps.setInt(param++, pagoGrupal.numPago);
            ps.setDate(param++, pagoGrupal.fechaPago);
            ps.setDouble(param++, pagoGrupal.monto);
            myLogger.debug("Ejecutando: "+ ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = "+ res);
        } catch (SQLException sqle) {
            myLogger.error("SQLException en addPagoGrupal: ", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en addPagoGrupal: ", e);
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

    public boolean addPagoGrupal(PagoGrupalVO pagoGrupal, Connection con) throws ClientesException {

        String query = "INSERT INTO d_pago_grupal (pg_numgrupo, pg_numciclo, pg_numtransaccion, pg_num_amortizacion, pg_numpago, pg_fechapago, pg_monto)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int param = 1;
        boolean listo = true;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(param++, pagoGrupal.numGrupo);
            ps.setInt(param++, pagoGrupal.numCiclo);
            ps.setInt(param++, pagoGrupal.numTransaccion);
            ps.setInt(param++, pagoGrupal.numAmortizacion);
            ps.setInt(param++, pagoGrupal.numPago);
            ps.setDate(param++, pagoGrupal.fechaPago);
            ps.setDouble(param++, pagoGrupal.monto);
            myLogger.debug("Ejecutando: "+ ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException en addPagoGrupal: ", sqle);
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

    public int updatePagoGrupal(PagoGrupalVO pagoGrupal) throws ClientesException {

        String query = "UPDATE D_PAGO_GRUPAL SET PG_SOLIDARIO = ?, PG_AHORRO = ?, PG_MULTA = ?, PG_NUM_AMORTIZACION = ? "
                + "WHERE PG_NUMGRUPO = ? AND PG_NUMCICLO = ? AND PG_NUMPAGO = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(param++, pagoGrupal.solidario);
            ps.setDouble(param++, pagoGrupal.ahorro);
            ps.setDouble(param++, pagoGrupal.multa);
            ps.setInt(param++, pagoGrupal.numAmortizacion);
            ps.setInt(param++, pagoGrupal.numGrupo);
            ps.setInt(param++, pagoGrupal.numCiclo);
            ps.setInt(param++, pagoGrupal.numPago);
            myLogger.debug("Ejecutando: "+ ps);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException en updatePagoGrupal: ", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en updatePagoGrupal: ", e);
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
    
    public PagoGrupalVO getPagoGrupalMonto(int numGrupo, int numCiclo, PagoVO pago) throws ClientesException, SQLException {
        
        PagoGrupalVO pagoGrupal = new PagoGrupalVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT pg_numgrupo,pg_numciclo,pg_numpago,pg_num_amortizacion FROM d_pago_grupal WHERE pg_numgrupo=? AND pg_numciclo=? AND pg_monto=? AND pg_fechapago=? ORDER BY pg_numpago DESC;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            ps.setDouble(3, pago.getMonto());
            ps.setDate(4, pago.getFechaPago());
            myLogger.debug("Ejecutando "+ps);
            res = ps.executeQuery();
            while (res.next()) {
                pagoGrupal = new PagoGrupalVO();
                pagoGrupal.setNumGrupo(res.getInt("pg_numgrupo"));
                pagoGrupal.setNumCiclo(res.getInt("pg_numciclo"));
                pagoGrupal.setNumPago(res.getInt("pg_numpago"));
                pagoGrupal.setNumAmortizacion(res.getInt("pg_num_amortizacion"));
            }
        } catch (SQLException sqle) {
            myLogger.debug("SQLException en getPagoGrupalMonto: ", sqle);
        } catch (Exception e) {
            myLogger.error("Excepcion en getPagoGrupalMonto: ", e);
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
        return pagoGrupal;
    }
    
    public ArrayList<PagoGrupalVO> getArrPagoGrupal(int numGrupo, int numCiclo, int numPago) throws ClientesException, SQLException {
        
        ArrayList<PagoGrupalVO> arrPagoGrupal = new ArrayList<PagoGrupalVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT pg_numgrupo,pg_numciclo,pg_numtransaccion,pg_numpago,pg_num_amortizacion,pg_fechapago,pg_monto,pg_solidario,pg_ahorro,pg_multa FROM d_pago_grupal WHERE pg_numgrupo=? AND pg_numciclo=? AND pg_numpago>?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            ps.setInt(3, numPago);
            myLogger.debug("Ejecutando "+ps);
            res = ps.executeQuery();
            while (res.next()) {
                arrPagoGrupal.add(new PagoGrupalVO(res.getInt("pg_numgrupo"), res.getInt("pg_numciclo"), res.getInt("pg_numtransaccion"), res.getInt("pg_num_amortizacion"), res.getInt("pg_numpago"), res.getDate("pg_fechapago"), res.getDouble("pg_monto"), res.getDouble("pg_solidario"), res.getDouble("pg_ahorro"), res.getDouble("pg_multa")));
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getArrPagoGrupal: ", sqle);
        } catch (Exception e) {
            myLogger.error("Excepcion en getArrPagoGrupal: ", e);
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
        return arrPagoGrupal;
    }
    
    public void deletePagoGrupal(PagoGrupalVO pago) throws ClientesException, SQLException {
        
        Connection con = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM d_pago_grupal WHERE pg_numgrupo=? AND pg_numciclo=? AND pg_numpago=?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, pago.getNumGrupo());
            ps.setDouble(2, pago.getNumCiclo());
            ps.setDouble(3, pago.getNumPago());
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException en deletePagoGrupal: ", sqle);
        } catch (Exception e) {
            myLogger.error("Excepcion en deletePagoGrupal: ", e);
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
    
    public void actualizaPagoGrupal(PagoGrupalVO pago, int numPago) throws ClientesException, SQLException {
        
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_pago_grupal SET pg_numpago=?,pg_solidario=?,pg_ahorro=?,pg_multa=? WHERE pg_numgrupo=? AND pg_numciclo=? AND pg_numpago=?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numPago);
            ps.setDouble(2, pago.getSolidario());
            ps.setDouble(3, pago.getAhorro());
            ps.setDouble(4, pago.getMulta());
            ps.setDouble(5, pago.getNumGrupo());
            ps.setDouble(6, pago.getNumCiclo());
            ps.setDouble(7, pago.getNumPago());
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
            con.close();
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("SQLException en actualizaPagoGrupal: ", sqle);
        } catch (Exception e) {
            myLogger.error("Excepcion en actualizaPagoGrupal: ", e);
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
    
    public double getPagosGarantia(int numGrupo, int numCiclo) throws ClientesException {
        
        double monto = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "SELECT SUM(pg_monto) AS monto FROM d_pago_grupal WHERE pg_numgrupo=? AND pg_numciclo=?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            if (res.next()) {
                monto = res.getInt("monto");
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getPagosGarantia: ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("SQLException en getPagosGarantia: ", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if(res != null){
                    res.close();
                }
                if(ps != null){
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return monto;
    }

}

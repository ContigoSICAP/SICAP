package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;

/**
 * Manejo de persistencia para la tabla d_pago_individual_grupos
 *
 * @author JahTechnologies
 *
 */
public class PagoIndividualGruposDAO extends DAOMaster {

    /**
     * Consulta de un pago individual para un grupo, ciclo, cliente y numero de
     * pago especifico
     *
     * @param numGrupo Numero de grupo
     * @param numCiclo Numero de ciclo
     * @param numCliente Numero de cliente
     * @param numPago Numero de pago
     * @return Clase entidad de pago individual grupos
     * @throws ClientesException
     */
    public PagoIndividualGruposVO getPagoIndividualGrupos(int numGrupo, int numCiclo, int numCliente, int numPago) throws ClientesException {
        PagoIndividualGruposVO pagoIndividual = null;
        Connection cn = null;
        String query = "SELECT *"
                + " FROM d_pago_individual_grupos WHERE pn_numgrupo=? AND pn_numciclo=? AND pn_numcliente=? AND pn_numpago=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            //Logger.debug("Ejecutando = "+query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numPago);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagoIndividual = new PagoIndividualGruposVO();
                pagoIndividual.numGrupo = rs.getInt("pn_numgrupo");
                pagoIndividual.numCiclo = rs.getInt("pn_numciclo");;
                pagoIndividual.numCliente = rs.getInt("pn_numcliente");;
                pagoIndividual.numPago = rs.getInt("pn_numpago");
                pagoIndividual.monto = rs.getDouble("pn_monto");;
                pagoIndividual.usuario = rs.getString("pn_usuario");
                pagoIndividual.corroborar = rs.getInt("pn_corroborar");
//				Logger.debug("Pago esperado Individual encontrado : "+pagoIndividual.toString());
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getPagoIndividualGrupos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getPagoIndividualGrupos: " + e.getMessage());
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
        return pagoIndividual;
    }

    /**
     * Consulta todos los pago individuales para un grupo, ciclo y numero de
     * pago especifico
     *
     * @param numGrupo Numero de grupo
     * @param numCiclo Numero de ciclo
     * @param numPago Numero de pago
     * @return ArrayLust de pagos undividuales grupales
     * @throws ClientesException
     */
    public ArrayList<PagoIndividualGruposVO> getPagosIndividuales(int numGrupo, int numCiclo, int numPago) throws ClientesException {
        PagoIndividualGruposVO pagoIndividual = null;
        ArrayList<PagoIndividualGruposVO> pagos = new ArrayList<PagoIndividualGruposVO>();
        Connection cn = null;
        String query = "SELECT *"
                + " FROM d_pago_individual_grupos WHERE pn_numgrupo=? AND pn_numciclo=? AND pn_numpago=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            //Logger.debug("Ejecutando = "+query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numPago);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagoIndividual = new PagoIndividualGruposVO();
                pagoIndividual.numGrupo = rs.getInt("pn_numgrupo");
                pagoIndividual.numCiclo = rs.getInt("pn_numciclo");;
                pagoIndividual.numCliente = rs.getInt("pn_numcliente");;
                pagoIndividual.numPago = rs.getInt("pn_numpago");
                pagoIndividual.monto = rs.getDouble("pn_monto");;
                pagoIndividual.usuario = rs.getString("pn_usuario");
                pagoIndividual.corroborar = rs.getInt("pn_corroborar");
                pagos.add(pagoIndividual);
//				Logger.debug("Pago esperado Individual encontrado : "+pagoIndividual.toString());
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getPagoIndividualGrupos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getPagoIndividualGrupos: " + e.getMessage());
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
        return pagos;
    }

    public int addPagoIndividualGrupos(PagoIndividualGruposVO pagoIndividual) throws ClientesException {
        return addPagoIndividualGrupos(null, pagoIndividual);
    }

    /**
     * Inserta un nuevo pago undividual grupal
     *
     * @param pagoIndividual Clase entidad de pago individual grupos
     * @return Numero 1 si la insercion es correcta
     * @throws ClientesException
     */
    public int addPagoIndividualGrupos(Connection conn, PagoIndividualGruposVO pagoIndividual) throws ClientesException {

        String query = "INSERT INTO d_pago_individual_grupos (pn_numgrupo, pn_numciclo, pn_numcliente, pn_numpago, pn_monto, pn_usuario, pn_corroborar)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

			//PreparedStatement ps =null;
            //cn = getConnection();
            //ps = cn.prepareStatement(query); 
            ps.setInt(param++, pagoIndividual.numGrupo);
            ps.setInt(param++, pagoIndividual.numCiclo);
            ps.setInt(param++, pagoIndividual.numCliente);
            ps.setInt(param++, pagoIndividual.numPago);
            ps.setDouble(param++, pagoIndividual.monto);
            ps.setString(param++, pagoIndividual.usuario);
            ps.setInt(param++, pagoIndividual.corroborar);
            Logger.debug("Ejecutando = "+ps);
//			Logger.debug("Pago Ind Grup VO: "+pagoIndividual.toString());
            res = ps.executeUpdate();
//			Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            Logger.debug("SQLException en addPagoIndividualGrupos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en addPagoIndividualGrupos: " + e.getMessage());
            e.printStackTrace();
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

    public int updatePagoIndividualGrupos(int numGrupo, int numCiclo, int numPago, int numCliente, PagoIndividualGruposVO pagoIndividual) throws ClientesException {
        return updatePagoIndividualGrupos(null, numGrupo, numCiclo, numPago, numCliente, pagoIndividual);
    }

    /**
     * Actualiza pago undividual grupal
     *
     * @param pagoIndividual Clase entidad de pago individual grupos
     * @return Numero 1 si la insercion es correcta
     * @throws ClientesException
     */
    public int updatePagoIndividualGrupos(Connection conn, int numGrupo, int numCiclo, int numPago, int numCliente, PagoIndividualGruposVO pagoIndividual) throws ClientesException {

        String query = "UPDATE D_PAGO_INDIVIDUAL_GRUPOS SET  PN_MONTO = ?, PN_USUARIO = ?, PN_CORROBORAR = ? "
                + " WHERE PN_NUMGRUPO=? AND PN_NUMCICLO=? AND PN_NUMPAGO = ? AND PN_NUMCLIENTE=?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
			//PreparedStatement ps =null;
            //cn = getConnection();
            //ps = cn.prepareStatement(query); 
            ps.setDouble(param++, pagoIndividual.monto);
            ps.setString(param++, pagoIndividual.usuario);
            ps.setInt(param++, pagoIndividual.corroborar);
            ps.setInt(param++, pagoIndividual.numGrupo);
            ps.setInt(param++, pagoIndividual.numCiclo);
            ps.setInt(param++, pagoIndividual.numPago);
            ps.setInt(param++, pagoIndividual.numCliente);
            //Logger.debug("Ejecutando = "+query);
            res = ps.executeUpdate();
            //Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            Logger.debug("SQLException en updatePagoIndividualGrupos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en updatePagoIndividualGrupos: " + e.getMessage());
            e.printStackTrace();
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

    /**
     * Verifica si existe un pago grupal para un determinado grupo y ciclo
     *
     * @param numGrupo
     * @param numCiclo
     * @return true si existe el pago individual
     * @throws ClientesException
     */

    public double sumaPagosIndividuales(int numGrupo, int numCiclo, int numPago) throws ClientesException {

        String query = "SELECT SUM(PN_MONTO) FROM D_PAGO_INDIVIDUAL_GRUPOS WHERE PN_NUMGRUPO=? AND PN_NUMCICLO=? AND PN_NUMPAGO=?";

        Connection cn = null;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numPago);
//			Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);  //retorna el monto obtenido
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en existePagoIndividual: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en existePagoIndividual: " + e.getMessage());
            e.printStackTrace();
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
        return 0;
    }

    /**
     * Verifica si existe un pago grupal para un determinado grupo y ciclo
     *
     * @param numGrupo
     * @param numCiclo
     * @return true si existe el pago individual
     * @throws ClientesException
     */
    public boolean existePagoIndividual(int numGrupo, int numCiclo, int numPago) throws ClientesException {
        return existePagoIndividual(numGrupo, numCiclo, numPago, false);
    }

    public boolean existePagoIndividual(int numGrupo, int numCiclo, int numPago, boolean noCorroborado) throws ClientesException {

        String query = null;
        if (noCorroborado) {
            query = "SELECT COUNT(*) FROM D_PAGO_INDIVIDUAL_GRUPOS WHERE PN_NUMGRUPO=? AND PN_NUMCICLO=? AND PN_NUMPAGO=? ";
        } else {
            query = "SELECT COUNT(*) FROM D_PAGO_INDIVIDUAL_GRUPOS WHERE PN_NUMGRUPO=? AND PN_NUMCICLO=? AND PN_NUMPAGO=? AND PN_CORROBORAR=1";
        }

        Connection cn = null;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numPago);
//			Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en existePagoIndividual: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en existePagoIndividual: " + e.getMessage());
            e.printStackTrace();
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
        return false;
    }

    /**
     * Verifica si el pago individual todavia no se ha registrado 0 - en la
     * tabla pero sin revisar en la matriz (update) 1 - pago procesado
     * (consulta)
     *
     * @param pagoIndividual Clase entidad de pago individual grupos
     * @return true si hay pagos por corroborar
     * @throws ClientesException
     */
    public boolean corroborarPagoIndividual(int numGrupo, int numCiclo, int numPago) throws ClientesException {

        String query = "SELECT COUNT(*) FROM D_PAGO_INDIVIDUAL_GRUPOS "
                + " WHERE pn_numgrupo=? AND pn_numciclo=? AND pn_numpago=? AND pn_CORROBORAR = 0";

        //int res = 0;
        Connection cn = null;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ps.setInt(param++, numPago);

//			Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en corroborarPagoIndividual: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en corroborarPagoIndividual: " + e.getMessage());
            e.printStackTrace();
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
        return false;
    }

    public int pagoMaximoRegistrado(int numGrupo, int numCiclo) throws ClientesException {

        String query = null;

        query = "SELECT MAX(PN_NUMPAGO) AS 'MAX' FROM D_PAGO_INDIVIDUAL_GRUPOS WHERE PN_NUMGRUPO=? AND PN_NUMCICLO=?  AND PN_CORROBORAR=1";

        Connection cn = null;
        int param = 1;
        int regitradoMax = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
//			Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                regitradoMax = rs.getInt("MAX");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en existePagoIndividual: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en existePagoIndividual: " + e.getMessage());
            e.printStackTrace();
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
        return regitradoMax;
    }
    
    public void deletePagoIndividualGrupal(PagoGrupalVO pago) throws ClientesException, SQLException {
        
        Connection con = null;
        PreparedStatement ps = null;
        String query = "DELETE FROM d_pago_individual_grupos WHERE pn_numgrupo=? AND pn_numciclo=? AND pn_numpago=?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDouble(1, pago.getNumGrupo());
            ps.setDouble(2, pago.getNumCiclo());
            ps.setDouble(3, pago.getNumAmortizacion());
            Logger.debug("Ejecutando "+ps);
            ps.executeUpdate();
            con.close();
        } catch (SQLException sqle) {
            con.close();
            Logger.debug("SQLException en deletePagoIndividualGrupal: " + sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception e) {
            Logger.debug("Excepcion en deletePagoIndividualGrupal: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
    }

}

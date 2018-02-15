package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import org.apache.log4j.Logger;

public class EjecutivoCreditoDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(EjecutivoCreditoDAO.class);

    /**
     * Listado de ejecutivos
     *
     * @return
     * @throws ClientesException
     */
    public ArrayList<EjecutivoCreditoVO> getEjecutivos() throws ClientesException {

        String query = "SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, EJ_USUARIO FROM C_EJECUTIVOS";

        ArrayList<EjecutivoCreditoVO> lista = new ArrayList<EjecutivoCreditoVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
                ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
                ejecutivo.nombre = res.getString("EJ_NOMBRE");
                ejecutivo.aPaterno = res.getString("EJ_APATERNO");
                ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
                ejecutivo.estatus = res.getString("EJ_STATUS");
                ejecutivo.usuario = res.getString("EJ_USUARIO");
                lista.add(ejecutivo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }

    /**
     * Listado de ejecutivos con un estatus de (A)lta o (B)aja
     *
     * @return
     * @throws ClientesException
     */
    public ArrayList<EjecutivoCreditoVO> getEjecutivosSucursal(int idSucursal, String estatus) throws ClientesException {

        String query = "SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, "
                + "EJ_USUARIO FROM C_EJECUTIVOS WHERE EJ_NUMSUCURSAL=? AND EJ_STATUS=? "
                + "ORDER BY EJ_STATUS, EJ_NUMEJECUTIVO";
        ArrayList<EjecutivoCreditoVO> lista = new ArrayList<EjecutivoCreditoVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ps.setString(2, estatus);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
                ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
                ejecutivo.nombre = res.getString("EJ_NOMBRE");
                ejecutivo.aPaterno = res.getString("EJ_APATERNO");
                ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
                ejecutivo.estatus = res.getString("EJ_STATUS");
                ejecutivo.usuario = res.getString("EJ_USUARIO");
                lista.add(ejecutivo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivosSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivosSucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }
    
    public ArrayList<EjecutivoCreditoVO> getEjecutivosSucursalComercial(int idSucursal, String estatus) throws ClientesException {

        String query = "SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, "
                + "EJ_USUARIO FROM C_EJECUTIVOS WHERE EJ_NUMSUCURSAL=? AND EJ_STATUS=? AND ej_tipo_ejecutivo!=4";
        ArrayList<EjecutivoCreditoVO> lista = new ArrayList<EjecutivoCreditoVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ps.setString(2, estatus);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
                ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
                ejecutivo.nombre = res.getString("EJ_NOMBRE");
                ejecutivo.aPaterno = res.getString("EJ_APATERNO");
                ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
                ejecutivo.estatus = res.getString("EJ_STATUS");
                ejecutivo.usuario = res.getString("EJ_USUARIO");
                lista.add(ejecutivo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivosSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivosSucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }
    
    public ArrayList<EjecutivoCreditoVO> getAperturadorSucursal(int idSucursal, String estatus) throws ClientesException {

        String query = "SELECT ej_numsucursal,ej_numejecutivo,ej_nombre,ej_apaterno,ej_amaterno,ej_status,ej_usuario "
                +"FROM c_ejecutivos WHERE ej_numsucursal=? AND ej_status=? AND ej_tipo_ejecutivo=4;";
        ArrayList<EjecutivoCreditoVO> lista = new ArrayList<EjecutivoCreditoVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ps.setString(2, estatus);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("ej_numsucursal");
                ejecutivo.idEjecutivo = res.getInt("ej_numejecutivo");
                ejecutivo.nombre = res.getString("ej_nombre");
                ejecutivo.aPaterno = res.getString("ej_apaterno");
                ejecutivo.aMaterno = res.getString("ej_amaterno");
                ejecutivo.estatus = res.getString("ej_status");
                ejecutivo.usuario = res.getString("ej_usuario");
                lista.add(ejecutivo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getAperturadorSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getAperturadorSucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }

    /**
     * Listado de ejecutivos que pertenecen a una determinada sucursal
     *
     * @return
     * @throws ClientesException
     */
    public ArrayList<EjecutivoCreditoVO> getEjecutivosSucursal(int numSucursal) throws ClientesException {

        String query = "SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, "
                + "EJ_USUARIO, EJ_FECHA_MODIFICACION FROM C_EJECUTIVOS WHERE EJ_NUMSUCURSAL=? "
                + "ORDER BY EJ_STATUS, EJ_NUMEJECUTIVO";
        ArrayList<EjecutivoCreditoVO> lista = new ArrayList<EjecutivoCreditoVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numSucursal);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
                ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
                ejecutivo.nombre = res.getString("EJ_NOMBRE");
                ejecutivo.aPaterno = res.getString("EJ_APATERNO");
                ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
                ejecutivo.estatus = res.getString("EJ_STATUS");
                ejecutivo.usuario = res.getString("EJ_USUARIO");
                ejecutivo.fechaHoramodificacion = res.getTimestamp("EJ_FECHA_MODIFICACION");
                lista.add(ejecutivo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivosSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivosSucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }

    /**
     * Obtiene los datos de un ejecutivo
     *
     * @return
     * @throws ClientesException
     */
    public EjecutivoCreditoVO getEjecutivo(int numEjecutivo) throws ClientesException {

        String query = "SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, "
                + "EJ_USUARIO, EJ_FECHA_MODIFICACION, EJ_TIPO_EJECUTIVO, EJ_UPLINE FROM C_EJECUTIVOS WHERE EJ_NUMEJECUTIVO=?";
        Connection cn = null;
        EjecutivoCreditoVO ejecutivo = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numEjecutivo);
            myLogger.debug("Ejecutando : " + query);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                ejecutivo = new EjecutivoCreditoVO();
                ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
                ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
                ejecutivo.nombre = res.getString("EJ_NOMBRE");
                ejecutivo.aPaterno = res.getString("EJ_APATERNO");
                ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
                ejecutivo.estatus = res.getString("EJ_STATUS");
                ejecutivo.fechaHoramodificacion = res.getTimestamp("EJ_FECHA_MODIFICACION");
                ejecutivo.usuario = res.getString("EJ_USUARIO");
                ejecutivo.tipoEjecutivo = res.getInt("EJ_TIPO_EJECUTIVO");
                ejecutivo.upline = res.getInt("EJ_UPLINE");
                myLogger.debug("Ejecutivo encontrado: " + ejecutivo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivo", e);
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
        return ejecutivo;
    }

    /**
     * Agregar un ejecutivo de credito
     *
     * @param EjecutivoCreditoVO
     * @return
     * @throws ClientesException
     */
    public int addEjecutivo(EjecutivoCreditoVO ejecutivo) throws ClientesException {

        String query = "INSERT INTO C_EJECUTIVOS(EJ_NUMSUCURSAL,EJ_NUMEJECUTIVO,EJ_NOMBRE,EJ_APATERNO,EJ_AMATERNO,"
                + "EJ_STATUS,EJ_USUARIO, EJ_FECHA_MODIFICACION, EJ_TIPO_EJECUTIVO, EJ_UPLINE) VALUES ( ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
        Connection cn = null;
        int param = 1, numEjec = 0;
        ResultSet res = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ejecutivo.idSucursal);
            ps.setInt(param++, ejecutivo.idEjecutivo);
            ps.setString(param++, ejecutivo.nombre);
            ps.setString(param++, ejecutivo.aPaterno);
            ps.setString(param++, ejecutivo.aMaterno);
            ps.setString(param++, ejecutivo.estatus);
            ps.setString(param++, ejecutivo.usuario);
            ps.setInt(param++, ejecutivo.tipoEjecutivo);
            ps.setInt(param++, ejecutivo.upline);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
            res = ps.getGeneratedKeys();
            if(res.next())
                numEjec = res.getInt(1);
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addEjecutivo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addEjecutivo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (res != null)
                    res.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numEjec;
    }

    /**
     * Actualiza un ejecutivo de credito
     *
     * @param EjecutivoCreditoVO
     * @return
     * @throws ClientesException
     */
    public int updateEjecutivo(EjecutivoCreditoVO ejecutivo) throws ClientesException {

        String query = "UPDATE C_EJECUTIVOS SET EJ_NUMSUCURSAL = ?, EJ_NOMBRE = ?, EJ_APATERNO = ?, EJ_AMATERNO = ?, "
                + "EJ_STATUS = ? ,EJ_USUARIO=?, EJ_FECHA_MODIFICACION=CURRENT_TIMESTAMP, EJ_TIPO_EJECUTIVO=?, EJ_UPLINE=? WHERE EJ_NUMEJECUTIVO = ?";
        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ejecutivo.idSucursal);
            ps.setString(param++, ejecutivo.nombre);
            ps.setString(param++, ejecutivo.aPaterno);
            ps.setString(param++, ejecutivo.aMaterno);
            ps.setString(param++, ejecutivo.estatus);
            ps.setString(param++, ejecutivo.usuario);
            ps.setInt(param++, ejecutivo.tipoEjecutivo);
            ps.setInt(param++, ejecutivo.upline);
            ps.setInt(param++, ejecutivo.idEjecutivo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateEjecutivo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEjecutivo", e);
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

    public int tipoEjecutivo(int idAsesor) throws ClientesException {

        int tipo = 0;
        Connection con = null;
        try {
            String query = "SELECT ej_tipo_ejecutivo FROM c_ejecutivos WHERE ej_numejecutivo=?;";
            con = getConnection();
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setInt(1, idAsesor);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idAsesor + "]");
            ResultSet res = pstm.executeQuery();
            if (res.next()) {
                tipo = res.getInt("ej_tipo_ejecutivo");
            }
        } catch (SQLException sqle) {
            myLogger.error("updateEjecutivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tipoEjecutivo", e);
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
        return tipo;
    }

    public boolean tieneCartera(int idEjecutivo) throws ClientesException {
        boolean respuesta = false;
        String query = "SELECT * FROM d_saldos WHERE ib_cta_contable = ? AND ib_estatus not in (3,6,7,8);";
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idEjecutivo);
            myLogger.debug("Ejecutando:" + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("tieneCartera", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tieneCartera", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return respuesta;
    }
}

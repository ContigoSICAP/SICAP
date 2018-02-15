package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CobradorVO;
import org.apache.log4j.Logger;

public class CobradorDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(EjecutivoCreditoDAO.class);

    public int addCobrador(Connection conn, CobradorVO cobrador) throws ClientesException {

        String query = "INSERT INTO C_COBRADORES(COB_NUMCOBRADOR,COB_NUMSUCURSAL,COB_NOMBRE,COB_APATERNO,COB_AMATERNO,COB_ESTATUS,COB_USUARIO,COB_FECHA_MODIFICACION)\n"
                + " VALUES (0,?,?,?,?,?,?,NOW())";

        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setInt(param++, cobrador.getIdSucursal());
            ps.setString(param++, cobrador.getNombre());
            ps.setString(param++, cobrador.getaPaterno());
            ps.setString(param++, cobrador.getaMaterno());
            ps.setInt(param++, cobrador.getEstatus());
            ps.setString(param++, cobrador.getUsuario());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("PArametros = "+cobrador.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addCobrador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addCobrador", e);
            throw new ClientesException(e.getMessage());
        }
        return res;
    }

    public int updateCobrador(Connection con, CobradorVO cobrador) throws ClientesException {

        String query = " update C_COBRADORES\n"
                + " set COB_NUMSUCURSAL =? ,\n"
                + "     COB_NOMBRE = ?,\n"
                + "     COB_APATERNO  = ?,\n"
                + "     COB_AMATERNO = ?,\n"
                + "     COB_ESTATUS =?,\n"
                + "     COB_FECHA_MODIFICACION = NOW()\n"
                + " where COB_NUMCOBRADOR = ?;";
        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            ps = con.prepareStatement(query);
            ps.setInt(param++, cobrador.getIdSucursal());
            ps.setString(param++, cobrador.getNombre());
            ps.setString(param++, cobrador.getaPaterno());
            ps.setString(param++, cobrador.getaMaterno());
            ps.setInt(param++, cobrador.getEstatus());
            ps.setInt(param++, cobrador.getIdCobrador());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros= "+cobrador.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateCobrador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCobrador", e);
            throw new ClientesException(e.getMessage());
        }
        return res;
    }

    public int cobradorActivo(Connection conn, CobradorVO cobrador) throws ClientesException {

        String query = "Select count(cob_numcobrador) as CANTIDAD\n"
                + " from c_cobradores\n"
                + " where cob_estatus=1\n"
                + " and cob_numsucursal=?;";
        int param = 1;
        ResultSet res = null;
        int iCobActivos = 0;
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setInt(param++, cobrador.getIdSucursal());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro = ["+ cobrador.getIdSucursal()+"]");
            res = ps.executeQuery();
            while (res.next()) {
                iCobActivos = res.getInt("CANTIDAD");
            }
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("cobradorActivo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("cobradorActivo", e);
            throw new ClientesException(e.getMessage());
        }
        return iCobActivos;
    }

    public boolean existeCobrador(Connection conn, CobradorVO cobrador) throws ClientesException {

        String query = "Select count(cob_numcobrador) as CANTIDAD from c_cobradores\n"
                + " where cob_nombre = ?\n"
                + " and cob_apaterno = ?\n"
                + " and cob_amaterno = ?\n"
                + " and cob_numsucursal = ?;";
        int param = 1;
        ResultSet res = null;
        int iCantidad = 0;
        boolean bExisteCob = false;
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setString(param++, cobrador.getNombre());
            ps.setString(param++, cobrador.getaPaterno());
            ps.setString(param++, cobrador.getaMaterno());
            ps.setInt(param++, cobrador.getIdSucursal());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro = ["+ cobrador.getNombre()+","+cobrador.getaPaterno()+","+cobrador.getaMaterno()+","+cobrador.getIdSucursal()+"]");
            res = ps.executeQuery();
            while (res.next()) {
                iCantidad = res.getInt("CANTIDAD");
                if (iCantidad >= 1) {
                    bExisteCob = true;
                }
            }
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("existeCobrador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeCobrador", e);
            throw new ClientesException(e.getMessage());
        }
        return bExisteCob;
    }

    public CobradorVO getCobradorAnt(Connection conn, CobradorVO cobradornew) throws ClientesException {

        String query = "Select Cob_numcobrador, cob_numsucursal, cob_nombre, cob_apaterno,cob_amaterno, cob_estatus\n "
                + " from  c_cobradores\n"
                + " where cob_numsucursal = ?\n"
                + " and cob_estatus = 1;";
        CobradorVO cobrador = new CobradorVO();
        int param = 1;
        ResultSet res = null;
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setInt(param++, cobradornew.getIdSucursal());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro = ["+ cobradornew.getIdSucursal()+"]");
            res = ps.executeQuery();
            while (res.next()) {
                cobrador.setIdCobrador((res.getString("Cob_numcobrador") != null ? res.getInt("Cob_numcobrador") : 0));
                cobrador.setIdSucursal((res.getString("cob_numsucursal") != null ? res.getInt("cob_numsucursal") : 0));
                cobrador.setNombre((res.getString("cob_nombre") != null ? res.getString("cob_nombre") : ""));
                cobrador.setaPaterno((res.getString("cob_apaterno") != null ? res.getString("cob_apaterno") : ""));
                cobrador.setaMaterno((res.getString("cob_amaterno") != null ? res.getString("cob_amaterno") : ""));
                cobrador.setEstatus((res.getString("cob_estatus") != null ? res.getInt("cob_estatus") : 0));

            }
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("getCobradorAnt", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCobradorAnt", e);
            throw new ClientesException(e.getMessage());
        }
        return cobrador;
    }
    
        public ArrayList<CobradorVO> getCobradoresSucursal(Connection conn,int numSucursal) throws ClientesException {

        String query = "Select Cob_numcobrador, cob_numsucursal, cob_nombre, cob_apaterno,cob_amaterno, cob_estatus, Cob_fecha_modificacion from "
                           + " c_cobradores\n" +
                      " where cob_numsucursal = ?"
                    + " order by cob_estatus, Cob_numcobrador; ";
        ArrayList<CobradorVO> lista = new ArrayList<CobradorVO>();
        Connection cn = null;
        ResultSet res = null;

        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro = ["+ numSucursal+"]");
            res = ps.executeQuery();
            while (res.next()) {
                CobradorVO cobrador = new CobradorVO();
                cobrador.setIdCobrador((res.getString("Cob_numcobrador") != null ? res.getInt("Cob_numcobrador") : 0));
                cobrador.setIdSucursal((res.getString("cob_numsucursal") != null ? res.getInt("cob_numsucursal") : 0));
                cobrador.setNombre((res.getString("cob_nombre") != null ? res.getString("cob_nombre") : ""));
                cobrador.setaPaterno((res.getString("cob_apaterno") != null ? res.getString("cob_apaterno") : ""));
                cobrador.setaMaterno((res.getString("cob_amaterno") != null ? res.getString("cob_amaterno") : ""));
                cobrador.setEstatus((res.getString("cob_estatus") != null ? res.getInt("cob_estatus") : 0));
                cobrador.setFechaHoramodificacion(res.getTimestamp("Cob_fecha_modificacion"));
                lista.add(cobrador);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCobradoresSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCobradoresSucursal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                 myLogger.error("getCobradoresSucursal", sqle);
                throw new ClientesException(sqle.getMessage());
            }
        }
        return lista;
    }
    public CobradorVO getCobrador(Connection conn, int idCobrador) throws ClientesException {

        String query = "Select Cob_numcobrador, cob_numsucursal, cob_nombre, cob_apaterno,cob_amaterno, cob_estatus\n "
                + " from  c_cobradores\n"
                + " where Cob_numcobrador = ?;";
        CobradorVO cobrador = new CobradorVO();
        int param = 1;
        ResultSet res = null;
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(query);
            ps.setInt(param++, idCobrador);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro = ["+ idCobrador+"]");
            res = ps.executeQuery();
            while (res.next()) {
                cobrador.setIdCobrador((res.getString("Cob_numcobrador") != null ? res.getInt("Cob_numcobrador") : 0));
                cobrador.setIdSucursal((res.getString("cob_numsucursal") != null ? res.getInt("cob_numsucursal") : 0));
                cobrador.setNombre((res.getString("cob_nombre") != null ? res.getString("cob_nombre") : ""));
                cobrador.setaPaterno((res.getString("cob_apaterno") != null ? res.getString("cob_apaterno") : ""));
                cobrador.setaMaterno((res.getString("cob_amaterno") != null ? res.getString("cob_amaterno") : ""));
                cobrador.setEstatus((res.getString("cob_estatus") != null ? res.getInt("cob_estatus") : 0));

            }
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("getCobrador", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCobrador", e);
            throw new ClientesException(e.getMessage());
        }
        return cobrador;
    }

}

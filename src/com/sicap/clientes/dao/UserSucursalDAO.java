package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.UsuarioVO;
import org.apache.log4j.Logger;

public class UserSucursalDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(UserSucursalDAO.class);

    public SucursalVO[] getSucursales(UsuarioVO usuario) throws ClientesException {

        ArrayList<SucursalVO> array = new ArrayList<SucursalVO>();
        SucursalVO sucursales[] = null;
        Connection cn = null;
        String query = null;
        if (usuario.idSucursal != 33) {
            query = "SELECT A.SU_NUMSUCURSAL, A.SU_NOMBRE, A.SU_NUMREGION, A.SU_NUMPLAZA, A.SU_FRONTERIZO "
                    + "FROM C_SUCURSALES A, USER_SUCURSALES B WHERE A.SU_NUMSUCURSAL = B.SU_NUMSUCURSAL AND B.SU_USER = ?";
        } else {
            query = "SELECT SU_NUMSUCURSAL, SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO FROM C_SUCURSALES ";
        }
        SucursalVO temporal = null;
        try {
            cn = getConnection();
            ResultSet rs = null;
            if (usuario.idSucursal != 33) {
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, usuario.nombre);
                rs = ps.executeQuery();
            } else {
                Statement ps = cn.createStatement();
                rs = ps.executeQuery(query);
            }

            myLogger.debug("Ejecutando getSucursales = " + query);

            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = rs.getInt("SU_NUMSUCURSAL");
                temporal.nombre = rs.getString("SU_NOMBRE");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursales", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursales", e);
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

        if (array.size() > 0) {
            sucursales = new SucursalVO[array.size()];
            for (int i = 0; i < sucursales.length; i++) {
                sucursales[i] = (SucursalVO) array.get(i);
            }
        }

        return sucursales;
    }

    public int addUserSucursal(String user, int idSucursal, Connection con) throws ClientesException {

        Connection cn = null;
        int param = 1;
        int res = 0;
        String query = "INSERT INTO USER_SUCURSALES VALUES(?, ?)";
        try {
            PreparedStatement ps = null;
            if (con != null) {
                ps = con.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setString(param++, user);
            ps.setInt(param++, idSucursal);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addUserSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addUserSucursal", e);
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

}

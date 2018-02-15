package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.UsuarioVO;
import org.apache.log4j.Logger;

public class UsuarioDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(UsuarioDAO.class);

    public UsuarioVO[] getUsuarios(String nombreUsuario) throws ClientesException {

        ArrayList<UsuarioVO> array = new ArrayList<UsuarioVO>();
        UsuarioVO temporal = null;
        UsuarioVO usuarios[] = null;
        Connection cn = null;
        String query = "SELECT * FROM USERS WHERE USER_NAME LIKE ?";

        try {
            cn = getConnection();
            //cn = getUSRConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, "%" + nombreUsuario + "%");
            myLogger.debug("Ejecutando getUsuarios = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new UsuarioVO();
                temporal.nombre = rs.getString(1);
                temporal.password = rs.getString(2);
                temporal.identificador = rs.getString(3);
                temporal.idSucursal = rs.getInt(4);
                temporal.fechaCreacion = rs.getDate(5);
                temporal.fechaVencimiento = rs.getDate(6);
                temporal.fechaAcceso = rs.getDate(7);
                temporal.status = rs.getString(8);
                temporal.roles = getRolesUsuario(temporal.nombre);
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getUsuarios", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getUsuarios", e);
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
        usuarios = new UsuarioVO[array.size()];
        for (int i = 0; i < usuarios.length; i++) {
            usuarios[i] = (UsuarioVO) array.get(i);
        }
        return usuarios;
    }

    public int addUsuario(UsuarioVO usuario, Connection con) throws ClientesException {

        Connection cn = null;
        int param = 1;
        int res = 0;
        String query = "INSERT INTO USERS(USER_NAME, USER_PASS, USER_IDENTIFICADOR, NUM_SUCURSAL, FECHA_CREACION, FECHA_VENCIMIENTO, FECHA_ULTIMO_ACCESO, STATUS, NOMBRE_COMPLETO) "
                + "VALUES (?, ?, ?, ?, CURDATE(), ADDDATE(CURRENT_DATE, ?), CURDATE(), 'A', ?)";
        try {
            PreparedStatement ps = null;
            if (con != null) {
                ps = con.prepareStatement(query);
            } else {
                cn = getConnection();
                //cn = getUSRConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setString(param++, usuario.nombre);
            ps.setString(param++, SecurityUtil.toMD5(usuario.password));
            ps.setString(param++, usuario.identificador);
            ps.setInt(param++, usuario.idSucursal);
            //ps.setDate(param++, usuario.fechaCreacion);
            //ps.setDate(param++, usuario.fechaVencimiento);
            //ps.setDate(param++, usuario.fechaAcceso);
            //ps.setString(param++, usuario.status);
            ps.setInt(param++, ClientesConstants.DIAS_VIGENCIA_PASSWORD);
            ps.setString(param++, usuario.nombreCompleto);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Usuario= " + usuario.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addUsuario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getUsuarios", e);
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

    /*public int pasaInactivoporIntentosFallidos (String usuario) throws ClientesException{

     String query =	"UPDATE USERS SET STATUS = ? WHERE USER_NAME = ?";
		
     Connection cn = null;
     int  res = 0;
     int  param = 1;
     try{
     PreparedStatement ps =null;
     cn = getConnection();
     ps = cn.prepareStatement(query); 
			
     ps.setString(param++, "I");
     ps.setString(param++, usuario);
		
     myLogger.debug("Ejecutando = "+query);
     myLogger.debug("Usuario="+usuario);
     res = ps.executeUpdate();
     myLogger.debug("Resultado = "+res);
     }
     catch(SQLException sqle) {
     myLogger.debug("SQLException en pasaInactivoporIntentosFallidos : "+sqle.getMessage());
     sqle.printStackTrace();
     throw new ClientesDBException(sqle.getMessage());
     }
     catch(Exception e) {
     myLogger.debug("Excepcion en pasaInactivoporIntentosFallidos : "+e.getMessage());
     e.printStackTrace();
     throw new ClientesException(e.getMessage());
     }
     finally {
     try {
     if ( cn!=null ) cn.close();
     }
     catch(SQLException sqle) {
     throw new ClientesDBException(sqle.getMessage());
     }
     }
     return res;
     } */
    public int updateUsuario(UsuarioVO usuario, String user_name) throws ClientesException {

        String query = "UPDATE USERS SET USER_PASS = ?, FECHA_CREACION = ?, FECHA_VENCIMIENTO = ?, "
                + "FECHA_ULTIMO_ACCESO = ?, STATUS = ?, INTENTOS_FALLIDOS = ? WHERE USER_NAME = ?";

        Connection cn = null;
        int res = 0;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            //cn = getUSRConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, usuario.password);
            ps.setDate(param++, usuario.fechaCreacion);
            ps.setDate(param++, usuario.fechaVencimiento);
            ps.setDate(param++, usuario.fechaAcceso);
            ps.setString(param++, usuario.status);
            ps.setInt(param++, usuario.intentosFallidos);
            ps.setString(param++, user_name);

//			myLogger.debug("Ejecutando = "+query);
//			myLogger.debug("Usuario="+usuario);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateUsuario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateUsuario", e);
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

    public UsuarioVO getDatosUsuario(String user_name) throws ClientesException {

        UsuarioVO usuario = null;
        String query = "SELECT USER_NAME, USER_PASS, USER_IDENTIFICADOR, NUM_SUCURSAL, FECHA_CREACION, FECHA_VENCIMIENTO, FECHA_ULTIMO_ACCESO, STATUS, INTENTOS_FALLIDOS, NOMBRE_COMPLETO, "
                + "DATEDIFF(FECHA_VENCIMIENTO, CURRENT_DATE) AS DIAS_EXPIRACION, DATEDIFF(CURRENT_DATE, FECHA_ULTIMO_ACCESO) AS DIAS_SIN_ACCESAR "
                + "FROM USERS WHERE USER_NAME = ? ";
        Connection cn = null;

        try {

            //cn = getUSRConnection();
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, user_name);
//			myLogger.debug("Ejecutando : "+query);
//			myLogger.debug("Para usuario : "+user_name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usuario = new UsuarioVO();
                usuario.nombre = rs.getString("USER_NAME");
                usuario.password = rs.getString("USER_PASS");
                usuario.identificador = rs.getString("USER_IDENTIFICADOR");
                usuario.idSucursal = rs.getInt("NUM_SUCURSAL");
                usuario.fechaCreacion = rs.getDate("FECHA_CREACION");
                usuario.fechaVencimiento = rs.getDate("FECHA_VENCIMIENTO");
                usuario.fechaAcceso = rs.getDate("FECHA_ULTIMO_ACCESO");
                usuario.status = rs.getString("STATUS");
                usuario.diasExpiracion = rs.getInt("DIAS_EXPIRACION");
                usuario.intentosFallidos = rs.getInt("INTENTOS_FALLIDOS");
                usuario.nombreCompleto = rs.getString("NOMBRE_COMPLETO");
                usuario.diasSinAccesar = rs.getInt("DIAS_SIN_ACCESAR");
//				myLogger.debug("Usuario encontrado : "+usuario.toString());
            }
            if (usuario != null) {
                usuario.roles = getRolesUsuario(usuario.nombre);//super necesario para llenar completamente el objeto UsuarioVO
            }
        } catch (SQLException sqle) {
            myLogger.error("getDatosUsuario", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDatosUsuario", e);
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
        return usuario;

    }

    public boolean updateDatosUsuario(String user, String password, Connection con) throws ClientesException {

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String query = "UPDATE USERS SET ";

            if (password != null || password.trim().length() != 0) {
                query += "USER_PASS = '" + SecurityUtil.toMD5(password) + "' , ";
            }

            query += "FECHA_VENCIMIENTO = ADDDATE(CURRENT_DATE, ?) WHERE USER_NAME = ?";
            myLogger.debug("Se ejecuto: " + query);
            if (con != null) {
                ps = con.prepareStatement(query);
            } else {
                cn = getConnection();
                //cn = getUSRConnection();
                ps = cn.prepareStatement(query);
            }
            //ps.setString(1, SecurityUtil.toMD5( password) );
            ps.setInt(1, ClientesConstants.DIAS_VIGENCIA_PASSWORD);
            ps.setString(2, user);

            if (!ps.execute()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqle) {
            myLogger.error("updateDatosUsuario", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDatosUsuario", e);
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

    public int addRolUsuario(String nombreUsuario, String nombreRol, Connection cn) throws ClientesException {
        String query = "INSERT INTO USER_ROLES VALUES(?,?)";
        Connection con = null;
        int param = 1;
        int noReg = 0;
        PreparedStatement ps = null;
        try {
            if (cn == null) {
                con = getConnection();
                ps = con.prepareStatement(query);
            } else {
                ps = cn.prepareStatement(query);
            }
            ps.setString(param++, nombreUsuario);
            ps.setString(param++, nombreRol);
            myLogger.debug("Ejecutando = " + query);
            noReg = ps.executeUpdate();
            myLogger.debug("Registros insertados = " + noReg);
        } catch (SQLException exc) {
            myLogger.error("addRolUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("addRolUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("addRolUsuario", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return noReg;
    }

    public int deleteRolUsuario(String nombreUsuario, String nombreRol) throws ClientesException {
        String query = "DELETE FROM USER_ROLES WHERE USER_NAME = ? AND ROLE_NAME = ?";
        Connection con = null;
        int param = 1;
        int noReg = 0;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(param++, nombreUsuario);
            ps.setString(param++, nombreRol);
            myLogger.debug("Ejecutando = " + query);
            noReg = ps.executeUpdate();
            myLogger.debug("Registros borrados = " + noReg);
        } catch (SQLException exc) {
            myLogger.error("deleteRolUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("deleteRolUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("deleteRolUsuario", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return noReg;
    }

    public String[] getRoles() throws ClientesException {
        String[] roles = null;
        ArrayList<String> arrayList = null;
        String query = "SELECT * FROM ROLES";
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            arrayList = new ArrayList<String>();
            while (rs.next()) {
                arrayList.add(rs.getString("ROLE_NAME"));
            }
            roles = new String[arrayList.size()];
            for (int i = 0; i < roles.length; i++) {
                roles[i] = arrayList.get(i);
            }
        } catch (SQLException exc) {
            myLogger.error("getRoles", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("getRoles", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getRoles", exc);
                throw new ClientesException(exc.getMessage());
            }
        }

        return roles;
    }

    public int deleteRolesUsuario(String nombreUsuario, Connection cn) throws ClientesException {
        String query = "DELETE FROM USER_ROLES WHERE USER_NAME = ?";
        Connection con = null;
        int param = 1;
        int noReg = 0;
        PreparedStatement ps = null;
        try {
            if (cn == null) {
                con = getConnection();
                ps = con.prepareStatement(query);
            } else {
                ps = cn.prepareStatement(query);
            }
            ps.setString(param++, nombreUsuario);
            myLogger.debug("Ejecutando = " + query);
            noReg = ps.executeUpdate();
            myLogger.debug("Registros borrados = " + noReg);
        } catch (SQLException exc) {
            myLogger.error("deleteRolesUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("deleteRolesUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("deleteRolesUsuario", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return noReg;
    }

    public String[] getRolesUsuario(String username) throws ClientesException {
        String[] rolesUsuario = null;
        Connection con = null;
        ArrayList<String> arrayRoles = new ArrayList<String>();
        String query = "SELECT * FROM USER_ROLES WHERE USER_NAME = ?";
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(param++, username);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arrayRoles.add(rs.getString("ROLE_NAME"));
            }
            rolesUsuario = new String[arrayRoles.size()];
            for (int i = 0; i < rolesUsuario.length; i++) {
                rolesUsuario[i] = arrayRoles.get(i);
            }
        } catch (SQLException exc) {
            myLogger.error("getRolesUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            myLogger.error("getRolesUsuario", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getRolesUsuario", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return rolesUsuario;
    }

    public Hashtable getProductos(String user_name) throws ClientesException {

        Hashtable<Integer, Integer> productos = new Hashtable<Integer, Integer>();
        String query = "SELECT * FROM USER_PRODUCTOS WHERE PR_USER = ?";
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, user_name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productos.put(rs.getInt("PR_NUMPRODUCTO"), rs.getInt("PR_NUMPRODUCTO"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getProductos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getProductos", e);
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
        return productos;

    }

    public int updateUsuarioSucursal(UsuarioVO usuario, String user_name) throws ClientesException {

        String query = "UPDATE USERS SET USER_NAME = ?, NUM_SUCURSAL = ?, NOMBRE_COMPLETO = ? WHERE USER_NAME = ?";

        Connection cn = null;
        int res = 0;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            //cn = getUSRConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, usuario.nombre);
            ps.setInt(param++, usuario.idSucursal);
            ps.setString(param++, usuario.nombreCompleto);
            ps.setString(param++, user_name);

            res = ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updateUsuarioSucursal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateUsuarioSucursal", e);
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
    public String getUsuarioAnalisis() throws ClientesException{
        String query="SELECT user_name FROM users where puesto = 10;";
        String usuario ="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                usuario=rs.getString("user_name");
            }
        }catch (SQLException sqle) {
            myLogger.error("getUsuarioAsignado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUsuarioAsignado", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if(cn != null){
                    cn.close();
                }
                if (ps != null){
                    ps.close();
                }
                if (rs !=null){
                    rs.close();
                }
            } catch (SQLException sqle){
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return usuario;
    }

}

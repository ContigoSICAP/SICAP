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
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.util.logging.Level;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class SucursalDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(SucursalDAO.class);

    public String getSucursalNombre(int idSucursal) {
        try {
            String query = "SELECT SU_NOMBRE FROM C_SUCURSALES WHERE SU_NUMSUCURSAL = ?";
            Connection cn = null;

            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String ret = rs.getString("SU_NOMBRE");
            cn.close();
            return ret;
        } catch (NamingException ex) {
            myLogger.error("NamingException en getSucursalNombre",ex);
            return "";
        } catch (SQLException ex) {
            myLogger.error("SQLException en getSucursalNombre",ex);
            return "";
        }
    }

    public SucursalVO getSucursal(int idSucursal) throws ClientesException {

        //String query = "SELECT SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, SU_TIPO_SUCURSAL, SU_DIRECCION, SU_REPRESENTANTE FROM C_SUCURSALES WHERE SU_NUMSUCURSAL = ?";
        String query = "SELECT SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, "
                + "SU_TIPO_SUCURSAL, SU_DIRECCION_CALLE, SU_REPRESENTANTE, SU_CODIGO, "
                + "SU_NUMERO, SU_CP, SU_MUNICIPIO, MU_NOMBRE, SU_ESTADO, ES_NOMBRE,"
                + "SU_COLONIA, CO_NOMBRE_COLONIA, SU_TELEFONO, su_calificacion, su_seg_funerario, su_codigosap FROM C_SUCURSALES "
                + "INNER JOIN C_ESTADOS ON C_SUCURSALES.SU_ESTADO = C_ESTADOS.ES_NUMESTADO "
                + "INNER JOIN C_COLONIAS ON C_SUCURSALES.SU_COLONIA = C_COLONIAS.CO_NUMCOLONIA "
                + "INNER JOIN C_MUNICIPIOS ON (C_SUCURSALES.SU_MUNICIPIO = C_MUNICIPIOS.MU_NUMMUNICIPIO) "
                + "AND (C_SUCURSALES.SU_ESTADO = C_MUNICIPIOS.MU_NUMESTADO) WHERE SU_NUMSUCURSAL = ?";
        SucursalVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = " + idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = idSucursal;
                temporal.nombre = rs.getString("SU_NOMBRE");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                temporal.idBanco = rs.getInt("SU_BANCO");
                temporal.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                temporal.direccion_calle = rs.getString("SU_DIRECCION_CALLE");
                temporal.representante = rs.getString("SU_REPRESENTANTE");
                temporal.codigo = rs.getInt("SU_CODIGO");
                temporal.numero = rs.getString("SU_NUMERO");
                temporal.cp = rs.getInt("SU_CP");
                temporal.idMunicipio = rs.getInt("SU_MUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idEstado = rs.getInt("SU_ESTADO");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.idColonia = rs.getInt("SU_COLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.telefono = rs.getString("SU_TELEFONO");
                temporal.calificacion = rs.getInt("su_calificacion");
                temporal.SeguroFunerario = rs.getDouble("su_seg_funerario");
                temporal.codigoSAP = rs.getString("su_codigosap");

            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursal",e);
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
        return temporal;

    }

        public SucursalVO getSucursalDirecto(int idSucursal) throws ClientesException {

        //String query = "SELECT SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, SU_TIPO_SUCURSAL, SU_DIRECCION, SU_REPRESENTANTE FROM C_SUCURSALES WHERE SU_NUMSUCURSAL = ?";
        String query = "SELECT SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, "
                + "SU_TIPO_SUCURSAL, SU_DIRECCION_CALLE, SU_REPRESENTANTE, SU_CODIGO, "
                + "SU_NUMERO, SU_CP, SU_MUNICIPIO, SU_ESTADO, "
                + "SU_COLONIA, SU_TELEFONO, su_codigosap, su_calificacion FROM C_SUCURSALES "
                + " WHERE SU_NUMSUCURSAL = ?";
        SucursalVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = " + idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = idSucursal;
                temporal.nombre = rs.getString("SU_NOMBRE");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                temporal.idBanco = rs.getInt("SU_BANCO");
                temporal.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                temporal.direccion_calle = rs.getString("SU_DIRECCION_CALLE");
                temporal.representante = rs.getString("SU_REPRESENTANTE");
                temporal.codigo = rs.getInt("SU_CODIGO");
                temporal.numero = rs.getString("SU_NUMERO");
                temporal.cp = rs.getInt("SU_CP");
                temporal.idMunicipio = rs.getInt("SU_MUNICIPIO");
                temporal.idEstado = rs.getInt("SU_ESTADO");
                temporal.idColonia = rs.getInt("SU_COLONIA");
                temporal.telefono = rs.getString("SU_TELEFONO");
                temporal.codigoSAP = rs.getString("su_codigosap");
                temporal.calificacion = rs.getInt("su_calificacion");

            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursalDirecto",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursalDirecto",e);
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
        return temporal;

    }

    public SucursalVO[] getSucursal(String nombreSucursal) throws ClientesException {

        String query = "SELECT SU_NUMSUCURSAL, SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, "
                + "SU_TIPO_SUCURSAL, SU_DIRECCION_CALLE, SU_REPRESENTANTE, "
                + "SU_NUMERO, SU_CP, SU_MUNICIPIO, MU_NOMBRE, SU_ESTADO, ES_NOMBRE,"
                + "SU_COLONIA, CO_NOMBRE_COLONIA, SU_TELEFONO, SU_ESTATUS, SU_CODIGO, su_calificacion FROM C_SUCURSALES "
                + "INNER JOIN C_ESTADOS ON C_SUCURSALES.SU_ESTADO = C_ESTADOS.ES_NUMESTADO "
                + "INNER JOIN C_COLONIAS ON C_SUCURSALES.SU_COLONIA = C_COLONIAS.CO_NUMCOLONIA "
                + "INNER JOIN C_MUNICIPIOS ON (C_SUCURSALES.SU_MUNICIPIO = C_MUNICIPIOS.MU_NUMMUNICIPIO) "
                + "AND (C_SUCURSALES.SU_ESTADO = C_MUNICIPIOS.MU_NUMESTADO) WHERE SU_NOMBRE LIKE ?";

        ArrayList<SucursalVO> array = new ArrayList<SucursalVO>();
        SucursalVO temporal = null;
        SucursalVO sucursales[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, "%" + nombreSucursal + "%");
            //	Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = rs.getInt("SU_NUMSUCURSAL");
                temporal.nombre = rs.getString("SU_NOMBRE");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                temporal.idBanco = rs.getInt("SU_BANCO");
                temporal.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                temporal.direccion_calle = rs.getString("SU_DIRECCION_CALLE");
                temporal.representante = rs.getString("SU_REPRESENTANTE");
                temporal.numero = rs.getString("SU_NUMERO");
                temporal.cp = rs.getInt("SU_CP");
                temporal.idMunicipio = rs.getInt("SU_MUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idEstado = rs.getInt("SU_ESTADO");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.idColonia = rs.getInt("SU_COLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.telefono = rs.getString("SU_TELEFONO");
                temporal.estatus = rs.getInt("SU_ESTATUS");
                temporal.codigo = rs.getInt("SU_CODIGO");
                temporal.calificacion = rs.getInt("su_calificacion");


                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursal",e);
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
        sucursales = new SucursalVO[array.size()];
        for (int i = 0; i < sucursales.length; i++) {
            sucursales[i] = (SucursalVO) array.get(i);
        }
        return sucursales;
    }

    public SucursalVO[] getSucursal() throws ClientesException {

        String query = "SELECT * FROM C_SUCURSALES";
        ArrayList<SucursalVO> array = new ArrayList<SucursalVO>();
        SucursalVO temporal = null;
        SucursalVO sucursales[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = rs.getInt(1);
                temporal.nombre = rs.getString(2);
                temporal.idRegion = rs.getInt(3);
                temporal.idPlaza = rs.getInt(4);
                temporal.fronterizo = rs.getBoolean(5);
                temporal.identificador = rs.getString(6);
                temporal.idBanco = rs.getInt(7);
                temporal.tipoSucursal = rs.getInt(8);
                temporal.direccion_calle = rs.getString(9);
                temporal.representante = rs.getString(10);
                temporal.calificacion = rs.getInt(26);

                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursal",e);
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
        sucursales = new SucursalVO[array.size()];
        for (int i = 0; i < sucursales.length; i++) {
            sucursales[i] = (SucursalVO) array.get(i);
        }
        return sucursales;
    }

    public Hashtable<Integer, SucursalVO> getSucursales() throws ClientesException {
        String query = "SELECT SU_NUMSUCURSAL, SU_NOMBRE, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, SU_TIPO_SUCURSAL FROM C_SUCURSALES";
        SucursalVO temporal = null;
        Connection cn = null;
        Hashtable<Integer, SucursalVO> sucursales = new Hashtable<Integer, SucursalVO>();
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.idSucursal = rs.getInt("SU_NUMSUCURSAL");
                temporal.nombre = rs.getString("SU_NOMBRE");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                temporal.idBanco = rs.getInt("SU_BANCO");
                temporal.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                sucursales.put(new Integer(temporal.idSucursal), temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursales",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursales",e);
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
        return sucursales;
    }

    public int addSucursal(SucursalVO sucursal, Connection con) throws ClientesException {

        Connection cn = null;
        int param = 1;
        int res = 0;

        String query = "INSERT INTO C_SUCURSALES(SU_NUMSUCURSAL, SU_NOMBRE, "
                + "SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, su_identificador, "
                + "SU_BANCO, SU_DIRECCION_CALLE, SU_REPRESENTANTE, SU_NUMERO, SU_CP, "
                + "SU_ESTADO, SU_MUNICIPIO, SU_COLONIA, SU_TELEFONO, SU_FECHA_ALTA, SU_CODIGO)"
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?)";
        try {
            PreparedStatement ps = null;
            if (con != null) {
                ps = con.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setInt(param++, sucursal.idSucursal);
            ps.setString(param++, sucursal.nombre);
            ps.setInt(param++, sucursal.idRegion);
            ps.setInt(param++, sucursal.idPlaza);
            ps.setBoolean(param++, sucursal.fronterizo);
            ps.setString(param++, sucursal.identificador);
            ps.setInt(param++, sucursal.idBanco);
            ps.setString(param++, sucursal.direccion_calle);
            ps.setString(param++, sucursal.representante);
            ps.setString(param++, sucursal.numero);
            ps.setInt(param++, sucursal.cp);
            ps.setInt(param++, sucursal.idEstado);
            ps.setInt(param++, sucursal.idMunicipio);
            ps.setInt(param++, sucursal.idColonia);
            ps.setString(param++, sucursal.telefono);
            ps.setInt(param++, sucursal.codigo);
            //ps.setString(param++, SecurityUtil.toMD5(usuario.password));		
            //ps.setDate(param++, usuario.fechaCreacion);
            //ps.setDate(param++, usuario.fechaVencimiento);
            //ps.setDate(param++, usuario.fechaAcceso);
            //
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Usuario= " + sucursal.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addSucursal",e);
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

    public SucursalVO getSucursalCom(String nombre) throws ClientesException {

        String query = "SELECT SU_NUMSUCURSAL, SU_NUMREGION, SU_NUMPLAZA, SU_FRONTERIZO, SU_BANCO, "
                + "SU_TIPO_SUCURSAL, SU_DIRECCION_CALLE, SU_REPRESENTANTE, "
                + "SU_NUMERO, SU_CP, SU_MUNICIPIO, MU_NOMBRE, SU_ESTADO, ES_NOMBRE,"
                + "SU_COLONIA, CO_NOMBRE_COLONIA, SU_TELEFONO FROM C_SUCURSALES "
                + "INNER JOIN C_ESTADOS ON C_SUCURSALES.SU_ESTADO = C_ESTADOS.ES_NUMESTADO "
                + "INNER JOIN C_COLONIAS ON C_SUCURSALES.SU_COLONIA = C_COLONIAS.CO_NUMCOLONIA "
                + "INNER JOIN C_MUNICIPIOS ON (C_SUCURSALES.SU_MUNICIPIO = C_MUNICIPIOS.MU_NUMMUNICIPIO) "
                + "AND (C_SUCURSALES.SU_ESTADO = C_MUNICIPIOS.MU_NUMESTADO) WHERE SU_NOMBRE = ?";
        SucursalVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, nombre);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SucursalVO();
                temporal.nombre = nombre;
                temporal.nombre = rs.getString("SU_NUMSUCURSAL");
                temporal.idRegion = rs.getInt("SU_NUMREGION");
                temporal.idPlaza = rs.getInt("SU_NUMPLAZA");
                temporal.fronterizo = rs.getBoolean("SU_FRONTERIZO");
                temporal.idBanco = rs.getInt("SU_BANCO");
                temporal.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                temporal.direccion_calle = rs.getString("SU_DIRECCION_CALLE");
                temporal.representante = rs.getString("SU_REPRESENTANTE");
                temporal.numero = rs.getString("SU_NUMERO");
                temporal.cp = rs.getInt("SU_NUMERO");
                temporal.idMunicipio = rs.getInt("SU_MUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idEstado = rs.getInt("SU_ESTADO");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.idColonia = rs.getInt("SU_COLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.telefono = rs.getString("SU_TELEFONO");

            }
        } catch (SQLException sqle) {
            myLogger.error("getSucursalCom",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSucursalCom",e);
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
        return temporal;

    }

    /*
    public HashMap<Integer,String> getSucursales() throws ClientesException{
    
    String query = "SELECT SU_NUMSUCURSAL, SU_NOMBRE FROM C_SUCURSALES ";
    HashMap<Integer,String> temporal =new HashMap<Integer,String>();
    Connection cn = null;
    try{
    cn = getConnection();
    PreparedStatement ps = cn.prepareStatement(query);
    Logger.debug("Ejecutando = "+query);
    ResultSet rs = ps.executeQuery();
    while(rs.next()){
    temporal.put(new Integer(rs.getInt("su_numsucursal")),rs.getString("su_nombre"));
    }
    }catch(SQLException sqle) {
    Logger.debug("SQLException en getSucursal : "+sqle.getMessage());
    sqle.printStackTrace();
    throw new ClientesDBException(sqle.getMessage());
    }
    catch(Exception e) {
    Logger.debug("Excepcion en getSucursal : "+e.getMessage());
    e.printStackTrace();
    throw new ClientesException(e.getMessage());
    }
    finally {
    try {
    if ( cn!=null ) cn.close();
    }catch(SQLException e) {
    throw new ClientesDBException(e.getMessage());
    }
    }
    return temporal;
    
    }*/
    /*	public int addTelefono(Connection conn, int idCliente, int idDireccion, TelefonoVO telefono) throws ClientesException{
    
    String query =	"INSERT INTO D_TELEFONOS (TE_NUMCLIENTE, TE_NUMDIRECCION, TE_NUMTELEFONO, TE_TIPOTELEFONO, TE_TELEFONO) "+
    "VALUES (?, ?, ?, ?, ?)";
    
    Connection cn = null;
    PreparedStatement ps = null;
    int param = 1;
    int res = 0;
    int next = getNext(idCliente, idDireccion);
    telefono.idTelefono = next;
    try{
    if( conn==null ){
    cn = getConnection();
    ps = cn.prepareStatement(query); 
    }
    else{
    ps = conn.prepareStatement(query);
    }
    ps.setInt(param++, idCliente);
    ps.setInt(param++, idDireccion);
    ps.setInt(param++, telefono.idTelefono);
    ps.setInt(param++, telefono.tipoTelefono);
    ps.setString(param++, telefono.numeroTelefono);
    Logger.debug("Ejecutando = "+query);
    Logger.debug("Para : "+telefono.toString());
    res = ps.executeUpdate();
    Logger.debug("Resultado : "+res);
    }
    catch(SQLException sqle) {
    Logger.debug("SQLException en addTelefono : "+sqle.getMessage());
    sqle.printStackTrace();
    throw new ClientesDBException(sqle.getMessage());
    }
    catch(Exception e) {
    Logger.debug("Excepcion en addTelefono : "+e.getMessage());
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
    return next;
    }
    
    
    
    public int updateTelefono(Connection conn, int idCliente, int idDireccion, TelefonoVO telefono) throws ClientesException{
    
    int res = 0;
    String query =	"UPDATE D_TELEFONOS SET TE_TELEFONO = ? WHERE TE_NUMCLIENTE = ? AND TE_NUMDIRECCION = ? AND TE_TIPOTELEFONO = ?";
    Connection cn = null;
    PreparedStatement ps = null;
    int param = 1;
    try{
    if( conn==null ){
    cn = getConnection();
    ps = cn.prepareStatement(query); 
    }
    else{
    ps = conn.prepareStatement(query);
    }
    ps.setString(param++, telefono.numeroTelefono);
    ps.setInt(param++, idCliente);
    ps.setInt(param++, idDireccion);
    ps.setInt(param++, telefono.tipoTelefono);
    Logger.debug("Ejecutando = "+query);
    Logger.debug("Telefono = "+telefono.toString());
    res = ps.executeUpdate();
    Logger.debug("Resultado : "+res);
    }
    catch(SQLException sqle) {
    Logger.debug("SQLException en updateTelefono : "+sqle.getMessage());
    sqle.printStackTrace();
    throw new ClientesDBException(sqle.getMessage());
    }
    catch(Exception e) {
    Logger.debug("Excepcion en updateTelefono : "+e.getMessage());
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
    }
    
    
    
    
    public int getNext(int idCliente, int idDireccion) throws ClientesException{
    
    String query = "SELECT COALESCE(MAX(TE_NUMTELEFONO),0)+1 AS NEXT FROM D_TELEFONOS WHERE "+
    "TE_NUMCLIENTE = ? AND TE_NUMDIRECCION = ?";
    Connection cn = null;
    int next = 1;
    try{
    cn = getConnection();
    PreparedStatement ps = cn.prepareStatement(query);
    ps.setInt(1, idCliente);
    ps.setInt(2, idDireccion);
    ResultSet rs = ps.executeQuery();
    Logger.debug("Ejecutando = "+query);
    if( rs.next() ){
    next = rs.getInt("NEXT");
    }
    Logger.debug("Resultado : "+next);
    }
    catch(SQLException sqle) {
    Logger.debug("SQLException en getNext : "+sqle.getMessage());
    sqle.printStackTrace();
    throw new ClientesDBException(sqle.getMessage());
    }
    catch(Exception e) {
    Logger.debug("Excepcion en getNext : "+e.getMessage());
    e.printStackTrace();
    throw new ClientesException(e.getMessage());
    }
    finally {
    try {
    if ( cn!=null )
    cn.close();
    }catch(SQLException e) {
    throw new ClientesDBException(e.getMessage());
    }
    }
    return next;
    }*/
    public int updateSucursal(Connection conn, SucursalVO sucursal) throws ClientesException {

        int res = 0;
        String query = "UPDATE C_SUCURSALES SET SU_NOMBRE = ?, SU_DIRECCION_CALLE = ?, su_identificador = ?, "
                + "SU_REPRESENTANTE = ?, SU_BANCO = ?, SU_NUMERO = ?, SU_CP = ?, "
                + "SU_ESTADO = ?, SU_MUNICIPIO = ?, SU_COLONIA = ?, SU_TELEFONO = ?, SU_NUMREGION = ?, SU_CODIGO = ? WHERE SU_NUMSUCURSAL = ?";
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
            ps.setString(param++, sucursal.nombre);
            ps.setString(param++, sucursal.direccion_calle);
            ps.setString(param++, sucursal.identificador);
            ps.setString(param++, sucursal.representante);
            ps.setInt(param++, sucursal.idBanco);
            ps.setString(param++, sucursal.numero);
            ps.setInt(param++, sucursal.cp);
            ps.setInt(param++, sucursal.idEstado);
            ps.setInt(param++, sucursal.idMunicipio);
            ps.setInt(param++, sucursal.idColonia);
            ps.setString(param++, sucursal.telefono);
            ps.setInt(param++, sucursal.idRegion);
            ps.setInt(param++, sucursal.codigo);
            ps.setInt(param++, sucursal.idSucursal);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Cliente = " + sucursal.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSucursal",e);
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
    
    public boolean  getCambioEstatus(Connection conn, SucursalVO sucursal) throws ClientesException {

        boolean cambio = false;
        String query = "SELECT su_estatus FROM c_sucursales where su_numsucursal=?";
        PreparedStatement ps = null;
        Connection cn = null;
        ResultSet res = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, sucursal.idSucursal);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros[" + sucursal.idSucursal+"]");
            res = ps.executeQuery();
            while (res.next()) {                
                if(res.getInt("su_estatus")!=sucursal.estatus)
                    updateEstatusSucursal(conn, sucursal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCambioEstatus",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCambioEstatus",e);
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
        return cambio;

    }
    
    public void updateEstatusSucursal(Connection conn, SucursalVO sucursal) throws ClientesException {

        String query = "UPDATE c_sucursales SET su_estatus = ?, ";
        
        if(sucursal.estatus==0)
            query += "su_fecha_cierre=CURDATE() ";
        if(sucursal.estatus==1)
            query += "su_fecha_alta=CURDATE() ";
        query += "WHERE SU_NUMSUCURSAL = ?";
        
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, sucursal.estatus);
            ps.setInt(2, sucursal.idSucursal);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros["+sucursal.idSucursal+"]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateEstatusSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusSucursal",e);
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
}

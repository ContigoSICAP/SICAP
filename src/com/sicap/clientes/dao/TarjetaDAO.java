package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TarjetasVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

public class TarjetaDAO extends DAOMaster{
    
    private static Logger myLogger = Logger.getLogger(OrdenDePagoDAO.class);
    
    public int insertDoctoTarjeta(TarjetasVO tarjeta, Connection con) throws ClientesException, SQLException {
        
        int respuesta = 0;
        String query = "INSERT INTO d_docto_tarjeta(dt_iddoctotar, dt_fechacaptura, dt_usuarioalta, dt_bancoorigen, dt_numlotebanco, dt_numlotesicap, dt_totaltarjetas, dt_archivo) VALUES (0,?,?,?,?,?,?,?);";
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement(query);
            ps.setTimestamp(1, Convertidor.toSqlTimeStamp(new Date()));
            ps.setString(2, tarjeta.getUsuario());
            ps.setInt(3, tarjeta.getBanco());
            ps.setString(4, tarjeta.getLoteBanco());
            ps.setString(5, tarjeta.getLoteSicap());
            ps.setInt(6, tarjeta.getTotTarjetas());
            ps.setString(7, tarjeta.getArchivo());
            myLogger.debug("Ejecutando: "+ps.toString());
            respuesta = ps.executeUpdate();
            if(respuesta == 1){
                res = ps.getGeneratedKeys();
                if(res.next())
                    respuesta = res.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
            if(res!=null){
                res.close();
            }
        }
        return respuesta;
    }
    
    public int insertMovtoTarjeta(TarjetasVO tarjeta, Connection con) throws ClientesException, SQLException {
        
        int respuesta = 0;
        String query = "INSERT INTO d_movto_tarjeta(mt_idmovtotar, mt_iddocto, mt_bancoorigen, mt_numtarjeta)"
                +" VALUES(0,?,?,?);";
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tarjeta.getCarga());
            ps.setInt(2, tarjeta.getBanco());
            ps.setString(3, tarjeta.getTarjeta());
            myLogger.debug("Ejecutando: "+ps.toString());
            respuesta = ps.executeUpdate();
        } catch (SQLException sqle) {
            //respuesta = 0;
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{if(ps!=null){
                ps.close();
            }
            if(res!=null){
                res.close();
            }
            return respuesta;
        }
    }
    
    public ArrayList<TarjetasVO> selectAsignaTarjeta(int banco, int idSucursal) throws ClientesException, SQLException {
        
        ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
        int respuesta = 0;
        String query = "SELECT mt_numtarjeta,en_numcliente,en_nombre,en_primer_apellido,en_segundo_apellido,en_rfc,en_fecha_nac,su_nombre "
                +"FROM d_movto_tarjeta INNER JOIN d_clientes ON (mt_numcliente=en_numcliente) INNER JOIN c_sucursales ON (en_numsucursal=su_numsucursal) "
                +"WHERE mt_bancoorigen=? AND mt_asignada=1 AND mt_esbaja=0 AND mt_numenvio=0";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        SucursalVO sucursal = null;
        try {
            con = getConnection();
            if(idSucursal > 0)
                query+=" AND en_numsucursal="+idSucursal;
            ps = con.prepareStatement(query);
            ps.setInt(1, banco);
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            while (res.next()){
                sucursal = new SucursalVO();
                sucursal.setNombre(res.getString("su_nombre"));
                arrTarjetas.add(new TarjetasVO(res.getString("mt_numtarjeta"), new ClienteVO(res.getInt("en_numcliente"), res.getString("en_nombre"), res.getString("en_primer_apellido"), res.getString("en_segundo_apellido"), res.getString("en_rfc"), res.getDate("en_fecha_nac")), sucursal));
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
            if(res!=null){
                res.close();
            }
            return arrTarjetas;
        }
    }
    
    public int getEnvioTarjeta(Date fecha) throws ClientesException, SQLException {
        
        int envio = 1;
        String query = "SELECT MAX(mt_numenvio) AS envio FROM d_movto_tarjeta WHERE mt_fechaenvio>=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, Convertidor.toSqlDate(fecha));
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                envio += res.getInt("envio");
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
            if(res!=null){
                res.close();
            }
        }
        return envio;
    }
    
    public int getEnvioFondeoTarjeta(Date fecha) throws ClientesException, SQLException {
        
        int envio = 1;
        String query = "SELECT MAX(ft_envio) AS envio FROM d_fondeo_tarjetas WHERE ft_fecha_envio>=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, Convertidor.toSqlDate(fecha));
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                if(res.getInt("envio") == 0)
                    envio = 41;
                else
                    envio += res.getInt("envio");
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
            if(res!=null){
                res.close();
            }
        }
        return envio;
    }
    
    public int verificaEnvioTarjeta(TarjetasVO tarjeta, int banco) throws ClientesException, SQLException {
        
        int envio = 0;
        String query = "SELECT mt_numenvio FROM d_movto_tarjeta WHERE mt_bancoorigen=? AND mt_numtarjeta=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, banco);
            ps.setString(2, tarjeta.getTarjeta());
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                envio += res.getInt("mt_numenvio");
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
            if(res!=null){
                res.close();
            }
        }
        return envio;
    }
    
    public int verificaEnvioFondeoTarjeta(TarjetasVO tarjeta, int banco) throws ClientesException, SQLException {
        
        int envio = 0;
        String query = "SELECT ft_envio FROM d_fondeo_tarjetas WHERE ft_banco=? AND ft_tarjeta=? AND ft_numcliente=? AND ft_numsolicitud=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, banco);
            ps.setString(2, tarjeta.getTarjeta());
            ps.setInt(3, tarjeta.getIdCliente());
            ps.setInt(4, tarjeta.getIdSolicitud());
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                envio += res.getInt("ft_envio");
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
            if(res!=null){
                res.close();
            }
        }
        return envio;
    }
    
    public void updateAsignaTarjeta(TarjetasVO tarjeta, int envio, int banco, Connection con) throws ClientesException, SQLException {
        
        String query = "UPDATE d_movto_tarjeta SET mt_numenvio=?,mt_fechaenvio=? WHERE mt_bancoorigen=? AND mt_numtarjeta=?;";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, envio);
            ps.setTimestamp(2, Convertidor.toSqlTimeStamp(new Date()));
            ps.setInt(3, banco);
            ps.setString(4, tarjeta.getTarjeta());
            myLogger.debug("Ejecutando: "+ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    public void updateFondeoTarjeta(TarjetasVO tarjeta, int envio, int banco, Connection con) throws ClientesException, SQLException {
        
        String query = "UPDATE d_fondeo_tarjetas SET ft_fecha_envio=?,ft_estatus=?,ft_envio=?,ft_usuario=? WHERE ft_banco=? AND ft_tarjeta=? AND ft_numcliente=? AND ft_numsolicitud=?;";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setTimestamp(1, Convertidor.toSqlTimeStamp(new Date()));
            ps.setInt(2, tarjeta.getEstatus());
            ps.setInt(3, envio);
            ps.setString(4, tarjeta.getUsuario());
            ps.setInt(5, banco);
            ps.setString(6, tarjeta.getTarjeta());
            ps.setInt(7, tarjeta.getIdCliente());
            ps.setInt(8, tarjeta.getIdSolicitud());
            myLogger.debug("Ejecutando: "+ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    public String getClienteTarjeta(int idCliente, Connection con) throws ClientesException, SQLException {
        
        String tarjeta = "";
        String query = "SELECT mt_numtarjeta FROM d_movto_tarjeta WHERE mt_asignada=1 AND mt_esbaja=0 AND mt_numenvio!=0 AND mt_numcliente=?;";
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next())
                tarjeta = res.getString("mt_numtarjeta");
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
            if(res!=null){
                res.close();
            }
        }
        return tarjeta;
    }
    
    public void insertFondeoTarjeta(TarjetasVO tarjeta, Connection con) throws ClientesException, SQLException {
        
        String query = "INSERT INTO d_fondeo_tarjetas (ft_numcliente, ft_numsolicitud, ft_numsucursal, ft_usuario, ft_nombre_cliente, ft_monto, "
                + "ft_tarjeta, ft_banco, ft_estatus, ft_fecha_captura, ft_fecha_envio, ft_fecha_cancelacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tarjeta.getIdCliente());
            ps.setInt(2, tarjeta.getIdSolicitud());
            ps.setInt(3, tarjeta.getIdSucursal());
            ps.setString(4, tarjeta.getUsuario());
            ps.setString(5, tarjeta.getNombre());
            ps.setDouble(6, tarjeta.getMonto());
            ps.setString(7, tarjeta.getTarjeta());
            ps.setInt(8, tarjeta.getBanco());
            ps.setInt(9, tarjeta.getEstatus());
            ps.setTimestamp(10, null);
            ps.setTimestamp(11, null);
            ps.setTimestamp(12, null);
            myLogger.debug("Ejecutando: "+ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    public TarjetasVO getTarjetaClinete(int idCliente, int idSolicitud) throws ClientesException, SQLException {
        
        TarjetasVO tarjeta = new TarjetasVO();
        String query = "SELECT d_fondeo_tarjetas.*, so_numoperacion 'operacion', op_nombre AS 'producto', su_nombre AS 'sucursal', co_descripcion AS 'estatus', gr_nombre AS 'grupo' "
                +"FROM d_fondeo_tarjetas JOIN c_sucursales ON (ft_numsucursal=su_numsucursal) JOIN c_estatus_ordenes_pago ON (ft_estatus=co_num_estatus) "
                +"JOIN (d_solicitudes JOIN c_operaciones ON (so_numoperacion=op_numoperacion)) ON (ft_numcliente=so_numcliente AND ft_numsolicitud=so_numsolicitud) "
                +"LEFT JOIN (d_integrantes_ciclo JOIN d_grupos ON (gr_numgrupo=ic_numgrupo)) ON (ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                +"WHERE ft_numcliente=? AND ft_numsolicitud=? AND ft_estatus BETWEEN 1 AND 8 ORDER BY ft_identificador DESC LIMIT 1";
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                tarjeta.setTarjeta(res.getString("ft_tarjeta"));
                tarjeta.setIdCliente(res.getInt("ft_numcliente"));
                tarjeta.setIdSolicitud(res.getInt("ft_numsolicitud"));
                tarjeta.setIdSucursal(res.getInt("ft_numsucursal"));
                tarjeta.setNombre(res.getString("ft_nombre_cliente"));
                tarjeta.setMonto(res.getDouble("ft_monto"));
                tarjeta.setFechaCaptura(res.getTimestamp("ft_fecha_captura"));
                tarjeta.setFechaEnvio(res.getTimestamp("ft_fecha_envio"));
                tarjeta.setBanco(res.getInt("ft_banco"));
                tarjeta.setEstatus(res.getInt("ft_estatus"));
                //tarjeta.setNombreArchivo(res.getString("pg_nombre_archivo"));
                tarjeta.setUsuario(res.getString("ft_usuario"));
                /*tarjeta.setIdOperacion(rs.getInt("operacion"));
                oPago.setProducto(rs.getString("producto"));
                oPago.setNomSucursal(rs.getString("sucursal"));
                tarjeta.setDescEstatus(res.getString("estatus"));*/
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
            if(res!=null){
                res.close();
            }
        }
        return tarjeta;
    }
    
    public int updateTarjetaConfirmaDesembolso(Connection con, TarjetasVO tarjeta) throws ClientesException, SQLException {
        
        int resp = 0;
        String query = "UPDATE d_fondeo_tarjetas SET ft_tarjeta=?, ft_numcliente=?, ft_numsolicitud=?, ft_numsucursal=?, ft_nombre_cliente=?, ft_monto=?, ft_fecha_captura=?, "
                +"ft_fecha_envio=?, ft_fecha_cancelacion=?, ft_banco=?, ft_estatus=?, ft_nombre_archivo=?, ft_usuario=? "
                +"WHERE ft_numcliente=? AND ft_numsolicitud=? AND ft_estatus=8 ";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, tarjeta.getTarjeta());
            ps.setInt(2, tarjeta.getIdCliente());
            ps.setInt(3, tarjeta.getIdSolicitud());
            ps.setInt(4, tarjeta.getIdSucursal());
            ps.setString(5, tarjeta.getNombre());
            ps.setDouble(6, tarjeta.getMonto());
            ps.setTimestamp(7, tarjeta.getFechaCaptura());
            ps.setTimestamp(8, tarjeta.getFechaEnvio());
            ps.setTimestamp(9, tarjeta.getFechaCancelacion());
            ps.setInt(10, tarjeta.getBanco());
            ps.setInt(11, tarjeta.getEstatus());
            ps.setString(12, "");
            ps.setString(13, tarjeta.getUsuario());
            ps.setInt(14, tarjeta.getIdCliente());
            ps.setInt(15, tarjeta.getIdSolicitud());
            myLogger.debug("Ejecutando: "+ps.toString());
            resp = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
        }
        return resp;
    }
    
    public int getBancoTarjeta(int idCliente, String tarjeta, Connection con) throws ClientesException, SQLException {
        
        int banco = 0;
        String query = "SELECT mt_bancoorigen FROM d_movto_tarjeta WHERE mt_numcliente=? and mt_numtarjeta=?;";
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setString(2, tarjeta);
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            if(res.next())
                banco = res.getInt("mt_bancoorigen");
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null){
                ps.close();
            }
            if(res!=null){
                res.close();
            }
        }
        return banco;
    }
    
    public ArrayList<TarjetasVO> selectFondeoTarjeta(int banco, int equipo, int ciclo, int estatus) throws ClientesException, SQLException {
        
        ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
        String query = "SELECT ft_identificador,ft_tarjeta,ft_numcliente,ft_numsolicitud,ft_numsucursal,ft_nombre_cliente,ft_monto,ft_fecha_captura,ft_fecha_envio,ft_banco,ft_estatus," +
                "ft_nombre_archivo,ft_usuario,ft_envio,so_numoperacion 'operacion',op_nombre AS 'producto',su_nombre AS 'sucursal',co_descripcion AS 'estatus',gr_nombre AS 'grupo' " +
                "FROM d_fondeo_tarjetas LEFT JOIN d_integrantes_ciclo ON (ft_numcliente=ic_numcliente AND ft_numsolicitud=ic_numsolicitud) " +
                "LEFT JOIN d_solicitudes ON (ft_numcliente=so_numcliente AND ft_numsolicitud=so_numsolicitud) LEFT JOIN c_operaciones ON (so_numoperacion=op_numoperacion) " +
                "LEFT JOIN c_sucursales ON (ft_numsucursal=su_numsucursal) LEFT JOIN c_estatus_ordenes_pago ON (ft_estatus=co_num_estatus) " +
                "LEFT JOIN d_grupos ON (gr_numgrupo=ic_numgrupo AND gr_numsucursal=ft_numsucursal) WHERE ft_banco=? AND ic_numgrupo=? AND ic_numciclo=? AND ft_estatus>0";
        query += (estatus > 0 ? " AND ft_estatus=? " : " ");
        query += "ORDER BY ft_estatus, grupo, ft_numcliente, ft_numsolicitud ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        SucursalVO sucursal = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, banco);
            ps.setInt(2, equipo);
            ps.setInt(3, ciclo);
            if(estatus > 0)
                ps.setInt(4, estatus);
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeQuery();
            while (res.next()) {
                sucursal = new SucursalVO();
                sucursal.setNombre(res.getString("sucursal"));
                sucursal.setIdSucursal(res.getInt("ft_numsucursal"));
                arrTarjetas.add(new TarjetasVO(res.getInt("ft_identificador"), res.getTimestamp("ft_fecha_captura"), res.getString("ft_usuario"), res.getInt("ft_banco"), res.getString("ft_tarjeta"), res.getInt("ft_numcliente"),
                        res.getInt("ft_envio"), res.getTimestamp("ft_fecha_envio"), res.getString("ft_nombre_archivo"), res.getInt("ft_numsolicitud"), sucursal, res.getString("ft_nombre_cliente"),
                        res.getDouble("ft_monto"), res.getInt("ft_estatus"), res.getString("grupo") != null ? res.getString("grupo") : "--", res.getString("estatus")));
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
            if(res!=null){
                res.close();
            }
        }
        return arrTarjetas;
    }
}

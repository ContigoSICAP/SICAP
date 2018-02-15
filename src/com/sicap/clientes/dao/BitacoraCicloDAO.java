package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.BitacoraCicloVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class BitacoraCicloDAO extends DAOMaster{
    
    private static Logger myLogger = Logger.getLogger(BitacoraDAO.class);
    
    public ArrayList<BitacoraCicloVO> getUltimaModificacion (int idEquipo, Date fechaInicio, Date fechaFin, int idEstatus, int idSucursal, String analista, int subProd) throws ClientesException{
        
        String query ="";
        String query2 ="";
        if(subProd == 0){
            query ="SELECT ci_numgrupo, ci_numciclo, ci_numcredito_ibs, ci_estatus AS estatus, bc_numcomentario, bc_comentario, bc_usuario_comentario, bc_usuario_asignado, bc_fecha, bc_SemDisp, gr_nombre, gr_numsucursal, "
                +"IF(bc_usuario_comentario=\"sistema\", \"Sistema\", u1.nombre_completo) usuarioModificacion, IF(bc_usuario_asignado=\"sistema\", \"Sistema\", u2.nombre_completo) usuarioAsignado "
                +"FROM d_bitacora_ciclo bc INNER JOIN d_ciclos_grupales ON (bc_numgrupo=ci_numgrupo AND bc_numciclo=ci_numciclo AND bc_estatus_ciclo=ci_estatus) "
                +"INNER JOIN d_grupos e ON(ci_numgrupo=gr_numgrupo) LEFT JOIN users u1 ON(bc_usuario_comentario=u1.user_name) LEFT JOIN users u2 ON(bc_usuario_asignado=u2.user_name) "
                +"WHERE bc_numcomentario=(SELECT MAX(bc2.bc_numcomentario) FROM d_bitacora_ciclo bc2 WHERE bc2.bc_numgrupo=bc.bc_numgrupo AND bc2.bc_numciclo=bc.bc_numciclo AND bc2.bc_estatus_ciclo=bc.bc_estatus_ciclo) ";
        } else if(subProd == 1){
            query ="SELECT ci_numgrupo, ci_numciclo, ci_numcredito_ibs, ci_estatusIC AS estatus, bc_numcomentario, bc_comentario, bc_usuario_comentario, bc_usuario_asignado, bc_fecha, bc_SemDisp, gr_nombre, gr_numsucursal, "
                +"IF(bc_usuario_comentario=\"sistema\", \"Sistema\", u1.nombre_completo) usuarioModificacion, IF(bc_usuario_asignado=\"sistema\", \"Sistema\", u2.nombre_completo) usuarioAsignado "
                +"FROM d_bitacora_ciclo bc INNER JOIN d_ciclos_grupales ON (bc_numgrupo=ci_numgrupo AND bc_numciclo=ci_numciclo AND bc_estatus_ciclo=ci_estatusIC) "
                +"INNER JOIN d_grupos e ON(ci_numgrupo=gr_numgrupo) LEFT JOIN users u1 ON(bc_usuario_comentario=u1.user_name) LEFT JOIN users u2 ON(bc_usuario_asignado=u2.user_name) "
                +"WHERE bc_numcomentario=(SELECT MAX(bc2.bc_numcomentario) FROM d_bitacora_ciclo bc2 WHERE bc2.bc_numgrupo=bc.bc_numgrupo AND bc2.bc_numciclo=bc.bc_numciclo AND bc2.bc_estatus_ciclo=bc.bc_estatus_ciclo) ";
            
            query2 ="SELECT ci_numgrupo, ci_numciclo, ci_numcredito_ibs, ci_estatusIC_2 AS estatus, bc_numcomentario, bc_comentario, bc_usuario_comentario, bc_usuario_asignado, bc_fecha, bc_SemDisp, gr_nombre, gr_numsucursal, "
                +"IF(bc_usuario_comentario=\"sistema\", \"Sistema\", u1.nombre_completo) usuarioModificacion, IF(bc_usuario_asignado=\"sistema\", \"Sistema\", u2.nombre_completo) usuarioAsignado "
                +"FROM d_bitacora_ciclo bc INNER JOIN d_ciclos_grupales ON (bc_numgrupo=ci_numgrupo AND bc_numciclo=ci_numciclo AND bc_estatus_ciclo=ci_estatusIC_2) "
                +"INNER JOIN d_grupos e ON(ci_numgrupo=gr_numgrupo) LEFT JOIN users u1 ON(bc_usuario_comentario=u1.user_name) LEFT JOIN users u2 ON(bc_usuario_asignado=u2.user_name) "
                +"WHERE bc_numcomentario=(SELECT MAX(bc2.bc_numcomentario) FROM d_bitacora_ciclo bc2 WHERE bc2.bc_numgrupo=bc.bc_numgrupo AND bc2.bc_numciclo=bc.bc_numciclo AND bc2.bc_estatus_ciclo=bc.bc_estatus_ciclo) ";
        }
        ArrayList<BitacoraCicloVO> registros = new ArrayList<BitacoraCicloVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int parametro = 1, controlParam = -1;
        
        try {
            if(idEquipo!=0){
                query+="AND ci_numgrupo=? ";
                if(subProd == 1){
                    query2+="AND ci_numgrupo=? ";
                    controlParam++;
                }
            }
            if(fechaInicio!=null&&fechaFin!=null){
                query+="AND DATE(bc_fecha) BETWEEN ? AND ? ";
                if(subProd == 1){
                    query2+="AND DATE(bc_fecha) BETWEEN ? AND ? ";
                    controlParam+= 2;
                }
            }else{
                query+="AND ci_fecha_dispersion >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ";
                query2+="AND ci_fecha_dispersion >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ";
            }
            if(subProd == 0){
                if(idEstatus!=0)
                    query+="AND ci_estatus=? ";
                else
                    query+="AND ci_estatus!=0 ";
            } else if(subProd == 1) {
                if(idEstatus!=0){
                    query+="AND ci_estatusIC=? ";
                    query2+="AND ci_estatusIC_2=? ";
                    controlParam++;
                }else{
                    query+="AND ci_numcredito_ibs!=0 AND ci_estatusIC!=0 ";
                    query2+="AND ci_numcredito_ibs!=0 AND ci_estatusIC_2!=0 ";
                }
            }
            if(idSucursal!=0){
                query+="AND gr_numsucursal=? ";
                if(subProd == 1){
                    query2+="AND gr_numsucursal=? ";
                    controlParam++;
                }
            }
            if(!analista.equals("")){
                query+="AND bc_usuario_asignado=? ";
                if(subProd == 1){
                    query2+="AND bc_usuario_asignado=? ";
                    controlParam++;
                }
            }
            if(subProd == 1){
                query = query+"UNION "+query2;
            }
            query+=" ORDER BY bc_fecha DESC;";
            cn = getConnection();
            ps = cn.prepareStatement(query);
            
            if(idEquipo!=0){
                ps.setInt(parametro++, idEquipo);
                if(subProd == 1)
                    ps.setInt(parametro + controlParam, idEquipo);
            }
            myLogger.debug("ps "+ ps.toString());
            if(fechaInicio!=null&&fechaFin!=null){
                ps.setDate(parametro++, fechaInicio);
                if(subProd == 1)
                    ps.setDate(parametro + controlParam, fechaInicio);
                ps.setDate(parametro++, fechaFin);
                if(subProd == 1)
                    ps.setDate(parametro + controlParam, fechaFin);
            }
            myLogger.debug("ps "+ ps.toString());
            if(idEstatus!=0){
                if(subProd == 0)
                    ps.setInt(parametro++, idEstatus);
                else if (subProd == 1){
                    ps.setInt(parametro++, idEstatus);
                    ps.setInt(parametro + controlParam, idEstatus);
                }
            }
            myLogger.debug("ps "+ ps.toString());
            if(idSucursal!=0){
                ps.setInt(parametro++, idSucursal);
                if(subProd == 1)
                    ps.setInt(parametro + controlParam, idSucursal);
            }
            myLogger.debug("ps "+ ps.toString());
            if(!analista.equals("")){
                ps.setString(parametro++, analista);
                if(subProd == 1)
                    ps.setString(parametro + controlParam, analista);
            }
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                BitacoraCicloVO bitacora = new BitacoraCicloVO();
                bitacora.setIdEquipo(rs.getInt("ci_numgrupo"));
                bitacora.setIdCiclo(rs.getInt("ci_numciclo"));
                bitacora.setIdCredito(rs.getInt("ci_numcredito_ibs"));
                bitacora.setNombreEquipo(rs.getString("gr_nombre"));
                bitacora.setEstatus(rs.getInt("estatus"));
                bitacora.setIdSucursal(rs.getInt("gr_numsucursal"));
                bitacora.setIdComentario(rs.getInt("bc_numcomentario"));
                bitacora.setComentario(rs.getString("bc_comentario"));
                bitacora.setUsuarioComentario(rs.getString("usuarioModificacion"));
                bitacora.setUsuarioAsignado(rs.getString("usuarioAsignado"));
                bitacora.setFechaHora(rs.getTimestamp("bc_fecha"));
                bitacora.setSemDisp(rs.getInt("bc_SemDisp"));
                registros.add(bitacora);
            }
        } catch (SQLException sqle) {
            myLogger.error("getUltimaModificacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUltimaModificacion", e);
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
        return registros;        
    }
    public ArrayList<BitacoraCicloVO> getBitacoraFechaNumComent (int idEquipo, int idCiclo) throws ClientesException{
        String query = "SELECT * FROM d_bitacora_ciclo " +
                       " WHERE bc_numgrupo = ?" +
                       " AND bc_numciclo = ?" +
                       " order by bc_numcomentario desc, bc_fecha desc";
        
        ArrayList<BitacoraCicloVO> registros = new ArrayList<BitacoraCicloVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                BitacoraCicloVO bitacora = new BitacoraCicloVO();
                bitacora.setComentario(rs.getString("bc_comentario"));
                bitacora.setEstatus(rs.getInt("bc_estatus_ciclo"));
                bitacora.setFechaHora(rs.getTimestamp("bc_fecha"));
                bitacora.setIdCiclo(rs.getInt("bc_numciclo"));
                bitacora.setIdComentario(rs.getInt("bc_numcomentario"));
                bitacora.setIdEquipo(rs.getInt("bc_numgrupo"));
                bitacora.setUsuarioAsignado(rs.getString("bc_usuario_asignado"));
                bitacora.setUsuarioComentario(rs.getString("bc_usuario_comentario"));
                registros.add(bitacora);
            }
        } catch (SQLException sqle) {
            myLogger.error("getHistorialEquipo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getHistorialEquipo", e);
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
        return registros;        
    }
    
    public ArrayList<BitacoraCicloVO> getHistorialEquipo (int idEquipo, int idCiclo) throws ClientesException{
        String query = "SELECT bc_fecha, bc_estatus_ciclo, IF(bc_usuario_comentario=\"sistema\", \"Sistema\", b.nombre_completo) usuarioModificacion, IF(bc_usuario_asignado=\"sistema\", \"Sistema\", c.nombre_completo) usuarioAsignado, bc_comentario " +
                "FROM d_bitacora_ciclo a LEFT JOIN users b ON(bc_usuario_comentario=b.user_name) LEFT JOIN users c ON(bc_usuario_asignado=c.user_name) " +
                "WHERE bc_numgrupo=? AND bc_numciclo=? ORDER BY bc_fecha DESC";
        
        ArrayList<BitacoraCicloVO> registros = new ArrayList<BitacoraCicloVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                BitacoraCicloVO bitacora = new BitacoraCicloVO();
                bitacora.setEstatus(rs.getInt("bc_estatus_ciclo"));
                bitacora.setComentario(rs.getString("bc_comentario"));
                bitacora.setUsuarioComentario(rs.getString("usuarioModificacion"));
                bitacora.setUsuarioAsignado(rs.getString("usuarioAsignado"));
                bitacora.setFechaHora(rs.getTimestamp("bc_fecha"));
                registros.add(bitacora);
            }
        } catch (SQLException sqle) {
            myLogger.error("getHistorialEquipo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getHistorialEquipo", e);
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
        return registros;        
    }
    
    public int getNumComentario (int idEquipo, int idCiclo) throws ClientesException{
        String query="SELECT MAX(bc_numcomentario) numComentario FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=?";
        int numComentario=0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                numComentario = rs.getInt("numComentario");
            }
        }catch (SQLException sqle) {
            myLogger.error("getNumComentario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getNumComentario", e);
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
        return numComentario;
    }
    
    public BitacoraCicloVO getUltimoRegistro (int idEquipo, int idCiclo) throws ClientesException{
        String query="SELECT * FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? ORDER BY bc_fecha DESC LIMIT 1";
        BitacoraCicloVO registro = new BitacoraCicloVO();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                registro.setComentario(rs.getString("bc_comentario"));
                registro.setEstatus(rs.getInt("bc_estatus_ciclo"));
                registro.setFechaHora(rs.getTimestamp("bc_fecha"));
                registro.setIdCiclo(rs.getInt("bc_numciclo"));
                registro.setIdComentario(rs.getInt("bc_numcomentario"));
                registro.setIdEquipo(rs.getInt("bc_numgrupo"));
                registro.setUsuarioAsignado(rs.getString("bc_usuario_asignado"));
                registro.setUsuarioComentario(rs.getString("bc_usuario_comentario"));
            }
        }catch (SQLException sqle) {
            myLogger.error("getUltimoRegistro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUltimoRegistro", e);
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
        return registro;
    }
        public BitacoraCicloVO getPrimerRegistroEstatus (int idEquipo, int idCiclo, int estatus) throws ClientesException{
        String query="SELECT * FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? and bc_estatus_ciclo = ? ORDER BY bc_fecha LIMIT 1";
        BitacoraCicloVO registro = new BitacoraCicloVO();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            ps.setInt(3, estatus);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                registro.setComentario(rs.getString("bc_comentario"));
                registro.setEstatus(rs.getInt("bc_estatus_ciclo"));
                registro.setFechaHora(rs.getTimestamp("bc_fecha"));
                registro.setIdCiclo(rs.getInt("bc_numciclo"));
                registro.setIdComentario(rs.getInt("bc_numcomentario"));
                registro.setIdEquipo(rs.getInt("bc_numgrupo"));
                registro.setUsuarioAsignado(rs.getString("bc_usuario_asignado"));
                registro.setUsuarioComentario(rs.getString("bc_usuario_comentario"));
            }
        }catch (SQLException sqle) {
            myLogger.error("getUltimoRegistro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUltimoRegistro", e);
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
        return registro;
    }
    
    public String getUltimoComentario (int idEquipo, int idCiclo) throws ClientesException{
        String query="SELECT bc_comentario FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? ORDER BY bc_fecha DESC LIMIT 1";
        String comentario = "";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                comentario = rs.getString("bc_comentario");                
            }
        }catch (SQLException sqle) {
            myLogger.error("getUltimoComentario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUltimoComentario", e);
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
        return comentario;
    }
    
    public void insertaBitacoraCiclo(Connection con, BitacoraCicloVO bitacora) throws ClientesException{
        String query="INSERT INTO d_bitacora_ciclo (bc_numgrupo, bc_numciclo, bc_estatus_ciclo, bc_numcomentario, bc_comentario, bc_usuario_comentario, bc_usuario_asignado, bc_SemDisp) VALUES (?,?,?,?,?,?,?,?)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (con == null){
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = con.prepareStatement(query);
            }
            ps.setInt(1, bitacora.getIdEquipo());
            ps.setInt(2, bitacora.getIdCiclo());
            ps.setInt(3, bitacora.getEstatus());
            ps.setInt(4, bitacora.getIdComentario());
            ps.setString(5, bitacora.getComentario());
            ps.setString(6, bitacora.getUsuarioComentario());
            ps.setString(7, bitacora.getUsuarioAsignado());
            ps.setInt(8, bitacora.getSemDisp());
            myLogger.debug("Ejecutando: "+ ps.toString());
            ps.executeUpdate();
        }catch (SQLException sqle) {
            myLogger.error("insertaBitacoraCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("insertaBitacoraCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if(cn != null){
                    cn.close();
                }
                if (ps != null){
                    ps.close();
                }
            } catch (SQLException sqle){
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public String getUsuarioEstatus(int numEquipo, int numCiclo, int estatus) throws ClientesException{
        
        String query="SELECT bc_usuario_comentario FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? AND bc_estatus_ciclo=? ORDER BY bc_fecha DESC LIMIT 1;";
        if(estatus == ClientesConstants.CICLO_AUTORIZADO)
            query = "SELECT bc_usuario_comentario FROM d_bitacora_ciclo INNER JOIN users ON (bc_usuario_comentario=user_name and num_sucursal=33 and puesto!=0)"
                    +"WHERE bc_numgrupo=? AND bc_numciclo=? AND bc_estatus_ciclo=? ORDER BY bc_fecha DESC LIMIT 1;";
        String usuario ="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            ps.setInt(3, estatus);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                usuario=rs.getString("bc_usuario_comentario");
            }
        }catch (SQLException sqle) {
            myLogger.error("getUsuarioEstatus", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUsuarioEstatus", e);
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
    public String getUsuarioEstatusIC(int numEquipo, int numCiclo, int estatus, int SemDisp) throws ClientesException{
        
        String query="SELECT bc_usuario_comentario FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? AND bc_estatus_ciclo=? AND bc_SemDisp = ? ORDER BY bc_fecha DESC LIMIT 1;";
        if(estatus == ClientesConstants.CICLO_AUTORIZADO)
            query = "SELECT bc_usuario_comentario FROM d_bitacora_ciclo INNER JOIN users ON (bc_usuario_comentario=user_name and num_sucursal=33 and puesto!=0)"
                    +"WHERE bc_numgrupo=? AND bc_numciclo=? AND bc_estatus_ciclo=? AND bc_SemDisp = ? ORDER BY bc_fecha DESC LIMIT 1;";
        String usuario ="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            ps.setInt(3, estatus);
            ps.setInt(4, SemDisp);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                usuario=rs.getString("bc_usuario_comentario");
            }
        }catch (SQLException sqle) {
            myLogger.error("getUsuarioEstatus", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            myLogger.error("getUsuarioEstatus", e);
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
    
    public String getUsuarioAsignado(int numEquipo, int numCiclo) throws ClientesException{
        String query="SELECT bc_usuario_asignado FROM d_bitacora_ciclo WHERE bc_numgrupo=? AND bc_numciclo=? ORDER BY bc_fecha DESC,bc_numcomentario DESC LIMIT 1;";
        String usuario ="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                usuario=rs.getString("bc_usuario_asignado");
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

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import org.apache.log4j.Logger;

public class BitacoraEstatusDAO extends DAOMaster {

    private static Logger logger = Logger.getLogger(InformacionFinancieraDAO.class);

    public int add(BitacoraEstatusVO bitacora) throws ClientesException {

        String query = "INSERT INTO D_BITACORA_ESTATUS(BE_NUMCLIENTE, BE_NUMSOLICITUD, BE_ESTATUS, BE_USUARIO, BE_ANALISIS_CREDITO, BE_FECHA_HORA, be_numComentario,be_comentario) VALUES(?,?,?,?,?,CURRENT_TIMESTAMP,?,?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, bitacora.idCliente);
            ps.setInt(param++, bitacora.idSolicitud);
            ps.setInt(param++, bitacora.estatus);
            ps.setString(param++, bitacora.usuario);
            ps.setInt(param++, bitacora.esAnalisisCredito);
            ps.setInt(param++,bitacora.numComentario);
            ps.setString(param++,bitacora.comentario);
            logger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("add", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("add", e);
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

    public BitacoraEstatusVO[] getUltimoRegistroInsertado(int idCliente, int idSolicitud) throws ClientesException {
        String query = "SELECT * FROM (SELECT * FROM D_BITACORA_ESTATUS where BE_ANALISIS_CREDITO = 1 AND BE_NUMCLIENTE = ? AND BE_NUMSOLICITUD = ? ORDER BY BE_FECHA_HORA DESC LIMIT 1) A "
                + "UNION SELECT * FROM(SELECT * FROM D_BITACORA_ESTATUS where BE_ANALISIS_CREDITO = 0 AND BE_NUMCLIENTE = ? AND BE_NUMSOLICITUD = ? ORDER BY BE_FECHA_HORA DESC LIMIT 1) B";
        Connection con = null;
        ArrayList<BitacoraEstatusVO> listBit = new ArrayList<BitacoraEstatusVO>();
        BitacoraEstatusVO[] arrayBitVO = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BitacoraEstatusVO bitVO = new BitacoraEstatusVO();
                bitVO.idCliente = idCliente;
                bitVO.idSolicitud = idSolicitud;
                bitVO.estatus = rs.getInt("BE_ESTATUS");
                bitVO.usuario = rs.getString("BE_USUARIO");
                bitVO.esAnalisisCredito = rs.getInt("BE_ANALISIS_CREDITO");
                bitVO.fechaHora = rs.getTimestamp("BE_FECHA_HORA");
                listBit.add(bitVO);
            }
            arrayBitVO = new BitacoraEstatusVO[listBit.size()];
            for (int i = 0; i < arrayBitVO.length; i++) {
                arrayBitVO[i] = listBit.get(i);
            }
        } catch (SQLException exc) {
            logger.error("getUltimoRegistroInsertado", exc );
        } catch (NamingException exc) {
            logger.error("getUltimoRegistroInsertado", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return arrayBitVO;
    }

    public BitacoraEstatusVO getUltimoRegistroInsertadoAnalisisCredito(int idCliente, int idSolicitud, int esAnalisisCredito) throws ClientesException {

        String query = "SELECT * FROM D_BITACORA_ESTATUS WHERE BE_NUMCLIENTE = ? AND BE_NUMSOLICITUD = ? AND BE_ANALISIS_CREDITO = ? ORDER BY BE_FECHA_HORA DESC LIMIT 1";
        logger.debug("Query a ejecutar :" + query);
        logger.debug("Parámetros :[ " + idCliente + " , " + idSolicitud + " , " + esAnalisisCredito + " ]");
        Connection con = null;
        BitacoraEstatusVO bitVO = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, esAnalisisCredito);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bitVO = new BitacoraEstatusVO();
                bitVO.idCliente = idCliente;
                bitVO.idSolicitud = idSolicitud;
                bitVO.estatus = rs.getInt("BE_ESTATUS");
                bitVO.usuario = rs.getString("BE_USUARIO");
                bitVO.esAnalisisCredito = esAnalisisCredito;
                bitVO.fechaHora = rs.getTimestamp("BE_FECHA_HORA");
            }
        } catch (SQLException exc) {
            logger.error("getUltimoRegistroInsertadoAnalisisCredito", exc );
        } catch (NamingException exc) {
            logger.error("getUltimoRegistroInsertadoAnalisisCredito", exc );
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return bitVO;
    }

    public BitacoraEstatusVO getPrimerRegistroInsertadoCapturista(int idCliente, int idSolicitud, int esAnalisisCredito, int estatus) throws ClientesException {

        String query = "SELECT * FROM D_BITACORA_ESTATUS WHERE BE_NUMCLIENTE = ? AND BE_NUMSOLICITUD = ? AND BE_ANALISIS_CREDITO = ? AND BE_ESTATUS = ? ORDER BY BE_FECHA_HORA ASC LIMIT 1";
        logger.debug("Query a ejecutar :" + query);
        logger.debug("Parámetros :[ " + idCliente + " , " + idSolicitud + " , " + esAnalisisCredito + " , " + estatus + " ]");
        Connection con = null;
        BitacoraEstatusVO bitVO = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, esAnalisisCredito);
            ps.setInt(param++, estatus);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bitVO = new BitacoraEstatusVO();
                bitVO.idCliente = idCliente;
                bitVO.idSolicitud = idSolicitud;
                bitVO.estatus = rs.getInt("BE_ESTATUS");
                bitVO.usuario = rs.getString("BE_USUARIO");
                bitVO.esAnalisisCredito = esAnalisisCredito;
                bitVO.fechaHora = rs.getTimestamp("BE_FECHA_HORA");
            }
        } catch (SQLException exc) {
            logger.error("getUltimoRegistroInsertadoAnalisisCredito", exc );
        } catch (NamingException exc) {
            logger.error("getUltimoRegistroInsertadoAnalisisCredito", exc );
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return bitVO;
    }
    public int getNumComentario (int idCliente, int idSolicitud) throws ClientesException{
        String query="SELECT max(be_numComentario) as numComentario FROM d_bitacora_estatus" +
                     " where be_numcliente = ?" +
                     " and be_numsolicitud = ?";
        int numComentario=0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            logger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                numComentario = rs.getInt("numComentario");
            }
        }catch (SQLException sqle) {
            logger.error("getNumComentario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            logger.error("getNumComentario", e);
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
    
        public ArrayList<BitacoraEstatusVO> getHistorialEquipo (int idCliente, int idSolicitud) throws ClientesException{
        String query = "SELECT * FROM d_bitacora_estatus, c_estatus_solicitud, users" +
                      " where be_usuario = user_name" +
                      " and be_estatus = es_numestatus" +
                      " and be_numcliente = ?" +
                      " and be_numsolicitud = ?"+
                      " order by be_fecha_hora desc";
        
        ArrayList<BitacoraEstatusVO> registros = new ArrayList<BitacoraEstatusVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            logger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                BitacoraEstatusVO bitacora = new BitacoraEstatusVO();
                bitacora.idCliente= rs.getInt("be_numcliente");
                bitacora.idSolicitud=rs.getInt("be_numsolicitud");
                bitacora.estatus = rs.getInt("be_estatus");
                bitacora.estatusDescripcion = rs.getString("es_descripcion");
                bitacora.usuario = rs.getString("nombre_completo");
                bitacora.numComentario = rs.getInt("be_numComentario");
                bitacora.comentario = rs.getString("be_comentario");
                bitacora.fechaHora=rs.getTimestamp("be_fecha_hora");
                registros.add(bitacora);
            }
        } catch (SQLException sqle) {
            logger.error("getHistorialEquipo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e){
            logger.error("getHistorialEquipo", e);
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

}

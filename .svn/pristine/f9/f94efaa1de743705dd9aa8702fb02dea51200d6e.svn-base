package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import org.apache.log4j.Logger;

public class ReferenciaGeneralDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(ReferenciaGeneralDAO.class);

    public void addReferencia(Connection conn, ReferenciaGeneralVO referencia) {
        String query = "INSERT INTO D_REFERENCIAS_GENERALES(RG_CONTRATO, RG_NUMCLIENTE, RG_NOMBRE, RG_REFERENCIA, RG_FECHA_INICIO, "
                + "RG_ES_T24, RG_PRODUCTO, RG_NUMSOLICITUD, RG_NUMSUCURSAL) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setString(param++, referencia.contrato);
            ps.setInt(param++, referencia.numcliente);
            ps.setString(param++, referencia.nombre);
            ps.setString(param++, referencia.referencia);
            ps.setInt(param++, referencia.esT24);
            ps.setInt(param++, referencia.producto);
            ps.setInt(param++, referencia.numSolicitud);
            ps.setInt(param++, referencia.numSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Referencia = " + referencia.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addReferencia", sqle);
        } catch (Exception e) {
            myLogger.error("addReferencia", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("addReferencia", e);
            }
        }
    }

    public void addReferenciaApertura(ReferenciaGeneralVO referencia) {
        String query = "INSERT INTO D_REFERENCIAS_GENERALES(RG_CONTRATO, RG_NUMCLIENTE, RG_NOMBRE, RG_REFERENCIA, RG_FECHA_INICIO, "
                + "RG_ES_T24, RG_PRODUCTO, RG_NUMSOLICITUD, RG_NUMSUCURSAL) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            //if( conn==null ){
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*}
             else{
             ps = conn.prepareStatement(query);
             }*/
            ps.setString(param++, referencia.contrato);
            ps.setInt(param++, referencia.numcliente);
            ps.setString(param++, referencia.nombre);
            ps.setString(param++, referencia.referencia);
            ps.setInt(param++, referencia.esT24);
            ps.setInt(param++, referencia.producto);
            ps.setInt(param++, referencia.numSolicitud);
            ps.setInt(param++, referencia.numSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Referencia = " + referencia.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addReferenciaApertura", sqle);
        } catch (Exception e) {
            myLogger.error("addReferenciaApertura", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("addReferenciaApertura", e);
            }
        }
    }

    public void addReferencia(ReferenciaGeneralVO referencia) {
        String query = "INSERT INTO D_REFERENCIAS_GENERALES(RG_CONTRATO, RG_NUMCLIENTE, RG_NOMBRE, RG_REFERENCIA, RG_FECHA_INICIO, "
                + "RG_ES_T24, RG_PRODUCTO, RG_NUMSOLICITUD, RG_NUMSUCURSAL) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
        Connection cn = null;
        int param = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia.contrato);
            ps.setInt(param++, referencia.numcliente);
            ps.setString(param++, referencia.nombre);
            ps.setString(param++, referencia.referencia);
            ps.setInt(param++, referencia.esT24);
            ps.setInt(param++, referencia.producto);
            ps.setInt(param++, referencia.numSolicitud);
            ps.setInt(param++, referencia.numSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Referencia = " + referencia.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addReferencia", sqle);
        } catch (Exception e) {
            myLogger.error("addReferencia", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("addReferencia", e);
            }
        }
    }

    public void updateSolicitudes() {
        myLogger.debug("Inicia actualizacion de solicitudes [" + new Date().toString() + "]");
        //CREA TABLA TEMPORAL DE RERFERENCIAS
        String drop = "DROP TABLE IF EXISTS CONTRATOS";
        String create = "CREATE TABLE CONTRATOS AS SELECT SO_NO_CONTRATO LD FROM D_SOLICITUDES WHERE SO_NO_CONTRATO IS NOT NULL";
        //ACTUALIZA LOS CONTRATOS PARA INDIVIDUOS QUE YA LO TENGAN EN SOLICITUDES
        String update = "UPDATE D_SOLICITUDES, D_REFERENCIAS_GENERALES SET RG_CONTRATO = SO_NO_CONTRATO WHERE SO_NUMCLIENTE = RG_NUMCLIENTE "
                + "AND SO_NUMSOLICITUD = RG_NUMSOLICITUD AND RG_CONTRATO IS NULL AND SO_NO_CONTRATO IS NOT NULL AND SUBSTR(RG_REFERENCIA, 4, 1) != 9";
        //ACTUALIZA LOS NUEVOS CONTRATOS DE T24  
        String update2 = "UPDATE D_CLIENTES, D_SALDOS_T24, D_DECISION_COMITE, D_SOLICITUDES S LEFT JOIN D_REFERENCIAS_GENERALES "
                + "ON RG_NUMCLIENTE = SO_NUMCLIENTE AND RG_NUMSOLICITUD = SO_NUMSOLICITUD SET SO_NO_CONTRATO = ST_CONTRATO, "
                + "RG_CONTRATO = ST_CONTRATO WHERE (S.SO_NO_CONTRATO IS NULL OR RG_CONTRATO IS NULL) AND "
                + "DATE(DE_FECHACAPTURA)>'2007-12-21' AND S.SO_ESTATUS IN (1,2) AND S.SO_NUMOPERACION != 3 "
                + "AND EN_NUMCLIENTE = S.SO_NUMCLIENTE AND S.SO_NUMCLIENTE = DE_NUMCLIENTE AND "
                + "S.SO_NUMSOLICITUD = DE_NUMSOLICITUD AND ST_RFC = SUBSTR(EN_RFC,1,10) AND ST_CONTRATO NOT IN (SELECT LD FROM CONTRATOS)";
        //ELIMINA LA TABLA TEMPORAL
        String delete = "DROP TABLE CONTRATOS";

        Connection con = null;
        try {
            con = getConnection();
            int resultado = 0;
            Statement stmt = con.createStatement();
            myLogger.debug("Ejecutando = " + drop);
            stmt.executeUpdate(drop);
            myLogger.debug("Ejecutando = " + create);
            stmt.executeUpdate(create);
            myLogger.debug("Ejecutando = " + update);
            resultado = stmt.executeUpdate(update);
            myLogger.debug("Registros actualizados = " + resultado);
            myLogger.debug("Ejecutando = " + update2);
            resultado = stmt.executeUpdate(update2);
            myLogger.debug("Registros actualizados2 = " + resultado);
            myLogger.debug("Ejecutando = " + delete);
            stmt.executeUpdate(delete);
            myLogger.debug("Finaliza actualizacion de solicitudes [" + new Date().toString() + "]");
        } catch (NamingException exc) {
            myLogger.error("updateSolicitudes", exc);
        } catch (SQLException exc) {
            myLogger.error("updateSolicitudes", exc);
        } finally {
            myLogger.debug("Finaliza con excepcion actualizacion de solicitudes [" + new Date().toString() + "]");
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                myLogger.error("updateSolicitudes", e);
            }
        }
    }

    public void updateCiclosGrupales() {
        myLogger.debug("Inicia actualizcaion de grupos [" + new Date().toString() + "]");
        String drop = "DROP TABLE IF EXISTS CONTRATOS";
        String create = "CREATE TABLE CONTRATOS AS SELECT CI_CONTRATO LD FROM D_CICLOS_GRUPALES WHERE CI_CONTRATO IS NOT NULL";
        String update = "UPDATE D_GRUPOS, D_SALDOS_T24, D_CICLOS_GRUPALES C LEFT JOIN D_REFERENCIAS_GENERALES "
                + "ON RG_NUMCLIENTE = CI_NUMGRUPO AND RG_NUMSOLICITUD = CI_NUMCICLO SET CI_CONTRATO = ST_CONTRATO, "
                + "RG_CONTRATO = ST_CONTRATO WHERE (C.CI_CONTRATO IS NULL OR RG_CONTRATO IS NULL) AND C.CI_ESTATUS = 1 "
                + "AND GR_NUMGRUPO = C.CI_NUMGRUPO AND ST_RFC = GR_RFC AND ST_CONTRATO NOT IN (SELECT LD FROM CONTRATOS)";
        String delete = "DROP TABLE CONTRATOS";
        Connection con = null;
        try {
            con = getConnection();
            int resultado = 0;
            Statement stmt = con.createStatement();
            myLogger.debug("Ejecutando = " + drop);
            stmt.executeUpdate(drop);
            myLogger.debug("Ejecutando = " + create);
            stmt.executeUpdate(create);
            myLogger.debug("Ejecutando = " + update);
            resultado = stmt.executeUpdate(update);
            myLogger.debug("Registros actualizados = " + resultado);
            myLogger.debug("Ejecutando = " + delete);
            stmt.executeUpdate(delete);
            myLogger.debug("Finaliza actualizacion de grupos [" + new Date().toString() + "]");
        } catch (NamingException exc) {
            myLogger.error("updateCiclosGrupales", exc);
        } catch (SQLException exc) {
            myLogger.error("updateCiclosGrupales", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                myLogger.error("updateCiclosGrupales", e);
            }
        }
    }

    public ReferenciaGeneralVO getReferenciaGeneral(String referencia) {
        String query = "SELECT * FROM D_REFERENCIAS_GENERALES WHERE RG_REFERENCIA = ?";
        ReferenciaGeneralVO refGralVO = null;
        Connection con = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                refGralVO = new ReferenciaGeneralVO();
                refGralVO.contrato = rs.getString("rg_contrato");
                refGralVO.esT24 = rs.getInt("rg_es_t24");
                refGralVO.fechaInicio = rs.getDate("rg_fecha_inicio");
                refGralVO.nombre = rs.getString("rg_nombre");
                refGralVO.numcliente = rs.getInt("rg_numcliente");
                refGralVO.numSolicitud = rs.getInt("rg_numsolicitud");
                refGralVO.numSucursal = rs.getInt("rg_numsucursal");
                refGralVO.producto = rs.getInt("rg_producto");
                refGralVO.referencia = rs.getString("rg_referencia");
            }
        } catch (NamingException exc) {
            myLogger.error("getReferenciaGeneral", exc);
        } catch (SQLException exc) {
            myLogger.error("getReferenciaGeneral", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                myLogger.error("getReferenciaGeneral", e);
            }
        }
        return refGralVO;
    }
     public ReferenciaGeneralVO getReferenciaGeneral(String referencia,Connection con) {
        String query = "SELECT * FROM D_REFERENCIAS_GENERALES WHERE RG_REFERENCIA = ?";
        ReferenciaGeneralVO refGralVO = null;
        int param = 1;
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                refGralVO = new ReferenciaGeneralVO();
                refGralVO.contrato = rs.getString("rg_contrato");
                refGralVO.esT24 = rs.getInt("rg_es_t24");
                refGralVO.fechaInicio = rs.getDate("rg_fecha_inicio");
                refGralVO.nombre = rs.getString("rg_nombre");
                refGralVO.numcliente = rs.getInt("rg_numcliente");
                refGralVO.numSolicitud = rs.getInt("rg_numsolicitud");
                refGralVO.numSucursal = rs.getInt("rg_numsucursal");
                refGralVO.producto = rs.getInt("rg_producto");
                refGralVO.referencia = rs.getString("rg_referencia");
            }
        } catch (SQLException exc) {
            myLogger.error("getReferenciaGeneral", exc);
        } 
        return refGralVO;
    }
     
    public ReferenciaGeneralVO getReferenciaGeneralbyNumCliente(int numcliente) {
        String query = "SELECT * FROM D_REFERENCIAS_GENERALES WHERE RG_NUMCLIENTE = ?";
        ReferenciaGeneralVO refGralVO = null;
        Connection con = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(param++, numcliente);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                refGralVO = new ReferenciaGeneralVO();
                refGralVO.contrato = rs.getString("rg_contrato");
                refGralVO.esT24 = rs.getInt("rg_es_t24");
                refGralVO.fechaInicio = rs.getDate("rg_fecha_inicio");
                refGralVO.nombre = rs.getString("rg_nombre");
                refGralVO.numcliente = rs.getInt("rg_numcliente");
                refGralVO.numSolicitud = rs.getInt("rg_numsolicitud");
                refGralVO.numSucursal = rs.getInt("rg_numsucursal");
                refGralVO.producto = rs.getInt("rg_producto");
                refGralVO.referencia = rs.getString("rg_referencia");
            }
        } catch (NamingException exc) {
            myLogger.error("getReferenciaGeneralbyNumCliente", exc);
        } catch (SQLException exc) {
            myLogger.error("getReferenciaGeneralbyNumCliente", exc);
        } finally {

            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                myLogger.error("getReferenciaGeneralbyNumCliente", e);
            }
        }
        return refGralVO;
    }

    public String getReferencia(int idCliente, int idSolicitud, char tipo) throws ClientesException {

        String queryIndividuo = "SELECT * FROM D_REFERENCIAS_GENERALES WHERE RG_NUMCLIENTE = ? "
                + "AND RG_NUMSOLICITUD = ? AND SUBSTR(RG_REFERENCIA, 4, 1) != 9";
        String queryGrupo = "SELECT * FROM D_REFERENCIAS_GENERALES WHERE RG_NUMCLIENTE = ? "
                + "AND RG_NUMSOLICITUD = ? AND SUBSTR(RG_REFERENCIA, 4, 1) = 9";
        Connection cn = null;
        ReferenciaGeneralVO referencia = new ReferenciaGeneralVO();
        try {
            String query = null;
            if (tipo == 'I') {
                query = queryIndividuo;
            } else {
                query = queryGrupo;
            }
            cn = getConnection();

            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Para cliente : " + idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                referencia = new ReferenciaGeneralVO();
                referencia.contrato = rs.getString("RG_CONTRATO");
                referencia.numcliente = rs.getInt("RG_NUMCLIENTE");
                referencia.nombre = rs.getString("RG_NOMBRE");
                referencia.referencia = rs.getString("RG_REFERENCIA");
                referencia.fechaInicio = rs.getDate("RG_FECHA_INICIO");
                referencia.esT24 = rs.getInt("RG_ES_T24");
                referencia.producto = rs.getInt("RG_PRODUCTO");
                referencia.numSolicitud = rs.getInt("RG_NUMSOLICITUD");
                referencia.numSucursal = rs.getInt("RG_NUMSUCURSAL");
                myLogger.debug("Referencia encontrada : " + referencia.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getReferencia", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getReferencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getReferencia", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return referencia.referencia;

    }

    public int updateReferencia(Connection conn, String referenciaActual, String nuevaReferencia) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_REFERENCIAS_GENERALES SET RG_REFERENCIA = ? WHERE RG_REFERENCIA = ?";
        Connection cn = null;
        try {
            PreparedStatement ps = null;
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            int param = 1;
            ps.setString(param++, nuevaReferencia);
            ps.setString(param++, referenciaActual);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateReferencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("updateReferencia", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int updateReferenciaApertura(String referenciaActual, String nuevaReferencia) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_REFERENCIAS_GENERALES SET RG_REFERENCIA = ? WHERE RG_REFERENCIA = ?";
        Connection cn = null;
        try {
            PreparedStatement ps = null;
            //if( conn==null ){
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*}
             else{
             ps = conn.prepareStatement(query);
             }*/
            int param = 1;
            ps.setString(param++, nuevaReferencia);
            ps.setString(param++, referenciaActual);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateReferenciaApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateReferenciaApertura", e);
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

    public int deleteReferencia(CicloGrupalVO ciclo) throws ClientesException {

        String query = "DELETE FROM D_REFERENCIAS_GENERALES WHERE RG_NUMCLIENTE = ? AND RG_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteReferencia", e);
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

    public void deleteReferencia(int idEquipo, int idCiclo) throws ClientesException {

        String query = "DELETE FROM d_referencias_generales WHERE rg_numcliente = ? AND rg_numsolicitud = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteReferencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    
    /**
     * JECB 24/11/2017
     * Método que obtiene el registro de la tabla de referencia general por su grupo y su ciclo
     * @param grupo valor del grupo
     * @param ciclo valor del ciclo
     * @return bean ReferenciaGeneralVO con el registro encontrado, null en caso de no existir
     */
    public ReferenciaGeneralVO getReferenciaGeneralByGrupoYCiclo(String grupo, String ciclo) {
        String query = "select *  from d_referencias_generales where rg_numcliente = "+Integer.parseInt(grupo) + 
                " and rg_numsolicitud = " + Integer.parseInt(ciclo) + " and "
                + "SUBSTR(rg_referencia, 5, 6) = '"+grupo+"' and SUBSTR(rg_referencia, 11, 2) = '"+ciclo+"'";
        
        
        ReferenciaGeneralVO refGralVO = null;
        Connection con = null;
        int param = 1;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                refGralVO = new ReferenciaGeneralVO();
                refGralVO.contrato = rs.getString("rg_contrato");
                refGralVO.esT24 = rs.getInt("rg_es_t24");
                refGralVO.fechaInicio = rs.getDate("rg_fecha_inicio");
                refGralVO.nombre = rs.getString("rg_nombre");
                refGralVO.numcliente = rs.getInt("rg_numcliente");
                refGralVO.numSolicitud = rs.getInt("rg_numsolicitud");
                refGralVO.numSucursal = rs.getInt("rg_numsucursal");
                refGralVO.producto = rs.getInt("rg_producto");
                refGralVO.referencia = rs.getString("rg_referencia");
            }
        } catch (NamingException exc) {
            myLogger.error("getReferenciaGeneralByGrupoYCiclo", exc);
        } catch (SQLException exc) {
            myLogger.error("getReferenciaGeneralByGrupoYCiclo", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                myLogger.error("getReferenciaGeneralByGrupoYCiclo", e);
            }
        }
        return refGralVO;
    }
}

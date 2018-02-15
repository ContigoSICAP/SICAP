package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.RenovacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RenovacionDAO extends DAOMaster{
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(RenovacionDAO.class);
    
    public void insertaPlaneacion(RenovacionVO renovacion) throws ClientesException{
        String query = "INSERT INTO d_equipos_no_renovados (enr_numgrupo, enr_numciclo, enr_numejecutivo, enr_numintegrantes, enr_fecha_vencimiento, enr_motivo)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        PreparedStatement ps = null;
            try {
                cn = getConnection();
                ps = cn.prepareStatement(query);
                ps.setInt(1, renovacion.getIdEquipo());
                ps.setInt(2, renovacion.getIdCiclo());
                ps.setInt(3, renovacion.getIdAsesor());
                ps.setInt(4, renovacion.getNumIntegrantes());
                ps.setDate(5, Convertidor.toSqlDate(renovacion.getFechaVencimiento()));
                ps.setInt(6, renovacion.getIdMotivo());
                myLogger.debug("Ejecutando: "+ ps.toString());
                ps.executeUpdate();
            } catch (SQLException sqle) {
                myLogger.error("insertaPlaneacion", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("insertaPlaneacion", e);
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
    
    public RenovacionVO getRenovacion (int idEquipo, int idCiclo) throws ClientesException{
        String query = "SELECT * FROM d_equipos_no_renovados WHERE enr_numgrupo = ? AND enr_numciclo = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RenovacionVO renovacion = new RenovacionVO();
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: "+ ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                renovacion.setIdEquipo(rs.getInt("enr_numgrupo"));
                renovacion.setIdCiclo(rs.getInt("enr_numciclo"));
                renovacion.setIdAsesor(rs.getInt("enr_numejecutivo"));
                renovacion.setNumIntegrantes(rs.getInt("enr_numintegrantes"));
                renovacion.setFechaVencimiento(rs.getDate("enr_fecha_vencimiento"));
                renovacion.setIdMotivo(rs.getInt("enr_motivo"));
            } else {
                renovacion = null;
            }
        } catch (SQLException sqle) {
                myLogger.error("getRenovacion", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getRenovacion", e);
                throw new ClientesException(e.getMessage());
            } finally {
                try {
                    if(cn != null){
                    cn.close();
                    }
                    if (ps != null){
                        ps.close();
                    }
                    if (rs != null){
                        rs.close();
                    }
                } catch (SQLException sqle){
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
        return renovacion;
    }
    
    public ArrayList<GrupoVO> getEquiposNoRenovados(int idSucursal, int idYear, int idMonth) throws ClientesException{
            ArrayList<GrupoVO> registros = new ArrayList<GrupoVO>();
            String query = "SELECT COUNT(ib_numclientesicap) integrantes, ib_numclientesicap, gr_nombre, gr_calificacion, ib_numsolicitudsicap, ib_fecha_vencimiento, e.ci_ejecutivo "+
                    "FROM d_saldos a, (SELECT ci_numgrupo, MAX(ci_numciclo) ciclo, ci_ejecutivo FROM d_ciclos_grupales "+
                    "WHERE ci_estatus<3 AND ci_origenmigracion = 0 GROUP BY ci_numgrupo) b, d_grupos c, d_integrantes_ciclo d, d_ciclos_grupales e "+
                    "WHERE a.ib_numclientesicap = b.ci_numgrupo AND a.ib_numsolicitudsicap = ciclo  AND a.ib_numclientesicap = c.gr_numgrupo "+
                    "AND ib_numclientesicap = ic_numgrupo AND a.ib_numclientesicap = e.ci_numgrupo AND ciclo = e.ci_numciclo AND ciclo = ic_numciclo AND ib_numsucursal = ? "+
                    "AND YEAR(a.ib_fecha_vencimiento) = ? AND MONTH(a.ib_fecha_vencimiento) = ? AND a.ib_estatus=3 GROUP BY ib_numclientesicap";
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                cn= getConnection();
                ps=cn.prepareStatement(query);
                ps.setInt(1, idSucursal);
                ps.setInt(2, idYear);
                ps.setInt(3, idMonth);
                myLogger.debug("Ejecutando: "+ ps.toString());
                rs = ps.executeQuery();
                while(rs.next()){
                    GrupoVO equipo = new GrupoVO();
                    CicloGrupalVO ciclo = new CicloGrupalVO();
                    CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                    equipo.idGrupo = rs.getInt("ib_numclientesicap");
                    equipo.nombre = rs.getString("gr_nombre");
                    equipo.calificacion = rs.getString("gr_calificacion");
                    equipo.idCicloOriginal = rs.getInt("ib_numsolicitudsicap");
                    ciclo.numIntegrantes = rs.getInt("integrantes");
                    ciclo.fechaDispersion = rs.getDate("ib_fecha_vencimiento");
                    ciclo.asesor = rs.getInt("e.ci_ejecutivo");
                    ciclos[0]=ciclo;
                    equipo.ciclos=ciclos;
                    registros.add(equipo);
                }
            } catch (SQLException sqle) {
                myLogger.error("getEquiposNoRenovados", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getEquiposNoRenovados", e);
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
                    myLogger.error("getPlaneacionRenovacion", sqle);
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
            return registros;
        }
    
    public void eliminaPlaneacion(int idEquipo, int idCiclo) throws ClientesException{
        String query = "DELETE FROM d_equipos_no_renovados WHERE enr_numgrupo = ? AND enr_numciclo = ?";
        Connection cn = null;
        PreparedStatement ps = null;
            try {
                cn = getConnection();
                ps = cn.prepareStatement(query);
                ps.setInt(1, idEquipo);
                ps.setInt(2, idCiclo);
                myLogger.debug("Ejecutando: "+ ps.toString());
                ps.executeUpdate();
            } catch (SQLException sqle) {
                myLogger.error("eliminaPlaneacion", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("eliminaPlaneacion", e);
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
    
    public boolean esNoRenovado(int idEquipo, int idCiclo) throws ClientesException{
        String query = "SELECT * FROM d_equipos_no_renovados WHERE enr_numgrupo = ? AND enr_numciclo = ?";
        boolean respuesta = false;
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
                respuesta=true;
            }
        } catch (SQLException sqle) {
            myLogger.error("esNoRenovado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch(Exception e) {
            myLogger.error("esNoRenovado", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if(cn != null){
                    cn.close();
                }
                if (ps != null){
                    ps.close();
                }
                if (rs != null){
                    rs.close();
                }
            } catch (SQLException sqle){
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return respuesta;
   }
}

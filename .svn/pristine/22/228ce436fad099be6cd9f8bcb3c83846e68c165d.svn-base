package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.PlaneacionRenovacionVO;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;


public class PlaneacionRenovacionDAO extends DAOMaster{
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(PlaneacionRenovacionDAO.class);

	public int addPlaneacion( PlaneacionRenovacionVO planeacion ) throws ClientesException{

		String query =	"INSERT INTO D_PLANEACION_RENOVACION (PE_NUMEJECUTIVO, PE_NUMSUCURSAL, PE_NUMGRUPO, PE_NUMCICLO, PE_INTEGRANTES, PE_INTEGRANTES_TOTAL, PE_FECHA_VENCIMIENTO, PE_RENUEVA, PE_MOTIVO) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int param = 1;
		int res = 0;
		PreparedStatement ps = null;
		Connection cn = null;

		try{
			cn = getConnection();
			ps = cn.prepareStatement(query);

			ps.setInt(param++, planeacion.numEjecutivo);
			ps.setInt(param++, planeacion.numSucursal);
			ps.setInt(param++, planeacion.numGrupo);
			ps.setInt(param++, planeacion.numCiclo);
			ps.setInt(param++, planeacion.integrantes);
			ps.setInt(param++, planeacion.integrantesTotal);
			ps.setDate(param++, Convertidor.toSqlDate(planeacion.fechaVencimiento));
			ps.setInt(param++, planeacion.renueva);
			ps.setInt(param++, planeacion.numMotivo);
			
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addPlaneacion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addPlaneacion : "+e.getMessage());
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
        
        public void eliminaPlaneacion(int idAsesor) throws ClientesException{
            String query = "DELETE FROM d_planeacion_renovacion WHERE pe_numejecutivo = ?";
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                cn = getConnection();
                ps = cn.prepareStatement(query);
                ps.setInt(1, idAsesor);
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
        
        public ArrayList<PlaneacionRenovacionVO> getPlaneacionRenovacion(int idAsesor, int idYear, int idMonth) throws ClientesException{
            ArrayList<PlaneacionRenovacionVO> registrosPlaneacion = new ArrayList<PlaneacionRenovacionVO>();
            String query = "SELECT * FROM d_planeacion_renovacion WHERE pe_numejecutivo = ? AND YEAR(pe_fecha_vencimiento)=? AND MONTH(pe_fecha_vencimiento)=? AND pe_renueva=1";
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                cn= getConnection();
                ps=cn.prepareStatement(query);
                ps.setInt(1, idAsesor);
                ps.setInt(2, idYear);
                ps.setInt(3, idMonth);
                myLogger.debug("Ejecutando: "+ ps.toString());
                rs = ps.executeQuery();
                while(rs.next()){
                    PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
                    planeacion.numEjecutivo = rs.getInt("pe_numejecutivo");
                    planeacion.numSucursal = rs.getInt("pe_numsucursal");
                    planeacion.numGrupo = rs.getInt("pe_numgrupo");
                    planeacion.numCiclo = rs.getInt("pe_numciclo");
                    planeacion.integrantes = rs.getInt("pe_integrantes");
                    planeacion.integrantesTotal = rs.getInt("pe_integrantes_total");
                    planeacion.fechaVencimiento = rs.getDate("pe_fecha_vencimiento");
                    planeacion.renueva = rs.getInt("pe_renueva");
                    planeacion.numMotivo = rs.getInt("pe_motivo");
                    registrosPlaneacion.add(planeacion);
                }
            } catch (SQLException sqle) {
                myLogger.error("getPlaneacionRenovacion", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getPlaneacionRenovacion", e);
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
            return registrosPlaneacion;
        }
        
        public ArrayList<GrupoVO> getEquiposPlaneados(int idAsesor, int idYear, int idMonth) throws ClientesException{
            ArrayList<GrupoVO> registrosPlaneados = new ArrayList<GrupoVO>();
            String query = "SELECT pe_numgrupo, pe_numciclo, gr_nombre, gr_calificacion FROM d_planeacion_renovacion LEFT JOIN d_grupos ON(pe_numgrupo=gr_numgrupo) " +
                    "WHERE pe_numejecutivo = ? AND YEAR(pe_fecha_vencimiento) = ? AND MONTH(pe_fecha_vencimiento) = ? AND pe_renueva = 1";
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                cn= getConnection();
                ps=cn.prepareStatement(query);
                ps.setInt(1, idAsesor);
                ps.setInt(2, idYear);
                ps.setInt(3, idMonth);
                myLogger.debug("Ejecutando: "+ ps.toString());
                rs = ps.executeQuery();
                while(rs.next()){
                    GrupoVO equipo = new GrupoVO();
                    equipo.idGrupo = rs.getInt("pe_numgrupo");
                    equipo.nombre = rs.getString("gr_nombre");
                    equipo.calificacion = rs.getString("gr_calificacion");
                    equipo.idCicloOriginal = rs.getInt("pe_numciclo");
                    registrosPlaneados.add(equipo);
                }
            } catch (SQLException sqle) {
                myLogger.error("getEquiposPlaneados", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getEquiposPlaneados", e);
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
            return registrosPlaneados;
        }
        
        public ArrayList<GrupoVO> getEquiposTotales(int idAsesor, int idYear, int idMonth) throws ClientesException{
            ArrayList<GrupoVO> registrosPlaneados = new ArrayList<GrupoVO>();
            String query = "SELECT COUNT(ib_numclientesicap) numIntegrantes, ib_numclientesicap, ib_numsolicitudsicap, gr_nombre, gr_calificacion, ib_fecha_desembolso "+
                    "FROM d_saldos LEFT JOIN d_grupos ON(ib_numclientesicap=gr_numgrupo) " +
                    "LEFT JOIN d_ciclos_grupales ON(ib_numclientesicap=ci_numgrupo AND ib_numsolicitudsicap=ci_numciclo) " +
                    "LEFT JOIN d_integrantes_ciclo ON(ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo ) " +
                    "WHERE ci_ejecutivo = ? AND YEAR(ib_fecha_desembolso) = ? AND MONTH(ib_fecha_desembolso) = ? GROUP BY ib_numclientesicap";
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                cn= getConnection();
                ps=cn.prepareStatement(query);
                ps.setInt(1, idAsesor);
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
                    ciclo.numIntegrantes = rs.getInt("numIntegrantes");
                    ciclo.fechaDispersion = rs.getDate("ib_fecha_desembolso");
                    ciclos[0]=ciclo;
                    equipo.ciclos=ciclos;
                    registrosPlaneados.add(equipo);
                }
            } catch (SQLException sqle) {
                myLogger.error("getEquiposTotales", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getEquiposTotales", e);
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
            return registrosPlaneados;
        }
        
        public PlaneacionRenovacionVO getPlaneacionEquipo (int idEquipo, int idCiclo) throws ClientesException{
            PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
            String query = "SELECT * FROM d_planeacion_renovacion WHERE pe_numgrupo = ? AND pe_numciclo = ?";
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                cn= getConnection();
                ps=cn.prepareStatement(query);
                ps.setInt(1, idEquipo);
                ps.setInt(2, idCiclo);
                myLogger.debug("Ejecutando: "+ ps.toString());
                rs = ps.executeQuery();
                if(rs.next()){                    
                    planeacion.numEjecutivo = rs.getInt("pe_numejecutivo");
                    planeacion.numSucursal = rs.getInt("pe_numsucursal");
                    planeacion.numGrupo = rs.getInt("pe_numgrupo");
                    planeacion.numCiclo = rs.getInt("pe_numciclo");
                    planeacion.integrantes = rs.getInt("pe_integrantes");
                    planeacion.integrantesTotal = rs.getInt("pe_integrantes_total");
                    planeacion.fechaVencimiento = rs.getDate("pe_fecha_vencimiento");
                    planeacion.renueva = rs.getInt("pe_renueva");
                    planeacion.numMotivo = rs.getInt("pe_motivo");
                }
            } catch (SQLException sqle) {
                myLogger.error("getPlaneacionEquipo", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("getPlaneacionEquipo", e);
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
                    myLogger.error("getPlaneacionEquipo", sqle);
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
            return planeacion;
        }

}
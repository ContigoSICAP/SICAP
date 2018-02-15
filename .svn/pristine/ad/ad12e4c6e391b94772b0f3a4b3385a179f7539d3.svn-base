package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import org.apache.log4j.Logger;

public class GrupoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(GrupoDAO.class);

    public GrupoVO[] getGruposSucursal(int idSucursal) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS WHERE GR_NUMSUCURSAL = ? ORDER BY GR_NOMBRE";
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro ["+idSucursal+"]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.otraFinaciera = rs.getInt ("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposSucursal",e);
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
        return elementos;

    }

    
    public GrupoVO[] getGruposEjecutivo(int idEjecutivo) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS WHERE GR_NUMSUCURSAL = ? ORDER BY GR_NOMBRE";
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idEjecutivo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro ["+idEjecutivo+"]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.otraFinaciera = rs.getInt("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposEjecutivo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposEjecutivo",e);
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
        return elementos;

    }

    public GrupoVO[] getGruposSucursalConDetalle(int idSucursal) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS "
                + " LEFT JOIN D_MONITOR_PAGOS_GRUPAL "
                + " ON(MO_NUMGRUPO=GR_NUMGRUPO AND (MO_ESTATUS_VISITA_GERENTE=1 OR MO_ESTATUS_VISITA_SUPERVISOR=1)) "
                + " WHERE GR_NUMSUCURSAL = ? ORDER BY GR_NOMBRE ";
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.monitorPagos.identificador = rs.getInt("MO_ID");
                temporal.monitorPagos.numGrupo = temporal.idGrupo;
                temporal.monitorPagos.numCiclo = rs.getInt("MO_NUMCICLO");
                temporal.monitorPagos.numPago = rs.getInt("MO_NUMPAGO");
                temporal.monitorPagos.numAtrasos = rs.getInt("MO_NUMERO_ATRASOS");
                temporal.monitorPagos.estatusVisitaSupervisor = rs.getInt("MO_ESTATUS_VISITA_SUPERVISOR");
                temporal.monitorPagos.estatusVisitaGerente = rs.getInt("MO_ESTATUS_VISITA_GERENTE");
                temporal.monitorPagos.enMora = rs.getString("MO_EN_MORA");
                temporal.otraFinaciera= rs.getInt("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposSucursalConDetalle",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposSucursalConDetalle",e);
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
        return elementos;

    }

    public GrupoVO[] getGrupos() throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS";
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.otraFinaciera = rs.getInt("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGrupos",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGrupos",e);
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
        return elementos;

    }

    public GrupoVO getGrupoUltimoCicloCliente(int idCliente) throws ClientesException {

        String query = "SELECT MAX(IC_NUMCICLO),D_GRUPOS.*, SU_TIPO_SUCURSAL  FROM D_GRUPOS JOIN C_SUCURSALES ON(SU_NUMSUCURSAL=GR_NUMSUCURSAL), "
                + "D_INTEGRANTES_CICLO WHERE GR_NUMGRUPO=IC_NUMGRUPO AND IC_NUMCLIENTE=? GROUP BY(GR_NUMGRUPO) ";
        GrupoVO grupo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                grupo = new GrupoVO();
                grupo.idGrupo = rs.getInt("GR_NUMGRUPO");
                grupo.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                grupo.rfc = rs.getString("GR_RFC");
                grupo.sucursal = rs.getInt("GR_NUMSUCURSAL");
                grupo.estatus = rs.getInt("GR_ESTATUS");
                grupo.nombre = rs.getString("GR_NOMBRE");
                grupo.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                grupo.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                grupo.idOperacion = rs.getInt("GR_NUMOPERACION");
                grupo.calificacion = rs.getString("GR_CALIFICACION");
                grupo.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                //Logger.debug("Grupo encontrado : "+grupo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getGrupoUltimoCicloCliente",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGrupoUltimoCicloCliente",e);
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
        return grupo;

    }

    public GrupoVO getGrupo(int idGrupo) throws ClientesException {

        String query = "SELECT GR_NUMGRUPO, GR_NUMGRUPO_IBS, GR_RFC, GR_NUMSUCURSAL, GR_ESTATUS, GR_NOMBRE, GR_FECHA_FORMACION, GR_FECHA_CAPTURA, GR_NUMOPERACION, "
                + "GR_CALIFICACION, GR_NUMGRUPO_ORIGINAL, GR_NUMCICLO_ORIGINAL, SU_TIPO_SUCURSAL, gr_otraFinanciera FROM D_GRUPOS JOIN C_SUCURSALES ON(SU_NUMSUCURSAL=GR_NUMSUCURSAL) WHERE GR_NUMGRUPO = ?";
        GrupoVO grupo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                grupo = new GrupoVO();
                grupo.idGrupo = rs.getInt("GR_NUMGRUPO");
                grupo.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                grupo.rfc = rs.getString("GR_RFC");
                grupo.sucursal = rs.getInt("GR_NUMSUCURSAL");
                grupo.estatus = rs.getInt("GR_ESTATUS");
                grupo.nombre = rs.getString("GR_NOMBRE");
                grupo.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                grupo.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                grupo.idOperacion = rs.getInt("GR_NUMOPERACION");
                grupo.calificacion = rs.getString("GR_CALIFICACION");
                grupo.idGrupoOriginal = rs.getInt("GR_NUMGRUPO_ORIGINAL");
                grupo.idCicloOriginal = rs.getInt("GR_NUMCICLO_ORIGINAL");
                grupo.tipoSucursal = rs.getInt("SU_TIPO_SUCURSAL");
                grupo.otraFinaciera = rs.getInt("gr_otraFinanciera");
                myLogger.debug("Grupo encontrado : " + grupo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.debug("getGrupo",e);
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
        return grupo;

    }

    public GrupoVO getGrupo(int idGrupo, String sucursales) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS WHERE GR_NUMGRUPO = ? AND GR_NUMSUCURSAL IN" + sucursales;
        GrupoVO grupo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                grupo = new GrupoVO();
                grupo.idGrupo = rs.getInt("GR_NUMGRUPO");
                grupo.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                grupo.rfc = rs.getString("GR_RFC");
                grupo.sucursal = rs.getInt("GR_NUMSUCURSAL");
                grupo.estatus = rs.getInt("GR_ESTATUS");
                grupo.nombre = rs.getString("GR_NOMBRE");
                grupo.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                grupo.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                grupo.idOperacion = rs.getInt("GR_NUMOPERACION");
                grupo.calificacion = rs.getString("GR_CALIFICACION");
                grupo.otraFinaciera = rs.getInt("gr_otraFinanciera");
                myLogger.debug("Grupo encontrado : " + grupo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGrupo",e);
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
        return grupo;

    }

    public int addGrupo(GrupoVO grupo) throws ClientesException {

        String query = "INSERT INTO D_GRUPOS (GR_NUMGRUPO, GR_RFC, GR_NUMSUCURSAL, GR_ESTATUS, GR_NOMBRE, GR_NUMOPERACION, GR_CALIFICACION, GR_NUMGRUPO_ORIGINAL, GR_NUMCICLO_ORIGINAL, GR_NIVEL_RESTRUCTURA, GR_FECHA_FORMACION, GR_FECHA_CAPTURA,gr_otraFinanciera) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP,?)";

        Connection cn = null;
        PreparedStatement ps = null;
        grupo.idGrupo = getNext();
        int param = 1;
        int res = 0;
        try {
            grupo.rfc = GrupoUtil.getMnemonicoGrupo(grupo);
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, grupo.idGrupo);
            ps.setString(param++, grupo.rfc);
            ps.setInt(param++, grupo.sucursal);
            ps.setInt(param++, grupo.estatus);
            ps.setString(param++, grupo.nombre);
            ps.setInt(param++, grupo.idOperacion);
            ps.setString(param++, grupo.calificacion);
            ps.setInt(param++, grupo.idGrupoOriginal);
            ps.setInt(param++, grupo.idCicloOriginal);
            ps.setString(param++, grupo.nivelRestructura);
            ps.setDate(param++, grupo.fechaFormacion);
            ps.setInt(param++, grupo.otraFinaciera);            
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Grupo= " + grupo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addGrupo",e);
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

    public int updateGrupo(GrupoVO grupo) throws ClientesException {

        String query = "UPDATE D_GRUPOS SET GR_NOMBRE = ?, GR_NUMSUCURSAL = ?, GR_ESTATUS = ?, GR_FECHA_FORMACION = ?, GR_NUMOPERACION = ?, "
                + "GR_CALIFICACION = ?, gr_otraFinanciera = ? WHERE GR_NUMGRUPO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, grupo.nombre);
            ps.setInt(param++, grupo.sucursal);
            ps.setInt(param++, grupo.estatus);
            ps.setDate(param++, grupo.fechaFormacion);
            ps.setInt(param++, grupo.idOperacion);
            ps.setString(param++, grupo.calificacion);
            ps.setInt(param++, grupo.otraFinaciera);
            ps.setInt(param++, grupo.idGrupo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Grupo = " + grupo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateGrupo",e);
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
    public int updateCalificacionGrupo(int multas) throws ClientesException {

        String query = "Update" +
                " (Select ta_numcliente, ta_numcredito, count(ta_multa)as Nomultas" +
                "      from cartera_cec.d_tabla_amortizacion" +
                "      where  ta_multa >0" +
                "      group by ta_numcliente, ta_numcredito)" +
                "      as T1," +
                "      (Select ib_numclientesicap, max(ib_credito )as ib_credito " +
                "       from d_saldos" +
                "       group by ib_numclientesicap) as t2,clientes_cec.d_grupos" +
                " Set gr_calificacion = 'B'" +
                " where ta_numcliente=ib_numclientesicap" +
                " and ta_numcredito= ib_credito" +
                " and ta_numcliente = gr_numgrupo" +
                " and NoMultas >=?" +
                " and gr_calificacion !='B'";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, multas);            
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateCalificacionGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCalificacionGrupo",e);
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


    public int updateCalificacionGrupo(String calificacion, int idGrupo) throws ClientesException {
        return updateCalificacionGrupo(null, calificacion, idGrupo);
    }

    public int updateCalificacionGrupo(Connection conn, String calificacion, int idGrupo) throws ClientesException {

        String query = "UPDATE D_GRUPOS SET GR_CALIFICACION = ? WHERE GR_NUMGRUPO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setString(param++, calificacion);
            ps.setInt(param++, idGrupo);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Grupo aCTUALIZADO = " + idGrupo);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateCalificacionGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCalificacionGrupo",e);
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

    public int getNext() throws ClientesException {

        String query = "SELECT COALESCE(MAX(GR_NUMGRUPO),0)+1 AS NEXT FROM D_GRUPOS";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
        } catch (SQLException sqle) {
            myLogger.error("getNext",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNext",e);
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
        return next;
    }
    
    public int getNumBanco(int numSucursal) throws ClientesException {

        String query = "SELECT su_banco FROM c_sucursales WHERE su_numsucursal = ?";
        Connection cn = null;
        int numBanco = 0;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numSucursal);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                numBanco = rs.getInt("su_banco");
            }
        } catch (SQLException sqle) {
            myLogger.error("getNumBanco",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumBanco",e);
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
        return numBanco;
    }

    public boolean exists(GrupoVO grupo) throws ClientesException {

        String query = "SELECT 1 FROM D_GRUPOS WHERE GR_NOMBRE = ? AND GR_NUMSUCURSAL = ?";
        Connection cn = null;
        boolean encontrado = false;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, grupo.nombre);
            ps.setInt(2, grupo.sucursal);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                encontrado = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("getNumBanco",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumBanco",e);
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
        return encontrado;

    }

    public int updateMnemonico(int idGrupo, String rfc) throws ClientesException {

        String query = "UPDATE D_GRUPOS SET GR_RFC = ? WHERE GR_NUMGRUPO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, rfc);
            ps.setInt(param++, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateMnemonico",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateMnemonico",e);
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

    public int updateGrupoIBS(int idGrupoIBS, int idGrupo) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_GRUPOS SET GR_NUMGRUPO_IBS = ? WHERE GR_NUMGRUPO = ?";
        int param = 1;
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, idGrupoIBS);
            ps.setInt(param++, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateGrupoIBS",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateGrupoIBS",e);
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

    public GrupoVO[] getGrupoNombre(String nombreGrupo, String sucursales) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS WHERE  GR_NOMBRE LIKE ? AND GR_NUMSUCURSAL IN" + sucursales;
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, "%" + nombreGrupo + "%");
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.idGrupoIBS = rs.getInt("GR_NUMGRUPO_IBS");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.otraFinaciera = rs.getInt("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGrupoNombre",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGrupoNombre",e);
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
        return elementos;

    }

    public int recalificacionGrupal() throws ClientesException {

        /* Se retiran los querys para cartera win		
        String query1 = "DROP TABLE IF EXISTS D_CICLO_MAXIMO_TEMPORAL ";
        
        String query2 =	"CREATE TABLE D_CICLO_MAXIMO_TEMPORAL AS SELECT GR_NUMGRUPO AS GRUPO, MAX(CICLO) AS CICLO FROM D_GRUPOS, " +
        "CF_CARTERA_DB.INCUMPLIMIENTO WHERE GR_NUMGRUPO=NUM_CLIENTE AND GR_RFC=RFC AND ESTATUS IN (1,2) GROUP BY(1) ";
        
        String query3 = "UPDATE D_GRUPOS A, D_CICLO_MAXIMO_TEMPORAL B, CF_CARTERA_DB.INCUMPLIMIENTO C SET A.GR_CALIFICACION=C.CALIFICACION " +
        "WHERE B.GRUPO=C.NUM_CLIENTE AND B.CICLO=C.CICLO AND A.GR_NUMGRUPO=C.NUM_CLIENTE ";
        
        String query4 = "DROP TABLE D_CICLO_MAXIMO_TEMPORAL ";
         */
        String query5 = "UPDATE D_GRUPOS, D_SALDOS SET GR_CALIFICACION = CASE IB_CUOTAS_VENCIDAS WHEN 0 THEN 'AAA' WHEN 1 THEN 'AA' WHEN 2 THEN 'AA' ELSE 'A' END WHERE IB_PRODUCTO IN (3) AND IB_ESTATUS IN (1,2) AND IB_FECHA_VENCIMIENTO >= CURDATE() AND GR_NUMGRUPO = IB_NUMCLIENTESICAP";

        String query6 = "UPDATE D_GRUPOS, D_SALDOS SET GR_CALIFICACION = 'B' WHERE IB_PRODUCTO IN (3) AND IB_ESTATUS IN (1,2) AND IB_FECHA_VENCIMIENTO < CURDATE() AND IB_TOTAL_VENCIDO > 0 AND GR_NUMGRUPO = IB_NUMCLIENTESICAP";
        /*	
         * 	INSTRUCCION PARA ACTUALIZAR LAS CALIFICACIONES DE LOS GRUPOS CUYO CICLO SE REGISTRO EN IBS
         * 	UPDATE D_GRUPOS, D_SALDOS SET GR_CALIFICACION = CASE IB_CUOTAS_VENCIDAS WHEN 0 THEN 'AAA' WHEN 1 THEN 'AAA' WHEN 2 THEN 'AA' WHEN 3 THEN 'A' ELSE 'B' END WHERE IB_PRODUCTO IN (3) AND IB_ESTATUS IN (1,2) AND GR_NUMGRUPO = IB_NUMCLIENTESICAP
         */

        Connection conn = null;
        PreparedStatement ps = null;
        int res = 0;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            /* Se retiran los querys para CWIN JB OCT-10
            ps = conn.prepareStatement(query1); 
            ps.executeUpdate();
            
            ps = conn.prepareStatement(query2); 
            res = ps.executeUpdate();
            
            ps = conn.prepareStatement(query3); 
            ps.executeUpdate();
            
            ps = conn.prepareStatement(query4); 
            ps.executeUpdate();
             */
            ps = conn.prepareStatement(query5);
            ps.executeUpdate();

            ps = conn.prepareStatement(query6);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException sqle) {
            myLogger.error("recalificacionGrupal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("recalificacionGrupal",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public GrupoVO[] obtieneRestructurasGrupo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT * FROM D_GRUPOS WHERE GR_NUMGRUPO_ORIGINAL = ? AND GR_NUMCICLO_ORIGINAL = ?";
        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
                temporal.rfc = rs.getString("GR_RFC");
                temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
                temporal.estatus = rs.getInt("GR_ESTATUS");
                temporal.nombre = rs.getString("GR_NOMBRE");
                temporal.calificacion = rs.getString("GR_CALIFICACION");
                temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
                temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
                temporal.idOperacion = rs.getInt("GR_NUMOPERACION");
                temporal.nivelRestructura = rs.getString("GR_NIVEL_RESTRUCTURA");
                temporal.idGrupoOriginal = rs.getInt("GR_NUMGRUPO_ORIGINAL");
                temporal.idCicloOriginal = rs.getInt("GR_NUMCICLO_ORIGINAL");
                temporal.otraFinaciera = rs.getInt("gr_otraFinanciera");
                array.add(temporal);
                //Logger.debug("Grupo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("obtieneRestructurasGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("obtieneRestructurasGrupo",e);
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
        return elementos;
    }

    public GrupoVO[] getGruposPorVencer(int idEjecutivo, String dateIni, String dateFin) throws ClientesException {

        String query = "SELECT CG.CI_EJECUTIVO AS EJECUTIVO, GR.GR_NOMBRE AS NOMBRE, GR.GR_NUMGRUPO AS NUMGRUPO, GR.GR_CALIFICACION AS CALIFICACION, "
                + "TA.TA_ID_SOLICITUD AS CICLO, (SELECT COUNT(*) FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = GR.GR_NUMGRUPO AND IC_NUMCICLO = TA.TA_ID_SOLICITUD) "
                + "AS INTEGRANTES, TA.TA_FECHA_PAGO AS FECHA_ULTIMO FROM D_TABLA_AMORTIZACION TA, D_CICLOS_GRUPALES CG, D_GRUPOS GR "
                + "WHERE TA_TPO_AMORTIZACION = 1 AND TA_NO_PAGO = 16 AND TA_FECHA_PAGO BETWEEN ? AND ? AND CG.CI_EJECUTIVO = ? "
                + "AND CG.CI_NUMGRUPO = TA.TA_ID_CLIENTE AND CG.CI_NUMCICLO = TA.TA_ID_SOLICITUD AND GR_NUMGRUPO = TA.TA_ID_CLIENTE ORDER BY CG.CI_EJECUTIVO, "
                + "TA.TA_FECHA_PAGO, GR_NUMGRUPO";

        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, dateIni);
            ps.setString(2, dateFin);
            ps.setInt(3, idEjecutivo);

            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                CicloGrupalVO ciclo = new CicloGrupalVO();
                ciclo.asesor = rs.getInt("EJECUTIVO");
                ciclo.idCiclo = rs.getInt("CICLO");
                ciclo.numIntegrantes = rs.getInt("INTEGRANTES");
                ciclo.fechaUltimoPago = rs.getDate("FECHA_ULTIMO");
                temporal.idGrupo = rs.getInt("NUMGRUPO");
                temporal.nombre = rs.getString("NOMBRE");
                temporal.calificacion = rs.getString("CALIFICACION");
                CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                ciclos[0] = ciclo;
                temporal.ciclos = ciclos;
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposPorVencer",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposPorVencer",e);
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
        return elementos;

    }
    
    public ArrayList<GrupoVO> getGruposRenovacion(int idEjecutivo, String fechaInicial, String fechaFinal) throws ClientesException {

        String query = "SELECT a.ci_ejecutivo AS EJECUTIVO, c.ib_nombrecliente AS NOMBRE, b.ci_numgrupo AS NUMGRUPO, d.gr_calificacion AS CALIFICACION, ciclo AS CICLO, " +
                "(SELECT COUNT(*) FROM d_integrantes_ciclo WHERE ic_numgrupo=b.ci_numgrupo AND ic_numciclo=ciclo) AS INTEGRANTES, c.ib_fecha_vencimiento AS FECHA_ULTIMO " +
                "FROM d_ciclos_grupales a, (SELECT ci_numgrupo, MAX(ci_numciclo) ciclo FROM d_ciclos_grupales GROUP BY ci_numgrupo) b " +
                "LEFT JOIN d_saldos c ON(b.ci_numgrupo=ib_numclientesicap AND ciclo=ib_numsolicitudsicap) " +
                "LEFT JOIN d_grupos d ON(b.ci_numgrupo=gr_numgrupo) " +
                "WHERE a.ci_numgrupo = b.ci_numgrupo AND a.ci_numciclo = ciclo AND c.ib_fecha_vencimiento BETWEEN ? AND ? AND a.ci_ejecutivo= ? " +
                "ORDER BY a.ci_ejecutivo, c.ib_fecha_vencimiento, b.ci_numgrupo;";

        ArrayList<GrupoVO> grupos = new ArrayList<GrupoVO>();
        GrupoVO grupo = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(1, fechaInicial);
            ps.setString(2, fechaFinal);
            ps.setInt(3, idEjecutivo);
            myLogger.debug("Ejecutando = " + ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                grupo = new GrupoVO();
                CicloGrupalVO ciclo = new CicloGrupalVO();
                ciclo.asesor = rs.getInt("EJECUTIVO");
                ciclo.idCiclo = rs.getInt("CICLO");
                ciclo.numIntegrantes = rs.getInt("INTEGRANTES");
                ciclo.fechaUltimoPago = rs.getDate("FECHA_ULTIMO");
                grupo.idGrupo = rs.getInt("NUMGRUPO");
                grupo.nombre = rs.getString("NOMBRE");
                grupo.calificacion = rs.getString("CALIFICACION");
                CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                ciclos[0] = ciclo;
                grupo.ciclos = ciclos;
                grupos.add(grupo);
            }            
        } catch (SQLException sqle) {
            myLogger.error("getGruposRenovacion",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposRenovacion",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                myLogger.error("getGruposRenovacion", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return grupos;
    }

    public GrupoVO[] getGruposMetasEjecutivos(int idEjecutivo, String dateIni, String dateFin) throws ClientesException {

        /*String query = "SELECT CG.CI_EJECUTIVO AS EJECUTIVO, GR.GR_NOMBRE AS NOMBRE, GR.GR_NUMGRUPO AS NUMGRUPO, GR.GR_CALIFICACION AS CALIFICACION, " +
        "TA.TA_ID_SOLICITUD AS CICLO, (SELECT COUNT(*) FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = GR.GR_NUMGRUPO AND IC_NUMCICLO = TA.TA_ID_SOLICITUD) " +
        "AS INTEGRANTES, TA.TA_FECHA_PAGO AS FECHA_ULTIMO FROM D_TABLA_AMORTIZACION TA, D_CICLOS_GRUPALES CG, D_GRUPOS GR, D_PLANEACION_RENOVACION PR " +
        "WHERE TA_TPO_AMORTIZACION = 1 AND TA_NO_PAGO = 16 AND TA_FECHA_PAGO BETWEEN ? AND ? AND CG.CI_EJECUTIVO = ? AND PR.PE_RENUEVA = 1 " +
        "AND PR.PE_NUMGRUPO = CG.CI_NUMGRUPO	AND PR.PE_NUMCICLO = CG.CI_NUMCICLO	AND CG.CI_NUMGRUPO = TA.TA_ID_CLIENTE " +
        "AND CG.CI_NUMCICLO = TA.TA_ID_SOLICITUD	AND GR_NUMGRUPO = TA.TA_ID_CLIENTE	ORDER BY CG.CI_EJECUTIVO, TA.TA_FECHA_PAGO, GR_NUMGRUPO";*/

        String query = "SELECT CG.CI_EJECUTIVO AS EJECUTIVO, GR.GR_NOMBRE AS NOMBRE, GR.GR_NUMGRUPO AS NUMGRUPO, GR.GR_CALIFICACION AS CALIFICACION, "
                + "PR.PE_NUMCICLO AS CICLO, (SELECT COUNT(*) FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = GR.GR_NUMGRUPO AND IC_NUMCICLO = PR.PE_NUMCICLO) AS INTEGRANTES, "
                + "PR.PE_FECHA_VENCIMIENTO AS FECHA_ULTIMO FROM D_CICLOS_GRUPALES CG, D_GRUPOS GR, D_PLANEACION_RENOVACION PR "
                + "WHERE PR.PE_FECHA_VENCIMIENTO BETWEEN ? AND ? AND CG.CI_EJECUTIVO = ? AND PR.PE_RENUEVA = 1 "
                + "AND PR.PE_NUMGRUPO NOT IN (SELECT CI_NUMGRUPO FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = GR.GR_NUMGRUPO AND CI_NUMCICLO = (PR.PE_NUMCICLO + 1) AND CI_DESEMBOLSADO = 3) "
                + "AND PR.PE_NUMGRUPO = CG.CI_NUMGRUPO AND PR.PE_NUMCICLO = CG.CI_NUMCICLO AND PR.PE_NUMGRUPO = GR.GR_NUMGRUPO "
                + "ORDER BY CG.CI_EJECUTIVO, PR.PE_FECHA_VENCIMIENTO, GR_NUMGRUPO";

        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, dateIni);
            ps.setString(2, dateFin);
            ps.setInt(3, idEjecutivo);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                CicloGrupalVO ciclo = new CicloGrupalVO();
                ciclo.asesor = rs.getInt("EJECUTIVO");
                ciclo.idCiclo = rs.getInt("CICLO");
                ciclo.numIntegrantes = rs.getInt("INTEGRANTES");
                ciclo.fechaUltimoPago = rs.getDate("FECHA_ULTIMO");
                temporal.idGrupo = rs.getInt("NUMGRUPO");
                temporal.nombre = rs.getString("NOMBRE");
                temporal.calificacion = rs.getString("CALIFICACION");
                CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                ciclos[0] = ciclo;
                temporal.ciclos = ciclos;
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposMetasEjecutivos",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposMetasEjecutivos",e);
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
        return elementos;

    }

    public GrupoVO[] getGruposMetasEjecutivosSucursal(int idSucursal, String dateIni, String dateFin) throws ClientesException {

        String query = "SELECT CG.CI_EJECUTIVO AS EJECUTIVO, GR.GR_NOMBRE AS NOMBRE, GR.GR_NUMGRUPO AS NUMGRUPO, GR.GR_CALIFICACION AS CALIFICACION, "
                + "PR.PE_NUMCICLO AS CICLO, (SELECT COUNT(*) FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = GR.GR_NUMGRUPO AND IC_NUMCICLO = PR.PE_NUMCICLO) AS INTEGRANTES, "
                + "PR.PE_FECHA_VENCIMIENTO AS FECHA_ULTIMO FROM D_CICLOS_GRUPALES CG, D_GRUPOS GR, D_PLANEACION_RENOVACION PR "
                + "WHERE PR.PE_FECHA_VENCIMIENTO BETWEEN ? AND ? AND PR.PE_NUMSUCURSAL = ? AND PR.PE_RENUEVA = 1 "
                + "AND PR.PE_NUMGRUPO NOT IN (SELECT CI_NUMGRUPO FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = NUMGRUPO AND CI_NUMCICLO = CICLO + 1 AND CI_DESEMBOLSADO = 3) "
                + "AND PR.PE_NUMGRUPO = CG.CI_NUMGRUPO AND PR.PE_NUMCICLO = CG.CI_NUMCICLO AND PR.PE_NUMGRUPO = GR.GR_NUMGRUPO "
                + "ORDER BY CG.CI_EJECUTIVO, PR.PE_FECHA_VENCIMIENTO, GR_NUMGRUPO";

        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, dateIni);
            ps.setString(2, dateFin);
            ps.setInt(3, idSucursal);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                CicloGrupalVO ciclo = new CicloGrupalVO();
                ciclo.asesor = rs.getInt("EJECUTIVO");
                ciclo.idCiclo = rs.getInt("CICLO");
                ciclo.numIntegrantes = rs.getInt("INTEGRANTES");
                ciclo.fechaUltimoPago = rs.getDate("FECHA_ULTIMO");
                temporal.idGrupo = rs.getInt("NUMGRUPO");
                temporal.nombre = rs.getString("NOMBRE");
                temporal.calificacion = rs.getString("CALIFICACION");
                CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                ciclos[0] = ciclo;
                temporal.ciclos = ciclos;
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposMetasEjecutivosSucursal",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposMetasEjecutivosSucursal",e);
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
        return elementos;

    }

    public GrupoVO[] getGruposMetasDesembolsados(int idEjecutivo, String dateIni, String dateFin) throws ClientesException {

        String query = "SELECT CG.CI_EJECUTIVO AS EJECUTIVO, GR.GR_NOMBRE AS NOMBRE, GR.GR_NUMGRUPO AS NUMGRUPO, GR.GR_CALIFICACION AS CALIFICACION, "
                + "TA.TA_ID_SOLICITUD AS CICLO, (SELECT COUNT(*) FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = GR.GR_NUMGRUPO AND IC_NUMCICLO = TA.TA_ID_SOLICITUD) "
                + "AS INTEGRANTES, TA.TA_FECHA_PAGO AS FECHA_DESEMBOLSO FROM D_TABLA_AMORTIZACION TA, D_CICLOS_GRUPALES CG, D_GRUPOS GR "
                + "WHERE TA_TPO_AMORTIZACION = 1 AND TA_NO_PAGO = 0 AND TA_FECHA_PAGO BETWEEN ? AND ? AND CG.CI_EJECUTIVO= ? "
                + "AND CG.CI_NUMGRUPO = TA.TA_ID_CLIENTE AND CG.CI_NUMCICLO = TA.TA_ID_SOLICITUD AND GR_NUMGRUPO = TA.TA_ID_CLIENTE "
                + "ORDER BY CG.CI_EJECUTIVO,TA.TA_FECHA_PAGO, GR_NUMGRUPO";

        ArrayList<GrupoVO> array = new ArrayList<GrupoVO>();
        GrupoVO temporal = null;
        GrupoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, dateIni);
            ps.setString(2, dateFin);
            ps.setInt(3, idEjecutivo);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new GrupoVO();
                CicloGrupalVO ciclo = new CicloGrupalVO();
                ciclo.asesor = rs.getInt("EJECUTIVO");
                ciclo.idCiclo = rs.getInt("CICLO");
                ciclo.numIntegrantes = rs.getInt("INTEGRANTES");
                ciclo.fechaUltimoPago = rs.getDate("FECHA_DESEMBOLSO");
                temporal.idGrupo = rs.getInt("NUMGRUPO");
                temporal.nombre = rs.getString("NOMBRE");
                temporal.calificacion = rs.getString("CALIFICACION");
                CicloGrupalVO ciclos[] = new CicloGrupalVO[1];
                ciclos[0] = ciclo;
                temporal.ciclos = ciclos;
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new GrupoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (GrupoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposMetasDesembolsados",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposMetasDesembolsados",e);
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
        return elementos;

    }

    public GrupoVO getGruposID(int numGrupo) throws ClientesException {

        String query = "SELECT * FROM clientes.d_grupos WHERE gr_numgrupo=?";

        GrupoVO grupo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                grupo = new GrupoVO();
                grupo.idGrupo = rs.getInt("gr_numgrupo");
                grupo.sucursal = rs.getInt("gr_numsucursal");
            }
        } catch (SQLException sqle) {
            myLogger.error("getGruposID",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGruposID",e);
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
        return grupo;
    }
    
    public String getNombreGrupo(int numGrupo) throws ClientesException {
        String query = "SELECT gr_nombre FROM d_grupos WHERE gr_numgrupo=?";
        String nombreGrupo = null;
        Connection cn = null;
        
        try {
            cn=getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numGrupo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametro ["+numGrupo+"]");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nombreGrupo= rs.getString("gr_nombre");
            }
        }
        catch (SQLException sqle) {
            myLogger.error("getNombreGrupo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNombreGrupo",e);
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
        return nombreGrupo;
    }
}
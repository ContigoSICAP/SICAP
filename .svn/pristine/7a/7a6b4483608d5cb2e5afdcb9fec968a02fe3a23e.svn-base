package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import org.apache.log4j.Logger;

public class ArchivoAsociadoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(ArchivoAsociadoDAO.class);

    
    public ArchivoAsociadoVO[] getArchivos(int idCliente, int idSolicitud) throws ClientesException {

        return getArchivos(idCliente, idSolicitud, "I");

    }

    public ArchivoAsociadoVO[] getArchivos(int idGrupo, int idCiclo, String tipoCliente) throws ClientesException {

        String query = "SELECT AS_NUMCLIENTE, AS_NUMSOLICITUD, AS_TIPO_CLIENTE, AS_TIPO, AS_NOMBRE, AS_CONSECUTIVO, AS_FECHA_CAPTURA FROM D_ARCHIVOS_ASOCIADOS WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO_CLIENTE=? ";
        ArrayList<ArchivoAsociadoVO> array = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;
        ArchivoAsociadoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            ps.setString(3, tipoCliente);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = idGrupo;
                temporal.idSolicitud = idCiclo;
                temporal.tipoCliente = rs.getString("AS_TIPO_CLIENTE");
                temporal.tipo = rs.getInt("AS_TIPO");
                temporal.consecutivo = rs.getInt("AS_CONSECUTIVO");
                temporal.nombre = rs.getString("AS_NOMBRE");
                temporal.fechaCaptura = rs.getTimestamp("AS_FECHA_CAPTURA");
                array.add(temporal);
                //myLogger.debug("Archivo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new ArchivoAsociadoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (ArchivoAsociadoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getArchivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getArchivos", e);
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
    public ArchivoAsociadoVO getArchivoIndividual(int idCliente, int idCiclo, String tipoCliente, int tipoArchivo) throws ClientesException {

        String query = "SELECT AS_NUMCLIENTE, AS_NUMSOLICITUD, AS_TIPO_CLIENTE, AS_TIPO, AS_NOMBRE, AS_CONSECUTIVO, AS_FECHA_CAPTURA FROM D_ARCHIVOS_ASOCIADOS WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO_CLIENTE=? AND AS_TIPO  = ?";
        ArrayList<ArchivoAsociadoVO> array = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;
        ArchivoAsociadoVO elemento = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idCiclo);
            ps.setString(3, tipoCliente);
            ps.setInt(4, tipoArchivo);
             
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = idCliente;
                temporal.idSolicitud = idCiclo;
                temporal.tipoCliente = rs.getString("AS_TIPO_CLIENTE");
                temporal.tipo = rs.getInt("AS_TIPO");
                temporal.consecutivo = rs.getInt("AS_CONSECUTIVO");
                temporal.nombre = rs.getString("AS_NOMBRE");
                temporal.fechaCaptura = rs.getTimestamp("AS_FECHA_CAPTURA");
                array.add(temporal);
                //myLogger.debug("Archivo encontrado : "+temporal.toString());
            }
            if (array.size() > 0) {
                elemento = new ArchivoAsociadoVO();
                elemento = (ArchivoAsociadoVO) array.get(0);
            }
        } catch (SQLException sqle) {
            myLogger.error("getArchivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getArchivos", e);
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
        return elemento;

    }
    
    public ArchivoAsociadoVO[] getArchivosTipo(int idGrupo, int idCiclo/*, int tipoArchvio*/) throws ClientesException {

        //String query = "SELECT as_numcliente,as_numsolicitud,as_tipo_cliente,as_tipo,as_nombre,as_consecutivo,as_fecha_captura FROM d_archivos_asociados WHERE as_numcliente=? AND as_numsolicitud=? AND as_tipo=?";
        String query = "SELECT as_numcliente,as_numsolicitud,as_tipo_cliente,as_tipo,as_nombre,as_consecutivo,as_fecha_captura FROM d_archivos_asociados WHERE as_numcliente=? AND as_numsolicitud=?";
        ArrayList<ArchivoAsociadoVO> array = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;
        ArchivoAsociadoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            //ps.setInt(3, tipoArchvio);
            myLogger.debug("Ejecutando = " + query);
            //myLogger.debug("Parametros["+idGrupo+", "+idCiclo+", "+tipoArchvio+"]");
            myLogger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = idGrupo;
                temporal.idSolicitud = idCiclo;
                temporal.tipoCliente = rs.getString("as_tipo_cliente");
                temporal.tipo = rs.getInt("as_tipo");
                temporal.consecutivo = rs.getInt("as_consecutivo");
                temporal.nombre = rs.getString("as_nombre");
                temporal.fechaCaptura = rs.getTimestamp("as_fecha_captura");
                array.add(temporal);
                myLogger.debug("Archivo encontrado : " + temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new ArchivoAsociadoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (ArchivoAsociadoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getArchivosTipo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getArchivosTipo", e);
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

    /*	public ArchivoAsociadoVO getArchivo(int idCliente, int idSolicitud) throws ClientesException{
     ArchivoAsociadoVO  archivo = null;
     Connection cn = null;
     String query =	"SELECT AS_NUMCLIENTE, AS_NUMSOLICITUD, AS_TIPO, AS_NOMBRE, AS_FECHA_CAPTURA FROM D_ARCHIVOS_ASOCIADOS WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ?";
     try{
     cn = getConnection();
     PreparedStatement ps = cn.prepareStatement(query);
     ps.setInt(1, idCliente);
     ps.setInt(2, idSolicitud);
     myLogger.debug("Ejecutando = "+query);
     ResultSet rs = ps.executeQuery();
     if(rs.next()){
     archivo =  new ArchivoAsociadoVO();
     archivo.idCliente = idCliente;
     archivo.idSolicitud = idSolicitud;
     archivo.tipo = rs.getInt("AS_TIPO");
     archivo.nombre = rs.getString("AS_NOMBRE");
     archivo.fechaCaptura = rs.getTimestamp("AS_FECHA_CAPTURA");
     }
     }catch(SQLException sqle) {
     Logger.debug("SQLException en getArchivo : "+sqle.getMessage());
     sqle.printStackTrace();
     throw new ClientesException(sqle.getMessage());
     }
     catch(Exception e) {
     Logger.debug("Excepcion en getArchivo : "+e.getMessage());
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
     return archivo;
     }*/
    public int addArchivo(int idCliente, int idSolicitud, ArchivoAsociadoVO archivo) throws ClientesException {

        String query = "INSERT INTO D_ARCHIVOS_ASOCIADOS (AS_NUMCLIENTE, AS_NUMSOLICITUD, AS_TIPO_CLIENTE, AS_TIPO, "
                + "AS_CONSECUTIVO, AS_NOMBRE, AS_FECHA_CAPTURA, AS_ESTATUS_MIGRACION ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, archivo.tipoCliente);
            ps.setInt(param++, archivo.tipo);
            ps.setInt(param++, archivo.consecutivo);
            ps.setString(param++, archivo.nombre);
            ps.setTimestamp(param++, archivo.fechaCaptura);
            ps.setString(param++, archivo.estatusMigracion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Archivo= " + archivo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addArchivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addArchivo", e);
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

//	public int updateArchivo(int idCliente, int idSolicitud, ArchivoAsociadoVO archivo) throws ClientesException{
//
//		String query =	"UPDATE D_ARCHIVOS_ASOCIADOS SET AS_NOMBRE = ?, AS_FECHA_CAPTURA = CURRENT_TIMESTAMP WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO = ?";
//
//		Connection cn = null;
//		PreparedStatement ps = null;
//		int param = 1;
//		int  res = 0;
//		try{
//			cn = getConnection();
//			ps = cn.prepareStatement(query); 
//			
//			ps.setString(param++, archivo.nombre);
//			ps.setInt(param++, idCliente);
//			ps.setInt(param++, idSolicitud);
//			ps.setInt(param++, archivo.tipo);
//			Logger.debug("Ejecutando = "+query);
//			Logger.debug("Archivo= "+archivo.toString());
//			res = ps.executeUpdate();
//			Logger.debug("Resultado = "+res);
//		}
//		catch(SQLException sqle) {
//			Logger.debug("SQLException en updateArchivo : "+sqle.getMessage());
//			sqle.printStackTrace();
//			throw new ClientesDBException(sqle.getMessage());
//		}
//		catch(Exception e) {
//			Logger.debug("Excepcion en updateArchivo : "+e.getMessage());
//			e.printStackTrace();
//			throw new ClientesException(e.getMessage());
//		}
//		finally {
//			try {
//				if ( cn!=null ) cn.close();
//			}
//			catch(SQLException sqle) {
//				throw new ClientesDBException(sqle.getMessage());
//			}
//		}
//		return res;
//	}
    public int updateArchivo(int idCliente, int idSolicitud, ArchivoAsociadoVO archivo) throws ClientesException {

        String query = "UPDATE D_ARCHIVOS_ASOCIADOS SET AS_NOMBRE = ?, AS_FECHA_CAPTURA = CURRENT_TIMESTAMP, AS_ESTATUS_MIGRACION='N' WHERE "
                + "AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO = ? AND AS_CONSECUTIVO = ? AND AS_TIPO_CLIENTE = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setString(param++, archivo.nombre);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, archivo.tipo);
            ps.setInt(param++, archivo.consecutivo);
            ps.setString(param++, archivo.tipoCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Archivo= " + archivo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateArchivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateArchivo", e);
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
    
        public int updateEstatusArchivo(ArchivoAsociadoVO archivo) throws ClientesException {

        String query = "UPDATE D_ARCHIVOS_ASOCIADOS SET as_estatus_migracion=? WHERE "
                + "AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO = ? AND AS_CONSECUTIVO = ? AND AS_TIPO_CLIENTE = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setString(param++, archivo.estatusMigracion);
            ps.setInt(param++, archivo.idCliente);
            ps.setInt(param++, archivo.idSolicitud);
            ps.setInt(param++, archivo.tipo);
            ps.setInt(param++, archivo.consecutivo);
            ps.setString(param++, archivo.tipoCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Archivo= " + archivo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateArchivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateArchivo", e);
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

    public void deleteArchivoAsociado(int idCliente, int idSolicitud, int tipoArchivo) throws ClientesException {
        deleteArchivoAsociado(null, idCliente, idSolicitud, tipoArchivo, 0);
    }

    public void deleteArchivoAsociado(Connection conn, int idCliente, int idSolicitud, int tipoArchivo, int consecutivo) throws ClientesException {

        String query = "DELETE FROM D_ARCHIVOS_ASOCIADOS WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO = ? AND AS_CONSECUTIVO= ?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, tipoArchivo);
            ps.setInt(4, consecutivo);

            myLogger.debug("Ejecutando = " + query);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteArchivoAsociado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteArchivoAsociado", e);
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
        public void deleteArchivoAsociado(Connection conn, int idCliente, int idSolicitud, int tipoArchivo) throws ClientesException {

        String query = "DELETE FROM D_ARCHIVOS_ASOCIADOS WHERE AS_NUMCLIENTE = ? AND AS_NUMSOLICITUD = ? AND AS_TIPO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, tipoArchivo);

            myLogger.debug("Ejecutando = " + query);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteArchivoAsociado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteArchivoAsociado", e);
            throw new ClientesException(e.getMessage());
        }
    }

    public void respaldaArchivoCiclo(int numEquipo, int numCiclo) throws ClientesException {

        String query = "insert into d_archivos_asociados_del "
                + "(as_numcliente,as_numsolicitud,as_tipo_cliente,as_tipo,as_consecutivo,as_nombre,as_fecha_captura,as_estatus_migracion) "
                + "(select as_numcliente,as_numsolicitud,as_tipo_cliente,as_tipo,as_consecutivo,as_nombre,as_fecha_captura,as_estatus_migracion "
                + "FROM d_archivos_asociados WHERE as_numcliente=? AND as_numsolicitud=? AND as_tipo=16)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaArchivoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaArchivoCiclo", e);
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

    public void eliminaArchivoCiclo(int numEquipo, int numCiclo, boolean conexionODS) throws ClientesException {

        String query = "DELETE FROM d_archivos_asociados WHERE as_numcliente=? AND as_numsolicitud=? AND as_tipo=16";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                cn = getODSConnection();    
            }else{
                cn = getConnection();
            }
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaArchivoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaArchivoCiclo", e);
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

    
    public int deleteRegistrosCaducados() throws ClientesException {

        String query = "DELETE B FROM D_SOLICITUDES A, D_ARCHIVOS_ASOCIADOS B WHERE DATE(AS_FECHA_CAPTURA)<=ADDDATE(CURDATE(),-2) "
                + "AND AS_TIPO = 4 AND SO_DESEMBOLSADO = 1 AND AS_NUMCLIENTE = SO_NUMCLIENTE AND AS_NUMSOLICITUD = SO_NUMSOLICITUD";

        Connection cn = null;
        Statement st = null;
        int res = 0;

        try {
            cn = getConnection();
            st = cn.createStatement();
            myLogger.debug("Ejecutando = " + query);
            res = st.executeUpdate(query);
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteRegistrosCaducados", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteRegistrosCaducados", e);
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

    public boolean tieneArchivo(int idCliente, int idSolicitud) throws ClientesException {
        String query = "SELECT * FROM d_archivos_asociados WHERE as_numcliente=? AND as_numsolicitud=? AND as_tipo_cliente='I'";
        boolean tieneArchivo = false;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                tieneArchivo = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("tieneArchivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tieneArchivo", e);
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
        return tieneArchivo;
    }

    public ArrayList<ArchivoAsociadoVO> getArchivosMigracion() {

        String sql = "";
        ArrayList<ArchivoAsociadoVO> arrArchivosBO = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            sql = "Select * from d_archivos_asociados"
                    + " where as_estatus_migracion in ('N','E')";

            System.out.println("Ejecutando = " + sql);
            cn = getConnection();
            ps = cn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = res.getInt("as_numcliente");
                temporal.idSolicitud = res.getInt("as_numsolicitud");
                temporal.tipoCliente = res.getString("as_tipo_cliente");
                temporal.tipo = res.getInt("as_tipo");
                temporal.consecutivo = res.getInt("as_consecutivo");
                temporal.nombre = res.getString("as_nombre");
                temporal.estatusMigracion = res.getString("as_estatus_migracion");
                arrArchivosBO.add(temporal);
            }
        } catch (SQLException exc) {

            exc.printStackTrace();
            System.out.println("getArchivosMigracion " + exc.getMessage());
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("getArchivosMigracion " + exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
                System.out.println("Error al cerrar la connecion en getArchivosMigracion" + exc.getMessage());
            }
        }
        return arrArchivosBO;
    }
    
    public ArrayList<ArchivoAsociadoVO> getArchivosMigracionFirstLoad(String rangoFechas) {

        String sql = "";
        String[] fechas = rangoFechas.split(",");
        String fechaIni = fechas[0];
        String fechaFinal = fechas[1];
        ArrayList<ArchivoAsociadoVO> arrArchivosBO = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            sql = "Select  * from d_archivos_asociados" +
                    " where as_fecha_captura between ? and ?" +
                    " order by as_fecha_captura";

            System.out.println("Ejecutando = " + sql);
            cn = getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, fechaIni);
            ps.setString(2, fechaFinal);
            res = ps.executeQuery();
            while (res.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = res.getInt("as_numcliente");
                temporal.idSolicitud = res.getInt("as_numsolicitud");
                temporal.tipoCliente = res.getString("as_tipo_cliente");
                temporal.tipo = res.getInt("as_tipo");
                temporal.consecutivo = res.getInt("as_consecutivo");
                temporal.nombre = res.getString("as_nombre");
                temporal.estatusMigracion = res.getString("as_estatus_migracion");
                arrArchivosBO.add(temporal);
            }
        } catch (SQLException exc) {

            exc.printStackTrace();
            System.out.println("getArchivosMigracion " + exc.getMessage());
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("getArchivosMigracion " + exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
                System.out.println("Error al cerrar la connecion en getArchivosMigracion" + exc.getMessage());
            }
        }
        return arrArchivosBO;
    }
    
     public ArrayList<ArchivoAsociadoVO> getArchivosMigracionFirstLoad(String rangoFechas, Connection cn) {

        String sql = "";
        String[] fechas = rangoFechas.split(",");
        String fechaIni = fechas[0];
        String fechaFinal = fechas[1];
        ArrayList<ArchivoAsociadoVO> arrArchivosBO = new ArrayList<ArchivoAsociadoVO>();
        ArchivoAsociadoVO temporal = null;        
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            sql = "Select  * from d_archivos_asociados" +
                    " where as_fecha_captura between ? and ?" +
                    " order by as_fecha_captura";

            myLogger.info("Ejecutando = " + sql);
            //cn = getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, fechaIni);
            ps.setString(2, fechaFinal);
            res = ps.executeQuery();
            while (res.next()) {
                temporal = new ArchivoAsociadoVO();
                temporal.idCliente = res.getInt("as_numcliente");
                temporal.idSolicitud = res.getInt("as_numsolicitud");
                temporal.tipoCliente = res.getString("as_tipo_cliente");
                temporal.tipo = res.getInt("as_tipo");
                temporal.consecutivo = res.getInt("as_consecutivo");
                temporal.nombre = res.getString("as_nombre");
                temporal.estatusMigracion = res.getString("as_estatus_migracion");
                arrArchivosBO.add(temporal);
            }
        } catch (SQLException exc) {

            exc.printStackTrace();
            myLogger.info("getArchivosMigracion " + exc.getMessage());
        } catch (Exception exc) {
            exc.printStackTrace();
            myLogger.info("getArchivosMigracion " + exc.getMessage());
        } finally {
            
        }
        return arrArchivosBO;
    }
}

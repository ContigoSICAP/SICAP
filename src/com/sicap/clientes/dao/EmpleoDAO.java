package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import org.apache.log4j.Logger;

public class EmpleoDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(EmpleoDAO.class);

//SELECT EM_NUMCLIENTE, EM_NUMSOLICITUD, EM_ESTATUS, EM_EXP_LABORAL, EM_TIPO_CONTRATO, EM_PLAZO_CONTRATO, EM_RAZON_SOCIAL, EM_EMPLEADOS, EM_ARRAIGO_EMPRESA, EM_FECHA_CAPTURA FROM D_EMPLEOS WHERE EM_NUMCLIENTE = ? AND EM_NUMSOLICITUD = ? 
    public EmpleoVO getEmpleo(int idCliente, int idSolicitud) throws ClientesException {
        EmpleoVO empleo = null;
        Connection cn = null;
        String query = "SELECT * FROM D_EMPLEOS WHERE EM_NUMCLIENTE = ? AND EM_NUMSOLICITUD = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
//			myLogger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                empleo = new EmpleoVO();
                empleo.idCliente = idCliente;
                empleo.idSolicitud = idSolicitud;
                empleo.estatus = rs.getInt("EM_ESTATUS");
                empleo.antEmpleo = rs.getInt("EM_ANT_EMPLEO");
                empleo.fechaInicioNeg = rs.getDate("EM_FECHAINICIONEG");
                empleo.tipoContrato = rs.getInt("EM_TIPO_CONTRATO");
                empleo.plazoContrato = rs.getInt("EM_PLAZO_CONTRATO");
                empleo.turno = rs.getInt("EM_TURNO");
                empleo.afiliacionIMSS = rs.getInt("EM_AFILIACION_IMSS");
                empleo.razonSocial = rs.getString("EM_RAZON_SOCIAL");
                empleo.numeroEmpleados = rs.getInt("EM_EMPLEADOS");
                empleo.arraigoEmpresa = rs.getInt("EM_ARRAIGO_EMPRESA");
                empleo.fechaCaptura = rs.getTimestamp("EM_FECHA_CAPTURA");
                empleo.numEmpleado = rs.getString("EM_NUMEMPLEADO");
                empleo.area = rs.getString("EM_AREA");
                empleo.puesto = rs.getString("EM_PUESTO");
                empleo.telefono = rs.getString("EM_TELEFONO");
                empleo.extension = rs.getString("EM_EXT");
                empleo.sueldoMensual = rs.getDouble("EM_SUELDO_MENSUAL");
                empleo.otrosIngresos = rs.getDouble("EM_OTROS_INGRESOS");
                empleo.fteOtrosIngresos = rs.getInt("EM_FTE_OTROS_INGRESOS");
                empleo.formaIngreso = rs.getInt("EM_FORMA_INGRESO");
                empleo.tipoSector = rs.getInt("EM_TIPO_SECTOR");
                empleo.dependencia = rs.getInt("EM_DEPENDENCIA");
                empleo.referencia = rs.getString("em_referencias");
                empleo.ubicacionNegocio = rs.getInt("em_numubicacionneg");
            }
        } catch (SQLException sqle) {
            myLogger.error("getEmpleo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEmpleo", e);
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
        return empleo;
    }

//INSERT INTO D_EMPLEOS (EM_NUMCLIENTE, EM_NUMSOLICITUD, EM_ESTATUS, EM_ANT_EMPLEO, EM_TIPO_CONTRATO, EM_PLAZO_CONTRATO, EM_RAZON_SOCIAL, EM_EMPLEADOS, EM_ARRAIGO_EMPRESA, EM_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    public int addEmpleo(Connection conn, int idCliente, int idSolicitud, EmpleoVO empleo) throws ClientesException {

        String query = "INSERT INTO D_EMPLEOS (EM_NUMCLIENTE, EM_NUMSOLICITUD, EM_ESTATUS, EM_ANT_EMPLEO, em_fechaInicioNeg, "
                + "EM_TIPO_CONTRATO, EM_PLAZO_CONTRATO, EM_TURNO, EM_AFILIACION_IMSS, EM_RAZON_SOCIAL, EM_EMPLEADOS, EM_ARRAIGO_EMPRESA, "
                + "EM_FECHA_CAPTURA, EM_NUMEMPLEADO, EM_AREA, EM_PUESTO, EM_TELEFONO, EM_EXT, EM_SUELDO_MENSUAL, EM_OTROS_INGRESOS, EM_FTE_OTROS_INGRESOS, "
                + "EM_FORMA_INGRESO, EM_TIPO_SECTOR, EM_DEPENDENCIA, em_numubicacionneg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, empleo.estatus);
            ps.setInt(param++, empleo.antEmpleo);
            ps.setDate(param++,empleo.fechaInicioNeg);
            ps.setInt(param++, empleo.tipoContrato);
            ps.setInt(param++, empleo.plazoContrato);
            ps.setInt(param++, empleo.turno);
            ps.setInt(param++, empleo.afiliacionIMSS);
            ps.setString(param++, empleo.razonSocial);
            ps.setInt(param++, empleo.numeroEmpleados);
            ps.setInt(param++, empleo.arraigoEmpresa);
            ps.setTimestamp(param++, empleo.fechaCaptura);
            ps.setString(param++, empleo.numEmpleado);

            ps.setString(param++, empleo.area);
            ps.setString(param++, empleo.puesto);
            ps.setString(param++, empleo.telefono);
            ps.setString(param++, empleo.extension);
            ps.setDouble(param++, empleo.sueldoMensual);
            ps.setDouble(param++, empleo.otrosIngresos);
            ps.setInt(param++, empleo.fteOtrosIngresos);
            ps.setInt(param++, empleo.formaIngreso);
            ps.setInt(param++, empleo.tipoSector);
            ps.setInt(param++, empleo.dependencia);
            ps.setInt(param++, empleo.ubicacionNegocio);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Empleo = " + empleo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addEmpleo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addEmpleo", e);
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

//SELECT EM_NUMCLIENTE, EM_NUMSOLICITUD, EM_ESTATUS, EM_ANT_EMPLEO, EM_TIPO_CONTRATO, EM_PLAZO_CONTRATO, EM_RAZON_SOCIAL, EM_EMPLEADOS, EM_ARRAIGO_EMPRESA, EM_FECHA_CAPTURA FROM D_EMPLEOS WHERE EM_NUMCLIENTE = ? AND EM_NUMSOLICITUD = ?
//UPDATE D_EMPLEOS SET EM_ESTATUS = ?, EM_ANT_EMPLEO = ?, EM_TIPO_CONTRATO = ?, EM_PLAZO_CONTRATO = ?, EM_RAZON_SOCIAL = ?, EM_EMPLEADOS = ?, EM_ARRAIGO_EMPRESA WHERE EM_NUMCLIENTE = ? AND EM_NUMSOLICITUD = ?
    public int updateEmpleo(Connection conn, int idCliente, int idSolicitud, EmpleoVO empleo) throws ClientesException {

        String query = "UPDATE D_EMPLEOS SET EM_ESTATUS = ?, EM_ANT_EMPLEO = ?, EM_TIPO_CONTRATO = ?, EM_PLAZO_CONTRATO = ?, "
                + "EM_TURNO = ?, EM_AFILIACION_IMSS = ?, EM_RAZON_SOCIAL = ?, EM_EMPLEADOS = ?, EM_ARRAIGO_EMPRESA = ?, "
                + "EM_AREA = ?, EM_NUMEMPLEADO = ?, EM_PUESTO = ?, EM_TELEFONO = ?, EM_EXT = ?, EM_SUELDO_MENSUAL = ?, EM_OTROS_INGRESOS = ?, "
                + "EM_FTE_OTROS_INGRESOS = ?, EM_FORMA_INGRESO = ?, EM_TIPO_SECTOR = ?, EM_DEPENDENCIA = ?, em_numubicacionneg = ?, EM_FECHAINICIONEG =? "
                + "WHERE EM_NUMCLIENTE = ? AND EM_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int res = 0;
        int param = 1;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setInt(param++, empleo.estatus);
            ps.setInt(param++, empleo.antEmpleo);
            ps.setInt(param++, empleo.tipoContrato);
            ps.setInt(param++, empleo.plazoContrato);
            ps.setInt(param++, empleo.turno);
            ps.setInt(param++, empleo.afiliacionIMSS);
            ps.setString(param++, empleo.razonSocial);
            ps.setInt(param++, empleo.numeroEmpleados);
            ps.setInt(param++, empleo.arraigoEmpresa);
            
            ps.setString(param++, empleo.area);
            ps.setString(param++, empleo.numEmpleado);
            ps.setString(param++, empleo.puesto);
            ps.setString(param++, empleo.telefono);
            ps.setString(param++, empleo.extension);
            ps.setDouble(param++, empleo.sueldoMensual);
            ps.setDouble(param++, empleo.otrosIngresos);
            ps.setInt(param++, empleo.fteOtrosIngresos);
            ps.setInt(param++, empleo.formaIngreso);
            ps.setInt(param++, empleo.tipoSector);
            ps.setInt(param++, empleo.dependencia);
            ps.setInt(param++, empleo.ubicacionNegocio);
            ps.setDate(param++, empleo.fechaInicioNeg);

            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Empleo = " + empleo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateEmpleo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEmpleo", e);
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

    public int compruebaEmpleo(IntegranteCicloVO integrante) throws ClientesException {
        int informacion = 0;
        String query = "SELECT em_numcliente FROM d_empleos WHERE em_numcliente=? AND em_numsolicitud=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, integrante.idCliente);
            ps.setInt(2, integrante.idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + integrante.idCliente + ", " + integrante.idSolicitud + "]");
            res = ps.executeQuery();
            if (res.next()) {
                informacion = 1;
            }
        } catch (SQLException sqle) {
            myLogger.error("compruebaEmpleo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("compruebaEmpleo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return informacion;
    }
    public int deleteEmpleo(Connection conn, int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM d_empleos" +
                       " where em_numcliente = ?" +
                       " and em_numsolicitud = ?";
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
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteEmpleo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteEmpleo", e);
            throw new ClientesException(e.getMessage());
        } 
        return res;
    }
}

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class CreditoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(CreditoDAO.class);

    public CreditoVO getCredito(int idCliente, int idSolicitud, int tipoInformacion) throws ClientesException {
        return getCredito(idCliente, idSolicitud, tipoInformacion, 0);
    }

    public CreditoVO getCredito(int idCliente, int idSolicitud, int tipoInformacion, int idObligado) throws ClientesException {
        CreditoVO credito = null;
        Connection cn = null;
        String query = "SELECT * FROM  D_CREDITO WHERE CR_NUMCLIENTE = ? AND CR_NUMSOLICITUD = ? AND CR_TIPOCREDITO = ? AND CR_NUMOBLIGADO = ?";
        try {
            cn = this.getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, tipoInformacion);
            ps.setInt(4, idObligado);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("idCliente = [" + idCliente + "], tipoInformacion = [" + tipoInformacion + "], idObligado = [" + idObligado + "]");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                credito = new CreditoVO();
                credito.idCliente = idCliente;
                credito.idObligado = idObligado;
                credito.idSolicitud = idSolicitud;
                credito.fechaCaptura = rs.getTimestamp("CR_FECHACAPTURA");
                credito.fechaConsulta = rs.getDate("CR_FECHACONSULTA");
                credito.comportamiento = rs.getInt("CR_COMPORTAMIENTO");
                credito.calificacionMesaControl = rs.getInt("CR_CALIFICACION_MESA_CONTROL");
                credito.descripcion = rs.getString("CR_DESCRIPCION");
                credito.tipoCredito = rs.getInt("CR_TIPOCREDITO");
                credito.tipoCuenta = rs.getInt("CR_TIPO_CUENTA");
                credito.antCuenta = rs.getInt("CR_ANT_CUENTA");
                credito.numBusquedaCuenta = rs.getInt("CR_BUSQUEDA_CUENTA");
                credito.respCrediticia = rs.getString("CR_RESP_CREDITICIA");
                credito.fechaConsCrediticia = rs.getDate("CR_FECHACONS_CREDITICIA");
                credito.aceptaRegular = rs.getInt("cr_acepta_regular");
                myLogger.debug("Buro/circulo encontrado = " + credito);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCredito", e);
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
        return credito;
    }

    public CreditoVO getUltimoCredito(int idCliente, int tipoInformacion, int idObligado) throws ClientesException {
        CreditoVO credito = null;
        Connection cn = null;
        String query = "SELECT * FROM  D_CREDITO WHERE CR_NUMCLIENTE = ? AND CR_TIPOCREDITO = ? AND CR_NUMOBLIGADO = ? order by CR_NUMSOLICITUD desc limit 1";
        try {
            cn = this.getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, tipoInformacion);
            ps.setInt(3, idObligado);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("idCliente = [" + idCliente + "], tipoInformacion = [" + tipoInformacion + "], idObligado = [" + idObligado + "]");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                credito = new CreditoVO();
                credito.idCliente = idCliente;
                credito.idObligado = idObligado;
                credito.idSolicitud = rs.getInt("CR_NUMSOLICITUD");
                credito.fechaCaptura = rs.getTimestamp("CR_FECHACAPTURA");
                credito.fechaConsulta = rs.getDate("CR_FECHACONSULTA");
                credito.comportamiento = rs.getInt("CR_COMPORTAMIENTO");
                credito.calificacionMesaControl = rs.getInt("CR_CALIFICACION_MESA_CONTROL");
                credito.descripcion = rs.getString("CR_DESCRIPCION");
                credito.tipoCredito = rs.getInt("CR_TIPOCREDITO");
                credito.tipoCuenta = rs.getInt("CR_TIPO_CUENTA");
                credito.antCuenta = rs.getInt("CR_ANT_CUENTA");
                credito.numBusquedaCuenta = rs.getInt("CR_BUSQUEDA_CUENTA");
                credito.respCrediticia = rs.getString("CR_RESP_CREDITICIA");
                credito.fechaConsCrediticia = rs.getDate("CR_FECHACONS_CREDITICIA");
                credito.aceptaRegular = rs.getInt("cr_acepta_regular");
                myLogger.debug("Ultimo credito encontrado = " + credito);
            }
        } catch (SQLException sqle) {
            myLogger.error("getUltimoCredito", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getUltimoCredito", e);
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
        return credito;
    }
    
    public int addBuroCredito(Connection conn, CreditoVO buro) throws ClientesException {

        String query = "INSERT INTO D_CREDITO (CR_NUMCLIENTE, CR_NUMOBLIGADO, CR_NUMSOLICITUD, "
                + "CR_FECHACONSULTA, CR_FECHACAPTURA, CR_COMPORTAMIENTO, CR_CALIFICACION_MESA_CONTROL, CR_DESCRIPCION, "
                + "CR_TIPOCREDITO, CR_TIPO_CUENTA, CR_ANT_CUENTA, CR_BUSQUEDA_CUENTA, CR_RESP_CREDITICIA, cr_acepta_regular, CR_FECHACONS_CREDITICIA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE)";

        Connection cn = null;
        int res = 0;
        try {
            PreparedStatement ps = null;
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            int param = 1;
            ps.setInt(param++, buro.idCliente);
            ps.setInt(param++, buro.idObligado);
            ps.setInt(param++, buro.idSolicitud);
            ps.setDate(param++, buro.fechaConsulta);
            ps.setTimestamp(param++, buro.fechaCaptura);
            ps.setInt(param++, buro.comportamiento);
            ps.setInt(param++, buro.calificacionMesaControl);
            ps.setString(param++, buro.descripcion);
            ps.setInt(param++, buro.tipoCredito);
            ps.setInt(param++, buro.tipoCuenta);
            ps.setInt(param++, buro.antCuenta);
            ps.setInt(param++, buro.numBusquedaCuenta);
            ps.setString(param++, buro.respCrediticia);
            ps.setInt(param++, buro.aceptaRegular);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Buro/Circulo= " + buro.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addBuroCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addBuroCredito", e);
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

    public int updateBuroCliente(Connection conn, CreditoVO buro) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_CREDITO SET CR_FECHACONSULTA = ?, CR_COMPORTAMIENTO = ?, CR_CALIFICACION_MESA_CONTROL = ?, "
                + "CR_DESCRIPCION = ?, CR_TIPO_CUENTA = ?, CR_ANT_CUENTA = ?, CR_BUSQUEDA_CUENTA = ?, CR_RESP_CREDITICIA = ?, CR_FECHACONS_CREDITICIA = CURRENT_DATE, cr_acepta_regular = ?"
                + " WHERE CR_NUMCLIENTE = ? AND CR_NUMOBLIGADO =  ? AND CR_NUMSOLICITUD = ? AND CR_TIPOCREDITO=?";
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
            ps.setDate(param++, buro.fechaConsulta);
            ps.setInt(param++, buro.comportamiento);
            ps.setInt(param++, buro.calificacionMesaControl);
            ps.setString(param++, buro.descripcion);
            ps.setInt(param++, buro.tipoCuenta);
            ps.setInt(param++, buro.antCuenta);
            ps.setInt(param++, buro.numBusquedaCuenta);
            ps.setString(param++, buro.respCrediticia);
            ps.setInt(param++,buro.aceptaRegular);
            ps.setInt(param++, buro.idCliente);
            ps.setInt(param++, buro.idObligado);
            ps.setInt(param++, buro.idSolicitud);
            ps.setInt(param++, buro.tipoCredito);            
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Credito= " + buro.toString());
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateBuroCliente", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateBuroCliente", e);
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
    
    public int delCredito(InformacionCrediticiaVO infoCredito) throws ClientesException {

        String query = "DELETE FROM d_credito WHERE cr_numcliente=? AND cr_numsolicitud=?;";

        Connection cn = null;
        PreparedStatement ps = null;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, infoCredito.idCliente);
            ps.setInt(2, infoCredito.idSolicitud);
            myLogger.debug("Ejecutando= "+ps);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("delCredito",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delCredito",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
        public int delInfoCrediticia(Connection conn, int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM d_credito WHERE cr_numcliente=? AND cr_numsolicitud=?";
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

            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("delInfoCrediticia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delInfoCrediticia", e);
            throw new ClientesException(e.getMessage());
        }finally {
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

    /**
     * Metodo que obtiene los creditos de los clientes que estan autorizados por escepcion de un grupo
     * 
     * @param idGrupo
     * 
     * @param idCiclo
     * 
     * @return
     * 
     * @throws ClientesException 
     */
    public List<CreditoVO> getCreditosGrupoAutExcepcion(int idGrupo, int idCiclo) throws ClientesException {
        List<CreditoVO> creditos = new ArrayList<CreditoVO>();
        Connection cn = null;
        String query = "select cr.* from d_credito cr " +
                        "inner join d_integrantes_ciclo ic on cr.CR_NUMCLIENTE = ic.ic_numcliente and cr.CR_NUMSOLICITUD = ic.ic_numsolicitud " +
                        "where cr.cr_acepta_regular in (3,4) and ic.ic_numgrupo = ? and ic.ic_numciclo = ?;";
        try {
            cn = this.getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("idGrupo = [" + idGrupo + "], idCiclo = [" + idCiclo + "]");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                CreditoVO credito = new CreditoVO();
                credito.idCliente = rs.getInt("CR_NUMCLIENTE");
                credito.idObligado = rs.getInt("CR_NUMOBLIGADO");
                credito.idSolicitud = rs.getInt("CR_NUMSOLICITUD");
                credito.fechaCaptura = rs.getTimestamp("CR_FECHACAPTURA");
                credito.fechaConsulta = rs.getDate("CR_FECHACONSULTA");
                credito.comportamiento = rs.getInt("CR_COMPORTAMIENTO");
                credito.calificacionMesaControl = rs.getInt("CR_CALIFICACION_MESA_CONTROL");
                credito.descripcion = rs.getString("CR_DESCRIPCION");
                credito.tipoCredito = rs.getInt("CR_TIPOCREDITO");
                credito.tipoCuenta = rs.getInt("CR_TIPO_CUENTA");
                credito.antCuenta = rs.getInt("CR_ANT_CUENTA");
                credito.numBusquedaCuenta = rs.getInt("CR_BUSQUEDA_CUENTA");
                credito.respCrediticia = rs.getString("CR_RESP_CREDITICIA");
                credito.fechaConsCrediticia = rs.getDate("CR_FECHACONS_CREDITICIA");
                credito.aceptaRegular = rs.getInt("cr_acepta_regular");
                myLogger.debug("Buro/circulo encontrado = " + credito);
                creditos.add(credito);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditosGrupoAutExcepcion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditosGrupoAutExcepcion", e);
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
        return creditos;
    }
        public List<CreditoVO> getCreditosGrupoAutOtraFin(int idGrupo, int idCiclo) throws ClientesException {
        List<CreditoVO> creditos = new ArrayList<CreditoVO>();
        Connection cn = null;
        String query = "select cr.* from d_credito cr " +
                        "inner join d_integrantes_ciclo ic on cr.CR_NUMCLIENTE = ic.ic_numcliente and cr.CR_NUMSOLICITUD = ic.ic_numsolicitud " +
                        "where cr.cr_acepta_regular =6 and ic.ic_numgrupo = ? and ic.ic_numciclo = ?;";
        try {
            cn = this.getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("idGrupo = [" + idGrupo + "], idCiclo = [" + idCiclo + "]");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                CreditoVO credito = new CreditoVO();
                credito.idCliente = rs.getInt("CR_NUMCLIENTE");
                credito.idObligado = rs.getInt("CR_NUMOBLIGADO");
                credito.idSolicitud = rs.getInt("CR_NUMSOLICITUD");
                credito.fechaCaptura = rs.getTimestamp("CR_FECHACAPTURA");
                credito.fechaConsulta = rs.getDate("CR_FECHACONSULTA");
                credito.comportamiento = rs.getInt("CR_COMPORTAMIENTO");
                credito.calificacionMesaControl = rs.getInt("CR_CALIFICACION_MESA_CONTROL");
                credito.descripcion = rs.getString("CR_DESCRIPCION");
                credito.tipoCredito = rs.getInt("CR_TIPOCREDITO");
                credito.tipoCuenta = rs.getInt("CR_TIPO_CUENTA");
                credito.antCuenta = rs.getInt("CR_ANT_CUENTA");
                credito.numBusquedaCuenta = rs.getInt("CR_BUSQUEDA_CUENTA");
                credito.respCrediticia = rs.getString("CR_RESP_CREDITICIA");
                credito.fechaConsCrediticia = rs.getDate("CR_FECHACONS_CREDITICIA");
                credito.aceptaRegular = rs.getInt("cr_acepta_regular");
                myLogger.debug("Buro/circulo encontrado = " + credito);
                creditos.add(credito);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditosGrupoAutExcepcion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditosGrupoAutExcepcion", e);
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
        return creditos;
    }
}

package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.ClienteIntercicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class ClienteIntercicloDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(IntegranteCicloDAO.class);

    public int addIntegranteInterciclo(Connection con, ClienteIntercicloVO integrante) throws ClientesException {
        String query = "insert into d_integrantes_interciclo(in_numgrupo,in_numciclo,in_numcliente,in_numsolicitud,in_semDispersion,in_fechaAlta,in_fechaUltModificacion, in_estatus) "
                + "values(?,?,?,?,?,now(),now(),?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (con == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = con.prepareStatement(query);
            }
            ps.setInt(param++, integrante.getNumGrupo());
            ps.setInt(param++, integrante.getNumCiclo());
            ps.setInt(param++, integrante.getNumCliente());
            ps.setInt(param++, integrante.getNumSolicitud());
            ps.setInt(param++, integrante.getSemDispercion());
            ps.setInt(param++, integrante.getEstatus());
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIntegranteInterciclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIntegranteInterciclo", e);
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
    public int updateIntegranteInterCiclo(Connection con, ClienteIntercicloVO integrante) throws ClientesException {

        String query = "update d_integrantes_interciclo set in_fechaUltModificacion = now(), in_estatus = ?" +
                       " where in_numgrupo = ?" +
                       " and in_numciclo = ?" +
                       " and in_numcliente = ?" +
                       " and in_numsolicitud =?" +
                       " and in_semDispersion =?;";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if(con == null){
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                con = getConnection();
                ps = con.prepareStatement(query);
            }
            ps.setInt(param++, integrante.getEstatus());
            ps.setInt(param++, integrante.getNumGrupo());
            ps.setInt(param++, integrante.getNumCiclo());
            ps.setInt(param++, integrante.getNumCliente());
            ps.setInt(param++, integrante.getNumSolicitud());
            ps.setInt(param++, integrante.getSemDispercion());
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateIntegranteInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateIntegranteInterCiclo", e);
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
    
    public ArrayList getClientesInterCiclo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "Select * from d_integrantes_interciclo" +
                       " where in_numgrupo = ?" +
                       " and in_numciclo = ?" +
                       " and in_estatus = 1";

         ArrayList<ClienteIntercicloVO> clientesIC = new ArrayList<ClienteIntercicloVO>();
        ClienteIntercicloVO integranteIC = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integranteIC = new ClienteIntercicloVO();
                integranteIC.setNumGrupo(rs.getInt("in_numgrupo")); 
                integranteIC.setNumCiclo(rs.getInt("in_numciclo"));
                integranteIC.setNumCliente(rs.getInt("in_numcliente"));
                integranteIC.setNumSolicitud(rs.getInt("in_numsolicitud"));
                integranteIC.setSemDispercion(rs.getInt("in_semDispersion"));
                integranteIC.setFechaCaptura(rs.getTimestamp("in_fechaAlta"));
                integranteIC.setFechaUltModificacion(rs.getTimestamp("in_fechaUltModificacion"));
                integranteIC.setEstatus(rs.getInt("in_estatus"));
                clientesIC.add(integranteIC);
            }
        } catch (SQLException sqle) {
            myLogger.error("getClientesInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getClientesInterCiclo", e);
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
        return clientesIC;
    }
    public  ClienteIntercicloVO getClienteInterCiclo(int idGrupo, int idCiclo, int idCliente, int idSolicitud) throws ClientesException {

        String query = "Select * from d_integrantes_interciclo" +
                       " where in_numgrupo = ?" +
                       " and in_numciclo = ? "+
                       " and  in_numcliente = ?"+
                       " and in_numSolicitud = ?"+
                       " and in_estatus = 1";

        
        ClienteIntercicloVO integranteIC = new  ClienteIntercicloVO();
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            ps.setInt(3, idCliente);
            ps.setInt(4, idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integranteIC = new ClienteIntercicloVO();
                integranteIC.setNumGrupo(rs.getInt("in_numgrupo")); 
                integranteIC.setNumCiclo(rs.getInt("in_numciclo"));
                integranteIC.setNumCliente(rs.getInt("in_numcliente"));
                integranteIC.setNumSolicitud(rs.getInt("in_numsolicitud"));
                integranteIC.setSemDispercion(rs.getInt("in_semDispersion"));
                integranteIC.setFechaCaptura(rs.getTimestamp("in_fechaAlta"));
                integranteIC.setFechaUltModificacion(rs.getTimestamp("in_fechaUltModificacion"));
                integranteIC.setEstatus(rs.getInt("in_estatus"));                
            }
        } catch (SQLException sqle) {
            myLogger.error("getClientesInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getClientesInterCiclo", e);
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
        return integranteIC;
    }
    
    public  ClienteIntercicloVO getClienteICporSemDisp(ClienteIntercicloVO clienteIC) throws ClientesException {

        String query = "Select * from d_integrantes_interciclo" +
                       " where in_numgrupo = ?" +
                       " and in_numciclo = ? "+
                       " and  in_numcliente = ?"+
                       " and in_numSolicitud = ?"+
                       " and in_semDispersion = ?";
        ClienteIntercicloVO integranteIC = new  ClienteIntercicloVO();
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, clienteIC.getNumGrupo());
            ps.setInt(2, clienteIC.getNumCiclo());
            ps.setInt(3, clienteIC.getNumCliente());
            ps.setInt(4, clienteIC.getNumSolicitud());
            ps.setInt(5, clienteIC.getSemDispercion());
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integranteIC = new ClienteIntercicloVO();
                integranteIC.setNumGrupo(rs.getInt("in_numgrupo")); 
                integranteIC.setNumCiclo(rs.getInt("in_numciclo"));
                integranteIC.setNumCliente(rs.getInt("in_numcliente"));
                integranteIC.setNumSolicitud(rs.getInt("in_numsolicitud"));
                integranteIC.setSemDispercion(rs.getInt("in_semDispersion"));
                integranteIC.setFechaCaptura(rs.getTimestamp("in_fechaAlta"));
                integranteIC.setFechaUltModificacion(rs.getTimestamp("in_fechaUltModificacion"));
                integranteIC.setEstatus(rs.getInt("in_estatus"));                
            }
        } catch (SQLException sqle) {
            myLogger.error("getClientesInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getClientesInterCiclo", e);
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
        return integranteIC;
    }
    
    public  ClienteIntercicloVO getClienteInterCicloPorSolicitud(SolicitudVO solicitud) throws ClientesException {

        String query = "Select * from d_integrantes_interciclo" +
                       " where in_numcliente = ?"+
                       " and in_numSolicitud = ?"+
                       " and in_estatus = 1";

        
        ClienteIntercicloVO integranteIC = new  ClienteIntercicloVO();
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, solicitud.idCliente);
            ps.setInt(2, solicitud.idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integranteIC = new ClienteIntercicloVO();
                integranteIC.setNumGrupo(rs.getInt("in_numgrupo")); 
                integranteIC.setNumCiclo(rs.getInt("in_numciclo"));
                integranteIC.setNumCliente(rs.getInt("in_numcliente"));
                integranteIC.setNumSolicitud(rs.getInt("in_numsolicitud"));
                integranteIC.setSemDispercion(rs.getInt("in_semDispersion"));
                integranteIC.setFechaCaptura(rs.getTimestamp("in_fechaAlta"));
                integranteIC.setFechaUltModificacion(rs.getTimestamp("in_fechaUltModificacion"));
                integranteIC.setEstatus(rs.getInt("in_estatus"));                
            }
        } catch (SQLException sqle) {
            myLogger.error("getClientesInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getClientesInterCiclo", e);
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
        return integranteIC;
    }
}

package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.AuditoresVO;
import com.sicap.clientes.vo.PaynetVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class AuditoresDAO extends DAOMaster{
    
    private static Logger myLogger = Logger.getLogger(PaynetDAO.class);
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    
    public boolean findAuditorRFC(String rfc) throws ClientesDBException, NamingException{
        
        boolean listo = false;
        query = "SELECT au_rfc FROM c_auditores WHERE LEFT(au_rfc,10)=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, rfc);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if (res.next())
                listo = true;
        } catch (SQLException e) {
            myLogger.error("Erro en findAuditorRFC", e);
            return listo = false;
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return listo;
    }
    
    public ArrayList<AuditoresVO> findAuditor(AuditoresVO auditor) throws ClientesDBException, NamingException{
        
        ArrayList<AuditoresVO> arrAuditor = new ArrayList<AuditoresVO>();
        query = "SELECT au_numauditor, au_nombre, au_apaterno, au_amaterno, au_rfc, au_estatus FROM c_auditores WHERE ";
        try {
            con = getConnection();
            if(auditor.getNumAuditor()>0){
                query += "au_numauditor= ?";
                pstm = con.prepareStatement(query);
                pstm.setInt(1, auditor.getNumAuditor());
            } else if(!auditor.getRfc().equals("")){
                query += "au_rfc LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getRfc()+"%");
            } else if(!auditor.getNombre().equals("") && auditor.getApPaterno().equals("") && auditor.getApMaterno().equals("")){
                query += "au_nombre LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getNombre()+"%");
            } else if(auditor.getNombre().equals("") && !auditor.getApPaterno().equals("") && auditor.getApMaterno().equals("")){
                query += "au_apaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getApPaterno()+"%");
            } else if(auditor.getNombre().equals("") && auditor.getApPaterno().equals("") && !auditor.getApMaterno().equals("")){
                query += "au_amaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getApMaterno()+"%");
            } else if(!auditor.getNombre().equals("") && !auditor.getApPaterno().equals("") && auditor.getApMaterno().equals("")){
                query += "au_nombre LIKE ? AND au_apaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getNombre()+"%");
                pstm.setString(2, "%"+auditor.getApPaterno()+"%");
            } else if(auditor.getNombre().equals("") && !auditor.getApPaterno().equals("") && !auditor.getApMaterno().equals("")){
                query += "au_apaterno LIKE ? AND au_amaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getApPaterno()+"%");
                pstm.setString(2, "%"+auditor.getApMaterno()+"%");
            } else if(!auditor.getNombre().equals("") && auditor.getApPaterno().equals("") && !auditor.getApMaterno().equals("")){
                query += "au_nombre LIKE ? AND au_amaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getNombre()+"%");
                pstm.setString(2, "%"+auditor.getApMaterno()+"%");
            } else if(!auditor.getNombre().equals("") && !auditor.getApPaterno().equals("") && !auditor.getApMaterno().equals("")){
                query += "au_nombre LIKE ? AND au_apaterno LIKE ? AND au_amaterno LIKE ?";
                pstm = con.prepareStatement(query);
                pstm.setString(1, "%"+auditor.getNombre()+"%");
                pstm.setString(2, "%"+auditor.getApPaterno()+"%");
                pstm.setString(3, "%"+auditor.getApMaterno()+"%");
            }
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                arrAuditor.add(new AuditoresVO(res.getInt("au_numauditor"), res.getString("au_nombre"), res.getString("au_apaterno"), res.getString("au_amaterno"), res.getString("au_rfc"), res.getInt("au_estatus")));
        } catch (SQLException e) {
            myLogger.error("Erro en findAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrAuditor;
    }
    
    public AuditoresVO findAuditorID(int idAuditor) throws ClientesDBException, NamingException{
        
        AuditoresVO auditor = null;
        query = "SELECT * FROM c_auditores WHERE au_numauditor= ?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idAuditor);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                auditor = new AuditoresVO(res.getInt("au_numauditor"), res.getString("au_nombre"), res.getString("au_apaterno"), res.getString("au_amaterno"), 
                        res.getString("au_rfc"), res.getInt("au_estatus"), res.getTimestamp("au_fechaalta"));
        } catch (SQLException e) {
            myLogger.error("Erro en findAuditorID", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return auditor;
    }
    
    public ArrayList<AuditoresVO> findSucursalesAuditor(int idAuditor, int estatus) throws ClientesDBException, NamingException{
        
        ArrayList<AuditoresVO> arrSucursales = new ArrayList<AuditoresVO>();
        query = "SELECT as_numsucursal FROM c_auditor_sucursal WHERE as_numauditor=? AND as_estatus=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idAuditor);
            pstm.setInt(2, estatus);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                arrSucursales.add(new AuditoresVO(res.getInt("as_numsucursal")));
        } catch (SQLException e) {
            myLogger.error("Erro en findAuditorID", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrSucursales;
    }
    
    public boolean buscaSucursalAsignada(int idSucursal, int idAuditor) throws ClientesDBException, NamingException{
        
        boolean asignada = false;
        query = "SELECT as_numauditor FROM c_auditor_sucursal WHERE as_numsucursal=? AND as_numauditor!=? AND as_estatus=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idSucursal);
            pstm.setInt(2, idAuditor);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if(res.next())
                asignada = true;
        } catch (SQLException e) {
            myLogger.error("Erro en buscaSucursalAsignada", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return asignada;
    }
    
    public boolean buscaSucursalAuditor(int idSucursal, int idAuditor) throws ClientesDBException, NamingException{
        
        boolean asignada = false;
        query = "SELECT as_numauditor FROM c_auditor_sucursal WHERE as_numsucursal=? AND as_numauditor=? AND as_estatus=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idSucursal);
            pstm.setInt(2, idAuditor);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if(res.next())
                asignada = true;
        } catch (SQLException e) {
            myLogger.error("Erro en buscaSucursalAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return asignada;
    }
    
    public int buscaSucursal(int idSucursal) throws ClientesDBException, NamingException{
        
        int idAuditor = 0;
        query = "SELECT as_numauditor FROM c_auditor_sucursal WHERE as_numsucursal=? AND as_estatus=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idSucursal);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if(res.next())
                idAuditor = res.getInt("as_numauditor");
        } catch (SQLException e) {
            myLogger.error("Erro en buscaSucursal", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return idAuditor;
    }
    
    public int insertAuditor(AuditoresVO auditor) throws ClientesDBException, NamingException{
        
        int idAuditor = 0;
        query = "INSERT INTO c_auditores(au_nombre, au_apaterno, au_amaterno, au_rfc, au_estatus, au_fechaalta) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, auditor.getNombre());
            pstm.setString(2, auditor.getApPaterno());
            pstm.setString(3, auditor.getApMaterno());
            pstm.setString(4, auditor.getRfc());
            pstm.setInt(5, auditor.getEstatus());
            pstm.setTimestamp(6, Convertidor.toSqlTimeStamp(new Date()));
            myLogger.debug("pstm "+pstm);
            pstm.executeUpdate();
            res = pstm.getGeneratedKeys();
            if (res.next())
                idAuditor = res.getInt(1);
        } catch (SQLException e) {
            myLogger.error("Erro en insertAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return idAuditor;
    }
    
    public boolean insertSucursalAuditor(int idAuditor, int idSucursal) throws ClientesDBException, NamingException{
        
        boolean insert = false;
        query = "INSERT INTO c_auditor_sucursal(as_numauditor,as_numsucursal,as_estatus,as_fechaalta) VALUES (?, ?, 1, ?);";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idAuditor);
            pstm.setInt(2, idSucursal);
            pstm.setTimestamp(3, Convertidor.toSqlTimeStamp(new Date()));
            myLogger.debug("pstm "+pstm);
            if(pstm.executeUpdate() == 1)
                insert = true;
        } catch (SQLException e) {
            myLogger.error("Erro en insertSucursalAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return insert;
    }
    
    public int updateAuditor(AuditoresVO auditor, boolean baja) throws ClientesDBException, NamingException{
        
        int param = 0;
        int res = 0;
        query = "UPDATE c_auditores SET au_nombre=?,au_apaterno=?,au_amaterno=?,au_rfc=?,au_estatus=? ";
        if(baja)
            query += ",au_fechabaja=? ";
        else
            query += ",au_fechaalta=? ";
        query +="WHERE au_numauditor=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(++param, auditor.getNombre());
            pstm.setString(++param, auditor.getApPaterno());
            pstm.setString(++param, auditor.getApMaterno());
            pstm.setString(++param, auditor.getRfc());
            pstm.setInt(++param, auditor.getEstatus());
            if(baja)
                pstm.setTimestamp(++param, auditor.getFechaBaja());
            else
                pstm.setTimestamp(++param, auditor.getFechaAlta());
            pstm.setInt(++param, auditor.getNumAuditor());
            myLogger.debug("pstm "+pstm);
            res = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }
    
    public int updateBajaSucursalAsig(AuditoresVO auditor) throws ClientesDBException, NamingException{
        
        int res = 0;
        query = "UPDATE c_auditor_sucursal SET as_estatus=0,as_fechabaja=? WHERE as_numauditor=? AND as_numsucursal=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setTimestamp(1, Convertidor.toSqlTimeStamp(new Date()));
            pstm.setInt(2, auditor.getNumAuditor());
            pstm.setInt(3, auditor.getIdSucursal());
            myLogger.debug("pstm "+pstm);
            res = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateBajaSucursalAsig", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }
    
    public int updateBajaAuditor(AuditoresVO auditor) throws ClientesDBException, NamingException{
        
        int res = 0;
        query = "UPDATE c_auditores SET au_estatus=0,au_fechabaja=? where au_numauditor=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setTimestamp(1, Convertidor.toSqlTimeStamp(new Date()));
            pstm.setInt(2, auditor.getNumAuditor());
            myLogger.debug("pstm "+pstm);
            res = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en updateBajaAuditor", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return res;
    }
    
}

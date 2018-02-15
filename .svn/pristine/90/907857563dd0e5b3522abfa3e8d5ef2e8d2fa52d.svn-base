/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.AseguradosVO;
import com.sicap.clientes.vo.SegurosVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex
 */
public class AseguradosDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(AseguradosDAO.class);

    public void addAsegurados(Connection con, Savepoint save, SegurosVO seguros, AseguradosVO asegurado) throws ClientesException {

        String query = "INSERT INTO d_asegurados (as_numcliente, as_numsolicitud, as_numseguro, as_numasegurado, as_nombre, as_apaterno, as_amaterno, "
                + "as_relacion, as_fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //Connection con = null;
        PreparedStatement ps = null;
        try {
            //con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, seguros.idCliente);
            ps.setInt(2, seguros.idSolicitud);
            ps.setInt(3, seguros.numSeguro);
            ps.setInt(4, asegurado.getNumAsegurado());
            ps.setString(5, asegurado.getNombre());
            ps.setString(6, asegurado.getApPaterno());
            ps.setString(7, asegurado.getApMaterno());
            ps.setInt(8, asegurado.getParentesco());
            ps.setString(9, asegurado.getFecNacimiento());
            //System.out.println(ps);            
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + seguros.idCliente + ", " + seguros.idSolicitud + ", " + seguros.numSeguro + ", " + asegurado.getNombre()
                    + ", " + asegurado.getApPaterno() + ", " + asegurado.getApMaterno() + ", " + asegurado.getParentesco() + ", " + asegurado.getFecNacimiento() + "]");

            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("addAsegurados", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addAsegurados", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
         try {
         if (con != null)
         con.close();
         } catch (SQLException sqle) {
         throw new ClientesDBException(sqle.getMessage());
         }
         }*/

    }

    public ArrayList<AseguradosVO> getAsegurados(int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT as_nombre,as_apaterno,as_amaterno,as_fecha_nacimiento,as_relacion FROM d_asegurados WHERE as_numcliente=? AND as_numsolicitud=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<AseguradosVO> asegurados = new ArrayList<AseguradosVO>();
        String fecNacimiento = "";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idCliente + ", " + idSolicitud + "]");

            res = ps.executeQuery();
            while (res.next()) {
                fecNacimiento = res.getString("as_fecha_nacimiento").substring(8, 10) + "/" + res.getString("as_fecha_nacimiento").substring(5, 7) + "/" + res.getString("as_fecha_nacimiento").substring(0, 4);
                asegurados.add(new AseguradosVO(res.getString("as_nombre"), res.getString("as_apaterno"), res.getString("as_amaterno"), fecNacimiento, res.getInt("as_relacion")));
            }

        } catch (SQLException sqle) {
            myLogger.error("getAsegurados", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getAsegurados", e);
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
        return asegurados;
    }

    public void deleteAsegurados(Connection con, Savepoint save, SegurosVO seguros) throws ClientesException {

        String query = "DELETE FROM d_asegurados WHERE as_numcliente=? AND as_numsolicitud=? AND as_numseguro=?";
        //Connection con = null;
        PreparedStatement ps = null;
        try {
            //con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, seguros.idCliente);
            ps.setInt(2, seguros.idSolicitud);
            ps.setInt(3, seguros.numSeguro);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + seguros.idCliente + ", " + seguros.idSolicitud + ", " + seguros.numSeguro + "]");

            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("deleteAsegurados", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteAsegurados", e);
            throw new ClientesException(e.getMessage());
        } /*finally {
         try {
         if (con != null)
         con.close();
         } catch (SQLException sqle) {
         throw new ClientesDBException(sqle.getMessage());
         }
         }*/

    }
}

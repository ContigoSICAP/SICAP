/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.UsuarioMovilVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author jmerino
 */
public class UsuarioMovilDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(UsuarioMovilDAO.class);

    public UsuarioMovilVO validaUsuarioPassword(String usuario, String password) throws ClientesException {

        UsuarioMovilVO usuariovo = null;
        Connection cn = null;
        String query = "SELECT * FROM d_usuarios_movil WHERE um_usuario = ? and um_password = ?";

        try {
            cn = getConnection();
            //cn = getUSRConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, password);
            myLogger.debug("query = " + query);
            myLogger.debug("usuario = " + usuario);
            myLogger.debug("password = " + password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuariovo = new UsuarioMovilVO();
                usuariovo.setIdUsuario(rs.getString("um_numusuario"));
                usuariovo.setIdEjecutivo(rs.getInt("um_numejecutivo"));
                usuariovo.setUsuario(rs.getString("um_usuario"));
                usuariovo.setPassword(rs.getString("um_password"));
            }
        } catch (SQLException sqle) {
            myLogger.error("validaUsuarioPassword", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("validaUsuarioPassword", e);
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

        return usuariovo;
    }


    public UsuarioMovilVO getUsuario(String usuario) throws ClientesException {

        UsuarioMovilVO usuariovo = null;
        Connection cn = null;
        String query = "SELECT * FROM d_usuarios_movil WHERE um_usuario = ?";

        try {
            cn = getConnection();
            //cn = getUSRConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, usuario);
            myLogger.debug("query = " + query);
            myLogger.debug("usuario = " + usuario);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                myLogger.debug("Encontró el usuario");
                usuariovo = new UsuarioMovilVO();
                usuariovo.setIdUsuario(rs.getString("um_numusuario"));
                usuariovo.setIdEjecutivo(rs.getInt("um_numejecutivo"));
                usuariovo.setUsuario(rs.getString("um_usuario"));
                usuariovo.setPassword(rs.getString("um_password"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getUsuario", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getUsuario", e);
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

        return usuariovo;
    }



}

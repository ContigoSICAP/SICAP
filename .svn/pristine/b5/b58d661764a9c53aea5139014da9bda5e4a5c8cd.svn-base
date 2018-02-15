package com.sicap.clientes.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {

    public static Connection getMySQLConnection() throws NamingException, SQLException {
        
        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource) ictx.lookup(ClientesConstants.MYSQL_DS);
        return ds.getConnection();

    }

    public static Connection getCWConnection() throws NamingException, SQLException {

        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource) ictx.lookup(ClientesConstants.MYSQL_CW);
        return ds.getConnection();

    }
    
    public static Connection getMigConnection() throws NamingException, SQLException {

        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource) ictx.lookup(ClientesConstants.MYSQL_MIG);
        return ds.getConnection();

    }
    
    public static Connection getMigCredexConnection() throws NamingException, SQLException {

        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource) ictx.lookup(ClientesConstants.MYSQL_MIG_CREDEX);
        return ds.getConnection();

    }
    
    public static Connection getTCIConnection() throws NamingException, SQLException {
        
        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource)ictx.lookup( ClientesConstants.MYSQL_TCI);
        return ds.getConnection();
    
    }
    
    public static Connection getODSConnection() throws NamingException, SQLException {
        
        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource)ictx.lookup( ClientesConstants.MYSQL_ODS);
        return ds.getConnection();
    
    }
    
    public static Connection getCODSConnection() throws NamingException, SQLException {
        
        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource)ictx.lookup( ClientesConstants.MYSQL_CODS);
        return ds.getConnection();
    
    }
}

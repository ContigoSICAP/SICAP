package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.sicap.clientes.util.ConnectionManager;


public class DAOMaster {


	public final Connection getConnection() throws NamingException, SQLException {
		return ConnectionManager.getMySQLConnection();
	}

	public final Connection getCWConnection() throws NamingException, SQLException {
		return ConnectionManager.getCWConnection();
	}
        
	public final Connection getMigConnection() throws NamingException, SQLException {
		return ConnectionManager.getMigConnection();
	}
        
	public final Connection getMigCredexConnection() throws NamingException, SQLException {
		return ConnectionManager.getMigCredexConnection();
	}
        
        public final Connection getTCIConnection() throws NamingException, SQLException {
                return ConnectionManager.getTCIConnection();
        }

        public final Connection getODSConnection() throws NamingException, SQLException {
		return ConnectionManager.getODSConnection();
	}
        
        public final Connection getCODSConnection() throws NamingException, SQLException {
                return ConnectionManager.getCODSConnection();
        }
}
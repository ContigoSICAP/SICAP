package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ConyugeVO;


public class ConyugeDAO extends DAOMaster{


	public ConyugeVO getConyuge(int idCliente) throws ClientesException{
		ConyugeVO  conyuge = null;
		Connection cn = null;
		String query =	"SELECT CN_FECHA_EVALUACION, CN_NOMBRE, CN_APATERNO, CN_AMATERNO, CN_DIRECCION_DOMICILIO, CN_TELEFONO_DOMICILIO, "+
						"CN_DIRECCION_TRABAJO, CN_TELEFONO_TRABAJO, CN_TELEFONO_CELULAR, CN_FECHA_CAPTURA, CN_RFC, CN_CURP, CN_FECHA_NAC, " +
						"CN_SEXO, CN_FORMA_INGRESO, CN_TIPO_SECTOR, CN_DEPENDENCIA, CN_SUELDO_MENSUAL FROM D_CONYUGES WHERE CN_NUMCLIENTE = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				conyuge =  new ConyugeVO();
				conyuge.idCliente = idCliente;
				conyuge.fechaEvaluacion = rs.getDate("CN_FECHA_EVALUACION");
				conyuge.nombre = rs.getString("CN_NOMBRE");
				conyuge.aPaterno = rs.getString("CN_APATERNO");
				conyuge.aMaterno = rs.getString("CN_AMATERNO");
				conyuge.direccionDomicilio = rs.getString("CN_DIRECCION_DOMICILIO");
				conyuge.telefonoDomicilio = rs.getString("CN_TELEFONO_DOMICILIO");
				conyuge.direccionTrabajo = rs.getString("CN_DIRECCION_TRABAJO");
				conyuge.telefonoTrabajo = rs.getString("CN_TELEFONO_TRABAJO");
				conyuge.telefonoCelular = rs.getString("CN_TELEFONO_CELULAR");
				conyuge.fechaCaptura = rs.getTimestamp("CN_FECHA_CAPTURA");
				conyuge.rfc = rs.getString("CN_RFC");
				conyuge.curp = rs.getString("CN_CURP");
				conyuge.fechaNacimiento = rs.getDate("CN_FECHA_NAC");
				conyuge.sexo = rs.getInt("CN_SEXO");
				conyuge.formaIngreso = rs.getInt("CN_FORMA_INGRESO");
				conyuge.tipoSector = rs.getInt("CN_TIPO_SECTOR");
				conyuge.dependencia = rs.getInt("CN_DEPENDENCIA");
				conyuge.sueldoMensual = rs.getDouble("CN_SUELDO_MENSUAL");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getConyuge : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getConyuge : "+e.getMessage());
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
		return conyuge;
	}




	public int addConyuge(int idCliente, ConyugeVO conyuge) throws ClientesException{

		String query =	"INSERT INTO D_CONYUGES (CN_NUMCLIENTE, CN_FECHA_EVALUACION, CN_NOMBRE, CN_APATERNO, CN_AMATERNO, "+
						"CN_DIRECCION_DOMICILIO, CN_TELEFONO_DOMICILIO, CN_DIRECCION_TRABAJO, CN_TELEFONO_TRABAJO, "+
						"CN_TELEFONO_CELULAR, CN_FECHA_CAPTURA, CN_RFC, CN_CURP, CN_FECHA_NAC, CN_SEXO, CN_FORMA_INGRESO, " +
						"CN_TIPO_SECTOR, CN_DEPENDENCIA, CN_SUELDO_MENSUAL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idCliente);
			ps.setDate(param++, conyuge.fechaEvaluacion);
			ps.setString(param++, conyuge.nombre);
			ps.setString(param++, conyuge.aPaterno);
			ps.setString(param++, conyuge.aMaterno);
			ps.setString(param++, conyuge.direccionDomicilio);
			ps.setString(param++, conyuge.telefonoDomicilio);
			ps.setString(param++, conyuge.direccionTrabajo);
			ps.setString(param++, conyuge.telefonoTrabajo);
			ps.setString(param++, conyuge.telefonoCelular);
			ps.setTimestamp(param++, conyuge.fechaCaptura);
			ps.setString(param++, conyuge.rfc);
			ps.setString(param++, conyuge.curp);
			ps.setDate(param++, conyuge.fechaNacimiento);
			ps.setInt(param++, conyuge.sexo);
			ps.setInt(param++, conyuge.formaIngreso);
			ps.setInt(param++, conyuge.tipoSector);
			ps.setInt(param++, conyuge.dependencia);
			ps.setDouble(param++, conyuge.sueldoMensual);
			
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Conyuge= "+conyuge.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addConyuge : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addConyuge : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		return res;
	}



//UPDATE D_CONYUGES SET CN_FECHA_EVALUACION = ?, CN_NOMBRE = ?, CN_APATERNO = ?, CN_AMATERNO = ?, CN_DIRECCION_DOMICILIO = ?, CN_TELEFONO_DOMICILIO = ?, CN_DIRECCION_TRABAJO = ?, CN_TELEFONO_TRABAJO = ?, CN_TELEFONO_CELULAR = ? WHERE CN_NUMCLIENTE = ? 

	public int updateConyuge (int idCliente, ConyugeVO conyuge) throws ClientesException{

		String query =	"UPDATE D_CONYUGES SET CN_FECHA_EVALUACION = ?, CN_NOMBRE = ?, CN_APATERNO = ?, "+
						"CN_AMATERNO = ?, CN_DIRECCION_DOMICILIO = ?, CN_TELEFONO_DOMICILIO = ?, CN_DIRECCION_TRABAJO = ?, "+
						"CN_TELEFONO_TRABAJO = ?, CN_TELEFONO_CELULAR = ?, CN_RFC = ?, CN_CURP = ?, CN_FECHA_NAC = ?, CN_SEXO = ?, " +
						"CN_FORMA_INGRESO = ?, CN_TIPO_SECTOR = ?, CN_DEPENDENCIA = ?, CN_SUELDO_MENSUAL = ? WHERE CN_NUMCLIENTE = ?";
		
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setDate(param++, conyuge.fechaEvaluacion);
			ps.setString(param++, conyuge.nombre);
			ps.setString(param++, conyuge.aPaterno);
			ps.setString(param++, conyuge.aMaterno);
			ps.setString(param++, conyuge.direccionDomicilio);
			ps.setString(param++, conyuge.telefonoDomicilio);
			ps.setString(param++, conyuge.direccionTrabajo);
			ps.setString(param++, conyuge.telefonoTrabajo);
			ps.setString(param++, conyuge.telefonoCelular);
			ps.setString(param++, conyuge.rfc);
			ps.setString(param++, conyuge.curp);
			ps.setDate(param++, conyuge.fechaNacimiento);
			ps.setInt(param++, conyuge.sexo);
			ps.setInt(param++, conyuge.formaIngreso);
			ps.setInt(param++, conyuge.tipoSector);
			ps.setInt(param++, conyuge.dependencia);
			ps.setDouble(param++, conyuge.sueldoMensual);
			
			ps.setInt(param++, idCliente);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Conyuge = "+conyuge.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateConyuge : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateConyuge : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		return res;
	}


}
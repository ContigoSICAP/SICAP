package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.EconomiaObligadoVO;



public class EconomiaObligadoDAO extends DAOMaster{



//EC_NUMCLIENTE, EC_NUMSOLICITUD, EC_NUMOBLIGADO, EC_OCUPACION, EC_TIPO_INGRESO, EC_EMPRESA, EC_TIPO_CONTRATO, EC_SALARIO, EC_PASIVOS_FAMILIARES, EC_ACTIVOS_FAMILIARES, EC_INGRESOS_FAMILIARES, EC_GASTOS, EC_FECHA_CAPTURA

	
	public EconomiaObligadoVO getEconomiaObligado(int idCliente, int idSolicitud, int idObligado) throws ClientesException{
		EconomiaObligadoVO credito = null;
		Connection cn = null;
		String query = "SELECT EC_NUMCLIENTE, EC_NUMSOLICITUD, EC_NUMOBLIGADO, EC_OCUPACION, EC_FRECUENCIA_INGRESOS, EC_EMPRESA, "+
						"EC_TIPO_CONTRATO, EC_SALARIO, EC_PASIVOS_FAMILIARES, EC_ACTIVOS_FAMILIARES, EC_INGRESOS_FAMILIARES, EC_GASTOS_FAMILIARES, "+
						"EC_FECHA_CAPTURA FROM D_ECONOMIA_OBLIGADO_SOLIDARIO WHERE EC_NUMCLIENTE = ? AND EC_NUMSOLICITUD = ? "+
						"AND EC_NUMOBLIGADO = ?";
		try{
			cn = this.getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			ps.setInt(3, idObligado);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				credito = new EconomiaObligadoVO();
				credito.idCliente = idCliente;
				credito.idSolicitud = idSolicitud;
				credito.idObligado = idObligado;
				credito.ocupacion = rs.getInt("EC_OCUPACION");;
				credito.frecuenciaIngresos = rs.getInt("EC_FRECUENCIA_INGRESOS");
				credito.empresa = rs.getString("EC_EMPRESA");
				credito.tipoContrato = rs.getInt("EC_TIPO_CONTRATO");
				credito.salario = rs.getDouble("EC_SALARIO");
				credito.pasivosFamiliares = rs.getDouble("EC_PASIVOS_FAMILIARES");;
				credito.activosFamiliares = rs.getDouble("EC_ACTIVOS_FAMILIARES");;
				credito.ingresosFamiliares = rs.getDouble("EC_INGRESOS_FAMILIARES");;
				credito.gastosFamiliares = rs.getDouble("EC_GASTOS_FAMILIARES");;
				credito.fechaCaptura = rs.getTimestamp("EC_FECHA_CAPTURA");

				Logger.debug("Informacion credito = "+credito);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getEconomiaObligado : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getEconomiaObligado : "+e.getMessage());
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
		return credito;
	}


//INSERT INTO D_ECONOMIA_OBLIGADO_SOLIDARIO (EC_NUMCLIENTE, EC_NUMSOLICITUD, EC_NUMOBLIGADO, EC_OCUPACION, EC_FRECUENCIA_INGRESOS, EC_EMPRESA, EC_TIPO_CONTRATO, EC_SALARIO, EC_PASIVOS_FAMILIARES, EC_ACTIVOS_FAMILIARES, EC_INGRESOS_FAMILIARES, EC_GASTOS_FAMILIARES, EC_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

	public int addEconomiaObligado(int idCliente, int idSolicitud, int idObligado, EconomiaObligadoVO economia) throws ClientesException{
		return addEconomiaObligado(null, idCliente, idSolicitud, idObligado, economia);
	}

	public int addEconomiaObligado(Connection conn, int idCliente, int idSolicitud, int idObligado, EconomiaObligadoVO economia) throws ClientesException{

		String query =	"INSERT INTO D_ECONOMIA_OBLIGADO_SOLIDARIO (EC_NUMCLIENTE, EC_NUMSOLICITUD, EC_NUMOBLIGADO, "+
						"EC_OCUPACION, EC_FRECUENCIA_INGRESOS, EC_EMPRESA, EC_TIPO_CONTRATO, EC_SALARIO, EC_PASIVOS_FAMILIARES, "+
						"EC_ACTIVOS_FAMILIARES, EC_INGRESOS_FAMILIARES, EC_GASTOS_FAMILIARES, EC_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection cn = null;
		int  res = 0;
		try{
			PreparedStatement ps = null;
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			int param = 1; 
			ps.setInt(param++, economia.idCliente);
			ps.setInt(param++, economia.idSolicitud);
			ps.setInt(param++, economia.idObligado);
			ps.setInt(param++, economia.ocupacion);
			ps.setInt(param++, economia.frecuenciaIngresos);
			ps.setString(param++, economia.empresa);
			ps.setInt(param++, economia.tipoContrato);
			ps.setDouble(param++, economia.salario);
			ps.setDouble(param++, economia.pasivosFamiliares);
			ps.setDouble(param++, economia.activosFamiliares);
			ps.setDouble(param++, economia.ingresosFamiliares);
			ps.setDouble(param++, economia.gastosFamiliares);
			ps.setTimestamp(param++, economia.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("EconomiaObligadoVO= "+economia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addEconomiaObligado : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addEconomiaObligado : "+e.getMessage());
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


//UPDATE D_ECONOMIA_OBLIGADO_SOLIDARIO SET EC_OCUPACION = ?, EC_FRECUENCIA_INGRESOS = ?, EC_EMPRESA = ?, EC_TIPO_CONTRATO = ?, EC_SALARIO = ?, EC_PASIVOS_FAMILIARES = ?, EC_ACTIVOS_FAMILIARES = ?, EC_INGRESOS_FAMILIARES = ?, EC_GASTOS_FAMILIARES = ? WHERE EC_NUMCLIENTE = ? AND EC_NUMSOLICITUD = ? AND EC_NUMOBLIGADO = ? 


	public int updateEconomiaObligado(int idCliente, int idSolicitud, int idObligado, EconomiaObligadoVO economia) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_ECONOMIA_OBLIGADO_SOLIDARIO SET EC_OCUPACION = ?, EC_FRECUENCIA_INGRESOS = ?, "+
						"EC_EMPRESA = ?, EC_TIPO_CONTRATO = ?, EC_SALARIO = ?, EC_PASIVOS_FAMILIARES = ?, "+
						"EC_ACTIVOS_FAMILIARES = ?, EC_INGRESOS_FAMILIARES = ?, EC_GASTOS_FAMILIARES = ? WHERE EC_NUMCLIENTE = ? "+
						"AND EC_NUMSOLICITUD = ? AND EC_NUMOBLIGADO = ?";
		Connection cn = null;
		try{
			PreparedStatement ps =null;

			cn = getConnection();
			ps =cn.prepareStatement(query); 
			int param = 1; 
			ps.setInt(param++, economia.ocupacion);
			ps.setInt(param++, economia.frecuenciaIngresos);
			ps.setString(param++, economia.empresa);
			ps.setInt(param++, economia.tipoContrato);
			ps.setDouble(param++, economia.salario);
			ps.setDouble(param++, economia.pasivosFamiliares);
			ps.setDouble(param++, economia.activosFamiliares);
			ps.setDouble(param++, economia.ingresosFamiliares);
			ps.setDouble(param++, economia.gastosFamiliares);
			ps.setInt(param++, economia.idCliente);
			ps.setInt(param++, economia.idSolicitud);
			ps.setInt(param++, economia.idObligado);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("EconomiaObligadoVO = "+economia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateEconomiaObligado : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateEconomiaObligado : "+e.getMessage());
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
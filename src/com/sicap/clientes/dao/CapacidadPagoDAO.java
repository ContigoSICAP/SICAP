package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CapacidadPagoVO;


public class CapacidadPagoDAO extends DAOMaster{

//SELECT CP_NUMCLIENTE, CP_NUMSOLICITUD, CP_ESTATUS, CP_ING_NOMINA, CP_ING_NO_COMPROBABLES, CP_OTROS_INGRESOS, CP_RENTA_VIVIENDA, CP_PAGO_DEUDA, CP_OTROS_GASTOS, CP_DISPONIBLE_MENSUAL, CP_CUOTA_DISPONIBLE, CP_CUOTA_INGRESO_BRUTO, CP_FECHA_CAPTURA FROM D_CAPACIDAD_PAGO

	public CapacidadPagoVO getCapacidadPago(int idCliente, int idSolicitud) throws ClientesException{
		CapacidadPagoVO capacidad = null;
		Connection cn = null;
		String query =	"SELECT CP_NUMCLIENTE, CP_NUMSOLICITUD, CP_ESTATUS, CP_ING_NOMINA, CP_ING_NO_COMPROBABLES, CP_FTE_OTROS_INGRESOS, "+
						"CP_OTROS_INGRESOS, CP_MARCA_MODELO_AUTO, CP_ESTATUS_AUTO, CP_VALOR_AUTO, CP_RENTA_VIVIENDA, CP_PAGO_DEUDA, CP_OTROS_GASTOS, CP_DISPONIBLE_MENSUAL, "+
						"CP_CUOTA_DISPONIBLE, CP_CUOTA_INGRESO_BRUTO, CP_FECHA_CAPTURA FROM D_CAPACIDAD_PAGO "+
						"WHERE CP_NUMCLIENTE = ? AND CP_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				capacidad =  new CapacidadPagoVO();
				capacidad.idCliente = idCliente;
				capacidad.idSolicitud = idSolicitud;
				capacidad.estatus = rs.getInt("CP_ESTATUS");
				capacidad.ingresosNomina = rs.getDouble("CP_ING_NOMINA");
				capacidad.otrosNoComprobables = rs.getDouble("CP_ING_NO_COMPROBABLES");
				capacidad.fuenteOtrosIngresos = rs.getInt("CP_FTE_OTROS_INGRESOS");
				capacidad.otrosIngresos = rs.getDouble("CP_OTROS_INGRESOS");
				capacidad.marcaModeloAuto = rs.getString("CP_MARCA_MODELO_AUTO");
				
				capacidad.estatusAuto = rs.getInt("CP_ESTATUS_AUTO");
				capacidad.valorAuto = rs.getDouble("CP_VALOR_AUTO");
				
				capacidad.rentaVivienda = rs.getDouble("CP_RENTA_VIVIENDA");
				capacidad.pagoDeuda = rs.getDouble("CP_PAGO_DEUDA");
				capacidad.otrosGastos = rs.getDouble("CP_OTROS_GASTOS");
				capacidad.disponibleMensual = rs.getDouble("CP_DISPONIBLE_MENSUAL");
				capacidad.cuotaSobreDisponible = rs.getInt("CP_CUOTA_DISPONIBLE");
				capacidad.cuotaSobreIngresoBruto = rs.getInt("CP_CUOTA_INGRESO_BRUTO");
				capacidad.fechaCaptura = rs.getTimestamp("CP_FECHA_CAPTURA");
				//Logger.debug("Capacidad : "+capacidad.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCapacidadPago : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCapacidadPago : "+e.getMessage());
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
		return capacidad;
	}

//INSERT INTO D_CAPACIDAD_PAGO (CP_NUMCLIENTE, CP_NUMSOLICITUD, CP_ESTATUS, CP_ING_NOMINA, CP_ING_NO_COMPROBABLES, CP_OTROS_INGRESOS, CP_RENTA_VIVIENDA, CP_PAGO_DEUDA, CP_OTROS_GASTOS, CP_DISPONIBLE_MENSUAL, CP_CUOTA_DISPONIBLE, CP_CUOTA_INGRESO_BRUTO, CP_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//cp_marca_modelo_auto, cp_estatus_auto, cp_valor_auto
	public int addCapacidadPago(int idCliente, int idSolicitud, CapacidadPagoVO capacidad) throws ClientesException{

		String query =	"INSERT INTO D_CAPACIDAD_PAGO (CP_NUMCLIENTE, CP_NUMSOLICITUD, CP_ESTATUS, CP_ING_NOMINA, "+
						"CP_ING_NO_COMPROBABLES, CP_FTE_OTROS_INGRESOS, CP_OTROS_INGRESOS, CP_MARCA_MODELO_AUTO, CP_ESTATUS_AUTO, CP_VALOR_AUTO, CP_RENTA_VIVIENDA, CP_PAGO_DEUDA, CP_OTROS_GASTOS, "+
						"CP_DISPONIBLE_MENSUAL, CP_CUOTA_DISPONIBLE, CP_CUOTA_INGRESO_BRUTO, CP_FECHA_CAPTURA) "+
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int  res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, capacidad.estatus);
			ps.setDouble(param++, capacidad.ingresosNomina);
			ps.setDouble(param++, capacidad.otrosNoComprobables);
			ps.setInt(param++, capacidad.fuenteOtrosIngresos);
			ps.setDouble(param++, capacidad.otrosIngresos);
			ps.setString(param++, capacidad.marcaModeloAuto);
			
			ps.setInt(param++, capacidad.estatusAuto);
			ps.setDouble(param++, capacidad.valorAuto);
			
			ps.setDouble(param++, capacidad.rentaVivienda);
			ps.setDouble(param++, capacidad.pagoDeuda);
			ps.setDouble(param++, capacidad.otrosGastos);
			ps.setDouble(param++, capacidad.disponibleMensual);
			ps.setInt(param++, capacidad.cuotaSobreDisponible);
			ps.setInt(param++, capacidad.cuotaSobreIngresoBruto);
			ps.setTimestamp(param++, capacidad.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Capacidad = "+capacidad.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addCapacidadPago : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addCapacidadPago : "+e.getMessage());
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



//UPDATE D_VIVIENDAS CP_ESTATUS = ?, CP_ING_NOMINA = ?, CP_ING_NO_COMPROBABLES = ?, CP_OTROS_INGRESOS = ?, CP_RENTA_VIVIENDA = ?, CP_PAGO_DEUDA = ?, CP_OTROS_GASTOS = ?, CP_DISPONIBLE_MENSUAL = ?, CP_CUOTA_DISPONIBLE = ?, CP_CUOTA_INGRESO_BRUTO = ? WHERE CP_NUMCLIENTE = ? AND CP_NUMSOLICITUD = ? 

	public int updateCapacidadPago (int idCliente, int idSolicitud, CapacidadPagoVO capacidad) throws ClientesException{

		String query =	"UPDATE D_CAPACIDAD_PAGO SET CP_ESTATUS = ?, CP_ING_NOMINA = ?, CP_ING_NO_COMPROBABLES = ?, "+
						"CP_FTE_OTROS_INGRESOS = ?, CP_OTROS_INGRESOS = ?, CP_MARCA_MODELO_AUTO = ?, CP_ESTATUS_AUTO = ?, CP_VALOR_AUTO = ?, CP_RENTA_VIVIENDA = ?, CP_PAGO_DEUDA = ?, CP_OTROS_GASTOS = ?, "+
						"CP_DISPONIBLE_MENSUAL = ?, CP_CUOTA_DISPONIBLE = ?, CP_CUOTA_INGRESO_BRUTO = ? "+
						"WHERE CP_NUMCLIENTE = ? AND CP_NUMSOLICITUD = ?";
		
		Connection cn = null;
		PreparedStatement ps = null;
		int  res = 0;
		int  param = 1;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, capacidad.estatus);
			ps.setDouble(param++, capacidad.ingresosNomina);
			ps.setDouble(param++, capacidad.otrosNoComprobables);
			ps.setInt(param++, capacidad.fuenteOtrosIngresos);
			ps.setDouble(param++, capacidad.otrosIngresos);
			ps.setString(param++, capacidad.marcaModeloAuto);
			
			ps.setInt(param++, capacidad.estatusAuto);
			ps.setDouble(param++, capacidad.valorAuto);
			
			ps.setDouble(param++, capacidad.rentaVivienda);
			ps.setDouble(param++, capacidad.pagoDeuda);
			ps.setDouble(param++, capacidad.otrosGastos);
			ps.setDouble(param++, capacidad.disponibleMensual);
			ps.setInt(param++, capacidad.cuotaSobreDisponible);
			ps.setInt(param++, capacidad.cuotaSobreIngresoBruto);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Capacidad = "+capacidad.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateCapacidadPago : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateCapacidadPago : "+e.getMessage());
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
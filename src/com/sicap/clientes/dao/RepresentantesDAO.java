package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.RepresentantesVO;

public class RepresentantesDAO extends DAOMaster{

	/**
	 * Listado de ejecutivos
	 * @return 
	 * @throws ClientesException
	 */
	public ArrayList<RepresentantesVO> getRepresentantes() throws ClientesException {
		
		String query = "SELECT * FROM C_REPRESENTANTES";
		
		ArrayList<RepresentantesVO> lista=new ArrayList<RepresentantesVO>();
		Connection cn = null;
		ResultSet res = null;
		
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeQuery();
			while (res.next()){
				RepresentantesVO representante = new RepresentantesVO();
				representante.idSucursal = res.getInt("RP_NUMSUCURSAL");
				representante.idRepresentante = res.getInt("RP_NUMEJECUTIVO");
				representante.nombre = res.getString("RP_NOMBRE");
				representante.estatus = res.getString("RP_STATUS");
				representante.factor = res.getFloat("RP_FACTOR");
				lista.add(representante);
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getEjecutivos: " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getEjecutivos: " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesException(sqle.getMessage());
			}
		}
		return lista;
	}
	
	/**
	 * Listado de ejecutivos con un estatus de (A)lta o (B)aja
	 * @return 
	 * @throws ClientesException
	 
	public ArrayList<EjecutivoCreditoVO> getEjecutivosSucursal(int idSucursal, String estatus) throws ClientesException {
		
		String query =	"SELECT EJ_NUMSUCURSAL, EJ_NUMEJECUTIVO, EJ_NOMBRE, EJ_APATERNO, EJ_AMATERNO, EJ_STATUS, " +
						"EJ_USUARIO FROM C_EJECUTIVOS WHERE EJ_NUMSUCURSAL=? AND EJ_STATUS=?";
		ArrayList<EjecutivoCreditoVO> lista=new ArrayList<EjecutivoCreditoVO>();
		Connection cn = null;
		ResultSet res = null;
		
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(1, idSucursal);
			ps.setString(2, estatus);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeQuery();
			while (res.next()){
				EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
				ejecutivo.idSucursal = res.getInt("EJ_NUMSUCURSAL");
				ejecutivo.idEjecutivo = res.getInt("EJ_NUMEJECUTIVO");
				ejecutivo.nombre = res.getString("EJ_NOMBRE");
				ejecutivo.aPaterno = res.getString("EJ_APATERNO");
				ejecutivo.aMaterno = res.getString("EJ_AMATERNO");
				ejecutivo.estatus = res.getString("EJ_STATUS");
				ejecutivo.usuario = res.getString("EJ_USUARIO");
				lista.add(ejecutivo);
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getEjecutivosSucursal: " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getEjecutivosSucursal: " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesException(sqle.getMessage());
			}
		}
		return lista;
	}	

	
	 * Listado de ejecutivos que pertenecen a una determinada sucursal
	 * @return 
	 * @throws ClientesException
	 */
	public ArrayList<RepresentantesVO> getRepresentanteSucursal(int numSucursal) throws ClientesException {
		
		String query =	"SELECT * FROM C_REPRESENTANTES WHERE RP_NUMSUCURSAL=?";
						
		ArrayList<RepresentantesVO> lista=new ArrayList<RepresentantesVO>();
		Connection cn = null;
		ResultSet res = null;
		
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(1, numSucursal);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeQuery();
			while (res.next()){
				RepresentantesVO representante = new RepresentantesVO();
				representante.idSucursal = res.getInt("RP_NUMSUCURSAL");
				representante.idRepresentante = res.getInt("RP_NUMREPRESENTANTE");
				representante.nombre = res.getString("RP_NOMBRE");
				representante.estatus = res.getString("RP_STATUS");
				representante.factor = res.getFloat("RP_FACTOR");
				lista.add(representante);
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getRepresentantesSucursal: " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getRepresentantesSucursal: " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesException(sqle.getMessage());
			}
		}
		return lista;
	}	
	
	
	/**
	 * Obtiene los datos de un ejecutivo
	 * @return 
	 * @throws ClientesException
	 */	
	public RepresentantesVO getRepresentante(int numRepresentante) throws ClientesException{

		String query =	"SELECT * FROM C_REPRESENTANTES WHERE RP_NUMREPRESENTANTE=?";
						
		Connection cn = null;
		RepresentantesVO representante = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,numRepresentante);
			Logger.debug("Ejecutando : "+query);
			ResultSet res = ps.executeQuery();
			if(res.next()){
				representante = new RepresentantesVO();
				representante.idRepresentante = res.getInt("RP_NUMREPRESENTANTE");
				representante.nombre = res.getString("RP_NOMBRE");
				representante.idSucursal = res.getInt("RP_NUMSUCURSAL");
				representante.estatus = res.getString("RP_STATUS");
				representante.factor = res.getFloat("RP_FACTOR");
				representante.segmento = res.getInt("RP_SEGMENTO");
				representante.tipoTabla = res.getInt("RP_TIPO_TABLA");
				representante.baseInteres = res.getInt("RP_BASE_INTERES");
				representante.tasaMora = res.getInt("RP_TASA_MORA");
				representante.gastoCobranza = res.getInt("RP_GASTO_COBRANZA");
				representante.feriado = res.getInt("RP_FERIADO");
				representante.manejoFeriado = res.getInt("RP_MANEJO_FERIADO");
				representante.unidadTiempo = res.getInt("RP_UNIDAD_TIEMPO");
				representante.plazoMaximo = res.getInt("RP_PLAZO_MAXIMO");
				representante.plazoMinimo = res.getInt("RP_PLAZO_MINIMO");
				representante.diaGracia = res.getInt("RP_DIA_GRACIA");
				representante.diaPago = res.getInt("RP_DIA_PAGO");
				
			}
			
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getRepresentante : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getRepresentante : "+e.getMessage());
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
		return representante;
	}

	/**
	 * Agregar un ejecutivo de credito
	 * @param EjecutivoCreditoVO
	 * @return
	 * @throws ClientesException
	 */
	public int addRepresentante(RepresentantesVO representante) throws ClientesException {

		String query =	"INSERT INTO C_REPRESENTANTES(RP_NUMREPRESENTANTE, RP_NOMBRE, RP_NUMSUCURSAL, " +
						"RP_STATUS, RP_FACTOR, RP_SEGMENTO, RP_TIPO_TABLA, RP_BASE_INTERES, RP_TASA_MORA, " +
						"RP_GASTO_COBRANZA, RP_FERIADO, RP_MANEJO_FERIADO, RP_UNIDAD_TIEMPO, RP_PLAZO_MAXIMO, " +
						"RP_PLAZO_MINIMO, RP_DIA_GRACIA, RP_DIA_PAGO) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection cn = null;
		int param = 1;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(param++, representante.idRepresentante);
			ps.setString(param++, representante.nombre);
			ps.setInt(param++, representante.idSucursal);
			ps.setString(param++, representante.estatus);
			ps.setFloat(param++, representante.factor);
			ps.setInt(param++, representante.segmento);
			ps.setInt(param++, representante.tipoTabla);
			ps.setInt(param++, representante.baseInteres);
			ps.setInt(param++, representante.tasaMora);
			ps.setInt(param++, representante.gastoCobranza);
			ps.setInt(param++, representante.feriado);
			ps.setInt(param++, representante.manejoFeriado);
			ps.setInt(param++, representante.unidadTiempo);
			ps.setInt(param++, representante.plazoMaximo);
			ps.setInt(param++, representante.plazoMinimo);
			ps.setInt(param++, representante.diaGracia);
			ps.setInt(param++, representante.diaPago);
			
			
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addRepresentante : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addRepresentante: " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		return res;
	}

	/**
	 * Actualiza un ejecutivo de credito
	 * @param EjecutivoCreditoVO
	 * @return
	 * @throws ClientesException
	 */
	public int updateRepresentante(RepresentantesVO representante) throws ClientesException {

		String query =	"UPDATE C_REPRESENTANTES SET RP_NUMSUCURSAL = ?, RP_NOMBRE = ?, RP_STATUS = ? , RP_FACTOR = ? ," +
		" RP_SEGMENTO = ? , RP_TIPO_TABLA = ? , RP_BASE_INTERES = ? , RP_TASA_MORA = ? ," +
		" RP_GASTO_COBRANZA = ? , RP_FERIADO = ? , RP_MANEJO_FERIADO = ? , RP_UNIDAD_TIEMPO = ? ," +
		" RP_PLAZO_MAXIMO = ?, RP_PLAZO_MINIMO = ? , RP_DIA_GRACIA = ? , RP_DIA_PAGO = ? WHERE RP_NUMREPRESENTANTE = ?";
		Connection cn = null;
		int param = 1;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(param++, representante.idSucursal);
			ps.setString(param++, representante.nombre);
			ps.setString(param++, representante.estatus);
			ps.setFloat(param++, representante.factor);
			ps.setInt(param++, representante.segmento);
			ps.setInt(param++, representante.tipoTabla);
			ps.setInt(param++, representante.baseInteres);
			ps.setInt(param++, representante.tasaMora);
			ps.setInt(param++, representante.gastoCobranza);
			ps.setInt(param++, representante.feriado);
			ps.setInt(param++, representante.manejoFeriado);
			ps.setInt(param++, representante.unidadTiempo);
			ps.setInt(param++, representante.plazoMaximo);
			ps.setInt(param++, representante.plazoMinimo);
			ps.setInt(param++, representante.diaGracia);
			ps.setInt(param++, representante.diaPago);
			ps.setInt(param++, representante.idRepresentante);
			
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateRepresentante : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateRepresentante: " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		return res;
	}
	
}

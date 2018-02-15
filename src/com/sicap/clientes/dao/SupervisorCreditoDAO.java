package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.SupervisorCreditoVO;

public class SupervisorCreditoDAO extends DAOMaster{
	
	/**
	 * Agregar un supervisor de credito
	 */
	
	public int addSupervisor(SupervisorCreditoVO supervisor) throws ClientesException {

		String query =	"INSERT INTO C_SUPERVISORES(CSU_NUMSUCURSAL, CSU_NUMSUPERVISOR, CSU_NOMBRE," +
				" CSU_APATERNO, CSU_AMATERNO, CSU_STATUS, CSU_USUARIO," +
				" CSU_FECHA_MODIFICACION) VALUES ( ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
		Connection cn = null;
		int param = 1;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(param++, supervisor.idSucursal);
			ps.setInt(param++, supervisor.idSupervisor);
			ps.setString(param++, supervisor.nombre);
			ps.setString(param++, supervisor.aPaterno);
			ps.setString(param++, supervisor.aMaterno);
			ps.setString(param++, supervisor.estatus);
			ps.setString(param++, supervisor.usuario);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addEjecutivo : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addEjecutivo: " + e.getMessage());
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
		 * Listado de supervisores
		 */
		public ArrayList<SupervisorCreditoVO> getSupervisor() throws ClientesException {
			
			String query = "SELECT CSU_NUMSUCURSAL, CSU_NUMSUPERVISOR, CSU_NOMBRE, " +
					"CSU_APATERNO, CSU_AMATERNO, CSU_STATUS, CSU_USUARIO FROM C_SUPERVISORES";
			
			ArrayList<SupervisorCreditoVO> lista=new ArrayList<SupervisorCreditoVO>();
			Connection cn = null;
			ResultSet res = null;
			
			try {
				PreparedStatement ps = null;
				cn = getConnection();
				ps = cn.prepareStatement(query);
				Logger.debug("Ejecutando = " + query);
				res = ps.executeQuery();
				while (res.next()){
					SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
					supervisor.idSucursal = res.getInt("CSU_NUMSUCURSAL");
					supervisor.idSupervisor = res.getInt("CSU_NUMSUPERVISOR");
					supervisor.nombre = res.getString("CSU_NOMBRE");
					supervisor.aPaterno = res.getString("CSU_APATERNO");
					supervisor.aMaterno = res.getString("CSU_AMATERNO");
					supervisor.estatus = res.getString("CSU_STATUS");
					supervisor.usuario = res.getString("CSU_USUARIO");
					lista.add(supervisor);
				}
			} catch (SQLException sqle) {
				Logger.debug("SQLException en getSupervisor: " + sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			} catch (Exception e) {
				Logger.debug("Excepcion en getSupervisor: " + e.getMessage());
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
		 * Listado de supervisor con un estatus de (A)lta o (B)aja
		 */
		public ArrayList<SupervisorCreditoVO> getSupervisorSucursal(int idSucursal, String estatus) throws ClientesException {
			
			String query =	"SELECT CSU_NUMSUCURSAL, CSU_NUMSUPERVISOR, CSU_NOMBRE, CSU_APATERNO, CSU_AMATERNO, CSU_STATUS, " +
							"CSU_USUARIO FROM C_SUPERVISORES WHERE CSU_NUMSUCURSAL=? AND CSU_STATUS=?";
			ArrayList<SupervisorCreditoVO> lista=new ArrayList<SupervisorCreditoVO>();
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
					SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
					supervisor.idSucursal = res.getInt("CSU_NUMSUCURSAL");
					supervisor.idSupervisor = res.getInt("CSU_NUMSUPERVISOR");
					supervisor.nombre = res.getString("CSU_NOMBRE");
					supervisor.aPaterno = res.getString("CSU_APATERNO");
					supervisor.aMaterno = res.getString("CSU_AMATERNO");
					supervisor.estatus = res.getString("CSU_STATUS");
					supervisor.usuario = res.getString("CSU_USUARIO");
					lista.add(supervisor);
				}
			} catch (SQLException sqle) {
				Logger.debug("SQLException en getSupervisorSucursal: " + sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			} catch (Exception e) {
				Logger.debug("Excepcion en getSupervisorSucursal: " + e.getMessage());
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
		 * Listado de supervisor que pertenecen a una determinada sucursal
		 */
		public ArrayList<SupervisorCreditoVO> getSupervisorSucursal(int numSucursal) throws ClientesException {
			
			String query =	"SELECT CSU_NUMSUCURSAL, CSU_NUMSUPERVISOR, CSU_NOMBRE, CSU_APATERNO, CSU_AMATERNO, CSU_STATUS, " +
							"CSU_USUARIO, CSU_FECHA_MODIFICACION FROM C_SUPERVISORES WHERE CSU_NUMSUCURSAL=?";
			ArrayList<SupervisorCreditoVO> lista=new ArrayList<SupervisorCreditoVO>();
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
					SupervisorCreditoVO supervisor = new SupervisorCreditoVO();
					supervisor.idSucursal = res.getInt("CSU_NUMSUCURSAL");
					supervisor.idSupervisor = res.getInt("CSU_NUMSUPERVISOR");
					supervisor.nombre = res.getString("CSU_NOMBRE");
					supervisor.aPaterno = res.getString("CSU_APATERNO");
					supervisor.aMaterno = res.getString("CSU_AMATERNO");
					supervisor.estatus = res.getString("CSU_STATUS");
					supervisor.usuario = res.getString("CSU_USUARIO");
					supervisor.fechaHoramodificacion = res.getTimestamp("CSU_FECHA_MODIFICACION");
					lista.add(supervisor);
				}
			} catch (SQLException sqle) {
				Logger.debug("SQLException en getSupervisorSucursal: " + sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			} catch (Exception e) {
				Logger.debug("Excepcion en getSupervisorSucursal: " + e.getMessage());
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
		 * Obtiene los datos de un supervisor
		 */	
		public SupervisorCreditoVO getSupervisor(int numSupervisor) throws ClientesException{

			String query =	"SELECT CSU_NUMSUCURSAL, CSU_NUMSUPERVISOR, CSU_NOMBRE, CSU_APATERNO, CSU_AMATERNO, CSU_STATUS, " +
							"CSU_USUARIO, CSU_FECHA_MODIFICACION FROM C_SUPERVISORES WHERE CSU_NUMSUPERVISOR=?";
			Connection cn = null;
			SupervisorCreditoVO supervisor = null;
			try{
				cn = getConnection();
				PreparedStatement ps = cn.prepareStatement(query);
				ps.setInt(1,numSupervisor);
				Logger.debug("Ejecutando : "+query);
				ResultSet res = ps.executeQuery();
				if(res.next()){
					supervisor = new SupervisorCreditoVO();
					supervisor.idSucursal = res.getInt("CSU_NUMSUCURSAL");
					supervisor.idSupervisor = res.getInt("CSU_NUMSUPERVISOR");
					supervisor.nombre = res.getString("CSU_NOMBRE");
					supervisor.aPaterno = res.getString("CSU_APATERNO");
					supervisor.aMaterno = res.getString("CSU_AMATERNO");
					supervisor.estatus = res.getString("CSU_STATUS");
					supervisor.fechaHoramodificacion = res.getTimestamp("CSU_FECHA_MODIFICACION");
					supervisor.usuario = res.getString("CSU_USUARIO");
				}
			}catch(SQLException sqle) {
				Logger.debug("SQLException en getSupervisor : "+sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			}
			catch(Exception e) {
				Logger.debug("Excepcion en getSupervisor : "+e.getMessage());
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
			return supervisor;
		}
		/**
		 * Actualiza un supervisor de credito
		 * @param SupervisorCreditoVO
		 */
		public int updateSupervisor(SupervisorCreditoVO supervisor) throws ClientesException {

			String query =	"UPDATE C_SUPERVISORES SET CSU_NUMSUCURSAL = ?, CSU_NOMBRE = ?, CSU_APATERNO = ?, CSU_AMATERNO = ?, " +
							"CSU_STATUS = ? ,CSU_USUARIO=?, CSU_FECHA_MODIFICACION=CURRENT_TIMESTAMP WHERE CSU_NUMSUPERVISOR = ?";
			Connection cn = null;
			int param = 1;
			int res = 0;
			try {
				PreparedStatement ps = null;
				cn = getConnection();
				ps = cn.prepareStatement(query);
				ps.setInt(param++, supervisor.idSucursal);
				ps.setString(param++, supervisor.nombre);
				ps.setString(param++, supervisor.aPaterno);
				ps.setString(param++, supervisor.aMaterno);
				ps.setString(param++, supervisor.estatus);
				ps.setString(param++, supervisor.usuario);
				ps.setInt(param++, supervisor.idSupervisor);
				Logger.debug("Ejecutando = " + query);
				res = ps.executeUpdate();
				Logger.debug("Resultado = " + res);
			} catch (SQLException sqle) {
				Logger.debug("SQLException en updateSupervisor : " + sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			} catch (Exception e) {
				Logger.debug("Excepcion en updateSupervisor: " + e.getMessage());
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


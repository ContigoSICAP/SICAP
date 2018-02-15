package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.cartera.DescongelaAhorroVO;

public class DescongelaAhorroDAO extends DAOMaster {

	public DescongelaAhorroVO getCreditoGrupoSol(int numCliente, int numSolicitud) throws ClientesException{

		//ArrayList<DescongelaAhorroVO> lista=new ArrayList<DescongelaAhorroVO>();
		DescongelaAhorroVO creditos = new DescongelaAhorroVO();
		Connection conn = null;
		ResultSet  rs   = null;
		try{
			conn = getCWConnection();
			String query = "SELECT * FROM d_credito WHERE CR_NUM_CLIENTE = ? AND CR_NUM_SOLICITUD = ?";

			PreparedStatement ps = conn.prepareStatement(query);

			ps.setInt   (1, numCliente);
			ps.setInt   (2, numSolicitud);

			rs = ps.executeQuery();

			if( rs.next() ){
				creditos = new DescongelaAhorroVO();

				creditos.setNumCliente                ( rs.getInt("cr_num_cliente"));
				creditos.setNumSolicitud              ( rs.getInt("cr_num_solicitud"));
				creditos.setNumCredito                ( rs.getInt("cr_num_credito"));
				creditos.setReferencia                ( rs.getString("cr_referencia"));
				creditos.setNumDocumento              ( rs.getString("cr_num_documento"));
				creditos.setNumCuenta                 ( rs.getInt("cr_num_cuenta"));
				creditos.setNumProducto               ( rs.getInt("cr_num_producto"));
				creditos.setNumSucursal               ( rs.getInt("cr_num_sucursal"));
				creditos.setNumEmpresa                ( rs.getInt("cr_num_empresa"));
				creditos.setFechaDesembolso           ( rs.getDate("cr_fecha_desembolso"));
				creditos.setFechaVencimiento          ( rs.getDate("cr_fecha_vencimiento"));
				creditos.setFechaLiquidacion          ( rs.getDate("cr_fecha_liquidacion"));
				creditos.setPeriodicidad              ( rs.getInt("cr_periodicidad"));
				creditos.setNumCuotas                 ( rs.getInt("cr_numero_cuotas"));
				creditos.setMontoCredito              ( rs.getDouble("cr_monto_credito"));
				creditos.setMontoDesembolsado         ( rs.getDouble("cr_monto_desembolsado"));
				creditos.setMontoCuenta               ( rs.getDouble("cr_monto_cuenta"));
				creditos.setMontoCuentaCongelada      ( rs.getDouble("cr_monto_cuenta_congelada"));
				creditos.setMontoComision             ( rs.getDouble("cr_monto_comision"));
				creditos.setMontoIvaComision          ( rs.getDouble("cr_iva_comision"));
				creditos.setMontoSeguro               ( rs.getDouble("cr_monto_seguro"));
				creditos.setMontoIvaSeguro            ( rs.getDouble("cr_iva_seguro"));
				creditos.setMontoOtrosCargos          ( rs.getDouble("cr_otros_cargos"));
				creditos.setMontoIvaOtrosCargos       ( rs.getDouble("cr_iva_otros_cargos"));
				creditos.setTasaInteres               ( rs.getDouble("cr_tasa_int"));
				creditos.setTasaInteresSIVA           ( rs.getDouble("cr_tasa_int_siniva"));
				creditos.setTasaComision              ( rs.getDouble("cr_tasa_comision_siniva"));
				creditos.setTasaMora                  ( rs.getDouble("cr_tasa_mora_siniva"));
				creditos.setTasaIVA                   ( rs.getDouble("cr_tasa_iva"));
				creditos.setNumDias                   ( rs.getInt("cr_num_dias"));
				creditos.setNumDiasMora               ( rs.getInt("cr_dias_mora"));
				creditos.setFondeador                 ( rs.getInt("cr_fondeador"));
				creditos.setStatus                    ( rs.getInt("cr_status"));
				creditos.setNumEjecutivo              ( rs.getInt("cr_num_ejecutivo"));
				creditos.setFechaUltimaActualizacion  ( rs.getDate("cr_fecha_ult_actualiz"));
				creditos.setFechaUltimoPago           ( rs.getDate("cr_fecha_ult_pago"));
				creditos.setValorCuota                ( rs.getDouble("cr_valor_cuota"));
				creditos.setBancoDesembolso           ( rs.getInt("cr_banco_desembolso"));
				//lista.add(creditos);
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally{
			try{
				if ( conn!=null ) conn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		
		return creditos;
	}
	
	
	public int updateMontoCuentaCongelado(int numGrupo, int numCiclo) throws ClientesException {

		String query =	"UPDATE D_CREDITO SET CR_MONTO_CUENTA_CONGELADA = 0 WHERE CR_NUM_CLIENTE = ?";
		Connection cn = null;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getCWConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(1, numGrupo);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateEjecutivo : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateEjecutivo: " + e.getMessage());
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

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.BitacoraBuroCirculoTotalesVO;



public class BitacoraBuroCirculoTotalesDAO extends DAOMaster{

	public int add(BitacoraBuroCirculoTotalesVO bitacora) throws ClientesException{

		String query =	"INSERT INTO D_BITACORA_BUROCIRCULO_TOTALES (BC_TOTAL_CUENTAS, BC_CUENTAS_MORA, BC_CUENTAS_CERRADAS, BC_TOTAL_SALDOS_ACTUALES, BC_TOTAL_SALDOS_VENCIDOS, BC_FECHA_ENVIO) " +
						"VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, bitacora.totalCuentas);
			ps.setInt(param++, bitacora.totalCuentasMora);
			ps.setInt(param++, bitacora.totalCuentasCerradas);
			ps.setDouble(param++, bitacora.totalSaldosActuales);
			ps.setDouble(param++, bitacora.totalSaldosVencidos);

			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addBitacoraBuroCirculoTotales : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addBitacoraBuroCirculoTotales : "+e.getMessage());
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
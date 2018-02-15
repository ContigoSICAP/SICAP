package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.CicloGrupalInffinixVO;


public class CicloGrupalInffinixDAO extends CicloGrupalDAO{

	public CicloGrupalInffinixVO[] getCiclosInffinix(int idGrupo) throws ClientesException{

		String query = "SELECT D_CICLOS_GRUPALES.*, TMP_REFERENCIAS.* FROM D_CICLOS_GRUPALES, D_SALDOS_T24, TMP_REFERENCIAS " +
						"WHERE CI_NUMGRUPO = ? AND CI_NUMGRUPO = ST_NUMCLIENTE AND CI_NUMCICLO " +
						"= ST_CICLO AND REFERENCIA = ST_REFERENCIA AND APARICIONES = 1 AND ST_NUMOPERACION IN (3, 5)";
		ArrayList<CicloGrupalInffinixVO> array = new ArrayList<CicloGrupalInffinixVO>();
        CicloGrupalInffinixVO temporal = null;
        CicloGrupalInffinixVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idGrupo);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal =  new CicloGrupalInffinixVO();
				temporal.idGrupo = rs.getInt("CI_NUMGRUPO");
				temporal.idCiclo = rs.getInt("CI_NUMCICLO");
				temporal.estatus = rs.getInt("CI_ESTATUS");
				temporal.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
				temporal.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
				temporal.diaReunion = rs.getInt("CI_DIA_REUNION");
				temporal.horaReunion = rs.getInt("CI_HORA_REUNION");
				temporal.asesor = rs.getInt("CI_EJECUTIVO");
				temporal.coordinador = rs.getInt("CI_COORDINADOR");
				temporal.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
				temporal.multaFalta = rs.getDouble("CI_MULTA_FALTA");
				temporal.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
				temporal.referencia = rs.getString("REFERENCIA");
				
				array.add(temporal);
				//Logger.debug("Ciclo encontrado : "+temporal.toString());
			}
			if ( array.size()>0 ){
				elementos = new CicloGrupalInffinixVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (CicloGrupalInffinixVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCiclos : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCiclos : "+e.getMessage());
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
	    return elementos;
	}
}
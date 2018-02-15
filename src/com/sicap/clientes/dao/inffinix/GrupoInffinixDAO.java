package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.GrupoInffinixVO;


public class GrupoInffinixDAO extends GrupoDAO{

	public GrupoInffinixVO[] getGruposInffinix() throws ClientesException{

		//String query = "SELECT * FROM D_CLIENTES WHERE EN_RFC = ?";
		//String inicioReferencia = "DROP TABLE IF EXISTS TMP_REFERENCIAS_GRUPAL";
		//String createReferencias = "CREATE TABLE TMP_REFERENCIAS_GRUPAL AS SELECT ST_REFERENCIA REFERENCIA_GRUPAL, COUNT(*) APARICIONES FROM D_SALDOS_T24 WHERE ST_NUMOPERACION =3 GROUP BY ST_REFERENCIA";
		//String index = "ALTER TABLE tmp_referencias_grupal ADD INDEX Index_1(REFERENCIA, APARICIONES);";
		String query = "SELECT DISTINCT D_GRUPOS.* FROM D_GRUPOS, D_SALDOS_T24, TMP_REFERENCIAS WHERE ST_NUMCLIENTE = GR_NUMGRUPO AND ST_REFERENCIA = REFERENCIA AND APARICIONES = 1 ";
		//ANEXO AL QUERY PARA NO INCLUIR COMUNAL
		query +="AND ST_NUMOPERACION IN (3, 5)";
		
		PreparedStatement ps = null;
		//String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMCLIENTE IN(SELECT SO_NUMCLIENTE FROM D_SOLICITUDES WHERE SO_NO_CONTRATO IS NOT NULL AND SUBSTR(SO_NO_CONTRATO,1,12) IN(SELECT SUBSTR(PA_REFERENCIA,1,12) FROM D_PAGOS WHERE PA_TIPO = 'T' AND PA_ENVIADO = 0 AND PA_FECHA_PAGO = (SELECT PA_FECHA_PAGO FROM D_PAGOS ORDER BY PA_FECHA_PAGO DESC LIMIT 1)))";
		Connection cn = null;
		ArrayList<GrupoInffinixVO> array = new ArrayList<GrupoInffinixVO>();
		GrupoInffinixVO temporal = null;
		GrupoInffinixVO elementos[] = null;
		try{
			cn = getConnection();
			//ps = cn.prepareStatement(inicioReferencia);
			//Logger.debug("Ejecutando : "+inicioReferencia);
			//ps.executeUpdate();
			//ps = cn.prepareStatement(createReferencias);
			//Logger.debug("Ejecutando : "+createReferencias);
			//ps.executeUpdate();
			//ps = cn.prepareStatement(index);
			//Logger.debug("Ejecutando : "+index);
			//ps.executeUpdate();
			ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery(query);
			while( rs.next() ){
				temporal = new GrupoInffinixVO();
				temporal.estatus = rs.getInt("GR_ESTATUS");
				temporal.fechaCaptura = rs.getTimestamp("GR_FECHA_CAPTURA");
				temporal.fechaFormacion = rs.getDate("GR_FECHA_FORMACION");
				temporal.idGrupo = rs.getInt("GR_NUMGRUPO");
				temporal.nombre = rs.getString("GR_NOMBRE");
				temporal.rfc = rs.getString("GR_RFC");
				temporal.sucursal = rs.getInt("GR_NUMSUCURSAL");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new GrupoInffinixVO[array.size()];
			    for(int i=0;i<elementos.length; i++) 
			    	elementos[i] = array.get(i);
			    Logger.debug("Cantidad de grupos encontrados::" + elementos.length);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getGruposInffinix : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getGruposInffinix : "+e.getMessage());
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
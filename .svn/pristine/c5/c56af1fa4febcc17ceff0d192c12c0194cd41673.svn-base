package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.ClienteInffinix;


public class ClienteInffinixDAO extends ClienteDAO{


	/*public ClienteInffinix[] getClientesInfinixSaldosContrato(String contrato) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMCLIENTE IN ";
		query += "(SELECT SO_NUMCLIENTE FROM D_SOLICITUDES WHERE SO_NO_CONTRATO IS NOT NULL ";
		query += "AND SO_NO_CONTRATO IN (SELECT ST_CONTRATO FROM D_SALDOS_T24 ";
		query += "WHERE ST_CONTRATO LIKE ?));";
		Connection cn = null;
		int param = 1;
		ArrayList<ClienteInffinix> array = new ArrayList<ClienteInffinix>();
        ClienteInffinix temporal = null;
        ClienteInffinix elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++, contrato);
			Logger.debug("Parametro::" + contrato);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery(query);
			while( rs.next() ){
				temporal = new ClienteInffinix();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				temporal.estatus = rs.getInt("EN_ESTATUS");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				temporal.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				temporal.sexo = rs.getInt("EN_SEXO");
				temporal.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				temporal.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				temporal.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				temporal.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				temporal.correoElectronico = rs.getString("EN_EMAIL");
				temporal.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ClienteInffinix[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ClienteInffinix)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getClientesSincronet : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getClientesSincronet : "+e.getMessage());
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
	}*/

	public ClienteInffinix[] getClientesInfinix() throws ClientesException{

		//String query = "SELECT * FROM D_CLIENTES WHERE EN_RFC = ?";
		String inicioReferencia = "DROP TABLE IF EXISTS TMP_REFERENCIAS";
		String createReferencias = "CREATE TABLE TMP_REFERENCIAS AS SELECT ST_REFERENCIA REFERENCIA, COUNT(*) APARICIONES FROM D_SALDOS_T24 GROUP BY ST_REFERENCIA";
		String index = "ALTER TABLE TMP_REFERENCIAS ADD INDEX INDEX_1(REFERENCIA, APARICIONES);";
		String query = "SELECT DISTINCT D_CLIENTES.* FROM D_CLIENTES, D_SALDOS_T24, TMP_REFERENCIAS WHERE ST_NUMCLIENTE = EN_NUMCLIENTE AND ST_REFERENCIA = REFERENCIA AND APARICIONES = 1 ";
		//ANEXO AL QUERY PARA NO INCLUIR COMUNAL
		query +="AND ST_NUMOPERACION NOT IN (3, 5)";
		
		PreparedStatement ps = null;
		//String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMCLIENTE IN(SELECT SO_NUMCLIENTE FROM D_SOLICITUDES WHERE SO_NO_CONTRATO IS NOT NULL AND SUBSTR(SO_NO_CONTRATO,1,12) IN(SELECT SUBSTR(PA_REFERENCIA,1,12) FROM D_PAGOS WHERE PA_TIPO = 'T' AND PA_ENVIADO = 0 AND PA_FECHA_PAGO = (SELECT PA_FECHA_PAGO FROM D_PAGOS ORDER BY PA_FECHA_PAGO DESC LIMIT 1)))";
		Connection cn = null;
		ArrayList<ClienteInffinix> array = new ArrayList<ClienteInffinix>();
		ClienteInffinix temporal = null;
		ClienteInffinix elementos[] = null;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(inicioReferencia);
			Logger.debug("Ejecutando : "+inicioReferencia);
			ps.executeUpdate();
			ps = cn.prepareStatement(createReferencias);
			Logger.debug("Ejecutando : "+createReferencias);
			ps.executeUpdate();
			ps = cn.prepareStatement(index);
			Logger.debug("Ejecutando : "+index);
			ps.executeUpdate();
			ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery(query);
			while( rs.next() ){
				temporal = new ClienteInffinix();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				temporal.estatus = rs.getInt("EN_ESTATUS");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				temporal.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				temporal.sexo = rs.getInt("EN_SEXO");
				temporal.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				temporal.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				temporal.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				temporal.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				temporal.correoElectronico = rs.getString("EN_EMAIL");
				temporal.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ClienteInffinix[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ClienteInffinix)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getClientesSincronet : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getClientesSincronet : "+e.getMessage());
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
	
	
	public ClienteInffinix[] getClientesGrupal() throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMGRUPO IN(SELECT CI_NUMGRUPO FROM D_CICLOS_GRUPALES WHERE CI_CONTRATO IS NOT NULL AND CI_CONTRATO IN(SELECT ST_CONTRATO FROM D_SALDOS_T24))";
		Connection cn = null;
		ArrayList<ClienteInffinix> array = new ArrayList<ClienteInffinix>();
		ClienteInffinix temporal = null;
		ClienteInffinix elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery(query);
			while( rs.next() ){
				temporal = new ClienteInffinix();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				temporal.estatus = rs.getInt("EN_ESTATUS");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				temporal.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				temporal.sexo = rs.getInt("EN_SEXO");
				temporal.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				temporal.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				temporal.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				temporal.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				temporal.correoElectronico = rs.getString("EN_EMAIL");
				temporal.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ClienteInffinix[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ClienteInffinix)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getClientesSincronet : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getClientesSincronet : "+e.getMessage());
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
	
	
	public ClienteInffinix[] findByRFC(String rfc) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_RFC LIKE ? ";
		Connection cn = null;
		ArrayList<ClienteInffinix> array = new ArrayList<ClienteInffinix>();
        ClienteInffinix temporal = null;
        ClienteInffinix elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,"%"+rfc.toUpperCase()+"%");
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				temporal = new ClienteInffinix();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ClienteInffinix[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ClienteInffinix)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en findByRFC : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en findByRFC : "+e.getMessage());
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
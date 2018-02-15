package com.sicap.clientes.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.BusquedaClientesVO;
import com.sicap.clientes.vo.ClienteVO;


public class ClienteDAO extends DAOMaster{

        private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(ClienteDAO.class);

	public ClienteVO getCliente(int idCliente) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMCLIENTE = ? ";
		Connection cn = null;
		ClienteVO cliente = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				cliente = new ClienteVO();
				cliente.idCliente = idCliente;
				cliente.idClienteIBS = rs.getInt("EN_NUMCLIENTE_IBS");
				cliente.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				cliente.estatus = rs.getInt("EN_ESTATUS");
				cliente.rfc = rs.getString("EN_RFC");
				cliente.nombre = rs.getString("EN_NOMBRE");
				cliente.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				cliente.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				cliente.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				cliente.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				cliente.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				cliente.sexo = rs.getInt("EN_SEXO");
				cliente.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				cliente.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				cliente.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				cliente.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				cliente.correoElectronico = rs.getString("EN_EMAIL");
				cliente.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				cliente.dependientesEconomicos = rs.getInt("EN_DEPENDIENTES_ECONOMICOS");
				cliente.idGrupo = rs.getInt("EN_NUMGRUPO");
				cliente.nivelEstudios = rs.getInt("EN_NIVEL_ESTUDIOS");
				cliente.curp = rs.getString("EN_CURP");
                                cliente.Discapacidad = rs.getInt("en_Discapacidad");
                                cliente.LenguaIndigena = rs.getInt("en_LenguaIndigena");
                                cliente.UsodeInternet = rs.getInt("en_UsodeInternet");
                                cliente.RedesSociales = rs.getInt("en_RedesSociales");
			}
//			Logger.debug("Cliente encontrado : "+cliente.toString());
		}catch(SQLException sqle) {
                        myLogger.error("getCliente",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
                        myLogger.error("getCliente",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return cliente;

	}
        
        public int getEstadoCivilFommur(int idEstadoCivil) throws ClientesException{

		String query = "Select  es_idfommur from c_estado_civil" +
                               " where es_numestadocivil = ?; ";
		Connection cn = null;
                int eCFommur = 0;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idEstadoCivil);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
                                eCFommur =  rs.getInt("es_idfommur");
				
			}
//			Logger.debug("Cliente encontrado : "+cliente.toString());
		}catch(SQLException sqle) {
                        myLogger.error("getCliente",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
                        myLogger.error("getCliente",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return eCFommur;

	}
        
        public String getNombreCliente(int idCliente) throws ClientesException{
            String query = "SELECT en_nombre_completo FROM d_clientes WHERE en_numcliente=?";
            String nombre = null;
            Connection cn = null;
            
            try {
                cn=getConnection();
                PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
                            nombre=rs.getString("en_nombre_completo");
                        }
                
            }
            catch(SQLException sqle) {
			myLogger.error("getNombreCliente",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("getNombreCliente",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
            return nombre;
        }


/*	public ClienteVO getCliente(String nombre) throws ClientesException{
		ClienteVO cliente = null;
		Connection cn = null;
		String query = "";
		Logger.debug("Nombre a buscar: " +nombre);
		query = "SELECT * FROM D_CLIENTES WHERE EN_NOMBRE_COMPLETO LIKE ?";

		try{
			cn = this.getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1, "%" + nombre + "%");
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();

			while( rs.next() ){
				cliente = new ClienteVO();
				cliente.idCliente = rs.getInt("EN_NUMCLIENTE");;
				cliente.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				cliente.estatus = rs.getInt("EN_ESTATUS");
				cliente.nombre = rs.getString("EN_NOMBRE").trim().toUpperCase();
				cliente.aPaterno = rs.getString("EN_PRIMER_APELLIDO").trim().toUpperCase();
				cliente.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO").trim().toUpperCase();
				cliente.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO").trim().toUpperCase();
				cliente.rfc = rs.getString("EN_RFC");
				cliente.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				cliente.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				cliente.sexo = rs.getInt("EN_SEXO");
				cliente.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				cliente.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				cliente.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				cliente.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				cliente.correoElectronico = rs.getString("EN_EMAIL");
				cliente.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				cliente.curp = rs.getString("EN_CURP");
			}

		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCliente : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCliente : "+e.getMessage());
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
	    return cliente;

	}*/
	

	public ClienteVO[] findByRFCTablas(String rfc) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES, D_SOLICITUDES WHERE EN_RFC LIKE ? AND EN_NUMCLIENTE = SO_NUMCLIENTE AND SO_DESEMBOLSADO = 1 ORDER BY EN_FECHA_CAPTURA DESC";
		Connection cn = null;
		ArrayList<ClienteVO> array = new ArrayList<ClienteVO>();
        ClienteVO temporal = null;
        ClienteVO elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,rfc.toUpperCase()+"%");
			myLogger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				temporal = new ClienteVO();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				temporal.curp = rs.getString("EN_CURP");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ClienteVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ClienteVO)array.get(i);
			}
		}catch(SQLException sqle) {
			myLogger.error("findByRFCTablas",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("findByRFCTablas",e);
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


	public int addCliente(ClienteVO cliente) throws ClientesException{
		return addCliente(null, cliente);
	}


	public int addCliente(Connection conn, ClienteVO cliente) throws ClientesException{

		String query =	"INSERT INTO D_CLIENTES (EN_NUMCLIENTE, EN_NUMSUCURSAL, EN_ESTATUS, EN_NOMBRE, "+
						"EN_PRIMER_APELLIDO, EN_SEGUNDO_APELLIDO, EN_NOMBRE_COMPLETO, EN_RFC, EN_FECHA_NAC, "+
						"EN_ENTIDAD_NAC, EN_SEXO, EN_NACIONALIDAD, EN_TIPO_ID, EN_NUMERO_IDENTIFICACION, "+
						"EN_EDO_CIVIL, EN_EMAIL, EN_FECHA_CAPTURA, EN_DEPENDIENTES_ECONOMICOS, EN_NUMGRUPO, "+
						"EN_NIVEL_ESTUDIOS, EN_CURP, en_id_migracion, en_origenmigracion,en_LenguaIndigena,en_Discapacidad,en_UsodeInternet,en_RedesSociales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		int next = getNext();
		try{
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			//cn = getConnection();
			
			ps.setInt(param++, next);
			ps.setInt(param++, cliente.idSucursal);
			ps.setInt(param++, cliente.estatus);
			ps.setString(param++, cliente.nombre);
			ps.setString(param++, cliente.aPaterno);
			ps.setString(param++, cliente.aMaterno);
			ps.setString(param++, cliente.nombreCompleto);
			ps.setString(param++, cliente.rfc);
			ps.setDate(param++, cliente.fechaNacimiento);
			ps.setInt(param++, cliente.entidadNacimiento);//sexo
			ps.setInt(param++, cliente.sexo);//sexo
			ps.setInt(param++, cliente.nacionalidad);//nacionalidad
			ps.setInt(param++, cliente.tipoIdentificacion);//tipo_id
			ps.setString(param++, cliente.numeroIdentificacion);//
			ps.setInt(param++, cliente.estadoCivil);//edo_civil
			ps.setString(param++, cliente.correoElectronico);//email
			ps.setTimestamp(param++, cliente.fechaCaptura);
			ps.setInt(param++, cliente.dependientesEconomicos);//
			ps.setInt(param++, cliente.idGrupo);//
			ps.setInt(param++, cliente.nivelEstudios);//
			ps.setString(param++, cliente.curp);//
                        ps.setInt(param++, cliente.idMigracion);//
			ps.setInt(param++, cliente.origenMigracion);//
                        ps.setInt(param++, cliente.LenguaIndigena);//
                        ps.setInt(param++, cliente.Discapacidad);//
                        ps.setInt(param++, cliente.UsodeInternet);//
                        ps.setInt(param++, cliente.RedesSociales);//

			myLogger.debug("Ejecutando = "+query);
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			myLogger.error("addCliente",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("addCliente",e);
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
		return next;
	}


	public int updateCliente(ClienteVO cliente) throws ClientesException{
		return updateCliente(null, cliente);
	}


	public int updateCliente(Connection conn, ClienteVO cliente) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_CLIENTES SET EN_NUMSUCURSAL = ?, EN_ESTATUS = ?, EN_NOMBRE = ?, "+
						"EN_PRIMER_APELLIDO = ?, EN_SEGUNDO_APELLIDO = ?, EN_NOMBRE_COMPLETO = ?, "+
						"EN_RFC  = ?, EN_FECHA_NAC = ?, EN_ENTIDAD_NAC = ?, EN_SEXO = ?, EN_NACIONALIDAD = ?, "+
						"EN_TIPO_ID = ?, EN_NUMERO_IDENTIFICACION = ?, EN_EDO_CIVIL = ?, EN_EMAIL = ?, "+
						"EN_DEPENDIENTES_ECONOMICOS = ?, EN_NUMGRUPO = ?, EN_NIVEL_ESTUDIOS = ?, EN_CURP = ?, "+
                                                " en_LenguaIndigena = ?, en_Discapacidad = ?, en_UsodeInternet = ?, en_RedesSociales = ? "+
                                                " WHERE EN_NUMCLIENTE = ?";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		try{
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
//			cn = getConnection();
//			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++, cliente.idSucursal);
			ps.setInt(param++, cliente.estatus);
			ps.setString(param++, cliente.nombre);
			ps.setString(param++, cliente.aPaterno);
			ps.setString(param++, cliente.aMaterno);
			ps.setString(param++, cliente.nombreCompleto);
			ps.setString(param++, cliente.rfc);
			ps.setDate(param++, cliente.fechaNacimiento);
			ps.setInt(param++, cliente.entidadNacimiento);//sexo
			ps.setInt(param++, cliente.sexo);//sexo
			ps.setInt(param++, cliente.nacionalidad);//nacionalidad
			ps.setInt(param++, cliente.tipoIdentificacion);//tipo_id
			ps.setString(param++, cliente.numeroIdentificacion);
			ps.setInt(param++, cliente.estadoCivil);//edo_civil
			ps.setString(param++, cliente.correoElectronico);//email
			ps.setInt(param++, cliente.dependientesEconomicos);
			ps.setInt(param++, cliente.idGrupo);
			ps.setInt(param++, cliente.nivelEstudios);
			ps.setString(param++, cliente.curp);
                        ps.setInt(param++, cliente.LenguaIndigena);
                        ps.setInt(param++, cliente.Discapacidad);
                        ps.setInt(param++, cliente.UsodeInternet);
                        ps.setInt(param++, cliente.RedesSociales);
			ps.setInt(param++, cliente.idCliente);
			myLogger.debug("Ejecutando = "+query);
			myLogger.debug("Cliente = "+cliente.toString());
			res = ps.executeUpdate();
			myLogger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			myLogger.error("addCliente",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("updateCliente",e);
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

	public int updateGrupo(int idCliente, int idGrupo) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_CLIENTES SET EN_NUMGRUPO = ? WHERE EN_NUMCLIENTE = ?";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, idGrupo);
			ps.setInt(param++, idCliente);
			myLogger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			myLogger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			myLogger.error("updateGrupo",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("updateGrupo",e);
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

	public int updateClienteIBS( int idClienteIBS, int idCliente ) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_CLIENTES SET EN_NUMCLIENTE_IBS = ? WHERE EN_NUMCLIENTE = ?";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query);

			ps.setInt(param++, idClienteIBS);
			ps.setInt(param++, idCliente);
			myLogger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			myLogger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			myLogger.error("updateClienteIBS",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("updateClienteIBS",e);
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

	
	public int getNext() throws ClientesException{

		String query = "SELECT COALESCE(MAX(EN_NUMCLIENTE),0)+1 AS NEXT FROM D_CLIENTES";
		Connection cn = null;
		int next = 1;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			myLogger.debug("Ejecutando = "+query);
			if( rs.next() ){
				next = rs.getInt("NEXT");
			}
		}
		catch(SQLException sqle) {
			myLogger.error("getNext",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("getNext",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null )
					cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return next;
	}

	public ClienteVO getClienteRFC(String rfc) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE LEFT(EN_RFC,10) = ? ";
		Connection cn = null;
		ClienteVO cliente = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,rfc);
                        myLogger.debug("Ejecutando = "+ps.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				cliente = new ClienteVO();
				cliente.idCliente = rs.getInt("EN_NUMCLIENTE");
				cliente.idClienteIBS = rs.getInt("EN_NUMCLIENTE_IBS");
				cliente.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				cliente.estatus = rs.getInt("EN_ESTATUS");
				cliente.rfc = rs.getString("EN_RFC");
				cliente.nombre = rs.getString("EN_NOMBRE");
				cliente.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				cliente.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				cliente.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				cliente.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				cliente.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				cliente.sexo = rs.getInt("EN_SEXO");
				cliente.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				cliente.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				cliente.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				cliente.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				cliente.correoElectronico = rs.getString("EN_EMAIL");
				cliente.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				cliente.dependientesEconomicos = rs.getInt("EN_DEPENDIENTES_ECONOMICOS");
				cliente.idGrupo = rs.getInt("EN_NUMGRUPO");
			}

		}catch(SQLException sqle) {
			myLogger.error("getClienteRFC",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("getClienteRFC",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return cliente;

	}
        
	public ClienteVO getClienteRFCCompleto(String rfc) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_RFC    = ? ";
		Connection cn = null;
		ClienteVO cliente = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,rfc);
                        myLogger.debug("Ejecutando = "+ps.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				cliente = new ClienteVO();
				cliente.idCliente = rs.getInt("EN_NUMCLIENTE");
				cliente.idClienteIBS = rs.getInt("EN_NUMCLIENTE_IBS");
				cliente.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				cliente.estatus = rs.getInt("EN_ESTATUS");
				cliente.rfc = rs.getString("EN_RFC");
				cliente.nombre = rs.getString("EN_NOMBRE");
				cliente.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				cliente.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				cliente.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				cliente.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				cliente.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				cliente.sexo = rs.getInt("EN_SEXO");
				cliente.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				cliente.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				cliente.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				cliente.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				cliente.correoElectronico = rs.getString("EN_EMAIL");
				cliente.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				cliente.dependientesEconomicos = rs.getInt("EN_DEPENDIENTES_ECONOMICOS");
				cliente.idGrupo = rs.getInt("EN_NUMGRUPO");
			}

		}catch(SQLException sqle) {
			myLogger.error("getClienteRFC",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("getClienteRFC",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return cliente;

	}
	
        public ClienteVO[] getClientesRFC(String rfc) throws ClientesException{

		String query = "SELECT * FROM D_CLIENTES WHERE EN_RFC LIKE ? ";
		Connection cn = null;
                ClienteVO temporal = null;
		ClienteVO clientes[] = null;
                ArrayList<ClienteVO> array = new ArrayList<ClienteVO>();
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,rfc+'%');
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new ClienteVO();
				temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
				temporal.idClienteIBS = rs.getInt("EN_NUMCLIENTE_IBS");
				temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
				temporal.estatus = rs.getInt("EN_ESTATUS");
				temporal.rfc = rs.getString("EN_RFC");
				temporal.nombre = rs.getString("EN_NOMBRE");
				temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
				temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
				temporal.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
				temporal.fechaNacimiento = rs.getDate("EN_FECHA_NAC");
				temporal.entidadNacimiento = rs.getInt("EN_ENTIDAD_NAC");
				temporal.sexo = rs.getInt("EN_SEXO");
				temporal.nacionalidad = rs.getInt("EN_NACIONALIDAD");
				temporal.tipoIdentificacion = rs.getInt("EN_TIPO_ID");
				temporal.numeroIdentificacion = rs.getString("EN_NUMERO_IDENTIFICACION");
				temporal.estadoCivil = rs.getInt("EN_EDO_CIVIL");
				temporal.correoElectronico = rs.getString("EN_EMAIL");
				temporal.fechaCaptura = rs.getTimestamp("EN_FECHA_CAPTURA");
				temporal.dependientesEconomicos = rs.getInt("EN_DEPENDIENTES_ECONOMICOS");
				temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
                                array.add(temporal);                                
			}
                        if (array.size() > 0) {
                            clientes = new ClienteVO[array.size()];
                            for(int i=0;i<clientes.length; i++) {
                                clientes[i] = (ClienteVO)array.get(i);
                            }
                        }
		}catch(SQLException sqle) {
			myLogger.error("getClienteRFC",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("getClienteRFC",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return clientes;

	}
	public ClienteVO[] buscaCliente(BusquedaClientesVO busca) throws ClientesException{
        
		ArrayList<ClienteVO> array = new ArrayList<ClienteVO>();
		ClienteVO temporal = null;
		ClienteVO clientes[] = null;
		String aux="";
		StringBuffer query = new StringBuffer("SELECT * FROM D_CLIENTES WHERE "); 
		int param = 1;
		Connection cn = null;
		boolean busquedaCorrecta = false;
		PreparedStatement ps = null;
		//Logger.debug("valor de param : "+param);
		
		try{
			if(!busca.RFC.equals("")){
				if(!busca.RFC.substring(0,1).equals("%")){
					query.append("AND EN_RFC LIKE ? ");
					aux+="RFC";
					busquedaCorrecta = true;
				}
			}if(busca.idCliente!=0){
				query.append("AND EN_NUMCLIENTE = ? ");
				aux+="NUMCLIENTE"; 
				busquedaCorrecta = true;
			}if(!busca.apellidoPaterno.equals("")){
				if(!busca.apellidoPaterno.substring(0,1).equals("%")){
					query.append("AND EN_PRIMER_APELLIDO LIKE ? ");
					aux+="PRIMERAPELLIDO";
					busquedaCorrecta = true;
				}
			}if(!busca.apellidoMaterno.equals("")){
				if(!busca.apellidoMaterno.substring(0,1).equals("%")){
					query.append("AND EN_SEGUNDO_APELLIDO LIKE ? ");
					aux+="SEGUNDOAPELLIDO";
					busquedaCorrecta = true;
				}
			}if(!busca.nombreS.equals("")){
				if(!busca.nombreS.substring(0,1).equals("%")){
					query.append("AND EN_NOMBRE LIKE ? ");
					aux+="NOMBRE";
					busquedaCorrecta = true;
				}
			}
			cn = getConnection();
			String queryfinal = query.toString().replaceFirst("AND ", "");
			if(busca.sucursal != 33 && busca.sucursal != 0){
				String sucursales = "";
				if(busca.sucursales != null) {
					for(int i = 0; i<busca.sucursales.length; i++){
						sucursales +=  String.valueOf(busca.sucursales[i].idSucursal)+",";
					}
					sucursales += "0";
				}else{
					sucursales = String.valueOf(busca.sucursal);
				}
				
				String filtro = " AND EN_NUMSUCURSAL IN ("+sucursales+")";
				queryfinal+=filtro;
			}
			if( busquedaCorrecta == true ){
				ps = cn.prepareStatement(queryfinal);
			    
		        if(aux.indexOf("RFC")!=-1) 
				 ps.setString(param++, busca.RFC.toUpperCase() + "%");
		        if(aux.indexOf("NUMCLIENTE")!=-1)
				 ps.setInt(param++, busca.idCliente);
		        if(aux.indexOf("PRIMERAPELLIDO")!=-1)
				 ps.setString(param++, "%" + busca.apellidoPaterno.toUpperCase() + "%");
		        if(aux.indexOf("SEGUNDOAPELLIDO")!=-1)
				 ps.setString(param++, "%" + busca.apellidoMaterno.toUpperCase() + "%");
		        if(aux.indexOf("NOMBRE")!=-1)
				 ps.setString(param++, "%" + busca.nombreS.toUpperCase() + "%");
				myLogger.debug("Ejecutando buscaCliente = "+queryfinal);  
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					temporal = new ClienteVO();
					temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
					temporal.idClienteIBS = rs.getInt("EN_NUMCLIENTE_IBS");
					temporal.idSucursal = rs.getInt("EN_NUMSUCURSAL");
					temporal.estatus = rs.getInt("EN_ESTATUS");
					temporal.nombre = rs.getString("EN_NOMBRE");
					temporal.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
					temporal.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
					temporal.nombreCompleto = rs.getString("EN_NOMBRE_COMPLETO");
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
					temporal.dependientesEconomicos = rs.getInt("EN_DEPENDIENTES_ECONOMICOS");
					temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
					temporal.nivelEstudios = rs.getInt("EN_NIVEL_ESTUDIOS");
					temporal.curp = rs.getString("EN_CURP");
                                        temporal.LenguaIndigena = rs.getInt("en_LenguaIndigena");
                                        temporal.Discapacidad = rs.getInt("en_Discapacidad");
                                        temporal.UsodeInternet = rs.getInt("en_UsodeInternet");
                                        temporal.RedesSociales = rs.getInt("en_RedesSociales");
					array.add(temporal);
				}
			}
		}catch(SQLException sqle) {
			myLogger.error("buscaCliente",sqle);
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("buscaCliente",e);
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		clientes = new ClienteVO[array.size()];
	    for(int i=0;i<clientes.length; i++) clientes[i] = (ClienteVO)array.get(i);
	    return clientes;

	}
        
        public boolean updateSucursal(int numCliente, int nuevaSucursal) throws ClientesException{
		String query =	"UPDATE d_clientes SET en_numsucursal=? WHERE en_numcliente=?";
		Connection cn = null;
		PreparedStatement ps = null;
		boolean respuesta = false;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(1, nuevaSucursal);
			ps.setInt(2, numCliente);
                        myLogger.debug("Ejecutando = " + query);
                        myLogger.debug("Parametros [" + nuevaSucursal+", "+numCliente+"]");
			ps.executeUpdate();
                        respuesta=true;
		}
		catch(SQLException sqle) {
			myLogger.error("updateSucursal",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("updateSucursal",e);
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
		return respuesta;
	}
        
        public boolean updateRFC(int numCliente, String rfc ) throws ClientesException{
            
		String query =	"UPDATE d_clientes SET en_RFC=? WHERE en_numcliente=?";
		Connection cn = null;
		PreparedStatement ps = null;
		boolean respuesta = false;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setString(1, rfc);
			ps.setInt(2, numCliente);
                        myLogger.debug("Ejecutando = " + ps);
			ps.executeUpdate();
                        respuesta=true;
		}
		catch(SQLException sqle) {
			myLogger.error("updateRFC",sqle);
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			myLogger.error("updateRFC",e);
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
		return respuesta;
	}
}
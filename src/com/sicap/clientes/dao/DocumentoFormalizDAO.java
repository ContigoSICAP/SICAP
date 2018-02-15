package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.DocumentoFormalizVO;;

public class DocumentoFormalizDAO extends DAOMaster{

	public ArrayList<DocumentoFormalizVO> getDocumentosFormaliz() throws ClientesException {
		
		String query = "SELECT * FROM C_DOCUM_FORMAL_CONVENIO";
		
		ArrayList<DocumentoFormalizVO> lista=new ArrayList<DocumentoFormalizVO>();
		Connection cn = null;
		ResultSet res = null;
		
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeQuery();
			while (res.next()){
				DocumentoFormalizVO documento = new DocumentoFormalizVO();
				documento.tipoDocumento = res.getString("DF_TIPO_DOCUMENTO");
				documento.tipoOperacion = res.getInt("DF_TIPO_OPERACION");
				documento.numSucursal = res.getInt("DF_NUMSUCURSAL");
				documento.numConvenio = res.getInt("DF_NUMCONVENIO");
				documento.rutaArchivo = res.getString("DF_RUTA");
				documento.plantilla = res.getString("DF_PLANTILLA");
				documento.variable = res.getInt("DF_VARIABLE");
				lista.add(documento);
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getDocumentosFormaliz: " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getDocumentosFormaliz: " + e.getMessage());
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
	
	public ArrayList<DocumentoFormalizVO> getDocumentosConvenio(int numConvenio) throws ClientesException {
		
		String query =	"SELECT * FROM C_DOC_FORMAL_CONVENIO WHERE DF_NUMCONVENIO=?";
						
		ArrayList<DocumentoFormalizVO> lista=new ArrayList<DocumentoFormalizVO>();
		Connection cn = null;
		ResultSet res = null;
		
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(1, numConvenio);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeQuery();
			while (res.next()){
				DocumentoFormalizVO documento = new DocumentoFormalizVO();
				documento.tipoDocumento = res.getString("DF_TIPO_DOCUMENTO");
				documento.tipoOperacion = res.getInt("DF_TIPO_OPERACION");
				documento.numSucursal = res.getInt("DF_NUMSUCURSAL");
				documento.numConvenio = res.getInt("DF_NUMCONVENIO");
				documento.rutaArchivo = res.getString("DF_RUTA");
				documento.plantilla = res.getString("DF_PLANTILLA");
				documento.variable = res.getInt("DF_VARIABLE");
				lista.add(documento);
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getDocumentoConvenio: " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getDocumentoConveni: " + e.getMessage());
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
	
	
	public DocumentoFormalizVO getDocumentoFormaliz(String tipoDocumento, int numConvenio, int periodos) throws ClientesException{

		String query =	"SELECT * FROM C_DOCUM_FORMAL_CONVENIO WHERE DF_TIPO_DOCUMENTO = ? AND DF_NUMCONVENIO = ? AND DF_VARIABLE = ?";
						
		Connection cn = null;
		DocumentoFormalizVO documento = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1,tipoDocumento);
			ps.setInt(2,numConvenio);
			ps.setInt(3,periodos);
			Logger.debug("Ejecutando : "+query);
			Logger.debug("Parametros = ["+tipoDocumento+","+numConvenio+","+periodos+"]");
			ResultSet res = ps.executeQuery();
			if(res.next()){
				documento = new DocumentoFormalizVO();
				documento.tipoDocumento = res.getString("DF_TIPO_DOCUMENTO");
				documento.tipoOperacion = res.getInt("DF_TIPO_OPERACION");
				documento.numSucursal = res.getInt("DF_NUMSUCURSAL");
				documento.numConvenio = res.getInt("DF_NUMCONVENIO");
				documento.rutaArchivo = res.getString("DF_RUTA");
				documento.plantilla = res.getString("DF_PLANTILLA");
				documento.variable = res.getInt("DF_VARIABLE");
				
			}
			
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getDocumentoFormaliz : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getDocumentoFormaliz : "+e.getMessage());
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
		return documento;
	}

	public int addRepresentante(DocumentoFormalizVO documento) throws ClientesException {

		String query =	"INSERT INTO C_DOCUM_FORMALIZ_CONVENIO(DF_TIPO_DOCUMENTO, DF_TIPO_OPERACION, DF_NUMSUCURSAL, DF_NUMCONVENIO," +
						"DF_RUTA, DF_PLANTILLA, DF_VARIABLE) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
		Connection cn = null;
		int param = 1;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setString(param++, documento.tipoDocumento);
			ps.setInt(param++, documento.tipoOperacion);
			ps.setInt(param++, documento.numSucursal);
			ps.setInt(param++, documento.numConvenio);
			ps.setString(param++, documento.rutaArchivo);
			ps.setString(param++, documento.plantilla);
			ps.setInt(param++, documento.variable);
			
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addDocumentoFormaliz : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addDocumentoFormaliz: " + e.getMessage());
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
	 * Actualiza un documento de formalizacion
	 * @param DocumentoFormalizVO
	 * @return
	 * @throws ClientesException
	 */
	public int updateDocumentoFormaliz(DocumentoFormalizVO documento) throws ClientesException {

		String query =	"UPDATE C_DOCUM_FORMAL_CONVENIO SET DF_RUTA = ? ,DF_PLANTILLA = ?, DF_VARIABLE = ? "+
						"WHERE DF_NUMCONVENIO = ? AND DF_TIPO_DOCUMENTO = ?";
		Connection cn = null;
		int param = 1;
		int res = 0;
		try {
			PreparedStatement ps = null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setString(param++, documento.rutaArchivo);
			ps.setString(param++, documento.plantilla);
			ps.setInt(param++, documento.variable);
			ps.setInt(param++, documento.numConvenio);
			ps.setString(param++, documento.tipoDocumento);
			Logger.debug("Ejecutando = " + query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = " + res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateDocumentoFormaliz : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateDocumentoFormaliz: " + e.getMessage());
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

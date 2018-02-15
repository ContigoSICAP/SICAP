package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.BuroInternoVO;
import com.sicap.clientes.vo.BusquedaClientesVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.UsuarioVO;

public class BuroInternoDAO extends DAOMaster{
    public int addIncidenciaBuroInterno(BuroInternoVO buro, Connection con) throws ClientesException{
        Connection cn = null;
	int param = 1;
	int  res = 0;
	String query =	"INSERT INTO D_BURO_INTERNO(SBI_NUMERO_CLIENTE, " +
			"SBI_ESTATUS, SBI_MOTIVO_INGRESO, SBI_DESCRIPCION, "+
			"SBI_FECHA_ULTIMA_MODIFICACION, SBI_USUARIO_ULTIMA_MODIFICACION, SBI_SUCURSAL )"+
			"VALUES (?,?,?,?,now(),?,?)";
	try{
            PreparedStatement ps =null;
            if(con!=null){
                ps = con.prepareStatement(query);
            }
            else{
                cn = getConnection();
		ps = cn.prepareStatement(query);
            }
            ps.setInt(param++, buro.numCliente);			
            ps.setInt(param++, buro.estatus);
            ps.setInt(param++, buro.motivoIngreso);
            ps.setString(param++, buro.descripcion);
            ps.setString(param++, buro.usuarioUltimaModificacion);
            ps.setInt(param++, buro.numSucursal);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Usuario= "+buro.toString());
            res = ps.executeUpdate();
            Logger.debug("Resultado = "+res);
	}
	catch(SQLException sqle) {
            Logger.debug("SQLException en addIncidenciaBuroInterno : "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
	}
	catch(Exception e) {
            Logger.debug("Excepcion en addIncidenciaBuroInterno : "+e.getMessage());
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
    public BuroInternoVO[] buscaIncidenciaBuro(BuroInternoVO busca) throws ClientesException{
        ArrayList<BuroInternoVO> array = new ArrayList<BuroInternoVO>();
        BuroInternoVO temporal = null;
        BuroInternoVO buro[] = null;
        String aux="";
        StringBuffer query = new StringBuffer("SELECT * FROM D_BURO_INTERNO WHERE "); 
        int param = 1;
        Connection cn = null;
        boolean busquedaCorrecta = false;
        PreparedStatement ps = null;
        //Logger.debug("valor de param : "+param);
        try{
            if(busca.numCliente!=0){
                query.append("AND SBI_NUMERO_CLIENTE = ? ");
                aux+="NUMCLIENTE"; 
                busquedaCorrecta = true;
            }
            cn = getConnection();
            String queryfinal = query.toString().replaceFirst("AND ", "");
            if( busquedaCorrecta == true ){
                ps = cn.prepareStatement(queryfinal);	
                if(aux.indexOf("NUMCLIENTE")!=-1)
                    ps.setInt(param++, busca.numCliente);		        
                Logger.debug("Ejecutando buscaCliente = "+queryfinal);                              
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    temporal = new BuroInternoVO();
                    temporal.numCliente = rs.getInt("SBI_NUMERO_CLIENTE");
                    temporal.estatus = rs.getInt("SBI_ESTATUS");
                    temporal.motivoIngreso = rs.getInt("SBI_MOTIVO_INGRESO");
                    temporal.descripcion = rs.getString("SBI_DESCRIPCION");
                    temporal.fechaUltimaModificacion = rs.getTimestamp("SBI_FECHA_ULTIMA_MODIFICACION");
                    temporal.usuarioUltimaModificacion = rs.getString("SBI_USUARIO_ULTIMA_MODIFICACION");
                    temporal.numSucursal = rs.getInt("SBI_SUCURSAL");
                    array.add(temporal);
                }
            }
        }catch(SQLException sqle) {
            Logger.debug("SQLException en buscaIncidenciaBuro : "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }		
        catch(Exception e) {
            Logger.debug("Excepcion en buscaIncidenciaBuro : "+e.getMessage());
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
        buro = new BuroInternoVO[array.size()];
        for(int i=0;i<buro.length; i++) buro[i] = (BuroInternoVO)array.get(i);
        return buro;
    }
    
    public BuroInternoVO buscaCliente(int numCliente) throws ClientesException{   
	String query = "SELECT * FROM D_BURO_INTERNO WHERE SBI_NUMERO_CLIENTE = ? ";
	Connection cn = null;
	BuroInternoVO buro = null;
	try{
		cn = getConnection();
		PreparedStatement ps = cn.prepareStatement(query);
		ps.setInt(1,numCliente);
//		Logger.debug("Ejecutando : "+query);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			buro = new BuroInternoVO();
			buro.numCliente = numCliente;
			buro.numSucursal = rs.getInt("SBI_SUCURSAL");
			buro.motivoIngreso = rs.getInt("SBI_MOTIVO_INGRESO");
			buro.estatus = rs.getInt("SBI_ESTATUS");
			buro.descripcion = rs.getString("SBI_DESCRIPCION");
			buro.usuarioUltimaModificacion = rs.getString("SBI_USUARIO_ULTIMA_MODIFICACION");
			buro.fechaUltimaModificacion = rs.getTimestamp("SBI_FECHA_ULTIMA_MODIFICACION");
		}
//		Logger.debug("Cliente encontrado : "+cliente.toString());
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
        return buro;
    }
    
    
    public int updateIncidenciaBuroInterno (BuroInternoVO buro) throws ClientesException{

	String query =	"UPDATE D_BURO_INTERNO SET " +
			"SBI_ESTATUS = ?, SBI_DESCRIPCION = ?, SBI_MOTIVO_INGRESO = ?, SBI_FECHA_ULTIMA_MODIFICACION = now(),"+ 
			"SBI_SUCURSAL = ?, SBI_USUARIO_ULTIMA_MODIFICACION = ? WHERE SBI_NUMERO_CLIENTE = ?";
	
	Connection cn = null;
	int  res = 0;
	int  param = 1;
	try{
		PreparedStatement ps =null;
		cn = getConnection();
		ps = cn.prepareStatement(query);
		ps.setInt(param++, buro.estatus);
		ps.setString(param++, buro.descripcion);
		ps.setInt(param++, buro.motivoIngreso);
		ps.setInt(param++, buro.numSucursal);
		ps.setString(param++, buro.usuarioUltimaModificacion);
		ps.setInt(param++, buro.numCliente);
	
//		Logger.debug("Ejecutando = "+query);
//		Logger.debug("Usuario="+usuario);
		res = ps.executeUpdate();
//		Logger.debug("Resultado = "+res);
	}
	catch(SQLException sqle) {
		Logger.debug("SQLException en updateIncidenciaBuroInterno : "+sqle.getMessage());
		sqle.printStackTrace();
		throw new ClientesDBException(sqle.getMessage());
	}
	catch(Exception e) {
		Logger.debug("Excepcion en updateIncidenciaBuroInterno : "+e.getMessage());
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

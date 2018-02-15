package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.GestionVO;

public class GestionDAO extends DAOMaster{
	
	public void insertGestion(GestionVO gestion){
		String query = "insert into d_gestiones(ge_numcliente_t24, ge_numcontrato, ge_codigo_accion, ge_codigo_resultado, ge_codigo_carta, ge_fecha_captura, ge_numejecutivo, ge_comentarios) ";
			   query += "values (?,?,?,?,?,?,?,?)";
		int param = 1;
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(param++, gestion.idClienteT24);
			ps.setString(param++, gestion.contrato);
			ps.setString(param++, gestion.codigoAccion);
			ps.setString(param++, gestion.codigoResultado);
			ps.setString(param++, gestion.codigoCarta);
			ps.setDate(param++, gestion.fechaCaptura);
			ps.setInt(param++, gestion.numEjecutivo);
			ps.setString(param++, gestion.comentario);
			ps.executeUpdate();
			Logger.debug("Gestion guardada correctamente!!!");
		}
		catch(SQLException exc){
			Logger.debug("SQLException en GestionDAO.insertGestion::" + exc);
		}
		catch(NamingException exc){
			Logger.debug("NamingException en GestionDAO.insertGestion::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en GestionDAO.insertGestion::" + exc);
				}
			}
		}
	}
	
	public GestionVO[] getGestionesByContrato(String contrato){
		GestionVO[] gestionVO = null;
		ArrayList<GestionVO> array = new ArrayList<GestionVO>();
		String query = "SELECT * FROM D_GESTIONES WHERE GE_NUMCONTRATO = ?";
		Connection con = null;
		int param = 1;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(param++, contrato);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				GestionVO auxGes = new GestionVO();
				auxGes.codigoAccion = rs.getString("ge_codigo_accion");
				auxGes.codigoCarta = rs.getString("ge_codigo_carta");
				auxGes.codigoResultado = rs.getString("ge_codigo_resultado");
				auxGes.comentario = rs.getString("ge_comentarios");
				auxGes.contrato = rs.getString("ge_numcontrato");
				auxGes.fechaCaptura = rs.getDate("ge_fecha_captura");
				auxGes.idClienteT24 = rs.getString("ge_numcliente_t24");
				auxGes.numEjecutivo = rs.getInt("ge_numejecutivo");
				array.add(auxGes);
			}
			gestionVO = new GestionVO[array.size()];
			for(int i = 0; i < gestionVO.length; i++){
				gestionVO[i] = (GestionVO) array.get(i);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en GestionDAO.getGestionesByContrato::" + exc);
		}
		catch(NamingException exc){
			Logger.debug("NamingException en GestionDAO.getGestionesByContrato::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en GestionDAO.getGestionesByContrato::" + exc);
				}
			}
		}
		return gestionVO;
	}
}
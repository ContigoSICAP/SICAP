package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.BeneficiarioVO;
import com.sicap.clientes.vo.SegurosVO;
import java.sql.Savepoint;

public class BeneficiarioDAO extends DAOMaster {

    public ArrayList<BeneficiarioVO> getBeneficiarios (int idCliente, int idSolicitud) throws ClientesException{
        
        String query = "SELECT be_nombre,be_apaterno,be_amaterno,be_relacion,be_otra_relacion,be_porcentaje,be_fecha_nacimiento "
                +"FROM d_beneficiarios WHERE be_numcliente=? AND be_numsolicitud=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<BeneficiarioVO> beneficiarios = new ArrayList<BeneficiarioVO>();
        String fecNacimiento = "";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCliente+", "+idSolicitud+"]");
            
            res = ps.executeQuery();
            while (res.next()){
                fecNacimiento = res.getString("be_fecha_nacimiento").substring(8, 10)+"/"+res.getString("be_fecha_nacimiento").substring(5, 7)+"/"+res.getString("be_fecha_nacimiento").substring(0, 4);
                beneficiarios.add(new BeneficiarioVO(res.getString("be_nombre"), res.getString("be_apaterno"), res.getString("be_amaterno"),
                        res.getInt("be_relacion"), res.getString("be_otra_relacion"), res.getInt("be_porcentaje"), fecNacimiento));
            }
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en addAsegurados: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en addAsegurados: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return beneficiarios;
    }

    public void addBeneficiario(Connection cn, Savepoint save, SegurosVO seguro, BeneficiarioVO beneficiario) throws ClientesException {

        String query = "INSERT INTO D_BENEFICIARIOS (BE_NUMCLIENTE, BE_NUMSOLICITUD, BE_NUMSEGURO, BE_NUMBENEFICIARIO,"
                + "BE_NOMBRE, BE_APATERNO, BE_AMATERNO, BE_RELACION, BE_OTRA_RELACION, BE_PORCENTAJE, BE_FECHA_NACIMIENTO)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            //cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, seguro.idCliente);
            ps.setInt(2, seguro.idSolicitud);
            ps.setInt(3, seguro.numSeguro);
            ps.setInt(4, beneficiario.getNumBeneficiario());
            ps.setString(5, beneficiario.getNombre());
            ps.setString(6, beneficiario.getaPaterno());
            ps.setString(7, beneficiario.getaMaterno());
            ps.setInt(8, beneficiario.getRelacion());
            ps.setString(9, beneficiario.getOtraRelacion());
            ps.setInt(10, beneficiario.getPorcentaje());
            ps.setString(11, beneficiario.getFechaNacimiento());
            
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros["+seguro.idCliente+", "+seguro.idSolicitud+", "+seguro.numSeguro+", "+beneficiario.getNumBeneficiario()+", "+beneficiario.getNombre()+", "+
                    beneficiario.getaPaterno()+", "+beneficiario.getaMaterno()+", "+beneficiario.getRelacion()+
                    beneficiario.getOtraRelacion()+", "+beneficiario.getPorcentaje()+", "+beneficiario.getFechaNacimiento()+"]");
            
            ps.executeUpdate();
            //Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            Logger.debug("SQLException en addBeneficiario : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en addBeneficiario : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }*/
    }

    public int updateBeneficiarios(BeneficiarioVO beneficiario) throws ClientesException {

        String query = "UPDATE D_BENEFICIARIOS SET BE_NOMBRE = ?, BE_APATERNO = ?, BE_AMATERNO = ?, "
                + "BE_RELACION = ?, BE_OTRA_RELACION = ?, BE_PORCENTAJE = ? WHERE BE_NUMCLIENTE = ?"
                + "AND BE_NUMSOLICITUD = ? AND BE_NUMSEGURO = ? AND BE_NUMBENEFICIARIO = ?";

        Connection cn = null;
        int res = 0;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, beneficiario.nombre);
            ps.setString(param++, beneficiario.aPaterno);
            ps.setString(param++, beneficiario.aMaterno);
            ps.setInt(param++, beneficiario.relacion);
            ps.setString(param++, beneficiario.otraRelacion);
            ps.setInt(param++, beneficiario.porcentaje);
            ps.setInt(param++, beneficiario.idCliente);
            ps.setInt(param++, beneficiario.idSolicitud);
            ps.setInt(param++, beneficiario.numSeguro);
            ps.setInt(param++, beneficiario.numBeneficiario);

            Logger.debug("Ejecutando = " + query);
            Logger.debug("Conyuge = " + beneficiario.toString());
            res = ps.executeUpdate();
            Logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            Logger.debug("SQLException en updateBeneficiario : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en updateBeneficiario : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    
    public ArrayList<String> getNombreBeneficiario(int idCliente, int idSolicitud) throws ClientesException{
            String query = "SELECT be_nombre, be_apaterno, be_amaterno FROM d_beneficiarios WHERE be_numcliente=? AND be_numsolicitud=?";
            ArrayList<String> nombre = new ArrayList<String>();
            Connection cn = null;
            
            try {
                cn=getConnection();
                PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			ps.setInt(2,idSolicitud);
			Logger.debug("Ejecutando : "+query);
                        Logger.debug("Parametros ["+idCliente+", "+idSolicitud+"]");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
                            //nombre.add(new String(rs.getString("be_nombre")+" "+rs.getString("be_apaterno")+" "+rs.getString("be_amaterno")));
                            //String nombreBen = (rs.getString("be_nombre")+" "+rs.getString("be_apaterno")+" "+rs.getString("be_amaterno"));
                            nombre.add(rs.getString("be_nombre")+" "+rs.getString("be_apaterno")+" "+rs.getString("be_amaterno"));                            
                        }
                        /*if(rs.next()){
                            nombre.add(rs.getString("be_nombre")+" "+rs.getString("be_apaterno")+" "+rs.getString("be_amaterno"));
                        }*/                
            }
            catch(SQLException sqle) {
			Logger.debug("SQLException en getNombreBeneficiario : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Exception en getNombreBeneficiario : "+e.getMessage());
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
            return nombre;
    }
    
    public ArrayList<Integer> getPorcentajes(int idCliente, int idSolicitud) throws ClientesException{
            String query = "SELECT be_porcentaje FROM d_beneficiarios WHERE be_numcliente=? AND be_numsolicitud=?";
            ArrayList<Integer> porcentajes = new ArrayList<Integer>();
            Connection cn = null;
            
            try {
                cn=getConnection();
                PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			ps.setInt(2,idSolicitud);
			Logger.debug("Ejecutando : "+query);
                        Logger.debug("Parametros ["+idCliente+", "+idSolicitud+"]");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){porcentajes.add(rs.getInt("be_porcentaje"));
                        }  
            }
            catch(SQLException sqle) {
			Logger.debug("SQLException en getPorcentajes : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Exception en getPorcentajes : "+e.getMessage());
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
            return porcentajes;
    }

    public int getNext(int idCliente, int idSolicitud, int numSeguro) throws ClientesException {

        String query = "SELECT COALESCE(MAX(BE_NUMBENEFICIARIO),0)+1 AS NEXT FROM D_BENEFICIARIOS WHERE BE_NUMCLIENTE = ? AND BE_NUMSOLICITUD = ?"
                + " AND BE_NUMSEGURO = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, numSeguro);
            ResultSet rs = ps.executeQuery();
            Logger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getNext : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getNext : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return next;
    }

    public void deleteBeneficiario(Connection con, Savepoint save, SegurosVO seguros) throws ClientesException {

        String query = "DELETE FROM d_beneficiarios WHERE be_numcliente=? AND be_numsolicitud=? AND be_numseguro=?";
        //Connection con = null;
        PreparedStatement ps = null;
        try {
            //con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, seguros.idCliente);
            ps.setInt(2, seguros.idSolicitud);
            ps.setInt(3, seguros.numSeguro);

            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [" + seguros.idCliente + ", " + seguros.idSolicitud + ", " + seguros.numSeguro + "]");

            ps.executeUpdate();

        } catch (SQLException sqle) {
            Logger.debug("SQLException en deleteBeneficiario: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en deleteBeneficiario: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } /*finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }*/
    }
}

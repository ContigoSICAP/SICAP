/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CicloMigracionVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.MigracionInformacionVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class MigracionInformacionDAO extends DAOMaster{
    
    private Connection con = null;
    private PreparedStatement ps = null;
    private Statement st = null;
    private ResultSet res = null;
    private String sql = "";
    
    public int findID (String campo, String tabla) throws ClientesException{
        
        int id = 0;
        try {
            con = getConnection();
            sql = "SELECT MAX("+campo+") AS id FROM "+tabla+";";
            st = con.createStatement();
            Logger.debug("Tabla ["+tabla+"]");
            res = st.executeQuery(sql);
            if(res.next())
                id = res.getInt(1);
        } catch (SQLException exc) {
            Logger.debug("SQLException en findID:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findID:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return id;
    }
    
    public void insertRegistros (String query) throws ClientesException{
        
        int id = 0;
        try {
            con = getConnection();
            st = con.createStatement();
            Logger.debug(query);
            if(!st.execute(query))
                System.out.println("Inserto");
        } catch (SQLException exc) {
            System.out.println("**********************Problema "+ps);
            Logger.debug("SQLException en insertRegistros:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en insertRegistros:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
    }
    
    public void updateIdMigracion (String query) throws ClientesException{
        
        try {
            con = getConnection();
            st = con.createStatement();
            Logger.debug(query);
            st.execute(query);
        } catch (SQLException exc) {
            Logger.debug("SQLException en updateIdMigracion:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en updateIdMigracion:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
    }
    
    public boolean findTasa (String tasa) throws ClientesException{
        
        boolean existe = false;
        try {
            con = getConnection();
            sql = "SELECT tg_numtasa FROM c_tasas_grupal WHERE tg_descripcion=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, tasa);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+tasa+"]");
            res = ps.executeQuery();
            if(res.next())
                existe = true;
        } catch (SQLException exc) {
            Logger.debug("SQLException en getSaldosAC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getSaldosAC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return existe;
    }
    
    public MigracionInformacionVO findSucursal (String sucursal) throws ClientesException{
        
        MigracionInformacionVO info = null;
        try {
            con = getConnection();
            sql = "SELECT su_numsucursal,su_nombre FROM c_sucursales WHERE su_nombre=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, sucursal);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+sucursal+"]");
            res = ps.executeQuery();
            if(res.next())
                info = new MigracionInformacionVO(res.getInt("su_numsucursal"), res.getString("su_nombre"));
            else
                info = new MigracionInformacionVO(0, "0");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return info;
    }
    
    public int findClientes (String nombre, String paterno, String materno, String nacimiento) throws ClientesException{
        
        int existe = 0;
        try {
            con = getConnection();
            sql = "SELECT en_numcliente FROM d_clientes WHERE en_nombre LIKE '%"+nombre+"%' AND en_primer_apellido LIKE '%"+paterno+"%' AND en_segundo_apellido LIKE '%"+materno+"%' AND en_fecha_nac LIKE '%"+nacimiento+"%';";
            st = con.createStatement();
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+nombre+", "+paterno+", "+materno+", "+nacimiento+"]");
            res = st.executeQuery(sql);
            if(res.next())
                existe = res.getInt("en_numcliente");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findClientes:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findClientes:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return existe;
    }
    
    public int findGrupo (String nombre, int sucursal) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT gr_numgrupo FROM d_grupos WHERE gr_numsucursal=? AND gr_nombre like '"+nombre+"%';";
            ps = con.prepareStatement(sql);
            ps.setInt(1, sucursal);
            Logger.debug("Ejecutando = "+ps);
            //Logger.debug("Parametros ["+nombre+", "+sucursal+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("gr_numgrupo");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findGrupo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findGrupo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findIdSucursal (int idMigra, int origen) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT su_numsucursal FROM c_sucursales WHERE su_id_migracion=? AND su_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMigra);
            ps.setInt(2, origen);
            Logger.debug("Ejecutando = "+ps);
            //Logger.debug("Parametros ["+idMigra+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("su_numsucursal");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findIdCliente (int idMigra, int origen) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT en_numcliente FROM d_clientes WHERE en_id_migracion=? AND en_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMigra);
            ps.setInt(2, origen);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+idMigra+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("en_numcliente");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdCliente:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdCliente:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public ClienteVO findIdClienteDupli (int idCliente) throws ClientesException{
        
        //int numero = 0;
        ClienteVO cliente = new ClienteVO();
        try {
            con = getConnection();
            sql = "SELECT en_id_migracion,en_origenmigracion FROM d_clientes WHERE en_numcliente=? AND en_id_migracion!=0";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+idMigra+"]");
            res = ps.executeQuery();
            if(res.next()){
                cliente.setIdMigracion(res.getInt("en_id_migracion"));
                cliente.setOrigenMigracion(res.getInt("en_origenmigracion"));
            }
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdClienteDupli:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdClienteDupli:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        //return numero;
        return cliente;
    }
    
    public MigracionInformacionVO findInfoCliente (int idCliente) throws ClientesException{
        
        MigracionInformacionVO cliente = null;
        try {
            con = getConnection();
            sql = "SELECT en_numsucursal,en_nombre_completo FROM d_clientes WHERE en_numcliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+idCliente+"]");
            res = ps.executeQuery();
            if(res.next())
                cliente = new MigracionInformacionVO(res.getInt("en_numsucursal"), res.getString("en_nombre_completo"));
            else
                cliente = new MigracionInformacionVO(0, "");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findInfoCliente:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findInfoCliente:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return cliente;
    }
    
    public int findIdGrupo (int idMigra, int origen) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT gr_numgrupo FROM d_grupos where gr_id_migracion=? AND gr_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMigra);
            ps.setInt(2, origen);
            Logger.debug("Ejecutando = "+ps);
            //Logger.debug("Parametros ["+idMigra+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("gr_numgrupo");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdGrupo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdGrupo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findNumSucursal (int idGrupo) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT gr_numsucursal FROM d_grupos where gr_numgrupo=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idGrupo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+idGrupo+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("gr_numsucursal");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findNumSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findNumSucursal:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findIdEjecutivo (int idMigra, int origen) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT ej_numejecutivo FROM c_ejecutivos WHERE ej_id_migracion=? AND ej_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMigra);
            ps.setInt(2, origen);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+idMigra+"]");
            res = ps.executeQuery();
            while(res.next())
                numero = res.getInt("ej_numejecutivo");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdEjecutivo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdEjecutivo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findSolicitud (int idCliente) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT MAX(so_numsolicitud) AS solicitud FROM d_solicitudes WHERE so_numcliente=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+idCliente+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("solicitud");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findIdSolicitud (int idCliente, int numSolicitud, int origen) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT so_numsolicitud FROM d_solicitudes WHERE so_numcliente=? AND so_id_migracion=? and so_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ps.setInt(2, numSolicitud);
            ps.setInt(3, origen);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+idCliente+", "+numSolicitud+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("so_numsolicitud");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findIdSolicitud (int numSolicitud) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT so_numsolicitud FROM d_solicitudes WHERE so_id_migracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, numSolicitud);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+numSolicitud+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("so_numsolicitud");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findIdSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findIdSolicitud:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public boolean findRFC (String rfc) throws ClientesException{
        
        boolean existe = false;
        try {
            con = getConnection();
            sql = "SELECT en_rfc FROM d_clientes WHERE en_rfc=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, rfc);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+rfc+"]");
            res = ps.executeQuery();
            if(res.next())
                existe = true;
        } catch (SQLException exc) {
            Logger.debug("SQLException en findRFC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findRFC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return existe;
    }
    
    public int findEstado (String nombre) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT es_numestado FROM c_estados WHERE es_nombre=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+nombre+"]");
            res = ps.executeQuery();
            if(res.next())
                numero = res.getInt("es_numestado");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findEstado:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findEstado:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public int findMunicipio (int estado, String nombre) throws ClientesException{
        
        int numero = 0;
        try {
            con = getConnection();
            sql = "SELECT mu_nummunicipio FROM c_municipios WHERE mu_numestado="+estado+" AND mu_nombre LIKE '%"+nombre+"%';";
            st = con.createStatement();
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+estado+", "+nombre+"]");
            res = st.executeQuery(sql);
            if(res.next())
                numero = res.getInt("mu_nummunicipio");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findMunicipio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findMunicipio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return numero;
    }
    
    public MigracionInformacionVO findColonia (int estado, int municipio, String nombre, boolean completo) throws ClientesException{
        
        MigracionInformacionVO info = null;
        int numero = 0;
        try {
            con = getConnection();
            if(completo){
                sql = "SELECT co_numcolonia,co_cp FROM c_colonias WHERE co_numestado=? AND co_nummunicipio=? AND co_nombre_colonia like '%"+nombre+"%';";
                ps = con.prepareStatement(sql);
                ps.setInt(1, estado);
                ps.setInt(2, municipio);
            }else{
                sql = "SELECT co_numcolonia,co_cp FROM c_colonias WHERE co_numestado=? AND co_nombre_colonia like '%"+nombre+"%' LIMIT 1;";
                ps = con.prepareStatement(sql);
                ps.setInt(1, estado);
            }
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+estado+", "+municipio+", "+nombre+"]");
            res = ps.executeQuery();
            if(res.next()){
                info = new MigracionInformacionVO(res.getInt("co_numcolonia"), res.getString("co_cp"));
            }else
                info = new MigracionInformacionVO(0, "0");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findColonia:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findColonia:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return info;
    }
    
    public int findCiclo (int idCliente) throws ClientesException{
        
        int ciclo = 0;
        try {
            con = getConnection();
            sql = "SELECT MAX(ci_numciclo) AS ciclo FROM d_ciclos_grupales WHERE ci_numgrupo=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+estado+", "+municipio+", "+nombre+"]");
            res = ps.executeQuery();
            while(res.next())
                ciclo = res.getInt("ciclo");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findCiclo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findCiclo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return ciclo;
    }
    
    public int findNumCiclo (int idGrupo, String referencia, int origen) throws ClientesException{
        
        int ciclo = 0;
        try {
            con = getConnection();
            sql = "SELECT ci_numciclo FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_id_migracion=? AND ci_origenmigracion=?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idGrupo);
            ps.setString(2, referencia);
            ps.setInt(3, origen);
            //Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+estado+", "+municipio+", "+nombre+"]");
            res = ps.executeQuery();
            while(res.next())
                ciclo = res.getInt("ci_numciclo");
        } catch (SQLException exc) {
            Logger.debug("SQLException en findNumCiclo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en findNumCiclo:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return ciclo;
    }
    
    public ArrayList<ClienteVO> getArrClientes (int conteo) throws ClientesException{
    //public ArrayList<ClienteVO> getArrClientes () throws ClientesException{
        
        ArrayList<ClienteVO> arrClientes = new ArrayList<ClienteVO>();
        try {
            con = getMigConnection();
            sql = "SELECT cl_numcliente,TRIM(CONCAT(cl_nombre1,' ',cl_nombre2)) AS nombre,cl_apaterno,cl_amaterno,cl_nacimiento,cl_rfc FROM clientes WHERE cl_rfc='X' LIMIT ?,500;";
            //sql = "SELECT cl_numcliente,TRIM(CONCAT(cl_nombre1,' ',cl_nombre2)) AS nombre,cl_apaterno,cl_amaterno,cl_nacimiento,cl_rfc FROM clientes WHERE cl_rfc='X' LIMIT 500;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            //Logger.debug("Parametros ["+nombre+", "+paterno+", "+materno+", "+nacimiento+"]");
            res = ps.executeQuery();
            while(res.next())
                arrClientes.add(new ClienteVO(res.getInt("cl_numcliente"), res.getString("nombre"), res.getString("cl_apaterno"), res.getString("cl_amaterno"), res.getString("cl_rfc"), res.getDate("cl_nacimiento")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrClientes:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrClientes:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrClientes;
    }
    
    public ArrayList<ClienteVO> getArrClientesLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<ClienteVO> arrClientes = new ArrayList<ClienteVO>();
        try {
            if(origen == 1)
                con = getMigConnection();
            else if (origen == 2)
                con = getMigCredexConnection();
            sql = "SELECT cll_sucursal,cll_numcliente,CONCAT(cll_nombre1,' ',cll_nombre2) AS nombre,cll_apaterno,cll_amaterno,cll_nacimiento,cll_rfc,cll_sexo,cll_nacionalidad,cll_tipodocumento,"
                    + "cll_noidentificacion,cll_edocivil,cll_numdependientes,cll_calle,cll_numext,cll_numint,cll_estado,cll_municipio,cll_colonia,cll_sitvivienda,cll_antivivienda,cll_telefono "
                    + "FROM clienteslimpio where id_clientel>=100 LIMIT ?,500;";
                    //+ "FROM clienteslimpio LIMIT ?,500;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrClientes.add(new ClienteVO(res.getInt("cll_numcliente"), res.getString("nombre"), res.getString("cll_apaterno"), res.getString("cll_amaterno"), res.getString("cll_rfc"), res.getDate("cll_nacimiento"),
                        res.getString("cll_noidentificacion"), res.getInt("cll_numdependientes"), res.getInt("cll_sucursal"), res.getString("cll_calle"), res.getString("cll_numext"), res.getString("cll_numint"),
                        res.getString("cll_estado"), res.getString("cll_municipio"), res.getString("cll_colonia"), res.getString("cll_sitvivienda"), res.getInt("cll_antivivienda"), res.getString("cll_telefono"),
                        res.getString("cll_tipodocumento"), res.getString("cll_sexo"), res.getString("cll_nacionalidad"), res.getString("cll_edocivil")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrClientesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrClientesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrClientes;
    }
    
    public ArrayList<SolicitudVO> getArrSolicitudesLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<SolicitudVO> arrSolicitudes = new ArrayList<SolicitudVO>();
        try {
            if(origen == 1)
                con = getMigConnection();
            else if (origen == 2)
                con = getMigCredexConnection();
            sql = "SELECT sol_numcliente,sol_numsolicitud,sol_fechafirma,sol_monto,sol_plazo,sol_frecuencia FROM solicitudlimpio WHERE id_solicitudl>=100 LIMIT ?,500;";
            //sql = "SELECT sol_numcliente,sol_numsolicitud,sol_fechafirma,sol_monto,sol_plazo,sol_frecuencia FROM solicitudlimpio LIMIT ?,500;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrSolicitudes.add(new SolicitudVO(res.getInt("sol_numcliente"), res.getInt("sol_numsolicitud"), res.getDate("sol_fechafirma"), res.getDouble("sol_monto"), res.getInt("sol_plazo"), res.getString("sol_frecuencia")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrSolicitudesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrSolicitudesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrSolicitudes;
    }
    
    public GrupoVO getNombreGrupoCR (String referencia, int origen) throws ClientesException{
        
        GrupoVO grupo = new GrupoVO();
        try {
            if(origen == 1)
                con = getMigConnection();
            else if (origen == 2)
                con = getMigCredexConnection();
            sql = "SELECT grl_nombre,grl_numsucursal,grl_numero FROM gruposlimpio WHERE grl_numero=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, referencia);
            Logger.debug("Ejecutando = "+ps);
            //Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next()){
                grupo.setSucursal(res.getInt("grl_numsucursal"));
                grupo.setNombre(res.getString("grl_nombre"));
                if(origen == 2)
                    grupo.setIdGrupoOriginal(res.getInt("grl_numero"));
            }
        } catch (SQLException exc) {
            Logger.debug("SQLException en getNombreGrupoCR:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getNombreGrupoCR:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return grupo;
    }
    
    public ArrayList<GrupoVO> getArrGruposLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<GrupoVO> arrGrupos = new ArrayList<GrupoVO>();
        try {
            if(origen == 1)
                con = getMigConnection();
            else if (origen == 2)
                con = getMigCredexConnection();
            //sql = "SELECT grl_numsucursal,grl_numero,grl_nombre,grl_fechacaptura FROM gruposlimpio LIMIT ?,500;";
            sql = "SELECT grl_numsucursal,grl_numero,grl_nombre,grl_fechacaptura FROM gruposlimpio where id_grupol>=100 LIMIT ?,500;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrGrupos.add(new GrupoVO(res.getInt("grl_numsucursal"), res.getString("grl_nombre"), res.getDate("grl_fechacaptura"), res.getString("grl_numero")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrGruposLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrGruposLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrGrupos;
    }
    
    public ArrayList<CicloMigracionVO> getArrCiclosLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<CicloMigracionVO> arrCiclos = new ArrayList<CicloMigracionVO>();
        try {
            if(origen == 1){
                con = getMigConnection();
                sql = "SELECT cil_numgrupo,cil_diareunion,cil_horareunion,cil_ejecutivo,cil_capital,cil_fechacaptura,cil_plazo,cil_calle,cil_numext,cil_numint,cil_estado,cil_municipio,cil_colonia,0 AS cil_numciclo,cil_tasa "
                        +"FROM cicloslimpio where id_ciclol>=100 LIMIT ?,500;";
            }
            else if(origen == 2){
                con = getMigCredexConnection();
                sql = "SELECT cil_numgrupo,cil_diareunion,cil_horareunion,cil_ejecutivo,cil_capital,cil_fechacaptura,cil_plazo,cil_calle,cil_numext,cil_numint,cil_estado,cil_municipio,cil_colonia,cil_numciclo,cil_tasa "
                        +"FROM cicloslimpio where id_ciclol>=100 LIMIT ?,500;";
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrCiclos.add( new CicloMigracionVO(res.getString("cil_numgrupo"), res.getString("cil_diareunion"), res.getString("cil_horareunion"), res.getInt("cil_ejecutivo"), res.getDate("cil_fechacaptura"), res.getString("cil_calle"),
                        res.getString("cil_numext"), res.getString("cil_numint"), res.getString("cil_colonia"), res.getString("cil_municipio"), res.getString("cil_estado"), res.getDouble("cil_capital"), res.getInt("cil_plazo"), res.getInt("cil_numciclo"), res.getDouble("cil_tasa")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrCiclosLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrCiclosLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrCiclos;
    }
    
    public ArrayList<CicloMigracionVO> getArrSaldosLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<CicloMigracionVO> arrCiclos = new ArrayList<CicloMigracionVO>();
        try {
            if(origen == 1){
                con = getMigConnection();
                sql = "SELECT cil_numgrupo,cil_ejecutivo,cil_capital,cil_fechacaptura,cil_plazo,cil_cuota,0 AS cil_numciclo,cil_tasa "
                    +"FROM cicloslimpio where id_ciclol>=100 LIMIT ?,500;";
            }else if(origen == 2){
                con = getMigCredexConnection();
                sql = "SELECT cil_numgrupo,cil_ejecutivo,cil_capital,cil_fechacaptura,cil_plazo,cil_cuota,cil_numciclo,cil_tasa "
                    +"FROM cicloslimpio where id_ciclol>=100 LIMIT ?,500;"; 
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrCiclos.add( new CicloMigracionVO(res.getString("cil_numgrupo"), res.getInt("cil_ejecutivo"), res.getDate("cil_fechacaptura"),
                        res.getDouble("cil_cuota"), res.getInt("cil_plazo"), res.getDouble("cil_capital"), res.getInt("cil_numciclo"), res.getDouble("cil_tasa")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrSaldosLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrSaldosLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrCiclos;
    }
    
    public ArrayList<IntegranteCicloVO> getArrIntegrantesLimpio (int conteo, int origen) throws ClientesException{
        
        ArrayList<IntegranteCicloVO> arrIntegrantes = new ArrayList<IntegranteCicloVO>();
        try {
            if(origen == 1){
                con = getMigConnection();
                sql = "SELECT inl_numgrupo,inl_numcliente,inl_numsolicitud,inl_monto,inl_fechacaptura,0 as inl_numciclo FROM integranteslimpio where id_integrantesl>=100 LIMIT ?,500;";
            }else if(origen == 2){
                con = getMigCredexConnection();
                sql = "SELECT inl_numgrupo,inl_numcliente,inl_numsolicitud,inl_monto,inl_fechacaptura,inl_numciclo FROM integranteslimpio where id_integrantesl>=100 LIMIT ?,500;";
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, conteo);
            Logger.debug("Ejecutando = "+sql);
            Logger.debug("Parametros ["+conteo+"]");
            res = ps.executeQuery();
            while(res.next())
                arrIntegrantes.add(new IntegranteCicloVO(res.getInt("inl_numcliente"), res.getInt("inl_numsolicitud"), res.getDouble("inl_monto"), res.getString("inl_numgrupo"), res.getDate("inl_fechacaptura"), res.getInt("inl_numciclo")));
        } catch (SQLException exc) {
            Logger.debug("SQLException en getArrIntegrantesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en getArrIntegrantesLimpio:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrIntegrantes;
    }
    
    public void updateRFC (String query, int origen) throws ClientesException{
        
        try {
            if(origen == 1)
                con = getMigConnection();
            else if (origen == 2)
                con = getMigCredexConnection();
            st = con.createStatement();
            Logger.debug(query);
            st.execute(query);
        } catch (SQLException exc) {
            Logger.debug("SQLException en updateRFC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } catch (Exception exc) {
            Logger.debug("NamingException en updateRFC:" + exc);
            exc.printStackTrace();
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                throw new ClientesDBException(exc.getMessage());
            }
        }
    }
    
}

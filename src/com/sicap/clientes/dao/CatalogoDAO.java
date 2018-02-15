package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.CatBancosSeguroVO;
import com.sicap.clientes.vo.CatCuentasSegurosVO;
import com.sicap.clientes.vo.CatParentescoVO;
import com.sicap.clientes.vo.CatRelacionVO;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CodigoPostalVO;
import com.sicap.clientes.vo.ColoniaVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.DespachosVO;
import com.sicap.clientes.vo.MontoMaximoVO;
import com.sicap.clientes.vo.ParametroScoringVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.SumaAseguradaVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.TipoSeguroVO;

import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class CatalogoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(CatalogoDAO.class);
    
    public ComisionVO[] getCatalogoComisiones(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<ComisionVO> array = new ArrayList<ComisionVO>();
        ComisionVO temporal = null;
        ComisionVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                temporal = new ComisionVO();
                temporal.id = rs.getInt(1);
                temporal.descripcion = rs.getString(2);
                temporal.valor = rs.getDouble(3);
                temporal.porcentaje = rs.getDouble(4);
                temporal.status = rs.getString(5);
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoComisiones", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoComisiones", e);
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
        elementos = new ComisionVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (ComisionVO) array.get(i);
        }
        return elementos;

    }

    public TasaInteresVO[] getCatalogoTasasInteres(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<TasaInteresVO> array = new ArrayList<TasaInteresVO>();
        TasaInteresVO temporal = null;
        TasaInteresVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                temporal = new TasaInteresVO();
                temporal.id = rs.getInt(1);
                temporal.descripcion = rs.getString(2);
                temporal.valor = rs.getDouble(3);
                temporal.status = rs.getString(4);
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoTasasInteres", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoTasasInteres", e);
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
        elementos = new TasaInteresVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (TasaInteresVO) array.get(i);
        }
        return elementos;

    }

    public CatalogoVO[] getCatalogo(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        CatalogoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt(1);
                temporal.descripcion = rs.getString(2);
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoTasasInteres", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoTasasInteres", e);
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
        elementos = new CatalogoVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (CatalogoVO) array.get(i);
        }
        return elementos;

    }

    public CatalogoVO[] getEjecutivos(int idSucursal) throws ClientesException {

        String query = "SELECT EJ_NUMEJECUTIVO, CONCAT(EJ_NOMBRE, ' ', EJ_APATERNO, ' ', COALESCE(EJ_AMATERNO,'')) "
                + "NOMBRE FROM C_EJECUTIVOS WHERE EJ_NUMSUCURSAL = ?";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        CatalogoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt("EJ_NUMEJECUTIVO");
                temporal.descripcion = rs.getString("NOMBRE");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivos", e);
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
        elementos = new CatalogoVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (CatalogoVO) array.get(i);
        }
        return elementos;

    }
    
    public ArrayList<CatalogoVO> getEjecutivosActivos(int idSucursal) throws ClientesException {

        String query = "SELECT ej_numejecutivo, CONCAT(ej_nombre, ' ', ej_apaterno, ' ', COALESCE(ej_amaterno,'')) nombre "
                + "FROM c_ejecutivos WHERE ej_numsucursal = ? AND ej_status='A' AND ej_tipo_ejecutivo IN (1,2)";
        ArrayList<CatalogoVO> ejecutivosArray = new ArrayList<CatalogoVO>();
        CatalogoVO catalogo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idSucursal + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                catalogo = new CatalogoVO();
                catalogo.id = rs.getInt("ej_numejecutivo");
                catalogo.descripcion = rs.getString("nombre");
                ejecutivosArray.add(catalogo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getEjecutivosActivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEjecutivosActivos", e);
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
        return ejecutivosArray;

    }
    
    public ArrayList<CatalogoVO> getIntegrantesComite(int numEquipo, int numCiclo) throws ClientesException {

        String query = "SELECT ic_rol, en_nombre_completo FROM d_integrantes_ciclo LEFT JOIN d_clientes " +
                "ON (ic_numcliente=en_numcliente) " +
                "WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_rol>0";
        ArrayList<CatalogoVO> integrantesComite = new ArrayList<CatalogoVO>();
        CatalogoVO catalogo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + numEquipo + ", "+ numCiclo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                catalogo = new CatalogoVO();
                catalogo.id = rs.getInt("ic_rol");
                catalogo.descripcion = rs.getString("en_nombre_completo");
                integrantesComite.add(catalogo);
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesComite", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesComite", e);
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
        return integrantesComite;

    }
    
    public ArrayList<CatalogoVO> getBancos() throws ClientesException {

        String query = "SELECT * FROM c_bancos WHERE ba_estatus = 1";
        ArrayList<CatalogoVO> catalogo = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt("ba_numbanco");
                temporal.descripcion = rs.getString("ba_nombre");
                catalogo.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getBancos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getBancos", e);
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
        return catalogo;

    }
    
    public ArrayList<CatalogoVO> getBancosTarjeta() throws ClientesException {

        String query = "SELECT * FROM c_bancos WHERE ba_pagotarjeta = 1";
        ArrayList<CatalogoVO> catalogo = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt("ba_numbanco");
                temporal.descripcion = rs.getString("ba_nombre");
                catalogo.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getBancos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getBancos", e);
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
        return catalogo;

    }

    public CatalogoVO[] getRepresentantes(int idSucursal) throws ClientesException {

        String query = "SELECT RP_NUMREPRESENTANTE, RP_NOMBRE FROM C_REPRESENTANTES WHERE (RP_STATUS = 'A' && RP_NUMSUCURSAL = ?)";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        CatalogoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt("RP_NUMREPRESENTANTE");
                temporal.descripcion = rs.getString("RP_NOMBRE");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getRepresentantes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getRepresentantes", e);
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
        elementos = new CatalogoVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (CatalogoVO) array.get(i);
        }
        return elementos;

    }

    public CodigoPostalVO[] getColonias(String cp) throws ClientesException {

        String query = "SELECT CO_NUMCOLONIA, ES_NOMBRE, MU_NOMBRE, CO_NOMBRE_COLONIA, CO_CP, CO_ASENTAMIENTO_CP FROM"
                + " C_ESTADOS, C_MUNICIPIOS, C_COLONIAS WHERE CO_CP = ? AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO"
                + " AND CO_NUMESTADO = MU_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO";
        ArrayList<CodigoPostalVO> array = new ArrayList<CodigoPostalVO>();
        CodigoPostalVO temporal = null;
        CodigoPostalVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cp);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + cp + "]");
            while (rs.next()) {
                temporal = new CodigoPostalVO();
                temporal.cp = cp;
                temporal.idColonia = rs.getInt("CO_NUMCOLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.asentamiento_cp = rs.getString("CO_ASENTAMIENTO_CP");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getColonias", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getColonias", e);
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
        elementos = new CodigoPostalVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (CodigoPostalVO) array.get(i);
        }
        return elementos;

    }

    public ColoniaVO getDetalleColonia(int idColonia) throws ClientesException {

        String query = "SELECT CO_NUMESTADO, CO_NUMMUNICIPIO, CO_CP, CO_NOMBRE_COLONIA, CO_ASENTAMIENTO_CP FROM "
                + "C_COLONIAS WHERE CO_NUMCOLONIA = ?";

        ColoniaVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando query: " + query);
            myLogger.debug("Parametros [" + idColonia + "]");
            ps.setInt(1, idColonia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ColoniaVO();
                temporal.idColonia = idColonia;
                temporal.idEstado = rs.getInt("CO_NUMESTADO");
                temporal.idMunicipio = rs.getInt("CO_NUMMUNICIPIO");
                temporal.cp = rs.getInt("CO_CP");
                temporal.nombreColonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.asentamiento = rs.getString("CO_ASENTAMIENTO_CP");
            }
        } catch (SQLException sqle) {
            myLogger.error("getDetalleColonia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDetalleColonia", e);
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

        return temporal;

    }

    public ParametroScoringVO[] getCatalogoParametrosScoring(int catalogo) throws ClientesException {

        String query = "SELECT * FROM C_PARAMETROS_SCORING WHERE SC_CVE_CATALOGO = ?";
        ArrayList<ParametroScoringVO> array = new ArrayList<ParametroScoringVO>();
        ParametroScoringVO temporal = null;
        ParametroScoringVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, catalogo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ParametroScoringVO();
                temporal.codigo = rs.getInt("SC_CODIGO");
                temporal.valor = rs.getDouble("SC_VALOR");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoParametrosScoring", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoParametrosScoring", e);
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
        elementos = new ParametroScoringVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (ParametroScoringVO) array.get(i);
        }
        return elementos;

    }

    public ParametroVO getParametro(String cveParametro) throws ClientesException {

        String query = "SELECT PA_CVE_PARAM, PA_VALOR FROM C_PARAMETROS WHERE PA_CVE_PARAM = ?";
        ParametroVO param = null;

        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cveParametro);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                param = new ParametroVO();
                param.codigo = rs.getString("PA_CVE_PARAM");
                param.valor = rs.getString("PA_VALOR");
            }
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
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

        return param;

    }
    
        public ParametroVO getParametro(String cveParametro,Connection cn) throws ClientesException {

        String query = "SELECT PA_CVE_PARAM, PA_VALOR FROM C_PARAMETROS WHERE PA_CVE_PARAM = ?";
        ParametroVO param = null;
        try {
            //cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cveParametro);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                param = new ParametroVO();
                param.codigo = rs.getString("PA_CVE_PARAM");
                param.valor = rs.getString("PA_VALOR");
            }
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
            throw new ClientesException(e.getMessage());
        }
        return param;

    }

    public ParametroVO updateParametro(String cveParametro, String valor) throws ClientesException {

        String query = "UPDATE C_PARAMETROS SET PA_VALOR = ? WHERE PA_CVE_PARAM = ?";
        ParametroVO param = null;

        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, valor);
            ps.setString(2, cveParametro);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
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
        return param;
    }
    
        public ParametroVO updateParametro(String cveParametro, String valor, Connection cn) throws ClientesException {

        String query = "UPDATE C_PARAMETROS SET PA_VALOR = ? WHERE PA_CVE_PARAM = ?";
        ParametroVO param = null;
        try {            
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, valor);
            ps.setString(2, cveParametro);
            myLogger.debug("Executando: "+ ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
            throw new ClientesException(e.getMessage());
        } 
        return param;
    }

    public CatalogoVO[] getLocalidades(int idEstado, int idMunicipio, String localidad, int numero) throws ClientesException {

        String tipoConsulta = "";
        if (numero != 0) {
            tipoConsulta = "AND LO_NUMLOCALIDAD = ?";
        } else if (localidad != null) {
            tipoConsulta = "AND LO_DESCRIPCION LIKE ?";
        }

        String query = "SELECT LO_NUMLOCALIDAD, LO_DESCRIPCION FROM C_LOCALIDADES WHERE LO_NUMESTADO = ? "
                + "AND LO_NUMMUNICIPIO = ? " + tipoConsulta;

        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        CatalogoVO temporal = null;
        CatalogoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idEstado);
            ps.setInt(2, idMunicipio);

            if (numero != 0) {
                ps.setInt(3, numero);
            } else if (localidad != null) {
                ps.setString(3, "%" + localidad + "%");
            }
            myLogger.debug("Ejecutando query: " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = rs.getInt("LO_NUMLOCALIDAD");
                temporal.descripcion = rs.getString("LO_DESCRIPCION");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidades", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidades", e);
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
        elementos = new CatalogoVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (CatalogoVO) array.get(i);
        }
        return elementos;

    }

    public CatalogoVO getLocalidad(int idColonia, int idLocalidad) throws ClientesException {

        String query = "SELECT LO.LO_DESCRIPCION FROM C_LOCALIDADES LO, C_COLONIAS CO WHERE LO.LO_NUMESTADO = CO.CO_NUMESTADO "
                + "AND LO.LO_NUMMUNICIPIO=CO.CO_NUMMUNICIPIO AND LO.LO_NUMLOCALIDAD=? AND CO.CO_NUMCOLONIA=?";

        CatalogoVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idLocalidad);
            ps.setInt(2, idColonia);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CatalogoVO();
                temporal.id = idLocalidad;
                temporal.descripcion = rs.getString("LO_DESCRIPCION");
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidad", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidad", e);
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

        return temporal;

    }

    public MontoMaximoVO[] getCatalogoMontoMaximo(int catalogo) throws ClientesException {

        String query = "SELECT * FROM C_MONTO_MAXIMO WHERE MG_TPOCREDITO = ?";
        ArrayList<MontoMaximoVO> array = new ArrayList<MontoMaximoVO>();
        MontoMaximoVO temporal = null;
        MontoMaximoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, catalogo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new MontoMaximoVO();
                temporal.codigo = rs.getInt("MG_NUMSOLICITUD");
                temporal.valor = rs.getInt("MG_MONTO");
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoMontoMaximo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoMontoMaximo", e);
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
        elementos = new MontoMaximoVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (MontoMaximoVO) array.get(i);
        }
        return elementos;

    }

    public int asignaTasaGrupal(String esNuevo, int tipoSucursal, String calificacionGrupo) throws ClientesException {

        String query = "SELECT MG_TASA FROM C_MAPEO_TASAS_COMISIONES_GRUPAL WHERE MG_CLIENTE_NUEVO = ? AND MG_TIPO_SUCURSAL = ? AND MG_CALIF_GRUPO = ? ";

        int temporal = 0;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, esNuevo);
            ps.setInt(2, tipoSucursal);
            ps.setString(3, calificacionGrupo);

            ResultSet rs = ps.executeQuery();
            myLogger.debug("query"+ps);
            while (rs.next()) {
                temporal = rs.getInt("MG_TASA");
            }
        } catch (SQLException sqle) {
            myLogger.error("asignaTasaGrupal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("asignaTasaGrupal", e);
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
        return temporal;

    }

    public int asignaComisionGrupal(String esNuevo, int calificacionCirculo, int tipoSucursal, String calificacionGrupo) throws ClientesException {

        String query = "SELECT MG_COMISION FROM C_MAPEO_TASAS_COMISIONES_GRUPAL WHERE MG_CLIENTE_NUEVO = ? AND MG_CALIF_CIRCULO = ? "
                + "AND MG_TIPO_SUCURSAL = ? AND MG_CALIF_GRUPO = ? ";

        int temporal = 0;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, esNuevo);
            ps.setInt(2, calificacionCirculo);
            ps.setInt(3, tipoSucursal);
            ps.setString(4, calificacionGrupo);
            myLogger.debug("Ejecutando = "+query);
            myLogger.debug("Parametros["+esNuevo+", "+calificacionCirculo+", "+tipoSucursal+", "+calificacionGrupo+"]");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = rs.getInt("MG_COMISION");
            }
        } catch (SQLException sqle) {
            myLogger.error("asignaComisionGrupal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("asignaComisionGrupal", e);
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
        return temporal;

    }

    public ArrayList<SumaAseguradaVO> getCatalogoSumaAsegurada(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<SumaAseguradaVO> array = new ArrayList<SumaAseguradaVO>();
        FormatUtil formato = null;
        array.add(new SumaAseguradaVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {

                array.add(new SumaAseguradaVO(rs.getInt(1), formato.formatDobleMiles(rs.getDouble(2))));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoSumaAsegurada", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoSumaAsegurada", e);
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
        return array;

    }

    public ArrayList<TipoSeguroVO> getCatalogoTipoSeguro(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<TipoSeguroVO> array = new ArrayList<TipoSeguroVO>();
        FormatUtil formato = null;
        array.add(new TipoSeguroVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {

                array.add(new TipoSeguroVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoTipoSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoTipoSeguro", e);
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
        return array;

    }

    public ArrayList<CatRelacionVO> getCatalogoRelacion(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatRelacionVO> array = new ArrayList<CatRelacionVO>();
        FormatUtil formato = null;
        array.add(new CatRelacionVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatRelacionVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoRelacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoRelacion", e);
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
        return array;

    }

    public ArrayList<CatParentescoVO> getCatalogoParentesco(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatParentescoVO> array = new ArrayList<CatParentescoVO>();
        FormatUtil formato = null;
        array.add(new CatParentescoVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatParentescoVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoParentesco", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoParentesco", e);
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
        return array;

    }

    public ArrayList<DespachosVO> getCatalogoDespachos(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<DespachosVO> array = new ArrayList<DespachosVO>();
        FormatUtil formato = null;
        array.add(new DespachosVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new DespachosVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoDespachos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoDespachos", e);
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
        return array;

    }

    public ArrayList<CatBancosSeguroVO> getCatalogoBancoSeguros(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatBancosSeguroVO> array = new ArrayList<CatBancosSeguroVO>();
        FormatUtil formato = null;
        array.add(new CatBancosSeguroVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatBancosSeguroVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoBancoSeguros", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoBancoSeguros", e);
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
        return array;

    }

    public ArrayList<CatCuentasSegurosVO> getCatalogoCuentasSeguros(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatCuentasSegurosVO> array = new ArrayList<CatCuentasSegurosVO>();
        FormatUtil formato = null;
        array.add(new CatCuentasSegurosVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatCuentasSegurosVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoCuentasSeguros", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoCuentasSeguros", e);
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
        return array;

    }

    public ArrayList<SaldoIBSVO> getCatalogoEstatusCredito(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<SaldoIBSVO> array = new ArrayList<SaldoIBSVO>();
        FormatUtil formato = null;
        array.add(new SaldoIBSVO(0, "Seleccionar..."));
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new SaldoIBSVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoEstatusCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoEstatusCredito", e);
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
        return array;

    }

    public ArrayList<CatalogoVO> getCatalogoGeneral(String catalogo) throws ClientesException {

        String query = "SELECT * FROM " + catalogo;
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        FormatUtil formato = null;
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatalogoVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoGeneral", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoGeneral", e);
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
        return array;

    }
    
    public ArrayList<CatalogoVO> getCatalogoProcentaje(int semAdicional) throws ClientesException {

        String query = "SELECT pa_ID, pa_nombre FROM c_porcentajeAdicional where pa_prioridad >= ?";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        FormatUtil formato = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, semAdicional);
            myLogger.debug("Ejecutando : " + ps);
            ResultSet rs = ps.executeQuery();            
            while (rs.next()) {
                array.add(new CatalogoVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoProcentaje", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoProcentaje", e);
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
        return array;
    }
    
    public double getValorProcentajeAdicional(int idPorce) throws ClientesException {

        String query = "SELECT pa_valor FROM c_porcentajeAdicional WHERE pa_id = ?";
        double valor = 0.0;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idPorce);
            myLogger.debug("Ejecutando : " + ps);
            ResultSet rs = ps.executeQuery();            
            while (rs.next()) {
                valor = rs.getDouble(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoProcentaje", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoProcentaje", e);
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
        return valor;
    }
    
    public ArrayList<CatalogoVO> getCatalogoGeneralEstatus(String catalogo, String nomCampo, int estatus) throws ClientesException {

        String query = "SELECT * FROM " + catalogo +" WHERE " + nomCampo + " = "+estatus;
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        FormatUtil formato = null;
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (rs.next()) {
                array.add(new CatalogoVO(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoGeneral", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoGeneral", e);
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
        return array;

    }
    
    public boolean ejecutandoCierre() throws ClientesException, SQLException {
        String query="SELECT pa_valor FROM c_parametros WHERE pa_cve_param='EJECUTANDO_CIERRE_DIA'";
        boolean ejecutando =false;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int bandera=0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                bandera=Integer.parseInt(rs.getString("pa_valor"));
                if(bandera==1){
                    ejecutando=true;
                }
            }
        } catch(Exception e){
            myLogger.error("Exception", e);
            e.printStackTrace();
        } finally {
            if(cn!=null){
                cn.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return ejecutando;
    }
    
    public boolean ejecutandoCierreFondeadores() throws ClientesException, SQLException {
        String query="SELECT pa_valor FROM c_parametros WHERE pa_cve_param='EJECUTANDO_CIERRE_FONDEADORES'";
        boolean ejecutando =false;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int bandera=0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                bandera=Integer.parseInt(rs.getString("pa_valor"));
                if(bandera==1){
                    ejecutando=true;
                }
            }
        } catch(Exception e){
            myLogger.error("Exception", e);
            e.printStackTrace();
        } finally {
            if(cn!=null){
                cn.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return ejecutando;
    }
    
    public void banderaCierreDia(int bandera) throws ClientesException, SQLException {
        String query="UPDATE c_parametros SET pa_valor=? WHERE pa_cve_param='EJECUTANDO_CIERRE_DIA'";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(1, Integer.toString(bandera));
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeUpdate();            
            myLogger.debug("Registros actualizados: "+res);
        } catch(Exception e){
            myLogger.error("Exception", e);
        } finally {
            if(cn!=null){
                cn.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }        
    }
    
    public void updateBanderaCierreFondeadores(int bandera) throws ClientesException, SQLException {
        String query="UPDATE c_parametros SET pa_valor=? WHERE pa_cve_param='EJECUTANDO_CIERRE_FONDEADORES'";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(1, Integer.toString(bandera));
            myLogger.debug("Ejecutando: "+ps.toString());
            res = ps.executeUpdate();            
            myLogger.debug("Registros actualizados: "+res);
        } catch(Exception e){
            myLogger.error("Exception", e);
        } finally {
            if(cn!=null){
                cn.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }        
    }
    
     public Hashtable<String, String> getAnalistas() throws ClientesException {

        String query = "SELECT user_name, nombre_completo FROM users LEFT JOIN c_puesto_area ON(puesto=pa_numpuesto) WHERE pa_numpuesto IN(10,11,12)";
        Hashtable<String, String> analistas = new Hashtable<String, String>();
        Connection cn = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            rs = ps.executeQuery();
            myLogger.debug("Ejecutando: "+ ps.toString());
            analistas.put("", "Seleccione...");
            while (rs.next()) {
                analistas.put(rs.getString("user_name"), rs.getString("nombre_completo"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getAnalistas", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getAnalistas", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return analistas;

    }
    public String getGarantia(int idGarantia) throws ClientesException, SQLException {
        
        String query="SELECT gg_valor FROM c_garantia_grupal WHERE gg_idgarantia=?;";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valor = "";
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGarantia);
            myLogger.debug("Ejecutando: "+ps.toString());
            rs = ps.executeQuery();
            if(rs.next())
                valor = rs.getString("gg_valor");
        } catch(Exception e){
            myLogger.error("Exception", e);
        } finally {
            if(cn!=null){
                cn.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return valor;
    }
    
    public ArrayList<CatalogoVO> getCatalogoGeneral(int idCatalogo) throws ClientesException {

        String query = "SELECT cc_valor,cc_descripcion FROM c_catalogos WHERE cc_idcatalogo=?;";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idCatalogo);
            res = pstm.executeQuery();
            myLogger.debug("pstm "+pstm);
            while (res.next()) {
                array.add(new CatalogoVO(res.getInt("cc_valor"), res.getString("cc_descripcion")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoGeneral", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoGeneral", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;
    }
    
    public ArrayList<CatalogoVO> getAuditorActivo() throws ClientesException {

        String query = "SELECT au_numauditor,CONCAT(au_nombre,' ',au_apaterno,' ',au_amaterno) AS auditor FROM c_auditores WHERE au_estatus=1;";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            res = pstm.executeQuery();
            myLogger.debug("pstm "+pstm);
            while (res.next()) {
                array.add(new CatalogoVO(res.getInt("au_numauditor"), res.getString("auditor")));
            }
        } catch (SQLException sqle) {
            myLogger.error("geAuditorActivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("geAuditorActivo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;
    }
    
    public ArrayList<SucursalVO> getMapaSucursales() throws ClientesException {

        String query = "SELECT sd_numsubdir,sd_subdireccion,re_numregion,re_region,su_numsucursal,su_nombre "+
                "FROM c_subdireccion INNER JOIN c_regiones ON (sd_numsubdir=re_subdireccion) INNER JOIN c_sucursales ON (re_numregion=su_numregion) "+
                "ORDER BY sd_numsubdir,re_numregion,su_nombre;";
        ArrayList<SucursalVO> array = new ArrayList<SucursalVO>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            res = pstm.executeQuery();
            myLogger.debug("pstm "+pstm);
            while (res.next()) {
                array.add(new SucursalVO(res.getInt("su_numsucursal"), res.getString("su_nombre"), res.getInt("re_numregion"), res.getString("re_region"), res.getInt("sd_numsubdir"), res.getString("sd_subdireccion")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getMapaSucursales", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMapaSucursales", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;
    }
    
    public final ArrayList<CatalogoVO> getCatalogoFondeadores() throws ClientesException {

        String query = "SELECT * FROM c_fondeadores WHERE fo_status=1;";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (res.next()) {
                array.add(new CatalogoVO(res.getInt(1), res.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoFondeadores", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoFondeadores", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;

    }
    
    
    /**
     * Id en IN clause= 9,12,13,14,15 se agrega Credito Real id=1
     * Se tendran que agregar masfondeadores conforme se den de alta y apliquen para asignacion de cartera
     * @return
     * @throws ClientesException 
     */
    public final ArrayList<CatalogoVO> getCatalogoFondeadoresAsignacionCarteraGarantia() throws ClientesException {

        String query = "SELECT * FROM c_fondeadores WHERE fo_numfondeador in (1,9,12,13,14,15);";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (res.next()) {
                array.add(new CatalogoVO(res.getInt(1), res.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoFondeadoresAsignacionCarteraGarantia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoFondeadoresAsignacionCarteraGarantia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;

    }
    
    public final ArrayList<CatalogoVO> getCatalogoFondeadoresPreseleccion() throws ClientesException {

        String query = "SELECT * FROM c_fondeadores WHERE fo_conPreseleccion = 1;";
        ArrayList<CatalogoVO> array = new ArrayList<CatalogoVO>();
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            myLogger.debug("Ejecutando : " + query);
            while (res.next()) {
                array.add(new CatalogoVO(res.getInt(1), res.getString(2)));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoFondeadoresPreseleccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoFondeadoresPreseleccion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;

    }
    
    public final ArrayList<CatalogoVO> getCatalogoFondeadoresByPrioridadGreaterThan(int iPrioridad, boolean preseleccion, Connection con) throws ClientesException {

        String query = "SELECT * FROM c_fondeadores WHERE fo_prioridad > ? and fo_conPreseleccion = ? ORDER BY fo_prioridad ;";
        ArrayList<CatalogoVO> lista = new ArrayList<CatalogoVO>();
        
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            if(con==null){
                con = getConnection();
            }
            //con = getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, iPrioridad);
            pstmt.setInt(2, preseleccion?1:0);
            myLogger.debug("Ejecutando : " + pstmt);
            res = pstmt.executeQuery();
            while (res.next()) {
                lista.add(new CatalogoVO(res.getInt(1), res.getString(2)));
            }
            
        } catch (SQLException sqle) {
            myLogger.error("getCatalogoFondeadoresByPrioridadGreaterThan", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoFondeadoresByPrioridadGreaterThan", e);
            throw new ClientesException(e.getMessage());
        }
        return lista;

    }
    
    public  ArrayList<CatalogoVO> getCatParamFondeador(String cveParametro) throws ClientesDBException, ClientesException{
        
        ArrayList<CatalogoVO> lstParametros=null;
        Connection con;
        try {
            con = getConnection();
            lstParametros = getCatParamFondeador(cveParametro, con);
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(CatalogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CatalogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return lstParametros;
    }
    
    public  ArrayList<CatalogoVO> getCatParamFondeador(String cveParametro, Connection con) throws ClientesDBException, ClientesException{
        
        String query="SELECT * FROM c_parametros_fondeadores where pf_CVE = ? ";
        ArrayList<CatalogoVO> lstParametros= new ArrayList<CatalogoVO>();
        
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            if(con==null){
                con = getConnection();
            }
            //con = getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, cveParametro);
            myLogger.debug("Ejecutando : " + query);
            res = pstmt.executeQuery();
            while (res.next()) {
                        lstParametros.add( new CatalogoVO(res.getInt("PF_FONDEADOR"), res.getString("PF_VALOR")));
            }
            
        } catch (SQLException sqle) {
            myLogger.error("getCatParamFondeador", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCatalogoFondeadoresByPrioridadGreaterThan", e);
            throw new ClientesException(e.getMessage());
        }
        return lstParametros;
    }
    
    public ParametroVO getParametroFondeador(String cveParametro,int idFondeador) throws ClientesException {

        String query ="SELECT concat(PF_CVE,'_',pf_fondeador) AS PF_CVE_PARAM , PF_VALOR "
                + " FROM C_PARAMETROS_FONDEADORES "
                + " WHERE PF_CVE = ? "
                + " AND PF_FONDEADOR= ? ;";
        
        ParametroVO param = null;

        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cveParametro);
            ps.setInt(2, idFondeador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                param = new ParametroVO();
                param.codigo = rs.getString("PF_CVE_PARAM");
                param.valor = rs.getString("PF_VALOR");
            }
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
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

        return param;

    }
    
    public ParametroVO getParametroFondeador(String cveParametro,int idFondeador,Connection cn) throws ClientesException {
            
        String query ="SELECT concat(PF_CVE,'_',pf_fondeador) AS PF_CVE_PARAM , PF_VALOR "
                + " FROM C_PARAMETROS_FONDEADORES "
                + " WHERE PF_CVE = ? "
                + " AND PF_FONDEADOR= ? ;";
        
        ParametroVO param = null;
        try {
            //cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cveParametro);
            ps.setInt(2, idFondeador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                param = new ParametroVO();
                 param.codigo = rs.getString("PF_CVE_PARAM");
                param.valor = rs.getString("PF_VALOR");
            }
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
            throw new ClientesException(e.getMessage());
        }
        return param;

    }
    
    
     
    
    public ParametroVO updateParametroFondeador(String cveParametro, int idFondeador, String valor){
        ParametroVO param = null;
        
        Connection cn = null;
        
        try {
             cn = getConnection();
            param= updateParametroFondeador(cveParametro, idFondeador, valor, cn);
        } catch (ClientesException ex) {
            java.util.logging.Logger.getLogger(CatalogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(CatalogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CatalogoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return param;
    }
        
        
    public ParametroVO updateParametroFondeador(String cveParametro, int idFondeador, String valor, Connection cn) throws ClientesException {

        String query = "UPDATE C_PARAMETROS_FONDEADORES "
                + " SET PF_VALOR = ? "
                + " WHERE PF_CVE = ? "
                + " AND PF_FONDEADOR = ?; ";
        
        ParametroVO param = null;
        try {            
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, valor);
            ps.setString(2, cveParametro);
            ps.setInt(3, idFondeador);
            myLogger.debug("Executando: "+ ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("getParametro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getParametro", e);
            throw new ClientesException(e.getMessage());
        } 
        return param;
    }    
    
}
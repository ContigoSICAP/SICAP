package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.IncidenciaPagoGrupalVO;
import org.apache.log4j.Logger;

/**
 * Maneja persistencia de tabla d_incidencias
 *
 * @author JahTechnologies
 *
 */
public class IncidenciaPagoGrupalDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(IncidenciaPagoGrupalDAO.class);

    /**
     * Consulta de todas las incidencias
     *
     * @return Arreglo de clases de entidad de incidencias
     * @throws ClientesException
     */
    public IncidenciaPagoGrupalVO[] getIncidencias() throws ClientesException {
        IncidenciaPagoGrupalVO incidencia = null;
        Connection cn = null;
        ArrayList<IncidenciaPagoGrupalVO> array = new ArrayList<IncidenciaPagoGrupalVO>();
        IncidenciaPagoGrupalVO elementos[] = null;

        String query = "SELECT *"
                + " FROM d_incidencias_pagos_grupales";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                incidencia = new IncidenciaPagoGrupalVO();
                incidencia.fecha = rs.getDate("ig_fecha");
                incidencia.diferencia = rs.getDouble("ig_diferencia");;
                incidencia.montoDepositado = rs.getDouble("ig_montodepositado");
                incidencia.montoEsperado = rs.getDouble("ig_montoesperado");
                incidencia.nombreGrupo = rs.getString("ig_nombregrupo");
                incidencia.numSucursal = rs.getInt("ig_numsucursal");
                array.add(incidencia);
                myLogger.debug("Incidencia encontrada : " + incidencia.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getIncidencias", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIncidencias", e);
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
        elementos = new IncidenciaPagoGrupalVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (IncidenciaPagoGrupalVO) array.get(i);
        }
        return elementos;
    }

    /**
     * Inserta una nueva incidencia
     *
     * @param incidencia Clase entidad de incidencias
     * @return Numero 1 si la insercion fue correcta
     * @throws ClientesException
     */
    public int addIncidencia(IncidenciaPagoGrupalVO incidencia) throws ClientesException {

        String query = "INSERT INTO d_incidencias_pagos_grupales (ig_fecha, ig_numsucursal, ig_nombregrupo, ig_montoesperado, ig_montodepositado, ig_diferencia )"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(param++, incidencia.fecha);
            ps.setInt(param++, incidencia.numSucursal);
            ps.setString(param++, incidencia.nombreGrupo);
            ps.setDouble(param++, incidencia.montoEsperado);
            ps.setDouble(param++, incidencia.montoDepositado);
            ps.setDouble(param++, incidencia.diferencia);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Incidencia= " + incidencia.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIncidencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIncidencia", e);
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
    
    public boolean addIncidencia(IncidenciaPagoGrupalVO incidencia, Connection con) throws ClientesException {

        String query = "INSERT INTO d_incidencias_pagos_grupales (ig_fecha, ig_numsucursal, ig_nombregrupo, ig_montoesperado, ig_montodepositado, ig_diferencia )"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        int param = 1;
        boolean listo = true;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, incidencia.fecha);
            ps.setInt(param++, incidencia.numSucursal);
            ps.setString(param++, incidencia.nombreGrupo);
            ps.setDouble(param++, incidencia.montoEsperado);
            ps.setDouble(param++, incidencia.montoDepositado);
            ps.setDouble(param++, incidencia.diferencia);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addIncidencia", sqle);
            return listo = false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return listo;
    }
}

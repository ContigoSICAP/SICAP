package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.ObligadoSolidarioVO;
import org.apache.log4j.Logger;

public class ObligadoSolidarioDAO extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(ObligadoSolidarioDAO.class);

    public ObligadoSolidarioVO[] getObligadosSolidarios(int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT OB_NUMCLIENTE, OB_NUMSOLICITUD, OB_NUMOBLIGADO, OB_ESTATUS, OB_RFC, OB_NOMBRE, "
                + "OB_APATERNO, OB_AMATERNO, OB_FECHA_NACIMIENTO, OB_SEXO, OB_NACIONALIDAD, "
                + "OB_TIPO_IDENTIFICACION, OB_NUMERO_IDENTIFICACION, OB_EDO_CIVIL, OB_FECHA_CAPTURA, "
                + "OB_TELEFONO, OB_FECHA_FIRMA, OB_EMPRESA, OB_PUESTO, OB_TELEFONO_EMPLEO, OB_SUELDO_MENSUAL, OB_DIRECCION_EMPLEO "
                + "FROM D_OBLIGADOS_SOLIDARIOS WHERE OB_NUMCLIENTE = ? AND OB_NUMSOLICITUD = ?";
        ArrayList<ObligadoSolidarioVO> array = new ArrayList<ObligadoSolidarioVO>();
        ObligadoSolidarioVO temporal = null;
        ObligadoSolidarioVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ObligadoSolidarioVO();
                temporal.idCliente = idCliente;
                temporal.idSolicitud = idSolicitud;
                temporal.idObligado = rs.getInt("OB_NUMOBLIGADO");
                temporal.estatus = rs.getInt("OB_ESTATUS");
                temporal.rfc = rs.getString("OB_RFC");
                temporal.nombre = rs.getString("OB_NOMBRE");
                temporal.aPaterno = rs.getString("OB_APATERNO");
                temporal.aMaterno = rs.getString("OB_AMATERNO");
                temporal.fechaNacimiento = rs.getDate("OB_FECHA_NACIMIENTO");
                temporal.sexo = rs.getInt("OB_SEXO");
                temporal.nacionalidad = rs.getInt("OB_NACIONALIDAD");
                temporal.tipoIdentificacion = rs.getInt("OB_TIPO_IDENTIFICACION");
                temporal.numeroIdentificacion = rs.getString("OB_NUMERO_IDENTIFICACION");
                temporal.estadoCivil = rs.getInt("OB_EDO_CIVIL");
                temporal.fechaCaptura = rs.getTimestamp("OB_FECHA_CAPTURA");
                temporal.telefono = rs.getString("OB_TELEFONO");
                temporal.fechaFirmaSolicitud = rs.getDate("OB_FECHA_FIRMA");

                temporal.empresa = rs.getString("OB_EMPRESA");
                temporal.puesto = rs.getString("OB_PUESTO");
                temporal.telefonoExt = rs.getString("OB_TELEFONO_EMPLEO");
                temporal.sueldoMensual = rs.getInt("OB_SUELDO_MENSUAL");
                temporal.direccionTrabajo = rs.getString("OB_DIRECCION_EMPLEO");

                temporal.direccion = new DireccionVO();
                myLogger.debug("Obligado encontrado : " + temporal.toString());
                array.add(temporal);
            }
        } catch (SQLException sqle) {
            myLogger.error("getObligadosSolidarios", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getObligadosSolidarios", e);
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
        elementos = new ObligadoSolidarioVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (ObligadoSolidarioVO) array.get(i);
        }
        return elementos;

    }

    public ObligadoSolidarioVO getObligadoSolidario(int idCliente, int idSolicitud, int idObligado) throws ClientesException {

        String query = "SELECT * FROM D_CLIENTES WHERE EN_NUMCLIENTE = ? ";
        Connection cn = null;
        ObligadoSolidarioVO cliente = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cliente = new ObligadoSolidarioVO();
                cliente.idCliente = idCliente;
                cliente.nombre = rs.getString("EN_NOMBRE");
                cliente.aPaterno = rs.getString("EN_PRIMER_APELLIDO");
                cliente.aMaterno = rs.getString("EN_SEGUNDO_APELLIDO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getObligadoSolidario", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getObligadoSolidario", e);
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
        return cliente;

    }

    public boolean obligadoDisponible(int idCliente, int idSolicitud, int idObligado, String rfc) throws ClientesException {

        String query = "SELECT OB_NUMCLIENTE, OB_NUMSOLICITUD, OB_NUMOBLIGADO, OB_RFC, OB_NOMBRE, OB_APATERNO, OB_AMATERNO "
                + "FROM D_OBLIGADOS_SOLIDARIOS, D_SALDOS_T24 WHERE OB_RFC = ? AND OB_NUMCLIENTE != ? AND "
                + "OB_NUMCLIENTE = ST_NUMCLIENTE AND OB_NUMSOLICITUD = ST_CICLO AND ST_NUMOPERACION NOT IN (3,5) "
                + "AND ST_SITUACION_ACTUAL_CREDITO IN (1,2) "
                + "UNION SELECT OB_NUMCLIENTE, OB_NUMSOLICITUD, OB_NUMOBLIGADO, OB_RFC, OB_NOMBRE, "
                + "OB_APATERNO, OB_AMATERNO FROM D_OBLIGADOS_SOLIDARIOS WHERE OB_RFC = ? AND OB_NUMCLIENTE = ? AND OB_NUMSOLICITUD = ? AND OB_NUMOBLIGADO != ?";
        Connection cn = null;
        boolean res = true;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, rfc.toUpperCase());
            ps.setInt(2, idCliente);
            ps.setString(3, rfc.toUpperCase());
            ps.setInt(4, idCliente);
            ps.setInt(5, idSolicitud);
            ps.setInt(6, idObligado);
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("rfc : " + rfc);
            myLogger.debug("idCliente : " + idCliente);
            myLogger.debug("idSolicitud : " + idSolicitud);
            myLogger.debug("idObligado : " + idObligado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                myLogger.debug("Obligados encontrados");
                res = false;
            }
        } catch (SQLException sqle) {
            myLogger.error("obligadoDisponible", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("obligadoDisponible", e);
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
        return res;

    }

    public int addObligadoSolidario(Connection conn, int idCliente, int idSolicitud, ObligadoSolidarioVO obligado) throws ClientesException {

        String query = "INSERT INTO D_OBLIGADOS_SOLIDARIOS (OB_NUMCLIENTE, OB_NUMSOLICITUD, OB_NUMOBLIGADO, "
                + "OB_ESTATUS, OB_RFC, OB_NOMBRE, OB_APATERNO, OB_AMATERNO, OB_FECHA_NACIMIENTO, OB_SEXO,"
                + " OB_NACIONALIDAD, OB_TIPO_IDENTIFICACION, OB_NUMERO_IDENTIFICACION, OB_EDO_CIVIL, "
                + "OB_FECHA_CAPTURA, OB_TELEFONO, OB_FECHA_FIRMA, OB_EMPRESA, OB_PUESTO, OB_TELEFONO_EMPLEO, "
                + "OB_SUELDO_MENSUAL, OB_DIRECCION_EMPLEO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int next = 0;
        try {
            if (conn == null) {
                next = getNext(idCliente, idSolicitud);
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                next = getNext(conn, idCliente, idSolicitud);
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, next);
            ps.setInt(param++, obligado.estatus);
            ps.setString(param++, obligado.rfc);
            ps.setString(param++, obligado.nombre);
            ps.setString(param++, obligado.aPaterno);
            ps.setString(param++, obligado.aMaterno);
            ps.setDate(param++, obligado.fechaNacimiento);
            ps.setInt(param++, obligado.sexo);
            ps.setInt(param++, obligado.nacionalidad);
            ps.setInt(param++, obligado.tipoIdentificacion);
            ps.setString(param++, obligado.numeroIdentificacion);
            ps.setInt(param++, obligado.estadoCivil);
            ps.setTimestamp(param++, obligado.fechaCaptura);
            ps.setString(param++, obligado.telefono);
            ps.setDate(param++, obligado.fechaFirmaSolicitud);

            ps.setString(param++, obligado.empresa);
            ps.setString(param++, obligado.puesto);
            ps.setString(param++, obligado.telefonoExt);
            ps.setDouble(param++, obligado.sueldoMensual);
            ps.setString(param++, obligado.direccionTrabajo);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Obligado = " + obligado);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addObligadoSolidario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addObligadoSolidario", e);
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
        return next;
    }

    public int updateObligadoSolidario(Connection conn, int idCliente, int idSolicitud, int idObligado, ObligadoSolidarioVO obligado) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_OBLIGADOS_SOLIDARIOS SET OB_ESTATUS = ?, OB_RFC = ?, OB_NOMBRE = ?, OB_APATERNO = ?, "
                + "OB_AMATERNO = ?, OB_FECHA_NACIMIENTO = ?, OB_SEXO = ?, OB_NACIONALIDAD = ?, OB_TIPO_IDENTIFICACION = ?, "
                + "OB_NUMERO_IDENTIFICACION = ?, OB_EDO_CIVIL = ?, OB_TELEFONO = ?, OB_FECHA_FIRMA = ?, OB_EMPRESA = ?, "
                + "OB_PUESTO = ?, OB_TELEFONO_EMPLEO = ?, OB_SUELDO_MENSUAL = ?, OB_DIRECCION_EMPLEO = ? WHERE "
                + "OB_NUMCLIENTE = ? AND OB_NUMSOLICITUD = ? AND OB_NUMOBLIGADO = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, obligado.estatus);
            ps.setString(param++, obligado.rfc);
            ps.setString(param++, obligado.nombre);
            ps.setString(param++, obligado.aPaterno);
            ps.setString(param++, obligado.aMaterno);
            ps.setDate(param++, obligado.fechaNacimiento);
            ps.setInt(param++, obligado.sexo);
            ps.setInt(param++, obligado.nacionalidad);
            ps.setInt(param++, obligado.tipoIdentificacion);
            ps.setString(param++, obligado.numeroIdentificacion);
            ps.setInt(param++, obligado.estadoCivil);
            ps.setString(param++, obligado.telefono);
            ps.setDate(param++, obligado.fechaFirmaSolicitud);

            ps.setString(param++, obligado.empresa);
            ps.setString(param++, obligado.puesto);
            ps.setString(param++, obligado.telefonoExt);
            ps.setDouble(param++, obligado.sueldoMensual);
            ps.setString(param++, obligado.direccionTrabajo);

            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setInt(param++, idObligado);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Oblogado solidario = " + obligado.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateObligadoSolidario", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateObligadoSolidario", e);
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

    public int getNext(int idCliente, int idSolicitud) throws ClientesException {
        return getNext(null, idCliente, idSolicitud);
    }

    public int getNext(Connection conn, int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT COALESCE(MAX(OB_NUMOBLIGADO),0)+1 AS NEXT FROM D_OBLIGADOS_SOLIDARIOS"
                + " WHERE OB_NUMCLIENTE = ? AND OB_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int next = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
        } catch (SQLException sqle) {
            myLogger.error("getNext", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNext", e);
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

}

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class IntegranteCicloDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(IntegranteCicloDAO.class);

    public IntegranteCicloVO[] getIntegrantes(int idGrupo, int idCiclo) throws ClientesException {
        return getIntegrantes(null, idGrupo, idCiclo);
    }

    public IntegranteCicloVO[] getIntegrantes(Connection conn, int idGrupo, int idCiclo) throws ClientesException {

        /*String query =	"SELECT A.*, B.EN_NOMBRE_COMPLETO, DE_MONTOAUTORIZADO FROM D_INTEGRANTES_CICLO A, "+
         "D_CLIENTES B, D_DECISION_COMITE C WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ? AND "+
         "A.IC_NUMCLIENTE = B.EN_NUMCLIENTE AND EN_NUMCLIENTE = DE_NUMCLIENTE ORDER BY A.IC_ROL DESC, IC_MONTO DESC";
         String query = 	"SELECT A.*, B.EN_NOMBRE_COMPLETO, S.DE_MONTOAUTORIZADO, S.DE_COMISION, IC_COMISION, IC_CALIFICACION, C.SO_NUMCHEQUE, C.SO_DESEMBOLSADO FROM " +
         "D_INTEGRANTES_CICLO A " +
         "LEFT JOIN D_DECISION_COMITE S ON A.IC_NUMCLIENTE = S.DE_NUMCLIENTE and A.IC_NUMSOLICITUD = S.DE_NUMSOLICITUD "+
         "LEFT JOIN D_SOLICITUDES C ON A.IC_NUMCLIENTE = C.SO_NUMCLIENTE AND " +
         "A.IC_NUMSOLICITUD = C.SO_NUMSOLICITUD, D_CLIENTES B WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ? " +
         "AND A.IC_NUMCLIENTE = B.EN_NUMCLIENTE ORDER BY A.IC_ROL DESC, IC_MONTO DESC";*/
        String query = "SELECT A.*, B.EN_NOMBRE_COMPLETO, S.DE_MONTOAUTORIZADO, S.DE_COMISION, IC_COMISION, IC_CALIFICACION, C.SO_NUMCHEQUE, C.SO_DESEMBOLSADO, SE.se_prima_total, en_mediocobro, C.so_subproducto, ic_porcentajeAdicional, "
                + "SE.SE_MODULOS, B.EN_NUMSUCURSAL, SE.SE_CONTRATACION "
                /**
                 * JECB 01/10/2017
                 * Se agrega los campos de "EN_SEXO" y "SE_CONTRATACION"
                 * a la consulta principal para conocer el sexo del cliente
                 * y si dicho cliente contrato seguro de vida
                 */
                + ", B.EN_SEXO, SE.SE_CONTRATACION "
                + "FROM D_INTEGRANTES_CICLO A "
                + "LEFT JOIN D_DECISION_COMITE S ON (A.IC_NUMCLIENTE = S.DE_NUMCLIENTE and A.IC_NUMSOLICITUD = S.DE_NUMSOLICITUD) "
                + "LEFT JOIN D_SOLICITUDES C ON (A.IC_NUMCLIENTE = C.SO_NUMCLIENTE AND A.IC_NUMSOLICITUD = C.SO_NUMSOLICITUD) "
                + "LEFT JOIN D_SEGUROS SE ON ( A.ic_numcliente=SE.se_numcliente AND A.ic_numsolicitud=SE.se_numsolicitud ), "
                + "D_CLIENTES B "
                + "WHERE IC_NUMGRUPO = ? "
                + "AND IC_NUMCICLO = ? "
                + "AND A.IC_NUMCLIENTE = B.EN_NUMCLIENTE ORDER BY A.IC_ROL DESC, IC_MONTO DESC";
        ArrayList<IntegranteCicloVO> array = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO temporal = null;
        IntegranteCicloVO elementos[] = null;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new IntegranteCicloVO();
                temporal.idGrupo = rs.getInt("IC_NUMGRUPO");
                temporal.idCiclo = rs.getInt("IC_NUMCICLO");
                temporal.idCliente = rs.getInt("IC_NUMCLIENTE");
                temporal.idSolicitud = rs.getInt("IC_NUMSOLICITUD");
                temporal.nombre = rs.getString("EN_NOMBRE_COMPLETO");
                temporal.monto = rs.getDouble("DE_MONTOAUTORIZADO");
                temporal.montoRefinanciado = rs.getDouble("IC_MONTO_REFINANCIAR");
                temporal.comision = rs.getInt("DE_COMISION");
                temporal.numCheque = rs.getString("SO_NUMCHEQUE");
                temporal.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                temporal.estatus = rs.getInt("IC_ESTATUS");
                temporal.calificacion = rs.getInt("IC_CALIFICACION");
                temporal.rol = rs.getInt("IC_ROL");
                temporal.fechaCaptura = rs.getTimestamp("IC_FECHA_CAPTURA");
                temporal.ordenPago = new OrdenDePagoDAO().getOrdenDePago(temporal.idCliente, temporal.idSolicitud);
                temporal.primaSeguro = rs.getDouble("se_prima_total");
                temporal.medioCobro = rs.getInt("en_mediocobro");
                temporal.medioDisp = rs.getInt("ic_mediodisp");
                temporal.tarjetaCobro = new TarjetaDAO().getTarjetaClinete(temporal.idCliente, temporal.idSolicitud);
                temporal.esInterciclo = rs.getInt("so_subproducto");
                temporal.tipo = rs.getInt("ic_tipo");
                temporal.idPorcentajeAdicional = rs.getInt("ic_porcentajeAdicional");
                temporal.tipo_adicional = rs.getInt("ic_tipo_adi");
                temporal.montoAdicional = rs.getDouble("ic_montoAdicional");
                temporal.montoConSeguro = rs.getDouble("ic_montoconseguro");
                temporal.montoConSeguroTemp = rs.getDouble("ic_montoconseguro"); 
                temporal.tipoSeguro = rs.getInt("se_modulos");
                temporal.idSucursal = rs.getInt("en_numsucursal");
                temporal.segContratado = rs.getInt("SE_CONTRATACION");
                temporal.sexoCliente = rs.getInt("EN_SEXO");
                temporal.contratacionSeguro = rs.getInt("SE_CONTRATACION");                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new IntegranteCicloVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (IntegranteCicloVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantes", e);
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
        return elementos;

    }
     public IntegranteCicloVO[] getIntegrantesPagos(Connection conn, int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT A.*, B.EN_NOMBRE_COMPLETO, S.DE_MONTOAUTORIZADO, S.DE_COMISION, IC_COMISION, IC_CALIFICACION, C.SO_NUMCHEQUE, C.SO_DESEMBOLSADO, SE.se_prima_total, en_mediocobro, C.so_subproducto "
                + "FROM D_INTEGRANTES_CICLO A "
                + "LEFT JOIN D_DECISION_COMITE S ON (A.IC_NUMCLIENTE = S.DE_NUMCLIENTE and A.IC_NUMSOLICITUD = S.DE_NUMSOLICITUD) "
                + "LEFT JOIN D_SOLICITUDES C ON (A.IC_NUMCLIENTE = C.SO_NUMCLIENTE AND A.IC_NUMSOLICITUD = C.SO_NUMSOLICITUD) "
                + "LEFT JOIN D_SEGUROS SE ON ( A.ic_numcliente=SE.se_numcliente AND A.ic_numsolicitud=SE.se_numsolicitud ), "
                + "D_CLIENTES B "
                + "WHERE IC_NUMGRUPO = ? "
                + "AND IC_NUMCICLO = ? "
                + "AND A.IC_NUMCLIENTE = B.EN_NUMCLIENTE ORDER BY A.IC_ROL DESC, IC_MONTO DESC";
        ArrayList<IntegranteCicloVO> array = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO temporal = null;
        IntegranteCicloVO elementos[] = null;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new IntegranteCicloVO();
                temporal.idGrupo = rs.getInt("IC_NUMGRUPO");
                temporal.idCiclo = rs.getInt("IC_NUMCICLO");
                temporal.idCliente = rs.getInt("IC_NUMCLIENTE");
                temporal.idSolicitud = rs.getInt("IC_NUMSOLICITUD");
                temporal.nombre = rs.getString("EN_NOMBRE_COMPLETO");
                temporal.monto = rs.getDouble("DE_MONTOAUTORIZADO");
                temporal.montoRefinanciado = rs.getDouble("IC_MONTO_REFINANCIAR");
                temporal.comision = rs.getInt("DE_COMISION");
                temporal.numCheque = rs.getString("SO_NUMCHEQUE");
                temporal.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                temporal.estatus = rs.getInt("IC_ESTATUS");
                temporal.calificacion = rs.getInt("IC_CALIFICACION");
                temporal.rol = rs.getInt("IC_ROL");
                temporal.fechaCaptura = rs.getTimestamp("IC_FECHA_CAPTURA");
                temporal.ordenPago = new OrdenDePagoDAO().getOrdenDePago(temporal.idCliente, temporal.idSolicitud);
                temporal.primaSeguro = rs.getDouble("se_prima_total");
                temporal.medioCobro = rs.getInt("en_mediocobro");
                temporal.medioDisp = rs.getInt("ic_mediodisp");
                temporal.tarjetaCobro = new TarjetaDAO().getTarjetaClinete(temporal.idCliente, temporal.idSolicitud);
                temporal.esInterciclo = rs.getInt("so_subproducto");
                temporal.tipo = rs.getInt("ic_tipo");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new IntegranteCicloVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (IntegranteCicloVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantes", e);
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
        return elementos;

    }


    public ArrayList<IntegranteCicloVO> getIntegrantesApertura(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT A.ic_numgrupo, A.ic_numciclo, A.ic_numcliente, A.ic_numsolicitud, A.ic_monto_refinanciar, A.ic_estatus, A.ic_calificacion, A.ic_rol, A.ic_fecha_captura, A.ic_esnuevo, "
                + "B.de_montoautorizado, B.de_comision, C.so_numcheque, C.so_desembolsado, C.so_consulcc, D.se_prima_total, E.en_nombre_completo, "
                + "A.IC_MONTOCONSEGURO, D.SE_MODULOS, E.EN_NUMSUCURSAL, D.SE_CONTRATACION "
                + "FROM d_integrantes_ciclo A "
                + "LEFT JOIN d_decision_comite B ON (A.ic_numcliente = B.de_numcliente AND A.ic_numsolicitud = B.de_numsolicitud) "
                + "LEFT JOIN d_solicitudes C ON (A.ic_numcliente = C.so_numcliente AND A.ic_numsolicitud = C.so_numsolicitud) "
                + "LEFT JOIN d_seguros D ON (A.ic_numcliente = D.se_numcliente AND A.ic_numsolicitud = D.se_numsolicitud) "
                + "LEFT JOIN d_clientes E ON (A.ic_numcliente = E.en_numcliente) "
                + "WHERE ic_numgrupo = ? AND ic_numciclo = ? ORDER BY A.ic_rol DESC, ic_monto DESC";

        ArrayList<IntegranteCicloVO> IntegrantesArray = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            //if (conn == null) {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*} else {
             ps = conn.prepareStatement(query);
             }*/
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                integrante.idGrupo = rs.getInt("IC_NUMGRUPO");
                integrante.idCiclo = rs.getInt("IC_NUMCICLO");
                integrante.idCliente = rs.getInt("IC_NUMCLIENTE");
                integrante.idSolicitud = rs.getInt("IC_NUMSOLICITUD");
                integrante.nombre = rs.getString("EN_NOMBRE_COMPLETO");
                integrante.monto = rs.getDouble("DE_MONTOAUTORIZADO");
                integrante.montoRefinanciado = rs.getDouble("IC_MONTO_REFINANCIAR");
                integrante.comision = rs.getInt("DE_COMISION");
                integrante.numCheque = rs.getString("SO_NUMCHEQUE");
                integrante.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                integrante.estatus = rs.getInt("IC_ESTATUS");
                integrante.calificacion = rs.getInt("IC_CALIFICACION");
                integrante.rol = rs.getInt("IC_ROL");
                integrante.fechaCaptura = rs.getTimestamp("IC_FECHA_CAPTURA");
                integrante.ordenPago = new OrdenDePagoDAO().getOrdenDePago(integrante.idCliente, integrante.idSolicitud);
                integrante.primaSeguro = rs.getDouble("se_prima_total");
                integrante.consultaCC = rs.getInt("so_consulcc");
                integrante.esNuevo = rs.getInt("ic_esnuevo");
                integrante.montoConSeguro = rs.getDouble("ic_montoconseguro");
                // necesaria para ciclo grupal estatus autorizar
                integrante.montoConSeguroTemp = rs.getDouble("ic_montoconseguro"); 
                integrante.tipoSeguro = rs.getInt("se_modulos");
                integrante.idSucursal = rs.getInt("en_numsucursal");
                integrante.segContratado = rs.getInt("SE_CONTRATACION");
                IntegrantesArray.add(integrante);
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesApertura", e);
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
        return IntegrantesArray;
    }

    public IntegranteCicloVO[] getIntegrantesNuevoCiclo(int idGrupo, int idOperacion) throws ClientesException {

        /*String query =	"SELECT * FROM D_CLIENTES, (SELECT SO_NUMCLIENTE, MAX(SO_NUMSOLICITUD) NUMSOL, SO_ESTATUS " +
         "FROM D_SOLICITUDES WHERE SO_ESTATUS = 2 AND SO_NUMOPERACION = ? AND SO_DESEMBOLSADO <> 2 " +
         "GROUP BY SO_NUMCLIENTE) S LEFT JOIN D_INTEGRANTES_CICLO ON (IC_NUMCLIENTE = SO_NUMCLIENTE " +
         "AND IC_NUMSOLICITUD = NUMSOL), D_DECISION_COMITE WHERE EN_NUMGRUPO = ? AND EN_NUMCLIENTE = " +
         "S.SO_NUMCLIENTE AND EN_NUMCLIENTE = DE_NUMCLIENTE AND S.NUMSOL = DE_NUMSOLICITUD AND IC_NUMGRUPO IS NULL";*/
        String query = "SELECT * "
                + "FROM D_CLIENTES, "
                + "(SELECT SO_NUMCLIENTE, MAX(SO_NUMSOLICITUD) NUMSOL, SO_ESTATUS "
                + "FROM D_SOLICITUDES "
                + "WHERE SO_ESTATUS = 2 "
                + "AND SO_NUMOPERACION = ? "
                + "AND SO_DESEMBOLSADO <> 2 "
                + "GROUP BY SO_NUMCLIENTE) S "
                + "LEFT JOIN D_INTEGRANTES_CICLO ON (IC_NUMCLIENTE = SO_NUMCLIENTE AND IC_NUMSOLICITUD = NUMSOL) "
                + "LEFT JOIN D_SEGUROS ON ( SO_NUMCLIENTE=se_numcliente AND NUMSOL=se_numsolicitud ), "
                + "D_DECISION_COMITE "
                + "WHERE EN_NUMGRUPO = ? "
                + "AND EN_NUMCLIENTE = S.SO_NUMCLIENTE "
                + "AND EN_NUMCLIENTE = DE_NUMCLIENTE "
                + "AND S.NUMSOL = DE_NUMSOLICITUD "
                + "AND IC_NUMGRUPO IS NULL";

        ArrayList<IntegranteCicloVO> array = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO temporal = null;
        IntegranteCicloVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idOperacion);
            ps.setInt(2, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idOperacion + "," + idGrupo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //if (getIntegranteActivo(rs.getInt("EN_NUMCLIENTE")) != 1) {
                if (getIntegranteActivo(rs.getInt("EN_NUMCLIENTE")).equals("")) {
                    temporal = new IntegranteCicloVO();
                    temporal.idGrupo = rs.getInt("EN_NUMGRUPO");
                    temporal.idCliente = rs.getInt("EN_NUMCLIENTE");
                    temporal.monto = rs.getDouble("DE_MONTOAUTORIZADO");
                    temporal.comision = rs.getInt("DE_COMISION");
                    if (idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                        temporal.idTasa = rs.getInt("DE_TASA");
                        temporal.plazo = rs.getInt("DE_PLAZOAUTORIZADO");
                    }
                    temporal.nombre = rs.getString("EN_NOMBRE_COMPLETO");
                    temporal.idSolicitud = rs.getInt("NUMSOL");
                    temporal.ordenPago = new OrdenDePagoDAO().getOrdenDePago(temporal.idCliente, temporal.idSolicitud);
                    temporal.primaSeguro = rs.getDouble("se_prima_total");
                    array.add(temporal);
                }

            }
            if (array.size() > 0) {
                elementos = new IntegranteCicloVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (IntegranteCicloVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesNuevoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesNuevoCiclo", e);
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
        return elementos;

    }
    
    public IntegranteCicloVO[] getIntegrantesNuevoInterCiclo(int idGrupo, int idCiclo) throws ClientesException {

        String query = " Select en_numcliente, so_numgrupo, en_numcliente, so_numsolicitud, en_nombre_completo, ifnull(de_montoautorizado,0) as de_montoautorizado ," +
                       " ifnull(de_comision,0) as de_comision, ifnull(so_numcheque,'')as so_numcheque, so_desembolsado, en_mediocobro ,so_subproducto, in_semDispersion," + 
                       " SE_MODULOS, EN_NUMSUCURSAL, DE_MONTOCONSEGURO, SE_CONTRATACION " + // Se agregan para calcular de manera informativa el costo del seguro
                       " from d_integrantes_interciclo, d_clientes, d_solicitudes" +
                       " left join d_decision_comite on (de_numcliente = so_numcliente and de_numsolicitud = so_numsolicitud)" +
                       " LEFT JOIN D_SEGUROS ON ( se_numcliente = so_numcliente AND se_numsolicitud = so_numsolicitud)" +
                       " where en_numcliente = in_numcliente" +
                       " and in_numsolicitud = so_numsolicitud" +
                       " and in_numcliente = so_numcliente"+
                       " and in_estatus =1" +
                       " and in_numgrupo = ?" +
                       " and in_numciclo = ?";

        ArrayList<IntegranteCicloVO> array = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO temporal = null;
        IntegranteCicloVO elementos[] = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = ["  + idGrupo + "]");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (getIntegranteActivo(rs.getInt("en_numcliente")).equals("")) {
                    temporal = new IntegranteCicloVO();
                    temporal.idGrupo = rs.getInt("so_numgrupo");
                    temporal.idCliente = rs.getInt("en_numcliente");
                    temporal.idSolicitud = rs.getInt("so_numsolicitud");
                    temporal.nombre = rs.getString("en_nombre_completo");
                    temporal.monto = rs.getDouble("de_montoautorizado");
                    temporal.comision = rs.getInt("de_comision");
                    temporal.numCheque = rs.getString("so_numcheque");
                    temporal.desembolsado = rs.getInt("so_desembolsado");
                    temporal.ordenPago = new OrdenDePagoDAO().getOrdenDePago(temporal.idCliente, temporal.idSolicitud);
                    temporal.medioCobro = rs.getInt("en_mediocobro");
                    temporal.tarjetaCobro = new TarjetaDAO().getTarjetaClinete(temporal.idCliente, temporal.idSolicitud);
                    temporal.esInterciclo = rs.getInt("so_subproducto");
                    temporal.semDisp = rs.getInt("in_semDispersion");
                    temporal.tipoSeguro = rs.getInt("se_modulos");
                    temporal.idSucursal = rs.getInt("en_numsucursal");
                    temporal.montoConSeguro = rs.getInt("de_montoconseguro");
                    temporal.montoConSeguroTemp = rs.getInt("de_montoconseguro");
                    temporal.segContratado = rs.getInt("SE_CONTRATACION");
                    array.add(temporal);
                }
            }
            if (array.size() > 0) {
                elementos = new IntegranteCicloVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (IntegranteCicloVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesNuevoInterCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesNuevoInterCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;

    }

    public ArrayList getIntegrantesCicloActual(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT en_numcliente, ic_numsolicitud, en_nombre_completo, de_montoautorizado FROM d_clientes "
                + "LEFT JOIN d_integrantes_ciclo ON (en_numcliente = ic_numcliente) "
                + "LEFT JOIN d_decision_comite ON (ic_numcliente = de_numcliente AND ic_numsolicitud = de_numsolicitud) "
                + "WHERE ic_numgrupo = ? AND ic_numciclo = ?";

        ArrayList<IntegranteCicloVO> arrayIntegrantes = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                integrante.idCliente = rs.getInt("en_numcliente");
                integrante.idSolicitud = rs.getInt("ic_numsolicitud");
                integrante.nombre = rs.getString("en_nombre_completo");
                integrante.monto = rs.getDouble("de_montoautorizado");
                arrayIntegrantes.add(integrante);
//				Logger.debug("Integrante encontrado : "+temporal.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesCicloActual", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesCicloActual", e);
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
        return arrayIntegrantes;

    }
    
    public ArrayList getIntegrantesCicloActivo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT ic_numgrupo, ic_numciclo, ic_numcliente, ic_numsolicitud "
                + " from d_integrantes_ciclo "
                + " where ic_numgrupo = ?"
                + " and ic_numciclo =?"
                + " and ic_estatus != 2";

        ArrayList<IntegranteCicloVO> arrayIntegrantes = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                    integrante.idGrupo = rs.getInt("ic_numgrupo");
                    integrante.idCiclo = rs.getInt("ic_numciclo");
                    integrante.idCliente = rs.getInt("ic_numcliente");
                    integrante.idSolicitud = rs.getInt("ic_numsolicitud");
                arrayIntegrantes.add(integrante);
//				Logger.debug("Integrante encontrado : "+temporal.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesCicloActual", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesCicloActual", e);
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
        return arrayIntegrantes;

    }

    public ArrayList getNuevosIntegrantes(int idGrupo) throws ClientesException {

        String query = "SELECT en_numcliente, so_numsolicitud, en_nombre_completo, de_montoautorizado FROM d_solicitudes "
                + "LEFT JOIN d_clientes ON (en_numcliente = so_numcliente) "
                + "LEFT JOIN d_decision_comite ON (so_numcliente = de_numcliente AND so_numsolicitud = de_numsolicitud) "
                + "WHERE so_numgrupo = ? AND so_desembolsado IN (0,1);";

        ArrayList<IntegranteCicloVO> arrayIntegrantes = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            //ps.setInt(1, idOperacion);
            ps.setInt(1, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idGrupo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new IntegranteCicloVO();
                temporal.idCliente = rs.getInt("en_numcliente");
                temporal.idSolicitud = rs.getInt("so_numsolicitud");
                temporal.nombre = rs.getString("en_nombre_completo");
                temporal.monto = rs.getDouble("de_montoautorizado");
                arrayIntegrantes.add(temporal);
            }

        } catch (SQLException sqle) {
            myLogger.error("getNuevosIntegrantes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNuevosIntegrantes", e);
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
        return arrayIntegrantes;

    }

    public IntegranteCicloVO getIntegranteCiclo(int idCliente, int idSolicitud) throws ClientesException {

        /*String query =	"SELECT IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, IC_FECHA_CAPTURA " +
         "FROM D_INTEGRANTES_CICLO WHERE IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ?";*/
        String query
                = "SELECT IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_MONTO_REFINANCIAR, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, IC_FECHA_CAPTURA, SE_PRIMA_TOTAL "
                + "FROM D_INTEGRANTES_CICLO "
                + "LEFT JOIN D_SEGUROS ON ( se_numcliente=ic_numcliente AND se_numsolicitud=ic_numsolicitud ) "
                + "WHERE IC_NUMCLIENTE = ? "
                + "AND IC_NUMSOLICITUD = ?";

        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idCliente + ", " + idSolicitud + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                integrante.idGrupo = rs.getInt("IC_NUMGRUPO");
                integrante.idCiclo = rs.getInt("IC_NUMCICLO");
                integrante.idCliente = rs.getInt("IC_NUMCLIENTE");
                integrante.idSolicitud = rs.getInt("IC_NUMSOLICITUD");
                integrante.monto = rs.getDouble("IC_MONTO");
                integrante.montoRefinanciado = rs.getDouble("IC_MONTO_REFINANCIAR");
                integrante.comision = rs.getInt("IC_COMISION");
                integrante.estatus = rs.getInt("IC_ESTATUS");
                integrante.calificacion = rs.getInt("IC_CALIFICACION");
                integrante.rol = rs.getInt("IC_ROL");
                integrante.fechaCaptura = rs.getTimestamp("IC_FECHA_CAPTURA");
                integrante.ordenPago = new OrdenDePagoDAO().getOrdenDePago(integrante.idCliente, integrante.idSolicitud);
                integrante.primaSeguro = rs.getDouble("SE_PRIMA_TOTAL");
//				Logger.debug("Integrante encontrado : "+integrante.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteCiclo", e);
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
        return integrante;

    }

    public IntegranteCicloVO getIntegranteCicloPorRol(int idEquipo, int idCiclo, int rol) throws ClientesException {

        String query = "SELECT * FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_rol=?";

        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            ps.setInt(3, rol);
            /*
             myLogger.debug("Ejecutando = " + query);
             myLogger.debug("Parametros [" + idCliente + ", " + idSolicitud + "]");
             */
            ResultSet rs = ps.executeQuery();
            myLogger.debug(ps.toString());
            while (rs.next()) {
                integrante = new IntegranteCicloVO(rs.getInt("ic_numcliente"), rs.getInt("ic_numsolicitud"), rs.getInt("ic_Rol"));
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteCicloPorRol", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteCicloPorRol", e);
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
        return integrante;

    }

    public IntegranteCicloVO getIntegranteCicloPorClienteCiclo(int idGrupo, int idCliente, int idCiclo) throws ClientesException {

        /*String query =	"SELECT IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, IC_FECHA_CAPTURA " +
         "FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCLIENTE = ? AND IC_NUMCICLO = ?";*/
        String query = "SELECT IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_MONTO_REFINANCIAR, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, IC_FECHA_CAPTURA, SE_PRIMA_TOTAL "
                + "FROM D_INTEGRANTES_CICLO "
                + "LEFT JOIN D_SEGUROS ON ( se_numcliente=ic_numcliente AND se_numsolicitud=ic_numsolicitud ) "
                + "WHERE IC_NUMGRUPO = ? "
                + "AND IC_NUMCLIENTE = ? "
                + "AND IC_NUMCICLO = ? ";
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCliente);
            ps.setInt(3, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros= [" + idGrupo + "," + idCliente + "," + idCiclo + "]");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                integrante.idGrupo = rs.getInt("IC_NUMGRUPO");
                integrante.idCiclo = rs.getInt("IC_NUMCICLO");
                integrante.idCliente = rs.getInt("IC_NUMCLIENTE");
                integrante.idSolicitud = rs.getInt("IC_NUMSOLICITUD");
                integrante.monto = rs.getDouble("IC_MONTO");
                integrante.montoRefinanciado = rs.getDouble("IC_MONTO_REFINANCIAR");
                integrante.comision = rs.getInt("IC_COMISION");
                integrante.estatus = rs.getInt("IC_ESTATUS");
                integrante.calificacion = rs.getInt("IC_CALIFICACION");
                integrante.rol = rs.getInt("IC_ROL");
                integrante.fechaCaptura = rs.getTimestamp("IC_FECHA_CAPTURA");
                integrante.ordenPago = new OrdenDePagoDAO().getOrdenDePago(integrante.idCliente, integrante.idSolicitud);
                integrante.primaSeguro = rs.getDouble("SE_PRIMA_TOTAL");
//				Logger.debug("Integrante encontrado : "+integrante.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteCicloPorClienteCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteCicloPorClienteCiclo", e);
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
        return integrante;

    }

    public int addIntegrante(Connection conn, int idGrupo, int idCiclo, IntegranteCicloVO integrante) throws ClientesException {
        // Se agrega el monto con seguro financiado, ultima columana IC_MONTOCONSEGURO
        String query = "INSERT INTO D_INTEGRANTES_CICLO (IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_MONTO_REFINANCIAR, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, ic_tipo, IC_FECHA_CAPTURA, IC_MONTOCONSEGURO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, integrante.idCliente);
            ps.setInt(param++, integrante.idSolicitud);
            ps.setDouble(param++, integrante.monto);
            ps.setDouble(param++, integrante.montoRefinanciado);
            ps.setInt(param++, integrante.comision);
            ps.setInt(param++, integrante.estatus);
            ps.setInt(param++, integrante.calificacion);
            ps.setInt(param++, integrante.rol);
            ps.setInt(param++, integrante.tipo);
            ps.setDouble(param++, integrante.montoConSeguro); // Se agrega monto con seguro financiado Agosto 2017
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Ciclo= " + integrante.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIntegrante", e);
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

    public int addIntegranteApertura(int idGrupo, int idCiclo, IntegranteCicloVO integrante) throws ClientesException {

        String query = "INSERT INTO D_INTEGRANTES_CICLO (IC_NUMGRUPO, IC_NUMCICLO, IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_MONTO, IC_MONTO_REFINANCIAR, IC_COMISION, IC_ESTATUS, IC_CALIFICACION, IC_ROL, ic_esnuevo, IC_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            //if (conn == null) {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*} else {
             ps = conn.prepareStatement(query);
             }*/
            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, integrante.idCliente);
            ps.setInt(param++, integrante.idSolicitud);
            ps.setDouble(param++, integrante.monto);
            ps.setDouble(param++, integrante.montoRefinanciado);
            ps.setInt(param++, integrante.comision);
            ps.setInt(param++, integrante.estatus);
            ps.setInt(param++, integrante.calificacion);
            ps.setInt(param++, integrante.rol);
            ps.setInt(param++, integrante.esNuevo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Ciclo= " + integrante.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIntegranteApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIntegranteApertura", e);
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
    
    public int deleteIntegrante(int idGrupo, int idCiclo, int idCliente) throws ClientesException {
        return deleteIntegrante(null, idGrupo, idCiclo, idCliente, false);
    }

    public int deleteIntegrante(Connection conn, int idGrupo, int idCiclo, int idCliente) throws ClientesException {
        return deleteIntegrante(conn, idGrupo, idCiclo, idCliente, false);
    }
    
    public int deleteIntegranteODS(int idGrupo, int idCiclo, int idCliente) {
        try {
            return deleteIntegrante(null, idGrupo, idCiclo, idCliente, true);
        } catch (ClientesException ex) {
            String query = "DELETE FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = "+idGrupo+" AND IC_NUMCICLO = "+idCiclo+" AND IC_NUMCLIENTE = "+idCliente+";";
            generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro integrante ciclo de la ODS: ", ex);
            myLogger.error(query);
        }
        return -1;
    }
    
    public int deleteIntegrante(Connection conn, int idGrupo, int idCiclo, int idCliente, boolean conexionODS) throws ClientesException {

        int res = 0;
        //EN CASO DE MODIFICAR EL QUERY, MODIFICAR TAMBIEN EL DEL METODO deleteIntegranteODS EN LA PARTE DEL CATCH
        String query = "DELETE FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ? AND IC_NUMCLIENTE = ?";
        int param = 1;
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            if (conn == null) {
                cn = (conexionODS)?getODSConnection():getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, idCliente);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteIntegrante", e);
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
    public int deleteIntegranteSolicitud(Connection conn, int idGrupo, int idCiclo, int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ? AND IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ?";
        int param = 1;
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteIntegrante", e);
            throw new ClientesException(e.getMessage());
        } 
        return res;
    }

    public int deleteIntegranteApertura(int idGrupo, int idCiclo) throws ClientesException {
        return deleteIntegranteApertura(idGrupo, idCiclo, false);
    }
    
    public int deleteIntegranteAperturaODS(int idGrupo, int idCiclo) {
        try {
            return deleteIntegranteApertura(idGrupo, idCiclo, true);
        } catch (ClientesException ex) {
            String query = "DELETE FROM d_integrantes_ciclo WHERE ic_numgrupo="+idGrupo+" AND ic_numciclo="+idCiclo+";";
            generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro integrante ciclo de la ODS: ", ex);
            myLogger.error(query);
        }
        return -1;
    }
    
    public int deleteIntegranteApertura(int idGrupo, int idCiclo, boolean conexionODS) throws ClientesException {

        int res = 0;
        //EN CASO DE MODIFICAR EL QUERY, MODIFICAR TAMBIEN EL DEL METODO deleteIntegranteAperturaODS EN LA PARTE DEL CATCH
        String query = "DELETE FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numciclo=?;";
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            cn = (conexionODS)?getODSConnection():getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteIntegranteApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteIntegranteApertura", e);
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
    
       public void generarArchivoDeleteODS(String registro){
           myLogger.info("Generando archivo deletes ODS...");
           String nombreArchivo = "deletesODS.txt";
        try {
            CatalogoDAO cat = new CatalogoDAO();
            ParametroVO param = cat.getParametro("RUTA_DELETES_ODS");
            List<String> regs = new ArrayList<String>();
            regs.add(registro);
            
            if(registro!=null && !registro.isEmpty()){
                Path file = Paths.get(param.valor+nombreArchivo);
                Files.write(file, regs, Charset.forName("UTF-8"),StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }
        } catch (IOException ex) {
            myLogger.error("Error al crear archivo "+nombreArchivo+"........", ex);
        } catch (ClientesException ex) {
            myLogger.error("Error al consultar la ruta de ODS DELETES...", ex);
        }
    }

    public boolean existeIntegrante(int idGrupo, int idCiclo, int idCliente) throws ClientesException {

        String query = "SELECT * FROM d_integrantes_ciclo WHERE ic_numgrupo = ? AND ic_numciclo = ? AND ic_numcliente = ?";
        boolean respuesta = false;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            ps.setInt(3, idCliente);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros [" + idGrupo + ", " + idCiclo + ", " + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("existeIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeIntegrante", e);
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
        return respuesta;
    }

    public boolean esSolicitudAsignada(int idCliente, int idCiclo, int idGrupo) throws ClientesException {

        String query = "SELECT * FROM d_integrantes_ciclo WHERE ic_numcliente =? AND ic_numsolicitud=? AND ic_numgrupo != ?";
        boolean activa = false;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idCiclo);
            ps.setInt(3, idGrupo);
            //Logger.debug("Ejecutando: "+ query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activa = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("esSolicitudAsignada", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("esSolicitudAsignada", e);
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
        return activa;
    }

    public boolean esSolicitudActiva(int idCliente, int idSolicitud, int idGrupo) throws ClientesException {

        String query = "SELECT ci_estatus FROM d_integrantes_ciclo LEFT JOIN d_ciclos_grupales "
                + "ON (ic_numgrupo = ci_numgrupo AND ic_numciclo = ci_numciclo) "
                + "WHERE ic_numcliente = ? AND ic_numsolicitud = ? AND ic_numgrupo !=? AND ci_estatus=1";
        boolean activa = false;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, idGrupo);
            myLogger.debug("Ejecutando: " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activa = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("esSolicitudActiva", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("esSolicitudActiva", e);
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
        return activa;
    }

    public double getMontoTotalCiclo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT SUM(IC_MONTO) AS SUMA "
                + "FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ?";

        Connection cn = null;
        double montoTotal = 0;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                montoTotal = rs.getDouble("SUMA");
                myLogger.debug("Monto total calculado : " + montoTotal);
            }

        } catch (SQLException sqle) {
            myLogger.error("getMontoTotalCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMontoTotalCiclo", e);
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
        return montoTotal;

    }

    public int getCalificacionIntegrante(int idCliente, int idSolicitud) throws ClientesException {
        String query = "SELECT de_decision_comite FROM d_decision_comite WHERE de_numcliente = ? AND de_numsolicitud = ?";
        Connection cn = null;
        int calificacion = 0;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                calificacion = rs.getInt("de_decision_comite");
            }
        } catch (SQLException sqle) {
            myLogger.error("getCalificacionIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCalificacionIntegrante", e);
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
        return calificacion;
    }

    public int getNext(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT COALESCE(MAX(IC_NUMCICLO),0)+1 AS NEXT FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCICLO = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
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

    public int integranteCiclo(int idGrupo, int idCliente) throws ClientesException {

        String query = "SELECT MAX(IC_NUMCICLO) AS CICLO FROM D_INTEGRANTES_CICLO WHERE IC_NUMGRUPO = ? AND IC_NUMCLIENTE=?";
        Connection cn = null;
        int noCiclo = 0;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCliente);
            ResultSet rs = ps.executeQuery();
//			Logger.debug("Ejecutando = "+query);
            if (rs.next()) {
                noCiclo = rs.getInt("CICLO");
            }
        } catch (SQLException sqle) {
            myLogger.error("integranteCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("integranteCiclo", e);
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
        return noCiclo;
    }

    public int updateIntegrante(Connection conn, int idGrupo, int idCiclo, IntegranteCicloVO integrante) throws ClientesException {

        String query = "UPDATE D_INTEGRANTES_CICLO SET IC_ESTATUS=?, IC_MONTO=?, IC_MONTO_REFINANCIAR=?, IC_COMISION=?, IC_ROL=? WHERE IC_NUMGRUPO=? AND IC_NUMCICLO=? AND IC_NUMCLIENTE=? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setDouble(param++, integrante.estatus);
            ps.setDouble(param++, integrante.monto);
            ps.setDouble(param++, integrante.montoRefinanciado);
            ps.setInt(param++, integrante.comision);
            ps.setInt(param++, integrante.rol);
            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, integrante.idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Ciclo= " + integrante.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateIntegrante", e);
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
    
    public int updateIntegranteAdicional(Connection conn, int idGrupo, int idCiclo, IntegranteCicloVO integrante) throws ClientesException {

        String query = "UPDATE d_integrantes_ciclo SET ic_estatus=?, ic_monto=?, ic_tipo_adi=?, ic_porcentajeAdicional=?, ic_montoAdicional=?, ic_fechaDesembAdicional=?, ic_montoconseguro=? "
                + "WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_numcliente=?;";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, integrante.estatus);
            ps.setDouble(param++, integrante.monto);
            ps.setInt(param++, integrante.tipo_adicional);
            ps.setInt(param++, integrante.idPorcentajeAdicional);
            ps.setDouble(param++, integrante.montoAdicional);
            ps.setDate(param++, Convertidor.toSqlDate(integrante.fechaDesembAdicional));
            ps.setDouble(param++, integrante.montoConSeguro);
            ps.setInt(param++, idGrupo);
            ps.setInt(param++, idCiclo);
            ps.setInt(param++, integrante.idCliente);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateIntegranteAdicional", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateIntegranteAdicional", e);
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

    public int numCiclosParticipados(IntegranteCicloVO integrante) throws ClientesException {

        String query = "select count(*) AS CICLOS from d_integrantes_ciclo where ic_numcliente= ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, integrante.idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                res = rs.getInt("CICLOS");
            }

            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("numCiclosParticipados", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("numCiclosParticipados", e);
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

    public CreditoVO numGrupoCicloXClienteSolisitud(SolicitudVO solicitud) throws ClientesException {

        String query = "SELECT ic_numgrupo,ic_numciclo FROM d_integrantes_ciclo WHERE ic_numcliente=? AND ic_numsolicitud=?";

        Connection cn = null;
        PreparedStatement ps = null;
        CreditoVO credito = null;
        int numGrupo = 0;
        int numCiclo = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, solicitud.idCliente);
            ps.setInt(2, solicitud.idSolicitud);
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Parametros [" + solicitud.idCliente + ", " + solicitud.idSolicitud + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numGrupo = rs.getInt(1);
                numCiclo = rs.getInt(2);
                credito = new CreditoVO(numGrupo, numCiclo);
            }
        } catch (SQLException sqle) {
            myLogger.error("numGrupoCicloXClienteSolisitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("numGrupoCicloXClienteSolisitud", e);
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
        return credito;
    }

    public int getIntegranteTCI(int idCliente) throws ClientesException {

        String query = "SELECT count(ib_credito) AS ACTIVO FROM d_integrantes_ciclo LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap AND ic_numciclo=ib_numsolicitudsicap) "
                + "WHERE ic_numcliente=? AND ib_estatus=" + ClientesConstants.SITUACION_CREDITO_SALDOST24_CARTERACEDIDA;
        int invalido = 0;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("ACTIVO") != 0) {
                    invalido = 1;
                }
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteTCI", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteTCI", e);
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
        return invalido;

    }

    public String getIntegranteActivo(int idCliente) throws ClientesException {

        String query = "SELECT ib_referencia FROM d_integrantes_ciclo LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap AND ic_numciclo=ib_numsolicitudsicap) "
                + "WHERE ic_numcliente=? AND ic_estatus=0 AND ib_estatus!=3";
        String referencia = "";
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                referencia = rs.getString("ib_referencia");
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegranteActivo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegranteActivo", e);
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
        return referencia;

    }

    public int getGrupoGarantia(int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT ci_desembolsado FROM d_solicitudes,d_integrantes_ciclo,d_ciclos_grupales "
                + "WHERE ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud AND ic_numgrupo=ci_numgrupo AND ic_numciclo=ic_numciclo AND so_numcliente=? AND so_numsolicitud=?";
        int garantia = 0;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + ", " + idSolicitud + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                garantia = rs.getInt("ci_desembolsado");
            }

        } catch (SQLException sqle) {
            myLogger.error("getGrupoGarantia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getGrupoGarantia", e);
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
        return garantia;

    }

    public double getMontoMaximoAutorizado(int idCliente) throws ClientesException {

        String query = "SELECT MAX(de_montoautorizado) as montoAutorizado FROM d_integrantes_ciclo "
                + "INNER JOIN d_ciclos_grupales ON (ic_numgrupo=ci_numgrupo AND ic_numciclo=ci_numciclo)"
                + "INNER JOIN d_decision_comite ON (ic_numcliente=de_numcliente AND ic_numsolicitud=de_numsolicitud)"
                + "WHERE ic_numcliente=? AND ci_numcredito_ibs!=0";
        double montoMaximo = 0;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDouble(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                montoMaximo = rs.getDouble("montoAutorizado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getMontoMaximoAutorizado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMontoMaximoAutorizado", e);
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
        return montoMaximo;

    }

    public Date getTipoIntegrante(int idCliente) throws ClientesException {

        String query = "SELECT MAX(ib_fecha_vencimiento) AS fecha FROM d_integrantes_ciclo LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap and ic_numciclo=ib_numsolicitudsicap) "
                //+"AND ib_estatus=3) WHERE ic_numcliente=?;";
                + " WHERE ic_numcliente=? AND ic_estatus=0;";
        Date fecha = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + idCliente + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fecha = rs.getDate("fecha");
            }
        } catch (SQLException sqle) {
            myLogger.error("getTipoIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTipoIntegrante", e);
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
        return fecha;

    }

    public void updateTipoIntegrante(CicloGrupalVO ciclo, int idCliente, int tipo) throws ClientesException {

        String query = "UPDATE d_integrantes_ciclo SET ic_tipo=? WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_numcliente=?;";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, tipo);
            ps.setInt(2, ciclo.idGrupo);
            ps.setInt(3, ciclo.idCiclo);
            ps.setInt(4, idCliente);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateTipoIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateTipoIntegrante", e);
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
    }

    public void updateEnvioTelecom(OrdenDePagoVO orden) throws ClientesException {

        String query = "UPDATE d_integrantes_ciclo SET ic_enviado=1 WHERE ic_numcliente=? AND ic_numsolicitud=?;";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, orden.getIdCliente());
            ps.setInt(2, orden.getIdSolicitud());
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateEnvioTelecom", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEnvioTelecom", e);
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
    }
    
    public int getCicloSolicitud(int numEquipo, SolicitudVO solicitud) throws ClientesException {

        String query = "SELECT ic_numciclo FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numcliente=? AND ic_numsolicitud=?;";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int numCiclo = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, solicitud.idCliente);
            ps.setInt(3, solicitud.idSolicitud);
            myLogger.debug("Ejecutando: "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                numCiclo = rs.getInt("ic_numciclo");
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numCiclo;
    }
    
    /**
     * JECB 01/10/2017
     * M�todo que verifica los datos de devolucion de una orden de pago
     * @param statusIntegranteCiclo estatus del integrande del ciclo
     * @param numEquipo numero de equipo
     * @param numCiclo numero de ciclo
     * @param referencia referencia de la orden de pago
     * @return 1 en caso de que la validacion sea exitosa , 0 en caso contrario
     * @throws ClientesException 
     */
    public int verificaDatosDevolicion(int statusIntegranteCiclo,int numEquipo, int numCiclo, String referencia) throws ClientesException {
        
        int valido = 0;
        String query = "Select pg_numcliente from d_integrantes_ciclo, d_saldos, d_ordenes_de_pago" +
                       " where ic_numgrupo = ib_numclientesicap" +
                       " and ic_numciclo = ib_numsolicitudsicap" +
                       " and ic_numcliente = pg_numcliente" +
                       " and ic_numsolicitud = pg_numsolicitud"+
                       " and ic_estatus = ?" +
                       " and ib_numclientesicap = ?" +
                       " and ib_numsolicitudsicap = ?" +
                       " and pg_referencia = ?;";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, statusIntegranteCiclo);
            ps.setInt(2, numEquipo);
            ps.setInt(3, numCiclo);
            ps.setString(4, referencia);
            myLogger.debug("Ejecutando: "+ps);
            rs = ps.executeQuery();
            if(rs.next()) {
                valido = 1;
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return valido;
    }
    
    public void updateTipoCobroIntegrante(Connection con, IntegranteCicloVO integrante, int tipoCobro) throws ClientesException, SQLException {

        String query = "UPDATE d_integrantes_ciclo SET ic_mediodisp=? WHERE ic_numgrupo=? AND ic_numciclo=? AND ic_numcliente=?;";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, tipoCobro);
            ps.setInt(2, integrante.idGrupo);
            ps.setInt(3, integrante.idCiclo);
            ps.setInt(4, integrante.idCliente);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateTipoCobroIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateTipoCobroIntegrante", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null)
                ps.close();
        }
    }
    
    public void updateRolIntegrante(Connection con, IntegranteCicloVO integrante) throws ClientesException, SQLException {

        String query = "UPDATE d_integrantes_ciclo SET ic_rol=? WHERE ic_numcliente=? AND ic_numsolicitud=?;";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, integrante.rol);
            ps.setInt(2, integrante.idCliente);
            ps.setInt(3, integrante.idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateRolIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateRolIntegrante", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null)
                ps.close();
        }
    }
    public void updateRolMontoIntegrante(Connection con, IntegranteCicloVO integrante) throws ClientesException, SQLException {

        String query = "UPDATE d_integrantes_ciclo SET ic_rol=?, ic_monto=? WHERE ic_numcliente=? AND ic_numsolicitud=?;";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, integrante.rol);
            ps.setDouble(2, integrante.monto);
            ps.setInt(3, integrante.idCliente);
            ps.setInt(4, integrante.idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateRolIntegrante", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateRolIntegrante", e);
            throw new ClientesException(e.getMessage());
        } finally{
            if(ps!=null)
                ps.close();
        }
    }
    
    public double getMontoMaximoAutorizadoCC(int idCliente) throws ClientesException {

        String query = "SELECT MAX(ib_monto_desembolsado) as montoAutorizado FROM d_integrantes_ciclo INNER JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap AND ic_numciclo=ib_numsolicitudsicap) "
                +"WHERE ic_numcliente=? AND ic_estatus=0;";
        double montoMaximo = 0;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDouble(1, idCliente);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                montoMaximo = rs.getDouble("montoAutorizado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getMontoMaximoAutorizadoCC", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMontoMaximoAutorizadoCC", e);
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
        return montoMaximo;

    }
    
    public int getNumDiasUltFechaDesembolso(int idCliente) throws ClientesException {

        String query = "SELECT TIMESTAMPDIFF(DAY,ib_fecha_desembolso,CURDATE()) AS nummeses FROM d_solicitudes "
                +"LEFT JOIN d_integrantes_ciclo ON (so_numcliente=ic_numcliente and so_numsolicitud=ic_numsolicitud) "
                +"LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap and ic_numciclo=ib_numsolicitudsicap) "
                +"WHERE so_numcliente=? AND so_estatus in (1,2) ORDER BY ib_fecha_desembolso DESC LIMIT 1;";
        int difMeses = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                difMeses = rs.getInt("nummeses");
            }
            myLogger.debug("difMeses "+difMeses);
        } catch (SQLException sqle) {
            myLogger.error("getNumDiasUltFechaDesembolso", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumDiasUltFechaDesembolso", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return difMeses;

    }
    
    public int getNumUltSolicitudDes(int idCliente) throws ClientesException {

        String query = "SELECT ifnull(so_numsolicitud,0) as so_numsolicitud FROM d_solicitudes " +
                      "  LEFT JOIN d_integrantes_ciclo ON (so_numcliente=ic_numcliente and so_numsolicitud=ic_numsolicitud) " +
                      "  LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap and ic_numciclo=ib_numsolicitudsicap)" +
                      "  WHERE so_numcliente=? AND so_estatus = 2 ORDER BY ib_fecha_desembolso DESC LIMIT 1";
        int numSolicitud = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando = " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                numSolicitud = rs.getInt("so_numsolicitud");
            }
            myLogger.debug("numSolicitud "+numSolicitud);
        } catch (SQLException sqle) {
            myLogger.error("getNumUltSolicitudDes", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumUltSolicitudDes", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return numSolicitud;

    }
    /**
     * JECB 01/10/2017
     * M�todo que obtiene el tipo de adicional de un integrante ciclo
     * apartir de una referencia de orden de pago
     * @param numEquipo numero de equipo
     * @param numCiclo numero de ciclo
     * @param referencia referencia
     * @return 0 en caso de que la refenrencia a la orden de pago no sea de 
     * un adicional , en cualquier otro caso un valor de diferente de 0
     * que sera el tipo de adicional asignado al cliente
     * @throws ClientesException 
     */
    public int obtenerTipoAdicionalIntegranteCiclo(int numEquipo, int numCiclo, String referencia) throws ClientesException {
        
        int valido = 0;

        String query = "Select ic_tipo_adi from d_integrantes_ciclo, d_saldos, d_ordenes_de_pago" +
                       " where ic_numgrupo = ib_numclientesicap" +
                       " and ic_numciclo = ib_numsolicitudsicap" +
                       " and ic_numcliente = pg_numcliente" +
                       " and ic_numsolicitud = pg_numsolicitud"+
                       " and ib_numclientesicap = ?" +
                       " and ib_numsolicitudsicap = ?" +
                       " and pg_referencia = ?;";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            ps.setString(3, referencia);
            myLogger.debug("Ejecutando: "+ps);
            rs = ps.executeQuery();
            if(rs.next()) {
                valido = rs.getInt("ic_tipo_adi");
            }


















        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return valido;
    }
    
    /**
     * JECB 01/10/2017
     * Obtiene el integrante ciclo asociado a una orden de pago
     * @param ordenDePago orden de pago a la que se le quiere obtener el integrante ciclo
     * @return bean con el contenido de integrante ciclo o null en caso de no existir
     * @throws ClientesException 
     */
    public IntegranteCicloVO getIntegrantesCicloFromOrdenPago(OrdenDePagoVO ordenDePago) throws ClientesException {

        String query = "SELECT * "
                + " from d_integrantes_ciclo "
                + " where ic_numcliente = ?"
                + " and ic_numsolicitud =?"
                ;

        ArrayList<IntegranteCicloVO> arrayIntegrantes = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, ordenDePago.getIdCliente());
            ps.setInt(2, ordenDePago.getIdSolicitud());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [ " + ordenDePago.getIdCliente() + "," + ordenDePago.getIdSolicitud() + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integrante = new IntegranteCicloVO();
                    integrante.idGrupo = rs.getInt("ic_numgrupo");
                    integrante.idCiclo = rs.getInt("ic_numciclo");
                    integrante.idCliente = rs.getInt("ic_numcliente");
                    integrante.idSolicitud = rs.getInt("ic_numsolicitud");
                    /**
                     * JECB 01/10/2017
                     * Se mapean campos adicionales 
                     * de la consutal
                     */
                    integrante.estatus = rs.getInt("IC_ESTATUS");
                    integrante.monto = rs.getDouble("IC_MONTO");
                    integrante.tipo_adicional = rs.getInt("ic_tipo_adi");
                    
                    integrante.idPorcentajeAdicional = rs.getInt("ic_porcentajeAdicional");
                    integrante.montoAdicional = rs.getDouble("ic_montoAdicional");
                    integrante.fechaDesembAdicional = new Date(rs.getDate("ic_fechaDesembAdicional").getTime());
                    arrayIntegrantes.add(integrante);
//				Logger.debug("Integrante encontrado : "+temporal.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getIntegrantesCicloActual", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIntegrantesCicloActual", e);
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
        return arrayIntegrantes.isEmpty() ? null : arrayIntegrantes.get(0);

    }

/**
     * Metodo que obtiene grupos con clientes autorizados por excepcion que han sido dispersados durante el mes
     * @param numSucursal
     * @return
     * @throws ClientesException 
     */
    public ArrayList<IntegranteCicloVO> obtenerEquiposAutExcDispersados() throws ClientesException {
        
        String query = "select ge.ic_numgrupo, ge.ic_numciclo from " +
                        "(select distinct ic.ic_numgrupo, ic.ic_numciclo from d_credito cr " +
                        "inner join d_integrantes_ciclo ic on cr.CR_NUMCLIENTE = ic.ic_numcliente and cr.CR_NUMSOLICITUD = ic.ic_numsolicitud " +
                        "where cr.cr_acepta_regular in (3,4)) as ge " +
                        "inner join d_saldos sal on sal.ib_numClienteSICAP = ge.ic_numgrupo and sal.ib_numSolicitudSICAP = ge.ic_numciclo " +
                        "where month(sal.ib_fecha_desembolso) = month(CURRENT_DATE()) and year(sal.ib_fecha_desembolso) = year(CURRENT_DATE())";
        
        ArrayList<IntegranteCicloVO> array = new ArrayList<IntegranteCicloVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando: "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                IntegranteCicloVO temporal = new IntegranteCicloVO();
                temporal.idGrupo = rs.getInt("IC_NUMGRUPO");
                temporal.idCiclo = rs.getInt("IC_NUMCICLO");
                array.add(temporal);





            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return array;
    }
  /**
     /**
     * Metodo que obtiene los creditos de los clientes que estan autorizados por escepcion de un grupo
     * 
     * @param idGrupo
     * 
     * @param idCiclo
     * 
     * @return
     * 
     * @throws ClientesException 
     */
    public int  getNumPasesOtraFinanciera() throws ClientesException {
        int catPases = 0;
        Connection cn = null;
        String query = "Select count(distinct(ic_numgrupo)) from d_ciclos_grupales, d_integrantes_ciclo,d_credito" +
                " where ci_numgrupo = ic_numgrupo" +
                " and ic_numciclo = ci_numciclo" +
                "and ic_numcliente = CR_NUMCLIENTE" +
                "and ic_numsolicitud = CR_NUMSOLICITUD" +
                "and cr_acepta_regular = 6" +
                "and ci_estatus in(1,2)" +
                "and Month(ci_fecha_dispersion)= Month(NOW())" +
                "and year(ci_fecha_dispersion)= year(NOW())";
        try {
            cn = this.getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                catPases = rs.getInt("CantidadOtraFin");
            }
        } catch (SQLException sqle) {
            myLogger.error("getCreditosGrupoAutExcepcion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCreditosGrupoAutExcepcion", e);
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
        return catPases;
    }
    /**     * Actualiza el monto con seguro contratado.
     * 
     * @param con conexion DB
     * @param montoConSeguro monto asegurado
     * @param idCliente identificador del cliente
     * @param idSolicitud identificador de la solicitud
     * @param idCiclo identificador del ciclo al que pertenece el cliente
     * @throws ClientesException exception
     */
    public void updateMontoConSeguro(Connection con, double montoConSeguro, int idCliente, int idSolicitud, int idCiclo) throws ClientesException {

        StringBuilder query = new StringBuilder();
        query.append("UPDATE d_integrantes_ciclo ");
        query.append("SET    ic_montoconseguro = ?  ");
        query.append("WHERE  ic_numcliente = ?  ");
        query.append("AND    ic_numsolicitud = ?  ");
        query.append("AND    ic_numciclo = ?  ");

        try {
            PreparedStatement ps = con.prepareStatement(query.toString());
            ps.setDouble(1, montoConSeguro);
            ps.setInt(2, idCliente);
            ps.setInt(3, idSolicitud);
            ps.setInt(4, idCiclo);
            myLogger.debug("Ejecutando = " + query.toString());
            myLogger.debug("Parametros [" + montoConSeguro + ", " + idCliente + ", " + idSolicitud +  ", " + idCiclo +"]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateMontoConSeguro", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateMontoConSeguro", e);
            throw new ClientesException(e.getMessage());
        }
    }
     /**
     * Devuelve el integrante con sus monto y monto con seguro.
     * 
     * @param numEquipo id del grupo
     * @param numCiclo numero del ciclo
     * @param referencia referencia
     * @return monto con seguro
     * @throws ClientesException exception
     */
    public IntegranteCicloVO getDatosMontoConSeguroDevolicion(int statusIntegranteCiclo,int numEquipo, int numCiclo, String referencia) throws ClientesException {
        // Query para obtener el monto con seguro
        String query = "Select ic_monto, ic_montoconseguro, ic_tipo from d_integrantes_ciclo, d_saldos, d_ordenes_de_pago" +
                       " where ic_numgrupo = ib_numclientesicap" +
                       " and ic_numciclo = ib_numsolicitudsicap" +
                       " and ic_numcliente = pg_numcliente" +
                       " and ic_numsolicitud = pg_numsolicitud"+
                       " and ic_estatus = ?" +
                       " and ib_numclientesicap = ?" +
                       " and ib_numsolicitudsicap = ?" +
                       " and pg_referencia = ?;";
        IntegranteCicloVO integrante = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, statusIntegranteCiclo);
            ps.setInt(2, numEquipo);
            ps.setInt(3, numCiclo);
            ps.setString(4, referencia);
            myLogger.debug("Ejecutando: " + ps);
            rs = ps.executeQuery();
            if(rs.next()) {
                integrante = new IntegranteCicloVO();
                integrante.setMonto(rs.getDouble("ic_monto"));
                integrante.setMontoConSeguro(rs.getDouble("ic_montoconseguro"));
                integrante.setTipo(rs.getInt("ic_tipo"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());            }
        }
        return integrante;
    }
            
}

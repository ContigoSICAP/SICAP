package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.SegurosAfirmeVO;
import com.sicap.clientes.vo.SegurosVO;
import org.apache.log4j.Logger;

/*
 * 	Módulo: Gestión Seguros: JahTech
 */
public class SegurosAfirmeDAO extends DAOMaster {

        private static Logger logger = Logger.getLogger(SegurosAfirmeDAO.class);

    
    public SegurosAfirmeVO getSegurosAfirme(int idCliente) throws ClientesException {
        String query = "SELECT SA_IDCLIENTE, SA_IDSEGURO, SA_NOMBRE_CLIENTE, SA_FECHA_CONTRATACION, SA_FECHA_VENCIMIENTO, SA_MONTO_PAGADO, "
                + " SA_ESTATUS "
                + " FROM D_SEGUROS_AFIRME WHERE SA_IDCLIENTE = ?";

        SegurosAfirmeVO seg = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();
            if (rs.next()) {
                seg = new SegurosAfirmeVO();
                seg.idCliente = rs.getInt("SA_IDCLIENTE");
                seg.idSeguro = rs.getInt("SA_IDSEGURO");
                seg.nombreCliente = rs.getString("SA_NOMBRE_CLIENTE");
                seg.fechaContratacion = rs.getDate("SA_FECHA_CONTRATACION");
                seg.fechaVencimiento = rs.getDate("SA_FECHA_VENCIMIENTO");
                seg.montoPagado = rs.getInt("SA_MONTO_PAGADO");
                seg.estatus = rs.getInt("SA_ESTATUS");

                logger.debug("Ejecutando = " + query);
                logger.debug("Seguros: " + seg);
            }
        } catch (SQLException sqle) {
            logger.error("getSegurosAfirme", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getSegurosAfirme", e);
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
        return seg;

    }

    public ArrayList<SegurosAfirmeVO> getSegurosAfirme() throws ClientesException {

        String query = "SELECT SA_IDCLIENTE, SA_IDSEGURO, SA_NOMBRE_CLIENTE, SA_FECHA_CONTRATACION, SA_FECHA_VENCIMIENTO, SA_MONTO_PAGADO, "
                + " SA_ESTATUS "
                + " FROM D_SEGUROS_AFIRME";

        ArrayList<SegurosAfirmeVO> seguros = new ArrayList<SegurosAfirmeVO>();
        SegurosAfirmeVO seg = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next()) {
                seg = new SegurosAfirmeVO();
                seg.idSeguro = rs.getInt("SA_IDSEGURO");
                seg.nombreCliente = rs.getString("SA_NOMBRE_CLIENTE");
                seg.fechaContratacion = rs.getDate("SA_FECHA_CONTRATACION");
                seg.fechaVencimiento = rs.getDate("SA_FECHA_VENCIMIENTO");
                seg.montoPagado = rs.getInt("SA_MONTO_PAGADO");
                seg.estatus = rs.getInt("SA_ESTATUS");

                seguros.add(seg);
                logger.debug("Ejecutando = " + query);
                logger.debug("Seguros: " + seg);
            }
        } catch (SQLException sqle) {
            logger.error("getSegurosAfirme", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getSegurosAfirme", e);
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
        return seguros;

    }

    public int addSegurosAfirme(SegurosAfirmeVO seguro) throws ClientesException {

        String query = "INSERT INTO D_SEGUROS_AFIRME(SA_IDCLIENTE, SA_IDSEGURO, SA_NOMBRE_CLIENTE, SA_FECHA_CONTRATACION,"
                + "SA_FECHA_VENCIMIENTO, SA_MONTO_PAGADO, SA_ESTATUS) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, seguro.idCliente);
            ps.setInt(param++, seguro.idSeguro);
            ps.setString(param++, seguro.nombreCliente);
            ps.setDate(param++, seguro.fechaContratacion);
            ps.setDate(param++, seguro.fechaVencimiento);
            ps.setDouble(param++, seguro.montoPagado);
            ps.setInt(param++, seguro.estatus);
            logger.debug("Ejecutando insert = " + query);
            logger.debug("SeguroAfirme= " + seguro.toString());
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("addSegurosAfirme", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("addSegurosAfirme", e);
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

    public int updateSegurosAfirme(SegurosAfirmeVO seguro) throws ClientesException {

        String query = "UPDATE D_SEGUROS_AFIRME SET SA_IDSEGURO = ?, SA_NOMBRE_CLIENTE = ?, SA_FECHA_CONTRATACION = ?, SA_FECHA_VENCIMIENTO = ?, "
                + "SA_MONTO_PAGADO = ?, SA_ESTATUS = ? "
                + " WHERE SA_IDCLIENTE = ?";

        Connection cn = null;
        int res = 0;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, seguro.idSeguro);
            ps.setString(param++, seguro.nombreCliente);
            ps.setDate(param++, seguro.fechaContratacion);
            ps.setDate(param++, seguro.fechaVencimiento);
            ps.setDouble(param++, seguro.montoPagado);
            ps.setInt(param++, seguro.estatus);

            ps.setInt(param++, seguro.idCliente);

            logger.debug("Ejecutando update = " + query);
            logger.debug("SeguroAfirme = " + seguro.toString());
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("updateSegurosAfirme", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("updateSegurosAfirme", e);
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

    public int insertaSegurosAfirme() throws ClientesException {

        Connection cn = null;
        PreparedStatement ps = null;
        int res = 0;
		//String query="INSERT INTO D_SEGUROS_AFIRME_ SE_NUMCLIENTE = ?, SE_NUMSOLICITUD = ?, SE_NUMSEGURO = ?, SE_FECHA_CONTRATACION = ?," +
        //	"SE_FECHA_VENCIMIENTO = ?, SE_ESTATUS = ?, CURDATE(), extract(MONTH FROM curdate()) FROM D_SEGUROS";
        String query = "insert into d_seguros_afirme_ (SELECT se_numcliente, se_numsolicitud, se_numseguro, "
                + "se_fecha_contratacion, se_fecha_vencimiento, se_estatus, curdate(), extract(MONTH FROM curdate()),extract(YEAR FROM curdate()) "
                + "FROM d_seguros where (se_fecha_vencimiento >= curdate() || extract(MONTH FROM se_fecha_vencimiento) >= extract(MONTH FROM curdate()))) ";

        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            logger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);

        } catch (SQLException sqle) {
            logger.error("insertaSegurosAfirme", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("insertaSegurosAfirme", e);
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

    public ArrayList<SegurosVO> getDatosReporteSeguros(String mes, String año) throws ClientesException {

        Connection cn = null;
        ArrayList<SegurosVO> array = new ArrayList<SegurosVO>();
        SegurosVO temporal = null;
        String query = "SELECT distinct SU_NOMBRE 'SUCURSAL',EN_NOMBRE_COMPLETO 'NOMBRE',EN_NUMCLIENTE 'NUM CLIENTE',"
                + "SE_FECHA_CONTRATACION 'FECHA_CONTRATACION',SE_FECHA_VENCIMIENTO 'FECHA_VENCIMIENTO',"
                + "case X.SE_SUMA_ASEGURADA when 1 then '15000' when 2 then '30000' when 3 then '50000' end 'SUMA ASEGURADA',"
                + "X.SE_MODULOS 'MODULOS',DE_PLAZOAUTORIZADO 'PLAZO',EN_FECHA_NAC 'FECHA NACIMIENTO',SE_PRIMA 'PRIMA',"
                + "sa_ESTATUS 'STATUS',SA_FECHA 'FECHA' FROM D_CLIENTES A,D_SOLICITUDES,D_DECISION_COMITE,C_SUCURSALES S,D_SEGUROS X,"
                + "d_seguros_afirme_ sa,C_OPERACIONES WHERE EN_NUMCLIENTE = SO_NUMCLIENTE AND SO_NUMCLIENTE = DE_NUMCLIENTE "
                + "AND SO_NUMSOLICITUD = DE_NUMSOLICITUD AND EN_NUMSUCURSAL = SU_NUMSUCURSAL AND EN_NUMCLIENTE = X.SE_NUMCLIENTE "
                + "AND SO_NUMSOLICITUD = X.SE_NUMSOLICITUD AND se_NUMCLIENTE = sa_NUMCLIENTE AND se_NUMSOLICITUD = sa_NUMSOLICITUD "
                + "AND X.SE_CONTRATACION = 1 AND SO_ESTATUS = 2 AND SO_NUMOPERACION = OP_NUMOPERACION AND (se_fecha_vencimiento >= curdate() || extract(MONTH FROM se_fecha_vencimiento) >= extract(MONTH FROM curdate()) ) and sa_mes = ? and sa_año = ? "
                + "ORDER BY 1,2,3";
        try {

            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, mes);
            ps.setString(2, año);
            logger.debug("Ejecutando = " + query);
            logger.debug("Parametros: " + mes + " , " + año);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new SegurosVO();
                temporal.sucursal = rs.getString("SUCURSAL");
                temporal.nombreCliente = rs.getString("NOMBRE");
                temporal.idCliente = rs.getInt("NUM CLIENTE");
                temporal.fechaContratacion = rs.getDate("FECHA_CONTRATACION");
                temporal.fechaVencimiento = rs.getDate("FECHA_VENCIMIENTO");
                temporal.sumaAsegurada = rs.getInt("SUMA ASEGURADA");
                temporal.modulos = rs.getInt("MODULOS");
                temporal.plazo = rs.getInt("PLAZO");
                temporal.fechaNacimiento = rs.getDate("FECHA NACIMIENTO");
                temporal.prima = rs.getDouble("PRIMA");
                temporal.estatus = rs.getInt("STATUS");
                temporal.fechaReporte = rs.getDate("FECHA");
                array.add(temporal);
                logger.debug("Seguro encontrado : " + temporal.toString());
            }

        } catch (SQLException sqle) {
            logger.error("getDatosReporteSeguros", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getDatosReporteSeguros", e);
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
}

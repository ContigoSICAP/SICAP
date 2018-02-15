package com.sicap.clientes.dao;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.vo.ReporteVO;
import org.apache.log4j.Logger;

public class ReporteDAO extends DAOMaster {

    public static Logger myLogger = Logger.getLogger(ReporteDAO.class);

//    public ReporteVO[] getSolicitudesPorSucursalEstatus(int sucursal, int estatus, int producto, Date fInicial, Date ffinal) {
//        Connection cn = null;
//        int param = 1;
//        ReporteVO[] reportes = null;
//        ArrayList<ReporteVO> array = new ArrayList<ReporteVO>();
//        String query = "SELECT DISTINCT EN_NUMCLIENTE, EN_RFC, EN_NOMBRE_COMPLETO, OP_NUMOPERACION, OP_NOMBRE, SO_NUMSOLICITUD, SO_ESTATUS, SO_DESEMBOLSADO, SC_DICTAMEN, DE_CAUSARECHAZO, CA_NOMBRECUASARECHAZOCOMITE, SU_NOMBRE, AS_FECHA_CAPTURA ";
//        query += "FROM D_CLIENTES, C_SUCURSALES, C_OPERACIONES, D_SOLICITUDES ";
//        query += "LEFT JOIN (D_DECISION_COMITE JOIN C_CAUSA_RECHAZO_COMITE ON DE_CAUSARECHAZO = CA_NUMCAUSARECHAZOCOMITE AND DE_CAUSARECHAZO != 0) ";
//        query += "ON (SO_NUMCLIENTE = DE_NUMCLIENTE AND SO_NUMSOLICITUD = DE_NUMSOLICITUD) ";
//        query += "LEFT JOIN D_SCORING ON (SC_NUMCLIENTE = SO_NUMCLIENTE AND SO_NUMSOLICITUD = SC_NUMSOLICITUD) ";
//        query += "LEFT JOIN D_ARCHIVOS_ASOCIADOS ON (SO_NUMCLIENTE = AS_NUMCLIENTE AND SO_NUMSOLICITUD = AS_NUMSOLICITUD AND AS_TIPO = 8) ";
//        query += "LEFT JOIN D_BITACORA_ESTATUS ON (SO_NUMCLIENTE = BE_NUMCLIENTE AND SO_NUMSOLICITUD = BE_NUMSOLICITUD) ";
//        query += "WHERE EN_NUMCLIENTE = SO_NUMCLIENTE ";
//        if (sucursal != 0) {
//            query += "AND EN_NUMSUCURSAL = ? ";
//        }
//        query += "AND EN_NUMSUCURSAL = SU_NUMSUCURSAL ";
//        query += "AND OP_NUMOPERACION = SO_NUMOPERACION ";
//        if (estatus != 0) {
//            query += "AND SO_ESTATUS = ? ";
//        }
//        query += "AND ((BE_FECHA_HORA IS NOT NULL AND DATE(BE_FECHA_HORA) BETWEEN ? AND ? ) ";
//        query += "OR (BE_FECHA_HORA IS NULL AND DATE(SO_FECHA_CAPTURA) BETWEEN ? AND ?)) ";
//        if (producto != 0) {
//            query += "AND OP_NUMOPERACION = ? ";
//        }
//        query += "ORDER BY EN_NUMCLIENTE;";
//        try {
//            cn = getConnection();
//            PreparedStatement ps = cn.prepareStatement(query);
//            if (sucursal != 0) {
//                ps.setInt(param++, sucursal);
//            }
//            if (estatus != 0) {
//                ps.setInt(param++, estatus);
//            }
//            ps.setDate(param++, fInicial);
//            ps.setDate(param++, ffinal);
//            ps.setDate(param++, fInicial);
//            ps.setDate(param++, ffinal);
//            if (producto != 0) {
//                ps.setInt(param++, producto);
//            }
//            myLogger.debug("Ejecutando : " + query);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                ReporteVO reporteVO = new ReporteVO();
//                reporteVO.idCliente = rs.getInt("EN_NUMCLIENTE");
//                reporteVO.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
//                reporteVO.rfc = rs.getString("EN_RFC");
//                reporteVO.nombre = rs.getString("EN_NOMBRE_COMPLETO");
//                reporteVO.idProducto = rs.getInt("OP_NUMOPERACION");
//                reporteVO.producto = rs.getString("OP_NOMBRE");
//                reporteVO.idEstatusSolicitud = rs.getInt("SO_ESTATUS");
//                reporteVO.idEstatusDesembolso = rs.getInt("SO_DESEMBOLSADO");
//                reporteVO.idMotivoRechazo = rs.getInt("DE_CAUSARECHAZO");
//                reporteVO.descMotivoRechazo = rs.getString("CA_NOMBRECUASARECHAZOCOMITE");
//                reporteVO.nombreSucursal = rs.getString("SU_NOMBRE");
//                reporteVO.fechaCertificado = rs.getDate("AS_FECHA_CAPTURA");
//                reporteVO.dictamen = rs.getInt("SC_DICTAMEN");
//                array.add(reporteVO);
//            }
//            reportes = new ReporteVO[array.size()];
//            for (int i = 0; i < reportes.length; i++) {
//                reportes[i] = array.get(i);
//            }
//        } catch (SQLException exc) {
//            exc.printStackTrace();
//        } catch (NamingException exc) {
//            exc.printStackTrace();
//        } finally {
//            try {
//                if (cn != null) {
//                    cn.close();
//                }
//            } catch (SQLException exc) {
//                exc.printStackTrace();
//            }
//        }
//        return reportes;
//    }
    public ReporteVO[] getSolicitudesPorSucursalEstatus(int sucursal, int estatus, int producto, Date fInicial, Date ffinal) {
        Connection cn = null;
        int param = 1;
        ReporteVO[] reportes = null;
        ArrayList<ReporteVO> array = new ArrayList<ReporteVO>();
        String query = "SELECT EN_NUMCLIENTE,EN_RFC,EN_NOMBRE_COMPLETO,OP_NOMBRE,SO_NUMSOLICITUD,SO_ESTATUS,SO_DESEMBOLSADO,DE_CAUSARECHAZO,SU_NOMBRE,SO_FECHA_CAPTURA,(CASE so_ORIGENMIGRACION "
                + "        WHEN 3 THEN 'Movil' ELSE 'SICAP' END) AS 'ORIGEN', UPPER(CONCAT(ej_nombre,' ',EJ_APATERNO,' ',EJ_AMATERNO)) AS 'EJECUTIVO'"
                + "FROM D_SOLICITUDES INNER JOIN (D_CLIENTES INNER JOIN C_SUCURSALES ON SU_NUMSUCURSAL = EN_NUMSUCURSAL) ON (SO_NUMCLIENTE = EN_NUMCLIENTE AND EN_NUMSUCURSAL = ?) "
                + "INNER JOIN C_OPERACIONES ON (OP_NUMOPERACION = SO_NUMOPERACION) INNER JOIN C_EJECUTIVOS ON (EJ_NUMEJECUTIVO = SO_NUMEJECUTIVO) LEFT JOIN (D_DECISION_COMITE JOIN C_CAUSA_RECHAZO_COMITE ON CA_NUMCAUSARECHAZOCOMITE = DE_CAUSARECHAZO "
                + "AND DE_CAUSARECHAZO != 0) ON (DE_NUMCLIENTE = SO_NUMCLIENTE AND DE_NUMSOLICITUD = SO_NUMSOLICITUD) WHERE DATE(SO_FECHA_CAPTURA) BETWEEN ? AND ? ORDER BY SO_FECHA_CAPTURA";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, sucursal);
            ps.setDate(param++, fInicial);
            ps.setDate(param++, ffinal);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReporteVO reporteVO = new ReporteVO();
                reporteVO.idCliente = rs.getInt("EN_NUMCLIENTE");
                reporteVO.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                reporteVO.rfc = rs.getString("EN_RFC");
                reporteVO.nombre = rs.getString("EN_NOMBRE_COMPLETO");
                reporteVO.producto = rs.getString("OP_NOMBRE");
                reporteVO.idEstatusSolicitud = rs.getInt("SO_ESTATUS");
                reporteVO.idEstatusDesembolso = rs.getInt("SO_DESEMBOLSADO");
                reporteVO.idMotivoRechazo = rs.getInt("DE_CAUSARECHAZO");
                reporteVO.nombreSucursal = rs.getString("SU_NOMBRE");
                reporteVO.fechaCaptura = rs.getDate("SO_FECHA_CAPTURA");
                reporteVO.origen = rs.getString("origen");
                reporteVO.ejecutivo = rs.getString("ejecutivo");
                array.add(reporteVO);
            }
            reportes = new ReporteVO[array.size()];
            for (int i = 0; i < reportes.length; i++) {
                reportes[i] = array.get(i);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (NamingException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return reportes;
    }

    public ReporteVO[] getCiclosPorSucursalEstatus(int sucursal, int estatus, int producto, Date fInicial, Date ffinal) {
        Connection cn = null;
        int param = 1;
        ReporteVO[] reportes = null;
        ArrayList<ReporteVO> array = new ArrayList<ReporteVO>();
        String query = "SELECT gr_numsucursal , gr_rfc, su_nombre ,CI_NUMGRUPO,gr_nombre ,ci_numciclo ,ci_fecha_dispersion ,ec_descripcion ,ci_desembolsado,ci_fecha_captura, CI_ESTATUS, be_usuario, be_analisis_credito ";
        query += "FROM d_ciclos_grupales LEFT JOIN d_grupos ON (ci_numgrupo=gr_numgrupo) INNER JOIN c_sucursales ON (gr_numsucursal=su_numsucursal)\n";
        query += "INNER JOIN c_estatus_ciclo ON (ci_estatus=ec_numestatus)";
        query += "LEFT JOIN D_BITACORA_ESTATUS ON (CI_NUMGRUPO = BE_NUMCLIENTE AND CI_NUMCICLO = BE_NUMSOLICITUD) ";
        query += "WHERE ci_numcredito_ibs=0 AND LEFT(ci_fecha_captura,10)>= ? AND LEFT(ci_fecha_captura,10)<= ? ";
        if (sucursal != 0) {
            query += "AND GR_NUMSUCURSAL = ? ";
        }
        query += "AND GR_NUMSUCURSAL = SU_NUMSUCURSAL ";
        if (estatus != 0) {
            query += "AND CI_ESTATUS = ? ";
        }
        query += "AND ((BE_FECHA_HORA IS NOT NULL AND DATE(BE_FECHA_HORA) BETWEEN ? AND ? ) ";
        query += "OR (BE_FECHA_HORA IS NULL AND DATE(CI_FECHA_CAPTURA) BETWEEN ? AND ?)) ";
        query += "ORDER BY CI_NUMGRUPO;";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(param++, fInicial);
            ps.setDate(param++, ffinal);
            if (sucursal != 0) {
                ps.setInt(param++, sucursal);
            }
            if (estatus != 0) {
                ps.setInt(param++, estatus);
            }
            ps.setDate(param++, fInicial);
            ps.setDate(param++, ffinal);
            ps.setDate(param++, fInicial);
            ps.setDate(param++, ffinal);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReporteVO reporteVO = new ReporteVO();
                reporteVO.idCliente = rs.getInt("CI_NUMGRUPO");
                reporteVO.idSolicitud = rs.getInt("CI_NUMCICLO");
                reporteVO.rfc = rs.getString("GR_RFC");
                reporteVO.nombre = rs.getString("GR_NOMBRE");
                reporteVO.idProducto = ClientesConstants.GRUPAL;
                reporteVO.producto = "GRUPAL";
                reporteVO.idEstatusSolicitud = rs.getInt("CI_ESTATUS");
                reporteVO.idEstatusDesembolso = rs.getInt("CI_DESEMBOLSADO");
                reporteVO.idMotivoRechazo = 0;
                reporteVO.descMotivoRechazo = "";
                reporteVO.nombreSucursal = rs.getString("SU_NOMBRE");
                reporteVO.fechaCaptura = rs.getDate("CI_FECHA_CAPTURA");
                reporteVO.usuarioSucursal = rs.getString("BE_USUARIO");
                reporteVO.usuarioMesaControl = rs.getString("BE_ANALISIS_CREDITO");
                array.add(reporteVO);
            }
            reportes = new ReporteVO[array.size()];
            for (int i = 0; i < reportes.length; i++) {
                reportes[i] = array.get(i);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (NamingException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return reportes;
    }

    public ReporteVO[] getSolicitudesEstatus(int sucursal, int estatus, int numcliente, Date fInicial, Date ffinal) {
        Connection cn = null;
        int param = 1;
        ReporteVO[] reportes = null;
        ArrayList<ReporteVO> array = new ArrayList<ReporteVO>();
        String query = "SELECT so_numcliente, en_rfc,So_numsolicitud, en_nombre_completo , es_descripcion, so_desembolsado,en_numsucursal, so_fecha_captura, c.be_fecha_hora, c.be_comentario, ifnull(CR_COMPORTAMIENTO,99)as CR_COMPORTAMIENTO ,ifnull(CR_CALIFICACION_MESA_CONTROL,99) as CR_CALIFICACION_MESA_CONTROL, ifnull(CR_acepta_regular,0) as CR_acepta_regular" +
                       " FROM d_solicitudes" +
                       " inner JOIN (d_bitacora_estatus c INNER JOIN (SELECT be_numcliente, MAX(be_numsolicitud) maxsolicitud, MAX(be_numcomentario) comentario FROM d_bitacora_estatus GROUP BY be_numcliente, be_numsolicitud) d ON(c.be_numcliente = d.be_numcliente AND c.be_numsolicitud = maxsolicitud AND c.be_numcomentario = comentario))" +
                       " ON(so_numcliente=c.be_numcliente AND so_numsolicitud=c.be_numsolicitud)" +
                       " Inner Join d_clientes on so_numcliente = en_numcliente" +
                       " inner Join c_estatus_solicitud on (so_estatus = es_numestatus)" +
                       " left join  clientes_cec.D_CREDITO on (CR_NUMCLIENTE = so_numcliente and CR_NUMSOLICITUD = so_numsolicitud AND CR_TIPOCREDITO = 2 AND CR_NUMOBLIGADO = 0)"+
                       " where so_numsolicitud >= 1 ";
        if (numcliente != 0) {
            query += " and en_numcliente = ? ";
        } else {
            if (sucursal != 0) {
                
                query += " and en_numsucursal =? ";
            }
            if (estatus != 0) {
                query += " and so_estatus =? ";
            }
            if (fInicial != null && ffinal != null) {
                query += " and date(be_fecha_hora) between ? and ?";
            }
        }
        query +=" order by be_fecha_hora desc ";

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            if (numcliente != 0) {
                ps.setInt(param++, numcliente);
            } else {
                if (sucursal != 0) {
                    ps.setInt(param++, sucursal);
                }
                if (estatus != 0) {
                    ps.setInt(param++, estatus);
                }
                if (fInicial != null && ffinal != null) {                    
                    ps.setDate(param++, fInicial);
                    ps.setDate(param++, ffinal);
                }
            }

            myLogger.debug("Ejecutando : " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReporteVO reporteVO = new ReporteVO();
                reporteVO.idCliente = rs.getInt("so_numcliente");
                reporteVO.idSolicitud = rs.getInt("So_numsolicitud");
                reporteVO.rfc = rs.getString("en_rfc");
                reporteVO.nombre = rs.getString("en_nombre_completo");
                reporteVO.descEstatusSolicitud = rs.getString("es_descripcion");
                reporteVO.idEstatusDesembolso = rs.getInt("so_desembolsado");
                reporteVO.fechaHora = rs.getTimestamp("c.be_fecha_hora");
                reporteVO.calificacionCC = rs.getInt("CR_COMPORTAMIENTO");
                reporteVO.calificacionMesa = rs.getInt("CR_CALIFICACION_MESA_CONTROL");
                reporteVO.aceptaRegular = rs.getInt("CR_acepta_regular");
                reporteVO.idSucursal = rs.getInt("en_numsucursal");
                array.add(reporteVO);
            }
            reportes = new ReporteVO[array.size()];
            for (int i = 0; i < reportes.length; i++) {
                reportes[i] = array.get(i);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (NamingException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return reportes;
    }
    public ReporteVO[] getActualizacionDoc(int sucursal, int mes) {
        Connection cn = null;
        int param = 1;
        ReporteVO[] reportes = null;
        ArrayList<ReporteVO> array = new ArrayList<ReporteVO>();
        String query = "Select en_numcliente, en_rfc, so_numsolicitud, ib_nombreCliente , ic_numgrupo, en_nombre_completo, so_fecha_firma, date_add(so_fecha_firma, interval 1 year) as fecha_Vencimiento, Concat(ej_nombre,' ',ej_apaterno,' ',ej_amaterno) as ejecutivo" +
                       " from d_solicitudes as c,  d_clientes, d_integrantes_ciclo ,c_ejecutivos, d_saldos" +
                       " where so_numcliente = en_numcliente" +
                       " and  so_numejecutivo = ej_numejecutivo" +
                       " and ib_numClienteSICAP= ic_numgrupo" +
                       " and ib_numSolicitudSICAP= ic_numciclo" +
                       " and so_numcliente = ic_numcliente" +
                       " and so_numsolicitud = ic_numsolicitud" + 
                       " and so_desembolsado =2" +
                       " and ic_estatus =0" +
                       " and so_numsolicitud =   (select  max(so_numsolicitud )" +
                                                 " from d_solicitudes" +
                                                 " where c.so_numcliente = so_numcliente" +
                                                 " and so_domentacion_completa = 1)" +
                       " and en_numsucursal = ?" +
                       " and Month(so_fecha_firma)= ?" +
                       " and year(so_fecha_firma) = year(date_sub(curdate(), interval 1 year))" +
                       " order by ic_numgrupo";

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, sucursal);                            
            ps.setInt(param++, mes);
            myLogger.debug("Ejecutando : " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReporteVO reporteVO = new ReporteVO();
                reporteVO.idCliente = rs.getInt("en_numcliente");
                reporteVO.rfc = rs.getString("en_rfc"); 
                reporteVO.numGrupo = rs.getInt ("ic_numgrupo");
                reporteVO.nombreGrupo = rs.getString ("ib_nombreCliente");
                reporteVO.idSolicitud = rs.getInt("So_numsolicitud");
                reporteVO.nombre = rs.getString("en_nombre_completo");                
                reporteVO.fechaFirma = rs.getDate("so_fecha_firma");
                reporteVO.fechaVencimiento = rs.getDate("fecha_Vencimiento");
                reporteVO.ejecutivo = rs.getString("ejecutivo");
                array.add(reporteVO);
            }
            reportes = new ReporteVO[array.size()];
            for (int i = 0; i < reportes.length; i++) {
                reportes[i] = array.get(i);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (NamingException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return reportes;
    }

}

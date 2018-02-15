package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CalificacionAsesorVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

public class CalificacionAsesoresDAO extends DAOMaster{
    
    String query = "";
    PreparedStatement pstm = null;
    ResultSet res = null;
    Connection conn = null;
    
    public boolean insertGruoposTotales(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'grupostotales' AS campo,COUNT(ib_credito) AS valor FROM d_saldos,d_ciclos_grupales "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_fecha_desembolso<=? GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertGruoposTotales");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertGruoposVigentesTotales(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'gruposvigentestot' AS campo,COUNT(sh_credito) AS valor FROM d_saldos_hist,d_ciclos_grupales "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_estatus in (1,2,4) AND sh_fecha_desembolso<=? AND sh_fecha_historico=? "+
                    "GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertGruoposVigentesTotales");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertGruoposTotalesCiclo(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,CONCAT('numgpototc',ib_numSolicitudSICAP) AS campo,COUNT(ib_credito) AS valor "+
                    "FROM d_saldos,d_ciclos_grupales WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_fecha_desembolso<=? "+
                    "GROUP BY ci_ejecutivo,ib_numSolicitudSICAP ORDER BY ib_numSucursal,ci_ejecutivo,ib_numSolicitudSICAP;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertGruoposTotalesCiclo");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertGruoposVigentesTotalesCiclo(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,CONCAT('numgpovigc',sh_numSolicitudSICAP) AS campo,COUNT(sh_credito) AS valor "+
                    "FROM d_saldos_hist,d_ciclos_grupales WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_estatus in (1,2,4) AND sh_fecha_desembolso<=? "+
                    "AND sh_fecha_historico=? GROUP BY ci_ejecutivo,sh_numSolicitudSICAP ORDER BY sh_numSucursal,ci_ejecutivo,sh_numSolicitudSICAP;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertGruoposTotalesCiclo");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertClientesInicioMes(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'clientesinicio' AS campo,COUNT(ic_numcliente) AS valor FROM d_saldos_hist,d_ciclos_grupales,d_integrantes_ciclo "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND ci_numgrupo=ic_numgrupo AND ci_numciclo=ic_numciclo AND sh_fecha_desembolso<=? AND sh_estatus IN (1,2,4) "+
                    "AND sh_fecha_historico=? GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertClientesInicioMes");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertClientesFinMes(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'clientesfinal' AS campo,COUNT(ic_numcliente) AS valor FROM d_saldos_hist,d_ciclos_grupales,d_integrantes_ciclo "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND ci_numgrupo=ic_numgrupo AND ci_numciclo=ic_numciclo AND sh_fecha_desembolso<=? AND sh_estatus IN (1,2,4) "+
                    "AND sh_fecha_historico=? GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertClientesFinMes");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertTotGruposRenovar(Connection con, Savepoint save, String periodo, String fechaIni, String fechaFin) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'gpoarenovar' AS campo,COUNT(ib_credito) AS valor FROM d_saldos,d_ciclos_grupales "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_fecha_vencimiento BETWEEN ? AND ? GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fechaIni);
            pstm.setString(3, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fechaIni+", "+fechaFin+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertTotGruposRenovar");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertTotGruposRenovados(Connection con, Savepoint save, String periodo, String fechaIni, String fechaFin) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'gpoarenovado' AS campo,COUNT(ib_credito) AS valor FROM d_saldos,d_ciclos_grupales "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_fecha_desembolso BETWEEN ? AND ? GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fechaIni);
            pstm.setString(3, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fechaIni+", "+fechaFin+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertTotGruposRenovados");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertInicioCartera(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'carterainicial' AS campo,SUM(sh_saldo_con_intereses_al_final) AS valor FROM d_saldos_hist,d_ciclos_grupales "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_fecha_historico=? AND sh_estatus in (1,2,4) AND sh_fecha_desembolso<=? "+
                    "GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertInicioCartera");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertFinCartera(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'carterafinal' AS campo,SUM(sh_saldo_con_intereses_al_final) AS valor FROM d_saldos_hist,d_ciclos_grupales "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_fecha_historico=? AND sh_estatus in (1,2,4) AND sh_fecha_desembolso<=? "+
                    "GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            pstm.setString(3, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertFinCartera");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertMontoDesembolsado(Connection con, Savepoint save, String periodo, String fechaIni, String fechaFin) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'montodesembolsado' AS campo,SUM(ib_saldo_con_intereses_al_final) AS valor FROM d_saldos,d_ciclos_grupales "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_fecha_desembolso BETWEEN ? AND ? GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fechaIni);
            pstm.setString(3, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fechaIni+", "+fechaFin+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertMontoDesembolsado");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertInicioMora(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'morainicial' AS campo,SUM(sh_total_vencido) AS valor FROM d_saldos_hist,d_ciclos_grupales "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_fecha_historico=? AND sh_total_vencido>0 GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertInicioMora");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertFinalMora(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,sh_numSucursal,ci_ejecutivo,'morafinal' AS campo,SUM(sh_total_vencido) AS valor FROM d_saldos_hist,d_ciclos_grupales "+
                    "WHERE sh_numClienteSICAP=ci_numgrupo AND sh_numSolicitudSICAP=ci_numciclo AND sh_fecha_historico=? AND sh_total_vencido>0 GROUP BY ci_ejecutivo ORDER BY sh_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertFinalMora");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertFichasRecuperar(Connection con, Savepoint save, String periodo, String fechaIni, String fechaFin) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'fichascomprecuperar' as campo,COUNT(ta_numpago) AS valor FROM d_saldos,d_ciclos_grupales,cartera_fincontigo.d_tabla_amortizacion "+
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'fichascomprecuperar' as campo,COUNT(ta_numpago) AS valor FROM d_saldos,d_ciclos_grupales,cf_cartera_db.d_tabla_amortizacion "+
                    "WHERE ib_numclientesicap=ci_numgrupo AND ib_numsolicitudsicap=ci_numciclo AND ib_credito=ta_numcredito AND ta_fecha_pago BETWEEN ? AND ? AND ta_numpago NOT IN (14,15,16) "+
                    "GROUP BY ci_ejecutivo ORDER BY ib_numSucursal;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fechaIni);
            pstm.setString(3, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fechaIni+", "+fechaFin+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertFichasRecuperar");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertFichasRecuperadas(Connection con, Savepoint save, String periodo, String fechaIni, String fechaFin) throws SQLException{
        
        boolean listo = true;
        try {
            //query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'fichascomprecuperada' as campo,COUNT(ta_numpago) AS valor FROM d_saldos,d_ciclos_grupales,cartera_fincontigo.d_tabla_amortizacion "+
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'fichascomprecuperada' as campo,COUNT(ta_numpago) AS valor FROM d_saldos,d_ciclos_grupales,cf_cartera_db.d_tabla_amortizacion "+
                    "WHERE ib_numclientesicap=ci_numgrupo AND ib_numsolicitudsicap=ci_numciclo AND ib_credito=ta_numcredito AND ta_fecha_pago BETWEEN ? AND ? AND ta_numpago NOT IN (14,15,16) "+
                    "AND ta_multa=0 AND ta_pagado='S' GROUP BY ci_ejecutivo ORDER BY ib_numSucursal;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fechaIni);
            pstm.setString(3, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fechaIni+", "+fechaFin+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertFichasRecuperadas");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertSegurosInicial(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'seguroinicial' AS campo,SUM(se_numseguro) AS valor FROM d_saldos,d_ciclos_grupales,d_integrantes_ciclo,d_seguros "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ci_numgrupo=ic_numgrupo AND ci_numciclo=ic_numciclo AND ic_numcliente=se_numcliente AND ic_numsolicitud=se_numsolicitud "+
                    "AND ib_fecha_desembolso<=? AND se_contratacion=1 GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertSegurosInicial");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertSegurosFinal(Connection con, Savepoint save, String periodo, String fecha) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores SELECT 0,?,ib_numSucursal,ci_ejecutivo,'segurofinal' AS campo,SUM(se_numseguro) AS valor FROM d_saldos,d_ciclos_grupales,d_integrantes_ciclo,d_seguros "+
                    "WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ci_numgrupo=ic_numgrupo AND ci_numciclo=ic_numciclo AND ic_numcliente=se_numcliente AND ic_numsolicitud=se_numsolicitud "+
                    "AND ib_fecha_desembolso<=? AND se_contratacion=1 GROUP BY ci_ejecutivo ORDER BY ib_numSucursal,ci_ejecutivo;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setString(2, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+periodo+", "+fecha+"]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertSegurosFinal");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public boolean insertRegistroCampo(Connection con, Savepoint save, String periodo, int idSucursal, int idAsesor, String campo, double valor) throws SQLException{
        
        boolean listo = true;
        try {
            query = "INSERT INTO d_ranking_asesores VALUES(0,?,?,?,?,?);";
            pstm = con.prepareStatement(query);
            pstm.setString(1, periodo);
            pstm.setInt(2, idSucursal);
            pstm.setInt(3, idAsesor);
            pstm.setString(4, campo);
            pstm.setDouble(5, valor);
            Logger.debug("Ejecutando = "+query);
            pstm.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("Problema dentro insertRegistroCampo");
            con.rollback(save);
            e.printStackTrace();
            return listo = false;
        }
        return listo;
    }
    
    public ArrayList<CalificacionAsesorVO> getGruposAsesor(String fecha) throws ClientesException{
        
        ArrayList<CalificacionAsesorVO> arr = new ArrayList<CalificacionAsesorVO>();
        try {
            query = "SELECT ci_ejecutivo,sh_credito,sh_numsucursal,sh_total_vencido FROM d_saldos_hist,d_ciclos_grupales WHERE sh_estatus IN (1,2,4) AND sh_fecha_historico=? AND sh_numclientesicap=ci_numgrupo AND sh_numsolicitudsicap=ci_numciclo ORDER BY ci_ejecutivo,sh_credito;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setString(1, fecha);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+fecha+"]");
            res = pstm.executeQuery();
            while(res.next())
                arr.add(new CalificacionAsesorVO(res.getInt("ci_ejecutivo"), res.getInt("sh_credito"), res.getInt("sh_numsucursal"), res.getDouble("sh_total_vencido")));
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getGruposAsesor : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getGruposAsesor: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arr;
    }
    
    public int getOrigenCredito(int idCredito) throws ClientesException{
        
        int asesor = 0;
        try {
            query = "SELECT cr_num_ejecutivo FROM d_credito WHERE cr_num_credito=?;";
            conn = getCWConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idCredito);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCredito+"]");
            res = pstm.executeQuery();
            while(res.next())
                asesor = res.getInt("cr_num_ejecutivo");
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getOrigenCredito : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getOrigenCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return asesor;
    }
    
    public ArrayList<CalificacionAsesorVO> getOrigenClientes(String fechaIni, String fechaFin) throws ClientesException{
        
        ArrayList<CalificacionAsesorVO> arr = new ArrayList<CalificacionAsesorVO>();
        try {
            query = "SELECT ci_ejecutivo,ib_credito,ib_numsucursal,ib_numclientesicap,ib_numsolicitudsicap FROM d_saldos,d_ciclos_grupales WHERE ib_fecha_desembolso BETWEEN ? AND ? "+
                    "AND ib_numclientesicap=ci_numgrupo AND ib_numsolicitudsicap=ci_numciclo ORDER BY ci_ejecutivo,ib_credito;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setString(1, fechaIni);
            pstm.setString(2, fechaFin);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+fechaIni+", "+fechaFin+"]");
            res = pstm.executeQuery();
            while(res.next())
                arr.add(new CalificacionAsesorVO(res.getInt("ci_ejecutivo"), res.getInt("ib_credito"), res.getInt("ib_numsucursal"), res.getInt("ib_numclientesicap"), res.getInt("ib_numsolicitudsicap")));
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getOrigenClientes : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getOrigenClientes: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arr;
    }
    
    public ArrayList<Integer> getIntegrantesGrupo(int idGrupo, int idCiclo) throws ClientesException{
        
        ArrayList<Integer> arr = new ArrayList<Integer>();
        try {
            query = "SELECT ic_numcliente FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numciclo=?;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, idCiclo);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idGrupo+", "+idCiclo+"]");
            res = pstm.executeQuery();
            while(res.next())
                arr.add(res.getInt("ic_numcliente"));
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getIntegrantesGrupo : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getIntegrantesGrupo: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arr;
    }
    
    public int getOrigenCliente(int idCredito, int idCliente) throws ClientesException{
        
        int grupo = 0;
        try {
            query = "SELECT ic_numgrupo FROM d_saldos,d_integrantes_ciclo WHERE ib_numclientesicap=ic_numgrupo AND ib_numsolicitudsicap=ic_numciclo "+
                    "AND ib_credito!=? AND ic_numcliente=? ORDER BY ib_credito DESC LIMIT 1;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idCredito);
            pstm.setInt(2, idCliente);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCredito+", "+idCliente+"]");
            res = pstm.executeQuery();
            while(res.next())
                grupo = res.getInt("ic_numgrupo");
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getOrigenCliente : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getOrigenCliente: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return grupo;
    }
    
    public int getCicloAnterior(int idGrupo, int idCiclo) throws ClientesException{
        
        int asesor = 0;
        try {
            query = "SELECT ci_ejecutivo FROM d_saldos,d_ciclos_grupales WHERE ib_numClienteSICAP=ci_numgrupo AND ib_numSolicitudSICAP=ci_numciclo AND ib_numClienteSICAP=? AND ib_numSolicitudSICAP=?";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, idCiclo);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idGrupo+", "+idCiclo+"]");
            res = pstm.executeQuery();
            while(res.next())
                asesor = res.getInt("ci_ejecutivo");
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getCicloAnterior : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getCicloAnterior: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return asesor;
    }
    
    public ArrayList<Integer> getIntegrantesSeguro(int idGrupo, int idCiclo) throws ClientesException{
        
        ArrayList<Integer> arr = new ArrayList<Integer>();
        try {
            query = "SELECT se_numcliente FROM d_integrantes_ciclo,d_seguros WHERE ic_numcliente=se_numcliente AND ic_numsolicitud=se_numsolicitud "+
                    "AND ic_numgrupo=? AND ic_numciclo=? AND se_contratacion=1;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, idCiclo);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idGrupo+", "+idCiclo+"]");
            res = pstm.executeQuery();
            while(res.next())
                arr.add(res.getInt("se_numcliente"));
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getIntegrantesSeguro : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getIntegrantesSeguro: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arr;
    }
    
    public CalificacionAsesorVO getSeguro(int idCredito, int idCliente) throws ClientesException{
        
        CalificacionAsesorVO seguro = null;
        try {
            query = "SELECT ic_numgrupo,ib_credito FROM d_saldos,d_integrantes_ciclo,d_seguros WHERE ib_numclientesicap=ic_numgrupo AND "+
                    "ib_numsolicitudsicap=ic_numciclo AND ic_numcliente=se_numcliente AND ic_numsolicitud=se_numsolicitud AND "+
                    "ib_credito!=? AND ic_numcliente=? AND se_contratacion=1 ORDER BY ib_credito DESC LIMIT 1;";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idCredito);
            pstm.setInt(2, idCliente);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+idCredito+", "+idCliente+"]");
            res = pstm.executeQuery();
            while(res.next())
                seguro = new CalificacionAsesorVO(res.getInt("ib_credito"), res.getInt("ic_numgrupo"));
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getSeguro : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getSeguro: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return seguro;
    }
    
    public void updateParametro(String mes) throws ClientesException{
        
        try {
            query = "UPDATE c_parametros SET pa_valor=? WHERE pa_cve_param='MES_CIERRE_RATING';";
            conn = getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setString(1, mes);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+mes+"]");
            pstm.executeUpdate();
        } catch (SQLException sqle) {
            Logger.debug("SQLException en updateParametro : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en updateParametro: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
}

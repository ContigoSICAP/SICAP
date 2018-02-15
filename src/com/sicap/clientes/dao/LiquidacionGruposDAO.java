package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.sql.Date;

public class LiquidacionGruposDAO extends DAOMaster {
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(LiquidacionGruposDAO.class);

    public boolean updateEstatusCreditoCR(String nuevaFecha, int idGrupo, int idCiclo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_saldos SET ib_estatus= 3, ib_fecha_generacion=? WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ?";
        boolean ejecucion = false;

        try {
            con = getConnection();//clientes_Cec
            ps = con.prepareStatement(query);
            ps.setString(1, nuevaFecha);
            ps.setInt(2, idGrupo);
            ps.setInt(3, idCiclo);
            Logger.debug("Ejecutando: " + query);
            Logger.debug("Parametros[" + nuevaFecha + ", " + idGrupo + ", " + idCiclo + "]");
            if(ps.execute())
                ejecucion = true;
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en updateEstatusCredito: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en updateEstatusCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return ejecucion;
    }
    
    public boolean updateEstatusCreditoFC(int idGrupo, int idCiclo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_saldos SET ib_estatus= 3 WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ?";
        boolean ejecucion = false;

        try {
            con = getConnection();//clientes_Cec
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando: " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            if(ps.execute())
                ejecucion = true;
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en updateEstatusCredito: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en updateEstatusCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return ejecucion;
    }
    
    public int getEstatusCredito(int idGrupo, int idCiclo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int estatus = 0;
        String query = "SELECT ib_estatus FROM d_saldos WHERE ib_numclientesicap= ? AND ib_numsolicitudsicap= ?";
        
        try {
            con = getConnection();//clientes_Cec
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando: " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                estatus = res.getInt("ib_estatus");
            }
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en getEstatusCredito: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en getEstatusCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return estatus;
    }
    
    public String getReferencia(int idGrupo, int idCiclo) throws ClientesException {
        
        String query = "SELECT ib_referencia FROM d_saldos WHERE ib_numclientesicap=? and ib_numsolicitudsicap =?";
        String referencia = null;
        Connection cn = null;
        
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                referencia = rs.getString("ib_referencia");
            }
            
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en getReferencia: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en getReferencia: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return referencia;
    }
    
    public  int getCredito(int idGrupo, int idCiclo) throws ClientesException {
        
        String query = "SELECT ib_credito FROM d_saldos WHERE ib_numclientesicap=? and ib_numsolicitudsicap =?";
        int credito = 0;
        Connection cn = null;
        
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credito = rs.getInt("ib_credito");
            }
            
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en getCredito: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en getCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return credito;
    }
    
    public void deleteSaldos(int idGrupo, int idCiclo) throws ClientesException {
        Connection con = null;
        try {
            con = getConnection();
            String query = "DELETE FROM d_saldos WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getCredito: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en getCredito: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void insertSaldos(int idGrupo, int idCiclo) throws ClientesException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            String query = "INSERT INTO d_saldos "
                  +"SELECT ib_origen, ib_credito, ib_referencia, ib_numClienteSICAP, ib_numSolicitudSICAP, ib_numClienteIBS, ib_nombreCliente, ib_rfc, ib_numSucursal, ib_nombreSucursal, ib_fecha_envio, ib_fecha_generacion, ib_hora_generacion, ib_num_cuotas, ib_num_cuotas_trancurridas, ib_plazo, ib_periodicidad, ib_fecha_desembolso, ib_fecha_vencimiento, ib_monto_credito, ib_saldo_total_dia, ib_saldo_capital, ib_saldo_interes, ib_saldo_interes_vigente, ib_saldo_interes_vencido, ib_saldo_interes_vencido_90dias, ib_saldo_interes_cuentas_orden, ib_saldo_iva_interes, ib_saldo_bonificacion_iva, ib_saldo_interes_mora, ib_saldo_iva_mora, ib_saldo_multa, ib_saldo_iva_multa, ib_capital_pagado, ib_interes_normal_pagado, ib_iva_interes_normal_pagado, ib_bonificacion_pagada, ib_moratorio_pagado, ib_iva_moratorio_pagado, ib_multa_pagada, ib_iva_multa_pagada, ib_comision, ib_iva_comision, ib_monto_seguro, ib_monto_desembolsado, ib_fecha_sig_amortizacion, ib_capital_sig_amortizacion, ib_interes_sig_amortizacion, ib_iva_interes_sig_amortizacion, ib_fondeador, ib_nombre_fondeador, ib_tasa_interes_sin_iva, ib_tasa_mora_sin_iva, ib_tasa_iva, ib_saldo_con_intereses_al_final, ib_capital_vencido, ib_interes_vencido, ib_iva_interes_vencido, ib_total_vencido, ib_estatus, ib_producto, ib_fecha_incumplimiento, ib_fecha_a_cartera_vencida, ib_num_dias_mora, ib_dias_transcurridos, ib_cuotas_vencidas, ib_num_pagos_realizados, ib_moto_total_pagado, ib_fecha_ultimo_pago, ib_bandera_reestructura, ib_credito_reestructurado, ib_dias_mora_reestructura, ib_tasa_preferencial_iva, ib_cuenta_bucket, ib_saldo_bucket, ib_cta_contable, ib_numdespacho, ib_max_cuotas_vencidas, ib_max_dias_mora, ib_montoconintereses, ib_tasaelegida, 0, 0, 0 FROM clientes.d_saldos WHERE ib_numclientesicap=? AND ib_numsolicitudsicap=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en insertSaldos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en insertSaldos: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
            
    public boolean updateEstatusCiclo(int idGrupo) throws ClientesException {

        String query = "UPDATE d_ciclos_grupales SET ci_estatus=2 WHERE ci_numgrupo=? AND ci_estatus=1";

        Connection cn = null;
        PreparedStatement ps = null;
        boolean ejecucion = false;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros ["+idGrupo+"]");
            if(ps.execute())
                ejecucion = true;
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en updateEstatusCiclo : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en updateEstatusCiclo : " + e.getMessage());
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
        return ejecucion;
    }
    
    public int getEstatusCiclo(int idGrupo, int idCiclo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int estatus = 0;
        String query = "SELECT ci_estatus FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_numciclo=?";
        
        try {
            con = getConnection();//clientes_Cec
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando: " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                estatus = res.getInt("ci_estatus");
            }
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en getEstatusCiclo : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en getEstatusCiclo : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return estatus;
    }
    
    public void deleteCredito(int idGrupo, int idCiclo) throws ClientesException {
        Connection con = null;
        try {
            con = getCWConnection();
            String query = "DELETE FROM d_credito WHERE cr_num_cliente=? AND cr_num_solicitud=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            Logger.debug("SQLException en deleteCredito : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en deleteCredito : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void insertCredito(int idGrupo, int idCiclo) throws ClientesException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            String query = "INSERT INTO d_credito "
                  +"SELECT * FROM cf_cartera_db.d_credito WHERE cr_num_cliente=? AND cr_num_solicitud=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en insertCredito : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en insertCredito : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void deleteTablaAmortizacion(int idGrupo, int numCredito) throws ClientesException {
        Connection con = null;
        try {
            con = getCWConnection();
            String query = "DELETE FROM d_tabla_amortizacion WHERE ta_numcliente=? AND ta_numcredito=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, numCredito);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + numCredito + " ]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            Logger.debug("SQLException en deleteTablaAmortizacion : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en deleteTablaAmortizacion : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void insertTablaAmortizacion(int idGrupo, int numCredito) throws ClientesException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            String query = "INSERT INTO d_tabla_amortizacion(TA_NUMCLIENTE, TA_NUMCREDITO, TA_NUMDISPOSICION, TA_TPO_AMORTIZACION, TA_NUMPAGO, TA_FECHA_PAGO, TA_SALDO_INICIAL, TA_ABONO_CAPITAL, TA_SALDO_CAPITAL, TA_CAPITAL_PAGADO, TA_COMISION_INICIAL, TA_IVA_COMISION, TA_INTERES, TA_IVA_INTERES, TA_INTERES_ACUM, TA_IVA_INTERES_ACUM, TA_INTERES_PAGADO, TA_IVA_INTERES_PAGADO, TA_MORA, TA_IVA_MORA, TA_MORA_PAGADO, TA_IVA_MORA_PAGADO, TA_MULTA, TA_IVA_MULTA, TA_MULTA_PAGADO, TA_IVA_MULTA_PAGADO, TA_MONTO_PAGAR, TA_MONTO_PAGADO, TA_STATUS, TA_PAGADO) "
                  +"SELECT * FROM cf_cartera_db.d_tabla_amortizacion WHERE ta_numcliente=? AND ta_numcredito=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, numCredito);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + numCredito + " ]");
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en insertTablaAmortizacion : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en insertTablaAmortizacion : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void deletePagos(String referencia) throws ClientesException {
        deletePagos(referencia, false);
    }
    
    public int deletePagosODS(String referencia) throws ClientesException {
        try {
            deletePagos(referencia, true);
        } catch (ClientesException ex) {
            String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia='"+referencia+"';";
            new IntegranteCicloDAO().generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro pagos cartera liquidacion de la ODS: ", ex);
            myLogger.error(query);
        }
        return -1;
    }
    
    public void deletePagos(String referencia, boolean conexionODS) throws ClientesException {
        Connection con = null;
        try {
            con = (conexionODS)?getODSConnection():getConnection();
            String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + referencia + " ]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            Logger.debug("SQLException en deletePagos : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en deletePagos : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public void insertPagos(String referencia) throws ClientesException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            String query = "INSERT INTO d_pagos_cartera "
                  +"SELECT pc_referencia, pc_monto, pc_fecha_pago, pc_fecha_hora, pc_banco_referencia, pc_sucursal, pc_status, pc_destino, pc_id_contabilidad, pc_enviado, 0, pc_numpagrupal "
                  +"FROM clientes.d_pagos_cartera WHERE pc_referencia=?";
            ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + referencia + " ]");
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en insertPagos : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en insertPagos : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
    
    public String getIdMigracion (int idGrupo, int idCiclo) throws ClientesException {

        Connection conn = null;
        ResultSet rs = null;
        String idMigracion = null;
        try {
            conn = getConnection();
            String query = "SELECT ci_id_migracion FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_numciclo=?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            rs = ps.executeQuery();
            while (rs.next()) {
                idMigracion = rs.getString("ci_id_migracion");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getIdMigracion : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en getIdMigracion : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return idMigracion;
    }
    
    public boolean existeGrupo (int idGrupo, int idCiclo) throws ClientesException {

        Connection conn = null;
        ResultSet rs = null;
        boolean existeGrupo = false;
        try {
            conn = getConnection();
            String query = "SELECT ci_id_migracion FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_numciclo=?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros [ " + idGrupo + "," + idCiclo + " ]");
            rs = ps.executeQuery();
            if (rs.next()) {
                existeGrupo = true;
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en existeGrupo : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Exception en existeGrupo : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return existeGrupo;
    }
    
     public int getOrigenMigracion(int idGrupo, int idCiclo) throws ClientesException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int origen = 0;
        String query = "SELECT ci_origenmigracion FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_numciclo=?";
        
        try {
            con = getConnection();//clientes_Cec
            ps = con.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            Logger.debug("Ejecutando: " + query);
            Logger.debug("Parametros[" + idGrupo + ", " + idCiclo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                origen = res.getInt("ci_origenmigracion");
            }
        }
        catch (SQLException sqle) {
            Logger.debug("SQLException en getOrigenMigracion : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        }
        catch (Exception e) {
            Logger.debug("Exception en getOrigenMigracion : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return origen;
    }
    
}

package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class PagoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(PagoDAO.class);

    public PagoVO[] getFechasPagosCliente(String referencia) {

        //String query = "SELECT PC_REFERENCIA, PC_FECHA_PAGO, PC_MONTO, PC_BANCO_REFERENCIA FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ? UNION SELECT PA_REFERENCIA, PA_FECHA_PAGO, PA_MONTO, PA_BANCO_REFERENCIA FROM D_PAGOS WHERE PA_REFERENCIA = ? ORDER BY 2";
        String query = "SELECT PC_REFERENCIA, PC_FECHA_PAGO, PC_MONTO, PC_BANCO_REFERENCIA FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ? ";
        ArrayList<PagoVO> listaPagos = new ArrayList<PagoVO>();
        int param = 1;
        PagoVO[] arrayPagos = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia);
            //ps.setString(param++, referencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoVO pago = new PagoVO();
                pago.referencia = rs.getString("PC_REFERENCIA");
                pago.monto = rs.getDouble("PC_MONTO");
                pago.fechaPago = rs.getDate("PC_FECHA_PAGO");
                pago.bancoReferencia = rs.getInt("PC_BANCO_REFERENCIA");
                //pago.tipo = rs.getString("pa_tipo");
                //pago.fechaHora = rs.getTimestamp("pa_fecha_hora");
                //pago.enviado = rs.getInt("pa_enviado");
                listaPagos.add(pago);
            }
        } catch (SQLException exc) {
            myLogger.error("getFechasPagosCliente", exc);
        } catch (NamingException exc) {
            myLogger.error("getFechasPagosCliente", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getFechasPagosCliente", exc);
            }
        }
        arrayPagos = new PagoVO[listaPagos.size()];
        for (int i = 0; i < arrayPagos.length; i++) {
            arrayPagos[i] = listaPagos.get(i);
        }
        return arrayPagos;
    }

    public PagoVO[] getPagosByReferencia(String referencia) throws ClientesException {
        ArrayList<PagoVO> arrayListPagos = new ArrayList<PagoVO>();
        PagoVO[] pagos = null;
        int param = 1;
        String query = "SELECT * FROM D_PAGOS WHERE PA_REFERENCIA = ? ORDER BY pa_fecha_pago";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoVO pago = new PagoVO();
                pago.referencia = rs.getString("pa_referencia");
                pago.monto = rs.getDouble("pa_monto");
                pago.fechaPago = rs.getDate("pa_fecha_pago");
                pago.tipo = rs.getString("pa_tipo");
                pago.fechaHora = rs.getTimestamp("pa_fecha_hora");
                pago.enviado = rs.getInt("pa_enviado");
                pago.bancoReferencia = rs.getInt("pa_banco_referencia");
                arrayListPagos.add(pago);
            }
        } catch (SQLException exc) {
            myLogger.error("getPagosByReferencia", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getPagosByReferencia", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagosByReferencia", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }
        pagos = new PagoVO[arrayListPagos.size()];
        for (int i = 0; i < pagos.length; i++) {
            pagos[i] = arrayListPagos.get(i);
        }
        return pagos;
    }

    public ArrayList<PagoVO> getArrPagosByReferencia(String referencia) throws ClientesException {

        ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
        String query = "SELECT * FROM d_pagos_cartera WHERE pc_referencia= ?;";
        Connection con = null;
        ResultSet res = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            res = ps.executeQuery();
            while (res.next()) {
                arrPagos.add(new PagoVO(res.getDouble("pc_monto"), res.getDate("pc_fecha_pago"), res.getInt("pc_enviado"), res.getInt("pc_banco_referencia"), res.getInt("pc_numcuenta")));
            }
        } catch (SQLException exc) {
            myLogger.error("getArrPagosByReferencia", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getArrPagosByReferencia", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getArrPagosByReferencia", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return arrPagos;
    }

    public boolean updatePagosCartera(PagoVO pagos, String referencia) {
        int param = 1, regModif = 0;
        //String query = "UPDATE D_PAGOS_CARTERA SET PC_ENVIADO = ? WHERE PC_REFERENCIA = ? AND PC_FECHA_PAGO = ?;";
        String query = "UPDATE d_pagos_cartera SET pc_enviado=? WHERE pc_numregistro=?;";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
//			ps.setDouble(param++, pagos.monto);
//			ps.setTimestamp(param++, pagos.fechaHora);
            ps.setInt(param++, pagos.enviado);
            /*ps.setString(param++, referencia);
            ps.setDate(param++, pagos.fechaPago);*/
            ps.setInt(param++, pagos.numRegistro);

            regModif = ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("updatePagosCartera", exc);
        } catch (NamingException exc) {
            myLogger.error("updatePagosCartera", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("updatePagosCartera", exc);
            }
        }
        if (regModif > 0) {
            return true;
        }
        return false;
    }

    public void updatePagosCartera(int envio, String referencia) {

        String query = "UPDATE D_PAGOS_CARTERA SET PC_ENVIADO = ? WHERE PC_REFERENCIA = ?;";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, envio);
            ps.setString(2, referencia);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("updatePagosCartera", exc);
        } catch (NamingException exc) {
            myLogger.error("updatePagosCartera", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("updatePagosCartera", exc);
            }
        }
    }

    public void updatePagosCarteraCierre(PagoVO pagos) {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE d_pagos_cartera SET pc_enviado=1 WHERE pc_referencia= ? AND pc_fecha_pago= ? AND pc_numregistro= ?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, pagos.getReferencia());
            ps.setDate(2, pagos.getFechaPago());
            ps.setInt(3, pagos.getNumRegistro());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + pagos.getReferencia() + ", " + pagos.getFechaPago() + ", " + pagos.getNumRegistro() + "]");
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("updatePagosCarteraCierre", exc);
        } catch (NamingException exc) {
            myLogger.error("updatePagosCarteraCierre", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("updatePagosCarteraCierre", exc);
            }
        }
    }

    public void insertPago(PagoVO pago) throws ClientesException {
        int param = 1;
        String query = "INSERT INTO D_PAGOS(PA_REFERENCIA, PA_MONTO, PA_FECHA_PAGO, PA_TIPO, PA_FECHA_HORA, PA_ENVIADO,PA_STATUS,PA_BANCO_REFERENCIA, PA_SUCURSAL, PA_ID_CONTABILIDAD, pa_numcuenta)";
        query += "VALUES(?,?,?,?,CURRENT_TIMESTAMP,?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        try {
            myLogger.debug("Referencia::" + pago.referencia);
            myLogger.debug("Monto::" + pago.monto);
            myLogger.debug("FechaPago::" + pago.fechaPago);
            myLogger.debug("Tipo::" + pago.tipo);
            myLogger.debug("Enviado::" + pago.enviado);
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.monto);
            ps.setDate(param++, pago.fechaPago);
            ps.setString(param++, pago.tipo);
            //ps.setDate(param++, pago.fechaHora);
            ps.setInt(param++, pago.enviado);
            ps.setInt(param++, pago.status);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setString(param++, pago.sucursal);
            ps.setInt(param++, pago.idContable);
            ps.setInt(param++, pago.numCuenta);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("insertPago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("insertPago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("insertPago", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public boolean insertPago(PagoVO pago, Connection con) throws ClientesException {
        
        boolean listo = true;
        int param = 1;
        PreparedStatement ps = null;
        String query = "INSERT INTO D_PAGOS(PA_REFERENCIA, PA_MONTO, PA_FECHA_PAGO, PA_TIPO, PA_FECHA_HORA, PA_ENVIADO,PA_STATUS,PA_BANCO_REFERENCIA, PA_SUCURSAL, PA_ID_CONTABILIDAD, pa_numcuenta)"
                + "VALUES(?,?,?,?,CURRENT_TIMESTAMP,?, ?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.monto);
            ps.setDate(param++, pago.fechaPago);
            ps.setString(param++, pago.tipo);
            ps.setInt(param++, pago.enviado);
            ps.setInt(param++, pago.status);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setString(param++, pago.sucursal);
            ps.setInt(param++, pago.idContable);
            ps.setInt(param++, pago.numCuenta);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("Error en insertPago", exc);
            return listo = false;
        }
        return listo;
    }
            public void insertPagoCarteraLayout(PagoVO pagocartera,Connection con) throws ClientesException {
        int param = 1;
        String query = "INSERT INTO D_PAGOS_CARTERA(PC_REFERENCIA, PC_MONTO, PC_FECHA_PAGO, PC_FECHA_HORA, PC_BANCO_REFERENCIA, PC_SUCURSAL,PC_STATUS, PC_DESTINO, PC_ID_CONTABILIDAD, pc_enviado, pc_numcuenta)";
        query += "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(param++, pagocartera.referencia);
            ps.setDouble(param++, pagocartera.monto);
            ps.setDate(param++, pagocartera.fechaPago);
            ps.setTimestamp(param++, pagocartera.fechaHora);
            ps.setInt(param++, pagocartera.bancoReferencia);
            ps.setString(param++, pagocartera.sucursal);
            ps.setInt(param++, pagocartera.status);
            ps.setInt(param++, pagocartera.destino);
            ps.setInt(param++, pagocartera.idContable);
            ps.setInt(param++, pagocartera.enviado);
            ps.setInt(param++, pagocartera.numCuenta);
            ps.executeUpdate();
            myLogger.debug("Pago Cartera: "+ps.toString());
        } catch (SQLException exc) {
            myLogger.error("insertPagoCartera", exc);
            throw new ClientesException(exc.getMessage());
        } 
    }

    public void insertPagoCartera(PagoVO pagocartera) throws ClientesException {
        int param = 1;
        String query = "INSERT INTO D_PAGOS_CARTERA(PC_REFERENCIA, PC_MONTO, PC_FECHA_PAGO, PC_FECHA_HORA, PC_BANCO_REFERENCIA, PC_SUCURSAL,PC_STATUS, PC_DESTINO, PC_ID_CONTABILIDAD, pc_enviado, pc_numcuenta)";
        query += "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, pagocartera.referencia);
            ps.setDouble(param++, pagocartera.monto);
            ps.setDate(param++, pagocartera.fechaPago);
            ps.setTimestamp(param++, pagocartera.fechaHora);
            ps.setInt(param++, pagocartera.bancoReferencia);
            ps.setString(param++, pagocartera.sucursal);
            ps.setInt(param++, pagocartera.status);
            ps.setInt(param++, pagocartera.destino);
            ps.setInt(param++, pagocartera.idContable);
            ps.setInt(param++, pagocartera.enviado);
            ps.setInt(param++, pagocartera.numCuenta);
            ps.executeUpdate();
            myLogger.debug("Pago Cartera: "+ps.toString());
        } catch (SQLException exc) {
            myLogger.error("insertPagoCartera", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("insertPagoCartera", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("insertPagoCartera", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public int insertPagoCartera(PagoVO pagocartera, Connection con) throws ClientesException {
        
        int param = 1;
        String query = "INSERT INTO D_PAGOS_CARTERA(PC_REFERENCIA, PC_MONTO, PC_FECHA_PAGO, PC_FECHA_HORA, PC_BANCO_REFERENCIA, PC_SUCURSAL,PC_STATUS, PC_DESTINO, PC_ID_CONTABILIDAD, pc_enviado, pc_numcuenta)";
        query += "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        ResultSet res = null;
        int numPago = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setString(param++, pagocartera.referencia);
            ps.setDouble(param++, pagocartera.monto);
            ps.setDate(param++, pagocartera.fechaPago);
            ps.setTimestamp(param++, pagocartera.fechaHora);
            ps.setInt(param++, pagocartera.bancoReferencia);
            ps.setString(param++, pagocartera.sucursal);
            ps.setInt(param++, pagocartera.status);
            ps.setInt(param++, pagocartera.destino);
            ps.setInt(param++, pagocartera.idContable);
            ps.setInt(param++, pagocartera.enviado);
            ps.setInt(param++, pagocartera.numCuenta);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
            res = ps.getGeneratedKeys();
            if (res.next())
                numPago = res.getInt(1);
        } catch (SQLException exc) {
            myLogger.error("insertPagoCartera", exc);
        } finally {
            try {
                if(ps != null)
                    ps.close();
                if(res != null)
                    res.close();
            } catch (SQLException exc) {
                myLogger.error("insertPagoCartera", exc);
            }
        }
        return numPago;
    }

    public void insertPagoCartera(PagoVO pagocartera, ReferenciaGeneralVO ref) throws ClientesException {
        int param = 1;
        String query = "INSERT INTO D_PAGOS_CARTERA(PC_REFERENCIA, PC_MONTO, PC_FECHA_PAGO, PC_FECHA_HORA, PC_BANCO_REFERENCIA, PC_SUCURSAL,PC_STATUS, PC_DESTINO, PC_ID_CONTABILIDAD,pc_numpagrupal)";
        query += "VALUES(?,?,?,?,?,?,?,?,?,?)";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, pagocartera.referencia);
            ps.setDouble(param++, pagocartera.monto);
            ps.setDate(param++, pagocartera.fechaPago);
            ps.setTimestamp(param++, pagocartera.fechaHora);
            ps.setInt(param++, pagocartera.bancoReferencia);
            ps.setString(param++, pagocartera.sucursal);
            ps.setInt(param++, pagocartera.status);
            ps.setInt(param++, pagocartera.destino);
            ps.setInt(param++, pagocartera.idContable);
            ps.setInt(param++, new PagoGrupalDAO().getNumPagoGrupalActual(ref.numcliente, ref.numSolicitud));
            ps.executeUpdate();
            //myLogger.debug("Pago Cartera: "+pagocartera.toString());
        } catch (SQLException exc) {
            myLogger.error("insertPagoCartera", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("insertPagoCartera", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("insertPagoCartera", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    /**
     * Este método obtiene los últimos pagos pertenecientes a T24 (tipo 'T').
     * Para ello se filtran los pagos tomando como referencia la fecha de pago
     * mós reciente. Los pagos son seleccionados siempre que no hayan sido
     * enviados previamente (PA_ENVIADO = 0);
     *
     * @return El arreglo de los pagos T24 efectuados la fecha previa.
     */
    public PagoVO[] getNoEnviados() {
        PagoVO[] pagos = null;
        /*String query = "SELECT PC_FECHA_HORA, PC_FECHA_PAGO, PC_MONTO, PC_REFERENCIA, PC_BANCO_REFERENCIA FROM D_PAGOS_CARTERA "
                + "LEFT JOIN D_SALDOS ON (IB_REFERENCIA = PC_REFERENCIA AND IB_FECHA_DESEMBOLSO > '2010-09-26')"
                + "WHERE PC_ENVIADO = 0 ";*/
        String query = "SELECT pc_referencia,pc_monto,pc_fecha_pago,pc_fecha_hora,pc_banco_referencia,pc_numregistro,pc_numcuenta FROM d_pagos_cartera WHERE pc_enviado=0;";
        Connection cn = null;
        ArrayList<PagoVO> arrayPago = new ArrayList<PagoVO>();
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoVO pago = new PagoVO();
                //pago.enviado = rs.getInt("pa_enviado");
                //pago.fechaHora = rs.getTimestamp("pa_fecha_hora");
                pago.fechaHora = rs.getTimestamp("pc_fecha_hora");
                pago.fechaPago = rs.getDate("pc_fecha_pago");
                pago.monto = rs.getDouble("pc_monto");
                pago.referencia = rs.getString("pc_referencia");
                pago.bancoReferencia = rs.getInt("pc_banco_referencia");
                pago.numRegistro = rs.getInt("pc_numregistro");
                pago.numCuenta = rs.getInt("pc_numcuenta");
                //pago.tipo = rs.getString("pa_tipo");
                arrayPago.add(pago);
            }
            pagos = new PagoVO[arrayPago.size()];
            for (int i = 0; i < pagos.length; i++) {
                pagos[i] = arrayPago.get(i);
            }
        } catch (SQLException exc) {
            myLogger.error("getNoEnviados", exc);
        } catch (NamingException exc) {
            myLogger.error("getNoEnviados", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNoEnviados", exc);
            }
        }
        return pagos;
    }

    public ArrayList<PagoVO> getNoEnviadosCierre() {

        ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
        Connection con = null;
        Statement st = null;
        ResultSet res = null;
        String query = "SELECT pc_referencia,pc_monto,pc_fecha_pago,pc_numregistro FROM d_pagos_cartera WHERE pc_enviado = 0";

        try {
            con = getConnection();
            st = con.createStatement();
            myLogger.debug("Ejecutando : " + query);
            res = st.executeQuery(query);
            while (res.next()) {
                arrPagos.add(new PagoVO(res.getString("pc_referencia"), res.getDouble("pc_monto"), res.getDate("pc_fecha_pago"), res.getInt("pc_numregistro")));
            }
        } catch (SQLException exc) {
            myLogger.error("getNoEnviadosCierre", exc);
        } catch (NamingException exc) {
            myLogger.error("getNoEnviadosCierre", exc);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNoEnviadosCierre", exc);
            }
        }
        return arrPagos;
    }

    public PagoVO[] getNoEnviadosMigracion() {
        PagoVO[] pagos = null;
        String query = "SELECT PC_FECHA_HORA, PC_FECHA_PAGO, PC_MONTO, PC_REFERENCIA FROM D_PAGOS_CARTERA "
                + "LEFT JOIN D_SALDOS ON (IB_REFERENCIA = PC_REFERENCIA AND IB_FECHA_DESEMBOLSO < '2010-09-26')"
                + "WHERE PC_ENVIADO = 0 ";
        Connection cn = null;
        int param = 1;
        ArrayList<PagoVO> arrayPago = new ArrayList<PagoVO>();
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoVO pago = new PagoVO();
                //pago.enviado = rs.getInt("pa_enviado");
                //pago.fechaHora = rs.getTimestamp("pa_fecha_hora");
                pago.fechaHora = rs.getTimestamp("pc_fecha_hora");
                pago.fechaPago = rs.getDate("pc_fecha_pago");
                pago.monto = rs.getDouble("pc_monto");
                pago.referencia = rs.getString("pc_referencia");
                //pago.tipo = rs.getString("pa_tipo");
                arrayPago.add(pago);
            }
            pagos = new PagoVO[arrayPago.size()];
            for (int i = 0; i < pagos.length; i++) {
                pagos[i] = arrayPago.get(i);
            }
        } catch (SQLException exc) {
            myLogger.error("getNoEnviadosMigracion", exc);
        } catch (NamingException exc) {
            myLogger.error("getNoEnviadosMigracion", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNoEnviadosMigracion", exc);
            }
        }
        return pagos;
    }

    public Date getUltimoPago(String referencia) throws ClientesException {
        Date fecha = new Date();
        int param = 1;
        String query = "SELECT MAX(PC_FECHA_PAGO) AS FECHA FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ?";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fecha = rs.getDate("FECHA");
            }
        } catch (SQLException exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getUltimoPago", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return fecha;
    }

    public int getNumeroPagos(String referencia) throws ClientesException {
        int param = 1;
        int num_pagos = 0;
        String query = "SELECT COUNT(PC_FECHA_PAGO) AS NUM_PAGOS FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ?";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                num_pagos = rs.getInt("NUM_PAGOS");
            }
        } catch (SQLException exc) {
            myLogger.error("getNumeroPagos", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getNumeroPagos", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getNumeroPagos", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return num_pagos;
    }

    public double getTotalPagado(String referencia) throws ClientesException {
        int param = 1;
        double total_pagado = 0;
        String query = "SELECT SUM(PC_MONTO) AS TOTAL_PAGADO FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA = ?";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, referencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total_pagado = rs.getDouble("TOTAL_PAGADO");
            }
        } catch (SQLException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getTotalPagado", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getTotalPagado", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return total_pagado;
    }

    public ArrayList<PagoVO> getPagoVO(int archivoprocesado) throws ClientesException {
        PagoVO pago = null;
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        //PagoCarteraWINVO elementos[] = null;
        String query = "SELECT * "
                + "FROM D_PAGOS_CARTERA "
                + "WHERE PC_ID_CONTABILIDAD = ? "
                + "AND PC_DESTINO != 3";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, archivoprocesado);
            //ps.setInt(2,bancoreferencia);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + archivoprocesado + " ]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pago = new PagoVO();
                pago.referencia = rs.getString("PC_REFERENCIA");
                pago.monto = rs.getDouble("PC_MONTO");
                pago.fechaPago = rs.getDate("PC_FECHA_PAGO");
                pago.fechaHora = rs.getTimestamp("PC_FECHA_HORA");
                pago.bancoReferencia = rs.getInt("PC_BANCO_REFERENCIA");
                pago.sucursal = rs.getString("PC_SUCURSAL");
                pago.status = rs.getInt("PC_STATUS");
                array.add(pago);
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagoVO", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagoVO", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagoVO", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        //elementos = new PagoCarteraWINVO[array.size()];
        //for(int i=0;i<elementos.length; i++) elementos[i] = (PagoCarteraWINVO)array.get(i);		
        return array;
    }
    
    public int getPagoVO(PagoVO pagos, Connection con) throws ClientesException {
        PagoVO pago = null;
        int resultado = 0;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * "
                + "FROM D_PAGOS_CARTERA "
                + "WHERE PC_REFERENCIA = ? "
                + "AND PC_MONTO = ? AND PC_FECHA_PAGO = ? and PC_BANCO_REFERENCIA = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, pagos.referencia);
            ps.setDouble(2, pagos.monto);
            ps.setDate(3, pagos.fechaPago);
            ps.setInt(4, pagos.bancoReferencia);
            myLogger.debug("Ejecutando "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                pago = new PagoVO();
                pago.referencia = rs.getString("PC_REFERENCIA");
                pago.monto = rs.getDouble("PC_MONTO");
                pago.fechaPago = rs.getDate("PC_FECHA_PAGO");
                pago.fechaHora = rs.getTimestamp("PC_FECHA_HORA");
                pago.bancoReferencia = rs.getInt("PC_BANCO_REFERENCIA");
                pago.sucursal = rs.getString("PC_SUCURSAL");
                pago.status = rs.getInt("PC_STATUS");
                array.add(pago);
            }
            if (array.size() > 0) {
                resultado = 1;
            }

        } catch (SQLException sqle) {
            myLogger.error("getPagoVO", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagoVO", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                myLogger.error("getPagoVO", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return resultado;
    }

    public int getPagoVO(PagoVO pagos) throws ClientesException {
        PagoVO pago = null;
        int resultado = 0;
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        //PagoCarteraWINVO elementos[] = null;
        String query = "SELECT * "
                + "FROM D_PAGOS_CARTERA "
                + "WHERE PC_REFERENCIA = ? "
                + "AND PC_MONTO = ? AND PC_FECHA_PAGO = ? and PC_BANCO_REFERENCIA = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, pagos.referencia);
            ps.setDouble(2, pagos.monto);
            ps.setDate(3, pagos.fechaPago);
            ps.setInt(4, pagos.bancoReferencia);
            myLogger.debug("Ejecutando "+ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pago = new PagoVO();
                pago.referencia = rs.getString("PC_REFERENCIA");
                pago.monto = rs.getDouble("PC_MONTO");
                pago.fechaPago = rs.getDate("PC_FECHA_PAGO");
                pago.fechaHora = rs.getTimestamp("PC_FECHA_HORA");
                pago.bancoReferencia = rs.getInt("PC_BANCO_REFERENCIA");
                pago.sucursal = rs.getString("PC_SUCURSAL");
                pago.status = rs.getInt("PC_STATUS");
                array.add(pago);
            }
            if (array.size() > 0) {
                resultado = 1;
            }

        } catch (SQLException sqle) {
            myLogger.error("getPagoVO", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagoVO", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagoVO", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        //elementos = new PagoCarteraWINVO[array.size()];
        //for(int i=0;i<elementos.length; i++) elementos[i] = (PagoCarteraWINVO)array.get(i);		
        return resultado;
    }
    public int getPagoVOProcesaPago(PagoVO pagos,Connection cn) throws ClientesException {
        PagoVO pago = null;
        int resultado = 0;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        //PagoCarteraWINVO elementos[] = null;
        String query = "SELECT * "
                + "FROM D_PAGOS_CARTERA "
                + "WHERE PC_REFERENCIA = ? "
                + "AND PC_MONTO = ? AND PC_FECHA_PAGO = ? and PC_BANCO_REFERENCIA = ?";
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, pagos.referencia);
            ps.setDouble(2, pagos.monto);
            ps.setDate(3, pagos.fechaPago);
            ps.setInt(4, pagos.bancoReferencia);
            myLogger.debug("Ejecutando "+ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pago = new PagoVO();
                pago.referencia = rs.getString("PC_REFERENCIA");
                pago.monto = rs.getDouble("PC_MONTO");
                pago.fechaPago = rs.getDate("PC_FECHA_PAGO");
                pago.fechaHora = rs.getTimestamp("PC_FECHA_HORA");
                pago.bancoReferencia = rs.getInt("PC_BANCO_REFERENCIA");
                pago.sucursal = rs.getString("PC_SUCURSAL");
                pago.status = rs.getInt("PC_STATUS");
                array.add(pago);
            }
            if (array.size() > 0) {
                resultado = 1;
            }

        } catch (SQLException sqle) {
            myLogger.error("getPagoVO", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagoVO", e);
            throw new ClientesException(e.getMessage());
        }
        //elementos = new PagoCarteraWINVO[array.size()];
        //for(int i=0;i<elementos.length; i++) elementos[i] = (PagoCarteraWINVO)array.get(i);		
        return resultado;
    }
    
    public ArrayList<PagoVO> getPagosIBS() throws ClientesException {
        PagoVO pago = null;
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        String query = "SELECT * FROM D_PAGOS WHERE DATE(PA_FECHA_HORA) = CURDATE()";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pago = new PagoVO();
                pago.referencia = rs.getString("PA_REFERENCIA");
                pago.monto = rs.getDouble("PA_MONTO");
                pago.fechaPago = rs.getDate("PA_FECHA_PAGO");
                pago.fechaHora = rs.getTimestamp("PA_FECHA_HORA");
                pago.bancoReferencia = rs.getInt("PA_BANCO_REFERENCIA");
                pago.sucursal = rs.getString("PA_SUCURSAL");
                pago.status = rs.getInt("PA_STATUS");
                array.add(pago);
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagosIBS", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagosIBS", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return array;
    }

    public void actualizaPagosEnviados() throws ClientesException {

        String query = "UPDATE RESPALDO.D_PAGOS_CARTERA PAC, CLIENTES.D_PAGOS_CARTERA PAN SET PAN.PC_ENVIADO = 1 WHERE PAC.PC_NUMREGISTRO = PAN.PC_NUMREGISTRO AND PAC.PC_ENVIADO = 1 AND PAN.PC_ENVIADO = 0";
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            //prepareStatement(query);
            int res = stmt.executeUpdate(query);
            myLogger.debug("Pagos actualizados: " + res);
        } catch (SQLException exc) {
            myLogger.error("actualizaPagosEnviados", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("actualizaPagosEnviados", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("actualizaPagosEnviados", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public void respaldaPagosEnviados() throws ClientesException {

        String query1 = "TRUNCATE RESPALDO.D_PAGOS_CARTERA";
        String query2 = "INSERT INTO RESPALDO.D_PAGOS_CARTERA SELECT * FROM CLIENTES.D_PAGOS_CARTERA";
        Connection cn = null;
        try {
            cn = getConnection();
            Statement stmt = cn.createStatement();
            //prepareStatement(query);
            stmt.executeUpdate(query1);
            int res = stmt.executeUpdate(query2);
            myLogger.debug("Pagos respaldados: " + res);
        } catch (SQLException exc) {
            myLogger.error("respaldaPagosEnviados", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("respaldaPagosEnviados", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("respaldaPagosEnviados", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public void deletePagoCiclo(String referencia) throws ClientesException {
        deletePagoCiclo(referencia, false);
    }
    
    public int deletePagoCicloODS(String referencia) throws ClientesException {
        try {
            deletePagoCiclo(referencia, true);
        } catch (ClientesException ex) {
            String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia='"+referencia+"';";
            new IntegranteCicloDAO().generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro pagos cartera ciclo de la ODS: ", ex);
            myLogger.error(query);
        }
        return -1;
    }
    
    public void deletePagoCiclo(String referencia, boolean conexionODS) throws ClientesException {
        String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia=?";
        Connection cn = null;
        try {
            cn = (conexionODS)?getODSConnection():getConnection();
            PreparedStatement pstm = cn.prepareStatement(query);
            pstm.setString(1, referencia);
            myLogger.debug("Ejecutando " + query);
            pstm.execute();
        } catch (SQLException exc) {
            myLogger.error("deletePagoCiclo", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("deletePagoCiclo", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("deletePagoCiclo", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public void deletePago(PagoVO pago) throws ClientesException, SQLException {
        deletePago(pago, false);
    }
    
    public int deletePagoODS(PagoVO pago) {
        try {
            deletePago(pago, true);
        } catch (ClientesException ex) {
            String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia='"+pago.getReferencia()+"' AND pc_monto="+pago.getMonto()+" AND pc_fecha_pago='"+pago.getFechaPago()+"' LIMIT 1;";
            new IntegranteCicloDAO().generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro pagos cartera de la ODS: ", ex);
            myLogger.error(query);
        } catch (SQLException ex) {
            String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia='"+pago.getReferencia()+"' AND pc_monto="+pago.getMonto()+" AND pc_fecha_pago='"+pago.getFechaPago()+"' LIMIT 1;";
            new IntegranteCicloDAO().generarArchivoDeleteODS(query);
            myLogger.error("Error al eliminar registro pagos cartera de la ODS2: ", ex);
            myLogger.error(query);
        }
        return -1;
    }
    
    public void deletePago(PagoVO pago, boolean conexionODS) throws ClientesException, SQLException {

        String query = "DELETE FROM d_pagos_cartera WHERE pc_referencia=? AND pc_monto=? AND pc_fecha_pago=? LIMIT 1;";
        Connection con = null;
        try {
            con = (conexionODS)?getODSConnection():getConnection();
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setString(1, pago.getReferencia());
            pstm.setDouble(2, pago.getMonto());
            pstm.setDate(3, pago.getFechaPago());
            myLogger.debug("Ejecutando " + pstm);
            pstm.execute();
            con.close();
        } catch (SQLException exc) {
            myLogger.error("deletePago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("deletePago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException exc) {
                    myLogger.error("deletePago", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }
    
    public double getMontoUltimoPago(String referencia) throws ClientesException {
        
        double monto = 0;
        String query = "SELECT pc_monto FROM D_PAGOS_CARTERA WHERE PC_REFERENCIA=? ORDER BY pc_fecha_hora DESC,pc_numregistro DESC LIMIT 1;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            res = ps.executeQuery();
            if(res.next()) {
                monto = res.getDouble("pc_monto");
            }
        } catch (SQLException exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getUltimoPago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException exc) {
                myLogger.error("getUltimoPago", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return monto;
    }
}

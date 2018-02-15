package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoExcedenteVO;
import org.apache.log4j.Logger;

public class PagosExcedentesDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(PagosExcedentesDAO.class);

    public void addPagoExcedente(PagoExcedenteVO pago) throws ClientesException {

        String query = "INSERT INTO D_PAGOS_EXCEDENTES(PE_FECHA_MOV,PE_REFERENCIA,PE_MONTO_EXCEDENTE,PE_NUMCLIENTE,"
                + "PE_NUMCUENTA_CONTABLE,PE_FECHA_HORA,PE_BANCO_REFERENCIA,PE_ID_CONTABILIDAD"
                + ") VALUES (?, ?, ?, ?,?, CURRENT_TIMESTAMP,?,?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(param++, pago.fechaMovimiento);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.montoExcedente);
            ps.setInt(param++, pago.numCliente);
            ps.setString(param++, pago.numCuentaContable);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setInt(param++, pago.idContabilidad);
            myLogger.debug("Ejecutando " + ps);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("Error dentro addPagoExcedente", sqle);
        } catch (Exception e) {
            myLogger.error("Error dentro addPagoExcedente", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public boolean addPagoExcedente(PagoExcedenteVO pago, Connection con) throws ClientesException {

        String query = "INSERT INTO D_PAGOS_EXCEDENTES(PE_FECHA_MOV,PE_REFERENCIA,PE_MONTO_EXCEDENTE,PE_NUMCLIENTE,"
                + "PE_NUMCUENTA_CONTABLE,PE_FECHA_HORA,PE_BANCO_REFERENCIA,PE_ID_CONTABILIDAD) VALUES (?, ?, ?, ?,?, CURRENT_TIMESTAMP,?,?)";
        int param = 1;
        boolean listo = true;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, pago.fechaMovimiento);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.montoExcedente);
            ps.setInt(param++, pago.numCliente);
            ps.setString(param++, pago.numCuentaContable);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setInt(param++, pago.idContabilidad);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("Erro dentro addPagoExcedente", sqle);
        } catch (Exception e) {
            myLogger.error("Error dentro addPagoExcedente", e);
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

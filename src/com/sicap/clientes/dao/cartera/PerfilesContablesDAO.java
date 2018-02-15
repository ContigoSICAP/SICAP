package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.cartera.PerfilesContablesVO;

public class PerfilesContablesDAO extends DAOMaster {

    public PerfilesContablesVO[] getElementosTrans(String tipoTransaccion, int fondeador, int tipoOperacion, int centroCostos) throws ClientesException {
        PerfilesContablesVO perfiles = null;
        Connection cn = null;
        ArrayList<PerfilesContablesVO> array = new ArrayList<PerfilesContablesVO>();
        PerfilesContablesVO elementos[] = null;
        centroCostos = 1;

        String query = "SELECT pc_tipo_transaccion,pc_fondeador,pc_tipo_operacion,pc_codigo_rubro,pc_status,pc_no_cuenta,pc_tipo_movimiento,pc_centro_costos_dest,pc_origen,pc_no_cuenta_sap "
                + " FROM C_PERFILES_CONTABLES WHERE PC_TIPO_TRANSACCION = ? AND PC_FONDEADOR = ?  AND PC_TIPO_OPERACION = ?  AND PC_CENTRO_COSTOS = ?  ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, tipoTransaccion);
            ps.setInt(2, fondeador);
            ps.setInt(3, tipoOperacion);
            ps.setInt(4, centroCostos);
            Logger.debug("Ejecutando = " + query);
            Logger.debug("Parametros = [" + tipoTransaccion + "," + fondeador + "," + tipoOperacion + "," + centroCostos + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                perfiles = new PerfilesContablesVO();
                perfiles.tipoTransaccion = tipoTransaccion;
                perfiles.fondeador = fondeador;
                perfiles.tipoOperacion = tipoOperacion;
                perfiles.codigoRubro = rs.getString("PC_CODIGO_RUBRO");
                perfiles.statusRubro = rs.getString("PC_STATUS");
                perfiles.numCuenta = rs.getString("PC_NO_CUENTA");
                perfiles.tipoMovimiento = rs.getString("PC_TIPO_MOVIMIENTO");
                perfiles.centroCostosDest = rs.getInt("PC_CENTRO_COSTOS_DEST");
                perfiles.origen = rs.getInt("PC_ORIGEN");
                perfiles.numCuentaSap = rs.getString("pc_no_cuenta_sap");
                array.add(perfiles);
            }
            if (array.size() > 0) {
                elementos = new PerfilesContablesVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (PerfilesContablesVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getEventos : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getEventos : " + e.getMessage());
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

        return elementos;
    }

}

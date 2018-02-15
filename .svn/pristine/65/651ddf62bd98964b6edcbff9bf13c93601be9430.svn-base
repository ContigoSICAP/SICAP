package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.InformacionFinancieraVO;
import org.apache.log4j.Logger;

public class InformacionFinancieraDAO extends DAOMaster {

    private static Logger logger = Logger.getLogger(InformacionFinancieraDAO.class);

//SELECT IF_NUMCLIENTE, IF_NUMSOLICITUD, IF_VENTAS, IF_COSTO_VENTAS, IF_UTILIDAD_BRUTA, IF_GASTOS_OPERACION, IF_UTILIDAD_NEGOCIO, IF_OTROS_INGRESOS_FAMILIA, IF_GASTOS_FAMILIA, IF_UTILIDAD_NETA_FAMILIA, IF_UTILIDAD_UNIDAD_FAMILIA, IF_EFECTIVO_NEGOCIO, IF_CUENTASCORRIENTES_AHORROS, IF_CUENTAS_COBRAR, IF_INVENTARIOS, IF_ACTIVO_CORRIENTE, IF_INMUEBLES, IF_MAQUINARIA_EQUIPO, IF_VEHICULOS, IF_ACTIVO_FIJO, IF_OTROS_ACTIVOS, IF_TOTAL_OTROS_ACTIVOS, IF_TOTAL_ACTIVO, IF_PASIVO_CORTO_PLAZO, IF_PASIVO_LARGO_PLAZO, IF_TOTAL_PASIVO, IF_CAPITAL_APORTADO, IF_UTILIDAD, IF_TOTAL_CAPITAL_CONTABLE, IF_TOTAL_PASIVO_CAPITAL_CONTABLE, IF_MARGEN_UTILIDAD_OPER, IF_MARGEN_UTILIDAD_NETA, IF_INDICE_LIQUIDEZ, IF_ROTACION_INVENTARIOS, IF_ROTACION_CAPITAL_TRABAJO_SOL, IF_ROTACION_CUENTAS_COBRAR, IF_ROTACION_CUENTAS_PAGAR, IF_ENDEUDAMIENTO_TOTAL, IF_ENDEUDAMIENTO_FUTURO_TOTAL, IF_CAPACIDAD_PAGO, IF_DIAS_VENTA, IF_FECHA_CAPTURA FROM D_INFORMACION_FINANCIERA
    public InformacionFinancieraVO getInformacionFinanciera(int idCliente, int idSolicitud) throws ClientesException {
        InformacionFinancieraVO informacion = null;
        Connection cn = null;
        String query = "SELECT IF_NUMCLIENTE, IF_NUMSOLICITUD, IF_VENTAS, IF_COSTO_VENTAS, IF_UTILIDAD_BRUTA, "
                + "IF_GASTOS_OPERACION, IF_UTILIDAD_NEGOCIO, IF_OTROS_INGRESOS_FAMILIA, IF_GASTOS_FAMILIA, "
                + "IF_UTILIDAD_NETA_FAMILIA, IF_UTILIDAD_UNIDAD_FAMILIA, IF_EFECTIVO_NEGOCIO, IF_CUENTASCORRIENTES_AHORROS, "
                + "IF_CUENTAS_COBRAR, IF_INVENTARIOS, IF_ACTIVO_CORRIENTE, IF_INMUEBLES, IF_MAQUINARIA_EQUIPO, "
                + "IF_VEHICULOS, IF_ACTIVO_FIJO, IF_OTROS_ACTIVOS, IF_TOTAL_OTROS_ACTIVOS, IF_TOTAL_ACTIVO, "
                + "IF_PASIVO_CORTO_PLAZO, IF_PASIVO_LARGO_PLAZO, IF_TOTAL_PASIVO, IF_CAPITAL_APORTADO, "
                + "IF_UTILIDAD, IF_TOTAL_CAPITAL_CONTABLE, IF_TOTAL_PASIVO_CAPITAL_CONTABLE, IF_MARGEN_UTILIDAD_OPER, "
                + "IF_MARGEN_UTILIDAD_NETA, IF_INDICE_LIQUIDEZ, IF_ROTACION_INVENTARIOS, IF_ROTACION_CAPITAL_TRABAJO_SOL, "
                + "IF_ROTACION_CUENTAS_COBRAR, IF_ROTACION_CUENTAS_PAGAR, IF_ENDEUDAMIENTO_TOTAL, "
                + "IF_ENDEUDAMIENTO_FUTURO_TOTAL, IF_CAPACIDAD_PAGO, IF_DIAS_VENTA, IF_FECHA_CAPTURA FROM D_INFORMACION_FINANCIERA "
                + "WHERE IF_NUMCLIENTE = ? AND IF_NUMSOLICITUD = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            logger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                informacion = new InformacionFinancieraVO();
                informacion.idCliente = idCliente;
                informacion.idSolicitud = idSolicitud;
                informacion.ventas = rs.getDouble("IF_VENTAS");
                informacion.costoVentas = rs.getDouble("IF_COSTO_VENTAS");
                informacion.utilidadBruta = rs.getDouble("IF_UTILIDAD_BRUTA");
                informacion.gastosOperacion = rs.getDouble("IF_GASTOS_OPERACION");
                informacion.utilidadNegocio = rs.getDouble("IF_UTILIDAD_NEGOCIO");
                informacion.otrosIngresosFamilia = rs.getDouble("IF_OTROS_INGRESOS_FAMILIA");
                informacion.gastosFamilia = rs.getDouble("IF_GASTOS_FAMILIA");
                informacion.utilidadNetaFamilia = rs.getDouble("IF_UTILIDAD_NETA_FAMILIA");
                informacion.utilidadUnidadFamilia = rs.getDouble("IF_UTILIDAD_UNIDAD_FAMILIA");
                informacion.efectivoNegocio = rs.getDouble("IF_EFECTIVO_NEGOCIO");
                informacion.cuentasCorrientesAhorros = rs.getDouble("IF_CUENTASCORRIENTES_AHORROS");
                informacion.cuentasCobrar = rs.getDouble("IF_CUENTAS_COBRAR");
                informacion.inventarios = rs.getDouble("IF_INVENTARIOS");
                informacion.activoCorriente = rs.getDouble("IF_ACTIVO_CORRIENTE");
                informacion.inmuebles = rs.getDouble("IF_INMUEBLES");
                informacion.maquinariaEquipo = rs.getDouble("IF_MAQUINARIA_EQUIPO");
                informacion.vehiculos = rs.getDouble("IF_VEHICULOS");
                informacion.activoFijo = rs.getDouble("IF_ACTIVO_FIJO");
                informacion.otrosActivos = rs.getDouble("IF_OTROS_ACTIVOS");
                informacion.totalOtrosActivos = rs.getDouble("IF_TOTAL_OTROS_ACTIVOS");
                informacion.totalActivo = rs.getDouble("IF_TOTAL_ACTIVO");
                informacion.pasivoCortoPlazo = rs.getDouble("IF_PASIVO_CORTO_PLAZO");
                informacion.pasivoLargoPlazo = rs.getDouble("IF_PASIVO_LARGO_PLAZO");
                informacion.totalPasivo = rs.getDouble("IF_TOTAL_PASIVO");
                informacion.capitalAportado = rs.getDouble("IF_CAPITAL_APORTADO");
                informacion.utilidad = rs.getDouble("IF_UTILIDAD");
                informacion.totalCapitalContable = rs.getDouble("IF_TOTAL_CAPITAL_CONTABLE");
                informacion.totalPasivoMasCapitalContable = rs.getDouble("IF_TOTAL_PASIVO_CAPITAL_CONTABLE");
                informacion.margenUtilidadOper = rs.getInt("IF_MARGEN_UTILIDAD_OPER");
                informacion.margenUtilidadNeta = rs.getInt("IF_MARGEN_UTILIDAD_NETA");
                informacion.indiceLiquidez = rs.getInt("IF_INDICE_LIQUIDEZ");
                informacion.rotacionInventarios = rs.getInt("IF_ROTACION_INVENTARIOS");
                informacion.rotacionCapitalTrabajoSol = rs.getInt("IF_ROTACION_CAPITAL_TRABAJO_SOL");
                informacion.rotacionCuentasCobrar = rs.getInt("IF_ROTACION_CUENTAS_COBRAR");
                informacion.rotacionCuentasPagar = rs.getInt("IF_ROTACION_CUENTAS_PAGAR");
                informacion.endeudamientoTotal = rs.getInt("IF_ENDEUDAMIENTO_TOTAL");
                informacion.endeudamientoFuturoTotal = rs.getInt("IF_ENDEUDAMIENTO_FUTURO_TOTAL");
                informacion.capacidadPago = rs.getInt("IF_CAPACIDAD_PAGO");
                informacion.diasVenta = rs.getInt("IF_DIAS_VENTA");
                informacion.fechaCaptura = rs.getTimestamp("IF_FECHA_CAPTURA");
            }
        } catch (SQLException sqle) {
            logger.error("getInformacionFinanciera", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getInformacionFinanciera", e);
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
        return informacion;
    }

//INSERT INTO D_INFORMACION_FINANCIERA (IF_NUMCLIENTE, IF_NUMSOLICITUD, IF_VENTAS, IF_COSTO_VENTAS, IF_UTILIDAD_BRUTA, IF_GASTOS_OPERACION, IF_UTILIDAD_NEGOCIO, IF_OTROS_INGRESOS_FAMILIA, IF_GASTOS_FAMILIA, IF_UTILIDAD_NETA_FAMILIA, IF_UTILIDAD_UNIDAD_FAMILIA, IF_EFECTIVO_NEGOCIO, IF_CUENTASCORRIENTES_AHORROS, IF_CUENTAS_COBRAR, IF_INVENTARIOS, IF_ACTIVO_CORRIENTE, IF_INMUEBLES, IF_MAQUINARIA_EQUIPO, IF_VEHICULOS, IF_ACTIVO_FIJO, IF_OTROS_ACTIVOS, IF_TOTAL_OTROS_ACTIVOS, IF_TOTAL_ACTIVO, IF_PASIVO_LARGO_PLAZO, IF_TOTAL_PASIVO, IF_CAPITAL_APORTADO, IF_UTILIDAD, IF_TOTAL_CAPITAL_CONTABLE, IF_TOTAL_PASIVO_CAPITAL_CONTABLE, IF_MARGEN_UTILIDAD_OPER, IF_MARGEN_UTILIDAD_NETA, IF_INDICE_LIQUIDEZ, IF_ROTACION_INVENTARIOS, IF_ROTACION_CAPITAL_TRABAJO_SOL, IF_ROTACION_CUENTAS_COBRAR, IF_ROTACION_CUENTAS_PAGAR, IF_ENDEUDAMIENTO_TOTAL, IF_ENDEUDAMIENTO_FUTURO_TOTAL, IF_CAPACIDAD_PAGO, IF_DIAS_VENTA, IF_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    public int addInformacionFinanciera(int idCliente, int idSolicitud, InformacionFinancieraVO informacion) throws ClientesException {
        return addInformacionFinanciera(null, idCliente, idSolicitud, informacion);
    }

    public int addInformacionFinanciera(Connection conn, int idCliente, int idSolicitud, InformacionFinancieraVO informacion) throws ClientesException {

        String query = "INSERT INTO D_INFORMACION_FINANCIERA (IF_NUMCLIENTE, IF_NUMSOLICITUD, IF_VENTAS, IF_COSTO_VENTAS, "
                + "IF_UTILIDAD_BRUTA, IF_GASTOS_OPERACION, IF_UTILIDAD_NEGOCIO, IF_OTROS_INGRESOS_FAMILIA, "
                + "IF_GASTOS_FAMILIA, IF_UTILIDAD_NETA_FAMILIA, IF_UTILIDAD_UNIDAD_FAMILIA, IF_EFECTIVO_NEGOCIO, "
                + "IF_CUENTASCORRIENTES_AHORROS, IF_CUENTAS_COBRAR, IF_INVENTARIOS, IF_ACTIVO_CORRIENTE, IF_INMUEBLES, "
                + "IF_MAQUINARIA_EQUIPO, IF_VEHICULOS, IF_ACTIVO_FIJO, IF_OTROS_ACTIVOS, IF_TOTAL_OTROS_ACTIVOS, "
                + "IF_TOTAL_ACTIVO, IF_PASIVO_CORTO_PLAZO, IF_PASIVO_LARGO_PLAZO, IF_TOTAL_PASIVO, IF_CAPITAL_APORTADO, IF_UTILIDAD, "
                + "IF_TOTAL_CAPITAL_CONTABLE, IF_TOTAL_PASIVO_CAPITAL_CONTABLE, IF_MARGEN_UTILIDAD_OPER, IF_MARGEN_UTILIDAD_NETA, "
                + "IF_INDICE_LIQUIDEZ, IF_ROTACION_INVENTARIOS, IF_ROTACION_CAPITAL_TRABAJO_SOL, IF_ROTACION_CUENTAS_COBRAR, "
                + "IF_ROTACION_CUENTAS_PAGAR, IF_ENDEUDAMIENTO_TOTAL, IF_ENDEUDAMIENTO_FUTURO_TOTAL, IF_CAPACIDAD_PAGO, "
                + "IF_DIAS_VENTA, IF_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setDouble(param++, informacion.ventas);
            ps.setDouble(param++, informacion.costoVentas);
            ps.setDouble(param++, informacion.utilidadBruta);
            ps.setDouble(param++, informacion.gastosOperacion);
            ps.setDouble(param++, informacion.utilidadNegocio);
            ps.setDouble(param++, informacion.otrosIngresosFamilia);
            ps.setDouble(param++, informacion.gastosFamilia);
            ps.setDouble(param++, informacion.utilidadNetaFamilia);
            ps.setDouble(param++, informacion.utilidadUnidadFamilia);
            ps.setDouble(param++, informacion.efectivoNegocio);
            ps.setDouble(param++, informacion.cuentasCorrientesAhorros);
            ps.setDouble(param++, informacion.cuentasCobrar);
            ps.setDouble(param++, informacion.inventarios);
            ps.setDouble(param++, informacion.activoCorriente);
            ps.setDouble(param++, informacion.inmuebles);
            ps.setDouble(param++, informacion.maquinariaEquipo);
            ps.setDouble(param++, informacion.vehiculos);
            ps.setDouble(param++, informacion.activoFijo);
            ps.setDouble(param++, informacion.otrosActivos);
            ps.setDouble(param++, informacion.totalOtrosActivos);
            ps.setDouble(param++, informacion.totalActivo);
            ps.setDouble(param++, informacion.pasivoCortoPlazo);
            ps.setDouble(param++, informacion.pasivoLargoPlazo);
            ps.setDouble(param++, informacion.totalPasivo);
            ps.setDouble(param++, informacion.capitalAportado);
            ps.setDouble(param++, informacion.utilidad);
            ps.setDouble(param++, informacion.totalCapitalContable);
            ps.setDouble(param++, informacion.totalPasivoMasCapitalContable);
            ps.setInt(param++, informacion.margenUtilidadOper);
            ps.setInt(param++, informacion.margenUtilidadNeta);
            ps.setInt(param++, informacion.indiceLiquidez);
            ps.setInt(param++, informacion.rotacionInventarios);
            ps.setInt(param++, informacion.rotacionCapitalTrabajoSol);
            ps.setInt(param++, informacion.rotacionCuentasCobrar);
            ps.setInt(param++, informacion.rotacionCuentasPagar);
            ps.setInt(param++, informacion.endeudamientoTotal);
            ps.setInt(param++, informacion.endeudamientoFuturoTotal);
            ps.setInt(param++, informacion.capacidadPago);
            ps.setInt(param++, informacion.diasVenta);
            ps.setTimestamp(param++, informacion.fechaCaptura);
            logger.debug("Ejecutando = " + query);
            logger.debug("Informacion Financiera= " + informacion.toString());
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("addInformacionFinanciera", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("addInformacionFinanciera", e);
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

//UPDATE D_INFORMACION_FINANCIERA SET IF_VENTAS = ?, IF_COSTO_VENTAS = ?, IF_UTILIDAD_BRUTA = ?, IF_GASTOS_OPERACION = ?, IF_UTILIDAD_NEGOCIO = ?, IF_OTROS_INGRESOS_FAMILIA = ?, IF_GASTOS_FAMILIA = ?, IF_UTILIDAD_NETA_FAMILIA = ?, IF_UTILIDAD_UNIDAD_FAMILIA = ?, IF_EFECTIVO_NEGOCIO = ?, IF_CUENTASCORRIENTES_AHORROS = ?, IF_CUENTAS_COBRAR = ?, IF_INVENTARIOS = ?, IF_ACTIVO_CORRIENTE = ?, IF_INMUEBLES = ?, IF_MAQUINARIA_EQUIPO = ?, IF_VEHICULOS = ?, IF_ACTIVO_FIJO = ?, IF_OTROS_ACTIVOS = ?, IF_TOTAL_OTROS_ACTIVOS = ?, IF_TOTAL_ACTIVO = ?, IF_PASIVO_CORTO_PLAZO = ?, IF_PASIVO_LARGO_PLAZO = ?, IF_TOTAL_PASIVO = ?, IF_CAPITAL_APORTADO = ?, IF_UTILIDAD = ?, IF_TOTAL_CAPITAL_CONTABLE = ?, IF_TOTAL_PASIVO_CAPITAL_CONTABLE = ?, IF_MARGEN_UTILIDAD_OPER = ?, IF_MARGEN_UTILIDAD_NETA = ?, IF_INDICE_LIQUIDEZ = ?, IF_ROTACION_INVENTARIOS = ?, IF_ROTACION_CAPITAL_TRABAJO_SOL = ?, IF_ROTACION_CUENTAS_COBRAR = ?, IF_ROTACION_CUENTAS_PAGAR = ?, IF_ENDEUDAMIENTO_TOTAL = ?, IF_ENDEUDAMIENTO_FUTURO_TOTAL = ?, IF_CAPACIDAD_PAGO = ?, IF_DIAS_VENTA = ? WHERE IF_NUMCLIENTE = ? AND IF_NUMSOLICITUD = ?  
    public int updateInformacionFinanciera(int idCliente, int idSolicitud, InformacionFinancieraVO informacion) throws ClientesException {

        String query = "UPDATE D_INFORMACION_FINANCIERA SET IF_VENTAS = ?, IF_COSTO_VENTAS = ?, IF_UTILIDAD_BRUTA = ?, "
                + "IF_GASTOS_OPERACION = ?, IF_UTILIDAD_NEGOCIO = ?, IF_OTROS_INGRESOS_FAMILIA = ?, IF_GASTOS_FAMILIA = ?, "
                + "IF_UTILIDAD_NETA_FAMILIA = ?, IF_UTILIDAD_UNIDAD_FAMILIA = ?, IF_EFECTIVO_NEGOCIO = ?, "
                + "IF_CUENTASCORRIENTES_AHORROS = ?, IF_CUENTAS_COBRAR = ?, IF_INVENTARIOS = ?, IF_ACTIVO_CORRIENTE = ?, "
                + "IF_INMUEBLES = ?, IF_MAQUINARIA_EQUIPO = ?, IF_VEHICULOS = ?, IF_ACTIVO_FIJO = ?, IF_OTROS_ACTIVOS = ?, "
                + "IF_TOTAL_OTROS_ACTIVOS = ?, IF_TOTAL_ACTIVO = ?, IF_PASIVO_CORTO_PLAZO = ?, IF_PASIVO_LARGO_PLAZO = ?, "
                + "IF_TOTAL_PASIVO = ?, IF_CAPITAL_APORTADO = ?, IF_UTILIDAD = ?, IF_TOTAL_CAPITAL_CONTABLE = ?, "
                + "IF_TOTAL_PASIVO_CAPITAL_CONTABLE = ?, IF_MARGEN_UTILIDAD_OPER = ?, IF_MARGEN_UTILIDAD_NETA = ?, "
                + "IF_INDICE_LIQUIDEZ = ?, IF_ROTACION_INVENTARIOS = ?, IF_ROTACION_CAPITAL_TRABAJO_SOL = ?, "
                + "IF_ROTACION_CUENTAS_COBRAR = ?, IF_ROTACION_CUENTAS_PAGAR = ?, IF_ENDEUDAMIENTO_TOTAL = ?, "
                + "IF_ENDEUDAMIENTO_FUTURO_TOTAL = ?, IF_CAPACIDAD_PAGO = ?, IF_DIAS_VENTA = ?, IF_FECHA_CAPTURA = ? WHERE "
                + "IF_NUMCLIENTE = ? AND IF_NUMSOLICITUD = ?";
        Connection cn = null;
        int res = 0;
        int param = 1;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDouble(param++, informacion.ventas);
            ps.setDouble(param++, informacion.costoVentas);
            ps.setDouble(param++, informacion.utilidadBruta);
            ps.setDouble(param++, informacion.gastosOperacion);
            ps.setDouble(param++, informacion.utilidadNegocio);
            ps.setDouble(param++, informacion.otrosIngresosFamilia);
            ps.setDouble(param++, informacion.gastosFamilia);
            ps.setDouble(param++, informacion.utilidadNetaFamilia);
            ps.setDouble(param++, informacion.utilidadUnidadFamilia);
            ps.setDouble(param++, informacion.efectivoNegocio);
            ps.setDouble(param++, informacion.cuentasCorrientesAhorros);
            ps.setDouble(param++, informacion.cuentasCobrar);
            ps.setDouble(param++, informacion.inventarios);
            ps.setDouble(param++, informacion.activoCorriente);
            ps.setDouble(param++, informacion.inmuebles);
            ps.setDouble(param++, informacion.maquinariaEquipo);
            ps.setDouble(param++, informacion.vehiculos);
            ps.setDouble(param++, informacion.activoFijo);
            ps.setDouble(param++, informacion.otrosActivos);
            ps.setDouble(param++, informacion.totalOtrosActivos);
            ps.setDouble(param++, informacion.totalActivo);
            ps.setDouble(param++, informacion.pasivoCortoPlazo);
            ps.setDouble(param++, informacion.pasivoLargoPlazo);
            ps.setDouble(param++, informacion.totalPasivo);
            ps.setDouble(param++, informacion.capitalAportado);
            ps.setDouble(param++, informacion.utilidad);
            ps.setDouble(param++, informacion.totalCapitalContable);
            ps.setDouble(param++, informacion.totalPasivoMasCapitalContable);
            ps.setInt(param++, informacion.margenUtilidadOper);
            ps.setInt(param++, informacion.margenUtilidadNeta);
            ps.setInt(param++, informacion.indiceLiquidez);
            ps.setInt(param++, informacion.rotacionInventarios);
            ps.setInt(param++, informacion.rotacionCapitalTrabajoSol);
            ps.setInt(param++, informacion.rotacionCuentasCobrar);
            ps.setInt(param++, informacion.rotacionCuentasPagar);
            ps.setInt(param++, informacion.endeudamientoTotal);
            ps.setInt(param++, informacion.endeudamientoFuturoTotal);
            ps.setInt(param++, informacion.capacidadPago);
            ps.setInt(param++, informacion.diasVenta);
            ps.setTimestamp(param++, informacion.fechaCaptura);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            logger.debug("Ejecutando = " + query);
            logger.debug("Informacion Financiera = " + informacion.toString());
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("updateInformacionFinanciera", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("updateInformacionFinanciera", e);
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

}

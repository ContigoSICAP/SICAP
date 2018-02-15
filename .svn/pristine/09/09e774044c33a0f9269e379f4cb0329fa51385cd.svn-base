package com.sicap.clientes.saldofondeadorservice;

import com.sicap.clientes.circuloservice.ConsultaCirculo;
import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.SaldoFondeadorDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
@WebService(serviceName = "SaldoFondeador")
public class SaldoFondeador extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(ConsultaCirculo.class);

    /**
     * Metodo de actulizaSaldoFondeador
     *
     * @param usuario
     * @param password
     * @param numGrupo
     * @param numCredito
     * @param tipoMovimiento
     * @param bancoOrigen
     * @param monto
     * @param FechaMovimiento
     * @param Fondeador
     * @return
     */
    @WebMethod(operationName = "actulizaSaldoFondeador")
    public SaldoFondeadorResp actulizaSaldoFondeador(
            @XmlElement(required = true) @WebParam(name = "usuario") String usuario,
            @XmlElement(required = true) @WebParam(name = "password") String password,
            @XmlElement(required = true) @WebParam(name = "numGrupo") int numGrupo,
            @XmlElement(required = true) @WebParam(name = "numCredito") int numCredito,
            @XmlElement(required = true) @WebParam(name = "bancoOrigen") int bancoOrigen,
            @XmlElement(required = true) @WebParam(name = "monto") double monto,
            @XmlElement(required = true) @WebParam(name = "tipoMovimiento") String tipoMovimiento,
            @XmlElement(required = true) @WebParam(name = "FechaMovimiento") String FechaMovimiento,
            @XmlElement(required = true) @WebParam(name = "Fondeador") int Fondeador
    ) {
        SaldoFondeadorResp resp = new SaldoFondeadorResp();
        if (identifica(usuario, password)) {
            FondeadorUtil fondeadorUtil = new FondeadorUtil();
            SaldoFondeadorVO saldoFondeadorVo = new SaldoFondeadorVO();
            int respuesta = 0;
            NumberFormat num = new DecimalFormat("########0.##");
            try {
                if (tipoMovimiento.equals(ClientesConstants.DESEMBOLSO)
                        || tipoMovimiento.equals(ClientesConstants.CANCELACION_DESEMBOLSO)
                        || tipoMovimiento.equals(ClientesConstants.DEVOLUCION)
                        || tipoMovimiento.equals(ClientesConstants.CAMBIO_FONDEADOR_SALIDA)) {
                    saldoFondeadorVo.numFondeador = Fondeador;
                    saldoFondeadorVo.origen = bancoOrigen;
                    saldoFondeadorVo.tipoMov = tipoMovimiento;
                    saldoFondeadorVo.monto = monto;
                    saldoFondeadorVo.numCliente = numGrupo;
                    saldoFondeadorVo.numTransaccion = 1;
                    saldoFondeadorVo.numCredito = numCredito;
                    respuesta = fondeadorUtil.actualizaSaldoFondeador(saldoFondeadorVo);
                    switch (respuesta) {
                        case 0:
                            resp.setStatus("IN003,Hubo un error en la consulta favor de verificar");
                            break;
                        case 1:
                            resp.setStatus("OK");
                            break;
                        case 2:
                            resp.setStatus("IN001,Saldo insufiente");
                            break;
                    }
                    resp.setSaldoFondeador(num.format((Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + Fondeador)))));

                } else {
                    resp.setStatus("IN002,Tipo de movimiento no valido");
                }

            } catch (Exception e) {
                myLogger.info("Entra al catch");
                myLogger.error("ActualizaSaldoFondeador: ", e);
                resp.setStatus("KO");
                resp.setErrorCode("CCC008");
                resp.setErrorDetail("Error desconocido, favor de reportarlo");

            } finally {                
                resp.setTimeStamp(new java.util.Date());
                resp.toString();
                myLogger.info("Enviando respuesta");

            }
        } else {
            resp.setStatus("Credencial no valida");
        }
        return resp;
    }

    private static boolean identifica(String usuario, String pass) {
        boolean entra = false;
        if (usuario.equals(ClientesConstants.USS_WS) && pass.equals(ClientesConstants.PSS_WS)) {
            entra = true;
        }
        return entra;
    }

    /**
     * Metodo de obtenSaldoFondeador
     *
     * @param usuario
     * @param password
     * @param Fondeador
     * @return
     */

    @WebMethod(operationName = "obtenSaldoFondeador")
    public SaldoFondeadorResp obtenSaldoFondeador(
            @XmlElement(required = true) @WebParam(name = "usuario") String usuario,
            @XmlElement(required = true) @WebParam(name = "password") String password,
            @XmlElement(required = true) @WebParam(name = "Fondeador") int Fondeador
    ) {
        SaldoFondeadorResp resp = new SaldoFondeadorResp();
        NumberFormat num = new DecimalFormat("########0.##");
        try {
            if (identifica(usuario, password)) {
                double saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + Fondeador));
                resp.setSaldoFondeador(num.format(saldoFondeador));
                resp.setStatus("OK");
            } else {
                resp.setStatus("Credencial no valida");
            }
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("ActualizaSaldoFondeador: ", e);
            resp.setStatus("KO");
            resp.setErrorCode("CCC008");
            resp.setErrorDetail("Error desconocido, favor de reportarlo");
        } finally {
            resp.setTimeStamp(new java.util.Date());
            resp.toString();
            myLogger.info("Enviando respuesta");

        }
        return resp;
    }

}

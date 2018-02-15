package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CondonacionesVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.dao.cartera.CondonacionesDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandQuitaCapitalVencido implements Command {

    private String siguiente;

    public CommandQuitaCapitalVencido(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //TreeMap catEjecutivos = new TreeMap();
        CondonacionesDAO condonacionDAO = new CondonacionesDAO();
        CondonacionesVO condonaciones = null;
        Notification notificaciones[] = new Notification[1];
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        EventoHelper eventohelper = new EventoHelper();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        CreditoCartVO credito = new CreditoCartVO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        int idCliente = 0;
        int idSolicitud = 0;
        int idCredito = 0;
        int numPago = 0;
        double montoCapital = 0;
        double total_montoCapital = 0;
        double saldo_total_aldia = 0;
        try {
            if (!catalogoDAO.ejecutandoCierre()) {
                idCliente = HTMLHelper.getParameterInt(request, "idGrupo");
                idCredito = HTMLHelper.getParameterInt(request, "idCredito");
                idSolicitud = HTMLHelper.getParameterInt(request, "idCiclo");
                TablaAmortDAO tablaDAO = new TablaAmortDAO();
                TransaccionesHelper transacHelper = new TransaccionesHelper();
                credito = creditoDAO.getCredito(idCredito);
                saldoVO = saldoDAO.getSaldo(idCliente, idCredito);
                saldo_total_aldia = saldoVO.getSaldoTotalAlDia();
                int num_div = 0;
                String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
                SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                Date fechaUltimoCierre  = sdf.parse(ultimaFecha);

                if (idCliente > 0 && idCredito > 0) {
                    //vuelve a hacer la consulta para no perder los datos de la sesion
                    String[] IdDividendosCond = request.getParameterValues("dividendos");
                    num_div = IdDividendosCond.length;
                    for (int i = 0; i < IdDividendosCond.length; i++) {
                        condonaciones = new CondonacionesVO();
                        TablaAmortVO tablaAmort = new TablaAmortVO();
                        numPago = Convertidor.stringToInt(IdDividendosCond[i]);
                        tablaAmort = tablaDAO.getDivPago(idCliente, idCredito, numPago);
                        montoCapital = tablaAmort.abonoCapital - tablaAmort.capitalPagado;
                        System.out.println("contador " + i);
                        // Numero de cliente
                        condonaciones.numCliente = idCliente;
                        //  Usuario que reprocesa el pago
                        condonaciones.usuario = request.getRemoteUser().toString();
                        // Numero de Credito
                        condonaciones.numCredito = idCredito;
                        // Numero de cliente
                        condonaciones.numDividendo = numPago;
                        // Monto
                        condonaciones.monto = montoCapital;
                        // Fecha
                        condonaciones.fecha = Convertidor.toSqlDate(fechaUltimoCierre);
                        // Rubro (en este caso solo se considera la multa)
                        condonaciones.rubro = ClientesConstants.CAPITAL;
                        // Se actualizan los rubros de la tabla de amortizacion
                        tablaAmort.capitalPagado = tablaAmort.abonoCapital;
                        tablaAmort.montoPagar = tablaAmort.montoPagar - montoCapital;;
                        if (tablaAmort.montoPagar < .05) {
                            tablaAmort.pagado = "S";
                        }
                        tablaDAO.updateSaldosTablaAmort(tablaAmort);
// Se lleva un control de los totales
                        total_montoCapital = total_montoCapital + montoCapital;
                        saldo_total_aldia = saldo_total_aldia - (montoCapital);
                        // Se registra un evento
                        //eventohelper.registraCondonacion(condonaciones, saldo_total_aldia);
                        eventohelper.registraCondonacion(condonaciones, saldoVO.getSaldoConInteresAlFinal() - (total_montoCapital));
                        // Se guarda la condonacion
                        condonacionDAO.addCondonacion(condonaciones);
                    }
                    // Se actualiza la tabla de saldos
                    saldoVO.setSaldoCapital(saldoVO.getSaldoCapital() - total_montoCapital);
                    saldoVO.setCapitalVencido(saldoVO.getCapitalVencido() - total_montoCapital);
                    saldoVO.setTotalVencido(saldoVO.getTotalVencido() - (total_montoCapital));
                    saldoVO.setSaldoTotalAlDia(saldoVO.getSaldoTotalAlDia() - (total_montoCapital));
                    saldoVO.setSaldoConInteresAlFinal(saldoVO.getSaldoConInteresAlFinal() - (total_montoCapital));
                    saldoVO.setCtaContable(saldoVO.getCtaContable());
                    saldoVO.setBonificacionPagada(saldoVO.getBonificacionPagada() + total_montoCapital);
                    saldoVO.setCuotasVencidas(saldoVO.getCuotasVencidas() - num_div);
                    //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
                    if (saldoVO.getTotalVencido() > .05) {
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                    } else {
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                        saldoVO.setDiasMora(0);
                        credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                        credito.setNumDiasMora(0);
                    }
                    if (saldoVO.getSaldoTotalAlDia() - total_montoCapital <= .2) {
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
                        saldoVO.setDiasMora(0);
                        credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO);
                        credito.setNumDiasMora(0);
                    }
                    saldoDAO.updateSaldo(saldoVO);
                    creditoDAO.updatePagoCredito(credito);
                    /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
                     */
                    // se llenan los montos de los rubros
                    ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
                    RubrosVO rubro = new RubrosVO();
                    RubrosVO elementos[] = null;
                    if (total_montoCapital > 0) {
                        rubro.tipoRubro = ClientesConstants.CAPITAL;
                        rubro.monto = total_montoCapital;
                        rubro.status = ClientesConstants.RUBRO_VIGENTE;
                        array_pago.add(rubro);
                    }
                    Logger.debug("rubros " + array_pago.toString());
                    if (array_pago.size() > 0) {
                        elementos = new RubrosVO[array_pago.size()];
                        for (int i = 0; i < elementos.length; i++) {
                            elementos[i] = (RubrosVO) array_pago.get(i);
                        }
                    }

                    transacHelper.registraCondonacion(credito, elementos, Convertidor.toSqlDate(fechaUltimoCierre));
                    BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandQuitaCapitalVencido");
                    bitutil.registraEvento(condonaciones);
                    request.setAttribute("idCiclo", idSolicitud);
                    request.setAttribute("idGrupo", idCliente);
                    request.setAttribute("idCredito", idCredito);
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Quita de Capital");
                    request.setAttribute("NOTIFICACIONES", notificaciones);

                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se han seleccionado cliente y ciclo");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

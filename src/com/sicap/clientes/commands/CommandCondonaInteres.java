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

public class CommandCondonaInteres implements Command {

    private String siguiente;

    public CommandCondonaInteres(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //TreeMap catEjecutivos = new TreeMap();
        CondonacionesDAO condonacionDAO = new CondonacionesDAO();
        Notification notificaciones[] = new Notification[1];
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        EventoHelper eventohelper = new EventoHelper();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        CreditoCartVO credito = new CreditoCartVO();
        int idGrupo = 0;
        int idCiclo = 0;
        int idCredito = 0;
        int numPago = 0;
        double montoInteres = 0;
        double montoIvaInteres = 0;
        double total_montoInteres = 0;
        double total_montoIvaInteres = 0;
        double saldo_total_aldia = 0;
        try {
            if (!catalogoDAO.ejecutandoCierre()) {
                idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
                idCredito = HTMLHelper.getParameterInt(request, "idCredito");
                idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                TablaAmortDAO tablaDAO = new TablaAmortDAO();
                TransaccionesHelper transacHelper = new TransaccionesHelper();
                credito = creditoDAO.getCredito(idCredito);
                saldoVO = saldoDAO.getSaldo(idGrupo, idCredito);
                CondonacionesVO condonaciones = null;
                saldo_total_aldia = saldoVO.getSaldoTotalAlDia();
                String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
                SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                Date fechaUltimoCierre  = sdf.parse(ultimaFecha);

                if (idGrupo > 0 && idCredito > 0) {
                    //vuelve a hacer la consulta para no perder los datos de la sesion
                    String[] IdDividendosCond = request.getParameterValues("dividendos");
                    for (int i = 0; i < IdDividendosCond.length; i++) {
                        condonaciones = new CondonacionesVO();
                        TablaAmortVO tablaAmort = new TablaAmortVO();
                        numPago = Convertidor.stringToInt(IdDividendosCond[i]);
                        tablaAmort = tablaDAO.getDivPago(idGrupo, idCredito, numPago);
                        montoInteres = tablaAmort.interes - tablaAmort.interesPagado;
                        montoIvaInteres = tablaAmort.ivaInteres - tablaAmort.ivaInteresPagado;
                        System.out.println("contador " + i);
                        // Numero de cliente
                        condonaciones.numCliente = idGrupo;
                        //  Usuario que reprocesa el pago
                        condonaciones.usuario = request.getRemoteUser().toString();
                        // Numero de Credito
                        condonaciones.numCredito = idCredito;
                        // Numero de cliente
                        condonaciones.numDividendo = numPago;
                        // Monto
                        condonaciones.monto = (montoInteres + montoIvaInteres);
                        // Fecha
                        condonaciones.fecha = Convertidor.toSqlDate(fechaUltimoCierre);
                        // Rubro (en este caso solo se considera la multa)
                        condonaciones.rubro = ClientesConstants.INTERES;
                        // Se actualizan los rubros de la tabla de amortizacion
                        tablaAmort.interesPagado = tablaAmort.interes;
                        tablaAmort.ivaInteresPagado = tablaAmort.ivaInteres;
                        tablaAmort.montoPagar = tablaAmort.montoPagar - montoInteres - montoIvaInteres;
                        tablaDAO.updateSaldosTablaAmort(tablaAmort);
// Se lleva un control de los totales
                        total_montoInteres = total_montoInteres + montoInteres;
                        total_montoIvaInteres = total_montoIvaInteres + montoIvaInteres;
                        saldo_total_aldia = saldo_total_aldia - (montoInteres + montoIvaInteres);
                        // Se registra un evento
                        //eventohelper.registraCondonacion(condonaciones, saldo_total_aldia);
                        eventohelper.registraCondonacion(condonaciones, saldoVO.getSaldoConInteresAlFinal() - (total_montoInteres + total_montoIvaInteres));
                        // Se guarda la condonacion
                        condonacionDAO.addCondonacion(condonaciones);
                    }
                    // Se actualiza la tabla de saldos
                    saldoVO.setSaldoInteres(saldoVO.getSaldoInteres() - total_montoInteres);
                    saldoVO.setSaldoInteresVencido(saldoVO.getSaldoInteresVencido() - total_montoInteres);
                    saldoVO.setSaldoIvaInteres(saldoVO.getSaldoIvaInteres() - total_montoIvaInteres);
                    saldoVO.setIvaInteresVencido(saldoVO.getIvaInteresVencido() - total_montoIvaInteres);
                    saldoVO.setTotalVencido(saldoVO.getTotalVencido() - (total_montoInteres + total_montoIvaInteres));
                    saldoVO.setSaldoTotalAlDia(saldoVO.getSaldoTotalAlDia() - (total_montoInteres + total_montoIvaInteres));
                    saldoVO.setSaldoConInteresAlFinal(saldoVO.getSaldoConInteresAlFinal() - (total_montoInteres + total_montoIvaInteres));
                    saldoVO.setCtaContable(saldoVO.getCtaContable());
                    saldoVO.setBonificacionPagada(saldoVO.getBonificacionPagada() + total_montoInteres + total_montoIvaInteres);
                    //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
                    if (saldoVO.getTotalVencido() > 0) {
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                    } else {
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE);
                    }
                    saldoDAO.updateSaldo(saldoVO);
                    /* SE ACTUALIZA LA TABLA DE TRANSACCIONES		
                     */
                    // se llenan los montos de los rubros
                    ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
                    RubrosVO rubro = new RubrosVO();
                    RubrosVO elementos[] = null;
                    if (total_montoInteres > 0) {
                        rubro.tipoRubro = ClientesConstants.INTERES;
                        rubro.monto = total_montoInteres;
                        rubro.status = ClientesConstants.RUBRO_VIGENTE;
                        array_pago.add(rubro);
                    }
                    rubro = new RubrosVO();
                    if (total_montoIvaInteres > 0) {
                        rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                        rubro.monto = total_montoIvaInteres;
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
                    BitacoraUtil bitutil = new BitacoraUtil(idGrupo, request.getRemoteUser(), "CommandCondonaInteres");
                    bitutil.registraEvento(condonaciones);

                    request.setAttribute("idCiclo", idCiclo);
                    request.setAttribute("idGrupo", idGrupo);
                    request.setAttribute("idCredito", idCredito);
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Interes condonado");
                    request.setAttribute("NOTIFICACIONES", notificaciones);

                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se han seleccionado grupo y ciclo");
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

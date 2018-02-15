package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import java.util.ArrayList;
import java.util.Calendar;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandCastigarGrupo implements Command {

    private String siguiente;
    private Logger myLogger = Logger.getLogger(CommandCastigarGrupo.class);

    public CommandCastigarGrupo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //TreeMap catEjecutivos = new TreeMap();
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
        double montoMulta = 0;
        double montoMora = 0;
        double montoIvaMulta = 0;
        double montoIvaMora = 0;
        double total_montoMulta = 0;
        double total_montoMora = 0;
        double total_montoIvaMulta = 0;
        double total_montoIvaMora = 0;
        double saldo_total_aldia = 0;
        try {
            myLogger.info("Entrando a comando");
            if (!catalogoDAO.ejecutandoCierre()) {
                idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
                idCredito = HTMLHelper.getParameterInt(request, "idCredito");
                idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                myLogger.info("Iniciando castigo del credito :" + idCredito);
                TransaccionesHelper transacHelper = new TransaccionesHelper();
                credito = creditoDAO.getCredito(idCredito);
                saldoVO = saldoDAO.getSaldo(idGrupo, idCredito);
                if (saldoVO.getEstatus() == 4) {
                    saldo_total_aldia = saldoVO.getSaldoTotalAlDia();
                    Calendar c1 = Calendar.getInstance();
                    String fecha = "";
                    String fechaCierre = CatalogoHelper.getParametro("FECHA_CIERRE");
                    Date fechaCierreDt = Convertidor.stringToDate(fechaCierre,ClientesConstants.FORMATO_FECHA_EU);
                    if (idGrupo > 0 && idCredito > 0) {
                        //vuelve a hacer la consulta para no perder los datos de la sesion
                        String[] saldos = request.getParameterValues("saldos");
                        // Se registra un evento
                        eventohelper.registraCastigo(saldoVO, request);
                        // Se guarda la condonacion
                        // Se actualiza la tabla de saldos
                        saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_CASTIGADO);
                        //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaCierreDt));
                        saldoDAO.updateSaldo(saldoVO);
                        myLogger.info("Actualizo saldo para credito:" + idCredito);
                        /* SE ACTUALIZA LA TABLA DE CREDITO		
                         */
                        credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_CASTIGADO);
                        creditoDAO.updatePagoCredito(credito);
                        // se llenan los montos de los rubros
                        ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
                        RubrosVO rubro = new RubrosVO();
                        RubrosVO elementos[] = null;
                        if (saldoVO.getSaldoCapital() > 0) {
                            rubro.tipoRubro = ClientesConstants.CAPITAL;
                            rubro.monto = saldoVO.getSaldoCapital();
                            rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                            array_pago.add(rubro);
                        }
                        rubro = new RubrosVO();
                        if (saldoVO.getSaldoInteres() > 0) {
                            rubro.tipoRubro = ClientesConstants.INTERES;
                            rubro.monto = saldoVO.getSaldoInteres();
                            rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                            array_pago.add(rubro);
                        }
                        rubro = new RubrosVO();
                        if (saldoVO.getSaldoIvaInteres() > 0) {
                            rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                            rubro.monto = saldoVO.getSaldoIvaInteres();
                            rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                            array_pago.add(rubro);
                        }
                        myLogger.debug("rubros " + array_pago.toString());
                        if (array_pago.size() > 0) {
                            elementos = new RubrosVO[array_pago.size()];
                            for (int i = 0; i < elementos.length; i++) {
                                elementos[i] = (RubrosVO) array_pago.get(i);
                            }
                        }

                        transacHelper.registraCastigo(credito, elementos, fechaCierreDt);
                        request.setAttribute("idCiclo", idCiclo);
                        request.setAttribute("idGrupo", idGrupo);
                        request.setAttribute("idCredito", idCredito);
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Credito Castigado");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se han seleccionado grupo y ciclo");
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El cr&eacute;dito no se encuentra en estatus vencido");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            } else {
                myLogger.info("El cierre esta en ejecucion, no se puede hacer el castigo del credito :" + idCredito);
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

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import java.util.ArrayList;
//import java.util.Calendar;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.naming.NamingException;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.EventosVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

public class CommandRegistraDevOrden implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandRegistraDevOrden.class);

    public CommandRegistraDevOrden(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest req) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        HttpSession session = req.getSession();
        // Obtenemos num. de grupo
        int numGrupo = Integer.parseInt(req.getParameter("idGrupo"));
        // Obtenemos num. de ciclo
        int numCiclo = Integer.parseInt(req.getParameter("idCiclo"));
        String ordenPago = (req.getParameter("ordenPago"));
        GrupoDAO grupoDAO = new GrupoDAO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        PagoVO pago = new PagoVO();
        PagoVO[] totalPagos = null;
        String referencia = null;
        OrdenDePagoVO orden = new OrdenDePagoVO();
        OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
        totalPagos = new PagoVO[1];
        CreditoCartVO credito = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        TransaccionesHelper transaccionHelper = new TransaccionesHelper();
        EventoHelper eventoHelper = new EventoHelper();
        RubrosVO rubro = new RubrosVO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
        int validaDatos = 0;
        IntegranteCicloVO integrante = null;
        String strGarantia = "";
        double porGarantia = 0.0;
        String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        int estatusIntegranteCiclo = 0;
        boolean esOrdPagAdicional = false;
        boolean esInterciclo = false;
        try {
            Date fechaUltimoCierre  = sdf.parse(ultimaFecha);
            if (!catalogoDAO.ejecutandoCierre()) {
                synchronized (this) {
                    myLogger.debug("Iniciando devolucion para orden " + ordenPago);
                    
                    
                    /**
                     * JECB 01/10/2017
                     * Regla de negocio que valida que se haya ingresado una orden de 
                     * pago
                     */
                    if(ordenPago == null){
                        myLogger.debug("Orden de pago no capturada");
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago no fue capturada");
                        req.setAttribute("NOTIFICACIONES", notificaciones);
                        return siguiente;
                    }
                    
                    /**
                     * JECB 01/10/2017
                     * Regla de negocio que valida que la orden de pago ingrsada 
                     * exista en el ambito del sistema
                     * 
                     */
                    OrdenDePagoVO ordenPagoBean = ordenDAO.getOrdenDePago(ordenPago);
                    if(ordenPagoBean == null) {
                        myLogger.debug("No encontro orden de pago");
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago no existe");
                        req.setAttribute("NOTIFICACIONES", notificaciones);
                        return siguiente;                 
                    }
                    
                    /**
                     * JECB 01/10/2017
                     * Verificamos si la devolucion se trata de una orden 
                     * de pago de un credito adicional
                     */
                    estatusIntegranteCiclo = 0;
                    esOrdPagAdicional = AdicionalUtil.isOrdenPagoAdicional(ordenPagoBean);
                    myLogger.debug("Es orden de pago adicional:" + esOrdPagAdicional);
                    if(esOrdPagAdicional){
                        estatusIntegranteCiclo = ClientesConstants.INTEGRANTE_ACTIVO;
                    }else{
                        estatusIntegranteCiclo = ClientesConstants.INTEGRANTE_CANCELADO;
                    }
                    myLogger.debug("estatus de integrante ciclo:" + estatusIntegranteCiclo);
                    /**
                     * JECB 01/10/2017
                     * Se verifican los dato de la devolucion
                     * en caso de tratarse de un credito adicional el cliente no debera de estar 
                     * cancelado, en caso de tratarde de un credito comunal es prioritario que 
                     * el cliente este cancelado antes de continuar con el proceso de devolucion
                     */
                    //validaDatos = integranteCicloDAO.verificaDatosDevolicion(estatusIntegranteCiclo,numGrupo, numCiclo, ordenPago);
                    
                    //validaDatos = integranteCicloDAO.verificaDatosDevolicion(numGrupo, numCiclo, ordenPago);
                    integrante = integranteCicloDAO.getDatosMontoConSeguroDevolicion(estatusIntegranteCiclo,numGrupo, numCiclo, ordenPago);                    
                    //if (validaDatos == 1) {
                    if (integrante != null) {
                        if(integrante.getTipo()==ClientesConstants.TIPO_CLIENTE_INTERCICLO||integrante.getTipo()==ClientesConstants.TIPO_CLIENTE_INTERCICLO_2){
                            esInterciclo = true;
                        }                        
                        GrupoVO grupo = grupoDAO.getGrupo(numGrupo);
                        if (grupo != null) {
                            myLogger.debug("Obtuvo el grupo");
                            CicloGrupalVO ciclo = cicloDAO.getCiclo(numGrupo, numCiclo);
                            if (ciclo != null) {
                                myLogger.debug("Obtuvo el ciclo");
                                strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
                                porGarantia = Double.parseDouble(strGarantia);
                                porGarantia = porGarantia / 100;
                                credito = creditoDAO.getCreditoClienteSol(ciclo.idGrupo, ciclo.idCiclo);
                                if (credito.getStatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                                    myLogger.debug("Credito vigente");
                                    if (ordenPago != null) {
                                        /**
                                        * JECB 01/10/2017
                                        * Se omite la consulta de la orden de pago pues que previamente ya se realizo 
                                        * dicha consulta
                                        */
                                        //orden = ordenDAO.getOrdenDePago(ordenPago);
                                        orden = ordenPagoBean;
                                        if (orden != null) {
                                            myLogger.debug("Obtuvo orden");
                                            if ((orden.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE && orden.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA)
                                                    //|| (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX && orden.getEstatus() == ClientesConstants.OP_SOLICITA_CANCELACION)
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX && orden.getEstatus() == ClientesConstants.OP_CANCELADA)
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX && orden.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA)
                                                    //|| (orden.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK && orden.getEstatus() == ClientesConstants.OP_SOLICITA_CANCELACION)
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK && orden.getEstatus() == ClientesConstants.OP_CANCELADA)
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK && orden.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA)
                                                    //|| (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER && orden.getEstatus() == ClientesConstants.OP_SOLICITA_CANCELACION)
                                                    //|| (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER && orden.getEstatus() == ClientesConstants.OP_CANCELADA)
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER && orden.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA)
                                                    || orden.getEstatus() == ClientesConstants.OP_DEVUELTA
                                                    || (orden.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO && orden.getEstatus() == ClientesConstants.OP_CANCELACION_CONFIRMADA)
                                                    || orden.getEstatus() == ClientesConstants.OP_DEVUELTA) {
                                    //eventos = eventoDAO.getDividendoTipo(ciclo.idGrupo, ciclo.idCreditoIBS, orden.getIdCliente(), ClientesConstants.DEVOLUCION_ORDEN_PAGO);
                                                //if (eventos == null && orden.getEstatus() != ClientesConstants.OP_DEVUELTA) {
                                                if (orden.getEstatus() != ClientesConstants.OP_DEVUELTA) {
                                                    myLogger.debug("Aplicando pago");
                                                    // Se arma el pago
                                                    pago.monto = orden.getMonto();
                                                    pago.referencia = referencia;
                                                    pago.fechaPago = Convertidor.toSqlDate(fechaUltimoCierre);
                                                    pago.bancoReferencia = orden.getIdBanco();
                                                    switch (ciclo.bancoDispersion) {
                                                        case ClientesConstants.ID_BANCO_BANORTE:
                                                            pago.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANORTE"), "D");
                                                            break;
                                                        case ClientesConstants.ID_BANCO_BANCOMER:
                                                            pago.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER"), "D");
                                                            break;
                                                        case ClientesConstants.ID_BANCO_BANAMEX:
                                                            pago.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX"), "D");
                                                            break;
                                                        case ClientesConstants.ID_BANCO_SCOTIABANK:
                                                            pago.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA"), "D");
                                                            break;
                                                        case ClientesConstants.ID_BANCO_SANTANDER_NVO:
                                                            pago.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ciclo.bancoDispersion, CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER"), "D");
                                                            break;
                                                    }
                                                    totalPagos[0] = pago;
                                                    double ordenPagoDev = 0;
                                                    if (integrante.getMontoConSeguro() > 0) {
                                                        // Cuenta con seguro y calculamos su devolucion con seguro
                                                        ordenPagoDev = integrante.getMontoConSeguro();
                                                        // Calculamos el monto con seguro
                                                        integrante.costoSeguro = integrante.getMontoConSeguro() - orden.getMonto();
                                                    } else {
                                                        ordenPagoDev = orden.getMonto();
                                                    }
                                                    myLogger.debug("Monto a devolver: " + ordenPagoDev);
                                                    
                                                    double remanente = 0d;
                                                    
                                                    /**
                                                    * JECB 01/10/2017
                                                    * En caso de tratarse de una devolucion de una orden de pago 
                                                    * de adicional se invoca método donde se realiza toda las reglas de negocio
                                                    * de la devolucion a excepcion de decrementar el 10% de garantia sobre el monto
                                                    * del credito adicional
                                                    */
                                                    if(esOrdPagAdicional){
                                                        myLogger.debug("es devolucion para un adicional:");
                                                        remanente = TablaAmortHelper.aplicaPagoAnticipadoReduccionGrupoAdicional(credito, ciclo, grupo, orden.getMonto());
                                                    }else{
                                                        myLogger.debug("es devolucion normal:");
                                                        remanente = TablaAmortHelper.aplicaPagoAnticipadoReduccionGrupo(credito, ciclo, grupo, ordenPagoDev, orden.getMonto());
                                                    }
                                                    myLogger.debug("Pago aplicado");
                                                    credito.setBancoDesembolso(orden.getIdBanco());
                                                    rubro.status = ClientesConstants.RUBRO_VIGENTE;
                                                    rubro.monto = orden.getMonto();
                                                    rubro.tipoRubro = ClientesConstants.EFECTIVO;
                                                    transaccionHelper.registraPagoDev(credito, pago);
                                                    myLogger.debug("Transacción pago registrada");
                                                    // Se agrega el parametro de integrante para las tx de monto con seguro
                                                    if(esOrdPagAdicional){
                                                        transaccionHelper.registraDevolucionOdp(credito, rubro, Convertidor.toSqlDate(fechaUltimoCierre), null,esOrdPagAdicional,esInterciclo);
                                                    }else{
                                                        transaccionHelper.registraDevolucionOdp(credito, rubro, Convertidor.toSqlDate(fechaUltimoCierre), integrante,esOrdPagAdicional,esInterciclo);
                                                    }
                                                    
                                                    myLogger.debug("Transacción devolución registrada");
                                                    //transaccionHelper.aplicaPagoGarantia(credito, Calendar.getInstance().getTime(), (orden.getMonto()*porGarantia));
                                                    transaccionHelper.registraPagoCancelacionDes(credito, pago);
                                                    myLogger.debug("Transacción devolución registrada");
                                                    saldoVO = saldoDAO.getSaldo(credito.getNumCliente(), credito.getNumCredito());
                                                    //saldoVO.setFechaGeneracion(Convertidor.toSqlDate(fechaUltimoCierre));
                                                    eventoHelper.registraDevolucionOrden(credito, orden, pago, saldoVO);
                                                    myLogger.debug("Evento registrado");
                                                    ordenDAO.updateEstatusODP(orden.getIdCliente(), orden.getIdSolicitud(), orden.getReferencia(), ClientesConstants.OP_DEVUELTA);
                                                    myLogger.debug("Estatus de orden actualizado");
                                                    new TablaDevengamientoHelper().ajustaDevengamiento(credito, saldoVO);
                                                    myLogger.debug("Ajuste de Devengamientos actualizado");
                                                    if (remanente == 0) {
                                                        // Actualiza el status del folio
                                                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La devolucion fue procesada correctamente");
                                                        BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, req.getRemoteUser(), "CommandRegistraDevOrden");
                                                        bitutil.registraEvento(orden);
                                                        myLogger.debug("Registro en bitacora");
                                                        
                                                        
                                                        /**
                                                         * JECB 01/10/2017
                                                         * Se ingrega funcionalidad que realiza la actualizacion de informacion en 
                                                         * tabla d_decision_comite y d_integrante_ciclo, para cuando se trata de un 
                                                         * una devolucion de crédito adicional
                                                         */
                                                        if(esOrdPagAdicional){
                                                            myLogger.debug("Es devolucion de adicional, se actualizara info en decision_comite e integrante_ciclo");
                                                            IntegranteCicloVO icVO =  integranteCicloDAO.getIntegrantesCicloFromOrdenPago(ordenPagoBean);
                                                            double montoOriginal = icVO.monto - icVO.montoAdicional;
                                                            myLogger.debug("idGrupo:"+ icVO.idGrupo);
                                                            myLogger.debug("idCiclo:"+icVO.idCiclo);
                                                            
                                                            myLogger.debug("idCliente:"+ icVO.idCliente);
                                                            myLogger.debug("idSolicitidus:"+icVO.idSolicitud);
                                                            myLogger.debug("monto:" + icVO.monto);
                                                            myLogger.debug("montoAdicional:" + icVO.montoAdicional);
                                                            myLogger.debug("montoOriginal:"+montoOriginal);
                                                            icVO.monto = montoOriginal;
                                                            int res = new DecisionComiteDAO().updateMontos(null, icVO.idCliente, icVO.idSolicitud, icVO.getMonto());
                                                            myLogger.debug("registros actualizados en decision comite:"+res);
                                                            
                                                            icVO.setTipo_adicional(0);
                                                            icVO.setIdPorcentajeAdicional(0);
                                                            icVO.setMontoAdicional(0d);
                                                            Calendar fecha = Calendar.getInstance();
                                                            fecha.set(Calendar.HOUR_OF_DAY, 0);
                                                            fecha.set(Calendar.MINUTE, 0);
                                                            fecha.set(Calendar.SECOND, 0);
                                                            fecha.set(Calendar.MILLISECOND, 0);
                                                            fecha.set(Calendar.YEAR, 1900);       
                                                            fecha.set(Calendar.MONTH, Calendar.JANUARY);
                                                            fecha.set(Calendar.DAY_OF_MONTH, 1);
                                                            icVO.setFechaDesembAdicional(fecha.getTime());
                                                            
                                                            myLogger.debug("tipo Adicional:"+ icVO.tipo_adicional);
                                                            myLogger.debug("porcentaje adicional:"+icVO.idPorcentajeAdicional);
                                                            myLogger.debug("montoAdicional:"+ icVO.montoAdicional);
                                                            myLogger.debug("fecha desembolso Adicional:"+icVO.fechaDesembAdicional);
                                                            myLogger.debug("Actualizando integrante ciclo por cancelacion de adicional");
                                                            integranteCicloDAO.updateIntegranteAdicional(null, icVO.idGrupo, icVO.idCiclo, icVO); 
                                                        }
                                                        
                                                    } else {
                                                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El pago no fue procesado correctamente, verificar incidencias");
                                                    }
                                                } else {
                                                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago ya fue devuelta");
                                                    myLogger.debug("Orden de pago devuelta");
                                                }
                                            } else {
                                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago no se encuentra cancelada");
                                                myLogger.debug("Orden de pago sin cancelar");
                                            }
                                        } else {
                                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago no existe");
                                            myLogger.debug("Orden de pago no capturada");
                                        }
                                    } else {
                                        myLogger.debug("No encontro orden de pago");
                                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La orden de pago no fue capturada");
                                    }

                                } else {
                                    myLogger.debug("Credito no vigente");
                                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Para ejecutar la devolución es necesario que el crédito se encuentre vigente");
                                }
                            } else {
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El grupo no tiene ese ciclo");
                            }

                        } else {
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El grupo no existe o no se encuentra formado");
                        }
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Los datos de Grupo, Ciclo y Orden de Pago Cancelada no coinciden o el integrante no esta cancelado");
                    }
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
            }
        } catch (NamingException e) {
            myLogger.error("NamingException", e);
            throw new CommandException(e.getMessage());
        } catch (SQLException e) {
            myLogger.error("SQLException", e);
            throw new CommandException(e.getMessage());
        } catch (ClientesException e) {
            // TODO Auto-generated catch block
            myLogger.error("ClientesException", e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            myLogger.error("Exception", e);
        }

        req.setAttribute("NOTIFICACIONES", notificaciones);
        return siguiente;
    }	//fin command

}

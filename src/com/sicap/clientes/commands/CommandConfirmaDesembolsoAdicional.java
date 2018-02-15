/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandConfirmaDesembolsoAdicional implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConfirmaDesembolsoAdicional.class);

    public CommandConfirmaDesembolsoAdicional(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        Connection con = null, conCar = null;
        GrupoVO grupoVO = new GrupoVO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        TablaAmortVO tablaVO = new TablaAmortVO();
        CreditoCartVO creditoVO = new CreditoCartVO();
        OrdenDePagoVO ordenVO = null;
        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
        ArrayList<IntegranteCicloVO> arrIntegrantes = new ArrayList<IntegranteCicloVO>();
        OrdenDePagoVO[] ordenesDePagoActuales = null;
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        CatalogoDAO catDAO = new CatalogoDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        DecisionComiteDAO decisDAO = new DecisionComiteDAO();
        BitacoraUtil bitacora = null;
        int i = 0, j = 0, semDisp = 0, idCliente = 0, idProcentaje = 0;
        double montoIncremento = 0, saldoFondeador = 0.00, porcentaje = 0.0, montoAdicional = 0.0;
        DecimalFormat num = new DecimalFormat("#######.00");
        try {
            synchronized (this) {
                con = ConnectionManager.getMySQLConnection();
                conCar = ConnectionManager.getCWConnection();
                con.setAutoCommit(false);
                conCar.setAutoCommit(false);
                int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                String nombre = "", referencia = "";
                grupoVO = (GrupoVO) session.getAttribute("GRUPO");
                if (idCiclo != 0) {
                    cicloVO = GrupoUtil.getCiclo(grupoVO.ciclos, idCiclo);
                    //cicloOrigVO = GrupoUtil.getCiclo(grupoVO.ciclos, idCiclo);
                }
                SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                Date fechaUltimoCierre = sdf.parse(CatalogoHelper.getParametro("FECHA_CIERRE"));
                semDisp = cicloVO.getAceptaAdicional();
                bitacora = new BitacoraUtil(grupoVO.idGrupo, request.getRemoteUser(), "CommandConfirmaDesembolsoAdicional");
                for (i = 0; cicloVO.integrantes != null && i < cicloVO.integrantes.length; i++) {
                    nombre = "desembolso" + i;
                    idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
                    idProcentaje = HTMLHelper.getParameterInt(request, "porcentaje" + i);
                    if (HTMLHelper.getCheckBox(request, nombre)) {
                        cicloVO.integrantes[i].setIdPorcentajeAdicional(idProcentaje);
                        arrIntegrantes.add(cicloVO.integrantes[i]);
                    }
                }
                myLogger.debug("Procesando las confirmaciones de los integrantes");
                tablaVO = tablaCartDAO.getDivPago(grupoVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), semDisp);
                creditoVO = creditoDAO.getCredito(cicloVO.getIdCreditoIBS());
                if ((cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FOMMUR) || (cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS) || (cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FINAFIM)) {
                    double capitalFondeador = InterCicloHelper.cambiaFondeador(cicloVO.getSaldo(), creditoVO, cicloVO, con, conCar, semDisp, request);
                    myLogger.debug("Saldo capital: " + capitalFondeador);
                    creditoVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                    cicloVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                    cicloVO.getSaldo().setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                }
                for (IntegranteCicloVO integrante : arrIntegrantes) {
                    referencia = ClientesUtil.makeReferenciaAdicional(grupoVO.getSucursal(), integrante.getIdCliente(), integrante.getIdSolicitud());
                    myLogger.debug("referencia generada:" + referencia);
                    ordenesDePagoActuales = new OrdenDePagoDAO().getOrdenesDePago(integrante.getIdCliente(), integrante.getIdSolicitud());
                    referencia = GrupoHelper.modificaReferencia(referencia, ordenesDePagoActuales);

                    myLogger.debug("referencia after modificaReferencia :" + referencia);

                    if (referencia.charAt(12) != '8') {
                        referencia = referencia.substring(0, referencia.length() - 1) + "8";
                    }
                    myLogger.debug("referencia generada modificada :" + referencia);
                    /**
                     * JECB 01/10/2017 Se implementa de regla de negocio que
                     * considere el caso donde un cliente solicite un credito
                     * adicional y previamente haya cancelado uno que ya tenia
                     * asignado, la regla incluye la correcta generacion de las
                     * referencia de pago, de no implementarse se generarian
                     * referencias duplicadas
                     */
                    //Obtenemos todas las ordenes de pago de adicional
                    List<OrdenDePagoVO> listOrdPagAdicional = new ArrayList<OrdenDePagoVO>();

                    for (OrdenDePagoVO tmpOP : ordenesDePagoActuales) {
                        if (tmpOP.getReferencia().substring(12).equals("8")) {
                            listOrdPagAdicional.add(tmpOP);
                        }
                    }

                    if (!listOrdPagAdicional.isEmpty()) {
                        myLogger.debug("Numero de ordenes de pago existententes de adicional:" + listOrdPagAdicional.size());
                        //Se busca la orden de pago 
                        OrdenDePagoVO tmpOP = null;
                        for (java.util.Iterator<OrdenDePagoVO> itr = listOrdPagAdicional.iterator(); itr.hasNext();) {
                            OrdenDePagoVO ordPagBuscado = itr.next();
                            if (ordPagBuscado.getReferencia().equals(referencia)) {
                                tmpOP = ordPagBuscado;
                            }
                        }

                        //Si la referencia generada existe registrada entre las ordenes de pago
                        //se modificara la referencia actual para generar la orden de pago 
                        //previamente se validara que la orden de pago ya se encuentre en un estatus 13
                        //en caso de no existir se continua el proceso actual
                        if (tmpOP != null) {
                            myLogger.debug("Existe Referencia:" + referencia + " registrda en la tabla de orden de pagos");
                            if (tmpOP.getEstatus() != ClientesConstants.OP_DEVUELTA) {
                                myLogger.debug("Existe Referencia:" + referencia + " registrda en la tabla de orden de pagos con estatus dif 13");
                                throw new CommandException("No se puede realizar el desembolso del adicional ya que se cuentan con orden de pago no devueltas");
                            } else {
                                //generamos siguiente referencia decrementando digito significativo
                                String refNew = null;
                                String refPivote = new String(referencia);
                                myLogger.debug("referencia refPivote:" + refPivote);
                                do {
                                    //Actualizamos la lista quitando la orden de pago de la referencia original
                                    //ya que en este punto esa referencia ya esta en estatus 13 devuelto 
                                    ListIterator<OrdenDePagoVO> iter = listOrdPagAdicional.listIterator();
                                    while (iter.hasNext()) {
                                        if (iter.next().getReferencia().equals(refPivote)) {
                                            iter.remove();
                                            break;
                                        }
                                    }
                                    //Generamos nueva referencia modificando el digito
                                    //verificador decrementandolo en el digito verificador
                                    int digito = Integer.parseInt(refPivote.substring(3, 4));
                                    --digito;
                                    refPivote = refPivote.substring(0, 3) + digito + refPivote.substring(4);
                                    myLogger.debug("Nueva referencia refPivote:" + refPivote);
                                    tmpOP = null;
                                    for (java.util.Iterator<OrdenDePagoVO> itr = listOrdPagAdicional.iterator(); itr.hasNext();) {
                                        OrdenDePagoVO ordPagBuscado = itr.next();
                                        if (ordPagBuscado.getReferencia().equals(refPivote)) {
                                            tmpOP = ordPagBuscado;
                                        }
                                    }
                                    if (tmpOP == null) {
                                        refNew = new String(refPivote);
                                    } else if (tmpOP.getEstatus() != ClientesConstants.OP_DEVUELTA) {
                                        myLogger.debug("Existe Referencia:" + referencia + " registrda en la tabla de orden de pagos con estatus dif 13");
                                        throw new CommandException("No se puede realizar el desembolso del adicional ya que se cuentan con orden de pago no devueltas");
                                    } else {
                                        refNew = null;
                                    }

                                } while (refNew == null);
                                referencia = refNew;
                                myLogger.debug("Referencia nueva generada:" + referencia);
                            }
                        }
                    }

                    porcentaje = new CatalogoDAO().getValorProcentajeAdicional(integrante.getIdPorcentajeAdicional());
                    montoAdicional = (integrante.getMonto() * porcentaje);
                    integrante.setMontoAdicional(montoAdicional);
                    integrante.setMonto(integrante.getMonto() + montoAdicional);
                    integrante.setMontoDesembolso(integrante.getMontoDesembolso() + montoAdicional);
                    
                    int resTieneSeguro = new SegurosDAO().contratoSeguro(integrante.idCliente, integrante.idSolicitud);
                    myLogger.debug("resTieneSeguro:" + resTieneSeguro);
                    myLogger.debug("integrante.getMontoConSeguro():" + integrante.getMontoConSeguro());
                    if (resTieneSeguro == 1 && (integrante.getMontoConSeguro() != 0 && integrante.getMontoDesembolso() != integrante.getMontoConSeguro())) {
                        integrante.setMontoConSeguro(integrante.getMontoConSeguro() + montoAdicional);
                    }
                    integrante.setFechaDesembAdicional(fechaUltimoCierre);
                    montoIncremento += montoAdicional;
                    ordenVO = new OrdenDePagoVO();
                    ordenVO.setIdCliente(integrante.getIdCliente());
                    ordenVO.setIdSolicitud(integrante.getIdSolicitud());
                    ordenVO.setIdSucursal(grupoVO.getSucursal());
                    ordenVO.setUsuario(request.getRemoteUser());
                    ordenVO.setNombre(integrante.getNombre().replace("NO PROPORCIONADO", " "));
                    ordenVO.setMonto(montoAdicional);
                    ordenVO.setIdBanco(cicloVO.getBancoDispersion());
                    ordenVO.setReferencia(referencia);
                    ordenVO.setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                    integrante.setOrdenPago(ordenVO);
                    referencia = ChequerasHelper.asignaOrdenDePago(con, ordenVO);
                    decisDAO.updateMontos(con, integrante.idCliente, integrante.idSolicitud, integrante.getMonto());
                }
                saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + cicloVO.getFondeador()));
                myLogger.debug("Nuevo saldo fondeador" + saldoFondeador);
                saldoFondeador -= montoIncremento;
                if (GrupoUtil.procesaIntegrantesConfirmacionDispersionAdicional(con, cicloVO, request, grupoVO.getSucursal(), tablaVO.getFechaPago(), arrIntegrantes, semDisp)) {
                    GrupoHelper.obtenTablaAmortizacionAdicional(grupoVO, cicloVO, montoIncremento, semDisp, ClientesConstants.CICLO_DISPERSADO);
                    creditoVO = creditoDAO.getCredito(cicloVO.getIdCreditoIBS());
                    new TablaAmortHelper().aplicaCapitalGrupoAdicional(con, conCar, creditoVO, cicloVO, grupoVO, montoIncremento, saldoFondeador, request, semDisp);
                    new CicloGrupalDAO().updateCiclo(con, cicloVO.idGrupo, cicloVO.idCiclo, semDisp);
                    bitacoraCiclo.setIdEquipo(grupoVO.idGrupo);
                    bitacoraCiclo.setIdCiclo(idCiclo);
                    bitacoraCiclo.setEstatus(ClientesConstants.CICLO_DISPERSADO);
                    bitacoraCiclo.setIdComentario(bitacoraCicloDAO.getNumComentario(grupoVO.idGrupo, idCiclo) + 1);
                    bitacoraCiclo.setComentario("Adicional dispersado");
                    bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                    bitacoraCiclo.setUsuarioAsignado(request.getRemoteUser());
                    bitacoraCiclo.setSemDisp(semDisp);
                    myLogger.info("Insertando registro de bitácora ciclo");
                    bitacoraCicloDAO.insertaBitacoraCiclo(con, bitacoraCiclo);
                    con.commit();
                    conCar.commit();
                    CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + cicloVO.getFondeador(), num.format(saldoFondeador));
                    /*tablaClieDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_GRUPAL);
                    tablaClieDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
                    for (int k = 0; k < cicloVO.tablaAmortizacion.length; k++) {
                        cicloVO.tablaAmortizacion[k].tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                        tablaClieDAO.addTablaAmortizacion(cicloVO.tablaAmortizacion[k]);
                    }
                    for (int k = 0; k < cicloVO.tablaAmortInterciclo.length; k++) {
                        cicloVO.tablaAmortInterciclo[k].tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                        tablaClieDAO.addTablaAmortizacion(cicloVO.tablaAmortInterciclo[k]);
                    }*/
                    grupoVO.ciclos[idCiclo - 1].setAceptaAdicional(semDisp);
                    grupoVO.ciclos[idCiclo - 1].integrantes = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
                    /**
                     * JECB 01/10/2017 En caso de que al menos un integrante del
                     * ciclo tenga asignado un monto adicional establecemos en
                     * True la propiedades tieneAsignadoCreditoAdicional del
                     * bean de CicloGrupalVO
                     */
                    for (int a = 0; grupoVO.ciclos[idCiclo - 1].integrantes != null && a < grupoVO.ciclos[idCiclo - 1].integrantes.length; a++) {
                        //Verificamos si el ciclo tiene asignado por lo menos un credito adicional
                        //en alguno de sus integrantes
                        if (!grupoVO.ciclos[idCiclo - 1].tieneAsignadoCreditoAdicional && grupoVO.ciclos[idCiclo - 1].integrantes[a].montoAdicional > 0) {
                            grupoVO.ciclos[idCiclo - 1].tieneAsignadoCreditoAdicional = true;
                        }
                    }

                    bitacora.registraEvento(cicloVO);
                    notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Adicional Dispersado"));
                }
            }
            session.setAttribute("GRUPO", grupoVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (conCar != null) {
                    conCar.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }

}

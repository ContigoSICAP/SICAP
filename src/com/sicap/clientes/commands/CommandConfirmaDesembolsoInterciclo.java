package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandConfirmaDesembolsoInterciclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConfirmaDesembolsoInterciclo.class);

    public CommandConfirmaDesembolsoInterciclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupoVO = new GrupoVO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        CicloGrupalVO cicloOrigVO = new CicloGrupalVO();
        ArrayList<IntegranteCicloVO> arrIntegrantes = new ArrayList<IntegranteCicloVO>();
        SolicitudVO solicitudVO = new SolicitudVO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO elementos[] = null;
        TablaAmortVO tablaVO = new TablaAmortVO();        
        CreditoCartVO creditoVO = new CreditoCartVO();
        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        TablaAmortizacionDAO tablaClieDAO = new TablaAmortizacionDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        TablaAmortHelper tablaHelp = new TablaAmortHelper();
        Connection con = null, conCar = null;
        String FondeadorInicial = "";
        int i = 0, j = 0;
        double montoIncremento = 0, montoConSeguro = 0, saldoFondeador = 0.00;
        boolean activo = false;
        DecimalFormat num = new DecimalFormat("#######.00");
        BitacoraUtil bitacora = null;
        Calendar fechaCal = Calendar.getInstance();
        try {
            synchronized (this) {
                con = ConnectionManager.getMySQLConnection();
                conCar = ConnectionManager.getCWConnection();
                con.setAutoCommit(false);
                conCar.setAutoCommit(false);
                java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
                int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                int semDisp = HTMLHelper.getParameterInt(request, "semDisp");
                grupoVO = (GrupoVO) session.getAttribute("GRUPO");
                if (idCiclo != 0) {
                    cicloVO = GrupoUtil.getCiclo(grupoVO.ciclos, idCiclo);
                    cicloOrigVO = GrupoUtil.getCiclo(grupoVO.ciclos, idCiclo);
                }
                bitacora = new BitacoraUtil(grupoVO.idGrupo, request.getRemoteUser(), "CommandConfirmaDesembolsoInterciclo");
                for (i = 0; i < cicloVO.integrantes.length; i++) {
                    arrIntegrantes.add(cicloVO.integrantes[i]);
                }
                for (i = 0; cicloVO.integrantes != null && i < cicloVO.integrantes.length; i++) {
                    String nombre = "desembolso" + i;
                    int idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
                    j = 0;
                    if (i == 0) {
                        arrIntegrantes.remove(j);
                    } else {
                        for (IntegranteCicloVO integrante : arrIntegrantes) {
                            if (integrante.idCliente == idCliente) {
                                if (!HTMLHelper.getCheckBox(request, nombre)) {
                                    if (integrante.getEsInterciclo() == 1 && integrante.semDisp == semDisp) {
                                        solicitudVO = solicitudDAO.getSolicitud(integrante.getIdCliente(), integrante.getIdSolicitud());
                                        solicitudVO.setSubproducto(0);
                                        solicitudVO.setDesembolsado(ClientesConstants.CANCELADO);
                                        solicitudVO.setFechaDesembolso(null);
                                        solicitudVO.setNumCheque(null);
                                        solicitudDAO.updateSolicitud(con, integrante.getIdCliente(), solicitudVO);
                                        integrante.ordenPago = ordenDAO.getOrdenPago(integrante.getIdCliente(), integrante.getIdSolicitud());
                                        integrante.getOrdenPago().setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                        integrante.getOrdenPago().setUsuario(request.getRemoteUser());
                                        integrante.getOrdenPago().setFechaCancelacion(new Timestamp(System.currentTimeMillis()));
                                        ordenDAO.updateOrdenDePagoConfirmaDesembolso(con, integrante.getOrdenPago());
                                        if (integrante.getMedioCobro() == 1) {
                                            integrante.tarjetaCobro = tarjetaDAO.getTarjetaClinete(integrante.getIdCliente(), integrante.getIdSolicitud());
                                            integrante.getTarjetaCobro().setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                            integrante.getTarjetaCobro().setUsuario(request.getRemoteUser());
                                            integrante.getTarjetaCobro().setFechaCancelacion(new Timestamp(System.currentTimeMillis()));
                                            tarjetaDAO.updateTarjetaConfirmaDesembolso(con, integrante.getTarjetaCobro());
                                        }
                                    }
                                    arrIntegrantes.remove(j);
                                    break;
                                } else {
                                    if (!integranteDAO.getIntegranteActivo(integrante.getIdCliente()).equals("")) {
                                        activo = true;
                                    }
                                    // Monto con seguro financiado, para ordenes de pago y tarjetas se queda el monto sin seguro
                                    montoIncremento += integrante.getMonto();
                                    montoConSeguro += integrante.getMontoConSeguro();
                                }
                            }
                            j++;
                        }
                    }
                }
                if (!arrIntegrantes.isEmpty()) {
                    if (!activo) {
                        if (cicloVO.getSaldo().getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                            integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
                            if (InterCicloHelper.validaDevolicionODP(integrantesCicloVO)) {
                                myLogger.debug("Procesando las confirmaciones de los integrantes");
                                tablaVO = tablaCartDAO.getDivPago(grupoVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), semDisp);
                                creditoVO = creditoDAO.getCredito(cicloVO.getIdCreditoIBS());
                                if (InterCicloHelper.validaCambioEstatus(semDisp, ClientesConstants.CICLO_DISPERSADO, grupoVO.idGrupo, cicloVO.idCreditoIBS, fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))) {                                    
                                    if ((cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FOMMUR) || (cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS) ||(cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FINAFIM)) {
                                        double capitalFondeador = InterCicloHelper.cambiaFondeador(cicloVO.getSaldo(), creditoVO, cicloVO, con, conCar, semDisp, request);
                                        myLogger.debug("Saldo capital: "+capitalFondeador);
                                        creditoVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                                        cicloVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                                        cicloVO.getSaldo().setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
                                    }
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + cicloVO.getFondeador()));
                                    myLogger.debug("Nuevo saldo fondeador"+saldoFondeador);
                                    saldoFondeador -= montoConSeguro;
                                    if (GrupoUtil.procesaIntegrantesConfirmacionDispersionInterciclo(con, cicloVO, request, grupoVO.getSucursal(), tablaVO.getFechaPago(), arrIntegrantes, semDisp)) {
                                        GrupoHelper.obtenTablaAmortizacionInterciclo(grupoVO, cicloVO, montoConSeguro, semDisp, ClientesConstants.CICLO_DISPERSADO);
                                        //creditoVO = creditoDAO.getCredito(cicloVO.getIdCreditoIBS());
                                        tablaHelp.aplicaCapitalGrupoInterciclo(con, conCar, creditoVO, cicloVO, grupoVO, montoConSeguro,montoIncremento, saldoFondeador, request, arrIntegrantes.size(), semDisp);
                                        new CicloGrupalDAO().updateEstatusCicloIC(con, grupoVO.idGrupo, idCiclo, ClientesConstants.CICLO_DISPERSADO, semDisp);
                                        if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                            cicloVO.setEstatusIC(ClientesConstants.CICLO_DISPERSADO);
                                        } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                            cicloVO.setEstatusIC2(ClientesConstants.CICLO_DISPERSADO);
                                        }
                                        bitacoraCiclo.setIdEquipo(grupoVO.idGrupo);
                                        bitacoraCiclo.setIdCiclo(idCiclo);
                                        bitacoraCiclo.setEstatus(ClientesConstants.CICLO_DISPERSADO);
                                        bitacoraCiclo.setIdComentario(bitacoraCicloDAO.getNumComentario(grupoVO.idGrupo, idCiclo) + 1);
                                        bitacoraCiclo.setComentario("Inter-Ciclo dispersado");
                                        bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                                        bitacoraCiclo.setUsuarioAsignado(request.getRemoteUser());
                                        bitacoraCiclo.setSemDisp(semDisp);
                                        myLogger.info("Insertando registro de bitácora ciclo");
                                        bitacoraCicloDAO.insertaBitacoraCiclo(con, bitacoraCiclo);
                                        con.commit();
                                        conCar.commit();
                                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + cicloVO.getFondeador(), num.format(saldoFondeador));
                                        tablaClieDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_GRUPAL);
                                        tablaClieDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
                                        for (int k = 0; k < cicloVO.tablaAmortizacion.length; k++) {
                                            cicloVO.tablaAmortizacion[k].tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO;
                                            tablaClieDAO.addTablaAmortizacion(cicloVO.tablaAmortizacion[k]);
                                        }
                                        for (int k = 0; k < cicloVO.tablaAmortInterciclo.length; k++) {
                                            cicloVO.tablaAmortInterciclo[k].tipoAmortizacion = ClientesConstants.AMORTIZACION_GRUPAL;
                                            tablaClieDAO.addTablaAmortizacion(cicloVO.tablaAmortInterciclo[k]);
                                        }
                                        grupoVO.ciclos[idCiclo - 1].integrantes = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
                                        bitacora.registraEvento(cicloVO);
                                        notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Inter-Ciclo Dispersado"));
                                    } else {
                                        integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
                                        elementos = new IntegranteCicloVO[integrantesCicloVO.length + arrIntegrantes.size()];
                                        for (i = 0; i < integrantesCicloVO.length; i++) {
                                            elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                                        }
                                        j = 0;
                                        for (IntegranteCicloVO integrante : arrIntegrantes) {
                                            elementos[i + j] = integrante;
                                            j++;
                                        }
                                        grupoVO.ciclos[idCiclo - 1].integrantes = elementos;
                                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Error al actualizar ordenes de pago"));
                                        con.rollback();
                                        conCar.rollback();
                                    }

                                } else {
                                    integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
                                    elementos = new IntegranteCicloVO[integrantesCicloVO.length + arrIntegrantes.size()];
                                    for (i = 0; i < integrantesCicloVO.length; i++) {
                                        elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                                    }
                                    j = 0;
                                    for (IntegranteCicloVO integrante : arrIntegrantes) {
                                        elementos[i + j] = integrante;
                                        j++;
                                    }
                                    grupoVO.ciclos[idCiclo - 1].integrantes = elementos;
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La fecha actual es diferente al fecha permitida"));
                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                }

                            } else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se puede realizar la Dispercion ya que existen Ordenes de Pago canceladas sin devolver"));
                                request.setAttribute("NOTIFICACIONES", notificaciones);
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El equipo no esta vigente"));
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                        }
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante tiene una solicitud sin liquidar"));
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }

                } else {
                    grupoVO.ciclos[idCiclo - 1].integrantes = cicloVO.integrantes;
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Sin clientes para conformar Inter-Ciclo"));
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
                con.close();
                conCar.close();
                // Calculamos de monto con seguro financiado para Interciclo Agosto 2017
                int integrantesLenght = grupoVO.ciclos[idCiclo - 1].integrantes.length; // evitas un calculo en el for
                for (int k = 0; k < integrantesLenght; k++) {
                    // Calculamos el monto con seguro financiado Agosto 2017
                    IntegranteCicloVO integrante = grupoVO.ciclos[idCiclo - 1].integrantes[k];
                    if (integrante.montoConSeguro == 0) {
                        integrante.montoConSeguro = integrante.monto;
                    }
                    // Solo si tiene un seguro contratado se muestra el costo del seguro
                    if (integrante.segContratado == SeguroConstantes.CONTRATACION_SI) {
                        // Agregamos el costo del seguro Agosto 2017
                        int idSucursal = integrante.idSucursal;
                        int tipoSeguro = integrante.tipoSeguro;
                        SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
                        double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                        integrante.costoSeguro = costoSeguroContratado;
                    }
                }           
                session.setAttribute("GRUPO", grupoVO);
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }
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

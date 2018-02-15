package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TarjetasVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandDesembolsoInterciclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandDesembolsoInterciclo.class);

    public CommandDesembolsoInterciclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupoVO = new GrupoVO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        ArrayList<IntegranteCicloVO> arrIntegrantes = new ArrayList<IntegranteCicloVO>();
        SolicitudVO solicitudVO = new SolicitudVO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO elementos[] = null;
        OrdenDePagoVO ordenVO = new OrdenDePagoVO();
        TarjetasVO tarjetaVO = new TarjetasVO();
        OrdenDePagoVO[] ordenesDePagoActuales = null;
        TablaAmortVO tablaVO = null;
        TablaAmortizacionVO[] tablaAmortInterciclo = null;
        Connection con = null;
        SegurosDAO seguroDAO = new SegurosDAO();
        EmpleoDAO empleoDAO = new EmpleoDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        double montoCapital = 0, saldoFondeador = 0.00;
        int i = 0, j = 0, clieSeguro = 0, fondeador = 0, semActual = 0;
        boolean infoIncompleta = false, activo = false;
        String referencia = "";
        BitacoraUtil bitacora = null;
        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
        try {
            java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int semDisp = HTMLHelper.getParameterInt(request, "semDisp");
            grupoVO = (GrupoVO) session.getAttribute("GRUPO");
            if (idCiclo != 0) {
                cicloVO = GrupoUtil.getCiclo(grupoVO.ciclos, idCiclo);
            }
            bitacora = new BitacoraUtil(grupoVO.idGrupo, request.getRemoteUser(), "CommandDesembolsoInterciclo");
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
                                if (integrante.getEsInterciclo() == 1) {
                                    solicitudVO = solicitudDAO.getSolicitud(integrante.getIdCliente(), integrante.getIdSolicitud());
                                    solicitudVO.setSubproducto(0);
                                    solicitudDAO.updateSolicitud(integrante.getIdCliente(), solicitudVO);
                                }
                                arrIntegrantes.remove(j);
                                break;
                            }
                        }
                        j++;
                    }
                }
            }
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupoVO.idGrupo, idCiclo);
            if (!arrIntegrantes.isEmpty()) {
                cicloVO.integrantesArray = arrIntegrantes;
                for (IntegranteCicloVO integrante : arrIntegrantes) {
                    clieSeguro = seguroDAO.compruebaSeguro(integrante);
                    if (clieSeguro < 1 || empleoDAO.compruebaEmpleo(integrante) == 0) {
                        infoIncompleta = true;
                    }
                    if (!integranteDAO.getIntegranteActivo(integrante.getIdCliente()).equals("")) {
                        activo = true;
                    }
                    // Monto con seguro financiado, para las ordenes de pago se mantiene el monto original Agosto 2017
                    montoCapital += integrante.getMontoConSeguro();
                }
                tablaVO = new TablaAmortDAO().getDivPago(grupoVO.idGrupo, cicloVO.idCreditoIBS, semDisp);
                if (InterCicloHelper.validaDevolicionODP(integrantesCicloVO)) {
                    if (InterCicloHelper.validaCambioEstatus(semDisp, ClientesConstants.CICLO_DESEMBOLSO, grupoVO.idGrupo, cicloVO.idCreditoIBS, fechasInhabiles, Convertidor.toSqlDate(tablaVO.getFechaPago()))) {
                        if (!activo) {
                            if (!infoIncompleta) {
                                if (cicloVO.getFondeador() == ClientesConstants.ID_FONDEADOR_FOMMUR) {
                                    fondeador = FondeadorUtil.asignaFondeador(cicloVO, cicloVO.saldo.getIdSucursal());
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + cicloVO.getFondeador()));
                                } else {
                                    fondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
                                    saldoFondeador = montoCapital;
                                }
                                System.out.println("cicloVO.getFondeador() " + cicloVO.getFondeador() + " fondeador " + fondeador);
                                //if (cicloVO.getFondeador() == fondeador) {                                   
                                    if (cicloVO.getSaldo().getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                                        //if (saldoFondeador >= montoCapital) {
                                            j = 0;
                                            for (IntegranteCicloVO integrante : arrIntegrantes) {
                                                ordenVO = ordenDAO.getOrdenPago(integrante.getIdCliente(), integrante.getIdSolicitud());
                                                if (ordenVO == null) {
                                                    solicitudVO = solicitudDAO.getSolicitud(integrante.getIdCliente(), integrante.getIdSolicitud());
                                                    referencia = ClientesUtil.makeReferencia(grupoVO.getSucursal(), integrante.getIdCliente(), integrante.getIdSolicitud());
                                                    ordenesDePagoActuales = new OrdenDePagoDAO().getOrdenesDePago(integrante.getIdCliente(), integrante.getIdSolicitud());
                                                    referencia = GrupoHelper.modificaReferencia(referencia, ordenesDePagoActuales);
                                                    ordenVO = new OrdenDePagoVO();
                                                    ordenVO.setIdCliente(integrante.getIdCliente());
                                                    ordenVO.setIdSolicitud(integrante.getIdSolicitud());
                                                    ordenVO.setIdSucursal(grupoVO.getSucursal());
                                                    ordenVO.setUsuario(request.getRemoteUser());
                                                    ordenVO.setNombre(integrante.getNombre().replace("NO PROPORCIONADO", " "));
                                                    ordenVO.setMonto(integrante.getMonto());
                                                    ordenVO.setIdBanco(cicloVO.getBancoDispersion());
                                                    ordenVO.setReferencia(referencia);
                                                    ordenVO.setEstatus(ClientesConstants.OP_POR_CONFIRMAR);
                                                    integrante.setOrdenPago(ordenVO);
                                                    referencia = ChequerasHelper.asignaOrdenDePago(con, ordenVO);
                                                    myLogger.info("Cliente: " + ordenVO.getIdCliente() + ", Orden de pago asignada : " + referencia + ", referencia: " + ordenVO.getReferencia());
                                                    if (integrante.getMedioCobro() == 1) {
                                                        referencia = tarjetaDAO.getClienteTarjeta(integrante.getIdCliente(), con);
                                                        tarjetaVO = new TarjetasVO();
                                                        tarjetaVO.setIdCliente(integrante.getIdCliente());
                                                        tarjetaVO.setIdSolicitud(integrante.getIdSolicitud());
                                                        tarjetaVO.setIdSucursal(grupoVO.getSucursal());
                                                        tarjetaVO.setUsuario(request.getRemoteUser());
                                                        tarjetaVO.setNombre(integrante.getNombre().replace("NO PROPORCIONADO", " "));
                                                        tarjetaVO.setMonto(integrante.getMonto());
                                                        tarjetaVO.setBanco(cicloVO.getBancoDispersion());
                                                        tarjetaVO.setTarjeta(referencia);
                                                        tarjetaVO.setEstatus(ClientesConstants.OP_POR_CONFIRMAR);
                                                        ChequerasHelper.asignaTarjeta(con, tarjetaVO, tarjetaDAO);
                                                        integranteDAO.updateTipoCobroIntegrante(con, integrante, integrante.getMedioCobro());
                                                        integrante.setTarjetaCobro(tarjetaVO);
                                                        integrante.setMedioDisp(integrante.getMedioCobro());
                                                        myLogger.info("Cliente: " + tarjetaVO.getIdCliente() + ", Tarjeta asignada : " + referencia);
                                                    }
                                                    solicitudVO.setNumCheque(referencia);
                                                    solicitudVO.setDesembolsado(ClientesConstants.DESEMBOLSADO);
                                                    solicitudVO.setFechaDesembolso(cicloVO.saldo.getFechaSigAmortizacion());
                                                    solicitudDAO.updateSolicitud(con, integrante.getIdCliente(), solicitudVO);
                                                    myLogger.info("Solicitud actualizada");
                                                    arrIntegrantes.get(j).setNumCheque(solicitudVO.getNumCheque());
                                                    arrIntegrantes.get(j).setDesembolsado(solicitudVO.getDesembolsado());
                                                    j++;
                                                } else {
                                                    con.rollback();
                                                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El integrante " + integrante.getNombre() + " ya cuenta con una Orden de Pago"));
                                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                                    break;
                                                }
                                            }
                                            GrupoHelper.obtenTablaAmortizacionInterciclo(grupoVO, cicloVO, montoCapital, semDisp, ClientesConstants.CICLO_DESEMBOLSO);
                                            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                                cicloVO.setEstatusIC(ClientesConstants.CICLO_DESEMBOLSO);
                                            } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                                cicloVO.setEstatusIC2(ClientesConstants.CICLO_DESEMBOLSO);
                                            }
                                            bitacoraCiclo.setIdEquipo(grupoVO.idGrupo);
                                            bitacoraCiclo.setIdCiclo(idCiclo);
                                            bitacoraCiclo.setEstatus(ClientesConstants.CICLO_DESEMBOLSO);
                                            bitacoraCiclo.setIdComentario(bitacoraCicloDAO.getNumComentario(grupoVO.idGrupo, idCiclo) + 1);
                                            bitacoraCiclo.setComentario("Inter-Ciclo desembolsado");
                                            bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                                            if (request.isUserInRole("AUTORIZACION_EQUIPOS_VIP")) {
                                                bitacoraCiclo.setUsuarioAsignado(request.getRemoteUser());
                                            } else {
                                                bitacoraCiclo.setUsuarioAsignado("sistema");
                                            }
                                            bitacoraCiclo.setSemDisp(semDisp);
                                            myLogger.info("Insertando registro de bitácora ciclo");
                                            bitacoraCicloDAO.insertaBitacoraCiclo(con, bitacoraCiclo);
                                            new CicloGrupalDAO().updateEstatusCicloIC(con, grupoVO.idGrupo, idCiclo, ClientesConstants.CICLO_DESEMBOLSO, semDisp);
                                            con.commit();
                                            bitacora.registraEvento(cicloVO);
                                            notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, "Inter-Ciclo Desembolsado"));
                                            request.setAttribute("NOTIFICACIONES", notificaciones);
                                        /*} else {
                                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Saldo insuficiente del fondeador"));
                                            request.setAttribute("NOTIFICACIONES", notificaciones);
                                        }*/
                                    } else {
                                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El equipo no esta no tiene un saldo Vigente"));
                                        request.setAttribute("NOTIFICACIONES", notificaciones);
                                    }
                                /*} else {
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El fondeador del credito es diferente al asignado"));
                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                }*/
                            } else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante no contiene información completa dentro de la solicitud"));
                                request.setAttribute("NOTIFICACIONES", notificaciones);
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante tiene una solicitud sin liquidar"));
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                        }
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La fecha para el desembolso del equipo es Incorrecta"));
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se puede desembolsar el equipo ya que existen Ordenes de Pago canceladas sin devolver"));
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Sin clientes para conformar Inter-Ciclo"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }
            con.close();

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
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}

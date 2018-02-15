package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.LoteChequesDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.LoteChequesVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TarjetasVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

public class CommandDesembolsoGrupal implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandDesembolsoGrupal.class);

    public CommandDesembolsoGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        //Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
        //boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        SolicitudDAO solDAO = new SolicitudDAO();
        BitacoraCicloVO bitacora = new BitacoraCicloVO();
//		CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        ChequesDAO chequeDAO = new ChequesDAO();
        LoteChequesDAO loteDAO = new LoteChequesDAO();
        SucursalDAO sucursaldao = new SucursalDAO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        OrdenDePagoVO oPago = null;
        IntegranteCicloDAO inteDAO = new IntegranteCicloDAO();
        TarjetasVO tarjetaVO = null;
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        Connection conn = null;
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);

            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            int tasa = HTMLHelper.getParameterInt(request, "idTasa");
            int fondeador = HTMLHelper.getParameterInt(request, "fondeador");
            //int idBanco = sucursaldao.getSucursal(idSucursal).idBanco;
            int numIntegrantesChecked = 0;
            int chequesDisponibles = 0;
            int numLote = 0;
            SegurosDAO seguroDAO = new SegurosDAO();
            boolean blnFicha = false;
            boolean sinFicha = false;
            boolean activo = false;
            int clieSeguro = 0;
            String numcheque = "";
            Date fechaDesembolso = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaDesembolsoProg"));
            boolean infoIncompleta = false;
            List<Integer> lstDelIntegrantes = new ArrayList<Integer>();
            // Lista de integrantes sin modificar
            IntegranteCicloVO[] integrantesBckup = null;
            synchronized (this) {
                CicloGrupalVO cicloTemporal = new CicloGrupalDAO().getCiclo(idGrupo, idCiclo);
                if (cicloTemporal.desembolsado != 0) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El ciclo ya se encuentra desembolsado"));
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                } else {
                    grupo = (GrupoVO) session.getAttribute("GRUPO");
                    if (idCiclo != 0) {
                        ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
                        // Para el monto con seguro necesitamos comparar el monto previo del integrante
                        integrantesBckup = Arrays.copyOf(ciclo.integrantes, ciclo.integrantes.length);
                        ciclo.fondeador = fondeador;
                    }
                    int idBanco = ciclo.bancoDispersion;
                    if (ciclo.archivosAsociados != null) {
                        for (ArchivoAsociadoVO archivo : ciclo.archivosAsociados) {
                            if (archivo.tipo == 16) {
                                blnFicha = true;
                            }
                        }
                    } else // Validamos si todos los integrantes cuentan con seguro financiado
                    if (SegurosUtil.validarNumIntegrantesSegFinanciado(ciclo.integrantes)) {
                        // no requiere ficha de deposito, se asume que si tiene ficha para continuar con el flujo
                        blnFicha = true;
                    }
                    for (int i = 0; i < ciclo.integrantes.length; i++) {
                        lstDelIntegrantes.add(ciclo.integrantes[i].idCliente);
                    }
                    GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
                    for (int i = 0; i < ciclo.integrantes.length; i++) {
                        for (int j = 0; j < lstDelIntegrantes.size(); j++) {
                            if (ciclo.integrantes[i].idCliente == lstDelIntegrantes.get(j)) {
                                lstDelIntegrantes.remove(j);
                                integranteDAO.updateRolIntegrante(conn, ciclo.integrantes[i]);
                                break;
                            }
                        }
                    }
                    if (!lstDelIntegrantes.isEmpty()) {
                        for (Integer delIntegrante : lstDelIntegrantes) {
                            integranteDAO.deleteIntegrante(conn, idGrupo, idCiclo, delIntegrante);
                            integranteDAO.deleteIntegranteODS(idGrupo, idCiclo, delIntegrante);
                        }
                    }

                    for (int i = 0; i < ciclo.integrantes.length; i++) {
                        clieSeguro = seguroDAO.compruebaSeguro(ciclo.integrantes[i]);
                        if (clieSeguro < 1 || new EmpleoDAO().compruebaEmpleo(ciclo.integrantes[i]) == 0) {
                            infoIncompleta = true;
                        }
                        if (!blnFicha && clieSeguro == 1) {
                            sinFicha = true;
                        }
                        if (!inteDAO.getIntegranteActivo(ciclo.integrantes[i].idCliente).equals("")) {
                            activo = true;
                        }

                    }

                    if (!activo) {

                        if (!infoIncompleta) {
                            LoteChequesVO[] lotes = loteDAO.getLotesCheques(idSucursal);
                            if (lotes != null && lotes.length > 0) {
                                for (int i = 0; i < lotes.length; i++) {
                                    numLote = lotes[i].idLote;
                                    chequesDisponibles += chequeDAO.getCountCheques(numLote);
                                }
                            }
                            numIntegrantesChecked = ciclo.integrantes.length;
                            if (idBanco > 0 || chequesDisponibles > numIntegrantesChecked) {
                                for (int i = 0; i < ciclo.integrantes.length; i++) {
                                    /*String nombre = "desembolso"+i;
                                         if ( HTMLHelper.getCheckBox(request, nombre) ){*/
                                    SolicitudVO solicitud = new SolicitudVO();
                                    solicitud = solDAO.getSolicitud(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                                    if (grupo.idOperacion == ClientesConstants.GRUPAL) {
                                        synchronized (this) {
                                            if (idBanco > 0) {
                                                String referencia = ClientesUtil.makeReferencia(idSucursal, ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                                                OrdenDePagoVO[] ordenesDePagoActuales = new OrdenDePagoDAO().getOrdenesDePago(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                                                double monto = 0;
                                                if (!GrupoHelper.existeOrdenDePagoActiva(ordenesDePagoActuales)) {
                                                    referencia = GrupoHelper.modificaReferencia(referencia, ordenesDePagoActuales);
                                                    myLogger.info("Referencia modificada");

                                                    monto = ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                                    oPago = new OrdenDePagoVO();

                                                    oPago.setIdCliente(ciclo.integrantes[i].idCliente);
                                                    oPago.setIdSolicitud(ciclo.integrantes[i].idSolicitud);
                                                    oPago.setIdSucursal(idSucursal);
                                                    oPago.setUsuario(request.getRemoteUser());
                                                    oPago.setNombre(ciclo.integrantes[i].nombre.replace("NO PROPORCIONADO", " "));
                                                    oPago.setMonto(monto);
                                                    oPago.setIdBanco(idBanco);
                                                    oPago.setReferencia(referencia);
                                                    oPago.setEstatus(ClientesConstants.OP_POR_CONFIRMAR);
                                                    ciclo.integrantes[i].ordenPago = oPago;
                                                    numcheque = ChequerasHelper.asignaOrdenDePago(conn, oPago);
                                                    myLogger.info("Cliente: " + oPago.getIdCliente() + ", Orden de pago asignada : " + numcheque + ", referencia: " + oPago.getReferencia());

                                                } else {
                                                    conn.rollback();
                                                    myLogger.info("Rollback de transaccion");
                                                    //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El Integrante "+ ciclo.integrantes[i].nombre +" cuenta con una Orden de Pago Activa.");
                                                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El Integrante " + ciclo.integrantes[i].nombre + " cuenta con una Orden de Pago Activa."));
                                                    request.setAttribute("NOTIFICACIONES", notificaciones);
                                                    return siguiente;
                                                }
                                                if (ciclo.integrantes[i].medioCobro == 1) {
                                                    numcheque = tarjetaDAO.getClienteTarjeta(ciclo.integrantes[i].idCliente, conn);
                                                    //monto = ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                                    tarjetaVO = new TarjetasVO();
                                                    tarjetaVO.setIdCliente(ciclo.integrantes[i].idCliente);
                                                    tarjetaVO.setIdSolicitud(ciclo.integrantes[i].idSolicitud);
                                                    tarjetaVO.setIdSucursal(idSucursal);
                                                    tarjetaVO.setUsuario(request.getRemoteUser());
                                                    tarjetaVO.setNombre(ciclo.integrantes[i].nombre.replace("NO PROPORCIONADO", " "));
                                                    tarjetaVO.setMonto(monto);
                                                    tarjetaVO.setBanco(idBanco);
                                                    tarjetaVO.setTarjeta(numcheque);
                                                    tarjetaVO.setEstatus(ClientesConstants.OP_POR_CONFIRMAR);
                                                    ChequerasHelper.asignaTarjeta(conn, tarjetaVO, tarjetaDAO);
                                                    integranteDAO.updateTipoCobroIntegrante(conn, ciclo.integrantes[i], ciclo.integrantes[i].medioCobro);
                                                    ciclo.integrantes[i].tarjetaCobro = tarjetaVO;
                                                    ciclo.integrantes[i].medioDisp = ciclo.integrantes[i].medioCobro;
                                                    myLogger.info("Cliente: " + tarjetaVO.getIdCliente() + ", Tarjeta asignada : " + numcheque);
                                                }
                                            } else {
                                                numcheque = ChequerasHelper.asignaCheque(conn, idSucursal, ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud, idGrupo, idCiclo);
                                            }
                                        }
                                        solicitud.numCheque = numcheque;
                                    }
                                    solicitud.desembolsado = ClientesConstants.DESEMBOLSADO;
                                    solicitud.fechaDesembolso = fechaDesembolso;
                                    solDAO.updateSolicitud(conn, ciclo.integrantes[i].idCliente, solicitud);
                                    myLogger.info("Solicitud actualizada");
                                    ciclo.integrantes[i].numCheque = solicitud.numCheque;
                                    ciclo.integrantes[i].desembolsado = solicitud.desembolsado;
                                    /*}else{
                                         clienteDAO.updateGrupo(ciclo.integrantes[i].idCliente, 0);
                                         integrantesdao.deleteIntegrante(idGrupo, idCiclo, ciclo.integrantes[i].idCliente);
                                         DecisionComiteVO decision = new DecisionComiteDAO().getDecisionComite(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                                         if ( decision.comision != 0)
                                         comision = decision.comision;
                                         numCancelados++;
                                         }*/
                                }
                                //conn.commit();	
                                ciclo.tasa = tasa;
                                //ciclo.integrantes=integrantesdao.getIntegrantes(idGrupo, idCiclo);
                                // Calculamos montos nuevos, montos con seguro en caso de que tenga Agosto 2017
                                for (int i = 0; i < ciclo.integrantes.length; i++) {
                                    // Cuenta con monto con seguro financiado y el monto cambio?
                                    if (integrantesBckup[i].montoConSeguroTemp > 0) {
                                        ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto + integrantesBckup[i].costoSeguro;
                                        ciclo.integrantes[i].montoConSeguroTemp = ciclo.integrantes[i].montoConSeguro;
                                        // Si cambiaron los montos valor anterior valor actual realizamos update
                                        if (integrantesBckup[i].monto != ciclo.integrantes[i].monto) {
                                            // Actualizamos los montos con seguro en las tablas d_integrantes_ciclo y d_decision_comite
                                            int idCliente = ciclo.integrantes[i].idCliente;
                                            int idSolicitud = ciclo.integrantes[i].idSolicitud;
                                            IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                                            integranteCicloDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud, idCiclo);
                                            DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                                            decisionComiteDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud);
                                        }
                                    } else {
                                        // No cuenta con seguro por eso solo se le asignamos el monto
                                        ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto;
                                    }
                                    // Solo si tiene un seguro contratado se muestra el costo del seguro
                                    if (integrantesBckup[i].segContratado == SeguroConstantes.CONTRATACION_SI) {
                                        // Agregamos el costo del seguro Agosto 2017
                                        int tipoSeguro = integrantesBckup[i].tipoSeguro;
                                        SucursalVO sucursalVo = new SucursalDAO().getSucursal(integrantesBckup[i].idSucursal);
                                        double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                                        ciclo.integrantes[i].costoSeguro = costoSeguroContratado;
                                        // como esta parte esta en request mantenemos los datos
                                        ciclo.integrantes[i].segContratado = integrantesBckup[i].segContratado;
                                        ciclo.integrantes[i].idSucursal = integrantesBckup[i].idSucursal;
                                        ciclo.integrantes[i].tipoSeguro = integrantesBckup[i].tipoSeguro;
                                    }
                                }
                                ciclo.monto = 0.0;
                                ciclo.montoConComision = 0.0;
                                for (int i = 0; i < ciclo.integrantes.length; i++) {
                                    ciclo.monto += ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal) + ciclo.integrantes[i].primaSeguro;
                                    // Para el calculo de la tabla de amortizacion necesitamos el monto con seguro y no el monto
                                    ciclo.montoConComision += ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal) + ciclo.integrantes[i].montoConSeguro + ciclo.integrantes[i].montoRefinanciado;
                                }
                                if (ciclo.idTipoCiclo == ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO) {
                                    for (int i = 0; i < ciclo.integrantes.length; i++) {
                                        ciclo.integrantes[i].montoDesembolso = ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                    }
                                }
                                //ciclo.plazo = ciclo.tablaAmortizacion.length - 1;
                                myLogger.info("Usuario: " + request.getRemoteUser() + ", Grupo: " + ciclo.idGrupo + ", ciclo:" + ciclo.idCiclo);
                                GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, fechaDesembolso);

                                //Si se quitaron integrantes del grupo se recalcula la tabla de amortización
                                /*				if ( numCancelados > 0){
                                     double montoTotal = integrantesdao.getMontoTotalCiclo(idGrupo, idCiclo);
                                     TablaAmortizacionVO[] tabla = new TablaAmortizacionDAO().getElementos(idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                                     //int plazo = tabla.length-1;
                                     Date fechaInicio = new Date();
                                     ciclo.tasa = tasa;
                                     if ( tabla!=null && tabla.length>0)
                                     fechaInicio = tabla[0].fechaPago;
                                     GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, fechaInicio); 
                                     tabla = ciclo.tablaAmortizacion;				
                                     }*/
                                //Registro en IBS
                                //if ( GrupoHelper.registerIBS(grupo, ciclo, request, notificaciones) ){
                                //	cicloDAO.updateCicloCredito(ciclo);
                                ciclo.desembolsado = ClientesConstants.CICLO_DESEMBOLSADO;
                                ciclo.estatus = ClientesConstants.CICLO_DESEMBOLSO;
                                ciclo.fechaDispersion = fechaDesembolso;
                                ciclo.diaReunion = HTMLHelper.getParameterInt(request, "diaReunion");

                                /*ASIGNACION DE FONDEADOR AUTOMATICO
                                    ciclo.fondeador = FondeadorUtil.asignaFondeador(ciclo);*/
                                new CicloGrupalDAO().updateCiclo(conn, ciclo);
                                myLogger.info("Ciclo actualizado");
                                conn.commit();
                                myLogger.info("Commit a la transaccion");
                                bitacora.setIdEquipo(idGrupo);
                                bitacora.setIdCiclo(idCiclo);
                                bitacora.setEstatus(ciclo.estatus);
                                bitacora.setIdComentario(bitacoraCicloDao.getNumComentario(idGrupo, idCiclo) + 1);
                                bitacora.setComentario("Equipo desembolsado");
                                bitacora.setUsuarioComentario(request.getRemoteUser());
                                if (request.isUserInRole("AUTORIZACION_EQUIPOS_VIP")) {
                                    bitacora.setUsuarioAsignado(request.getRemoteUser());
                                } else {
                                    bitacora.setUsuarioAsignado("sistema");
                                }
                                myLogger.info("Insertando registro de bitácora ciclo");
                                bitacoraCicloDao.insertaBitacoraCiclo(null, bitacora);
                                BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandDesembolsoGrupal");
                                //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                                bitutil.registraEvento(ciclo);
                                for (int i = 0; i < ciclo.integrantes.length; i++) {
                                    bitutil.registraEvento(ciclo.integrantes[i]);
                                }
                                //Se actualiza en sesión la información generada
                                grupo.ciclos[idCiclo - 1] = ciclo;
                                session.setAttribute("GRUPO", grupo);
                                //}else{
                                //conn.rollback();
                                //}
                                request.setAttribute("NOTIFICACIONES", notificaciones);

                            } else {
                                //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No existen suficientes cheques disponibles para asignar");
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No existen suficientes cheques disponibles para asignar"));
                                request.setAttribute("NOTIFICACIONES", notificaciones);
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante no contiene información completa dentro de la solicitud."));
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                        }

                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Algun integrante tiene una solicitud sin liquidar."));
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                    }
                    conn.close();
                }
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("ClientesDBException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}

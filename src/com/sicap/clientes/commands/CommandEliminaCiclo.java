package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.DevengamientoDAO;
import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandEliminaCiclo implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandEliminaCiclo.class);

    public CommandEliminaCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        CreditoCartVO credito = new CreditoCartVO();
        SaldoIBSVO saldo = new SaldoIBSVO();
        BitacoraCicloVO registroBitacora = new BitacoraCicloVO();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        CreditoCartDAO creditoDao = new CreditoCartDAO();
        OrdenDePagoDAO oPagoDao = new OrdenDePagoDAO();
        CatalogoDAO catalogoDao = new CatalogoDAO();
        TablaAmortizacionDAO tablaClientesDao = new TablaAmortizacionDAO();
        TablaAmortDAO tablaCarteraDao = new TablaAmortDAO();
        ReferenciaGeneralDAO referenciaDao = new ReferenciaGeneralDAO();
        SaldoIBSDAO saldoDao = new SaldoIBSDAO();
        SolicitudDAO solicitudDao = new SolicitudDAO();
        EventoDAO eventoDao = new EventoDAO();
        DevengamientoDAO devengaDAO = new DevengamientoDAO();
        ArchivoAsociadoDAO archivoDao = new ArchivoAsociadoDAO();
        SegurosDAO segurosDao = new SegurosDAO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        PagosReferenciadosHelper pagosHelper = new PagosReferenciadosHelper();
        TransaccionesHelper transaccionesHelper = new TransaccionesHelper();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        boolean procesandoCierre = false;
        myLogger.debug("Variables inicializadas");
        try {
            int idEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            String generar = HTMLHelper.getParameterString(request, "generar");
            String comentario = HTMLHelper.getParameterString(request, "comentario");
            myLogger.debug("GENERAR: "+generar);
            myLogger.debug("Obteniendo ciclo a eliminar");
            ciclo = cicloDao.getCicloEliminacion(idEquipo, idCiclo);
            BitacoraUtil bitacora = new BitacoraUtil(idEquipo, request.getRemoteUser(), "CommandEliminaCicloGrupal");
            if(ciclo.getDesembolsado()==ClientesConstants.CICLO_DESEMBOLSADO){
                myLogger.debug("El ciclo está desembolsado");
                myLogger.debug("Verificando si existen órdenes de pago activas");                
                if(!oPagoDao.diferenteDeDesembolsado(idEquipo, idCiclo)){
                    referenciaDao.deleteReferencia(idEquipo, idCiclo);
                    myLogger.info("Referencia eliminada");
                    tablaClientesDao.delTablaAmortizacion(idEquipo, idCiclo);
                    myLogger.info("Tabla de amortización clientes eliminada");
                    solicitudDao.updateSolicitudCiclo(ClientesConstants.ESTATUS_CAPTURADO, idEquipo, idCiclo);
                    myLogger.info("Solicitudes actualizadas a capturadas");
                    
                    oPagoDao.eliminaOrdenesCiclo(idEquipo, idCiclo, false);
                    myLogger.info("Órdenes de pago desembolsadas eliminadas");
                    cicloDao.deleteCicloGrupal(idEquipo, idCiclo, false);
                    integranteDAO.deleteIntegranteAperturaODS(idEquipo, idCiclo);
                    myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " eliminado por: "+request.getRemoteUser());
                    archivoDao.eliminaArchivoCiclo(idEquipo, idCiclo, false);
                    myLogger.info("Ficha de Seguro eliminada");
                    segurosDao.eliminaSegurosCiclo(idEquipo, idCiclo, false);
                    myLogger.info("Seguros Eliminados");
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se elimina el ciclo desembolsado correctamente"));
                    bitacora.registraEvento(ciclo);
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. Existe una Orden de Pago activa"));
                }
            } else if(ciclo.getDesembolsado()==ClientesConstants.CICLO_DESEMBOLSO_CONFIRMADO){
                myLogger.debug("El ciclo está dispersado");
                myLogger.debug("Verificando permiso para eliminar ciclos dispersados");
                if(request.isUserInRole("ELIMINA_CICLO_DISPERSADO")){
                    procesandoCierre=catalogoDao.ejecutandoCierre();
                    myLogger.debug("Verificando si el cierre de día se encuentra en proceso");
                    if (!procesandoCierre) {
                        myLogger.debug("Obteniendo saldo del ciclo a eliminar");
                        saldo = saldoDao.getSaldo(idEquipo, ciclo.getIdCreditoIBS());
                        myLogger.info("Numero de Credito: "+saldo.getCredito());
                        
                        myLogger.debug("Verificando que no hayan transcurrido semanas del crédito o que el usuario tenga permiso manager");
                        if(saldo.getNumeroCuotasTranscurridas()==0||request.isUserInRole("manager")){
                            myLogger.debug("Verificando que las órdenes de pago se encuentren canceladas");
                            if(!oPagoDao.odpNoCancelada(idEquipo, idCiclo)){
                                myLogger.debug("Obteniendo crédito del ciclo a eliminar");
                                credito = creditoDao.getCreditoClienteSol(idEquipo, idCiclo);
                                myLogger.debug("Verificando que exista el registro de crédito");
                                if(credito!=null&&credito.getNumCredito()!=0){
                                    pagosHelper.transfierePagosIncidencias(saldo.getReferencia());
                                    myLogger.info("Pagos transferidos a incidencias");
                                    referenciaDao.deleteReferencia(idEquipo, idCiclo);
                                    myLogger.info("Referencia eliminada");
                                    
                                    myLogger.info("Respaldando Datos DESEMBOLSO CONFIRMADO");
                                    saldoDao.respaldaSaldosIBS(saldo.getCredito());
                                    creditoDao.respaldaCredito(idEquipo, saldo.getCredito());
                                    tablaCarteraDao.respaldaTablaAmortizacion(idEquipo, saldo.getCredito());
                                    eventoDao.respaldaEventoCiclo(idEquipo, saldo.getCredito());
                                    devengaDAO.respaldaDevengamiento(credito);
                                    myLogger.info("Termina Respaldo Datos DESEMBOLSO CONFIRMADO");
                                    
                                    saldoDao.deleteSaldosIBS(saldo.getCredito(), false);
                                    saldoDao.bajaLogicaSaldosIBS(saldo.getCredito());
                                    myLogger.info("Saldo eliminado");
                                    tablaClientesDao.delTablaAmortizacion(idEquipo, idCiclo);
                                    myLogger.info("Tabla de amortización clientes eliminada");
                                    
                                    creditoDao.eliminaCredito(idEquipo, saldo.getCredito(), false);
                                    creditoDao.bajaLogicaCredito(idEquipo, saldo.getCredito());
                                    myLogger.info("Crédito cartera eliminado");
                                    
                                    tablaCarteraDao.delTablaAmortizacion(idEquipo, saldo.getCredito(), false);
                                    myLogger.info("Tabla de amortización cartera eliminada");
                                    if(!generar.equals("on")){
                                        myLogger.debug("ENTRA A CHECKED");
                                        referenciaDao.deleteReferencia(idEquipo, idCiclo);
                                        myLogger.info("Referencia eliminada");
                                        
                                        myLogger.info("Respaldando datos on");
                                        archivoDao.respaldaArchivoCiclo(idEquipo, idCiclo);
                                        segurosDao.respaldaSegurosCiclo(idEquipo, idCiclo);
                                        cicloDao.respaldaCicloGrupal(idEquipo, idCiclo);
                                        myLogger.info("Termina Respaldo datos on");
                                        
                                        archivoDao.eliminaArchivoCiclo(idEquipo, idCiclo, false);
                                        myLogger.info("Ficha de Seguro eliminada");
                                        
                                        segurosDao.eliminaSegurosCiclo(idEquipo, idCiclo, false);
                                        myLogger.info("Seguros Eliminados");
                                        
                                        cicloDao.deleteCicloGrupal(idEquipo, idCiclo, false);
                                        integranteDAO.deleteIntegranteAperturaODS(idEquipo, idCiclo);
                                        cicloDao.bajaLogicaCicloGrupal(idEquipo, idCiclo);
                                        myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " eliminado por: "+request.getRemoteUser());
                                    } else {
                                        ciclo.setIdCreditoIBS(0);
                                        ciclo.setEstatus(ClientesConstants.CICLO_APERTURA);
                                        ciclo.setDesembolsado(0);
                                        cicloDao.updateCiclo(ciclo);
                                        myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " listo para capturar por: "+request.getRemoteUser());
                                    }
                                    transaccionesHelper.revierteTransaccionesCiclo(idEquipo, saldo.getCredito());
                                    myLogger.info("Contabilidad revertida");
                                    
                                    eventoDao.eliminaEventoCiclo(idEquipo, saldo.getCredito(),false);
                                    //eventoDao.eliminaEventoCiclo(idEquipo, saldo.getCredito(),true);
                                    myLogger.info("Eventos eliminados");
                                    
                                    devengaDAO.deleteDevengamiento(credito, false);
                                    //devengaDAO.deleteDevengamiento(credito, true);
                                    myLogger.info("Devengamiento eliminado");
                                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se elimina el ciclo dispersado correctamente"));
                                    bitacora.registraEvento(ciclo);
                                } else{
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo"));
                                }
                            } else if ((!oPagoDao.odpEnviadas(idEquipo, idCiclo))||(generar.equals("on"))){
                                myLogger.debug("Verificando si hay ODP enviadas o si se generará nuevamente");
                                credito = creditoDao.getCreditoClienteSol(idEquipo, idCiclo);
                                myLogger.debug("Obteniendo crédito del ciclo a eliminar");
                                myLogger.debug("Verificando que exista el registro de crédito");
                                if(credito!=null&&credito.getNumCredito()!=0){
                                    pagosHelper.transfierePagosIncidencias(saldo.getReferencia());
                                    myLogger.info("Pagos transferidos a incidencias");
                                    
                                    myLogger.info("Respaldando datos ciclo desembolsado2");
                                    saldoDao.respaldaSaldosIBS(saldo.getCredito());
                                    oPagoDao.respaldaOrdenesCiclo(idEquipo, idCiclo);
                                    creditoDao.respaldaCredito(idEquipo, saldo.getCredito());
                                    tablaCarteraDao.respaldaTablaAmortizacion(idEquipo, saldo.getCredito());
                                    eventoDao.respaldaEventoCiclo(idEquipo, saldo.getCredito());
                                    devengaDAO.respaldaDevengamiento(credito);
                                    myLogger.info("Termina respaldo datos ciclo desembolsado2");
                                    
                                    saldoDao.deleteSaldosIBS(saldo.getCredito(), false);
                                    saldoDao.bajaLogicaSaldosIBS(saldo.getCredito());
                                    myLogger.info("Saldo eliminado");
                                    tablaClientesDao.delTablaAmortizacion(idEquipo, idCiclo);
                                    myLogger.info("Tabla de amortización clientes eliminada");
                                    solicitudDao.updateSolicitudCiclo(ClientesConstants.ESTATUS_CAPTURADO, idEquipo, idCiclo);
                                    myLogger.info("Solicitudes actualizadas a capturadas");
                                    
                                    oPagoDao.eliminaOrdenesCiclo(idEquipo, idCiclo, false);
                                    myLogger.info("Órdenes de pago eliminadas");
                                    
                                    creditoDao.eliminaCredito(idEquipo, saldo.getCredito(), false);
                                    creditoDao.bajaLogicaCredito(idEquipo, saldo.getCredito());
                                    myLogger.info("Crédito cartera eliminado");
                                    
                                    tablaCarteraDao.delTablaAmortizacion(idEquipo, saldo.getCredito(), false);
                                    myLogger.info("Tabla de amortización cartera eliminada");
                                    if(!generar.equals("on")){  
                                        myLogger.debug("ENTRA A CHECKED");
                                        referenciaDao.deleteReferencia(idEquipo, idCiclo);
                                        myLogger.info("Referencia eliminada");
                                        
                                        myLogger.info("Respaldando datos ciclo desembolsado2 on");
                                        archivoDao.respaldaArchivoCiclo(idEquipo, idCiclo);
                                        segurosDao.respaldaSegurosCiclo(idEquipo, idCiclo);
                                        cicloDao.respaldaCicloGrupal(idEquipo, idCiclo);
                                        myLogger.info("Termina Respaldo datos ciclo desembolsado2 on");
                                        
                                        archivoDao.eliminaArchivoCiclo(idEquipo, idCiclo, false);
                                        myLogger.info("Ficha de Seguro eliminada");
                                        
                                        segurosDao.eliminaSegurosCiclo(idEquipo, idCiclo, false);
                                        myLogger.info("Seguros Eliminados");
                                        
                                        cicloDao.deleteCicloGrupal(idEquipo, idCiclo, false);
                                        integranteDAO.deleteIntegranteAperturaODS(idEquipo, idCiclo);
                                        cicloDao.bajaLogicaCicloGrupal(idEquipo, idCiclo);
                                        myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " eliminado por: "+request.getRemoteUser());
                                    } else {
                                        ciclo.setIdCreditoIBS(0);
                                        ciclo.setEstatus(ClientesConstants.CICLO_APERTURA);
                                        ciclo.setDesembolsado(0);
                                        cicloDao.updateCiclo(ciclo);
                                        myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " listo para capturar por: "+request.getRemoteUser());
                                    }
                                    transaccionesHelper.revierteTransaccionesCiclo(idEquipo, saldo.getCredito());
                                    myLogger.info("Contabilidad revertida");
                                    
                                    eventoDao.eliminaEventoCiclo(idEquipo, saldo.getCredito(), false);
                                    //eventoDao.eliminaEventoCiclo(idEquipo, saldo.getCredito(), true);
                                    myLogger.info("Eventos eliminados");
                                    
                                    devengaDAO.deleteDevengamiento(credito, false);
                                    //devengaDAO.deleteDevengamiento(credito, true);
                                    myLogger.info("Devengamiento eliminado");
                                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se elimina el ciclo dispersado correctamente"));
                                    bitacora.registraEvento(ciclo);
                                } else{
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo"));
                                }                                
                            }
                            else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. Existe una Orden de Pago activa"));
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. Ya transcurrieron "+saldo.getNumeroCuotasTranscurridas()+" semanas"));
                        }
                    } else {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. El cierre de día se encuentra en proceso"));
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. Ya se encuentra en cartera"));
                }
            }else if(ciclo.getIdCreditoIBS()==0){
                myLogger.debug("El ciclo no está ni desembolsado ni dispersado");
                if(!oPagoDao.existeOdpCiclo(idEquipo, idCiclo)){
                    myLogger.info("Referencia eliminada");
                    referenciaDao.deleteReferencia(idEquipo, idCiclo);
                    myLogger.info("Tabla de amortización clientes eliminada");
                    solicitudDao.updateSolicitudCiclo(1, idEquipo, idCiclo);
                    myLogger.info("Solicitudes actualizadas a estatus 1");
                    
                    cicloDao.deleteCicloGrupal(idEquipo, idCiclo, false);
                    integranteDAO.deleteIntegranteAperturaODS(idEquipo, idCiclo);
                    myLogger.info("Equipo "+idEquipo+", Ciclo: "+idCiclo+ " eliminado por: "+request.getRemoteUser());
                    
                    archivoDao.eliminaArchivoCiclo(idEquipo, idCiclo, false);
                    myLogger.info("Ficha de Seguro eliminada");
                    
                    segurosDao.eliminaSegurosCiclo(idEquipo, idCiclo, false);
                    myLogger.info("Seguros Eliminados");
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se elimina el ciclo correctamente"));
                    bitacora.registraEvento(ciclo);
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo. Existe una Orden de Pago activa"));
                }
            } else{
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible eliminar el ciclo"));
            }
            registroBitacora.setComentario(comentario);
            registroBitacora.setEstatus(0);
            registroBitacora.setIdCiclo(idCiclo);
            registroBitacora.setIdComentario(bitacoraCicloDao.getNumComentario(idEquipo, idCiclo)+1);
            registroBitacora.setIdEquipo(idEquipo);
            registroBitacora.setUsuarioAsignado("sistema");
            registroBitacora.setUsuarioComentario(request.getRemoteUser());
            myLogger.info("Insertando registro de bitácora ciclo");
            bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch(ClientesDBException dbe){
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
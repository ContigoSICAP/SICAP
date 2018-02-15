package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DecisionComiteHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SaldosHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.vo.SucursalVO;
import org.apache.log4j.Logger;

public class CommandGuardaDecisionComite implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaDecisionComite.class);

    public CommandGuardaDecisionComite(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        SolicitudVO solicitud = new SolicitudVO();
        DecisionComiteVO decisionComite = new DecisionComiteVO();
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        DecisionComiteDAO decisionComitedao = new DecisionComiteDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
        ArchivoAsociadoDAO archivoAsociadoDao = new ArchivoAsociadoDAO();

        Connection conn = null;
        Calendar calendario = Calendar.getInstance();
        int plazoAutorizado = 0;
        int frecuenciaPago = 0;
        int estatusAntDesembolso = 0;
        String numCheque = "";
        boolean actualizaSolicitud = true;        
        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            ClienteVO clienteActual = (ClienteVO) session.getAttribute("CLIENTE");
            Date[] fechasInhabiles = (Date[]) session.getAttribute("INHABILES");
            int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
            BitacoraUtil bitutil = new BitacoraUtil(clienteActual.idCliente, request.getRemoteUser(), "CommandGuardaDecisionComite");
            solicitud = clienteActual.solicitudes[indiceSolicitud];
            int estatusPrevio = solicitud.estatus;
            estatusAntDesembolso = solicitud.desembolsado;
            numCheque = solicitud.numCheque;
            decisionComite = solicitud.decisionComite;
            TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
            //SaldoIBSVO cicloPendiente = new SaldoIBSVO();
            //cicloPendiente = new IntegranteCicloDAO().getCicloAbierto(clienteActual.idCliente);
            myLogger.info("decisionComite " + decisionComite);
            
            //JECB
            //11/10/2017
            //Declaracion de la variable que almacenara el monto
            //original autorizado
            double montoOriginalAutorizado = 0d;
            
                if (decisionComite == null) {
                    myLogger.info("decisionComite es null");
                    decisionComite = DecisionComiteHelper.getDecisionVO(new DecisionComiteVO(), solicitud, request);
                    // Como ya no se usa el campo monto con comision y se cambio por Monto con seguro financiado en el VO
                    // se asignaba al monto autorizado el monto por comision ya que eran iguales, debido a esto lo siguiente
                    decisionComite.montoAutorizado = decisionComite.montoSinComision;
                    if ((solicitud.tipoOperacion == ClientesConstants.MICROCREDITO && (decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO || (ClientesUtil.clienteCompletoMicro(clienteActual, indiceSolicitud, notificaciones) && SolicitudUtil.cumpleLimites(solicitud, decisionComite, notificaciones, catComisiones, catTasas))))
                            || (solicitud.tipoOperacion == ClientesConstants.CONSUMO && ClientesUtil.clienteCompletoConsumo(clienteActual, indiceSolicitud, notificaciones))
                            || (solicitud.tipoOperacion == ClientesConstants.VIVIENDA && ClientesUtil.clienteCompletoVivienda(clienteActual, indiceSolicitud, notificaciones))
                            || (solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR && ClientesUtil.clienteCompletoConsumo(clienteActual, indiceSolicitud, notificaciones))
                            || solicitud.tipoOperacion == ClientesConstants.GRUPAL || solicitud.tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                        if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO) {
                            decisionComite.montoAutorizado = decisionComite.montoSinComision;
                        }
                        if (solicitud.archivosAsociados != null) {
                            if (solicitud.empleo != null) {
                                decisionComitedao.addDecisionComite(conn, decisionComite);
                                myLogger.info("Agrego el registro en tabla");
                                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                                solicitud.decisionComite = decisionComite;
                                //solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
                                solicitud.estatus = ClientesUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
                                
                                synchronized (this) {
                                    if (!SolicitudHelper.asignaFechaDesembolso(conn, solicitud, clienteActual, request)) {
                                        myLogger.info("Se encontro orden de pago activa");
                                        conn.rollback();
                                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El cliente cuenta con una Orden de Pago Activa."));
                                        request.setAttribute("NOTIFICACIONES", notificaciones);
                                        return siguiente;
                                    }
                                }
                                //Si la solicitud es de consumo se preparan los elementos para calcular tabla de amortización
                                if ((solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR) && (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO)) {
                                    double prima = 0;
                                    double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                    double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                                    TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                                    if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                                        prima = solicitud.seguro.primaTotal;
                                    }
                                    decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                    decisionComitedao.updateDecisionComite(conn, decisionComite);
                                    solicitud.decisionComite = decisionComite;
                                    plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
                                    frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
                                    //Genera la tabla de amortización
                                    FechasUtil.calculaFechaReestructura(solicitud, calendario, fechasInhabiles);
                                    Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, decisionComite.montoAutorizado + primaConComision, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, solicitud.tipoOperacion);
                                    Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor);
                                    Double tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, tasaLogaritmo);
                                    solicitud.cuota = pagoUnitario;
                                    solicitud.tasaCalculada = tasaCalculada;
                                    // Se cambia la insercion de la tabla por semanal JBL-JUN10
                                    // Se cambia la insercion de la tabla global mensual y quincenal para Credito Nomina JBL-OCT10
                                    TablaAmortizacionHelper.insertTablaInsolutoConsumo(clienteActual, solicitud, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
//						TablaAmortizacionHelper.insertTablaInsolutoIndivSemanal(clienteActual, solicitud, decisionComite.montoAutorizado+primaConComision, montoSinComision+prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
//						TablaAmortizacionHelper.insertTablaConsumo(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal , decisionComite.montoAutorizado, montoSinComision, plazoAutorizado, frecuenciaPago, tasa.valor, calendario.getTime());
                                    solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                                }
                                if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO && solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO) {
                                    double prima = 0;
                                    double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                                    if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                                        prima = solicitud.seguro.primaTotal;
                                    }
                                    TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                                    double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                    decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                    decisionComitedao.updateDecisionComite(conn, decisionComite);
                                    //Genera la tabla de amortización
                                    FechasUtil.calculaFechaReestructura(solicitud, calendario, fechasInhabiles);
                                    Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, decisionComite.montoAutorizado + primaConComision, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, solicitud.tipoOperacion);
                                    //Double tasaLogaritmo = 	TablaAmortizacionHelper.getTasaLogaritmico (decisionComite.montoAutorizado+primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor);
                                    Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmicoMicro(decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor, solicitud.tipoOperacion);
                                    Double tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, tasaLogaritmo);
                                    solicitud.cuota = pagoUnitario;
                                    solicitud.tasaCalculada = tasaCalculada;
                                    TablaAmortizacionHelper.insertTablaInsolutoMicro(clienteActual, solicitud, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
                                    solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                                }
                                if (solicitud.tipoOperacion == ClientesConstants.VIVIENDA && solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO) {
                                    double prima = 0;
                                    double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                                    if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                                        prima = solicitud.seguro.primaTotal;
                                    }
                                    double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                    Double tasaAnual = 0.0;
                                    TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                                    tasaAnual = tasa.valor;
                                    decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                    decisionComitedao.updateDecisionComite(conn, decisionComite);
                                    solicitud.decisionComite = decisionComite;
                                    plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
                                    frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
                                    TablaAmortizacionHelper.insertTablaVivienda(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, plazoAutorizado, frecuenciaPago, tasaAnual, calendario.getTime());
                                    solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                                }
                                if (solicitud.tipoOperacion != ClientesConstants.GRUPAL && solicitud.tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL) {
                                    pagoReferenciaVO.referencia = ClientesUtil.makeReferencia(clienteActual, idSolicitud);
                                    pagoReferenciaVO.numcliente = clienteActual.idCliente;
                                    pagoReferenciaVO.numSolicitud = idSolicitud;
                                    pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
                                    solicitud.referencia = pagoReferenciaVO.referencia;
                                } else {
                                    if (clienteActual.idGrupo != 0) {
                                        if (decisionComite.tasa == 0 && decisionComite.comision == 0) {
                                            myLogger.info("Grupo sin tasa ni comision");
                                            GrupoVO grupocliente = new GrupoVO();
                                            grupocliente = new GrupoDAO().getGrupo(clienteActual.idGrupo);
                                            grupocliente.ciclos = new CicloGrupalDAO().getCiclos(clienteActual.idGrupo);
                                            myLogger.info("Obteniendo grupo y ciclos");
                                            //if(grupocliente.ciclos.length<1){
                                            if (grupocliente.ciclos == null) {
                                                myLogger.info("Grupo nuevo");
                                                decisionComite.tasa = GrupoUtil.asignaTasaGrupal(grupocliente, true);
                                                //decisionComite.comision=GrupoUtil.asignaComicionGrupal(grupocliente.calificacion,true);
                                                decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, true, ScoringUtil.getCalificacionCirculo(solicitud.infoCreditoCirculo));
                                                //Guaurda Datos
                                                double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                                decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                                decisionComitedao.updateDecisionComite(conn, decisionComite);
                                                myLogger.info("Actualizo desicion");
                                                solicitud.decisionComite = decisionComite;
                                                //Termina
                                            } else /*if (!GrupoUtil.tieneCicloActivo(grupocliente))*/ {
                                                myLogger.info("Grupo con ciclos");
                                                decisionComite.tasa = GrupoUtil.asignaTasaGrupal(grupocliente, false);
                                                if (GrupoUtil.clienteExisteCicloAnterior(grupocliente, clienteActual)) {
                                                    decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, false);
                                                } else {
                                                    decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, true, ScoringUtil.getCalificacionCirculo(solicitud.infoCreditoCirculo));
                                                }
                                                //Guarda Datos
                                                double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                                decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                                decisionComitedao.updateDecisionComite(conn, decisionComite);
                                                myLogger.info("Actualizo desicion");
                                                solicitud.decisionComite = decisionComite;
                                                //Termina
                                            }/* else {
                                             //notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"El cliente cuenta con una solicitud activa dentro del grupo "+cicloPendiente.getIdClienteSICAP()+" "+cicloPendiente.getNombreCliente()+" ciclo " +cicloPendiente.getIdSolicitudSICAP()+" pendiente por cerrar") );
                                             actualizaSolicitud = false;
                                             }*/

                                        } else {
                                            //Guarda Datos
                                            myLogger.info("Grupo con tasa y/o comision");
                                            double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                            decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                            decisionComitedao.updateDecisionComite(conn, decisionComite);
                                            myLogger.info("Actualizo desicion");
                                            solicitud.decisionComite = decisionComite;
                                            //Termina
                                        }
                                    } else {
                                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Cliente no pertenece a un grupo"));
                                        actualizaSolicitud = false;
                                    }
                                }
                                solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
                                myLogger.info("Actualizo desicion");
                                conn.commit();
                                myLogger.info("Commit de transaccion");
                                bitutil.registraEvento(solicitud);
                                bitutil.registraEvento(decisionComite);
                                bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
                            } else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Para autorizar la solicitud se requiere la sección Actividad Económica"));
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Para autorizar la solicitud se requiere la sección Archivos Asociados"));

                        }
                    }
                } else {
                    myLogger.info("Actualizacion de registro");
                    montoOriginalAutorizado = new DecisionComiteDAO().getMontoAutorizado(clienteActual.idCliente, idSolicitud);
                    decisionComite = DecisionComiteHelper.getDecisionVO(decisionComite, solicitud, request);
                    // Como ya no se usa el campo monto con comision y se cambio por Monto con seguro financiado en el VO
                    // se asignaba al monto autorizado el monto por comision ya que eran iguales, debido a esto lo siguiente
                    decisionComite.montoAutorizado = decisionComite.montoSinComision;
                    
                    /*
                     if ( HTMLHelper.getParameterInt(request, "desembolsado")==ClientesConstants.DESEMBOLSADO && solicitud.seguro==null ){
                     notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"Debe capturar la sección Seguro de vida antes de realizar el desembolso") );
                     }else{*/
                    //Asignación de cheque para los productos: MicroCrédito
                    solicitud.decisionComite = decisionComite;
                    //solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(clienteActual, indiceSolicitud);
                    solicitud.estatus = SolicitudUtil.asignaEstatusSolicitud(solicitud, indiceSolicitud);
                    
                    solicituddao.updateSolicitud(null, clienteActual.idCliente, solicitud);
                    synchronized (this) {
                        if (!SolicitudHelper.asignaFechaDesembolso(conn, solicitud, clienteActual, request)) {
                            myLogger.info("Cliente con orden de pago activa");
                            conn.rollback();
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El cliente cuenta con una Orden de Pago Activa."));
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                            return siguiente;
                        }
                    }
                    //solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
                    //Si la solicitud es de consumo se preparan los elementos para calcular tabla de amortización
                    TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
                    if ((solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR) && (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO)) {
                        double prima = 0;
                        double primaConComision = 0;
                        double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                        if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                            myLogger.info("Calculando prima");
                            prima = solicitud.seguro.primaTotal;
                            SeguroHelper.calculaPrimaTotal(solicitud);
                            if (prima != solicitud.seguro.primaTotal) {
                                new SegurosDAO().updateSeguro(conn, solicitud.seguro);
                                myLogger.info("Actualizo seguro");
                                prima = solicitud.seguro.primaTotal;
                            }
                            primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                        }
                        FechasUtil.calculaFechaReestructura(solicitud, calendario, fechasInhabiles);
                        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                        decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                        decisionComitedao.updateDecisionComite(conn, decisionComite);
                        myLogger.info("Actualizo desicion");
                        solicitud.decisionComite = decisionComite;
                        plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
                        frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
                        //Borra la información de la tabla que se generá previamente
                        myLogger.info("Usuario: " + request.getRemoteUser() + ", cliente: " + solicitud.idCliente + ", solicitud: " + solicitud.idSolicitud);
                        tablaDAO.delTablaAmortizacion(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        //Genera la tabla de amortización
                        Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, decisionComite.montoAutorizado + primaConComision, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, solicitud.tipoOperacion);
                        Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor);
                        Double tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, tasaLogaritmo);
                        solicitud.cuota = pagoUnitario;
                        solicitud.tasaCalculada = tasaCalculada;
//						TablaAmortizacionHelper.insertTablaInsolutoConsumo(clienteActual, solicitud, decisionComite.montoAutorizado+primaConComision, montoSinComision+prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
                        TablaAmortizacionHelper.insertTablaInsolutoIndivSemanal(clienteActual, solicitud, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                    }
                    if (solicitud.tipoOperacion == ClientesConstants.MICROCREDITO && solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO) {
                        if (SolicitudUtil.cumpleLimites(solicitud, decisionComite, notificaciones, catComisiones, catTasas)) {
                            double prima = 0;
                            double primaConComision = 0;
                            if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                                prima = solicitud.seguro.primaTotal;
                                SeguroHelper.calculaPrimaTotal(solicitud);
                                if (prima != solicitud.seguro.primaTotal) {
                                    new SegurosDAO().updateSeguro(conn, solicitud.seguro);
                                    prima = solicitud.seguro.primaTotal;
                                }
                                primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                            }
                            FechasUtil.calculaFechaReestructura(solicitud, calendario, fechasInhabiles);
                            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                            double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                            decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                            decisionComitedao.updateDecisionComite(conn, decisionComite);
                            //Genera la tabla de amortización
                            myLogger.info("_______________________________" + request.getRemoteUser() + " CommandGuardaDecisionComite/MICROCREDITO" + " solicitud.idCliente " + solicitud.idCliente + " solicitud.idSolicitud " + solicitud.idSolicitud);
                            tablaDAO.delTablaAmortizacion(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                            //Genera la tabla de amortización
                            Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, decisionComite.montoAutorizado + primaConComision, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, solicitud.tipoOperacion);
                            //Double tasaLogaritmo = 	TablaAmortizacionHelper.getTasaLogaritmico (decisionComite.montoAutorizado+primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor);
                            Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmicoMicro(decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, tasa.valor, solicitud.tipoOperacion);
                            Double tasaCalculada = TablaAmortizacionHelper.calcTasa(solicitud.tipoOperacion, decisionComite.montoAutorizado + primaConComision, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), clienteActual.idSucursal, tasaLogaritmo);
                            solicitud.cuota = pagoUnitario;
                            solicitud.tasaCalculada = tasaCalculada;
                            TablaAmortizacionHelper.insertTablaInsolutoMicro(clienteActual, solicitud, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, pagoUnitario, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), tasaCalculada);
                            solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        } else {
                            actualizaSolicitud = false;
                        }
                    }
                    if (solicitud.tipoOperacion == ClientesConstants.VIVIENDA && solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && decisionComite.decisionComite != ClientesConstants.CREDITO_RECHAZADO) {
                        double prima = 0;
                        double primaConComision = 0;
                        if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
                            prima = solicitud.seguro.primaTotal;
                            SeguroHelper.calculaPrimaTotal(solicitud);
                            if (prima != solicitud.seguro.primaTotal) {
                                new SegurosDAO().updateSeguro(conn, solicitud.seguro);
                                prima = solicitud.seguro.primaTotal;
                            }
                            primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                        }
                        double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                        Double tasaAnual = 0.0;
                        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(decisionComite.tasa);
                        tasaAnual = tasa.valor;
                        decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                        decisionComitedao.updateDecisionComite(conn, decisionComite);
                        solicitud.decisionComite = decisionComite;
                        plazoAutorizado = Integer.parseInt(request.getParameter("plazoAutorizado"));
                        frecuenciaPago = Integer.parseInt(request.getParameter("frecuenciaPago"));
                        //Borra la información de la tabla que se generó previamente
                        myLogger.info("_______________________________" + request.getRemoteUser() + " CommandGuardaDecisionComite/VIVIENDA" + " solicitud.idCliente " + solicitud.idCliente + " solicitud.idSolicitud " + solicitud.idSolicitud);
                        tablaDAO.delTablaAmortizacion(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                        //Genera la nueva tabla de amortización						
                        TablaAmortizacionHelper.insertTablaVivienda(clienteActual.idCliente, idSolicitud, clienteActual.idSucursal, decisionComite.montoAutorizado + primaConComision, montoSinComision + prima, plazoAutorizado, frecuenciaPago, tasaAnual, calendario.getTime());
                        solicitud.amortizacion = new TablaAmortizacionDAO().getElementos(clienteActual.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                    }
//	JBL NOV/10 estaba dando error debido a que la conexion estaba tomada
//					if ( decisionComite.decisionComite==ClientesConstants.CREDITO_RECHAZADO ){
//						decisionComitedao.updateDecisionComite(conn, decisionComite);
//					}
                    if (solicitud.tipoOperacion == ClientesConstants.GRUPAL || solicitud.tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                        //Agregar validacion de archiovs
                        if (archivoAsociadoDao.tieneArchivo(clienteActual.idCliente, idSolicitud)) {
                            if (clienteActual.idGrupo != 0) {
                                GrupoVO grupocliente = new GrupoVO();
                                grupocliente = new GrupoDAO().getGrupo(clienteActual.idGrupo);
                                grupocliente.ciclos = new CicloGrupalDAO().getCiclos(clienteActual.idGrupo);
                                myLogger.info("Obteniendo grupo y ciclos");
                                if (decisionComite.tasa == 0 && decisionComite.comision == 0) {
                                    if (grupocliente.ciclos == null) {
                                        myLogger.info("Grupo sin ciclos");
                                        decisionComite.tasa = GrupoUtil.asignaTasaGrupal(grupocliente, true);
                                        //decisionComite.comision=GrupoUtil.asignaComicionGrupal(grupocliente.calificacion,true);
                                        decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, true, ScoringUtil.getCalificacionCirculo(solicitud.infoCreditoCirculo));
                                        //Gaurda DAtos
                                        double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                        decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                        decisionComitedao.updateDecisionComite(conn, decisionComite);
                                        myLogger.info("Actualizo desicion");
                                        solicitud.decisionComite = decisionComite;
                                        //Termina
                                    } else/* if(!GrupoUtil.tieneCicloActivo(grupocliente))*/ {
                                        myLogger.info("Grupo tiene ciclos");
                                        decisionComite.tasa = GrupoUtil.asignaTasaGrupal(grupocliente, false);
                                        if (GrupoUtil.clienteExisteCicloAnterior(grupocliente, clienteActual)) {
                                            decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, false);
                                        } else //decisionComite.comision=GrupoUtil.asignaComicionGrupal(grupocliente.calificacion,true);
                                        {
                                            decisionComite.comision = GrupoUtil.asignaComisionGrupal(grupocliente, true, ScoringUtil.getCalificacionCirculo(solicitud.infoCreditoCirculo));
                                        }
                                        //Gaurda DAtos
                                        double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                        decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                        decisionComitedao.updateDecisionComite(conn, decisionComite);
                                        myLogger.info("Actualizo desicion");
                                        solicitud.decisionComite = decisionComite;
                                        //Termina
                                    }/*else{
                                     //notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"El cliente cuenta con una solicitud activa dentro del grupo "+cicloPendiente.getIdClienteSICAP()+" "+cicloPendiente.getNombreCliente()+" ciclo " +cicloPendiente.getIdSolicitudSICAP()+" pendiente por cerrar") );
                                     actualizaSolicitud = false;
                                     System.out.println("NO SE ACTUALIZA");
                                     }*/

                                } else {
                                    //Guarda DAtos
                                    myLogger.info("Grupo con tasa y comision");
                                    double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                                    decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(montoSinComision, decisionComite.comision, catComisiones);
                                    decisionComitedao.updateDecisionComite(decisionComite);
                                    myLogger.info("Actualizo desicion");
                                    //Verifico si existe en ciclo actual activo
                                    if (GrupoUtil.clienteExisteCicloActual(conn, clienteActual.idGrupo, clienteActual.idCliente, grupocliente.ciclos, decisionComite, grupocliente)) {
                                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "La tabla de amortización se ha recalculado. Consulte grupo y ciclo"));
                                    }
                                    solicitud.decisionComite = decisionComite;
                                    //Termina
                                }
                            } else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Cliente no pertenece a un grupo"));
                                actualizaSolicitud = false;
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible autorizar ya que el cliente no cuenta con el archivo de la solicitud"));
                            actualizaSolicitud = false;
                        }
                    }
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO && solicitud.reestructura == 0 && solicitud.numCheque.equalsIgnoreCase("0")) {
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron cheques para asignar"));
                        solicitud.desembolsado = estatusAntDesembolso;
                        solicitud.numCheque = numCheque;
                        /* 
                         * Cambio para que no envie la notificacin a IBS y registre el credito en la tabla de creditos JBL-10
                         */

                    } else if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO && actualizaSolicitud && CreditoHelperCartera.registraCreditoCartera(solicitud, clienteActual, request, notificaciones)) {
//					}else if ( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO && actualizaSolicitud && CreditoHelperCartera.registraCreditoCartera( solicitud, clienteActual, request, notificaciones ) ){
                        double montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
                        double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
                        TablaAmortHelper.insertTablaInsolutoMicro(clienteActual, solicitud, decisionComite.montoAutorizado + primaConComision, montoSinComision + solicitud.seguro.primaTotal, solicitud.cuota, decisionComite.plazoAutorizado, decisionComite.frecuenciaPago, Convertidor.dateToString(calendario.getTime()), solicitud.tasaCalculada);
                        /*
                         * Se registra el Evento en la tabla de eventos JBL MAY10
                                            
                         */
                        EventoHelper.registraDesembolso(solicitud, clienteActual, request, notificaciones);
                        myLogger.info("Registro desembolso");
                        SaldosHelper.insertSaldo(solicitud, clienteActual, request, notificaciones);
                        myLogger.info("Inserto saldo");
                        solicituddao.updateSolicitud(conn, clienteActual.idCliente, solicitud);
                        myLogger.info("Actualizo solicitud");
                        conn.commit();
                        bitutil.registraEvento(solicitud);
                        bitutil.registraEvento(decisionComite);
                        bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
                    }else if (solicitud.tipoOperacion == ClientesConstants.GRUPAL && solicitud.desembolsado != ClientesConstants.DESEMBOLSADO){
                        conn.commit();
                        bitutil.registraEvento(solicitud);
                        bitutil.registraEvento(decisionComite);
                        bitutil.decideInsercionCambioEstatus(estatusPrevio, solicitud, request);
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));           
                    }else {
                        solicitud.desembolsado = estatusAntDesembolso;
                        conn.rollback();                        
                    }
                }
            
            // Validacion para la actualizacion del monto con seguro financiado
            if (actualizaSolicitud) {
                DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                //JECB 11/10/2017
                //Se comenta la linea que obtenia el monto original 
                //ya que para este punto el monto ya fue autorizado en la base y
                //corresponde con el mismo que hay en la forma
                //double montoOriginalAutorizado = decisionComiteDAO.getMontoAutorizado(clienteActual.idCliente, idSolicitud);
                myLogger.debug("monto original autorizado:"+montoOriginalAutorizado );
                myLogger.debug("monto  autorizado:"+decisionComite.montoAutorizado );
                // Si es diferente se calcula el monto con seguro
                if (decisionComite.montoAutorizado != montoOriginalAutorizado) {
                    SucursalVO sucursalVo = new SucursalDAO().getSucursal(clienteActual.idSucursal);
                    double costoSeguroContratado = SeguroHelper.getCostoSeguro(solicitud.seguro.modulos, sucursalVo);
                    double montoConSeguro = solicitud.decisionComite.montoAutorizado + costoSeguroContratado;
                    IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                    integranteCicloDAO.updateMontoConSeguro(conn, montoConSeguro, clienteActual.idCliente, idSolicitud, solicitud.idCiclo);
                    decisionComiteDAO.updateMontoConSeguro(conn, montoConSeguro, clienteActual.idCliente, idSolicitud);
                    decisionComite.montoConSeguro = montoConSeguro;
                    conn.commit();
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            //Actualiza la solicitud del objeto cliente
            clienteActual.solicitudes[indiceSolicitud] = solicitud;
            //Actualiza el cliente en sesion
            session.setAttribute("CLIENTE", clienteActual);
        } catch (ClientesDBException dbe) {
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
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }

}

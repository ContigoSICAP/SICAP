package com.sicap.clientes.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.commands.Command;
import com.sicap.clientes.commands.CommandActualizaCiclo;
import com.sicap.clientes.commands.CommandActualizaCicloApertura;
import com.sicap.clientes.commands.CommandActualizaDatosCredito;
import com.sicap.clientes.commands.CommandActualizaSeguros;
import com.sicap.clientes.commands.CommandActualizaUsuario;
import com.sicap.clientes.commands.CommandActualizacionDocumentos;
import com.sicap.clientes.commands.CommandActualizaclientesNuevos;
import com.sicap.clientes.commands.CommandAgregaComentarioCiclo;
import com.sicap.clientes.commands.CommandAltaIncidenciaBuroInterno;
import com.sicap.clientes.commands.CommandAperturaNuevoCiclo;
import com.sicap.clientes.commands.CommandArrendatarioDomicilio;
import com.sicap.clientes.commands.CommandArrendatarioLocal;
import com.sicap.clientes.commands.CommandCambiaEstatusCiclo;
import com.sicap.clientes.commands.CommandAutorizaSellFinance;
import com.sicap.clientes.commands.CommandBuscaCP;
import com.sicap.clientes.commands.CommandBuscaClienteParaIncidencia;
import com.sicap.clientes.commands.CommandBuscaClientePorRFC;
import com.sicap.clientes.commands.CommandBuscaGrupos;
import com.sicap.clientes.commands.CommandBuscaHistorialComentarios;
import com.sicap.clientes.commands.CommandBuscaIncidenciaBuroInterno;
import com.sicap.clientes.commands.CommandBuscaLocalidad;
import com.sicap.clientes.commands.CommandCancelaCheques;
import com.sicap.clientes.commands.CommandCancelaChequesConfirmados;
import com.sicap.clientes.commands.CommandCicloGrupalAutorizado;
import com.sicap.clientes.commands.CommandCierraCiclo;
import com.sicap.clientes.commands.CommandClienteCapturaRapida;
import com.sicap.clientes.commands.CommandConfirmaDesembolsoAdicional;
import com.sicap.clientes.commands.CommandConfirmaDesembolsoGrupal;
import com.sicap.clientes.commands.CommandConfirmaDesembolsoInterciclo;
import com.sicap.clientes.commands.CommandDesembolsoInterciclo;
import com.sicap.clientes.commands.CommandConsultaBuro;
import com.sicap.clientes.commands.CommandConsultaChequeras;
import com.sicap.clientes.commands.CommandConsultaCheques;
import com.sicap.clientes.commands.CommandConsultaCicloGrupal;
import com.sicap.clientes.commands.CommandConsultaCirculo;
import com.sicap.clientes.commands.CommandConsultaCliente;
import com.sicap.clientes.commands.CommandConsultaEstadoCuenta;
import com.sicap.clientes.commands.CommandConsultaEstadoCuentaGrupal;
import com.sicap.clientes.commands.CommandConsultaEstatusCiclo;
import com.sicap.clientes.commands.CommandConsultaGrupoMonitor;
import com.sicap.clientes.commands.CommandConsultaInformeCobranza;
import com.sicap.clientes.commands.CommandConsultaInformeCobranzaGrupal;
import com.sicap.clientes.commands.CommandConsultaInformeVisita;
import com.sicap.clientes.commands.CommandConsultaInterCiclo;
import com.sicap.clientes.commands.CommandConsultaMasivaCC;
import com.sicap.clientes.commands.CommandConsultaPagosManuales;
import com.sicap.clientes.commands.CommandConsultaPagosManualesGrupal;
import com.sicap.clientes.commands.CommandConsultaReportes;
import com.sicap.clientes.commands.CommandConsultaSolicitudCliente;
import com.sicap.clientes.commands.CommandConsultaSolicitudes;
import com.sicap.clientes.commands.CommandConsultarPagosComunales;
import com.sicap.clientes.commands.CommandControlPagos;
import com.sicap.clientes.commands.CommandDesembolsoGrupal;
import com.sicap.clientes.commands.CommandDesembolsoRestructura;
import com.sicap.clientes.commands.CommandEliminaAlertaMonitor;
import com.sicap.clientes.commands.CommandGeneraAmortizacion;
import com.sicap.clientes.commands.CommandGeneraRFC;
import com.sicap.clientes.commands.CommandGuardaAperturaCiclo;
import com.sicap.clientes.commands.CommandGuardaArrendatario;
import com.sicap.clientes.commands.CommandGuardaAutorizacionMaxZapatos;
import com.sicap.clientes.commands.CommandGuardaCambiosCliente;
import com.sicap.clientes.commands.CommandGuardaCapacidadPago;
import com.sicap.clientes.commands.CommandGuardaChequera;
import com.sicap.clientes.commands.CommandGuardaCicloGrupal;
import com.sicap.clientes.commands.CommandGuardaCicloRestructura;
import com.sicap.clientes.commands.CommandGuardaConyuge;
import com.sicap.clientes.commands.CommandGuardaCreditoSolicitado;
import com.sicap.clientes.commands.CommandGuardaDatosAbreviada;
import com.sicap.clientes.commands.CommandGuardaDatosCliente;
import com.sicap.clientes.commands.CommandGuardaDatosCredito;
import com.sicap.clientes.commands.CommandGuardaDatosCreditoVivienda;
import com.sicap.clientes.commands.CommandGuardaDatosObligadoSolidario;
import com.sicap.clientes.commands.CommandGuardaDecisionComite;
import com.sicap.clientes.commands.CommandGuardaDisposiciones;
import com.sicap.clientes.commands.CommandGuardaEconomiaObligado;
import com.sicap.clientes.commands.CommandGuardaEmpleo;
import com.sicap.clientes.commands.CommandGuardaExpedienteComunal;
import com.sicap.clientes.commands.CommandGuardaExpedienteOperativoLegal;
import com.sicap.clientes.commands.CommandGuardaGrupo;
import com.sicap.clientes.commands.CommandGuardaInformacionFinanciera;
import com.sicap.clientes.commands.CommandGuardaInformeCobranza;
import com.sicap.clientes.commands.CommandGuardaInformeVisitaGrupal;
import com.sicap.clientes.commands.CommandGuardaLimitesCredito;
import com.sicap.clientes.commands.CommandGuardaNegocio;
import com.sicap.clientes.commands.CommandGuardaOtrosDatos;
import com.sicap.clientes.commands.CommandGuardaOtrosDomicilios;
import com.sicap.clientes.commands.CommandGuardaPropuestaComite;
import com.sicap.clientes.commands.CommandGuardaReferenciaComercial;
import com.sicap.clientes.commands.CommandGuardaReferenciaLaboral;
import com.sicap.clientes.commands.CommandGuardaReferenciaPersonal;
import com.sicap.clientes.commands.CommandGuardaReferenciasCrediticias;
import com.sicap.clientes.commands.CommandGuardaRefinanciamientoGrupal;
import com.sicap.clientes.commands.CommandGuardaRenovacionCicloGrupal;
import com.sicap.clientes.commands.CommandGuardaVivienda;
import com.sicap.clientes.commands.CommandIgnorarAlertasFuturas;
import com.sicap.clientes.commands.CommandModificaIncidenciaBuroInterno;
import com.sicap.clientes.commands.CommandNull;
import com.sicap.clientes.commands.CommandObtenGrupo;
import com.sicap.clientes.commands.CommandObtenScore;
import com.sicap.clientes.commands.CommandPrueba2;
import com.sicap.clientes.commands.CommandRefinanciamientoGrupal;
import com.sicap.clientes.commands.CommandRegistraRFCCliente;
import com.sicap.clientes.commands.CommandRegistraReestructura;
import com.sicap.clientes.commands.CommandRegistraSeguroCliente;
import com.sicap.clientes.commands.CommandRegistraSolicitud;
import com.sicap.clientes.commands.CommandRegistrarPagoIndividual;
import com.sicap.clientes.commands.CommandRegistroMasivoIBS;
import com.sicap.clientes.commands.CommandRenovacionGrupal;
import com.sicap.clientes.commands.CommandObtenCapacidadPago;
import com.sicap.clientes.commands.CommandObtenCiclo;
import com.sicap.clientes.commands.CommandObtenCicloApertura;
import com.sicap.clientes.commands.CommandUsoSaldoFavor;
import com.sicap.clientes.commands.CommandConsultaOrdenPagoSeguro;
import com.sicap.clientes.commands.CommandConsultaOrdenesSeguros;
import com.sicap.clientes.commands.CommandConsultaSaldoFavor;
import com.sicap.clientes.commands.CommandConsultaSolicitudesEstatus;
import com.sicap.clientes.commands.CommandGuardaEstatusSolcitud;
import com.sicap.clientes.commands.CommandGuardaOrdenSaldoFavor;
import com.sicap.clientes.commands.CommandGuardaOrdenSeguro;
import com.sicap.clientes.commands.CommandIdentificaPersonal;
import com.sicap.clientes.commands.CommandObtenInterCiclo;
import com.sicap.clientes.commands.CommandGuardaInterCiclo;
import com.sicap.clientes.commands.CommandHistorialComentariosCliente;
import com.sicap.clientes.commands.CommandIntercicloAutrizado;
import com.sicap.clientes.commands.CommandObtenCicloAdicional;
import com.sicap.clientes.commands.CommandRechazaInterCiclo;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.Config;
import org.apache.log4j.Logger;

public class ApplicationController extends HttpServlet {

    private HashMap<String, Command> commands = null;
    private final String ERROR_PAGE = "/error.jsp";
    private static Logger myLogger = Logger.getLogger(ApplicationController.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Config.setInitParameters(config.getServletContext());
        iniciaComandos();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String target = null;
        try {
            String command = "inicio";
            HttpSession session = request.getSession(true);
            if (session.getAttribute("SESION_INICIADA") == null) {
                session.setAttribute("SESION_INICIADA", new String("1"));
            } else {
                command = request.getParameter("command");
            }
            Command cmd = getCommand(command);
            target = cmd.execute(request);
        } catch (CommandException e) {
            myLogger.error("service", e);
            request.setAttribute("javax.servlet.jsp.jspException", e);
            target = ERROR_PAGE;
        }
        myLogger.debug("Forward a :" + target);
        getServletContext().getRequestDispatcher(target).forward(request, response);

    }

    private Command getCommand(String cmd) throws CommandException {

        if (cmd == null) {
            cmd = "inicio";
        }
        myLogger.debug("Procesando comando : " + cmd);
        if (commands.containsKey(cmd)) {
            return (Command) commands.get(cmd);
        } else {
            throw new CommandException("Comando [" + cmd + "] Invalido");
        }

    }

    private void iniciaComandos() {

        commands = new HashMap<String, Command>();
        commands.put("principal", new CommandIdentificaPersonal("/buscaClientePorRFC.jsp"));
        commands.put("inicio", new CommandNull("/abreVentana.jsp"));
        commands.put("buscaClientePorRFC", new CommandBuscaClientePorRFC("/buscaClientePorRFC.jsp"));
        commands.put("registraCliente", new CommandRegistraRFCCliente("/capturaDatosCliente.jsp"));
        commands.put("cambiaEstatusCliente",new CommandGuardaEstatusSolcitud("/archivosAsociados.jsp"));        
        commands.put("registraClienteCR", new CommandClienteCapturaRapida("/archivosAsociados.jsp"));
        commands.put("actualizaClienteCR", new CommandClienteCapturaRapida("/clienteCapturaRapida.jsp"));
        commands.put("registraClienteConsumo", new CommandRegistraRFCCliente("/calculaScoring.jsp"));
        commands.put("registraClienteDescuento", new CommandRegistraRFCCliente("/capturaDatosClienteDescuento.jsp"));
        commands.put("registraClienteSellFinance", new CommandRegistraRFCCliente("/capturaDatosClienteConsumo.jsp"));
        commands.put("registraClienteMaxZapatos", new CommandRegistraRFCCliente("/capturaDatosClienteConsumo.jsp"));
        commands.put("obtenMunicipiosEstado", new CommandNull("/capturaDatosCliente.jsp"));
        commands.put("obtenColoniasMunicipio", new CommandNull("/capturaDatosCliente.jsp"));
        commands.put("guardaDatosCliente", new CommandGuardaDatosCliente("/capturaDatosCliente.jsp"));
        commands.put("guardaDatosClienteConsumo", new CommandGuardaDatosCliente("/capturaDatosClienteConsumo.jsp"));
        commands.put("guardaDatosClienteVivienda", new CommandGuardaDatosCliente("/capturaDatosClienteVivienda.jsp"));
        commands.put("guardaDatosClienteDescuento", new CommandGuardaDatosCliente("/capturaDatosClienteDescuento.jsp"));
        commands.put("buscaCodigoPostal", new CommandBuscaCP("/ayudaCodigoPostal.jsp"));
        commands.put("consultaPagosManuales", new CommandConsultaPagosManuales("/muestraTablaMovimientos.jsp"));
        commands.put("consultaPagosManualesGrupal", new CommandConsultaPagosManualesGrupal("/muestraTablaMovimientosGrupal.jsp"));
        commands.put("buscaCodigoPostalSegundaDir", new CommandBuscaCP("/ayudaCodigoPostal.jsp?numdireccion=2"));
        commands.put("buscaLocalidad", new CommandBuscaLocalidad("/ayudaLocalidad.jsp"));
        commands.put("cargaArchivo", new CommandNull("/cargaArchivos.jsp"));
        commands.put("ayudaRFC", new CommandNull("/ayudaRFC.jsp"));
        commands.put("nuevoCliente", new CommandNull("/nuevoCliente.jsp"));
        commands.put("nuevoClienteCapturaRapida", new CommandNull("/nuevoClienteCapturaRapida.jsp"));
        commands.put("generaRFC", new CommandGeneraRFC("/ayudaRFC.jsp"));
        commands.put("consultaObligadosSolidarios", new CommandNull("/consultaObligadosSolidarios.jsp"));
        commands.put("capturaObligadoSolidario", new CommandNull("/capturaObligadoSolidario.jsp"));
        commands.put("consultaDetalleGrupo", new CommandNull("/capturaDatosBuro.jsp"));
        commands.put("capturaDatosCredito", new CommandNull("/capturaDatosBuro.jsp"));
        commands.put("guardaDatosCredito", new CommandGuardaDatosCredito("/capturaDatosBuro.jsp"));
        commands.put("guardaDatosCreditoObligado", new CommandGuardaDatosCredito("/consultaCreditoObligadosSolidarios.jsp"));
        commands.put("guardaDatosObligadoSolidario", new CommandGuardaDatosObligadoSolidario("/consultaObligadosSolidarios.jsp"));
        commands.put("modificaObligadoSolidario", new CommandNull("/capturaObligadoSolidario.jsp"));
        commands.put("consultaCreditoObligadosSolidarios", new CommandNull("/consultaCreditoObligadosSolidarios.jsp"));
        commands.put("capturaCreditoObligadosSolidarios", new CommandNull("/capturaDatosBuroObligado.jsp"));
        commands.put("actualizaDatosCredito", new CommandActualizaDatosCredito("/capturaDatosBuro.jsp"));
        commands.put("actualizaDatosCreditoObligado", new CommandActualizaDatosCredito("/consultaCreditoObligadosSolidarios.jsp"));
        commands.put("datosGeneralesCliente", new CommandNull("/capturaDatosCliente.jsp"));
        commands.put("datosGeneralesClienteConsumo", new CommandNull("/capturaDatosClienteConsumo.jsp"));
        commands.put("datosGeneralesClienteDescuento", new CommandNull("/capturaDatosClienteDescuento.jsp"));
        commands.put("datosGeneralesClienteVivienda", new CommandNull("/capturaDatosClienteVivienda.jsp"));
        commands.put("capturaPropuestaComiteCredito", new CommandNull("/capturaPropuestaComiteCredito.jsp"));
        commands.put("guardaPropuestaComiteCredito", new CommandGuardaPropuestaComite("/capturaPropuestaComiteCredito.jsp"));
        //commands.put("consultaCliente",new CommandConsultaCliente("/capturaDatosCliente.jsp"));
        commands.put("consultaCliente", new CommandConsultaCliente("/consultaSolicitudesCliente.jsp"));
        //commands.put("consultaCliente",new CommandConsultaCliente("/capturaDatosClienteConsumo.jsp"));
        commands.put("decisionComiteCredito", new CommandNull("/capturaDecisionComite.jsp"));
        commands.put("capturaDecisionComite", new CommandGuardaDecisionComite("/capturaDecisionComite.jsp"));

        commands.put("autorizacionCreditoConsumo", new CommandNull("/capturaAutorizacionCreditoConsumo.jsp"));
        commands.put("guardaAutorizacionCreditoConsumo", new CommandGuardaDecisionComite("/capturaAutorizacionCreditoConsumo.jsp"));

        commands.put("capturaArrendatarioDomicilio", new CommandArrendatarioDomicilio("/capturaArrendatario.jsp"));
        commands.put("guardaArrendatario", new CommandGuardaArrendatario("/capturaArrendatario.jsp"));
        commands.put("capturaArrendatarioLocal", new CommandArrendatarioLocal("/capturaArrendatario.jsp"));
        commands.put("capturaConyuge", new CommandNull("/capturaConyuge.jsp"));
        commands.put("capturaConyugeVivienda", new CommandNull("/capturaConyugeVivienda.jsp"));
        commands.put("guardaConyuge", new CommandGuardaConyuge("/capturaConyuge.jsp"));
        commands.put("capturaNegocioCliente", new CommandNull("/capturaNegocioCliente.jsp"));
        commands.put("guardaNegocioCliente", new CommandGuardaNegocio("/capturaNegocioCliente.jsp"));
        commands.put("capturaEconomiaObligados", new CommandNull("/consultaObligadosSolidarios.jsp"));
        commands.put("capturaEconomiaObligado", new CommandNull("/capturaEconomiaObligadoSolidario.jsp"));
        commands.put("guardaEconomiaObligadoSolidario", new CommandGuardaEconomiaObligado("/consultaObligadosSolidarios.jsp"));
        commands.put("capturaInformacionFinanciera", new CommandNull("/capturaInformacionFinanciera.jsp"));
        commands.put("guardaInformacionFinanciera", new CommandGuardaInformacionFinanciera("/capturaInformacionFinanciera.jsp"));
        commands.put("consultaReferenciasComerciales", new CommandNull("/consultaReferenciasComerciales.jsp"));
        commands.put("consultaReferenciasPersonales", new CommandNull("/consultaReferenciasPersonales.jsp"));

        commands.put("referenciaPersonal", new CommandNull("/referenciaPersonal.jsp"));
        commands.put("capturaReferenciaPersonal", new CommandGuardaReferenciaPersonal("/consultaReferenciasPersonales.jsp"));
        commands.put("modificaReferenciaPersonal", new CommandNull("/referenciaPersonal.jsp"));
        commands.put("modificaRefPer", new CommandGuardaReferenciaPersonal("/consultaReferenciasPersonales.jsp"));
        commands.put("capturaReferenciaComercial", new CommandNull("/capturaReferenciaComercial.jsp"));
        commands.put("guardaReferenciaComercial", new CommandGuardaReferenciaComercial("/consultaReferenciasComerciales.jsp"));
        commands.put("modificaReferenciaComercial", new CommandNull("/capturaReferenciaComercial.jsp"));
        commands.put("consultaReferenciasPersonales", new CommandNull("/consultaReferenciasPersonales.jsp"));
        commands.put("consultaArchivosAsociados", new CommandNull("/archivosAsociados.jsp"));
        commands.put("consultaBuroCredito", new CommandNull("/consultaBuro.jsp"));

        commands.put("obtieneRespuestaBuro", new CommandConsultaBuro("/respuestaBuro.jsp"));

        commands.put("consultaCirculoDeCredito", new CommandNull("/consultaCirculo.jsp"));
        commands.put("obtieneRespuestaCirculo", new CommandConsultaCirculo("/respuestaCirculo.jsp"));

        commands.put("capturaEmpleoCliente", new CommandNull("/capturaEmpleoCliente.jsp"));
        commands.put("capturaEmpleoClienteGrupal", new CommandNull("/capturaEmpleoClienteGrupal.jsp"));
        commands.put("capturaEmpleoClienteVivienda", new CommandNull("/capturaEmpleoClienteVivienda.jsp"));
        commands.put("capturaEmpleoClienteDescuento", new CommandNull("/capturaEmpleoClienteDescuento.jsp"));
        commands.put("guardaEmpleoCliente", new CommandGuardaEmpleo("/capturaEmpleoCliente.jsp"));
        commands.put("guardaEmpleoClienteDescuento", new CommandGuardaEmpleo("/capturaEmpleoClienteDescuento.jsp"));
        commands.put("guardaEmpleoClienteVivienda", new CommandGuardaEmpleo("/capturaEmpleoClienteVivienda.jsp"));
        commands.put("guardaEmpleoClienteGrupal", new CommandGuardaEmpleo("/capturaEmpleoClienteGrupal.jsp"));

        commands.put("capturaViviendaCliente", new CommandNull("/capturaViviendaCliente.jsp"));
        commands.put("guardaViviendaCliente", new CommandGuardaVivienda("/capturaViviendaCliente.jsp"));
        commands.put("capturaCapacidadPago", new CommandNull("/capturaCapacidadPago.jsp"));
        commands.put("guardaCapacidadPago", new CommandGuardaCapacidadPago("/capturaCapacidadPago.jsp"));

        commands.put("capturaReferenciaLaboral", new CommandNull("/capturaReferenciaLaboral.jsp"));
        commands.put("guardaReferenciaLaboral", new CommandGuardaReferenciaLaboral("/capturaReferenciaLaboral.jsp"));

        commands.put("scoreConsumo", new CommandNull("/calculaScoring.jsp"));
        commands.put("obtenScoreConsumo", new CommandObtenScore("/calculaScoring.jsp"));

        commands.put("calculaCapacidad", new CommandNull("/calculaCapacidadDescNom.jsp"));
        commands.put("obtenCapacidadPago", new CommandObtenCapacidadPago("/calculaCapacidadDescNom.jsp"));

        commands.put("capturaGrupo", new CommandNull("/capturaGrupo.jsp"));
        commands.put("guardaGrupo", new CommandGuardaGrupo("/capturaGrupo.jsp"));

        commands.put("capturaSeguro", new CommandNull("/capturaSeguro.jsp"));
        commands.put("registraSeguroCliente", new CommandRegistraSeguroCliente("/capturaSeguro.jsp"));

        commands.put("capturaReferenciasCrediticias", new CommandNull("/capturaReferenciasCrediticias.jsp"));
        commands.put("guardaReferenciasCrediticias", new CommandGuardaReferenciasCrediticias("/capturaReferenciasCrediticias.jsp"));

        commands.put("referenciaPersonalConsumo", new CommandNull("/capturaReferenciaPersonalConsumo.jsp"));
        commands.put("capturaReferenciaPersonalConsumo", new CommandGuardaReferenciaPersonal("/consultaReferenciasPersonales.jsp"));
        commands.put("modificaReferenciaPersonalConsumo", new CommandNull("/capturaReferenciaPersonalConsumo.jsp"));
        commands.put("modificaRefPerConsumo", new CommandGuardaReferenciaPersonal("/consultaReferenciasPersonales.jsp"));

        commands.put("capturaArrendatarioDomicilioConsumo", new CommandArrendatarioDomicilio("/capturaArrendatarioConsumo.jsp"));
        commands.put("guardaArrendatarioConsumo", new CommandGuardaArrendatario("/capturaArrendatarioConsumo.jsp"));

        commands.put("capturaObligadoSolidarioConsumo", new CommandNull("/capturaObligadoSolidarioConsumo.jsp"));
        commands.put("modificaObligadoSolidarioConsumo", new CommandNull("/capturaObligadoSolidarioConsumo.jsp"));
        commands.put("guardaDatosObligadoSolidarioConsumo", new CommandGuardaDatosObligadoSolidario("/consultaObligadosSolidarios.jsp"));

        commands.put("capturaCreditoSolicitadoConsumo", new CommandNull("/capturaCreditoSolicitadoConsumo.jsp"));
        commands.put("guardaCreditoSolicitadoConsumo", new CommandGuardaCreditoSolicitado("/capturaCreditoSolicitadoConsumo.jsp"));
        commands.put("capturaCreditoSolicitadoDescuento", new CommandNull("/capturaCreditoSolicitadoDescuento.jsp"));
        commands.put("guardaCreditoSolicitadoDescuento", new CommandGuardaCreditoSolicitado("/capturaCreditoSolicitadoDescuento.jsp"));

        commands.put("capturaAbreviadaDatosCliente", new CommandNull("/capturaAbreviadaDatosCliente.jsp"));
        commands.put("guardaDatosAbreviadaCliente", new CommandGuardaDatosAbreviada("/capturaAbreviadaDatosCliente.jsp"));

        commands.put("capturaGrupo", new CommandNull("/capturaGrupo.jsp"));
        commands.put("guardaGrupo", new CommandGuardaGrupo("/capturaGrupo.jsp"));
        commands.put("administraGrupos", new CommandNull("/consultaGrupos.jsp"));
        commands.put("administraClientes", new CommandNull("/buscaClientePorRFC.jsp"));
        commands.put("buscaGrupos", new CommandBuscaGrupos("/consultaGrupos.jsp"));
        commands.put("consultaDetalleGrupo", new CommandObtenGrupo("/capturaGrupo.jsp"));
        commands.put("capturaCicloGrupal", new CommandConsultaCicloGrupal("/capturaCicloGrupal.jsp"));
        
        commands.put("capturaCicloRestructura", new CommandConsultaCicloGrupal("/capturaCicloRestructura.jsp"));
        commands.put("guardaCicloRestructura", new CommandGuardaCicloRestructura("/capturaCicloRestructura.jsp"));
        commands.put("consultaCicloRestructura", new CommandNull("/capturaCicloRestructura.jsp"));
        commands.put("desembolsaCicloRestructura", new CommandDesembolsoRestructura("/capturaCicloRestructura.jsp"));

        commands.put("renuevaCicloGrupal", new CommandRenovacionGrupal("/capturaRenovacionCicloGrupal.jsp"));
        commands.put("guardaRenovacionCicloGrupal", new CommandGuardaRenovacionCicloGrupal("/capturaCicloGrupal.jsp"));
        commands.put("guardaAperturaCiclo", new CommandGuardaAperturaCiclo("/capturaCicloApertura.jsp"));
        commands.put("aperturaNuevoCiclo", new CommandAperturaNuevoCiclo("/capturaAperturaNuevoCiclo.jsp"));

        commands.put("guardaCicloGrupal", new CommandGuardaCicloGrupal("/capturaCicloGrupal.jsp"));
        commands.put("guardaCicloAutorizado", new CommandGuardaCicloGrupal("/capturaCicloAutorizado.jsp"));
        commands.put("actualizaCicloApertura", new CommandActualizaCicloApertura("/capturaCicloApertura.jsp"));
        //commands.put("consultaCicloGrupal",new CommandNull("/capturaCicloGrupal.jsp"));
        commands.put("consultaCicloGrupal", new CommandObtenCiclo("/capturaCicloGrupal.jsp"));
        commands.put("consultaCicloApertura", new CommandObtenCicloApertura("/capturaCicloApertura.jsp"));
        commands.put("agregaIntegrantesNuevos", new CommandActualizaclientesNuevos("/capturaCicloApertura.jsp"));
        commands.put("consultaMasivaCC", new CommandConsultaMasivaCC("/capturaCicloApertura.jsp"));
        commands.put("consultaCicloAutorizado", new CommandObtenCiclo("/capturaCicloAutorizado.jsp"));
        commands.put("peticionDesembolso", new CommandCicloGrupalAutorizado("/capturaCicloAutorizado.jsp"));
        
        //Interciclo
        commands.put("consultaCicloGrupalIC", new CommandObtenInterCiclo("/capturaCicloGrupalIC.jsp"));
        commands.put("guardaInterCiclo", new CommandGuardaInterCiclo("/capturaCicloGrupalIC.jsp"));
        commands.put("aceptaInterCiclo", new CommandGuardaInterCiclo("/capturaAutorizacionInterciclo.jsp"));
        commands.put("rechazaInterCiclo", new CommandRechazaInterCiclo("/capturaCicloGrupalIC.jsp"));
        commands.put("consultaInterCicloEstatus", new CommandNull("/consultaInterCiclo.jsp"));
        commands.put("consultaEstatusInterCiclo", new CommandConsultaInterCiclo("/consultaInterCiclo.jsp"));
        commands.put("consultaIntercicloAutorizado", new CommandObtenInterCiclo("/capturaAutorizacionInterciclo.jsp"));
        commands.put("consultaIntercicloDispersado", new CommandObtenCiclo("/capturaAutorizacionInterciclo.jsp"));
        commands.put("peticionDesembolsoInterciclo", new CommandIntercicloAutrizado("/capturaAutorizacionInterciclo.jsp"));
        commands.put("desembolsaInterciclo", new CommandDesembolsoInterciclo("/capturaAutorizacionInterciclo.jsp"));
        commands.put("confirmaDispersionInterciclo", new CommandConfirmaDesembolsoInterciclo("/capturaAutorizacionInterciclo.jsp"));

        commands.put("reporteCartera", new CommandNull("/consultaReporteCartera.jsp"));
        commands.put("respuestaReportes", new CommandConsultaReportes("/muestraReportes.jsp"));

        commands.put("procesaPagosReferenciados", new CommandNull("/procesaArchivoPagosReferenciados.jsp"));
        commands.put("archivosPagosReferenciados", new CommandNull("/archivosPagosReferenciados.jsp"));

        commands.put("consultaSolicitudCliente", new CommandConsultaSolicitudCliente("/capturaDatosCliente.jsp"));
        commands.put("capturaNuevaSolicitud", new CommandNull("/seleccionaProducto.jsp"));
        commands.put("registraSolicitud", new CommandRegistraSolicitud("/capturaDatosCliente.jsp"));

        commands.put("consultaSolicitudes", new CommandNull("/consultaSolicitudesCliente.jsp"));
        commands.put("consultaReporteSolicitudes", new CommandNull("/consultaSolicitudesEstatusSucursal.jsp"));

        commands.put("consultaSolicitudesConsumo", new CommandConsultaSolicitudes("/consultaSolicitudesEstatusSucursal.jsp"));
        commands.put("menuReportes", new CommandNull("/menuReportes.jsp"));
        commands.put("buscaSolicitudEstatus", new CommandNull("/consultaSolicitudesEstatus.jsp"));
        commands.put("buscaActualizacionDoc", new CommandNull("/consultaActualizacionDocumentos.jsp"));
        commands.put("generaReporteDocVencidos", new CommandActualizacionDocumentos("/descargaDocumentosVencidos.jsp"));
        commands.put("consultaSolicitudesEstatus", new CommandConsultaSolicitudesEstatus("/consultaSolicitudesEstatus.jsp"));
        commands.put("consultaActualizacionDoc", new CommandActualizacionDocumentos("/consultaActualizacionDocumentos.jsp"));
        commands.put("capturaDatosCreditoVivienda", new CommandNull("/capturaDatosCreditoVivienda.jsp"));
        commands.put("guardaDatosCreditoVivienda", new CommandGuardaDatosCreditoVivienda("/capturaDatosCreditoVivienda.jsp"));

        commands.put("capturaExpedienteOperativoLegal", new CommandNull("/capturaExpedienteOperativoLegal.jsp"));
        commands.put("guardaExpedienteOperativoLegal", new CommandGuardaExpedienteOperativoLegal("/capturaExpedienteOperativoLegal.jsp"));

        commands.put("capturaExpedienteComunal", new CommandNull("/capturaExpedienteComunal.jsp"));
        commands.put("guardaExpedienteComunal", new CommandGuardaExpedienteComunal("/capturaExpedienteComunal.jsp"));

        commands.put("capturaExpedienteDescuentoNomina", new CommandNull("/capturaExpedienteDescuentoNomina.jsp"));
        commands.put("guardaExpedienteDescuentoNomina", new CommandGuardaExpedienteOperativoLegal("/capturaExpedienteDescuentoNomina.jsp"));

        commands.put("cambioPasswordUsuario", new CommandNull("/cambioPasswordUsuario.jsp"));
        commands.put("actualizaUsuario", new CommandActualizaUsuario("/buscaClientePorRFC.jsp"));

        commands.put("admonChequera", new CommandNull("/administracionChequera.jsp"));
        commands.put("altaChequera", new CommandNull("/capturaChequeras.jsp"));
        commands.put("guardaChequera", new CommandGuardaChequera("/capturaChequeras.jsp"));
        commands.put("consultaChequeras", new CommandNull("/consultaChequeras.jsp"));
        commands.put("obtieneChequeras", new CommandConsultaChequeras("/consultaChequeras.jsp"));
        commands.put("modificaEstatusCheques", new CommandNull("/modificaEstatusCheques.jsp"));
        commands.put("consultaCheque", new CommandConsultaCheques("/modificaEstatusCheques.jsp"));
        commands.put("cancelaCheque", new CommandCancelaCheques("/modificaEstatusCheques.jsp"));
        commands.put("cancelaChequeConfirmado", new CommandCancelaChequesConfirmados("/modificaEstatusCheques.jsp"));
        //commands.put("desembolsaCicloGrupal", new CommandDesembolsoGrupal("/capturaCicloGrupal.jsp"));
        commands.put("desembolsaCicloGrupal", new CommandDesembolsoGrupal("/capturaCicloAutorizado.jsp"));
        commands.put("desembolsaCicloGrupalSucursal", new CommandDesembolsoGrupal("/capturaCicloGrupal.jsp"));

        commands.put("consultaEstadoCuenta", new CommandConsultaEstadoCuenta("/consultaEstadoCuenta.jsp"));
        commands.put("consultaEstadoCuentaGrupal", new CommandConsultaEstadoCuentaGrupal("/consultaEstadoCuentaGrupal.jsp"));
        commands.put("usoSaldoFavor", new CommandUsoSaldoFavor("/consultaEstadoCuentaGrupal.jsp"));

        commands.put("capturaLimitesCredito", new CommandNull("/capturaLimitesCredito.jsp"));
        commands.put("guardaLimitesCredito", new CommandGuardaLimitesCredito("/capturaLimitesCredito.jsp"));

        commands.put("consultaDisposiciones", new CommandNull("/consultaDisposiciones.jsp"));
        commands.put("capturaDisposicion", new CommandNull("/capturaDisposiciones.jsp"));
        commands.put("guardaDisposiciones", new CommandGuardaDisposiciones("/capturaDisposiciones.jsp"));

        commands.put("autorizacionMaxZapatos", new CommandNull("/capturaAutorizacionMaxZapatos.jsp"));
        commands.put("guardaAutorizacionMaxZapatos", new CommandGuardaAutorizacionMaxZapatos("/capturaAutorizacionMaxZapatos.jsp"));
        commands.put("muestraPagareMaxzapatos", new CommandNull("/muestraPagareMaxZapatos.jsp"));

        commands.put("autorizacionSellFinance", new CommandNull("/capturaAutorizaSellFinance.jsp"));
        commands.put("guardaAutorizacionSellFinance", new CommandAutorizaSellFinance("/capturaAutorizaSellFinance.jsp"));

        commands.put("actualizaSeguros", new CommandActualizaSeguros("/actualizarSeguros.jsp"));

        commands.put("otrosDomicilios", new CommandNull("/capturaOtrosDomicilios.jsp"));
        commands.put("guardaOtrosDomicilios", new CommandGuardaOtrosDomicilios("/capturaOtrosDomicilios.jsp"));

        commands.put("validarPagoComunal", new CommandConsultarPagosComunales("/capturaPagoComunal.jsp"));
        commands.put("registrarPagoIndividual", new CommandRegistrarPagoIndividual("/capturaPagoComunal.jsp"));

        commands.put("generaAmortizacion", new CommandGeneraAmortizacion("/generaAmortizacion.jsp"));
        //commands.put("consultaMantenimientoCliente",new CommandNull("/mantenimientoClientes.jsp"));
        commands.put("mantenimientoCliente", new CommandNull("/mantenimientoClientes.jsp"));
        commands.put("guardaCambiosCliente", new CommandGuardaCambiosCliente("/mantenimientoClientes.jsp"));

        commands.put("consultaDetalleMonitor", new CommandConsultaGrupoMonitor("/consultaEventosPagoCiclo.jsp"));
        commands.put("consultaEventosPagos", new CommandNull("/consultaEventosPagoCiclo.jsp"));

        commands.put("registraReestructura", new CommandRegistraReestructura("/capturaDatosCliente.jsp"));
        commands.put("registroMasivoIBS", new CommandRegistroMasivoIBS("/registroMasivoIBS.jsp"));

        commands.put("refinanciaCiclo", new CommandRefinanciamientoGrupal("/capturaCicloGrupal.jsp"));
        commands.put("guardaCicloRefinanciamiento", new CommandGuardaRefinanciamientoGrupal("/capturaCicloGrupal.jsp"));

        commands.put("confirmaDesembolsaCicloGrupal", new CommandConfirmaDesembolsoGrupal("/capturaCicloGrupal.jsp"));

        commands.put("consultaInformeCobranza", new CommandConsultaInformeCobranza("/capturaInformeCobranza.jsp"));
        commands.put("guardaCuestionario", new CommandGuardaInformeCobranza("/consultaEventosPagoCiclo.jsp"));

        commands.put("consultaInformeVisita", new CommandConsultaInformeVisita("/capturaInformeAlertaGrupal.jsp"));
        commands.put("guardaInformeVisita", new CommandGuardaInformeVisitaGrupal("/consultaEventosPagoCiclo.jsp"));

        commands.put("consultaInformeCobranzaGrupal", new CommandConsultaInformeCobranzaGrupal("/consultaEventosPagoCiclo.jsp"));

        commands.put("eliminaAlerta", new CommandEliminaAlertaMonitor("/consultaEventosPagoCiclo.jsp"));
        commands.put("consultaGrupo", new CommandNull("/capturaGrupo.jsp"));

        commands.put("ignorarAlertasFuturas", new CommandIgnorarAlertasFuturas("/capturaGrupo.jsp"));

        commands.put("guardaOtrosDatos", new CommandGuardaOtrosDatos("/capturaCicloGrupal.jsp"));

        // Llamados para la funcionalidad de Alta Baja y Actualización del Buro Interno
        commands.put("consultaBuroInterno", new CommandNull("/buscaClienteBuro.jsp"));
        commands.put("altaIncidencia", new CommandBuscaClienteParaIncidencia("/altaBuroInterno.jsp"));
        commands.put("buscaIncidenciaBuro", new CommandBuscaIncidenciaBuroInterno("/buscaClienteBuro.jsp"));
        commands.put("altaIncidenciaBuroInterno", new CommandAltaIncidenciaBuroInterno("/buscaClienteBuro.jsp"));
        commands.put("modificaIncidencia", new CommandBuscaIncidenciaBuroInterno("/modificaBuroInterno.jsp"));
        commands.put("modificaIncidenciaBuroInterno", new CommandModificaIncidenciaBuroInterno("/buscaClienteBuro.jsp"));

        commands.put("mikePrueba", new CommandNull("/mikePrueba.jsp"));
        commands.put("pruebas", new CommandPrueba2("/buscaClientePorRFC.jsp"));

        commands.put("capturaControlPagos", new CommandNull("/capturaControlDePagos.jsp"));
        commands.put("registraControlPagos", new CommandControlPagos("/capturaControlDePagos.jsp"));
        commands.put("descarga", new CommandControlPagos("/capturaControlDePagos.jsp"));

        commands.put("consultaOrdenPagoSeguro", new CommandConsultaOrdenPagoSeguro("/generaOrdenPagoSeguro.jsp"));
        commands.put("generaOrdenSeguro", new CommandNull("/generaOrdenPagoSeguro.jsp"));
        commands.put("guardaOrdenSeguro", new CommandGuardaOrdenSeguro("/generaOrdenPagoSeguro.jsp"));
        commands.put("consultaOrdenPagoSeguros", new CommandNull("/consultaOrdenPagoSeguros.jsp"));
        commands.put("consultaOrdenesSeguros", new CommandConsultaOrdenesSeguros("/consultaOrdenPagoSeguros.jsp"));

        commands.put("devolucionSaldoFavor", new CommandNull("/devolucionSaldoFavor.jsp"));
        commands.put("consultaSaldoFavor", new CommandConsultaSaldoFavor("/devolucionSaldoFavor.jsp"));
        commands.put("guardaOrdenPagoSaldoFavor", new CommandGuardaOrdenSaldoFavor("/devolucionSaldoFavor.jsp"));

        commands.put("consultaODP", new CommandNull("/consultaOrdenesPagoCliente.jsp"));

        commands.put("muestraDocumento", new CommandActualizaCiclo("/muestraDocumento.jsp"));
        commands.put("descargaDocumento", new CommandNull("/muestraDocumento.jsp"));

        commands.put("consultaCicloEstatus", new CommandNull("/consultaCicloEstatus.jsp"));
        commands.put("consultaEstatusCiclo", new CommandConsultaEstatusCiclo("/consultaCicloEstatus.jsp"));
        commands.put("cambiaEstatusCiclo", new CommandCambiaEstatusCiclo("/consultaCicloEstatus.jsp"));
        commands.put("historialComentarios", new CommandBuscaHistorialComentarios("/historialComentariosCiclo.jsp"));
        commands.put("historialComentariosCliente", new CommandHistorialComentariosCliente("/historialComentariosCliente.jsp"));
        commands.put("cierraCicloLiquidado", new CommandCierraCiclo("/consultaEstadoCuentaGrupal.jsp"));
        commands.put("agregaComentarioCiclo", new CommandAgregaComentarioCiclo("/capturaCicloGrupal.jsp"));
        commands.put("agregaComentarioCicloAut", new CommandAgregaComentarioCiclo("/capturaCicloAutorizado.jsp"));
        
        /*Producto Adicional*/
        commands.put("consultaCicloGrupalAdicional", new CommandObtenCicloAdicional("/capturaCicloGrupalAdicional.jsp"));
        commands.put("confirmaDesembolsaAdicional", new CommandConfirmaDesembolsoAdicional("/capturaCicloGrupal.jsp"));
        /**
         * JECB 01/10/2017
         * Se agrega regla de navegacion para permitir la accion 
         * de carga de archivos de solicitudes de crédito adicional
         */
        commands.put("cargaArchivoAdicional", new CommandNull("/cargaArchivos.jsp"));
        
    }

}

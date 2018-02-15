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
import com.sicap.clientes.commands.CommandActualizaCarteraGarantia;
import com.sicap.clientes.commands.CommandActualizaSucursal;
import com.sicap.clientes.commands.CommandAltaSucursal;
import com.sicap.clientes.commands.CommandAltaUsuario;
import com.sicap.clientes.commands.CommandAjusteCreditos;
import com.sicap.clientes.commands.CommandAltaAmortPagare;
import com.sicap.clientes.commands.CommandAltaAuditor;
import com.sicap.clientes.commands.CommandAltaFondeador;
import com.sicap.clientes.commands.CommandLineaCredito;
import com.sicap.clientes.commands.CommandAltaPagare;
import com.sicap.clientes.commands.CommandAsignacionAuditor;
import com.sicap.clientes.commands.CommandBuscaCarteraCedida;
import com.sicap.clientes.commands.CommandBuscaEjecutivos;
import com.sicap.clientes.commands.CommandBuscaEjecutivosActivos;
import com.sicap.clientes.commands.CommandBuscaSucursal;
import com.sicap.clientes.commands.CommandBuscaUsuarios;
import com.sicap.clientes.commands.CommandCalificacionAsesores;
import com.sicap.clientes.commands.CommandCambiaSucursalCliente;
import com.sicap.clientes.commands.CommandCancelaOrdenDePago;
import com.sicap.clientes.commands.CommandCancelaOrdenDePagoConfirmada;
import com.sicap.clientes.commands.CommandCancelaOrdenesDePagoGrupal;
import com.sicap.clientes.commands.CommandCastigarGrupo;
import com.sicap.clientes.commands.CommandCierreDia;
import com.sicap.clientes.commands.CommandCierreDiaBursa;
import com.sicap.clientes.commands.CommandCierreDiaHora;
import com.sicap.clientes.commands.CommandCobranzaFondeador;
import com.sicap.clientes.commands.CommandConsultaGruposEjecutivos;
import com.sicap.clientes.commands.CommandConsultaOrdenDePago;
import com.sicap.clientes.commands.CommandDesbloqueaUsuario;
import com.sicap.clientes.commands.CommandDescongelaAhorro;
import com.sicap.clientes.commands.CommandDescongelaPagoGarantiaInd;
import com.sicap.clientes.commands.CommandFechaReprocesarPagos;
import com.sicap.clientes.commands.CommandGuardaEjecutivos;
import com.sicap.clientes.commands.CommandGuardaPlaneacion;
import com.sicap.clientes.commands.CommandGuardaRepresentantes;
import com.sicap.clientes.commands.CommandGuardaSupervisor;
import com.sicap.clientes.commands.CommandListaEjecutivos;
import com.sicap.clientes.commands.CommandListaGrupos;
import com.sicap.clientes.commands.CommandListaSupervisor;
import com.sicap.clientes.commands.CommandListarSolicitudes;
import com.sicap.clientes.commands.CommandModificaBancoOrden;
import com.sicap.clientes.commands.CommandModificaUsuario;
import com.sicap.clientes.commands.CommandModificarEjecutivo;
import com.sicap.clientes.commands.CommandModificarRepresentante;
import com.sicap.clientes.commands.CommandModificarSupervisor;
import com.sicap.clientes.commands.CommandNull;
import com.sicap.clientes.commands.CommandObtenSucursal;
import com.sicap.clientes.commands.CommandObtenUsuario;
import com.sicap.clientes.commands.CommandOrdenesDePago;
import com.sicap.clientes.commands.CommandReasignarCartera;
import com.sicap.clientes.commands.CommandReporteSeguros;
import com.sicap.clientes.commands.CommandReprocesarPagos;
import com.sicap.clientes.commands.CommandSeleccionaRepresentante;
import com.sicap.clientes.commands.CommandSeleccionaSupervisor;
import com.sicap.clientes.commands.CommandSeleccionarEjecutivo;
import com.sicap.clientes.commands.CommandconsultaRepresentantes;
import com.sicap.clientes.commands.CommandListaDivMora;
import com.sicap.clientes.commands.CommandCondonaMora;
import com.sicap.clientes.commands.CommandContabilizar;
import com.sicap.clientes.commands.CommandContabilizarEnvio;
import com.sicap.clientes.commands.CommandEliminaIncidencia;
import com.sicap.clientes.commands.CommandGeneraOrdenesPago;
import com.sicap.clientes.commands.CommandGuardaCarteraCedida;
import com.sicap.clientes.commands.CommandMigracionSemanal;
import com.sicap.clientes.commands.CommandLiquidarGrupo;
import com.sicap.clientes.commands.CommandListarEquipos;
import com.sicap.clientes.commands.CommandMuestraEjecutivosActivos;
import com.sicap.clientes.commands.CommandRegistraDevOrden;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.Config;
import com.sicap.clientes.commands.CommandCondonaInteres;
import com.sicap.clientes.commands.CommandConsultaCicloEliminacion;
import com.sicap.clientes.commands.CommandConsultaDatosCliente;
import com.sicap.clientes.commands.CommandConsultaEquiposNoRenovados;
import com.sicap.clientes.commands.CommandConsultaGestionMetas;
import com.sicap.clientes.commands.CommandConsultaOrdenDePagoGrupal;
import com.sicap.clientes.commands.CommandConsultaPlaneacionRenovacion;
import com.sicap.clientes.commands.CommandConsultaSaldosAsigCartera;
import com.sicap.clientes.commands.CommandEliminaCiclo;
import com.sicap.clientes.commands.CommandEliminaPagos;
import com.sicap.clientes.commands.CommandFondeoTarjetas;
import com.sicap.clientes.commands.CommandGuardaCobradores;
import com.sicap.clientes.commands.CommandModificaCobradores;
import com.sicap.clientes.commands.CommandGuardaNoRenovado;
import com.sicap.clientes.commands.CommandGuardaEquiposNoRenovados;
import com.sicap.clientes.commands.CommandIncidenciasPaynet;
import com.sicap.clientes.commands.CommandIngresoPagosManaules;
import com.sicap.clientes.commands.CommandListaCobradores;
import com.sicap.clientes.commands.CommandModificaAuditor;
import com.sicap.clientes.commands.CommandPagoPagare;
import com.sicap.clientes.commands.CommandPersonalizacionTarjeta;
import com.sicap.clientes.commands.CommandPreseleccionaCartera;
import com.sicap.clientes.commands.CommandAsignaCarteraPreSinRechazos;
import com.sicap.clientes.commands.CommandConsultaLineasCredito;
import com.sicap.clientes.commands.CommandInterfacesBursa;
import com.sicap.clientes.commands.CommandQuitaCapitalVencido;
import com.sicap.clientes.commands.CommandSaldoFondeador;
import com.sicap.clientes.commands.CommandSeleccionarCobrador;
import mantenimiento.EjecutaClasees;
import org.apache.log4j.Logger;

public class AdministracionController extends HttpServlet {

    private HashMap<String, Command> commands = null;
    private final String ERROR_PAGE = "/error.jsp";
    private static Logger myLogger = Logger.getLogger(AdministracionController.class);


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
        myLogger.debug("Forward a :"+target);
        getServletContext().getRequestDispatcher(target).forward(request, response);

    }

    private Command getCommand(String cmd) throws CommandException {

        if (cmd == null) {
            cmd = "inicio";
        }
        myLogger.debug("Procesando comando : "+cmd);
        if (commands.containsKey(cmd)) {
            return (Command) commands.get(cmd);
        } else {
            throw new CommandException("Comando [" + cmd + "] Invalido");
        }

    }

    private void iniciaComandos() {

        commands = new HashMap<String, Command>();
        commands.put("inicio", new CommandNull("/menuPrincipalAdministracion.jsp"));
        commands.put("administracionUsuarios", new CommandNull("/menuAdministracionUsuarios.jsp"));

        commands.put("altaUsuarioTomcat", new CommandNull("/altaModificacionUsuario.jsp"));
        commands.put("altaUsuario", new CommandAltaUsuario("/altaModificacionUsuario.jsp"));
        commands.put("obtenUsuario", new CommandObtenUsuario("/altaModificacionUsuario.jsp"));
        commands.put("modificaUsuario", new CommandModificaUsuario("/altaModificacionUsuario.jsp"));

        commands.put("modificacionUsuario", new CommandNull("/consultaUsuariosNombre.jsp"));
        commands.put("buscaUsuarios", new CommandBuscaUsuarios("/consultaUsuariosNombre.jsp"));

        commands.put("desbloqueaUsuario", new CommandNull("/desbloqueaUsuario.jsp"));
        commands.put("buscaUsuariosDesb", new CommandBuscaUsuarios("/desbloqueaUsuario.jsp"));
        commands.put("obtenUsuarioDesb", new CommandObtenUsuario("/desbloqueaUsuario.jsp"));
        commands.put("cambiaEstatusUsuario", new CommandDesbloqueaUsuario("/desbloqueaUsuario.jsp"));

        commands.put("administracionEjecutivos", new CommandNull("/menuAdministracionEjecutivos.jsp"));
        commands.put("altaEjecutivos", new CommandNull("/capturaEjecutivos.jsp"));
        commands.put("guardaEjecutivos", new CommandGuardaEjecutivos("/capturaEjecutivos.jsp"));
        commands.put("reasignacionCartera", new CommandBuscaEjecutivos("/consultaSucursalEjecutivo.jsp"));
        commands.put("buscaEjecutivos", new CommandBuscaEjecutivos("/consultaSucursalEjecutivo.jsp"));
        commands.put("listarSolicitudes", new CommandListarSolicitudes("/consultaSucursalEjecutivo.jsp"));
        commands.put("reasignarCartera", new CommandReasignarCartera("/consultaSucursalEjecutivo.jsp"));
        commands.put("modificacionEjecutivos", new CommandNull("/modificacionEjecutivos.jsp"));
        commands.put("listaEjecutivos", new CommandListaEjecutivos("/modificacionEjecutivos.jsp"));
        commands.put("seleccionarEjecutivo", new CommandSeleccionarEjecutivo("/modificacionEjecutivos.jsp"));
        commands.put("modificarEjecutivo", new CommandModificarEjecutivo("/modificacionEjecutivos.jsp"));
        commands.put("listarEquipos", new CommandListarEquipos("/consultaSucursalEjecutivo.jsp"));
	commands.put("castigarGrupo",new CommandCastigarGrupo("/castigoGrupal.jsp"));
        commands.put("administracionCiclos", new CommandNull("/menuAdministracionCiclos.jsp"));
        
        //Cobradores
        commands.put("administracionCobradores", new CommandNull("/menuAdministracionCobradores.jsp"));
        commands.put("altaCobradores", new CommandNull("/capturaCobradores.jsp"));
        commands.put("guardaCobrador", new CommandGuardaCobradores("/capturaCobradores.jsp"));
        commands.put("modificacionCobradores", new CommandNull("/modificaCobradores.jsp"));
        commands.put("listaCobradores", new CommandListaCobradores("/modificaCobradores.jsp"));
        commands.put("seleccionarCobrador", new CommandSeleccionarCobrador("/modificaCobradores.jsp"));
        commands.put("modificarCobrador", new CommandModificaCobradores("/modificaCobradores.jsp"));

        commands.put("administracionRepresentantes", new CommandNull("/menuAdministracionRepresentantes.jsp"));
        commands.put("altaRepresentantes", new CommandNull("/capturaRepresentantes.jsp"));
        commands.put("guardaRepresentantes", new CommandGuardaRepresentantes("/capturaRepresentantes.jsp"));
        commands.put("modificarRepresentante", new CommandModificarRepresentante("/modificacionRepresentantes.jsp"));

        commands.put("modificarRepresentantes", new CommandNull("/modificacionRepresentantes.jsp"));
        commands.put("consultaRepresentantes", new CommandconsultaRepresentantes("/modificacionRepresentantes.jsp"));
        commands.put("seleccionaRepresentante", new CommandSeleccionaRepresentante("/modificacionRepresentantes.jsp"));

        commands.put("pagosReferenciados", new CommandNull("/menuPagosReferenciados.jsp"));
//		commands.put("reprocesarPagos",new CommandReprocesarPagos("/reprocesarPagosGenerarArchivo.jsp"));
        commands.put("reprocesarPagos", new CommandReprocesarPagos("/reprocesarPagos.jsp"));
        commands.put("eliminarIncidencias", new CommandEliminaIncidencia("/reprocesarPagos.jsp"));
        commands.put("cierreDia", new CommandNull("/cierreDia.jsp"));
        commands.put("procesaCierre", new CommandCierreDia("/cierreDia.jsp"));
        commands.put("restarHora", new CommandCierreDiaHora("/cierreDia.jsp"));
        commands.put("liquidacionGrupos", new CommandNull("/liquidacionGrupos.jsp"));
        commands.put("liquidarGrupo", new CommandLiquidarGrupo("/liquidacionGrupos.jsp"));
        commands.put("cierreDiaMigracion", new CommandNull("/cierreDiaMigracion.jsp"));
        
        commands.put("cierreDiaBursa", new CommandNull("/cierreDiaBursa.jsp"));
        commands.put("procesaCierreBursa", new CommandCierreDiaBursa("/cierreDiaBursa.jsp"));
        
        commands.put("interfacesBursa", new CommandNull("/interfacesBursa.jsp"));
        commands.put("generaInterfacesBursa", new CommandInterfacesBursa("/interfacesBursa.jsp"));
           
        commands.put("cierreContable", new CommandNull("/contabiliza.jsp"));
        commands.put("mayorizacion", new CommandNull("/mayoriza.jsp"));
        commands.put("procesaContabilidad", new CommandContabilizar("/contabiliza.jsp"));
        commands.put("procesaMayorizacion", new CommandContabilizarEnvio("/mayoriza.jsp"));
        commands.put("consultarPagosReprocesar", new CommandFechaReprocesarPagos("/reprocesarPagos.jsp"));
        commands.put("consultaReportes", new CommandNull("/menuAdministracionReportes.jsp"));
        commands.put("reporteSeguros", new CommandNull("/generaReporteSA.jsp"));
        commands.put("reporteSegurosAfirme", new CommandReporteSeguros("/salidaReporteSA.jsp"));
        commands.put("listarDividendosMora", new CommandListaDivMora("/condonacionGrupal.jsp"));
        commands.put("condonarMora", new CommandCondonaMora("/condonacionGrupal.jsp"));
        commands.put("administracionPagos", new CommandNull("/menuPagosReferenciados.jsp"));
        commands.put("condonarInteres", new CommandCondonaInteres("/condonacionInteresGrupal.jsp"));
        commands.put("listarDividendosInteresVen", new CommandListaDivMora("/condonacionInteresGrupal.jsp"));
        commands.put("quitaCapital", new CommandQuitaCapitalVencido("/quitaCapitalGrupal.jsp"));
        commands.put("listarDividendosCapitalVen", new CommandListaDivMora("/quitaCapitalGrupal.jsp"));
        commands.put("listarGruposCastigo",new CommandListaGrupos("/castigoGrupal.jsp"));

        commands.put("ordenesDePago", new CommandNull("/administracionOrdenesDePago.jsp"));
        //commands.put("muestraDesembolsosPendientes", new CommandOrdenesDePago("/generaDispersionesOrdenesDePago.jsp"));
        commands.put("muestraDesembolsosPendientes", new CommandOrdenesDePago("/generaOrdenesPago.jsp"));
        commands.put("dispersaOrdenPago", new CommandNull("/dispersaOrdenPago.jsp"));
        commands.put("cargarArchivoOrdenPago", new CommandNull("/cargarArchivoOrdenPago.jsp"));
        //commands.put("generaOrdenDePago", new CommandGeneraOrdenDePago("/descargaArchivoOrdenDePago.jsp"));
        commands.put("generaOrdenDePago", new CommandGeneraOrdenesPago("/descargaArchivoOrdenDePago.jsp"));
        commands.put("windowCancelaOrdenDePago", new CommandNull("/cancelaOrdenDePago.jsp"));
        commands.put("cancelacionGrupalODP", new CommandNull("/cancelacionGrupalOrdenesDePago.jsp"));
        commands.put("consultaOrdenDePago", new CommandConsultaOrdenDePago("/cancelaOrdenDePago.jsp"));
        commands.put("consultaOrdenDePagoGrupal", new CommandConsultaOrdenDePagoGrupal("/cancelacionGrupalOrdenesDePago.jsp"));
        commands.put("cancelaOrdenesDePagoGrupal", new CommandCancelaOrdenesDePagoGrupal("/cancelacionGrupalOrdenesDePago.jsp"));
        commands.put("cambiaBanco", new CommandNull("/modificaBancoOrdenPago.jsp"));
        commands.put("modificaBancoOrdenPago", new CommandModificaBancoOrden("/modificaBancoOrdenPago.jsp"));
        commands.put("capturaDevolucionOrden", new CommandNull("/devolucionOrdenPago.jsp"));
        commands.put("devolucionOrden", new CommandRegistraDevOrden("/devolucionOrdenPago.jsp"));
        commands.put("buscaOrdenesPagos", new CommandGeneraOrdenesPago("/generaOrdenesPago.jsp"));
        //commands.put("generaOrdenesPagos", new CommandGeneraOrdenesPago("/descargaArchivoOrdenDePago.jsp"));
        commands.put("generaOrdenesPagos", new CommandGeneraOrdenesPago("/muestraDocumento.jsp"));


        commands.put("cancelaOrdenDePago", new CommandCancelaOrdenDePago("/cancelaOrdenDePago.jsp"));
        commands.put("cancelaOrdenDePagoConfirmada", new CommandCancelaOrdenDePagoConfirmada("/cancelaOrdenDePago.jsp"));
        commands.put("administracionSoporte", new CommandNull("/administracionSoporte.jsp"));
        //commands.put("eliminaCiclo", new CommandNull("/eliminaCiclo.jsp"));
        commands.put("eliminaCiclo", new CommandNull("/eliminaCicloGrupal.jsp"));
        commands.put("consultaCicloEliminacion", new CommandConsultaCicloEliminacion("/eliminaCicloGrupal.jsp"));
        //commands.put("eliminarCiclo", new CommandEliminaCicloGrupal("/eliminaCiclo.jsp"));
        commands.put("eliminarCiclo", new CommandEliminaCiclo("/eliminaCicloGrupal.jsp"));

        commands.put("ejecutaConsulta", new CommandNull("/ejecutaConsultaDB.jsp"));

        commands.put("metasPlaneacion", new CommandNull("/capturaMetasEjecutivos.jsp"));
        commands.put("metasGestion", new CommandNull("/gestionMetasEjecutivos.jsp"));
        commands.put("metasMonitoreo", new CommandNull("/monitoreaMetasEjecutivos.jsp"));
        //commands.put("consultaGruposEjecutivos", new CommandConsultaGruposEjecutivos("/capturaMetasEjecutivos.jsp"));
        commands.put("buscaEjecutivosPlaneacion", new CommandBuscaEjecutivos("/capturaMetasEjecutivos.jsp"));
        commands.put("buscaEjecutivosActivos", new CommandBuscaEjecutivosActivos("/capturaEjecutivos.jsp"));
        commands.put("muestraEjecutivosActivos", new CommandMuestraEjecutivosActivos("/modificacionEjecutivos.jsp"));
        commands.put("guardaPlaneacion", new CommandGuardaPlaneacion("/capturaMetasEjecutivos.jsp"));
        commands.put("buscaEjecutivosGestion", new CommandBuscaEjecutivos("/gestionMetasEjecutivos.jsp"));
        //commands.put("consultaMetas", new CommandConsultaGruposEjecutivos("/gestionMetasEjecutivos.jsp"));
        commands.put("consultaMetas", new CommandConsultaGestionMetas("/gestionMetasEjecutivos.jsp"));
        commands.put("consultaMetasMonitoreo", new CommandConsultaGruposEjecutivos("/monitoreaMetasEjecutivos.jsp"));
        commands.put("calificacionasesores", new CommandNull("/calificacionasesores.jsp"));
        commands.put("sinRenovacion", new CommandNull("/equiposNoRenovados.jsp"));
        commands.put("procesaCalificacion", new CommandCalificacionAsesores("/calificacionasesores.jsp"));
        commands.put("consultaEquiposRenovacion", new CommandConsultaPlaneacionRenovacion("/capturaMetasEjecutivos.jsp"));
        commands.put("guardaNoRenovado", new CommandGuardaNoRenovado("/gestionMetasEjecutivos.jsp"));
        commands.put("consultaNoRenovados", new CommandConsultaEquiposNoRenovados("/equiposNoRenovados.jsp"));
        commands.put("guardaNoRenovados", new CommandGuardaEquiposNoRenovados("/equiposNoRenovados.jsp"));

        // Llamadas y redireccion del Comando para la descongelacion del ahorro
        // Miguel Mendoza 14 Dic 2010

        commands.put("descongelaAhorro", new CommandDescongelaAhorro("/descongelaAhorro.jsp"));


        commands.put("descongelaAhorroIndividual", new CommandDescongelaPagoGarantiaInd("/descongelarPagoGarantiaIndividual.jsp"));

        // Llamadas para el modulo de Alta Baja y Cambio de Sucursales
        // Miguel Mendoza 19 Enero 2011
        commands.put("administracionSucursales", new CommandNull("/menuAdministracionSucursales.jsp"));
        commands.put("altaSucursalTomcat", new CommandNull("/altaModificacionSucursal.jsp"));
        commands.put("buscaSucursal", new CommandNull("/busquedaSucursal.jsp"));

        commands.put("altaSucursal", new CommandAltaSucursal("/altaModificacionSucursal.jsp"));
        commands.put("busquedaSucursal", new CommandNull("/busquedaSucursal.jsp"));

        commands.put("buscaSucursal", new CommandBuscaSucursal("/busquedaSucursal.jsp"));
        commands.put("modificaSucursal", new CommandObtenSucursal("/modificaSucursal.jsp"));

        commands.put("actualizaSucursal", new CommandActualizaSucursal("/modificaSucursal.jsp"));

        //llamadas para el modulo Alta Baja de supervisor

        commands.put("administracionEjecutivos", new CommandNull("/menuAdministracionEjecutivos.jsp"));
        commands.put("capturaSupervisor", new CommandNull("/capturaSupervisor.jsp"));
        commands.put("guardaSupervisor", new CommandGuardaSupervisor("/capturaSupervisor.jsp"));

        commands.put("modificaSupervisor", new CommandNull("/modificaSupervisor.jsp"));
        commands.put("listaSupervisor", new CommandListaSupervisor("/modificaSupervisor.jsp"));
        commands.put("seleccionarSupervisor", new CommandSeleccionaSupervisor("/modificaSupervisor.jsp"));
        commands.put("modificarSupervisor", new CommandModificarSupervisor("/modificaSupervisor.jsp"));
        
        //MANTENIMIENTO
        commands.put("manClases", new CommandNull("/manClases.jsp"));
        commands.put("ejecutaClases", new EjecutaClasees("/manClases.jsp"));
        commands.put("ajusteCredito", new CommandNull("/ajusteCredito.jsp"));
        commands.put("ajustarCredito", new CommandAjusteCreditos("/ajusteCredito.jsp"));
        commands.put("migracionSemanal", new CommandMigracionSemanal("/migracionSemanal.jsp"));
        commands.put("caretraCedida", new CommandNull("/carteraCedida.jsp"));
        commands.put("buscaCarteraCedida", new CommandBuscaCarteraCedida("/carteraCedida.jsp"));
        commands.put("guardaCarteraCedida", new CommandGuardaCarteraCedida("/carteraCedida.jsp"));
        commands.put("importacionInformacion", new CommandNull("/importarInformacion.jsp"));
        
        commands.put("administracionSeguros", new CommandNull("/administracionSeguros.jsp"));
        commands.put("cambioSucursalClientes", new CommandNull("/cambioSucursalClientes.jsp"));
        commands.put("cambiarSucursalCliente", new CommandCambiaSucursalCliente("/cambioSucursalClientes.jsp"));
        commands.put("consultaDatosCliente", new CommandConsultaDatosCliente("/cambioSucursalClientes.jsp"));
        //ADMINISTRACION PAGOS
        commands.put("buscaPagosManual", new CommandIngresoPagosManaules("/pagosManuales.jsp"));
        commands.put("aplicaPagosManual", new CommandIngresoPagosManaules("/pagosManuales.jsp"));
        commands.put("buscaPagos", new CommandNull("/eliminacionPagos.jsp"));
        commands.put("buscaPagosEliminar", new CommandEliminaPagos("/eliminacionPagos.jsp"));
        commands.put("eliminaPagos", new CommandEliminaPagos("/eliminacionPagos.jsp"));
        commands.put("menuPagosPaynet", new CommandNull("/menuPagosPaynet.jsp"));
        commands.put("cargaArchivoPaynet", new CommandNull("/cargaArchivoConfirmPaynet.jsp"));
        commands.put("buscaIncidenciasPaynet", new CommandNull("/buscaIncidenciasPaynet.jsp"));
        commands.put("incidenciasPaynet", new CommandIncidenciasPaynet("/incidenciasPaynet.jsp"));
        commands.put("envioPagoPaynet", new CommandIncidenciasPaynet("/incidenciasPaynet.jsp"));
        commands.put("confirmaPagoPaynet", new CommandIncidenciasPaynet("/incidenciasPaynet.jsp"));
        commands.put("confirmaCancelaPaynet", new CommandIncidenciasPaynet("/incidenciasPaynet.jsp"));
        
        commands.put("saldoFavor", new CommandNull("/menuSaldoFavor.jsp"));
        
        //ADMINISTRACION TARJETAS
        commands.put("tarjetas", new CommandNull("/menuTarjetas.jsp"));
        commands.put("cargaLote", new CommandNull("/cargaLoteTarjeta.jsp"));
        commands.put("asignacionTarjeta", new CommandNull("/asignacionTarjetas.jsp"));
        commands.put("altaPersonalizar", new CommandNull("/altaPersonalizacion.jsp"));
        commands.put("buscaClienteTarjeta", new CommandPersonalizacionTarjeta("/generaPersonalizacion.jsp"));
        //commands.put("generaPersonalizacion", new CommandPersonalizacionTarjeta("/descargaArchivoOrdenDePago.jsp"));
        commands.put("generaPersonalizacion", new CommandPersonalizacionTarjeta("/muestraDocumento.jsp"));
        commands.put("buscaTarjetaDispersa", new CommandNull("/buscaTarjetasDispersar.jsp"));
        commands.put("generaFondeoTarjeta", new CommandNull("/buscaTarjetasDispersar.jsp"));
        commands.put("buscaTarjetaFondeo", new CommandFondeoTarjetas("/generaFondeoTarjeta.jsp"));
        //commands.put("generaFondeoTarjetas", new CommandFondeoTarjetas("/descargaArchivoOrdenDePago.jsp"));
        commands.put("generaFondeoTarjetas", new CommandFondeoTarjetas("/muestraDocumento.jsp"));
        
        //ADMINISTRACION AUDITORES
        commands.put("auditores", new CommandNull("/menuAuditores.jsp"));
        commands.put("altaAuditor", new CommandNull("/altaAuditor.jsp"));
        commands.put("ingresaAuditor", new CommandAltaAuditor("/altaAuditor.jsp"));
        commands.put("buscaAuditor", new CommandNull("/buscaAuditor.jsp"));
        commands.put("buscaAuditorModificar", new CommandModificaAuditor("/buscaAuditor.jsp"));
        commands.put("consultaDatosAuditor", new CommandModificaAuditor("/modificaAuditor.jsp"));
        commands.put("modificaDatosAuditor", new CommandModificaAuditor("/modificaAuditor.jsp"));
        commands.put("buscaAsignacionAuditor", new CommandNull("/asignaAuditor.jsp"));
        commands.put("buscaAuditorAsignar", new CommandAsignacionAuditor("/asignaAuditor.jsp"));
        commands.put("asignaSucursalAuditor", new CommandAsignacionAuditor("/asignaAuditor.jsp"));
        
        //ADMINISTRACION FONDEADORES
        commands.put("administracionFondeadores", new CommandNull("/menuAdministracionFondeadores.jsp"));
        commands.put("saldofondeador", new CommandSaldoFondeador("/saldoFondeadores.jsp"));
        commands.put("ingresoCobranza", new CommandNull("/ingresoCobranzaFondeador.jsp"));
        commands.put("buscaCobranza", new CommandCobranzaFondeador("/ingresoCobranzaFondeador.jsp"));
        commands.put("ingresaCobranza", new CommandCobranzaFondeador("/ingresoCobranzaFondeador.jsp"));
            //Asignacion manual de Cartera
        commands.put("asignaCarteraGarantia", new CommandNull("/asignacionCarteraEnGarantia.jsp"));
        commands.put("consultaSaldosAsigCartera", new CommandConsultaSaldosAsigCartera("/asignacionCarteraEnGarantia.jsp"));
        commands.put("actualizaFondeadorCartera", new CommandActualizaCarteraGarantia("/asignacionCarteraEnGarantia.jsp"));
            //Preseleccion
        commands.put("generarPreseleccion", new CommandNull("/generarPreseleccion.jsp"));
        commands.put("ejecutaPreseleccion", new CommandPreseleccionaCartera("/generarPreseleccion.jsp"));
            //VistoBueno
        commands.put("subePreSeleccionada", new CommandNull("/subirCarteraPreseleccionada.jsp"));
        //commands.put("asignaCarteraPreseleccion", new CommandAsignaCarteraPre("/subirCarteraPreseleccionada.jsp"));
        commands.put("asignarCarteraPreSinRechazos", new CommandAsignaCarteraPreSinRechazos("/subirCarteraPreseleccionada.jsp"));
        
        commands.put("ingresoLineaCredito", new CommandNull("/altaLineaCredito.jsp"));
        commands.put("altaLineaCredito", new CommandLineaCredito("/altaLineaCredito.jsp"));
        commands.put("ingresoPagare", new CommandNull("/altaPagare.jsp"));
        commands.put("altaPagare", new CommandAltaPagare("/altaPagare.jsp"));
        commands.put("ingresoAmort", new CommandNull("/altaAmortizacionPagare.jsp"));
        commands.put("altaAmortPagare", new CommandAltaAmortPagare("/altaAmortizacionPagare.jsp"));
        commands.put("ingresoDesactLineaCredito", new CommandNull("/desactivarLineaCredito.jsp"));
        commands.put("desactivarLineaCredito", new CommandLineaCredito("/desactivarLineaCredito.jsp"));
        commands.put("ingresoPagoPagare", new CommandNull("/pagoPagareFondeador.jsp"));
        commands.put("aplicaPagosPagare", new CommandPagoPagare("/pagoPagareFondeador.jsp"));
        commands.put("ingresoAltaFondeador", new CommandNull("/altaFondeador.jsp"));
        commands.put("altaFondeador", new CommandAltaFondeador("/altaFondeador.jsp"));
        commands.put("ingresoConsultaLC", new CommandNull("/consultaLineasCredito.jsp"));
        commands.put("consultarFondeadores", new CommandConsultaLineasCredito("/consultaLineasCredito.jsp"));
    }
}

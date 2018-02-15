package com.sicap.clientes.helpers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SellFinanceDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.helpers.ibs.ClienteHelperIBS;
import com.sicap.clientes.helpers.ibs.CreditoHelperIBS;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.ArrendatarioVO;
import com.sicap.clientes.vo.CapacidadPagoVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DisposicionVO;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.vo.ExpedienteVO;
import com.sicap.clientes.vo.InformacionFinancieraVO;
import com.sicap.clientes.vo.NegocioVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.ReferenciaLaboralVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.SellFinanceVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.ViviendaVO;

public class SolicitudHelper{


	public static TreeMap getCatalogoAfiliacionIMSS(){
		TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
		cat.put(new Integer(0), "Seleccionar...");
		cat.put(new Integer(1), "Si");
		cat.put(new Integer(2), "No");
		return cat;
	}
	
	public static TreeMap getCatalogoEstatusAuto(){
		TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
		cat.put(new Integer(0), "Seleccionar...");
		cat.put(new Integer(1), "En proceso de pago");
		cat.put(new Integer(2), "Propio");
		return cat;
	}

	public static TreeMap getCatalogoDesembolso(boolean limitado){
		TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
		cat.put(new Integer(0), "Seleccionar...");
		cat.put(new Integer(1), "Listo para desembolsar");
		if ( !limitado )
			cat.put(new Integer(2), "Desembolsado");
		return cat;
	}
	
	
	//Esde metodo ser� actualizado para funcionar como lo hace el getVo de ClienteHelper
	public static SolicitudVO getVO (SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		
		if ( request.getParameter("idCliente")!=null ) solicitud.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		if ( request.getParameter("idSolicitud")!=null ) solicitud.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		//solicitud.fechaFirma = new java.sql.Date(HTMLHelper.getParameterDate(request, "solicitudFechaFirma").getTime());
		if ( request.getParameter("solicitudFechaFirma")!=null ) solicitud.fechaFirma = Convertidor.toSqlDate( HTMLHelper.getParameterDate(request, "solicitudFechaFirma") );
		if ( solicitud.fechaCaptura==null ) solicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
		if ( request.getParameter("ejecutivo")!=null ) solicitud.idEjecutivo = HTMLHelper.getParameterInt(request, "ejecutivo");
		if ( request.getParameter("medio")!=null ) solicitud.medio = HTMLHelper.getParameterInt(request, "medio");
		if ( request.getParameter("idGrupo")!=null ) solicitud.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
                if ( solicitud.tipoOperacion!=ClientesConstants.CONSUMO || solicitud.tipoOperacion!=ClientesConstants.VIVIENDA || solicitud.tipoOperacion!=ClientesConstants.CREDIHOGAR ){
			if ( request.getParameter("montoSolicitado")!=null ) solicitud.montoSolicitado = HTMLHelper.getParameterDouble(request, "montoSolicitado");
			if ( request.getParameter("plazoSolicitado")!=null ) solicitud.plazoSolicitado = HTMLHelper.getParameterInt(request, "plazoSolicitado");
			if ( request.getParameter("frecuenciaPagoSolicitada")!=null ) solicitud.frecuenciaPagoSolicitada = HTMLHelper.getParameterInt(request, "frecuenciaPagoSolicitada");
			if ( request.getParameter("destinoCredito")!=null ) solicitud.destinoCredito = HTMLHelper.getParameterInt(request, "destinoCredito");
			if ( request.getParameter("numrepresentante")!=null ) solicitud.numrepresentante = HTMLHelper.getParameterInt(request, "numrepresentante");
                        solicitud.consultaCC = 0;
			if ( request.getParameter("consultaCC")!=null) solicitud.consultaCC = (request.getParameter("consultaCC").equals("si") ? 1 : 0);
                        
		}
                if ( request.getParameter("rolesHogar")!=null ) solicitud.rolHogar = HTMLHelper.getParameterInt(request, "rolesHogar");
                if (!request.isUserInRole("ANALISIS_CREDITO")){
                    if ( request.getParameter("interCiclo")!=null) solicitud.subproducto = (request.getParameter("interCiclo").equals("si") ? 1 : 0);
                    else solicitud.subproducto=0;
                }
                if ( request.getParameter("otroCredito")!=null ) solicitud.otroCredito = HTMLHelper.getParameterInt(request, "otroCredito");
                if ( request.getParameter("mejorIngreso")!=null ) solicitud.mejorIngreso = HTMLHelper.getParameterInt(request, "mejorIngreso");
                if ( request.getParameter("idProgProspera")!=null ) solicitud.idProgProspera = HTMLHelper.getParameterString(request, "idProgProspera");
                if ( request.getParameter("DocCompleta")!=null) solicitud.documentacionCompleta = (request.getParameter("DocCompleta").equals("si")? 1 : 0);
                
		return solicitud;

	}



	public static SolicitudVO getPropuestaComite (SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		solicitud.montoPropuesto = HTMLHelper.getParameterDouble(request, "montoPropuesto");
		solicitud.plazoPropuesto = HTMLHelper.getParameterInt(request, "plazoPropuesto");
		solicitud.frecuenciaPagoPropuesta = HTMLHelper.getParameterInt(request, "frecuenciaPagoPropuesta");
		solicitud.destinoCredito = HTMLHelper.getParameterInt(request, "destinoCredito");
		return solicitud;
	}



	public static ArrendatarioVO getArrendatario (ArrendatarioVO arrendatario, HttpServletRequest request) throws Exception{

		arrendatario.idTipo = HTMLHelper.getParameterInt(request, "tipoArrendatario");
		arrendatario.nombre = request.getParameter("nombreArrendatario");
		arrendatario.telefono = request.getParameter("telefonoArrendatario");
		arrendatario.horarioLlamada = request.getParameter("horaLlamadaArrendatario");
		arrendatario.tiempoConocimiento = request.getParameter("tiempoConocimiento");
		arrendatario.conocimientoOcupacion = request.getParameter("conocimientoOcupacion");
		arrendatario.conocimientoVivienda = request.getParameter("conocimientoVivienda");
		arrendatario.relacion = request.getParameter("relacionArrendatario");
		arrendatario.inmuebleRenta = request.getParameter("inmuebleRenta");
		arrendatario.tiempoRenta = request.getParameter("tiempoRentaArrendatario");
		arrendatario.existenciaContrato = request.getParameter("existenciaContrato");
		arrendatario.duracionContrato = request.getParameter("duracionContrato");
		arrendatario.puntualidadPago = request.getParameter("puntualidadPago");
		arrendatario.conductaAtraso = request.getParameter("conductaAtraso");
		arrendatario.planRentaFutura = request.getParameter("planRentaFutura");
		arrendatario.reconmendacionCredito = request.getParameter("reconmendacionCredito");
		arrendatario.descripcionCliente = request.getParameter("descripcionCliente");
		arrendatario.disponibilidadRespaldo = request.getParameter("disponibilidadRespaldo");
		arrendatario.calificacionCliente = HTMLHelper.getParameterInt(request, "calificacionCliente");
		//arrendatario.fechaRealizacionConsulta = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaRealizacionConsulta").getTime());
		arrendatario.fechaRealizacionConsulta  =  Convertidor.stringToSqlDate(request.getParameter("fechaRealizacionConsulta"));
		arrendatario.direccion = request.getParameter("direccion");
		
		return arrendatario;
	}

	public static ExpedienteVO getExpediente(ExpedienteVO expediente, HttpServletRequest request) throws Exception{

		expediente.solicitudcredito = HTMLHelper.getParameternotNull(request.getParameter("solicitudcredito"));
		expediente.idtitular = HTMLHelper.getParameternotNull(request.getParameter("idtitular"));
		expediente.idsolidario = HTMLHelper.getParameternotNull(request.getParameter("idsolidario"));
		expediente.idaval = HTMLHelper.getParameternotNull(request.getParameter("idaval"));
		expediente.compdomicilio = HTMLHelper.getParameternotNull(request.getParameter("compdomicilio"));
		expediente.autorizasic = HTMLHelper.getParameternotNull(request.getParameter("autorizasic"));
		expediente.consultatitular = HTMLHelper.getParameternotNull(request.getParameter("consultatitular"));
		expediente.consultasolidario = HTMLHelper.getParameternotNull(request.getParameter("consultasolidario"));
		expediente.consultaavales = HTMLHelper.getParameternotNull(request.getParameter("consultaavales"));
		expediente.consultacirctitular = HTMLHelper.getParameternotNull(request.getParameter("consultacirctitular"));
		expediente.consultacircsolidario = HTMLHelper.getParameternotNull(request.getParameter("consultacircsolidario"));
		expediente.consultacircavales = HTMLHelper.getParameternotNull(request.getParameter("consultacircavales"));
		expediente.consultaintertitular = HTMLHelper.getParameternotNull(request.getParameter("consultaintertitular"));
		expediente.consultaintersolidario = HTMLHelper.getParameternotNull(request.getParameter("consultaintersolidario"));
		expediente.consultainteravales = HTMLHelper.getParameternotNull(request.getParameter("consultainteravales"));
		expediente.formatoevaluacion = HTMLHelper.getParameternotNull(request.getParameter("formatoevaluacion"));
		expediente.perfiloperaciones = HTMLHelper.getParameternotNull(request.getParameter("perfiloperaciones"));
		expediente.formatoref = HTMLHelper.getParameternotNull(request.getParameter("formatoref"));
		expediente.formatocredito = HTMLHelper.getParameternotNull(request.getParameter("formatocredito"));
		expediente.bitacoracobranza = HTMLHelper.getParameternotNull(request.getParameter("bitacoracobranza"));
		expediente.tablaamort = HTMLHelper.getParameternotNull(request.getParameter("tablaamort"));
		expediente.pagare = HTMLHelper.getParameternotNull(request.getParameter("pagare"));
		expediente.contratocredito = HTMLHelper.getParameternotNull(request.getParameter("contratocredito"));
		expediente.factgarantia = HTMLHelper.getParameternotNull(request.getParameter("factgarantia"));
		expediente.formatoseguro = HTMLHelper.getParameternotNull(request.getParameter("formatoseguro"));
		expediente.facturabiengarantia = HTMLHelper.getParameternotNull(request.getParameter("facturabiengarantia"));
		expediente.reglamentointerno = HTMLHelper.getParameternotNull(request.getParameter("reglamentointerno"));
		expediente.actaformaciongrupo = HTMLHelper.getParameternotNull(request.getParameter("actaformaciongrupo"));
		expediente.anexobgrupal = HTMLHelper.getParameternotNull(request.getParameter("anexobgrupal"));
		
		return expediente;
	}

	public static NegocioVO getNegocioVO(NegocioVO negocio, HttpServletRequest request) throws Exception{

		negocio.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		negocio.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		negocio.razonSocial = request.getParameter("razonSocial");
		negocio.rfc = request.getParameter("rfc");
		negocio.activiad = HTMLHelper.getParameterInt(request, "actividad");
		negocio.sector = HTMLHelper.getParameterInt(request, "sector");
		negocio.telefono = request.getParameter("telefono");
		negocio.telefonoCelular = request.getParameter("telefonoCelular");
		negocio.fechaVisita = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaVisita").getTime());
		negocio.tiempoExperiencia = request.getParameter("tiempoExperiencia");
		negocio.empleados = HTMLHelper.getParameterInt(request, "empleados");
		negocio.ventasContado = HTMLHelper.getParameterInt(request, "ventasContado");
		negocio.ventasCredito = HTMLHelper.getParameterInt(request, "ventasCredito");
		negocio.comprasContado = HTMLHelper.getParameterInt(request, "comprasContado");
		negocio.comprasCredito = HTMLHelper.getParameterInt(request, "comprasCredito");
		negocio.situacionLocal = HTMLHelper.getParameterInt(request, "situacionLocal");
		negocio.entornoNegocio = HTMLHelper.getParameterInt(request, "entornoNegocio");
		negocio.registrosContables = HTMLHelper.getParameterInt(request, "registrosContables");
		negocio.autorizacionesNegocio = HTMLHelper.getParameterInt(request, "autorizacionesNegocio");

		return negocio;
	}



	public static InformacionFinancieraVO getInformacionFinanciera(InformacionFinancieraVO informacion, HttpServletRequest request) throws Exception{

		informacion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		informacion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		informacion.ventas = HTMLHelper.getParameterDouble(request, "ventas");
		informacion.costoVentas = HTMLHelper.getParameterDouble(request, "costoVentas");
		informacion.utilidadBruta = HTMLHelper.getParameterDouble(request, "utilidadBruta");
		informacion.gastosOperacion = HTMLHelper.getParameterDouble(request, "gastosOperacion");
		informacion.utilidadNegocio = HTMLHelper.getParameterDouble(request, "utilidadNegocio");
		informacion.otrosIngresosFamilia = HTMLHelper.getParameterDouble(request, "otrosIngresosFamilia");
		informacion.gastosFamilia = HTMLHelper.getParameterDouble(request, "gastosFamilia");
		informacion.utilidadNetaFamilia = HTMLHelper.getParameterDouble(request, "utilidadNetaFamilia");
		informacion.utilidadUnidadFamilia = HTMLHelper.getParameterDouble(request, "utilidadUnidadFamilia");
		informacion.efectivoNegocio = HTMLHelper.getParameterDouble(request, "efectivoNegocio");
		informacion.cuentasCorrientesAhorros = HTMLHelper.getParameterDouble(request, "cuentasCorrientesAhorros");
		informacion.cuentasCobrar = HTMLHelper.getParameterDouble(request, "cuentasCobrar");
		informacion.inventarios = HTMLHelper.getParameterDouble(request, "inventarios");
		informacion.activoCorriente = HTMLHelper.getParameterDouble(request, "activoCorriente");
		informacion.inmuebles = HTMLHelper.getParameterDouble(request, "inmuebles");
		informacion.maquinariaEquipo = HTMLHelper.getParameterDouble(request, "maquinariaEquipo");
		informacion.vehiculos = HTMLHelper.getParameterDouble(request, "vehiculos");
		informacion.activoFijo = HTMLHelper.getParameterDouble(request, "activoFijo");
		informacion.otrosActivos = HTMLHelper.getParameterDouble(request, "otrosActivos");
		informacion.totalOtrosActivos = HTMLHelper.getParameterDouble(request, "totalOtrosActivos");
		informacion.totalActivo = HTMLHelper.getParameterDouble(request, "totalActivo");
		informacion.pasivoCortoPlazo = HTMLHelper.getParameterDouble(request, "pasivoCortoPlazo");
		informacion.pasivoLargoPlazo = HTMLHelper.getParameterDouble(request, "pasivoLargoPlazo");
		informacion.totalPasivo = HTMLHelper.getParameterDouble(request, "totalPasivo");
		informacion.capitalAportado = HTMLHelper.getParameterDouble(request, "capitalAportado");
		informacion.utilidad = HTMLHelper.getParameterDouble(request, "utilidad");
		informacion.totalCapitalContable = HTMLHelper.getParameterDouble(request, "totalCapitalContable");
		informacion.totalPasivoMasCapitalContable = HTMLHelper.getParameterDouble(request, "totalPasivoMasCapitalContable");
		informacion.margenUtilidadOper = HTMLHelper.getParameterInt(request, "margenUtilidadOper");
		informacion.margenUtilidadNeta = HTMLHelper.getParameterInt(request, "margenUtilidadNeta");
		informacion.indiceLiquidez = HTMLHelper.getParameterInt(request, "indiceLiquidez");
		informacion.rotacionInventarios = HTMLHelper.getParameterInt(request, "rotacionInventarios");
		informacion.rotacionCapitalTrabajoSol = HTMLHelper.getParameterInt(request, "rotacionCapitalTrabajoSol");
		informacion.rotacionCuentasCobrar = HTMLHelper.getParameterInt(request, "rotacionCuentasCobrar");
		informacion.rotacionCuentasPagar = HTMLHelper.getParameterInt(request, "rotacionCuentasPagar");
		informacion.endeudamientoTotal = HTMLHelper.getParameterInt(request, "endeudamientoTotal");
		informacion.endeudamientoFuturoTotal = HTMLHelper.getParameterInt(request, "endeudamientoFuturoTotal");
		informacion.capacidadPago = HTMLHelper.getParameterInt(request, "capacidadPago");
		informacion.diasVenta = HTMLHelper.getParameterInt(request, "diasVenta");
		return informacion;
	}



	public static EmpleoVO getEmpleoVO(EmpleoVO empleo, HttpServletRequest request) throws Exception{

		empleo.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		empleo.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		empleo.estatus = HTMLHelper.getParameterInt(request, "estatus");
		empleo.antEmpleo = HTMLHelper.getParameterInt(request, "antEmpleo");
                empleo.fechaInicioNeg = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaInicioNeg"));
		empleo.tipoContrato = HTMLHelper.getParameterInt(request, "tipoContrato");
		empleo.plazoContrato = HTMLHelper.getParameterInt(request, "plazoContrato");
		empleo.turno = HTMLHelper.getParameterInt(request, "turno");
		empleo.afiliacionIMSS = HTMLHelper.getParameterInt(request, "afiliacionIMSS");
		empleo.razonSocial = request.getParameter("razonSocial");
		empleo.numeroEmpleados = HTMLHelper.getParameterInt(request, "numeroEmpleados");
		empleo.arraigoEmpresa = HTMLHelper.getParameterInt(request, "arraigoEmpresa");
		
		empleo.numEmpleado = request.getParameter("numeroEmpleado");
		empleo.area = request.getParameter("area");
		empleo.puesto = request.getParameter("puesto");
		empleo.telefono = request.getParameter("telefono");
		empleo.extension = request.getParameter("extension");
		empleo.sueldoMensual = HTMLHelper.getParameterDouble(request, "sueldoMensual");
		empleo.otrosIngresos = HTMLHelper.getParameterDouble(request, "otrosIngresos");
		empleo.fteOtrosIngresos = HTMLHelper.getParameterInt(request, "fteOtrosIngresos");
		empleo.formaIngreso = HTMLHelper.getParameterInt(request, "formaingreso");
		empleo.tipoSector = HTMLHelper.getParameterInt(request, "tiposector");
		empleo.dependencia = HTMLHelper.getParameterInt(request, "dependencia");
                empleo.tipoSector = HTMLHelper.getParameterInt(request, "sector");
                empleo.referencia = HTMLHelper.getParameterString(request, "referencia");
                empleo.ubicacionNegocio = HTMLHelper.getParameterInt(request,"ubicacionNegocio");
		
		return empleo;
	}


	public static ViviendaVO getViviendaVO(ViviendaVO vivienda, HttpServletRequest request) throws Exception{

		vivienda.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		vivienda.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		vivienda.estatus = HTMLHelper.getParameterInt(request, "estatus");
		vivienda.tipoVivienda = HTMLHelper.getParameterInt(request, "tipoVivienda");
		vivienda.impAlquilerHipoteca = HTMLHelper.getParameterDouble(request, "impAlquilerHipoteca");
		vivienda.nivelVivienda = HTMLHelper.getParameterInt(request, "nivelVivienda");
		vivienda.zona = HTMLHelper.getParameterInt(request, "zona");
		vivienda.pisos = HTMLHelper.getParameterInt(request, "pisos");
		vivienda.cuartos = HTMLHelper.getParameterInt(request, "cuartos");
		vivienda.fachada = HTMLHelper.getParameterInt(request, "fachada");
		vivienda.techo = HTMLHelper.getParameterInt(request, "techo");
		vivienda.tiempoResidencia = HTMLHelper.getParameterInt(request, "tiempoResidencia");
		return vivienda;
	}


	public static CapacidadPagoVO getCapacidadPagoVO(CapacidadPagoVO capacidad, HttpServletRequest request) throws Exception{

		capacidad.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		capacidad.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		capacidad.estatus = HTMLHelper.getParameterInt(request, "estatus");
		capacidad.ingresosNomina = HTMLHelper.getParameterDouble(request, "ingresosNomina");
		capacidad.otrosNoComprobables = HTMLHelper.getParameterDouble(request, "otrosNoComprobables");
		capacidad.fuenteOtrosIngresos = HTMLHelper.getParameterInt(request, "fuenteOtrosIngresos");
		capacidad.otrosIngresos = HTMLHelper.getParameterDouble(request, "otrosIngresos");
		
		capacidad.marcaModeloAuto = request.getParameter("marcaModeloAuto");
		capacidad.estatusAuto = HTMLHelper.getParameterInt(request, "estatusAuto");
		capacidad.valorAuto = HTMLHelper.getParameterDouble(request, "valorAuto");
		
		
		capacidad.rentaVivienda = HTMLHelper.getParameterDouble(request, "rentaVivienda");
		capacidad.pagoDeuda = HTMLHelper.getParameterDouble(request, "pagoDeuda");
		capacidad.otrosGastos = HTMLHelper.getParameterDouble(request, "otrosGastos");
		capacidad.disponibleMensual = HTMLHelper.getParameterDouble(request, "disponibleMensual");
		capacidad.cuotaSobreDisponible = HTMLHelper.getParameterInt(request, "cuotaSobreDisponible");
		capacidad.cuotaSobreIngresoBruto = HTMLHelper.getParameterInt(request, "cuotaSobreIngresoBruto");

		return capacidad;
	}


	public static ReferenciaLaboralVO getReferenciaLaboralVO(ReferenciaLaboralVO referenciaLaboral, HttpServletRequest request) throws Exception{

		referenciaLaboral.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		referenciaLaboral.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		referenciaLaboral.estatus = HTMLHelper.getParameterInt(request, "estatus");
		//referenciaLaboral.fechaElaboracion = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaElaboracion").getTime());
		referenciaLaboral.fechaElaboracion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaElaboracion"));
		referenciaLaboral.nombreEmpresa = request.getParameter("nombreEmpresa");
		referenciaLaboral.nombreInformante = request.getParameter("nombreInformante");
		referenciaLaboral.cargo = request.getParameter("cargo");
		referenciaLaboral.giroEmpresa = request.getParameter("giroEmpresa");
		referenciaLaboral.rfc = request.getParameter("rfc");
		referenciaLaboral.direccionVerdadera = HTMLHelper.getParameterInt(request, "direccionVerdadera");
		referenciaLaboral.telefono1 = request.getParameter("telefono1");
		referenciaLaboral.telefono2 = request.getParameter("telefono2");
		referenciaLaboral.inicioOperaciones = request.getParameter("inicioOperaciones");
		referenciaLaboral.numeroEmpleados = HTMLHelper.getParameterInt(request, "numeroEmpleados");
		referenciaLaboral.principalesProdsServs = request.getParameter("principalesProdsServs");
		referenciaLaboral.fechaAltaHacienda = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaAltaHacienda"));
		referenciaLaboral.puesto = request.getParameter("puesto");
		referenciaLaboral.descPuesto = request.getParameter("descPuesto");
		referenciaLaboral.inicioEmpleo = request.getParameter("inicioEmpleo");
		referenciaLaboral.reconmendacionCredito = HTMLHelper.getParameterInt(request, "reconmendacionCredito");
		referenciaLaboral.perspectivasMeses = HTMLHelper.getParameterInt(request, "perspectivasMeses");
		referenciaLaboral.perspectivasAnios = HTMLHelper.getParameterInt(request, "perspectivasAnios");
		referenciaLaboral.jornada = HTMLHelper.getParameterInt(request, "jornada");
		referenciaLaboral.horario = request.getParameter("horario");
		referenciaLaboral.diasDescanso = request.getParameter("diasDescanso");
		referenciaLaboral.salarioFijo = HTMLHelper.getParameterDouble(request, "salarioFijo");
		referenciaLaboral.incentivos = HTMLHelper.getParameterDouble(request, "incentivos");
		referenciaLaboral.seguroMedico = HTMLHelper.getParameterInt(request, "seguroMedico");
		referenciaLaboral.tipoSeguro = request.getParameter("tipoSeguro");
		
		return referenciaLaboral;
	}

	public static boolean asignaFechaDesembolso( Connection conn, SolicitudVO solicitud, ClienteVO cliente, HttpServletRequest request )throws Exception{
		
		boolean procesoOK = true;
		
		if ( request.getParameter("desembolsado")==null )
			return procesoOK;
                if(solicitud.subproducto == ClientesConstants.ID_INTERCICLO&&solicitud.desembolsado!=ClientesConstants.CANCELADO)
                    solicitud.desembolsado = HTMLHelper.getParameterInt(request, "desembolsado");                
		switch (solicitud.desembolsado){
			case 0:
				break;
			case 1:
				solicitud.fechaDesembolso = null;
				break;
			case 2:
				if ( solicitud.fechaDesembolso==null )
					solicitud.fechaDesembolso = new Date(System.currentTimeMillis());
				if( requiereNumeroCheque (solicitud.tipoOperacion) && solicitud.reestructura==0 ){
					if( cliente.idBanco > 0 ){
						String referencia = ClientesUtil.makeReferencia(cliente, solicitud.idSolicitud);
						OrdenDePagoVO[] ordenesDePagoActuales = new OrdenDePagoDAO().getOrdenesDePago( cliente.idCliente, solicitud.idSolicitud );
						
						if( !GrupoHelper.existeOrdenDePagoActiva(ordenesDePagoActuales) ){
							referencia = GrupoHelper.modificaReferencia(referencia, ordenesDePagoActuales);
							TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
							double monto = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones);
							OrdenDePagoVO oPago = new OrdenDePagoVO();
							
							oPago.setIdCliente  ( cliente.idCliente );
							oPago.setIdSolicitud( solicitud.idSolicitud );
							oPago.setIdSucursal ( solicitud.idSucursal );
							oPago.setUsuario    ( request.getRemoteUser() );
							oPago.setNombre     ( cliente.nombreCompleto );
							oPago.setMonto      ( monto );
							oPago.setIdBanco    ( cliente.idBanco );
							oPago.setReferencia ( referencia );
							oPago.setEstatus    ( 1 );
							oPago.setIdSucursal ( cliente.idSucursal);
							solicitud.numCheque = ChequerasHelper.asignaOrdenDePago(conn, oPago);
// JBL NOV-10 asigna la orden de pago
							solicitud.ordenPago  = new OrdenDePagoDAO().getOrdenDePago(cliente.idCliente, solicitud.idSolicitud);

						}else{
							procesoOK = false;
							conn.rollback();
						}
						
					}
					else{
						System.out.println("Generando Cheque " );
						solicitud.numCheque = ChequerasHelper.asignaCheque(conn, cliente.idSucursal, cliente.idCliente, solicitud.idSolicitud, 0, 0);
						procesoOK = true;
					}
					
					
				}	
				//else
					//asignaNumeroFactura(solicitud, request);
				break;
		}
		return procesoOK;
		
	}

	public static boolean registerIBS( SolicitudVO solicitud, ClienteVO cliente, HttpServletRequest request, Vector notificaciones )throws Exception{

		if( doRegisterIBS (solicitud.tipoOperacion) && HTMLHelper.getParameterInt(request, "desembolsado") == ClientesConstants.DESEMBOLSADO ){
			ClienteHelperIBS clienteHelper = new ClienteHelperIBS();
			CreditoHelperIBS creditoHelper = new CreditoHelperIBS();

			//Si el cliente no tiene asignado un n�mero de cliente en IBS lo genera
			if ( cliente.idClienteIBS == 0 )
				clienteHelper.registraClienteCuentaIBS(cliente, solicitud, request, true, notificaciones);
			else if ( cliente.idClienteIBS != 0 && solicitud.idCuentaIBS == 0 )
				clienteHelper.registraClienteCuentaIBS(cliente, solicitud, request, false, notificaciones);
	
			if ( cliente.idClienteIBS != 0 && solicitud.idCuentaIBS != 0 ){
				if ( !creditoHelper.registraCreditoIBS(cliente, solicitud, request, notificaciones) )
					return false;
			}else{
				return false;
			}
			
			/*Pruebas IBS
			solicitud.idCuentaIBS=88888;
			solicitud.idCreditoIBS=88888;
			cliente.idClienteIBS=88888;
			new ClienteDAO().updateClienteIBS(cliente.idClienteIBS, cliente.idCliente );
			new SolicitudDAO().updateSolicitudCredito(solicitud);*/
			
			System.out.println("Número Cliente IBS: " + cliente.idClienteIBS);
			System.out.println("Número Crédito IBS: " + solicitud.idCreditoIBS);
			System.out.println("Número Cuenta  IBS: " + solicitud.idCuentaIBS);
		}
		
		return true;
	}


	public static boolean requiereNumeroCheque (int idTipoOperacion){
		switch (idTipoOperacion){
			case ClientesConstants.CONSUMO:
				return true;
			case ClientesConstants.MICROCREDITO:
				return true;
			case ClientesConstants.GRUPAL:
				return true;
			case ClientesConstants.VIVIENDA:
				return true;
			case ClientesConstants.SELL_FINANCE:
				return true;
			case ClientesConstants.CREDIHOGAR:
				return true;
		}
		return false;
	}

	public static boolean doRegisterIBS (int idTipoOperacion){
		switch (idTipoOperacion){
			case ClientesConstants.CONSUMO:
				return true;
			case ClientesConstants.MICROCREDITO:
				return true;
			case ClientesConstants.GRUPAL:
				return true;
			case ClientesConstants.VIVIENDA:
				return false;
			case ClientesConstants.SELL_FINANCE:
				return false;
			case ClientesConstants.CREDIHOGAR:
				return false;
		}
		return false;
	}

	public static boolean asignaNumeroFactura (SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		SellFinanceDAO sellFinanceDAO = new SellFinanceDAO();
		switch (solicitud.tipoOperacion){
			case ClientesConstants.SELL_FINANCE:
				solicitud.sellFinance.numeroFactura = HTMLHelper.getParameterString(request, "numeroFactura");
				sellFinanceDAO.updateNumFactura(solicitud.sellFinance);
				return false;
		}
		return false;
	}

	public static SolicitudVO getCreditoSolicitado (SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		solicitud.montoSolicitado = HTMLHelper.getParameterDouble(request, "montoSolicitado");
		solicitud.plazoSolicitado = HTMLHelper.getParameterInt(request, "plazoSolicitado");
		solicitud.frecuenciaPagoSolicitada = HTMLHelper.getParameterInt(request, "frecuenciaPagoSolicitada");
		solicitud.cuota = HTMLHelper.getParameterDouble(request, "cuota");
		solicitud.destinoCredito = HTMLHelper.getParameterInt(request, "destinoCredito");
		solicitud.numCheque = request.getParameter("numeroCheque");
		solicitud.desembolsado = HTMLHelper.getParameterInt(request, "desembolsado"); 
		solicitud.estatus = HTMLHelper.getParameterInt(request, "estatus");
		if(solicitud.comentarios==null)
			solicitud.comentarios = "";
		if(request.getParameter("comentariosNvo").trim().length()!=0)
			solicitud.comentarios += "[" + request.getRemoteUser() + " " + Convertidor.dateToString(new java.util.Date(), ClientesConstants.FORMATO_CORTO_FECHA_HORA) +"]" + " - " + request.getParameter("comentariosNvo") + "\n";
		if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE ){
			if(solicitud.sellFinance == null){
			solicitud.sellFinance = new SellFinanceVO();
			solicitud.sellFinance.idCliente = solicitud.idCliente;
			solicitud.sellFinance.idSolicitud = solicitud.idSolicitud;
			solicitud.sellFinance.numeroFactura = HTMLHelper.getParameterString(request, "numeroFactura");
			if ( request.getParameter("marca")!=null ) solicitud.sellFinance.idMarca = HTMLHelper.getParameterInt(request, "marca");
			if ( request.getParameter("producto")!=null ) solicitud.sellFinance.idProducto = HTMLHelper.getParameterInt(request, "producto");
			}else{
				solicitud.sellFinance.numeroFactura = HTMLHelper.getParameterString(request, "numeroFactura");
				if ( request.getParameter("marca")!=null ) solicitud.sellFinance.idMarca = HTMLHelper.getParameterInt(request, "marca");
				if ( request.getParameter("producto")!=null ) solicitud.sellFinance.idProducto = HTMLHelper.getParameterInt(request, "producto");
			}
		}
		return solicitud;
	}


	public static ScoringVO getScoring(ScoringVO scoring, HttpServletRequest request) throws Exception{

		scoring.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		scoring.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		scoring.calificacionSIC = HTMLHelper.getParameterInt(request, "calificacionSIC");
		scoring.tipoCuenta = HTMLHelper.getParameterInt(request, "tipoCuenta");
		scoring.antCuenta = HTMLHelper.getParameterInt(request, "antCuenta");
		scoring.numBuquedas = HTMLHelper.getParameterInt(request, "numBuquedas");
		scoring.genero = HTMLHelper.getParameterInt(request, "genero");
		scoring.edad = HTMLHelper.getParameterInt(request, "anios");
		scoring.estadoCivil = HTMLHelper.getParameterInt(request, "catEstadoCivil");
		scoring.antLaboral = HTMLHelper.getParameterInt(request, "antLaboralAnios");
		scoring.tipoContrato = HTMLHelper.getParameterInt(request, "tipoContrato");
		scoring.situacionVivienda = HTMLHelper.getParameterInt(request, "situacionVivienda");
		scoring.alquilerHipoteca = HTMLHelper.getParameterDouble(request, "alquilerHipoteca");
		scoring.tiempoResidencia = HTMLHelper.getParameterInt(request, "tiempoResidenciaAnios");
		scoring.dependientesEconomicos = HTMLHelper.getParameterInt(request, "dependientesEconomicos");
		scoring.ingresosNomina = HTMLHelper.getParameterDouble(request, "ingresosNomina");
		scoring.otrosNoComprobables = HTMLHelper.getParameterDouble(request, "otrosNoComprobables");
		scoring.otrosIngresos = HTMLHelper.getParameterDouble(request, "otrosIngresos");
		scoring.rentaVivienda = HTMLHelper.getParameterDouble(request, "rentaVivienda");
		scoring.pagoDeuda = HTMLHelper.getParameterDouble(request, "pagoDeuda");
		scoring.otrosGastos = HTMLHelper.getParameterDouble(request, "otrosGastos");
		scoring.disponibleMensual = HTMLHelper.getParameterDouble(request, "disponibleMensual");
		scoring.cuotaSobreDisponible = HTMLHelper.getParameterDouble(request, "cuotaSobreDisponible");
		scoring.cuotaSobreIngresoBruto = HTMLHelper.getParameterDouble(request, "cuotaSobreIngresoBruto");
		scoring.nivelVivienda = HTMLHelper.getParameterInt(request, "nivelVivienda");
		scoring.calificacionZona = HTMLHelper.getParameterInt(request, "calificacionZona");
		scoring.pisosVivienda = HTMLHelper.getParameterInt(request, "pisosVivienda");
		scoring.habitacionesVivienda = HTMLHelper.getParameterInt(request, "habitacionesVivienda");
		scoring.caracteristicasFachada = HTMLHelper.getParameterInt(request, "caracteristicasFachada");
		scoring.caracteristicasTecho = HTMLHelper.getParameterInt(request, "caracteristicasTecho");
		scoring.arraigoEmpresa = HTMLHelper.getParameterInt(request, "arraigoEmpresaAnios");
		scoring.numeroEmpleados = HTMLHelper.getParameterInt(request, "numeroEmpleados");
		scoring.jornada = HTMLHelper.getParameterInt(request, "jornada");
		scoring.plazoContrato = HTMLHelper.getParameterInt(request, "plazoContrato");
		scoring.referencia1 = HTMLHelper.getParameterInt(request, "referencia1");
		scoring.referencia2 = HTMLHelper.getParameterInt(request, "referencia2");
		scoring.referenciaLaboral = HTMLHelper.getParameterInt(request, "referenciaLaboral");
		scoring.referenciaArrendador = HTMLHelper.getParameterInt(request, "referenciaArrendador");
		scoring.destinoCredito = HTMLHelper.getParameterInt(request, "destinoCredito");
		
		scoring.monto = HTMLHelper.getParameterDouble(request, "monto");
		scoring.plazo = HTMLHelper.getParameterInt(request, "plazo");
		
		scoring.tasa = HTMLHelper.getParameterInt(request, "tasa");
		scoring.comision = HTMLHelper.getParameterInt(request, "comision");
		//scoring.montoConComision = HTMLHelper.getParameterDouble(request, "montoConComision");
		//scoring.cuota = HTMLHelper.getParameterDouble(request, "cuota");
		
		
		return scoring;
	}


	public static ScoringVO getCapacidadPago(ScoringVO scoring, HttpServletRequest request) throws Exception{

		scoring.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		scoring.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		scoring.ingresosNomina = HTMLHelper.getParameterDouble(request, "ingresosNomina");
		scoring.otrosNoComprobables = HTMLHelper.getParameterDouble(request, "otrosNoComprobables");
		scoring.otrosIngresos = HTMLHelper.getParameterDouble(request, "otrosIngresos");
		scoring.rentaVivienda = HTMLHelper.getParameterDouble(request, "rentaVivienda");
		scoring.pagoDeuda = HTMLHelper.getParameterDouble(request, "pagoDeuda");
		scoring.otrosGastos = HTMLHelper.getParameterDouble(request, "otrosGastos");
		scoring.disponibleMensual = HTMLHelper.getParameterDouble(request, "disponibleMensual");
		scoring.cuotaSobreDisponible = HTMLHelper.getParameterDouble(request, "cuotaSobreDisponible");
		scoring.cuotaSobreIngresoBruto = HTMLHelper.getParameterDouble(request, "cuotaSobreIngresoBruto");
		
		scoring.monto = HTMLHelper.getParameterDouble(request, "monto");
		scoring.plazo = HTMLHelper.getParameterInt(request, "plazo");
		scoring.periodicidad  = HTMLHelper.getParameterInt(request, "periodicidad");
		
		scoring.tasa = HTMLHelper.getParameterInt(request, "tasa");
		scoring.comision = HTMLHelper.getParameterInt(request, "comision");
		//scoring.montoConComision = HTMLHelper.getParameterDouble(request, "montoConComision");
		//scoring.cuota = HTMLHelper.getParameterDouble(request, "cuota");
		
		
		return scoring;
	}


	
	public static DisposicionVO getDisposicionVO(DisposicionVO disposicion, HttpServletRequest request) throws Exception{

		disposicion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		disposicion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		//disposicion.idDisposicion = HTMLHelper.getParameterInt(request, "idDisposicion");
		disposicion.monto = HTMLHelper.getParameterDouble(request, "monto");
		return disposicion;
	}

	public static SolicitudVO generaNuevaSolicitud (int numCliente) throws Exception{
		return generaNuevaSolicitud(numCliente, false);
	}

	public static SolicitudVO generaNuevaSolicitud (int numCliente, boolean restructura) throws Exception{
		SolicitudVO nuevaSolicitud = null;
		Calendar cal = Calendar.getInstance();
		
		SolicitudVO[] solicitudes = new SolicitudDAO().getSolicitudes(numCliente);
		for( int i=solicitudes.length-1 ; i>=0 ; i-- ){
			if( solicitudes[i].tipoOperacion==ClientesConstants.GRUPAL && solicitudes[i].estatus==ClientesConstants.SOLICITUD_AUTORIZADA ){
				nuevaSolicitud = new SolicitudVO();
				nuevaSolicitud = solicitudes[i];
				nuevaSolicitud.estatus = ClientesConstants.SOLICITUD_AUTORIZADA;
				nuevaSolicitud.fechaFirma = Convertidor.toSqlDate(cal.getTime());
				nuevaSolicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
				nuevaSolicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
				nuevaSolicitud.fechaDesembolso = null;
				nuevaSolicitud.numCheque = null;
				if( restructura ){
					nuevaSolicitud.reestructura = 1;
					nuevaSolicitud.solicitudReestructura = nuevaSolicitud.idSolicitud;
					nuevaSolicitud.tipoOperacion = ClientesConstants.REESTRUCTURA_GRUPAL;
				}
				break;
			}
		}
		new SolicitudDAO().addSolicitud(numCliente, nuevaSolicitud);
		return nuevaSolicitud;
	}
	
	public static String obtenNumeroCreditoIBS(String Referencia) throws Exception{
		String numCredito = "";
		SolicitudVO solicitud = null;
		SolicitudDAO solDAO = new SolicitudDAO();
		ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
		ReferenciaGeneralVO refVo = new ReferenciaGeneralVO();		
	    refVo = referenciaDAO.getReferenciaGeneral(Referencia);
		
	     if(refVo != null)
		   solicitud = solDAO.getSolicitud(refVo.numcliente, refVo.numSolicitud);
		
		 if(solicitud!=null)
			 numCredito = String.valueOf(solicitud.idCreditoIBS);
	
		 if(numCredito.length()!=12)
			 numCredito = FormatUtil.completaCadena(numCredito, '0', 12);
			 
		return numCredito;
	}

}
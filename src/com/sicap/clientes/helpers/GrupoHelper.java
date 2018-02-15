package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.ibs.ClienteHelperIBS;
import com.sicap.clientes.helpers.ibs.CreditoHelperIBS;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;
import com.sicap.clientes.vo.PlaneacionRenovacionVO;
import com.sicap.clientes.vo.ReporteCobranzaGrupalVO;
import com.sicap.clientes.vo.ReporteVisitaGrupalVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DescongelaPagoGarantiaIndVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import org.apache.log4j.Logger;

public class GrupoHelper {

    private static Logger myLogger = Logger.getLogger(GrupoHelper.class);

    public static GrupoVO getGrupoVO(GrupoVO grupo, HttpServletRequest request) throws Exception {

        grupo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        grupo.nombre = request.getParameter("nombre").toUpperCase().trim();
        grupo.sucursal = HTMLHelper.getParameterInt(request, "idSucursal");
        grupo.fechaFormacion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaFormacion"));
        grupo.idOperacion = HTMLHelper.getParameterInt(request, "idOperacion");
        if (!HTMLHelper.getParameterString(request, "calificacionGrupal").equals("")) {
            grupo.calificacion = HTMLHelper.getParameterString(request, "calificacionGrupal");
        }
        grupo.otraFinaciera = 0;
        if (request.getParameter("otraFin") != null) {
            grupo.otraFinaciera = (request.getParameter("otraFin").equals("si") ? 1 : 0);
        }
        return grupo;

    }

    public static CicloGrupalVO getCicloGrupalVO(CicloGrupalVO ciclo, HttpServletRequest request) throws Exception {

        ciclo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        ciclo.idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        ciclo.estatus = HTMLHelper.getParameterInt(request, "estatus");
        ciclo.diaReunion = HTMLHelper.getParameterInt(request, "diaReunion");
        ciclo.horaReunion = HTMLHelper.getParameterInt(request, "horaReunion");
        ciclo.asesor = HTMLHelper.getParameterInt(request, "asesor");
        ciclo.tasa = HTMLHelper.getParameterInt(request, "idTasa");
        ciclo.coordinador = HTMLHelper.getParameterInt(request, "coordinador");
        ciclo.multaRetraso = HTMLHelper.getParameterDouble(request, "multaRetraso");
        ciclo.multaFalta = HTMLHelper.getParameterDouble(request, "multaFalta");
        if (request.getParameter("fechaValor") != null && !request.getParameter("fechaValor").equals("")) {
            ciclo.fechaValor = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaValor"));
        } else {
            ciclo.fechaValor = new java.sql.Date(System.currentTimeMillis());
        }
        ciclo.estatusT24 = HTMLHelper.getParameterInt(request, "saldoT24");
        ciclo.idTipoCiclo = HTMLHelper.getParameterInt(request, "idTipoCiclo");
        ciclo.plazo = HTMLHelper.getParameterInt(request, "plazo");
        ciclo.fechaDispersion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaDispersion"));
        ciclo.bancoDispersion = HTMLHelper.getParameterInt(request, "bancoDispersion");
        ciclo.fondeador = HTMLHelper.getParameterInt(request, "fondeador");
        return ciclo;

    }

    public static CicloGrupalVO getCicloGrupalApertura(CicloGrupalVO ciclo, HttpServletRequest request) throws Exception {

        ciclo.idDireccionReunion = HTMLHelper.getParameterInt(request, "idDireccionReunion");
        ciclo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        ciclo.idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        ciclo.diaReunion = HTMLHelper.getParameterInt(request, "diaReunion");
        ciclo.horaReunion = HTMLHelper.getParameterInt(request, "horaReunion");
        ciclo.asesor = HTMLHelper.getParameterInt(request, "asesor");
        ciclo.tasa = HTMLHelper.getParameterInt(request, "idTasa");
        ciclo.coordinador = HTMLHelper.getParameterInt(request, "coordinador");
        ciclo.multaRetraso = HTMLHelper.getParameterDouble(request, "multaRetraso");
        ciclo.multaFalta = HTMLHelper.getParameterDouble(request, "multaFalta");
        if (request.getParameter("fechaValor") != null && !request.getParameter("fechaValor").equals("")) {
            ciclo.fechaValor = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaValor"));
        } else {
            ciclo.fechaValor = new java.sql.Date(System.currentTimeMillis());
        }
        ciclo.estatusT24 = HTMLHelper.getParameterInt(request, "saldoT24");
        ciclo.idTipoCiclo = HTMLHelper.getParameterInt(request, "idTipoCiclo");
        ciclo.plazo = HTMLHelper.getParameterInt(request, "plazo");
        ciclo.fechaDispersion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaDispersion"));
        ciclo.bancoDispersion = HTMLHelper.getParameterInt(request, "bancoDispersion");
        ciclo.estatus = HTMLHelper.getParameterInt(request, "estatus");
        ciclo.aperturador = HTMLHelper.getParameterInt(request, "aperturador");
        ciclo.garantia = HTMLHelper.getParameterInt(request, "garantia");
        return ciclo;

    }

    public static ReporteCobranzaGrupalVO getCuestionarioCobranza(ReporteCobranzaGrupalVO reporteCobranza, HttpServletRequest request) throws Exception {

        reporteCobranza.idAlerta = HTMLHelper.getParameterInt(request, "idUnicoAlerta");
        reporteCobranza.numCliente = HTMLHelper.getParameterInt(request, "numCliente");
        reporteCobranza.nombreCliente = HTMLHelper.getParameterString(request, "nombreCliente");
        reporteCobranza.usuario = HTMLHelper.getParameterString(request, "usuario");
        reporteCobranza.motivoNoContacto = HTMLHelper.getParameterInt(request, "R1");
        reporteCobranza.realizaPagos = HTMLHelper.getParameterBoolean(request, "P1");
        reporteCobranza.receptorPagos = HTMLHelper.getParameterInt(request, "P2");
        reporteCobranza.receptorPagosOtro = HTMLHelper.getParameterString(request, "P2_1");
        reporteCobranza.asesorVisitaSemanal = HTMLHelper.getParameterBoolean(request, "P3");
        reporteCobranza.numerofaltas = HTMLHelper.getParameterInt(request, "P4");
        reporteCobranza.asesorPuntual = HTMLHelper.getParameterBoolean(request, "P5");
        reporteCobranza.asesorProductivo = HTMLHelper.getParameterBoolean(request, "P6");
        reporteCobranza.asesorRespeta = HTMLHelper.getParameterBoolean(request, "P7");
        reporteCobranza.asesorRecibePagos = HTMLHelper.getParameterBoolean(request, "P8");
        reporteCobranza.comentarios = HTMLHelper.getParameterString(request, "P9");

        return reporteCobranza;

    }

    public static ReporteVisitaGrupalVO getReporteVisitaGrupal(ReporteVisitaGrupalVO reporteVisita, HttpServletRequest request, CicloGrupalVO ciclo) throws Exception {

        reporteVisita.idAlerta = HTMLHelper.getParameterInt(request, "idUnicoAlerta");
        reporteVisita.usuario = HTMLHelper.getParameterString(request, "usuario");
        reporteVisita.problemasGrupo = HTMLHelper.getParameterInt(request, "S1");
        reporteVisita.problemasAsesor = HTMLHelper.getParameterInt(request, "S2");
        reporteVisita.problemasNegocio = HTMLHelper.getParameterInt(request, "S3");
        reporteVisita.problemasPersonales = HTMLHelper.getParameterInt(request, "S4");
        reporteVisita.problemasOtros = HTMLHelper.getParameterInt(request, "S5");
        reporteVisita.propuestaSolucion = HTMLHelper.getParameterInt(request, "S6");
        reporteVisita.comentarios = HTMLHelper.getParameterString(request, "S7");

        reporteVisita.integrantesVisitados = "";
        for (int i = 0; i < ciclo.integrantes.length; i++) {
            String nombre = "seleccionado" + i;
            String cliente = "idCliente" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                reporteVisita.integrantesVisitados += HTMLHelper.getParameterString(request, cliente) + " ";
            }
        }

        return reporteVisita;

    }

    public static IntegranteCicloVO[] getIntegrantesCiclo(HttpServletRequest request) throws Exception { //AKI

        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        IntegranteCicloVO[] integrantes = new IntegranteCicloVO[numIntegrantes];
        for (int i = 0; i < integrantes.length; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante.idGrupo = idGrupo;
            integrante.idCiclo = idCiclo;
            integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
            integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
            integrante.nombre = request.getParameter("nombre" + i);
            integrante.monto = HTMLHelper.getParameterDouble(request, "monto" + i);
            integrante.comision = HTMLHelper.getParameterInt(request, "comision" + i);
            integrante.montoRefinanciado = HTMLHelper.getParameterDouble(request, "montoRefinanciado" + i);
//			if ( ciclo!=null && ciclo.integrantes!=null )
//				integrante.ordenPago = ciclo.integrantes[i].ordenPago;
//			else
//				integrante.ordenPago = new OrdenDePagoVO();
            //integrante.estatus = HTMLHelper.getParameterInt(request, "estatus");
            integrante.rol = HTMLHelper.getParameterInt(request, "rol" + i);
            integrante.medioCobro = HTMLHelper.getParameterInt(request, "medioCobro" + i);
            integrantes[i] = integrante;

        }
        return integrantes;

    }

    public static CicloGrupalVO getIntegrantesCicloRenovacion(HttpServletRequest request, CicloGrupalVO ciclo) throws Exception { //AKI

        HttpSession session = request.getSession();
        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        int numIntegrantesChecked = GrupoHelper.getIntegrantesCicloChecked(request, numIntegrantes);
        IntegranteCicloVO[] integrantes = new IntegranteCicloVO[numIntegrantes];
        IntegranteCicloVO[] integrantesSeleccionados = new IntegranteCicloVO[numIntegrantesChecked];

        double monto = 0;

        for (int i = 0; i < integrantes.length; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante.idGrupo = idGrupo;
            integrante.idCiclo = idCiclo;
            integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
            integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
            integrante.nombre = request.getParameter("nombre" + i);
            myLogger.debug(integrante.nombre);
            integrante.monto = HTMLHelper.getParameterDouble(request, "monto" + i);
            integrante.calificacion = HTMLHelper.getParameterInt(request, "calificacion" + i);
            integrante.aceptaRegular = HTMLHelper.getParameterInt(request, "aceptaRegular" + i);
            integrante.comision = HTMLHelper.getParameterInt(request, "comision" + i);
            integrante.montoRefinanciado = HTMLHelper.getParameterDouble(request, "montoRefinanciado" + i);
            integrante.rol = HTMLHelper.getParameterInt(request, "rol" + i);
            integrante.seguro = HTMLHelper.getParameterInt(request, "conseguro" + i);
            integrante.empleo = HTMLHelper.getParameterInt(request, "conempleo" + i);
            integrante.grupo = HTMLHelper.getParameterString(request, "activoen" + i);
            integrante.medioCobro = HTMLHelper.getParameterInt(request, "medioCobro" + i);
            integrante.esInterciclo = HTMLHelper.getParameterInt(request, "esInterciclo" + i);
            integrantes[i] = integrante;
        }

        for (int i = 0, f = 0; i < integrantes.length; i++) {
            String nombre = "desembolso" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                integrantesSeleccionados[f] = integrantes[i];
                monto += integrantesSeleccionados[f].monto;    //Monto sin comision traido del request
                f++;
            }
        }

        if (ciclo.integrantes == null && session.getAttribute("INTEGRANTES_CICLO_SESION") != null) {
            ciclo.integrantes = (IntegranteCicloVO[]) session.getAttribute("INTEGRANTES_CICLO_SESION");
            session.removeAttribute("INTEGRANTES_CICLO_SESION");
        }

        for (int i = 0; i < integrantesSeleccionados.length; i++) {
            for (int j = 0; ciclo.integrantes != null && j < ciclo.integrantes.length; j++) {
                if (integrantesSeleccionados[i].idCliente == ciclo.integrantes[j].idCliente) {
                    integrantesSeleccionados[i].primaSeguro = ciclo.integrantes[j].primaSeguro;
                }
            }
        }

        ciclo.monto = HTMLHelper.getParameterDouble(request, "montoTotal");
        ciclo.montoConComision = HTMLHelper.getParameterDouble(request, "montoTotalConComision");
        ciclo.integrantes = integrantesSeleccionados;
        return ciclo;
    }

    public static CicloGrupalVO getIntegrantesInterCiclo(HttpServletRequest request, CicloGrupalVO ciclo) throws Exception { //AKI

        HttpSession session = request.getSession();
        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        int numIntegrantesChecked = GrupoHelper.getIntegrantesCicloChecked(request, numIntegrantes);
        IntegranteCicloVO[] integrantes = new IntegranteCicloVO[numIntegrantes];
        IntegranteCicloVO[] integrantesSeleccionados = new IntegranteCicloVO[numIntegrantesChecked];

        double monto = 0;

        for (int i = 0; i < integrantes.length; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante.idGrupo = idGrupo;
            integrante.idCiclo = idCiclo;
            integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
            integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
            integrante.nombre = request.getParameter("nombre" + i);
            integrante.monto = HTMLHelper.getParameterDouble(request, "monto" + i);
            integrante.calificacion = HTMLHelper.getParameterInt(request, "calificacion" + i);
            integrante.comision = HTMLHelper.getParameterInt(request, "comision" + i);
            integrante.medioCobro = HTMLHelper.getParameterInt(request, "medioCobro" + i);
            integrante.rol = HTMLHelper.getParameterInt(request, "rol" + i);
            integrantes[i] = integrante;
        }

        for (int i = 0, f = 0; i < integrantes.length; i++) {
            String nombre = "desembolso" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                integrantesSeleccionados[f] = integrantes[i];
                monto += integrantesSeleccionados[f].monto;    //Monto sin comision traido del request
                f++;
            }
        }

        if (ciclo.integrantes == null && session.getAttribute("INTEGRANTES_CICLO_SESION") != null) {
            ciclo.integrantes = (IntegranteCicloVO[]) session.getAttribute("INTEGRANTES_CICLO_SESION");
            session.removeAttribute("INTEGRANTES_CICLO_SESION");
        }

        for (int i = 0; i < integrantesSeleccionados.length; i++) {
            for (int j = 0; ciclo.integrantes != null && j < ciclo.integrantes.length; j++) {
                if (integrantesSeleccionados[i].idCliente == ciclo.integrantes[j].idCliente) {
                    integrantesSeleccionados[i].primaSeguro = ciclo.integrantes[j].primaSeguro;
                }
            }
        }

        ciclo.monto = HTMLHelper.getParameterDouble(request, "montoTotal");
        ciclo.montoConComision = HTMLHelper.getParameterDouble(request, "montoTotalConComision");
        ciclo.integrantes = integrantesSeleccionados;
        return ciclo;
    }

    public static ArrayList<IntegranteCicloVO> getIntegrantesCicloApertura(HttpServletRequest request) throws Exception { //AKI

        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        int estatus = HTMLHelper.getParameterInt(request, "estatus");
        ArrayList<IntegranteCicloVO> integrantes = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        SolicitudVO solicitudes[] = null;
        SolicitudDAO solicituddao = new SolicitudDAO();
        for (int i = 0; i < numIntegrantes; i++) {
            String nombre = "desembolso" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                IntegranteCicloVO integrante = new IntegranteCicloVO();
                integrante.idGrupo = idGrupo;
                integrante.idCiclo = idCiclo;
                integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
                integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
                integrante.nombre = request.getParameter("nombre" + i);
                if (HTMLHelper.getParameterInt(request, "estatus") == ClientesConstants.CICLO_AUTORIZADO || HTMLHelper.getParameterInt(request, "estatus") == ClientesConstants.CICLO_PENDIENTE || HTMLHelper.getParameterInt(request, "estatus") == ClientesConstants.CICLO_RECHAZADO) {
                    integrante.esNuevo = 0;
                } else {
                    integrante.esNuevo = HTMLHelper.getParameterInt(request, "esNuevo" + i);
                }
                integrante.monto = HTMLHelper.getParameterDouble(request, "monto" + i);
                integrante.costoSeguro = HTMLHelper.getParameterDouble(request, "costoSeguro" + i);
                integrante.montoConSeguro = HTMLHelper.getParameterDouble(request, "montoComision" + i);
                integrante.calificacion = HTMLHelper.getParameterInt(request, "calificacion" + i);
                if (integrante.calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR) {
                    integrante.aceptaRegular = HTMLHelper.getParameterInt(request, "aceptaRegular" + i);
                }
                integrante.comision = HTMLHelper.getParameterInt(request, "comision" + i);
                integrante.montoRefinanciado = HTMLHelper.getParameterDouble(request, "montoRefinanciado" + i);
                integrante.rol = HTMLHelper.getParameterInt(request, "rol" + i);
                if (estatus != ClientesConstants.CICLO_DISPERSADO && estatus != ClientesConstants.CICLO_CERRADO && estatus != ClientesConstants.CICLO_AUTORIZADO && estatus != ClientesConstants.CICLO_PARADESEMBOLSAR && estatus != ClientesConstants.CICLO_DESEMBOLSO) {
                    solicitudes = solicituddao.getSolicitudes(integrante.idCliente);
                    for (int y = 0; y < solicitudes.length; y++) {
                        if (solicitudes[y].documentacionCompleta == 1) {
                            integrante.RenovacionDoc = 0;
                            if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                integrante.RenovacionDoc = 1;
                            }
                        }
                    }
                    if (integrante.RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                        integrante.RenovacionDoc = 1;
                    }
                    if (integrante.RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                        integrante.DocCompletos = 1;
                    }
                }
                integrantesCiclo.add(integrante);

            }
        }
        return integrantesCiclo;
    }

    public static CicloGrupalVO getIntegrantesTotal(HttpServletRequest request, CicloGrupalVO ciclo) throws Exception { //AKI

        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();

        for (int i = 0; i < numIntegrantes; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante.idGrupo = idGrupo;
            integrante.idCiclo = idCiclo;
            integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
            integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
            integrante.nombre = request.getParameter("nombre" + i);
            integrante.monto = HTMLHelper.getParameterDouble(request, "monto" + i);
            integrante.calificacion = HTMLHelper.getParameterInt(request, "calificacion" + i);
            integrante.comision = HTMLHelper.getParameterInt(request, "comision" + i);
            integrante.montoRefinanciado = HTMLHelper.getParameterDouble(request, "montoRefinanciado" + i);
            integrante.rol = HTMLHelper.getParameterInt(request, "rol" + i);
            integrantesCiclo.add(integrante);

        }

        ciclo.monto = HTMLHelper.getParameterDouble(request, "montoTotal");
        ciclo.montoConComision = HTMLHelper.getParameterDouble(request, "montoTotalConComision");
        ciclo.integrantesArray = integrantesCiclo;
        return ciclo;
    }

    public static boolean existeIntegrante(CicloGrupalVO cicloAnterior, IntegranteCicloVO integrante) throws Exception {

        for (int i = 0; i < cicloAnterior.integrantes.length; i++) {
            if (cicloAnterior.integrantes[i].idCliente == integrante.idCliente) {
                return true;
            }
        }

        return false;
    }

    public static boolean existeNuevoIntegrante(ArrayList<IntegranteCicloVO> nuevoIntegrante, IntegranteCicloVO integranteActual) throws Exception {

        for (IntegranteCicloVO integrante : nuevoIntegrante) {
            if (integrante.idCliente == integranteActual.idCliente) {
                return true;
            }
        }

        return false;
    }

    public static int validaTasaComisionPlazo(IntegranteCicloVO[] integrantes) throws Exception {

        // 0 los valores son iguales
        // 1 los valores son diferentes, NO generar la tabla de amortizaci�n
        int comparar_valores = 0;
        if (integrantes != null && integrantes.length > 0) {
            for (int i = 0; i < integrantes.length; i++) {
                if (!(integrantes[0].plazo == integrantes[i].plazo) || !(integrantes[0].comision == integrantes[i].comision) || !(integrantes[0].idTasa == integrantes[i].idTasa)) {
                    comparar_valores = 1;
                    break;
                }
            }
        }
        return comparar_valores;

    }

    public static int getIntegrantesCicloChecked(HttpServletRequest request, int numIntegrantes) throws Exception {

        int integrantesChecked = 0;
        for (int i = 0; i < numIntegrantes; i++) {
            String nombre = "desembolso" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                integrantesChecked++;
            }
        }
        return integrantesChecked;
    }

    public static String getDomicilio(CicloGrupalVO ciclo) {
        String domicilio = "";
        if (ciclo != null && ciclo.direccionReunion != null) {
            domicilio = ciclo.direccionReunion.calle + " ";
            domicilio += "#" + ciclo.direccionReunion.numeroExterior + " ";
            domicilio += ciclo.direccionReunion.numeroInterior + " ";
            domicilio += ciclo.direccionReunion.colonia + ", ";
            domicilio += ciclo.direccionReunion.municipio + ", ";
            domicilio += ciclo.direccionReunion.estado;
        }
        return domicilio;
    }

    public static boolean validaAceptaRegulares(GrupoVO grupo, CicloGrupalVO ciclo) {
        boolean resultado = false;
        int integratnesTotales = 0;
        int integrantesREgulares = 0;
        SucursalVO sucursalVO = new SucursalVO();
        SucursalDAO sucursalDAO = new SucursalDAO();
        try {
            integratnesTotales = ciclo.integrantesArray.size();
            for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                if (integrante.aceptaRegular > 0 && integrante.aceptaRegular < 3) {
                    integrantesREgulares++;
                }
            }
            sucursalVO = sucursalDAO.getSucursal(grupo.sucursal);
            
            myLogger.info("integrantes regulares: "+integrantesREgulares);
            myLogger.info("Integrantes totales: "+integratnesTotales);
            
            double porcentajeRegulares = (((double)integrantesREgulares)/((double)integratnesTotales))*100;
            myLogger.info("Porcentaje integrantes regulares: "+porcentajeRegulares);
            
            switch (sucursalVO.calificacion) {
                case ClientesConstants.BUENA_EXCELENTE:
                    if (porcentajeRegulares <= 20) {
                        resultado = true;
                    }
                    break;
                case ClientesConstants.BUENA_BUENA:
                    if (ciclo.idCiclo == 1) {
                        if (porcentajeRegulares <= 12.5) {
                            resultado = true;
                        }
                    } else if (porcentajeRegulares <= 20) {
                        resultado = true;
                    }
                    break;
                case ClientesConstants.REGULAR_EXCELENTE:
                    if (ciclo.idCiclo == 1) {
                        if (integrantesREgulares == 0) {
                            resultado = true;
                        }
                    }else if (ciclo.idCiclo == 2) {
                        if (porcentajeRegulares <= 12.5) {
                            resultado = true;
                        }
                    }else if (ciclo.idCiclo >= 3) {
                        if (porcentajeRegulares <= 20) {
                            resultado = true;
                        }
                    }
                    break;
                case ClientesConstants.REGULAR_BUENA:
                    if (ciclo.idCiclo == 1) {
                        if (integrantesREgulares == 0) {
                            resultado = true;
                        }
                    } else if (ciclo.idCiclo <= 3) {
                        if (porcentajeRegulares <= 12.5) {
                            resultado = true;
                        }
                    } else if (ciclo.idCiclo >= 4) {
                        if (porcentajeRegulares <= 20) {
                            resultado = true;
                        }
                    }
                    break;
            }

        } catch (Exception e) {
        }
        return resultado;
    }

    /**
     * Obtiene el piloto por el cual los clientes pasaron,
     * Si tiene clientes del piloto 1 regresa 1, si tiene clientes de piloto 2 regresa 2
     * Si tiene ambos pilotos regresa 3, en caso de no tener ninguno regresa 0
     * 
     * @param grupo
     * @param ciclo
     * @return 
     */
    public static int obtenerPilotoGrupo(GrupoVO grupo, CicloGrupalVO ciclo) throws ClientesException {
        int resultado = 0;
        int clientesRegularesP1 = 0;
        int clientesOtraFin = 0;
        
        List<CreditoVO> creditosAutExcep = new CreditoDAO().getCreditosGrupoAutExcepcion(ciclo.idGrupo, ciclo.idCiclo);
        int clientesAutorizadosExcepcionP2 = creditosAutExcep.size();
        
            for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                if (integrante.aceptaRegular > 0 && integrante.aceptaRegular < 3) {
                    clientesRegularesP1++;
                }
                else if (integrante.aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA){
                    clientesOtraFin++;
                }
            }
            
            myLogger.info("Clientes Piloto 1: "+clientesRegularesP1);
            myLogger.info("Clientes Piloto 2: "+clientesAutorizadosExcepcionP2);
            
            if(clientesRegularesP1 > 0 && clientesAutorizadosExcepcionP2 > 0 && clientesOtraFin >0){
                resultado = 3;
            }else if(clientesRegularesP1 > 0){
                resultado = 1;
            }else if(clientesAutorizadosExcepcionP2 > 0){
                resultado = 2;
            }else if (clientesOtraFin >0){
                resultado = 4; 
            }
            
        return resultado;
    }
    
    public static ArrayList<IntegranteCicloVO> integraInterciclo(IntegranteCicloVO[] originales, IntegranteCicloVO[] interciclo) {
        ArrayList<IntegranteCicloVO> integrantesCicloVO = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO integranteCicloVO = null;
        CreditoVO informacionCrediticia = new CreditoVO();
        InformacionCrediticiaVO conusltaCrediticia = null;
        try {
            for (IntegranteCicloVO original : originales) {
                informacionCrediticia = new CreditoDAO().getCredito(original.idCliente, original.idSolicitud, 2);
                integranteCicloVO = original;
                if (informacionCrediticia == null) {
                    conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(original.idCliente);
                    integranteCicloVO.calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                    integranteCicloVO.aceptaRegular = 0;

                } else {
                    integranteCicloVO.calificacion = informacionCrediticia.calificacionMesaControl;
                    integranteCicloVO.aceptaRegular = informacionCrediticia.aceptaRegular;
                }
                integrantesCicloVO.add(integranteCicloVO);
            }
            for (IntegranteCicloVO clienteIC : interciclo) {
                informacionCrediticia = new CreditoDAO().getCredito(clienteIC.idCliente, clienteIC.idSolicitud, 2);
                integranteCicloVO = clienteIC;
                if (informacionCrediticia == null) {
                    conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(clienteIC.idCliente);
                    integranteCicloVO.calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                    integranteCicloVO.aceptaRegular = 0;
                } else {
                    integranteCicloVO.calificacion = informacionCrediticia.calificacionMesaControl;
                    integranteCicloVO.aceptaRegular = informacionCrediticia.aceptaRegular;
                }
                integrantesCicloVO.add(integranteCicloVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //agregar trow
        }
        return integrantesCicloVO;
    }

    public static TablaAmortizacionVO[] obtenTablaAmortizacion(GrupoVO grupo, int idCiclo, double monto, int idComision, Date fechaInicio, int plazo) throws ClientesException {
        TablaAmortizacionVO tabla[] = null;
        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
        TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
        double montoConComision = ClientesUtil.calculaMontoConComision(monto, idComision, catComisiones);

        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(new Integer(1));

//		CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        new TablaAmortizacionDAO().delTablaAmortizacion(grupo.idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
        TablaAmortizacionHelper.insertTablaGlobal(grupo.idGrupo, idCiclo, grupo.sucursal, monto, montoConComision, tasa.valor, 0, fechaInicio, plazo);

        tabla = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);

        return tabla;
    }

    public static void obtenTablaAmortizacion(GrupoVO grupo, CicloGrupalVO ciclo, Date fechaInicio) throws ClientesException {
        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(ciclo.tasa);
        //IntegranteCicloDAO integrantesdao = new IntegranteCicloDAO();
        //ciclo.montoConComision = integrantesdao.getMontoTotalCiclo(ciclo.idGrupo, ciclo.idCiclo);
        try {
            new TablaAmortizacionDAO().delTablaAmortizacion(grupo.idGrupo, ciclo.idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
            if (ciclo.montoConComision != 0) {
                /*if (ciclo.plazo == 0) {
                 ciclo.plazo = 16;
                 }*/
                Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, ciclo.montoConComision, ciclo.plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaInicio), grupo.sucursal, ClientesConstants.GRUPAL);
                Double tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(ciclo.montoConComision, pagoUnitario, ciclo.plazo, ClientesConstants.PAGO_SEMANAL, tasa.valor);
                Double tasaCalculada = TablaAmortizacionHelper.calcTasa(ClientesConstants.GRUPAL, ciclo.montoConComision, pagoUnitario, ciclo.plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaInicio), grupo.sucursal, tasaLogaritmo);
                TablaAmortizacionHelper.insertTablaInsolutoComunal(grupo, ciclo, pagoUnitario, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaInicio), tasaCalculada);
                ciclo.tasaCalculada = tasaCalculada;
            }
            //TablaAmortizacionHelper.insertTablaGlobal (grupo.idGrupo, idCiclo, grupo.sucursal, monto, montoConComision, tasa.valor, 0, fechaInicio, plazo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ciclo.tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, ciclo.idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);

    }

    public static boolean registerIBS(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletRequest request, Vector notificaciones) throws Exception {

        if (SolicitudHelper.doRegisterIBS(ClientesConstants.GRUPAL)) {
            ClienteHelperIBS clienteHelper = new ClienteHelperIBS();
            CreditoHelperIBS creditoHelper = new CreditoHelperIBS();

            //Si el cliente no tiene asignado un n�mero de cliente en IBS lo genera
            if (grupo.idGrupoIBS == 0) {
                clienteHelper.registraGrupoCuentaIBS(grupo, ciclo, request, true, notificaciones);
            } else if (grupo.idGrupoIBS != 0 && ciclo.idCuentaIBS == 0) {
                clienteHelper.registraGrupoCuentaIBS(grupo, ciclo, request, false, notificaciones);
            }

            if (grupo.idGrupoIBS != 0 && ciclo.idCuentaIBS != 0) {
                creditoHelper.registraCreditoGrupoIBS(grupo, ciclo, request, notificaciones);
                if (ciclo.idCreditoIBS == 0) {
                    return false;
                }
            } else {
                return false;
            }

            System.out.println("Número Grupo IBS: " + grupo.idGrupoIBS);
            System.out.println("Número Crédito IBS: " + ciclo.idCreditoIBS);
            System.out.println("Número Cuenta IBS: " + ciclo.idCuentaIBS);
        }

        return true;
    }

//	public static TablaAmortizacionVO[] obtenTablaAmortizacion(GrupoVO grupo, int idCiclo, double monto, int idComision, Date fechaInicio, int plazo) throws ClientesException{
//		TablaAmortizacionVO tabla[] = null;
//		TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
//		TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
//		double montoConComision = ClientesUtil.calculaMontoConComision(monto, idComision, catComisiones);
//
//		TasaInteresVO tasa = (TasaInteresVO)catTasas.get(new Integer(1));
//
//		new TablaAmortizacionDAO().delTablaAmortizacion(grupo.idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
//		TablaAmortizacionHelper.insertTablaGlobal (grupo.idGrupo, idCiclo, grupo.sucursal, monto, montoConComision, tasa.valor, 0, fechaInicio, plazo);
//
//		tabla = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
//
//		return tabla;
//	}
    public static boolean isDesembolsado(IntegranteCicloVO[] integrantes) throws Exception {
        boolean isDesembolsado = false;

        if (integrantes != null) {
            for (int i = 0; i < integrantes.length; i++) {
                IntegranteCicloVO integrante = new IntegranteCicloVO();
                integrante = integrantes[i];
                if (integrante.desembolsado == ClientesConstants.DESEMBOLSADO || integrante.estatus == ClientesConstants.INTEGRANTE_CANCELADO) {
                    isDesembolsado = true;
                }
            }
        }

        return isDesembolsado;
    }

    public static boolean isDispersado(IntegranteCicloVO[] integrantes) throws Exception {
        boolean isDispersado = true;

        for (int i = 0; integrantes != null && i < integrantes.length; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante = integrantes[i];
            //if (integrante.ordenPago.getEstatus() != ClientesConstants.OP_DESEMBOLSADO && integrante.ordenPago.getEstatus() != ClientesConstants.OP_DISPERSADA && integrante.ordenPago.getEstatus() != ClientesConstants.OP_COBRADA && integrante.estatus != ClientesConstants.INTEGRANTE_CANCELADO) {
            if (integrante.medioDisp == 0) {
                if (integrante.ordenPago.getEstatus() != ClientesConstants.OP_DESEMBOLSADO && integrante.ordenPago.getEstatus() != ClientesConstants.OP_DISPERSADA && integrante.ordenPago.getEstatus() != ClientesConstants.OP_COBRADA && ((integrante.ordenPago.getEstatus() != ClientesConstants.OP_CANCELACION_CONFIRMADA && (integrante.ordenPago.getIdBanco() == 2 || integrante.ordenPago.getIdBanco() == 3)) || ((integrante.ordenPago.getEstatus() != ClientesConstants.OP_CANCELADA && integrante.ordenPago.getEstatus() != ClientesConstants.OP_CANCELACION_CONFIRMADA) && (integrante.ordenPago.getIdBanco() != 2 && integrante.ordenPago.getIdBanco() != 3))) && integrante.estatus != ClientesConstants.INTEGRANTE_CANCELADO) {
                    isDispersado = false;
                }
            } else if (integrante.medioDisp == 1) {
                if (integrante.tarjetaCobro.getEstatus() != ClientesConstants.OP_DESEMBOLSADO && integrante.tarjetaCobro.getEstatus() != ClientesConstants.OP_DISPERSADA && integrante.tarjetaCobro.getEstatus() != ClientesConstants.OP_COBRADA && integrante.estatus != ClientesConstants.INTEGRANTE_CANCELADO) {
                    isDispersado = false;
                }
            }
        }
        return isDispersado;
    }

    public static boolean isPorConfirmar(IntegranteCicloVO[] integrantes) throws Exception {
        boolean isPorConfirmar = false;

        for (int i = 0; integrantes != null && i < integrantes.length; i++) {
            IntegranteCicloVO integrante = new IntegranteCicloVO();
            integrante = integrantes[i];
            if (integrante.medioDisp == 0) {
                if (integrante.ordenPago.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                    isPorConfirmar = true;
                }
            } else if (integrante.medioDisp == 1) {
                if (integrante.tarjetaCobro.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                    isPorConfirmar = true;
                }
            }
        }
        return isPorConfirmar;
    }

    public static int muestraOpcionesGrupal(CicloGrupalVO ciclo, String desembolsoOk) throws Exception {
        int value = 0;
        boolean isOrdenDePago = false;
        for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
            if (ciclo.integrantes[i].medioDisp == 0) {
                if (ciclo.integrantes[i].ordenPago != null && ciclo.integrantes[i].ordenPago.getReferencia() != null) {
                    isOrdenDePago = true;
                    break;
                }
            } else if (ciclo.integrantes[i].medioDisp == 1) {
                if (ciclo.integrantes[i].tarjetaCobro != null && ciclo.integrantes[i].tarjetaCobro.getTarjeta() != null) {
                    isOrdenDePago = true;
                    break;
                }
            }
        }
        if (isOrdenDePago) {
            if (ciclo.tablaAmortizacion != null && ciclo.tablaAmortizacion.length > 0 && (isDispersado(ciclo.integrantes) || isPorConfirmar(ciclo.integrantes))) {
                value = 2;
            } else {
                value = 0;
            }
        } else if (ciclo.tablaAmortizacion != null && ciclo.tablaAmortizacion.length > 0 && isDesembolsado(ciclo.integrantes)) {
            value = 1;
        } else {
            value = 0;
        }

        return value;
    }

    public static boolean existeOrdenDePagoActiva(OrdenDePagoVO[] ordenesDePago) {
        boolean existOrdenActiva = false;
        for (int i = 0; ordenesDePago != null && i < ordenesDePago.length; i++) {
            if (ordenesDePago[i].getEstatus() != ClientesConstants.OP_CANCELACION_CONFIRMADA) {
                existOrdenActiva = true;
                break;
            }
        }
        return existOrdenActiva;
    }

    public static String modificaReferencia(String referencia, OrdenDePagoVO[] ordenesDePagoActuales) {

        List<Integer> consecutivosDisponibles = new ArrayList<Integer>();
        int[] consecutivosUtilizados = new int[ordenesDePagoActuales.length];

        for (int i = 0; i < ordenesDePagoActuales.length; i++) {
            consecutivosUtilizados[i] = Integer.parseInt(ordenesDePagoActuales[i].getReferencia().substring(12, 13));
        }

        boolean bandera = true;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < consecutivosUtilizados.length; j++) {
                if (consecutivosUtilizados[j] == i) {
                    bandera = false;
                }
            }
            if (bandera) {
                consecutivosDisponibles.add(i);
            }
            bandera = true;

        }
        referencia = referencia.substring(0, 12);
        referencia += String.valueOf(consecutivosDisponibles.get(consecutivosDisponibles.size() - 1));

        return referencia;
    }

    public static PlaneacionRenovacionVO[] getGruposPlaneacion(HttpServletRequest request) throws Exception {

        int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
        int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
        int numGrupos = HTMLHelper.getParameterInt(request, "grupos");
        PlaneacionRenovacionVO[] grupos = new PlaneacionRenovacionVO[numGrupos];

        for (int i = 0; i < numGrupos; i++) {
            PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
            planeacion.numEjecutivo = idEjecutivo;
            planeacion.numSucursal = idSucursal;
            planeacion.numGrupo = HTMLHelper.getParameterInt(request, "idGrupo" + i);
            planeacion.numCiclo = HTMLHelper.getParameterInt(request, "idCiclo" + i);
            planeacion.integrantes = HTMLHelper.getParameterInt(request, "integrante" + i);
            planeacion.integrantesTotal = HTMLHelper.getParameterInt(request, "integranteTotal" + i);
            planeacion.fechaVencimiento = Convertidor.stringToDate(HTMLHelper.getParameterString(request, "vencimiento" + i));
            planeacion.renueva = HTMLHelper.getParameterInt(request, "renueva" + i);
            planeacion.numMotivo = HTMLHelper.getParameterInt(request, "motivo" + i);
            grupos[i] = planeacion;
        }

        return grupos;
    }

    public static String getGruposNuevos(HttpServletRequest request) throws Exception {

        int semana1 = HTMLHelper.getParameterInt(request, "semana1");
        int semana2 = HTMLHelper.getParameterInt(request, "semana2");
        int semana3 = HTMLHelper.getParameterInt(request, "semana3");
        int semana4 = HTMLHelper.getParameterInt(request, "semana4");
        int semana5 = HTMLHelper.getParameterInt(request, "semana5");
        String gruposSemana = HTMLHelper.displayField(semana1, true) + HTMLHelper.displayField(semana2, true) + HTMLHelper.displayField(semana3, true) + HTMLHelper.displayField(semana4, true) + HTMLHelper.displayField(semana5, true);

        return gruposSemana;
    }

    public static DescongelaPagoGarantiaIndVO getPagoIndividual(CreditoCartVO credito, PagoIndividualGruposVO pagos, String user, String nombre_grupo, String nombre_completo, String fecha) throws Exception {
        DescongelaPagoGarantiaIndVO registro = new DescongelaPagoGarantiaIndVO();
        registro.setNumGrupo(credito.getNumCliente());
        registro.setNumCiclo(credito.getNumSolicitud());
        registro.setNumCliente(pagos.numCliente);
        registro.setNumCredito(credito.getNumCredito());
        registro.setNumPago(0);
        registro.setMontoCredito(credito.getMontoCredito());
        registro.setMontoDesembolsado(credito.getMontoDesembolsado());
        registro.setMontoPago(pagos.monto);
        registro.setNombreCompleto(nombre_completo);
        registro.setNombreGrupo(nombre_grupo);
        registro.setUsuario(user);
        registro.setFechaDevolucion(Convertidor.stringToSqlDate(fecha));

        return registro;

    }

    public ArrayList<IntegranteCicloVO> getIntegrantesConsultaMasiva(HttpServletRequest request) throws Exception { //AKI

        int numIntegrantes = HTMLHelper.getParameterInt(request, "numIntegrantes");
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        for (int i = 0; i < numIntegrantes; i++) {
            String nombre = "consCC" + i;
            if (HTMLHelper.getCheckBox(request, nombre)) {
                IntegranteCicloVO integrante = new IntegranteCicloVO();
                integrante.idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
                integrante.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud" + i);
                integrantesCiclo.add(integrante);
            }
        }
        return integrantesCiclo;
    }

    public static void obtenTablaAmortizacionInterciclo(GrupoVO grupoVO, CicloGrupalVO cicloVO, double montoIncremento, int semDisp, int estatus) throws ClientesException { //revisar a fondo

        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupoVO.idOperacion);
        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(cicloVO.getTasa());
        TablaAmortVO tablaVO = new TablaAmortVO();
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        double saldoCapital = 0;
        int plazo = cicloVO.plazo - semDisp;
        try {
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && cicloVO.estatusIC == ClientesConstants.CICLO_DISPERSADO && estatus == ClientesConstants.CICLO_DESEMBOLSO) {
                cicloVO.tablaAmortInterciclo = tablaDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
                if (cicloVO.tablaAmortInterciclo != null) {
                    for (int i = 0; i < cicloVO.tablaAmortInterciclo.length; i++) {
                        cicloVO.tablaAmortInterciclo[i].tipoAmortizacion = ClientesConstants.AMORTIZACION_INTERCICLO_2;
                        tablaDAO.addTablaAmortizacion(cicloVO.tablaAmortInterciclo[i]);
                    }
                }
            }
            tablaDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);
            tablaVO = tablaCartDAO.getDivPago(grupoVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), semDisp);
            saldoCapital = tablaVO.getSaldoCapital() + montoIncremento;
            Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, saldoCapital, plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(FechasUtil.getDate(tablaVO.getFechaPago(), 7, 1)), grupoVO.getSucursal(), ClientesConstants.GRUPAL);
            TablaAmortizacionHelper.insertTablaInsolutoComunalInterciclo(grupoVO, cicloVO, montoIncremento, pagoUnitario, saldoCapital, semDisp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cicloVO.tablaAmortInterciclo = tablaDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_INTERCICLO);

    }
    
    public static void obtenTablaAmortizacionAdicional(GrupoVO grupoVO, CicloGrupalVO cicloVO, double montoIncremento, int semDisp, int estatus) throws ClientesException { //revisar a fondo

        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupoVO.idOperacion);
        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(cicloVO.getTasa());
        TablaAmortVO tablaVO = new TablaAmortVO();
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        double saldoCapital = 0;
        int plazo = cicloVO.plazo - semDisp, numAmort = 0, i= 0, j= 0, amort = 0;
        try {
            numAmort = tablaDAO.getMaxElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo());
            amort = (numAmort + 1);
            for( i= 0; i < (numAmort); i++){
                cicloVO.tablaAmortInterciclo = tablaDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), (numAmort - i));
                for ( j = 0; j < cicloVO.tablaAmortInterciclo.length; j++) {
                    cicloVO.tablaAmortInterciclo[j].tipoAmortizacion = (amort);
                    tablaDAO.addTablaAmortizacion(cicloVO.tablaAmortInterciclo[j]);
                }
                amort--;
                tablaDAO.delTablaAmortizacion(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), (numAmort - i));
            }
            tablaVO = tablaCartDAO.getDivPago(grupoVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), semDisp);
            saldoCapital = tablaVO.getSaldoCapital() + montoIncremento;
            Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario(tasa.valor, saldoCapital, plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(FechasUtil.getDate(tablaVO.getFechaPago(), 7, 1)), grupoVO.getSucursal(), ClientesConstants.GRUPAL);
            TablaAmortizacionHelper.insertTablaInsolutoComunalAdicional(grupoVO, cicloVO, montoIncremento, pagoUnitario, saldoCapital, semDisp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cicloVO.tablaAmortInterciclo = tablaDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), ClientesConstants.AMORTIZACION_GRUPAL);

    }

    public boolean cierraCiclo(int numGrupo, int numCiclo) {
        boolean respuesta = false;
        int idEquipo = numGrupo;
        int idCiclo = numCiclo;
        SaldoIBSVO saldo = new SaldoIBSVO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        BitacoraCicloVO registroBitacora = new BitacoraCicloVO();
        SaldoIBSDAO saldoDao = new SaldoIBSDAO();
        TablaAmortDAO tablaAmortizacionDao = new TablaAmortDAO();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        PagoDAO pagoDao = new PagoDAO();
        double montoPagar = 0;
        double montoPagado = 0;
        try {
            myLogger.debug("Obteniendo saldo");
            saldo = saldoDao.getSaldos(idEquipo, idCiclo);
            BitacoraUtil bitacora = new BitacoraUtil(idEquipo, "sistema", "GrupoHelper.cierraCiclo");
            if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO) {
                if (tablaAmortizacionDao.getTotalPorpagar(saldo.getIdClienteSICAP(), saldo.getCredito()) > 0) {
                    myLogger.debug("Monto por pagar mayor a cero");
                    registroBitacora.setComentario("No es posible cerrar el ciclo, aún tiene monto a pagar");
                    registroBitacora.setEstatus(ClientesConstants.CICLO_DISPERSADO);
                    registroBitacora.setIdCiclo(saldo.getIdSolicitudSICAP());
                    registroBitacora.setIdComentario(bitacoraCicloDao.getNumComentario(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP()) + 1);
                    registroBitacora.setIdEquipo(saldo.getIdClienteSICAP());
                    registroBitacora.setUsuarioAsignado("sistema");
                    registroBitacora.setUsuarioComentario("sistema");
                    bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
                } else {
                    myLogger.debug("Obteniendo Total por Pagar");
                    montoPagar = tablaAmortizacionDao.getTotalPagado(saldo.getIdClienteSICAP(), saldo.getCredito());
                    myLogger.debug("Obteniendo Total Pagado");
                    montoPagado = pagoDao.getTotalPagado(saldo.getReferencia());
                    if (montoPagado >= montoPagar) {
                        myLogger.debug("Monto pagado es mayor o igual a Monto pagar, Pagado: " + montoPagado + " Pagar: " + montoPagar);
                        myLogger.info("Actualizando estatus del ciclo");
                        cicloDao.updateEstatusCiclo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP(), ClientesConstants.CICLO_CERRADO);
                        myLogger.debug("Actualizando estatus del ciclo en varibale del grupo en session");
                        registroBitacora.setComentario("Ciclo Cerrado");
                        registroBitacora.setEstatus(ClientesConstants.CICLO_CERRADO);
                        registroBitacora.setIdCiclo(saldo.getIdSolicitudSICAP());
                        registroBitacora.setIdComentario(bitacoraCicloDao.getNumComentario(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP()) + 1);
                        registroBitacora.setIdEquipo(saldo.getIdClienteSICAP());
                        registroBitacora.setUsuarioAsignado("sistema");
                        registroBitacora.setUsuarioComentario("sistema");
                        myLogger.info("Insertando registro en bitácora ciclo.");
                        bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
                        myLogger.info("Insertando registro en bitácora");
                        bitacora.registraEvento(registroBitacora);
                        respuesta = true;
                    } else {
                        registroBitacora.setComentario("No es posible cerrar el ciclo, tiene una diferencia de " + FormatUtil.roundDecimal(montoPagar - montoPagado, 2));
                        registroBitacora.setEstatus(ClientesConstants.CICLO_DISPERSADO);
                        registroBitacora.setIdCiclo(saldo.getIdSolicitudSICAP());
                        registroBitacora.setIdComentario(bitacoraCicloDao.getNumComentario(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP()) + 1);
                        registroBitacora.setIdEquipo(saldo.getIdClienteSICAP());
                        registroBitacora.setUsuarioAsignado("sistema");
                        registroBitacora.setUsuarioComentario("sistema");
                        bitacoraCicloDao.insertaBitacoraCiclo(null, registroBitacora);
                        myLogger.debug("El monto a pagar es mayor al monto pagado");
                    }
                }
            } else {
                myLogger.debug("EL equipo " + idEquipo + " no esta Liquidado");
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("CommandCierraCiclo", dbe);
        } catch (Exception e) {
            myLogger.error("CommandCierraCiclo", e);
        }
        return respuesta;
    }
}

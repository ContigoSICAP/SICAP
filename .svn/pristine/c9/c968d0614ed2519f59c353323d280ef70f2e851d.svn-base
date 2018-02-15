package com.sicap.clientes.util;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IncidenciaPagoGrupalDAO;
import com.sicap.clientes.dao.IncidenciasMonitorPagosDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IntegrantesHelper;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.EventosDePagoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IncidenciaPagoGrupalVO;
import com.sicap.clientes.vo.IncidenciasMonitorPagosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.ReporteCobranzaGrupalVO;
import com.sicap.clientes.vo.ReporteVisitaGrupalVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class GrupoUtil {

    private static Logger myLogger = Logger.getLogger(GrupoUtil.class);

    public static int getIndiceCiclo(CicloGrupalVO[] ciclos, int idCiclo) {

        if (ciclos != null && ciclos.length > 0 && idCiclo > 0) {
            for (int i = 0; i < ciclos.length; i++) {
                if (ciclos[i].idCiclo == idCiclo) {
                    idCiclo = i;
                }
            }
        }
        return idCiclo;
    }

    public static CicloGrupalVO getCiclo(CicloGrupalVO[] ciclos, int idCiclo) {

        CicloGrupalVO ciclo = new CicloGrupalVO();
        if (ciclos != null && ciclos.length > 0 && idCiclo > 0) {
            for (int i = 0; i < ciclos.length; i++) {
                if (ciclos[i].idCiclo == idCiclo) {
                    ciclo = ciclos[i];
                }
            }
        }

        return ciclo;
    }

    public static ReporteCobranzaGrupalVO getReporteIntegrante(EventosDePagoVO evento, int idCliente) throws ClientesException {

        ReporteCobranzaGrupalVO eventoCobranza = new ReporteCobranzaGrupalVO();
        if (evento != null && idCliente != 0) {
            for (int i = 0; evento.reporteCobranza != null && i < evento.reporteCobranza.length; i++) {
                if (evento.reporteCobranza[i].numCliente == idCliente) {
                    eventoCobranza = evento.reporteCobranza[i];
                }
            }
        }

        return eventoCobranza;
    }

    public static CicloGrupalVO[] addCiclo(GrupoVO grupo, CicloGrupalVO ciclo) throws ClientesException {
        CicloGrupalVO elementos[] = null;
        elementos = new CicloGrupalVO[grupo.ciclos.length + 1];
        for (int i = 0; i < elementos.length; i++) {
            if (i < grupo.ciclos.length) {
                elementos[i] = grupo.ciclos[i];
            } else {
                elementos[i] = ciclo;
            }
        }
        grupo.ciclos = new CicloGrupalDAO().getCiclos(grupo.idGrupo);
        return elementos;
    }

    public static CicloGrupalVO[] addCicloRestructura(GrupoVO grupo, CicloGrupalVO ciclo) throws ClientesException {
        CicloGrupalVO elementos[] = null;
        elementos = new CicloGrupalVO[1];
        elementos[0] = ciclo;

        grupo.ciclos = new CicloGrupalDAO().getCiclos(grupo.idGrupo);
        return elementos;
    }

    public static String getMnemonicoGrupo(GrupoVO grupo) {
        String mnemonico = "G";
        if (grupo != null) {
            String numGrupo = String.valueOf(grupo.idGrupo);
            String numSucursal = String.valueOf(grupo.sucursal);
            String nombre = grupo.nombre.trim().replace(' ', '0');
            mnemonico += FormatUtil.completaCadena(numGrupo, '0', 4, "L");
            mnemonico += FormatUtil.completaCadena(numSucursal, '0', 3);
            mnemonico += getSiglasGrupo(nombre);
        }
        return Convertidor.toSyncronetCharacterSet(mnemonico);
    }

    private static String getSiglasGrupo(String nombre) {
        String siglas = "";
        if (nombre != null) {
            if (nombre.length() < 2) {
                siglas = FormatUtil.completaCadena(nombre, '0', 2);
            } else {
                siglas = nombre.substring(0, 2);
            }
        }
        return siglas;
    }

    public static CicloGrupalVO setCiclo(GrupoVO grupo, CicloGrupalVO ciclo, int idCiclo) {

        if (grupo != null && grupo.ciclos != null && grupo.ciclos.length > 0 && idCiclo > 0) {
            for (int i = 0; i < grupo.ciclos.length; i++) {
                if (grupo.ciclos[i].idCiclo == idCiclo) {
                    grupo.ciclos[i] = ciclo;
                }
            }
        }

        return ciclo;
    }

    public static void procesaReporteCobranza(CicloGrupalVO ciclo, ReporteCobranzaGrupalVO reporteCobranza, int idAlerta) throws ClientesException {

        int nuevoTamano = (ciclo.eventosDePago[idAlerta].reporteCobranza != null ? ciclo.eventosDePago[idAlerta].reporteCobranza.length + 1 : 1);
        ReporteCobranzaGrupalVO[] nuevoArray = new ReporteCobranzaGrupalVO[nuevoTamano];
        for (int i = 0; i < nuevoTamano - 1; i++) {
            nuevoArray[i] = ciclo.eventosDePago[idAlerta].reporteCobranza[i];
        }
        nuevoArray[nuevoArray.length - 1] = reporteCobranza;

        int resultado = nuevoArray.length % 2;
        if (resultado == 0) {
            ciclo.eventosDePago[idAlerta].estatusReporteCobranza = 2;
            new EventosPagosGrupalDAO().updateMonitor(ciclo.eventosDePago[idAlerta]);
        }

        ciclo.eventosDePago[idAlerta].reporteCobranza = nuevoArray;

    }

    public static void procesaInformeVisita(CicloGrupalVO ciclo, ReporteVisitaGrupalVO reporteVisita, int idAlerta) throws ClientesException {

        EventosDePagoVO alertaGrupal = new EventosDePagoVO();
        if (reporteVisita != null && idAlerta > -1 && ciclo.eventosDePago != null) {
            alertaGrupal = ciclo.eventosDePago[idAlerta];
            if (alertaGrupal.estatusVisitaGerente == 1) {
                alertaGrupal.estatusVisitaGerente = 2;
            }
            if (alertaGrupal.estatusVisitaSupervisor == 1) {
                alertaGrupal.estatusVisitaSupervisor = 2;
            }
            if (alertaGrupal.estatusVisitaGestor == 1) {
                alertaGrupal.estatusVisitaGestor = 2;
            }

            new EventosPagosGrupalDAO().updateMonitor(alertaGrupal);
            ciclo.eventosDePago[idAlerta].reporteVisita = reporteVisita;
            ciclo.eventosDePago[idAlerta] = alertaGrupal;
        }

    }

    public static boolean tieneCicloActivo(GrupoVO grupo) {
        boolean resultado = false;
        if (grupo != null && grupo.ciclos != null && grupo.ciclos.length > 0) {
            for (int i = 0; i < grupo.ciclos.length; i++) {
                if (grupo.ciclos[i].estatus != 2) {
                    resultado = true;
                }
            }
        }
        return resultado;
    }

    public static boolean esPagoGrupal(String numReferencia) {
        boolean esSICAP = false;

        try {
            String referencia = numReferencia;
            if (referencia.indexOf("LD") == -1 && referencia.length() == 13) {
                String anno = referencia.substring(9, 11);
                if (anno.equalsIgnoreCase("07")) {
                    return false;
                } else if (anno.equalsIgnoreCase("08")) {
                    int dia = Integer.parseInt(referencia.substring(0, 2));
                    int mes = Integer.parseInt(referencia.substring(4, 6));
                    int prod = Integer.parseInt(referencia.substring(11, 12));
                    if (dia >= 3 && mes >= 4 && prod == 2) {
                        esSICAP = true;
                        /*
                         ReferenciaGeneralVO refGralVO = null;
                         ReferenciaGeneralDAO refGralDAO = new ReferenciaGeneralDAO();
                         refGralVO = refGralDAO.getReferenciaGeneral(referencia);
                         */
                    }
                } else {
                    //Por ver si es grupal sicap
                    int prod = Integer.parseInt(referencia.substring(3, 4));
                    if (prod == 9) {
                        esSICAP = true;
                        //int suc = Integer.parseInt(referencia.substring(0, 3));
                    }
                }
            }
        } catch (NumberFormatException exc) {
            myLogger.error("NumberFormatException", exc);
        } catch (Exception exc) {
            myLogger.error("Exception", exc);
        }

        return esSICAP;

    }

    public static PagoGrupalVO esPagoGrupalVO(String numReferencia) {
        //boolean esSICAP = false;
        PagoGrupalVO pagoGVO = new PagoGrupalVO();

        try {
            String referencia = numReferencia;
            if (referencia.indexOf("LD") == -1 && referencia.length() == 13) {
                String anno = referencia.substring(9, 11);
                if (anno.equalsIgnoreCase("07")) {
                    return null;
                } else if (anno.equalsIgnoreCase("08")) {
                    int dia = Integer.parseInt(referencia.substring(0, 2));
                    int mes = Integer.parseInt(referencia.substring(4, 6));
                    int prod = Integer.parseInt(referencia.substring(11, 12));
                    if (dia >= 3 && mes >= 4 && prod == 2) {
//						esSICAP = true;
                        ReferenciaGeneralVO refGralVO = null;
                        ReferenciaGeneralDAO refGralDAO = new ReferenciaGeneralDAO();
                        refGralVO = refGralDAO.getReferenciaGeneral(referencia);

                        pagoGVO.numGrupo = refGralVO.numcliente;
                        pagoGVO.numCiclo = 1;
                        pagoGVO.numPago = (new PagoGrupalDAO()).getNumPagoGrupalActual(pagoGVO.numGrupo, pagoGVO.numCiclo);
                    }
                } else {
                    //Por ver si es grupal sicap
                    int prod = Integer.parseInt(referencia.substring(3, 4));
                    if (prod == 9) {
//						esSICAP = true;
//						int suc = Integer.parseInt(referencia.substring(0, 3));
                        pagoGVO.numCiclo = Integer.parseInt(referencia.substring(10, 12));
// Las referencias con excepcion se consideran como ciclo 1						
                        if (pagoGVO.numCiclo == 99) {
                            pagoGVO.numCiclo = 1;
                        }
                        pagoGVO.numGrupo = Integer.parseInt(referencia.substring(4, 10));
                        pagoGVO.numPago = (new PagoGrupalDAO()).getNumPagoGrupalActual(pagoGVO.numGrupo, pagoGVO.numCiclo);
                    }
                }
            }
        } catch (NumberFormatException exc) {
            myLogger.error("PagoGrupalVO", exc);
        } catch (Exception exc) {
            myLogger.error("PagoGrupalVO", exc);
        }

        return pagoGVO;

    }

    public static int obtieneComisionIntegrante(IntegranteCicloVO integrante, GrupoVO grupoDestino) throws ClientesException {

        int comision = 0;

        GrupoVO grupoAnterior = new GrupoDAO().getGrupoUltimoCicloCliente(integrante.idCliente);
        if (grupoAnterior == null) {
            comision = GrupoUtil.asignaComisionGrupal(grupoDestino, true, integrante.calificacion);
        } else if (grupoAnterior.idGrupo == grupoDestino.idGrupo) {
            comision = GrupoUtil.asignaComisionGrupal(grupoDestino, false);
        } else {
            if (grupoAnterior.calificacion.equals("B")) {
                grupoAnterior.calificacion = "A1";
            }
            comision = GrupoUtil.asignaComisionGrupal(grupoAnterior, false);
        }

        return comision;
    }

    public static int asignaTasaGrupal(GrupoVO grupo, boolean esNuevo) throws ClientesException {
        int resultado = 0;
        String clienteNuevo = (esNuevo ? "SI" : "NO");
        if (!grupo.calificacion.equals("RG")) {
            resultado = new CatalogoDAO().asignaTasaGrupal(clienteNuevo, grupo.tipoSucursal, grupo.calificacion);
        } else if (grupo.calificacion.equals("RG")) {
            resultado = 2;
        }

        return resultado;
    }

    public static int decideCatalogo(GrupoVO grupo) throws ClientesException {
        int resp = 0;
        int i = 0;
        boolean reviza = false;
        Date FechaLib;
        Date FechaAutoriza;
        String fechaLib = "07/03/2017";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        BitacoraCicloVO bitacoraCicloVO = null;
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        try {
            if (grupo.ciclos != null) {
                if (grupo.ciclos.length < 4) {
                    if (grupo.otraFinaciera == 0) {
                        for (i = 0; i < grupo.ciclos.length; i++) {
                            if (grupo.ciclos[i].estatus > ClientesConstants.CICLO_CERRADO) {
                                reviza = true;
                                break;
                            }
                        }
                        resp = ClientesConstants.CATALOGO_TASA_NUEVA;
                        if (reviza) {
                            bitacoraCicloVO = bitacoraCicloDAO.getPrimerRegistroEstatus(grupo.ciclos[i].idGrupo, grupo.ciclos[i].idCiclo, ClientesConstants.CICLO_APERTURA);
                            FechaAutoriza = new Date(bitacoraCicloVO.getFechaHora().getTime());
                            FechaLib = sdf.parse(fechaLib);
                            if (FechaAutoriza.before(FechaLib)) {
                                resp = ClientesConstants.CATALOGO_TASA;
                            }
                        }
                    }
                }
            } else {
                resp = ClientesConstants.CATALOGO_TASA_NUEVA;
            }
        } catch (Exception exc) {
            myLogger.error("decideCatalogo", exc);
        }

        return resp;
    }

    public static int asignaComisionGrupal(GrupoVO grupo, boolean esNuevo) throws ClientesException {

        return asignaComisionGrupal(grupo, esNuevo, 1);

    }

    public static int asignaComisionGrupal(GrupoVO grupo, boolean esNuevo, int calificacionCirculo) throws ClientesException {
        String clienteNuevo = (esNuevo ? "SI" : "NO");
        return new CatalogoDAO().asignaComisionGrupal(clienteNuevo, calificacionCirculo, grupo.tipoSucursal, grupo.calificacion);
    }

    public static CicloGrupalVO recalculaTasaComision(Connection conn, CicloGrupalVO ciclo, GrupoVO grupo) throws Exception {
        String nuevaCalificacion = grupo.calificacion;
        switch (ciclo.idCiclo) {
            case 2:
                if (ciclo.integrantes.length < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_2) {
                    nuevaCalificacion = bajaCalificacion(conn, grupo.calificacion, ciclo.idGrupo);
                }
                break;
            case 3:
                if (ciclo.integrantes.length < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3) {
                    nuevaCalificacion = bajaCalificacion(conn, grupo.calificacion, ciclo.idGrupo);
                }
                break;
            default:
                if (ciclo.integrantes.length < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3 && ciclo.idCiclo > 3) {
                    nuevaCalificacion = bajaCalificacion(conn, grupo.calificacion, ciclo.idGrupo);
                }
        }
        if (!nuevaCalificacion.equals(grupo.calificacion)) { //Solo si canbia la calificacion recalcula todo
            grupo.calificacion = nuevaCalificacion;
            ciclo.tasa = asignaTasaGrupal(grupo, false);
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                ciclo.integrantes[i].idTasa = asignaTasaGrupal(grupo, false);
                //Obtiene nueva comision de acuerdo a su calificacion
                ciclo.integrantes[i].comision = obtieneComisionIntegrante(ciclo.integrantes[i], grupo);
            }
        }
        return ciclo;
    }

    public static CicloGrupalVO recalculaTasaComisionApertura(CicloGrupalVO ciclo, GrupoVO grupo) throws Exception {
        String nuevaCalificacion = grupo.calificacion;
        switch (ciclo.idCiclo) {
            case 2:
                if (ciclo.integrantesArray.size() < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_2) {
                    nuevaCalificacion = bajaCalificacionApertura(grupo.calificacion, ciclo.idGrupo);
                }
                break;
            case 3:
                if (ciclo.integrantesArray.size() < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3) {
                    nuevaCalificacion = bajaCalificacionApertura(grupo.calificacion, ciclo.idGrupo);
                }
                break;
            default:
                if (ciclo.integrantesArray.size() < ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3 && ciclo.idCiclo > 3) {
                    nuevaCalificacion = bajaCalificacionApertura(grupo.calificacion, ciclo.idGrupo);
                }
        }
        if (!nuevaCalificacion.equals(grupo.calificacion)) { //Solo si canbia la calificacion recalcula todo
            grupo.calificacion = nuevaCalificacion;
            ciclo.tasa = asignaTasaGrupal(grupo, false);
            for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                integrante.idTasa = asignaTasaGrupal(grupo, false);
                //Obtiene nueva comision de acuerdo a su calificacion
                integrante.comision = obtieneComisionIntegrante(integrante, grupo);
            }
        }
        return ciclo;
    }

    public static String bajaCalificacion(Connection conn, String calificacion, int grupo) throws Exception {
        String nuevaCalificacion = "";
        if (calificacion.equals("AAA")) {
            nuevaCalificacion = "AA";
        } else if (calificacion.equals("AA")) {
            nuevaCalificacion = "A";
        } else if (calificacion.equals("A")) {
            nuevaCalificacion = "B";
        } else {
            nuevaCalificacion = "B";
        }

        new GrupoDAO().updateCalificacionGrupo(nuevaCalificacion, grupo);
        return nuevaCalificacion;
    }

    public static String bajaCalificacionApertura(String calificacion, int grupo) throws Exception {
        String nuevaCalificacion = "";
        if (calificacion.equals("AAA")) {
            nuevaCalificacion = "AA";
        } else if (calificacion.equals("AA")) {
            nuevaCalificacion = "A";
        } else if (calificacion.equals("A")) {
            nuevaCalificacion = "B";
        } else {
            nuevaCalificacion = "B";
        }

        new GrupoDAO().updateCalificacionGrupo(nuevaCalificacion, grupo);
        return nuevaCalificacion;
    }

    /*public static String AsiganaNuevaCalificacion(int idTasa ){
     String nuevaCalificacion="";
     switch ( idTasa ){
     case 1:
     nuevaCalificacion = "AAA";
     break;
     case 2:
     nuevaCalificacion = "AA";
     break;
     case 3:
     nuevaCalificacion = "A";
     break;
     case 4:
     nuevaCalificacion = "B";
     break;
     default :
     nuevaCalificacion = "B";
     break;
     }
     return nuevaCalificacion;
     }*/
    public static int asignaNumeroAmortizacion(Date fechaPagoOriginal, TablaAmortizacionVO[] tablaAmortizacion) throws ClientesException {

        int numeroAmortizacion = 0;
        PagoIndividualGruposDAO pagoIndv = new PagoIndividualGruposDAO();

        GregorianCalendar fechaPago = new GregorianCalendar();
        fechaPago.setTime(fechaPagoOriginal);
        fechaPago.set(Calendar.HOUR_OF_DAY, 0);
        fechaPago.set(Calendar.MINUTE, 0);
        fechaPago.set(Calendar.SECOND, 0);
        fechaPago.set(Calendar.MILLISECOND, 0);
        GregorianCalendar fechaDesembolso = new GregorianCalendar();
        fechaDesembolso.setTime(tablaAmortizacion[0].fechaPago);
        GregorianCalendar fechaPrimerPago = new GregorianCalendar();
        fechaPrimerPago.setTime(tablaAmortizacion[1].fechaPago);
        double montoPagado = new PagoGrupalDAO().getPagosGarantia(tablaAmortizacion[0].idCliente, tablaAmortizacion[0].idSolicitud);
        myLogger.debug("montoPagado " + montoPagado + " (tablaAmortizacion[0].saldoInicial*0.10) " + (tablaAmortizacion[0].saldoInicial * 0.10));
        if (fechaPago.getTime().before(fechaDesembolso.getTime()) || fechaPago.getTime().equals(fechaDesembolso.getTime())) {
            numeroAmortizacion = 0;
        } else if (fechaPago.getTime().after(fechaDesembolso.getTime()) && fechaPago.getTime().before(fechaPrimerPago.getTime()) && montoPagado < (tablaAmortizacion[0].saldoInicial * 0.10)) {
            numeroAmortizacion = 0;
        } else {
            //Verifico por la fecha del pago en que numero de pago entra
            for (int a = 1; a < tablaAmortizacion.length; a++) {
                GregorianCalendar limiteInferior = new GregorianCalendar();
                GregorianCalendar limiteSuperior = new GregorianCalendar();

                limiteSuperior.setTime(tablaAmortizacion[a].fechaPago);
                limiteSuperior.set(Calendar.HOUR_OF_DAY, 0);
                limiteSuperior.set(Calendar.MINUTE, 0);
                limiteSuperior.set(Calendar.SECOND, 0);
                limiteSuperior.set(Calendar.MILLISECOND, 0);
                limiteSuperior.add(Calendar.DAY_OF_YEAR, 4);

                limiteInferior.setTime(tablaAmortizacion[a].fechaPago);
                limiteInferior.set(Calendar.DAY_OF_YEAR, limiteInferior.get(Calendar.DAY_OF_YEAR) - 2);
                limiteInferior.set(Calendar.HOUR_OF_DAY, 0);
                limiteInferior.set(Calendar.MINUTE, 0);
                limiteInferior.set(Calendar.SECOND, 0);
                limiteInferior.set(Calendar.MILLISECOND, 0);

                if (((fechaPago.getTime().before(limiteSuperior.getTime()) || fechaPago.getTime().equals(limiteSuperior.getTime()))
                        && (fechaPago.getTime().after(limiteInferior.getTime()) || fechaPago.getTime().equals(limiteInferior.getTime())))
                        || tablaAmortizacion[a].numPago == 16) {

                    if (pagoIndv.existePagoIndividual(tablaAmortizacion[a].idCliente, tablaAmortizacion[a].idSolicitud, tablaAmortizacion[a].numPago)) {
                        if (tablaAmortizacion[a].numPago < 16) {
                            numeroAmortizacion = new PagoIndividualGruposDAO().pagoMaximoRegistrado(tablaAmortizacion[a].idCliente, tablaAmortizacion[a].idSolicitud) + 1;
                            break;
                        }
                    } else if (tablaAmortizacion[a].numPago < 16) {
                        numeroAmortizacion = tablaAmortizacion[a].numPago;
                        break;
                    } else if (tablaAmortizacion[a].numPago == 16) {
                        numeroAmortizacion = 16;
                    }

                }
            }

        }
        return numeroAmortizacion;
    }

    public static void procesaPago(PagoVO pago, PagoGrupalVO pagoGVO, TablaAmortizacionVO[] tablaAmortizacion) throws ClientesException {

        pagoGVO.numPago = new PagoGrupalDAO().getNumPagoGrupalActual(tablaAmortizacion[0].idCliente, tablaAmortizacion[0].idSolicitud) + 1;
        pagoGVO.numTransaccion = 1;
        pagoGVO.fechaPago = pago.fechaPago;
        pagoGVO.monto = pago.monto;
        pagoGVO.numAmortizacion = asignaNumeroAmortizacion(pago.fechaPago, tablaAmortizacion);

        new PagoGrupalDAO().addPagoGrupal(pagoGVO);
    }

    public void procesaPago(PagoVO pago, PagoGrupalVO pagoGVO, TablaAmortizacionVO[] tablaAmortizacion, Connection con) throws ClientesException {

        pagoGVO.numPago = new PagoGrupalDAO().getNumPagoGrupalActual(tablaAmortizacion[0].idCliente, tablaAmortizacion[0].idSolicitud) + 1;
        pagoGVO.numTransaccion = 1;
        pagoGVO.fechaPago = pago.fechaPago;
        pagoGVO.monto = pago.monto;
        pagoGVO.numAmortizacion = asignaNumeroAmortizacion(pago.fechaPago, tablaAmortizacion);

        new PagoGrupalDAO().addPagoGrupal(pagoGVO, con);
    }

    public static int matrizPagos(IntegranteCicloVO[] integrantes, PagoVO pago, HttpServletRequest request) {

        TablaAmortizacionVO[] arrayTabla = null;
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        PagoGrupalVO pagoGVO = new PagoGrupalVO();
        int resultado = 0;
        int tipoPago = 0;
        double montoTotalIntegrantes = 0;
        double montoTotalGrupo = 0;

        try {

            if (integrantes.length > 0) {

                arrayTabla = tablaDAO.getElementos(integrantes[1].idGrupo, integrantes[1].idCiclo, 1);
                pagoGVO.numGrupo = integrantes[1].idGrupo;
                pagoGVO.numCiclo = integrantes[1].idCiclo;
            }
            if (arrayTabla != null && arrayTabla.length > 0) {
                double pagoEsperado = arrayTabla[1].montoPagar;

                if ((arrayTabla[1].montoPagar) == pago.monto) {
                    tipoPago = 1;
                } else if ((arrayTabla[1].montoPagar) < pago.monto) {
                    tipoPago = 2;
                } else if ((arrayTabla[1].montoPagar) > pago.monto) {
                    tipoPago = 3;
                }

                montoTotalGrupo = pago.monto * 16;

                for (int i = 0; i < integrantes.length; i++) {
                    montoTotalIntegrantes += integrantes[i].monto;
                }

                switch (tipoPago) {
                    case 0:
                        break;
                    case 1:
                        //Logger.debug("Tipo pago: pago esperado.  Actualiza pago grupal e individual");							

                        //incrementa el número de pago
                        procesaPago(pago, pagoGVO, arrayTabla);
                        /*pagoGVO.numPago =  pagoGrupalDAO.getNumPagoGrupalActual(pagoGVO.numGrupo, pagoGVO.numCiclo) + 1;
                         pagoGVO.numTransaccion = 1;
                         pagoGVO.fechaPago = pago.fechaPago;
                         pagoGVO.monto = pago.monto;
                         pagoGrupalDAO.addPagoGrupal(pagoGVO);*/
                        for (int i = 0; i < integrantes.length; i++) {
                            //obtenemos el monto a pagar por cada integrante: 
                            double aPagarPorPersona = (integrantes[i].montoConSeguro * montoTotalGrupo) / montoTotalIntegrantes;

                            aPagarPorPersona = aPagarPorPersona / 16;

                            //Logger.debug("Pagando:" + aPagarPorPersona);
                            //registra los pagos individuales
                            PagoIndividualGruposVO pagoIndividual = new PagoIndividualGruposVO();
                            pagoIndividual.monto = aPagarPorPersona;
                            pagoIndividual.numCiclo = pagoGVO.numCiclo;
                            pagoIndividual.numCliente = integrantes[i].idCliente;
                            pagoIndividual.numGrupo = integrantes[i].idGrupo;
                            pagoIndividual.numPago = pagoGVO.numAmortizacion;
                            pagoIndividual.usuario = (request.getRemoteUser() != null ? request.getRemoteUser() : "sistema");
                            pagoIndividual.corroborar = 0;
                            //registra en la tabla bd
                            new PagoIndividualGruposDAO().addPagoIndividualGrupos(pagoIndividual);
                        }
                        break;
                    case 2:
                        procesaPago(pago, pagoGVO, arrayTabla);
                        //incrementa el número de pago
                        /*pagoGVO.numPago =  pagoGrupalDAO2.getNumPagoGrupalActual(pagoGVO.numGrupo, pagoGVO.numCiclo) + 1;
                         pagoGVO.numTransaccion = 1;
                         pagoGVO.fechaPago = pago.fechaPago;
                         pagoGVO.monto = pago.monto;
                         pagoGrupalDAO2.addPagoGrupal(pagoGVO);*/

 /*for ( int i=0 ; i < integrantes.length ; i++ ){
                         double aPagarPorPersona = 0;				
                         //registra los pagos individuales
                         PagoIndividualGruposVO pagoIndividual=new PagoIndividualGruposVO();
                         pagoIndividual.monto= aPagarPorPersona;
                         pagoIndividual.numCiclo= pagoGVO.numCiclo;
                         pagoIndividual.numCliente=integrantes[i].idCliente;
                         pagoIndividual.numGrupo= integrantes[i].idGrupo;
                         pagoIndividual.numPago= pagoGVO.numPago;
                        
                         pagoIndividual.usuario=request.getRemoteUser();
                         pagoIndividual.corroborar = 0;   //quiere decir que se modificará en la matriz de pagos  
                         //registra en la tabla bd
                         //new PagoIndividualGruposDAO().addPagoIndividualGrupos(pagoIndividual);						
                         }*/
                        break;
                    case 3:
                        procesaPago(pago, pagoGVO, arrayTabla);
                        //incrementa el número de pago
                        /*pagoGVO.numPago =  pagoGrupalDAO3.getNumPagoGrupalActual(pagoGVO.numGrupo, pagoGVO.numCiclo) + 1;
                         pagoGVO.numTransaccion = 1;
                         pagoGVO.fechaPago = pago.fechaPago;
                         pagoGVO.monto = pago.monto;
                         pagoGrupalDAO3.addPagoGrupal(pagoGVO);*/

 /*for ( int i=0 ; i < integrantes.length ; i++ ){
                         double aPagarPorPersona = 0;					
                         //registra los pagos individuales
                         PagoIndividualGruposVO pagoIndividual=new PagoIndividualGruposVO();
                         pagoIndividual.monto= aPagarPorPersona;
                         pagoIndividual.numCiclo= pagoGVO.numCiclo;
                         pagoIndividual.numCliente=integrantes[i].idCliente;
                         pagoIndividual.numGrupo= integrantes[i].idGrupo;
                         pagoIndividual.numPago= pagoGVO.numPago;
                         pagoIndividual.usuario=request.getRemoteUser();
                         pagoIndividual.corroborar = 0;  //quiere decir que se modificará en la matriz de pagos
                         //registra en la tabla bd
                         //new PagoIndividualGruposDAO().addPagoIndividualGrupos(pagoIndividual);						
                         }*/
                        IncidenciaPagoGrupalDAO incidenciasdao = new IncidenciaPagoGrupalDAO();
                        IncidenciaPagoGrupalVO incidencia = new IncidenciaPagoGrupalVO();
                        incidencia.fecha = pago.fechaPago;
                        incidencia.diferencia = pagoEsperado - pago.monto;
                        incidencia.montoDepositado = pago.monto;
                        incidencia.montoEsperado = pagoEsperado;

                        GrupoVO grupo = new GrupoVO();
                        GrupoDAO grupodao = new GrupoDAO();
                        grupo = grupodao.getGrupo(pagoGVO.numGrupo);

                        if (grupo != null) {
                            incidencia.nombreGrupo = grupo.nombre;
                            incidencia.numSucursal = grupo.sucursal;
                        }
                        incidenciasdao.addIncidencia(incidencia);

                        //FileManager fileTemp=new FileManager(ClientesConstants.RUTA_ARCHIVO_PAGOS_COMUNALES,"incidencias_"+new Date(Calendar.getInstance().getTimeInMillis()).toString()+".txt",true);
                        //fileTemp.agregarIncidencia(incidencia);
                        //fileTemp.guardarArchivo();							
                        break;
                }

            } else {
                myLogger.debug("No se pudo obtener el pago esperado para la matriz de pagos");
                resultado = 1;
            }

        } catch (Exception e) {
            myLogger.error("Error en Matriz de Pagos", e);
            resultado = 1;
        }

        return resultado;
    }

    public int matrizPagos(IntegranteCicloVO[] integrantes, PagoVO pago, String usuario, Connection con) {

        TablaAmortizacionVO[] arrayTabla = null;
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        PagoGrupalVO pagoGVO = new PagoGrupalVO();
        int resultado = 0;
        int tipoPago = 0;
        double montoTotalIntegrantes = 0;
        double montoTotalGrupo = 0;
        try {
            if (integrantes.length > 0) {
                arrayTabla = tablaDAO.getElementos(integrantes[1].idGrupo, integrantes[1].idCiclo, 1);
                pagoGVO.numGrupo = integrantes[1].idGrupo;
                pagoGVO.numCiclo = integrantes[1].idCiclo;
            }
            if (arrayTabla != null && arrayTabla.length > 0) {
                double pagoEsperado = arrayTabla[1].montoPagar;
                if ((arrayTabla[1].montoPagar) == pago.monto) {
                    tipoPago = 1;
                } else if ((arrayTabla[1].montoPagar) < pago.monto) {
                    tipoPago = 2;
                } else if ((arrayTabla[1].montoPagar) > pago.monto) {
                    tipoPago = 3;
                }
                montoTotalGrupo = pago.monto * 16;
                //montoTotalGrupo = pago.monto * arrayTabla.length-1;
                for (int i = 0; i < integrantes.length; i++) {
                    montoTotalIntegrantes += integrantes[i].monto;
                }
                switch (tipoPago) {
                    case 0://CASO NULO
                        break;
                    case 1://CASO PAGO IGUAL A PAGO SEMANAL
                        //incrementa el número de pago
                        procesaPago(pago, pagoGVO, arrayTabla, con);
                        for (int i = 0; i < integrantes.length; i++) {
                            //obtenemos el monto a pagar por cada integrante: 
                            double aPagarPorPersona = (integrantes[i].monto * montoTotalGrupo) / montoTotalIntegrantes;
                            aPagarPorPersona = aPagarPorPersona / 16;
                            //aPagarPorPersona = aPagarPorPersona / arrayTabla.length-1;
                            //registra los pagos individuales
                            PagoIndividualGruposVO pagoIndividual = new PagoIndividualGruposVO();
                            pagoIndividual.monto = aPagarPorPersona;
                            pagoIndividual.numCiclo = pagoGVO.numCiclo;
                            pagoIndividual.numCliente = integrantes[i].idCliente;
                            pagoIndividual.numGrupo = integrantes[i].idGrupo;
                            pagoIndividual.numPago = pagoGVO.numAmortizacion;
                            pagoIndividual.usuario = usuario;
                            pagoIndividual.corroborar = 0;
                            //registra en la tabla bd
                            new PagoIndividualGruposDAO().addPagoIndividualGrupos(con, pagoIndividual);
                        }
                        break;
                    case 2://CASO PAGO MAYOR A PAGO SEMANAL
                        procesaPago(pago, pagoGVO, arrayTabla, con);
                        break;
                    case 3://CASO PAGO MENOR A PAGO SEMANAL
                        procesaPago(pago, pagoGVO, arrayTabla, con);
                        IncidenciaPagoGrupalDAO incidenciasdao = new IncidenciaPagoGrupalDAO();
                        IncidenciaPagoGrupalVO incidencia = new IncidenciaPagoGrupalVO();
                        incidencia.fecha = pago.fechaPago;
                        incidencia.diferencia = pagoEsperado - pago.monto;
                        incidencia.montoDepositado = pago.monto;
                        incidencia.montoEsperado = pagoEsperado;

                        GrupoVO grupo = new GrupoVO();
                        GrupoDAO grupodao = new GrupoDAO();
                        grupo = grupodao.getGrupo(pagoGVO.numGrupo);

                        if (grupo != null) {
                            incidencia.nombreGrupo = grupo.nombre;
                            incidencia.numSucursal = grupo.sucursal;
                        }
                        incidenciasdao.addIncidencia(incidencia, con);
                        break;
                }
            } else {
                myLogger.debug("No se pudo obtener el pago esperado para la matriz de pagos");
                resultado = 1;
            }
        } catch (Exception e) {
            myLogger.error("Error en Matriz de Pagos", e);
            resultado = 1;
        }
        return resultado;
    }

    public static boolean clienteExisteCicloAnterior(GrupoVO grupo, ClienteVO cliente) throws ClientesException {
        boolean resultado = false;
        if (new IntegranteCicloDAO().integranteCiclo(grupo.idGrupo, cliente.idCliente) != 0) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean aplicaComision(int idCliente) throws ClientesException {
        boolean resultado = false;
        GrupoVO grupoAnterior = new GrupoDAO().getGrupoUltimoCicloCliente(idCliente);
        if (grupoAnterior == null || grupoAnterior.calificacion.equals("")) {
            resultado = true;
        } else if (grupoAnterior != null && (grupoAnterior.calificacion.equals("A") || grupoAnterior.calificacion.equals("B"))) {
            resultado = true;
        }

        return resultado;
    }

    public static boolean clienteExisteCicloAnterior(int idGrupo, int idCliente) throws ClientesException {
        boolean resultado = false;
        if (new IntegranteCicloDAO().integranteCiclo(idGrupo, idCliente) != 0) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean clienteExisteCicloActual(Connection conn, int idGrupo, int idCliente, CicloGrupalVO[] ciclo, DecisionComiteVO decisionComite, GrupoVO grupo) throws ClientesException {
        boolean resultado = false;
        IntegranteCicloVO integrante = new IntegranteCicloVO();
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
        int idCiclo = new CicloGrupalDAO().getCicloActual(idGrupo) - 1;
        int idCicloNumero = idCiclo + 1;
        if (ciclo != null && ciclo.length != 0) {
            if ((new IntegranteCicloDAO().integranteCiclo(idGrupo, idCliente) - 1) == idCiclo && ciclo[idCiclo].estatus == 1) {
                integrante = new IntegranteCicloDAO().getIntegranteCicloPorClienteCiclo(idGrupo, idCliente, idCicloNumero);
                integrante.idCliente = idCliente;
                integrante.monto = decisionComite.montoAutorizado;
                integrante.comision = decisionComite.comision;
                if (new IntegranteCicloDAO().updateIntegrante(conn, idGrupo, idCicloNumero, integrante) == 1) {
                    ciclo[idCiclo].integrantes = new IntegranteCicloDAO().getIntegrantes(idGrupo, idCicloNumero);
                    ciclo[idCiclo].monto = 0.0;
                    ciclo[idCiclo].montoConComision = 0.0;
                    for (int i = 0; i < ciclo[idCiclo].integrantes.length; i++) {
                        if (ciclo[idCiclo].integrantes[i].idCliente == idCliente) {
                            ciclo[idCiclo].monto += integrante.monto;
                            ciclo[idCiclo].integrantes[i].monto = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
                            ciclo[idCiclo].montoConComision += ciclo[idCiclo].integrantes[i].monto;
                        } else {
                            ciclo[idCiclo].monto += ciclo[idCiclo].integrantes[i].monto;
                            ciclo[idCiclo].integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo[idCiclo].integrantes[i].monto, ciclo[idCiclo].integrantes[i].comision, catComisionesGrupal);
                            ciclo[idCiclo].montoConComision += ciclo[idCiclo].integrantes[i].monto;
                        }
                    }
                    //GrupoHelper.obtenTablaAmortizacion(grupo, ciclo[idCiclo], new Date());
                    new CicloGrupalDAO().updateCiclo(ciclo[idCiclo]);
                    resultado = true;
                }
            }
        }
        return resultado;
    }

    public EventosDePagoVO[] obtieneMorososDelDia() throws ClientesException {

        EventosDePagoVO[] monitorDiario = new EventosPagosGrupalDAO().getVencimientosDelDia();
        ArrayList<EventosDePagoVO> arrayTemporal = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO[] monitorNuevo = null;

        for (int i = 0; monitorDiario != null && i < monitorDiario.length; i++) {
            EventosDePagoVO monitorTemporal = null;
            if (monitorDiario[i].esIBS) {
                monitorTemporal = new EventosPagosGrupalDAO().getSaldos(monitorDiario[i].numGrupo, monitorDiario[i].numCiclo);
            } else {
                monitorTemporal = new EventosPagosGrupalDAO().getSaldosT24(monitorDiario[i].numGrupo, monitorDiario[i].numCiclo);
            }

            if (monitorTemporal != null) {
                monitorTemporal.numPago = monitorDiario[i].numPago;
                arrayTemporal.add(monitorTemporal);
            }
        }

        if (arrayTemporal.size() > 0) {
            monitorNuevo = new EventosDePagoVO[arrayTemporal.size()];
            for (int a = 0; a < monitorNuevo.length; a++) {
                monitorNuevo[a] = (EventosDePagoVO) arrayTemporal.get(a);
            }
        }

        return monitorNuevo;
    }

    public void procesoMonitorPagosGrupos() {

        try {
            EventosDePagoVO[] monitorNuevo = obtieneMorososDelDia();
            EventosDePagoVO[] monitorActual = new EventosPagosGrupalDAO().getMonitoreoGeneral();
            EventosPagosGrupalDAO eventoDAO = new EventosPagosGrupalDAO();
            int cambios = -1;
            boolean procesado = false;

            if (monitorNuevo != null) {
                //Logger.debug("Recorriendo Incidencias...");
                for (int i = 0; i < monitorNuevo.length; i++) {
                    //cambios = 1;
                    procesado = eventoDAO.existeEventoMonitor(monitorNuevo[i].numGrupo, monitorNuevo[i].numCiclo, monitorNuevo[i].numPago);
                    cambios = -1;
                    cambios = procesoRegistro(monitorActual, monitorNuevo[i]);
                    if (cambios == -1) {
                        int noAtrasos = (int) Math.ceil((double) monitorNuevo[i].diasVencidos / 7d);
                        //myLogger.debug("Numero de atrasos : "+noAtrasos);
                        if (noAtrasos > 0 && noAtrasos < monitorNuevo[i].numPago) {
                            monitorNuevo[i].enMora = "SI";
                            monitorNuevo[i].numAtrasos = noAtrasos;
                            int identificarPar = noAtrasos % 2;
                            if (monitorNuevo[i].numAtrasos > 1 && monitorNuevo[i].numAtrasos <= 3) {
                                monitorNuevo[i].estatusVisitaSupervisor = 1;
                                monitorNuevo[i].fechaAlertaSupervisor = Convertidor.toSqlDate(new Date());
                            } else if (monitorNuevo[i].numAtrasos > 3 && monitorNuevo[i].numAtrasos <= 5) {
                                monitorNuevo[i].estatusVisitaGerente = 1;
                                monitorNuevo[i].fechaAlertaGerente = Convertidor.toSqlDate(new Date());
                            } else if (monitorNuevo[i].numAtrasos > 5) {
                                monitorNuevo[i].estatusVisitaGestor = 1;
                                monitorNuevo[i].fechaAlertaGestor = Convertidor.toSqlDate(new Date());

                            }

                            if (monitorNuevo[i].numAtrasos == 1 && !procesado) {
                                monitorNuevo[i].estatusReporteCobranza = 1;
                                monitorNuevo[i].fechaReporteCobranza = Convertidor.toSqlDate(new Date());
                            }
                            new EventosPagosGrupalDAO().addMonitor(monitorNuevo[i]);
                        }
                        //myLogger.debug("Aqui toy cambios = -1");
                    } else if (cambios != -1) {
                        //myLogger.debug("Aqui toy cambios = 1");
                        new EventosPagosGrupalDAO().updateMonitor(monitorActual[cambios]);
                    }
                }
            }
        } catch (Exception e) {
            myLogger.error("Problema en el proceso de monitor de pagos", e);
        }
    }

    public static int procesoRegistro(EventosDePagoVO[] monitorActual, EventosDePagoVO grupo) throws ClientesException {

        for (int i = 0; monitorActual != null && i < monitorActual.length; i++) {
            //Identifica si el vencimiento ya esta en eventos de pago
            if (monitorActual[i].numGrupo == grupo.numGrupo && monitorActual[i].numCiclo == grupo.numCiclo) {
                int noAtrasos = (int) Math.ceil((double) grupo.diasVencidos / 7d);
                //myLogger.debug("Numero de atrasos : "+noAtrasos+" Para el Numero Grupo: "+grupo.numGrupo+" con ciclo: "+grupo.numCiclo);
                if (noAtrasos > 0 && noAtrasos < monitorActual[i].numPago) {

                    if (monitorActual[i].estatusVisitaSupervisor == 1 || monitorActual[i].estatusVisitaGerente == 1) {
                        //fabrico Objeto insidencias y guardo esta incidencia
                        IncidenciasMonitorPagosVO incidencia = new IncidenciasMonitorPagosVO();
                        incidencia.numAlerta = monitorActual[i].identificador;
                        if (monitorActual[i].estatusVisitaSupervisor == 1) {
                            incidencia.visitaSupervisor = 1;
                            incidencia.fechaAlerta = monitorActual[i].fechaAlertaSupervisor;
                        }
                        if (monitorActual[i].estatusVisitaGerente == 1) {
                            incidencia.visitaGerente = 1;
                            incidencia.fechaAlerta = monitorActual[i].fechaAlertaGerente;
                        }
                        incidencia.numAtraso = monitorActual[i].numAtrasos;
                        incidencia.numPago = monitorActual[i].numPago;
                        incidencia.fechaRegistrada = Convertidor.toSqlDate(new Date());
                        incidencia.estatus = 1; //Estatus recien ingresado con fines de reporteria 2 Atendido
                        // Guado incidencia
                        new IncidenciasMonitorPagosDAO().addIncidenciaMonitorPagos(incidencia);
                    }

                    if (noAtrasos > 1) {
                        //Asigno nueva alarma segun corresponda ya que es una y una segun valla incrementando
                        if (monitorActual[i].estatusVisitaGerente == 1) {
                            monitorActual[i].estatusVisitaSupervisor = 1;
                            monitorActual[i].estatusVisitaGerente = 0;
                            monitorActual[i].fechaAlertaSupervisor = Convertidor.toSqlDate(new Date());

                        }
                        if (monitorActual[i].estatusVisitaSupervisor == 1) {
                            monitorActual[i].estatusVisitaSupervisor = 0;
                            monitorActual[i].estatusVisitaGerente = 1;
                            monitorActual[i].fechaAlertaGerente = Convertidor.toSqlDate(new Date());
                        }
                    }
                    //Si es el atrasos 1 :: activo seguimiento Call Center
                    if (noAtrasos == 1) {
                        monitorActual[i].estatusVisitaSupervisor = 0;
                        monitorActual[i].estatusVisitaGerente = 1;
                        monitorActual[i].fechaAlertaGerente = Convertidor.toSqlDate(new Date());
                        monitorActual[i].estatusReporteCobranza = 1;
                        monitorActual[i].fechaReporteCobranza = Convertidor.toSqlDate(new Date());
                    }
                    monitorActual[i].enMora = "SI";
                    monitorActual[i].enMora = grupo.enMora;
                    monitorActual[i].numPago = grupo.numPago;

                    return i;
                }
            }
        }
        return -1;
    }

    public boolean comparaFechasMonitor() {
        boolean esFechaAnterior = false;
        try {
            // Inicia comparacion entre la fecha actual y la ultima fecha de ejecucion de proceso
            String ultimaFecha = CatalogoHelper.getParametro("FECHA_EJECUCION_MONITOR_PAGOS_GRUPAL");
            SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaUltimoProceso = format.parse(ultimaFecha);
            Calendar hoy = Calendar.getInstance();
            Date fechaHoy = format.parse(hoy.get(Calendar.YEAR) + "-" + (hoy.get(Calendar.MONTH) + 1) + "-" + hoy.get(Calendar.DAY_OF_MONTH));
            if (fechaHoy.compareTo(fechaUltimoProceso) > 0) {
                esFechaAnterior = true;
            }
        } catch (ParseException exc) {
            myLogger.error("ParseException :" + exc);
        } finally {
        }
        return esFechaAnterior;
    }

    public void procesoRecalificacionGrupos() {

        try {
            myLogger.info("Es sabado inicia recalificacion automatica grupal::");
            if (new GrupoDAO().recalificacionGrupal() > 0) {
                myLogger.info("Finaliza recalificacion grupal correctamente::");
            } else {
                myLogger.info("Finaliza recalificacion grupal hubo un problema::");
            }
        } catch (Exception e) {
            myLogger.error("Problemas al recalcular las calificaciones de grupos", e);
        }
    }

    public void procesaTasaComision(GrupoVO grupo, boolean esNuevo) throws ClientesException {
    }

    public static CicloGrupalVO calculaPorcentajeRefinanciado(CicloGrupalVO ciclo, TreeMap catComisiones) throws ClientesException {
        CicloGrupalVO cicloRefinanciado = new CicloGrupalVO();
        cicloRefinanciado.integrantes = new IntegranteCicloDAO().getIntegrantes(ciclo.idGrupo, ciclo.idCiclo - 1);
        double totalDesembolsadoAnterior = 0.0;
        //Obtengo el total desembolsado del ciclo anterior para calcular porcentajes x integrante 
        for (int i = 0; i < cicloRefinanciado.integrantes.length; i++) {
            for (int j = 0; j < ciclo.integrantes.length; j++) {
                if (ciclo.integrantes[j].idCliente == cicloRefinanciado.integrantes[i].idCliente) {
                    totalDesembolsadoAnterior += ClientesUtil.calculaMontoSinComision(cicloRefinanciado.integrantes[i].monto, cicloRefinanciado.integrantes[i].comision, catComisiones);
                }
            }
        }
        //Asigno monto a refinaciar
        for (int i = 0; i < ciclo.integrantes.length; i++) {
            for (int a = 0; a < cicloRefinanciado.integrantes.length; a++) {
                if (ciclo.integrantes[i].idCliente == cicloRefinanciado.integrantes[a].idCliente) {
                    ciclo.integrantes[i].montoRefinanciado = ((1 + (ClientesUtil.calculaMontoSinComision(cicloRefinanciado.integrantes[a].monto, cicloRefinanciado.integrantes[a].comision, catComisiones) / totalDesembolsadoAnterior)) * ciclo.montoRefinanciado) - ciclo.montoRefinanciado;
                }
            }
        }
        return ciclo;
    }

    public static void obtieneRestructuraComunal(GrupoVO grupo) throws ClientesException {
        CicloGrupalVO cicloTemporal = grupo.ciclos[grupo.ciclos.length - 1];
        //Aqui agrago todo lo que necesita el grupo nuevo y sus integrantes
        grupo.idGrupoOriginal = grupo.idGrupo;
        grupo.idCicloOriginal = cicloTemporal.idCiclo;
        grupo.calificacion = "RG";
        grupo.idOperacion = ClientesConstants.REESTRUCTURA_GRUPAL;
        grupo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
        grupo.fechaCaptura = new Timestamp(System.currentTimeMillis());
        grupo.fechaFormacion = Convertidor.toSqlDate(grupo.fechaCaptura);

        grupo.ciclos = null;
        grupo.ciclos = new CicloGrupalVO[1];
        grupo.ciclos[0] = cicloTemporal;
        //Verificar que nivel de restructura aplica busca si hay relacion entre grupo y ciclo
        GrupoVO[] gruposRestructuraRelacionados = new GrupoDAO().obtieneRestructurasGrupo(grupo.idGrupo, cicloTemporal.idCiclo);
        if (gruposRestructuraRelacionados == null) {
            grupo.nivelRestructura = "ALFA";
        } else if (gruposRestructuraRelacionados.length == 1) {
            grupo.nivelRestructura = "BETA";
        } else if (gruposRestructuraRelacionados.length == 2) {
            grupo.nivelRestructura = null;
        }

        if (grupo.nivelRestructura != null) {
            grupo.nombre = grupo.nivelRestructura + " " + grupo.nombre;
            new GrupoDAO().addGrupo(grupo);
        }
        //Adiero propiedades restantes al ciclo nuevo del grupo
        grupo.ciclos[0].idGrupo = grupo.idGrupo;
        grupo.ciclos[0].idCiclo = 0;
        grupo.ciclos[0].tasa = 1;
        grupo.ciclos[0].plazo = 0;
        grupo.ciclos[0].tablaAmortizacion = null;
        //Recorro integrantes y calculo montos sujeridos
        for (int i = 0; i < grupo.ciclos[0].integrantes.length; i++) {
            grupo.ciclos[0].integrantes[i].idGrupo = grupo.idGrupo;
            grupo.ciclos[0].integrantes[i].idCiclo = 1;
            grupo.ciclos[0].integrantes[i].idSolicitud = 0;
            grupo.ciclos[0].integrantes[i].comision = 1;
            grupo.ciclos[0].integrantes[i].calificacion = 0;
            grupo.ciclos[0].integrantes[i].desembolsado = 0;
            grupo.ciclos[0].integrantes[i].monto = (grupo.ciclos[0].integrantes[i].monto / cicloTemporal.saldo.getMontoDesembolsado()) * (cicloTemporal.saldo.getSaldoTotalAlDia());
            if (gruposRestructuraRelacionados != null && gruposRestructuraRelacionados.length == 1) {
                IntegranteCicloVO[] integrantesInertetic = new IntegranteCicloDAO().getIntegrantes(gruposRestructuraRelacionados[0].idGrupo, 1);
                for (int j = 0; integrantesInertetic != null && j < integrantesInertetic.length; j++) {
                    if (grupo.ciclos[0].integrantes[i].idCliente == integrantesInertetic[j].idCliente) {
                        grupo.ciclos[0].integrantes[i].idSolicitud = integrantesInertetic[j].idSolicitud;
                    }
                }
            }
        }
    }

    public static CicloGrupalVO obtieneIntegrantesRefinanciamiento(CicloGrupalVO ciclo) throws ClientesException {

        TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        //double montorefinancearIndividual = (ciclo.saldo.capitalVencido+ciclo.saldo.feeVencidos+ciclo.saldo.interesMoratorio+ciclo.saldo.ivaMoratorio+ciclo.saldo.totalMulta+ciclo.saldo.ivaMulta)/ciclo.integrantes.length;
        ciclo.montoRefinanciado = ciclo.saldo.getCapitalVencido() + ciclo.saldo.getSaldoInteresVencido() + ciclo.saldo.getSaldoMora() + ciclo.saldo.getSaldoIVAMora() + ciclo.saldo.getSaldoMulta() + ciclo.saldo.getSaldoIVAMulta();
        IntegranteCicloVO[] nuevosIntegrantes = new IntegranteCicloDAO().getIntegrantesNuevoCiclo(ciclo.idGrupo, ClientesConstants.GRUPAL);
        IntegranteCicloVO[] integrantesNuevoCiclo = null;

        if (nuevosIntegrantes != null) {
            integrantesNuevoCiclo = new IntegranteCicloVO[ciclo.integrantes.length + nuevosIntegrantes.length];
        } else {
            integrantesNuevoCiclo = new IntegranteCicloVO[ciclo.integrantes.length];
        }

        for (int i = 0; i < ciclo.integrantes.length; i++) {
            integrantesNuevoCiclo[i] = ciclo.integrantes[i];
            integrantesNuevoCiclo[i].idCiclo++;
            //integrantesNuevoCiclo[i].montoRefinanciado = montorefinancearIndividual;
            integrantesNuevoCiclo[i].montoDesembolso = ClientesUtil.calculaMontoSinComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);
            integrantesNuevoCiclo[i].comision = 8;
            integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoConComision(integrantesNuevoCiclo[i].montoDesembolso, integrantesNuevoCiclo[i].comision, catComisiones);
            integrantesNuevoCiclo[i].idSolicitud = 0;
            integrantesNuevoCiclo[i].numCheque = null;
            integrantesNuevoCiclo[i].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
        }

        for (int i = ciclo.integrantes.length; nuevosIntegrantes != null && i < integrantesNuevoCiclo.length; i++) {
            integrantesNuevoCiclo[i] = nuevosIntegrantes[i - ciclo.integrantes.length];
            integrantesNuevoCiclo[i].calificacion = ScoringUtil.getCalificacionCirculo(new InformacionCrediticiaDAO().getLastInfoCrediticia(integrantesNuevoCiclo[i].idCliente));
            integrantesNuevoCiclo[i].montoDesembolso = ClientesUtil.calculaMontoSinComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);
            integrantesNuevoCiclo[i].comision = 8;
            integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoConComision(integrantesNuevoCiclo[i].montoDesembolso, integrantesNuevoCiclo[i].comision, catComisiones);
            integrantesNuevoCiclo[i].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
        }
        ciclo.integrantes = integrantesNuevoCiclo;
        calculaPorcentajeRefinanciado(ciclo, catComisiones);
        ciclo.tablaAmortizacion = null;
        return ciclo;
    }

    public static CicloGrupalVO procesaIntegrantesRefinanciamiento(Connection conn, CicloGrupalVO ciclo, HttpServletRequest request, boolean esNuevo) throws Exception {
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        int integrantesSeleccionados = 0;
        //double totalVencido = HTMLHelper.getParameterDouble(request, "totalVencido");
        ciclo.montoRefinanciado = HTMLHelper.getParameterDouble(request, "totalVencido");
        ciclo.integrantes = GrupoHelper.getIntegrantesCiclo(request);
        //Recorro integrantes verifico cuales son de Refinanciamiento y checo si esta seleccionado
        for (int i = 0; i < ciclo.integrantes.length; i++) {
            String nombre = "desembolso" + i;
            if (ciclo.integrantes[i].montoRefinanciado != 0 && HTMLHelper.getCheckBox(request, nombre)) {
                integrantesSeleccionados++;
            }
            if (!HTMLHelper.getCheckBox(request, nombre) && !esNuevo) {
                new IntegranteCicloDAO().deleteIntegrante(conn, ciclo.idGrupo, ciclo.idCiclo, ciclo.integrantes[i].idCliente);
            }
        }
        //Obtengo integrantes seleccionados
        GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
        //Re asigno el monto a Refinancear por cada integrante
        calculaPorcentajeRefinanciado(ciclo, catComisionesGrupal);
        for (int i = 0; i < ciclo.integrantes.length; i++) {
            //if( ciclo.integrantes[i].montoRefinanciado != 0 )
            //	ciclo.integrantes[i].montoRefinanciado = totalVencido/integrantesSeleccionados;
            if (!esNuevo) {
                new IntegranteCicloDAO().updateIntegrante(conn, ciclo.integrantes[i].idGrupo, ciclo.integrantes[i].idCiclo, ciclo.integrantes[i]);
            }
        }
        //Procesa solicitudes
        SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, ciclo.tasa);

        ciclo.monto = 0.0;
        ciclo.montoConComision = 0.0;
        //recorre y recalcula montos totales
        for (int i = 0; i < ciclo.integrantes.length; i++) {
            ciclo.integrantes[i].montoDesembolso = ciclo.integrantes[i].monto;
            ciclo.monto += ciclo.integrantes[i].monto + ciclo.integrantes[i].primaSeguro;
            ciclo.integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].monto + ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal);
            ciclo.montoConComision += ciclo.integrantes[i].monto + ciclo.integrantes[i].montoRefinanciado;
            ciclo.integrantes[i].desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
        }

        return ciclo;
    }

    public static CicloGrupalVO procesaCancelaDesembolsoRefinanciamiento(Connection conn, CicloGrupalVO ciclo, int idCliente) throws Exception {
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        double totalVencido = 0;
        //int integarntesRefinanciados = 0;
        IntegranteCicloVO[] integrantesNuevos = new IntegranteCicloVO[ciclo.integrantes.length - 1];
        boolean cancelacionSencilla = false;
        //Re calculo el monto a Refinancear por cada integrante
        for (int i = 0, nuevo = 0; i < ciclo.integrantes.length; i++) {
            if (ciclo.integrantes[i].montoRefinanciado != 0) {
                totalVencido += ciclo.integrantes[i].montoRefinanciado;
                //integarntesRefinanciados++;
            } else if (ciclo.integrantes[i].idCliente == idCliente) {
                cancelacionSencilla = true;
            }
            if (ciclo.integrantes[i].idCliente != idCliente) {
                integrantesNuevos[nuevo] = ciclo.integrantes[i];
                nuevo++;
            }
        }
        ciclo.montoRefinanciado = totalVencido;
        ciclo.integrantes = integrantesNuevos;
        new IntegranteCicloDAO().deleteIntegrante(conn, ciclo.integrantes[0].idGrupo, ciclo.integrantes[0].idCiclo, idCliente);
        ciclo.montoConComision = 0;
        ciclo.monto = 0;
        if (cancelacionSencilla) {
            for (int i = 0; i < integrantesNuevos.length; i++) {
                ciclo.montoConComision += integrantesNuevos[i].monto + ClientesUtil.calculaMontoConComision(integrantesNuevos[i].primaSeguro, integrantesNuevos[i].comision, catComisionesGrupal);
                ciclo.monto += ClientesUtil.calculaMontoSinComision(integrantesNuevos[i].monto, integrantesNuevos[i].comision, catComisionesGrupal) + integrantesNuevos[i].primaSeguro;
            }
        } else {
            calculaPorcentajeRefinanciado(ciclo, catComisionesGrupal);
            for (int i = 0; i < integrantesNuevos.length; i++) {
                //if( integrantesNuevos[i].montoRefinanciado!=0 )
                //integrantesNuevos[i].montoRefinanciado = totalVencido/(integarntesRefinanciados-1);
                new IntegranteCicloDAO().updateIntegrante(conn, integrantesNuevos[i].idGrupo, integrantesNuevos[i].idCiclo, integrantesNuevos[i]);
                ciclo.montoConComision += integrantesNuevos[i].monto + integrantesNuevos[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(integrantesNuevos[i].primaSeguro, integrantesNuevos[i].comision, catComisionesGrupal);
                integrantesNuevos[i].monto = ClientesUtil.calculaMontoSinComision(integrantesNuevos[i].monto, integrantesNuevos[i].comision, catComisionesGrupal);
                ciclo.monto += integrantesNuevos[i].monto + integrantesNuevos[i].primaSeguro;
            }
        }

        //Actualiza ciclo
        new CicloGrupalDAO().updateCiclo(conn, ciclo);
        //Actualiza decicion comite
        SolicitudUtil.procesaRenovaciones(conn, integrantesNuevos, 3);

        return ciclo;
    }

    public static boolean procesaIntegrantesConfirmacionDesembolso(Connection conn, CicloGrupalVO ciclo, HttpServletRequest request, int idSucursal, Date fechaDispersion) throws Exception {

        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        Vector<IntegranteCicloVO> integrantesSeleccionados = new Vector<IntegranteCicloVO>();
        Vector<IntegranteCicloVO> integrantesDeseleccionados = new Vector<IntegranteCicloVO>();
        java.sql.Date fechaUltCred = null;
        IntegranteCicloDAO inteDAO = new IntegranteCicloDAO();
        IntegrantesHelper inteHelp = new IntegrantesHelper();
        for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
            String nombre = "desembolso" + i;
            int idCliente = HTMLHelper.getParameterInt(request, "idCliente" + i);
            for (int a = 0; a < ciclo.integrantes.length; a++) {
                if (ciclo.integrantes[a].idCliente == idCliente) {
                    if (HTMLHelper.getCheckBox(request, nombre)) {
                        ciclo.integrantes[a].rol = HTMLHelper.getParameterInt(request, "rol" + i);
                        integrantesSeleccionados.add(ciclo.integrantes[a]);
                    } else {
                        integrantesDeseleccionados.add(ciclo.integrantes[a]);
                    }
                }
            }

        }
        //proceso seleccionados
        //discrimino de cheques y tomo solo si es OPago
        //Si es OP unicamente actualizo OPago.Estatus = 1;
        IntegranteCicloVO[] integrantes = new IntegranteCicloVO[integrantesSeleccionados.size()];
        Iterator<IntegranteCicloVO> itSeleccionados = integrantesSeleccionados.iterator();
        int i = 0;
        int confirmacionOk = 0;
        while (itSeleccionados.hasNext()) {
            confirmacionOk = 0;
            IntegranteCicloVO integranteConfirmado = (IntegranteCicloVO) itSeleccionados.next();
            if (integranteConfirmado.ordenPago != null && integranteConfirmado.ordenPago.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                if (integranteConfirmado.medioCobro == 0) {
                    integranteConfirmado.ordenPago.setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                } else {
                    integranteConfirmado.ordenPago.setEstatus(0);
                }
                confirmacionOk = new OrdenDePagoDAO().updateOrdenDePagoConfirmaDesembolso(conn, integranteConfirmado.ordenPago);
            } else {
                integranteConfirmado.ordenPago = new OrdenDePagoDAO().getOrdenDePago(integranteConfirmado.idCliente, integranteConfirmado.idSolicitud);
                if (integranteConfirmado.ordenPago.getIdOrdenPago() != 0) {
                    integranteConfirmado.ordenPago.setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                    confirmacionOk = new OrdenDePagoDAO().updateOrdenDePagoConfirmaDesembolso(conn, integranteConfirmado.ordenPago);
                } else { // Verifico si es cheque
                    SolicitudVO solicitud = new SolicitudDAO().getSolicitud(integranteConfirmado.idCliente, integranteConfirmado.idSolicitud);
                    ChequeVO cheque = new ChequeVO();
                    cheque = new ChequesDAO().getCheque(Integer.parseInt(solicitud.numCheque), solicitud.idCliente, idSucursal);
                    if (cheque != null && cheque.numCliente == solicitud.idCliente) {
                        confirmacionOk = 1;
                    }
                }
            }
            if (integranteConfirmado.tarjetaCobro != null && integranteConfirmado.tarjetaCobro.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integranteConfirmado.tarjetaCobro.setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                confirmacionOk = new TarjetaDAO().updateTarjetaConfirmaDesembolso(conn, integranteConfirmado.tarjetaCobro);
            }
            fechaUltCred = inteDAO.getTipoIntegrante(integranteConfirmado.idCliente);
            inteDAO.updateTipoIntegrante(ciclo, integranteConfirmado.idCliente, inteHelp.getTipoCliente(integranteConfirmado.idCliente, Convertidor.toSqlDate(fechaDispersion), fechaUltCred));
            inteDAO.updateRolIntegrante(conn, integranteConfirmado);
            if (confirmacionOk == 1) {
                integrantes[i] = integranteConfirmado;
                i++;
            } else {
                return false;
            }
        }

        //proceso no seleccionados
        Calendar cal = Calendar.getInstance();
        Iterator<IntegranteCicloVO> itDeseleccionados = integrantesDeseleccionados.iterator();
        while (itDeseleccionados.hasNext()) {
            //tomo solicitud regreso estatus desembolsado
            IntegranteCicloVO integranteDeseleccionado = (IntegranteCicloVO) itDeseleccionados.next();
            SolicitudVO solicitud = new SolicitudVO();
            solicitud = new SolicitudDAO().getSolicitud(integranteDeseleccionado.idCliente, integranteDeseleccionado.idSolicitud);
            ChequeVO cheque = new ChequeVO();
            if (integranteDeseleccionado.ordenPago == null || integranteDeseleccionado.ordenPago.getReferencia() == null) {
                cheque = new ChequesDAO().getCheque(Integer.parseInt(solicitud.numCheque), solicitud.idCliente, idSucursal);
            }
            solicitud.desembolsado = ClientesConstants.LISTO_DESEMBOLSAR;
            solicitud.fechaDesembolso = null;
            solicitud.numCheque = null;
            new SolicitudDAO().updateSolicitud(conn, integranteDeseleccionado.idCliente, solicitud);
            //Quito integrante del ciclo
            IntegranteCicloDAO integrateCicloDAO = new IntegranteCicloDAO();
            integrateCicloDAO.deleteIntegrante(conn, integranteDeseleccionado.idGrupo, integranteDeseleccionado.idCiclo, integranteDeseleccionado.idCliente);
            integrateCicloDAO.deleteIntegranteODS(integranteDeseleccionado.idGrupo, integranteDeseleccionado.idCiclo, integranteDeseleccionado.idCliente);
            
            //actualizo OPago cambio estatus a 7
            if (integranteDeseleccionado.ordenPago != null && integranteDeseleccionado.ordenPago.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integranteDeseleccionado.ordenPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                integranteDeseleccionado.ordenPago.setUsuario(request.getRemoteUser());
                integranteDeseleccionado.ordenPago.setFechaCancelacion(new Timestamp(System.currentTimeMillis()));
                new OrdenDePagoDAO().updateOrdenDePagoConfirmaDesembolso(conn, integranteDeseleccionado.ordenPago);
            }
            if (integranteDeseleccionado.tarjetaCobro != null && integranteDeseleccionado.tarjetaCobro.getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integranteDeseleccionado.tarjetaCobro.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                integranteDeseleccionado.tarjetaCobro.setUsuario(request.getRemoteUser());
                integranteDeseleccionado.tarjetaCobro.setFechaCancelacion(new Timestamp(System.currentTimeMillis()));
                new TarjetaDAO().updateTarjetaConfirmaDesembolso(conn, integranteDeseleccionado.tarjetaCobro);
            }
            if (cheque != null) {
                //Si es cheque actualizo d_cheques y cancelo por desistimiento
                cheque.tipoCancelacion = 10;//Desistimeinto del Credito
                cheque.estatus = ClientesConstants.CHEQUE_CANCELADO;
                cheque.fechaCancelacion = Convertidor.toSqlDate(cal.getTime());
                new ChequesDAO().updateCheque(conn, cheque);
            }
        }

        //Lo adiero al objeto que recibi en ciclo
        ciclo.integrantes = integrantes;
        ciclo.montoConComision = 0;
        ciclo.monto = 0;
        for (int a = 0; ciclo.integrantes != null && a < ciclo.integrantes.length; a++) {
            ciclo.montoConComision += ciclo.integrantes[a].montoConSeguro + ciclo.integrantes[a].montoRefinanciado;
            ciclo.monto += ClientesUtil.calculaMontoSinComision(ciclo.integrantes[a].monto, ciclo.integrantes[a].comision, catComisionesGrupal);
            //Sumando Prima del Seguro
            ciclo.monto += ciclo.integrantes[a].primaSeguro;
            ciclo.montoConComision += ClientesUtil.calculaMontoConComision(ciclo.integrantes[a].primaSeguro, ciclo.integrantes[a].comision, catComisionesGrupal);
        }
        ciclo.desembolsado = ClientesConstants.CICLO_DESEMBOLSO_CONFIRMADO;

        //regreso boleano
        return true;
    }

    public static boolean aplicaApertura(GrupoVO grupo) {
        boolean resultado = false;
        if (grupo != null && grupo.ciclos != null && grupo.ciclos.length > 0) {
            int cuotas = grupo.ciclos[grupo.ciclos.length - 1].saldo.getNumeroCuotasTranscurridas();
            if ((grupo.ciclos[grupo.ciclos.length - 1].saldo.getPlazo()) - cuotas < 5 && (grupo.ciclos[grupo.ciclos.length - 1].saldo.getPlazo()) - cuotas > 0 && grupo.ciclos[grupo.ciclos.length - 1].saldo.getEstatus() == 1) {
                resultado = true;
            }
        }
        return resultado;
    }

    public static boolean procesaIntegrantesConfirmacionDispersionInterciclo(Connection con, CicloGrupalVO cicloVO, HttpServletRequest request, int idSucursal, Date fechaDispersion, ArrayList<IntegranteCicloVO> arrIntegrantes, int semDisp) throws Exception {

        OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        int confirmacionOk = 0;
        double montoIncremento = 0;
        for (IntegranteCicloVO integrante : arrIntegrantes) {
            //INSERTA EL CLIENTE
            integrante.ordenPago = new OrdenDePagoDAO().getOrdenDePago(integrante.getIdCliente(), integrante.getIdSolicitud());
            if (integrante.getOrdenPago() != null && integrante.getOrdenPago().getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integrante.getOrdenPago().setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                confirmacionOk = ordenDAO.updateOrdenDePagoConfirmaDesembolso(con, integrante.getOrdenPago());
            }
            integrante.tarjetaCobro = tarjetaDAO.getTarjetaClinete(integrante.getIdCliente(), integrante.getIdSolicitud());
            if (integrante.getTarjetaCobro() != null && integrante.getTarjetaCobro().getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integrante.getTarjetaCobro().setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                confirmacionOk = tarjetaDAO.updateTarjetaConfirmaDesembolso(con, integrante.getTarjetaCobro());
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                integrante.setTipo(ClientesConstants.TIPO_CLIENTE_INTERCICLO);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                integrante.setTipo(ClientesConstants.TIPO_CLIENTE_INTERCICLO_2);
            }
            // Aqui si cuenta con seguro 
            integranteDAO.addIntegrante(con, cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), integrante);
            // Monto con seguro financiado para el ciclo Agosto 2017
            montoIncremento += integrante.getMontoConSeguro();
        }
        //ciclo.integrantes = integrantes;
        cicloVO.setMonto(cicloVO.getMonto() + montoIncremento);
        cicloVO.setMontoConComision(cicloVO.getMontoConComision() + montoIncremento);
        return true;
    }
    
    public static boolean procesaIntegrantesConfirmacionDispersionAdicional(Connection con, CicloGrupalVO cicloVO, HttpServletRequest request, int idSucursal, Date fechaDispersion, ArrayList<IntegranteCicloVO> arrIntegrantes, int semDisp) throws Exception {

        OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        int confirmacionOk = 0;
        double montoIncremento = 0;
        for (IntegranteCicloVO integrante : arrIntegrantes) {
            //INSERTA EL CLIENTE
            /*integrante.ordenPago = new OrdenDePagoDAO().getOrdenPago(integrante.getIdCliente(), integrante.getIdSolicitud());
            if (integrante.getOrdenPago() != null && integrante.getOrdenPago().getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integrante.getOrdenPago().setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                confirmacionOk = ordenDAO.updateOrdenDePagoConfirmaDesembolso(con, integrante.getOrdenPago());
            }
            integrante.tarjetaCobro = tarjetaDAO.getTarjetaClinete(integrante.getIdCliente(), integrante.getIdSolicitud());
            if (integrante.getTarjetaCobro() != null && integrante.getTarjetaCobro().getEstatus() == ClientesConstants.OP_POR_CONFIRMAR) {
                integrante.getTarjetaCobro().setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                confirmacionOk = tarjetaDAO.updateTarjetaConfirmaDesembolso(con, integrante.getTarjetaCobro());
            }*/
            /**
             * JECB 01/10/2017
             * Se habilitan las semanas de adicional extras 
             * semana 5,7,9 para ser consideradas en la dispersion
             */
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_4);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_5) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_5);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_6) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_6);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_7) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_7);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_8) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_8);
            }
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_9) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_9);
            }            
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_10) {
                integrante.setTipo_adicional(ClientesConstants.TIPO_CLIENTE_ADICONAL_10);
            }
            integranteDAO.updateIntegranteAdicional(con, cicloVO.getIdGrupo(), cicloVO.getIdCiclo(), integrante);
            montoIncremento += integrante.getMonto();
        }
        //ciclo.integrantes = integrantes;
        cicloVO.setMonto(cicloVO.getMonto() + montoIncremento);
        cicloVO.setMontoConComision(cicloVO.getMontoConComision() + montoIncremento);
        return true;
    }

    public static boolean validaDiaSolicitudDesembolso(CicloGrupalVO ciclo) throws Exception {
        boolean resultado = false;
        //BitacoraCicloVO BitacoraVO = new BitacoraCicloVO();
        BitacoraCicloDAO BitacoraDAO = new BitacoraCicloDAO();
        ArrayList<BitacoraCicloVO> BitacoraVO = new ArrayList<BitacoraCicloVO>();
        java.sql.Date fechahoy, fechaAut = null;
        try {
            BitacoraVO = BitacoraDAO.getBitacoraFechaNumComent(ciclo.idGrupo, ciclo.idCiclo);
            fechahoy = Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(Calendar.getInstance().getTime())));
            for (int i = 0; i < BitacoraVO.size(); i++) {
                System.out.println("BitacoraVO.get(i).getEstatus(): " + BitacoraVO.get(i).getEstatus());
                if (BitacoraVO.get(i).getEstatus() == ClientesConstants.CICLO_AUTORIZADO) {
                    fechaAut = Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(BitacoraVO.get(i).getFechaHora())));
                    System.out.println("fechaAut: " + fechaAut);
                } else {
                    System.out.println("entro al break");
                    break;
                }
            }
            System.out.println("fechahoy: " + fechahoy);
            System.out.println("Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString( ciclo.fechaDispersion)))) " + Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(ciclo.fechaDispersion))));
            if (fechahoy.after(fechaAut)) {
                resultado = true;
            } else if (fechahoy.equals(fechaAut) && fechahoy.before(Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(ciclo.fechaDispersion))))) {
                resultado = true;
            }
        } catch (Exception e) {
            myLogger.error("validaSolciitudDesembolso", e);
            throw new ClientesException(e.getMessage());
        }
        return resultado;
    }

    public static boolean validafechaDesembolso(CicloGrupalVO ciclo) throws Exception {
        boolean resultado = true;
        java.sql.Date fechahoy, fechaDisp = null;
        try {

            fechahoy = Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(Calendar.getInstance().getTime())));
            fechaDisp = Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(ciclo.fechaDispersion)));

            System.out.println("fechahoy: " + fechahoy);
            System.out.println("Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString( ciclo.fechaDispersion)))) " + Convertidor.toSqlDate(Convertidor.stringToDate(Convertidor.dateToString(ciclo.fechaDispersion))));
            if (fechaDisp.before(fechahoy)) {
                resultado = false;
            }
        } catch (Exception e) {
            myLogger.error("validaSolciitudDesembolso", e);
            throw new ClientesException(e.getMessage());
        }
        return resultado;
    }
    
    public static String validaPorcentajeAutExcepcionGrupo(int numGrupo, int numCiclo, int intercicloTotal, int autExcepInterciclo) throws ClientesException{
        
        IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
        CreditoDAO creditoDAO = new CreditoDAO();
        
        IntegranteCicloVO[] integrantes =  integranteDao.getIntegrantes(numGrupo, numCiclo);
        List<CreditoVO> creditosAutExcep = creditoDAO.getCreditosGrupoAutExcepcion(numGrupo, numCiclo);
        int numAutPorExceGrupo = creditosAutExcep.size();
        numAutPorExceGrupo = numAutPorExceGrupo + autExcepInterciclo;
        
        int integrantesTotal = integrantes.length + intercicloTotal;
        
        myLogger.info("Numero de Integrantes del grupo: "+integrantesTotal);
        myLogger.info("Numero de clientes autorizados por excepcion del grupo: "+numAutPorExceGrupo);
        if(integrantesTotal <= 10){
            myLogger.info("Grupo con menos o igual a 10 integrantes");
            //Si el numero actual de autorizados por excepcion es menor a 3 entonces aun se puede agregar otro al grupo
            if(numAutPorExceGrupo<=3){
                myLogger.info("El Grupo "+numGrupo+" con ciclo "+numCiclo+" cumple con la validación de porcentaje de autorizados por excepción");
            }else{
                return "Los Grupos con 10 integrantes o menos pueden tener maximo 3 integrates Autorizados por Excepción";
            }
            
        }else{
            myLogger.info("Grupo con mas de 10 integrantes");
            
            double porcentajeAutorizadoMaximo = Double.parseDouble(CatalogoHelper.getParametro("PORCENTAJE_GRUPO_AUTORIZADOS_POR_EXCEPCION"));
            double porcentajeIntegrantesAut = (porcentajeAutorizadoMaximo / 100) * integrantesTotal;
            myLogger.info("Integrantes autorizados MAX: "+porcentajeIntegrantesAut);
            double porcentajeRedondeado = new BigDecimal(porcentajeIntegrantesAut).setScale(0, RoundingMode.HALF_DOWN).doubleValue();
            myLogger.info("Integrantes autorizados Redondeado: "+porcentajeRedondeado);
            
            if(numAutPorExceGrupo <= porcentajeRedondeado){
                myLogger.info("El Grupo "+numGrupo+" con ciclo "+numCiclo+" cumple con la validación de porcentaje de autorizados por excepción");
            }else{
                return "El Grupo solo permite tener "+((int)porcentajeRedondeado)+" integrantes Autorizados por Excepción";
            }
        }
        
        return null;
    }
    
    /**
     * Metodo que valida si un grupo puede dispersarse con clientes autorizados por excepcion.
     * Valida si el grupo ya se encuentra con clientes autorizados por excepcion y si no valida si aun puede dispersarse
     * 
     * @return 
     */
    public static String validaGruposAutorizadosExcepcion(int numGrupo, int numCiclo, int numeroInterciclosAutorizadosPorExepcion) throws ClientesException{
        IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
        List<IntegranteCicloVO> equiposAutExcepcionList = integranteDao.obtenerEquiposAutExcDispersados();
        
        int numeroGrupos = equiposAutExcepcionList.size();
        myLogger.info("Numero de grupos con clientes autorizados por excepcion actual: "+numeroGrupos);
        boolean grupoAutPorExcepcion = equipoAutPorExepcion(numGrupo, numCiclo, equiposAutExcepcionList);
        
        
        
        //Si el grupo no se encuentra en los autorizados por excepcion, entonces se suma a la lista
        if(!grupoAutPorExcepcion){
            CreditoDAO creditoDAO = new CreditoDAO();
            List<CreditoVO> creditosAutExcep = creditoDAO.getCreditosGrupoAutExcepcion(numGrupo, numCiclo);
            
            int numAutorizadosExcepcion = creditosAutExcep.size() + numeroInterciclosAutorizadosPorExepcion;
            myLogger.info("Numero de clientes autorizados por excepción del grupo: "+numAutorizadosExcepcion);
            //Validamos si el equipo actual tiene clientes autorizados por excepion, si si tiene entonces se suma al numero de equipos por excepcion para validarlo
            if(numAutorizadosExcepcion > 0){
                int numGruposPermitidos = Integer.parseInt(CatalogoHelper.getParametro("NUMERO_GRUPOS_MAX_AUTORIZADOS_POR_EXCEPCION"));
                numeroGrupos = numeroGrupos + 1;

                if(numeroGrupos>numGruposPermitidos){
                    myLogger.info("Solo se permiten "+numGruposPermitidos+" grupos dispersados que tengan clientes Autorizados por Excepción");
                    return "Solo se permiten "+numGruposPermitidos+" grupos dispersados que tengan clientes Autorizados por Excepción";
                }
            }
            
        }
        
        return null;
    }
    
     /**
     * Metodo que busca si el equipo ya tiene clientes autorizados por excepcion
     * 
     * @param idGrupo
     * 
     * @param ciclo
     * 
     * @return
     */
    public static boolean equipoAutPorExepcion(int idGrupo, int ciclo, List<IntegranteCicloVO> integrantes){
        boolean equipoAutPorExcepcion = false;
        
        for(IntegranteCicloVO ic : integrantes){
            if(idGrupo == ic.idGrupo && ciclo == ic.idCiclo){
                equipoAutPorExcepcion = true;
            }
        }
        
        return equipoAutPorExcepcion;
    } 

}

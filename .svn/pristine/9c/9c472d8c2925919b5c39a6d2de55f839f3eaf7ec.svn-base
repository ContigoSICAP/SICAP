package com.sicap.clientes.util;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.AseguradosDAO;
import com.sicap.clientes.dao.BeneficiarioDAO;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.LimiteCreditoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.SegurosVO;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Calendar;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.util.Convertidor;

public class SolicitudUtil {

    private static Logger myLogger = Logger.getLogger(SolicitudUtil.class);

    public static int getIndice(SolicitudVO[] solicitudes, int idSolicitud) {

        int indice = -1;

        if (solicitudes != null && solicitudes.length > 0 && idSolicitud != 0) {
            for (int i = 0; i < solicitudes.length; i++) {
                if (solicitudes[i].idSolicitud == idSolicitud) {
                    indice = i;
                }
            }
        }

        return indice;
    }

    public static void cambiaEstatusSolicitud(ClienteVO cliente, int idSolicitud) throws ClientesException {
        SolicitudDAO solicituddao = new SolicitudDAO();
        if (cliente.solicitudes[idSolicitud].decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
            cliente.solicitudes[idSolicitud].estatus = ClientesConstants.SOLICITUD_RECHAZADA;
        } else {
            cliente.solicitudes[idSolicitud].estatus = ClientesConstants.SOLICITUD_AUTORIZADA;
        }
        solicituddao.updateSolicitud(cliente.idCliente, cliente.solicitudes[idSolicitud]);

    }

    //public static int asignaEstatusSolicitud(ClienteVO cliente, int idSolicitud) throws ClientesException {
    public static int asignaEstatusSolicitud(SolicitudVO solicitud, int idSolicitud) throws ClientesException {

        //if (cliente.solicitudes[idSolicitud].decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
        if (solicitud.decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
            return ClientesConstants.SOLICITUD_RECHAZADA;
        } else {
            return ClientesConstants.SOLICITUD_AUTORIZADA;
        }

    }

    public static boolean creditoEnviado(int idCliente, int idSolicitud, int idProd) throws ClientesException {

        if (idProd == 3) {
            return new CicloGrupalDAO().grupoEnviado(idCliente, idSolicitud);
        } else {
            return new SolicitudDAO().solicitudEnviada(idCliente, idSolicitud);
        }
    }

    public static boolean aplicaNuevaSolicitud(ClienteVO cliente) throws ClientesException {
        IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
        IntegranteCicloVO integranteCicloVO = new IntegranteCicloVO();
        SaldoIBSVO saldoResultadoVO = new SaldoIBSVO();
        for (int i = 0; i < cliente.solicitudes.length; i++) {
            if (cliente.solicitudes[i].tipoOperacion == ClientesConstants.GRUPAL) {
                if (cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO) {
                    //if(integranteCicloDAO.getVerificaIntegrante(cliente.solicitudes[i].idCliente) == 1)
                    if (!integranteCicloDAO.getIntegranteActivo(cliente.solicitudes[i].idCliente).equals("")) {
                        return false;
                    }
                } else if (cliente.solicitudes[i].desembolsado != ClientesConstants.SOLICITUD_RECHAZADA) {
                    return false;
                }
            } else if (cliente.solicitudes[i].tipoOperacion != ClientesConstants.GRUPAL) {
                if (cliente.solicitudes[i].desembolsado != ClientesConstants.SOLICITUD_RECHAZADA) {
                    return false;
                }
            } else {
                return false;
            }
            /*if (cliente.solicitudes[i].tipoOperacion != ClientesConstants.GRUPAL && cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO) {
                System.out.println("******** saldo "+cliente.solicitudes[i].saldo+" estatus "+cliente.solicitudes[i].saldo.getEstatus());
                if (cliente.solicitudes[i].saldo != null && (cliente.solicitudes[i].saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE || cliente.solicitudes[i].saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA)) {
                    return false;
                }
            }
            if (cliente.solicitudes[i].tipoOperacion == ClientesConstants.GRUPAL && cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO) {
                integranteCicloVO = integranteCicloDAO.getIntegranteCiclo(cliente.idCliente, cliente.solicitudes[i].idSolicitud);
                if (integranteCicloVO != null) {
                    CicloGrupalVO ciclo = new CicloGrupalDAO().getCiclo(integranteCicloVO.idGrupo, integranteCicloVO.idCiclo);
                    if (ciclo.idCuentaIBS != 0 && ciclo.idCreditoIBS != 0) {
                        saldoResultadoVO = new SaldoIBSDAO().getSaldo(ciclo.idGrupo, ciclo.idCiclo, ClientesConstants.GRUPAL);
                    } else {
                        saldoResultadoVO = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(integranteCicloVO.idGrupo, integranteCicloVO.idCiclo, ClientesConstants.GRUPAL));
                    }
                }
                
                if (saldoResultadoVO != null && (saldoResultadoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE || saldoResultadoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA)) {
                    return false;
                }
            } else if(cliente.solicitudes[i].tipoOperacion == ClientesConstants.GRUPAL && (cliente.solicitudes[i].desembolsado == ClientesConstants.SOLICITUD_RECHAZADA || cliente.solicitudes[i].ordenPago.getEstatus() != 7)){
                return false;
            } else
                return false;*/
        }
        return true;
    }

    public static double montoMaximoSolicitud(ClienteVO cliente) {
        double montoMaximo = 0.0;
        for (int i = 0; i < cliente.solicitudes.length; i++) {
            if (cliente.solicitudes[i].tipoOperacion == ClientesConstants.MICROCREDITO && cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO && cliente.solicitudes[i].decisionComite != null) {
                if (cliente.solicitudes[i].decisionComite.montoAutorizado > montoMaximo) {
                    montoMaximo = cliente.solicitudes[i].decisionComite.montoAutorizado;
                }
            }
        }
        montoMaximo = (montoMaximo * .30) + montoMaximo;
        montoMaximo = FormatUtil.roundDouble(montoMaximo, 2);
        return montoMaximo;
    }

    public static boolean regenaracionValida(SolicitudVO solicitud) throws Exception {

        int diferencia = 0;
        if (solicitud.amortizacion != null) {
            diferencia = FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date());
            if (solicitud.estatus != ClientesConstants.SOLICITUD_AUTORIZADA) {
                return false;
            }
            if (diferencia < 1 || diferencia > 30) {
                return false;
            }
            if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO) {
                return false;
            }
            return true;
        }
        return false;

    }

    public static boolean cumpleLimites(SolicitudVO solicitud, DecisionComiteVO decision, Vector<Notification> notificaciones, TreeMap catComisiones, TreeMap catTasas) throws ClientesException {
        boolean resultado = false;
        LimiteCreditoVO limites = solicitud.limites;
        if (limites == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se cuenta con la determinación de comité central"));
        } else {
            //double montoSinComision = ClientesUtil.calculaMontoSinComision(decision.montoAutorizado, decision.comision, catComisiones);
            double montoSinComision = decision.montoSinComision;
            TasaInteresVO tasaLimite = (TasaInteresVO) catTasas.get(new Integer(solicitud.limites.tasa));
            TasaInteresVO tasaSeleccionada = (TasaInteresVO) catTasas.get(new Integer(decision.tasa));
            ComisionVO comisionLimite = (ComisionVO) catComisiones.get(new Integer(solicitud.limites.comision));
            ComisionVO comisionSeleccionada = (ComisionVO) catComisiones.get(new Integer(decision.comision));
            if (montoSinComision <= limites.monto && tasaSeleccionada.valor >= tasaLimite.valor && comisionSeleccionada.porcentaje >= comisionLimite.porcentaje) {
                resultado = true;
            } else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La tasa, comisión o monto no cumlen con lo determinado por comité central"));
            }
        }
        return resultado;
    }

    public static DecisionComiteVO autorizaSolicitud(Connection conn, IntegranteCicloVO integrante, int numeroSolicitud, int tasa, boolean restructura, int plazo) throws ClientesException {
        DecisionComiteVO decisionComite = new DecisionComiteVO();
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones((restructura ? ClientesConstants.REESTRUCTURA_GRUPAL : ClientesConstants.GRUPAL));
        if (integrante.idCliente != 0 && numeroSolicitud != 0) {
            decisionComite.idCliente = integrante.idCliente;
            decisionComite.idSolicitud = numeroSolicitud;
            decisionComite.fechaRealizacion = new java.sql.Date(System.currentTimeMillis());
            decisionComite.fechaCaptura = new Timestamp(System.currentTimeMillis());
            decisionComite.decisionComite = 1;
            decisionComite.montoSinComision = integrante.monto;
            decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
            decisionComite.montoRefinanciado = integrante.montoRefinanciado;
            decisionComite.plazoAutorizado = (restructura ? plazo : 16);
            decisionComite.tasa = tasa;
            decisionComite.comision = integrante.comision;
            decisionComite.frecuenciaPago = 3;
            decisionComite.detalleMotivoRechazoCliente = "";
            decisionComite.comentariosComite = "";
            if (decisionComite.montoAutorizado != 0 || decisionComite.montoAutorizado != 0.0 || decisionComite.montoAutorizado != 0.00) {
                new DecisionComiteDAO().addDecisionComite(conn, decisionComite);
            }
        }
        return decisionComite;
    }

    public static boolean procesaRenovaciones(Connection conn, IntegranteCicloVO[] integrantesSeleccionados, int tasa) throws Exception {
        return procesaRenovaciones(conn, integrantesSeleccionados, tasa, false, 0);
    }

    public static boolean procesaRenovaciones(Connection conn, IntegranteCicloVO[] integrantesSeleccionados, int tasa, boolean restructura, int plazo) throws Exception {
        SolicitudVO solicitud = new SolicitudVO();
        for (int i = 0; i < integrantesSeleccionados.length; i++) {
            if (integrantesSeleccionados[i].idSolicitud == 0) {
                if (integrantesSeleccionados[i].monto != 0 || integrantesSeleccionados[i].monto != 0.00) {
                    solicitud = SolicitudHelper.generaNuevaSolicitud(integrantesSeleccionados[i].idCliente, restructura);
                    integrantesSeleccionados[i].idSolicitud = solicitud.idSolicitud;
                    SolicitudUtil.autorizaSolicitud(conn, integrantesSeleccionados[i], solicitud.idSolicitud, tasa, restructura, plazo);
                } else {
                    return false;
                }
            } else //La solicitud ya esta creada solo se actualiza, :::COMICION:::TASA:::MONTO:::
            {
                if (integrantesSeleccionados[i].monto != 0 || integrantesSeleccionados[i].monto != 0.00) {
                    SolicitudUtil.actualizaDesicionComite(conn, integrantesSeleccionados[i], tasa, restructura, plazo);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static DecisionComiteVO actualizaDesicionComite(Connection conn, IntegranteCicloVO integrante, int tasa, boolean restructura, int plazo) throws ClientesException, SQLException {
        DecisionComiteVO decisionComite = new DecisionComiteVO();
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones((restructura ? ClientesConstants.REESTRUCTURA_GRUPAL : ClientesConstants.GRUPAL));
        if (integrante.idCliente != 0 && integrante.idSolicitud != 0) {
            decisionComite.idCliente = integrante.idCliente;
            decisionComite.idSolicitud = integrante.idSolicitud;
            decisionComite.montoSinComision = integrante.monto;
            decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
            decisionComite.montoRefinanciado = integrante.montoRefinanciado;
            decisionComite.tasa = tasa;
            decisionComite.comision = integrante.comision;
            decisionComite.plazoAutorizado = (restructura ? plazo : 16);
            if (decisionComite.montoAutorizado != 0 || decisionComite.montoAutorizado != 0.0 || decisionComite.montoAutorizado != 0.00) {
                new DecisionComiteDAO().updateMontoComisionTasa(conn, decisionComite);
                new IntegranteCicloDAO().updateRolMontoIntegrante(conn, integrante);
            }
        }
        return decisionComite;
    }

    public static SolicitudVO calculaReestructuraIndividual(SolicitudVO nuevaSolicitud, SolicitudVO[] solicitudes) throws ClientesException {

        SaldoT24DAO saldot24dao = new SaldoT24DAO();
        SaldoIBSVO saldoIBSvo = new SaldoIBSVO();
        double montoReestructura = 0.0;

        if (solicitudes != null && solicitudes.length > 0) {

            for (int i = 0; i < solicitudes.length; i++) {
                if (solicitudes[i].saldoVigente != 0) {
                    nuevaSolicitud.idSolicitud = solicitudes[i].idSolicitud;
                    nuevaSolicitud.tipoOperacion = solicitudes[i].tipoOperacion;
                    nuevaSolicitud.solicitudReestructura = nuevaSolicitud.idSolicitud;
                }
            }
            if (nuevaSolicitud.idCreditoIBS != 0 && nuevaSolicitud.idCuentaIBS != 0) {
                saldoIBSvo = new SaldoIBSDAO().getSaldo(nuevaSolicitud.idCliente, nuevaSolicitud.idSolicitud, nuevaSolicitud.tipoOperacion);
            } else {
                saldoIBSvo = IBSHelper.getSaldosT24ToIBS(saldot24dao.getSaldosT24ByNumClienteSolicitudProducto(nuevaSolicitud.idCliente, nuevaSolicitud.idSolicitud, nuevaSolicitud.tipoOperacion));
            }

            if (saldoIBSvo != null) {
                montoReestructura = saldoIBSvo.getSaldoTotalAlDia();
            }
            myLogger.debug("montoReestructura: " + montoReestructura);
            nuevaSolicitud.reestructura = 1;
            nuevaSolicitud.montoSolicitado = montoReestructura;

        }

        return nuevaSolicitud;
    }

    public static int validaSolicitudNueva(int cliente, HttpServletRequest request) {
        int respuesta = 0;
        SaldoIBSDAO saldoDao = new SaldoIBSDAO();
        try {
            SaldoIBSVO saldo = saldoDao.getSaldoIntegranteActivo(cliente);
            if (saldo == null || saldo.getIdClienteSICAP() == 0) {
                respuesta = ClientesConstants.SOLICITUD_PERMITIDA;
            } else if (saldo.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                if ((saldo.getNumeroCuotas() - saldo.getNumeroCuotasTranscurridas() <= 5) || request.isUserInRole("ANALISIS_CREDITO")) {
                    respuesta = ClientesConstants.SOLICITUD_PERMITIDA;
                } else {
                    respuesta = ClientesConstants.SOLICITUD_FUERA_RANG0;
                }
            } else {
                respuesta = ClientesConstants.SOLICITUD_MORA;
            }
        } catch (Exception e) {
            myLogger.error("getIntegranteActivo", e);
        }
        return respuesta;
    }

    public static double calculaMontoAutorizadoReestructuraIndividual(SolicitudVO solicitud) throws ClientesException {

        SaldoT24DAO saldot24dao = new SaldoT24DAO();
        SaldoIBSVO saldoIBSvo = new SaldoIBSVO();
        double montoAutorizado = 0.0;
        int idcliente = 0;
        int idsolicitud = 0;
        int producto = 0;
        if (solicitud != null) {
            idcliente = solicitud.idCliente;
            idsolicitud = solicitud.solicitudReestructura;
            producto = solicitud.tipoOperacion;
        }

        if (solicitud.idCreditoIBS != 0 && solicitud.idCuentaIBS != 0) {
            saldoIBSvo = new SaldoIBSDAO().getSaldo(idcliente, idsolicitud, producto);
        } else {
            saldoIBSvo = IBSHelper.getSaldosT24ToIBS(saldot24dao.getSaldosT24ByNumClienteSolicitudProducto(idcliente, idsolicitud, producto));
        }

        if (saldoIBSvo != null) {
            montoAutorizado = saldoIBSvo.getSaldoTotalAlDia();
        }

        return montoAutorizado;
    }

    public static boolean revisaAntSolicitud(SolicitudVO solicitud, CicloGrupalVO cicloGrupal) throws ClientesException {
        boolean modifica = false;
        Calendar cal = Calendar.getInstance();
        Calendar.getInstance();
        try {
            Date fechaHoy = cal.getTime();
            if (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && solicitud.desembolsado != ClientesConstants.CANCELADO) {
                if (FechasUtil.inBetweenDays(solicitud.fechaFirma, fechaHoy) >= 30) {
                    modifica = true;
                    if (cicloGrupal != null) {
                        if (cicloGrupal.estatus > ClientesConstants.CICLO_CERRADO && cicloGrupal.estatus < ClientesConstants.CICLO_AUTORIZADO) {
                            modifica = true;
                        } else {
                            modifica = false;
                        }
                    }
                }
            }

        } catch (Exception e) {
            myLogger.error("getIntegranteActivo", e);
        }
        return modifica;
    }

    public static boolean limpiaSolicitud(SolicitudVO solicitud, CicloGrupalVO cicloGrupal, SolicitudVO[] solicitudes) throws ClientesException {
        boolean modifica = false;
        IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
        DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
        ArchivoAsociadoDAO archivoAsociadoDAO = new ArchivoAsociadoDAO();
        CreditoDAO creditoDAO = new CreditoDAO();
        DireccionDAO direcionesDAO = new DireccionDAO();
        InformacionCrediticiaDAO informacionCrediticiaDAO = new InformacionCrediticiaDAO();
        SegurosDAO seguroDAO = new SegurosDAO();
        AseguradosDAO asegurDAO = new AseguradosDAO();
        BeneficiarioDAO benefiDAO = new BeneficiarioDAO();
        EmpleoDAO empleoDAO = new EmpleoDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        SolicitudVO solicitudVO = new SolicitudVO();
        Connection cn = null;
        Savepoint save = null;
        try {
            cn = ConnectionManager.getMySQLConnection();
            cn.setAutoCommit(false);
            solicitud.decisionComite = new DecisionComiteDAO().getDecisionComite(solicitud.idCliente, solicitud.idSolicitud);
            solicitud.archivosAsociados = new ArchivoAsociadoDAO().getArchivos(solicitud.idCliente, solicitud.idSolicitud);
            solicitud.empleo = new EmpleoDAO().getEmpleo(solicitud.idCliente, solicitud.idSolicitud);
            solicitud.seguro = new SegurosDAO().getSeguros(solicitud.idCliente, solicitud.idSolicitud);
            solicitud.circuloCredito = creditoDAO.getCredito(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.CIRCULO_CREDIT0);
            solicitud.infoCreditoCirculo = new InformacionCrediticiaDAO().getInfoCrediticia(solicitud.idCliente, solicitud.idSolicitud, 0, ClientesConstants.SOCIEDAD_CIRCULO);

            solicitud.circuloCredito = creditoDAO.getCredito(solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.CIRCULO_CREDIT0);
            if (cicloGrupal != null) {
                integranteCicloDAO.deleteIntegranteSolicitud(cn, cicloGrupal.idGrupo, cicloGrupal.idCiclo, solicitud.idCliente, solicitud.idSolicitud);
            }
            if (solicitud.decisionComite != null) {
                decisionComiteDAO.deleteDecisionComite(cn, solicitud.idCliente, solicitud.idSolicitud);
            }
            if (solicitud.archivosAsociados != null) {
                archivoAsociadoDAO.deleteArchivoAsociado(cn, solicitud.idCliente, solicitud.idSolicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
            }
            if (solicitud.infoCreditoCirculo != null) {
                informacionCrediticiaDAO.delInfoCrediticia(cn, solicitud.idCliente, solicitud.idSolicitud);
            }
            if (solicitud.circuloCredito != null) {
                creditoDAO.delInfoCrediticia(cn, solicitud.idCliente, solicitud.idSolicitud);
            }
            if (solicitud.empleo != null) {
                direcionesDAO.deleteDirEmpleo(cn, solicitud.idCliente, solicitud.idSolicitud);
                empleoDAO.deleteEmpleo(cn, solicitud.idCliente, solicitud.idSolicitud);

            }
            if (solicitud.seguro != null) {
                asegurDAO.deleteAsegurados(cn, save, solicitud.seguro);
                benefiDAO.deleteBeneficiario(cn, save, solicitud.seguro);
                seguroDAO.deleteSeguro(cn, solicitud.idCliente, solicitud.idSolicitud);
            }
            solicitudVO.estatus = 1;
            if (solicitudes.length == 1) {
                solicitudVO.estatus = 7;
            } else {
                for (int i = 0; i < solicitudes.length; i++) {
                    solicitudVO.estatus = 7;
                    if (FechasUtil.obtenAniosDiferencia(solicitudes[i].fechaFirma, new java.util.Date())< 1 && solicitudes[i].desembolsado == ClientesConstants.CICLO_DESEMBOLSADO) {
                        solicitudVO.estatus = 1;
                        break;
                    }
                }
            }
            solicitudDAO.updateLimpiaSolicitud(cn, solicitud.idCliente, solicitud.idSolicitud, solicitudVO);
            cn.commit();
        } catch (Exception e) {
            myLogger.error("limpiaSolicitud", e);
            try {
                cn.rollback();
            } catch (SQLException sqle) {
                myLogger.error("limpiaSolicitud", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle1) {
                myLogger.error("limpiaSolicitud", sqle1);
                throw new ClientesDBException(sqle1.getMessage());
            }
        }
        return modifica;
    }
    public static SolicitudVO heredaDatosDelCicloAnterior(SolicitudVO solicitudActual, SolicitudVO solicitudCicloAnterior, int idSucursal, int idEjecutivoActual) throws ClientesException {
        //solicitudActual.idEjecutivo = idEjecutivoActual;
        if(solicitudActual.idEjecutivo == 0)
        {
            //solicitudActual.idEjecutivo = solicitudCicloAnterior.idEjecutivo;
            
            //Empieza código
            EjecutivoCreditoVO ejecutivoCredito= new com.sicap.clientes.dao.EjecutivoCreditoDAO().getEjecutivo(solicitudCicloAnterior.idEjecutivo);
            if(ejecutivoCredito == null)
            {
                solicitudActual.idEjecutivo = 0;
            }
            else
            {
                if(!(ejecutivoCredito.getEstatus().equals("A")))
                {
                    solicitudActual.idEjecutivo = 0;
                }
                else
                {
                    if(!(ejecutivoCredito.tipoEjecutivo == 1 || ejecutivoCredito.tipoEjecutivo == 2))
                    {
                        solicitudActual.idEjecutivo = 0;
                    }
                    else
                    {
                       solicitudActual.idEjecutivo = solicitudCicloAnterior.idEjecutivo;
                    }
                }
            }
            //Termina código
        }
        else
        {
            EjecutivoCreditoVO ejecutivoCredito= new com.sicap.clientes.dao.EjecutivoCreditoDAO().getEjecutivo(solicitudActual.idEjecutivo);
            if(ejecutivoCredito == null)
            {
                solicitudActual.idEjecutivo = 0;
            }
            else
            {
                if(!(ejecutivoCredito.getEstatus().equals("A")))
                {
                    solicitudActual.idEjecutivo = 0;
                }
                else
                {
                    if(!(ejecutivoCredito.tipoEjecutivo == 1 || ejecutivoCredito.tipoEjecutivo == 2))
                    {
                        solicitudActual.idEjecutivo = 0;
                    }
                    else
                    {
                       solicitudActual.idEjecutivo = solicitudCicloAnterior.idEjecutivo;
                    }
                }
            }
        }
        if(solicitudActual.idGrupo == 0)
        {
            solicitudActual.idGrupo = solicitudCicloAnterior.idGrupo;
        }
        if(solicitudActual.rolHogar == 0)
        {
            solicitudActual.rolHogar = solicitudCicloAnterior.rolHogar;
        }
        /*if(solicitudActual.rolHogar == 0)
        {
            solicitudActual.rolHogar = solicitudCicloAnterior.rolHogar;
        }*/
        if(solicitudActual.medio == 0)
        {
            solicitudActual.medio = solicitudCicloAnterior.medio;
        }
        if(solicitudActual.destinoCredito == 0)
        {
            solicitudActual.destinoCredito = solicitudCicloAnterior.destinoCredito;
        }
        if(solicitudActual.otroCredito == 0)
        {
            solicitudActual.otroCredito = solicitudCicloAnterior.otroCredito;
        }
        if(solicitudActual.mejorIngreso == 0)
        {
            solicitudActual.mejorIngreso = solicitudCicloAnterior.mejorIngreso;
        }
        if(solicitudActual.idProgProspera != null)
        {
            if(solicitudActual.idProgProspera.equals(""))
            {
                solicitudActual.idProgProspera = solicitudCicloAnterior.idProgProspera;
            }
         }
        
            return solicitudActual;
       }
    
       public static EmpleoVO heredaDatosEmpleoDelCicloAnterior(int idCliente, int idUltimaSolicitud) throws ClientesException {
        
        
        EmpleoVO EmpleoResultante = new EmpleoVO();
        EmpleoVO EmpleoAnterior = new EmpleoDAO().getEmpleo(idCliente, idUltimaSolicitud);

        if(EmpleoAnterior == null){
            myLogger.debug("Sin registro de empleo anterior...");
            return null;
        }
        
        EmpleoResultante.direccion = new com.sicap.clientes.dao.DireccionDAO().getDireccion(idCliente, idUltimaSolicitud, "d_empleos", 1);
        
        if(EmpleoResultante.direccion == null){
            myLogger.debug("Sin registro de direccion de empleo anterior...");
            return null;
        }
        
        if(EmpleoResultante.razonSocial == null)
        {
            EmpleoResultante.razonSocial = EmpleoAnterior.razonSocial;
            
        }
        if(EmpleoResultante.tipoSector == 0)
        {
            EmpleoResultante.tipoSector = EmpleoAnterior.tipoSector;
        }
        if(EmpleoResultante.ubicacionNegocio == 0)
        {
            EmpleoResultante.ubicacionNegocio = EmpleoAnterior.ubicacionNegocio;
        }
       
        if (EmpleoResultante != null && EmpleoAnterior.direccion != null)
         {
            if(EmpleoResultante.direccion.estado == null)
            {
                EmpleoResultante.direccion.estado = EmpleoAnterior.direccion.estado;
            }   
            if(EmpleoResultante.direccion.municipio == null)
            {
                EmpleoResultante.direccion.municipio = EmpleoAnterior.direccion.municipio;
            }  
            if(EmpleoResultante.direccion.colonia == null)
            {
                EmpleoResultante.direccion.colonia = EmpleoAnterior.direccion.colonia;
            }  
            if(EmpleoResultante.direccion.cp == null)
            {
                EmpleoResultante.direccion.cp = EmpleoAnterior.direccion.cp;
            } 

            if(EmpleoResultante.direccion.calle == null)
            {
                EmpleoResultante.direccion.calle = EmpleoAnterior.direccion.calle;
            } 

            if(EmpleoResultante.direccion.numeroExterior == null)
            {
                EmpleoResultante.direccion.numeroExterior = EmpleoAnterior.direccion.numeroExterior;
            } 

            if(EmpleoResultante.direccion.numeroInterior == null)
            {
                EmpleoResultante.direccion.numeroInterior = EmpleoAnterior.direccion.numeroInterior;
            } 
        }
        if(EmpleoResultante.telefono == null)
        {
            EmpleoResultante.telefono = EmpleoAnterior.telefono;
        } 
        if(EmpleoResultante.referencia == null)
        {
            EmpleoResultante.referencia = EmpleoAnterior.referencia;
        } 
        if(EmpleoResultante.antEmpleo == 0)
        {
            EmpleoResultante.antEmpleo = EmpleoAnterior.antEmpleo;
        } 
        if(EmpleoResultante.fechaInicioNeg == null)
        {
            EmpleoResultante.fechaInicioNeg = EmpleoAnterior.fechaInicioNeg;
        } 
        if(EmpleoResultante.sueldoMensual == 0)
        {
            EmpleoResultante.sueldoMensual = EmpleoAnterior.sueldoMensual;
        } 
         if(EmpleoResultante.numeroEmpleados == 0)
        {
            EmpleoResultante.numeroEmpleados = EmpleoAnterior.numeroEmpleados;
        } 
         
        EmpleoResultante.estatus = ClientesConstants.ESTATUS_CAPTURADO;
        return EmpleoResultante;
    }
   
   public static SegurosVO heredaDatosSeguroDelCicloAnterior(SegurosVO SeguroActual, int idCliente, int idSolicitud, int idSolicitudAnterior) throws ClientesException 
   {
        
        
        SegurosVO SeguroResultante = new SegurosVO();
        SegurosVO SeguroCicloAnterior = new SegurosDAO().getSeguros(idCliente, idSolicitudAnterior);
       
        if(SeguroCicloAnterior != null)
        {
           
           if(SeguroActual == null)
           {
                SeguroResultante.asegurados = new AseguradosDAO().getAsegurados(idCliente, idSolicitudAnterior);
                SeguroResultante.beneficiarios= new BeneficiarioDAO().getBeneficiarios(idCliente, idSolicitudAnterior);
                SeguroResultante.sumaAsegurada = 4; 
                SeguroResultante.modulos = 1; 
                SeguroResultante.contratacion = 1;
                
           }
           else
           {    
               SeguroResultante = SeguroActual;
               if(SeguroActual.asegurados == null )
                {
                    SeguroResultante.asegurados= new AseguradosDAO().getAsegurados(idCliente, idSolicitudAnterior);
                }
                if(SeguroActual.beneficiarios == null )
                {
                    SeguroResultante.beneficiarios= new BeneficiarioDAO().getBeneficiarios(idCliente, idSolicitud);
                }


                if(SeguroActual.sumaAsegurada == 0)
                {
                   SeguroResultante.sumaAsegurada = 4; 
                }

                if(SeguroActual.modulos == 0)
                {
                   SeguroResultante.modulos = 1; 
                }
                if(SeguroActual.contratacion == 0)
                {
                   SeguroResultante.contratacion = 1; 
                }
            }
        }
 
        return SeguroResultante;
    }
    public static boolean HayDiferenciaEntreDirecciones(DireccionVO direccionActual, DireccionVO direccionAnterior) 
                throws ClientesException 
   {
       if(  !(new String(direccionActual.estado == null ? "":  direccionActual.estado)).equals((new String(direccionAnterior.estado == null ? "":  direccionAnterior.estado))) ||
            !(new String(direccionActual.municipio == null ? "" :  direccionActual.municipio)).equals((new String(direccionAnterior.municipio == null ? "" :  direccionAnterior.municipio)))  ||   
            !(new String(direccionActual.colonia == null ? "" :  direccionActual.colonia)).equals((new String(direccionAnterior.colonia == null ? "" :  direccionAnterior.colonia))) ||   
            !(new String(direccionActual.cp == null ? "":  direccionActual.cp)).equals((new String(direccionAnterior.cp == null ? "":  direccionAnterior.cp))) ||                   
            direccionActual.idLocalidad != direccionAnterior.idLocalidad ||
            direccionActual.tipoAsentamiento != direccionAnterior.tipoAsentamiento || 
            direccionActual.tipoVialidad != direccionAnterior.tipoVialidad ||
            !(new String(direccionActual.calle == null ? "":  direccionActual.calle)).equals((new String(direccionAnterior.calle == null ? "":  direccionAnterior.calle))) || 
            !(new String(direccionActual.numeroExterior == null ? "":  direccionActual.numeroExterior)).equals((new String(direccionAnterior.numeroExterior == null ? "":  direccionAnterior.numeroExterior))) ||
            !(new String(direccionActual.numeroInterior == null ? "":  direccionActual.numeroInterior)).equals((new String(direccionAnterior.numeroInterior == null ? "":  direccionAnterior.numeroInterior)))  
            )
       {
           return true;
       }
       return false;
       
   }
  public static boolean HayDiferenciaEntreDireccionesBasico(DireccionVO direccionActual, DireccionVO direccionAnterior) 
                throws ClientesException 
   {
       if(  !(new String(direccionActual.estado == null ? "":  direccionActual.estado)).equals((new String(direccionAnterior.estado == null ? "":  direccionAnterior.estado))) ||
            !(new String(direccionActual.municipio == null ? "" :  direccionActual.municipio)).equals((new String(direccionAnterior.municipio == null ? "" :  direccionAnterior.municipio)))  ||   
            !(new String(direccionActual.colonia == null ? "" :  direccionActual.colonia)).equals((new String(direccionAnterior.colonia == null ? "" :  direccionAnterior.colonia))) ||   
            !(new String(direccionActual.cp == null ? "":  direccionActual.cp)).equals((new String(direccionAnterior.cp == null ? "":  direccionAnterior.cp))) ||                   
            !(new String(direccionActual.calle == null ? "":  direccionActual.calle)).equals((new String(direccionAnterior.calle == null ? "":  direccionAnterior.calle))) || 
            !(new String(direccionActual.numeroExterior == null ? "":  direccionActual.numeroExterior)).equals((new String(direccionAnterior.numeroExterior == null ? "":  direccionAnterior.numeroExterior))) ||
            !(new String(direccionActual.numeroInterior == null ? "":  direccionActual.numeroInterior)).equals((new String(direccionAnterior.numeroInterior == null ? "":  direccionAnterior.numeroInterior)))  
            )
       {
           return true;
       }
       return false;
       
   }
   public static boolean HayDiferenciaEntreNombresYFN(ClienteVO cliente, String nombreAnterior, String aPaternoAnterior, String aMaternoAnterior, String fechaNacimientoAnterior, int entidadNacAnterior, int sexoAnterior) 
                throws ClientesException 
   {
      String fechaNacimientoActual = "";
      try
      {
            fechaNacimientoActual = Convertidor.dateToString(cliente.fechaNacimiento);
      }catch(Exception e){}
      
        System.out.println(cliente.nombre+"-"+nombreAnterior);
        System.out.println(cliente.aPaterno+"-"+aPaternoAnterior);
        System.out.println(cliente.aMaterno+"-"+aMaternoAnterior);
        System.out.println(fechaNacimientoActual+"-"+fechaNacimientoAnterior);
        System.out.println(cliente.entidadNacimiento+"-"+entidadNacAnterior);
        System.out.println(cliente.sexo+"-"+sexoAnterior);
        
      //String fechaNacimientoActual =  Convertidor.dateToString(cliente.fechaNacimiento);
      if(  !(new String(cliente.nombre == null ? "" :  cliente.nombre)).equals((new String(nombreAnterior == null ? "" :  nombreAnterior))) ||
           !(new String(cliente.aPaterno == null ? "" :  cliente.aPaterno)).equals((new String(aPaternoAnterior == null ? "" :  aPaternoAnterior)))  || 
           !(new String(cliente.aMaterno == null ? "" :  cliente.aMaterno)).equals((new String(aMaternoAnterior == null ? "" :  aMaternoAnterior)))  ||
           !(new String(fechaNacimientoActual == null ? "" : fechaNacimientoActual )).equals((new String(fechaNacimientoAnterior == null ? "" :  fechaNacimientoAnterior))) ||
           !(cliente.entidadNacimiento == entidadNacAnterior) ||
           !(cliente.sexo == sexoAnterior)
          )
       {
           System.out.println("TREU NAME");
           return true;
       }
       return false;
       
   }
   
   public static boolean HayDiferenciaEntreDatosActEco(EmpleoVO empleo, String razonSocialAnterior, int tipoSectorAnterior, int ubicacionNegocioAnterior) 
                throws ClientesException 
   {
    
 
     if(  !(new String(empleo.razonSocial == null ? "" :  empleo.razonSocial)).equals((new String(razonSocialAnterior == null ? "" :  razonSocialAnterior))) ||
           empleo.tipoSector != tipoSectorAnterior ||
           empleo.ubicacionNegocio != ubicacionNegocioAnterior
          )
       {
           return true;
       }
       return false;
       
   }
    
   public static java.sql.Date DeterminaFechaUltimoCambioEnDocumentacion(ClienteVO cliente)
   {
    
        SolicitudVO solicitudes[] = cliente.solicitudes; 
        for (int i = cliente.solicitudes.length - 1 ; i >= 0; i--) 
        {
            SolicitudVO solicitud = cliente.solicitudes[i];
            if(solicitud.documentacionCompleta == 1)
            {
                if(solicitud.fechaFirma != null)
                {
                    myLogger.info("Ultima fecha cambio en documentacion : "+solicitud.fechaFirma);
                    return(solicitud.fechaFirma);
                }
                        
            }

       }
        myLogger.info("No encontro ultima fecha de cambio en documentacion.");
        return null;
   }
   
   public static int DeterminaCicloUltimoCambioEnDocumentacion(ClienteVO cliente) 
                throws ClientesException 
   {
    
       int retorno = - 1; 
       SolicitudVO solicitudes[] = cliente.solicitudes; 
        for (int i = cliente.solicitudes.length - 1 ; i >= 0; i--) 
        {
            SolicitudVO solicitud = cliente.solicitudes[i];
            if(solicitud.documentacionCompleta == 1)
            {
                if(solicitud.fechaFirma != null)
                {
                    return(i);
                }
                        
            }

       }
        return retorno;
   }
}

package com.sicap.clientes.commands;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.CirculoDeCreditoHelper;
import com.sicap.clientes.helpers.CreatePdfCDC;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ReporteCreditoPDFHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClienteCDCSocket;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.ScoreFicoVO;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class CommandConsultaCirculo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaCirculo.class);

    public CommandConsultaCirculo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        try {
            Calendar cal = Calendar.getInstance();
            Calendar.getInstance();
            Date fechaHoy = cal.getTime();

            CirculoDeCreditoHelper circuloHelp = new CirculoDeCreditoHelper();

            String tipoConsulta = request.getParameter("buro");
            HttpSession session = request.getSession();
            String usuario = request.getRemoteUser();
            Notification notificaciones[] = new Notification[1];
            ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
            InformacionCrediticiaVO infoCredito = new InformacionCrediticiaVO();
            ScoreFicoVO scoreFicoVO = new ScoreFicoVO();
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

            String respuestaCDC = "";
            String cadenaConsulta = null;
            String persona = (String) request.getParameter("persona");
            Object object = null;
            ByteArrayOutputStream baosPDF = null;
            boolean isCliente = persona.equals("cliente") ? true : false;

            int idObligado = 0;
            idObligado = HTMLHelper.getParameterInt(request, "idObligado");
            request.setAttribute("persona", persona);
            request.setAttribute("idSolicitud", idSolicitud);
            request.setAttribute("idObligado", idObligado);
            //Si existe la consulta en BD no llama al servicio de consulta de Buró
            if (tipoConsulta.equalsIgnoreCase("true")) {
                //Actualiza la información
                if (persona.equals("cliente")) {
                    respuestaCDC = cliente.solicitudes[indiceSolicitud].infoCreditoCirculo.respuesta;
                }
                if (persona.equals("obligado")) {
                    respuestaCDC = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo.respuesta;
                }

                if (respuestaCDC != null) {
                    myLogger.debug("respuestaCDC_1: "+respuestaCDC);
                    object = circuloHelp.buildXMLToObject(respuestaCDC);
                    myLogger.debug("respuestaCDC_2: "+respuestaCDC);
					//CreatePdfCDC pdf = new CreatePdfCDC( (ReporteCirculoVO)object );
                    //baosPDF = (ByteArrayOutputStream)pdf.createPDF();
                    ReporteCreditoPDFHelper pdf = new ReporteCreditoPDFHelper();
                    baosPDF = (ByteArrayOutputStream) pdf.doReporteCredito((ReporteCirculoVO) object);
                }
            } else {
                cadenaConsulta = circuloHelp.buildObjectToXML(cliente, idSolicitud, isCliente, idObligado);
                respuestaCDC = ClienteCDCSocket.getReporteCDC(cadenaConsulta);

                if (respuestaCDC == null) {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Imposible establecer comunicacion con Circulo");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    cliente.solicitudes[indiceSolicitud].infoCreditoCirculo = null;
                    cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo = null;
                    return siguiente;
                }
                object = circuloHelp.buildXMLToObject(respuestaCDC);

                if (object instanceof ReporteCirculoVO) {
                    InformacionCrediticiaDAO infoCreditoDAO = new InformacionCrediticiaDAO();
                    /*CreatePdfCDC pdf = new CreatePdfCDC( (ReporteCirculoVO)object );
                     baosPDF = (ByteArrayOutputStream)pdf.createPDF();*/
                    ReporteCreditoPDFHelper pdf = new ReporteCreditoPDFHelper();
                    baosPDF = (ByteArrayOutputStream) pdf.doReporteCredito((ReporteCirculoVO) object);
                    CreditoVO infoCalificaCredito = new CreditoVO();
                    Connection conn = null;
                    CreditoDAO dao = new CreditoDAO();

                    if (isCliente) {
                        if (cliente.solicitudes[indiceSolicitud].infoCreditoCirculo == null) {
                            infoCredito = new InformacionCrediticiaVO();
                            infoCredito.idCliente = cliente.idCliente;
                            infoCredito.idSolicitud = idSolicitud;
                            infoCredito.idObligado = 0;
                            infoCredito.idSociedad = ClientesConstants.SOCIEDAD_CIRCULO;
                            infoCredito.idTipoRespuesta = 1;
                            infoCredito.respuesta = respuestaCDC;
                            infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCredito.idProvider = 0;
                            infoCredito.usuarioConsulta = usuario;
                            infoCreditoDAO.addInfoCrediticia(infoCredito);
                            infoCalificaCredito.idCliente = cliente.idCliente;
                            infoCalificaCredito.idObligado = 0;
                            infoCalificaCredito.idSolicitud = idSolicitud;
                            infoCalificaCredito.fechaCaptura = new Timestamp(System.currentTimeMillis());
                            infoCalificaCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCalificaCredito.comportamiento = ScoringUtil.getCalificacionCirculo(infoCredito);
                            if (CatalogoHelper.esSucursalAceptaRegular(cliente.idSucursal)&&infoCalificaCredito.comportamiento==ClientesConstants.CALIFICACION_CIRCULO_MALA){                               
                                if ( ScoringUtil.esRegular(infoCredito)){
                                    infoCalificaCredito.comportamiento =ClientesConstants.CALIFICACION_CIRCULO_REGULAR;
                                    infoCalificaCredito.aceptaRegular =1;
                                }                                    
                            }
                            infoCalificaCredito.tipoCredito = ClientesConstants.SOCIEDAD_CIRCULO;
                            dao.addBuroCredito(conn, infoCalificaCredito);
                            ReporteCirculoVO infoCC = (ReporteCirculoVO) object;
                            Iterator iterator = infoCC.getScoreFico().iterator();
                            ScoreFicoVO scoreCC = null;
                            ScoreFicoVO score = new ScoreFicoVO();
                            while (iterator.hasNext()) {
                                scoreCC = (ScoreFicoVO) iterator.next();
                                score.setIdCliente(cliente.idCliente);
                                score.setIdSolicitud(idSolicitud);
                                score.setFechaCaptura(Convertidor.toSqlDate(fechaHoy));
                                score.setNombre(scoreCC.getNombre());
                                score.setCodigo(scoreCC.getCodigo());
                                score.setValor(scoreCC.getValor());
                                score.setRazon1(scoreCC.getRazon1());
                                score.setRazon2(scoreCC.getRazon2());
                                score.setRazon3(scoreCC.getRazon3());
                                score.setRazon4(scoreCC.getRazon4());
                                new ScoreCirculoCreditoDAO().setScore(score);
                            }
                            cliente.solicitudes[indiceSolicitud].circuloCredito = infoCalificaCredito;
                            cliente.solicitudes[indiceSolicitud].infoCreditoCirculo = infoCredito;
                            cliente.solicitudes[indiceSolicitud].scoreCC = score;
                            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaCirculo");
                            bitutil.registraEvento("Insertó buró de credito");
                        } else {
                            infoCredito = cliente.solicitudes[indiceSolicitud].infoCreditoCirculo;
                            infoCredito.respuesta = respuestaCDC;
                            infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCredito.usuarioConsulta = usuario;
                            infoCreditoDAO.updateInfoCrediticia(infoCredito);
                            cliente.solicitudes[indiceSolicitud].infoCreditoCirculo = infoCredito;
                            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaCirculo");
                            bitutil.registraEvento("Actualizó buró de credito");
                        }
                    } else if (persona.equals("obligado")) {
                        if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo == null) {
                            infoCredito = new InformacionCrediticiaVO();
                            infoCredito.idCliente = cliente.idCliente;
                            infoCredito.idSolicitud = idSolicitud;
                            infoCredito.idObligado = idObligado;
                            infoCredito.idSociedad = ClientesConstants.SOCIEDAD_CIRCULO;
                            infoCredito.idTipoRespuesta = 1;
                            infoCredito.respuesta = respuestaCDC;
                            infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCredito.idProvider = 0;
                            infoCredito.usuarioConsulta = usuario;
                            infoCreditoDAO.addInfoCrediticia(infoCredito);
                            infoCalificaCredito.idCliente = cliente.idCliente;
                            infoCalificaCredito.idObligado = idObligado;
                            infoCalificaCredito.idSolicitud = idSolicitud;
                            infoCalificaCredito.fechaCaptura = new Timestamp(System.currentTimeMillis());
                            infoCalificaCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCalificaCredito.comportamiento = ScoringUtil.getCalificacionCirculo(infoCredito);
                            infoCalificaCredito.tipoCredito = ClientesConstants.SOCIEDAD_CIRCULO;
                            dao.addBuroCredito(conn, infoCalificaCredito);
                            cliente.solicitudes[indiceSolicitud].circuloCredito = infoCalificaCredito;
                            cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo = infoCredito;
                            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaCirculo");
                            bitutil.registraEvento("Insertó buró de credito obligado");
                        } else {
                            infoCredito = new InformacionCrediticiaVO();
                            infoCredito = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo;
                            infoCredito.respuesta = respuestaCDC;
                            infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                            infoCredito.usuarioConsulta = usuario;
                            infoCreditoDAO.updateInfoCrediticia(infoCredito);
                            cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoCirculo = infoCredito;
                            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaCirculo");
                            bitutil.registraEvento("Actualizó buró de credito obligado");
                        }
                    }
                } else if (object instanceof Notification[]) {
                    notificaciones = (Notification[]) object;
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ocurrió un error en la consulta a Circulo de Crédito, favor de comunicar al administrador del sistema");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
            }
            session.setAttribute("CLIENTE", cliente);
            request.setAttribute("pdf", baosPDF);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;

    }
}

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.CirculoDeCreditoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClienteCDCSocket;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.ScoreFicoVO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandConsultaMasivaCC implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaMasivaCC.class);

    public CommandConsultaMasivaCC(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<IntegranteCicloVO> integrantesCC = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        ClienteVO cliente = new ClienteVO();
        CreditoVO informacionCrediticia = null;
        GrupoVO grupo = new GrupoVO();
        CatalogoVO localidad = null;
        ScoreFicoVO scoreCC = null;
        ScoreFicoVO score = null;
        InformacionCrediticiaVO infoCredito = null;
        String cadenaConsulta = null;
        String respuestaCDC = null;
        CirculoDeCreditoHelper consultaCCHelp = new CirculoDeCreditoHelper();
        Object objectResp = null;
        ClienteDAO clienteDAO = new ClienteDAO();
        DireccionDAO direccionDAO = new DireccionDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();
        InformacionCrediticiaDAO infoCreditoDAO = new InformacionCrediticiaDAO();
        CreditoDAO creditoDAO = new CreditoDAO();
        ScoreCirculoCreditoDAO scoreDAO = new ScoreCirculoCreditoDAO();
        try {
            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Consulta de Informaci&oacute;n Crediticia Realizada"));
            Calendar cal = Calendar.getInstance();
            Calendar.getInstance();
            Date fechaHoy = cal.getTime();
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            integrantesCC = new GrupoHelper().getIntegrantesConsultaMasiva(request);
            for (IntegranteCicloVO integranteVO : integrantesCC) {
                myLogger.debug("Consulta Masiva " + integranteVO.getIdCliente() + "_" + integranteVO.getIdSolicitud());
                cliente = clienteDAO.getCliente(integranteVO.getIdCliente());
                cliente.direcciones = direccionDAO.getDirecciones(integranteVO.getIdCliente());
                if (cliente.direcciones != null) {
                    localidad = catalogoDAO.getLocalidad(cliente.direcciones[0].idColonia, cliente.direcciones[0].idLocalidad);
                    if (localidad != null) {
                        cliente.direcciones[0].localidad = localidad.descripcion;
                    }
                }
                for (int i = 0; cliente.direcciones != null && i < cliente.direcciones.length; i++) {
                    cliente.direcciones[i].telefonos = telefonoDAO.getTelefonos(integranteVO.getIdCliente(), cliente.direcciones[i].idDireccion);
                }
                infoCredito = new InformacionCrediticiaDAO().getInfoCrediticia(integranteVO.getIdCliente(), integranteVO.getIdSolicitud(), 0, ClientesConstants.SOCIEDAD_CIRCULO);
                if (infoCredito != null) {
                    infoCreditoDAO.delInfoCrediticia(infoCredito);
                    creditoDAO.delCredito(infoCredito);
                    scoreDAO.deleteScore(infoCredito);
                }
                cadenaConsulta = consultaCCHelp.buildObjectToXML(cliente, integranteVO.getIdSolicitud(), true, 0);
                //respuestaCDC = ClienteCDCSocket.getReporteCDC(cadenaConsulta);
                respuestaCDC = ClienteCDCSocket.getReporteCDC(cadenaConsulta);
                if (respuestaCDC == null) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Problema de conexi&oacute;n en la consulta del cliente " + integranteVO.getIdCliente() + "."));
                } else {
                    objectResp = consultaCCHelp.buildXMLToObject(respuestaCDC);
                    infoCredito = new InformacionCrediticiaVO();
                    infoCredito.idCliente = integranteVO.getIdCliente();
                    infoCredito.idSolicitud = integranteVO.getIdSolicitud();
                    infoCredito.idObligado = 0;
                    infoCredito.idSociedad = ClientesConstants.SOCIEDAD_CIRCULO;
                    infoCredito.idTipoRespuesta = 1;
                    infoCredito.respuesta = respuestaCDC;
                    infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                    infoCredito.idProvider = 0;
                    infoCredito.usuarioConsulta = request.getRemoteUser();
                    infoCreditoDAO.addInfoCrediticia(infoCredito);

                    informacionCrediticia = new CreditoVO();
                    informacionCrediticia.idCliente = integranteVO.getIdCliente();
                    informacionCrediticia.idObligado = 0;
                    informacionCrediticia.idSolicitud = integranteVO.getIdSolicitud();
                    informacionCrediticia.fechaCaptura = new Timestamp(System.currentTimeMillis());
                    informacionCrediticia.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                    informacionCrediticia.comportamiento = ScoringUtil.getCalificacionCirculo(infoCredito);
                    informacionCrediticia.tipoCredito = ClientesConstants.SOCIEDAD_CIRCULO;
                    creditoDAO.addBuroCredito(null, informacionCrediticia);

                    ReporteCirculoVO infoCC = (ReporteCirculoVO) objectResp;
                    Iterator iterator = infoCC.getScoreFico().iterator();
                    score = new ScoreFicoVO();
                    while (iterator.hasNext()) {
                        scoreCC = (ScoreFicoVO) iterator.next();
                        score.setIdCliente(integranteVO.getIdCliente());
                        score.setIdSolicitud(integranteVO.getIdSolicitud());
                        score.setFechaCaptura(Convertidor.toSqlDate(fechaHoy));
                        score.setNombre(scoreCC.getNombre());
                        score.setCodigo(scoreCC.getCodigo());
                        score.setValor(scoreCC.getValor());
                        score.setRazon1(scoreCC.getRazon1());
                        score.setRazon2(scoreCC.getRazon2());
                        score.setRazon3(scoreCC.getRazon3());
                        score.setRazon4(scoreCC.getRazon4());
                        scoreDAO.setScore(score);
                    }
                    BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaMasivaCC");
                    bitutil.registraEvento("Insertó buró de credito");
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Consulta del cliente " + integranteVO.getIdCliente() + " con &eacute;xito."));
                }
            }
            //notificaciones.add( new Notification(ClientesConstants.INFO_TYPE, "Los clientes fueron consultados correctamente"));
            integrantesCiclo = new IntegranteCicloDAO().getIntegrantesApertura(idGrupo, idCiclo);
            for (IntegranteCicloVO integrantes : integrantesCiclo) {
                integrantes.monto = ClientesUtil.calculaMontoSinComision(integrantes.monto, integrantes.comision, catComisiones);
                integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                integrantes.comision = GrupoUtil.obtieneComisionIntegrante(integrantes, grupo);
                integrantes.monto = ClientesUtil.calculaMontoConComision(integrantes.monto, integrantes.comision, catComisiones);
                informacionCrediticia = new CreditoDAO().getCredito(integrantes.idCliente, integrantes.idSolicitud, 2);
                if (informacionCrediticia != null) {
                    integrantes.calificacion = informacionCrediticia.comportamiento;
                    integrantes.calificacionAutomatica = informacionCrediticia.comportamiento;
                    if (informacionCrediticia.calificacionMesaControl != 0) {
                        integrantes.calificacion = informacionCrediticia.calificacionMesaControl;
                    }
                    integrantes.aceptaRegular = informacionCrediticia.aceptaRegular;
//                    else
//                        integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                } else {
                    if (integrantes.getIdSolicitud() == 1) {
                        integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                    } else {
                        integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                    }
                }
                myLogger.debug("Obteniendo Score");
                integrantes.scoreFico = new ScoreCirculoCreditoDAO().getValorScore(integrantes.idCliente);
            }
            request.setAttribute("INTEGRANTES_CICLO", integrantesCiclo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}

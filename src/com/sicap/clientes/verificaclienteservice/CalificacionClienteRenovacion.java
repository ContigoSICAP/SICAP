package com.sicap.clientes.verificaclienteservice;

import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import java.sql.Date;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
@WebService(serviceName = "CalificacionClienteRenovacion")
public class CalificacionClienteRenovacion {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    private static Logger myLogger = Logger.getLogger(CalificacionClienteRenovacion.class);
    
    /**
     * Metodo Calificacion Cliente de Renovacion
     *
     * @param usuario
     * @param password
     * @param idSicap
     * @return
     */
    
    @WebMethod(operationName = "calificacionRenovacion")
    public VerificaCalificacionCliente calificacionRenovacion(
            @XmlElement(required = true) @WebParam(name = "usuario") String usuario,
            @XmlElement(required = true) @WebParam(name = "password") String password,
            @XmlElement(required = true) @WebParam(name = "stIDSicap") String stIDSicap
    ) {
        VerificaCalificacionCliente resp = new VerificaCalificacionCliente();
        
        try {
            if (identifica(usuario, password)) {
                if(!stIDSicap.equals("")){
                    int idCliente = Integer.valueOf(stIDSicap);
                    Date fechaUltCred = null;
                    Date fechaHoy = Convertidor.toSqlDate(new java.util.Date());
                    IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                    if(idCliente != 0){
                        fechaUltCred = integranteCicloDAO.getTipoIntegrante(idCliente);
                        InformacionCrediticiaVO conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(idCliente);
                        if(fechaUltCred != null){
                            myLogger.debug("Cliente de Renovacion");
                            if(FechasUtil.inBetweenDays(fechaUltCred, fechaHoy) <= 365){
                                myLogger.debug("Ultimo Credito Menor a 365 dias, "+fechaUltCred);
                                if(conusltaCrediticia != null)
                                    resp = new VerificaCalificacionCliente(conusltaCrediticia.fechaConsulta, conusltaCrediticia.respuesta, ClientesConstants.CALIFICACION_CIRCULO_BUENA);
                                else
                                    resp = new VerificaCalificacionCliente(null, null, ClientesConstants.CALIFICACION_CIRCULO_BUENA);
                                resp.setRespuesta("Cliente Renovacion Encontrado, Consulta Vigente, "+tipoRespuesta(resp.getCalificacion()));
                            } else if(conusltaCrediticia != null && FechasUtil.inBetweenDays(conusltaCrediticia.fechaConsulta, fechaHoy) <= 30){
                                myLogger.debug("Calificacion Vigente");
                                resp = new VerificaCalificacionCliente(conusltaCrediticia.fechaConsulta, conusltaCrediticia.respuesta, obtenCalificacion(idCliente, conusltaCrediticia.idSolicitud, conusltaCrediticia));
                                resp.setRespuesta("Cliente Nuevo Encontrado, Consulta Vigente, "+tipoRespuesta(resp.getCalificacion()));
                            } else {
                                resp = new VerificaCalificacionCliente(null, "", 0);
                                resp.setRespuesta("Cliente Renovacion Encontrado, Generar Consulta");
                            }
                        } else {
                            myLogger.debug("Cliente de Nuevo");
                            if(conusltaCrediticia != null && FechasUtil.inBetweenDays(conusltaCrediticia.fechaConsulta, fechaHoy) <= 30){
                                myLogger.debug("Calificacion Vigente");
                                resp = new VerificaCalificacionCliente(conusltaCrediticia.fechaConsulta, conusltaCrediticia.respuesta, obtenCalificacion(idCliente, conusltaCrediticia.idSolicitud, conusltaCrediticia));
                                resp.setRespuesta("Cliente Nuevo Encontrado, Consulta Vigente, "+tipoRespuesta(resp.getCalificacion()));
                            } else {
                                resp = new VerificaCalificacionCliente(null, "", 0);
                                resp.setRespuesta("Cliente Nuevo Encontrado, Generar Consulta");
                            }
                        }
                    } else {
                        resp.setRespuesta("Cliente Enviado en 0");
                    }
                } else {
                    resp.setRespuesta("Faltan datos para la consulta!!");
                }
            } else {
                resp.setRespuesta("Credencial no valida");
            }
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("consulta", e);
            resp.setRespuesta("Error en Servicio");
        } finally {
            resp.setTimeStamp(new java.util.Date());
            resp.toString();
            myLogger.info("Enviando respuesta");
        }
        return resp;
    }
    
    private static boolean identifica(String usuario, String pass) {
        boolean entra = false;
        if (usuario.equals(ClientesConstants.USS_WS) && pass.equals(ClientesConstants.PSS_WS)) {
            entra = true;
        }
        return entra;
    }
    
    private static int obtenCalificacion(int idCliente, int idSolicitud, InformacionCrediticiaVO conusltaCrediticia) throws ClientesException{
        
        int calificacion = 0;
        CreditoVO informacionCrediticia = new CreditoDAO().getCredito(idCliente, idSolicitud, 2);
        if (informacionCrediticia != null) {
            if (informacionCrediticia.calificacionMesaControl != 0) {
                calificacion = informacionCrediticia.calificacionMesaControl;
                myLogger.debug("Calificacion de Mesa de Control");
            } else if (conusltaCrediticia != null) {
                calificacion = informacionCrediticia.comportamiento;
                myLogger.debug("Calificacion de Circulo de Credito");
            } else {
                calificacion = -1;
                myLogger.debug("Sin Consulta de Circulo de Credito");
            }
        } else {
            calificacion = -2;
            myLogger.debug("Sin Calificacion de Credito");
        }
        return (calificacion==4)?1:calificacion;
    }
    
    private static String tipoRespuesta(int calificacion){
        
        String resp = "";
        switch(calificacion){
            case -2:
                resp = "Sin Calificacion de Credito";
                break;
            case -1:
                resp = "Sin Consulta de Circulo de Credito";
                break;
            case 0:
                resp = "Sin Calificacion";
                break;
            default:
                resp = "Calificacion";
                break;
        }
        return resp;
    }
}

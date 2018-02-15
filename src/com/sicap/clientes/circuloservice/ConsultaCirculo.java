/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.circuloservice;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.ColoniaDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.dao.UsuarioMovilDAO;
import com.sicap.clientes.helpers.ArchivosAsociadosHelper;
import com.sicap.clientes.helpers.CirculoDeCreditoHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClienteCDCSocket;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.ScoreFicoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TelefonoVO;
import com.sicap.clientes.vo.UsuarioMovilVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author jmerino
 */
@WebService(serviceName = "ConsultaCirculo")
public class ConsultaCirculo {

    private static Logger myLogger = Logger.getLogger(ConsultaCirculo.class);

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello .." + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(action = "consultaAction", operationName = "consulta")
    public ConsultaCirculoServiceResp consulta(
            @XmlElement(required = true) @WebParam(name = "usuarioSicap") String usuarioSicap,
            @XmlElement(required = true) @WebParam(name = "tipoUsuario") String tipoUsuario,
            @XmlElement(required = true) @WebParam(name = "idAsesor") Integer idAsesor,
            @XmlElement(required = true) @WebParam(name = "nombre") String nombre,
            @XmlElement(required = true) @WebParam(name = "aPaterno") String aPaterno,
            @XmlElement(required = true) @WebParam(name = "aMaterno") String aMaterno,
            @XmlElement(required = true) @WebParam(name = "fechaNacimiento") java.util.Date fechaNacimiento,
            @XmlElement(required = true) @WebParam(name = "cp") String cp,
            @XmlElement(required = true) @WebParam(name = "codAsentamiento") String codAsentamiento,
            @XmlElement(required = true) @WebParam(name = "calle") String calle,
            @XmlElement(required = true) @WebParam(name = "numExterior") String numExterior,
            @WebParam(name = "numInterior") String numInterior,
            @WebParam(name = "telCasa") String telCasa,
            @WebParam(name = "telCelular") String telCelular,
            @XmlElement(required = true) @WebParam(name = "autorizacion") String autorizacion) {
        //TODO write your implementation code here:

        myLogger.debug("usuarioSicap: " + usuarioSicap);
        myLogger.debug("tipoUsuario: " + tipoUsuario);
        myLogger.debug("idAsesor: " + idAsesor);
        myLogger.debug("nombre: " + nombre);
        myLogger.debug("aPaterno: " + aPaterno);
        myLogger.debug("aMaterno: " + aMaterno);
        myLogger.debug("fechaNacimiento: " + fechaNacimiento);
        myLogger.debug("cp: " + cp);
        myLogger.debug("codAsentamiento: " + codAsentamiento);
        myLogger.debug("calle: " + calle);
        myLogger.debug("numExterior: " + numExterior);
        myLogger.debug("numInterior: " + numInterior);
        myLogger.debug("telCasa: " + telCasa);
        myLogger.debug("telCelular: " + telCelular);

        ConsultaCirculoServiceResp resp = new ConsultaCirculoServiceResp();
        ClienteDAO clientedao = new ClienteDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        SolicitudVO solicitud = new SolicitudVO();
        ClienteDAO clienteDao = new ClienteDAO();
        EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
        ArchivoAsociadoVO archivo = new ArchivoAsociadoVO();
        String nombreArchivo = null;
        String ruta = null;
        String cadenaConsulta = null;
        String respuestaCDC = null;
        Object respuestaObject = null;
        CirculoDeCreditoHelper circuloHelp = new CirculoDeCreditoHelper();
        Calendar cal = Calendar.getInstance();
        Calendar.getInstance();
        Date fechaHoy = cal.getTime();

        try {
            //Validar si el cliente existe
            String fechaNac = Convertidor.dateToString(fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU);

            //Validar edad del cliente
            fechaNac = fechaNac.replace("-", "");
            String RFC = RFCUtil.obtenRFC(nombre.trim().toUpperCase(), aPaterno.trim().toUpperCase(), aMaterno.trim().toUpperCase(), fechaNac);
            myLogger.info("RFC generado: " + RFC);
            ClienteVO cliente = clientedao.getClienteRFCCompleto(RFC);

            if (cliente != null) {
                myLogger.info("Cliente encontrado: " + cliente.idCliente);
                resp.setStatus("OK");
                //resp.setErrorCode("CCC007");
                resp.setNumCliente(cliente.idCliente);
                //resp.setErrorDetail("Cliente o RFC duplicado");
                resp.setComportamiento("C");
            } else {
                myLogger.info("RFC no encontrado");
                cliente = new ClienteVO();
                cliente.rfc = RFC;
                cliente.nombre = nombre;
                if (aPaterno.equals("X")) {
                    aPaterno = "";
                }
                if (aMaterno.equals("X")) {
                    aMaterno = "";
                }
                cliente.aPaterno = aPaterno;
                cliente.aMaterno = aMaterno;
                cliente.nombreCompleto = cliente.nombre + " " + cliente.aPaterno + " " + cliente.aMaterno;

                //cliente.fechaNacimiento = fechaNacimiento;
                cliente.fechaNacimiento = Convertidor.toSqlDate(fechaNacimiento);

                //PARAMETRO A AGREGAR EN SERVICIO O USAR LA SUCURSAL DEL USUARIO DE SICAP
                UsuarioMovilDAO usuariomovdao = new UsuarioMovilDAO();
                UsuarioMovilVO usuario = usuariomovdao.getUsuario(usuarioSicap);
                if (usuario == null) {
                    myLogger.info("Usuario no existe: " + usuarioSicap);
                    resp.setStatus("KO");
                    resp.setErrorCode("CCC004");
                    resp.setErrorDetail("Usuario inválido");
                } else {
                    idAsesor = usuario.getIdEjecutivo();
                    myLogger.info("El asesor que corresponde al usuario es: " + idAsesor);
                    EjecutivoCreditoVO ejecutivo = ejecutivodao.getEjecutivo(idAsesor);
                    if (ejecutivo == null) {
                        myLogger.info("Ejecutivo no existe: " + idAsesor);
                        resp.setStatus("KO");
                        resp.setErrorCode("CCC010");
                        resp.setErrorDetail("Número de asesor no válido");
                    } else {
                        cliente.idSucursal = ejecutivo.getIdSucursal();
                        cliente.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                        cliente.fechaCaptura = new Timestamp(System.currentTimeMillis());
                        cliente.origenMigracion = ClientesConstants.ORIGEN_MOVIL;
                        int idCliente = clientedao.addCliente(cliente);
                        myLogger.info("Cliente registrado: " + idCliente);
                        resp.setNumCliente(idCliente);
                        BitacoraUtil bitutil = new BitacoraUtil(idCliente, usuarioSicap, "ConsultaCirculo");
                        bitutil.registraEvento(cliente);
                        if (idCliente != 0) {
                            myLogger.info("Registrando solicitud");
                            cliente.idCliente = idCliente;
                            solicitud.idCliente = idCliente;
                            solicitud.tipoOperacion = ClientesConstants.GRUPAL;
                            solicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
                            solicitud.estatus = ClientesConstants.SOLICITUD_NUEVA;
                            solicitud.idEjecutivo = idAsesor;
                            solicitud.origenMigracion = 3;
                            int idSolicitud = solicituddao.addSolicitud(idCliente, solicitud);
                            myLogger.info("Solicitud registrada");
                            bitutil.registraEvento(solicitud);

                            DireccionVO direccion = new DireccionVO();
                            direccion.idCliente = idCliente;
                            direccion.idSolicitud = idSolicitud;
                            direccion.indiceTabla = 1;

                            direccion.idColonia = new ColoniaDAO().getIdColonia(cp, codAsentamiento);
                            if (direccion.idColonia == 0) {
                                resp.setStatus("KO");
                                resp.setErrorCode("CCC006");
                                resp.setErrorDetail("Código de asentamiento inválido");
                            } else {
                                direccion.cp = cp;
                                direccion.asentamiento_cp = codAsentamiento;
                                direccion.calle = calle;
                                direccion.numeroExterior = numExterior;
                                direccion.numeroInterior = numInterior;
                                DireccionDAO direcciondao = new DireccionDAO();
                                int idDireccion = direcciondao.addDireccion(null, idCliente, direccion);
                                cliente.direcciones = direcciondao.getDirecciones(idCliente);

                                //inserta el telefono de casa
                                if (telCasa != null && !telCasa.equals("")) {
                                    TelefonoVO telVOCasa = new TelefonoVO();
                                    telVOCasa.idCliente = idCliente;
                                    telVOCasa.idDireccion = 1;
                                    telVOCasa.numeroTelefono = telCasa;
                                    telVOCasa.tipoTelefono = ClientesConstants.TELEFONO_PRINCIPAL;
                                    new TelefonoDAO().addTelefono(null, idCliente, idDireccion, telVOCasa);
                                }

                                //inserta el telefono cel
                                if (telCelular != null && !telCelular.equals("")) {
                                    TelefonoVO telVOCel = new TelefonoVO();
                                    telVOCel.idCliente = idCliente;
                                    telVOCel.idDireccion = 1;
                                    telVOCel.numeroTelefono = telCelular;
                                    telVOCel.tipoTelefono = ClientesConstants.TELEFONO_CELULAR;
                                    new TelefonoDAO().addTelefono(null, idCliente, idDireccion, telVOCel);
                                }

                                nombreArchivo = idCliente + "_" + idSolicitud + "_" + "autorizacion.pdf";
                                ruta = ArchivosAsociadosHelper.getRutaArchivo(cliente.idCliente, idSolicitud);
                                File direcotio = new File(ruta);
                                if (!direcotio.exists()) {
                                    direcotio.mkdirs();
                                }
                                writeDecodedString(autorizacion, ruta + nombreArchivo);
                                myLogger.info("Archivo guardado");

                                //Registrar archivo en DB
                                archivo.idCliente = cliente.idCliente;
                                archivo.idSolicitud = idSolicitud;
                                archivo.tipoCliente = "I";
                                archivo.tipo = ClientesConstants.ARCHIVO_TIPO_AUTORIZACION;
                                archivo.nombre = nombreArchivo;
                                archivo.fechaCaptura = new Timestamp(System.currentTimeMillis());
                                new ArchivoAsociadoDAO().addArchivo(cliente.idCliente, idSolicitud, archivo);
                                myLogger.info("Archivo registrado en tabla: " + nombreArchivo);

                                //Consultar CC
                                //cadenaConsulta = circuloHelp.getXMLConsulta(cliente, idSolicitud, true, 0, usuarioSicap);
                                cadenaConsulta = circuloHelp.buildObjectToXML(cliente, idSolicitud, true, 0);
                                respuestaCDC = ClienteCDCSocket.getReporteCDC(cadenaConsulta);
                                if (respuestaCDC == null) {
                                    resp.setStatus("KO");
                                    resp.setErrorCode("CCC009");
                                    resp.setErrorDetail("Respuesta incorrecta de círculo de crédito");
                                } else {
                                    CreditoDAO dao = new CreditoDAO();
                                    respuestaObject = circuloHelp.buildXMLToObject(respuestaCDC);
                                    InformacionCrediticiaVO infoCredito = new InformacionCrediticiaVO();
                                    infoCredito.idCliente = cliente.idCliente;
                                    infoCredito.idSolicitud = idSolicitud;
                                    infoCredito.idObligado = 0;
                                    infoCredito.idSociedad = ClientesConstants.SOCIEDAD_CIRCULO;
                                    infoCredito.idTipoRespuesta = 1;
                                    infoCredito.respuesta = respuestaCDC;
                                    infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                                    infoCredito.idProvider = 0;
                                    infoCredito.usuarioConsulta = usuarioSicap;
                                    InformacionCrediticiaDAO infoCreditoDAO = new InformacionCrediticiaDAO();

                                    infoCreditoDAO.addInfoCrediticia(infoCredito);
                                    CreditoVO infoCalificaCredito = new CreditoVO();
                                    infoCalificaCredito.idCliente = cliente.idCliente;
                                    infoCalificaCredito.idObligado = 0;
                                    infoCalificaCredito.idSolicitud = idSolicitud;
                                    infoCalificaCredito.fechaCaptura = new Timestamp(System.currentTimeMillis());
                                    infoCalificaCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
                                    infoCalificaCredito.comportamiento = ScoringUtil.getCalificacionCirculo(infoCredito);
                                    infoCalificaCredito.tipoCredito = ClientesConstants.SOCIEDAD_CIRCULO;
                                    dao.addBuroCredito(null, infoCalificaCredito);
                                    ReporteCirculoVO infoCC = (ReporteCirculoVO) respuestaObject;
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
                                    resp.setStatus("OK");
                                    resp.setComportamiento(traduceCalificacionCC(infoCalificaCredito.comportamiento));
                                    myLogger.info("Respuesta armada");
                                }
                            }
                            //Armar respuesta
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("consulta", e);
            resp.setStatus("KO");
            resp.setErrorCode("CCC008");
            resp.setErrorDetail("Error desconocido, favor de reportarlo");
        }finally{
            resp.setTimeStamp(new java.util.Date());
            resp.toString();
            myLogger.info("Enviando respuesta");
            return resp;
        }
        

        
    }

    private void writeDecodedString(String s, String fileName) {

        byte[] decodedBytes = Base64.decodeBase64(s.getBytes());
        try {
            File file = new File(fileName);
            FileOutputStream osf = new FileOutputStream(file);
            osf.write(decodedBytes);
            osf.flush();
            osf.close();
        } catch (IOException ioe) {
            myLogger.error("writeDecodedString", ioe);
        }

    }

    private String traduceCalificacionCC(int califNum) {

        String califLetra = null;
        switch (califNum) {
            case ClientesConstants.CALIFICACION_CIRCULO_BUENA:
                califLetra = "B";
                break;
            case ClientesConstants.CALIFICACION_CIRCULO_MALA:
                califLetra = "M";
                break;
            case ClientesConstants.CALIFICACION_CIRCULO_REGULAR:
                califLetra = "R";
                break;
            case ClientesConstants.CALIFICACION_CIRCULO_NA:
                califLetra = "N";
                break;
        }
        return califLetra;

    }

}

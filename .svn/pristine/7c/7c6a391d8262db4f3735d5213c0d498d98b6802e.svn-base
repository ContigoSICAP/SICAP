/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.verificaclienteservice;

import com.sicap.clientes.circuloservice.ConsultaCirculo;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.ConyugeDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.LocalidadDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.sql.Date;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.LocalidadVO;
import com.sicap.clientes.vo.TelefonoVO;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
@WebService(serviceName = "VerificaCliente")
public class VerificaCliente {

    private static Logger myLogger = Logger.getLogger(VerificaCliente.class);

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Metodo de busca clientes
     *
     * @param usuario
     * @param password
     * @param Nombre
     * @param ApellidoP
     * @param ApellidoM
     * @param FechaNac
     * @param RFC
     * @return
     */
    @WebMethod(operationName = "buscaCliente")
    public VerificaClienteResp buscaCliente(
            @XmlElement(required = true) @WebParam(name = "usuario") String usuario,
            @XmlElement(required = true) @WebParam(name = "password") String password,
            @XmlElement(required = true) @WebParam(name = "Nombre") String Nombre,
            @XmlElement(required = true) @WebParam(name = "ApellidoP") String ApellidoP,
            @XmlElement(required = true) @WebParam(name = "ApellidoM") String ApellidoM,
            @XmlElement(required = true) @WebParam(name = "FechaNac") String FechaNac,
            @XmlElement(required = true) @WebParam(name = "RFC") String RFC
    ) {
        VerificaClienteResp resp = new VerificaClienteResp();

        ClienteDAO clientedao = new ClienteDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        SolicitudVO solicitud = new SolicitudVO();
        ClienteVO clienteVo = null;
        ClienteVO clientesVo[] = null;
        //insertar aqui verificacion de usuario y contraseña
        //Validamos que los tatos esten 
        try {
            if (identifica(usuario, password)) {
                if ((!Nombre.equals("") || (Nombre == null)) && (!FechaNac.equals("") || (FechaNac == null)) && (!RFC.equals("") || (RFC == null))) {
                    if ((!ApellidoP.equals("") || (ApellidoP == null)) || (!ApellidoM.equals("") || (ApellidoM == null))) {
                        //verifica formato del RFC
                        if (RFC.toUpperCase().matches("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}")) {
                            if (FechaNac.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}")) {
                                clienteVo = buscacliente(Nombre, ApellidoP, ApellidoM, FechaNac, RFC);
                                if (clienteVo != null) {
                                    clienteVo = getDatosCliente(clienteVo);
                                    resp = llenaRespuesta(clienteVo);
                                } else {
                                    resp.setStatus("No se encontro el cliente en sicap");
                                }
                            } else {
                                resp.setStatus("La fecha no tiene un formato valido");
                            }
                        } else {
                            resp.setStatus("RFC no tiene Formato valido");
                        }
                    } else {
                        resp.setStatus("No tiene apellidos!!");
                    }
                } else {
                    resp.setStatus("Faltan datos para la consulta!!");
                }
            } else {
                resp.setStatus("Credencial no valida");
            }
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("consulta", e);
            resp.setStatus("KO");
            resp.setErrorCode("CCC008");
            resp.setErrorDetail("Error desconocido, favor de reportarlo");
        } finally {
            resp.setTimeStamp(new java.util.Date());
            resp.toString();
            myLogger.info("Enviando respuesta");
            return resp;
        }
    }

    private ClienteVO buscacliente(String Nombre, String Apaterno, String Amaterno, String FechaNac, String RFC) {
        ClienteVO clienteVo = null;
        ClienteDAO clientedao = new ClienteDAO();
        ClienteVO clientesVo[] = null;
        String nombreSicap = "";
        String apaternoSicap = "";
        String amaternoSicap = "";
        String nombreIxaya = "";
        String apaternoIxaya = "";
        String amaternoIxaya = "";

        try {
            //Nombre.toUpperCase().replace(" ", "");
            nombreIxaya = Nombre;
            if (Apaterno.equals("")) {
                apaternoIxaya = Amaterno;
                amaternoIxaya = "";
            } else {
                apaternoIxaya = Apaterno;
                amaternoIxaya = Amaterno;
            }
            clienteVo = clientedao.getClienteRFCCompleto(RFC);
            if (clienteVo == null) {
                clientesVo = clientedao.getClientesRFC(RFC.substring(0, 10));
                if (clientesVo.length > 0) {
                    for (int i = 0; i < clientesVo.length; i++) {
                        nombreSicap = clientesVo[i].nombre;
                        if (clientesVo[i].aPaterno.equals("")) {
                            apaternoSicap = clientesVo[i].aMaterno;
                            amaternoSicap = "";
                        } else {
                            apaternoSicap = clientesVo[i].aPaterno;
                            amaternoSicap = clientesVo[i].aMaterno;;
                        }
                        if (nombreIxaya.replace(" ", "").toUpperCase().equals(nombreSicap.replace(" ", "").toUpperCase())
                                && apaternoIxaya.replace(" ", "").toUpperCase().equals(apaternoSicap.replace(" ", "").toUpperCase())
                                && amaternoIxaya.replace(" ", "").toUpperCase().equals(amaternoSicap.replace(" ", "").toUpperCase())
                                && FechaNac.equals(Convertidor.dateToString(clientesVo[i].fechaNacimiento, ClientesConstants.FORMATO_FECHA))) {
                            clienteVo = clientesVo[i];
                            break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            myLogger.error("buscacliente: ", e);
        }
        return clienteVo;
    }

    private ClienteVO getDatosCliente(ClienteVO clienteVo) {

        ClienteDAO clientedao = new ClienteDAO();
        SucursalDAO sucursaldao = new SucursalDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        DireccionDAO direcciondao = new DireccionDAO();
        TelefonoDAO telefonodao = new TelefonoDAO();
        LocalidadVO localidad = null;
        CatalogoDAO dao = new CatalogoDAO();
        LocalidadDAO localidadDao = new LocalidadDAO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        Date fechaFirma = null;
        Date fechafrimaResp = null;
        IntegranteCicloDAO integratneCicloDao = new IntegranteCicloDAO();

        int ultimaSolDes = 0;
        try {
            ultimaSolDes = integratneCicloDao.getNumUltSolicitudDes(clienteVo.idCliente);
            clienteVo = clientedao.getCliente(clienteVo.idCliente);
            clienteVo.estadoCivilFommur = clientedao.getEstadoCivilFommur(clienteVo.estadoCivil);
            clienteVo.sucursalSAP = sucursaldao.getSucursal(clienteVo.idSucursal).codigoSAP;
            clienteVo.direcciones = direcciondao.getDirecciones(clienteVo.idCliente);
            if (clienteVo.direcciones != null) {
                localidad = localidadDao.getLocalidad(clienteVo.direcciones[0].idColonia, clienteVo.direcciones[0].idLocalidad);
                if (localidad != null) {
                    clienteVo.direcciones[0].localidad = localidad.getNombreLocalidad();
                    clienteVo.direcciones[0].idLocalidad = localidad.getNumLocalidad();
                    clienteVo.direcciones[0].ambito = localidad.getAmbito();
                }
            }
            for (int i = 0; clienteVo.direcciones != null && i < clienteVo.direcciones.length; i++) {
                clienteVo.direcciones[i].telefonos = telefonodao.getTelefonos(clienteVo.idCliente, clienteVo.direcciones[i].idDireccion);
            }

            clienteVo.solicitudes = solicituddao.getSolicitudes(clienteVo.idCliente);
            for (int i = 0; i < clienteVo.solicitudes.length; i++) {
                clienteVo.solicitudes[i].decisionComite = new DecisionComiteDAO().getDecisionComite(clienteVo.idCliente, clienteVo.solicitudes[i].idSolicitud);
                clienteVo.solicitudes[i].ordenPago = new OrdenDePagoDAO().getOrdenDePago(clienteVo.idCliente, clienteVo.solicitudes[i].idSolicitud);
                ciclodao.getCicloSolicitud(clienteVo.solicitudes[i]);
                if (clienteVo.solicitudes[i].documentacionCompleta == 1) {
                    fechaFirma = clienteVo.solicitudes[i].fechaFirma;
                }
                if (clienteVo.solicitudes[i].idSolicitud == ultimaSolDes) {
                    clienteVo.setUltimaSolicitud(clienteVo.solicitudes[i]);
                    ciclodao.getCicloSolicitud(clienteVo.getUltimaSolicitud());
                    clienteVo.getUltimaSolicitud().decisionComite = clienteVo.solicitudes[i].decisionComite;
                    fechafrimaResp = clienteVo.getUltimaSolicitud().fechaFirma;
                    clienteVo.getUltimaSolicitud().fechaFirma = fechaFirma;
                }
            }
            if (clienteVo.getUltimaSolicitud().fechaFirma == null) {
                clienteVo.getUltimaSolicitud().fechaFirma = fechafrimaResp;
            }
            clienteVo.conyuge = new ConyugeDAO().getConyuge(clienteVo.idCliente);
        } catch (ClientesDBException dbe) {
            myLogger.error("getDatosCliente: ", dbe);
        } catch (Exception e) {
            myLogger.error("getDatosCliente: ", e);
        }

        return clienteVo;
    }

    private VerificaClienteResp llenaRespuesta(ClienteVO clienteVo) {
        VerificaClienteResp resp = new VerificaClienteResp();
        try {

            resp.setIdClienteSicap(clienteVo.idCliente);
            if (clienteVo.solicitudes != null) {
                resp.setNumcreditos(obtieneSolDesembolsadas(clienteVo.solicitudes));
                resp.setMontoMaximo(obtieneMontoMaximo(clienteVo.solicitudes));
                resp.setFechaUltimoMovimiento(obtieneFechaUltimoCliente(clienteVo.solicitudes));
            }
            resp.setSucursal(clienteVo.sucursalSAP);
            resp.setNombre(clienteVo.nombre);
            if (!clienteVo.aPaterno.equals("")) {
                resp.setaPaterno(clienteVo.aPaterno);
            }
            if (!clienteVo.aMaterno.equals("")) {
                resp.setaMaterno(clienteVo.aMaterno);
            }
            resp.setFechaNacimiento(Convertidor.dateToString(clienteVo.fechaNacimiento, ClientesConstants.FORMATO_FECHA));
            resp.setSexo(obtieneGenero(clienteVo.sexo));
            resp.setEntidadNacimiento(clienteVo.entidadNacimiento);
            resp.setDependientesEconomicos(clienteVo.dependientesEconomicos);
            resp.setTipoIdentificacion(clienteVo.tipoIdentificacion);
            resp.setNumeroIdentificacion(clienteVo.numeroIdentificacion);
            resp.setRfc(clienteVo.rfc);
            resp.setCorreoElectronico(clienteVo.correoElectronico);
            resp.setCurp(clienteVo.curp);
            resp.setEstadoCivil(clienteVo.estadoCivil);
            resp.setNivelEstudios(clienteVo.nivelEstudios);
            resp.setLenguaIndigena(obtineEtiquetaCatBinario(clienteVo.LenguaIndigena));
            resp.setDiscapacidad(obtineEtiquetaCatBinario(clienteVo.Discapacidad));
            resp.setUsodeInternet(obtineEtiquetaCatBinario(clienteVo.UsodeInternet));
            resp.setRedesSociales(obtineEtiquetaCatBinario(clienteVo.RedesSociales));
            if (clienteVo.ultimaSolicitud != null) {
                resp.setRolHogar(clienteVo.ultimaSolicitud.rolHogar);
                resp.setOtroCredito(obtineEtiquetaCatBinario(clienteVo.ultimaSolicitud.otroCredito));
                resp.setMejorIngreso(obtineEtiquetaCatBinario(clienteVo.ultimaSolicitud.mejorIngreso));
                resp.setTieneProgProspera(obtineRespApoyoProsepera(clienteVo.ultimaSolicitud));
                if (clienteVo.ultimaSolicitud.decisionComite != null) {
                    resp.setUltimoMonto(clienteVo.ultimaSolicitud.decisionComite.montoAutorizado);
                } else {
                    resp.setUltimoMonto(clienteVo.ultimaSolicitud.montoSolicitado);
                }
                if (resp.getTieneProgProspera().equals("SI")) {
                    resp.setIdProgProspera(clienteVo.ultimaSolicitud.idProgProspera);
                }
                if (clienteVo.ultimaSolicitud.nombreGrupo != null) {
                    resp.setIdGrupo(clienteVo.ultimaSolicitud.idGrupo);
                    resp.setNomUltGrupo(clienteVo.ultimaSolicitud.nombreGrupo);
                    resp.setEstatusUltGrupo(clienteVo.ultimaSolicitud.estatusCiclo);
                    resp.setFechaDesembolso(Convertidor.dateToString(clienteVo.ultimaSolicitud.fechaDesembolso, ClientesConstants.FORMATO_FECHA));
                    resp.setIdCiclo(clienteVo.ultimaSolicitud.idCiclo);
                    resp.setNumCuotas(clienteVo.ultimaSolicitud.numCuotas);
                    resp.setSemTransUltGrupo(clienteVo.ultimaSolicitud.numCuotasTrans);
                    resp.setFechaActDocs(Convertidor.dateToString(clienteVo.ultimaSolicitud.fechaFirma, ClientesConstants.FORMATO_FECHA));
                }

            }
            if (clienteVo.direcciones != null) {
                resp.setTipoVialidad(clienteVo.direcciones[0].tipoVialidad);//se agrego
                resp.setCalle(clienteVo.direcciones[0].calle);
                resp.setNumeroExterior(clienteVo.direcciones[0].numeroExterior);
                resp.setNumeroInterior(clienteVo.direcciones[0].numeroInterior);
                resp.setCp(clienteVo.direcciones[0].cp);
                resp.setIdColonia(clienteVo.direcciones[0].idColoniaSepomex);
                resp.setIdLocalidad(clienteVo.direcciones[0].idLocalidad);
                resp.setTipoAsentamiento(clienteVo.direcciones[0].tipoAsentamiento);//se agrego
                resp.setNumMunicipio(clienteVo.direcciones[0].numMunicipio);
                resp.setEstado(clienteVo.direcciones[0].numestado);
                resp.setAntiguedadViv(clienteVo.direcciones[0].antDomicilio);
                resp.setSituacionViv(clienteVo.direcciones[0].situacionVivienda);
                resp.setNumeroTelefono(getTelefono(clienteVo.direcciones[0].telefonos, ClientesConstants.TELEFONO_PRINCIPAL));
                resp.setNumeroCelular(getTelefono(clienteVo.direcciones[0].telefonos, ClientesConstants.TELEFONO_CELULAR));
            }
            resp.setFechaCaptura(Convertidor.dateToString(clienteVo.fechaCaptura, ClientesConstants.FORMATO_FECHA));

        } catch (Exception e) {
            resp.setStatus("KO");
            resp.setErrorCode("CCC009");
            resp.setErrorDetail("Error al llenar la respuesta favor de verificarlo");
            myLogger.error("getDatosCliente: ", e);
        }
        return resp;
    }

    private int obtieneSolDesembolsadas(SolicitudVO[] solicitudes) {
        int numSolicitudes = 0;

        for (SolicitudVO solicitud : solicitudes) {
            if (solicitud.desembolsado == 2) {
                numSolicitudes++;
            }
        }
        return numSolicitudes;
    }

    private double obtieneMontoMaximo(SolicitudVO[] solicitudes) {
        double montoMaximo = 0;
        for (SolicitudVO solicitud : solicitudes) {
            if (solicitud.desembolsado == ClientesConstants.OP_DISPERSADA) {
                if (solicitud.decisionComite != null) {
                    if (solicitud.decisionComite.montoAutorizado > montoMaximo) {
                        montoMaximo = solicitud.decisionComite.montoAutorizado;
                    }
                } else if (solicitud.montoSolicitado > montoMaximo) {
                    montoMaximo = solicitud.montoSolicitado;
                }
            }
        }
        return montoMaximo;
    }

    private String obtieneFechaUltimoCliente(SolicitudVO[] solicitudes) {
        String fecha = "";
        try {
            fecha = Convertidor.dateToString(solicitudes[solicitudes.length - 1].fechaCaptura, ClientesConstants.FORMATO_FECHA);
        } catch (Exception e) {
            myLogger.error("getDatosCliente: ", e);
        }
        return fecha;
    }

    private String obtieneGenero(int sexo) {
        String genero = "";
        if (sexo == 1) {
            genero = "F";
        } else if (sexo == 2) {
            genero = "M";
        }
        return genero;
    }

    private String obtineEtiquetaCatBinario(int resp) {
        String binario = "SIN DATO";
        if (resp == 1) {
            binario = "SI";
        } else if (resp == 2) {
            binario = "NO";
        }
        return binario;
    }

    private String obtineRespApoyoProsepera(SolicitudVO solicitudVo) {
        String respuesta = "";
        if (solicitudVo.idProgProspera == null || solicitudVo.idProgProspera.equals("0") || solicitudVo.idProgProspera.equals("")) {
            respuesta = "NO";
        } else {
            respuesta = "SI";
        }
        return respuesta;
    }

    private static String getTelefono(TelefonoVO[] telefonos, int tipoTelefono) {
        TelefonoVO telefono = new TelefonoVO();
        for (int i = 0; telefonos != null && i < telefonos.length; i++) {
            if (telefonos[i].tipoTelefono == tipoTelefono) {
                telefono = telefonos[i];
            }
        }
        return telefono.numeroTelefono;
    }

    private static boolean identifica(String usuario, String pass) {
        boolean entra = false;
        if (usuario.equals(ClientesConstants.USS_WS) && pass.equals(ClientesConstants.PSS_WS)) {
            entra = true;
        }
        return entra;
    }

//crear metodo de verificacion de usuario y contraseña
}

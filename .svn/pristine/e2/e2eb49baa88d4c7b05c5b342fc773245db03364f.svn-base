/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.autenticacionservice;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.UsuarioMovilDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.UsuarioMovilVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import org.apache.log4j.Logger;

/**
 *
 * @author jmerino
 */
@WebService(serviceName = "Autenticacion")
public class Autenticacion {

    private static Logger myLogger = Logger.getLogger(Autenticacion.class);
    private static TreeMap catTipoEjecutivos = null;
    private static TreeMap catSucursales = null;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     *
     * @param usuario
     * @param password
     * @return
     */
    @WebMethod(action = "autenticaAction", operationName = "autentica")
    public com.sicap.clientes.autenticacionservice.AutenticaServiceResp autentica(
            @XmlElement(required = true) @WebParam(name = "usuario") String usuario,
            @XmlElement(required = true) @WebParam(name = "password") String password) {

        EjecutivoCreditoVO ejecutivo = null;
        UsuarioMovilVO usuariovo = null;
        AutenticaServiceResp resp = null;
        


        //TODO write your implementation code here:
        //AutenticaServiceResp resp = new AutenticaServiceResp();
        try {
            if ( catTipoEjecutivos==null ){
                myLogger.debug("Cargando catalogo de ejecutivos");
                catTipoEjecutivos = CatalogoHelper.getCatalogoSeleccione(ClientesConstants.CAT_TIPO_EJECUTIVOS);
            }
            if ( catSucursales==null ){
                myLogger.debug("Cargando catalogo de sucursales");
                catSucursales = CatalogoHelper.getCatalogoSucursales();
            }
            usuariovo = validaUsuarioPassword(usuario, password);
            if (usuariovo == null) {
                myLogger.debug("Usuario/pwd incorrectos para : " + usuario);
                resp = getDummyAccessDeniedResp();
            } else {
                myLogger.debug("Usuario/pwd corectos para : " + usuario);
                ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(usuariovo.getIdEjecutivo());

                if (ejecutivo == null) {
                    myLogger.debug("Ejecutivo no encontrado");
                } else {
                    myLogger.debug("ejecutivo : " + ejecutivo);
                    if (ejecutivo.estatus.equals("B")) {
                        resp = getInactiveExecutiveResponse(usuariovo, ejecutivo);
                    } else {
                        resp = getResponse(usuariovo, ejecutivo);
                    }
                }

                //resp = getDummyResp();
            }
        } catch (ClientesException ce) {
            myLogger.error("autentica", ce);
        } catch (Exception e) {
            myLogger.error("autentica", e);
        } finally {
            if (resp == null) {
                resp = getUnknownErrorResponseResp();
            }
            return resp;
        }
    }

    private static AutenticaServiceResp getResponse(UsuarioMovilVO usuario, EjecutivoCreditoVO ejecutivo) {
        AutenticaServiceResp resp = new AutenticaServiceResp();

        ArrayList<String> permisos = new ArrayList();

        permisos.add("CONSULTA_CIRCULO");
        resp.setStatus("OK");
        resp.setCodigoError(null);
        resp.setDescError(null);
        resp.setFechaHora(new Date());
        resp.setNombre(ejecutivo.nombre + " " + ejecutivo.aPaterno + " " + ejecutivo.aMaterno);
        myLogger.debug(catTipoEjecutivos.toString());
        resp.setRol((String)catTipoEjecutivos.get(   new Integer(ejecutivo.tipoEjecutivo)    ));
        resp.setNumSucursal(ejecutivo.idSucursal);
        
        resp.setSucursal(       (String)catSucursales.get(   new Integer(ejecutivo.idSucursal)   )   );
        
        //resp.setSucursal("");
        resp.setPermisos(permisos);

        return resp;
    }

    private static AutenticaServiceResp getDummyAccessDeniedResp() {
        AutenticaServiceResp resp = new AutenticaServiceResp();

        ArrayList<String> permisos = new ArrayList();

        resp.setStatus("KO");
        resp.setCodigoError("ACM001");
        resp.setDescError("Usuario o password inválidos");
        resp.setFechaHora(new Date());

        return resp;
    }

    private static AutenticaServiceResp getDummyResp() {
        AutenticaServiceResp resp = new AutenticaServiceResp();

        ArrayList<String> permisos = new ArrayList();

        permisos.add("CONSULTA_CIRCULO");
        resp.setStatus("OK");
        resp.setCodigoError(null);
        resp.setDescError(null);
        resp.setFechaHora(new Date());
        resp.setNombre("Nombre Apaterno Amaterno");
        resp.setRol("Asesor");
        resp.setNumSucursal(2);
        resp.setSucursal("Toluca");
        resp.setPermisos(null);

        return resp;
    }

    private UsuarioMovilVO validaUsuarioPassword(String usuario, String password) {
        UsuarioMovilVO usuariovo = null;
        UsuarioMovilDAO usuariodao = new UsuarioMovilDAO();

        try {
            //String passwordMD5 = SecurityUtil.toMD5(password);
            usuariovo = usuariodao.validaUsuarioPassword(usuario, password);
            if (usuariovo != null) {
                myLogger.debug("Usuario encontrado");
            }
        } catch (Exception e) {
            myLogger.error("validaUsuarioPassword", e);
        }
        return usuariovo;
    }

    private static AutenticaServiceResp getUnknownErrorResponseResp() {
        AutenticaServiceResp resp = new AutenticaServiceResp();

        ArrayList<String> permisos = new ArrayList();

        resp.setStatus("KO");
        resp.setCodigoError("ACM003");
        resp.setDescError("Error desconocido");
        resp.setFechaHora(new Date());

        return resp;
    }

    private AutenticaServiceResp getInactiveExecutiveResponse(UsuarioMovilVO usuario, EjecutivoCreditoVO ejecutivo) {
        AutenticaServiceResp resp = new AutenticaServiceResp();

        resp.setStatus("KO");
        resp.setCodigoError("ACM002");
        resp.setDescError("Usuario inactivo");
        resp.setFechaHora(new Date());

        return resp;
    }

}

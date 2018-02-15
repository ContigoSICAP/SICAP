package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.AmortPagareDAO;
import com.sicap.clientes.dao.BeneficiarioDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.LineaCreditoDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.vo.AmortizacionPagareVO;
import org.apache.log4j.Logger;
import com.sicap.clientes.vo.CatBancosSeguroVO;
import com.sicap.clientes.vo.CatCuentasSegurosVO;
import com.sicap.clientes.vo.CatParentescoVO;
import com.sicap.clientes.vo.CatRelacionVO;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.DespachosVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.LineaCreditoVO;
import com.sicap.clientes.vo.PagareVO;
import com.sicap.clientes.vo.ParametroScoringVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.SumaAseguradaVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.TipoSeguroVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.sql.Connection;
import java.util.Calendar;

public class CatalogoHelper {
    
    private static org.apache.log4j.Logger myLogger = Logger.getLogger(CatalogoHelper.class);

    public static TreeMap getEstatusSolicitud() {
        return getCatalogoEstatus(0);
    }

    public static TreeMap getCatalogoEstatus(int statusActual) {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        switch (statusActual) {
            case ClientesConstants.ESTATUS_CAPTURADO:
                cat.put(new Integer(ClientesConstants.ESTATUS_CAPTURADO), "En proceso");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            case ClientesConstants.SOLICITUD_AUTORIZADA:
                cat.put(new Integer(ClientesConstants.SOLICITUD_AUTORIZADA), "Autorizada");
                break;
            case ClientesConstants.SOLICITUD_RECHAZADA:
                cat.put(new Integer(ClientesConstants.SOLICITUD_RECHAZADA), "Rechazada");
                break;
            case ClientesConstants.SOLICITUD_EN_ANALISIS:
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            case ClientesConstants.SOLICITUD_PENDIENTE:
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            default:
                cat.put(new Integer(0), "Seleccionar...");
                cat.put(new Integer(ClientesConstants.ESTATUS_CAPTURADO), "En proceso");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                cat.put(new Integer(ClientesConstants.SOLICITUD_AUTORIZADA), "Autorizada");
                cat.put(new Integer(ClientesConstants.SOLICITUD_RECHAZADA), "Rechazada");
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_PREAPROBADA), "Procede análisis");
                cat.put(new Integer(0), "Todas");
                break;
        }
        return cat;
    }

    public static TreeMap getCatalogoEstatusDN(int statusActual) {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        switch (statusActual) {
            case ClientesConstants.ESTATUS_CAPTURADO:
                cat.put(new Integer(ClientesConstants.ESTATUS_CAPTURADO), "En proceso");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            case ClientesConstants.SOLICITUD_AUTORIZADA:
                cat.put(new Integer(ClientesConstants.SOLICITUD_AUTORIZADA), "Autorizada");
                break;
            case ClientesConstants.SOLICITUD_RECHAZADA:
                cat.put(new Integer(ClientesConstants.SOLICITUD_RECHAZADA), "Rechazada");
                break;
            case ClientesConstants.SOLICITUD_EN_ANALISIS:
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            case ClientesConstants.SOLICITUD_PENDIENTE:
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                break;
            default:
                cat.put(new Integer(0), "Seleccionar...");
                cat.put(new Integer(ClientesConstants.ESTATUS_CAPTURADO), "En proceso");
                cat.put(new Integer(ClientesConstants.SOLICITUD_EN_ANALISIS), "Análisis");
                cat.put(new Integer(ClientesConstants.SOLICITUD_AUTORIZADA), "Autorizada");
                cat.put(new Integer(ClientesConstants.SOLICITUD_RECHAZADA), "Rechazada");
                cat.put(new Integer(ClientesConstants.SOLICITUD_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.SOLICITUD_PREAPROBADA), "Procede análisis");
                cat.put(new Integer(0), "Todas");
                break;
        }
        return cat;
    }

    public static TreeMap getCatalogoDesembolso() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Sin desembolsar");
        cat.put(new Integer(2), "Desembolsado");
        cat.put(new Integer(3), "Cancelada");
        return cat;
    }

    public static TreeMap getCatalogoRolesGrupo() {
        return getCatalogoRolesGrupo(false);
    }

    public static TreeMap getCatalogoRolesGrupo(boolean otrasOpciones) {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(3), "Presidente");
        cat.put(new Integer(2), "Secretario");
        cat.put(new Integer(1), "Tesorero");
        if (otrasOpciones) {
            cat.put(new Integer(4), "Referenciado");
            cat.put(new Integer(5), "Supervisor");
        }
        return cat;
    }

    public static TreeMap getCatalogoMotivosEliminacionAlerta() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Alerta falsa");
        cat.put(new Integer(2), "No se aplico el pago");
        cat.put(new Integer(3), "El grupo esta al corriente");
        return cat;
    }

    public static TreeMap getCatalogoEstatusCredito() throws ClientesException {
        String nomCatalogo = "c_estatus_saldos";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<SaldoIBSVO> lista = dao.getCatalogoEstatusCredito(nomCatalogo);
        for (SaldoIBSVO elementos : lista) {
            cat.put(new Integer(elementos.getEstatus()), elementos.getNomEstatus());
        }
        /*TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(7), "Cartera Cedida");
        cat.put(new Integer(6), "Castigado");
        cat.put(new Integer(5), "Cancelado");
        cat.put(new Integer(4), "Vencido");
        cat.put(new Integer(3), "Liquidado");
        cat.put(new Integer(2), "Mora");
        cat.put(new Integer(1), "Vigente");*/
        return cat;
    }

    public static TreeMap getCatalogoHorasReunion() throws ClientesException {
        String nomCatalogo = "c_hora_reunion";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        cat.put(new Integer(0), "Seleccionar...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        /*cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "08:30");
        cat.put(new Integer(2), "09:00");
        cat.put(new Integer(3), "09:30");
        cat.put(new Integer(4), "10:00");
        cat.put(new Integer(5), "10:30");
        cat.put(new Integer(6), "11:00");
        cat.put(new Integer(7), "11:30");
        cat.put(new Integer(8), "12:00");
        cat.put(new Integer(9), "12:30");
        cat.put(new Integer(10), "13:00");
        cat.put(new Integer(11), "13:30");
        cat.put(new Integer(12), "14:00");
        cat.put(new Integer(13), "14:30");
        cat.put(new Integer(14), "15:00");
        cat.put(new Integer(15), "15:30");
        cat.put(new Integer(16), "16:00");
        cat.put(new Integer(17), "16:30");
        cat.put(new Integer(18), "17:00");
        cat.put(new Integer(19), "17:30");
        cat.put(new Integer(20), "18:00");
        cat.put(new Integer(21), "18:30");*/
        return cat;
    }

    public static TreeMap getCatalogoDiasReunion() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Lunes");
        cat.put(new Integer(2), "Martes");
        cat.put(new Integer(3), "Miercoles");
        cat.put(new Integer(4), "Jueves");
        cat.put(new Integer(5), "Viernes");
        cat.put(new Integer(6), "Sabado");
        cat.put(new Integer(7), "Domingo");
        return cat;
    }

    public static TreeMap getCatalogoMeses() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Enero");
        cat.put(new Integer(2), "Febrero");
        cat.put(new Integer(3), "Marzo");
        cat.put(new Integer(4), "Abril");
        cat.put(new Integer(5), "Mayo");
        cat.put(new Integer(6), "Junio");
        cat.put(new Integer(7), "Julio");
        cat.put(new Integer(8), "Agosto");
        cat.put(new Integer(9), "Septiembre");
        cat.put(new Integer(10), "Octubre");
        cat.put(new Integer(11), "Noviembre");
        cat.put(new Integer(12), "Diciembre");
        return cat;
    }

    public static TreeMap getCatalogoAños() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "2008");
        cat.put(new Integer(2), "2009");
        cat.put(new Integer(3), "2010");
        cat.put(new Integer(4), "2011");
        return cat;
    }
    
    public static TreeMap getCatalogoYear() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        Calendar cal = Calendar.getInstance();
        cat.put(new Integer(1), Integer.toString(cal.get(Calendar.YEAR)-1));
        cat.put(new Integer(2), Integer.toString(cal.get(Calendar.YEAR)));        
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusCiclo(boolean todo, int estatus) throws ClientesException {
        String nomCatalogo = "c_estatus_ciclo";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        if(todo){
            cat.put(0, "Seleccione...");
            for (CatalogoVO elementos : lista) {
                cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
            }
        } else {
            switch (estatus){
                case ClientesConstants.CICLO_AUTORIZADO:
                    cat.put(new Integer(ClientesConstants.CICLO_AUTORIZADO), lista.get(ClientesConstants.CICLO_AUTORIZADO-1).getDescripcion());
                    cat.put(new Integer(ClientesConstants.CICLO_RECHAZADO), lista.get(ClientesConstants.CICLO_RECHAZADO-1).getDescripcion());
                    break;
                case ClientesConstants.CICLO_DISPERSADO:
                    cat.put(new Integer(ClientesConstants.CICLO_DISPERSADO), lista.get(ClientesConstants.CICLO_DISPERSADO-1).getDescripcion());
                    break;
                case ClientesConstants.CICLO_CERRADO:
                    cat.put(new Integer(ClientesConstants.CICLO_CERRADO), lista.get(ClientesConstants.CICLO_CERRADO-1).getDescripcion());
                    break;
                case ClientesConstants.CICLO_PARADESEMBOLSAR:
                    cat.put(new Integer(ClientesConstants.CICLO_PARADESEMBOLSAR), lista.get(ClientesConstants.CICLO_PARADESEMBOLSAR-1).getDescripcion());
                    break;
                case ClientesConstants.CICLO_DESEMBOLSO:
                    cat.put(new Integer(ClientesConstants.CICLO_DESEMBOLSO), lista.get(ClientesConstants.CICLO_DESEMBOLSO-1).getDescripcion());
                    break;
            }
            //if(elementos.getId() == ClientesConstants.CICLO_AUTORIZADO || elementos.getId() == ClientesConstants.CICLO_ACTIVO || elementos.getId() == ClientesConstants.CICLO_CERRADO || elementos.getId() == ClientesConstants.CICLO_RECHAZADO)
                //cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;
    }
    public static TreeMap getCatalogoEstatusSolcitud(boolean todo) throws ClientesException {
        String nomCatalogo = "c_estatus_solicitud";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        for (CatalogoVO elementos : lista) {
            if(todo){
                cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
            } else {
                if(elementos.getId() == ClientesConstants.SOLICITUD_PREAPROBADA || elementos.getId() == ClientesConstants.SOLICITUD_RECHAZADA || elementos.getId() == ClientesConstants.SOLICITUD_PENDIENTE|| elementos.getId()==0)
                    cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
            }
        }
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusNuevoCiclo() throws ClientesException {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(ClientesConstants.CICLO_APERTURA), "Apertura");
        //cat.put(new Integer(ClientesConstants.CICLO_ANALISIS), "Análisis");
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusNuevoCicloVIP() throws ClientesException {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(ClientesConstants.CICLO_APERTURA), "Apertura");
        cat.put(new Integer(ClientesConstants.CICLO_ANALISIS), "Análisis");
        cat.put(new Integer(ClientesConstants.CICLO_AUTORIZADO), "Autorizado");
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusCicloApertura(int estatus) throws ClientesException {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        switch (estatus){
            case 0:
                cat.put(new Integer(ClientesConstants.CICLO_APERTURA), "Apertura");
                break;
            case ClientesConstants.CICLO_APERTURA:
                cat.put(new Integer(ClientesConstants.CICLO_APERTURA), "Apertura");
                cat.put(new Integer(ClientesConstants.CICLO_ANALISIS), "Análisis");
                break;
            case ClientesConstants.CICLO_ANALISIS:
                cat.put(new Integer(ClientesConstants.CICLO_ANALISIS), "Análisis");
                cat.put(new Integer(ClientesConstants.CICLO_ASIGNADO), "Asignado");
                break;
            case ClientesConstants.CICLO_ASIGNADO:
                cat.put(new Integer(ClientesConstants.CICLO_ASIGNADO), "Asignado");
                cat.put(new Integer(ClientesConstants.CICLO_PROCESO), "En Proceso");
                break;
            case ClientesConstants.CICLO_PROCESO:
                cat.put(new Integer(ClientesConstants.CICLO_PROCESO), "En Proceso");
                cat.put(new Integer(ClientesConstants.CICLO_AUTORIZADO), "Autorizado");
                cat.put(new Integer(ClientesConstants.CICLO_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.CICLO_RECHAZADO), "Rechazado");
                break;
            case ClientesConstants.CICLO_AUTORIZADO:
                cat.put(new Integer(ClientesConstants.CICLO_AUTORIZADO), "Autorizado");
                break;
            case ClientesConstants.CICLO_PENDIENTE:
                cat.put(new Integer(ClientesConstants.CICLO_PENDIENTE), "Pendiente");
                cat.put(new Integer(ClientesConstants.CICLO_REVALORACION), "Revaloración");
                break;
            case ClientesConstants.CICLO_RECHAZADO:
                cat.put(new Integer(ClientesConstants.CICLO_RECHAZADO), "Rechazado");
                cat.put(new Integer(ClientesConstants.CICLO_REVALORACION), "Revaloración");
                break;
            case ClientesConstants.CICLO_REVALORACION:
                cat.put(new Integer(ClientesConstants.CICLO_REVALORACION), "Revaloración");
                cat.put(new Integer(ClientesConstants.CICLO_PROCESO), "En Proceso");
                break;
        }
        /*TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(ClientesConstants.CICLO_APERTURA), "Apertura");
        cat.put(new Integer(ClientesConstants.CICLO_ANALISIS), "Análisis");
        cat.put(new Integer(ClientesConstants.CICLO_PENDIENTE), "Pendiente");
        cat.put(new Integer(ClientesConstants.CICLO_RECHAZADO), "Rechazado");
        cat.put(new Integer(ClientesConstants.CICLO_REVALORACION), "Revaloración");
        cat.put(new Integer(ClientesConstants.CICLO_ASIGNADO), "Asignado");
        cat.put(new Integer(ClientesConstants.CICLO_PROCESO), "En Proceso");
        cat.put(new Integer(ClientesConstants.CICLO_AUTORIZADO), "Autorizado");*/
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusCicloCombo() throws ClientesException {
        String nomCatalogo = "c_estatus_ciclo";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        cat.put(0, "Seleccione...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;
    }
    
    /*public static TreeMap getCatalogoEstatusInterciclo() throws ClientesException {
        String nomCatalogo = "c_estatus_ciclo";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        cat.put(0, "Seleccione...");
        for (CatalogoVO elementos : lista) {
            switch(elementos.getId()){
                case ClientesConstants.CICLO_ANALISIS:
                    cat.put(new Integer(ClientesConstants.INTERCICLO_APERTURA), elementos.getDescripcion());
                    break;
                case ClientesConstants.CICLO_RECHAZADO:
                    cat.put(new Integer(ClientesConstants.INTERCICLO_RECHAZADO), elementos.getDescripcion());
                    break;
                case ClientesConstants.CICLO_AUTORIZADO:
                    cat.put(new Integer(ClientesConstants.INTERCICLO_AUTORIZADO), elementos.getDescripcion());
                    break;
                case ClientesConstants.CICLO_DESEMBOLSADO:
                    cat.put(new Integer(ClientesConstants.INTERCICLO_DESEMBOLSADO), "Desembolsado");
                    break;
                case ClientesConstants.CICLO_DISPERSADO:
                    cat.put(new Integer(ClientesConstants.INTERCICLO_DISPERSADO), "Dispersado");
                    break;
            }
        }
        return cat;
    }*/

    public static TreeMap getCatalogoIgnorarAlertasFuturas() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "El grupo no quiere pagar");
        cat.put(new Integer(2), "Errores en la contratación");
        return cat;
    }

    public static TreeMap getCatalogoReestructurasIBS() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(1), "0004");
        cat.put(new Integer(2), "0005");
        cat.put(new Integer(5), "0006");
        return cat;
    }

    public static TreeMap getCatalogoCalifReferencia() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Buena");
        cat.put(new Integer(2), "Mala");
        return cat;
    }

    public static TreeMap getCatalogoSumaAsegurada() throws ClientesException {
        String nomCatalogo = "c_sumas_aseguradas";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<SumaAseguradaVO> lista = dao.getCatalogoSumaAsegurada(nomCatalogo);
        for (SumaAseguradaVO elementos : lista) {
            cat.put(new Integer(elementos.getIdSuma()), elementos.getSuma());
        }
        return cat;

        /*TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "10,000.00");
        cat.put(new Integer(2), "15,000.00");
        cat.put(new Integer(3), "30,000.00");
        cat.put(new Integer(4), "60,000.00");
        return cat;*/
    }

    public static TreeMap getCatalogoModulos() throws ClientesException {
        String nomCatalogo = "c_tipo_seguro";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<TipoSeguroVO> lista = dao.getCatalogoTipoSeguro(nomCatalogo);
        for (TipoSeguroVO elementos : lista) {
            cat.put(new Integer(elementos.getIdTipoSeguro()), elementos.getNombreSeguro());
        }
        return cat;
        /* POSIBLES APERTURAS PARA SEGURO SICAP
        cat.put(new Integer(2), "2");
        cat.put(new Integer(3), "3");
        cat.put(new Integer(4), "4");
        cat.put(new Integer(5), "5");
        
        return cat;*/
    }

    public static TreeMap getCatalogoBinario() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Si");
        cat.put(new Integer(2), "No");
        return cat;
    }

    public static TreeMap getCatalogoEstatus() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(1), "A");
        cat.put(new Integer(2), "I");
        return cat;
    }
    
    public static TreeMap getCatalogoPeriodo() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(1), "Actual");
        cat.put(new Integer(2), "Anterior");
        return cat;
    }

    public static TreeMap getCatalogoRelacion() throws ClientesException {
        String nomCatalogo = "c_relacion";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatRelacionVO> lista = dao.getCatalogoRelacion(nomCatalogo);
        for (CatRelacionVO elementos : lista) {
            cat.put(new Integer(elementos.getNumRelacion()), elementos.getNomRelacion());
        }
        /*TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Padre");
        cat.put(new Integer(2), "Madre");
        cat.put(new Integer(3), "Tío");
        cat.put(new Integer(4), "Tía");
        cat.put(new Integer(5), "Hijo");
        cat.put(new Integer(6), "Hija");
        cat.put(new Integer(7), "Hermano");
        cat.put(new Integer(8), "Hermana");
        cat.put(new Integer(9), "Esposo");
        cat.put(new Integer(10), "Esposa");
        cat.put(new Integer(11), "Otro");*/
        return cat;
    }
    
    public static TreeMap getCatalogoParentesco() throws ClientesException {
        String nomCatalogo = "c_parentesco";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatParentescoVO> lista = dao.getCatalogoParentesco(nomCatalogo);
        for (CatParentescoVO elementos : lista) {
            cat.put(new Integer(elementos.getNumParentesco()), elementos.getNomParentresco());
        }
        /*TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Esposa");
        cat.put(new Integer(2), "Esposo");
        cat.put(new Integer(3), "Hija");
        cat.put(new Integer(4), "Hijo");*/
        return cat;
    }
        public static TreeMap getCatalogoProcentaje(int semAdicional) throws ClientesException {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoProcentaje(semAdicional);
        cat.put(new Integer(0),"Seleccione...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.id), elementos.descripcion);
        }
        return cat;
    }
    
    public static TreeMap getCatalogoBancoSeguro() throws ClientesException {
        String nomCatalogo = "c_bancos_seguros";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatBancosSeguroVO> lista = dao.getCatalogoBancoSeguros(nomCatalogo);
        for (CatBancosSeguroVO elementos : lista) {
            cat.put(new Integer(elementos.getNumBanco()), elementos.getNomBanco());
        }
        return cat;
    }
    
    public static TreeMap getCatalogoCuentaSeguro() throws ClientesException {
        String nomCatalogo = "c_cuentas_seguros";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatCuentasSegurosVO> lista = dao.getCatalogoCuentasSeguros(nomCatalogo);
        for (CatCuentasSegurosVO elementos : lista) {
            cat.put(new Integer(elementos.getNumCuenta()), elementos.getNomCuenta());
        }
        return cat;
    }

    public static TreeMap getCatalogoProductosCwin() {
        TreeMap<String, Integer> cat = new TreeMap<String, Integer>();
        cat.put("Cons", 1);
        cat.put("Micr", 2);
        cat.put("Comu", 3);
        cat.put("Vivi", 4);
        cat.put("Rees", 5);
        cat.put("Cred", 21);
        cat.put("C12", 7);
        cat.put("C13", 7);
        cat.put("Plan", 7);
        cat.put("Mese", 7);
        return cat;
    }

    public static TreeMap getCatalogoGruposSucursal(int idSucursal) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        GrupoDAO dao = new GrupoDAO();

        GrupoVO elementos[] = dao.getGruposSucursal(idSucursal);
        for (int i = 0; elementos != null && i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].idGrupo), elementos[i].nombre);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoEsquemaCredito() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Normal");
        cat.put(new Integer(2), "Cofinanciado");
        return cat;
    }

    public static TreeMap getCatalogoBancosCheques() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(4), "Afirme");
        cat.put(new Integer(41), "Banorte");
        cat.put(new Integer(34), "HSBC");
        return cat;
    }
    
    public static TreeMap getCatalogoEstatusSaldos() {
        TreeMap<String, Integer> cat = new TreeMap<String, Integer>();
        cat.put("VIGENTE", new Integer(1));
        cat.put("MORA", new Integer(2));
        cat.put("LIQUIDADO", new Integer(3));
        cat.put("VENCIDO", new Integer(4));
        cat.put("CASTIGADO", new Integer(5));
        cat.put("CANCELADO", new Integer(6));
        return cat;
    }

    public static TreeMap getCatalogoBancos(String nomCatalogo) throws ClientesException {

        Hashtable<Integer, ComisionVO> catalogo = new Hashtable<Integer, ComisionVO>();
        CatalogoDAO dao = new CatalogoDAO();

        ComisionVO elementos[] = dao.getCatalogoComisiones(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i]);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoTablasAmort() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Saldo Global");
        cat.put(new Integer(2), "Saldo Insoluto");
        return cat;
    }

    public static TreeMap getCatalogoComisiones(String nomCatalogo) throws ClientesException {

        Hashtable<Integer, ComisionVO> catalogo = new Hashtable<Integer, ComisionVO>();
        CatalogoDAO dao = new CatalogoDAO();

        ComisionVO elementos[] = dao.getCatalogoComisiones(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i]);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoComisiones(int idProducto) throws ClientesException {

        TreeMap catalogo = new TreeMap();
        String nomCatalogo = null;
        switch (idProducto) {
            case 1:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_CONSUMO;
                break;
            case 2:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_MICRO;
                break;
            case 3:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_GRUPAL;
                break;
            case 4:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_VIVIENDA;
                break;
            case 5:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_REESTRUCTURA_GRUPAL;
                break;
            case 6:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_MAXZAPATOS;
                break;
            case 7:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_SELL_FINANCE;
                break;
            case 21:
                nomCatalogo = ClientesConstants.CAT_COMISIONES_CREDIHOGAR;
                break;
        }
        catalogo = getCatalogoComisiones(nomCatalogo);
        return catalogo;

    }

    public static String getDescripcionEstatusCredito(int situacionActualCredito, TreeMap catalogo) {
        if (situacionActualCredito == 0 || catalogo == null || catalogo.get(new Integer(situacionActualCredito)) == null) {
            return "";
        }
        String descripcion = (String) catalogo.get(new Integer(situacionActualCredito));
        return descripcion;
    }

    public static TreeMap getCatalogoTasas(String nomCatalogo) throws ClientesException {

        Hashtable<Integer, TasaInteresVO> catalogo = new Hashtable<Integer, TasaInteresVO>();
        CatalogoDAO dao = new CatalogoDAO();

        TasaInteresVO elementos[] = dao.getCatalogoTasasInteres(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i]);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoTasas(int idProducto) throws ClientesException {

        TreeMap catalogo = new TreeMap();
        String nomCatalogo = null;
        switch (idProducto) {
            case 1:
                nomCatalogo = ClientesConstants.CAT_TASAS_CONSUMO;
                break;
            case 2:
                nomCatalogo = ClientesConstants.CAT_TASAS_MICRO;
                break;
            case 3:
                nomCatalogo = ClientesConstants.CAT_TASAS_GRUPAL;
                break;
            case 4:
                nomCatalogo = ClientesConstants.CAT_TASAS_VIVIENDA;
                break;
            case 5:
                nomCatalogo = ClientesConstants.CAT_TASAS_REESTRUCTURA_GRUPAL;
                break;
            case 6:
                nomCatalogo = ClientesConstants.CAT_TASAS_MAXZAPATOS;
                break;
            case 7:
                nomCatalogo = ClientesConstants.CAT_TASAS_SELL_FINANCE;
                break;
            case 21:
                nomCatalogo = ClientesConstants.CAT_TASAS_CREDIHOGAR;
                break;
        }
        catalogo = getCatalogoTasas(nomCatalogo);

        return catalogo;

    }

//	public static TreeMap getCatalogoTasasMensuales(String nomCatalogo) throws ClientesException{
//
//		Hashtable<Integer,TasaInteresVO> catalogo = new Hashtable<Integer,TasaInteresVO>();
//		CatalogoDAO dao = new CatalogoDAO();
//
//		TasaInteresVO elementos[] = dao.getCatalogoTasasInteres(nomCatalogo);
//		for ( int i=0 ; i<elementos.length ; i++ ){
//			TasaInteresVO tasa = elementos[i];
//			tasa.valor = ((tasa.valor*1.15)/12)/100;
//			catalogo.put(new Integer(elementos[i].id), tasa);
//		}
//		return sort(catalogo);
//
//	}
    public static TreeMap getCatalogoTasasMensuales(int idProducto) throws ClientesException {

        TasaInteresVO tasa = null;
        Hashtable<Integer, TasaInteresVO> catalogo = new Hashtable<Integer, TasaInteresVO>();
        TreeMap tasas = getCatalogoTasas(idProducto);
        Collection valores = tasas.values();
        Object[] valoresObj = valores.toArray();

        for (int i = 0; i < valoresObj.length; i++) {
            tasa = (TasaInteresVO) valoresObj[i];
            tasa.valor = ((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO)) / 12) / 100;
            catalogo.put(tasa.id, tasa);
        }
        return sort(catalogo);
    }

    public static TreeMap getCatalogo(String nomCatalogo, int elem) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();

        CatalogoVO elementos[] = dao.getCatalogo(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            if (elementos[i].id != elem) {
                catalogo.put(new Integer(elementos[i].id), elementos[i].descripcion);
            }
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogo(String nomCatalogo) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        
        CatalogoVO elementos[] = dao.getCatalogo(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i].descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoSeleccione(String nomCatalogo) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        CatalogoVO elementos[] = dao.getCatalogo(nomCatalogo);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i].descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoEjecutivosActivos(int idSucursal) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getEjecutivosActivos(idSucursal);
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoBeneficiariosSaldoFavor(int numEquipo, int numCiclo) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getIntegrantesComite(numEquipo, numCiclo);
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoAnalistas(int numEquipo, int numCiclo) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getIntegrantesComite(numEquipo, numCiclo);
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoOperaciones(UsuarioVO usuario, boolean catalogoCompleto) throws ClientesException {

        TreeMap catalogo = getCatalogo(ClientesConstants.CAT_OPERACIONES);
        Hashtable<Integer, String> catalogoFiltrado = new Hashtable<Integer, String>();

        if (usuario.identificador.equals("E") && catalogoCompleto) {
            if (usuario.productos.containsKey(ClientesConstants.SELL_FINANCE)) {
                catalogoFiltrado.put(ClientesConstants.SELL_FINANCE, catalogo.get(ClientesConstants.SELL_FINANCE).toString());
            }
            if (usuario.productos.containsKey(ClientesConstants.MAX_ZAPATOS)) {
                catalogoFiltrado.put(ClientesConstants.MAX_ZAPATOS, catalogo.get(ClientesConstants.MAX_ZAPATOS).toString());
            }
            return sort(catalogoFiltrado);
        } else if (catalogoCompleto) {
            return catalogo;
        } else {
            catalogo.remove(new Integer(ClientesConstants.VIVIENDA));
            catalogo.remove(new Integer(ClientesConstants.CREDIHOGAR));
//			catalogo.remove(new Integer(ClientesConstants.SELL_FINANCE));
            catalogo.remove(new Integer(ClientesConstants.MAX_ZAPATOS));
//			catalogo.remove(new Integer(ClientesConstants.CONSUMO));
        }
        return catalogo;
    }

    public static TreeMap getCatalogoFrecuenciasPago(int idProducto) throws ClientesException {

        TreeMap catalogo = getCatalogo(ClientesConstants.CAT_FRECUENCIA_PAGO);
        switch (idProducto) {
            case 1:
// Cambio a credito comunal para incluir semanal y catorcenal JBL-JUN10				
                catalogo.remove(ClientesConstants.PAGO_SEMANAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
//				catalogo.remove(ClientesConstants.PAGO_MENSUAL);
//				catalogo.remove(ClientesConstants.PAGO_QUINCENAL);
                break;
            case 2:
                catalogo.remove(0);
                //catalogo.remove(ClientesConstants.PAGO_SEMANAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
            case 3:
                catalogo.remove(0);
                catalogo.remove(ClientesConstants.PAGO_MENSUAL);
                catalogo.remove(ClientesConstants.PAGO_QUINCENAL);
                break;
            case 4:
                catalogo.remove(ClientesConstants.PAGO_SEMANAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
            case 5:
                catalogo.remove(0);
                catalogo.remove(ClientesConstants.PAGO_MENSUAL);
                catalogo.remove(ClientesConstants.PAGO_QUINCENAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
            case 6:
                catalogo.remove(0);
                catalogo.remove(ClientesConstants.PAGO_SEMANAL);
                catalogo.remove(ClientesConstants.PAGO_QUINCENAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
            case 7:
                catalogo.remove(0);
                catalogo.remove(ClientesConstants.PAGO_MENSUAL);
                catalogo.remove(ClientesConstants.PAGO_SEMANAL);
//				catalogo.remove(ClientesConstants.PAGO_QUINCENAL);
//				catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
            case 21:
                catalogo.remove(ClientesConstants.PAGO_SEMANAL);
                catalogo.remove(ClientesConstants.PAGO_CATORCENAL);
                break;
        }

        return catalogo;
    }

    private static TreeMap sort(Hashtable hash) {

        TreeMap<Object, Object> tree = new TreeMap<Object, Object>();
        if (hash != null && hash.size() > 0) {
            Enumeration e = hash.keys();
            while (e.hasMoreElements()) {
                Object key = e.nextElement();
                Object value = hash.get(key);
                tree.put(key, value);
            }
        }
        return tree;

    }

    public static TreeMap getCatalogoEjecutivos(int idSucursal) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();

        CatalogoVO elementos[] = dao.getEjecutivos(idSucursal);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i].descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoBancosDispersion() throws ClientesException {

        Hashtable<Integer, String> catalogoBancos = new Hashtable<Integer, String>();
        ArrayList <CatalogoVO> catalogo = new ArrayList<CatalogoVO>();
        catalogo = new CatalogoDAO().getBancos();
        for(CatalogoVO cat: catalogo){
            catalogoBancos.put(cat.id, cat.descripcion);
        }
        return sort(catalogoBancos);
    }
    
    public static TreeMap getCatalogoBancosDispersionTarjeta() throws ClientesException {

        Hashtable<Integer, String> catalogoBancos = new Hashtable<Integer, String>();
        ArrayList <CatalogoVO> catalogo = new ArrayList<CatalogoVO>();
        catalogo = new CatalogoDAO().getBancosTarjeta();
        for(CatalogoVO cat: catalogo){
            catalogoBancos.put(cat.id, cat.descripcion);
        }
        return sort(catalogoBancos);
    }

    public static String getNombreEjecutivo(int idSucursal, CicloGrupalVO ciclo) throws ClientesException {
        TreeMap catAsesores = CatalogoHelper.getCatalogoEjecutivos(idSucursal);
        Set set = catAsesores.keySet();
        Iterator llaves = set.iterator();
        Object key = null;
        StringBuffer codigo = new StringBuffer();
        String nombreEjecutivo = null;
        while (llaves.hasNext()) {
            key = llaves.next();
            if (key.toString().equals(String.valueOf(ciclo.asesor))) {
                codigo.append(catAsesores.get(key).toString());
            }
        }
        if (codigo == null || codigo.toString().equals("")) {
            nombreEjecutivo = "&nbsp;";
        } else {
            nombreEjecutivo = codigo.toString();
        }

        return nombreEjecutivo;
    }

    public static TreeMap getCatalogoRepresentantes(int idSucursal) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();

        CatalogoVO elementos[] = dao.getRepresentantes(idSucursal);
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i].descripcion);
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoEjecutivos(int idSucursal, String estatus) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        EjecutivoCreditoVO ejecutivo = null;
        EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
        ArrayList<EjecutivoCreditoVO> listaEjecutivo = ejecutivodao.getEjecutivosSucursal(idSucursal, estatus);

        for (int i = 0; i < listaEjecutivo.size(); i++) {
            ejecutivo = listaEjecutivo.get(i);
            catalogo.put(new Integer(ejecutivo.idEjecutivo), ejecutivo.nombre + " " + ejecutivo.aPaterno + " " + ejecutivo.aMaterno);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoEjecutivosComercial(int idSucursal, String estatus) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        EjecutivoCreditoVO ejecutivo = null;
        EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
        ArrayList<EjecutivoCreditoVO> listaEjecutivo = ejecutivodao.getEjecutivosSucursalComercial(idSucursal, estatus);

        for (int i = 0; i < listaEjecutivo.size(); i++) {
            ejecutivo = listaEjecutivo.get(i);
            catalogo.put(new Integer(ejecutivo.idEjecutivo), ejecutivo.nombre + " " + ejecutivo.aPaterno + " " + ejecutivo.aMaterno);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoAperturador(int idSucursal, String estatus) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        EjecutivoCreditoVO ejecutivo = null;
        EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
        ArrayList<EjecutivoCreditoVO> listaEjecutivo = ejecutivodao.getAperturadorSucursal(idSucursal, estatus);
        catalogo.put(0, "Seleccione...");
        for (int i = 0; i < listaEjecutivo.size(); i++) {
            ejecutivo = listaEjecutivo.get(i);
            catalogo.put(new Integer(ejecutivo.idEjecutivo), ejecutivo.nombre + " " + ejecutivo.aPaterno + " " + ejecutivo.aMaterno);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoBeneficiarios(int idCliente, int idSolicitud) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        ClienteDAO clienteDAO = new ClienteDAO();
        BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO();
        String nombreTitular = null;
        ArrayList<String> nombreBeneficiario= new ArrayList<String>();
        //int idCatalogo=0;
        nombreTitular = clienteDAO.getNombreCliente(idCliente);
        System.out.println("TITULAR");
        nombreBeneficiario = beneficiarioDAO.getNombreBeneficiario(idCliente, idSolicitud);
        for(int i=0;i<nombreBeneficiario.size();i++){
            System.out.println("BENEF: "+nombreBeneficiario);
        }
        catalogo.put(0, "Seleccione...");
        catalogo.put(1, nombreTitular);
        if(nombreBeneficiario.size()>1){
            catalogo.put(3, "BENEFICIARIOS");
        }
        else {
            catalogo.put(2,nombreBeneficiario.get(0));
            //idCatalogo++;
        }
        catalogo.put(4, "OTRO");
        return sort(catalogo);
    }

    public static TreeMap getParametrosScoring(int cveCatalogo) {

        Hashtable<Integer, Double> catalogo = new Hashtable<Integer, Double>();
        try {
            CatalogoDAO dao = new CatalogoDAO();
            ParametroScoringVO elementos[] = dao.getCatalogoParametrosScoring(cveCatalogo);
            for (int i = 0; i < elementos.length; i++) {
                catalogo.put(new Integer(elementos[i].codigo), new Double(elementos[i].valor));
            }
        } catch (Exception e) {
        }
        return sort(catalogo);

    }

    public static TreeMap getCatalogoSucursal(SucursalVO[] sucursales) throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        catalogo.put(new Integer(0), "Seleccione...");

        for (int i = 0; sucursales != null && i < sucursales.length; i++) {
            catalogo.put(new Integer(sucursales[i].idSucursal), sucursales[i].nombre);
        }

        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoAnalistas() throws ClientesException {

        Hashtable<String, String> catalogo = new CatalogoDAO().getAnalistas();
        return sort(catalogo);

    }

    public static TreeMap getCatalogoSucursales() throws ClientesException {

        SucursalVO procesaSucursal = null;
        //System.out.println("entra a catalogo");
        Hashtable<Integer, SucursalVO> sucursales = new SucursalDAO().getSucursales();
        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        catalogo.put(new Integer(0), "Seleccione...");

// JBL 6-OCT-10, SE CAMBIA A QUE VAYA A SUCURSALES -1 PARA QUE NO TRAIGA AL DF LA CUAL NO ES CONSECUTIVA
//		for ( int i=0 ; sucursales!=null && i<sucursales.size() ; i++ ){
        for (int i = 0; sucursales != null && i < sucursales.size(); i++) {
            //System.out.println("numero sucursal" + i);
            procesaSucursal = (SucursalVO) sucursales.get(i);
            //System.out.println("nombre " + procesaSucursal.nombre);
            catalogo.put(new Integer(procesaSucursal.idSucursal), procesaSucursal.nombre);
        }

        return sort(catalogo);

    }

    public static String getParametro(String cveParametro) {

        String valor = null;
        try {
            CatalogoDAO dao = new CatalogoDAO();
            ParametroVO parametro = dao.getParametro(cveParametro);
            if (parametro != null) {
                valor = parametro.valor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor;

    }
        public static String getParametro(String cveParametro,Connection cn) {

        String valor = null;
        try {
            CatalogoDAO dao = new CatalogoDAO();
            ParametroVO parametro = dao.getParametro(cveParametro,cn);
            if (parametro != null) {
                valor = parametro.valor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor;

    }

    public static String updateParametro(String cveParametro, String valor) {
        try {
            CatalogoDAO dao = new CatalogoDAO();
            dao.updateParametro(cveParametro, valor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor;
    }
    public static String updateParametro(String cveParametro, String valor, Connection cn)throws ClientesDBException {
        try {
            CatalogoDAO dao = new CatalogoDAO();
            dao.updateParametro(cveParametro, valor,cn);
        } catch (Exception e) {
            myLogger.error("getParametro", e);
            throw new ClientesDBException(e.getMessage());
        }
        return valor;
    }

    public static String getDescripcionTasa(int idTasa, TreeMap catalogo) {
        if (idTasa == 0 || catalogo == null || catalogo.get(new Integer(idTasa)) == null) {
            return "";
        }
        TasaInteresVO tasaVO = (TasaInteresVO) catalogo.get(new Integer(idTasa));
        return tasaVO.descripcion;
    }

    public static String getDescripcionComision(int idComision, TreeMap catalogo) {
        if (idComision == 0 || catalogo == null || catalogo.get(new Integer(idComision)) == null) {
            return "";
        }
        ComisionVO comision = (ComisionVO) catalogo.get(new Integer(idComision));
        return comision.descripcion;
    }

    public static String getDescripcionFrecuenciaPago(int idFrecuencia, TreeMap catalogo) {
        if (idFrecuencia == 0 || catalogo == null || catalogo.get(new Integer(idFrecuencia)) == null) {
            return "";
        }
        String descripcion = (String) catalogo.get(new Integer(idFrecuencia));
        return descripcion;
    }
    
    public static String getDescripcionEstatus(int idEstatus, TreeMap catalogo) {
        if (idEstatus == 0 || catalogo == null || catalogo.get(new Integer(idEstatus)) == null) {
            return "";
        }
        String descripcion = (String) catalogo.get(new Integer(idEstatus));
        return descripcion;
    }
    
    public static String getDescripcionBeneficiario(int idBeneficiario, TreeMap catalogo) {
        if (idBeneficiario == 0 || catalogo == null || catalogo.get(new Integer(idBeneficiario)) == null) {
            return "";
        }
        String descripcion = (String) catalogo.get(new Integer(idBeneficiario));
        return descripcion;
    }

    public static TreeMap getCatalogoDesembolso(HttpServletRequest request, SolicitudVO solicitud) {
        TreeMap catalogo = new TreeMap();
        switch (solicitud.tipoOperacion) {
            //CONDICIONES A CUMPLIR PARA UN CREDITO COMUNAL PARA QUE PUEDA SER DESEMBOLSADO
            case ClientesConstants.GRUPAL:
                if (solicitud.estatus != ClientesConstants.SOLICITUD_RECHAZADA) {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                } else {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                }
                break;
            //CONDICIONES A CUMPLIR PARA UN MICROCREDITO PARA QUE PUEDA SER DESEMBOLSADO
            case ClientesConstants.MICROCREDITO:
                if (solicitud.solicitudReestructura != 0) {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (request.isUserInRole("DESEMBOLSO_REESTRUCTURA") && solicitud.amortizacion != null)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                } else {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                }
                break;
            case ClientesConstants.REESTRUCTURA_GRUPAL:
                if (solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA) {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                } else {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                }
                break;
            case ClientesConstants.CONSUMO:
                if (solicitud.solicitudReestructura != 0) {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (request.isUserInRole("DESEMBOLSO_REESTRUCTURA") && solicitud.amortizacion != null)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                } else {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                }
                break;
            case ClientesConstants.CREDIHOGAR:
                if (solicitud.solicitudReestructura != 0) {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (request.isUserInRole("DESEMBOLSO_REESTRUCTURA") && solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                } else {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                }
                break;
            case ClientesConstants.MAX_ZAPATOS:
                catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                break;
            case ClientesConstants.SELL_FINANCE:
                if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                } else {
                    catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                }
                break;
            case ClientesConstants.VIVIENDA:
                if (solicitud.solicitudReestructura != 0) {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (request.isUserInRole("DESEMBOLSO_REESTRUCTURA") && solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                } else {
                    if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || (solicitud.amortizacion != null && FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date()) == 0)) {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(false);
                    } else {
                        catalogo = SolicitudHelper.getCatalogoDesembolso(true);
                    }
                }
                break;

        }
        return catalogo;
    }

    public static Object getCatalogoJNDI(String nombre) {

        Object obj = null;
        InitialContext ic = null;
        try {
            ic = new InitialContext();
            //Busca objeto en arbol JNDI
            obj = ic.lookup("CF/SICAP/CATALOGOS/" + nombre);
        } catch (NamingException ne) {
            //No se encontro objeto en arbol JNDI
            myLogger.debug("Catalogo [" + nombre + "] no existe en arbol JNDI");
        }
        return obj;
    }

    public static TreeMap putCatalogoJNDI(String nombre, TreeMap catalogo) {

        InitialContext ic = null;
        try {
            ic = new InitialContext();
            //se agrega el catalogo en el árbol JNDI
            ic.bind("CF/SICAP/CATALOGOS/" + nombre, catalogo);
        } catch (NamingException ne) {
            System.out.println("Error al cargar catalogo en JNDI");
            ne.printStackTrace();
        }
        return catalogo;
    }

    public static TreeMap getCatalogoPlanesSellFinance() throws ClientesException {

        Hashtable<Integer, TasaInteresVO> catalogo = new Hashtable<Integer, TasaInteresVO>();
        CatalogoDAO dao = new CatalogoDAO();

        TasaInteresVO elementos[] = dao.getCatalogoTasasInteres("c_planes_sell_finance");
        for (int i = 0; i < elementos.length; i++) {
            catalogo.put(new Integer(elementos[i].id), elementos[i]);
        }
        return sort(catalogo);
    }

    public static String getCatalogoSucursalesTexto(HttpServletRequest request) {
        String sucursales = null;
        HttpSession session = request.getSession();
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
        if (usuario.sucursales != null) {
            sucursales = "(";
            for (int i = 0; i < usuario.sucursales.length; i++) {
                sucursales += String.valueOf(usuario.sucursales[i].idSucursal) + ",";
            }
            sucursales += "0)";
        } else {
            sucursales = "(" + String.valueOf(usuario.idSucursal) + ")";
        }
        return sucursales;
    }

    public static TreeMap getCatalogoDictamen() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(ClientesConstants.APROBAR, "Aprobar");
        cat.put(ClientesConstants.DENEGAR, "Rechazar");
        cat.put(ClientesConstants.INVESTIGAR, "Investigar");
        cat.put(ClientesConstants.ALTO_RIESGO, "Alto riesgo");
        return cat;
    }

    public static TreeMap getCatalogoDictamenCapPago() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(1, "Buena");
        cat.put(2, "Mala");
        return cat;
    }

    public static TreeMap getCatalogoCalificacionGrupal() {
        TreeMap<String, String> cat = new TreeMap<String, String>();
        cat.put("C3","C3");
        cat.put("C1","C1");
        cat.put("B3","B3");
        cat.put("B1","B1");
        cat.put("A3","A3");
        cat.put("A2","A2");
        cat.put("A1","A1");
        cat.put("B", "B");
        //cat.put("RG", "RG");
        //cat.put("A2", "A2");
        //cat.put("A3", "A3");
        return cat;
    }
        public static TreeMap getCatalogoTasasNuevas() {
        TreeMap<String, String> cat = new TreeMap<String, String>();
        cat.put("D1", "D1");
        cat.put("C2", "C2");
        cat.put("B4", "B4");      
        cat.put("B2", "B2");
        cat.put("B", "B");
        
        
        return cat;
    }

    public static String getIDCNBV(Integer bancoReferencia) {

        String id = "";
        Hashtable<Integer, String> idBanco = new Hashtable<Integer, String>();

        idBanco.put(1, "0021");
        idBanco.put(2, "0072");
        idBanco.put(3, "0012");
        idBanco.put(4, "0062");
        idBanco.put(5, "0166");
        idBanco.put(6, "0060");
        idBanco.put(7, "0099");
        //idBanco.put(8, "0110"); o idBanco.put(11, "0110");

        id = (String) idBanco.get(bancoReferencia);

        return id;
    }

    public static int getIDBancoReferenciaSICAP(String bancoReferencia) {

        int id = 0;
        Hashtable<String, Integer> idBanco = new Hashtable<String, Integer>();
        System.out.println("bancoReferencia: "+bancoReferencia);
        idBanco.put("0021", ClientesConstants.ID_BANCO_HSBC);
        idBanco.put("0072", ClientesConstants.ID_BANCO_BANORTE);
        idBanco.put("0012", ClientesConstants.ID_BANCO_BANCOMER);
        idBanco.put("0062", ClientesConstants.ID_BANCO_AFIRME);
        idBanco.put("0166", ClientesConstants.ID_BANCO_BANSEFI);
        idBanco.put("0060", ClientesConstants.ID_BANCO_SANTANDER);
        idBanco.put("0080", ClientesConstants.ID_BANCO_EFECTIVO);
        idBanco.put("0081", ClientesConstants.ID_BANCO_TRANSFERENCIA);
        idBanco.put("0061", ClientesConstants.ID_BANCO_SANTANDER_28);
        idBanco.put("0090", ClientesConstants.ID_BANCO_TELECOM);
        idBanco.put("0110", ClientesConstants.ID_BANCO_COMA);
        idBanco.put("0120", ClientesConstants.ID_BANCO_BANAMEX);
        idBanco.put("0013", ClientesConstants.ID_BANCO_SEGUROS);
        idBanco.put("0014", ClientesConstants.ID_BANCO_COM_MEXICANA);
        idBanco.put("0015", ClientesConstants.ID_BANCO_CONDONACION_MULTA);
        idBanco.put("0016", ClientesConstants.ID_BANCO_SCOTIABANK);
        idBanco.put("0017", ClientesConstants.ID_BANCO_BANBAJIO);
        idBanco.put("0018", ClientesConstants.ID_BANCO_OXXO);
        //idBanco.put("0018", ClientesConstants.ID_BANCO_PAYNET);
        idBanco.put("0020", ClientesConstants.ID_BANCO_BENAVIDES);
        idBanco.put("0021", ClientesConstants.ID_BANCO_FABC);
        idBanco.put("0023", ClientesConstants.ID_BANCO_FRESKO);
        idBanco.put("0026", ClientesConstants.ID_BANCO_SORIANA);
        idBanco.put("0025", ClientesConstants.ID_BANCO_OPENPAY);
        idBanco.put("0027", ClientesConstants.ID_BANCO_CASALEY);

        id = idBanco.get(bancoReferencia);

        return id;

    }

    public static TreeMap getCatalogoBancosIBS_CWin() {
        TreeMap<Integer, Integer> cat = new TreeMap<Integer, Integer>();

        cat.put(21, 1);
        cat.put(72, 2);
        cat.put(12, 3);
        cat.put(62, 4);
        cat.put(166, 5);
        cat.put(30, 6);

        return cat;

    }
    //CATALOGO PREGUNTAS VISITA GRUPAL

    public static TreeMap getCatalogoProblemasGrupo() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "La Presidenta / Tesorera se quedo con lo pagos");
        cat.put(new Integer(2), "Se le pago al asesor");
        cat.put(new Integer(3), "No se reúne el grupo");
        cat.put(new Integer(4), "Los integrantes del grupo no se conocen");
        cat.put(new Integer(5), "El grupo tiene conflictos internos y ya no dan el pago");
        cat.put(new Integer(6), "No hay tesorera en el grupo");
        cat.put(new Integer(7), "Los integrantes del grupo no saben que estan en mora");
        cat.put(new Integer(8), "El grupo esta desintegrado y no cumplen con las promesas de pago");
        cat.put(new Integer(9), "El grupo no quiere hacer solidario");
        cat.put(new Integer(10), "Solo pagan unos integrantes");
        return cat;
    }

    public static TreeMap getCatalogoProblemasAsesor() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "No tiene asesor que los atienda");
        cat.put(new Integer(2), "El asesor no asiste a las reuniones");
        cat.put(new Integer(3), "El asesor llega tarde a las reuniones");
        cat.put(new Integer(4), "El asesor no los apoya en la cobranza");
        cat.put(new Integer(5), "El asesor está involucrado en el fraude");
        cat.put(new Integer(6), "El asesor recibe el dinero del grupo");
        return cat;
    }

    public static TreeMap getCatalogoProblemasNegocio() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "No tiene ventas");
        cat.put(new Integer(2), "Bajaron sus ventas y no tiene para pagar");
        cat.put(new Integer(3), "Cerró el negocio por falta de ventas");
        cat.put(new Integer(4), "Asaltaron el establecimiento");
        return cat;
    }

    public static TreeMap getCatalogoProblemasPersonales() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "El cliente tuvo un accidente y no puede trabajar");
        cat.put(new Integer(2), "El cliente no tiene trabajo");
        cat.put(new Integer(3), "El cliente tiene problemas familiares");
        cat.put(new Integer(4), "El cliente padece de una enfermedad");
        cat.put(new Integer(5), "El cliente tiene otras deudas");
        return cat;
    }

    public static TreeMap getCatalogoProblemasOtros() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "No quiere pagar");
        cat.put(new Integer(2), "No se localiza el cliente");
        cat.put(new Integer(3), "Cambio de domicilio y no se localiza");
        cat.put(new Integer(4), "Es prestanombres");
        cat.put(new Integer(5), "Se le pago al referenciado o persona externa");
        cat.put(new Integer(6), "Se le pago al asesor anterior");
        //cat.put(new Integer(7), "Promesa de pago para \"x\" fecha");
        return cat;
    }

    /*public static TreeMap getCatalogoPropuestasSolucion() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccionar...");
        cat.put(new Integer(1), "Garantía Solidaria");
        cat.put(new Integer(2), "Cobranza Grupal ó Individual");
        cat.put(new Integer(3), "Convenio de Pago");
        cat.put(new Integer(5), "Pago con ahorros del grupo");
        cat.put(new Integer(6), "Gestor de cobranza");
        cat.put(new Integer(7), "Demanda");
        cat.put(new Integer(4), "Otra");
        return cat;
    }
*/
    public static TreeMap getMapeoProductosIBS_SICAP() {
        TreeMap<String, Integer> cat = new TreeMap<String, Integer>();
        cat.put("0001", new Integer(1));
        cat.put("0002", new Integer(2));
        cat.put("0003", new Integer(3));
        cat.put("0004", new Integer(1));
        cat.put("0005", new Integer(2));
        cat.put("0006", new Integer(5));
        return cat;
    }

    public static TreeMap getCatalogoBancosAfirme() {

        TreeMap<String, String> cat = new TreeMap<String, String>();
        cat.put("0062", "BANCA AFIRME, S.A.");
        cat.put("0030", "BANCO DEL BAJIO, S.A.");
        cat.put("0072", "BANCO MERCANTIL DEL NORTE, S. A.");
        cat.put("0166", "BANSEFI");
        cat.put("0012", "BBVA BANCOMER, S.A.");
        cat.put("0021", "HSBC");
        return cat;

    }

    public static String getDescripcionTipoTransaccion(String indice) {
        String descripcion = "";
        TreeMap<String, String> cat = new TreeMap<String, String>();
        cat.put(ClientesConstants.DESEMBOLSO, "DESEMBOLSO");
        cat.put(ClientesConstants.PROVISION, "PROVISION");
        cat.put(ClientesConstants.MORATORIO, "MORATORIO");
        cat.put(ClientesConstants.MULTA, "MULTA");
        cat.put(ClientesConstants.REGISTRO_PAGO, "REGISTRO DE PAGO");
        cat.put(ClientesConstants.PAGO, "PAGO");
        cat.put(ClientesConstants.CONDONACION, "CONDONACION");
        cat.put(ClientesConstants.C_ESTADO_VENCIDO, "CAMBIO DE ESTADO A VENCIDO");
        cat.put(ClientesConstants.CASTIGO, "CASTIGO");
        cat.put(ClientesConstants.RESTRUCTURA, "RESTRUCUTRA");
        cat.put("CAN_CON", "CANCELACION DE CONDONACION");
        cat.put("CAN_DES", "CANCELACION DE DESEMBOLSO");
        cat.put("CAN_PRV", "CANCELACION DE PROVISION");
        cat.put(ClientesConstants.PAGO_CANCELADO, "CANCELACION DE PAGO");
        cat.put(ClientesConstants.REGISTRO_CAN_PAGO, "CANCELACION DE REGISTRO DE PAGO");
        cat.put(ClientesConstants.DEVOLUCION, "DEVOLUCION DE ODP");
        cat.put(ClientesConstants.DEVOLUCION_SALDO_FAVOR, "DEVOLUCION DE SALDO A FAVOR");
        cat.put(ClientesConstants.REGISTRO_PAGO_GARANTIA, "REGISTRO PAGO GARANTIA");
        cat.put("CAN_RGA", "CANCELACION REGISTRO PAGO GARANTIA");
        Set set = cat.keySet();
        Iterator llaves = set.iterator();
        Object key = null;
        while (llaves.hasNext()) {
            key = llaves.next();
            if (key.toString().equals(String.valueOf(indice))) {
                descripcion = cat.get(key).toString();
            }
        }
        return descripcion;
    }

    public static boolean esSucursalAutorizadaComunal(int idEjecutivo) {
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_AUTOR_COMUNAL");
        String ejecutivosAutorizados[] = valor.split(",");
        //int ejecutivosAutorizados[] = new int[elementos.length]; 

        //INSERT INTO C_PARAMETROS VALUES ('SUCURSALES_AUTOR_COMUNAL', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38');
        for (int i = 0; i < ejecutivosAutorizados.length; i++) {
            if (Integer.parseInt(ejecutivosAutorizados[i].trim()) == idEjecutivo) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    public static TreeMap getCatalogoSegmento() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Consumo");
        cat.put(new Integer(2), "Producción");
        cat.put(new Integer(3), "Vivienda");
        return cat;
    }

    public static TreeMap getCatalogoTipoTabla() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Pagos Fijos");
        return cat;
    }

    public static TreeMap getCatalogoBaseInteres() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Saldo Global");
        cat.put(new Integer(2), "Saldo Insoluto");
        return cat;
    }

    public static TreeMap getCatalogoEvitaFeriados() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Si");
        cat.put(new Integer(2), "No");
        return cat;
    }

    public static TreeMap getCatalogoManejoFeriados() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Forward");
        cat.put(new Integer(2), "Backward");
        return cat;
    }

    public static TreeMap getCatalogoUnidadTiempo() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Quincenal");
        cat.put(new Integer(2), "Mensual");
        cat.put(new Integer(3), "Semanal");
        cat.put(new Integer(4), "Catorcenal");
        return cat;
    }

    public static TreeMap getCatalogoEstatusBuroInterno() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "Buro");
        cat.put(new Integer(2), "Historico");
        return cat;
    }

    public static TreeMap getCatalogoMotivo() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(1), "No pago");
        cat.put(new Integer(2), "Refinanciamiento");
        cat.put(new Integer(3), "Fraude");
        cat.put(new Integer(4), "Moroso");
        cat.put(new Integer(5), "otros");
        return cat;
    }
    
    public static TreeMap getCatalogoPlazos() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(12), "12");
        cat.put(new Integer(16), "16");
        return cat;
    }
    
    public static TreeMap getCatalogoDespachos() throws ClientesException {

        String nomCatalogo = "c_despachos";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<DespachosVO> lista = dao.getCatalogoDespachos(nomCatalogo);
        for (DespachosVO elementos : lista) {
            cat.put(new Integer(elementos.getIdDespacho()), elementos.getNomDespacho());
        }
        return cat;

    }
    
    public static TreeMap getCatalogoFondeador() throws ClientesException {

        String nomCatalogo = "c_fondeadores";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        cat.put(new Integer(0), "Seleccionar...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;

    }
    
    public static TreeMap getCatalogoActividades() throws ClientesException {

        String nomCatalogo = "c_actividades";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(nomCatalogo);
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;

    }
    
    public static TreeMap getCatalogoGarantiaGrupal() throws ClientesException {

        String nomCatalogo = "c_garantia_grupal";
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneralEstatus(nomCatalogo,"gg_estatus",1);
        cat.put(new Integer(0), "Seleccionar...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;

    }
    
    public static boolean esEquipoAutorizadoGarantia(int numGrupo) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("EQUIPO_GARANTIA");
        String equipos[] = valor.split(",");
        for (int i = 0; i < equipos.length; i++) {
            if (Integer.parseInt(equipos[i].trim()) == numGrupo) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static TreeMap getCatalogoGeneral(int idCatalogo) throws ClientesException {
        
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<CatalogoVO> lista = dao.getCatalogoGeneral(idCatalogo);
        cat.put(new Integer(0), "Seleccionar...");
        for (CatalogoVO elementos : lista) {
            cat.put(new Integer(elementos.getId()), elementos.getDescripcion());
        }
        return cat;
    }
    
    public static boolean esSucursalPaynet(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUC_PAYNET");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    public static boolean esSucursalInterCiclo(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_INTERCICLO");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static boolean esSucursalAdicional(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_ADICIONAL");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static boolean esSucursalAceptaRegular(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_PILOTO_REGULARES");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
        public static boolean esSucursalPiloto3(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_PILOTO_3");
        String sucursales[] = valor.split(",");

        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        //para habilitar a todas las sucursales solo poner el valor en 0
        if (sucursales.length ==1){
            if(Integer.parseInt(sucursales[0].trim())==0)
             resultado = true;   
        }
        return resultado;
    }
    public static boolean sucursalPermiteAutorizacionPorExcepcion(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_AUTORIZACION_POR_EXCEPCION");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static boolean sucursalCartaRenovacion(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_CARTA_RENOVACION");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static boolean esSucursalFunerario(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_FUNERARIO");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
        public static boolean esSucursalFunerarioIncremento(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_FUNERARIO_INCREMENTO");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    public static boolean esSucursalProspectoInterCiclo(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_FUTURO_INTERCICLO");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    public static boolean esSucursalCapRapida (int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("SUCURSALES_CAPTURARAPIDA");
        if(valor.equals("0")){
            resultado = true;
        }else{
            String sucursales[] = valor.split(",");
            for (int i = 0; i < sucursales.length; i++) {
                if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                    resultado = true;
                    break;
                }
            }
        }
        return resultado;
    }
    
     public static boolean esSucursaldeIxaya(int numSucursal) {
        
        boolean resultado = false;
        String valor = CatalogoHelper.getParametro("LIBERACIONES_IXAYA");
        String sucursales[] = valor.split(",");
        for (int i = 0; i < sucursales.length; i++) {
            if (Integer.parseInt(sucursales[i].trim()) == numSucursal) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    
    public static TreeMap getCatalogoAuditoresActivos() throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getAuditorActivo();
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
    
    public static ArrayList<SucursalVO> getCatalogoMapaSucursales() throws ClientesException {
        CatalogoDAO dao = new CatalogoDAO();
        ArrayList<SucursalVO> lista = new ArrayList<SucursalVO>();
        lista = dao.getMapaSucursales();
        return lista;
    }
    
    public static TreeMap getCatalogoFondeadorActivo() throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getCatalogoFondeadores();
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
    
     public static TreeMap getCatalogoFondeadorAsigancionCartera() throws ClientesException {

        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getCatalogoFondeadoresAsignacionCarteraGarantia();
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
     
     public static TreeMap getCatalogoFondeadorPreseleccion() throws ClientesException {
        Hashtable<Integer, String> catalogo = new Hashtable<Integer, String>();
        CatalogoDAO dao = new CatalogoDAO();
        catalogo.put(new Integer(0), "Seleccione...");

        ArrayList<CatalogoVO> elementos = dao.getCatalogoFondeadoresPreseleccion();
        for (CatalogoVO cat : elementos){
            catalogo.put(new Integer(cat.id), cat.descripcion);
        }
        return sort(catalogo);

    }
    
    public static TreeMap getCatalogoSubProductoGrupal() {
        TreeMap<Integer, String> cat = new TreeMap<Integer, String>();
        //cat.put(new Integer(0), "Seleccione...");
        cat.put(new Integer(0), "Comunal");
        cat.put(new Integer(1), "Inter-Ciclo");
        //cat.put(new Integer(3), "Paralelo");
        return cat;
    }
    
    public static String getParametroFondeador(String cveParametro,int idFondeador) {

        String valor = null;
        try {
            CatalogoDAO dao = new CatalogoDAO();
            ParametroVO parametro = dao.getParametroFondeador(cveParametro,idFondeador);
            if (parametro != null) {
                valor = parametro.valor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor;

    }
        public static String getParametroFondeador(String cveParametro,int idFondeador,Connection cn) {

        String valor = null;
        try {
            CatalogoDAO dao = new CatalogoDAO();
            ParametroVO parametro = dao.getParametroFondeador(cveParametro,idFondeador,cn);
            if (parametro != null) {
                valor = parametro.valor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor;

    }
    //mostrar combo lineas credito activas
    public static TreeMap getLineasCreditoActivas() throws ClientesException {

        Hashtable<Integer, String> lineasCredActivas = new Hashtable<Integer, String>();
        LineaCreditoDAO dao = new LineaCreditoDAO();
        lineasCredActivas.put(new Integer(0), "Seleccione...");

        ArrayList<LineaCreditoVO> elementos = dao.getNombreLineasCredito(ClientesConstants.ACTIVO);
        for (LineaCreditoVO lineas : elementos){
            lineasCredActivas.put(new Integer(lineas.getIdLineaCredito()), lineas.getNombreLineaCredito());
        }
        return sort(lineasCredActivas);

    }
    
    //mostrar combo pagares activos
    public static TreeMap getPagaresLineaCred() throws ClientesException {

        Hashtable<Integer, String> pagaresActivos = new Hashtable<Integer, String>();
        PagareDAO dao = new PagareDAO();
        pagaresActivos.put(new Integer(0), "Seleccione...");

        ArrayList<PagareVO> elementos = dao.getPagaresLineaCredito(0, ClientesConstants.ACTIVO);
        for (PagareVO pagares : elementos){
            pagaresActivos.put(new Integer(pagares.getNumPagare()), pagares.getNombrePagare()+" - "+pagares.getNombreLineaCredito());
        }
        return sort(pagaresActivos);

    }
    
}
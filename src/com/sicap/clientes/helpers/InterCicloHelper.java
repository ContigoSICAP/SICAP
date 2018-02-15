/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.ClienteIntercicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.vo.ClienteIntercicloVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class InterCicloHelper {

    private static Logger myLogger = Logger.getLogger(InterCicloHelper.class);

    public static SaldoIBSVO validaSolicitudIC(SolicitudVO solicitud, ClienteVO cliente) throws ClientesException {
        SaldoIBSVO saldo = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        IntegranteCicloDAO integranteCicloDao = new IntegranteCicloDAO();
        try {
            if (integranteCicloDao.getIntegranteActivo(cliente.idCliente).equals("")) {
                if (cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO) {
                    saldo = saldoDAO.validaInterCiclo(solicitud.idGrupo);
                }
            }
        } catch (Exception e) {
            myLogger.error("InterCicloHelper.validarInterciclo", e);
            throw new ClientesException(e.getMessage());
        }
        return saldo;
    }

    public static int semanaDispersion(SaldoIBSVO saldo) throws ClientesException {
        int semanaDis = 0;
        try {
            if (saldo.getNumeroCuotasTranscurridas() < ClientesConstants.DISPERSION_SEMANA_2) {
                semanaDis = ClientesConstants.DISPERSION_SEMANA_2;
            } else if (saldo.getNumeroCuotasTranscurridas() < ClientesConstants.DISPERSION_SEMANA_4) {
                semanaDis = ClientesConstants.DISPERSION_SEMANA_4;
            }
        } catch (Exception e) {

            myLogger.error("InterCicloHelper.semanaDispersion", e);
            throw new ClientesException(e.getMessage());
        }
        return semanaDis;
    }

    public static int semanaDispersionDesembolso(SaldoIBSVO saldo) throws ClientesException {
        int semanaDis = 0;
        try {
            if (saldo.getNumeroCuotasTranscurridas() <= ClientesConstants.DISPERSION_SEMANA_2) {
                semanaDis = ClientesConstants.DISPERSION_SEMANA_2;
            } else if (saldo.getNumeroCuotasTranscurridas() <= ClientesConstants.DISPERSION_SEMANA_4) {
                semanaDis = ClientesConstants.DISPERSION_SEMANA_4;
            }
        } catch (Exception e) {

            myLogger.error("InterCicloHelper.semanaDispersion", e);
            throw new ClientesException(e.getMessage());
        }
        return semanaDis;
    }

    public static int semanaDispersion(SaldoIBSVO saldoVO, CicloGrupalVO cicloVO) throws ClientesException {

        int semanaDis = 0;
        int clientesICDesa = 0;
        ArrayList<ClienteIntercicloVO> clientesIC = new ArrayList<ClienteIntercicloVO>();
        ClienteIntercicloDAO clienteICDao = new ClienteIntercicloDAO();
        try {
            clientesIC = clienteICDao.getClientesInterCiclo(cicloVO.idGrupo, cicloVO.idCiclo);
            if (saldoVO.getNumeroCuotasTranscurridas() == ClientesConstants.DISPERSION_SEMANA_2 && cicloVO.getEstatusIC() == ClientesConstants.CICLO_DESEMBOLSO) {
                myLogger.debug("Caso Semana 2 Estaus Desembolso");
                semanaDis = ClientesConstants.DISPERSION_SEMANA_2;
            } else if (saldoVO.getNumeroCuotasTranscurridas() < ClientesConstants.DISPERSION_SEMANA_2 && cicloVO.getEstatusIC() > ClientesConstants.CICLO_CERRADO) {
                myLogger.debug("Caso Semana 2");
                semanaDis = ClientesConstants.DISPERSION_SEMANA_2;
            } else if (saldoVO.getNumeroCuotasTranscurridas() == ClientesConstants.DISPERSION_SEMANA_4 && cicloVO.getEstatusIC2() == ClientesConstants.CICLO_DESEMBOLSO) {
                myLogger.debug("Caso Semana 4 Estaus Desembolso");
                semanaDis = ClientesConstants.DISPERSION_SEMANA_4;

            } else if (saldoVO.getNumeroCuotasTranscurridas() < ClientesConstants.DISPERSION_SEMANA_4) {
                myLogger.debug("Caso Semana 4");
                semanaDis = ClientesConstants.DISPERSION_SEMANA_4;
                if (cicloVO.getEstatusIC() > ClientesConstants.CICLO_CERRADO && saldoVO.getNumeroCuotasTranscurridas() > ClientesConstants.DISPERSION_SEMANA_2) {
                    if (clientesIC.size() > 0) {
                        clientesICDesa = desactivaClienteIntercilo(clientesIC, ClientesConstants.DISPERSION_SEMANA_2);
                        myLogger.debug("Se desactivaron " + clientesICDesa + " integratnes de Inter-Ciclo");
                    }
                    myLogger.debug("Se verifica la limpieza");
                }
                if ((cicloVO.getEstatusIC2() < ClientesConstants.CICLO_CERRADO && clientesIC.size() == 0)||cicloVO.getEstatusIC2() ==0) {
                    semanaDis = 0;
                    myLogger.debug("Semana Dispersada o sin intericlo");
                }
            } else if (saldoVO.getNumeroCuotasTranscurridas() > ClientesConstants.DISPERSION_SEMANA_4) {
                myLogger.debug("Se paso de las cuotas");
                if (cicloVO.getEstatusIC2() > ClientesConstants.CICLO_CERRADO) {
                    if (clientesIC.size() > 0) {
                        clientesICDesa = desactivaClienteIntercilo(clientesIC, ClientesConstants.DISPERSION_SEMANA_4);
                        myLogger.debug("Se desactivaron " + clientesICDesa + " integratnes de Inter-Ciclo");
                    }
                    myLogger.debug("Se verifica la limpieza");
                }
            }
        } catch (Exception e) {

            myLogger.error("semanaDispersion", e);
            throw new ClientesException(e.getMessage());
        }
        return semanaDis;
    }

    public static boolean validaPorcentajeClientesIC(SaldoIBSVO saldo) throws ClientesException {
        boolean valido = false;
        int cantIntegrantesIC = 0;
        int cantIntegrantes = 0;
        int i, j = 0;
        IntegranteCicloDAO integrantesDao = new IntegranteCicloDAO();
        ClienteIntercicloDAO clientesICdao = new ClienteIntercicloDAO();
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        ArrayList<ClienteIntercicloVO> clientesIC = new ArrayList<ClienteIntercicloVO>();
        try {
            clientesIC = clientesICdao.getClientesInterCiclo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            integrantesCiclo = integrantesDao.getIntegrantesCicloActivo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            cantIntegrantesIC = clientesIC.size() + 1;
            cantIntegrantes = integrantesCiclo.size();
            for (i = 0; i < clientesIC.size(); i++) {
                if (clientesIC.get(i).getEstatus() == 2) {
                    cantIntegrantesIC--;
                }
                for (j = 0; j < integrantesCiclo.size(); j++) {
                    if (integrantesCiclo.get(j).idCliente == clientesIC.get(i).getNumCliente() && clientesIC.get(i).getEstatus() == 1) {
                        cantIntegrantes--;
                    }
                }
            }
            double maxInterCiclo = Math.ceil(cantIntegrantes * .4);
            if (maxInterCiclo >= cantIntegrantesIC) {
                valido = true;
            }
        } catch (Exception e) {
            myLogger.error("InterCicloHelper.validaPorcentajeClientesIC", e);
            throw new ClientesException(e.getMessage());
        }
        return valido;
    }

    public static boolean verificaExiteIntegranteIC(ClienteIntercicloVO clienteIC) throws ClientesException {
        boolean existe = false;
        ClienteIntercicloVO integranteICexistente = new ClienteIntercicloVO();
        ClienteIntercicloDAO clienteICdao = new ClienteIntercicloDAO();
        try {
            integranteICexistente = clienteICdao.getClienteICporSemDisp(clienteIC);
            if ((integranteICexistente.getNumCliente() > 0)) {
                existe = true;
            } else {
                existe = false;
            }
        } catch (Exception e) {
            myLogger.error("InterCicloHelper.validaPorcentajeClientesIC", e);
            throw new ClientesException(e.getMessage());
        }

        return existe;

    }

    public static boolean validaCambioEstatus(int semDisp, int estatus, int idGrupo, int idCredito, Date[] fechasInhabiles, Date fechaDisp) throws ClientesException {

        boolean valido = false;
        int diasDif = 0;
        Date fechaActual = Calendar.getInstance().getTime();
        try {
            if (fechaDisp.after(fechaActual)) {
                do {
                    if (!FechasUtil.esDiaInhabil(fechaActual, fechasInhabiles)) {
                        diasDif++;
                    }
                    fechaActual = FechasUtil.getRestarDias(fechaActual, -1);
                } while (FechasUtil.inBetweenDays(fechaActual, fechaDisp) != 0);
                if (estatus == ClientesConstants.CICLO_ANALISIS || estatus == ClientesConstants.CICLO_APERTURA ) {
                    if (diasDif >= 2) {
                        valido = true;
                    }
                } else if (estatus == ClientesConstants.CICLO_AUTORIZADO || estatus == ClientesConstants.CICLO_PROCESO
                        || estatus == ClientesConstants.CICLO_PENDIENTE || estatus == ClientesConstants.CICLO_RECHAZADO
                        || estatus == ClientesConstants.CICLO_REVALORACION || estatus == ClientesConstants.CICLO_ASIGNADO) {
                    if (diasDif >= 1) {
                        valido = true;
                    }
                }
                else if (estatus == ClientesConstants.CICLO_PARADESEMBOLSAR || estatus == ClientesConstants.CICLO_DESEMBOLSO){
                    if (diasDif >=0) {
                        valido = true;
                    }
                }
            } else {
                while (FechasUtil.inBetweenDays(fechaDisp, fechaActual) != 0){
                    if (!FechasUtil.esDiaInhabil(fechaDisp, fechasInhabiles)) {
                        diasDif++;
                    }
                    fechaDisp = FechasUtil.getRestarDias(fechaDisp, -1);
                    System.out.println("fechaDisp " + fechaDisp);
                }
                if (estatus == ClientesConstants.CICLO_DISPERSADO) {
                    if (diasDif <= 3) {
                        return true;
                    }
                }else if (estatus == ClientesConstants.CICLO_PARADESEMBOLSAR || estatus == ClientesConstants.CICLO_DESEMBOLSO){
                    if (diasDif ==0) {
                        valido = true;
                    }
                }
            }
        } catch (Exception e) {
            myLogger.error("Error en validaCambioEstatus ", e);
            throw new ClientesException(e.getMessage());
        }
        return valido;
    }

    public static int desactivaClienteIntercilo(ArrayList<ClienteIntercicloVO> clientesIC, int SemDips) throws ClientesException {

        int clienteDesactivados = 0;
        int estatus = 0;
        SolicitudVO solicitudVO = null;
        SolicitudDAO solicitusDAO = new SolicitudDAO();
        ClienteIntercicloDAO clienteICDAO = new ClienteIntercicloDAO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        try {
            for (int i = 0; i < clientesIC.size(); i++) {
                if (SemDips == clientesIC.get(i).getSemDispercion() && clientesIC.get(i).getEstatus() == 1) {
                    solicitudVO = new SolicitudVO();
                    solicitudVO = solicitusDAO.getSolicitud(clientesIC.get(i).getNumCliente(), clientesIC.get(i).getNumSolicitud());
                    clientesIC.get(i).setEstatus(2);
                    solicitudVO.subproducto = 0;
                    solicitudVO.desembolsado = 1;
                    solicitusDAO.updateSolicitud(clientesIC.get(i).getNumCliente(), solicitudVO);
                    clienteICDAO.updateIntegranteInterCiclo(null, clientesIC.get(i));
                    clienteDesactivados++;

                }
            }
        } catch (Exception e) {
            myLogger.error("Error en desactivaClienteIntercilo ", e);
            throw new ClientesException(e.getMessage());
        }
        return clienteDesactivados;
    }

    public static boolean validaDevolicionODP(IntegranteCicloVO[] IntegrantesCicloVO) throws ClientesException {
        OrdenDePagoVO ordenPagoVO = null;
        OrdenDePagoDAO ordenPagoDAO = new OrdenDePagoDAO();
        boolean valido = true;
        int i = 0;
        try {
            for (i = 0; i < IntegrantesCicloVO.length; i++) {
                if (IntegrantesCicloVO[i].estatus == 2) {
                    ordenPagoVO = ordenPagoDAO.getOrdenPago(IntegrantesCicloVO[i].idCliente, IntegrantesCicloVO[i].idSolicitud);
                    if (ordenPagoVO.getEstatus() != ClientesConstants.OP_DEVUELTA) {
                        valido = false;
                    }
                }
            }
        } catch (Exception e) {
            myLogger.error("Error en desactivaClienteIntercilo ", e);
            throw new ClientesException(e.getMessage());
        }
        return valido;
    }
    
    public static double cambiaFondeador (SaldoIBSVO saldo, CreditoCartVO creditoVO, CicloGrupalVO cicloVO, Connection con, Connection conCar, int semDisp, HttpServletRequest request) throws ClientesException{
        SaldoIBSDAO saldoIbsDAO = new SaldoIBSDAO();        
        CreditoCartDAO creditoDAO =new CreditoCartDAO();
        double intDeveng = 0;
        double saldoCapital = 0, saldoInteres = 0, saldoIvaInteres =0;
        BitacoraUtil bitacora = null;
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        TablaDevengamientoHelper tblDevenHelper = new TablaDevengamientoHelper();
        TablaAmortVO[] tablaAmortCart = null;
        TablaAmortDAO tablaCartDAO = new TablaAmortDAO();
        CicloGrupalDAO cicloGruplaDAO = new CicloGrupalDAO();
        myLogger.debug("ingresa a cambio de fondeador");
        try {
            tablaAmortCart = tablaCartDAO.getElementos(cicloVO.getIdGrupo(), cicloVO.getIdCreditoIBS(), 0);
            for (int i = 0;i<tablaAmortCart.length; i++){
                if (tablaAmortCart[i].pagado.equals("N")){
                    saldoCapital+= (tablaAmortCart[i].abonoCapital - tablaAmortCart[i].capitalPagado);
                    saldoInteres+= tablaAmortCart[i].interes - tablaAmortCart[i].interesPagado;
                    saldoIvaInteres += tablaAmortCart[i].ivaInteres - tablaAmortCart[i].ivaInteresPagado;
                }
            }
            //Parcial de devengameinto
            intDeveng = tblDevenHelper.devengadoParcial(semDisp,creditoVO,null);
            // Saldo capital
            ArrayList<RubrosVO> arrayCambioFondeador = new ArrayList<RubrosVO>();
            RubrosVO rubro = new RubrosVO();
            RubrosVO elementos[] = null;
            rubro.tipoRubro = ClientesConstants.CAPITAL;
            rubro.monto = saldoCapital;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            arrayCambioFondeador.add(rubro);
            //Saldo interes
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.INTERES;
            rubro.monto = saldoInteres;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            arrayCambioFondeador.add(rubro);
            //Saldo Iva Interes
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.IVA_INTERES;
            rubro.monto = saldoIvaInteres;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            arrayCambioFondeador.add(rubro);
            //Efectivo
            rubro = new RubrosVO();
            rubro.tipoRubro = ClientesConstants.EFECTIVO;
            rubro.monto = saldoCapital + saldoInteres;
            rubro.status = ClientesConstants.RUBRO_VIGENTE;
            rubro.origen = 0;
            arrayCambioFondeador.add(rubro);
            if (intDeveng>0){
                rubro = new RubrosVO();
                rubro.tipoRubro = ClientesConstants.INTERES;
                rubro.monto = intDeveng;
                rubro.status = ClientesConstants.RUBRO_NO_VIGENTE;
                arrayCambioFondeador.add(rubro);                
            }
            if (arrayCambioFondeador.size() > 0) {
                elementos = new RubrosVO[arrayCambioFondeador.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (RubrosVO) arrayCambioFondeador.get(i);
                }
            }
            //Se registran transaciones de salida con el fondeador actual
            transacHelper.registraSalidaFondeador(conCar, creditoVO, elementos, saldoCapital);
            bitacora = new BitacoraUtil(cicloVO.idGrupo, request.getRemoteUser(), "cambiaFondeadorSalida");
            bitacora.registraEvento(cicloVO);
            //SE registan transaciones de traspaso de los itneres devengados 
            
            //Se setea el nuevo fondeador en el objeto 
            cicloVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
            creditoVO.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
            saldo.setFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL);
            //Se realiza la ctualizacion en base del nuevo fondeador
            saldoIbsDAO.updateOtrosDatos(con,saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP(),ClientesConstants.ID_FONDEADOR_CREDITO_REAL, 0);
            creditoDAO.updateCreditoFondeador(conCar,creditoVO);
            cicloGruplaDAO.updateCiclo(con, cicloVO);
            //Se registran transacciones de Entrada para el nuevo fondeador
            transacHelper.registraEntradaFondeador(conCar, creditoVO, elementos); 
            bitacora = new BitacoraUtil(cicloVO.idGrupo, request.getRemoteUser(), "cambiaFondeadorEntrada");
            bitacora.registraEvento(cicloVO);
        } catch (Exception e) {
            myLogger.error("aplicaCapitalGrupoInterciclo", e);
            throw new ClientesException(e.getMessage());
        }
        return saldoCapital;
    }
    

}

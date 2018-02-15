package com.sicap.clientes.helpers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.commands.CommandProcesarPagoReferenciado;
import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.IncidenciaSegurosDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.PagosExcedentesDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.RegistroPagosSegurosDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.CifraControlVO;
import com.sicap.clientes.vo.CuentaBancariaVO;
import com.sicap.clientes.vo.IncidenciaSegurosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoExcedenteVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.RegistroPagosSegurosVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SolicitudVO;
import org.apache.log4j.Logger;

public class ProcesaPagosReferenciadosHelper {

    public static Logger myLogger = Logger.getLogger(ProcesaPagosReferenciadosHelper.class);
    Notification[] notificaciones = new Notification[1];
    PagoReferenciadoDAO pr = new PagoReferenciadoDAO();
    TransaccionesHelper transacHelper = new TransaccionesHelper();
    EventoHelper eventoHelper = new EventoHelper();
    PagoVO pagosaldo = new PagoVO();
    CommandProcesarPagoReferenciado cmdPagoRef = new CommandProcesarPagoReferenciado();
//	SaldoT24VO verificaLiquidadoCancelado = null;
    SaldoIBSVO saldoIBSVO = null;
    CreditoCartVO creditoVO = new CreditoCartVO();
    CreditoCartDAO creditoDAO = new CreditoCartDAO();
    IncidenciaSegurosDAO incidenciasSegDAO = new IncidenciaSegurosDAO();
    double montoLiquidacion = 0.0;
    int caso1 = 0, caso2 = 0, caso3 = 0, caso4 = 0, caso5 = 0, caso6 = 0, caso7 = 0, caso8 = 0, caso9 = 0, caso10 = 0, caso11 = 0, caso12 = 0, caso13 = 0, caso14 = 0;
    double totalPrima = 0.0;
    double diferenciaEntrePagosSeguros = 0.0;
    int bancoReferencia = 0;
    PagosExcedentesDAO pagosExcedentes = new PagosExcedentesDAO();
    ArrayList<Double> excedentesArray = new ArrayList<Double>();
    RegistroPagosSegurosDAO registropagosegurodao = new RegistroPagosSegurosDAO();
    CifraControlVO cf = new CifraControlVO();
    ArrayList<PagoVO> incidenciasArray = new ArrayList<PagoVO>();
    ArrayList<PagoVO> pagosProcesadosArray = new ArrayList<PagoVO>();
    ArrayList<PagoVO> pagosInsuficienteArray = new ArrayList<PagoVO>();
    ArrayList<Double> segurosArray = new ArrayList<Double>();
    RegistroPagosSegurosVO registropagoseguros = new RegistroPagosSegurosVO();
    PagoExcedenteVO pe = new PagoExcedenteVO();
    SegurosVO contratoSeguro = new SegurosVO();
    SegurosDAO contratoSegurodao = new SegurosDAO();
    PagoReferenciadoDAO pagoReferenciado = new PagoReferenciadoDAO();
    PagoReferenciadoDAO updateIncidencias = new PagoReferenciadoDAO();
    String productosTQ = CatalogoHelper.getParametro("PRODUCTOS_TQ");
    PagoDAO pagocarteradao = new PagoDAO();
    ArrayList<PagoVO> totalPagosArray = new ArrayList<PagoVO>();
    CifraControlDAO cifraControlDAO = new CifraControlDAO();
    ArrayList<PagoVO> pagoscarteraparaenviar = new ArrayList<PagoVO>();
    Date fechaMov = new Date(Calendar.getInstance().getTimeInMillis());
    SaldoIBSDAO ibsdao = new SaldoIBSDAO();

    public String procesaPagos(PagoVO[] pagos, HttpServletRequest request) {
        String siguiente = "";
        double temporalmontopagocartera = 0.0;
        ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
        int pagoVerif = 0;
        try {
            if (pagos != null && pagos.length > 0) {
                PagoVO pagocarteraVO = null;
                IntegranteCicloVO[] integrantes = null;
                ReferenciaGeneralVO refGeneralVO = new ReferenciaGeneralVO();
                CuentaBancariaVO cuentaVO = new CuentaBancariaVO();
                creditoVO = creditoDAO.getCreditoReferencia(pagos[0].referencia);
                IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
                CuentasBancariasDAO cuentasDAO = new CuentasBancariasDAO();
                double totalVigente = 0;
                double montoTotal = 0;
                double montoGarantia = creditoVO.getMontoCuentaCongelada();
                double montoRegistro = 0;
                cf.fechaMov = fechaMov;
                int IdparaPolizaContable = cifraControlDAO.addCifraControl(cf);
                cf.idCifraControl = IdparaPolizaContable;
                //Se Recorre todos los pagos recibidos 
                for (int j = 0; j <= pagos.length - 1; j++) {
                    pagoVerif = 0;
                    pagocarteraVO = new PagoVO(pagos[j].referencia, pagos[j].monto, pagos[j].fechaPago, pagos[j].fechaHora, pagos[j].enviado, pagos[j].status, pagos[j].bancoReferencia, pagos[j].sucursal, pagos[j].numCuenta);
                    // poliza contable
                    myLogger.info("IdparaPolizaContable: " + IdparaPolizaContable);
                    if (pagos[j] != null) {
                        pagocarteraVO.setIdContable(IdparaPolizaContable);
                        temporalmontopagocartera = 0.0;
                        // Asignar el banco de referencia al pago del seguro y a excedentes					    	    					    	   
                        registropagoseguros.bancoReferencia = pagos[j].bancoReferencia;
                        registropagoseguros.idContabilidad = IdparaPolizaContable;
                        pe.bancoReferencia = pagos[j].bancoReferencia;
                        pe.idContabilidad = IdparaPolizaContable;
                        // Generar Id de Salida de Poliza Contable para Incidencias
                        pagos[j].idContablePolizaSalidaIncidencia = IdparaPolizaContable;
                        if (pagos[j].referencia.length() == 13) {
                            refGeneralVO = referenciaDAO.getReferenciaGeneral(pagos[j].referencia);
                            if(refGeneralVO != null) {
                                totalVigente = -1;
                                saldoIBSVO = cmdPagoRef.verificaLiquidadoCancelado(pagos[j].referencia);
                                totalVigente = cmdPagoRef.getSaldoInsolutoAlCompromiso(saldoIBSVO);
                                if (totalVigente != -1.0) {
                                    pagos[j].montoAbono = pagos[j].monto;
                                    montoTotal = pagos[j].monto;
                                    montoLiquidacion = saldoIBSVO.getSaldoTotalAlDia();
                                    if (saldoIBSVO.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO) {
                                        if (saldoIBSVO.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_CANCELADO) {
                                            pagoVerif = pagocarteradao.getPagoVO(pagos[j]);
                                            if (pagoVerif == 0) {
                                                if (GrupoUtil.esPagoGrupal(refGeneralVO.referencia)) {
                                                    pagocarteraVO.setDestino(1);
                                                    if (ibsdao.isCreditoIBS(pagocarteraVO.getReferencia()))
                                                        pagocarteraVO.setDestino(3);
                                                    integrantes = integranteDAO.getIntegrantes(refGeneralVO.numcliente, refGeneralVO.numSolicitud);
                                                    if (integrantes != null) {
                                                        myLogger.debug("Va a Matriz de Pagos");
                                                        if (GrupoUtil.matrizPagos(integrantes, pagos[j], request) == 0)
                                                            myLogger.debug("Matriz de Pagos Lista para :" + pagos[j].referencia);
                                                        else
                                                            myLogger.debug("Error en Matriz de Pagos para :" + pagos[j].referencia);
                                                    }
                                                }
                                                if (montoTotal > totalVigente) {
                                                    //Incidencias de excedente
                                                    pe.fechaMovimiento = pagos[j].fechaPago;
                                                    pe.referencia = pagos[j].referencia;
                                                    pe.montoExcedente = pagos[j].monto - totalVigente;
                                                    pe.numCliente = cmdPagoRef.getReferencia(pagos[j].referencia).numcliente;
                                                    pe.numCuentaContable = "";
                                                    pagosExcedentes.addPagoExcedente(pe);
                                                    excedentesArray.add(pe.montoExcedente);
                                                    pagocarteraVO.setMonto(pagos[j].monto);
                                                    pagosProcesadosArray.add(pagos[j]);
                                                    myLogger.info("Pago Excedente pero no tenéa seguro: " + pe.toString());
                                                    caso8++;
                                                    pagosaldo.status = 8;
                                                    pagocarteraVO.setStatus(8);
                                                    pagos[j].reprocesar = 2;
                                                } else {
                                                    pagocarteraVO.setMonto(pagos[j].monto);
                                                    pagosProcesadosArray.add(pagos[j]);
                                                    pagosInsuficienteArray.add(pagos[j]);
                                                    myLogger.info("Pago Insuficiente");
                                                    caso6++;
                                                    pagosaldo.status = 6;
                                                    pagocarteraVO.setStatus(6);
                                                    pagos[j].reprocesar = 2;
                                                }
                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                pagoscarteraparaenviar.add(pagocarteraVO);
                                                pagocarteraVO.setSucursal(pagos[j].referencia.substring(0, 3));
                                                pagocarteraVO.setFechaHora(new Timestamp(System.currentTimeMillis()));
                                                pagocarteraVO.setEnviado(1);
                                                if(creditoVO.getMontoCuenta() >= creditoVO.getMontoCuentaCongelada()){
                                                    pagocarteraVO.setDestino(1);
                                                    pagocarteraVO.setMonto(pagos[j].monto);
                                                    transacHelper.registraPago(creditoVO, pagocarteraVO);
                                                }else{
                                                    if(pagocarteraVO.getMonto() <= (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta()))
                                                        transacHelper.registraPagoGarantia(creditoVO, pagocarteraVO, true);
                                                    else{
                                                        pagocarteraVO.setMonto(creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta());
                                                        transacHelper.registraPagoGarantia(creditoVO, pagocarteraVO, false);
                                                        pagocarteraVO.setMonto(pagos[j].monto - (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta()));
                                                        transacHelper.registraPago(creditoVO, pagocarteraVO);
                                                    }
                                                    pagocarteraVO.setDestino(3);
                                                    pagocarteraVO.setMonto(pagos[j].monto);
                                                }
                                                creditoVO.setMontoCuenta(pagos[j].monto + creditoVO.getMontoCuenta());
                                                creditoDAO.updatePagoCreditoCierre(creditoVO);
                                                eventoHelper.registraPago(creditoVO, pagocarteraVO);
                                                pagocarteradao.insertPagoCartera(pagocarteraVO);
                                                new PagoManualDAO().insertPago(pagocarteraVO);
                                            } else {
                                                //Causa incidencia, Pago Repetido
                                                incidenciasArray.add(pagos[j]);
                                                myLogger.info("Pago Repetido");
                                                caso6++;
                                                pagosaldo.status = 6;
                                                pagos[j].reprocesar = 1;
                                                pagos[j].tipo = "6";
                                                pagos[j].observaciones = "Pago Repetido";
                                                updateIncidencias.updateIncidencias(pagos[j]);
                                            }
                                        } else {
                                            //Causa incidencia, Cr�dito Cancelado
                                            incidenciasArray.add(pagos[j]);
                                            myLogger.info("Crédito Cancelado");
                                            caso5++;
                                            pagosaldo.status = 5;
                                            pagos[j].reprocesar = 1;
                                            pagos[j].tipo = "5";
                                            pagos[j].observaciones = "Crédito Cancelado";
                                            updateIncidencias.updateIncidencias(pagos[j]);
                                        }
                                    } else {
                                        //Causa incidencia, Cr�dito Liquidado
                                        incidenciasArray.add(pagos[j]);
                                        myLogger.info("Crédito Liquidado");
                                        caso4++;
                                        pagosaldo.status = 4;
                                        pagos[j].reprocesar = 1;
                                        pagos[j].tipo = "4";
                                        pagos[j].observaciones = "Crédito Liquidado";
                                        updateIncidencias.updateIncidencias(pagos[j]);
                                    }
                                } else {
                                    //Causa incidencia, Referencia encontrada, Saldo no encontrado
                                    incidenciasArray.add(pagos[j]);
                                    myLogger.info("Saldo No Encontrado");
                                    caso3++;
                                    pagosaldo.status = 3;
                                    pagos[j].reprocesar = 1;
                                    pagos[j].tipo = "3";
                                    pagos[j].observaciones = "Saldo No Encontrado";
                                    updateIncidencias.updateIncidencias(pagos[j]);
                                }
                            } else {
                                //Causa incidencia, Referencia No Encontrada
                                incidenciasArray.add(pagos[j]);
                                myLogger.info("Referencia No Encontrada");
                                caso2++;
                                pagosaldo.status = 2;
                                pagos[j].reprocesar = 1;
                                pagos[j].tipo = "2";
                                pagos[j].observaciones = "Referencia No Encontrada";
                                updateIncidencias.updateIncidencias(pagos[j]);
                            }
                        } else {
                            //Causa Incidencia, Referencia No Valida
                            incidenciasArray.add(pagos[j]);
                            myLogger.info("Referencia No Válida");
                            caso1++;
                            pagosaldo.status = 1;
                            pagos[j].reprocesar = 1;
                            pagos[j].tipo = "1";
                            pagos[j].observaciones = "Referencia No Válida";
                            updateIncidencias.updateIncidencias(pagos[j]);
                        }
                        //  Cifras Control, guardar todos los pagos recibidos (procesados o no)
                        totalPagosArray.add(pagos[j]);

                        // pagos a la BD *** ESTE YA NO DEBER�A INSERTAR A LA BD.....
                        pagosaldo.sucursal = pagos[j].referencia.substring(0, 3);
                    }
                }
                // NOTA: En los pagos procesados la aplicaci�n est� descontando la prima
                double pagos_normales = cmdPagoRef.getMontoTotalArchivo(pagosProcesadosArray) - cmdPagoRef.getMontoTotalArchivoExcedentes(excedentesArray);
                myLogger.info("********RESUMEN*********");

                cf.fechaMov = fechaMov;
                cf.registrosIntroducidos = totalPagosArray.size();
                cf.registrosProcesados = pagosProcesadosArray.size();
                cf.registrosNoProcesados = incidenciasArray.size();
                cf.seguros = segurosArray.size();

                cf.montosIntroducidos = cmdPagoRef.getMontoTotalArchivo(totalPagosArray) + totalPrima;
                cf.montosProcesados = cmdPagoRef.getMontoTotalArchivo(pagosProcesadosArray) + totalPrima;
                cf.montosNoProcesados = cmdPagoRef.getMontoTotalArchivo(incidenciasArray);
                cf.montosAplicadosaCredito = pagos_normales;
                cf.montosAplicadosaSeguros = totalPrima;
                cf.montosdeExcedentes = cmdPagoRef.getMontoTotalArchivoExcedentes(excedentesArray);

                myLogger.info("REGISTROS INTRODUCIDOS: " + totalPagosArray.size()); // TODOS LOS PAGOS
                myLogger.info("REGISTROS PROCESADOS: " + pagosProcesadosArray.size()); // PAGOS INCLUYENDO PAGO INSUFICIENTE Y PAGO EXCEDENTE
                myLogger.info("REGISTROS NO PROCESADOS: " + incidenciasArray.size()); // PAGOS CON REF NO ENCONTRADA,SALDO NO ENCONTRADO,CREDITO CANCELADO,CREDITO LIQUIDADO
                myLogger.info("MONTOS INTRODUCIDOS: " + cf.montosIntroducidos);
                myLogger.info("MONTOS PROCESADOS: " + cf.montosProcesados);
                myLogger.info("MONTOS NO PROCESADOS: " + cf.montosNoProcesados);
                myLogger.info("MONTOS APLICADOS A CRÉDITO: " + cf.montosAplicadosaCredito);
                myLogger.info("MONTOS APLICADOS A SEGUROS: " + cf.montosAplicadosaSeguros);
                myLogger.info("MONTOS DE EXCEDENTES: " + cf.montosdeExcedentes);
                myLogger.info("------ Otros Datos -----------");
                myLogger.info("REGISTROS SEGUROS: " + segurosArray.size());
                myLogger.info("------ Pagos No Procesados Por Caso -----------");
                myLogger.info("Referencia No Valida: " + caso1);
                myLogger.info("Referencia No Encontrada: " + caso2);
                myLogger.info("Saldo No Encontrado: " + caso3);
                myLogger.info("Crédito Liquidado: " + caso4);
                myLogger.info("Crédito Cancelado: " + caso5);
                myLogger.info("------ Pagos Procesados Por Caso -----------");
                myLogger.info("Pago insuficiente: " + caso6);
                myLogger.info("Pago normal al crédito, no tiene seguro: " + caso7);
                myLogger.info("Pago Excedente pero no tenía seguro: " + caso8);
                myLogger.info("Tiene seguro, pero sólo cubre el crédito exacto: " + caso9);
                myLogger.info("Pago excedente al crédito pero no alcanza a cubrir el seguro: " + caso10);
                myLogger.info("Pago normal al crédito y 90% al seguro: " + caso11);
                myLogger.info("Pago normal al crédito y al seguro: " + caso12);
                myLogger.info("Pago Excedente al crédito y con seguro: " + caso13);
                myLogger.info("Pago tipo EXCEL CARTERA: " + caso14);

                // cifra control
                cf.situacionPago = 1;
                cifraControlDAO.updateCifraControl(cf);

                //  TERMINZA EL COPY DE RESUMEN DE PAGOS
//				*********************************
                myLogger.info("Se reprocesaron  pagos");
            } else {
                myLogger.info("No hay pagos para el Reproceso");
            }

        } catch (Exception exc) {
            myLogger.error("Exception caught in ProcesaPagosReferenciadosHelper" + exc.getMessage());
        } finally {
        }
        return siguiente;

    }

    public static String displayNotifications(Notification[] notificaciones) {

        String notificacion = "";
        Notification not = null;

        for (int c = 0; notificaciones != null && c < notificaciones.length; c++) {
            not = (Notification) notificaciones[c];
            if (not.type == ClientesConstants.ERROR_TYPE) {
                notificacion += "<b><font color='" + ClientesConstants.ERROR_COLOR + "'>" + not.text + "</font><b><br><br>";
            } else {
                notificacion += "<b><font color='" + ClientesConstants.INFO_COLOR + "'>" + not.text + "</font></b><br><br>";
            }
        }
        return notificacion;

    }
}

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.IncidenciaSegurosDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.PagosExcedentesDAO;
import com.sicap.clientes.dao.RegistroPagosSegurosDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CifraControlVO;
import com.sicap.clientes.vo.IncidenciaSegurosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoExcedenteVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.RegistroPagosSegurosVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import org.apache.log4j.Logger;

public class CommandReprocesarPagos extends DAOMaster implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandReprocesarPagos.class);

    public CommandReprocesarPagos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];

        PagoReferenciadoDAO pr = new PagoReferenciadoDAO();
        PagoVO pagosaldo = new PagoVO();
        PagoVO pagoGar = null;
        CommandProcesarPagoReferenciado cmdPagoRef = new CommandProcesarPagoReferenciado();
//		SaldoT24VO verificaLiquidadoCancelado = null;
        SaldoIBSVO saldoIBSVO = null;
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
        CreditoCartVO creditoVO = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        EventoHelper eventoHelper = new EventoHelper();
        CatalogoDAO catalogoDAO = new CatalogoDAO();

        try {
            if (!catalogoDAO.ejecutandoCierre()) {
                // Obtener primero las incidencias del JSP para procesar			
                String[] IdIncidenciasParaReprocesar = request.getParameterValues("Incidencias");

                PagoVO[] pagos = new PagoVO[IdIncidenciasParaReprocesar.length];
                for (int i = 0; i < IdIncidenciasParaReprocesar.length; i++) {
                    pagos[i] = pr.getPagoAReprocesar(IdIncidenciasParaReprocesar[i]);
                    //  Usuario que reprocesa el pago
                    pagos[i].usuarioReproceso = request.getRemoteUser().toString();
                    // Comentarios de reproceso para cada pago
                    pagos[i].comentarios = request.getParameter(IdIncidenciasParaReprocesar[i] + "comentarios");
                    // Comentarios de reproceso para cada pago
                    pagos[i].nuevareferencia = request.getParameter(IdIncidenciasParaReprocesar[i]);
                }
                if (pagos != null && pagos.length > 0) {
//				enviar el objeto pagos tipo PAGOVO.. traer todo lo de la tabla de pagos						  				   
                    //  COMIENZA EL COPY DE COMMANDPROCESARPAGOREFERENCIADO 
                    //   *****************************
                    PagoVO[] pagocartera = pagos;
                    // Cifra de Control para la Póliza Contable
                    cf.fechaMov = fechaMov;
                    cifraControlDAO.addCifraControl(cf);
                    int IdparaPolizaContable = cifraControlDAO.getIdCifraControl();
                    cf.idCifraControl = IdparaPolizaContable;
                    //Se Recorre todos los pagos recibidos 
                    for (int j = 0; j <= pagos.length - 1; j++) {
                        // poliza contable
                        myLogger.info("IdparaPolizaContable: " + IdparaPolizaContable);
                        if (pagos[j] != null) {
                            // Si hay nueva referencia, tomar ésta
                            if (pagos[j].nuevareferencia != null && !pagos[j].nuevareferencia.equals("")) {
                                pagocartera[j].referencia = pagos[j].nuevareferencia;
                                pagos[j].referencia = pagos[j].nuevareferencia;
                            } else {
                                pagocartera[j].referencia = pagos[j].referencia;
                            }
                            // Los Pagos a Cartera WIN
                            pagocartera[j].fechaPago = pagos[j].fechaPago;
                            pagocartera[j].fechaHora = pagos[j].fechaHora;
                            pagocartera[j].bancoReferencia = pagos[j].bancoReferencia;
                            pagocartera[j].idContable = IdparaPolizaContable;
                            pagocartera[j].monto = pagos[j].monto;
                            double temporalmontopagocartera = 0.0;
                            // Asignar el banco de referencia al pago del seguro y a excedentes					    	    					    	   
                            registropagoseguros.bancoReferencia = pagos[j].bancoReferencia;
                            registropagoseguros.idContabilidad = IdparaPolizaContable;
                            pe.bancoReferencia = pagos[j].bancoReferencia;
                            pe.idContabilidad = IdparaPolizaContable;
                            // Generar Id de Salida de Poliza Contable para Incidencias
                            pagos[j].idContablePolizaSalidaIncidencia = IdparaPolizaContable;
                            if (!pagos[j].comentarios.toUpperCase().replace(" ", "").trim().equals("EXCELCARTERA")) {
//							Se valida que la referencia sea de 13 caracteres
                                if (pagos[j].referencia.length() == 13) {
								// situacionreferencia = 0    referencia no existe
                                    // situacionreferencia = 1    referencia de referencias generales
                                    // situacionreferencia = 2    referencia alterna
                                    int situacionreferencia = cmdPagoRef.existeReferencia(pagos[j].referencia);
                                    if (situacionreferencia != 0) {
                                        myLogger.info("**********REFERENCIA ENCONTRADA********");
                                        double totalVigente = -1;
                                        double prima = 0.0;
                                        if (situacionreferencia == 1) {
                                            myLogger.info("En Referencias Generales");
                                        } else if (situacionreferencia == 2) {
                                            PagoVO refAlterna = pagoReferenciado.getReferenciaAlterna(pagos[j]);
                                            pagos[j].referencia = refAlterna.referencia;
                                            myLogger.info("En Referencias Alternas");
                                        }
                                        myLogger.info("Referencia: " + pagos[j].referencia);
                                        // Se verifica que exista el saldo, si no causa incidencia
                                        myLogger.info("Ir a Saldos para obtener total vigente");
                                        saldoIBSVO = cmdPagoRef.verificaLiquidadoCancelado(pagos[j].referencia);
                                        totalVigente = cmdPagoRef.getSaldoInsolutoAlCompromiso(saldoIBSVO);
                                        if (!(totalVigente == -1.0)) {
                                            myLogger.info("Existe el saldo");
                                            myLogger.info("TOTAL VIGENTE ES : " + totalVigente);
                                            myLogger.info("El pago fue de: " + pagos[j].monto);
                                            pagos[j].montoAbono = pagos[j].monto;
                                            double montoTotal = pagos[j].monto;
										// Obtener el monto de liquidacion
                                            // Se cambian por variables de saldos
//										montoLiquidacion = verificaLiquidadoCancelado.interesesDevNoCobrados;
                                            montoLiquidacion = saldoIBSVO.getSaldoTotalAlDia();
                                            myLogger.info("Monto de Liquidación: " + montoLiquidacion);
                                            //	 Verificar si está liquidado, si no causa incidencia
//										if(!(verificaLiquidadoCancelado.situacionActualCredito==3)){    	        
                                            if (!(saldoIBSVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO)) {
                                                // Verificar si está cancelado, si no causa incidencia		      	       
//											if(!(verificaLiquidadoCancelado.situacionActualCredito==4)){  	  
                                                if (!(saldoIBSVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_CANCELADO)) {
                                                    //	*-   OBTENIENDO DATOS DE SEGURO INDIVIDUAL Y/O GRUPAL
                                                    prima = cmdPagoRef.getPrimaSeguro(pagos[j].referencia);
                                                    ReferenciaGeneralVO refVo = cmdPagoRef.getReferencia(pagos[j].referencia);
                                                    // Si es pago individual validar si tiene seguro
                                                    if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                        //	 Pago Individual: Obtener tipo de producto														
                                                        SolicitudDAO solicitud = new SolicitudDAO();
                                                        SolicitudVO solVO = new SolicitudVO();
                                                        int tipoProducto = 0;
                                                        solVO = solicitud.getSolicitud(refVo.numcliente, refVo.numSolicitud);
                                                        if (solVO != null) {
                                                            tipoProducto = solVO.tipoOperacion;
                                                        }
//													myLogger.info("ID de Producto: "+tipoProducto);
                                                        pagocartera[j].destino = 1;
													// validando si es producto TQ; 
                                                        // 1 para cartera WIN
                                                        // 2 para cartera TQ
                                                        String[] buscarTQ = productosTQ.split(",");
                                                        for (int i = 0; i < buscarTQ.length; i++) {
                                                            if (buscarTQ[i].equals(String.valueOf(tipoProducto))) {
                                                                // Producto = 2, es Cartera TQ
                                                                pagocartera[j].destino = 2;

                                                            }
                                                        }
                                                        if (ibsdao.isCreditoIBS(pagocartera[j].referencia)) {
                                                            pagocartera[j].destino = 3;
                                                        }
                                                        contratoSeguro = contratoSegurodao.getSeguros(refVo.numcliente, refVo.numSolicitud);
                                                        //   objeto de pago seguro para individual	 			
                                                        registropagoseguros.idcliente = refVo.numcliente;
                                                        registropagoseguros.numsolicitud = refVo.numSolicitud;
                                                        registropagoseguros.referencia = pagos[j].referencia;

                                                        if (contratoSeguro == null || contratoSeguro.contratacion == 2 || contratoSeguro.primaTotal != 0) {
                                                            myLogger.info("¡No Contrató Seguro Individual!");
                                                        } else {
                                                            myLogger.info("La Prima Individual es: " + prima);
                                                        }
                                                        // Si es pago grupal validar si alguno de los integrantes tiene seguro
                                                    } else {
                                                        //	 Pago Grupal: CARTERA WIN ID:1
                                                        pagocartera[j].destino = 3;
                                                        if (ibsdao.isCreditoIBS(pagocartera[j].referencia)) {
                                                            creditoVO = creditoDAO.getCreditoReferencia(pagocartera[j].referencia);
                                                            if(creditoVO.getMontoCuenta() >= creditoVO.getMontoCuentaCongelada()){
                                                                pagocartera[j].setDestino(1);
                                                                pagocartera[j].setEnviado(0);
                                                            }else{
                                                                pagoGar = new PagoVO(pagocartera[j].getMonto(), pagocartera[j].getBancoReferencia());
                                                                if(pagocartera[j].getMonto() <= (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta()))
                                                                    transacHelper.registraPagoGarantia(creditoVO, pagocartera[j], true);
                                                                else{
                                                                    pagocartera[j].setMonto(creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta());
                                                                    transacHelper.registraPagoGarantia(creditoVO, pagocartera[j], false);
                                                                    pagocartera[j].setMonto(pagoGar.getMonto() - (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta()));
                                                                    transacHelper.registraPago(creditoVO, pagocartera[j]);
                                                                }
                                                                creditoVO.setMontoCuenta(pagoGar.getMonto() + creditoVO.getMontoCuenta());
                                                                creditoDAO.updatePagoCreditoCierre(creditoVO);
                                                                pagocartera[j].setDestino(3);
                                                                pagocartera[j].setEnviado(1);
                                                                pagocartera[j].setMonto(pagoGar.getMonto());
                                                                eventoHelper.registraPago(creditoVO, pagocartera[j]);
                                                            }
                                                            /*if (creditoVO.getMontoCuenta() >= creditoVO.getMontoCuentaCongelada()) {
                                                                pagocartera[j].destino = 1;
                                                            } //if(pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_EFECTIVO && pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_SEGUROS && pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_TRANSFERENCIA)
                                                            //transacHelper.registraPago(creditoVO, pagocartera[j]);
                                                            else {
                                                                creditoVO.setMontoCuenta(pagocartera[j].monto + creditoVO.getMontoCuenta());
                                                                creditoDAO.updatePagoCreditoCierre(creditoVO);
                                                                pagocartera[j].enviado = 1;
                                                                                                                //if(pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_EFECTIVO && pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_SEGUROS && pagocartera[j].bancoReferencia!=ClientesConstants.ID_BANCO_TRANSFERENCIA)
                                                                //transacHelper.registraPago(creditoVO, pagocartera[j]);
                                                            }*/
                                                        }
                                                        //myLogger.info("Producto es "+pagocartera[j].producto+ " para "+pagos[j].referencia);
                                                        PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                        IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                        if (integrantes != null) {
                                                            GrupoUtil matriz = new GrupoUtil();
                                                            myLogger.info("Va a la Matriz");
                                                            int statusMatrizPagos = matriz.matrizPagos(integrantes, pagos[j], request);
                                                            if (statusMatrizPagos == 0) {
                                                                myLogger.info("Matriz de Pagos Lista para :" + pagos[j].referencia);
                                                            } else if (statusMatrizPagos == 1) {
                                                                myLogger.info("Error en Matriz de Pagos para :" + pagos[j].referencia);
                                                            }
                                                            for (int i = 0; i < integrantes.length; i++) {
                                                                contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
															// buscar que por lo menos uno tenga seguro 
                                                                // con esto dejo el objeto contratoSeguro con datos para validar abajo 
                                                                if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (prima == 0) {
                                                            myLogger.info("¡No Contrató Seguro Grupal!" + prima);
                                                        } else {
                                                            myLogger.info("La Prima Grupal es: " + prima);
                                                        }
                                                    }
                                                    // Verifica si el pago realizado es mayor al pago vigente, si es menor causa incidencia
                                                    if (pagos[j].monto >= totalVigente) {
                                                        // Casos cuando tiene seguro....
                                                        if (((cmdPagoRef.tieneSeguro(pagos[j].referencia) || GrupoUtil.esPagoGrupal(pagos[j].referencia)) && (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0)) && contratoSeguro.saldoInsoluto > 0) {
                                                            if ((montoTotal - totalVigente) > prima) {
                                                                // Incidencias de excedente
                                                                pe.fechaMovimiento = pagos[j].fechaPago;
                                                                pe.referencia = pagos[j].referencia;
                                                                pe.montoExcedente = pagos[j].monto - totalVigente - prima;
                                                                pe.numCliente = cmdPagoRef.getReferencia(pagos[j].referencia).numcliente;
                                                                pe.numCuentaContable = "";
                                                                pagosExcedentes.addPagoExcedente(pe);
                                                                // Pago al seguro individual 
                                                                if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                                    if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                        registropagoseguros.montoCubierto = (prima);
                                                                        temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                        registropagoseguros.montoporCubrir = 0;
                                                                        registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                        registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                        // cifras control de prima
                                                                        segurosArray.add(prima);
                                                                        totalPrima += prima;
                                                                        // actualizar saldo insoluto, saldo vigente y status del seguro
                                                                        contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - prima;
                                                                        contratoSeguro.saldoActual = 0;
                                                                        contratoSeguro.estatus = 1;
                                                                        contratoSegurodao.updateSeguro(contratoSeguro);
                                                                    }   // Pago al seguro de cada individuo del grupo
                                                                } else {
                                                                    PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                                    IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                                    if (integrantes != null) {
                                                                        for (int i = 0; i < integrantes.length; i++) {
                                                                            contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
                                                                            if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                                registropagoseguros.idcliente = integrantes[i].idCliente;
                                                                                registropagoseguros.numsolicitud = integrantes[i].idSolicitud;
                                                                                registropagoseguros.referencia = pagos[j].referencia;
                                                                                registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                                registropagoseguros.montoCubierto = ((contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima) * 8;
                                                                                temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                                registropagoseguros.montoporCubrir = 0;
                                                                                registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                                // cifras control de prima
                                                                                double primadecadaintegrante = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                segurosArray.add(primadecadaintegrante);
                                                                                totalPrima += primadecadaintegrante;
                                                                                //	actualizar saldo insoluto, saldo vigente y status del seguro
                                                                                contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - primadecadaintegrante;
                                                                                contratoSeguro.saldoActual = 0;
                                                                                contratoSeguro.estatus = 1;
                                                                                contratoSegurodao.updateSeguro(contratoSeguro);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                // Cifras Control
                                                                excedentesArray.add(pe.montoExcedente);
                                                                // Pago al crédito menos el seguro
                                                                pagocartera[j].monto = pagos[j].monto - temporalmontopagocartera;
                                                                diferenciaEntrePagosSeguros += pagos[j].monto;
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Pago Excedente al crédito y con seguro: " + pe.toString());
                                                                caso13++;
                                                                pagosaldo.status = 13;
                                                                pagocartera[j].status = 13;
                                                                // actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            } else if ((montoTotal - totalVigente) == prima) {
                                                                //	Pago al seguro individual 	
                                                                if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                                    if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                        registropagoseguros.montoCubierto = prima;
                                                                        temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                        registropagoseguros.montoporCubrir = 0;
                                                                        registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                        registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                        //	 cifras control de prima
                                                                        segurosArray.add(prima);
                                                                        totalPrima += prima;
                                                                        //	 actualizar saldo insoluto, saldo vigente y status del seguro
                                                                        contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - prima;
                                                                        contratoSeguro.saldoActual = 0;
                                                                        contratoSeguro.estatus = 1;
                                                                        contratoSegurodao.updateSeguro(contratoSeguro);
                                                                    }
                                                                    //   Pago al seguro de cada individuo del grupo
                                                                } else {
                                                                    PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                                    IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                                    if (integrantes != null) {
                                                                        for (int i = 0; i < integrantes.length; i++) {
                                                                            contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
                                                                            if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                                registropagoseguros.idcliente = integrantes[i].idCliente;
                                                                                registropagoseguros.numsolicitud = integrantes[i].idSolicitud;
                                                                                registropagoseguros.referencia = pagos[j].referencia;
                                                                                registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                                registropagoseguros.montoCubierto = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                                registropagoseguros.montoporCubrir = 0;
                                                                                registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                                //	 cifras control de prima
                                                                                double primadecadaintegrante = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                segurosArray.add(primadecadaintegrante);
                                                                                totalPrima += primadecadaintegrante;
                                                                                //	actualizar saldo insoluto, saldo vigente y status del seguro
                                                                                contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - primadecadaintegrante;
                                                                                contratoSeguro.saldoActual = 0;
                                                                                contratoSeguro.estatus = 1;
                                                                                contratoSegurodao.updateSeguro(contratoSeguro);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                //	 Pago al crédito menos el seguro
                                                                pagocartera[j].monto = pagos[j].monto - temporalmontopagocartera;
                                                                diferenciaEntrePagosSeguros += pagos[j].monto;
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Pago normal al crédito y al seguro");
                                                                caso12++;
                                                                pagosaldo.status = 12;
                                                                pagocartera[j].status = 12;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            } else if ((montoTotal - totalVigente) >= (prima * ClientesConstants.PORCENTAJE_APROBAR_PRIMA) && (montoTotal - totalVigente) <= prima) {
                                                                double faltantePrima = prima - (montoTotal - totalVigente);
                                                                prima = (montoTotal - totalVigente);
                                                                //	Pago al seguro individual 
                                                                if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                                    if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                        registropagoseguros.montoCubierto = prima;
                                                                        temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                        registropagoseguros.montoporCubrir = faltantePrima;
                                                                        registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                        registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                        //	Cifras Control
                                                                        segurosArray.add(prima);
                                                                        totalPrima += prima;
                                                                        //	 actualizar saldo insoluto, saldo vigente y status del seguro
                                                                        contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - prima;
                                                                        contratoSeguro.saldoActual = 0;
                                                                        contratoSeguro.estatus = 1;
                                                                        contratoSegurodao.updateSeguro(contratoSeguro);
                                                                    }
                                                                    //	  Pago al seguro de cada individuo del grupo
                                                                } else {
                                                                    PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                                    IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                                    if (integrantes != null) {
                                                                        for (int i = 0; i < integrantes.length; i++) {
                                                                            contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
                                                                            if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                                registropagoseguros.idcliente = integrantes[i].idCliente;
                                                                                registropagoseguros.numsolicitud = integrantes[i].idSolicitud;
                                                                                registropagoseguros.referencia = pagos[j].referencia;
                                                                                registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                                registropagoseguros.montoCubierto = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                                registropagoseguros.montoporCubrir = faltantePrima;
                                                                                registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                                //		 cifras control de prima
                                                                                double primadecadaintegrante = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                segurosArray.add(primadecadaintegrante);
                                                                                totalPrima += primadecadaintegrante;
                                                                                // actualizar saldo insoluto, saldo vigente y status del seguro
                                                                                contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - primadecadaintegrante;
                                                                                contratoSeguro.saldoActual = 0;
                                                                                contratoSeguro.estatus = 1;
                                                                                contratoSegurodao.updateSeguro(contratoSeguro);

                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                // Pago a cartera
                                                                pagocartera[j].monto = pagos[j].monto - temporalmontopagocartera;
                                                                //pagos[j].monto = pagos[j].monto-totalPrima;
                                                                diferenciaEntrePagosSeguros += pagos[j].monto;
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Pago normal al crédito y 90% al seguro");
                                                                caso11++;
                                                                pagosaldo.status = 11;
                                                                pagocartera[j].status = 11;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            } else if ((montoTotal != totalVigente) && (montoTotal - totalVigente) < prima) {
                                                                // Si es invidual es pago excedente e incidencia
                                                                if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                                    //	Excedente por no cubrir la prima 				    	    
                                                                    pe.fechaMovimiento = pagos[j].fechaPago;
                                                                    pe.referencia = pagos[j].referencia;
                                                                    pe.montoExcedente = pagos[j].monto - totalVigente;
                                                                    pe.numCliente = cmdPagoRef.getReferencia(pagos[j].referencia).numcliente;
                                                                    //falta por definir
                                                                    pe.numCuentaContable = "";
                                                                    pagosExcedentes.addPagoExcedente(pe);
                                                                    temporalmontopagocartera = 0;
                                                                    // Incidencia de seguro
                                                                    cmdPagoRef.addIncidenciasSeguros(pagos[j].fechaPago, pagos[j].referencia, "No cubre prima de seguro", bancoReferencia);
                                                                    // actualizar saldo vigente y status del seguro. Saldo insoluto se queda igual	    	     
                                                                    contratoSeguro.saldoActual = prima;
                                                                    contratoSeguro.estatus = 2;
                                                                    contratoSegurodao.updateSeguro(contratoSeguro);
                                                                    // Cifras Control
                                                                    excedentesArray.add(pe.montoExcedente);
																// Cuando es grupal, el excedente se toma como parte del seguro para
                                                                    // algunos integrantes (por lo tanto NO será excedente)
                                                                } else {
                                                                    prima = montoTotal - totalVigente;
                                                                    int contadorrestaprima = 0;
                                                                    PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                                    IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                                    if (integrantes != null) {
                                                                        for (int i = 0; i < integrantes.length; i++) {
                                                                            contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
                                                                            if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                                registropagoseguros.idcliente = integrantes[i].idCliente;
                                                                                registropagoseguros.numsolicitud = integrantes[i].idSolicitud;
                                                                                registropagoseguros.referencia = pagos[j].referencia;
                                                                                registropagoseguros.fechaPago = pagos[j].fechaPago;
                                                                                double primadecadaintegrante = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                // asignar a los integrantes por orden lo que quede de la prima
                                                                                if (prima > ((contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8)) {
                                                                                    myLogger.info("Prima grupal para repartir entre los integrantes: " + prima);
                                                                                    registropagoseguros.montoCubierto = (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                    temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                                    registropagoseguros.montoporCubrir = 0;
                                                                                    prima -= (contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8;
                                                                                } else if (prima >= ((contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8) * ClientesConstants.PORCENTAJE_APROBAR_PRIMA) {
																				// este ultimo integrante ya no cubrirá toda la prima , si cubre el 90%
                                                                                    // cuenta como que cubrió el seguro	 
                                                                                    myLogger.info("Prima grupal restante :" + prima);
                                                                                    registropagoseguros.montoCubierto = prima;
                                                                                    temporalmontopagocartera += registropagoseguros.montoCubierto;
                                                                                    registropagoseguros.montoporCubrir = ((contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud)).prima * 8) - prima;
                                                                                    contadorrestaprima++;
                                                                                    myLogger.info("Registrando pago 90% del seguro para integrante: " + registropagoseguros.toString());
                                                                                    registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
                                                                                    //  datos extra para ultimo integrante del 90% del seguro
                                                                                    segurosArray.add(prima);
                                                                                    totalPrima += prima;
                                                                                    //	actualizar saldo insoluto, saldo vigente y status del seguro
                                                                                    contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - prima;
                                                                                    contratoSeguro.saldoActual = 0;
                                                                                    contratoSeguro.estatus = 1;
                                                                                    contratoSegurodao.updateSeguro(contratoSeguro);
                                                                                    //   fin datos extra
//																				la prima se acabo entre los integrantes, no habra excedentes
                                                                                    prima = 0;
																				// Si lo que quedó de la prima no cubrió el 90% para asignárselo al siguiente integrante
                                                                                    // causa una incidencia de excedente
                                                                                } else if (prima != 0) {
                                                                                    pe.fechaMovimiento = pagos[j].fechaPago;
                                                                                    pe.referencia = pagos[j].referencia;
                                                                                    // la prima que quedó que no se le pudo asignar a nadie es el excedente
                                                                                    pe.montoExcedente = prima;
                                                                                    pe.numCliente = cmdPagoRef.getReferencia(pagos[j].referencia).numcliente;
                                                                                    //falta por definir
                                                                                    pe.numCuentaContable = "";
                                                                                    pagosExcedentes.addPagoExcedente(pe);
                                                                                    prima = 0;
                                                                                    contadorrestaprima++;
                                                                                    // excedentesArray agregado para grupal
                                                                                    excedentesArray.add(pe.montoExcedente);
                                                                                }
																			// cuando se acabe de repartir la prima entre los integrantes serán incidencias
                                                                                // de q no cubrió el seguro
                                                                                if (prima == 0 && contadorrestaprima != 0) {
                                                                                    IncidenciaSegurosVO incidencia = new IncidenciaSegurosVO();
                                                                                    //incidencia.montoseguroquenocubrio = (contratoSegurodao.getSeguros(integrantes[i].idCliente,integrantes[i].idSolicitud)).prima*8;
                                                                                    incidencia.fechaMovimiento = pagos[j].fechaPago;
                                                                                    incidencia.idCliente = integrantes[i].idCliente;
                                                                                    incidencia.referencia = pagos[j].referencia;
                                                                                    incidencia.tipoIncidencia = 0;
                                                                                    incidencia.observaciones = "No cubre prima de seguro (Integrante)";
                                                                                    incidencia.bancoReferencia = bancoReferencia;
                                                                                    incidenciasSegDAO.addIncidencias(incidencia);
                                                                                    //	actualizar saldo vigente y status= 2 q no pagó. Saldo insoluto se queda igual ** / **	    	     
                                                                                    contratoSeguro.saldoActual = prima;
                                                                                    contratoSeguro.estatus = 2;
                                                                                    contratoSegurodao.updateSeguro(contratoSeguro);

                                                                                } else {
                                                                                    myLogger.info("Registrando pago seguro para integrante: " + registropagoseguros.toString());
                                                                                    registropagosegurodao.insertRegistroPagoSeguro(registropagoseguros);
//																				cifras control de prima
                                                                                    //double primadecadaintegrante = (contratoSegurodao.getSeguros(integrantes[i].idCliente,integrantes[i].idSolicitud)).prima*8;
                                                                                    segurosArray.add(primadecadaintegrante);
                                                                                    totalPrima += primadecadaintegrante;

                                                                                    //	actualizar saldo insoluto, saldo vigente y status del seguro
                                                                                    contratoSeguro.saldoInsoluto = contratoSeguro.saldoInsoluto - primadecadaintegrante;
                                                                                    contratoSeguro.saldoActual = 0;
                                                                                    contratoSeguro.estatus = 1;
                                                                                    contratoSegurodao.updateSeguro(contratoSeguro);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
															// pago al credito menos el seguro
                                                                // pago cartera
                                                                pagocartera[j].monto = pagos[j].monto - temporalmontopagocartera;
                                                                //pago al credito
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Pago excedente al crédito pero no alcanza a cubrir el seguro individual (o de todos grupal)");
                                                                caso10++;
                                                                pagosaldo.status = 10;
                                                                pagocartera[j].status = 10;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            } else if ((montoTotal - totalVigente == 0)) {
                                                                // Tiene seguro, pero sólo paga normal el crédito 
                                                                if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                                    // Incidencia de seguro individual
                                                                    cmdPagoRef.addIncidenciasSeguros(pagos[j].fechaPago, pagos[j].referencia, "No cubre prima de seguro", bancoReferencia);
                                                                    //actualizar saldo vigente y status= 2 q no pagó. Saldo insoluto se queda igual	    	     
                                                                    contratoSeguro.saldoActual = prima;
                                                                    contratoSeguro.estatus = 2;
                                                                    contratoSegurodao.updateSeguro(contratoSeguro);
                                                                    // Incidencias de seguro de cada integrante del grupo
                                                                } else {
                                                                    PagoGrupalVO pagoGVO = GrupoUtil.esPagoGrupalVO(pagos[j].referencia);
                                                                    IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                                                    if (integrantes != null) {
                                                                        for (int i = 0; i < integrantes.length; i++) {
                                                                            contratoSeguro = contratoSegurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
                                                                            if (contratoSeguro != null && contratoSeguro.contratacion != 2 && contratoSeguro.prima != 0) {
                                                                                IncidenciaSegurosVO incidencia = new IncidenciaSegurosVO();
                                                                                incidencia.fechaMovimiento = pagos[j].fechaPago;
                                                                                incidencia.idCliente = integrantes[i].idCliente;
                                                                                incidencia.referencia = pagos[j].referencia;
                                                                                incidencia.tipoIncidencia = 0;
                                                                                incidencia.observaciones = "No cubre prima de seguro (Integrante)";
                                                                                incidencia.bancoReferencia = bancoReferencia;
                                                                                incidenciasSegDAO.addIncidencias(incidencia);
                                                                                //actualizar saldo vigente y status= 2 q no pagó. Saldo insoluto se queda igual	    	     
                                                                                contratoSeguro.saldoActual = prima;
                                                                                contratoSeguro.estatus = 2;
                                                                                contratoSegurodao.updateSeguro(contratoSeguro);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                // pago cartera
                                                                pagocartera[j].monto = pagos[j].monto;
                                                                //	pago al credito
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Tiene seguro, pero sólo cubre el crédito exacto");
                                                                caso9++;
                                                                pagosaldo.status = 9;
                                                                pagocartera[j].status = 9;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);

                                                            }
                                                            // Casos cuando no tiene seguro  	   
                                                        } else {
                                                            if ((montoTotal > totalVigente)) {
                                                                //Incidencias de excedente
                                                                pe.fechaMovimiento = pagos[j].fechaPago;
                                                                pe.referencia = pagos[j].referencia;
                                                                pe.montoExcedente = pagos[j].monto - totalVigente;
                                                                pe.numCliente = cmdPagoRef.getReferencia(pagos[j].referencia).numcliente;
                                                                pe.numCuentaContable = "";
                                                                pagosExcedentes.addPagoExcedente(pe);

															// Cifras Control
                                                                //excedentesArray.add(pagos[j]);
                                                                excedentesArray.add(pe.montoExcedente);

															//incidenciasArray.add(pagos[j]);
                                                                // pago cartera
                                                                pagocartera[j].monto = pagos[j].monto;
                                                                //pago al credito
                                                                pagosProcesadosArray.add(pagos[j]);

                                                                myLogger.info("Pago Excedente pero no tenía seguro: " + pe.toString());
                                                                caso8++;
                                                                pagosaldo.status = 8;
                                                                pagocartera[j].status = 8;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            } else {
                                                                //	pago cartera
                                                                pagocartera[j].monto = pagos[j].monto;
                                                                // pago al credito
                                                                pagosProcesadosArray.add(pagos[j]);
                                                                myLogger.info("Pago normal al crédito, no tiene seguro");
                                                                caso7++;
                                                                pagosaldo.status = 7;
                                                                pagocartera[j].status = 7;
//															actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                                pagos[j].reprocesar = 2;
                                                                updateIncidencias.updateIncidencias(pagos[j]);
                                                                pagoscarteraparaenviar.add(pagocartera[j]);
                                                            }
                                                        }

                                                        // pagos cartera a la BD
                                                        pagocartera[j].sucursal = pagos[j].referencia.substring(0, 3);
                                                        pagocartera[j].fechaHora = new Timestamp(System.currentTimeMillis());
                                                        pagocartera[j].numCuenta = pagos[j].numCuenta;
                                                        pagocarteradao.insertPagoCartera(pagocartera[j]);
                                                        new PagoManualDAO().insertPago(pagocartera[j]);

                                                        myLogger.info("Pago Cartera1: " + pagocartera.toString());

                                                    } else {

													//No cubre con lo minimo a pagar, causa incidencia
                                                        // cmdPagoRef.agregarIncidencia(pagos[j],2,"Pago Insuficiente");
                                                        //	pago cartera
                                                        pagocartera[j].monto = pagos[j].monto;
                                                        // Cifras Control
                                                        pagosProcesadosArray.add(pagos[j]);
                                                        pagosInsuficienteArray.add(pagos[j]);
                                                        myLogger.info("Pago Insuficiente");
                                                        caso6++;
                                                        pagosaldo.status = 6;
                                                        pagocartera[j].status = 6;
//													actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                                        pagos[j].reprocesar = 2;
                                                        updateIncidencias.updateIncidencias(pagos[j]);
                                                        pagoscarteraparaenviar.add(pagocartera[j]);

													// pagos cartera a la BD
                                                        //myLogger.info("DEBUGEAR .,. ,.,. ,.:"+pagos[j].referencia);
                                                        pagocartera[j].sucursal = pagos[j].referencia.substring(0, 3);
                                                        pagocartera[j].fechaHora = new Timestamp(System.currentTimeMillis());
                                                        pagocartera[j].numCuenta = pagos[j].numCuenta;
                                                        pagocarteradao.insertPagoCartera(pagocartera[j]);
                                                        new PagoManualDAO().insertPago(pagocartera[j]);
                                                        //myLogger.info("Pago Cartera2: "+pagocartera.toString());
                                                    }
                                                } else {
												//Causa incidencia, Crédito Cancelado
                                                    //cmdPagoRef.agregarIncidencia(pagos[j],5,"Crédito Cancelado");

                                                    // Cifras Control
                                                    incidenciasArray.add(pagos[j]);
                                                    myLogger.info("Crédito Cancelado");
                                                    caso5++;
                                                    pagosaldo.status = 5;

                                                    //		actualizar incidencia con status=1. Significa que no se pudo reprocesar, queda en espera del nuevo reproceso
                                                    pagos[j].reprocesar = 1;
                                                    pagos[j].tipo = "5";
                                                    pagos[j].observaciones = "Crédito Cancelado";
                                                    updateIncidencias.updateIncidencias(pagos[j]);
                                                }
                                            } else {
											//Causa incidencia, Crédito Liquidado
                                                // cmdPagoRef.agregarIncidencia(pagos[j],4,"Crédito Liquidado");

                                                // Cifras Control
                                                incidenciasArray.add(pagos[j]);
                                                myLogger.info("Crédito Liquidado");
                                                caso4++;
                                                pagosaldo.status = 4;
                                                //	actualizar incidencia con status=1. Significa que no se pudo reprocesar, queda en espera del nuevo reproceso
                                                pagos[j].reprocesar = 1;
                                                pagos[j].tipo = "4";
                                                pagos[j].observaciones = "Crédito Liquidado";
                                                updateIncidencias.updateIncidencias(pagos[j]);
                                            }

                                        } else {
										//Causa incidencia, Referencia encontrada, Saldo no encontrado
                                            //cmdPagoRef.agregarIncidencia(pagos[j],3,"Saldo No Encontrado");

                                            // Cifras Control
                                            incidenciasArray.add(pagos[j]);

                                            myLogger.info("Saldo No Encontrado");
                                            caso3++;
                                            pagosaldo.status = 3;

                                            //	actualizar incidencia con status=1. Significa que no se pudo reprocesar, queda en espera del nuevo reproceso
                                            pagos[j].reprocesar = 1;
                                            pagos[j].tipo = "3";
                                            pagos[j].observaciones = "Saldo No Encontrado";
                                            updateIncidencias.updateIncidencias(pagos[j]);
                                        }
                                    } else {
									//Causa incidencia, Referencia No Encontrada
                                        // cmdPagoRef.agregarIncidencia(pagos[j],2,"Referencia No Encontrada");
                                        //  Cifras Control
                                        incidenciasArray.add(pagos[j]);
                                        myLogger.info("Referencia No Encontrada");
                                        caso2++;
                                        pagosaldo.status = 2;

                                        // actualizar incidencia con status=1. Significa que no se pudo reprocesar, queda en espera del nuevo reproceso
                                        pagos[j].reprocesar = 1;
                                        pagos[j].tipo = "2";
                                        pagos[j].observaciones = "Referencia No Encontrada";
                                        updateIncidencias.updateIncidencias(pagos[j]);
                                    }

                                } else {
								//Causa Incidencia, Referencia No Valida
                                    //cmdPagoRef.agregarIncidencia(pagos[j], 1,"Referencia No Válida");

                                    // Cifras Control
                                    incidenciasArray.add(pagos[j]);
                                    myLogger.info("Referencia No Válida");
                                    caso1++;
                                    pagosaldo.status = 1;
                                    // actualizar incidencia con status=1. Significa que no se pudo reprocesar, queda en espera del nuevo reproceso
                                    pagos[j].reprocesar = 1;
                                    pagos[j].tipo = "1";
                                    pagos[j].observaciones = "Referencia No Válida";
                                    updateIncidencias.updateIncidencias(pagos[j]);
                                }
                            } else {
							// aqui va todo lo referente a EXCEL CARTERA ...  ya se saltá validaciones
                                // y va directo a meter el pago a la cartera
//							pago cartera
                                pagocartera[j].monto = pagos[j].monto;
                                //pago al credito
                                pagosProcesadosArray.add(pagos[j]);
                                myLogger.info("Pago tipo EXCEL CARTERA");
                                caso14++;
                                pagocartera[j].status = 14;
//							actualizar incidencia con status=2. Significa que la incidencia se reprocesó exitosamente
                                pagos[j].reprocesar = 2;
                                updateIncidencias.updateIncidencias(pagos[j]);
                                pagoscarteraparaenviar.add(pagocartera[j]);
                                pagocartera[j].sucursal = pagos[j].referencia.substring(0, 3);
                                pagocartera[j].fechaHora = new Timestamp(System.currentTimeMillis());
                                pagocartera[j].numCuenta = pagos[j].numCuenta;
                                pagocarteradao.insertPagoCartera(pagocartera[j]);
                                new PagoManualDAO().insertPago(pagocartera[j]);
                                //myLogger.info("Pago Cartera2: "+pagocartera.toString());
                            }
                            //  Cifras Control, guardar todos los pagos recibidos (procesados o no)
                            totalPagosArray.add(pagos[j]);

                            // pagos a la BD *** ESTE YA NO DEBERÍA INSERTAR A LA BD.....
                            pagosaldo.sucursal = pagos[j].referencia.substring(0, 3);
                        }
                    }
                    // NOTA: En los pagos procesados la aplicación está descontando la prima
                    double pagos_normales = cmdPagoRef.getMontoTotalArchivo(pagosProcesadosArray) - cmdPagoRef.getMontoTotalArchivoExcedentes(excedentesArray);
                    myLogger.info("********RESUMEN*********");

                    myLogger.info("Reprocesó:" + request.getRemoteUser());
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
                    notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "Se reprocesaron los pagos");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    request.setAttribute("PAGOS_CARTERA", pagoscarteraparaenviar);
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "No se reprocesaron los pagos");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    myLogger.info("No hay pagos para el Reproceso");
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {

        }
        return siguiente;
    }

}

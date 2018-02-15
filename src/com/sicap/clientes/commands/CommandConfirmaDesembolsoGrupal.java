package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.RenovacionDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.cartera.SaldoFavorDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ProcesaPagosReferenciadosHelper;
import com.sicap.clientes.helpers.SaldosHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class CommandConfirmaDesembolsoGrupal implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConfirmaDesembolsoGrupal.class);

    public CommandConfirmaDesembolsoGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        CicloGrupalVO cicloAnt = new CicloGrupalVO();
        // Se agregan las variables para la conexion a PagosReferenciadosDAO
        PagoVO pagos[] = null;
        TablaAmortVO[] tablaAmort = null;
        PagoGrupalVO pagoGrupo = new PagoGrupalVO();
        PagoReferenciadoDAO pagoRefDAO = new PagoReferenciadoDAO();
        ProcesaPagosReferenciadosHelper pagoRefHelper = new ProcesaPagosReferenciadosHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        CreditoCartDAO creditoAntDAO = new CreditoCartDAO();
        CreditoCartVO creditoAntVO = new CreditoCartVO();
        // Se agrega llamado a Referencia General para la estraccion del numero de referncia
        ReferenciaGeneralDAO referenciaDao = new ReferenciaGeneralDAO();
        SaldoIBSVO saldoAntVO = new SaldoIBSVO();
        SaldoIBSDAO saldoAntDAO = new SaldoIBSDAO();
        PagoGrupalDAO pagoGrupoDAO = new PagoGrupalDAO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        BitacoraCicloVO bitacora = new BitacoraCicloVO();
        Calendar c1 = Calendar.getInstance();
        SegurosDAO seguroDAO = new SegurosDAO();
        RenovacionDAO renovacionDao = new RenovacionDAO();
        String strGarantia = "";
        double porGarantia = 0.00;
        double saldoFondeador = 0.00;
        String ultimaFecha = null;
        boolean procesandoCierre = false;
        java.sql.Date sqlDate = null;

        Connection conn = null;
        myLogger.debug("Variables inicializadas");
        try {
            synchronized (this) {
                conn = ConnectionManager.getMySQLConnection();
                conn.setAutoCommit(false);

                int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
                int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
                Date fechaConfDesembolso = HTMLHelper.getParameterDate(request, "fechaConfirmacion");
                if (fechaConfDesembolso == null) {
                    fechaConfDesembolso = HTMLHelper.getParameterDate(request, "fechaDesembolso");
                }
                int fondeador = HTMLHelper.getParameterInt(request, "fondeador");
                int usoSaldo = 0;
                procesandoCierre = new CatalogoDAO().ejecutandoCierre();
                ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
                SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
                Date fechaUltimoCierre = sdf.parse(ultimaFecha);
                sqlDate = Convertidor.toSqlDate(fechaUltimoCierre);

                String numeroReferencia = null;
                double montoDepositado = 0;
                double montoFaltante = 0;
                double aux = 0;
                int contador = 0;
                double monto_cuenta = 0;
                int resultado = 0;
                int ciclo_ant = 0;
                boolean blnFicha = false;
                boolean sinFicha = false;
                int clieSeguro = 0;
                EventoHelper eventoHelper = new EventoHelper();

                grupo = (GrupoVO) session.getAttribute("GRUPO");
                if (idCiclo != 0) {
                    ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
                    ciclo.fondeador = fondeador;
                }
                /* Obtiene el ciclo anterior
                 peticion de autorizacion de uso de saldo*/
                SaldoFavorDAO autorizado = new SaldoFavorDAO();
                usoSaldo = autorizado.aplicaUsoSaldo(grupo.idGrupo, idCiclo - 1);
                myLogger.debug("Obteniendo grupo : " + grupo.idGrupo + " ciclo :" + idCiclo);
                CicloGrupalVO cicloTemporal = new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo);
                if (cicloTemporal.desembolsado == ClientesConstants.CICLO_DESEMBOLSADO) {
                    strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
                    porGarantia = Double.parseDouble(strGarantia);
                    porGarantia = porGarantia / 100;
                    myLogger.debug("El ciclo está desembolsado");
                    myLogger.debug("Comprobando archivos asociados");
                    if (ciclo.archivosAsociados != null) { // Validacion alta del seguro no iria
                        for (ArchivoAsociadoVO archivo : ciclo.archivosAsociados) {
                            if (archivo.tipo == 16) {
                                blnFicha = true;
                            }
                        }
                    } else // Validamos si todos los integrantes cuentan con seguro financiado
                    if (SegurosUtil.validarNumIntegrantesSegFinanciado(ciclo.integrantes)) {
                        // no requiere ficha de deposito, se asume que si tiene ficha para continuar con el flujo
                        blnFicha = true;
                    }
                    myLogger.debug("Comprobando seguros");
                    int countAsegurados = 0;
                    boolean conSeguroCompleto = false;
                    for (int i = 0; i < ciclo.integrantes.length; i++) {
                        clieSeguro = seguroDAO.compruebaSeguro(ciclo.integrantes[i]);
                        if (!blnFicha && clieSeguro == 1) {
                            sinFicha = true;
                        }

                        /*
                        Validar seguro contratado por cada uno de los integrantes
                        1 SI seguro contratado
                        2 NO seguro contratado
                         */
                        if (clieSeguro == 1) {
                            countAsegurados++;
                        }

                    }

                    //Bandera paraindicar sitodos los integrantes estan asegurados
                    conSeguroCompleto = (countAsegurados == ciclo.integrantes.length);

                    //  CAMPO PARA ELIMINAR LAS TRANSFERENCIAS
                    if (ciclo.idCiclo > 1 && usoSaldo == 1) {
                        cicloAnt = new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo - 1);
                        creditoAntVO = creditoAntDAO.getCreditoClienteSol(grupo.idGrupo, cicloAnt.idCiclo);
                        monto_cuenta = creditoAntVO.getMontoCuenta();
                        saldoAntVO = saldoAntDAO.getSaldo(creditoAntVO.getNumCliente(), creditoAntVO.getNumSolicitud(), creditoAntVO.getReferencia());
                    }
                    /*Proceso para la validacion del deposito del 10% del monto prestamo para el ahorro
                         Miguel Angel Mendoza Maldonado 08/12/2010
                         Obtenemos el numero de referencia*/
                    numeroReferencia = referenciaDao.getReferencia(ciclo.idGrupo, idCiclo, 'G');
                    //Se extraen los pagos realizados para el ahorro
                    pagos = pagoRefDAO.getPagoAReprocesarReferencia(numeroReferencia);
                    // Si aun no se ha efectuado el pago el montodepositado es cero por default
                    if (pagos == null) {
                        //notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El Grupo no ha hecho ningun deposito y"));
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El Equipo no efectuo algun deposito de garantía. "));
                    } else {
                        // Inicializamos contador para hacer la suma de los pagos en caso de ser mas de uno
                        //contador = pagoRefDAO.getPagoAReprocesarReferencia(numeroReferencia).length;
                        contador = pagos.length;
                        // se hace validacion si mi contador es 1 tengo un solo registro de pago y no se deben sumar los montos
                        // en caso contrario se hace una sumatoria de cada uno de los pagos
                        if (contador == 1) {
                            montoDepositado = pagos[0].monto;
                            //System.out.println("La suma total de los pagos es: " + montoDepositado);
                        } else {
                            montoDepositado = pagos[0].monto;
                            for (int i = 1; i < contador; i++) {
                                aux = montoDepositado;
                                montoDepositado = pagos[i].monto + montoDepositado;
                            }
                            //System.out.println("La suma total de los pagos es: " + montoDepositado + "el monto de la cuenta es" + monto_cuenta);
                        }
                        //System.out.println("El monto Desembolsado es: " + ciclo.monto);
                        //System.out.println("El 10% del monto es: " + (ciclo.monto * 0.1));
                    }
                    // condicion si el monto depositado es menor que al 10% de ahorro no te dejo pasar
                    //if ((montoDepositado + monto_cuenta) < (ciclo.monto * 0.1)) {
                    // Se quita la parte del filtro de deposito de garantia
                    if (1 == 2) {
                        //System.out.println("Aun no cubres el monto de tu ahorro");
                        montoFaltante = (ciclo.monto * porGarantia) - montoDepositado - monto_cuenta;
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Aun no se cubre el monto de la garantía, hacen falta $" + montoFaltante));
                    } else {
                        /*if ((montoDepositado + monto_cuenta) > 0) {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Monto de Garatía $" + (montoDepositado + monto_cuenta)));
                            }*/
                        //Proceso confirmacion solicitudes, ciclo grupal integrantes etc.
                        myLogger.debug("Procesando las confirmaciones de los integrantes");
                        System.out.println("conn " + conn);
                        System.out.println("ciclo " + ciclo);
                        System.out.println("request " + request);
                        System.out.println("idSucursal " + idSucursal);
                        System.out.println("fechaConfDesembolso " + fechaConfDesembolso);
                        if (GrupoUtil.procesaIntegrantesConfirmacionDesembolso(conn, ciclo, request, idSucursal, fechaConfDesembolso)) {
                            ciclo.plazo = ciclo.tablaAmortizacion.length - 1;
                            //Calcula tabla de amortizacion
                            //GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, ciclo.tablaAmortizacion[0].fechaPago);
                            myLogger.info("usuario: " + request.getRemoteUser() + ", grupo: " + ciclo.idGrupo + ", ciclo " + ciclo.idCiclo);
                            GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, fechaConfDesembolso);

                            /* Cambio para que no registre en IBS JBL MAY/10
                                 * 		        
                             */
                            //				if ( GrupoHelper.registerIBS(grupo, ciclo, request, notificaciones) ){
                            CreditoHelperCartera CreditoCartHelper = new CreditoHelperCartera();
                            myLogger.debug("Registrando credito grupal");
                            if (ciclo.fondeador != ClientesConstants.ID_FONDEADOR_CREDITO_REAL) {
                                //if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM_P2) {//descomentar y comentar la lina de abajo para cambiar la prioridad de FINAFIM
                                if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM) {
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + ciclo.fondeador));
                                    if (saldoFondeador < ciclo.montoConComision) {
                                        //ciclo.fondeador = ClientesConstants.ID_FONDEADOR_FINAFIM;
                                        ciclo.fondeador = ClientesConstants.ID_FONDEADOR_FINAFIM_P2;
                                    }
                                }
                                //if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM) {//descomentar y comentar la lina de abajo para cambiar la prioridad de FINAFIM
                                if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM_P2) {
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + ciclo.fondeador));
                                    if (saldoFondeador < ciclo.montoConComision) {
                                        ciclo.fondeador = FondeadorUtil.cambiaFondeadorFinafim(ciclo, idSucursal);
                                    }
                                }
                                if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS) {
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + ciclo.fondeador));
                                    if (saldoFondeador < ciclo.montoConComision) {
                                        ciclo.fondeador = FondeadorUtil.cambiaFondeador(ciclo, idSucursal);
                                    }
                                }
                                if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR) {
                                    saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + ciclo.fondeador));
                                    if (saldoFondeador < ciclo.montoConComision) {
                                        ciclo.fondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
                                    }
                                }
                            }
                            //if (CreditoCartHelper.registraCreditoGrupo(grupo, montoDepositado + monto_cuenta, monto_cuenta, ciclo, request, notificaciones)) {					//cicloDAO.updateCicloCredito(ciclo);
                            if (CreditoCartHelper.registraCreditoGrupo(grupo, 0.0, 0.0, ciclo, request, notificaciones)) {					//cicloDAO.updateCicloCredito(ciclo);
                                myLogger.info("Credito grupal registrado");
                                CreditoCartVO credito = new CreditoCartVO();
                                CreditoCartDAO creditoDAO = new CreditoCartDAO();
                                credito = creditoDAO.getCreditoReferencia(ciclo.referencia);
                                /*
                                     * JBL, en caso de traer monto de cuenta anterior lo deja en ceros. Se arma tambien el pago grupal correspondiente al monto de la cuenta					
                                 */
                                if (monto_cuenta > 0) {
                                    creditoAntVO.setMontoCuenta(0);
                                    creditoAntVO.setMontoCuentaCongelada(0);
                                    resultado = creditoAntDAO.updatePagoCredito(creditoAntVO);
                                    saldoAntVO.setSaldoBucket(0);
                                    saldoAntDAO.updateSaldo(saldoAntVO);
                                    myLogger.info("Actualizo saldo");
                                    // Se forma el pago del grupo para ser posteriormente individualizado por la matriz.						
                                    pagoGrupo.monto = monto_cuenta;
                                    pagoGrupo.numGrupo = ciclo.idGrupo;
                                    pagoGrupo.numCiclo = ciclo.idCiclo;
                                    pagoGrupo.numTransaccion = 1;
                                    pagoGrupo.numPago = 1;
                                    pagoGrupo.numAmortizacion = 0;
                                    pagoGrupo.fechaPago = Convertidor.toSqlDate(c1.getTime());
                                    pagoGrupo.solidario = 0;
                                    pagoGrupo.ahorro = 0;
                                    pagoGrupo.multa = 0;
                                    pagoGrupoDAO.addPagoGrupal(pagoGrupo);
                                    myLogger.info("Agrego pago grupal");
                                    // Se arma pago de grupo que contiene el excedente del ciclo anterior.
                                    PagoVO pagoTransferido = new PagoVO();
                                    PagoDAO creaPagoTransferencia = new PagoDAO();
                                    pagoTransferido.referencia = numeroReferencia;
                                    pagoTransferido.monto = monto_cuenta;
                                    pagoTransferido.fechaPago = Convertidor.toSqlDate(c1.getTime());
                                    pagoTransferido.fechaHora = new Timestamp(System.currentTimeMillis());
                                    pagoTransferido.tipo = "T";
                                    pagoTransferido.enviado = 1;
                                    pagoTransferido.status = 6;
                                    //pagoTransferido.bancoReferencia = 8;
                                    pagoTransferido.bancoReferencia = ClientesConstants.ID_BANCO_TRANSFERENCIA;
                                    pagoTransferido.sucursal = numeroReferencia.substring(0, 3);
                                    pagoTransferido.idContable = 0;
                                    pagoTransferido.destino = 3;
                                    pagoTransferido.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ClientesConstants.ID_BANCO_TRANSFERENCIA, "00080", "C");
                                    // Se crea el pago virtual con el detalle de TRANSFERENCIA					
                                    creaPagoTransferencia.insertPago(pagoTransferido);
                                    myLogger.info("Inserto pago de transferencia");
                                    creaPagoTransferencia.insertPagoCartera(pagoTransferido);
                                    myLogger.info("Inserto pago cartera");
                                    TransaccionesHelper.registraPagoGarantia(credito, pagoTransferido, false);
                                    credito.setMontoCuenta(monto_cuenta);
                                    creditoAntDAO.updatePagoCredito(credito);
                                    eventoHelper.registraPago(credito, pagoTransferido);
                                }
                                ciclo.idCreditoIBS = credito.getNumCredito();
                                ciclo.idCuentaIBS = credito.getNumCuenta();
                                ciclo.fechaDispersion = credito.getFechaDesembolso();
                                ciclo.estatus = ClientesConstants.CICLO_DISPERSADO;
                                //String fechaDesembolso = Convertidor.dateToString(credito.getFechaDesembolso());
                                String fechaDesembolso = Convertidor.dateToString(fechaConfDesembolso);
                                new GrupoDAO().updateGrupoIBS(ciclo.idCreditoIBS, grupo.idGrupo);
                                myLogger.info("Actualizo grupo");

                                //Seterar en ciclo consegurocompleto
                                ciclo.seguroCompleto = conSeguroCompleto ? 1 : 0;
                                //UPDATE unicamente seguroCompleto
                                new CicloGrupalDAO().
                                        updateSeguroCompletoCiclo(ciclo);

                                /*ASIGNACION DE FONDEADOR AUTOMATICO
                                    ciclo.fondeador = FondeadorUtil.asignaFondeador(ciclo);*/
                                new CicloGrupalDAO().updateCiclo(conn, ciclo);
                                myLogger.info("Actualizo actualizo ciclo");
                                conn.commit();
                                myLogger.info("Re sealizo el commit");
                                tablaAmort = TablaAmortHelper.insertTablaInsolutoComunal(grupo, ciclo, credito.getValorCuota(), credito.getPeriodicidad(), fechaDesembolso, credito.getTasaInteres());
                                myLogger.info("Tabla de amortizacion insertada");
                                EventoHelper.registraDesembolso(ciclo, grupo, request, notificaciones);
                                EventoHelper.registraDesembolsoInteres(ciclo, grupo, request, notificaciones);
                                myLogger.info("Desembolso registrado");
                                ciclo.saldo = SaldosHelper.insertSaldo(ciclo, grupo, request, notificaciones);
                                myLogger.info("Saldo insertado");
                                transacHelper.registraDesembolso(credito, ciclo, request, notificaciones, saldoFondeador);
                                myLogger.info("Desembolso registrado");
                                bitacora.setIdEquipo(grupo.idGrupo);
                                bitacora.setIdCiclo(idCiclo);
                                bitacora.setEstatus(ciclo.estatus);
                                bitacora.setIdComentario(bitacoraCicloDao.getNumComentario(grupo.idGrupo, idCiclo) + 1);
                                bitacora.setComentario("Equipo dispersado");
                                bitacora.setUsuarioComentario(request.getRemoteUser());
                                bitacora.setUsuarioAsignado(bitacoraCicloDao.getUsuarioEstatus(grupo.idGrupo, idCiclo, ClientesConstants.CICLO_ANALISIS));
                                myLogger.info("Insertando registro de bitácora ciclo");
                                bitacoraCicloDao.insertaBitacoraCiclo(null, bitacora);
                                if (renovacionDao.esNoRenovado(grupo.idGrupo, idCiclo - 1)) {
                                    myLogger.debug("El equipo se encuentra como no renovado");
                                    renovacionDao.eliminaPlaneacion(grupo.idGrupo, idCiclo - 1);
                                    myLogger.info("Se elimina registro de equipo no renovado");
                                }
                                TablaDevengamientoHelper.regitroDevengamientoDiario(tablaAmort, credito.getPeriodicidad());
                                myLogger.info("Devengamiento registrado");
                                // En caso de que existan pagos referenciados para deposito en garantía estos de reprocesan automáticamente
                                if (contador > 0) {
                                    for (int i = 0; i < contador; i++) {
                                        //  Usuario que reprocesa el pago
                                        pagos[i].usuarioReproceso = "sistema";
                                        // Comentarios de reproceso para cada pago
                                        pagos[i].comentarios = "Deposito en garantia reprocesado automaticamente";
                                        // Comentarios de reproceso para cada pago
                                        pagos[i].nuevareferencia = numeroReferencia;
                                    }
                                    // Actualiza el monto de la cuenta con el monto depositado
                                    //credito.setMontoCuenta(credito.getMontoCuenta());
                                    credito.setMontoCuentaCongelada(credito.getMontoDesembolsado() * porGarantia);
                                    resultado = creditoAntDAO.updatePagoCredito(credito);
                                    myLogger.info("Pago credito actualizado");
                                    // Reprocesa los pagos de deposito en garantia
                                    pagoRefHelper.procesaPagos(pagos, request);
                                    myLogger.info("Pagos reprocesados");
                                    //Actualizamos el estatus del campo pc_enviado de todos pagos hecho para el ahorro
                                    //para que al Cierre del día ya no sean tomado en cuenta
                                    PagoDAO pagosRealizados = new PagoDAO();
                                    pagosRealizados.updatePagosCartera(1, numeroReferencia);
                                    myLogger.info("Pagos actualizados");

                                    creditoAntVO = creditoDAO.getCreditoReferencia(ciclo.referencia);
                                    credito.setMontoCuenta(creditoAntVO.getMontoCuenta());
                                    // Actualiza el monto de la cuenta con el monto depositado
                                    //credito.setMontoCuenta(credito.getMontoCuenta());
                                    //credito.setMontoCuentaCongelada(credito.getMontoCredito() * porGarantia);
                                    resultado = creditoAntDAO.updatePagoCredito(credito);
                                    myLogger.info("Pago credito actualizado");
                                    for (int i = 0; i < pagos.length; i++) {
                                        eventoHelper.registraPago(credito, pagos[i]);
                                    }
                                    myLogger.info("Eventos de pago registrados");
                                }
                                BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandConfirmaDesembolsoGrupal");
                                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados y confirmados correctamente"));
                                bitutil.registraEvento(ciclo);
                                for (int i = 0; i < ciclo.integrantes.length; i++) {
                                    bitutil.registraEvento(ciclo.integrantes[i]);
                                }
                                //Se verifica que no este atrasado el credito por cancelacion
                                if (procesandoCierre) {
                                    myLogger.info("Procesando cierre se aumenta fecha de comparacion");
                                    sqlDate = Convertidor.toSqlDate(FechasUtil.getRestarDias(Convertidor.toSqlDate(sqlDate), -1));
                                }
                                System.out.println("sqlDate " + sqlDate.toString());
                                System.out.println("ciclo.saldo.getFechaDesembolso() " + ciclo.saldo.getFechaDesembolso() + " fechaUltimoCierre " + fechaUltimoCierre);
                                if (ciclo.saldo.getFechaDesembolso().before(Convertidor.toSqlDate(fechaUltimoCierre))) {
                                    myLogger.info("Credito atrasado");
                                    TablaAmortDAO tablaDAO = new TablaAmortDAO();
                                    int numPagoAmort = 0;
                                    double saldoCapital = 0;
                                    double saldoInteres = 0;
                                    double saldoIVAInteres = 0;
                                    for (TablaAmortVO tablaAmortVO : tablaAmort) {
                                        if (tablaAmortVO.getFechaPago().before(sqlDate)) {
                                            tablaDAO.actStatusTablaCierre(ciclo.saldo, tablaAmortVO.getNumPago(), 1);
                                            saldoCapital += tablaAmortVO.getAbonoCapital();
                                            saldoInteres += tablaAmortVO.getInteres();
                                            saldoIVAInteres += tablaAmortVO.getIvaInteres();
                                            numPagoAmort++;
                                        }
                                    }
                                    System.out.println("numPagoAmort " + numPagoAmort);
                                    if (numPagoAmort > 0) {
                                        tablaDAO.actStatusTablaCierre(ciclo.saldo, tablaAmort[numPagoAmort].getNumPago(), 1);
                                        saldoInteres += tablaAmort[numPagoAmort].getInteres();
                                        saldoIVAInteres += tablaAmort[numPagoAmort].getIvaInteres();
                                        myLogger.info("Credito con pagos pendientes");
                                        ciclo.saldo.setSaldoTotalAlDia(ciclo.saldo.getMontoDesembolsado() + saldoInteres + saldoIVAInteres);
                                        saldoAntDAO.updateSaldo(ciclo.saldo);
                                        TablaAmortHelper.aplicaPagosTabla(credito, credito.getMontoCuenta() - credito.getMontoCuentaCongelada(), Convertidor.toSqlDate(FechasUtil.getRestarDias(Convertidor.toSqlDate(sqlDate), 1)), false, 0, sqlDate);
                                        ciclo.saldo = saldoAntDAO.getSaldo(ciclo.saldo.getIdClienteSICAP(), ciclo.saldo.getIdSolicitudSICAP(), ciclo.saldo.getReferencia());
                                        ciclo.saldo.setNumeroCuotasTranscurridas(numPagoAmort);
                                        ciclo.saldo.setSaldoInteres(saldoInteres - ciclo.saldo.getInteresNormalPagado());
                                        ciclo.saldo.setSaldoInteresVigente(tablaAmort[numPagoAmort].getInteres());
                                        ciclo.saldo.setSaldoIvaInteres(saldoIVAInteres - ciclo.saldo.getIvaInteresNormalPagado());
                                        ciclo.saldo.setFechaSigAmortizacion(tablaAmort[numPagoAmort].getFechaPago());
                                        ciclo.saldo.setCapitalSigAmortizacion(tablaAmort[numPagoAmort].getAbonoCapital());
                                        ciclo.saldo.setInteresSigAmortizacion(tablaAmort[numPagoAmort].getInteres());
                                        ciclo.saldo.setIvaSigAmortizacion(tablaAmort[numPagoAmort].getIvaInteres());
                                        ciclo.saldo.setCapitalVencido(saldoCapital - ciclo.saldo.getCapitalPagado());
                                        ciclo.saldo.setSaldoInteresVencido(ciclo.saldo.getSaldoInteres() - ciclo.saldo.getSaldoInteresVigente());
                                        ciclo.saldo.setIvaInteresVencido(saldoIVAInteres - ciclo.saldo.getIvaInteresNormalPagado() - tablaAmort[numPagoAmort].getIvaInteres());
                                        ciclo.saldo.setTotalVencido(ciclo.saldo.getCapitalVencido() + ciclo.saldo.getSaldoInteresVencido() + ciclo.saldo.getIvaInteresVencido());
                                        if (ciclo.saldo.getTotalVencido() > 0) {
                                            ciclo.saldo.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                                            credito.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA);
                                            creditoAntDAO.updatePagoCredito(credito);
                                        }
                                        saldoAntDAO.updateSaldo(ciclo.saldo);
                                    }
                                    myLogger.info("Devengando intereses pendientes");
                                    TablaDevengamientoHelper.devengamientoAtrasado(credito, Convertidor.toSqlDate(FechasUtil.getRestarDias(Convertidor.toSqlDate(sqlDate), 1)));
                                }
                                grupo.ciclos[idCiclo - 1] = ciclo;
                                // Calculamos de monto con seguro financiado para Interciclo Agosto 2017
                                int integrantesLenght = grupo.ciclos[idCiclo - 1].integrantes.length;
                                // Calculamos el costo con seguro financiado 
                                for (int k = 0; k < integrantesLenght; k++) {
                                    // Calculamos el monto con seguro financiado Agosto 2017
                                    IntegranteCicloVO integrante = grupo.ciclos[idCiclo - 1].integrantes[k];
                                    if (integrante.montoConSeguro == 0) {
                                        integrante.montoConSeguro = integrante.monto;
                                        integrante.montoConSeguroTemp = integrante.montoConSeguro;
                                    }
                                    // Solo si tiene un seguro contratado se muestra el costo del seguro
                                    if (integrante.segContratado == SeguroConstantes.CONTRATACION_SI) {
                                        // Agregamos el costo del seguro Agosto 2017
                                        int tipoSeguro = integrante.tipoSeguro;
                                        SucursalVO sucursalVo = new SucursalDAO().getSucursal(integrante.idSucursal);
                                        double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                                        integrante.costoSeguro = costoSeguroContratado;
                                    }
                                }
                                session.setAttribute("GRUPO", grupo);
                            } else {
                                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El ciclo crédito no se pudo registrar. Verifique que no haya sido dispersado previamente."));
                                conn.rollback();
                            }
                        } else {
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Error al actualizar ordenes de pago."));
                            conn.rollback();
                        }
                        // Aqui va la forma de reprocesar el pago
                    }//termina else validacion pago 10% credito

                }// termina if ciclo confirmado		                
                else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El ciclo ya se encuentra confirmado."));
                }
                request.setAttribute("NOTIFICACIONES", notificaciones);
                conn.close();
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}

package com.sicap.clientes.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import com.sicap.clientes.dao.SaldoHistoricoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.cartera.EventoDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.cartera.DevengamientoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.CreditoHelperCartera;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.MailUtil;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoHistoricoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DevengamientoVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import com.sicap.clientes.vo.cartera.EventosVO;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;


public class CommandCierreDia implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCierreDia.class);

    public CommandCierreDia(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        EventoDAO eventoDAO = new EventoDAO();
        TablaAmortDAO tablaAmortdao = new TablaAmortDAO();
        CifraControlDAO cifraControlDAO = new CifraControlDAO();
        GrupoDAO grupoDao =new GrupoDAO();
        GrupoHelper grupoHelper = new GrupoHelper();
        DevengamientoDAO devenDAO = new DevengamientoDAO();
        CreditoHelperCartera creditoHelper = new CreditoHelperCartera();
        TablaAmortHelper tablaAmortHelp = new TablaAmortHelper();
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        EventoHelper eventoHelper = new EventoHelper();
        ParametroVO parametroVO = new ParametroVO();
        EventosVO eventoVO = new EventosVO();
        ArrayList<DevengamientoVO> arrDevenVO = new ArrayList<DevengamientoVO>();
        HttpSession session = request.getSession();
        Timestamp fechaTime = new Timestamp(System.currentTimeMillis());
        String Destinatarios = CatalogoHelper.getParametro("MAIL_CIERRE_RECIPIENT");
        myLogger.debug("Destinatarios: " + Destinatarios);

        MailUtil Mail = new MailUtil();

        boolean migracion = false;
        String ultimaFecha = null;
        boolean procesandoCierre = false;
        try {
            /* REGISTRO DE PAGOS  JBL-JUN10 
             * Extrae todos los pagos pendientes por aplicar y actualiza el saldo en la tabla de creditos
             */
            BitacoraUtil bitutil = new BitacoraUtil(0, request.getRemoteUser(), "CommandCierreDia");
            String fechaCierreJSP = HTMLHelper.getParameterString(request, "fechaCierre");

            procesandoCierre = catalogoDAO.ejecutandoCierre();
            myLogger.info("PROCESANDO: " + procesandoCierre);
            ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaUltimoCierre = sdf.parse(ultimaFecha);
            myLogger.info("Fecha de Cierre CEC " + ultimaFecha.toString());
            myLogger.info("Fecha Cierre JSP: " + fechaCierreJSP);
            if (!procesandoCierre) {
                if (fechaCierreJSP.equals(ultimaFecha)) {
                    int horaEspera = Integer.valueOf(CatalogoHelper.getParametro("HORA_CIERRE_MES"));
                    if(horaEspera>0)
                        catalogoDAO.updateParametro("HORA_CIERRE_MES", "0");
                    bitutil.registraEventoString("Inicia cierre de dia con fecha :" + ultimaFecha);
                    catalogoDAO.banderaCierreDia(1);
                    Mail.enviaCorreo("Inicio Cierre " + ultimaFecha, "inicio del cierre de dia en el sistema", Destinatarios);
                    myLogger.info("Se actualiza bandera 1");
                    Date[] fechasInhabiles = (Date[]) session.getAttribute("INHABILES");

                    if (siguiente.contentEquals("/cierreDiaMigracion.jsp")) {
                        migracion = true;
                    }

                    PagoVO[] pagos_pend = null;
                    CreditoCartVO creditoVO = new CreditoCartVO();
                    PagoDAO pagos = new PagoDAO();
                    pagos_pend = pagos.getNoEnviados();
                    int numDiasPaso = 0;
                    numDiasPaso = obtieneNumeroDias();
                    // Temporal para que solo procese un dia
                    numDiasPaso = 1;
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(fechaUltimoCierre);
                    int year = c1.get(Calendar.YEAR);
                    int month = c1.get(Calendar.MONTH);
                    int day = c1.get(Calendar.DATE);
                    String fechaFin = year + "-" + month + "-" + day;

                    // Actualiza saldos con los pagos pendientes por enviar
                    myLogger.info("Procesara pagos :" + pagos_pend.length);
                    for (int j = 0; j < pagos_pend.length; j++) {
                        //				if (CreditoHelperCartera.actualizaSaldoCuenta(pagos_pend[j].referencia, pagos_pend[j].monto)){
                        if (CreditoHelperCartera.actualizaSaldoCuenta(pagos_pend[j])) {
                            pagos_pend[j].enviado = 1;
                            pagos.updatePagosCartera(pagos_pend[j], pagos_pend[j].referencia);
                            creditoVO = creditoCartDAO.getCreditoReferencia(pagos_pend[j].referencia);
                            // Se actualiza la tabla de transacciones
                            transacHelper.registraPago(creditoVO, pagos_pend[j]);
                        }
                        myLogger.info("Proceso pago :" + j);
                    }
                    bitutil.registraEventoString("Finaliza aplicación de pagos");

                    /* APLICACION DE PAGOS  JBL-JUN10 
                     * Trae todos los creditos con status vigente, moroso y vencido para procesar 
                     * Obtiene la fecha de ultimo cierre y calcula el numero de dias sobre los que tiene que cerrar
                     */
                    CreditoCartVO[] credito = null;

                    credito = creditoCartDAO.getCreditoVig();
                    myLogger.info("Procesando creditos vigentes:");
                    myLogger.info("numero de creditos. " + credito.length);

                    TablaAmortDAO tablaAmort = new TablaAmortDAO();
                    double remanente = 0;
                    double saldo_a_liquidar = 0.00;
                    double saldo_a_liquidar_capital = 0.00;
                    double saldo_a_favor = 0.00;
                    SaldoIBSVO saldoIni = new SaldoIBSVO();
                    TablaAmortVO tablaAmortVig = new TablaAmortVO();
                    double Tolerancia = Double.parseDouble(CatalogoHelper.getParametro("TOLERANCIA_LIQ"));
                    double PagoLiq = 0;
                    
                    for (int i = 0; i < numDiasPaso; i++) {
                        PagoLiq = 0;
                        myLogger.info("procesando dia no. " + i);
                        for (int j = 0; j < credito.length; j++) {
                            
                            myLogger.info("Procesando credito: " + credito[j].getNumCredito());
                            myLogger.info("monto cuenta " + (credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada()));
                            /*
                             * En caso de migracion verifica que liquide el monto total proyectado (con intereses al final) en caso contrario
                             * verificara el saldo al dia  JBL AGO-13
                             */
                            saldoIni = this.getSaldo(credito[j].getReferencia());
                            if (migracion) {
                                saldo_a_liquidar = this.getSaldoInsolutoAlCompromiso(credito[j].getReferencia());
                                saldo_a_liquidar_capital = this.getSaldoCapital(credito[j].getReferencia());
                            } else {
                                saldo_a_liquidar = saldoIni.getSaldoConInteresAlFinal();
                            }
                            myLogger.info("saldo a liquidar " + saldo_a_liquidar);
                            myLogger.info("saldo a liquidar capital " + saldo_a_liquidar_capital);
                            if(credito[j].getMontoCuentaCongelada() != 0)
                                saldo_a_favor = obtenSaldoFavor(credito[j], saldoIni, c1.getTime());
                            else
                                credito[j].setAplicaGarantia(false);
                            /* APLICACION DE PAGOS
                             * Verifica primero si el monto en la cuenta es suficiente para liquidar el crédito
                             * En caso contrario verifica si tiene un monto pendiente por aplicar, si es moroso y 
                             * si tiene vencimiento, en caso afirmativo se aplica el pago
                             */
                            myLogger.info("Tolerancia Inicial "+Tolerancia);
                            myLogger.info("Monto Cuenta "+credito[j].getMontoCuenta());
                            myLogger.info("Monto Cuenta Congelada "+credito[j].getMontoCuentaCongelada());

                            if (((credito[j].getMontoCuenta()+Tolerancia) - credito[j].getMontoCuentaCongelada()) >= saldo_a_liquidar) {
                                
                                if (((credito[j].getMontoCuenta()) - credito[j].getMontoCuentaCongelada()) >= saldo_a_liquidar){
                                   Tolerancia = 0; 
                                   PagoLiq = 0;
                                }
                                else{
                                    PagoLiq = Math.ceil(saldo_a_liquidar-(credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada()));
                                    myLogger.info("Entra a la transaccion por liquidacion");
                                    java.util.Date utilDate = new java.util.Date();
                                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                                    int IdparaPolizaContable = cifraControlDAO.getIdCifraControl();
                                    PagoDAO pagocarteradao = new PagoDAO();
                                    PagoVO PagoTolerancia = new PagoVO();
                                    PagoTolerancia.monto = PagoLiq;
                                    PagoTolerancia.referencia = saldoIni.getReferencia();
                                    PagoTolerancia.idContable=IdparaPolizaContable;
                                    PagoTolerancia.bancoReferencia = ClientesConstants.ID_BANCO_EFECTIVO;
                                    PagoTolerancia.fechaPago = sqlDate;
                                    PagoTolerancia.fechaHora = new Timestamp(System.currentTimeMillis());
                                    PagoTolerancia.sucursal = saldoIni.getReferencia().substring(0, 3);
                                    PagoTolerancia.status= 6;
                                    PagoTolerancia.destino = 1;
                                    PagoTolerancia.enviado = 1;
                                    //PagoTolerancia.numCuenta = 0;
                                    PagoTolerancia.numCuenta = new CuentasBancariasDAO().getNumCuentaBancaria(ClientesConstants.ID_BANCO_EFECTIVO, "00070", "C");
                                    pagocarteradao.insertPagoCartera(PagoTolerancia);
                                    transacHelper.registraPago(credito[j], PagoTolerancia);
                                    
                                }
                                myLogger.info("Tolerancia Maxima= "+Tolerancia);
                                myLogger.info("Pago liqui con tolerancia"+ PagoLiq);
                                // Se liquida el credito
                                myLogger.info("se liquida credito " + credito[j].getNumCliente());
                                TablaAmortVO tablaAmortLiq; 
                                tablaAmortLiq = tablaAmortdao.getDivVigente(credito[j].getNumCliente(), credito[j].getNumCredito());
                                remanente = remanente+PagoLiq;
                                myLogger.info("Remanante = "+remanente);
                                remanente = TablaAmortHelper.aplicaPagosTabla(credito[j], credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada()+PagoLiq, Convertidor.toSqlDate(tablaAmortLiq.fechaPago), false, 0, Convertidor.toSqlDate(c1.getTime()));
                                CreditoCartVO creditoliq = null;
                                creditoliq = creditoCartDAO.getCredito(credito[j].getNumCredito());
                                // Solo en caso de existir un remanente despues de aplicar los pagos se llama al metod de liquidar
                                if (remanente > 0 && creditoliq.getStatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO) {
                                    remanente = TablaAmortHelper.liquidaTablaProyectado(credito[j], remanente, Convertidor.toSqlDate(c1.getTime()));                                    
                                }
                                grupoHelper.cierraCiclo(credito[j].getNumCliente(),credito[j].getNumSolicitud());
                                //}else if (((credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada())  > 0 &&( credito[j].getStatus()  > ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE && credito[j].getNumDiasMora() > 0) || ( (credito[j].getMontoCuenta()-credito[j].getMontoCuentaCongelada()) >= 0 && tablaAmort.esVencimiento(credito[j].getNumCliente(), credito[j].getNumCredito(), Convertidor.toSqlDate(c1.getTime()))))){
                            } else if (((credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada()) > 0 && (credito[j].getStatus() > ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) || ((credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada()) > 0 && tablaAmort.esVencimiento(credito[j].getNumCliente(), credito[j].getNumCredito(), Convertidor.toSqlDate(c1.getTime()))))) {
                                // Se pagan los dividendos vigentes y vencidos
                                remanente = TablaAmortHelper.aplicaPagosTabla(credito[j], credito[j].getMontoCuenta() - credito[j].getMontoCuentaCongelada(), Convertidor.toSqlDate(c1.getTime()), false, 0, Convertidor.toSqlDate(c1.getTime()));
                            }
                            /* PROVISION DE INTERESES
                             * Verifica si es vencimiento o fin de mes  - el fin de mes se deshabilita JBL AGO-13
                             */
                            if (((tablaAmort.esVencimientoProv(credito[j].getNumCliente(), credito[j].getNumCredito(), Convertidor.toSqlDate(c1.getTime()))) && credito[j].getStatus() < ClientesConstants.SITUACION_CREDITO_SALDOST24_CANCELADO && credito[j].getStatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO)) {
                                /* Se obtiene el dividendo vigente de la tabla de amoritizacion y se busca en la tabla de enventos por el ultimo de provision
                                 * para en base a ese calcular el número de días a provisionar
                                 */
                                boolean esVencimiento = false;
                                if (tablaAmort.esVencimientoProv(credito[j].getNumCliente(), credito[j].getNumCredito(), Convertidor.toSqlDate(c1.getTime()))) {
                                    esVencimiento = true;
                                }

                                myLogger.info("entra a provision de intereses");
                                TablaAmortVO tablaAmortPr = new TablaAmortVO();
                                TablaAmortHelper tablaHelperPr = new TablaAmortHelper();
                                TablaAmortDAO tablaDAOPr = new TablaAmortDAO();
                                ArrayList<RubrosVO> array_rubro = new ArrayList<RubrosVO>();
                                ArrayList<RubrosVO> array_rubro_ce = new ArrayList<RubrosVO>();
                                RubrosVO rubro_prov = new RubrosVO();
                                RubrosVO rubro_ce = new RubrosVO();
                                RubrosVO elementos[] = null;
                                Date fechaIniDiv = new Date();
                                Date fechaFinDiv = Convertidor.toSqlDate(c1.getTime());
                                SaldoIBSDAO saldoIBS = new SaldoIBSDAO();
                                SaldoIBSVO saldoVO = new SaldoIBSVO();
                                // Se obitiene el saldo del crédito actual
                                saldoVO = saldoIBS.getSaldo(credito[j].getNumCliente(), credito[j].getNumCredito());
                                // Variables de la tabla de saldos
                                double saldo_interes = saldoVO.getSaldoInteres();
                                double saldo_iva_interes = saldoVO.getSaldoIvaInteres();
                                double saldo_interes_vigente = saldoVO.getSaldoInteresVigente();
                                double saldo_interes_vencido = saldoVO.getSaldoInteresVencido();
                                double saldo_iva_interes_vencido = saldoVO.getIvaInteresVencido();
                                myLogger.info("saldo interes vencido en prov " + saldo_interes_vencido);
                                double saldo_total_al_dia = saldoVO.getSaldoTotalAlDia();
                                double saldo_interes_ctas_orden = saldoVO.getSaldoInteresCtasOrden();
                                double saldo_interes_vencido_90 = saldoVO.getSaldoInteresVencido90dias();
                                double capital_vencido = saldoVO.getCapitalVencido();
                                double capital = saldoVO.getSaldoCapital();
                                double total_vencido = saldoVO.getTotalVencido();
                                double cap_sig_cuota = saldoVO.getCapitalSigAmortizacion();
                                double int_sig_cuota = saldoVO.getInteresSigAmortizacion();
                                double iva_int_sig_cuota = saldoVO.getIvaSigAmortizacion();
                                double saldo_multa = saldoVO.getSaldoMulta();
                                double saldo_iva_multa = saldoVO.getSaldoIVAMulta();
                                double saldo_mora = saldoVO.getSaldoMora();
                                double saldo_iva_mora = saldoVO.getSaldoIVAMora();
                                int numero_dias_trans = saldoVO.getDiasTranscurridos();
                                int numero_dias = 0;
                                int numero_cuotas_trans = saldoVO.getNumeroCuotasTranscurridas();
                                int numero_cuotas_vencidas = saldoVO.getCuotasVencidas();
                                Date fecha_sig_amortizacion = saldoVO.getFechaSigAmortizacion();
                                Date fecha_incumplimiento = saldoVO.getFechaIncumplimiento();
                                int estatus = saldoVO.getEstatus();
                                int numero_dias_mora = saldoVO.getDiasMora();
                                int numero_pago_sostenido = saldoVO.getNumPagoSostenido();
                                int periodicidad = saldoVO.getPeriodicidad();
                                double tasa_interes = saldoVO.getTasaInteresSinIVA();
                                double tasa_iva = saldoVO.getTasaIVA();
                                int numero_dias_arriba_vencido = 0;
                                double interes_periodo = 0;
                                double iva_interes_periodo = 0;
                                boolean cambio_estado = false;
                                boolean cambio_estado_vigente = false;
                                // Se obtienen los dias del periodo
                                // Se obtiene la fecha del ultimo evento de provison
                                eventoVO = eventoDAO.getLastEventoTipo(credito[j].getNumCliente(), credito[j].getNumCredito(), "PRV");
                                if (eventoVO == null) {
                                    eventoVO = new EventosVO();
                                    fechaIniDiv = credito[j].getFechaDesembolso();
                                    numero_dias = FechasUtil.inBetweenDays(fechaIniDiv, fechaFinDiv);
                                    //eventoVO.fechaFin = Convertidor.toSqlDate(fechaIniDiv);
                                    fechaTime.setDate(fechaIniDiv.getDate() + 1); // se aumenta un día a la fecha límite
                                    fechaTime.setMonth(fechaIniDiv.getMonth());
                                    fechaTime.setYear(fechaIniDiv.getYear());
                                    eventoVO.fechaFin = fechaTime;/*<-----------------------------------------*/

                                } else {
                                    numero_dias = FechasUtil.inBetweenDays(eventoVO.fechaFin, fechaFinDiv);
                                }

                                // Se verifica que la fecha del evento no sea igual a la fecha del dia, en caso afirmativo se sale del ciclo
                                if (FechasUtil.inBetweenDays(eventoVO.fechaFin, c1.getTime()) == 0) {
                                    myLogger.info("El dividendo ya se provisiono");
                                } else {
                                    tablaAmortPr = tablaDAOPr.getDivVigenteProv(credito[j].getNumCliente(), credito[j].getNumCredito(), Convertidor.toSqlDate(c1.getTime()));
                                    // Se verifica que exista un dividendo vigente
                                    if (tablaAmortPr == null) {
                                        myLogger.info("no se encontro dividendo vigente");
                                    } else {
                                        // Verifica si el dividendo está pagado
                                        myLogger.info("Numero dividendo Dividendo" + tablaAmortPr.numPago + "pagado " + tablaAmortPr.pagado);
                                        if (tablaAmortPr.pagado.equalsIgnoreCase(ClientesConstants.DIVIDENDO_PAGADO)) {
                                            numero_dias_mora = 0;
                                            estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE;
                                            myLogger.info("Entra al siguiente dividendo" + tablaAmortPr.numPago + 1);
                                            // Se coloca el siguiente Dividendo como vigente, suponiendo que aun hay dividendos pendientes
                                            if (tablaAmortPr.numPago < credito[j].getNumCuotas()) {
                                                /*
                                                 * Unicamente se actualizan saldos y se coloca al siguiente dividendo como vigente 
                                                 * cuando es vencimiento y no cuando es fin de mes JBL JUL-13
                                                 */
                                                if (esVencimiento) {
                                                    TablaAmortVO tablaSigDiv = new TablaAmortVO();
                                                    TablaAmortVO[] tabla = null;
                                                    tablaSigDiv = tablaDAOPr.getSiguienteDiv(credito[j].getNumCliente(), credito[j].getNumCredito(), (tablaAmortPr.numPago + 1));
                                                    /* Se añade un control para el caso en que el siguiente dividendo tenga un pago adelantado
                                                     * 
                                                     */
                                                    if (tablaSigDiv != null) {
                                                        if (!tablaSigDiv.pagado.contentEquals(ClientesConstants.DIVIDENDO_PAGADO) && tablaSigDiv.status == ClientesConstants.DIVIDENDO_NO_VIGENTE) {

                                                            tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;
                                                            myLogger.info("coloca el siguiente status " + tablaAmortPr.status);
                                                            tablaDAOPr.updateSaldosTablaAmort(tablaSigDiv);
                                                            // Se actualizan los datos de la tabla de saldos
                                                            saldo_interes = saldo_interes + tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            saldo_iva_interes = saldo_iva_interes + tablaSigDiv.ivaInteres - tablaSigDiv.ivaInteresPagado;
                                                            saldo_interes_vigente = saldo_interes_vigente + tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            cap_sig_cuota = tablaSigDiv.abonoCapital;
                                                            int_sig_cuota = tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            iva_int_sig_cuota = tablaSigDiv.ivaInteres - tablaSigDiv.ivaInteresPagado;
                                                            fecha_sig_amortizacion = tablaSigDiv.fechaPago;
                                                            estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE;
                                                            saldo_total_al_dia = saldoVO.getSaldoTotalAlDia() + saldo_interes + saldo_iva_interes;
                                                            myLogger.info("Saldo total al dia en provision vigente " + saldo_total_al_dia);
                                                            saldo_interes_vencido = 0;
                                                            saldo_iva_interes_vencido = 0;
                                                            capital_vencido = 0;
                                                            total_vencido = 0;
                                                            numero_dias_trans = numero_dias_trans + numero_dias;
                                                            numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        } else {
                                                            numero_dias_trans = numero_dias_trans + numero_dias;
                                                            numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        }
                                                    } else {
                                                        numero_dias_trans = numero_dias_trans + numero_dias;
                                                        numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        cap_sig_cuota = 0;
                                                        int_sig_cuota = 0;
                                                        iva_int_sig_cuota = 0;
                                                    }
                                                    if (tablaSigDiv == null) {
                                                        tabla = tablaDAOPr.getDivPagoNoVigente(credito[j].getNumCliente(), credito[j].getNumCredito());
                                                        if (tabla != null) {
                                                            tablaSigDiv = tabla[0];
                                                        }
                                                    }
                                                    tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;
                                                    tablaDAOPr.updateSaldosTablaAmort(tablaSigDiv);
                                                    // Se actualizan los datos de la tabla de saldos
                                                }

                                            } else if (tablaAmortPr.numPago == credito[j].getNumCuotas()) {
                                                numero_cuotas_trans = numero_cuotas_trans + 1;
                                                estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE;
                                                cap_sig_cuota = 0;
                                                int_sig_cuota = 0;
                                                iva_int_sig_cuota = 0;
                                            }
                                        } else if (tablaAmortPr.pagado.equalsIgnoreCase(ClientesConstants.DIVIDENDO_PAGADO) && credito[j].getStatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                            myLogger.debug("Entra al siguiente dividendo" + tablaAmortPr.numPago + 1);
                                            /* 
                                             * Se incrementa el número de pagos sostenidos JBL SEP-14
                                             */
                                            numero_pago_sostenido = numero_pago_sostenido + 1;
                                            // Se coloca el siguiente Dividendo como vigente, suponiendo que aun hay dividendos pendientes
                                            if (tablaAmortPr.numPago < credito[j].getNumCuotas()) {
                                                /*
                                                 * Unicamente se actualizan saldos y se coloca al siguiente dividendo como vigente 
                                                 * cuando es vencimiento y no cuando es fin de mes JBL JUL-13
                                                 */
                                                if (esVencimiento) {
                                                    TablaAmortVO tablaSigDiv = new TablaAmortVO();
                                                    TablaAmortVO[] tabla = null;
                                                    tablaSigDiv = tablaDAOPr.getSiguienteDiv(credito[j].getNumCliente(), credito[j].getNumCredito(), (tablaAmortPr.numPago + 1));
                                                    /* Se añade un control para el caso en que el siguiente dividendo tenga un pago adelantado
                                                     * 
                                                     */
                                                    if (tablaSigDiv != null) {
                                                        if (!tablaSigDiv.pagado.contentEquals(ClientesConstants.DIVIDENDO_PAGADO) && tablaSigDiv.status == ClientesConstants.DIVIDENDO_NO_VIGENTE) {

//                                                    tablaSigDiv.status = ClientesConstants.DIVIDENDO_RESOLUCION;
                                                            tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;
                                                            myLogger.debug("coloca el siguiente status " + tablaAmortPr.status);
                                                            tablaDAOPr.updateSaldosTablaAmort(tablaSigDiv);
                                                            // Se actualizan los datos de la tabla de saldos
                                                            saldo_interes = saldo_interes + tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            saldo_iva_interes = saldo_iva_interes + tablaSigDiv.ivaInteres - tablaSigDiv.ivaInteresPagado;
                                                            saldo_interes_ctas_orden = saldo_interes_ctas_orden + tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            cap_sig_cuota = tablaSigDiv.abonoCapital;
                                                            int_sig_cuota = tablaSigDiv.interes - tablaSigDiv.interesPagado;
                                                            iva_int_sig_cuota = tablaSigDiv.ivaInteres - tablaSigDiv.ivaInteresPagado;
                                                            fecha_sig_amortizacion = tablaSigDiv.fechaPago;
                                                            estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO;
                                                            saldo_total_al_dia = saldoVO.getSaldoTotalAlDia() + saldo_interes + saldo_iva_interes;
                                                            myLogger.debug("Saldo total al dia en provision credito vencido " + saldo_total_al_dia);
                                                            // Se suma el interes generado al total vencido en caso de que el dividendo no sea vigente
                                                            if (tablaAmortPr.status > ClientesConstants.DIVIDENDO_VIGENTE) {
                                                                total_vencido = total_vencido + (tablaSigDiv.interes - tablaSigDiv.interesPagado) + (tablaSigDiv.ivaInteres - tablaSigDiv.ivaInteresPagado);
                                                            }
                                                            numero_dias_trans = numero_dias_trans + numero_dias;
                                                            numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        } else {
                                                            numero_dias_trans = numero_dias_trans + numero_dias;
                                                            numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        }
                                                    } else {
                                                        numero_dias_trans = numero_dias_trans + numero_dias;
                                                        numero_cuotas_trans = numero_cuotas_trans + 1;
                                                        cap_sig_cuota = 0;
                                                        int_sig_cuota = 0;
                                                        iva_int_sig_cuota = 0;
                                                    }
                                                    if (tablaSigDiv == null) {
                                                        tabla = tablaDAOPr.getDivPagoNoVigente(credito[j].getNumCliente(), credito[j].getNumCredito());
                                                        if (tabla != null) {
                                                            tablaSigDiv = tabla[0];
                                                        }
                                                    }
//                                            tablaSigDiv.status = ClientesConstants.DIVIDENDO_RESOLUCION;
                                                    tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;
//                                            Logger.debug("coloca el siguiente status " + tablaAmortPr.status);
                                                    tablaDAOPr.updateSaldosTablaAmort(tablaSigDiv);

                                                }

                                            } else if (tablaAmortPr.numPago == credito[j].getNumCuotas()) {
                                                numero_cuotas_trans = numero_cuotas_trans + 1;
                                                estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO;
                                                cap_sig_cuota = 0;
                                                int_sig_cuota = 0;
                                                iva_int_sig_cuota = 0;
                                            }
                                            if (numero_pago_sostenido >= ClientesConstants.NUM_PAGO_SOSTENIDO) {
                                                cambio_estado_vigente = true;
                                            }
                                            if (cambio_estado_vigente) {
                                                saldo_interes_vigente = saldo_interes_ctas_orden;
                                                saldo_interes_ctas_orden = 0;
                                                estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE;
                                            }

                                            /*
                                             * Dividendos morosos
                                             */
                                        } else {
                                            // Se cambia el status del dividendo actual a moroso
                                            tablaAmortPr.status = ClientesConstants.DIVIDENDO_MOROSO;

                                            numero_dias_trans = numero_dias_trans + numero_dias;
                                            if (estatus == ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE) {
                                                estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA;
                                            }
                                            numero_cuotas_vencidas = numero_cuotas_vencidas + 1;
                                            // Se calcula el numero de dias vencidos para saber si existe un cambio de estado
                                            if (estatus < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                                numero_dias_mora = numero_dias_mora + 1;
                                            }
                                            if (numero_dias_mora > ClientesConstants.DIAS_MORA_VENCIDO && estatus < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                                cambio_estado = true;
                                                myLogger.info("cambio de estado en provision ");
                                                numero_dias_arriba_vencido = numero_dias_mora - ClientesConstants.DIAS_MORA_VENCIDO;
                                            }
                                            // En caso de existir un cambio de estado se inserta un evento y se cambia el status
                                            if (cambio_estado) {
                                                //eventoHelper.registraCambioEstadoVen(credito[j], tablaAmortPr, numero_dias_mora, fechaIniDiv, c1.getTime(), capital, saldo_total_al_dia);
                                                eventoHelper.registraCambioEstadoVen(credito[j], tablaAmortPr, numero_dias_mora, fechaIniDiv, c1.getTime(), capital, saldoVO.getSaldoConInteresAlFinal());
                                                myLogger.debug("Registro evento de cambio a vencido");
                                                estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO;
                                                tablaAmortPr.status = ClientesConstants.DIVIDENDO_VENCIDO;
                                                tablaDAOPr.updateStatusVenTablaAmort(tablaAmortPr);
                                            }
                                            // Se llevan los totales de interes de acuerdo a la situacion del credito
                                            if (estatus == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                                // En caso de cambio de estado se calcula solamente la parte proporcional que supera el limite de vencido
//										tablaAmortPr.status = ClientesConstants.DIVIDENDO_RESOLUCION;
//										tablaAmortPr.status = ClientesConstants.DIVIDENDO_MOROSO;
                                                TablaAmortVO tablaSigDiv = new TablaAmortVO();
                                                tablaSigDiv = tablaDAOPr.getSiguienteDiv(credito[j].getNumCliente(), credito[j].getNumCredito(), (tablaAmortPr.numPago + 1));
                                                if (cambio_estado) {
                                                    saldo_interes_vencido = saldoVO.getSaldoInteresVencido() + tablaAmortPr.interes - tablaAmortPr.interesPagado;
                                                    saldo_iva_interes_vencido = Convertidor.getMontoIva(saldo_interes_vencido, credito[j].getNumSucursal(), 2);
                                                    saldo_interes_vencido_90 = saldo_interes_vencido;
                                                } else {
                                                    saldo_interes_vencido = saldoVO.getInteresVencido() + tablaAmortPr.interes - tablaAmortPr.interesPagado;
                                                    saldo_iva_interes_vencido = Convertidor.getMontoIva(saldo_interes_vencido, credito[j].getNumSucursal(), 2);
                                                }
                                            } else { // Suponemos que el credito tiene status de moroso
                                                saldo_interes_vencido = saldoVO.getSaldoInteresVencido() + tablaAmortPr.interes - tablaAmortPr.interesPagado;
                                                myLogger.info("saldo interes vencido en prov final" + saldo_interes_vencido);
                                                saldo_iva_interes_vencido = Convertidor.getMontoIva(saldo_interes_vencido, credito[j].getNumSucursal(), 2);
                                            }
                                            // se calcula el capital moroso para el total vencido
                                            capital_vencido = capital_vencido + tablaAmortPr.abonoCapital - tablaAmortPr.capitalPagado;
                                            total_vencido = capital_vencido + saldo_interes_vencido + saldo_iva_interes_vencido + saldo_multa + saldo_iva_multa + saldo_mora + saldo_iva_mora + saldo_interes_vencido_90;
                                            myLogger.info("capital vencido " + capital_vencido);
                                            myLogger.info("total vencido " + total_vencido);
                                            if (fecha_incumplimiento == null) {
                                                fecha_incumplimiento = tablaAmortPr.fechaPago;
                                            }
                                            tablaDAOPr.updateSaldosTablaAmort(tablaAmortPr);

                                            // En caso de existir un siguiente dividendo, se actualizan los datos
                                            if (tablaAmortPr.numPago < credito[j].getNumCuotas()) {
                                                myLogger.info("Entra al siguiente dividendo desde vencido ");
                                                TablaAmortVO tablaSigDiv = new TablaAmortVO();
                                                tablaSigDiv = tablaDAOPr.getSiguienteDiv(credito[j].getNumCliente(), credito[j].getNumCredito(), (tablaAmortPr.numPago + 1));
                                                if (estatus == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                                    tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;
                                                    //												tablaSigDiv.status = ClientesConstants.DIVIDENDO_RESOLUCION;
                                                } else { // Suponemos que el credito tiene status de moroso
                                                    tablaSigDiv.status = ClientesConstants.DIVIDENDO_VIGENTE;

                                                }
                                                tablaDAOPr.updateSaldosTablaAmort(tablaSigDiv);
                                                saldo_interes = saldo_interes + tablaSigDiv.interes;
                                                saldo_iva_interes = saldo_iva_interes + tablaSigDiv.ivaInteres;
                                                if (estatus == ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                                    saldo_interes_ctas_orden = saldo_interes_ctas_orden + tablaSigDiv.interes;
                                                } else {
                                                    saldo_interes_vigente = tablaSigDiv.interes;
                                                }
                                                fecha_sig_amortizacion = tablaSigDiv.fechaPago;
                                                numero_cuotas_trans = numero_cuotas_trans + 1;
                                                cap_sig_cuota = tablaSigDiv.abonoCapital;
                                                int_sig_cuota = tablaSigDiv.interes;
                                                iva_int_sig_cuota = tablaSigDiv.ivaInteres;
                                                // No se suma el interes vencido debido a que ya se sumo al saldo en la anterior amortizacion o en el desembolso
                                                saldo_total_al_dia = saldoVO.getSaldoTotalAlDia() + tablaSigDiv.interes + tablaSigDiv.ivaInteres;
                                                myLogger.info("Saldo total al dia en provision vencido " + saldo_total_al_dia);

                                            } else if (tablaAmortPr.numPago == credito[j].getNumCuotas()) {
                                                numero_cuotas_trans = numero_cuotas_trans + 1;
                                                numero_cuotas_vencidas = numero_cuotas_vencidas + 1;
                                                // El interes vigente pasa a interes vencido.
                                                saldo_interes_vigente = 0;
                                                cap_sig_cuota = 0;
                                                int_sig_cuota = 0;
                                                iva_int_sig_cuota = 0;
                                            }
                                        }
                                        // Se actualiza la tabla de Saldos
                                        saldoVO.setSaldoInteres(saldo_interes);
                                        saldoVO.setSaldoIvaInteres(saldo_iva_interes);
                                        saldoVO.setSaldoInteresVigente(saldo_interes_vigente);
                                        myLogger.info("interes vencido prov al actualizar " + saldo_interes_vencido);
                                        saldoVO.setSaldoInteresVencido(saldo_interes_vencido);
                                        saldoVO.setCapitalVencido(capital_vencido);
                                        saldoVO.setCapitalSigAmortizacion(cap_sig_cuota);
                                        saldoVO.setInteresSigAmortizacion(int_sig_cuota);
                                        saldoVO.setIvaSigAmortizacion(iva_int_sig_cuota);
                                        saldoVO.setIvaInteresVencido(saldo_iva_interes_vencido);
                                        saldoVO.setSaldoTotalAlDia(saldo_total_al_dia);
                                        saldoVO.setTotalVencido(total_vencido);
                                        saldoVO.setFechaSigAmortizacion(Convertidor.toSqlDate(fecha_sig_amortizacion));
                                        if (fecha_incumplimiento != null) {
                                            saldoVO.setFechaIncumplimiento(Convertidor.toSqlDate(fecha_incumplimiento));
                                        }
                                        saldoVO.setDiasMora(numero_dias_mora);
                                        saldoVO.setNumeroCuotasTranscurridas(numero_cuotas_trans);
                                        saldoVO.setCuotasVencidas(numero_cuotas_vencidas);
                                        saldoVO.setDiasTranscurridos(numero_dias_trans);
                                        saldoVO.setNumPagoSostenido(numero_pago_sostenido);
                                        saldoVO.setSaldoInteresCtasOrden(saldo_interes_ctas_orden);
                                        saldoVO.setEstatus(estatus);
                                        saldoVO.setFechaGeneracion(Convertidor.toSqlDate(c1.getTime()));
                                        saldoVO.setCtaContable(saldoVO.getCtaContable());
                                        // Se verifica si existio cambio de estado, en caso afirmativo se inserta transaccion y se actualiza saldos JBL SEP/10
                                        if (cambio_estado) {
                                            // Se actualiza el saldo de interes vencido 90 dias
                                            // Se actualiza el saldo de interes vencido 90 dias
                                            myLogger.info("Entra a cambio de estado ");
                                            saldoVO.setSaldoInteresVencido90dias(saldoVO.getSaldoInteresVencido());
                                            saldoVO.setInteresVencido(saldoVO.getSaldoInteresVencido());
                                            saldoVO.setIvaInteresVencido(Convertidor.getMontoIva(saldoVO.getSaldoInteresVencido(), saldoVO.getIdSolicitudSICAP(), 2));
                                            saldoVO.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO);
                                            saldoVO.setCtaContable(saldoVO.getCtaContable());
                                            saldoVO.setSaldoInteresVencido(0);
                                            saldoVO.setSaldoInteresVigente(0);
                                        }
                                        saldoIBS.updateSaldo(saldoVO);
                                        // Se actualiza la tabla de creditos
                                        credito[j].setStatus(saldoVO.getEstatus());
                                        credito[j].setFechaUltimaActualizacion(Convertidor.toSqlDate(c1.getTime()));
                                        // SE ACTUALIZA EL ESTATUS DEL CREDITO
                                        CreditoHelperCartera.actualizaSaldoCuenta(credito[j]);
                                        if(credito[j].getAplicaGarantia() && credito[j].getMontoCuentaCongelada() == 0){
                                            transacHelper.aplicaPagoGarantia(credito[j], Convertidor.toSqlDate(c1.getTime()), credito[j].getAplicaMontoGarantia());
                                            credito[j].setAplicaGarantia(false);
                                        }
                                        /*
                                         * En caso de no existir provision del periodo y encontrar dividendo vigente se pasa a la provision de interes
                                         */
                                        if (esVencimiento) {
                                            fechaFinDiv = tablaAmortPr.fechaPago;
                                        } else {
                                            myLogger.info("provision a fecha de fin de mes " + fechaFinDiv);
                                        }

                                        // Se verifica que el evento con ese dividendo exista, en caso contrario se inserta un nuevo evento de provision
                                        if (eventoVO.fechaFin != null) {
                                            // Se compara la fecha fin con la de la transaccion actual y se calcula la fecha inicial y fecha final
                                            fechaIniDiv = eventoVO.fechaFin;
                                            numero_dias = FechasUtil.inBetweenDays(fechaIniDiv, fechaFinDiv);
                                            // Se inserta un nuevo evento de provision
                                            //eventoHelper.registraProvision(credito[j], tablaAmortPr, numero_dias, fechaFinDiv, saldoVO.getSaldoTotalAlDia());
                                            eventoHelper.registraProvision(credito[j], tablaAmortPr, numero_dias, fechaFinDiv, 0);

                                        } else {
                                            // El numero de dias será de acuerdo a la fecha de desembolso
                                            fechaIniDiv = credito[j].getFechaDesembolso();
                                            numero_dias = FechasUtil.inBetweenDays(fechaIniDiv, fechaFinDiv);
                                            // Se inserta un nuevo evento de provision
                                            //eventoHelper.registraProvision(credito[j], tablaAmortPr, numero_dias, fechaFinDiv, saldoVO.getSaldoTotalAlDia());
                                            eventoHelper.registraProvision(credito[j], tablaAmortPr, numero_dias, fechaFinDiv, 0);

                                        }
                                        interes_periodo = (tablaAmortPr.abonoCapital) * tasa_interes / 100 / 360 * 1;
                                        iva_interes_periodo = interes_periodo * tasa_iva / 100;
                                        
                                        /*SE DESACTIVA EL DEVENGAMIENTO SEMANAL PARA REALIZARLO DIARIO
                                        // Se asignan los rubros verificando el status del credito
                                        if (credito[j].getStatus() < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                            // Creditos vigentes
                                            rubro_prov.tipoRubro = ClientesConstants.INTERES;
                                            rubro_prov.monto = tablaAmortPr.interes;
                                            rubro_prov.status = ClientesConstants.RUBRO_VIGENTE;
                                            array_rubro.add(rubro_prov);

                                            rubro_prov = new RubrosVO();
                                            rubro_prov.tipoRubro = ClientesConstants.IVA_INTERES;
                                            rubro_prov.monto = tablaAmortPr.ivaInteres;
                                            rubro_prov.status = ClientesConstants.RUBRO_VIGENTE;
                                            array_rubro.add(rubro_prov);

                                        } else {
                                            // Creditos vencidos, se asigna a resolucion
                                            rubro_prov.tipoRubro = ClientesConstants.INTERES;
                                            rubro_prov.monto = tablaAmortPr.interes;
                                            rubro_prov.status = ClientesConstants.RUBRO_RESOLUCION;
                                            array_rubro.add(rubro_prov);

                                            rubro_prov = new RubrosVO();
                                            rubro_prov.tipoRubro = ClientesConstants.IVA_INTERES;
                                            rubro_prov.monto = tablaAmortPr.ivaInteres;
                                            rubro_prov.status = ClientesConstants.RUBRO_RESOLUCION;
                                            array_rubro.add(rubro_prov);
                                        }
                                        // Inserta la transaccion de cambio de estado
                                        if (array_rubro.size() > 0) {
                                            elementos = new RubrosVO[array_rubro.size()];
                                            for (int k = 0; k < elementos.length; k++) {
                                                elementos[k] = (RubrosVO) array_rubro.get(k);
                                            }
                                        }
                                        transacHelper.registraProvision(credito[j], elementos, fechaFinDiv);
                                        */

                                        /*
                                         * Verifica si hubo cambio de estado, en caso afirmativo inserta la transaccion
                                         */
                                        myLogger.debug("antes de if de cambio de estado");
                                        if (cambio_estado) {
                                            // Se registra la transaccion del cambio de estado
                                            myLogger.debug("dentro del if de cambio de estado para registrar transaccion");
                                            rubro_ce = new RubrosVO();
                                            rubro_ce.tipoRubro = ClientesConstants.INTERES;
                                            rubro_ce.monto = saldoVO.getInteresVencido();
                                            rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                            array_rubro_ce.add(rubro_ce);

                                            rubro_ce = new RubrosVO();
                                            rubro_ce.tipoRubro = ClientesConstants.IVA_INTERES;
                                            rubro_ce.monto = saldoVO.getIvaInteresVencido();
                                            rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                            array_rubro_ce.add(rubro_ce);

                                            rubro_ce = new RubrosVO();
                                            rubro_ce.tipoRubro = ClientesConstants.CAPITAL;
                                            rubro_ce.monto = saldoVO.getCapitalVencido();
                                            rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                            array_rubro_ce.add(rubro_ce);
                                            // Inserta la transaccion de cambio de estado
                                            if (array_rubro_ce.size() > 0) {
                                                elementos = new RubrosVO[array_rubro_ce.size()];
                                                for (int k = 0; k < elementos.length; k++) {
                                                    elementos[k] = (RubrosVO) array_rubro_ce.get(k);
                                                }
                                            }
                                            myLogger.debug("antes de registrar transaccion de cev");
                                            transacHelper.registraCambioEstadoVen(credito[j], elementos, fechaUltimoCierre);
                                            myLogger.debug("despues de registrar la transaccion de cev");

                                        }

                                    }
                                }
                                // Se obtiene el dividendo vigente de la tabla de amortizacion						
                            }
                        } // termina for de creditos vigentes, morosos y vencidos
                                    /* INTERES MORATORIO
                         * Se inicia un nuevo ciclo con los creditos morosos y vencidos
                         */
                        bitutil.registraEventoString("Finaliza con creditos vigentes");

                        CreditoCartVO[] creditoVen = null;

                        if (migracion) {
                            creditoVen = creditoCartDAO.getCreditoMorVenMigracion();
                        } else {
                            creditoVen = creditoCartDAO.getCreditoMorVen();
                        }

                        TablaAmortVO[] tablaAmortVen = null;
                        TablaAmortDAO tablaDAOVen = new TablaAmortDAO();
                        EventoDAO eventoVenDAO = new EventoDAO();
                        EventoHelper eventoVenHelper = new EventoHelper();
                        EventosVO eventoVenVO = new EventosVO();
                        RubrosVO rubro_prov = new RubrosVO();
                        RubrosVO rubro_mul = new RubrosVO();
                        RubrosVO elementos[] = null;
                        SaldoIBSVO saldo = new SaldoIBSVO();
                        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                        TablaAmortVO tablaAmortPr = new TablaAmortVO();

                        double tasa_mora = 0;
                        double tasa_iva = 0;
                        double interes_mora = 0;
                        double iva_interes_mora = 0;
                        double saldo_interes_mora = 0;
                        double saldo_iva_interes_mora = 0;
                        int dias_mora = 0;
                        double total_vencido = 0;
                        double monto_pagar = 0;
                        double tot_interes_mora = 0;
                        double tot_iva_interes_mora = 0;
                        double saldo_total_al_dia = 0;
                        double saldo_con_interes_al_final = 0;
                        double mora_dia = 0.00;
                        double iva_mora_dia = 0.00;
                        double saldo_multa = 0.00;
                        double saldo_iva_multa = 0.00;
                        double monto_iva = 0.00;
                        int res = 0;
                        Date fechaIniDiv = null;
                        int numero_dias = 0;
                        int numero_creditos_ven = 0;
                        boolean indicador_continuar = true;
                        boolean indicador_multa = false;
                        //nuevas varibles para vencidos
                        double saldo_interes_ctas_orden = 0;
                        int numero_dias_mora = 0;
                        int numero_dias_arriba_vencido = 0;
                        boolean cambio_estado = false;
                        int estatus = 0;
                        double saldo_interes_vencido = 0.00;
                        double saldo_iva_interes_vencido = 0.00;
                        double saldo_interes_vencido_90 = 0.00;
                        // Verifica que existan creditos vencidos JBL JUN/10
                        if (creditoVen != null) {
                            numero_creditos_ven = creditoVen.length;
                        }
                        myLogger.info("Procesando creditos en mora");
                        for (int j = 0; j < numero_creditos_ven; j++) {

                            // Se obtiene la fecha del ultimo evento de mora
                            cambio_estado = false;
                            indicador_continuar = true;
                            indicador_multa = false;
                            tot_interes_mora = 0;
                            tot_iva_interes_mora = 0;
                            saldo_multa = 0;
                            saldo_iva_multa = 0;
                            saldo_interes_mora = 0;
                            saldo_iva_interes_mora = 0;
                            double multa_dividendo = 0;
                            mora_dia = 0;
                            iva_mora_dia = 0;
                            tasa_mora = creditoVen[j].getTasaMora();
                            tasa_iva = creditoVen[j].getTasaIVA();
                            saldo = saldoDAO.getSaldo(creditoVen[j].getNumCliente(), creditoVen[j].getNumCredito());
                            saldo_interes_vencido = saldo.getSaldoInteresVencido();
                            saldo_interes_ctas_orden = saldo.getSaldoInteresVencido();
                            saldo_iva_interes_vencido = saldo.getIvaInteresVencido();
                            myLogger.info("Procesando credito: " + credito[j].getNumCredito());
                            myLogger.info("saldo interes vencido mora inicio " + saldo_interes_vencido);
                            total_vencido = saldo.getTotalVencido();
                            saldo_total_al_dia = saldo.getSaldoTotalAlDia();
                            saldo_con_interes_al_final = saldo.getSaldoConInteresAlFinal();

                            dias_mora = saldo.getDiasMora();
                            myLogger.info("Dias mora " + dias_mora);
                            myLogger.info("saldo total al dia entrando a mora" + saldo_total_al_dia);
                            // Se obitnee la tasa de interes
                            // TABLA DE AMORTIZACION
                            // Se obtienen los dividendos morosos y vencidos
                            tablaAmortVen = tablaDAOVen.getElementosMorVen(creditoVen[j].getNumCliente(), creditoVen[j].getNumCredito(), 0, 0);
                            // Se recorren todos los dividendos morosos
                            for (int k = 0; k < tablaAmortVen.length; k++) {
                                eventoVenVO = eventoVenDAO.getLastEventoTipo(creditoVen[j].getNumCliente(), creditoVen[j].getNumCredito(), tablaAmortVen[k].numPago, "MOR", Convertidor.toSqlDate(c1.getTime()));
                                // Se verifica que la fecha del evento no sea igual a la fecha del dia, en caso afirmativo se sale del ciclo
                                if (eventoVenVO != null) {
                                    myLogger.info("La mora del dia ya se calculo");
                                    indicador_continuar = false;
                                    break;
                                }
                                // Calcula los dias vencidos
                                if (k == 0) {
                                    myLogger.info("Entra a dias mora");
                                    dias_mora = FechasUtil.inBetweenDays(tablaAmortVen[k].fechaPago, Convertidor.toSqlDate(c1.getTime()));
                                    // Se verifica si los dias mora iguala o supera los dias establecidos para el credito vencido
                                    if (dias_mora > ClientesConstants.DIAS_MORA_VENCIDO && creditoVen[j].getStatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_MORA) {
                                        // Se realiza el cambio de estado tanto en el el saldo como en el 
                                        tablaAmortVen[k].status = ClientesConstants.DIVIDENDO_VENCIDO;
                                        cambio_estado = true;
                                    }
                                }
                                // Se calcula el interes moratorio
                                if (dias_mora > ClientesConstants.DIAS_GRACIA_MORA) {
                                    int dias_diferencia_mora = 0;
                                    dias_diferencia_mora = dias_mora - ClientesConstants.DIAS_GRACIA_MORA;
                                    /*
                                     * En caso de que acabe de superar los días de gracia de mora se cobran todos los dias, en caso contrario se cobra solo un dia							
                                     */
                                    if (dias_diferencia_mora == 1) {
                                        mora_dia = (tablaAmortVen[k].abonoCapital - tablaAmortVen[k].capitalPagado) * tasa_mora / 100 / 360 * dias_mora;
                                    } else {
                                        mora_dia = (tablaAmortVen[k].abonoCapital - tablaAmortVen[k].capitalPagado) * tasa_mora / 100 / 360 * 1;
                                    }

                                }
                                // En caso de seguir vencido despues de la gracia se aplica la multa
                                myLogger.info("Mora del dia " + mora_dia);
                                iva_mora_dia = Convertidor.getMontoIva(mora_dia, creditoVen[j].getNumSucursal(), 2);
                                interes_mora = tablaAmortVen[k].intMoratorio + mora_dia;
                                iva_interes_mora = tablaAmortVen[k].ivaIntMoratorio + iva_mora_dia;
                                multa_dividendo = tablaAmortVen[k].multa + tablaAmortVen[k].ivaMulta;
                                saldo_interes_mora = interes_mora - tablaAmortVen[k].intMoratorioPagado;
                                saldo_iva_interes_mora = iva_interes_mora - tablaAmortVen[k].ivaIntMoratorioPagado;
                                monto_pagar = tablaAmortVen[k].montoPagar + mora_dia + iva_mora_dia;
                                myLogger.info("Monto a pagar " + monto_pagar);
                                // Calcula totales
                                tot_interes_mora = tot_interes_mora + saldo_interes_mora;
                                tot_iva_interes_mora = tot_iva_interes_mora + saldo_iva_interes_mora;
                                if (dias_mora >= ClientesConstants.DIAS_GRACIA_MULTA && !FechasUtil.esDiaInhabil(c1.getTime(), fechasInhabiles) && multa_dividendo < 1) {

                                    eventoVenVO = eventoVenDAO.getLastEventoTipo(creditoVen[j].getNumCliente(), creditoVen[j].getNumCredito(), tablaAmortVen[k].numPago, "MUL");
                                    // Se verifica que la fecha del evento no sea igual a la fecha del dia, en caso afirmativo se sale del ciclo
                                    if (eventoVenVO != null) {
                                        myLogger.info("La multa de ese dividendo ya se calculo");
                                        indicador_multa = false;
                                    } else {
                                        if (migracion) {
                                            tablaAmortVen[k].multa = Convertidor.getMontoIva(ClientesConstants.CUOTA_MULTA_MIGRACION, creditoVen[j].getNumSucursal(), 1);
                                        } else {
                                            tablaAmortVen[k].multa = Convertidor.getMontoIva(ClientesConstants.CUOTA_MULTA, creditoVen[j].getNumSucursal(), 1);
                                        }

                                        monto_iva = ClientesConstants.CUOTA_MULTA - tablaAmortVen[k].multa;
                                        tablaAmortVen[k].ivaMulta = monto_iva;
                                        saldo_multa = tablaAmortVen[k].multa - tablaAmortVen[k].multaPagado;
                                        saldo_iva_multa = tablaAmortVen[k].ivaMulta - tablaAmortVen[k].ivaMultaPagado;
                                        monto_pagar = monto_pagar + saldo_multa + saldo_iva_multa;
                                        // Se registra el evento de multa
                                        //eventoHelper.registraMulta(creditoVen[j], tablaAmortVen[k], dias_mora, fechaIniDiv, c1.getTime(), ClientesConstants.CUOTA_MULTA, saldo_total_al_dia + saldo_multa + saldo_iva_multa);
                                        eventoHelper.registraMulta(creditoVen[j], tablaAmortVen[k], dias_mora, fechaIniDiv, c1.getTime(), ClientesConstants.CUOTA_MULTA, saldo_con_interes_al_final + saldo_multa + saldo_iva_multa);
                                        indicador_multa = true;
                                    }
                                }
                                total_vencido = total_vencido + mora_dia + iva_mora_dia + saldo_multa + saldo_iva_multa;
                                saldo_total_al_dia = saldo_total_al_dia + mora_dia + iva_mora_dia + saldo_multa + saldo_iva_multa;
                                saldo_con_interes_al_final = saldo_con_interes_al_final + mora_dia + iva_mora_dia + saldo_multa + saldo_iva_multa;
                                // Se actualizan los valores de la tabla
                                tablaAmortVen[k].intMoratorio = interes_mora;
                                tablaAmortVen[k].ivaIntMoratorio = iva_interes_mora;
                                tablaAmortVen[k].montoPagar = monto_pagar;
                                // Actualiza la tabla de amortrizacion
                                res = tablaDAOVen.updateSaldosTablaAmort(tablaAmortVen[k]);
                                // Actualiza la tabla de eventos
                                myLogger.info("llega a registrar evento mora");
                                eventoVenVO = eventoVenDAO.getLastEventoTipo(creditoVen[j].getNumCliente(), creditoVen[j].getNumCredito(), tablaAmortVen[k].numPago, "MOR", Convertidor.toSqlDate(c1.getTime()));
                                // Se verifica que el evento con ese dividendo exista, en caso contrario se inserta un nuevo evento de provision
                                if (eventoVenVO != null) {
                                    // Se compara la fecha fin con la de la transaccion actual y se calcula la fecha inicial y fecha final
                                    fechaIniDiv = eventoVenVO.fechaFin;
                                    numero_dias = FechasUtil.inBetweenDays(fechaIniDiv, Convertidor.toSqlDate(c1.getTime()));
                                    // Se inserta un nuevo evento de mora en caso de ser mayor a 0
                                    if (tablaAmortVen[k].intMoratorio > 0) {
                                        //eventoVenHelper.registraMora(creditoVen[j], tablaAmortVen[k], numero_dias, fechaIniDiv, c1.getTime(), (saldo_interes_mora + saldo_iva_interes_mora), saldo_total_al_dia);
                                        eventoVenHelper.registraMora(creditoVen[j], tablaAmortVen[k], numero_dias, fechaIniDiv, c1.getTime(), (saldo_interes_mora + saldo_iva_interes_mora), saldo_con_interes_al_final);
                                    }
                                } else {
                                    // El numero de dias será de acuerdo a la fecha de pago de la tabla de amortizacion
                                    fechaIniDiv = tablaAmortVen[k].fechaPago;
                                    numero_dias = FechasUtil.inBetweenDays(fechaIniDiv, Convertidor.toSqlDate(c1.getTime())) + 1;
                                    // Se inserta un nuevo evento de provision
                                    if (tablaAmortVen[k].intMoratorio > 0) {
                                        //eventoVenHelper.registraMora(creditoVen[j], tablaAmortVen[k], numero_dias, fechaIniDiv, c1.getTime(), (mora_dia + iva_mora_dia), saldo_total_al_dia);
                                        eventoVenHelper.registraMora(creditoVen[j], tablaAmortVen[k], numero_dias, fechaIniDiv, c1.getTime(), (mora_dia + iva_mora_dia), saldo_con_interes_al_final);
                                    }
                                }
                                // En caso de cambio de estado se registra
                                if (numero_dias_mora > ClientesConstants.DIAS_MORA_VENCIDO && estatus < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                    cambio_estado = true;
                                    myLogger.debug("Prende la bandera de cambio de estado");
                                }
                                if (cambio_estado) {
                                    myLogger.debug("entra al if");
                                    saldo_interes_ctas_orden = saldo.getSaldoInteresVigente();
                                    saldo_interes_vencido = saldo.getSaldoInteresVencido() + tablaAmortPr.interes - tablaAmortPr.interesPagado;
                                    saldo_iva_interes_vencido = Convertidor.getMontoIva(saldo_interes_vencido, credito[j].getNumSucursal(), 2);
                                    //total_vencido = total_vencido + saldo_interes_vencido + saldo_iva_interes_vencido;
                                    //saldo_total_al_dia = saldo_total_al_dia + saldo_interes_vencido + saldo_iva_interes_vencido;
                                    //saldo_con_interes_al_final = saldo_con_interes_al_final + saldo_interes_vencido + saldo_iva_interes_vencido;
                                    saldo_interes_vencido_90 = saldo_interes_vencido;
                                    /* 
                                     *  Se comentan por lo pronto el calculo de saldo vencido de acuerdo al numero de dias del periodo hasta tener claro el diseño final
                                     */
                                }						// SE INSERTA LA TRANSACCION DE MORA
                                myLogger.debug("total vencido posterior a cambio estado " + total_vencido);
                                ArrayList<RubrosVO> array_rubro = new ArrayList<RubrosVO>();
                                ArrayList<RubrosVO> array_rubro_mul = new ArrayList<RubrosVO>();

                                // Se asignan los rubros
                                if (tablaAmortVen[k].intMoratorio > 0) {
                                    rubro_prov = new RubrosVO();
                                    rubro_prov.tipoRubro = ClientesConstants.INTERES_MORATORIO;
                                    rubro_prov.monto = mora_dia;
                                    rubro_prov.status = ClientesConstants.RUBRO_VIGENTE;
                                    array_rubro.add(rubro_prov);

                                    rubro_prov = new RubrosVO();
                                    rubro_prov.tipoRubro = ClientesConstants.IVA_INTERES_MORATORIO;
                                    rubro_prov.monto = iva_mora_dia;
                                    rubro_prov.status = ClientesConstants.RUBRO_VIGENTE;
                                    array_rubro.add(rubro_prov);
                                    // Inserta la transaccion
                                    if (array_rubro.size() > 0) {
                                        elementos = new RubrosVO[array_rubro.size()];
                                        for (int n = 0; n < elementos.length; n++) {
                                            elementos[n] = (RubrosVO) array_rubro.get(n);
                                        }
                                    }
                                    transacHelper.registraMora(creditoVen[j], elementos, fechaIniDiv);
                                }
                                // Se asigna la multa en caso de aplicar
                                if (tablaAmortVen[k].multa > 0 && indicador_multa) {
                                    rubro_mul = new RubrosVO();
                                    rubro_mul.tipoRubro = ClientesConstants.MULTA;
                                    rubro_mul.monto = tablaAmortVen[k].multa;
                                    rubro_mul.status = ClientesConstants.RUBRO_VIGENTE;
                                    array_rubro_mul.add(rubro_mul);

                                    rubro_mul = new RubrosVO();
                                    rubro_mul.tipoRubro = ClientesConstants.IVA_MULTA;
                                    rubro_mul.monto = tablaAmortVen[k].ivaMulta;
                                    rubro_mul.status = ClientesConstants.RUBRO_VIGENTE;
                                    array_rubro_mul.add(rubro_mul);

                                    if (array_rubro_mul.size() > 0) {
                                        elementos = new RubrosVO[array_rubro_mul.size()];
                                        for (int r = 0; r < elementos.length; r++) {
                                            elementos[r] = (RubrosVO) array_rubro_mul.get(r);
                                        }
                                    }
                                    transacHelper.registraMulta(creditoVen[j], elementos, fechaUltimoCierre);
                                }
                                /*Ajustes para que cambie de estado a creditos no vencidos mayores a 90 dias*/
                                double capital = saldo.getSaldoCapital();
                                if (numero_dias_mora > ClientesConstants.DIAS_MORA_VENCIDO && estatus < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
                                    cambio_estado = true;
                                    myLogger.debug("Prende la bandera de cambio de estado");
                                    numero_dias_arriba_vencido = numero_dias_mora - ClientesConstants.DIAS_MORA_VENCIDO;
                                }
                                // En caso de existir un cambio de estado se inserta un evento y se cambia el status
                                if (cambio_estado) {
                                    myLogger.debug("entra a cambio de estado desde mora");
                                    //eventoHelper.registraCambioEstadoVen(credito[j], tablaAmortVen[k], numero_dias_mora, fechaIniDiv, c1.getTime(), capital, saldo_total_al_dia);
                                    eventoHelper.registraCambioEstadoVen(credito[j], tablaAmortVen[k], numero_dias_mora, fechaIniDiv, c1.getTime(), capital, saldo_con_interes_al_final);
                                    estatus = ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO;
                                    tablaAmortVen[k].status = ClientesConstants.DIVIDENDO_VENCIDO;
                                    tablaDAOVen.updateStatusVenTablaAmort(tablaAmortVen[k]);
                                }
                                // Se llevan los totales de interes de acuerdo a la situacion del credito
                                                    /*codigo insertado para vencidos*/

                            }
                            // Actualiza la tabla de saldos
                            if (indicador_continuar) {
                                saldo.setSaldoMora(tot_interes_mora);
                                saldo.setSaldoIVAMora(tot_iva_interes_mora);
                                saldo.setSaldoMulta(saldo.getSaldoMulta() + saldo_multa);
                                saldo.setSaldoIVAMulta(saldo.getSaldoIVAMulta() + saldo_iva_multa);
                                saldo.setSaldoInteresVencido(saldo_interes_vencido);
                                saldo.setIvaInteresVencido(saldo_iva_interes_vencido);
                                saldo.setTotalVencido(total_vencido);
                                saldo.setDiasMora(dias_mora);
                                saldo.setSaldoTotalAlDia(saldo_total_al_dia);
                                saldo.setSaldoConInteresAlFinal(saldo_con_interes_al_final);
                                saldo.setFechaGeneracion(Convertidor.toSqlDate(c1.getTime()));
                                saldo.setCtaContable(saldo.getCtaContable());
                                //saldo.setInteresPorDevengar(saldo.getInteresPorDevengar());
                                //saldo.setIvaInteresPorDevengar(saldo.getIvaInteresPorDevengar());
                                if (cambio_estado) {
                                    // Se actualiza el saldo de interes vencido 90 dias
                                    // Se actualiza el saldo de interes vencido 90 dias
                                    myLogger.debug("Entra a cambio de estado desde mora");
                                    saldo.setSaldoInteresVencido90dias(saldo_interes_vencido_90);
                                    saldo.setInteresVencido(saldo_interes_vencido);
                                    saldo.setIvaInteresVencido(Convertidor.getMontoIva(saldo_interes_vencido, creditoVen[j].getNumSucursal(), 2));
                                    saldo.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO);
                                    saldo.setCtaContable(saldo.getCtaContable());
                                    saldo.setFechaAcarteraVencida(Convertidor.toSqlDate(c1.getTime()));
                                    saldo.setSaldoInteresVencido(0);  // se actualiza a 0 el interes moroso
                                    saldo.setSaldoInteresVigente(0);
                                    saldo.setSaldoInteresCtasOrden(saldo_interes_ctas_orden);
                                }
                                saldoDAO.updateSaldo(saldo);
                                // Actualiza la tabla de creditos
                                creditoVen[j].setStatus(saldo.getEstatus());
                                creditoVen[j].setFechaUltimaActualizacion(Convertidor.toSqlDate(c1.getTime()));
                                creditoVen[j].setNumDiasMora(dias_mora);
                                // SE ACTUALIZA EL ESTATUS DEL CREDITO
                                CreditoHelperCartera.actualizaSaldoCuenta(creditoVen[j]);
                                // Se actualiza el evento relevante JBL ABR-13
                                //CreditoHelperCartera.actualizaEventoRelevante(saldo);
                            }
                            if (cambio_estado) {
                                myLogger.debug("dentro del if para registrar transaccion");
                                RubrosVO rubro_ce = new RubrosVO();
                                ArrayList<RubrosVO> array_rubro_ce = new ArrayList<RubrosVO>();
                                creditoCartDAO.updateEstatusCredito(creditoVen[j]);
                                // Se registra la transaccion del cambio de estado
                                rubro_ce = new RubrosVO();
                                rubro_ce.tipoRubro = ClientesConstants.INTERES;
                                rubro_ce.monto = saldo.getInteresVencido();
                                rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                array_rubro_ce.add(rubro_ce);

                                rubro_ce = new RubrosVO();
                                rubro_ce.tipoRubro = ClientesConstants.IVA_INTERES;
                                rubro_ce.monto = saldo.getIvaInteresVencido();
                                rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                array_rubro_ce.add(rubro_ce);

                                rubro_ce = new RubrosVO();
                                rubro_ce.tipoRubro = ClientesConstants.CAPITAL;
                                rubro_ce.monto = saldo.getCapitalVencido();
                                rubro_ce.status = ClientesConstants.RUBRO_VENCIDO;
                                array_rubro_ce.add(rubro_ce);
                                // Inserta la transaccion de cambio de estado
                                if (array_rubro_ce.size() > 0) {
                                    elementos = new RubrosVO[array_rubro_ce.size()];
                                    for (int m = 0; m < elementos.length; m++) {
                                        elementos[m] = (RubrosVO) array_rubro_ce.get(m);
                                    }
                                }
                                myLogger.debug("Antes de registrar transaccion");
                                transacHelper.registraCambioEstadoVen(creditoVen[j], elementos, fechaUltimoCierre);
                                myLogger.debug("Despues de registrar transaccion");
                            }
                        }
                        bitutil.registraEventoString("Finaliza creditos vencidos");
                        /*PROCESO DE CASTIGO AUTOMATICO */
                        if((c1.get(Calendar.DATE) == c1.getActualMaximum(Calendar.DAY_OF_MONTH))){
                            myLogger.info("Procesado Castigos");
                            ArrayList<CreditoCartVO> creditoCastigar = creditoCartDAO.getArrayCreditoCastigo();
                            myLogger.debug("Numero de Creditos a Castigar "+creditoCastigar.size());
                            for (CreditoCartVO creditoCastigo : creditoCastigar) {
                                System.out.println("Credito "+creditoCastigo.getNumCliente()+" "+creditoCastigo.getNumSolicitud()+" "+creditoCastigo.getNumCredito());
                                saldo = saldoDAO.getSaldo(creditoCastigo.getNumCliente(), creditoCastigo.getNumCredito());
                                // Se registra un evento
                                eventoHelper.registraCastigo(saldo, request);
                                saldo.setEstatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_CASTIGADO);
                                saldoDAO.updateSaldo(saldo);
                                myLogger.info("Actualizo saldo para credito:" + creditoCastigo.getNumCredito());
                                creditoCastigo.setStatus(ClientesConstants.SITUACION_CREDITO_SALDOST24_CASTIGADO);
                                creditoCartDAO.updatePagoCredito(creditoCastigo);
                                ArrayList<RubrosVO> array_pago = new ArrayList<RubrosVO>();
                                RubrosVO rubro = new RubrosVO();
                                if (saldo.getSaldoCapital() > 0) {
                                    rubro.tipoRubro = ClientesConstants.CAPITAL;
                                    rubro.monto = saldo.getSaldoCapital();
                                    rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                                    array_pago.add(rubro);
                                }
                                rubro = new RubrosVO();
                                if (saldo.getSaldoInteres() > 0) {
                                    rubro.tipoRubro = ClientesConstants.INTERES;
                                    rubro.monto = saldo.getSaldoInteres();
                                    rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                                    array_pago.add(rubro);
                                }
                                rubro = new RubrosVO();
                                if (saldo.getSaldoIvaInteres() > 0) {
                                    rubro.tipoRubro = ClientesConstants.IVA_INTERES;
                                    rubro.monto = saldo.getSaldoIvaInteres();
                                    rubro.status = ClientesConstants.RUBRO_CASTIGADO;
                                    array_pago.add(rubro);
                                }
                                myLogger.debug("rubros " + array_pago.toString());
                                if (array_pago.size() > 0) {
                                    elementos = new RubrosVO[array_pago.size()];
                                    for (i = 0; i < elementos.length; i++) {
                                        elementos[i] = (RubrosVO) array_pago.get(i);
                                    }
                                }
                                transacHelper.registraCastigo(creditoCastigo, elementos, c1.getTime());
                            }
                            myLogger.debug("Finaliza Procesado Castigos");
                        } else {
                            parametroVO = catalogoDAO.getParametro("HORA_CIERRE_MES");
                            if(!parametroVO.valor.equals("0")){
                                catalogoDAO.updateParametro("HORA_CIERRE_MES", "0");
                                myLogger.debug("Apaga Espera Cierre Inicio de Mes");
                            }
                            myLogger.info("Dia Inhabil para Proceso Castigos");
                        }
                        /*FIN PROCESO DE CASTIGO AUTOMATICO */
                        //Actualiza las fecha de generacion de los saldos no contemplados con estatus 1
                        saldoDAO.setFechaAlDia(Convertidor.toSqlDate(c1.getTime()));
                        myLogger.info("Fin Cierre CEC actualiza fecha " + c1.getTime().toString());
                        //DEVENGAMIENTO DIARIO
                        if(devenDAO.getDiaDevengado(Convertidor.toSqlDate(fechaUltimoCierre)) > 0){
                            myLogger.info("Inicia Devengamiento Diario "+arrDevenVO.size());
                            if(devenDAO.generaDevengamiento(Convertidor.toSqlDate(fechaUltimoCierre)) > 0){
                                myLogger.info("Finaliza Devengamiento Diario");
                                bitutil.registraEventoString("Finaliza Devengamiento Diario");
                            } else {
                                myLogger.error("ERROR Devengamiento Diario");
                                bitutil.registraEventoString("ERROR Devengamiento Diario");
                                Mail.enviaCorreo("Error en Cierre", "Devengamiento Diario", Destinatarios);
                            }
                        }
                        /*
                        * Inserta histórico
                        */
                        //TODO Mandar llamar aqui cierre fondeadores
                        CommandCierreDiaBursa cmdCierreBursa = new CommandCierreDiaBursa(siguiente);
                        cmdCierreBursa.execute(request);
                        if (!migracion) {
                            this.insertaSaldoHistoricoNuevo(fechaUltimoCierre);
                            myLogger.info("Finalizo la insercion de saldos historicos");
                            bitutil.registraEventoString("Finaliza inserción de históricos");
                        }
                        if (migracion) {
                            //Añade un dia al calendario
                            c1.add(Calendar.DAY_OF_MONTH, 1);
                            // Actualiza la fecha de cierre
                            parametroVO = catalogoDAO.updateParametro("FECHA_CIERRE_MIGRACION", Convertidor.dateToString(c1.getTime(), ClientesConstants.FORMATO_FECHA_EU));
                            parametroVO = catalogoDAO.getParametro("FECHA_CIERRE_MIGRACION");
                            siguiente = "/cierreDiaMigracion.jsp";
                        } else {
                            //Añade un dia al calendario
                            c1.add(Calendar.DAY_OF_MONTH, 1);
                            parametroVO = catalogoDAO.updateParametro("FECHA_CIERRE", Convertidor.dateToString(c1.getTime(), ClientesConstants.FORMATO_FECHA_EU));
                            parametroVO = catalogoDAO.getParametro("FECHA_CIERRE");
                            siguiente = "/cierreDia.jsp";
                        }

                    }
                    catalogoDAO.banderaCierreDia(0);
                    bitutil.registraEventoString("Termina cierre de dia con fecha :" + ultimaFecha);
                    myLogger.info("REGRESA BANDERA");
                    // envio de mail al finalizar el cierre
                    SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                    String Contenido = saldoDAO.getSaldosfechacierre(ultimaFecha);
                    Mail.enviaCorreo("Fin Cierre sistema " + ultimaFecha, Contenido, Destinatarios);
                    myLogger.info("Envio mail al finalizar cierre");
                    myLogger.info("Empieza Actualizacion de Calificacion de equipos ");
                    int noMultas = ClientesConstants.NUMERO_MULTAS_ESTATUS_B;
                    int res = grupoDao.updateCalificacionGrupo(noMultas);
                    myLogger.info("Termina Actualizacion de Calificacion de equipos registros actualizados: "+res);
                } else {
                    myLogger.debug("Error en las fechas");
                }    
                
                //Manda llamar Generación de Interfaces Bursa
                //CommandInterfacesBursa cmdInterfacesBursa = new CommandInterfacesBursa(siguiente);
                //cmdInterfacesBursa.generarInterfacesBursa(fechaUltimoCierre);
                
            } else {
                myLogger.debug("El cierre ya se encuentra en proceso");
            }
        } catch (Exception e) {
            myLogger.error("execute", e);
            try {
                myLogger.debug("envio de mail por error en el cierre");
                Mail.enviaCorreo("Error en Cierre", e + "", Destinatarios);
            } catch (Exception maile) {
                myLogger.error("Envio mail error", maile);
            }
        }
        return siguiente;

    }

    public boolean validaUltimaFechaEjecucion() {
        boolean esFechaAnterior = false;
        try {
            String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaUltimoProceso = format.parse(ultimaFecha);
            Calendar hoy = Calendar.getInstance();
            Date fechaHoy = format.parse(hoy.get(Calendar.YEAR) + "-" + (hoy.get(Calendar.MONTH) + 1) + "-" + hoy.get(Calendar.DAY_OF_MONTH));
            if (fechaHoy.compareTo(fechaUltimoProceso) > 0) {
                esFechaAnterior = true;
            }
        } catch (ParseException exc) {
            myLogger.error("validaUltimaFechaEjecucion", exc);
        } finally {

        }
        return esFechaAnterior;
    }

    public int obtieneNumeroDias() {
        int numeroDias = 0;
        try {
            String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaUltimoProceso = format.parse(ultimaFecha);
            Calendar hoy = Calendar.getInstance();
            Date fechaHoy = format.parse(hoy.get(Calendar.YEAR) + "-" + (hoy.get(Calendar.MONTH) + 1) + "-" + hoy.get(Calendar.DAY_OF_MONTH));
            myLogger.info("fecha hoy" + fechaHoy.toString());
            numeroDias = FechasUtil.inBetweenDays(fechaUltimoProceso, fechaHoy);
        } catch (ParseException exc) {
            myLogger.error("obtieneNumeroDias", exc);
        } finally {

        }
        return numeroDias;
    }

    public double getSaldoInsolutoAlCompromiso(String numReferencia) throws ClientesException {
        double totalVigente = -1;
        try {
            ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
            ReferenciaGeneralVO refVo = referenciaDAO.getReferenciaGeneral(numReferencia);
            SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
            SaldoIBSVO saldo = null;
            saldo = saldoIBSDAO.getSaldo(refVo.numcliente, refVo.numSolicitud, refVo.referencia);

            if (saldo != null) {
                totalVigente = saldo.getSaldoTotalAlDia();
            }
        } catch (Exception exc) {
            myLogger.error("getSaldoInsolutoAlCompromiso", exc);
        } finally {

        }
        return totalVigente;
    }

    public double getSaldoInsolutoProyecto(String numReferencia) throws ClientesException {
        double totalVigente = -1;
        try {
            ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
            myLogger.info("num referencia " + numReferencia);
            ReferenciaGeneralVO refVo = referenciaDAO.getReferenciaGeneral(numReferencia);
            SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
            myLogger.info("sale de referencias");
            myLogger.info("numcliente " + refVo.numcliente);
            SaldoIBSVO saldo = new SaldoIBSVO();
            saldo = saldoIBSDAO.getSaldo(refVo.numcliente, refVo.numSolicitud, refVo.referencia);

            if (saldo != null) {
                totalVigente = saldo.getSaldoConInteresAlFinal();
            }
        } catch (Exception exc) {
            myLogger.error("getSaldoInsolutoProyecto", exc);
        } finally {

        }
        return totalVigente;
    }

    public SaldoIBSVO getSaldo(String numReferencia) throws ClientesException {
        SaldoIBSVO saldo = new SaldoIBSVO();
        try {
            ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
            myLogger.info("num referencia " + numReferencia);
            ReferenciaGeneralVO refVo = referenciaDAO.getReferenciaGeneral(numReferencia);
            SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
            myLogger.info("sale de referencias");
            myLogger.info("numcliente " + refVo.numcliente);
            saldo = saldoIBSDAO.getSaldo(refVo.numcliente, refVo.numSolicitud, refVo.referencia);

        } catch (Exception exc) {
            myLogger.error("getSaldo", exc);
        } finally {

        }
        return saldo;
    }

    public double getSaldoCapital(String numReferencia) throws ClientesException {
        double totalVigente = -1;
        try {
            ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
            ReferenciaGeneralVO refVo = referenciaDAO.getReferenciaGeneral(numReferencia);
            SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
            SaldoIBSVO saldo = null;
            saldo = saldoIBSDAO.getSaldo(refVo.numcliente, refVo.numSolicitud, refVo.referencia);

            if (saldo != null) {
                totalVigente = saldo.getSaldoCapital();
            }
        } catch (Exception exc) {
            myLogger.error("getSaldoCapital", exc);
        } finally {

        }
        return totalVigente;
    }

    public void insertaSaldoHistoricoNuevo(Date fechaRegistro) throws ClientesException {

        int numeroRegistros = 0;
        int numeroRegistrosAmort = 0;
        try {
            /*int diferenciaEnDias = 1;
            long tiempoCierre = fechaRegistro.getTime();
            long unDia = diferenciaEnDias * 24 * 60 * 60 * 1000;
            Date fechaHist = new Date(tiempoCierre - unDia);
            java.sql.Date fechaSaldoHistorico = new java.sql.Date(fechaHist.getTime());*/
            java.sql.Date fechaSaldoHistorico = Convertidor.toSqlDate(fechaRegistro);
            SaldoIBSVO saldo[] = null;
            SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
            SaldoHistoricoVO saldoHistorico = new SaldoHistoricoVO();
            SaldoHistoricoDAO saldoHistoricoDAO = new SaldoHistoricoDAO();

            if (!saldoHistoricoDAO.getHistorico(fechaSaldoHistorico)) {
                numeroRegistros = saldoHistoricoDAO.addSaldoHistorico(fechaSaldoHistorico);
                numeroRegistrosAmort = saldoHistoricoDAO.addTablaAmortHistorico(fechaSaldoHistorico);
                myLogger.info("Inserto tabla amortizacion registros: "+numeroRegistrosAmort);
                /*int saldosHistoricos = 0;
                 saldo = saldoDAO.getSaldosIBS(fechaSaldoHistorico);
                 numeroRegistros = saldo.length;

                 for (int i = 0; i < numeroRegistros; i++) {

                 saldoHistorico.setOrigen(saldo[i].getOrigen());
                 saldoHistorico.setCredito(saldo[i].getCredito());
                 saldoHistorico.setReferencia(saldo[i].getReferencia());
                 saldoHistorico.setIdClienteSICAP(saldo[i].getIdClienteSICAP());
                 saldoHistorico.setIdSolicitudSICAP(saldo[i].getIdSolicitudSICAP());
                 saldoHistorico.setIdClienteIBS(saldo[i].getIdClienteIBS());
                 saldoHistorico.setNombreCliente(saldo[i].getNombreCliente());
                 saldoHistorico.setRfc(saldo[i].getRfc());
                 saldoHistorico.setIdSucursal(saldo[i].getIdSucursal());
                 saldoHistorico.setNombreSucursal(saldo[i].getNombreSucursal());
                 saldoHistorico.setFechaEnvio(saldo[i].getFechaEnvio());
                 saldoHistorico.setFechaGeneracion(saldo[i].getFechaGeneracion());
                 saldoHistorico.setHoraGeneracion(saldo[i].getHoraGeneracion());
                 saldoHistorico.setNumeroCuotas(saldo[i].getNumeroCuotas());
                 saldoHistorico.setNumeroCuotasTranscurridas(saldo[i].getNumeroCuotasTranscurridas());
                 saldoHistorico.setPlazo(saldo[i].getPlazo());
                 saldoHistorico.setPeriodicidad(saldo[i].getPeriodicidad());
                 saldoHistorico.setFechaDesembolso(saldo[i].getFechaDesembolso());
                 saldoHistorico.setFechaVencimiento(saldo[i].getFechaVencimiento());
                 saldoHistorico.setMontoCredito(saldo[i].getMontoCredito());
                 saldoHistorico.setSaldoTotalAlDia(saldo[i].getSaldoTotalAlDia());
                 saldoHistorico.setSaldoCapital(saldo[i].getSaldoCapital());
                 saldoHistorico.setSaldoInteres(saldo[i].getSaldoInteres());
                 saldoHistorico.setSaldoInteresVigente(saldo[i].getSaldoInteresVigente());
                 saldoHistorico.setSaldoInteresVencido(saldo[i].getSaldoInteresVencido());
                 saldoHistorico.setSaldoInteresVencido90dias(saldo[i].getSaldoInteresVencido90dias());
                 saldoHistorico.setSaldoInteresCtasOrden(saldo[i].getSaldoInteresCtasOrden());
                 saldoHistorico.setSaldoIvaInteres(saldo[i].getSaldoIvaInteres());
                 saldoHistorico.setSaldoBonificacionDeIVA(saldo[i].getSaldoBonificacionDeIVA());
                 saldoHistorico.setSaldoMora(saldo[i].getSaldoMora());
                 saldoHistorico.setSaldoIVAMora(saldo[i].getSaldoIVAMora());
                 saldoHistorico.setSaldoMulta(saldo[i].getSaldoMulta());
                 saldoHistorico.setSaldoIVAMulta(saldo[i].getSaldoIVAMulta());
                 saldoHistorico.setCapitalPagado(saldo[i].getCapitalPagado());
                 saldoHistorico.setInteresNormalPagado(saldo[i].getInteresNormalPagado());
                 saldoHistorico.setIvaInteresNormalPagado(saldo[i].getIvaInteresNormalPagado());
                 saldoHistorico.setBonificacionPagada(saldo[i].getBonificacionPagada());
                 saldoHistorico.setMoratorioPagado(saldo[i].getMoratorioPagado());
                 saldoHistorico.setIvaMoraPagado(saldo[i].getIvaMoraPagado());
                 saldoHistorico.setMultaPagada(saldo[i].getMultaPagada());
                 saldoHistorico.setIvaMultaPagado(saldo[i].getIvaMultaPagado());
                 saldoHistorico.setComision(saldo[i].getComision());
                 saldoHistorico.setIvaComision(saldo[i].getIvaComision());
                 saldoHistorico.setMontoSeguro(saldo[i].getMontoSeguro());
                 saldoHistorico.setMontoDesembolsado(saldo[i].getMontoDesembolsado());
                 saldoHistorico.setFechaSigAmortizacion(saldo[i].getFechaSigAmortizacion());
                 saldoHistorico.setCapitalSigAmortizacion(saldo[i].getCapitalSigAmortizacion());
                 saldoHistorico.setInteresSigAmortizacion(saldo[i].getInteresSigAmortizacion());
                 saldoHistorico.setIvaSigAmortizacion(saldo[i].getIvaSigAmortizacion());
                 saldoHistorico.setFondeador(saldo[i].getFondeador());
                 saldoHistorico.setNombreFondeador(saldo[i].getNombreFondeador());
                 saldoHistorico.setTasaInteresSinIVA(saldo[i].getTasaInteresSinIVA());
                 saldoHistorico.setTasaMoraSinIVA(saldo[i].getTasaMoraSinIVA());
                 saldoHistorico.setTasaIVA(saldo[i].getTasaIVA());
                 saldoHistorico.setSaldoConInteresAlFinal(saldo[i].getSaldoConInteresAlFinal());
                 saldoHistorico.setCapitalVencido(saldo[i].getCapitalVencido());
                 saldoHistorico.setInteresVencido(saldo[i].getInteresVencido());
                 saldoHistorico.setIvaInteresVencido(saldo[i].getIvaInteresVencido());
                 saldoHistorico.setTotalVencido(saldo[i].getTotalVencido());
                 saldoHistorico.setEstatus(saldo[i].getEstatus());
                 saldoHistorico.setCtaContable(saldo[i].getCtaContable());
                 saldoHistorico.setIdProducto(saldo[i].getIdProducto());
                 saldoHistorico.setFechaIncumplimiento(saldo[i].getFechaIncumplimiento());
                 saldoHistorico.setFechaAcarteraVencida(saldo[i].getFechaAcarteraVencida());
                 saldoHistorico.setDiasMora(saldo[i].getDiasMora());
                 saldoHistorico.setDiasTranscurridos(saldo[i].getDiasTranscurridos());
                 saldoHistorico.setCuotasVencidas(saldo[i].getCuotasVencidas());
                 saldoHistorico.setNumeroPagosRealizados(saldo[i].getNumeroPagosRealizados());
                 saldoHistorico.setMontoTotalPagado(saldo[i].getMontoTotalPagado());
                 saldoHistorico.setFechaUltimoPago(saldo[i].getFechaUltimoPago());
                 saldoHistorico.setBanderaReestructura(saldo[i].getBanderaReestructura());
                 saldoHistorico.setCreditoReestructurado(saldo[i].getCreditoReestructurado());
                 saldoHistorico.setDiasMoraReestructura(saldo[i].getDiasMoraReestructura());
                 saldoHistorico.setTasaPreferencialIVA(saldo[i].getTasaPreferencialIVA());
                 saldoHistorico.setCuentaBucket(saldo[i].getCuentaBucket());
                 saldoHistorico.setSaldoBucket(saldo[i].getSaldoBucket());
                 saldoHistorico.setFechaHistorico(fechaSaldoHistorico);
                 saldoHistorico.setInteresPorDevengar(saldo[i].getInteresPorDevengar());
                 saldoHistorico.setIvaInteresPorDevengar(saldo[i].getIvaInteresPorDevengar());
                 // Se ejecuta DAO para insert los datos en la tabla d_saldos_hist
                 saldosHistoricos = saldoHistoricoDAO.addSaldoHistorico(saldoHistorico);
                 }*/
            }

            myLogger.info("la cantidad de columnas insertadas fue: " + numeroRegistros);
        } catch (Exception exc) {
            myLogger.error("insertaSaldoHistoricoNuevo", exc);
        }

    }

    //Se crea Método para insertar el contenido de la tabla d_saldos en la tabla d_saldos_hist
    // Miguel A Mendoza 28/12/2010
    public void insertaSaldoHistorico(Date fechaRegistro) throws ClientesException {
        int numeroRegistros = 0;
        try {

            java.sql.Date fechaSaldoHistorico = new java.sql.Date(fechaRegistro.getTime());
            SaldoIBSVO saldo[] = null;
            SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
            SaldoHistoricoVO saldoHistorico = new SaldoHistoricoVO();
            SaldoHistoricoDAO saldoHistoricoDAO = new SaldoHistoricoDAO();

            int saldosHistoricos = 0;
//			saldo = saldoDAO.getSaldosIBS();
            numeroRegistros = saldo.length;

            for (int i = 0; i < numeroRegistros; i++) {

                saldoHistorico.setOrigen(saldo[i].getOrigen());
                saldoHistorico.setCredito(saldo[i].getCredito());
                saldoHistorico.setReferencia(saldo[i].getReferencia());
                saldoHistorico.setIdClienteSICAP(saldo[i].getIdClienteSICAP());
                saldoHistorico.setIdSolicitudSICAP(saldo[i].getIdSolicitudSICAP());
                saldoHistorico.setIdClienteIBS(saldo[i].getIdClienteIBS());
                saldoHistorico.setNombreCliente(saldo[i].getNombreCliente());
                saldoHistorico.setRfc(saldo[i].getRfc());
                saldoHistorico.setIdSucursal(saldo[i].getIdSucursal());
                saldoHistorico.setNombreSucursal(saldo[i].getNombreSucursal());
                saldoHistorico.setFechaEnvio(saldo[i].getFechaEnvio());
                saldoHistorico.setFechaGeneracion(saldo[i].getFechaGeneracion());
                saldoHistorico.setHoraGeneracion(saldo[i].getHoraGeneracion());
                saldoHistorico.setNumeroCuotas(saldo[i].getNumeroCuotas());
                saldoHistorico.setNumeroCuotasTranscurridas(saldo[i].getNumeroCuotasTranscurridas());
                saldoHistorico.setPlazo(saldo[i].getPlazo());
                saldoHistorico.setPeriodicidad(saldo[i].getPeriodicidad());
                saldoHistorico.setFechaDesembolso(saldo[i].getFechaDesembolso());
                saldoHistorico.setFechaVencimiento(saldo[i].getFechaVencimiento());
                saldoHistorico.setMontoCredito(saldo[i].getMontoCredito());
                saldoHistorico.setSaldoTotalAlDia(saldo[i].getSaldoTotalAlDia());
                saldoHistorico.setSaldoCapital(saldo[i].getSaldoCapital());
                saldoHistorico.setSaldoInteres(saldo[i].getSaldoInteres());
                saldoHistorico.setSaldoInteresVigente(saldo[i].getSaldoInteresVigente());
                saldoHistorico.setSaldoInteresVencido(saldo[i].getSaldoInteresVencido());
                saldoHistorico.setSaldoInteresVencido90dias(saldo[i].getSaldoInteresVencido90dias());
                saldoHistorico.setSaldoInteresCtasOrden(saldo[i].getSaldoInteresCtasOrden());
                saldoHistorico.setSaldoIvaInteres(saldo[i].getSaldoIvaInteres());
                saldoHistorico.setIvaInteresVencido(saldo[i].getIvaInteresVencido());
                saldoHistorico.setSaldoBonificacionDeIVA(saldo[i].getSaldoBonificacionDeIVA());
                saldoHistorico.setSaldoMora(saldo[i].getSaldoMora());
                saldoHistorico.setSaldoIVAMora(saldo[i].getSaldoIVAMora());
                saldoHistorico.setSaldoMulta(saldo[i].getSaldoMulta());
                saldoHistorico.setSaldoIVAMulta(saldo[i].getSaldoIVAMulta());
                saldoHistorico.setCapitalPagado(saldo[i].getCapitalPagado());
                saldoHistorico.setInteresNormalPagado(saldo[i].getInteresNormalPagado());
                saldoHistorico.setIvaInteresNormalPagado(saldo[i].getIvaInteresNormalPagado());
                saldoHistorico.setBonificacionPagada(saldo[i].getBonificacionPagada());
                saldoHistorico.setMoratorioPagado(saldo[i].getMoratorioPagado());
                saldoHistorico.setIvaMoraPagado(saldo[i].getIvaMoraPagado());
                saldoHistorico.setMultaPagada(saldo[i].getMultaPagada());
                saldoHistorico.setIvaMultaPagado(saldo[i].getIvaMultaPagado());
                saldoHistorico.setComision(saldo[i].getComision());
                saldoHistorico.setIvaComision(saldo[i].getIvaComision());
                saldoHistorico.setMontoSeguro(saldo[i].getMontoSeguro());
                saldoHistorico.setMontoDesembolsado(saldo[i].getMontoDesembolsado());
                saldoHistorico.setFechaSigAmortizacion(saldo[i].getFechaSigAmortizacion());
                saldoHistorico.setCapitalSigAmortizacion(saldo[i].getCapitalSigAmortizacion());
                saldoHistorico.setInteresSigAmortizacion(saldo[i].getInteresSigAmortizacion());
                saldoHistorico.setIvaSigAmortizacion(saldo[i].getIvaSigAmortizacion());
                saldoHistorico.setFondeador(saldo[i].getFondeador());
                saldoHistorico.setNombreFondeador(saldo[i].getNombreFondeador());
                saldoHistorico.setTasaInteresSinIVA(saldo[i].getTasaInteresSinIVA());
                saldoHistorico.setTasaMoraSinIVA(saldo[i].getTasaMoraSinIVA());
                saldoHistorico.setTasaIVA(saldo[i].getTasaIVA());
                saldoHistorico.setSaldoConInteresAlFinal(saldo[i].getSaldoConInteresAlFinal());
                saldoHistorico.setCapitalVencido(saldo[i].getCapitalVencido());
                saldoHistorico.setInteresVencido(saldo[i].getInteresVencido());
                saldoHistorico.setIvaInteresVencido(saldo[i].getIvaInteresVencido());
                saldoHistorico.setTotalVencido(saldo[i].getTotalVencido());
                saldoHistorico.setEstatus(saldo[i].getEstatus());
                saldoHistorico.setCtaContable(saldo[i].getCtaContable());
                saldoHistorico.setIdProducto(saldo[i].getIdProducto());
                saldoHistorico.setFechaIncumplimiento(saldo[i].getFechaIncumplimiento());
                saldoHistorico.setFechaAcarteraVencida(saldo[i].getFechaAcarteraVencida());
                saldoHistorico.setDiasMora(saldo[i].getDiasMora());
                saldoHistorico.setDiasTranscurridos(saldo[i].getDiasTranscurridos());
                saldoHistorico.setCuotasVencidas(saldo[i].getCuotasVencidas());
                saldoHistorico.setNumeroPagosRealizados(saldo[i].getNumeroPagosRealizados());
                saldoHistorico.setMontoTotalPagado(saldo[i].getMontoTotalPagado());
                saldoHistorico.setFechaUltimoPago(saldo[i].getFechaUltimoPago());
                saldoHistorico.setBanderaReestructura(saldo[i].getBanderaReestructura());
                saldoHistorico.setCreditoReestructurado(saldo[i].getCreditoReestructurado());
                saldoHistorico.setDiasMoraReestructura(saldo[i].getDiasMoraReestructura());
                saldoHistorico.setTasaPreferencialIVA(saldo[i].getTasaPreferencialIVA());
                saldoHistorico.setCuentaBucket(saldo[i].getCuentaBucket());
                saldoHistorico.setSaldoBucket(saldo[i].getSaldoBucket());
                saldoHistorico.setFechaHistorico(fechaSaldoHistorico);

                // Se ejecuta DAO para insert los datos en la tabla d_saldos_hist
                saldosHistoricos = saldoHistoricoDAO.addSaldoHistorico(saldoHistorico);

            }
            myLogger.info("la cantidad de columnas insertadas fue: " + numeroRegistros);
        } catch (Exception exc) {
            myLogger.error("insertaSaldoHistorico", exc);
        }

    }

    private boolean esFinDeMes(Calendar cal) {
        int dia = cal.get(GregorianCalendar.DATE);
        int diaFinalMes = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        if ((diaFinalMes == dia)) {
            return true;
        } else {
            return false;
        }
    }

    public double obtenSaldoFavor(CreditoCartVO cartera, SaldoIBSVO saldo, Date fechaCierre) throws Exception {

        myLogger.info("Saldo a Favor*************");
        double saldoFavor = 0;
        cartera.setAplicaGarantia(false);
        cartera.setAplicaMontoGarantia(0);
        //myLogger.info("Credito "+saldo.getReferencia());
        //myLogger.info("Cuotas "+saldo.getNumeroCuotasTranscurridas()+", Estatus "+saldo.getEstatus());
        if (cartera.getStatus() == 1 && cartera.getMontoCuenta() >= saldo.getSaldoConInteresAlFinal()) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 16 && saldo.getNumeroCuotasTranscurridas() >= 13 && saldo.getEstatus() == 1) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 12 && saldo.getNumeroCuotasTranscurridas() >= 9 && saldo.getEstatus() == 1) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 16 && saldo.getNumeroCuotasTranscurridas() == 15) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        } else if (saldo.getPlazo() == 12 && saldo.getNumeroCuotasTranscurridas() == 11) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        } else if ((saldo.getSaldoConInteresAlFinal() - cartera.getMontoCuenta()) < cartera.getValorCuota() && saldo.getEstatus() == 1) {
            cartera.setAplicaGarantia(true);
            cartera.setAplicaMontoGarantia(cartera.getMontoCuentaCongelada());
            cartera.setMontoCuentaCongelada(0);
        }
        if (cartera.getMontoCuentaCongelada() < cartera.getMontoCuenta())
            saldoFavor = cartera.getMontoCuenta() - cartera.getMontoCuentaCongelada();
        saldoFavor = FormatUtil.roundDouble(saldoFavor, 2);
        myLogger.info("Saldo Favor " + saldoFavor);
        System.out.println("cartera "+cartera.getNumCliente()+"_"+cartera.getNumCredito()+"_"+cartera.getMontoCuentaCongelada()+"_"+cartera.getAplicaGarantia()+"_"+cartera.getAplicaMontoGarantia());
        return saldoFavor;
    }

    private Timestamp Timestamp(long currentTimeMillis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

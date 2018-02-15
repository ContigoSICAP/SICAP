/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import com.sicap.clientes.commands.Command;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BuroCirculoTimerUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class CommandAjusteCierre implements Command {

    private String siguiente;

    public CommandAjusteCierre(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification mensaje[] = new Notification[1];
        int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
        int numCiclo = Integer.parseInt(request.getParameter("numCiclo"));
        String fechaAjuste = request.getParameter("Fecha");
        AjusteCierreDAO valida = new AjusteCierreDAO();
        CreditoCartVO credito = new CreditoCartVO();
        ArrayList<TablaAmortVO> tabGenerada = new ArrayList<TablaAmortVO>();
        try {
            BuroCirculoTimerUtil archFin = new BuroCirculoTimerUtil();
            archFin.run();
            /*credito = valida.compruebaCiclo(idGrupo, numCiclo);
            tabGenerada = AjusteCredito(credito, fechaAjuste);
            for (TablaAmortVO nuevaAmort : tabGenerada) {
                System.out.println(nuevaAmort.getNumPago() + ", " + nuevaAmort.getFechaPago() + ", "
                        + nuevaAmort.getAbonoCapital() + ", " + nuevaAmort.getCapitalPagado() + ", "
                        + nuevaAmort.getInteres() + ", " + nuevaAmort.getIvaInteres() + ", "
                        + nuevaAmort.getInteresPagado() + ", " + nuevaAmort.getIvaInteresPagado() + ", "
                        + nuevaAmort.getMulta() + ", " + nuevaAmort.getIvaMulta() + ", "
                        + nuevaAmort.getMultaPagado() + ", " + nuevaAmort.getIvaMultaPagado() + ", "
                        + nuevaAmort.getMontoPagar() + ", " + nuevaAmort.getTotalPagado() + ", "
                        + nuevaAmort.getStatus() + ", " + nuevaAmort.getPagado());
            }*/
            /*UUID cred = new UUID(2, 3);
            cred.fromString("00000000-0000-0002-0000-000000000003");
            UUID credStering = UUID.fromString("00000000-0000-0002-0000-000000000003");
            System.out.println("cred: "+cred);
            System.out.println("credStering: "+credStering);*/
            /*String cadena = "cadena a cifar";
            System.out.println("**************");
            System.out.println("Mensaje = " + cadena);
            System.out.println("MD2 = " + StringMD.getStringMessageDigest(cadena, StringMD.MD2));
            System.out.println("MD5 = " + StringMD.getStringMessageDigest(cadena, StringMD.MD5));
            System.out.println("SHA-1 = " + StringMD.getStringMessageDigest(cadena, StringMD.SHA1));
            System.out.println("SHA-256 = " + StringMD.getStringMessageDigest(cadena, StringMD.SHA256));
            System.out.println("SHA-384 = " + StringMD.getStringMessageDigest(cadena, StringMD.SHA384));
            System.out.println("SHA-512 = " + StringMD.getStringMessageDigest(cadena, StringMD.SHA512));*/
            /*StringEncrypter.testUsingSecretKey();
            StringEncrypter.testUsingPassPhrase();  */
            /*String[] args = null;
            args[0]= "cadena que se encryptara";
            //args[1]= "cadena que se encryptara dos";
            System.out.println("Pasa aqui");
            if (args.length != 1) {
                System.out.println("Sintaxis: java RSA [tamaño de los primos]");
                System.out.println("por ejemplo: java RSA 512");
                args = new String[1];
                args[0] = "1024";
            }
            int tamPrimo = Integer.parseInt(args[0]);
            RSA rsa = new RSA(tamPrimo);

            System.out.println("Tam Clave: [" + tamPrimo + "]n");

            System.out.println("p: [" + rsa.damep().toString(16).toUpperCase() + "]");
            System.out.println("q: [" + rsa.dameq().toString(16).toUpperCase() + "]n");

            System.out.println("Clave publica (n,e)");
            System.out.println("n: [" + rsa.damen().toString(16).toUpperCase() + "]");
            System.out.println("e: [" + rsa.damee().toString(16).toUpperCase() + "]n");

            System.out.println("Clave publica (n,d)");
            System.out.println("n: [" + rsa.damen().toString(16).toUpperCase() + "]");
            System.out.println("d: [" + rsa.damed().toString(16).toUpperCase() + "]n");

            System.out.println("Texto a encriptar: ");
            String textoPlano = (new BufferedReader(new InputStreamReader(System.in))).readLine();

            BigInteger[] textoCifrado = rsa.encripta(textoPlano);

            System.out.println("nTexto encriptado: [");
            for (int i = 0; i < textoCifrado.length; i++) {
                System.out.print(textoCifrado[i].toString(16).toUpperCase());
                if (i != textoCifrado.length - 1) {
                    System.out.println("");
                }
            }
            System.out.println("]n");

            String recuperarTextoPlano = rsa.desencripta(textoCifrado);
            System.out.println("Texto desencritado: [" + recuperarTextoPlano + "]");*/

            if (tabGenerada.size() > 0)
                mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "DATOS PROCESADOS");
            else
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Datos ingresados sin correlacion");
            request.setAttribute("NOTIFICACIONES", mensaje);
        } catch (Exception e) {
            System.out.println("ERROR: Inicio de Ajuste de Cierre\n" + e);
        }

        return siguiente;
    }

    private ArrayList AjusteCredito(CreditoCartVO credito, String fechaAjuste) {
        //String exito = "* * *";
        //ArrayList<TablaAmortVO> nuevaTabla = null;
        ArrayList<TablaAmortVO> amortizaciones = new ArrayList<TablaAmortVO>();
        try {
            ArrayList<PagoVO> pagos = new ArrayList<PagoVO>();
            AjusteCierreDAO ajuste = new AjusteCierreDAO();
            //ArrayList<TablaAmortVO> amortizaciones = new ArrayList<TablaAmortVO>();
            pagos = ajuste.pagos(credito);
            amortizaciones = ajuste.tablaAmortizacion(credito);
            double pagoCongelado = 0, montoPagar = 0, saldo = 0, totalPagar = 0, monto = 0, montoTotal = 0, total = 0, garantia = 0;
            int numAmort = 0, numDias = 0, numPagos = 0;/*, faltaAmort = 15*/;
            boolean pagoAtrazado = false;
            Date amortiActual = amortizaciones.get(0).getFechaPago();
            Date fechaDia = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            FormatUtil redondeo = new FormatUtil();
            /*String fechaActual = sdf.format(fechaDia);
            fechaDia = sdf.parse(fechaActual);*/
            fechaDia = sdf.parse(fechaAjuste);
            garantia = redondeo.roundDecimal(credito.getMontoDesembolsado()*0.1, 0);
            //garantia = credito.getMontoDesembolsado()*0.1;
            try {
                for (int i=0; i < pagos.size(); i++) {
                    numPagos++;
                //for (PagoVO pago : pagos) {
                    if (pagos.get(i).getFechaPago().before(credito.getFechaDesembolso()) || pagos.get(i).getFechaPago().equals(credito.getFechaDesembolso())){
                        pagoCongelado += pagos.get(i).getMonto();
                        if (pagoCongelado > garantia){
                            saldo += pagoCongelado-garantia;
                            pagoCongelado = garantia;
                        }
                        System.out.println("Congelado: "+pagoCongelado);
                        System.out.println("Saldo: "+saldo);
                    } else {
                        System.out.println(pagos.get(i).getFechaPago()+" "+amortiActual);
                        numDias = diferenciaDias(pagos.get(i).getFechaPago(), amortiActual);
                        System.out.println("Verifica: "+pagos.get(i).getFechaPago().after(amortiActual)+" "+numDias);
                        if (pagos.get(i).getFechaPago().after(amortiActual) && numDias >= 3){
                            montoPagar = amortizaciones.get(numAmort).getMontoPagar();
                            if (amortizaciones.get(numAmort).getMulta() == 0){
                                montoPagar += 100;
                            }
                            if (amortizaciones.get(numAmort).getTotalPagado() != amortizaciones.get(numAmort).getMontoPagar()){
                                amortizaciones.get(numAmort).setStatus(2);
                            }
                            amortizaciones.get(numAmort).setMontoPagar(montoPagar);
                            amortizaciones.get(numAmort).setMulta(86.21);
                            amortizaciones.get(numAmort).setIvaMulta(13.79);
                            System.out.println("Cargo de Multa");
                        } else {
                            amortizaciones.get(numAmort).setStatus(1);
                        }
                        System.out.println("------------------------------>Semana: "+numAmort);
                        if(numAmort == 13){
                            saldo += pagoCongelado;
                            System.out.println("Saldo+Congelado: "+saldo);
                        }
                        montoPagar = saldo + pagos.get(i).getMonto();
                        total = montoPagar;
                        System.out.println("Monto: "+montoPagar);
                        System.out.println("A Pagar: "+amortizaciones.get(numAmort).getMontoPagar());
                        if (montoPagar > 0){
                            montoTotal = montoPagar;
                            if (amortizaciones.get(numAmort).getMulta() > amortizaciones.get(numAmort).getMultaPagado()){
                                System.out.println("***Pago de Multa");
                                totalPagar = montoPagar;
                                System.out.println("Monto Inicial: "+totalPagar);
                                monto = (amortizaciones.get(numAmort).getMulta() + amortizaciones.get(numAmort).getIvaMulta()) - (amortizaciones.get(numAmort).getMultaPagado() + amortizaciones.get(numAmort).getIvaMultaPagado());
                                totalPagar -= monto;
                                if (totalPagar >= 0){
                                    amortizaciones.get(numAmort).setMultaPagado(amortizaciones.get(numAmort).getMulta());
                                    amortizaciones.get(numAmort).setIvaMultaPagado(amortizaciones.get(numAmort).getIvaMulta());
                                    montoPagar -= monto;
                                } else {
                                    monto = montoPagar * 0.16;
                                    amortizaciones.get(numAmort).setMultaPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getMultaPagado() + montoPagar - monto, 2));
                                    amortizaciones.get(numAmort).setIvaMultaPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getIvaMultaPagado() + monto, 2));
                                    montoPagar = 0;
                                }
                                System.out.println("Monto Final: "+montoPagar);
                            }
                            if (montoPagar  > 0 && (amortizaciones.get(numAmort).getInteres() > amortizaciones.get(numAmort).getInteresPagado())){
                                System.out.println("***Pago de Interes");
                                totalPagar = montoPagar;
                                System.out.println("Monto Inicial: "+totalPagar);
                                monto = (amortizaciones.get(numAmort).getInteres() + amortizaciones.get(numAmort).getIvaInteres()) - (amortizaciones.get(numAmort).getInteresPagado() + amortizaciones.get(numAmort).getIvaInteresPagado());
                                totalPagar -= monto;
                                if (totalPagar >= 0) {
                                    amortizaciones.get(numAmort).setInteresPagado(amortizaciones.get(numAmort).getInteres());
                                    amortizaciones.get(numAmort).setIvaInteresPagado(amortizaciones.get(numAmort).getIvaInteres());
                                    montoPagar -= monto;
                                } else {
                                    monto = montoPagar * 0.16;
                                    amortizaciones.get(numAmort).setInteresPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getInteresPagado() + montoPagar - monto, 2));
                                    amortizaciones.get(numAmort).setIvaInteresPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getIvaInteresPagado() + monto, 2));
                                    montoPagar = 0;
                                }
                                System.out.println("Monto Final: "+montoPagar);
                            }
                            if (montoPagar > 0 && amortizaciones.get(numAmort).getAbonoCapital() > amortizaciones.get(numAmort).getCapitalPagado()) {
                                System.out.println("***Pago de Capital");
                                totalPagar = montoPagar;
                                System.out.println("Monto Inicial: "+totalPagar);
                                monto = (amortizaciones.get(numAmort).getAbonoCapital()) - (amortizaciones.get(numAmort).getCapitalPagado());
                                totalPagar -= monto;
                                if (totalPagar >= 0){
                                    amortizaciones.get(numAmort).setCapitalPagado(amortizaciones.get(numAmort).getAbonoCapital());
                                    montoPagar -= monto;
                                } else {
                                    amortizaciones.get(numAmort).setCapitalPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getCapitalPagado() + montoPagar, 2));
                                    montoPagar = 0;
                                }
                                System.out.println("Monto Final: "+montoPagar);
                            }
                            saldo = montoPagar;
                            System.out.println("Saldo: "+saldo);
                            if (total >= amortizaciones.get(numAmort).getMontoPagar()){
                                amortizaciones.get(numAmort).setTotalPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getMontoPagar()+amortizaciones.get(numAmort).getTotalPagado(), 2));
                                amortizaciones.get(numAmort).setMontoPagar(0);
                                amortizaciones.get(numAmort).setPagado("S");
                                System.out.println(amortizaciones.get(numAmort).getNumPago() + ", " + amortizaciones.get(numAmort).getFechaPago() + ", "
                                    + amortizaciones.get(numAmort).getAbonoCapital() + ", " + amortizaciones.get(numAmort).getCapitalPagado() + ", "
                                    + amortizaciones.get(numAmort).getInteres() + ", " + amortizaciones.get(numAmort).getIvaInteres() + ", "
                                    + amortizaciones.get(numAmort).getInteresPagado() + ", " + amortizaciones.get(numAmort).getIvaInteresPagado() + ", "
                                    + amortizaciones.get(numAmort).getMulta() + ", " + amortizaciones.get(numAmort).getIvaMulta() + ", "
                                    + amortizaciones.get(numAmort).getMultaPagado() + ", " + amortizaciones.get(numAmort).getIvaMultaPagado() + ", "
                                    + amortizaciones.get(numAmort).getMontoPagar() + ", " + amortizaciones.get(numAmort).getTotalPagado() + ", "
                                    + amortizaciones.get(numAmort).getStatus() + ", " + amortizaciones.get(numAmort).getPagado());
                                numAmort++;
                                if ((saldo >= amortizaciones.get(numAmort).getMontoPagar()) && (pagos.get(i).getFechaPago().after(amortizaciones.get(numAmort).getFechaPago()) || pagos.get(i).getFechaPago().equals(amortizaciones.get(numAmort).getFechaPago()))) {
                                    System.out.println("Fecha del pago: "+pagos.get(i).getFechaPago()+" "+pagos.get(i).getMonto());
                                    pagos.get(i).setMonto(0);
                                    i -= 1;
                                }
                                amortiActual = amortizaciones.get(numAmort).getFechaPago();
                            } else {
                                amortizaciones.get(numAmort).setMontoPagar(redondeo.roundDecimal(amortizaciones.get(numAmort).getMontoPagar()-total, 2));
                                amortizaciones.get(numAmort).setTotalPagado(redondeo.roundDecimal(amortizaciones.get(numAmort).getTotalPagado()+total, 2));
                                System.out.println(amortizaciones.get(numAmort).getNumPago() + ", " + amortizaciones.get(numAmort).getFechaPago() + ", "
                                    + amortizaciones.get(numAmort).getAbonoCapital() + ", " + amortizaciones.get(numAmort).getCapitalPagado() + ", "
                                    + amortizaciones.get(numAmort).getInteres() + ", " + amortizaciones.get(numAmort).getIvaInteres() + ", "
                                    + amortizaciones.get(numAmort).getInteresPagado() + ", " + amortizaciones.get(numAmort).getIvaInteresPagado() + ", "
                                    + amortizaciones.get(numAmort).getMulta() + ", " + amortizaciones.get(numAmort).getIvaMulta() + ", "
                                    + amortizaciones.get(numAmort).getMultaPagado() + ", " + amortizaciones.get(numAmort).getIvaMultaPagado() + ", "
                                    + amortizaciones.get(numAmort).getMontoPagar() + ", " + amortizaciones.get(numAmort).getTotalPagado() + ", "
                                    + amortizaciones.get(numAmort).getStatus() + ", " + amortizaciones.get(numAmort).getPagado());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.debug("ERROR: Procedimeinto de Lectura de Apliocacion Pagos\n" + e);
            }
            numAmort = 0;
            //saldo += pagoCongelado;
            try {
                for (TablaAmortVO amortizacion : amortizaciones) {
                    System.out.println(amortizacion.getFechaPago()+" "+fechaDia);
                    if (amortizacion.getPagado().equals("N")){
                        if (amortizacion.getFechaPago().before(fechaDia)){
                            numDias = diferenciaDias(fechaDia, amortizacion.getFechaPago());
                            pagoAtrazado = true;
                            amortizacion.setStatus(2);
                        }
                        if (amortizacion.getFechaPago().after(fechaDia)){
                            numDias = diferenciaDias(amortizacion.getFechaPago(), fechaDia);
                            pagoAtrazado = false;
                            if (numDias <= 7 || amortizacion.getMontoPagar() <= saldo)   amortizacion.setStatus(1);
                            else                amortizacion.setStatus(0);
                        }
                        //if (amortizacion.getStatus() == 0 || amortizacion.getStatus() == 2){
                        if (amortizacion.getStatus() != 0){
                            System.out.println("Arrastre de Saldo: "+saldo);
                            if (pagoAtrazado && numDias >= 3){
                                montoPagar = amortizacion.getMontoPagar();
                                if (amortizacion.getMulta() == 0){
                                    montoPagar += 100;
                                }
                                amortizacion.setStatus(2);
                                amortizacion.setMontoPagar(montoPagar);
                                amortizacion.setMulta(86.21);
                                amortizacion.setIvaMulta(13.79);
                                System.out.println("Cargo de Multa");
                                System.out.println(numAmort+") "+amortizacion.getStatus()+" "+amortizacion.getMontoPagar()+" "+amortizacion.getMulta()+" "+amortizacion.getIvaMulta());
                            }
                            if (saldo > 0){
                                total = saldo;
                                if (amortizacion.getMulta() > amortizacion.getMultaPagado()){
                                    System.out.println("***Pago de Multa");
                                    totalPagar = saldo;
                                    System.out.println("Monto Inicial: "+totalPagar);
                                    monto = (amortizacion.getMulta() + amortizacion.getIvaMulta()) - (amortizacion.getMultaPagado() + amortizacion.getIvaMultaPagado());
                                    totalPagar -= monto;
                                    if (totalPagar >= 0){
                                        amortizacion.setMultaPagado(amortizacion.getMulta());
                                        amortizacion.setIvaMultaPagado(amortizacion.getIvaMulta());
                                        saldo -= monto;
                                    } else {
                                        monto = saldo * 0.16;
                                        amortizacion.setMultaPagado(redondeo.roundDecimal(amortizacion.getMultaPagado() + saldo - monto, 2));
                                        amortizacion.setIvaMultaPagado(redondeo.roundDecimal(amortizacion.getIvaMultaPagado() + monto, 2));
                                        saldo = 0;
                                    }
                                    System.out.println("Monto Final: "+saldo);
                                }
                                if (saldo  > 0 && (amortizacion.getInteres() > amortizacion.getInteresPagado())){
                                    System.out.println("***Pago de Interes");
                                    totalPagar = saldo;
                                    System.out.println("Monto Inicial: "+totalPagar);
                                    monto = (amortizacion.getInteres() + amortizacion.getIvaInteres()) - (amortizacion.getInteresPagado() + amortizacion.getIvaInteresPagado());
                                    totalPagar -= monto;
                                    if (totalPagar >= 0){
                                        amortizacion.setInteresPagado(amortizacion.getInteres());
                                        amortizacion.setIvaInteresPagado(amortizacion.getIvaInteres());
                                        saldo -= monto;
                                    } else {
                                        monto = saldo * 0.16;
                                        amortizacion.setInteresPagado(redondeo.roundDecimal(amortizacion.getInteresPagado() + saldo - monto, 2));
                                        amortizacion.setIvaInteresPagado(redondeo.roundDecimal(amortizacion.getIvaInteresPagado() + monto, 2));
                                        saldo = 0;
                                    }
                                    System.out.println("Monto Final: "+saldo);
                                }
                                if (saldo > 0 && amortizacion.getAbonoCapital() > amortizacion.getCapitalPagado()) {
                                    System.out.println("***Pago de Capital");
                                    totalPagar = saldo;
                                    System.out.println("Monto Inicial: "+totalPagar);
                                    monto = (amortizacion.getAbonoCapital()) - (amortizacion.getCapitalPagado());
                                    totalPagar -= monto;
                                    if (totalPagar >= 0){
                                        amortizacion.setCapitalPagado(amortizacion.getAbonoCapital());
                                        saldo -= monto;
                                    } else {
                                        amortizacion.setCapitalPagado(redondeo.roundDecimal(amortizacion.getCapitalPagado() + saldo, 2));
                                        saldo = 0;
                                    }
                                    System.out.println("Monto Final: "+saldo);
                                }
                                if (amortizacion.getAbonoCapital() == amortizacion.getCapitalPagado()){
                                    amortizacion.setTotalPagado(redondeo.roundDecimal(amortizacion.getMontoPagar()+amortizacion.getTotalPagado(), 2));
                                    amortizacion.setMontoPagar(0);
                                    amortizacion.setStatus(1);
                                    amortizacion.setPagado("S");
                                } else {
                                    amortizacion.setMontoPagar(redondeo.roundDecimal(amortizacion.getMontoPagar()-total, 2));
                                    amortizacion.setTotalPagado(redondeo.roundDecimal(amortizacion.getTotalPagado()+total, 2));
                                    if (amortizacion.getFechaPago().before(fechaDia)){
                                        amortizacion.setStatus(2);
                                        
                                    }
                                }
                            } else {
                                if (amortizacion.getFechaPago().before(fechaDia)){
                                    amortizacion.setStatus(2);
                                }
                            }
                            if (numAmort == 13 && amortizacion.getPagado().equals("S"))
                                saldo += garantia;
                        }
                        if (amortizacion.getFechaPago().after(fechaDia) && numDias <=7)
                            amortizacion.setStatus(1);
                    }
                    numAmort++;
                }
            } catch (Exception e) {
                Logger.debug("ERROR: Procedimeinto de Aplicacion de Saldo a Favor\n" + e);
            }
            double montoPagado=0, pagoMulta=0, pagoIvaMulta=0, pagoInteres=0, pagoIvaInteres=0, pagoCapital=0;
            double saldoTotalDia=0, saldoCapital=0, debeMulta=0, debeIvaMulta=0, debeInteres=0, debeIvaInteres=0, debeCapital=0, saldoAlFinal=0;
            double actualInteres=0, actualIvaInteres=0, actualCapital=0, totalVencido=0, saldoDia=0;
            double capital=0, interes=0, ivainteres=0;
            SaldoIBSVO taSaldos = new SaldoIBSVO();
            int numCuota=0, status=0, diasMora=0, numDiasTrans=0, cuotasVencidas=0;
            Date dateAjuste = new Date();
            dateAjuste = sdf.parse(fechaAjuste);
            java.sql.Date date = new java.sql.Date(dateAjuste.getTime());
            try {
                for (TablaAmortVO amortizacion : amortizaciones) {
                    pagoMulta += amortizacion.getMultaPagado();
                    pagoIvaMulta += amortizacion.getIvaMultaPagado();
                    pagoInteres += amortizacion.getInteresPagado();
                    pagoIvaInteres += amortizacion.getIvaInteresPagado();
                    pagoCapital += amortizacion.getCapitalPagado();
                    saldoCapital += amortizacion.getAbonoCapital()-amortizacion.getCapitalPagado();
                    montoPagado += amortizacion.getTotalPagado();
                    saldoAlFinal += amortizacion.getMontoPagar();
                    if (amortizacion.getStatus() == 2 && amortizacion.getPagado().equals("N")) {
                        debeMulta += amortizacion.getMulta()-amortizacion.getMultaPagado();
                        debeIvaMulta += amortizacion.getIvaMulta()-amortizacion.getIvaMultaPagado();
                        debeInteres += amortizacion.getInteres()-amortizacion.getInteresPagado();
                        debeIvaInteres += amortizacion.getIvaInteres()-amortizacion.getIvaInteresPagado();
                        debeCapital += amortizacion.getAbonoCapital()-amortizacion.getCapitalPagado();
                        //dateAjuste = sdf.parse(fechaAjuste);
                        numDias = diferenciaDias(dateAjuste, amortizacion.getFechaPago());
                        if (numDias > diasMora)
                            diasMora = numDias;
                        cuotasVencidas++;
                    }
                    if (amortizacion.getStatus() == 1 && amortizacion.getPagado().equals("N")) {
                        actualInteres = amortizacion.getInteres();
                        actualIvaInteres = amortizacion.getIvaInteres();
                        actualCapital = amortizacion.getAbonoCapital();
                        numCuota = amortizacion.getNumPago()-1;
                    }
                    if (amortizacion.getNumPago() >= 14 && amortizacion.getPagado().equals("S"))
                        pagoCongelado = 0;
                    if (amortizacion.getStatus() != 0) {
                        //numCuota = amortizacion.getNumPago();
                        fechaDia = amortizacion.getFechaPago();
                    }
                    /*if (amortizacion.getStatus() > 0){
                        numCuota = amortizacion.getNumPago();
                    }*/
                    //if (amortizacion.getStatus() != 0)
                    if (debeMulta != 0 || debeIvaMulta != 0 || debeInteres != 0 || debeIvaInteres != 0 || debeCapital != 0)
                        status = 2;
                    else
                        status = 1;
                    if (amortizacion.getNumPago() == 16 && amortizacion.getPagado().equals("S")){
                        numCuota = amortizacion.getNumPago();
                        status = 3;
                    }
                    if(amortizacion.getPagado().equals("N")){
                        capital = amortizacion.getAbonoCapital()-amortizacion.getCapitalPagado();
                        interes = amortizacion.getInteres()-amortizacion.getInteresPagado();
                        ivainteres = amortizacion.getIvaInteres()-amortizacion.getIvaInteresPagado();
                        saldoDia += capital+interes+ivainteres;
                    }
                    if (amortizacion.getNumPago() == 1)
                        //numDiasTrans = diferenciaDias(dateAjuste, amortizacion.getFechaPago());
                        numDiasTrans = diferenciaDias(dateAjuste, credito.getFechaDesembolso());
                }
                totalVencido = debeCapital+debeInteres+debeIvaInteres+debeMulta+debeIvaMulta;
                saldoTotalDia = saldoCapital+actualInteres+actualIvaInteres+totalVencido;
                saldo += pagoCongelado;
                saldoDia += debeMulta+debeIvaMulta;
                //System.out.println("ib_saldo_total_dia: "+saldoTotalDia);
                taSaldos.setSaldoTotalAlDia(redondeo.roundDecimal(saldoTotalDia, 2));
                //System.out.println("ib_saldo_capital: "+saldoCapital);
                taSaldos.setSaldoCapital(redondeo.roundDecimal(saldoCapital, 2));
                //System.out.println("ib_saldo_interes: "+(actualInteres+debeInteres));
                taSaldos.setSaldoInteres(redondeo.roundDecimal((actualInteres+debeInteres), 2));
                //System.out.println("ib_saldo_interes_vigente: "+(actualInteres+debeInteres));
                taSaldos.setSaldoInteresVigente(redondeo.roundDecimal(actualInteres, 2));
                //System.out.println("ib_saldo_interes_vigente: "+taSaldos.getSaldoInteresVigente());
                //System.out.println("ib_saldo_interes_vencido: "+debeInteres);
                taSaldos.setSaldoInteresVencido(redondeo.roundDecimal(debeInteres, 2));
                //System.out.println("ib_saldo_iva_interes: "+taSaldos.getSaldoIvaInteres());
                taSaldos.setSaldoIvaInteres(redondeo.roundDecimal((actualIvaInteres+debeIvaInteres), 2));
                //System.out.println("ib_saldo_multa: "+debeMulta);
                taSaldos.setSaldoMulta(redondeo.roundDecimal(debeMulta, 2));
                //System.out.println("ib_saldo_iva_multa: "+debeIvaMulta);
                taSaldos.setSaldoIVAMulta(redondeo.roundDecimal(debeIvaMulta, 2));
                //System.out.println("ib_capital_pagado: "+pagoCapital);
                taSaldos.setCapitalPagado(redondeo.roundDecimal(pagoCapital, 2));
                //System.out.println("ib_interes_normal_pagado: "+pagoInteres);
                taSaldos.setInteresNormalPagado(redondeo.roundDecimal(pagoInteres, 2));
                //System.out.println("ib_iva_interes_normal_pagado: "+pagoIvaInteres);
                taSaldos.setIvaInteresNormalPagado(redondeo.roundDecimal(pagoIvaInteres, 2));
                //System.out.println("ib_multa_pagada: "+pagoMulta);
                taSaldos.setMultaPagada(redondeo.roundDecimal(pagoMulta, 2));
                //System.out.println("ib_iva_multa_pagada: "+pagoIvaMulta);
                taSaldos.setIvaMultaPagado(redondeo.roundDecimal(pagoIvaMulta, 2));
                //System.out.println("ib_fecha_sig_amortizacion: "+fechaDia);
                //taSaldos.setFechaSigAmortizacion();
                //System.out.println("ib_capital_sig_amortizacion: "+actualCapital);
                taSaldos.setCapitalSigAmortizacion(redondeo.roundDecimal(actualCapital, 2));
                //System.out.println("ib_interes_sig_amortizacion: "+actualInteres);
                taSaldos.setInteresSigAmortizacion(redondeo.roundDecimal(actualInteres, 2));
                //System.out.println("ib_iva_interes_sig_amortizacion: "+actualIvaInteres);
                taSaldos.setIvaSigAmortizacion(redondeo.roundDecimal(actualIvaInteres, 2));
                //System.out.println("ib_saldo_con_intereses_al_final: "+saldoDia);
                taSaldos.setSaldoConInteresAlFinal(redondeo.roundDecimal(saldoDia, 2));
                //System.out.println("ib_capital_vencido: "+debeCapital);
                taSaldos.setCapitalVencido(redondeo.roundDecimal(debeCapital, 2));
                //System.out.println("ib_total_vencido: "+totalVencido);
                taSaldos.setTotalVencido(redondeo.roundDecimal(totalVencido, 2));
                //System.out.println("ib_estatus: "+status);
                taSaldos.setEstatus(status);
                //System.out.println("ib_moto_total_pagado: "+montoPagado);
                taSaldos.setMontoTotalPagado(redondeo.roundDecimal(montoPagado, 2));
                //System.out.println("ib_fecha_ultimo_pago: "+dateAjuste);
                taSaldos.setFechaUltimoPago(date);
                //System.out.println("ib_saldo_bucket: "+saldo);
                taSaldos.setSaldoBucket(redondeo.roundDecimal(saldo, 2));
                //System.out.println("IB_FECHA_GENERACION: "+dateAjuste);
                taSaldos.setFechaGeneracion(date);
                //System.out.println("ib_num_cuotas_trancurridas: "+numCuota);
                taSaldos.setNumeroCuotasTranscurridas(numCuota);
                //System.out.println("IB_NUM_DIAS_MORA: "+diasMora);
                taSaldos.setDiasMora(diasMora);
                //System.out.println("numPagos: "+numPagos);
                taSaldos.setNumeroPagosRealizados(numPagos);
                //System.out.println("pagoCongelado: "+pagoCongelado);
                //System.out.println("saldoConinteres: "+saldoDia);
                taSaldos.setDiasTranscurridos(numDiasTrans);
                taSaldos.setCuotasVencidas(cuotasVencidas);
                
                try {
                    for (TablaAmortVO amortizacion : amortizaciones) {
                        System.out.println("UPDATE cf_cartera_db.D_TABLA_AMORTIZACION SET "+
                                "TA_CAPITAL_PAGADO="+amortizacion.getCapitalPagado()+","+
                                "TA_INTERES_PAGADO="+amortizacion.getInteresPagado()+","+
                                "TA_IVA_INTERES_PAGADO="+amortizacion.getIvaInteresPagado()+","+
                                "TA_MULTA="+amortizacion.getMulta()+","+
                                "TA_IVA_MULTA="+amortizacion.getIvaMulta()+","+
                                "TA_MULTA_PAGADO="+amortizacion.getMultaPagado()+","+
                                "TA_IVA_MULTA_PAGADO="+amortizacion.getIvaMultaPagado()+","+
                                "TA_MONTO_PAGAR="+amortizacion.getMontoPagar()+","+
                                "TA_MONTO_PAGADO="+amortizacion.getTotalPagado()+","+
                                "TA_STATUS="+amortizacion.getStatus()+","+
                                "TA_PAGADO='"+amortizacion.getPagado()+"' "+
                                "WHERE TA_NUMCLIENTE="+credito.getNumCliente()+" AND TA_NUMCREDITO="+credito.getNumCredito()+" AND TA_NUMPAGO="+amortizacion.getNumPago()+";");
                    }
                    System.out.println("UPDATE cf_cartera_db.d_credito SET "+
                            "CR_MONTO_CUENTA="+saldo+","+
                            "CR_NUM_DIAS="+numDiasTrans+","+
                            "CR_DIAS_MORA="+diasMora+","+
                            "CR_STATUS="+status+","+
                            "CR_MONTO_CUENTA_CONGELADA="+pagoCongelado+" "+
                            "WHERE CR_NUM_CLIENTE="+credito.getNumCliente()+" AND CR_NUM_CREDITO="+credito.getNumCredito()+";");
                    if (status == 3)
                        System.out.println("UPDATE clientes.D_CICLOS_GRUPALES SET CI_ESTATUS=2 "+
                                "WHERE CI_NUMGRUPO="+credito.getNumCliente()+" AND CI_NUMCREDITO_IBS="+credito.getNumCredito()+";");
                    else
                        System.out.println("UPDATE clientes.D_CICLOS_GRUPALES SET CI_ESTATUS=1 "+
                                "WHERE CI_NUMGRUPO="+credito.getNumCliente()+" AND CI_NUMCREDITO_IBS="+credito.getNumCredito()+";");
                    System.out.println("UPDATE clientes.d_saldos SET "+
                            "IB_FECHA_GENERACION='"+taSaldos.getFechaGeneracion()+"',"+
                            "ib_num_cuotas_trancurridas="+numCuota+","+
                            "ib_saldo_total_dia="+taSaldos.getSaldoTotalAlDia()+","+
                            "ib_saldo_capital="+taSaldos.getSaldoCapital()+","+
                            "ib_saldo_interes="+taSaldos.getSaldoInteres()+","+
                            "ib_saldo_interes_vigente="+taSaldos.getSaldoInteresVigente()+","+
                            "ib_saldo_interes_vencido="+taSaldos.getSaldoInteresVencido()+","+
                            "ib_saldo_iva_interes="+taSaldos.getSaldoIvaInteres()+","+
                            "ib_saldo_multa="+taSaldos.getSaldoMulta()+","+
                            "ib_saldo_iva_multa="+taSaldos.getSaldoIVAMulta()+","+
                            "ib_capital_pagado="+taSaldos.getCapitalPagado()+","+
                            "ib_interes_normal_pagado="+taSaldos.getInteresNormalPagado()+","+
                            "ib_iva_interes_normal_pagado="+taSaldos.getIvaInteresNormalPagado()+","+
                            "ib_multa_pagada="+taSaldos.getMultaPagada()+","+
                            "ib_iva_multa_pagada="+taSaldos.getIvaMultaPagado()+","+
                            "ib_fecha_sig_amortizacion='"+fechaDia+"',"+
                            "ib_capital_sig_amortizacion="+taSaldos.getCapitalSigAmortizacion()+","+
                            "ib_interes_sig_amortizacion="+taSaldos.getInteresSigAmortizacion()+","+
                            "ib_iva_interes_sig_amortizacion="+taSaldos.getIvaSigAmortizacion()+","+
                            "ib_saldo_con_intereses_al_final="+taSaldos.getSaldoConInteresAlFinal()+","+
                            "ib_capital_vencido="+taSaldos.getCapitalVencido()+","+
                            "ib_total_vencido="+taSaldos.getTotalVencido()+","+
                            "ib_estatus="+taSaldos.getEstatus()+","+
                            "ib_num_dias_mora="+taSaldos.getDiasMora()+","+
                            "ib_dias_transcurridos="+taSaldos.getDiasTranscurridos()+","+
                            "ib_cuotas_vencidas="+taSaldos.getCuotasVencidas()+","+
                            "ib_num_pagos_realizados="+taSaldos.getNumeroPagosRealizados()+","+
                            "ib_moto_total_pagado="+taSaldos.getMontoTotalPagado()+","+
                            "ib_fecha_ultimo_pago='"+taSaldos.getFechaUltimoPago()+"',"+
                            "ib_saldo_bucket="+taSaldos.getSaldoBucket()+" "+
                            "WHERE IB_NUMCLIENTESICAP="+credito.getNumCliente()+" AND IB_CREDITO="+credito.getNumCredito()+
                            ";");
                    System.out.println("UPDATE clientes.d_pagos_cartera SET PC_ENVIADO=1 WHERE PC_REFERENCIA='"+credito.getReferencia()+"';");
                } catch (Exception e) {
                    Logger.debug("ERROR: Procedimeinto de Actualizacion de Valores en BD\n" + e);
                }
            } catch (Exception e) {
                Logger.debug("ERROR: Procedimeinto de Actualizacion de Valores\n" + e);
            }
        } catch (Exception e) {
            Logger.debug("ERROR: Procedimeinto de Ajuste\n" + e);
        }
        return amortizaciones;
    }

    private int diferenciaDias(Date fechaPago, Date fechaAmort) {
        int numeroDias = 0;
        try {
            numeroDias = FechasUtil.inBetweenDays(fechaAmort, fechaPago);
        } catch (Exception e) {
            Logger.debug("Error calculando fechas" + e);
            e.printStackTrace();
        }
        return numeroDias;
    }
}

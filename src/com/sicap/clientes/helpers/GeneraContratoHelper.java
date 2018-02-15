package com.sicap.clientes.helpers;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.DocumentoFormalizDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConvertNumberToString;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.DocumentoFormalizVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.ReferenciaPersonalVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.TelefonoVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sun.font.Font2D;

public class GeneraContratoHelper {

    private static Logger myLogger = Logger.getLogger(GeneraContratoHelper.class);

    Document document;
    Font titleFont;
    Font headFont;
    Font boldFont;
    Font bodyFont;
    Font tableFont;
    Font labelFont;
    Font smallFont;
    Font numberFont;
    int letra;
    Table dataTable;
    String fechaHoy;

    public static String makePagareIndividual(GrupoVO grupo, CicloGrupalVO ciclo, TreeMap catComisionesGrupal) throws ClientesException {

        String lineaPagare = "";
        Double tasaInteres = null;

        try {
            File file = null;
            file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "PagareIndividual.txt");
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            dis.mark(-1);
            TasaInteresVO tasa = null;
            ConvertNumberToString numeroEntero;
            String fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago);
            int plazo = ciclo.tablaAmortizacion.length - 1;
            SucursalDAO sucDAO = new SucursalDAO();
            SucursalVO sucursales = sucDAO.getSucursal(grupo.sucursal);
            String domGrupo = sucursales.direccion_calle + " # " + sucursales.numero + " Col. " + sucursales.colonia + " C.P. " + sucursales.cp;
            try {
                //Para obtener comisi�n
                TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
                if (catTasas != null) {
                    tasa = catTasas.get(ciclo.tasa);
                    tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                myLogger.error("generaHojaResumen", e);
            }
            String mtoLetrasEntero = "";
            String mtoDecimal = "";
            int decimal = 0;
            boolean a = ciclo.integrantes != null;
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                //idSolicitud
                Double montoPagar = 0d;
                if (grupo.idOperacion == ClientesConstants.GRUPAL) {
                    montoPagar = (ciclo.integrantes[i].monto + ciclo.integrantes[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal)) * ((FormatUtil.roundDouble(tasa.valor * 1.16, 2)) / 100) / 360;
                    montoPagar = (ciclo.integrantes[i].monto + ciclo.integrantes[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal)) + plazo * 7 * montoPagar;
                } else {
                    montoPagar = ciclo.integrantes[i].monto * ((FormatUtil.roundDouble(tasa.valor * 1.16, 2)) / 100) / 360;
                    montoPagar = ciclo.integrantes[i].monto + plazo * 7 * montoPagar;
                }
                String montoPagarFormat = String.valueOf(HTMLHelper.formatoMonto(montoPagar));
                decimal = montoPagarFormat.indexOf(".");
                Integer entero = Integer.parseInt(montoPagarFormat.substring(0, decimal));
                numeroEntero = new ConvertNumberToString(entero);
                //Conversi�n de la cifra a letras
                mtoLetrasEntero = numeroEntero.convertirLetras(entero);
                mtoDecimal = montoPagarFormat.substring(decimal + 1);
                String domicilio = ClientesUtil.getDomicilio(ciclo.integrantes[i].idCliente);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    line = line.replace("<DOMICILIOGRUPO>", domGrupo);
                    line = line.replace("<NOMBRECLIENTE>", ciclo.integrantes[i].nombre);
                    line = line.replace("<DOMICILIOCLIENTE>", domicilio);
                    line = line.replace("<MONTOLETRASENTERO>", mtoLetrasEntero.toUpperCase());
                    line = line.replace("<DECIMALPAGARE>", mtoDecimal);
                    line = line.replace("<PLAZO>", String.valueOf(plazo));
                    line = line.replace("<TASAINTERES>", HTMLHelper.formatoMonto(tasaInteres));
                    line = line.replace("<FECDIAS>", fechaFirma.substring(0, 2));
                    line = line.replace("<FECMES>", FechasUtil.obtenNombreMes(fechaFirma).toLowerCase());
                    line = line.replace("<FECAÑO>", fechaFirma.substring(fechaFirma.lastIndexOf("/") + 1));
                    line = line.replace("<TASAINTERESDOS>", HTMLHelper.formatoMonto(tasaInteres * 2));
                    line = line.replace("<MONTOPAGARE>", montoPagarFormat);
                    lineaPagare += "<p align='justify' <font size='2.5' face='Arial'>" + line + "</p>";
                }
                lineaPagare += "<br><br><br><br><br>";
                dis.reset();
                //tablas.put(ciclo.integrantes[i].idCliente, tabla);

            }
            fis.close();
            bis.close();
            dis.close();
        } catch (IOException e) {
            myLogger.error("generaHojaResumen", e);
        }
        return lineaPagare;
    }

    public static String makeOrdenPago(CicloGrupalVO ciclo) {

        StringBuffer buffer = new StringBuffer();
        ParametroVO parametroVO = new ParametroVO();
        String CVE_BANCO = "";
        String font = "<font face=\"arial\" size=\"4\" color=\"#990000\">";
        String font2 = "<font face=\"arial\" size=\"4\" color=\"#009966\">";
        String fecha = null;
        try {
            for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                if (ciclo.integrantes[i].ordenPago != null) {
                    StringBuffer bufferCliente = new StringBuffer();
                    fecha = Convertidor.timestampToString(ciclo.integrantes[i].ordenPago.getFechaEnvio());
                    int idBanco = ciclo.integrantes[i].ordenPago.getIdBanco();
                    if (idBanco == ClientesConstants.NO_BANCO_BANCOMER) {
                        CVE_BANCO = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
                        bufferCliente.append("<br><br><br><br><br><br><br>");
                        bufferCliente.append("<center><b>ORDEN DE PAGO</b></center>");
                        bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");
                        bufferCliente.append("<table align='center' border='0' cellspacing='5'>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "FECHA:</font></b></center></td>");
                        bufferCliente.append("<td><left>" + font + "<b>" + fecha + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "BENEFICIARIO:</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + ciclo.integrantes[i].ordenPago.getNombre() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "REFERENCIA</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + CVE_BANCO + ciclo.integrantes[i].ordenPago.getReferencia() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "IMPORTE</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + ciclo.integrantes[i].ordenPago.getMonto() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "CONVENIO</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + '0' + CVE_BANCO + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "CONCEPTO</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + "ODP" + ciclo.integrantes[i].ordenPago.getIdCliente() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "CLAVE DE IDENTIFICACION</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + "IFE" + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "BANCO</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + "BBVA-BANCOMER</font></b></center></td></tr>");
                        bufferCliente.append("</table>");
                        bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");
                        buffer.append(bufferCliente.toString());
                        buffer.append("<br><br><br><br><br><tr><td colspan='8'><hr size='2' color='#006633' align='center' width='100%'></hr></td></tr><br>");
                        buffer.append(bufferCliente.toString());

                    } else {
                        CVE_BANCO = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
                        bufferCliente.append("<br><br><br><br><br><br><br>");
                        bufferCliente.append("<center><b>ORDEN DE PAGO</b></center>");
                        bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");
                        bufferCliente.append("<table align='center' border='0' cellspacing='5'>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "FECHA:</font></b></center></td>");
                        bufferCliente.append("<td><left>" + font + "<b>" + fecha + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "BENEFICIARIO:</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + ciclo.integrantes[i].ordenPago.getNombre() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "REFERENCIA</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + CVE_BANCO + ciclo.integrantes[i].ordenPago.getReferencia() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "IMPORTE</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + ciclo.integrantes[i].ordenPago.getMonto() + "</font></b></center></td></tr>");
                        bufferCliente.append("<tr><td><right><b>" + font2 + "BANCO</font></b></center></td>");
                        bufferCliente.append("<td><left><b>" + font + "BANORTE</font></b></center></td></tr>");
                        bufferCliente.append("</table>");
                        bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");
                        buffer.append(bufferCliente.toString());
                        buffer.append("<br><br><br><br><br><tr><td colspan='8'><hr size='2' color='#006633' align='center' width='100%'></hr></td></tr><br>");
                        buffer.append(bufferCliente.toString());

                    }
                }

            }
        } catch (Exception e) {
            myLogger.error("generaHojaResumen", e);
        }
        return buffer.toString();
    }

    public static String makeOrdenPago(ClienteVO cliente, int idSolicitud) {

        StringBuffer buffer = new StringBuffer();
        String CVE_BANORTE = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
        StringBuffer bufferCliente = new StringBuffer();
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        OrdenDePagoVO ordenPago = cliente.solicitudes[indiceSolicitud].ordenPago;
        String font = "<font face=\"arial\" size=\"4\" color=\"#990000\">";
        String font2 = "<font face=\"arial\" size=\"4\" color=\"#009966\">";

        try {

            if (ordenPago != null) {

                String fecha = Convertidor.timestampToString(ordenPago.getFechaEnvio());

                bufferCliente.append("<br><br><br><br><br><br><br>");
                bufferCliente.append("<center><b>ORDEN DE PAGO</b></center>");
                bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");
                bufferCliente.append("<table align='center' border='0' cellspacing='5'>");

                bufferCliente.append("<tr><td><right><b>" + font2 + "FECHA:</font></b></center></td>");
                bufferCliente.append("<td><left><b>" + font + fecha + "</font></b></center></td></tr>");
                bufferCliente.append("<tr><td><right><b>" + font2 + "BENEFICIARIO:</font></b></center></td>");
                bufferCliente.append("<td><left><b>" + font + ordenPago.getNombre() + "</font></b></center></td></tr>");
                bufferCliente.append("<tr><td><right><b>" + font2 + "REFERENCIA</font></b></center></td>");
                bufferCliente.append("<td><left><b>" + font + CVE_BANORTE + ordenPago.getReferencia() + "</font></b></center></td></tr>");
                bufferCliente.append("<tr><td><right><b>" + font2 + "IMPORTE</font></b></center></td>");
                bufferCliente.append("<td><left><b>" + font + ordenPago.getMonto() + "</font></b></center></td></tr>");
                bufferCliente.append("<tr><td><right><b>" + font2 + "BANCO</font></b></center></td>");
                bufferCliente.append("<td><left><b>" + font + "BANORTE</font></b></center></td></tr>");
                bufferCliente.append("</table>");
                bufferCliente.append("<tr><td colspan='8'><hr size='5' color='#006633' align='center' width='80%'></hr></td></tr>");

                buffer.append(bufferCliente.toString());
                buffer.append("<br><br><br><br><br><tr><td colspan='8'><hr size='2' color='#006633' align='center' width='100%'></hr></td></tr><br>");
                buffer.append(bufferCliente.toString());

            }
        } catch (Exception e) {
            myLogger.error("makeOrdenPago", e);
        }
        return buffer.toString();
    }

    public static String makeContract(GrupoVO grupo, CicloGrupalVO ciclo) {

        String lineaContrato = "";
        String CAT = "";

        try {
            File file = null;
            file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoGrupal.txt");
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            NumberFormat NF = NumberFormat.getInstance();
            NF.setMaximumFractionDigits(2);
            TasaInteresVO tasa = null;
            String nombreGrupo = grupo.nombre;
            String domGrupo = GrupoHelper.getDomicilio(ciclo);
            ConvertNumberToString numeroEntero;
            try {
                //Para obtener tasa
                TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);

                if (catTasas != null) {
                    tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
                    //tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                myLogger.error("makeContract", e);
            }
            String mtoLetrasEntero = "";
            String mtoDecimal = "";
            int decimal = 0;
            Double montoAutorizado = ciclo.tablaAmortizacion[0].saldoInicial;
            String monto = String.valueOf(HTMLHelper.formatoMonto(montoAutorizado));

            myLogger.debug(monto);
            decimal = monto.indexOf(".");
            Integer entero = Integer.parseInt(monto.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(entero);
            //Conversi�n de la cifra a letras
            mtoLetrasEntero = numeroEntero.convertirLetras(entero);
            mtoDecimal = monto.substring(decimal + 1);

            String fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago);

            //Monto del pagar�
            Double temp = 0.0;
            myLogger.debug("ciclo.tablaAmortizacion.length " + ciclo.tablaAmortizacion.length);
            for (int i = 1; i < (ciclo.tablaAmortizacion.length); i++) {
                temp += ciclo.tablaAmortizacion[i].montoPagar;
            }

            String montoPagare = HTMLHelper.formatoMonto(temp);
            decimal = montoPagare.indexOf(".");
            entero = Integer.parseInt(montoPagare.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(entero);
            String enteroPagare = numeroEntero.convertirLetras(entero);
            String decimalPagare = montoPagare.substring(decimal + 1);

            ArrayList<String> fechasPagos = new ArrayList<String>();
            double pagos[] = null;
            int plazo = 0;
            try {
                TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
                pagos = new double[tablaVO.length];
                plazo = tablaVO.length - 1;
                for (int i = 0; i < tablaVO.length; i++) {
                    fechasPagos.add(Convertidor.dateToString(tablaVO[i].fechaPago));
                    if (i == 0) {
                        pagos[i] = tablaVO[i].montoPagar;
                    } else {
                        pagos[i] = tablaVO[i].abonoCapital + tablaVO[i].interes;
                    }
                }
            } catch (Exception e) {
                myLogger.error("makeContract", e);
            }

            if (ciclo.tablaAmortizacion != null && ciclo.tablaAmortizacion[0].saldoInicial > 0) {
                CAT = getCAT(ciclo.tablaAmortizacion[0].saldoInicial, pagos, fechasPagos);
            }

            while (dis.available() != 0) {
                String line = dis.readLine();
                line = line.replace("<GRUPO>", nombreGrupo);
                line = line.replace("<DOMICILIOGRUPO>", domGrupo);
                line = line.replace("<MONTOCREDITO>", monto);
                line = line.replace("<MONTOENTERO>", mtoLetrasEntero.toUpperCase());
                line = line.replace("<MONTODECIMAL>", mtoDecimal);
                line = line.replace("<TASAINTERES>", HTMLHelper.formatoMonto(tasa.valor));
                line = line.replace("<FECDIAS>", fechaFirma.substring(0, 2));
                line = line.replace("<FECMES>", FechasUtil.obtenNombreMes(fechaFirma).toLowerCase());
                line = line.replace("<FECAÑO>", fechaFirma.substring(fechaFirma.lastIndexOf("/") + 1));
                line = line.replace("<TASAINTERESDOS>", HTMLHelper.formatoMonto(tasa.valor * 2));
                line = line.replace("<CONTRATOLD>", HTMLHelper.displayField(ciclo.referencia));
                line = line.replace("<MONTOPAGARE>", montoPagare);
                line = line.replace("<PLAZO>", String.valueOf(plazo));
                line = line.replace("<ENTEROPAGARE>", enteroPagare.toUpperCase());
                line = line.replace("<DECIMALPAGARE>", decimalPagare);
                line = line.replace("<CAT>", CAT);
                line = line.replace("<NUMPAGOS>", String.valueOf(plazo));

                if (line.equals("**ANEXO AMORTIZACIONES**")) {
                    lineaContrato += TablaAmortizacionHelper.makeTable(ciclo.tablaAmortizacion);
                } else if (line.equals("**ANEXO PAGOS**")) {
                    lineaContrato += "<center>" + TablaAmortizacionHelper.makePagare(ciclo.tablaAmortizacion) + "</center>";
                } else if (line.equals("**LISTADO DE INTEGRANTES**")) {
                    lineaContrato += getTablaIntegrantes(ciclo);
                } else if (line.equals("**LISTADO DE INTEGRANTES PAGARE**")) {
                    lineaContrato += getTablaIntegrantesPagare(ciclo);
                } else {
                    lineaContrato += "<p align='justify' <font size='2.5' face='Arial'>" + line + "</p>";
                }
            }

            fis.close();
            bis.close();
            dis.close();
        } catch (IOException e) {
            myLogger.error("makeContract" + e);
        }
        return lineaContrato;
    }

    public static String makeContract(ClienteVO cliente, int idSolicitud) {

        String lineaContrato = "";
        Double tasaComision = null;
        Double tasaInteres = null;
        SucursalVO sucursal = new SucursalVO();
        SucursalDAO sucursalDAO = new SucursalDAO();
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        int tipoOperacion = cliente.solicitudes[indiceSolicitud].tipoOperacion;
        try {
            File file = null;
            if (tipoOperacion == ClientesConstants.MICROCREDITO) {
                file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoMicro.txt");
            }
            if (tipoOperacion == ClientesConstants.CONSUMO) {
                file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoConsumo.txt");
            }
            if (tipoOperacion == ClientesConstants.VIVIENDA) {
                file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoVivienda.txt");
            }
            if (tipoOperacion == ClientesConstants.CREDIHOGAR) {
                file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoConsumo.txt");
            }
            if (tipoOperacion == ClientesConstants.SELL_FINANCE) {
                file = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "ContratoDescuento.txt");
            }
            myLogger.debug("file " + file);
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            NumberFormat NF = NumberFormat.getInstance();
            NF.setMaximumFractionDigits(2);
            String edoCivil = "";
            String nombre = cliente.aPaterno + " " + cliente.aMaterno + " " + cliente.nombre;
            String domCliente = cliente.direcciones[0].calle + " " + cliente.direcciones[0].numeroExterior + " " + cliente.direcciones[0].numeroInterior + "Col.";
            domCliente += cliente.direcciones[0].colonia + " " + cliente.direcciones[0].estado + " c.p." + cliente.direcciones[0].cp;
            ConvertNumberToString numeroEntero;

            try {
                //Para obtener comisi�n
                int idComision = cliente.solicitudes[indiceSolicitud].decisionComite.comision;
                ComisionVO[] catComisiones = null;
                CatalogoDAO cat = new CatalogoDAO();
                if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO) {
                    catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_MICRO);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                    catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_CONSUMO);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.VIVIENDA) {
                    catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_VIVIENDA);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR) {
                    catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_CREDIHOGAR);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                    catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_SELL_FINANCE);
                }
                for (int i = 0; i < catComisiones.length; i++) {
                    if (catComisiones[i].id == idComision) {
                        tasaComision = catComisiones[i].porcentaje;
                    }
                }

                //Para obtener tasa de inter�s
                int idTasa = cliente.solicitudes[indiceSolicitud].decisionComite.tasa;
                TasaInteresVO[] tasaInt = null;
                if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO) {
                    tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_MICRO);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                    tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_CONSUMO);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.VIVIENDA) {
                    tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_VIVIENDA);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR) {
                    tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_CREDIHOGAR);
                } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                    tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_SELL_FINANCE);
                }
                for (int i = 0; i < tasaInt.length; i++) {
                    if (tasaInt[i].id == idTasa) {
                        tasaInteres = tasaInt[i].valor;
                    }
                }
                if (Convertidor.esFronterizo(cliente.idSucursal)) {
                    tasaInteres = tasaInteres * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
                }
                if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                    if (cliente.solicitudes[indiceSolicitud].tasaCalculada > 0) {
                        tasaInteres = Convertidor.getMontoIva(cliente.solicitudes[indiceSolicitud].tasaCalculada, cliente.idSucursal, 1);
                    }
                }
                //Para obtener la sucursal
                sucursal = sucursalDAO.getSucursal(cliente.idSucursal);
                myLogger.debug("sucursal " + sucursal.toString());
            } catch (Exception e) {
                myLogger.error("makeContract", e);
            }
            String mtoLetrasEntero = "";
            String mtoDecimal = "";
            int decimal = 0;
            String tasaEntero = "";
            String tasaDecimal = "";
            String cuotaEntero = "";
            String cuotaDecimal = "";

            if (cliente.estadoCivil == 1) {
                edoCivil = "CASADO";
            }
            if (cliente.estadoCivil == 2) {
                edoCivil = "SOLTERO";
            }
            if (cliente.estadoCivil == 3) {
                edoCivil = "UNION LIBRE";
            }
            if (cliente.estadoCivil == 4) {
                edoCivil = "DIVORCIADO";
            }
            if (cliente.estadoCivil == 5) {
                edoCivil = "VIUDO";
            }

            SolicitudVO solicitud = new SolicitudVO();
            solicitud = cliente.solicitudes[indiceSolicitud];
            Double montoAutorizado = 0.00;
            Double cuota = 0.00;
            //Se hace la correccion del campo monto Autorizado (MontoaPagar * NoPagos)
            //montoAutorizado = solicitud.amortizacion[0].saldoInicial;
            montoAutorizado = solicitud.amortizacion[1].montoPagar * solicitud.decisionComite.plazoAutorizado;
            cuota = solicitud.amortizacion[1].montoPagar;
            myLogger.debug("monto autorizado " + montoAutorizado);

            String monto = String.valueOf(HTMLHelper.formatoMonto(montoAutorizado));
            myLogger.debug("monto " + monto);

            decimal = monto.indexOf(".");
            Integer entero = Integer.parseInt(monto.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(entero);
            //Conversi�n del monto a letras
            mtoLetrasEntero = numeroEntero.convertirLetras(entero);
            mtoDecimal = monto.substring(decimal + 1);

            //Conversi�n de la tasa a letras
            String tasaString = String.valueOf(HTMLHelper.formatoMonto(tasaInteres));
            decimal = tasaString.indexOf(".");
            Integer enteroTasa = Integer.parseInt(tasaString.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(enteroTasa);
            tasaEntero = numeroEntero.convertirLetras(enteroTasa);
            tasaDecimal = tasaString.substring(decimal + 1);

            //Conversi�n de la cuota a letras
            String cuotaString = String.valueOf(HTMLHelper.formatoMonto(cuota));
            decimal = cuotaString.indexOf(".");
            Integer enteroCuota = Integer.parseInt(cuotaString.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(enteroCuota);
            cuotaEntero = numeroEntero.convertirLetras(enteroCuota);
            cuotaDecimal = cuotaString.substring(decimal + 1);

            String fechaFirma = HTMLHelper.displayField(solicitud.amortizacion[0].fechaPago);

            //Monto del pagar�
            Double temp = 0.0;
//       		for(int i=1;i<(solicitud.amortizacion.length);i++)
//       			temp+=solicitud.amortizacion[i].montoPagar;
            temp = solicitud.amortizacion[0].saldoInicial;// - solicitud.amortizacion[0].montoPagar;

            String montoPagare = HTMLHelper.formatoMonto(temp);
            decimal = montoPagare.indexOf(".");
            entero = Integer.parseInt(montoPagare.substring(0, decimal));
            numeroEntero = new ConvertNumberToString(entero);
            String enteroPagare = numeroEntero.convertirLetras(entero);
            String decimalPagare = montoPagare.substring(decimal + 1);

            ArrayList<String> fechasPagos = new ArrayList<String>();
            double pagos[] = null;

            try {
                TablaAmortizacionVO[] tablaVO = solicitud.amortizacion;
                pagos = new double[tablaVO.length];

                for (int i = 0; i < tablaVO.length; i++) {
                    fechasPagos.add(Convertidor.dateToString(tablaVO[i].fechaPago));
                    if (i == 0) {
                        pagos[i] = tablaVO[i].montoPagar;
                    } else {
                        pagos[i] = tablaVO[i].abonoCapital + tablaVO[i].interes;
                    }
                }
            } catch (Exception e) {
                myLogger.error("makeContract", e);
            }

            //Calcula CAT e inserta el valor en la solicitud
            String CAT = getCAT(solicitud.amortizacion[0].saldoInicial, pagos, fechasPagos);

            try {
                SolicitudDAO solicitudDAO = new SolicitudDAO();
                solicitud.CAT = CAT;
                solicitudDAO.updateSolicitud(cliente.idCliente, solicitud);
            } catch (Exception e) {
                myLogger.error("makeContract", e);
            }

            while (dis.available() != 0) {
                String line = dis.readLine();
                line = line.replace("<CLIENTE>", nombre);
                line = line.replace("<EDOCIVIL>", edoCivil);
                line = line.replace("<DOMICILIOCLIENTE>", domCliente);
                line = line.replace("<MONTOCREDITO>", monto);
                line = line.replace("<MONTOENTERO>", mtoLetrasEntero.toUpperCase());
                line = line.replace("<MONTODECIMAL>", mtoDecimal);
                line = line.replace("<TASACOMISION>", HTMLHelper.formatoMonto(tasaComision));
                line = line.replace("<TASAINTERES>", HTMLHelper.formatoMonto(tasaInteres));
                line = line.replace("<FECDIAS>", fechaFirma.substring(0, 2));
//			    line = line.replace("<FECMES>", fechaFirma.substring(fechaFirma.indexOf("/")+1, fechaFirma.lastIndexOf("/")));
                line = line.replace("<FECMES>", FechasUtil.obtenNombreMes(fechaFirma).toLowerCase());
                line = line.replace("<FECAÑO>", fechaFirma.substring(fechaFirma.lastIndexOf("/") + 1));
                line = line.replace("<TASAINTERESDOS>", HTMLHelper.formatoMonto(tasaInteres));
                line = line.replace("<CONTRATOLD>", HTMLHelper.displayField(cliente.solicitudes[indiceSolicitud].referencia));
                line = line.replace("<MONTOPAGARE>", montoPagare);
                line = line.replace("<ENTEROPAGARE>", enteroPagare.toUpperCase());
                line = line.replace("<DECIMALPAGARE>", decimalPagare);
                line = line.replace("<CAT>", CAT);
                line = line.replace("<DIRECCION>", sucursal.direccion_calle);
                line = line.replace("<REPRESENTANTE>", sucursal.representante);
                line = line.replace("<CUOTAPAGARE>", cuotaString);
                line = line.replace("<ENTEROCUOTA>", cuotaEntero.toUpperCase());
                line = line.replace("<DECIMALCUOTA>", cuotaDecimal);
                line = line.replace("<ENTEROTASA>", tasaEntero.toUpperCase());
                line = line.replace("<DECIMALTASA>", tasaDecimal);
                int numPagos = solicitud.decisionComite.plazoAutorizado;;
                if (solicitud.decisionComite.frecuenciaPago == 1) {
                    if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE) {
                        numPagos = numPagos * 1;
                    } else {
                        numPagos = numPagos * 2;
                    }
                }
                //Convertir numero de pagos en letras
                String numeroPagosString = String.valueOf(numPagos);
                line = line.replace("<NUMPAGOS>", numeroPagosString);
                numeroEntero = new ConvertNumberToString(numPagos);
                String numPagosLetras = numeroEntero.convertirLetras(numPagos);
                line = line.replace("<NUMPAGOSLETRAS>", numPagosLetras);

                if (line.equals("**ANEXO AMORTIZACIONES**")) {
                    lineaContrato += TablaAmortizacionHelper.makeTable(solicitud.amortizacion);
                } else if (line.equals("**ANEXO PAGOS**")) {
                    lineaContrato += "<center>" + TablaAmortizacionHelper.makePagare(solicitud.amortizacion) + "</center>";
                } else {
                    lineaContrato += "<p align='justify' <font size='2.5' face='Arial'>" + line + "</p>";
                }
            }

            fis.close();
            bis.close();
            dis.close();
        } catch (IOException e) {
            myLogger.error("makeContract", e);
        }

        return lineaContrato;
    }

    public void doPdfContrato(HttpServletResponse response, ClienteVO cliente, int idSolicitud) {

        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        String nombre = cliente.aPaterno + " " + cliente.aMaterno + " " + cliente.nombre;
        String domCliente = cliente.direcciones[0].calle + " " + cliente.direcciones[0].numeroExterior + " " + cliente.direcciones[0].numeroInterior + " Col. "
                + cliente.direcciones[0].colonia + " " + cliente.direcciones[0].estado + " C.P." + cliente.direcciones[0].cp;
        SolicitudVO solicitud = new SolicitudVO();
        solicitud = cliente.solicitudes[indiceSolicitud];
        Double montoAutorizado = solicitud.amortizacion[0].saldoInicial;
        String monto = String.valueOf(HTMLHelper.formatoMonto(montoAutorizado));
//		String fechaFirma = HTMLHelper.displayField(solicitud.amortizacion[0].fechaPago);
        int tipoOperacion = cliente.solicitudes[indiceSolicitud].tipoOperacion;
        String nomObligadoSoli = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].aPaterno + " " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].aMaterno + " " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].nombre;
        String dirObligadoSoli = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.calle + " " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.numeroExterior + " "
                + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.numeroInterior + " Col. " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.colonia + " "
                + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.estado + " C.P. " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.cp;
        String sucursal = cliente.solicitudes[indiceSolicitud].saldo.getNombreSucursal();
        /**
         * Obteniendo la Frecuencia de Pagos
         */
        String frecuencia = "";

        if (solicitud.decisionComite.frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            frecuencia = "semanales";
        }
        if (solicitud.decisionComite.frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            frecuencia = "quincenales";
        }
        if (solicitud.decisionComite.frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
            frecuencia = "mensuales";
        }

        /**
         * Obtieniendo el Estado Civil de la persona.
         */
        String edoCivil = null;
        if (cliente.estadoCivil == 1) {
            edoCivil = "CASADO";
        }
        if (cliente.estadoCivil == 2) {
            edoCivil = "SOLTERO";
        }
        if (cliente.estadoCivil == 3) {
            edoCivil = "UNION LIBRE";
        }
        if (cliente.estadoCivil == 4) {
            edoCivil = "DIVORCIADO";
        }
        if (cliente.estadoCivil == 5) {
            edoCivil = "VIUDO";
        }

        /**
         * Obteniendo el importe con letras
         */
        int decimal = monto.indexOf(".");
        Integer entero = Integer.parseInt(monto.substring(0, decimal));
        ConvertNumberToString numeroEntero = new ConvertNumberToString(entero);
        String mtoLetrasEntero = numeroEntero.convertirLetras(entero);
        String mtoDecimal = monto.substring(decimal + 1);

        /**
         * Obteniendo la tasa de Comisi�n
         */
        Double tasaComision = null;
        Double tasaInteres = null;

        int idComision = cliente.solicitudes[indiceSolicitud].decisionComite.comision;
        ComisionVO[] catComisiones = null;
        CatalogoDAO cat = new CatalogoDAO();
        try {
            if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO) {
                catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_MICRO);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_CONSUMO);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.VIVIENDA) {
                catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_VIVIENDA);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR) {
                catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_CREDIHOGAR);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                catComisiones = cat.getCatalogoComisiones(ClientesConstants.CAT_COMISIONES_SELL_FINANCE);
            }
            for (int i = 0; i < catComisiones.length; i++) {
                if (catComisiones[i].id == idComision) {
                    tasaComision = catComisiones[i].porcentaje;
                }
            }
            /**
             * Obteniendo la Tasa Interes
             */
            int idTasa = cliente.solicitudes[indiceSolicitud].decisionComite.tasa;
            TasaInteresVO[] tasaInt = null;
            if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO) {
                tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_MICRO);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_CONSUMO);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.VIVIENDA) {
                tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_VIVIENDA);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR) {
                tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_CREDIHOGAR);
            } else if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                tasaInt = cat.getCatalogoTasasInteres(ClientesConstants.CAT_TASAS_SELL_FINANCE);
            }
            for (int i = 0; i < tasaInt.length; i++) {
                if (tasaInt[i].id == idTasa) {
                    tasaInteres = tasaInt[i].valor;
                }
            }

            if (Convertidor.esFronterizo(cliente.idSucursal)) {
                tasaInteres = tasaInteres * ((1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO) / (1 + ClientesConstants.TASA_IVA_FRONTERIZO));
            }

            if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.MICROCREDITO || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO) {
                if (solicitud.tasaCalculada > 0) {
                    tasaInteres = Convertidor.getMontoIva(solicitud.tasaCalculada, cliente.idSucursal, 1);
                }
            }
        } catch (Exception e) {
            myLogger.error("doPdfContrato", e);
        }

        /**
         * Obteniendo el Monto del Pagare
         */
        Double temp = 0.0;
        for (int i = 1; i < (solicitud.amortizacion.length); i++) {
            temp += solicitud.amortizacion[i].montoPagar;
        }

        String montoPagare = HTMLHelper.formatoMonto(temp);
        decimal = montoPagare.indexOf(".");
        entero = Integer.parseInt(montoPagare.substring(0, decimal));
        numeroEntero = new ConvertNumberToString(entero);
        String enteroPagare = numeroEntero.convertirLetras(entero);
        String decimalPagare = montoPagare.substring(decimal + 1);

        /**
         * Obteniendo fechaPagos
         */
        ArrayList<String> fechasPagos = new ArrayList<String>();
        double pagos[] = null;
        try {
            TablaAmortizacionVO[] tablaVO = solicitud.amortizacion;
            pagos = new double[tablaVO.length];

            for (int i = 0; i < tablaVO.length; i++) {
                fechasPagos.add(Convertidor.dateToString(tablaVO[i].fechaPago));
                if (i == 0) {
                    pagos[i] = tablaVO[i].montoPagar;
                } else {
                    pagos[i] = tablaVO[i].abonoCapital + tablaVO[i].interes;
                }
            }
        } catch (Exception e) {
            myLogger.error("doPdfContrato", e);
        }
        /**
         * Obteniendo el CAT
         */
        String CAT = getCAT(solicitud.amortizacion[0].saldoInicial, pagos, fechasPagos);

        try {
            SolicitudDAO solicitudDAO = new SolicitudDAO();
            solicitud.CAT = CAT;
            solicitudDAO.updateSolicitud(cliente.idCliente, solicitud);
        } catch (Exception e) {
            myLogger.error("doPdfContrato", e);
        }
        TablaAmortizacionVO[] tabla = solicitud.amortizacion;
        //String referencia = ClientesUtil.makeReferencia(cliente, idSolicitud);
        String referencia = cliente.solicitudes[indiceSolicitud].referencia;
        if (referencia == null) {
            referencia = "";
        }

        /**
         * Generando el Stream con el PDF
         */
        try {

            double mtoComision = Convertidor.getMontoIva(new Double(100), cliente.idSucursal, 1);
            String montoComisionS = String.valueOf(HTMLHelper.formatoMonto(mtoComision));
            decimal = montoComisionS.indexOf(".");
            entero = Integer.parseInt(montoComisionS.substring(0, decimal));
            String mtoLetrasComision = numeroEntero.convertirLetras(entero);
            String mtoDecimalComision = montoComisionS.substring(decimal + 1) + "/100 MN";

            PdfReader reader = new PdfReader(getNombrePlantilla(cliente.solicitudes[indiceSolicitud], tabla.length, ClientesConstants.DOC_FORMAL_CONTRATO));

            PdfStamper reciboPDF = new PdfStamper(reader, response.getOutputStream());
            AcroFields reciboForma = reciboPDF.getAcroFields();

            /**
             * Variables Comunes
             */
            reciboForma.setField("nombreCompleto", nombre);
            reciboForma.setField("nombreCompleto1", nombre);
            reciboForma.setField("edoCivil", edoCivil);
            reciboForma.setField("domicilio", domCliente);
            reciboForma.setField("importeN", FormatUtil.formatDobleMiles(montoAutorizado));
            reciboForma.setField("importeL", (mtoLetrasEntero + " pesos " + mtoDecimal + "/100 M.N.").toUpperCase());
            reciboForma.setField("noPagos", tabla.length - 1 + "");
            reciboForma.setField("frecuencia", frecuencia);
            reciboForma.setField("tasaInteres", HTMLHelper.formatoMonto(tasaInteres));
            reciboForma.setField("tasaComision", HTMLHelper.formatoMonto(Convertidor.getMontoIva(tasaComision, cliente.idSucursal, 1)));
            reciboForma.setField("cat", CAT);
            reciboForma.setField("cat2", CAT);
            reciboForma.setField("comisionCobN", montoComisionS);
            reciboForma.setField("comisionCobL", mtoLetrasComision);
            reciboForma.setField("centMntoComision", mtoDecimalComision);
            reciboForma.setField("interesMoratorio", HTMLHelper.formatoMonto(tasaInteres * 2));
            reciboForma.setField("diaContrato", FechasUtil.obtenParteFecha(tabla[0].fechaPago, 1));
            reciboForma.setField("mesContrato", FechasUtil.obtenParteFecha(tabla[0].fechaPago, 2));
            reciboForma.setField("mesNombre", FechasUtil.obtenNombreMes(Convertidor.dateToString(tabla[0].fechaPago)));
            reciboForma.setField("anioContrato", FechasUtil.obtenParteFecha(tabla[0].fechaPago, 3));
            reciboForma.setField("fechaContrato", Convertidor.dateToString(tabla[0].fechaPago));
            reciboForma.setField("afirme", referencia);
            reciboForma.setField("banorte", referencia);
            reciboForma.setField("hsbc", referencia);
            reciboForma.setField("bancomer", referencia);
            reciboForma.setField("multa", String.valueOf(cliente.solicitudes[indiceSolicitud].decisionComite.multa));
            reciboForma.setField("nombreObligado", nomObligadoSoli);
            reciboForma.setField("direccionObligado", dirObligadoSoli);
            reciboForma.setField("ciudad", sucursal);
            /**
             * Variables Contrato Consumo
             */
            if (tipoOperacion == ClientesConstants.CONSUMO || tipoOperacion == ClientesConstants.CREDIHOGAR) {

                double comisiones = (tabla.length > 0 ? (tabla[0].comisionInicial + tabla[0].ivaComision) : 0);
                double montoCredito = (tabla.length > 0 ? tabla[0].saldoInicial : 0);

                reciboForma.setField("TotalAPagar", "$ " + montoPagare);

                montoPagare = HTMLHelper.formatoMonto(montoCredito);
                decimal = montoPagare.indexOf(".");
                entero = Integer.parseInt(montoPagare.substring(0, decimal));
                numeroEntero = new ConvertNumberToString(entero);
                enteroPagare = numeroEntero.convertirLetras(entero);
                decimalPagare = montoPagare.substring(decimal + 1);

                reciboForma.setField("catFront", CAT + " %");
                reciboForma.setField("moraFija", HTMLHelper.formatoMonto(tasaInteres * 2) + " % por mora fija");
                reciboForma.setField("ordinariaFija", HTMLHelper.formatoMonto(tasaInteres) + " % Ordinaria fija");
                reciboForma.setField("montoCredito", "$ " + HTMLHelper.formatoMonto(montoCredito));
                reciboForma.setField("montosClausulas", "Por gestión $" + HTMLHelper.formatoMonto(comisiones) + " cláusula quinta");
                reciboForma.setField("montosClausulas2", "Por cobranza $ 100 cláusula decimo Tercera");
                reciboForma.setField("plazoCredito", "Plazo del Crédito: " + (tabla.length - 1) + " meses");
                reciboForma.setField("plazoCredito", "Plazo del Crédito: " + solicitud.decisionComite.plazoAutorizado + " meses");
                reciboForma.setField("numero", tabla.length - 1 + "");
                reciboForma.setField("montoFijo", (tabla.length > 0 ? tabla[1].montoPagar + "" : ""));
                reciboForma.setField("frecuenciaFront", frecuencia);
                reciboForma.setField("importeN", montoPagare);
                reciboForma.setField("importeL", enteroPagare.toUpperCase());
                reciboForma.setField("importeCent", "pesos " + decimalPagare + "/100 MN");

            }
            /**
             * Variables Contrato DESCUENTO NOMINA
             */
            if (tipoOperacion == ClientesConstants.SELL_FINANCE) {

                double comisiones = (tabla.length > 0 ? (tabla[0].comisionInicial + tabla[0].ivaComision) : 0);
                double montoCredito = (tabla.length > 0 ? tabla[0].saldoInicial : 0);

                reciboForma.setField("TotalAPagar", "$ " + montoPagare);

                montoPagare = HTMLHelper.formatoMonto(montoCredito);
                decimal = montoPagare.indexOf(".");
                entero = Integer.parseInt(montoPagare.substring(0, decimal));
                numeroEntero = new ConvertNumberToString(entero);
                enteroPagare = numeroEntero.convertirLetras(entero);
                decimalPagare = montoPagare.substring(decimal + 1);

                reciboForma.setField("catFront", CAT + " %");
                reciboForma.setField("moraFija", HTMLHelper.formatoMonto(tasaInteres * 2) + " % por mora fija");
                reciboForma.setField("ordinariaFija", HTMLHelper.formatoMonto(tasaInteres) + " % Ordinaria fija");
                reciboForma.setField("montoCredito", "$ " + HTMLHelper.formatoMonto(montoCredito));
                reciboForma.setField("montosClausulas", "Por gestión $" + HTMLHelper.formatoMonto(comisiones) + " cláusula quinta");
                reciboForma.setField("montosClausulas2", "Por cobranza $ 100 cláusula decimo Tercera");
                reciboForma.setField("plazoCredito", "Plazo del Crédito: " + (tabla.length - 1) + " meses");
                reciboForma.setField("plazoCredito", "Plazo del Crédito: " + solicitud.decisionComite.plazoAutorizado + " meses");
                reciboForma.setField("numero", tabla.length - 1 + "");
                reciboForma.setField("montoFijo", (tabla.length > 0 ? tabla[1].montoPagar + "" : ""));
                reciboForma.setField("frecuenciaFront", frecuencia);
                reciboForma.setField("importeN", montoPagare);
                reciboForma.setField("importeL", enteroPagare.toUpperCase());
                reciboForma.setField("importeCent", "pesos " + decimalPagare + "/100 MN");
                reciboForma.setField("fechaNacimiento", Convertidor.dateToString(cliente.fechaNacimiento));
                reciboForma.setField("claveElector", cliente.numeroIdentificacion);
                reciboForma.setField("calle", cliente.direcciones[0].calle);
                reciboForma.setField("cp", cliente.direcciones[0].cp);
            }
            /**
             * Escribiendo Tabla de Amortizaciones
             */
            for (int i = 0; i < tabla.length; i++) {

                reciboForma.setField("pago" + i, HTMLHelper.displayField(tabla[i].numPago, true));
                reciboForma.setField("fecha" + i, HTMLHelper.displayField(tabla[i].fechaPago));
                reciboForma.setField("sInicial" + i, HTMLHelper.formatoMonto(tabla[i].saldoInicial));
                reciboForma.setField("aCapital" + i, HTMLHelper.formatoMonto(tabla[i].abonoCapital));
                reciboForma.setField("sCapital" + i, HTMLHelper.formatoMonto(tabla[i].saldoCapital));
                reciboForma.setField("cInicial" + i, HTMLHelper.formatoMonto(tabla[i].comisionInicial));
                reciboForma.setField("ivaComision" + i, HTMLHelper.formatoMonto(tabla[i].ivaComision));
                reciboForma.setField("interes" + i, HTMLHelper.formatoMonto(tabla[i].interes));
                reciboForma.setField("ivaInteres" + i, HTMLHelper.formatoMonto(tabla[i].ivaInteres));
                reciboForma.setField("monto" + i, HTMLHelper.formatoMonto(tabla[i].montoPagar));

            }

            /**
             * ******************************************************
             * Finalizando Escritura de Tabla de Amortizaciones
             * ******************************************************
             */
            reciboPDF.setFormFlattening(true);
            reciboPDF.close();

        } catch (Exception e) {
            myLogger.error("doPdfContrato", e);
        }
    }

    public void doContratoComunal(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            Paragraph p = new Paragraph();
            Color blueFooterColor = new Color(0xB2FCE5);
            String texto = "13317-439-012789/04-05715-1017";
            Chunk ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            ck.setBackground(blueFooterColor, 210, 2, 210, 3);
            p.add(ck);
            p.setLeading(15);
            HeaderFooter footer = new HeaderFooter(p, false);
            footer.setBorderColor(Color.white);
            footer.setAlignment(Chunk.ALIGN_CENTER);
            document.setFooter(footer);
            document.open();
            contenidoDocContratoComunal(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doContratoComunal", exception);
        }
    }

    public void doCaratulaComunal(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            document.setMargins(40, 40, 40, 40);
            Paragraph p = new Paragraph();
            Paragraph phd = new Paragraph();
            Color blueFooterColor = new Color(0xB2FCE5);
            String texto = "13317-439-012789/04-05715-1017\n";
            Chunk ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            ck.setBackground(blueFooterColor, 210, 2, 210, 3);
            p.add(ck);
            p.setLeading(15);
            p.setAlignment(Chunk.ALIGN_RIGHT);
            HeaderFooter footer = new HeaderFooter(p, false);
            footer.setBorderColor(Color.white);
            footer.setAlignment(Chunk.ALIGN_CENTER);
            document.setFooter(footer);
            //Se adjunta la fecha como Encabezado 
            texto = dateFormat.format(date);
            Chunk ckhd = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            phd.add(ckhd);
            HeaderFooter header = new HeaderFooter(phd, false);
            header.setBorderColor(Color.white);
            header.setAlignment(Chunk.ALIGN_RIGHT);
            document.setHeader(header);
            document.open();
            contenidoDocCaratula(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doCaratulaComunal", exception);
        }
    }

    public void doOrdenPagoComunal(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDisp) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoOrdenPagoComunal(grupo, ciclo, response, semDisp);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doOrdenPagoPDF", exception);
        }
    }

    private void contenidoOrdenPagoComunal(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response, int semDisp) {
                try {
            Paragraph p = null;
            Paragraph pSegunda = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            Color greenColor = new Color(0x03520D);
            Color greenLetterColor = new Color(0x418248);
            Color redLetterColor = new Color(0x6F0917);
            String texto = "";
            String fecha = null;//Convertidor.timestampToString(ciclo.integrantes[i].ordenPago.getFechaEnvio());
            String cve_Banorte = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
            String cve_Banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
            String cve_Scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
            String cve_Bancomer = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
            String cve_Santander = CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER");
            OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            int numTarjetas = 0, j = 0;
            int tamanoLetra = 12;

            for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                boolean imprime = false;
                //ciclo.integrantes[i].tarjetaCobro = tarjetaDAO.getTarjetaClinete(ciclo.integrantes[i].idCliente, temporal.idSolicitud);
                if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                    tamanoLetra = 8;
                }
                if (ciclo.integrantes[i].ordenPago != null && ciclo.integrantes[i].medioDisp == 0) {
                    
                    if (semDisp == 0 && ciclo.integrantes[i].tipo <= 5) {
                        imprime = true;
                    } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_2 && ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                        imprime = true;
                    } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                        imprime = true;
                    }
                    if(AdicionalUtil.isOrdenPagoAdicional(ciclo.integrantes[i].ordenPago)){
                        imprime = false;
                    }
                    if (imprime) {
                        p = new Paragraph();
                        texto = "ORDEN DE PAGO";
                        ck = new Chunk(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
                        p.add(ck);
                        texto = "________________\n\n";
                        ck = new Chunk(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.add(ck);
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        pSegunda = p;

                        int col[] = {38, 62};
                        tabla = new PdfPTable(2);
                        tabla.setWidthPercentage(100F);
                        tabla.setWidths(col);
                        texto = "FECHA:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        fecha = Convertidor.timestampToString(ciclo.integrantes[i].ordenPago.getFechaEnvio());
                        texto = fecha;
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "BENEFICIARIO:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        texto = ciclo.integrantes[i].ordenPago.getNombre();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "REFERENCIA:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                            texto = cve_Banorte + ciclo.integrantes[i].ordenPago.getReferencia();
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                            texto = cve_Banamex + ciclo.integrantes[i].ordenPago.getReferencia();
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                            texto = cve_Scotia + ciclo.integrantes[i].ordenPago.getReferencia();
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = cve_Bancomer + ciclo.integrantes[i].ordenPago.getReferencia();
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_TELECOM) {
                            texto = ordenDAO.getReferenciaTelecom(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            texto = cve_Santander.substring(4, 11) + ciclo.integrantes[i].ordenPago.getReferencia();
                        }
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "IMPORTE:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        texto = "" + ciclo.integrantes[i].ordenPago.getMonto();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        
                        texto = "BANCO:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                            texto = "BANORTE";
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                            texto = "BANAMEX No Emisor 48";
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                            texto = "SCOTIABANK No Empresa " + cve_Scotia;
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = "BBVA-BANCOMER Convenio 0" + cve_Bancomer;
                            texto += "\nConcepto ODP" + ciclo.integrantes[i].ordenPago.getIdCliente() + " Clave de Identificacion IFE";
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_TELECOM) {
                            texto = "TELECOM Documento de Identificacion IFE";
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            texto = "SANTANDER";
                        }
                        if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                        } else if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.BOLD, redLetterColor));
                        } else {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, redLetterColor));
                        }
                        if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            texto = "CONTRATO:";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            cell.setBorderColor(Color.WHITE);
                            texto = CatalogoHelper.getParametro("NUM_CONTRATO_SANTANDER");;
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));

                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            texto = "Clave beneficiario:";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            cell.setBorderColor(Color.WHITE);
                            texto = StringUtils.leftPad(ciclo.integrantes[i].ordenPago.getIdCliente() + "", 7, "0") + StringUtils.leftPad(ciclo.integrantes[i].ordenPago.getIdSolicitud() + "", 2, "0");
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));

                        }

                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        document.add(tabla);

                        texto = "________________\n\n";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);

                        if (ciclo.integrantes[i].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = "____________________________________________________________________\n\n";
                        } else {
                            texto = "\n\n____________________________________________________________________\n\n";
                        }
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);

                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        document.add(pSegunda);
                        document.add(tabla);
                        texto = "________________\n";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        if (i % 2 == 0) {
                            texto = "\n_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n\n";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, Color.GRAY));
                            p.setAlignment(Chunk.ALIGN_CENTER);
                            p.setLeading(12);
                            document.add(p);
                        } else {
                            document.newPage();
                        }
                    }
                } else {
                    numTarjetas++;
                }
            }
            if (numTarjetas > 0) {
                int colList[] = {8, 50, 20, 22};
                tabla = new PdfPTable(4);
                tabla.setWidthPercentage(100);
                tabla.setWidths(colList);
                texto = "TARJETAS DE PAGO";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre de la Sucursal :        " + sucursal.nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre del Equipo :            " + grupo.nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Banco :   ";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Fecha :    " + Convertidor.dateToString(new java.util.Date());
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "No.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre del Cliente";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Numero de Tarjeta";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Firma";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                    if (ciclo.integrantes[i].medioDisp == 1) {
                        texto = "" + (++j);
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        texto = "" + ciclo.integrantes[i].nombre;
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        texto = "" + ciclo.integrantes[i].tarjetaCobro.getTarjeta();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setPadding(12);
                        tabla.addCell(cell);
                    }
                }
                document.add(tabla);
            }
        } catch (Exception e) {
            myLogger.error("contenidoOrdenPagoComunal", e);
        }

 
    }

    public void doOrdenPagoSeguros(HttpServletResponse response, int idCliente, int idSolicitud) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoOrdenPagoSeguros(idCliente, idSolicitud, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doOrdenPagoSeguros", exception);
        }
    }

    private void contenidoOrdenPagoSeguros(int idCliente, int idSolicitud, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Paragraph pSegunda = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            Color greenColor = new Color(0x03520D);
            Color greenLetterColor = new Color(0x418248);
            Color redLetterColor = new Color(0x6F0917);
            String texto = "";
            String fecha = null;
            String cve_Banorte = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
            String cve_Banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
            String cve_Scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
            String cve_Bancomer = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
            ArrayList<OrdenDePagoVO> ordenesSeguro = new ArrayList<OrdenDePagoVO>();
            OrdenDePagoDAO ordenPagoDAO = new OrdenDePagoDAO();
            int sizeODP = ordenPagoDAO.getSizeODPSeguro(idCliente, idSolicitud);
            ordenesSeguro = ordenPagoDAO.getOrdenPagoSeguro(idCliente, idSolicitud, sizeODP);
            for (int i = 0; ordenesSeguro != null && i < ordenesSeguro.size(); i++) {
                //if (ciclo.integrantes[i].ordenPago != null) {
                p = new Paragraph();
                texto = "ORDEN DE PAGO";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
                p.add(ck);
                texto = "________________\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
                pSegunda = p;

                int col[] = {38, 62};
                tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100F);
                tabla.setWidths(col);
                texto = "FECHA:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                fecha = Convertidor.timestampToString(ordenesSeguro.get(i).getFechaEnvio());
                texto = fecha;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "BENEFICIARIO:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                texto = ordenesSeguro.get(i).getNombre();
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "REFERENCIA:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                    texto = cve_Banorte + ordenesSeguro.get(i).getReferencia();
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                    texto = cve_Banamex + ordenesSeguro.get(i).getReferencia();
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                    texto = cve_Scotia + ordenesSeguro.get(i).getReferencia();
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = cve_Bancomer + ordenesSeguro.get(i).getReferencia();
                }
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "IMPORTE:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                texto = "" + ordenesSeguro.get(i).getMonto();
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "BANCO:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                    texto = "BANORTE";
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                    texto = "BANAMEX No Emisor 48";
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                    texto = "SCOTIABANK No Empresa " + cve_Scotia;
                } else if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = "BBVA-BANCOMER Convenio 0" + cve_Bancomer;
                    texto += "\nConcepto ODP" + ordenesSeguro.get(i).getIdCliente() + " Clave de Identificacion IFE";
                }
                if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                } else {
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, redLetterColor));
                }
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                document.add(tabla);

                texto = "________________\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                if (ordenesSeguro.get(i).getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = "____________________________________________________________________\n\n";
                } else {
                    texto = "\n\n____________________________________________________________________\n\n";
                }
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                texto = "";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
                document.add(pSegunda);
                document.add(tabla);
                texto = "________________\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
                if (i % 2 == 0) {
                    texto = "\n_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, Color.GRAY));
                    p.setAlignment(Chunk.ALIGN_CENTER);
                    p.setLeading(12);
                    document.add(p);
                } else {
                    document.newPage();
                }

            }
        } catch (Exception e) {
            myLogger.error("contenidoOrdenPagoSeguros", e);
        }
    }

    public void doOrdenPagoReferencia(HttpServletResponse response, String referencia) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoOrdenPagoReferencia(referencia, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doOrdenPagoReferencia", exception);
        }
    }

    private void contenidoOrdenPagoReferencia(String referencia, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Paragraph pSegunda = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            Color greenColor = new Color(0x03520D);
            Color greenLetterColor = new Color(0x418248);
            Color redLetterColor = new Color(0x6F0917);
            String texto = "";
            String fecha = null;
            String cve_Banorte = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
            String cve_Banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
            String cve_Scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
            String cve_Bancomer = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
            String cve_Santander = CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER");
            OrdenDePagoDAO ordenPagoDAO = new OrdenDePagoDAO();
            OrdenDePagoVO ordenPago = new OrdenDePagoVO();
            ordenPago = ordenPagoDAO.getOrdenDePagoReferencia(referencia);
            if (ordenPago != null) {
                //if (ciclo.integrantes[i].ordenPago != null) {
                p = new Paragraph();
                texto = "ORDEN DE PAGO";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
                p.add(ck);
                texto = "________________\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
                pSegunda = p;

                int col[] = {38, 62};
                tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100F);
                tabla.setWidths(col);
                texto = "FECHA:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                fecha = Convertidor.timestampToString(ordenPago.getFechaEnvio());
                texto = fecha;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "BENEFICIARIO:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                texto = ordenPago.getNombre();
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "REFERENCIA:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                    texto = cve_Banorte + ordenPago.getReferencia();
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                    texto = cve_Banamex + ordenPago.getReferencia();
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                    texto = cve_Scotia + ordenPago.getReferencia();
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = cve_Bancomer + ordenPago.getReferencia();
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                    texto = cve_Santander.substring(4, 11) + ordenPago.getReferencia();
                }
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "IMPORTE:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                texto = "" + ordenPago.getMonto();
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                texto = "BANCO:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                cell.setBorderColor(Color.WHITE);
                if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                    texto = "BANORTE";
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                    texto = "BANAMEX No Emisor 48";
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                    texto = "SCOTIABANK No Empresa " + cve_Scotia;
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = "BBVA-BANCOMER Convenio 0" + cve_Bancomer;
                    texto += "\nConcepto ODP" + ordenPago.getIdCliente() + " Clave de Identificacion IFE";
                } else if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                    texto = "SANTANDER ";
                }
                if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                } else {
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, redLetterColor));
                }
                if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(20);
                    cell.setBorderColor(Color.WHITE);
                    tabla.addCell(cell);
                    texto = "CONTRATO:";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(20);
                    cell.setBorderColor(Color.WHITE);
                    tabla.addCell(cell);
                    cell.setBorderColor(Color.WHITE);
                    texto = CatalogoHelper.getParametro("NUM_CONTRATO_SANTANDER");;
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));

                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(20);
                    cell.setBorderColor(Color.WHITE);
                    tabla.addCell(cell);
                    texto = "Clave beneficiario:";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(20);
                    cell.setBorderColor(Color.WHITE);
                    tabla.addCell(cell);
                    cell.setBorderColor(Color.WHITE);
                    texto = StringUtils.leftPad(ordenPago.getIdCliente() + "", 7, "0") + StringUtils.leftPad(ordenPago.getIdSolicitud() + "", 2, "0");
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));

                }
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(20);
                cell.setBorderColor(Color.WHITE);
                tabla.addCell(cell);
                document.add(tabla);

                texto = "________________\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                if (ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                    texto = "____________________________________________________________________\n\n";
                } else {
                    texto = "\n\n____________________________________________________________________\n\n";
                }
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                texto = "";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
                document.add(pSegunda);
                document.add(tabla);
                texto = "________________\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                texto = "\n_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, Color.GRAY));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);
            }
        } catch (Exception e) {
            myLogger.error("contenidoOrdenPagoReferencia", e);
        }
    }

    private void contenidoDocContratoComunal(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null, tabla2 = null, tabla3 = null;
            PdfPTable tablaInterna = null;
            PdfPCell cell = null;
            String texto = "";
            Color blueColor = new Color(0x00A6FF);
            Color greenHeaderColor = new Color(0x5D8C41);
            Color greenCellColor = new Color(0xB0CF9F);
            Color grayColor = new Color(0xB0AFAF);
            Color grayLightColor = new Color(0xEAE9E9);
            Date fechaFirma = ciclo.tablaAmortizacion[0].fechaPago;
            //System.out.println("valorfechafirma"+fechaFirma);
            String cat = "";
            String mes = FechasUtil.obtenNombreMes(Convertidor.dateToString(fechaFirma));
            ArrayList<String> fechasPagos = new ArrayList<String>();
            String garantia = new CatalogoDAO().getGarantia(ciclo.garantia);
            String mesadirectiva = "";
            String presidente = "";
            String secretario = "";
            String tesorero = "";
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            double montoConIntereses = 0.00;
            for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
                montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
            }
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                switch (ciclo.integrantes[i].rol) {
                    case 3:
                        presidente = ciclo.integrantes[i].nombre;
                        break;
                    case 2:
                        secretario = ciclo.integrantes[i].nombre;
                        break;
                    case 1:
                        tesorero = ciclo.integrantes[i].nombre;
                        break;
                }
            }
            mesadirectiva = presidente + ", " + secretario + " Y " + tesorero;
            //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
            String montoIni = String.valueOf(montoConIntereses);
            int posicion = montoIni.indexOf(".");
            Integer entero = Integer.parseInt(montoIni.substring(0, posicion));
            ConvertNumberToString montoLetra = new ConvertNumberToString(entero);
            String mtoLetrasEntero = montoLetra.convertirLetras(entero);
            String mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");
            TasaInteresVO tasa = null;
            try {
                //Para obtener tasa
                TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);

                if (catTasas != null) {
                    tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
                    //tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                myLogger.error("contenidoDocContratoComunal", e);
            }
            double pagos[] = null;
            int plazo = 0;
            try {
                TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
                pagos = new double[tablaVO.length];
                plazo = tablaVO.length - 1;
                for (int i = 0; i < tablaVO.length; i++) {
                    fechasPagos.add(Convertidor.dateToString(tablaVO[i].fechaPago));
                    if (i == 0) {
                        pagos[i] = tablaVO[i].montoPagar;
                    } else {
                        pagos[i] = tablaVO[i].abonoCapital + tablaVO[i].interes;
                    }
                }
            } catch (Exception e) {
                myLogger.error("contenidoDocContratoComunal", e);
            }

            if (ciclo.tablaAmortizacion != null && ciclo.tablaAmortizacion[0].saldoInicial > 0) {
                cat = getCAT(ciclo.tablaAmortizacion[0].saldoInicial, pagos, fechasPagos);
            }
            
            //JECB 24/10/2017
            //Sumatoria de importe del seguro financiado
            double sumatoriaSegContratado = SegurosUtil.sumatoriaSegurosXGrupo(ciclo.integrantes);
            myLogger.debug("Costo seguro contratado:"+sumatoriaSegContratado);

            String domGrupo = GrupoHelper.getDomicilio(ciclo);
            ConvertNumberToString plazoToLetra = new ConvertNumberToString(ciclo.plazo);
            String plazoLetra = plazoToLetra.convertirLetras(ciclo.plazo);
            myLogger.debug("mtoLetras, mto Decimal" + mtoLetrasEntero + " " + mtoDecimal);
            p = new Paragraph();
            texto = "CONTRATO DE CRÉDITO QUE CELEBRAN POR UNA PARTE CEGE CAPITAL, S.A.P.I DE C.V., SOFOM, E.N.R., EN SU CARACTER DE ACREDITANTE, REPRESENTADA EN ESTE ACTO POR ";
            texto += "________________________________________________, (EN LO SUCESIVO \"CONTIGO\") Y POR LA OTRA PARTE, EN SU CARACTER DE ACREDITADAS, LAS PERSONAS QUE SE UBICAN EN EL LISTADO ESTABLECIDO EN LA PARTE FINAL DEL PRESENTE CONTRATO, QUIENES DE FORMA CONJUNTA SE DENOMINARÁN ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "" + grupo.nombre;
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " (EN LO SUCESIVO \"EL GRUPO\"),";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            /*texto = "" + mesadirectiva;
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);*/
            texto = " DE CONFORMIDAD CON LOS ANTECEDENTES, DECLARACIONES, Y CLÁUSULAS SIGUIENTES:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "ANTECEDENTES\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "PRIMERO. El día ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + FechasUtil.obtenParteFecha(fechaFirma, 1);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " de ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " de ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + FechasUtil.obtenParteFecha(fechaFirma, 3);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ", el GRUPO, formuló, firmó y presento una solicitud de crédito simple con obligación solidaria (en adelante, la \"Solicitud\"), a favor de  CONTIGO  por la cantidad de $";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " +HTMLHelper.formatoMonto(ciclo.monto + sumatoriaSegContratado );
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " pesos M.N.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\n\nSEGUNDO. Derivado de dicha solicitud, EL GRUPO entregó los datos y documentos requeridos para la valoración y análisis de la procedencia de la solicitud.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\n\nTERCERO. CONTIGO, previa revisión y análisis de la Solicitud, información y documentación proporcionada y presentada por el GRUPO, autorizó el otorgamiento del crédito simple con obligación solidaria, a favor del GRUPO, bajo las declaraciones y cláusulas siguientes:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "DECLARACIONES\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            texto = "I. DECLARA CONTIGO, POR CONDUCTO DE SU REPRESENTANTE LEGAL:\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            p = new Paragraph();
            texto = "a) Que es una sociedad mercantil legalmente constituida de conformidad con las leyes de México, según consta en la escritura número 11,731 de fecha 7 de marzo de 2013, otorgada ante la fe del Licenciado Guillermo Escamilla Narváez, titular de la notaría pública número 243 de la Ciudad de México, instrumento cuyo primer testimonio ha quedado inscrito en el Registro Público de Comercio de la Ciudad de México, bajo el folio mercantil número 492864-1, con fecha 2 de mayo de 2013 .\n\n";
            texto += "b) Que cuenta con un registro vigente ante la Comisión Nacional Para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF).\n\n";
            texto += "c) Que en términos del artículo 87-J de la Ley General de Organizaciones y Actividades Auxiliares de Crédito, manifiesta que para su constitución y operación con carácter de sociedad financiera de objeto múltiple, entidad no regulada no requiere de autorización de la Secretaría de Hacienda y Crédito Público, y que se encuentra sujeta a la supervisión de la de la Comisión Nacional Bancaria y de Valores únicamente para efectos de lo dispuesto por el Artículo 56 del Ordenamiento Legal en cita.\n\n";
            texto += "d) Que su representante cuenta con las facultades necesarias para la celebración del presente Contrato, mismas que no le han sido revocadas, modificadas o restringidas en forma alguna a la fecha de celebración del presente Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            texto = "e) Que el presente Contrato se encuentra registrado como un Contrato de Adhesión en el Registro de Contratos de Adhesión (RECA) de la CONDUSEF bajo el número: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "13317-439-012789/04-05715-1017.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            p.setAlignment(Chunk.ALIGN_LEFT);
            texto = "f) Qué el Costo Anual Total (CAT) del presente crédito es el que se establece en la Carátula del Crédito y se tiene por aquí reproducido como si se insertase a la letra. El CAT se encuentra calculado a la fecha de firma del presente Contrato en términos anuales.\n\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            texto = "II. DECLARAN CONJUNTAMENTE TODAS LAS INTEGRANTES DEL GRUPO POR SU PROPIO DERECHO:\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            p = new Paragraph();
            texto = "a) Que son un GRUPO de personas físicas de nacionalidad mexicana con capacidad de ejercicio, económica y moral suficiente para asumir las obligaciones materia de este Contrato y cumplirlas en los términos que más adelante se precisan, manifestando bajo protesta de decir verdad que no están imposibilitadas legalmente para celebrar el mismo.\n\n";
            texto += "b) Que libremente han constituido un grupo de personas  que desempeñan una actividad productiva por cuenta propia y que el Crédito solicitado será destinado para el desarrollo de actividades productivas lícitas, con base en la solicitud de crédito simple con obligación solidaria presentada a CONTIGO, y que han convenido en denominarse ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "" + grupo.nombre;
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " (en lo sucesivo el \"GRUPO\").\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "c) Que los recursos con los cuales han de pagar el crédito recibido, así como las obligaciones contraídas, han sido o serán obtenidos o generados a través de una fuente de origen lícito. Así mismo que el destino de los recursos obtenidos al amparo del presente Contrato de Crédito será tan solo para fines permitidos por la ley, y que no se encuentran dentro de los supuestos establecidos en los artículos 139 Quater y 400 bis del Código Penal Federal y sus correlativos en los Códigos Penales de los Estados de la República Mexicana.\n\n";
            texto += "d) Que con anterioridad a la firma del presente Contrato, cada una de las integrantes del GRUPO han suscrito el formato de autorización para solicitar Reportes de Crédito de Personas Físicas, ante las Sociedades de Información Crediticia que estime conveniente CONTIGO.\n\n";
            texto += "e) Que con anterioridad a la fecha de firma del presente Contrato, CONTIGO, les ha informado y explicado a cada una de las integrantes del GRUPO, el contenido de cada una de las cláusulas que lo integran, tales como el monto de los pago parciales, la forma y periodicidad para liquidarlos, cargas financieras, accesorios, el derecho que tienen a liquidar anticipadamente la operación y las condiciones para ello, los intereses ordinarios y moratorios, en su caso, la forma de calcular los mismos, gastos de cobranza y/o comisiones.\n\n";
            texto += "f) Que el GRUPO está de acuerdo que al firmar de forma conjunta 3 (tres) de las personas que conforman el mismo (en adelante las \"PERSONAS AUTORIZADAS\"), dichas \"PERSONAS AUTORIZADAS\" tendrán la representación del GRUPO para atender y responder por los derechos y obligaciones derivadas del presente contrato con independencia de la responsabilidad solidaria de todas las integrantes del GRUPO.\n\n";
            texto += "g) Que para efectos informativos, se les dio a conocer sobre el Costo Anual Total (\"CAT\") del Crédito que se contrata en términos del presente contrato.\n\n";
            texto += "h) Que al momento de la celebración del presente Contrato, la cantidad que asciende al 10% (diez por ciento) del monto del crédito objeto de este contrato ha sido depositada por el GRUPO en la cuenta bancaria designada por CONTIGO, lo anterior con el objeto de que se aplique como pago adelantado ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "a la última amortización ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "del crédito otorgado a favor del GRUPO por medio del presente Contrato o en créditos subsecuentes que le otorgue CONTIGO a las integrantes del GRUPO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "i) Que con anterioridad a la celebración del presente, CONTIGO les explicó el tratamiento que le dará a sus datos personales mediante la entrega de un Aviso de Privacidad, en términos de la Ley Federal de Protección de Datos Personales en Posesión de los Particulares, donde se señala, además del tratamiento que se le darán a sus datos personales, los derechos de acceso, rectificación, cancelación u oposición con los que cuenta y la forma cómo los puede hacer valer.\n\n";
            texto += "j) Que conocen que el Crédito podrá ser otorgado con el apoyo de NACIONAL FINANCIERA, SOCIEDAD NACIONAL DE CRÉDITO, INSTITUCIÓN DE BANCA DE DESARROLLO, exclusivamente para fines de desarrollo social o del FONDO DE MICROFINANCIAMIENTO A MUJERES RURALES.\n\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            texto = "III. DECLARAN LAS PERSONAS AUTORIZADAS POR EL GRUPO:\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            texto = "a) Que fueron seleccionadas por los integrantes del GRUPO, y que de forma voluntaria aceptaron la representación del mismo, por lo que cuentan con plena capacidad legal para obligarse en los términos del presente Contrato.\n\n";
            texto += "b) Que recibirán las notificaciones que les dirija CONTIGO y se obligan a informar el contenido de las mismas a la totalidad de las integrantes del GRUPO a fin de propiciar el buen desempeño de las obligaciones establecidas en el presente contrato.\n\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            texto = "IV. DECLARAN TODAS LAS PARTES:\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            p = new Paragraph();
            texto = "a) Reconocerse la capacidad jurídica con las que comparecen para celebrar el presente Contrato.\n\n";
            texto += "b) Para el supuesto de intervenir de manera conjunta en diversa estipulación del Contrato, se les denominará como las \"PARTES\".\n\n";
            texto += "c) El Contrato lo celebran: (i) de mutuo acuerdo; (ii) sin que exista algún vicio de la voluntad; y, (iii) bajo el amparo de las estipulaciones al efecto establecidas.\n\n";
            texto += "d) La Solicitud, documentación y demás información que el GRUPO proporcionó a CONTIGO, forman parte del proceso para la originación, análisis y otorgamiento del Crédito materia del presente Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "Conformes las PARTES con las declaraciones que anteceden, es su voluntad contraer las obligaciones que se derivan del presente instrumento, al tenor de las siguientes: \n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            texto = "CLÁUSULAS\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "PRIMERA.- ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "DEFINICIONES.- ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = "Para efectos del presente Contrato, las PARTES de mutuo acuerdo, reconocen el significado de los siguientes términos,  independientemente de su utilización en singular o en plural:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"CAT\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Es el Costo Anual Total de financiamiento expresado en términos porcentuales anuales que, para fines informativos y de comparación, incorpora la totalidad de los costos y gastos inherentes al Crédito materia del presente Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Carátula\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el documento integrante del Contrato en el que se establecen de forma genérica la Información del Crédito, así como la información que determina el Contrato y que le es aplicable.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Comisión por Pago Tardío\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa una cantidad fija indicada en la Carátula cuyo importe deberá pagarse por el GRUPO a CONTIGO, en forma inmediata junto con el importe de la Parcialidad pendiente de pago que ocasiono dicha comisión.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);            
            texto = "\"Día Hábil\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa cualquier día de la semana, excluyendo los días sábado y domingo, así como los días en que, conforme a las disposiciones aplicables, las instituciones Financieras deban cerrar sus puertas, suspender operaciones y la prestación de servicios al público.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Fecha de Pago\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el Día Hábil en que el GRUPO deberá pagar a CONTIGO la Parcialidad establecida en la tabla de amortización. Cuando la Fecha de Pago sea en un día inhábil se recorrerá al siguiente día hábil.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);           
            texto = "\"Importe del Crédito\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el importe indicado en la Carátula como \"Monto del crédito\", que conforme al Contrato, será otorgado al GRUPO por CONTIGO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Importe Total del Crédito\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa la adición del importe de la Tasa de Interés Ordinario del Crédito, así como del importe del IVA de la Tasa de Interés Ordinario del Crédito, al Importe del Crédito. ";
            texto += "El Importe Total del Crédito será el importe que el GRUPO debe pagar al CONTIGO mediante las Parcialidades establecidas, indicado en la Carátula como \"Monto total a pagar\" y se podrá actualizar en caso de incrementos o modificaciones previamente autorizadas por CONTIGO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"IVA de la Tasa de Interés Ordinario del Crédito\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el Impuesto al Valor Agregado que, conforme a las disposiciones legales aplicables, será multiplicado sobre el importe de la Tasa de Interés Ordinario del Crédito. El importe del IVA de la Tasa de Interés Ordinario del Crédito será pagado ";
            texto += "a cargo del GRUPO a través de las Parcialidades. El importe del IVA de la Tasa de Interés Ordinario del Crédito se indica en la tabla de amortización.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Pago Adelantado\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el pago parcial o total, que aún no es exigible, aplicado a cubrir pagos periódicos del crédito, inmediatos siguientes.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Pago Anticipado\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el pago parcial o total del Saldo Insoluto del Crédito, antes de la fecha en que sea exigible.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Parcialidades\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa cada uno de los pagos a realizar por parte del GRUPO a CONTIGO, según se establezca en la tabla de amortizaciones. El número de pagos, periodicidad e importe de los mismos, se indican en la Carátula.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Tasa de Interés Moratorio\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el porcentaje fijo mensual, indicada en la Carátula mismo que incluye el Impuesto al Valor Agregado, aplicada al saldo pendiente de pago entre la fecha de vencimiento de una operación de crédito no pagada y la fecha en que ésta se liquida, la cual será cubierta ";
            texto += "en forma inmediata, en Día Hábil y, según sea el caso, junto con el importe que complete el importe no pagado en forma completa. La Tasa de Interés Moratorio será aplicable en tanto no sea pagado el importe de que se trate.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Tasa de Interés Ordinario del Crédito\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ". Significa el porcentaje fijo indicado en la Carátula, que será multiplicado sobre el Importe del Crédito y, cuyo resultante deberá pagarse por el GRUPO a CONTIGO a través de las Parcialidades. El pago de la Tasa de Interés Ordinario del Crédito no podrá ser exigido ";
            texto += "por adelantado sino únicamente por periodos vencidos, salvo por lo que establece el Contrato. En la Carátula se establece la Tasa de Interés Ordinario del Crédito anualizada, como \"Tasa de interés anual\".\n\n";
            texto += "Asimismo, las PARTES determinan que: (i) las Fechas de Pago; (ii) la Comisión por Pago Tardío; (iii) el Importe del Crédito; (iv) el Importe Total del Crédito; (v) el IVA de la Tasa de Interés Ordinario del Crédito; (vi) las Parcialidades; ";
            texto += "(vii) la Tasa de Interés Moratorio; y, (viii) la Tasa de Interés Ordinario del Crédito; establecidos en la Carátula y tabla de amortización respectivamente, son integrantes de la Información del Crédito y, por lo tanto, aplicables y relativos al mismo.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "SEGUNDA.- ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "OBJETO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El presente instrumento tiene por objeto establecer las obligaciones, derechos, términos, condiciones y características, bajo los cuales CONTIGO otorgará el crédito simple con obligación solidaria al GRUPO. ";
            texto += "Para efectos del Contrato, la Información del Crédito se encuentra establecida en la Carátula. Los importes que se asientan en la Carátula, se expresan en pesos, moneda nacional.\n\n";
            texto += "CONTIGO explicó al GRUPO, la Información relacionada con el otorgamiento del Crédito y, por lo tanto, el contenido, alcance y efectos del Contrato, mismos que son de la comprensión y entera satisfacción del GRUPO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "TERCERA.- ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "OTORGAMIENTO DEL CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Como consecuencia del Contrato y, en la fecha de su celebración, CONTIGO en este acto otorga un crédito simple con obligación solidaria al GRUPO, hasta por el importe señalado en la Carátula del presente contrato, dicho importe se podrá actualizar en caso de incrementos o modificaciones previamente autorizadas por CONTIGO.\n\n";
            texto += "Forman parte integrante del presente Contrato, los anexos que se enuncian a continuación, los cuáles se adjuntan al mismo en el orden siguiente:\n\n";
            texto += "Anexo A.- Carátula del Crédito.\n\n";
            texto += "Anexo B.- Solicitud de Crédito.\n\n";
            texto += "Anexo C.- Tabla de Amortización.\n\n";
            texto += "Dentro del Importe del Crédito, no se encuentra comprendida la Tasa de Interés Ordinario del Crédito, ni el IVA de la Tasa de Interés Ordinario del Crédito.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "CUARTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "DESTINO DEL CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las PARTES establecen que el Importe del Crédito, objeto del presente Contrato sólo podrá destinarse a las actividades comerciales del GRUPO (en adelante, las ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Actividades\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), que deberán ser siempre, productos o servicios lícitos.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "QUINTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "DISPOSICIÓN DEL IMPORTE DEL CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las PARTES determinan que el Importe del Crédito podrá ser dispuesto en una o varias disposiciones y para la primer disposición del Importe del Crédito, el GRUPO a través del presente contrato, instruye, autoriza y faculta a CONTIGO, ";
            texto += "en forma expresa e irrevocable, para que el monto de su primer disposición sea dividido en partes proporcionales, conforme se indica en la tabla al efecto establecida, cuyos resultantes (en adelante, en singular o en plural, los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Importes proporcionales del Crédito\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), sean entregados a cada una de las personas integrantes del GRUPO, mediante cualquiera de los siguientes instrumentos de disposición de los recursos económicos (en adelante, los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Instrumentos de Disposición\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), que sean reconocidos y empleados por alguna institución de crédito (en adelante, el \"Banco\"): (i) dispersiones automatizadas de pagos; (ii) tarjetas de débito; y, (iii) cualquier otro instrumento que sea autorizado por CONTIGO y admitido por el Banco; en sus diferentes  sucursales, según se aprecia a continuación:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);
            
            int colAcreditados[] = {10, 45, 15, 15, 15};
            tabla = new PdfPTable(5);
            tabla.setWidthPercentage(80);
            tabla.setWidths(colAcreditados);
            
            texto = "Nombre del Equipo";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            tabla.addCell(cell);
            
            texto = "" + grupo.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            tabla.addCell(cell);
            
            texto = "No";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            texto = "Nombre de la integrante del\n\"GRUPO\"";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            int colAcreditados1[] = {34, 33, 33};
            tabla2 = new PdfPTable(3);
            tabla2.setWidths(colAcreditados1);
            
            texto = "Monto total otorgado";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            tabla2.addCell(cell);
            
            //JECB 24/10/2017
            //Se replica el importe que aparece al inicion del 
            //Contrato
            //texto = "$ "+ciclo.tablaAmortizacion[0].saldoInicial;
            texto = "$ "+(ciclo.monto + sumatoriaSegContratado);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla2.addCell(cell);
            
            int colAcreditados2[] = {50, 50};
            tabla3 = new PdfPTable(2);
            tabla3.setWidths(colAcreditados2);
            
            texto = "Importes parciales del crédito";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            tabla3.addCell(cell);
            
            texto = "Forma de entrega";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla3.addCell(cell);
            
            texto = "Importes\n(pesos M/N)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla3.addCell(cell);
            
            cell = new PdfPCell(tabla3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            //cell.setPaddingTop(20);
            //cell.setPaddingBottom(20);
            tabla2.addCell(cell);
            
            texto = "Total a Pagar Capital con Intereses\n(IVA incluido)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla2.addCell(cell);
            
            cell = new PdfPCell(tabla2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            tabla.addCell(cell);
            double interes = 0, pagoIndividual = 0, interesColumna = 0;
            
            
            
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                
                //JECB 26/10/2017
                //Se obtiene el valor del seguro financiado
                double costoSeguroCliente = SegurosUtil.costoSeguroXIntegranteCiclo(ciclo.integrantes[i]);

                plazo = ciclo.tablaAmortizacion.length-1;
                if (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                    plazo = plazo - ClientesConstants.DISPERSION_SEMANA_2;
                } else if (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                    plazo = plazo - ClientesConstants.DISPERSION_SEMANA_4;
                }
                interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
                //JECB 26/10/2017
                //Se considera en el calculo del interes el valor del seguro
                interes = ((ciclo.integrantes[i].montoDesembolso - ciclo.integrantes[i].montoAdicional) + costoSeguroCliente) * interes;
                interes = FormatUtil.redondeaMoneda(interes);
                interes = Math.ceil(interes);
                
                texto = ""+ciclo.integrantes[i].idCliente;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                texto = ""+ciclo.integrantes[i].nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                texto = ciclo.integrantes[i].medioCobro==1 ? "TAR" : "ODP";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                
                
                //JECB 13/10/2017
                //Se resta el importe de adicional en caso de que existiera
                //para que quede el importe del credito comunal
                texto = "$ "+((ciclo.integrantes[i].montoDesembolso - ciclo.integrantes[i].montoAdicional) + costoSeguroCliente);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                texto = "$ "+(((ciclo.integrantes[i].montoDesembolso - ciclo.integrantes[i].montoAdicional) + costoSeguroCliente)+interes);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);
            
            }
            
            document.add(tabla);

            p = new Paragraph();
            texto = "En virtud de lo anterior, CONTIGO entrega en este acto a los integrantes del GRUPO, los números, folios, órdenes o instrucciones relativas a los Instrumentos de Disposición (en adelante, las \"Referencias\"), quienes por conducto del presente Contrato aceptan y acusan su recepción, a su entera satisfacción, ";
            texto += "el GRUPO, dentro de los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "3 (tres) días hábiles inmediatos siguientes a la fecha de celebración del presente instrumento ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "deberá presentarse en cualquier sucursal del Banco que previamente haya elegido, con las Referencias, para el cobro de los Instrumentos de Disposición.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Por lo tanto, las PARTES determinan que: (i) el cobro de los Instrumentos de Disposición por parte de los integrantes del GRUPO, se considerará como la disposición sobre los Importes proporcionales del Crédito y, consecuentemente, de su recepción; ";
            texto += "(ii) la disposición de los Importes proporcionales del Crédito se considerará como la entrega de los recursos económicos del crédito otorgado a cargo de CONTIGO a favor del GRUPO. (iii) En consecuencia, se tendrá por recibido, a la entera satisfacción de los integrantes ";
            texto += "del GRUPO, el Importe del Crédito; y la disposición se considerará como cumplimiento de CONTIGO a las obligaciones que, a su cargo y a favor del GRUPO, se derivan del presente Contrato.\n\n";
            texto += "Realizada la disposición del Importe del Crédito, los integrantes del GRUPO, estarán obligados a pagar a CONTIGO, a través de las Parcialidades al efecto establecidas, el Importe Total del Crédito. El GRUPO será responsable del mal uso que se haga de cualquier monto que reciban en virtud del presente Contrato.\n\n";
            texto += "El GRUPO, en este acto, suscribe a su cargo y a favor de CONTIGO, un pagaré (en adelante, el ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Pagaré\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), por el importe que asciende al Total del Crédito y, el que a su vez, documenta la disposición que realiza el GRUPO sobre el monto total del Crédito indicado en el presente instrumento. Al respecto, el Crédito quedará otorgado para todos los efectos legales a que haya lugar.\n\n";
            texto += "El GRUPO tendrá un plazo de diez días hábiles contados a partir del día siguiente al de la fecha de la firma del presente contrato, para solicitar la cancelación del mismo sin comisión, penalización ni responsabilidad alguna para ellos, siempre y cuando no hubieren dispuesto total o parcialmente del crédito objeto del mismo\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "SEXTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "OMISIÓN EN LA DISPOSICIÓN DE LOS IMPORTES PARCIALES DEL CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " En caso que cualquiera de las integrantes de GRUPO omita, dentro del plazo que refiere la CLÁUSULA inmediata anterior, el cobro de los Instrumentos de Disposición y, por lo tanto, la disposición de los Importes Parciales del Crédito (en adelante, las ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
            p.add(ck);
            texto = "\"Acreditadas Omisas\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 9, Font.UNDERLINE));
            p.add(ck);
            texto = "), CONTIGO, ajustará el Importe del Crédito y el Importe Total del Crédito que se indican en la Carátula, mediante la deducción de los Importes Proporcionales del Crédito no dispuestos, sobre el Importe del Crédito y el Importe Total del Crédito. Por lo tanto, los importes de: (i) el Pagaré; (ii) la Tasa de Interés Ordinario del Crédito que se indica en la Carátula; ";
            texto += "(iii) las Parcialidades que se indica en la Carátula; (iv) el IVA de la Tasa de Interés Ordinario del Crédito que se indica en la Carátula; y, (v) el monto del 10% (Diez por ciento) que refiere la CLÁUSULA DÉCIMA siguiente; se ajustarán conforme al Importe del Crédito y al Importe Total del Crédito que resulten del ajuste y, en consecuencia, ";
            texto += "las integrantes del GRUPO que realicen el cobro de los Instrumentos de Disposición, estarán obligadas, en lo conducente y, conforme a los términos del Contrato, al pago y entrega, a su cargo y a favor de CONTIGO, de los importes que, respecto del Pagaré, ";
            texto += "la Tasa de Interés Ordinario del Crédito, las Parcialidades, el IVA de la Tasa de Interés Ordinario del Crédito y el monto del 10% (Diez por ciento) que refiere la CLÁUSULA DÉCIMA siguiente, resulten del ajuste (en adelante, y, en conjunto, los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
            p.add(ck);
            texto = "\"Importes Ajustados\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 9, Font.UNDERLINE));
            p.add(ck);
            texto = ").\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
            p.add(ck);
            texto = "Los Importes Ajustados serán notificados por CONTIGO a las integrantes del GRUPO que realicen el cobro de los Instrumentos de Disposición, a través del Representante que refiere la CLÁUSULA DÉCIMA TERCERA siguiente, ";
            texto += "y en el domicilio común del GRUPO, dentro de los 4 (cuatro) días hábiles inmediatos siguientes al de terminación del plazo de 3 (tres) días hábiles siguientes que refiere la CLÁUSULA QUINTA anterior, para que las integrantes que realicen el cobro de los Instrumentos de Disposición, efectúen, a su cargo ";
            texto += "y a favor de CONTIGO y, en lo conducente, el pago y entrega de los Importes Ajustados, conforme a los términos del Contrato, el Costo Anual Total (CAT), el Importe del Crédito, el Importe Total del Crédito, ";
            texto += "el importe de la Tasa de Interés Ordinario del Crédito a pagar en cada Parcialidad y el importe del IVA de la Tasa de Interés Ordinario del Crédito a pagar en cada Parcialidad, resultantes del ajuste (en adelante y, en conjunto, la ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Información Ajustada\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "). Como consecuencia de lo anterior el Contrato, la Carátula y el Pagaré se tendrán por modificados, en lo conducente, en los términos de los Importes Ajustados y de la Información Ajustada\n\n";
            texto += "Los Acreditados que omitan la disposición de los Importes Parciales del Crédito (en adelante, los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Acreditados Omisos\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), no serán parte del Contrato y, en consecuencia, no estarán obligados a su cumplimiento. Por lo tanto, CONTIGO entregará a los Acreditados Omisos, los importes proporcionales ";
            texto += "que efectuaron para integrar el anticipo del pago de la Ultima Amortización que refiere la CLÁUSULA DÉCIMA siguiente, así como en su caso de ser precedentes los que pagaron por concepto de las primas para la adqusición de la póliza del seguro que refiere la CLÁUSULA VIGÉSIMA PRIMERA del Contrato ";
            texto += "dentro de los 30 (treinta) días hábiles inmediatos siguientes al de terminación del plazo de los tres días hábiles a que refiere la CLÁUSULA QUINTA anterior.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "SÉPTIMA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "PAGOS.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las Parcialidades, así como: (i) los importes de la Comisión por pago tardío; (ii) los importes de la Tasa de Interés Moratorio; y, (iii) cualquier otro importe que el GRUPO adeude a CONTIGO ";
            texto += "conforme al Contrato; deberán pagarse mediante: 1) depósito en cualquiera de las instituciones de crédito denominadas (en adelante, el ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Banco Receptor\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ") o 2) recaudación en cualquiera de los establecimientos, tiendas o sucursales de las empresas a las que esté ";
            texto += "afiliado CONTIGO, a través de las fichas de recepción de pagos en las que se establecerán los respectivos códigos de barras.\n\n";
            texto += "El Importe Total del Crédito será pagado a cargo del GRUPO y a favor de CONTIGO, a través de las Parcialidades y en las respectivas Fechas de Pago. Por lo tanto, los importes de: (i) la Tasa de Interés Ordinario del Crédito a pagar en cada Parcialidad; (ii) el IVA de la Tasa de Interés Ordinario del Crédito a pagar ";
            texto += "en cada Parcialidad; y, (iii) cada Parcialidad; se indican en la Carátula, junto con las Fechas de Pago\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "OCTAVA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "APLICACIÓN DE PAGOS.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Cualquier pago que, en términos del Contrato, sea realizado por el GRUPO, será aplicado por CONTIGO, para cubrir, en primer lugar y hasta donde alcance, el saldo insoluto del importe de la Comisión por pago tardío de que se trate, en caso de que exista, seguidamente ";
            texto += "y hasta donde alcance, el saldo insoluto del importe de la Tasa de Interés Moratorio de que se trate, en caso de que exista, el saldo insoluto del importe de la Tasa de Interés Ordinario de que se trate, en caso de que exista y por último y hasta donde alcance, el saldo insoluto del importe de las Parcialidades de que se trate.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "NOVENA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "PAGO ADELANTADO Y PAGO ANTICIPADO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " A partir de la anterior a la antepenúltima Parcialidad y, siempre que el GRUPO se encuentre al corriente en el cumplimiento de las obligaciones de pago que establece el Contrato a su cargo, podrá pagar adelantadamente a CONTIGO, ";
            texto += "la totalidad de las Parcialidades por vencer. El pago adelantado de la totalidad de las Parcialidades: a) no causará: (i) la reducción o condonación del saldo insoluto de la Tasa de Interés Ordinario del Crédito, así como del saldo insoluto del ";
            texto += "IVA de la Tasa de Interés Ordinario del Crédito; y, (ii) cargo alguno al GRUPO; b) deberá realizarse por el importe íntegro de la totalidad de las Parcialidades por vencer; y, c) causará la terminación del Contrato.\n\n";
            texto += "Siempre que el GRUPO se encuentre al corriente en el cumplimiento de todas y cada una de sus obligaciones asumidas en el presente Contrato, podrá realizar pagos anticipados, sin premio o castigo alguno, dichos pagos anticipados no eximen al GRUPO de cubrir oportunamente los siguientes pagos pactados, ni reducen el importe de los mismos.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "ÚLTIMA AMORTIZACIÓN.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " En la fecha de celebración del Contrato, el GRUPO deposita en la cuenta bancaria que CONTIGO le indico abierta en la institución bancaria (Banco Receptor), o en tiendas y sucursales de las empresas a las que esté afiliado (corresponsales bancarios) ";
            texto += "la cantidad equivalente al 10% (diez por ciento) del Importe del Crédito, y siempre y cuando el GRUPO se encuentre al corriente en sus pagos, dicha cantidad será aplicada a favor del GRUPO en la Última Amortización de su crédito otorgado por medio del presente Contrato o en créditos subsecuentes que le otorgue CONTIGO a las integrantes del GRUPO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Asimismo, las PARTES determinan que el monto del 10% (diez por ciento) señalado anteriormente será empleado para cubrir a favor de CONTIGO, en su caso el pago de: "
                    + "(i) el saldo insoluto por principal, así como por accesorios del Crédito; (ii) cualquier otro importe que el GRUPO adeude a CONTIGO conforme al "
                    + "Contrato; (iii) los gastos y costas que se causen por el procedimiento judicial que, en su caso, sea instaurado por CONTIGO en contra del GRUPO, para requerirles el cumplimiento de las obligaciones que, "
                    + "en términos del Contrato, contraen a su cargo; y, (iv) las demás prestaciones que resulten a cargo del GRUPO que se deriven del presente instrumento. Lo anterior en el entendido que dicho monto de ninguna manera generará, a favor del GRUPO, interés, rendimiento o ganancia alguna.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA PRIMERA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "OBLIGACIÓN SOLIDARIA.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Cada una de las integrantes del GRUPO constituyen una obligación personal y solidaria respecto de las obligaciones asumidas por las demás integrantes, manifestando expresamente que no cesará su obligación sino hasta en tanto CONTIGO haya recibido la totalidad de las cantidades de las ";
            texto += "intengrantes del GRUPO que, en su conjunto se obligan a pagar. En virtud de lo anterior, las integrantes del GRUPO en este acto se obligan de manera solidaria y personal a pagar a CONTIGO, el importe total del Crédito y los intereses ordinarios que se indican en este Contrato, ";
            texto += "así como los intereses moratorios, gastos de cobranza y/o comisiones, que en su caso se generen.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA SEGUNDA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "EFECTOS DEL CUMPLIMIENTO DEL CONTRATO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " En caso que el Crédito, sea pagado en los términos del Contrato, se dará por terminado. Asimismo, conforme a lo establecido en la CLÁUSULA DÉCIMA SEPTIMA siguiente, será publicado, "
                    + "dentro de los 30 Días Hábiles siguientes al día hábil siguiente en que se verifique la liquidación del crédito, según se establezca en la tabla de amortización, el estado de cuenta en el cual se indique: "
                    + "a) la finalización de la relación contractual; (b) la cancelación y por tanto extinción de los derechos y obligaciones derivados del Contrato; y, (c) la inexistencia de adeudos entre las PARTES.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA TERCERA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "OBLIGACIONES DEL GRUPO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las integrantes del GRUPO, en su calidad de integrantes del GRUPO se obligan a cumplir con los siguientes lineamientos:\n\n";
            texto += "1. El GRUPO deberá sesionar de manera Semanal\n\n";
            texto += "2. Las PERSONAS AUTORIZADAS del GRUPO tendrá(n) la obligación de controlar y recabar los pagos de todas las integrantes, mismas que deberán registrar y, depositar como pago por cuenta de éstas a CONTIGO conforme a lo dispuesto en la CLAUSULA SEPTIMA del presente Contrato. ";
            texto += "En caso de que alguna de las integrantes del GRUPO se atrase, las demás integrantes deberán cubrir dicho faltante a efecto de realizar el pago correspondiente a CONTIGO en los términos pactados.\n\n";
            texto += "3. Las PERSONAS AUTORIZADAS del GRUPO deberán hacer entrega de la ficha de depósito al representante de CONTIGO y mostrar dicho comprobante al GRUPO en la siguiente reunión, en el entendido que dicho ";
            texto += "representante de CONTIGO tiene prohibido recibir dinero en efectivo y solo puede recibir el comprobante respectivo de pago.\n\n";
            texto += "4. Las integrantes del GRUPO se obligan a proporcionar en cualquier momento, durante la vigencia del Crédito, la información que les sea requerida por CONTIGO.\n\n";
            texto += "5.- Cumplir con todas y cada una  de las obligaciones contraídas en el presente instrumento.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA CUARTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "DEL EJERCICIO DE LAS PERSONAS AUTORIZADAS DEL GRUPO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El GRUPO, a través del Contrato, instruye, autoriza y faculta a las PERSONAS AUTORIZADAS que previamente fueron designadas frente a CONTIGO, en forma expresa e irrevocable, para que una vez que el Crédito sea pagado en sus términos, soliciten y entreguen a favor de las PERSONAS AUTORIZADAS el Pagaré ";
            texto += "que fue firmado en el momento de la celebración del presente instrumento. Al efecto, las PERSONAS AUTORIZADAS deberán solicitar a CONTIGO la entrega del Pagaré. La solicitud de referencia deberá realizarse por escrito y en el domicilio de la sucursal en donde fue otorgado el crédito.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA QUINTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "BURÓ DE CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El GRUPO a través de la Solicitud, autoriza a CONTIGO con la finalidad de obtener de cualquier SOCIEDAD DE INFORMACIÓN CREDITICIA la información sobre sus respectivos historiales crediticios, respecto de las operaciones crediticias y otras de naturaleza análoga, ";
            texto += "que los integran. Por lo tanto y, toda vez que el GRUPO, ha leído y comprendido la naturaleza y alcance de la información contenida en la  base de datos de la sociedad de información crediticia, cualquier incumplimiento del GRUPO a las obligaciones de pago que a su cargo establece el ";
            texto += "Contrato, será registrado por CONTIGO en dicha sociedad, con claves de observación establecidas en los correspondientes reportes de crédito, las cuales podrán afectar los respectivos historiales crediticios del GRUPO.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA SEXTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "TÍTULO EJECUTIVO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El Contrato y el estado de cuenta que certifique el contador de CONTIGO, serán título ejecutivo mercantil, sin necesidad de reconocimiento de firma ni de otro ";
            texto += " requisito alguno, de conformidad con lo establecido por los artículos 87-E y 87-F de la Ley General de Organizaciones y Actividades Auxiliares del Crédito.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA SÉPTIMA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "ESTADOS DE CUENTA, UNIDAD ESPECIALIZADA Y ATENCIÓN A USUARIOS.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = "i) Servicio de atención al público en consultas y aclaraciones.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            p.setFont(bodyFont);
            texto = "CONTIGO, pondrá los estados de cuenta a disposición de las PERSONAS AUTORIZADAS por el GRUPO, en forma mensual y, sin costo alguno, en la sucursal ";
            texto +="que se desembolsó el importe del crédito otorgado a su favor, dentro de los 10 (Diez) Días hábiles siguientes a la fecha de cierre del período mensual de que se trate.\n\n";
	    texto +="En el supuesto de que el GRUPO no esté de acuerdo con alguno de los movimientos que aparezcan en el estado de cuenta, El GRUPO contará con un período de 90 (noventa) días naturales, contado a partir de la fecha de publicación del respectivo estado de cuenta, para formular, por escrito y, a través de las PERSONAS AUTORIZADAS por el GRUPO, ";
            texto +="cualquier aclaración, inconformidad, reclamación o queja con respecto a la información contenida en el mismo, ante la Unidad Especializada de Consultas y Reclamaciones de CONTIGO, cuyos datos de localización y contacto se establecen en el párrafo inmediato siguiente. En caso contrario, se entenderá que dicha información es aceptada en los términos en los que se pública.\n\n";
	    texto +="De igual forma, contará con el mismo plazo, para cualquier aclaración, inconformidad, reclamación o queja que se relacione con el Crédito. Dicho plazo contará a partir de la fecha en que El GRUPO tenga y haga del conocimiento a CONTIGO sobre el acto u omisión que la haya motivado, para formular por escrito cualquier aclaración, inconformidad, reclamación o queja, a través de alguna de las PERSONAS AUTORIZADAS, ante el ";
            texto +="Titular de la Unidad Especializada de Consultas y Reclamaciones de CONTIGO (en adelante, el ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Titular\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), ubicada en  Carretera México-Toluca número 2430, Piso 4, Colonia Lomas de Bezares, Delegación Miguel Hidalgo, Ciudad de México, Código Postal 11910 , teléfonos (55) 41-60-21-00 ó 01 800 8378760 (lada sin costo), en un horario de atención en días hábiles de 8:00 a 17:00 horas de lunes a viernes, o a través del correo electrónico ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "une@fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ", o a través de la dirección de Internet ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ", al respecto, CONTIGO estará obligada a acusar recibo de dicha solicitud\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "En cumplimiento del artículo 5, fracción VII, inciso c. de la DISPOSICIONES de carácter general en materia de transparencia aplicables a las Sociedades Financieras de Objeto Múltiple, Entidades No Reguladas, publicadas en el Diario Oficial de la Federación el 11 de agosto del 2015, se realiza la explicación del proceso de aclaración contenido en el artículo 23 de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros.\n\n";
            texto += "Una vez que CONTIGO reciba cualquier aclaración que sea formulada por el GRUPO conforme a lo establecido en los párrafos anteriores de la presente CLÁUSULA, tendrá un plazo de hasta 45 (cuarenta y cinco) días naturales para entregar al GRUPO (a través de las \"PERSONAS AUTORIZADAS\"), el dictamen correspondiente, junto con la información y/o documentación considerada para su ";
            texto += "emisión, así como un informe detallado en el que se respondan los hechos contenidos en la solicitud de aclaración. En caso de que conforme a dicho dictamen resulte procedente el cobro del monto de que se trate, el GRUPO deberá hacer el pago de la cantidad a su cargo, incluyendo los intereses ordinarios y excluyendo la Tasa de Interés Moratorio.\n\n";
            texto += "Dentro del plazo de 45 (cuarenta y cinco) días naturales contado a partir de la entrega del dictamen de referencia, CONTIGO pondrá a disposición del GRUPO, a través de su Unidad Especializada de Consultas y Reclamaciones, el expediente generado por la solicitud, con la integración de la información y documentación que deba obrar en su poder y que se relacione ";
            texto += "directamente con la solicitud. Hasta en tanto la solicitud de aclaración no sea resuelta, CONTIGO no podrá reportar como vencidas las cantidades sujetas a dicha aclaración a las SOCIEDADES DE INFORMACIÓN CREDITICIA.\n\n";
            texto += "Lo anterior sin perjuicio del derecho de las integrantes del GRUPO de acudir ante la CONDUSEF o ante la autoridad jurisdiccional correspondiente conforme a las disposiciones legales aplicables.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "ii) Unidad Especializada.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            p.setFont(bodyFont);
	    texto = "Para cualquier solicitud o consulta que se relacione con el Crédito, el GRUPO podrá formularla, por escrito y, a través de las PERSONAS AUTORIZADAS: (i) ante el Titular; o (ii) ante el área de atención a clientes, ubicada en  Carretera México-Toluca número 2430, Piso 4, Colonia Lomas de Bezares, Delegación Miguel Hidalgo, Ciudad de México, Código ";
            texto += "Postal 11910, teléfonos (55) 41-60-21-00 ó 01 800 8378760 (lada sin costo) en días hábiles de 8:00 a 17:00 horas de lunes a viernes, o a través de la dirección de Internet ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " y correo electrónico ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "une@fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ";\n\n";ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "iii) Atención CONDUSEF.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            p.setFont(bodyFont);
            texto = "Se hace del conocimiento del GRUPO, que el número telefónico de la COMISIÓN NACIONAL PARA LA PROTECCIÓN Y DEFENSA DE LOS USUARIOS DE SERVICIOS FINANCIEROS (CONDUSEF) para la atención de usuarios es el: 53-40-09-99 ó 01800-999-8080 (lada sin costo), dirección en Internet: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.condusef.gob.mx";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ", y correo electrónico: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "opinion@condusef.gob.mx";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ".\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "DÉCIMA OCTAVA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "VIGENCIA.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " La vigencia del Contrato iniciará a partir de su fecha de celebración y terminará en la fecha de pago de la última Parcialidad, pudiendo prolongarse hasta en tanto no sean cumplimentadas, en su totalidad, las obligaciones que, en términos del Contrato, haya contraído el GRUPO. La responsabilidad del GRUPO, terminará una vez que sean concluidas las obligaciones que por virtud del presente contrato hayan sido contraídas.\n\n";
	    ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);

            texto = "DÉCIMA NOVENA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "VENCIMIENTO ANTICIPADO DEL CONTRATO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " En el caso que acontezca cualquiera de los supuestos que se establecen en los incisos inmediatos siguientes, CONTIGO dará por vencido anticipadamente el Contrato y, por lo tanto, el GRUPO pagará a favor de CONTIGO: (i) el saldo insoluto";
            texto += " por principal, así como por accesorios del Crédito; y, (ii) cualquier otro importe que el GRUPO adeude a CONTIGO conforme al Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Serán causas de vencimiento anticipado del contrato:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "a) El Crédito no sea pagado en los términos del Contrato.\n\n";
            texto += "b) La falta de pago por el GRUPO, en las fechas de Pago, de cualquiera de las parcialidades, así como de cualquier otro importe que el GRUPO adeude a CONTIGO conforme al presente Contrato. Se considerará como falta de pago, el incumplimiento a partir de la segunda parcialidad no cubierta en forma sucesiva, según se establezca en la tabla de amortización.\n\n";
            texto += "c) Que la información o documentación que deba ser informada o entregada por el GRUPO a CONTIGO resulte ser falsa.\n\n";
            texto += "d) El incumplimiento del GRUPO a cualquier obligación que, en términos del Contrato, se encuentra establecida a su cargo.\n\n";
            texto += "e) El Importe del Crédito se destine a una actividad distinta a las Actividades o, a actividades ilícitas.\n\n";
            
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "TERMINACIÓN ANTICIPADA DEL CONTRATO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El GRUPO podrá solicitar a CONTIGO, en cualquier tiempo, la terminación anticipada del Contrato, para tal efecto, deberá pagar a CONTIGO, en los términos establecidos en el Contrato: (i) el saldo insoluto por principal, así como por accesorios del ";
            texto += "Crédito; y, (ii) cualquier otro importe que el GRUPO adeude a CONTIGO conforme al Contrato. Por la terminación anticipada del Contrato, no se reducirá o condonará el saldo insoluto de la Tasa de Interés Ordinario del Crédito, así como el saldo insoluto del IVA de la Tasa de Interés Ordinario del Crédito.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA PRIMERA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "SEGURO INDIVIDUAL DEL CRÉDITO.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El GRUPO de manera opcional puede instruir y autorizar a CONTIGO para que, a nombre y por cuenta del GRUPO, CONTIGO contrate un seguro individual con la respectiva Aseguradora, a favor de los Beneficiarios, con la finalidad de que cubra los riesgos establecidos en la Póliza y las condiciones generales del seguro opcional correspondientes, ";
            texto += " respecto de  cada uno de los integrantes del GRUPO (el ; ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Seguro Opcional\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ") mediante alguna de las siguientes opciones:\n\n(i) ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Seguro Opcional por pago único.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " En la fecha de celebración del presente Contrato, la Prima del Seguro Opcional será pagada por cada una de las integrantes del GRUPO que así lo hayan solicitado, en su totalidad, mediante pago en el Banco Receptor, ";
            texto += " o pago realizado en cualquiera de los establecimientos, tiendas o sucursales (corresponsales bancarios) mediante las cuales CONTIGO tiene convenios para hacer uso de dichos servicios de corresponsalía..\n\n(ii) ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Seguro Opcional con pagos parciales.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " En las respectivas fechas de pago, de manera consecutiva e ininterrumpida, las integrantes del GRUPO que así lo hayan solicitado pagarán a CONTIGO la Prima del Seguro Opcional, en la forma que se indique en la Póliza que corresponda, ";
            texto += " mediante pago en el Banco Receptor, o recaudación en cualquiera de los establecimientos, tiendas o sucursales de las empresas a las que esté afiliado CONTIGO. Las integrantes del GRUPO estarán obligadas a comprobar, ";
            texto += " en cualquier momento, a CONTIGO, los pagos que sean realizados, mediante cualquier Medio de Comprobación de Pagos.\n\n(iii) ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Seguro Opcional con Pago Inicial y Pagos Parciales.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " En la fecha de celebración del presente Contrato, las integrantes del GRUPO que lo hayan solicitado pagarán a CONTIGO, el monto inicial a cuenta de la Prima del Seguro Opcional por el monto que se establece en la Póliza que corresponda, mediante pago en el Banco Receptor, ";
            texto += " o recaudación en cualquiera de los establecimientos, tiendas o sucursales de las empresas a las que esté afiliado CONTIGO. Posteriormente, en las fechas de pago, de manera consecutiva e ininterrumpida, las integrantes del GRUPO entregarán a CONTIGO el remanente de la ";
            texto += " Prima del Seguro Opcional en la forma que se indica en la Póliza correspondiente, mediante pago mediante pago en el Banco Receptor, o pago realizado en cualquiera de los establecimientos, tiendas o sucursales (corresponsales bancarios) mediante la cuales CONTIGO tiene convenios para hacer uso de dichos servicios de corresponsalía.\n\n";
            texto += " La Prima del Seguro Opcional no es integrante del Crédito y por lo tanto del Monto Total a Pagar y/o Monto Total Adicional a Pagar, en su caso. La vigencia, coberturas, exclusiones, condiciones ";
            texto += " y términos relativos y aplicables al Seguro Opcional, se regirán por la póliza, los certificados individuales, así como por las condiciones generales del Seguro Opcional.\n\n";
            texto += " La vigencia del Seguro Opcional deberá comenzar, a más tardar a partir de la fecha de disposición del Importe Parcial del Crédito correspondiente.\n\n ";
            texto += "Las PARTES acuerdan que CONTIGO será el primer beneficiario irrevocable y, por lo tanto, en caso del fallecimiento de cualquiera de las integrantes del GRUPO (en adelante, la ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "\"Integrante Fallecida\"";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "), la suma asegurada de la Cobertura por Fallecimiento Individual será entregada por la Compañía Aseguradora a favor de CONTIGO, para pagar, hasta donde alcance, el saldo insoluto, a la fecha del fallecimiento de la integrante del GRUPO, de: ";
            texto += " (i) el Importe Parcial del Crédito; (ii) la Tasa de Interés Ordinario del Crédito que corresponda al saldo insoluto del Importe Parcial del Crédito; (iii) el IVA de la Tasa de Interés Ordinario del Crédito que corresponda al saldo insoluto ";
            texto += " de la Tasa de Interés Ordinario del Crédito que corresponda al saldo insoluto del Importe Parcial del Crédito; y, (iv) cualquier otro importe que la Integrante Fallecida adeude a CONTIGO conforme al Contrato. El remanente, en caso de que exista, será entregado por la Compañía Aseguradora a los beneficiarios designados por la Integrante Fallecida en el certificado individual. Las ";
            texto += "Integrantes del GRUPO supervivientes, continuarán obligadas, en lo conducente, al cumplimiento del Contrato.\n\n";
            texto += "CONTIGO explicó al GRUPO, las coberturas, exclusiones y alcance del Seguro Opcional a que se refiere la presente cláusula del Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA SEGUNDA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "IMPUESTOS.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Todos los impuestos, contribuciones y derechos que deban cubrirse con motivo de la celebración y ejecución del Contrato, serán cubiertos por la parte que resulte obligada a ello, de conformidad con las disposiciones fiscales aplicables.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA TERCERA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "TRANSMISIÓN.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las PARTES no podrán transmitir cualquier derecho u obligación que en términos del Contrato, resulte a favor o a cargo de alguna de ellas sin la previa aprobación escrita de la contraparte, ";
            texto += " a excepción de lo indicado en el párrafo inmediato siguiente, por lo que cualquier transmisión en términos distintos a los aquí previstos será nula y no será reconocida por la parte de que se trate.\n\n";
            texto += " En términos de lo dispuesto por el artículo 299 de la Ley General de Títulos y Operaciones de Crédito, las integrantes del GRUPO facultan a CONTIGO para endosar, ceder, transmitir, descontar, transferir, negociar, afectar y/o gravar, en cualquier tiempo y, sin previa autorización del GRUPO, respectivamente, cualquier derecho que, en ";
             texto += " términos del Contrato, resulte a favor del GRUPO y/o los derechos de crédito contenidos en el Contrato y/o en el Pagaré. De igual forma, cualquier causahabiente de CONTIGO, podrá realizar lo establecido en el presente párrafo, conforme al mismo.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA CUARTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "LEYES APLICABLES.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " El Contrato se rige por lo dispuesto en sus CLÁUSULAS, en su defecto o supletoriamente, por las disposiciones contenidas y que resulten aplicables de la Ley General de Títulos y Operaciones de Crédito, de la Ley General de Organizaciones y ";
            texto += " Actividades Auxiliares del Crédito, de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros, de las DISPOSICIONES de carácter general en materia de transparencia aplicables a las Sociedades Financieras de Objeto Múltiple, Entidades No Reguladas, del Código de Comercio, así como del Código Civil Federal.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA QUINTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "NOTIFICACIONES.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Las PARTES señalan como sus domicilios para los efectos de notificaciones de carácter extrajudicial o judicial que se relacionen con el Contrato, los enunciados respectivamente en las Solicitudes de Crédito.\n\n";
            //texto += "Actividades Auxiliares del Crédito, de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros, de la Disposición Única de la CONDUSEF aplicable a las Entidades Financieras, del Código de Comercio, así como del Código Civil Federal.\n\n";
            texto += "Sin embargo, las PARTES podrán cambiar o señalar en el futuro cualquier otro domicilio, mediante notificación realizada a la otra parte con un plazo de por lo menos 10 (diez) días hábiles de anticipación, ";
            texto += "en el entendido que de no proporcionarse tal aviso de cambio, todas y cada una de las notificaciones que se le hicieren a la parte de que se trate, en el domicilio que ahora señala, se considerarán legalmente realizadas.\n\n";
            texto += "Las notificaciones extrajudiciales que tengan que realizar las PARTES, serán por escrito, dirigidas a la contraparte, mediante entrega personal con acuse de recibo en el respectivo domicilio. Surtirán sus efectos, a partir de la fecha en que sean entregadas por CONTIGO y recibidas por la parte a quien hayan sido dirigidas.\n\n";
            texto += "Sin perjuicio de lo establecido en los párrafos primero y tercero de la presente CLÁUSULA, las notificaciones extrajudiciales que sean relativas al requerimiento del o de los pagos del Crédito al GRUPO, ";
            texto += "podrán realizarse por CONTIGO, en cualquier domicilio de los integrantes del GRUPO que sea del conocimiento de CONTIGO, por escrito y, entregadas en forma personal, sin acuse de recibo.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA SEXTA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "PROTECCION DE DATOS PERSONALES.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Conforme a la Ley Federal de Protección de Datos Personales en Posesión de los Particulares, CONTIGO, previamente a la celebración del presente contrato, solicitó datos personales a las integrantes del GRUPO, con la finalidad de identificarlas y poder celebrar el presente contrato de crédito, informarles sobre el estatus del mismo, ceder o vender el mismo, realizar requerimientos de pago.\n\n";
	    texto += "CONTIGO protegerá y mantendrá los datos personales de las integrantes del GRUPO por el tiempo razonablemente necesario, tomando en cuenta las disposiciones legales aplicables y sólo compartirá y/o ";
            texto += "transferirá dicha información con otra(s) entidad(es), cuando las integrantes del GRUPO contraten otro producto y/o servicio de o, a través CONTIGO, o para la cesión y/o venta del presente contrato, o bien, cuando así se requiera por disposición legal.\n\n";
	    texto += "Las integrantes del GRUPO podrán ejercer en todo momento ante CONTIGO, sus derechos de acceso, rectificación, cancelación u oposición en el tratamiento de sus datos personales, en caso de que legalmente sea procedente, conforme a los lineamientos y requisitos que marca la Ley Federal de Protección de Datos ";
            texto += "Personales en Posesión de los Particulares. Al respecto las personas interesadas podrán acudir a la sucursal más cercana de CONTIGO, con la finalidad de realizar su solicitud con apoyo del personal que se encuentre en las mismas, o en su caso, deberán enviar su solicitud a través del correo electrónico ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "datospersonales@fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " o comunicarse al teléfono 01 800 837 8760.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);	texto = "Por último señala CONTIGO que el Aviso de Privacidad puede ser consultado a través de su sitio en internet: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
	    texto = " y de igual forma cualquier cambio y/o modificación total o parcial al Aviso de Privacidad se dará a conocer por medio de la mencionada página web y/o directamente en las Sucursales de CONTIGO.\n\n";
	    ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA SÉPTIMA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "SUBTÍTULOS.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Los subtítulos en las CLÁUSULAS del Contrato, son exclusivamente por conveniencia de las PARTES, para una referencia y lectura más simple, por lo que no regirán la interpretación del Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA OCTAVA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "TRIBUNALES COMPETENTES.-";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Para la interpretación y cumplimiento del Contrato, las PARTES se someten de manera expresa a la jurisdicción de los Tribunales competentes de la delegación o municipio ";
            texto += "que elija la parte actora, por consiguiente, renuncian a cualquier otra jurisdicción que por razón de su domicilio o cualquier otra causa les corresponda o pudiere corresponderles, ya sea en lo presente o futuro.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "LEÍDO Y COMPRENDIDO EL PRESENTE CONTRATO, ENTERADAS LAS PARTES QUE INTERVIENEN DE SU CONTENIDO, ALCANCES, CONSECUENCIAS LEGALES Y ECONÓMICAS, LO FIRMAN POR DUPLICADO DE CONFORMIDAD, A LOS ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + FechasUtil.obtenParteFecha(fechaFirma, 1);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " DIAS DEL MES DE ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = mes;
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " DEL AÑO ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + FechasUtil.obtenParteFecha(fechaFirma, 3) + " ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " EN LA CIUDAD DE ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = sucursal.municipio.toUpperCase();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " Y EN ESTE ACTO SE HACE ENTREGA A CADA UNA DE LAS PARTES QUE INTERVIENEN EN EL PRESENTE CONTRATO, DE UN TANTO DE ESTE INSTRUMENTO Y SUS RESPECTIVOS ANEXOS.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            document.newPage();
            texto = "LISTADO DE INTEGRANTES DEL GRUPO\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            int col[] = {10, 65, 25};
            tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);
            texto = "NÚMERO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);
            texto = "NOMBRE(S) Y APELLIDO(S) COMPLETO / DOMICILIO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);
            texto = "FIRMA";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                texto = "" + (i + 1);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(12);
                tabla.addCell(cell);
                texto = "" + ciclo.integrantes[i].nombre + "\n" + ClientesUtil.getDomicilio(ciclo.integrantes[i].idCliente);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(12);
                tabla.addCell(cell);
                cell = new PdfPCell();
                cell.setPadding(12);
                tabla.addCell(cell);
            }
            document.add(tabla);
          

            /*texto = "\n\n\n\n\n______________________________________\nRepresentante\nCEGE CAPITAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.setAlignment(Chunk.ALIGN_CENTER);
             p.setLeading(12);
             document.add(p);
            
             document.newPage();
             texto = "Anexo\nTabla de Amortización\n\n";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.setAlignment(Chunk.ALIGN_CENTER);
             p.setLeading(12);
             document.add(p);
            
             int colAmort [] = {8,12,10,10,10,10,10,10,10,10};
             tabla = new PdfPTable(10);
             tabla.setWidthPercentage(100);
             tabla.setWidths(colAmort);
             texto = "PAGO";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "FECHA";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "SALDO INICIAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "ABONO CAPITAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "SALDO CAPITAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "COMISIÓN INICIAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "IVA COMISIÓN";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "INTERÉS";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "IVA INTERÉS";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "MONTO A PAGAR";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
            
             for(int i=0;i<=ciclo.saldo.getPlazo();i++){
             texto = ""+i;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             if (i%2==0){
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].fechaPago;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].saldoInicial;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].abonoCapital;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].saldoCapital;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].comisionInicial;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].ivaComision;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].interes;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].ivaInteres;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].montoPagar;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             }
             else{
             cell.setBackgroundColor(grayLightColor);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].fechaPago;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].saldoInicial;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].abonoCapital;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].saldoCapital;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].comisionInicial;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].ivaComision;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].interes;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].ivaInteres;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.tablaAmortizacion[i].montoPagar;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setBackgroundColor(grayLightColor);
             cell.setBorderWidth(0);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             }
             } 
             document.add(tabla);
            
             p = new Paragraph();
             texto = "\n\nLo anterior, es sin considerar los accesorios financieros que al efecto se generen de conformidad con el presente contrato.\n\n";
             texto += "1. DOMICILIO DE PAGO ACCESORIO.- En caso que la Institución de Crédito no pueda ofrecer el servicio o se niega por alguna causa la imposibilidad de recibir el pago, en este acto CEGE CAPITAL indica el siguiente domicilio para realizar el pago correspondiente: ";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "                                                                                                                                   ";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "\n\n2. COSTO ANUAL TOTAL (CAT).- Para los efectos informativos aplicables el CAT de conformidad con el cálculo de las disposiciones hoy en vigor asciende a: ";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = cat;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = "%\n\n3. CUENTAS BANCARIAS PARA LA REALIZACIÓN DE PAGOS. Para la realización de pagos, las cuentas bancarias de CEGE CAPITAL son:\n\n";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             document.add(p);
            
             int colCuentas [] = {30,35,35};
             tabla = new PdfPTable (3);
             tabla.setWidthPercentage(80);
             tabla.setWidths(colCuentas);
             texto = "CUENTAS BANCARIAS";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             cell.setColspan(3);
             tabla.addCell(cell);
             texto = "BANCO";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "REFERENCIA";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "No. SERVICIO/CUENTA";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
            
             texto = "Banorte";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = ""+ciclo.referencia;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             texto = "No. SERVICIO/CUENTA";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tabla.addCell(cell);
             document.add(tabla);
             * 
             */
 /*
             * texto = "                                                                                          CONTRATO DE CRÉDITO SIMPLE                                                                                          \n";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE, Color.GRAY));
             p.setAlignment(Chunk.ALIGN_CENTER);
             p.setFont(bodyFont);
             document.add(p);
            
             texto = "NÚMERO DE REGISTRO DE CONTRATO DE ADHESIÓN: EN TRÁMITE.";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY));
             p.setAlignment(Chunk.ALIGN_LEFT);
             document.add(p);
            
             p= new Paragraph();
             texto = "\nCONTRATO  DE  CRÉDITO  SIMPLE  ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "(el  "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Contrato";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "")  que  celebran,  por  una  parte,  CEGE  CAPITAL,  S. A. P. I.  de  C. V.,  SOFOM,  E. N. R.  (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditante";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), y, por  la  otra  parte, las  personas  que  suscriben  la  Carátula (según  dicho  término  se  define  más  adelante) del  presente  contrato (los  "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditados";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "" y conjuntamente con el Acreditante, las "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Partes";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), de conformidad con lo siguiente:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);            
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "ANTECEDENTES";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p= new Paragraph();
             texto = "\nPRIMERO. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Con anterioridad a la celebración del presente Contrato, los Acreditados presentaron al Acreditante una solicitud de crédito simple (y en su caso, una nueva solicitud de crédito simple ";
             texto +=  "para clientes recurrentes) (la "Solicitud") para la obtención del Crédito (según se define más adelante) en los términos que se establecen en el presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "SEGUNDO. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "El Acreditante, previa revisión y análisis de la Solicitud, así como de la demás información y documentación que fue proporcionada y presentada por los Acreditados, autorizó a su favor la Solicitud.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "DECLARACIONES";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p= new Paragraph();
             texto = "\nPRIMERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Declara el Acreditante, que:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "1.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Es una sociedad anónima promotora de inversión de capital variable que se encuentra legalmente constituida conforme a las normas jurídicas aplicables en los Estados Unidos Mexicanos.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "2.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Para los efectos relacionados con el Contrato, su domicilio se encuentra en Prado Norte 550, Col. Lomas de Chapultepec, Delegación Miguel Hidalgo, C.P. 11000, México, Distrito Federal.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);            
             texto = "3.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "No requiere autorización de la Secretaría de Hacienda y Crédito Público, ni se encuentra sujeto a la supervisión y vigilancia de la Comisión Nacional Bancaria y de Valores, ";
             texto += "para realizar, bajo el carácter de sociedad financiera de objeto múltiple, entidad no regulada, operaciones de crédito como acreditante.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "4.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "El modelo de adhesión del presente Contrato se encuentra en trámite de registro ante la Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "5.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Ha explicado a los Acreditados, la Información del Crédito (según se define adelante) y, por lo tanto, el contenido, alcance y efectos del Contrato, ";
             texto+="y en particular, previo a la celebración del Contrato, hizo del conocimiento de los Acreditados, el CAT.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "6.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Es su intención otorgar el Crédito (según se define más adelante), conforme al presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "\nSEGUNDA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Declaran los Acreditados, que:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "1.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Son personas físicas que cuentan con la capacidad legal suficiente para celebrar el presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "2.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Para los efectos relacionados con el Contrato, su domicilio es aquel que se menciona en la Carátula (según se define más adelante) del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "3.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Comparecen por su propio derecho y han tomado todas las acciones necesarias para la celebración de este Contrato y el cumplimiento de sus obligaciones bajo el mismo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "4.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "La información y documentación que proporcionaron y presentaron al Acreditante, así como la información que asentaron en la Solicitud ";
             texto += "(misma que se incorpora por referencia al presente Contrato), es verdadera, correcta y no se ha visto modificada en forma alguna a la fecha de celebración del mismo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "5.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Cuentan con los recursos económicos para el cumplimiento de sus obligaciones bajo el presente Contrato, mismos que provienen y provendrán de fuentes lícitas.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "6.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Han recibido del Acreditante una explicación de la Información del Crédito, por lo que conocen, comprenden y consienten con el contenido, ";
             texto += "alcance y efectos del Contrato, y en particular, han recibido del Acreditante, previo a la celebración del presente Contrato, una explicación del CAT.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "7.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Las Actividades Permitidas (según se define más adelante) son y serán lícitas.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "8.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Es su intención recibir el Crédito (según se define más adelante), conforme al presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "9.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Es de su conocimiento que el Crédito, podrá ser otorgado con el apoyo de Nacional Financiera, Sociedad Nacional de Crédito, ";
             texto += "Institución de Banca de Desarrollo, exclusivamente para fines del desarrollo nacional.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "\nTERCERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Declaran las Partes, que:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "1.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Se reconocen la capacidad jurídica con las que, respectivamente, comparecen para celebrar el Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "2.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "La Solicitud, así como toda aquella información y documentación que los Acreditados proporcionaron y presentaron al Acreditante, son integrantes, para los efectos conducentes, del Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "DEFINICIONES";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p = new Paragraph();
             texto = "\n1.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Salvo que de otra forma se defina en este Contrato o que conforme a reglas de ortografía constituya sustantivo, ";
             texto += "los siguientes vocablos con mayúscula inicial tendrán el significado que a continuación se indica:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditados Preferentes";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquellas personas que se indican en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditados Fallecidos";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Cuarta.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Actividades Permitidas";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan aquellas actividades comerciales de los Acreditados que fueron reveladas en la Solicitud y tomadas en cuenta para el otorgamiento del Crédito.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Aseguradora del Seguro Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquella persona que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Aseguradora del Seguro Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquella persona que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Banco de Disposición";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la institución de crédito que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Beneficiarios";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa, según corresponda, el Beneficiario del Seguro Obligatorio en Primer Lugar, los Beneficiarios del Seguro Obligatorio en Segundo Lugar, y/o los Beneficiarios del Seguro Adicional.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Beneficiarios del Seguro Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan aquellas personas que se indican en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Beneficiario del Seguro Opcional en Primer Lugar";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquella persona que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Beneficiarios del Seguro Opcional en Segundo Lugar";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan aquellas personas que se indican en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Carátula";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la carátula integrante del presente Contrato donde se establece la Información del Crédito, los ajustes por ausencia y la celebración del presente Contrato (incluyendo, en su caso, la Carátula Ajustada y/o la Carátula Complementaria).\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Carátula Ajustada";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el documento integrante de la Carátula donde se ajusta la Información del Crédito por ausencia de algún Acreditado.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Carátula Complementaria";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el documento integrante de la Carátula donde se amplía la línea del Crédito hasta por el Monto Adicional del Crédito.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "CAT";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el costo anual total de financiamiento expresado en términos porcentuales anuales que para fines informativos ";
             texto += "y de comparación se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Comisión por Pago Anticipado";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el porcentaje o importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Comisión por No Disposición";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el porcentaje o importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "CONDUSEF";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la línea de crédito otorgada por el Monto del Crédito, y en su caso, el Monto Adicional del Crédito, conforme a los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Día Hábil";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa cualquier día de la semana, excluyendo los días sábado y domingo, así como los días en que, conforme a las disposiciones aplicables, ";
             texto+="las instituciones de crédito ubicadas en la Ciudad de México, Distrito Federal, deban cerrar sus puertas, suspender operaciones y la prestación de servicios al público.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Domicilio del Equipo";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la dirección indicada en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Estado de Cuenta Final";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Fecha de Inicio de la Terminación";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Fechas de Pago";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los días en que los Acreditados deberán pagar al Acreditante la Parcialidad correspondiente según se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Fechas de Pago Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los días en que los Acreditados deberán pagar al Acreditante la Parcialidad Adicional correspondiente según se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Gastos de Cobranza";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Garantía";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Tercera del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Información del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa los Términos y Condiciones Aplicables del Crédito y la Información Adicional del Crédito.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Información Adicional del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquella información especificada como tal en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Instrumentos de Disposición";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa (i) transferencias automatizadas de pagos; (ii) cheques (electrónicos o no); (iii) tarjetas de débito; y (iv) cualquier otro instrumento de pago que sea utilizado por el Banco de Disposición.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Intereses Moratorios";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe que resulte de multiplicar la Tasa de Interés Moratorio por cualquier cantidad que los Acreditados no paguen a la Acreditante en forma oportuna y/o completa conforme al presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Intereses Ordinarios";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe que resulte de multiplicar la Tasa de Interés Ordinario por Principal.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Intereses Preferentes";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe que resulte de multiplicar la Tasa de Interés Preferente por Principal Adicional.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importe Adicional para Cliente Preferente";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la línea de crédito adicional otorgada conforme a los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes en Exceso";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes Parciales de la Garantía";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los importes indicados en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes Parciales del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los importes indicados en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes Parciales Adicionales del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los importes indicados en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes Pendientes por Restituir";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "IVA";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el impuesto al valor agregado correspondiente en los Estados Unidos Mexicanos.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Líder del Equipo";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa la(s) persona(s) que se señala(n) en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Líder Sustituto";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el Acreditado que el Acreditante designe de tiempo en tiempo, a su entero juicio y discreción.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Medio de Comprobación de Pagos";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa: (i) las fichas de recepción de pagos con sello de recibido del cajero del Receptor de Pagos (cuando éste sea una institución de crédito); (ii) las fichas de recepción de pago de los establecimientos, tiendas o sucursales del Receptor ";
             texto += "de Pagos con código de barras (cuando éste no sea una institución de crédito), debidamente selladas y anexando el ticket correspondiente; o (iii) cualquier otra documentación satisfactoria a juicio del Acreditante.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Monto del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Monto Adicional del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Monto Total a Pagar";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Monto Total Adicional a Pagar";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Número de Referencia";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el número indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Parcialidades";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los importes que los Acreditados deberán pagar al Acreditante conforme a los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula y del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Pagaré";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que para dicho término se establece en la Cláusula Quinta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Pagaré Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que para dicho término se establece en la Cláusula Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Parcialidades Adicionales";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significan los importes que los Acreditados deberán pagar al Acreditante conforme a los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo de Disposición";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que para dicho término se establece en la Cláusula Quinta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo de Restitución";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo de Terminación";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Sexta del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Prima";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa, según corresponda, la Prima del Seguro Adicional y/o la Prima del Seguro Obligatorio.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Prima del Seguro Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Prima del Seguro Obligatorio";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el importe indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Principal";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el monto que resulte de sustraer la suma de la Tasa de Interés Ordinaria más IVA a una Parcialidad.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Principal Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el monto que resulte de sustraer la suma de la Tasa de Interés Preferente más IVA a una Parcialidad Adicional.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Receptor de Pagos";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa cualquiera de las personas que se indican en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Referencias";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa los números, folios, órdenes o instrucciones de, o relativas a, los Instrumentos de Disposición.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa, según corresponda, el Seguro Adicional y/o Seguro Opcional.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Quinta.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Tiene el significado que se establece en la Cláusula Décima Cuarta.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Tasa de Interés Ordinario";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el porcentaje mensual de interés ordinario indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Tasa de Interés Moratorio";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el porcentaje mensual de interés moratorio indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Tasa de Interés Preferente";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa el porcentaje mensual de interés ordinario indicado en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = """;
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Términos y Condiciones Aplicables del Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "". Significa aquella información especificada como tal en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "2.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Todos los Anexos del presente Contrato forman parte integral del mismo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "3.     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Los términos definidos en plural, cuando sean expresados en singular, significarán cualquier unidad de entre las que comprende el plural de dicho término.  Los términos definidos en singular, cuando sean expresados en plural, ";
             texto += "significan todas las unidades que pertenecen a la especie a la cual se refiere el singular. Consecuentemente, las palabras y términos aplicarán igualmente para el singular como para el plural de dicha palabra o término.\n\n";
             texto += "En virtud de lo anterior, las Partes otorgan y se sujetan a las siguientes:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
                        
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "CLÁUSULAS";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p= new Paragraph();
             texto = "\nPRIMERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Información del crédito.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             System.out.println(""+p.getFont().toString());
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " La Información del Crédito establecida en la Carátula determina la aplicación del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "En caso de contradicción entre el presente Contrato y la Carátula, prevalecerá lo dispuesto en la Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "SEGUNDA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Otorgamiento del Crédito.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " En la fecha de celebración del presente Contrato, el Acreditante otorga una línea de crédito hasta por el Monto del Crédito a los Acreditados.\n\n";
             texto += " La línea de crédito se podrá incrementar hasta por el Monto Adicional del Crédito, según se indique en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula.  En caso de que no se indique en los Términos y Condiciones ";
             texto += "Aplicables del Crédito que se muestran en la Carátula o se indique como que no aplica, el Acreditante a su juicio podrá incrementar la línea de crédito hasta por el Monto Adicional del Crédito mediante la Solicitud correspondiente.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "TERCERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Destino del Crédito.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " El Crédito sólo podrá destinarse a las Actividades Permitidas de los Acreditados.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "CUARTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Obligación Solidaria.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados son obligados solidarios respecto del cumplimiento de cualquier obligación que, en términos del presente Contrato, les corresponda. Por lo tanto, el Acreditante podrá ";
             texto += "exigir indistintamente a cualquiera de los Acreditados el cumplimiento total de la obligación de que se trate, incluyendo el pago del Monto Total a Pagar y, en su caso, del Monto Adicional a Pagar.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "QUINTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Disposición de la Parte del Crédito Original.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados instruyen y autorizan al Acreditante, en forma expresa e irrevocable, para que el Monto del Crédito, ";
             texto += "dividido conforme a los Importes Parciales del Crédito, sea entregado a cada uno de ellos mediante el Instrumento de Disposición que el Acreditante determine conveniente.\n\n";
             texto += "El Acreditante cumple con la obligación a su cargo de otorgar el Monto del Crédito en términos del presente Contrato, mediante ";
             texto += "(i) depósito del Monto del Crédito en el Banco de Disposición para disposición de los Acreditados; y (ii) la entrega de Referencias a los Acreditados.\n\n";
             texto += "El Acreditante entrega en este acto a los Acreditados las Referencias, quienes acusan su recepción a su entera satisfacción.\n\n";
             texto += "Los Acreditados, dentro de los tres Días Hábiles inmediatos siguientes a la fecha de celebración del presente Contrato (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo de Disposición";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), deberán presentarse en cualquier sucursal del Banco de Disposición con las Referencias, para llevar a cabo la disposición total del Importe Parcial del Crédito que les corresponda. ";
             texto += "No se podrán hacer disposiciones parciales del Importe Parcial del Crédito que corresponda.\n\n";
             texto += "Como consecuencia del otorgamiento del Monto del Crédito, los Acreditados estarán obligados a pagar al Acreditante el Monto Total a Pagar, en Parcialidades.\n\n";
             texto += "Los Acreditados, en este acto, suscriben a su cargo y a favor del Acreditante, un pagaré cuyo importe asciende al Monto Total a Pagar ";
             texto += "y, el que a su vez, documenta la disposición que realizan los Acreditados sobre el Monto del Crédito (en adelante, el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Pagaré";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""). El Acreditante acusa la recepción del Pagaré, a su entera satisfacción.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "SEXTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Disposición de la Parte del Crédito Adicional para Cliente Preferente.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Según se indique en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, los Acreditados instruyen y autorizan al Acreditante, en forma expresa e irrevocable, para que el Monto Adicional del Crédito, ";
             texto += "dividido conforme a los Importes Parciales Adicionales del Crédito, sea entregado a los Acreditados Preferentes mediante el Instrumento de Disposición que el Acreditante determine conveniente.\n\n";
             texto += "El Acreditante cumple con la obligación a su cargo de otorgar el Monto Adicional del Crédito en términos del presente Contrato, mediante ";
             texto += "(i) depósito del Monto Adicional del Crédito en el Banco de Disposición para disposición de los Acreditados Preferentes; y (ii) la entrega de Referencias a los Acreditados Preferentes.\n\n";
             texto += "El Acreditante entrega en este acto a los Acreditados Preferentes las Referencias, quienes acusan su recepción a su entera satisfacción.\n\n";
             texto += "Los Acreditados Preferentes, dentro del Plazo de Disposición, deberán presentarse en cualquier sucursal del Banco de Disposición con las Referencias, para llevar a cabo ";
             texto += "la disposición total del Importe Parcial Adicional del Crédito que les corresponda. No se podrán hacer disposiciones parciales del Importe Parcial Adicional del Crédito que corresponda.\n\n";
             texto += "Como consecuencia del otorgamiento del Monto Adicional del Crédito, los Acreditados estarán obligados a pagar al Acreditante el Monto Total Adicional a Pagar, en Parcialidades Adicionales.\n\n";
             texto += "Los Acreditados, en este acto, suscriben a su cargo y a favor del Acreditante, un pagaré cuyo importe asciende al Monto Total Adicional a Pagar y, el que a su vez, documenta ";
             texto += "la disposición que realizan los Acreditados sobre el Monto Adicional del Crédito (en adelante, el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Pagaré Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""). El Acreditante acusa la recepción del Pagaré Adicional, a su entera satisfacción.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "SÉPTIMA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Omisión en la Disposición de la Parte del Crédito Original.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " La omisión en la disposición del Monto del Crédito por parte de los Acreditados no liberará a éstos de su obligación de pagar al Acreditante el Monto Total a Pagar.\n\n";
             texto += "No obstante lo anterior, en caso de que uno o más de los Acreditados no acudan con sus Referencias a disponer del Importe Parcial del Crédito que les corresponda, dentro del ";
             texto += "Plazo de Disposición, dichos Acreditados por este medio instruyen irrevocablemente al Acreditante para que los montos no dispuestos se destinen al pago del Principal, al momento de vencimiento ";
             texto += "del Plazo de Disposición. En este caso, el Acreditante deberá registrar la anotación correspondiente en el Pagaré y poner a disposición del Líder del Equipo el recibo correspondiente en el ";
             texto += "domicilio del Acreditante. El Líder del Equipo se obliga a recoger dicho recibo dentro de los cinco Días Hábiles al vencimiento del Plazo de Disposición.\n\n";
             texto += "La omisión en la disposición causará el pago de la Comisión por No Disposición. Cada Acreditado instruye que, para el caso de que no disponga del ";
             texto += "Importe Parcial del Crédito que le corresponde, el Acreditante aplique el Importe Parcial de la Garantía correspondiente a dicho Acreditado, como pago de la Comisión por No Disposición.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "OCTAVA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Omisión en la Disposición de la Parte del Crédito Adicional para Cliente Preferente.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " La omisión en la disposición del Monto Adicional del Crédito por parte de los Acreditados Preferentes no liberará a los Acreditados de su obligación de pagar al Acreditante el Monto Total Adicional a Pagar.\n\n";
             texto += "No obstante lo anterior, en caso de que uno o más de los Acreditados Preferentes no acudan con sus Referencias a disponer del Importe Parcial Adicional del Crédito que les corresponda, ";
             texto += "dentro del Plazo de Disposición, los Acreditados por este medio instruyen irrevocablemente al Acreditante para que los montos no dispuestos se destinen al pago del Principal Adicional, al ";
             texto += "momento de vencimiento del Plazo de Disposición. En este caso, el Acreditante deberá registrar la anotación correspondiente en el Pagaré Adicional y poner a disposición del Líder del Equipo ";
             texto += "el recibo correspondiente en el domicilio del Acreditante. El Líder del Equipo se obliga a recoger dicho recibo dentro de los cinco Días Hábiles al vencimiento del Plazo de Disposición.\n\n";
             texto += "La omisión en la disposición de la Parte del Crédito Adicional para Cliente Preferente causará el pago de la Comisión por No Disposición. Cada Acreditado instruye que, para el caso de que ";
             texto += "no disponga del Importe Parcial Adicional del Crédito que le corresponde, el Acreditante aplique el Importe Parcial de la Garantía correspondiente a dicho Acreditado, como pago de la Comisión por No Disposición.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "NOVENA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Tiempo de Pago.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Las Parcialidades deberán pagarse por los Acreditados al Acreditante en la Fecha de Pago correspondiente. En caso de que la Fecha de Pago no sea Día Hábil, el pago deberá efectuarse el Día Hábil inmediato anterior ";
             texto += "al de la Fecha de Pago. En caso de que una Parcialidad no sea pagada en la Fecha de Pago correspondiente, los Acreditados estarán obligados a pagar Gastos de Cobranza e Intereses Moratorios.\n\n";
             texto += "Las Parcialidades Adicionales deberán pagarse por los Acreditados al Acreditante en la Fecha de Pago Adicional correspondiente. En caso de que la Fecha de Pago Adicional no sea Día Hábil, el pago deberá efectuarse el ";
             texto += "Día Hábil inmediato anterior al de la Fecha de Pago Adicional. En caso de que una Parcialidad Adicional no sea pagada en la Fecha de Pago Adicional correspondiente, los Acreditados estarán obligados a pagar Gastos de Cobranza e Intereses Moratorios.\n\n";
             texto += "Los Gastos de Cobranza que se generen, incluyendo el IVA que corresponda, deberán pagarse por los Acreditados al Acreditante en forma inmediata, en Día Hábil y, ";
             texto += "Lsegún sea el caso, junto con el importe que termine de cubrir cualesquier cantidades pendientes de pago de cada Parcialidad o Parcialidad Adicional, según sea el caso.\n\n";
             texto += "Los Intereses Moratorios que se generen, incluyendo el IVA que corresponda, deberán pagarse por los Acreditados al Acreditante en forma inmediata, en Día Hábil y, ";
             texto += "según sea el caso, junto con el importe que termine de cubrir cualesquier cantidades pendientes de pago de cada Parcialidad o Parcialidad Adicional, según sea el caso.\n\n";
             texto += "El Acreditante, a su entero juicio y discreción, podrá conceder un plazo de gracia que determine conveniente, una vez verificado el incumplimiento de los Acreditados, ";
             texto += "para que los Acreditados lleven a cabo el cumplimiento de cualquier obligación que, en términos del Contrato, se encuentra establecida a su cargo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Medios de Pago.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Las Parcialidades, las Parcialidades Adicionales, los Gastos de Cobranza,  los Intereses Moratorios y cualquier otro importe que los Acreditados adeuden al Acreditante conforme al ";
             texto += "presente Contrato, deberán pagarse al Acreditante por los Acreditados en el Receptor de Pagos, indicando el Número de Referencia.\n\n";
             texto += "En caso de que el Acreditante identifique un error numérico en el Número de Referencia que utilicen los Acreditados para pagar, los Acreditados autorizan al Acreditante para corregir dicho error numérico.\n\n";
             texto += "Los Acreditados estarán obligados a comprobar, en cualquier momento, al Acreditante, los pagos que sean realizados, mediante cualquier Medio de Comprobación de Pagos.";
             texto += "Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA PRIMERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Aplicación de Pago.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Salvo que de otra forma se establezca en el presente Contrato, cualquier pago que en términos del Contrato sea realizado por los Acreditados será aplicado por el Acreditante para cubrir:\n\n";
             texto += "(i)	En primer lugar y hasta donde alcance, el saldo insoluto del importe de los Gastos de Cobranza de que se trate, en caso de que exista;\n\n";
             texto += "(ii)	En segundo lugar y hasta donde alcance, el saldo insoluto de los Intereses Moratorios de que se trate, en caso de que existan;\n\n";
             texto += "(iii)	En tercer lugar y hasta donde alcance, el saldo insoluto de los Intereses Ordinarios y/o Intereses Preferentes de que se trate; y\n\n";
             texto += "(iv)	Por último y hasta donde alcance, el saldo insoluto de Principal y/o Principal Adicional de que se trate.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA SEGUNDA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Pago Anticipado.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados, siempre que se encuentren al corriente en el cumplimiento de sus obligaciones bajo el presente Contrato, podrán pagar anticipadamente al Acreditante, la totalidad de Principal y/o Principal Adicional por vencer.\n\n";
             texto += "El pago anticipado de la totalidad de Principal y/o Principal Adicional:\n\n";
             texto += "(i) 	Causará el pago de la Comisión por Pago Anticipado;\n\n";
             texto += "(ii) 	Deberá realizarse por el importe íntegro de la totalidad de Principal y/o Principal Adicional por vencer; y\n\n";
             texto += "(iii) 	Causará la terminación del Contrato, en términos de la Cláusula Décima Sexta.\n\n";
             texto += "Por este medio, los Acreditados, autorizan al Acreditante para que éste, a su entero juicio y discreción, aplique el importe correspondiente de la Garantía como pago de Principal y, ";
             texto += "en su caso, de Principal Adicional, más la Comisión por Pago Anticipado correspondiente, en todo o en parte, hasta donde alcance. Dicha aplicación se entenderá como el ";
             texto += "consentimiento del Acreditante a la extinción de la Garantía. Para el caso en que la aplicación del importe correspondiente de la Garantía conforme a lo anterior extinga la ";
             texto += "obligación de pago del Monto Total a Pagar y, en su caso, del Monto Total Adicional a Pagar, ello causará la terminación del Contrato, en términos de la Cláusula Décima Sexta.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA TERCERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Garantía Prendaria.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados constituyen una prenda de dinero en efectivo, en primer lugar y grado, a favor del Acreditante, hasta por el importe equivalente al porcentaje del ";
             texto += "Monto del Crédito que se señala en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula (la "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Garantía";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), por alguno de los siguientes medios según se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula:\n\n";
             texto += "(i) 	";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Garantía con pago único.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " En la fecha de celebración del presente Contrato, los Acreditados entregan al Acreditante la Garantía, en la forma que se indica en los ";
             texto += "Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos, según se demuestra con Medios de Comprobación de Pagos.\n\n";
             texto += "(ii) 	";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Garantía con pagos parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " En las respectivas Parcialidades, de manera consecutiva e ininterrumpida, los Acreditados entregarán al Acreditante la Garantía, en la forma que se indica en los ";
             texto += "Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. Los Acreditados estarán obligados a comprobar, en cualquier ";
             texto += "momento, al Acreditante, las entregas que sean realizadas, mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             texto += "(iii) 	";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Garantía con Pago inicial y Pagos Parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " En la fecha de celebración del presente Contrato, los Acreditados entregan al Acreditante, un pago inicial a cuenta de la Garantía por el monto que se establece en los ";
             texto += "Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. Posteriormente, en las respectivas Parcialidades, de manera consecutiva ";
             texto += "e ininterrumpida, los Acreditados entregarán al Acreditante el remanente de la Garantía en la forma que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran ";
             texto += "en la Carátula, mediante pago en el Receptor de Pagos. Los Acreditados estarán obligados a comprobar, en cualquier momento, al Acreditante, las entregas que sean realizadas, ";
             texto += "mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             texto += "La Garantía, ya sea a través de pago único, pagos parciales, o pago inicial y pagos parciales, se constituye para garantizar por el incumplimiento en el pago de:\n\n";
             texto += "(i) 	El Monto Total a Pagar y/o del Monto Total Adicional a Pagar, en su caso;\n\n(ii)	Cualquier otro importe que los Acreditados adeuden al Acreditante conforme al presente Contrato; y\n\n";
             texto += "(iii) 	Los gastos y costas que se causen por cualquier interpelación notarial o judicial, honorarios de agencias de cobranza u otros procedimientos judiciales ";
             texto += "o extrajudiciales que, en su caso, sean iniciados por el Acreditante en contra de los Acreditados, para requerirles el cumplimiento de sus obligaciones conforme al presente Contrato.\n\n";
             texto += "En caso de que la Garantía sea insuficiente para el pago de lo establecido anteriormente, los Acreditados pagarán a favor del Acreditante, los saldos insolutos que resulten a su cargo.\n\n";
             texto += "La Garantía permanecerá vigente durante el tiempo que subsistan las obligaciones que, en términos del Contrato, sean a cargo de los Acreditados. La Garantía transmite la propiedad del dinero en efectivo en términos del ";
             texto += "segundo párrafo del artículo 336 de la Ley General de Títulos y Operaciones de Crédito, por lo que la misma no generará, a favor de los Acreditados, interés, rendimiento o ganancia alguna.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA CUARTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Seguro Opcional.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Cada Acreditado de manera opcional puede instruir y autorizar al Acreditante para que, por cuenta de dicho Acreditado, el Acreditante contrate un seguro individual con la Aseguradora ";
             texto += "del Seguro Opcional, a favor de los Beneficiarios, que cubra los riesgos que se establecen en la póliza y condiciones generales del Seguro Opcional correspondientes ";
             texto += "respecto de dicho Acreditado (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto= "") mediante alguno de los siguientes medios, según se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula:\n\n(i)        ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Opcional por pago único.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En la fecha de celebración del presente Contrato, la Prima del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " será pagada por cada uno de los Acreditados que así lo hayan solicitado, en su totalidad, mediante pago en el Receptor de Pagos, según se demuestra con Medios de Comprobación de Pagos.\n\n(ii)      ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Opcional con pagos parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En las respectivas Fechas de Pago, de manera consecutiva e ininterrumpida, los Acreditados que así lo hayan solicitado pagarán al Acreditante la Prima del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ", en la forma que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. Los Acreditados estarán obligados a comprobar, ";
             texto += "en cualquier momento, al Acreditante, los pagos que sean realizados, mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n(iii)     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Opcional con Pago Inicial y Pagos Parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En la fecha de celebración del presente Contrato, los Acreditados que lo hayan solicitado entregarán al Acreditante, un pago inicial a cuenta de la Prima del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " por el monto que se establece en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. ";
             texto += "Posteriormente, en las Fechas de Pago, de manera consecutiva e ininterrumpida, los Acreditados entregarán al Acreditante el remanente de la Prima del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " en la forma que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. Los Acreditados estarán obligados ";
             texto += "a comprobar, en cualquier momento, al Acreditante, las entregas que sean realizadas, mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             texto += "La Prima del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " no es integrante del Crédito y por lo tanto del Monto Total a Pagar y/o Monto Total Adicional a Pagar, en su caso. La vigencia, coberturas, exclusiones, condiciones y términos relativos y aplicables al Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ", se regirán por la póliza, los certificados individuales, así como por las condiciones generales del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ".\n\nLa vigencia del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " deberá comenzar, a más tardar a partir de la fecha de disposición del Importe Parcial del Crédito correspondiente.\n\n";
             texto += "En caso del fallecimiento de cualquiera de los Acreditados (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditado Fallecido";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), la suma asegurada por el Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " será entregada por la Aseguradora del Seguro Opcional al Beneficiario del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " al Beneficiario del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " en Primer Lugar, para pagar, hasta donde alcance, el saldo insoluto, a la fecha del fallecimiento del Acreditado Fallecido, ";
             texto += "del Importe Parcial y/o Importe Parcial Adicional, en su caso, del Acreditado de que se trate. El remanente se le dará a los Beneficiarios del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " en Segundo Lugar. El pago que se haga en estos términos con cargo a la suma asegurada no generará Comisión por Pago Anticipado.\n\n";
             texto += "Los Acreditados supervivientes, continuarán obligados, en lo conducente, al cumplimiento del Contrato.\n\nEl Acreditante explicó a los Acreditados, las coberturas, exclusiones y alcance del Seguro ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Opcional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " a que se refiere la presente cláusula del Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA QUINTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Seguro Adicional.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Cada Acreditado, según corresponda, instruye y autoriza al Acreditante para que, por cuenta de dicho Acreditado, contrate un seguro individual con la Aseguradora del ";
             texto += "Seguro Adicional, a favor de los Beneficiarios del Seguro Adicional, que cubra los riesgos que indique la póliza correspondiente respecto de dicho Acreditado (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicional";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " ") mediante alguno de los siguientes medios, según se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula:\n\n";
             texto += "(i)       ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicionalpor pago único.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En la fecha de celebración del presente Contrato, la Prima del Seguro Adicional es pagada por el Acreditado correspondiente, en su totalidad, mediante pago en el Receptor de Pagos, según se demuestra con Medios de Comprobación de Pagos.\n\n";
             texto += "(ii)      ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicional con pagos parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En las respectivas Fechas de Pago, de manera consecutiva e ininterrumpida, los Acreditados pagarán al Acreditante la Prima del Seguro Adicional, en la forma que se indica en los ";
             texto += "Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante el pago en el Receptor de Pagos. Los Acreditados estarán obligados a comprobar, en cualquier momento, ";
             texto += "al Acreditante, los pagos que sean realizados, mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             texto += "(iii)     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicional con pago inicial y pagos parciales.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En la fecha de celebración del presente Contrato, los Acreditados entregan al Acreditante, un pago inicial a cuenta de la Prima del Seguro Adicional, por el monto que se establece en los Términos y Condiciones Aplicables ";
             texto += "del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. Posteriormente, en las respectivas Fechas de Pago, de manera consecutiva e ininterrumpida, los Acreditados entregarán al ";
             texto += "Acreditante el remanente de la Prima del Seguro Obligatorio en la forma que se indica en los Términos y Condiciones Aplicables del Crédito que se muestran en la Carátula, mediante pago en el Receptor de Pagos. ";
             texto += "Acreditados estarán obligados a comprobar, en cualquier momento, al Acreditante, las entregas que sean realizadas, mediante cualquier Medio de Comprobación de Pagos. Los Acreditados deberán ";
             texto += "conservar los Medios de Comprobación de Pagos durante la vigencia del presente Contrato.\n\n";
             texto += "(iv)     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Seguro Adicional gratuito.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " En la fecha de celebración del presente Contrato, el Acreditante se obliga a contratar, sin cargo para los Acreditados, el Seguro Adicional.\n\n";
             texto += "La Prima del Seguro Adicional no es integrante del Crédito y por lo tanto del Monto Total a Pagar y/o Monto Total Adicional a Pagar, en su caso. La vigencia, coberturas, exclusiones, ";
             texto += "condiciones y términos relativos y aplicables al Seguro Adicional, se regirán por la póliza, los certificados individuales, así como por las condiciones generales del Seguro Adicional.\n\n";
             texto += "La vigencia del Seguro Adicional deberá comenzar a partir de la fecha de disposición del Importe Parcial del Crédito correspondiente.\n\n";
             texto += "En caso de que se actualice alguno de los riesgos que cubre la póliza del Seguro Adicional, la suma asegurada por el Seguro Adicional será entregada por la Aseguradora del Seguro Adicional al Beneficiario del Seguro Adicional.\n\n";
             texto += "El Acreditante explicó a cada Acreditado, según corresponda, las coberturas, exclusiones y alcance del Seguro Adicional a que se refiere la presente cláusula del Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA SEXTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Efectos del cumplimiento del Contrato.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " A partir del día en que el Monto Total a Pagar y/o el Monto Total Adicional a Pagar, en su caso, sea pagado en su totalidad en los términos del presente Contrato (la "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Fecha de Inicio de la Terminación";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), se producirán los siguientes efectos:\n\n(i)       ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Emisión del Estado de Cuenta Final.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " Dentro de los 30 Días Hábiles  siguientes a la Fecha de Inicio de la Terminación (el  "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo  de  Terminación";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), el Acreditante publicará un estado de cuenta (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Estado de Cuenta Final";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), en términos de la Cláusula siguiente, que evidencie: (i) la terminación de la relación contractual; y (ii) la inexistencia de adeudos entre las Partes. Dentro de los 15 Días Hábiles siguientes al Plazo de Terminación (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Plazo de Restitución";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), el Líder del Equipo deberá acudir con el Acreditante a obtener el Estado de Cuenta Final.\n\n(ii)      ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Cancelación del Pagaré y, en su caso, del Pagaré Adicional.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " Dentro del Plazo de Terminación, el Acreditante procederá a la cancelación del Pagaré y, en su caso, del Pagaré Adicional. Dentro del Plazo de Restitución, el Líder del ";
             texto += "Equipo deberá presentarse en el domicilio del Acreditante para recibir una copia del Pagaré y, en su caso, del Pagaré Adicional, debidamente cancelados.\n\n(iii)     ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Restitución de importes pagados y/o entregados en exceso por los Acreditados.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " Dentro del Plazo de Restitución, el Acreditante procederá a la restitución de importes pagados y/o entregados en exceso por los Acreditados (los "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Importes en Exceso";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "").\n\nPara tal efecto, el Líder del Equipo deberá presentarse en el domicilio del Acreditante para recibir los Importes en Exceso, de conformidad con el Estado de Cuenta Final. La sumatoria de dichas cantidades ";
             texto += "será puesta a disposición del Líder del Equipo, para que éste en representación de los Acreditados la retire y la entere a los Acreditados que correspondan. Dicha disposición podrá ser efectuada a través del ";
             texto += "Instrumento de Disposición que el Acreditante considere conveniente, mediante la entrega de las Referencias correspondientes por parte del Acreditante en su domicilio contra recibo de conformidad por el Líder del Equipo.\n\n";
             texto += "A partir de la recepción de las Referencias, el Líder del Equipo será el único y exclusivo responsable de la entrega y custodia de los Importes en Exceso. En consecuencia, el Líder del Equipo ";
             texto += "deberá reunirse con los Acreditados, solicitarles los Medios de Comprobación, y entregarle a cada uno, bajo su estricta responsabilidad, las cantidades que cada Acreditado hubiere pagado en exceso, en su caso.\n\n";
             texto += "Si el Líder del Equipo no acude a cualquiera de los actos previstos en los párrafos (i), (ii) y (iii) de la presente Cláusula, podrá acudir en su lugar el Líder Sustituto, a quien desde este ";
             texto += "momento los Acreditados autorizan y facultan con las mismas facultades con que actualmente cuenta el Líder del Equipo en términos de la Cláusula Décima Octava.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA SÉPTIMA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Estados de Cuenta, Unidad Especializada y Atención a Usuarios.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados, respecto de los estados de cuenta que sean relativos al Crédito, eligen como medio para su consulta el acceso a la dirección de Internet del Acreditante que se encuentra ubicada en ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "http://estadodecuenta.crediequiposcontigo.mx";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " mediante la clave que se indica en la Información Adicional contenida en la Carátula. El Acreditante, tendrá los estados de cuenta a disposición de los Acreditados, en forma mensual ";
             texto += "y, sin costo alguno a cargo de los Acreditados, dentro de los 10 Días Hábiles siguientes a la fecha de cierre del período mensual de que se trate.\n\n";
             texto += "Los Acreditados contarán con un período de 90 días naturales, contado a partir de la fecha de publicación del respectivo estado de cuenta, para formular, por escrito y, a través del Representante, cualquier aclaración, inconformidad, ";
             texto += "reclamación o queja con respecto a la información contenida en el mismo, ante la Unidad Especializada de Consultas y Reclamaciones del Acreditante, cuyos datos de localización y contacto se establecen en el";
             texto += "párrafo inmediato siguiente o, en el domicilio del Acreditante indicado en el inciso (iii) del párrafo inmediato siguiente; en caso contrario, se entenderá que dicha información es aceptada en los términos en los que se publica.\n\n";
             texto += "Para cualquier aclaración, inconformidad, reclamación o queja que se relacione con el Crédito, los Acreditados contarán con un período de 90 días naturales, contado a partir de la fecha en que tengan conocimiento ";
             texto += "del acto u omisión que la motive, para formularla, por escrito y, a través del Líder del Equipo, ante el Titular de la Unidad Especializada de Consultas y Reclamaciones del Acreditante (el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Titular";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = " "), en Prado Norte 550, Col. Lomas de Chapultepec, Delegación Miguel Hidalgo, C.P. 11000, México, Distrito Federal, teléfono 41-60-21-00 ó 01 800 837 8760, en un horario de atención ";
             texto += "de 7:00 a 17:00 horas, de lunes a viernes, o a través del correo electrónico info@crediequiposcontigo.mx o a través de la dirección de Internet";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "www.crediequiposcontigo.mx";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "\n\nAsimismo, para cualquier solicitud o consulta que se relacione con el Crédito, los Acreditados podrán formularla, por escrito y, a través del Líder del Equipo: (i) ante el Titular; (ii) ante el ";
             texto += "área de atención a clientes, en Prado Norte 550, Col. Lomas de Chapultepec, Delegación Miguel Hidalgo, C.P. 11000, México, Distrito Federal, teléfono 41-60-21-00 ó 01 800 837 8760, ";
             texto += "en un horario de atención de 7:00 a 17:00 horas, de lunes a viernes, o a través del correo electrónico info@crediequiposcontigo.mx o a través de la dirección de Internet ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "www.crediequiposcontigo.mx";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = "\n\nDe igual forma, se hace del conocimiento de los Acreditados, el número telefónico de atención a usuarios: 53-40-09-99 ó 01800-999-8080 ";
             texto += "(lada sin costo), dirección en Internet: www.condusef.gob.mx, y correo electrónico: opinion@condusef.gob.mx, de la CONDUSEF.\n\n";
             texto += "En cumplimiento del artículo 5, fracción V, inciso d) de la Disposición Única de la CONDUSEF aplicable a las Entidades Financieras, se realiza la explicación del proceso de aclaración contenido en el artículo 23 ";
             texto += "de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros; por lo que, una vez que el Acreditante reciba cualquier aclaración que sea formulada por los Acreditados conforme a lo establecido en los párrafos anteriores de la ";
             texto += "presente Cláusula, tendrá un plazo de hasta 45 días naturales para entregar a los Acreditados (a través del Representante), el dictamen correspondiente, junto con la información y/o documentación considerada ";
             texto += "para su emisión, así como un informe detallado en el que se respondan los hechos contenidos en la solicitud de aclaración. En caso de que conforme a dicho dictamen resulte procedente el cobro del monto de que se trate, los ";
             texto += "Acreditados deberán hacer el pago de la cantidad a su cargo, incluyendo los intereses ordinarios y excluyendo la Tasa de Interés Moratorio. Dentro del plazo de 45 días naturales contado a partir de la entrega del dictamen de ";
             texto += "referencia, el Acreditante pondrá a disposición de los Acreditados, a través de su Unidad Especializada de Consultas y Reclamaciones, el expediente generado por la solicitud, con la integración de la información y documentación ";
             texto += "que deba obrar en su poder y que se relacione directamente con la solicitud. Hasta en tanto la solicitud de aclaración no sea resuelta, el ";
             texto += "Acreditante  no podrá reportar como vencidas las cantidades sujetas a dicha aclaración a las Sociedades de Información Crediticia.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA OCTAVA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Líder del Equipo y Líder Sustituto.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " En términos del presente Contrato, los Acreditados en este acto autorizan e instruyen al Líder del Equipo (quien en este acto acepta la representación) para que, como su apoderado legal, ";
             texto += "con representación limitada en cuanto a su objeto pero general en cuanto a las facultades que se le confieren, realice toda clase de  pleitos y cobranzas (incluyendo aquellos que requieran ";
             texto += "cláusula especial conforme a la ley) y actos de administración, en términos de los párrafos primero y segundo del artículo 2554 del Código Civil Federal y sus disposiciones ";
             texto += "correlativas y concordantes en las entidades federativas de los Estados Unidos Mexicanos, para llevar a cabo las obligaciones que se le imponen bajo el presente Contrato.\n\n";
             texto += "Asimismo, los Acreditados consienten que el Acreditante podrá designar a un Líder Sustituto de tiempo en tiempo. El Acreditado que sea designado como Líder Sustituto desde este momento acepta el encargo.\n\n";
             texto += "Los Acreditados en este acto autorizan e instruyen al Líder Sustituto, para que, como su apoderado legal, con representación limitada en cuanto a su objeto pero general en cuanto a las facultades que se le confieren, ";
             texto += "realice toda clase de  pleitos y cobranzas (incluyendo aquellos que requieran cláusula especial conforme a la ley) y actos de administración, en términos de los párrafos primero y segundo del artículo 2554 del ";
             texto += "Código Civil Federal y sus disposiciones correlativas y concordantes en las entidades federativas de los Estados Unidos Mexicanos, para llevar a cabo las obligaciones que se le imponen bajo el presente Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "DÉCIMA NOVENA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Buró de Crédito.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Los Acreditados a través de la Solicitud, autorizaron al Acreditante para que solicitara y obtuviera de cualquier Sociedad de Información Crediticia (en adelante, el "";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Buró de Crédito";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.add(ck);
             texto = ""), la información sobre sus respectivos historiales crediticios, respecto de las operaciones crediticias y otras de naturaleza análoga, que los integran. Por lo tanto y, toda vez que los Acreditados, han leído y comprendido ";
             texto += "la naturaleza y alcance del Buró de Crédito y de la información contenida en su base de datos, cualquier incumplimiento de los Acreditados a las obligaciones de pago que a su cargo establece el Contrato, será registrado por el ";
             texto += "Acreditante, en el Buró de Crédito con claves de observación establecidas en los correspondientes reportes de crédito, las cuales podrán afectar los respectivos historiales crediticios de los Acreditados.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Título Ejecutivo.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " El presente Contrato y el estado de cuenta correspondiente que certifique el contador del Acreditante, serán título ejecutivo mercantil, sin necesidad de reconocimiento de ";
             texto += "firma ni de otro requisito alguno, de conformidad con lo establecido por los artículos 87-E y 87-F de la Ley General de Organizaciones y Actividades Auxiliares del Crédito.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA PRIMERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Vigencia.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " La vigencia del Contrato iniciará a partir de su fecha de celebración y terminará una vez concluidos los actos a que se refiere la Cláusula Décima Sexta.\n\n";
             texto += "No obstante lo anterior, las partes convienen en que mientras exista cualquier obligación de pago pendiente de cubrir por parte de cualquiera de los Acreditados, el presente Contrato se mantendrá vigente en sus términos hasta en tanto la misma ";
             texto += "no sea cubierta con todos sus accesorios, incluyendo de manera enunciativa y no limitativa, impuestos, Intereses Ordinarios, Intereses Moratorios, Gastos de Cobranza, Primas, honorarios de agencias de cobranza o gastos y costas.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA SEGUNDA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Vencimiento Anticipado.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Cualquiera de los supuestos que se establecen a continuación dará derecho al Acreditante de declarar vencido anticipadamente el pago de la porción insoluta del Monto Total a Pagar ";
             texto += "y, en su caso, del Monto Total Adicional a Pagar, así como de cualquier otro importe que los Acreditados adeuden al Acreditante conforme al Contrato:\n\n";
             texto += "(i)       La falta de pago por los Acreditados, a su vencimiento o Fecha de Pago, de cualesquier cantidades que adeuden al Acreditante en términos del presente Contrato;\n\n";
             texto += "(ii)      La determinación de que alguna de las Declaraciones que corresponden a los Acreditados resulte ser falsa, incorrecta u omisa a la fecha del presente Contrato o en el futuro;\n\n";
             texto += "(iii)     El incumplimiento por parte de los Acreditados a cualquier otra obligación que, en términos del Contrato, se encuentra establecida a su cargo; y\n\n";
             texto += "(iv)      El estado de insolvencia, declaración de concurso mercantil o incumplimiento generalizado de obligaciones de cualquiera de los Acreditados.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA TERCERA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Impuestos";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = ". Todos los impuestos, contribuciones y derechos que deban cubrirse con motivo de la celebración y ejecución del Contrato, serán cubiertos por la parte que resulte obligada a ello, de conformidad con las disposiciones fiscales aplicables.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA CUARTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Cesión";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = ". Los Acreditados no podrán ceder ni transmitir cualquier derecho u obligación que derive del presente Contrato, sin la previa aprobación ";
             texto += "escrita del Acreditante. Cualquier transmisión en términos distintos a los aquí previstos será nula y no será reconocida por el Acreditante.\n\n";
             texto += "El Acreditante podrá endosar, ceder, transmitir, descontar, transferir, negociar, afectar y/o gravar, en cualquier tiempo y, sin previa autorización de los Acreditados, respectivamente, cualquier derecho que, en términos del Contrato, ";
             texto += "resulte a favor del Acreditante y/o la Garantía y/o los derechos de crédito contenidos en el Contrato y/o en el Pagaré. De igual forma, cualquier causahabiente del Acreditante, podrá realizar lo establecido en el presente párrafo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA QUINTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Leyes Aplicables";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = ". Para la interpretación y cumplimiento del Contrato, éste se regirá por lo dispuesto en sus Cláusulas, en su defecto o supletoriamente, por las disposiciones contenidas y que resulten aplicables ";
             texto += "de la Ley General de Títulos y Operaciones de Crédito, de la Ley General de Organizaciones y Actividades Auxiliares del Crédito, de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros, ";
             texto += "de la Disposición Única de la CONDUSEF aplicable a las Entidades Financieras, del Código de Comercio, del Código Civil Federal, y de las demás leyes federales vigentes en los Estados Unidos Mexicanos.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA SEXTA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Domicilios.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = " Las Partes señalan como sus domicilios para los efectos de notificaciones de carácter extrajudicial o judicial que se relacionen con el Contrato, los enunciados respectivamente en la Carátula del Contrato.\n\n";
             texto += "Sin embargo, las Partes podrán cambiar o señalar en el futuro cualquier otro domicilio, mediante notificación por escrito realizada a la otra Parte con por lo menos 10 Días Hábiles de anticipación, en el entendido que de no proporcionarse tal aviso ";
             texto += "de cambio, todas y cada una de las notificaciones que se le hicieren a la parte de que se trate, en el domicilio que se señala en la Carátula a la fecha de firma del presente Contrato, se considerarán legalmente realizadas para todos los efectos.\n\n";
             texto += "Las notificaciones extrajudiciales que tengan que realizar las Partes, deberán hacerse por escrito, dirigidas a la contraparte, mediante entrega personal con acuse ";
             texto += "de recibo en el respectivo domicilio y surtirán efectos en la fecha en que sean recibidas por la parte a quien hayan sido dirigidas.\n\n";
             texto += "No obstante lo establecido en los párrafos primero y tercero de la presente Cláusula, las notificaciones extrajudiciales que sean relativas al requerimiento del o de los pagos del Crédito a los Acreditados, ";
             texto += "podrán realizarse por el Acreditante, en el domicilio de cualquiera de los Acreditados indistintamente, por escrito y, entregadas en forma personal, sin necesidad de acuse de recibo.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA SÉPTIMA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Encabezados";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = ". Los encabezados en las Cláusulas del Contrato, son exclusivamente por conveniencia de las Partes, para una referencia y lectura más simple, por lo que no regirán la interpretación del Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "VIGÉSIMA OCTAVA. ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = "Jurisdicción";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
             p.setFont(boldFont);
             p.add(ck);
             p.setFont(bodyFont);
             texto = ". Para la interpretación y cumplimiento del Contrato, las Partes se someten de manera expresa a la jurisdicción de los Tribunales Federales competentes con sede en Ciudad de México, ";
             texto += "Distrito Federal, por consiguiente, renuncian a cualquier otra jurisdicción que por razón de su domicilio o cualquier otra causa les corresponda o pudiere corresponderles, ya sea en lo presente o futuro.\n\n";
             texto += "El presente Contrato se celebra mediante la suscripción por las Partes de la Carátula, en la fecha y lugar indicados en dicha Carátula.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             /************************************
             document.newPage();
             p=new Paragraph();
             texto = "Anexo 1\n\n ";
             texto+= "Carátula Complementaria para otorgamiento de la Parte del Crédito Adicional para Cliente Preferente\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_CENTER);
             p.setLeading(12);
             document.add(p);
             p=new Paragraph();
             texto = "NÚMERO DE REGISTRO DE CONTRATOS DE ADHESIÓN: \n\n";
             texto += "LUGAR : "+sucursal.estado+", "+sucursal.municipio+".\n\n";
             fecha = Convertidor.dateToString(ciclo.saldo.getFechaDesembolso());
             mes = FechasUtil.obtenNombreMes(fecha);
             texto += "FECHA : "+ciclo.saldo.getFechaDesembolso().getDate()+" de "+mes.substring(0, 1)+mes.substring(1, mes.length()).toLowerCase()+" de "+FechasUtil.obtenParteFecha(ciclo.saldo.getFechaDesembolso(), 3)+".\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_LEFT);
             p.setLeading(12);
             document.add(p);
             p = new Paragraph();
             texto = "\nCARÁTULA COMPLEMENTARIA DEL CONTRATO DE CRÉDITO SIMPLE";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = " que celebraron CEGE CAPITAL, S.A.P.IB. de C.V., ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
             p=new Paragraph();
             texto = " SOFOM, E.N.R., como ";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "Acreditante";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             ck.setBackground(Color.red, 1, 1, 1, 2);
             p.add(ck);
             texto = " y las personas que suscriben la presente como acreditados, el día  [___] de  [___] de [___] (el "Contrato"), de conformidad con lo siguiente:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setIndentationLeft(25);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "TÉRMINOS DEFINIDOS";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p = new Paragraph();
             texto = "Cualquier término definido que se utilice en el presente documento tendrá el significado que a dicho término se le atribuye en el Contrato.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "ANTECEDENTES";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);            
             p = new Paragraph();
             texto = "PRIMERO.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = " El Acreditante celebró con los Acreditados el Contrato, de conformidad con el cual, el Acreditante otorgó una línea de crédito hasta por el Monto del Crédito a los Acreditados.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "SEGUNDO.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = " Conforme al Contrato, el Acreditante a su juicio podrá incrementar la línea de crédito hasta por el Monto Adicional del Crédito mediante la suscripción y llenado del presente documento.\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             texto = "TERCERO.";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             p.add(ck);
             texto = " El Acreditante, previa revisión y análisis de la información y documentación que fue proporcionada y presentada por los Acreditados, autorizó el otorgamiento del Monto del Crédito Adicional.\n\n";
             texto += "En consecuencia, los términos y condiciones siguientes serán aplicables:\n\n";
             ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             int col[] = {11,11,11,16,17,17,17};
             tabla = new PdfPTable(7);
             tabla.setWidthPercentage(100F);
             tabla.setWidths(col);
            
             texto = "Parte del Crédito Adicional para Cliente Preferente";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenHeaderColor);
             cell.setColspan(7);
             tabla.addCell(cell);
             texto = "CAT(Costo Anual Total)*";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Tasa de Interés Anual Preferente*";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Tasa de Interés Preferente*";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Monto Adicional del Crédito";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Principal Adicional";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Parcialidades Adicionales*";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Monto Total Adicional a Pagar*";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "88% \n\n*Sin IVA y para fines informativos y comparativos";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "88% \n\n*Fija";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "88% \n\n*Fija";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "$8,888,888.00";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "$8,888,888.00";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "$8,888,888.00 \n\n*Con IVA";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "$ "+FormatUtil.formatIntegerMiles(ciclo.saldo.getMontoConIntereses())+" "+mtoDecimal+"/100 M.N.";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "Plazo del Importe Adicional para Cliente Preferente";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Periodicidad de pago de Parcialidades Adicionales";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Número de Parcialidades Adicionales";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Fechas de Pago Adicional";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "Acreditados Preferentes";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             cell.setColspan(2);
             tabla.addCell(cell);
             texto = "Importes Parciales Adicionales";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(greenCellColor);
             tabla.addCell(cell);
             texto = "16";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "16";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "16";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             texto = "2013/04/28";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
            
             tablaInterna = new PdfPTable (1);
             tablaInterna.setWidthPercentage(100F);
             for(int i=0;i<ciclo.integrantes.length;i++){
             texto = ""+ciclo.integrantes[i].nombre;
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             tablaInterna.addCell(cell);
             }            
             cell = new PdfPCell(tablaInterna);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setColspan(2);
             tabla.addCell(cell);
            
             tablaInterna = new PdfPTable (1);
             tablaInterna.setWidthPercentage(100F);
             texto = "$8,888,888.00";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tablaInterna.addCell(cell);
             texto = "$8,888,888.00";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tablaInterna.addCell(cell);
            
             cell = new PdfPCell(tablaInterna);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(cell);
             document.add(tabla);
            
             p = new Paragraph();
             texto = "\nEn virtud de lo anterior, resultan aplicables, a partir de esta fecha, las Cláusulas Sexta y Octava del Contrato. Los demás términos y condiciones conforme a la Carátula seguirán siendo aplicables.\n\n";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);
            
             tabla = new PdfPTable(1);
             tabla.setWidthPercentage(100F);
             texto = "SUSCRIPCIÓN DE PAGARÉ ADICIONAL";
             p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE));
             p.setAlignment(Chunk.ALIGN_LEFT);
             cell = new PdfPCell(p);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setVerticalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(blueColor);
             tabla.addCell(cell);            
             document.add(tabla);
            
             p = new Paragraph();
             texto = "\nLos Acreditados, en este acto, suscriben a su cargo y a favor del Acreditante, el Pagaré Adicional cuyo importe asciende al Monto Total Adicional a Pagar y, el que a su vez, ";
             texto += "documenta la disposición que realizan los Acreditados sobre el Monto Adicional del Crédito. El Acreditante acusa la recepción del Pagaré Adicional, a su entera satisfacción.";
             ck = new Chunk (texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
             p.add(ck);
             p.setAlignment(Chunk.ALIGN_JUSTIFIED);
             p.setLeading(12);
             document.add(p);

             *********************************************************/
        } catch (Exception e) {
            myLogger.error("contenidoDocContratoComunal", e);
        }
    }

    private void contenidoDocCaratula(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPTable tablaInterna = null;
            PdfPTable tablaCuadro = null;
            PdfPCell cell = null;
            String texto = "";
            Color blueColor = new Color(0x00A6FF);
            Color blueCellColor = new Color(0xB9DDE7);
            Color greenHeaderColor = new Color(0x6B904D);
            Color greenCellColor = new Color(0xB7D99C);
            Color grayCellColor = new Color(0xD5E7C4);
            String fecha = "", mes = "";
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            double montoConIntereses = 0.00;

            for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
                montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
            }
            //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
            String montoIni = String.valueOf(montoConIntereses);
            int posicion = montoIni.indexOf(".");
            String mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");
            TasaInteresVO tasa = null;
            String cat = "";
            ArrayList<String> fechasPagos = new ArrayList<String>();
            try {
                //Para obtener tasa
                TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);

                if (catTasas != null) {
                    tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
                    //tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                myLogger.error("contenidoDocCaratula", e);
            }
            double pagos[] = null;
            int plazo = 0;
            try {
                TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
                pagos = new double[tablaVO.length];
                plazo = tablaVO.length - 1;
                for (int i = 0; i < tablaVO.length; i++) {
                    fechasPagos.add(Convertidor.dateToString(tablaVO[i].fechaPago));
                    if (i == 0) {
                        pagos[i] = tablaVO[i].montoPagar;
                    } else {
                        pagos[i] = tablaVO[i].abonoCapital + tablaVO[i].interes;
                    }
                }
            } catch (Exception e) {
                myLogger.error("contenidoDocCaratula", e);
            }

            if (ciclo.tablaAmortizacion != null && ciclo.tablaAmortizacion[0].saldoInicial > 0) {
                cat = getCAT(ciclo.tablaAmortizacion[0].saldoInicial, pagos, fechasPagos);
            }
            Double montoTotal = 0.0;
            Double comisionGestion = ciclo.tablaAmortizacion[0].comisionInicial + ciclo.tablaAmortizacion[0].ivaComision;
            myLogger.debug("CI, IVA, " + ciclo.tablaAmortizacion[0].comisionInicial + ciclo.tablaAmortizacion[0].ivaComision);
            for (int i = 1; i < (ciclo.tablaAmortizacion.length); i++) {
                montoTotal += ciclo.tablaAmortizacion[i].montoPagar;
            }
            texto = "CEGE CAPITAL, S.A.P.I. de C.V., SOFOM, E.N.R.\n";
            texto += "Carátula del Contrato de Crédito Grupal\n\nCuadro Informativo\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            int col[] = {20, 20, 20, 20, 20};
            tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);
            texto = "CAT\nCosto Anual Total";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "TASA DE INTERÉS ANUAL";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "MONTO DEL CRÉDITO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "MONTO TOTAL A PAGAR";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "COMISIONES\nMontos y Cláusulas";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "Para fines informativos y de comparación\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "" + cat + " %";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "" + tasa.valor;
            texto = texto.substring(0, 5);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = " % Ordinaria fija +IVA\n\n99.75 % por mora fija";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "$ " + ciclo.tablaAmortizacion[0].saldoInicial + "";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "$ " + montoTotal + "";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "Comisión 0%\n\nPor gestión $" + comisionGestion + "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "Metodología de cálculo de interés: El monto del interés del periodo se calculará con base en la multiplicación de la tasa de interés del periodo por el monto del crédito.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            texto = "Plazo del Crédito: " + ciclo.plazo + " semanas";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            texto = "Sus Pagos serán como siguen:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            int colInt[] = {33, 33, 34};
            tablaInterna = new PdfPTable(3);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInt);
            texto = "Número";
            p = new Paragraph();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tablaInterna.addCell(cell);
            texto = "Monto";
            p = new Paragraph();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            texto = "Cuándo se realizan los pagos";
            p = new Paragraph();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            texto = "" + ciclo.plazo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            p = new Paragraph();
            texto = "$ " + ciclo.tablaAmortizacion[1].montoPagar + "\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Incluye IVA, por lo que pueden existir pequeñas variaciones en cada pago.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            p = new Paragraph();
            texto = "Semanal\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Los vencimientos se estipulan en la tabla de amortización del presente contrato.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "Tasa variable y Moneda/UDIS: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "Autorización: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Los datos personales pueden utilizarse para mercadeo:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthBottom(0);
            cell.setColspan(5);
            tabla.addCell(cell);
            texto = "O Si          O NO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            cell.setBorderWidthBottom(0);
            cell.setColspan(5);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "Comisiones relevantes:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);

            /*int colInt2[] = {30, 80};
            tablaInterna = new PdfPTable(2);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInt2);*/
            //p = new Paragraph();
            List listado = new List();
            listado.setListSymbol("\u2022");
            ListItem itemLst = new ListItem();
            texto = "  Apertura:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);

            itemLst = new ListItem();
            texto = "  Anualidad:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);

            itemLst = new ListItem();
            texto = "  Prepago:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);

            itemLst = new ListItem();
            texto = "  Pago tardío:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  $86.20 (+IVA)\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            //p.add(listado);
            //cell = new PdfPCell(p);
            cell = new PdfPCell();
            cell.addElement(listado);
            cell.setColspan(2);
            tabla.addCell(cell);
            //document.add(listado);
            /*texto = "Cuadro de Garantías: No aplica";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.ITALIC));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
            listado = new List();
            listado.setListSymbol("\u2022");
            itemLst = new ListItem();
            texto = "  Reposición de tarjeta:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);

            itemLst = new ListItem();
            texto = "  Reclamación improcedente:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            itemLst = new ListItem();
            texto = "  Cobranza:";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            itemLst.add(ck);
            texto = "  No aplica\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            //p.add(listado);
            //cell = new PdfPCell(p);
            cell = new PdfPCell();
            cell.addElement(listado);
            cell.setColspan(3);
            tabla.addCell(cell);

            p = new Paragraph();
            texto = "Advertencia: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            listado = new List();
            itemLst = new ListItem();
            texto = "  Incumplir tus obligaciones te puede generar Comisiones e intereses moratorios";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            itemLst = new ListItem();
            texto = "  Contratar créditos que exceden tu capacidad de pago afecta tu historial crediticio";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            itemLst = new ListItem();
            texto = "  El avalista, obligado solidario o coacreditado responderá como obligado principal por el total del pago frente a la Entidad Financiera\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            itemLst.add(ck);
            listado.add(itemLst);
            cell = new PdfPCell();
            cell.addElement(p);
            cell.addElement(listado);
            cell.setColspan(5);
            tabla.addCell(cell);

            p = new Paragraph();
            texto = "Cuadro de Garantías: No aplica ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(15);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingBottom(4);
            cell.setColspan(5);
            tabla.addCell(cell);

            texto = "SEGUROS:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);

            int colInt1[] = {33, 33, 34};
            tablaInterna = new PdfPTable(3);
            //tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInt1);

            texto = "Seguro: ";
            p = new Paragraph();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "            opcional            .";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.UNDERLINE));
            p.add(ck);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tablaInterna.addCell(cell);
            texto = "Aseguradora:\n";
            p = new Paragraph();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "CHUBB DE MEXICO COMPAÑÍA DE SEGUROS, S.A. DE C.V.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            p = new Paragraph();
            texto = "Cláusula:\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "VIGÉSIMA PRIMERA";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);

            p = new Paragraph();
            texto = "Dudas, aclaraciones y reclamaciones: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "El procedimiento de reclamaciones previsto en el artículo 23 de la Ley para la Transparencia y Ordenamiento de los Servicios Financieros se describe en la cláusula vigésima segunda. Para seguir dicho procedimiento la reclamación respectiva deberá dirigirse a la ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "Unidad Especializada de Atención a Usuarios ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "localizada en:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "Domicilio: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Carretera México Toluca, No. 2430, Col. Lomas de Bezares, C.P. 11910, Delegación Miguel Hidalgo, Ciudad de México; en un horario de atención de 8:00 a 17:00 horas, de Lunes a Viernes.\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "Teléfono: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "(55) 41-60-21-00 ó 01 800 2288 133 (lada sin costo)\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "Correo electrónico: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "une@fcontigo.com\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "Página de Internet: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "www.fcontigo.com\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.UNDERLINE));
            p.add(ck);
            p.setLeading(15);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingBottom(4);
            cell.setColspan(5);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "[ESTADO DE CUENTA]/[CONSULTA DE MOVIMIENTOS]\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "O Entregado en domicilio\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "O Consulta vía Internet\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "O Consulta en ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "____________________\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setLeading(12);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthBottom(0);
            cell.setPaddingBottom(4);
            cell.setPaddingLeft(8);
            cell.setColspan(5);
            tabla.addCell(cell);
            p = new Paragraph();
            texto = "Registro de Contratos de Adhesión Núm: 13317-439-012789/04-05715-1017\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros (CONDUSEF):\nAv. Insurgentes Sur #762 Col. Del Valle México D.F. C.P. 03100\nTeléfono: 01 800 999 8080 y 53400999. Página de Internet. www.condusef.gob.mx";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(5);
            tabla.addCell(cell);
            document.add(tabla);
            /*List listado = new List();
            listado.setListSymbol("\u2022");
            texto = "   Punto 1";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            ListItem itemLst = new ListItem(ck);
            listado.add(itemLst);
            texto = "   Punto 2";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            itemLst = new ListItem(ck);
            listado.add(itemLst);
            texto = "   Punto 3";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            itemLst = new ListItem(ck);
            listado.add(itemLst);
            document.add(listado);*/
        } catch (Exception e) {
            myLogger.error("contenidoDocCaratula", e);
        }
    }

    public static String getCAT(double saldo, double[] pagos, ArrayList fechasPagos) {
        double CAT = 150.00;
        boolean found = false;
        double sk = 0.00;
        double p = 0.00;
        double cat = 0.00;
        double sumatoria = 0.00;
        ArrayList<Integer> diasDiferencia = new ArrayList<Integer>();
        int diasLong = 0;
        double diasDouble = 0.00;

        try {
            for (int i = 0; i < fechasPagos.size(); i++) {
                diasLong = FechasUtil.inBetweenDays(Convertidor.stringToDate((String) fechasPagos.get(0)), Convertidor.stringToDate((String) fechasPagos.get(i)));
                diasDiferencia.add(diasLong);
            }
        } catch (Exception e) {
            myLogger.error("getCAT", e);
        }

        while (!found) {
            CAT = CAT - 0.001;
            sumatoria = 0.00;
            for (Integer i = 0; i <= pagos.length - 1; i++) {
                diasLong = (Integer) diasDiferencia.get(i);
                diasDouble = diasLong;
                sk = diasDouble / 365;
                cat = java.lang.Math.pow(CAT + 1, sk);
                p = pagos[i] / cat;
                sumatoria = sumatoria + p;
            }

            if (sumatoria >= saldo) {
                found = true;
            }
        }
        CAT = CAT * 100;

        return HTMLHelper.formatoMonto(CAT);
    }

    private static String getNombrePlantilla(SolicitudVO solicitud, int periodos, String tipoDocumento) {

        int tipoOperacion = solicitud.tipoOperacion;
        String plantilla = null;
        if (tipoOperacion == ClientesConstants.MICROCREDITO) {
            if (solicitud.solicitudReestructura != 0) {
                plantilla = (periodos <= 25 ? ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\ConvenioReestructura24.pdf"
                        : ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\ConvenioReestructura36.pdf");
            } else /*plantilla = (periodos <= 25 ? ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\Micro24.pdf"
                 : ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\Micro36.pdf");*/ if (tipoDocumento.equals(ClientesConstants.DOC_FORMAL_CONTRATO)) {
                plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\Micro.pdf";
            } else if (tipoDocumento.equals(ClientesConstants.DOC_FORMAL_AUTORIZACION_DESEMB)) {
                plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Micro\\Autorizacion.pdf";
            }
        }
        if (tipoOperacion == ClientesConstants.CONSUMO || tipoOperacion == ClientesConstants.CREDIHOGAR) {
            if (periodos <= 25) {
                if (solicitud.solicitudReestructura != 0) {
                    plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\ConvenioReestructura24.pdf";
                } else {
                    plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\Consumo24.pdf";
                }
            } else if (periodos >= 26 && periodos <= 37) {
                if (solicitud.solicitudReestructura != 0) {
                    plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\ConvenioReestructura36.pdf";
                } else {
                    plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\Consumo36.pdf";
                }
            } else if (solicitud.solicitudReestructura != 0) {
                plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\ConvenioReestructura72.pdf";
            } else {
                plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Consumo\\Consumo72.pdf";
            }
        }
        if (tipoOperacion == ClientesConstants.SELL_FINANCE) {
            DocumentoFormalizVO documento = new DocumentoFormalizVO();
            DocumentoFormalizDAO documentoDAO = new DocumentoFormalizDAO();
            int periodo_doc = 0;
            if (periodos <= 25) {
                periodo_doc = 24;
            } else if (periodos >= 26 && periodos <= 37) {
                periodo_doc = 36;
            } else {
                periodo_doc = 72;
            }
            try {
                documento = documentoDAO.getDocumentoFormaliz(ClientesConstants.DOC_FORMAL_CONTRATO, solicitud.numrepresentante, periodo_doc);
            } catch (ClientesException e) {
                // TODO Auto-generated catch block
                myLogger.error("getNombrePlantilla", e);
            }
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + documento.rutaArchivo + documento.plantilla;
        }

        return plantilla;

    }

    private static String getNombrePlantillaGeneral(SolicitudVO solicitud, String tipoPlantilla, int periodos) {

        int tipoOperacion = solicitud.tipoOperacion;
        String plantilla = null;
        if (tipoOperacion == ClientesConstants.SELL_FINANCE) {
            DocumentoFormalizVO documento = new DocumentoFormalizVO();
            DocumentoFormalizDAO documentoDAO = new DocumentoFormalizDAO();
            int periodo_doc = 0;
            if (periodos <= 25) {
                periodo_doc = 24;
            } else if (periodos >= 26 && periodos <= 37) {
                periodo_doc = 36;
            } else {
                periodo_doc = 72;
            }
            try {
                documento = documentoDAO.getDocumentoFormaliz(tipoPlantilla, solicitud.numrepresentante, periodo_doc);
            } catch (ClientesException e) {
                // TODO Auto-generated catch block
                myLogger.error("getNombrePlantillaGeneral", e);
            }
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + documento.rutaArchivo + documento.plantilla;
        }

        return plantilla;

    }

    public static void getTablaIntegrantesPDF(CicloGrupalVO ciclo, AcroFields reciboForma) {

        try {
            int cont = 0;

            for (int i = 0; i < ciclo.integrantes.length; i++) {
                String domicilio = ClientesUtil.getDomicilio(ciclo.integrantes[i].idCliente);
                cont++;

                reciboForma.setField("numIntegrante" + cont, cont + "");
                reciboForma.setField("nombreIntegrante" + cont, ciclo.integrantes[i].nombre);
                reciboForma.setField("direccionIntegrante" + cont, domicilio.toUpperCase());
            }

        } catch (Exception e) {
            myLogger.error("getTablaIntegrantesPDF", e);
        }
    }

    public static String getTablaIntegrantes(CicloGrupalVO ciclo) {

        String tablaFormat = "";

        try {
            tablaFormat = "<table border='1' cellspacing='0' width='100%' align='center'>";
            tablaFormat += "<tr bgcolor='#c0c0c0'><td><center><b>&nbsp;</b></center></td>";
            tablaFormat += "<td><center><b>Nombre (s) y Apellido (s) Completo / Domicilio</b></center></td>";
            tablaFormat += "<td><center><b>Firma</b></center></td></tr>";

            for (int i = 0; i < ciclo.integrantes.length; i++) {
                String domicilio = ClientesUtil.getDomicilio(ciclo.integrantes[i].idCliente);
                tablaFormat += "<tr><td width='10%'><center><font size='2'>" + (i + 1) + "</font></center></td>";
                tablaFormat += "<td width='60%'><font size='2'>" + HTMLHelper.displayField(ciclo.integrantes[i].nombre) + "<br>Domicilio: " + HTMLHelper.displayField(domicilio) + "</font></td>";
                tablaFormat += "<td width='30%'><center>&nbsp;</center></td></tr>";
            }
            tablaFormat += "</table>";

        } catch (Exception e) {
            myLogger.error("getTablaIntegrantes", e);
        }
        return tablaFormat;
    }

    public static String getTablaIntegrantesPagare(CicloGrupalVO ciclo) {

        String tablaFormat = "";

        try {
            if (ciclo != null && ciclo.integrantes != null && ciclo.integrantes.length > 0) {
                tablaFormat = "<table border='1' cellspacing='0' width='100%' align='center'>";
                tablaFormat += "<tr bgcolor='#c0c0c0'><td><center><b>&nbsp;</b></center></td>";
                tablaFormat += "<td><center><b>Nombre (s) y Apellido (s) Completo / Domicilio</b></center></td>";
                tablaFormat += "<td><center><b>Firma</b></center></td></tr>";
                tablaFormat += "<tr><td width='10%'><center><font size='2'>" + (1) + "</font></center></td>";
                tablaFormat += "<td width='60%'><font size='2'>" + HTMLHelper.displayField(ciclo.integrantes[0].nombre) + "<br>Domicilio: " + HTMLHelper.displayField(ClientesUtil.getDomicilio(ciclo.integrantes[0].idCliente)) + "</font></td>";
                tablaFormat += "<td width='30%'><center>&nbsp;</center></td></tr>";
                tablaFormat += "</table>";

                tablaFormat += "<br><center><b>AVALES</b></center><br>";
                tablaFormat += "<table border='1' cellspacing='0' width='100%' align='center'>";
                tablaFormat += "<tr bgcolor='#c0c0c0'><td><center><b>&nbsp;</b></center></td>";
                tablaFormat += "<td><center><b>Nombre (s) y Apellido (s) Completo / Domicilio</b></center></td>";
                tablaFormat += "<td><center><b>Firma</b></center></td></tr>";
                for (int i = 1; i < ciclo.integrantes.length; i++) {
                    String domicilio = ClientesUtil.getDomicilio(ciclo.integrantes[i].idCliente);
                    tablaFormat += "<tr><td width='10%'><center><font size='2'>" + (i + 1) + "</font></center></td>";
                    tablaFormat += "<td width='60%'><font size='2'>" + HTMLHelper.displayField(ciclo.integrantes[i].nombre) + "<br>Domicilio: " + HTMLHelper.displayField(domicilio) + "</font></td>";
                    tablaFormat += "<td width='30%'><center>&nbsp;</center></td></tr>";
                }
                tablaFormat += "</table>";
            }

        } catch (Exception e) {
            myLogger.error("getTablaIntegrantesPagare", e);
        }
        return tablaFormat;
    }

    public void generaHojaResumen(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) throws CommandException, ParseException {
        String fecha = CatalogoHelper.getParametro("FECHA_LIBERACION_HR_CP");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        Date fechaHojaResumenControlPagos = sdf.parse(fecha);
        myLogger.info("Fecha Desembolso hoja resumen: "+ciclo.saldo.getFechaDesembolso());
        myLogger.info("Fecha Liberacion Hoja Resumen y Control Pagos: "+fecha);
        
        if(ciclo.saldo.getFechaDesembolso().compareTo(fechaHojaResumenControlPagos) >= 0){
            myLogger.info("Hoja Resumen Nueva Version");
            generaHojaResumenFinal(response, grupo, ciclo);
        }else{
            myLogger.info("Hoja Resumen version anterior");
            generaHojaResumenAnterior(response, grupo, ciclo);
        }
        
    }
    
    public void generaHojaResumenFinal(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) throws CommandException {
        GeneraDocumentosHelper docHelper = new GeneraDocumentosHelper();
        try {
            String nombreArchivo = "";
            int plazo = 0, plazoInterciclo = 12, plazoGrupal = 0;
            if (ciclo.tablaAmortizacion != null) {
                plazo = ciclo.tablaAmortizacion.length - 1;
                plazoGrupal = ciclo.tablaAmortizacion.length - 1;
            }
            
            TablaAmortDAO tablaDao = new TablaAmortDAO();
            TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
            
            TablaAmortVO tablaAmortizacion = new TablaAmortVO();
            tablaAmortizacion = tablaDao.getDivVigente(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            if (tablaAmortizacion == null) {
                tablaAmortizacion = tablaDao.getUltmioDivPagado(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            }
            int numPago = 1;
            if (tablaAmortizacion != null) {
                numPago = tablaAmortizacion.getNumPago();
            }
            
//            IntegranteCicloVO [] integrantesFinales = descartaIntegrantes(ciclo, grupo);
            IntegranteCicloVO [] integrantesFinales = docHelper.descartaIntegrantes(ciclo, grupo, numPago);

            if (integrantesFinales != null) {
                nombreArchivo = (integrantesFinales.length > 20 ? "HojaResumenGrupo40.pdf" : "HojaResumenGrupo.pdf");
            }

            PdfReader reader = new PdfReader(ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Comunal\\" + nombreArchivo);
            PdfStamper reciboPDF = new PdfStamper(reader, response.getOutputStream());
            AcroFields reciboForma = reciboPDF.getAcroFields();

            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fechaImp = dateFormat.format(date);

            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);

            ComisionVO comision = new ComisionVO();
            double comisiones = (ciclo.tablaAmortizacion.length > 0 ? (ciclo.tablaAmortizacion[0].comisionInicial + ciclo.tablaAmortizacion[0].ivaComision) : 0);
            double montoCredito = (ciclo.tablaAmortizacion.length > 0 ? ciclo.tablaAmortizacion[0].saldoInicial : 0);
            comision.porcentaje = (comisiones / montoCredito) * 100;
            String tasaComisionPrint = String.valueOf(HTMLHelper.formatoMonto(comision.porcentaje));

            TreeMap catAsesores = CatalogoHelper.getCatalogoEjecutivos(grupo.sucursal);
            Set set = catAsesores.keySet();
            Iterator llaves = set.iterator();
            Object key = null;

            StringBuffer codigo = new StringBuffer();
            while (llaves.hasNext()) {
                key = llaves.next();
                if (key.toString().equals(String.valueOf(ciclo.asesor))) {
                    codigo.append(catAsesores.get(key).toString());
                }
            }

            // Dependiendo del numero de integrantes es que se llena la forma, esto se realizo debido a un problema en la edicion de la forma
            // HojaResumenComunal40, la cual pedia el underscore _ en lugar de espacio.
            if (integrantesFinales.length < 21) {
                reciboForma.setField("FechaImpresion", fechaImp);
                reciboForma.setField("nombreGrupo", grupo.nombre);
                reciboForma.setField("asesor", codigo.toString());
                reciboForma.setField("ciclo", String.valueOf(ciclo.idCiclo));
                reciboForma.setField("comunidad", ciclo.direccionReunion.colonia);
                reciboForma.setField("fechaDesembolso", HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago));
                reciboForma.setField("fechaUltimoPago", HTMLHelper.displayField(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
                reciboForma.setField("tasa", tasa.descripcion);
                reciboForma.setField("comision", tasaComisionPrint + " %");
                reciboForma.setField("frecuenciaJuntas", "SEMANAL");
                reciboForma.setField("noSemanas", String.valueOf(plazo));

                
                double montoTotalAmortizaciones = 0;
                double montoCorrespondiente = 0;
                int pi = 1; //indice de la plantilla
                //Segundo bloque escribe fechas de pago 
                for (int i = 0; tablasAmor != null && i < tablasAmor.length; i++) {                    
                    reciboForma.setField("Semana " + pi, HTMLHelper.displayField(tablasAmor[i].fechaPago));
                    myLogger.info("Monto pagado: "+tablasAmor[i].totalPagado +" monto pagar: "+ tablasAmor[i].montoPagar);
                    montoTotalAmortizaciones += tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    if(tablasAmor[i].numPago == numPago){
                        montoCorrespondiente = tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    }
                    pi =pi+1;
                }
                myLogger.info("Monto amortizacion corresppndiente: "+montoCorrespondiente);
                myLogger.info("Monto Total amortizaciones: "+montoTotalAmortizaciones);
                
                //Tercer Bloque Uso Oficial  de OM
                reciboForma.setField("No Grupo", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No Cuenta", ciclo.referencia);
                reciboForma.setField("No Cliente", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No Contrato", "No Disponible");

                //Bloque informacion de pagos
                reciboForma.setField("NumConvenioBancomer", ClientesConstants.CUENTA_BANCOMER);
                reciboForma.setField("NumCuentaBansefi", ClientesConstants.CUENTA_BANSEFI);
                reciboForma.setField("NumCuentaBanorte", ClientesConstants.CUENTA_BANORTE);
                reciboForma.setField("NumCuentaBajio", ClientesConstants.CUENTA_BAJIO);
                reciboForma.setField("NumCuentaAfirme", ClientesConstants.CUENTA_BANCO_AFIRME);
                reciboForma.setField("NumReferencia", ciclo.referencia);

                //Bloque integrantes
                int cont = 0;
                double tempMontoDesembolsado = 0.0;
                double tempComSeg = 0.0;
                double tempInteres = 0.0;
                double tempPagoIndividual = 0.0;
                double pagTotalTotal = 0;
                
                double [] interesesIndividuales = new double[integrantesFinales.length];
                double [] pagSemanalColumna = obtenerPagosIndividuales(integrantesFinales, montoCorrespondiente, tasa, catComisiones, ciclo.getPlazo(), numPago);
                double [] montoSinComision = new double[integrantesFinales.length];
                double [] montoSinComisionPDF = new double[integrantesFinales.length];
                double [] comisionSeguroRefinaciado = new double[integrantesFinales.length];
                double [] interesesAdicionales = new double[integrantesFinales.length];
                boolean intercicloAdicional = false;
                
                double montoTotalInteresesAdicionales = 0.0;//codigo para adicional
                //Obtenemos primero los montos individucales y el monto total sin redondeos
                for(int i = 0; i < integrantesFinales.length; i++){
                    
                        montoSinComisionPDF[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        montoSinComision[i] = montoSinComisionPDF[i];
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            montoSinComision[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        }
                    
                        montoSinComision[i] = FormatUtil.redondeaMoneda(montoSinComision[i]);
                        
                        comisionSeguroRefinaciado[i] = (integrantesFinales[i].monto - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            comisionSeguroRefinaciado[i] = (integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        }
                        
                        comisionSeguroRefinaciado[i] = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado[i]);
                        tempComSeg += comisionSeguroRefinaciado[i];
                        plazo = plazoGrupal;
                        if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_2;
                            intercicloAdicional = true;
                        } else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_4;
                            intercicloAdicional = true;
                        }
                        double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
                        interes = (montoSinComision[i] + comisionSeguroRefinaciado[i]) * interes;
                        interes = FormatUtil.redondeaMoneda(interes);
                        
                        double pagoIndividual = (montoSinComision[i] + comisionSeguroRefinaciado[i] + interes) / (plazo);
                        pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                        
                        if(integrantesFinales[i].tipo_adicional!=0){
                            int plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
                            myLogger.info("monto adicional: "+integrantesFinales[i].montoAdicional);
                            double interesAdi = integrantesFinales[i].montoAdicional *(AdicionalUtil.calculaInteresAdicional(tasa.valor, plazoAdi));
                            double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                            pagoIndividual +=Math.ceil(pagoIndivudualAdi);
                            montoSinComision[i] += Math.ceil(integrantesFinales[i].montoAdicional);
                            interes+=Math.ceil(interesAdi);
                            interesesAdicionales[i]=Math.ceil(interesAdi);
                            montoTotalInteresesAdicionales += Math.ceil(interesAdi);
                            myLogger.info("Interes adicional: "+Math.ceil(interesAdi));
                            intercicloAdicional = true;
                        }else{
                            interesesAdicionales[i]=0;
                        }
                        tempMontoDesembolsado += montoSinComision[i];
                        tempInteres += Math.ceil(interes);
                        tempPagoIndividual += Math.ceil(pagoIndividual);
                        double interesColumna = Math.ceil(interes);
                        double pagTotalColumna = Math.ceil(montoSinComision[i] + comisionSeguroRefinaciado[i] + interes);
                        
                        interesesIndividuales[i]=Math.ceil(interes);
                        myLogger.info("interes total individual antes: "+interesColumna);
                        pagTotalTotal += pagTotalColumna;
                }
                myLogger.info("Total interes sin ajuste: "+tempInteres);
                myLogger.info("Monto total sin comision desembolsado: "+tempMontoDesembolsado);
                
                //Restamos al total de amortizacion el monto neto prestado para obetner unicamente
                //los interese de las amortizaciones correcto
                double interesTotalCorrecto = montoTotalAmortizaciones - tempMontoDesembolsado;
                myLogger.info("Interes total correcto: "+interesTotalCorrecto);
                

                double diferencia = interesTotalCorrecto - tempInteres;
                myLogger.info("Diferencia Montos: "+diferencia);
                int complemento = 0;
                if(((int)diferencia)==0){
                    myLogger.info("Los montos cuadran, no se realiza ajuste!");

                }else if(((int)diferencia)<0){
                    myLogger.info("Diferencia Negativa, no se realizara ajuste, solo el monto en PDF");
                    interesTotalCorrecto=tempInteres;

                }else if(((int)diferencia)>0){
                    myLogger.info("Diferencia Positiva");
                    myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
                    complemento = docHelper.realizarAjusteMontos(integrantesFinales, interesesIndividuales, interesesAdicionales, montoTotalInteresesAdicionales, interesTotalCorrecto, tempInteres, intercicloAdicional);
                    myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");
                    interesTotalCorrecto = interesTotalCorrecto + complemento;
                }
                
                
                for (int i = 0; i < integrantesFinales.length; i++) {
                    if (integrantesFinales[i].tipo != 0) {
                        String domicilio = ClientesUtil.getDomicilio(integrantesFinales[i].idCliente);
                        ClienteVO cliente = new ClienteDAO().getCliente(integrantesFinales[i].idCliente);
                        TelefonoVO[] telefonos = new TelefonoDAO().getTelefonos(integrantesFinales[i].idCliente, 1);
                        String telefono = "";
                        for (int a = 0; telefonos != null && a < telefonos.length; a++) {
                            telefono += telefonos[a].numeroTelefono;
                            int temp = a + 1;
                            if (temp < telefonos.length) {
                                telefono += " : ";
                            }
                        }
                        
                        int numeroCiclosParticipados = new IntegranteCicloDAO().numCiclosParticipados(integrantesFinales[i]);
                        cont++;
                        reciboForma.setField("NoRow" + cont, cont + "");
                        reciboForma.setField("PuestoRow" + cont, (integrantesFinales[i].rol == 1 ? "TESORERO" : integrantesFinales[i].rol == 2 ? "SECRETARIO" : integrantesFinales[i].rol == 3 ? "PRESIDENTE" : ""));
                        reciboForma.setField("No ClienteRow" + cont, HTMLHelper.displayField(integrantesFinales[i].idCliente));
                        integrantesFinales[i].nombre = integrantesFinales[i].nombre.replace("Ñ", "N");
                        reciboForma.setField("Nombre del miembro del grupoRow" + cont, integrantesFinales[i].nombre);
                        reciboForma.setField("DirecciónRow" + cont, domicilio.toUpperCase());
                        reciboForma.setField("TeléfonoRow" + cont, telefono);
                        reciboForma.setField("Ciclo Row" + cont, HTMLHelper.displayField(numeroCiclosParticipados));
                        reciboForma.setField("SexoRow" + cont, (cliente.sexo == 1 ? "F" : cliente.sexo == 2 ? "M" : "N/D"));
                        reciboForma.setField("Monto DESEMBOLSADORow" + cont, HTMLHelper.formatoMonto(montoSinComisionPDF[i]));
                        reciboForma.setField("AccesoriosRow" + cont, HTMLHelper.formatoMonto(comisionSeguroRefinaciado[i]));
                        reciboForma.setField("InterésRow" + cont, HTMLHelper.formatoMonto(interesesIndividuales[i]));
                        reciboForma.setField("Pago TotalRow" + cont, HTMLHelper.formatoMonto(montoSinComision[i]+interesesIndividuales[i]));
                        reciboForma.setField("Pago SemanalRow" + cont, HTMLHelper.formatoMonto(pagSemanalColumna[i]));
                    }
                }

                //BLoque de totales 
                reciboForma.setField("Monto DESEMBOLSADOTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempMontoDesembolsado)));
                reciboForma.setField("AccesoriosTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempComSeg)));
                reciboForma.setField("InterésTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(interesTotalCorrecto)));
                reciboForma.setField("Pago TotalTOTAL", HTMLHelper.formatoMonto(montoTotalAmortizaciones));
                reciboForma.setField("Pago SemanalTOTAL", HTMLHelper.formatoMonto(tempPagoIndividual));

                //Ultimo campo de interes moratorio calculado
                //reciboForma.setField("interesMora" 						, "$ " + HTMLHelper.formatoMonto( ( (tempMontoDesembolsado+tempComSeg+tempInteres)/plazo )*0.003928 ) );
            } else {
                /**
                 * Variables Pimer Bolque informaciongenral del grupo
                 */
                reciboForma.setField("nombreGrupo", grupo.nombre);
                reciboForma.setField("asesor", codigo.toString());
                reciboForma.setField("ciclo", String.valueOf(ciclo.idCiclo));
                reciboForma.setField("comunidad", ciclo.direccionReunion.colonia);
                reciboForma.setField("fechaDesembolso", HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago));
                reciboForma.setField("fechaUltimoPago", HTMLHelper.displayField(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
                reciboForma.setField("tasa", tasa.descripcion);
                reciboForma.setField("comision", tasaComisionPrint + " %");
                reciboForma.setField("frecuenciaJuntas", "SEMANAL");
                reciboForma.setField("noSemanas", String.valueOf(plazo));

                double montoTotalAmortizaciones = 0;
                double montoCorrespondiente = 0;
                
                //Segundo bloque escribe fechas de pago 
                for (int i = 0; tablasAmor != null && i < tablasAmor.length; i++) {
                    reciboForma.setField("Semana " + i, HTMLHelper.displayField(tablasAmor[i].fechaPago));
                    myLogger.info("Monto pagado: "+tablasAmor[i].totalPagado +" monto pagar: "+ tablasAmor[i].montoPagar);
                    montoTotalAmortizaciones += tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    if(tablasAmor[i].numPago == numPago){
                        montoCorrespondiente = tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    }
                }
                myLogger.info("Monto amortizacion correspondiente: "+montoCorrespondiente);
                myLogger.info("Monto Total amortizaciones: "+montoTotalAmortizaciones);
                
                //Tercer Bloque Uso Oficial  de OM
                reciboForma.setField("No_Grupo", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No_Cuenta", ciclo.referencia);
                reciboForma.setField("No_Cliente", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No_Contrato", "No Disponible");

                //Bloque informacion de pagos
                reciboForma.setField("NumConvenioBancomer", ClientesConstants.CUENTA_BANCOMER);
                reciboForma.setField("NumCuentaBansefi", ClientesConstants.CUENTA_BANSEFI);
                reciboForma.setField("NumCuentaBanorte", ClientesConstants.CUENTA_BANORTE);
                reciboForma.setField("NumCuentaBajio", ClientesConstants.CUENTA_BAJIO);
                reciboForma.setField("NumCuentaAfirme", ClientesConstants.CUENTA_BANCO_AFIRME);
                reciboForma.setField("NumReferencia", ciclo.referencia);

                //Bloque integrantes
                int cont = 0;
                double tempMontoDesembolsado = 0.0;
                double tempComSeg = 0.0;
                double tempInteres = 0.0;
                double tempPagoIndividual = 0.0;
                
                double [] interesesIndividuales = new double[integrantesFinales.length];
                double [] pagSemanalColumna = obtenerPagosIndividuales(integrantesFinales, montoCorrespondiente, tasa, catComisiones, ciclo.getPlazo(), numPago);
                double [] montoSinComision = new double[integrantesFinales.length];
                double [] montoSinComisionPDF = new double[integrantesFinales.length];
                double [] comisionSeguroRefinaciado = new double[integrantesFinales.length];
                double [] interesesAdicionales = new double[integrantesFinales.length];
                boolean intercicloAdicional = false;
                
                double montoTotalInteresesAdicionales = 0.0;//codigo para adicional
                
                //Obtenemos primero los montos individucales y el monto total sin redondeos
                for(int i = 0; i < integrantesFinales.length; i++){
                        
                        montoSinComisionPDF[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        montoSinComision[i] = montoSinComisionPDF[i];
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            montoSinComision[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        }
                    
                        montoSinComision[i] = FormatUtil.redondeaMoneda(montoSinComision[i]);
						
                        comisionSeguroRefinaciado[i] = (integrantesFinales[i].monto - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            comisionSeguroRefinaciado[i] = (integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        }
                        
                        comisionSeguroRefinaciado[i] = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado[i]);
                        tempComSeg += comisionSeguroRefinaciado[i];
                        plazo = plazoGrupal;
                        if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_2;
                            intercicloAdicional = true;
                        } else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_4;
                            intercicloAdicional = true;
                        }

                        double interes = ((((tasa.valor * 1.16)) / 100) / 360) * (plazo * 7);
                        interes = (montoSinComision[i] + comisionSeguroRefinaciado[i]) * interes;
                        interes = FormatUtil.redondeaMoneda(interes);
                        
                        double pagoIndividual = (montoSinComision[i] + comisionSeguroRefinaciado[i] + interes) / (plazo);
                        pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                        
                        if(integrantesFinales[i].tipo_adicional!=0){
                            int plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
							myLogger.info("monto adicional: "+integrantesFinales[i].montoAdicional);
                            double interesAdi = integrantesFinales[i].montoAdicional * (AdicionalUtil.calculaInteresAdicional(tasa.valor, plazoAdi));
                            double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                            pagoIndividual +=Math.ceil(pagoIndivudualAdi);
                            montoSinComision[i] += Math.ceil(integrantesFinales[i].montoAdicional);
                            interes+=Math.ceil(interesAdi);
                            interesesAdicionales[i]=Math.ceil(interesAdi);
                            montoTotalInteresesAdicionales += Math.ceil(interesAdi);
                            myLogger.info("Interes adicional: "+Math.ceil(interesAdi));
                            intercicloAdicional = true;
                        }else{
                            interesesAdicionales[i]=0;
                        }
                                                
                        tempMontoDesembolsado += montoSinComision[i];
                        tempInteres += Math.ceil(interes);
                        tempPagoIndividual += Math.ceil(pagoIndividual);
                        
                        double interesColumna = Math.ceil(interes);
                        double pagTotalColumna = Math.ceil(pagoIndividual * plazo);
                        
                        interesesIndividuales[i]=interesColumna;
                        myLogger.info("interes total individual antes: "+interesColumna);
                }      
                
                myLogger.info("Total interes sin ajuste: "+tempInteres);
                myLogger.info("Monto total sin comision desembolsado: "+tempMontoDesembolsado);
                
                //Restamos al total de amortizacion el monto neto prestado para obetner unicamente
                //los interese de las amortizaciones correcto
                double interesTotalCorrecto = montoTotalAmortizaciones - tempMontoDesembolsado;
                myLogger.info("Interes total correcto: "+interesTotalCorrecto);
                
                double diferencia = interesTotalCorrecto - tempInteres;
                myLogger.info("Diferencia Montos: "+diferencia);
                int complemento = 0;
                if(((int)diferencia)==0){
                    myLogger.info("Los montos cuadran, no se realiza ajuste!");

                }else if(((int)diferencia)<0){
                    myLogger.info("Diferencia Negativa, no se realizara ajuste, solo el monto en PDF");
                    interesTotalCorrecto=tempInteres;

                }else if(((int)diferencia)>0){
                    myLogger.info("Diferencia Positiva");
                    myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
                    complemento = docHelper.realizarAjusteMontos(integrantesFinales, interesesIndividuales, interesesAdicionales, montoTotalInteresesAdicionales, interesTotalCorrecto, tempInteres, intercicloAdicional);
                    myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");
                    interesTotalCorrecto = interesTotalCorrecto + complemento;
                }
                
                for (int i = 0; i < integrantesFinales.length; i++) {
                    String domicilio = ClientesUtil.getDomicilio(integrantesFinales[i].idCliente);
                    ClienteVO cliente = new ClienteDAO().getCliente(integrantesFinales[i].idCliente);
                    TelefonoVO[] telefonos = new TelefonoDAO().getTelefonos(integrantesFinales[i].idCliente, 1);
                    String telefono = "";
                    for (int a = 0; telefonos != null && a < telefonos.length; a++) {
                        telefono += telefonos[a].numeroTelefono;
                        int temp = a + 1;
                        if (temp < telefonos.length) {
                            telefono += " : ";
                        }
                    }
                    
                    int numeroCiclosParticipados = new IntegranteCicloDAO().numCiclosParticipados(integrantesFinales[i]);

                    cont++;
                    reciboForma.setField("NoRow" + cont, cont + "");
                    reciboForma.setField("PuestoRow" + cont, (integrantesFinales[i].rol == 1 ? "TESORERO" : integrantesFinales[i].rol == 2 ? "SECRETARIO" : integrantesFinales[i].rol == 3 ? "PRESIDENTE" : ""));
                    reciboForma.setField("No_ClienteRow" + cont, HTMLHelper.displayField(integrantesFinales[i].idCliente));
                    reciboForma.setField("Nombre_del_miembro_del_grupoRow" + cont, integrantesFinales[i].nombre);
                    reciboForma.setField("DirecciónRow" + cont, domicilio.toUpperCase());
                    reciboForma.setField("TeléfonoRow" + cont, telefono);
                    reciboForma.setField("Ciclo_Row" + cont, HTMLHelper.displayField(numeroCiclosParticipados));
                    reciboForma.setField("SexoRow" + cont, (cliente.sexo == 1 ? "F" : cliente.sexo == 2 ? "M" : "N/D"));
                    reciboForma.setField("Monto_DESEMBOLSADORow" + cont, HTMLHelper.formatoMonto(montoSinComisionPDF[i]));
                    reciboForma.setField("AccesoriosRow" + cont, HTMLHelper.formatoMonto(comisionSeguroRefinaciado[i]));
                    reciboForma.setField("InterésRow" + cont, HTMLHelper.formatoMonto(Math.ceil(interesesIndividuales[i])));
                    reciboForma.setField("Pago_TotalRow" + cont, HTMLHelper.formatoMonto(Math.ceil(montoSinComision[i] + comisionSeguroRefinaciado[i] + interesesIndividuales[i])));
                    reciboForma.setField("Pago_SemanalRow" + cont, HTMLHelper.formatoMonto(Math.ceil(pagSemanalColumna[i])));
                }

                reciboForma.setField("Monto_DESEMBOLSADOTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempMontoDesembolsado)));
                reciboForma.setField("AccesoriosTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempComSeg)));
                reciboForma.setField("InterésTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(interesTotalCorrecto)));
                reciboForma.setField("Pago_TotalTOTAL", HTMLHelper.formatoMonto(montoTotalAmortizaciones));
                reciboForma.setField("Pago_SemanalTOTAL", HTMLHelper.formatoMonto(tempPagoIndividual));
                
                //Ultimo campo de interes moratorio calculado
                //reciboForma.setField("interesMora" 						, "$ " + HTMLHelper.formatoMonto( ( (tempMontoDesembolsado+tempComSeg+tempInteres)/plazo )*0.003928 ) );
            }

            reciboPDF.setFormFlattening(true);
            reciboPDF.close();

        } catch (Exception e) {
            myLogger.error("generaHojaResumen", e);
            throw new CommandException(e.getMessage());
            //myLogger.debug ("I/O Exception: " + e);
        }
    }
    
    public double[] obtenerPagosIndividuales(IntegranteCicloVO [] integrantesFinales, double montoAmortizacion, TasaInteresVO tasa, TreeMap catComisiones, int plazoCiclo, int numPago) throws ClientesException{
        GeneraDocumentosHelper docHelper =  new GeneraDocumentosHelper();
        double montoTotalSinAjuste = 0;
        int plazo = 0;
        
        myLogger.info("Monto Amortizacion: "+montoAmortizacion);
        //Obtener Monto Total exacto y el monto individual de cada integrante
        //para que en la generacion del pdf se calcule monto individual en base al monto total de la amortizacion
        double [] pagosIndividuales = new double [integrantesFinales.length];
        double [] pagosAdicionales = new double [integrantesFinales.length];
        double montoTotalPagosAdicionales = 0;
        boolean intercicloAdicional = false;
        for (int i = 0; i < integrantesFinales.length; i++) {
            if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO){
                plazo = 14;//plazo para interciclo
                intercicloAdicional = true;
            }else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2){
                plazo = 12;//plazo para interciclo   
                intercicloAdicional = true;
            }else{
                plazo = plazoCiclo;
            }
            double montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
            
            if (integrantesFinales[i].montoConSeguro > 0) {
                montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
            }
            
            montoSinComision = FormatUtil.redondeaMoneda(montoSinComision);
            double comisionSeguroRefinaciado = (integrantesFinales[i].monto-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
            
            if (integrantesFinales[i].montoConSeguro > 0) {
                comisionSeguroRefinaciado = (integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
            }
			
			comisionSeguroRefinaciado = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado);
            double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
            interes = (montoSinComision + comisionSeguroRefinaciado) * interes;
            interes = FormatUtil.redondeaMoneda(interes);
            double pagoIndividual = (montoSinComision + comisionSeguroRefinaciado + interes) / (plazo);

            int plazoAdi = 0;
            if(integrantesFinales[i].tipo_adicional!=0 ){
                plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
            }
                
            myLogger.debug("Plazo Adicional desembolso :"+ (16-plazoAdi));
            if(plazoAdi > 0 && numPago > (16-plazoAdi)){
                double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                pagoIndividual +=Math.ceil(pagoIndivudualAdi);
                pagosAdicionales[i]=Math.ceil(pagoIndivudualAdi);
                montoTotalPagosAdicionales += Math.ceil(pagoIndivudualAdi);
                myLogger.info("Pago adicional: "+Math.ceil(pagoIndivudualAdi));
                intercicloAdicional = true;
            }else{
                pagosAdicionales[i]=0;
            }

            montoTotalSinAjuste += Math.ceil(pagoIndividual);
//                montoTotal = Math.round(montoTotal);
//                pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
            pagosIndividuales[i]=Math.ceil(pagoIndividual);
            myLogger.debug("Pago inividual antes: "+pagosIndividuales[i]);
        }
        
        
        myLogger.info("Monto Total SIN AJUSTE: "+montoTotalSinAjuste);
            
        double diferencia = montoAmortizacion - montoTotalSinAjuste;
        myLogger.info("Diferencia Montos: "+diferencia);
        int complemento = 0;
        if(((int)diferencia)==0){
            myLogger.info("Los montos cuadran, no se realiza ajuste!");

        }else if(((int)diferencia)<0){
            myLogger.info("Diferencia Negativa, no se realizara ajuste, solo el monto en PDF");
            montoAmortizacion=montoTotalSinAjuste;

        }else if(((int)diferencia)>0){
            myLogger.info("Diferencia Positiva");
            myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
            complemento = docHelper.realizarAjusteMontos(integrantesFinales, pagosIndividuales, pagosAdicionales, montoTotalPagosAdicionales, montoAmortizacion, montoTotalSinAjuste, intercicloAdicional);
            myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");
            montoAmortizacion = montoAmortizacion + complemento;
        }
        
        //Redondeamos los pagos individuales para saber cual es el monto de los pagos
        //individuales redondeados
//            double montoTotalRedondeado = 0;
//            for (int i = 0; i < pagosIndividuales.length; i++) {
//                
//                //Regla de 3 para obtener monto correspondiente al monto de la amortizacion
//                double pagoIndividualCorrespondiente = montoAmortizacion * pagosIndividuales[i];
//                pagoIndividualCorrespondiente = pagoIndividualCorrespondiente/montoTotalSinAjuste; 
//                
//                double pagoRedondeado = Math.round(pagoIndividualCorrespondiente);
//   
//                pagosIndividuales[i]=pagoRedondeado;
//                montoTotalRedondeado += pagosIndividuales[i];
//                myLogger.debug("Pago inividual despues: "+pagosIndividuales[i]);
//            }
//            myLogger.info("Monto Total REDONDEADO: "+montoTotalRedondeado);
//            
//            double diferencia = montoAmortizacion - montoTotalRedondeado;
//            myLogger.info("Diferencia Montos: "+diferencia);
//            
//            int sum = 0;
//            //Distribuimos la diferencia entre los integrantes
//            if(diferencia>0){
//                myLogger.info("Dif positiva");
//                sum=1;
//            }else if(diferencia<0){
//                myLogger.info("Dif negativa");
//                diferencia=diferencia*(-1);
//                sum=-1;
//            }
//            myLogger.info("Dif final: "+diferencia);
//            for(int i = 0; i<diferencia; i++){
//                    pagosIndividuales[i]=pagosIndividuales[i]+sum;
//                    myLogger.info("Pago individual ajustado: "+pagosIndividuales[i]);
//            }
        return pagosIndividuales;
    }


    public void doPdfAutorizacion(HttpServletResponse response, ClienteVO cliente, int idSolicitud) throws IOException, DocumentException, ClientesException {

        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        PdfReader reader = new PdfReader(getNombrePlantilla(cliente.solicitudes[indiceSolicitud], 0, ClientesConstants.DOC_FORMAL_AUTORIZACION_DESEMB));
        PdfStamper reciboPDF = new PdfStamper(reader, response.getOutputStream());
        AcroFields reciboForma = reciboPDF.getAcroFields();
        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(cliente.solicitudes[indiceSolicitud].tipoOperacion);
        TasaInteresVO tasa = (TasaInteresVO) catTasas.get(cliente.solicitudes[indiceSolicitud].decisionComite.tasa);
        String frecuencia = "";
        if (cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            frecuencia = "Semanal";
        }
        if (cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            frecuencia = "Quincenal";
        }
        if (cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago == ClientesConstants.PAGO_MENSUAL) {
            frecuencia = "Mensual";
        }

        reciboForma.setField("nombre", cliente.nombre);
        reciboForma.setField("apPaterno", cliente.aPaterno);
        reciboForma.setField("apMaterno", cliente.aMaterno);
        reciboForma.setField("rfc", cliente.rfc);
        reciboForma.setField("calle", cliente.direcciones[0].calle);
        reciboForma.setField("numExterior", cliente.direcciones[0].numeroExterior);
        reciboForma.setField("colonia", cliente.direcciones[0].colonia);
        reciboForma.setField("cp", cliente.direcciones[0].cp);
        reciboForma.setField("municipio", cliente.direcciones[0].municipio);
        reciboForma.setField("estado", cliente.direcciones[0].estado);
        reciboForma.setField("nombreObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].nombre);
        reciboForma.setField("paternoObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].aPaterno);
        reciboForma.setField("maternoObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].aMaterno);
        reciboForma.setField("rfcObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].rfc);
        reciboForma.setField("calleObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.calle);
        reciboForma.setField("numExteriorObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.numeroExterior);
        reciboForma.setField("coloniaObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.colonia);
        reciboForma.setField("cpObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.cp);
        reciboForma.setField("municipioObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.municipio);
        reciboForma.setField("estadoObligado", cliente.solicitudes[indiceSolicitud].obligadosSolidarios[0].direccion.estado);
        reciboForma.setField("montoCredito", "$ " + FormatUtil.formatDobleMiles(cliente.solicitudes[indiceSolicitud].saldo.getMontoCredito()));
        reciboForma.setField("cuota", "$ " + FormatUtil.formatDobleMiles(cliente.solicitudes[indiceSolicitud].cuota));
        reciboForma.setField("tasa", tasa.valor + " %");
        reciboForma.setField("tasaMora", tasa.valor * 2 + " %");
        reciboForma.setField("comisionGestion", "0 %");
        reciboForma.setField("cat", "" + cliente.solicitudes[indiceSolicitud].CAT);
        reciboForma.setField("comisionCobranza", "0 %");
        reciboForma.setField("formaPago", frecuencia);
        reciboForma.setField("plazo", "" + cliente.solicitudes[indiceSolicitud].saldo.getPlazo());
        reciboForma.setField("referencia", cliente.solicitudes[indiceSolicitud].saldo.getReferencia());

        reciboPDF.setFormFlattening(true);
        reciboPDF.close();

    }

    public void doAddendumIC(HttpServletResponse response, GrupoVO grupo, int idCiclo, int semDisp) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            Paragraph p = new Paragraph();
            Color blueFooterColor = new Color(0xB2FCE5);
            String texto = "13317-439-012789";
            Chunk ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            ck.setBackground(blueFooterColor, 210, 2, 210, 3);
            p.add(ck);
            p.setLeading(15);
            HeaderFooter footer = new HeaderFooter(p, false);
            footer.setBorderColor(Color.white);
            footer.setAlignment(Chunk.ALIGN_CENTER);
            document.setFooter(footer);
            document.open();
            contenidoAddendumIC(grupo, idCiclo, semDisp, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doAddendumIC", exception);
        }
    }

    private void contenidoAddendumIC(GrupoVO grupo, int idCiclo, int semDisp, HttpServletResponse response) {
        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            Color grayColor = new Color(0xB0AFAF);
            String cat = "";
            java.sql.Date fechaDate = null;
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            // Espacio para cargar variables
            TablaAmortizacionVO tablaIC[] = null;
            TablaAmortizacionDAO tablaDao = new TablaAmortizacionDAO();
            double montoOriginal = 0;
            double capitalOrdianrio = 0;
            double interesOrdianrio = 0;
            double deudaTotal = 0;
            String fecha = "", mes = "";
            //int numFirmas = 4;
            ArrayList<IntegranteCicloVO> arrInterciclo = new ArrayList<IntegranteCicloVO>();
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2 && grupo.ciclos[idCiclo - 1].estatusIC2 == ClientesConstants.CICLO_DISPERSADO) {
                tablaIC = tablaDao.getElementos(grupo.idGrupo, idCiclo, 0, ClientesConstants.AMORTIZACION_INTERCICLO_2);
            } else {
                tablaIC = tablaDao.getElementos(grupo.idGrupo, idCiclo, 0, ClientesConstants.AMORTIZACION_INTERCICLO);
                capitalOrdianrio = tablaIC[semDisp].saldoCapital + tablaIC[semDisp].abonoCapital;

                for (int i = 0; i < tablaIC.length; i++) {
                    if (i >= semDisp) {
                        interesOrdianrio += tablaIC[i].interes + tablaIC[i].ivaInteres;
                    }
                }
                deudaTotal = capitalOrdianrio + interesOrdianrio;
                p = new Paragraph();
                for (int i = 0; i < grupo.ciclos[idCiclo - 1].integrantes.length; i++) {
                    if (grupo.ciclos[idCiclo - 1].integrantes[i].tipo <= 5) {
                        montoOriginal += grupo.ciclos[idCiclo - 1].integrantes[i].ordenPago.getMonto();
                    } else {
                        if (semDisp == ClientesConstants.DISPERSION_SEMANA_2 && (grupo.ciclos[idCiclo - 1].integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO || grupo.ciclos[idCiclo - 1].integrantes[i].semDisp == ClientesConstants.DISPERSION_SEMANA_2)) {
                            arrInterciclo.add(new IntegranteCicloVO(grupo.ciclos[idCiclo - 1].integrantes[i].idCliente, grupo.ciclos[idCiclo - 1].integrantes[i].idSolicitud,
                                    grupo.ciclos[idCiclo - 1].integrantes[i].nombre, grupo.ciclos[idCiclo - 1].integrantes[i].monto));
                        }
                        if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && (grupo.ciclos[idCiclo - 1].integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 || grupo.ciclos[idCiclo - 1].integrantes[i].semDisp == ClientesConstants.DISPERSION_SEMANA_4)) {
                            arrInterciclo.add(new IntegranteCicloVO(grupo.ciclos[idCiclo - 1].integrantes[i].idCliente, grupo.ciclos[idCiclo - 1].integrantes[i].idSolicitud,
                                    grupo.ciclos[idCiclo - 1].integrantes[i].nombre, grupo.ciclos[idCiclo - 1].integrantes[i].monto));
                        }
                    }
                }

                p = new Paragraph();
                texto = "ADDENDUM AL CONTRATO DE CRÉDITO CELEBRADO EL DÍA ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                    fecha = Convertidor.dateToString(tablaIC[ClientesConstants.DISPERSION_SEMANA_2].fechaPago);
                    fechaDate = tablaIC[ClientesConstants.DISPERSION_SEMANA_2].fechaPago;
                } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                    fecha = Convertidor.dateToString(tablaIC[ClientesConstants.DISPERSION_SEMANA_4].fechaPago);
                    fechaDate = tablaIC[ClientesConstants.DISPERSION_SEMANA_4].fechaPago;
                } else {
                    fecha = Convertidor.dateToString(tablaIC[0].fechaPago);
                    fechaDate = tablaIC[0].fechaPago;
                }

                mes = FechasUtil.obtenNombreMes(fecha);

                texto = "" + fechaDate.getDate();
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.setFont(boldFont);
                p.add(ck);
                p.setFont(bodyFont);
                texto = " DE ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = mes.substring(0, 1) + mes.substring(1, mes.length()).toUpperCase();
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.setFont(boldFont);
                p.add(ck);
                p.setFont(bodyFont);
                texto = " DE ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = FechasUtil.obtenParteFecha(fechaDate, 3);
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.setFont(boldFont);
                p.add(ck);
                p.setFont(bodyFont);
                texto = " EN LO SUCESIVO EL \"CONTRATO\" QUE CELEBRAN POR UNA PARTE CEGE CAPITAL, ";
                texto += " S.A.P.I. DE C.V., SOFOM, E.N.R., REPRESENTADA EN ESTE ACTO POR ________________________, EN LO SUCESIVO \"CEGE CAPITAL\" Y POR LA OTRA PARTE,";
                texto += " LAS PERSONAS AUTORIZADAS Y LOS NUEVOS INTEGRANTES O INTEGRANTES BENEFICIARIOS QUE APARECEN AL FINAL DEL PRESENTE ADDENDUM LLAMADAS";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " " + grupo.nombre;
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.add(ck);
                p.setFont(bodyFont);
                texto = ", EN LO SUCESIVO EL \"GRUPO\", DE CONFORMIDAD CON LO SIGUIENTE;";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                texto = "TÉRMINOS DEFINIDOS\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                texto = "Cualquier término definido que se utilice en el presente documento tendrá el significado que a dicho término se le atribuye en el Contrato.\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                texto = "ANTECEDENTES\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "PRIMERO.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " CEGE CAPITAL celebró con el GRUPO el Contrato, de conformidad con el cual, CEGE CAPITAL otorgó una línea de crédito hasta por el Monto de $";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                texto = " " + String.valueOf(HTMLHelper.formatoMonto(montoOriginal));
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.add(ck);
                texto = " al GRUPO, con un plazo de 16 (dieciséis) semanas.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "SEGUNDO.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " Dentro del mismo Contrato el GRUPO designo a las Personas Autorizadas.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "TERCERO. ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = "El GRUPO, a través de la solicitud que formuló y firmó, requirió a CEGE CAPITAL, el otorgamiento de un crédito, "
                        + "el cual será considerado como un incremento a su línea otorgada en el Contrato.\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                texto = "DECLARACIONES\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "ÚNICO. ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " Las Partes, por su propio derecho o por conducto de su apoderado, respectivamente, manifiestan que:\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "PRIMERA.  ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " Se reconocen mutuamente su legal existencia y capacidad;";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "SEGUNDA. ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " Según corresponda, (i) su representante cuenta con los poderes y facultades suficientes para celebrar el presente Addendum, "
                        + "mismas que no le han sido revocadas o limitadas de forma alguna; y (ii) son un GRUPO de personas físicas de nacionalidad mexicana, "
                        + "mayores de edad y que a la fecha no existe ningún impedimento legal o material para asumir las obligaciones y derechos "
                        + "que a su cargo libremente pacta en el presente Addendum, contando con la capacidad legal suficiente para suscribirlo;";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "TERCERA.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " El Addendum lo celebran: (i) de mutuo acuerdo; (ii) sin que medie coacción alguna de diversa especie; (iii) sin que exista diverso vicio "
                        + "de la voluntad; y, (iv) bajo el amparo de sus estipulaciones.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "CUARTA.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = " Es su voluntad celebrar el Addendum con el objeto de incrementar la línea de crédito y en su caso, adherira nuevos integrantes al GRUPO, "
                        + "según se establece más adelante.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "Las Partes acuerdan celebrar el presente Addendum al tenor de las siguientes:\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "CLÁUSULAS\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "PRIMERA. Objeto. ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = "Mediante el presente Addendumse incrementa la línea de crédito y el GRUPO a través de las Personas Autorizadas y mediante el presente "
                        + "Addendum, instruye, autoriza y faculta a CEGE CAPITAL, en forma expresa e irrevocable, para que el importe del incremento del Crédito "
                        + "sea dividido conforme a la tabla siguiente y, los importes resultantes (en adelante, en singular o en plural, los \"Importes Parciales "
                        + "del Incremento del Crédito\"), sean entregados, respectivamente, a cada uno de los integrantes beneficiarios del incremento o a los nuevos integrantes del GRUPO, mediante cualquiera "
                        + "de los siguientes instrumentos de disposición (en adelante, los \"Instrumentos de Disposición\"), que sean utilizados por alguna "
                        + "institución de crédito (en adelante, el \"Banco\"): (i) dispersiones automatizadas de pagos; (ii) cheques electrónicos; (ii) "
                        + "cheques electrónicos; (iii) tarjetas de débito; y, (iv) cualquier otro instrumento que sea utilizado por el Banco; y, en cualquier "
                        + "sucursal del Banco.\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                int col[] = {50, 50};
                tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100);
                tabla.setWidths(col);

                texto = "Nombres de los integrantes del GRUPO";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setBackgroundColor(grayColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "Importes Parciales del Incremento del Crédito";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setBackgroundColor(grayColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                for (IntegranteCicloVO integrante : arrInterciclo) {
                    texto = "" + integrante.nombre;
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPadding(12);
                    tabla.addCell(cell);
                    texto = "$" + HTMLHelper.formatoMonto(integrante.monto);
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPadding(12);
                    tabla.addCell(cell);
                }
                document.add(tabla);

                texto = "El GRUPO, en este acto, suscribe a su cargo y a favor de CEGE CAPITAL, un pagaré cuyo importe asciende al Importe Total del Incremento del "
                        + "Crédito y, el que a su vez, documenta la disposición que realiza el GRUPO sobre el Incremento del Importe del Crédito "
                        + "(en adelante, el \"Pagaré\")\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                texto = "Las personas antes mencionadas en este acto se constituyen voluntariamente en lo personal y por propio derecho como integrantes del GRUPO,"
                        + " lo anterior en el entendido que todas y cada una de las obligaciones contraídas por el GRUPO se encontrarán vigentes hasta que se de "
                        + "cumplimiento a las mismas y CEGE CAPITAL manifieste su conformidad.\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "SEGUNDA. Obligación Solidaria.   ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = "Cualquiera de los integrantes del GRUPO, estará solidariamente obligado, respecto del Crédito y el incremento del mismo, ante CEGE CAPITAL, "
                        + "al cumplimiento de cualquier obligación que, en términos del Contrato y el presente Addendum, sea a cargo del GRUPO. Por lo anterior, "
                        + "CEGE CAPITAL podrá exigir indistintamente a cualquiera de los integrantes del GRUPO, el cumplimiento de la obligación de que se trate.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "TERCERA. Ratificación y Reconocimiento de Adeudo.  ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = "Las Partes acuerdan ratificar todos los términos pactados por las mismas en el Contrato de Crédito, salvo por las modificaciones "
                        + "que se mencionan en la Cláusula Primera anterior.";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "El GRUPO expresamente reconoce que a la fecha de celebración del presente Addendum adeudan a CEGE CAPITAL:\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                col = new int[]{25, 75};
                tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100);
                tabla.setWidths(col);

                texto = "De Capital:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "$" + HTMLHelper.formatoMonto(capitalOrdianrio);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "De Intereses Ordinarios:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "$" + HTMLHelper.formatoMonto(interesOrdianrio);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "De Intereses Moratorios:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "$0";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);;
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "Deuda Total:";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "$" + HTMLHelper.formatoMonto(deudaTotal);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                document.add(tabla);

                p = new Paragraph();
                texto = "\nCUARTA. Ausencia de Novación.   ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.add(ck);
                texto = "La Celebración de este Addendum no implica novación de las obligaciones establecidas para cada una de las Partes en el Contrato. ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                p = new Paragraph();
                texto = "Leído y comprendido su contenido y alcance del presente Addendum por las partes, lo firman de conformidad a los ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                texto = "" + fechaDate.getDate();
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.add(ck);
                texto = " días del mes de ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                texto = mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase();
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.add(ck);
                texto = " de ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                texto = FechasUtil.obtenParteFecha(fechaDate, 3);
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.add(ck);
                texto = " en la ciudad de ";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                texto = sucursal.municipio.toUpperCase();
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
                p.setFont(boldFont);
                p.add(ck);
                p.setFont(bodyFont);
                texto = ".\n\n";
                ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.add(ck);
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                p.setLeading(12);
                document.add(p);

                texto = "LISTADO DE PERSONAS AUTORIZADAS E INTEGRANTES DEL GRUPO\n\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                p.setAlignment(Chunk.ALIGN_CENTER);
                p.setLeading(12);
                document.add(p);

                col = new int[]{75, 25};
                tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100);
                tabla.setWidths(col);

                texto = "NOMBRE(S) Y APELLIDO(S) COMPLETO / DOMICILIO";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setBackgroundColor(grayColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "FIRMA";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setBackgroundColor(grayColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                for (int i = 0; i < (arrInterciclo.size() + 4); i++) {
                    texto = "\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPadding(8);
                    tabla.addCell(cell);

                    texto = "\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPadding(8);
                    tabla.addCell(cell);
                }
                document.add(tabla);

            }
        } catch (Exception e) {
            myLogger.error("contenidoDocContratoComunal", e);
        }

    }

        /**
     * Descarta los integrantes que son de interciclo que no les corresponde la semana
     * del control de pagos que se esta generando
     * 
     * @param integrantesTotales son los integrantes totales del ciclo
     * 
     * @return integrantes que corresponden unicamente a la semana del control de pagos
     */
    public IntegranteCicloVO [] descartaIntegrantesAnterior(CicloGrupalVO ciclo, GrupoVO grupo) throws ClientesException{
        java.util.List<IntegranteCicloVO> integrantesCorrespondientes = new ArrayList<IntegranteCicloVO>();

        TablaAmortDAO tablaDao = new TablaAmortDAO();
        for(IntegranteCicloVO ic : ciclo.integrantes){
            if(ic.estatus == ClientesConstants.INTEGRANTE_CANCELADO){
                myLogger.info("Integrante Cancelado: "+ic.getIdCliente());
                OrdenDePagoDAO ordenPago = new OrdenDePagoDAO();
                OrdenDePagoVO orden = ordenPago.getOrdenPago(ic.getIdCliente(), ic.getIdSolicitud());
                myLogger.info("Orden de pago: "+orden);
                if(orden != null && orden.getEstatus() == ClientesConstants.OP_DEVUELTA){
                    myLogger.info("Orden de pago ref: "+orden.getReferencia());


                    TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
                    myLogger.info("Size amort: "+tablasAmor.length);
                    int numPago = 0;
                    for(TablaAmortVO ta : tablasAmor){
                        myLogger.info("monto anticipado: "+ta.capitalAnticipado);
                        if(ta.capitalAnticipado>0){
                            myLogger.info("Descarta por la cantidad: "+ta.getCapitalAnticipado()+" en el num pago: "+ta.getNumPago());
                            numPago = ta.getNumPago();
                            break;
                        }
                    }
                    
                    if(numPago>0){
                        myLogger.info("Descarta integrante CANCELADO del control de pagos");
                        continue;
                    }
                    
                }else{
                    myLogger.info("No tiene orden de pago o aun no esta devuelta");
                }
            }else if((ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)
                    || (ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2)){
                myLogger.info("Revisamos si interciclo ya tiene el incremento");
                
                int semanaIncremento = (ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)?1:3;
                TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
                myLogger.info("Size amort: "+tablasAmor.length);
                
                for(TablaAmortVO ta : tablasAmor){
                    myLogger.info("Incremento Capital: "+ta.incrementoCapital);
                }
                    
                if(tablasAmor[semanaIncremento].incrementoCapital <= 0){
                    myLogger.info("No tiene incremento en capital aun... se descarta cliente");
                    continue;
                }
            }
            integrantesCorrespondientes.add(ic);
        }
        
        IntegranteCicloVO [] integrantesFinales = new IntegranteCicloVO [integrantesCorrespondientes.size()];
        for(int i = 0; i < integrantesCorrespondientes.size(); i++){
            integrantesFinales[i]=integrantesCorrespondientes.get(i);
        }
        
        return integrantesFinales;
    }
	
	    /**
     * JECB 01/10/2017
     * Método que genera la orde de pago de adicionales por semana
     * @param response response de httpServlet a donde se enviara el documento generado
     * @param grupo bean del grupo que contiene el adicional al cual se le quiere imprimir las odp
     * @param ciclo bean del ciclo que contiene el adicional al cual se requiere imprimir las odp
     * @param semDispAdicional 
     */
    public void doOrdenPagoAdicional(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDispAdicional) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoOrdenPagoAdicional(grupo, ciclo, response, semDispAdicional);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doOrdenPagoPDF", exception);
        }
    }

    /**
     * JECB 01/10/2017
     * Método que construye las ordes de pago de adicional por 
     * @param grupo bean del grupo de adicional
     * @param ciclo bean del ciclo de adicional
     * @param response respuesta http
     * @param semDispAdicional semana de adicional
     */
    private void contenidoOrdenPagoAdicional(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response, int semDispAdicional) {

        try {
            Paragraph p = null;
            Paragraph pSegunda = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            Color greenColor = new Color(0x03520D);
            Color greenLetterColor = new Color(0x418248);
            Color redLetterColor = new Color(0x6F0917);
            String texto = "";
            String fecha = null;//Convertidor.timestampToString(ciclo.integrantes[i].ordenPago.getFechaEnvio());
            String cve_Banorte = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
            String cve_Banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
            String cve_Scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
            String cve_Bancomer = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
            String cve_Santander = CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER");
            OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            int numTarjetas = 0, j = 0;
            int tamanoLetra = 12;

            myLogger.debug("Semana de adicional:" + semDispAdicional);
            int tipoAdicional = AdicionalUtil.obtieneTipoAdicional(semDispAdicional);
            myLogger.debug("tipo de adicional:" + tipoAdicional);
            
            //Obtenemos los integrantes que tiene asignado credito adicional 
            //que coincida con la semana de adicional
            java.util.List<IntegranteCicloVO> listIC = new ArrayList<IntegranteCicloVO>();
            for(IntegranteCicloVO icTmp : ciclo.integrantes){
                if(icTmp.tipo_adicional == tipoAdicional ){
                    listIC.add((IntegranteCicloVO)icTmp.clone());
                }
            }
            
            //generamos String con los id de integrantes ciclo que se empleara 
            //en la consulta de ordenes de pago
            String idsIC = null;
            if(!listIC.isEmpty()){
                for(IntegranteCicloVO icTmp : listIC){
                    idsIC = icTmp.idCliente +",";
                }
                idsIC = idsIC.substring(0, idsIC.length() - 1);
            }
            myLogger.debug("id numero de clientes:" + idsIC);
            
            java.util.List<OrdenDePagoVO> listOrdPag = null;
            if(!listIC.isEmpty()){
                OrdenDePagoDAO odDao = new OrdenDePagoDAO();
                //Obtencion de todas las  ordenes de pago de los clientes que solo tienen 
                //asignado un adicional
                listOrdPag =  odDao.getOrdenesPagoXGrupoCicloIdCliente(grupo.idGrupo, ciclo.idCiclo, idsIC);
                if(listOrdPag != null && !listOrdPag.isEmpty()){
                    //Filtramos las ordenes de pago por integrante ciclo 
                    for(IntegranteCicloVO icTmp : listIC){
                        //Lista que almacena de manera temporal las odp pertenecientes al 
                        //integrante ciclo y que sean solo de adicional
                        java.util.List<OrdenDePagoVO> odpCoinciden = new java.util.ArrayList<OrdenDePagoVO>();
                        for(OrdenDePagoVO odpTmp : listOrdPag){
                            if(icTmp.getIdCliente() == odpTmp.getIdCliente() 
                                    && icTmp.idSolicitud == odpTmp.getIdSolicitud() 
                                    && AdicionalUtil.isOrdenPagoAdicional(odpTmp) ){
                                odpCoinciden.add(odpTmp);
                            }
                        }
                        
                        if(odpCoinciden.size() == 1){
                            //Si el intregante ciclo solo tiene una orden de pago 
                            //asignada se asigna como atributo a la colleccion de integrante ciclol
                            icTmp.ordenPago = odpCoinciden.get(0);
                        }else{
                            //Si el integrante ciclo tiene mas de una odp de adicinal asignada, significa que 
                            //ha cancelado odp de adicional previamente, por lo que se itera las odp del integrante 
                            //ciclo de adicional para quedarse con la mas actual que no este en estatus devuelta
                            for(java.util.Iterator<OrdenDePagoVO> itr = odpCoinciden.iterator(); itr.hasNext();){
                                OrdenDePagoVO ordPag = itr.next();
                                if(ordPag.getEstatus() != ClientesConstants.OP_DEVUELTA){
                                    icTmp.ordenPago = ordPag;
                                }
                            }
                        }
                    }
                }
            }
            
            IntegranteCicloVO[] arrayIC = new IntegranteCicloVO[listIC.size()];
            listIC.toArray(arrayIC);
            for (int z = 0; arrayIC != null && z < arrayIC.length; z++) {
                boolean imprime = false;
                //ciclo.integrantes[i].tarjetaCobro = tarjetaDAO.getTarjetaClinete(ciclo.integrantes[i].idCliente, temporal.idSolicitud);
                if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                    tamanoLetra = 8;
                }
                if (arrayIC[z].ordenPago != null && arrayIC[z].medioDisp == 0) {
                    if (tipoAdicional == arrayIC[j].tipo_adicional ) {
                        imprime = true;
                    } 
                    if (imprime) {
                        p = new Paragraph();
                        texto = "ORDEN DE PAGO";
                        ck = new Chunk(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
                        p.add(ck);
                        texto = "________________\n\n";
                        ck = new Chunk(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.add(ck);
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        pSegunda = p;

                        int col[] = {38, 62};
                        tabla = new PdfPTable(2);
                        tabla.setWidthPercentage(100F);
                        tabla.setWidths(col);
                        texto = "FECHA:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        fecha = Convertidor.timestampToString(arrayIC[z].ordenPago.getFechaEnvio());
                        texto = fecha;
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "BENEFICIARIO:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        texto = arrayIC[z].ordenPago.getNombre();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "REFERENCIA:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                            texto = cve_Banorte + arrayIC[z].ordenPago.getReferencia();
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                            texto = cve_Banamex + arrayIC[z].ordenPago.getReferencia();
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                            texto = cve_Scotia + arrayIC[z].ordenPago.getReferencia();
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = cve_Bancomer + arrayIC[z].ordenPago.getReferencia();
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_TELECOM) {
                            texto = ordenDAO.getReferenciaTelecom(arrayIC[z].idCliente, arrayIC[z].idSolicitud);
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            texto = cve_Santander.substring(4, 11) + arrayIC[z].ordenPago.getReferencia();
                        }
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        texto = "IMPORTE:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        texto = "" + arrayIC[z].ordenPago.getMonto();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        /*
                         texto = "CONVENIO:";
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);
                         cell.setBorderColor(Color.WHITE);
                         texto = "0"+CVE_BANCO;
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);            
                         texto = "CONCEPTO:";
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);
                         cell.setBorderColor(Color.WHITE);
                         texto = "ODP"+ciclo.integrantes[i].ordenPago.getIdCliente();
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);            
                         texto = "CLAVE DE IDENTIFICACION:";
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, greenLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);
                         cell.setBorderColor(Color.WHITE);
                         texto = "IFE";
                         p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                         cell = new PdfPCell(p);
                         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                         cell.setVerticalAlignment(Element.ALIGN_CENTER);
                         cell.setBorder(20);
                         cell.setBorderColor(Color.WHITE);
                         tabla.addCell(cell);
                         *
                         */
                        texto = "BANCO:";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        cell.setBorderColor(Color.WHITE);
                        if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANORTE) {
                            texto = "BANORTE";
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANAMEX) {
                            texto = "BANAMEX No Emisor 48";
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SCOTIABANK) {
                            texto = "SCOTIABANK No Empresa " + cve_Scotia;
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = "BBVA-BANCOMER Convenio 0" + cve_Bancomer;
                            texto += "\nConcepto ODP" + arrayIC[z].ordenPago.getIdCliente() + " Clave de Identificacion IFE";
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_TELECOM) {
                            texto = "TELECOM Documento de Identificacion IFE";
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            texto = "SANTANDER";
                        }
                        if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD, redLetterColor));
                        } else if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.BOLD, redLetterColor));
                        } else {
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, redLetterColor));
                        }
                        if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            texto = "CONTRATO:";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            cell.setBorderColor(Color.WHITE);
                            texto = CatalogoHelper.getParametro("NUM_CONTRATO_SANTANDER");;
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));

                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            texto = "Clave beneficiario:";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, greenLetterColor));
                            cell = new PdfPCell(p);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setBorder(20);
                            cell.setBorderColor(Color.WHITE);
                            tabla.addCell(cell);
                            cell.setBorderColor(Color.WHITE);
                            texto = StringUtils.leftPad(arrayIC[z].ordenPago.getIdCliente() + "", 7, "0") + StringUtils.leftPad(arrayIC[z].ordenPago.getIdSolicitud() + "", 2, "0");
                            p = new Paragraph(texto, new Font(Font.HELVETICA, tamanoLetra, Font.BOLD, redLetterColor));

                        }

                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(20);
                        cell.setBorderColor(Color.WHITE);
                        tabla.addCell(cell);
                        document.add(tabla);

                        texto = "________________\n\n";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);

                        if (arrayIC[z].ordenPago.getIdBanco() == ClientesConstants.ID_BANCO_BANCOMER) {
                            texto = "____________________________________________________________________\n\n";
                        } else {
                            texto = "\n\n____________________________________________________________________\n\n";
                        }
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);

                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, greenLetterColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        document.add(pSegunda);
                        document.add(tabla);
                        texto = "________________\n";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 55, Font.NORMAL, greenColor));
                        p.setAlignment(Chunk.ALIGN_CENTER);
                        p.setLeading(12);
                        document.add(p);
                        if (z % 2 == 0) {
                            texto = "\n_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n\n";
                            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD, Color.GRAY));
                            p.setAlignment(Chunk.ALIGN_CENTER);
                            p.setLeading(12);
                            document.add(p);
                        } else {
                            document.newPage();
                        }
                    }
                } else {
                    numTarjetas++;
                }
            }
            if (numTarjetas > 0) {
                int colList[] = {8, 50, 20, 22};
                tabla = new PdfPTable(4);
                tabla.setWidthPercentage(100);
                tabla.setWidths(colList);
                texto = "TARJETAS DE PAGO";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre de la Sucursal :        " + sucursal.nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre del Equipo :            " + grupo.nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Banco :   ";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Fecha :    " + Convertidor.dateToString(new java.util.Date());
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "No.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Nombre del Cliente";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Numero de Tarjeta";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                texto = "Firma";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
                for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                    if (ciclo.integrantes[i].medioDisp == 1) {
                        texto = "" + (++j);
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        texto = "" + ciclo.integrantes[i].nombre;
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        texto = "" + ciclo.integrantes[i].tarjetaCobro.getTarjeta();
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 9, Font.NORMAL));
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(12);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setPadding(12);
                        tabla.addCell(cell);
                    }
                }
                document.add(tabla);
            }
        } catch (Exception e) {
            myLogger.error("contenidoOrdenPagoComunal", e);
        }
    }
    
    public void doAddendumAdicional(HttpServletResponse response, GrupoVO grupo, int idCiclo, int semDispAd) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            boldFont = new Font(letra, 8F, Font.BOLD);
            bodyFont = new Font(letra, 8F, Font.NORMAL);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            Paragraph p = new Paragraph();
            Color blueFooterColor = new Color(0xB2FCE5);
            String texto = "13317-439-012789";
            Chunk ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            ck.setBackground(blueFooterColor, 210, 2, 210, 3);
            p.add(ck);
            p.setLeading(15);
            HeaderFooter footer = new HeaderFooter(p, false);
            footer.setBorderColor(Color.white);
            footer.setAlignment(Chunk.ALIGN_CENTER);
            document.setFooter(footer);
            document.open();
            contenidoAddendumAdicional(grupo, idCiclo, semDispAd, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doAddendumIC", exception);
        }
    }
    
    private void contenidoAddendumAdicional(GrupoVO grupo, int idCiclo, int semDisp, HttpServletResponse response) {
        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            Color grayColor = new Color(0xB0AFAF);
            String cat = "";
            java.sql.Date fechaDate = null;
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            // Espacio para cargar variables
            TablaAmortizacionVO tablaAdicional[] = null;
            TablaAmortizacionDAO tablaDao = new TablaAmortizacionDAO();
            double montoOriginal = 0;
            double capitalOrdianrio = 0;
            double interesOrdianrio = 0;
            double deudaTotal = 0;
            String fecha = "", mes = "";
            //int numFirmas = 4;
            ArrayList<IntegranteCicloVO> arrAdicional = new ArrayList<IntegranteCicloVO>();
            
            myLogger.debug("Semana dispersion adicional:" +semDisp);
            //Ontenemos el mapeo de los tipos de amortizacion p
            //////Map<Integer,Integer> map = TablaAmortizacionUtil.obtenerMapeoProductoTipoAmortizacion(grupo, idCiclo);
            //////int tipoAdicional = AdicionalUtil.obtieneTipoAdicional(semDisp);
            //////myLogger.debug("contenidoAddendumAdicional tipoAdicional:" + tipoAdicional);
            /////int tipoAmortizacion = map.get(tipoAdicional);
            /////myLogger.debug("contenidoAddendumAdicional tipoAmortizacion:" + tipoAmortizacion);   
            
            
            tablaAdicional = tablaDao.getElementos(grupo.idGrupo, idCiclo, 0, 1);
            capitalOrdianrio = tablaAdicional[semDisp].saldoCapital + tablaAdicional[semDisp].abonoCapital;

            for (int i = 0; i < tablaAdicional.length; i++) {
                if (i >= semDisp) {
                    interesOrdianrio += tablaAdicional[i].interes + tablaAdicional[i].ivaInteres;
                }
            }
            deudaTotal = capitalOrdianrio + interesOrdianrio;
            p = new Paragraph();
            
            double sumatoriaSegContratado = SegurosUtil.sumatoriaSegurosXGrupo(grupo.ciclos[idCiclo - 1].integrantes);
            myLogger.debug("Costo seguro contratado:"+sumatoriaSegContratado);
            montoOriginal = grupo.ciclos[idCiclo - 1].monto + sumatoriaSegContratado ;
            
            for (int i = 0; i < grupo.ciclos[idCiclo - 1].integrantes.length; i++) {
                //montoOriginal += grupo.ciclos[idCiclo - 1].integrantes[i].monto -  grupo.ciclos[idCiclo - 1].integrantes[i].montoAdicional;
                
                if(grupo.ciclos[idCiclo - 1].integrantes[i].tipo_adicional > 0 
                        && AdicionalUtil.obtieneTipoAdicional(semDisp) == grupo.ciclos[idCiclo - 1].integrantes[i].tipo_adicional){
                    arrAdicional.add(grupo.ciclos[idCiclo - 1].integrantes[i]);
                }
            }

            p = new Paragraph();
            texto = "ADDENDUM AL CONTRATO DE CRÉDITO CELEBRADO EL DÍA ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            
            fecha = Convertidor.dateToString(tablaAdicional[semDisp].fechaPago);
            fechaDate = tablaAdicional[semDisp].fechaPago;

            mes = FechasUtil.obtenNombreMes(fecha);

            texto = "" + fechaDate.getDate();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " DE ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = mes.substring(0, 1) + mes.substring(1, mes.length()).toUpperCase();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " DE ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = FechasUtil.obtenParteFecha(fechaDate, 3);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = " EN LO SUCESIVO EL \"CONTRATO\" QUE CELEBRAN POR UNA PARTE CEGE CAPITAL, ";
            texto += " S.A.P.I. DE C.V., SOFOM, E.N.R., REPRESENTADA EN ESTE ACTO POR ________________________, EN LO SUCESIVO \"CEGE CAPITAL\" Y POR LA OTRA PARTE,";
            texto += " LAS PERSONAS AUTORIZADAS Y LOS NUEVOS INTEGRANTES O INTEGRANTES BENEFICIARIOS QUE APARECEN AL FINAL DEL PRESENTE ADDENDUM LLAMADAS";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " " + grupo.nombre;
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            p.setFont(bodyFont);
            texto = ", EN LO SUCESIVO EL \"GRUPO\", DE CONFORMIDAD CON LO SIGUIENTE;";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "TÉRMINOS DEFINIDOS\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            texto = "Cualquier término definido que se utilice en el presente documento tendrá el significado que a dicho término se le atribuye en el Contrato.\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "ANTECEDENTES\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "PRIMERO.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " CEGE CAPITAL celebró con el GRUPO el Contrato, de conformidad con el cual, CEGE CAPITAL otorgó una línea de crédito hasta por el Monto de $";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = " " + String.valueOf(HTMLHelper.formatoMonto(montoOriginal));
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " al GRUPO, con un plazo de 16 (dieciséis) semanas.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "SEGUNDO.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " Dentro del mismo Contrato el GRUPO designo a las Personas Autorizadas.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "TERCERO. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "El GRUPO, a través de la solicitud que formuló y firmó, requirió a CEGE CAPITAL, el otorgamiento de un crédito, "
                    + "el cual será considerado como un incremento a su línea otorgada en el Contrato.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "DECLARACIONES\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "ÚNICO. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " Las Partes, por su propio derecho o por conducto de su apoderado, respectivamente, manifiestan que:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "PRIMERA.  ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " Se reconocen mutuamente su legal existencia y capacidad;";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "SEGUNDA. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " Según corresponda, (i) su representante cuenta con los poderes y facultades suficientes para celebrar el presente Addendum, "
                    + "mismas que no le han sido revocadas o limitadas de forma alguna; y (ii) son un GRUPO de personas físicas de nacionalidad mexicana, "
                    + "mayores de edad y que a la fecha no existe ningún impedimento legal o material para asumir las obligaciones y derechos "
                    + "que a su cargo libremente pacta en el presente Addendum, contando con la capacidad legal suficiente para suscribirlo;";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "TERCERA.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " El Addendum lo celebran: (i) de mutuo acuerdo; (ii) sin que medie coacción alguna de diversa especie; (iii) sin que exista diverso vicio "
                    + "de la voluntad; y, (iv) bajo el amparo de sus estipulaciones.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "CUARTA.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = " Es su voluntad celebrar el Addendum con el objeto de incrementar la línea de crédito y en su caso, adherira nuevos integrantes al GRUPO, "
                    + "según se establece más adelante.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "Las Partes acuerdan celebrar el presente Addendum al tenor de las siguientes:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "CLÁUSULAS\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "PRIMERA. Objeto. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Mediante el presente Addendumse incrementa la línea de crédito y el GRUPO a través de las Personas Autorizadas y mediante el presente "
                    + "Addendum, instruye, autoriza y faculta a CEGE CAPITAL, en forma expresa e irrevocable, para que el importe del incremento del Crédito "
                    + "sea dividido conforme a la tabla siguiente y, los importes resultantes (en adelante, en singular o en plural, los \"Importes Parciales "
                    + "del Incremento del Crédito\"), sean entregados, respectivamente, a cada uno de los integrantes beneficiarios del incremento o a los nuevos integrantes del GRUPO, mediante cualquiera "
                    + "de los siguientes instrumentos de disposición (en adelante, los \"Instrumentos de Disposición\"), que sean utilizados por alguna "
                    + "institución de crédito (en adelante, el \"Banco\"): (i) dispersiones automatizadas de pagos; (ii) cheques electrónicos; (ii) "
                    + "cheques electrónicos; (iii) tarjetas de débito; y, (iv) cualquier otro instrumento que sea utilizado por el Banco; y, en cualquier "
                    + "sucursal del Banco.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            int col[] = {50, 50};
            tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);

            texto = "Nombres de los integrantes del GRUPO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "Importes Parciales del Incremento del Crédito";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            for (IntegranteCicloVO integrante : arrAdicional) {
                texto = "" + integrante.nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(12);
                tabla.addCell(cell);
                texto = "$" + HTMLHelper.formatoMonto(integrante.montoAdicional);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(12);
                tabla.addCell(cell);
            }
            document.add(tabla);

            texto = "El GRUPO, en este acto, suscribe a su cargo y a favor de CEGE CAPITAL, un pagaré cuyo importe asciende al Importe Total del Incremento del "
                    + "Crédito y, el que a su vez, documenta la disposición que realiza el GRUPO sobre el Incremento del Importe del Crédito "
                    + "(en adelante, el \"Pagaré\")\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "Las personas antes mencionadas en este acto se constituyen voluntariamente en lo personal y por propio derecho como integrantes del GRUPO,"
                    + " lo anterior en el entendido que todas y cada una de las obligaciones contraídas por el GRUPO se encontrarán vigentes hasta que se de "
                    + "cumplimiento a las mismas y CEGE CAPITAL manifieste su conformidad.\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "SEGUNDA. Obligación Solidaria.   ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Cualquiera de los integrantes del GRUPO, estará solidariamente obligado, respecto del Crédito y el incremento del mismo, ante CEGE CAPITAL, "
                    + "al cumplimiento de cualquier obligación que, en términos del Contrato y el presente Addendum, sea a cargo del GRUPO. Por lo anterior, "
                    + "CEGE CAPITAL podrá exigir indistintamente a cualquiera de los integrantes del GRUPO, el cumplimiento de la obligación de que se trate.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "TERCERA. Ratificación y Reconocimiento de Adeudo.  ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Las Partes acuerdan ratificar todos los términos pactados por las mismas en el Contrato de Crédito, salvo por las modificaciones "
                    + "que se mencionan en la Cláusula Primera anterior.";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "El GRUPO expresamente reconoce que a la fecha de celebración del presente Addendum adeudan a CEGE CAPITAL:\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            col = new int[]{25, 75};
            tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);

            texto = "De Capital:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "$" + HTMLHelper.formatoMonto(capitalOrdianrio);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "De Intereses Ordinarios:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "$" + HTMLHelper.formatoMonto(interesOrdianrio);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "De Intereses Moratorios:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "$0";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);;
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "Deuda Total:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "$" + HTMLHelper.formatoMonto(deudaTotal);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            document.add(tabla);

            p = new Paragraph();
            texto = "\nCUARTA. Ausencia de Novación.   ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "La Celebración de este Addendum no implica novación de las obligaciones establecidas para cada una de las Partes en el Contrato. ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "Leído y comprendido su contenido y alcance del presente Addendum por las partes, lo firman de conformidad a los ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "" + fechaDate.getDate();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " días del mes de ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " de ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = FechasUtil.obtenParteFecha(fechaDate, 3);
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = " en la ciudad de ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = sucursal.municipio.toUpperCase();
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ".\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "LISTADO DE PERSONAS AUTORIZADAS E INTEGRANTES DEL GRUPO\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            col = new int[]{75, 25};
            tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);

            texto = "NOMBRE(S) Y APELLIDO(S) COMPLETO / DOMICILIO";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            texto = "FIRMA";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setBackgroundColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            tabla.addCell(cell);

            for (int i = 0; i < (arrAdicional.size() + 4); i++) {
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);

                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                tabla.addCell(cell);
            }
            document.add(tabla);

            
        } catch (Exception e) {
            myLogger.error("contenidoDocContratoComunal", e);
        }

    }
    
    public void generaHojaResumenAnterior(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) throws CommandException {
        GeneraDocumentosHelper docHelper = new GeneraDocumentosHelper();
        try {
            String nombreArchivo = "";
            int plazo = 0, plazoInterciclo = 12, plazoGrupal = 0;
            if (ciclo.tablaAmortizacion != null) {
                plazo = ciclo.tablaAmortizacion.length - 1;
                plazoGrupal = ciclo.tablaAmortizacion.length - 1;
            }
            
            TablaAmortDAO tablaDao = new TablaAmortDAO();
            TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
            
            TablaAmortVO tablaAmortizacion = new TablaAmortVO();
            tablaAmortizacion = tablaDao.getDivVigente(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            if (tablaAmortizacion == null) {
                tablaAmortizacion = tablaDao.getUltmioDivPagado(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            }
            int numPago = 1;
            if (tablaAmortizacion != null) {
                numPago = tablaAmortizacion.getNumPago();
            }
            
            IntegranteCicloVO [] integrantesFinales = descartaIntegrantesAnterior(ciclo, grupo);

            if (integrantesFinales != null) {
                nombreArchivo = (integrantesFinales.length > 20 ? "HojaResumenGrupo40.pdf" : "HojaResumenGrupo.pdf");
            }

            PdfReader reader = new PdfReader(ClientesConstants.RUTA_BASE_ARCHIVOS + "Contratos\\Comunal\\" + nombreArchivo);
            PdfStamper reciboPDF = new PdfStamper(reader, response.getOutputStream());
            AcroFields reciboForma = reciboPDF.getAcroFields();

            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fechaImp = dateFormat.format(date);

            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);

            ComisionVO comision = new ComisionVO();
            double comisiones = (ciclo.tablaAmortizacion.length > 0 ? (ciclo.tablaAmortizacion[0].comisionInicial + ciclo.tablaAmortizacion[0].ivaComision) : 0);
            double montoCredito = (ciclo.tablaAmortizacion.length > 0 ? ciclo.tablaAmortizacion[0].saldoInicial : 0);
            comision.porcentaje = (comisiones / montoCredito) * 100;
            String tasaComisionPrint = String.valueOf(HTMLHelper.formatoMonto(comision.porcentaje));

            TreeMap catAsesores = CatalogoHelper.getCatalogoEjecutivos(grupo.sucursal);
            Set set = catAsesores.keySet();
            Iterator llaves = set.iterator();
            Object key = null;

            StringBuffer codigo = new StringBuffer();
            while (llaves.hasNext()) {
                key = llaves.next();
                if (key.toString().equals(String.valueOf(ciclo.asesor))) {
                    codigo.append(catAsesores.get(key).toString());
                }
            }

            // Dependiendo del numero de integrantes es que se llena la forma, esto se realizo debido a un problema en la edicion de la forma
            // HojaResumenComunal40, la cual pedia el underscore _ en lugar de espacio.
            if (integrantesFinales.length < 21) {
                reciboForma.setField("FechaImpresion", fechaImp);
                reciboForma.setField("nombreGrupo", grupo.nombre);
                reciboForma.setField("asesor", codigo.toString());
                reciboForma.setField("ciclo", String.valueOf(ciclo.idCiclo));
                reciboForma.setField("comunidad", ciclo.direccionReunion.colonia);
                reciboForma.setField("fechaDesembolso", HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago));
                reciboForma.setField("fechaUltimoPago", HTMLHelper.displayField(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
                reciboForma.setField("tasa", tasa.descripcion);
                reciboForma.setField("comision", tasaComisionPrint + " %");
                reciboForma.setField("frecuenciaJuntas", "SEMANAL");
                reciboForma.setField("noSemanas", String.valueOf(plazo));

                
                double montoTotalAmortizaciones = 0;
                double montoAmortSemana15 = 0;
                int pi = 1; //indice de la plantilla
                //Segundo bloque escribe fechas de pago 
                for (int i = 0; tablasAmor != null && i < tablasAmor.length; i++) {                    
                    reciboForma.setField("Semana " + pi, HTMLHelper.displayField(tablasAmor[i].fechaPago));
                    myLogger.info("Monto pagado: "+tablasAmor[i].totalPagado +" monto pagar: "+ tablasAmor[i].montoPagar);
                    montoTotalAmortizaciones += tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    if(i == (tablasAmor.length-2)){
                        montoAmortSemana15 = tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    }
                    pi =pi+1;
                }
                myLogger.info("Monto amortizacion semana 15: "+montoAmortSemana15);
                myLogger.info("Monto Total amortizaciones: "+montoTotalAmortizaciones);
                
                //Tercer Bloque Uso Oficial  de OM
                reciboForma.setField("No Grupo", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No Cuenta", ciclo.referencia);
                reciboForma.setField("No Cliente", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No Contrato", "No Disponible");

                //Bloque informacion de pagos
                reciboForma.setField("NumConvenioBancomer", ClientesConstants.CUENTA_BANCOMER);
                reciboForma.setField("NumCuentaBansefi", ClientesConstants.CUENTA_BANSEFI);
                reciboForma.setField("NumCuentaBanorte", ClientesConstants.CUENTA_BANORTE);
                reciboForma.setField("NumCuentaBajio", ClientesConstants.CUENTA_BAJIO);
                reciboForma.setField("NumCuentaAfirme", ClientesConstants.CUENTA_BANCO_AFIRME);
                reciboForma.setField("NumReferencia", ciclo.referencia);

                //Bloque integrantes
                int cont = 0;
                double tempMontoDesembolsado = 0.0;
                double tempComSeg = 0.0;
                double tempInteres = 0.0;
                double tempPagoIndividual = 0.0;
                double pagTotalTotal = 0;
                
                double [] interesesIndividuales = new double[integrantesFinales.length];
                double [] pagSemanalColumna = obtenerPagosIndividualesAnterior(integrantesFinales, montoAmortSemana15, tasa, catComisiones, ciclo.getPlazo());
                double [] montoSinComision = new double[integrantesFinales.length];
                double [] montoSinComisionPDF = new double[integrantesFinales.length];
                double [] comisionSeguroRefinaciado = new double[integrantesFinales.length];
                double [] interesesAdicionales = new double[integrantesFinales.length];
                
                double montoTotalInteresesAdicionales = 0.0;//codigo para adicional
                //Obtenemos primero los montos individucales y el monto total sin redondeos
                for(int i = 0; i < integrantesFinales.length; i++){
                    
                        montoSinComisionPDF[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        montoSinComision[i] = montoSinComisionPDF[i];
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            montoSinComision[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        }
                    
                        montoSinComision[i] = FormatUtil.redondeaMoneda(montoSinComision[i]);
                        
                        comisionSeguroRefinaciado[i] = (integrantesFinales[i].monto - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            comisionSeguroRefinaciado[i] = (integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        }
                        
                        comisionSeguroRefinaciado[i] = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado[i]);
                        tempComSeg += comisionSeguroRefinaciado[i];
                        plazo = plazoGrupal;
                        if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_2;
                        } else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_4;
                        }
                        double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
                        interes = (montoSinComision[i] + comisionSeguroRefinaciado[i]) * interes;
                        interes = FormatUtil.redondeaMoneda(interes);
                        
                        double pagoIndividual = (montoSinComision[i] + comisionSeguroRefinaciado[i] + interes) / (plazo);
                        pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                        
                        if(integrantesFinales[i].tipo_adicional!=0){
                            int plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
                            myLogger.info("monto adicional: "+integrantesFinales[i].montoAdicional);
                            double interesAdi = integrantesFinales[i].montoAdicional *(AdicionalUtil.calculaInteresAdicional(tasa.valor, plazoAdi));
                            double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                            pagoIndividual +=pagoIndivudualAdi;
                            montoSinComision[i] += integrantesFinales[i].montoAdicional;
                            interes+=interesAdi;
                            interesesAdicionales[i]=interesAdi;
                            montoTotalInteresesAdicionales += interesAdi;
                            myLogger.info("Interes adicional: "+interesAdi);
                        }else{
                            interesesAdicionales[i]=0;
                        }
                        tempMontoDesembolsado += montoSinComision[i];
                        tempInteres += interes;
                        tempPagoIndividual += pagoIndividual;
                        double interesColumna = interes;
                        double pagTotalColumna = montoSinComision[i] + comisionSeguroRefinaciado[i] + interes;
                        
                        interesesIndividuales[i]=interes;
                        myLogger.info("interes total individual antes: "+interesColumna);
                        pagTotalTotal += pagTotalColumna;
                }
                myLogger.info("Total interes sin ajuste: "+tempInteres);
                myLogger.info("Monto total sin comision desembolsado: "+tempMontoDesembolsado);
                
                //Restamos al total de amortizacion el monto neto prestado para obetner unicamente
                //los interese de las amortizaciones correcto
                double interesTotalCorrecto = montoTotalAmortizaciones - tempMontoDesembolsado;
                myLogger.info("Interes total correcto: "+interesTotalCorrecto);
                
                //Metodo que realiza el ajuste al monto para que quede igual al de la amortizacion
                docHelper.realizarAjusteRedondeoAnterior(integrantesFinales, interesesIndividuales, interesesAdicionales, montoTotalInteresesAdicionales, interesTotalCorrecto, tempInteres);
                
                for (int i = 0; i < integrantesFinales.length; i++) {
                    if (integrantesFinales[i].tipo != 0) {
                        String domicilio = ClientesUtil.getDomicilio(integrantesFinales[i].idCliente);
                        ClienteVO cliente = new ClienteDAO().getCliente(integrantesFinales[i].idCliente);
                        TelefonoVO[] telefonos = new TelefonoDAO().getTelefonos(integrantesFinales[i].idCliente, 1);
                        String telefono = "";
                        for (int a = 0; telefonos != null && a < telefonos.length; a++) {
                            telefono += telefonos[a].numeroTelefono;
                            int temp = a + 1;
                            if (temp < telefonos.length) {
                                telefono += " : ";
                            }
                        }
                        
                        int numeroCiclosParticipados = new IntegranteCicloDAO().numCiclosParticipados(integrantesFinales[i]);
                        cont++;
                        reciboForma.setField("NoRow" + cont, cont + "");
                        reciboForma.setField("PuestoRow" + cont, (integrantesFinales[i].rol == 1 ? "TESORERO" : integrantesFinales[i].rol == 2 ? "SECRETARIO" : integrantesFinales[i].rol == 3 ? "PRESIDENTE" : ""));
                        reciboForma.setField("No ClienteRow" + cont, HTMLHelper.displayField(integrantesFinales[i].idCliente));
                        integrantesFinales[i].nombre = integrantesFinales[i].nombre.replace("Ñ", "N");
                        reciboForma.setField("Nombre del miembro del grupoRow" + cont, integrantesFinales[i].nombre);
                        reciboForma.setField("DirecciónRow" + cont, domicilio.toUpperCase());
                        reciboForma.setField("TeléfonoRow" + cont, telefono);
                        reciboForma.setField("Ciclo Row" + cont, HTMLHelper.displayField(numeroCiclosParticipados));
                        reciboForma.setField("SexoRow" + cont, (cliente.sexo == 1 ? "F" : cliente.sexo == 2 ? "M" : "N/D"));
                        reciboForma.setField("Monto DESEMBOLSADORow" + cont, HTMLHelper.formatoMonto(montoSinComisionPDF[i]));
                        reciboForma.setField("AccesoriosRow" + cont, HTMLHelper.formatoMonto(comisionSeguroRefinaciado[i]));
                        reciboForma.setField("InterésRow" + cont, HTMLHelper.formatoMonto(interesesIndividuales[i]));
                        reciboForma.setField("Pago TotalRow" + cont, HTMLHelper.formatoMonto(montoSinComision[i]+interesesIndividuales[i]));
                        reciboForma.setField("Pago SemanalRow" + cont, HTMLHelper.formatoMonto(pagSemanalColumna[i]));
                    }
                }

                //BLoque de totales 
                reciboForma.setField("Monto DESEMBOLSADOTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempMontoDesembolsado)));
                reciboForma.setField("AccesoriosTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempComSeg)));
                reciboForma.setField("InterésTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(interesTotalCorrecto)));
                reciboForma.setField("Pago TotalTOTAL", HTMLHelper.formatoMonto(montoTotalAmortizaciones));
                reciboForma.setField("Pago SemanalTOTAL", HTMLHelper.formatoMonto(tempPagoIndividual));

                //Ultimo campo de interes moratorio calculado
                //reciboForma.setField("interesMora" 						, "$ " + HTMLHelper.formatoMonto( ( (tempMontoDesembolsado+tempComSeg+tempInteres)/plazo )*0.003928 ) );
            } else {
                /**
                 * Variables Pimer Bolque informaciongenral del grupo
                 */
                reciboForma.setField("nombreGrupo", grupo.nombre);
                reciboForma.setField("asesor", codigo.toString());
                reciboForma.setField("ciclo", String.valueOf(ciclo.idCiclo));
                reciboForma.setField("comunidad", ciclo.direccionReunion.colonia);
                reciboForma.setField("fechaDesembolso", HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago));
                reciboForma.setField("fechaUltimoPago", HTMLHelper.displayField(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
                reciboForma.setField("tasa", tasa.descripcion);
                reciboForma.setField("comision", tasaComisionPrint + " %");
                reciboForma.setField("frecuenciaJuntas", "SEMANAL");
                reciboForma.setField("noSemanas", String.valueOf(plazo));

                double montoTotalAmortizaciones = 0;
                double montoAmortSemana15 = 0;
                
                //Segundo bloque escribe fechas de pago 
                for (int i = 0; tablasAmor != null && i < tablasAmor.length; i++) {
                    reciboForma.setField("Semana " + i, HTMLHelper.displayField(tablasAmor[i].fechaPago));
                    myLogger.info("Monto pagado: "+tablasAmor[i].totalPagado +" monto pagar: "+ tablasAmor[i].montoPagar);
                    montoTotalAmortizaciones += tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    if(i == (tablasAmor.length-2)){
                        montoAmortSemana15 = tablasAmor[i].totalPagado + tablasAmor[i].montoPagar;
                    }
                }
                myLogger.info("Monto amortizacion semana 15: "+montoAmortSemana15);
                myLogger.info("Monto Total amortizaciones: "+montoTotalAmortizaciones);
                
                //Tercer Bloque Uso Oficial  de OM
                reciboForma.setField("No_Grupo", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No_Cuenta", ciclo.referencia);
                reciboForma.setField("No_Cliente", HTMLHelper.displayField(grupo.idGrupo));
                reciboForma.setField("No_Contrato", "No Disponible");

                //Bloque informacion de pagos
                reciboForma.setField("NumConvenioBancomer", ClientesConstants.CUENTA_BANCOMER);
                reciboForma.setField("NumCuentaBansefi", ClientesConstants.CUENTA_BANSEFI);
                reciboForma.setField("NumCuentaBanorte", ClientesConstants.CUENTA_BANORTE);
                reciboForma.setField("NumCuentaBajio", ClientesConstants.CUENTA_BAJIO);
                reciboForma.setField("NumCuentaAfirme", ClientesConstants.CUENTA_BANCO_AFIRME);
                reciboForma.setField("NumReferencia", ciclo.referencia);

                //Bloque integrantes
                int cont = 0;
                double tempMontoDesembolsado = 0.0;
                double tempComSeg = 0.0;
                double tempInteres = 0.0;
                double tempPagoIndividual = 0.0;
                
                double [] interesesIndividuales = new double[integrantesFinales.length];
                double [] pagSemanalColumna = obtenerPagosIndividualesAnterior(integrantesFinales, montoAmortSemana15, tasa, catComisiones, ciclo.getPlazo());
                double [] montoSinComision = new double[integrantesFinales.length];
                double [] montoSinComisionPDF = new double[integrantesFinales.length];
                double [] comisionSeguroRefinaciado = new double[integrantesFinales.length];
                double [] interesesAdicionales = new double[integrantesFinales.length];
                
                double montoTotalInteresesAdicionales = 0.0;//codigo para adicional
                
                //Obtenemos primero los montos individucales y el monto total sin redondeos
                for(int i = 0; i < integrantesFinales.length; i++){
                        
                        montoSinComisionPDF[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        montoSinComision[i] = montoSinComisionPDF[i];
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            montoSinComision[i] = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                        }
                    
                        montoSinComision[i] = FormatUtil.redondeaMoneda(montoSinComision[i]);
						
                        comisionSeguroRefinaciado[i] = (integrantesFinales[i].monto - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        
                        if (integrantesFinales[i].montoConSeguro > 0) {
                            comisionSeguroRefinaciado[i] = (integrantesFinales[i].montoConSeguro - integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision[i];
                        }
                        
                        comisionSeguroRefinaciado[i] = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado[i]);
                        tempComSeg += comisionSeguroRefinaciado[i];
                        plazo = plazoGrupal;
                        if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_2;
                        } else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                            plazo = plazoGrupal - ClientesConstants.DISPERSION_SEMANA_4;
                        }

                        double interes = ((((tasa.valor * 1.16)) / 100) / 360) * (plazo * 7);
                        interes = (montoSinComision[i] + comisionSeguroRefinaciado[i]) * interes;
                        interes = FormatUtil.redondeaMoneda(interes);
                        
                        double pagoIndividual = (montoSinComision[i] + comisionSeguroRefinaciado[i] + interes) / (plazo);
                        pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                        
                        if(integrantesFinales[i].tipo_adicional!=0){
                            int plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
							myLogger.info("monto adicional: "+integrantesFinales[i].montoAdicional);
                            double interesAdi = integrantesFinales[i].montoAdicional * (AdicionalUtil.calculaInteresAdicional(tasa.valor, plazoAdi));
                            double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                            pagoIndividual +=pagoIndivudualAdi;
                            montoSinComision[i] += integrantesFinales[i].montoAdicional;
                            interes+=interesAdi;
                            interesesAdicionales[i]=interesAdi;
                            montoTotalInteresesAdicionales += interesAdi;
                            myLogger.info("Interes adicional: "+interesAdi);
                        }else{
                            interesesAdicionales[i]=0;
                        }
                        tempMontoDesembolsado += montoSinComision[i];
                        tempInteres += interes;
                        tempPagoIndividual += pagoIndividual;
                        
                        double interesColumna = interes;
                        double pagTotalColumna = pagoIndividual * plazo;
                        
                        interesesIndividuales[i]=interesColumna;
                        myLogger.info("interes total individual antes: "+interesColumna);
                }      
                
                myLogger.info("Total interes sin ajuste: "+tempInteres);
                myLogger.info("Monto total sin comision desembolsado: "+tempMontoDesembolsado);
                
                //Restamos al total de amortizacion el monto neto prestado para obetner unicamente
                //los interese de las amortizaciones correcto
                double interesTotalCorrecto = montoTotalAmortizaciones - tempMontoDesembolsado;
                myLogger.info("Interes total correcto: "+interesTotalCorrecto);
                
                docHelper.realizarAjusteRedondeoAnterior(integrantesFinales, interesesIndividuales, interesesAdicionales, montoTotalInteresesAdicionales, interesTotalCorrecto, tempInteres);
                
                for (int i = 0; i < integrantesFinales.length; i++) {
                    String domicilio = ClientesUtil.getDomicilio(integrantesFinales[i].idCliente);
                    ClienteVO cliente = new ClienteDAO().getCliente(integrantesFinales[i].idCliente);
                    TelefonoVO[] telefonos = new TelefonoDAO().getTelefonos(integrantesFinales[i].idCliente, 1);
                    String telefono = "";
                    for (int a = 0; telefonos != null && a < telefonos.length; a++) {
                        telefono += telefonos[a].numeroTelefono;
                        int temp = a + 1;
                        if (temp < telefonos.length) {
                            telefono += " : ";
                        }
                    }
                    
                    int numeroCiclosParticipados = new IntegranteCicloDAO().numCiclosParticipados(integrantesFinales[i]);

                    cont++;
                    reciboForma.setField("NoRow" + cont, cont + "");
                    reciboForma.setField("PuestoRow" + cont, (integrantesFinales[i].rol == 1 ? "TESORERO" : integrantesFinales[i].rol == 2 ? "SECRETARIO" : integrantesFinales[i].rol == 3 ? "PRESIDENTE" : ""));
                    reciboForma.setField("No_ClienteRow" + cont, HTMLHelper.displayField(integrantesFinales[i].idCliente));
                    reciboForma.setField("Nombre_del_miembro_del_grupoRow" + cont, integrantesFinales[i].nombre);
                    reciboForma.setField("DirecciónRow" + cont, domicilio.toUpperCase());
                    reciboForma.setField("TeléfonoRow" + cont, telefono);
                    reciboForma.setField("Ciclo_Row" + cont, HTMLHelper.displayField(numeroCiclosParticipados));
                    reciboForma.setField("SexoRow" + cont, (cliente.sexo == 1 ? "F" : cliente.sexo == 2 ? "M" : "N/D"));
                    reciboForma.setField("Monto_DESEMBOLSADORow" + cont, HTMLHelper.formatoMonto(montoSinComisionPDF[i]));
                    reciboForma.setField("AccesoriosRow" + cont, HTMLHelper.formatoMonto(comisionSeguroRefinaciado[i]));
                    reciboForma.setField("InterésRow" + cont, HTMLHelper.formatoMonto(Math.ceil(interesesIndividuales[i])));
                    reciboForma.setField("Pago_TotalRow" + cont, HTMLHelper.formatoMonto(Math.ceil(montoSinComision[i] + comisionSeguroRefinaciado[i] + interesesIndividuales[i])));
                    reciboForma.setField("Pago_SemanalRow" + cont, HTMLHelper.formatoMonto(Math.ceil(pagSemanalColumna[i])));
                }

                reciboForma.setField("Monto_DESEMBOLSADOTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempMontoDesembolsado)));
                reciboForma.setField("AccesoriosTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(tempComSeg)));
                reciboForma.setField("InterésTOTAL", HTMLHelper.formatoMonto(FormatUtil.redondeaMoneda(interesTotalCorrecto)));
                reciboForma.setField("Pago_TotalTOTAL", HTMLHelper.formatoMonto(montoTotalAmortizaciones));
                reciboForma.setField("Pago_SemanalTOTAL", HTMLHelper.formatoMonto(tempPagoIndividual));
                
                //Ultimo campo de interes moratorio calculado
                //reciboForma.setField("interesMora" 						, "$ " + HTMLHelper.formatoMonto( ( (tempMontoDesembolsado+tempComSeg+tempInteres)/plazo )*0.003928 ) );
            }

            reciboPDF.setFormFlattening(true);
            reciboPDF.close();

        } catch (Exception e) {
            myLogger.error("generaHojaResumen", e);
            throw new CommandException(e.getMessage());
            //myLogger.debug ("I/O Exception: " + e);
        }
    }
    
    public double[] obtenerPagosIndividualesAnterior(IntegranteCicloVO [] integrantesFinales, double montoAmortizacion, TasaInteresVO tasa, TreeMap catComisiones, int plazoCiclo) throws ClientesException{
        GeneraDocumentosHelper docHelper =  new GeneraDocumentosHelper();
        double montoTotalSinAjuste = 0;
        int plazo = 0;
        
        myLogger.info("Monto Amortizacion: "+montoAmortizacion);
        //Obtener Monto Total exacto y el monto individual de cada integrante
        //para que en la generacion del pdf se calcule monto individual en base al monto total de la amortizacion
        double [] pagosIndividuales = new double [integrantesFinales.length];
        double [] pagosAdicionales = new double [integrantesFinales.length];
        double montoTotalPagosAdicionales = 0;
        for (int i = 0; i < integrantesFinales.length; i++) {
            if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)
                plazo = 14;//plazo para interciclo                            
            else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2)
                plazo = 12;//plazo para interciclo   
            else
                plazo = plazoCiclo;
            double montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
            
			if (integrantesFinales[i].montoConSeguro > 0) {
                montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
            }
            
            montoSinComision = FormatUtil.redondeaMoneda(montoSinComision);
            double comisionSeguroRefinaciado = (integrantesFinales[i].monto-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
            
            if (integrantesFinales[i].montoConSeguro > 0) {
                comisionSeguroRefinaciado = (integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
            }
			
			comisionSeguroRefinaciado = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado);
            double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
            interes = (montoSinComision + comisionSeguroRefinaciado) * interes;
            interes = FormatUtil.redondeaMoneda(interes);
            double pagoIndividual = (montoSinComision + comisionSeguroRefinaciado + interes) / (plazo);

            if(integrantesFinales[i].tipo_adicional!=0){
                int plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
                double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                pagoIndividual +=pagoIndivudualAdi;
                pagosAdicionales[i]=pagoIndivudualAdi;
                montoTotalPagosAdicionales += pagoIndivudualAdi;
                myLogger.info("Pago adicional: "+pagoIndivudualAdi);
            }else{
                pagosAdicionales[i]=0;
            }

            montoTotalSinAjuste += pagoIndividual;
//                montoTotal = Math.round(montoTotal);
//                pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
            pagosIndividuales[i]=pagoIndividual;
            myLogger.debug("Pago inividual antes: "+pagosIndividuales[i]);
        }

        myLogger.info("Monto Total SIN AJUSTE: "+montoTotalSinAjuste);

        myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
        docHelper.realizarAjusteRedondeoAnterior(integrantesFinales, pagosIndividuales, pagosAdicionales, montoTotalPagosAdicionales, montoAmortizacion, montoTotalSinAjuste);
        myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");

        //Redondeamos los pagos individuales para saber cual es el monto de los pagos
        //individuales redondeados
//            double montoTotalRedondeado = 0;
//            for (int i = 0; i < pagosIndividuales.length; i++) {
//                
//                //Regla de 3 para obtener monto correspondiente al monto de la amortizacion
//                double pagoIndividualCorrespondiente = montoAmortizacion * pagosIndividuales[i];
//                pagoIndividualCorrespondiente = pagoIndividualCorrespondiente/montoTotalSinAjuste; 
//                
//                double pagoRedondeado = Math.round(pagoIndividualCorrespondiente);
//   
//                pagosIndividuales[i]=pagoRedondeado;
//                montoTotalRedondeado += pagosIndividuales[i];
//                myLogger.debug("Pago inividual despues: "+pagosIndividuales[i]);
//            }
//            myLogger.info("Monto Total REDONDEADO: "+montoTotalRedondeado);
//            
//            double diferencia = montoAmortizacion - montoTotalRedondeado;
//            myLogger.info("Diferencia Montos: "+diferencia);
//            
//            int sum = 0;
//            //Distribuimos la diferencia entre los integrantes
//            if(diferencia>0){
//                myLogger.info("Dif positiva");
//                sum=1;
//            }else if(diferencia<0){
//                myLogger.info("Dif negativa");
//                diferencia=diferencia*(-1);
//                sum=-1;
//            }
//            myLogger.info("Dif final: "+diferencia);
//            for(int i = 0; i<diferencia; i++){
//                    pagosIndividuales[i]=pagosIndividuales[i]+sum;
//                    myLogger.info("Pago individual ajustado: "+pagosIndividuales[i]);
//            }
        return pagosIndividuales;
    }

    
}

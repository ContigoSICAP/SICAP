package com.sicap.clientes.helpers;

import com.lowagie.text.Chunk;
import java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConvertNumberToString;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import java.awt.Color;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sicap.clientes.vo.IntegranteCicloVO;

public class GeneraPagareHelper {

    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(GeneraPagareHelper.class);
    
    Document document;
    Font titleFont;
    Font headFont;
    Font tableFont;
    Font labelFont;
    Font smallFont;
    Font numberFont;
    int letra;
    Table dataTable;
    String fechaHoy;

    public void doPagarePDF(HttpServletRequest request, HttpServletResponse response, ClienteVO cliente, SolicitudVO solicitud) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Arial");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoDocPagare(cliente, solicitud);
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main doPagarePDF");
            exception.printStackTrace();
        }
    }

    public void doPagareGrupalPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDisp) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoDocPagareGrupal(grupo, ciclo, semDisp);
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main doPagarePDF");
            exception.printStackTrace();
        }
    }

    public void doPagareIndividualPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDisp) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoDocPagareIndividual(grupo, ciclo, semDisp);
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main doPagareIndividualPDF");
            exception.printStackTrace();
        }
    }

    private void contenidoDocPagare(ClienteVO cliente, SolicitudVO solicitud) {

        try {
            String montoIni = String.valueOf(HTMLHelper.formatoMonto(java.lang.Math.ceil(solicitud.saldo.getMontoConIntereses())));
            //String montoIni = String.valueOf(HTMLHelper.formatoMonto(solicitud.saldo.getMontoCredito()));
            String monto = "";
            int posicion = montoIni.indexOf(".");
            int decimal = 0;
            String tasaLetrasDecimal = "";
            String tasaMoLetrasDecimal = "";
            Integer entero = Integer.parseInt(montoIni.substring(0, posicion));
            ConvertNumberToString montoLetra = new ConvertNumberToString(entero);
            String mtoLetrasEntero = montoLetra.convertirLetras(entero);
            String mtoDecimal = montoIni.substring(posicion + 1);
            Paragraph p = null;
            Paragraph pp = null;
            String texto = "";
            int i = 0;
            String fecha = "";
            String nombreComp = cliente.aPaterno + " " + cliente.aMaterno + " " + cliente.nombre;
            String domCliente = cliente.direcciones[0].calle + " " + cliente.direcciones[0].numeroExterior + " " + cliente.direcciones[0].numeroInterior + " Col. "
                    + cliente.direcciones[0].colonia + " " + cliente.direcciones[0].estado + " C.P." + cliente.direcciones[0].cp;
            String nomObligadoSoli = solicitud.obligadosSolidarios[0].aPaterno + " " + solicitud.obligadosSolidarios[0].aMaterno + " " + solicitud.obligadosSolidarios[0].nombre;
            String dirObligadoSoli = solicitud.obligadosSolidarios[0].direccion.calle + " " + solicitud.obligadosSolidarios[0].direccion.numeroExterior + " "
                    + solicitud.obligadosSolidarios[0].direccion.numeroInterior + " Col. " + solicitud.obligadosSolidarios[0].direccion.colonia + " "
                    + solicitud.obligadosSolidarios[0].direccion.estado + " C.P. " + solicitud.obligadosSolidarios[0].direccion.cp;
            TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(solicitud.decisionComite.tasa);
            montoIni = String.valueOf(HTMLHelper.formatoMonto(tasa.valor));
            posicion = montoIni.indexOf(".");
            entero = Integer.parseInt(montoIni.substring(0, posicion));
            String tasaLetrasEntero = montoLetra.convertirLetras(entero);
            monto = montoIni.substring(0, posicion);
            posicion = monto.length() + 1;
            decimal = Integer.parseInt(montoIni.substring(posicion));
            if (decimal != 0) {
                tasaLetrasDecimal = montoLetra.convertirLetras(decimal);
            } else {
                tasaLetrasDecimal = "cero";
            }
            montoIni = String.valueOf(HTMLHelper.formatoMonto(tasa.valor * 2));
            posicion = montoIni.indexOf(".");
            entero = Integer.parseInt(montoIni.substring(0, posicion));
            String tasaMoLetrasEntero = montoLetra.convertirLetras(entero);
            monto = montoIni.substring(0, posicion);
            posicion = monto.length() + 1;
            decimal = Integer.parseInt(montoIni.substring(posicion));
            if (decimal != 0) {
                tasaMoLetrasDecimal = montoLetra.convertirLetras(decimal);
            } else {
                tasaMoLetrasDecimal = "cero";
            }
            CatalogoDAO paramDao = new CatalogoDAO();
            ParametroVO paramVO = new ParametroVO();
            paramVO = paramDao.getParametro("RUTA_IMAGEN_LOGO_CONTIGO");
            Image logo = Image.getInstance(paramVO.valor);
            logo.scaleToFit(100, 150);
            logo.setAlignment(Chunk.ALIGN_RIGHT);
            document.add(logo);
            texto = "PAGARÉ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "\n\n\nPor este pagaré me obligo incondicionalmente a pagar a la orden de SERVICIOS FINANCIEROS CONTIGO, S.A.P.I.   DE C.V., SOFOM, E.N.R., la cantidad de $" + FormatUtil.formatDobleMiles(java.lang.Math.ceil(solicitud.saldo.getMontoConIntereses())) + " ("
                    + (mtoLetrasEntero + " pesos " + mtoDecimal + "/100 M.N.").toUpperCase() + "), valor recibido  a mi entera satisfacción.";
            texto += "\n\nLa cantidad antes señalada será pagada en " + solicitud.saldo.getPlazo() + " pagos sucesivos, en los vencimientos y por los montos establecidos en la tabla siguiente:";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            texto = "\n";
            p = new Paragraph(texto);
            document.add(p);
            int col[] = {20, 40, 40};
            PdfPTable tabla = new PdfPTable(3);
            PdfPCell cell = null;
            tabla.setWidthPercentage(80F);
            tabla.setWidths(col);

            texto = "Exhibición";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setGrayFill(0.80f);
            tabla.addCell(cell);
            texto = "Abono";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setGrayFill(0.80f);
            tabla.addCell(cell);
            texto = "Fecha de Pago";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setGrayFill(0.80f);
            tabla.addCell(cell);
            for (i = 1; i < solicitud.amortizacion.length; i++) {
                texto = "" + solicitud.amortizacion[i].numPago;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setGrayFill(0.97f);
                tabla.addCell(cell);
                texto = "$" + FormatUtil.formatDobleMiles(solicitud.amortizacion[i].montoPagar);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setGrayFill(0.97f);
                tabla.addCell(cell);
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                fecha = sdf.format(solicitud.amortizacion[i].fechaPago);
                texto = fecha;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setGrayFill(0.97f);
                tabla.addCell(cell);
            }
            document.add(tabla);
            texto = "\n\nEl presente pagaré generará un Interés Ordinario anual a razón de una tasa del " + tasa.valor + "% (" + tasaLetrasEntero + " punto " + tasaLetrasDecimal + " por ciento).";
            texto += "\n\nEl presente pagaré generará Intereses Moratorios a razón de una tasa de " + (tasa.valor * 2) + "% (" + tasaMoLetrasEntero + " punto " + tasaMoLetrasDecimal + " por ciento), calculado diariamente sobre el saldo adeudado, aplicable desde el día siguiente en que incurrió en mora y hasta que efectúe el pago del adeudo.";
            texto += "\n\nLa falta de pago oportuno de cualquier amortización en la fecha en que debiera pagarse, dará por vencidas anticipadamente todas las que le sigan en número y falten de cubrirse, por lo que se entenderán pagaderas a la vista.";
            texto += "\n\nLugar y fecha de suscripción " + solicitud.saldo.getNombreSucursal() + " a " + FechasUtil.obtenParteFecha(solicitud.saldo.getFechaDesembolso(), 1) + " de " + FechasUtil.obtenNombreMes(Convertidor.dateToString(solicitud.saldo.getFechaDesembolso())) + " de " + FechasUtil.obtenParteFecha(solicitud.saldo.getFechaDesembolso(), 3) + ".";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            texto = "\n\n\nEL SUSCRIPTOR\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "     " + nombreComp + "     ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 15, Font.UNDERLINE));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "NOMBRE Y FIRMA DE \"EL CLIENTE\"";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "\nDOMICILIO:  ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            texto = domCliente;
            pp = new Paragraph(texto, new Font(Font.NORMAL, 15, Font.UNDERLINE));
            p.add(pp);
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "\n\nEL AVAL";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "     " + nomObligadoSoli + "     ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 15, Font.UNDERLINE));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "NOMBRE Y FIRMA DE \"OBLIGADO SOLIDARIO\"";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "\nDOMICILIO:  ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.BOLD));
            texto = domCliente;
            pp = new Paragraph(texto, new Font(Font.NORMAL, 15, Font.UNDERLINE));
            p.add(pp);
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "\n\nSERVICIOS FINANCIEROS CONTIGO, S.A.P.I. DE C.V., SOFOM, E.N.R.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 14, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);

        } catch (Exception e) {
            Logger.debug("Excepcion en contenido de Pagare PDF");
            e.printStackTrace();
        }
    }

    private void contenidoDocPagareGrupal(GrupoVO grupo, CicloGrupalVO ciclo, int semDisp) throws ClientesException {

        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
        TasaInteresVO tasaVO = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
        double montoConIntereses = 0.00;
        TablaAmortizacionVO tablaIC[] = null;
        Date fechaDate = null;
        TablaAmortizacionDAO tablaDao = new TablaAmortizacionDAO();
        for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
            montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
        }
        double tasa = Double.parseDouble(tasaVO.descripcion.replace("%", ""));
        //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
        String montoIni = String.valueOf(montoConIntereses);
        int posicion = montoIni.indexOf(".");
        Integer entero = Integer.parseInt(montoIni.substring(0, posicion));
        ConvertNumberToString montoLetra = new ConvertNumberToString(entero);
        String mtoLetrasEntero = montoLetra.convertirLetras(entero);
        String mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");
        String fecha = "", mes = "";
        SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
        int cantIntegrates = 0;
        boolean imprime = false;
        int tipoTabla = 0;
        if (semDisp == 0) {
            if (ciclo.estatusIC == 0 && ciclo.estatusIC2 == 0) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if ((ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO)
                    || (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO)) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO_2;
            } else if ((ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 > ClientesConstants.CICLO_DISPERSADO)
                    || (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC == 0)) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            } else {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            }
        } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
            if(ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO&&ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO)
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            else if (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if (ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSO) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            }
        } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
            if (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if (ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            }
        }
        ciclo.setTablaAmortizacion(tablaDao.getElementos(ciclo.idGrupo, ciclo.idCiclo, 0, tipoTabla));
        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            String textoPronafin = "";
            texto = "NÚMERO DE REGISTRO DE CONTRATO DE ADHESIÓN: 13317-439-012789/04-05715-1017.                                                                                       ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE, Color.GRAY));
            p.setAlignment(Chunk.ALIGN_LEFT);
            document.add(p);

            texto = "\nPAGARÉ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph();
            texto = "\nPor el presente pagaré, los suscritos (en adelante y, en conjunto, los “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Deudores";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), prometen pagar incondicional y solidariamente a la orden de CEGE CAPITAL, S.A.P.I. DE C.V., SOFOM, E.N.R. (en adelante, el “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Acreedor";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), las cantidades que se señalan en la relación siguiente (cada una de ellas, un “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Abono";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "” y conjuntamente todas ellas, el “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Importe";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), en cualquier lugar o domicilio en el que se le requiera de pago. El Importe será liquidado por los Deudores mediante el pago de parcialidades por el equivalente a cada Abono en el día señalado como “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Fecha de Pago";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”, según se establece en la relación siguiente. En caso de que la Fecha de Pago de cualquier Abono, no sea día hábil bancario, el Abono de que se trate deberá pagarse el día hábil bancario inmediato anterior.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            int col[] = {20, 50, 30};
            tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100F);
            tabla.setWidths(col);

            texto = "Importe";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            //p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            tabla.addCell(cell);

            texto = "Fecha de Pago";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "Cantidad con número";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "Cantidad con letra";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "";
            p = new Paragraph(texto);
            cell = new PdfPCell(p);
            tabla.addCell(cell);

            texto = "$ " + FormatUtil.formatIntegerMiles(montoConIntereses) + " " + mtoDecimal + "/100 M.N.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            texto = mtoLetrasEntero.substring(0, 1).toUpperCase() + mtoLetrasEntero.substring(1, mtoLetrasEntero.length()) + " pesos " + mtoDecimal + "/100 M.N.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            //fecha = Convertidor.dateToString(ciclo.saldo.getFechaVencimiento());
            fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago);
            mes = FechasUtil.obtenNombreMes(fecha);
            texto = "El día " + ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " del año " + FechasUtil.obtenParteFecha(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago, 3) + ". (el “Vencimiento”).";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            texto = "Abonos";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            tabla.addCell(cell);

            texto = "";
            p = new Paragraph(texto);
            cell = new PdfPCell(p);
            tabla.addCell(cell);

            for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
                montoIni = String.valueOf(ciclo.tablaAmortizacion[i].montoPagar);
                posicion = montoIni.indexOf(".");
                entero = Integer.parseInt(montoIni.substring(0, posicion));
                montoLetra = new ConvertNumberToString(entero);
                mtoLetrasEntero = montoLetra.convertirLetras(entero);
                mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");

                texto = "$ " + FormatUtil.formatIntegerMiles(ciclo.tablaAmortizacion[i].montoPagar) + " 00/100 M.N.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                texto = mtoLetrasEntero.substring(0, 1).toUpperCase() + mtoLetrasEntero.substring(1, mtoLetrasEntero.length()) + " pesos " + mtoDecimal + "/100 M.N.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[i].fechaPago);
                mes = FechasUtil.obtenNombreMes(fecha);
                texto = "El día " + ciclo.tablaAmortizacion[i].fechaPago.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " del año " + FechasUtil.obtenParteFecha(ciclo.tablaAmortizacion[i].fechaPago, 3) + "";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);
            }
            document.add(tabla);

            texto = "\nEn caso que cualquiera de los Abonos, sea pagado en forma incompleta y/o extemporánea por los Deudores, se incrementará al importe del Abono de que se trate, el importe equivalente a $ 100 pesos (mismo que incluirá el Impuesto al Valor Agregado), por pago tardío.";
            texto += "\n\nEn caso que los Deudores no paguen en forma completa y/u oportuna cualquier cantidad que deba cubrirse derivada del presente pagaré, dicho monto insoluto causará intereses moratorios sobre la tasa mensual del " + tasa * 2 + " % (mismo que incluirá el Impuesto al Valor Agregado).";
            texto += "\n\nLos Deudores, en forma expresa, desde ahora: (i) autorizan al Acreedor para que endose y/o transmita y/o descuente y/o transfiera y/o ceda y/o negocie y/o afecte y/o grave este pagaré y los derechos de crédito que ampara, en cualquier tiempo y lugar, ";
            texto += "sirviendo el presente como la más amplia y necesaria autorización de los Deudores al respecto; (ii) amplían el plazo de presentación de este pagaré, para que su pago pueda efectuarse dentro del plazo de un año posterior a la fecha del último pago parcial previsto; ";
            texto += "y, (iii) autorizan al Acreedor para que, en caso de que los Deudores omitan liquidar cualesquiera de las parcialidades pactadas, en la fecha de pago de que se trate, pueda dar válidamente por vencido anticipadamente el plazo establecido en el presente pagaré y, por lo tanto, ";
            texto += "los Deudores estarán obligados a pagar al Acreedor, el saldo insoluto pendiente de pago y en su caso los saldos insolutos de los gastos de cobranza e intereses moratorios que sean causados.";
            texto += "\n\nCualquier pago efectuado por los Deudores, será aplicado por el Acreedor, para cubrir, en primer lugar y hasta donde alcance, el saldo insoluto de los gastos de cobranza de que se trate, en caso de que exista, ";
            texto += "seguidamente y hasta donde alcance, el saldo insoluto de los intereses moratorios de que se trate, en caso de que exista y, por último y hasta donde alcance, el saldo insoluto del Importe.";
            texto += "\n\nPara toda controversia o litigio de cualquier tipo en relación con el presente pagaré, los Deudores están de acuerdo en someterse expresamente a las leyes federales de los Estados Unidos Mexicanos y a la jurisdicción y competencia ";
            texto += "de los tribunales del domicilio del acreedor o de los deudores, a elección del Acreedor, por lo tanto, los Deudores renuncian al fuero territorial que por cualquier causa pudiere corresponderles, ya sea en lo presente o en lo futuro.\n";

            //fecha = Convertidor.dateToString(ciclo.saldo.getFechaDesembolso());
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                if (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO || ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSO) {
                    fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[2].fechaPago);
                    fechaDate = ciclo.tablaAmortizacion[2].fechaPago;
                }
            }
            else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                if (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO || ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO) {
                    fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[4].fechaPago);
                    fechaDate = ciclo.tablaAmortizacion[4].fechaPago;
                }
            } else {
                fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[0].fechaPago);
                fechaDate = ciclo.tablaAmortizacion[0].fechaPago;
            }

            mes = FechasUtil.obtenNombreMes(fecha);

            texto += "\n" + sucursal.estado + ", " + sucursal.municipio + ", a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n";

            texto += "\nLos Deudores\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            document.add(p);

            int colF[] = {33, 33, 33};
            tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100F);
            tabla.setWidths(colF);

            for (int i = 0; i < ciclo.integrantes.length; i++) {
                imprime = false;
                if (semDisp == 0 && ciclo.integrantes[i].tipo <= ClientesConstants.TIPO_CLIENTE_RECUPERADO) {
                    imprime = true;
                } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_2 && (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO||ciclo.integrantes[i].tipo ==0)) {
                    imprime = true;
                } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2||ciclo.integrantes[i].tipo ==0)) {
                    imprime = true;
                }
                if (imprime) {
                    cantIntegrates++;
                    texto = "\n\n\n\n\n\n\n\n\n\n_________________________________\n" + ciclo.integrantes[i].nombre;
                    System.out.println("texto " + texto);
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                    p.setAlignment(Chunk.ALIGN_LEFT);
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(cell);           
                    
                }
            }
            int firmaComplemento = 4;
            if (semDisp>0) {                
                for (int i = 0; i < firmaComplemento; i++) {
                    texto = "\n\n\n\n\n\n\n\n\n     _________________________________\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                    p.setAlignment(Chunk.ALIGN_LEFT);
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(cell);
                }
                int Compceldas = 3- ((cantIntegrates + firmaComplemento)%3);
                if (Compceldas < 3) {
                    for (int i = 0; i < Compceldas; i++) {
                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                        p.setAlignment(Chunk.ALIGN_LEFT);
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setBorderWidthBottom(0);
                        cell.setBorderWidthRight(0);
                        cell.setBorderWidthTop(1);
                        tabla.addCell(cell);
                    }
                }
            }else{
                firmaComplemento = 3 - (cantIntegrates % 3);
                if (firmaComplemento < 3) {
                    for (int i = 0; i < firmaComplemento; i++) {
                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                        p.setAlignment(Chunk.ALIGN_LEFT);
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tabla.addCell(cell);
                    }
                }
            }
            document.add(tabla);
            //LLamar fondeador de ciclos grupales una vez terminado el desarrollo de Alex 
            if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR || ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS) {
                textoPronafin = "\n\nEndoso el presente pagaré en garantía a Nacional Financiera, S.N.C., I.B.D.; fiduciaria en el Fideicomiso del Fondo de Microfinanciamiento a Mujeres Rurales, (FOMMUR).\n"
                        + "\nCEGE CAPITAL, S.A.P.I. de C.V., SOFOM, E.N.R.\n"
                        + "Representada por: ___________________________________________________\n"
                        + "Mediante Carta Poder de Fecha ______ de __________________ de _______\n"
                        + ciclo.direccionReunion.estado + ", " + ciclo.direccionReunion.municipio
                        + " a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n"
                        + "\nFirma:__________________________________";
                p = new Paragraph(textoPronafin, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                document.add(p);

            }    
            if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM || ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM_P2) {
                textoPronafin = "\n\nEndoso el presente pagaré en garantía a Nacional Financiera, S.N.C., I.B.D.; fiduciaria del Fideicomiso del Programa Nacional de Financiamiento al Microempresario.\n"
                        + "Representada por: ___________________________________________________\n"
                        + "Mediante Carta Poder de Fecha ______ de __________________ de _______\n"
                        + ciclo.direccionReunion.estado + ", " + ciclo.direccionReunion.municipio
                        + " a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n"
                        + "\nFirma:__________________________________";
                p = new Paragraph(textoPronafin, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                document.add(p);

            }
            

        } catch (Exception e) {
            Logger.debug("Excepcion en contenido de Pagare PDF");
            e.printStackTrace();
        }
    }

    private void contenidoDocPagareIndividual(GrupoVO grupo, CicloGrupalVO ciclo, int semDisp) throws ClientesException {

        double montoConIntereses = 0.00;
        for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
            montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
        }
        //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
        String montoIni = String.valueOf(montoConIntereses);
        int posicion = montoIni.indexOf(".");
        ConvertNumberToString numeroEntero;
        String fecha = "", mes = "";
        SucursalDAO sucDAO = new SucursalDAO();
        SucursalVO sucursales = sucDAO.getSucursal(grupo.sucursal);
//        SucursalVO sucursal = new SucursalDAO().getSucursal(ciclo.saldo.getIdSucursal());
        String domGrupo = sucursales.direccion_calle + " # " + sucursales.numero + " Col. " + sucursales.colonia + " C.P. " + sucursales.cp;
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        Double tasaInteres = null;
        int decimal = 0;
        String mtoLetrasEntero = "";
        String mtoDecimal = "";
        boolean imprime = false;

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            TasaInteresVO tasa = null;
            int plazo = ciclo.tablaAmortizacion.length - 1;
            String fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago);
            try {
                //Para obtener comisi�n
                TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
                if (catTasas != null) {
                    tasa = catTasas.get(ciclo.tasa);
                    tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                //JECB 26/10/2017
                //Variable para la obtencion dle seguro contratado por el cliente
                double costoSeguroCliente = SegurosUtil.costoSeguroXIntegranteCiclo(ciclo.integrantes[i]);
                
                imprime = false;
                if (semDisp == 0 && ciclo.integrantes[i].tipo <= 5) {
                    imprime = true;
                    plazo = ciclo.tablaAmortizacion.length - 1;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago);
                } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_2 && ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_2;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_2].fechaPago);
                } else if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_4;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_4].fechaPago);
                }
                if (imprime) {
                    texto = "PAGARÉ\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                    p.setAlignment(Chunk.ALIGN_CENTER);
                    document.add(p);

                    Double montoPagar = 0d;
                    double montoCapital = 0;
                    if (grupo.idOperacion == ClientesConstants.GRUPAL) {
                        Logger.debug("ciclo.integrantes[i].tipo_adicional"+ ciclo.integrantes[i].tipo_adicional );
                        
                        /**
                         * JECB 01/10/2017
                         * genera pagare individual para todos los clientes 
                         * dejando de lado los montos de credito adicional
                        if(ciclo.integrantes[i].tipo_adicional!=0){
                           Logger.debug("encontro Adicional"+ ciclo.integrantes[i].tipo_adicional );
                           //montoCapital = AdicionalUtil.calculaCapitalAdicional(ciclo.integrantes[i]); 
                           montoCapital  = ciclo.integrantes[i].montoAdicional;
                           plazo = plazo-AdicionalUtil.plazoAdicional(ciclo.integrantes[i].tipo_adicional);
                        }else{
                           montoCapital = ciclo.integrantes[i].monto;
                        }
                        * 
                        */
                        //JECB 26/10/2017
                        //la sumatoria debera considerar el consto de seguro de cliente
                        montoCapital = (ciclo.integrantes[i].monto - ciclo.integrantes[i].montoAdicional) + costoSeguroCliente;
                        montoPagar = ( montoCapital + ciclo.integrantes[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal)) * ((FormatUtil.roundDouble(tasa.valor * 1.16, 2)) / 100) / 360;
                        montoPagar = ( montoCapital + ciclo.integrantes[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisionesGrupal)) + plazo * 7 * montoPagar;
                    } else {
                        montoPagar = ciclo.integrantes[i].monto * ((FormatUtil.roundDouble(tasa.valor * 1.16, 2)) / 100) / 360;
                        montoPagar = ciclo.integrantes[i].monto + plazo * 7 * montoPagar;
                    }
                    montoPagar = FormatUtil.redondeaMoneda(montoPagar);
                    montoPagar = Math.ceil(montoPagar);
                    String montoPagarFormat = String.valueOf(HTMLHelper.formatoMonto(montoPagar));
                    decimal = montoPagarFormat.indexOf(".");
                    Integer entero = Integer.parseInt(montoPagarFormat.substring(0, decimal));
                    numeroEntero = new ConvertNumberToString(entero);
                    mtoLetrasEntero = numeroEntero.convertirLetras(entero);
                    mtoDecimal = montoPagarFormat.substring(decimal + 1);

                    texto = "Por este pagaré me obligo a pagar incondicionalmente a la orden de CEGE CAPITAL S.A.P.I. DE C.V. SOFOM, ENR, en su domicilio ubicado en ";
                    texto += domGrupo + " la cantidad principal de $" + montoPagarFormat + " (" + mtoLetrasEntero.toUpperCase() + " pesos " + mtoDecimal + "/100 M.N.), valor recibido a mi entera satisfacción.\n\n";
                    texto += "La cantidad antes señalada será pagada en " + String.valueOf(plazo) + " pagos [iguales] [semanales/] sucesivos, pagaderos sin excepción ni demora y sin necesidad de requerimiento judicial o extrajudicial alguno, comenzando el día " + fechaFirma.substring(0, 2) + " del mes de " + FechasUtil.obtenNombreMes(fechaFirma).toLowerCase() + " del año " + fechaFirma.substring(fechaFirma.lastIndexOf("/") + 1) + ", ";
                    texto += "en el entendido de que en caso de falta de pago de una o varias amortizaciones, el presente pagaré será a la vista sin necesidad de presentación, demanda, protesto o aviso alguno, a todos los cuales el suscriptor renuncia expresamente.\n\n";
                    texto += "El suscriptor se obliga a pagar intereses sobre el monto principal total de este Pagaré a una tasa de interés global anual calculados sobre el monto total del presente Pagaré, ";
                    texto += "y por lo tanto aplicando dicha tasa de interés global anual al monto total del presente Pagaré, sin descontar amortizaciones de capital durante la vigencia del mismo.\n\n";
                    texto += "Los intereses se computarán sobre la base de días naturales efectivamente transcurridos (incluyendo el primer día; pero excluyendo el último día) sobre un año de trescientos sesenta (360) días. Si la fecha de vencimiento de cualquier abono no es un Día Hábil, ";
                    texto += "el pago se hará el primer Día Hábil inmediato posterior, a menos que dicha extensión cause que el pago se realice en el mes calendario inmediato siguiente, en cuyo caso el pago se hará en el Día Hábil inmediato anterior, y en todo caso dicha aplicación del plazo será en dicho caso incluida en el cómputo de intereses.\n\n";
                    texto += "En caso de incumplimiento del pago de cualquier monto de principal o intereses en la fecha de su vencimiento, el total del principal se considerará vencido y pagadero a opción y ";
                    texto += "petición del tenedor del presente pagaré y, a partir de ese incumplimiento, el total del principal insoluto de este pagaré devengará un interés moratorio, a una tasa de interés moratorio anual del " + HTMLHelper.formatoMonto(tasaInteres * 2) + " %.\n\n";
                    texto += "La suma principal de este pagaré y sus intereses se pagarán en moneda nacional, libre de cualquier deducción por cualquier impuesto, derecho, cargo o imposición fiscal ";
                    texto += "presente o futura con respecto a dichos pagos, los cuales en caso de ser aplicables, serán por cuenta del suscriptor y pagados por éste.\n\n";
                    texto += "Para los efectos del artículo 128 de la Ley General de Títulos y Operaciones de Crédito, el suscriptor prorroga irrevocablemente el plazo para la presentación de este pagaré hasta la fecha que ocurra 5 (cinco) años después de la fecha en que deba de hacerse ";
                    texto += "la última amortización arriba señalada, en el entendido de que dicha extensión no impedirá la presentación de este pagaré con anterioridad a dicha fecha. El suscriptor promete incondicionalmente pagar los gastos que impliquen el cobro de este pagaré y los honorarios de los abogados que intervengan en el mismo cobro.\n\n";
                    texto += "El suscriptor se somete expresamente a las leyes federales de los Estados Unidos Mexicanos y a la jurisdicción y competencia de los tribunales del domicilio del acreedor o del suscriptor, ";
                    texto += "a elección del Acreedor, por lo tanto, el suscriptor renuncia al fuero territorial que por cualquier causa pudiere corresponderle, ya sea en lo presente o en lo futuro.\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                    document.add(p);

                    p = new Paragraph();
                    texto = "Lugar y fecha de suscripción:________________________________________a__________de_______________________de 20_____";
                    ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    ck.setBackground(Color.yellow, 1, 1, 1, 1);
                    p.add(ck);
                    p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                    document.add(p);

                    texto = "\n\n\n\n\n________________________________________________________\n" + ciclo.integrantes[i].nombre;
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    p.setAlignment(Chunk.ALIGN_CENTER);
                    p.setLeading(12);
                    document.add(p);

                    document.newPage();
                }
            }

        } catch (Exception e) {
            Logger.debug("Excepcion en contenido de Pagare Individual PDF");
            e.printStackTrace();
        }
    }
    /**
     * JECB 01/10/2017
     * Metodo que construye el pagare individual de adicional en PDF
     * @param request
     * @param response
     * @param grupo
     * @param ciclo
     * @param semDispAdicional 
     */
    public void doPagareIndividualAdicionalPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDispAdicional) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoDocPagareIndividualAdicional(grupo, ciclo, semDispAdicional);
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main doPagareIndividualPDF");
            exception.printStackTrace();
        }
    }
    
    /**
     * JECB 01/10/2017
     * Metodo que contruye el contenido del pagare individual de adicional
     * @param grupo
     * @param ciclo
     * @param semDispAdicional
     * @throws ClientesException 
     */
    private void contenidoDocPagareIndividualAdicional(GrupoVO grupo, CicloGrupalVO ciclo, int semDispAdicional) throws ClientesException {

        double montoConIntereses = 0.00;
        for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
            montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
        }
        //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
        String montoIni = String.valueOf(montoConIntereses);
        int posicion = montoIni.indexOf(".");
        ConvertNumberToString numeroEntero;
        String fecha = "", mes = "";
        SucursalDAO sucDAO = new SucursalDAO();
        SucursalVO sucursales = sucDAO.getSucursal(grupo.sucursal);
//        SucursalVO sucursal = new SucursalDAO().getSucursal(ciclo.saldo.getIdSucursal());
        String domGrupo = sucursales.direccion_calle + " # " + sucursales.numero + " Col. " + sucursales.colonia + " C.P. " + sucursales.cp;
        TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
        Double tasaInteres = null;
        int decimal = 0;
        String mtoLetrasEntero = "";
        String mtoDecimal = "";
        boolean imprime = false;

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            TasaInteresVO tasa = null;
            int plazo = ciclo.tablaAmortizacion.length - 1;
            String fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[0].fechaPago);
            try {
                //Para obtener comisi�n
                TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
                if (catTasas != null) {
                    tasa = catTasas.get(ciclo.tasa);
                    tasaInteres = tasa.valor;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            myLogger.debug("Semana de adicional:" + semDispAdicional);
            int tipoAdicional = AdicionalUtil.obtieneTipoAdicional(semDispAdicional);
            myLogger.debug("tipo de adicional:" + tipoAdicional);
            
            //Obtenemos los integrantes que tiene asignado credito adicional 
            //que coincida con la semana de adicional
            java.util.List<IntegranteCicloVO> listIC = new java.util.ArrayList<IntegranteCicloVO>();
            for(IntegranteCicloVO icTmp : ciclo.integrantes){
                if(icTmp.tipo_adicional == tipoAdicional ){
                    listIC.add((IntegranteCicloVO)icTmp.clone());
                }
            }
            
            IntegranteCicloVO[] arrayIC = new IntegranteCicloVO[listIC.size()];
            listIC.toArray(arrayIC);
            
            /**
             * JECB 01/10/2017
             * Se realizan adecuaciones
             * para que considere las nuevas semanas a manejar 
             * del credito adicional 
             */
            for (int i = 0; i < arrayIC.length; i++) {
                imprime = false;
                if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_2 ) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_2;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_2].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_4) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_4;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_4].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_5) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_5;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_5].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_6) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_6;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_6].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_7) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_7;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_7].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_8) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_8;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_8].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_9) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_9;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_9].fechaPago);
                }else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_10) {
                    imprime = true;
                    plazo = (ciclo.tablaAmortizacion.length - 1) - ClientesConstants.DISPERSION_SEMANA_10;
                    fechaFirma = HTMLHelper.displayField(ciclo.tablaAmortizacion[ClientesConstants.DISPERSION_SEMANA_10].fechaPago);
                }
                
                if (imprime) {
                    texto = "PAGARÉ\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                    p.setAlignment(Chunk.ALIGN_CENTER);
                    document.add(p);

                    Double montoPagar = 0d;
                    double montoCapital = 0;
                    if (grupo.idOperacion == ClientesConstants.GRUPAL) {
                        Logger.debug("arrayIC[i].tipo_adicional"+ arrayIC[i].tipo_adicional );
                        
                        Logger.debug("encontro Adicional"+ arrayIC[i].tipo_adicional );
                        montoCapital  = arrayIC[i].montoAdicional;
                        //plazo = plazo-AdicionalUtil.plazoAdicional(arrayIC[i].tipo_adicional);
                        
                        montoPagar = ( montoCapital + arrayIC[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(arrayIC[i].primaSeguro, arrayIC[i].comision, catComisionesGrupal)) * ((FormatUtil.roundDouble(tasa.valor * 1.16, 2)) / 100) / 360;
                        montoPagar = ( montoCapital + arrayIC[i].montoRefinanciado + ClientesUtil.calculaMontoConComision(arrayIC[i].primaSeguro, arrayIC[i].comision, catComisionesGrupal)) + plazo * 7 * montoPagar;
                    }
                    
                    String montoPagarFormat = String.valueOf(HTMLHelper.formatoMonto(montoPagar));
                    decimal = montoPagarFormat.indexOf(".");
                    Integer entero = Integer.parseInt(montoPagarFormat.substring(0, decimal));
                    numeroEntero = new ConvertNumberToString(entero);
                    mtoLetrasEntero = numeroEntero.convertirLetras(entero);
                    mtoDecimal = montoPagarFormat.substring(decimal + 1);

                    texto = "Por este pagaré me obligo a pagar incondicionalmente a la orden de CEGE CAPITAL S.A.P.I. DE C.V. SOFOM, ENR, en su domicilio ubicado en ";
                    texto += domGrupo + " la cantidad principal de $" + montoPagarFormat + " (" + mtoLetrasEntero.toUpperCase() + " pesos " + mtoDecimal + "/100 M.N.), valor recibido a mi entera satisfacción.\n\n";
                    texto += "La cantidad antes señalada será pagada en " + String.valueOf(plazo) + " pagos [iguales] [semanales/] sucesivos, pagaderos sin excepción ni demora y sin necesidad de requerimiento judicial o extrajudicial alguno, comenzando el día " + fechaFirma.substring(0, 2) + " del mes de " + FechasUtil.obtenNombreMes(fechaFirma).toLowerCase() + " del año " + fechaFirma.substring(fechaFirma.lastIndexOf("/") + 1) + ", ";
                    texto += "en el entendido de que en caso de falta de pago de una o varias amortizaciones, el presente pagaré será a la vista sin necesidad de presentación, demanda, protesto o aviso alguno, a todos los cuales el suscriptor renuncia expresamente.\n\n";
                    texto += "El suscriptor se obliga a pagar intereses sobre el monto principal total de este Pagaré a una tasa de interés global anual calculados sobre el monto total del presente Pagaré, ";
                    texto += "y por lo tanto aplicando dicha tasa de interés global anual al monto total del presente Pagaré, sin descontar amortizaciones de capital durante la vigencia del mismo.\n\n";
                    texto += "Los intereses se computarán sobre la base de días naturales efectivamente transcurridos (incluyendo el primer día; pero excluyendo el último día) sobre un año de trescientos sesenta (360) días. Si la fecha de vencimiento de cualquier abono no es un Día Hábil, ";
                    texto += "el pago se hará el primer Día Hábil inmediato posterior, a menos que dicha extensión cause que el pago se realice en el mes calendario inmediato siguiente, en cuyo caso el pago se hará en el Día Hábil inmediato anterior, y en todo caso dicha aplicación del plazo será en dicho caso incluida en el cómputo de intereses.\n\n";
                    texto += "En caso de incumplimiento del pago de cualquier monto de principal o intereses en la fecha de su vencimiento, el total del principal se considerará vencido y pagadero a opción y ";
                    texto += "petición del tenedor del presente pagaré y, a partir de ese incumplimiento, el total del principal insoluto de este pagaré devengará un interés moratorio, a una tasa de interés moratorio anual del " + HTMLHelper.formatoMonto(tasaInteres * 2) + " %.\n\n";
                    texto += "La suma principal de este pagaré y sus intereses se pagarán en moneda nacional, libre de cualquier deducción por cualquier impuesto, derecho, cargo o imposición fiscal ";
                    texto += "presente o futura con respecto a dichos pagos, los cuales en caso de ser aplicables, serán por cuenta del suscriptor y pagados por éste.\n\n";
                    texto += "Para los efectos del artículo 128 de la Ley General de Títulos y Operaciones de Crédito, el suscriptor prorroga irrevocablemente el plazo para la presentación de este pagaré hasta la fecha que ocurra 5 (cinco) años después de la fecha en que deba de hacerse ";
                    texto += "la última amortización arriba señalada, en el entendido de que dicha extensión no impedirá la presentación de este pagaré con anterioridad a dicha fecha. El suscriptor promete incondicionalmente pagar los gastos que impliquen el cobro de este pagaré y los honorarios de los abogados que intervengan en el mismo cobro.\n\n";
                    texto += "El suscriptor se somete expresamente a las leyes federales de los Estados Unidos Mexicanos y a la jurisdicción y competencia de los tribunales del domicilio del acreedor o del suscriptor, ";
                    texto += "a elección del Acreedor, por lo tanto, el suscriptor renuncia al fuero territorial que por cualquier causa pudiere corresponderle, ya sea en lo presente o en lo futuro.\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                    document.add(p);

                    p = new Paragraph();
                    texto = "Lugar y fecha de suscripción:________________________________________a__________de_______________________de 20_____";
                    ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    ck.setBackground(Color.yellow, 1, 1, 1, 1);
                    p.add(ck);
                    p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                    document.add(p);

                    texto = "\n\n\n\n\n________________________________________________________\n" + arrayIC[i].nombre;
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                    p.setAlignment(Chunk.ALIGN_CENTER);
                    p.setLeading(12);
                    document.add(p);

                    document.newPage();
                }
            }

        } catch (Exception e) {
            Logger.debug("Excepcion en contenido de Pagare Individual PDF");
            e.printStackTrace();
        }
    }
    
    
    
    public void doPagareGrupalAdicionalPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo, int semDispAdicional) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 40, 40);
            document.open();
            contenidoDocPagareGrupalAdicional(grupo, ciclo, semDispAdicional);
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main doPagarePDF");
            exception.printStackTrace();
        }
    }
    
    
    private void contenidoDocPagareGrupalAdicional(GrupoVO grupo, CicloGrupalVO ciclo, int semDispAdicional) throws ClientesException {

        TreeMap catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
        TasaInteresVO tasaVO = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
        double montoConIntereses = 0.00;
        TablaAmortizacionVO tablaIC[] = null;
        Date fechaDate = null;
        TablaAmortizacionDAO tablaDao = new TablaAmortizacionDAO();
        for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
            montoConIntereses += ciclo.tablaAmortizacion[i].montoPagar;
        }
        double tasa = Double.parseDouble(tasaVO.descripcion.replace("%", ""));
        //String montoIni = String.valueOf(ciclo.saldo.getMontoConIntereses());
        String montoIni = String.valueOf(montoConIntereses);
        int posicion = montoIni.indexOf(".");
        Integer entero = Integer.parseInt(montoIni.substring(0, posicion));
        ConvertNumberToString montoLetra = new ConvertNumberToString(entero);
        String mtoLetrasEntero = montoLetra.convertirLetras(entero);
        String mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");
        String fecha = "", mes = "";
        SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
        int cantIntegrates = 0;
        boolean imprime = false;
        int tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
        /**
        if (semDispAdicional == 0) {
            if (ciclo.estatusIC == 0 && ciclo.estatusIC2 == 0) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if ((ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO)
                    || (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO)) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO_2;
            } else if ((ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC2 > ClientesConstants.CICLO_DISPERSADO)
                    || (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO && ciclo.estatusIC == 0)) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            } else {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            }
        } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_2) {
            if(ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO&&ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO)
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            else if (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if (ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSO) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            }
        } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_4) {
            if (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO) {
                tipoTabla = ClientesConstants.AMORTIZACION_GRUPAL;
            } else if (ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO) {
                tipoTabla = ClientesConstants.AMORTIZACION_INTERCICLO;
            }
        }
        **/
        myLogger.debug("**tipoTabla:**" + tipoTabla);
        
        ciclo.setTablaAmortizacion(tablaDao.getElementos(ciclo.idGrupo, ciclo.idCiclo, 0, tipoTabla));
        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            String textoPronafin = "";
            texto = "NÚMERO DE REGISTRO DE CONTRATO DE ADHESIÓN: 13317-439-012789/04-05715-1017.                                                                                       ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE, Color.GRAY));
            p.setAlignment(Chunk.ALIGN_LEFT);
            document.add(p);

            texto = "\nPAGARÉ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph();
            texto = "\nPor el presente pagaré, los suscritos (en adelante y, en conjunto, los “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Deudores";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), prometen pagar incondicional y solidariamente a la orden de CEGE CAPITAL, S.A.P.I. DE C.V., SOFOM, E.N.R. (en adelante, el “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Acreedor";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), las cantidades que se señalan en la relación siguiente (cada una de ellas, un “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Abono";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "” y conjuntamente todas ellas, el “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Importe";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”), en cualquier lugar o domicilio en el que se le requiera de pago. El Importe será liquidado por los Deudores mediante el pago de parcialidades por el equivalente a cada Abono en el día señalado como “";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Fecha de Pago";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = "”, según se establece en la relación siguiente. En caso de que la Fecha de Pago de cualquier Abono, no sea día hábil bancario, el Abono de que se trate deberá pagarse el día hábil bancario inmediato anterior.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            int col[] = {20, 50, 30};
            tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100F);
            tabla.setWidths(col);

            texto = "Importe";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            //p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            tabla.addCell(cell);

            texto = "Fecha de Pago";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "Cantidad con número";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "Cantidad con letra";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);

            texto = "";
            p = new Paragraph(texto);
            cell = new PdfPCell(p);
            tabla.addCell(cell);

            texto = "$ " + FormatUtil.formatIntegerMiles(montoConIntereses) + " " + mtoDecimal + "/100 M.N.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            texto = mtoLetrasEntero.substring(0, 1).toUpperCase() + mtoLetrasEntero.substring(1, mtoLetrasEntero.length()) + " pesos " + mtoDecimal + "/100 M.N.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            //fecha = Convertidor.dateToString(ciclo.saldo.getFechaVencimiento());
            fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago);
            mes = FechasUtil.obtenNombreMes(fecha);
            texto = "El día " + ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " del año " + FechasUtil.obtenParteFecha(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago, 3) + ". (el “Vencimiento”).";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);

            texto = "Abonos";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            tabla.addCell(cell);

            texto = "";
            p = new Paragraph(texto);
            cell = new PdfPCell(p);
            tabla.addCell(cell);

            for (int i = 1; i < ciclo.tablaAmortizacion.length; i++) {
                montoIni = String.valueOf(ciclo.tablaAmortizacion[i].montoPagar);
                posicion = montoIni.indexOf(".");
                entero = Integer.parseInt(montoIni.substring(0, posicion));
                montoLetra = new ConvertNumberToString(entero);
                mtoLetrasEntero = montoLetra.convertirLetras(entero);
                mtoDecimal = FormatUtil.completaCadena(montoIni.substring(posicion + 1), '0', 2, "R");

                texto = "$ " + FormatUtil.formatIntegerMiles(ciclo.tablaAmortizacion[i].montoPagar) + " 00/100 M.N.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                texto = mtoLetrasEntero.substring(0, 1).toUpperCase() + mtoLetrasEntero.substring(1, mtoLetrasEntero.length()) + " pesos " + mtoDecimal + "/100 M.N.";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);

                fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[i].fechaPago);
                mes = FechasUtil.obtenNombreMes(fecha);
                texto = "El día " + ciclo.tablaAmortizacion[i].fechaPago.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " del año " + FechasUtil.obtenParteFecha(ciclo.tablaAmortizacion[i].fechaPago, 3) + "";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                p.setAlignment(Chunk.ALIGN_LEFT);
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);
            }
            document.add(tabla);

            texto = "\nEn caso que cualquiera de los Abonos, sea pagado en forma incompleta y/o extemporánea por los Deudores, se incrementará al importe del Abono de que se trate, el importe equivalente a $ 100 pesos (mismo que incluirá el Impuesto al Valor Agregado), por pago tardío.";
            texto += "\n\nEn caso que los Deudores no paguen en forma completa y/u oportuna cualquier cantidad que deba cubrirse derivada del presente pagaré, dicho monto insoluto causará intereses moratorios sobre la tasa mensual del " + tasa * 2 + " % (mismo que incluirá el Impuesto al Valor Agregado).";
            texto += "\n\nLos Deudores, en forma expresa, desde ahora: (i) autorizan al Acreedor para que endose y/o transmita y/o descuente y/o transfiera y/o ceda y/o negocie y/o afecte y/o grave este pagaré y los derechos de crédito que ampara, en cualquier tiempo y lugar, ";
            texto += "sirviendo el presente como la más amplia y necesaria autorización de los Deudores al respecto; (ii) amplían el plazo de presentación de este pagaré, para que su pago pueda efectuarse dentro del plazo de un año posterior a la fecha del último pago parcial previsto; ";
            texto += "y, (iii) autorizan al Acreedor para que, en caso de que los Deudores omitan liquidar cualesquiera de las parcialidades pactadas, en la fecha de pago de que se trate, pueda dar válidamente por vencido anticipadamente el plazo establecido en el presente pagaré y, por lo tanto, ";
            texto += "los Deudores estarán obligados a pagar al Acreedor, el saldo insoluto pendiente de pago y en su caso los saldos insolutos de los gastos de cobranza e intereses moratorios que sean causados.";
            texto += "\n\nCualquier pago efectuado por los Deudores, será aplicado por el Acreedor, para cubrir, en primer lugar y hasta donde alcance, el saldo insoluto de los gastos de cobranza de que se trate, en caso de que exista, ";
            texto += "seguidamente y hasta donde alcance, el saldo insoluto de los intereses moratorios de que se trate, en caso de que exista y, por último y hasta donde alcance, el saldo insoluto del Importe.";
            texto += "\n\nPara toda controversia o litigio de cualquier tipo en relación con el presente pagaré, los Deudores están de acuerdo en someterse expresamente a las leyes federales de los Estados Unidos Mexicanos y a la jurisdicción y competencia ";
            texto += "de los tribunales del domicilio del acreedor o de los deudores, a elección del Acreedor, por lo tanto, los Deudores renuncian al fuero territorial que por cualquier causa pudiere corresponderles, ya sea en lo presente o en lo futuro.\n";

            //fecha = Convertidor.dateToString(ciclo.saldo.getFechaDesembolso());
            /**
            if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_2) {
                if (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO || ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSO) {
                    fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[2].fechaPago);
                    fechaDate = ciclo.tablaAmortizacion[2].fechaPago;
                }
            }
            else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_4) {
                if (ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO || ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO) {
                    fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[4].fechaPago);
                    fechaDate = ciclo.tablaAmortizacion[4].fechaPago;
                }
            } else {
                fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[0].fechaPago);
                fechaDate = ciclo.tablaAmortizacion[0].fechaPago;
            }
            */
            //JECB 24/10/2017
            //Se obtiene la fecha de pago
            //de la tabla de amortizacion correspondiente con el adicional
            myLogger.debug("Semana de adicional ====:" + semDispAdicional);
            fecha = Convertidor.dateToString(ciclo.tablaAmortizacion[semDispAdicional].fechaPago);
            fechaDate = ciclo.tablaAmortizacion[semDispAdicional].fechaPago;
            
            mes = FechasUtil.obtenNombreMes(fecha);

            
            texto += "\n" + sucursal.estado + ", " + sucursal.municipio + ", a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n";

            texto += "\nLos Deudores\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_LEFT);
            document.add(p);

            int colF[] = {33, 33, 33};
            tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100F);
            tabla.setWidths(colF);

            for (int i = 0; i < ciclo.integrantes.length; i++) {
                imprime = false;
                /**
                if (semDispAdicional == 0 && ciclo.integrantes[i].tipo <= ClientesConstants.TIPO_CLIENTE_RECUPERADO) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_2 && (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO||ciclo.integrantes[i].tipo ==0)) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_4 && (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2||ciclo.integrantes[i].tipo ==0)) {
                    imprime = true;
                }
                */
                if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_4 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_4) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_5 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_5 ) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_6 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_6 ) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_7 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_7 ) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_8 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_8 ) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_9 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_9) {
                    imprime = true;
                } else if (semDispAdicional == ClientesConstants.DISPERSION_SEMANA_10 && ciclo.integrantes[i].tipo_adicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_10) {
                    imprime = true;
                }
                
                if (imprime) {
                    cantIntegrates++;
                    texto = "\n\n\n\n\n\n\n\n\n\n_________________________________\n" + ciclo.integrantes[i].nombre;
                    System.out.println("texto " + texto);
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                    p.setAlignment(Chunk.ALIGN_LEFT);
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(cell);           
                    
                }
            }
            int firmaComplemento = 4;
            if (semDispAdicional>0) {                
                for (int i = 0; i < firmaComplemento; i++) {
                    texto = "\n\n\n\n\n\n\n\n\n     _________________________________\n\n";
                    p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                    p.setAlignment(Chunk.ALIGN_LEFT);
                    cell = new PdfPCell(p);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(cell);
                }
                int Compceldas = 3- ((cantIntegrates + firmaComplemento)%3);
                if (Compceldas < 3) {
                    for (int i = 0; i < Compceldas; i++) {
                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                        p.setAlignment(Chunk.ALIGN_LEFT);
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setBorderWidthBottom(0);
                        cell.setBorderWidthRight(0);
                        cell.setBorderWidthTop(1);
                        tabla.addCell(cell);
                    }
                }
            }else{
                firmaComplemento = 3 - (cantIntegrates % 3);
                if (firmaComplemento < 3) {
                    for (int i = 0; i < firmaComplemento; i++) {
                        texto = "";
                        p = new Paragraph(texto, new Font(Font.HELVETICA, 8));
                        p.setAlignment(Chunk.ALIGN_LEFT);
                        cell = new PdfPCell(p);
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tabla.addCell(cell);
                    }
                }
            }
            document.add(tabla);
            //LLamar fondeador de ciclos grupales una vez terminado el desarrollo de Alex 
            if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR || ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS) {
                textoPronafin = "\n\nEndoso el presente pagaré en garantía a Nacional Financiera, S.N.C., I.B.D.; fiduciaria en el Fideicomiso del Fondo de Microfinanciamiento a Mujeres Rurales, (FOMMUR).\n"
                        + "\nCEGE CAPITAL, S.A.P.I. de C.V., SOFOM, E.N.R.\n"
                        + "Representada por: ___________________________________________________\n"
                        + "Mediante Carta Poder de Fecha ______ de __________________ de _______\n"
                        + ciclo.direccionReunion.estado + ", " + ciclo.direccionReunion.municipio
                        + " a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n"
                        + "\nFirma:__________________________________";
                p = new Paragraph(textoPronafin, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                document.add(p);

            }    
            if (ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM || ciclo.fondeador == ClientesConstants.ID_FONDEADOR_FINAFIM_P2) {
                textoPronafin = "\n\nEndoso el presente pagaré en garantía a Nacional Financiera, S.N.C., I.B.D.; fiduciaria del Fideicomiso del Programa Nacional de Financiamiento al Microempresario.\n"
                        + "Representada por: ___________________________________________________\n"
                        + "Mediante Carta Poder de Fecha ______ de __________________ de _______\n"
                        + ciclo.direccionReunion.estado + ", " + ciclo.direccionReunion.municipio
                        + " a " + fechaDate.getDate() + " de " + mes.substring(0, 1) + mes.substring(1, mes.length()).toLowerCase() + " de " + FechasUtil.obtenParteFecha(fechaDate, 3) + ".\n"
                        + "\nFirma:__________________________________";
                p = new Paragraph(textoPronafin, new Font(Font.HELVETICA, 8, Font.NORMAL));
                p.setAlignment(Chunk.ALIGN_JUSTIFIED);
                document.add(p);

            }
            

        } catch (Exception e) {
            Logger.debug("Excepcion en contenido de Pagare PDF");
            e.printStackTrace();
        }
    }
}

package com.sicap.clientes.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Code128Util;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mx.datalogic.paynet.rugen.Parameters;
import mx.datalogic.paynet.rugen.ReferenceGenerator;


public class GeneraFichaGarantiaHelper {
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(GeneraFichaGarantiaHelper.class);
    
    private PdfWriter pdfWrite = null;
    public void pdfWriter(HttpServletRequest request, HttpServletResponse response, Object objeto, int id) {

        ClienteVO cliente = null;
        GrupoVO grupo = null;
        String nombre = "";
        String referencia = "";
        boolean isGrupal = false;
        SaldoIBSVO saldo = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        int ciclo_anterior = 0;
        String strGarantia = "";
        double porGarantia = 0.00;

        if (objeto instanceof ClienteVO) {
            cliente = (ClienteVO) objeto;
        } else {
            grupo = (GrupoVO) objeto;
        }



        document = new Document(PageSize.LETTER);
        try {
            pdfWrite = PdfWriter.getInstance(document, response.getOutputStream());
            arialNum = Font.getFamilyIndex("Helvetica");
            titleFont = new Font(arialNum, 8F, 1);
            headFont = new Font(arialNum, 10F, 1);
            tableFont = new Font(arialNum, 5.0F, 1);
            labelFont = new Font(arialNum, 5.0F, 2);
            smallFont = new Font(arialNum, 4.0F, 1);
            numberFont = new Font(arialNum, 5.0F, 1);

            HeaderFooter headerfooter1 = new HeaderFooter(new Phrase("Pagina ", new Font(arialNum, 8F)), true);
            headerfooter1.setAlignment(2);
            headerfooter1.setBorder(0);
            document.setFooter(headerfooter1);
            //left, right, up, down
            document.setMargins(10, 10, 40, 10);
            document.open();
            Double montoSeguro = 0.00;
            Double montoAhorro = 0.00;
            TablaAmortizacionVO[] tablaVO = null;
            TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
            CicloGrupalVO ciclo = null;
            CicloGrupalVO cicloAnt = null;
            int indiceSolicitud = 0;
            if (cliente != null) {
                if (id == 0) {
                    id = ((Integer) request.getAttribute("ID_SOLICITUD")).intValue();
                }
                indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, id);

                SegurosVO seguro = cliente.solicitudes[indiceSolicitud].seguro;
                if (seguro != null && seguro.primaTotal == 0) {
                    montoSeguro = FormatUtil.roundDouble(SeguroHelper.getMontoPeriodo(cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, seguro.sumaAsegurada, seguro.modulos), 2);
                }

                int idCliente = cliente.idCliente;
                //referencia = ClientesUtil.makeReferencia(cliente, id);
                tablaVO = tablaDAO.getElementos(idCliente, id, 0);
                nombre = cliente.nombreCompleto;

            } else {
                ciclo = GrupoUtil.getCiclo(grupo.ciclos, id);
                tablaVO = ciclo.tablaAmortizacion;
                //referencia = ClientesUtil.makeReferenciaGrupal(grupo, id);
                nombre = grupo.nombre;
                isGrupal = true;
                //montoAhorro = tablaVO[0].saldoInicial * .10;
                strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
                porGarantia = Double.parseDouble(strGarantia);
                porGarantia = porGarantia/100;
                /**
                 * JECB 10/10/2017
                 * Apartir de ahora el importe a imprimir 
                 * se obtendra de la tabla d_saldos del esquema de cartera
                 * monto_desembolsado
                 */
                //montoAhorro = tablaVO[0].saldoInicial * porGarantia;
                montoAhorro = ciclo.monto * porGarantia;
                myLogger.debug("ciclo.monto:" + ciclo.monto + ", montoAhorro:" + montoAhorro);
 
                // En caso de ser una renovacion se traera el saldo a favor del ciclo anterior        
                if (ciclo.idCiclo > 1) {
                    cicloAnt = GrupoUtil.getCiclo(grupo.ciclos, ciclo.idCiclo - 1);
                    if (cicloAnt != null) {
                        saldo = saldoDAO.getSaldo(cicloAnt.idGrupo, cicloAnt.idCiclo);
                        //montoAhorro = tablaVO[0].saldoInicial * .10 - saldo.getSaldoBucket();
                        /**
                         * JECB 10/10/2017
                         * al tratarse de una renovacion se toma en cuenta el saldo bucket
                         * se obtendra de la tabla d_saldos del esquema de cartera
                         * monto_desembolsado
                         */
                        myLogger.debug("renovacion se traera el saldo a favor del ciclo anterior");
                        myLogger.debug("SALDO BUCKET:" + saldo.getSaldoBucket());
                        montoAhorro = ciclo.monto * porGarantia - saldo.getSaldoBucket();
                    }
                }


            }

            if (tablaVO != null) {
                if (!isGrupal) {
                    generaDocumento(cliente.solicitudes[indiceSolicitud].referencia, nombre, tablaVO, montoSeguro, isGrupal);
                } else {
                    generaDocumento(ciclo.referencia, nombre, tablaVO, montoAhorro, isGrupal);
                }
            }
            document.close();
        } catch (Exception exception) {
            Logger.debug("Excepcion en main pdfWriter");
            exception.printStackTrace();
        }
    }

    private void generaDocumento(String referencia, String nombreCompleto, TablaAmortizacionVO[] tabla, double montoAhorro, boolean isGrupal)
            throws BadElementException, DocumentException, Exception {


        String nombre = (isGrupal ? nombreCompleto : nombreCompleto.substring(0, nombreCompleto.indexOf(' ')));
//		String nombre = nombreCompleto.substring(0, nombreCompleto.indexOf(' '));
        String comentarios = "...";
        int ai[] = {50, 50};
        PdfPTable dataTabla = new PdfPTable(2);
        dataTabla.setWidthPercentage(100F);
        dataTabla.setWidths(ai);
        CatalogoDAO paramDao = new CatalogoDAO();
        ParametroVO paramVO = new ParametroVO();
        paramVO = paramDao.getParametro("RUTA_IMAGEN_REPORTE");
        Image LogoCF = Image.getInstance(paramVO.valor);
        LogoCF.scalePercent(30);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_COMER");
        Image LogoComer = Image.getInstance(paramVO.valor);
        LogoComer.scalePercent(35);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_TELECOM");
        Image LogoTelecom = Image.getInstance(paramVO.valor);
        LogoTelecom.scalePercent(35);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_BENAVIDES");
        Image LogoBena = Image.getInstance(paramVO.valor);
        LogoBena.scalePercent(25);
        GregorianCalendar fechaVig = new GregorianCalendar();
        fechaVig.setTime(tabla[0].fechaPago);
        fechaVig.add(Calendar.YEAR, 4);
        FontFactory.registerDirectories();
        paramVO = paramDao.getParametro("GLN_INDIVIDUAL");
        String glnIndividual = paramVO.valor;
        paramVO = paramDao.getParametro("GLN_LINEA_CAPTURA");
        String glnLineaCap = paramVO.valor;
        ReferenceGenerator refGen = new ReferenceGenerator();
        paramVO = paramDao.getParametro("ID_EMISOR_PAYNET");
        String issuer = paramVO.valor;
        ArrayList<Parameters> lstParam = new ArrayList<Parameters>();
        lstParam.add(new Parameters("IDPAGO", referencia));

        String refPaynet = refGen.CreateRU(issuer, lstParam);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_LOGOSPAYNET");
        Image LogosPaynet = Image.getInstance(paramVO.valor);
        LogosPaynet.scalePercent(50);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_LOGOPAYNET");
        Image LogoPaynet = Image.getInstance(paramVO.valor);
        //LogosPaynet.scalePercent(50);
        paramVO = paramDao.getParametro("RUTA_IMAGEN_SOL_WOOLWORRTH");
        Image Logo711Sol = Image.getInstance(paramVO.valor);
        Logo711Sol.scalePercent(25);
        
        //JECB 23/11/2017
        //Configuracion de la imagen de soriana y casa ley 
        paramVO = paramDao.getParametro("RUTA_IMAGEN_SORIANA");
        Image LogoSoriana = Image.getInstance(paramVO.valor);
        LogoSoriana.scalePercent(40);
        
        
        PdfContentByte contByte = pdfWrite.getDirectContent();
        Barcode128 code128 = new Barcode128();
        Image imgCode = null;

        PdfPCell celda = null;
        celda = new PdfPCell(LogoCF);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        Paragraph titulo1 = new Paragraph("DEPOSITO EN GARANTÍA \n" + "", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC));

        celda = new PdfPCell(titulo1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        if (referencia == null) {
            referencia = "";
        }

        if (isGrupal) {
            comentarios = "Estimado Equipo: " + nombre + ", esta ficha les servirá para realizar su depósito de garantía por un monto de " + montoAhorro + "  sólo deben presentarlo al cajero del banco de su preferencia.\n\n\n";
        } else {
            comentarios = "Estimado(a): " + nombre + ", este tarjetón te será de gran utilidad para dar seguimiento y controlar tus pagos, sólo debes presentarlo al cajero del banco de tu preferencia.\n\n\n";
        }

        celda = new PdfPCell(new Phrase(comentarios, headFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        PdfPTable bancos = new PdfPTable(1);
        bancos.setWidthPercentage(100F);

        celda = new PdfPCell(new Paragraph((isGrupal ? "Grupo:  " : "Cliente:  ") + nombreCompleto, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        Paragraph p = null;

        celda = new PdfPCell(new Paragraph("Banco       No. Convenio/Cuenta     Referencia", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        p = new Paragraph(new Paragraph("Banorte                       " + ClientesConstants.CUENTA_BANORTE+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        
        p.setAlignment(Element.ALIGN_LEFT);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        
        Chunk c = new Chunk(Logo711Sol, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);        
        bancos.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Banamex        "+ClientesConstants.CUENTA_BANAMEX+" suc. 7006        "+referencia, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Scotiabank                 "+ClientesConstants.CUENTA_SCOTIABANK+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("BanBajio                    "+ClientesConstants.CUENTA_BAJIO+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Bancomer              "+ClientesConstants.CUENTA_BANCOMER+"               "+referencia, FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Bansefi                     "+"- - - - -"+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        p = new Paragraph("Telecom                   "+"- - - - -"+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLDITALIC));
        p.setAlignment(Element.ALIGN_LEFT);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        
        c = new Chunk(LogoTelecom, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);
        bancos.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        p = new Paragraph("Benavides                "+"- - - - -"+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLDITALIC));
        p.setAlignment(Element.ALIGN_LEFT);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        
        c = new Chunk(LogoBena, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);        
        bancos.addCell(celda);

        /**
         * //JECB 23/11/2017
         * //Se agregan las referencias de soriana
         * JECB 04/12/2017
         * descomentar la refenrencia de SORIANA
         */
        celda = new PdfPCell(new Paragraph("Soriana                   "+"- - - - -"+"                 "+referencia, FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(0.5f);
        bancos.addCell(celda);
        
        PdfPCell cellTablaBanco = new PdfPCell();
        cellTablaBanco.setBorder(Rectangle.NO_BORDER);
        cellTablaBanco.setColspan(1);
        cellTablaBanco.addElement(bancos);
        dataTabla.addCell(cellTablaBanco);
        
        PdfPTable tablaCodBarras = new PdfPTable(1);
        tablaCodBarras.setWidthPercentage(100f);
        
        celda = new PdfPCell(); 
        celda.setBorder(Rectangle.NO_BORDER);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        
        
        c = new Chunk(LogoComer, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);        
        tablaCodBarras.addCell(celda);
        
        celda = new PdfPCell(); 
        celda.setBorder(Rectangle.NO_BORDER);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        
        c = new Chunk(LogoSoriana, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);        
        tablaCodBarras.addCell(celda);
        
        celda = new PdfPCell(); 
        celda.setBorder(Rectangle.NO_BORDER);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);  
        
        tablaCodBarras.addCell(celda);
        
        /*GENERACION DE CODIGO DE BARRAS GS1*/
        celda = new PdfPCell(new Paragraph("LINEA DE CAPTURA UNIVERSAL", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        tablaCodBarras.addCell(celda);
        glnLineaCap = ClientesUtil.getDigitoVerificadorMod97(glnLineaCap+"100"+FormatUtil.completaCadena(referencia, '0', 16, "L")+"400");
        glnLineaCap = glnLineaCap.substring(0, 4)+"  "+glnLineaCap.substring(4, 8)+"  "+glnLineaCap.substring(8, 12)+"  "+glnLineaCap.substring(12, 16)+"  "+glnLineaCap.substring(16, 20)+"  "+glnLineaCap.substring(20, 24)+"  "+glnLineaCap.substring(24, 28)+"  "+glnLineaCap.substring(28, 32);
        celda = new PdfPCell(new Paragraph(glnLineaCap, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        tablaCodBarras.addCell(celda);
        
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p); 
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        tablaCodBarras.addCell(celda);
        
        //celda = new PdfPCell(new Paragraph(Code128Util.barras("Ê415"+glnIndividual+"90"+"0100"+"Ê8020"+"0"+referencia), FontFactory.getFont("code128", 36)));
        code128.setCode("Ê415"+glnIndividual+"90"+"0100"+"Ê8020"+"0"+referencia);
        code128.setX(1f);
        code128.setN(2f);
        code128.setBarHeight(35f);
        code128.setSize(0.01f);
        imgCode = code128.createImageWithBarcode(contByte, Color.BLACK, Color.WHITE);
        
        celda = new PdfPCell();
        celda.setBorderWidth(Rectangle.NO_BORDER);
        c = new Chunk(imgCode, 0, 0, true);
        p = new Paragraph(c);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);        
        tablaCodBarras.addCell(celda);
        
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        celda = new PdfPCell(new Paragraph("(415)"+glnIndividual+"(90)"+"0100"+"(8020)"+"0"+referencia, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        tablaCodBarras.addCell(celda);
        
        PdfPCell cellNestedTabla = new PdfPCell();
        cellNestedTabla.setBorder(Rectangle.NO_BORDER);
        cellNestedTabla.setColspan(1);
        cellNestedTabla.addElement(tablaCodBarras);
        dataTabla.addCell(cellNestedTabla);
        
        celda = new PdfPCell(new Paragraph("\n\n\nCEGE CAPITAL, S.A.P.I. DE C.V. SOFOM E.N.R.\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("\nPara un manejo adecuado de tu crédito, te sugerimos seguir estos sencillos pasos:\n\n* Realiza tu depósito de garantía antes de la fecha de desembolso pactada.\n\n* La REFERENCIA y el NÚMERO DE SERVICIO son los que nos permiten identificar tus pagos por lo que es de suma importancia que el cajero del banco los registre correctamente.\n\n* Cada vez que acudas a realizar tu pago, presenta el tarjetón al cajero para que digite los datos de manera adecuada.\n\n*La REFERENCIA es únicamente válida para el crédito aquí mostrado,\npara créditos posteriores se entregará un nuevo tarjetón con una nueva REFERENCIA.\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);

        celda = new PdfPCell(new Paragraph("\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(2);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        document.add(dataTabla);
        if(montoAhorro < 10000){
            
            AnexoOXXO(nombre, montoAhorro, referencia, tabla, LogoCF);
            //Invocacion para generacion de la hoja de Oxxo con importe de $1.00
            //AnexoOXXO(nombre, 1d, referencia, tabla, LogoCF);
        }
        /**
        * JECB 04/12/2017
        * descomentar la siguiente linea para la invocacion que genera el documento de open pay
        * //Invocación a hoja para canal de pago de open pay
        */
        //generaDocOpenPay(referencia, nombreCompleto,  tabla, montoAhorro, isGrupal);
        
    }
    Document document;
    Font titleFont;
    Font headFont;
    Font tableFont;
    Font labelFont;
    Font smallFont;
    Font numberFont;
    int arialNum;
    Table dataTable;
    String fechaHoy;
    
    private void AnexoOXXO(String nombreEquipo, double montoAhorro, String referencia, TablaAmortizacionVO[] tabla, Image LogoCF) throws DocumentException, ClientesException, Exception{
        
        document.newPage();
        
        int ai[] = {50,50};
        PdfPTable dataTabla = new PdfPTable(2);
        dataTabla.setWidthPercentage(100F);
        dataTabla.setWidths(ai);
    
        String texto = "";
        Paragraph p = new Paragraph();
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(1);
        PdfPTable table2 = new PdfPTable(2);
        CatalogoDAO paramDAO = new CatalogoDAO();
        ParametroVO paramVO = paramDAO.getParametro("CODIGO_BARRA_OXXO");
        String codigoOxxo = paramVO.valor;
        paramVO = paramDAO.getParametro("RUTA_IMAGENES");
        String rutaImagenes = paramVO.valor;
        GregorianCalendar fechaVig = new GregorianCalendar();
        fechaVig.setTime(tabla[0].fechaPago);
        fechaVig.add(Calendar.YEAR, 4);
        String vigencia = String.valueOf(fechaVig.get(Calendar.YEAR))+FormatUtil.completaCadena(fechaVig.get(Calendar.MONTH)+1 + "", '0', 2, "L")+FormatUtil.completaCadena(fechaVig.get(Calendar.DATE)+ "", '0', 2, "L");
        int montoCodigo = (int) (FormatUtil.roundDouble(montoAhorro, 2) * 100);
        String referenciaCodigo = codigoOxxo+referencia+vigencia+FormatUtil.completaCadena(montoCodigo+"", '0', 7, "L");
        referenciaCodigo = ClientesUtil.getDigitoVerificadorBase19(referenciaCodigo);
        Image imagenOxxo = Image.getInstance(rutaImagenes+"oxxo.jpg");
        imagenOxxo.scalePercent(25);
        
        paramVO = paramDAO.getParametro("RUTA_IMAGEN_OXXO_BARA_LEY");
        Image imagenOxxoBaraLey = Image.getInstance(paramVO.valor);
        //Tamaño sin logo casa ley
        imagenOxxoBaraLey.scalePercent(30);
        //Tamaño con logo casa ley
        //imagenOxxoBaraLey.scalePercent(35);
        
        
        PdfContentByte contByte = pdfWrite.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCode(referenciaCodigo);
        code128.setX(1.1f);
        code128.setN(2f);
        code128.setBarHeight(35f);
        code128.setSize(0.01f);
        Image imgCode = code128.createImageWithBarcode(contByte, Color.BLACK, Color.WHITE);
        
        PdfPCell celda = null;
        
        Paragraph titulo1 = new Paragraph("DEPOSITO EN GARANTÍA \n" + "", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC));

        celda = new PdfPCell(titulo1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        
        celda = new PdfPCell(LogoCF);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        

        texto = "\n\nEstimado Equipo: "+nombreEquipo+", esta ficha les servirá para realizar su depósito de garantía por un monto de $"+FormatUtil.formatDobleMiles(montoAhorro)+" sólo deben presentarlo al cajero del OXXO de su preferencia.";

        celda = new PdfPCell(new Phrase(texto, headFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell();
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        
        dataTabla.addCell(celda);

        celda.addElement(imagenOxxoBaraLey);
        
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell();
        p = new Paragraph(Chunk.NEWLINE);
        celda.addElement(p);
        
        p = new Paragraph("Para un manejo adecuado de tu crédito, te sugerimos seguir estos sencillos pasos:\n\n* Realiza tu depósito de garantía antes de la fecha de desembolso pactada.\n\n* La REFERENCIA y el NÚMERO DE SERVICIO son los que nos permiten identificar tus pagos por lo que es de suma importancia que el cajero del banco los registre correctamente.\n\n* Cada vez que acudas a realizar tu pago, presenta el tarjetón al cajero para que digite los datos de manera adecuada.\n\n*La REFERENCIA es únicamente válida para el crédito aquí mostrado,\npara créditos posteriores se entregará un nuevo tarjetón con una nueva REFERENCIA.\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC));
        celda.addElement(p);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        
        celda = new PdfPCell(imgCode);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        dataTabla.addCell(celda);
        

        texto = referenciaCodigo.substring(0, 5)+"  "+referenciaCodigo.substring(5, 10)+"  "+referenciaCodigo.substring(10, 15)+"  "+referenciaCodigo.substring(15, 20)+"  "+referenciaCodigo.substring(20, 25)+"  "+referenciaCodigo.substring(25, 31);
        celda = new PdfPCell(new Paragraph(texto));
        celda.setColspan(1);
        celda.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        dataTabla.addCell(celda);
        

        texto = "\nHorario de Recepcion de Pagos durante las 24 hrs. El monto maximo de recepcion de pagos sera de $10,000.00 Diez mil pesos(00/100 mn)";
        celda = new PdfPCell(new Paragraph(texto, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setColspan(1);
        celda.setHorizontalAlignment(Chunk.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        dataTabla.addCell(celda);
        
        celda = new PdfPCell();
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        dataTabla.addCell(celda);

        document.add(dataTabla);
    }
    
    private PdfPCell AnexoOXXO(Image logo) throws DocumentException, ClientesException, Exception{
        
        String texto = "\n\n";
        Paragraph p = new Paragraph();
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(1);
        
        cell = new PdfPCell(logo);
        cell.setHorizontalAlignment(1);
        cell.setVerticalAlignment(0);
        cell.setBorderWidth(0);
        table.addCell(cell);
        
        texto += "\nCEGE CAPITAL, S.A.P.I. DE C.V. SOFOM E.N.R.\n\n";
        cell = new PdfPCell(new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLDITALIC)));
        cell.setBorderWidth(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Chunk.ALIGN_CENTER);
        table.addCell(cell);
        
        texto = "\nPara un manejo adecuado de tu crédito, te sugerimos seguir estos sencillos pasos:";
        texto += "\n\n* Realiza tu depósito de garantía antes de la fecha de desembolso pactada.";
        texto += "\n\n* La REFERENCIA y el NÚMERO DE SERVICIO son los que nos permiten identificar tus pagos por lo que es de suma importancia que el cajero del banco los registre correctamente.";
        texto += "\n\n* Cada vez que acudas a realizar tu pago, presenta el tarjetón al cajero para que digite los datos de manera adecuada.";
        texto += "\n\n* La REFERENCIA es únicamente válida para el crédito aquí mostrado, para créditos posteriores se entregará un nuevo tarjetón con una nueva REFERENCIA.";
        cell = new PdfPCell(new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLDITALIC)));
        cell.setBorderWidth(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Chunk.ALIGN_LEFT);
        table.addCell(cell);
        
        cell = new PdfPCell(table);
        cell.setBorderWidth(0);
        cell.setHorizontalAlignment(1);
        
        return cell;
    }
    
    /**
     * JECB 23/11/2017
     * Método que genera la hoja de ficha de garantia para open pay
     * @param referencia
     * @param nombreCompleto
     * @param tabla
     * @param montoAhorro
     * @param isGrupal
     * @throws BadElementException
     * @throws DocumentException
     * @throws Exception 
     */
    private void generaDocOpenPay(String referencia, String nombreCompleto, TablaAmortizacionVO[] tabla, double montoAhorro, boolean isGrupal)
            throws BadElementException, DocumentException, Exception {

        document.newPage();
        
        String nombre = (isGrupal ? nombreCompleto : nombreCompleto.substring(0, nombreCompleto.indexOf(' ')));

        String comentarios = "...";

        CatalogoDAO paramDao = new CatalogoDAO();
        ParametroVO paramVO = new ParametroVO();
        paramVO = paramDao.getParametro("RUTA_IMAGEN_REPORTE");
        Image LogoCF = Image.getInstance(paramVO.valor);
        LogoCF.scalePercent(30);
        
        paramVO = paramDao.getParametro("RUTA_IMAGEN_LOGOPAYNET");
        Image LogoPaynet = Image.getInstance(paramVO.valor);
        LogoPaynet.scalePercent(40);
       
        paramVO = paramDao.getParametro("RUTA_IMAGEN_LOGOSPAYNET");
        Image LogosAfiliadasPaynet = Image.getInstance(paramVO.valor);
        LogosAfiliadasPaynet.scalePercent(30);
        
        GregorianCalendar fechaVig = new GregorianCalendar();
        fechaVig.setTime(tabla[0].fechaPago);
        fechaVig.add(Calendar.YEAR, 4);
        FontFactory.registerDirectories();
        paramVO = paramDao.getParametro("GLN_INDIVIDUAL");
        String glnIndividual = paramVO.valor;
        paramVO = paramDao.getParametro("GLN_LINEA_CAPTURA");
        String glnLineaCap = paramVO.valor;
        ReferenceGenerator refGen = new ReferenceGenerator();

        myLogger.debug("Referencia original:" + referencia);
        paramVO = paramDao.getParametro("ID_EMISOR_OPENPAY");//00071

        String issuer = paramVO.valor;
        ArrayList<Parameters> lstParam = new ArrayList<Parameters>();
        //Se genera substring, se omite los 3 primeros digitos de sucursal y el ultimo del digito verificador
        String strOpenPay = referencia.substring(3,referencia.length()-1);
        myLogger.debug("Referencia openpay antes de la generacion:" + strOpenPay);
        lstParam.add(new Parameters("IDPAGO", strOpenPay));
        //--------------- 230317 ---------------
        String refOpenpay = refGen.CreateRU(issuer, lstParam);
        myLogger.debug("Referencia openpay despues de la generacion:" + refOpenpay);
        
        PdfContentByte contByte = pdfWrite.getDirectContent();
        Barcode128 code128 = new Barcode128();
        Image imgCode = null;

        int hp[] = {100};
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100F);
        table.setWidths(hp);

        
        PdfPCell celda = null;
        celda = new PdfPCell(LogoCF);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        
        Paragraph titulo1 = new Paragraph("\nDEPOSITO EN GARANTÍA \n" + "", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC));

        celda = new PdfPCell(titulo1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);

        if (referencia == null) {
            referencia = "";
        }

        if (isGrupal) {
            comentarios = "Estimado Equipo: " + nombre + ", esta ficha les servirá para realizar su depósito de garantía por un monto de " + montoAhorro + "  sólo deben presentarlo al cajero del banco de su preferencia.\n\n\n";
        } else {
            comentarios = "Estimado(a): " + nombre + ", este tarjetón te será de gran utilidad para dar seguimiento y controlar tus pagos, sólo debes presentarlo al cajero del banco de tu preferencia.\n\n\n";
        }

        celda = new PdfPCell(new Phrase(comentarios, headFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);

        celda = new PdfPCell(LogoPaynet);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);

        
        
        celda = new PdfPCell(new Paragraph("\n\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        
        code128.setCode(refOpenpay);
        code128.setX(1.5f);
        code128.setN(2f);
        code128.setBarHeight(35f);
        code128.setSize(0.01f);
        imgCode = code128.createImageWithBarcode(contByte, Color.BLACK, Color.WHITE);
        celda = new PdfPCell(imgCode);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        
        celda = new PdfPCell(new Paragraph(refOpenpay+"\n\n", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        
        celda = new PdfPCell(LogosAfiliadasPaynet);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        //------------- 230317 -------------
        
        celda = new PdfPCell(new Paragraph("\nCEGE CAPITAL, S.A.P.I. DE C.V. SOFOM E.N.R.\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(1);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("\nPara un manejo adecuado de tu crédito, te sugerimos seguir estos sencillos pasos:\n\n* Realiza tu depósito de garantía antes de la fecha de desembolso pactada.\n\n* La REFERENCIA y el NÚMERO DE SERVICIO son los que nos permiten identificar tus pagos por lo que es de suma importancia que el cajero del banco los registre correctamente.\n\n* Cada vez que acudas a realizar tu pago, presenta el tarjetón al cajero para que digite los datos de manera adecuada.\n\n*La REFERENCIA es únicamente válida para el crédito aquí mostrado,\npara créditos posteriores se entregará un nuevo tarjetón con una nueva REFERENCIA.\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);

        celda = new PdfPCell(new Paragraph("\n\n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(celda);
        //Parte de info
        
        int hp2[] = {50,50};
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100f);
        info.setWidths(hp2);
                
        celda = new PdfPCell(new Paragraph("Como realizar el pago", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Instrucciones para el cajero", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("1. Acude a cualquier tienda afiliada.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("1. Ingresa al menú de pagos de Servicios.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
       
        celda = new PdfPCell(new Paragraph("2. Entrega al cajero el código de barras y menciona que realizarás un \n pago de servicio Paynet.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("2. Selecciona Paynet.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("3. Realizar el pago en efectivo por $xxxxxx (más $8.00 de comisión)", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("3. Ingresa la cantidad total a pagar.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("4 Conserva el ticket para cualquier aclaración.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("4. Cobra al cliente la transacción y entrega el ticket al cliente.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("5. Confirmar la transacción y entrega el ticket al cliente.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(new Paragraph("Para cualquier duda sobre como cobrar, por favor llamar al\n 01 800 22 88 133 en un horario de 8am a 9pm de luner a domingo.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderWidth(Rectangle.NO_BORDER);
        info.addCell(celda);
        
        celda = new PdfPCell(info);
        celda.setBorder(Rectangle.NO_BORDER);
        
        table.addCell(celda);      
        
        document.add(table);
    }
}
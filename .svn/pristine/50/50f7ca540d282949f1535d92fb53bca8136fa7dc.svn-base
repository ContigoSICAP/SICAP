package com.sicap.clientes.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ParametroVO;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;




public class PDFHelper{
	
	public void pdfWriter(HttpServletRequest request, HttpServletResponse response, Hashtable buro){
		document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            arialNum = Font.getFamilyIndex("Helvetica");
            titleFont = new Font(arialNum, 8F, 1);
            headFont = new Font(arialNum, 10F, 1);
            tableFont = new Font(arialNum, 5.0F, 1);
            labelFont = new Font(arialNum, 5.0F,2);
            smallFont = new Font(arialNum, 4.0F, 1);
            numberFont = new Font(arialNum, 5.0F, 1);
            
            HeaderFooter headerfooter1 = new HeaderFooter(new Phrase("Pagina ", new Font(arialNum, 8F)), true);
            headerfooter1.setAlignment(2);
            headerfooter1.setBorder(0);
            document.setFooter(headerfooter1);
            document.open();
            generaDocumento(buro);
            document.close();
        } catch (Exception exception) {
        	Logger.debug("Excepcion en main pdfWriter");
        }
    }


    public void generaDocumento(Hashtable buro) {
        try {
            setHeader();
            setDatosGenerales(buro);
            setDomicilios(buro);
            setDetalleCreditos(buro);
            //document.newPage();
            setResumen();
            setDetalleConsultas(buro);
        }
        catch(BadElementException badelementexception)
        {
            badelementexception.printStackTrace();
            Logger.debug("BadElementException PdfFormater -> " + badelementexception.toString());
        }
        catch(DocumentException documentexception)
        {
            documentexception.printStackTrace();
            Logger.debug("DocumentException PdfFormater -> " + documentexception.toString());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            Logger.debug("Exception PdfFormater -> " + exception.toString());
        }

    }	
	
	
	private void setHeader() throws BadElementException, DocumentException {
    	try{
    	CatalogoDAO paramDao = new CatalogoDAO();
    	ParametroVO paramVO = new ParametroVO();
    	paramVO = paramDao.getParametro("RUTA_IMAGEN_REPORTE");
    	Image headerImage = Image.getInstance(paramVO.valor);
    	headerImage.scalePercent(50);
       	Calendar cal = Calendar.getInstance();
       	Date fecha = cal.getTime();
        fechaHoy = Convertidor.dateToString(fecha);
     		
    	dataTable = new Table(2);
        int ai[] = {50,50};
        dataTable.setWidth(100F);
        dataTable.setBorder(0);
        dataTable.setDefaultCellBorder(0);
        dataTable.setWidths(ai);
        dataTable.setDefaultHorizontalAlignment(Element.ALIGN_RIGHT);
        dataTable.setDefaultVerticalAlignment(5);
        dataTable.setCellsFitPage(true);
        dataTable.setTableFitsPage(true);
        Cell cell1 = new Cell(headerImage);
        cell1.setHorizontalAlignment(0);
        cell1.setColspan(1);
        dataTable.addCell(cell1);
        Cell cell2 = new Cell(new Phrase("Fecha consulta: ", labelFont));
        cell2.add(new Phrase(fechaHoy, tableFont));
        cell2.setHorizontalAlignment(2);
        cell2.setColspan(1);
        dataTable.addCell(cell2);
        Cell cell3 = new Cell(new Paragraph("REPORTE DE CREDITO", headFont));
        cell3.setHorizontalAlignment(1);
        cell3.setColspan(2);
        dataTable.addCell(cell3);
        document.add(dataTable);
        
    	} catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException();
        }

    }
    

	private void setDatosGenerales(Hashtable buro)
        throws BadElementException, DocumentException, Exception{

        int ai[] = {23, 23, 10, 14, 10, 10, 10};
        dataTable = new Table(7);
        dataTable.setWidth(100F);
        dataTable.setSpacing(0F);
        dataTable.setPadding(0.2F);
        dataTable.setWidths(ai);
        dataTable.setCellsFitPage(true);

        Cell cell = new Cell(new Phrase("DATOS GENERALES",titleFont));
        cell.setHorizontalAlignment(1);
        cell.setVerticalAlignment(0);
        cell.setColspan(7);
        cell.setGrayFill(1);
        dataTable.addCell(cell);

        Cell cell1 = new Cell(new Phrase("NOMBRES",labelFont));
        cell1.setHorizontalAlignment(1);
        cell1.setVerticalAlignment(0);
        cell1.setColspan(1);
        dataTable.addCell(cell1);

        Cell cell2 = new Cell(new Phrase("APELLIDOS",labelFont));
        cell2.setHorizontalAlignment(1);
        cell2.setVerticalAlignment(0);
        cell2.setColspan(1);
        dataTable.addCell(cell2);

        Cell cell3 = new Cell(new Phrase("RFC",labelFont));
        cell3.setHorizontalAlignment(1);
        cell3.setVerticalAlignment(0);
        cell3.setColspan(1);
        dataTable.addCell(cell3);

        Cell cell4 = new Cell(new Phrase("FECHA DE NACIMIENTO",labelFont));
        cell4.setHorizontalAlignment(1);
        cell4.setVerticalAlignment(0);
        cell4.setColspan(1);
        dataTable.addCell(cell4);

        Cell cell5 = new Cell(new Phrase("IFE",labelFont));
        cell5.setHorizontalAlignment(1);
        cell5.setVerticalAlignment(0);
        cell5.setColspan(1);
        dataTable.addCell(cell5);

        Cell cell6 = new Cell(new Phrase("CURP",labelFont));
        cell6.setHorizontalAlignment(1);
        cell6.setVerticalAlignment(0);
        cell6.setColspan(1);
        cell6.setGrayFill(3);
        dataTable.addCell(cell6);

        Cell cell7 = new Cell(new Phrase("REGISTRO EN BC",labelFont));
        cell7.setHorizontalAlignment(1);
        cell7.setVerticalAlignment(0);
        cell7.setColspan(1);
        cell7.setGrayFill(2);
        dataTable.addCell(cell7);        

        
        Hashtable PN = (Hashtable)buro.get(1);
        //Nombres
        Cell cell8 = new Cell(new Phrase(HTMLHelper.displayField((String)PN.get("02")) + " " + HTMLHelper.displayField((String)PN.get("03")),tableFont));
        cell8.setHorizontalAlignment(1);
        cell8.setVerticalAlignment(0);
        cell8.setColspan(1);
        dataTable.addCell(cell8);
        //Apellidos paterno y materno
        Cell cell9 = new Cell(new Phrase(HTMLHelper.displayField((String)PN.get("PN")) + " " + HTMLHelper.displayField((String)PN.get("00")),tableFont));
        cell9.setHorizontalAlignment(1);
        cell9.setVerticalAlignment(0);
        cell9.setColspan(1);
        dataTable.addCell(cell9);
        //RFC
        Cell cell10 = new Cell(new Phrase(HTMLHelper.displayField((String)PN.get("05")),tableFont));
        cell10.setHorizontalAlignment(1);
        cell10.setVerticalAlignment(0);
        cell10.setColspan(1);
        dataTable.addCell(cell10);        
        //Fecha de nacimiento
        Cell cell11 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)PN.get("04"))),tableFont));
        cell11.setHorizontalAlignment(1);
        cell11.setVerticalAlignment(0);
        cell11.setColspan(1);
        dataTable.addCell(cell11);
        //IFE
        Cell cell12 = new Cell(new Phrase(HTMLHelper.displayField((String)PN.get("14")),tableFont));
        cell12.setHorizontalAlignment(1);
        cell12.setVerticalAlignment(0);
        cell12.setColspan(1);
        dataTable.addCell(cell12);
        //CURP *****No existe el dato en el formato de respuesta de BC
        Cell cell13 = new Cell(new Phrase(" ",tableFont));
        cell13.setHorizontalAlignment(1);
        cell13.setVerticalAlignment(0);
        cell13.setColspan(1);
        dataTable.addCell(cell13);

    	Hashtable temporal = new Hashtable();
        for(int i=1; i<=buro.size();i++){
        	temporal = (Hashtable)buro.get(i);
        	if (temporal.get("RS")!= null){
        		break;
        	}
        }
        //Registro en BC
        Cell cell14 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("RS")).toString()),tableFont));
        cell14.setHorizontalAlignment(1);
        cell14.setVerticalAlignment(0);
        cell14.setColspan(1);
        dataTable.addCell(cell14);
        
        document.add(dataTable);
    }


	private void setDomicilios(Hashtable buro)
    throws BadElementException, DocumentException, Exception{

    int ai[] = {3, 20, 17, 15, 15, 10, 5, 5, 10};
    dataTable = new Table(9);
    dataTable.setWidth(100F);
    dataTable.setSpacing(0F);
    dataTable.setPadding(0.2F);
    dataTable.setWidths(ai);
    dataTable.setCellsFitPage(true);
    Cell cell = new Cell(new Phrase("DOMICILIO(S) REPORTADO(S)",titleFont));
    cell.setHorizontalAlignment(1);
    cell.setVerticalAlignment(0);
    cell.setColspan(9);
    dataTable.addCell(cell);
    Cell cell1 = new Cell(new Phrase("#",labelFont));
    cell1.setHorizontalAlignment(1);
    cell1.setVerticalAlignment(0);
    cell1.setColspan(1);
    dataTable.addCell(cell1);
    Cell cell2 = new Cell(new Phrase("CALLE Y NUMERO",labelFont));
    cell2.setHorizontalAlignment(1);
    cell2.setVerticalAlignment(0);
    cell2.setColspan(1);
    dataTable.addCell(cell2);
    Cell cell3 = new Cell(new Phrase("COLONIA",labelFont));
    cell3.setHorizontalAlignment(1);
    cell3.setVerticalAlignment(0);
    cell3.setColspan(1);
    dataTable.addCell(cell3);
    Cell cell4 = new Cell(new Phrase("DEL/MPIO",labelFont));
    cell4.setHorizontalAlignment(1);
    cell4.setVerticalAlignment(0);
    cell4.setColspan(1);
    dataTable.addCell(cell4);
    Cell cell5 = new Cell(new Phrase("CIUDAD",labelFont));
    cell5.setHorizontalAlignment(1);
    cell5.setVerticalAlignment(0);
    cell5.setColspan(1);
    dataTable.addCell(cell5);
    Cell cell6 = new Cell(new Phrase("EDO",labelFont));
    cell6.setHorizontalAlignment(1);
    cell6.setVerticalAlignment(2);
    cell6.setColspan(1);
    cell6.setGrayFill(3);
    dataTable.addCell(cell6);
    Cell cell7 = new Cell(new Phrase("CP",labelFont));
    cell7.setHorizontalAlignment(1);
    cell7.setVerticalAlignment(0);
    cell7.setColspan(1);
    cell7.setGrayFill(2);
    dataTable.addCell(cell7);        
    Cell cell8 = new Cell(new Phrase("TEL",labelFont));
    cell8.setHorizontalAlignment(1);
    cell8.setVerticalAlignment(0);
    cell8.setColspan(1);
    cell8.setGrayFill(2);
    dataTable.addCell(cell8);
    Cell cell9 = new Cell(new Phrase("REGISTRO EN BC",labelFont));
    cell9.setHorizontalAlignment(1);
    cell9.setVerticalAlignment(0);
    cell9.setColspan(1);
    cell9.setGrayFill(2);
    dataTable.addCell(cell9);        
    
	int contDomicilios = 0;
    for(int i=1; i<=buro.size();i++){
    	Hashtable temporal = (Hashtable)buro.get(i);
    	if (temporal.get("PA")!= null){
    		contDomicilios++;
            //Contador de domicilios
    		Cell cell10 = new Cell(new Phrase(String.valueOf(contDomicilios) ,tableFont));
            cell10.setHorizontalAlignment(1);
            cell10.setVerticalAlignment(0);
            cell10.setColspan(1);
            dataTable.addCell(cell10);

    		//Calle y número
            Cell cell11 = new Cell(new Phrase((String)temporal.get("PA"),tableFont));
    	    cell11.setHorizontalAlignment(0);
    	    cell11.setVerticalAlignment(0);
    	    cell11.setColspan(1);
    	    dataTable.addCell(cell11);
    	    
    		//Colonia
    	    Cell cell12 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("01")),tableFont));
    		cell12.setHorizontalAlignment(0);
    	    cell12.setVerticalAlignment(0);
    	    cell12.setColspan(1);
    	    dataTable.addCell(cell12);
    	    
    		//Delegación o municipio
    	    Cell cell13 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("02")),tableFont));
    		cell13.setHorizontalAlignment(0);
    	    cell13.setVerticalAlignment(0);
    	    cell13.setColspan(1);
    	    dataTable.addCell(cell13);

    		//Ciudad
    	    Cell cell14 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("03")),tableFont));
    		cell14.setHorizontalAlignment(1);
    	    cell14.setVerticalAlignment(0);
    	    cell14.setColspan(1);
    	    dataTable.addCell(cell14);

    		//Estado
    	    Cell cell15 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("04")),tableFont));
    		cell15.setHorizontalAlignment(1);
    	    cell15.setVerticalAlignment(0);
    	    cell15.setColspan(1);
    	    dataTable.addCell(cell15);

    		//Código Postal
    	    Cell cell16 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("05")),tableFont));
    		cell16.setHorizontalAlignment(1);
    	    cell16.setVerticalAlignment(0);
    	    cell16.setColspan(1);
    	    dataTable.addCell(cell16);

    		//Teléfono
    	    Cell cell17 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("07")),tableFont));
    		cell17.setHorizontalAlignment(1);
    	    cell17.setVerticalAlignment(0);
    	    cell17.setColspan(1);
    	    dataTable.addCell(cell17);

    		//Registro en BC
    	    Cell cell18 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("12"))).toString(),tableFont));
    		cell18.setHorizontalAlignment(1);
    	    cell18.setVerticalAlignment(0);
    	    cell18.setColspan(1);
    	    dataTable.addCell(cell18);
       	}
    }

    document.add(dataTable);
	}


	private void setDetalleCreditos(Hashtable buro)
    throws BadElementException, DocumentException, Exception{

	int dc[] = {2, 5, 5, 2, 3, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 8, 5, 18};		
	dataTable = new Table(18);
	dataTable.setWidth(100F);
	dataTable.setSpacing(0F);
	dataTable.setPadding(0.2F);
	dataTable.setWidths(dc);
	/*dataTable.setBorder(0);
	dataTable.setBorderWidthBottom(0.5f);
	dataTable.setBorderWidthLeft(0.5f);
	dataTable.setBorderWidthRight(0.5f);*/
	dataTable.setCellsFitPage(true);

	Table headerDetalle = new Table(18);
	headerDetalle.setWidth(100F);
	headerDetalle.setSpacing(0F);
	headerDetalle.setPadding(0F);
	headerDetalle.setWidths(dc);
	//headerDetalle.setBorder(0);
	headerDetalle.setCellsFitPage(true);
		
	Cell cell0 = new Cell(new Phrase(new Phrase("DETALLE DE LOS CREDITO(S)",titleFont)));
	cell0.setHorizontalAlignment(1);
    cell0.setVerticalAlignment(0);
    cell0.setColspan(18);
    headerDetalle.addCell(cell0);

	Cell head1 = new Cell(new Phrase(new Phrase(" ",labelFont)));
	head1.setHorizontalAlignment(1);
	head1.setVerticalAlignment(0);
	head1.setColspan(4);
	headerDetalle.addCell(head1);

	Cell head2 = new Cell(new Phrase(new Phrase("FECHAS",labelFont)));
	head2.setHorizontalAlignment(1);
	head2.setVerticalAlignment(0);
	head2.setColspan(6);
	headerDetalle.addCell(head2);    
    
	Cell head3 = new Cell(new Phrase(new Phrase("IMPORTES",labelFont)));
	head3.setHorizontalAlignment(1);
	head3.setVerticalAlignment(0);
	head3.setColspan(5);
	headerDetalle.addCell(head3);

	Cell head4 = new Cell(new Phrase(new Phrase(" ",labelFont)));
	head4.setHorizontalAlignment(1);
	head4.setVerticalAlignment(0);
	head4.setColspan(3);
	headerDetalle.addCell(head4);
	document.add(headerDetalle);
	
	Cell cell1 = new Cell(new Phrase("#",labelFont));
    cell1.setHorizontalAlignment(1);
    cell1.setVerticalAlignment(0);
    cell1.setColspan(1);
    dataTable.addCell(cell1);
    Cell cell2 = new Cell(new Phrase("TIPO DE CREDITO",labelFont));
    cell2.setHorizontalAlignment(1);
    cell2.setVerticalAlignment(0);
    cell2.setColspan(1);
    dataTable.addCell(cell2);
    Cell cell3 = new Cell(new Phrase("OTORGA NO CUENTA",labelFont));
    cell3.setHorizontalAlignment(1);
    cell3.setVerticalAlignment(0);
    cell3.setColspan(1);
    dataTable.addCell(cell3);
    Cell cell4 = new Cell(new Phrase("MO-NE-DA",labelFont));
    cell4.setHorizontalAlignment(1);
    cell4.setVerticalAlignment(0);
    cell4.setColspan(1);
    dataTable.addCell(cell4);
    
    Cell cell5 = new Cell(new Phrase("ACTUALIZADO",labelFont));
    cell5.setHorizontalAlignment(1);
    cell5.setVerticalAlignment(0);
    cell5.setColspan(1);
    dataTable.addCell(cell5);
    
    Cell cell6 = new Cell(new Phrase("APER-TURA",labelFont));
    cell6.setHorizontalAlignment(1);
    cell6.setVerticalAlignment(0);
    cell6.setColspan(1);
    dataTable.addCell(cell6);
    
    Cell cell7 = new Cell(new Phrase("ULTIMO PAGO",labelFont));
    cell7.setHorizontalAlignment(1);
    cell7.setVerticalAlignment(0);
    cell7.setColspan(1);
    dataTable.addCell(cell7);
    
    Cell cell8 = new Cell(new Phrase("ULTIMA COMPRA",labelFont));
    cell8.setHorizontalAlignment(1);
    cell8.setVerticalAlignment(0);
    cell8.setColspan(1);
    dataTable.addCell(cell8);
    
    Cell cell9 = new Cell(new Phrase("CIERRE",labelFont));
    cell9.setHorizontalAlignment(1);
    cell9.setVerticalAlignment(0);
    cell9.setColspan(1);
    dataTable.addCell(cell9);
    
    Cell cell10 = new Cell(new Phrase("ULT SDO",labelFont));
    cell10.setHorizontalAlignment(1);
    cell10.setVerticalAlignment(0);
    cell10.setColspan(1);
    dataTable.addCell(cell10);    
    
    Cell cell11 = new Cell(new Phrase("LIMITE CREDITO",labelFont));
    cell11.setHorizontalAlignment(1);
    cell11.setVerticalAlignment(0);
    cell11.setColspan(1);
    dataTable.addCell(cell11);
    
    Cell cell12 = new Cell(new Phrase("CREDITO MAXIMO",labelFont));
    cell12.setHorizontalAlignment(1);
    cell12.setVerticalAlignment(0);
    cell12.setColspan(1);
    dataTable.addCell(cell12);
    
    Cell cell13 = new Cell(new Phrase("SALDO ACTUAL",labelFont));
    cell13.setHorizontalAlignment(1);
    cell13.setVerticalAlignment(0);
    cell13.setColspan(1);
    dataTable.addCell(cell13);

    Cell cell14 = new Cell(new Phrase("MONTO",labelFont));
    cell14.setHorizontalAlignment(1);
    cell14.setVerticalAlignment(0);
    cell14.setColspan(1);
    dataTable.addCell(cell14);
    
    Cell cell15 = new Cell(new Phrase("MONTO A PAGAR",labelFont));
    cell15.setHorizontalAlignment(1);
    cell15.setVerticalAlignment(0);
    cell15.setColspan(1);
    dataTable.addCell(cell15);    
   
    Cell cell16 = new Cell(new Phrase("FORMA DE PAGO EN ACTUALIZACION",labelFont));
    cell16.setHorizontalAlignment(1);
    cell16.setVerticalAlignment(0);
    cell16.setColspan(1);
    dataTable.addCell(cell16);        

    Cell cell17 = new Cell(new Phrase("MOP FECHA IMPORTE",labelFont));
    cell17.setHorizontalAlignment(1);
    cell17.setVerticalAlignment(0);
    cell17.setColspan(1);
    dataTable.addCell(cell17);

    Cell cell18 = new Cell(new Phrase("HISTORICO DE PAGOS",labelFont));
    cell18.setHorizontalAlignment(1);
    cell18.setVerticalAlignment(0);
    cell18.setColspan(1);
    dataTable.addCell(cell18);        

    Hashtable<Integer,Integer> temp = new Hashtable<Integer,Integer>();
	String tpoCuenta = "";
    int contCreditos = 0;
    for(int i=1; i<=buro.size();i++){
    	Hashtable temporal = (Hashtable)buro.get(i);
    	if (temporal.get("TL")!= null){
    		contCreditos++;
            //Contador de Cuentas o créditos
    		Cell cell19 = new Cell(new Phrase(String.valueOf(contCreditos) ,tableFont));
            cell19.setHorizontalAlignment(1);
            cell19.setVerticalAlignment(0);
            cell19.setColspan(1);
            dataTable.addCell(cell19);
    		//Resumen de cuentas
            tpoCuenta = (String)temporal.get("26");
    		temp = (Hashtable)resumen.get(tpoCuenta);
			Integer ctasAbiertas = 0;
			Integer limiteAbiertas = 0;
			Integer maxAbiertas = 0;
			Integer sdoActual = 0;
			Integer sdoVencido = 0;
			Integer pagoRealizar = 0;
			Integer ctasCerradas = 0;
			Integer limiteCerradas = 0;
			Integer maxCerradas = 0;
			Integer sdoCerradas = 0;
			Integer montoCerradas = 0;
    		if (temp != null){
    			ctasAbiertas = (Integer)temp.get(0);
    			limiteAbiertas = (Integer)temp.get(1);
    			maxAbiertas = (Integer)temp.get(2);
    			sdoActual = (Integer)temp.get(3);
    			sdoVencido = (Integer)temp.get(4);
    			pagoRealizar = (Integer)temp.get(5);
    			ctasCerradas = (Integer)temp.get(6);
    			limiteCerradas = (Integer)temp.get(7);
    			maxCerradas = (Integer)temp.get(8);
    			sdoCerradas = (Integer)temp.get(9);
    			montoCerradas = (Integer)temp.get(10);
    		}else{
    			temp = new Hashtable<Integer,Integer>();
    			}
    			String eliminaSigno = (String)temporal.get("22");
				if (eliminaSigno != null)
					if (eliminaSigno.length()>1){
						eliminaSigno = eliminaSigno.substring(0, eliminaSigno.length() - 1);
					}
				else
					eliminaSigno = "0";
					
				Integer saldo = GetBuroHelper.stringToInteger(eliminaSigno);

    			if (tpoCuenta.equals("UR")){
    				temp.put(0, ctasAbiertas + 1);
	    			temp.put(1, limiteAbiertas + GetBuroHelper.stringToInteger((String)temporal.get("23")));
	    			temp.put(2, maxAbiertas + GetBuroHelper.stringToInteger((String)temporal.get("21")));
	    			temp.put(3, sdoActual + saldo);
	    			temp.put(4, sdoVencido + GetBuroHelper.stringToInteger((String)temporal.get("24")));
	    			temp.put(5, pagoRealizar + GetBuroHelper.stringToInteger((String)temporal.get("12")));
	    			temp.put(6, 0);
	    			temp.put(7, 0);
	    			temp.put(8, 0);
	    			temp.put(9, 0);
	    			temp.put(10, 0);
   			}else{
    				if (saldo != 0){
    					temp.put(0, ctasAbiertas + 1);
    	    			temp.put(1, limiteAbiertas + GetBuroHelper.stringToInteger((String)temporal.get("23")));
    	    			temp.put(2, maxAbiertas + GetBuroHelper.stringToInteger((String)temporal.get("21")));
    	    			temp.put(3, sdoActual + saldo);
    	    			temp.put(4, sdoVencido + GetBuroHelper.stringToInteger((String)temporal.get("24")));
    	    			temp.put(5, pagoRealizar + GetBuroHelper.stringToInteger((String)temporal.get("12")));
    					temp.put(6, ctasCerradas);
    	    			temp.put(7, limiteCerradas);
    	    			temp.put(8, maxCerradas);
    	    			temp.put(9, sdoCerradas);
    	    			temp.put(10, montoCerradas);

    				}else{
    					temp.put(0, ctasAbiertas);
    	    			temp.put(1, limiteAbiertas);
    	    			temp.put(2, maxAbiertas);
    	    			temp.put(3, sdoActual);
    	    			temp.put(4, sdoVencido);
    	    			temp.put(5, pagoRealizar);
    					temp.put(6, ctasCerradas + 1);
    	    			temp.put(7, limiteCerradas + GetBuroHelper.stringToInteger((String)temporal.get("23")));
    	    			temp.put(8, maxCerradas + GetBuroHelper.stringToInteger((String)temporal.get("21")));
    	    			temp.put(9, sdoCerradas + saldo);
    	    			temp.put(10, montoCerradas + GetBuroHelper.stringToInteger((String)temporal.get("12")));
    				}
    			}
    		resumen.put(tpoCuenta, temp);
    		//Resumen de cuentas            
            //Tipo de cuenta
            String contrato = GetBuroHelper.getTipoContrato(HTMLHelper.displayField((String)temporal.get("07")));
            contrato += " " + GetBuroHelper.getTipoCuenta(HTMLHelper.displayField((String)temporal.get("06")));
            contrato += " " + GetBuroHelper.getResponsabilidad(HTMLHelper.displayField((String)temporal.get("05")));
            Cell cell20 = new Cell(new Phrase(contrato,tableFont));
    	    cell20.setHorizontalAlignment(1);
    	    cell20.setVerticalAlignment(0);
    	    cell20.setColspan(1);
    	    dataTable.addCell(cell20);
    		//Otorgante de cuenta
    	    Cell cell21 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("02")),tableFont));
    		cell21.setHorizontalAlignment(1);
    	    cell21.setVerticalAlignment(0);
    	    cell21.setColspan(1);
    	    dataTable.addCell(cell21);
    		//Moneda
    	    Cell cell22 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("08")),tableFont));
    		cell22.setHorizontalAlignment(1);
    	    cell22.setVerticalAlignment(0);
    	    cell22.setColspan(1);
    	    dataTable.addCell(cell22);
    	    //Actualizado
    	    Cell cell23 = new Cell(new Phrase(GetBuroHelper.monthConvert(HTMLHelper.displayField((String)temporal.get("TL")).toString()),tableFont));
    		cell23.setHorizontalAlignment(1);
    	    cell23.setColspan(1);
    	    dataTable.addCell(cell23);
    		//Apertura
    	    Cell cell24 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("13")).toString()),tableFont));
    		cell24.setHorizontalAlignment(1);
    	    cell24.setColspan(1);
            dataTable.addCell(cell24);
    		//Ultimo pago
    	    Cell cell25 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("14")).toString()),tableFont));
    		cell25.setHorizontalAlignment(1);
    	    cell25.setColspan(1);
            /*cell16.setBorderWidth(0);
            cell16.setBorderWidthRight(0.5f);*/
            dataTable.addCell(cell25);
    		//Ultima compra
    	    Cell cell26 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("15")).toString()),tableFont));
    		cell26.setHorizontalAlignment(1);
    	    cell26.setColspan(1);
            dataTable.addCell(cell26);
    		//Cierre
    	    Cell cell27 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("16")).toString()),tableFont));
    		cell27.setHorizontalAlignment(1);
    	    cell27.setColspan(1);
            dataTable.addCell(cell27);
    		//Ultima vez saldo en 0
    	    Cell cell28 = new Cell(new Phrase(GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("19")).toString()),tableFont));
    		cell28.setHorizontalAlignment(1);
    	    cell28.setColspan(1);
            dataTable.addCell(cell28);    	    
    		//Límite de crédito
    	    Cell cell29 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("23")),tableFont));
    		cell29.setHorizontalAlignment(1);
    	    cell29.setVerticalAlignment(0);
    	    cell29.setColspan(1);
    	    dataTable.addCell(cell29);    	    
    		//Crédito máximo
    	    Cell cell30 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("21")),tableFont));
    		cell30.setHorizontalAlignment(1);
    	    cell30.setVerticalAlignment(0);
    	    cell30.setColspan(1);
    	    dataTable.addCell(cell30);
    	    
    		//Saldo actual
    	    Cell cell31 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("22")),tableFont));
    	    cell31.setHorizontalAlignment(1);
    	    cell31.setVerticalAlignment(0);
    	    cell31.setColspan(1);
    	    dataTable.addCell(cell31);
    	    
    		//Monto
    	    Cell cell32 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("24")),tableFont));
    	    cell32.setHorizontalAlignment(1);
    	    cell32.setVerticalAlignment(0);
    	    cell32.setColspan(1);
    	    dataTable.addCell(cell32);
    	    
    	    //Monto a pagar
    	    String monto = HTMLHelper.displayField((String)temporal.get("12")) + "\n";
    	    monto += GetBuroHelper.getFrecuencia(HTMLHelper.displayField((String)temporal.get("11"))) + "\n";
    	    monto += HTMLHelper.displayField((String)temporal.get("10"));
    	    Cell cell33 = new Cell(new Phrase(monto ,tableFont));
    	    cell33.setHorizontalAlignment(1);
    	    cell33.setVerticalAlignment(0);
    	    cell33.setColspan(1);
    	    dataTable.addCell(cell33);    	    

    	    //Forma de pago en fecha actualización
    	    Cell cell34 = new Cell(new Phrase(HTMLHelper.displayField((String)temporal.get("26")) + "=" + GetBuroHelper.getMOP(HTMLHelper.displayField((String)temporal.get("26"))) ,tableFont));
    	    cell34.setHorizontalAlignment(1);
    	    cell34.setVerticalAlignment(0);
    	    cell34.setColspan(1);
    	    dataTable.addCell(cell34);    	    

    	    //Máxima morosidad
    	    String morosidad = HTMLHelper.displayField((String)temporal.get("38")) + "\n";
    	    morosidad += GetBuroHelper.formatDate(HTMLHelper.displayField((String)temporal.get("37"))) + "\n";
    	    morosidad += HTMLHelper.displayField((String)temporal.get("36"));
    	    Cell cell35 = new Cell(new Phrase(morosidad ,tableFont));
    	    cell35.setHorizontalAlignment(1);
    	    cell35.setVerticalAlignment(0);
    	    cell35.setColspan(1);
    	    dataTable.addCell(cell35);    	    
    	    int hp[] = {6, 6, 6};		
    		Table table = new Table(3);
    		table.setWidth(100F);
    		table.setSpacing(0F);
    		table.setPadding(0F);
    		table.setWidths(hp);
    		table.setCellsFitPage(true);
    		
    		String actualizado = (String)temporal.get("TL");
    		actualizado = actualizado.substring(4);
    		int añoActual = Convertidor.stringToInt(actualizado);

    		Cell cell36 = new Cell(new Phrase(actualizado ,tableFont));
    	    cell36.setHorizontalAlignment(1);
     	    cell36.setColspan(1);
    	    table.addCell(cell36);
    	    añoActual = añoActual - 1;

    	    Cell cell37 = new Cell(new Phrase(String.valueOf(añoActual) ,tableFont));
    	    cell37.setHorizontalAlignment(1);
     	    cell37.setColspan(1);
    	    table.addCell(cell37);
    	    añoActual = añoActual - 1;

    	    Cell cell38 = new Cell(new Phrase(String.valueOf(añoActual) ,tableFont));
    	    cell38.setHorizontalAlignment(1);
     	    cell38.setColspan(1);
    	    table.addCell(cell38);    	    

    	    Cell cell39 = new Cell(new Phrase("DNOSAJJMAMFE" ,smallFont));
    	    cell39.setHorizontalAlignment(2);
     	    cell39.setColspan(1);
    	    table.addCell(cell39);    	    
    	    
    		Cell cell40 = new Cell(new Phrase("DNOSAJJMAMFE" ,smallFont));
    	    cell40.setHorizontalAlignment(0);
     	    cell40.setColspan(1);
    	    table.addCell(cell40);    	    

    		Cell cell41 = new Cell(new Phrase("DNOSAJJMAMFE" ,smallFont));
    	    cell41.setHorizontalAlignment(0);
     	    cell41.setColspan(1);
    	    table.addCell(cell41);    	    
    	    
     	    actualizado = (String)temporal.get("TL");
     	    int mes = Convertidor.stringToInt(actualizado.substring(2,4));

     	    String historico = (String)temporal.get("27");
     	    if(historico!=null)
     	    	historico = FormatUtil.completaCadena(historico, '-', 24, "R");
 	    	
 	    	if (historico == null)
     	    	historico = "------------------------------";

 	    	String añouno = "";
 	    	if (mes == 1)
     	    	añouno = "------------";
 	    	else
 	    		añouno = historico.substring(0, mes - 1);

 	    	Cell cell42 = new Cell(new Phrase(añouno ,numberFont));
 	    	cell42.setHorizontalAlignment(2);
     	    cell42.setColspan(1);
     	    table.addCell(cell42);
     	    if (mes != 1)
     	    	historico = historico.substring(mes - 1);
     	    
     	    String segundoAño = ""; 
    	    if (historico.length()>=12){
    	    	segundoAño = historico.substring(0, 12);
    	    	historico = historico.substring(12);
    	    }else{
    	    	segundoAño = historico;
    	    	historico = "";
    	    }
    	    
    	    Cell cell43 = new Cell(new Phrase(segundoAño ,numberFont));
    	    cell43.setHorizontalAlignment(0);
     	    cell43.setColspan(1);
    	    table.addCell(cell43);
    	    
    	    if (historico.length()<=0)
    	    	historico = "------------";
     	    
    	    Cell cell44 = new Cell(new Phrase(historico ,numberFont));
    	    cell44.setHorizontalAlignment(0);
     	    cell44.setColspan(1);
    	    table.addCell(cell44);

    	    Cell cell45 = new Cell(table);
       	    cell45.setHorizontalAlignment(1);
     	    cell45.setColspan(1);
    	    dataTable.addCell(cell45);
    	}
    }
    document.add(dataTable);
	}
	
	private void setResumen() throws BadElementException, DocumentException {
    	try{
     		
    	int dr[] = {5, 5, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9};		
    	dataTable = new Table(12);
    	dataTable.setWidth(100F);
    	dataTable.setSpacing(0F);
    	dataTable.setPadding(0.2F);
    	dataTable.setWidths(dr);
   		dataTable.setCellsFitPage(true);

   		Cell cell1 = new Cell(new Phrase("RESUMEN DE CREDITOS (M.N.)", titleFont));
        cell1.setHorizontalAlignment(1);
        cell1.setColspan(12);
        dataTable.addCell(cell1);
        Cell cell2 = new Cell(new Phrase(" ", labelFont));
        cell2.setHorizontalAlignment(1);
        cell2.setColspan(1);
        dataTable.addCell(cell2);
        Cell cell3 = new Cell(new Paragraph("CUENTAS ABIERTAS", labelFont));
        cell3.setHorizontalAlignment(1);
        cell3.setColspan(6);
        dataTable.addCell(cell3);
        Cell cell4 = new Cell(new Paragraph("CUENTAS CERRADAS", labelFont));
        cell4.setHorizontalAlignment(1);
        cell4.setColspan(5);
        dataTable.addCell(cell4);
        Cell cell5 = new Cell(new Paragraph("MOP", labelFont));
        cell5.setHorizontalAlignment(1);
        cell5.setColspan(1);
        dataTable.addCell(cell5);
        Cell cell6 = new Cell(new Paragraph("CUENTAS ABIERTAS", labelFont));
        cell6.setHorizontalAlignment(1);
        cell6.setColspan(1);
        dataTable.addCell(cell6);
        Cell cell7 = new Cell(new Paragraph("LIMITE ABIERTAS", labelFont));
        cell7.setHorizontalAlignment(1);
        cell7.setColspan(1);
        dataTable.addCell(cell7);        
        Cell cell8 = new Cell(new Paragraph("MAXIMO ABIERTAS", labelFont));
        cell8.setHorizontalAlignment(1);
        cell8.setColspan(1);
        dataTable.addCell(cell8);        
        Cell cell9 = new Cell(new Paragraph("SALDO ACTUAL ABIERTAS", labelFont));
        cell9.setHorizontalAlignment(1);
        cell9.setColspan(1);
        dataTable.addCell(cell9);        
        Cell cell10 = new Cell(new Paragraph("SALDO VENCIDO ABIERTAS", labelFont));
        cell10.setHorizontalAlignment(1);
        cell10.setColspan(1);
        dataTable.addCell(cell10);        
        Cell cell11 = new Cell(new Paragraph("PAGO A REALIZAR", labelFont));
        cell11.setHorizontalAlignment(1);
        cell11.setColspan(1);
        dataTable.addCell(cell11);        
        Cell cell12 = new Cell(new Paragraph("CUENTAS CERRADAS", labelFont));
        cell12.setHorizontalAlignment(1);
        cell12.setColspan(1);
        dataTable.addCell(cell12);
        Cell cell13 = new Cell(new Paragraph("LIMITE CERRADAS", labelFont));
        cell13.setHorizontalAlignment(1);
        cell13.setColspan(1);
        dataTable.addCell(cell13);
        Cell cell14 = new Cell(new Paragraph("MAXIMO CERRADAS", labelFont));
        cell14.setHorizontalAlignment(1);
        cell14.setColspan(1);
        dataTable.addCell(cell14);
        Cell cell15 = new Cell(new Paragraph("SALDO ACTUAL CERRADAS", labelFont));
        cell15.setHorizontalAlignment(1);
        cell15.setColspan(1);
        dataTable.addCell(cell15);
        Cell cell16 = new Cell(new Paragraph("MONTO CERRADAS", labelFont));
        cell16.setHorizontalAlignment(1);
        cell16.setColspan(1);
        dataTable.addCell(cell16);
        
        for (int i=0;i<=11;i++){
        	String MOP = GetBuroHelper.getOrderMOP(i);
        	Hashtable res = new Hashtable();
        	if(resumen.containsKey(MOP))
            	res = (Hashtable)resumen.get(MOP);
        	else res = null;

        	if (res!=null){
                Cell cell17 = new Cell(new Paragraph(MOP, tableFont));
                cell17.setHorizontalAlignment(1);
                cell17.setColspan(1);
                dataTable.addCell(cell17);
                Cell cell18 = new Cell(new Paragraph(res.get(0).toString(), tableFont));
                cell18.setHorizontalAlignment(0);
                cell18.setColspan(1);
                dataTable.addCell(cell18);
                /*String formateado = res.get(1).toString();
                DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO);
                formateado = String.format(ClientesConstants.FORMATO_MONTO, 0);*/
                Cell cell19 = new Cell(new Paragraph(res.get(1).toString(), tableFont));
                cell19.setHorizontalAlignment(01);
                cell19.setColspan(1);
                dataTable.addCell(cell19);
                Cell cell20 = new Cell(new Paragraph(res.get(2).toString(), tableFont));
                cell20.setHorizontalAlignment(0);
                cell20.setColspan(1);
                dataTable.addCell(cell20);
                Cell cell21 = new Cell(new Paragraph(res.get(3).toString(), tableFont));
                cell21.setHorizontalAlignment(0);
                cell21.setColspan(1);
                dataTable.addCell(cell21);        
                Cell cell22 = new Cell(new Paragraph(res.get(4).toString(), tableFont));
                cell22.setHorizontalAlignment(0);
                cell22.setColspan(1);
                dataTable.addCell(cell22);        
                Cell cell23 = new Cell(new Paragraph(res.get(5).toString(), tableFont));
                cell23.setHorizontalAlignment(0);
                cell23.setColspan(1);
                dataTable.addCell(cell23);        
                Cell cell24 = new Cell(new Paragraph(res.get(6).toString(), tableFont));
                cell24.setHorizontalAlignment(0);
                cell24.setColspan(1);
                dataTable.addCell(cell24);        
                Cell cell25 = new Cell(new Paragraph(res.get(7).toString(), tableFont));
                cell25.setHorizontalAlignment(0);
                cell25.setColspan(1);
                dataTable.addCell(cell25);        
                Cell cell26 = new Cell(new Paragraph(res.get(8).toString(), tableFont));
                cell26.setHorizontalAlignment(0);
                cell26.setColspan(1);
                dataTable.addCell(cell26);
                Cell cell27 = new Cell(new Paragraph(res.get(9).toString(), tableFont));
                cell27.setHorizontalAlignment(0);
                cell27.setColspan(1);
                dataTable.addCell(cell27);
                Cell cell28 = new Cell(new Paragraph(res.get(10).toString(), tableFont));
                cell28.setHorizontalAlignment(0);
                cell28.setColspan(1);
                dataTable.addCell(cell28);
                resumen.remove(MOP);
        	}
        }
        
        document.add(dataTable);
        
    	} catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException();
        }

    }

	private void setDetalleConsultas(Hashtable buro) throws BadElementException, DocumentException {
    	try{
     		
    	int dc[] = {18, 18, 18, 18, 18, 10};		
    	dataTable = new Table(6);
    	dataTable.setWidth(100F);
    	dataTable.setSpacing(0F);
    	dataTable.setPadding(0.2F);
    	dataTable.setWidths(dc);
   		dataTable.setCellsFitPage(true);

   		Cell cell1 = new Cell(new Phrase("DETALLE DE LAS CONSULTAS", titleFont));
        cell1.setHorizontalAlignment(1);
        cell1.setColspan(6);
        dataTable.addCell(cell1);
        Cell cell3 = new Cell(new Paragraph("OTORGANTE", labelFont));
        cell3.setHorizontalAlignment(1);
        cell3.setColspan(1);
        dataTable.addCell(cell3);
        Cell cell4 = new Cell(new Paragraph("FECHA DE LA CONSULTA", labelFont));
        cell4.setHorizontalAlignment(1);
        cell4.setColspan(1);
        dataTable.addCell(cell4);
        Cell cell5 = new Cell(new Paragraph("RESPONSABILIDAD", labelFont));
        cell5.setHorizontalAlignment(1);
        cell5.setColspan(1);
        dataTable.addCell(cell5);
        Cell cell6 = new Cell(new Paragraph("TIPO DE CONTRATO", labelFont));
        cell6.setHorizontalAlignment(1);
        cell6.setColspan(1);
        dataTable.addCell(cell6);
        Cell cell7 = new Cell(new Paragraph("IMPORTE", labelFont));
        cell7.setHorizontalAlignment(1);
        cell7.setColspan(1);
        dataTable.addCell(cell7);        
        Cell cell8 = new Cell(new Paragraph("TIPO DE MONEDA", labelFont));
        cell8.setHorizontalAlignment(1);
        cell8.setColspan(1);
        dataTable.addCell(cell8);        
        
        for(int i=1; i<=buro.size();i++){
        	Hashtable temporal = (Hashtable)buro.get(i);
        	if (temporal.get("IQ")!= null){
                Cell cell9 = new Cell(new Paragraph(HTMLHelper.displayField((String)temporal.get("02")), tableFont));
                cell9.setHorizontalAlignment(0);
                cell9.setColspan(1);
                dataTable.addCell(cell9);
                String fechaCons = (String)temporal.get("IQ");
                fechaCons = fechaCons.substring(0, 2) + "/" + fechaCons.substring(2, 4) + "/" + fechaCons.substring(4);
                Cell cell10 = new Cell(new Paragraph(fechaCons, tableFont));
                cell10.setHorizontalAlignment(0);
                cell10.setColspan(1);
                dataTable.addCell(cell10);
                Cell cell11 = new Cell(new Paragraph(GetBuroHelper.getResponsabilidad(HTMLHelper.displayField((String)temporal.get("07"))), tableFont));
                cell11.setHorizontalAlignment(0);
                cell11.setColspan(1);
                dataTable.addCell(cell11);
                Cell cell12 = new Cell(new Paragraph(GetBuroHelper.getTipoContrato(HTMLHelper.displayField((String)temporal.get("04"))), tableFont));
                cell12.setHorizontalAlignment(0);
                cell12.setColspan(1);
                dataTable.addCell(cell12);
                Cell cell13 = new Cell(new Paragraph(HTMLHelper.displayField((String)temporal.get("06")), tableFont));
                cell13.setHorizontalAlignment(0);
                cell13.setColspan(1);
                dataTable.addCell(cell13);        
                Cell cell14 = new Cell(new Paragraph(HTMLHelper.displayField((String)temporal.get("05")), tableFont));
                cell14.setHorizontalAlignment(0);
                cell14.setColspan(1);
                dataTable.addCell(cell14);

        	}
        }
        
        document.add(dataTable);
        
    	} catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException();
        }

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
	Hashtable resumen = new Hashtable();
	}
	

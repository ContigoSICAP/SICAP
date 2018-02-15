package com.sicap.clientes.helpers;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sicap.clientes.dao.BitacoraEstatusDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.lowagie.text.BadElementException;
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
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;



public class GeneraPDFDatosComunalHelper{
	
	public void pdfWriter(HttpServletRequest request, HttpServletResponse response, ClienteVO cliente, int id,int idReferencia){
				
		    SolicitudVO solicitud = new SolicitudVO();
		    CreditoVO circuloCredito = new CreditoVO();
			DecisionComiteVO decisionComite = new DecisionComiteVO();
			ScoringVO score = new ScoringVO();
			TreeMap catEstatus = null;
			CreditoVO buroCredito = new CreditoVO();
							
		    Logger.debug("Cliente: "+cliente.toString());
		    Logger.debug("Solicitud :"+id);
		   
            SolicitudVO[] solicitudes = cliente.solicitudes;
		
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
			//left, right, up, down
			document.setMargins(10, 10, 40, 10);
			document.open();
			
			if(cliente != null){				
				
				// 
								
				int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, id);
				
				Logger.debug("Indice de Solicitud es:"+indiceSolicitud);
								 
				// Obteniendo Solicitud
				if ( cliente.solicitudes!=null && cliente.solicitudes.length>0 )
					solicitud = cliente.solicitudes[indiceSolicitud];
				Logger.debug("Solicitud= "+solicitud.toString());
				Logger.debug("Fecha y hora de alta SOLICITUD: "+solicitud.fechaCaptura);
								
				// Obteniendo Circulo de Crédito
				if( solicitudes[indiceSolicitud].circuloCredito!=null )
					circuloCredito =solicitudes[indiceSolicitud].circuloCredito;
				 							
				if( solicitudes[indiceSolicitud].buroCredito!=null ){
					buroCredito = solicitudes[indiceSolicitud].buroCredito;
				}
											
				// Obteniendo Score
				if ( cliente.solicitudes!=null && id>0 ){
					solicitud = cliente.solicitudes[indiceSolicitud];
					catEstatus = CatalogoHelper.getCatalogoEstatus(solicitud.estatus);
					if ( solicitud.scoring!=null )
						score = solicitud.scoring;
										 					   
					   // Obteniendo Decision Comite					   
					   if(solicitud.decisionComite != null)
						   decisionComite = solicitud.decisionComite;					   					
	              }
								     
			generaDocumento(indiceSolicitud, catEstatus, cliente, solicitud, circuloCredito, score, decisionComite, buroCredito);
				  
			}
			
           document.close();
        } catch (Exception exception) {
        	Logger.debug("Excepcion en main pdfWriter");
        	exception.printStackTrace();
        }
    }
	

	// CAMBIO private void generaDocumento(String referencia, String nombreCompleto, TablaAmortizacionVO[] tabla, double montoSeguro, boolean isGrupal)
	      private void generaDocumento(int indiceSolicitud,TreeMap catEstatus, ClienteVO cliente,  SolicitudVO solicitud, CreditoVO circuloCredito,  ScoringVO score,  DecisionComiteVO decisionComite, CreditoVO buroCredito)
	           throws BadElementException, DocumentException, Exception{

	    	  TreeMap catOperaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OPERACIONES);
	    	  TreeMap catEjecutivosCredito = CatalogoHelper.getCatalogoEjecutivos(cliente.idSucursal);	    	 	    	  
	    	  TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
	    	  TreeMap catFrecuenciaPago = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FRECUENCIA_PAGO);
	    	  TreeMap catTasas = CatalogoHelper.getCatalogoTasas(ClientesConstants.CAT_TASAS_CONSUMO);	    	  
	    	  TreeMap catDecision   = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DECISION_COMITE);
	    	  BitacoraEstatusVO bitacoraEstatusVOAnalisisCredito = new BitacoraEstatusVO();
	    	  BitacoraEstatusVO bitacoraEstatusVOCapturista = new BitacoraEstatusVO();
			  BitacoraEstatusDAO bitacoraEstatusDAO = new BitacoraEstatusDAO();
					    	  	    	  
	    	  String Producto = HTMLHelper.getDescripcion(catOperaciones, solicitud.tipoOperacion);
	    	  Logger.debug("Producto:"+Producto);
	    	    	  
	    	  String analista = "";
	    	  bitacoraEstatusVOAnalisisCredito = bitacoraEstatusDAO.getUltimoRegistroInsertadoAnalisisCredito(cliente.idCliente, solicitud.idSolicitud, 1);
	    	  if(bitacoraEstatusVOAnalisisCredito!=null)
	    		  analista = bitacoraEstatusVOAnalisisCredito.usuario;
	    	  			  	
	    	  String capturista = "";
	    	  bitacoraEstatusVOCapturista = bitacoraEstatusDAO.getPrimerRegistroInsertadoCapturista(cliente.idCliente, solicitud.idSolicitud, 0, 1);
	    	  if(bitacoraEstatusVOCapturista!=null)
	    		  capturista = bitacoraEstatusVOCapturista.usuario;
	    	  
	    	  
	    	  
		int ai[] = {50, 50};
        PdfPTable dataTabla = new PdfPTable(2);
        dataTabla.setWidthPercentage(100F);
        dataTabla.setWidths(ai);
      
        CatalogoDAO paramDao = new CatalogoDAO();
    	ParametroVO paramVO = new ParametroVO();
    	paramVO = paramDao.getParametro("RUTA_IMAGEN_REPORTE");
    	Image LogoCF = Image.getInstance(paramVO.valor);
    	 LogoCF.scalePercent(50);
    	   	  	
//  CREAR TABLA DEL LOGO
    //float[] widths = {0.3f, 0.7f};
	    PdfPTable logo = new PdfPTable(1);
	    //header.setWidthPercentage(0.5F);
	    
	Image image = Image.getInstance(paramVO.valor);
	image.scalePercent(100);
	image.setAlignment(Image.MIDDLE);
	 logo.addCell(new PdfPCell(image));	
	 logo.setSpacingAfter(15f);
	document.add(image);
   	
   	
   	    // ********    CREAR TABLA DEL HEADER "HOJA DE AYUDA COMUNAL"
	
   	    PdfPTable header = new PdfPTable(1);
   	    //header.setWidthPercentage(0.5F);
		//Image image = Image.getInstance(paramVO.valor);
		header.getDefaultCell().setBorderWidth(0.0f);
		header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		header.getDefaultCell().setColspan(1);
		//header.addCell(new PdfPCell(image, true));
		String nombreCompleto = HTMLHelper.displayField(cliente.nombre)+" "+HTMLHelper.displayField(cliente.aPaterno)+" "+HTMLHelper.displayField(cliente.aMaterno);
		header.addCell(new Paragraph(nombreCompleto, FontFactory.getFont(FontFactory.HELVETICA,14, Font.BOLDITALIC)));
		header.setSpacingAfter(15f);
		document.add(header);
		
				
		// *******         CREAR TABLA PARA DATOS "SOLICITUD DE CREDITO"     15 DE WIDTH
		 float[] widthsolicitud = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
		 PdfPTable iniciodatos = new PdfPTable(widthsolicitud);
		 iniciodatos.setWidthPercentage(100F);
		 
		 
		 
		 Paragraph numerocliente = new Paragraph("Cliente: "+HTMLHelper.displayField(cliente.idCliente), FontFactory.getFont(FontFactory.HELVETICA,12, Font.BOLDITALIC));
	     PdfPCell cell111 = new PdfPCell(numerocliente);
	     cell111.setHorizontalAlignment(0);
	     cell111.setColspan(5);
	     cell111.setBorderWidth(0.0f);
	     cell111.setPadding(10f);
	     iniciodatos.addCell(cell111);
	     
	     String fechahoraCliente = "";
	     if(cliente.fechaCaptura!=null)
	    	 fechahoraCliente = String.valueOf(cliente.fechaCaptura);
	     
	     Paragraph fechahoraalta = new Paragraph("Fecha y Hora de Alta (Cliente): "+fechahoraCliente, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell91 = new PdfPCell(fechahoraalta);
	     cell91.setHorizontalAlignment(0);
	     cell91.setColspan(10);
	     cell91.setBorderWidth(0.0f);
	     cell91.setPadding(10f);
		 iniciodatos.addCell(cell91);
	     
		 
		 Paragraph nombrecliente = new Paragraph("Nombre: "+HTMLHelper.displayField(nombreCompleto), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1 = new PdfPCell(nombrecliente);
		 cell1.setHorizontalAlignment(0);
		 cell1.setColspan(15);
		 cell1.setBorderWidth(0.0f);
		 cell1.setPadding(10f);
		 iniciodatos.addCell(cell1);
		 
				 
		 Paragraph sol = new Paragraph("Solicitud: ", FontFactory.getFont(FontFactory.HELVETICA,12, Font.BOLDITALIC));
	     PdfPCell cell250 = new PdfPCell(sol);
	     cell250.setHorizontalAlignment(0);
	     cell250.setColspan(15);
	     cell250.setBorderWidth(0.0f);
	     cell250.setPadding(10f);
		 iniciodatos.addCell(cell250);
		 
		 
		 String fechahoraSolicitud = "";
	     if(solicitud.fechaCaptura!=null)
	    	 fechahoraSolicitud = String.valueOf(solicitud.fechaCaptura);
	     
		 Paragraph fechahoraStatus = new Paragraph("Fecha de Alta y Hora de Status: "+fechahoraSolicitud, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell198 = new PdfPCell(fechahoraStatus);
	     cell198.setHorizontalAlignment(0);
	     cell198.setColspan(15);
	     cell198.setBorderWidth(0.0f);
	     cell198.setPadding(10f);
	     iniciodatos.addCell(cell198);
	     
	    Paragraph Capturista = new Paragraph("Capturista: "+capturista, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1981 = new PdfPCell(Capturista);
	     cell1981.setHorizontalAlignment(0);
	     cell1981.setColspan(7);
	     cell1981.setBorderWidth(0.0f);
	     cell1981.setPadding(10f);
	     iniciodatos.addCell(cell1981);
	     
	     
	     Paragraph analistaCredito = new Paragraph("Analista: "+analista, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1982 = new PdfPCell(analistaCredito);
	     cell1982.setHorizontalAlignment(0);
	     cell1982.setColspan(8);
	     cell1982.setBorderWidth(0.0f);
	     cell1982.setPadding(10f);
	     iniciodatos.addCell(cell1982);
	     
	     
	   
	     Paragraph ejecutivo1 = new Paragraph("Vendedor: "+HTMLHelper.getDescripcion(catEjecutivosCredito,solicitud.idEjecutivo), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell113 = new PdfPCell(ejecutivo1);
	     cell113.setHorizontalAlignment(0);
	     cell113.setColspan(7);
	     cell113.setBorderWidth(0.0f);
	     cell113.setPadding(10f);
	     iniciodatos.addCell(cell113);
	   
	     
	      String plazoString = "";
	         if( solicitud.tipoOperacion==ClientesConstants.GRUPAL || solicitud.tipoOperacion==ClientesConstants.REESTRUCTURA_GRUPAL)
	            plazoString = "Semanas";
	         else
		        plazoString = "Meses";
	     
	     Paragraph plazoDecision = new Paragraph("Plazo: "+HTMLHelper.displayField(decisionComite.plazoAutorizado)+" "+plazoString, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1134 = new PdfPCell(plazoDecision);
	     cell1134.setHorizontalAlignment(0);
	     cell1134.setColspan(3);
	     cell1134.setBorderWidth(0.0f);
	     cell1134.setPadding(10f);
	     iniciodatos.addCell(cell1134);
	     
	     
	     Paragraph frecPago = new Paragraph("Frecuencia de Pago: "+HTMLHelper.getDescripcion(catFrecuenciaPago,decisionComite.frecuenciaPago), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1135 = new PdfPCell(frecPago);
	     cell1135.setHorizontalAlignment(0);
	     cell1135.setColspan(5);
	     cell1135.setBorderWidth(0.0f);
	     cell1135.setPadding(10f);
	     iniciodatos.addCell(cell1135);
	     
	     
	     Paragraph tipoCredito = new Paragraph("Tipo de Crédito: "+Producto, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell1138 = new PdfPCell(tipoCredito);
	     cell1138.setHorizontalAlignment(0);
	     cell1138.setColspan(7);
	     cell1138.setBorderWidth(0.0f);
	     cell1138.setPadding(10f);
	     iniciodatos.addCell(cell1138);
	     
	     
	     Paragraph tasa = new Paragraph("Tasa: "+HTMLHelper.muestraTasa(decisionComite.tasa, catTasas)+" %", FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11351 = new PdfPCell(tasa);
	     cell11351.setHorizontalAlignment(0);
	     cell11351.setColspan(8);
	     cell11351.setBorderWidth(0.0f);
	     cell11351.setPadding(10f);
	     iniciodatos.addCell(cell11351);
	     
	     
	     Paragraph compañia = new Paragraph("Compañía: Crédito Firme", FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11389 = new PdfPCell(compañia);
	     cell11389.setHorizontalAlignment(0);
	     cell11389.setColspan(7);
	     cell11389.setBorderWidth(0.0f);
	     cell11389.setPadding(10f);
	     iniciodatos.addCell(cell11389);
	     
	     
	     Paragraph sucursal = new Paragraph("Sucursal: "+HTMLHelper.getDescripcion(catSucursales, cliente.idSucursal), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell113512 = new PdfPCell(sucursal);
	     cell113512.setHorizontalAlignment(0);
	     cell113512.setColspan(8);
	     cell113512.setBorderWidth(0.0f);
	     cell113512.setPadding(10f);
	     iniciodatos.addCell(cell113512);
	     
	     
	     Paragraph status = new Paragraph("Status: "+HTMLHelper.getDescripcion(catDecision,decisionComite.decisionComite), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11352 = new PdfPCell(status);
	     cell11352.setHorizontalAlignment(0);
	     cell11352.setColspan(15);
	     cell11352.setBorderWidth(0.0f);
	     cell11352.setPadding(10f);
	     iniciodatos.addCell(cell11352);
	     
	     
	     	     
	     Paragraph observaciones = new Paragraph("Observaciones: "+HTMLHelper.displayField(decisionComite.comentariosComite), FontFactory.getFont(FontFactory.HELVETICA,12, Font.BOLDITALIC));
	     PdfPCell cell11353 = new PdfPCell(observaciones);
	     cell11353.setHorizontalAlignment(0);
	     cell11353.setColspan(15);
	     cell11353.setBorderWidth(0.5f);
	     cell11353.setPadding(10f);
	     iniciodatos.addCell(cell11353);
	     
	   
	      Logger.debug("Scoring: "+score.toString());
	 	String dictamenScoring = "";
	     if(score!= null){
	    	 switch(score.dictamenFinal){
	    	  case 1:
	    		  dictamenScoring = "Aprobar";
	    		  break;
	    	  case 2:
	    		  dictamenScoring = "Dudar";
	    		  break;
	    	  case 3:
	    		  dictamenScoring = "Denegar";
	    		  break;
	    		  
	    	 } 
	    	 
	     }
	     
	     
	     Paragraph scoreDic = new Paragraph("Scoring: "+dictamenScoring, FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11354 = new PdfPCell(scoreDic);
	     cell11354.setHorizontalAlignment(0);
	     cell11354.setColspan(15);
	     cell11354.setBorderWidth(0.0f);
	     cell11354.setPadding(10f);
	     iniciodatos.addCell(cell11354);
	     
	     
	     Paragraph fechaCirculo = new Paragraph("Fecha Círculo: "+HTMLHelper.displayField(circuloCredito.fechaConsulta), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11355 = new PdfPCell(fechaCirculo);
	     cell11355.setHorizontalAlignment(0);
	     cell11355.setColspan(7);
	     cell11355.setBorderWidth(0.0f);
	     cell11355.setPadding(10f);
	     iniciodatos.addCell(cell11355);
	     
	     
	     Paragraph fechaBNC = new Paragraph("Fecha BNC: "+HTMLHelper.displayField(buroCredito.fechaConsulta), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11356 = new PdfPCell(fechaBNC);
	     cell11356.setHorizontalAlignment(0);
	     cell11356.setColspan(8);
	     cell11356.setBorderWidth(0.0f);
	     cell11356.setPadding(10f);
	     iniciodatos.addCell(cell11356);
	     
	     
	     Paragraph credSolicitado = new Paragraph("Crédito Solicitado: "+HTMLHelper.formatCantidad(solicitud.montoSolicitado), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11357 = new PdfPCell(credSolicitado);
	     cell11357.setHorizontalAlignment(0);
	     cell11357.setColspan(7);
	     cell11357.setBorderWidth(0.0f);
	     cell11357.setPadding(10f);
	     iniciodatos.addCell(cell11357);
	     
	     
	     Paragraph credAutorizado = new Paragraph("Crédito Autorizado: "+HTMLHelper.formatCantidad(decisionComite.montoAutorizado), FontFactory.getFont(FontFactory.HELVETICA,10, Font.BOLDITALIC));
	     PdfPCell cell11358 = new PdfPCell(credAutorizado);
	     cell11358.setHorizontalAlignment(0);
	     cell11358.setColspan(8);
	     cell11358.setBorderWidth(0.0f);
	     cell11358.setPadding(10f);
	     iniciodatos.addCell(cell11358);
	     
	     iniciodatos.setSpacingAfter(15f);
		 document.add(iniciodatos);
	     
	     
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
    
}


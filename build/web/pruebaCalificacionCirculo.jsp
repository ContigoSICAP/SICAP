<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.dao.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="java.text.NumberFormat"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%

	try{
		InformacionCrediticiaVO[] info = null;
		InformacionCrediticiaDAO infoDAO = new InformacionCrediticiaDAO();
		info = infoDAO.getInfoCrediticiaCirculo(2);
		int contBuenas = 0;
		int contRegulares = 0;
		int contMalas = 0;
		int contNA = 0;
		int contSin = 0;
			
		for( int j=0; j<info.length; j++ ){
			InformacionCrediticiaVO temp = new InformacionCrediticiaVO();
			temp = info[j];
			int calificacion = new ScoringUtil().getCalificacionCirculo(temp);
			switch(calificacion){
				case 1:
					contBuenas++;
					break;
				case 2:
					contMalas++;
					break;
				case 3:
					contRegulares++;
					break;
				case 4:
					contNA++;
					break;
				case 5:
					contSin++;
					break;
			}
		}

			
		System.out.println("No. Buenas: " + contBuenas);
		System.out.println("No. Malas: " + contMalas);
		System.out.println("No. Regulares: " + contRegulares);
		System.out.println("No. NA: " + contNA);
		System.out.println("No. SIN: " + contSin);
		System.out.println("Totales: " + info.length);
		
		}catch(Exception e){
			Logger.debug("Excepcion en consultas Círculo : "+e.getMessage());
			e.printStackTrace();
		}
%>
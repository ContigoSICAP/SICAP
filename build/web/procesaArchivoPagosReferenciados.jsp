<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.dao.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%@ page import="java.sql.Date"%><%
	int cont = 0;
	ArrayList<Notification> array = new ArrayList<Notification>();
	Notification notificaciones[] = null;
	mySmartUpload.initialize(pageContext);
	mySmartUpload.upload();
	Logger.debug("1");
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
	java.util.Date date = new java.util.Date();
	String fechaHoy = sdf.format(date);
	PagosReferenciadosHelper prh = new PagosReferenciadosHelper();
	for (int i=0;i<mySmartUpload.getFiles().getCount();i++){
		Logger.debug("2");
		com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
		if (!myFile.isMissing() ) {
			Logger.debug("3");
			if ( myFile.getSize()>0){
				Logger.debug("4");
			 	if (ArchivosAsociadosHelper.esFormatoValido(myFile)){
			 		Logger.debug("5");
					String content = myFile.getContentString();
					String cadSalida = "";
					if(myFile.getFieldName().equalsIgnoreCase("pagosReferenciadosHSBC")){
						String lines[] = content.split("\r");
						try{
							for(int j=0; j<=lines.length - 1; j++){
								Logger.debug("6");
								//TransferenciasHelper trHelper = new TransferenciasHelper();
						 		if(!lines[j].trim().equalsIgnoreCase("")){
						 			Logger.debug("7");
									PagoReferenciadoVO vo = prh.getPagosReferenciadosVO(lines[j]);
									//TransferenciasVO trVO = trHelper.getTransferenciaVO(vo);
									if(vo.descripcion.trim().equalsIgnoreCase("IGNORAR")){
										Logger.debug("Es IGNORAR....");
										continue;
									}
									if(!prh.existeReferencia(vo) ){
										Logger.debug("Es incidencia....");
										prh.agregaIncidencia(vo);
									}
									else if(prh.esPagoT24(vo)){
										Logger.debug("Es HSBC...");
										Logger.debug("Es pago T24");
										cadSalida = prh.agregaPagoArchivo(vo);
										Logger.debug("Por agregar pago tipo T...");
										prh.agregaPago(vo,"T");
									}
									else{
										ReferenciaGeneralDAO refGralDAO = new ReferenciaGeneralDAO();
										ReferenciaGeneralVO refGralVO = refGralDAO.getReferenciaGeneral(vo.referencia);
										if(refGralVO != null){
											if(refGralVO.producto==4 || refGralVO.producto==5){
												Logger.debug("Es vivienda o PyME!!!");
												Logger.debug("Es referencia anterior");
												prh.addPagoReferenciaAnterior(vo);
												prh.agregaPago(vo,"A");
											}
											else if(refGralVO.producto==1 || refGralVO.producto==3){
												Logger.debug("Por ver si es anterior al 2 de abril...");
												if(refGralVO.fechaInicio != null){
													Date fecha3Abril = new Date(2008,4,2);
													if(refGralVO.fechaInicio.before(fecha3Abril)){
														Logger.debug("Es referencia anterior");
														prh.addPagoReferenciaAnterior(vo);
														prh.agregaPago(vo,"A");
													}
													else
														prh.agregaPago(vo,"T");
												}
												else
													prh.agregaIncidencia(vo);
											}
											else if(refGralVO.producto==2){
												Logger.debug("Por ver si es anterior al 21 de diciembre...");
												if(refGralVO.fechaInicio != null){
													Date fecha21Dic = new Date(2007,12,21);
													if(refGralVO.fechaInicio.before(fecha21Dic)){
														Logger.debug("Es referencia anterior");
														prh.addPagoReferenciaAnterior(vo);
														prh.agregaPago(vo,"A");
													}
													else
														prh.agregaPago(vo,"T");
												}
												else
													prh.agregaPago(vo,"T");
											}
										}
									}
									//trHelper.insertaTransferencia(trVO);
									cont++;
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					if(myFile.getFieldName().equalsIgnoreCase("pagosReferenciadosBanorte")){
						String lines[] = content.split("\n");
						cadSalida += prh.procesaPagosBANORTE(lines);
					}
					if(cadSalida!=null && !cadSalida.trim().equalsIgnoreCase("\n") && cadSalida.trim().length()!=0)
						out.print(cadSalida);
					response.setContentType("text/plain");
					response.setHeader("Content-Disposition","attachment; filename=\"PAGOST24_" + fechaHoy + ".CSV\"");
					response.setHeader("cache-control", "no-cache");
				}else
					array.add(new Notification( ClientesConstants.ERROR_TYPE, "El archivo "+myFile.getFileName()+ " no se encuentra en un formato permitido." ));
				}else
					array.add(new Notification( ClientesConstants.ERROR_TYPE, "El archivo "+myFile.getFileName()+ " excede el tamaño permitido." ));
				}else
					array.add(new Notification( ClientesConstants.ERROR_TYPE, "El archivo "+myFile.getFileName()+ " no se cargó exitosamente." ));
		}
		if ( array.size()>0 ){
			notificaciones = new Notification[array.size()];
	    	for(int i=0;i<notificaciones.length; i++) notificaciones[i] = (Notification)array.get(i);
		}
		request.setAttribute("NOTIFICACIONES",notificaciones);
		//request.setAttribute("PAGOSREFERENCIADOSHELPER",prh);
		//Integer integ = new Integer(cont);
		//request.setAttribute("TOTALREGISTROSPROCESADOS", integ);
		//response.sendRedirect("/Afirme/resultadoPagosReferenciados.jsp");
%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Logger"%>
<jsp:directive.page import="com.sicap.clientes.vo.SolicitudVO"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Integer indiceSolicitud = (Integer)request.getAttribute("indiceSolicitud");
ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud.intValue()];
double saldo = 0;
double cargo = 0;
double abono = 0;
String evento = null;
String fecha = null;
boolean desplegar = false;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Muestra tabla de movimientos</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
		<link rel="stylesheet" type="text/css" href="styles.css">
		-->
		<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
	</head>
	<body leftmargin="0" topmargin="0">
		<jsp:include page="header.jsp" flush="true"/>
		<form action="">
			<table border="0" width="100%">
				<tr>
					<td align="center">
						<br>
						<h2>Tabla de movimientos</h2>
					<%if ( solicitud.idCreditoIBS == 0 && solicitud.idCuentaIBS == 0 ) {%>						
						<table border="1" cellpadding="0" cellspacing="0" width="90%">
							<tr>
								<td width="15%" align="center">Fecha</td>
								<td width="15%" align="center">Evento</td>
								<td width="15%" align="center">CARGO</td>
								<td width="15%" align="center">ABONO</td>
								<td width="15%" align="center">SALDO</td>
							</tr>
							<%for(int i = 0;solicitud.pagosManuales!=null && i < solicitud.pagosManuales.length; i++){
								desplegar = false;
								if(solicitud.pagosManuales[i].evento.equalsIgnoreCase("Cálculo de Interes")){
									fecha = "Del " + solicitud.pagosManuales[i].fechaIncial + " al " + solicitud.pagosManuales[i].fechaFinal;
									evento = "Cálculo de Intereses";
									if(solicitud.pagosManuales[i].intMoratorio > 0){
										if(solicitud.pagosManuales[i].multa > 0){
											cargo = (solicitud.pagosManuales[i].multa + solicitud.pagosManuales[i].ivaMulta + solicitud.pagosManuales[i].intMoratorio + solicitud.pagosManuales[i].cIntMoratorio + solicitud.pagosManuales[i].ivaIntMoratorio + solicitud.pagosManuales[i].cIvaIntMoratorio);
										}else{
											cargo = (solicitud.pagosManuales[i].intMoratorio + solicitud.pagosManuales[i].cIntMoratorio + solicitud.pagosManuales[i].ivaIntMoratorio + solicitud.pagosManuales[i].cIvaIntMoratorio);
										}
										if(solicitud.pagosManuales[i].capVigente==0)
											saldo = solicitud.pagosManuales[i].saldo;
										else
											saldo -= cargo;
										//cargo = sTotal;
										abono = 0;
										desplegar = true;
										cargo *= -1;
									}
								}else if(solicitud.pagosManuales[i].evento.equalsIgnoreCase("Recuperación")){
									fecha = solicitud.pagosManuales[i].fechaEvento.toString();
									evento = "Su pago, gracias...";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = 0;
									//abono = sTotal;
									abono = solicitud.pagosManuales[i].monto;
									saldo = solicitud.pagosManuales[i].saldo;
									desplegar = true;
								}else if(solicitud.pagosManuales[i].evento.equalsIgnoreCase("Traspaso a Cartera Vencida")){
									fecha = solicitud.pagosManuales[i].fechaEvento.toString();
									evento = "Vencimiento";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = solicitud.pagosManuales[i].totPagar * -1;
									abono = 0;
									saldo = solicitud.pagosManuales[i].saldo;
									desplegar = true;
								}
								if(desplegar){
							%>
							<tr>
								<td width="15%" align="center"><%= HTMLHelper.displayField(fecha)%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(evento)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(cargo)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(abono)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(saldo)%></td>
							</tr>
							<%	}
							}
							%>
						</table>
					<%} else if ( solicitud.idCreditoIBS != 0 ) {%>
						<table border="1" cellpadding="0" cellspacing="0" width="90%">
							<tr>
								<td width="15%" align="center">Fecha</td>
								<td width="15%" align="center">Evento</td>
								<td width="15%" align="center">CARGO</td>
								<td width="15%" align="center">ABONO</td>
								<td width="15%" align="center">SALDO</td>
							</tr>
							<%for(int i = 0;solicitud.eventos!=null && i < solicitud.eventos.length; i++){
								desplegar = false;
								if(solicitud.eventos[i].tipoEvento.equalsIgnoreCase("DES")){
									fecha = solicitud.eventos[i].fechaFin.toString();
									evento = "Desembolso";
									cargo = solicitud.eventos[i].monto;
									abono = 0;
									saldo = solicitud.eventos[i].saldo;
									desplegar = true;
								}else if(solicitud.eventos[i].tipoEvento.equalsIgnoreCase("PRV")){
									fecha = "Del " + solicitud.eventos[i].fechaIni + " al " + solicitud.eventos[i].fechaFin;
									evento = "Provision de Interes";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = solicitud.eventos[i].monto;
									abono = 0;
									saldo = solicitud.eventos[i].saldo;
									desplegar = true;
								}else if(solicitud.eventos[i].tipoEvento.equalsIgnoreCase("PAG")){
									fecha = solicitud.eventos[i].fechaFin.toString();
									evento = "Aplicacion de pago";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = 0;
									//abono = sTotal;
									abono = solicitud.eventos[i].monto;
									saldo = solicitud.eventos[i].saldo;
									desplegar = true;
								}else if(solicitud.eventos[i].tipoEvento.equalsIgnoreCase("MOR")){
									fecha = solicitud.eventos[i].fechaFin.toString();
									evento = "Calculo Moratorio";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = solicitud.eventos[i].monto;
									abono = 0;
									saldo = solicitud.eventos[i].saldo;
									desplegar = true;
								}else if(solicitud.eventos[i].tipoEvento.equalsIgnoreCase("MUL")){
									fecha = solicitud.eventos[i].fechaFin.toString();
									evento = "Multa";
									//sTotal = solicitud.pagosManuales[i].monto;
									cargo = solicitud.eventos[i].monto;
									abono = 0;
									saldo = solicitud.eventos[i].saldo;
									desplegar = true;
								}
								if(desplegar){
							%>
							<tr>
								<td width="15%" align="center"><%= HTMLHelper.displayField(fecha)%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(evento)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(cargo)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(abono)%></td>
								<td width="15%" align="center"><%= HTMLHelper.formatoMonto(saldo)%></td>
							</tr>
							<%	}
							}
							%>
						</table>
					<%	} %>
						<br>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="Cerrar" onclick="window.close();">
					</td>
				</tr>
			</table>
		</form>
		<jsp:include page="footer.jsp" flush="true"/>
	</body>
</html>
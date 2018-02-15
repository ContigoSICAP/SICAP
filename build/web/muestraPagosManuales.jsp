<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.cartera.PagoManualVO"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
PagoManualVO[] pagos = (PagoManualVO[])request.getAttribute("PAGOS_MANUALES");
String evento = null;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
    	<base href="<%=basePath%>">
		<title>Muestra Pagos Manuales</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
	</head>
	<body leftmargin="0" topmargin="0">
		<jsp:include page="header.jsp" flush="true"/>
		<form action="">
			<table border="0" width="100%">
				<tr>
					<td align="center">
					<br>
						<h3>Informaci&oacute;n del cliente</h3>
						<table border="1" cellpadding="0" cellspacing="0" width="85%">
							<tr>
								<td width="15%" align="left">Numero de cliente:</td>
								<td width="70%" align="left"><%=HTMLHelper.displayField(pagos[0].numCliete)%></td>
							</tr>
							<tr>
								<td width="15%" align="left">Cliente:</td>
								<td width="70%" align="left"><%=HTMLHelper.displayField(pagos[0].cliente)%></td>
							</tr>
							<tr>
								<td width="15%" align="left">Solicitud:</td>
								<td width="70%" align="left"><%=HTMLHelper.displayField(pagos[0].ciclo)%></td>
							</tr>
							<tr>
								<td width="15%" align="left">Referencia:</td>
								<td width="70%" align="left"><%=HTMLHelper.displayField(pagos[0].referencia)%></td>
							</tr>
						</table>
						<br>
						<h3>Aplicaci&oacute;n de pagos e intereses</h3>
						<table border="1" cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td align="center" style="font-weight: bold;font-size: 11px">Fecha</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Evento</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Monto</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Multa</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Int. Morat</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Interes</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Capital</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Int. Morat Vdos</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Int. Vdos</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Capítal Vdo</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Tot x Pagar</td>
								<td align="center" style="font-weight: bold;font-size: 11px">Saldo</td>
							</tr>
							<%for(int i = 0; pagos!=null && i < pagos.length; i++){
								pagos[i].saldo *= -1;
								evento = pagos[i].evento;
								if(pagos[i].evento.equalsIgnoreCase("Recuperación"))
									evento = "Pago";
							%>
							<tr>
								<td align="center"><%=HTMLHelper.displayField(pagos[i].fechaEvento.toString(), "&nbsp;") %></td>
								<td align="center"><%=HTMLHelper.displayField(evento)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].monto)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].multa + pagos[i].ivaMulta)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].intMoratorio + pagos[i].ivaIntMoratorio)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].intVigente + pagos[i].ivaIntVigente)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].capVigente)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].intMoratorioVencido)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].intVencido)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].capVencido)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].totPagar)%></td>
								<td align="center"><%=HTMLHelper.formatoMonto(pagos[i].saldo)%></td>
							</tr>
							<%}%>
						</table>
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

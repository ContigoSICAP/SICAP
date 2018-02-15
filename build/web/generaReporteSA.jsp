<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.SegurosVO"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>Pagos Referenciados</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	
	
 function reporteSegurosAfirme(){
  window.document.forma.command.value='reporteSegurosAfirme';
  window.document.forma.submit(); 
 }

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" onunload="recibir()">
<jsp:include page="header.jsp" flush="true"/>

<%   SegurosVO seguro = new SegurosVO();
     TreeMap meses = CatalogoHelper.getCatalogoMeses();
     TreeMap años = CatalogoHelper.getCatalogoAños();
%>

<center>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%">
	<tr>		
		<td align="center" width="100%">
			<h3>Reporte Seguros Afirme</h3>			
			<table border="0" cellpadding="0" width="100%">
																				
						
			<tr>
			  <td width="50%" align="right">Mes<br></td>
			  <td width="50%">
			  	<select name="Mes">
			  	<%=HTMLHelper.displayCombo(meses, seguro.mes)%>
			  	</select>
			  </td>
			</tr> 
			
			<tr>
			  <td width="50%" align="right">Año<br></td>
			  <td width="50%">
			  	<select name="Año">
			  	<%=HTMLHelper.displayComboCheck(años, seguro.año)%>
			  	</select>
			  </td>
			</tr>          
			
			<tr>
					<td align="center" colspan="2">
					<br>		  
						<input type="button" onclick="reporteSegurosAfirme()" value="Enviar">						
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</center>
  </body>
</html>

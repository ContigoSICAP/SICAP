<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.*"%>

<html>
<head>
<title>Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

<script>

	function generaRFC(){
		
		if ( window.document.formarfc.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.formarfc.nombre.value)){
			alert("Ingrese el nombre completo");
			window.document.formarfc.nombre.focus();
			return false;
		}
		
		if ( window.document.formarfc.aPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.formarfc.aPaterno.value) ){
			alert("Ingrese un apellido Paterno v·lido");
			window.document.formarfc.aMaterno.focus();
			return false;
		}

		if ( window.document.formarfc.aMaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.formarfc.aMaterno.value)){
			alert("Ingrese un apellido Materno v·lido");
			window.document.formarfc.aMaterno.focus();
			return false;
		}
                
                if(window.document.formarfc.aPaterno.value.toUpperCase()=='NO PROPORCIONADO' && window.document.formarfc.aMaterno.value.toUpperCase()=='NO PROPORCIONADO')
                    {
                        alert("Proporcione almenos un apellido");
			window.document.formarfc.aPaterno.focus();
			return false;
                    }
                
                
		
		if ( window.document.formarfc.fechaNacimiento.value!='' ){
			if ( !esFechaValida(window.document.formarfc.fechaNacimiento,false) ){
				alert("La Fecha de nacimiento es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de nacimiento");
			window.document.formarfc.fechaNacimiento.focus();
			return false;
		}

                if(window.document.formarfc.aPaterno.value.toUpperCase()=='NO PROPORCIONADO' && esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.formarfc.aMaterno.value))
                    {
			window.document.formarfc.aPaterno.value = window.document.formarfc.aMaterno.value.toUpperCase();
                        window.document.formarfc.aMaterno.value = 'NO PROPORCIONADO';
                    } 
                    
		window.document.formarfc.submit();	
	}

	function seleccionar(){
		window.opener.document.forma.rfc.value = window.document.formarfc.rfc.value;
		window.close();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	ClienteVO cliente = new ClienteVO();
	TreeMap catEstados = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADOS);		
	
	if ((ClienteVO)request.getAttribute("ayudarfc")!=null){
		cliente = (ClienteVO)request.getAttribute("ayudarfc");
		session.setAttribute("ayudarfc", cliente);
	}

	//if ( cliente.direcciones!=null ){
	//	direccion = cliente.direcciones[0];
	//}
	
%>

<body leftmargin="0" topmargin="0">
<center>

<form name="formarfc" action="controller" method="post">
<input type="hidden" name="command" value="generaRFC">

<table border="1" cellpadding="0" width="515" height="183">
<tr>
<td colspan="2"><h3 align="center">Datos del cliente<br></h3></td>
</tr>
	<tr>
  	<td width="50%" align="right">Nombres</td>
  	<td width="50%">  
  	<input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(request.getParameter("nombre"))%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido paterno</td>
  	<td width="50%">  
  	<input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(request.getParameter("aPaterno"))%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido materno</td>
  	<td width="50%">  
  	<input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(request.getParameter("aMaterno"))%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Fecha de nacimiento</td>
  	<td width="50%">  
  	<input type="text" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("fechaNacimiento"))%>">(dd/mm/aaaa)
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Entidad de nacimiento</td>
  	<td width="50%">  
  	<select name="entidadNacimiento" size="1">
		<%=HTMLHelper.displayCombo(catEstados, cliente.entidadNacimiento)%>
	</select>
  	</td>
	</tr>

	<tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Generar RFC" onClick="generaRFC()">
		</td>
	</tr>

	<tr>
		<input type="hidden" name="rfc" value="">
		
		<td align="center" colspan="2"><a href="#" onClick="seleccionar()">  <%=HTMLHelper.displayField(cliente.rfc)%></a></td>
	</tr>
</table> 
</form>
</center>
</body>
</html>
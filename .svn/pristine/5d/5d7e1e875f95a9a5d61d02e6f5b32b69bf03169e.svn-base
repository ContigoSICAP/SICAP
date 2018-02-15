<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Registro masivo de clientes SICAP-IBS</title>
<script>
  function registroClientes(){
      if ( window.document.forma.fecha.value==''){
          alert("Debe especificar una fecha a procesar");
          return false;
      }
      window.document.forma.submit();
  }
</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="white" bgproperties=fixed>
<table>
<tr>
<td>
<jsp:include page="header.jsp" flush="true"/>
</td></tr>
</table>
    <center><h3>Registro masivo de clientes en IBS<br></h3></center>
<center><div align="right"> 
</div>
<form name="forma" action="controller" method="POST">
<input type="hidden" name="command" value="registroMasivoIBS">
<div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
            <tr>
                        <td width="50%" height="10%" align="right">Fecha de carga (DD/MM/AAAA)<br></td>
                        <td width="50%"><input type="text" name="fecha" size="10" maxlength="10"></td>
            </tr>

            <tr>
                        <td colspan="3" height="10%" align="center"><input type="button" value="Registra clientes" onclick="registroClientes();"></td>
                        <td colspan="3" height="10%" align="center"><br></td>
            </tr>
            </table>
</form>
</center>
<%@include file="footer.jsp"%>
</body>
</html>


<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="java.util.*"%>

<%
	TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
        OrdenDePagoVO oPago = (OrdenDePagoVO)request.getAttribute("ORDENDEPAGO");
        Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
        int numCliente = HTMLHelper.getParameterInt(request, "idCliente");
        int numSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
        int idBeneficiario = 0;
        Float monto = (Float)request.getAttribute("MONTO");
        ArrayList<String> nombresBeneficiarios = (ArrayList<String>)request.getAttribute("NOMBRESBENEFICIARIOS");
        ArrayList<Integer> porcentajes = (ArrayList<Integer>)request.getAttribute("PORCENTAJES");        
%>

<html>
<head>
<title>Generación de Orden de Pago para Seguro</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
  function buscaOrdenSeguro(){
      if ( window.document.forma.idCliente.value=='' || window.document.forma.idCliente.value==0 || !esEntero(window.document.forma.idCliente.value)){
          alert("Número de cliente inválido");
          return false;
      }
      if (window.document.forma.idSolicitud.value==''|| window.document.forma.idSolicitud.value==0 || !esEntero(window.document.forma.idSolicitud.value)){
          alert("Número de solicitud inválido");
          return false;
      }
      window.document.forma.command.value='consultaOrdenPagoSeguro';
      window.document.forma.submit();
  }
  function recalculaMonto(){
      if (window.document.forma.monto.value==''|| window.document.forma.monto.value==0 || !esFormatoMoneda(window.document.forma.monto.value)){
          alert("Monto inválido");
          window.document.forma.monto.value=0;
          return false;
      }
      else {
          window.document.forma.command.value='consultaOrdenPagoSeguro';
          window.document.forma.submit();
      }
  }
  function guardaOrdenSeguro(){
      if (window.document.forma.monto.value==''|| window.document.forma.monto.value==0 || !esFormatoMoneda(window.document.forma.monto.value)){
          alert("Monto inválido");
          return false;
      }
      if (window.document.forma.tipoBeneficiario.value==0){
          alert("Seleccione un beneficiario");
          return false;
      }
      if(window.document.forma.tipoBeneficiario.value==4){
          if(window.document.forma.otroBeneficiario.value==''|| !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.otroBeneficiario.value)){
                  alert("Nombre del beneficiario inválido");
                  return false;
              }
      }
      if(confirm("¿Está seguro de generar las Ordenes de Pago?")){
        window.document.forma.command.value='guardaOrdenSeguro';
        window.document.forma.submit();
      }
  }
  function nuevaBusqueda(){
      window.document.forma.command.value='generaOrdenSeguro';
      window.document.forma.submit();
  }
  function existeODP(){
      if(confirm("Este cliente ya cuenta con una Orden de Pago generada para seguro. ¿Desea capturar una nueva?")){
          window.document.forma.confirmaGenerarNuevaODP.value='1';
      }
      else {
          window.document.forma.confirmaGenerarNuevaODP.value='0';
      }
      window.document.forma.command.value='consultaOrdenPagoSeguro';
      window.document.forma.submit();
  }
  function muestraDocumento(tipo){
      window.document.forma.command.value='muestraDocumento';
      window.document.forma.tipo.value=tipo;
      window.document.forma.submit();
}

</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%if ( oPago != null){
     TreeMap catBeneficiarios = CatalogoHelper.getCatalogoBeneficiarios(numCliente, numSolicitud);
     boolean muestraBeneficiarios = (Boolean)request.getAttribute("MUESTRABENEFICIARIO");
     boolean existeODP = (Boolean)request.getAttribute("ADVERTENCIAODPEXISTENTE");
     boolean imprimirOrden = (Boolean)request.getAttribute("IMPRIMIRORDEN");
     idBeneficiario = (Integer)request.getAttribute("IDBENEFICIARIO");
     if(existeODP){
%>
<body leftmargin="0" topmargin="0" onload="existeODP()">
    <%} else {%>
<body leftmargin="0" topmargin="0">
    <%}%>
    <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Generación de Orden de Pago para Seguro<br></h3></center>
    <form name="forma" action="controller" method="POST">
    <input type="hidden" name="command" value="">
    <input type="hidden" name="tipo" value="">
    <input type="hidden" name="confirmaGenerarNuevaODP" value="1">
    <input type="hidden" name="nuevaConsulta" value="0">
    <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
    <div>
        <table border="0" width="100%" cellspacing="0" align="center" height="10%">
            <tr>
                <td width="50%" height="10%" align="right"><strong>No. cliente</strong><br></td>
                <td width="50%"><input type="text" name="idCliente" size="10" maxlength="20" value="<%=numCliente%>"readonly="readonly" class="soloLectura"></td>
            </tr>
            <tr>
                <td width="50%" height="10%" align="right"><strong>No. solicitud</strong><br></td>
                <td width="50%"><input type="text" name="idSolicitud" size="10" maxlength="20" value="<%=numSolicitud%>"readonly="readonly" class="soloLectura"></td>
            </tr>
            <br>
            <tr>
                <td width="50%" align="right"><strong>Monto</strong></td>
                <td width="50%"><input type="text" name="monto" size="10" maxlength="10" value="<%=monto%>" onchange="recalculaMonto()"/></td>
            </tr>
            <tr>
                <td width="49%" align="right"><strong>Banco</strong></td>
                <td width="51%">
                    <select name="bancoODP" id="bancoODP" readOnly="readOnly">
                        <%=HTMLHelper.displayCombo(catBancos, oPago.getIdBanco())%>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="49%" align="right"><strong>Beneficiario</strong></td>
                <td width="51%">
                    <select name="tipoBeneficiario" id="tipoBeneficiario" readOnly="readOnly" onchange="buscaOrdenSeguro()">
                        <%=HTMLHelper.displayCombo(catBeneficiarios, idBeneficiario)%>
                    </select>
                    <input type="hidden" name="nombreCompleto" value="<%=CatalogoHelper.getDescripcionBeneficiario(idBeneficiario, catBeneficiarios) %>">
                </td>
            </tr>            
    <%if (muestraBeneficiarios){
        if(idBeneficiario==3){%>
            <tr>
                <td colspan="2" align="center">
                    <table border="0" width="75%" cellspacing="0" align="center" height="10%">
                        <tr>
            <%for(int i=0; i<nombresBeneficiarios.size(); i++){%>
                            <td width="50%" align="center"><strong>Orden de Pago <%=i+1%></strong></td>
            <%}%>
                        </tr>
                        <tr>
            <%for(int i=0; i<nombresBeneficiarios.size(); i++){%>
            <td width="50%" align="center"><input type="text" size="45" name="nombreBeneficiario<%=i%>" value="<%=nombresBeneficiarios.get(i)%>"readonly="readonly" class="soloLectura"></td>               
            <%}%>

                        </tr>
                        <tr>
            <%for (int i=0; i<porcentajes.size(); i++){%>
            <td width="50%" align="center"><%=porcentajes.get(i)%>% <input type="text" size="10" name="montoBeneficiario<%=i%>" value="<%=porcentajes.get(i)*(monto/100)%>"readonly="readonly" class="soloLectura"></td>               
            <%}%>
                        </tr>
                    </table>
                </td>
            </tr>
       <%} else if(idBeneficiario==4) {
                if (imprimirOrden){
                    
         }%>
            <tr>
                <td width="50%" align="right"><strong>Nombre del beneficiario</strong></td>
                <%if (imprimirOrden){
                    String otroBeneficiario = (String)request.getAttribute("OTROBENEFICIARIO");%>                
                    <td width="50%"><input type="text" name="otroBeneficiario" size="45" maxlength="75" value="<%=otroBeneficiario%>"></td>
                <%} else {%>                    
                    <td width="50%"><input type="text" name="otroBeneficiario" size="45" maxlength="75"></td>
                <%}%>
            </tr>
        <%}%>
    <%} if (imprimirOrden){ %>   
            <table border="0" width="25%" cellspacing="10" align="center">
                <tr>
                    <td align="center">
                        <br>
                        <input type="button" name="" value="Consulta Orden de Pago" onClick="muestraDocumento('ORDENPAGOSEGURO')">
                        <br>
                        <br>
                    </td>
                    <td align="center">
                        <br>
                        <input type="button" name="" value="Nueva Consulta" onClick="nuevaBusqueda()">
                        <br>
                        <br>
                    </td>
                </tr>                
            </table>
    <%} else { %>
            <table border="0" width="25%" cellspacing="10" align="center">
                <tr>
                    <td align="center">
                        <br>
                        <input type="button" name="" value="Generar" onClick="guardaOrdenSeguro()">
                        <br>
                        <br>
                    </td>
                    <td align="center">
                        <br>
                        <input type="button" name="" value="Regresar" onClick="nuevaBusqueda()" >
                        <br>
                        <br>
                    </td>
                </tr>
            </table>
    <%}%>
        </table>
    </div>
    <%} else {%>
<body leftmargin="0" topmargin="0">
    <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Generación de Orden de Pago para Seguro<br></h3></center>
    <form name="forma" action="controller" method="POST">
    <input type="hidden" name="command" value="">
    <input type="hidden" name="tipo" value="">
    <input type="hidden" name="nuevaConsulta" value="1">
    <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
    <div>
        <table border="0" width="100%" cellspacing="0" align="center" height="10%">
            <tr>
                <td width="50%" height="10%" align="right"><strong>No. cliente</strong><br></td>
                <td width="50%"><input type="text" name="idCliente" size="10" maxlength="20"></td>
            </tr>
            <tr>
                <td width="50%" height="10%" align="right"><strong>No. solicitud</strong><br></td>
                <td width="50%"><input type="text" name="idSolicitud" size="10" maxlength="20"></td>
            </tr>
            <tr>
                <td colspan="3" height="10%" align="center"><input type="button" value="Buscar" onclick="buscaOrdenSeguro()"></td>
                <td colspan="3" height="10%" align="center"><br></td>
            </tr>
        </table>
    </div>
    <%}%>
    </form>
    <%@include file="footer.jsp"%>
</body>
</html>
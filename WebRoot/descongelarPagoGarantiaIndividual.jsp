<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.cartera.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<html>
<head>
<title>Descongelar pago en garant&iacute;a individual</title>
<script>
  function listarPagos(){
      if ( window.document.forma.idGrupo.value=='' || window.document.forma.idCiclo.value=='' || window.document.forma.idCliente.value=='' ){
          alert("Debe especificar un grupo, ciclo y numero de cliente");
          return false;
      }
      window.document.forma.command.value = 'descongelaAhorroIndividual';
	  window.document.forma.submit();
  }
  
  function descongelaPagoIndividual(){
      window.document.forma.command.value = 'descongelaAhorroIndividual';
	  window.document.forma.submit();
  }
</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<%
int i = 0;
int muestrapantallaactualizacion = 0;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
//int idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
//if(request.getAttribute("ACTUALIZACION_EJECUTIVO")!= null)
	//muestrapantallaactualizacion = (Integer)request.getAttribute("ACTUALIZACION_EJECUTIVO");

//UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
DescongelaPagoGarantiaIndVO listaPagos = (DescongelaPagoGarantiaIndVO) request.getAttribute("LISTAPAGOS");
GrupoVO datosGrupo = (GrupoVO) request.getAttribute("DATOSGRUPO");
//DescongelaAhorroVO grupoSeleccionado = null;
int numeroGrupo = 0;
String referencia = null;
String nombreGrupo = null;
int numCliente = 0;
int numCiclo  = 0;
String nombre_completo = null;
double montoCongelado = 0;
double montoPago = 0;
//ArrayList listaGrupos = null;



//Ejecutivo seleccionado
if(request.getAttribute("LISTAPAGOS")!=null) {
	//grupoSeleccionado = (DescongelaAhorroVO)request.getAttribute("MONTODESCONGELADO");
}
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center><h3>Descongelar Pago en Garant&iacute;a Individual<br></h3></center>

<form name="forma" action="admin" method="POST">
<input type="hidden" name="command" value="descongelaPagoIndividual">
<center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
<div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
            <tr>
                        <td width="50%" height="10%" align="right">Grupo<br></td>
                        <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10"></td>
            </tr>
            
            <tr>
            <td>&nbsp;</td>
            </tr>

            <tr>
                        <td width="50%" height="10%" align="right">Ciclo<br></td>
                        <td width="50%"><input type="text" name="idCiclo" size="10" maxlength="10"></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            </tr>

            <tr>
                        <td width="50%" height="10%" align="right">Número Cliente<br></td>
                        <td width="50%"><input type="text" name="idCliente" size="10" maxlength="10"></td>
            </tr>
            
            <tr>
            <td>&nbsp;</td>
            </tr>
            
            <tr>
					<td colspan="2">
				<%if ( listaPagos !=null ){
				%>
					<br><table width="80%" align="center" border="0">
						    <tr bgcolor="#009865">
						        <td class="whitetext" align="center">Descongelar</td>
						    	<td class="whitetext" align="center">Numero de Grupo</td>
						    	<td class="whitetext" align="center">Numero de Ciclo</td>
								<td class="whitetext" align="center">Nombre Grupo</td>
								<td class="whitetext" align="center">Numero Cliente</td>
								<td class="whitetext" align="center">Nombre Cliente</td>
								<td class="whitetext" align="center">Monto Pago</td>
							</tr>
							<% 
								for(i=0; i<1; i++) {		
									//grupoDescongelaMonto = (DescongelaAhorroVO)listaGrupos.get(i);
									//grupoDescongelaMonto = (DescongelaAhorroVO[]) request.getAttribute("MONTODESCONGELADO");
									numeroGrupo = listaPagos.getNumGrupo();
									numCliente  = listaPagos.getNumCliente();
									montoCongelado = listaPagos.getMontoPago();
									nombre_completo = listaPagos.getNombreCompleto();
									numCiclo		= listaPagos.getNumCiclo();
									nombreGrupo = datosGrupo.nombre;
									
							%>	
							<tr>
								<td><input type="radio" name="opcion" value="<%=numeroGrupo%>" checked>&nbsp;</td>
								<td><%=numeroGrupo%></td>
								<td><%=numCiclo%></td>
								<td><%=nombreGrupo%></td>
								<td><%=numCliente%></td>
								<td><%=nombre_completo%></td>
								<td><%=montoCongelado%></td>
							</tr>
							<% 
								}
							%>
					</table>	
										
				<%
				}	//fin listaIntegrantes
				%>
					</td>
				</tr>
				
			<tr>
            <td><input type="hidden" name="numGrupo" size="10" maxlength="10" value="<%=numeroGrupo%>"></td>
            <td><input type="hidden" name="numCliente" size="10" maxlength="10" value="<%=numCliente%>"></td>
            <td><input type="hidden" name="numCiclo" size="10" maxlength="10" value="<%=numCiclo%>"></td>
            <td><input type="hidden" name="monto_ind" size="10" maxlength="10" value="<%=montoCongelado%>"></td>
            </tr>	

            <tr>
                        <td colspan="3" height="10%" align="center">
                        <input type="button" value="Listar" onclick="listarPagos();">
                        <% if(listaPagos!=null){ %>
						<input type="button" value="Descongelar" onClick="descongelaPagoIndividual()">
					    &nbsp;
					    <% } %>
                        </td>
                        <td colspan="3" height="10%" align="center"><br></td>
            </tr>
            </table>
</div>
</form>

<%@include file="footer.jsp"%></body>
</html>


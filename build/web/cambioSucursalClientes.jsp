<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    TreeMap catSucursales = CatalogoHelper.getCatalogo("c_sucursales");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    ClienteVO cliente = (ClienteVO)request.getAttribute("CLIENTE");    
%>
<html>
    <head>
        <title>Cambio de Sucursal Clientes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            function consultaCliente(){
                if (window.document.forma.numCliente.value==0||window.document.forma.numCliente.value==''|| !esEntero(window.document.forma.numCliente.value)){
                    alert('Número de cliente inválido');
                    return false;
		}
		window.document.forma.command.value='consultaDatosCliente';
		window.document.forma.submit();
            }
            function cambiaSucursal(){
                if(window.document.forma.nuevaSucursal.value==0){
                    alert('No ha seleccionado la nueva sucursal');
                    return false;
                }
                else if(window.document.forma.nuevaSucursal.value==window.document.forma.numSucursal.value){
                    alert('Seleccione una sucursal diferente a la actual');
                    return false;
                }
                if(confirm("¿Está seguro de cambiar la sucursal?")){
                    window.document.forma.command.value="cambiarSucursalCliente";
                    window.document.forma.submit();
                }
            }
            function nuevaConsulta(){
                window.document.forma.command.value="cambioSucursalClientes";
                window.document.forma.submit();
            }            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <h2>Cambio de Sucursal Clientes</h2>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
            <form action="admin" method="post" name="forma">
                <input type="hidden" name="command" value="">
                <%if(cliente!=null){ %>
                    <input type="hidden" name="numCliente" value="<%=cliente.getIdCliente()%>">
                    <input type="hidden" name="numSucursal" value="<%=cliente.getIdSucursal()%>">
                    <table border="1" cellpadding="0" cellspacing="0" width="50%">
                        <tr>
                            <td width="20%" align="right">Número de Cliente&nbsp;</td>
                            <td width="45%" align="left">&nbsp; <%=cliente.getIdCliente()%></td>
                        </tr>
                        <tr>
                            <td width="20%" align="right">Nombre Completo&nbsp;</td>
                            <td width="45%" align="left">&nbsp; <%=cliente.getNombreCompleto()%></td>
                        </tr>
                        <tr>
                            <td width="20%" align="right">Fecha de Nacimiento&nbsp;</td>
                            <td width="45%" align="left">&nbsp; <%=cliente.getFechaNacimiento()%></td>
                        </tr>
                        <tr>
                            <td width="20%" align="right">RFC&nbsp;</td>
                            <td width="45%" align="left">&nbsp; <%=cliente.getRfc()%></td>
                        </tr>
                        <tr>
                            <td width="20%" align="right">Sucursal&nbsp;</td>
                            <td width="45%" align="left">&nbsp; <%=HTMLHelper.getDescripcion(catSucursales, cliente.getIdSucursal())%></td>
                        </tr>
                        <br>
                        <br>                        
                    </table>
                    <br/>
                    <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                        <tr>
                            <td align="center" colspan="7">
                                Nueva sucursal:
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="7">                                
                                <select name="nuevaSucursal"><%=HTMLHelper.displayCombo(catSucursales, 0)%></select>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="7">
                                <br>
                                <input type="button" value="Cambiar sucursal" onClick="cambiaSucursal()">
                                <input type="button" value="Nueva consulta" onClick="nuevaConsulta()">
                            </td>
                        </tr>
                    </table>
                <%} else {%>
                    <table border="0" width="90%" align="center" >
                    <tr>
                        <td width="50%" align="right">Número de Cliente</td>
                        <td width="50%"><input type="text" name="numCliente" size="7" maxlength="7" value="" /></td>
                    </tr>
                   <tr>
                        <td colspan="2" align="center">
                            <br>
                            <input type="button" name="" value="Consultar" onClick="consultaCliente();" >
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>
                <%}%>
            </form>
        </center>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
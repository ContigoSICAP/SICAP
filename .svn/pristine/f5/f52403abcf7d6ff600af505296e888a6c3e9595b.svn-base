<%-- 
    Document   : caretraCedida
    Created on : 20/08/2012, 10:02:36 AM
    Author     : Alex
--%>

<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@ page contentType="text/html" pageEncoding="iso-8859-1"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
TreeMap catDespachos = CatalogoHelper.getCatalogoDespachos();
ArrayList<SaldoIBSVO> saldos = new ArrayList<SaldoIBSVO>();
if ((ArrayList<SaldoIBSVO>)request.getAttribute("SALDOSCARTERA")!=null)
    saldos = (ArrayList<SaldoIBSVO>)request.getAttribute("SALDOSCARTERA");
int i = 0;
/*for (SaldoIBSVO cartera : saldos){
    System.out.println("caretra_"+cartera.getIdClienteSICAP()+" "+cartera.getNombreCliente()+" "+cartera.getIdSolicitudSICAP()+" "+cartera.getMontoCredito());
}*/
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Migraci&oacute;n de Cartera</title>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="./js/functions.js" ></script>
        <script>
            function buscaClientes(){
                window.document.forma.command.value='buscaCarteraCedida';
                window.document.forma.submit();	
            }
            function migrarCartera(){
                window.document.forma.command.value='guardaCarteraCedida';
                var pasa = false;
                var verifica = false;
                if(window.document.forma.idDespacho.value != 0){
                    //for(var i = 0; i<window.document.forma.aplicaCartera.length; i++){
                    for(var i = 0; i<window.document.forma.numClientes.value; i++){
                        if(window.document.forma.numClientes.value == 1)
                            verifica = window.document.forma.aplicaCartera.checked;
                        else
                            verifica = window.document.forma.aplicaCartera[i].checked;
                        if(verifica){
                            pasa = true;
                        }
                    }
                    if(pasa){
                        window.document.forma.submit();
                    }else{
                        alert("Debe seleccionar algun grupo");
                    }
                }else{
                    alert("Debe seleccionar un despacho");
                }
            }
            function seleccionaTodo(){
                var checa = 1;
                if(window.document.forma.checkGeneral.checked == false)
                    checa = 0;
                //for (var i = 0; i<window.document.forma.aplicaCartera.length; i++){
                for (var i = 0; i<window.document.forma.numClientes.value; i++){
                    if(window.document.forma.numClientes.value == 1)
                        window.document.forma.aplicaCartera.checked=checa;
                    else
                        window.document.forma.aplicaCartera[i].checked=checa;
                }
            }
        </script>
    </head>
    <body>
    <center>
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value=""/>
            <h2>Migraci&oacute;n de Cartera a TCI<br/><br/></h2>
            <table border="0" cellpadding="1" cellspacing="1" width="60%">
                <tr>
                    <td align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="1" align="center">
                            <tr>
                                <td align="right">Sucursal&nbsp;</td>
                                <td><select name="sucursal" id="sucursal" size="1" onKeyPress="return submitenter(this,event)">
                                        <%= HTMLHelper.displayCombo(catSucursales, 0)%>
                                    </select>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><input type="button" value="Buscar" onclick="buscaClientes()"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <table border="2" cellpadding="0" cellspacing="1" align="center" bordercolor="#2ECCFA">
                            <tr bgcolor="#FFFFFF">
                                <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                                <td width="5%" align="center"><b>No Grupo</b></td>
                                <td width="20%" align="center"><b>Nombre Grupo</b></td>
                                <td width="5%" align="center"><b>Ciclo</b></td>
                                <td width="10%" align="center"><b>Monto Credito</b></td>
                                <td width="10%" align="center"><b>Saldo Cartera</b></td>
                                <td width="5%" align="center"><b>Dias de Mora</b></td>
                            </tr>
                            <%if (!saldos.isEmpty()){
                                for (SaldoIBSVO cartera : saldos){%>
                                    <tr>
                                        <td align="center"><input type="checkbox" name="aplicaCartera" id="aplicaCartera" value="<%=i%>" checked/></td>
                                        <td align="right"><%=HTMLHelper.displayField(cartera.getIdClienteSICAP(), true)%>&nbsp;&nbsp;</td>
                                        <td>&nbsp;&nbsp;<%=HTMLHelper.displayField(cartera.getNombreCliente())%></td>
                                        <td align="center"><%=HTMLHelper.displayField(cartera.getIdSolicitudSICAP(), true)%></td>
                                        <td align="right"><%=HTMLHelper.formatCantidad(cartera.getMontoCredito(), false)%>&nbsp;&nbsp;</td>
                                        <td align="right"><%=HTMLHelper.formatCantidad(cartera.getSaldoConInteresAlFinal(), false)%>&nbsp;&nbsp;</td>
                                        <td align="center"><%=HTMLHelper.displayField(cartera.getDiasMora(), true)%></td>
                                        <input type="hidden" name="idCliente<%=i%>" value="<%=cartera.getIdClienteSICAP()%>"/>
                                        <input type="hidden" name="nomcliente<%=i%>" value="<%=cartera.getNombreCliente()%>"/>
                                        <input type="hidden" name="idSolicitud<%=i%>" value="<%=cartera.getIdSolicitudSICAP()%>"/>
                                        <input type="hidden" name="montoCredito<%=i%>" value="<%=cartera.getMontoCredito()%>"/>
                                        <input type="hidden" name="idCredito<%=i%>" value="<%=cartera.getCredito()%>"/>
                                        <input type="hidden" name="saldoCartera<%=i%>" value="<%=cartera.getSaldoConInteresAlFinal()%>"/>
                                        <input type="hidden" name="diasMora<%=i%>" value="<%=cartera.getDiasMora()%>"/>
                                        <input type="hidden" name="referencia<%=i%>" value="<%=cartera.getReferencia()%>"/>
                                        <input type="hidden" name="fechaVencimiento<%=i%>" value="<%=cartera.getFechaVencimiento()%>"/>
                                    </tr>
                                    <%i++;
                                }
                            }%>
                        </table>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td><table border="0" cellpadding="0" cellspacing="1" align="center">
                            <tr>
                                <td align="right">Despacho&nbsp;</td>
                                <td><select name="idDespacho" id="idDespacho" size="1" onKeyPress="return submitenter(this,event)">
                                        <%= HTMLHelper.displayCombo(catDespachos, 0)%>
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td><input type="button" value="Migrar" onclick="migrarCartera()"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="numClientes" id="numClientes" value="<%=i%>"/>
        </form>
        <%@include file="footer.jsp"%>
    </center>
    </body>
</html>
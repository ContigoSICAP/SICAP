<%-- 
    Document   : asignaAuditor
    Created on : 18/02/2015, 12:51:22 PM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.vo.SucursalVO"%>
<%@page import="com.sicap.clientes.vo.CatalogoVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.vo.AuditoresVO"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
AuditoresVO auditor = null;
TreeMap catAuditores = CatalogoHelper.getCatalogoAuditoresActivos();
ArrayList<SucursalVO> arrMapaSucursales = new ArrayList<SucursalVO>();
ArrayList<AuditoresVO> arrSucursales = new ArrayList<AuditoresVO>();
int idAuditor = 0, idSuddir = 0, idRegion = 0, numSuddir = 0, numRegion = 0, numSucursal = 0, i = 0;
boolean asingada = false;
String mapaSucursales = "\"ini\"", mapaSub = "", mapaReg = "";
if(request.getAttribute("AUDITOR") != null){
    auditor = (AuditoresVO) request.getAttribute("AUDITOR");
    arrMapaSucursales = CatalogoHelper.getCatalogoMapaSucursales();
    arrSucursales = auditor.getArrSucursal();
    idAuditor = auditor.getNumAuditor();
}
if(!arrMapaSucursales.isEmpty()){
    for(SucursalVO mapa : arrMapaSucursales){
        if(idSuddir != mapa.getIdSubdireccion()){
            idSuddir = mapa.getIdSubdireccion();
            numSuddir++;
            numRegion = 0;
            mapaSucursales += ",\"subdir"+idSuddir+"\"";
            mapaSub = ",\"subdir"+idSuddir+"\"";
        }
        if(idSuddir == mapa.getIdSubdireccion()){
            if(idRegion != mapa.getIdRegion()){
                idRegion = mapa.getIdRegion();
                numRegion++;
                numSucursal = 0;
                mapaSucursales += ",\"reg"+numSuddir+numRegion+"\"";
                mapaReg = ",\"reg"+numSuddir+numRegion+"\"";
            }
            for(AuditoresVO listado :  arrSucursales){
                if(listado.getIdSucursal() == mapa.getIdSucursal()){
                    mapaSucursales = mapaSucursales.replace(mapaSub, "");
                    mapaSucursales = mapaSucursales.replace(mapaReg, "");
                }
            }
        }
    }
}
%>
<html>
    <head>
        <title>Asignaci&oacute;n de Sucursales</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function asignar(){
                deshabilita2();
                window.document.forma.command.value = 'asignaSucursalAuditor';
                window.document.forma.submit();
            }
            function buscar(){
                deshabilita();
                window.document.forma.command.value = 'buscaAuditorAsignar';
                window.document.forma.submit();
            }
            function regresar(){
                deshabilita();
                window.document.forma.command.value = 'auditores';
                window.document.forma.submit();
            }
            function habilita(){
                document.getElementById("Buscar").disabled = false;
                document.getElementById("Regresar").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita(){
                document.getElementById("Buscar").disabled = true;
                document.getElementById("Regresar").disabled = true;
                document.body.style.cursor = "wait";
            }
            function habilita2(){
                document.getElementById("Buscar").disabled = false;
                document.getElementById("Regresar").disabled = false;
                document.getElementById("Asignar").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita2(){
                document.getElementById("Buscar").disabled = true;
                document.getElementById("Regresar").disabled = true;
                document.getElementById("Asignar").disabled = true;
                document.body.style.cursor = "wait";
            }
        </script>
        <script type="text/javascript">
            function changeDisplay(theChecked,theWhat) {
                var theNode;
                var theDisplay;
                if (theChecked == true){
                    theDisplay = "";
                }else{
                    theDisplay = "none";
                }
                for (var i = 1;;i++){
                    // Check if the getElementById method is available
                    if (document.getElementById){
                        theNode = document.getElementById(theWhat+i);
                        /* alert(theNode); */
                        if (theNode == null)
                        { return; }
                    } else if (document.all){
                        // The alert lets me verify that I tested the path.
                        alert("Running an older version of IE." +" May not be able to hide rows");
                        theNode = document.all[theWhat+i];
                        if (theNode == null)
                        { return; }
                    }else{
                        alert("Cannot change visibility of the display element." +" Was " + theWhat);
                        return;
                    }
                    theNode.style.display = theDisplay;
                }
            }
            function changeAll(which){
                var all = [<%=mapaSucursales%>];
                for (var i = 0; i< all.length; i++){
                    changeDisplay(false,all[i]);
                    var element = all[i];
                    if (document.forms[0].elements[element]){
                        document.forms[0].elements[element].checked = false;
                    }
                }
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0" onload="changeAll(false);">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Asignaci&oacute;n de Sucursales</h2>
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <table border="0" cellspacing="5" align="center">
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Auditor:</th>
                <td width="50%">  
                    <select name="idAuditor" size="1">
                        <%=HTMLHelper.displayCombo(catAuditores, idAuditor)%>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <br>
                    <input type="button" name="Buscar" id="Buscar" value="Buscar" onclick="buscar()">
                    <input type="button" name="Regresar" id="Regresar" value="Regresar" onClick="regresar()">
                    <br><br></td>
            </tr>
        </table>
        <%if(auditor != null){%>
        <center><h2>Directorio de Sucursales</h2></center>
        <center>
        <table border="1" width="50%" >
            <tbody>
                <tr>
                    <th align="center"><strong>Subdireccion</strong></th>
                    <th align="center"><strong>Region</strong></td>
                    <th align="center"><strong>Sucursal</strong></td>
                </tr>
        <%idAuditor = 0;
        idSuddir = 0;
        idRegion = 0;
        numSuddir = 0;
        numRegion = 0;
        if(!arrMapaSucursales.isEmpty()){
            for(SucursalVO mapa : arrMapaSucursales){
                i++;
                asingada = false;
                for(int j = 0; j < arrSucursales.size();j++){
                    if(mapa.getIdSucursal() == arrSucursales.get(j).getIdSucursal()){
                        asingada = true;
                        arrSucursales.remove(j);
                        break;
                    }
                }
                if(idSuddir != mapa.getIdSubdireccion()){
                    idSuddir = mapa.getIdSubdireccion();
                    numSuddir++;
                    numRegion = 0;%>
                    <tr>
                        <td><input name="subdir<%=numSuddir%>" checked value="checked" onclick="changeDisplay(this.checked,this.name);" type="checkbox"><%=mapa.getNombreSubdireccion()%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                <%}
                if(idSuddir == mapa.getIdSubdireccion()){
                    if(idRegion != mapa.getIdRegion()){
                        idRegion = mapa.getIdRegion();
                        numRegion++;
                        numSucursal = 0;%>
                        <tr class="expand" id="subdir<%=numSuddir%><%=numRegion%>">
                            <td>&nbsp;</td>
                            <td><input name="reg<%=numSuddir%><%=numRegion%>" checked value="checked" onclick="changeDisplay(this.checked,this.name);" type="checkbox"><%=mapa.getNombreRegion()%></td>
                            <td>&nbsp;</td>
                        </tr>
                    <%}
                    if(idRegion == mapa.getIdRegion()){
                        numSucursal++;%>
                        <tr class="expand" id="reg<%=numSuddir%><%=numRegion%><%=numSucursal%>">
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td><%=HTMLHelper.displayCheck("asignasuc"+i,asingada)%><%=mapa.getNombre()%></td>
                            <input type="hidden" name="idSucursal<%=i%>" value="<%=mapa.getIdSucursal()%>">
                        </tr>
                    <%}
                }
            }
        }%>
                <tr>
                    <td align="center" colspan="3">
                        <br>
                        <input type="button" name="Asignar" id="Asignar" value="Asignar" onclick="asignar()">
                        <input type="hidden" name="listaSuc" value="<%=i%>">
                        <br><br></td>
                </tr>
            </tbody>
        </table>
        </center>
        <%}%>
    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
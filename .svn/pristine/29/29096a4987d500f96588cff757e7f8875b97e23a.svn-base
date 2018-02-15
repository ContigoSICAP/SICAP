<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.vo.TarjetasVO"%>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
int i = 0;
int numTarjetas = 0;
int banco = HTMLHelper.getParameterInt(request, "banco");
int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
if(!request.getAttribute("TARJETAS").equals("")){
    arrTarjetas = (ArrayList<TarjetasVO>)request.getAttribute("TARJETAS");
    numTarjetas = arrTarjetas.size();
}
%>
<html>
<head>
    <title>Genera Personalizaci&oacute;n Tarjeta</title>
    <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    function seleccionaTodo(){
        var checa = 1;
        if(window.document.forma.checkGeneral.checked == false)
            checa = 0;
        for (var i = 0; i<window.document.forma.numRegistros.value; i++){
            if(window.document.forma.numRegistros.value == 1)
                window.document.forma.idCheckBox.checked=checa;
            else
                window.document.forma.idCheckBox[i].checked=checa;
        }
        calculaImporteArchivo(window.document.forma.numRegistros.value);
    }
    
    function generaTarjetas(totalTarjetas){
        document.getElementById("getFile").disabled = true;
        if(window.confirm("¿ Confirma que deseas crear el archivo Personalización Tarjetas?")){
            window.document.forma.command.value='generaPersonalizacion';
            var pasa = false;
            var verifica = false;
            for(var i = 0; i<window.document.forma.numRegistros.value; i++){
                if(window.document.forma.numRegistros.value == 1)
                    verifica = window.document.forma.idCheckBox.checked;
                else
                    verifica = window.document.forma.idCheckBox[i].checked;
                if(verifica){
                    pasa = true;
                }
            }
            if(pasa){
                window.document.forma.submit();
            }else{
                alert("Debe seleccionar alguna tarjeta");
            }
        } else {
            document.getElementById("getFile").disabled = false;
        }
    }
    
    function regresar() {
        window.document.forma.command.value='altaPersonalizar';
        window.document.forma.submit();
    }
</script>
</head>
<body>
    <br><jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Genera Personalizaci&oacute;n Tarjeta</h2>
    <%=HTMLHelper.displayNotifications(notificaciones)%>
    <form name="forma" action="admin" method="post">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="banco" value="<%=banco%>">
        <input type="hidden" name="sucursal" value="<%=sucursal%>">
        <input type="hidden" name="tipo" value="LAYOUTPERSONAT">
        <input type="hidden" name="filename" value="">
        <table border="1" width="90%" align="center" >
            <!--<tr bgcolor="#6AD500">VERDE-->
            <tr bgcolor="#545454"><!--GRIS-->
            <!--<tr bgcolor="#DE0082">ROSA-->
                <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                <td class="whitetext" align="center">Sucursal</td>
                <td class="whitetext" align="center">Tarjeta</td>
                <td class="whitetext" align="center">Cliente</td>
                <td class="whitetext" align="center">Nombre</td>
                <td class="whitetext" align="center">RFC</td>
                <td class="whitetext" align="center">Fecha Nacimiento</td>
            </tr>
            <%for(TarjetasVO tarjeta : arrTarjetas) {%>
            <tr>
                <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked/></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getSucursalVO().getNombre())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getTarjeta())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getClienteVO().getIdCliente())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getClienteVO().getNombre()+" "+tarjeta.getClienteVO().getaPaterno()+" "+tarjeta.getClienteVO().getaMaterno())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getClienteVO().getRfc())%></td>
                <td align="right"><%=HTMLHelper.displayField(tarjeta.getClienteVO().getFechaNacimiento())%></td>
                <input type="hidden" name="nomSucursal<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getSucursalVO().getNombre()%>">
                <input type="hidden" name="numTarjeta<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getTarjeta()%>">
                <input type="hidden" name="numCliente<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getIdCliente()%>">
                <input type="hidden" name="nombre<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getNombre()%>">
                <input type="hidden" name="paterno<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getaPaterno()%>">
                <input type="hidden" name="materno<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getaMaterno()%>">
                <input type="hidden" name="rfc<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getRfc()%>">
                <input type="hidden" name="fechaNac<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getClienteVO().getFechaNacimiento()%>">
            </tr>
            <%  i++;
            }%>
        </table>
        <input type="hidden" name="numRegistros" id="numRegistros" value="<%=i%>"/>
        <table width="90%" border="0" cellspacing="2" class="maintitle">
            <tr>
                <td height="46" colspan="2" align="right">
                    <input type="button" name="getFile" id="getFile" onClick="generaTarjetas(<%=numTarjetas%>)" value="Genera Archivo">
                    <input type="button" id="botonRegresar" onclick="regresar()" value="Regresar">
                </td>
            </tr>
        </table>
    </form>
</center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</body>
</html>

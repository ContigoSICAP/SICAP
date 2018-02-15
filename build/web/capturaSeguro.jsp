<%@page import="com.sicap.clientes.dao.OrdenDePagoDAO"%>
<%@page import="com.sicap.clientes.dao.AseguradosDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
    int numeroBeneficiarios = 0;
    int numAsegurados = 0;
    SegurosVO seguro = new SegurosVO();
    //int porcentajeTotal = 0;
    List<BeneficiarioVO> listBeneficiarios = null;
    BeneficiarioVO beneficiario = new BeneficiarioVO();
    List<AseguradosVO> listAsegurados = null;
    AseguradosVO asegurado = new AseguradosVO();
    TreeMap catSumaAsegurada = CatalogoHelper.getCatalogoSumaAsegurada();
    TreeMap catModulos = CatalogoHelper.getCatalogoModulos();
    TreeMap catContratacion = CatalogoHelper.getCatalogoBinario();
    TreeMap catRelacion = CatalogoHelper.getCatalogoRelacion();
    TreeMap catParentesco = CatalogoHelper.getCatalogoParentesco();
    TreeMap catBanco = null;
    TreeMap catCuenta = null;
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
    boolean aceptaFunerario = false;
    int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    SolicitudVO solicitud = new SolicitudVO();
    String estatusBoton = "";
    int sizeODP = 0;
    OrdenDePagoDAO ordenPagoDAO = new OrdenDePagoDAO();
    // Seguro financiado, variable para ocultar los campos si son clientes anteriores
    boolean ocultaCampos = true;
    if (cliente.solicitudes != null && cliente.solicitudes.length > 0) {
        solicitud = cliente.solicitudes[indiceSolicitud];
        if (solicitud.seguro != null) {
            seguro = solicitud.seguro;
            ocultaCampos = SegurosUtil.validarFechaDeadLineSegF(seguro.fechaCaptura);
            if (!ocultaCampos) {
               catBanco = CatalogoHelper.getCatalogoBancoSeguro();
               catCuenta = CatalogoHelper.getCatalogoCuentaSeguro();
            }
                
            listAsegurados = seguro.asegurados;
            numAsegurados = listAsegurados.size();
            listBeneficiarios = seguro.beneficiarios;
            numeroBeneficiarios = seguro.beneficiarios.size();
            if (solicitud.enGarantia == 3) {
                estatusBoton = "disabled";
                if(request.isUserInRole("CORPORATIVO")==true)
                    estatusBoton = "enable";
            }
            
        } else {
            if (solicitud.estatus != ClientesConstants.SOLICITUD_AUTORIZADA && solicitud.tipoOperacion != ClientesConstants.MICROCREDITO) {
                notificaciones = new Notification[1];
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La solicitud debe encontrarse autorizada para capturar el seguro de vida");
                estatusBoton = "disabled";
            } else if (notificaciones == null) {
                if (solicitud.enGarantia == 3) {
                    estatusBoton = "disabled";
                    if (request.isUserInRole("CORPORATIVO")==true)
                        estatusBoton = "enable";
                } else {
                    notificaciones = new Notification[1];
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La información capturada no podrá ser modificada, verifiquela cuidadosamente");
                }
            }
        }
    }
    sizeODP=ordenPagoDAO.getSizeODPSeguro(cliente.idCliente, idSolicitud);
    aceptaFunerario = CatalogoHelper.esSucursalFunerario(cliente.idSucursal);
    if (!aceptaFunerario){
        catModulos.remove(2);
      }

%>

<html>
    <head>   
        <title>Captura Seguro de Vida</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function registraSeguroCliente(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value='registraSeguroCliente';
                //var respuesta = confirm('Recuerde que los datos no podrán ser modificados ¿desea guardar esta información?');
                //if ( respuesta ){
                    if ( window.document.forma.contratacion.value==0 ){
                        alert("Debe seleccionar una opción para el campo 'Contratación'");
                        document.getElementById("td").disabled = false;
                        return false;
                    //}else if ( window.document.forma.contratacion.value==1 ){
                    }else{
                        if ( window.document.forma.sumaAsegurada.value==0 ){
                            alert("Debe indicar la suma asegurada");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.modulos.value==0 ){
                            alert("Debe indicar el tipo se seguro");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if(window.document.forma.contratacion.value==1 && window.document.forma.fechaFirma.value==''){
                            alert("Debe capturar la Fecha de firma de la poliza");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.fechaFirma.value!='' && window.document.forma.contratacion.value==1){
                            if ( !esFechaValida(window.document.forma.fechaFirma, false) ){
                                alert("La Fecha de firma es inválida");
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                        }
                        // Validaciones de banco y cuenta de acuerdo al deadline
                        var ocultaCampos = <%=ocultaCampos%>;
                        if (!ocultaCampos) {
                            if(window.document.forma.contratacion.value==1 && window.document.forma.banco.value==0){
                                alert("Debe capturar el Banco de Deposito");
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                            if(window.document.forma.contratacion.value==1 && window.document.forma.cuenta.value==0){
                                alert("Debe capturar la Cuenta de Deposito");
                                document.getElementById("td").disabled = false;
                                return false;
                            }

                            //CUENTAS BANCOMER
                            if(window.document.forma.banco.value==1){
                                if(window.document.forma.cuenta.value!=1 && window.document.forma.cuenta.value!=7 ){
                                    alert("La Cuenta seleccionada no es de Bancomer");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }
                            //CUENTAS BANORTE
                            if(window.document.forma.banco.value==2){
                                if(window.document.forma.cuenta.value!=2 && window.document.forma.cuenta.value!=3){
                                    alert("La Cuenta seleccionada no es de Banorte");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }
                            //CUENTAS BANAMEX
                            if(window.document.forma.banco.value==3){
                                if(window.document.forma.cuenta.value!=4){
                                    alert("La Cuenta seleccionada no es de Banamex");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }
                            //CUENTAS SCOTIABANK
                            if(window.document.forma.banco.value==4){
                                if(window.document.forma.cuenta.value!=5){
                                    alert("La Cuenta seleccionada no es de Scotiabank");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }
                            //CUENTAS BANBAJIO
                            if(window.document.forma.banco.value==5){
                                if(window.document.forma.cuenta.value!=6){
                                    alert("La Cuenta seleccionada no es de BanBajio");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }
                        } // fin
                        if(window.document.forma.contratacion.value==1){
                            if ( !beneficiariosCorrectos() || !porcentajesCorrectos() || !aseguradosCorrectos()){
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                        }
                        if ( window.document.forma.comentarios.value.length>300 ){
                            alert("Los comentarios no deben exceder los 300 caracteres");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                    //}else{
                        if ( window.document.forma.contratacion.value==2 && window.document.forma.comentarios.value.length==0 ){
                            alert("Debe capturar el campo Comentarios");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                    //}
                    document.body.style.cursor = "wait";
                    if(confirm("¿Está seguro de registrar el seguro para la solicitud "+<%=solicitud.getIdSolicitud()%>+" ?")){
                        window.document.forma.submit();
                    }
                    else {
                        return false;
                    }
                }
            }

            function beneficiariosCorrectos(){
                var i=0;
                var ambos=false;
                for ( i ; i<2 ; i++ ){
                    if ( validarBeneficiario(i) ){
                        
                        
                        if ( window.document.forma.nomBeneficiario[i].value=='' ){
                            alert("Debe capturar el nombre del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.apPaterBene[i].value=='' ){
                            alert("Debe capturar el apellido paterno del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.apMaterBene[i].value=='' ){
                            alert("Debe capturar apellido materno del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.relacion[i].value==0 ){
                            alert("Debe seleccionar la relacion del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if ( window.document.forma.relacion[i].value==11 &&  window.document.forma.especificacion[i].value==''){
                            alert("Debe especificar la relación del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }				
                        if ( window.document.forma.porcentaje[i].value!='' ){
                            if ( !esFormatoPorcentaje(window.document.forma.porcentaje[i].value) ){
                                alert("El porcentaje del beneficiario es inválido "+(i+1));
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                        }else{
                            alert("Debe capturar porcentaje del beneficiario "+(i+1));
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                        if(i==0){
                            if(window.document.forma.Fecha6.value!=''){
                                if(!esEdadValidaSeguro(window.document.forma.Fecha6.value, 'beneficiario')){
                                    alert("El primer beneficiario no es mayor de edad");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }else{
                                alert("Primer beneficiario sin fecha de nacimiento");
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                            if(!formatofechavalido(window.document.forma.Fecha6.value)){
                                var nombre = window.document.forma.nomBeneficiario[i].value +" "+ 
                                         window.document.forma.apPaterBene[i].value +" "+ window.document.forma.apMaterBene[i].value 
                                alert("El Formato de la fecha para el beneficiario "+nombre+" es incorrecto este debe ser 'dd/mm/yyyy'");
                                return false;
                            }
                            if(!validafecha(window.document.forma.Fecha6.value)){
                                var nombre = window.document.forma.nomBeneficiario[i].value +" "+ 
                                         window.document.forma.apPaterBene[i].value +" "+ window.document.forma.apMaterBene[i].value 
                                alert("La fecha de nacimiento del beneficiario "+nombre +" es invalida");
                                return false;
                            }
                        }else{
                            if(window.document.forma.Fecha7.value!=''){
                                if(!esEdadValidaSeguro(window.document.forma.Fecha7.value, 'beneficiario')){
                                    alert("El segundo beneficiario no es mayor de edad");
                                    document.getElementById("td").disabled = false;
                                    return false;
                                }
                            }else{
                                alert("Segundo beneficiario sin fecha de nacimiento");
                                document.getElementById("td").disabled = false;
                                return false;
                            }
                            if(!formatofechavalido(window.document.forma.Fecha7.value)){                          
                                alert("El Formato de la fecha es incorrecto este debe ser 'dd/mm/yyyy'");
                                return false;
                            }
                            if(!validafecha(window.document.forma.Fecha7.value)){
                                alert("La fecha es invalida");
                                return false;
                            }
                        }
                        if(i==1)
                            ambos=true;
                    }else{
                        i=2;
                    }
                }
                if(ambos){
                    if(window.document.forma.relacion[0].value==window.document.forma.relacion[1].value && 
                        (window.document.forma.relacion[0].value==1 || window.document.forma.relacion[0].value==2 || 
                        window.document.forma.relacion[0].value==9 || window.document.forma.relacion[0].value==10)){
                        alert("No pueden tener ambos beneficiarios la misma relación");
                        document.getElementById("td").disabled = false;
                        return false;
                    }
                }
                return true;
            }

            function validarBeneficiario(indice){
                if (window.document.forma.nomBeneficiario[indice].value!='' || 
                    window.document.forma.apPaterBene[indice].value!='' || 
                    window.document.forma.apMaterBene[indice].value!='' || 
                    window.document.forma.especificacion[indice].value!=0 || 
                    window.document.forma.porcentaje[indice].value!='' ){
                    return true;
                }else{
                    if(indice<1){
                        alert("Debe capturar por lo menos un Beneficiario");
                        document.getElementById("td").disabled = false;
                        return false;
                    }
                }
            }

            function porcentajesCorrectos(){
                var i = 0;
                var total = 0;
                for ( i ; i<2 ; i++ ){
                    if ( window.document.forma.porcentaje[i].value!='' ){
                        total = total + parseInt( window.document.forma.porcentaje[i].value );
                    }
                }
                //window.document.forma.porcentajeTotal.value = total;
                if ( total==100 ){
                    return true;
                }
                alert("La suma de los porcentajes debe ser igual a 100");
                document.getElementById("td").disabled = false;
                return false;
            }
            
            function aseguradosCorrectos(){
                
                var conyuge=0;
                var numAsegurados=0;
                var fecNacimiento;
                for(var i = 0; i<5; i++){
                    if(validaAsegurado(i)){
                        numAsegurados++;
                        switch (i){
                            case 0:                                   
                                fecNacimiento = window.document.forma.Fecha1.value;
                                break;                            
                            case 1:
                                fecNacimiento = window.document.forma.Fecha2.value;
                                break;
                            case 2:
                                fecNacimiento = window.document.forma.Fecha3.value;
                                break;
                            case 3:
                                fecNacimiento = window.document.forma.Fecha4.value;
                                break;
                            case 4:
                                fecNacimiento = window.document.forma.Fecha5.value;
                                break;
                            case 5:
                                fecNacimiento = window.document.forma.Fecha8.value;
                                break;
                            case 6:
                                fecNacimiento = window.document.forma.Fecha9.value;
                                break;
                            case 7:
                                fecNacimiento = window.document.forma.Fecha10.value;
                                break;
                        }
                        if(window.document.forma.parentesco[i].value==1 || window.document.forma.parentesco[i].value==2){
                            conyuge++;
                            /*if(esEdadValidaSeguro(fecNacimiento, 'asegConyuge')){
                                alert("El conyuge no cumple con la edad requerida (menos de 71 años)");
                                return false;
                            }*/
                        }
                        if(!formatofechavalido(fecNacimiento)){
                            var nombre = window.document.forma.nomAsegurado[i].value +" "+ 
                                         window.document.forma.apPaterAseg[i].value +" "+ window.document.forma.apMaterAseg[i].value 
                            alert("El Formato de la fecha para el asegurado "+nombre+"  incorrecto este debe ser 'dd/mm/yyyy'");                            
                            return false;                            
                        }
                        /*else {
                            var texfecha = window.document.forma.Fecha[i];
                            //texfecha.style.backgroundColor = "white";
                            
                        }*/
                        if(!validafecha(fecNacimiento))
                        {
                            var nombre = window.document.forma.nomAsegurado[i].value  +" "+ 
                                         window.document.forma.apPaterAseg[i].value +" "+ window.document.forma.apMaterAseg[i].value
                            alert("La fecha de nacimiento del asegurado "+nombre+" es invalida");
                            return false;
                        }
                        /*else {
                            var texfecha = window.document.forma.Fecha[i];
                            texfecha.style.backgroundColor = "white";
                            
                        }*/
                        /*if(window.document.forma.parentesco[i].value==3 || window.document.forma.parentesco[i].value==4){
                            if(esEdadValidaSeguro(fecNacimiento, 'asegHijo')){
                                alert("El hijo no cumple con la edad requerida (menor de 21 años)");
                                return false;
                            }
                        }*/
                        if(conyuge>1){
                            alert("Hay mas de un conyuge en los asegurados");
                            document.getElementById("td").disabled = false;
                            return false;
                        }
                    }
                }
                if(numAsegurados==0){
                    var confirma = confirm("No existe información de asegurados. Desea continuar.");
                    document.getElementById("td").disabled = false;
                    return confirma;
                }
                return true;
            }
            
            function validaAsegurado(indice){
                if (window.document.forma.nomAsegurado[indice].value!='' || 
                    window.document.forma.apPaterAseg[indice].value!='' || 
                    window.document.forma.apMaterAseg[indice].value!='' ||
                    window.document.forma.parentesco[indice].value!=0){
                    return true;
                }
                return false;
            }
            function desabilita(){
                window.document.forma.action="";
                var i=0;
                var bol = true;
                if(window.document.forma.contratacion.value==2)
                    bol = true;
                else
                    bol = false;
                window.document.forma.fechaFirma.disabled=bol;
                window.document.forma.banco.disabled=bol;
                window.document.forma.cuenta.disabled=bol;
                for(i=0; i<8; i++){
                    window.document.forma.nomAsegurado[i].disabled=bol;
                    window.document.forma.apPaterAseg[i].disabled=bol;
                    window.document.forma.apMaterAseg[i].disabled=bol;
                    window.document.forma.fecNacimiento[i].disabled=bol;
                    window.document.forma.parentesco[i].disabled=bol;
                }
                for(i=0; i<2; i++){
                    window.document.forma.nomBeneficiario[i].disabled=bol;
                    window.document.forma.apPaterBene[i].disabled=bol;
                    window.document.forma.apMaterBene[i].disabled=bol;
                    window.document.forma.fecNaciBene[i].disabled=bol;
                    window.document.forma.relacion[i].disabled=bol;
                    window.document.forma.especificacion[i].disabled=bol;
                    window.document.forma.porcentaje[i].disabled=bol;
                }
            }
            function muestraDocumento(tipo){
                window.document.forma.command.value='muestraDocumento';
		//window.document.forma.target="_blank";
		window.document.forma.tipo.value=tipo;
		window.document.forma.submit();
                
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <body leftmargin="0" topmargin="0" onload="desabilita();">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuIzquierdo.jsp" flush="true"/>
        <center>
            <form name="forma" action="controller" method="post">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="tipo" value="">
                <input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
                <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
                <!-- INICIO DEL CODIGO ANTERIOR -->
                <table border="0" width="100%">
                    <tr>
                        <td align="center">
                            <h3>Alta Seguro</h3>
                            <%=HTMLHelper.displayNotifications(notificaciones)%>
                            <table border="0" cellpadding="0" width="100%">
                                <%-- if (seguro.numSeguro != 0 && seguro.contratacion == 1) {--%>
                                <!--<tr> 
                                    <td width="50%" align="center" colspan="2">
                                        <a href="/Sicap/generaPolizaSeguro.jsp?idSolicitud=<%--=solicitud.idSolicitud--%>">Imprimir p&oacute;liza</a><br><br>
                                        <a href="/generaPolizaSeguro.jsp?idSolicitud=<%--=solicitud.idSolicitud--%>">Imprimir p&oacute;liza</a><br><br>
                                    </td>  
                                </tr>--> 
                                <%--}--%>
                                <% if(sizeODP>0){%>
                                    <tr>
                                        <td width="50%" align="center" colspan="2">
                                            <a href="#" onClick="muestraDocumento('ORDENPAGOSEGURO')">Consulta orden de pago</a><br><br>
                                        </td>
                                    </tr>
                                <%}%>
                                <tr>
                                    <td width="50%" align="right">Suma Asegurada</td>
                                    <td width="50%">  
                                        <select name="sumaAsegurada" size="1">
                                            <%=HTMLHelper.displayCombo(catSumaAsegurada, seguro.sumaAsegurada)%>
                                        </select>
                                    </td>
                                </tr>

                                <tr>
                                    <!--<td width="50%" align="right">N&uacute;mero de m&oacute;dulos</td>-->
                                    <td width="50%" align="right">Tipo Seguro</td>
                                    <td width="50%">
                                        <select name="modulos" size="1">
                                            <%=HTMLHelper.displayCombo(catModulos, seguro.modulos)%>
                                        </select>                                                                            
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Contrataci&oacute;n</td>
                                    <td width="50%">  
                                        <%if(solicitud.subproducto == ClientesConstants.ID_INTERCICLO){%>
                                        <select name="contratacion" id="contratacion" size="1" onKeyPress="return submitenter(this,event)" onfocus="this.oldvalue=this.value;this.blur();" onchange="this.value=this.oldvalue;">
                                            <%=HTMLHelper.displayCombo(catContratacion, 1)%>
                                        </select>
                                        <%} else {%>
                                        <select name="contratacion" id="contratacion" size="1" onchange="desabilita();">
                                            <%=HTMLHelper.displayCombo(catContratacion, seguro.contratacion)%>
                                        </select>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Fecha de firma de p&oacute;liza</td>
                                    <td width="50%">
                                        <input type="text" name="fechaFirma" id="fechaFirma" size="10" maxlength="10" value="<%=HTMLHelper.displayField(seguro.fechaFirma)%>" onKeyPress="return submitenter(this,event)">(dd/mm/aaaa)
                                    </td>
                                </tr>
                                <%-- RAD Seguro Financiero v1.1 Se eliminan los siguientes combos
                                     @since 03-08-2017
                                     @author axxis
                                --%>
                                <tr style="<%= ocultaCampos? "display:none":"" %>">
                                    <td width="50%" align="right">Banco de Depósito</td>
                                    <td width="50%">  
                                        <select name="banco" id="banco" size="1">
                                            <%=HTMLHelper.displayCombo(catBanco, seguro.banco)%>
                                        </select>
                                    </td>
                                </tr>
                                <tr style="<%= ocultaCampos? "display:none":"" %>">
                                    <td width="50%" align="right">No. de Cuenta</td>
                                    <td width="50%">  
                                        <select name="cuenta" id="cuenta" size="1">
                                            <%=HTMLHelper.displayCombo(catCuenta, seguro.cuenta)%>
                                        </select>
                                    </td>
                                </tr>
                                <tr><td colspan="2"><br></td></tr>
                                <tr>
                                    <td colspan="2">
                                        <table border="1" align="center" cellpadding="1" width="40%">
                                            <tr>
                                                <td align="center" colspan="6"><b>Asegurados</b></td>
                                            </tr>
                                            <tr>
                                                <td align="center">No.</td>
                                                <td align="center">Nombre(s)</td>
                                                <td align="center">Apellido Paterno</td>
                                                <td align="center">Apellido Materno</td>
                                                <td align="center">Fecha Nacimiento</td>
                                                <td align="center">Parentesco</td>
                                            </tr>
                                            <%
                                            if(numAsegurados>0)
                                                asegurado = listAsegurados.get(0);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr>
                                                <td align="center">1&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha1" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>1)
                                                asegurado = listAsegurados.get(1);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>2&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha2" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>2)
                                                asegurado = listAsegurados.get(2);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>3&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha3" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>3)
                                                asegurado = listAsegurados.get(3);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>4&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha4" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>4)
                                                asegurado = listAsegurados.get(4);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>5&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha5" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>5)
                                                asegurado = listAsegurados.get(5);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>6&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha8" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>6)
                                                asegurado = listAsegurados.get(6);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>7&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha9" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                            <%
                                            if(numAsegurados>7)
                                                asegurado = listAsegurados.get(7);
                                            else
                                                asegurado = new AseguradosVO();
                                            %>
                                            <tr align="center">
                                                <td>8&nbsp;</td>
                                                <td><input type="text" name="nomAsegurado" id="nomAsegurado" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getNombre())%>"></td>
                                                <td><input type="text" name="apPaterAseg" id="apPaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApPaterno())%>"></td>
                                                <td><input type="text" name="apMaterAseg" id="apMaterAseg" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(asegurado.getApMaterno())%>"></td>
                                                <td><input type="text" name="fecNacimiento" id="Fecha10" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(asegurado.getFecNacimiento())%>"></td>
                                                <td><select name="parentesco" id="parentesco" size="1">
                                                        <%=HTMLHelper.displayCombo(catParentesco, asegurado.getParentesco())%>
                                                </select></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td colspan="2"><br></td></tr>
                                <tr>
                                    <td colspan="2">
                                        <table border="1" align="center" cellpadding="1" width="70%">
                                            <tr>
                                                <td align="center" colspan="8"><b>Beneficiarios</b></td>
                                            </tr>
                                            <tr>
                                                <td align="center">No.</td>
                                                <td align="center">Nombre(s)</td>
                                                <td align="center">Apellido Paterno</td>
                                                <td align="center">Apellido Materno</td>
                                                <td align="center" width="10%">Fecha Nacimiento</td>
                                                <td align="center">Relacion</td>
                                                <td align="center">Especifique</td>
                                                <td align="center">Porcentaje</td>
                                            </tr>
                                            <%
                                            if(numeroBeneficiarios>0)
                                                beneficiario = listBeneficiarios.get(0);
                                            else
                                                beneficiario = new BeneficiarioVO();
                                            %>
                                            <tr>
                                                <td align="center">1&nbsp;</td>
                                                <td><input type="text" name="nomBeneficiario" id="nomBeneficiario" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getNombre())%>">
                                                <td><input type="text" name="apPaterBene" id="apPaterBene" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getaPaterno())%>">
                                                <td><input type="text" name="apMaterBene" id="apMaterBene" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getaMaterno())%>">
                                                <td><input type="text" name="fecNaciBene" id="Fecha6" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(beneficiario.getFechaNacimiento())%>">
                                                <td><select name="relacion" size="1">
                                                        <%=HTMLHelper.displayCombo(catRelacion, beneficiario.getRelacion())%>
                                                </select></td>
                                                <td><input type="text" name="especificacion" id="especificacion" size="20%" maxlength="40" value="<%=HTMLHelper.displayField(beneficiario.getOtraRelacion())%>">
                                                <td><input type="text" name="porcentaje" id="porcentaje" size="5%" maxlength="3" value="<%=HTMLHelper.displayField(beneficiario.getPorcentaje())%>">
                                            </tr>
                                            <%
                                            if (numeroBeneficiarios>1)
                                                beneficiario = listBeneficiarios.get(1);
                                            else
                                                beneficiario = new BeneficiarioVO();
                                            %>
                                            <tr>
                                                <td align="center">2&nbsp;</td>
                                                <td><input type="text" name="nomBeneficiario" id="nomBeneficiario" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getNombre())%>">
                                                <td><input type="text" name="apPaterBene" id="apPaterBene" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getaPaterno())%>">
                                                <td><input type="text" name="apMaterBene" id="apMaterBene" size="23%" maxlength="70" value="<%=HTMLHelper.displayField(beneficiario.getaMaterno())%>">
                                                <td><input type="text" name="fecNaciBene" id="Fecha7" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(beneficiario.getFechaNacimiento())%>">
                                                <td><select name="relacion" size="1">
                                                        <%=HTMLHelper.displayCombo(catRelacion, beneficiario.getRelacion())%>
                                                </select></td>
                                                <td><input type="text" name="especificacion" id="especificacion" size="20%" maxlength="40" value="<%=HTMLHelper.displayField(beneficiario.getOtraRelacion())%>">
                                                <td><input type="text" name="porcentaje" id="porcentaje" size="5%" maxlength="3" value="<%=HTMLHelper.displayField(beneficiario.getPorcentaje())%>">
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td colspan="2"><br></td></tr>
                                <tr>
                                    <!--<td width="50%" align="right">Comentarios</td>-->
                                    <td width="50%" colspan="2" align="center"><b>Comentarios</b><br>
                                        <textarea cols="100" rows="2" name="comentarios"><%=HTMLHelper.displayField(seguro.comentarios)%></textarea>
                                    </td>
                                </tr>
                                <%-- 
                                if(seguro.numSeguro>0){
                                %>
                                    <input type="hidden" name="numSeguro" value="<%=seguro.numSeguro%>"
                                <%
                                }--%>
                                <tr>
                                    <td align="center" colspan="2" id="td">
                                        <br><input type="button" value="Enviar" onClick="registraSeguroCliente()" <%=estatusBoton%>>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </center>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>

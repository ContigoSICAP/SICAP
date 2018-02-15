<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="com.sicap.clientes.util.FormatUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.vo.CatalogoVO"%>
<%@page import="java.util.List"%>
<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.sicap.clientes.dao.OrdenDePagoDAO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="java.util.Vector"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<html>
    <head>
        <title>Asiganción manual de cartera a Fondeador</title>
        <script>
            function consultaSaldos(){
                if (window.document.forma.idEquipo.value==0||window.document.forma.idEquipo.value==''|| !esEntero(window.document.forma.idEquipo.value)){
                    alert('Número de equipo inválido');
                    return false;
		}
                else if (window.document.forma.idCiclo.value==0||window.document.forma.idCiclo.value==''|| !esEntero(window.document.forma.idCiclo.value)){
                    alert('Número de ciclo inválido');
                    return false;
                }
                else {
                    window.document.forma.command.value='consultaSaldosAsigCartera';
                    window.document.forma.submit();
                }
            }
            
            function validaNuevoFondeador(){
                var idFondeadorGarantiaActual=window.document.forma.hiddenIdFondeador.value;
                var idIbFondeadorActual=window.document.forma.hiddenIdIbFondeador.value;
                
                if(window.document.forma.idFondeadorNuevo.value == 0){
                    alert("Seleccione un fondeador");
                    return false;
                }
                
                if(idFondeadorGarantiaActual == 0 
                        && idIbFondeadorActual== <%= ClientesConstants.ID_FONDEADOR_CREDITO_REAL %> ){
                        //Validar para credito real
                        //alert(Fondeador actual es de credito real y aun no se asigna a cartera);
                        if(window.document.forma.idFondeadorNuevo.value==idIbFondeadorActual){
                            
                            alert('Fondeador a asignar es el mismo que el anterior');
                            return false;
                            
                        }else{
                            return true;
                            
                        }
                
                }else{
                    
                    if(window.document.forma.idFondeadorNuevo.value==window.document.forma.hiddenIdFondeador.value){
                        alert("Fondeador a asignar es el mismo que el anterior");
                        //alert("El fondeador a garantizar es el mismo fondeador que el actual\nFondeador nuevo: "+window.document.forma.idFondeadorNuevo.value+"\n Fondearo actual: "+window.document.forma.hiddenIdFondeador.value  );
                        return false;
                    }else if(window.document.forma.hiddenIdFondeador.value==<%= ClientesConstants.ID_FONDEADOR_BURSA %> 
                            || window.document.forma.idFondeadorNuevo.value==<%= ClientesConstants.ID_FONDEADOR_BURSA %> ){
                        
                        //Validar bursa
                        if(window.document.forma.hiddenIdFondeador.value==<%= ClientesConstants.ID_FONDEADOR_BURSA %>){
                            //Bursa es fondeador actual(viejo)
                            if(window.document.forma.idFondeadorNuevo.value==<%= ClientesConstants.ID_FONDEADOR_CREDITO_REAL %>){
                                return true; 
                            }else{
                                alert("Si el fondeador actual es BURSA el fondeador nuevo tiene que ser CREDITO REAL");
                                return false; 
                            }
                            
                        }else{
                            //Bursa es nuevo fondeador
                            if(idIbFondeadorActual==<%= ClientesConstants.ID_FONDEADOR_CREDITO_REAL %> 
                            && idFondeadorGarantiaActual == 0){
                                return true; 
                            }else{
                                alert("Sólo se puede asignar cartera a bursa de Credito Real");
                                return false; 
                            }
                        }
                        
                        

                    }else{
                        //alert("Fondeador nuevo: "+window.document.forma.idFondeadorNuevo.value+"\n Fondeador actual: "+window.document.forma.hiddenIdFondeador.value  );
                        return true;
                    }
                    
                }
                
                
                
            }
            
            function nuevaConsulta(){
                window.document.forma.command.value='asignaCarteraGarantia';
                window.document.forma.submit();
            }
            
            //Actualizar saldo
            function actualizaFondeadorCartera(){
                //debugger;
                if(validaNuevoFondeador()){
                    var msgConfirm="";
                    var idFondeador=window.document.forma.hiddenIdFondeador.value;
                    var idIbFondeador=window.document.forma.hiddenIdIbFondeador.value;
                    //alert('idFondeador: '+idFondeador +' idIbFondeador: '+idIbFondeador +' ');
                    
                    if(idFondeador == 0 
                        && idIbFondeador== <%= ClientesConstants.ID_FONDEADOR_CREDITO_REAL %> ){
                    
                    //ES credito_real por lo que no aplica validar montos
                    msgConfirm="¿Deseas asignar como cartera a garantizar el saldo de Crédito Real?";
                    
                    }else{
                    
                        var pct= parseFloat(window.document.getElementById('pct_'+idFondeador).value);
                        var saldoFond = parseFloat(window.document.getElementById('saldoFond_'+idFondeador).value);
                        var saldoGarantia = parseFloat(window.document.getElementById('saldoGarantia_'+idFondeador).value);
                        var saldoCapital = window.document.forma.saldoCapital.value;

                        //alert("ID : "+idFondeador +"\nPCT: "+pct+"\nSaldo: "+saldoFond+"\nGarantizado: "+saldoGarantia+"\nsaldoCredito: "+saldoCapital+"\nsaldoAgarantiza: "+ (saldoFond*(1+pct)));
    //                    return false;
    //                    
                        //Si saldo en Garantia menos SaldoCapital del credito es menor que el minimos garantizado
                        if( (saldoGarantia-saldoCapital) < ( saldoFond*(1+pct))  ){
                            msgConfirm="¿Al hacer la asignación el saldo total será menor al que se debe tener en garantía.\nDeseas actualizar el Fondeador a garantizar?";                        
                        }else{
                            msgConfirm="¿Deseas actualizar el Fondeador a garantizar?";
                        }
                    }
                    
                    if(confirm(msgConfirm)){                    
                        window.document.getElementById("botonActualiza").disabled=true;                
                        window.document.forma.command.value='actualizaFondeadorCartera';
                        window.document.forma.submit();
                    } else {
                        return false;
                    }
                    
                }
              
            }
            function regresar(){
                window.document.forma.command.value='administracionFondeadores';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%
        //TreeMap catCiclos = CatalogoHelper.getCatalogo("c_estatus_ciclo");
        //TreeMap catSaldos = CatalogoHelper.getCatalogo("c_estatus_saldos");
        Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
        TreeMap catFondeador = CatalogoHelper.getCatalogoFondeadorAsigancionCartera();
        int idFondeador = 0;
        //CicloGrupalVO ciclo = (CicloGrupalVO)request.getAttribute("CICLO");
        SaldoIBSVO saldo = (SaldoIBSVO)request.getAttribute("SALDO");
        OrdenDePagoDAO ordenPagoDao = new OrdenDePagoDAO();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        FormatUtil fUtil = new FormatUtil();
        CatalogoDAO catDao= new CatalogoDAO();
        ArrayList<CatalogoVO> lstPorcentajes = catDao.getCatParamFondeador("PORCENTAJE_GARANTIA_FONDEADOR");
        ArrayList<CatalogoVO> lstSaldosFond = catDao.getCatParamFondeador("SALDO_FONDEADOR");
        ArrayList<CatalogoVO> lstSaldosGarantia = catDao.getCatParamFondeador("SALDO_GARANTIZADO");
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <h3>Asignación manual de cartera</h3>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
            <form name="forma" action="admin" method="POST">
               <input type="hidden" name="command" value="">
               
               <%if(saldo!=null){%>
                   
                  <!--Hiddens de catalogos con lainformacion de daldos fondeadores-->
                    <%for(CatalogoVO param: lstPorcentajes){%>
                        <input type="hidden"  id="pct_<%= param.getId() %>" name="pct_<%= param.getId() %>" value="<%=param.getDescripcion() %>">
                        
                    <%}%>
                    
                    <%for(CatalogoVO param: lstSaldosFond){%>
                        <input type="hidden"  id="saldoFond_<%= param.getId() %>" name="saldoFond_<%= param.getId() %>" value="<%=param.getDescripcion() %>">
                        
                    <%}%>
                    
                    <%for(CatalogoVO param: lstSaldosGarantia){%>
                        <input type="hidden"  id="saldoGarantia_<%= param.getId() %>" name="saldoGarantia_<%= param.getId() %>" value="<%=param.getDescripcion() %>">
                        
                    <%}%>
               
               
               <div>
                   <table border="1" cellpadding="0" cellspacing="0" width="35%">
                       <tr>
                           <td width="25%" align="right">Equipo</td>
                           <input type="hidden" name="idEquipo" value="<%=saldo.getIdClienteSICAP() %>">
                           <input type="hidden" name="idCiclo" value="<%=saldo.getIdSolicitudSICAP() %>">
                           <td width="40%" align="left">&nbsp; <%=saldo.getIdClienteSICAP()%></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Nombre</td>
                           <td width="45%" align="left">&nbsp; <%= saldo.getNombreCliente() %></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Sucursal</td>
                           <td width="45%" align="left">&nbsp; <%= saldo.getNombreSucursal() %></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Ciclo</td>
                           <td width="45%" align="left">&nbsp; <%= saldo.getIdSolicitudSICAP() %></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right"> Saldo </td>
                           <td width="45%" align="left">&nbsp; <%= formatter.format( saldo.getSaldoCapital() )%></td>
                           <input type="hidden" name="saldoCapital" value="<%=saldo.getSaldoCapital() %>">
                       </tr>
                       <tr>
                           <td width="20%" align="right">Saldo  </td>
                           <td width="45%" align="left">&nbsp;  <%= fUtil.formateaMoneda(saldo.getSaldoCapital() ) %> </td>
                           
                       </tr>
                       
                       <tr>
                           <td width="20%" align="right">Fondeador en garantía</td>
                           <td width="45%" align="left">&nbsp; <%= saldo.getFondeoGarantia() %></td>
                       </tr>
                       <tr>
                            <td width="20%" align="right">Nombre Fondeador en garantía </td>
                            <td width="45%" align="left">&nbsp; <%=HTMLHelper.getDescripcion(catFondeador, saldo.getFondeoGarantia() ) %></td>
                        </tr>
                       <br>
                       <br>
                   </table>
                       
                   <table>
                       
                       <input type="hidden" name="hiddenIdFondeador" value="<%= saldo.getFondeoGarantia() %>">
                       <input type="hidden" name="hiddenIdIbFondeador" value="<%= saldo.getFondeador() %>">
                       
                       <tr>
                           <td >&nbsp;</td>
                       </tr>
                       <tr>
                            <td align="right">Fondeador(nuevo) a garantizar:</td>
                            <td width="50%">
                                <select name="idFondeadorNuevo" size="1" >
                                    <%= HTMLHelper.displayCombo(catFondeador, idFondeador)%>
                                </select>
                            </td>
                       </tr>
                       
                       <tr>
                           <td >&nbsp;</td>
                       </tr>
                       
                       <tr>
                           <td align="center" colspan="2">
                               <br>
                               <%if( !catDao.ejecutandoCierre() ){%>
                                <input type="button" id="botonActualiza" value="Actualizar Cartera" onClick="actualizaFondeadorCartera()">
                               <%}%>
                               <input type="button" value="Nueva consulta" onClick="nuevaConsulta()">
                           </td>
                       </tr>
                   </table>
                </div>
               <%} else {%>
                <div>
                    <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                        <tr>
                            <td width="50%" height="10%" align="right">Equipo<br></td>
                            <td width="50%"><input type="text" name="idEquipo" size="10" maxlength="10"></td>
                        </tr>
                        <tr>
                            <td width="50%" height="10%" align="right">Ciclo<br></td>
                            <td width="50%"><input type="text" name="idCiclo" size="10" maxlength="10"></td>
                        </tr>
                        <tr>
                            <td align="right"><br><input type="button" value="Consultar" onclick="consultaSaldos();"></td>
                            <td align="left"><br><input type="button" value="Regresar" onclick="regresar();"></td>
                        </tr>
                    </table>
                </div>
                <%}%>
            
        </center>
    </body>
</html>

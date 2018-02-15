
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.util.Convertidor"%>
<%@page import="java.sql.Date"%>
<%@page import="com.sicap.clientes.vo.BitacoraCicloVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.util.FechasUtil"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<!DOCTYPE html>
<%
    TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCicloCombo();
    TreeMap catSucursales = CatalogoHelper.getCatalogoSucursales();
    TreeMap catAnalistas = CatalogoHelper.getCatalogoAnalistas();
    TreeMap catSubProducto = CatalogoHelper.getCatalogoSubProductoGrupal();
    BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
    ArrayList<BitacoraCicloVO> registros = (ArrayList<BitacoraCicloVO>)request.getAttribute("REGISTROS");
    String fechaInicio = request.getParameter("fechaInicial");
    String fechaFin = request.getParameter("fechaFinal");
    int estatusCiclo = HTMLHelper.getParameterInt(request, "estatus");
    int idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
    int numEquipo = HTMLHelper.getParameterInt(request, "idEquipo");    
    int subProd = HTMLHelper.getParameterInt(request, "subproducto");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar cal = Calendar.getInstance();
    String FechaHoy = dateFormat.format(cal.getTime());
    String fechaAnterior= "";
    String analista = HTMLHelper.getParameterString(request, "analista");
    boolean fechaInvalida = false;
    int x=1;
    int j=0;
    //fechaAnterior = Convertidor.dateToString(FechasUtil.getRestarDias(Convertidor.stringToDate(FechaHoy,"dd/MM/yyyy"),3));
    //fechaInvalida = FechasUtil.esDiaInhabil(Convertidor.stringToDate(fechaAnterior,"dd/MM/yyyy"),(java.util.Date[])session.getAttribute("INHABILES"));
    do{        
        fechaAnterior = Convertidor.dateToString(FechasUtil.getRestarDias(Convertidor.stringToDate(FechaHoy,"dd/MM/yyyy"),x+j));
        fechaInvalida = FechasUtil.esDiaInhabil(Convertidor.stringToDate(fechaAnterior,"dd/MM/yyyy"),(java.util.Date[])session.getAttribute("INHABILES"));
        if(fechaInvalida)
            j++;
        else
            x++;
    }while(x<=3);
    
%>
<html>
    <head>
        <title>Consulta Ciclo Estatus</title>
        <script>
            function regresar(){
                window.document.forma.command.value='administraGrupos';
                window.document.forma.submit();
            }
            function consultaEstatus(){
                document.getElementById("Consultar").disabled = true;
                document.getElementById("Regresar").disabled = true;
                document.body.style.cursor = "wait";
                if((window.document.forma.fechaInicial.value==''&&window.document.forma.fechaFinal.value!='')||(window.document.forma.fechaInicial.value!=''&&window.document.forma.fechaFinal.value=='')){
                    alert("Rango de fechas inválido");
                    document.getElementById("Consultar").disabled = false;
                    document.getElementById("Regresar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                } else if((window.document.forma.numEquipo.value!='')&&(!esEntero(window.document.forma.numEquipo.value))){
                    alert("Número de equipo inválido");
                    document.getElementById("Consultar").disabled = false;
                    document.getElementById("Regresar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                    
                } else{
                    window.document.forma.command.value='consultaEstatusCiclo';
                    window.document.forma.submit();
                }
            }
            function asignarEquipo(numEquipo, numCiclo, estatus, indice, semDisp, idCredito){
                if(document.getElementById("analistaAsignacion"+indice).value!=''){
                    if (window.document.forma.estatus.value==0){
                         alert("Limite la busqueda por los estatus de Analisis y Asignacion");
                        return false;
                    }
                    if(confirm("¿Está seguro de asignar el equipo a "+document.getElementById("analistaAsignacion"+indice).value+"?")){
                        window.document.forma.analistaAsignar.value=document.getElementById("analistaAsignacion"+indice).value;
                        window.document.forma.idGrupo.value=numEquipo;
                        window.document.forma.idCiclo.value=numCiclo;
                        window.document.forma.estatusAsignar.value=estatus;
                        window.document.forma.accion.value=1;
                        window.document.forma.semDisp.value=semDisp;
                        window.document.forma.idCredito.value=idCredito;
                        window.document.forma.command.value='cambiaEstatusCiclo';
                        window.document.forma.submit();
                    }
                } else {
                        alert("Favor de seleccionar un usuario");
                        return false;
                }
            }
            function analizarEquipo(numEquipo, numCiclo, estatus, semDisp, idCredito){
                if(estatus==8||estatus==6){
                    <%if(request.isUserInRole("ANALISIS_CREDITO")&&!request.isUserInRole("manager")){%>
                    <%--if(request.isUserInRole("ANALISIS_CREDITO")){--%>
                    if(confirm("¿Estás seguro de comenzar el proceso de análisis?")){
                        window.document.forma.idGrupo.value=numEquipo;
                        window.document.forma.idCiclo.value=numCiclo;
                        window.document.forma.estatusAsignar.value=estatus;
                        window.document.forma.accion.value=2;
                        window.document.forma.semDisp.value=semDisp;
                        window.document.forma.idCredito.value=idCredito;
                        window.document.forma.command.value='cambiaEstatusCiclo';
                        window.document.forma.submit();
                    }
                    <%} else{%>
                        window.document.forma.idGrupo.value=numEquipo;
                        window.document.forma.command.value='consultaDetalleGrupo';
                        window.document.forma.submit();
                    <%}%>
                } else {
                    window.document.forma.idGrupo.value=numEquipo;
                    window.document.forma.command.value='consultaDetalleGrupo';
                    window.document.forma.submit();
                }
            }
            function ayudaHistorialComentarios(numEquipo, numCiclo) {
                params = "?command=historialComentarios"+"&idGrupo="+numEquipo+"&idCiclo="+numCiclo;
                url = "/CEC/controller";            
                abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
            }
           
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");%>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
            <center>
                <h3>Consulta Ciclo por Estatus</h3>
                <%=HTMLHelper.displayNotifications(notificaciones)%>
            </center>            
            <form name="forma" action="controller" method="POST">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="idGrupo" value="">
                <input type="hidden" name="idCiclo" value="">
                <input type="hidden" name="analistaAsignar" value="">
                <input type="hidden" name="estatusAsignar" value="">
                <input type="hidden" name="accion" value="0">
                <input type="hidden" name="semDisp" value="0">
                <input type="hidden" name="idCredito" value="0">
               <div>
                    <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                        <tr>
                            <td align="right">Fecha de Modificación Inicial</td>
                            <%if(fechaInicio!=null){%>
                            <td><input name="fechaInicial" value="<%=fechaInicio%>" type="text" placeholder="dd/mm/yyyy" id="fechaInicial"></td>
                            <%} else{%>
                            <td><input name="fechaInicial" value="<%=fechaAnterior%>" type="text" placeholder="dd/mm/yyyy" id="fechaInicial"></td>
                            <%}%>
                        </tr>
                        <tr>
                            <td align="right">Fecha de Modificación Final</td>
                            <%if(fechaFin!=null){%>
                            <td><input name="fechaFinal" value="<%=fechaFin%>" type="text" placeholder="dd/mm/yyyy" id="fechaFinal"></td>
                            <%} else {%>
                            <td><input name="fechaFinal" value="<%=FechaHoy%>" type="text" placeholder="dd/mm/yyyy" id="fechaFinal"></td>
                            <%}%>
                        </tr>
                        <tr>
                            <td width="50%" align="right">Subproducto</td>
                            <td width="50%">
                                <select name="subproducto" size="1">
                                    <%=HTMLHelper.displayCombo(catSubProducto, subProd)%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="50%" align="right">Estatus</td>
                            <td width="50%">
                                <select name="estatus" size="1">
                                    <%=HTMLHelper.displayCombo(catEstatus,estatusCiclo)%>
                                </select>
                            </td>
                        </tr>
                        <%if(request.isUserInRole("ANALISIS_CREDITO")){%>
                        <tr>
                            <td width="50%" align="right">Sucursal</td>
                            <td width="50%">
                                <select name="sucursal" size="1">
                                    <%=HTMLHelper.displayCombo(catSucursales,idSucursal)%>
                                </select>
                            </td>
                        </tr>
                        <%}%>
                        <tr>
                            <td width="50%" height="10%" align="right">No. Equipo<br></td>
                            <%if(numEquipo!=0){%>
                            <td width="50%"><input name="numEquipo" size="10" maxlength="10" min="1" value="<%=numEquipo%>"></td>
                            <%} else{%>
                            <td width="50%"><input name="numEquipo" size="10" maxlength="10" min="1"></td>
                            <%}%>
                        </tr>
                        <%if((request.isUserInRole("ASIGNACION_EQUIPOS"))||(request.isUserInRole("manager"))){%>
                        <tr>
                            <td width="50%" align="right">Analista</td>
                            <td width="50%">
                                <select name="analista" size="1">
                                    <%=HTMLHelper.displayCombo(catAnalistas,analista)%>
                                </select>
                            </td>
                        </tr>
                        <%}%>
                        <tr>
                            <td align="right"><br><input type="button" value="Consultar" id="Consultar" onclick="consultaEstatus();"></td>
                            <td align="left"><br><input type="button" value="Regresar" id="Regresar" onclick="regresar();"></td>
                        </tr>
                    </table>
               </div>
                <%if(registros!=null&&registros.size()>0){%>
                <div>
                    <table border="0" width="95%" align="center">
                        <tr bgcolor="#343482" class="whitetext">
                            <td align="center">Equipo</td>
                            <td align="center">Nombre</td>
                            <td align="center">No. Ciclo</td>
                            <td align="center">Sucursal</td>
                            <td align="center">Estatus</td> 
                            <td align="center">Usuario Asignado</td>
                            <td align="center">Fecha Modificaci&oacute;n</td>
                            <td align="center">Última Modificaci&oacute;n</td>
                            <td align="center" colspan="2">Comentario</td>
                            <%if(subProd == 1){%>
                            <td align="center">Sem Dispersi&oacute;n</td>
                            <%}%>
                            <%if(request.isUserInRole("ASIGNACION_EQUIPOS")){%>
                                <td align="center">Asignación</td>
                            <%}%>
                        </tr>
                        <%int i=0;%>
                        <%for(BitacoraCicloVO registro : registros){%>
                        <tr>
                            <td align="right"><%=HTMLHelper.displayField(registro.getIdEquipo())%></td>
                            <td align="center"><a href="#" onClick="analizarEquipo(<%=registro.getIdEquipo()%>,<%=registro.getIdCiclo()%>,<%=registro.getEstatus()%>,<%=registro.getSemDisp()%>,<%=registro.getIdCredito()%>)">
                                    <%=HTMLHelper.displayField(registro.getNombreEquipo())%></a></td>
                            <td align="center"><%=HTMLHelper.displayField(registro.getIdCiclo())%></td>
                            <td align="left"><%=HTMLHelper.getDescripcion(catSucursales, registro.getIdSucursal())%></td>
                            <td align="left"><%=HTMLHelper.getDescripcion(catEstatus, registro.getEstatus())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getUsuarioAsignado())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getFechaHora())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getUsuarioComentario())%></td>
                            <td align="left" ><%=HTMLHelper.displayField(registro.getComentario())%></td>
                            <td align="right" ><a href="#" onClick="ayudaHistorialComentarios(<%=registro.getIdEquipo()%>, <%=registro.getIdCiclo()%>)"><img name="imagen" alt="Historial" src="images/history_icon.png"></a></td>
                            <%if(subProd == 1){%>
                            <td align="center"><%=HTMLHelper.displayField(registro.getSemDisp())%></td>
                            <%}%>
                            <%if(request.isUserInRole("ASIGNACION_EQUIPOS")&&(registro.getEstatus()==7||registro.getEstatus()==8||registro.getEstatus()==6)){%>
                            <td align="right">
                                <select name="analistaAsignacion<%=i%>" id="analistaAsignacion<%=i%>" size="1">
                                    <%=HTMLHelper.displayCombo(catAnalistas,"")%>
                                </select>
                                <input type="button" value="Asignar" onclick="asignarEquipo(<%=registro.getIdEquipo()%>,<%=registro.getIdCiclo()%>,<%=registro.getEstatus()%>,<%=i%>,<%=registro.getSemDisp()%>,<%=registro.getIdCredito()%>);">
                            </td>
                            <%}%>
                        </tr>
                        <%i++;}%>
                    </table>
                </div>
            <%}%>
        </form>
    </body>
</html>
<%@page import="com.sicap.clientes.dao.RenovacionDAO"%>
<%@page import="com.sicap.clientes.vo.RenovacionVO"%>
<%@page import="com.sicap.clientes.vo.PlaneacionRenovacionVO"%>
<%@page import="com.sicap.clientes.dao.PlaneacionRenovacionDAO"%>
<%@page import="com.sicap.clientes.dao.EjecutivoCreditoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.EjecutivoCreditoVO"%>
<%@ page import="com.sicap.clientes.vo.MetasEjecutivosVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
        TreeMap catMeses = CatalogoHelper.getCatalogoMeses();
        TreeMap catYear = CatalogoHelper.getCatalogoYear();
	Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
	ArrayList<GrupoVO> equiposPlaneados = (ArrayList<GrupoVO>)request.getAttribute("EQUIPOSPLANEADOS");
        ArrayList<GrupoVO> equiposRenovados = (ArrayList<GrupoVO>)request.getAttribute("EQUIPOSRENOVADOS");
        ArrayList<GrupoVO> equiposNuevos = (ArrayList<GrupoVO>)request.getAttribute("EQUIPOSNUEVOS");
        MetasEjecutivosVO metaAsesor = (MetasEjecutivosVO)request.getAttribute("META");	
        TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(HTMLHelper.getParameterInt(request, "idSucursal"), "A");
        TreeMap catNoRenovacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MOTIVOS_NO_RENOVACION);
	Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        System.out.println("DATE: "+dateNow);
        Calendar fechaVencimiento = Calendar.getInstance();        
%>
<html>
    <head>
        <title>Gestión de metas asesores</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            
            function consultaMetas(){
                if ( window.document.forma.idEjecutivo.value==0){
                    alert('Debe seleccionar un asesor');
                    return false;
                }
                if ( window.document.forma.idMonth.value==0){
                    alert('Debe seleccionar el mes de consulta');
                    return false;
                }                
                window.document.forma.command.value='consultaMetas';
                window.document.forma.submit();
            }
            
            function buscaEjecutivos(){
                window.document.forma.command.value='buscaEjecutivosGestion';
                window.document.forma.submit();
            }
            
            function noRenueva(idEquipo, idCiclo, i){
                if(document.getElementById("motivo"+i).value==0){
                    alert('Seleccione un motivo de no renovación');
                    return false;
                } else {
                    if(!confirm("¿Desea guardar el equipo como no renovado?")){
                        return false;
                    } else {
                        window.document.forma.idEquipo.value = idEquipo;
                        window.document.forma.idCiclo.value = idCiclo;
                        window.document.forma.idMotivo.value = document.getElementById("motivo"+i).value;
                        window.document.forma.numIntegrantes.value = document.getElementById("numIntegrantes"+i).value;
                        window.document.forma.fechaVencimiento.value = window.document.getElementById("vencimiento"+i).value;
                        window.document.forma.command.value='guardaNoRenovado';
                        window.document.forma.submit();
                    }
                }
            }
            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form action="admin" method="post" name="forma">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="grupos" value="0">            
            <input type="hidden" name="idEquipo" value="">
            <input type="hidden" name="idCiclo" value="">
            <input type="hidden" name="idMotivo" value="">
            <input type="hidden" name="numIntegrantes" value="">
            <input type="hidden" name="fechaVencimiento" value="">            
            <table border="0" width="100%">
                <tr>
                    <td colspan="2" align="center">
                        <h3>Gestión de Metas<br></h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%" align="left">
                        <select name="idSucursal" onchange="buscaEjecutivos()"><%=HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%></select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Ejecutivo</td>
                    <td width="50%" align="left">
                        <select name="idEjecutivo"><%=HTMLHelper.displayCombo(catEjecutivos, HTMLHelper.getParameterInt(request, "idEjecutivo"))%></select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Mes</td>
                    <%if(HTMLHelper.getParameterInt(request, "idMonth")!=0){%>
                    <td width="50%" align="left"><select name="idMonth"><%=HTMLHelper.displayCombo(catMeses, HTMLHelper.getParameterInt(request, "idMonth"))%></select></td>
                    <%} else {%>
                    <td width="50%" align="left"><select name="idMonth"><%=HTMLHelper.displayCombo(catMeses, cal.get(Calendar.MONTH)+1)%></select></td>
                    <%}%>
                </tr>
                <tr>
                    <td width="50%" align="right">Año</td>
                    <%if(HTMLHelper.getParameterInt(request, "idYear")!=0){%>
                    <td width="50%" align="left"><select name="idYear"><%=HTMLHelper.displayCombo(catYear, HTMLHelper.getParameterInt(request, "idYear"))%></select></td>
                    <%} else {%>
                    <td width="50%" align="left"><select name="idYear"><%=HTMLHelper.displayCombo(catYear, 2)%></select></td>
                    <%}%>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Consultar" onClick="consultaMetas();">
                        <br><br>
                    </td>
                </tr>
                <% if ( metaAsesor != null ) {
                    int porcentaje = (Integer)request.getAttribute("PORCENTAJE");
                    int sumaNuevos = (Integer)request.getAttribute("SUMANUEVOS");
                    int sumaRenovados= (Integer)request.getAttribute("SUMARENOVADOS");
                    EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(HTMLHelper.getParameterInt(request, "idEjecutivo"));
                %>
                <tr>
                    <td colspan="2" align="center">
                        <table border="1" cellpadding="1" width="90%" >
                            <tr bgcolor="#343482" class="whitetext">
                                <td width="8%" 	align="center">No.Asesor: <%=ejecutivo.idEjecutivo %></td>
                                <td width="20%" align="center">Asesor: <%=(ejecutivo.nombre+" "+ejecutivo.aPaterno+" "+ejecutivo.aMaterno).toUpperCase() %></td>
                                <td width="5%"  align="right">Meta:<input type="text" size="3" name="meta" value="<%=metaAsesor.meta%>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <td width="5%"  align="right">Porcentaje:<input type="text" size="3" name="porcentaje" value="<%=porcentaje%>%" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <input type="hidden" name="porciento" value="<%=porcentaje %>">
                            </tr>
                        </table>
                        <table border="1" cellpadding="1" width="90%" >
                            <tr bgcolor="#343482" class="whitetext">
                                <td width="20%" align="center">Clientes Nuevos Planeados <input type="text" size="3" name="nuevosPlaneados" value="<%=metaAsesor.clientesNuevos%>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <td width="20%" align="center">Clientes Nuevos Alcanzados <input type="text" size="3" name="nuevosAlcanzados" value="<%=sumaNuevos %>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <td width="20%" align="center">Clientes Renovación Planeados <input type="text" size="3" name="RenovadosPlaneados" value="<%=metaAsesor.integrantesTotal %>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <td width="20%" align="center">Clientes Renovación Alcanzados <input type="text" size="3" name="RenovadosAlcanzados" value="<%=sumaRenovados %>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                                <td width="20%" align="center">Meta Alcanzada <input type="text" size="3" name="metaAlcanzada" value="<%=sumaNuevos+sumaRenovados %>" maxlength="3" readonly="readonly" class="soloLectura"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <table border="1" width="90%" align="center">
                            <tr>
                                <td width="3%" 	align="center">No.Equipos</td>
                                <td width="15%" align="center">Equipos</td>
                                <td width="3%" 	align="center">Calificación</td>
                                <td width="3%"  align="center">Ciclo</td>
                                <td width="5%"	align="center">No.Integrantes</td>
                                <td width="5%"	align="center">No.Integrantes Planeados</td>
                                <td width="5%"	align="center">No.Integrantes Alcanzados</td>
                                <td width="7%" 	align="center">Fecha de vencimiento</td>
                                <td width="7%" 	align="center">Fecha de desembolso</td>
                                <td width="10%"	align="center">Estatus</td>
                                <td width="5%" 	align="center">Motivo no renovación</td>
                            </tr>
                <%if (equiposPlaneados!=null){
                    for ( int i=0 ; i < equiposPlaneados.size() ; i++ ){
                    PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
                    RenovacionVO renovacion = new RenovacionVO();
                    planeacion= new PlaneacionRenovacionDAO().getPlaneacionEquipo(equiposPlaneados.get(i).idGrupo, equiposPlaneados.get(i).idCicloOriginal);
                    renovacion = new RenovacionDAO().getRenovacion(equiposPlaneados.get(i).idGrupo, equiposPlaneados.get(i).idCicloOriginal);
                    fechaVencimiento.setTime(planeacion.fechaVencimiento);
                    long diferenciaMs = cal.getTimeInMillis()-fechaVencimiento.getTimeInMillis();
                    long diferenciaDias  = diferenciaMs/(1000*60*60*24);
                    System.out.println("DIFERENCIA: "+ diferenciaDias);
                    %>
                    <tr>
                    <input type="hidden" name="idGrupo<%=i%>" value="<%=equiposPlaneados.get(i).idGrupo %>">
                        <td width="3%" align="center"> <%=equiposPlaneados.get(i).idGrupo %></td>
                        <td width="15%" align="center"> <%=equiposPlaneados.get(i).nombre %></td>
                        <td width="3%" align="center"> <%=equiposPlaneados.get(i).calificacion %></td>
                        <input type="hidden" name="idCiclo<%=i%>" value="<%=planeacion.numCiclo %>">
                        <td width="3%"	align="center"> <%=planeacion.numCiclo %></td>
                        <td width="5%"	align="center">	<%=planeacion.integrantes %></td>
                        <input type="hidden" name="numIntegrantes<%=i%>" id="numIntegrantes<%=i%>" value="<%=planeacion.integrantes %>">
                        <td width="5%"	align="center">	<%=planeacion.integrantesTotal %></td>
                        <td width="5%"	align="center">	- </td>
                        <input type="hidden" name="vencimiento<%=i%>" id="vencimiento<%=i%>" value="<%=planeacion.fechaVencimiento %>">
                        <td width="7%"	align="center">	<%=planeacion.fechaVencimiento %></td>
                        <td width="7%"	align="center">	- </td>
                        <%if(diferenciaDias<0){ %>
                        <td width="10%" BGCOLOR="#FFFFFF"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <%} else if(diferenciaDias>=1&&diferenciaDias<=3){%>
                        <td width="10%" BGCOLOR="#04B431"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <%} else if(diferenciaDias>=4&&diferenciaDias<=6){%>
                        <td width="10%" BGCOLOR="#FFFF00"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <%} else if(diferenciaDias>=7){%>
                        <td width="10%" BGCOLOR="#FF0000"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <%} else {%>
                        <td width="10%" BGCOLOR="#FFFFFF"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <%}%>
                        <%if(renovacion!=null){ %>
                        <td width="4%"	align="center">
                            <input name="motivo<%=i%>" id="motivo<%=i%>" readonly="readonly" class="soloLectura" value="<%=HTMLHelper.getDescripcion(catNoRenovacion, renovacion.getIdMotivo()) %>">
                        </td>
                        <td width="5%"	align="center"></td>
                        <%} else{%>
                        <td width="4%"	align="center">
                            <select name="motivo<%=i%>" id="motivo<%=i%>" ><%=HTMLHelper.displayCombo(catNoRenovacion, 0)%></select>
                        </td>
                        <td width="5%"	align="center">
                        <input type="button" value="Guardar" onclick="noRenueva(<%=equiposPlaneados.get(i).idGrupo %>,<%=equiposPlaneados.get(i).idCicloOriginal %>,<%=i%>);">
                        </td>
                        <%}%>
                    </tr>
                    <%}%>
                    </td>
                </tr>
                <tr></tr>
                <%}%>
                <%if (equiposRenovados!=null){%>
                   
                    <%for ( int i=0 ; i < equiposRenovados.size() ; i++ ){ 
                    PlaneacionRenovacionVO planeacion = new PlaneacionRenovacionVO();
                    planeacion= new PlaneacionRenovacionDAO().getPlaneacionEquipo(equiposRenovados.get(i).idGrupo, equiposRenovados.get(i).idCicloOriginal-1);
                    %>
                    <tr>
                    <input type="hidden" name="idGrupo<%=i%>" value="<%=equiposRenovados.get(i).idGrupo %>">
                        <td width="3%" align="center"> <%=equiposRenovados.get(i).idGrupo %></td>
                        <td width="15%" align="center"> <%=equiposRenovados.get(i).nombre %></td>
                        <td width="3%" align="center"> <%=equiposRenovados.get(i).calificacion %></td>
                        <input type="hidden" name="idCiclo<%=i%>" value="<%=equiposRenovados.get(i).idCicloOriginal %>">
                        <td width="3%"	align="center"> <%=equiposRenovados.get(i).idCicloOriginal %></td>
                        <td width="5%"	align="center">	<%=planeacion.integrantes %></td>
                        <td width="5%"	align="center">	<%=planeacion.integrantesTotal %></td>
                        <td width="5%"	align="center">	<%=equiposRenovados.get(i).ciclos[0].numIntegrantes %></td>
                        <td width="7%"	align="center">	- </td>
                        <td width="7%"	align="center">	<%=equiposRenovados.get(i).ciclos[0].fechaDispersion %></td>
                        <td width="10%" BGCOLOR="#5882FA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <td width="4%"	align="center"></td>
                        <td width="5%"	align="center"></td>
                    </tr>
                    <%}%>
                    </td>
                </tr>
                <tr></tr>
                <%}%>
                <%if (equiposNuevos!=null){%>
                    <tr bgcolor="#343482" class="whitetext">
                        <td colspan="12" align="center">EQUIPOS NUEVOS</td>
                    </tr>
                    <%for ( int i=0 ; i < equiposNuevos.size() ; i++ ){ 
                    %>
                    <tr>
                    <input type="hidden" name="idGrupo<%=i%>" value="<%=equiposNuevos.get(i).idGrupo %>">
                        <td width="3%" align="center"> <%=equiposNuevos.get(i).idGrupo %></td>
                        <td width="15%" align="center"> <%=equiposNuevos.get(i).nombre %></td>
                        <td width="3%" align="center"> <%=equiposNuevos.get(i).calificacion %></td>
                        <input type="hidden" name="idCiclo<%=i%>" value="<%=equiposNuevos.get(i).idCicloOriginal %>">
                        <td width="3%"	align="center"> <%=equiposNuevos.get(i).idCicloOriginal %></td>
                        <td width="5%"	align="center">	- </td>
                        <td width="5%"	align="center">	- </td>
                        <td width="5%"	align="center">	<%=equiposNuevos.get(i).ciclos[0].numIntegrantes %></td>
                        <td width="7%"	align="center">	- </td>
                        <td width="7%"	align="center">	<%=equiposNuevos.get(i).ciclos[0].fechaDispersion %></td>
                        <td width="10%" BGCOLOR="#5882FA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                        <td width="4%"	align="center"></td>
                        <td width="5%"	align="center"></td>
                    </tr>
                    <%}%>
                    <tr></tr>
                <%}%>
                        </table>
                    </td>
                </tr>
                <%}%>
                <tr><td colspan="2" align="center"><br><a href="<%=request.getContextPath() %>">Inicio</a></td></tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
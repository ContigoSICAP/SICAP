<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.util.Convertidor"%>
<%@page import="java.sql.Date"%>
<%@page import="com.sicap.clientes.vo.BitacoraCicloVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<!DOCTYPE html>
<%
    TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusInterciclo();
    TreeMap catFondeadores = CatalogoHelper.getCatalogoFondeador();
    TreeMap catSucursales = CatalogoHelper.getCatalogoSucursales();
    TreeMap catAnalistas = CatalogoHelper.getCatalogoAnalistas();
    BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
    ArrayList<CicloGrupalVO> registros = (ArrayList<CicloGrupalVO>)request.getAttribute("REGISTROS");
    String fechaInicio = request.getParameter("fechaInicial");
    String fechaFin = request.getParameter("fechaFinal");
    int estatusCiclo = HTMLHelper.getParameterInt(request, "estatus");
    int idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
    int numEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
    String analista = HTMLHelper.getParameterString(request, "analista");
%>
<html>
    <head>
        <title>Consulta Inter-Ciclo por Estatus</title>
        <script>
            function regresar(){
                window.document.forma.command.value='administraGrupos';
                window.document.forma.submit();
            }
            function consultaEstatus(){
                inputs= document.getElementsByTagName('input');
                for(i=0;i<inputs.length;i++){
                    if (inputs[i].type === 'button'){// solo si es un boton entramos
                        inputs[i].disabled = true;
                    }
                }
                if((window.document.forma.fechaInicial.value==''&&window.document.forma.fechaFinal.value!='')||(window.document.forma.fechaInicial.value!=''&&window.document.forma.fechaFinal.value=='')){
                    alert("Rango de fechas inválido");
                    for(i=0;i<inputs.length;i++){
                        if (inputs[i].type === 'button'){// solo si es un boton entramos
                            inputs[i].disabled = false;
                        }
                    }
                    return false;
                } else if((window.document.forma.numEquipo.value!='')&&(!esEntero(window.document.forma.numEquipo.value))){
                    alert("Número de equipo inválido");
                    for(i=0;i<inputs.length;i++){
                        if (inputs[i].type === 'button'){// solo si es un boton entramos
                            inputs[i].disabled = false;
                        }
                    }
                    return false;
                } else if(window.document.forma.estatus.value ==0){
                    alert("Debe seleccionar un estatus");
                    for(i=0;i<inputs.length;i++){
                        if (inputs[i].type === 'button'){// solo si es un boton entramos
                            inputs[i].disabled = false;
                        }
                    }
                    return false;
                }
                else{
                    window.document.forma.command.value='consultaEstatusInterCiclo';
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }
            }

            function analizarEquipo(numEquipo, numCiclo, estatus){
                if(estatus==8||estatus==6){
                    <%if(request.isUserInRole("ANALISIS_CREDITO")&&!request.isUserInRole("manager")){%>
                    <%--if(request.isUserInRole("ANALISIS_CREDITO")){--%>
                    if(confirm("¿Estás seguro de comenzar el proceso de análisis?")){
                        window.document.forma.idGrupo.value=numEquipo;
                        window.document.forma.idCiclo.value=numCiclo;
                        window.document.forma.estatusAsignar.value=estatus;
                        window.document.forma.accion.value=2;
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
                    document.body.style.cursor = "wait";
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
                <h3>Consulta Inter-Ciclo por Estatus</h3>
                <%=HTMLHelper.displayNotifications(notificaciones)%>
            </center>            
            <form name="forma" action="controller" method="POST">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="idGrupo" value="">
                <input type="hidden" name="idCiclo" value="">
                <input type="hidden" name="analistaAsignar" value="">
                <input type="hidden" name="estatusAsignar" value="">
                <input type="hidden" name="accion" value="0">                
               <div>
                    <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                        <tr>
                            <td align="right">Fecha de Dispersión Inicial</td>
                            <%if(fechaInicio!=null){%>
                            <td><input name="fechaInicial" value="<%=fechaInicio%>" type="text" placeholder="dd/mm/yyyy" id="fechaInicial"></td>
                            <%} else{%>
                            <td><input name="fechaInicial" value="" type="text" placeholder="dd/mm/yyyy" id="fechaInicial"></td>
                            <%}%>
                        </tr>
                        <tr>
                            <td align="right">Fecha de Dispersión Final</td>
                            <%if(fechaFin!=null){%>
                            <td><input name="fechaFinal" value="<%=fechaFin%>" type="date" placeholder="dd/mm/yyyy" id="fechaFinal"></td>
                            <%} else{%>
                            <td><input name="fechaFinal" value="" type="text" placeholder="dd/mm/yyyy" id="fechaFinal"></td>
                            <%}%>
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
                        
                        <tr>
                            <td align="right"><br><input type="button" value="Consultar" onclick="consultaEstatus();"></td>
                            <td align="left"><br><input type="button" value="Regresar" onclick="regresar();"></td>
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
                            <td align="center">Fecha Dispersi&oacute;n</td>
                            <td align="center">Fondeador</td>
                            
                        </tr>
                        <%int i=0;%>
                        <%for(CicloGrupalVO registro : registros){%>
                        <tr>
                            <td align="right"><%=HTMLHelper.displayField(registro.idGrupo)%></td>
                            <td align="center"><a href="#" onClick="analizarEquipo(<%=registro.idGrupo%>,<%=registro.getIdCiclo()%>,<%=registro.getEstatus()%>)">
                                    <%=HTMLHelper.displayField(registro.getNombreEquipo())%></a></td>
                            <td align="center"><%=HTMLHelper.displayField(registro.getIdCiclo())%></td>
                            <td align="left"><%=HTMLHelper.getDescripcion(catSucursales, registro.getCoordinador())%></td>
                            <td align="left"><%=HTMLHelper.getDescripcion(catEstatus, registro.getEstatusIC())%></td>
                            <td align="center"><%=HTMLHelper.displayField(registro.fechaDispersion)%></td>
                            <td aling="left"><%=HTMLHelper.getDescripcion(catFondeadores,registro.fondeador)%></td>
                        </tr>
                        <%i++;}%>
                    </table>
                </div>
            <%}%>
        </form>
    </body>
</html>
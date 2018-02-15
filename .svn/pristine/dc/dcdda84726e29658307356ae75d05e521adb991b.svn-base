<%-- 
    Document   : cargaArchivos
    Created on : 26/10/2012, 10:31:05 AM
    Author     : Alex
--%>

<%@page import="java.util.Set"%>
<%@page import="com.sicap.clientes.util.AdicionalUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.sicap.clientes.vo.ArchivoAsociadoVO"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.vo.GrupoVO"%>
<%@page import="com.sicap.clientes.util.GrupoUtil"%>
<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
int conSeguro = HTMLHelper.getParameterInt(request, "conSeguro");
int semanaAdicional = HTMLHelper.getParameterInt(request, "semanaAdicional");
GrupoVO grupo = new GrupoVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
ArchivoAsociadoVO fldSeguro = null, fldGarantia = null, fldDocOficial = null, fldSeguroInter = null, fldDocOficialInter = null;
List<ArchivoAsociadoVO> listFldCreditoAdicional = new ArrayList<ArchivoAsociadoVO>();
List<ArchivoAsociadoVO> listDocLegalesAdicional = new ArrayList<ArchivoAsociadoVO>();

if(session.getAttribute("GRUPO")!=null ){
    grupo = (GrupoVO)session.getAttribute("GRUPO");
    if ( idCiclo!=0 ){
        ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
    }
}

Set<Integer> setPlazoAdicionales = AdicionalUtil.obtienePlazosAdicionalDeIntegrantesCiclo (ciclo.integrantes);
System.out.println("Tamaño set plazos adicionales:" + setPlazoAdicionales.size());

if(ciclo.archivosAsociados != null){
    for(int i=0; i<ciclo.archivosAsociados.length; i++){
        if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_SEGURO){
            fldSeguro = ciclo.archivosAsociados[i];
        } else if (ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_GARANTIA){
            fldGarantia = ciclo.archivosAsociados[i];
        } else if (ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES){
            fldDocOficial = ciclo.archivosAsociados[i];
        } else if (ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_SEGURO_INTERCICLO){
            fldSeguroInter = ciclo.archivosAsociados[i];        
        } else if (ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES_INTERCICLO){
            fldDocOficialInter = ciclo.archivosAsociados[i];
        } else if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_SOLICITUD_ADICIONAL){
            listFldCreditoAdicional.add(ciclo.archivosAsociados[i]);
        }else if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_DOC_LEGAL_ADICIONAL){
            listDocLegalesAdicional.add(ciclo.archivosAsociados[i]);
        }
    }
    
    if(!listFldCreditoAdicional.isEmpty()){
        Collections.sort(listFldCreditoAdicional, new Comparator<ArchivoAsociadoVO>(){
            public int compare(ArchivoAsociadoVO o1, ArchivoAsociadoVO o2){
                return o1.nombre.compareTo(o2.nombre);
            }
        });
    }
    
    if(!listDocLegalesAdicional.isEmpty()){
        Collections.sort(listDocLegalesAdicional, new Comparator<ArchivoAsociadoVO>(){
            public int compare(ArchivoAsociadoVO o1, ArchivoAsociadoVO o2){
                return o1.nombre.compareTo(o2.nombre);
            }
        });
    }
    
    
}
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Carga de Archivos</title>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
        <script>
            function cargarArchivos(tipo){
                document.forms['forma'].setAttribute('enctype', 'multipart/form-data', 0);
                window.document.forma.action="guardaArchivosGrupal.jsp";
                window.document.forma.target="_self";
                var autorizado = "";
                if(tipo == "fileFichaSeguros"){
                    autorizado = window.document.forma.autorizacion1.value;
                    window.document.forma.tipoArchivo.value = "fileFichaSeguros";
                }else if(tipo == "fileFichaGarantia"){
                    autorizado = window.document.forma.autorizacion2.value;
                    window.document.forma.tipoArchivo.value = "fileFichaGarantia";
                }else if(tipo == "fileDocOficial"){
                    autorizado = window.document.forma.autorizacion3.value;
                    window.document.forma.tipoArchivo.value = "fileDocOficial";
                }else if(tipo == "fileFichaSegurosInter"){
                    autorizado = window.document.forma.autorizacion4.value;
                    window.document.forma.tipoArchivo.value = "fileFichaSegurosInter";
                }else if(tipo == "fileFichaGarantiaInter"){
                    autorizado = window.document.forma.autorizacion5.value;
                    window.document.forma.tipoArchivo.value = "fileFichaGarantiaInter";
                }else if(tipo == "fileDocOficialInter"){
                    autorizado = window.document.forma.autorizacion6.value;
                    window.document.forma.tipoArchivo.value = "fileDocOficialInter";
                }else if(tipo == "fileCreditoAdicional4"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional5"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional6"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional7"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional8"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional9"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileCreditoAdicional10"){
                    autorizado = window.document.forma.autorizacion7.value;
                    window.document.forma.tipoArchivo.value = "fileCreditoAdicional";
                }else if(tipo == "fileDocLegalAdicional4"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_4";
                }else if(tipo == "fileDocLegalAdicional5"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_5";
                }else if(tipo == "fileDocLegalAdicional6"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_6";
                }else if(tipo == "fileDocLegalAdicional7"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_7";
                }else if(tipo == "fileDocLegalAdicional8"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_8";
                }else if(tipo == "fileDocLegalAdicional9"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_9";
                }else if(tipo == "fileDocLegalAdicional10"){
                    autorizado = window.document.forma.autorizacion8.value;
                    window.document.forma.tipoArchivo.value = "fileDocLegalAdicional_10";
                }
                
                
                
                
                
                if(autorizado != ""){
                    var extencion = autorizado.value;
                    //alert("**** "+extencion.indexOf('.zip')+" ***** "+extencion.indexOf('.rar'));
                    if(tipo == "fileDocumentacion" && (extencion.indexOf('.zip') != -1 || extencion.indexOf('.rar') != -1)){
                         window.document.forma.submit();
                    } else if(tipo == "fileFichaSeguros" || tipo == "fileFichaGarantia" || tipo == "fileDocOficial" || tipo == "fileFichaSegurosInter" || tipo == "fileFichaGarantiaInter" || tipo == "fileDocOficialInter"){
                        window.document.forma.submit();
                    } else if(tipo == "fileCreditoAdicional4" || tipo == "fileCreditoAdicional5" || tipo == "fileCreditoAdicional6" 
                            || tipo == "fileCreditoAdicional7" || tipo == "fileCreditoAdicional8" || tipo == "fileCreditoAdicional9"  
                            || tipo == "fileCreditoAdicional10"){
                        window.document.forma.submit();
                    } else if(tipo == "fileDocLegalAdicional4" || tipo == "fileDocLegalAdicional5" || tipo == "fileDocLegalAdicional6" 
                            || tipo == "fileDocLegalAdicional7" || tipo == "fileDocLegalAdicional8" || tipo == "fileDocLegalAdicional9"  
                            || tipo == "fileDocLegalAdicional10"){
                        window.document.forma.submit();
                    } 
                    else {
                        alert("El Archivo no se encuentra en un formato permitido");
                    }
                } else {
                    alert("Debe de agregar la ruta del archvo");
                }
            }
            function muestraDocumento(tipo){
                window.document.forma.command.value='descargaDocumento';
                //window.document.forma.target="_blank";
		window.document.forma.tipo.value=tipo;
                window.document.forma.submit();        
            }
        </script>
    </head>
    <body>
    <center>
        <h3><%=HTMLHelper.displayNotifications(notificaciones)%></h3>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="tipo" value="">
            <input type="hidden" name="tipoArchivo" value=""/>
            <input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>"/>
            <input type="hidden" name="idCiclo" value="<%=idCiclo%>"/>
            <input type="hidden" name="idNombre" value="<%=grupo.nombre%>"/>
            <input type="hidden" name="semanaAdicional" value="<%=semanaAdicional%>"/>
            <br/>
            <table width="100%" border="2" cellpadding="0" bordercolor="#0040FF">
                <tr>
                    <td align="center" colspan="2"><h3>Documentaci&oacute;n de Ciclo</h3></td>
                </tr>
                <%if(fldSeguro == null && conSeguro == 1){%>
                <tr>
                    <td align="center"><b>Ingreso Ficha de Seguros</b></td>
                    <td align="center">
                        <input type="file" name="autorizacion1" size="45"/>
                        <input type="button" value="Guardar" onClick="cargarArchivos('fileFichaSeguros')"/><br/>
                        <input type="hidden" name="conSeguro" value="1"/>
                    </td>
                </tr>
                <%} else if(fldSeguro != null && ciclo.seguro == 1){%>
                  <tr>
                      <td align="center"><i><a href="#" onClick="muestraDocumento('FICHASEGURO')">Consulta Ficha de Seguros</a></i></td>
                        <input type="hidden" name="fileSeguro" value="<%=fldSeguro.nombre%>"/>
                      <td align="center">
                        <input type="file" name="autorizacion1" size="45" disabled=""/>
                        <input type="button" value="Guardar" disabled=""/><br/>
                      </td>
                </tr>  
                <%}
                if (fldGarantia == null && ciclo.desembolsado > 0){%>
                <tr>
                    <td align="center"><b>Ingreso Ficha de Garantia</b></td>
                    <td align="center">
                        <input type="file" name="autorizacion2" size="45"/>
                        <input type="button" value="Guardar" onClick="cargarArchivos('fileFichaGarantia')"/><br/>
                    </td>
                </tr>
                <%} else if (ciclo.desembolsado > 0){%>
                <tr>
                    <td align="center" ><i><a href="#" onClick="muestraDocumento('FICHAGARANTIA')">Consulta Ficha de Garantia</a></i></td>
                        <input type="hidden" name="fileGarantia" value="<%=fldGarantia.nombre%>"/>
                    <td align="center">
                        <input type="file" name="autorizacion2" size="45" disabled=""/>
                        <input type="button" value="Guardar" disabled=""/><br/>
                    </td>
                </tr>
                <%}
                if (fldDocOficial == null && ciclo.desembolsado > 0){%>
                <tr>
                    <td align="center"><b>Ingreso de Documentos Oficiales</b></td>
                    <td align="center" >
                        <input type="file" name="autorizacion3" size="45"/>
                        <input type="button" value="Guardar" onClick="cargarArchivos('fileDocOficial')"/>
                    </td>
                </tr>
                <%} else if (ciclo.desembolsado > 0){%>
                <tr>
                    <td align="center"><i><a href="#" onClick="muestraDocumento('DOCOFICIALES')">Consulta Documentos Legales</a></i></td>
                        <input type="hidden" name="fileDocumento" value="<%=fldDocOficial.nombre%>"/>
                    <td align="center">
                        <input type="file" name="autorizacion3" size="45" disabled=""/>
                        <input type="button" value="Guardar" disabled=""/><br/>
                    </td>
                </tr>
                <%}
                if (ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO||ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO){
                    if (fldSeguroInter == null){%>
                <tr>
                    <td align="center"><b>Ingreso Ficha de Seguros Inter-Ciclo</b></td>
                    <td align="center" >
                        <input type="file" name="autorizacion4" size="45"/>
                        <input type="button" value="Guardar" onClick="cargarArchivos('fileFichaSegurosInter')"/>
                    </td>
                </tr>
                    <%} else {%>
                <tr>
                    <td align="center"><i><a href="#" onClick="muestraDocumento('FICHASEGUROINTER')">Consulta Ficha de Seguros Inter-Ciclo</a></i></td>
                        <input type="hidden" name="fileSegurosInter" value="<%=fldSeguroInter.nombre%>"/>
                    <td align="center">
                        <input type="file" name="autorizacion4" size="45" disabled=""/>
                        <input type="button" value="Guardar" disabled=""/><br/>
                    </td>
                </tr>
                    <%}                 
                    if (fldDocOficialInter == null){%>
                <tr>
                    <td align="center"><b>Ingreso de Documentos Oficiales Inter-Ciclo</b></td>
                    <td align="center" >
                        <input type="file" name="autorizacion6" size="45"/>
                        <input type="button" value="Guardar" onClick="cargarArchivos('fileDocOficialInter')"/>
                    </td>
                </tr>
                    <%} else {%>
                <tr>
                    <td align="center"><i><a href="#" onClick="muestraDocumento('DOCOFICIALESINTER')">Consulta Documentos Oficiales Inter-Ciclo</a></i></td>
                        <input type="hidden" name="fileDocumentoInter" value="<%=fldDocOficialInter.nombre%>"/>
                    <td align="center">
                        <input type="file" name="autorizacion6" size="45" disabled=""/>
                        <input type="button" value="Guardar" disabled=""/><br/>
                    </td>
                </tr>
                    <%}%>
                <%}%>
                
                <%if(semanaAdicional > 0 ){
                
                    
                      //Iteracion para mostrar los elementos relacionados con la documentacion
                      //de la solicitud de credito de adicional
                      for(int i= 4; i <=10; i++){
                        String nombreDoc = ClientesConstants.nombreDocArchivoAdicional;
                        nombreDoc = nombreDoc.replace("#",String.valueOf(i));
                        System.out.println("Nombre de documento a buscar:" + nombreDoc + " indice i:" + i + " semanaAdicional:" + semanaAdicional);
                        ArchivoAsociadoVO fileTmp = null;
                        
                        
                        for(java.util.Iterator<ArchivoAsociadoVO> itr =  listFldCreditoAdicional.iterator(); itr.hasNext(); ){
                            ArchivoAsociadoVO tmp = itr.next();
                        
                            if(tmp.nombre.contains(nombreDoc)){
                                fileTmp = tmp;
                                System.out.println("Nombre archivo:" + fileTmp.nombre);
                                break;
                            }
                        }
                        
                        
                        if(fileTmp == null && semanaAdicional == i){%>
                            <tr>
                                <td align="center"><b>Ingreso Solicitud Crédito Adicional Semana <%=i%><b></td>
                                <td align="center">
                                    <input type="file" name="autorizacion7" size="45"/>
                                    <input type="button" value="Guardar" onClick="cargarArchivos('fileCreditoAdicional<%=i%>')"/><br/>
                                </td>
                            </tr>
                        
                        
                        
                        <%} else if(fileTmp != null){%>
                              <tr>
                                  <td align="center"><i><a href="#" onClick="muestraDocumento('DOCADICIONAL<%=i%>')">Consulta Solicitud Crédito Adicional Semana <%=i%></a></i></td>
                                    <input type="hidden" name="fileDocAdicional<%=i%>" value="<%=fileTmp.nombre%>"/>
                                  <td align="center">
                                    <input type="file" name="autorizacionAdicional<%=i%>" size="45" disabled=""/>
                                    <input type="button" value="Guardar" disabled=""/><br/>
                                  </td>
                            </tr>  
                        <%}
                        if(semanaAdicional == i){
                               break;
                        }
                   }

                   //Iteracion para mostrar los elementos relacionados con la documentacion
                   //legale credito de adicional
                    for(int i : setPlazoAdicionales){
                      String nombreDoc = ClientesConstants.nombreDocLegalesAdicionales;
                      nombreDoc = nombreDoc.replace("#",String.valueOf(i));
                      System.out.println("Nombre de documento a buscar:" + nombreDoc + " indice i:" + i + " semanaAdicional:" + semanaAdicional);
                      ArchivoAsociadoVO fileTmp = null;


                      for(java.util.Iterator<ArchivoAsociadoVO> itr =  listDocLegalesAdicional.iterator(); itr.hasNext(); ){
                          ArchivoAsociadoVO tmp = itr.next();

                          if(tmp.nombre.contains(nombreDoc)){
                              fileTmp = tmp;
                              System.out.println("Nombre archivo:" + fileTmp.nombre);
                              break;
                          }
                      }
                      if(fileTmp == null ){%>
                         <tr>
                             <td align="center"><b>Ingreso Doc. Legal de Adicional Semana <%=i%><b></td>
                             <td align="center">
                                 <input type="file" name="autorizacion8" size="45"/>
                                 <input type="button" value="Guardar" onClick="cargarArchivos('fileDocLegalAdicional<%=i%>')"/><br/>
                             </td>
                         </tr>
                      <%}else if(fileTmp != null){%>
                              <tr>
                                  <td align="center"><i><a href="#" onClick="muestraDocumento('DOCLEGALADICIONAL<%=i%>')">Consulta Documentacion Legal Adicional Semana <%=i%></a></i></td>
                                    <input type="hidden" name="fileDocLegalAdicional<%=i%>" value="<%=fileTmp.nombre%>"/>
                                  <td align="center">
                                    <input type="file" name="autorizacionAdicional<%=i%>" size="45" disabled=""/>
                                    <input type="button" value="Guardar" disabled=""/><br/>
                                  </td>
                            </tr>  
                        <%}



                    }
                   
                        
                   %>
                
                    
                    
                <%}%>
            </table>
            <br/>
        </form>
    </center>
    </body>
</html>

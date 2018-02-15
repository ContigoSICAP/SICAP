<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.dao.SaldoIBSDAO"%>
<%@page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>
<%@page import="com.sicap.clientes.vo.IntegranteCicloVO"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.vo.BitacoraCicloVO"%>
<%@page import="com.sicap.clientes.vo.ArchivoAsociadoVO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.sicap.clientes.util.ClientesUtil"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.util.FechasUtil"%>
<%@ page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@ page import="com.sicap.clientes.util.FormatUtil"%>

<%
Calendar cal = Calendar.getInstance();
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
boolean fechaPagoValidaDesembolso = ((Boolean)request.getAttribute("FechaPagoValida")).booleanValue();
boolean solicitudAdicional = ((Boolean)request.getAttribute("tieneSolicitudAdicional")).booleanValue();
int semanaAdicional = ((Integer)session.getAttribute("SEMANA_ADICIONAL")).intValue();
boolean estatusCredito = ((Boolean)request.getAttribute("EstatusCredito")).booleanValue();
boolean docLegalesCargados = ((Boolean)request.getAttribute("docLegalesCargados")).booleanValue();

//BitacoraCicloVO bitacoraCiclo = (BitacoraCicloVO)request.getAttribute("COMENTARIO");
//System.out.println("COMENTARIO: "+ bitacoraCiclo.getComentario());
boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
boolean sinConsulta = false;
boolean tieneAtrasos = false;
boolean tieneMultas = false;
GrupoVO grupo = new GrupoVO();
double total = 0;
double totalConComision = 0;
double totalRefinanciado = 0;
double totalVencido = 0;
double totalConSegugoFinanciado = 0;
double totalCostoSeguro = 0;
BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
TablaAmortDAO tablaDao = new TablaAmortDAO();
SaldoIBSDAO saldoDao= new SaldoIBSDAO();
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catMontosMaximosCiclo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MAXIMOS_POR_CICLO);
//TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo(false);
TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo(true, 0);
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catPorcentajeAdicional = CatalogoHelper.getCatalogoProcentaje((Integer)session.getAttribute("SEMANA_ADICIONAL"));
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catEjecutivos = new TreeMap();
TreeMap catCoordinadores = catSucursales;//new TreeMap();
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();//new TreeMap();
TreeMap catTasasGrupal = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
TreeMap catFondeador = CatalogoHelper.getCatalogoFondeador();
TreeMap catDespachos = CatalogoHelper.getCatalogoDespachos();
TreeMap catPlazos = CatalogoHelper.getCatalogoPlazos();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
TreeMap catAperturador = new TreeMap();
TreeMap catGarantia = CatalogoHelper.getCatalogoGarantiaGrupal();
int idCiclo 		= HTMLHelper.getParameterInt(request, "idCiclo");
//int otraFinanciera 	= HTMLHelper.getParameterInt(request, "otraFinanciera");
int otraFinanciera 	= 3;
int numIntegrantes = 0;
// el campo de seguros completos se deja como 1 para pasar la validacion JBL- SEP/10
int segurosCompletos = 1;
int idComision=0;
int plazo=0;
int tasa = 0;
int idBanco = 0;
int fondeador = 0;
int despacho = 0;
int semDisp = 0;
int estatusIC =0;
String campSeguro = "";
ArchivoAsociadoVO fldSeguro = null;
//ArchivoAsociadoVO fldDocums = null;
String bloqueo = "";
if ( session.getAttribute("GRUPO")!=null ){
        grupo = (GrupoVO)session.getAttribute("GRUPO");
        tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
	idBanco = new SucursalDAO().getSucursal(grupo.sucursal).idBanco;
	if ( idCiclo!=0 ){
                ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
                //ciclo.tasa = tasa;
		if ( ciclo!=null && ciclo.direccionReunion!=null ) 
			direccion=ciclo.direccionReunion;
		if ( ciclo!=null && ciclo.integrantes!=null ){
			numIntegrantes = ciclo.integrantes.length;
			 // cuando ya existe el ciclo obtener tasa, plazo y comision
                        plazo = ciclo.plazo;
                        idComision = ciclo.comision;
			/*if ( SeguroHelper.cicloSegurosCompletos(ciclo) )
				 segurosCompletos = 1;*/
		}
                if(ciclo.saldo!=null){
                    fondeador = ciclo.getFondeador();
                    despacho = ciclo.saldo.getNumDespacho();
                }
                //campSeguro = HTMLHelper.getParameterString(request, "campSeguro");
                for(int i=0; i<ciclo.integrantes.length; i++){
                    if(ciclo.integrantes[i].seguro == 1){
                        campSeguro = "si";
                    }
                }
                if(ciclo.archivosAsociados != null){
                    for(int i=0; i<ciclo.archivosAsociados.length; i++){
                        if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_SEGURO){
                            fldSeguro = ciclo.archivosAsociados[i];
                        }/* else if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES){
                            fldDocums = ciclo.archivosAsociados[i];
                        }*/
                    }
                }
                if(idCiclo>1){
                    if(tablaDao.getAtrasos(grupo.idGrupo, grupo.ciclos[idCiclo-2].idCreditoIBS)>3){
                        tieneAtrasos=true;
                    }
                    tieneMultas = saldoDao.tieneMultas(grupo.idGrupo, grupo.ciclos[idCiclo-2].idCreditoIBS);
                }
	}else if ( request.getAttribute("CICLO")!=null ){
		ciclo = (CicloGrupalVO)request.getAttribute("CICLO");
		//ciclo.tasa = tasa;
		if ( ciclo!=null && ciclo.direccionReunion!=null ) direccion=ciclo.direccionReunion;
		if ( ciclo.integrantes!=null )
			numIntegrantes = ciclo.integrantes.length;
		if ( ciclo!=null && ciclo.integrantes!=null ){
			/*if ( SeguroHelper.cicloSegurosCompletos(ciclo) )
				 segurosCompletos = 1;*/
		}
	}
	/*if( ciclo.tablaAmortizacion!=null && ciclo.tablaAmortizacion.length>0 ){
		String resp = GeneraContratoHelper.makeContract(grupo, ciclo);
		int index = resp.indexOf("<center><b>PAGARÉ COLECTIVO FINANCIERA CONTIGO</b></center>");
		String temp = "<body>" + resp.substring(0,index) + "</body>";
		session.setAttribute("CONTRATO",temp);
		temp = "<body>" + resp.substring(index) + "</body>";
		session.setAttribute("PAGARE",temp);
		String pagareIndiv = GeneraContratoHelper.makePagareIndividual(grupo, ciclo, catComisionesGrupal);
		session.setAttribute("PAGAREINDIVIDUAL", pagareIndiv);
		temp = GeneraContratoHelper.makeOrdenPago(ciclo);
		session.setAttribute("ORDENPAGO", temp);
	}*/
	catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
        catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
}
if(request.getAttribute("semanaDisp")!=null){
    semDisp = (Integer)request.getAttribute("semanaDisp");
}

String desembolsoOk = "NO";
if ( request.getAttribute("VALIDACION")!=null )
	desembolsoOk = (String)request.getAttribute("VALIDACION");

int validaCiclo = -1;
if ( request.getAttribute("ID_CICLO")!=null )
	validaCiclo = (Integer)request.getAttribute("ID_CICLO");

// Se verifica si viene de otra financiera JBL SEP/10
boolean isDesembolsadoFlag = GrupoHelper.isDesembolsado(ciclo.integrantes);
boolean isRefinaceoFlag	= ( ciclo.idTipoCiclo==ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO ? true : false );
//boolean isOtraFinancieraFlag =  (otraFinanciera==ClientesConstants.CICLO_OTRA_FINANCIERA ? true : false );
boolean isOtraFinancieraFlag =  false;

if( !isDesembolsadoFlag )
	ciclo.tasa = tasa;
else
	tasa = ciclo.tasa;

// En caso de venir de otra financiera se cambia el tipo de ciclo y asi se le da un trato diferente JBL SEP/10
if (isOtraFinancieraFlag)
	ciclo.idTipoCiclo = otraFinanciera;

boolean isConfirmadorDesembolso = request.isUserInRole("CONFIRMACION_DESEMBOLSO_GRUPAL");

boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
int opcion = GrupoHelper.muestraOpcionesGrupal( ciclo, desembolsoOk );
String medio = "";
%>
<html>
<head>
<title>Alta de Credito Adicional</title>
<script language="Javascript" src="./js/functions.js"></script>
<script language="Javascript" src="./js/functionsGrupal.js"></script>
<script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
<!--
	function guardaCicloGrupal(integrantes){
            if ( window.document.forma.estatus.value == 5 ){
                window.document.forma.command.value = 'guardaOtrosDatos';
                window.document.forma.submit();
            } else {
                <%if( isRefinaceoFlag ) {%>
                    window.document.forma.command.value = 'guardaCicloRefinanciamiento';
                <%}else{%>
                    window.document.forma.command.value = 'guardaCicloGrupal';
                <%}%>
                    window.document.forma.action="controller";

                    if ( integrantes>40 ){
                        alert("El grupo debe contar con un máximo de 40 integrantes");
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.idColonia.value==0 ){
                        alert("Debe capturar la dirección");
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.calle.value=='' ){
                        alert("Debe capturar la calle");
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.numeroExterior.value=='' ){
                        alert("Debe capturar el número exterior");
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.estatus.value!=10 ){
                        alert('Estatus inválido');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    /*if ( window.document.forma.estatus.value>2 ){
                        alert('estatus inválido');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }*/
                    if ( window.document.forma.diaReunion.value==0 ){
                        alert('Debe indicar el día de reunión');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.horaReunion.value==0 ){
                        alert('Debe indicar la hora de reunión');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.asesor.value==0 ){
                        alert('Debe indicar el ejecutivo');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.coordinador.value==0 ){
                        alert('Debe indicar el coordinador');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( !esFormatoMoneda(window.document.forma.multaRetraso.value) || window.document.forma.multaRetraso.value<=0 ){
                        alert('El monto de la multa por retraso es inválida');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( !esFormatoMoneda(window.document.forma.multaFalta.value) || window.document.forma.multaFalta.value<=0  ){
                        alert('El monto de la multa por falta es inválida');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.fechaDispersion.value=='' ){
                            alert("Debe capturar la fecha de dispersión");
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                    }
                    if ( window.document.forma.bancoDispersion.value==0 ){
                            alert('Debe indicar el banco para dispersar');
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                    }
                    if( !validaRol(integrantes) ){
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                    }
                    <%if(request.isUserInRole("AUTORIZACION_EQUIPOS_VIP")){ %>
                        if( !minimoIntegrantesVIP(integrantes) ){
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                        }
                        <%if(tieneAtrasos){%>
                            alert('El equipo tiene más de tres atrasos en el ciclo anterior, favor de validarlo con Mesa de Control');
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                        <%}%>
                        <%if(tieneMultas){%>
                            alert('El equipo tiene multas en el ciclo anterior, favor de validarlo con Mesa de Control');
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                        <%}%>
                    <%} else{%>
                        if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>) ){
                                document.getElementById("guardaCiclo").disabled = false;
                                return false;
                        }
                    <%}%>
                    if ( !getCalificacionGrupal(integrantes) ){
                            alert('La calificación de Círculo de crédito determina rechazar el crédito');
                            document.getElementById("guardaCiclo").disabled = false;
                            return false;
                    }
                    if ( window.document.forma.plazo.value==0 ){
                        alert('Debe indicar el tipo de palzo');
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if ( !validaMontoIntegrantes(integrantes) ){
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
                    }
                    if (window.document.forma.fondeador.value == 0){
                        alert("Debe de seleccionar un Fondeador");
                        document.getElementById("btn_desembolsar").disabled = false;
                        return false;
                    }
                    res = confirm('La información que está apunto de guardar no podrá ser modificada, verifique que se encuentren todos los integrantes del grupo y los montos sean los correctos. ¿Esta seguro de que desea registrar el ciclo con los datos actuales?');
                if ( !res ) return res;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	}

	function desembolsaCicloGrupal(integrantes){
                document.getElementById("btn_desembolsar").disabled = true;
		window.document.forma.command.value = 'desembolsaCicloGrupal';
		var desembolsos = cuentaDesembolsos(integrantes);
		
		var fecha = new Date();
		var milisegundos = parseInt(1*24*60*60*1000);
		var fechaPart = window.document.forma.fechaDesembolso.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
		
		var tiempo = fecha.getTime();
		var total = fecha.setTime(parseInt(tiempo-milisegundos));
		var fechaActual = new Date(total);
                //DESCOMENTAR PARA LA PARTE DE CAPTURA DE 48 HORAS
                /*var diaActual = new Date();
                var fechaTemp = window.document.forma.fechaDispersion.value;
                var diaDisp = new Date(fechaTemp.substring(6,10), fechaTemp.substring(3,5)-1, fechaTemp.substring(0,2));
                var difDias = diaDisp.getTime()-diaActual.getTime();
                var dias = Math.floor(difDias/(1000*60*60*24));
                if (dias < 1){
                    alert('La fecha de Dispersion debe de contar con 48 hrs de anticipacion');
                    enableEdit();
                    return false;
                }*/
		
		if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>) ){
			document.getElementById("btn_desembolsar").disabled = false;
                        return false;
		}

		/*if ( window.document.forma.segurosCompletos.value==0 ){
			alert('El grupo debe contar con todos los seguros de vida de sus integrantes capturados');
                        document.getElementById("btn_desembolsar").disabled = false;
			return false;
		}*/
		
		if (window.document.forma.fechaDesembolso.value ==''){
			alert("Se debe elegir una fecha para el desembolso");
                        document.getElementById("btn_desembolsar").disabled = false;
			return false;
		}
                
                if( !validaRol(integrantes) ){
                    document.getElementById("btn_desembolsar").disabled = false;
                    return false;
                }
		
		if(fechaActual > fechaIngresada){
			alert("La fecha de desembolso debe ser mayor a la fecha actual");
                        document.getElementById("btn_desembolsar").disabled = false;
			return false;
		} 
		
		if ( !esFechaValida(window.document.forma.fechaDesembolso,false) ){
				alert("La Fecha de Realización de la consulta es inválida");
                                document.getElementById("btn_desembolsar").disabled = false;
				return false;
		}
                
                if (window.document.forma.fechaDesembolso.value != window.document.forma.fechaDispersion.value){
                    if(!confirm("Las Fechas Desembolso y Dispersion son diferente. ¿Desea continuar?")){
                        document.getElementById("btn_desembolsar").disabled = false;
                        return false;
                    }
		}
                
                if (window.document.forma.fondeador.value == 0){
                    alert("Debe de seleccionar un Fondeador");
                    document.getElementById("btn_desembolsar").disabled = false;
                    return false;
		}

	<%if ( diainhabil ){%>
		alert("El desembolso no puede efectuarse debido a que es fecha de día inhábil");
                document.getElementById("btn_desembolsar").disabled = false;
		return false;
	<%}%>
			
		document.getElementById("btn_desembolsar").disabled = true;
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}


	function confirmaDesembolsaCicloGrupal(integrantes){
            document.getElementById("btn_confirmaDesembolsar").disabled = true;
            window.document.forma.command.value = 'confirmaDesembolsaAdicional';  
            if  (validaCheckAdicional(integrantes)){
                if (validaCheckProcentaje(integrantes)){
                    res = confirm('La información que está apunto de guardar no podrá ser modificada. ¿Esta seguro de que desea registrar el incremento de los integrantes?');
                    if ( !res ){
                        document.getElementById("btn_confirmaDesembolsar").disabled = false;
                        return res;
                    }           
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }else{
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
               
            }
            else {
            document.getElementById("btn_confirmaDesembolsar").disabled = false;
                return false;
            
            }


	}
	
	function regresaAGrupo(){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.action="controller";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function consultaPagosComunales(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='validarPagoComunal';
                window.document.forma.submit();
	}

	function muestraDocumento(tipo){
                window.document.forma.command.value='muestraDocumento';
		//window.document.forma.target="_blank";
		window.document.forma.tipo.value=tipo;
                window.document.forma.submit();
                
	}
	
	function consultaCliente(idCliente){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='consultaCliente';
		window.document.forma.idCliente.value=idCliente;
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function cuentaDesembolsos(integrantes){
		var numchecked = 0;
		for ( var i=0; i < integrantes ; i++ ){
			var nombreDinamico = "desembolso"+i;
			var elemento = document.getElementById(nombreDinamico);
			if ( elemento.checked == true )
				numchecked++;
		}
		
		return numchecked;
	}
	
	function consultaEstadoCuentaGrupal(){
                document.getElementById("td").disabled = true;
		window.document.forma.action="controller";
		window.document.forma.command.value="consultaEstadoCuentaGrupal";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}

	function guardaOtrosDatos(){
                document.getElementById("guardaDatosExtra").disabled = true;
		window.document.forma.action="controller";
		window.document.forma.command.value="guardaOtrosDatos";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}

	function habilitaCierreCiclo(){
		if( window.document.forma.estatus.value==2 ){
			window.document.forma.guardaCiclo.disabled=false;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = true;
		}else{
			window.document.forma.guardaCiclo.disabled=true;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = false;
		}
	}
	function habilitaBoton(){
		if( window.document.forma.estatus.value==2 ){
			window.document.forma.guardaCiclo.disabled=true;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = false;
		}else{
			window.document.forma.guardaCiclo.disabled=false;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = true;
		}
	}
	
	
	function refinanciaCiclo(){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value = 'refinanciaCiclo';
		window.document.forma.idCiclo.value = 0;
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function desembolsaCicloGrupalProg(){
		
		alert("Por favor ingresar la fecha del desembolso y posteriormente pulsar el boton Desembolsar")
		$("#ocultaFechaDesembolso").css("display", "block");
                document.getElementById("boton").disabled = false;
		return false;
	}
        
        function capturaControlPagos(){
            window.document.forma.command.value = 'capturaControlPagos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        
        function cargarArchivos(tipo){
            window.document.forma.action="guardaArchivosGrupal.jsp";
            window.document.forma.target="_self";
            if(window.document.forma.autorizacion.value != ""){
                if(tipo == "fileDocumentacion" && (window.document.forma.autorizacion.indexOf(".zip") != -1 || window.document.forma.autorizacion.indexOf(".rar") != -1)){
                    document.body.style.cursor = "wait";
                     window.document.forma.submit();
                } else if(tipo == "fileFicha"){
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                } else {
                    alert("El Archivo no se encuentra en un formato permitido");
                }
            } else {
                alert("Debe de agregar la ruta del archvo");
            }
            //window.document.forma.submit();
        }
        
        function descargaFichaSeguro(){
            window.document.forma.command.value = 'descargaArchivo';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        function days(date){
            var day = date.getDay();
            var diaHabilitado = window.document.forma.diaReunion.value;
            return [(day==diaHabilitado),""];
        }
        function resetDate(){
             window.document.forma.fechaDispersion.value='';
        }
        
       function ayudaHistorialComentarios(numEquipo, numCiclo) {
           params = "?command=historialComentarios"+"&idGrupo="+numEquipo+"&idCiclo="+numCiclo;
           url = "/CEC/controller";            
           abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
       }
       function agregaComentario(){
           if(window.document.forma.comentario.value==""){
               alert("No has ingresado comentarios")  
               return false;
           } else if(!confirm("¿Deseas agregar el comentario?")){
               return false;
           } else {
               window.document.getElementById("boton_agrega_comentario").disabled=true;
               window.document.forma.command.value='agregaComentarioCiclo';
               window.document.forma.submit();
           }
       }
       function validaMonto(monto){
           if(monto > 40000){
               alert("El monto no puede ser mayor a $40,000.00");
               window.document.getElementById("guardaCiclo").disabled=true;
           } else {
               window.document.getElementById("guardaCiclo").disabled=false;
           }
       }
       function validaMontoIntegrantes(integrantes){
           for ( var i=0; i < integrantes ; i=i+1 ){
               var nombreDinamico = "monto"+i;
               var cliente = "idCliente"+i;
               var elemento = document.getElementById(nombreDinamico);
               if(elemento > 40000){
                   alert("El monto del cliente "+cliente+" no puede ser mayor a los $40,000.00");
                   return false;
               }
               return true;
            }
       }
       function cambiarDisplay(id) {
           if (!document.getElementById)
               return false;
           fila = document.getElementById(id);
           if (fila.style.display != "none") {
               fila.style.display = "none"; //ocultar fila
           } else {
               fila.style.display = ""; //mostrar fila
           }
       }
//-->
</script>
<script type="text/javascript">
                $(document).ready(function(){
                    $("#expanderHead").click(function(){
                        $("#expanderContent").slideToggle();
                        if ($("#expanderSign").text() == "+"){
                                $("#expanderSign").html("-")
                        }
                        else {
                                $("#expanderSign").text("+")
                        }
                    });
                });
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Alta de Adicional</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<%if(fldSeguro == null && ciclo.idCiclo > 0 && campSeguro.equals("si")){%>
<b><font color='red'>Se requiere subir la Ficha de pagos de Seguros</font><b><br><br>
<%}%>
<br>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaCicloGrupal">
<input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
<input type="hidden" name="idTipoCiclo" value="<%=ciclo.idTipoCiclo%>">
<input type="hidden" name="idCicloRefinancear" value="<%=ciclo.idCiclo%>" >
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="esNuevo" value="<%=validaCiclo%>" >
<input type="hidden" name="saldoT24" value="<%=ciclo.estatusT24%>">
<input type="hidden" name="idCiclo" <%if(idCiclo==0){%>value="<%=ciclo.idCiclo%>"<%}else{%>value="<%=idCiclo%>"<%}%> >
<input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
<input type="hidden" name="tipo" value="">
<input type="hidden" name="numIntegrantes" <%if(numIntegrantes==0 && ciclo.integrantes != null ){%>value="<%=ciclo.integrantes.length%>"<%}else{%>value="<%=numIntegrantes%>"<%}%> >
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<input type="hidden" name="segurosCompletos" value="<%=segurosCompletos%>">
<input type="hidden" name="desembolsado" value="<%=isDesembolsadoFlag%>">
<input type="hidden" name="idComision" value="<%=idComision%>">
<input type="hidden" name="semanaDisp">

<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">N&uacute;mero del grupo </td>
		<td width="50%">
			<input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.idGrupo)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre del grupo </td>
		<td width="50%">
			<input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Número de ciclo</td>
		<td width="50%">
			<input type="text" name="idCicloRead" size="10" maxlength="10" value="<% if ( ciclo.idCiclo!=0 ) out.print(HTMLHelper.displayField(ciclo.idCiclo)); %>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
		<td width="50%" align="right">Estado</td>
		<td width="50%">  
		<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Municipio</td>
		<td width="50%">  
		<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Colonia</td>
		<td width="50%">  
		<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">C&oacute;digo postal</td>
		<td width="50%">  
		<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Calle</td>
		<td width="50%">  
		<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero exterior</td>
		<td width="50%">  
		<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>" readonly="readonly" class="soloLectura">>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero interior</td>
		<td width="50%">  
		<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>" readonly="readonly" class="soloLectura">>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Estatus</td>
		<td width="50%">  
			<select name="estatus" id="estatus" size="1" <%if( isDesembolsadoFlag ){%>onchange="habilitaCierreCiclo()"<%}else{%>onchange="habilitaBoton()"<%} if(isDesembolsadoFlag && (ciclo.estatus==2 || ciclo.estatusT24!=3) && !esMesaControl){%>disabled="disabled"<%}%> >
                        
			<%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>

			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">D&iacute;a de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="diaReunion" size="1" disabled="disabled">
			<%=HTMLHelper.displayCombo(catDiasReunon, ciclo.diaReunion)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Hora de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="horaReunion" size="1" disabled="disabled">
			<%=HTMLHelper.displayCombo(catHorasReunion, ciclo.horaReunion)%>
			</select>
		</td>
	</tr>
        <tr>
                <td width="50%" align="right">Aperturador</td>
		<td width="50%">  
                    <select name="aperturador" size="1" disabled>
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catAperturador, ciclo.aperturador)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Ejecutivo</td>
		<td width="50%">  
			<select name="asesor" size="1" disabled="disabled">
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
			</select>
		</td>
                <input type="hidden" name="aperturador" value=<%=ciclo.aperturador%>>
	</tr>
	<tr>
		<td width="50%" align="right">Sucursal</td>
		<td width="50%">  
			<select name="coordinador" size="1" disabled="disabled">
			<%=HTMLHelper.displayCombo(catCoordinadores, grupo.sucursal)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por retraso</td>
		<td width="50%">  
			<input type="text" name="multaRetraso" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaRetraso)%>" readonly="readonly" class="soloLectura">>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por falta</td>
		<td width="50%">  
			<input type="text" name="multaFalta" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaFalta)%>" readonly="readonly" class="soloLectura">> 
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tasa</td>
		<td width="50%">
			<input type="text" name="tasaDescripcion" size="10" maxlength="8" value="<%=CatalogoHelper.getDescripcionTasa(tasa, catTasasGrupal)%>" readonly="readonly" class="soloLectura"> 
			<input type="hidden" name="idTasa" value="<%=tasa%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Plazo</td>
                <td><select name="plazo" id="plazo" size="1" onKeyPress="return submitenter(this,event)" disabled="disabled">
                    <%= HTMLHelper.displayCombo(catPlazos, plazo)%>
                    </select>
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Fecha de Dispersión</td>
                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" readonly="readonly">
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Dispersar por</td>
		<td width="50%">  
			<select name="bancoDispersion" size="1"  disabled="disabled">
			<%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
			</select>
		</td>
	</tr>
        <%if(request.isUserInRole("CAPTURA_GARANTIA") && CatalogoHelper.esEquipoAutorizadoGarantia(grupo.idGrupo)){
            if(ciclo.estatus == 1 || ciclo.estatus == 2){
                bloqueo = "disabled";
            }
        %>
        <tr>
		<td width="50%" align="right">% Garant&iacute;a</td>
		<td width="50%">  
                    <select name="garantia" size="1" <%=bloqueo%>>
			<%=HTMLHelper.displayCombo(catGarantia, ciclo.garantia)%>
			</select>
		</td>
	</tr>
        <%}else{%>
        <input type="hidden" name="garantia" value="3">
        <%}%>
        <tr>
            <td width="50%" align="right">Fondeador</td>
            <%if(request.isUserInRole("ANALISIS_CREDITO") && fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR){%>
            <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)" disabled="disabled">
                    <%= HTMLHelper.displayCombo(catFondeador, fondeador)%>
                </select>
            </td>
            <%} else {%>
            <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)" onfocus="this.oldvalue=this.value;this.blur();" onchange="this.value=this.oldvalue;">
                    <%= HTMLHelper.displayCombo(catFondeador, fondeador)%>
                </select>
            </td>
            <%}%>
	</tr>
        <tr>
            <td width="50%" align="right">Despacho</td>
            <%if(request.isUserInRole("ADM_PAGOS_MANAGER")){%>
            <td><select name="numDespacho" id="numDespacho" size="1" onKeyPress="return submitenter(this,event)">
                    <%= HTMLHelper.displayCombo(catDespachos, despacho)%>
                </select>
            </td>
            <%} else {%>
            <td><select name="numDespacho" id="numDespacho" size="1" onKeyPress="return submitenter(this,event)" disabled>
                    <%= HTMLHelper.displayCombo(catDespachos, despacho)%>
                </select>
            </td>
            <%}%>
	</tr>
	<%if( isRefinaceoFlag && ciclo.tablaAmortizacion==null ) {%>
		<tr>
			<td width="50%" colspan="2" align="center"><b>Resumen de refinanciamiento</b></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><br>
			<table border="0" width="60%">
	  			<tr>
				    <th>Capital vencido</th>
				    <th>Interes vencido c/iva</th>
				    <th>Interes moratorio c/iva</th>
				    <th>Saldo de multa c/iva</th>
				    <th>Total Vencido</th>
	  			</tr>
	  			<tr>
				    <td align="center"><%=ciclo.saldo.getCapitalVencido() %></td>
				    <td align="center"><%=ciclo.saldo.getSaldoInteresVencido()%></td>
				    <td align="center"><%=ciclo.saldo.getSaldoMora()+ciclo.saldo.getSaldoIVAMora()%></td>
				    <td align="center"><%=ciclo.saldo.getSaldoMulta()+ciclo.saldo.getSaldoIVAMulta()%></td>
				    <td align="center"><%=HTMLHelper.formatoMontoRefinanceado(ciclo)%></td>
	  			</tr>
			</table>
		</tr>
		<tr>
		<td colspan="2" align="center"><br>
			<b>Seleccione integrantes que conformaral el refinanciamiento.</b>
		</tr>
	<%} %>
	<tr>
		<td colspan="2" align="center"><br>
			<table border="1" width="90%">
				<tr>
					<td align="center">  
						
					</td>
					<td align="center">
						No.
					</td>
					<td align="center"> 
						Cliente
					</td>
					<td align="center"> 
						Solicitud
					</td>
					<td align="center">
						Nombre
					</td>
					<%if(validaCiclo == 1){%>
						<td align="center">
							Monto maximo posible
						</td>
					<%}%>
					<%if( isRefinaceoFlag ) {%>
						<td align="center">
							Monto a desembolsar
						</td>
						<td align="center">
							Comisión
						</td>
						<td align="center">
							Monto con comisión
						</td>
						<td align="center">
							Monto Refinanciado
						</td>
						<td align="center">
							Total
						</td>		
					<%} else{%>
						<td align="center">
							Monto a desembolsar
						</td>
						<td align="center">
							Monto con Seguro Financiado
						</td>
                                                <td align="center">
							Monto de Seguro
						</td>
						<td align="center">
							Comisión
						</td>
					<%} %>
					<td align="center">
						Calificación
					</td>
					<td align="center">
						Medio Cobro
					</td>
					<td align="center">
						Rol
					</td>
                                        <td align="center">
						Procentaje
					</td>
                                        <%if(opcion == 0){%>
                                        <td align="center">
                                            Seguro
					</td>
                                        <td align="center">
                                            Actividad
					</td>
                                        <td align="center">
                                            Activo en
					</td>
                                        <%}%>
				</tr>
<%for ( int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){ 
	String estilo = "";
	
            if(ciclo.integrantes[i].estatus == 2){ //Esta cancelado
                if(ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)//Interciclo con dispercion Semana 2
                    estilo = "clienteInterCiclo2Cancelado";
                else if(ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) //Interciclo con dispercion Semana 4
                    estilo = "clienteInterCicloCancelado";
                else
                    estilo = "soloLecturaRojo";

            }else{
                if(ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)//Interciclo con dispercion Semana 2
                    estilo = "clienteInterCiclo2";
                if(ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) //Interciclo con dispercion Semana 4
                    estilo = "clienteInterCiclo";
            }
            if(estilo.equals("")){
                if(i%2==0){
                    estilo = "AdicionalFondoAzul";
                }
            }
        
%>
				<tr class="<%=estilo%>" >
					<td align="center">
                                               <%=HTMLHelper.displayCheck("desembolso"+i,"desembolso"+i,false,
                                                       (ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO ||
                                             ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 ||
                                             ciclo.integrantes[i].monto < 8000 ||
                                             ciclo.integrantes[i].estatus == ClientesConstants.INTEGRANTE_CANCELADO||
                                             ciclo.integrantes[i].tipo_adicional != 0 ||
                                             /*En caso de no ser mujer no se la da la opcion de credito adicional*/   
                                             ciclo.integrantes[i].sexoCliente != 1 ||
                                             /* En caso de que el cliente no tenga al menos 1 solicitudes desembosaldas 
                                                al menos en el ultimo año
                                                no se da la opcion de credito adicional */  
                                             ciclo.integrantes[i].totalSolicitudesDesembolsados < 1)|| 
                                             /**
                                              * condicion que verifica que el integrante ciclo tenga contratado un seguro
                                              * de vida
                                              */
                                             ciclo.integrantes[i].contratacionSeguro != ClientesConstants.CATALOGO_BINARIO_OP_SI
                                                        )%>
					</td>
					<td align="center"> 
						<%=i+1%>
					</td>
					<td align="center" id="td"> 
						<input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
						<a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a>
					</td>
					<td align="center"> 
						<input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
						<%=ciclo.integrantes[i].idSolicitud%>
					</td>
					<td align="center">
						<input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
						<%=ciclo.integrantes[i].nombre%>
					</td>
					<%if(validaCiclo == 1){%>
						<td align="right">
							<input type="hidden" name="montoMaximo<%=i%>" id="montoMaximo<%=i%>" value="<%=HTMLHelper.formatoMonto( FormatUtil.redondeaMoneda( ClientesUtil.calculaMontoEscalera(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud) ))%>">
							<%=HTMLHelper.formatoMonto( FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoEscalera(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud) ))%>
						</td>
					<%}%>
					<%if( isRefinaceoFlag ) {%>
						<%if( validaCiclo==1 ){%>
							<td align="right">
								<input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].montoDesembolso )%>" size="8" maxlength="9">
							</td>
						<%}else{%>
							<td align="right"> 
								<input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].montoDesembolso )%>">
								<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].montoDesembolso )%>
							</td>
						<%}%>
						<td align="right">
							<input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
							<%=CatalogoHelper.getDescripcionComision(ciclo.integrantes[i].comision, catComisionesGrupal)%>
						</td>
                                                <td align="right"> <%-- Se quita el monto por comision y se cambia por Monto Con Seguro F. --%> 
							<input type="hidden" name="montoComision<%=i%>" id="montoComision<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
							<input type="hidden" name="oPago<%=i%>" value="<%=ciclo.integrantes[i].ordenPago%>">
							<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].montoConSeguro)%>
						</td>
                                                <td align="right"> <%-- Monto del Seguro F.--%> 
							<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].costoSeguro)%>
						</td>
						<td align="right">
							<input type="hidden" name="montoRefinanciado<%=i%>" value="<%=ciclo.integrantes[i].montoRefinanciado%>">
							<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].montoRefinanciado )%>
						</td>
						<td align="right">
							<input type="hidden" name="total<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
							<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].monto+ciclo.integrantes[i].montoRefinanciado)%>
						</td>
					<%}else{ %>
						<%if(validaCiclo == 1 && ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) > ClientesUtil.calculaMontoMaximoGrupal(ciclo.idTipoCiclo, validaCiclo, ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) , catMontosMaximosCiclo) ){%>
							<td align="right">
								<input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>" size="8" maxlength="9">
							</td>
						<%}else{%>
                                                    <%if(request.isUserInRole("ANALISIS_CREDITO") && ciclo.desembolsado == 0){%>
                                                        <td align="center"> 
                                                            <input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>" size="8" maxlength="8" onchange="validaMonto(this.value)">
							</td>
                                                    <%} else {%>
							<td align="right"> 
								<input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>">
								<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>
							</td>
                                                    <%}
                                                }%>
						<td align="right"> <%-- Se quita el monto por comision y se cambia por Monto Con Seguro --%> 
							<input type="hidden" name="montoComision<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
							<input type="hidden" name="oPago<%=i%>" value="<%=ciclo.integrantes[i].ordenPago%>">
							<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].montoConSeguro)%>
						</td>
                                                <td align="right"> <%-- Monto del Seguro F.--%>  
							<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].costoSeguro)%>
						</td>
						<td align="right">
							<input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
							<%=CatalogoHelper.getDescripcionComision(ciclo.integrantes[i].comision, catComisionesGrupal)%>
						</td>
					<%}%>
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || ciclo.integrantes[i].calificacion == 0 ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="#33FF33"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
					<%} %>
			
					<% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR ){ 
                                                if (ciclo.integrantes[i].aceptaRegular>0){%>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                                <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="<%=ciclo.integrantes[i].aceptaRegular%>">
						<td BGCOLOR="#ffff00"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}else{%>
                                                <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="#BDBDBA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}%>
					<%} %>
					
					<% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="#FF0000"><center><b>&nbsp;</b></center></td>
					<%} %>
					
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="#33FF99"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
					<%} %>
					
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA ){ sinConsulta=true;%>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="#FFEBCD"><center><font style="font-style: oblique;">Sin consulta</font></center></td>
					<%} %>
                                        <%if (ciclo.idCiclo > 0){
                                            medio = (ciclo.integrantes[i].medioCobro==1 ? "TAR" : "ODP");
                                        }else{
                                            medio = (ciclo.integrantes[i].medioDisp==1 ? "TAR" : "ODP");
                                        }%>
					<td align="center">
						<input type="hidden" name="medioCobro<%=i%>" value="<%=ciclo.integrantes[i].medioCobro%>">
						<%=HTMLHelper.displayField(medio)%>
					</td>
					<td align="center">
						<select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>" >
						<%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
						</select>
					</td>
                                        <%if(ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO ||
                                             ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 ||
                                             ciclo.integrantes[i].monto < 8000 ||
                                             ciclo.integrantes[i].estatus == ClientesConstants.INTEGRANTE_CANCELADO||
                                             ciclo.integrantes[i].tipo_adicional != 0 ||
                                             /*En caso de no ser mujer no se la da la opcion de credito adicional*/   
                                             ciclo.integrantes[i].sexoCliente != 1 || 
                                             /* En caso de que el cliente no tenga al menos 1 solicitudes desembosaldas 
                                                en el ultimo año
                                                no se da la opcion de credito adicional */  
                                             ciclo.integrantes[i].totalSolicitudesDesembolsados < 1 || 
                                             /**
                                              * condicion que verifica que el integrante ciclo tenga contratado un seguro
                                              * de vida
                                              */
                                             ciclo.integrantes[i].contratacionSeguro != ClientesConstants.CATALOGO_BINARIO_OP_SI
                                                        ){%>
                                        <td align="center">
						<select disabled name="porcentaje<%=i%>" id="porcentaje<%=i%>" size="1" value="<%=ciclo.integrantes[i].idPorcentajeAdicional%>" >
						<%=HTMLHelper.displayCombo(catPorcentajeAdicional, 0)%>
						</select>
					</td>
                                        <%}else{%>
                                        <td align="center">
						<select name="porcentaje<%=i%>" id="porcentaje<%=i%>" size="1" value="<%=ciclo.integrantes[i].idPorcentajeAdicional%>" >
						<%=HTMLHelper.displayCombo(catPorcentajeAdicional, ciclo.integrantes[i].idPorcentajeAdicional)%>
						</select>
					</td>
                                        <%}%>
                                        <%if(opcion == 0){%>
                                        <td align="center">
                                            <input type="hidden" name="conseguro<%=i%>" value="<%=ciclo.integrantes[i].seguro%>">
                                            <%=(ciclo.integrantes[i].seguro > 0 ?"OK" : "--")%>
					</td>
                                        <td align="center">
                                            <input type="hidden" name="conempleo<%=i%>" value="<%=ciclo.integrantes[i].empleo%>">
                                            <%=(ciclo.integrantes[i].empleo != 0 ?"OK" : "--")%>
					</td>
                                        <td align="center">
                                            <input type="hidden" name="activoen<%=i%>" value="<%=ciclo.integrantes[i].grupo%>">
                                            <%=(ciclo.integrantes[i].grupo != "" ?HTMLHelper.displayField(ciclo.integrantes[i].grupo): "--")%>
					</td>
                                        <%}%>
				</tr>
<%
total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ));
totalConComision += ciclo.integrantes[i].monto;
totalConSegugoFinanciado += ciclo.integrantes[i].montoConSeguro;
totalCostoSeguro += ciclo.integrantes[i].costoSeguro;
if( isRefinaceoFlag ){
	totalRefinanciado += ciclo.integrantes[i].monto+ciclo.integrantes[i].montoRefinanciado;
	totalVencido += ciclo.integrantes[i].montoRefinanciado;
	}
numIntegrantes = i;
}%>
				<tr>
					<td colspan="5" align="center">  
						Total 
					</td>
					<%if(validaCiclo == 1){%>
					<td>
						&nbsp;
					</td>
					<%}%>
					<td align="right">
						<input type="hidden" name="montoTotal" value="<%=total%>">
						<%=HTMLHelper.formatoMonto(total) %>
					</td>
					<%if( isRefinaceoFlag ) {%>
						<td align="right">
							&nbsp;
						</td>
					<%}%>
					<td align="right">
						<input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
						<%-- Total monto con seguro --%>
                                                <%=HTMLHelper.formatoMonto(totalConSegugoFinanciado) %>
					</td>
                                        <td align="right">
						<%-- Total monto con seguro --%>
                                                <%=HTMLHelper.formatoMonto(totalCostoSeguro)%>
					</td>
					<%if( isRefinaceoFlag ) {%>
						<td align="right">
							<input type="hidden" name="totalVencido" value="<%=HTMLHelper.formatoMonto(totalVencido)%>">
							<%=HTMLHelper.formatoMonto(totalVencido) %>
						</td>
						<td align="right">
							<input type="hidden" name="montoTotalConComision" value="<%=totalRefinanciado%>">
							<%=HTMLHelper.formatoMonto(totalRefinanciado) %>
						</td>
					<%}else{%>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp; 
						</td>
					<%}%>
					<td>
						&nbsp;
					</td>
					<td align="center">
						<%if( ciclo.idTipoCiclo==1 && ciclo.estatus==1 && ciclo.saldo!=null && ciclo.saldo.getCapitalVencido()!=0 && ciclo.estatusT24==2 ){%>
						 &nbsp;	<!--<INPUT TYPE="button" id="boton" value="Refinanciar" onClick="refinanciaCiclo()" align="center">-->
						<%}else{%>
						 &nbsp;
						<%}%>
					</td>
                                        <%if(opcion == 0){%>
                                        <td align="center">
                                            &nbsp;
					</td>
                                        <td align="center">
                                            &nbsp;
					</td>
                                        <td align="center">
                                            &nbsp;
					</td>
                                        <%}%>
				</tr>
			</table>
		</td>
	</tr>

        
        <%
if( opcion > 0 ){
%>
<tr><td>&nbsp;</td></tr>
	<tr>
            <td colspan="2" align="center">
                <table width="90%" border="2" cellpadding="0" bordercolor="#0040FF">
                    <tr>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTRATO')">Consulta contrato</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CARATULA')">Consulta car&aacute;tula</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('AVISOPRIVACIDAD')">Consulta Aviso de privacidad</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTROLDEPAGOS')">Control de pagos</a></td>
                        <td align="center" id="td"><a href="generaFichasPagos.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Fichas de pago</a></td>
                        <td align="center" id="td"><a href="#" onClick="consultaEstadoCuentaGrupal()">Estado de cuenta</a></td>
                        <td align="center" id="td"><a href="#" onClick="consultaPagosComunales()"> Consulta pagos comunales</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('HOJARESUMEN')">Hoja resumen</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('REGLAMENTO')">Reglamento Interno</a></td>
                    </tr>
                </table>
            </td>
	</tr>
       
        
<%}%>
        
        
        <tr>
            <td align="center" colspan="2">
                <input type="button" id="boton" value="Regresar" onClick="regresaAGrupo()">
                <br><br><center id="ocultaFechaDesembolso"><input type="text" name="fechaDesembolso" id="fechaDesembolso" size="10" maxlength="10" >(dd/mm/aaaa)</center>			
            </td>
	</tr>
	<tr>
            <td align="center" colspan="2">
                    <br><input <%=(fechaPagoValidaDesembolso && solicitudAdicional && estatusCredito && docLegalesCargados) ?"":"disabled" %> type="button" id="btn_confirmaDesembolsar" value="Confirma Desembolso" onClick="confirmaDesembolsaCicloGrupal(<%=numIntegrantes+1%>)" >
	    </td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<br><INPUT TYPE="button" value="Imprimir" onClick="window.print()">
		</td>
	</tr>
	<tr>
            <td align="center" colspan="2">
                <%if(ciclo.idCiclo > 0){%>
                <input type="button" value="Documentaci&oacute;n" onClick="ayudaCargaArchivoAdicional()"/>
                <input type="hidden" name="tipoArchivo" value="fileDocumentacion"/>
                <input type="hidden" name="conSeguro" value="0"/>
                <input type="hidden" name="semanaAdicional" value="<%=semanaAdicional%>"/>
                <%}%>
            </td>
	</tr>      
</table>
</form>
		</td>
	</tr>
</table>
</body>
</html>

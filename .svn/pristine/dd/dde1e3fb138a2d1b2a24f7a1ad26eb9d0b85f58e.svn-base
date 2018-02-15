<%@page import="com.sicap.clientes.dao.InformacionCrediticiaDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    CicloGrupalVO cicloGrupalVO = (CicloGrupalVO)session.getAttribute("CICLOGRUPAL");
    ClienteVO clienteAyuda = (ClienteVO) session.getAttribute("ayudarfc");
    TreeMap catEstados = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADOS);
    TreeMap catMedios = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MEDIOS);
    TreeMap catSexo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SEXO);
    TreeMap catNacionalidades = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NACIONALIDADES);
    TreeMap catTiposIdentificacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_IDENTIFICACION);
    TreeMap catEstadoCivil = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADO_CIVIL);
    TreeMap catRolesHogar = CatalogoHelper.getCatalogo("c_roles_hogar");
    TreeMap catNivelEstudios = CatalogoHelper.getCatalogo("c_niveles_estudio");
    TreeMap catTipoVialidades = CatalogoHelper.getCatalogo("c_tipoVialidad");
    TreeMap catTipoAsentameintos = CatalogoHelper.getCatalogo("c_tipoAsentamiento");    
    TreeMap catBinario = CatalogoHelper.getCatalogoBinario();
    TreeMap catSituacionVivienda = null;
    TreeMap catDestinoCredito = null;
    //TreeMap catBinario = CatalogoHelper.getCatalogoBinario();
    //TreeMap catEjecutivosCredito = CatalogoHelper.getCatalogoEjecutivos(cliente.idSucursal);
    TreeMap catEjecutivosCredito = CatalogoHelper.getCatalogoEjecutivos(cliente.idSucursal, "A");
    TreeMap catDepEconomicos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEP_ECONOMICOS);
    TreeMap catGrupos = CatalogoHelper.getCatalogoGruposSucursal(cliente.idSucursal);
    TreeMap catOperaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OPERACIONES);
    boolean consultaCC = false;
    boolean veInterCiclo = false;
    boolean interCiclo = false;
    boolean CambiointerCiclo = false;
    boolean DocCompleta =false;
    veInterCiclo = CatalogoHelper.esSucursalInterCiclo(cliente.idSucursal);
    String mensajeDoc = "";
    if (request.isUserInRole("ANALISIS_CREDITO")) {
        CambiointerCiclo = true;
    }
    Date fechaConCC = new java.sql.Date(System.currentTimeMillis());

    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");

    SolicitudVO solicitud = new SolicitudVO();
    DireccionVO direccion = new DireccionVO();

    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
    if (idSolicitud == 0) {
        idSolicitud = ((Integer) request.getAttribute("ID_SOLICITUD")).intValue();
    }
    int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
    if (cliente.solicitudes != null && cliente.solicitudes.length > 0) {
        solicitud = cliente.solicitudes[indiceSolicitud];
        fechaConCC = new InformacionCrediticiaDAO().getMaxFechaConsulta(cliente.idCliente);
        if (solicitud.consultaCC == 1) {
            consultaCC = true;
        }
        if (solicitud.subproducto == ClientesConstants.ID_INTERCICLO) {
            interCiclo = true;
        }
        if (solicitud.documentacionCompleta == 1) {
            DocCompleta = true;
        }
        if (solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.VIVIENDA || solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR) {
            catDestinoCredito = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_CONSUMO);
            catSituacionVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA, 5);
        } //if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
        else {
            catDestinoCredito = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_MICRO);
            catSituacionVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA);
        }
    }
    if (cliente.direcciones != null) {
        direccion = cliente.direcciones[0];
    }
    TreeMap catFrecuenciaPago = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
    TelefonoVO telefonoPrincipal = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_PRINCIPAL);
    TelefonoVO telefonoRecados = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_RECADOS);
    TelefonoVO telefonoCelular = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_CELULAR);

    if (clienteAyuda != null) {
        cliente.nombre = clienteAyuda.nombre;
        cliente.aPaterno = clienteAyuda.aPaterno;
        cliente.aMaterno = clienteAyuda.aMaterno;
        cliente.fechaNacimiento = clienteAyuda.fechaNacimiento;
    }
    double montoMaximo = SolicitudUtil.montoMaximoSolicitud(cliente);
    String habilitado = "disabled";
    if (request.isUserInRole("SOPORTE_OPERATIVO")) {
        habilitado = "";
    }
    if(session.getAttribute("MENSAJEDOC")!=null)
        mensajeDoc = (String)session.getAttribute("MENSAJEDOC");
    int cicloFirmaConUltimoCambioDoctos = com.sicap.clientes.util.SolicitudUtil.DeterminaCicloUltimoCambioEnDocumentacion(cliente);
%>

<html>
    <head>
        <title>Alta/Modificaci&oacute;n de cliente</title>

        <script>
            function guardaDatosCliente(tipoOperacion, montoMaximo) {
                document.getElementById("boton").disabled = true;
                var paternoSes = "<%=cliente.aPaterno%>";
                var maternoSes = "<%=cliente.aMaterno%>";
                window.document.forma.command.value = 'guardaDatosCliente';
                if (window.document.forma.solicitudFechaFirma.value != '') {
                    if (!esFechaValida(window.document.forma.solicitudFechaFirma, false) || esPosterior(window.document.forma.solicitudFechaFirma.value, 'Hoy')) {
                        alert("La Fecha de firma es inválida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de firma de la solicitud");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                <%if (!mensajeDoc.equals("") && solicitud.documentacionCompleta==0){%>
                    if(!window.document.forma.DocCompleta.checked){
                        alert("Debe indicar que subi\u00f3 la documentaci\u00f3n completa en esta solicitud");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                <%}%>
                if (<%=veInterCiclo%>) {
                    if (window.document.forma.interCiclo.checked == true) {
                        if (!confirm("Esta seguro que desea validar que el cliente es apto para Inter-Ciclo?"))
                            window.document.forma.interCiclo.checked = false
                    }
                }
                if (window.document.forma.ejecutivo.value == 0) {
                    alert('Debe seleccionar un ejecutivo');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.medio.value == 0) {
                    alert('Debe seleccionar como se enteró del servicio');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.nombre.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.nombre.value)) {
                    alert('Debe introducir un Nombre válido');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                //if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aPaterno.value) || window.document.forma.aPaterno.value.toUpperCase()=='X'){
                if ((!esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aPaterno.value) || window.document.forma.aPaterno.value.toUpperCase() == 'X') && window.document.forma.aPaterno.value != paternoSes) {
                    alert('Debe introducir un Apellido Paterno válido');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                //if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aMaterno.value) || window.document.forma.aMaterno.value.toUpperCase()=='X'){
                if ((!esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aMaterno.value) || window.document.forma.aMaterno.value.toUpperCase() == 'X') && window.document.forma.aMaterno.value != maternoSes) {
                    alert('2Debe introducir un Apellido Materno válido');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.aPaterno.value == 'X')
                    window.document.forma.aPaterno.value = '';
                if (window.document.forma.aMaterno.value == 'X')
                    window.document.forma.aMaterno.value = '';
                if (window.document.forma.fechaNacimiento.value != '') {
                    if (!esFechaValida(window.document.forma.fechaNacimiento, false)) {
                        alert("La Fecha de nacimiento es inv\u00e1lida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    } else if (!esEdadValida(window.document.forma.fechaNacimiento.value, tipoOperacion)) {
                        alert("La edad del cliente está fuera del rango permitido");
                        if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                            if (!confirm("Desea guaradr la información del cliente")) {
                                document.getElementById("boton").disabled = false;
                                return false;
                            }
                        } else {
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                    }
                } else {
                    alert("Debe capturar la Fecha de nacimiento");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.curp.value!='' && !validaCURP(window.document.forma.curp.value)){
                    alert('La CURP no corresponde a los datos del cliente');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.idGrupo.value == 0) {
                    alert('Debe seleccionar un Grupo');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.entidadNacimiento.value == 0) {
                    alert('Debe seleccionar una Entidad de Nacimiento');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.sexo.value == 0) {
                    alert('Debe seleccionar sexo');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.dependientesEconomicos.value == 0 || !esEntero(window.document.forma.dependientesEconomicos.value)) {
                    alert('Debe seleccionar un numero de Dependientes Economicos');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.nacionalidad.value == 0) {
                    alert('Debe seleccionar nacionalidad');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.tipoIdentificacion.value == 0) {
                    alert('Debe seleccionar tipo de identificaci\u00f3n');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.numeroIdentificacion.value == '') {
                    alert('Debe introducir un Número de Indentificaci\u00f3n v\u00e1lido');
                    document.getElementById("boton").disabled = false;
                    return false;
                } else {
                    if (window.document.forma.tipoIdentificacion.value == 1 && !esClaveElector(window.document.forma.numeroIdentificacion.value)) {
                        alert('El número de identificación es inválido, para credencial de IFE debe usar la clave de elector');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                if (window.document.forma.estadoCivil.value == 0) {
                    alert('Debe seleccionar Estado Civil');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.rolesHogar.value == 0) {
                    alert('Debe seleccionar el Rol en el Hogar');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.nivelEstudios.value == 0) {
                    alert('Debe seleccionar el Nivel de Estudios');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.LenguaIndigena.value == 0) {
                    alert('Debe indicar si el cliente habla una lengua indigena');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.Discapacidad.value == 0) {
                    alert('Debe indicar si el cliente tiene una discapacidad');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.UsodeInternet.value == 0) {
                    alert('Debe indicar si el cliente Usa internet');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.RedesSociales.value == 0) {
                    alert('Debe indicar si el cliente usa Redes Sociales');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.cp.value == '') {
                    alert("Debe capturar el Codigo Postal");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.localidad.value == '') {
                    alert("Debe capturar la Localidad");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.TipoAsentameinto.value == 0) {
                    alert("Debe capturar el Tipo de Asentamiento");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.TipoVialidad.value == 0) {
                    alert("Debe capturar el Tipo de Vialidad");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.calle.value == '') {
                    alert('Debe introducir una Calle');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.numeroExterior.value == '' || !esEntero(window.document.forma.numeroExterior.value)) {
                    alert('Debe introducir un Numero exterior de Domicilio');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                // Se pide telefono Principal con lada o que el de recados sea obligatorio
                if (window.document.forma.telefonoPrincipal.value == '' && window.document.forma.telefonoCelular.value == '') {
                    if (window.document.forma.telefonoRecados.value == '' || !esTelefonoValido(window.document.forma.telefonoRecados.value)) {
                        alert('Debe introducir algun numero telefonico (incluir clave LADA)');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if (window.document.forma.nomContacto.value == '') {
                        alert('Debe introducir el nombre del contacto');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else if (window.document.forma.telefonoPrincipal.value != '' && !esTelefonoValido(window.document.forma.telefonoPrincipal.value)) {
                    alert('Debe introducir un Teléfono de casa válido (incluir clave LADA)');
                    document.getElementById("boton").disabled = false;
                    return false;
                } else if (window.document.forma.telefonoCelular.value != '' && !esTelefonoValido(window.document.forma.telefonoCelular.value)) {
                    alert('Debe introducir un Teléfono celular válido (incluir clave LADA)');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.idColonia.value == 0) {
                    alert("Debe capturar la direcci\u00f3n");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.correoElectronico.value != '') {
                    if (!esEmail(window.document.forma.correoElectronico.value)) {
                        alert("El correo es inv\u00e1lido");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                if (window.document.forma.situacionVivienda.value == 0) {
                    alert('Debe seleccionar Situaci\u00f3n de la Vivienda');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.antiguedadDomicilio.value == '' || !esMesesAnios(window.document.forma.antiguedadDomicilio.value)) {
                    alert('La Antigüedad en el Domicilio es inv\u00e1lida');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.montoSolicitado.value != '') {
                    if (!esMontoValido(window.document.forma.montoSolicitado.value)) {
                        alert("El monto es inv\u00e1lido");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar el monto");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.plazoSolicitado.value != '') {
                    if (!esPlazoValido(window.document.forma.plazoSolicitado.value, <%=solicitud.tipoOperacion%>)) {
                        alert("El plazo es inv\u00e1lido");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar el plazo");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.frecuenciaPagoSolicitada.value == 0) {
                    alert('Debe seleccionar La Frecuencia de Pago');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.destinoCredito.value == 0) {
                    alert('Debe seleccionar el Destino del Cr\u00e9dito');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.otroCredito.value == 0) {
                    alert('Debe seleccionar si ha solicitado otro credito');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.mejorIngreso.value == 0) {
                    alert('Debe seleccionar si ha mejorado sus ingresos');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.idProgProspera.value == '') {
                    alert('No puede estar vacio el numero para programa Prospera');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.idProgProspera.value)) {
                    alert('Numero invalido para Programa Prospera');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.montoSolicitado.value > montoMaximo && montoMaximo != '0.0') {
                    if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                        if (confirm('Está seguro de solicitar esa cantidad que supera el 30% extra del crédito anterior'))
                            document.body.style.cursor = "wait";
                        window.document.forma.submit();
                    } else {
                        alert('El monto solicitado excede por más del 30% el del útimo credito liquidado. Cantidad máxima: $' + montoMaximo);
                        document.body.style.cursor = "wait";
                        window.document.forma.submit();
                    }
                } else {
                    document.body.style.cursor = "wait";
                    var espera = deshabilitarElementos(false);
                    window.document.forma.submit();
                }
            }
            
            function guardaDatosBasicosCliente() {
                
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'guardaDatosCliente';
                var nombreSes = "<%=cliente.nombre%>";
                var paternoSes = "<%=cliente.aPaterno%>";
                var maternoSes = "<%=cliente.aMaterno%>";
                var fechaNacSes = "<%=Convertidor.dateToString(cliente.fechaNacimiento)%>";
                if (window.document.forma.solicitudFechaFirma.value != '') {
                    if (!esFechaValida(window.document.forma.solicitudFechaFirma, false) || esPosterior(window.document.forma.solicitudFechaFirma.value, 'Hoy')) {
                        alert("La Fecha de firma es inválida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de firma de la solicitud");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.nombre.value != nombreSes || window.document.forma.aPaterno.value != paternoSes || window.document.forma.aMaterno.value != maternoSes || window.document.forma.fechaNacimiento.value != fechaNacSes) {
                    if (window.document.forma.nombre.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.nombre.value)) {
                        alert('Debe introducir un Nombre válido');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if ((window.document.forma.aPaterno.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aPaterno.value)) && window.document.forma.aPaterno.value != paternoSes) {
                        alert('Debe introducir un Apellido Paterno válido');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if ((window.document.forma.aMaterno.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aMaterno.value)) && window.document.forma.aMaterno.value != maternoSes) {
                        alert('Debe introducir un Apellido Materno válido');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                        if (!confirm("Desea cambiar los datos de nombre del cliente")) {
                            document.getElementById("boton").disabled = false;
                            return false;
                        } else {
                            window.document.forma.autorizacionRFC.value = 1;
                        }
                    } else {
                        alert("No puede realizar cambios de datos en nombre del cliente");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                if (window.document.forma.idGrupo.value == 0) {
                    alert('Debe seleccionar un Grupo');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.entidadNacimiento.value == 0) {
                    alert('Debe seleccionar una Entidad de Nacimiento');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.sexo.value == 0) {
                    alert('Debe seleccionar sexo');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.estadoCivil.value == 0) {
                    alert('Debe seleccionar Estado Civil');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                
                    if ( window.document.forma.curp.value=='') {
                        alert('Debe ingresar una CURP');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if ( window.document.forma.curp.value.length !=18 ){
                        alert('Debe ingresar una CURP Completa');
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                
                if (window.document.forma.rolesHogar.value == 0) {
                    alert('Debe seleccionar el Rol en el Hogar');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.nivelEstudios.value == 0) {
                    alert('Debe seleccionar el Nivel de Estudios');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.cp.value == '') {
                    alert("Debe capturar el Codigo Postal");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.localidad.value == '') {
                    alert("Debe capturar la Localidad");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.calle.value == '') {
                    alert('Debe introducir una Calle');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.numeroExterior.value == '' || !esEntero(window.document.forma.numeroExterior.value)) {
                    alert('Debe introducir un N\u00famero exterior de Domicilio');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.destinoCredito.value == 0) {
                    alert('Debe seleccionar el Destino del Cr\u00e9dito');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.idProgProspera.value)) {
                    alert('Numero invalido para Programa Prospera');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                // No se pide el telefono principal como obligatorio
                //		if ( window.document.forma.telefonoPrincipal.value=='' || !esTelefonoValido(window.document.forma.telefonoPrincipal.value) ){
                //		   alert('Debe introducir un Tel\u00e9fono principal válido (incluir clave LADA)');
                //		   return false;
                //		}
            <%if (request.isUserInRole("ANALISIS_CREDITO") && (fechaConCC == null || FechasUtil.inBetweenDays(fechaConCC, new Date()) >= ClientesConstants.DIAS_HABILES_CONSULTA_CC)) {%>
                if (window.document.forma.consultaCC.checked == true) {
            <%if (solicitud.infoCreditoCirculo != null) {%>
                    if (!confirm("Ya esxiste una consulta de CC para esta solicitud.¿Desea realizarla?")) {
                        window.document.forma.consultaCC.checked = false;
                    }
            <%}%>
                }
            <%}%>
                document.body.style.cursor = "wait";
                var espera = deshabilitarElementos(false);
                window.document.forma.submit();
            }

            function goCicloGrupal(estatus) {
                document.getElementById("boton").disabled = true;
                document.getElementById("botonCiclo").disabled = true;
                if (estatus == 1)
                    window.document.forma.command.value = 'consultaCicloApertura';
                else
                    window.document.forma.command.value = 'consultaCicloGrupal';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuIzquierdo.jsp" flush="true"/>
    <center>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idLocalidad" value="<%=direccion.idLocalidad%>">
            <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
            <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
            <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
            <input type="hidden" name="autorizacionRFC" value="0">
            <input type="hidden" name="idCiclo" value="<%=solicitud.idCiclo%>">
            <!-- INICIO DEL CODIGO ANTERIOR -->
            <table border="0" width="100%">
                <tr>
                    <td align="center">
                        <h3>Alta/Modificaci&oacute;n de cliente</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <%if(!mensajeDoc.equals("") && solicitud.documentacionCompleta==0 && (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && solicitud.desembolsado != ClientesConstants.CANCELADO)){%>
                            <b><font color='<%=ClientesConstants.ERROR_COLOR %>'> <%=mensajeDoc%> </font></b>
                        <%}%>
                        <table border="0" cellpadding="0" width="100%">
                            <tr>
                                <td width="50%" align="right">N&uacute;mero de cliente</td>
                                <td width="50%">  
                                    <input type="text" name="idCliente" size="10" value="<%=cliente.idCliente%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">RFC</td>
                                <td width="50%">  
                                    <input type="text" name="rfc" size="13" maxlength="13" value="<%=cliente.rfc%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Producto</td>
                                <td width="50%">  
                                    <input type="text" name="descripcion" size="18" value="<%=HTMLHelper.getDescripcion(catOperaciones, solicitud.tipoOperacion)%>" readonly>
                                </td>
                            </tr>
                            <%if (veInterCiclo) {%>
                            <tr>
                                <td width="50%" align="right">Inter-Ciclo</td>
                                <td width="50%">  
                                    <%=HTMLHelper.displayCheck("interCiclo", interCiclo, CambiointerCiclo)%>
                                </td>
                            </tr>
                            <%}%>
                            <%if (request.isUserInRole("ANALISIS_CREDITO") && (fechaConCC == null || FechasUtil.inBetweenDays(fechaConCC, new Date()) >= ClientesConstants.DIAS_HABILES_CONSULTA_CC)) {%>
                            <tr>
                                <td width="50%" align="right">Consulta Circulo de Cr&eacute;dito</td>
                                <td width="50%">  
                                    <%=HTMLHelper.displayCheck("consultaCC", consultaCC)%>
                                </td>
                            </tr>
                            <%}%>
                                                      
                            <tr>
                                <td width="50%" align="right">Fecha de firma de solicitud</td>
                                <td width="50%">  
                                    <% if (cicloGrupalVO == null){%>  
                                    <input type="text" name="solicitudFechaFirma" id="solicitudFechaFirma" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaFirma)%>" >(dd/mm/aaaa)
                                    <%}else{ 
                                        if(cicloGrupalVO.estatus==ClientesConstants.CICLO_APERTURA||cicloGrupalVO.estatus==ClientesConstants.CICLO_PENDIENTE){%>
                                           <input type="text" name="solicitudFechaFirma" id="solicitudFechaFirma" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaFirma)%>" >(dd/mm/aaaa)
                                        <%}else{%>
                                           <input type="text" name="solicitudFechaFirma" id="solicitudFechaFirma" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaFirma)%>" <%=(request.isUserInRole("ANALISIS_CREDITO")?"":"disabled")%>>(dd/mm/aaaa)                                                                             
                                         <%}%>
                                    <%}%>
                                </td>
                            </tr>
                            
                            
                                   
                            
                           
                            <tr>
                                <td width="50%" align="right">Fecha de captura de solicitud</td>
                                <td width="50%">  
                                    <input type="text" name="solicitudFechaCaptura" id="solicitudFechaCaptura" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaCaptura, new Date())%>" readonly>(dd/mm/aaaa)
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Ejecutivo de cr&eacute;dito</td>
                                <td width="50%">
                                    
                                    <select name="ejecutivo" size="1">
                                        <option value="0">Seleccione...</option>
                                        <%=HTMLHelper.displayCombo(catEjecutivosCredito, solicitud.idEjecutivo)%>
                                    </select>
                                    
                                   
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">C&oacute;mo se enter&oacute; del servicio</td>
                                <td width="50%">  
                                    <select name="medio" size="1">
                                        <%=HTMLHelper.displayCombo(catMedios, solicitud.medio)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Nombres</td>
                                <td width="50%"> 
                                    <input type="hidden" name="nombreAnterior" value="<%=HTMLHelper.displayField(cliente.nombre)%>">
                                    <input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.nombre)%>" <%=habilitado%>>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Apellido paterno</td>
                                <td width="50%">  
                                    <input type="hidden" name="aPaternoAnterior" value="<%=HTMLHelper.displayField(cliente.aPaterno)%>">
                                    <input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aPaterno)%>" <%=habilitado%>>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Apellido materno</td>
                                <td width="50%"> 
                                    <input type="hidden" name="aMaternoAnterior" value="<%=HTMLHelper.displayField(cliente.aMaterno)%>">
                                    <input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aMaterno)%>" <%=habilitado%>>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de nacimiento</td>
                                <td width="50%">
                                    <input type="hidden" name="fechaNacimientoAnterior" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>" >
                                    <input type="text" name="fechaNacimiento" id="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>" <%=habilitado%>>(dd/mm/aaaa)
                                </td>
                            </tr>
                            <%if (solicitud.tipoOperacion == ClientesConstants.GRUPAL || solicitud.tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {%>
                            <tr>
                                <td width="50%" align="right">Grupo</td>
                                <td width="50%">  
                                    <select name="idGrupo" size="1">
                                        <option value="0">Seleccione...</option>
                                        <%=HTMLHelper.displayCombo(catGrupos, solicitud.idGrupo)%>
                                    </select>
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td width="50%" align="right">Entidad de nacimiento</td>
                                <td width="50%">
                                    <input type="hidden" name="entidadNacimientoAnterior" value="<%=cliente.entidadNacimiento%>">
                                    <select name="entidadNacimiento" size="1">
                                        <%=HTMLHelper.displayCombo(catEstados, cliente.entidadNacimiento)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Sexo</td>
                                <td width="50%">
                                    <input type="hidden" name="sexoAnterior" value="<%=cliente.sexo%>">
                                    <select name="sexo" size="1">
                                        <%=HTMLHelper.displayCombo(catSexo, cliente.sexo)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Dependientes econ&oacute;micos</td>
                                <td width="50%">  
                                    <select name="dependientesEconomicos" size="1">
                                        <%=HTMLHelper.displayCombo(catDepEconomicos, cliente.dependientesEconomicos)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Nacionalidad</td>
                                <td width="50%">  
                                    <select name="nacionalidad" size="1">
                                        <%=HTMLHelper.displayCombo(catNacionalidades, cliente.nacionalidad)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tipo de identificaci&oacute;n</td>
                                <td width="50%">  
                                    <select name="tipoIdentificacion" size="1">
                                        <%=HTMLHelper.displayCombo(catTiposIdentificacion, cliente.tipoIdentificacion)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero de identificaci&oacute;n</td>
                                <td width="50%">  
                                    <input type="text" name="numeroIdentificacion" size="30" maxlength="30" value="<%=HTMLHelper.displayField(cliente.numeroIdentificacion)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">CURP</td>
                                <td width="50%"><input type="text" name="curp" id="curp" size="18" maxlength="18" value="<%=HTMLHelper.displayField(cliente.curp)%>"></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Estado civil</td>
                                <td width="50%">  
                                    <select name="estadoCivil" size="1">
                                        <%=HTMLHelper.displayCombo(catEstadoCivil, cliente.estadoCivil)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Rol en el Hogar</td>
                                <td width="50%">  
                                    <select name="rolesHogar" size="1">
                                        <%=HTMLHelper.displayCombo(catRolesHogar, solicitud.rolHogar)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Nivel de Estudios</td>
                                <td width="50%">  
                                    <select name="nivelEstudios" size="1">
                                        <%=HTMLHelper.displayCombo(catNivelEstudios, cliente.nivelEstudios)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Lengua indigena</td>
                                <td width="50%">  
                                    <select name="LenguaIndigena" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, cliente.LenguaIndigena)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Discapacidad</td>
                                <td width="50%">  
                                    <select name="Discapacidad" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, cliente.Discapacidad)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Uso de Internet</td>
                                <td width="50%">  
                                    <select name="UsodeInternet" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, cliente.UsodeInternet)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Redes Sociales</td>
                                <td width="50%">  
                                    <select name="RedesSociales" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, cliente.RedesSociales)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Correo electr&oacute;nico</td>
                                <td width="50%">  
                                    <input type="text" name="correoElectronico" size="50" maxlength="50" value="<%=HTMLHelper.displayField(cliente.correoElectronico)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Estado</td>
                                <td width="50%">
                                    <input type="hidden" name="estadoAnterior" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>">
                                    <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Municipio</td>
                                <td width="50%">  
                                    <input type="hidden" name="municipioAnterior" value="<%=HTMLHelper.displayField(direccion.municipio)%>">
                                    <input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Colonia</td>
                                <td width="50%">  
                                    <input type="hidden" name="coloniaAnterior" value="<%=HTMLHelper.displayField(direccion.colonia)%>">
                                    <input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly>
                                </td>
                            </tr>

                            <tr>
                                <td width="50%" align="right">C&oacute;digo postal</td>
                                <td width="50%">  
                                    <input type="hidden" name="cpAnterior" value="<%=HTMLHelper.displayField(direccion.cp)%>">
                                    <input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly>
                                </td>
                            </tr>

                            <tr>
                                <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaLocalidad()">Ayuda Localidad</a></td>
                            </tr>
                            <tr>
                                
                            </tr>
                            <tr>
                                <td width="50%" align="right">Localidad</td>
                                <td width="50%">
                                    <input type="hidden" name="idLocalidadAnterior" value="<%=direccion.idLocalidad%>">
                                    <input type="text" name="localidad" size="40" maxlength="80" value="<%=HTMLHelper.displayField(direccion.localidad)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tipo Asentamiento</td>
                                <td width="50%">
                                    <input type="hidden" name="TipoAsentameintoAnterior" value="<%=direccion.tipoAsentamiento%>">
                                    <select name="TipoAsentameinto" size="1">
                                        <%=HTMLHelper.displayCombo(catTipoAsentameintos, direccion.tipoAsentamiento)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tipo Vialidad</td>
                                <td width="50%">  
                                    <input type="hidden" name="TipoVialidadAnterior" value="<%=direccion.tipoVialidad%>">
                                    <select name="TipoVialidad" size="1">
                                        <%=HTMLHelper.displayCombo(catTipoVialidades, direccion.tipoVialidad)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Calle</td>
                                <td width="50%">  
                                    <input type="hidden" name="calleAnterior" value="<%=HTMLHelper.displayField(direccion.calle)%>">
                                    <input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero exterior</td>
                                <td width="50%"> 
                                    
                                    <input type="hidden" name="numeroExteriorAnterior" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
                                    <input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero interior</td>
                                <td width="50%">
                                    <input type="hidden" name="numeroInteriorAnterior" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                                    <input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tel&eacute;fono Casa</td>
                                <td width="50%">  
                                    <input type="text" name="telefonoPrincipal" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoPrincipal.numeroTelefono)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tel&eacute;fono Celular</td>
                                <td width="50%">  
                                    <input type="text" name="telefonoCelular" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoCelular.numeroTelefono)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Nombre Contacto</td>
                                <td width="50%">  
                                    <input type="text" name="nomContacto" size="10" maxlength="70" value="<%=HTMLHelper.displayField(telefonoRecados.nomContacto)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tel&eacute;fono Contacto</td>
                                <td width="50%">  
                                    <input type="text" name="telefonoRecados" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoRecados.numeroTelefono)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Situaci&oacute;n de la vivienda</td>
                                <td width="50%">  
                                    <select name="situacionVivienda" size="1">
                                        <%=HTMLHelper.displayCombo(catSituacionVivienda, direccion.situacionVivienda)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Desde cuando vive en el domicilio </td>
                                <td width="50%">  
                                    <input type="text" name="antiguedadDomicilio" size="7" maxlength="7" value="<%=HTMLHelper.displayField(direccion.antDomicilio)%>"> (mm/aaaa)
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Monto solicitado</td>
                                <td width="50%">  
                                    <input type="text" name="montoSolicitado" size="10" maxlength="11" value="<%=HTMLHelper.formatoMonto(solicitud.montoSolicitado)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Plazo solicitado</td>
                                <td width="50%">  
                                    <input type="text" name="plazoSolicitado" size="5" maxlength="2" value="<%=HTMLHelper.displayField(solicitud.plazoSolicitado)%>">
                                    <% 	if (solicitud.tipoOperacion == ClientesConstants.GRUPAL || solicitud.tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {%>
                                    (Semanas)
                                    <%} else {%>
                                    (Meses)
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Frecuencia de pago solicitada</td>
                                <td width="50%">  
                                    <select name="frecuenciaPagoSolicitada" size="1">
                                        <%=HTMLHelper.displayCombo(catFrecuenciaPago, solicitud.frecuenciaPagoSolicitada)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Destino del cr&eacute;dito</td>
                                <td width="50%">  
                                    <select name="destinoCredito" size="1">
                                        <%=HTMLHelper.displayCombo(catDestinoCredito, solicitud.destinoCredito)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">¿Has Solicitado otro Cr&eacute;dito?</td>
                                <td align="left">
                                    <select name="otroCredito" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, solicitud.otroCredito)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">¿Ha Mejorado sus Ingresos?</td>
                                <td align="left">
                                    <select name="mejorIngreso" size="1">
                                        <%=HTMLHelper.displayCombo(catBinario, solicitud.mejorIngreso)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Numero Identificador de Programa Prospera</td>
                                <td align="left">
                                    <input type="text" name="idProgProspera" size="18" maxlength="18" value="<%=HTMLHelper.displayField(solicitud.idProgProspera)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Actualizaci&oacute;n de Documentos</td>
                                <td width="50%">  
                                    <%=HTMLHelper.displayCheckEvent("DocCompleta", DocCompleta, "changeDoc(this)")%>&nbsp 
                                    <% if(solicitud.fechaFirmaConUltimoCambioDoctos != null && cicloFirmaConUltimoCambioDoctos <= indiceSolicitud)
                                        {
                                    %>
                                        <input type="text" name="fechaFirmaConUltimoCambioDoctos" id="fechaFirmaConUltimoCambioDoctos" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaFirmaConUltimoCambioDoctos)%>" disabled>(dd/mm/aaaa)
                                    <%
                                        }
                                    %>
                                    
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                   
                                    <%
                                     if (request.isUserInRole("ANALISIS_CREDITO")) {%>
                                            <br><input type="button" id="boton0" value="Editar" onClick = "deshabilitarElementos(false);">
                                     <%}else if(indiceSolicitud == (cliente.solicitudes.length - 1))
                                     {
                                        if(cicloGrupalVO == null){
                                            %>
                                                <br><input type="button" id="boton0" value="Editar" onClick = "deshabilitarElementos(false);">
                                            <%
                                        }else if(cicloGrupalVO.estatus==ClientesConstants.CICLO_APERTURA || cicloGrupalVO.estatus==ClientesConstants.CICLO_PENDIENTE || cicloGrupalVO.estatus==ClientesConstants.CICLO_RECHAZADO)
                                         {      
                                        %>
                                            <br><input type="button" id="boton0" value="Editar" onClick = "deshabilitarElementos(false);">
                                        <%
                                         }
                                     }
                                    %>
                                </td>
                                <td align="left">
                                    <%if (request.isUserInRole("ANALISIS_CREDITO")) {%>
                                    <br><input type="button" id="boton" value="Enviar" onClick="guardaDatosBasicosCliente()">
                                    <%} else if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO && request.isUserInRole("ANALISIS_CREDITO")) {%>
                                    <br><input type="button" id="boton" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>)">
                                    <%} else if (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && solicitud.desembolsado != ClientesConstants.CANCELADO) {%>
                                    <br><input type="button" id="boton" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>, <%=montoMaximo%>)">
                                    <%}%>
                                </td>
                                
                            </tr>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <!-- FIN DEL CODIGO ANTERIOR -->		
        </form>
    </center>
    <script>
        
        function changeDoc(checkboxElem){
            if (checkboxElem.checked) {
                alert ("Recuerda cargar la informacion actualizada del cliente !" 
                        +"\n1.- Solicitud comunal"
                        +"\n2.- Comprobante de domicilio vigente"
                        +"\n3.- INE");
            }
        }
        function deshabilitarElementos(deshabilitarControl) 
        {

                formulario = document.forms[1];
                for(var i=0; i<formulario.length;i++) 
                {
                        if( !(formulario.elements[i].name == 'rfc'
                                || formulario.elements[i].name == 'idCliente'
                                || formulario.elements[i].name == 'descripcion'
                                || formulario.elements[i].name == 'consultaCC'
                                || formulario.elements[i].name == 'solicitudFechaFirma'
                                || formulario.elements[i].name == 'solicitudFechaCaptura'
                                || formulario.elements[i].name == 'ejecutivo'
                                || formulario.elements[i].name == 'medio'
                                || formulario.elements[i].name == 'rolesHogar'
                                || formulario.elements[i].name == 'nivelEstudios'
                                || formulario.elements[i].name == 'DocCompleta'
                                || formulario.elements[i].type == 'button' 
                                || formulario.elements[i].type == 'hidden' 
                              ))
                            {
                              formulario.elements[i].disabled = deshabilitarControl;
                              //alert(formulario.elements[i].type)
                            }
                            if(formulario.elements[i].name == 'nombre'
                                 || formulario.elements[i].name == 'aPaterno'
                                 || formulario.elements[i].name == 'aMaterno'
                                 || formulario.elements[i].name == 'fechaNacimiento'){
                                 if(<%=request.isUserInRole("SOPORTE_OPERATIVO")%> && !deshabilitarControl){
                                    formulario.elements[i].disabled = false;
                                 }else{
                                    formulario.elements[i].disabled = true;
                                 }
                            }
                    } 
                return(true);
            } 
       deshabilitarElementos(true);
    </script>                            
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>

function esTelefonoValido(str) {
    if (esEntero(str)) {
        if (str.length == 10)
            return true;
    }
    return false;
}

function ayudaRFC() {
    params = "?command=ayudaRFC";
    url = "/CEC/controller";
    //url = window.document.URL;
    abreVentana(url + params, 'scrollbars=yes', 550, 250, true, 0, 0);
}

function ayudaCodigoPostal() {
    params = "?command=buscaCodigoPostal";
    url = "/CEC/controller";
    //url = window.document.URL;
    abreVentana(url + params, 'scrollbars=yes', 550, 210, true, 0, 0);
}

function ayudaLocalidad() {
    params = "?command=buscaLocalidad";
    url = "/CEC/controller";
    //url = window.document.URL;
    abreVentana(url + params, 'scrollbars=yes', 550, 210, true, 0, 0);
}

function ayudaCargaArchivo() {
    params = "?command=cargaArchivo&tipoArchivo=" + window.document.forma.tipoArchivo.value
            + "&idCiclo=" + window.document.forma.idCiclo.value
            + "&conSeguro=" + window.document.forma.conSeguro.value;
    //url = "/CEC/controller";
    url = window.document.URL;
    abreVentana(url + params, 'scrollbars=yes', 700, 250, true, 0, 0);
}

function ayudaCargaArchivoAdicional() {
    params = "?command=cargaArchivo&tipoArchivo=" + window.document.forma.tipoArchivo.value
            + "&idCiclo=" + window.document.forma.idCiclo.value
            + "&conSeguro=" + window.document.forma.conSeguro.value
            + "&semanaAdicional=" + window.document.forma.semanaAdicional.value;
    //url = "/CEC/controller";
    url = window.document.URL;
    abreVentana(url + params, 'scrollbars=yes', 700, 250, true, 0, 0);
}

function formateaADosDecimales(numero) {
    numero = parseFloat(numero);
    numero = numero * 100;
    numero = Math.round(numero);
    numero = parseInt(numero);
    numero = numero / 100;
    return numero;
}

function esMesesAnios(str) {

    var reg = new RegExp("^[0-9]{2}(\/)[0-9]{4}$", "m");
    if (!reg.test(str)) {
        return false;
    }
    else {
        var meses = (str.substring(0, 2));
        var anio = (str.substring(3, 7));
        if (meses >= 13 || meses <= 0) {
            return false;
        }
        else if (anio < 1900) {
            return false;
        }
    }
    return true;
}


function esClaveElector(str) {
    var reg = new RegExp("^[A-Z]{6}[0-9]{8}[H,M]{1}[0-9]{3}?$", "m");
    return reg.test(str);
}

function esHomoclaveValida(rfc) {

    var homoclave = rfc.substring(10, 13);
    var c1 = homoclave.charAt(0);
    var c2 = homoclave.charAt(1);
    var c3 = homoclave.charAt(2);
    if (rfc.length == 10)
        return true;
    if (c1 == c2 && c1 == c2 && c2 == c3)
        return false;
    else if (c1 == '0' && c2 == '0')
        return false
    return true;
}

function esRFCPersonaFisica(rfc) {
    var reg = new RegExp("^[A-Z,a-z]{4}[0-9]{6}([A-Z,a-z,0-9]{3})?$", "m");
    var res = reg.test(rfc);

    var fecha = new Date();
    var actualyear = fecha.getYear();
    var anioInferior = (actualyear - 71) - 1900;
    var anioSuperior = (actualyear - 18) - 1900;

    if (res) {
        var anio = rfc.substring(4, 6);
        anio = parseInt(anio);
        if (anio < anioInferior || anio > anioSuperior)
            res = false;
        else {
            var mes = rfc.substring(6, 8);
            if (mes < 1 || mes > 12)
                res = false;
            var dia = rfc.substring(8, 10);
            if (dia < 1 || dia > 31)
                res = false;
        }
    }

    return res;
}


function esRFCPersonaFisica(rfc, producto) {
    var reg = new RegExp("^[A-Z,a-z]{4}[0-9]{6}([A-Z,a-z,0-9]{3})?$", "m");
    var res = reg.test(rfc);

    var fecha = new Date();
    var actualyear = fecha.getFullYear();
    var anioInferior = actualyear - 71 - 1900;
    var anioSuperior = actualyear - 18 - 1900;

    if (producto == 3)
        anioInferior = actualyear - 75 - 1900;

    if (res) {
        var anio = rfc.substring(4, 6);
        anio = parseInt(anio);
        if (anio < anioInferior || anio > anioSuperior)
            res = false;
        else {
            var mes = rfc.substring(6, 8);
            if (mes < 1 || mes > 12)
                res = false;
            var dia = rfc.substring(8, 10);
            if (dia < 1 || dia > 31)
                res = false;
        }
    }

    return res;
}

function esMontoValido(monto) {
    return esMontoValido(monto, 2);
}

function esMontoValido(monto, producto) {

    if (monto != '' && esFormatoMoneda(monto) && !esNegativo(monto)) {
        switch (producto) {
            case 1:
                if (monto < 1000 || monto > 200000)
                    return false;
                break;
            case 2:
                if (monto < 2000 || monto > 180000)
                    return false;
                break;
                //case 3: if(monto<1000 || monto>30000)//Monto original
            case 3:
                if (monto < 4000 || monto > 45000)
                    return false;
                break;
            case 5:
                if (monto < 1000 || monto > 25000)
                    return false;
                break;
            case 6:
                if (monto < 2000 || monto > 5000)
                    return false;
                break;
            case 21:
                if (monto < 1000 || monto > 200000)
                    return false;
                break;
        }
        return true;
    } else {
        return false;
    }
}


function esPlazoValido(plazo) {

    return esPlazoValido(plazo, 2);

}


function esPlazoValido(plazo, producto) {

    if (plazo != '' && esEntero(plazo)) {
        switch (producto) {
            case 1:
                if (plazo < 2 || plazo > 36)
                    return false;
                break;
            case 2:
                if (plazo < 2 || plazo > 36)
                    return false;
                break;
            case 3:
                if (plazo < 12 || plazo > 16)
                    return false;
                break;
            case 5:
                if (plazo == 4 || plazo == 8 || plazo == 12 || plazo == 16)
                    return false;
                break;
            case 6:
                if (plazo < 2 || plazo > 2)
                    return false;
                break;
            case 21:
                if (plazo < 2 || plazo > 36)
                    return false;
                break;
        }
        return true;
    } else {
        return false;
    }
}


function abreVentana(URL_Ventana, OpcionesVentana, Ancho, Alto, Centrada, PosX, PosY) {
    if (Centrada) {
        PosX = (screen.availWidth - Ancho) / 2;
        PosY = (screen.availHeight - Alto) / 2;
    }
    if (OpcionesVentana == '')
        OpcionesVentana = 'width=' + Ancho;
    else
        OpcionesVentana += ',width=' + Ancho;
    OpcionesVentana += ',height=' + Alto + ',left=' + PosX + ',top=' + PosY;
    window.open(URL_Ventana, "", OpcionesVentana);
}


function esFormatoPorcentaje(str) {
    var reg = new RegExp("^[0-9]{1,3}$", "m");
    return reg.test(str);
}


function esEntero(str) {
    var reg = new RegExp("^[0-9]+$", "m");
    return reg.test(str);
}


function esFormatoMoneda(str) {
    var reg = new RegExp("^(-)?[0-9]+(\.[0-9]{1,2})?$", "m");
    return reg.test(str);
}


function esNegativo(str) {
    var reg = new RegExp("^-", "m");
    return reg.test(str);
}


function esFechaValida(control, borrarContenido) {

    var valor;
    var dia;
    var mes;
    var anio;
    valor = control.value;
    if (valor == "")
        return (true);
    var diasMes = new Array(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    var patronFecha = /^(\d{2})(\/)(\d{2})(\/)(\d{4})?$/;
    var arregloMatch = valor.match(patronFecha);
    var anioActual = new Date().getYear();

    if (navigator.appName == "Netscape") {
        anioActual = anioActual + 1900;
    }
    if (arregloMatch == null) {
        //alert ("La fecha est� en un formato inv�lido.\n\nUse <dd/mm/aaaa>");
        if (borrarContenido == true) {
            control.value = "";
        }
        control.focus();
        return (false);
    }
    dia = arregloMatch [1];
    mes = arregloMatch [3];
    anio = arregloMatch [5];

    if (anio < 1800) {
        //alert ( "El a�o debe ser mayor o igual a 1800." );
        if (borrarContenido == true) {
            control.value = "";
        }
        control.focus();
        return (false);
    }

    if (mes < 1 || mes > 12) {
        //alert ("El mes debe estar entre el rango <1 y 12>");
        if (borrarContenido == true) {
            control.value = "";
        }
        control.focus();
        return (false);
    }
    if ((anio % 4 == 0 && anio % 100 != 0) || (anio % 4 == 0 && anio % 400 == 0)) {
        diasMes [1] = 29;
    } else {
        diasMes [1] = 28;
    }
    if (dia < 1 || dia > diasMes [mes - 1]) {
        //alert ("El d�a debe estar entre el rango <1 y " + diasMes [mes - 1] + ">");
        if (borrarContenido == true) {
            control.value = "";
        }
        control.focus();
        return (false);
    }
    return (true);
}

function esTexto(allow, str) {
    var imputStr = trim(str);
    if (imputStr == "")
        return false;
    var reg = new RegExp(allow, "m");
    return reg.test(str);
}

/*
 function esTexto(imputSt){
 var allow = "ABCDEFGHIJKLMNOPQRSTUVWXYZ�������. abcdefghijklmnopqrstuvwxyz�������";
 var imputStr = trim(imputSt);
 if(imputStr == "" )
 return false;
 for (var i = 0; i < imputStr.length; i++){
 var onechar = imputStr.charAt(i)
 if (allow.indexOf(onechar)==-1){
 return false;
 }
 }
 return true;
 }
 */

function formatofechavalido(unafecha) {

    if (unafecha.length === 10) {
        if (esEntero(unafecha.substring(0, 2))) {
            if (unafecha.substring(2, 3) === "/") {
                if (esEntero(unafecha.substring(3, 5))) {
                    if (unafecha.substring(5, 6) === "/") {
                        if (esEntero(unafecha.substring(6, 10))) {
                            return true;
                        } else
                            return false;
                    } else
                        return false;
                } else
                    return false;
            } else
                return false;
        } else
            return false;
    } else
        return false;
}
function validafecha(unafecha) {
    var dia = unafecha.substring(0, 2);
    var mes = unafecha.substring(3, 5);
    var anio = unafecha.substring(6, 10);
    //var fecha = new Date(año,mes,'0');
    if (dia > 31 || mes > 12 || anio < 1899) {
        return false;
    }
    else {
        if (dia == 31 && (mes == 02 || mes == 04 || mes == 06 || mes == 09 || mes == 11)) {
            return false;
        }
        else {
            if (dia == 29 && mes == 02 && (!((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))))) {
                return false;
            }
            else {
                return true;
            }
        }

    }
}

function ltrim(str) {
    for (var k = 0; k < str.length && isWhitespace(str.charAt(k)); k++)
        ;
    return str.substring(k, str.length);
}
function rtrim(str) {
    for (var j = str.length - 1; j >= 0 && isWhitespace(str.charAt(j)); j--)
        ;
    return str.substring(0, j + 1);
}
function trim(str) {
    return ltrim(rtrim(str));
}
function isWhitespace(charToCheck) {
    var whitespaceChars = " \t\n\r\f";
    return (whitespaceChars.indexOf(charToCheck) != -1);
}

function esEmail(str) {

    var filter = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    if (str == '' || filter.test(str)) {
        return true;
    }
    else
        return false;
}


//Regresa verdadero si fecha1 es mayor a fecha2 y falso en cualquier otro caso
function esPosterior(fecha1, fecha2) {
    var StartDT = new Date();
    var EndDT = new Date();
    if (fecha2 == 'Hoy') { //Valida si la fecha1 es mayor al dia de hoy si fecha2 contiene la cadena 'Hoy'
        StartDT = new Date();
        EndDT.setFullYear(fecha1.substring(6, 10), fecha1.substring(3, 5) - 1, fecha1.substring(0, 2));
    } else {
        //StartDT = Date.parse(fecha2);
        StartDT.setFullYear(fecha2.substring(6, 10), fecha2.substring(3, 5) - 1, fecha2.substring(0, 2));
        //EndDT = Date.parse(fecha1);
        EndDT.setFullYear(fecha1.substring(6, 10), fecha1.substring(3, 5) - 1, fecha1.substring(0, 2));
    }
    //EndDT.setFullYear(fecha1.substring(6, 10), fecha1.substring(3, 5) - 1, fecha1.substring(0, 2));
    
    if (StartDT >= EndDT) {
        return false;
    }
    return true;
}


function esEdadValida(fNacimiento) {

    var fechaLimiteInferior = new Date();
    var fechaLimiteSuperior = new Date();
    var fechaNacimiento = new Date();
    var hoy = new Date();
    fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 84);
    fechaLimiteSuperior.setYear(fechaLimiteSuperior.getFullYear() - 18);

    fechaNacimiento.setFullYear(fNacimiento.substring(6, 10), fNacimiento.substring(3, 5) - 1, fNacimiento.substring(0, 2));

    if (fechaNacimiento > fechaLimiteSuperior || fechaNacimiento < fechaLimiteInferior)
        return false;
    else
        return true;

}


function esEdadValida(fNacimiento, producto) {

    var fechaLimiteInferior = new Date();
    var fechaLimiteSuperior = new Date();
    var fechaNacimiento = new Date();
    var hoy = new Date();
    if (producto == 1)
        fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 82);
    else if (producto == 3 || producto == 5)
        fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 84);
    else
        fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 71);
    fechaLimiteSuperior.setYear(fechaLimiteSuperior.getFullYear() - 18);

    fechaNacimiento.setFullYear(fNacimiento.substring(6, 10), fNacimiento.substring(3, 5) - 1, fNacimiento.substring(0, 2));

    if (fechaNacimiento > fechaLimiteSuperior || fechaNacimiento < fechaLimiteInferior)
        return false;
    else
        return true;

}

function esEdadValidaSeguro(fNacimiento, tipo) {

    var fechaLimiteInferior = new Date();
    var fechaLimiteSuperior = new Date();
    var fechaNacimiento = new Date();
    var hoy = new Date();

    fechaNacimiento.setFullYear(fNacimiento.substring(6, 10), fNacimiento.substring(3, 5) - 1, fNacimiento.substring(0, 2));

    if (tipo == 'beneficiario') {
        fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 100);
        fechaLimiteSuperior.setYear(fechaLimiteSuperior.getFullYear() - 18);
        if (fechaNacimiento > fechaLimiteSuperior || fechaNacimiento < fechaLimiteInferior)
            return false;
        else
            return true;
    } else if (tipo == 'asegConyuge') {
        fechaLimiteInferior.setYear(fechaLimiteInferior.getFullYear() - 71);
        if (fechaNacimiento > fechaLimiteInferior)
            return false;
        else
            return true;
    } else if (tipo == 'asegHijo') {
        fechaLimiteSuperior.setYear(fechaLimiteSuperior.getFullYear() - 21);
        if (fechaNacimiento > fechaLimiteSuperior)
            return false;
        else
            return true;
    }
    /*if ( fechaNacimiento>fechaLimiteSuperior || fechaNacimiento<fechaLimiteInferior )
     return false;
     else
     return true;*/

}


//La variable rango debe contener el n�mero de dias de diferencia entre las fechas
function esRangoFechaValido(fechaInicio, fechaFin, rango) {

    var strDia = fechaInicio.substring(0, 2);
    var strMes = fechaInicio.substring(3, 5);
    var strAnno = fechaInicio.substring(6);

    var dia = parseInt(strDia);
    var mes = parseInt(strMes.substring(1));
    var anno = parseInt(strAnno);

    var fechaInicial = new Date(anno, mes - 1, dia);

    strDia = fechaFin.substring(0, 2);
    strMes = fechaFin.substring(3, 5);
    strAnno = fechaFin.substring(6);

    dia = parseInt(strDia);
    mes = parseInt(strMes.substring(1));
    anno = parseInt(strAnno);

    var fechaFinal = new Date(anno, mes - 1, dia);

    //var fechaHoy = new Date();

    //var difer = fechaHoy - fechaInicial;

    //difer = difer / 86400000;
    //if(difer > rango){
    //	return false;
    //}

    //difer = fechaHoy - fechaFinal;
    //difer = difer / 86400000;
    //if(difer > rango){
    //	return false;
    //}

    //difer = fechaFinal - fechaInicial;
    //difer = difer / 86400000;
    //if(difer > rango){
    //	return false;
    //}
    return true;
}

function esRangoValido(fechaInicio, fechaFin, rango) {

    var strDia = fechaInicio.substring(0, 2);
    var strMes = fechaInicio.substring(3, 5);
    var strAnno = fechaInicio.substring(6);

    var dia = parseInt(strDia);
    var mes = parseInt(strMes);
    var anno = parseInt(strAnno);

    var fechaInicial = new Date(anno, mes - 1, dia);

    strDia = fechaFin.substring(0, 2);
    strMes = fechaFin.substring(3, 5);
    strAnno = fechaFin.substring(6);

    dia = parseInt(strDia);
    mes = parseInt(strMes);
    anno = parseInt(strAnno);

    var fechaFinal = new Date(anno, mes - 1, dia);

    //var difer = fechaHoy - fechaInicial;
    var difer = fechaFinal - fechaInicial;

    difer = difer / 86400000;
    if(difer > rango){
    	return false;
    }
    difer = fechaFinal - fechaInicial;
    difer = difer / 86400000;
    if(difer > rango){
    	return false;
    }
    return true;
}

function formatoMillones(numero, decimales, separador_decimal, separador_miles) {
    numero = parseFloat(numero);
    if (isNaN(numero)) {
        return "";
    }

    if (decimales !== undefined) {
        numero = numero.toFixed(decimales);
    }

    numero = numero.toString().replace(".", separador_decimal !== undefined ? separador_decimal : ",");

    if (separador_miles) {
        var miles = new RegExp("(-?[0-9]+)([0-9]{3})");
        while (miles.test(numero)) {
            numero = numero.replace(miles, "$1" + separador_miles + "$2");
        }
    }

    return numero;
}


function validaCURP(curp){
    if (curp.match(/^([a-z]{4})([0-9]{6})([a-z]{6})([0-9]{2})$/i)) {//AAAA######AAAAAA##
        return true;
    } else {
        return false;
    }
}

function validaFecha(fecha)
{
    //valida fecha en formato dd/mm/aaaa
	var dtCh= "/";
	var minYear=1900;
	var maxYear=2100;
	function isInteger(s){
		var i;
		for (i = 0; i < s.length; i++){
			var c = s.charAt(i);
			if (((c < "0") || (c > "9"))) return false;
		}
		return true;
	}
	function stripCharsInBag(s, bag){
		var i;
		var returnString = "";
		for (i = 0; i < s.length; i++){
			var c = s.charAt(i);
			if (bag.indexOf(c) == -1) returnString += c;
		}
		return returnString;
	}
	function daysInFebruary (year){
		return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
	}
	function DaysArray(n) {
		for (var i = 1; i <= n; i++) {
			this[i] = 31
			if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
			if (i==2) {this[i] = 29}
		}
		return this;
	}
	function isDate(dtStr){
		var daysInMonth = DaysArray(12)
		var pos1=dtStr.indexOf(dtCh)
		var pos2=dtStr.indexOf(dtCh,pos1+1)
		var strDay=dtStr.substring(0,pos1)
		var strMonth=dtStr.substring(pos1+1,pos2)
		var strYear=dtStr.substring(pos2+1)
		strYr=strYear
		if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
		if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
		for (var i = 1; i <= 3; i++) {
			if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
		}
		month=parseInt(strMonth)
		day=parseInt(strDay)
		year=parseInt(strYr)
		if (pos1==-1 || pos2==-1){
			return false
		}
		if (strMonth.length<1 || month<1 || month>12){
			return false
		}
		if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
			return false
		}
		if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
			return false
		}
		if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
			return false
		}
		return true
	}
	if(isDate(fecha)){
		return true;
	}
        else{
		return false;
	}
}

function numeroMayorCero(valor)
{
    valor.value;
    if(valor <= 0 )
        return false;
    else if(valor !== 0 )
    {
        if(!isNaN(parseInt(valor)))
        return true;
    }
}

function validarFormatoFecha(fecha)
{ 
    var sExpresion = /^\d{2}\/\d{2}\/\d{4}$/ ;
    return (fecha.value.match(sExpresion)) ;
}
//comprar fecha fin y fecha inicio
function compararFechaIniFin(fechaIni, fechaFin)
{
    var fecha1 = Date.parse(fechaIni); //01 de Octubre del 2013
    var fecha2 = Date.parse(fechaFin); //03 de Octubre del 2013
     
    if (fecha1 === fecha2){
        //alert("fechas iguales");
        return true;
    } else if (fecha1 > fecha2) {
        alert("El dato [Fecha Inicio] es mayor que [Fecha Fin] favor de validar e ingresarlo nuevamente");
        return true;
    } else{
        return false;
    }
    return true;
}
function validaFechFinMayorFechFin(fechaInicial,fechaFinal)
{
    valuesStart=fechaInicial.split("/");

    valuesEnd=fechaFinal.split("/");
    
    // Verificamos que la fecha no sea posterior a la actual

    var dateStart=new Date(valuesStart[2],(valuesStart[1]-1),valuesStart[0]);
    var dateEnd=new Date(valuesEnd[2],(valuesEnd[1]-1),valuesEnd[0]);

    if(dateStart>dateEnd)
    {
        alert("El dato [Fecha Inicio] es mayor que [Fecha Fin] favor de validar e ingresarlo nuevamente");
        return true;
    }
    else
        return false;

}

function validaFechInicialnMayorFechFin(fechaInicial,fechaFinal)
{
    valuesStart=fechaInicial.split("/");

    valuesEnd=fechaFinal.split("/");
    
    // Verificamos que la fecha no sea posterior a la actual

    var dateStart=new Date(valuesStart[2],(valuesStart[1]-1),valuesStart[0]);
    var dateEnd=new Date(valuesEnd[2],(valuesEnd[1]-1),valuesEnd[0]);

    if(dateStart>dateEnd)
    {
        return true;
    }
    else
        return false;

}

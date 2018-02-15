
function minimoIntegrantes(tipoOperacion, tipoCiclo, integrantes, esMesaControl) {
    var seleccionados = 0;
    var montos = true;
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "monto" + i;
        var cheked = "desembolso" + i;
        var elemento = document.getElementById(nombreDinamico);
        var seleccionadoOk = document.getElementById(cheked);
        var montoMinimo = parseFloat(elemento.value);
        if (seleccionadoOk.checked === true) {
            seleccionados = seleccionados + 1;
            if (montoMinimo < 1000 || elemento.value == 0 || elemento.value == 0.0 || elemento.value == 0.00 || elemento.value == null || elemento.value == '')
                montos = false;
        }
    }
    var estatus = document.getElementById("estatus");
    if (!montos && estatus.value == 1 && tipoOperacion == 3) {
        alert('El monto minimo para el producto es $1,000.00');
        return false;
    }
    if (tipoOperacion == 3 && tipoCiclo == 1 && seleccionados <= 7 && !esMesaControl) {
        alert("El grupo debe contar con un minimo de 8 integrantes");
        return false;
    } else if (tipoOperacion == 3 && tipoCiclo == 1 && seleccionados <= 7 && esMesaControl) {
        alert("El grupo debe contar con un minimo de 8 integrantes");
        return false;
    } else if (tipoOperacion == 3 && tipoCiclo == 2 && seleccionados <= 7) {
        alert("El grupo debe contar con un minimo de 8 integrantes");
        return false;
    } else if (tipoOperacion == 5 && seleccionados < 1) {
        alert("El grupo debe contar con minimo 1 integrante");
        return false;
    }
    return true;
}

function minimoIntegrantesVIP(integrantes) {
    var seleccionados = 0;
    var montos = true;
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "monto" + i;
        var cheked = "desembolso" + i;
        var elemento = document.getElementById(nombreDinamico);
        var seleccionadoOk = document.getElementById(cheked);
        var montoMinimo = parseFloat(elemento.value);
        if (seleccionadoOk.checked === true) {
            seleccionados = seleccionados + 1;
            if (montoMinimo < 1000 || elemento.value == 0 || elemento.value == 0.0 || elemento.value == 0.00 || elemento.value == null || elemento.value == '')
                montos = false;
        }
    }
    var estatus = document.getElementById("estatus");
    if (!montos && estatus.value == 1 && tipoOperacion == 3) {
        alert('El monto minimo para el producto es $1,000.00');
        return false;
    }
    if (seleccionados < 10) {
        alert("El equipo debe contar con un minimo de 10 integrantes");
        return false;
    }
    return true;
}

function validaRolAut(integrantes) {
    var presidente = false;
    var secretario = false;
    var tesorero = false;
    var cantidadpres=0;
    var cantidadsec=0;
    var cantidadtes=0;
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "rol" + i;
        var elemento = document.getElementById(nombreDinamico);       
            if (elemento.value == 3) {
                presidente = true;
                cantidadpres=cantidadpres+1;
            } else if (elemento.value == 2) {
                secretario = true;
                cantidadsec=cantidadsec+1;
            } else if (elemento.value == 1) {
                tesorero = true;
                cantidadtes=cantidadtes+1;
            }        
    }
    if(cantidadpres>1||cantidadsec>1||cantidadtes>1){
        alert('No se debe asignar el mismo rol a dos integrantes');
        return false;        
    }
    if (presidente == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de presidente');
        return false;
    }
    if (secretario == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de secretario');
        return false;
    }
    if (tesorero == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de tesorero');
        return false;
    }    
    return true;
}

function validaCheckProcentaje(integrantes) {
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "desembolso" + i;  
        var varchecked = document.getElementById(nombreDinamico).checked
        var varporcentaje = document.getElementById("porcentaje"+i).value
        if (document.getElementById(nombreDinamico).checked && document.getElementById("porcentaje"+i).value==0) {
            alert('Se debe elegir un porcentaje de cr\u00e9dito adicional para la clienta seleccionada');
            return false;
            break;            
        } 
        if (!document.getElementById(nombreDinamico).checked && document.getElementById("porcentaje"+i).value>0){
            alert('Se debe elegir al cliente al que se le dio un porcentaje para cr\u00e9dito adicional');
            return false;
            break;
        }
    }
    return true;
}

function validaCheckAdicional(integrantes) {
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "desembolso" + i;  
        var varchecked = document.getElementById(nombreDinamico).checked
        if (document.getElementById(nombreDinamico).checked) {
            return true;
            break;            
        } 
    }
    alert('Se debe selecionar al menos un cliente'); 
    return false;
}



function validaRol(integrantes) {
    var presidente = false;
    var secretario = false;
    var tesorero = false;
    var cantidadpres=0;
    var cantidadsec=0;
    var cantidadtes=0;
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "rol" + i;
        var elemento = document.getElementById(nombreDinamico);
        var cheked = "desembolso" + i;
        var seleccionadoOk = document.getElementById(cheked);
        if (seleccionadoOk.checked == true) {
            if (elemento.value == 3) {
                presidente = true;
                cantidadpres=cantidadpres+1;
            } else if (elemento.value == 2) {
                secretario = true;
                cantidadsec=cantidadsec+1;
            } else if (elemento.value == 1) {
                tesorero = true;
                cantidadtes=cantidadtes+1;
            }
        }
    }
    if(cantidadpres>1||cantidadsec>1||cantidadtes>1){
        alert('No se debe asignar el mismo rol a dos integrantes');
        return false;        
    }
    if (presidente == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de presidente');
        return false;
    }
    if (secretario == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de secretario');
        return false;
    }
    if (tesorero == false) {
        alert('Debe indicar el integrante que desempe\u00f1a el rol de tesorero');
        return false;
    }    
    return true;
}


function validaMontosMaximos(integrantes) {
    for (var j = 0; j < integrantes; j = j + 1) {
        var nombreDinamicoMonto = "monto" + j;
        var nombreDinamicoMM = "montoMaximo" + j;
        var nombreCliente = "nombre" + j;
        var cheked = "desembolso" + j;
        var elementoMonto = document.getElementById(nombreDinamicoMonto);
        var elementoMaximo = document.getElementById(nombreDinamicoMM);
        var nombre = document.getElementById(nombreCliente);
        var montoCliente = parseFloat(elementoMonto.value);
        var montoMaximoCliente = parseFloat(elementoMaximo.value);
        var seleccionadoOk = document.getElementById(cheked);
        if (seleccionadoOk.checked == true && montoCliente > montoMaximoCliente) {
            alert('El monto del cliente: ' + nombre.value + ' supera el maximo permitido');
            return false;
        }
    }
    return true;
}

function getCalificacionGrupal(integrantes) {

    var estatus = document.getElementById("estatus");
    if (estatus.value == 2) {
        return true;
    }

    var numBuenos = 0;
    var numRegulares = 0;
    var numMalos = 0;
    var numSinConsulta = 0;
    for (var i = 0; i < integrantes; i = i + 1) {
        var nombreDinamico = "desembolso" + i;
        var elemento = document.getElementById(nombreDinamico);
        if (elemento.checked == true) {
            var nombre = "calificacion" + i;
            var objeto = document.getElementById(nombre);
            var calificacion = objeto.value;

            if (calificacion == 1 || calificacion == 0) {
                numBuenos = numBuenos + 1;
            } else if (calificacion == 3) {
                numRegulares = numRegulares + 1;
            } else if (calificacion == 2) {
                numMalos = numMalos + 1;
            } else if (calificacion == 4) {
                numBuenos = numBuenos + 1;
            } else if (calificacion == 5) {
                numSinConsulta = numSinConsulta + 1;
            }
        }
    }

    return getCalificacion(numBuenos, numRegulares, numMalos, numSinConsulta);

}

function getCalificacion(numBuenos, numRegulares, numMalos, numSinConsulta) {
    var totales = numBuenos + numRegulares + numMalos;
    var resultado = false;

    var porcentajeBuenos = Math.round((numBuenos * 100) / totales);
    var porcentajeRegulares = Math.round((numRegulares * 100) / totales);
    var porcentajeMalos = Math.round((numMalos * 100) / totales);

    if (numSinConsulta > 0)
        return false;
    if (porcentajeBuenos >= 70)
        resultado = true;
    else if ((porcentajeRegulares >= 10 && porcentajeRegulares <= 30) && porcentajeBuenos >= 70)
        resultado = true;
    else if (porcentajeMalos <= 20 && (porcentajeRegulares >= 10 && porcentajeRegulares <= 30))
        resultado = true;
    else if (porcentajeMalos <= 20 && porcentajeBuenos >= 70)
        resultado = true;

    return resultado;
}

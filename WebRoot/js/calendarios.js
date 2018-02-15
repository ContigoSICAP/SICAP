$(document).ready(function() {

$( "#fechaUltimaModificacion" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#solicitudFechaFirma" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
	
$( "#fechaNacimiento" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			yearRange: "-70:+0",
			changeMonth: true,
			changeYear: true
	});

$( "#fechaRealizacion" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#fechaFormacion" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#fechaInicial" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
	
$( "#fechaFinal" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#fechaInicio" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
	
$( "#fechaFin" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#fechaUltimaModificacion" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#fechaNacimiento" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			yearRange: "-70:+0",
			changeMonth: true,
			changeYear: true
	});
 
$( "#Fecha" ).datepicker({ 
		
			dateFormat: 'yy-mm-dd',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha1" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha2" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha3" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha4" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha5" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha6" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha7" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha8" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha9" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
        
$( "#Fecha10" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});

$( "#FechaFin" ).datepicker({

			dateFormat: 'yy-mm-dd',
			changeMonth: true,
			changeYear: true
	});
        
$( "#fechaDispersion" ).datepicker({ 
                        dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true,
                        minDate: -0,
                        beforeShowDay: function(date){
                            var bloquearDias = days(date);
                            return bloquearDias;
                        }
                        
	});

$( "#fechaDispersionAut" ).datepicker({ 
                        dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true,
                        beforeShowDay: function(date){
                            return [habilitada(date), "" ];
                        }                        
	});

$( "#fechaDesembolso" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true,
			minDate: -0
	});
        
$( "#fechaDesembolsoProg" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true,
			minDate: -0,
                        beforeShowDay: function(date){
                            var bloquearDias = days(date);
                            return bloquearDias;
                        }
	});

$( "#fechaFirma" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			changeYear: true
	});
$( "#fechaInicioNeg" ).datepicker({ 
		
			dateFormat: 'dd/mm/yy',
			yearRange: "-70:+0",
			changeMonth: true,
			changeYear: true
	});

// Manejo de dias feriados en la captura del catalogo de Representantes
if ($("#idEvitaFeriados").val() == '2'){
	$("#idManejoFeriados").attr('disabled','-1');
}
else {
    $("#idManejoFeriados").removeAttr('disabled')
}  

	var miFecha = new Date();
	var m = "" + (miFecha.getMonth()+1);
	var mm = "0" + m;
	mm = mm.substring(mm.length-2, mm.length);
	var d = "" + (miFecha.getDate());
	var dd = "0" + d;
	dd = dd.substring(dd.length-2, dd.length);
	var anio = miFecha.getFullYear();
	var lindaFecha =  dd + "/" + mm + "/" + anio;
	$("#fechaDesembolso").val(lindaFecha);
	
	$("#ocultaFechaDesembolso").css("display", "none");

 });

	
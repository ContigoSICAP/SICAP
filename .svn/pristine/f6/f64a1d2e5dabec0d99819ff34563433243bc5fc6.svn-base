$(document).ready(function(){  
      
        var tiempo_inicio_anim = 200;  
        var tiempo_entre_img = 3000;  
        var tiempo_fade = 1000;  
      
        function animacion_simple() {  
      
           // Mostramos la foto 1 
            $(".foto2").fadeOut(tiempo_fade); 
             $(".foto3").fadeOut(tiempo_fade); 
             $(".foto1").fadeIn(tiempo_fade); 
     
           // Cuando pasen otros 3000 milisegundos, ocultamos la foto 1 y mostramos la foto 2  
           setTimeout(function() {  
               // Ocultamos la foto 1  
               $(".foto1").fadeOut(tiempo_fade);  
               // Mostramos la foto 2  
               $(".foto2").fadeIn(tiempo_fade);  
           }, tiempo_entre_img);  
     
           // Cuando pasen otros 3000 milisegundos, ocultamos la foto 2 y mostramos la foto 3  
           setTimeout(function() {  
               // Ocultamos la foto 2  
               $(".foto2").fadeOut(tiempo_fade);  
               // Mostramos la foto 3  
               $(".foto3").fadeIn(tiempo_fade);  
           }, tiempo_entre_img*2);  
     
           // Cuando pasen otros 3000 milisegundos, ocultamos la foto 3 y volvemos a iniciar la animación  
           setTimeout(function() {  
               // Ocultamos la foto 3  
               $(".foto3").fadeOut(tiempo_fade);  
                $(".foto1").fadeIn(tiempo_fade);
               // Iniciamos otra vez la animación  
               animacion_simple();  
           }, tiempo_entre_img*3);  
     
       }  
     	
     	$(".foto1").hide(); 
     	$(".foto2").hide(); 
     	$(".foto3").hide(); 
       //Empezamos la animación a los 200 milisegundos  
       setTimeout(function() {  
           animacion_simple();  
       }, tiempo_inicio_anim);  
     
   });  

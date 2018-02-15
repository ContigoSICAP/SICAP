package com.sicap.clientes.util;

import java.util.Hashtable;
/**
 * 
 * @author Mguel Galindo
 * 
 */
public class RFCUtil {
//	Calcula el RFC de una persona f�sica su homoclave incluida.
/**
     * 
     * @param nombre
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param fechaNacimiento
     * @return 
     */
    public static String obtenRFC(String nombre, String apellidoPaterno, String apellidoMaterno, String fechaNacimiento) {
        //	Cambiamos todo a may�sculas
                nombre = nombre.toUpperCase();
                apellidoPaterno = apellidoPaterno.toUpperCase();
                apellidoMaterno = apellidoMaterno.toUpperCase();

                //	RFC que se regresa
                String rfc = "";
                boolean sinApellido = false;
                boolean largo = true;

                //	Quitamos los espacios al principio y final del nombre y apellidos
                nombre = nombre.trim();
                apellidoPaterno = apellidoPaterno.trim();
                apellidoMaterno = apellidoMaterno.trim();

                if(apellidoPaterno.equals("X") || apellidoMaterno.equals("X")){
                    sinApellido = true;
                    if(apellidoPaterno.equals("X"))
                        apellidoPaterno = apellidoMaterno;
                }
                /*String temp = apellidoMaterno.replace(" ", "");                
                if (temp.equals("NOPROPORCIONADO")) {
                    sinApellido = true;
                }*/
                
                
                //	Quitamos los art�culos de los apellidos, incluye los de nombres extranjeros
                String nom = quitaArticulos(nombre);
                String aPaterno = quitaArticulos(apellidoPaterno);
                String aMaterno = quitaArticulos(apellidoMaterno);

                int caso=0;
                boolean tieneVocal = tieneVocal(aPaterno);
                
                if(aPaterno.length()<=2)
                    largo = false;
                
                rfc+= RFCUtil.calculaApePat(aPaterno, tieneVocal, sinApellido);
                if(!sinApellido)
                    rfc += ""+aMaterno.charAt(0);
                rfc += RFCUtil.ignoraNombreComun(nom, sinApellido, largo);
                
                if(rfc.length() != 4)
                    rfc += "X";

                //	agregamos la fecha en formato yymmdd
                //System.out.println("la fecha es:"+fechaNacimiento);
                rfc += fechaNacimiento.substring(2, 4) + fechaNacimiento.substring(4, 6) + fechaNacimiento.substring(6, 8);

                //	Le agregamos la homoclave al rfc 
                //rfc=CalcularHomoclave(apellidoPaterno + " " + apellidoMaterno + " " + nombre, fechaNacimiento, rfc);
                //System.out.println("el rfc generado es:" + rfc);
                rfc = remuevePalabrasAltisonantes(rfc);
                //System.out.println("el rfc que regresa es:" + rfc);
                rfc = calculaHomoclave(apellidoPaterno+" "+apellidoMaterno+" "+nombre, fechaNacimiento, rfc);
                return rfc;
    }


    //	 Verifica si el caracter pasado es una vocal
    private static boolean esVocal(char letra) {

        if (letra == 'A' || letra == 'E' || letra == 'I' || letra == 'O' || letra == 'U') {
            return true;
        }
        return false;

    }
    
    private static boolean tieneVocal(String s)
    {
        if(s.length()==1)
            return false;
        for(int i=1;i<s.length();i++)
        {
            if(esVocal(s.charAt(i)))
                return true;
        }
            
        return false;
    }
    
    private static String primerVocal(String apellidoPaterno)
    {
        
        for(int i = 1; i < apellidoPaterno.length(); i++) 
        {
            if (esVocal(apellidoPaterno.charAt(i))) 
            {
                return ""+apellidoPaterno.charAt(i);
            }
        }
        return "";//nunca deveria ocurrir
    }

//	 Remplaza los art�culos com�nes en los apellidos en M�xico con caracter vac�o ("").
    private static String quitaArticulos(String palabra) {
        return palabra.replace(" DEL ", " ").replace("DEL ", "").replace(" LAS ", " ").replace(" DE ", " ").replace("DE ", "").replace(" LA ", " ").replace("LA ", "").replace(" Y ", " ").replace("Y ", "").replace(" A ", " ").replace(" MC ", " ").replace(" LOS ", " ").replace("LOS ", "").replace("VON ", "").replace("VAN ", "").replace(".", "");
    }

    private static String ignoraNombreComun(String nombre, boolean sinApellido, boolean largo) {

        String caracteres = "";
        String nombres[] = nombre.split("\\s");

        if (sinApellido || !largo) 
        {
            if (nombres.length > 1) 
            {
                for (int i = 0; i < nombres.length; i++) 
                {
                    if (!nombres[i].equals("JOSE") && !nombres[i].equals("JOSÉ") && !nombres[i].equals("MARIA") && !nombres[i].equals("MARÍA")&& !nombres[i].equals("M")) {
                        caracteres = nombres[i].substring(0, 2);
                        break;
                    }
                }
                if(caracteres.length()==0)
                        caracteres = nombres[0].substring(0, 2);
            } 
            else 
            {
                caracteres = nombre.substring(0, 2);
            }
            return caracteres;

        } 
        else 
        {
            return ignoraNombreComun(nombre);
        }

    }
    
    

//	Regresa la primera vocal del primer nombre ignorando los nombres comunes como Jos� o Mar�a
    private static String ignoraNombreComun(String nombre) {

        String caracteres = "";
        String nombres[] = nombre.split("\\s");
        if (nombres.length > 1) 
            {
                for (int i = 0; i < nombres.length; i++) 
                {
                    if (!nombres[i].equals("JOSE") && !nombres[i].equals("JOSÉ") && !nombres[i].equals("MARIA") && !nombres[i].equals("MARÍA")&& !nombres[i].equals("M")) {
                        caracteres = nombres[i].substring(0, 1);
                        break;
                    }
                }
                if(caracteres.length()==0)
                        caracteres = nombres[0].substring(0, 1);
            } 
            else 
            {
                caracteres = nombre.substring(0, 1);
            }
            return caracteres;
    }
    
  
    private static String calculaApePat(String apPat, boolean tieneVoc, boolean sinApellido)
    {
        if(apPat.length()>2)
        {
            if(tieneVoc)
                return apPat.charAt(0)+primerVocal(apPat);
            else
                return apPat.substring(0,2); 
        }
        else if(apPat.length()<=2 && !sinApellido)
        {
            return ""+apPat.charAt(0);
        }
        else if(apPat.length()<=2 && sinApellido)////////verificar esta regla
        {
            return ""+apPat.charAt(0);// y se le agrega una x al final 
        }
             
        else
            return "XXXX"; //simbolo de uqe no esta en ninguna regla verificar por que
            
            
    }

    //Reemplaza las palabras altisonantes de un RFC
    private static String remuevePalabrasAltisonantes(String RFC) {

      
        RFC = RFC.toUpperCase();

        RFC = RFC.replaceFirst("BUEI", "BUEX");
        RFC = RFC.replaceFirst("BUEY", "BUEX");
        RFC = RFC.replaceFirst("CACA", "CACX");
        RFC = RFC.replaceFirst("CACO", "CACX");
        RFC = RFC.replaceFirst("CAGA", "CAGX");
        RFC = RFC.replaceFirst("CAGO", "CAGX");
        RFC = RFC.replaceFirst("CAKA", "CAKX");
        RFC = RFC.replaceFirst("CAKO", "KAKX");
        RFC = RFC.replaceFirst("COGE", "COGX");
        RFC = RFC.replaceFirst("COJA", "COJX");
        RFC = RFC.replaceFirst("KOGE", "KOGX");
        RFC = RFC.replaceFirst("KOJO", "KOJX");
        RFC = RFC.replaceFirst("KAKA", "KAKX");
        RFC = RFC.replaceFirst("KULO", "KULX");
        RFC = RFC.replaceFirst("MAME", "MAMX");
        RFC = RFC.replaceFirst("LOCO", "LOCX");
        RFC = RFC.replaceFirst("LOCA", "LOCX");
        RFC = RFC.replaceFirst("MAMO", "MAMX");
        RFC = RFC.replaceFirst("MEAR", "MEAX");
        RFC = RFC.replaceFirst("MEAS", "MEAX");
        RFC = RFC.replaceFirst("MEON", "MEOX");
        RFC = RFC.replaceFirst("MION", "MIOX");
        RFC = RFC.replaceFirst("COJE", "COJX");
        RFC = RFC.replaceFirst("COJI", "COJX");
        RFC = RFC.replaceFirst("COJO", "COJX");
        RFC = RFC.replaceFirst("CULO", "CULX");
        RFC = RFC.replaceFirst("FETO", "FETX");
        RFC = RFC.replaceFirst("GUEY", "GUEX");
        RFC = RFC.replaceFirst("JOTO", "JOTX");
        RFC = RFC.replaceFirst("KACA", "KACX");
        RFC = RFC.replaceFirst("KACO", "KACX");
        RFC = RFC.replaceFirst("KAGA", "KAGX");
        RFC = RFC.replaceFirst("KAGO", "KAGX");
        RFC = RFC.replaceFirst("MOCO", "MOCX");
        RFC = RFC.replaceFirst("MULA", "MULX");
        RFC = RFC.replaceFirst("PEDA", "PEDX");
        RFC = RFC.replaceFirst("PEDO", "PEDX");
        RFC = RFC.replaceFirst("PENE", "PENX");
        RFC = RFC.replaceFirst("PUTA", "PUTX");
        RFC = RFC.replaceFirst("PUTO", "PUTX");
        RFC = RFC.replaceFirst("QULO", "QULX");
        RFC = RFC.replaceFirst("RATA", "RATX");
        RFC = RFC.replaceFirst("RUIN", "RUIX");

        return RFC;
       
    }
    
    
    //	 Calcula la homoclave
    public static String calculaHomoclave(String nombreCompleto, String fechaNacimiento, String rfc) {

//	Guardara el nombre en su correspondiente num�rico
        StringBuilder nombreEnNumero = new StringBuilder();;

//	La suma de la secuencia de n�meros de nombreEnNumero
        long valorSuma = 0;

//	Tablas para calcular la homoclave
        Hashtable<String, String> tablaRFC1 = new Hashtable<String, String>();
        tablaRFC1.put("&", "10");
        tablaRFC1.put("Ñ", "40");
        tablaRFC1.put("A", "11");
        tablaRFC1.put("B", "12");
        tablaRFC1.put("C", "13");
        tablaRFC1.put("D", "14");
        tablaRFC1.put("E", "15");
        tablaRFC1.put("F", "16");
        tablaRFC1.put("G", "17");
        tablaRFC1.put("H", "18");
        tablaRFC1.put("I", "19");
        tablaRFC1.put("J", "21");
        tablaRFC1.put("K", "22");
        tablaRFC1.put("L", "23");
        tablaRFC1.put("M", "24");
        tablaRFC1.put("N", "25");
        tablaRFC1.put("O", "26");
        tablaRFC1.put("P", "27");
        tablaRFC1.put("Q", "28");
        tablaRFC1.put("R", "29");
        tablaRFC1.put("S", "32");
        tablaRFC1.put("T", "33");
        tablaRFC1.put("U", "34");
        tablaRFC1.put("V", "35");
        tablaRFC1.put("W", "36");
        tablaRFC1.put("X", "37");
        tablaRFC1.put("Y", "38");
        tablaRFC1.put("Z", "39");
        tablaRFC1.put("0", "0");
        tablaRFC1.put("1", "1");
        tablaRFC1.put("2", "2");
        tablaRFC1.put("3", "3");
        tablaRFC1.put("4", "4");
        tablaRFC1.put("5", "5");
        tablaRFC1.put("6", "6");
        tablaRFC1.put("7", "7");
        tablaRFC1.put("8", "8");
        tablaRFC1.put("9", "9");

//	TablaRFC 2
        Hashtable<String, String> tablaRFC2 = new Hashtable<String, String>();
        tablaRFC2.put("0", "1");
        tablaRFC2.put("1", "2");
        tablaRFC2.put("2", "3");
        tablaRFC2.put("3", "4");
        tablaRFC2.put("4", "5");
        tablaRFC2.put("5", "6");
        tablaRFC2.put("6", "7");
        tablaRFC2.put("7", "8");
        tablaRFC2.put("8", "9");
        tablaRFC2.put("9", "A");
        tablaRFC2.put("10", "B");
        tablaRFC2.put("11", "C");
        tablaRFC2.put("12", "D");
        tablaRFC2.put("13", "E");
        tablaRFC2.put("14", "F");
        tablaRFC2.put("15", "G");
        tablaRFC2.put("16", "H");
        tablaRFC2.put("17", "I");
        tablaRFC2.put("18", "J");
        tablaRFC2.put("19", "K");
        tablaRFC2.put("20", "L");
        tablaRFC2.put("21", "M");
        tablaRFC2.put("22", "N");
        tablaRFC2.put("23", "P");
        tablaRFC2.put("24", "Q");
        tablaRFC2.put("25", "R");
        tablaRFC2.put("26", "S");
        tablaRFC2.put("27", "T");
        tablaRFC2.put("28", "U");
        tablaRFC2.put("29", "V");
        tablaRFC2.put("30", "W");
        tablaRFC2.put("31", "X");
        tablaRFC2.put("32", "Y");
        tablaRFC2.put("33", "Z");

//	TablaRFC 3
        Hashtable<String, String> tablaRFC3 = new Hashtable<String, String>();
        tablaRFC3.put("A", "10");
        tablaRFC3.put("B", "11");
        tablaRFC3.put("C", "12");
        tablaRFC3.put("D", "13");
        tablaRFC3.put("E", "14");
        tablaRFC3.put("F", "15");
        tablaRFC3.put("G", "16");
        tablaRFC3.put("H", "17");
        tablaRFC3.put("I", "18");
        tablaRFC3.put("J", "19");
        tablaRFC3.put("K", "20");
        tablaRFC3.put("L", "21");
        tablaRFC3.put("M", "22");
        tablaRFC3.put("N", "23");
        tablaRFC3.put("O", "25");
        tablaRFC3.put("P", "26");
        tablaRFC3.put("Q", "27");
        tablaRFC3.put("R", "28");
        tablaRFC3.put("S", "29");
        tablaRFC3.put("T", "30");
        tablaRFC3.put("U", "31");
        tablaRFC3.put("V", "32");
        tablaRFC3.put("W", "33");
        tablaRFC3.put("X", "34");
        tablaRFC3.put("Y", "35");
        tablaRFC3.put("Z", "36");
        tablaRFC3.put("0", "0");
        tablaRFC3.put("1", "1");
        tablaRFC3.put("2", "2");
        tablaRFC3.put("3", "3");
        tablaRFC3.put("4", "4");
        tablaRFC3.put("5", "5");
        tablaRFC3.put("6", "6");
        tablaRFC3.put("7", "7");
        tablaRFC3.put("8", "8");
        tablaRFC3.put("9", "9");
        tablaRFC3.put("", "24");
        tablaRFC3.put(" ", "37");

//	agregamos un cero al inicio de la representaci�n n�merica del nombre
        nombreEnNumero.append(0);

//	Recorremos el nombre y vamos convirtiendo las letras en 
//	su valor num�rico
        for (int i = 0; i < nombreCompleto.length(); i++) {
            if (tablaRFC1.containsKey(nombreCompleto.charAt(i) + "")) {
                nombreEnNumero.append(tablaRFC1.get(nombreCompleto.charAt(i) + ""));
            } else {
                nombreEnNumero.append("00");
            }
        }

//	Calculamos la suma de la secuencia de n�meros 
//	calculados anteriormente
//	la formula es:
//	( (el caracter actual multiplicado por diez)
//	mas el valor del caracter siguiente )
//	(y lo anterior multiplicado por el valor del caracter siguiente)
        for (int i = 0; i < nombreEnNumero.length() - 1; i++) {
            valorSuma += ((Integer.parseInt("" + nombreEnNumero.charAt(i)) * 10) + Integer.parseInt("" + nombreEnNumero.charAt(i + 1))) * Integer.parseInt("" + nombreEnNumero.charAt(i + 1));
        }

        int div = 0, mod = 0;
        div = (int) valorSuma % 1000;
        mod = div % 34;
        div = (div - mod) / 34;

        int indice = 0;
        String hc = ""; //los dos primeros caracteres de la homoclave
        while (indice <= 1) {
            if (tablaRFC2.containsKey("" + ((indice == 0) ? div : mod))) {
                hc += tablaRFC2.get("" + ((indice == 0) ? div : mod));
            } else {
                hc += "Z";
            }
            indice++;
        }

//	Agregamos al RFC los dos primeros caracteres de la homoclave
        rfc += hc;

//	Aqui empieza el calculo del digito verificador basado en lo que tenemos del RFC
        int rfcAnumeroSuma = 0, sumaParcial = 0;
        for (int i = 0; i < rfc.length(); i++) {
            if (tablaRFC3.containsKey(("" + rfc.charAt(i)))) {
                rfcAnumeroSuma = Integer.parseInt((String) tablaRFC3.get("" + rfc.charAt(i)));
                sumaParcial += (rfcAnumeroSuma * (14 - (i + 1)));
            }
        }

        int moduloVerificador = sumaParcial % 11;
        if (moduloVerificador == 0) {
            rfc += "0";
        } else {
            sumaParcial = 11 - moduloVerificador;
            if (sumaParcial == 10) { 
               rfc += "A";
            } else {
                rfc += sumaParcial;
            }
        }
        return rfc;
    }
    
}

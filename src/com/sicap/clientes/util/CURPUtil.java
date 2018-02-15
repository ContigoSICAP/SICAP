package com.sicap.clientes.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import com.sicap.clientes.util.RFCUtil;

public class CURPUtil {

    public static String generaCURP(String nombre, String aPaterno, String aMaterno, Date fechaNac, int entidadNac, int sexo) {

        String Mensaje = "";
        String entidadCurp = "";
        String FechaCurp = "";
        String sexoCurp = "";
        String mensaje = "";
        String curp = "";
        nombre = nombre.toUpperCase();
        aPaterno = aPaterno.toUpperCase();
        aMaterno = aMaterno.toUpperCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        boolean tieneVocal = tieneVocal(aPaterno);
        boolean sinApellido = tieneVocal(aPaterno);

        nombre = nombre.trim();
        aMaterno = aMaterno.trim();
        aPaterno = aPaterno.trim();

        nombre = nombre.replaceAll("Ñ", "X").replace("Ü", "U").replace("/", "X").replace("-", "X").replace(".", "X");
        aPaterno = aPaterno.replaceAll("Ñ", "X").replace("Ü", "U").replace("/", "X").replace("-", "X").replace(".", "X");
        aMaterno = aMaterno.replaceAll("Ñ", "X").replace("Ü", "U").replace("/", "X").replace("-", "X").replace(".", "X");

        nombre = quitaArticulos(nombre);
        aPaterno = quitaArticulos(aPaterno);
        aMaterno = quitaArticulos(aMaterno);

        nombre = ignoraNombreComun(nombre);
        aPaterno = ignoraApellidoCompuesto(aPaterno);
        aMaterno = ignoraApellidoCompuesto(aMaterno);

        if (aPaterno.equals("")) {
            aPaterno = new String(aMaterno);
            aMaterno = "X";
            sinApellido = true;
        }
        if (aMaterno.equals("")) {
            aMaterno = "X";
        }

        tieneVocal = tieneVocal(aPaterno);
        curp += calculaApePat(aPaterno, tieneVocal, sinApellido);
        curp += aMaterno.charAt(0);
        curp += nombre.charAt(0);
        curp = remuevePalabrasAltisonantes(curp);
        FechaCurp = sdf.format(fechaNac);
        curp += FechaCurp;
        if (sexo == 1) {
            sexoCurp = "M";
        } else if (sexo == 2) {
            sexoCurp = "H";
        }
        curp += sexoCurp;
        entidadCurp = validaEntidad(entidadNac);
        curp += entidadCurp;
        curp += getConsonateInterna(aPaterno);
        if (aMaterno.equals("X")) {
            curp += aMaterno;
        } else {
            curp += getConsonateInterna(aMaterno);
        }
        curp += getConsonateInterna(nombre);
        return curp;
    }

    private static char getConsonateInterna(String palabra) {
        char consonante = 'X';
        if (palabra.length() > 1) {
            for (int i = 1; i < palabra.length(); i++) {
                if (!esVocal(palabra.charAt(i))) {
                    consonante = palabra.charAt(i);
                    break;
                }
            }
        }
        return consonante;
    }

    private static String remuevePalabrasAltisonantes(String CURP) {

        CURP = CURP.toUpperCase();
        CURP = CURP.replaceFirst("BACA", "BXCA");
        CURP = CURP.replaceFirst("BAKA", "BXKA");
        CURP = CURP.replaceFirst("BUEI", "BXEI");
        CURP = CURP.replaceFirst("BUEY", "BXEY");
        CURP = CURP.replaceFirst("CACA", "CXCA");
        CURP = CURP.replaceFirst("CACO", "CXCO");
        CURP = CURP.replaceFirst("CAGA", "CXGA");
        CURP = CURP.replaceFirst("CAGO", "CXGO");
        CURP = CURP.replaceFirst("CAKA", "CXKA");
        CURP = CURP.replaceFirst("CAKO", "CXKO");
        CURP = CURP.replaceFirst("COGE", "CXGE");
        CURP = CURP.replaceFirst("COGI", "CXGI");
        CURP = CURP.replaceFirst("COJA", "CXJA");
        CURP = CURP.replaceFirst("COJE", "CXJE");
        CURP = CURP.replaceFirst("COJI", "CXJI");
        CURP = CURP.replaceFirst("COJO", "CXJO");
        CURP = CURP.replaceFirst("COLA", "CXLA");
        CURP = CURP.replaceFirst("CULO", "CXLO");
        CURP = CURP.replaceFirst("FALO", "FXLO");
        CURP = CURP.replaceFirst("FETO", "FXTO");
        CURP = CURP.replaceFirst("GETA", "GXTA");
        CURP = CURP.replaceFirst("GUEI", "GXEI");
        CURP = CURP.replaceFirst("GUEY", "GXEY");
        CURP = CURP.replaceFirst("JETA", "JXTA");
        CURP = CURP.replaceFirst("JOTO", "JXTO");
        CURP = CURP.replaceFirst("KACA", "KXCA");
        CURP = CURP.replaceFirst("KACO", "KXCO");
        CURP = CURP.replaceFirst("KAGA", "KXGA");
        CURP = CURP.replaceFirst("KAGO", "KXGO");
        CURP = CURP.replaceFirst("KAKA", "KXKA");
        CURP = CURP.replaceFirst("KAKO", "KXKO");
        CURP = CURP.replaceFirst("KOGE", "KXGE");
        CURP = CURP.replaceFirst("KOGI", "KXGI");
        CURP = CURP.replaceFirst("KOJA", "KXJA");
        CURP = CURP.replaceFirst("KOJE", "KXJE");
        CURP = CURP.replaceFirst("KOJI", "KXJI");
        CURP = CURP.replaceFirst("KOJO", "KXJO");
        CURP = CURP.replaceFirst("KOLA", "KXLA");
        CURP = CURP.replaceFirst("KULO", "KXLO");
        CURP = CURP.replaceFirst("LILO", "LXLO");
        CURP = CURP.replaceFirst("LOCA", "LXCA");
        CURP = CURP.replaceFirst("LOCO", "LXCO");
        CURP = CURP.replaceFirst("LOKA", "LXKA");
        CURP = CURP.replaceFirst("LOKO", "LXKO");
        CURP = CURP.replaceFirst("MAME", "MXME");
        CURP = CURP.replaceFirst("MAMO", "MXMO");
        CURP = CURP.replaceFirst("MEAR", "MXAR");
        CURP = CURP.replaceFirst("MEAS", "MXAS");
        CURP = CURP.replaceFirst("MEON", "MXON");
        CURP = CURP.replaceFirst("MIAR", "MXAR");
        CURP = CURP.replaceFirst("MION", "MXON");
        CURP = CURP.replaceFirst("MOCO", "MXCO");
        CURP = CURP.replaceFirst("MOKO", "MXKO");
        CURP = CURP.replaceFirst("MULA", "MXLA");
        CURP = CURP.replaceFirst("MULO", "MXLO");
        CURP = CURP.replaceFirst("NACA", "NXCA");
        CURP = CURP.replaceFirst("NACO", "NXCO");
        CURP = CURP.replaceFirst("PEDA", "PXDA");
        CURP = CURP.replaceFirst("PEDO", "PXDO");
        CURP = CURP.replaceFirst("PENE", "PXNE");
        CURP = CURP.replaceFirst("PIPI", "PXPI");
        CURP = CURP.replaceFirst("PITO", "PXTO");
        CURP = CURP.replaceFirst("POPO", "PXPO");
        CURP = CURP.replaceFirst("PUTA", "PXTA");
        CURP = CURP.replaceFirst("PUTO", "PXTO");
        CURP = CURP.replaceFirst("QULO", "QXLO");
        CURP = CURP.replaceFirst("RATA", "RXTA");
        CURP = CURP.replaceFirst("ROBA", "RXBA");
        CURP = CURP.replaceFirst("ROBE", "RXBE");
        CURP = CURP.replaceFirst("ROBO", "RXBO");
        CURP = CURP.replaceFirst("RUIN", "RXIN");
        CURP = CURP.replaceFirst("SENO", "SXNO");
        CURP = CURP.replaceFirst("TETA", "TXTA");
        CURP = CURP.replaceFirst("VACA", "VXCA");
        CURP = CURP.replaceFirst("VAGA", "VXGA");
        CURP = CURP.replaceFirst("VAGO", "VXGO");
        CURP = CURP.replaceFirst("VAKA", "VXKA");
        CURP = CURP.replaceFirst("VUEI", "VXEI");
        CURP = CURP.replaceFirst("VUEY", "VXEY");
        CURP = CURP.replaceFirst("WUEI", "WXEI");
        CURP = CURP.replaceFirst("WUEY", "WXEY");
        return CURP;
    }

    private static String validaEntidad(int Estado) {
        String EstadoCurp = "";
        switch (Estado) {
            case 1://Aguascalientes
                EstadoCurp = "AS";
                break;
            case 2://Baja California
                EstadoCurp = "BC";
                break;
            case 3://Baja California Su
                EstadoCurp = "BS";
                break;
            case 4://Campeche
                EstadoCurp = "CC";
                break;
            case 5://Coahuila
                EstadoCurp = "CL";
                break;
            case 6://Colima
                EstadoCurp = "CM";
                break;
            case 7://Chiapas
                EstadoCurp = "CS";
                break;
            case 8://Chihuahua
                EstadoCurp = "CH";
                break;
            case 9://Distrito FederaL
                EstadoCurp = "DF";
                break;
            case 10://Durango
                EstadoCurp = "DG";
                break;
            case 11://Guanajuato
                EstadoCurp = "GT";
                break;
            case 12://Guerrero
                EstadoCurp = "GR";
                break;
            case 13://Hidalgo
                EstadoCurp = "HG";
                break;
            case 14://Jalisco
                EstadoCurp = "JC";
                break;
            case 15://México
                EstadoCurp = "MC";
                break;
            case 16://Michoacán
                EstadoCurp = "MN";
                break;
            case 17://Morelos
                EstadoCurp = "MS";
                break;
            case 18://Nayarit
                EstadoCurp = "NT";
                break;
            case 19://Nuevo León
                EstadoCurp = "NL";
                break;
            case 20://Oaxaca
                EstadoCurp = "OC";
                break;
            case 21://Puebla
                EstadoCurp = "PL";
                break;
            case 22://Querétaro
                EstadoCurp = "QT";
                break;
            case 23://Quintana Roo
                EstadoCurp = "QR";
                break;
            case 24://San Luis Potosí
                EstadoCurp = "SP";
                break;
            case 25://Sinaloa
                EstadoCurp = "SL";
                break;
            case 26://Sonora
                EstadoCurp = "SR";
                break;
            case 27://Tabasco
                EstadoCurp = "TC";
                break;
            case 28://Tamaulipas
                EstadoCurp = "TS";
                break;
            case 29://Tlaxcala
                EstadoCurp = "TL";
                break;
            case 30://Veracruz
                EstadoCurp = "VZ";
                break;
            case 31://Yucatán
                EstadoCurp = "YN";
                break;
            case 32://Zacatecas
                EstadoCurp = "ZS";
                break;
        }
        return EstadoCurp;
    }

    private static String ignoraNombreComun(String nombre) {
        String caracteres = "";
        String nombres[] = nombre.split("\\s");
        if (nombres.length > 1) {
            for (int i = 0; i < nombres.length; i++) {
                if (!nombres[i].equals("JOSE") && !nombres[i].equals("J") && !nombres[i].equals("J.") && !nombres[i].equals("JOSÉ") && !nombres[i].equals("MARIA") && !nombres[i].equals("MARÍA") && !nombres[i].equals("MA") && !nombres[i].equals("MA.") && !nombres[i].equals("M") && !nombres[i].equals("M.")) {
                    caracteres = nombres[i];
                    break;
                }
            }
            if (caracteres.equals("")) {
                if (nombres[0].equals("JOSE") || nombres[0].equals("J") || nombres[0].equals("J.") || nombres[0].equals("JOSÉ") || nombres[0].equals("MARIA") || nombres[0].equals("MARÍA") || nombres[0].equals("MA") || nombres[0].equals("MA.") || nombres[0].equals("M") || nombres[0].equals("M.")) 
                    caracteres = nombres[1];
                else 
                    caracteres = nombres[0];                
            }
        } else {
            caracteres = nombre;
        }
        return caracteres;
    }

    private static String ignoraApellidoCompuesto(String nombre) {
        String caracteres = "";
        String nombres[] = nombre.split("\\s");
        if (nombres.length > 1) {
            caracteres = nombres[0];
        } else {
            caracteres = nombre;
        }
        return caracteres;
    }

    private static String quitaArticulos(String palabra) {
        String nombre = "";
        String nombres[] = palabra.split("\\s");
        for (int i = 0; i < nombres.length; i++) {
            if (!nombres[i].trim().equals("DEL") && !nombres[i].trim().equals("LAS") && !nombres[i].trim().equals("DE") && !nombres[i].trim().equals("LA") && !nombres[i].trim().equals("Y")
                    && !nombres[i].trim().equals("A") && !nombres[i].trim().equals("MC") && !nombres[i].trim().equals("LOS") && !nombres[i].trim().equals("VON") && !nombres[i].trim().equals("VAN")
                    && !nombres[i].trim().equals(".") && !nombres[i].trim().equals("EL") && !nombres[i].trim().equals("DA") && !nombres[i].trim().equals("DAS") && !nombres[i].trim().equals("DER")
                    && !nombres[i].trim().equals("DI") && !nombres[i].trim().equals("DIE") && !nombres[i].trim().equals("DD") && !nombres[i].trim().equals("LE") && !nombres[i].trim().equals("LES")
                    && !nombres[i].equals("MAC")) {
                nombre += nombres[i] + " ";
            }
        }
        return nombre.substring(0, nombre.length() - 1);
    }

    private static boolean tieneVocal(String s) {
        if (s.length() == 1) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (esVocal(s.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    private static boolean esVocal(char letra) {

        if (letra == 'A' || letra == 'E' || letra == 'I' || letra == 'O' || letra == 'U') {
            return true;
        }
        return false;

    }

    private static String calculaApePat(String apPat, boolean tieneVoc, boolean sinApellido) {
        if (apPat.length() > 2) {
            if (tieneVoc) {
                return apPat.charAt(0) + primerVocal(apPat);
            } else {
                return apPat.charAt(0) + "X";
            }
        } else if (apPat.length() == 2) {
            if (tieneVoc) {
                return apPat.substring(0, 2);
            } else {
                return "" + apPat.charAt(0) + "X";
            }
        } else {
            return "" + apPat.charAt(0) + "X"; //simbolo de uqe no esta en ninguna regla verificar por que
        }

    }

    private static String primerVocal(String apellidoPaterno) {

        for (int i = 1; i < apellidoPaterno.length(); i++) {
            if (esVocal(apellidoPaterno.charAt(i))) {
                return "" + apellidoPaterno.charAt(i);
            }
        }
        return "";//nunca deveria ocurrir
    }

}

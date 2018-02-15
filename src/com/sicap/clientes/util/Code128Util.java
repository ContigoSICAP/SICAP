/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

/**
 *
 * @author Alex
 */
public class Code128Util {

    private static final byte STARTA = 103;
    private static final byte STARTB = 104;
    private static final byte STARTC = 105;
    private static final byte CODEA = 101;
    private static final byte CODEB = 100;
    private static final byte CODEC = 99;
    private static final byte SHIFT = 98;
    private static final byte STOP = 106;
    private static final byte FNC1 = 102;

    /**
     * Dado un caracter en code 128 tipo B retorna su equivalente ascii
     */
    private static char asciiB(char s) {
        if (s <= 95) // " !"# ....ABC..XYZ..^_..abcd...xyz..}~DEL"
        {
            return (char) (s + 32);
        }
        // fnc, shift y code propios del codigo de barras
        return (char) (s + 100);
    }

    /**
     * Dado un caracter en ascci retorna su equivalente code 128
     */
    private static char code128(String ascci) {
        if (ascci == null) {
            return 0;
        }

        int len = ascci.length();
        if (len == 0) {
            return 0;
        }

        if (ascci.length() == 1) {
            char c = ascci.charAt(0);
            if (c <= 31) {
                return (char) (c + 64); // NULL SOH STX .. RS UC
            }
            if (c <= 127) {
                return (char) (c - 32); // " !"# ....ABC..XYZ..^_..abcd...xyz..}~DEL"
            }
            return (char) (c - 100); // fnc, shift y code propios del codigo de barras
        }

        int numero = Integer.parseInt(ascci.substring(0, 2));
        return (char) numero;
    }

    /**
     * ¿el caracter ascii se puede representar con el conjunto de caracteres A?
     */
    private static boolean esSetA(char c) {
        if ((0 <= c) && (c <= 'Z')) {
            return true;
        }
        if (c == asciiB((char) FNC1)) {
            return true;
        }
        return false;
    }

    /**
     * ¿el caracter ascii se puede representar con el conjunto de caracteres B?
     */
    private static boolean esSetB(char c) {
        if ((' ' <= c) && (c <= 'z')) {
            return true;
        }
        if (c == asciiB((char) FNC1)) {
            return true;
        }
        return false;
    }

    /**
     * ¿el caracter ascii se puede representar con el conjunto de caracteres C?
     */
    private static boolean esSetC(char c) {
        if (('0' <= c) && (c <= '9')) {
            return true;
        }

        if (c == asciiB((char) FNC1)) {
            return true;
        }
        return false;
    }

    /**
     * ¿Dada una cadena cuantos caracteres se pueden representar con el conjunto de caracteres A?
     */
    private static int endSetA(String data) {
        int idx = 0;
        for (int i = 0; i < data.length(); i++) {
            if (!esSetA(data.charAt(i))) {
                break;
            }
            idx++;
        }
        return idx;
    }

    /**
     * ¿Dada una cadena cuantos caracteres se pueden representar con el conjunto de caracteres B?
     */
    private static int endSetB(String data) {
        int idx = 0;
        for (int i = 0; i < data.length(); i++) {
            if (!esSetB(data.charAt(i))) {
                break;
            }
            idx++;
        }
        return idx;
    }

    /**
     * Note que aparte de los digitos el function code es considerado un número
     */
    private static boolean sonNumeros(String datos) {
        if (datos.matches("[0-9," + asciiB((char) FNC1) + "]+[0-9," + asciiB((char) FNC1) + "]")) {
            return true;
        }
        return false;
    }

    /**
     * Esta función es la que decide si se necesita cambiar el conjunto de caracteres
     */
    private static byte setCode(String datos, byte codeActual) {
        int revisa = datos.length();
        if (revisa >= 4) {
            revisa = 4;
        }

        switch (codeActual) {
            case CODEA:
                if (sonNumeros(datos.substring(0, revisa))) {
                    return CODEC;
                }
                if (esSetA(datos.charAt(0))) {
                    return CODEA;
                }
                if (revisa > 1 && esSetA(datos.charAt(1))) {
                    if (setCode(datos.substring(1, revisa), (byte) 0) == CODEB) {
                        return CODEB;
                    }
                    return SHIFT;
                }
                return CODEB;
            case CODEB:
                if (sonNumeros(datos.substring(0, revisa))) {
                    return CODEC;
                }
                if (esSetB(datos.charAt(0))) {
                    return CODEB;
                }
                if (revisa > 1 && esSetB(datos.charAt(1))) {
                    if (setCode(datos.substring(1, revisa), (byte) 0) == CODEA) {
                        return CODEA;
                    }
                    return SHIFT;
                }
                return CODEA;
            case CODEC:
                if (revisa < 2 || !esSetC(datos.charAt(0)) || !esSetC(datos.charAt(1))) {
                    return setCode(datos, (byte) 0);
                }
                return CODEC;
        }

        if (revisa > 2 && sonNumeros(datos.substring(0, revisa))) {
            return CODEC;
        }

        int idxA = endSetA(datos);
        int idxB = endSetB(datos);
        if (idxA > idxB) {
            return CODEA;
        }
        return CODEB;
    }

    /**
     * Necesario, el caracter de inicio no es el mismo que el devuelto por 'setCode'
     */
    private static byte startCode(byte setcode) {
        switch (setcode) {
            case CODEA:
                return STARTA;
            case CODEB:
                return STARTB;
            case CODEC:
                return STARTC;
        }
        return STARTB;
    }

    /**
     * Aqui la cadena de texto se convierte al formato code 128, en el proceso se puede acortar o alargar, por el cambio en el conjunto de caracteres
     */
    private static byte[] cadenaCode128(String datos) {
        int tamBufer = 1;
        byte[] bufer = new byte[100];

        byte codeActual = setCode(datos, (byte) 0); // STARTB;
        bufer[0] = startCode(codeActual);

        int len = datos.length();
        for (int i = 0; i < len;) {
            String c;

            byte codeNuevo = setCode(datos.substring(i, len), codeActual); // STARTB;
            if (codeActual != codeNuevo) {
                bufer[tamBufer] = codeNuevo;
                tamBufer++;
                if (codeNuevo != SHIFT) {
                    codeActual = codeNuevo;
                }
            }
            if ((codeActual == CODEC) && (datos.charAt(i) != asciiB((char) FNC1))) {
                c = datos.substring(i, i + 2);
                i += 2;
                if (c.charAt(1) == asciiB((char) FNC1)) {
                    bufer[tamBufer++] = CODEB;
                    codeActual = CODEB;
                    bufer[tamBufer++] = (byte) code128(c.substring(0, 1));
                    c = c.substring(1, 2);
                }
            } else {
                c = datos.substring(i, i + 1);
                i++;
            }
            bufer[tamBufer] = (byte) code128(c);
            tamBufer++;
        }

        byte[] resultado = new byte[tamBufer + 2]; // deja espacio para el verificador y el stop
        for (int i = 0; i < tamBufer; i++) {
            resultado[i] = bufer[i];
        }
        return resultado;
    }

    /**
     * El digito verificador, el caractere de inicio se suma pero no esta pesado, el caracter stop no cuenta
     */
    private static byte verificador(byte[] cadena128) {
        int len = cadena128.length - 2; // No se calcula el espacio del verificador y el stop
        int peso = 0;
        for (int i = 1; i < len; i++) {
            byte code = cadena128[i];
            peso = peso + code * (i);
        }
        peso += cadena128[0];
        byte verificador = (byte) (peso % 103);
        return verificador;
    }

    /*
     * Dado el codigo de barras en set code 128 lo convierte a ascii ya que el font en windows así se espera
     */
    private static String cadenaString(byte[] cadena128) {
        String cadena = "";
        int len = cadena128.length;
        for (int i = 0; i < len; i++) {
            byte code = cadena128[i];
            cadena = cadena + asciiB((char) code);
        }
        return cadena;
    }

    /*
     * Genera la secuencia de bytes necesaria para imprimir un codigo de barras code 128 con soporte GS1 y todos los conjuntos de caracteres A, B y C
     */
    public static String barras(String datos) {
        if (datos == null) {
            return "";
        }

        if (datos.length() < 6) {
            return "";  //No se pueden representar menos de 6 datos en una barra code 128
        }
        byte[] cadena128 = cadenaCode128(datos);
        byte verificador = verificador(cadena128);
        cadena128[cadena128.length - 2] = verificador;
        cadena128[cadena128.length - 1] = STOP;

        String cuerpo = cadenaString(cadena128);
        return cuerpo;
    }
}

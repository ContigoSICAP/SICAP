package com.sicap.clientes.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
	
	public static boolean isMismoPassword(String pass, String confPass){

		String encriptado = "";
		boolean resultado = false;
		try{
			encriptado = toMD5(pass);
		if( encriptado.equals(confPass) ){
			resultado = true;
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
        return resultado;
	}
	
      public static boolean validaPassword(String password){
    	
    	 boolean validaPass = false;
    	 final String valpass1 = "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*[$%/*])[a-zA-Z0-9$%/*]{8,16}$";
               if(password.matches(valpass1))
                  validaPass = true;
         return validaPass;  
  	 }
	
	public static boolean isAlfaNumerico(String Texto) {

		char[] Caracteres = new char[Texto.length()];
		Texto.getChars(0, Texto.length(), Caracteres, 0);
		for (int i = 0; i < Caracteres.length; i++) {
			if (!(('0' <= Caracteres[i] && Caracteres[i] <= '9')
					|| ('a' <= Caracteres[i] && Caracteres[i] <= 'z')
					|| (' ' == Caracteres[i]) || ('A' <= Caracteres[i] && Caracteres[i] <= 'Z'))) {
				return false;
			}
		}
		return true;
	}
/*
	public static String toMD5(String textoString) {

		byte[] defaultBytes = textoString.getBytes();
		StringBuffer hexString = new StringBuffer();

		try{

			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}

		}catch(NoSuchAlgorithmException nsae){

		}
		return hexString.toString();
	}
*/


    public static String toMD5(String texto) throws Exception{
    	String resultado = "";
    	try {
    		MessageDigest	md5 = MessageDigest.getInstance("MD5");
    		md5.update (texto.getBytes("ISO-8859-1"));
    		resultado = toHexString(md5.digest());
    	} catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.toString());
        } catch(UnsupportedEncodingException e){
        	System.out.print(e);
        }
        return resultado;
    }


    private static String toHexString (byte [] v)
	{
		StringBuffer	sb = new StringBuffer ();
		byte		n1, n2;
		
		for (int c = 0; c < v.length; c++)
		{
			n1 = (byte)((v[c] & 0xF0) >>> 4); // This line was changed
			n2 = (byte)((v[c] & 0x0F)); // So was this line
			
			sb.append (n1 >= 0xA ? (char)(n1 - 0xA + 'a') : (char)(n1 + '0'));
			sb.append (n2 >= 0xA ? (char)(n2 - 0xA + 'a') : (char)(n2 + '0'));
		}
		
		return sb.toString();
	}

}

package com.sicap.clientes.util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.sicap.clientes.helpers.*;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class MailUtil {

    private static Logger myLogger = Logger.getLogger(MailUtil.class);

    public boolean enviaCorreo(String Asunto, String Mensaje, String Destinatarios,int prioridad){
    
        boolean resultado = false;
        try {
            myLogger.info("Inicia enviaCorreo");
            final String sMail = CatalogoHelper.getParametro("MAIL_ENVIO");;
            final String sPassword = CatalogoHelper.getParametro("MAIL_PASS");;

            Properties pProp = new Properties();
            pProp.put("mail.smtp.auth", "true");
            pProp.put("mail.smtp.strattls", "true");
            pProp.put("mail.smtp.host", CatalogoHelper.getParametro("MAIL_SMPT_HOST"));
            pProp.put("mail.smtp.port", CatalogoHelper.getParametro("MAIL_SMPT_PORT"));
            myLogger.info("Termino seteo de Properties");

            Session session = Session.getInstance(pProp,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sMail, sPassword);
                        }
                    });
            myLogger.info("Genero Session del Mail");
            myLogger.info("Iniciando envio de mail");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sMail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Destinatarios));
            message.setSubject(Asunto);
            message.setText(Mensaje);
            if(prioridad>0){
                message.setHeader("X-Priority", Integer.toString(prioridad));
            }
            
            Transport.send(message);
            myLogger.info("Se envio el mail");
        } catch (Exception e) {
            myLogger.error("enviaCorreo", e);
        }
        return resultado;
    }
    
    public boolean enviaCorreo(String Asusnto, String Mensaje, String Destinatarios) {
	return enviaCorreo(Asusnto, Mensaje, Destinatarios,0);
        /*
        boolean resultado = false;
        try {
            myLogger.info("Inicia enviaCorreo");
            final String sMail = CatalogoHelper.getParametro("MAIL_ENVIO");;
            final String sPassword = CatalogoHelper.getParametro("MAIL_PASS");;

            Properties pProp = new Properties();
            pProp.put("mail.smtp.auth", "true");
            pProp.put("mail.smtp.strattls", "true");
            pProp.put("mail.smtp.host", CatalogoHelper.getParametro("MAIL_SMPT_HOST"));
            pProp.put("mail.smtp.port", CatalogoHelper.getParametro("MAIL_SMPT_PORT"));
            myLogger.info("Termino seteo de Properties");

            Session session = Session.getInstance(pProp,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sMail, sPassword);
                        }
                    });
            myLogger.info("Genero Session del Mail");
            myLogger.info("Iniciando envio de mail");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sMail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Destinatarios));
            message.setSubject(Asusnto);
            message.setText(Mensaje);
            Transport.send(message);
            myLogger.info("Se envio el mail");
        } catch (Exception e) {
            myLogger.error("enviaCorreo", e);
        }
        return resultado;
        */
    }

}

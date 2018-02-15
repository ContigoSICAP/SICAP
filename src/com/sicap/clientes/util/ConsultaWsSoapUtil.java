package com.sicap.clientes.util;

import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.vo.ClienteVO;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.soap.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

/**
 *
 * @author LDAVILA
 */
public class ConsultaWsSoapUtil {

    private static Logger myLogger = Logger.getLogger(ConsultaWsSoapUtil.class);

    public ClienteVO consultaClienteIxa(ClienteVO clienteConsulta) {
        ClienteVO clienteresp = null;
        String url = "";
        try {
            //Crea Conexion SOAP
            SOAPConnectionFactory soapConFact = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConFact.createConnection();
            //Envia el Mensaje SOAP
            url = CatalogoHelper.getParametro("SOAP_URL");
            SOAPMessage MesajeSOAP = soapConnection.call(creaXmlIxa(clienteConsulta), url);
            //Procesa XML
            clienteresp = leeMesajeSOAP(MesajeSOAP);

        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("creaXmlIxa", e);
        }

        return clienteresp;
    }

    public SOAPMessage creaXmlIxa(ClienteVO clienteConsulta) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        soapMessage.getSOAPHeader().setPrefix("soapenv");
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String evalMaterno="", evalPaterno = "";
        String serverURI = "http://tempuri.org/";
        try {

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.removeNamespaceDeclaration("SOAP-ENV");
            envelope.setPrefix("soapenv");

            envelope.addNamespaceDeclaration("tem", serverURI);
            /*
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
            <soapenv:Header/>
            <soapenv:Body>
                <tem:Consultar>
                    <!--Optional:-->
                    <tem:Usuario>ConsulIxaya</tem:Usuario>
                    <!--Optional:-->
                    <tem:Password>C0nsult4.1xA.2016</tem:Password>
                    <!--Optional:-->
                    <tem:NombreCompleto></tem:NombreCompleto>
                    <!--Optional:-->
                    <tem:ApellidoPaterno></tem:ApellidoPaterno>
                    <!--Optional:-->
                    <tem:ApellidoMaterno></tem:ApellidoMaterno>
                    <!--Optional:-->
                    <tem:RFC>PERA7611308P4</tem:RFC>
                    <!--Optional:-->
                    <tem:FechaNacimiento>30/11/1976</tem:FechaNacimiento>
                </tem:Consultar>
            </soapenv:Body>
            </soapenv:Envelope>
             */
            SOAPBody soapBody = envelope.getBody();
            //soapBody.setPrefix("soapenv");
            SOAPElement soapBodyElem = soapBody.addChildElement("Consultar", "tem");
            SOAPElement sbeUsuario = soapBodyElem.addChildElement("Usuario", "tem");
            SOAPElement sbePass = soapBodyElem.addChildElement("Password", "tem");
            SOAPElement sbeNombre = soapBodyElem.addChildElement("NombreCompleto", "tem");
            SOAPElement sbeaPaterno = soapBodyElem.addChildElement("ApellidoPaterno", "tem");
            SOAPElement sbeaMaterno = soapBodyElem.addChildElement("ApellidoMaterno", "tem");
            SOAPElement sbeRFC = soapBodyElem.addChildElement("RFC", "tem");
            SOAPElement sbeFechaNac = soapBodyElem.addChildElement("FechaNacimiento", "tem");

            sbeUsuario.addTextNode(CatalogoHelper.getParametro("SOAP_USUARIO"));
            sbePass.addTextNode(CatalogoHelper.getParametro("SOAP_PASS"));
            
            if (clienteConsulta.aMaterno.equals("X")||clienteConsulta.aMaterno.equals("")) {
                evalMaterno = " ";
            } else {
                evalMaterno =clienteConsulta.aMaterno;
            }
            if (clienteConsulta.aPaterno.equals("X")||clienteConsulta.aPaterno.equals("")) {
                evalPaterno = clienteConsulta.aMaterno;
                evalMaterno =" ";
            } else {
                evalPaterno =clienteConsulta.aPaterno;
            }
            sbeNombre.addTextNode(clienteConsulta.nombre);
            sbeaPaterno.addTextNode(evalPaterno);
            sbeaMaterno.addTextNode(evalMaterno);
            sbeRFC.addTextNode(clienteConsulta.rfc);
            sbeFechaNac.addTextNode(Convertidor.dateToString(clienteConsulta.fechaNacimiento, ClientesConstants.FORMATO_FECHA));

            MimeHeaders headers = soapMessage.getMimeHeaders();

            headers.addHeader("SOAPAction", serverURI + "IConsultarCliente/Consultar");

            soapMessage.saveChanges();

            /* Print the request message */
            System.out.print("Request SOAP Message = ");
            soapMessage.writeTo(System.out);
            System.out.println();
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("creaXmlIxa", e);
        } finally {
            myLogger.info("Enviando respuesta de creaXmlIxa");

        }
        return soapMessage;

    }

    public ClienteVO leeMesajeSOAP(SOAPMessage soapResponse) {
        ClienteVO clienteVO = null;
        String[] codigoError;
        try {
            SOAPBody body = soapResponse.getSOAPBody();
            DOMSource source = new DOMSource(body);
            StringWriter strRes = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(strRes));
            String mensaje = strRes.toString();
            myLogger.info("XMLRespuesta " + mensaje);
            codigoError = soapResponse.getMimeHeaders().getHeader("a:Error");

            NodeList returnList = body.getElementsByTagName("ConsultarResponse");

            String dato = returnList.item(0).getTextContent();
            String error = body.getElementsByTagName("a:Error").item(0).getTextContent();

            if (error.equals("")) {
                clienteVO = new ClienteVO();
                clienteVO.idCliente = Integer.parseInt(body.getElementsByTagName("a:IdClienteIxaya").item(0).getTextContent());
                clienteVO.nombre = body.getElementsByTagName("a:NombreCompleto").item(0).getTextContent();
                clienteVO.aPaterno = body.getElementsByTagName("a:ApellidoPaterno").item(0).getTextContent();
                clienteVO.aMaterno = body.getElementsByTagName("a:ApellidoMaterno").item(0).getTextContent();
                clienteVO.rfc = body.getElementsByTagName("a:RFC").item(0).getTextContent();
                clienteVO.fechaNacimiento = Convertidor.stringToSqlDate(body.getElementsByTagName("a:FechaNacimiento").item(0).getTextContent(), "dd/MM/yyyy");
            } else if (error.equals("No se encontró el cliente en la base de datos.")) {
                clienteVO = new ClienteVO();
                clienteVO.mensaje = error;
            }else{
                myLogger.info("Error en la Consulta del WS "+ error);
                myLogger.info("XMLRespuesta " + mensaje);            
            }
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("leeMesajeSOAP", e);
        }
        return clienteVO;
    }
}

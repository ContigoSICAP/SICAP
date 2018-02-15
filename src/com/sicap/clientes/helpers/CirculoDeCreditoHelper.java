package com.sicap.clientes.helpers;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ConsultaEfectuadaVO;
import com.sicap.clientes.vo.CuentaVO;
import com.sicap.clientes.vo.DomicilioVO;
import com.sicap.clientes.vo.PersonaVO;
import com.sicap.clientes.vo.ReporteCirculoEmpleoVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.EncabezadoCirculoVO;
import com.sicap.clientes.vo.ScoreFicoVO;
import java.util.Date;
import org.apache.log4j.Logger;

public class CirculoDeCreditoHelper {

    private static Logger myLogger = Logger.getLogger(CirculoDeCreditoHelper.class);
    CatalogoCDC catalogo = new CatalogoCDC();
    String formatDate = ClientesConstants.FORMATO_CIRCULO_XML;

    public String buildObjectToXML(ClienteVO cliente, int idSolicitud, boolean isCliente, int idObligado) throws CommandException {

        StringWriter writer = new StringWriter();
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

        try {

            String usuario = CatalogoHelper.getParametro("USER_CIRCULO");
            String contra = CatalogoHelper.getParametro("PASS_CIRCULO");
            String otorgante = CatalogoHelper.getParametro("OTORGANTE_CIRCULO");

            TreeMap catalogo = MapeoHelper.getCatalogoMapeo(1);

            String aPaterno = (isCliente ? cliente.aPaterno : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].aPaterno);
            String aMaterno = (isCliente ? cliente.aMaterno : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].aMaterno);
            if (aPaterno.equals("")) {
                aPaterno = "No Proporcionado";
            }
            if (aMaterno.equals("")) {
                aMaterno = "No Proporcionado";
            }
            String nombre = (isCliente ? cliente.nombre : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].nombre);
            String fechaNac = (isCliente ? Convertidor.dateToString(cliente.fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU) : Convertidor.dateToString(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU));
            String rfc = (isCliente ? cliente.rfc : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].rfc);

            String[] colonia = new String[cliente.direcciones.length];
            String[] municipio = new String[cliente.direcciones.length];
            String[] ciudad = new String[cliente.direcciones.length];
            String[] cp = new String[cliente.direcciones.length];
            String[] estado = new String[cliente.direcciones.length];
            String[] calle = new String[cliente.direcciones.length];
            boolean bucleInteligente = true;
            for (int i = 0; i < cliente.direcciones.length && bucleInteligente == true; i++) {
                colonia[i] = (isCliente ? cliente.direcciones[i].colonia : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.colonia);
                municipio[i] = (isCliente ? cliente.direcciones[i].municipio : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.municipio);
                ciudad[i] = isCliente ? cliente.direcciones[i].ciudad : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.ciudad;
                cp[i] = (isCliente ? cliente.direcciones[i].cp : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.cp);
                estado[i] = (String) catalogo.get(String.valueOf(isCliente ? cliente.direcciones[i].numestado : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.numestado));
                calle[i] = (isCliente ? cliente.direcciones[i].calle + " " + cliente.direcciones[i].numeroExterior
                        : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.calle + " "
                        + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.numeroExterior);
                bucleInteligente = isCliente ? true : false;
            }
            bucleInteligente = true;
            Namespace ns = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Element xmlTemp = null;
            Element xmlTemp1 = null;
            Element xmlElement = null;
            Element xmlRoot = new Element("Consulta");
            Document xmlDocument = new Document(xmlRoot);
            xmlRoot.setAttribute("noNamespaceSchemaLocation", "/Consulta.xsd", ns);

            /**
             * Agregamos el elemento Encabezado al XML de Consulta
             */
            xmlElement = addElement(xmlRoot, "Encabezado");
            addElementText(xmlElement, "ClaveOtorgante", otorgante);
            addElementText(xmlElement, "NombreUsuario", usuario);
            addElementText(xmlElement, "Password", contra);

            /**
             * Agregamos el elemento Personas
             */
            xmlRoot = addElement(xmlDocument.getRootElement(), "Personas");
            xmlElement = addElement(xmlRoot, "Persona");

            /**
             * Agregamos el elemento DetalleConsulta
             */
            xmlTemp = addElement(xmlElement, "DetalleConsulta");
            addElementText(xmlTemp, "FolioConsultaOtorgante", "453727");//Folio Nuestro
            addElementText(xmlTemp, "ProductoRequerido", "14");// Siempre 14 peticion de informacipon con FICO
            addElementText(xmlTemp, "TipoCuenta", "F");
            addElementText(xmlTemp, "ClaveUnidadMonetaria", "MX");
            addElementText(xmlTemp, "ImporteContrato", "4500");
            addElementText(xmlTemp, "NumeroFirma", "0152250000123456"); //Numero de firma asociado a la autorizacion del cliente

            /**
             * Agregamos el elemento Nombre
             */
            xmlTemp = addElement(xmlElement, "Nombre");
            addElementText(xmlTemp, "ApellidoPaterno", aPaterno);
            addElementText(xmlTemp, "ApellidoMaterno", aMaterno);
            addElementText(xmlTemp, "ApellidoAdicional", null);
            addElementText(xmlTemp, "Nombres", nombre);
            addElementText(xmlTemp, "FechaNacimiento", fechaNac);
            addElementText(xmlTemp, "RFC", rfc);
            addElementText(xmlTemp, "CURP", null);
            addElementText(xmlTemp, "Nacionalidad", "MX");
            addElementText(xmlTemp, "Residencia", null);
            addElementText(xmlTemp, "EstadoCivil", null);
            addElementText(xmlTemp, "Sexo", null);
            addElementText(xmlTemp, "ClaveElectorIFE", null);
            addElementText(xmlTemp, "NumeroDependientes", null);

            /**
             * Agregamos el elemento Domicilios
             */
            xmlTemp = addElement(xmlElement, "Domicilios");
            for (int i = 0; i < 1 && bucleInteligente == true; i++) {

                xmlTemp1 = addElement(xmlTemp, "Domicilio");
                addElementText(xmlTemp1, "Direccion", calle[i]);
                addElementText(xmlTemp1, "ColoniaPoblacion", colonia[i]);
                addElementText(xmlTemp1, "DelegacionMunicipio", municipio[i]);
                addElementText(xmlTemp1, "Ciudad", ciudad[i]);
                addElementText(xmlTemp1, "Estado", estado[i]);
                addElementText(xmlTemp1, "CP", cp[i]);
                addElementText(xmlTemp1, "FechaResidencia", null);
                addElementText(xmlTemp1, "NumeroTelefono", null);
                addElementText(xmlTemp1, "TipoDomicilio", null);
                addElementText(xmlTemp1, "TipoAsentamiento", null);
                bucleInteligente = isCliente ? true : false;
            }
            /**
             * Agregamos el elemento Empleos
             */
            xmlRoot = addElement(xmlElement, "Empleos");
            xmlTemp = addElement(xmlRoot, "Empleo");

            addElementText(xmlTemp, "NombreEmpresa", null);
            addElementText(xmlTemp, "Direccion", null);
            addElementText(xmlTemp, "ColoniaPoblacion", null);
            addElementText(xmlTemp, "DelegacionMunicipio", null);
            addElementText(xmlTemp, "Ciudad", null);
            addElementText(xmlTemp, "Estado", null);
            addElementText(xmlTemp, "CP", null);
            addElementText(xmlTemp, "NumeroTelefono", null);
            addElementText(xmlTemp, "Extension", null);
            addElementText(xmlTemp, "Fax", null);
            addElementText(xmlTemp, "Puesto", null);
            addElementText(xmlTemp, "FechaContratacion", null);
            addElementText(xmlTemp, "ClaveMoneda", null);
            addElementText(xmlTemp, "SalarioMensual", null);
            addElementText(xmlTemp, "FechaUltimoDiaEmpleo", null);

            /**
             * Agregamos el elemento CuentasReferencia
             */
            xmlRoot = addElement(xmlElement, "CuentasReferencia");
            addElementText(xmlRoot, "NumeroCuenta", null);

            Format xmlFormat = Format.getRawFormat();
            xmlFormat.setEncoding("ISO-8859-1");
            xmlFormat.setIndent("  ");
            XMLOutputter out = new XMLOutputter(xmlFormat);
            out.output(xmlDocument, writer);
            //System.out.println("INICIO ::: "+writer.toString()+" :::FIN");
        } catch (Exception e) {
            myLogger.error("buildObjectToXML", e);
            throw new CommandException(e.getMessage());
        }
        return writer.toString();
    }

    
        public String getXMLConsulta(ClienteVO cliente, int idSolicitud, boolean isCliente, int idObligado, String usuarioMovil) throws CommandException {

        StringWriter writer = new StringWriter();
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

        try {

            //String usuario = CatalogoHelper.getParametro("USER_CIRCULO");
            String usuario = usuarioMovil;
            String contra = CatalogoHelper.getParametro("PASS_CIRCULO");
            String otorgante = CatalogoHelper.getParametro("OTORGANTE_CIRCULO");

            TreeMap catalogo = MapeoHelper.getCatalogoMapeo(1);

            String aPaterno = (isCliente ? cliente.aPaterno : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].aPaterno);
            String aMaterno = (isCliente ? cliente.aMaterno : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].aMaterno);
            if (aPaterno.equals("")) {
                aPaterno = "No Proporcionado";
            }
            if (aMaterno.equals("")) {
                aMaterno = "No Proporcionado";
            }
            String nombre = (isCliente ? cliente.nombre : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].nombre);
            String fechaNac = (isCliente ? Convertidor.dateToString(cliente.fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU) : Convertidor.dateToString(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU));
            String rfc = (isCliente ? cliente.rfc : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].rfc);

            String[] colonia = new String[cliente.direcciones.length];
            String[] municipio = new String[cliente.direcciones.length];
            String[] ciudad = new String[cliente.direcciones.length];
            String[] cp = new String[cliente.direcciones.length];
            String[] estado = new String[cliente.direcciones.length];
            String[] calle = new String[cliente.direcciones.length];
            boolean bucleInteligente = true;
            for (int i = 0; i < cliente.direcciones.length && bucleInteligente == true; i++) {
                colonia[i] = (isCliente ? cliente.direcciones[i].colonia : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.colonia);
                municipio[i] = (isCliente ? cliente.direcciones[i].municipio : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.municipio);
                ciudad[i] = isCliente ? cliente.direcciones[i].ciudad : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.ciudad;
                cp[i] = (isCliente ? cliente.direcciones[i].cp : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.cp);
                estado[i] = (String) catalogo.get(String.valueOf(isCliente ? cliente.direcciones[i].numestado : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.numestado));
                calle[i] = (isCliente ? cliente.direcciones[i].calle + " " + cliente.direcciones[i].numeroExterior
                        : cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.calle + " "
                        + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].direccion.numeroExterior);
                bucleInteligente = isCliente ? true : false;
            }
            bucleInteligente = true;
            Namespace ns = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Element xmlTemp = null;
            Element xmlTemp1 = null;
            Element xmlElement = null;
            Element xmlRoot = new Element("Consulta");
            Document xmlDocument = new Document(xmlRoot);
            xmlRoot.setAttribute("noNamespaceSchemaLocation", "/Consulta.xsd", ns);

            /**
             * Agregamos el elemento Encabezado al XML de Consulta
             */
            xmlElement = addElement(xmlRoot, "Encabezado");
            addElementText(xmlElement, "ClaveOtorgante", otorgante);
            addElementText(xmlElement, "NombreUsuario", usuario);
            addElementText(xmlElement, "Password", contra);

            /**
             * Agregamos el elemento Personas
             */
            xmlRoot = addElement(xmlDocument.getRootElement(), "Personas");
            xmlElement = addElement(xmlRoot, "Persona");

            /**
             * Agregamos el elemento DetalleConsulta
             */
            xmlTemp = addElement(xmlElement, "DetalleConsulta");
            addElementText(xmlTemp, "FolioConsultaOtorgante", "453727");//Folio Nuestro
            addElementText(xmlTemp, "ProductoRequerido", "14");// Siempre 14 peticion de informacipon con FICO
            addElementText(xmlTemp, "TipoCuenta", "F");
            addElementText(xmlTemp, "ClaveUnidadMonetaria", "MX");
            addElementText(xmlTemp, "ImporteContrato", "4500");
            addElementText(xmlTemp, "NumeroFirma", "0152250000123456"); //Numero de firma asociado a la autorizacion del cliente

            /**
             * Agregamos el elemento Nombre
             */
            xmlTemp = addElement(xmlElement, "Nombre");
            addElementText(xmlTemp, "ApellidoPaterno", aPaterno);
            addElementText(xmlTemp, "ApellidoMaterno", aMaterno);
            addElementText(xmlTemp, "ApellidoAdicional", null);
            addElementText(xmlTemp, "Nombres", nombre);
            addElementText(xmlTemp, "FechaNacimiento", fechaNac);
            addElementText(xmlTemp, "RFC", rfc);
            addElementText(xmlTemp, "CURP", null);
            addElementText(xmlTemp, "Nacionalidad", "MX");
            addElementText(xmlTemp, "Residencia", null);
            addElementText(xmlTemp, "EstadoCivil", null);
            addElementText(xmlTemp, "Sexo", null);
            addElementText(xmlTemp, "ClaveElectorIFE", null);
            addElementText(xmlTemp, "NumeroDependientes", null);

            /**
             * Agregamos el elemento Domicilios
             */
            xmlTemp = addElement(xmlElement, "Domicilios");
            for (int i = 0; i < 1 && bucleInteligente == true; i++) {

                xmlTemp1 = addElement(xmlTemp, "Domicilio");
                addElementText(xmlTemp1, "Direccion", calle[i]);
                addElementText(xmlTemp1, "ColoniaPoblacion", colonia[i]);
                addElementText(xmlTemp1, "DelegacionMunicipio", municipio[i]);
                addElementText(xmlTemp1, "Ciudad", ciudad[i]);
                addElementText(xmlTemp1, "Estado", estado[i]);
                addElementText(xmlTemp1, "CP", cp[i]);
                addElementText(xmlTemp1, "FechaResidencia", null);
                addElementText(xmlTemp1, "NumeroTelefono", null);
                addElementText(xmlTemp1, "TipoDomicilio", null);
                addElementText(xmlTemp1, "TipoAsentamiento", null);
                bucleInteligente = isCliente ? true : false;
            }
            /**
             * Agregamos el elemento Empleos
             */
            xmlRoot = addElement(xmlElement, "Empleos");
            xmlTemp = addElement(xmlRoot, "Empleo");

            addElementText(xmlTemp, "NombreEmpresa", null);
            addElementText(xmlTemp, "Direccion", null);
            addElementText(xmlTemp, "ColoniaPoblacion", null);
            addElementText(xmlTemp, "DelegacionMunicipio", null);
            addElementText(xmlTemp, "Ciudad", null);
            addElementText(xmlTemp, "Estado", null);
            addElementText(xmlTemp, "CP", null);
            addElementText(xmlTemp, "NumeroTelefono", null);
            addElementText(xmlTemp, "Extension", null);
            addElementText(xmlTemp, "Fax", null);
            addElementText(xmlTemp, "Puesto", null);
            addElementText(xmlTemp, "FechaContratacion", null);
            addElementText(xmlTemp, "ClaveMoneda", null);
            addElementText(xmlTemp, "SalarioMensual", null);
            addElementText(xmlTemp, "FechaUltimoDiaEmpleo", null);

            /**
             * Agregamos el elemento CuentasReferencia
             */
            xmlRoot = addElement(xmlElement, "CuentasReferencia");
            addElementText(xmlRoot, "NumeroCuenta", null);

            Format xmlFormat = Format.getRawFormat();
            xmlFormat.setEncoding("ISO-8859-1");
            xmlFormat.setIndent("  ");
            XMLOutputter out = new XMLOutputter(xmlFormat);
            out.output(xmlDocument, writer);
            //System.out.println("INICIO ::: "+writer.toString()+" :::FIN");
        } catch (Exception e) {
            myLogger.error("buildObjectToXML", e);
            throw new CommandException(e.getMessage());
        }
        return writer.toString();
    }
    
    
    public String buildObjectToXML(ClienteVO cliente, int idSolicitud, boolean isCliente) throws CommandException {

        StringWriter writer = new StringWriter();
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

        try {

            String usuario = CatalogoHelper.getParametro("USER_CIRCULO");
            String contra = CatalogoHelper.getParametro("PASS_CIRCULO");
            String otorgante = CatalogoHelper.getParametro("OTORGANTE_CIRCULO");

            TreeMap catalogo = MapeoHelper.getCatalogoMapeo(1);

            String aPaterno = cliente.aPaterno;
            String aMaterno = cliente.aMaterno;
            if (aPaterno.equals("")) {
                aPaterno = "No Proporcionado";
            }
            if (aMaterno.equals("")) {
                aMaterno = "No Proporcionado";
            }
            String nombre = cliente.nombre;
            String fechaNac = Convertidor.dateToString(cliente.fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU);
            String rfc = cliente.rfc;

            Namespace ns = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Element xmlTemp = null;
            Element xmlTemp1 = null;
            Element xmlElement = null;
            Element xmlRoot = new Element("Consulta");
            Document xmlDocument = new Document(xmlRoot);
            xmlRoot.setAttribute("noNamespaceSchemaLocation", "/Consulta.xsd", ns);

            /**
             * Agregamos el elemento Encabezado al XML de Consulta
             */
            xmlElement = addElement(xmlRoot, "Encabezado");
            addElementText(xmlElement, "ClaveOtorgante", otorgante);
            addElementText(xmlElement, "NombreUsuario", usuario);
            addElementText(xmlElement, "Password", contra);

            /**
             * Agregamos el elemento Personas
             */
            xmlRoot = addElement(xmlDocument.getRootElement(), "Personas");
            xmlElement = addElement(xmlRoot, "Persona");

            /**
             * Agregamos el elemento DetalleConsulta
             */
            xmlTemp = addElement(xmlElement, "DetalleConsulta");
            addElementText(xmlTemp, "FolioConsultaOtorgante", "453727");//Folio Nuestro
            addElementText(xmlTemp, "ProductoRequerido", "14");// Siempre 14 peticion de informacipon con FICO
            addElementText(xmlTemp, "TipoCuenta", "F");
            addElementText(xmlTemp, "ClaveUnidadMonetaria", "MX");
            addElementText(xmlTemp, "ImporteContrato", "4500");
            addElementText(xmlTemp, "NumeroFirma", "0152250000123456"); //Numero de firma asociado a la autorizacion del cliente

            /**
             * Agregamos el elemento Nombre
             */
            xmlTemp = addElement(xmlElement, "Nombre");
            addElementText(xmlTemp, "ApellidoPaterno", aPaterno);
            addElementText(xmlTemp, "ApellidoMaterno", aMaterno);
            addElementText(xmlTemp, "ApellidoAdicional", null);
            addElementText(xmlTemp, "Nombres", nombre);
            addElementText(xmlTemp, "FechaNacimiento", fechaNac);
            addElementText(xmlTemp, "RFC", rfc);
            addElementText(xmlTemp, "CURP", null);
            addElementText(xmlTemp, "Nacionalidad", "MX");
            addElementText(xmlTemp, "Residencia", null);
            addElementText(xmlTemp, "EstadoCivil", null);
            addElementText(xmlTemp, "Sexo", null);
            addElementText(xmlTemp, "ClaveElectorIFE", null);
            addElementText(xmlTemp, "NumeroDependientes", null);

            /**
             * Agregamos el elemento CuentasReferencia
             */
            xmlRoot = addElement(xmlElement, "CuentasReferencia");
            addElementText(xmlRoot, "NumeroCuenta", null);

            Format xmlFormat = Format.getRawFormat();
            xmlFormat.setEncoding("ISO-8859-1");
            xmlFormat.setIndent("  ");
            XMLOutputter out = new XMLOutputter(xmlFormat);
            out.output(xmlDocument, writer);
            //System.out.println("INICIO ::: "+writer.toString()+" :::FIN");
        } catch (Exception e) {
            myLogger.error("buildObjectToXML", e);
            throw new CommandException(e.getMessage());
        }
        return writer.toString();
    }

    private Element addElement(Element parent, String name) {
        if ((parent == null) || (name == null)) {
            return null;
        }
        Element element = new Element(name.trim());
        parent.addContent(element);
        return element;
    }

    private Element addElementText(Element parent, String name, String text) {
        if ((parent == null) || (name == null)) {
            return null;
        }
        Element element = new Element(name.trim());
        parent.addContent(element);
        if (text != null) {
            text = text.trim();
            if (text.length() != 0) {
                try {
                    element.addContent(new Text(text));
                } catch (IllegalDataException ex) {
                    myLogger.error("IllegalDataException", ex);
                }
            }
        }
        return element;
    }

    public Object buildXMLToObject(Object xml) throws CommandException {
        Element xmlRoot = null;
        Document xmlDocument = null;
        SAXBuilder saxParser = new SAXBuilder();
        String elementRoot = null;
        ReporteCirculoVO reporte = null;

        try {
            xmlDocument = saxParser.build(new StringReader((String) xml));
            xmlRoot = xmlDocument.getRootElement();
            elementRoot = StringUtils.trimToEmpty(xmlRoot.getName());

            if (elementRoot.equals("Respuesta")) {

                reporte = new ReporteCirculoVO();
                reporte.setEncabezadoCirculo(getEncabezado(xmlRoot));
                reporte.setPersonas(getDatosGenerales(xmlRoot));
                reporte.setDomicilios(getDomicilio(xmlRoot));
                reporte.setEmpleos(getEmpleos(xmlRoot));
                reporte.setCuentas(getCuentas(xmlRoot));
                reporte.setConsultas(getConsultasEfectuadas(xmlRoot));
                reporte.setScoreFico(getScoreFICO(xmlRoot));

            } else if (elementRoot.equals("Error")) {
                return getError(xmlRoot);
            }
        } catch (Exception e) {
            myLogger.error("buildXMLToObject", e);
            throw new CommandException(e.getMessage());
        }
        return reporte;
    }

    private EncabezadoCirculoVO getEncabezado(Element xmlRoot) throws CommandException {
        EncabezadoCirculoVO encabezado = new EncabezadoCirculoVO();
        Element xmlElement = null;
        try {
            xmlElement = xmlRoot.getChild("Personas").getChild("Persona").getChild("Encabezado");

            encabezado.setFolioOtorgante(xmlElement.getChildTextTrim("FolioConsultaOtorgante"));
            encabezado.setClaveOtorgante(xmlElement.getChildTextTrim("ClaveOtorgante"));
            encabezado.setExpedienteEncontrado(xmlElement.getChildTextTrim("ExpedienteEncontrado"));
            encabezado.setFolioConsulta(xmlElement.getChildTextTrim("FolioConsulta"));

        } catch (Exception e) {
            myLogger.error("getEncabezado", e);
            throw new CommandException(e.getMessage());
        }
        return encabezado;
    }

    private PersonaVO getDatosGenerales(Element xmlRoot) throws CommandException {
        PersonaVO persona = new PersonaVO();
        Element xmlElement = null;
        try {
            xmlElement = xmlRoot.getChild("Personas").getChild("Persona").getChild("Nombre");

            persona.setNombre(xmlElement.getChildTextTrim("Nombres"));
            persona.setPaterno(xmlElement.getChildTextTrim("ApellidoPaterno"));
            persona.setMaterno(xmlElement.getChildTextTrim("ApellidoMaterno"));
            persona.setNacimiento(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaNacimiento"), formatDate));
            persona.setRfc(xmlElement.getChildTextTrim("RFC"));
            persona.setCurp(xmlElement.getChildTextTrim("CURP"));

        } catch (Exception e) {
            myLogger.error("getDatosGenerales", e);
            throw new CommandException(e.getMessage());
        }
        return persona;
    }

    private List getDomicilio(Element xmlRoot) throws CommandException {

        DomicilioVO domicilio = null;
        Iterator iter = null;
        Element xmlElement = null;
        Object object = null;
        List<DomicilioVO> list = new ArrayList<DomicilioVO>();

        try {

            iter = xmlRoot.getChild("Personas").getChild("Persona").getChild("Domicilios").getChildren().iterator();

            while (iter.hasNext()) {
                object = iter.next();
                if (object instanceof Element) {

                    domicilio = new DomicilioVO();
                    xmlElement = (Element) object;

                    domicilio.setCalle(xmlElement.getChildTextTrim("Direccion"));
                    domicilio.setColonia(xmlElement.getChildTextTrim("ColoniaPoblacion"));
                    domicilio.setDelegacion(xmlElement.getChildTextTrim("DelegacionMunicipio"));
                    domicilio.setCiudad(xmlElement.getChildTextTrim("Ciudad"));
                    domicilio.setEstado(xmlElement.getChildTextTrim("Estado"));
                    domicilio.setCp(xmlElement.getChildTextTrim("CP"));
                    domicilio.setTelefono(xmlElement.getChildTextTrim("NumeroTelefono"));
                    domicilio.setRegistro(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaRegistroDomicilio"), formatDate));

                    list.add(domicilio);
                }
            }
        } catch (Exception e) {
            myLogger.error("getDomicilio", e);
            throw new CommandException(e.getMessage());
        }
        return list;
    }

    private List getEmpleos(Element xmlRoot) throws CommandException {

        ReporteCirculoEmpleoVO empleo = null;
        Iterator iter = null;
        Element xmlElement = null;
        Object object = null;
        int cont = 0;
        List<ReporteCirculoEmpleoVO> list = new ArrayList<ReporteCirculoEmpleoVO>();

        try {

            iter = xmlRoot.getChild("Personas").getChild("Persona").getChild("Empleos").getChildren().iterator();

            while (iter.hasNext()) {
                cont++;
                object = iter.next();

                if (object instanceof Element) {

                    empleo = new ReporteCirculoEmpleoVO();
                    xmlElement = (Element) object;

                    empleo.setIdEmpleo(cont);
                    empleo.setEmpresa(xmlElement.getChildTextTrim("NombreEmpresa"));
                    empleo.setPuesto(xmlElement.getChildTextTrim("Puesto"));
                    empleo.setSueldo(Double.parseDouble(xmlElement.getChildTextTrim("SalarioMensual")));
                    empleo.setCalle(xmlElement.getChildTextTrim("Direccion"));
                    empleo.setColonia(xmlElement.getChildTextTrim("ColoniaPoblacion"));
                    empleo.setMunicipio(xmlElement.getChildTextTrim("DelegacionMunicipio"));
                    empleo.setCiudad(xmlElement.getChildTextTrim("Ciudad"));
                    empleo.setEstado(xmlElement.getChildTextTrim("Estado"));
                    empleo.setCp(xmlElement.getChildTextTrim("CP"));
                    empleo.setTelefono(xmlElement.getChildTextTrim("NumeroTelefono"));
                    empleo.setFechaRegistro(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaContratacion"), formatDate));

                    list.add(empleo);
                }
            }
        } catch (Exception e) {
            myLogger.error("getEmpleos", e);
            throw new CommandException(e.getMessage());
        }
        return list;
    }

    private List getCuentas(Element xmlRoot) {

        CuentaVO cuenta = null;
        Iterator iter = null;
        Element xmlElement = null;
        Object object = null;
        int cont = 0;
        List<CuentaVO> list = new ArrayList<CuentaVO>();

        try {

            iter = xmlRoot.getChild("Personas").getChild("Persona").getChild("Cuentas").getChildren().iterator();

            while (iter.hasNext()) {
                cont++;
                object = iter.next();

                if (object instanceof Element) {

                    cuenta = new CuentaVO();
                    xmlElement = (Element) object;

                    cuenta.setIdCuenta(cont);
                    cuenta.setFrecuencia(catalogo.getFrecuencia(xmlElement.getChildTextTrim("FrecuenciaPagos")));
                    cuenta.setTipoCredito(xmlElement.getChildTextTrim("TipoCredito"));//Catalogo
                    cuenta.setDescTipoCredito(catalogo.getTipoCredito(xmlElement.getChildTextTrim("TipoCredito")));//catalogo
                    cuenta.setTipoCuenta(catalogo.getTipoCuenta(xmlElement.getChildTextTrim("TipoCuenta")));
//					cuenta.setTipoNegocio    ( Convertidor.stringToInt(catalogo.getTipoCuenta(xmlElement.getChildTextTrim( "TipoNegocio" )) ));
                    cuenta.setOtorgante(xmlElement.getChildTextTrim("NombreOtorgante"));
                    cuenta.setNumeroPagos(Convertidor.stringToInt(xmlElement.getChildTextTrim("NumeroPagos")));
                    cuenta.setLimiteCredito(Convertidor.stringToDouble(xmlElement.getChildTextTrim("LimiteCredito")));
                    cuenta.setAPagar(Convertidor.stringToInt(xmlElement.getChildTextTrim("MontoPagar")));
                    cuenta.setCreditoMaximo(Convertidor.stringToDouble(xmlElement.getChildTextTrim("CreditoMaximo")));
                    cuenta.setSaldoActual(Convertidor.stringToDouble(xmlElement.getChildTextTrim("SaldoActual")));
                    cuenta.setSaldoVencido(Convertidor.stringToDouble(xmlElement.getChildTextTrim("SaldoVencido")));
                    cuenta.setFechaActualizacion(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaActualizacion"), formatDate));
                    cuenta.setFechaApertura(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaAperturaCuenta"), formatDate));
                    cuenta.setFechaCierre(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaCierreCuenta"), formatDate));
                    cuenta.setFechaUltimoPago(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaUltimoPago"), formatDate));
                    cuenta.setHistorial(xmlElement.getChildTextTrim("HistoricoPagos"));
                    cuenta.setCvePrevencion(xmlElement.getChildTextTrim("ClavePrevencion"));
                    cuenta.setPeorAtraso(Convertidor.stringToInt(xmlElement.getChildTextTrim("PeorAtraso")));
                    cuenta.setSaldoVencidoPeorAtraso(Convertidor.stringToDouble(xmlElement.getChildTextTrim("SaldoVencidoPeorAtraso")));
                    cuenta.setFechaPeorAtraso(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaPeorAtraso"), formatDate));

                    if (cuenta.getFechaActualizacion() != null && cuenta.getFrecuencia() != null
                            && !cuenta.getTipoCredito().equals("") && !cuenta.getOtorgante().equals("")) {
                        list.add(cuenta);
                    }
                }
            }
        } catch (Exception ex) {
            myLogger.error("getCuentas", ex);
        }
        return list;
    }

    private List getConsultasEfectuadas(Element xmlRoot) {

        ConsultaEfectuadaVO consulta = null;
        Iterator iter = null;
        Element xmlElement = null;
        Object object = null;
        int cont = 0;
        List<ConsultaEfectuadaVO> list = new ArrayList<ConsultaEfectuadaVO>();

        try {

            iter = xmlRoot.getChild("Personas").getChild("Persona").getChild("ConsultasEfectuadas").getChildren().iterator();

            while (iter.hasNext()) {

                object = iter.next();
                cont++;

                if (object instanceof Element) {

                    consulta = new ConsultaEfectuadaVO();
                    xmlElement = (Element) object;

                    consulta.setIdConsulta(cont);
                    consulta.setFechaConsulta(Convertidor.stringToDate(xmlElement.getChildTextTrim("FechaConsulta"), formatDate));
                    consulta.setOtorgante(xmlElement.getChildTextTrim("NombreOtorgante"));
                    consulta.setTipoCredito(catalogo.getTipoCredito(xmlElement.getChildTextTrim("TipoCredito")));
                    consulta.setMonto(Convertidor.stringToDouble(xmlElement.getChildTextTrim("ImporteCredito")));
                    consulta.setMoneda(catalogo.getMoneda(xmlElement.getChildTextTrim("ClaveUnidadMonetaria")));

                    list.add(consulta);
                }
            }
        } catch (Exception ex) {
            myLogger.error("getConsultasEfectuadas", ex);
        }
        return list;
    }

    private Notification[] getError(Element xmlRoot) throws CommandException {
        String error = null;
        Element xmlElement = null;
        Object object = null;
        Iterator iter = null;
        int i = 0;
        Notification notificaciones[] = new Notification[xmlRoot.getChild("Errores").getChildren().size()];

        try {
            iter = xmlRoot.getChild("Errores").getChildren().iterator();

            while (iter.hasNext()) {
                object = iter.next();
                if (object instanceof Element) {
                    xmlElement = (Element) object;
                    error = xmlElement.getText();
                    notificaciones[i] = new Notification(ClientesConstants.INFO_TYPE, error);
                    i++;
                }
            }

        } catch (Exception e) {
            myLogger.error("getError", e);
            throw new CommandException(e.getMessage());
        }
        return notificaciones;
    }

    //Valida si hay un caso de cve de prevención
    public static boolean getCveSegmento(String cveSegmento) {

        String[] segmentos = new String[10];
        segmentos[0] = "CM";
        segmentos[1] = "CV";
        segmentos[2] = "EL";
        segmentos[3] = "FD";
        segmentos[4] = "FR";
        segmentos[5] = "IR";
        segmentos[6] = "LC";
        segmentos[7] = "LG";
        segmentos[8] = "PC";
        segmentos[9] = "RV";

        for (int i = 0; i <= 9; i++) {
            if (cveSegmento.equals(segmentos[i])) {
                return true;
            }
        }

        return false;
    }

    private List getScoreFICO(Element xmlRoot) {

        ScoreFicoVO score = null;
        Iterator iter = null;
        Element xmlElement = null;
        Object object = null;
        int cont = 0;
        List<ScoreFicoVO> list = new ArrayList<ScoreFicoVO>();
        try {
            iter = xmlRoot.getChild("Personas").getChild("Persona").getChild("Scores").getChildren().iterator();
            while (iter.hasNext()) {
                object = iter.next();
                cont++;
                if (object instanceof Element) {
                    score = new ScoreFicoVO();
                    xmlElement = (Element) object;
                    score.setIdScore(cont);
                    score.setNombre(xmlElement.getChildTextTrim("NombreScore"));
                    score.setCodigo(Integer.parseInt(xmlElement.getChildTextTrim("Codigo")));
                    score.setValor(Integer.parseInt(xmlElement.getChildTextTrim("Valor")));
                    score.setRazon1(xmlElement.getChildTextTrim("Razon1"));
                    score.setRazon2(xmlElement.getChildTextTrim("Razon2"));
                    score.setRazon3(xmlElement.getChildTextTrim("Razon3"));
                    score.setRazon4(xmlElement.getChildTextTrim("Razon4"));
                    list.add(score);
                }
            }
        } catch (Exception ex) {
            myLogger.error("getScoreFICO", ex);
        }
        return list;
    }

    public boolean enRango(CuentaVO cuenta) {
        //Debe validar si es menor a 2 años
        boolean respuesta;
        Date fechaApertura = cuenta.getFechaApertura();
        int anios = FechasUtil.obtenAniosDiferencia(fechaApertura, new Date());
        if (anios < 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean evaluar(CuentaVO cuenta, int cuentasEnRango) {

        if (cuentasEnRango > 0) {
            return enRango(cuenta);
        } else {
            return enRangoFechaActualizacion(cuenta);
        }

    }

    public boolean enRangoFechaActualizacion(CuentaVO cuenta) {
        //Debe validar si es menor a 5 años
        boolean respuesta;
        Date fechaActualizacion = cuenta.getFechaActualizacion();
        int anios = FechasUtil.obtenAniosDiferencia(fechaActualizacion, new Date());
        if (anios < 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esComunal(CuentaVO cuenta) {
        //Debe validar si es comunal
        boolean respuesta = false;
        int numPagos;

        String tipoCredito = cuenta.getTipoCredito();
        if (tipoCredito.equalsIgnoreCase("BC") || tipoCredito.equalsIgnoreCase("GS")) {
            respuesta = true;
        } else {
            numPagos = cuenta.getNumeroPagos();
            String frecuencia = cuenta.getFrecuencia();
            if (numPagos == 16 && frecuencia.equalsIgnoreCase("SEMANAL")
                    || numPagos == 12 && frecuencia.equalsIgnoreCase("SEMANAL")) {
                respuesta = true;
            }

        }
        return respuesta;
    }

    public boolean esComunicacionesServicios(CuentaVO cuenta) {
        //Debe validar si es de comunicaciones y servicios
        boolean respuesta = false;
        String otorgante = cuenta.getOtorgante();
        if (otorgante.equalsIgnoreCase(ClientesConstants.TIPO_NEGOCIO_CC_COMUNICACIONES)
                || otorgante.equalsIgnoreCase(ClientesConstants.TIPO_NEGOCIO_CC_TEL_CELULAR)
                || otorgante.equalsIgnoreCase(ClientesConstants.TIPO_NEGOCIO_CC_TEL_LOCAL_LD)
                || otorgante.equalsIgnoreCase(ClientesConstants.TIPO_NEGOCIO_CC_TVPAGA)) {
            respuesta = true;
        }

        return respuesta;
    }

    public int obtenAtrasos(CuentaVO cuenta) {
        int atrasos = cuenta.getPeorAtraso();
        String frecuencia = cuenta.getFrecuencia();

        if (frecuencia.equalsIgnoreCase("SEMANAL")) {

        } else if (frecuencia.equalsIgnoreCase("QUINCENAL") || frecuencia.equalsIgnoreCase("CATORCENAL")) {
            atrasos = atrasos * 2;
        } else if (frecuencia.equalsIgnoreCase("MENSUAL") || frecuencia.equalsIgnoreCase("BIMESTRAL") || frecuencia.equalsIgnoreCase("TRIMESTRAL")) {
            atrasos = atrasos * 4;
        }

        return atrasos;
    }

    public CuentaVO obtenComunalMasReciente(CuentaVO ultimoTemporal, CuentaVO cuentaComunal) {
        //Regresa la cuenta comunal más reciente
        long fechaUltimoTemporal;
        long fechaCuentaComunal;

        if (ultimoTemporal == null) {
            return cuentaComunal;
        } else {
            fechaUltimoTemporal = ultimoTemporal.getFechaApertura().getTime();
            fechaCuentaComunal = ultimoTemporal.getFechaApertura().getTime();
            if (fechaUltimoTemporal > fechaCuentaComunal) {
                return ultimoTemporal;
            } else {
                return cuentaComunal;
            }
        }

    }

    public int getScoreFico(List scoreFico) {

        int puntaje = 0;
        Iterator iterator = scoreFico.iterator();
        ScoreFicoVO scoreCC = null;
        ScoreFicoVO score = new ScoreFicoVO();
        while (iterator.hasNext()) {
            scoreCC = (ScoreFicoVO) iterator.next();
            puntaje = scoreCC.getValor();
        }
        return puntaje;
    }

}

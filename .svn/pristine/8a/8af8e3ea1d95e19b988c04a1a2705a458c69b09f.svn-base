package com.sicap.clientes.helpers;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConvertNumberToString;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class GeneraDocumentosHelper {

    Document document;
    Font titleFont;
    Font headFont;
    Font tableFont;
    Font labelFont;
    Font smallFont;
    Font numberFont;
    int letra;
    Table dataTable;
    String fechaHoy;
    private static Logger myLogger = Logger.getLogger(GeneraDocumentosHelper.class);

    public void doControlPagosPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        document = new Document(PageSize.LETTER.rotate());
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 20, 40);
            document.open();
            contenidoControlPagos(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doControlPagosPDF", exception);
        }
    }

    public void doAvisoPrivacidad(HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 20, 40);
            document.open();
            contenidoAvisoPrivacidad(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doAvisoPrivacidad", exception);
        }
    }

    public void dolistaAsistenciaPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        document = new Document(PageSize.LETTER.rotate());
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 20, 40);
            document.open();
            contenidolistaAsistencia(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("dolistaAsistenciaPDF", exception);
        }
    }
    
    public void doCartaRenovacionPDF(HttpServletRequest request, HttpServletResponse response, GrupoVO grupo, CicloGrupalVO ciclo) {
        myLogger.info("CARTA RENOVACION");
        document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            letra = Font.getFamilyIndex("Verdana");
            titleFont = new Font(letra, 8F, 1);
            headFont = new Font(letra, 10F, 1);
            tableFont = new Font(letra, 5.0F, 1);
            labelFont = new Font(letra, 5.0F, 2);
            smallFont = new Font(letra, 4.0F, 1);
            numberFont = new Font(letra, 5.0F, 1);
            document.setMargins(40, 40, 20, 40);
            document.open();
            contenidoCartaRenovacion(grupo, ciclo, response);
            document.close();
        } catch (Exception exception) {
            myLogger.error("doCartaRenovacionPDF", exception);
        }
    }

    private void contenidoAvisoPrivacidad(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPTable tablaInterna = null;
            PdfPCell cell = null;
            String texto = "";
            Font boldFont =new Font(letra, 8F, Font.BOLD);
            Font bodyFont = new Font(letra, 8F, Font.NORMAL);

            tabla = new PdfPTable(1);
            tabla.setWidthPercentage(100);
            texto = "Resultado de la Entrevista:\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "INFORMACIÓN ADICIONAL. ¿Usted desempeña o ha desempeñado funciones públicas destacadas en un país extranjero o en territorio nacional, considerando entre otros, a los jefes de estado o de Gobierno, Líderes Políticos, ";
            texto += "Funcionarios Gubernamentales, Judiciales o Militares de alta jerarquía, altos ejecutivos de empresas estatales o funcionarios o miembros importantes de partidos políticos?. Respuesta: SI_____ / NO____.\n\n";
            texto += "En caso afirmativo: a) puesto o cargo: ___________________. b) Periodo: ___________________.\n\n";
            texto += "¿Algún familiar de usted de hasta segundo grado de consanguinidad o afinidad (cónyuge, concubina, concubinario, padre, madre, hijos, hermanos, abuelos, tíos, primos, cuñados, suegros, yernos o nueras), se encuentra en el supuesto antes mencionado?\n\n";
            texto += "Respuesta: SI_____ / NO____.\n\n";
            texto += "En caso afirmativo: a) Nombre Completo:  Apellido Paterno__________________________\n\n";
            texto += "Apellido Materno____________________  Nombre(s): _____________________________\n\n";
            texto += "b) Parentesco_______________________. c) Puesto o Cargo: ________________________\n\n";
            texto += "d) Periodo: _________________________.\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            document.add(tabla);

            int col[] = {42, 18, 17, 23};
            tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);
            texto = "Obligado Solidario: no aplica, salvo por la obligación solidaria que se establece en el contrato de crédito, con respecto a lo solicitante, que refiere el párrafo inmediato siguiente, cuya información se asienta de igual forma en las solicitudes de crédito que les corresponde.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            tablaInterna = new PdfPTable(1);
            tabla.setWidthPercentage(100);
            texto = "BENEFICIARIO FINAL";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            cell.setBorderWidthLeft(0);
            cell.setBorderWidthRight(0);
            tablaInterna.addCell(cell);
            texto = "No aplica";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaInterna.addCell(cell);
            tabla.addCell(tablaInterna);
            tablaInterna = new PdfPTable(1);
            tabla.setWidthPercentage(100);
            texto = "PROPIETARIO REAL";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            cell.setBorderWidthLeft(0);
            cell.setBorderWidthRight(0);
            tablaInterna.addCell(cell);
            texto = "No aplica";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaInterna.addCell(cell);
            tabla.addCell(tablaInterna);
            tablaInterna = new PdfPTable(1);
            tabla.setWidthPercentage(100);
            texto = "PROVEEDOR DE RECURSOS";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            cell.setBorderWidthLeft(0);
            cell.setBorderWidthRight(0);
            tablaInterna.addCell(cell);
            texto = "No aplica";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaInterna.addCell(cell);
            tabla.addCell(tablaInterna);
            document.add(tabla);

            texto = "\n\nAVISO DE PRIVACIDAD\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "Identidad y Domicilio\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "CEGE Capital, S.A.P.I. de C.V., SOFOM, E.N.R. (en adelante “CEGE”), con domicilio en Carretera México Toluca no. 2430 piso 4, Col. Lomas de Bezares, Delegación Miguel Hidalgo, C.P. 11910, Ciudad de México.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Datos Personales a Tratar\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Para llevar a cabo las finalidades descritas en el presente aviso de privacidad, utilizaremos los siguientes datos personales: i) Datos de Contacto, ii) Datos de Identificación, iii) Datos Laborales y iv) Datos Patrimoniales.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Datos Personales Sensibles\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Además de los datos personales mencionados anteriormente, para las finalidades informadas en el presente aviso de privacidad utilizaremos los siguientes datos personales considerados como sensibles, que requieren de especial protección: i) Estado de salud físico o mental, presente, pasado o futuro, así como información genética; ii) Datos de origen étnico.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Finalidades del Tratamiento\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Los datos personales que recabamos de usted los utilizaremos para las siguientes finalidades que son necesarias para el servicio que solicita: (i) Verificar y confirmar su identidad y situación patrimonial, administrar y operar los servicios y productos bancarios que solicita o contrata con nosotros; ";
            texto += "(ii) Consultar su información e historial crediticio en las Sociedades de Información Crediticia; (iii) prevenir y/o detectar fraudes u otros ilícitos en su agravio y/o de CEGE; (iv) contratar seguros por CEGE que sean obligatorios, relacionados con el Crédito, independientes y/o adicionales a éstos; (v) Reportar información a las autoridades correspondientes con relación a su Crédito; (vi) Efectuar cobranza extrajudicial y/o judicial de su Crédito y; (vii) Cumplir con las leyes, reglamentos y disposiciones legales aplicables.\n\n";
            texto += "De manera adicional, utilizaremos su información personal para las siguientes finalidades que no son necesarias para el servicio solicitado, pero que nos permiten y facilitan brindarle una mejor atención: (i) Medición de la calidad del servicio, (ii) Fines estadísticos, (iii) Publicidad; (iv) Consultas, investigaciones y/o revisiones de las actividades, operaciones, quejas y/o reclamaciones relacionadas con el Crédito y; (v) Promoción y/o telemarketing de los bienes, productos y/o servicios que sean ofrecidos por CEGE, por cualquier medio.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "Mecanismo para manifestar la negativa\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "En caso de que no desee que sus datos personales se utilicen para estos fines, indíquelo a continuación        Sí O      No O     \n";
            texto +="La negativa para el uso de sus datos personales para estas finalidades no podrá ser un motivo para que le neguemos los servicios y productos que solicita o contrata con nosotros.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Transferencia de datos\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Sus datos personales se transferirán a nivel nacional o internacional a las siguientes personas: (i) A las instituciones financieras y/o inversionistas para llevar a cabo las finalidades primarias antes establecidas cuando CEGE ceda, transmita, transfiera, afecte, grave y/o negocie, en cualquier forma, los derechos de crédito del o los Créditos que se registren en el correspondiente contrato o contratos de apertura de crédito celebrados y/o, en el o los pagarés suscritos al amparo de ";
            texto +="los mismos, o cuando CEGE celebre fusiones y/o escisiones en las que dichas personas se vean involucradas; (ii) A la sociedad controladora o a las subsidiarias o afiliadas de CEGE para llevar a cabo las finalidades primarias antes establecidas, cuando CEGE requiera de su apoyo para la operación y/o administración del o los ";
            texto +="Créditos o cuando CEGE celebre fusiones y/o escisiones en las que dichas sociedades se vean involucradas; (iii) A las compañías de seguros para la contratación de seguros relacionados con el o los Créditos; y, (iv) A alguna autoridad con la finalidad de dar cumplimiento a alguna ley, reglamento o disposición legal aplicable cuando la transferencia sea obligatoria.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            p = new Paragraph();
            texto = "Cláusula para consentir la transferencia\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Las transferencias que se mencionan anteriormente no requieren de su consentimiento.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Medios y Procedimiento para Ejercer Derechos ARCO\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Usted tiene derecho a conocer qué datos personales tenemos de usted, para qué los utilizamos y las condiciones del uso que les damos (";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Acceso";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "). También es su derecho solicitar la corrección de su información personal en caso de que esté desactualizada, sea inexacta o incompleta \n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "(";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Rectificación";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "); que la eliminemos de nuestros registros o bases de datos cuando considere que la misma no está siendo utilizada conforme a los principios, deberes y obligaciones previstas en la normativa (";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Cancelación";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "); así como oponerse al uso de sus datos personales para fines específicos (";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Oposición";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "). Estos derechos se conocen como derechos ARCO. Si desea ejercer sus derechos ARCO o desea revocar su consentimiento, podrá: (i) Acudir al Departamento de Protección de Datos Personales con domicilio en Carretera México Toluca no. 2430 piso 4, Col. Lomas de ";
            texto +="Bezares, Delegación Miguel Hidalgo, C.P. 11910, Ciudad de México, teléfonos (55) 41602100 o 01800 8378760 (lada sin costo), en un horario de atención de lunes a viernes de 8:00 a 17:00 horas; o, (ii) Enviar un correo electrónico a la dirección ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "datos.personales@fcontigo.com ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ", con su nombre y datos de contacto. Los procedimientos tanto para el ejercicio de sus derechos ARCO, como para la revocación de su consentimiento se le darán a conocer a través de los medios anteriores una vez recibida su solicitud.\n";
            texto +="Usted puede revocar el consentimiento que nos haya otorgado para el tratamiento de sus datos personales; sin embargo, es importante que tenga en cuenta que no en todos los casos podremos atender su solicitud o concluir el uso de forma inmediata, ya que es posible que por alguna obligación legal requiramos seguir tratando sus ";
            texto +="datos personales. De igual forma, usted deberá considerar que para ciertos fines, la revocación de su consentimiento implicará que no le podamos seguir prestando el servicio que nos solicitó, o la conclusión de su relación con nosotros.\n";
            texto +="Si usted considera que su derecho de protección de datos personales ha sido vulnerado por alguna conducta de nuestros empleados o de nuestras actuaciones o respuestas, puede acudir ante el Instituto Nacional de Transparencia, Acceso a la Información y Protección de Datos Personales (INAI). Para mayor información visite ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.inai.org.mx";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.add(ck);
            texto = ".\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Opciones y medios para limitar el uso y divulgación de datos personales\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Usted podrá inscribirse en el Registro Público de Usuarios conforme a la Ley de Protección y Defensa al Usuario de Servicios Financieros con la finalidad de limitar el uso y divulgación de sus datos personales. Este registro (REUS), le permitirá estar considerado en una base de datos a fin de restringir llamadas promocionales en números ";
            texto += "particulares (fijo o móvil), laborales o correo electrónico, buscando mantener a salvo su privacidad y evitar las molestias que causan estas llamadas o envío de información. Podrá registrarse al REUS a través de los medios que se establecen en la siguiente página de Internet: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "http://portalif.condusef.gob.mx/REUS/home.php";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ".\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "El uso de tecnologías de rastreo en nuestro portal de Internet\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "Le informamos que en nuestra página de Internet utilizamos cookies, web beacons y otras tecnologías a través de las cuales es posible monitorear su comportamiento como usuario de Internet, brindarle un mejor servicio y experiencia de usuario al navegar en nuestra página, así como ofrecerle nuevos productos y servicios basados en ";
            texto +="sus preferencias. Los datos personales que obtenemos de estas tecnologías de rastreo son los siguientes: Horario y tiempo de navegación en nuestra página de Internet, secciones consultadas, y páginas de Internet previamente consultadas a la nuestra. Le informamos que sus datos personales que se obtienen a través de estas tecnologías no se comparten con terceros; estas tecnologías podrán deshabilitarse de acuerdo al explorador utilizado.\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "Cambios al Aviso de Privacidad\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            p.add(ck);
            texto = "El presente aviso de privacidad puede sufrir modificaciones, cambios o actualizaciones derivadas de nuevos requerimientos legales; de nuestras propias necesidades por los productos o servicios que ofrecemos; de nuestras prácticas de privacidad; de cambios en nuestro modelo de negocio, o por otras causas. Nos comprometemos a mantenerlo informado sobre los cambios que pueda sufrir el presente aviso de privacidad, a través de nuestro portal ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            texto = "www.fcontigo.com";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.UNDERLINE));
            p.setFont(boldFont);
            p.add(ck);
            p.setFont(bodyFont);
            texto = ".\n\n\n\n\n\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

            texto = "________________________________________________                                           ________________________________________________\n";
            texto += "            Lugar y fecha de firma del aviso de privacidad                                                                                    Nombre y firma del (la) Titular\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            p.setLeading(12);
            document.add(p);

        } catch (Exception e) {
            myLogger.error("contenidoAvisoPrivacidad", e);
        }
    }

    private void contenidolistaAsistencia(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            String texto = "";
            Color grayColor = new Color(0x5A5959);
            CatalogoDAO paramDAO = new CatalogoDAO();
            ParametroVO paramVO = new ParametroVO();
            paramVO = paramDAO.getParametro("RUTA_IMAGEN_REPORTE");
            Image logoCrediequipos = Image.getInstance(paramVO.valor);
            logoCrediequipos.scalePercent(20);
            document.add(logoCrediequipos);
            texto = "LISTA DE ASISTENCIA\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_CENTER);
            p.setLeading(12);
            document.add(p);

            int colHeader[] = {25, 25, 25, 25};
            tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(colHeader);
            texto = "Grupo";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Sucursal";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Elaboró : Asesor";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Revisó : Supervisor";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "" + grupo.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "NOMBRE_SUCURSAL";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setBackgroundColor(Color.YELLOW);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setBackgroundColor(Color.YELLOW);
            tabla.addCell(cell);
            document.add(tabla);

            texto = "\n\n\n"; //Para que no se mezclen las tablas
            p = new Paragraph(texto);
            p.setLeading(12);
            document.add(p);

            int colAsistencia[] = {3, 65, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
            tabla = new PdfPTable(18);
            tabla.setWidthPercentage(100);
            tabla.setWidths(colAsistencia);
            cell = new PdfPCell();
            cell.setBorderColor(grayColor);
            cell.setColspan(2);
            tabla.addCell(cell);
            texto = "Control de semana";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setBorderColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(16);
            tabla.addCell(cell);
            texto = "No.";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setBorderColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            texto = "Nombre";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setBorderColor(grayColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            for (int i = 1; i <= 16; i++) {
                texto = "" + i;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
                cell = new PdfPCell(p);
                cell.setBorderColor(grayColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);
            }
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                texto = "" + (i + 1);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "" + ciclo.integrantes[i].nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                for (int j = 1; j <= 16; j++) {
                    cell = new PdfPCell();
                    cell.setBorderColor(grayColor);
                    tabla.addCell(cell);
                }
            }
            texto = "Visto bueno del asesor";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            for (int i = 1; i <= 16; i++) {
                cell = new PdfPCell();
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
            }
            document.add(tabla);

            texto = "\n\n\n"; //Para que no se mezclen las tablas
            p = new Paragraph(texto);
            p.setLeading(12);
            document.add(p);

            int colObs[] = {60, 40};
            tabla = new PdfPTable(2);
            tabla.setWidthPercentage(60);
            tabla.setWidths(colObs);
            texto = "OBSERVACIONES";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Nombre del cliente";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Observaciones";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            for (int i = 1; i <= 5; i++) {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                cell.setPadding(8);
                tabla.addCell(cell);
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                cell.setPadding(8);
                tabla.addCell(cell);
            }
            document.add(tabla);

            p = new Paragraph();
            texto = "\n\n\n\nNota: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "la persona que tome la asistencia deberá anotar\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "A: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Asistencia\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "R: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Retardo\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            texto = "F: ";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.BOLD));
            p.add(ck);
            texto = "Falta\n";
            ck = new Chunk(texto, new Font(Font.HELVETICA, 7, Font.NORMAL));
            p.add(ck);
            p.setAlignment(Chunk.ALIGN_LEFT);
            p.setLeading(12);
            document.add(p);
        } catch (Exception e) {
            myLogger.error("contenidoAsistencia", e);
        }
    }
    
    private void contenidoControlPagos(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) throws ParseException {

        String fecha = CatalogoHelper.getParametro("FECHA_LIBERACION_HR_CP");
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        Date fechaHojaResumenControlPagos = sdf.parse(fecha);
        myLogger.info("Fecha Desembolso control pagos: "+ciclo.saldo.getFechaDesembolso());
        myLogger.info("Fecha Liberacion Hoja Resumen y Control Pagos: "+fecha);
        
        if(ciclo.saldo.getFechaDesembolso().compareTo(fechaHojaResumenControlPagos) >= 0){
            myLogger.info("Control Pagos Nueva Version");
            contenidoControlPagosFinal(grupo, ciclo, response);
        }else{
            myLogger.info("Control Pagos version anterior");
            contenidoControlPagosAnterior(grupo, ciclo, response);
        }
    }
    
    private void contenidoControlPagosFinal(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPTable tablaInterna = null;
            PdfPTable tablaHead = null;
            PdfPTable tablaFoot = null;
            PdfPCell cell = null;
            String texto = "";
            TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
            TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
            TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
            Color grayColor = new Color(0x5A5959);
            CatalogoDAO paramDAO = new CatalogoDAO();
            ParametroVO paramVO = new ParametroVO();
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            SaldoIBSDAO saldo = new SaldoIBSDAO();
            Calendar calendario = Calendar.getInstance();
            Date fechaActual = Convertidor.toSqlDate(calendario.getTime());
            paramVO = paramDAO.getParametro("RUTA_IMAGEN_REPORTE");
            //Image logoCrediequipos = Image.getInstance(paramVO.valor);
            Image logoCrediequipos = Image.getInstance(paramVO.valor);
            logoCrediequipos.scalePercent(20);
            int plazo = ciclo.getPlazo();
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
            TablaAmortVO tablaAmortizacion = new TablaAmortVO();
            TablaAmortizacionVO tablaAmortizacionclie = new TablaAmortizacionVO();
            TablaAmortDAO tablaDao = new TablaAmortDAO();
            tablaAmortizacion = tablaDao.getDivVigente(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            if (tablaAmortizacion == null) {
                tablaAmortizacion = tablaDao.getUltmioDivPagado(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            }
            if (tablaAmortizacion == null) {
                tablaAmortizacionclie = tablaDao.getElementos(ciclo.getIdGrupo(), ciclo.getIdCiclo());
            }
            int colHead[] = {8, 10, 1, 10, 20, 1, 14, 10, 1, 25};
            tablaHead = new PdfPTable(10);
            tablaHead.setWidthPercentage(100);
            tablaHead.setWidths(colHead);
            texto = "Control de Pagos y Garantías Semanales\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(7);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            cell = new PdfPCell(logoCrediequipos);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "Equipo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = grupo.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Fecha: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            if (tablaAmortizacion != null) {
                texto = "" + Convertidor.dateToString(tablaAmortizacion.getFechaPago());
            } else {
                texto = "" + Convertidor.dateToString(tablaAmortizacionclie.fechaPago);
            }

            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaHead.addCell(cell);
            texto = "Día de Reunión: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.diaReunion, catDiasReunon);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);

            texto = "Sucursal: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = sucursal.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Asesor: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.asesor, catEjecutivos);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaHead.addCell(cell);
            texto = "Hora: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.horaReunion, catHorasReunion);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);

            texto = "Ciclo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.idCiclo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "No. de Pago: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            int numPago = 1;
            if (tablaAmortizacion != null) {
                numPago = tablaAmortizacion.getNumPago();
                texto = "" + numPago;
            } else {
                texto = String.valueOf(numPago);
            }
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Domicilio de Reunión: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.getDireccionReunion().calle + " " + ciclo.getDireccionReunion().numeroExterior + " " + ciclo.getDireccionReunion().numeroInterior + ", " + ciclo.getDireccionReunion().colonia;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaHead.addCell(cell);

            texto = "Plazo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.plazo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            cell.setColspan(8);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            cell.setColspan(10);
            tablaHead.addCell(cell);

            document.add(tablaHead);

            int col[] = {3, 24, 4, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7};
            tabla = new PdfPTable(13);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);
            texto = "No";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Nombre del cliente";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Asist";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Pago Semanal";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            int colInterna[] = {30, 30, 30};
            tablaInterna = new PdfPTable(3);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInterna);
            texto = "Crédito";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tablaInterna.addCell(cell);
            texto = "Saldo inicial";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Pago (A)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Saldo final";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tabla.addCell(cell);
            tablaInterna = new PdfPTable(3);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInterna);
            texto = "Garantías";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tablaInterna.addCell(cell);
            texto = "Saldo inicial";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Pago (B)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Saldo final";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tabla.addCell(cell);
            texto = "Multas (C)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Solidario (D)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Total de Pago (A+B+C+D)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            double montoTotalSinAjuste = 0;
            
            double montoAmortizacion = 0;
            if (tablaAmortizacion != null) {
                montoAmortizacion = tablaAmortizacion.getMontoPagar() + tablaAmortizacion.getTotalPagado();
            } else {
                TablaAmortizacionVO tablaAmortizacionCte[] = new TablaAmortizacionDAO().getElementos(ciclo.idGrupo, ciclo.idCiclo, 1);
                montoAmortizacion = tablaAmortizacionCte[1].montoPagar;
            }

            myLogger.info("Monto Amortizacion: "+montoAmortizacion);
            
            
            //Descartamos los integrantes del ciclo que no les corresponda pago en esta semana por ser interciclo
            IntegranteCicloVO [] integrantesFinales = descartaIntegrantes(ciclo, grupo, numPago);
            
            //Obtener Monto Total exacto y el monto individual de cada integrante
            //para que en la generacion del pdf se calcule monto individual en base al monto total de la amortizacion
            double [] pagosIndividuales = new double [integrantesFinales.length];
            double [] pagosAdicionales = new double [integrantesFinales.length];
            double montoTotalPagosAdicionales = 0;
            double montoTotalRedondeado = 0;
            boolean intercicloAdicional = false;
            for (int i = 0; i < integrantesFinales.length; i++) {
                if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO){
                    plazo = 14;//plazo para interciclo           
                    intercicloAdicional = true;
                }else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2){
                    plazo = 12;//plazo para interciclo   
                    intercicloAdicional = true;
                }else{
                    plazo = ciclo.getPlazo();
                }
         
                myLogger.debug("==== integrantesFinales[i].monto:" + integrantesFinales[i].monto);
                myLogger.debug("==== integrantesFinales[i].montoAdicional:" + integrantesFinales[i].montoAdicional);
                myLogger.debug("==== integrantesFinales[i].comision:" + integrantesFinales[i].comision);
                double montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                myLogger.debug("==== Obtencion montoSinComision:" + montoSinComision);
                
                myLogger.debug("==== integrantesFinales[i].montoConSeguro:" + integrantesFinales[i].montoConSeguro);
                myLogger.debug("==== integrantesFinales[i].montoAdicional:" + integrantesFinales[i].montoAdicional);
                if (integrantesFinales[i].montoConSeguro > 0) {
                    montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                }
                myLogger.debug("Despues del seguro Obtencion montoSinComision:" + montoSinComision);
                montoSinComision = FormatUtil.redondeaMoneda(montoSinComision);
                myLogger.debug("Despues rendondeo Obtencion montoSinComision:" + montoSinComision);
                double comisionSeguroRefinaciado = (integrantesFinales[i].monto-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
                myLogger.debug("antes del if comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                if (integrantesFinales[i].montoConSeguro > 0) {
                    comisionSeguroRefinaciado = (integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
                    myLogger.debug("dentro del if comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                }
                comisionSeguroRefinaciado = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado);
                myLogger.debug("==== comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
                
                interes = (montoSinComision + comisionSeguroRefinaciado) * interes;
                interes = FormatUtil.redondeaMoneda(interes);
                myLogger.debug("montoSinComision:"+ montoSinComision);
                myLogger.debug("comisionSeguroRefinaciado:"+ comisionSeguroRefinaciado);
                myLogger.debug("plazo:"+ plazo);
                double pagoIndividual = (montoSinComision + comisionSeguroRefinaciado + interes) / (plazo);
                
                int plazoAdi = 0;
                if(integrantesFinales[i].tipo_adicional!=0 ){
                    plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
                }
                myLogger.debug("Plazo Adicional desembolso :"+ (16-plazoAdi));
                if(plazoAdi > 0 && numPago > (16-plazoAdi)){
                    double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                    myLogger.debug("plazoAdi:"+ plazoAdi);
                    myLogger.debug("integrantesFinales[i].montoAdicional:"+ integrantesFinales[i].montoAdicional);
                    myLogger.debug("tasa.valor:"+ tasa.valor);
                    pagoIndividual +=Math.ceil(pagoIndivudualAdi);
                    pagosAdicionales[i]=Math.ceil(pagoIndivudualAdi);
                    montoTotalPagosAdicionales += Math.ceil(pagoIndivudualAdi);
                    myLogger.info("Pago adicional: "+Math.ceil(pagoIndivudualAdi));
                    intercicloAdicional = true;
                }else{
                    pagosAdicionales[i]=0;
                }
                
                montoTotalSinAjuste += Math.ceil(pagoIndividual);
//                montoTotal = Math.round(montoTotal);
//                pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                pagosIndividuales[i]=Math.ceil(pagoIndividual);
                myLogger.debug("Pago inividual antes: "+pagosIndividuales[i]);
            }
            
            myLogger.info("Monto Total SIN AJUSTE: "+montoTotalSinAjuste);
            
            double diferencia = montoAmortizacion - montoTotalSinAjuste;
            myLogger.info("Diferencia Montos: "+diferencia);
            int complemento = 0;
            if(((int)diferencia)==0){
                myLogger.info("Los montos cuadran, no se realiza ajuste!");
                
            }else if(((int)diferencia)<0){
                myLogger.info("Diferencia Negativa, no se realizara ajuste, solo el monto en PDF");
                montoAmortizacion=montoTotalSinAjuste;
                
            }else if(((int)diferencia)>0){
                myLogger.info("Diferencia Positiva");
                myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
                complemento = realizarAjusteMontos(integrantesFinales, pagosIndividuales, pagosAdicionales, montoTotalPagosAdicionales, montoAmortizacion, montoTotalSinAjuste, intercicloAdicional);
                myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");
                montoAmortizacion = montoAmortizacion + complemento;
            }
            
            
            
            

            //Redondeamos los pagos individuales para saber cual es el monto de los pagos
            //individuales redondeados
//            double montoTotalRedondeado = 0;
//            for (int i = 0; i < pagosIndividuales.length; i++) {
//                
//                //Regla de 3 para obtener monto correspondiente al monto de la amortizacion
//                double pagoIndividualCorrespondiente = montoAmortizacion * pagosIndividuales[i];
//                pagoIndividualCorrespondiente = pagoIndividualCorrespondiente/montoTotalSinAjuste; 
//                
//                double pagoRedondeado = Math.round(pagoIndividualCorrespondiente);
//   
//                pagosIndividuales[i]=pagoRedondeado;
//                montoTotalRedondeado += pagosIndividuales[i];
//                myLogger.debug("Pago inividual despues: "+pagosIndividuales[i]);
//            }
//            myLogger.info("Monto Total REDONDEADO: "+montoTotalRedondeado);
//            
//            double diferencia = montoAmortizacion - montoTotalRedondeado;
//            myLogger.info("Diferencia Montos: "+diferencia);
//            
//            int sum = 0;
//            //Distribuimos la diferencia entre los integrantes
//            if(diferencia>0){
//                myLogger.info("Dif positiva");
//                sum=1;
//            }else if(diferencia<0){
//                myLogger.info("Dif negativa");
//                diferencia=diferencia*(-1);
//                sum=-1;
//            }
//            myLogger.info("Dif final: "+diferencia);
//            for(int i = 0; i<diferencia; i++){
//                    pagosIndividuales[i]=pagosIndividuales[i]+sum;
//                    myLogger.info("Pago individual ajustado: "+pagosIndividuales[i]);
//            }
            
            for (int i = 0; i < integrantesFinales.length; i++) {
                double pagoFinal = pagosIndividuales[i];
                
                texto = "" + (i + 1);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "" + integrantesFinales[i].nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "$" + HTMLHelper.formatoMonto(pagoFinal);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
            }
            texto = "Totales";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(2);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "$" + HTMLHelper.formatoMonto(montoAmortizacion);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            document.add(tabla);

            int colFoot[] = {25, 25, 25, 25};
            tablaFoot = new PdfPTable(4);
            tablaFoot.setWidthPercentage(100);
            tablaFoot.setWidths(colFoot);
            texto = "\n\nAcuerdos del equipo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n_______________________________________________________________________________________________________________________\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaFoot.addCell(cell);
            texto = "\n ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n_______________________________________________________________________________________________________________________\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaFoot.addCell(cell);
            texto = "\nNota: Deberá ser llenado por la mesa directiva del Equipo\n      La mesa directiva deberá sacar una copia por cada pago semanal\n"
                    + "      El saldo inicial del crédito y garantía debe de ser el saldo final final de la semana anterior";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaFoot.addCell(cell);
            texto = "\nClaves Asist\nE = Envío     A = Asistencia     R = Retardo     I = Inasistencia";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nPresidenta";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nTesorera";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nSecretaria";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nAsesor";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            document.add(tablaFoot);
        } catch (Exception e) {
            myLogger.error("contenidoControlPagos", e);
        }
    }
    
    public int realizarAjusteMontos(IntegranteCicloVO [] integrantesFinales, double [] montosIndividuales, double [] montosAdicionales, double montoTotalAdicionales, double montoTotalCorrecto, double montoTotalSinAjuste, boolean intercicloAdicional){
        int complemento = 0;
        double diferencia = montoTotalCorrecto - montoTotalSinAjuste;
        
        if(intercicloAdicional){
            //En este proceso vamos a obtener el ultimo tipo de interciclo u adicional que hay
            //Para posteriormente obtener los indices de interciclos y adicional y mandarlos a realizar el ajuste
            int ultimoTipo = obtieneUltimosInterciclosAdicionales(integrantesFinales);
            List<Integer> indicesIntercicloAdicional = new ArrayList<Integer>();
            for(int i=0; i<integrantesFinales.length; i++){
                if(integrantesFinales[i].tipo == ultimoTipo || integrantesFinales[i].tipo_adicional == ultimoTipo){
                    indicesIntercicloAdicional.add(i);
                }
            }
            complemento = ajustarMontosSinRepetir(integrantesFinales, montosIndividuales, montosAdicionales, diferencia, indicesIntercicloAdicional);
        }else{
            List<Integer> indices = new ArrayList<Integer>();
            for(int i=0; i<integrantesFinales.length; i++){
                indices.add(i);
            }
            complemento = ajustarMontosSinRepetir(integrantesFinales, montosIndividuales, montosAdicionales, diferencia, indices);
        }
        return complemento;
    }
    
    /**
     * Metodo que sirve para obtener los montos sin repetir
     * 
     */
    public int ajustarMontosSinRepetir(IntegranteCicloVO [] integrantesFinales, double [] montosIndividuales, double [] montosAdicionales, double diferencia, List<Integer> indices){
        
        int complemento = 0;
        boolean indicesConMontoRepetido = false;
        
        for (int i : indices) {
            int repeticiones = obtenerNumeroMontosRepetidos(montosIndividuales, montosAdicionales, montosIndividuales[i], montosAdicionales[i], indices);
            
            if(repeticiones>1){
                indicesConMontoRepetido = true;
            }
        }
        
        if(indicesConMontoRepetido){
            myLogger.info("Hay montos que se repiten");
            myLogger.info("Indice size: "+indices.size());
            int modulo = ((int)diferencia)%indices.size();
            myLogger.info("Modulo: "+modulo);
            complemento = indices.size() - modulo;
            myLogger.info("complemento: "+complemento);
            diferencia = diferencia + complemento;
            myLogger.info("diferencia nueva: "+diferencia);
            
        }else{
            myLogger.info("Todos los montos son diferentes...");
        }
                
        myLogger.info("Montos individuales size: "+montosIndividuales.length);
        myLogger.info("Indices size: "+indices.size());
        
        int i = 0;
        while(diferencia>0) {
            montosIndividuales[indices.get(i)] = montosIndividuales[indices.get(i)] + 1;
            diferencia = diferencia - 1;

            if(i < indices.size()-1){
                i++;
            }else{
                i=0;
            }
        }
        
        return complemento;
    }
    
    public int obtenerNumeroMontosRepetidos(double [] montosIndividuales, double [] montosAdicionales, double montoIndividual, double montoAdicional, List<Integer> indices){
        
        int numeroRepeticiones = 0;
        myLogger.info("Monto: "+montoIndividual+" adicional: "+montoAdicional);
        for (int i : indices) {
//            myLogger.info("Monto: "+montosIndividuales[i]+" adicional: "+montosAdicionales[i]);
            if(montosIndividuales[i] == montoIndividual && montosAdicionales[i] == montoAdicional){
                numeroRepeticiones++;
                myLogger.info("Se encuentra monto repetido: "+numeroRepeticiones);
            }
        }
        
        return numeroRepeticiones;
    }
    
    /**
     * Metodo que obtiene el ultimo tipo de interciclo o ultimo tipo de adicional
     * 
     * @param integrantesFinales
     * 
     * @param interciclo indica que los integrantes son de interciclo
     * 
     * @param adicional indica que los integrantes son de adicional
     * 
     * @return 
     */
    public int obtieneUltimosInterciclosAdicionales(IntegranteCicloVO [] integrantesFinales){
        int semana = 0;
        int tipo = 0;
        
        Map<Integer, Integer> semanas = new HashMap<Integer, Integer>();
        semanas.put(ClientesConstants.TIPO_CLIENTE_INTERCICLO, 2);
        semanas.put(ClientesConstants.TIPO_CLIENTE_INTERCICLO_2, 4);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_4, 4);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_5, 5);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_6, 6);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_7, 7);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_8, 8);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_9, 9);
        semanas.put(ClientesConstants.TIPO_CLIENTE_ADICONAL_10, 10);
        
        
        for(IntegranteCicloVO integrante : integrantesFinales){
            
            int semInterciclo = (semanas.containsKey(integrante.tipo))?semanas.get(integrante.tipo):0;
            int semAdicional = (semanas.containsKey(integrante.tipo_adicional))?semanas.get(integrante.tipo_adicional):0;
            
            if(semInterciclo>0 && semInterciclo>semana){
                semana=semInterciclo;
                tipo = integrante.tipo;
            }else if(semAdicional>0 && semAdicional>semana){
                semana=semAdicional;
                tipo = integrante.tipo_adicional;
            }
        }
        
        myLogger.debug("Ultimo tipo de interciclo u adicional: "+tipo);
        return tipo;
    }
    
    /**
     * Descarta los integrantes que son de interciclo que no les corresponde la semana
     * del control de pagos que se esta generando
     * 
     * @param integrantesTotales son los integrantes totales del ciclo
     * 
     * @return integrantes que corresponden unicamente a la semana del control de pagos
     */
    public IntegranteCicloVO [] descartaIntegrantes(CicloGrupalVO ciclo, GrupoVO grupo, int semana) throws ClientesException{
        List<IntegranteCicloVO> integrantesCorrespondientes = new ArrayList<IntegranteCicloVO>();

        myLogger.info("Semana Actual: "+semana);
        
        for(IntegranteCicloVO ic : ciclo.integrantes){
            if(ic.estatus == ClientesConstants.INTEGRANTE_CANCELADO){
                myLogger.info("Integrante Cancelado: "+ic.getIdCliente());
                OrdenDePagoDAO ordenPago = new OrdenDePagoDAO();
                OrdenDePagoVO orden = ordenPago.getOrdenPago(ic.getIdCliente(), ic.getIdSolicitud());
                myLogger.info("Orden de pago: "+orden);
                if(orden != null && orden.getEstatus() == ClientesConstants.OP_DEVUELTA){
                    myLogger.info("Orden de pago ref: "+orden.getReferencia());
//                    falta validar en que semana se hizo la devolucion
                    TablaAmortDAO tablaDao = new TablaAmortDAO();

                    TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
                    myLogger.info("Size amort: "+tablasAmor.length);
                    int numPago = 0;
                    for(TablaAmortVO ta : tablasAmor){
                        myLogger.info("monto anticipado: "+ta.capitalAnticipado);
                        if(ta.capitalAnticipado>0){
                            myLogger.info("Descarta por la cantidad: "+ta.getCapitalAnticipado()+" en el num pago: "+ta.getNumPago());
                            numPago = ta.getNumPago();
                            break;
                        }
                    }
                    
                    if(numPago>0 && semana >= numPago){
                        myLogger.info("Descarta integrante CANCELADO del control de pagos");
                        continue;
                    }
                    
                }else{
                    myLogger.info("No tiene orden de pago o aun no esta devuelta");
                }
            }else if((ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO && semana < 3)
                    || (ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 && semana < 5)){
                myLogger.info("Descarta integrante de interciclo ya que esta semana no le corresponde pago");
                continue;
            }
            integrantesCorrespondientes.add(ic);
        }
        
        IntegranteCicloVO [] integrantesFinales = new IntegranteCicloVO [integrantesCorrespondientes.size()];
        for(int i = 0; i < integrantesCorrespondientes.size(); i++){
            integrantesFinales[i]=integrantesCorrespondientes.get(i);
        }
        
        return integrantesFinales;
    }
    
    private Calendar obtenerFechaDiaReunion(int dia, Date fecSigAmort){
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecSigAmort);
        boolean fechaEncontrada = false;
        do{
            myLogger.info("fecha sig amort: "+cal.getTime());
            myLogger.info("compare: "+(dia+1)+" - "+cal.get(Calendar.DAY_OF_WEEK));
            if(dia+1==cal.get(Calendar.DAY_OF_WEEK)){
                fechaEncontrada=true;
            }else{
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
        }while(!fechaEncontrada);
        return cal;
    }
    
    private void contenidoCartaRenovacion(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {
        myLogger.info("Generar Carta de renovacion contenido...");
        try{
            CatalogoDAO paramDAO = new CatalogoDAO();
            
            Paragraph p = null;
            PdfPTable tabla = null;
            PdfPCell cell = null;
            int plazoActual = ciclo.saldo.getNumeroCuotasTranscurridas();
            
            Calendar fechaReunion = obtenerFechaDiaReunion(ciclo.getDiaReunion(), ciclo.saldo.getFechaSigAmortizacion());
            int diaReunion = fechaReunion.get(Calendar.DAY_OF_MONTH);
            int numMes = fechaReunion.get(Calendar.MONTH)+1;
            String numMesString = (numMes<10)?"0"+numMes:String.valueOf(numMes);
            String mesReunion = FechasUtil.obtenNombreMes("0/"+(numMesString)+"/0");
            int anioReunion = fechaReunion.get(Calendar.YEAR);
            String nombreEquipo = grupo.getNombre();
            String sucursal = ciclo.saldo.getNombreSucursal();
            String texto = "";
            String presidenta = "";
            String secretaria = "";
            String tesorera = "";
            for(IntegranteCicloVO ic : ciclo.integrantes){
                presidenta = (ic.rol==3)?"PRESIDENTA "+ic.getNombre():presidenta;
                secretaria = (ic.rol==2)?"SECRETARIA "+ic.getNombre():secretaria;
                tesorera = (ic.rol==1)?"TESORERA "+ic.getNombre():tesorera;
            }
            
            
            texto = "\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            document.add(p);
            
            ParametroVO paramVO = paramDAO.getParametro("RUTA_IMAGEN_REPORTE");
            //Image logoCrediequipos = Image.getInstance(paramVO.valor);
            Image logoCrediequipos = Image.getInstance(paramVO.valor);
            logoCrediequipos.scalePercent(20);
            logoCrediequipos.setAlignment(Element.ALIGN_RIGHT);
            document.add(logoCrediequipos);
            
            texto = "\nA "+diaReunion+" de "+mesReunion+" del "+anioReunion;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_RIGHT);
            document.add(p);
            
            texto = "\nIntegrantes del equipo: "+nombreEquipo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_LEFT);
            document.add(p);
//            
            texto = "\n\n¡Felicidades! Gracias al desempeño mostrado en este ciclo, tu equipo se encuentra en proceso de ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.add(new Chunk("renovación", new Font(Font.HELVETICA, 12, Font.BOLD)));
            texto = ", es decir, por su buen comportamiento de pago puntual pueden obtener su siguiente crédito.";
            texto += " ¡Podrán continuar con la inversión en sus negocios!\n";
            texto += " El día de hoy es su semana "; 
            p.add(new Chunk(texto, new Font(Font.HELVETICA, 12, Font.NORMAL)));
            p.add(new Chunk(String.valueOf(plazoActual+1), new Font(Font.HELVETICA, 12, Font.BOLD)));
            texto = " por lo que podemos empezar con el proceso.\n\n";
            p.add(new Chunk(texto, new Font(Font.HELVETICA, 12, Font.NORMAL)));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto = "¿Qué necesitan para renovar e ingresar a tu ciclo "+String.valueOf(plazoActual+1)+"?\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto = "       • Actualizar sus datos y entregar la tu documentación (en caso de ser necesario).\n";
            texto +="        • Solicitar tu nuevo crédito y firmar tu solicitud de crédito. \n";
            texto +="        • Invitar a más integrantes.\n\n";
            
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto = "Trabaja en conjunto con tu asesor para seguir obteniendo beneficios como: \n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto = "       • Crecimiento en el monto de su crédito.\n";
            texto +="        • Posibilidad de la disminución de su tasa de interés de acuerdo al comportamiento y\n"
                    + "       características del Equipo.\n\n";
            
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto = "En Contigo sabemos que eres el sustento de tu familia, por eso te invitamos a seguir obteniendo nuestros beneficios al renovar tu crédito.\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_JUSTIFIED);
            document.add(p);
            
            texto ="¡Pregunta a tu asesor!\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            
            texto ="Atentamente\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 11, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            
            texto = "_____________________________";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 11, Font.NORMAL));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
            texto = "Contigo "+sucursal+"\n\n\n\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 11, Font.BOLD));
            p.setAlignment(Chunk.ALIGN_CENTER);
            document.add(p);
//
            int col[] = {60, 40};
            tabla = new PdfPTable(2);
            tabla.setWidthPercentage(90);
            tabla.setWidths(col);
//            
            texto = "Nombre";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
    
            texto = "Firma";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            texto = presidenta;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            p = new Paragraph("", new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            texto = secretaria;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            p = new Paragraph("", new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            texto = tesorera;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            p = new Paragraph("", new Font(Font.HELVETICA, 10, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
            
            document.add(tabla);
        }catch(Exception e){
            myLogger.error("contenidoCartaRenovacion", e);
        }
    }
    
    private void contenidoControlPagosAnterior(GrupoVO grupo, CicloGrupalVO ciclo, HttpServletResponse response) {

        try {
            Paragraph p = null;
            Chunk ck = null;
            PdfPTable tabla = null;
            PdfPTable tablaInterna = null;
            PdfPTable tablaHead = null;
            PdfPTable tablaFoot = null;
            PdfPCell cell = null;
            String texto = "";
            TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
            TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
            TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
            Color grayColor = new Color(0x5A5959);
            CatalogoDAO paramDAO = new CatalogoDAO();
            ParametroVO paramVO = new ParametroVO();
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            SaldoIBSDAO saldo = new SaldoIBSDAO();
            Calendar calendario = Calendar.getInstance();
            Date fechaActual = Convertidor.toSqlDate(calendario.getTime());
            paramVO = paramDAO.getParametro("RUTA_IMAGEN_REPORTE");
            //Image logoCrediequipos = Image.getInstance(paramVO.valor);
            Image logoCrediequipos = Image.getInstance(paramVO.valor);
            logoCrediequipos.scalePercent(20);
            int plazo = ciclo.getPlazo();
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
            TasaInteresVO tasa = (TasaInteresVO) catTasas.get(new Integer(ciclo.tasa));
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
            TablaAmortVO tablaAmortizacion = new TablaAmortVO();
            TablaAmortizacionVO tablaAmortizacionclie = new TablaAmortizacionVO();
            TablaAmortDAO tablaDao = new TablaAmortDAO();
            tablaAmortizacion = tablaDao.getDivVigente(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            if (tablaAmortizacion == null) {
                tablaAmortizacion = tablaDao.getUltmioDivPagado(ciclo.getIdGrupo(), ciclo.getIdCreditoIBS());
            }
            if (tablaAmortizacion == null) {
                tablaAmortizacionclie = tablaDao.getElementos(ciclo.getIdGrupo(), ciclo.getIdCiclo());
            }
            int colHead[] = {8, 10, 1, 10, 20, 1, 14, 10, 1, 25};
            tablaHead = new PdfPTable(10);
            tablaHead.setWidthPercentage(100);
            tablaHead.setWidths(colHead);
            texto = "Control de Pagos y Garantías Semanales\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 12, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(7);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            cell = new PdfPCell(logoCrediequipos);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "Equipo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = grupo.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Fecha: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            if (tablaAmortizacion != null) {
                texto = "" + Convertidor.dateToString(tablaAmortizacion.getFechaPago());
            } else {
                texto = "" + Convertidor.dateToString(tablaAmortizacionclie.fechaPago);
            }

            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaHead.addCell(cell);
            texto = "Día de Reunión: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.diaReunion, catDiasReunon);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);

            texto = "Sucursal: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = sucursal.nombre;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Asesor: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.asesor, catEjecutivos);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaHead.addCell(cell);
            texto = "Hora: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + CatalogoHelper.getDescripcionEstatus(ciclo.horaReunion, catHorasReunion);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);

            texto = "Ciclo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.idCiclo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "No. de Pago: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            int numPago = 1;
            if (tablaAmortizacion != null) {
                numPago = tablaAmortizacion.getNumPago();
                texto = "" + numPago;
            } else {
                texto = String.valueOf(numPago);
            }
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto += "Domicilio de Reunión: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.getDireccionReunion().calle + " " + ciclo.getDireccionReunion().numeroExterior + " " + ciclo.getDireccionReunion().numeroInterior + ", " + ciclo.getDireccionReunion().colonia;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaHead.addCell(cell);

            texto = "Plazo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "" + ciclo.plazo;
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            cell.setColspan(8);
            tablaHead.addCell(cell);
            texto = "";
            cell = new PdfPCell();
            cell.setBorderWidth(0);
            cell.setColspan(10);
            tablaHead.addCell(cell);

            document.add(tablaHead);

            int col[] = {3, 24, 4, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7};
            tabla = new PdfPTable(13);
            tabla.setWidthPercentage(100);
            tabla.setWidths(col);
            texto = "No";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Nombre del cliente";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Asist";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Pago Semanal";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            int colInterna[] = {30, 30, 30};
            tablaInterna = new PdfPTable(3);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInterna);
            texto = "Crédito";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tablaInterna.addCell(cell);
            texto = "Saldo inicial";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Pago (A)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Saldo final";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tabla.addCell(cell);
            tablaInterna = new PdfPTable(3);
            tablaInterna.setWidthPercentage(100);
            tablaInterna.setWidths(colInterna);
            texto = "Garantías";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tablaInterna.addCell(cell);
            texto = "Saldo inicial";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Pago (B)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            texto = "Saldo final";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tablaInterna.addCell(cell);
            cell = new PdfPCell(tablaInterna);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(3);
            tabla.addCell(cell);
            texto = "Multas (C)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Solidario (D)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "Total de Pago (A+B+C+D)";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            double montoTotalSinAjuste = 0;
            
            double montoAmortizacion = 0;
            if (tablaAmortizacion != null) {
                montoAmortizacion = tablaAmortizacion.getMontoPagar() + tablaAmortizacion.getTotalPagado();
            } else {
                TablaAmortizacionVO tablaAmortizacionCte[] = new TablaAmortizacionDAO().getElementos(ciclo.idGrupo, ciclo.idCiclo, 1);
                montoAmortizacion = tablaAmortizacionCte[1].montoPagar;
            }

            myLogger.info("Monto Amortizacion: "+montoAmortizacion);
            
            
            //Descartamos los integrantes del ciclo que no les corresponda pago en esta semana por ser interciclo
            IntegranteCicloVO [] integrantesFinales = descartaIntegrantesAnterior(ciclo, grupo, numPago);
            
            //Obtener Monto Total exacto y el monto individual de cada integrante
            //para que en la generacion del pdf se calcule monto individual en base al monto total de la amortizacion
            double [] pagosIndividuales = new double [integrantesFinales.length];
            double [] pagosAdicionales = new double [integrantesFinales.length];
            double montoTotalPagosAdicionales = 0;
            for (int i = 0; i < integrantesFinales.length; i++) {
                if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)
                    plazo = 14;//plazo para interciclo                            
                else if (integrantesFinales[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2)
                    plazo = 12;//plazo para interciclo   
                else
                    plazo = ciclo.getPlazo();
         
                myLogger.debug("==== integrantesFinales[i].monto:" + integrantesFinales[i].monto);
                myLogger.debug("==== integrantesFinales[i].montoAdicional:" + integrantesFinales[i].montoAdicional);
                myLogger.debug("==== integrantesFinales[i].comision:" + integrantesFinales[i].comision);
                double montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].monto-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                myLogger.debug("==== Obtencion montoSinComision:" + montoSinComision);
                
                myLogger.debug("==== integrantesFinales[i].montoConSeguro:" + integrantesFinales[i].montoConSeguro);
                myLogger.debug("==== integrantesFinales[i].montoAdicional:" + integrantesFinales[i].montoAdicional);
                if (integrantesFinales[i].montoConSeguro > 0) {
                    montoSinComision = ClientesUtil.calculaMontoSinComision((integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional), integrantesFinales[i].comision, catComisiones);
                }
                myLogger.debug("Despues del seguro Obtencion montoSinComision:" + montoSinComision);
                montoSinComision = FormatUtil.redondeaMoneda(montoSinComision);
                myLogger.debug("Despues rendondeo Obtencion montoSinComision:" + montoSinComision);
                double comisionSeguroRefinaciado = (integrantesFinales[i].monto-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
                myLogger.debug("antes del if comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                if (integrantesFinales[i].montoConSeguro > 0) {
                    comisionSeguroRefinaciado = (integrantesFinales[i].montoConSeguro-integrantesFinales[i].montoAdicional) + ClientesUtil.calculaMontoConComision(integrantesFinales[i].primaSeguro, integrantesFinales[i].comision, catComisiones) + integrantesFinales[i].montoRefinanciado - montoSinComision;
                    myLogger.debug("dentro del if comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                }
                comisionSeguroRefinaciado = FormatUtil.redondeaMoneda(comisionSeguroRefinaciado);
                myLogger.debug("==== comisionSeguroRefinaciado:" + comisionSeguroRefinaciado);
                double interes = ((((tasa.valor * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
                
                interes = (montoSinComision + comisionSeguroRefinaciado) * interes;
                interes = FormatUtil.redondeaMoneda(interes);
                myLogger.debug("montoSinComision:"+ montoSinComision);
                myLogger.debug("comisionSeguroRefinaciado:"+ comisionSeguroRefinaciado);
                myLogger.debug("plazo:"+ plazo);
                double pagoIndividual = (montoSinComision + comisionSeguroRefinaciado + interes) / (plazo);
                
                int plazoAdi = 0;
                if(integrantesFinales[i].tipo_adicional!=0 ){
                    plazoAdi = plazo-AdicionalUtil.plazoAdicional(integrantesFinales[i].tipo_adicional);
                }
                myLogger.debug("Plazo Adicional desembolso :"+ (16-plazoAdi));
                if(plazoAdi > 0 && numPago > (16-plazoAdi)){
                    double pagoIndivudualAdi = AdicionalUtil.calculaAdicionalSemanal(integrantesFinales[i].montoAdicional, tasa.valor , plazoAdi);
                    myLogger.debug("plazoAdi:"+ plazoAdi);
                    myLogger.debug("integrantesFinales[i].montoAdicional:"+ integrantesFinales[i].montoAdicional);
                    myLogger.debug("tasa.valor:"+ tasa.valor);
                    pagoIndividual +=pagoIndivudualAdi;
                    pagosAdicionales[i]=pagoIndivudualAdi;
                    montoTotalPagosAdicionales += pagoIndivudualAdi;
                    myLogger.info("Pago adicional: "+pagoIndivudualAdi);
                }else{
                    pagosAdicionales[i]=0;
                }
                
                montoTotalSinAjuste += pagoIndividual;
//                montoTotal = Math.round(montoTotal);
//                pagoIndividual = FormatUtil.redondeaMoneda(pagoIndividual);
                pagosIndividuales[i]=pagoIndividual;
                myLogger.debug("Pago inividual antes: "+pagosIndividuales[i]);
            }
            
            myLogger.info("Monto Total SIN AJUSTE: "+montoTotalSinAjuste);

            myLogger.info("*****************Inicia Ajuste REDONDEO PAGOS SEMANAL****************");
            realizarAjusteRedondeoAnterior(integrantesFinales, pagosIndividuales, pagosAdicionales, montoTotalPagosAdicionales, montoAmortizacion, montoTotalSinAjuste);
            myLogger.info("*****************Finaliza Ajuste REDONDEO PAGOS SEMANAL****************");
            //Redondeamos los pagos individuales para saber cual es el monto de los pagos
            //individuales redondeados
//            double montoTotalRedondeado = 0;
//            for (int i = 0; i < pagosIndividuales.length; i++) {
//                
//                //Regla de 3 para obtener monto correspondiente al monto de la amortizacion
//                double pagoIndividualCorrespondiente = montoAmortizacion * pagosIndividuales[i];
//                pagoIndividualCorrespondiente = pagoIndividualCorrespondiente/montoTotalSinAjuste; 
//                
//                double pagoRedondeado = Math.round(pagoIndividualCorrespondiente);
//   
//                pagosIndividuales[i]=pagoRedondeado;
//                montoTotalRedondeado += pagosIndividuales[i];
//                myLogger.debug("Pago inividual despues: "+pagosIndividuales[i]);
//            }
//            myLogger.info("Monto Total REDONDEADO: "+montoTotalRedondeado);
//            
//            double diferencia = montoAmortizacion - montoTotalRedondeado;
//            myLogger.info("Diferencia Montos: "+diferencia);
//            
//            int sum = 0;
//            //Distribuimos la diferencia entre los integrantes
//            if(diferencia>0){
//                myLogger.info("Dif positiva");
//                sum=1;
//            }else if(diferencia<0){
//                myLogger.info("Dif negativa");
//                diferencia=diferencia*(-1);
//                sum=-1;
//            }
//            myLogger.info("Dif final: "+diferencia);
//            for(int i = 0; i<diferencia; i++){
//                    pagosIndividuales[i]=pagosIndividuales[i]+sum;
//                    myLogger.info("Pago individual ajustado: "+pagosIndividuales[i]);
//            }
            
            for (int i = 0; i < integrantesFinales.length; i++) {
                double pagoFinal = pagosIndividuales[i];
                
                texto = "" + (i + 1);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "" + integrantesFinales[i].nombre;
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "$" + HTMLHelper.formatoMonto(pagoFinal);
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
                texto = "\n";
                p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(grayColor);
                tabla.addCell(cell);
            }
            texto = "Totales";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            cell.setColspan(2);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "$" + HTMLHelper.formatoMonto(montoAmortizacion);
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            texto = "\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderColor(grayColor);
            tabla.addCell(cell);
            document.add(tabla);

            int colFoot[] = {25, 25, 25, 25};
            tablaFoot = new PdfPTable(4);
            tablaFoot.setWidthPercentage(100);
            tablaFoot.setWidths(colFoot);
            texto = "\n\nAcuerdos del equipo: ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n_______________________________________________________________________________________________________________________\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaFoot.addCell(cell);
            texto = "\n ";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n_______________________________________________________________________________________________________________________\n";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(3);
            tablaFoot.addCell(cell);
            texto = "\nNota: Deberá ser llenado por la mesa directiva del Equipo\n      La mesa directiva deberá sacar una copia por cada pago semanal\n"
                    + "      El saldo inicial del crédito y garantía debe de ser el saldo final final de la semana anterior";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaFoot.addCell(cell);
            texto = "\nClaves Asist\nE = Envío     A = Asistencia     R = Retardo     I = Inasistencia";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.BOLD));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            cell.setColspan(2);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nPresidenta";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nTesorera";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nSecretaria";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            texto = "\n\n\n\n\n________________________\nAsesor";
            p = new Paragraph(texto, new Font(Font.HELVETICA, 8, Font.NORMAL));
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0);
            tablaFoot.addCell(cell);
            document.add(tablaFoot);
        } catch (Exception e) {
            myLogger.error("contenidoControlPagos", e);
        }
    }
    
    public void realizarAjusteRedondeoAnterior(IntegranteCicloVO [] integrantesFinales, double [] montosIndividuales, double [] montosAdicionales, double montoTotalAdicionales, double montoTotalCorrecto, double montoTotalSinAjuste ){
        double interesTotalRedondeado = 0;
        if(montoTotalAdicionales==0){
            myLogger.info("SIN ADICIONAL********************");
            for (int i = 0; i < integrantesFinales.length; i++) {
                //Regla de 3 para obtener interes correspondiente al interes de la amortizacion
               double interesIndividualCorrespondiente = montoTotalCorrecto * montosIndividuales[i];
               interesIndividualCorrespondiente = interesIndividualCorrespondiente/montoTotalSinAjuste;
               double interesRedondeado = Math.round(interesIndividualCorrespondiente);

               montosIndividuales[i]=interesRedondeado;
               interesTotalRedondeado += montosIndividuales[i];
               myLogger.debug("monto inividual despues: "+montosIndividuales[i]);
           }

            myLogger.info("Monto Total REDONDEADO: "+interesTotalRedondeado);

            double diferencia = montoTotalCorrecto - interesTotalRedondeado;
            myLogger.info("Diferencia Montos: "+diferencia);

            int sum = 0;
            //Distribuimos la diferencia entre los integrantes
            if(diferencia>0){
                myLogger.info("Dif positiva");
                sum=1;
            }else if(diferencia<0){
                myLogger.info("Dif negativa");
                diferencia=diferencia*(-1);
                sum=-1;
            }
            myLogger.info("Dif final: "+diferencia);
            for(int i = 0; i<diferencia; i++){
                    montosIndividuales[i]=montosIndividuales[i]+sum;
                    myLogger.info("monto individual ajustado: "+montosIndividuales[i]);
            }

        }else{
            myLogger.info("CON ADICIONAL********************");
            double interesTmpRedondeado = 0;
            for (int i = 0; i < integrantesFinales.length; i++) {
                montosIndividuales[i]=Math.round(montosIndividuales[i]);
                interesTmpRedondeado +=  montosIndividuales[i];
            }
            myLogger.info("Monto sin ajuste redondeado: "+interesTmpRedondeado);
            double diferenciaIntereses = montoTotalCorrecto - interesTmpRedondeado;
            myLogger.info("Dif correcto - redondeado adicional: "+diferenciaIntereses);
            double totalMontoAdicionalRedondeado = 0;
            for (int i = 0; i < integrantesFinales.length; i++) {
                if(montosAdicionales[i]>0){
                    double porcentajeInteresAdicional = montosAdicionales[i]/montoTotalAdicionales;
                    double interesCorrespondiente = porcentajeInteresAdicional*diferenciaIntereses;
                    montosAdicionales[i]=Math.round(interesCorrespondiente);
                    totalMontoAdicionalRedondeado += montosAdicionales[i];
                    myLogger.info("Monto correspondiente: "+interesCorrespondiente);
                }
            }
            myLogger.info("Total monto adicional redondeado:  "+totalMontoAdicionalRedondeado);
            double diferencia = diferenciaIntereses - totalMontoAdicionalRedondeado;
            myLogger.info("diferencia: "+diferencia);
            int sum = 0;
            //Distribuimos la diferencia entre los integrantes
            if(diferencia>0){
                myLogger.info("Dif positiva");
                sum=1;
            }else if(diferencia<0){
                myLogger.info("Dif negativa");
                diferencia=diferencia*(-1);
                sum=-1;
            }

            int i = 0;
            while(diferencia!=0&&i<montosAdicionales.length) {
                if(montosAdicionales[i]!=0){
                    montosAdicionales[i] = montosAdicionales[i] + sum;
                    diferencia = diferencia - 1;
                }
                i++;
            }
            for (int j = 0; j < integrantesFinales.length; j++) {
                montosIndividuales[j]=montosIndividuales[j]+montosAdicionales[j];
                myLogger.info("Monto individual despues: "+montosIndividuales[j]);
            }
        }
    }
    
    /**
     * Descarta los integrantes que son de interciclo que no les corresponde la semana
     * del control de pagos que se esta generando
     * 
     * @param integrantesTotales son los integrantes totales del ciclo
     * 
     * @return integrantes que corresponden unicamente a la semana del control de pagos
     */
    public IntegranteCicloVO [] descartaIntegrantesAnterior(CicloGrupalVO ciclo, GrupoVO grupo, int semana) throws ClientesException{
        List<IntegranteCicloVO> integrantesCorrespondientes = new ArrayList<IntegranteCicloVO>();

        myLogger.info("Semana Actual: "+semana);
        
        for(IntegranteCicloVO ic : ciclo.integrantes){
            if(ic.estatus == ClientesConstants.INTEGRANTE_CANCELADO){
                myLogger.info("Integrante Cancelado: "+ic.getIdCliente());
                OrdenDePagoDAO ordenPago = new OrdenDePagoDAO();
                OrdenDePagoVO orden = ordenPago.getOrdenPago(ic.getIdCliente(), ic.getIdSolicitud());
                myLogger.info("Orden de pago: "+orden);
                if(orden != null && orden.getEstatus() == ClientesConstants.OP_DEVUELTA){
                    myLogger.info("Orden de pago ref: "+orden.getReferencia());
//                    falta validar en que semana se hizo la devolucion
                    TablaAmortDAO tablaDao = new TablaAmortDAO();

                    TablaAmortVO [] tablasAmor = tablaDao.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
                    myLogger.info("Size amort: "+tablasAmor.length);
                    int numPago = 0;
                    for(TablaAmortVO ta : tablasAmor){
                        myLogger.info("monto anticipado: "+ta.capitalAnticipado);
                        if(ta.capitalAnticipado>0){
                            myLogger.info("Descarta por la cantidad: "+ta.getCapitalAnticipado()+" en el num pago: "+ta.getNumPago());
                            numPago = ta.getNumPago();
                            break;
                        }
                    }
                    
                    if(numPago>0 && semana >= numPago){
                        myLogger.info("Descarta integrante CANCELADO del control de pagos");
                        continue;
                    }
                    
                }else{
                    myLogger.info("No tiene orden de pago o aun no esta devuelta");
                }
            }else if((ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO && semana < 3)
                    || (ic.tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2 && semana < 5)){
                myLogger.info("Descarta integrante de interciclo ya que esta semana no le corresponde pago");
                continue;
            }
            integrantesCorrespondientes.add(ic);
        }
        
        IntegranteCicloVO [] integrantesFinales = new IntegranteCicloVO [integrantesCorrespondientes.size()];
        for(int i = 0; i < integrantesCorrespondientes.size(); i++){
            integrantesFinales[i]=integrantesCorrespondientes.get(i);
        }
        
        return integrantesFinales;
    }
    
}
